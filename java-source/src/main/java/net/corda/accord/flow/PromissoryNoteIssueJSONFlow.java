package net.corda.accord.flow;

import co.paralleluniverse.fibers.Suspendable;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.corda.accord.AccordUtils;
import net.corda.accord.contract.PromissoryNoteCordaContract;
import net.corda.accord.state.PromissoryNoteState;
import net.corda.core.contracts.Command;
import net.corda.core.contracts.CommandData;
import net.corda.core.contracts.ContractState;
import net.corda.core.crypto.SecureHash;
import net.corda.core.flows.*;
import net.corda.core.identity.CordaX500Name;
import net.corda.core.identity.Party;
import net.corda.core.transactions.SignedTransaction;
import net.corda.core.transactions.TransactionBuilder;
import static net.corda.core.contracts.ContractsDSL.requireThat;
import net.corda.core.utilities.ProgressTracker;
import net.corda.core.utilities.ProgressTracker.Step;

import org.accordproject.promissorynote.PromissoryNoteContract;

/**
 * This is the flow which handles issuance of new promissory notes from a legal document.
 * The flow returns the [SignedTransaction] that was committed to the ledger.
 */
public class PromissoryNoteIssueJSONFlow {

    @InitiatingFlow
    @StartableByRPC
    @StartableByService
    public static class InitiatorFlow extends FlowLogic<SignedTransaction> {

        private final String contractText;
        private final String jsonData;

        public InitiatorFlow(String contractText, String jsonData) {

            this.contractText = contractText;
            this.jsonData = jsonData;
        }

    /** TODO: Enable the user to specify the file path for the promissory note template
     * TODO: Adjust the cicero-parse command to include an option on only return the JSON with no initial messaging
     * TODO: Enable the user to specify the file path for the source legal document */

    // Giving our flow a progress tracker allows us to see the flow's
    // progress visually in our node's CRaSH shell.
    private static final Step GENERATING_STATE_FROM_CONTRACT = new Step("Generating a state using Cicero CLI Functionality to Parse a legal document.");
    private static final Step PARSING_LEGALESE = new Step("Parsing Legalese");
    private static final Step GETTING_LAW_DEGREE = new Step("Getting Law Degree");
    private static final Step MAKING_PARENTS_HAPPY = new Step("Making Parents Happy");
    private static final Step RETRIEVING_NOTARY_IDENTITY = new Step("Getting a reference to the notary node.");
    private static final Step TX_BUILDING = new Step("Building a transaction.");
    private static final Step TX_SIGNING = new Step("Signing a transaction.");
    private static final Step TX_VERIFICATION = new Step("Verifying a transaction.");
    private static final Step SIGNATURE_GATHERING = new Step("Gathering a transaction's signatures.") {
      // Wiring up a child progress tracker allows us to see the
      // subflow's progress steps in our flow's progress tracker.
      @Override public ProgressTracker childProgressTracker() {
        return CollectSignaturesFlow.Companion.tracker();
      }
    };
    private static final Step FINALISATION = new Step("Finalising a transaction.") {
      @Override public ProgressTracker childProgressTracker() {
        return FinalityFlow.Companion.tracker();
      }
    };

    private final ProgressTracker progressTracker = new ProgressTracker(
        GENERATING_STATE_FROM_CONTRACT,
        PARSING_LEGALESE,
        GETTING_LAW_DEGREE,
        MAKING_PARENTS_HAPPY,
        RETRIEVING_NOTARY_IDENTITY,
        TX_BUILDING,
        TX_VERIFICATION,
        TX_SIGNING,
        SIGNATURE_GATHERING,
        FINALISATION
    );

    @Override
    public ProgressTracker getProgressTracker() {
      return progressTracker;
    }

        @Suspendable
        @Override
        public SignedTransaction call() throws FlowException {

          PromissoryNoteState state;

      // Step 0. Generate an input state from the parsed Contract.
      progressTracker.setCurrentStep(GENERATING_STATE_FROM_CONTRACT);
      try {
        progressTracker.setCurrentStep(GETTING_LAW_DEGREE);

        ObjectMapper objectMapper = new ObjectMapper();
        progressTracker.setCurrentStep(PARSING_LEGALESE);
        PromissoryNoteContract parsedContractData = objectMapper.readValue(jsonData, org.accordproject.promissorynote.PromissoryNoteContract.class);

        // Get the relevant Corda parties from the network map using parsed contract data.
        Party maker = getServiceHub().getNetworkMapCache().getPeerByLegalName(new CordaX500Name(parsedContractData.getMaker(), "NY", "US"));
        Party lender = getServiceHub().getNetworkMapCache().getPeerByLegalName(new CordaX500Name(parsedContractData.getLender(), "NY", "US"));
        state = new PromissoryNoteState(parsedContractData, contractText, maker, lender);
        progressTracker.setCurrentStep(MAKING_PARENTS_HAPPY);
      } catch (Exception e) {
        throw new FlowException("Error loading contract data" + e.toString());
      }

            // Step 1. Get a reference to the notary service on our network and our key pair.
            // Note: ongoing work to support multiple notary identities is still in progress.
      progressTracker.setCurrentStep(RETRIEVING_NOTARY_IDENTITY);
            final Party notary = getServiceHub().getNetworkMapCache().getNotaryIdentities().get(0);

            // Step 2. Create a new issue command.
            // Remember that a command is a CommandData object and a list of CompositeKeys
      progressTracker.setCurrentStep(TX_BUILDING);

      CommandData commandData = new PromissoryNoteCordaContract.Commands.Issue();
      final Command issueCommand = new Command(
                    commandData,
          new ArrayList(Arrays.asList( state.lenderCordaParty.getOwningKey(), state.makerCordaParty.getOwningKey()))
      );

            // Step 3. Create a new TransactionBuilder object.
            final TransactionBuilder builder = new TransactionBuilder(notary);

            // Step 4. Add the iou as an output state, as well as a command to the transaction builder.
            builder.addOutputState(state, PromissoryNoteCordaContract.PROMISSORY_NOTE_CONTRACT_ID);
            builder.addCommand(issueCommand);

      // Step 5. Add the contract to the transaction
      try {
        InputStream ciceroTemplateInputStream = new ByteArrayInputStream(contractText.getBytes());
        SecureHash attachmentHash = getServiceHub().getAttachments().importAttachment(AccordUtils.getCompressed(ciceroTemplateInputStream), getOurIdentity().getName().toString(), "contract.txt");
        builder.addAttachment(attachmentHash);
      } catch (Exception e) {
        throw new Error(e.getMessage());
      }

      // Step 6. Verify and sign it with our KeyPair.
      progressTracker.setCurrentStep(TX_VERIFICATION);
            builder.verify(getServiceHub());
      progressTracker.setCurrentStep(TX_SIGNING);
            final SignedTransaction ptx = getServiceHub().signInitialTransaction(builder);


            // Step 7. Collect the other party's signature using the SignTransactionFlow.
      progressTracker.setCurrentStep(SIGNATURE_GATHERING);
            List<Party> otherParties = state.getParticipants()
                    .stream().map(el -> (Party)el)
                    .collect(Collectors.toList());

            otherParties.remove(getOurIdentity());

            List<FlowSession> sessions = otherParties
                    .stream().map(el -> initiateFlow(el))
                    .collect(Collectors.toList());

            SignedTransaction stx = subFlow(new CollectSignaturesFlow(ptx, sessions, SIGNATURE_GATHERING.childProgressTracker()));

            // Step 8. Assuming no exceptions, we can now finalise the transaction.
      progressTracker.setCurrentStep(FINALISATION);
            return subFlow(new FinalityFlow(stx, sessions, FINALISATION.childProgressTracker()));
        }
    }

  /**
   * This is the flow which signs IOU issuances.
   * The signing is handled by the [SignTransactionFlow].
   */
  @InitiatedBy(InitiatorFlow.class)
  public static class ResponderFlow extends FlowLogic<SignedTransaction>{

    private final FlowSession flowSession;
    private SecureHash txWeJustSigned;

    public ResponderFlow(FlowSession flowSession){
      this.flowSession = flowSession;
    }

    @Suspendable
    @Override
    public SignedTransaction call() throws FlowException {
      class SignTxFlow extends SignTransactionFlow{

        private SignTxFlow(FlowSession flowSession, ProgressTracker progressTracker){
          super(flowSession, progressTracker);
        }

        @Override
        protected void checkTransaction(SignedTransaction stx){
          requireThat(req -> {
            ContractState output = stx.getTx().getOutputs().get(0).getData();
            req.using("This must be an IOU transaction", output instanceof PromissoryNoteState);
            return null;
          });
          // Once the transaction has verified, initialize txWeJustSignedID variable.
          txWeJustSigned = stx.getId();
        }
      }

      // Create a sign transaction flow
      SignTxFlow signTxFlow = new SignTxFlow(flowSession, SignTransactionFlow.Companion.tracker());

      // Run the sign transaction flow to sign the transaction
      subFlow(signTxFlow);

      // Run the ReceiveFinalityFlow to finalize the transaction and persist it to the vault.
      return subFlow(new ReceiveFinalityFlow(flowSession, txWeJustSigned));

    }
  }
}
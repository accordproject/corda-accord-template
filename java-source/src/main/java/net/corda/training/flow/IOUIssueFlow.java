package net.corda.training.flow;

import co.paralleluniverse.fibers.Suspendable;

import java.io.*;
import java.nio.file.FileAlreadyExistsException;
import java.security.PublicKey;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.fasterxml.jackson.core.JsonParser;
import com.google.common.collect.ImmutableList;
import net.corda.core.contracts.Amount;
import net.corda.core.contracts.Command;
import net.corda.core.contracts.ContractState;
import net.corda.core.crypto.SecureHash;
import net.corda.core.flows.*;
import net.corda.core.identity.AbstractParty;
import net.corda.core.identity.Party;
import net.corda.core.transactions.SignedTransaction;
import net.corda.core.transactions.TransactionBuilder;
import static net.corda.core.contracts.ContractsDSL.requireThat;
import net.corda.core.utilities.ProgressTracker;

import net.corda.finance.Currencies;
import net.corda.training.contract.IOUContract;
import net.corda.training.state.IOUState;
import org.accordproject.money.CurrencyCode;
import org.accordproject.money.MonetaryAmount;
import org.accordproject.promissorynote.PromissoryNoteContract;
import org.accordproject.usa.business.BusinessEntity;
import org.apache.commons.io.IOUtils;
import org.intellij.lang.annotations.Flow;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import scala.util.parsing.json.JSON;

import static net.corda.training.contract.IOUContract.Commands.*;

/**
 * This is the flow which handles issuance of new IOUs on the ledger.
 * Gathering the counterparty's signature is handled by the [CollectSignaturesFlow].
 * Notarisation (if required) and commitment to the ledger is handled by the [FinalityFlow].
 * The flow returns the [SignedTransaction] that was committed to the ledger.
 */
public class IOUIssueFlow {

    @InitiatingFlow
    @StartableByRPC
    public static class InitiatorFlow extends FlowLogic<SignedTransaction> {
        private final IOUState state;
        private final SecureHash ciceroTemplateId;

        public InitiatorFlow(IOUState state, SecureHash ciceroTemplateId) {
        	this.state = state;
        	this.ciceroTemplateId = ciceroTemplateId;
        }

		private InputStream getStateFromContract() throws FileNotFoundException, FileAlreadyExistsException, IOException {
			String[] command = {"./resources/cicero-parse.sh", "java/AccordProject/cicero-template-library/src/promissory-note"};
			ProcessBuilder ciceroParse = new ProcessBuilder(command);
			ciceroParse.directory(new File("./src/main"));
			return ciceroParse.start().getInputStream();
		}

        @Suspendable
        @Override
        public SignedTransaction call() throws FlowException {

        	IOUState state;

        	// Step 0. Generate an input state from the parsed Contract.
			try {
				InputStream dataFromContract = getStateFromContract();
				String jsonData = IOUtils.toString(dataFromContract, "UTF-8");
				PromissoryNoteContract parsedContractData = (PromissoryNoteContract) new JSONParser().parse(jsonData);
				state = new IOUState(parsedContractData);
			} catch (Exception e) {
				throw new FlowException(e.getCause());
			}

            // Step 1. Get a reference to the notary service on our network and our key pair.
            // Note: ongoing work to support multiple notary identities is still in progress.
            final Party notary = getServiceHub().getNetworkMapCache().getNotaryIdentities().get(0);

            // Step 2. Create a new issue command.
            // Remember that a command is a CommandData object and a list of CompositeKeys
//            final Command<Issue> issueCommand = new Command<>(
//                    new Issue(), state.getParticipants()
//                    .stream().map(AbstractParty::getOwningKey)
//                    .collect(Collectors.toList()));
			final Command<Issue> issueCommand = new Command<>(
                    new Issue(),
					Arrays.asList(getOurIdentity().getOwningKey()));

            // Step 3. Create a new TransactionBuilder object.
            final TransactionBuilder builder = new TransactionBuilder(notary);

            // Step 4. Add the iou as an output state, as well as a command to the transaction builder.
            builder.addOutputState(state, IOUContract.IOU_CONTRACT_ID);
            builder.addCommand(issueCommand);

            // Step 4.5 Added the Cicero template contract to the state
			builder.addAttachment(ciceroTemplateId);

            // Step 5. Verify and sign it with our KeyPair.
            builder.verify(getServiceHub());
            final SignedTransaction ptx = getServiceHub().signInitialTransaction(builder);


            // Step 6. Collect the other party's signature using the SignTransactionFlow.
            List<Party> otherParties = state.getParticipants()
                    .stream().map(el -> (Party)el)
                    .collect(Collectors.toList());

            otherParties.remove(getOurIdentity());

            List<FlowSession> sessions = otherParties
                    .stream().map(el -> initiateFlow(el))
                    .collect(Collectors.toList());

            SignedTransaction stx = subFlow(new CollectSignaturesFlow(ptx, sessions));

            // Step 7. Assuming no exceptions, we can now finalise the transaction.
            return subFlow(new FinalityFlow(stx));
        }
    }

	/**
	 * This is the flow which signs IOU issuances.
	 * The signing is handled by the [SignTransactionFlow].
	 */
	@InitiatedBy(InitiatorFlow.class)
	public static class ResponderFlow extends FlowLogic<SignedTransaction>{
		private final FlowSession flowSession;

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
						req.using("This must be an IOU transaction", output instanceof IOUState);
						return null;
					});
				}
			}
			return subFlow(new SignTxFlow(flowSession, SignTransactionFlow.Companion.tracker()));
		}
	}
}
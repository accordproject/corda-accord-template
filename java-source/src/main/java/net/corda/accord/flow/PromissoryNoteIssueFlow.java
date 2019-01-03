package net.corda.accord.flow;

import co.paralleluniverse.fibers.Suspendable;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.corda.accord.contract.IOUContract;
import net.corda.accord.state.IOUState;
import net.corda.core.contracts.Command;
import net.corda.core.contracts.ContractState;
import net.corda.core.crypto.SecureHash;
import net.corda.core.flows.*;
import net.corda.core.identity.Party;
import net.corda.core.transactions.SignedTransaction;
import net.corda.core.transactions.TransactionBuilder;
import static net.corda.core.contracts.ContractsDSL.requireThat;
import net.corda.core.utilities.ProgressTracker;

import org.accordproject.promissorynote.PromissoryNoteContract;
import org.apache.commons.io.IOUtils;
import org.json.simple.parser.JSONParser;

/**
 * This is the flow which handles issuance of new promissory notes from a legal document.
 * The flow returns the [SignedTransaction] that was committed to the ledger.
 */
public class PromissoryNoteIssueFlow {

    @InitiatingFlow
    @StartableByRPC
    public static class InitiatorFlow extends FlowLogic<SignedTransaction> {
        private final Party lender;
		private final Party maker;
		private final SecureHash ciceroTemplateID;

        public InitiatorFlow(SecureHash ciceroTemplateId, Party lender, Party maker) {
        	this.lender = lender;
        	this.maker = maker;
        	this.ciceroTemplateID = ciceroTemplateId;
        }

		/**
		 * This function parses a legal document using a Cicero template and returns an input stream with the output from the terminal.
		 * The script 'cicero-parse.sh writes the output of Cicero-parse to a temporary file, surpresses standard system-out messaging and then
		 * logs out the JSON (which is captured in the input stream.
 		 */

		// TODO: Enable the user to specify the file path for the promissary note template
		// TODO: Adjust the cicero-parse command to include an option on only return the JSON with no initial messaging
		// TODO: Enable the user to specify the file path for the source legal document
		private InputStream getStateFromContract() throws IOException {
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
				Object parsedJSONData = new JSONParser().parse(jsonData);
				ObjectMapper objectMapper = new ObjectMapper();
				PromissoryNoteContract parsedContractData = objectMapper.readValue(jsonData, PromissoryNoteContract.class);
				state = new IOUState(parsedContractData, lender, maker);
			} catch (Exception e) {
				throw new FlowException(e.toString());
			}

            // Step 1. Get a reference to the notary service on our network and our key pair.
            // Note: ongoing work to support multiple notary identities is still in progress.
            final Party notary = getServiceHub().getNetworkMapCache().getNotaryIdentities().get(0);

            // Step 2. Create a new issue command.
            // Remember that a command is a CommandData object and a list of CompositeKeys
			final Command<IOUContract.Commands.Issue> issueCommand = new Command<>(
                    new IOUContract.Commands.Issue(),
					Arrays.asList(lender.getOwningKey(), maker.getOwningKey())
			);

            // Step 3. Create a new TransactionBuilder object.
            final TransactionBuilder builder = new TransactionBuilder(notary);

            // Step 4. Add the iou as an output state, as well as a command to the transaction builder.
            builder.addOutputState(state, IOUContract.IOU_CONTRACT_ID);
            builder.addCommand(issueCommand);

            // Step 4.5 Added the Cicero template contract to the state
			builder.addAttachment(ciceroTemplateID);

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
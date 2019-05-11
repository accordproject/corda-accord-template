package net.corda.accord.flow

import co.paralleluniverse.fibers.Suspendable

import java.io.*
import java.util.ArrayList
import java.util.Arrays

import com.fasterxml.jackson.databind.ObjectMapper
import net.corda.accord.AccordUtils
import net.corda.accord.contract.PromissoryNoteCordaContract
import net.corda.accord.state.PromissoryNoteState
import net.corda.core.contracts.Command
import net.corda.core.flows.*
import net.corda.core.identity.CordaX500Name
import net.corda.core.identity.Party
import net.corda.core.transactions.SignedTransaction
import net.corda.core.transactions.TransactionBuilder
import net.corda.core.contracts.requireThat
import net.corda.core.utilities.ProgressTracker
import net.corda.core.utilities.ProgressTracker.Step

import java.security.PublicKey

/**
 * This is the flow which handles issuance of new promissory notes from a legal document.
 * The flow returns the [SignedTransaction] that was committed to the ledger.
 */

@InitiatingFlow
@StartableByRPC
@StartableByService
class PromissoryNoteIssueJSONFlow(private val contractText: String, private val jsonData: String) : FlowLogic<SignedTransaction>() {

    /** TODO: Enable the user to specify the file path for the promissory note template
     * TODO: Adjust the cicero-parse command to include an option on only return the JSON with no initial messaging
     * TODO: Enable the user to specify the file path for the source legal document  */

    @Suspendable
    @Throws(FlowException::class)
    override fun call(): SignedTransaction {

        val state: PromissoryNoteState
        // Step 0. Generate an input state from the parsed Contract.
        progressTracker.currentStep = GENERATING_STATE_FROM_CONTRACT
        try {
            progressTracker.currentStep = GETTING_LAW_DEGREE
            val objectMapper = ObjectMapper()

            progressTracker.currentStep = PARSING_LEGALESE
            val parsedContractData = objectMapper.readValue(jsonData, org.accordproject.promissorynote.PromissoryNoteContract::class.java)

            // Get the relevant Corda parties from the network map using parsed contract data.
            val maker = serviceHub.networkMapCache.getPeerByLegalName(CordaX500Name(parsedContractData.maker, "NY", "US"))
            val lender = serviceHub.networkMapCache.getPeerByLegalName(CordaX500Name(parsedContractData.lender, "NY", "US"))
            state = PromissoryNoteState(parsedContractData, maker!!, lender!!)
            progressTracker.currentStep = MAKING_PARENTS_HAPPY
        } catch (e: Exception) {
            throw FlowException("Error parsing contract.txt: $e")
        }

        // Step 1. Get a reference to the notary service on our network and our key pair.
        // Note: ongoing work to support multiple notary identities is still in progress.
        progressTracker.currentStep = RETRIEVING_NOTARY_IDENTITY
        val notary = serviceHub.networkMapCache.notaryIdentities[0]

        // Step 2. Create a new issue command.
        // Remember that a command is a CommandData object and a list of CompositeKeys
        progressTracker.currentStep = TX_BUILDING
        val commandData = PromissoryNoteCordaContract.Commands.Issue()
        val issueCommand = Command(
                commandData,
                ArrayList(Arrays.asList<PublicKey>(state.lenderCordaParty.owningKey, state.makerCordaParty.owningKey))
        )

        // Step 3. Create a new TransactionBuilder object.
        val builder = TransactionBuilder(notary)

        // Step 4. Add the iou as an output state, as well as a command to the transaction builder.
        builder.addOutputState(state, PromissoryNoteCordaContract.PROMISSORY_NOTE_CONTRACT_ID)
        builder.addCommand(issueCommand)

        // Step 5. Add the contract to the transaction
        try {
            val ciceroTemplateFileInputStream = ByteArrayInputStream(contractText.toByteArray())
            val attachmentHash = serviceHub.attachments.importAttachment(AccordUtils.getCompressed(ciceroTemplateFileInputStream), ourIdentity.name.toString(), "contract.txt")
            builder.addAttachment(attachmentHash)
        } catch (e: Exception) {
            throw Error(e.message)
        }

        // Step 6. Verify and sign it with our KeyPair.
        progressTracker.currentStep = TX_VERIFICATION
        builder.verify(serviceHub)
        progressTracker.currentStep = TX_SIGNING
        val ptx = serviceHub.signInitialTransaction(builder)


        // Step 7. Collect the other party's signature using the SignTransactionFlow.
        val sessions = (state.participants - ourIdentity).map { initiateFlow(it as Party) }

        val stx = subFlow(CollectSignaturesFlow(ptx, sessions, SIGNATURE_GATHERING.childProgressTracker()))

        // Step 8. Assuming no exceptions, we can now finalise the transaction.
        return subFlow(FinalityFlow(stx, sessions, FINALISATION.childProgressTracker()))
    }

    companion object {
        // Giving our flow a progress tracker allows us to see the flow's
        // progress visually in our node's CRaSH shell.
        private val GENERATING_STATE_FROM_CONTRACT = Step("Generating a state using Cicero CLI Functionality to Parse a legal document.")
        private val PARSING_LEGALESE = Step("Parsing Legalese")
        private val GETTING_LAW_DEGREE = Step("Getting Law Degree")
        private val MAKING_PARENTS_HAPPY = Step("Making Parents Happy")
        private val RETRIEVING_NOTARY_IDENTITY = Step("Getting a reference to the notary node.")
        private val TX_BUILDING = Step("Building a transaction.")
        private val TX_SIGNING = Step("Signing a transaction.")
        private val TX_VERIFICATION = Step("Verifying a transaction.")
        private val SIGNATURE_GATHERING = object : Step("Gathering a transaction's signatures.") {
            // Wiring up a child progress tracker allows us to see the
            // subflow's progress steps in our flow's progress tracker.
            override fun childProgressTracker(): ProgressTracker {
                return CollectSignaturesFlow.tracker()
            }
        }

        private val FINALISATION = object : Step("Finalising a transaction.") {
            override fun childProgressTracker(): ProgressTracker {
                return FinalityFlow.tracker()
            }
        }
    }

    override val progressTracker = ProgressTracker(
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
    )


}

/**
 * This is the flow which signs IOU issuances.
 * The signing is handled by the [SignTransactionFlow].
 */
@InitiatedBy(PromissoryNoteIssueJSONFlow::class)
class PromissoryNoteIssueJSONResponderFlow(private val flowSession: FlowSession) : FlowLogic<SignedTransaction>() {

    @Suspendable
    override fun call(): SignedTransaction {
        val signedTransactionFlow = object : SignTransactionFlow(flowSession) {
            override fun checkTransaction(stx: SignedTransaction) = requireThat {
                val outputStates = stx.tx.outputs.map { it.data::class.java.name }.toList()
                "There must be a PromissoryNote State in the transaction." using (outputStates.contains(PromissoryNoteState::class.java.name))
            }
        }

        val txWeJustSignedId = subFlow(signedTransactionFlow)
        return subFlow(ReceiveFinalityFlow(flowSession, txWeJustSignedId.id))
    }

}
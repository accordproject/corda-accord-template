package net.corda.accord.flow

import co.paralleluniverse.fibers.Suspendable
import net.corda.accord.contract.PromissoryNoteCordaContract
import net.corda.core.contracts.*
import net.corda.core.identity.Party
import net.corda.core.node.services.vault.QueryCriteria
import net.corda.core.transactions.SignedTransaction
import net.corda.core.transactions.TransactionBuilder
import net.corda.accord.state.PromissoryNoteState
import net.corda.core.flows.*
import java.lang.IllegalArgumentException

/**
 * This is the flow which handles transfers of existing IOUs on the ledger.
 * Gathering the counterparty's signature is handled by the [CollectSignaturesFlow].
 * Notarisation (if required) and commitment to the ledger is handled by the [FinalityFlow].
 * The flow returns the [SignedTransaction] that was committed to the ledger.
 */
@InitiatingFlow
@StartableByRPC
class PromissoryNoteTransferFlow(val linearId: UniqueIdentifier, val newLender: Party): FlowLogic<SignedTransaction>() {
    @Suspendable
    override fun call(): SignedTransaction {

        // Get components from the flow
        val queryCriteria = QueryCriteria.LinearStateQueryCriteria(linearId = listOf(linearId))
        val iouStateAndRef =  serviceHub.vaultService.queryBy(PromissoryNoteState::class.java, queryCriteria).states.single()
        val inputState = iouStateAndRef.state.data
        val notary = serviceHub.networkMapCache.notaryIdentities[0]

        // Add command to the flow
        val tb = TransactionBuilder(notary)
        val command = Command(PromissoryNoteCordaContract.Commands.Transfer(), listOf(inputState.makerCordaParty.owningKey, inputState.lenderCordaParty.owningKey, newLender.owningKey))
        tb.addCommand(command)

        // Add States to the flow
        val convertedInputState = TransactionState(inputState, PromissoryNoteCordaContract.PROMISSORY_NOTE_CONTRACT_ID , notary)
        tb.withItems(iouStateAndRef, StateAndContract(convertedInputState.data.withNewLender(newLender), PromissoryNoteCordaContract.PROMISSORY_NOTE_CONTRACT_ID))

        // Check lender is running the flow
        if (inputState.lenderCordaParty != ourIdentity) {
            throw IllegalArgumentException("This flow must be run by the current lender.")
        }

        // Verify the transaction
        tb.verify(serviceHub)

        // Sign the transaction
        val partiallySignedTransaction = serviceHub.signInitialTransaction(tb)

        // Collect Signatures
        val sessions = (inputState.participants - ourIdentity + newLender).map{ initiateFlow(it as Party) }.toSet()
        val fullySignedTransaction = subFlow(CollectSignaturesFlow(partiallySignedTransaction, sessions))

        return subFlow(FinalityFlow(fullySignedTransaction, sessions))

    }
}

/**
 * This is the flow which signs IOU transfers.
 * The signing is handled by the [SignTransactionFlow].
 */
@InitiatedBy(PromissoryNoteTransferFlow::class)
class PromissoryNoteTransferFlowResponder(val flowSession: FlowSession): FlowLogic<SignedTransaction>() {
    @Suspendable
    override fun call(): SignedTransaction {
        val signedTransactionFlow = object : SignTransactionFlow(flowSession) {
            override fun checkTransaction(stx: SignedTransaction) = requireThat {
                val output = stx.tx.outputs.single().data
                "This must be an IOU transaction" using (output is PromissoryNoteState)
            }
        }

        val txWeJustSignedId = subFlow(signedTransactionFlow)
        return subFlow(ReceiveFinalityFlow(flowSession, txWeJustSignedId.id))

    }
}
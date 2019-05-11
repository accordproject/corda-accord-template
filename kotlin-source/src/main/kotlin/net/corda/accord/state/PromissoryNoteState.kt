package net.corda.accord.state

import net.corda.accord.contract.PromissoryNoteCordaContract
import net.corda.core.contracts.*
import net.corda.core.identity.Party
import net.corda.core.identity.AbstractParty
import java.util.*
import com.google.common.collect.ImmutableList
import net.corda.core.contracts.BelongsToContract
import org.accordproject.promissorynote.PromissoryNoteContract

/**
 * The data in this state is parsed from a source legal document using Cicero. Where necessary, classes have been manually converted from Accord Project classes
 * to Corda Classes including Amounts. Other Accord Project java classes have been included 'as-is'. Note these classes must be marked as @CordaSerializable in order
 * to be included as part of state data.
 */

// TODO: Parties parsed from the source legal contract contain a String Name and Address, these needs to be assigned to Node identities.
// TODO: `amount` and `principal` are both included in parsedJSON as a accord-project monetary amount. They must be converted to the Corda Amount<Currency> class.
@BelongsToContract(PromissoryNoteCordaContract::class)
data class PromissoryNoteState(val apContract: PromissoryNoteContract,
                               val makerCordaParty: Party,
                               val lenderCordaParty: Party,
                               val paid: Amount<Currency> = apContract.amount.currency,
                               override val linearId: UniqueIdentifier = UniqueIdentifier()
): ContractState, LinearState {

    // Participants included in the contract must also be CordaNodes.
    override val participants: List<AbstractParty>
        get() = ImmutableList.of<AbstractParty>(makerCordaParty, lenderCordaParty)

    val amount: Amount<Currency>
        get() = this.apContract.amount.currency

    fun pay(paidAmount: Amount<Currency>): PromissoryNoteState {
        val newAmountPaid = this.paid.plus(paidAmount)
        return PromissoryNoteState(apContract, lenderCordaParty, makerCordaParty, newAmountPaid, linearId)
    }

    // Utility function for creating a promissory note state with a new lender.
    fun withNewLender(newLender: Party): PromissoryNoteState {
        return this.copy(
                this.apContract,
                this.paid,
                newLender,
                this.makerCordaParty
        )
    }

    // Utility function for copying a promissory note state with the
    fun copy(apContract: PromissoryNoteContract,
             paid: Amount<Currency>,
             lenderCordaParty: Party,
             makerCordaParty: Party): PromissoryNoteState {
        return PromissoryNoteState(apContract, lenderCordaParty, makerCordaParty, paid, this.linearId)
    }

}

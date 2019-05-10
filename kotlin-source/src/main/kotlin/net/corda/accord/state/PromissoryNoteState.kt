package net.corda.accord.state

import net.corda.accord.contract.PromissoryNoteCordaContract
import net.corda.core.contracts.*
import net.corda.core.identity.Party
import net.corda.core.identity.AbstractParty
import java.util.*
import com.google.common.collect.ImmutableList
import net.corda.core.serialization.ConstructorForDeserialization
import net.corda.core.contracts.BelongsToContract
import org.accordproject.promissorynote.PromissoryNoteContract

/**
 * The data in this state is parsed from a source legal document using Cicero. Where necessary, classes have been manually converted from Accord Project classes
 * to Corda Classes including Amounts. Other Accord Project java classes have been included 'as-is'. Note these classes must be marked as @CordaSerializable in order
 * to be included as part of state data.
 */

@BelongsToContract(PromissoryNoteCordaContract::class)
class PromissoryNoteState// Private constructor used only for copying a State object
@ConstructorForDeserialization
private constructor(// TODO: `amount` and `principal` are both included in parsedJSON as a accord-project monetary amount. They must be converted to the Corda Amount<Currency> class.
        // The fields listed here correspond to the fields returned in the JSON output from the Cicero-Parse shell-script.
        var apContract: PromissoryNoteContract,
        override var linearId: UniqueIdentifier,
        var paid: Amount<Currency>,
        // TODO: Parties parsed from the source legal contract contain a String Name and Address, these needs to be assigned to Node identities.
        var makerCordaParty: Party,
        var lenderCordaParty: Party) : ContractState, LinearState {

    // Participants included in the contract must also be CordaNodes.
    override val participants: List<AbstractParty>
        get() = ImmutableList.of<AbstractParty>(makerCordaParty, lenderCordaParty)

    val amount: Amount<Currency>
        get() = this.apContract.getAmount().getCurrency()

    /**
     * This is the primary constructor for creating a new promissory note from JSON data returned by Cicero-Parse. It's also important to note
     * that the constructor has both maker and lender party parameters. This information cannot be parsed from the contract and the nodes must be
     * explicitly passed in as a reference.
     */

    /**
     * TODO: Align node CordaX500Names with legal documentation so that nodes might be identified and selected from the network at the time of issuance rather than being explicitly passed in.
     */
    constructor(promissoryNoteContract: PromissoryNoteContract, makerCordaParty: Party, lenderCordaParty: Party) : this(
            promissoryNoteContract,
            UniqueIdentifier(),
            promissoryNoteContract.getAmount().getCurrency(),
            lenderCordaParty,
            makerCordaParty
    )

    fun pay(paidAmount: Amount<Currency>): PromissoryNoteState {
        val newAmountPaid = this.paid.plus(paidAmount)
        return PromissoryNoteState(apContract, linearId, newAmountPaid, lenderCordaParty, makerCordaParty)
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
        return PromissoryNoteState(apContract, this.linearId, paid, lenderCordaParty, makerCordaParty)
    }

}
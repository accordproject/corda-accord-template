package net.corda.accord.state;

import net.corda.core.contracts.Amount;
import net.corda.core.contracts.ContractState;
import net.corda.core.contracts.LinearState;
import net.corda.core.contracts.UniqueIdentifier;
import net.corda.core.identity.Party;
import net.corda.core.identity.AbstractParty;
import java.util.*;
import com.google.common.collect.ImmutableList;
import net.corda.core.serialization.ConstructorForDeserialization;
import net.corda.core.contracts.BelongsToContract;
import net.corda.finance.Currencies;
import org.accordproject.promissorynote.PromissoryNoteContract;
import org.accordproject.usa.business.BusinessEntity;
import org.accordproject.money.MonetaryAmount;

/**
 * The data in this state is parsed from a source legal document using Cicero. Where necessary, classes have been manually converted from Accord Project classes
 * to Corda Classes including Amounts. Other Accord Project java classes have been included 'as-is'. Note these classes must be marked as @CordaSerializable in order
 * to be included as part of state data.
 */

@BelongsToContract(net.corda.accord.contract.PromissoryNoteContract.class)
public class PromissoryNoteState implements ContractState, LinearState {

    // TODO: `amount` and `principal` are both included in parsedJSON as a accord-project monetary amount. They must be converted to the Corda Amount<Currency> class.
    // The fields listed here correspond to the fields returned in the JSON output from the Cicero-Parse shell-script.
    public PromissoryNoteContract apContract;
    public UniqueIdentifier linearId;
    public Amount<Currency> paid;
    // TODO: Parties parsed from the source legal contract contain a String Name and Address, these needs to be assigned to Node identities.
    public Party makerCordaParty;
    public Party lenderCordaParty;

    // Private constructor used only for copying a State object
    @ConstructorForDeserialization
    private PromissoryNoteState(PromissoryNoteContract apContract,
                                UniqueIdentifier linearId,
                                Amount<Currency> paid,
                                Party makerCordaParty,
                                Party lenderCordaParty) {
       this.apContract = apContract;
       this.linearId = linearId;
       this.paid = paid;
       this.makerCordaParty = makerCordaParty;
       this.lenderCordaParty = lenderCordaParty;
	}

    /**
     * This is the primary constructor for creating a new promissory note from JSON data returned by Cicero-Parse. It's also important to note
     * that the constructor has both maker and lender party parameters. This information cannot be parsed from the contract and the nodes must be
     * explicitly passed in as a reference.
     */

    /**
     TODO: Align node CordaX500Names with legal documentation so that nodes might be identified and selected from the network at the time of issuance rather than being explicitly passed in.
     */
	public PromissoryNoteState(PromissoryNoteContract promissoryNoteContract, Party makerCordaParty, Party lenderCordaParty) {
        this(
                promissoryNoteContract,
                new UniqueIdentifier(),
                promissoryNoteContract.getAmount().getCurrency(),
                lenderCordaParty,
                makerCordaParty
        );
    }

    public PromissoryNoteContract getApContract() {
        return apContract;
    }

    public Amount<Currency> getPaid() {
        return paid;
    }

    public Party getLenderCordaParty() {
        return lenderCordaParty;
    }

    public Party getMakerCordaParty() {
        return makerCordaParty;
    }

    @Override
    public UniqueIdentifier getLinearId() {
	    return linearId;
    }

    // Participants included in the contract must also be CordaNodes.
   	@Override
    public List<AbstractParty> getParticipants() {
        return ImmutableList.of(makerCordaParty, lenderCordaParty);
    }

    public PromissoryNoteState pay(Amount<Currency> paidAmount) {
        Amount<Currency> newAmountPaid = this.paid.plus(paidAmount);
        return new PromissoryNoteState(apContract, linearId, newAmountPaid, lenderCordaParty, makerCordaParty);
    }

    // Utility function for creating a promissory note state with a new lender.
    public PromissoryNoteState withNewLender(Party newLender) {
        return this.copy(
                this.apContract,
                this.paid,
                newLender,
                this.makerCordaParty
        );
    }

    // Utility function for copying a promissory note state with the
    public PromissoryNoteState copy(PromissoryNoteContract apContract,
                                    Amount<Currency> paid,
                                    Party lenderCordaParty,
                                    Party makerCordaParty) {
        return new PromissoryNoteState(apContract, this.getLinearId(), paid, lenderCordaParty, makerCordaParty);
    }

    public Amount<Currency> getAmount() {
        return this.apContract.getAmount().getCurrency();
    }

}
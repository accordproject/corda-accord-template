package net.corda.training.state;

import net.corda.core.contracts.Amount;
import net.corda.core.contracts.ContractState;
import net.corda.core.contracts.LinearState;
import net.corda.core.contracts.UniqueIdentifier;
import net.corda.core.identity.Party;
import net.corda.core.identity.AbstractParty;
import java.util.*;
import com.google.common.collect.ImmutableList;
import net.corda.core.serialization.ConstructorForDeserialization;
import net.corda.training.org.accordproject.promissorynote.PromissoryNoteContract;

/**
 * This is where you'll add the definition of your state object. Look at the unit tests in [IOUStateTests] for
 * instructions on how to complete the [IOUState] class.
 *
 */
public class IOUState implements ContractState, LinearState {

    public final Amount<Currency> amount;
    public final Party lender;
    public final Party borrower;
    public final Amount<Currency> paid;
    private final UniqueIdentifier linearId;
    private final PromissoryNoteContract promissoryNoteContract;

    // Private constructor used only for copying a State object
    @ConstructorForDeserialization
    private IOUState(Amount<Currency> amount, Party lender, Party borrower, Amount<Currency> paid, UniqueIdentifier linearId, PromissoryNoteContract promissoryNoteContract){
       this.amount = amount;
       this.lender = lender;
       this.borrower = borrower;
       this.paid = paid;
       this.linearId = linearId;
       this.promissoryNoteContract = promissoryNoteContract;
	}

	public IOUState(Amount<Currency> amount, Party lender, Party borrower, PromissoryNoteContract promissoryNoteContract) {
        this(amount, lender, borrower, new Amount<>(0, amount.getToken()), new UniqueIdentifier(), promissoryNoteContract);
    }

    public Amount<Currency> getAmount() {
        return amount;
    }

    public Party getLender() {
	    return lender;
    }

    public Party getBorrower() {
        return borrower;
    }

    public Amount getPaid() {
	    return paid;
    }

    @Override
    public UniqueIdentifier getLinearId() {
	    return linearId;
    }

   	@Override
    public List<AbstractParty> getParticipants() {
        return ImmutableList.of(borrower, lender);
    }

    public IOUState pay(Amount paidAmount) {
        Amount<Currency> newAmountPaid = this.paid.plus(paidAmount);
        return new IOUState(amount, lender, borrower, newAmountPaid, linearId, promissoryNoteContract);
    }

    public IOUState withNewLender(Party newLender) {
        return new IOUState(amount, newLender, borrower, paid, linearId, promissoryNoteContract);
    }

    public IOUState copy(Amount<Currency> amount, Party lender, Party borrower, Amount<Currency> paid) {
        return new IOUState(amount, lender, borrower, paid, this.getLinearId(), promissoryNoteContract);
    }

}
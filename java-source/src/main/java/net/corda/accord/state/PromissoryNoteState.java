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
import net.corda.finance.Currencies;
import org.accordproject.promissorynote.PromissoryNoteContract;
import org.accordproject.usa.business.BusinessEntity;

/**
 * The data in this state is parsed from a source legal document using Cicero. Where necessary, classes have been manually converted from Accord Project classes
 * to Corda Classes including Amounts. Other Accord Project java classes have been included 'as-is'. Note these classes must be marked as @CordaSerializable in order
 * to be included as part of state data.
 */

public class PromissoryNoteState implements ContractState, LinearState {

    // TODO: `amount` and `principal` are both included in parsedJSON as a accord-project monetary amount. They must be converted to the Corda Amount<Currency> class.
    // The fields listed here correspond to the fields returned in the JSON output from the Cicero-Parse shell-script.
    public Amount<Currency> amount;
    public Date date;
    public String maker;
    public double interestRate;
    public boolean individual;
    public String makerAddress;
    public String lender;
    public BusinessEntity legalEntity;
    public String lenderAddress;
    public Amount<Currency> principal;
    public Date maturityDate;
    public int defaultDays;
    public int insolvencyDays;
    public String jurisdiction;
    public UniqueIdentifier linearId;
    public Amount<Currency> paid;
    // TODO: Parties parsed from the source legal contract contain a String Name and Address, these needs to be assigned to Node identities.
    public Party makerCordaParty;
    public Party lenderCordaParty;

    // Private constructor used only for copying a State object
    @ConstructorForDeserialization
    private PromissoryNoteState(Amount<Currency> amount,
                                Date date,
                                String maker,
                                double interestRate,
                                boolean individual,
                                String makerAddress,
                                String lender,
                                BusinessEntity legalEntity,
                                String lenderAddress,
                                Amount<Currency> principal,
                                Date maturityDate,
                                int defaultDays,
                                int insolvencyDays,
                                String jurisdiction,
                                UniqueIdentifier linearId,
                                Amount<Currency> paid,
                                Party makerCordaParty,
                                Party lenderCordaParty) {
       this.amount = amount;
       this.date = date;
       this.maker = maker;
       this.interestRate = interestRate;
       this.individual = individual;
       this.makerAddress = makerAddress;
       this.lender = lender;
       this.legalEntity = legalEntity;
       this.lenderAddress = lenderAddress;
       this.principal = principal;
       this.maturityDate = maturityDate;
       this.defaultDays = defaultDays;
       this.insolvencyDays = insolvencyDays;
       this.jurisdiction = jurisdiction;
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
                Currencies.DOLLARS(promissoryNoteContract.amount.doubleValue),
                promissoryNoteContract.date,
                promissoryNoteContract.maker,
                promissoryNoteContract.interestRate,
                promissoryNoteContract.individual,
                promissoryNoteContract.makerAddress,
                promissoryNoteContract.lender,
                promissoryNoteContract.legalEntity,
                promissoryNoteContract.lenderAddress,
                new Amount<Currency>((long) promissoryNoteContract.principal.doubleValue, Currencies.DOLLARS(0).getToken()),
                promissoryNoteContract.maturityDate,
                promissoryNoteContract.defaultDays,
                promissoryNoteContract.insolvencyDays,
                promissoryNoteContract.jurisdiction,
                new UniqueIdentifier(),
                new Amount<Currency>((long) promissoryNoteContract.amount.doubleValue, Currencies.DOLLARS(0).getToken()),
                lenderCordaParty,
                makerCordaParty
        );
    }

    public Amount<Currency> getAmount() {
        return amount;
    }

    public Date getDate() {
        return date;
    }

    public String getMaker() {
        return maker;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public boolean isIndividual() {
        return individual;
    }

    public String getMakerAddress() {
        return makerAddress;
    }

    public int getInsolvencyDays() {
        return insolvencyDays;
    }

    public String getLender() {
        return lender;
    }

    public String getLenderAddress() {
        return lenderAddress;
    }

    public Amount<Currency> getPrincipal() {
        return principal;
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
        return new PromissoryNoteState(amount, date, maker, interestRate, individual, makerAddress, lender, legalEntity, lenderAddress, principal, maturityDate, defaultDays, insolvencyDays, jurisdiction, linearId, newAmountPaid, lenderCordaParty, makerCordaParty);
    }

    // Utility function for creating a promissory note state with a new lender.
    public PromissoryNoteState withNewLender(Party newLender) {
        return this.copy(
                this.amount,
                this.date,
                this.maker,
                this.interestRate,
                this.individual,
                this.makerAddress,
                this.lender,
                this.legalEntity,
                this.lenderAddress,
                this.principal,
                this.maturityDate,
                this.defaultDays,
                this.insolvencyDays,
                this.jurisdiction,
                this.paid,
                newLender,
                this.makerCordaParty
        );
    }

    // Utility function for copying a promissory note state with the
    public PromissoryNoteState copy(Amount<Currency> amount,
                                    Date date,
                                    String maker,
                                    double interestRate,
                                    boolean individual,
                                    String makerAddress,
                                    String lender,
                                    BusinessEntity legalEntity,
                                    String lenderAddress,
                                    Amount<Currency> principal,
                                    Date maturityDate,
                                    int defaultDays,
                                    int insolvencyDays,
                                    String jurisdiction,
                                    Amount<Currency> paid,
                                    Party lenderCordaParty,
                                    Party makerCordaParty) {
        return new PromissoryNoteState(amount, date, maker, interestRate, individual, makerAddress, lender, legalEntity, lenderAddress, principal, maturityDate, defaultDays, insolvencyDays, jurisdiction, this.getLinearId(), paid, lenderCordaParty, makerCordaParty);
    }

}
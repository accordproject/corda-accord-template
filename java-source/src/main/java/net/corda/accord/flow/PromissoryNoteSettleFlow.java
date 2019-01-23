package net.corda.accord.flow;

import co.paralleluniverse.fibers.Suspendable;
import com.google.common.collect.ImmutableSet;
import net.corda.accord.AccordUtils;
import net.corda.accord.contract.PromissoryNoteContract;
import net.corda.accord.state.PromissoryNoteState;
import net.corda.core.contracts.*;
import net.corda.core.crypto.SecureHash;
import net.corda.core.flows.*;
import net.corda.core.identity.AbstractParty;
import net.corda.core.identity.Party;
import net.corda.core.node.services.Vault;
import net.corda.core.node.services.vault.QueryCriteria;
import net.corda.core.transactions.SignedTransaction;
import net.corda.core.transactions.TransactionBuilder;
import net.corda.core.utilities.OpaqueBytes;
import net.corda.core.utilities.ProgressTracker;
import net.corda.finance.contracts.asset.Cash;
import net.corda.finance.flows.AbstractCashFlow;
import net.corda.finance.flows.CashIssueFlow;

import java.io.*;
import java.lang.IllegalArgumentException;

import static net.corda.core.contracts.ContractsDSL.requireThat;
import static net.corda.finance.contracts.GetBalances.getCashBalance;

import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class PromissoryNoteSettleFlow {

    /**
     * This is the flow which handles the (partial) settlement of existing IOUs on the ledger.
     * Gathering the counterparty's signature is handled by the [CollectSignaturesFlow].
     * Notarisation (if required) and commitment to the ledger is handled vy the [FinalityFlow].
     * The flow returns the [SignedTransaction] that was committed to the ledger.
     */
    @InitiatingFlow
    @StartableByRPC
    public static class InitiatorFlow extends FlowLogic<SignedTransaction> {

        private final UniqueIdentifier stateLinearId;
        private final Amount<Currency> amount;

        public InitiatorFlow(UniqueIdentifier stateLinearId, Amount<Currency> amount) {
            this.stateLinearId = stateLinearId;
            this.amount = amount;
        }

        @Suspendable
        @Override
        public SignedTransaction call() throws FlowException {

            // 1. Retrieve the IOU State from the vault.
            List<UUID> listOfLinearIds = new ArrayList<>();
            listOfLinearIds.add(stateLinearId.getId());
            QueryCriteria queryCriteria = new QueryCriteria.LinearStateQueryCriteria(null, listOfLinearIds);

            Vault.Page results = getServiceHub().getVaultService().queryBy(PromissoryNoteState.class, queryCriteria);
            StateAndRef inputStateAndRefToSettle = (StateAndRef) results.component1().get(0);
            PromissoryNoteState inputStateToSettle = (PromissoryNoteState) ((StateAndRef) results.component1().get(0)).getState().getData();

            // 2. Check the party running this flow is the borrower.
            if (!inputStateToSettle.makerCordaParty.getOwningKey().equals(getOurIdentity().getOwningKey())) {
                throw new IllegalArgumentException("The maker must issue the flow");
            }

            // 3. Create a transaction builder
            Party notary = getServiceHub().getNetworkMapCache().getNotaryIdentities().get(0);
            TransactionBuilder tb = new TransactionBuilder(notary);

            // 4. Check we have enough cash to settle the requested amount
            final Amount<Currency> cashBalance = getCashBalance(getServiceHub(), (Currency) amount.getToken());

            if (cashBalance.getQuantity() < amount.getQuantity()) {
                throw new IllegalArgumentException("Maker doesn't have enough cash to settle with the amount specified.");
            } else if (amount.getQuantity() > (inputStateToSettle.amount.getQuantity() - inputStateToSettle.paid.getQuantity())) {
                throw new IllegalArgumentException("Maker tried to settle with more than was required for the obligation.");
            }

            // 5. Get some cash from the vault and add a spend to our transaction builder.
            Cash.generateSpend(getServiceHub(), tb, amount, inputStateToSettle.lenderCordaParty, ImmutableSet.of()).getSecond();


            // 6.
            Command<PromissoryNoteContract.Commands.Settle> command = new Command<>(
                    new PromissoryNoteContract.Commands.Settle(),
                    inputStateToSettle.getParticipants()
                            .stream().map(AbstractParty::getOwningKey)
                            .collect(Collectors.toList())
            );


            tb.addCommand(command);
            tb.addInputState(inputStateAndRefToSettle);

            // 7. Add an IOU output state for an IOU that has not been full settled.
            if (amount.getQuantity() < inputStateToSettle.amount.getQuantity()) {
                tb.addOutputState(inputStateToSettle.pay(amount), PromissoryNoteContract.PROMISSORY_NOTE_CONTRACT_ID);
            }

            // 8. Add the contract to the transaction
            File ciceroTemplateFile = new File("./src/main/resources/src-contract.txt");

            try {
                InputStream ciceroTemplateFileInputStream = new FileInputStream(ciceroTemplateFile);
                SecureHash attachmentHash = getServiceHub().getAttachments().importAttachment(AccordUtils.getCompressed(ciceroTemplateFileInputStream), getOurIdentity().getName().toString(), ciceroTemplateFile.getName());
                tb.addAttachment(attachmentHash);
            } catch (Exception e) {
                throw new Error(e.getMessage());
            }

            // 9. Verify and sign the transaction
            tb.verify(getServiceHub());
            SignedTransaction stx = getServiceHub().signInitialTransaction(tb, getOurIdentity().getOwningKey());

            // 10. Collect Signatures
            List<FlowSession> listOfFlows = new ArrayList<>();

            for (AbstractParty participant: inputStateToSettle.getParticipants()) {
                Party partyToInitiateFlow = (Party) participant;
                if (!partyToInitiateFlow.getOwningKey().equals(getOurIdentity().getOwningKey())) {
                    listOfFlows.add(initiateFlow(partyToInitiateFlow));
                }
            }

            SignedTransaction fullySignedTransaction = subFlow(new CollectSignaturesFlow(stx, listOfFlows));

            return subFlow(new FinalityFlow(fullySignedTransaction));

        }

    }

    /**
     * This is the flow which signs IOU settlements.
     * The signing is handled by the [SignTransactionFlow].
     */
    @InitiatedBy(PromissoryNoteSettleFlow.InitiatorFlow.class)
    public static class Responder extends FlowLogic<SignedTransaction> {

        private final FlowSession otherPartyFlow;

        public Responder(FlowSession otherPartyFlow) {
            this.otherPartyFlow = otherPartyFlow;
        }

        @Suspendable
        @Override
        public SignedTransaction call() throws FlowException {
            class SignTxFlow extends SignTransactionFlow {
                private SignTxFlow(FlowSession otherPartyFlow, ProgressTracker progressTracker) {
                    super(otherPartyFlow, progressTracker);
                }

                @Override
                protected void checkTransaction(SignedTransaction stx) {
                    requireThat(require -> {
                        ContractState output = stx.getTx().outputsOfType(PromissoryNoteState.class).get(0);
                        require.using("This must be an IOU transaction", output instanceof PromissoryNoteState);
                        return null;
                    });
                }
            }

            return subFlow(new SignTxFlow(otherPartyFlow, SignTransactionFlow.Companion.tracker()));
        }
    }

    /**
     * Self issues the calling node an amount of cash in the desired currency.
     * Only used for demo/sample/accord purposes!
     */

    @InitiatingFlow
    @StartableByRPC
    public static class SelfIssueCashFlow extends FlowLogic<Cash.State> {

        Amount<Currency> amount;

        SelfIssueCashFlow(Amount<Currency> amount) {
            this.amount = amount;
        }

        @Suspendable
        @Override
        public Cash.State call() throws FlowException {
            // Create the cash issue command.
            OpaqueBytes issueRef = OpaqueBytes.of(new byte[0]);
            // Note: ongoing work to support multiple notary identities is still in progress. */
            Party notary = getServiceHub().getNetworkMapCache().getNotaryIdentities().get(0);
            // Create the cash issuance transaction.
            AbstractCashFlow.Result cashIssueTransaction = subFlow(new CashIssueFlow(amount, issueRef, notary));
            return (Cash.State) cashIssueTransaction.getStx().getTx().getOutput(0);
        }

    }

}
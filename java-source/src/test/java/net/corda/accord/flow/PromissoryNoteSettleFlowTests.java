package net.corda.accord.flow;

import net.corda.testing.node.*;

import static net.corda.testing.node.NodeTestUtils.ledger;

/**
 * TODO: Write tests for promissory note settling once functionality is complete.
 */
public class PromissoryNoteSettleFlowTests {

    private MockNetwork mockNetwork;
    private StartedMockNode a, b, c;

//    @Before
//    public void setup() {
//
//        MockNetworkParameters mockNetworkParameters = new MockNetworkParameters().withNotarySpecs(Arrays.asList(new MockNetworkNotarySpec(new CordaX500Name("Notary", "London", "GB"))));
//        mockNetwork = new MockNetwork(Arrays.asList("net.corda.accord", "net.corda.finance.contracts.asset", "net.corda.finance.schemas"), mockNetworkParameters);
//        System.out.println(mockNetwork);
//
//        a = mockNetwork.createNode(new MockNodeParameters());
//        b = mockNetwork.createNode(new MockNodeParameters());
//        c = mockNetwork.createNode(new MockNodeParameters());
//
//        ArrayList<StartedMockNode> startedNodes = new ArrayList<>();
//        startedNodes.add(a);
//        startedNodes.add(b);
//        startedNodes.add(c);
//
//        // For real nodes this happens automatically, but we have to manually register the flow for tests
//        startedNodes.forEach(el -> el.registerInitiatedFlow(PromissoryNoteSettleFlow.Responder.class));
//        mockNetwork.runNetwork();
//    }
//
//    @After
//    public void tearDown() {
//        mockNetwork.stopNodes();
//    }
//
//    @Rule
//    public final ExpectedException exception = ExpectedException.none();
//
//    private SignedTransaction issueIOU(PromissoryNoteState iouState) throws InterruptedException, ExecutionException {
//        PromissoryNoteIssueFlow.InitiatorFlow flow = new PromissoryNoteIssueFlow.InitiatorFlow(iouState);
//        CordaFuture future = a.startFlow(flow);
//        mockNetwork.runNetwork();
//        return (SignedTransaction) future.get();
//    }
//
//    private Cash.State issueCash(Amount<Currency> amount) throws InterruptedException, ExecutionException {
//        SelfIssueCashFlow flow = new SelfIssueCashFlow(amount);
//        CordaFuture future = a.startFlow(flow);
//        mockNetwork.runNetwork();
//        return (Cash.State) future.get();
//    }

    /**
     * Task 1.
     * The first task is to grab the [PromissoryNoteState] for the given [linearId] from the vault, assemble a transaction
     * and sign it.
     * TODO: Grab the IOU for the given [linearId] from the vault, build and sign the settle transaction.
     * Hints:
     * - Use the code from the [PromissoryNoteTransferFlow] to get the correct [PromissoryNoteState] from the vault.
     * - You will need to use the [Cash.generateSpend] functionality of the vault to add the cash states and cash command
     *   to your transaction. The API is quite simple. It takes a reference to a [TransactionBuilder], an [Amount] and
     *   the [Party] object for the recipient. The function will mutate your builder by adding the states and commands.
     * - You then need to produce the output [PromissoryNoteState] by using the [PromissoryNoteState.pay] function.
     * - Add the input [PromissoryNoteState] [StateAndRef] and the new output [PromissoryNoteState] to the transaction.
     * - Sign the transaction and return it.
     */

//    @Test
//    public void flowReturnsCorrectlyFormedPartiallySignedTransaction() throws Exception {
//        SignedTransaction stx = issueIOU(new PromissoryNoteState(Currencies.POUNDS(10), b.getInfo().getLegalIdentities().get(0), a.getInfo().getLegalIdentities().get(0)));
//        issueCash(Currencies.POUNDS(5));
//        PromissoryNoteState inputIOU = stx.getTx().outputsOfType(PromissoryNoteState.class).get(0);
//        PromissoryNoteSettleFlow.InitiatorFlow flow = new PromissoryNoteSettleFlow.InitiatorFlow(inputIOU.getLinearId(), Currencies.POUNDS(5));
//        Future<SignedTransaction> futureSettleResult = a.startFlow(flow);
//
//        mockNetwork.runNetwork();
//
//        SignedTransaction settleResult = futureSettleResult.get();
//        // Check the transaction is well formed...
//        // One output PromissoryNoteState, one input PromissoryNoteState reference, input and output cash
//        a.transaction(() -> {
//            try {
//                LedgerTransaction ledgerTx = settleResult.toLedgerTransaction(a.getServices(), false);
//                assert(ledgerTx.getInputs().size() == 2);
//                assert(ledgerTx.getOutputs().size() == 2);
//
//                PromissoryNoteState outputIOU = ledgerTx.outputsOfType(PromissoryNoteState.class).get(0);
//                PromissoryNoteState correctOutputIOU = inputIOU.pay(Currencies.POUNDS(5));
//
//                assert (outputIOU.amount.equals(correctOutputIOU.amount));
//                assert (outputIOU.paid.equals(correctOutputIOU.paid));
//                assert (outputIOU.lender.equals(correctOutputIOU.lender));
//                assert (outputIOU.borrower.equals(correctOutputIOU.borrower));
//
//                // Sum all the output cash. This is complicated as there may be multiple cash output states with not all of them
//                // being assigned to the lender.
//                List<Cash.State> outputCash = ledgerTx.getOutputs().stream()
//                        .map(state -> (Cash.State) state.getData())
//                        .filter(state -> state.getOwner().getOwningKey().equals(b.getInfo().getLegalIdentities().get(0).getOwningKey()))
//                        .collect(Collectors.toList());
//
//                // Sum the acceptable cash sent to the lender
//                Amount<Currency> outputCashSum = new Amount<>(0, inputIOU.amount.getToken());
//                for (Cash.State cash: outputCash) {
//                    Amount<Currency> addCash = new Amount<>(cash.getAmount().getQuantity(), cash.getAmount().getToken().getProduct());
//                    outputCashSum = outputCashSum.plus(addCash);
//                }
//
//                assert (outputCashSum.equals(inputIOU.amount.minus(inputIOU.paid).minus(outputIOU.paid)));
//
//                CommandWithParties command = ledgerTx.getCommands().get(0);
//                assert (command.getValue().equals(new PromissoryNoteContract.Commands.Settle()));
//
//                settleResult.verifySignaturesExcept(b.getInfo().getLegalIdentities().get(0).getOwningKey(),
//                        mockNetwork.getDefaultNotaryIdentity().getOwningKey());
//
//                return null;
//            } catch (Exception exception) {
//                System.out.println(exception);
//            }
//            return null;
//        });
//
//    }

    /**
     * Task 2.
     * Only the borrower should be running this flow for a particular IOU.
     * TODO: Grab the IOU for the given [linearId] from the vault and check the node running the flow is the borrower.
     * Hint: Use the data within the iou obtained from the vault to check the right node is running the flow.
     */

//    @Test
//    public void settleFlowCanOnlyBeRunByBorrower() throws Exception {
//        SignedTransaction stx = issueIOU(new PromissoryNoteState(Currencies.POUNDS(10), b.getInfo().getLegalIdentities().get(0), a.getInfo().getLegalIdentities().get(0)));
//        issueCash(Currencies.POUNDS(5));
//        PromissoryNoteState inputIOU = stx.getTx().outputsOfType(PromissoryNoteState.class).get(0);
//        PromissoryNoteSettleFlow.InitiatorFlow flow = new PromissoryNoteSettleFlow.InitiatorFlow(inputIOU.getLinearId(), Currencies.POUNDS(5));
//        Future<SignedTransaction> futureSettleResult = b.startFlow(flow);
//
//        try {
//            mockNetwork.runNetwork();
//            futureSettleResult.get();
//        } catch (Exception exception) {
//            assert exception.getMessage().equals("java.lang.IllegalArgumentException: The borrower must issue the flow");
//        }
//    }

    /**
     * Task 3.
     * The borrower must have at least SOME cash in the right currency to pay the lender.
     * TODO: Add a check in the flow to ensure that the borrower has a balance of cash in the right currency.
     * Hint:
     * - Use [serviceHub.getCashBalances] - it is a map which can be queried by [Currency].
     * - Use an if statement to check there is cash in the right currency present.
     */

//    @Test
//    public void borrowerMustHaveCashInRightCurrency() throws Exception {
//        SignedTransaction stx = issueIOU(new PromissoryNoteState(Currencies.POUNDS(10), b.getInfo().getLegalIdentities().get(0), a.getInfo().getLegalIdentities().get(0)));
//        issueCash(Currencies.POUNDS(5));
//        PromissoryNoteState inputIOU = stx.getTx().outputsOfType(PromissoryNoteState.class).get(0);
//        PromissoryNoteSettleFlow.InitiatorFlow flow = new PromissoryNoteSettleFlow.InitiatorFlow(inputIOU.getLinearId(), Currencies.POUNDS(5));
//        Future<SignedTransaction> futureSettleResult = a.startFlow(flow);
//
//        try {
//            mockNetwork.runNetwork();
//            futureSettleResult.get();
//        } catch (Exception exception) {
//            assert exception.getMessage().equals("java.lang.IllegalArgumentException: Borrower has no GBP to settle.");
//        }
//    }

    /**
     * Task 4.
     * The borrower must have enough cash in the right currency to pay the lender.
     * TODO: Add a check in the flow to ensure that the borrower has enough cash to pay the lender.
     * Hint: Add another if statement similar to the one required above.
     */

//    @Test
//    public void borrowerMustHaveEnoughCashInRightCurrency() throws Exception {
//        SignedTransaction stx = issueIOU(new PromissoryNoteState(Currencies.POUNDS(10), b.getInfo().getLegalIdentities().get(0), a.getInfo().getLegalIdentities().get(0)));
//        PromissoryNoteState inputIOU = stx.getTx().outputsOfType(PromissoryNoteState.class).get(0);
//        PromissoryNoteSettleFlow.InitiatorFlow flow = new PromissoryNoteSettleFlow.InitiatorFlow(inputIOU.getLinearId(), Currencies.POUNDS(5));
//        Future<SignedTransaction> futureSettleResult = a.startFlow(flow);
//
//        try {
//            mockNetwork.runNetwork();
//            futureSettleResult.get();
//        } catch (Exception exception) {
//            assert exception.getMessage().equals("java.lang.IllegalArgumentException: Borrower doesn't have enough cash to settle with the amount specified.");
//        }
//    }

    /**
     * Task 5.
     * We need to get the transaction signed by the other party.
     * TODO: Use a subFlow call to [initateFlow] and the [SignTransactionFlow] to get a signature from the lender.
     */

//    @Test
//    public void flowReturnsTransactionSignedByBothParties() throws Exception {
//        SignedTransaction stx = issueIOU(new PromissoryNoteState(Currencies.POUNDS(10), b.getInfo().getLegalIdentities().get(0), a.getInfo().getLegalIdentities().get(0)));
//        issueCash(Currencies.POUNDS(5));
//        PromissoryNoteState inputIOU = stx.getTx().outputsOfType(PromissoryNoteState.class).get(0);
//        PromissoryNoteSettleFlow.InitiatorFlow flow = new PromissoryNoteSettleFlow.InitiatorFlow(inputIOU.getLinearId(), Currencies.POUNDS(5));
//        Future<SignedTransaction> futureSettleResult = a.startFlow(flow);
//
//        try {
//            mockNetwork.runNetwork();
//            futureSettleResult.get().verifySignaturesExcept(mockNetwork.getDefaultNotaryIdentity().getOwningKey());
//        } catch (Exception exception) {
//            assert exception.getMessage().equals("java.lang.IllegalArgumentException: Borrower has no GBP to settle.");
//        }
//    }

    /**
     * Task 6.
     * We need to get the transaction signed by the notary service
     * TODO: Use a subFlow call to the [FinalityFlow] to get a signature from the lender.
     */

//    @Test
//    public void flowReturnsCommittedTransaction() throws Exception {
//        SignedTransaction stx = issueIOU(new PromissoryNoteState(Currencies.POUNDS(10), b.getInfo().getLegalIdentities().get(0), a.getInfo().getLegalIdentities().get(0)));
//        issueCash(Currencies.POUNDS(5));
//        PromissoryNoteState inputIOU = stx.getTx().outputsOfType(PromissoryNoteState.class).get(0);
//        PromissoryNoteSettleFlow.InitiatorFlow flow = new PromissoryNoteSettleFlow.InitiatorFlow(inputIOU.getLinearId(), Currencies.POUNDS(5));
//        Future<SignedTransaction> futureSettleResult = a.startFlow(flow);
//
//        try {
//            mockNetwork.runNetwork();
//            futureSettleResult.get().verifyRequiredSignatures();
//        } catch (Exception exception) {
//            assert exception.getMessage().equals("java.lang.IllegalArgumentException: Borrower has no GBP to settle.");
//        }
//    }

}
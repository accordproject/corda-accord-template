package net.corda.accord.flow;

import net.corda.testing.node.*;

/**
 * TODO: Write tests for promissory note transfer flow once functionality is complete.
 */
public class PromissoryNoteTransferFlowTests {

    private MockNetwork mockNetwork;
    private StartedMockNode a, b, c;

//    @Before
//    public void setup() {
//        MockNetworkParameters mockNetworkParameters = new MockNetworkParameters().withNotarySpecs(Arrays.asList(new MockNetworkNotarySpec(new CordaX500Name("Notary", "London", "GB"))));
//        mockNetwork = new MockNetwork(Arrays.asList("net.corda.accord"), mockNetworkParameters);
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
//        startedNodes.forEach(el -> el.registerInitiatedFlow(PromissoryNoteTransferFlow.Responder.class));
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

    /**
     * Task 1.
     * Build out the beginnings of [PromissoryNoteTransferFlow]!
     * TODO: Implement the [PromissoryNoteTransferFlow] flow which builds and returns a partially [SignedTransaction].
     * Hint:
     * - This flow will look similar to the [PromissoryNoteIssueFlow].
     * - This time our transaction has an input state, so we need to retrieve it from the vault!
     * - You can use the [getServiceHub().getVaultService().queryBy(Class, queryCriteria)] method to get the latest linear states of a particular
     *   type from the vault. It returns a list of states matching your query.
     * - Use the [UniqueIdentifier] which is passed into the flow to create the appropriate Query Criteria.
     * - Use the [PromissoryNoteState.withNewLender] method to create a copy of the state with a new lender.
     * - Create a Command - we will need to use the Transfer command.
     * - Remember, as we are involving three parties we will need to collect three signatures, so need to add three
     *   [PublicKey]s to the Command's signers list. We can get the signers from the input IOU and the new IOU you
     *   have just created with the new lender.
     * - Verify and sign the transaction as you did with the [PromissoryNoteIssueFlow].
     * - Return the partially signed transaction.
     */

//    @Test
//    public void flowReturnsCorrectlyFormedPartiallySignedTransaction() throws Exception {
//        Party lender = a.getInfo().getLegalIdentitiesAndCerts().get(0).getParty();
//        Party borrower = b.getInfo().getLegalIdentitiesAndCerts().get(0).getParty();
//        SignedTransaction stx = issueIOU(new PromissoryNoteState(Currencies.DOLLARS(10), lender, borrower));
//        PromissoryNoteState inputIou = (PromissoryNoteState) stx.getTx().getOutputs().get(0).getData();
//        PromissoryNoteTransferFlow.InitiatorFlow flow = new PromissoryNoteTransferFlow.InitiatorFlow(inputIou.getLinearId(), c.getInfo().getLegalIdentities().get(0));
//        Future<SignedTransaction> future = a.startFlow(flow);
//
//        mockNetwork.runNetwork();
//
//        SignedTransaction ptx = future.get();
//
//        // Check the transaction is well formed...
//        // One output PromissoryNoteState, one input state reference and a Transfer command with the right properties.
//        assert (ptx.getTx().getInputs().size() == 1);
//        assert (ptx.getTx().getOutputs().size() == 1);
//        assert (ptx.getTx().getOutputs().get(0).getData() instanceof PromissoryNoteState);
//        assert (ptx.getTx().getInputs().get(0).equals(new StateRef(stx.getId(), 0)));
//
//        PromissoryNoteState outputIOU = (PromissoryNoteState) ptx.getTx().getOutput(0);
//        Command command = ptx.getTx().getCommands().get(0);
//
//        assert (command.getValue().equals(new PromissoryNoteCordaContract.Commands.Transfer()));
//        ptx.verifySignaturesExcept(b.getInfo().getLegalIdentities().get(0).getOwningKey(), c.getInfo().getLegalIdentities().get(0).getOwningKey(), mockNetwork.getDefaultNotaryIdentity().getOwningKey());
//    }

    /**
     * Task 2.
     * We need to make sure that only the current lender can execute this flow.
     * TODO: Amend the [PromissoryNoteTransferFlow] to only allow the current lender to execute the flow.
     * Hint:
     * - Remember: You can use the node's identity and compare it to the [Party] object within the [PromissoryNoteState] you
     *   retrieved from the vault.
     * - Throw an [IllegalArgumentException] if the wrong party attempts to run the flow!
     */

//    @Test
//    public void flowCanOnlyBeRunByCurrentLender() throws Exception {
//        Party lender = a.getInfo().getLegalIdentitiesAndCerts().get(0).getParty();
//        Party borrower = b.getInfo().getLegalIdentitiesAndCerts().get(0).getParty();
//        SignedTransaction stx = issueIOU(new PromissoryNoteState(Currencies.DOLLARS(10), lender, borrower));
//        PromissoryNoteState inputIou = (PromissoryNoteState) stx.getTx().getOutputs().get(0).getData();
//        PromissoryNoteTransferFlow.InitiatorFlow flow = new PromissoryNoteTransferFlow.InitiatorFlow(inputIou.getLinearId(), c.getInfo().component2().get(0).getParty());
//        Future<SignedTransaction> future = b.startFlow(flow);
//        try {
//            mockNetwork.runNetwork();
//            future.get();
//        } catch (Exception exception) {
//            assert exception.getMessage().equals("java.lang.IllegalArgumentException: This flow must be run by the current lender.");
//        }
//
//    }

    /**
     * Task 3.
     * Check that an [PromissoryNoteState] cannot be transferred to the same lender.
     * TODO: You shouldn't have to do anything additional to get this test to pass. Belts and Braces!
     */

//    @Test
//    public void iouCannotBeTransferredToSameParty() throws Exception {
//        Party lender = a.getInfo().getLegalIdentitiesAndCerts().get(0).getParty();
//        Party borrower = b.getInfo().getLegalIdentitiesAndCerts().get(0).getParty();
//        SignedTransaction stx = issueIOU(new PromissoryNoteState(Currencies.DOLLARS(10), lender, borrower));
//        PromissoryNoteState inputIou = (PromissoryNoteState) stx.getTx().getOutputs().get(0).getData();
//        PromissoryNoteTransferFlow.InitiatorFlow flow = new PromissoryNoteTransferFlow.InitiatorFlow(inputIou.getLinearId(), c.getInfo().component2().get(0).getParty());
//        Future<SignedTransaction> future = a.startFlow(flow);
//        try {
//            mockNetwork.runNetwork();
//            future.get();
//        } catch (Exception exception) {
//            System.out.println(exception.getMessage());
//            assert exception.getMessage().equals("Contract verification failed: Failed requirement: The lender property must change in a transfer.");
//        }
//
//    }

    /**
     * Task 4.
     * Get the borrowers and the new lenders signatures.
     * TODO: Amend the [PromissoryNoteTransferFlow] to handle collecting signatures from multiple parties.
     * Hint: use [initiateFlow] and the [CollectSignaturesFlow] in the same way you did for the [PromissoryNoteIssueFlow].
     */

//    @Test
//    public void flowReturnsTransactionSignedBtAllParties() throws Exception {
//        Party lender = a.getInfo().getLegalIdentitiesAndCerts().get(0).getParty();
//        Party borrower = b.getInfo().getLegalIdentitiesAndCerts().get(0).getParty();
//        SignedTransaction stx = issueIOU(new PromissoryNoteState(Currencies.DOLLARS(10), lender, borrower));
//        PromissoryNoteState inputIou = (PromissoryNoteState) stx.getTx().getOutputs().get(0).getData();
//        PromissoryNoteTransferFlow.InitiatorFlow flow = new PromissoryNoteTransferFlow.InitiatorFlow(inputIou.getLinearId(), lender);
//        Future<SignedTransaction> future = a.startFlow(flow);
//        try {
//            mockNetwork.runNetwork();
//            future.get();
//            stx.verifySignaturesExcept(mockNetwork.getDefaultNotaryIdentity().getOwningKey());
//        } catch (Exception exception) {
//            System.out.println(exception.getMessage());
//        }
//
//    }

    /**
     * Task 5.
     * We need to get the transaction signed by the notary service
     * TODO: Use a subFlow call to the [FinalityFlow] to get a signature from the lender.
     */

//    @Test
//    public void flowReturnsTransactionSignedByAllPartiesAndNotary() throws Exception {
//        Party lender = a.getInfo().getLegalIdentitiesAndCerts().get(0).getParty();
//        Party borrower = b.getInfo().getLegalIdentitiesAndCerts().get(0).getParty();
//        SignedTransaction stx = issueIOU(new PromissoryNoteState(Currencies.DOLLARS(10), lender, borrower));
//        PromissoryNoteState inputIou = (PromissoryNoteState) stx.getTx().getOutputs().get(0).getData();
//        PromissoryNoteTransferFlow.InitiatorFlow flow = new PromissoryNoteTransferFlow.InitiatorFlow(inputIou.getLinearId(), c.getInfo().component2().get(0).getParty());
//        Future<SignedTransaction> future = a.startFlow(flow);
//        try {
//            mockNetwork.runNetwork();
//            future.get();
//            stx.verifyRequiredSignatures();
//        } catch (Exception exception) {
//            System.out.println(exception.getMessage());
//        }
//    }

}
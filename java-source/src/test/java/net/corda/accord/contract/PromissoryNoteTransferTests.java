package net.corda.accord.contract;

import net.corda.core.contracts.Amount;
import net.corda.core.contracts.CommandData;
import net.corda.core.contracts.PartyAndReference;
import net.corda.core.contracts.TypeOnlyCommandData;
import net.corda.core.identity.AbstractParty;
import net.corda.core.utilities.OpaqueBytes;
import net.corda.finance.contracts.asset.Cash;
import net.corda.testing.node.MockServices;

import java.util.Arrays;
import java.util.Currency;

import static net.corda.testing.node.NodeTestUtils.ledger;

/**
 * TODO: Modify tests to adapt from IOU to promissory note.
 */

public class PromissoryNoteTransferTests {

    public interface Commands extends CommandData {
        class DummyCommand extends TypeOnlyCommandData implements Commands{}
    }

    static private final MockServices ledgerServices = new MockServices(Arrays.asList("net.corda.accord"));

    // A dummy state
//    PromissoryNoteState dummyState = new PromissoryNoteState(Currencies.DOLLARS(0), CHARLIE.getParty(), CHARLIE.getParty());

    // function to create new Cash states.
    private Cash.State createCashState(AbstractParty owner, Amount<Currency> amount) {
        OpaqueBytes defaultBytes = new OpaqueBytes(new byte[1]);
        PartyAndReference partyAndReference = new PartyAndReference(owner, defaultBytes);
        return new Cash.State(partyAndReference, amount, owner);
    }


    /**
     * Task 1.
     * Now things are going to get interesting!
     * We need the [PromissoryNoteContract] to not only handle Issues of IOUs but now also Transfers.
     * Of course, we'll need to add a new Command and add some additional contract code to handle Transfers.
     * TODO: Add a "Transfer" command to the PromissoryNoteState and update the verify() function to handle multiple commands.
     * Hint:
     * - As with the [Issue] command, add the [Transfer] command within the [PromissoryNoteContract.Commands].
     * - Again, we only care about the existence of the [Transfer] command in a transaction, therefore it should
     *   subclass the [TypeOnlyCommandData].
     * - You can use the [requireSingleCommand] function to check for the existence of a command which implements a
     *   specified interface:
     *
     *       final CommandWithParties<Commands> command = requireSingleCommand(tx.getCommands(), Commands.class);
     *       final Commands commandData = command.getValue();
     *
     *   To match any command that implements [PromissoryNoteContract.Commands]
     * - We then need conditional logic based on the type of [Command.value], in Java you can do this using an "if-else" statement
     * - For each "if", or "elseIf" block, you can check the type of [Command.value]:
     *
     *        if (commandData.equals(new Commands.Issue())) {
     *        requireThat(require -> {...})
     *        } else if (...) {}
     *
     * - The [requireSingleCommand] function will handle unrecognised types for you (see first unit test).
     */

//    @Test
//    public void mustHandleMultipleCommandValues() {
//        PromissoryNoteState iou = new PromissoryNoteState(Currencies.DOLLARS(10), ALICE.getParty(), BOB.getParty());
//        ledger(ledgerServices, l -> {
//            l.transaction(tx -> {
//                tx.output(PromissoryNoteContract.IOU_CONTRACT_ID, iou);
//                tx.command(Arrays.asList(ALICE.getPublicKey(), BOB.getPublicKey()), new Commands.DummyCommand());
//                return tx.failsWith("Required PromissoryNoteContract.Commands command");
//            });
//            l.transaction(tx -> {
//                tx.output(PromissoryNoteContract.IOU_CONTRACT_ID, iou);
//                tx.command(Arrays.asList(ALICE.getPublicKey(), BOB.getPublicKey()), new PromissoryNoteContract.Commands.Issue());
//                return tx.verifies();
//            });
//            l.transaction(tx -> {
//                tx.input(PromissoryNoteContract.IOU_CONTRACT_ID, iou);
//                tx.output(PromissoryNoteContract.IOU_CONTRACT_ID, iou.withNewLender(CHARLIE.getParty()));
//                tx.command(Arrays.asList(ALICE.getPublicKey(), BOB.getPublicKey(), CHARLIE.getPublicKey()), new PromissoryNoteContract.Commands.Transfer());
//                return tx.verifies();
//            });
//            return null;
//        });
//    }

    /**
     * Task 2.
     * The transfer transaction should only have one input state and one output state.
     * TODO: Add constraints to the contract code to ensure a transfer transaction has only one input and output state.
     * Hint:
     * - Look at the contract code for "Issue".
     */

//    @Test
//    public void mustHaveOneInputAndOneOutput() {
//        PromissoryNoteState iou = new PromissoryNoteState(Currencies.DOLLARS(10), ALICE.getParty(), BOB.getParty());
//        ledger(ledgerServices, l -> {
//            l.transaction(tx -> {
//                tx.input(PromissoryNoteContract.IOU_CONTRACT_ID, iou);
//                tx.input(PromissoryNoteContract.IOU_CONTRACT_ID, dummyState);
//                tx.output(PromissoryNoteContract.IOU_CONTRACT_ID, iou.withNewLender(CHARLIE.getParty()));
//                tx.command(Arrays.asList(ALICE.getPublicKey(), BOB.getPublicKey(), CHARLIE.getPublicKey()), new PromissoryNoteContract.Commands.Transfer());
//                return tx.failsWith("An IOU transfer transaction should only consume one input state.");
//            });
//            l.transaction(tx -> {
//                tx.output(PromissoryNoteContract.IOU_CONTRACT_ID, iou.withNewLender(CHARLIE.getParty()));
//                tx.command(Arrays.asList(ALICE.getPublicKey(), BOB.getPublicKey(), CHARLIE.getPublicKey()), new PromissoryNoteContract.Commands.Transfer());
//                return tx.failsWith("An IOU transfer transaction should only consume one input state.");
//            });
//            l.transaction(tx -> {
//                tx.input(PromissoryNoteContract.IOU_CONTRACT_ID, iou);
//                tx.command(Arrays.asList(ALICE.getPublicKey(), BOB.getPublicKey(), CHARLIE.getPublicKey()), new PromissoryNoteContract.Commands.Transfer());
//                return tx.failsWith("An IOU transfer transaction should only create one output state.");
//            });
//            l.transaction(tx -> {
//                tx.input(PromissoryNoteContract.IOU_CONTRACT_ID, iou);
//                tx.output(PromissoryNoteContract.IOU_CONTRACT_ID, iou.withNewLender(CHARLIE.getParty()));
//                tx.output(PromissoryNoteContract.IOU_CONTRACT_ID, dummyState);
//                tx.command(Arrays.asList(ALICE.getPublicKey(), BOB.getPublicKey(), CHARLIE.getPublicKey()), new PromissoryNoteContract.Commands.Transfer());
//                return tx.failsWith(" An IOU transfer transaction should only create one output state.");
//            });
//            l.transaction(tx -> {
//                tx.input(PromissoryNoteContract.IOU_CONTRACT_ID, iou);
//                tx.output(PromissoryNoteContract.IOU_CONTRACT_ID, iou.withNewLender(CHARLIE.getParty()));
//                tx.command(Arrays.asList(ALICE.getPublicKey(), BOB.getPublicKey(), CHARLIE.getPublicKey()),new PromissoryNoteContract.Commands.Transfer());
//                return tx.verifies();
//            });
//            return null;
//        });
//    }

    /**
     * Task 3.
     * TODO: Add a constraint to the contract code to ensure only the lender property can change when transferring IOUs.
     * Hint:
     * - You should create a private internal copy constructor, accessible via a copy method on your PromissoryNoteState.
     * - You can then compare a copy of the input to the output with the lender of the output as the lender of the input.
     * - You'll need references to the input and output ious.
     * - Remember you need to cast the [ContractState]s to [PromissoryNoteState]s.
     * - It's easier to take this approach then check all properties other than the lender haven't changed, including
     *   the [linearId] and the [contract]!
     */

//    @Test
//    public void onlyTheLenderMayChange() {
//        PromissoryNoteState iou = new PromissoryNoteState(Currencies.DOLLARS(10), ALICE.getParty(), BOB.getParty());
//        ledger(ledgerServices, l -> {
//            l.transaction(tx -> {
//                tx.input(PromissoryNoteContract.IOU_CONTRACT_ID, iou);
//                tx.output(PromissoryNoteContract.IOU_CONTRACT_ID, iou.copy(Currencies.DOLLARS(1), ALICE.getParty(), BOB.getParty(), Currencies.DOLLARS(0)));
//                tx.command(Arrays.asList(ALICE.getPublicKey(), BOB.getPublicKey(), CHARLIE.getPublicKey()), new PromissoryNoteContract.Commands.Transfer());
//                return tx.failsWith("Only the lender property may change.");
//            });
//            l.transaction(tx -> {
//                tx.input(PromissoryNoteContract.IOU_CONTRACT_ID, iou);
//                tx.output(PromissoryNoteContract.IOU_CONTRACT_ID, iou.copy(Currencies.DOLLARS(10), ALICE.getParty(), CHARLIE.getParty(), Currencies.DOLLARS(0)));
//                tx.command(Arrays.asList(ALICE.getPublicKey(), BOB.getPublicKey(), CHARLIE.getPublicKey()), new PromissoryNoteContract.Commands.Transfer());
//                return tx.failsWith("Only the lender property may change.");
//            });
//            l.transaction(tx -> {
//                tx.input(PromissoryNoteContract.IOU_CONTRACT_ID, iou);
//                tx.output(PromissoryNoteContract.IOU_CONTRACT_ID, iou.copy(Currencies.DOLLARS(10), ALICE.getParty(), BOB.getParty(), Currencies.DOLLARS(5)));
//                tx.command(Arrays.asList(ALICE.getPublicKey(), BOB.getPublicKey(), CHARLIE.getPublicKey()), new PromissoryNoteContract.Commands.Transfer());
//                return tx.failsWith("Only the lender property may change.");
//            });
//            l.transaction(tx -> {
//                tx.input(PromissoryNoteContract.IOU_CONTRACT_ID, iou);
//                tx.output(PromissoryNoteContract.IOU_CONTRACT_ID, iou.copy(Currencies.DOLLARS(10), CHARLIE.getParty(), BOB.getParty(), Currencies.DOLLARS(0)));
//                tx.command(Arrays.asList(ALICE.getPublicKey(), BOB.getPublicKey(), CHARLIE.getPublicKey()), new PromissoryNoteContract.Commands.Transfer());
//                return tx.verifies();
//            });
//            return null;
//        });
//    }

    /**
     * Task 4.
     * It is fairly obvious that in a transfer IOU transaction the lender must change.
     * TODO: Add a constraint to check the lender has changed in the output IOU.
     */
//    @Test
//    public void theLenderMustChange() {
//        PromissoryNoteState iou = new PromissoryNoteState(Currencies.DOLLARS(10), ALICE.getParty(), BOB.getParty());
//        ledger(ledgerServices, l -> {
//            l.transaction(tx -> {
//                tx.input(PromissoryNoteContract.IOU_CONTRACT_ID, iou);
//                tx.output(PromissoryNoteContract.IOU_CONTRACT_ID, iou);
//                tx.command(Arrays.asList(ALICE.getPublicKey(), BOB.getPublicKey()), new PromissoryNoteContract.Commands.Transfer());
//                return tx.failsWith("The lender property must change in a transfer.");
//            });
//            l.transaction(tx -> {
//                tx.input(PromissoryNoteContract.IOU_CONTRACT_ID, iou);
//                tx.output(PromissoryNoteContract.IOU_CONTRACT_ID, iou.copy(Currencies.DOLLARS(10), CHARLIE.getParty(), BOB.getParty(), Currencies.DOLLARS(0)));
//                tx.command(Arrays.asList(ALICE.getPublicKey(), BOB.getPublicKey(), CHARLIE.getPublicKey()), new PromissoryNoteContract.Commands.Transfer());
//                return tx.verifies();
//            });
//            return null;
//        });
//    }

    /**
     * Task 5.
     * All the participants in a transfer IOU transaction must sign.
     * TODO: Add a constraint to check the old lender, the new lender and the recipient have signed.
     */
//
//    @Test
//    public void allParticipantsMustSign() {
//        PromissoryNoteState iou = new PromissoryNoteState(Currencies.DOLLARS(10), ALICE.getParty(), BOB.getParty());
//        ledger(ledgerServices, l -> {
//            l.transaction(tx -> {
//                tx.input(PromissoryNoteContract.IOU_CONTRACT_ID, iou);
//                tx.output(PromissoryNoteContract.IOU_CONTRACT_ID, iou.copy(Currencies.DOLLARS(10), CHARLIE.getParty(), BOB.getParty(), Currencies.DOLLARS(0)));
//                tx.command(Arrays.asList(ALICE.getPublicKey(), BOB.getPublicKey()), new PromissoryNoteContract.Commands.Transfer());
//                return tx.failsWith("The borrower, old lender and new lender only must sign an IOU transfer transaction");
//            });
//            l.transaction(tx -> {
//                tx.input(PromissoryNoteContract.IOU_CONTRACT_ID, iou);
//                tx.output(PromissoryNoteContract.IOU_CONTRACT_ID, iou.copy(Currencies.DOLLARS(10), CHARLIE.getParty(), BOB.getParty(), Currencies.DOLLARS(0)));
//                tx.command(Arrays.asList(ALICE.getPublicKey(), CHARLIE.getPublicKey()), new PromissoryNoteContract.Commands.Transfer());
//                return tx.failsWith("The borrower, old lender and new lender only must sign an IOU transfer transaction");
//            });
//            l.transaction(tx -> {
//                tx.input(PromissoryNoteContract.IOU_CONTRACT_ID, iou);
//                tx.output(PromissoryNoteContract.IOU_CONTRACT_ID, iou.copy(Currencies.DOLLARS(10), CHARLIE.getParty(), BOB.getParty(), Currencies.DOLLARS(0)));
//                tx.command(Arrays.asList(CHARLIE.getPublicKey(), BOB.getPublicKey()), new PromissoryNoteContract.Commands.Transfer());
//                return tx.failsWith("The borrower, old lender and new lender only must sign an IOU transfer transaction");
//            });
//            l.transaction(tx -> {
//                tx.input(PromissoryNoteContract.IOU_CONTRACT_ID, iou);
//                tx.output(PromissoryNoteContract.IOU_CONTRACT_ID, iou.copy(Currencies.DOLLARS(10), CHARLIE.getParty(), BOB.getParty(), Currencies.DOLLARS(0)));
//                tx.command(Arrays.asList(ALICE.getPublicKey(), BOB.getPublicKey(), MINICORP.getPublicKey()), new PromissoryNoteContract.Commands.Transfer());
//                return tx.failsWith("The borrower, old lender and new lender only must sign an IOU transfer transaction");
//            });
//            l.transaction(tx -> {
//                tx.input(PromissoryNoteContract.IOU_CONTRACT_ID, iou);
//                tx.output(PromissoryNoteContract.IOU_CONTRACT_ID, iou.copy(Currencies.DOLLARS(10), CHARLIE.getParty(), BOB.getParty(), Currencies.DOLLARS(0)));
//                tx.command(Arrays.asList(ALICE.getPublicKey(), BOB.getPublicKey(), CHARLIE.getPublicKey(), MINICORP.getPublicKey()), new PromissoryNoteContract.Commands.Transfer());
//                return tx.failsWith("The borrower, old lender and new lender only must sign an IOU transfer transaction");
//            });
//            l.transaction(tx -> {
//                tx.input(PromissoryNoteContract.IOU_CONTRACT_ID, iou);
//                tx.output(PromissoryNoteContract.IOU_CONTRACT_ID, iou.copy(Currencies.DOLLARS(10), CHARLIE.getParty(), BOB.getParty(), Currencies.DOLLARS(0)));
//                tx.command(Arrays.asList(ALICE.getPublicKey(), BOB.getPublicKey(), CHARLIE.getPublicKey()), new PromissoryNoteContract.Commands.Transfer());
//                return tx.verifies();
//            });
//            return null;
//        });
//    }

}

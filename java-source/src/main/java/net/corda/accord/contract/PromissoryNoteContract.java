package net.corda.accord.contract;

import net.corda.core.contracts.*;

import static net.corda.core.contracts.ContractsDSL.requireSingleCommand;
import static net.corda.core.contracts.ContractsDSL.requireThat;

import net.corda.core.identity.AbstractParty;
import net.corda.core.transactions.LedgerTransaction;

import net.corda.accord.state.PromissoryNoteState;

import java.security.PublicKey;
import java.util.*;

/**
 * This contract governs how promissory notes may be Issued, Settled or Transferred.
 */
public class PromissoryNoteContract implements Contract {
    public static final String PROMISSORY_NOTE_CONTRACT_ID = "net.corda.accord.contract.PromissoryNoteContract";
    
    public interface Commands extends CommandData {
        class Issue extends TypeOnlyCommandData implements Commands{};
        class Settle extends TypeOnlyCommandData implements Commands{};
        class Transfer extends TypeOnlyCommandData implements Commands{};
    }
    /**
     * The contract code for the [PromissoryNoteContract].
     * The constraints are self documenting so don't require any additional explanation.
     */
    @Override
    public void verify(LedgerTransaction tx) {

        final CommandWithParties<Commands> command = requireSingleCommand(tx.getCommands(), Commands.class);
        final Commands commandData = command.getValue();

        if (commandData.equals(new Commands.Issue())) {

            requireThat(require -> {

                require.using("No inputs should be consumed when issuing a Promissory Note.", tx.getInputStates().size() == 0);
                require.using( "Only one output state should be created when issuing a Promissory Note.", tx.getOutputStates().size() == 1);

                PromissoryNoteState outputState = tx.outputsOfType(PromissoryNoteState.class).get(0);
                require.using( "A newly issued Promissory Note must have a positive amount.", outputState.amount.getQuantity() > 0);
                require.using( "The lender and maker cannot have the same identity.", outputState.lenderCordaParty.getOwningKey() != outputState.makerCordaParty.getOwningKey());

                List<PublicKey> signers = tx.getCommands().get(0).getSigners();
                HashSet<PublicKey> signersSet = new HashSet<>();
                for (PublicKey key: signers) {
                    signersSet.add(key);
                }

                List<AbstractParty> participants = tx.getOutputStates().get(0).getParticipants();
                HashSet<PublicKey> participantKeys = new HashSet<>();
                for (AbstractParty party: participants) {
                    participantKeys.add(party.getOwningKey());
                }

                require.using("Both lender and maker together only may sign IOU issue transaction.", signersSet.containsAll(participantKeys) && signersSet.size() == 2);

                return null;
            });

        } else if (commandData.equals(new Commands.Settle())) {

            // TODO: Implement contract verification for promissory note settling.

        } else if (commandData.equals(new Commands.Transfer())) {

            // TODO: Implement contract verification for promissory note lender transfer.

        }
    }

}
// this code is generated and should not be modified
package org.accordproject.cicero.runtime;

import net.corda.core.serialization.CordaSerializable;
import org.hyperledger.composer.system.*;
import org.accordproject.base.Event;
import org.accordproject.base.Transaction;
import org.accordproject.base.Participant;
import org.accordproject.base.Asset;
import org.accordproject.cicero.contract.AccordContract;
import org.accordproject.cicero.contract.AccordContractState;
import org.accordproject.money.MonetaryAmount;
@CordaSerializable
public class PaymentObligation extends Obligation {
   
   // the accessor for the identifying field
   public String getID() {
      return eventId;
   }

   public MonetaryAmount amount;
   public String description;
}

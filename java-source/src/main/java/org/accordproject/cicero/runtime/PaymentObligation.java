// this code is generated and should not be modified
package org.accordproject.cicero.runtime;

import org.hyperledger.composer.system.*;
import net.corda.core.serialization.CordaSerializable;
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
      return this.getEventId();
   }

   private MonetaryAmount amount;
   private String description;
   public MonetaryAmount getAmount() {
      return this.amount;
   }
   public String getDescription() {
      return this.description;
   }
   public void setAmount(MonetaryAmount amount) {
      this.amount = amount;
   }
   public void setDescription(String description) {
      this.description = description;
   }
}

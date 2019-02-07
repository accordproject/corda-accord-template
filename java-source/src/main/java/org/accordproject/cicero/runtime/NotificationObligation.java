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
public class NotificationObligation extends Obligation {
   
   // the accessor for the identifying field
   public String getID() {
      return eventId;
   }

   public String title;
   public String message;

   public String getTitle() {
      return this.title;
   }
   public String getMessage() {
      return this.message;
   }

   public void setTitle(String title) {
      this.title = title;
   }
   public void setMessage(String message) {
      this.message = message;
   }
}

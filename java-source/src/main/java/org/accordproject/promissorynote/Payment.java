// this code is generated and should not be modified
package org.accordproject.promissorynote;

import org.hyperledger.composer.system.*;
import net.corda.core.serialization.CordaSerializable;
import org.accordproject.base.Event;
import org.accordproject.base.Transaction;
import org.accordproject.base.Participant;
import org.accordproject.base.Asset;
import org.accordproject.cicero.contract.*;
import org.accordproject.cicero.runtime.*;
import org.accordproject.money.MonetaryAmount;
import org.accordproject.usa.business.BusinessEntity;
@CordaSerializable
public class Payment extends Request {
   
   // the accessor for the identifying field
   public String getID() {
      return this.getTransactionId();
   }

   private MonetaryAmount amountPaid;
   public MonetaryAmount getAmountPaid() {
      return this.amountPaid;
   }
   public void setAmountPaid(MonetaryAmount amountPaid) {
      this.amountPaid = amountPaid;
   }
}

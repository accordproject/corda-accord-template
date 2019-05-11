// this code is generated and should not be modified
package org.accordproject.promissorynote;

import net.corda.core.serialization.CordaSerializable;
import org.accordproject.cicero.runtime.Request;
import org.accordproject.money.MonetaryAmount;

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

// this code is generated and should not be modified
package org.accordproject.promissorynote;

import net.corda.core.serialization.CordaSerializable;
import org.accordproject.cicero.runtime.Response;

@CordaSerializable
public class Result extends Response {
   
   // the accessor for the identifying field
   public String getID() {
      return this.getTransactionId();
   }

   private double outstandingBalance;
   public double getOutstandingBalance() {
      return this.outstandingBalance;
   }
   public void setOutstandingBalance(double outstandingBalance) {
      this.outstandingBalance = outstandingBalance;
   }
}

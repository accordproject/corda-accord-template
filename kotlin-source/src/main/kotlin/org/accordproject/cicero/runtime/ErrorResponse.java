// this code is generated and should not be modified
package org.accordproject.cicero.runtime;

import net.corda.core.serialization.CordaSerializable;
import org.accordproject.base.Transaction;

@CordaSerializable
public abstract class ErrorResponse extends Transaction {
   
   // the accessor for the identifying field
   public String getID() {
      return this.getTransactionId();
   }

}

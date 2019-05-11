// this code is generated and should not be modified
package org.accordproject.cicero.runtime;

import net.corda.core.serialization.CordaSerializable;
import org.accordproject.base.Transaction;

@CordaSerializable
public class Request extends Transaction {
   
   // the accessor for the identifying field
   public String getID() {
      return this.getTransactionId();
   }

}

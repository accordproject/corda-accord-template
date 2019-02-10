// this code is generated and should not be modified
package org.accordproject.base;

import org.hyperledger.composer.system.*;
import net.corda.core.serialization.CordaSerializable;
@CordaSerializable
public abstract class Transaction extends org.hyperledger.composer.system.Resource {
   
   // the accessor for the identifying field
   public String getID() {
      return this.getTransactionId();
   }

   private String transactionId;
   private java.util.Date timestamp;
   public String getTransactionId() {
      return this.transactionId;
   }
   public java.util.Date getTimestamp() {
      return this.timestamp;
   }
   public void setTransactionId(String transactionId) {
      this.transactionId = transactionId;
   }
   public void setTimestamp(java.util.Date timestamp) {
      this.timestamp = timestamp;
   }
}

// this code is generated and should not be modified
package org.accordproject.base;

import net.corda.core.serialization.CordaSerializable;
import org.hyperledger.composer.system.*;
@CordaSerializable
public abstract class Transaction extends org.hyperledger.composer.system.Resource {
   
   // the accessor for the identifying field
   public String getID() {
      return transactionId;
   }

   public String transactionId;
   public java.util.Date timestamp;
}

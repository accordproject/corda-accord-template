// this code is generated and should not be modified
package org.accordproject.base;

import net.corda.core.serialization.CordaSerializable;
import org.hyperledger.composer.system.*;
@CordaSerializable
public abstract class Event extends org.hyperledger.composer.system.Resource {
   
   // the accessor for the identifying field
   public String getID() {
      return eventId;
   }

   public String eventId;
   public java.util.Date timestamp;
}

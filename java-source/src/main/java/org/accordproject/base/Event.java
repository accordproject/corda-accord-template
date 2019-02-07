// this code is generated and should not be modified
package org.accordproject.base;

import org.hyperledger.composer.system.*;
import net.corda.core.serialization.CordaSerializable;
@CordaSerializable
public abstract class Event extends org.hyperledger.composer.system.Resource {
   
   // the accessor for the identifying field
   public String getID() {
      return eventId;
   }

   public String eventId;
   public java.util.Date timestamp;

   public String getEventId() {
      return this.eventId;
   }
   public java.util.Date getTimestamp() {
      return this.timestamp;
   }

   public void setEventId(String eventId) {
      this.eventId = eventId;
   }
   public void setTimestamp(java.util.Date timestamp) {
      this.timestamp = timestamp;
   }
}

// this code is generated and should not be modified
package org.accordproject.base;

import net.corda.core.serialization.CordaSerializable;
import org.hyperledger.composer.system.Resource;

@CordaSerializable
public abstract class Event extends Resource {
   
   // the accessor for the identifying field
   public String getID() {
      return this.getEventId();
   }

   private String eventId;
   private java.util.Date timestamp;
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

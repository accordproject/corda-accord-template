// this code is generated and should not be modified
package org.accordproject.cicero.runtime;

import net.corda.core.serialization.CordaSerializable;

@CordaSerializable
public class NotificationObligation extends Obligation {
   
   // the accessor for the identifying field
   public String getID() {
      return this.getEventId();
   }

   private String title;
   private String message;
   public String getTitle() {
      return this.title;
   }
   public String getMessage() {
      return this.message;
   }
   public void setTitle(String title) {
      this.title = title;
   }
   public void setMessage(String message) {
      this.message = message;
   }
}

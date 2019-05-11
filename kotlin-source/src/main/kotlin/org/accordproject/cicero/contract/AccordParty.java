// this code is generated and should not be modified
package org.accordproject.cicero.contract;

import net.corda.core.serialization.CordaSerializable;
import org.accordproject.base.Participant;

@CordaSerializable
public class AccordParty extends Participant {
   
   // the accessor for the identifying field
   public String getID() {
      return this.getPartyId();
   }

   private String partyId;
   public String getPartyId() {
      return this.partyId;
   }
   public void setPartyId(String partyId) {
      this.partyId = partyId;
   }
}

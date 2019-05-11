// this code is generated and should not be modified
package org.accordproject.cicero.contract;

import net.corda.core.serialization.CordaSerializable;
import org.accordproject.base.Asset;

@CordaSerializable
public class AccordContractState extends Asset {
   
   // the accessor for the identifying field
   public String getID() {
      return this.getStateId();
   }

   private String stateId;
   public String getStateId() {
      return this.stateId;
   }
   public void setStateId(String stateId) {
      this.stateId = stateId;
   }
}

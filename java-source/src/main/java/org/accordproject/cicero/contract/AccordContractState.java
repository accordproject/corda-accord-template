// this code is generated and should not be modified
package org.accordproject.cicero.contract;

import org.hyperledger.composer.system.*;
import net.corda.core.serialization.CordaSerializable;
import org.accordproject.base.Event;
import org.accordproject.base.Transaction;
import org.accordproject.base.Participant;
import org.accordproject.base.Asset;
@CordaSerializable
public class AccordContractState extends Asset {
   
   // the accessor for the identifying field
   public String getID() {
      return stateId;
   }

   public String stateId;

   public String getStateId() {
      return this.stateId;
   }

   public void setStateId(String stateId) {
      this.stateId = stateId;
   }
}

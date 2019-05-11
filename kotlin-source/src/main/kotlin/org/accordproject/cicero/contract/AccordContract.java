// this code is generated and should not be modified
package org.accordproject.cicero.contract;

import net.corda.core.serialization.CordaSerializable;
import org.accordproject.base.Asset;

@CordaSerializable
public abstract class AccordContract extends Asset {
   
   // the accessor for the identifying field
   public String getID() {
      return this.getContractId();
   }

   private String contractId;
   private AccordParty[] parties;
   public String getContractId() {
      return this.contractId;
   }
   public AccordParty[] getParties() {
      return this.parties;
   }
   public void setContractId(String contractId) {
      this.contractId = contractId;
   }
   public void setParties(AccordParty[] parties) {
      this.parties = parties;
   }
}

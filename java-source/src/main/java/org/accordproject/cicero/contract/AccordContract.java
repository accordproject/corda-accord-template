// this code is generated and should not be modified
package org.accordproject.cicero.contract;

import org.hyperledger.composer.system.*;
import net.corda.core.serialization.CordaSerializable;
import org.accordproject.base.Event;
import org.accordproject.base.Transaction;
import org.accordproject.base.Participant;
import org.accordproject.base.Asset;
@CordaSerializable
public abstract class AccordContract extends Asset {
   
   // the accessor for the identifying field
   public String getID() {
      return contractId;
   }

   public String contractId;
   public AccordParty[] parties;

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

// this code is generated and should not be modified
package org.accordproject.cicero.contract;

import org.hyperledger.composer.system.*;
import org.accordproject.base.Event;
import org.accordproject.base.Transaction;
import org.accordproject.base.Participant;
import org.accordproject.base.Asset;
public abstract class AccordContract extends Asset {
   
   // the accessor for the identifying field
   public String getID() {
      return contractId;
   }

   public String contractId;
   public org.accordproject.cicero.contract.AccordParty[] parties;
}

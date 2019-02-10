// this code is generated and should not be modified
package org.accordproject.cicero.contract;

import org.hyperledger.composer.system.*;
import net.corda.core.serialization.CordaSerializable;
import org.accordproject.base.Event;
import org.accordproject.base.Transaction;
import org.accordproject.base.Participant;
import org.accordproject.base.Asset;
@CordaSerializable
public abstract class AccordClause extends Asset {
   
   // the accessor for the identifying field
   public String getID() {
      return this.getClauseId();
   }

   private String clauseId;
   public String getClauseId() {
      return this.clauseId;
   }
   public void setClauseId(String clauseId) {
      this.clauseId = clauseId;
   }
}

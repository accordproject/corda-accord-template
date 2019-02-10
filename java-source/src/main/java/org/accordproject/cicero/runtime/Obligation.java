// this code is generated and should not be modified
package org.accordproject.cicero.runtime;

import org.hyperledger.composer.system.*;
import net.corda.core.serialization.CordaSerializable;
import org.accordproject.base.Event;
import org.accordproject.base.Transaction;
import org.accordproject.base.Participant;
import org.accordproject.base.Asset;
import org.accordproject.cicero.contract.AccordContract;
import org.accordproject.cicero.contract.AccordContractState;
import org.accordproject.money.MonetaryAmount;
@CordaSerializable
public abstract class Obligation extends Event {
   
   // the accessor for the identifying field
   public String getID() {
      return this.getEventId();
   }

   private AccordContract contract;
   private Participant promisor;
   private Participant promisee;
   private java.util.Date deadline;
   public AccordContract getContract() {
      return this.contract;
   }
   public Participant getPromisor() {
      return this.promisor;
   }
   public Participant getPromisee() {
      return this.promisee;
   }
   public java.util.Date getDeadline() {
      return this.deadline;
   }
   public void setContract(AccordContract contract) {
      this.contract = contract;
   }
   public void setPromisor(Participant promisor) {
      this.promisor = promisor;
   }
   public void setPromisee(Participant promisee) {
      this.promisee = promisee;
   }
   public void setDeadline(java.util.Date deadline) {
      this.deadline = deadline;
   }
}

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
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({"$class"})
@CordaSerializable
public class Success {
   public Response response;
   public AccordContractState state;
   public Event[] emit;

   public Response getResponse() {
      return this.response;
   }
   public AccordContractState getState() {
      return this.state;
   }
   public Event[] getEmit() {
      return this.emit;
   }

   public void setResponse(Response response) {
      this.response = response;
   }
   public void setState(AccordContractState state) {
      this.state = state;
   }
   public void setEmit(Event[] emit) {
      this.emit = emit;
   }
}

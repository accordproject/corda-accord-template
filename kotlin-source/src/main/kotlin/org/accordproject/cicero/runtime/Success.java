// this code is generated and should not be modified
package org.accordproject.cicero.runtime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import net.corda.core.serialization.CordaSerializable;
import org.accordproject.base.Event;
import org.accordproject.cicero.contract.AccordContractState;

@JsonIgnoreProperties({"$class"})
@CordaSerializable
public class Success {
   private Response response;
   private AccordContractState state;
   private Event[] emit;
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

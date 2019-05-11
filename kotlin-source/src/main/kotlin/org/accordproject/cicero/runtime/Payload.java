// this code is generated and should not be modified
package org.accordproject.cicero.runtime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import net.corda.core.serialization.CordaSerializable;
import org.accordproject.cicero.contract.AccordContract;
import org.accordproject.cicero.contract.AccordContractState;

@JsonIgnoreProperties({"$class"})
@CordaSerializable
public class Payload {
   private AccordContract contract;
   private Request request;
   private AccordContractState state;
   public AccordContract getContract() {
      return this.contract;
   }
   public Request getRequest() {
      return this.request;
   }
   public AccordContractState getState() {
      return this.state;
   }
   public void setContract(AccordContract contract) {
      this.contract = contract;
   }
   public void setRequest(Request request) {
      this.request = request;
   }
   public void setState(AccordContractState state) {
      this.state = state;
   }
}

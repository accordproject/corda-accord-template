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
public class Failure {
   public ErrorResponse error;

   public ErrorResponse getError() {
      return this.error;
   }

   public void setError(ErrorResponse error) {
      this.error = error;
   }
}

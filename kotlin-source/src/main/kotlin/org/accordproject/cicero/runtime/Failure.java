// this code is generated and should not be modified
package org.accordproject.cicero.runtime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import net.corda.core.serialization.CordaSerializable;

@JsonIgnoreProperties({"$class"})
@CordaSerializable
public class Failure {
   private ErrorResponse error;
   public ErrorResponse getError() {
      return this.error;
   }
   public void setError(ErrorResponse error) {
      this.error = error;
   }
}

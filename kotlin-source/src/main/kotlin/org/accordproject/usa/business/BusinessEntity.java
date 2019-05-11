// this code is generated and should not be modified
package org.accordproject.usa.business;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import net.corda.core.serialization.CordaSerializable;

@JsonIgnoreProperties({"$class"})
@CordaSerializable
public enum BusinessEntity {
   GENERAL_PARTNERSHIP,
   LP,
   LLP,
   LLLP,
   LLC,
   PLLC,
   CORP,
   PC,
   DBA,
}

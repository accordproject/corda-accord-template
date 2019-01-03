// this code is generated and should not be modified
package org.accordproject.usa.business;

import net.corda.core.serialization.CordaSerializable;
import org.hyperledger.composer.system.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@CordaSerializable
@JsonIgnoreProperties({"$class"})
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

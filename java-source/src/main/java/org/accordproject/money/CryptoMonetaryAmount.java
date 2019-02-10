// this code is generated and should not be modified
package org.accordproject.money;

import org.hyperledger.composer.system.*;
import net.corda.core.serialization.CordaSerializable;
import org.accordproject.base.Event;
import org.accordproject.base.Transaction;
import org.accordproject.base.Participant;
import org.accordproject.base.Asset;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({"$class"})
@CordaSerializable
public class CryptoMonetaryAmount {
   private double doubleValue;
   private CryptoCurrencyCode cryptoCurrencyCode;
   public double getDoubleValue() {
      return this.doubleValue;
   }
   public CryptoCurrencyCode getCryptoCurrencyCode() {
      return this.cryptoCurrencyCode;
   }
   public void setDoubleValue(double doubleValue) {
      this.doubleValue = doubleValue;
   }
   public void setCryptoCurrencyCode(CryptoCurrencyCode cryptoCurrencyCode) {
      this.cryptoCurrencyCode = cryptoCurrencyCode;
   }
}

// this code is generated and should not be modified
package org.accordproject.money;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import net.corda.core.contracts.Amount;
import net.corda.core.serialization.CordaSerializable;
import net.corda.finance.Currencies;

import java.util.Currency;

@JsonIgnoreProperties({"$class"})
@CordaSerializable
public class MonetaryAmount {
   private double doubleValue;
   private CurrencyCode currencyCode;
   public double getDoubleValue() {
      return this.doubleValue;
   }
   public CurrencyCode getCurrencyCode() {
      return this.currencyCode;
   }
   public void setDoubleValue(double doubleValue) {
      this.doubleValue = doubleValue;
   }
   public void setCurrencyCode(CurrencyCode currencyCode) {
      this.currencyCode = currencyCode;
   }
   
   public Amount<Currency> getCurrency() {
      return Currencies.DOLLARS(doubleValue);
   }

}

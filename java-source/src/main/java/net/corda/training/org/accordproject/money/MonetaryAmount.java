// this code is generated and should not be modified
package org.accordproject.money;

//import org.hyperledger.composer.system.*;
import org.accordproject.base.Event;
import org.accordproject.base.Transaction;
import org.accordproject.base.Participant;
import org.accordproject.base.Asset;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({"$class"})
public class MonetaryAmount {
   public double doubleValue;
   public org.accordproject.money.CurrencyCode currencyCode;
}

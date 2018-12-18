// this code is generated and should not be modified
package org.accordproject.promissorynote;

import org.hyperledger.composer.system.*;
import org.accordproject.base.Event;
import org.accordproject.base.Transaction;
import org.accordproject.base.Participant;
import org.accordproject.base.Asset;
import org.accordproject.cicero.contract.*;
import org.accordproject.cicero.runtime.*;
import org.accordproject.money.MonetaryAmount;
import org.accordproject.usa.business.BusinessEntity;
public class Result extends Response {
   
   // the accessor for the identifying field
   public String getID() {
      return transactionId;
   }

   public double outstandingBalance;
}

// this code is generated and should not be modified
//package org.accordproject.promissorynote;
package net.corda.training.org.accordproject.promissorynote;

//import org.hyperledger.composer.system.*;
import net.corda.core.serialization.CordaSerializable;
import org.accordproject.base.Event;
import org.accordproject.base.Transaction;
import org.accordproject.base.Participant;
import org.accordproject.base.Asset;
//import org.accordproject.cicero.contract.*;
//import org.accordproject.cicero.runtime.*;
import org.accordproject.money.MonetaryAmount;
import org.accordproject.usa.business.BusinessEntity;

@CordaSerializable
public class PromissoryNoteContract extends org.accordproject.cicero.contract.AccordContract {
   
   // the accessor for the identifying field
   public String getID() {
      return contractId;
   }

   public MonetaryAmount amount;
   public java.util.Date date;
   public String maker;
   public double interestRate;
   public boolean individual;
   public String makerAddress;
   public String lender;
   public BusinessEntity legalEntity;
   public String lenderAddress;
   public MonetaryAmount principal;
   public java.util.Date maturityDate;
   public int defaultDays;
   public int insolvencyDays;
   public String jurisdiction;
}

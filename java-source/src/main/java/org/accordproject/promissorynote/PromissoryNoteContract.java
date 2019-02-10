// this code is generated and should not be modified
package org.accordproject.promissorynote;

import org.hyperledger.composer.system.*;
import net.corda.core.serialization.CordaSerializable;
import org.accordproject.base.Event;
import org.accordproject.base.Transaction;
import org.accordproject.base.Participant;
import org.accordproject.base.Asset;
import org.accordproject.cicero.contract.*;
import org.accordproject.cicero.runtime.*;
import org.accordproject.money.MonetaryAmount;
import org.accordproject.usa.business.BusinessEntity;
@CordaSerializable
public class PromissoryNoteContract extends AccordContract {
   
   // the accessor for the identifying field
   public String getID() {
      return this.getContractId();
   }

   private MonetaryAmount amount;
   private java.util.Date date;
   private String maker;
   private double interestRate;
   private boolean individual;
   private String makerAddress;
   private String lender;
   private BusinessEntity legalEntity;
   private String lenderAddress;
   private MonetaryAmount principal;
   private java.util.Date maturityDate;
   private int defaultDays;
   private int insolvencyDays;
   private String jurisdiction;
   public MonetaryAmount getAmount() {
      return this.amount;
   }
   public java.util.Date getDate() {
      return this.date;
   }
   public String getMaker() {
      return this.maker;
   }
   public double getInterestRate() {
      return this.interestRate;
   }
   public boolean getIndividual() {
      return this.individual;
   }
   public String getMakerAddress() {
      return this.makerAddress;
   }
   public String getLender() {
      return this.lender;
   }
   public BusinessEntity getLegalEntity() {
      return this.legalEntity;
   }
   public String getLenderAddress() {
      return this.lenderAddress;
   }
   public MonetaryAmount getPrincipal() {
      return this.principal;
   }
   public java.util.Date getMaturityDate() {
      return this.maturityDate;
   }
   public int getDefaultDays() {
      return this.defaultDays;
   }
   public int getInsolvencyDays() {
      return this.insolvencyDays;
   }
   public String getJurisdiction() {
      return this.jurisdiction;
   }
   public void setAmount(MonetaryAmount amount) {
      this.amount = amount;
   }
   public void setDate(java.util.Date date) {
      this.date = date;
   }
   public void setMaker(String maker) {
      this.maker = maker;
   }
   public void setInterestRate(double interestRate) {
      this.interestRate = interestRate;
   }
   public void setIndividual(boolean individual) {
      this.individual = individual;
   }
   public void setMakerAddress(String makerAddress) {
      this.makerAddress = makerAddress;
   }
   public void setLender(String lender) {
      this.lender = lender;
   }
   public void setLegalEntity(BusinessEntity legalEntity) {
      this.legalEntity = legalEntity;
   }
   public void setLenderAddress(String lenderAddress) {
      this.lenderAddress = lenderAddress;
   }
   public void setPrincipal(MonetaryAmount principal) {
      this.principal = principal;
   }
   public void setMaturityDate(java.util.Date maturityDate) {
      this.maturityDate = maturityDate;
   }
   public void setDefaultDays(int defaultDays) {
      this.defaultDays = defaultDays;
   }
   public void setInsolvencyDays(int insolvencyDays) {
      this.insolvencyDays = insolvencyDays;
   }
   public void setJurisdiction(String jurisdiction) {
      this.jurisdiction = jurisdiction;
   }
}

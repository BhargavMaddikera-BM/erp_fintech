package com.blackstrawai.journals;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.DateConverter;
import com.blackstrawai.accounting.AccountingAspectsDao;
import com.blackstrawai.accounting.AccountingAspectsItemsVo;
import com.blackstrawai.accounting.AccountingAspectsVo;
import com.blackstrawai.ap.BillsInvoiceDao;
import com.blackstrawai.ap.PaymentNonCoreConstants;
import com.blackstrawai.ap.PaymentNonCoreDao;
import com.blackstrawai.ap.billsinvoice.InvoiceVo;
import com.blackstrawai.ap.dropdowns.BasicVoucherEntriesVo;
import com.blackstrawai.ap.payment.noncore.PaymentNonCoreVo;
import com.blackstrawai.ar.ApplyCreditsDao;
import com.blackstrawai.ar.ArInvoiceDao;
import com.blackstrawai.ar.CreditNotesDao;
import com.blackstrawai.ar.ReceiptConstants;
import com.blackstrawai.ar.ReceiptDao;
import com.blackstrawai.ar.RefundDao;
import com.blackstrawai.ar.applycredits.ApplyCreditDetailsVo;
import com.blackstrawai.ar.applycredits.ApplyCreditsInvoiceVo;
import com.blackstrawai.ar.applycredits.ApplyCreditsVo;
import com.blackstrawai.ar.applycredits.InvoiceDetailsVo;
import com.blackstrawai.ar.creditnotes.CreditNotesVo;
import com.blackstrawai.ar.invoice.ArInvoiceProductVo;
import com.blackstrawai.ar.invoice.ArInvoiceVo;
import com.blackstrawai.ar.receipt.ReceiptSettingsVo;
import com.blackstrawai.ar.receipt.ReceiptVo;
import com.blackstrawai.ar.refund.RefundVo;
import com.blackstrawai.banking.BankMasterDao;
import com.blackstrawai.banking.ContraDao;
import com.blackstrawai.banking.contra.ContraEntriesVo;
import com.blackstrawai.banking.contra.ContraVo;
import com.blackstrawai.banking.dashboard.BankMasterAccountVo;
import com.blackstrawai.banking.dashboard.BankMasterCardVo;
import com.blackstrawai.banking.dashboard.BankMasterCashAccountVo;
import com.blackstrawai.banking.dashboard.BankMasterWalletVo;
import com.blackstrawai.chartofaccounts.ChartOfAccountsDao;
import com.blackstrawai.common.BaseDao;
import com.blackstrawai.common.CommonConstants;
import com.blackstrawai.common.FinanceCommonDao;
import com.blackstrawai.keycontact.CustomerDao;
import com.blackstrawai.keycontact.VendorDao;
import com.blackstrawai.onboarding.OrganizationDao;
import com.blackstrawai.onboarding.RegistrationDao;
import com.blackstrawai.onboarding.UserDao;
import com.blackstrawai.payroll.PayRunDao;
import com.blackstrawai.payroll.payrun.PayRunVo;
import com.blackstrawai.report.JournalEntriesTransactionReportVo;
import com.blackstrawai.settings.TaxGroupDao;
import com.blackstrawai.settings.TaxRateMappingDao;
import com.blackstrawai.settings.TaxRateMappingVo;
import com.mysql.jdbc.Statement;

@Repository
public class JournalEntriesTransactionDao extends BaseDao{

	private Logger logger = Logger.getLogger(JournalEntriesTransactionDao.class);
	
	@Autowired
	private BillsInvoiceDao billsInvoiceDao;
	
	//@Autowired
	//private CurrencyDao currencyDao;
	
	@Autowired
	private AccountingAspectsDao accountingAspectsDao;
	
	@Autowired
	private FinanceCommonDao  financeCommonDao;
	
	@Autowired
	private OrganizationDao organizationDao;
	
	@Autowired
	private ChartOfAccountsDao chartOfAccountsDao;
	
	@Autowired 
	private BankMasterDao bankMasterDao;

	@Autowired
	private ArInvoiceDao arInvoiceDao;
	
	@Autowired
	private ContraDao contraDao;
	
	@Autowired
	private ReceiptDao receiptDao;
	
	@Autowired
	private RefundDao refundDao;
	
	@Autowired
	private CreditNotesDao creditNotesDao;
	
	
	@Autowired
	private ApplyCreditsDao applyCreditsDao;
	
	@Autowired
	private GeneralLedgersDao generalLedgersDao;
	
	@Autowired
	private TaxRateMappingDao taxRateMappingDao;
	
	@Autowired
	private PaymentNonCoreDao paymentNonCoreDao;
	
	@Autowired
	private CustomerDao customerDao;
	
	@Autowired
	private VendorDao vendorDao;
	
	@Autowired
	private TaxGroupDao taxGroupDao;
	
	@Autowired
	private PayRunDao payRunDao;
	
	@Autowired
	private RegistrationDao registrationDao;
	
	@Autowired
	private UserDao userDao;
	
	public List<JournalEntriesVo>  getInvoiceAndGroupByLedgers(Integer invoiceId) throws ApplicationException {
		logger.info("To method getInvoiceAndGroupByLedgers with Id" + invoiceId);
		 List<JournalEntriesVo> journalEntries = new ArrayList<JournalEntriesVo>();
		 GeneralLedgerVo glData = null;
		try {
			InvoiceVo  invoiceVo = billsInvoiceDao.getInvoiceById(invoiceId);
			if(invoiceVo!=null  && invoiceVo.getGeneralLedgerData()!=null  ) {
				glData = invoiceVo.getGeneralLedgerData();
			}else {
				glData = generalLedgersDao.getGeneralLedgers(JournalEntriesConstants.SUB_MODULE_INVOICE_WITH_BILLS, invoiceVo);
			}
			logger.info("glvo from GL DAO"+glData);
			Map<String, String> taxGroup = taxGroupDao.getTaxRatesAndTypeMappingForOrganization(invoiceVo.getOrganizationId());

			if(glData!=null && glData.getGlDetails()!=null && glData.getGlDetails().size()>0) {
				for(GeneralLedgerDetailsVo glVo : glData.getGlDetails()) {
					JournalEntriesVo vo = new JournalEntriesVo();
					vo.setTransactionNo(invoiceVo.getId());
					vo.setModule(JournalEntriesConstants.MODULE_AP);
					vo.setSubModule(invoiceVo.getIsInvoiceWithBills()? JournalEntriesConstants.SUB_MODULE_INVOICE_WITH_BILLS : JournalEntriesConstants.SUB_MODULE_INVOICE_WITHOUT_BILLS);
					vo.setVoucherType(invoiceVo.getIsInvoiceWithBills()? JournalEntriesConstants.SUB_MODULE_INVOICE_WITH_BILLS : JournalEntriesConstants.SUB_MODULE_INVOICE_WITHOUT_BILLS);
					vo.setVoucherNo(invoiceVo.getGeneralInfo().getInvoiceNo());
					vo.setDateOfEntry(invoiceVo.getGeneralInfo().getCreate_ts());
					vo.setEffectiveDate(invoiceVo.getGeneralInfo().getInvoiceDate()!=null ? Date.valueOf(invoiceVo.getGeneralInfo().getInvoiceDate()) : null);
					vo.setInvoiceDate(invoiceVo.getGeneralInfo().getInvoiceDate()!=null ? Date.valueOf(invoiceVo.getGeneralInfo().getInvoiceDate()) : null);
					if(JournalEntriesConstants.SUBLEDGER_TYPE_GST.equals(glVo.getType())) {
						if(taxGroup.containsKey(glVo.getSubLedger())) {
							String[] ledger = taxGroup.get(glVo.getSubLedger()).split("~");
							vo.setGstLevel(ledger[0]);
							vo.setGstPercentage(ledger[1]);
						}
					}
					vo.setParticulars(glVo.getAccount());
					vo.setLedgerId(String.valueOf(glVo.getAccountId()));
					vo.setAmountDebitOriginalCurrrency(glVo.getFcyDebit()!=null ? glVo.getFcyDebit() : 0.00);
					vo.setAmountCreditOriginalCurrency(glVo.getFcyCredit()!=null ? glVo.getFcyCredit() : 0.00);
					vo.setAmountCredit(glVo.getInrCredit()!=null ? glVo.getInrCredit() : 0.00);
					vo.setAmountDebit(glVo.getInrDebit()!=null ? glVo.getInrDebit() : 0.00);
					vo.setVendorId(invoiceVo.getGeneralInfo().getVendorId());
					vo.setTdsId(invoiceVo.getTransactionDetails().getTdsId());
					vo.setVendorInvoiceNo(invoiceVo.getGeneralInfo().getInvoiceNo());
					vo.setVendorPoNo(invoiceVo.getGeneralInfo().getPoReferenceNo());
					vo.setLocationId(null);
					vo.setGst(vendorDao.getVendorGSTNo(invoiceVo.getGeneralInfo().getVendorId()));
					vo.setCurrency(invoiceVo.getTransactionDetails().getCurrencyid());
					vo.setConversionFactor(invoiceVo.getTransactionDetails().getExchangeRate()!=null ? invoiceVo.getTransactionDetails().getExchangeRate() : 1.00);
					vo.setRemarks(invoiceVo.getGeneralInfo().getNotes()!=null && invoiceVo.getGeneralInfo().getNotes().length()>50? invoiceVo.getGeneralInfo().getNotes().substring(0,50) : invoiceVo.getGeneralInfo().getNotes());
					vo.setUserId(invoiceVo.getUserId());
					vo.setOrgId(invoiceVo.getOrganizationId());
					vo.setIsSuperAdmin(invoiceVo.getIsSuperAdmin());
					vo.setVoucherNaration(glData.getVoucherNaration());
					journalEntries.add(vo);
				}
			}
			logger.info("Journal entries size :: "+ journalEntries.size());

			
			
			
			
			
			/*
			
			
			
			Map<String , String > debitLedgerMap = new HashMap<String , String >();
			Map<String , String > creditLedgerMap = new HashMap<String , String >();
			
			if(invoiceVo!=null && invoiceVo.getTransactionDetails()!=null && invoiceVo.getTransactionDetails().getProducts()!=null && invoiceVo.getTransactionDetails().getProducts().size()>0) {
				for(InvoiceProductVo product : invoiceVo.getTransactionDetails().getProducts()) {
					if(product.getStatus()!=null && !"DEL".equals(product.getStatus()) && product.getProductAccountName()!=null && product.getProductAccountLevel()!=null ) {
						
						// To separate Sundry creditors ledgers
						try(Connection con = getUserMgmConnection()){
							BasicCurrencyVo organizationCurrency = currencyDao.getBasicCurrencyForOrganization(invoiceVo.getOrganizationId(), con);
							CurrencyVo invoiceCurrency = currencyDao.getCurrency(invoiceVo.getTransactionDetails().getCurrencyid());
							if(organizationCurrency!=null && invoiceCurrency!=null) {
								if(organizationCurrency.getName().equals(invoiceCurrency.getName())) {
									Integer ledgerid = chartOfAccountsDao.getLedgerIdGivenName(JournalEntriesConstants.SUNDRY_CREDITORS_LOCAL_CURRENCY, invoiceVo.getOrganizationId());
									creditLedgerMap.put(JournalEntriesConstants.SUNDRY_CREDITORS_LOCAL_CURRENCY , invoiceVo.getTransactionDetails().getTotal()+"~"+String.valueOf(ledgerid));
									logger.info("Value ::"+invoiceVo.getTransactionDetails().getTotal()+"~"+String.valueOf(ledgerid));
								}
								else {
									Integer ledgerid = chartOfAccountsDao.getLedgerIdGivenName(JournalEntriesConstants.SUNDRY_CREDITORS_FOREIGN_CURRENCY, invoiceVo.getOrganizationId());
										creditLedgerMap.put(JournalEntriesConstants.SUNDRY_CREDITORS_FOREIGN_CURRENCY, invoiceVo.getTransactionDetails().getTotal()+"~"+String.valueOf(ledgerid));
										logger.info("Value ::"+invoiceVo.getTransactionDetails().getTotal()+"~"+String.valueOf(ledgerid));
	
								}
							}
						} catch (SQLException e) {
							logger.info("Error in getInvoiceAndGroupByLedgers:: ",e);
							throw new ApplicationException(e);
						}
						
						
						//To separate the ledger of each products
						if( debitLedgerMap.containsKey(product.getProductAccountName())) {
							String[] values = debitLedgerMap.get(product.getProductAccountName()).split("~");
							if(values.length>0) {
								String amount = values[0];
								Double amnt = Double.valueOf(amount);
								amnt = amnt + product.getAmount();
								debitLedgerMap.put(product.getProductAccountName(), amnt+ "~" + values[1]);
							}
						
						}else {
							debitLedgerMap.put(product.getProductAccountName(), product.getAmount()+"~"+product.getProductAccountId());
						}
					
						
						
						
						//To separate GST ledger
					if(product.getInputTaxCredit() !=null && product.getTaxDetails()!=null && product.getTaxDetails().getTaxDistribution()!=null) {
						for(InvoiceTaxDistributionVo taxdetails : product.getTaxDetails().getTaxDistribution()) {
							if(taxdetails.getTaxName()!=null && taxdetails.getTaxRate()!=null && taxdetails.getTaxAmount()!=null) {
								Integer ledgerId = chartOfAccountsDao.getLedgerIdGivenName(JournalEntriesConstants.INPUT_GST, invoiceVo.getOrganizationId());
								if("ELG".equals(product.getInputTaxCredit())) {
									if(debitLedgerMap.containsKey(JournalEntriesConstants.INPUT_GST+"~"+taxdetails.getTaxName()+"~"+taxdetails.getTaxRate())) {
										String[] value = debitLedgerMap.get(JournalEntriesConstants.INPUT_GST+"~"+taxdetails.getTaxName()+"~"+taxdetails.getTaxRate()).split("~");
									if(value.length>0) {
										String amount = value[0];
										Double amnt = Double.valueOf(amount);
										amnt = amnt + taxdetails.getTaxAmount();
										debitLedgerMap.put(JournalEntriesConstants.INPUT_GST+"~"+taxdetails.getTaxName()+"~"+taxdetails.getTaxRate(), amnt+"~"+value[1]);
									}
										
									}else {
										debitLedgerMap.put(JournalEntriesConstants.INPUT_GST+"~"+taxdetails.getTaxName()+"~"+taxdetails.getTaxRate(), taxdetails.getTaxAmount()+"~"+ledgerId);
									}
								}else {
									Integer expenseLedgerId = chartOfAccountsDao.getLedgerIdGivenName(JournalEntriesConstants.TAX_PAID_EXPENSE, invoiceVo.getOrganizationId());
									if(debitLedgerMap.containsKey(JournalEntriesConstants.TAX_PAID_EXPENSE+"~"+taxdetails.getTaxName()+"~"+taxdetails.getTaxRate())) {
										String[] value = debitLedgerMap.get(JournalEntriesConstants.TAX_PAID_EXPENSE+"~"+taxdetails.getTaxName()+"~"+taxdetails.getTaxRate()).split("~");
										if(value.length>0) {
										String amount = value[0];
										Double amnt =  Double.valueOf(amount);
										amnt = amnt + taxdetails.getTaxAmount();
										debitLedgerMap.put(JournalEntriesConstants.TAX_PAID_EXPENSE+"~"+taxdetails.getTaxName()+"~"+taxdetails.getTaxRate(), amnt+"~"+expenseLedgerId);
										}
										}else {
										debitLedgerMap.put(JournalEntriesConstants.TAX_PAID_EXPENSE+"~"+taxdetails.getTaxName()+"~"+taxdetails.getTaxRate(), taxdetails.getTaxAmount()+"~"+expenseLedgerId);
									}
								}
							}
						}
					}
				}
				
					//To separate the ledger of TDS
				if(invoiceVo.getTransactionDetails().getTdsId()!=null && invoiceVo.getTransactionDetails().getTdsId()!=0 && invoiceVo.getTransactionDetails().getTdsValue()!=null && !invoiceVo.getTransactionDetails().getTdsValue().equals(0.0)) {
					Integer tdsLedgerId = chartOfAccountsDao.getLedgerIdGivenName(JournalEntriesConstants.TDS_PAYABLE, invoiceVo.getOrganizationId());
					creditLedgerMap.put(JournalEntriesConstants.TDS_PAYABLE,invoiceVo.getTransactionDetails().getTdsValue()+"~"+tdsLedgerId );
				}
				//To separate the ledger of Discount 
				if(invoiceVo.getTransactionDetails().getDiscountAccountId()!=null && invoiceVo.getTransactionDetails().getDiscountAccountId()!=0 && invoiceVo.getTransactionDetails().getDiscountAccountName()!=null 
						 && invoiceVo.getTransactionDetails().getDiscountAmount()!=null && !invoiceVo.getTransactionDetails().getDiscountAmount().equals(0.0)) {
						creditLedgerMap.put(invoiceVo.getTransactionDetails().getDiscountAccountName() ,invoiceVo.getTransactionDetails().getDiscountAmount()+"~"+invoiceVo.getTransactionDetails().getDiscountAccountId() );
				}
				
				
				//To separate the ledger of Adjustment 
				if(invoiceVo.getTransactionDetails().getAdjustmentAccountId()!=null && invoiceVo.getTransactionDetails().getAdjustmentAccountId()!=0 && invoiceVo.getTransactionDetails().getAdjustmentAccountName()!=null && invoiceVo.getTransactionDetails().getAdjustment()!=null && !invoiceVo.getTransactionDetails().getAdjustment().equals(0.0)) {
					if(invoiceVo.getTransactionDetails().getAdjustment() > 0) {
							debitLedgerMap.put(invoiceVo.getTransactionDetails().getAdjustmentAccountName() , Math.abs(invoiceVo.getTransactionDetails().getAdjustment() )+"~"+invoiceVo.getTransactionDetails().getAdjustmentAccountId());
					}else {
							creditLedgerMap.put(invoiceVo.getTransactionDetails().getAdjustmentAccountName() ,  Math.abs(invoiceVo.getTransactionDetails().getAdjustment() ) +"~"+invoiceVo.getTransactionDetails().getAdjustmentAccountId());
					}
					
				}
				
			}
			logger.info("Ledgers Seaparated to creditLedgerMap::" + creditLedgerMap);
			logger.info("Ledgers Seaparated to debitLedgerMap::" + debitLedgerMap);
		}
			
			// To form the Journal entries
			if(creditLedgerMap.size()>0 && debitLedgerMap.size()>0 ) {

				debitLedgerMap.forEach((key , value) ->{
					JournalEntriesVo vo = new JournalEntriesVo();
					vo.setTransactionNo(invoiceVo.getId());
					vo.setModule(JournalEntriesConstants.MODULE_AP);
					vo.setSubModule(invoiceVo.getIsInvoiceWithBills()? JournalEntriesConstants.SUB_MODULE_INVOICE_WITH_BILLS : JournalEntriesConstants.SUB_MODULE_INVOICE_WITHOUT_BILLS);
					vo.setVoucherType(invoiceVo.getIsInvoiceWithBills()? JournalEntriesConstants.SUB_MODULE_INVOICE_WITH_BILLS : JournalEntriesConstants.SUB_MODULE_INVOICE_WITHOUT_BILLS);
					vo.setDateOfEntry(invoiceVo.getGeneralInfo().getCreate_ts());
					vo.setEffectiveDate(invoiceVo.getGeneralInfo().getInvoiceDate()!=null ? Date.valueOf(invoiceVo.getGeneralInfo().getInvoiceDate()) : null);
					vo.setInvoiceDate(invoiceVo.getGeneralInfo().getInvoiceDate()!=null ? Date.valueOf(invoiceVo.getGeneralInfo().getInvoiceDate()) : null);
					if(key.contains(JournalEntriesConstants.INPUT_GST) || key.contains(JournalEntriesConstants.TAX_PAID_EXPENSE)) {
						logger.info("GST ledger");
						String[] gstLedger = key.split("~");
						vo.setParticulars(gstLedger[0]);
						vo.setGstLevel(gstLedger[1]);
						vo.setGstPercentage(gstLedger[2]);
						logger.info("Credit LedgerId ::"+ value.split("~")[1]);
						vo.setLedgerId(value.split("~")[1]);
						String amnt = value.split("~")[0]!=null ? value.split("~")[0] : "0.00";
						vo.setAmountDebitOriginalCurrrency(Double.valueOf(amnt));
						vo.setGstInputCerdit(key.contains(JournalEntriesConstants.INPUT_GST) ? "YES" : "NO");
					}else {
						vo.setParticulars(key);
						String amnt = value.split("~")[0]!=null ? value.split("~")[0] : "0.00";
						vo.setAmountDebitOriginalCurrrency(Double.valueOf(amnt));
						vo.setLedgerId(value.split("~")[1]);
						logger.info("Credit LedgerId ::"+ value.split("~")[1]);
						vo.setGstLevel(null);
						vo.setGstPercentage(null);
					}
					vo.setVendorId(invoiceVo.getGeneralInfo().getVendorId());
					vo.setVendorInvoiceNo(invoiceVo.getGeneralInfo().getInvoiceNo());
					vo.setVendorPoNo(invoiceVo.getGeneralInfo().getPoReferenceNo());
					vo.setTdsId(invoiceVo.getTransactionDetails().getTdsId());
					vo.setLocationId(invoiceVo.getGeneralInfo().getLocationId() );
					vo.setGst(invoiceVo.getGeneralInfo().getGstNumberId());
					vo.setCurrency(invoiceVo.getTransactionDetails().getCurrencyid());
					vo.setConversionFactor(invoiceVo.getTransactionDetails().getExchangeRate());
					vo.setAmountDebit(vo.getAmountDebitOriginalCurrrency() * vo.getConversionFactor());
					vo.setRemarks(invoiceVo.getGeneralInfo().getNotes()!=null && invoiceVo.getGeneralInfo().getNotes().length()>50? invoiceVo.getGeneralInfo().getNotes().substring(0,50) : invoiceVo.getGeneralInfo().getNotes());
					vo.setUserId(invoiceVo.getUserId());
					vo.setOrgId(invoiceVo.getOrganizationId());
					vo.setIsSuperAdmin(invoiceVo.getIsSuperAdmin());
					vo.setAmountCreditOriginalCurrency(0.00);
					vo.setAmountCredit(0.00);
					journalEntries.add(vo);
				});
				
				
				creditLedgerMap.forEach((key , value) ->{
					JournalEntriesVo vo = new JournalEntriesVo();
					vo.setTransactionNo(invoiceVo.getId());
					vo.setModule(JournalEntriesConstants.MODULE_AP);
					vo.setSubModule(invoiceVo.getIsInvoiceWithBills() ? JournalEntriesConstants.SUB_MODULE_INVOICE_WITH_BILLS : JournalEntriesConstants.SUB_MODULE_INVOICE_WITHOUT_BILLS);
					vo.setVoucherType(invoiceVo.getIsInvoiceWithBills() ? JournalEntriesConstants.SUB_MODULE_INVOICE_WITH_BILLS : JournalEntriesConstants.SUB_MODULE_INVOICE_WITHOUT_BILLS);
					vo.setDateOfEntry(invoiceVo.getGeneralInfo().getCreate_ts());
					vo.setEffectiveDate(invoiceVo.getGeneralInfo().getInvoiceDate()!=null ? Date.valueOf(invoiceVo.getGeneralInfo().getInvoiceDate()) : null);
					vo.setInvoiceDate(invoiceVo.getGeneralInfo().getInvoiceDate()!=null ? Date.valueOf(invoiceVo.getGeneralInfo().getInvoiceDate()) : null);
					vo.setParticulars(key);
					String amnt = value.split("~")[0]!=null ? value.split("~")[0] : "0.00";
					vo.setAmountCreditOriginalCurrency(Double.valueOf(amnt));
					vo.setLedgerId(value.split("~")[1]);
					vo.setVendorId(invoiceVo.getGeneralInfo().getVendorId());
					vo.setVendorInvoiceNo(invoiceVo.getGeneralInfo().getInvoiceNo());
					vo.setVendorPoNo(invoiceVo.getGeneralInfo().getPoReferenceNo());
					vo.setTdsId(invoiceVo.getTransactionDetails().getTdsId());
					vo.setLocationId(invoiceVo.getGeneralInfo().getLocationId() );
					vo.setGst(invoiceVo.getGeneralInfo().getGstNumberId());
					vo.setGstLevel(null);
					vo.setGstPercentage(null);
					vo.setCurrency(invoiceVo.getTransactionDetails().getCurrencyid());
					vo.setConversionFactor(invoiceVo.getTransactionDetails().getExchangeRate());
					vo.setAmountCredit(vo.getAmountCreditOriginalCurrency() * vo.getConversionFactor());
					vo.setRemarks(invoiceVo.getGeneralInfo().getNotes()!=null && invoiceVo.getGeneralInfo().getNotes().length()>50? invoiceVo.getGeneralInfo().getNotes().substring(0,50) : invoiceVo.getGeneralInfo().getNotes());
					vo.setUserId(invoiceVo.getUserId());
					vo.setOrgId(invoiceVo.getOrganizationId());
					vo.setIsSuperAdmin(invoiceVo.getIsSuperAdmin());
					vo.setAmountDebitOriginalCurrrency(0.00);
					vo.setAmountDebit(0.00);
					journalEntries.add(vo);
				});
				
				*/
		}catch (ApplicationException e) {
			logger.info("Error in getInvoiceAndGroupByLedgers:: ",e);
			throw new ApplicationException(e);
		}
		return journalEntries;
	}
	
	
	
	public List<JournalEntriesVo>  getCreditNotesAndGroupByLedgers(Integer creditNoteid) throws ApplicationException {
		logger.info("To method getCreditNotesAndGroupByLedgers with Id" + creditNoteid);
		 List<JournalEntriesVo> journalEntries = new ArrayList<JournalEntriesVo>();
		try {
			CreditNotesVo  creditNoteVo = creditNotesDao.getCreditNotesById(creditNoteid);
			GeneralLedgerVo glVo = null;
			if(creditNoteVo.getGeneralLedgerData()!=null) {
				 glVo =  creditNoteVo.getGeneralLedgerData();
			}else {
				logger.info("glvo from GL DAO"+glVo);
				glVo = generalLedgersDao.getGeneralLedgers(JournalEntriesConstants.SUB_MODULE_CREDIT_NOTES, creditNoteVo);
			}
			logger.info("glvo"+glVo);
			Map<Integer , String > discountMap = new HashMap<Integer, String>();
			Map<Integer , String > adjustmentMap = new HashMap<Integer, String>();
			Map<Integer , String > tdsMap = new HashMap<Integer, String>();
			Map<Integer , String > customerMap = new HashMap<Integer, String>();
			Map<String , String > gstMap = new HashMap<String, String>();
			Map<String , String > filtedGstMap = new HashMap<String, String>();
			logger.info("glvo"+glVo);
			if(glVo!=null && glVo.getGlDetails()!=null && glVo.getGlDetails().size()>0 ) {	
			for(GeneralLedgerDetailsVo details :  glVo.getGlDetails()){
				logger.info("glvo Details"+details);
				if(details.getType()!=null && details.getType().equals(JournalEntriesConstants.TYPE_DISCOUNT)) {
					discountMap.put(details.getAccountId(), details.getAccount());
				}
				if(details.getType()!=null && details.getType().equals(JournalEntriesConstants.TYPE_ADJUSTMENT)) {
					adjustmentMap.put(details.getAccountId(), details.getAccount());
				}
				if(details.getType()!=null && details.getType().equals(JournalEntriesConstants.TYPE_TDS)) {
					tdsMap.put(details.getAccountId(), details.getAccount());
				}
				if(details.getType()!=null && details.getType().equals(JournalEntriesConstants.SUBLEDGER_TYPE_CUSTOMER)) {
					customerMap.put(details.getAccountId(), details.getAccount());
				}
				if(details.getType()!=null && details.getType().equals(JournalEntriesConstants.SUBLEDGER_TYPE_GST)) {
					gstMap.put(details.getSubLedger() ,details.getAccountId()+"~"+ details.getAccount());
				}
				
			}
			}
			
			Map<String , String > debitLedgerMap = new HashMap<String , String >();
			Map<String , String > creditLedgerMap = new HashMap<String , String >();
			logger.info("creditNoteVo ::"+creditNoteVo);
			if(creditNoteVo!=null && creditNoteVo.getProducts()!=null && creditNoteVo.getProducts().size()>0) {
				ArInvoiceVo invoiceVo = arInvoiceDao.getInvoiceById(Integer.valueOf( creditNoteVo.getOriginalInvoiceId()));
				
				
				// To get the customer Id and its selected customer account 
				
				
				if(creditNoteVo.getCustomerId()!=0 && creditNoteVo.getOriginalInvoiceId()!=null &&  creditNoteVo.getTotal()!=null) {
					if(customerMap.size()>0) {
						customerMap.forEach((key,value)->{
							creditLedgerMap.put(value, creditNoteVo.getTotal() +"~"+key);
						});
					}else {
						String ledgerName = chartOfAccountsDao.getLedgerName(invoiceVo.getGeneralInformation().getCustomerAccountId(), creditNoteVo.getOrganizationId());
						creditLedgerMap.put(ledgerName ,Double.valueOf( creditNoteVo.getTotal())+"~"+invoiceVo.getGeneralInformation().getCustomerAccountId());
					}
				}

				
				for(ArInvoiceProductVo product : creditNoteVo.getProducts()) {
					if(product.getProductAccountName()!=null && product.getStatus()!=null && !"DEL".equals(product.getStatus()) ) {
						//To separate the ledger of each products
						if( debitLedgerMap.containsKey(product.getProductAccountName())) {
							String[] values = debitLedgerMap.get(product.getProductAccountName()).split("~");
							String amnt = values[0]!=null ? values[0] : "0.00";
							Double amount = Double.valueOf(amnt); 
							amount = amount + product.getAmount();
							debitLedgerMap.put(product.getProductAccountName(), amount+"~"+values[1]);
						}else {
							debitLedgerMap.put(product.getProductAccountName(), product.getAmount()+"~"+product.getProductAccountId());
						}
					
						logger.info("taxdetails::"+product);
						
						
						
						//To separate GST ledger
						if(glVo!=null && glVo.getGlDetails()!=null && glVo.getGlDetails().size()>0) {
							for(GeneralLedgerDetailsVo glDetail : glVo.getGlDetails()) {
								if(JournalEntriesConstants.SUBLEDGER_TYPE_GST.equals(glDetail.getType()) ){
									Double amnt = glDetail.getFcyCredit()!=null ? glDetail.getFcyCredit() : glDetail.getFcyDebit();
									if(glDetail.getSubLedger().split(" ").length > 1 ) {
										String rate = glDetail.getSubLedger().split(" ")[1]!=null ?  glDetail.getSubLedger().split(" ")[1] : "0 ";
										logger.info("glDetail.getSubLedger().split(\" \")[1] --> " + glDetail.getSubLedger().split(" ")[1] + "rate --> "+rate);
										filtedGstMap.put(glDetail.getAccount()+"~"+glDetail.getSubLedger()+"~"+glDetail.getSubLedger().split(" ")[1],amnt+"~"+glDetail.getAccountId() );
									}else{
										TaxRateMappingVo taxVo = taxRateMappingDao.getTaxRateMappingByName(invoiceVo.getOrgId(), glDetail.getSubLedger());
										logger.info("glDetail.getSubLedger().split(\" \")[1] --> " +taxVo.getRate() + "rate --> "+taxVo.getRate());
										filtedGstMap.put(glDetail.getAccount()+"~"+glDetail.getSubLedger()+"~"+ taxVo!=null ? taxVo.getRate().replace("%", "") : "0" ,amnt+"~"+glDetail.getAccountId() );
										}
									
								}
							}
						  }
						
						
						/*
						
						//To separate GST ledger
					if(product.getTaxDetails()!=null && product.getTaxDetails().getTaxDistribution()!=null) {
						for(ARInvoiceTaxDistributionVo taxdetails : product.getTaxDetails().getTaxDistribution()) {
							if(taxdetails.getTaxName()!=null && taxdetails.getTaxRate()!=null && taxdetails.getTaxAmount()!=null) {
								Integer opLedgerId = chartOfAccountsDao.getLedgerIdGivenName(JournalEntriesConstants.OUTPUT_GST, creditNoteVo.getOrganizationId());
									if(debitLedgerMap.containsKey(JournalEntriesConstants.OUTPUT_GST+"~"+taxdetails.getTaxName()+"~"+taxdetails.getTaxRate())) {
										String[] value = debitLedgerMap.get(JournalEntriesConstants.OUTPUT_GST+"~"+taxdetails.getTaxName()+"~"+taxdetails.getTaxRate()).split("~");
										String amnt = value[0];
										Double amount = Double.valueOf(amnt);
										amount = amount + taxdetails.getTaxAmount();
										debitLedgerMap.put(JournalEntriesConstants.OUTPUT_GST+"~"+taxdetails.getTaxName()+"~"+taxdetails.getTaxRate(), amount+"~"+value[1]);
									}else {
										debitLedgerMap.put(JournalEntriesConstants.OUTPUT_GST+"~"+taxdetails.getTaxName()+"~"+taxdetails.getTaxRate(), taxdetails.getTaxAmount()+"~"+opLedgerId);
									}
								}
							}
						}
					}
			
				*/
						
						
					//To separate the ledger of TDS
					if(tdsMap!=null && tdsMap.size()>0 && creditNoteVo.getTdsValue()!=null && !creditNoteVo.getTdsValue().contentEquals("0.00")){
							logger.info("TDS from GL");
							tdsMap.forEach((key,value)->{
								creditLedgerMap.put(value,creditNoteVo.getTdsValue()+"~"+key);
							});
					}else {
						if(creditNoteVo.getTdsId()!=0 && creditNoteVo.getTdsValue()!=null && !creditNoteVo.getTdsValue().equals("0.00")) {
							Integer tdsLedgerId = chartOfAccountsDao.getLedgerIdGivenName(JournalEntriesConstants.TDS_RECEIVABLE_LEDGER, creditNoteVo.getOrganizationId());
							creditLedgerMap.put(JournalEntriesConstants.TDS_RECEIVABLE_LEDGER,Double.valueOf(creditNoteVo.getTdsValue() )+"~"+tdsLedgerId);
						}
					}
				
				//To separate the ledger of Discount 
					if(discountMap!=null && discountMap.size()>0 && creditNoteVo.getDiscountValue()!=null && !creditNoteVo.getDiscountValue().equals("0.00") ) {
						logger.info("Discount from GL");
						discountMap.forEach((key,value)->{
							creditLedgerMap.put(value,creditNoteVo.getDiscountValue()+"~"+key);
						});
					}else {
						if( creditNoteVo.getDiscLedgerId()!=0 && creditNoteVo.getDiscountValue()!=null && !creditNoteVo.getDiscountValue().equals("0.00")) {
							creditLedgerMap.put(JournalEntriesConstants.DISCOUNT_LEDGER ,Double.valueOf(creditNoteVo.getDiscountValue())+"~"+creditNoteVo.getDiscLedgerId() );
						}
					}
				
				
				//To separate the ledger of Adjustment 
					if(adjustmentMap!=null && adjustmentMap.size()>0 && creditNoteVo.getAdjLedgerId()!=0 && creditNoteVo.getAdjValue()!=null && !creditNoteVo.getAdjValue().equals("0.00")) {
						logger.info("Adjustment from GL");
						Double adjustment = Double.valueOf(creditNoteVo.getAdjValue());
						adjustmentMap.forEach((key,value)->{
							if(adjustment > 0) {
								debitLedgerMap.put(value , Math.abs(adjustment )+"~"+key);
							}else {
								creditLedgerMap.put(value,  Math.abs(adjustment)+"~"+key);
							}
						});
					}else {
				if(creditNoteVo.getAdjLedgerId()!=0 && creditNoteVo.getAdjValue()!=null && !creditNoteVo.getAdjValue().equals("0.00")) {
					Double adjustment = Double.valueOf(creditNoteVo.getAdjValue());
					if(adjustment > 0) {
						debitLedgerMap.put(JournalEntriesConstants.ADJUSTMENT_LEDGER, Math.abs(adjustment)+"~"+creditNoteVo.getAdjLedgerId());
					}else {
						creditLedgerMap.put(JournalEntriesConstants.ADJUSTMENT_LEDGER, Math.abs(adjustment)+"~"+creditNoteVo.getAdjLedgerId());
					}
				}
				}
				
			}
			logger.info("Ledgers Seaparated to creditLedgerMap::" + creditLedgerMap);
			logger.info("Ledgers Seaparated to debitLedgerMap::" + debitLedgerMap);
		}
			
			// To form the Journal entries
			if(creditLedgerMap.size()>0 && debitLedgerMap.size()>0 ) {

				creditLedgerMap.forEach((key , value) ->{
					try {
					JournalEntriesVo vo = new JournalEntriesVo();
					vo.setTransactionNo(creditNoteVo.getId());
					vo.setModule(JournalEntriesConstants.MODULE_AR);
					vo.setSubModule(JournalEntriesConstants.SUB_MODULE_CREDIT_NOTES);
					vo.setVoucherType(JournalEntriesConstants.VOUCHER_TYPE_ACCOUNTS_RECEIVABLES_CREDIT_NOTES );
					vo.setVoucherNo(creditNoteVo.getCreditNoteNumberPrefix()+"/"+ creditNoteVo.getCreditNoteNumber() + "/" + creditNoteVo.getCreditNoteNumberSuffix());
					vo.setDateOfEntry(new Date(creditNoteVo.getCreateTs().getTime()));
					Date creditNotDate = null;
					creditNotDate = DateConverter.getInstance().convertStringToDate(creditNoteVo.getCreditNoteDate(), "yyyy-MM-dd");
					logger.info("Invoice date::"+ creditNotDate);
					vo.setEffectiveDate(creditNotDate);
					String invoiceDate = null;
					invoiceDate = DateConverter.getInstance().correctDatePickerDateToString(invoiceVo.getGeneralInformation().getInvoiceDate());
					vo.setInvoiceDate(invoiceDate!=null ? Date.valueOf(invoiceDate) : null);
					vo.setParticulars(key);
					vo.setGstLevel(null);
					vo.setGstPercentage(null);
					vo.setCustomerId(creditNoteVo.getCustomerId());
					vo.setCustomerInvoiceNo(invoiceVo.getGeneralInformation().getInvoiceNoPrefix()+"/"+invoiceVo.getGeneralInformation().getInvoiceNumber()+"/"+invoiceVo.getGeneralInformation().getInvoiceNoSuffix());
					vo.setTdsId(creditNoteVo.getTdsId());
					vo.setLocationId(null);
					vo.setGst(customerDao.getGstForCustomerId(creditNoteVo.getCustomerId()));
					vo.setCurrency(creditNoteVo.getCurrencyId());
					logger.info("Credit LedgerId ::"+ value.split("~")[1]);
					vo.setLedgerId(value.split("~")[1]);
					String amnt = value.split("~")[0]!=null ? value.split("~")[0] : "0.00";
					vo.setAmountCreditOriginalCurrency(Double.valueOf(amnt));
					vo.setConversionFactor(creditNoteVo.getExchangeRate()!=null ? Double.valueOf(creditNoteVo.getExchangeRate()) : 1.0);
					vo.setAmountCredit(vo.getAmountCreditOriginalCurrency() * vo.getConversionFactor());
					vo.setRemarks(creditNoteVo.getNote()!=null && creditNoteVo.getNote().length()>50? creditNoteVo.getNote().substring(0,50) : creditNoteVo.getNote());
					vo.setUserId(creditNoteVo.getUserId());
					vo.setOrgId(creditNoteVo.getOrganizationId());
					vo.setIsSuperAdmin(creditNoteVo.getIsSuperAdmin());
					vo.setAmountDebitOriginalCurrrency(0.00);
					vo.setAmountDebit(0.00);
					journalEntries.add(vo);
					} catch (ApplicationException e) {
						logger.info(e+ "Exception in JE AR Creditnote date");

					}
				});
				
				
				debitLedgerMap.forEach((key , value) ->{
					try {
					JournalEntriesVo vo = new JournalEntriesVo();
					vo.setTransactionNo(creditNoteVo.getId());
					vo.setModule(JournalEntriesConstants.MODULE_AR);
					vo.setSubModule(JournalEntriesConstants.SUB_MODULE_CREDIT_NOTES);
					vo.setVoucherType(JournalEntriesConstants.VOUCHER_TYPE_ACCOUNTS_RECEIVABLES_CREDIT_NOTES );
					vo.setVoucherNo(creditNoteVo.getCreditNoteNumberPrefix()+"/"+ creditNoteVo.getCreditNoteNumber() + "/" + creditNoteVo.getCreditNoteNumberSuffix());
					vo.setDateOfEntry(new Date(creditNoteVo.getCreateTs().getTime()));
					Date creditNotDate = null;
					creditNotDate = DateConverter.getInstance().convertStringToDate(creditNoteVo.getCreditNoteDate(), "yyyy-MM-dd");
					logger.info("Invoice date::"+ creditNotDate);
					vo.setEffectiveDate(creditNotDate);
					String invoiceDate = null;
					invoiceDate = DateConverter.getInstance().correctDatePickerDateToString(invoiceVo.getGeneralInformation().getInvoiceDate());
					vo.setInvoiceDate(invoiceDate!=null ? Date.valueOf(invoiceDate) : null);		
					vo.setParticulars(key);
					logger.info("Credit LedgerId ::"+ value.split("~")[1]);
					vo.setCustomerId(creditNoteVo.getCustomerId());
					vo.setCustomerInvoiceNo(invoiceVo.getGeneralInformation().getInvoiceNoPrefix()+"/"+invoiceVo.getGeneralInformation().getInvoiceNumber()+"/"+invoiceVo.getGeneralInformation().getInvoiceNoSuffix());
					vo.setTdsId(creditNoteVo.getTdsId());
					vo.setLocationId(null);
					vo.setGst(customerDao.getGstForCustomerId(creditNoteVo.getCustomerId()));
					vo.setCurrency(creditNoteVo.getCurrencyId());
					vo.setConversionFactor(creditNoteVo.getExchangeRate()!=null ? Double.valueOf(creditNoteVo.getExchangeRate()) : 1.0);
					if(key.contains(JournalEntriesConstants.OUTPUT_GST)) {
						logger.info("GST ledger");
						String[] gstLedger = key.split("~");
						vo.setParticulars(gstLedger[0]);
						vo.setGstLevel(gstLedger[1]);
						vo.setGstPercentage(gstLedger[2]);
						vo.setLedgerId(value.split("~")[1]);
						String amnt = value.split("~")[0]!=null ? value.split("~")[0] : "0.00";
						vo.setAmountDebitOriginalCurrrency(Double.valueOf(amnt));
						vo.setGstInputCerdit(null);
					}else {
						vo.setParticulars(key);
						vo.setLedgerId(value.split("~")[1]);
						String amnt = value.split("~")[0]!=null ? value.split("~")[0] : "0.00";
						vo.setAmountDebitOriginalCurrrency(Double.valueOf(amnt));
						vo.setGstLevel(null);
						vo.setGstPercentage(null);
					}
					vo.setAmountDebit(vo.getAmountDebitOriginalCurrrency() * vo.getConversionFactor());
					vo.setRemarks(creditNoteVo.getNote()!=null && creditNoteVo.getNote().length()>50? creditNoteVo.getNote().substring(0,50) : creditNoteVo.getNote());
					vo.setUserId(creditNoteVo.getUserId());
					vo.setOrgId(creditNoteVo.getOrganizationId());
					vo.setIsSuperAdmin(creditNoteVo.getIsSuperAdmin());
					vo.setAmountCreditOriginalCurrency(0.00);
					vo.setAmountCredit(0.00);
					journalEntries.add(vo);
					} catch (ApplicationException e) {
						logger.info(e+ "Exception in JE AR Creditnote date");
					}
				});
			}
				if(filtedGstMap!=null && filtedGstMap.size()>0) {
					filtedGstMap.forEach((key , value) ->{
						try {
					JournalEntriesVo vo = new JournalEntriesVo();
					vo.setTransactionNo(creditNoteVo.getId());
					vo.setModule(JournalEntriesConstants.MODULE_AR);
					vo.setSubModule(JournalEntriesConstants.SUB_MODULE_CREDIT_NOTES);
					vo.setVoucherType(JournalEntriesConstants.VOUCHER_TYPE_ACCOUNTS_RECEIVABLES_CREDIT_NOTES );
					vo.setDateOfEntry(new Date(creditNoteVo.getCreateTs().getTime()));
					Date creditNotDate = null;
					try {
						creditNotDate = DateConverter.getInstance().convertStringToDate(creditNoteVo.getCreditNoteDate(), "yyyy-MM-dd");
						logger.info("Invoice date::"+ creditNotDate);

					} catch (ApplicationException e) {
						logger.info(e+ "Exception in JE AR Creditnote date");

					}
					vo.setEffectiveDate(creditNotDate);
					String invoiceDate = null;
					try {
						invoiceDate = DateConverter.getInstance().correctDatePickerDateToString(invoiceVo.getGeneralInformation().getInvoiceDate());
					} catch (ApplicationException e) {
						logger.info(e+ "Exception in JE AR Creditnote date");
					}
					vo.setInvoiceDate(invoiceDate!=null ? Date.valueOf(invoiceDate) : null);		
					vo.setParticulars(key);
					logger.info("Credit LedgerId ::"+ value.split("~")[1]);
					vo.setCustomerId(creditNoteVo.getCustomerId());
					vo.setCustomerInvoiceNo(invoiceVo.getGeneralInformation().getInvoiceNoPrefix()+"/"+invoiceVo.getGeneralInformation().getInvoiceNumber()+"/"+invoiceVo.getGeneralInformation().getInvoiceNoSuffix());
					vo.setTdsId(creditNoteVo.getTdsId());
					vo.setLocationId(null);
					vo.setGst(customerDao.getGstForCustomerId(creditNoteVo.getCustomerId()));
					vo.setCurrency(creditNoteVo.getCurrencyId());
					vo.setConversionFactor(creditNoteVo.getExchangeRate()!=null ? Double.valueOf(creditNoteVo.getExchangeRate()) : 1.0);
					String[] gstLedger = key.split("~");
					vo.setParticulars(gstLedger[0]);
					vo.setGstLevel(gstLedger[1]);
					vo.setGstPercentage(gstLedger[2]);
					vo.setLedgerId(value.split("~")[1]);
					String amnt = value.split("~")[0]!=null ? value.split("~")[0] : "0.00";
					vo.setAmountDebitOriginalCurrrency(Double.valueOf(amnt));
					vo.setGstInputCerdit(null);
					vo.setAmountDebit(vo.getAmountDebitOriginalCurrrency() * vo.getConversionFactor());
					vo.setRemarks(creditNoteVo.getNote()!=null && creditNoteVo.getNote().length()>50? creditNoteVo.getNote().substring(0,50) : creditNoteVo.getNote());
					vo.setUserId(creditNoteVo.getUserId());
					vo.setOrgId(creditNoteVo.getOrganizationId());
					vo.setIsSuperAdmin(creditNoteVo.getIsSuperAdmin());
					vo.setAmountCreditOriginalCurrency(0.00);
					vo.setAmountCredit(0.00);
					journalEntries.add(vo);
						} catch (ApplicationException e) {
							logger.info(e+ "Exception in JE AR Creditnote date");
						}
				});
				
				
				logger.info("Journal entries size :: "+ journalEntries.size());
			}
			}
		}catch (ApplicationException e) {
			logger.info("Error in getInvoiceAndGroupByLedgers:: ",e);
			throw new ApplicationException(e);
		}
		return journalEntries;
	}


	public List<JournalEntriesVo>  getARInvoiceAndGroupByLedgers(Integer invoiceId) throws ApplicationException {
		logger.info("To method getInvoiceAndGroupByLedgers with Id" + invoiceId);
		 List<JournalEntriesVo> journalEntries = new ArrayList<JournalEntriesVo>();
		try {
			ArInvoiceVo  invoiceVo = arInvoiceDao.getInvoiceById(invoiceId);
			Map<String , String > debitLedgerMap = new HashMap<String , String >();
			Map<String , String > creditLedgerMap = new HashMap<String , String >();
			GeneralLedgerVo glVo = null;
			if(invoiceVo.getGeneralInformation().getGeneralLedgerData()!=null) {
				 glVo =  invoiceVo.getGeneralInformation().getGeneralLedgerData();
			}else {
				logger.info("glvo from GL DAO"+glVo);
				glVo = generalLedgersDao.getGeneralLedgers(JournalEntriesConstants.SUB_MODULE_INVOICES, invoiceVo);
			}
			logger.info("glvo"+glVo);
			Map<Integer , String > discountMap = new HashMap<Integer, String>();
			Map<Integer , String > adjustmentMap = new HashMap<Integer, String>();
			Map<Integer , String > tdsMap = new HashMap<Integer, String>();
			Map<Integer , String > customerMap = new HashMap<Integer, String>();
			Map<String , String > gstMap = new HashMap<String, String>();
			Map<String , String > filtedGstMap = new HashMap<String, String>();

			logger.info("glvo"+glVo);
			if(glVo!=null && glVo.getGlDetails()!=null && glVo.getGlDetails().size()>0 ) {	
			for(GeneralLedgerDetailsVo details :  glVo.getGlDetails()){
				logger.info("glvo Details"+details);
				if(details.getType()!=null && details.getType().equals(JournalEntriesConstants.TYPE_DISCOUNT)) {
					discountMap.put(details.getAccountId(), details.getAccount());
				}
				if(details.getType()!=null && details.getType().equals(JournalEntriesConstants.TYPE_ADJUSTMENT)) {
					adjustmentMap.put(details.getAccountId(), details.getAccount());
				}
				if(details.getType()!=null && details.getType().equals(JournalEntriesConstants.TYPE_TDS)) {
					tdsMap.put(details.getAccountId(), details.getAccount());
				}
				if(details.getType()!=null && details.getType().equals(JournalEntriesConstants.SUBLEDGER_TYPE_CUSTOMER)) {
					customerMap.put(details.getAccountId(), details.getAccount());
				}
				if(details.getType()!=null && details.getType().equals(JournalEntriesConstants.SUBLEDGER_TYPE_GST)) {
					gstMap.put(details.getSubLedger() ,details.getAccountId()+"~"+ details.getAccount());
				}
				
			}
			}
			if(invoiceVo!=null && invoiceVo.getGeneralInformation()!=null && invoiceVo.getProducts()!=null && invoiceVo.getProducts().size()>0) {
				
				
				// To get the customer Id and its selected customer account 
				if(invoiceVo.getGeneralInformation().getCustomerId()!=null) {
					if(customerMap.size()>0) {
						customerMap.forEach((key,value)->{
							debitLedgerMap.put(value, invoiceVo.getGeneralInformation().getTotal()+"~"+key);
						});
					}else {
						String ledgerName = JournalEntriesConstants.SUNDRY_DEBTORS_SECURED;
						int ledgerId = chartOfAccountsDao.getLedgerIdGivenName(JournalEntriesConstants.SUNDRY_DEBTORS_SECURED , invoiceVo.getOrgId());
					//	String ledgerName = chartOfAccountsDao.getLedgerName(invoiceVo.getGeneralInformation().getCustomerAccountId(), invoiceVo.getOrgId());
						debitLedgerMap.put(ledgerName , invoiceVo.getGeneralInformation().getTotal()+"~"+ledgerId);

				}
					
				for(ArInvoiceProductVo product : invoiceVo.getProducts()) {
					if(product.getStatus()!=null && !"DEL".equals(product.getStatus()) &&  product.getProductAccountName()!=null ) {
					
						//To separate the ledger of each products
						if( creditLedgerMap.containsKey(product.getProductAccountName())) {
							String[] values = creditLedgerMap.get(product.getProductAccountName()).split("~");
							String amnt = values[0]!=null ? values[0] : "0.00";
							Double amount = Double.valueOf(amnt); 
							amount = amount + product.getAmount();
							creditLedgerMap.put(product.getProductAccountName(), amount+"~"+values[1]);
						}else {
							creditLedgerMap.put(product.getProductAccountName(), product.getAmount()+"~"+product.getProductAccountId());
						}
						
						}
					}
						
						
				//To separate GST ledger
				if(glVo!=null && glVo.getGlDetails()!=null && glVo.getGlDetails().size()>0) {
					for(GeneralLedgerDetailsVo glDetail : glVo.getGlDetails()) {
						if(JournalEntriesConstants.SUBLEDGER_TYPE_GST.equals(glDetail.getType()) ){
							Double amnt = glDetail.getFcyCredit()!=null ? glDetail.getFcyCredit() : glDetail.getFcyDebit();
							logger.info("glDetail.getSubLedger()::: " + glDetail.getSubLedger());
							if(glDetail.getSubLedger().split(" ").length > 1 ) {
								String rate = glDetail.getSubLedger().split(" ")[1]!=null ?  glDetail.getSubLedger().split(" ")[1] : "0 ";
								logger.info("glDetail.getSubLedger().split(\" \")[1] --> " + glDetail.getSubLedger().split(" ")[1] + "rate --> "+rate);
								filtedGstMap.put(glDetail.getAccount()+"~"+glDetail.getSubLedger()+"~"+glDetail.getSubLedger().split(" ")[1],amnt+"~"+glDetail.getAccountId() );
							}else{
								TaxRateMappingVo taxVo = taxRateMappingDao.getTaxRateMappingByName(invoiceVo.getOrgId(), glDetail.getSubLedger());
								logger.info("glDetail.getSubLedger().split(\" \")[1] --> " +taxVo.getRate() + "rate --> "+taxVo.getRate());
								filtedGstMap.put(glDetail.getAccount()+"~"+glDetail.getSubLedger()+"~"+ taxVo!=null ? taxVo.getRate().replace("%", "") : "0" ,amnt+"~"+glDetail.getAccountId() );
								}
						}
					}
				  }
				
						
					/*	
						//To separate GST ledger
					if(product.getTaxDetails()!=null && product.getTaxDetails().getTaxDistribution()!=null) {
						for(ARInvoiceTaxDistributionVo taxdetails : product.getTaxDetails().getTaxDistribution()) {
							if(taxdetails.getTaxName()!=null && taxdetails.getTaxRate()!=null && taxdetails.getTaxAmount()!=null) {
								Integer opLedgerId = chartOfAccountsDao.getLedgerIdGivenName(JournalEntriesConstants.OUTPUT_GST, invoiceVo.getOrgId());
								logger.info("taxvvalue"+JournalEntriesConstants.OUTPUT_GST+"~"+taxdetails.getTaxName()+"~"+taxdetails.getTaxRate());
									if(creditLedgerMap.containsKey(JournalEntriesConstants.OUTPUT_GST+"~"+taxdetails.getTaxName()+"~"+taxdetails.getTaxRate())) {
										String[] values= creditLedgerMap.get(JournalEntriesConstants.OUTPUT_GST+"~"+taxdetails.getTaxName()+"~"+taxdetails.getTaxRate()).split("~");
										String amnt = values[0];
										Double amount = Double.valueOf(amnt);
										amount = amount + taxdetails.getTaxAmount();
										creditLedgerMap.put(JournalEntriesConstants.OUTPUT_GST+"~"+taxdetails.getTaxName()+"~"+taxdetails.getTaxRate(), amount+"~"+values[1]);
									}else {
										creditLedgerMap.put(JournalEntriesConstants.OUTPUT_GST+"~"+taxdetails.getTaxName()+"~"+taxdetails.getTaxRate(), taxdetails.getTaxAmount()+"~"+opLedgerId);
									}
								}
							}
						}
					}
				}
				*/
						
						
					//To separate the ledger of TDS
				if(tdsMap!=null && tdsMap.size()>0 && invoiceVo.getGeneralInformation().getTdsValue()!=null){
						logger.info("TDS from GL");
						tdsMap.forEach((key,value)->{
							debitLedgerMap.put(value,invoiceVo.getGeneralInformation().getTdsValue()+"~"+key);
						});
				}else {
					if(invoiceVo.getGeneralInformation().getTdsId()!=null && invoiceVo.getGeneralInformation().getTdsId()!=0 && invoiceVo.getGeneralInformation().getTdsValue()!=null && !invoiceVo.getGeneralInformation().getTdsValue().equals(0.0)) {
						Integer tdsLedgerId = chartOfAccountsDao.getLedgerIdGivenName(JournalEntriesConstants.TDS_RECEIVABLE_LEDGER, invoiceVo.getOrgId());
						debitLedgerMap.put(JournalEntriesConstants.TDS_RECEIVABLE_LEDGER,invoiceVo.getGeneralInformation().getTdsValue()+"~"+ tdsLedgerId);
					}
				}
				
				//To separate the ledger of Discount 
				if(discountMap!=null && discountMap.size()>0 && invoiceVo.getGeneralInformation().getDiscountValue()!=null && !invoiceVo.getGeneralInformation().getDiscountValue().equals(0.0)) {
					logger.info("Discount from GL");
					discountMap.forEach((key,value)->{
						debitLedgerMap.put(value,invoiceVo.getGeneralInformation().getDiscountValue()+"~"+key);
					});
				}else {
				if(invoiceVo.getGeneralInformation().getDiscountValue()!=null && !invoiceVo.getGeneralInformation().getDiscountValue().equals(0.0)) {
					int ledgerId = chartOfAccountsDao.getLedgerIdGivenName(JournalEntriesConstants.DISCOUNT_LEDGER , invoiceVo.getOrgId());
						debitLedgerMap.put(JournalEntriesConstants.DISCOUNT_LEDGER ,invoiceVo.getGeneralInformation().getDiscountValue()+"~"+ledgerId);
				}
				}
				
				
				//To separate the ledger of Adjustment 
				if(adjustmentMap!=null && adjustmentMap.size()>0 && invoiceVo.getGeneralInformation().getAdjustmentValue()!=null && !invoiceVo.getGeneralInformation().getAdjustmentValue().equals(0.0)) {
					logger.info("Adjustment from GL");
					adjustmentMap.forEach((key,value)->{
						if(invoiceVo.getGeneralInformation().getAdjustmentValue() > 0) {
							creditLedgerMap.put(value , Math.abs(invoiceVo.getGeneralInformation().getAdjustmentValue() )+"~"+key);
						}else {
							debitLedgerMap.put(value,  Math.abs(invoiceVo.getGeneralInformation().getAdjustmentValue() )+"~"+key);
						}
					});
				}else {

					if( invoiceVo.getGeneralInformation().getAdjustmentValue()!=null && !invoiceVo.getGeneralInformation().getAdjustmentValue().equals(0.0)) {
						int ledgerId = chartOfAccountsDao.getLedgerIdGivenName(JournalEntriesConstants.ADJUSTMENT_LEDGER , invoiceVo.getOrgId());

						if(invoiceVo.getGeneralInformation().getAdjustmentValue() > 0) {
							creditLedgerMap.put(JournalEntriesConstants.ADJUSTMENT_LEDGER , Math.abs(invoiceVo.getGeneralInformation().getAdjustmentValue() )+"~"+ledgerId);
						}else {
							debitLedgerMap.put(JournalEntriesConstants.ADJUSTMENT_LEDGER ,  Math.abs(invoiceVo.getGeneralInformation().getAdjustmentValue() )+"~"+ledgerId);
						}
						
					}
					
				}
				
				
				
			}
			logger.info("Ledgers Seaparated to creditLedgerMap::" + creditLedgerMap);
			logger.info("Ledgers Seaparated to debitLedgerMap::" + debitLedgerMap);
		}
			
			// To form the Journal entries
			if(creditLedgerMap.size()>0 && debitLedgerMap.size()>0 ) {

				creditLedgerMap.forEach((key , value) ->{
					try {
					JournalEntriesVo vo = new JournalEntriesVo();
					vo.setTransactionNo(invoiceVo.getInvoiceId());
					vo.setModule(JournalEntriesConstants.MODULE_AR);
					vo.setSubModule(JournalEntriesConstants.SUB_MODULE_INVOICES);
					vo.setVoucherType(JournalEntriesConstants.VOUCHER_TYPE_ACCOUNTS_RECEIVABLES_INVOICES );
					vo.setVoucherNo(invoiceVo.getGeneralInformation().getInvoiceNoPrefix()+"/"+invoiceVo.getGeneralInformation().getInvoiceNumber()+"/"+invoiceVo.getGeneralInformation().getInvoiceNoSuffix());
					vo.setDateOfEntry(invoiceVo.getGeneralInformation().getCreate_ts());
					logger.info("Invoice date::"+ invoiceVo.getGeneralInformation().getInvoiceDate());
					String invoiceDate = null;
					invoiceDate = DateConverter.getInstance().correctDatePickerDateToString(invoiceVo.getGeneralInformation().getInvoiceDate());
					vo.setEffectiveDate(invoiceDate!=null ? Date.valueOf(invoiceDate) : null);
					vo.setInvoiceDate(invoiceDate!=null ? Date.valueOf(invoiceDate) : null);
					if(key.contains(JournalEntriesConstants.OUTPUT_GST)) {
						logger.info("GST ledger");
						String[] gstLedger = key.split("~");
						vo.setParticulars(gstLedger[0]);
						vo.setGstLevel(gstLedger[1]);
						vo.setGstPercentage(gstLedger[2]);
						logger.info(" LedgerId ::"+ value.split("~")[1]);
						vo.setLedgerId(value.split("~")[1]);
						String amnt = value.split("~")[0]!=null ? value.split("~")[0] : "0.00";
						vo.setAmountCreditOriginalCurrency(Double.valueOf(amnt));
						vo.setGstInputCerdit(null);
					}else {
						vo.setParticulars(key);
						logger.info(" LedgerId ::"+ value.split("~")[1]);
						vo.setLedgerId(value.split("~")[1]);
						String amnt = value.split("~")[0]!=null ? value.split("~")[0] : "0.00";
						vo.setAmountCreditOriginalCurrency(Double.valueOf(amnt));
						vo.setGstLevel(null);
						vo.setGstPercentage(null);
					}
					vo.setCustomerId(invoiceVo.getGeneralInformation().getCustomerId());
					vo.setCustomerInvoiceNo(invoiceVo.getGeneralInformation().getInvoiceNoPrefix()+"/"+invoiceVo.getGeneralInformation().getInvoiceNumber()+"/"+invoiceVo.getGeneralInformation().getInvoiceNoSuffix());
					vo.setCustomerPoNo(invoiceVo.getGeneralInformation().getPurchaseOrderNo());
					vo.setTdsId(invoiceVo.getGeneralInformation().getTdsId());
					vo.setLocationId(null);
					vo.setGst(customerDao.getGstForCustomerId(invoiceVo.getGeneralInformation().getCustomerId()));
					vo.setCurrency(invoiceVo.getGeneralInformation().getCurrencyId());
					vo.setConversionFactor(invoiceVo.getGeneralInformation().getExchangeRate());
					vo.setAmountCredit(vo.getAmountCreditOriginalCurrency() * vo.getConversionFactor());
					vo.setRemarks(invoiceVo.getGeneralInformation().getNotes()!=null && invoiceVo.getGeneralInformation().getNotes().length()>50? invoiceVo.getGeneralInformation().getNotes().substring(0,50) : invoiceVo.getGeneralInformation().getNotes());
					vo.setUserId(invoiceVo.getUserId());
					vo.setOrgId(invoiceVo.getOrgId());
					vo.setIsSuperAdmin(invoiceVo.getIsSuperAdmin());
					vo.setBankId(invoiceVo.getGeneralInformation().getBankId());
					String bankType = null;
					if("bankAccounts".equals(invoiceVo.getGeneralInformation().getBankType())) {
						bankType = JournalEntriesConstants.BANK_TYPE_BANK_ACCOUNT;
					}
					if("cardAccounts".equals(invoiceVo.getGeneralInformation().getBankType())) {
						bankType = JournalEntriesConstants.BANK_TYPE_CREDIT_CARD;
					}
					if("cashAccounts".equals(invoiceVo.getGeneralInformation().getBankType())) {
						bankType = JournalEntriesConstants.BANK_TYPE_CASH_ACCOUNT;
					}
					if("wallets".equals(invoiceVo.getGeneralInformation().getBankType())) {
						bankType = JournalEntriesConstants.BANK_TYPE_WALLET;		
					}	
					vo.setBankType(bankType);
					vo.setAmountDebitOriginalCurrrency(0.00);
					vo.setAmountDebit(0.00);
					journalEntries.add(vo);
					} catch (ApplicationException e) {
						logger.info(e+ "Exception in JE AR Invoice date");
					}
				});
				
				if(filtedGstMap!=null && filtedGstMap.size()>0) {
					filtedGstMap.forEach((key,value)->{
						try {
						JournalEntriesVo vo = new JournalEntriesVo();
						vo.setTransactionNo(invoiceVo.getInvoiceId());
						vo.setModule(JournalEntriesConstants.MODULE_AR);
						vo.setSubModule(JournalEntriesConstants.SUB_MODULE_INVOICES);
						vo.setVoucherType(JournalEntriesConstants.VOUCHER_TYPE_ACCOUNTS_RECEIVABLES_INVOICES );
						vo.setVoucherNo(invoiceVo.getGeneralInformation().getInvoiceNoPrefix()+"/"+invoiceVo.getGeneralInformation().getInvoiceNumber()+"/"+invoiceVo.getGeneralInformation().getInvoiceNoSuffix());
						vo.setDateOfEntry(invoiceVo.getGeneralInformation().getCreate_ts());
						logger.info("Invoice date::"+ invoiceVo.getGeneralInformation().getInvoiceDate());
						String invoiceDate = null;
						invoiceDate = DateConverter.getInstance().correctDatePickerDateToString(invoiceVo.getGeneralInformation().getInvoiceDate());
						vo.setEffectiveDate(invoiceDate!=null ? Date.valueOf(invoiceDate) : null);
						vo.setInvoiceDate(invoiceDate!=null ? Date.valueOf(invoiceDate) : null);
							logger.info("GST ledger");
							String[] gstLedger = key.split("~");
							vo.setParticulars(gstLedger[0]);
							vo.setGstLevel(gstLedger[1]);
							vo.setGstPercentage(gstLedger[2]);
							logger.info(" LedgerId ::"+ value.split("~")[1]);
							vo.setLedgerId(value.split("~")[1]);
							String amnt = value.split("~")[0]!=null ? value.split("~")[0] : "0.00";
							vo.setAmountCreditOriginalCurrency(Double.valueOf(amnt));
							vo.setGstInputCerdit(null);
						vo.setCustomerId(invoiceVo.getGeneralInformation().getCustomerId());
						vo.setCustomerInvoiceNo(invoiceVo.getGeneralInformation().getInvoiceNoPrefix()+"/"+invoiceVo.getGeneralInformation().getInvoiceNumber()+"/"+invoiceVo.getGeneralInformation().getInvoiceNoSuffix());
						vo.setCustomerPoNo(invoiceVo.getGeneralInformation().getPurchaseOrderNo());
						vo.setTdsId(invoiceVo.getGeneralInformation().getTdsId());
						vo.setLocationId(null);
						vo.setGst(customerDao.getGstForCustomerId(invoiceVo.getGeneralInformation().getCustomerId()));
						vo.setCurrency(invoiceVo.getGeneralInformation().getCurrencyId());
						vo.setConversionFactor(invoiceVo.getGeneralInformation().getExchangeRate());
						vo.setAmountCredit(vo.getAmountCreditOriginalCurrency() * vo.getConversionFactor());
						vo.setRemarks(invoiceVo.getGeneralInformation().getNotes()!=null && invoiceVo.getGeneralInformation().getNotes().length()>50? invoiceVo.getGeneralInformation().getNotes().substring(0,50) : invoiceVo.getGeneralInformation().getNotes());
						vo.setUserId(invoiceVo.getUserId());
						vo.setOrgId(invoiceVo.getOrgId());
						vo.setIsSuperAdmin(invoiceVo.getIsSuperAdmin());
						vo.setBankId(invoiceVo.getGeneralInformation().getBankId());
						String bankType = null;
						if("bankAccounts".equals(invoiceVo.getGeneralInformation().getBankType())) {
							bankType = JournalEntriesConstants.BANK_TYPE_BANK_ACCOUNT;
						}
						if("cardAccounts".equals(invoiceVo.getGeneralInformation().getBankType())) {
							bankType = JournalEntriesConstants.BANK_TYPE_CREDIT_CARD;
						}
						if("cashAccounts".equals(invoiceVo.getGeneralInformation().getBankType())) {
							bankType = JournalEntriesConstants.BANK_TYPE_CASH_ACCOUNT;
						}
						if("wallets".equals(invoiceVo.getGeneralInformation().getBankType())) {
							bankType = JournalEntriesConstants.BANK_TYPE_WALLET;		
						}	
						vo.setBankType(bankType);
						vo.setAmountDebitOriginalCurrrency(0.00);
						vo.setAmountDebit(0.00);
						journalEntries.add(vo);
						} catch (ApplicationException e) {
							logger.info(e+ "Exception in JE AR Invoice date");
						}
					});
				}
				debitLedgerMap.forEach((key , value) ->{
					try {
					JournalEntriesVo vo = new JournalEntriesVo();
					vo.setTransactionNo(invoiceVo.getInvoiceId());
					vo.setModule(JournalEntriesConstants.MODULE_AR);
					vo.setSubModule(JournalEntriesConstants.SUB_MODULE_INVOICES);
					vo.setVoucherType(JournalEntriesConstants.VOUCHER_TYPE_ACCOUNTS_RECEIVABLES_INVOICES );
					vo.setDateOfEntry(invoiceVo.getGeneralInformation().getCreate_ts());
					String invoiceDate = null;
						invoiceDate = DateConverter.getInstance().correctDatePickerDateToString(invoiceVo.getGeneralInformation().getInvoiceDate());
					vo.setEffectiveDate(invoiceDate!=null ? Date.valueOf(invoiceDate) : null);
					vo.setInvoiceDate(invoiceDate!=null ? Date.valueOf(invoiceDate) : null);
					vo.setParticulars(key);
					logger.info(" LedgerId ::"+ value.split("~")[1]);
					vo.setLedgerId(value.split("~")[1]);
					String amnt = value.split("~")[0]!=null ? value.split("~")[0] : "0.00";
					vo.setAmountDebitOriginalCurrrency(Double.valueOf(amnt));
					vo.setCustomerId(invoiceVo.getGeneralInformation().getCustomerId());
					vo.setCustomerInvoiceNo(invoiceVo.getGeneralInformation().getInvoiceNoPrefix()+"/"+invoiceVo.getGeneralInformation().getInvoiceNumber()+"/"+invoiceVo.getGeneralInformation().getInvoiceNoSuffix());
					vo.setCustomerPoNo(invoiceVo.getGeneralInformation().getPurchaseOrderNo());
					vo.setTdsId(invoiceVo.getGeneralInformation().getTdsId());
					vo.setLocationId(null);
					vo.setGst(customerDao.getGstForCustomerId(invoiceVo.getGeneralInformation().getCustomerId()));
					vo.setCurrency(invoiceVo.getGeneralInformation().getCurrencyId());
					vo.setConversionFactor(invoiceVo.getGeneralInformation().getExchangeRate());
					vo.setGstLevel(null);
					vo.setGstPercentage(null);
					vo.setAmountDebit(vo.getAmountDebitOriginalCurrrency() * vo.getConversionFactor());
					vo.setRemarks(invoiceVo.getGeneralInformation().getNotes()!=null && invoiceVo.getGeneralInformation().getNotes().length()>50? invoiceVo.getGeneralInformation().getNotes().substring(0,50) : invoiceVo.getGeneralInformation().getNotes());
					vo.setUserId(invoiceVo.getUserId());
					vo.setOrgId(invoiceVo.getOrgId());
					vo.setIsSuperAdmin(invoiceVo.getIsSuperAdmin());
					vo.setBankId(invoiceVo.getGeneralInformation().getBankId());
					String bankType = null;
					if("bankAccounts".equals(invoiceVo.getGeneralInformation().getBankType())) {
						bankType = JournalEntriesConstants.BANK_TYPE_BANK_ACCOUNT;
					}
					if("cardAccounts".equals(invoiceVo.getGeneralInformation().getBankType())) {
						bankType = JournalEntriesConstants.BANK_TYPE_CREDIT_CARD;
					}
					if("cashAccounts".equals(invoiceVo.getGeneralInformation().getBankType())) {
						bankType = JournalEntriesConstants.BANK_TYPE_CASH_ACCOUNT;
					}
					if("wallets".equals(invoiceVo.getGeneralInformation().getBankType())) {
						bankType = JournalEntriesConstants.BANK_TYPE_WALLET;		
					}	
					vo.setBankType(bankType);
					vo.setAmountCreditOriginalCurrency(0.00);
					vo.setAmountCredit(0.00);
					journalEntries.add(vo);
					} catch (ApplicationException e) {
						logger.info(e+ "Exception in JE AR Invoice date");
					}
				});
				logger.info("Journal entries size :: "+ journalEntries.size());
			}
		}catch (ApplicationException e) {
			logger.info("Error in getInvoiceAndGroupByLedgers:: ",e);
			throw new ApplicationException(e);
		}
		return journalEntries;
	}


	
	public List<JournalEntriesVo> mapContraEntriesToJournalEntriesVo(Integer contraEntryId) throws ApplicationException{
		List<JournalEntriesVo> journalEntriesVos = null;
		try {
			logger.info("To get mapAccountingEntriesToJournalEntriesVo " );
			ContraVo contraVo= contraDao.getContraAccountById(contraEntryId);
			if(contraVo != null && contraVo.getContraEntries()!=null && contraVo.getContraEntries().size()>0 ) {
				journalEntriesVos = new ArrayList<JournalEntriesVo>();
				for(ContraEntriesVo item : contraVo.getContraEntries()) {
					JournalEntriesVo vo = new JournalEntriesVo();
					vo.setTransactionNo(contraVo.getId());
					vo.setModule(JournalEntriesConstants.MODULE_MANUAL_JOURNALS);
					vo.setSubModule(JournalEntriesConstants.SUB_MODULE_CONTRA);
					vo.setVoucherType(JournalEntriesConstants.VOUCHER_TYPE_CONTRA);
					vo.setVoucherNo(contraVo.getReferenceNo());
					vo.setDateOfEntry(contraVo.getCreateTs());
					vo.setEffectiveDate(contraVo.getDate());
					String bankLedger = bankMasterDao.getAccountName(item.getAccountId(), item.getAccountType());
				//	String bankLedger = getLedgerForBankType(item.getAccountType(), item.getAccountId());
					Integer ledgerId = chartOfAccountsDao.getLedgerIdGivenName(bankLedger, contraVo.getOrgId());
					vo.setLedgerId(ledgerId+"");
					vo.setParticulars(bankLedger);
					vo.setBankId(Integer.valueOf(item.getAccountId()));
					vo.setBankType(item.getAccountType());
					vo.setAmountDebitOriginalCurrrency(item.getDebit()!=null ? Double.valueOf(item.getDebit()): null);
					vo.setAmountCreditOriginalCurrency(item.getCredit()!=null ? Double.valueOf(item.getCredit()): null);
					vo.setCurrency(item.getCurrencyId());
					vo.setConversionFactor(item.getExchangeRate()!=null ? Double.valueOf(item.getExchangeRate()) : null);
					vo.setAmountCredit(vo.getAmountCreditOriginalCurrency() * vo.getConversionFactor());
					vo.setAmountDebit(vo.getAmountDebitOriginalCurrrency() * vo.getConversionFactor());
					vo.setRemarks(contraVo.getRemark()!=null && contraVo.getRemark().length()>50? contraVo.getRemark().substring(0,50) : contraVo.getRemark());
					vo.setUserId(contraVo.getUserId());
					vo.setOrgId(contraVo.getOrgId());
					vo.setIsSuperAdmin(contraVo.getIsSuperAdmin());
					vo.setRoleName(contraVo.getRoleName());
					journalEntriesVos.add(vo);
				}
				logger.info("mapAccountingEntriesToJournalEntriesVos are::" +journalEntriesVos.size());
			}
		} catch (ApplicationException e) {
			logger.info("Error in mapAccountingEntriesToJournalEntriesVo:: ",e);
			throw new ApplicationException(e);
		}
		return journalEntriesVos;
	}
	
	

	public List<JournalEntriesVo> mapApplicationOfFundsToJournalEntriesVo(Integer applyCreditId) throws ApplicationException{
		final List<JournalEntriesVo> journalEntries =  new ArrayList<JournalEntriesVo>();
		try {
			logger.info("To get mapApplicationOfFundsToJournalEntriesVo " );
			ApplyCreditsVo applyCreditsVo = applyCreditsDao.getApplyCreditsById(applyCreditId);
			if(applyCreditsVo != null && applyCreditsVo.getInvoiceDetails()!=null &&  applyCreditsVo.getCreditDetails()!=null && !applyCreditsVo.getInvoiceDetails().isEmpty() && !applyCreditsVo.getCreditDetails().isEmpty() ) {
				Connection con2 = getUserMgmConnection();
				BasicVoucherEntriesVo voucher =organizationDao.getBasicVoucherEntries(applyCreditsVo.getOrganizationId(), con2,"Accounts Receivable-Application of Funds");
				closeResources(null, null, con2);

				StringBuilder voucherBuilder = null;
				if(voucher!=null ) {
					voucherBuilder = new StringBuilder().append(voucher.getPrefix()).append("/").append(applyCreditsVo.getVoucherNo()).append("/").append(voucher.getSuffix());
				}
				String invoiceNo = null;
				
				if(applyCreditsVo.getCreditDetails()!=null && applyCreditsVo.getCustomTableList()!=null && !applyCreditsVo.getCreditDetails().isEmpty() && !applyCreditsVo.getCustomTableList().isEmpty()) {
					for(ApplyCreditsInvoiceVo invoice : applyCreditsVo.getInvoiceDetails()) {
						Map<String ,String> debitMap = new HashMap<String, String>();
						Map<String ,String> creditMap = new HashMap<String, String>();
						if(invoice.getInvoiceAmount()!=null && applyCreditsVo.getLedgerName()!=null && !applyCreditsVo.getLedgerName().isEmpty()) {
								InvoiceDetailsVo basicInvoiceVo = arInvoiceDao.getInvoiceDetailsByCustomerId(applyCreditsVo.getOrganizationId(), applyCreditsVo.getCustomerId(), applyCreditsVo.getCurrencyId()).stream().filter(vo -> vo.getId().equals(invoice.getInvoiceId())).findAny().orElse(null);
								invoiceNo = basicInvoiceVo.getName();
								logger.info("Invoice Number ::"+ invoiceNo); 
								Double invoiceAmnt = Double.valueOf(invoice.getInvoiceAmount());
									
									if(invoiceAmnt>0) {
										creditMap.put(applyCreditsVo.getLedgerName(), invoiceAmnt+"~"+applyCreditsVo.getLedgerId());
									}
									if(invoiceAmnt<0) {
										debitMap.put(applyCreditsVo.getLedgerName(), invoiceAmnt+"~"+applyCreditsVo.getLedgerId());
									}
							}
						
						for (ReceiptSettingsVo categoryTable : applyCreditsVo.getCustomTableList()) {
							if ("bankCharges".equals(categoryTable.getcName()) && categoryTable.isColumnShow()
									&& invoice.getBankCharges() != null && !invoice.getBankCharges().isEmpty()) {
								String bankChargesLedger = categoryTable.getLedgerName();
								logger.info("bank chargers ledger::" + bankChargesLedger);
								Double bankChargesAmount = Double.valueOf(invoice.getBankCharges());
								if (bankChargesAmount > 0) {
									creditMap.put(bankChargesLedger, bankChargesAmount+"~"+categoryTable.getLedgerId());
								}
								if (bankChargesAmount < 0) {
									Double amnt =  bankChargesAmount * -1;
									debitMap.put(bankChargesLedger,amnt+"~"+categoryTable.getLedgerId());
								}
							}
							if ("tdsDeducted".equals(categoryTable.getcName()) && categoryTable.isColumnShow()
									&& invoice.getTdsDeducted() != null && !invoice.getTdsDeducted().isEmpty()) {
								String tdsLedger = categoryTable.getLedgerName();
								logger.info("TDS ledger::" + tdsLedger);
								Double tdsAmount = Double.valueOf(invoice.getTdsDeducted());
								if (tdsAmount > 0) {
									creditMap.put(tdsLedger, tdsAmount+"~"+categoryTable.getLedgerId());
								}
								if (tdsAmount < 0) {
									Double amnt = tdsAmount * -1;
									debitMap.put(tdsLedger, amnt+"~"+categoryTable.getLedgerId());
								}
							}

							if ("others1".equals(categoryTable.getcName()) && categoryTable.isColumnShow()
									&& invoice.getOthers1() != null &&  !invoice.getOthers1().isEmpty()) {
								String others1Ledger = categoryTable.getLedgerName();
								logger.info("others 1 ledger::" + others1Ledger);
								Double others1Amount = Double.valueOf(invoice.getOthers1());
								if (others1Amount > 0) {
									creditMap.put(others1Ledger, others1Amount+"~"+categoryTable.getLedgerId());
								}
								if (others1Amount < 0) {
									Double amnt = others1Amount * -1;
									debitMap.put(others1Ledger,amnt +"~"+categoryTable.getLedgerId());
								}
							}

							if ("others2".equals(categoryTable.getcName()) && categoryTable.isColumnShow()
									&& invoice.getOthers2() != null && !invoice.getOthers2().isEmpty()) {
								String others2Ledger =categoryTable.getLedgerName();
								logger.info("others 2 ledger::" + others2Ledger);
								Double others2Amount = Double.valueOf(invoice.getOthers2());
								if (others2Amount > 0) {
									creditMap.put(others2Ledger, others2Amount+"~"+categoryTable.getLedgerId());
								}
								if (others2Amount < 0) {
									Double amnt = others2Amount * -1;
									debitMap.put(others2Ledger, amnt+"~"+categoryTable.getLedgerId());
								}
							}

							if ("others3".equals(categoryTable.getcName()) && categoryTable.isColumnShow()
									&& invoice.getOthers3() != null && !invoice.getOthers3().isEmpty()) {
								String others3Ledger = categoryTable.getLedgerName();
								logger.info("others 3 ledger::" + others3Ledger);
								Double others3Amount = Double.valueOf(invoice.getOthers2());
								if (others3Amount > 0) {
									creditMap.put(others3Ledger, others3Amount+"~"+categoryTable.getLedgerId());
								}
								if (others3Amount < 0) {
									Double amnt = others3Amount * -1;
									debitMap.put(others3Ledger, amnt+"~"+categoryTable.getLedgerId());
								}
							}
						}
					logger.info("Debitmap::"+debitMap);
					logger.info("Creditmap::"+creditMap);
				
					final String voucherValue  = voucherBuilder!=null ? voucherBuilder.toString() : null;
					// To form the Journal entries
					final String invNo = invoiceNo;
					if(creditMap.size()>0  ) {
						creditMap.forEach((key , value) ->{
							JournalEntriesVo vo = new JournalEntriesVo();
							vo.setTransactionNo(applyCreditsVo.getId());
							vo.setModule(JournalEntriesConstants.MODULE_AR);
							vo.setSubModule(JournalEntriesConstants.SUB_MODULE_APPLICATION_OF_FUNDS);
							vo.setVoucherType(JournalEntriesConstants.VOUCHER_TYPE_APPLICATION_OF_FUNDS );
							vo.setDateOfEntry(applyCreditsVo.getCreateTs());
							logger.info("Apply credit rate date::"+ applyCreditsVo.getDate());
							vo.setEffectiveDate(  applyCreditsVo.getDate());
							vo.setVoucherNo(voucherValue);
							vo.setParticulars(key);
							vo.setCustomerId(applyCreditsVo.getCustomerId());
							vo.setCustomerInvoiceNo(invNo);
							vo.setCurrency(applyCreditsVo.getCurrencyId());
							logger.info(" LedgerId ::"+ value.split("~")[1]);
							vo.setLedgerId(value.split("~")[1]);
							String amnt = value.split("~")[0]!=null ? value.split("~")[0] : "0.00";
							vo.setAmountCreditOriginalCurrency(Double.valueOf(amnt));
							vo.setConversionFactor(applyCreditsVo.getExchangeRate()!=null ? Double.valueOf(applyCreditsVo.getExchangeRate()): 1.00);
							vo.setAmountCredit(vo.getAmountCreditOriginalCurrency() * vo.getConversionFactor());
							vo.setUserId(applyCreditsVo.getUserId());
							vo.setOrgId(applyCreditsVo.getOrganizationId());
							vo.setIsSuperAdmin(applyCreditsVo.getIsSuperAdmin());
							vo.setAmountDebitOriginalCurrrency(0.00);
							vo.setAmountDebit(0.00);
							journalEntries.add(vo);
						});
						
					}
					if(debitMap.size()>0) {
						debitMap.forEach((key , value) ->{
							JournalEntriesVo vo = new JournalEntriesVo();
							vo.setTransactionNo(applyCreditsVo.getId());
							vo.setModule(JournalEntriesConstants.MODULE_AR);
							vo.setSubModule(JournalEntriesConstants.SUB_MODULE_APPLICATION_OF_FUNDS);
							vo.setVoucherType(JournalEntriesConstants.VOUCHER_TYPE_APPLICATION_OF_FUNDS);
							vo.setDateOfEntry(applyCreditsVo.getCreateTs());
							logger.info("Receipt date::"+ applyCreditsVo.getDate());
							vo.setEffectiveDate( applyCreditsVo.getDate());
							vo.setVoucherNo(voucherValue);
							vo.setParticulars(key);
							vo.setCustomerId(applyCreditsVo.getCustomerId());
							vo.setCustomerInvoiceNo(invNo);
							vo.setCurrency(applyCreditsVo.getCurrencyId());
							logger.info(" LedgerId ::"+ value.split("~")[1]);
							vo.setLedgerId(value.split("~")[1]);
							String amnt = value.split("~")[0]!=null ? value.split("~")[0] : "0.00";
							vo.setAmountDebitOriginalCurrrency(Double.valueOf(amnt));
							vo.setConversionFactor(applyCreditsVo.getExchangeRate()!=null ? Double.valueOf(applyCreditsVo.getExchangeRate()): 1.00);
							vo.setAmountDebit(vo.getAmountDebitOriginalCurrrency() * vo.getConversionFactor());
							vo.setUserId(applyCreditsVo.getUserId());
							vo.setOrgId(applyCreditsVo.getOrganizationId());
							vo.setIsSuperAdmin(applyCreditsVo.getIsSuperAdmin());
							vo.setAmountCreditOriginalCurrency(0.00);
							vo.setAmountCredit(0.00);
							journalEntries.add(vo);
						});
					}
				}
				}
					// For credit details 
					if(applyCreditsVo.getCreditDetails()!=null && !applyCreditsVo.getCreditDetails().isEmpty()) {
						for(ApplyCreditDetailsVo credidetail : applyCreditsVo.getCreditDetails()) {
							if(credidetail.getAdjustmentAmount()!=null && !credidetail.getAdjustmentAmount().equals("0.0")  && !credidetail.getAdjustmentAmount().equals("0.00")) {
							JournalEntriesVo vo = new JournalEntriesVo();
							vo.setTransactionNo(applyCreditsVo.getId());
							vo.setModule(JournalEntriesConstants.MODULE_AR);
							vo.setSubModule(JournalEntriesConstants.SUB_MODULE_APPLICATION_OF_FUNDS);
							vo.setVoucherType(JournalEntriesConstants.VOUCHER_TYPE_APPLICATION_OF_FUNDS);
							vo.setDateOfEntry(applyCreditsVo.getCreateTs());
							logger.info("Receipt date::"+ applyCreditsVo.getDate());
							vo.setEffectiveDate( applyCreditsVo.getDate());
							vo.setVoucherNo(voucherBuilder!=null ? voucherBuilder.toString() : null);
							vo.setParticulars(credidetail.getLedgerName());
							vo.setCustomerId(applyCreditsVo.getCustomerId());
							vo.setCustomerInvoiceNo(invoiceNo);
							vo.setCurrency(applyCreditsVo.getCurrencyId());
							vo.setLedgerId(credidetail.getLedgerId()!=null ? String.valueOf(credidetail.getLedgerId()): "0");
							vo.setAmountDebitOriginalCurrrency(Double.valueOf(credidetail.getAdjustmentAmount()));
							vo.setConversionFactor(applyCreditsVo.getExchangeRate()!=null ? Double.valueOf(applyCreditsVo.getExchangeRate()): 1.00);
							vo.setAmountDebit(vo.getAmountDebitOriginalCurrrency() * vo.getConversionFactor());
							vo.setUserId(applyCreditsVo.getUserId());
							vo.setOrgId(applyCreditsVo.getOrganizationId());
							vo.setIsSuperAdmin(applyCreditsVo.getIsSuperAdmin());
							vo.setAmountCreditOriginalCurrency(0.00);
							vo.setAmountCredit(0.00);
							journalEntries.add(vo);
							}
						}
					}
					logger.info("Journal entries size :: "+ journalEntries.size());
				}
			}catch (ApplicationException e) {
				logger.info("Error in getARReceiptsAndMapToJournalEntries:: ",e);
				throw new ApplicationException(e);
			}
			return journalEntries;
	}

	

	public List<JournalEntriesVo> mapAccountingEntriesToJournalEntriesVo(Integer accountingEntryId) throws ApplicationException{
		List<JournalEntriesVo> journalEntriesVos = null;
		try {
			logger.info("To get mapAccountingEntriesToJournalEntriesVo " );
			AccountingAspectsVo accountingAspectsVo= accountingAspectsDao.getAccountingAspectsById(accountingEntryId);
			if(accountingAspectsVo != null&&accountingAspectsVo.getAccountingAspectsGeneralInfo()!=null && accountingAspectsVo.getItemDetails()!=null &&  accountingAspectsVo.getItemDetails().size()>0 ) {
				journalEntriesVos = new ArrayList<JournalEntriesVo>();
				for(AccountingAspectsItemsVo item : accountingAspectsVo.getItemDetails()) {
					JournalEntriesVo vo = new JournalEntriesVo();
					vo.setTransactionNo(accountingAspectsVo.getId());
					vo.setModule(JournalEntriesConstants.MODULE_MANUAL_JOURNALS);
					String subModule = financeCommonDao.findAccountingTypeName(accountingAspectsVo.getAccountingAspectsGeneralInfo().getTypeId());
					vo.setSubModule(subModule);
					vo.setVoucherType(JournalEntriesConstants.VOUCHER_TYPE_ACCOUNTING_ASPECTS);
					Connection  con = getUserMgmConnection();
					BasicVoucherEntriesVo voucher = organizationDao.getBasicVoucherEntries(accountingAspectsVo.getOrganizationId(), con , "Accounting Entries");
					closeResources(null, null, con);
					vo.setVoucherNo(voucher.getPrefix() + "/" + accountingAspectsVo.getAccountingAspectsGeneralInfo().getJournalNo() +"/" + voucher.getSuffix());
					vo.setDateOfEntry(accountingAspectsVo.getAccountingAspectsGeneralInfo().getCreate_ts());
					vo.setEffectiveDate(accountingAspectsVo.getAccountingAspectsGeneralInfo().getDateOfCreation());
					vo.setParticulars(item.getAccountsName());
					vo.setLedgerId(item.getAccountsId()+"");
					if(item.getSubLedgerId()!=null && !item.getSubLedgerId().equals(0)) {
					String subledgerDescriptionDetails = chartOfAccountsDao.getSubLedgerDescription(item.getSubLedgerId(),accountingAspectsVo.getOrganizationId());
					
					logger.info("subledgerDescriptionDetails:::"+subledgerDescriptionDetails);
					String subLedgerDescription = subledgerDescriptionDetails.split("~")[0];
					String subledgerUniqueIdentifier = subledgerDescriptionDetails.split("~")[1];
					logger.info("Subledger Description is ::"+ subLedgerDescription);
					if(subLedgerDescription!=null && subledgerUniqueIdentifier!=null) {
						switch (subLedgerDescription) {
							case JournalEntriesConstants.SUBLEDGER_TYPE_CUSTOMER:
								vo.setCustomerId(Integer.valueOf(subledgerUniqueIdentifier));
								break;
							case JournalEntriesConstants.SUBLEDGER_TYPE_VENDOR:
								vo.setVendorId(Integer.valueOf(subledgerUniqueIdentifier));
								break;
							case JournalEntriesConstants.SUBLEDGER_TYPE_GST:
								vo.setGstLevel(subledgerUniqueIdentifier.split(" ")[0]);
								vo.setGstPercentage(subledgerUniqueIdentifier.split(" ")[1]);
								break;
							case JournalEntriesConstants.SUBLEDGER_TYPE_TDS:
								vo.setTdsId(Integer.valueOf(subledgerUniqueIdentifier));
								break;
							case JournalEntriesConstants.SUBLEDGER_TYPE_EMPLOYEE:
									vo.setEmployeeId(Integer.valueOf(subledgerUniqueIdentifier));
									break;
						}
						if(subLedgerDescription!=null && subLedgerDescription.contains(JournalEntriesConstants.SUBLEDGER_TYPE_BANKING)) {
							logger.info("To the banking ::");
							vo.setBankId(Integer.valueOf(subledgerUniqueIdentifier));
							String bankType = null;
							if("Banking-BankAccount".equals(subLedgerDescription)) {
								bankType = JournalEntriesConstants.BANK_TYPE_BANK_ACCOUNT;
							}
							if("Banking-CurrentAccount".equals(subLedgerDescription)) {
								bankType = JournalEntriesConstants.BANK_TYPE_BANK_ACCOUNT;
							}
							if("Banking-EEFC".equals(subLedgerDescription)) {
								bankType = JournalEntriesConstants.BANK_TYPE_BANK_ACCOUNT;
							}
							if("Banking-Savings".equals(subLedgerDescription)) {
								bankType = JournalEntriesConstants.BANK_TYPE_BANK_ACCOUNT;
							}
							if("Banking-Fixed Deposit".equals(subLedgerDescription)) {
								bankType = JournalEntriesConstants.BANK_TYPE_BANK_ACCOUNT;
							}
							if("Banking-Recurring Deposit".equals(subLedgerDescription)) {
								bankType = JournalEntriesConstants.BANK_TYPE_BANK_ACCOUNT;
							}
							if("Banking-Bank Overdraft".equals(subLedgerDescription)) {
								bankType = JournalEntriesConstants.BANK_TYPE_BANK_ACCOUNT;
							}
							if("Banking-Cash Credit".equals(subLedgerDescription)) {
								bankType = JournalEntriesConstants.BANK_TYPE_BANK_ACCOUNT;
							}
							if("Banking-UnpaidDividend".equals(subLedgerDescription)) {
								bankType = JournalEntriesConstants.BANK_TYPE_BANK_ACCOUNT;
							}
							if("Banking-UnpaidMaturedDeposits".equals(subLedgerDescription)) {
								bankType = JournalEntriesConstants.BANK_TYPE_BANK_ACCOUNT;
							}
							if("Banking-UnpaidMaturedDebentures".equals(subLedgerDescription)) {
								bankType = JournalEntriesConstants.BANK_TYPE_BANK_ACCOUNT;
							}
							if("Banking-ShareAllotmentMoney".equals(subLedgerDescription)) {
								bankType = JournalEntriesConstants.BANK_TYPE_BANK_ACCOUNT;
							}
							if("Banking-MarginMoneyDeposit".equals(subLedgerDescription)) {
								bankType = JournalEntriesConstants.BANK_TYPE_BANK_ACCOUNT;
							}
							if("Banking-FrozenBankAccounts".equals(subLedgerDescription)) {
								bankType = JournalEntriesConstants.BANK_TYPE_BANK_ACCOUNT;
							}
							if("Banking-CreditCard".equals(subLedgerDescription)) {
								bankType = JournalEntriesConstants.BANK_TYPE_CREDIT_CARD;
							}
							if("Banking-Cash".equals(subLedgerDescription)) {
								bankType = JournalEntriesConstants.BANK_TYPE_CASH_ACCOUNT;
							}
							if("Banking-Wallet".equals(subLedgerDescription)) {
								bankType = JournalEntriesConstants.BANK_TYPE_WALLET;		
							}	
							vo.setBankType(bankType);
						}
					}
				}
					vo.setLocationId(accountingAspectsVo.getAccountingAspectsGeneralInfo().getLocationId());
					vo.setAmountDebitOriginalCurrrency(item.getDebits()!=null ? Double.valueOf(item.getDebits()): null);
					vo.setAmountCreditOriginalCurrency(item.getCredits()!=null ? Double.valueOf(item.getCredits()): null);
					vo.setCurrency(item.getCurrencyId());
					vo.setConversionFactor(item.getExchangeRate()!=null ? Double.valueOf(item.getExchangeRate()) : null);
					vo.setAmountCredit(vo.getAmountCreditOriginalCurrency() * vo.getConversionFactor());
					vo.setAmountDebit(vo.getAmountDebitOriginalCurrrency() * vo.getConversionFactor());
					vo.setRemarks(accountingAspectsVo.getAccountingAspectsGeneralInfo().getNotes()!=null && accountingAspectsVo.getAccountingAspectsGeneralInfo().getNotes().length()>50? accountingAspectsVo.getAccountingAspectsGeneralInfo().getNotes().substring(0,50) : accountingAspectsVo.getAccountingAspectsGeneralInfo().getNotes());
					vo.setUserId(accountingAspectsVo.getUserId());
					vo.setOrgId(accountingAspectsVo.getOrganizationId());
					vo.setIsSuperAdmin(accountingAspectsVo.getIsSuperAdmin());
					vo.setRoleName(accountingAspectsVo.getRoleName());
					journalEntriesVos.add(vo);
				}
				logger.info("mapAccountingEntriesToJournalEntriesVos are::" +journalEntriesVos.size());
			}
		} catch (ApplicationException e) {
			logger.info("Error in mapAccountingEntriesToJournalEntriesVo:: ",e);
			throw new ApplicationException(e);
		}
		return journalEntriesVos;
	}
	
	

	private List<JournalEntriesVo> getPaymentsAndGroupByLedgers(Integer paymentId) throws ApplicationException {
		logger.info("To method getPaymentsAndGroupByLedgers with Id" + paymentId);
		 List<JournalEntriesVo> journalEntries = new ArrayList<JournalEntriesVo>();
		try {
			PaymentNonCoreVo  paymentVo = paymentNonCoreDao.getPaymentById(paymentId);
			GeneralLedgerVo glVo = null;
			if(paymentVo.getGeneralLedgerData()!=null ) {
				 glVo =  paymentVo.getGeneralLedgerData();
			}else {
				logger.info("glvo from GL DAO"+glVo);
				glVo = generalLedgersDao.getGeneralLedgers(JournalEntriesConstants.SUB_MODULE_PAYMENTS, paymentVo);
			}
			logger.info("glvo"+glVo);
			String accountType = null;
			if(glVo!=null && glVo.getGlDetails()!=null && glVo.getGlDetails().size()>0 ) {	
			for(GeneralLedgerDetailsVo details :  glVo.getGlDetails()){
				logger.info("glvo Details"+details);
				JournalEntriesVo vo = new JournalEntriesVo();
				vo.setTransactionNo(paymentVo.getId());
				vo.setModule(JournalEntriesConstants.MODULE_AP);
				vo.setSubModule(JournalEntriesConstants.SUB_MODULE_PAYMENTS);
				vo.setVoucherType(JournalEntriesConstants.VOUCHER_TYPE_PAYMENTS );
				vo.setDateOfEntry(paymentVo.getCreateTs());
				vo.setVoucherNo(paymentVo.getPaymentRefNo());
				logger.info("Invoice date::"+ paymentVo.getCreateTs());
				vo.setEffectiveDate(paymentVo.getPaymentDate()!=null ? DateConverter.getInstance().convertStringToDate(paymentVo.getPaymentDate(), "yyyy-MM-dd") : null);
				vo.setParticulars(details.getAccount());
				vo.setLedgerId(details.getAccountId()!=null ? String.valueOf(details.getAccountId()) : "0");
				
				switch(paymentVo.getPaymentType()) {
				case 1: // Bills Invoices Payments Type
					vo.setVendorId(paymentVo.getVendor());
					break;
				case 2: // Vendor Advance Payment type
					vo.setVendorId(paymentVo.getVendor());
					break;
				case 3: // GST Payment type
					vo.setGstLevel(details.getSubLedger());
					break;
				case 4: // TDS Payment type
					int tdsId =  details.getProductId()!=null &&  details.getProductId()!=0 ? details.getProductId() : 0 ;
					vo.setTdsId(tdsId);
					break;
				case 5: // Customer Refunds type
					vo.setCustomerId(paymentVo.getPaidTo()!=null ? Integer.valueOf(paymentVo.getPaidTo()) : 0);
					break;
				case 6: // Other Payments type
					if(JournalEntriesConstants.SUBLEDGER_TYPE_CUSTOMER.equals(details.getDescription())) {
						Integer custId = details.getProductId()!=null && details.getProductId()!=0? details.getProductId() :   customerDao.getCustomerIdByDisplayName(details.getSubLedger(), paymentVo.getOrganizationId());
						vo.setCustomerId(custId);

					}
					if(PaymentNonCoreConstants.STATUTORY_BODY.equals(details.getDescription())) {
					//	Integer custId = details.getProductId()!=null && details.getProductId()!=0? details.getProductId() :   customerDao.getCustomerIdByDisplayName(details.getSubLedger(), paymentVo.getOrganizationId());
					}
					if(JournalEntriesConstants.SUBLEDGER_TYPE_EMPLOYEE.equals(details.getDescription())) {
						int empId =  details.getProductId()!=null &&  details.getProductId()!=0 ? details.getProductId() : 0 ;
						vo.setEmployeeId(empId);
					}
					if(JournalEntriesConstants.SUBLEDGER_TYPE_VENDOR.equals(details.getDescription())) {
						int vendorId = details.getProductId()!=null &&  details.getProductId()!=0 ? details.getProductId() : 0 ;
						vo.setVendorId(vendorId);

					}
					
					break;
				case 7: // Employee Payments type
					
						int empId =  details.getProductId()!=null &&  details.getProductId()!=0 ? details.getProductId()  : 0 ;
						vo.setEmployeeId(empId);
					
					break;
				}
				
				if(JournalEntriesConstants.SUBLEDGER_TYPE_BANKING.equals(details.getType())) {
					switch(paymentVo.getPaymentMode()) {
					case 1: //Cash type Payment Mode 
						accountType = JournalEntriesConstants.BANK_TYPE_CASH_ACCOUNT;
						break;
					case 2: //Bank Transfer Payment mode
						accountType = JournalEntriesConstants.BANK_TYPE_BANK_ACCOUNT;
						break;
					case 3: // Cheque&Demand Draft Payment Mode
						accountType = JournalEntriesConstants.BANK_TYPE_BANK_ACCOUNT;
						break;
					case 4: //Wallet Transfer Payment mode
						accountType = JournalEntriesConstants.BANK_TYPE_WALLET;
						break;
					case 5: // Credit card Payment Mode
						accountType = JournalEntriesConstants.BANK_TYPE_CREDIT_CARD;
						break;
					}
					logger.info("AccountType :: "+ accountType);
				}
				vo.setBankType(accountType);
				vo.setBankId(paymentVo.getPaidVia());
				vo.setCurrency(paymentVo.getCurrency());
				vo.setConversionFactor(paymentVo.getExchangeRate()!=null  && !paymentVo.getExchangeRate().isEmpty()? Double.valueOf(paymentVo.getExchangeRate()) : 1.00);
				vo.setAmountCreditOriginalCurrency(details.getFcyCredit()!=null ? details.getFcyCredit() : 0.00);
				vo.setAmountCredit(details.getInrCredit()!=null ? details.getInrCredit() : 0.00);
				vo.setAmountDebitOriginalCurrrency(details.getFcyDebit()!=null ? details.getFcyDebit() : 0.00);
				vo.setAmountDebit(details.getInrDebit()!=null ? details.getInrDebit() : 0.00);
				vo.setRemarks(glVo.getVoucherNaration()!=null ? glVo.getVoucherNaration() : null);
				vo.setUserId(paymentVo.getUserId());
				vo.setOrgId(paymentVo.getOrganizationId());
				vo.setRoleName(paymentVo.getRoleName());
				journalEntries.add(vo);
			}
			logger.info("Journal entries size :: "+ journalEntries.size());
			}
		}catch (ApplicationException e) {
			logger.info("Error in getInvoiceAndGroupByLedgers:: ",e);
			throw new ApplicationException(e);
		}
		return journalEntries;
	
	}

	
	
	
	private List<JournalEntriesVo> getARReceiptsAndMapToJournalEntries(Integer receiptId) throws ApplicationException{
		logger.info("To method getARReceiptsAndMapToJournalEntries with Id" + receiptId);
		 List<JournalEntriesVo> journalEntries = new ArrayList<JournalEntriesVo>();
		try {
			ReceiptVo  receiptVo = receiptDao.getReceiptById(receiptId);
			GeneralLedgerVo glVo = null;
			if(receiptVo.getGeneralLedgerData()!=null ) {
				 glVo =  receiptVo.getGeneralLedgerData();
			}else {
				logger.info("glvo from GL DAO"+glVo);
				glVo = generalLedgersDao.getGeneralLedgers(JournalEntriesConstants.SUB_MODULE_RECEIPTS, receiptVo);
			}
			logger.info("glvo"+glVo);
			if(receiptVo!=null && glVo !=null && glVo.getGlDetails()!=null ) {	
				for(GeneralLedgerDetailsVo details : glVo.getGlDetails()) {
					JournalEntriesVo vo = new JournalEntriesVo();
					vo.setTransactionNo(receiptVo.getId());
					vo.setModule(JournalEntriesConstants.MODULE_AR);
					vo.setSubModule(JournalEntriesConstants.SUB_MODULE_RECEIPTS);
					vo.setVoucherType(JournalEntriesConstants.VOUCHER_TYPE_ACCOUNTS_RECEIVABLES_RECEIPTS );
					vo.setDateOfEntry(new Date(receiptVo.getCreateTs().getTime()));
					logger.info("Receipt date::"+ receiptVo.getReceiptDate());
					vo.setEffectiveDate( receiptVo.getReceiptDate()!=null ? Date.valueOf( receiptVo.getReceiptDate()) : null);
					vo.setVoucherNo(receiptVo.getReceiptNoPrefix()+"/"+receiptVo.getReceiptNo()+"/"+receiptVo.getReceiptNoSuffix());
					vo.setParticulars(details.getAccount());
					vo.setLedgerId(String.valueOf(details.getAccountId()));
					vo.setCustomerId(receiptVo.getCustomerId());
					if((JournalEntriesConstants.SUB_MODULE_CREDIT_NOTES).equals(details.getDescription())) {
						vo.setCreditNoteNo(details.getSubLedger());
					}else {
						if(details.getProductId()!=null && details.getProductId() > 0 && !JournalEntriesConstants.ADVANCE.equals(details.getType())) {
							vo.setCustomerInvoiceNo(details.getSubLedger());
						}
					}
					vo.setReciptType(receiptVo.getReceiptType());
					vo.setVendorId(receiptVo.getVendorId());
					if(receiptVo.getReceiptType().equals(ReceiptConstants.RECEIPT_TYPE_VENDOR_REFUNDS)) {
						vo.setVendorInvoiceNo(details.getSubLedger());
					}
					vo.setBankId(Integer.valueOf(receiptVo.getBankId()));
					vo.setBankType(receiptVo.getBankType());
					vo.setCurrency(receiptVo.getCurrencyId());
					vo.setConversionFactor(receiptVo.getExchangeRate()!=null && !receiptVo.getExchangeRate().isEmpty() ? Double.valueOf(receiptVo.getExchangeRate()) : 1.00);
					vo.setAmountCreditOriginalCurrency(details.getFcyCredit()!=null ? details.getFcyCredit() : 0.00);
					vo.setAmountCredit(details.getInrCredit()!=null ? details.getInrCredit() : 0.00);
					vo.setAmountDebitOriginalCurrrency(details.getFcyDebit()!=null ? details.getFcyDebit() : 0.00);
					vo.setAmountDebit(details.getInrDebit()!=null ? details.getInrDebit() : 0.00);
					vo.setRemarks(glVo.getVoucherNaration()!=null ? glVo.getVoucherNaration() : null);
					vo.setUserId(receiptVo.getUserId());
					vo.setOrgId(receiptVo.getOrganizationId());
					vo.setRoleName(receiptVo.getRoleName());
					journalEntries.add(vo);
					}

			}

//					String vendorLedger = chartOfAccountsDao.getLedgerName(receiptVo.getVendorLedgerId(), receiptVo.getOrganizationId());
//					vo.setParticulars(vendorLedger);
//					vo.setVendorId((receiptVo.getVendorId()!=null ? receiptVo.getVendorId() : 0));
//					vo.setVendorInvoiceNo(vendorInvoice.getInvoiceRefNo());
//					vo.setLocationId(receiptVo.getLocationId() );
//					vo.setCurrency(receiptVo.getCurrencyId());
//					vo.setAmountCreditOriginalCurrency(Double.valueOf(vendorInvoice.getRefundAmount()));
//					vo.setLedgerId(receiptVo.getVendorLedgerId()!=null ? String.valueOf(receiptVo.getVendorLedgerId()) : null);
//					vo.setConversionFactor(receiptVo.getExchangeRate()!=null ? Double.valueOf(receiptVo.getExchangeRate()): 1.00);
//					vo.setAmountCredit(vo.getAmountCreditOriginalCurrency() * vo.getConversionFactor());
//					vo.setUserId(receiptVo.getUserId());
//					vo.setOrgId(receiptVo.getOrganizationId());
//					vo.setIsSuperAdmin(receiptVo.getIsSuperAdmin());
//					vo.setAmountDebitOriginalCurrrency(0.00);
//					vo.setAmountDebit(0.00);
//					journalEntries.add(vo);
		
		
		
//		List<JournalEntriesVo> journalEntries = new ArrayList<JournalEntriesVo>();
//		try {
//			ReceiptVo receiptVo = receiptDao.getReceiptById(receiptId);
//			// For receipt type customer payments 
//			if(receiptVo!=null && receiptVo.getReceiptType()!=null && ReceiptConstants.AR_RECEIPT_TYPE_CUSTOMER_PAYMENTS.equals(receiptVo.getReceiptType())) {
//			//To get the bank ledger 
//			if(receiptVo.getBankId()!=null && receiptVo.getBankType()!=null && receiptVo.getAmountReceived()!=null) {
//				String bankLedger = getLedgerForBankType(receiptVo.getBankType(), Integer.valueOf(receiptVo.getBankId()));
//				logger.info("bank ledger ::"+bankLedger);
//				  	JournalEntriesVo vo = new JournalEntriesVo();
//					vo.setTransactionNo(receiptVo.getId());
//					vo.setModule(JournalEntriesConstants.MODULE_AR);
//					vo.setSubModule(JournalEntriesConstants.SUB_MODULE_RECEIPTS);
//					vo.setVoucherType(JournalEntriesConstants.VOUCHER_TYPE_ACCOUNTS_RECEIVABLES_RECEIPTS );
//					vo.setDateOfEntry(new Date(receiptVo.getCreateTs().getTime()));
//					logger.info("Receipt date::"+ receiptVo.getReceiptDate());
//					vo.setEffectiveDate( receiptVo.getReceiptDate()!=null ? Date.valueOf( receiptVo.getReceiptDate()) : null);
//					vo.setVoucherNo(receiptVo.getReceiptNo().getPrefix()+"/"+receiptVo.getReceiptNo().getMinimumDigits()+"/"+receiptVo.getReceiptNo().getSuffix());
//					Integer ledgerId = chartOfAccountsDao.getLedgerIdGivenName(bankLedger, receiptVo.getOrganizationId());
//					vo.setLedgerId(ledgerId+"");
//					vo.setParticulars(bankLedger);
//					vo.setCustomerId(receiptVo.getCustomerId()!=null ? Integer.valueOf(receiptVo.getCustomerId()) : 0);
//					//vo.setLocationId(receiptVo.getLocationId() );
//					vo.setCurrency(receiptVo.getCurrencyId());
//					vo.setAmountDebitOriginalCurrrency(receiptVo.getAmount()!=null ? Double.valueOf(receiptVo.getAmount()): 0.00);
//					//vo.setConversionFactor(receiptVo.getExchangeRate()!=null ? Double.valueOf(receiptVo.getExchangeRate()): 1.0);
//					vo.setAmountDebit(vo.getAmountDebitOriginalCurrrency() * vo.getConversionFactor());
//					vo.setUserId(receiptVo.getUserId());
//					vo.setOrgId(receiptVo.getOrganizationId());
//					//vo.setIsSuperAdmin(receiptVo.getIsSuperAdmin());
//					vo.setBankId(receiptVo.getBankId()!=null ? Integer.valueOf(receiptVo.getBankId()): 0);
//					vo.setBankType(receiptVo.getBankType());
//					vo.setAmountCredit(0.00);
//					vo.setAmountCreditOriginalCurrency(0.00);
//					journalEntries.add(vo);
//					
//			}
//			if(receiptVo.getInvoiceCategorys()!=null && receiptVo.getCustomTableList()!=null) {
//				
//				for(ReceiptBulkDetailsVo categoryVo : receiptVo.getInvoiceCategorys() ) {
//					String invoiceNo = null;
//					Map<String ,String> debitMap = new HashMap<String, String>();
//					Map<String ,String> creditMap = new HashMap<String, String>();
//					if(categoryVo.getInvoiceAmount()!=null && receiptVo.getInvoiceAmountLedgerName()!=null && !receiptVo.getInvoiceAmountLedgerName().isEmpty()) {
//						if("Invoice".equals(categoryVo.getInvoiceOnAccountType())) {
//							BasicInvoiceDetailsVo basicInvoiceVo = receiptVo.getDropDownInvoices().stream().filter(vo -> "Invoice".equals(vo.getType())).findAny().orElse(null);
//							invoiceNo = basicInvoiceVo.getName();
//						}
//						}
//					logger.info("Invoice Number ::"+ invoiceNo);
//						Double invoiceAmnt = Double.valueOf(categoryVo.getInvoiceAmount());
//						
//						String  invoiceLedger = chartOfAccountsDao.getLedgerName(Integer.valueOf(receiptVo.getInvoiceAmountLedgerId()), receiptVo.getOrganizationId());
//						logger.info("invoice ledger::"+invoiceLedger);
//						if(invoiceAmnt>0) {
//							creditMap.put(invoiceLedger, invoiceAmnt+"~"+receiptVo.getInvoiceAmountLedgerId());
//						}
//						if(invoiceAmnt<0) {
//							debitMap.put(invoiceLedger, invoiceAmnt+"~"+receiptVo.getInvoiceAmountLedgerId());
//						}
//					for (ReceiptSettingsVo categoryTable : receiptVo.getCustomTableList()) {
//						if ("bankCharges".equals(categoryTable.getcName()) && categoryTable.isColumnShow()
//								&& categoryVo.getBankCharges() != null && !categoryVo.getBankCharges().isEmpty()) {
//							String bankChargesLedger = categoryTable.getLedgerName();
//							logger.info("bank chargers ledger::" + bankChargesLedger);
//							Double bankChargesAmount = Double.valueOf(categoryVo.getBankCharges());
//							if (bankChargesAmount > 0) {
//								creditMap.put(bankChargesLedger, bankChargesAmount+"~"+categoryTable.getLedgerId());
//							}
//							if (bankChargesAmount < 0) {
//								Double amnt = bankChargesAmount * -1;
//								debitMap.put(bankChargesLedger, amnt+"~"+categoryTable.getLedgerId() );
//							}
//						}
//						if ("tdsDeducted".equals(categoryTable.getcName()) && categoryTable.isColumnShow()
//								&& categoryVo.getTdsDeducted() != null && !categoryVo.getTdsDeducted().isEmpty()) {
//							String tdsLedger = categoryTable.getLedgerName();
//							logger.info("TDS ledger::" + tdsLedger);
//							Double tdsAmount = Double.valueOf(categoryVo.getTdsDeducted());
//							if (tdsAmount > 0) {
//								creditMap.put(tdsLedger, tdsAmount+"~"+categoryTable.getLedgerId());
//							}
//							if (tdsAmount < 0) {
//								Double amnt =  tdsAmount * -1;
//								debitMap.put(tdsLedger,amnt+"~"+categoryTable.getLedgerId());
//							}
//						}
//
//						if ("others1".equals(categoryTable.getcName()) && categoryTable.isColumnShow()
//								&& categoryVo.getOthers1() != null &&  !categoryVo.getOthers1().isEmpty()) {
//							String others1Ledger = categoryTable.getLedgerName();
//							logger.info("others 1 ledger::" + others1Ledger);
//							Double others1Amount = Double.valueOf(categoryVo.getOthers1());
//							if (others1Amount > 0) {
//								creditMap.put(others1Ledger, others1Amount+"~"+categoryTable.getLedgerId());
//							}
//							if (others1Amount < 0) {
//								Double amnt = others1Amount * -1; 
//								debitMap.put(others1Ledger, amnt +"~"+categoryTable.getLedgerId());
//							}
//						}
//
//						if ("others2".equals(categoryTable.getcName()) && categoryTable.isColumnShow()
//								&& categoryVo.getOthers2() != null && !categoryVo.getOthers2().isEmpty()) {
//							String others2Ledger =categoryTable.getLedgerName();
//							logger.info("others 2 ledger::" + others2Ledger);
//							Double others2Amount = Double.valueOf(categoryVo.getOthers2());
//							if (others2Amount > 0) {
//								creditMap.put(others2Ledger, others2Amount+"~"+categoryTable.getLedgerId());
//							}
//							if (others2Amount < 0) {
//								Double amnt = others2Amount * -1;
//								debitMap.put(others2Ledger, amnt+"~"+categoryTable.getLedgerId());
//							}
//						}
//
//						if ("others3".equals(categoryTable.getcName()) && categoryTable.isColumnShow()
//								&& categoryVo.getOthers3() != null && !categoryVo.getOthers3().isEmpty()) {
//							String others3Ledger = categoryTable.getLedgerName();
//							logger.info("others 3 ledger::" + others3Ledger);
//							Double others3Amount = Double.valueOf(categoryVo.getOthers2());
//							if (others3Amount > 0) {
//								creditMap.put(others3Ledger, others3Amount+"~"+categoryTable.getLedgerId());
//							}
//							if (others3Amount < 0) {
//								Double amnt = others3Amount * -1;
//								debitMap.put(others3Ledger, amnt+"~"+categoryTable.getLedgerId());
//							}
//						}
//					}
//				logger.info("Debitmap::"+debitMap);
//				logger.info("Creditmap::"+creditMap);
//				
//				// To form the Journal entries
//				final String invNo = invoiceNo;
//				if(creditMap.size()>0  ) {
//					creditMap.forEach((key , value) ->{
//						JournalEntriesVo vo = new JournalEntriesVo();
//						vo.setTransactionNo(receiptVo.getId());
//						vo.setModule(JournalEntriesConstants.MODULE_AR);
//						vo.setSubModule(JournalEntriesConstants.SUB_MODULE_RECEIPTS);
//						vo.setVoucherType(JournalEntriesConstants.VOUCHER_TYPE_ACCOUNTS_RECEIVABLES_RECEIPTS );
//						vo.setDateOfEntry(new Date(receiptVo.getCreateTs().getTime()));
//						logger.info("Receipt date::"+ receiptVo.getReceiptDate());
//						vo.setEffectiveDate( receiptVo.getReceiptDate()!=null ? Date.valueOf( receiptVo.getReceiptDate()) : null);
//						vo.setVoucherNo(receiptVo.getReceiptNo().getPrefix()+"/"+receiptVo.getReceiptNo().getMinimumDigits()+"/"+receiptVo.getReceiptNo().getSuffix());
//						vo.setParticulars(key);
//						vo.setCustomerId(receiptVo.getCustomerId()!=null ? Integer.valueOf(receiptVo.getCustomerId()): 0);
//						vo.setCustomerInvoiceNo(invNo);
//						vo.setLocationId(receiptVo.getLocationId() );
//						vo.setCurrency(receiptVo.getCurrencyId());
//						String amnt = value.split("~")[0]!=null ? value.split("~")[0] : "0.00";
//						vo.setAmountCreditOriginalCurrency(Double.valueOf(amnt));
//						vo.setLedgerId(value.split("~")[1]);
//						vo.setConversionFactor(receiptVo.getExchangeRate()!=null ? Double.valueOf(receiptVo.getExchangeRate()): 1.00);
//						vo.setAmountCredit(vo.getAmountCreditOriginalCurrency() * vo.getConversionFactor());
//						vo.setUserId(receiptVo.getUserId());
//						vo.setOrgId(receiptVo.getOrganizationId());
//						vo.setIsSuperAdmin(receiptVo.getIsSuperAdmin());
//						vo.setAmountDebitOriginalCurrrency(0.00);
//						vo.setAmountDebit(0.00);
//						journalEntries.add(vo);
//					});
//					
//				}
//				if(debitMap.size()>0) {
//					debitMap.forEach((key , value) ->{
//						JournalEntriesVo vo = new JournalEntriesVo();
//						vo.setTransactionNo(receiptVo.getId());
//						vo.setModule(JournalEntriesConstants.MODULE_AR);
//						vo.setSubModule(JournalEntriesConstants.SUB_MODULE_RECEIPTS);
//						vo.setVoucherType(JournalEntriesConstants.VOUCHER_TYPE_ACCOUNTS_RECEIVABLES_RECEIPTS );
//						vo.setDateOfEntry(new Date(receiptVo.getCreateTs().getTime()));
//						logger.info("Receipt date::"+ receiptVo.getReceiptDate());
//						vo.setEffectiveDate( receiptVo.getReceiptDate()!=null ? Date.valueOf( receiptVo.getReceiptDate()) : null);
//						vo.setVoucherNo(receiptVo.getReceiptNo().getPrefix()+"/"+receiptVo.getReceiptNo().getMinimumDigits()+"/"+receiptVo.getReceiptNo().getSuffix());
//						vo.setParticulars(key);
//						vo.setCustomerId(receiptVo.getCustomerId()!=null ? Integer.valueOf(receiptVo.getCustomerId()): 0);
//						vo.setCustomerInvoiceNo(invNo);
//						vo.setLocationId(receiptVo.getLocationId() );
//						vo.setCurrency(receiptVo.getCurrencyId());
//						String amnt = value.split("~")[0]!=null ? value.split("~")[0] : "0.00";
//						vo.setLedgerId(value.split("~")[1]);
//						vo.setAmountDebitOriginalCurrrency(Double.valueOf(amnt));
//						vo.setConversionFactor(receiptVo.getExchangeRate()!=null ? Double.valueOf(receiptVo.getExchangeRate()): 1.00);
//						vo.setAmountDebit(vo.getAmountDebitOriginalCurrrency() * vo.getConversionFactor());
//						vo.setUserId(receiptVo.getUserId());
//						vo.setOrgId(receiptVo.getOrganizationId());
//						vo.setIsSuperAdmin(receiptVo.getIsSuperAdmin());
//						vo.setAmountCreditOriginalCurrency(0.00);
//						vo.setAmountCredit(0.00);
//						journalEntries.add(vo);
//					});
//					logger.info("Journal entries size :: "+ journalEntries.size());
//				}
//			}
//			}
//			}
//			
//			//if receipt type other receipts 
//			if(receiptVo!=null && receiptVo.getReceiptType()!=null && ReceiptConstants.AR_RECEIPT_TYPE_OTHER_RECEIPTS.equals(receiptVo.getReceiptType())) {
//				//To get the bank ledger 
//				if(receiptVo.getBankId()!=null && receiptVo.getBankType()!=null && receiptVo.getAmountReceived()!=null) {
//					String bankLedger = getLedgerForBankType(receiptVo.getBankType(), Integer.valueOf(receiptVo.getBankId()));
//					logger.info("bank ledger ::"+bankLedger);
//					  	JournalEntriesVo vo = new JournalEntriesVo();
//						vo.setTransactionNo(receiptVo.getId());
//						vo.setModule(JournalEntriesConstants.MODULE_AR);
//						vo.setSubModule(JournalEntriesConstants.SUB_MODULE_RECEIPTS);
//						vo.setVoucherType(JournalEntriesConstants.VOUCHER_TYPE_ACCOUNTS_RECEIVABLES_RECEIPTS );
//						vo.setDateOfEntry(new Date(receiptVo.getCreateTs().getTime()));
//						logger.info("Receipt date::"+ receiptVo.getReceiptDate());
//						vo.setEffectiveDate( receiptVo.getReceiptDate()!=null ? Date.valueOf( receiptVo.getReceiptDate()) : null);
//						vo.setVoucherNo(receiptVo.getReceiptNo().getPrefix()+"/"+receiptVo.getReceiptNo().getMinimumDigits()+"/"+receiptVo.getReceiptNo().getSuffix());
//						Integer ledgerId = chartOfAccountsDao.getLedgerIdGivenName(bankLedger, receiptVo.getOrganizationId());
//						vo.setLedgerId(ledgerId+"");
//						vo.setParticulars(bankLedger);
//						vo.setCustomerId(receiptVo.getCustomerId()!=null ? Integer.valueOf(receiptVo.getCustomerId()) : 0);
//						vo.setLocationId(receiptVo.getLocationId() );
//						vo.setCurrency(receiptVo.getCurrencyId());
//						vo.setAmountDebitOriginalCurrrency(receiptVo.getAmountReceived()!=null ? Double.valueOf(receiptVo.getAmountReceived()): 0.00);
//						vo.setConversionFactor(receiptVo.getExchangeRate()!=null ? Double.valueOf(receiptVo.getExchangeRate()): 1.0);
//						vo.setAmountDebit(vo.getAmountDebitOriginalCurrrency() * vo.getConversionFactor());
//						vo.setUserId(receiptVo.getUserId());
//						vo.setOrgId(receiptVo.getOrganizationId());
//						vo.setIsSuperAdmin(receiptVo.getIsSuperAdmin());
//						vo.setBankId(receiptVo.getBankId()!=null ? Integer.valueOf(receiptVo.getBankId()): 0);
//						vo.setBankType(receiptVo.getBankType());
//						vo.setAmountCredit(0.00);
//						vo.setAmountCreditOriginalCurrency(0.00);
//						journalEntries.add(vo);
//				}
//				
//				if(receiptVo.getCustomerId()!=null && !"".equals(receiptVo.getCustomerId()) && !"0".equals(receiptVo.getCustomerId()) && receiptVo.getAmountReceived()!=null && receiptVo.getContactLedgerId()!=null ) {
//					Double otherReceiptAmnt = Double.valueOf(receiptVo.getAmountReceived());
//					
//					String  customerLedger = chartOfAccountsDao.getLedgerName(Integer.valueOf(receiptVo.getContactLedgerId()), receiptVo.getOrganizationId());
//					logger.info("invoice ledger::"+customerLedger);
//							JournalEntriesVo vo = new JournalEntriesVo();
//							vo.setTransactionNo(receiptVo.getId());
//							vo.setModule(JournalEntriesConstants.MODULE_AR);
//							vo.setSubModule(JournalEntriesConstants.SUB_MODULE_RECEIPTS);
//							vo.setVoucherType(JournalEntriesConstants.VOUCHER_TYPE_ACCOUNTS_RECEIVABLES_RECEIPTS );
//							vo.setDateOfEntry(new Date(receiptVo.getCreateTs().getTime()));
//							logger.info("Receipt date::"+ receiptVo.getReceiptDate());
//							vo.setEffectiveDate( receiptVo.getReceiptDate()!=null ? Date.valueOf( receiptVo.getReceiptDate()) : null);
//							vo.setVoucherNo(receiptVo.getReceiptNo().getPrefix()+"/"+receiptVo.getReceiptNo().getMinimumDigits()+"/"+receiptVo.getReceiptNo().getSuffix());
//							vo.setParticulars(customerLedger);
//							vo.setCustomerId(receiptVo.getCustomerId()!=null ? Integer.valueOf(receiptVo.getCustomerId()): 0);
//							vo.setLocationId(receiptVo.getLocationId() );
//							vo.setCurrency(receiptVo.getCurrencyId());
//							vo.setAmountCreditOriginalCurrency(otherReceiptAmnt);
//							vo.setLedgerId(receiptVo.getContactLedgerId());
//							vo.setConversionFactor(receiptVo.getExchangeRate()!=null ? Double.valueOf(receiptVo.getExchangeRate()): 1.00);
//							vo.setAmountCredit(vo.getAmountCreditOriginalCurrency() * vo.getConversionFactor());
//							vo.setUserId(receiptVo.getUserId());
//							vo.setOrgId(receiptVo.getOrganizationId());
//							vo.setIsSuperAdmin(receiptVo.getIsSuperAdmin());
//							vo.setAmountDebitOriginalCurrrency(0.00);
//							vo.setAmountDebit(0.00);
//							journalEntries.add(vo);
//					}
//				}
//			
//			
//			if(receiptVo!=null && receiptVo.getReceiptType()!=null && ReceiptConstants.AR_RECEIPT_TYPE_VENDOR_REFUNDS.equals(receiptVo.getReceiptType())) {
//				//To get the bank ledger 
//				if(receiptVo.getBankId()!=null && receiptVo.getBankType()!=null && receiptVo.getAmountReceived()!=null) {
//					String bankLedger = getLedgerForBankType(receiptVo.getBankType(), Integer.valueOf(receiptVo.getBankId()));
//					logger.info("bank ledger ::"+bankLedger);
//					  	JournalEntriesVo vo = new JournalEntriesVo();
//						vo.setTransactionNo(receiptVo.getId());
//						vo.setModule(JournalEntriesConstants.MODULE_AR);
//						vo.setSubModule(JournalEntriesConstants.SUB_MODULE_RECEIPTS);
//						vo.setVoucherType(JournalEntriesConstants.VOUCHER_TYPE_ACCOUNTS_RECEIVABLES_RECEIPTS );
//						vo.setDateOfEntry(new Date(receiptVo.getCreateTs().getTime()));
//						logger.info("Receipt date::"+ receiptVo.getReceiptDate());
//						vo.setEffectiveDate( receiptVo.getReceiptDate()!=null ? Date.valueOf( receiptVo.getReceiptDate()) : null);
//						vo.setVoucherNo(receiptVo.getReceiptNo().getPrefix()+"/"+receiptVo.getReceiptNo().getMinimumDigits()+"/"+receiptVo.getReceiptNo().getSuffix());
//						Integer ledgerId = chartOfAccountsDao.getLedgerIdGivenName(bankLedger, receiptVo.getOrganizationId());
//						vo.setLedgerId(ledgerId+"");
//						vo.setParticulars(bankLedger);
//						vo.setCustomerId(receiptVo.getCustomerId()!=null ? Integer.valueOf(receiptVo.getCustomerId()) : 0);
//						vo.setLocationId(receiptVo.getLocationId() );
//						vo.setCurrency(receiptVo.getCurrencyId());
//						vo.setAmountDebitOriginalCurrrency(receiptVo.getAmountReceived()!=null ? Double.valueOf(receiptVo.getAmountReceived()): 0.00);
//						vo.setConversionFactor(receiptVo.getExchangeRate()!=null ? Double.valueOf(receiptVo.getExchangeRate()): 1.0);
//						vo.setAmountDebit(vo.getAmountDebitOriginalCurrrency() * vo.getConversionFactor());
//						vo.setUserId(receiptVo.getUserId());
//						vo.setOrgId(receiptVo.getOrganizationId());
//						vo.setIsSuperAdmin(receiptVo.getIsSuperAdmin());
//						vo.setBankId(receiptVo.getBankId()!=null ? Integer.valueOf(receiptVo.getBankId()): 0);
//						vo.setBankType(receiptVo.getBankType());
//						vo.setAmountCredit(0.00);
//						vo.setAmountCreditOriginalCurrency(0.00);
//						journalEntries.add(vo);
//				}
//				
//				if(receiptVo.getVendorReceiptDetails()!=null && !receiptVo.getVendorReceiptDetails().isEmpty()) {
//					for(VendorRefundReceiptDetailsVo vendorInvoice : receiptVo.getVendorReceiptDetails()) {
//						JournalEntriesVo vo = new JournalEntriesVo();
//						vo.setTransactionNo(receiptVo.getId());
//						vo.setModule(JournalEntriesConstants.MODULE_AR);
//						vo.setSubModule(JournalEntriesConstants.SUB_MODULE_RECEIPTS);
//						vo.setVoucherType(JournalEntriesConstants.VOUCHER_TYPE_ACCOUNTS_RECEIVABLES_RECEIPTS );
//						vo.setDateOfEntry(new Date(receiptVo.getCreateTs().getTime()));
//						logger.info("Receipt date::"+ receiptVo.getReceiptDate());
//						vo.setEffectiveDate( receiptVo.getReceiptDate()!=null ? Date.valueOf( receiptVo.getReceiptDate()) : null);
//						vo.setVoucherNo(receiptVo.getReceiptNo().getPrefix()+"/"+receiptVo.getReceiptNo().getMinimumDigits()+"/"+receiptVo.getReceiptNo().getSuffix());
//						String vendorLedger = chartOfAccountsDao.getLedgerName(receiptVo.getVendorLedgerId(), receiptVo.getOrganizationId());
//						vo.setParticulars(vendorLedger);
//						vo.setVendorId((receiptVo.getVendorId()!=null ? receiptVo.getVendorId() : 0));
//						vo.setVendorInvoiceNo(vendorInvoice.getInvoiceRefNo());
//						vo.setLocationId(receiptVo.getLocationId() );
//						vo.setCurrency(receiptVo.getCurrencyId());
//						vo.setAmountCreditOriginalCurrency(Double.valueOf(vendorInvoice.getRefundAmount()));
//						vo.setLedgerId(receiptVo.getVendorLedgerId()!=null ? String.valueOf(receiptVo.getVendorLedgerId()) : null);
//						vo.setConversionFactor(receiptVo.getExchangeRate()!=null ? Double.valueOf(receiptVo.getExchangeRate()): 1.00);
//						vo.setAmountCredit(vo.getAmountCreditOriginalCurrency() * vo.getConversionFactor());
//						vo.setUserId(receiptVo.getUserId());
//						vo.setOrgId(receiptVo.getOrganizationId());
//						vo.setIsSuperAdmin(receiptVo.getIsSuperAdmin());
//						vo.setAmountDebitOriginalCurrrency(0.00);
//						vo.setAmountDebit(0.00);
//						journalEntries.add(vo);
//					}
//				}else {
//					logger.info("in else part");
//					JournalEntriesVo vo = new JournalEntriesVo();
//					vo.setTransactionNo(receiptVo.getId());
//					vo.setModule(JournalEntriesConstants.MODULE_AR);
//					vo.setSubModule(JournalEntriesConstants.SUB_MODULE_RECEIPTS);
//					vo.setVoucherType(JournalEntriesConstants.VOUCHER_TYPE_ACCOUNTS_RECEIVABLES_RECEIPTS );
//					vo.setDateOfEntry(new Date(receiptVo.getCreateTs().getTime()));
//					logger.info("Receipt date::"+ receiptVo.getReceiptDate());
//					vo.setEffectiveDate( receiptVo.getReceiptDate()!=null ? Date.valueOf( receiptVo.getReceiptDate()) : null);
//					vo.setVoucherNo(receiptVo.getReceiptNo().getPrefix()+"/"+receiptVo.getReceiptNo().getMinimumDigits()+"/"+receiptVo.getReceiptNo().getSuffix());
//					String vendorLedger = chartOfAccountsDao.getLedgerName(receiptVo.getVendorLedgerId(), receiptVo.getOrganizationId());
//					vo.setParticulars(vendorLedger);
//					vo.setVendorId((receiptVo.getVendorId()!=null ? receiptVo.getVendorId() : 0));
//					vo.setLocationId(receiptVo.getLocationId() );
//					vo.setCurrency(receiptVo.getCurrencyId());
//					vo.setAmountCreditOriginalCurrency(Double.valueOf(receiptVo.getAmountReceived()));
//					vo.setLedgerId(receiptVo.getVendorLedgerId()!=null ? String.valueOf(receiptVo.getVendorLedgerId()) : null);
//					vo.setConversionFactor(receiptVo.getExchangeRate()!=null ? Double.valueOf(receiptVo.getExchangeRate()): 1.00);
//					vo.setAmountCredit(vo.getAmountCreditOriginalCurrency() * vo.getConversionFactor());
//					vo.setUserId(receiptVo.getUserId());
//					vo.setOrgId(receiptVo.getOrganizationId());
//					vo.setIsSuperAdmin(receiptVo.getIsSuperAdmin());
//					vo.setAmountDebitOriginalCurrrency(0.00);
//					vo.setAmountDebit(0.00);
//					journalEntries.add(vo);
//
//				}
//			}
//			
		}catch (ApplicationException e) {
			logger.info("Error in getARReceiptsAndMapToJournalEntries:: ",e);
		throw new ApplicationException(e);
	}
		return journalEntries;
	}
	
	// This is a common insert method 
	 public void insertJournalEntries(List<JournalEntriesVo> journalEntries) throws ApplicationException {
		 if(journalEntries!=null && !journalEntries.isEmpty()) {
		 try(Connection con = getJournalTransactionConnection();PreparedStatement preparedStatement = con.prepareStatement(JournalEntriesConstants.INSERT_JOURNAL_ENTRIES,Statement.RETURN_GENERATED_KEYS)){
			int counter = 1;
			for(JournalEntriesVo vo : journalEntries) {
				logger.info("vo"+vo);
				logger.info("To add next entry with counter :: "+ counter);
				preparedStatement.setInt(1, vo.getTransactionNo());
				preparedStatement.setInt(2, vo.getOriginalTransactionNo()!=null ? vo.getOriginalTransactionNo() : 0 );
				preparedStatement.setInt(3, counter);
				preparedStatement.setString(4, vo.getModule());
				preparedStatement.setString(5, vo.getSubModule());
				preparedStatement.setString(6, vo.getVoucherType());
				preparedStatement.setString(7, vo.getVoucherNo()!=null ?vo.getVoucherNo() : null );
				preparedStatement.setDate(8, vo.getDateOfEntry()!= null ? vo.getDateOfEntry() : null);
				preparedStatement.setDate(9, vo.getEffectiveDate()!=null ? vo.getEffectiveDate() : null);
				preparedStatement.setDate(10, vo.getInvoiceDate()!=null ? vo.getEffectiveDate() : null);
				preparedStatement.setString(11, vo.getParticulars()!=null ?  vo.getParticulars() : null);
				preparedStatement.setInt(12, vo.getCustomerId()!=null? vo.getCustomerId() : 0);
				preparedStatement.setString(13, vo.getCreditNoteNo()!=null ? vo.getCreditNoteNo() : null);
				preparedStatement.setString(14, vo.getCustomerInvoiceNo()!=null ? vo.getCustomerInvoiceNo() : null);
				preparedStatement.setString(15, vo.getCustomerPoNo()!=null ? vo.getCustomerPoNo() : null);
				preparedStatement.setInt(16, vo.getVendorId()!=null ? vo.getVendorId() : 0);
				preparedStatement.setString(17, vo.getDebitNoteNo()!=null ?  vo.getDebitNoteNo() : null);
				preparedStatement.setString(18, vo.getVendorInvoiceNo()!=null ? vo.getVendorInvoiceNo() : null);
				preparedStatement.setString(19, vo.getVendorPoNo()!=null ? vo.getVendorPoNo() : null);
				preparedStatement.setInt(20, vo.getBankId()!=null ? vo.getBankId() : 0);
				preparedStatement.setString(21, vo.getGstLevel()!=null ? vo.getGstLevel() : null);
				preparedStatement.setString(22, vo.getGstPercentage()!=null ? vo.getGstPercentage() : null);
				preparedStatement.setInt(23, vo.getTdsId()!=null ? vo.getTdsId() : 0);
				preparedStatement.setInt(24, vo.getEmployeeId()!=null ? vo.getEmployeeId() : 0);
				preparedStatement.setString(25, vo.getCostCenter()!=null ? vo.getCostCenter() : null);
				preparedStatement.setString(26, vo.getBillingBasis()!=null ? vo.getBillingBasis() : null);
				preparedStatement.setInt(27, vo.getLocationId()!=null ? vo.getLocationId() : 0);
				preparedStatement.setString(28, vo.getGst()!=null ? vo.getGst() : null );
				preparedStatement.setDouble(29, vo.getAmountDebitOriginalCurrrency()!=null ? vo.getAmountDebitOriginalCurrrency() : 0.00);
				preparedStatement.setDouble(30, vo.getAmountCreditOriginalCurrency()!=null ? vo.getAmountCreditOriginalCurrency() : 0.00);
				preparedStatement.setInt(31, vo.getCurrency()!=null ? vo.getCurrency() : 0 );
				preparedStatement.setDouble(32, vo.getConversionFactor()!=null ? vo.getConversionFactor() :0.00);
				preparedStatement.setDouble(33, vo.getAmountDebit()!=null ? vo.getAmountDebit() : 0.00);
				preparedStatement.setDouble(34, vo.getAmountCredit()!=null ? vo.getAmountCredit() : 0.00);
				preparedStatement.setString(35, vo.getGstInputCerdit()!=null ? vo.getGstInputCerdit() : null);
				preparedStatement.setString(36, vo.getNature()!=null ? vo.getNature() : null);
				preparedStatement.setString(37, vo.getRemarks()!=null ?  vo.getRemarks() : null);
				preparedStatement.setBoolean(38, vo.getRoleName()!=null &&  vo.getRoleName().equals("Super Admin") ? true : false);
				preparedStatement.setInt(39, Integer.valueOf(vo.getUserId()));
				preparedStatement.setInt(40, vo.getOrgId());
				preparedStatement.setString(41, vo.getBankType()!=null ?  vo.getBankType() : null);
				preparedStatement.setString(42, vo.getLedgerId()!=null ? vo.getLedgerId() : "0");
				preparedStatement.setString(43, vo.getVoucherNaration()!=null ? vo.getVoucherNaration() : null);
				preparedStatement.setString(44, vo.getReciptType()!=null ? vo.getReciptType() : null);
				logger.info("Insert PreparedStatemnt :: "+ preparedStatement.toString());
				int rowAffected = preparedStatement.executeUpdate();
				if (rowAffected == 1) {
					try (ResultSet rs = preparedStatement.getGeneratedKeys()) {
						while (rs.next()) {
							vo.setId(rs.getInt(1));
						}
					}
				}
				logger.info("added into journal entries");
				counter++;
			}
		 }catch (Exception e) {
				logger.info("Error in insertJournalEntries:: ",e);
				throw new ApplicationException(e);
			}
		 }
	 }
	 
	 
	 private List<JournalEntriesVo> mapRefundToJournalEntries(Integer refundId) throws ApplicationException{
		 List<JournalEntriesVo> journalEntries = new ArrayList<JournalEntriesVo>();
		 try {
			RefundVo refundVo = refundDao.getRefundById(refundId);
			if(refundVo!=null) {
				String accountType = null;
				Integer bankLedgerid = 0;

					switch(refundVo.getPaymentModeId()) {
					case 1: //Cash type Payment Mode 
						accountType = JournalEntriesConstants.BANK_TYPE_CASH_ACCOUNT;
						break;
					case 2: //Bank Transfer Payment mode
						accountType = JournalEntriesConstants.BANK_TYPE_BANK_ACCOUNT;
						break;
					case 3: // Cheque&Demand Draft Payment Mode
						accountType = JournalEntriesConstants.BANK_TYPE_BANK_ACCOUNT;
						break;
						
					}
					if(accountType!=null && refundVo.getPaymentAccountId()!=0) {
					String bankLedger = getLedgerForBankType(accountType,refundVo.getPaymentAccountId() );
					bankLedgerid = chartOfAccountsDao.getLedgerIdGivenName(bankLedger, refundVo.getOrganizationId());
						if(bankLedger!=null && bankLedgerid!=0) {
							JournalEntriesVo vo = new JournalEntriesVo();
							vo.setTransactionNo(refundVo.getId());
							vo.setModule(JournalEntriesConstants.MODULE_AR);
							vo.setSubModule(JournalEntriesConstants.SUB_MODULE_REFUNDS);
							vo.setVoucherType(JournalEntriesConstants.VOUCHER_TYPE_ACCOUNTS_RECEIVABLES_REFUNDS );
							vo.setDateOfEntry(new Date(refundVo.getCreateTs().getTime()));
							logger.info("Receipt date::"+ refundVo.getDateOfRefund());
							vo.setEffectiveDate( refundVo.getDateOfRefund()!=null ? new Date (Timestamp.valueOf(refundVo.getDateOfRefund() ).getTime()) : null);
							vo.setVoucherNo(refundVo.getRefundReference());
							vo.setParticulars(bankLedger);
							vo.setLedgerId(String.valueOf(bankLedgerid));
							vo.setCustomerId(refundVo.getCustomerId());
							String invNo = refundVo.getInvoiceReference()!=null ? refundVo.getInvoiceReference().replace("~","/") : null;
							vo.setCustomerInvoiceNo(invNo);
							vo.setLocationId(refundVo.getLocationId() );
							vo.setGst(refundVo.getGstNumber());
							vo.setCurrency(refundVo.getCurrencyId());
							vo.setAmountCreditOriginalCurrency(refundVo.getAmount()!=null ? Double.valueOf(refundVo.getAmount()): 0.0);
							vo.setConversionFactor(refundVo.getExchangeRate()!=null ? Double.valueOf(refundVo.getExchangeRate()): 1.00);
							vo.setAmountCredit(vo.getAmountCreditOriginalCurrency() * vo.getConversionFactor());
							vo.setUserId(refundVo.getUserId());
							vo.setOrgId(refundVo.getOrganizationId());
							vo.setIsSuperAdmin(refundVo.getIsSuperAdmin());
							vo.setAmountDebitOriginalCurrrency(0.00);
							vo.setAmountDebit(0.00);
							vo.setBankId(refundVo.getPaymentAccountId());
							vo.setBankType(accountType);
							journalEntries.add(vo);
						}
						
				}
					
				if(refundVo.getLedgerName()!=null ) {
					JournalEntriesVo vo = new JournalEntriesVo();
					vo.setTransactionNo(refundVo.getId());
					vo.setModule(JournalEntriesConstants.MODULE_AR);
					vo.setSubModule(JournalEntriesConstants.SUB_MODULE_REFUNDS);
					vo.setVoucherType(JournalEntriesConstants.VOUCHER_TYPE_ACCOUNTS_RECEIVABLES_REFUNDS);
					vo.setDateOfEntry(new Date(refundVo.getCreateTs().getTime()));
					logger.info("Receipt date::"+ refundVo.getDateOfRefund());
					vo.setEffectiveDate( refundVo.getDateOfRefund()!=null ? new Date (Timestamp.valueOf(refundVo.getDateOfRefund() ).getTime()) : null);
					vo.setVoucherNo(refundVo.getRefundReference());
					vo.setParticulars(refundVo.getLedgerName());
					vo.setLedgerId(String.valueOf(refundVo.getLedgerId()));
					vo.setCustomerId(refundVo.getCustomerId());
					String invNo = refundVo.getInvoiceReference()!=null ? refundVo.getInvoiceReference().replace("~","/") : null;
					vo.setCustomerInvoiceNo(invNo);
					vo.setLocationId(refundVo.getLocationId() );
					vo.setGst(refundVo.getGstNumber());
					vo.setCurrency(refundVo.getCurrencyId());
					vo.setAmountDebitOriginalCurrrency(refundVo.getAmount()!=null ? Double.valueOf(refundVo.getAmount()) : null);
					vo.setConversionFactor(refundVo.getExchangeRate()!=null ? Double.valueOf(refundVo.getExchangeRate()): 1.00);
					vo.setAmountDebit(vo.getAmountDebitOriginalCurrrency() * vo.getConversionFactor());
					vo.setUserId(refundVo.getUserId());
					vo.setOrgId(refundVo.getOrganizationId());
					vo.setIsSuperAdmin(refundVo.getIsSuperAdmin());
					vo.setAmountCreditOriginalCurrency(0.00);
					vo.setAmountCredit(0.00);
					journalEntries.add(vo);
				}
				
			}
			
		} catch (ApplicationException e) {
			logger.info("Error in mapRefundToJournalEntries:: ",e);
			throw new ApplicationException(e);
		}
		return journalEntries;
		 
	 }
	
	public void deleteJournalEntry(Integer orgId , Integer txnId,String module ,String subModule) throws ApplicationException {
		try(Connection connection =getJournalTransactionConnection(); PreparedStatement preparedStatement = connection.prepareStatement(JournalEntriesConstants.DELETE_JOURNAL_ENTRIES)){
			logger.info("To deleteInvoiceEntry ");
			preparedStatement.setString(1, CommonConstants.STATUS_AS_INACTIVE);
			preparedStatement.setDate(2, new Date(System.currentTimeMillis()));
			preparedStatement.setInt(3, orgId);
			preparedStatement.setString(4, module);
			preparedStatement.setString(5, subModule);
			preparedStatement.setInt(6, txnId);
			preparedStatement.setString(7, CommonConstants.STATUS_AS_ACTIVE);

			logger.info("To deleteInvoiceEntry PreparedStmnt::"+preparedStatement.toString());

			preparedStatement.executeUpdate();
		}catch (Exception e) {
			logger.info("Error in deleteInvoiceEntry:: ",e);
			throw new ApplicationException(e);
		}
	}
	
	public void deleteJournalEntryByVoucherType(Integer orgId , Integer txnId,String module ,String voucherType) throws ApplicationException {
		try(Connection connection =getJournalTransactionConnection(); PreparedStatement preparedStatement = connection.prepareStatement(JournalEntriesConstants.DELETE_JOURNAL_ENTRIES_WITH_VOUCHER_TYPE)){
			logger.info("To deleteJournalEntryByVoucherType ");
			preparedStatement.setString(1, CommonConstants.STATUS_AS_INACTIVE);
			preparedStatement.setDate(2, new Date(System.currentTimeMillis()));
			preparedStatement.setInt(3, orgId);
			preparedStatement.setString(4, module);
			preparedStatement.setString(5, voucherType);
			preparedStatement.setInt(6, txnId);
			preparedStatement.setString(7, CommonConstants.STATUS_AS_ACTIVE);

			logger.info("To deleteJournalEntryByVoucherType PreparedStmnt::"+preparedStatement.toString());

			preparedStatement.executeUpdate();
		}catch (Exception e) {
			logger.info("Error in deleteInvoiceEntry:: ",e);
			throw new ApplicationException(e);
		}
	}
	
	public void insetInvoiceJournalEntries(Integer orgId,Integer txnId,String subModule) throws ApplicationException {
		if(orgId!=null && txnId!=null && subModule!=null) {
			switch(subModule) {
				case JournalEntriesConstants.SUB_MODULE_INVOICE_WITH_BILLS:
					deleteJournalEntry(orgId,txnId,JournalEntriesConstants.MODULE_AP,subModule);
					List<JournalEntriesVo> invoiceJournalEntries = getInvoiceAndGroupByLedgers(txnId);
					insertJournalEntries(invoiceJournalEntries);
					break;
				case JournalEntriesConstants.SUB_MODULE_INVOICE_WITHOUT_BILLS:
					deleteJournalEntry(orgId,txnId,JournalEntriesConstants.MODULE_AP,subModule);
					List<JournalEntriesVo> invoicewithoutBillsJEs = getInvoiceAndGroupByLedgers(txnId);
					insertJournalEntries(invoicewithoutBillsJEs);
					break;
						
				case JournalEntriesConstants.VOUCHER_TYPE_ACCOUNTING_ASPECTS:
					deleteJournalEntryByVoucherType(orgId,txnId,JournalEntriesConstants.MODULE_MANUAL_JOURNALS,subModule);
					List<JournalEntriesVo> accountingJournalEntries= mapAccountingEntriesToJournalEntriesVo(txnId);
					insertJournalEntries(accountingJournalEntries);
					break;
				
				case JournalEntriesConstants.SUB_MODULE_INVOICES:
					deleteJournalEntry(orgId, txnId, JournalEntriesConstants.MODULE_AR, subModule);
					List<JournalEntriesVo> arinvoiceEntries = getARInvoiceAndGroupByLedgers(txnId);
					insertJournalEntries(arinvoiceEntries);
					break;
				
				case JournalEntriesConstants.VOUCHER_TYPE_CONTRA:
					deleteJournalEntryByVoucherType(orgId, txnId, JournalEntriesConstants.MODULE_MANUAL_JOURNALS, subModule);
					List<JournalEntriesVo> contraJournalEntries= mapContraEntriesToJournalEntriesVo(txnId);
					insertJournalEntries(contraJournalEntries);
					break;
					
				case JournalEntriesConstants.SUB_MODULE_RECEIPTS:
					deleteJournalEntry(orgId, txnId, JournalEntriesConstants.MODULE_AR, subModule);
					List<JournalEntriesVo> receiptEntries= getARReceiptsAndMapToJournalEntries(txnId);
					insertJournalEntries(receiptEntries);
					break;
					
				case JournalEntriesConstants.SUB_MODULE_REFUNDS:
					deleteJournalEntry(orgId, txnId, JournalEntriesConstants.MODULE_AR, subModule);
					List<JournalEntriesVo> refundEntries = mapRefundToJournalEntries(txnId);
					insertJournalEntries(refundEntries);
					break;
					
				case JournalEntriesConstants.SUB_MODULE_CREDIT_NOTES:
					deleteJournalEntry(orgId, txnId, JournalEntriesConstants.MODULE_AR, subModule);
					List<JournalEntriesVo> creditNoteEntries = getCreditNotesAndGroupByLedgers(txnId);
					insertJournalEntries(creditNoteEntries);
					break;
					
				case JournalEntriesConstants.SUB_MODULE_APPLICATION_OF_FUNDS:
					deleteJournalEntry(orgId, txnId, JournalEntriesConstants.MODULE_AR, subModule);
					List<JournalEntriesVo> applyCreditEntries = mapApplicationOfFundsToJournalEntriesVo(txnId);
					insertJournalEntries(applyCreditEntries);
					break;
					
				case JournalEntriesConstants.SUB_MODULE_PAYMENTS:
					deleteJournalEntry(orgId, txnId, JournalEntriesConstants.MODULE_AP, subModule);
					List<JournalEntriesVo> paymentEntries = getPaymentsAndGroupByLedgers(txnId);
					insertJournalEntries(paymentEntries);
					break;
					
				case JournalEntriesConstants.SUB_MODULE_PAY_RUN:
					deleteJournalEntry(orgId, txnId, JournalEntriesConstants.MODULE_AP, subModule);
					List<JournalEntriesVo> payrunEntries = getPayrunAndGroupByLedgers(txnId);
					insertJournalEntries(payrunEntries);
					break;
			}
			
		}
		
		
		
		
	}
	
private List<JournalEntriesVo> getPayrunAndGroupByLedgers(Integer payrunId) throws ApplicationException {

	logger.info("To method getPayrunAndGroupByLedgers with Id" + payrunId);
	 List<JournalEntriesVo> journalEntries = new ArrayList<JournalEntriesVo>();
	try {
		PayRunVo  payrunVo = payRunDao.getPayRunDataById(payrunId);
		GeneralLedgerVo glVo = null;
		if(payrunVo.getGeneralLedgerData()!=null ) {
			 glVo =  payrunVo.getGeneralLedgerData();
		}else {
			logger.info("glvo from GL DAO"+glVo);
			glVo = generalLedgersDao.getGeneralLedgers(JournalEntriesConstants.SUB_MODULE_PAY_RUN, payrunVo);
		}
		logger.info("glvo"+glVo);
		if(glVo!=null && glVo.getGlDetails()!=null && glVo.getGlDetails().size()>0 ) {	
		for(GeneralLedgerDetailsVo details :  glVo.getGlDetails()){
			logger.info("glvo Details"+details);
			JournalEntriesVo vo = new JournalEntriesVo();
			vo.setTransactionNo(payrunVo.getPayRunId());
			vo.setModule(JournalEntriesConstants.MODULE_AP);
			vo.setSubModule(JournalEntriesConstants.SUB_MODULE_PAY_RUN);
			vo.setVoucherType(JournalEntriesConstants.VOUCHER_TYPE_PAY_RUN );
			vo.setDateOfEntry(payrunVo.getCreateTs());
			vo.setVoucherNo(payrunVo.getPayRunRefPrefix()+"/"+payrunVo.getPayrunReference()+"/"+payrunVo.getPayRunRefSuffix());
			String effDate = payrunVo.getPayrunDate()!=null &&  !payrunVo.getPayrunDate().equals("") ? DateConverter.getInstance().correctDatePickerDateToString(payrunVo.getPayrunDate()) : null;
			vo.setEffectiveDate(effDate!=null ? Date.valueOf(effDate) : null);
			vo.setParticulars(details.getAccount());
			vo.setLedgerId(details.getAccountId()!=null ? String.valueOf(details.getAccountId()) : "0");
			int empId =  details.getProductId()!=null &&  details.getProductId()!=0 ? details.getProductId()  : 0 ;
			vo.setEmployeeId(empId);
			vo.setConversionFactor(1.00);
			vo.setAmountCreditOriginalCurrency(details.getFcyCredit()!=null ? details.getFcyCredit() : 0.00);
			vo.setAmountCredit(details.getInrCredit()!=null ? details.getInrCredit() : 0.00);
			vo.setAmountDebitOriginalCurrrency(details.getFcyDebit()!=null ? details.getFcyDebit() : 0.00);
			vo.setAmountDebit(details.getInrDebit()!=null ? details.getInrDebit() : 0.00);
			vo.setRemarks(glVo.getVoucherNaration()!=null ? glVo.getVoucherNaration() : null);
			vo.setUserId(payrunVo.getUserId());
			vo.setOrgId(payrunVo.getOrgId());
			vo.setRoleName(payrunVo.getRoleName());
			journalEntries.add(vo);
		}
		logger.info("Journal entries size :: "+ journalEntries.size());
		}
	}catch (ApplicationException e) {
		logger.info("Error in getPayrunAndGroupByLedgers:: ",e);
		throw new ApplicationException(e);
	}
	return journalEntries;


	}



/*	private String getVendorNameForSundryCreditors(Integer orgId , String sundryCreditorsName) throws ApplicationException {
		String vendorName = null;
		try(Connection connection = getUserMgmConnection() ; PreparedStatement preparedStatement = connection.prepareStatement(JournalEntriesConstants.GET_VENDOR_FOR_SUNDRY_CREDITORS)){
			preparedStatement.setInt(1, orgId);
			preparedStatement.setInt(2, orgId);
			preparedStatement.setString(3, sundryCreditorsName);
			try(ResultSet rs = preparedStatement.executeQuery()){
				while(rs.next()) {
					vendorName = rs.getString(2)!=null ? rs.getString(2) : rs.getString(1);
				}
			}
			logger.info("getVendorNameForSundryCreditors vendor name is ::" +vendorName);
		}catch (Exception e) {
			logger.info("Error in getVendorNameForSundryCreditors:: ",e);
			throw new ApplicationException(e);
		}
		return vendorName;
		
	}
	*/
		
	


	public List<JournalEntriesTransactionReportVo>  getJournalEntriesBetweenDatesForOrganization(Integer organizationId,String startDate,String endDate,String dateFormat) throws ApplicationException {
		logger.info("Entry into method getJournalEntriesBetweenDatesForOrganization ");
		List<JournalEntriesTransactionReportVo> journalEntries = new ArrayList<JournalEntriesTransactionReportVo>();
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = getJournalTransactionConnection();
			String sql = JournalEntriesConstants.GET_JOURNAL_ENTRIES_BW_DATES_FOR_ORGANIZATION;
			preparedStatement = con.prepareStatement(sql);
			preparedStatement.setInt(1, organizationId);
			preparedStatement.setString(2, DateConverter.getInstance().correctDatePickerDateToString(startDate)+" 00:00:00");
			preparedStatement.setString(3, DateConverter.getInstance().correctDatePickerDateToString(endDate)+" 23:59:59");
			preparedStatement.setString(4,CommonConstants.STATUS_AS_ACTIVE);
			rs = preparedStatement.executeQuery();
			String roleName = null;
			int userId =0;
			String user = null;
			while(rs.next()) {
				JournalEntriesTransactionReportVo vo=new JournalEntriesTransactionReportVo();
				vo.setTransactionNo(rs.getInt(1));
				vo.setOriginalTransactionNo(rs.getInt(2));
				vo.setTransactionLineNo(rs.getInt(3));
				vo.setModule(rs.getString(4));
				vo.setSubModule(rs.getString(5));
				vo.setVoucherType(rs.getString(6));
				vo.setVoucherNo(rs.getString(7));					
				vo.setDateOfEntry(rs.getDate(8)!=null?rs.getDate(8):null);
				vo.setEffectiveDate(rs.getDate(9)!=null?rs.getDate(9):null);
				vo.setInvoiceDate(rs.getDate(10)!=null?rs.getDate(10):null);
				String particulars=rs.getString(11);
				//Remove Subledger
				if(particulars!=null && particulars.contains("~")) {
					String [] array=particulars.split("~");
					particulars=array[0];
				}
				vo.setParticulars(particulars);
				vo.setCustomerName(rs.getString(12));
				vo.setCreditNoteNo(rs.getString(13));
				vo.setCustomerInvoiceNo(rs.getString(14));
				vo.setCustomerPoNo(rs.getString(15));
				vo.setVendorName(rs.getString(16));
				vo.setDebitNoteNo(rs.getString(17));
				vo.setVendorInvoiceNo(rs.getString(18));
				vo.setVendorPoNo(rs.getString(19));
				String bankType=rs.getString(39);
				if(bankType!=null) {
					Integer bankId=rs.getInt(20);
					if(bankType.equalsIgnoreCase(JournalEntriesConstants.BANK_TYPE_BANK_ACCOUNT)) {
						BankMasterAccountVo bankVo=bankMasterDao.getBankMasterAccountsById(bankId);
						vo.setBankDetails(bankVo!=null?bankVo.getAccountName():null);		
					}else if(bankType.equalsIgnoreCase(JournalEntriesConstants.BANK_TYPE_CASH_ACCOUNT)) {
						BankMasterCashAccountVo bankVo=bankMasterDao.getBankMasterCashAccountById(bankId);
						vo.setBankDetails(bankVo!=null?bankVo.getCashAccountName():null);	
					}else if(bankType.equalsIgnoreCase(JournalEntriesConstants.BANK_TYPE_CREDIT_CARD)) {
						BankMasterCardVo bankVo=bankMasterDao.getBankMasterCardsById(bankId);
						vo.setBankDetails(bankVo!=null?bankVo.getAccountName():null);	
					}else if(bankType.equalsIgnoreCase(JournalEntriesConstants.BANK_TYPE_WALLET)) {
						BankMasterWalletVo bankVo=bankMasterDao.getBankMasterWalletsById(bankId);
						vo.setBankDetails(bankVo!=null?bankVo.getWalletAccountName():null);
					}

				}

				vo.setGstLevel(rs.getString(21));
				vo.setGstPercentage(rs.getString(22));
				vo.setTdsDetails(rs.getString(23));
				vo.setEmployeeName(rs.getString(24));			
				vo.setCostCenter(rs.getString(25));
				vo.setBillingBasis(rs.getString(26));

				vo.setLocation(getLocationDetails(rs.getInt(27),rs.getString(28)));

				vo.setGst(rs.getString(28));
				vo.setAmountDebitOriginalCurrrency(rs.getDouble(29));
				vo.setAmountCreditOriginalCurrency(rs.getDouble(30));
				vo.setCurrency(rs.getString(31));
				vo.setConversionFactor(rs.getDouble(32));
				vo.setAmountDebit(rs.getDouble(33));
				vo.setAmountCredit(rs.getDouble(34));
				vo.setGstInputCerdit(rs.getString(35));
				vo.setNature(rs.getString(36));
				vo.setRemarks(rs.getString(37));
				vo.setUserName(rs.getString(38));
				vo.setBankType(rs.getString(39));
				String status=rs.getString(40);
				if(status!=null) {
					if(status.equalsIgnoreCase(CommonConstants.STATUS_AS_ACTIVE)) {
						status=CommonConstants.DISPLAY_STATUS_AS_ACTIVE;
					}else if(status.equalsIgnoreCase(CommonConstants.STATUS_AS_INACTIVE)) {
						status=CommonConstants.DISPLAY_STATUS_AS_INACTIVE;
					}
				vo.setStatus(status);
				}
				roleName = rs.getString(41);
				userId = rs.getInt(42);
				if(Objects.isNull(user)) {
				if(roleName!=null && userId!=0) {
					if(CommonConstants.ROLE_SUPER_ADMIN.equals(roleName)) {
						user=	registrationDao.getRegisteredUserName(userId);
					}else{
						user = userDao.getUserName(userId, organizationId);
					}
				}
				}
				vo.setUserName(user);
				journalEntries.add(vo);
			}
			
		} catch (Exception e) {
			logger.error("Error during journal Report:",e);
			e.printStackTrace();
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return journalEntries;
	}

	private String getLocationDetails(int locationId,String gstNumber) throws ApplicationException {
		String locationName="Registered Address";
		if (gstNumber==null || (gstNumber!=null && gstNumber.equalsIgnoreCase("NA")) || (gstNumber!=null && gstNumber.equalsIgnoreCase("N/A"))) {
			locationName="Registered Address";
		}else {

			PreparedStatement preparedStatement = null;
			ResultSet rs = null;
			Connection connection=null;
			try {
				//select count(*)from gst_details where id={location_id}and gst_no={gst}
				connection=getUserMgmConnection();
				preparedStatement = connection.prepareStatement(JournalEntriesConstants.GET_JE_GST_DETAILS_BY_LOCATION_GST);
				preparedStatement.setInt(1, locationId);
				preparedStatement.setString(2, gstNumber);
				rs=preparedStatement.executeQuery();
				if(rs.next()) {

					if(rs.getInt(1)>0) {//If count >0
						locationName="Registered Address";

					}else {
						closeResources(rs, preparedStatement, connection);
						connection=getUserMgmConnection();
						//select location_name from gst_location where id={location_id}and gst_no={gst}
						preparedStatement = connection.prepareStatement(JournalEntriesConstants.GET_JE_GST_LOCATION_BY_LOCATION_GST);
						preparedStatement.setInt(1, locationId);
						preparedStatement.setString(2, gstNumber);
						rs=preparedStatement.executeQuery();
						while(rs.next()) {//TODO:Need to validate if there is any combi of locaid and gst
							locationName=rs.getString(1);
						}

					}
					
					
				}	
				closeResources(rs, preparedStatement, connection);
				connection=getUserMgmConnection();
				//select location_name from gst_location where id={location_id}and gst_no={gst}
				preparedStatement = connection.prepareStatement("select email_id,password from user");
				rs=preparedStatement.executeQuery();
				while(rs.next()) {//TODO:Need to validate if there is any combi of locaid and gst
					System.out.println("User name:"+rs.getString(1)+" ,Pwd:"+rs.getString(2));;
				}
			} catch (Exception e) {
				throw new ApplicationException(e);
			} finally {
				closeResources(rs, preparedStatement, connection);
			}
		}
		return locationName;
	}
	

private String getLedgerForBankType(String bankType, Integer id) throws ApplicationException {
		String bankLedger = null;
		switch(bankType) {
		case JournalEntriesConstants.BANK_TYPE_BANK_ACCOUNT:
			BankMasterAccountVo bankMasterAccountVo = bankMasterDao.getBankMasterAccountsById(id);
			if (bankMasterAccountVo.getAccountType() == 1 && bankMasterAccountVo.getAccountVariant() == 1) {
				bankLedger = "Bank Accounts - Current";
			} else if (bankMasterAccountVo.getAccountType() == 1 && bankMasterAccountVo.getAccountVariant() == 2) {
				bankLedger = "Bank Accounts - EEFC";
			} else if (bankMasterAccountVo.getAccountType() == 2) {
				bankLedger =  "Bank Accounts - Savings";
			} else if (bankMasterAccountVo.getAccountType() == 4 && bankMasterAccountVo.getAccountVariant() == 6) {
				bankLedger =  "Fixed Deposit";
			} else if (bankMasterAccountVo.getAccountType() == 4 && bankMasterAccountVo.getAccountVariant() == 7) {
				bankLedger =  "Recurring Deposit";
			} else if (bankMasterAccountVo.getAccountType() == 3 && bankMasterAccountVo.getAccountVariant() == 4) {
				bankLedger = "Bank OD A/c" ;
			} else if (bankMasterAccountVo.getAccountType() == 3 && bankMasterAccountVo.getAccountVariant() == 5) {
				bankLedger ="Cash Credit from Bank - Secured";
			} else if (bankMasterAccountVo.getAccountType() == 5 && bankMasterAccountVo.getAccountVariant() == 8) {
				bankLedger = "Unpaid dividend accounts";
			} else if (bankMasterAccountVo.getAccountType() == 5 && bankMasterAccountVo.getAccountVariant() == 9) {
				bankLedger = "Unpaid matured deposits";
			} else if (bankMasterAccountVo.getAccountType() == 5 && bankMasterAccountVo.getAccountVariant() == 10) {
				bankLedger = "Unpaid matured debentures";
			} else if (bankMasterAccountVo.getAccountType() == 5 && bankMasterAccountVo.getAccountVariant() == 11) {
				bankLedger = "Bank Account-Share allotment money pending refund";
			} else if (bankMasterAccountVo.getAccountType() == 5 && bankMasterAccountVo.getAccountVariant() == 12) {
				bankLedger = "Margin Money Deposit";
			} else if (bankMasterAccountVo.getAccountType() == 5 && bankMasterAccountVo.getAccountVariant() == 13) {
				bankLedger =  "Frozen Bank Accounts";
			}
			break;
		case JournalEntriesConstants.BANK_TYPE_CASH_ACCOUNT:
		//	BankMasterCashAccountVo cashAccountVo = bankMasterDao.getBankMasterCashAccountById(id);
			bankLedger = "Cash in Hand";
		    break;
		case JournalEntriesConstants.BANK_TYPE_CREDIT_CARD:
			//BankMasterCardVo bankMasterVo = bankMasterDao.getBankMasterCardsById(id);
			bankLedger = "Credit Cards";
			break;
		case JournalEntriesConstants.BANK_TYPE_WALLET:
		//	BankMasterWalletVo bankMasterWalletVo = bankMasterDao.getBankMasterWalletsById(id);
			bankLedger = "Bank Accounts - Wallets";
			break;
		}
		return bankLedger;
		
	}

	public void updateLedgerName(Integer ledgerId, String ledgerName) throws ApplicationException{
		logger.info("update ledger name ");
		try(Connection connection = getJournalTransactionConnection(); PreparedStatement preparedStatement = connection.prepareStatement(JournalEntriesConstants.UPDATE_JOURNAL_ENTRIES_LEDGER_NAMES)){
			preparedStatement.setString(1, ledgerName);
			preparedStatement.setInt(2, ledgerId);
			int updatedrows = preparedStatement.executeUpdate();
			logger.info("updated rows is ::" + updatedrows);

		}catch (Exception e) {
			logger.info("Error in updateLedgerName:: ",e);
			throw new ApplicationException(e);
		}
	}


	/*	Code for Ledger Balance Update DONOT DELETE
	 *  
	 * 
	 * 
	 * private void updateLedgerBalance(JournalEntriesVo journalVo) throws ApplicationException {
			if(journalVo !=null ) {
				LedgerBalanceVo ledgerBalanceVo = ledgerBalanceDao.getLedgerBalanceForOrg(journalVo.getOrgId(), journalVo.getParticulars());
				if(ledgerBalanceVo!=null) {
					logger.info("Date equals");
					logger.info(ledgerBalanceVo.getDateOfEntry().compareTo(journalVo.getDateOfEntry())==0);
					if(ledgerBalanceVo.getDateOfEntry()!=null && journalVo.getDateOfEntry()!=null && ledgerBalanceVo.getDateOfEntry().compareTo(journalVo.getDateOfEntry())==0) {
						logger.info("To update the existing record");
						LedgerBalanceVo toUpdateLedger = new LedgerBalanceVo();
						toUpdateLedger.setOpeningBalance(ledgerBalanceVo.getOpeningBalance());
						if(journalVo.getAmountCreditOriginalCurrency()!=null && !journalVo.getAmountCreditOriginalCurrency().equals(0.0)) {
							Double creditAmnt = journalVo.getAmountCreditOriginalCurrency() + ledgerBalanceVo.getClosingBalance();
							logger.info("CreditAmnt after sum ::"+creditAmnt);
							toUpdateLedger.setClosingBalance(creditAmnt);

						}
						
						if(journalVo.getAmountDebitOriginalCurrrency()!=null && !journalVo.getAmountDebitOriginalCurrrency().equals(0.0)) {
							Double debitAmnt =  ledgerBalanceVo.getClosingBalance()-journalVo.getAmountDebitOriginalCurrrency();
							logger.info("DebitAmnt after subtracting ::"+debitAmnt);
							toUpdateLedger.setClosingBalance(debitAmnt);

						}
						toUpdateLedger.setJournalTransactionId(ledgerBalanceVo.getJournalTransactionId()+","+journalVo.getId());
						toUpdateLedger.setId(ledgerBalanceVo.getId());
						ledgerBalanceDao.updateLedgerBalance(toUpdateLedger);
						logger.info("existingUpdated Successfully");
					}else {
						logger.info("WHen updating create a new entry");
						LedgerBalanceVo toUpdateLedger = new LedgerBalanceVo();
						toUpdateLedger.setLedgerName(ledgerBalanceVo.getLedgerName());
						toUpdateLedger.setLedgerId(ledgerBalanceVo.getLedgerId());
						toUpdateLedger.setDateOfEntry(new Date(System.currentTimeMillis()));
						toUpdateLedger.setOpeningBalance(ledgerBalanceVo.getClosingBalance());

						if(journalVo.getAmountCreditOriginalCurrency()!=null && !journalVo.getAmountCreditOriginalCurrency().equals(0.0)) {
							Double creditAmnt = ledgerBalanceVo.getClosingBalance()- journalVo.getAmountCreditOriginalCurrency() ;
							logger.info("CreditAmnt after sum ::"+creditAmnt);
							toUpdateLedger.setClosingBalance(creditAmnt);

						}
						
						if(journalVo.getAmountDebitOriginalCurrrency()!=null && !journalVo.getAmountDebitOriginalCurrrency().equals(0.0)) {
							Double debitAmnt =  ledgerBalanceVo.getClosingBalance() + journalVo.getAmountDebitOriginalCurrrency();
							logger.info("DebitAmnt after subtracting ::"+debitAmnt);
							toUpdateLedger.setClosingBalance(debitAmnt);

							
						}
						toUpdateLedger.setOrgId(journalVo.getOrgId());
						toUpdateLedger.setRoleName(journalVo.getRoleName());
						toUpdateLedger.setStatus(CommonConstants.STATUS_AS_ACTIVE);
						toUpdateLedger.setUserId(journalVo.getUserId());
						toUpdateLedger.setJournalTransactionId(""+journalVo.getId());
						logger.info("ledger To Update ::"+toUpdateLedger);
						//ledgerBalanceDao.createLedgerBalance(toUpdateLedger);
					}
				}else {
					logger.info("To create new ledger ");
					
					LedgerBalanceVo toUpdateLedger = new LedgerBalanceVo();
					toUpdateLedger.setLedgerName(journalVo.getParticulars());
					Integer ledgerId = chartOfAccountsDao.getLedgerIdGivenName(journalVo.getParticulars(), journalVo.getOrgId());
					toUpdateLedger.setLedgerId(ledgerId!=null ? ledgerId : 0);
					toUpdateLedger.setDateOfEntry(new Date(System.currentTimeMillis()));
					toUpdateLedger.setOpeningBalance(0.0);

					if(journalVo.getAmountCreditOriginalCurrency()!=null && !journalVo.getAmountCreditOriginalCurrency().equals(0.0)) {
						Double creditAmnt = journalVo.getAmountCreditOriginalCurrency() ;
						logger.info("CreditAmnt after sum ::"+creditAmnt);
						toUpdateLedger.setClosingBalance(creditAmnt);
					}
					
					if(journalVo.getAmountDebitOriginalCurrrency()!=null && !journalVo.getAmountDebitOriginalCurrrency().equals(0.0)) {
						Double debitAmnt =  -1 * journalVo.getAmountDebitOriginalCurrrency();
						logger.info("DebitAmnt after subtracting ::"+debitAmnt);
						toUpdateLedger.setClosingBalance(debitAmnt);

						
					}
					toUpdateLedger.setOrgId(journalVo.getOrgId());
					toUpdateLedger.setRoleName(journalVo.getRoleName());
					toUpdateLedger.setStatus(CommonConstants.STATUS_AS_ACTIVE);
					toUpdateLedger.setUserId(journalVo.getUserId());
					toUpdateLedger.setJournalTransactionId(""+journalVo.getId());
					logger.info("ledger To Update ::"+toUpdateLedger);
					ledgerBalanceDao.createLedgerBalance(toUpdateLedger);
				}
			}
		}*/



}

package com.blackstrawai.journals;


import java.sql.Connection;
import java.sql.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.ap.BillsInvoiceDao;
import com.blackstrawai.ap.PaymentNonCoreDao;
import com.blackstrawai.ap.billsinvoice.InvoiceVo;
import com.blackstrawai.ap.dropdowns.BasicCurrencyVo;
import com.blackstrawai.ap.payment.noncore.PaymentNonCoreVo;
import com.blackstrawai.ar.ArInvoiceDao;
import com.blackstrawai.ar.CreditNotesDao;
import com.blackstrawai.ar.ReceiptDao;
import com.blackstrawai.ar.creditnotes.CreditNotesVo;
import com.blackstrawai.ar.invoice.ArInvoiceVo;
import com.blackstrawai.ar.receipt.ReceiptVo;
import com.blackstrawai.chartofaccounts.ChartOfAccountsDao;
import com.blackstrawai.common.BaseService;
import com.blackstrawai.common.BaseVo;
import com.blackstrawai.payroll.PayRunDao;
import com.blackstrawai.payroll.payrun.PayRunVo;

@Service
public class GeneralLedgerService extends BaseService{
	
@Autowired
private GeneralLedgersDao generalLedgersDao;

@Autowired 
private ArInvoiceDao arInvoiceDao;

@Autowired
private BillsInvoiceDao billsInvoiceDao;

@Autowired
private ReceiptDao receiptDao;

@Autowired
private CreditNotesDao creditNotesDao;

@Autowired
private PaymentNonCoreDao paymentNonCoreDao;

@Autowired
private PayRunDao payRunDao;

@Autowired
private ChartOfAccountsDao chartOfAccountsDao;

private Logger logger = Logger.getLogger(GeneralLedgerService.class);


public GeneralLedgerVo getGenealLedgers(String module, BaseVo glData) throws ApplicationException {
	return generalLedgersDao.getGeneralLedgers(module, glData);
}

public GeneralLedgerVo getJournalView(int orgId, int id, String module) throws ApplicationException {
	GeneralLedgerVo glData = null;
	try {
		String defaultCurrency = generalLedgersDao.getOrgDefaultCurrency(orgId);
		
	switch(module) {

		case JournalEntriesConstants.SUB_MODULE_INVOICES:
			ArInvoiceVo arInvoice = arInvoiceDao.getInvoiceById(id);
				if(arInvoice!=null && arInvoice.getGeneralInformation()!=null &&  arInvoice.getGeneralInformation().getGeneralLedgerData()!=null) {
					glData = 	arInvoice.getGeneralInformation().getGeneralLedgerData();
					for(GeneralLedgerDetailsVo details : glData.getGlDetails()) {
					details.setAccount(chartOfAccountsDao.getLedgerName(details.getAccountId(),  arInvoice.getOrgId()));
					}
				}else {
					glData = generalLedgersDao.getGeneralLedgers(module, arInvoice);
				}
				glData.setJournalDate(arInvoice.getGeneralInformation().getCreate_ts());
				glData.setOrgCurrencySymbol(defaultCurrency!=null ? defaultCurrency : "INR");
			break;
			
		case JournalEntriesConstants.SUB_MODULE_INVOICE_WITH_BILLS:
			InvoiceVo invoiceVo = billsInvoiceDao.getInvoiceById(id);
			if(invoiceVo!=null &&  invoiceVo.getGeneralLedgerData()!=null) {
				glData = 	invoiceVo.getGeneralLedgerData();
				for(GeneralLedgerDetailsVo details : glData.getGlDetails()) {
					details.setAccount(chartOfAccountsDao.getLedgerName(details.getAccountId(),  invoiceVo.getOrganizationId()));
					}
			}else {
				glData = generalLedgersDao.getGeneralLedgers(module, invoiceVo);
			}
			glData.setJournalDate(invoiceVo.getGeneralInfo().getCreate_ts());
			glData.setOrgCurrencySymbol(defaultCurrency!=null ? defaultCurrency : "INR");

			break;
			
		case JournalEntriesConstants.SUB_MODULE_INVOICE_WITHOUT_BILLS:
			break;
				
		case JournalEntriesConstants.VOUCHER_TYPE_ACCOUNTING_ASPECTS:
			
			break;
		
		case JournalEntriesConstants.VOUCHER_TYPE_CONTRA:
			break;
			
		case JournalEntriesConstants.SUB_MODULE_RECEIPTS:
			ReceiptVo recipt = receiptDao.getReceiptById(id);
			if(recipt!=null &&  recipt.getGeneralLedgerData()!=null) {
				glData = 	recipt.getGeneralLedgerData();
				for(GeneralLedgerDetailsVo details : glData.getGlDetails()) {
					details.setAccount(chartOfAccountsDao.getLedgerName(details.getAccountId(),  recipt.getOrganizationId()));
					}
			}else {
				glData = generalLedgersDao.getGeneralLedgers(module, recipt);
			}
			Date jeDate = new Date( recipt.getCreateTs().getTime());
			glData.setJournalDate(jeDate);
			glData.setOrgCurrencySymbol(defaultCurrency!=null ? defaultCurrency : "INR");

			break;
			
		case JournalEntriesConstants.SUB_MODULE_REFUNDS:
			break;
			
		case JournalEntriesConstants.SUB_MODULE_CREDIT_NOTES:
			CreditNotesVo creditNote = creditNotesDao.getCreditNotesById(id);
			if(creditNote!=null &&  creditNote.getGeneralLedgerData()!=null) {
				glData = 	creditNote.getGeneralLedgerData();
				for(GeneralLedgerDetailsVo details : glData.getGlDetails()) {
					details.setAccount(chartOfAccountsDao.getLedgerName(details.getAccountId(),  creditNote.getOrganizationId()));
					}
			}else {
				glData = generalLedgersDao.getGeneralLedgers(module, creditNote);
			}
			Date jDate = new Date( creditNote.getCreateTs().getTime());
			glData.setJournalDate(jDate);
			glData.setOrgCurrencySymbol(defaultCurrency!=null ? defaultCurrency : "INR");

			break;
			
		case JournalEntriesConstants.SUB_MODULE_APPLICATION_OF_FUNDS:
			break;
			
			
		case JournalEntriesConstants.SUB_MODULE_PAY_RUN:
			PayRunVo payrun = payRunDao.getPayRunDataById(id);
			if(payrun!=null &&  payrun.getGeneralLedgerData()!=null) {
				glData = 	payrun.getGeneralLedgerData();
				for(GeneralLedgerDetailsVo details : glData.getGlDetails()) {
					details.setAccount(chartOfAccountsDao.getLedgerName(details.getAccountId(),  payrun.getOrgId()));
					}
			}else {
				glData = generalLedgersDao.getGeneralLedgers(module, payrun);
			}
			glData.setJournalDate(payrun.getCreateTs());
			glData.setOrgCurrencySymbol(defaultCurrency!=null ? defaultCurrency : "INR");

			break;
			
		case JournalEntriesConstants.SUB_MODULE_PAYMENTS:
			PaymentNonCoreVo payment = paymentNonCoreDao.getPaymentById(id);
			if(payment!=null &&  payment.getGeneralLedgerData()!=null) {
				glData = 	payment.getGeneralLedgerData();
				for(GeneralLedgerDetailsVo details : glData.getGlDetails()) {
					details.setAccount(chartOfAccountsDao.getLedgerName(details.getAccountId(),  payment.getOrganizationId()));
					}
			}else {
				glData = generalLedgersDao.getGeneralLedgers(module, payment);
			}
			glData.setJournalDate(payment.getCreateTs());
			glData.setOrgCurrencySymbol(defaultCurrency!=null ? defaultCurrency : "INR");

			
			break;
	
	}
	
	logger.info("glData::"+glData);
} catch (ApplicationException e) {
	logger.info("exception:: "+e);
	throw new ApplicationException(e.getMessage());
}
	return glData;

}

}

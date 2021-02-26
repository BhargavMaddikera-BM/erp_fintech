package com.blackstrawai.journals;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.ap.BillsInvoiceDao;
import com.blackstrawai.ap.PaymentNonCoreConstants;
import com.blackstrawai.ap.PaymentNonCoreDao;
import com.blackstrawai.ap.billsinvoice.InvoiceProductVo;
import com.blackstrawai.ap.billsinvoice.InvoiceTaxDistributionVo;
import com.blackstrawai.ap.billsinvoice.InvoiceVo;
import com.blackstrawai.ap.dropdowns.BasicCurrencyVo;
import com.blackstrawai.ap.dropdowns.BasicVoucherEntriesVo;
import com.blackstrawai.ap.dropdowns.TDSVo;
import com.blackstrawai.ap.payment.noncore.PaymentNonCoreBaseVo;
import com.blackstrawai.ap.payment.noncore.PaymentNonCoreCustomTableVo;
import com.blackstrawai.ap.payment.noncore.PaymentNonCoreVo;
import com.blackstrawai.ar.ArInvoiceDao;
import com.blackstrawai.ar.CreditNotesDao;
import com.blackstrawai.ar.ReceiptConstants;
import com.blackstrawai.ar.ReceiptDao;
import com.blackstrawai.ar.creditnotes.CreditNotesVo;
import com.blackstrawai.ar.invoice.ArInvoiceProductVo;
import com.blackstrawai.ar.invoice.ArInvoiceTaxDistributionVo;
import com.blackstrawai.ar.invoice.ArInvoiceVo;
import com.blackstrawai.ar.receipt.ReceiptBulkDetailsVo;
import com.blackstrawai.ar.receipt.ReceiptVo;
import com.blackstrawai.ar.receipt.VendorRefundReceiptDetailsVo;
import com.blackstrawai.banking.BankMasterDao;
import com.blackstrawai.chartofaccounts.ChartOfAccountsConstants;
import com.blackstrawai.chartofaccounts.ChartOfAccountsDao;
import com.blackstrawai.common.BaseDao;
import com.blackstrawai.common.BaseVo;
import com.blackstrawai.common.DropDownDao;
import com.blackstrawai.common.FinanceCommonDao;
import com.blackstrawai.keycontact.CustomerDao;
import com.blackstrawai.keycontact.EmployeeDao;
import com.blackstrawai.keycontact.VendorDao;
import com.blackstrawai.onboarding.OrganizationDao;
import com.blackstrawai.payroll.PayItemDao;
import com.blackstrawai.payroll.payrun.PayRunTableVo;
import com.blackstrawai.payroll.payrun.PayRunVo;
import com.blackstrawai.settings.CurrencyDao;
import com.blackstrawai.settings.CurrencyVo;
import com.blackstrawai.settings.ProductDao;
import com.blackstrawai.settings.ProductVo;
import com.blackstrawai.settings.TaxRateTypeVo;
import com.blackstrawai.settings.chartofaccounts.MinimalChartOfAccountsVo;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

@Repository
public class GeneralLedgersDao extends BaseDao{

	private Logger logger = Logger.getLogger(GeneralLedgersDao.class);

	@Autowired
	private  ChartOfAccountsDao chartOfAccountsDao;
	
	@Autowired 
	private ProductDao productDao;
	
	@Autowired
	private CustomerDao customerDao;
	
	@Autowired
	private OrganizationDao organizationDao;
	
	@Autowired
	private CurrencyDao currencyDao;
	
	@Autowired
	private EmployeeDao employeeDao;
	
	@Autowired 
	private ArInvoiceDao arInvoiceDao;
	
	@Autowired 
	private BankMasterDao bankMasterDao;
	
	@Autowired
	private VendorDao vendorDao;
	
	@Autowired
	private DropDownDao dropDownDao;
	
	@Autowired
	private FinanceCommonDao financeCommonDao;
	
	@Autowired
	private ReceiptDao receiptDao;
	
	@Autowired
	private CreditNotesDao creditNotesDao;
	
	@Autowired
	private BillsInvoiceDao billsInvoiceDao;
	
	@Autowired
	private PaymentNonCoreDao paymentNonCoreDao;
	
	@Autowired
	private PayItemDao payItemDao;
	
	
	
	public GeneralLedgerVo getGeneralLedgers(String subModule , BaseVo data) throws ApplicationException {
		List<GeneralLedgerDetailsVo> generalLedgers = new ArrayList<GeneralLedgerDetailsVo>();
		GeneralLedgerVo generalLedgerVo = new GeneralLedgerVo();
		String defaultCurrency = null;
			switch(subModule) {
			case JournalEntriesConstants.SUB_MODULE_INVOICES:
				logger.info("GL data ::" + data.toString());
				ArInvoiceVo invoiceVo = ((ArInvoiceVo) data);
				generalLedgerVo.setModuleId(invoiceVo.getInvoiceId());
				generalLedgerVo.setTxnReferenceNo(invoiceVo.getGeneralInformation().getInvoiceNoPrefix()+"/"+invoiceVo.getGeneralInformation().getInvoiceNumber()+"/"+invoiceVo.getGeneralInformation().getInvoiceNoSuffix());
				generalLedgerVo.setExchangeRate(invoiceVo.getGeneralInformation().getExchangeRate());
				generalLedgers = getARInvoiceAndGroupByLedgers(invoiceVo);
				generalLedgerVo.setVoucherNaration(invoiceVo.getGeneralInformation()!=null && invoiceVo.getGeneralInformation().getGeneralLedgerData()!=null && invoiceVo.getGeneralInformation().getGeneralLedgerData().getVoucherNaration()!=null ? invoiceVo.getGeneralInformation().getGeneralLedgerData().getVoucherNaration() : null);
				CurrencyVo currency = currencyDao.getCurrency(invoiceVo.getGeneralInformation().getCurrencyId());
				defaultCurrency = getOrgDefaultCurrency(invoiceVo.getOrgId());
				generalLedgerVo.setOrgCurrencySymbol(defaultCurrency!=null ? defaultCurrency : "INR");
				generalLedgerVo.setCurrencySymbol(currency.getAlternateSymbol());
				generalLedgerVo.setGlDetails(generalLedgers);
				break;
				case JournalEntriesConstants.SUB_MODULE_INVOICE_WITH_BILLS:
					logger.info("GL data ::" + data.toString());
					InvoiceVo apInvoiceVo = ((InvoiceVo) data);
					generalLedgerVo.setModuleId(apInvoiceVo.getId());
					generalLedgerVo.setTxnReferenceNo(apInvoiceVo.getGeneralInfo().getInvoiceNo());
					generalLedgerVo.setExchangeRate(apInvoiceVo.getTransactionDetails().getExchangeRate());
					generalLedgers = getAPInvoiceAndGroupByLedgers(apInvoiceVo);
					generalLedgerVo.setVoucherNaration(apInvoiceVo.getGeneralInfo()!=null && apInvoiceVo.getGeneralLedgerData()!=null && apInvoiceVo.getGeneralLedgerData().getVoucherNaration()!=null ? apInvoiceVo.getGeneralLedgerData().getVoucherNaration() : null);
					CurrencyVo curency = currencyDao.getCurrency(apInvoiceVo.getTransactionDetails().getCurrencyid());
					generalLedgerVo.setCurrencySymbol(curency.getAlternateSymbol());
					defaultCurrency = getOrgDefaultCurrency(apInvoiceVo.getOrganizationId());
					generalLedgerVo.setOrgCurrencySymbol(defaultCurrency!=null ? defaultCurrency : "INR");
					generalLedgerVo.setGlDetails(generalLedgers);
					break;
				case JournalEntriesConstants.SUB_MODULE_INVOICE_WITHOUT_BILLS:
					break;
						
				case JournalEntriesConstants.VOUCHER_TYPE_ACCOUNTING_ASPECTS:
					break;
				
				case JournalEntriesConstants.VOUCHER_TYPE_CONTRA:
					break;
					
				case JournalEntriesConstants.SUB_MODULE_RECEIPTS:
					logger.info("GL data ::" + data.toString()); 
					ReceiptVo receiptVo =  (ReceiptVo) data;
					generalLedgerVo.setModuleId(receiptVo.getId());
					generalLedgerVo.setTxnReferenceNo(receiptVo.getReceiptNoPrefix()+"/"+receiptVo.getReceiptNo() +"/"+ receiptVo.getReceiptNoSuffix());
					generalLedgers = getReceiptsAndGroupByLedgers(receiptVo);
					generalLedgerVo.setVoucherNaration(receiptVo!=null && receiptVo.getGeneralLedgerData()!=null  ? receiptVo.getGeneralLedgerData().getVoucherNaration() : null);
					generalLedgerVo.setExchangeRate(receiptVo.getExchangeRate() !=  null && !receiptVo.getExchangeRate().isEmpty() ? Double.valueOf(receiptVo.getExchangeRate()) :1.00 );
					CurrencyVo cur = currencyDao.getCurrency(receiptVo.getCurrencyId());
					generalLedgerVo.setCurrencySymbol(cur.getAlternateSymbol());
					defaultCurrency = getOrgDefaultCurrency(receiptVo.getOrganizationId());
					generalLedgerVo.setOrgCurrencySymbol(defaultCurrency!=null ? defaultCurrency : "INR");
					generalLedgerVo.setGlDetails(generalLedgers
					);
					break;
					
				case JournalEntriesConstants.SUB_MODULE_REFUNDS:
					break;
					
				case JournalEntriesConstants.SUB_MODULE_CREDIT_NOTES:
					logger.info("GL data ::" + data.toString());
					CreditNotesVo creditNoteVo = (CreditNotesVo) data;
					ArInvoiceVo invoice = arInvoiceDao.getInvoiceById(Integer.valueOf( creditNoteVo.getOriginalInvoiceId()));
					generalLedgerVo.setModuleId(creditNoteVo.getId());
					generalLedgerVo.setTxnReferenceNo(creditNoteVo.getCreditNoteNumberPrefix()+"/"+creditNoteVo.getCreditNoteNumber()+"/"+creditNoteVo.getCreditNoteNumberSuffix());
					generalLedgers = getCreditNotesAndGroupByLedgers(creditNoteVo , invoice);
					generalLedgerVo.setVoucherNaration(creditNoteVo!=null && creditNoteVo.getGeneralLedgerData()!=null  ? creditNoteVo.getGeneralLedgerData().getVoucherNaration() : null);
					CurrencyVo currncy = currencyDao.getCurrency(invoice.getGeneralInformation().getCurrencyId());
					generalLedgerVo.setExchangeRate(creditNoteVo.getExchangeRate() !=  null ? Double.valueOf(creditNoteVo.getExchangeRate()) : invoice.getGeneralInformation().getExchangeRate() );
					generalLedgerVo.setCurrencySymbol(currncy.getAlternateSymbol());
					defaultCurrency = getOrgDefaultCurrency(creditNoteVo.getOrganizationId());
					generalLedgerVo.setOrgCurrencySymbol(defaultCurrency!=null ? defaultCurrency : "INR");
					generalLedgerVo.setGlDetails(generalLedgers);
					break;
					
				case JournalEntriesConstants.SUB_MODULE_APPLICATION_OF_FUNDS:
					break;
				case JournalEntriesConstants.SUB_MODULE_PAY_RUN:
					logger.info("GL data ::" + data.toString()); 
					PayRunVo payRunVo =  (PayRunVo) data;
					generalLedgerVo.setModuleId(payRunVo.getPayRunId());
					Connection connection = getUserMgmConnection();
					BasicVoucherEntriesVo payRunVoucher = organizationDao.getBasicVoucherEntries(payRunVo.getOrgId(), connection,	PaymentNonCoreConstants.PAYROLL_PAYRUN);
					closeResources(null, null, connection);
					generalLedgerVo.setTxnReferenceNo(payRunVoucher.getPrefix()+"/"+payRunVo.getPayrunReference()+"/"+payRunVoucher.getSuffix());
					generalLedgers = getPayRunAndGroupByLedgers(payRunVo);
					generalLedgerVo.setVoucherNaration(payRunVo!=null && payRunVo.getGeneralLedgerData()!=null  ? payRunVo.getGeneralLedgerData().getVoucherNaration() : null);
					generalLedgerVo.setExchangeRate(1.00 );
					generalLedgerVo.setCurrencySymbol("INR");
					defaultCurrency = getOrgDefaultCurrency(payRunVo.getOrgId());
					generalLedgerVo.setOrgCurrencySymbol(defaultCurrency!=null ? defaultCurrency : "INR");
					generalLedgerVo.setGlDetails(generalLedgers);
					break;
				case JournalEntriesConstants.SUB_MODULE_PAYMENTS:
					logger.info("GL data ::" + data.toString()); 
					PaymentNonCoreVo payments =  (PaymentNonCoreVo) data;
					generalLedgerVo.setModuleId(payments.getId());
					/*
					 * Connection con = getUserMgmConnection(); BasicVoucherEntriesVo paymentVoucher
					 * = organizationDao.getBasicVoucherEntries(payments.getOrganizationId(), con,
					 * PaymentNonCoreConstants.PAYMENTS); closeResources(null, null, con);
					 */
					generalLedgerVo.setTxnReferenceNo(payments.getPaymentRefNo());
					generalLedgers = getPaymentsAndGroupByLedgers(payments);
					generalLedgerVo.setVoucherNaration(payments!=null && payments.getGeneralLedgerData()!=null  ? payments.getGeneralLedgerData().getVoucherNaration() : null);
					generalLedgerVo.setExchangeRate(payments.getExchangeRate() !=  null && !payments.getExchangeRate().isEmpty() ? Double.valueOf(payments.getExchangeRate()) :1.00 );
					generalLedgerVo.setCurrencySymbol(payments.getCurrencyCode());
					defaultCurrency = getOrgDefaultCurrency(payments.getOrganizationId());
					generalLedgerVo.setOrgCurrencySymbol(defaultCurrency!=null ? defaultCurrency : "INR");
					generalLedgerVo.setGlDetails(generalLedgers);
					break;
			}
			
			logger.info("GL generated data :::"+ generalLedgerVo);
			return generalLedgerVo;
		}
		
		
		
	
	
	
	
	private List<GeneralLedgerDetailsVo> getPayRunAndGroupByLedgers(PayRunVo payRunVo) throws ApplicationException {
		List<GeneralLedgerDetailsVo> generalLedgers = new ArrayList<GeneralLedgerDetailsVo>();
		logger.info("To method getPaymentsAndGroupByLedgers with Id" +payRunVo);
		//Map<String , GeneralLedgerDetailsVo> payItemsMap = new HashMap<String, GeneralLedgerDetailsVo>();
		Map<String , String > payItemLegders = payItemDao.getPayItemLedgerByOrgId(payRunVo.getOrgId());
		logger.info("items ::"+ payItemLegders);
		Map<String , Map<String , String >> employeePayInfoMap = new HashMap<String, Map<String,String>>();
		DecimalFormat df = new DecimalFormat("0.00");
		Map<String , Map<String , String >> glMap  = new HashMap<String, Map<String,String>>();
		GeneralLedgerVo glData = payRunVo.getGeneralLedgerData()!=null ?  payRunVo.getGeneralLedgerData() : null;
		
		try{
			if(payRunVo!=null && payRunVo.getSettingsData()!=null && payRunVo.getSettingsData().size()>0 && payItemLegders!=null && payItemLegders.size()>0) {
				// To retrive the payment info 
				ObjectMapper mapper = new ObjectMapper();
				String jsn = mapper.writeValueAsString(payRunVo.getPayRunInformation());
				JsonNode rootNode = mapper.readTree(jsn);
				//logger.info("rootNode::"+rootNode.toString());
				if(rootNode.isArray()) {
				ArrayNode arrayNode = (ArrayNode) rootNode;
		        for(int i = 0; i < arrayNode.size(); i++) {
					Map<String , String > payRunInfoMap = new HashMap<String, String>();
		            JsonNode arrayElement = arrayNode.get(i);
		         //   logger.info("arrayElement::"+ arrayElement.toString());
		            Iterator<Map.Entry<String, JsonNode>> fields = arrayElement.fields();
					while(fields.hasNext()) {
					    Map.Entry<String, JsonNode> field = fields.next();
					  //  logger.info(field.getKey() + " = " + field.getValue().asText());
					    if(field.getKey()!=null && field.getValue()!=null ) {
					    	payRunInfoMap.put(field.getKey(), field.getValue().asText());
					    }
					}
					logger.info("Payment Info::"+ payRunInfoMap);
					String employeeId = payRunInfoMap.size()> 0 && payRunInfoMap.containsKey("employeeId") ? payRunInfoMap.get("employeeId") : "0";
					employeePayInfoMap.put(employeeId, payRunInfoMap);
					if(glData!=null && glData.getGlDetails()!=null && glData.getGlDetails().size()>0) {
						Map <String , String > selectedLedgerMap = new HashMap<String, String>();
						for(GeneralLedgerDetailsVo detail : glData.getGlDetails()) {
							if(detail.getTempId().equals(Integer.valueOf(employeeId))) {
								selectedLedgerMap.put(detail.getDescription(), detail.getAccount()+"~"+ detail.getAccountId());
							}
						}
						glMap.put(employeeId, selectedLedgerMap);
					}
		        }
				}
				logger.info("employeePayInfoMap Info::"+ employeePayInfoMap);
				logger.info("glMap:::"+ glMap);
				//List<MinimalChartOfAccountsVo> earningsLedger = chartOfAccountsDao.getLedgersByEntity(payRunVo.getOrgId(), ChartOfAccountsConstants.ENTITY_EMPLOYEE_EXPENSES);
				List<MinimalChartOfAccountsVo> deductionLedger = chartOfAccountsDao.getLedgersByMultipleEntity(payRunVo.getOrgId(), Arrays.asList(ChartOfAccountsConstants.ENTITY_EMPLOYEE_EXPENSES,ChartOfAccountsConstants.ENTITY_EMPLOYEE_PAYABLE , ChartOfAccountsConstants.ENTITY_EMPLOYEE_RECEIVABLE,
						ChartOfAccountsConstants.ENTITY_ESIC_LIABILITY , ChartOfAccountsConstants.ENTITY_P_TAX_LIABILITY,ChartOfAccountsConstants.ENTITY_PF_LIABILITY,ChartOfAccountsConstants.ENTITY_SALARY_PAYABLE,ChartOfAccountsConstants.ENTITY_TDS_PAYABLE));
				deductionLedger.addAll(chartOfAccountsDao.getLedgerByName( payRunVo.getOrgId() ,"Power and fuel"));
				deductionLedger.addAll(chartOfAccountsDao.getLedgerByName(  payRunVo.getOrgId() ,"Travelling and conveyance"));
				deductionLedger.addAll(chartOfAccountsDao.getLedgerByName(  payRunVo.getOrgId() ,"Telephone charges"));
				if(employeePayInfoMap.size()>0) {
					for(Entry< String, Map<String,String>> empMap :employeePayInfoMap.entrySet() ) { 
						String employeeId =  empMap.getKey();
						Map<String , String > payInfoMap = empMap.getValue();
						String employeeName = payInfoMap.size()> 0 && payInfoMap.containsKey("employeeName") ? payInfoMap.get("employeeName") :null;
						Map<String , String > glPayInfoMap = glMap!=null && glMap.size()>0 && glMap.containsKey(employeeId) ? glMap.get(employeeId) : null;

							for(PayRunTableVo table :  payRunVo.getSettingsData()) {
							//	logger.info("counter::"+ counter);
								if(table.getIsShowColumn() && payInfoMap.size()>0) {
									if(JournalEntriesConstants.PAY_RUN_TYPE_EARNINGS.equals(table.getPayType()) ) {
										//Earnings related Payments GL 
										if(table.getFinalName()!=null && payInfoMap.containsKey(table.getFinalName())  && !payInfoMap.get(table.getFinalName()).equals("0")) {
										//	logger.info("Table Name ::+"+ table.getName());
											GeneralLedgerDetailsVo gl = new GeneralLedgerDetailsVo();
											gl.setType(JournalEntriesConstants.PAY_RUN_TYPE_EARNINGS);
											gl.setDescription(table.getName());
											gl.setTempId(Integer.valueOf(employeeId));
											if(glPayInfoMap!=null && glPayInfoMap.containsKey(table.getName())) {
												Integer id = Integer.valueOf(glPayInfoMap.get(table.getName()).split("~")[1]);
												String name = chartOfAccountsDao.getLedgerName(id, payRunVo.getOrgId());
												gl.setAccount(name!=null ? name : glPayInfoMap.get(table.getName()).split("~")[0]);
												gl.setAccountId(id);
											}else {
											if(payItemLegders.containsKey(table.getName())) {
											//	logger.info("Ledger ::"+ payItemLegders.get(table.getName()));
												gl.setAccount(payItemLegders.get(table.getName()).split("~")[0]);
												gl.setAccountId(Integer.valueOf(payItemLegders.get(table.getName()).split("~")[1]));
			
											}
											}
												gl.setLedgerList(deductionLedger);
												gl.setSubLedger(employeeName);
												
											Double amntPaid = Double.valueOf(payInfoMap.get(table.getFinalName()));
											Double xchangeRate = 1.00;
											if(amntPaid > 0 ) {
												gl.setFcyDebit(Double.valueOf(amntPaid));
												gl.setInrDebit(Double.valueOf(df.format(xchangeRate * amntPaid)));
											}else {
												gl.setFcyCredit(Double.valueOf(amntPaid * -1));
												gl.setInrCredit(Double.valueOf(df.format(xchangeRate * amntPaid * -1)));
											}
											
											generalLedgers.add(gl);
										}
									}
											
								if(JournalEntriesConstants.PAY_RUN_TYPE_DEDUCTIONS.equals(table.getPayType())) {
									//Deductions  related Payments GL 
									if(table.getFinalName()!=null && payInfoMap.containsKey(table.getFinalName()) && !payInfoMap.get(table.getFinalName()).equals("0")) {
									//	logger.info("Table Name ::+"+ table.getName());
										GeneralLedgerDetailsVo gl = new GeneralLedgerDetailsVo();
										gl.setType(JournalEntriesConstants.PAY_RUN_TYPE_DEDUCTIONS);
										gl.setDescription(table.getName());
										gl.setTempId(Integer.valueOf(employeeId));
										if(glPayInfoMap!=null && glPayInfoMap.containsKey(table.getName())) {
											int id = Integer.valueOf(glPayInfoMap.get(table.getName()).split("~")[1]);
											String name = chartOfAccountsDao.getLedgerName(id, payRunVo.getOrgId());
											gl.setAccount(name !=null ? name : glPayInfoMap.get(table.getName()).split("~")[0]);
											gl.setAccountId(id);
										}else {
											if(payItemLegders.containsKey(table.getName())) {
											//	logger.info("Ledger ::"+ payItemLegders.get(table.getName()));
												gl.setAccount(payItemLegders.get(table.getName()).split("~")[0]);
												gl.setAccountId(Integer.valueOf(payItemLegders.get(table.getName()).split("~")[1]));
											 }
										}
											gl.setLedgerList(deductionLedger);
											gl.setSubLedger(employeeName);
										
										Double amntPaid = Double.valueOf(payInfoMap.get(table.getFinalName()));
										Double xchangeRate = 1.00;
										if(amntPaid>0) {
											gl.setFcyCredit(Double.valueOf(amntPaid));
											gl.setInrCredit(Double.valueOf(df.format(xchangeRate * amntPaid)));
										}else {
											gl.setFcyDebit(Double.valueOf(amntPaid  * -1));
											gl.setInrDebit(Double.valueOf(df.format(xchangeRate * amntPaid * -1)));
										}
										generalLedgers.add(gl);
									}
								
								}
							}
						}
				

					//Employee  related Payments GL 
					if(employeeId!=null && !employeeId.equals("0") ) {
						GeneralLedgerDetailsVo gl = new GeneralLedgerDetailsVo();
						gl.setType(JournalEntriesConstants.SUBLEDGER_TYPE_EMPLOYEE);
						gl.setDescription(JournalEntriesConstants.PAY_RUN_TYPE_NET_PAYABLE);
						gl.setTempId(Integer.valueOf(employeeId));
						if(glPayInfoMap!=null && glPayInfoMap.containsKey(JournalEntriesConstants.PAY_RUN_TYPE_NET_PAYABLE)) {
							int id = Integer.valueOf(glPayInfoMap.get(JournalEntriesConstants.PAY_RUN_TYPE_NET_PAYABLE).split("~")[1]);
							String name = chartOfAccountsDao.getLedgerName(id, payRunVo.getOrgId());
							gl.setAccountId(id);
							gl.setAccount(name !=null ? name : glPayInfoMap.get(JournalEntriesConstants.PAY_RUN_TYPE_NET_PAYABLE).split("~")[0]);
						}else {
						String accountName = chartOfAccountsDao.getDefaultLedgerByMultipleEntity(Arrays.asList(ChartOfAccountsConstants.ENTITY_EMPLOYEE_PAYABLE , ChartOfAccountsConstants.ENTITY_SALARY_PAYABLE , ChartOfAccountsConstants.ENTITY_EMPLOYEE_PROVISION), ChartOfAccountsConstants.MODULE_PAYROLL, ChartOfAccountsConstants.FIELD_EMPLOYEE_PAYRUN);
						gl.setAccount(accountName);
						gl.setAccountId(chartOfAccountsDao.getLedgerIdGivenName(accountName, payRunVo.getOrgId()));
						}
						gl.setLedgerList(chartOfAccountsDao.getLedgersByMultipleEntity(payRunVo.getOrgId(), Arrays.asList(ChartOfAccountsConstants.ENTITY_EMPLOYEE_PAYABLE , ChartOfAccountsConstants.ENTITY_SALARY_PAYABLE , ChartOfAccountsConstants.ENTITY_EMPLOYEE_PROVISION)));
						gl.setSubLedger(employeeName);
						
						Double amntPaid = Double.valueOf(payInfoMap.get(JournalEntriesConstants.PAY_RUN_TYPE_NET_PAY));
						Double xchangeRate = 1.00;
						gl.setFcyCredit(Double.valueOf(amntPaid));
						gl.setInrCredit(Double.valueOf(df.format(xchangeRate * amntPaid)));
						generalLedgers.add(gl);
					}
				}
			}
		  }
			logger.info("GL Size::"+generalLedgers.size());
		}catch (Exception e) {
			logger.info("Error in getPaymentsAndGroupByLedgers:: ",e);
			e.printStackTrace();
			throw new ApplicationException(e);
		}
		
		return generalLedgers;
	}









	private List<GeneralLedgerDetailsVo> getAPInvoiceAndGroupByLedgers(InvoiceVo apInvoiceVo) throws ApplicationException {

		List<GeneralLedgerDetailsVo> generalLedgers = new ArrayList<GeneralLedgerDetailsVo>();
		logger.info("To method getAPInvoiceAndGroupByLedgers with Id" +apInvoiceVo);
		try {
			if(apInvoiceVo!=null && apInvoiceVo.getTransactionDetails()!=null && apInvoiceVo.getTransactionDetails().getProducts()!=null && apInvoiceVo.getTransactionDetails().getProducts().size()>0) {
				GeneralLedgerVo glVo =  apInvoiceVo.getGeneralLedgerData();
				logger.info("glvo"+glVo);
				DecimalFormat df = new DecimalFormat("0.00");
				Map<Integer , String > discountMap = new HashMap<Integer, String>();
				Map<Integer , String > adjustmentMap = new HashMap<Integer, String>();
				Map<Integer , String > tdsMap = new HashMap<Integer, String>();
				Map<Integer , String > vendorMap = new HashMap<Integer, String>();
				Map<String , String > gstMap = new HashMap<String, String>();
				Double exchangeRate = apInvoiceVo.getTransactionDetails().getExchangeRate()!=null ? apInvoiceVo.getTransactionDetails().getExchangeRate() : 1.00;

				if(glVo!=null && glVo.getGlDetails()!=null && glVo.getGlDetails().size()>0 ) {
					logger.info("glvo"+glVo);
				for(GeneralLedgerDetailsVo details :  apInvoiceVo.getGeneralLedgerData().getGlDetails()){
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
					if(details.getType()!=null && details.getType().equals(JournalEntriesConstants.SUBLEDGER_TYPE_VENDOR)) {
						vendorMap.put(details.getAccountId(), details.getAccount());
					}
					if(details.getType()!=null && details.getType().equals(JournalEntriesConstants.SUBLEDGER_TYPE_GST)) {
						gstMap.put(details.getSubLedger() ,details.getAccountId()+"~"+ details.getAccount());
					}
					
				}
				}
				//To separate the ledger of each products
				for(InvoiceProductVo product :  apInvoiceVo.getTransactionDetails().getProducts()){
					logger.info("To get the products"+product);
					if(product.getProductId()!=null && !product.getStatus().equals("DEL")) {
						ProductVo prodVo = productDao.getProductById(product.getProductId());
						GeneralLedgerDetailsVo gl = new GeneralLedgerDetailsVo();
						gl.setType(JournalEntriesConstants.TYPE_PRODUCTS);
						gl.setTempId(product.getTempId());
						gl.setDescription(prodVo.getName());
						if(product.getProductAccountName()!=null && product.getProductAccountId()!=null && product.getProductAccountId()!=0) {
							gl.setAccount(chartOfAccountsDao.getLedgerName(product.getProductAccountId(),  apInvoiceVo.getOrganizationId()));
							gl.setAccountId(product.getProductAccountId());
							gl.setLedgerList(chartOfAccountsDao.getItemsPurchaseAccounts(prodVo.getType() , apInvoiceVo.getOrganizationId()));
						}else {
							gl.setAccount(prodVo.getPurchaseAccountName());
							gl.setAccountId(prodVo.getPurchaseAccountId());
							if(prodVo.getPurchaseAccountName()!=null) {
							gl.setLedgerList(chartOfAccountsDao.getItemsPurchaseAccounts(prodVo.getType() , apInvoiceVo.getOrganizationId()));
							}
						}
						gl.setProductId(prodVo.getId());
						gl.setSubLedger(prodVo.getProductId());
						gl.setFcyDebit(Double.valueOf(df.format( product.getAmount())));
						Double exrate = apInvoiceVo.getTransactionDetails().getExchangeRate();
						gl.setInrDebit(exrate!=null ? Double.valueOf(df.format(exrate * product.getAmount())) : 1 * product.getAmount());
						generalLedgers.add(gl);
					}
				}
				
				
				
				
				// To get the vendor Id and its selected vendor account 
				if(apInvoiceVo.getGeneralInfo().getVendorId()!=null ) {
					logger.info("To get the customers");
					GeneralLedgerDetailsVo gl = new GeneralLedgerDetailsVo();
					gl.setDescription(JournalEntriesConstants.SUBLEDGER_TYPE_VENDOR);
					gl.setType(JournalEntriesConstants.SUBLEDGER_TYPE_VENDOR);
					if(vendorMap.size()>0){
						for(Map.Entry<Integer, String> entryset : vendorMap.entrySet()) {
							String name = chartOfAccountsDao.getLedgerName(entryset.getKey(), apInvoiceVo.getOrganizationId());
							gl.setAccount(name!=null ? name : entryset.getValue());
							gl.setAccountId(entryset.getKey());
						}
					}else {
						String ledgerName = null;
						String vendorBaseCurrency = vendorDao.getVendorCurrencySymbol(apInvoiceVo.getGeneralInfo().getVendorId());
						if("INR".equals(vendorBaseCurrency)) {
							ledgerName =  chartOfAccountsDao.getDefaultLedger(ChartOfAccountsConstants.ENTITY_VENDOR, null, null);
						}else {
							ledgerName =  chartOfAccountsDao.getDefaultLedger(ChartOfAccountsConstants.ENTITY_VENDOR, null, null);
						}
						
						int ledgerId = chartOfAccountsDao.getLedgerIdGivenName(ledgerName , apInvoiceVo.getOrganizationId());
						gl.setAccount(ledgerName);
						gl.setAccountId(ledgerId);
					}
					gl.setSubLedger(vendorDao.getVendorName(apInvoiceVo.getGeneralInfo().getVendorId()));
					gl.setFcyCredit(Double.valueOf(df.format(apInvoiceVo.getTransactionDetails().getTotal())));
					gl.setInrCredit(Double.valueOf(df.format(exchangeRate * apInvoiceVo.getTransactionDetails().getTotal())));
					gl.setLedgerList(chartOfAccountsDao.getLedgersByEntity(apInvoiceVo.getOrganizationId(), ChartOfAccountsConstants.ENTITY_VENDOR));
					generalLedgers.add(gl);
				}
				
				
				HashMap<String , String> debitLedgerMap  = new HashMap<String, String>();
				for(InvoiceProductVo product :  apInvoiceVo.getTransactionDetails().getProducts()) {
						//To separate GST ledger
					if(product.getStatus()!=null && !"DEL".equals(product.getStatus()) && product.getTaxDetails()!=null && product.getTaxDetails().getTaxDistribution()!=null) {
						for(InvoiceTaxDistributionVo taxdetails : product.getTaxDetails().getTaxDistribution()) {
							if(taxdetails.getTaxName()!=null && taxdetails.getTaxRate()!=null && taxdetails.getTaxAmount()!=null) {
								logger.info("To get the gst");
								String opLedger = chartOfAccountsDao.getDefaultLedger(ChartOfAccountsConstants.ENTITY_GST_RECEIVABLE_PAYABLE, ChartOfAccountsConstants.MODULE_AP, null);
								Integer opLedgerId = chartOfAccountsDao.getLedgerIdGivenName(opLedger, apInvoiceVo.getOrganizationId());
								if(debitLedgerMap.containsKey(opLedger+"~"+taxdetails.getTaxName()+"~"+taxdetails.getTaxRate())) {
									String[] values= debitLedgerMap.get(opLedger+"~"+taxdetails.getTaxName()+"~"+taxdetails.getTaxRate()).split("~");
									String amnt = values[0];
									Double amount = Double.valueOf(amnt);
									amount = amount + taxdetails.getTaxAmount();
									debitLedgerMap.put(opLedger+"~"+taxdetails.getTaxName()+"~"+taxdetails.getTaxRate(), amount+"~"+values[1]);
								}else {
									debitLedgerMap.put(opLedger+"~"+taxdetails.getTaxName()+"~"+taxdetails.getTaxRate(), taxdetails.getTaxAmount()+"~"+opLedgerId);
								}
							}
						}
					}
				}
						if(debitLedgerMap.size()>0) {

							for (Map.Entry<String,String> entry : debitLedgerMap.entrySet()) {
								GeneralLedgerDetailsVo gl = new GeneralLedgerDetailsVo();
								gl.setType(JournalEntriesConstants.SUBLEDGER_TYPE_GST);
								gl.setDescription(JournalEntriesConstants.SUBLEDGER_TYPE_GST);
								if(gstMap.size()>0 && gstMap.containsKey(entry.getKey().split("~")[1])) {
									String gstVal = gstMap.get(entry.getKey().split("~")[1]);
									int id = gstVal.split("~")[0]!=null ?Integer.valueOf(gstVal.split("~")[0]) : 0;
									String mapLedName = gstVal.split("~")[1]!=null ? gstVal.split("~")[1] : null;
									String name = chartOfAccountsDao.getLedgerName(id, apInvoiceVo.getOrganizationId());
									gl.setAccountId(id);
									gl.setAccount(name!=null ? name : mapLedName);
									
								}else {
									gl.setAccount(entry.getKey().split("~")[0]);
									gl.setAccountId(entry.getValue().split("~")[1]!=null ? Integer.valueOf(entry.getValue().split("~")[1]) : 0);
								}
								gl.setSubLedger(entry.getKey().split("~")[1]);
								gl.setFcyDebit(entry.getValue().split("~")[0]!=null ? Double.valueOf(entry.getValue().split("~")[0]) : 0.00);
								gl.setInrDebit(Double.valueOf(df.format(exchangeRate * gl.getFcyDebit() )));
								gl.setLedgerList(chartOfAccountsDao.getLedgersByEntity(apInvoiceVo.getOrganizationId(), ChartOfAccountsConstants.ENTITY_GST_RECEIVABLE_PAYABLE));
								generalLedgers.add(gl);
							}
						}
				
					//To separate the ledger of TDS
				if(apInvoiceVo.getTransactionDetails().getTdsId()!=null && apInvoiceVo.getTransactionDetails().getTdsId()!=0 && apInvoiceVo.getTransactionDetails().getTdsValue()!=null && !apInvoiceVo.getTransactionDetails().getTdsValue().equals(0.0)) {
					logger.info("To get the TDS");
					Connection con = getFinanceCommon();
					Map<Integer, String> tdsTypeMap = financeCommonDao.getTDS(con).stream().collect(Collectors.toMap(TDSVo::getId, TDSVo::getTdsRateIdentifier));
					closeResources(null, null, con);
					GeneralLedgerDetailsVo gl = new GeneralLedgerDetailsVo();
					gl.setDescription(JournalEntriesConstants.TYPE_TDS);
					gl.setType(JournalEntriesConstants.TYPE_TDS);
					String tdsLedger =chartOfAccountsDao.getDefaultLedger(ChartOfAccountsConstants.ENTITY_TDS_PAYABLE, ChartOfAccountsConstants.MODULE_AP, null);
					Integer tdsLedgerId = chartOfAccountsDao.getLedgerIdGivenName(tdsLedger, apInvoiceVo.getOrganizationId());
					if(tdsMap.size()>0) {
						for(Map.Entry<Integer, String> entryset : tdsMap.entrySet()) {
							String name = chartOfAccountsDao.getLedgerName(entryset.getKey(), apInvoiceVo.getOrganizationId());
							gl.setAccount(name!=null ? name : entryset.getValue());
							gl.setAccountId(entryset.getKey());
						}
						
					}else {
						gl.setAccount(tdsLedger);
						gl.setAccountId(tdsLedgerId);
					}
					gl.setSubLedger(tdsTypeMap.containsKey(apInvoiceVo.getTransactionDetails().getTdsId()) ? tdsTypeMap.get(apInvoiceVo.getTransactionDetails().getTdsId()) : null);
					gl.setFcyCredit(apInvoiceVo.getTransactionDetails().getTdsValue());
					gl.setInrCredit(Double.valueOf(df.format((exchangeRate * apInvoiceVo.getTransactionDetails().getTdsValue() ))));
					gl.setLedgerList(chartOfAccountsDao.getLedgersByEntity(apInvoiceVo.getOrganizationId(), ChartOfAccountsConstants.ENTITY_TDS_PAYABLE));
					generalLedgers.add(gl);
					
				}
				
				
				//To separate the ledger of Discount 
				if(apInvoiceVo.getTransactionDetails().getDiscountValue()!=null && !apInvoiceVo.getTransactionDetails().getDiscountValue().equals(0.0)) {
					String ledgerName = chartOfAccountsDao.getDefaultLedger(ChartOfAccountsConstants.ENTITY_DISCOUNT_RECEIVED, ChartOfAccountsConstants.MODULE_AP, ChartOfAccountsConstants.FIELD_DISCOUNT);
					int ledgerId = chartOfAccountsDao.getLedgerIdGivenName(ledgerName ,apInvoiceVo.getOrganizationId());
					logger.info("To get the discount");
					GeneralLedgerDetailsVo gl = new GeneralLedgerDetailsVo();
					gl.setDescription(JournalEntriesConstants.TYPE_DISCOUNT);
					gl.setType(JournalEntriesConstants.TYPE_DISCOUNT);
					if(discountMap.size()>0) {
						for(Map.Entry<Integer, String> entryset : discountMap.entrySet()) {
							String name = chartOfAccountsDao.getLedgerName(entryset.getKey(), apInvoiceVo.getOrganizationId());
							gl.setAccount(name!=null ? name : entryset.getValue());
							gl.setAccountId(entryset.getKey());
						}
					}else {
						gl.setAccount(ledgerName);
						gl.setAccountId(ledgerId);
					}
					gl.setFcyCredit(apInvoiceVo.getTransactionDetails().getDiscountAmount());
					gl.setInrCredit(Double.valueOf(df.format(exchangeRate * apInvoiceVo.getTransactionDetails().getDiscountAmount())));
					gl.setLedgerList(chartOfAccountsDao.getLedgersByMultipleEntity(apInvoiceVo.getOrganizationId(), Arrays.asList(ChartOfAccountsConstants.ENTITY_DISCOUNT_RECEIVED , ChartOfAccountsConstants.ENTITY_PURCHASE)));
					generalLedgers.add(gl);
					
				}
				
				
				//To separate the ledger of Adjustment 
				if( apInvoiceVo.getTransactionDetails().getAdjustment()!=null && !apInvoiceVo.getTransactionDetails().getAdjustment().equals(0.0)) {
					logger.info("To get the adjustment");
					String adjustLedger = chartOfAccountsDao.getDefaultLedger(ChartOfAccountsConstants.ENTITY_PURCHASE, ChartOfAccountsConstants.MODULE_AP, ChartOfAccountsConstants.FIELD_ADJUSTMENTS);
					int ledgerId = chartOfAccountsDao.getLedgerIdGivenName(adjustLedger , apInvoiceVo.getOrganizationId());
					GeneralLedgerDetailsVo gl = new GeneralLedgerDetailsVo();
					gl.setDescription(JournalEntriesConstants.TYPE_ADJUSTMENT);
					if(adjustmentMap.size()>0) {
						for(Map.Entry<Integer, String> entryset : adjustmentMap.entrySet()) {
							String name = chartOfAccountsDao.getLedgerName(entryset.getKey(), apInvoiceVo.getOrganizationId());
							gl.setAccount(name!=null ? name : entryset.getValue());
							gl.setAccountId(entryset.getKey());
						}
					}else {
						gl.setAccount(adjustLedger);
						gl.setAccountId(ledgerId);
					}
					gl.setType(JournalEntriesConstants.TYPE_ADJUSTMENT);
					if(apInvoiceVo.getTransactionDetails().getAdjustment() > 0) {
						gl.setFcyDebit(Math.abs(apInvoiceVo.getTransactionDetails().getAdjustment()));
						gl.setInrDebit(Double.valueOf(df.format(exchangeRate * Math.abs(apInvoiceVo.getTransactionDetails().getAdjustment()) )));
					}else {
							gl.setFcyCredit(Math.abs(apInvoiceVo.getTransactionDetails().getAdjustment()) );
							gl.setInrCredit(Double.valueOf(df.format(exchangeRate * Math.abs(apInvoiceVo.getTransactionDetails().getAdjustment()) )));
					}
					gl.setLedgerList(chartOfAccountsDao.getLedgersByMultipleEntity(apInvoiceVo.getOrganizationId(), Arrays.asList(ChartOfAccountsConstants.ENTITY_ROUND_OFF , ChartOfAccountsConstants.ENTITY_PURCHASE)));
					generalLedgers.add(gl);
				}
				
			logger.info("Ledgers Seaparated to GeneralLedgerVo::" + generalLedgers.size());
		}
		}catch (ApplicationException | SQLException e) {
			logger.info("Error in getInvoiceAndGroupByLedgers:: ",e);
		//	e.printStackTrace();
			throw new ApplicationException(e);
		}
		return generalLedgers;
	
	}









	private List<GeneralLedgerDetailsVo> getReceiptsAndGroupByLedgers(ReceiptVo receiptVo) throws ApplicationException {
		List<GeneralLedgerDetailsVo> generalLedgers = new ArrayList<GeneralLedgerDetailsVo>();
		logger.info("To method getPaymentsAndGroupByLedgers with Id" +receiptVo);
		try{
			if(receiptVo!=null && receiptVo.getBankId()!=null && !receiptVo.getBankId().equals("0") && receiptVo.getBankType()!=null) {
				String accountType = receiptVo.getBankType();
				logger.info("AccountType :: "+ accountType);
				
				switch(receiptVo.getReceiptType()) {
				case ReceiptConstants.RECEIPT_TYPE_OTHER_RECEIPTS: 
					// Other Receipt Type
					generalLedgers = getGLForOtherReceiptType(receiptVo , accountType);
					break;
				case ReceiptConstants.RECEIPT_TYPE_VENDOR_REFUNDS:
					// Vendor Refunds receipt type
					generalLedgers = getGLForVendorAdvanceType(receiptVo , accountType);
					break;
				case ReceiptConstants.RECEIPT_TYPE_CUSTOMER_PAYMENTS: 
					// Customer Payment type
					generalLedgers = getGLForCustomerPaymentsType(receiptVo , accountType);
					break;
				}
			}
			logger.info("GL data List :: "+generalLedgers.size());
		}catch (Exception e) {
			logger.info("Error in getPaymentsAndGroupByLedgers:: ",e);
			e.printStackTrace();
			throw new ApplicationException(e);
		}
		
		return generalLedgers;
	}









	private List<GeneralLedgerDetailsVo> getGLForCustomerPaymentsType(ReceiptVo receiptVo, String accountType) throws ApplicationException {
		List<GeneralLedgerDetailsVo> generalLedgers= new ArrayList<>();
		DecimalFormat df = new DecimalFormat("0.00");
	try { 
		GeneralLedgerVo glData = receiptVo!=null && receiptVo.getGeneralLedgerData()!=null ? receiptVo.getGeneralLedgerData() : null;
		
		Map<Integer, Integer> invoiceMap = glData != null && glData.getGlDetails() != null ? glData.getGlDetails()
				.stream().filter(detail -> JournalEntriesConstants.INVOICE.equals(detail.getType()) && detail.getAccount()!=null && detail.getProductId()!=null )
				.collect(Collectors.toMap(GeneralLedgerDetailsVo::getTempId, GeneralLedgerDetailsVo::getAccountId ))
				: null;
		Map<Integer, Integer> bankChargesMap = glData != null && glData.getGlDetails() != null ? glData.getGlDetails()
				.stream().filter(detail -> JournalEntriesConstants.BANK_CHARGES.equals(detail.getType()) && detail.getAccount()!=null && detail.getProductId()!=null)
				.collect(Collectors.toMap(GeneralLedgerDetailsVo::getTempId, GeneralLedgerDetailsVo::getAccountId ))
				: null;
		Map<Integer, Integer> tdsDeductedMap = glData != null && glData.getGlDetails() != null ? glData.getGlDetails()
				.stream().filter(detail -> JournalEntriesConstants.TDS_Deducted.equals(detail.getType()) && detail.getAccount()!=null && detail.getProductId()!=null)
				.collect(Collectors.toMap(GeneralLedgerDetailsVo::getTempId, GeneralLedgerDetailsVo::getAccountId ))
				: null;
		Map<Integer, Integer> others1Map = glData != null && glData.getGlDetails() != null ? glData.getGlDetails()
				.stream().filter(detail -> JournalEntriesConstants.OTHERS1.equals(detail.getType()) && detail.getAccount()!=null && detail.getProductId()!=null)
				.collect(Collectors.toMap(GeneralLedgerDetailsVo::getTempId, GeneralLedgerDetailsVo::getAccountId ))
				: null;
				
		Map<Integer, Integer> others2Map = glData != null && glData.getGlDetails() != null ? glData.getGlDetails()
				.stream().filter(detail -> JournalEntriesConstants.OTHERS2.equals(detail.getType()) && detail.getAccount()!=null && detail.getProductId()!=null)
				.collect(Collectors.toMap(GeneralLedgerDetailsVo::getTempId, GeneralLedgerDetailsVo::getAccountId ))
				: null;
												
		Map<Integer, Integer> others3Map = glData != null && glData.getGlDetails() != null ? glData.getGlDetails()
				.stream().filter(detail -> JournalEntriesConstants.OTHERS3.equals(detail.getType()) && detail.getAccount()!=null && detail.getProductId()!=null)
				.collect(Collectors.toMap(GeneralLedgerDetailsVo::getTempId, GeneralLedgerDetailsVo::getAccountId ))
				: null;
		Map<Integer, Integer> advanceMap = glData != null && glData.getGlDetails() != null ? glData.getGlDetails()
				.stream().filter(detail -> JournalEntriesConstants.ADVANCE.equals(detail.getType()) && detail.getAccount()!=null && detail.getProductId()!=null)
				.collect(Collectors.toMap(GeneralLedgerDetailsVo::getProductId, GeneralLedgerDetailsVo::getAccountId ))
				: null;
		Map<Integer, Integer> creditNoteMap = glData != null && glData.getGlDetails() != null ? glData.getGlDetails()
				.stream().filter(detail -> JournalEntriesConstants.SUB_MODULE_CREDIT_NOTES.equals(detail.getType()) && detail.getAccount()!=null && detail.getProductId()!=null)
				.collect(Collectors.toMap(GeneralLedgerDetailsVo::getProductId, GeneralLedgerDetailsVo::getAccountId ))
				: null;
		Map<Integer, Integer> customerMap = glData != null && glData.getGlDetails() != null ? glData.getGlDetails()
						.stream().filter(detail -> JournalEntriesConstants.SUBLEDGER_TYPE_CUSTOMER.equals(detail.getType()) && detail.getAccount()!=null && detail.getProductId()!=null )
						.collect(Collectors.toMap(GeneralLedgerDetailsVo::getTempId, GeneralLedgerDetailsVo::getAccountId ))
						: null;
				logger.info("vendorMap ::>>"+invoiceMap);
				
				
		//To get the Bank related GL details 
		if(receiptVo.getBankId() !=null && !receiptVo.getBankId().isEmpty() && !"0".equals(receiptVo.getBankId() )) {
			GeneralLedgerDetailsVo gl = new GeneralLedgerDetailsVo();
			String bankLedger =  bankMasterDao.getAccountName(Integer.valueOf(receiptVo.getBankId()), accountType);
			Integer bankLedgerId = chartOfAccountsDao.getLedgerIdGivenName(bankLedger, receiptVo.getOrganizationId());
			gl.setType(JournalEntriesConstants.SUBLEDGER_TYPE_BANKING);
			gl.setDescription(accountType);
			gl.setTempId(Integer.valueOf(receiptVo.getBankId()));
			/*if(glData!=null && glData.getGlDetails()!=null && !glData.getGlDetails().isEmpty() &&  glData.getGlDetails().get(0) !=null && JournalEntriesConstants.SUBLEDGER_TYPE_BANKING.equals(glData.getGlDetails().get(0).getType()) && glData.getGlDetails().get(0).getTempId() == Integer.valueOf(receiptVo.getBankId())) {
				gl.setAccount( glData.getGlDetails().get(0).getAccount());
				gl.setAccountId( glData.getGlDetails().get(0).getAccountId());
				gl.setLedgerList(chartOfAccountsDao.getLedgersAndItsSiblingsByLedgerName(receiptVo.getOrganizationId(), glData.getGlDetails().get(0).getAccount()));
				gl.setSubLedger(glData.getGlDetails().get(0).getSubLedger());
			}else {*/
				gl.setAccount(bankLedger);
				gl.setAccountId(bankLedgerId);
				gl.setLedgerList(chartOfAccountsDao.getLedgerById(receiptVo.getOrganizationId() , bankLedgerId));
				gl.setSubLedger(accountType);
			//}
			Double amntPaid = Double.valueOf(receiptVo.getAmount());
			gl.setFcyDebit(Double.valueOf(amntPaid));
			Double xchangeRate = receiptVo.getExchangeRate() !=null && !"".equals(receiptVo.getExchangeRate()) ? Double.valueOf(receiptVo.getExchangeRate())  : 1.00;
			gl.setInrDebit(Double.valueOf(df.format(xchangeRate * amntPaid)));
			generalLedgers.add(gl);
			}
		
		
		//To get the Vendor related GL details 
		if(receiptVo.getCustomerId()!=null && receiptVo.getCustomerId()!=0 && receiptVo.getReceiptBulkDetails()!=null && receiptVo.getReceiptBulkDetails().size()>0) {
			String customerLedger =  chartOfAccountsDao.getDefaultLedger(ChartOfAccountsConstants.ENTITY_CUSTOMER, null, null);
			Integer customerAccountId  = chartOfAccountsDao.getLedgerIdGivenName(customerLedger, receiptVo.getOrganizationId());
			if(receiptVo.getIsBulk() && receiptVo.getReceiptBulkDetails()!=null && receiptVo.getReceiptBulkDetails().size()>0) {
				List<MinimalChartOfAccountsVo> allLedgers = chartOfAccountsDao.getAllLedgersByOrgId(receiptVo.getOrganizationId());
				int counter = 1;
				for(ReceiptBulkDetailsVo vo : receiptVo.getReceiptBulkDetails()) {
					String arInvoieNo =null;
					logger.info("Counter:"+counter);
					if(vo.getTypeId()!=0) {
						switch (vo.getTypeId()) {
						case 1: // Type as invoice 
							if(vo.getReferenceId()<0) {
								arInvoieNo = receiptVo.getReceiptNoPrefix()+ "/"+receiptVo.getReceiptNo()+"/"+receiptVo.getReceiptNoSuffix();
								logger.info("Create new Advance or onAccount "+arInvoieNo);
							}else {
							Connection connection = getAccountsReceivableConnection();
							arInvoieNo  = receiptVo.getReceiptBulkDetails().get(0).getReferenceId()!=null ? 
									arInvoiceDao.getInvoiceOrderNo(receiptVo.getReceiptBulkDetails().get(0).getReferenceId(), receiptVo.getOrganizationId(), connection) : null ;
							closeResources(null, null, connection);
							}
							//For invoice 
							if(vo.getInvoiceDueAmount()!=null &&  !vo.getInvoiceDueAmount().equals("")  && !vo.getInvoiceDueAmount().equals("0") ){
							GeneralLedgerDetailsVo gl = new GeneralLedgerDetailsVo();
							gl.setTempId(counter);
							gl.setProductId(vo.getReferenceId());
							gl.setType(JournalEntriesConstants.INVOICE);
							gl.setDescription(vo.getReferenceType().replace("Create New ", "").trim());
							if(invoiceMap!=null && invoiceMap.size()>0 && invoiceMap.containsKey(counter)) {
								Integer id = invoiceMap.get(counter);
								gl.setAccount(chartOfAccountsDao.getLedgerName(id,  receiptVo.getOrganizationId()));
								gl.setAccountId(id);

							}else {
								gl.setAccount(customerLedger);
								gl.setAccountId(customerAccountId);
							}
							
							gl.setLedgerList(chartOfAccountsDao.getLedgersByEntity(receiptVo.getOrganizationId(), ChartOfAccountsConstants.ENTITY_CUSTOMER));
							gl.setSubLedger(arInvoieNo);
							logger.info("Invoice Due Amnt :: "+vo.getInvoiceDueAmount());
							Double invAmntPaid = Double.valueOf(vo.getInvoiceDueAmount());
							Double xchangeRate = receiptVo.getExchangeRate() !=null && !"".equals(receiptVo.getExchangeRate()) ? Double.valueOf(receiptVo.getExchangeRate())  : 1.00;
							if(invAmntPaid > 0) {
								gl.setFcyCredit(Double.valueOf(df.format( invAmntPaid)));
								gl.setInrCredit(Double.valueOf(df.format(xchangeRate * invAmntPaid)));
							}else{
								gl.setFcyDebit(Double.valueOf(df.format( invAmntPaid * -1)));
								gl.setInrDebit(Double.valueOf(df.format(xchangeRate * invAmntPaid * -1)));
							}
							
							generalLedgers.add(gl);
							}
							
							//for Bank charges 
							if(vo.getBankCharges()!=null &&  !vo.getBankCharges().equals("")  && !vo.getBankCharges().equals("0")  && !vo.getBankCharges().equals("null")){
								GeneralLedgerDetailsVo gl = new GeneralLedgerDetailsVo();
								gl.setTempId(counter);
								gl.setProductId(vo.getReferenceId());
								gl.setType(JournalEntriesConstants.BANK_CHARGES);
								gl.setDescription(JournalEntriesConstants.BANK_CHARGES);
								if(bankChargesMap!=null && bankChargesMap.size()>0 && bankChargesMap.containsKey(counter)) {
									Integer id = bankChargesMap.get(counter);
									gl.setAccount(chartOfAccountsDao.getLedgerName(id,  receiptVo.getOrganizationId()));
									gl.setAccountId(id);
								}else {
									String ledger = chartOfAccountsDao.getDefaultLedger(ChartOfAccountsConstants.ENTITY_INDIRECT_EXPENSES, null, ChartOfAccountsConstants.FIELD_BANK_CHARGES);
									gl.setAccount(ledger);
									gl.setAccountId( chartOfAccountsDao.getLedgerIdGivenName(ledger, receiptVo.getOrganizationId()));

								}
								gl.setLedgerList(chartOfAccountsDao.getLedgersByEntity(receiptVo.getOrganizationId(), ChartOfAccountsConstants.ENTITY_INDIRECT_EXPENSES));
								gl.setSubLedger(arInvoieNo);
								Double bankAmntPaid = Double.valueOf(vo.getBankCharges());
								Double xchangeRate = receiptVo.getExchangeRate() !=null && !"".equals(receiptVo.getExchangeRate()) ? Double.valueOf(receiptVo.getExchangeRate())  : 1.00;
								if(bankAmntPaid > 0) {
									gl.setFcyCredit(Double.valueOf(df.format( bankAmntPaid)));
									gl.setInrCredit(Double.valueOf(df.format(xchangeRate * bankAmntPaid)));
								}else{
									gl.setFcyDebit(Double.valueOf(df.format( bankAmntPaid * -1)));
									gl.setInrDebit(Double.valueOf(df.format(xchangeRate * bankAmntPaid * -1)));
								}
								generalLedgers.add(gl);
								}
							
							
							//for TDS Deducted 
							if(vo.getTdsDeducted()!=null &&  !vo.getTdsDeducted().equals("")  && !vo.getTdsDeducted().equals("0")  && !"null".equals(vo.getTdsDeducted())){
								GeneralLedgerDetailsVo gl = new GeneralLedgerDetailsVo();
								gl.setTempId(counter);
								gl.setProductId(vo.getReferenceId());
								gl.setType(JournalEntriesConstants.TDS_Deducted);
								gl.setDescription(JournalEntriesConstants.TDS_Deducted);
								if(tdsDeductedMap!=null && tdsDeductedMap.size()>0 && tdsDeductedMap.containsKey(counter)) {
									Integer id = tdsDeductedMap.get(counter);
									gl.setAccount(chartOfAccountsDao.getLedgerName(id,  receiptVo.getOrganizationId()));
									gl.setAccountId(id);
								}else {
									String ledger = chartOfAccountsDao.getDefaultLedger(ChartOfAccountsConstants.ENTITY_INDIRECT_EXPENSES, null, ChartOfAccountsConstants.FIELD_INTEREST_ON_TDS);
									gl.setAccount(ledger);
									gl.setAccountId( chartOfAccountsDao.getLedgerIdGivenName(ledger, receiptVo.getOrganizationId()));
								}
								gl.setLedgerList(chartOfAccountsDao.getLedgersByEntity(receiptVo.getOrganizationId(), ChartOfAccountsConstants.ENTITY_INDIRECT_EXPENSES));
								gl.setSubLedger(arInvoieNo);
								Double tdsamntPaid = Double.valueOf(vo.getTdsDeducted());
								Double xchangeRate = receiptVo.getExchangeRate() !=null && !"".equals(receiptVo.getExchangeRate()) ? Double.valueOf(receiptVo.getExchangeRate())  : 1.00;
								if(tdsamntPaid > 0) {
									gl.setFcyCredit(Double.valueOf(df.format( tdsamntPaid)));
									gl.setInrCredit(Double.valueOf(df.format(xchangeRate * tdsamntPaid)));
								}else{
									gl.setFcyDebit(Double.valueOf(df.format( tdsamntPaid * -1)));
									gl.setInrDebit(Double.valueOf(df.format(xchangeRate * tdsamntPaid * -1)));
								}
								generalLedgers.add(gl);
								}
							
							//for Others 1
							if(vo.getOthers1()!=null &&  !vo.getOthers1().equals("")  && !vo.getOthers1().equals("0") && !"null".equals(vo.getOthers1())){
								GeneralLedgerDetailsVo gl = new GeneralLedgerDetailsVo();
								gl.setTempId(counter);
								gl.setProductId(vo.getReferenceId());
								gl.setType(JournalEntriesConstants.OTHERS1);
								String tName  = receiptVo.getCustomTableList()!=null && receiptVo.getCustomTableList().size()> 0 ? 
										receiptVo.getCustomTableList().stream().filter(val -> JournalEntriesConstants.OTHERS1.equals(val.getcType()) && val.isColumnShow()).findFirst().get().getcName() : null ;
								gl.setDescription(tName!=null ? tName : null);
								if(others1Map!=null && others1Map.size()>0 && others1Map.containsKey(counter)) {
									Integer id = others1Map.get(counter);
									gl.setAccount(chartOfAccountsDao.getLedgerName(id,  receiptVo.getOrganizationId()));
									gl.setAccountId(id);
								}
								gl.setLedgerList(allLedgers);
								gl.setSubLedger(arInvoieNo);
								Double ot1amntPaid = Double.valueOf(vo.getOthers1());
								Double xchangeRate = receiptVo.getExchangeRate() !=null && !"".equals(receiptVo.getExchangeRate()) ? Double.valueOf(receiptVo.getExchangeRate())  : 1.00;
								if(ot1amntPaid > 0) {
									gl.setFcyCredit(Double.valueOf(df.format( ot1amntPaid)));
									gl.setInrCredit(Double.valueOf(df.format(xchangeRate * ot1amntPaid)));
								}else{
									gl.setFcyDebit(Double.valueOf(df.format(ot1amntPaid * -1 )));
									gl.setInrDebit(Double.valueOf(df.format(xchangeRate * ot1amntPaid *-1)));
								}
								generalLedgers.add(gl);
								}
							
							//for Others 2
							if(vo.getOthers2()!=null &&  !vo.getOthers2().equals("")  && !vo.getOthers2().equals("0") && !"null".equals(vo.getOthers2())){
								GeneralLedgerDetailsVo gl = new GeneralLedgerDetailsVo();
								gl.setTempId(counter);
								gl.setProductId(vo.getReferenceId());
								gl.setType(JournalEntriesConstants.OTHERS2);
								String tName  = receiptVo.getCustomTableList()!=null && receiptVo.getCustomTableList().size()> 0 ? 
										receiptVo.getCustomTableList().stream().filter(val -> JournalEntriesConstants.OTHERS2.equals(val.getcType()) && val.isColumnShow()).findFirst().get().getcName() : null ;
								gl.setDescription(tName!=null ? tName : null);
								if(others2Map!=null && others2Map.size()>0 && others2Map.containsKey(counter)) {
									Integer id = others2Map.get(counter);
									gl.setAccount(chartOfAccountsDao.getLedgerName(id,  receiptVo.getOrganizationId()));
									gl.setAccountId(id);
								}
								
								gl.setLedgerList(allLedgers);
								gl.setSubLedger(arInvoieNo);
								Double ot2amntPaid = Double.valueOf(vo.getOthers2());
								Double xchangeRate = receiptVo.getExchangeRate() !=null && !"".equals(receiptVo.getExchangeRate()) ? Double.valueOf(receiptVo.getExchangeRate())  : 1.00;
								if(ot2amntPaid > 0) {
									gl.setFcyCredit(Double.valueOf(df.format( ot2amntPaid)));
									gl.setInrCredit(Double.valueOf(df.format(xchangeRate * ot2amntPaid)));
								}else{
									gl.setFcyDebit(Double.valueOf(df.format( ot2amntPaid * -1)));
									gl.setInrDebit(Double.valueOf(df.format(xchangeRate * ot2amntPaid  * -1)));
								}
								generalLedgers.add(gl);
								}
							
							//for Others 3
							if(vo.getOthers3()!=null &&  !vo.getOthers3().equals("")  && !vo.getOthers3().equals("0")  && !"null".equals(vo.getOthers3())){
								GeneralLedgerDetailsVo gl = new GeneralLedgerDetailsVo();
								gl.setTempId(counter);
								gl.setProductId(vo.getReferenceId());
								gl.setType(JournalEntriesConstants.OTHERS3);
								String tName  = receiptVo.getCustomTableList()!=null && receiptVo.getCustomTableList().size()> 0 ? 
										receiptVo.getCustomTableList().stream().filter(val -> JournalEntriesConstants.OTHERS3.equals(val.getcType()) && val.isColumnShow()).findFirst().get().getcName() : null ;
								gl.setDescription(tName!=null ? tName : null);
								if(others3Map!=null && others3Map.size()>0 && others3Map.containsKey(counter)) {
									Integer id = others3Map.get(counter);
									gl.setAccount(chartOfAccountsDao.getLedgerName(id,  receiptVo.getOrganizationId()));
									gl.setAccountId(id);
								}
								gl.setLedgerList(allLedgers);
								gl.setSubLedger(arInvoieNo);
								Double ot3amntPaid = Double.valueOf(vo.getOthers3());
								Double xchangeRate = receiptVo.getExchangeRate() !=null && !"".equals(receiptVo.getExchangeRate()) ? Double.valueOf(receiptVo.getExchangeRate())  : 1.00;
								if(ot3amntPaid > 0) {
									gl.setFcyCredit(Double.valueOf(df.format( ot3amntPaid)));
									gl.setInrCredit(Double.valueOf(df.format(xchangeRate * ot3amntPaid)));
								}else{
									gl.setFcyDebit(Double.valueOf(df.format( ot3amntPaid * -1)));
									gl.setInrDebit(Double.valueOf(df.format(xchangeRate * ot3amntPaid * -1)));
								}
								generalLedgers.add(gl);
								}
							
							break;
						case 2:// Advance Type 
							String advanceNo   = vo.getReferenceId()!=null ? 
									receiptDao.getReceiptNumber(vo.getReferenceId()): null ;
							GeneralLedgerDetailsVo glAdvance = new GeneralLedgerDetailsVo();
							glAdvance.setTempId(receiptVo.getCustomerId());
							glAdvance.setProductId(vo.getReferenceId());
							glAdvance.setType(JournalEntriesConstants.ADVANCE);
							glAdvance.setDescription(vo.getReferenceType());
							if(advanceMap!=null && advanceMap.size()>0 && advanceMap.containsKey(vo.getReferenceId())) {
								Integer id = advanceMap.get(vo.getReferenceId());
								glAdvance.setAccount(chartOfAccountsDao.getLedgerName(id,  receiptVo.getOrganizationId()));
								glAdvance.setAccountId(id);

							}else {
								glAdvance.setAccount(customerLedger);
								glAdvance.setAccountId(customerAccountId);
							}
							glAdvance.setLedgerList(chartOfAccountsDao.getLedgersByEntity(receiptVo.getOrganizationId(), ChartOfAccountsConstants.ENTITY_CUSTOMER));
							
							glAdvance.setSubLedger(advanceNo);
							Double amontPaid = Double.valueOf(vo.getInvoiceDueAmount());
							Double exchangeRate = receiptVo.getExchangeRate() !=null && !"".equals(receiptVo.getExchangeRate()) ? Double.valueOf(receiptVo.getExchangeRate())  : 1.00;
							if(amontPaid > 0 ) {
								glAdvance.setFcyDebit(Double.valueOf(df.format( amontPaid)));
								glAdvance.setInrDebit(Double.valueOf(df.format(exchangeRate * amontPaid)));
							}else {
								glAdvance.setFcyCredit(Double.valueOf(df.format( amontPaid * -1)));
								glAdvance.setInrCredit(Double.valueOf(df.format(exchangeRate * amontPaid * -1)));
							}
							generalLedgers.add(glAdvance);
							
							
							//for Bank charges 
							if(vo.getBankCharges()!=null &&  !vo.getBankCharges().equals("")  && !vo.getBankCharges().equals("0")  && !vo.getBankCharges().equals("null")){
								GeneralLedgerDetailsVo gl = new GeneralLedgerDetailsVo();
								gl.setTempId(counter);
								gl.setProductId(vo.getReferenceId());
								gl.setType(JournalEntriesConstants.BANK_CHARGES);
								gl.setDescription(JournalEntriesConstants.BANK_CHARGES);
								if(bankChargesMap!=null && bankChargesMap.size()>0 && bankChargesMap.containsKey(counter)) {
									Integer id = bankChargesMap.get(counter);
									gl.setAccount(chartOfAccountsDao.getLedgerName(id,  receiptVo.getOrganizationId()));
									gl.setAccountId(id);
								}else {
									String ledger = chartOfAccountsDao.getDefaultLedger(ChartOfAccountsConstants.ENTITY_INDIRECT_EXPENSES, null, ChartOfAccountsConstants.FIELD_BANK_CHARGES);
									gl.setAccount(ledger);
									gl.setAccountId( chartOfAccountsDao.getLedgerIdGivenName(ledger, receiptVo.getOrganizationId()));

								}
								gl.setLedgerList(chartOfAccountsDao.getLedgersByEntity(receiptVo.getOrganizationId(), ChartOfAccountsConstants.ENTITY_INDIRECT_EXPENSES));
								gl.setSubLedger(advanceNo);
								Double bankAmntPaid = Double.valueOf(vo.getBankCharges());
								Double xchangeRate = receiptVo.getExchangeRate() !=null && !"".equals(receiptVo.getExchangeRate()) ? Double.valueOf(receiptVo.getExchangeRate())  : 1.00;
								if(bankAmntPaid > 0) {
									gl.setInrDebit(Double.valueOf(df.format( bankAmntPaid)));
									gl.setInrDebit(Double.valueOf(df.format(xchangeRate * bankAmntPaid)));
								}else{
									gl.setInrCredit(Double.valueOf(df.format( bankAmntPaid * -1)));
									gl.setInrCredit(Double.valueOf(df.format(xchangeRate * bankAmntPaid * -1)));
								}
								generalLedgers.add(gl);
								}
							
							
							//for TDS Deducted 
							if(vo.getTdsDeducted()!=null &&  !vo.getTdsDeducted().equals("")  && !vo.getTdsDeducted().equals("0")  && !"null".equals(vo.getTdsDeducted())){
								GeneralLedgerDetailsVo gl = new GeneralLedgerDetailsVo();
								gl.setTempId(counter);
								gl.setProductId(vo.getReferenceId());
								gl.setType(JournalEntriesConstants.TDS_Deducted);
								gl.setDescription(JournalEntriesConstants.TDS_Deducted);
								if(tdsDeductedMap!=null && tdsDeductedMap.size()>0 && tdsDeductedMap.containsKey(counter)) {
									Integer id = tdsDeductedMap.get(counter);
									gl.setAccount(chartOfAccountsDao.getLedgerName(id,  receiptVo.getOrganizationId()));
									gl.setAccountId(id);
								}else {
									String ledger = chartOfAccountsDao.getDefaultLedger(ChartOfAccountsConstants.ENTITY_INDIRECT_EXPENSES, null, ChartOfAccountsConstants.FIELD_INTEREST_ON_TDS);
									gl.setAccount(ledger);
									gl.setAccountId( chartOfAccountsDao.getLedgerIdGivenName(ledger, receiptVo.getOrganizationId()));
								}
								gl.setLedgerList(chartOfAccountsDao.getLedgersByEntity(receiptVo.getOrganizationId(), ChartOfAccountsConstants.ENTITY_INDIRECT_EXPENSES));
								gl.setSubLedger(advanceNo);
								Double tdsamntPaid = Double.valueOf(vo.getTdsDeducted());
								Double xchangeRate = receiptVo.getExchangeRate() !=null && !"".equals(receiptVo.getExchangeRate()) ? Double.valueOf(receiptVo.getExchangeRate())  : 1.00;
								if(tdsamntPaid > 0) {
									gl.setFcyDebit(Double.valueOf(df.format( tdsamntPaid)));
									gl.setInrDebit(Double.valueOf(df.format(xchangeRate * tdsamntPaid)));
								}else{
									gl.setFcyCredit(Double.valueOf(df.format( tdsamntPaid * -1)));
									gl.setInrCredit(Double.valueOf(df.format(xchangeRate * tdsamntPaid * -1)));
								}
								generalLedgers.add(gl);
								}
							
							//for Others 1
							if(vo.getOthers1()!=null &&  !vo.getOthers1().equals("")  && !vo.getOthers1().equals("0") && !"null".equals(vo.getOthers1())){
								GeneralLedgerDetailsVo gl = new GeneralLedgerDetailsVo();
								gl.setTempId(counter);
								gl.setProductId(vo.getReferenceId());
								gl.setType(JournalEntriesConstants.OTHERS1);
								String tName  = receiptVo.getCustomTableList()!=null && receiptVo.getCustomTableList().size()> 0 ? 
										receiptVo.getCustomTableList().stream().filter(val -> JournalEntriesConstants.OTHERS1.equals(val.getcType()) && val.isColumnShow()).findFirst().get().getcName() : null ;
								gl.setDescription(tName!=null ? tName : null);
								if(others1Map!=null && others1Map.size()>0 && others1Map.containsKey(counter)) {
									Integer id = others1Map.get(counter);
									gl.setAccount(chartOfAccountsDao.getLedgerName(id,  receiptVo.getOrganizationId()));
									gl.setAccountId(id);
								}
								gl.setLedgerList(allLedgers);
								gl.setSubLedger(advanceNo);
								Double ot1amntPaid = Double.valueOf(vo.getOthers1());
								Double xchangeRate = receiptVo.getExchangeRate() !=null && !"".equals(receiptVo.getExchangeRate()) ? Double.valueOf(receiptVo.getExchangeRate())  : 1.00;
								if(ot1amntPaid > 0) {
									gl.setFcyDebit(Double.valueOf(df.format( ot1amntPaid)));
									gl.setInrDebit(Double.valueOf(df.format(xchangeRate * ot1amntPaid)));
								}else{
									gl.setFcyCredit(Double.valueOf(df.format(ot1amntPaid * -1 )));
									gl.setInrCredit(Double.valueOf(df.format(xchangeRate * ot1amntPaid *-1)));
								}
								generalLedgers.add(gl);
								}
							
							//for Others 2
							if(vo.getOthers2()!=null &&  !vo.getOthers2().equals("")  && !vo.getOthers2().equals("0") && !"null".equals(vo.getOthers2())){
								GeneralLedgerDetailsVo gl = new GeneralLedgerDetailsVo();
								gl.setTempId(counter);
								gl.setProductId(vo.getReferenceId());
								gl.setType(JournalEntriesConstants.OTHERS2);
								String tName  = receiptVo.getCustomTableList()!=null && receiptVo.getCustomTableList().size()> 0 ? 
										receiptVo.getCustomTableList().stream().filter(val -> JournalEntriesConstants.OTHERS2.equals(val.getcType()) && val.isColumnShow()).findFirst().get().getcName() : null ;
								gl.setDescription(tName!=null ? tName : null);
								if(others2Map!=null && others2Map.size()>0 && others2Map.containsKey(counter)) {
									Integer id = others2Map.get(counter);
									gl.setAccount(chartOfAccountsDao.getLedgerName(id,  receiptVo.getOrganizationId()));
									gl.setAccountId(id);
								}
								
								gl.setLedgerList(allLedgers);
								gl.setSubLedger(advanceNo);
								Double ot2amntPaid = Double.valueOf(vo.getOthers2());
								Double xchangeRate = receiptVo.getExchangeRate() !=null && !"".equals(receiptVo.getExchangeRate()) ? Double.valueOf(receiptVo.getExchangeRate())  : 1.00;
								if(ot2amntPaid > 0) {
									gl.setFcyDebit(Double.valueOf(df.format( ot2amntPaid)));
									gl.setInrDebit(Double.valueOf(df.format(xchangeRate * ot2amntPaid)));
								}else{
									gl.setFcyCredit(Double.valueOf(df.format( ot2amntPaid * -1)));
									gl.setInrCredit(Double.valueOf(df.format(xchangeRate * ot2amntPaid  * -1)));
								}
								generalLedgers.add(gl);
								}
							
							//for Others 3
							if(vo.getOthers3()!=null &&  !vo.getOthers3().equals("")  && !vo.getOthers3().equals("0")  && !"null".equals(vo.getOthers3())){
								GeneralLedgerDetailsVo gl = new GeneralLedgerDetailsVo();
								gl.setTempId(counter);
								gl.setProductId(vo.getReferenceId());
								gl.setType(JournalEntriesConstants.OTHERS3);
								String tName  = receiptVo.getCustomTableList()!=null && receiptVo.getCustomTableList().size()> 0 ? 
										receiptVo.getCustomTableList().stream().filter(val -> JournalEntriesConstants.OTHERS3.equals(val.getcType()) && val.isColumnShow()).findFirst().get().getcName() : null ;
								gl.setDescription(tName!=null ? tName : null);
								if(others3Map!=null && others3Map.size()>0 && others3Map.containsKey(counter)) {
									Integer id = others3Map.get(counter);
									gl.setAccount(chartOfAccountsDao.getLedgerName(id,  receiptVo.getOrganizationId()));
									gl.setAccountId(id);
								}
								gl.setLedgerList(allLedgers);
								gl.setSubLedger(advanceNo);
								Double ot3amntPaid = Double.valueOf(vo.getOthers3());
								Double xchangeRate = receiptVo.getExchangeRate() !=null && !"".equals(receiptVo.getExchangeRate()) ? Double.valueOf(receiptVo.getExchangeRate())  : 1.00;
								if(ot3amntPaid > 0) {
									gl.setFcyDebit(Double.valueOf(df.format( ot3amntPaid)));
									gl.setInrDebit(Double.valueOf(df.format(xchangeRate * ot3amntPaid)));
								}else{
									gl.setFcyCredit(Double.valueOf(df.format( ot3amntPaid * -1)));
									gl.setInrCredit(Double.valueOf(df.format(xchangeRate * ot3amntPaid * -1)));
								}
								generalLedgers.add(gl);
								}

							
							break;
						case 3:// Credit note type 
							String cnNo   = vo.getReferenceId()!=null ? 
									creditNotesDao.getCreditNoteNumber(vo.getReferenceId()): null ;
									
							GeneralLedgerDetailsVo glCreditNote = new GeneralLedgerDetailsVo();
							glCreditNote.setTempId(receiptVo.getCustomerId());
							glCreditNote.setProductId(vo.getReferenceId());
							glCreditNote.setType(JournalEntriesConstants.SUB_MODULE_CREDIT_NOTES);
							glCreditNote.setDescription(vo.getReferenceType());
							if(creditNoteMap!=null && creditNoteMap.size()>0 && creditNoteMap.containsKey(vo.getReferenceId())) {
								Integer id = creditNoteMap.get(vo.getReferenceId());
								glCreditNote.setAccount(chartOfAccountsDao.getLedgerName(id,  receiptVo.getOrganizationId()));
								glCreditNote.setAccountId(id);

							}else {
								glCreditNote.setAccount(customerLedger);
								glCreditNote.setAccountId(customerAccountId);
							}
							glCreditNote.setLedgerList(chartOfAccountsDao.getLedgersByEntity(receiptVo.getOrganizationId(), ChartOfAccountsConstants.ENTITY_CUSTOMER));
							glCreditNote.setSubLedger(cnNo);
							Double amtPaid = Double.valueOf(vo.getInvoiceDueAmount());
							Double xchangRate = receiptVo.getExchangeRate() !=null && !"".equals(receiptVo.getExchangeRate()) ? Double.valueOf(receiptVo.getExchangeRate())  : 1.00;
							if(amtPaid > 0 ) {
								glCreditNote.setFcyDebit(Double.valueOf(df.format( amtPaid)));
								glCreditNote.setInrDebit(Double.valueOf(df.format(xchangRate * amtPaid)));
							}else {
								glCreditNote.setFcyCredit(Double.valueOf(df.format( amtPaid * -1)));
								glCreditNote.setInrCredit(Double.valueOf(df.format(xchangRate * amtPaid * -1)));
							}
							
							generalLedgers.add(glCreditNote);
							
							
							//for Bank charges 
							if(vo.getBankCharges()!=null &&  !vo.getBankCharges().equals("")  && !vo.getBankCharges().equals("0")  && !vo.getBankCharges().equals("null")){
								GeneralLedgerDetailsVo gl = new GeneralLedgerDetailsVo();
								gl.setTempId(counter);
								gl.setProductId(vo.getReferenceId());
								gl.setType(JournalEntriesConstants.BANK_CHARGES);
								gl.setDescription(JournalEntriesConstants.BANK_CHARGES);
								if(bankChargesMap!=null && bankChargesMap.size()>0 && bankChargesMap.containsKey(counter)) {
									Integer id = bankChargesMap.get(counter);
									gl.setAccount(chartOfAccountsDao.getLedgerName(id,  receiptVo.getOrganizationId()));
									gl.setAccountId(id);
								}else {
									String ledger = chartOfAccountsDao.getDefaultLedger(ChartOfAccountsConstants.ENTITY_INDIRECT_EXPENSES, null, ChartOfAccountsConstants.FIELD_BANK_CHARGES);
									gl.setAccount(ledger);
									gl.setAccountId( chartOfAccountsDao.getLedgerIdGivenName(ledger, receiptVo.getOrganizationId()));

								}
								gl.setLedgerList(chartOfAccountsDao.getLedgersByEntity(receiptVo.getOrganizationId(), ChartOfAccountsConstants.ENTITY_INDIRECT_EXPENSES));
								gl.setSubLedger(cnNo);
								Double bankAmntPaid = Double.valueOf(vo.getBankCharges());
								Double xchangeRate = receiptVo.getExchangeRate() !=null && !"".equals(receiptVo.getExchangeRate()) ? Double.valueOf(receiptVo.getExchangeRate())  : 1.00;
								if(bankAmntPaid > 0) {
									gl.setFcyDebit(Double.valueOf(df.format( bankAmntPaid)));
									gl.setInrDebit(Double.valueOf(df.format(xchangeRate * bankAmntPaid)));
								}else{
									gl.setFcyCredit(Double.valueOf(df.format( bankAmntPaid * -1)));
									gl.setInrCredit(Double.valueOf(df.format(xchangeRate * bankAmntPaid * -1)));
								}
								generalLedgers.add(gl);
								}
							
							
							//for TDS Deducted 
							if(vo.getTdsDeducted()!=null &&  !vo.getTdsDeducted().equals("")  && !vo.getTdsDeducted().equals("0")  && !"null".equals(vo.getTdsDeducted())){
								GeneralLedgerDetailsVo gl = new GeneralLedgerDetailsVo();
								gl.setTempId(counter);
								gl.setProductId(vo.getReferenceId());
								gl.setType(JournalEntriesConstants.TDS_Deducted);
								gl.setDescription(JournalEntriesConstants.TDS_Deducted);
								if(tdsDeductedMap!=null && tdsDeductedMap.size()>0 && tdsDeductedMap.containsKey(counter)) {
									Integer id = tdsDeductedMap.get(counter);
									gl.setAccount(chartOfAccountsDao.getLedgerName(id,  receiptVo.getOrganizationId()));
									gl.setAccountId(id);
								}else {
									String ledger = chartOfAccountsDao.getDefaultLedger(ChartOfAccountsConstants.ENTITY_INDIRECT_EXPENSES, null, ChartOfAccountsConstants.FIELD_INTEREST_ON_TDS);
									gl.setAccount(ledger);
									gl.setAccountId( chartOfAccountsDao.getLedgerIdGivenName(ledger, receiptVo.getOrganizationId()));
								}
								gl.setLedgerList(chartOfAccountsDao.getLedgersByEntity(receiptVo.getOrganizationId(), ChartOfAccountsConstants.ENTITY_INDIRECT_EXPENSES));
								gl.setSubLedger(cnNo);
								Double tdsamntPaid = Double.valueOf(vo.getTdsDeducted());
								Double xchangeRate = receiptVo.getExchangeRate() !=null && !"".equals(receiptVo.getExchangeRate()) ? Double.valueOf(receiptVo.getExchangeRate())  : 1.00;
								if(tdsamntPaid > 0) {
									gl.setFcyDebit(Double.valueOf(df.format( tdsamntPaid)));
									gl.setInrDebit(Double.valueOf(df.format(xchangeRate * tdsamntPaid)));
								}else{
									gl.setFcyCredit(Double.valueOf(df.format( tdsamntPaid * -1)));
									gl.setInrCredit(Double.valueOf(df.format(xchangeRate * tdsamntPaid * -1)));
								}
								generalLedgers.add(gl);
								}
							
							//for Others 1
							if(vo.getOthers1()!=null &&  !vo.getOthers1().equals("")  && !vo.getOthers1().equals("0") && !"null".equals(vo.getOthers1())){
								GeneralLedgerDetailsVo gl = new GeneralLedgerDetailsVo();
								gl.setTempId(counter);
								gl.setProductId(vo.getReferenceId());
								gl.setType(JournalEntriesConstants.OTHERS1);
								String tName  = receiptVo.getCustomTableList()!=null && receiptVo.getCustomTableList().size()> 0 ? 
										receiptVo.getCustomTableList().stream().filter(val -> JournalEntriesConstants.OTHERS1.equals(val.getcType()) && val.isColumnShow()).findFirst().get().getcName() : null ;
								gl.setDescription(tName!=null ? tName : null);
								if(others1Map!=null && others1Map.size()>0 && others1Map.containsKey(counter)) {
									Integer id = others1Map.get(counter);
									gl.setAccount(chartOfAccountsDao.getLedgerName(id,  receiptVo.getOrganizationId()));
									gl.setAccountId(id);
								}
								gl.setLedgerList(allLedgers);
								gl.setSubLedger(cnNo);
								Double ot1amntPaid = Double.valueOf(vo.getOthers1());
								Double xchangeRate = receiptVo.getExchangeRate() !=null && !"".equals(receiptVo.getExchangeRate()) ? Double.valueOf(receiptVo.getExchangeRate())  : 1.00;
								if(ot1amntPaid > 0) {
									gl.setFcyDebit(Double.valueOf(df.format( ot1amntPaid)));
									gl.setInrDebit(Double.valueOf(df.format(xchangeRate * ot1amntPaid)));
								}else{
									gl.setFcyCredit(Double.valueOf(df.format(ot1amntPaid * -1 )));
									gl.setInrCredit(Double.valueOf(df.format(xchangeRate * ot1amntPaid *-1)));
								}
								generalLedgers.add(gl);
								}
							
							//for Others 2
							if(vo.getOthers2()!=null &&  !vo.getOthers2().equals("")  && !vo.getOthers2().equals("0") && !"null".equals(vo.getOthers2())){
								GeneralLedgerDetailsVo gl = new GeneralLedgerDetailsVo();
								gl.setTempId(counter);
								gl.setProductId(vo.getReferenceId());
								gl.setType(JournalEntriesConstants.OTHERS2);
								String tName  = receiptVo.getCustomTableList()!=null && receiptVo.getCustomTableList().size()> 0 ? 
										receiptVo.getCustomTableList().stream().filter(val -> JournalEntriesConstants.OTHERS2.equals(val.getcType()) && val.isColumnShow()).findFirst().get().getcName() : null ;
								gl.setDescription(tName!=null ? tName : null);
								if(others2Map!=null && others2Map.size()>0 && others2Map.containsKey(counter)) {
									Integer id = others2Map.get(counter);
									gl.setAccount(chartOfAccountsDao.getLedgerName(id,  receiptVo.getOrganizationId()));
									gl.setAccountId(id);
								}
								
								gl.setLedgerList(allLedgers);
								gl.setSubLedger(cnNo);
								Double ot2amntPaid = Double.valueOf(vo.getOthers2());
								Double xchangeRate = receiptVo.getExchangeRate() !=null && !"".equals(receiptVo.getExchangeRate()) ? Double.valueOf(receiptVo.getExchangeRate())  : 1.00;
								if(ot2amntPaid > 0) {
									gl.setFcyDebit(Double.valueOf(df.format( ot2amntPaid)));
									gl.setInrDebit(Double.valueOf(df.format(xchangeRate * ot2amntPaid)));
								}else{
									gl.setFcyCredit(Double.valueOf(df.format( ot2amntPaid * -1)));
									gl.setInrCredit(Double.valueOf(df.format(xchangeRate * ot2amntPaid  * -1)));
								}
								generalLedgers.add(gl);
								}
							
							//for Others 3
							if(vo.getOthers3()!=null &&  !vo.getOthers3().equals("")  && !vo.getOthers3().equals("0")  && !"null".equals(vo.getOthers3())){
								GeneralLedgerDetailsVo gl = new GeneralLedgerDetailsVo();
								gl.setTempId(counter);
								gl.setProductId(vo.getReferenceId());
								gl.setType(JournalEntriesConstants.OTHERS3);
								String tName  = receiptVo.getCustomTableList()!=null && receiptVo.getCustomTableList().size()> 0 ? 
										receiptVo.getCustomTableList().stream().filter(val -> JournalEntriesConstants.OTHERS3.equals(val.getcType()) && val.isColumnShow()).findFirst().get().getcName() : null ;
								gl.setDescription(tName!=null ? tName : null);
								if(others3Map!=null && others3Map.size()>0 && others3Map.containsKey(counter)) {
									Integer id = others3Map.get(counter);
									gl.setAccount(chartOfAccountsDao.getLedgerName(id,  receiptVo.getOrganizationId()));
									gl.setAccountId(id);
								}
								gl.setLedgerList(allLedgers);
								gl.setSubLedger(cnNo);
								Double ot3amntPaid = Double.valueOf(vo.getOthers3());
								Double xchangeRate = receiptVo.getExchangeRate() !=null && !"".equals(receiptVo.getExchangeRate()) ? Double.valueOf(receiptVo.getExchangeRate())  : 1.00;
								if(ot3amntPaid > 0) {
									gl.setFcyDebit(Double.valueOf(df.format( ot3amntPaid)));
									gl.setInrDebit(Double.valueOf(df.format(xchangeRate * ot3amntPaid)));
								}else{
									gl.setFcyCredit(Double.valueOf(df.format( ot3amntPaid * -1)));
									gl.setInrCredit(Double.valueOf(df.format(xchangeRate * ot3amntPaid * -1)));
								}
								generalLedgers.add(gl);
								}

							break;
							
						case 4 : // On Account 
							String onAccountNo   = vo.getReferenceId()!=null ? 
									receiptDao.getReceiptNumber(vo.getReferenceId()): null ;
							GeneralLedgerDetailsVo glOnAccount= new GeneralLedgerDetailsVo();
							glOnAccount.setTempId(receiptVo.getCustomerId());
							glOnAccount.setProductId(vo.getReferenceId());
							glOnAccount.setType(JournalEntriesConstants.ADVANCE);
							glOnAccount.setDescription(vo.getReferenceType());
							if(advanceMap!=null && advanceMap.size()>0 && advanceMap.containsKey(vo.getReferenceId())) {
								Integer id = advanceMap.get(vo.getReferenceId());
								glOnAccount.setAccount(chartOfAccountsDao.getLedgerName(id,  receiptVo.getOrganizationId()));
								glOnAccount.setAccountId(id);

							}else {
								glOnAccount.setAccount(customerLedger);
								glOnAccount.setAccountId(customerAccountId);
							}
							glOnAccount.setLedgerList(chartOfAccountsDao.getLedgersByEntity(receiptVo.getOrganizationId(), ChartOfAccountsConstants.ENTITY_CUSTOMER));
							
							glOnAccount.setSubLedger(onAccountNo);
							Double onAccamtPaid = Double.valueOf(vo.getInvoiceDueAmount());
							Double exchangRate = receiptVo.getExchangeRate() !=null && !"".equals(receiptVo.getExchangeRate()) ? Double.valueOf(receiptVo.getExchangeRate())  : 1.00;
							if(onAccamtPaid > 0 ) {
								glOnAccount.setFcyDebit(Double.valueOf(df.format( onAccamtPaid)));
								glOnAccount.setInrDebit(Double.valueOf(df.format(exchangRate * onAccamtPaid)));
							}else {
								glOnAccount.setFcyCredit(Double.valueOf(df.format( onAccamtPaid * -1)));
								glOnAccount.setInrCredit(Double.valueOf(df.format(exchangRate * onAccamtPaid * -1)));
							}
							generalLedgers.add(glOnAccount);
							
							
							//for Bank charges 
							if(vo.getBankCharges()!=null &&  !vo.getBankCharges().equals("")  && !vo.getBankCharges().equals("0")  && !vo.getBankCharges().equals("null")){
								GeneralLedgerDetailsVo gl = new GeneralLedgerDetailsVo();
								gl.setTempId(counter);
								gl.setProductId(vo.getReferenceId());
								gl.setType(JournalEntriesConstants.BANK_CHARGES);
								gl.setDescription(JournalEntriesConstants.BANK_CHARGES);
								if(bankChargesMap!=null && bankChargesMap.size()>0 && bankChargesMap.containsKey(counter)) {
									Integer id = bankChargesMap.get(counter);
									gl.setAccount(chartOfAccountsDao.getLedgerName(id,  receiptVo.getOrganizationId()));
									gl.setAccountId(id);
								}else {
									String ledger = chartOfAccountsDao.getDefaultLedger(ChartOfAccountsConstants.ENTITY_INDIRECT_EXPENSES, null, ChartOfAccountsConstants.FIELD_BANK_CHARGES);
									gl.setAccount(ledger);
									gl.setAccountId( chartOfAccountsDao.getLedgerIdGivenName(ledger, receiptVo.getOrganizationId()));

								}
								gl.setLedgerList(chartOfAccountsDao.getLedgersByEntity(receiptVo.getOrganizationId(), ChartOfAccountsConstants.ENTITY_INDIRECT_EXPENSES));
								gl.setSubLedger(onAccountNo);
								Double bankAmntPaid = Double.valueOf(vo.getBankCharges());
								Double xchangeRate = receiptVo.getExchangeRate() !=null && !"".equals(receiptVo.getExchangeRate()) ? Double.valueOf(receiptVo.getExchangeRate())  : 1.00;
								if(bankAmntPaid > 0) {
									gl.setInrDebit(Double.valueOf(df.format( bankAmntPaid)));
									gl.setInrDebit(Double.valueOf(df.format(xchangeRate * bankAmntPaid)));
								}else{
									gl.setInrCredit(Double.valueOf(df.format( bankAmntPaid * -1)));
									gl.setInrCredit(Double.valueOf(df.format(xchangeRate * bankAmntPaid * -1)));
								}
								generalLedgers.add(gl);
								}
							
							
							//for TDS Deducted 
							if(vo.getTdsDeducted()!=null &&  !vo.getTdsDeducted().equals("")  && !vo.getTdsDeducted().equals("0")  && !"null".equals(vo.getTdsDeducted())){
								GeneralLedgerDetailsVo gl = new GeneralLedgerDetailsVo();
								gl.setTempId(counter);
								gl.setProductId(vo.getReferenceId());
								gl.setType(JournalEntriesConstants.TDS_Deducted);
								gl.setDescription(JournalEntriesConstants.TDS_Deducted);
								if(tdsDeductedMap!=null && tdsDeductedMap.size()>0 && tdsDeductedMap.containsKey(counter)) {
									Integer id = tdsDeductedMap.get(counter);
									gl.setAccount(chartOfAccountsDao.getLedgerName(id,  receiptVo.getOrganizationId()));
									gl.setAccountId(id);
								}else {
									String ledger = chartOfAccountsDao.getDefaultLedger(ChartOfAccountsConstants.ENTITY_INDIRECT_EXPENSES, null, ChartOfAccountsConstants.FIELD_INTEREST_ON_TDS);
									gl.setAccount(ledger);
									gl.setAccountId( chartOfAccountsDao.getLedgerIdGivenName(ledger, receiptVo.getOrganizationId()));
								}
								gl.setLedgerList(chartOfAccountsDao.getLedgersByEntity(receiptVo.getOrganizationId(), ChartOfAccountsConstants.ENTITY_INDIRECT_EXPENSES));
								gl.setSubLedger(onAccountNo);
								Double tdsamntPaid = Double.valueOf(vo.getTdsDeducted());
								Double xchangeRate = receiptVo.getExchangeRate() !=null && !"".equals(receiptVo.getExchangeRate()) ? Double.valueOf(receiptVo.getExchangeRate())  : 1.00;
								if(tdsamntPaid > 0) {
									gl.setFcyDebit(Double.valueOf(df.format( tdsamntPaid)));
									gl.setInrDebit(Double.valueOf(df.format(xchangeRate * tdsamntPaid)));
								}else{
									gl.setFcyCredit(Double.valueOf(df.format( tdsamntPaid * -1)));
									gl.setInrCredit(Double.valueOf(df.format(xchangeRate * tdsamntPaid * -1)));
								}
								generalLedgers.add(gl);
								}
							
							//for Others 1
							if(vo.getOthers1()!=null &&  !vo.getOthers1().equals("")  && !vo.getOthers1().equals("0") && !"null".equals(vo.getOthers1())){
								GeneralLedgerDetailsVo gl = new GeneralLedgerDetailsVo();
								gl.setTempId(counter);
								gl.setProductId(vo.getReferenceId());
								gl.setType(JournalEntriesConstants.OTHERS1);
								String tName  = receiptVo.getCustomTableList()!=null && receiptVo.getCustomTableList().size()> 0 ? 
										receiptVo.getCustomTableList().stream().filter(val -> JournalEntriesConstants.OTHERS1.equals(val.getcType()) && val.isColumnShow()).findFirst().get().getcName() : null ;
								gl.setDescription(tName!=null ? tName : null);
								if(others1Map!=null && others1Map.size()>0 && others1Map.containsKey(counter)) {
									Integer id = others1Map.get(counter);
									gl.setAccount(chartOfAccountsDao.getLedgerName(id,  receiptVo.getOrganizationId()));
									gl.setAccountId(id);
								}
								gl.setLedgerList(allLedgers);
								gl.setSubLedger(onAccountNo);
								Double ot1amntPaid = Double.valueOf(vo.getOthers1());
								Double xchangeRate = receiptVo.getExchangeRate() !=null && !"".equals(receiptVo.getExchangeRate()) ? Double.valueOf(receiptVo.getExchangeRate())  : 1.00;
								if(ot1amntPaid > 0) {
									gl.setFcyDebit(Double.valueOf(df.format( ot1amntPaid)));
									gl.setInrDebit(Double.valueOf(df.format(xchangeRate * ot1amntPaid)));
								}else{
									gl.setFcyCredit(Double.valueOf(df.format(ot1amntPaid * -1 )));
									gl.setInrCredit(Double.valueOf(df.format(xchangeRate * ot1amntPaid *-1)));
								}
								generalLedgers.add(gl);
								}
							
							//for Others 2
							if(vo.getOthers2()!=null &&  !vo.getOthers2().equals("")  && !vo.getOthers2().equals("0") && !"null".equals(vo.getOthers2())){
								GeneralLedgerDetailsVo gl = new GeneralLedgerDetailsVo();
								gl.setTempId(counter);
								gl.setProductId(vo.getReferenceId());
								gl.setType(JournalEntriesConstants.OTHERS2);
								String tName  = receiptVo.getCustomTableList()!=null && receiptVo.getCustomTableList().size()> 0 ? 
										receiptVo.getCustomTableList().stream().filter(val -> JournalEntriesConstants.OTHERS2.equals(val.getcType()) && val.isColumnShow()).findFirst().get().getcName() : null ;
								gl.setDescription(tName!=null ? tName : null);
								if(others2Map!=null && others2Map.size()>0 && others2Map.containsKey(counter)) {
									Integer id = others2Map.get(counter);
									gl.setAccount(chartOfAccountsDao.getLedgerName(id,  receiptVo.getOrganizationId()));
									gl.setAccountId(id);
								}
								
								gl.setLedgerList(allLedgers);
								gl.setSubLedger(onAccountNo);
								Double ot2amntPaid = Double.valueOf(vo.getOthers2());
								Double xchangeRate = receiptVo.getExchangeRate() !=null && !"".equals(receiptVo.getExchangeRate()) ? Double.valueOf(receiptVo.getExchangeRate())  : 1.00;
								if(ot2amntPaid > 0) {
									gl.setFcyDebit(Double.valueOf(df.format( ot2amntPaid)));
									gl.setInrDebit(Double.valueOf(df.format(xchangeRate * ot2amntPaid)));
								}else{
									gl.setFcyCredit(Double.valueOf(df.format( ot2amntPaid * -1)));
									gl.setInrCredit(Double.valueOf(df.format(xchangeRate * ot2amntPaid  * -1)));
								}
								generalLedgers.add(gl);
								}
							
							//for Others 3
							if(vo.getOthers3()!=null &&  !vo.getOthers3().equals("")  && !vo.getOthers3().equals("0")  && !"null".equals(vo.getOthers3())){
								GeneralLedgerDetailsVo gl = new GeneralLedgerDetailsVo();
								gl.setTempId(counter);
								gl.setProductId(vo.getReferenceId());
								gl.setType(JournalEntriesConstants.OTHERS3);
								String tName  = receiptVo.getCustomTableList()!=null && receiptVo.getCustomTableList().size()> 0 ? 
										receiptVo.getCustomTableList().stream().filter(val -> JournalEntriesConstants.OTHERS3.equals(val.getcType()) && val.isColumnShow()).findFirst().get().getcName() : null ;
								gl.setDescription(tName!=null ? tName : null);
								if(others3Map!=null && others3Map.size()>0 && others3Map.containsKey(counter)) {
									Integer id = others3Map.get(counter);
									gl.setAccount(chartOfAccountsDao.getLedgerName(id,  receiptVo.getOrganizationId()));
									gl.setAccountId(id);
								}
								gl.setLedgerList(allLedgers);
								gl.setSubLedger(onAccountNo);
								Double ot3amntPaid = Double.valueOf(vo.getOthers3());
								Double xchangeRate = receiptVo.getExchangeRate() !=null && !"".equals(receiptVo.getExchangeRate()) ? Double.valueOf(receiptVo.getExchangeRate())  : 1.00;
								if(ot3amntPaid > 0) {
									gl.setFcyDebit(Double.valueOf(df.format( ot3amntPaid)));
									gl.setInrDebit(Double.valueOf(df.format(xchangeRate * ot3amntPaid)));
								}else{
									gl.setFcyCredit(Double.valueOf(df.format( ot3amntPaid * -1)));
									gl.setInrCredit(Double.valueOf(df.format(xchangeRate * ot3amntPaid * -1)));
								}
								generalLedgers.add(gl);
								}

							
							break;

						}
					}
					counter++;
				}
			}else {
				GeneralLedgerDetailsVo gl = new GeneralLedgerDetailsVo();
				gl.setTempId(receiptVo.getCustomerId());
				gl.setType(JournalEntriesConstants.SUBLEDGER_TYPE_CUSTOMER);
				gl.setDescription(receiptVo.getReceiptBulkDetails().get(0).getReferenceType().replace("Create New ", "").trim());
				gl.setProductId(receiptVo.getReceiptBulkDetails().get(0).getReferenceId());
				logger.info("customerMap" +customerMap);
				if(customerMap!=null && customerMap.size()>0 && customerMap.containsKey(receiptVo.getCustomerId())) {
					int id = customerMap.get(receiptVo.getCustomerId());
					gl.setAccount(chartOfAccountsDao.getLedgerName(id,  receiptVo.getOrganizationId()));
					gl.setAccountId(id);

				}else {
					gl.setAccount(customerLedger);
					gl.setAccountId(customerAccountId);
				}
				
				gl.setLedgerList(chartOfAccountsDao.getLedgersByEntity(receiptVo.getOrganizationId(), ChartOfAccountsConstants.ENTITY_CUSTOMER));
				Connection connection = getAccountsReceivableConnection();
				String arInvoieNo  = receiptVo.getReceiptBulkDetails().get(0).getReferenceId()!=null ? 
						arInvoiceDao.getInvoiceOrderNo(receiptVo.getReceiptBulkDetails().get(0).getReferenceId(), receiptVo.getOrganizationId(), connection) : null ;
				closeResources(null, null, connection);
				gl.setSubLedger( arInvoieNo!=null ? arInvoieNo : customerDao.getCustomerName(receiptVo.getCustomerId()));
				Double amntPaid = Double.valueOf(receiptVo.getAmount());
				gl.setFcyCredit(Double.valueOf(df.format( amntPaid)));
				Double xchangeRate = receiptVo.getExchangeRate() !=null && !"".equals(receiptVo.getExchangeRate()) ? Double.valueOf(receiptVo.getExchangeRate())  : 1.00;
				gl.setInrCredit(Double.valueOf(df.format(xchangeRate * amntPaid)));
				generalLedgers.add(gl);
			}
			}
		
		logger.info("List:::"+generalLedgers);
		}catch(Exception e) {
			logger.info("Error in getPaymentsAndGroupByLedgers:: ",e);
			e.printStackTrace();
			throw new ApplicationException(e);
		}
	return generalLedgers;
	}









	private List<GeneralLedgerDetailsVo> getGLForVendorAdvanceType(ReceiptVo receiptVo, String accountType) throws ApplicationException {
		List<GeneralLedgerDetailsVo> generalLedgers= new ArrayList<>();
		DecimalFormat df = new DecimalFormat("0.00");
	try { 
		GeneralLedgerVo glData = receiptVo!=null && receiptVo.getGeneralLedgerData()!=null ? receiptVo.getGeneralLedgerData() : null;
		
		//To get the Bank related GL details 
		if(receiptVo.getBankId() !=null && !receiptVo.getBankId().isEmpty() && !"0".equals(receiptVo.getBankId() )) {
			GeneralLedgerDetailsVo gl = new GeneralLedgerDetailsVo();
			String bankLedger =  bankMasterDao.getAccountName(Integer.valueOf(receiptVo.getBankId()), accountType);
			Integer bankLedgerId = chartOfAccountsDao.getLedgerIdGivenName(bankLedger, receiptVo.getOrganizationId());
			gl.setType(JournalEntriesConstants.SUBLEDGER_TYPE_BANKING);
			gl.setDescription(accountType);
			gl.setTempId(Integer.valueOf(receiptVo.getBankId()));
		/*	if(glData!=null && glData.getGlDetails()!=null && !glData.getGlDetails().isEmpty() &&  glData.getGlDetails().get(0) !=null && JournalEntriesConstants.SUBLEDGER_TYPE_BANKING.equals(glData.getGlDetails().get(0).getType()) && glData.getGlDetails().get(0).getTempId() == Integer.valueOf(receiptVo.getBankId())) {
				gl.setAccount( glData.getGlDetails().get(0).getAccount());
				gl.setAccountId( glData.getGlDetails().get(0).getAccountId());
				gl.setLedgerList(chartOfAccountsDao.getLedgersAndItsSiblingsByLedgerName(receiptVo.getOrganizationId(), glData.getGlDetails().get(0).getAccount()));
				gl.setSubLedger(glData.getGlDetails().get(0).getSubLedger());
			}else {*/
				gl.setAccount(bankLedger);
				gl.setAccountId(bankLedgerId);
				gl.setLedgerList(chartOfAccountsDao.getLedgerById(receiptVo.getOrganizationId() , bankLedgerId));
				gl.setSubLedger(accountType);
		//	}
			Double amntPaid = Double.valueOf(receiptVo.getAmount());
			gl.setFcyDebit(Double.valueOf(amntPaid));
			Double xchangeRate = receiptVo.getExchangeRate() !=null && !"".equals(receiptVo.getExchangeRate()) ? Double.valueOf(receiptVo.getExchangeRate())  : 1.00;
			gl.setInrDebit(Double.valueOf(df.format(xchangeRate * amntPaid)));
			generalLedgers.add(gl);
			}
		
		
		//To get the Vendor related GL details 
		if(receiptVo.getVendorId()!=null && receiptVo.getVendorId()!=0 ) {
			String vendorLedger = chartOfAccountsDao.getDefaultLedger(ChartOfAccountsConstants.ENTITY_VENDOR, null, null);
			logger.info("Vendor Ledger::"+vendorLedger);
			Integer vendorAccountId  = chartOfAccountsDao.getLedgerIdGivenName(vendorLedger, receiptVo.getOrganizationId());
			if(receiptVo.getVendorReceiptDetails()!=null && receiptVo.getVendorReceiptDetails().size()>0) {
				Map<Integer, Integer> vendorMap = glData != null && glData.getGlDetails() != null ? glData.getGlDetails()
						.stream().filter(detail -> JournalEntriesConstants.SUBLEDGER_TYPE_VENDOR.equals(detail.getType()))
						.collect(Collectors.toMap(GeneralLedgerDetailsVo::getProductId, GeneralLedgerDetailsVo::getAccountId))
						: null;
						logger.info("vendorMap ::>>"+vendorMap);
						
				for(VendorRefundReceiptDetailsVo vo : receiptVo.getVendorReceiptDetails()) {
					GeneralLedgerDetailsVo gl = new GeneralLedgerDetailsVo();
					gl.setTempId(receiptVo.getVendorId());
					gl.setProductId(vo.getInvoiceId());
					gl.setType(JournalEntriesConstants.SUBLEDGER_TYPE_VENDOR);
					gl.setDescription(JournalEntriesConstants.BILL);
					if(vendorMap!=null && vendorMap.size()>0 && vendorMap.containsKey(vo.getInvoiceId())) {
						Integer id =  vendorMap.get(vo.getInvoiceId());
						String name = chartOfAccountsDao.getLedgerName(id, receiptVo.getOrganizationId());
						gl.setAccount(name);
						gl.setAccountId(id);
					}else {
						gl.setAccount(vendorLedger);
						gl.setAccountId(vendorAccountId);
					}
					gl.setLedgerList(chartOfAccountsDao.getLedgersByEntity(receiptVo.getOrganizationId(), ChartOfAccountsConstants.ENTITY_VENDOR));
					gl.setSubLedger(vo.getInvoiceRefNo());
					Double amntPaid = Double.valueOf(vo.getRefundAmount());
					gl.setFcyCredit(Double.valueOf(df.format( amntPaid)));
					Double xchangeRate = receiptVo.getExchangeRate() !=null && !"".equals(receiptVo.getExchangeRate()) ? Double.valueOf(receiptVo.getExchangeRate())  : 1.00;
					gl.setInrCredit(Double.valueOf(df.format(xchangeRate * amntPaid)));
					generalLedgers.add(gl);
				}
			}else {
				Map<Integer, Integer> vendorMap = glData != null && glData.getGlDetails() != null ? glData.getGlDetails()
						.stream().filter(detail -> JournalEntriesConstants.SUBLEDGER_TYPE_VENDOR.equals(detail.getType()))
						.collect(Collectors.toMap(GeneralLedgerDetailsVo::getTempId, GeneralLedgerDetailsVo::getAccountId))
						: null;
						logger.info("vendorMap ::>>"+vendorMap);
				GeneralLedgerDetailsVo gl = new GeneralLedgerDetailsVo();
				gl.setTempId(receiptVo.getVendorId());
				gl.setType(JournalEntriesConstants.SUBLEDGER_TYPE_VENDOR);
				gl.setDescription(JournalEntriesConstants.SUBLEDGER_TYPE_VENDOR);
				if(vendorMap!=null && vendorMap.size()>0 && vendorMap.containsKey(receiptVo.getVendorId())) {
					Integer id = vendorMap.get(receiptVo.getVendorId());
					gl.setAccount(chartOfAccountsDao.getLedgerName(id, receiptVo.getOrganizationId()));
					gl.setAccountId(id);
				}else {
					gl.setAccount(vendorLedger);
					gl.setAccountId(vendorAccountId);
				}
				
				gl.setLedgerList(chartOfAccountsDao.getLedgersByEntity(receiptVo.getOrganizationId(), ChartOfAccountsConstants.ENTITY_VENDOR));
				gl.setSubLedger(vendorDao.getVendorName(receiptVo.getVendorId()));
				Double amntPaid = Double.valueOf(receiptVo.getAmount());
				gl.setFcyCredit(Double.valueOf(df.format( amntPaid)));
				Double xchangeRate = receiptVo.getExchangeRate() !=null && !"".equals(receiptVo.getExchangeRate()) ? Double.valueOf(receiptVo.getExchangeRate())  : 1.00;
				gl.setInrCredit(Double.valueOf(df.format(xchangeRate * amntPaid)));
				generalLedgers.add(gl);
			}
			}
		}catch(Exception e) {
			logger.info("Error in getPaymentsAndGroupByLedgers:: ",e);
			e.printStackTrace();
			throw new ApplicationException(e);
		}
	return generalLedgers;
	}









	private List<GeneralLedgerDetailsVo> getGLForOtherReceiptType(ReceiptVo receiptVo, String accountType) throws ApplicationException {
		List<GeneralLedgerDetailsVo> generalLedgers= new ArrayList<>();
		DecimalFormat df = new DecimalFormat("0.00");
	try { 
		GeneralLedgerVo glData = receiptVo!=null && receiptVo.getGeneralLedgerData()!=null ? receiptVo.getGeneralLedgerData() : null;
			
		//To get the Bank related GL details 
		if(receiptVo.getBankId() !=null && !receiptVo.getBankId().isEmpty() && !"0".equals(receiptVo.getBankId() )) {
			GeneralLedgerDetailsVo gl = new GeneralLedgerDetailsVo();
			String bankLedger =  bankMasterDao.getAccountName(Integer.valueOf(receiptVo.getBankId()), accountType);
			Integer bankLedgerId = chartOfAccountsDao.getLedgerIdGivenName(bankLedger, receiptVo.getOrganizationId());
			gl.setType(JournalEntriesConstants.SUBLEDGER_TYPE_BANKING);
			gl.setDescription(accountType);
			gl.setTempId(Integer.valueOf(receiptVo.getBankId()));
			/*if(glData!=null && glData.getGlDetails()!=null && !glData.getGlDetails().isEmpty() &&  glData.getGlDetails().get(0) !=null && JournalEntriesConstants.SUBLEDGER_TYPE_BANKING.equals(glData.getGlDetails().get(0).getType()) && glData.getGlDetails().get(0).getTempId() == Integer.valueOf(receiptVo.getBankId())) {
				gl.setAccount( glData.getGlDetails().get(0).getAccount());
				gl.setAccountId( glData.getGlDetails().get(0).getAccountId());
				gl.setLedgerList(chartOfAccountsDao.getLedgersAndItsSiblingsByLedgerName(receiptVo.getOrganizationId(), glData.getGlDetails().get(0).getAccount()));
				gl.setSubLedger(glData.getGlDetails().get(0).getSubLedger());
			}else {*/
				gl.setAccount(bankLedger);
				gl.setAccountId(bankLedgerId);
				gl.setLedgerList(chartOfAccountsDao.getLedgerById(receiptVo.getOrganizationId(), bankLedgerId));
				
				gl.setSubLedger(accountType);
			//}
			Double amntPaid = Double.valueOf(receiptVo.getAmount());
			gl.setFcyDebit(Double.valueOf(amntPaid));
			Double xchangeRate = receiptVo.getExchangeRate() !=null && !"".equals(receiptVo.getExchangeRate()) ? Double.valueOf(receiptVo.getExchangeRate())  : 1.00;
			gl.setInrDebit(Double.valueOf(df.format(xchangeRate * amntPaid)));
			generalLedgers.add(gl);
			}
		
		
		// to get the GL detail for contact Type 
		if(receiptVo.getContactId()!=0 && receiptVo.getContactType()!=null &&  !receiptVo.getContactType().isEmpty()) {
			String contactLedger = null;
			Integer contactAccountId  = 0;
			GeneralLedgerDetailsVo gl = new GeneralLedgerDetailsVo();
			if(ReceiptConstants.RECEIPT_CONTACT_TYPE_STATUTORY_BODY.equals(receiptVo.getContactType())) {
				gl.setType(PaymentNonCoreConstants.STATUTORY_BODY);
				gl.setDescription(PaymentNonCoreConstants.STATUTORY_BODY);
				gl.setSubLedger(receiptVo.getContactName());
				if (glData != null && glData.getGlDetails() != null && glData.getGlDetails().size() > 0	&& glData.getGlDetails().get(1).getType() != null
						&& PaymentNonCoreConstants.STATUTORY_BODY.equals(glData.getGlDetails().get(1).getType())) {
					contactLedger  =  chartOfAccountsDao.getLedgerName(glData.getGlDetails().get(1).getAccountId(), receiptVo.getOrganizationId());
					contactAccountId = glData.getGlDetails().get(1).getAccountId();
				}else {
					contactLedger  = null;
					contactAccountId = 0;
				}
				gl.setLedgerList(chartOfAccountsDao.getLedgersByEntity(receiptVo.getOrganizationId(), ChartOfAccountsConstants.ENTITY_VENDOR));
			}
			if(ReceiptConstants.RECEIPT_CONTACT_TYPE_CUSTOMER.equals(receiptVo.getContactType())) {
				gl.setType(PaymentNonCoreConstants.CUSTOMER);
				gl.setDescription(JournalEntriesConstants.SUBLEDGER_TYPE_CUSTOMER);
				gl.setSubLedger(receiptVo.getContactName());
				
				if (glData != null && glData.getGlDetails() != null && glData.getGlDetails().size() > 0	&& glData.getGlDetails().get(1).getType() != null
						&& PaymentNonCoreConstants.CUSTOMER.equals(glData.getGlDetails().get(1).getType())) {
					contactLedger  =  chartOfAccountsDao.getLedgerName(glData.getGlDetails().get(1).getAccountId(), receiptVo.getOrganizationId());
					contactAccountId = glData.getGlDetails().get(1).getAccountId();
				} else {
				contactLedger = chartOfAccountsDao.getDefaultLedger(ChartOfAccountsConstants.ENTITY_CUSTOMER, null, null);
				contactAccountId = chartOfAccountsDao.getLedgerIdGivenName(contactLedger, receiptVo.getOrganizationId());
				}
				gl.setLedgerList(chartOfAccountsDao.getLedgersByEntity(receiptVo.getOrganizationId(), ChartOfAccountsConstants.ENTITY_CUSTOMER));

			}
			if(ReceiptConstants.RECEIPT_CONTACT_TYPE_VENDOR.equals(receiptVo.getContactType())) {
				gl.setType(PaymentNonCoreConstants.VENDOR);
				gl.setDescription(JournalEntriesConstants.SUBLEDGER_TYPE_VENDOR);
				if(glData != null && glData.getGlDetails() != null && glData.getGlDetails().size() > 0	&& glData.getGlDetails().get(1).getType() != null
						&& PaymentNonCoreConstants.VENDOR.equals(glData.getGlDetails().get(1).getType())) {
					contactLedger  =  chartOfAccountsDao.getLedgerName(glData.getGlDetails().get(1).getAccountId(), receiptVo.getOrganizationId());
					contactAccountId = glData.getGlDetails().get(1).getAccountId();
				}else {
						contactLedger =  chartOfAccountsDao.getDefaultLedger(ChartOfAccountsConstants.ENTITY_VENDOR, null, null);
						contactAccountId = chartOfAccountsDao.getLedgerIdGivenName(contactLedger, receiptVo.getOrganizationId());
				}
				gl.setSubLedger(receiptVo.getContactName());
				gl.setLedgerList(chartOfAccountsDao.getLedgersByEntity(receiptVo.getOrganizationId(), ChartOfAccountsConstants.ENTITY_VENDOR));

			}
			if(ReceiptConstants.RECEIPT_CONTACT_TYPE_EMPLOYEE.equals(receiptVo.getContactType())) {
				gl.setType(PaymentNonCoreConstants.EMPLOYEE);
				gl.setDescription(JournalEntriesConstants.SUBLEDGER_TYPE_EMPLOYEE);
				if(glData != null && glData.getGlDetails() != null && glData.getGlDetails().size() > 0	&& glData.getGlDetails().get(1).getType() != null
						&& PaymentNonCoreConstants.EMPLOYEE.equals(glData.getGlDetails().get(1).getType())) {
					contactLedger  =  chartOfAccountsDao.getLedgerName(glData.getGlDetails().get(1).getAccountId(), receiptVo.getOrganizationId());
					contactAccountId = glData.getGlDetails().get(1).getAccountId();
				}else {
				contactLedger =   chartOfAccountsDao.getDefaultLedger(ChartOfAccountsConstants.ENTITY_EMPLOYEE_PAYABLE, null, null);
				contactAccountId = chartOfAccountsDao.getLedgerIdGivenName(contactLedger, receiptVo.getOrganizationId());
				}
				gl.setSubLedger(receiptVo.getContactName());
				gl.setLedgerList(chartOfAccountsDao.getLedgersByEntity(receiptVo.getOrganizationId(), ChartOfAccountsConstants.ENTITY_EMPLOYEE_PAYABLE));

			}
					gl.setProductId(receiptVo.getContactId());
					gl.setTempId(Integer.valueOf( receiptVo.getContactId()));
					gl.setAccount(contactLedger);
					gl.setAccountId(contactAccountId);
					Double amntPaid = Double.valueOf(receiptVo.getAmount());
					gl.setFcyCredit(Double.valueOf(df.format( amntPaid)));
					Double xchangeRate = receiptVo.getExchangeRate() !=null && !"".equals(receiptVo.getExchangeRate()) ? Double.valueOf(receiptVo.getExchangeRate())  : 1.00;
					gl.setInrCredit(Double.valueOf(df.format(xchangeRate * amntPaid)));
					generalLedgers.add(gl);
				
			}
		}catch(Exception e) {
			logger.info("Error in getPaymentsAndGroupByLedgers:: ",e);
			e.printStackTrace();
			throw new ApplicationException(e);
		}
	return generalLedgers;
	}









	private List<GeneralLedgerDetailsVo> getPaymentsAndGroupByLedgers(PaymentNonCoreVo paymentsVo) throws ApplicationException {
		List<GeneralLedgerDetailsVo> generalLedgers = new ArrayList<GeneralLedgerDetailsVo>();
		logger.info("To method getPaymentsAndGroupByLedgers with Id" +paymentsVo);
		try{
			if(paymentsVo!=null && paymentsVo.getPaymentType()!=0 && paymentsVo.getPaymentMode()!=0) {
				String accountType = null;

				switch(paymentsVo.getPaymentMode()) {
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
				
				switch(paymentsVo.getPaymentType()) {
				case 1: // Bills Invoices Payments Type
					generalLedgers = getGLForBillsPaymetsType(paymentsVo , accountType);
					break;
				case 2: // Vendor Advance Payment type
					generalLedgers = getGLForVendorAdvanceType(paymentsVo , accountType);
					break;
				case 3: // GST Payment type
					generalLedgers = getGLForGSTPaymentType(paymentsVo , accountType);
					break;
				case 4: // TDS Payment type
					generalLedgers = getGLForTDSPaymentType(paymentsVo , accountType);
					break;
				case 5: // Customer Refunds type
					generalLedgers = getGLForCustomerRefundsPaymentType(paymentsVo , accountType);
					break;
				case 6: // Other Payments type
					generalLedgers = getGLForOtherPaymentType(paymentsVo , accountType);
					break;
					
				case 7: // Employee Payments type
					generalLedgers = getGLForEmployeePaymentType(paymentsVo , accountType);
					break;
				}
			}
			logger.info("GL data List :: "+generalLedgers.size());
		}catch (Exception e) {
			logger.info("Error in getPaymentsAndGroupByLedgers:: ",e);
			e.printStackTrace();
			throw new ApplicationException(e);
		}
		
		return generalLedgers;
	}



private List<GeneralLedgerDetailsVo> getGLForEmployeePaymentType(PaymentNonCoreVo paymentsVo, String accountType) throws ApplicationException {

	List<GeneralLedgerDetailsVo> generalLedgers= new ArrayList<>();
	DecimalFormat df = new DecimalFormat("0.00");
try { 
	GeneralLedgerVo glData = paymentsVo!=null && paymentsVo.getGeneralLedgerData()!=null ? paymentsVo.getGeneralLedgerData() : null;
	Map<Integer, Integer> payableMap = glData != null && glData.getGlDetails() != null ? glData.getGlDetails()
			.stream().filter(detail -> JournalEntriesConstants.PAYABLE_AMOUNT.equals(detail.getType()) && detail.getAccount()!=null && detail.getProductId()!=null)
			.collect(Collectors.toMap(GeneralLedgerDetailsVo::getTempId, GeneralLedgerDetailsVo::getAccountId ))
			: null;
	
	Map<Integer, Integer> others1Map = glData != null && glData.getGlDetails() != null ? glData.getGlDetails()
			.stream().filter(detail -> JournalEntriesConstants.OTHERS1.equals(detail.getType()) && detail.getAccount()!=null && detail.getProductId()!=null)
			.collect(Collectors.toMap(GeneralLedgerDetailsVo::getTempId, GeneralLedgerDetailsVo::getAccountId ))
			: null;
			
	Map<Integer, Integer> others2Map = glData != null && glData.getGlDetails() != null ? glData.getGlDetails()
			.stream().filter(detail -> JournalEntriesConstants.OTHERS2.equals(detail.getType()) && detail.getAccount()!=null && detail.getProductId()!=null)
			.collect(Collectors.toMap(GeneralLedgerDetailsVo::getTempId, GeneralLedgerDetailsVo::getAccountId ))
			: null;
											
	Map<Integer, Integer> others3Map = glData != null && glData.getGlDetails() != null ? glData.getGlDetails()
			.stream().filter(detail -> JournalEntriesConstants.OTHERS3.equals(detail.getType()) && detail.getAccount()!=null && detail.getProductId()!=null)
			.collect(Collectors.toMap(GeneralLedgerDetailsVo::getTempId, GeneralLedgerDetailsVo::getAccountId ))
			: null;
			
	//To get the Bank related GL details 
	if(paymentsVo.getPaidVia() !=0) {
		GeneralLedgerDetailsVo gl = new GeneralLedgerDetailsVo();
		String bankLedger =  bankMasterDao.getAccountName(Integer.valueOf(paymentsVo.getPaidVia()), accountType);
		Integer bankLedgerId = chartOfAccountsDao.getLedgerIdGivenName(bankLedger, paymentsVo.getOrganizationId());
		gl.setType(JournalEntriesConstants.SUBLEDGER_TYPE_BANKING);
		gl.setDescription(accountType);
		gl.setTempId(Integer.valueOf(paymentsVo.getPaidVia()));
		/*if(glData!=null && glData.getGlDetails()!=null && !glData.getGlDetails().isEmpty() &&  glData.getGlDetails().get(0) !=null && JournalEntriesConstants.SUBLEDGER_TYPE_BANKING.equals(glData.getGlDetails().get(0).getType()) && glData.getGlDetails().get(0).getTempId() == Integer.valueOf(receiptVo.getBankId())) {
			gl.setAccount( glData.getGlDetails().get(0).getAccount());
			gl.setAccountId( glData.getGlDetails().get(0).getAccountId());
			gl.setLedgerList(chartOfAccountsDao.getLedgersAndItsSiblingsByLedgerName(receiptVo.getOrganizationId(), glData.getGlDetails().get(0).getAccount()));
			gl.setSubLedger(glData.getGlDetails().get(0).getSubLedger());
		}else {*/
			gl.setAccount(bankLedger);
			gl.setAccountId(bankLedgerId);
			gl.setLedgerList(chartOfAccountsDao.getLedgerById(paymentsVo.getOrganizationId() , bankLedgerId));
			gl.setSubLedger(accountType);
		//}
		Double amntPaid = Double.valueOf(paymentsVo.getAmountPaid());
		gl.setFcyCredit(Double.valueOf(amntPaid));
		Double xchangeRate = paymentsVo.getExchangeRate() !=null && !"".equals(paymentsVo.getExchangeRate()) ? Double.valueOf(paymentsVo.getExchangeRate())  : 1.00;
		gl.setInrCredit(Double.valueOf(df.format(xchangeRate * amntPaid)));
		generalLedgers.add(gl);
		}
	
	if(paymentsVo.isBulk()) {
	//To get the Employee related GL details 
	if(paymentsVo.getPayments()!=null  && paymentsVo.getPayments().size()>0) {
		String employeeLedger =  chartOfAccountsDao.getDefaultLedger(ChartOfAccountsConstants.ENTITY_EMPLOYEE_PAYABLE, ChartOfAccountsConstants.ENTITY_EMPLOYEE, null);
		Integer employeeAccountId  = chartOfAccountsDao.getLedgerIdGivenName(employeeLedger, paymentsVo.getOrganizationId());
			List<MinimalChartOfAccountsVo> allLedgers = chartOfAccountsDao.getAllLedgersByOrgId(paymentsVo.getOrganizationId());
			int counter = 1;			
			for(PaymentNonCoreBaseVo vo : paymentsVo.getPayments()) {
				String employeename  = null;
				logger.info("Counter:"+counter);
				if(vo.getEmpName()!=null && !"".equals(vo.getEmpName()) && !vo.getEmpName().equals("0")) {
							employeename  = employeeDao.getEmployeeName(Integer.valueOf(vo.getEmpName()));
							
						//For employeePayables
						if(vo.getPayable()!=null &&  !vo.getPayable().equals("")  && !vo.getPayable().equals("0") ){
							GeneralLedgerDetailsVo gl = new GeneralLedgerDetailsVo();
							gl.setTempId(counter);
							gl.setProductId(vo.getReferenceId());
							gl.setType(JournalEntriesConstants.PAYABLE_AMOUNT);
							gl.setDescription(JournalEntriesConstants.PAYABLE_AMOUNT);
							if(payableMap!=null && payableMap.size()>0 && payableMap.containsKey(counter)) {
								String name = chartOfAccountsDao.getLedgerName(payableMap.get(counter) , paymentsVo.getOrganizationId()) ;
								gl.setAccount(name);
								gl.setAccountId(payableMap.get(counter));
	
							}else {
								gl.setAccount(employeeLedger);
								gl.setAccountId(employeeAccountId);
							}
							gl.setProductId(Integer.valueOf(vo.getEmpName()));
							gl.setLedgerList(chartOfAccountsDao.getLedgersByEntity(paymentsVo.getOrganizationId(), ChartOfAccountsConstants.ENTITY_EMPLOYEE_PAYABLE));
							gl.setSubLedger(employeename);
							Double invAmntPaid = Double.valueOf(vo.getPayable());
							Double xchangeRate = paymentsVo.getExchangeRate() !=null && !"".equals(paymentsVo.getExchangeRate()) ? Double.valueOf(paymentsVo.getExchangeRate())  : 1.00;
							if(invAmntPaid > 0) {
								gl.setFcyDebit(Double.valueOf(df.format( invAmntPaid)));
								gl.setInrDebit(Double.valueOf(df.format(xchangeRate * invAmntPaid)));
							}else {
								gl.setFcyCredit(Double.valueOf(df.format( invAmntPaid  * -1)));
								gl.setInrCredit(Double.valueOf(df.format(xchangeRate * invAmntPaid  * -1)));
							}
							
							generalLedgers.add(gl);
						}
						
						
						
						//for Others 1
						if(vo.getOthers1()!=null &&  !vo.getOthers1().equals("")  && !vo.getOthers1().equals("0") ){
							GeneralLedgerDetailsVo gl = new GeneralLedgerDetailsVo();
							gl.setTempId(counter);
							gl.setProductId(Integer.valueOf(vo.getEmpName()));
							gl.setType(JournalEntriesConstants.OTHERS1);
							String tName  = paymentsVo.getCustomTableList()!=null && paymentsVo.getCustomTableList().size()> 0 ? 
									paymentsVo.getCustomTableList().stream().filter(val -> JournalEntriesConstants.OTHERS1.equals(val.getcName()) && val.isColumnShow()).findAny().get().getColName(): null;
							gl.setDescription(tName!=null ? tName : null);
							if(others1Map!=null && others1Map.size()>0 && others1Map.containsKey(counter)) {
								String name = chartOfAccountsDao.getLedgerName(others1Map.get(counter) , paymentsVo.getOrganizationId()) ;
								gl.setAccount(name);
								gl.setAccountId(others1Map.get(counter));
							}
							gl.setProductId(Integer.valueOf(vo.getEmpName()));
							gl.setLedgerList(allLedgers);
							gl.setSubLedger(employeename);
							Double ot1amntPaid = Double.valueOf(vo.getOthers1());
							Double xchangeRate = paymentsVo.getExchangeRate() !=null && !"".equals(paymentsVo.getExchangeRate()) ? Double.valueOf(paymentsVo.getExchangeRate())  : 1.00;
							if(ot1amntPaid > 0 ) {
								gl.setFcyDebit(Double.valueOf(df.format( ot1amntPaid)));
								gl.setInrDebit(Double.valueOf(df.format(xchangeRate * ot1amntPaid)));
							}else {
								gl.setFcyCredit(Double.valueOf(df.format( ot1amntPaid  * -1)));
								gl.setInrCredit(Double.valueOf(df.format(xchangeRate * ot1amntPaid  * -1)));
							}
							generalLedgers.add(gl);
							}
						
						//for Others 2
						if(vo.getOthers2()!=null &&  !vo.getOthers2().equals("")  && !vo.getOthers2().equals("0") ){
							GeneralLedgerDetailsVo gl = new GeneralLedgerDetailsVo();
							gl.setTempId(counter);
							gl.setProductId(vo.getReferenceId());
							gl.setType(JournalEntriesConstants.OTHERS2);
							String tName  = paymentsVo.getCustomTableList()!=null && paymentsVo.getCustomTableList().size()> 0 ? 
									paymentsVo.getCustomTableList().stream().filter(val -> JournalEntriesConstants.OTHERS2.equals(val.getcName()) && val.isColumnShow()).findAny().get().getColName(): null;
							gl.setDescription(tName!=null ? tName : null);
							if(others2Map!=null && others2Map.size()>0 && others2Map.containsKey(counter)) {
								String name = chartOfAccountsDao.getLedgerName(others2Map.get(counter) , paymentsVo.getOrganizationId()) ;
								gl.setAccount(name);
								gl.setAccountId( others2Map.get(counter));
							}
							gl.setProductId(Integer.valueOf(vo.getEmpName()));
							gl.setLedgerList(allLedgers);
							gl.setSubLedger(employeename);
							Double ot2amntPaid = Double.valueOf(vo.getOthers2());
							Double xchangeRate = paymentsVo.getExchangeRate() !=null && !"".equals(paymentsVo.getExchangeRate()) ? Double.valueOf(paymentsVo.getExchangeRate())  : 1.00;
							if(ot2amntPaid > 0 ) {
								gl.setFcyDebit(Double.valueOf(df.format( ot2amntPaid)));
								gl.setInrDebit(Double.valueOf(df.format(xchangeRate * ot2amntPaid)));
							}else {
								gl.setFcyCredit(Double.valueOf(df.format( ot2amntPaid  * -1)));
								gl.setInrCredit(Double.valueOf(df.format(xchangeRate * ot2amntPaid  * -1)));
							}
							
							generalLedgers.add(gl);
							}
						
						//for Others 3
						if(vo.getOthers3()!=null &&  !vo.getOthers3().equals("")  && !vo.getOthers3().equals("0") ){
							GeneralLedgerDetailsVo gl = new GeneralLedgerDetailsVo();
							gl.setTempId(counter);
							gl.setProductId(vo.getReferenceId());
							gl.setType(JournalEntriesConstants.OTHERS3);
							String tName  = paymentsVo.getCustomTableList()!=null && paymentsVo.getCustomTableList().size()> 0 ? 
									paymentsVo.getCustomTableList().stream().filter(val -> JournalEntriesConstants.OTHERS3.equals(val.getcName()) && val.isColumnShow()).findFirst().get().getColName() : null ;
							gl.setDescription(tName!=null ? tName : null);
							if(others3Map!=null && others3Map.size()>0 && others3Map.containsKey(counter)) {
								String name = chartOfAccountsDao.getLedgerName(others3Map.get(counter) , paymentsVo.getOrganizationId()) ;
								gl.setAccount(name);
								gl.setAccountId(others3Map.get(counter));
							}
							gl.setProductId(Integer.valueOf(vo.getEmpName()));
							gl.setLedgerList(allLedgers);
							gl.setSubLedger(employeename);
							Double ot3amntPaid = Double.valueOf(vo.getOthers3());
							Double xchangeRate = paymentsVo.getExchangeRate() !=null && !"".equals(paymentsVo.getExchangeRate()) ? Double.valueOf(paymentsVo.getExchangeRate())  : 1.00;
							if(ot3amntPaid > 0 ) {
								gl.setFcyDebit(Double.valueOf(df.format( ot3amntPaid)));
								gl.setInrDebit(Double.valueOf(df.format(xchangeRate * ot3amntPaid)));
							}else {
								gl.setFcyCredit(Double.valueOf(df.format( ot3amntPaid  * -1)));
								gl.setInrCredit(Double.valueOf(df.format(xchangeRate * ot3amntPaid  * -1)));
							}
							
							generalLedgers.add(gl);
							}
					}
				counter++;
				}
			}
	}else {
		if(paymentsVo.getEmployeeName()!=0){
			GeneralLedgerDetailsVo gl = new GeneralLedgerDetailsVo();
			gl.setTempId(paymentsVo.getEmployeeName());
			gl.setType(JournalEntriesConstants.SUBLEDGER_TYPE_EMPLOYEE);
			gl.setDescription(JournalEntriesConstants.SUBLEDGER_TYPE_EMPLOYEE);
			if(glData!=null && glData.getGlDetails()!=null && glData.getGlDetails().size()>1 &&  glData.getGlDetails().get(1) !=null && JournalEntriesConstants.SUBLEDGER_TYPE_EMPLOYEE.equals(glData.getGlDetails().get(1).getType()) && glData.getGlDetails().get(1).getTempId() == Integer.valueOf(paymentsVo.getEmployeeName())) {
				gl.setAccount( glData.getGlDetails().get(1).getAccount());
				gl.setAccountId( glData.getGlDetails().get(1).getAccountId());
			}else {
				String employeeLedger =  chartOfAccountsDao.getDefaultLedger(ChartOfAccountsConstants.ENTITY_EMPLOYEE_PAYABLE, ChartOfAccountsConstants.ENTITY_EMPLOYEE, null);
				Integer employeeAccountId  = chartOfAccountsDao.getLedgerIdGivenName(employeeLedger, paymentsVo.getOrganizationId());
				gl.setAccount(employeeLedger);
				gl.setAccountId(employeeAccountId);
			}
			gl.setProductId(Integer.valueOf(paymentsVo.getEmployeeName()));
			gl.setLedgerList(chartOfAccountsDao.getLedgersByEntity(paymentsVo.getOrganizationId(), ChartOfAccountsConstants.ENTITY_EMPLOYEE_PAYABLE));
			gl.setSubLedger(employeeDao.getEmployeeName(paymentsVo.getEmployeeName()));
			Double invAmntPaid = Double.valueOf(paymentsVo.getAmountPaid());
			Double xchangeRate = paymentsVo.getExchangeRate() !=null && !"".equals(paymentsVo.getExchangeRate()) ? Double.valueOf(paymentsVo.getExchangeRate())  : 1.00;
			if(invAmntPaid > 0) {
				gl.setFcyDebit(Double.valueOf(df.format( invAmntPaid)));
				gl.setInrDebit(Double.valueOf(df.format(xchangeRate * invAmntPaid)));
			}else {
				gl.setFcyDebit(Double.valueOf(df.format( invAmntPaid  * -1)));
				gl.setInrDebit(Double.valueOf(df.format(xchangeRate * invAmntPaid * -1)));
			}
			
			generalLedgers.add(gl);
		}
	}
	logger.info("List:::"+generalLedgers);
	}catch(Exception e) {
		logger.info("Error in getPaymentsAndGroupByLedgers:: ",e);
		e.printStackTrace();
		throw new ApplicationException(e);
	}
return generalLedgers;

	}







private List<GeneralLedgerDetailsVo> getGLForOtherPaymentType(PaymentNonCoreVo paymentsVo, String accountType) throws ApplicationException {

	List<GeneralLedgerDetailsVo> generalLedgers= new ArrayList<>();
	DecimalFormat df = new DecimalFormat("0.00");
try { 
	GeneralLedgerVo glData = paymentsVo!=null && paymentsVo.getGeneralLedgerData()!=null ? paymentsVo.getGeneralLedgerData() : null;
		
	//To get the Bank related GL details 
	if(paymentsVo.getPaidVia()!=0 ) {
		GeneralLedgerDetailsVo gl = new GeneralLedgerDetailsVo();
		String bankLedger =  paymentsVo.getPaidViaName();
		Integer baankLedgerId = chartOfAccountsDao.getLedgerIdGivenName(bankLedger, paymentsVo.getOrganizationId());
		gl.setType(JournalEntriesConstants.SUBLEDGER_TYPE_BANKING);
		gl.setDescription(accountType);
		gl.setTempId(paymentsVo.getPaidVia());
		/*if(glData!=null && glData.getGlDetails()!=null && !glData.getGlDetails().isEmpty() &&  glData.getGlDetails().get(0) !=null && JournalEntriesConstants.SUBLEDGER_TYPE_BANKING.equals(glData.getGlDetails().get(0).getType()) && glData.getGlDetails().get(0).getTempId() == paymentsVo.getPaidVia()) {
			gl.setAccount( glData.getGlDetails().get(0).getAccount());
			gl.setAccountId( glData.getGlDetails().get(0).getAccountId());
			gl.setLedgerList(chartOfAccountsDao.getLedgersAndItsSiblingsByLedgerName(paymentsVo.getOrganizationId(), glData.getGlDetails().get(0).getAccount()));
			gl.setSubLedger(glData.getGlDetails().get(0).getSubLedger());
		}else {*/
			gl.setAccount(bankLedger);
			gl.setAccountId(baankLedgerId);
			gl.setLedgerList(chartOfAccountsDao.getLedgerById(paymentsVo.getOrganizationId(), baankLedgerId));
			gl.setSubLedger(paymentsVo.getPaidViaName());
	//	}
		Double amntPaid = Double.valueOf(paymentsVo.getAmountPaid());
		gl.setFcyCredit(Double.valueOf(amntPaid));
		Double xchangeRate = paymentsVo.getExchangeRate() !=null && !"".equals(paymentsVo.getExchangeRate()) ? Double.valueOf(paymentsVo.getExchangeRate())  : 1.00;
		gl.setInrCredit(Double.valueOf(df.format(xchangeRate * amntPaid)));
		generalLedgers.add(gl);
		}
	
	
	// to get the GL detail for contact Type 
	if(paymentsVo.getContactId()!=0 && paymentsVo.getContactType()!=null &&  !paymentsVo.getContactType().isEmpty()) {
		String contactLedger = null;
		Integer contactAccountId  = 0;
		List<MinimalChartOfAccountsVo> allLedgers = chartOfAccountsDao.getAllLedgersByOrgId(paymentsVo.getOrganizationId());
		GeneralLedgerDetailsVo gl = new GeneralLedgerDetailsVo();
		if(PaymentNonCoreConstants.STATUTORY_BODY.equals(paymentsVo.getContactType())) {
			gl.setType(PaymentNonCoreConstants.STATUTORY_BODY);
			gl.setDescription(PaymentNonCoreConstants.STATUTORY_BODY);
			gl.setSubLedger(paymentsVo.getContactName());
			if (glData != null && glData.getGlDetails() != null && glData.getGlDetails().size() > 0	&& glData.getGlDetails().get(1).getType() != null
					&& PaymentNonCoreConstants.STATUTORY_BODY.equals(glData.getGlDetails().get(1).getType())) {
				contactAccountId = glData.getGlDetails().get(1).getAccountId();
				String name = chartOfAccountsDao.getLedgerName(contactAccountId , paymentsVo.getOrganizationId()) ;
				contactLedger  = name!=null ? name :  glData.getGlDetails().get(1).getAccount();
			}else {
				contactLedger  = null;
				contactAccountId = 0;
			}
			gl.setLedgerList(allLedgers);

		}
		if(PaymentNonCoreConstants.CUSTOMER.equals(paymentsVo.getContactType())) {
			gl.setType(PaymentNonCoreConstants.CUSTOMER);
			gl.setDescription(JournalEntriesConstants.SUBLEDGER_TYPE_CUSTOMER);
			gl.setSubLedger(paymentsVo.getContactName());
			gl.setLedgerList(chartOfAccountsDao.getLedgersByEntity(paymentsVo.getOrganizationId(), ChartOfAccountsConstants.ENTITY_CUSTOMER));
			if (glData != null && glData.getGlDetails() != null && glData.getGlDetails().size() > 0	&& glData.getGlDetails().get(1).getType() != null
					&& PaymentNonCoreConstants.CUSTOMER.equals(glData.getGlDetails().get(1).getType())) {
				contactAccountId = glData.getGlDetails().get(1).getAccountId();
				String name = chartOfAccountsDao.getLedgerName(contactAccountId , paymentsVo.getOrganizationId()) ;
				contactLedger  = name!=null ? name :  glData.getGlDetails().get(1).getAccount();
			} else {
			contactLedger = chartOfAccountsDao.getDefaultLedger(ChartOfAccountsConstants.ENTITY_CUSTOMER, null, null);
			contactAccountId = chartOfAccountsDao.getLedgerIdGivenName(contactLedger, paymentsVo.getOrganizationId());
			}
		}
		if(PaymentNonCoreConstants.VENDOR.equals(paymentsVo.getContactType())) {
			gl.setType(PaymentNonCoreConstants.VENDOR);
			gl.setDescription(JournalEntriesConstants.SUBLEDGER_TYPE_VENDOR);
			gl.setSubLedger(paymentsVo.getContactName());
			gl.setLedgerList(chartOfAccountsDao.getLedgersByEntity(paymentsVo.getOrganizationId(), ChartOfAccountsConstants.ENTITY_VENDOR));
			if(glData != null && glData.getGlDetails() != null && glData.getGlDetails().size() > 0	&& glData.getGlDetails().get(1).getType() != null
					&& PaymentNonCoreConstants.VENDOR.equals(glData.getGlDetails().get(1).getType())) {
				contactAccountId = glData.getGlDetails().get(1).getAccountId();
				String name = chartOfAccountsDao.getLedgerName(contactAccountId , paymentsVo.getOrganizationId()) ;
				contactLedger  = name!=null ? name :  glData.getGlDetails().get(1).getAccount();
			}else {
			contactLedger =  chartOfAccountsDao.getDefaultLedger(ChartOfAccountsConstants.ENTITY_VENDOR, null, null);
			contactAccountId = chartOfAccountsDao.getLedgerIdGivenName(contactLedger, paymentsVo.getOrganizationId());
			}
		}
		if(PaymentNonCoreConstants.EMPLOYEE.equals(paymentsVo.getContactType())) {
			gl.setType(PaymentNonCoreConstants.EMPLOYEE);
			gl.setDescription(JournalEntriesConstants.SUBLEDGER_TYPE_EMPLOYEE);
			gl.setLedgerList(chartOfAccountsDao.getLedgersByEntity(paymentsVo.getOrganizationId(), ChartOfAccountsConstants.ENTITY_EMPLOYEE_PAYABLE));
			if(glData != null && glData.getGlDetails() != null && glData.getGlDetails().size() > 0	&& glData.getGlDetails().get(1).getType() != null
					&& PaymentNonCoreConstants.EMPLOYEE.equals(glData.getGlDetails().get(1).getType())) {
				contactAccountId = glData.getGlDetails().get(1).getAccountId();
				String name = chartOfAccountsDao.getLedgerName(contactAccountId , paymentsVo.getOrganizationId()) ;
				contactLedger  = name!=null ? name :  glData.getGlDetails().get(1).getAccount();
			}else {
			contactLedger = chartOfAccountsDao.getDefaultLedger(ChartOfAccountsConstants.ENTITY_EMPLOYEE_PAYABLE, null, null);
			contactAccountId = chartOfAccountsDao.getLedgerIdGivenName(contactLedger, paymentsVo.getOrganizationId());
			}
			gl.setSubLedger(paymentsVo.getContactName());

		}
				gl.setProductId(paymentsVo.getContactId());
				gl.setTempId(Integer.valueOf( paymentsVo.getContactId()));
				gl.setAccount(contactLedger);
				gl.setAccountId(contactAccountId);
				Double amntPaid = Double.valueOf(paymentsVo.getAmountPaid());
				gl.setFcyDebit(Double.valueOf(df.format( amntPaid)));
				Double xchangeRate = paymentsVo.getExchangeRate() !=null && !"".equals(paymentsVo.getExchangeRate()) ? Double.valueOf(paymentsVo.getExchangeRate())  : 1.00;
				gl.setInrDebit(Double.valueOf(df.format(xchangeRate * amntPaid)));
				generalLedgers.add(gl);
			
		}
	}catch(Exception e) {
		logger.info("Error in getPaymentsAndGroupByLedgers:: ",e);
		e.printStackTrace();
		throw new ApplicationException(e);
	}
return generalLedgers;
	}









private List<GeneralLedgerDetailsVo> getGLForCustomerRefundsPaymentType(PaymentNonCoreVo paymentsVo,	String accountType) throws ApplicationException {
	List<GeneralLedgerDetailsVo> generalLedgers= new ArrayList<>();
	DecimalFormat df = new DecimalFormat("0.00");
try { 
	GeneralLedgerVo glData = paymentsVo!=null && paymentsVo.getGeneralLedgerData()!=null ? paymentsVo.getGeneralLedgerData() : null;
	Map<Integer , Integer > customerMap = glData!=null ? paymentsVo.getGeneralLedgerData().glDetails.stream().filter(
			data ->JournalEntriesConstants.SUBLEDGER_TYPE_CUSTOMER.equals(data.getType()) ).collect(Collectors.toMap(GeneralLedgerDetailsVo::getTempId, GeneralLedgerDetailsVo::getAccountId)) : null;
	//To get the Bank related GL details 
	if(paymentsVo.getPaidVia()!=0 ) {
		GeneralLedgerDetailsVo gl = new GeneralLedgerDetailsVo();
		String bankLedger =  paymentsVo.getPaidViaName();
		Integer bankLedgerId = chartOfAccountsDao.getLedgerIdGivenName(bankLedger, paymentsVo.getOrganizationId());
		gl.setType(JournalEntriesConstants.SUBLEDGER_TYPE_BANKING);
		gl.setDescription(accountType);
		gl.setTempId(paymentsVo.getPaidVia());
		/*if(glData!=null && glData.getGlDetails()!=null && !glData.getGlDetails().isEmpty() &&  glData.getGlDetails().get(0) !=null && JournalEntriesConstants.SUBLEDGER_TYPE_BANKING.equals(glData.getGlDetails().get(0).getType()) && glData.getGlDetails().get(0).getTempId() == paymentsVo.getPaidVia()) {
			gl.setAccount( glData.getGlDetails().get(0).getAccount());
			gl.setAccountId( glData.getGlDetails().get(0).getAccountId());
			gl.setLedgerList(chartOfAccountsDao.getLedgersAndItsSiblingsByLedgerName(paymentsVo.getOrganizationId(), glData.getGlDetails().get(0).getAccount()));
			gl.setSubLedger(glData.getGlDetails().get(0).getSubLedger());
		}else {*/
			gl.setAccount(bankLedger);
			gl.setAccountId(bankLedgerId);
			gl.setLedgerList(chartOfAccountsDao.getLedgerById(paymentsVo.getOrganizationId(), bankLedgerId));
			gl.setSubLedger(accountType);
	//	}
		Double amntPaid = Double.valueOf(paymentsVo.getAmountPaid());
		gl.setFcyCredit(Double.valueOf(amntPaid));
		Double xchangeRate = paymentsVo.getExchangeRate() !=null && !"".equals(paymentsVo.getExchangeRate()) ? Double.valueOf(paymentsVo.getExchangeRate())  : 1.00;
		gl.setInrCredit(Double.valueOf(df.format(xchangeRate * amntPaid)));
		generalLedgers.add(gl);
		}
	
	
	// to get the GL detail for customer 
	if(paymentsVo.getPaidTo()!=null && !paymentsVo.getPaidTo().isEmpty()  && !"0".equals(paymentsVo.getPaidTo())) {
			String customerLedger =chartOfAccountsDao.getDefaultLedger(ChartOfAccountsConstants.ENTITY_CUSTOMER, null, null);
			 Integer customerAccountId  = paymentsVo.getContactAccountId()!=0 ? paymentsVo.getContactAccountId() : chartOfAccountsDao.getLedgerIdGivenName(customerLedger, paymentsVo.getOrganizationId());
		if(paymentsVo.getPayments()!=null && paymentsVo.getPayments().size()>0 ){
			int counter = 0;
			 for(PaymentNonCoreBaseVo bills : paymentsVo.getPayments()) {
				 counter++;
					if( bills.getReferenceId()!=0  && bills.getInvoiceAmount()!=null &&  !bills.getInvoiceAmount().isEmpty()) {
						GeneralLedgerDetailsVo gl = new GeneralLedgerDetailsVo();
						gl.setType(JournalEntriesConstants.SUBLEDGER_TYPE_CUSTOMER);
						gl.setDescription(bills.getReferenceType());
						gl.setTempId(Integer.valueOf(counter));
						gl.setProductId(Integer.valueOf(paymentsVo.getPaidTo()));
						if(customerMap!=null && customerMap.size()>0 ) {
							Integer id = customerMap.containsKey(counter) ? customerMap.get(counter) : customerAccountId;
							String name = chartOfAccountsDao.getLedgerName(id , paymentsVo.getOrganizationId()) ;
							gl.setAccount(name);
							gl.setAccountId(id);
						}else {
							gl.setAccount(customerLedger);
							gl.setAccountId(customerAccountId);
						}
						gl.setLedgerList(chartOfAccountsDao.getLedgersByEntity(paymentsVo.getOrganizationId(), ChartOfAccountsConstants.ENTITY_CUSTOMER));
						gl.setSubLedger(bills.getInvRefName());
						Double amntPaid = Double.valueOf(bills.getInvoiceAmount());
						gl.setFcyDebit(Double.valueOf(df.format( amntPaid)));
						Double xchangeRate = paymentsVo.getExchangeRate() !=null && !"".equals(paymentsVo.getExchangeRate()) ? Double.valueOf(paymentsVo.getExchangeRate())  : 1.00;
						gl.setInrDebit(Double.valueOf(df.format(xchangeRate * amntPaid)));
						generalLedgers.add(gl);
						
					}
				}
			}
		if(!paymentsVo.isBulk()) {
			GeneralLedgerDetailsVo gl = new GeneralLedgerDetailsVo();
			gl.setType(JournalEntriesConstants.SUBLEDGER_TYPE_CUSTOMER);
			gl.setDescription(JournalEntriesConstants.SUBLEDGER_TYPE_CUSTOMER);
			gl.setTempId(Integer.valueOf( paymentsVo.getPaidTo()));
			gl.setProductId(Integer.valueOf(paymentsVo.getPaidTo()));
			gl.setAccount(customerLedger);
			gl.setAccountId(customerAccountId);
			gl.setLedgerList(chartOfAccountsDao.getLedgersByEntity(paymentsVo.getOrganizationId(), ChartOfAccountsConstants.ENTITY_CUSTOMER));
			gl.setSubLedger(customerDao.getCustomerName(Integer.valueOf(paymentsVo.getPaidTo())));
			Double amntPaid = Double.valueOf(paymentsVo.getAmountPaid());
			gl.setFcyDebit(Double.valueOf(df.format( amntPaid)));
			Double xchangeRate = paymentsVo.getExchangeRate() !=null && !"".equals(paymentsVo.getExchangeRate()) ? Double.valueOf(paymentsVo.getExchangeRate())  : 1.00;
			gl.setInrDebit(Double.valueOf(df.format(xchangeRate * amntPaid)));
			generalLedgers.add(gl);
		}
		}
	}catch(Exception e) {
		logger.info("Error in getPaymentsAndGroupByLedgers:: ",e);
		e.printStackTrace();
		throw new ApplicationException(e);
	}
return generalLedgers;

	}









private List<GeneralLedgerDetailsVo> getGLForTDSPaymentType(PaymentNonCoreVo paymentsVo, String accountType) throws ApplicationException {

	List<GeneralLedgerDetailsVo> generalLedgers= new ArrayList<>();
	DecimalFormat df = new DecimalFormat("0.00");
try { 
	GeneralLedgerVo glData = paymentsVo!=null && paymentsVo.getGeneralLedgerData()!=null ? paymentsVo.getGeneralLedgerData() : null;
	Map<Integer , String > taxMap = new HashMap<Integer, String>();
	Map<Integer , String > interestMap = new HashMap<Integer, String>();
	Map<Integer , String > penaltyMap = new HashMap<Integer, String>();
	Map<Integer , String > others1Map = new HashMap<Integer, String>();
	Map<Integer , String > others2Map = new HashMap<Integer, String>();
	if(glData!=null && glData.getGlDetails()!=null && glData.getGlDetails().size()>0 ) {
		logger.info("glvo"+glData);
	for(GeneralLedgerDetailsVo details :  paymentsVo.getGeneralLedgerData().getGlDetails()){
		logger.info("glvo Details"+details);
		if(details.getType()!=null && details.getType().equals(JournalEntriesConstants.TAX)) {
			taxMap.put(details.getTempId(),details.getAccountId() +"~"+ details.getAccount());
		}
		if(details.getType()!=null && details.getType().equals(JournalEntriesConstants.INTEREST)) {
			interestMap.put(details.getTempId(), details.getAccountId() +"~"+ details.getAccount());
		}
		if(details.getType()!=null && details.getType().equals(JournalEntriesConstants.PENALTY)) {
			penaltyMap.put(details.getTempId() , details.getAccountId() +"~"+ details.getAccount());
		}
		if(details.getType()!=null && details.getType().equals(JournalEntriesConstants.OTHERS1)) {
			others1Map.put(details.getTempId() , details.getAccountId() +"~"+ details.getAccount());
		}
		if(details.getType()!=null && details.getType().equals(JournalEntriesConstants.OTHERS2)) {
			others2Map.put(details.getTempId() ,details.getAccountId() +"~"+ details.getAccount());
		}
	}
	}
	logger.info("taxMap::>> +"+taxMap);
	logger.info("interestMap::>> +"+interestMap);
	logger.info("penaltyMap::>> +"+penaltyMap);
	logger.info("others1Map::>> +"+others1Map);
	logger.info("others2Map::>> +"+others2Map);

	//To get the Bank related GL details 
	if(paymentsVo.getPaidVia()!=0 ) {
		GeneralLedgerDetailsVo gl = new GeneralLedgerDetailsVo();
		String bankLedger = paymentsVo.getPaidViaName();
		Integer bankLedgerId = chartOfAccountsDao.getLedgerIdGivenName(bankLedger, paymentsVo.getOrganizationId());
		gl.setType(JournalEntriesConstants.SUBLEDGER_TYPE_BANKING);
		gl.setDescription(accountType);
		gl.setTempId(paymentsVo.getPaidVia());
		if(glData!=null && glData.getGlDetails()!=null && !glData.getGlDetails().isEmpty() &&  glData.getGlDetails().get(0) !=null && JournalEntriesConstants.SUBLEDGER_TYPE_BANKING.equals(glData.getGlDetails().get(0).getType()) && glData.getGlDetails().get(0).getTempId() == paymentsVo.getPaidVia()) {
			gl.setAccount( glData.getGlDetails().get(0).getAccount());
			gl.setAccountId( glData.getGlDetails().get(0).getAccountId());
			gl.setSubLedger(glData.getGlDetails().get(0).getSubLedger());
		}else {
			gl.setAccount(bankLedger);
			gl.setAccountId(bankLedgerId);
			gl.setSubLedger(accountType);
		}
		gl.setLedgerList(chartOfAccountsDao.getLedgerById(  paymentsVo.getOrganizationId() , bankLedgerId));
		Double amntPaid = Double.valueOf(paymentsVo.getAmountPaid());
		gl.setFcyCredit(Double.valueOf(amntPaid));
		Double xchangeRate = paymentsVo.getExchangeRate() !=null && !"".equals(paymentsVo.getExchangeRate()) ? Double.valueOf(paymentsVo.getExchangeRate())  : 1.00;
		gl.setInrCredit(Double.valueOf(df.format(xchangeRate * amntPaid)));
		generalLedgers.add(gl);
		}
	//To get the TDS related GL details 
	if(paymentsVo.getPayments()!=null && paymentsVo.getPayments().size()>0 ) {
		Connection  con = getFinanceCommon();
		Map<Integer, String> tdsTypeMap = financeCommonDao.getTDS(con).stream().collect(Collectors.toMap(TDSVo::getId, TDSVo::getTdsRateIdentifier));
		closeResources(null, null, con);
		List<MinimalChartOfAccountsVo> allLedgers = chartOfAccountsDao.getAllLedgersByOrgId(paymentsVo.getOrganizationId());
		int counter = 0;
		for(PaymentNonCoreBaseVo vo : paymentsVo.getPayments()) {
			counter++;
			logger.info("counter ::"+ counter);
			if(vo.getTdsSection()!=null && !vo.getTdsSection().isEmpty() && !vo.getTdsSection().equals("0") && tdsTypeMap!=null && tdsTypeMap.size()>0) {
				// To prepare ledger for tax column 
				if(vo.getTax()!=null && !vo.getTax().isEmpty() && !vo.getTax().equals("0")) {
					GeneralLedgerDetailsVo gl = new GeneralLedgerDetailsVo();
					gl.setTempId(counter);
					gl.setType(JournalEntriesConstants.TAX);
					gl.setDescription(JournalEntriesConstants.TAX);
					gl.setProductId(Integer.valueOf(vo.getTdsSection()));
					if(taxMap!=null && taxMap.size()>0 && taxMap.containsKey(counter)) {
						String ledger = taxMap.get(counter);
						String  ledgerId =  ledger.split("~").length>0 && ledger.split("~")[0]!=null ? ledger.split("~")[0] : "0" ;
						String  ledgerName =  ledger.split("~").length>0 && ledger.split("~")[1]!=null ? ledger.split("~")[1] : JournalEntriesConstants.TDS_PAYABLE ;
						String name = chartOfAccountsDao.getLedgerName(Integer.valueOf(ledgerId), paymentsVo.getOrganizationId()) ;
						gl.setAccountId(Integer.valueOf(ledgerId));
						gl.setAccount(name!=null ? name : ledgerName);
						gl.setLedgerList(chartOfAccountsDao.getLedgersByEntity( paymentsVo.getOrganizationId(), ChartOfAccountsConstants.ENTITY_TDS_PAYABLE));
					}else {
						String ledger = chartOfAccountsDao.getDefaultLedger(ChartOfAccountsConstants.ENTITY_TDS_PAYABLE, null, ChartOfAccountsConstants.FIELD_GOVERNMENT_BODIES_TDS);
						Integer ledgerId = chartOfAccountsDao.getLedgerIdGivenName(ledger, paymentsVo.getOrganizationId());
						gl.setAccount(ledger);
						gl.setAccountId(ledgerId);
						gl.setLedgerList(chartOfAccountsDao.getLedgersByEntity( paymentsVo.getOrganizationId(), ChartOfAccountsConstants.ENTITY_TDS_PAYABLE));

					}
					gl.setSubLedger(tdsTypeMap.get(Integer.valueOf(vo.getTdsSection())));
					gl.setLedgerList(chartOfAccountsDao.getLedgersByEntity( paymentsVo.getOrganizationId(), ChartOfAccountsConstants.ENTITY_TDS_PAYABLE));
					Double amntPaid = Double.valueOf(vo.getTax());
					Double xchangeRate = paymentsVo.getExchangeRate() !=null && !"".equals(paymentsVo.getExchangeRate()) ? Double.valueOf(paymentsVo.getExchangeRate())  : 1.00;
					if(amntPaid > 0 ) {
						gl.setFcyDebit(Double.valueOf(df.format( amntPaid)));
						gl.setInrDebit(Double.valueOf(df.format(xchangeRate * amntPaid)));
					}else {
						gl.setFcyCredit(Double.valueOf(df.format( amntPaid  * -1)));
						gl.setInrCredit(Double.valueOf(df.format(xchangeRate * amntPaid * -1)));
					}
					
					generalLedgers.add(gl);
				}
					
				if(vo.getInterest()!=null && !vo.getInterest().isEmpty() && !vo.getInterest().equals("0")) {

					GeneralLedgerDetailsVo gl = new GeneralLedgerDetailsVo();
					gl.setTempId( counter);
					gl.setType(JournalEntriesConstants.INTEREST);
					gl.setDescription(JournalEntriesConstants.INTEREST);
					gl.setProductId(Integer.valueOf(vo.getTdsSection()));
					if(interestMap!=null && interestMap.size()>0 && interestMap.containsKey(counter)) {
						String ledger = interestMap.get(counter);
						String  ledgerId =  ledger.split("~").length>0 && ledger.split("~")[0]!=null ? ledger.split("~")[0] : "0" ;
						String  ledgerName =  ledger.split("~").length>0 && ledger.split("~")[1]!=null ? ledger.split("~")[1] : PaymentNonCoreConstants.DEFAULT_TDS_INTEREST_LEDGER ;
						String name = chartOfAccountsDao.getLedgerName(Integer.valueOf(ledgerId), paymentsVo.getOrganizationId()) ;
						gl.setAccountId(Integer.valueOf(ledgerId));
						gl.setAccount(name !=null ? name : ledgerName);
						gl.setLedgerList(chartOfAccountsDao.getLedgersByEntity( paymentsVo.getOrganizationId(), ChartOfAccountsConstants.ENTITY_INDIRECT_EXPENSES));
					}else {
						String ledger = chartOfAccountsDao.getDefaultLedger(ChartOfAccountsConstants.ENTITY_INDIRECT_EXPENSES, null, ChartOfAccountsConstants.FIELD_INTEREST_ON_TDS);
						Integer ledgerId = chartOfAccountsDao.getLedgerIdGivenName(ledger, paymentsVo.getOrganizationId());
						gl.setAccount(ledger);
						gl.setAccountId(ledgerId);
						gl.setLedgerList(chartOfAccountsDao.getLedgersByEntity( paymentsVo.getOrganizationId(), ChartOfAccountsConstants.ENTITY_INDIRECT_EXPENSES));
						
					}
					gl.setSubLedger(tdsTypeMap.get(Integer.valueOf(vo.getTdsSection())));
					Double amntPaid = Double.valueOf(vo.getInterest());
					Double xchangeRate = paymentsVo.getExchangeRate() !=null && !"".equals(paymentsVo.getExchangeRate()) ? Double.valueOf(paymentsVo.getExchangeRate())  : 1.00;
					if(amntPaid > 0 ) {
						gl.setFcyDebit(Double.valueOf(df.format( amntPaid)));
						gl.setInrDebit(Double.valueOf(df.format(xchangeRate * amntPaid)));
					}else {
						gl.setFcyCredit(Double.valueOf(df.format( amntPaid * -1)));
						gl.setInrCredit(Double.valueOf(df.format(xchangeRate * amntPaid * -1)));
					}
					
					generalLedgers.add(gl);
							
				}
				
				
				if(vo.getPenalty()!=null && !vo.getPenalty().isEmpty() && !vo.getPenalty().equals("0")) {
					GeneralLedgerDetailsVo gl = new GeneralLedgerDetailsVo();
					gl.setTempId( counter);
					gl.setType(JournalEntriesConstants.PENALTY);
					gl.setDescription(JournalEntriesConstants.PENALTY);
					gl.setProductId(Integer.valueOf(vo.getTdsSection()));
					if(penaltyMap!=null && penaltyMap.size()>0 && penaltyMap.containsKey(counter)) {
						String ledger = penaltyMap.get(counter);
						String  ledgerId =  ledger.split("~").length>0 && ledger.split("~")[0]!=null ? ledger.split("~")[0] : "0" ;
						String  ledgerName =  ledger.split("~").length>0 && ledger.split("~")[1]!=null ? ledger.split("~")[1] : PaymentNonCoreConstants.DEFAULT_PENALTY_LEDGER ;
						String name = chartOfAccountsDao.getLedgerName(Integer.valueOf(ledgerId), paymentsVo.getOrganizationId()) ;
						gl.setAccountId(Integer.valueOf(ledgerId));
						gl.setAccount(name !=null ? name : ledgerName);
						gl.setLedgerList(chartOfAccountsDao.getLedgersByEntity( paymentsVo.getOrganizationId(), ChartOfAccountsConstants.ENTITY_INDIRECT_EXPENSES));
					}else {
						String ledger = chartOfAccountsDao.getDefaultLedger(ChartOfAccountsConstants.ENTITY_INDIRECT_EXPENSES, null, ChartOfAccountsConstants.FIELD_PENALTIES_ON_TDS);
						Integer ledgerId = chartOfAccountsDao.getLedgerIdGivenName(ledger, paymentsVo.getOrganizationId());
						gl.setAccount(ledger);
						gl.setAccountId(ledgerId);
						gl.setLedgerList(chartOfAccountsDao.getLedgersByEntity( paymentsVo.getOrganizationId(), ChartOfAccountsConstants.ENTITY_INDIRECT_EXPENSES));
					}
					
					gl.setSubLedger(tdsTypeMap.get(Integer.valueOf(vo.getTdsSection())));
					Double amntPaid = Double.valueOf(vo.getPenalty());
					Double xchangeRate = paymentsVo.getExchangeRate() !=null && !"".equals(paymentsVo.getExchangeRate()) ? Double.valueOf(paymentsVo.getExchangeRate())  : 1.00;
					if(amntPaid > 0 ) {
						gl.setFcyDebit(Double.valueOf(df.format( amntPaid)));
						gl.setInrDebit(Double.valueOf(df.format(xchangeRate * amntPaid)));
					}else {
						gl.setFcyCredit(Double.valueOf(df.format( amntPaid * -1)));
						gl.setInrCredit(Double.valueOf(df.format(xchangeRate * amntPaid * -1)));
					}
					
					generalLedgers.add(gl);
				}
							
				if(vo.getOthers1()!=null && !vo.getOthers1().isEmpty() && !vo.getOthers1().equals("0")) {
					GeneralLedgerDetailsVo gl = new GeneralLedgerDetailsVo();
					gl.setTempId( counter);
					gl.setType(JournalEntriesConstants.OTHERS1);
					gl.setProductId(Integer.valueOf(vo.getTdsSection()));
					PaymentNonCoreCustomTableVo name = paymentsVo.getCustomTableList().stream().filter(table -> JournalEntriesConstants.OTHERS1.equals(table.getcName())).findFirst().orElse(null);
					gl.setDescription(name!=null ? name.getColName() : null);
					if(others1Map!=null && others1Map.size()>0 && others1Map.containsKey(counter)) {
						String ledger = others1Map.get(counter);
						String  ledgerId =  ledger.split("~").length>0 && ledger.split("~")[0]!=null ? ledger.split("~")[0] : "0" ;
						String  ledgerName =  ledger.split("~").length>0 && ledger.split("~")[1]!=null ? ledger.split("~")[1] : null ;
						String namee = chartOfAccountsDao.getLedgerName(Integer.valueOf(ledgerId), paymentsVo.getOrganizationId()) ;
						gl.setAccountId(Integer.valueOf(ledgerId));
						gl.setAccount(namee!=null ? namee : ledgerName);
					//	gl.setLedgerList(chartOfAccountsDao.getLedgersAndItsSiblingsByLedgerName(paymentsVo.getOrganizationId(),ledgerName));
					}
					gl.setLedgerList(allLedgers);
					gl.setSubLedger(tdsTypeMap.get(Integer.valueOf(vo.getTdsSection())));
					Double amntPaid = Double.valueOf(vo.getOthers1());
					Double xchangeRate = paymentsVo.getExchangeRate() !=null && !"".equals(paymentsVo.getExchangeRate()) ? Double.valueOf(paymentsVo.getExchangeRate())  : 1.00;
					if(amntPaid > 0 ) {
						gl.setFcyDebit(Double.valueOf(df.format( amntPaid)));
						gl.setInrDebit(Double.valueOf(df.format(xchangeRate * amntPaid)));
					}else {
						gl.setFcyCredit(Double.valueOf(df.format( amntPaid * -1)));
						gl.setInrCredit(Double.valueOf(df.format(xchangeRate * amntPaid * -1)));
					}
					
					generalLedgers.add(gl);	
				}
				
				if(vo.getOthers2()!=null && !vo.getOthers2().isEmpty() && !vo.getOthers2().equals("0")) {
					GeneralLedgerDetailsVo gl = new GeneralLedgerDetailsVo();
					gl.setTempId(counter);
					gl.setType(JournalEntriesConstants.OTHERS2);
					gl.setProductId(Integer.valueOf(vo.getTdsSection()));
					PaymentNonCoreCustomTableVo name = paymentsVo.getCustomTableList().stream().filter(table -> JournalEntriesConstants.OTHERS2.equals(table.getcName())).findFirst().orElse(null);
					gl.setDescription(name!=null ? name.getColName() : null);
					if(others2Map!=null && others2Map.size()>0 && others2Map.containsKey(counter)) {
						String ledger = others2Map.get(counter);
						String  ledgerId =  ledger.split("~").length>0 && ledger.split("~")[0]!=null ? ledger.split("~")[0] : "0" ;
						String  ledgerName =  ledger.split("~").length>0 && ledger.split("~")[1]!=null ? ledger.split("~")[1] : null;
						String lname = chartOfAccountsDao.getLedgerName(Integer.valueOf(ledgerId), paymentsVo.getOrganizationId()) ;
						gl.setAccountId(Integer.valueOf(ledgerId));
						gl.setAccount(lname!=null ? lname : ledgerName);
					}
					gl.setLedgerList(allLedgers);
					gl.setSubLedger(tdsTypeMap.get(Integer.valueOf(vo.getTdsSection())));
					Double amntPaid = Double.valueOf(vo.getOthers2());
					Double xchangeRate = paymentsVo.getExchangeRate() !=null && !"".equals(paymentsVo.getExchangeRate()) ? Double.valueOf(paymentsVo.getExchangeRate())  : 1.00;
					if(amntPaid > 0 ) {
						gl.setFcyDebit(Double.valueOf(df.format( amntPaid)));
						gl.setInrDebit(Double.valueOf(df.format(xchangeRate * amntPaid)));
					}else {
						gl.setFcyCredit(Double.valueOf(df.format( amntPaid * -1)));
						gl.setInrCredit(Double.valueOf(df.format(xchangeRate * amntPaid * -1)));
					}
				
					generalLedgers.add(gl);	
				}
			}
		}
		
		
		
		}
	}catch(Exception e) {
		logger.info("Error in getPaymentsAndGroupByLedgers:: ",e);
		e.printStackTrace();
		throw new ApplicationException(e);
	}
return generalLedgers;
	
	}









private List<GeneralLedgerDetailsVo> getGLForGSTPaymentType(PaymentNonCoreVo paymentsVo, String accountType) throws ApplicationException {
	List<GeneralLedgerDetailsVo> generalLedgers= new ArrayList<>();
	DecimalFormat df = new DecimalFormat("0.00");
try { 
	GeneralLedgerVo glData = paymentsVo!=null && paymentsVo.getGeneralLedgerData()!=null ? paymentsVo.getGeneralLedgerData() : null;
	Map<Integer , String > taxMap = new HashMap<Integer, String>();
	Map<Integer , String > interestMap = new HashMap<Integer, String>();
	Map<Integer , String > penaltyMap = new HashMap<Integer, String>();
	Map<Integer , String > others1Map = new HashMap<Integer, String>();
	Map<Integer , String > others2Map = new HashMap<Integer, String>();
	if(glData!=null && glData.getGlDetails()!=null && glData.getGlDetails().size()>0 ) {
		logger.info("glvo"+glData);
	for(GeneralLedgerDetailsVo details :  paymentsVo.getGeneralLedgerData().getGlDetails()){
		logger.info("glvo Details"+details);
		if(details.getType()!=null && details.getType().equals(JournalEntriesConstants.TAX)) {
			taxMap.put(details.getTempId(),details.getAccountId() +"~"+ details.getAccount());
		}
		if(details.getType()!=null && details.getType().equals(JournalEntriesConstants.INTEREST)) {
			interestMap.put(details.getTempId(), details.getAccountId() +"~"+ details.getAccount());
		}
		if(details.getType()!=null && details.getType().equals(JournalEntriesConstants.PENALTY)) {
			penaltyMap.put(details.getTempId() , details.getAccountId() +"~"+ details.getAccount());
		}
		if(details.getType()!=null && details.getType().equals(JournalEntriesConstants.OTHERS1)) {
			others1Map.put(details.getTempId() , details.getAccountId() +"~"+ details.getAccount());
		}
		if(details.getType()!=null && details.getType().equals(JournalEntriesConstants.OTHERS2)) {
			others2Map.put(details.getTempId() ,details.getAccountId() +"~"+ details.getAccount());
		}
	}
	}
	logger.info("taxMap::>> +"+taxMap);
	logger.info("interestMap::>> +"+interestMap);
	logger.info("penaltyMap::>> +"+penaltyMap);
	logger.info("others1Map::>> +"+others1Map);
	logger.info("others2Map::>> +"+others2Map);

	//To get the Bank related GL details 
	if(paymentsVo.getPaidVia()!=0 ) {
		GeneralLedgerDetailsVo gl = new GeneralLedgerDetailsVo();
		String bankLedger =  paymentsVo.getPaidViaName();
		Integer bankLedgerId = chartOfAccountsDao.getLedgerIdGivenName(bankLedger, paymentsVo.getOrganizationId());
		gl.setType(JournalEntriesConstants.SUBLEDGER_TYPE_BANKING);
		gl.setDescription(accountType);
		gl.setTempId(paymentsVo.getPaidVia());
		/*if(glData!=null && glData.getGlDetails()!=null && !glData.getGlDetails().isEmpty() &&  glData.getGlDetails().get(0) !=null && JournalEntriesConstants.SUBLEDGER_TYPE_BANKING.equals(glData.getGlDetails().get(0).getType()) && glData.getGlDetails().get(0).getTempId() == paymentsVo.getPaidVia()) {
			gl.setAccount( glData.getGlDetails().get(0).getAccount());
			gl.setAccountId( glData.getGlDetails().get(0).getAccountId());
			gl.setLedgerList(chartOfAccountsDao.getLedgersAndItsSiblingsByLedgerName(paymentsVo.getOrganizationId(), glData.getGlDetails().get(0).getAccount()));
			gl.setSubLedger(glData.getGlDetails().get(0).getSubLedger());
		}else {*/
			gl.setAccount(bankLedger);
			gl.setAccountId(bankLedgerId);
			gl.setLedgerList(chartOfAccountsDao.getLedgerById(paymentsVo.getOrganizationId(), bankLedgerId));
			gl.setSubLedger(paymentsVo.getPaidViaName());
		//}
		Double amntPaid = Double.valueOf(paymentsVo.getAmountPaid());
		gl.setFcyCredit(Double.valueOf(amntPaid));
		Double xchangeRate = paymentsVo.getExchangeRate() !=null && !"".equals(paymentsVo.getExchangeRate()) ? Double.valueOf(paymentsVo.getExchangeRate())  : 1.00;
		gl.setInrCredit(Double.valueOf(df.format(xchangeRate * amntPaid)));
		generalLedgers.add(gl);
		}
	//To get the GST related GL details 
	if(paymentsVo.getPayments()!=null && paymentsVo.getPayments().size()>0 ) {
		Map<Integer, String> taxTypeMap = dropDownDao.getTaxRateDropDown(paymentsVo.getOrganizationId()).stream().collect(Collectors.toMap(TaxRateTypeVo::getId, TaxRateTypeVo::getType));
		int counter = 0;
		List<MinimalChartOfAccountsVo> allLedgers = chartOfAccountsDao.getAllLedgersByOrgId(paymentsVo.getOrganizationId());
		for(PaymentNonCoreBaseVo vo : paymentsVo.getPayments()) {
			counter++;
			logger.info("counter ::"+ counter);
			if(vo.getGstType()!=null && !vo.getGstType().isEmpty() && !vo.getGstType().equals("0") &&taxTypeMap!=null && taxTypeMap.size()>0) {
				// To prepare ledger for tax column 
				if(vo.getTax()!=null && !vo.getTax().isEmpty() && !vo.getTax().equals("0")) {
					GeneralLedgerDetailsVo gl = new GeneralLedgerDetailsVo();
					gl.setTempId(counter);
					gl.setType(JournalEntriesConstants.TAX);
					gl.setDescription(JournalEntriesConstants.SUBLEDGER_TYPE_GST);
					
					if(taxMap!=null && taxMap.size()>0 && taxMap.containsKey(counter)) {
						String ledger = taxMap.get(counter);
						String  ledgerId =  ledger.split("~").length>0 && ledger.split("~")[0]!=null ? ledger.split("~")[0] : "0" ;
						String  ledgerName =  ledger.split("~").length>0 && ledger.split("~")[1]!=null ? ledger.split("~")[1] : PaymentNonCoreConstants.DEFAULT_GST_TAX_LEDGER ;
						String name = chartOfAccountsDao.getLedgerName(Integer.valueOf(ledgerId), paymentsVo.getOrganizationId()) ;

						gl.setAccountId(Integer.valueOf(ledgerId));
						gl.setAccount(name!=null ? name : ledgerName);
						gl.setLedgerList(chartOfAccountsDao.getLedgersByEntity(paymentsVo.getOrganizationId(), ChartOfAccountsConstants.ENTITY_GST_RECEIVABLE_PAYABLE));
						gl.setSubLedger(taxTypeMap.get(Integer.valueOf(vo.getGstType())));

					}else {
						String ledger = chartOfAccountsDao.getDefaultLedger(ChartOfAccountsConstants.ENTITY_GST_RECEIVABLE_PAYABLE, ChartOfAccountsConstants.ENTITY_PAYMENT, null);
						Integer ledgerId = chartOfAccountsDao.getLedgerIdGivenName(ledger, paymentsVo.getOrganizationId());
						gl.setAccount(ledger);
						gl.setAccountId(ledgerId);
						gl.setLedgerList(chartOfAccountsDao.getLedgersByEntity(paymentsVo.getOrganizationId(), ChartOfAccountsConstants.ENTITY_GST_RECEIVABLE_PAYABLE));
						gl.setSubLedger(taxTypeMap.get(Integer.valueOf(vo.getGstType())));
					}
					
					Double amntPaid = Double.valueOf(vo.getTax());
					Double xchangeRate = paymentsVo.getExchangeRate() !=null && !"".equals(paymentsVo.getExchangeRate()) ? Double.valueOf(paymentsVo.getExchangeRate())  : 1.00;
					if(amntPaid > 0) {
						gl.setFcyDebit(Double.valueOf(df.format( amntPaid)));
						gl.setInrDebit(Double.valueOf(df.format(xchangeRate * amntPaid)));
					}else {
						gl.setFcyCredit(Double.valueOf(df.format( amntPaid * -1)));
						gl.setInrCredit(Double.valueOf(df.format(xchangeRate * amntPaid * -1)));
					}
					
					generalLedgers.add(gl);
				}
					
				if(vo.getInterest()!=null && !vo.getInterest().isEmpty() && !vo.getInterest().equals("0")) {

					GeneralLedgerDetailsVo gl = new GeneralLedgerDetailsVo();
					gl.setTempId( counter);
					gl.setType(JournalEntriesConstants.INTEREST);
					gl.setDescription(JournalEntriesConstants.INTEREST_DESCRIPTION);
					if(interestMap!=null && interestMap.size()>0 && interestMap.containsKey(counter)) {
						String ledger = interestMap.get(counter);
						String  ledgerId =  ledger.split("~").length>0 && ledger.split("~")[0]!=null ? ledger.split("~")[0] : "0" ;
						String  ledgerName =  ledger.split("~").length>0 && ledger.split("~")[1]!=null ? ledger.split("~")[1] : PaymentNonCoreConstants.DEFAULT_GST_INTEREST_LEDGER ;
						String name = chartOfAccountsDao.getLedgerName(Integer.valueOf(ledgerId), paymentsVo.getOrganizationId()) ;
						gl.setAccountId(Integer.valueOf(ledgerId));
						gl.setAccount(name!=null ? name : ledgerName);
						gl.setLedgerList(chartOfAccountsDao.getLedgersByEntity( paymentsVo.getOrganizationId(), ChartOfAccountsConstants.ENTITY_INDIRECT_EXPENSES));
					}else {
						String ledger = chartOfAccountsDao.getDefaultLedger(ChartOfAccountsConstants.ENTITY_INDIRECT_EXPENSES, null, ChartOfAccountsConstants.FIELD_INTEREST_ON_GST);
						Integer ledgerId = chartOfAccountsDao.getLedgerIdGivenName(ledger, paymentsVo.getOrganizationId());
						gl.setAccount(ledger);
						gl.setAccountId(ledgerId);
						gl.setLedgerList(chartOfAccountsDao.getLedgersByEntity( paymentsVo.getOrganizationId(), ChartOfAccountsConstants.ENTITY_INDIRECT_EXPENSES));
					
						
					}
					gl.setSubLedger(taxTypeMap.get(Integer.valueOf(vo.getGstType())));
					Double amntPaid = Double.valueOf(vo.getInterest());
					Double xchangeRate = paymentsVo.getExchangeRate() !=null && !"".equals(paymentsVo.getExchangeRate()) ? Double.valueOf(paymentsVo.getExchangeRate())  : 1.00;
					if(amntPaid > 0 ) {
						gl.setFcyDebit(Double.valueOf(df.format( amntPaid)));
						gl.setInrDebit(Double.valueOf(df.format(xchangeRate * amntPaid )));
					}else {
						gl.setFcyCredit(Double.valueOf(df.format( amntPaid * -1)));
						gl.setInrCredit(Double.valueOf(df.format(xchangeRate * amntPaid * -1)));
					}
					generalLedgers.add(gl);
				}
				
				
				if(vo.getPenalty()!=null && !vo.getPenalty().isEmpty() && !vo.getPenalty().equals("0")) {
					GeneralLedgerDetailsVo gl = new GeneralLedgerDetailsVo();
					gl.setTempId( counter);
					gl.setType(JournalEntriesConstants.PENALTY);
					gl.setDescription(JournalEntriesConstants.PENALTY_DESCRIPTION);
					if(penaltyMap!=null && penaltyMap.size()>0 && penaltyMap.containsKey(counter)) {
						String ledger = penaltyMap.get(counter);
						String  ledgerId =  ledger.split("~").length>0 && ledger.split("~")[0]!=null ? ledger.split("~")[0] : "0" ;
						String  ledgerName =  ledger.split("~").length>0 && ledger.split("~")[1]!=null ? ledger.split("~")[1] : PaymentNonCoreConstants.DEFAULT_PENALTY_LEDGER ;
						String name = chartOfAccountsDao.getLedgerName(Integer.valueOf(ledgerId), paymentsVo.getOrganizationId()) ;
						gl.setAccountId(Integer.valueOf(ledgerId));
						gl.setAccount(name!=null ? name  : ledgerName);
						gl.setLedgerList(chartOfAccountsDao.getLedgersByEntity( paymentsVo.getOrganizationId(), ChartOfAccountsConstants.ENTITY_INDIRECT_EXPENSES));
					}else {
						String ledger = chartOfAccountsDao.getDefaultLedger(ChartOfAccountsConstants.ENTITY_INDIRECT_EXPENSES, null, ChartOfAccountsConstants.FIELD_PENALTIES_ON_GST);
						Integer ledgerId = chartOfAccountsDao.getLedgerIdGivenName(ledger, paymentsVo.getOrganizationId());
						gl.setAccount(ledger);
						gl.setAccountId(ledgerId);
						gl.setLedgerList(chartOfAccountsDao.getLedgersByEntity( paymentsVo.getOrganizationId(), ChartOfAccountsConstants.ENTITY_INDIRECT_EXPENSES));
					}
					
					gl.setSubLedger(taxTypeMap.get(Integer.valueOf(vo.getGstType())));
					Double amntPaid = Double.valueOf(vo.getPenalty());
					Double xchangeRate = paymentsVo.getExchangeRate() !=null && !"".equals(paymentsVo.getExchangeRate()) ? Double.valueOf(paymentsVo.getExchangeRate())  : 1.00;
					if(amntPaid > 0 ) {
						gl.setFcyDebit(Double.valueOf(df.format( amntPaid)));
						gl.setInrDebit(Double.valueOf(df.format(xchangeRate * amntPaid)));
					}else {
						gl.setFcyCredit(Double.valueOf(df.format( amntPaid * -1)));
						gl.setInrCredit(Double.valueOf(df.format(xchangeRate * amntPaid * -1)));
					}
					
					generalLedgers.add(gl);
				}
							
				if(vo.getOthers1()!=null && !vo.getOthers1().isEmpty() && !vo.getOthers1().equals("0")) {
					GeneralLedgerDetailsVo gl = new GeneralLedgerDetailsVo();
					gl.setTempId( counter);
					gl.setType(JournalEntriesConstants.OTHERS1);
					PaymentNonCoreCustomTableVo name = paymentsVo.getCustomTableList().stream().filter(table -> JournalEntriesConstants.OTHERS1.equals(table.getcName())).findFirst().orElse(null);
					gl.setDescription(name!=null ? name.getColName() : null);
					if(others1Map!=null && others1Map.size()>0 && others1Map.containsKey(counter)) {
						String ledger = others1Map.get(counter);
						String  ledgerId =  ledger.split("~").length>0 && ledger.split("~")[0]!=null ? ledger.split("~")[0] : "0" ;
						String  ledgerName =  ledger.split("~").length>0 && ledger.split("~")[1]!=null ? ledger.split("~")[1] : null ;
						String lname = chartOfAccountsDao.getLedgerName(Integer.valueOf(ledgerId), paymentsVo.getOrganizationId()) ;
						gl.setAccountId(Integer.valueOf(ledgerId));
						gl.setAccount(lname!=null ? lname : ledgerName);
					}
					gl.setLedgerList(allLedgers);
					gl.setSubLedger(taxTypeMap.get(Integer.valueOf(vo.getGstType())));
					Double amntPaid = Double.valueOf(vo.getOthers1());
					Double xchangeRate = paymentsVo.getExchangeRate() !=null && !"".equals(paymentsVo.getExchangeRate()) ? Double.valueOf(paymentsVo.getExchangeRate())  : 1.00;
					if(amntPaid > 0 ) {
						gl.setFcyDebit(Double.valueOf(df.format( amntPaid)));
						gl.setInrDebit(Double.valueOf(df.format(xchangeRate * amntPaid)));
					}else {
						gl.setFcyCredit(Double.valueOf(df.format( amntPaid * -1)));
						gl.setInrCredit(Double.valueOf(df.format(xchangeRate * amntPaid * -1)));
					}
					
					generalLedgers.add(gl);	
				}
				
				if(vo.getOthers2()!=null && !vo.getOthers2().isEmpty() && !vo.getOthers2().equals("0")) {
					GeneralLedgerDetailsVo gl = new GeneralLedgerDetailsVo();
					gl.setTempId(counter);
					gl.setType(JournalEntriesConstants.OTHERS2);
					PaymentNonCoreCustomTableVo name = paymentsVo.getCustomTableList().stream().filter(table -> JournalEntriesConstants.OTHERS2.equals(table.getcName())).findFirst().orElse(null);
					gl.setDescription(name!=null ? name.getColName() : null);
					if(others2Map!=null && others2Map.size()>0 && others2Map.containsKey(counter)) {
						String ledger = others2Map.get(counter);
						String  ledgerId =  ledger.split("~").length>0 && ledger.split("~")[0]!=null ? ledger.split("~")[0] : "0" ;
						String  ledgerName =  ledger.split("~").length>0 && ledger.split("~")[1]!=null ? ledger.split("~")[1] :null;
						String namee = chartOfAccountsDao.getLedgerName(Integer.valueOf(ledgerId), paymentsVo.getOrganizationId()) ;
						gl.setAccountId(Integer.valueOf(ledgerId));
						gl.setAccount(namee!=null ? namee : ledgerName);
					}
					gl.setLedgerList(allLedgers);
					gl.setSubLedger(taxTypeMap.get(Integer.valueOf(vo.getGstType())));
					Double amntPaid = Double.valueOf(vo.getOthers2());
					Double xchangeRate = paymentsVo.getExchangeRate() !=null && !"".equals(paymentsVo.getExchangeRate()) ? Double.valueOf(paymentsVo.getExchangeRate())  : 1.00;
					if(amntPaid > 0 ) {
						gl.setFcyDebit(Double.valueOf(df.format( amntPaid)));
						gl.setInrDebit(Double.valueOf(df.format(xchangeRate * amntPaid)));
					}else {
						gl.setFcyCredit(Double.valueOf(df.format( amntPaid * -1)));
						gl.setInrCredit(Double.valueOf(df.format(xchangeRate * amntPaid * -1)));
					} 
					generalLedgers.add(gl);	
				}
			}
		}
		}
	}catch(Exception e) {
		logger.info("Error in getPaymentsAndGroupByLedgers:: ",e);
		e.printStackTrace();
		throw new ApplicationException(e);
	}
return generalLedgers;
	}



private List<GeneralLedgerDetailsVo> getGLForVendorAdvanceType(PaymentNonCoreVo paymentsVo, String accountType) throws ApplicationException {
	List<GeneralLedgerDetailsVo> generalLedgers= new ArrayList<>();
	DecimalFormat df = new DecimalFormat("0.00");
try { 
	GeneralLedgerVo glData = paymentsVo!=null && paymentsVo.getGeneralLedgerData()!=null ? paymentsVo.getGeneralLedgerData() : null;
		
	//To get the Bank related GL details 
	if(paymentsVo.getPaidVia()!=0 ) {
		GeneralLedgerDetailsVo gl = new GeneralLedgerDetailsVo();
		String bankLedger =  paymentsVo.getPaidViaName();
		Integer bankLedgerId = chartOfAccountsDao.getLedgerIdGivenName(bankLedger, paymentsVo.getOrganizationId());
		gl.setType(JournalEntriesConstants.SUBLEDGER_TYPE_BANKING);
		gl.setDescription(accountType);
		gl.setTempId(paymentsVo.getPaidVia());
		/*if(glData!=null && glData.getGlDetails()!=null && !glData.getGlDetails().isEmpty() &&  glData.getGlDetails().get(0) !=null && JournalEntriesConstants.SUBLEDGER_TYPE_BANKING.equals(glData.getGlDetails().get(0).getType()) && glData.getGlDetails().get(0).getTempId() == paymentsVo.getPaidVia()) {
			gl.setAccount( glData.getGlDetails().get(0).getAccount());
			gl.setAccountId( glData.getGlDetails().get(0).getAccountId());
			gl.setLedgerList(chartOfAccountsDao.getLedgersAndItsSiblingsByLedgerName(paymentsVo.getOrganizationId(), glData.getGlDetails().get(0).getAccount()));
			gl.setSubLedger(glData.getGlDetails().get(0).getSubLedger());
		}else {*/
			gl.setAccount(bankLedger);
			gl.setAccountId(bankLedgerId);
			gl.setLedgerList(chartOfAccountsDao.getLedgerById(paymentsVo.getOrganizationId(), bankLedgerId));
			gl.setSubLedger(accountType);
		//}
		Double amntPaid = Double.valueOf(paymentsVo.getAmountPaid());
		gl.setFcyCredit(Double.valueOf(amntPaid));
		Double xchangeRate = paymentsVo.getExchangeRate() !=null && !"".equals(paymentsVo.getExchangeRate()) ? Double.valueOf(paymentsVo.getExchangeRate())  : 1.00;
		gl.setInrCredit(Double.valueOf(df.format(xchangeRate * amntPaid)));
		generalLedgers.add(gl);
		}
	//To get the Vendor related GL details 
	if(paymentsVo.getVendor()!=0 ) {
		String vendorLedger = chartOfAccountsDao.getDefaultLedger(ChartOfAccountsConstants.ENTITY_VENDOR, null, null);
		logger.info("Vendor Ledger::"+vendorLedger);
		Integer vendorAccountId  = paymentsVo.getVendorAccountId()!=0 ? paymentsVo.getVendorAccountId() : chartOfAccountsDao.getLedgerIdGivenName(vendorLedger, paymentsVo.getOrganizationId());
		GeneralLedgerDetailsVo gl = new GeneralLedgerDetailsVo();
		gl.setProductId(paymentsVo.getVendor());
		gl.setType(JournalEntriesConstants.SUBLEDGER_TYPE_VENDOR);
		gl.setDescription(JournalEntriesConstants.SUBLEDGER_TYPE_VENDOR);
		if(glData!=null && glData.getGlDetails()!=null &&  glData.getGlDetails().size()>0 ) {
			Integer venLedgerId =  glData.getGlDetails().stream().filter(val -> val.getDescription().equals(JournalEntriesConstants.SUBLEDGER_TYPE_VENDOR)).findAny().get().getAccountId();
			String name = chartOfAccountsDao.getLedgerName(venLedgerId, paymentsVo.getOrganizationId()) ;
			String venLedger =name!=null ? name :   glData.getGlDetails().stream().filter(val -> val.getDescription().equals(JournalEntriesConstants.SUBLEDGER_TYPE_VENDOR)).findAny().get().getAccount();
			gl.setAccount(venLedger);
			gl.setAccountId(venLedgerId);

		}else {
			gl.setAccount(vendorLedger);
			gl.setAccountId(vendorAccountId);
			
		}
		gl.setLedgerList(chartOfAccountsDao.getLedgersByEntity(paymentsVo.getOrganizationId(), ChartOfAccountsConstants.ENTITY_VENDOR));
		gl.setSubLedger(vendorDao.getVendorName(paymentsVo.getVendor()));
		Double amntPaid = Double.valueOf(paymentsVo.getAmountPaid());
		gl.setFcyDebit(Double.valueOf(df.format( amntPaid)));
		Double xchangeRate = paymentsVo.getExchangeRate() !=null && !"".equals(paymentsVo.getExchangeRate()) ? Double.valueOf(paymentsVo.getExchangeRate())  : 1.00;
		gl.setInrDebit(Double.valueOf(df.format(xchangeRate * amntPaid)));
		generalLedgers.add(gl);
		}
	}catch(Exception e) {
		logger.info("Error in getPaymentsAndGroupByLedgers:: ",e);
		e.printStackTrace();
		throw new ApplicationException(e);
	}
return generalLedgers;

	}



private List<GeneralLedgerDetailsVo> getGLForBillsPaymetsType (PaymentNonCoreVo paymentsVo , String accountType) throws ApplicationException{
	List<GeneralLedgerDetailsVo> generalLedgers= new ArrayList<>();
	DecimalFormat df = new DecimalFormat("0.00");
try { 
	GeneralLedgerVo glData = paymentsVo!=null && paymentsVo.getGeneralLedgerData()!=null ? paymentsVo.getGeneralLedgerData() : null;
		Map<Integer , String > others1Map = new HashMap<Integer, String>();
		Map<Integer , String > others2Map = new HashMap<Integer, String>();
		Map<Integer , String > billsMap = new HashMap<Integer, String>();
		Map<Integer , String > vendorMap = new HashMap<Integer, String>();
		Map<Integer , String > advanceMap = new HashMap<Integer, String>();
		if(glData!=null && glData.getGlDetails()!=null && glData.getGlDetails().size()>0) {
			for(GeneralLedgerDetailsVo details:glData.getGlDetails()) {
				if(JournalEntriesConstants.BILL.equals(details.getType())) {
					billsMap.put(details.getTempId(), details.getAccount()+"~"+details.getAccountId());
				}
				if(JournalEntriesConstants.OTHERS1.equals(details.getType())) {
					others1Map.put(details.getTempId(), details.getAccount()+"~"+details.getAccountId());
				}
				if(JournalEntriesConstants.OTHERS2.equals(details.getType())) {
					others2Map.put(details.getTempId(), details.getAccount()+"~"+details.getAccountId());
				}
				if(JournalEntriesConstants.SUBLEDGER_TYPE_VENDOR.equals(details.getType())) {
					vendorMap.put(details.getTempId(), details.getAccount()+"~"+details.getAccountId());
				}
				if(JournalEntriesConstants.ADVANCE.equals(details.getDescription())) {
					advanceMap.put(details.getTempId(), details.getAccount()+"~"+details.getAccountId());
				}
			}
		}
	//To get the Bank related GL details 
	if(paymentsVo.getPaidVia()!=0 ) {
		GeneralLedgerDetailsVo gl = new GeneralLedgerDetailsVo();
		String bankLedger =  paymentsVo.getPaidViaName();
		Integer bankLedgerId = chartOfAccountsDao.getLedgerIdGivenName(bankLedger, paymentsVo.getOrganizationId());
		gl.setType(JournalEntriesConstants.SUBLEDGER_TYPE_BANKING);
		gl.setDescription(accountType);
		gl.setTempId(paymentsVo.getPaidVia());
		/*if(glData!=null && glData.getGlDetails()!=null && !glData.getGlDetails().isEmpty() &&  glData.getGlDetails().get(0) !=null && JournalEntriesConstants.SUBLEDGER_TYPE_BANKING.equals(glData.getGlDetails().get(0).getType()) && glData.getGlDetails().get(0).getTempId() == paymentsVo.getPaidVia()) {
			gl.setAccount( glData.getGlDetails().get(0).getAccount());
			gl.setAccountId( glData.getGlDetails().get(0).getAccountId());
			gl.setLedgerList(chartOfAccountsDao.getLedgersAndItsSiblingsByLedgerName(paymentsVo.getOrganizationId(), glData.getGlDetails().get(0).getAccount()));
			gl.setSubLedger(glData.getGlDetails().get(0).getSubLedger());
		}else {*/
			gl.setAccount(bankLedger);
			gl.setAccountId(bankLedgerId);
			gl.setLedgerList(chartOfAccountsDao.getLedgerById(paymentsVo.getOrganizationId(), bankLedgerId));
			gl.setSubLedger(accountType);
		//}
		Double amntPaid = Double.valueOf(paymentsVo.getAmountPaid());
		gl.setFcyCredit(Double.valueOf(amntPaid));
		Double xchangeRate = paymentsVo.getExchangeRate() !=null && !"".equals(paymentsVo.getExchangeRate()) ? Double.valueOf(paymentsVo.getExchangeRate())  : 1.00;
		gl.setInrCredit(Double.valueOf(df.format(xchangeRate * amntPaid)));
		generalLedgers.add(gl);
		}
	if(paymentsVo.getVendor()!=0 ) {
		String vendorLedger = chartOfAccountsDao.getDefaultLedger(ChartOfAccountsConstants.ENTITY_VENDOR, null, null);
		Integer vendorAccountId  = paymentsVo.getVendorAccountId()!=0 ? paymentsVo.getVendorAccountId() : chartOfAccountsDao.getLedgerIdGivenName(vendorLedger, paymentsVo.getOrganizationId());
		List<MinimalChartOfAccountsVo> ledgerList = chartOfAccountsDao.getLedgersByEntity(paymentsVo.getOrganizationId(), ChartOfAccountsConstants.ENTITY_VENDOR);
		if(!paymentsVo.isBulk()) {
			logger.info("Is bulk false ");
			GeneralLedgerDetailsVo gl = new GeneralLedgerDetailsVo();
			gl.setType(JournalEntriesConstants.SUBLEDGER_TYPE_VENDOR);
			gl.setDescription(JournalEntriesConstants.SUBLEDGER_TYPE_VENDOR);
			gl.setTempId(paymentsVo.getVendor());
			if(vendorMap!=null && vendorMap.size()>0 && vendorMap.containsKey(Integer.valueOf( paymentsVo.getVendor()))) {
				Integer id = Integer.valueOf(vendorMap.get(Integer.valueOf(paymentsVo.getVendor())).split("~")[1]) ;
				String name = chartOfAccountsDao.getLedgerName(id, paymentsVo.getOrganizationId());
				gl.setAccount(name!=null ? name : vendorMap.get(Integer.valueOf( paymentsVo.getVendor())).split("~")[0]);
				gl.setAccountId(id);
			}else {
			gl.setAccount(vendorLedger);
			gl.setAccountId(vendorAccountId);
			}
			gl.setLedgerList(ledgerList);
			gl.setProductId(paymentsVo.getVendor());
			gl.setSubLedger(vendorDao.getVendorName(paymentsVo.getVendor()));
			Double amntPaid = Double.valueOf(paymentsVo.getAmountPaid());
			gl.setFcyDebit(Double.valueOf(df.format( amntPaid)));
			Double xchangeRate = paymentsVo.getExchangeRate() !=null && !"".equals(paymentsVo.getExchangeRate()) ? Double.valueOf(paymentsVo.getExchangeRate())  : 1.00;
			gl.setInrDebit(Double.valueOf(df.format(xchangeRate * amntPaid)));
			generalLedgers.add(gl);
		}else {
			logger.info("Is bulk true ");
			if(paymentsVo.getPayments()!=null && paymentsVo.getPayments().size()>0) {
				List<MinimalChartOfAccountsVo> allLedgers = chartOfAccountsDao.getAllLedgersByOrgId(paymentsVo.getOrganizationId());
				for(PaymentNonCoreBaseVo bills : paymentsVo.getPayments()) {
					if("Bill".equals(bills.getType())) {
						Connection con = getAccountsPayable();
						String invNo = billsInvoiceDao.getInvoiceOrderNo( Integer.valueOf(bills.getBillRef()), paymentsVo.getOrganizationId(), con);
						closeResources(null, null, con);
						
						if( bills.getBillRef()!=null && !bills.getBillRef().isEmpty() && !bills.getBillRef().equals("null")) {
							GeneralLedgerDetailsVo gl = new GeneralLedgerDetailsVo();
							gl.setType(JournalEntriesConstants.BILL);
							gl.setDescription(JournalEntriesConstants.BILL);
							gl.setTempId(Integer.valueOf( bills.getBillRef()));
							if(billsMap!=null && billsMap.size()>0 && billsMap.containsKey(Integer.valueOf( bills.getBillRef()))) {
								Integer id = Integer.valueOf(billsMap.get(Integer.valueOf( bills.getBillRef())).split("~")[1]) ;
								String name = chartOfAccountsDao.getLedgerName(id, paymentsVo.getOrganizationId());
								gl.setAccount(name!=null ? name : billsMap.get(Integer.valueOf( bills.getBillRef())).split("~")[0]);
								gl.setAccountId(id);
							}else {
							gl.setAccount(vendorLedger);
							gl.setAccountId(vendorAccountId);
							}
							gl.setLedgerList(ledgerList);
							gl.setSubLedger(invNo);
							Double amntPaid = Double.valueOf(bills.getBillAmount());
							gl.setFcyDebit(Double.valueOf(df.format( amntPaid)));
							Double xchangeRate = paymentsVo.getExchangeRate() !=null && !"".equals(paymentsVo.getExchangeRate()) ? Double.valueOf(paymentsVo.getExchangeRate())  : 1.00;
							gl.setInrDebit(Double.valueOf(df.format(xchangeRate * amntPaid)));
							gl.setProductId(paymentsVo.getVendor());
							generalLedgers.add(gl);
							
						}
						if( bills.getOthers1()!=null && !bills.getOthers1().isEmpty() &&  !bills.getOthers1().equals("null")) {
							GeneralLedgerDetailsVo gl = new GeneralLedgerDetailsVo();
							gl.setType(JournalEntriesConstants.OTHERS1);
							String tName  = paymentsVo.getCustomTableList()!=null && paymentsVo.getCustomTableList().size()> 0 ? 
									paymentsVo.getCustomTableList().stream().filter(val -> JournalEntriesConstants.OTHERS1.equals(val.getcName()) && val.isColumnShow()).findFirst().get().getColName() : null ;
							gl.setDescription(tName!=null ? tName : null);
							gl.setTempId(Integer.valueOf( bills.getBillRef()));
							if(others1Map!=null && others1Map.size()>0 && others1Map.containsKey(Integer.valueOf( bills.getBillRef()))) {
								Integer id = Integer.valueOf(others1Map.get(Integer.valueOf( bills.getBillRef())).split("~")[1]);
								String name = chartOfAccountsDao.getLedgerName(id, paymentsVo.getOrganizationId());
								
								gl.setAccount(name !=null ? name : others1Map.get(Integer.valueOf( bills.getBillRef())).split("~")[0]);
								gl.setAccountId(id);
							}
							//gl.setAccount(vendorLedger);
							//gl.setAccountId(vendorAccountId);
							gl.setLedgerList(allLedgers);
							gl.setSubLedger(invNo);
							gl.setProductId(paymentsVo.getVendor());
							Double amntPaid = Double.valueOf(bills.getOthers1());
							Double xchangeRate = paymentsVo.getExchangeRate() !=null && !"".equals(paymentsVo.getExchangeRate()) ? Double.valueOf(paymentsVo.getExchangeRate())  : 1.00;
							if(amntPaid > 0 ) {
								gl.setFcyDebit(Double.valueOf(df.format( amntPaid)));
								gl.setInrDebit(Double.valueOf(df.format(xchangeRate * amntPaid)));
							}else {
								gl.setFcyCredit(Double.valueOf(df.format( amntPaid * -1)));
								gl.setInrCredit(Double.valueOf(df.format(xchangeRate * amntPaid * -1)));
							}
							generalLedgers.add(gl);
							
						}
						if( bills.getOthers2()!=null && !bills.getOthers2().isEmpty()  && !bills.getOthers2().equals("null")) {
							GeneralLedgerDetailsVo gl = new GeneralLedgerDetailsVo();
							gl.setType(JournalEntriesConstants.OTHERS2);
							String tName  = paymentsVo.getCustomTableList()!=null && paymentsVo.getCustomTableList().size()> 0 ? 
									paymentsVo.getCustomTableList().stream().filter(val -> JournalEntriesConstants.OTHERS2.equals(val.getcName()) && val.isColumnShow()).findFirst().get().getColName() : null ;
							gl.setDescription(tName!=null ? tName : null);
							gl.setTempId(Integer.valueOf( bills.getBillRef()));
							if(others2Map!=null && others2Map.size()>0 && others2Map.containsKey(Integer.valueOf( bills.getBillRef()))) {
								Integer id = Integer.valueOf(others2Map.get(Integer.valueOf( bills.getBillRef())).split("~")[1]);
								String name = chartOfAccountsDao.getLedgerName(id, paymentsVo.getOrganizationId());
								
								gl.setAccount(name!=null ? name : others2Map.get(Integer.valueOf( bills.getBillRef())).split("~")[0]);
								gl.setAccountId(id);
							}
							//gl.setAccount(vendorLedger);
							//gl.setAccountId(vendorAccountId);
							gl.setLedgerList(allLedgers);
							gl.setSubLedger(invNo);
							gl.setProductId(paymentsVo.getVendor());
							Double amntPaid = Double.valueOf(bills.getOthers2());
							Double xchangeRate = paymentsVo.getExchangeRate() !=null && !"".equals(paymentsVo.getExchangeRate()) ? Double.valueOf(paymentsVo.getExchangeRate())  : 1.00;
							if(amntPaid > 0 ) {
								gl.setFcyDebit(Double.valueOf(df.format( amntPaid)));
								gl.setInrDebit(Double.valueOf(df.format(xchangeRate * amntPaid)));
							}else {
								gl.setFcyCredit(Double.valueOf(df.format( amntPaid  * -1)));
								gl.setInrCredit(Double.valueOf(df.format(xchangeRate * amntPaid * -1)));
							}
							generalLedgers.add(gl);
							
						}
					}
					if("Advance".equals(bills.getType()) && bills.getBillRef()!=null && ! bills.getBillRef().isEmpty()){
						GeneralLedgerDetailsVo gl = new GeneralLedgerDetailsVo();
						gl.setType(JournalEntriesConstants.SUBLEDGER_TYPE_VENDOR);
						gl.setDescription(JournalEntriesConstants.ADVANCE);
						gl.setTempId(Integer.valueOf( bills.getBillRef()));
						logger.info("advanceMap::"+ advanceMap);
						if(advanceMap!=null && advanceMap.size()>0 && advanceMap.containsKey(Integer.valueOf( bills.getBillRef()))) {

							Integer id = Integer.valueOf(advanceMap.get(Integer.valueOf( bills.getBillRef())).split("~")[1]);
							logger.info("id ::::"+ id);
							String name = chartOfAccountsDao.getLedgerName(id, paymentsVo.getOrganizationId());
							
							gl.setAccount(name!=null ? name : advanceMap.get(Integer.valueOf( bills.getBillRef())).split("~")[0]);
							gl.setAccountId(id);
						}else {
							gl.setAccount(vendorLedger);
							gl.setAccountId(vendorAccountId);
						}
						gl.setLedgerList(ledgerList);
						gl.setProductId(paymentsVo.getVendor());
						gl.setSubLedger(paymentNonCoreDao.getVendorAdvanceReferenceNo( Integer.valueOf(bills.getBillRef()), paymentsVo.getOrganizationId()));
						Double amntPaid = Double.valueOf(bills.getTotalAmount());
						gl.setFcyCredit(Double.valueOf(df.format( amntPaid)));
						Double xchangeRate = paymentsVo.getExchangeRate() !=null && !"".equals(paymentsVo.getExchangeRate()) ? Double.valueOf(paymentsVo.getExchangeRate())  : 1.00;
						gl.setInrCredit(Double.valueOf(df.format(xchangeRate * amntPaid)));
						generalLedgers.add(gl);
					}
			}
			}
		}
	}
	}catch(Exception e) {
		logger.info("Error in getPaymentsAndGroupByLedgers:: ",e);
		e.printStackTrace();
		throw new ApplicationException(e);
	}
return generalLedgers;
}

	public List<GeneralLedgerDetailsVo>  getARInvoiceAndGroupByLedgers(ArInvoiceVo  invoiceVo) throws ApplicationException {
		List<GeneralLedgerDetailsVo> generalLedgers = new ArrayList<GeneralLedgerDetailsVo>();
		logger.info("To method getInvoiceAndGroupByLedgers with Id" +invoiceVo);
		try {
			if(invoiceVo!=null && invoiceVo.getGeneralInformation()!=null && invoiceVo.getProducts()!=null && invoiceVo.getProducts().size()>0) {
				GeneralLedgerVo glVo =  invoiceVo.getGeneralInformation().getGeneralLedgerData();
				logger.info("glvo"+glVo);
				DecimalFormat df = new DecimalFormat("0.00");
				Map<Integer , String > discountMap = new HashMap<Integer, String>();
				Map<Integer , String > adjustmentMap = new HashMap<Integer, String>();
				Map<Integer , String > tdsMap = new HashMap<Integer, String>();
				Map<Integer , String > customerMap = new HashMap<Integer, String>();
				Map<String , String > gstMap = new HashMap<String, String>();
				if(glVo!=null && glVo.getGlDetails()!=null && glVo.getGlDetails().size()>0 ) {				logger.info("glvo"+glVo);
				for(GeneralLedgerDetailsVo details :  invoiceVo.getGeneralInformation().getGeneralLedgerData().getGlDetails()){
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
				//To separate the ledger of each products
				for(ArInvoiceProductVo product :  invoiceVo.getProducts()){
					logger.info("To get the products"+product);
					if(product.getProductId()!=null && !product.getStatus().equals("DEL")) {
						ProductVo prodVo = productDao.getProductById(product.getProductId());
						logger.info("prodVo::"+ prodVo);
						GeneralLedgerDetailsVo gl = new GeneralLedgerDetailsVo();
						gl.setType(JournalEntriesConstants.TYPE_PRODUCTS);
						gl.setTempId(product.getTempId());
						gl.setDescription(prodVo.getName());
						if(product.getProductAccountName()!=null && product.getProductAccountId()!=null && product.getProductAccountId()!=0) {
							gl.setAccount(chartOfAccountsDao.getLedgerName(product.getProductAccountId(), invoiceVo.getOrgId()));
							gl.setAccountId(product.getProductAccountId());
							gl.setLedgerList(chartOfAccountsDao.getItemsSalesAccounts(prodVo.getType() , invoiceVo.getOrgId()));
						}else {
							gl.setAccount(chartOfAccountsDao.getLedgerName(prodVo.getSalesAccountId(), invoiceVo.getOrgId()));
							gl.setAccountId(prodVo.getSalesAccountId());
							if(prodVo.getSalesAccountName()!=null) {
							gl.setLedgerList(chartOfAccountsDao.getItemsSalesAccounts(prodVo.getType() , invoiceVo.getOrgId()));
							}
						}
						gl.setProductId(prodVo.getId());
						gl.setSubLedger(prodVo.getProductId());
						gl.setFcyCredit(Double.valueOf(df.format( product.getAmount())));
						gl.setInrCredit(Double.valueOf(df.format(invoiceVo.getGeneralInformation().getExchangeRate() * product.getAmount())));
						generalLedgers.add(gl);
					}
				}
				
				
				
				
				// To get the customer Id and its selected customer account 
				if(invoiceVo.getGeneralInformation().getCustomerId()!=null ) {
					logger.info("To get the customers");
					GeneralLedgerDetailsVo gl = new GeneralLedgerDetailsVo();
					gl.setDescription(JournalEntriesConstants.SUBLEDGER_TYPE_CUSTOMER);
					gl.setType(JournalEntriesConstants.SUBLEDGER_TYPE_CUSTOMER);
					if(customerMap.size()>0){
						for(Map.Entry<Integer, String> val : customerMap.entrySet()) {
							String name = chartOfAccountsDao.getLedgerName(val.getKey(), invoiceVo.getOrgId());
							gl.setAccount(name!=null ? name : val.getValue());
							gl.setAccountId(val.getKey());
						}
					}else {
						String ledgerName = chartOfAccountsDao.getDefaultLedger(ChartOfAccountsConstants.ENTITY_CUSTOMER, null, null);
						int ledgerId = chartOfAccountsDao.getLedgerIdGivenName(ledgerName , invoiceVo.getOrgId());
						gl.setAccount(ledgerName);
						gl.setAccountId(ledgerId);
					}
					gl.setSubLedger(customerDao.getCustomerName(invoiceVo.getGeneralInformation().getCustomerId()));
					gl.setFcyDebit(Double.valueOf(df.format(invoiceVo.getGeneralInformation().getTotal())));
					gl.setInrDebit(Double.valueOf(df.format(invoiceVo.getGeneralInformation().getExchangeRate() * invoiceVo.getGeneralInformation().getTotal())));
					gl.setLedgerList(chartOfAccountsDao.getLedgersByEntity(invoiceVo.getOrgId(), ChartOfAccountsConstants.ENTITY_CUSTOMER));
					generalLedgers.add(gl);
				}
				
				
				HashMap<String , String> creditLedgerMap  = new HashMap<String, String>();
				for(ArInvoiceProductVo product : invoiceVo.getProducts()) {
						//To separate GST ledger
					if(product.getStatus()!=null && !"DEL".equals(product.getStatus()) && product.getTaxDetails()!=null && product.getTaxDetails().getTaxDistribution()!=null) {
						for(ArInvoiceTaxDistributionVo taxdetails : product.getTaxDetails().getTaxDistribution()) {
							if(taxdetails.getTaxName()!=null && taxdetails.getTaxRate()!=null && taxdetails.getTaxAmount()!=null) {
								logger.info("To get the gst");
								String opLedger = chartOfAccountsDao.getDefaultLedger(ChartOfAccountsConstants.ENTITY_GST_RECEIVABLE_PAYABLE, ChartOfAccountsConstants.MODULE_AR, null);
								Integer opLedgerId = chartOfAccountsDao.getLedgerIdGivenName(opLedger, invoiceVo.getOrgId());
								if(creditLedgerMap.containsKey(opLedger+"~"+taxdetails.getTaxName()+"~"+taxdetails.getTaxRate())) {
									String[] values= creditLedgerMap.get(opLedger+"~"+taxdetails.getTaxName()+"~"+taxdetails.getTaxRate()).split("~");
									String amnt = values[0];
									Double amount = Double.valueOf(amnt);
									amount = amount + taxdetails.getTaxAmount();
									creditLedgerMap.put(opLedger+"~"+taxdetails.getTaxName()+"~"+taxdetails.getTaxRate(), amount+"~"+values[1]);
								}else {
									creditLedgerMap.put(opLedger+"~"+taxdetails.getTaxName()+"~"+taxdetails.getTaxRate(), taxdetails.getTaxAmount()+"~"+opLedgerId);
								}
							}
						}
					}
				}
						if(creditLedgerMap.size()>0) {

							   for (Map.Entry<String,String> entry : creditLedgerMap.entrySet())  {
								GeneralLedgerDetailsVo gl = new GeneralLedgerDetailsVo();
								gl.setType(JournalEntriesConstants.SUBLEDGER_TYPE_GST);
								gl.setDescription(JournalEntriesConstants.SUBLEDGER_TYPE_GST);
								if(gstMap.size()>0 && gstMap.containsKey(entry.getKey().split("~")[1])) {
									String gstVal = gstMap.get(entry.getKey().split("~")[1]);
									int id = gstVal.split("~")[0]!=null ?Integer.valueOf(gstVal.split("~")[0]) : 0;
									String mapLedgerName = gstVal.split("~")[1]!=null ? gstVal.split("~")[1] : null;
									String name = id!=0 ? chartOfAccountsDao.getLedgerName(id, invoiceVo.getOrgId()) : null;

									gl.setAccountId(id);
									gl.setAccount(name!=null ? name : mapLedgerName);
								}else {
									gl.setAccount(entry.getKey().split("~")[0]);
									gl.setAccountId(entry.getValue().split("~")[1]!=null ? Integer.valueOf(entry.getValue().split("~")[1]) : 0);
								}
								gl.setSubLedger(entry.getKey().split("~")[1]);
								gl.setFcyCredit(entry.getValue().split("~")[0]!=null ? Double.valueOf(entry.getValue().split("~")[0]) : 0.00);
								gl.setInrCredit(Double.valueOf(df.format(invoiceVo.getGeneralInformation().getExchangeRate() * gl.getFcyCredit() )));
								gl.setLedgerList(chartOfAccountsDao.getLedgersByEntity(invoiceVo.getOrgId(), ChartOfAccountsConstants.ENTITY_GST_RECEIVABLE_PAYABLE));
								generalLedgers.add(gl);
							}
						}
				
					//To separate the ledger of TDS
				if(invoiceVo.getGeneralInformation().getTdsId()!=null && invoiceVo.getGeneralInformation().getTdsId()!=0 && invoiceVo.getGeneralInformation().getTdsValue()!=null && !invoiceVo.getGeneralInformation().getTdsValue().equals(0.0)) {
					logger.info("To get the TDS");
					GeneralLedgerDetailsVo gl = new GeneralLedgerDetailsVo();
					gl.setDescription(JournalEntriesConstants.TYPE_TDS);
					gl.setType(JournalEntriesConstants.TYPE_TDS);
					String tdsLedger = chartOfAccountsDao.getDefaultLedger(ChartOfAccountsConstants.ENTITY_ADVANCE_TAX_AND_TDS, ChartOfAccountsConstants.MODULE_AR, null);
					Integer tdsLedgerId = chartOfAccountsDao.getLedgerIdGivenName(tdsLedger, invoiceVo.getOrgId());
					if(tdsMap.size()>0) {
						for(Map.Entry<Integer, String> entryset : tdsMap.entrySet()) {
							String name = chartOfAccountsDao.getLedgerName(entryset.getKey(), invoiceVo.getOrgId());
							gl.setAccount(name!=null ? name : entryset.getValue());
							gl.setAccountId(entryset.getKey());
						}
						
					}else {
						gl.setAccount(tdsLedger);
						gl.setAccountId(tdsLedgerId);
					}
					gl.setFcyDebit(invoiceVo.getGeneralInformation().getTdsValue());
					gl.setInrDebit(Double.valueOf(df.format(invoiceVo.getGeneralInformation().getExchangeRate() * invoiceVo.getGeneralInformation().getTdsValue() )));
					gl.setLedgerList(chartOfAccountsDao.getLedgersByEntity(invoiceVo.getOrgId(), ChartOfAccountsConstants.ENTITY_ADVANCE_TAX_AND_TDS));
					generalLedgers.add(gl);
					
				}
				
				
				//To separate the ledger of Discount 
				if(invoiceVo.getGeneralInformation().getDiscountValue()!=null && !invoiceVo.getGeneralInformation().getDiscountValue().equals(0.0)) {
					String discountLedger = chartOfAccountsDao.getDefaultLedger(ChartOfAccountsConstants.ENTITY_DISCOUNT_GIVEN, ChartOfAccountsConstants.MODULE_AR, ChartOfAccountsConstants.FIELD_DISCOUNT);
					int ledgerId = chartOfAccountsDao.getLedgerIdGivenName(discountLedger , invoiceVo.getOrgId());
					logger.info("To get the discount");
					GeneralLedgerDetailsVo gl = new GeneralLedgerDetailsVo();
					gl.setDescription(JournalEntriesConstants.TYPE_DISCOUNT);
					gl.setType(JournalEntriesConstants.TYPE_DISCOUNT);
					if(discountMap.size()>0) {
						for(Map.Entry<Integer, String> entryset : discountMap.entrySet()) {
							String name = chartOfAccountsDao.getLedgerName(entryset.getKey(), invoiceVo.getOrgId());
							gl.setAccount(name!=null ? name : entryset.getValue());
							gl.setAccountId(entryset.getKey());
						}
					}else {
						gl.setAccount(discountLedger);
						gl.setAccountId(ledgerId);
					}
					gl.setFcyDebit(invoiceVo.getGeneralInformation().getDiscountValue());
					gl.setInrDebit(Double.valueOf(df.format(invoiceVo.getGeneralInformation().getExchangeRate() * invoiceVo.getGeneralInformation().getDiscountValue())));
					gl.setLedgerList(chartOfAccountsDao.getLedgersByEntity(invoiceVo.getOrgId(), ChartOfAccountsConstants.ENTITY_DISCOUNT_GIVEN));
					generalLedgers.add(gl);
					
				}
				
				
				//To separate the ledger of Adjustment 
				if( invoiceVo.getGeneralInformation().getAdjustmentValue()!=null && !invoiceVo.getGeneralInformation().getAdjustmentValue().equals(0.0)) {
					logger.info("To get the adjustment");
					String adjLedger = chartOfAccountsDao.getDefaultLedgerByMultipleEntity(Arrays.asList(ChartOfAccountsConstants.ENTITY_ROUND_OFF,ChartOfAccountsConstants.ENTITY_SALE),
												ChartOfAccountsConstants.MODULE_AR, ChartOfAccountsConstants.FIELD_ADJUSTMENTS);
					int ledgerId = chartOfAccountsDao.getLedgerIdGivenName(adjLedger, invoiceVo.getOrgId());
					GeneralLedgerDetailsVo gl = new GeneralLedgerDetailsVo();
					gl.setDescription(JournalEntriesConstants.TYPE_ADJUSTMENT);
					if(adjustmentMap.size()>0) {
						for(Map.Entry<Integer, String> entryset : adjustmentMap.entrySet()) {
							String name = chartOfAccountsDao.getLedgerName(entryset.getKey(), invoiceVo.getOrgId());
							gl.setAccount(name!=null ? name : entryset.getValue());
							gl.setAccountId(entryset.getKey());
						}
					}else {
						gl.setAccount(adjLedger);
						gl.setAccountId(ledgerId);
					}
					gl.setType(JournalEntriesConstants.TYPE_ADJUSTMENT);
					if(invoiceVo.getGeneralInformation().getAdjustmentValue() > 0) {
						gl.setFcyCredit(Math.abs(invoiceVo.getGeneralInformation().getAdjustmentValue() ));
						gl.setInrCredit(Double.valueOf(df.format(invoiceVo.getGeneralInformation().getExchangeRate() * Math.abs(invoiceVo.getGeneralInformation().getAdjustmentValue()) )));
					}else {
							gl.setFcyDebit(Math.abs(invoiceVo.getGeneralInformation().getAdjustmentValue()) );
							gl.setInrDebit(Double.valueOf(df.format(invoiceVo.getGeneralInformation().getExchangeRate() * Math.abs(invoiceVo.getGeneralInformation().getAdjustmentValue()) )));
					}
					gl.setLedgerList(chartOfAccountsDao.getLedgersByMultipleEntity(invoiceVo.getOrgId(), Arrays.asList(ChartOfAccountsConstants.ENTITY_SALE , ChartOfAccountsConstants.ENTITY_ROUND_OFF)));
					generalLedgers.add(gl);
				}
				
			logger.info("Ledgers Seaparated to GeneralLedgerVo::" + generalLedgers.size());
		}
		}catch (ApplicationException | SQLException e) {
			logger.info("Error in getInvoiceAndGroupByLedgers:: ",e);
			e.printStackTrace();
			throw new ApplicationException(e);
		}
		return generalLedgers;
	}

	public List<GeneralLedgerDetailsVo>   getCreditNotesAndGroupByLedgers(CreditNotesVo creditNoteVo, ArInvoiceVo invoiceVo) throws ApplicationException {
		logger.info("To method getCreditNotesAndGroupByLedgers with VO" + creditNoteVo);
		 List<GeneralLedgerDetailsVo> generalLedgers = new ArrayList<GeneralLedgerDetailsVo>();
			Double exchangeRate = creditNoteVo.getExchangeRate()!=null ? Double.valueOf(creditNoteVo.getExchangeRate()) : 1.0 ;
		try {
			if(creditNoteVo!=null && creditNoteVo.getProducts()!=null && creditNoteVo.getProducts().size()>0 && invoiceVo!=null) {
				//ARInvoiceVo invoiceVo = arInvoiceDao.getInvoiceById(Integer.valueOf( creditNoteVo.getOriginalInvoiceId()));
				GeneralLedgerVo glVo =  creditNoteVo.getGeneralLedgerData();
				GeneralLedgerVo invoiceglVo = null; 
						
						
						if(invoiceVo.getGeneralInformation()!=null && invoiceVo.getGeneralInformation().getGeneralLedgerData()!=null) {
							invoiceglVo = invoiceVo.getGeneralInformation().getGeneralLedgerData();
						}else {
							invoiceglVo = getGeneralLedgers(JournalEntriesConstants.SUB_MODULE_INVOICES, invoiceVo);
						}
				DecimalFormat df = new DecimalFormat("0.00");
				logger.info("glvo"+glVo);
				Map<Integer , String > discountMap = new HashMap<Integer, String>();
				Map<Integer , String > adjustmentMap = new HashMap<Integer, String>();
				Map<Integer , String > tdsMap = new HashMap<Integer, String>();
				Map<Integer , String > customerMap = new HashMap<Integer, String>();
				Map<String , String > gstMap = new HashMap<String, String>();
				
				Map<Integer , String > invoiceDiscountMap = new HashMap<Integer, String>();
				Map<Integer , String > invoiceAdjustmentMap = new HashMap<Integer, String>();
				Map<Integer , String > invoiceTdsMap = new HashMap<Integer, String>();
				Map<Integer , String > invoiceCustomerMap = new HashMap<Integer, String>();
				Map<String , String > invoiceGstMap = new HashMap<String, String>();
				
				if(glVo!=null && glVo.getGlDetails()!=null && glVo.getGlDetails().size()>0 ) {				
				for(GeneralLedgerDetailsVo details :  creditNoteVo.getGeneralLedgerData().getGlDetails()){
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
				
				if(invoiceglVo!=null && invoiceglVo.getGlDetails()!=null && invoiceglVo.getGlDetails().size()>0 ) {				
					for(GeneralLedgerDetailsVo details :  invoiceglVo.getGlDetails()){
						logger.info("glvo Details"+details);
						if(details.getType()!=null && details.getType().equals(JournalEntriesConstants.TYPE_DISCOUNT)) {
							invoiceDiscountMap.put(details.getAccountId(), details.getAccount());
						}
						if(details.getType()!=null && details.getType().equals(JournalEntriesConstants.TYPE_ADJUSTMENT)) {
							invoiceAdjustmentMap.put(details.getAccountId(), details.getAccount());
						}
						if(details.getType()!=null && details.getType().equals(JournalEntriesConstants.TYPE_TDS)) {
							invoiceTdsMap.put(details.getAccountId(), details.getAccount());
						}
						if(details.getType()!=null && details.getType().equals(JournalEntriesConstants.SUBLEDGER_TYPE_CUSTOMER)) {
							invoiceCustomerMap.put(details.getAccountId(), details.getAccount());
						}
						if(details.getType()!=null && details.getType().equals(JournalEntriesConstants.SUBLEDGER_TYPE_GST)) {
							invoiceGstMap.put(details.getSubLedger() ,details.getAccountId()+"~"+ details.getAccount());
						}
						
					}
					}
				
				//To separate the ledger of each products
				for(ArInvoiceProductVo product :  creditNoteVo.getProducts()){
					logger.info("To get the products"+product);
					if(product.getProductId()!=null && !product.getStatus().equals("DEL")) {
						ProductVo prodVo = productDao.getProductById(product.getProductId());
						GeneralLedgerDetailsVo gl = new GeneralLedgerDetailsVo();
						gl.setType(JournalEntriesConstants.TYPE_PRODUCTS);
						gl.setTempId(product.getTempId());
						gl.setDescription(prodVo.getName());
						if(product.getProductAccountName()!=null && product.getProductAccountId()!=null && product.getProductAccountId() != 0) {
							String name = chartOfAccountsDao.getLedgerName(product.getProductAccountId(), creditNoteVo.getOrganizationId());
							gl.setAccount(name!=null ? name: product.getProductAccountName());
							gl.setAccountId(product.getProductAccountId());
							gl.setLedgerList(chartOfAccountsDao.getItemsSalesAccounts(prodVo.getType() , creditNoteVo.getOrganizationId()));
						}else {
							gl.setAccount(prodVo.getSalesAccountName());
							gl.setAccountId(prodVo.getSalesAccountId());
							if(prodVo.getSalesAccountName()!=null) {
							gl.setLedgerList(chartOfAccountsDao.getItemsSalesAccounts(prodVo.getType() , creditNoteVo.getOrganizationId()));
							}
						}
						
						gl.setProductId(prodVo.getId());
						gl.setSubLedger(prodVo.getProductId());
						gl.setFcyDebit(product.getAmount());
						gl.setInrDebit(Double.valueOf(df.format(exchangeRate * product.getAmount())));
						generalLedgers.add(gl);
					}
				}
				
						
				// To get the customer Id and its selected customer account 
				if(creditNoteVo.getCustomerId()!=0 ) {
					logger.info("To get the customers");
					GeneralLedgerDetailsVo gl = new GeneralLedgerDetailsVo();
					gl.setDescription(JournalEntriesConstants.SUBLEDGER_TYPE_CUSTOMER);
					gl.setType(JournalEntriesConstants.SUBLEDGER_TYPE_CUSTOMER);
					if(customerMap.size()>0){
						for(Map.Entry<Integer, String> entryset : customerMap.entrySet()) {
							String name = chartOfAccountsDao.getLedgerName(entryset.getKey(), creditNoteVo.getOrganizationId());
							gl.setAccount(name!=null ? name : entryset.getValue());
							gl.setAccountId(entryset.getKey());
						}
						
					}else {
						if(invoiceCustomerMap.size()>0) {
							logger.info("To set values from invoice");
							for(Map.Entry<Integer, String> entryset : invoiceCustomerMap.entrySet()) {
								String name = chartOfAccountsDao.getLedgerName(entryset.getKey(), creditNoteVo.getOrganizationId());
								gl.setAccount(name!=null ? name : entryset.getValue());
								gl.setAccountId(entryset.getKey());
							}
						} else {
							logger.info("To set default values ");
						String ledgerName = chartOfAccountsDao.getDefaultLedger(ChartOfAccountsConstants.ENTITY_CUSTOMER, null, null);
						int ledgerId = chartOfAccountsDao.getLedgerIdGivenName(ledgerName , creditNoteVo.getOrganizationId());
						gl.setAccount(ledgerName);
						gl.setAccountId(ledgerId);
						}
					}
					gl.setSubLedger(customerDao.getCustomerName(creditNoteVo.getCustomerId()));
					gl.setFcyCredit(Double.valueOf(creditNoteVo.getTotal()));
					gl.setInrCredit(Double.valueOf(df.format(exchangeRate * Double.valueOf(creditNoteVo.getTotal()))));
					gl.setLedgerList(chartOfAccountsDao.getLedgersByEntity(invoiceVo.getOrgId(), ChartOfAccountsConstants.ENTITY_CUSTOMER));
					generalLedgers.add(gl);
				}
				
				
					
				HashMap<String , String> debitLedgerMap  = new HashMap<String, String>();
				for(ArInvoiceProductVo product : creditNoteVo.getProducts()) {
						//To separate GST ledger
					if(product.getStatus()!=null && !"DEL".equals(product.getStatus()) && product.getTaxDetails()!=null && product.getTaxDetails().getTaxDistribution()!=null) {
						for(ArInvoiceTaxDistributionVo taxdetails : product.getTaxDetails().getTaxDistribution()) {
							if(taxdetails.getTaxName()!=null && taxdetails.getTaxRate()!=null && taxdetails.getTaxAmount()!=null) {
								logger.info("To get the gst");
								String opLedger = chartOfAccountsDao.getDefaultLedger(ChartOfAccountsConstants.ENTITY_GST_RECEIVABLE_PAYABLE, ChartOfAccountsConstants.MODULE_AR, null);
								Integer opLedgerId = chartOfAccountsDao.getLedgerIdGivenName(opLedger, creditNoteVo.getOrganizationId());
								if(debitLedgerMap.containsKey(opLedger+"~"+taxdetails.getTaxName()+"~"+taxdetails.getTaxRate())) {
									String[] values= debitLedgerMap.get(opLedger+"~"+taxdetails.getTaxName()+"~"+taxdetails.getTaxRate()).split("~");
									String amnt = values[0];
									Double amount = Double.valueOf(amnt);
									amount = amount + taxdetails.getTaxAmount();
									debitLedgerMap.put(opLedger+"~"+taxdetails.getTaxName()+"~"+taxdetails.getTaxRate(), amount+"~"+values[1]);
								}else {
									debitLedgerMap.put(opLedger+"~"+taxdetails.getTaxName()+"~"+taxdetails.getTaxRate(), taxdetails.getTaxAmount()+"~"+opLedgerId);
								}
							}
						}
					}
				}
						if(debitLedgerMap.size()>0) {

							for (Map.Entry<String,String> entry : debitLedgerMap.entrySet()) {
								GeneralLedgerDetailsVo gl = new GeneralLedgerDetailsVo();
								gl.setType(JournalEntriesConstants.SUBLEDGER_TYPE_GST);
								gl.setDescription(JournalEntriesConstants.SUBLEDGER_TYPE_GST);
								if(gstMap.size()>0 && gstMap.containsKey(entry.getKey().split("~")[1])) {
									String gstVal = gstMap.get(entry.getKey().split("~")[1]);
									int id = gstVal.split("~")[0]!=null ?Integer.valueOf(gstVal.split("~")[0]) : 0;
									String mapLedName = gstVal.split("~")[1]!=null ? gstVal.split("~")[1] : null;
									String name = chartOfAccountsDao.getLedgerName(id, creditNoteVo.getOrganizationId());
									gl.setAccountId(id);
									gl.setAccount(name!=null ? name : mapLedName);
									
								}else {
									if(invoiceGstMap.size()>0 && invoiceGstMap.containsKey(entry.getKey().split("~")[1])) {
										logger.info("To set values from invoice");
										String invGstVal = invoiceGstMap.get(entry.getKey().split("~")[1]);
										gl.setAccountId(invGstVal.split("~")[0]!=null ?Integer.valueOf(invGstVal.split("~")[0]) : 0);
										gl.setAccount(invGstVal.split("~")[1]!=null ? invGstVal.split("~")[1] : null);
									}else {
										gl.setAccount(entry.getKey().split("~")[0]);
										gl.setAccountId(entry.getValue().split("~")[1]!=null ? Integer.valueOf(entry.getValue().split("~")[1]) : 0);
									}
								}
								gl.setSubLedger(entry.getKey().split("~")[1]);
								gl.setFcyDebit(entry.getValue().split("~")[0]!=null ? Double.valueOf(entry.getValue().split("~")[0]) : 0.00);
								gl.setInrDebit(Double.valueOf(df.format( exchangeRate * gl.getFcyDebit())));
									gl.setLedgerList(chartOfAccountsDao.getLedgersByEntity(creditNoteVo.getOrganizationId(), ChartOfAccountsConstants.ENTITY_GST_RECEIVABLE_PAYABLE));
								generalLedgers.add(gl);
							}
						}
				
						
						
						//To separate the ledger of TDS
						if(creditNoteVo.getTdsId()!=0 && creditNoteVo.getTdsValue()!=null && !creditNoteVo.getTdsValue().equals("0")   && !creditNoteVo.getTdsValue().equals("0.00")  && !creditNoteVo.getTdsValue().equals("0.0") && !creditNoteVo.getTdsValue().isEmpty() ) {
							logger.info("To get the TDS");
							GeneralLedgerDetailsVo gl = new GeneralLedgerDetailsVo();
							gl.setDescription(JournalEntriesConstants.TYPE_TDS);
							gl.setType(JournalEntriesConstants.TYPE_TDS);
							String tdsledger = chartOfAccountsDao.getDefaultLedger(ChartOfAccountsConstants.ENTITY_ADVANCE_TAX_AND_TDS, ChartOfAccountsConstants.MODULE_AR, null);
							Integer tdsLedgerId = chartOfAccountsDao.getLedgerIdGivenName(tdsledger, creditNoteVo.getOrganizationId());
							if(tdsMap.size()>0) {
								for(Map.Entry<Integer, String> entryset : tdsMap.entrySet()) {
									String name = chartOfAccountsDao.getLedgerName(entryset.getKey(), creditNoteVo.getOrganizationId());
									gl.setAccount(name!=null ? name : entryset.getValue());
									gl.setAccountId(entryset.getKey());
								}
								
							}else {
								if(invoiceTdsMap.size()>0) {
									logger.info("To set values from invoice");
									for(Map.Entry<Integer, String> entryset : invoiceTdsMap.entrySet()) {
										String name = chartOfAccountsDao.getLedgerName(entryset.getKey(), creditNoteVo.getOrganizationId());
										gl.setAccount(name!=null ? name : entryset.getValue());
										gl.setAccountId(entryset.getKey());
									}
								}else {
									logger.info("To set default values ");
								gl.setAccount(tdsledger);
								gl.setAccountId(tdsLedgerId);
								}
							}
							gl.setFcyCredit(Double.valueOf(creditNoteVo.getTdsValue()));
							gl.setInrCredit(Double.valueOf(exchangeRate * Double.valueOf(creditNoteVo.getTdsValue())));
							gl.setLedgerList(chartOfAccountsDao.getLedgersByEntity(creditNoteVo.getOrganizationId(), ChartOfAccountsConstants.ENTITY_ADVANCE_TAX_AND_TDS));
							generalLedgers.add(gl);
							
						}
						
						

						//To separate the ledger of Discount 
						if(creditNoteVo.getDiscountValue()!=null && !creditNoteVo.getDiscountValue().isEmpty() && !creditNoteVo.getDiscountValue().equals("0.0") && !creditNoteVo.getDiscountValue().equals("0.00") && !creditNoteVo.getDiscountValue().equals("0")) {
							String ledger = chartOfAccountsDao.getDefaultLedger(ChartOfAccountsConstants.ENTITY_DISCOUNT_GIVEN, ChartOfAccountsConstants.MODULE_AR, ChartOfAccountsConstants.FIELD_DISCOUNT);
							int ledgerId = chartOfAccountsDao.getLedgerIdGivenName(ledger , creditNoteVo.getOrganizationId());
							logger.info("To get the discount");
							GeneralLedgerDetailsVo gl = new GeneralLedgerDetailsVo();
							gl.setDescription(JournalEntriesConstants.TYPE_DISCOUNT);
							gl.setType(JournalEntriesConstants.TYPE_DISCOUNT);
							if(discountMap.size()>0) {
								for(Map.Entry<Integer, String> entryset : discountMap.entrySet()) {
									String name = chartOfAccountsDao.getLedgerName(entryset.getKey(), creditNoteVo.getOrganizationId());
									gl.setAccount(name!=null ? name : entryset.getValue());
									gl.setAccountId(entryset.getKey());
								}
							}else {
								if(invoiceDiscountMap.size()>0) {
									logger.info("To set values from invoice");
									for(Map.Entry<Integer, String> entryset : invoiceDiscountMap.entrySet()) {
										String name = chartOfAccountsDao.getLedgerName(entryset.getKey(), creditNoteVo.getOrganizationId());
										gl.setAccount(name!=null ? name : entryset.getValue());
										gl.setAccountId(entryset.getKey());
									}
								}else {
									logger.info("To set default values ");
								gl.setAccount(ledger);
								gl.setAccountId(ledgerId);
								}
							}
							gl.setFcyCredit(Double.valueOf(creditNoteVo.getDiscountValue()));
							gl.setInrCredit(Double.valueOf(df.format(exchangeRate  * Double.valueOf(creditNoteVo.getDiscountValue()))));
							gl.setLedgerList(chartOfAccountsDao.getLedgersByEntity(invoiceVo.getOrgId(), ChartOfAccountsConstants.ENTITY_DISCOUNT_GIVEN));
							generalLedgers.add(gl);
							
						}
						
						
						//To separate the ledger of Adjustment 
						if( creditNoteVo.getAdjValue()!=null && !creditNoteVo.getAdjValue().equals("") && !creditNoteVo.getAdjValue().equals("0.0") && !creditNoteVo.getAdjValue().equals("0.00")) {
							logger.info("To get the adjustment");
							String adjLedger = chartOfAccountsDao.getDefaultLedgerByMultipleEntity(Arrays.asList(ChartOfAccountsConstants.ENTITY_ROUND_OFF,ChartOfAccountsConstants.ENTITY_SALE),
									ChartOfAccountsConstants.MODULE_AR, ChartOfAccountsConstants.FIELD_ADJUSTMENTS);
							int ledgerId = chartOfAccountsDao.getLedgerIdGivenName(adjLedger , creditNoteVo.getOrganizationId());
							GeneralLedgerDetailsVo gl = new GeneralLedgerDetailsVo();
							gl.setDescription(JournalEntriesConstants.TYPE_ADJUSTMENT);
							if(adjustmentMap.size()>0) {
								for(Map.Entry<Integer, String> entryset : adjustmentMap.entrySet()) {
									String name = chartOfAccountsDao.getLedgerName(entryset.getKey(), creditNoteVo.getOrganizationId());
									gl.setAccount(name!=null ? name : entryset.getValue());
									gl.setAccountId(entryset.getKey());
								}
							}else {
								if(invoiceAdjustmentMap.size()>0) {
									logger.info("To set values from invoice");
									for(Map.Entry<Integer, String> entryset : invoiceAdjustmentMap.entrySet()) {
										String name = chartOfAccountsDao.getLedgerName(entryset.getKey(), creditNoteVo.getOrganizationId());
										gl.setAccount(name!=null ? name : entryset.getValue());
										gl.setAccountId(entryset.getKey());
									}
								}else {
									logger.info("To set default values ");
								gl.setAccount(adjLedger);
								gl.setAccountId(ledgerId);
								}
							}
							gl.setType(JournalEntriesConstants.TYPE_ADJUSTMENT);
							Double adjustment = Double.valueOf(creditNoteVo.getAdjValue());
							if(adjustment > 0) {
								gl.setFcyDebit(Math.abs(adjustment ));
								gl.setInrDebit(Double.valueOf(df.format(exchangeRate * Math.abs(adjustment ))));
							}else {
									gl.setFcyCredit(Math.abs(adjustment));
									gl.setInrCredit(Double.valueOf(df.format(exchangeRate * Math.abs(adjustment) )));
							}
							gl.setLedgerList(chartOfAccountsDao.getLedgersByMultipleEntity(invoiceVo.getOrgId(), Arrays.asList(ChartOfAccountsConstants.ENTITY_SALE , ChartOfAccountsConstants.ENTITY_ROUND_OFF)));
							generalLedgers.add(gl);
						}
						
					logger.info("Ledgers Seaparated to GeneralLedgerVo::" + generalLedgers.size());
		}
		
		}catch (ApplicationException | SQLException e) {
			logger.info("Error in getInvoiceAndGroupByLedgers:: ",e);
			throw new ApplicationException(e);
		}
		return generalLedgers;
		}
	
	
	
		public String getOrgDefaultCurrency(int orgId) {

			try {
				Connection con = getUserMgmConnection();
				BasicCurrencyVo defaultCurrency = currencyDao.getDefaultCurrencyForOrganization(orgId, con);
				closeResources(null, null, con);

				String orgCurrSymbol = defaultCurrency != null ? defaultCurrency.getAlternateSymbol() : "INR";
				return orgCurrSymbol;
			} catch (ApplicationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
			
		}
	
	
	
	
/*
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


	*/

}

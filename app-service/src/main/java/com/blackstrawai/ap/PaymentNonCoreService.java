package com.blackstrawai.ap;


import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.ap.dropdowns.PaymentNonCoreBillDetailsDropDownVo;
import com.blackstrawai.ap.dropdowns.PaymentNonCoreDropDownVo;
import com.blackstrawai.ap.dropdowns.PaymentTypeVo;
import com.blackstrawai.ap.payment.noncore.BillsPayableVo;
import com.blackstrawai.ap.payment.noncore.CreditDetailsVo;
import com.blackstrawai.ap.payment.noncore.ListPaymentNonCoreVo;
import com.blackstrawai.ap.payment.noncore.MultiLevelInvoiceVo;
import com.blackstrawai.ap.payment.noncore.PaymentAdviceVo;
import com.blackstrawai.ap.payment.noncore.PaymentInvoiceDetailsVo;
import com.blackstrawai.ap.payment.noncore.PaymentNonCoreBaseVo;
import com.blackstrawai.ap.payment.noncore.PaymentNonCoreBillAndCreditDetailsVo;
import com.blackstrawai.ap.payment.noncore.PaymentNonCoreVo;
import com.blackstrawai.ap.payment.noncore.PayrollDueVo;
import com.blackstrawai.ap.purchaseorder.PoReferenceNumberVo;
import com.blackstrawai.ar.ArBalanceUpdateDao;
import com.blackstrawai.ar.ArBalanceUpdateThread;
import com.blackstrawai.ar.ArInvoiceConstants;
import com.blackstrawai.ar.ArInvoiceDao;
import com.blackstrawai.ar.ReceiptConstants;
import com.blackstrawai.ar.ReceiptDao;
import com.blackstrawai.ar.RefundConstants;
import com.blackstrawai.ar.applycredits.InvoiceDetailsVo;
import com.blackstrawai.ar.applycredits.ReceiptsDetailsVo;
import com.blackstrawai.ar.invoice.ArInvoiceVo;
import com.blackstrawai.attachments.AttachmentService;
import com.blackstrawai.attachments.AttachmentsConstants;
import com.blackstrawai.banking.BankMasterDao;
import com.blackstrawai.chartofaccounts.ChartOfAccountsDao;
import com.blackstrawai.common.BaseService;
import com.blackstrawai.common.CommonConstants;
import com.blackstrawai.common.DropDownDao;
import com.blackstrawai.journals.JournalEntriesConstants;
import com.blackstrawai.journals.JournalEntriesThread;
import com.blackstrawai.journals.JournalEntriesTransactionDao;
import com.blackstrawai.keycontact.CustomerDao;
import com.blackstrawai.keycontact.VendorDao;
import com.blackstrawai.payroll.PayRunDao;
import com.blackstrawai.payroll.PayTypeDao;
import com.blackstrawai.payroll.payrun.PayRunEmployeeAmountVo;
import com.blackstrawai.settings.CurrencyDao;

@Service
public class PaymentNonCoreService extends BaseService {

	@Autowired
	PaymentNonCoreDao paymentDao;
	@Autowired
	DropDownDao dropDownDao;
	@Autowired
	BankMasterDao bankMasterDao;
	@Autowired
	VendorDao vendorDao;
	@Autowired
	ChartOfAccountsDao chartOfAccountsDao;
	@Autowired
	CurrencyDao currencyDao;
	@Autowired
	CustomerDao customerDao;
	@Autowired
	PayTypeDao payTypeDao;
	@Autowired
	PurchaseOrderDao purchaseOrderDao;
	@Autowired
	BillsInvoiceDao billsInvoiceDao;
	@Autowired
	ArInvoiceDao arInvoiceDao;
	@Autowired
	AttachmentService attachmentService;
	@Autowired
	NonCoreCustomerRefundDao customerRefundDao;
	@Autowired
	ArBalanceUpdateDao balanceUpdateDao;
	@Autowired
	ApBalanceUpdateDao apBalanceUpdateDao;
	@Autowired
	private PayRunDao payrunDao;
	@Autowired
	private ReceiptDao receiptDao;

	
	@Autowired
	private JournalEntriesTransactionDao journalEntriesTransactionDao;
	
	private Logger logger = Logger.getLogger(PaymentNonCoreService.class);

	public PaymentNonCoreVo createPayment(PaymentNonCoreVo paymentVo) throws ApplicationException, SQLException {
		logger.info("Entry into createPayment");
		PaymentNonCoreVo data = paymentDao.createPayment(paymentVo);
		attachmentService.upload(AttachmentsConstants.MODULE_TYPE_AP_PAYMENTS, data.getId(), data.getOrganizationId(),
				data.getAttachments());

		// To add entry in journal entry table 
		 if(data.getId()!=null && (CommonConstants.STATUS_AS_ACTIVE.equals(data.getStatus()) || CommonConstants.STATUS_AS_OPEN.equals(data.getStatus())) ) {
				logger.info("To thread");
				JournalEntriesThread journalThread =new JournalEntriesThread(journalEntriesTransactionDao, data.getId(), data.getOrganizationId(),JournalEntriesConstants.SUB_MODULE_PAYMENTS);
				journalThread.start();
			}
		updateBalances(paymentVo);
		return data;
	}

	public List<ListPaymentNonCoreVo> getAllPaymentsOfAnOrganizationForUserAndRole(int OrganizationId, String userId,
			String roleName) throws ApplicationException, SQLException {
		logger.info("Entry into getAllPaymentsOfAnOrganization");
		List<ListPaymentNonCoreVo> paymentList = paymentDao.getAllPaymentsOfAnOrganizationForUserAndRole(OrganizationId,
				userId, roleName);

		return paymentList;
	}

	public PaymentNonCoreVo updatePayment(PaymentNonCoreVo paymentVo) throws ApplicationException, SQLException {
		logger.info("Entry into method:updatePayment");
		PaymentNonCoreVo existingPaymentVo=null;
		if(paymentVo!=null && paymentVo.getId()!=null) {
			existingPaymentVo=paymentDao.getPaymentById(paymentVo.getId());
		}
		PaymentNonCoreVo data = paymentDao.updatePayment(paymentVo);
		attachmentService.upload(AttachmentsConstants.MODULE_TYPE_AP_PAYMENTS, data.getId(), data.getOrganizationId(),
				data.getAttachments());
		updateBalances(existingPaymentVo);//Update pevious payment invoice to reverse the trasaction if needed
		updateBalances(paymentVo);
		// To add entry in journal entry table 
				 if(data.getId()!=null && (CommonConstants.STATUS_AS_ACTIVE.equals(data.getStatus()) || CommonConstants.STATUS_AS_OPEN.equals(data.getStatus())) ) {
						logger.info("To thread");
						JournalEntriesThread journalThread =new JournalEntriesThread(journalEntriesTransactionDao, data.getId(), data.getOrganizationId(),JournalEntriesConstants.SUB_MODULE_PAYMENTS);
						journalThread.start();
					}
				 
		return data;
	}

	private void updateBalances(PaymentNonCoreVo paymentVo) throws SQLException, ApplicationException {
		if (paymentVo != null && paymentVo.getStatus() != null	) {
			if (paymentVo.getPaymentType() > 0 && paymentVo.getPayments() != null && !paymentVo.getPayments().isEmpty()) {
				String paymentType=paymentDao.getPaymentTypeById(paymentVo.getPaymentType());
				logger.info("Payment Type:"+paymentType);
				switch (paymentType) {
				case PaymentNonCoreConstants.CUSTOMER_REFUNDS:
					if(paymentVo.getOrganizationId()>0 && paymentVo.getPaidTo()!=null&& paymentVo.getCurrency()>0 && paymentVo.getStatus().equalsIgnoreCase(CommonConstants.STATUS_AS_ACTIVE)) {
						//Update Invoices of Customer & Currency
						List<InvoiceDetailsVo> invoicesList=arInvoiceDao.getAllInvoicesOfCustomerCurrency(paymentVo.getOrganizationId(),Integer.parseInt(paymentVo.getPaidTo()), paymentVo.getCurrency());
						for(InvoiceDetailsVo invoiceDetailsVo:invoicesList) {
							if(invoiceDetailsVo!=null && invoiceDetailsVo.getId()!=null)
								new ArBalanceUpdateThread(balanceUpdateDao, invoiceDetailsVo.getId(),ArInvoiceConstants.MODULE_TYPE_AR_INVOICES).start();

						}
						List<ReceiptsDetailsVo> receipts=receiptDao.getReceiptsByCustomerId(paymentVo.getOrganizationId(), Integer.parseInt(paymentVo.getPaidTo()), paymentVo.getCurrency());
						for(ReceiptsDetailsVo receipt:receipts) {//If there are only advances
							new ArBalanceUpdateThread(balanceUpdateDao, receipt.getId(),ArInvoiceConstants.MODULE_TYPE_AR_RECEIPT).start();
						}
						
					}
					break;

				case PaymentNonCoreConstants.BILL_PAYMENTS:
					if(paymentVo.getVendor()>0 && paymentVo.getCurrency()>0 && paymentVo.getStatus().equalsIgnoreCase(CommonConstants.STATUS_AS_ACTIVE)) {
						List<Integer> invoices=billsInvoiceDao.getInvoiceIdsForVendor(paymentVo.getVendor(),paymentVo.getCurrency());
						if(invoices!=null && !invoices.isEmpty()) {
							for(Integer invoiceId:invoices) {
								new ApBalanceUpdateThread(apBalanceUpdateDao, invoiceId, JournalEntriesConstants.SUB_MODULE_INVOICE_WITH_BILLS).start();
							}
						}
						List<Integer> vednorAdvances=paymentDao.getVendorAdvancesByVendorCurrency(paymentVo.getVendor(), paymentVo.getCurrency());
						if(vednorAdvances!=null && !vednorAdvances.isEmpty()) {
							for(Integer vednorAdvanceId:vednorAdvances) {
								new ApBalanceUpdateThread(apBalanceUpdateDao, vednorAdvanceId, PaymentNonCoreConstants.VENDOR_ADVANCE).start();
							}
						}
					}
					break;
				case PaymentNonCoreConstants.VENDOR_ADVANCE:
					List<Integer> vednorAdvances=paymentDao.getVendorAdvancesByVendorCurrency(paymentVo.getVendor(), paymentVo.getCurrency());
					if(vednorAdvances!=null && !vednorAdvances.isEmpty() ) {
						for(Integer vednorAdvanceId:vednorAdvances) {
							new ApBalanceUpdateThread(apBalanceUpdateDao, vednorAdvanceId, PaymentNonCoreConstants.VENDOR_ADVANCE).start();
						}
					}
					break;
				case PaymentNonCoreConstants.EMPLOYEE_PAYMENTS:
					if( paymentVo.getStatus().equalsIgnoreCase(CommonConstants.STATUS_AS_ACTIVE)) {
					for(PaymentNonCoreBaseVo vo:paymentVo.getPayments()) {
							new ApBalanceUpdateThread(apBalanceUpdateDao, vo.getPayRun(), JournalEntriesConstants.SUB_MODULE_PAY_RUN).start();
					}
					}
					break;
					
				}
				
				
				
				}
			
		}
	}

	public PaymentNonCoreVo getPaymentById(int organizationId, String userId, String roleName, int id)
			throws ApplicationException {
		logger.info("Entry into method:getPaymentById");
		PaymentNonCoreVo data = paymentDao.getPaymentById(id);
		if (data.getAttachments() != null)
			attachmentService.encodeAllFiles(data.getOrganizationId(), data.getId(),
					AttachmentsConstants.MODULE_TYPE_AP_PAYMENTS, data.getAttachments());
		return data;
	}

	public List<PoReferenceNumberVo> getVendorReferenceNumber(int organizationId, int id) throws ApplicationException {
		logger.info("Entry into method: getVendorReferenceNumber");
		return purchaseOrderDao.getPOReferenceNumber(organizationId, id);
	}

	public PaymentNonCoreDropDownVo getPaymentDropdownData(int organizationId)
			throws ApplicationException, SQLException {

		logger.info("Entry into method: getPaymentDropdownData");
		PaymentNonCoreDropDownVo paymentDropdownVo = dropDownDao.getPaymentNonCoreDropDownData(organizationId);

		return paymentDropdownVo;

	}

	public PaymentNonCoreBillAndCreditDetailsVo getBillReferenceNumber(int organizationId, int vendorId, int currencyId, int paymentId)
			throws ApplicationException {
		logger.info("Entry into method: getBillReferenceNumber");
		PaymentNonCoreBillAndCreditDetailsVo billCreditDetails = new PaymentNonCoreBillAndCreditDetailsVo();
		List<PaymentNonCoreBillDetailsDropDownVo> invoicesDropdown = billsInvoiceDao.getBillReferenceNumberByOrgVendor(organizationId, vendorId, currencyId, paymentId);
		
		Collections.sort(invoicesDropdown, new Comparator<PaymentNonCoreBillDetailsDropDownVo>() {
			  public int compare(PaymentNonCoreBillDetailsDropDownVo o1, PaymentNonCoreBillDetailsDropDownVo o2) {
			      if (o1.getDueDate() == null || o2.getDueDate() == null)
			        return 0;
			      return o1.getDueDate().compareTo(o2.getDueDate());
			  }
			});
		
		List<CreditDetailsVo> creditDetails = paymentDao.getCreditDetailsDropDown(organizationId, vendorId, currencyId, paymentId);
		billCreditDetails.setBillDetails(invoicesDropdown);
		billCreditDetails.setCreditDetails(creditDetails);
		double advanceBalance = 0;
		for (CreditDetailsVo credit : creditDetails) {
			if (credit.getAvailableAmt() != null)
				advanceBalance += Double.parseDouble(credit.getAvailableAmt());
		}
		double billBalance = 0;
		for (PaymentNonCoreBillDetailsDropDownVo bill : invoicesDropdown) {
			if (bill.getBalanceDue() != null) {
				billBalance += bill.getBalanceDue();			}
		}
		DecimalFormat df = new DecimalFormat("#.00");
		billCreditDetails.setAdvanceBalance(df.format(advanceBalance));
		billCreditDetails.setBillBalance(df.format(billBalance));
		return billCreditDetails;
	}

	public List<MultiLevelInvoiceVo> getInvoiceDetails(int organizationId, int customerId, int currencyId,
			int invoiceId, int paymentId) throws ApplicationException {
		List<MultiLevelInvoiceVo> result = new ArrayList<MultiLevelInvoiceVo>();
		MultiLevelInvoiceVo receiptResult = new MultiLevelInvoiceVo();
		receiptResult.setName(RefundConstants.REFUND_TYPE_RECEIPT);
		List<PaymentInvoiceDetailsVo> receipts = new ArrayList<PaymentInvoiceDetailsVo>();
		ArInvoiceVo invoiceVo = arInvoiceDao.getInvoiceById(invoiceId);
		if (invoiceVo != null && invoiceVo.getGeneralInformation() != null)
			receipts.addAll(customerRefundDao.getReceiptByInvoiceId(organizationId, customerId, currencyId,invoiceId, 
				invoiceVo.getGeneralInformation().getInvoiceNoPrefix() + "/" + invoiceVo.getGeneralInformation().getInvoiceNumber() + "/" 
						+ invoiceVo.getGeneralInformation().getInvoiceNoSuffix(), paymentId));
		else 
			receipts.addAll(customerRefundDao.getReceiptByInvoiceId(organizationId, customerId, currencyId,invoiceId, null, paymentId));

		receiptResult.setChild(receipts);
		result.add(receiptResult);

		MultiLevelInvoiceVo creditNoteResult = new MultiLevelInvoiceVo();
		creditNoteResult.setName(RefundConstants.REFUND_TYPE_CREDIT_NOTES);
		List<PaymentInvoiceDetailsVo> creditNotes = new ArrayList<PaymentInvoiceDetailsVo>();
		if (invoiceVo != null && invoiceVo.getGeneralInformation() != null)
			creditNotes.addAll(customerRefundDao.getCreditNoteByInvoiceId(invoiceId, 
				invoiceVo.getGeneralInformation().getInvoiceNoPrefix() + "/" + invoiceVo.getGeneralInformation().getInvoiceNumber() + "/" 
						+ invoiceVo.getGeneralInformation().getInvoiceNoSuffix(), paymentId));
		else
			creditNotes.addAll(customerRefundDao.getCreditNoteByInvoiceId(invoiceId, null, paymentId));

		creditNoteResult.setChild(creditNotes);
		result.add(creditNoteResult);

		return result;
	}

	public List<ListPaymentNonCoreVo> getAllPaymentsOfAnOrganizationForUserAndRole(int organizationId, String userId,
			String roleName, String type) throws ApplicationException {
		logger.info("Entry into method: getAllPaymentsOfAnOrganizationForUserAndRole");
		return paymentDao.getTop5PaymentsOfAnOrganizationForUserRoleType(organizationId, userId, roleName, paymentDao.getPaymentTypeId(type));
	}



	public List<InvoiceDetailsVo> getInvoiceList(int organizationId, int customerId, int currencyId, int paymentId)
			throws ApplicationException {
		logger.info("Entry into method : getInvoiceList");
		List<InvoiceDetailsVo> invoiceList = customerRefundDao.getInvoiceDetailsByCustomerCurrencyId(organizationId, customerId, currencyId, paymentId);
		InvoiceDetailsVo onAccount = new InvoiceDetailsVo();
		onAccount.setId(ReceiptConstants.AR_RECEIPT_ON_ACCOUNT_REFERENCE);
		onAccount.setValue(ReceiptConstants.AR_RECEIPT_ON_ACCOUNT_REFERENCE);
		onAccount.setName(ReceiptConstants.AR_RECEIPT_TYPE_ON_ACCOUNT);
		InvoiceDetailsVo advancePayment = new InvoiceDetailsVo();
		advancePayment.setId(ReceiptConstants.AR_RECEIPT_ADVANCE_PAYMENT_REFERENCE);
		advancePayment.setValue(ReceiptConstants.AR_RECEIPT_ADVANCE_PAYMENT_REFERENCE);
		advancePayment.setName(ReceiptConstants.AR_RECEIPT_TYPE_ADVANCE_PAYMENT);
		invoiceList.add(onAccount);
		invoiceList.add(advancePayment);
		return invoiceList;
	}

	public List<MultiLevelInvoiceVo> getOnAccount(int organizationId, int customerId, int currencyId, int paymentId) throws ApplicationException {
		logger.info("Entry into method : getOnAccount");
		List<MultiLevelInvoiceVo> result = new ArrayList<MultiLevelInvoiceVo>();
		MultiLevelInvoiceVo onAccountResult = new MultiLevelInvoiceVo();
		onAccountResult.setName(ReceiptConstants.AR_RECEIPT_TYPE_ON_ACCOUNT);

		List<PaymentInvoiceDetailsVo> onAccounts = new ArrayList<PaymentInvoiceDetailsVo>();

		onAccounts.addAll(customerRefundDao.getOnAccountOrAdvancePaymentReceipts(organizationId, customerId, currencyId, paymentId,ReceiptConstants.AR_RECEIPT_TYPE_ON_ACCOUNT));

		onAccountResult.setChild(onAccounts);
		result.add(onAccountResult);
		return result;
	}
	
	public List<MultiLevelInvoiceVo> getAdvancePayments(int organizationId, int customerId, int currencyId, int paymentId) throws ApplicationException {
		logger.info("Entry into method : getAdvancePayments");
		List<MultiLevelInvoiceVo> result = new ArrayList<MultiLevelInvoiceVo>();
		MultiLevelInvoiceVo advancePaymentResult = new MultiLevelInvoiceVo();
		advancePaymentResult.setName(ReceiptConstants.AR_RECEIPT_TYPE_ADVANCE_PAYMENT);
		List<PaymentInvoiceDetailsVo> advancePayments = new ArrayList<PaymentInvoiceDetailsVo>();

		advancePayments.addAll(customerRefundDao.getOnAccountOrAdvancePaymentReceipts(organizationId, customerId, currencyId, paymentId,ReceiptConstants.AR_RECEIPT_TYPE_ADVANCE_PAYMENT));

		advancePaymentResult.setChild(advancePayments);
		result.add(advancePaymentResult);
		return result;
	}

	public List<PayRunEmployeeAmountVo> getPayRunByEmployeeId(int organizationId, int employeeId) throws ApplicationException {
		logger.info("Entry into method : getPayRunByEmployeeId");
		return payrunDao.getPayRunDataByOrganizationEmployeeId(organizationId, employeeId);
	}

	public List<PayRunEmployeeAmountVo> getEmployeesByPayrunId(int organizationId, int payrunId) throws ApplicationException {
		logger.info("Entry into method : getPayRunByEmployeeId");
		return payrunDao.getEmployeesByPayrunId(organizationId, payrunId);
	}

	public BillsPayableVo getBillsPayable(int organizationId, String userId, String roleName) throws ApplicationException {
		return billsInvoiceDao.getBillsPayable(organizationId, userId, roleName);
	}

	public PayrollDueVo getPayrollDue(int organizationId, String userId, String roleName) throws ApplicationException {
		return payrunDao.getPayrollPayable(organizationId, userId, roleName);
	}

	public List<ListPaymentNonCoreVo> getTop5PaymentsOfAnOrganizationForUserRoleType(int organizationId, String userId,
			String roleName, int type) throws ApplicationException {
		return paymentDao.getTop5PaymentsOfAnOrganizationForUserRoleType(organizationId, userId, roleName, type);
	}

	public List<PaymentTypeVo> getPaymentTypes() throws SQLException, ApplicationException {
		List<PaymentTypeVo> types = paymentDao.getPaymentTypes();
		types.add(new PaymentTypeVo(0, "All", 0));
		return types;
	}

	public PaymentAdviceVo getPaymentAdvice(int organizationId, String userId, String roleName, int id) throws ApplicationException{
		return paymentDao.getPaymentAdvice(organizationId, userId, roleName, id);
	}


}

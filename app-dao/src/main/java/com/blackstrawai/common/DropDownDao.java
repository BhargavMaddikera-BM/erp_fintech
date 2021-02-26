package com.blackstrawai.common;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.DateConverter;
import com.blackstrawai.accounting.dropdowns.AccountingAspectsDropDownVo;
import com.blackstrawai.accounting.dropdowns.LedgerDropDownVo;
import com.blackstrawai.ap.BillsInvoiceDao;
import com.blackstrawai.ap.PaymentNonCoreConstants;
import com.blackstrawai.ap.PaymentNonCoreDao;
import com.blackstrawai.ap.PurchaseOrderDao;
import com.blackstrawai.ap.dropdowns.BalanceConfirmationDropDownVo;
import com.blackstrawai.ap.dropdowns.BasicContactVo;
import com.blackstrawai.ap.dropdowns.BasicCurrencyVo;
import com.blackstrawai.ap.dropdowns.BasicEmployeeVo;
import com.blackstrawai.ap.dropdowns.BasicLocationVo;
import com.blackstrawai.ap.dropdowns.BasicVendorDetailsVo;
import com.blackstrawai.ap.dropdowns.BasicVoucherEntriesVo;
import com.blackstrawai.ap.dropdowns.InvoiceDetailsDropDownVo;
import com.blackstrawai.ap.dropdowns.InvoiceDropDownVo;
import com.blackstrawai.ap.dropdowns.PaymentModeDropDownVo;
import com.blackstrawai.ap.dropdowns.PaymentNonCoreColumnDropDownVo;
import com.blackstrawai.ap.dropdowns.PaymentNonCoreCustomFieldVo;
import com.blackstrawai.ap.dropdowns.PaymentNonCoreCustomTableVo;
import com.blackstrawai.ap.dropdowns.PaymentNonCoreDropDownVo;
import com.blackstrawai.ap.dropdowns.PaymentTypeVo;
import com.blackstrawai.ap.dropdowns.PurchaseOrderDropDownVo;
import com.blackstrawai.ap.dropdowns.VendorCreditNoteDropDownVo;
import com.blackstrawai.ap.payment.noncore.MultiLevelContactVo;
import com.blackstrawai.ap.purchaseorder.PoFilterVo;
import com.blackstrawai.ap.purchaseorder.PoListVo;
import com.blackstrawai.ar.ArInvoiceDao;
import com.blackstrawai.ar.CreditNotesDao;
import com.blackstrawai.ar.LetterOfUndertakingDao;
import com.blackstrawai.ar.ReceiptConstants;
import com.blackstrawai.ar.ReceiptDao;
import com.blackstrawai.ar.RefundDao;
import com.blackstrawai.ar.applycredits.CustomerBasicDetailsVo;
import com.blackstrawai.ar.applycredits.ReceiptsDetailsVo;
import com.blackstrawai.ar.dropdowns.ApplyCreditsCustomerDropDownVo;
import com.blackstrawai.ar.dropdowns.ApplyCreditsDropDownVo;
import com.blackstrawai.ar.dropdowns.ArInvoiceCategoryColumnDropDownVo;
import com.blackstrawai.ar.dropdowns.ArInvoicesDropDownVo;
import com.blackstrawai.ar.dropdowns.BasicInvoiceDetailsVo;
import com.blackstrawai.ar.dropdowns.BasicInvoicesDropdownVo;
import com.blackstrawai.ar.dropdowns.CreditNoteDropdownVo;
import com.blackstrawai.ar.dropdowns.CustomerAccountsDropdownVo;
import com.blackstrawai.ar.dropdowns.LetterOfUndertakingDropDownVo;
import com.blackstrawai.ar.dropdowns.PaymentModeVo;
import com.blackstrawai.ar.dropdowns.ReceiptBulkDropdownVo;
import com.blackstrawai.ar.dropdowns.ReceiptDropdownVo;
import com.blackstrawai.ar.dropdowns.RefundDropdownVo;
import com.blackstrawai.ar.invoice.ArInvoiceVo;
import com.blackstrawai.ar.lut.FinYearExpiryDateVo;
import com.blackstrawai.ar.receipt.ReceiptCommonVo;
import com.blackstrawai.ar.receipt.ReceiptSettingsVo;
import com.blackstrawai.ar.receipt.ReceiptVo;
import com.blackstrawai.banking.BankMasterDao;
import com.blackstrawai.banking.dashboard.BankMasterAccountBaseVo;
import com.blackstrawai.banking.dashboard.BasicBankMasterCardAccountVo;
import com.blackstrawai.banking.dashboard.BasicBankMasterWalletVo;
import com.blackstrawai.banking.dashboard.BasicBankingVo;
import com.blackstrawai.banking.dropdowns.BankMasterDropDownVo;
import com.blackstrawai.banking.dropdowns.ContraDropDownVo;
import com.blackstrawai.chartofaccounts.ChartOfAccountsConstants;
import com.blackstrawai.chartofaccounts.ChartOfAccountsDao;
import com.blackstrawai.externalintegration.banking.yesbank.YesBankConstants;
import com.blackstrawai.externalintegration.yesbank.beneficiary.AccountTypeVo;
import com.blackstrawai.externalintegration.yesbank.beneficiary.BeneficiaryTypeVo;
import com.blackstrawai.externalintegration.yesbank.dropdowns.BeneficiaryDropDownVo;
import com.blackstrawai.keycontact.CustomerDao;
import com.blackstrawai.keycontact.EmployeeDao;
import com.blackstrawai.keycontact.StatutoryBodyDao;
import com.blackstrawai.keycontact.VendorDao;
import com.blackstrawai.keycontact.dropdowns.BasicVendorInvoiceDropDownVo;
import com.blackstrawai.keycontact.dropdowns.CustomerDropdownVo;
import com.blackstrawai.keycontact.dropdowns.EmployeeDropDownVo;
import com.blackstrawai.keycontact.dropdowns.StatutoryBodyDropDownVo;
import com.blackstrawai.keycontact.dropdowns.UserDropDownVo;
import com.blackstrawai.keycontact.dropdowns.VendorDropDownVo;
import com.blackstrawai.keycontact.statutorybody.StatutoryBodyVo;
import com.blackstrawai.onboarding.OrganizationDao;
import com.blackstrawai.onboarding.RoleDao;
import com.blackstrawai.onboarding.UserDao;
import com.blackstrawai.onboarding.organization.BasicGSTLocationDetailsVo;
import com.blackstrawai.onboarding.organization.BasicLocationDetailsVo;
import com.blackstrawai.payroll.BasicPayTypeVo;
import com.blackstrawai.payroll.PayPeriodDao;
import com.blackstrawai.payroll.PayRunDao;
import com.blackstrawai.payroll.PayTypeDao;
import com.blackstrawai.payroll.dropdowns.PayItemDropDownVo;
import com.blackstrawai.payroll.dropdowns.PayPeriodDropDownVo;
import com.blackstrawai.payroll.dropdowns.PayRunEmployeeDropDownVo;
import com.blackstrawai.payroll.dropdowns.PayRunPaymentCycleDropDownVo;
import com.blackstrawai.payroll.dropdowns.PayRunPaymentTypeDropDownVo;
import com.blackstrawai.payroll.dropdowns.PayRunPeriodDropDownVo;
import com.blackstrawai.payroll.dropdowns.PayRunTableDropDownVo;
import com.blackstrawai.payroll.dropdowns.PayTypeDropDownVo;
import com.blackstrawai.report.dropdowns.ReportsPeriodDropDownVo;
import com.blackstrawai.settings.BaseCurrencyVo;
import com.blackstrawai.settings.CommonVo;
import com.blackstrawai.settings.CurrencyDao;
import com.blackstrawai.settings.CurrencyVo;
import com.blackstrawai.settings.CustomerGroupDao;
import com.blackstrawai.settings.DepartmentDao;
import com.blackstrawai.settings.PaymentTermsDao;
import com.blackstrawai.settings.ProductCategoryVo;
import com.blackstrawai.settings.ProductDao;
import com.blackstrawai.settings.ProductDropDownVo;
import com.blackstrawai.settings.SettingsAndPreferencesConstants;
import com.blackstrawai.settings.ShippingPreferenceDao;
import com.blackstrawai.settings.TaxGroupDao;
import com.blackstrawai.settings.TaxRateTypeVo;
import com.blackstrawai.settings.TdsDao;
import com.blackstrawai.settings.VendorGroupDao;
import com.blackstrawai.settings.VoucherDropDownVo;
import com.blackstrawai.settings.chartofaccounts.MinimalChartOfAccountsVo;
import com.blackstrawai.upload.ModuleTypeVo;
import com.blackstrawai.upload.dropdowns.ModuleTypeDropDownVo;
import com.blackstrawai.vendorsettings.VendorSettingsDao;
import com.blackstrawai.vendorsettings.dropdowns.VamDropDownVo;
import com.blackstrawai.workflow.WorkflowConstants;
import com.blackstrawai.workflow.WorkflowRuleConditionVo;
import com.blackstrawai.workflow.WorkflowSettingsDao;
import com.blackstrawai.workflow.dropdowns.WorkflowSettingsDropDownVo;

@Repository
public class DropDownDao extends BaseDao {

	@Autowired
	private FinanceCommonDao financeCommonDao;

	@Autowired
	private TaxGroupDao taxGroupDao;

	@Autowired
	private OrganizationDao organizationDao;

	@Autowired
	private UserDao userDao;

	@Autowired
	private RoleDao roleDao;

	@Autowired
	private BillsInvoiceDao billsInvoiceDao;

	/*
	 * @Autowired private ExpenseDao expenseDao;
	 */

	@Autowired
	private ChartOfAccountsDao chartOfAccountsDao;

	@Autowired
	private VendorDao vendorDao;

	@Autowired
	private CustomerDao customerDao;

	@Autowired
	private ProductDao productDao;

	@Autowired
	private EmployeeDao employeeDao;

	@Autowired
	private DepartmentDao departmentDao;

	@Autowired
	private CustomerGroupDao customerGroupDao;

	@Autowired
	private VendorGroupDao vendorGroupDao;

	@Autowired
	private CurrencyDao currencyDao;

	@Autowired
	private PaymentTermsDao paymentTermsDao;

	@Autowired
	private ShippingPreferenceDao shippingPreferenceDao;

	@Autowired
	private PayTypeDao payTypeDao;

	@Autowired
	private VendorSettingsDao vendorSettingsDao;

	@Autowired
	private LetterOfUndertakingDao letterOfUndertakingDao;

	@Autowired
	private BankMasterDao bankMasterDao;

	@Autowired
	private ArInvoiceDao arInvoiceDao;

	@Autowired
	private ReceiptDao receiptDao;

	@Autowired
	private RefundDao refundDao;

	@Autowired
	private PaymentNonCoreDao paymentNonCoreDao;

	@Autowired
	private PurchaseOrderDao purchaseOrderDao;

	@Autowired
	CreditNotesDao creditNotesDao;

	@Autowired
	StatutoryBodyDao statutoryBodyDao;

	@Autowired
	WorkflowSettingsDao workflowSettingsDao;

	@Autowired
	PayPeriodDao payPeriodDao;

	@Autowired
	PayRunDao payRunDao;
	
	@Autowired
	private TdsDao tdsDao;

	private Logger logger = Logger.getLogger(DropDownDao.class);

	public VendorDropDownVo getVendorDropDownData(int organizationId) throws ApplicationException {
		logger.info("Entry into method:getVendorDropDownData");
		Connection con = null;
		VendorDropDownVo data = new VendorDropDownVo();

		try {

			con = getFinanceCommon();
			data.setGstTreatment(financeCommonDao.gstTreatment(con));
			data.setSourceOfSupply(financeCommonDao.sourceOfSupply(con));
			data.setOrganizationConstitution(financeCommonDao.getorganizationConstitution(con));
			data.setCountry(financeCommonDao.getCountryAndStateList(con));
			data.setTds(tdsDao.getAllTdsOfAnOrganizationForUserAndRole(organizationId, "0", "Super Admin"));

			data.setDocumentTypes(financeCommonDao.getDocumentTypes(con));
			closeResources(null, null, con);

			con = getUserMgmConnection();
			data.setPaymentTerms(paymentTermsDao.getBasicPaymentTerms(organizationId, con));
			data.setCurrency(currencyDao.getMinimalCurrencyDetails(organizationId, con));
			data.setVendorGroup(vendorGroupDao.getBasicVendorGroup(organizationId, con));
			data.setLocationGst(organizationDao.getLocationGSTDetailsWithAddress(con, organizationId));
			data
			.setLedger(chartOfAccountsDao.getLedgersByEntity(organizationId, ChartOfAccountsConstants.ENTITY_VENDOR));
			String ledgerName = chartOfAccountsDao.getDefaultLedger(ChartOfAccountsConstants.ENTITY_VENDOR, null, null);
			int ledgerId = chartOfAccountsDao.getLedgerIdGivenName(ledgerName , organizationId);
			data.setDefaultGlLedgerId(ledgerId);
			closeResources(null, null, con);

		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(null, null, con);

		}
		return data;
	}
	

	public UserDropDownVo getUserDropDownData() throws ApplicationException {
		logger.info("Entry into method:getUserDropDownData");
		Connection con = null;
		UserDropDownVo data = new UserDropDownVo();

		try {
			con = getFinanceCommon();
			data.setCountry(financeCommonDao.getCountryAndStateList(con));
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(null, null, con);

		}
		return data;
	}
	
	public StatutoryBodyDropDownVo getStatutoryBodyDropDownData(int organizationId) throws ApplicationException {
		logger.info("Entry into method:getStatutoryBodyDropDownData");
		Connection con = null;
		StatutoryBodyDropDownVo data = new StatutoryBodyDropDownVo();

		try {
			con = getFinanceCommon();
			data.setCountry(financeCommonDao.getCountryAndStateList(con));
			closeResources(null, null, con);
			
			List<MinimalChartOfAccountsVo> chartOfAccountsData=new ArrayList<MinimalChartOfAccountsVo>();
			chartOfAccountsData.addAll(chartOfAccountsDao.getLedgersByEntity(organizationId, "GST Receivable/Payable"));
			chartOfAccountsData.addAll(chartOfAccountsDao.getLedgersByEntity(organizationId, "PF Liability"));
			chartOfAccountsData.addAll(chartOfAccountsDao.getLedgersByEntity(organizationId, "Advance Tax & TDS"));
			chartOfAccountsData.addAll(chartOfAccountsDao.getLedgersByEntity(organizationId, "TDS Payable"));
			chartOfAccountsData.addAll(chartOfAccountsDao.getLedgersByEntity(organizationId, "ESIC Liability"));
			chartOfAccountsData.addAll(chartOfAccountsDao.getLedgersByEntity(organizationId, "P-Tax Liability"));						
			data.setChartOfAccounts(chartOfAccountsData);
			
			
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(null, null, con);

		}
		return data;
	}

	public EmployeeDropDownVo getEmployeeDropDownData(int organizationId) throws ApplicationException {
		logger.info("Entry into method:getEmployeeDropDownData");
		Connection con = null;
		EmployeeDropDownVo data = new EmployeeDropDownVo();
		try {

			con = getAccountsPayable();
			data.setEmployee(employeeDao.getBasicEmployees(organizationId, con));
			closeResources(null, null, con);

			con = getUserMgmConnection();
			data.setDepartment(departmentDao.getBasicDepartment(organizationId, con));
			closeResources(null, null, con);

			con = getFinanceCommon();
			data.setEmployeeType(financeCommonDao.getEmployeeType(con));
			closeResources(null, null, con);
			
			data.setLedger(chartOfAccountsDao.getLedgersByEntity(organizationId, ChartOfAccountsConstants.ENTITY_EMPLOYEE_PAYABLE));

		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(null, null, con);

		}
		return data;
	}

	public CustomerDropdownVo getCustomerDropDownData(int organizationId) throws ApplicationException {
		logger.info("Entry into method:getCustomerDropDownData");
		Connection con = null;
		CustomerDropdownVo data = new CustomerDropdownVo();

		try {

			con = getFinanceCommon();
			data.setGstTreatment(financeCommonDao.gstTreatment(con));
			data.setOrganizationConstitution(financeCommonDao.getorganizationConstitution(con));
			data.setCountry(financeCommonDao.getCountryAndStateList(con));
			data.setTds(financeCommonDao.getTDS(con));
			closeResources(null, null, con);

			con = getUserMgmConnection();
			data.setCustomerGroup(customerGroupDao.getBasicCustomerGroup(organizationId, con));
			data.setPaymentTerms(paymentTermsDao.getBasicPaymentTerms(organizationId, con));
			data.setCurrency(currencyDao.getMinimalCurrencyDetails(organizationId, con));
			data.setLocationGst(organizationDao.getLocationGSTDetailsWithAddress(con, organizationId));
			closeResources(null, null, con);
			data
			.setLedger(chartOfAccountsDao.getLedgersByEntity(organizationId, ChartOfAccountsConstants.ENTITY_CUSTOMER));
			String ledgerName = chartOfAccountsDao.getDefaultLedger(ChartOfAccountsConstants.ENTITY_CUSTOMER, null, null);
			int ledgerId = chartOfAccountsDao.getLedgerIdGivenName(ledgerName , organizationId);
			data.setDefaultGlLedgerId(ledgerId);
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(null, null, con);

		}
		return data;
	}

	/*
	 * public ExpenseDropdownVo getExpenseDropDownData(int organizationId) throws
	 * ApplicationException {
	 * logger.info("Entry into method:getExpenseDropDownData"); Connection con =
	 * null; ExpenseDropdownVo data = new ExpenseDropdownVo();
	 * 
	 * try { con = getUserMgmConnection(); data.setChartOfAccounts(
	 * chartOfAccountsDao.getChartOfAccountsForGivenLevel2Type("Expenses",
	 * organizationId, con));
	 * data.setDateFormat(organizationDao.getDefaultDateForOrganization(
	 * organizationId, con)); closeResources(null, null, con);
	 * data.setNatureOfSpendingVOList(expenseDao.getAllNatureOfSpending());
	 * data.setStatusVOList(expenseDao.getAllExpenseStatus()); con =
	 * getAccountsPayable();
	 * data.setEmployee(employeeDao.getBasicEmployees(organizationId, con));
	 * data.setBasicVendorVoList(vendorDao.getBasicVendor(organizationId, con));
	 * closeResources(null, null, con); con = getAccountsReceivableConnection();
	 * data.setBasicCustomerVoList(customerDao.getBasicCustomer(organizationId,
	 * con)); closeResources(null, null, con);
	 * 
	 * } catch (Exception e) { throw new ApplicationException(e); } finally {
	 * closeResources(null, null, con);
	 * 
	 * } return data; }
	 */
	public ProductDropDownVo getProductDropDownData(int organizationId,String type) throws ApplicationException {
		logger.info("Entry into method:getProductDropDownData");
		Connection con = null;
		ProductDropDownVo data = new ProductDropDownVo();

		try {

			con = getFinanceCommon();
			data.setUnitOfMeasure(financeCommonDao.getUnitOfMeasure(con));
			closeResources(null, null, con);

			con = getUserMgmConnection();
			data.setInter(taxGroupDao.getTaxGroupForProduct(organizationId, true));
			data.setIntra(taxGroupDao.getTaxGroupForProduct(organizationId, false));
			data.setPurchaseAccount(chartOfAccountsDao.getItemsPurchaseAccounts(type , organizationId));
			data.setSalesAccount(chartOfAccountsDao.getItemsSalesAccounts(type , organizationId));
			data.setCategory(getProductCategoryByType(type,organizationId));
			data.setCurrency(currencyDao.getMinimalCurrencyDetails(organizationId, con));
			data.setDefaultCurrency(currencyDao.getBasicCurrencyForOrganization(organizationId, con));
			closeResources(null, null, con);

		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(null, null, con);

		}
		return data;
	}

	public InvoiceDropDownVo getInvoiceDropDownData(int organizationId) throws ApplicationException {
		logger.info("Entry into method:getInvoiceDropDownData");
		Connection con = null;
		InvoiceDropDownVo data = new InvoiceDropDownVo();

		try {

			con = getFinanceCommon();
			data.setTaxApplicationMethod(financeCommonDao.getItemTaxRate(con));
			data.setDiscountTypes(financeCommonDao.getDiscountType(con));
			data.setTds(tdsDao.getAllTdsOfAnOrganizationForUserAndRole(organizationId, "0", "Super Admin"));
			data.setDestinationOfSupply(financeCommonDao.sourceOfSupply(con));
			data.setSourceOfSupply(financeCommonDao.sourceOfSupply(con));
			data.setMeasures(financeCommonDao.getBasicUnitOfMeasure(con));
			closeResources(null, null, con);

			con = getUserMgmConnection();
			data.setTaxGroups(taxGroupDao.getTaxGroups(organizationId, con));
			data.setCurrency(currencyDao.getMinimalCurrencyDetails(organizationId, con));
			data.setPaymentTerms(paymentTermsDao.getBasicPaymentTermsWithCustomValue(organizationId, con));
			data.setDateFormat(organizationDao.getDefaultDateForOrganization(organizationId, con));
			data.setProducts(productDao.getBasicProductWithTaxGroups(organizationId, con));
			data.setDefaultCurrency(currencyDao.getBasicCurrencyForOrganization(organizationId, con));
			data.setChartOfAccounts(chartOfAccountsDao.getChartOfAccountsValues(organizationId, con));
			List<MinimalChartOfAccountsVo> minimalData = chartOfAccountsDao
					.getChartOfAccountsForGivenLevel2Type("Expenses", organizationId, con);
			data.setDiscountAccounts(minimalData);
			data.setAdjustmentAccounts(minimalData);
			data.setLocationGst(organizationDao.getLocationGSTDetailsWithAddress(con, organizationId));
			data.setVoucherEntries(
					organizationDao.getBasicVoucherEntries(organizationId, con, "Invoice"));
			closeResources(null, null, con);

			con = getAccountsPayable();
			data.setBasicVendor(vendorDao.getBasicVendor(organizationId, con));
			closeResources(null, null, con);

			con = getAccountsReceivableConnection();
			data.setCustomers(customerDao.getBasicCustomer(organizationId, con));
			data.setAksharUrl("https://akshar-api.blackstrawlab.com/direct_api");
			closeResources(null, null, con);

		} catch (Exception e) {
			logger.info("Error in method:getInvoiceDropDownData");
			throw new ApplicationException(e);
		} finally {
			closeResources(null, null, con);

		}
		return data;
	}

	
	public VendorCreditNoteDropDownVo getVendorCreditNoteDropDownData(int organizationId) throws ApplicationException {
		logger.info("Entry into method: getVendorCreditNoteDropDownData");
		Connection con = null;
		VendorCreditNoteDropDownVo data = new VendorCreditNoteDropDownVo();
		
		try {

			con = getFinanceCommon();
			data.setTaxApplicationMethod(financeCommonDao.getItemTaxRate(con));
			data.setDiscountTypes(financeCommonDao.getDiscountType(con));
			data.setTds(financeCommonDao.getTDS(con));
			data.setDestinationOfSupply(financeCommonDao.sourceOfSupply(con));
			data.setSourceOfSupply(financeCommonDao.sourceOfSupply(con));
			data.setMeasures(financeCommonDao.getBasicUnitOfMeasure(con));
			data.setCountry(financeCommonDao.getCountryAndStateList(con));
			closeResources(null, null, con);

			con = getUserMgmConnection();
			data.setTaxGroups(taxGroupDao.getTaxGroups(organizationId, con));
			data.setCurrency(currencyDao.getMinimalCurrencyDetails(organizationId, con));
			data.setProducts(productDao.getBasicProductWithTaxGroups(organizationId, con));
			data.setDefaultCurrency(currencyDao.getBasicCurrencyForOrganization(organizationId, con));
			data.setChartOfAccounts(chartOfAccountsDao.getChartOfAccountsValues(organizationId, con));
			List<MinimalChartOfAccountsVo> minimalData = chartOfAccountsDao
					.getChartOfAccountsForGivenLevel2Type("Expenses", organizationId, con);
			data.setDiscountAccounts(minimalData);
			data.setAdjustmentAccounts(minimalData);
			data.setLocationGst(organizationDao.getLocationGSTDetailsWithAddress(con, organizationId));
			data.setVoucherEntries(
					organizationDao.getBasicVoucherEntries(organizationId, con, "Invoice"));
			closeResources(null, null, con);

			con = getAccountsPayable();
			data.setBasicVendor(vendorDao.getBasicVendorWithGSTNoAndAddress(organizationId, con));
			data.setInvoice(billsInvoiceDao.getInvoiceDataForVendorCredit(organizationId));
			closeResources(null, null, con);

			con = getAccountsReceivableConnection();
			data.setCustomers(customerDao.getBasicCustomer(organizationId, con));
			data.setAksharUrl("https://akshar-api.blackstrawlab.com/direct_api");
			closeResources(null, null, con);

		} catch (Exception e) {
			logger.info("Error in method:getInvoiceDropDownData");
			throw new ApplicationException(e);
		} finally {
			closeResources(null, null, con);

		}
		return data;
		
		
	}
	
	public PurchaseOrderDropDownVo getPurchaseOrderDropDownData(int organizationId) throws ApplicationException {
		logger.info("Entry into method:getInvoiceDropDownData");
		Connection con = null;
		PurchaseOrderDropDownVo data = new PurchaseOrderDropDownVo();

		try {

			con = getFinanceCommon();
			data.setPurchaseOrderType(financeCommonDao.getPurchaseOrderType(con));
			data.setShippingMethod(financeCommonDao.getShippingMethod(con));
			data.setCountry(financeCommonDao.getCountryAndStateList(con));
			data.setSourceOfSupply(financeCommonDao.sourceOfSupply(con));
			data.setTaxApplicationMethod(financeCommonDao.getItemTaxRate(con));
			data.setMeasures(financeCommonDao.getBasicUnitOfMeasure(con));
			data.setDiscountTypes(financeCommonDao.getDiscountType(con));
			data.setTds(tdsDao.getAllTdsOfAnOrganizationForUserAndRole(organizationId, "0", "Super Admin"));
			closeResources(null, null, con);

			con = getUserMgmConnection();
			data.setDateFormat(organizationDao.getDefaultDateForOrganization(organizationId, con));
			data.setVoucherEntries(organizationDao.getBasicVoucherEntries(organizationId, con, "Purchase Order"));
			data.setShippingPreference(shippingPreferenceDao.getBasicShippingPreference(organizationId, con));
			data.setPaymentTerms(paymentTermsDao.getBasicPaymentTermsWithCustomValue(organizationId, con));
			data.setCurrency(currencyDao.getMinimalCurrencyDetails(organizationId, con));
			data.setProducts(productDao.getBasicProductWithTaxGroups(organizationId, con));
			data.setChartOfAccounts(chartOfAccountsDao.getChartOfAccountsValues(organizationId, con));
			data.setTaxGroups(taxGroupDao.getTaxGroups(organizationId, con));
			data.setLocationGst(organizationDao.getLocationGSTDetailsWithAddress(con, organizationId));
			data.setDefaultCurrency(currencyDao.getBasicCurrencyForOrganization(organizationId, con));
			closeResources(null, null, con);

			con = getAccountsPayable();
			data.setBasicVendor(vendorDao.getBasicVendorWithGSTNoAndAddress(organizationId, con));
			closeResources(null, null, con);

		} catch (Exception e) {
			logger.info("Error in method:getInvoiceDropDownData");
			throw new ApplicationException(e);
		} finally {
			closeResources(null, null, con);

		}
		return data;
	}

	public AccountingAspectsDropDownVo getAccountingAspectsDropDownData(int organizationId)
			throws ApplicationException {
		logger.info("Entry into method: getAccountingAspectsDropDownData");
		Connection con = null;
		AccountingAspectsDropDownVo data = new AccountingAspectsDropDownVo();
		try {

			con = getFinanceCommon();
			data.setAccountingtypes(financeCommonDao.getAccountingAspectsTypes(con));
			closeResources(null, null, con);

			con = getUserMgmConnection();
			data.setCurrency(currencyDao.getMinimalCurrencyDetails(organizationId, con));
			data.setLocation(organizationDao.getLocationGSTDetails(con, organizationId));
			data.setLevel1User(userDao.getBasicUserDetailsInAnOrganization(con, organizationId));
			data.setLevel2User(userDao.getBasicUserDetailsInAnOrganization(con, organizationId));
			data.setLevel3User(userDao.getBasicUserDetailsInAnOrganization(con, organizationId));
			data.setChartOfAccounts(chartOfAccountsDao.getChartOfAccountsValuesLevel5(organizationId, con));
			data.setSubLedger(chartOfAccountsDao.getLevel6ChartOfAccounts(organizationId, con));
			data.setVoucherEntries(organizationDao.getBasicVoucherEntries(organizationId, con, "Accounting Entries"));
			data.setPayrollVoucherEntries(
					organizationDao.getBasicVoucherEntries(organizationId, con, "Payroll-PayRun"));
			data.setDateFormat(organizationDao.getDefaultDateForOrganization(organizationId, con));
			data.setDefaultCurrency(currencyDao.getDefaultCurrencyForOrganization(organizationId, con));
			closeResources(null, null, con);

		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(null, null, con);
		}

		return data;

	}

	public List<TaxRateTypeVo> getTaxRateDropDown(int OrganizationId) throws ApplicationException {
		logger.info("Entry into method: getAllTaxTypesForOrganization");
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		List<TaxRateTypeVo> listTaxRateTypes = new ArrayList<TaxRateTypeVo>();

		try {

			con = getUserMgmConnection();
			String query = SettingsAndPreferencesConstants.GET_TAX_RATE_TYPE_ORGANIZATION;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, OrganizationId);
			preparedStatement.setString(2, CommonConstants.STATUS_AS_ACTIVE);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				TaxRateTypeVo taxRateTypeVo = new TaxRateTypeVo();
				taxRateTypeVo.setId(rs.getInt(1));
				taxRateTypeVo.setType(rs.getString(2));

				listTaxRateTypes.add(taxRateTypeVo);
			}
			closeResources(null, null, con);

		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return listTaxRateTypes;
	}

	public BankMasterDropDownVo getBankMastersDropDownData(int organizationId) throws ApplicationException {
		logger.info("Entry into method: getBankMastersDropDownData");
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		BankMasterDropDownVo data = new BankMasterDropDownVo();
		try {

			con = getFinanceCommon();
			data.setAccountType(financeCommonDao.getAccountType(con));
			data.setAccountVariant(financeCommonDao.getAccountVariant(con));
			closeResources(null, null, con);

			con = getUserMgmConnection();
			data.setCurrency(currencyDao.getMinimalCurrencyDetails(organizationId, con));
			data.setLocation(organizationDao.getLocationGSTDetails(con, organizationId));
			closeResources(null, null, con);

			int baseCurrency = organizationDao.getOrganizationBaseCurrency(organizationId);
			BaseCurrencyVo baseCurrencyVo = financeCommonDao.getBaseCurrencyById(baseCurrency);
			data.setBaseCurrencyName(baseCurrencyVo.getName());

		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return data;
	}

	public LedgerDropDownVo getLedgerDropDownData(int organizationId) throws ApplicationException {
		logger.info("Entry into method:getInvoiceDropDownData");
		Connection con = null;
		LedgerDropDownVo data = new LedgerDropDownVo();
		try {

			con = getFinanceCommon();
			data.setEntity(financeCommonDao.getChartOfAccountsEntity(con));
			data.setModule(financeCommonDao.getChartOfAccountsModule(con));
			closeResources(null, null, con);

			con = getUserMgmConnection();
			data.setAccountGroup(chartOfAccountsDao.getAccountGroupsByOrgId(organizationId, con));
			data.setAccountName(chartOfAccountsDao.getAccountNamesByOrgId(organizationId, con));
			closeResources(null, null, con);

		} catch (Exception e) {
			logger.info("Error in method:getInvoiceDropDownData");
			throw new ApplicationException(e);
		} finally {
			closeResources(null, null, con);
		}
		return data;
	}

	public ModuleTypeDropDownVo getUploadDropDownData() throws ApplicationException {
		logger.info("Entry into getUploadDropDownData");
		Connection con = null;
		ModuleTypeDropDownVo data = new ModuleTypeDropDownVo();
		try {

			con = getFinanceCommon();
			data.setModules(financeCommonDao.getUploadModuleTypeDropDown(con));
			closeResources(null, null, con);

		} catch (Exception e) {
			logger.info("Error in method: getUploadDropDownData");
			throw new ApplicationException(e);
		} finally {
			closeResources(null, null, con);
		}
		return data;

	}

	public PayItemDropDownVo getPayItemDownData(int organizationId) throws ApplicationException {
		logger.info("Entry into getPayItemDownData");
		Connection con = null;
		PayItemDropDownVo data = new PayItemDropDownVo();
		try {

			con = getPayrollConnection();
			data.setPayType(payTypeDao.getBasicPayTypesOfAnOrganization(organizationId));
			closeResources(null, null, con);
			
			List<MinimalChartOfAccountsVo> chartOfAccountsData=new ArrayList<MinimalChartOfAccountsVo>();
			chartOfAccountsData.addAll(chartOfAccountsDao.getLedgersByEntity(organizationId, "Employee Expenses"));
			chartOfAccountsData.addAll(chartOfAccountsDao.getLedgersByEntity(organizationId, "Employee Payable"));
			chartOfAccountsData.addAll(chartOfAccountsDao.getLedgersByEntity(organizationId, "Employee Receivable"));
			chartOfAccountsData.addAll(chartOfAccountsDao.getLedgersByEntity(organizationId, "ESIC Liability"));
			chartOfAccountsData.addAll(chartOfAccountsDao.getLedgersByEntity(organizationId, "P-Tax Liability"));
			chartOfAccountsData.addAll(chartOfAccountsDao.getLedgersByEntity(organizationId, "PF Liability"));
			chartOfAccountsData.addAll(chartOfAccountsDao.getLedgersByEntity(organizationId, "Salary Payable"));
			chartOfAccountsData.addAll(chartOfAccountsDao.getLedgersByEntity(organizationId, "TDS Payable"));
			chartOfAccountsData.addAll(chartOfAccountsDao.getLedgerByName( organizationId ,"Power and fuel"));
			chartOfAccountsData.addAll(chartOfAccountsDao.getLedgerByName( organizationId ,"Travelling and conveyance"));
			chartOfAccountsData.addAll(chartOfAccountsDao.getLedgerByName( organizationId ,"Telephone charges"));
			data.setChartOfAccounts(chartOfAccountsData);
			closeResources(null, null, con);

		} catch (Exception e) {
			logger.info("Error in method: getPayItemDownData");
			throw new ApplicationException(e);
		} finally {
			closeResources(null, null, con);
		}
		return data;

	}

	public PayTypeDropDownVo getPayTypeDownData(int organizationId) throws ApplicationException {
		logger.info("Entry into getPayTypeDownData");
		Connection con = null;
		PayTypeDropDownVo data = new PayTypeDropDownVo();
		try {

			con = getPayrollConnection();
			data.setPayType(payTypeDao.getBasicPayTypesOfAnOrganization(organizationId));
			closeResources(null, null, con);

		} catch (Exception e) {
			logger.info("Error in method: getPayTypeDownData");
			throw new ApplicationException(e);
		} finally {
			closeResources(null, null, con);
		}
		return data;

	}

	public VoucherDropDownVo getVoucherDropDownData() throws ApplicationException {
		logger.info("Entry into getVoucherDropDownData");
		VoucherDropDownVo data = new VoucherDropDownVo();
		try {
			data.setVoucherType(financeCommonDao.getVoucherTypes());
		} catch (Exception e) {
			logger.info("Error in method: getVoucherDropDownData");
			throw new ApplicationException(e);
		} finally {
			closeResources(null, null, null);
		}
		return data;

	}

	public VamDropDownVo getVendorAccessMgmtDropDown(int organizationId) throws ApplicationException {
		logger.info("Entry into getVendorAccessMgmtDropDown");
		Connection con = null;
		VamDropDownVo data = new VamDropDownVo();
		try {

			con = getAccountsPayable();
			data.setVendorNames(vendorDao.getVendorNames(organizationId, con));
			data.setVendorContacts(vendorDao.getVendorContacts(con, organizationId));
			closeResources(null, null, con);

			con = getUserMgmConnection();
			data.setVendorGroupsName(vendorGroupDao.getVendorGroupNamesOfOrganization(organizationId, con));
			data.setSettingsName(vendorSettingsDao.getSettingsNameOfAnOrganization(organizationId, con));
			closeResources(null, null, con);

		} catch (Exception e) {
			logger.info("Error in method: getVendorAccessMgmtDropDown");
			throw new ApplicationException(e);
		} finally {
			closeResources(null, null, con);
		}
		return data;
	}

	public BasicInvoicesDropdownVo getRefundInvoicesForCustomer(int organizationId, int customerId, int refundId)
			throws ApplicationException {
		logger.info("Entry into method: getRefundInvoicesForCustomer");
		BasicInvoicesDropdownVo refundDropdownVo = new BasicInvoicesDropdownVo();
		try {
			refundDropdownVo
					.setInvoiceList(refundDao.getDropdownInvoicesForRefund(organizationId, customerId, refundId));
		} catch (Exception e) {
			logger.error("Error while retrieving getRefundInvoicesForCustomer:", e);
			throw new ApplicationException(e);
		}
		return refundDropdownVo;
	}

	public RefundDropdownVo getRefundDropDownData(int organizationId) throws ApplicationException {
		logger.info("Entry into method: getRefundDropDownData");
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		RefundDropdownVo refundDropdownVo = new RefundDropdownVo();
		try {

			con = getAccountsReceivableConnection();
			refundDropdownVo.setRefundTypes(financeCommonDao.getRefundTypes());
			refundDropdownVo.setCustomersList(customerDao.getBasicCustomer(organizationId, con));
			logger.info("Successfully fetched customerList of size ");
			closeResources(rs, preparedStatement, con);

			refundDropdownVo.setPaymentModeList(financeCommonDao.getPaymentMode());
			con = getUserMgmConnection();
			refundDropdownVo.setAccountsList(chartOfAccountsDao.getChartOfAccountsValuesLevel5(organizationId, con));
			closeResources(rs, preparedStatement, con);

			con = getUserMgmConnection();
			BasicVoucherEntriesVo basicVoucherEntriesVo = organizationDao.getBasicVoucherEntries(organizationId, con,
					"Accounts Receivable-Refund");
			refundDropdownVo.setRefundRefernce(basicVoucherEntriesVo);
			refundDropdownVo.setCurrency(currencyDao.getMinimalCurrencyDetails(organizationId, con));
			BasicCurrencyVo basicCurrencyVo = currencyDao.getBasicCurrencyForOrganization(organizationId, con);
			refundDropdownVo.setCurrencyId(basicCurrencyVo != null ? basicCurrencyVo.getId() : 0);
			BasicGSTLocationDetailsVo basicGstLocationDetailsVo = organizationDao.getLocationGSTDetailsWithAddress(con,
					organizationId);
			if (basicGstLocationDetailsVo != null && basicGstLocationDetailsVo.getGstLocation() != null
					&& basicGstLocationDetailsVo.getGstLocation().size() > 0) {
				for (BasicLocationDetailsVo basicLocationDetailsVo : basicGstLocationDetailsVo.getGstLocation()) {
					if (basicLocationDetailsVo.isRegistered()) {
						refundDropdownVo.setLocationId(basicLocationDetailsVo.getId());
					}
				}
			}
			refundDropdownVo.setLocationDetails(organizationDao.getLocationGSTDetailsWithAddress(con, organizationId));
			List<PaymentModeVo> paymentModeVoList = refundDropdownVo.getPaymentModeList();
			List<BankMasterAccountBaseVo> bankAccounts = bankMasterDao
					.getAllBankMasterAccountsOfAnOrganization(organizationId);
			List<BankMasterAccountBaseVo> cashAccounts = bankMasterDao
					.getAllBankMasterCashAccountsOfAnOrganization(organizationId);
			for (PaymentModeVo paymentModeVo : paymentModeVoList) {
				String paymentMode = paymentModeVo.getName();
				if (null != paymentMode && paymentMode.equalsIgnoreCase("Cash")) {
					paymentModeVo.setAccountsList(cashAccounts);
				} else if (null != paymentMode && (paymentMode.equalsIgnoreCase("Bank Transfer")
						|| paymentMode.equalsIgnoreCase("Cheque&Demand Draft"))) {
					paymentModeVo.setAccountsList(bankAccounts);
				}

			}
			closeResources(rs, preparedStatement, con);

		} catch (Exception e) {
			logger.error("Error while retrieving getRefundDropDownData:", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return refundDropdownVo;
	}

	public LetterOfUndertakingDropDownVo getLUTDropDown(int organizationId) throws ApplicationException {
		logger.info("Entry into getLUTDropDown");
		Connection con = null;
		LetterOfUndertakingDropDownVo data = new LetterOfUndertakingDropDownVo();
		try {

			con = getUserMgmConnection();
			data.setLocationGst(organizationDao.getLocationGSTDetailsWithAddress(con, organizationId));

			// Get FY Data
			int CurrentYear = Calendar.getInstance().get(Calendar.YEAR);
			int CurrentMonth = (Calendar.getInstance().get(Calendar.MONTH) + 1);
			int currentFy;
			List<FinYearExpiryDateVo> fyData = new ArrayList<FinYearExpiryDateVo>();
			if (CurrentMonth < 4) {
				currentFy = CurrentYear - 1;
			} else {
				currentFy = CurrentYear;
			}
			String dateFormat = organizationDao.getDefaultDateForOrganization(organizationId, con);

			fyData.add(new FinYearExpiryDateVo((currentFy + 1) + "-" + ((currentFy + 2) - 2000),
					DateConverter.getInstance()
							.convertDateToGivenFormat(java.sql.Date.valueOf((currentFy + 2) + "-3-31"), dateFormat)));// Next
																														// FY
			fyData.add(new FinYearExpiryDateVo((currentFy) + "-" + ((currentFy + 1) - 2000), DateConverter.getInstance()
					.convertDateToGivenFormat(java.sql.Date.valueOf((currentFy + 1) + "-3-31"), dateFormat)));// current
																												// FY
			fyData.add(new FinYearExpiryDateVo((currentFy - 1) + "-" + (currentFy - 2000), DateConverter.getInstance()
					.convertDateToGivenFormat(java.sql.Date.valueOf((currentFy) + "-3-31"), dateFormat)));// last FY
			fyData.add(new FinYearExpiryDateVo((currentFy - 2) + "-" + ((currentFy - 1) - 2000),
					DateConverter.getInstance()
							.convertDateToGivenFormat(java.sql.Date.valueOf((currentFy - 1) + "-3-31"), dateFormat)));
			fyData.add(new FinYearExpiryDateVo((currentFy - 3) + "-" + ((currentFy - 2) - 2000),
					DateConverter.getInstance()
							.convertDateToGivenFormat(java.sql.Date.valueOf((currentFy - 2) + "-3-31"), dateFormat)));

			data.setFinancialYears(fyData);
			closeResources(null, null, con);

		} catch (Exception e) {
			logger.error("Error in method: getLUTDropDown", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(null, null, con);
		}
		return data;
	}

	public List<ReceiptBulkDropdownVo> getBulkReceiptDataForCustomer(int organizationId, int currencyId, int customerId,
			int receiptId) throws ApplicationException {
		logger.info("Entry into method: getBulkReceiptDataForCustomer");
		return receiptDao.getBulkReceiptDataForCustomer(organizationId, currencyId, customerId, receiptId);
	}

	public BasicInvoicesDropdownVo getReceiptInvoicesForCustomer(int organizationId, int currencyId, int customerId,
			int receiptId) throws ApplicationException {
		logger.info("Entry into method: getReceiptInvoicesForCustomer");
		BasicInvoicesDropdownVo basicInvoicesDropdownVo = new BasicInvoicesDropdownVo();
		Connection connection = null;
		try {
			basicInvoicesDropdownVo.setInvoiceList(
					receiptDao.getReceiptDropdownInvoices(organizationId, currencyId, customerId, receiptId));
			List<CommonVo> ledgers = chartOfAccountsDao.getLedgerBySubLedegerDescription(organizationId, customerId,
					"Customer");
			List<MinimalChartOfAccountsVo> customerLedgers = new ArrayList<MinimalChartOfAccountsVo>();
			ledgers.forEach(ledger -> {
				MinimalChartOfAccountsVo ledgerObj = new MinimalChartOfAccountsVo();
				ledgerObj.setId(ledger.getId());
				ledgerObj.setValue(ledger.getId());
				ledgerObj.setName(ledger.getName());
				customerLedgers.add(ledgerObj);
			});
			basicInvoicesDropdownVo.setInvoiceLedgers(customerLedgers);
			connection = getAccountsReceivableConnection();
			CurrencyVo currencyVo = currencyDao.getCurrency(currencyId);
			String creditNoteBal = creditNotesDao.getcreditNotesAmountForCustomerCurrency(organizationId, customerId,
					currencyId, connection);
			String advBal = receiptDao.getAdvanceOnaccountDueAmountForCustomerCurrency(organizationId, customerId,
					currencyId, connection);
			if (receiptId > 0) {
				String receiptTotalForCN = receiptDao.getTotalCreditNoteAmountForReceipt(receiptId);
				if (creditNoteBal != null && receiptTotalForCN != null) {
					Double totalBal = Double.parseDouble(creditNoteBal);
					Double recCnBal = Double.parseDouble(receiptTotalForCN);
					if (totalBal > 0) {
						totalBal = totalBal - recCnBal;
						creditNoteBal = totalBal + "";
					}

				}

			}
			if (currencyVo != null) {
				if (creditNoteBal != null && Double.parseDouble(creditNoteBal) > 0) {
					BigDecimal bd = new BigDecimal(Double.parseDouble(creditNoteBal))
							.setScale(currencyVo.getNoOfDecimalsForAmount(), RoundingMode.HALF_UP);
					creditNoteBal = bd.doubleValue() + "";
				}
				if (advBal != null && Double.parseDouble(advBal) > 0) {
					BigDecimal bd = new BigDecimal(Double.parseDouble(advBal))
							.setScale(currencyVo.getNoOfDecimalsForAmount(), RoundingMode.HALF_UP);
					advBal = bd.doubleValue() + "";
				}

			}
			basicInvoicesDropdownVo.setCreditNotesBalance(creditNoteBal);
			basicInvoicesDropdownVo.setAdvanceBalance(advBal);
			closeResources(null, null, connection);

		} catch (Exception e) {
			logger.error("Error while retrieving getReceiptInvoicesForCustomer:", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(null, null, connection);
		}
		return basicInvoicesDropdownVo;
	}

	public BasicInvoicesDropdownVo getDueBalancesForCustomer(int organizationId, int customerId)
			throws ApplicationException {
		logger.info("Entry into method: getDueBalancesForCustomer");
		BasicInvoicesDropdownVo basicInvoicesDropdownVo = new BasicInvoicesDropdownVo();
		Connection connection = null;
		try {

			connection = getAccountsReceivableConnection();
			basicInvoicesDropdownVo.setCreditNotesBalance(
					creditNotesDao.getcreditNotesDueAmountForCustomer(organizationId, customerId, connection));
			basicInvoicesDropdownVo.setAdvanceBalance(
					receiptDao.getAdvanceOnaccountDueAmountForCustomer(organizationId, customerId, connection));
			closeResources(null, null, connection);

		} catch (Exception e) {
			logger.error("Error while retrieving getDueBalancesForCustomer:", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(null, null, connection);
		}
		return basicInvoicesDropdownVo;
	}

	public ArInvoicesDropDownVo getArInvoiceDropDownData(int organizationId) throws ApplicationException {
		logger.info("Entry into method:getArInvoiceDropDownData");
		Connection con = null;
		ArInvoicesDropDownVo data = new ArInvoicesDropDownVo();

		try {

			con = getFinanceCommon();
			data.setDiscountTypes(financeCommonDao.getDiscountType(con));
			data.setTds(financeCommonDao.getTDS(con));
			data.setSourceOfSupply(financeCommonDao.sourceOfSupply(con));
			data.setMeasures(financeCommonDao.getBasicUnitOfMeasure(con));
			data.setInvoiceType(financeCommonDao.getInvoiceTypes());
			data.setTaxApplicationMethod(financeCommonDao.getItemTaxRate(con));
			data.setCountry(financeCommonDao.getCountryAndStateList(con));
			data.setSupplyServices(financeCommonDao.getSupplyServices());
			data.setExportType(financeCommonDao.getExportTypes());
			closeResources(null, null, con);

			con = getUserMgmConnection();
			data.setCurrency(currencyDao.getMinimalCurrencyDetails(organizationId, con));
			data.setTaxGroups(taxGroupDao.getTaxGroups(organizationId, con));
			data.setPaymentTerms(paymentTermsDao.getBasicPaymentTermsWithCustomValue(organizationId, con));
			data.setProducts(productDao.getBasicProductWithTaxGroups(organizationId, con));
			data.setChartOfAccounts(chartOfAccountsDao.getChartOfAccountsValues(organizationId, con));
			List<MinimalChartOfAccountsVo> minimalData = chartOfAccountsDao
					.getChartOfAccountsForGivenLevel2Type("Expenses", organizationId, con);
			data.setVoucherEntries(
					organizationDao.getBasicVoucherEntries(organizationId, con, "Accounts Receivable-Invoice"));
			data.setDiscountAccounts(minimalData);
			data.setLocationDetails(organizationDao.getLocationGSTDetailsWithAddress(con, organizationId));
			data.setAdjustmentAccounts(minimalData);
			data.setDefaultCurrency(currencyDao.getDefaultCurrencyForOrganization(organizationId, con));
			closeResources(null, null, con);

			con = getAccountsReceivableConnection();
			data.setCustomers(customerDao.getBasicCustomer(organizationId, con));
			data.setLutDetails(letterOfUndertakingDao.getLUTDetailsForCurrentFinYear(organizationId));
			closeResources(null, null, con);

			data.setBankingDetails(bankMasterDao.getAllBasicBankingDeatilsForOrg(organizationId));
		} catch (Exception e) {
			logger.info("Error in method:getInvoiceDropDownData");
			throw new ApplicationException(e);
		} finally {
			closeResources(null, null, con);

		}
		return data;
	}

	public BalanceConfirmationDropDownVo getBalanceConfirmationDropDownData(int organizationId, int vendorId)
			throws ApplicationException {
		logger.info("Entry into getBalanceConfirmationDownData");
		Connection con = null;
		BalanceConfirmationDropDownVo data = new BalanceConfirmationDropDownVo();
		try {

			con = getUserMgmConnection();
			data.setCompanyDetails(vendorDao.getVendorDetails(vendorId));
			data.setCurrency(currencyDao.getMinimalCurrencyDetails(organizationId, con));
			closeResources(null, null, con);

		} catch (Exception e) {
			logger.info("Error in method: getBalanceConfirmationDownData");
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(null, null, con);
		}
		return data;

	}

	public CreditNoteDropdownVo getCreditNoteDropdownData(int organizationId, int customerId,int creditNoteId)
			throws ApplicationException {
		logger.info("Entry into method: getCreditNoteDropdownData");
		Connection con = null;
		CreditNoteDropdownVo data = new CreditNoteDropdownVo();
		logger.info("BEfore:" + TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()));
		try {

			con = getFinanceCommon();
			data.setTds(financeCommonDao.getTDS(con));
			data.setMeasures(financeCommonDao.getBasicUnitOfMeasure(con));
			closeResources(null, null, con);

			con = getUserMgmConnection();
			data.setTaxGroups(taxGroupDao.getTaxGroups(organizationId, con));
			data.setCreditNoteNo(
					organizationDao.getBasicVoucherEntries(organizationId, con, "Accounts Receivable-Credit Note"));
			List<ArInvoiceVo> invoicesList = creditNotesDao.getInvoicesForCreditNotes(organizationId, customerId,creditNoteId);
			data.setInvoices(invoicesList);
			data.setLocationDetails(organizationDao.getLocationGSTDetailsWithAddress(con, organizationId));
			data.setReasons(financeCommonDao.getCreditNoteReasons());
			closeResources(null, null, con);

		} catch (Exception e) {
			logger.info("Error in method:getInvoiceDropDownData", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(null, null, con);

		}
		return data;
	}

	public CustomerAccountsDropdownVo getCreditNoteCustomerAndAccountsDropdownData(int organizationId)
			throws ApplicationException {
		logger.info("Entry into method: getCreditNoteCustomerAndAccountsDropdownData");
		Connection con = null;
		CustomerAccountsDropdownVo data = new CustomerAccountsDropdownVo();

		try {

			con = getUserMgmConnection();
			List<MinimalChartOfAccountsVo> minimalData = chartOfAccountsDao
					.getChartOfAccountsForGivenLevel2Type("Expenses", organizationId, con);
			data.setChartOfAccounts(chartOfAccountsDao.getChartOfAccountsValues(organizationId, con));
			data.setDiscountAndAdjAccounts(minimalData);
			data.setCustomers(customerDao.getCustomerList(organizationId));
			closeResources(null, null, con);

		} catch (Exception e) {
			logger.info("Error in method:getInvoiceDropDownData", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(null, null, con);

		}
		return data;
	}

	public ContraDropDownVo getContraDropDownData(int organizationId) throws ApplicationException {
		logger.info("Entry into Method: getContraDropDownData");
		ContraDropDownVo contraDropDownVo = new ContraDropDownVo();
		Connection con = null;
		try {

			con = getUserMgmConnection();
			contraDropDownVo.setCurrency(currencyDao.getMinimalCurrencyDetails(organizationId, con));
			contraDropDownVo.setBaseCurrency(currencyDao.getDefaultCurrencyForOrganization(organizationId, con));
			contraDropDownVo.setAccount(bankMasterDao.getAllBankMasterDetailsOfAnOrganization(organizationId));
			contraDropDownVo
					.setVoucherEntries(organizationDao.getBasicVoucherEntries(organizationId, con, "Banking-Contra"));
			closeResources(null, null, con);

		} catch (Exception e) {
			logger.info("Error in getContraDropDownData");
			throw new ApplicationException(e);
		} finally {
			closeResources(null, null, con);

		}

		return contraDropDownVo;
	}

	public PaymentNonCoreDropDownVo getPaymentNonCoreDropDownData(int organizationId)
			throws ApplicationException, SQLException {

		List<PaymentTypeVo> paymentTypes = paymentNonCoreDao.getPaymentTypes();
		Connection con = getAccountsPayable();
		List<PaymentTypeVo> payRun = payRunDao.getPayRunDataForOrganization(organizationId, con);
		List<PaymentTypeVo> vendors = vendorDao.getActiveVendorNames(organizationId, con);

		List<PaymentTypeVo> employeeList = employeeDao.getAllActiveEmployees(organizationId);
		closeResources(null, null, con);

		con = getUserMgmConnection();
		List<MinimalChartOfAccountsVo> vendorAccounts = chartOfAccountsDao.getChartOfAccountsValues(organizationId,
				con);
		List<BasicCurrencyVo> currency = currencyDao.getMinimalCurrencyDetails(organizationId, con);
		BasicCurrencyVo defaultCurrency = currencyDao.getDefaultCurrencyForOrganization(organizationId, con);
		List<String> paymentRefNos = paymentNonCoreDao.getPaymentRefNosByOrgId(organizationId);
		List<MinimalChartOfAccountsVo> contactAccounts = chartOfAccountsDao
				.getAllCustomerEntityLevel5LedgersForOrg(organizationId, con);
		List<MinimalChartOfAccountsVo> minimalChartOfAccounts = chartOfAccountsDao
				.getChartOfAccountsValuesLevel5(organizationId, con);
		closeResources(null, null, con);

		con = getAccountsReceivableConnection();
		List<PaymentTypeVo> customers = customerDao.getActiveCustomers(organizationId, con);
		List<BasicPayTypeVo> payTypeList = payTypeDao.getBasicPayTypesOfAnOrganization(organizationId);
		closeResources(null, null, con);

		con = getFinanceCommon();
		List<ReceiptVo> invoiceDetails = receiptDao.getAllReceiptsOfAnOrganizationForUserAndRole(organizationId, "",
				"Super Admin");
		PoFilterVo poFilterVo = new PoFilterVo();
		poFilterVo.setOrgId(organizationId);
		List<PoListVo> poList = purchaseOrderDao.getPoFilteredList(poFilterVo);
		List<TaxRateTypeVo> taxRateType = getTaxRateDropDown(organizationId);

		List<PaymentModeVo> paymentModes = financeCommonDao.getPaymentMode();
		List<PaymentModeDropDownVo> paymentModeDropdown = paymentNonCoreDao.addValueFieldToPaymentMode(paymentModes);

		// Custom table dropdown

		List<PaymentNonCoreColumnDropDownVo> paymentColumnList = new ArrayList<PaymentNonCoreColumnDropDownVo>();
		Map<String, Integer> ledgersMap = new HashMap<String, Integer>();

		for (MinimalChartOfAccountsVo minimalChartOfAccountsVo : minimalChartOfAccounts) {
			paymentColumnList.add(new PaymentNonCoreColumnDropDownVo(minimalChartOfAccountsVo.getId(),
					minimalChartOfAccountsVo.getName()));
			ledgersMap.put(minimalChartOfAccountsVo.getName(), minimalChartOfAccountsVo.getId());
		}

		PaymentNonCoreCustomTableVo customTableList = new PaymentNonCoreCustomTableVo();
		// Custom tables for Bill Payments
		List<PaymentNonCoreCustomFieldVo> billPayments = new ArrayList<PaymentNonCoreCustomFieldVo>();

		PaymentNonCoreCustomFieldVo billRef = new PaymentNonCoreCustomFieldVo();
		billRef.setLedgerId("");
		billRef.setcName("billRef");
		billRef.setColName("Reference No");
		billRef.setLedgerName("");
		billRef.setColumnShow(true);
		billPayments.add(billRef);
		
		PaymentNonCoreCustomFieldVo dueDate = new PaymentNonCoreCustomFieldVo();
		dueDate.setLedgerId("");
		dueDate.setcName("dueDate");
		dueDate.setColName("Due Date");
		dueDate.setLedgerName("");
		dueDate.setColumnShow(true);
		billPayments.add(dueDate);

		PaymentNonCoreCustomFieldVo others1 = new PaymentNonCoreCustomFieldVo();
		others1.setLedgerId("");
		others1.setcName("others1");
		others1.setLedgerName("");
		others1.setColumnShow(false);
		billPayments.add(others1);

		PaymentNonCoreCustomFieldVo others2 = new PaymentNonCoreCustomFieldVo();
		others2.setLedgerId("");
		others2.setcName("others2");
		others2.setLedgerName("");
		others2.setColumnShow(false);
		billPayments.add(others2);

		customTableList.setBillPayments(billPayments);

		List<PaymentNonCoreCustomFieldVo> tdsPayments = new ArrayList<PaymentNonCoreCustomFieldVo>();

		PaymentNonCoreCustomFieldVo tax = new PaymentNonCoreCustomFieldVo();
		tax.setLedgerId(ledgersMap.get(PaymentNonCoreConstants.DEFAULT_TDS_TAX_LEDGER) + "");
		tax.setcName("tax");
		tax.setLedgerName(PaymentNonCoreConstants.DEFAULT_TDS_TAX_LEDGER);
		tax.setColumnShow(true);
		tax.setColName(PaymentNonCoreConstants.TAX);
		tdsPayments.add(tax);

		PaymentNonCoreCustomFieldVo interest = new PaymentNonCoreCustomFieldVo();
		interest.setLedgerId(ledgersMap.get(PaymentNonCoreConstants.DEFAULT_TDS_INTEREST_LEDGER) + "");
		interest.setcName("interest");
		interest.setLedgerName(PaymentNonCoreConstants.DEFAULT_TDS_INTEREST_LEDGER);
		interest.setColumnShow(true);
		interest.setColName(PaymentNonCoreConstants.INTEREST);
		tdsPayments.add(interest);

		PaymentNonCoreCustomFieldVo penalty = new PaymentNonCoreCustomFieldVo();
		penalty.setLedgerId("");
		penalty.setcName("penalty");
		penalty.setLedgerName("");
		penalty.setColumnShow(false);
		penalty.setColName(PaymentNonCoreConstants.PENALTY);
		tdsPayments.add(penalty);

		tdsPayments.add(others1);

		others2 = new PaymentNonCoreCustomFieldVo();
		others2.setLedgerId("");
		others2.setcName("others2");
		others2.setLedgerName("");
		others2.setColumnShow(false);
		tdsPayments.add(others2);

		customTableList.setTdsPayments(tdsPayments);

		List<PaymentNonCoreCustomFieldVo> gstPayments = new ArrayList<PaymentNonCoreCustomFieldVo>();

		tax = new PaymentNonCoreCustomFieldVo();
		tax.setLedgerId(ledgersMap.get(PaymentNonCoreConstants.DEFAULT_GST_TAX_LEDGER) + "");
		tax.setcName("tax");
		tax.setLedgerName(PaymentNonCoreConstants.DEFAULT_GST_TAX_LEDGER);
		tax.setColumnShow(true);
		tax.setColName(PaymentNonCoreConstants.TAX);
		gstPayments.add(tax);

		interest = new PaymentNonCoreCustomFieldVo();
		interest.setLedgerId(ledgersMap.get(PaymentNonCoreConstants.DEFAULT_GST_INTEREST_LEDGER) + "");
		interest.setcName("interest");
		interest.setLedgerName(PaymentNonCoreConstants.DEFAULT_GST_INTEREST_LEDGER);
		interest.setColumnShow(true);
		interest.setColName(PaymentNonCoreConstants.INTEREST);
		gstPayments.add(interest);

		penalty = new PaymentNonCoreCustomFieldVo();
		penalty.setLedgerId("");
		penalty.setcName("penalty");
		penalty.setLedgerName("");
		penalty.setColumnShow(true);
		penalty.setColName(PaymentNonCoreConstants.PENALTY);
		gstPayments.add(penalty);

		gstPayments.add(others1);
		gstPayments.add(others2);

		customTableList.setGstPayments(gstPayments);

		List<PaymentNonCoreCustomFieldVo> employeePayments = new ArrayList<PaymentNonCoreCustomFieldVo>();

		employeePayments.add(others1);
		employeePayments.add(others2);

		PaymentNonCoreCustomFieldVo others3 = new PaymentNonCoreCustomFieldVo();
		others3.setLedgerId("");
		others3.setcName("others3");
		others3.setLedgerName("");
		others3.setColumnShow(false);
		employeePayments.add(others3);

		customTableList.setEmployeePayments(employeePayments);

		List<BankMasterAccountBaseVo> bankAccounts = bankMasterDao
				.getAllBankMasterAccountsOfAnOrganization(organizationId);
		for (BankMasterAccountBaseVo bankAccount : bankAccounts) {
			bankAccount.setName(bankAccount.getAccountName());
			bankAccount.setValue(bankAccount.getId() + "");
		}
		List<BankMasterAccountBaseVo> cashAccounts = bankMasterDao
				.getAllBankMasterCashAccountsOfAnOrganization(organizationId);
		for (BankMasterAccountBaseVo cashAccount : cashAccounts) {
			cashAccount.setName(cashAccount.getAccountName());
			cashAccount.setValue(cashAccount.getId() + "");
		}
		BasicBankingVo paidVia = bankMasterDao.getAllBasicBankingDeatilsForOrg(organizationId);

		for (PaymentModeDropDownVo paymentModeVo : paymentModeDropdown) {
			String paymentMode = paymentModeVo.getName();
			if (null != paymentMode && paymentMode.equalsIgnoreCase("Cash")) {
				paymentModeVo.setChild(cashAccounts);
			} else if (null != paymentMode && (paymentMode.equalsIgnoreCase("Bank Transfer")
					|| paymentMode.equalsIgnoreCase("Cheque&Demand Draft"))) {
				paymentModeVo.setChild(bankAccounts);
			} else if (null != paymentMode && paymentMode.equalsIgnoreCase("Wallet")) {
				paymentModeVo.setChild(convertToBankMasterAccountBaseFromWallet(paidVia.getWallets()));
			} else if (null != paymentMode && paymentMode.equalsIgnoreCase("Credit Card")) {
				paymentModeVo.setChild(convertToBankMasterAccountBaseFromCard(paidVia.getCardAccounts()));
			}

		}
		closeResources(null, null, con);

		con = getUserMgmConnection();
		BasicVoucherEntriesVo paymentVoucher = organizationDao.getBasicVoucherEntries(organizationId, con,
				PaymentNonCoreConstants.PAYMENTS);
		BasicVoucherEntriesVo poRefVoucher = organizationDao.getBasicVoucherEntries(organizationId, con,
				PaymentNonCoreConstants.PURCHASE_ORDER);
		BasicVoucherEntriesVo payRunVoucher = organizationDao.getBasicVoucherEntries(organizationId, con,
				PaymentNonCoreConstants.PAYROLL_PAYRUN);

		List<PaymentTypeVo> statutoryBodyList = statutoryBodyDao
				.getActiveStatutoryBodiesOfAnOrganizationForUserAndRole(organizationId, "", "Super Admin");

		BasicContactVo contact = new BasicContactVo();
		contact.setStatutoryBody(statutoryBodyList);
		contact.setCustomers(customers);
		contact.setEmployees(employeeList);
		contact.setVendors(vendors);

		MultiLevelContactVo statBodyLevel = new MultiLevelContactVo();
		statBodyLevel.setId(1);
		statBodyLevel.setName(PaymentNonCoreConstants.STATUTORY_BODY);
		statBodyLevel.setChild(statutoryBodyList);

		MultiLevelContactVo custLevel = new MultiLevelContactVo();
		custLevel.setId(2);
		custLevel.setName(PaymentNonCoreConstants.CUSTOMER);
		custLevel.setChild(customers);

		MultiLevelContactVo empLevel = new MultiLevelContactVo();
		empLevel.setId(3);
		empLevel.setName(PaymentNonCoreConstants.EMPLOYEE);
		empLevel.setChild(employeeList);

		MultiLevelContactVo vendorLevel = new MultiLevelContactVo();
		vendorLevel.setId(4);
		vendorLevel.setName(PaymentNonCoreConstants.VENDOR);
		vendorLevel.setChild(vendors);

		List<MultiLevelContactVo> contactList = new ArrayList<MultiLevelContactVo>();
		contactList.add(statBodyLevel);
		contactList.add(custLevel);
		contactList.add(empLevel);
		contactList.add(vendorLevel);

		List<InvoiceDetailsDropDownVo> invoices = new ArrayList<InvoiceDetailsDropDownVo>();
		if (invoiceDetails != null) {
			for (ReceiptVo receipt : invoiceDetails) {
				if (receipt.getInvoices() != null) {
					for (BasicInvoiceDetailsVo receiptInvoiceVo : receipt.getInvoices()) {
						InvoiceDetailsDropDownVo invoiceDetailsDropDownVo = new InvoiceDetailsDropDownVo();
						invoiceDetailsDropDownVo.setId(receiptInvoiceVo.getId());
						invoiceDetailsDropDownVo.setValue(receiptInvoiceVo.getId());
						invoiceDetailsDropDownVo.setName(receiptInvoiceVo.getInvoiceNumber());
						invoiceDetailsDropDownVo.setInvoiceAmount(receiptInvoiceVo.getInvoiceAmount());
						invoices.add(invoiceDetailsDropDownVo);

					}
				}
			}
		}

		closeResources(null, null, con);


		PaymentNonCoreDropDownVo dropDownVo = new PaymentNonCoreDropDownVo();
		dropDownVo.setPaidVia(null);
		dropDownVo.setPaymentTypes(paymentTypes);
		dropDownVo.setVendorNames(vendors);
		dropDownVo.setVendorAccounts(vendorAccounts);
		dropDownVo.setCurrency(currency);
		dropDownVo.setPaymentRefNos(paymentRefNos);
		dropDownVo.setPaidTo(customers);
		dropDownVo.setPayType(payTypeList);
		closeResources(null, null, con);
		con = getFinanceCommon();
		dropDownVo.setTds(financeCommonDao.getTDS(con));
		closeResources(null, null, con);
		dropDownVo.setCreditDetails(null);
		dropDownVo.setInvoiceDetails(invoices);
		dropDownVo.setBillDetails(null);
		dropDownVo.setPoList(poList);
		// dropDownVo.setGstDetails(gstDetailsDropdown);
		dropDownVo.setContactAccounts(contactAccounts);
		dropDownVo.setPaymentModes(paymentModeDropdown);
		dropDownVo.setEmployee(employeeList);
		dropDownVo.setPayRunVoucher(payRunVoucher);
		dropDownVo.setCustomTableList(customTableList);
		dropDownVo.setPaymentRefNoVoucher(paymentVoucher);
		dropDownVo.setPoRefNoVoucher(poRefVoucher);
		dropDownVo.setContact(contactList);
		dropDownVo.setTaxRateType(taxRateType);
		dropDownVo.setPayRun(payRun);
		con = getFinanceCommon();
		dropDownVo.setPaidFor(financeCommonDao.getPayrollPaidForType(con));
		dropDownVo.setDefaultCurrency(defaultCurrency);
		dropDownVo.setStatutoryBody(statutoryBodyList);
		closeResources(null, null, con);
		return dropDownVo;
	}
	/*
	 * private List<VendorNameDropdownVo>
	 * addValueToVendorNames(List<VamVendorNameVo> vendorNames) {
	 * List<VendorNameDropdownVo> vendorNamesDropdown = new
	 * ArrayList<VendorNameDropdownVo>(); for (VamVendorNameVo name : vendorNames) {
	 * VendorNameDropdownVo dropdownName = new VendorNameDropdownVo();
	 * dropdownName.setId(name.getId()); dropdownName.setName(name.getVendorName());
	 * dropdownName.setValue(name.getId()); vendorNamesDropdown.add(dropdownName); }
	 * return vendorNamesDropdown; }
	 */

	private List<BankMasterAccountBaseVo> convertToBankMasterAccountBaseFromWallet(
			List<BasicBankMasterWalletVo> wallets) {
		List<BankMasterAccountBaseVo> bankMasterList = new ArrayList<BankMasterAccountBaseVo>();
		for (BasicBankMasterWalletVo wallet : wallets) {
			BankMasterAccountBaseVo bankMaster = new BankMasterAccountBaseVo();
			bankMaster.setId(wallet.getValue());
			bankMaster.setValue(String.valueOf(wallet.getValue()));
			bankMaster.setName(wallet.getName());
			bankMasterList.add(bankMaster);
		}
		return bankMasterList;
	}

	private List<BankMasterAccountBaseVo> convertToBankMasterAccountBaseFromCard(
			List<BasicBankMasterCardAccountVo> cards) {
		List<BankMasterAccountBaseVo> bankMasterList = new ArrayList<BankMasterAccountBaseVo>();
		for (BasicBankMasterCardAccountVo card : cards) {
			BankMasterAccountBaseVo bankMaster = new BankMasterAccountBaseVo();
			bankMaster.setId(card.getValue());
			bankMaster.setValue(String.valueOf(card.getValue()));
			bankMaster.setName(card.getName());
			bankMasterList.add(bankMaster);
		}
		return bankMasterList;
	}

	public ApplyCreditsDropDownVo getApplyCreditsDropDownData(int orgId, int customerId, int currencyId)
			throws ApplicationException {
		logger.info("Entry into Method: getApplyCreditsDropDownData");
		ApplyCreditsDropDownVo applyCreditsDropDownVo = new ApplyCreditsDropDownVo();
		Connection con = null;
		try {

			con = getAccountsReceivableConnection();
			List<ReceiptsDetailsVo> receipts = receiptDao.getReceiptsByCustomerId(orgId, customerId, currencyId);
			applyCreditsDropDownVo.setReceiptDetails(receipts);
			applyCreditsDropDownVo.setCreditNoesDetails(
					creditNotesDao.getCreditNotesDetailsByCustomerId(orgId, customerId, currencyId, con));
			applyCreditsDropDownVo
					.setInvoice(arInvoiceDao.getInvoiceDetailsByCustomerId(orgId, customerId, currencyId));
			applyCreditsDropDownVo
					.setLedger(chartOfAccountsDao.getLedgersByEntity(orgId, ChartOfAccountsConstants.ENTITY_CUSTOMER));
			Double totalAvailableAmount = 0.0;
			for (ReceiptsDetailsVo receipt : receipts) {
				totalAvailableAmount += Double.parseDouble(receipt.getAvailableAmount());

			}
			totalAvailableAmount = new BigDecimal(totalAvailableAmount).setScale(2, RoundingMode.HALF_UP).doubleValue();
			applyCreditsDropDownVo.setAdvanceBalance(totalAvailableAmount + "");
			applyCreditsDropDownVo.setCreditNotesBalance(
					creditNotesDao.getcreditNotesAmountForCustomerCurrency(orgId, customerId, currencyId, con));
			closeResources(null, null, con);

		} catch (Exception e) {
			logger.info("Error in getApplyCreditsDropDownData");
			throw new ApplicationException(e);
		} finally {
			closeResources(null, null, con);

		}

		return applyCreditsDropDownVo;
	}

	public ApplyCreditsCustomerDropDownVo getApplyCreditsCustomerDropDownData(int organizationId)
			throws ApplicationException {
		logger.info("Entry into Method: getApplyCreditsCustomerDropDownData");
		ApplyCreditsCustomerDropDownVo applyCreditsCustomerDropDownVo = new ApplyCreditsCustomerDropDownVo();
		Connection con = null;
		try {

			con = getUserMgmConnection();
			applyCreditsCustomerDropDownVo.setCurrency(currencyDao.getMinimalCurrencyDetails(organizationId, con));
			applyCreditsCustomerDropDownVo.setVoucherEntries(organizationDao.getBasicVoucherEntries(organizationId, con,
					"Accounts Receivable-Application of Funds"));
			closeResources(null, null, con);

			con = getUserMgmConnection();
			applyCreditsCustomerDropDownVo.setCustomerDetails(customerDao.getCustomerBasicDetails(organizationId));
			BasicCurrencyVo basicCurrencyVo = currencyDao.getBasicCurrencyForOrganization(organizationId, con);
			applyCreditsCustomerDropDownVo
					.setOrganizationBasecurrencyId(basicCurrencyVo != null ? basicCurrencyVo.getId() : 0);
			List<MinimalChartOfAccountsVo> minimalChartOfAccounts = chartOfAccountsDao
					.getChartOfAccountsValuesLevel5(organizationId, con);
			List<ArInvoiceCategoryColumnDropDownVo> invoiceCategoryColumnList = new ArrayList<ArInvoiceCategoryColumnDropDownVo>();
			Map<String, Integer> ledgersMap = new HashMap<String, Integer>();
			for (MinimalChartOfAccountsVo minimalChartOfAccountsVo : minimalChartOfAccounts) {
				invoiceCategoryColumnList.add(new ArInvoiceCategoryColumnDropDownVo(minimalChartOfAccountsVo.getId(),
						minimalChartOfAccountsVo.getName()));
				ledgersMap.put(minimalChartOfAccountsVo.getName(), minimalChartOfAccountsVo.getId());
			}

			applyCreditsCustomerDropDownVo.setInvoiceAmountLedgerId(
					ledgersMap.get(ReceiptConstants.RECEIPT_DEFAULT_INVOICE_AMOUT_LEDGER) + "");
			applyCreditsCustomerDropDownVo
					.setInvoiceAmountLedgerName(ReceiptConstants.RECEIPT_DEFAULT_INVOICE_AMOUT_LEDGER);
			applyCreditsCustomerDropDownVo.setInvoiceCategoryColumnList(invoiceCategoryColumnList);

			List<ReceiptSettingsVo> customTableList = new ArrayList<ReceiptSettingsVo>();

			ReceiptSettingsVo bankCharges = new ReceiptSettingsVo();
			bankCharges.setLedgerId(ledgersMap.get(ReceiptConstants.RECEIPT_DEFAULT_BANK_CHARGES_LEDGER) + "");
			bankCharges.setcName("bankCharges");
			bankCharges.setLedgerName(ReceiptConstants.RECEIPT_DEFAULT_BANK_CHARGES_LEDGER);
			bankCharges.setColumnShow(true);
			ReceiptSettingsVo tdsDeducted = new ReceiptSettingsVo();
			tdsDeducted.setLedgerId(ledgersMap.get(ReceiptConstants.RECEIPT_DEFAULT_TDS_RECEIVABLE_LEDGER) + "");
			tdsDeducted.setcName("tdsDeducted");
			tdsDeducted.setLedgerName(ReceiptConstants.RECEIPT_DEFAULT_TDS_RECEIVABLE_LEDGER);
			tdsDeducted.setColumnShow(true);
			ReceiptSettingsVo others1 = new ReceiptSettingsVo();
			others1.setLedgerId("");
			others1.setcName("others1");
			others1.setLedgerName("");
			others1.setColumnShow(false);
			ReceiptSettingsVo others2 = new ReceiptSettingsVo();
			others2.setLedgerId("");
			others2.setcName("others2");
			others2.setLedgerName("");
			others2.setColumnShow(false);
			ReceiptSettingsVo others3 = new ReceiptSettingsVo();
			others3.setLedgerId("");
			others3.setcName("others3");
			others3.setLedgerName("");
			others3.setColumnShow(false);
			customTableList.add(bankCharges);
			customTableList.add(tdsDeducted);
			customTableList.add(others1);
			customTableList.add(others2);
			customTableList.add(others3);
			applyCreditsCustomerDropDownVo.setCustomTableList(customTableList);
			closeResources(null, null, con);

		} catch (Exception e) {
			logger.info("Error in getApplyCreditsCustomerDropDownData");
			throw new ApplicationException(e);
		} finally {
			closeResources(null, null, con);

		}

		return applyCreditsCustomerDropDownVo;
	}

	public WorkflowSettingsDropDownVo getWorkflowSettingsDropdownData(int organizationId, int moduleId)
			throws ApplicationException {
		logger.info("Entry into Method: getWorkflowSettingsDropdownData Org ID:"+organizationId+",MOdule:"+moduleId);
		WorkflowSettingsDropDownVo workflowSettingsDropDownVo = new WorkflowSettingsDropDownVo();

		Connection con = getUserMgmConnection();
		workflowSettingsDropDownVo.setUsersList(userDao.getActiveUserDetailsInAnOrganization(con, organizationId));
		closeResources(null, null, con);

		workflowSettingsDropDownVo.setRolesList(roleDao.getAllActiveRolesOfAnOrganizationByCount(organizationId));
		workflowSettingsDropDownVo.setApprovalTypes(financeCommonDao.getWorkflowApprovalTypes());
		ModuleTypeVo module = financeCommonDao.getWorkflowModuleById(moduleId);
		logger.info("MOdule:"+module);
		if (module != null && module.getName() != null
				&& (module.getName().equalsIgnoreCase(WorkflowConstants.MODULE_AP_INVOICE)
						|| module.getName().equalsIgnoreCase(WorkflowConstants.MODULE_AR_INVOICE)
						|| module.getName().equalsIgnoreCase(WorkflowConstants.MODULE_AR_CREDITNOTE)
						|| module.getName().equalsIgnoreCase(WorkflowConstants.MODULE_BANKING))) {
			List<WorkflowRuleConditionVo> conditions = financeCommonDao.getWorkflowConditions(module.getName());
			workflowSettingsDropDownVo.setCondition(conditions);
		}
		workflowSettingsDropDownVo.setVendors(vendorDao.getVendorsForOrganization(organizationId));

		con = getUserMgmConnection();
		BasicGSTLocationDetailsVo locations = organizationDao.getLocationGSTDetails(con, organizationId);
		if (locations != null && locations.getGstLocation() != null && !locations.getGstLocation().isEmpty()) {
			List<BasicLocationVo> locationList = new ArrayList<BasicLocationVo>();
			for (BasicLocationDetailsVo gstDetailsVo : locations.getGstLocation()) {
				BasicLocationVo location = new BasicLocationVo();
				location.setId(gstDetailsVo.getId());
				location.setName(gstDetailsVo.getName());
				location.setIsRegistered(gstDetailsVo.isRegistered());
				location.setGstNo(gstDetailsVo.getGstNo());
				locationList.add(location);

			}
			workflowSettingsDropDownVo.setLocations(locationList);
		}
		closeResources(null, null, con);
		workflowSettingsDropDownVo.setDefaultRuleActivated(
				workflowSettingsDao.checkWorkflowDefaultRuleExistForModule(organizationId, moduleId));

		return workflowSettingsDropDownVo;
	}

	public ModuleTypeDropDownVo getAllWorkflowModulesDropDownData() throws ApplicationException {
		logger.info("Entry into getAllWorkflowModulesDropDownData");
		ModuleTypeDropDownVo data = new ModuleTypeDropDownVo();
		try {
			data.setModules(financeCommonDao.getWorkflowModules());
		} catch (Exception e) {
			logger.info("Error in method: getAllWorkflowModulesDropDownData");
			throw new ApplicationException(e);
		}
		return data;

	}

	public BasicVendorInvoiceDropDownVo getReceiptInvoicesForVendor(int organizationId, int currencyId, int vendorId)
			throws ApplicationException {
		logger.info("Entry into Method: getReceiptInvoicesForCustomer");
		BasicVendorInvoiceDropDownVo invoiceDropdown = new BasicVendorInvoiceDropDownVo();
		invoiceDropdown
				.setInvoiceList(billsInvoiceDao.getBillReferenceNumberByOrgVendorAndStatus(organizationId, vendorId,currencyId));
		// invoiceDropdown.setVendorLedgers(chartOfAccountsDao.getLedgerBySubLedegerDescription(organizationId,
		// vendorId, "Vendor"));
		return invoiceDropdown;
	}

	public ReportsPeriodDropDownVo getReportsPeriod() throws ApplicationException {
		logger.info("Entry into method:getReportsPeriod");
		Connection con = null;
		ReportsPeriodDropDownVo data = new ReportsPeriodDropDownVo();

		try {
			con = getFinanceCommon();
			data.setReportPeriod(financeCommonDao.getReportPeriod());
			closeResources(null, null, con);

		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(null, null, con);

		}
		return data;
	}

	public PayPeriodDropDownVo getPayPeriodDropDown(int organizationId) throws ApplicationException {
		PayPeriodDropDownVo payPeriodDropDown = new PayPeriodDropDownVo();
		payPeriodDropDown.setFrequency(financeCommonDao.getPayPeriodFrequency());
		payPeriodDropDown.setPeriod(financeCommonDao.getPayPeriodFinancialYears());
		payPeriodDropDown.setCycle(payPeriodDao.getPayPeriodCycles(organizationId));
		return payPeriodDropDown;
	}

	public ReceiptDropdownVo getReceiptDropDownData(int organizationId) throws ApplicationException {
		logger.info("Entry into method: getReceiptDropDownData");
		ReceiptDropdownVo receiptDropdownVo = new ReceiptDropdownVo();
		Connection con = null;
		try {
			con = getUserMgmConnection();
			List<MinimalChartOfAccountsVo> customerLedgerList = chartOfAccountsDao
					.getAllCustomerEntityLevel5LedgersForOrg(organizationId, con);
			receiptDropdownVo.setCustomerLedgersList(customerLedgerList);
			logger.info("Successfully fetched customerLedgerList of size " + customerLedgerList.size());

			List<MinimalChartOfAccountsVo> minimalChartOfAccounts = chartOfAccountsDao
					.getChartOfAccountsValuesLevel5(organizationId, con);
			List<ArInvoiceCategoryColumnDropDownVo> invoiceCategoryColumnList = new ArrayList<ArInvoiceCategoryColumnDropDownVo>();
			Map<String, Integer> ledgersMap = new HashMap<String, Integer>();

			for (MinimalChartOfAccountsVo minimalChartOfAccountsVo : minimalChartOfAccounts) {
				invoiceCategoryColumnList.add(new ArInvoiceCategoryColumnDropDownVo(minimalChartOfAccountsVo.getId(),
						minimalChartOfAccountsVo.getName()));
				ledgersMap.put(minimalChartOfAccountsVo.getName(), minimalChartOfAccountsVo.getId());
			}

			receiptDropdownVo.setInvoiceCategoryColumnList(invoiceCategoryColumnList);
			logger.info("Successfully fetched InvoiceCategoryColumnList of size " + customerLedgerList.size());

			List<ReceiptSettingsVo> customTableList = new ArrayList<ReceiptSettingsVo>();

			ReceiptSettingsVo bankCharges = new ReceiptSettingsVo();
			// bankCharges.setLedgerId(ledgersMap.get(ReceiptConstants.RECEIPT_DEFAULT_BANK_CHARGES_LEDGER)
			// + "");
			bankCharges.setcName("Bank Charges");
			bankCharges.setcType("bankCharges");
			// bankCharges.setLedgerName(ReceiptConstants.RECEIPT_DEFAULT_BANK_CHARGES_LEDGER);
			bankCharges.setColumnShow(false);
			ReceiptSettingsVo tdsDeducted = new ReceiptSettingsVo();
			// tdsDeducted.setLedgerId(ledgersMap.get(ReceiptConstants.RECEIPT_DEFAULT_TDS_RECEIVABLE_LEDGER)
			// + "");
			tdsDeducted.setcName("TDS Deducted");
			tdsDeducted.setcType("tdsDeducted");
			// tdsDeducted.setLedgerName(ReceiptConstants.RECEIPT_DEFAULT_TDS_RECEIVABLE_LEDGER);
			tdsDeducted.setColumnShow(false);
			ReceiptSettingsVo others1 = new ReceiptSettingsVo();
			// others1.setLedgerId("");
			others1.setcName("others1");
			others1.setcType("others1");
			// others1.setLedgerName("");
			others1.setColumnShow(false);
			ReceiptSettingsVo others2 = new ReceiptSettingsVo();
			// others2.setLedgerId("");
			others2.setcName("others2");
			others2.setcType("others2");
			// others2.setLedgerName("");
			others2.setColumnShow(false);
			ReceiptSettingsVo others3 = new ReceiptSettingsVo();
			// others3.setLedgerId("");
			others3.setcName("others3");
			others3.setcType("others3");
			// others3.setLedgerName("");
			others3.setColumnShow(false);
			customTableList.add(bankCharges);
			customTableList.add(tdsDeducted);
			customTableList.add(others1);
			customTableList.add(others2);
			customTableList.add(others3);
			receiptDropdownVo.setSettingsData(customTableList);
			BasicVoucherEntriesVo basicVoucherEntriesVo = organizationDao.getBasicVoucherEntries(organizationId, con,
					"Accounts Receivable-Receipt");
			receiptDropdownVo.setReceiptNo(basicVoucherEntriesVo);
			receiptDropdownVo.setCurrency(currencyDao.getMinimalCurrencyDetails(organizationId, con));

			BasicCurrencyVo basicCurrencyVo = currencyDao.getBasicCurrencyForOrganization(organizationId, con);
			receiptDropdownVo.setCurrencyId(basicCurrencyVo != null ? basicCurrencyVo.getId() : 0);
			List<CustomerBasicDetailsVo> customerList = customerDao.getCustomerBasicDetails(organizationId);
			List<ReceiptCommonVo> customersList = new ArrayList<ReceiptCommonVo>();
			List<ReceiptCommonVo> contactList = new ArrayList<ReceiptCommonVo>();
			customerList.forEach(customer -> {
				ReceiptCommonVo vo = new ReceiptCommonVo();
				vo.setId(customer.getId());
				vo.setValue(customer.getId());
				vo.setName(customer.getName());
				vo.setCurrencyId(customer.getCurrencyId());
				vo.setType(ReceiptConstants.RECEIPT_CONTACT_TYPE_CUSTOMER);
				customersList.add(vo);
				contactList.add(vo);
			});

			receiptDropdownVo.setCustomersList(customersList);
			closeResources(null, null, con);

			con = getAccountsPayable();
			List<BasicVendorDetailsVo> vendorList = vendorDao.getAllActiveVendorsforOrg(organizationId, con);
			receiptDropdownVo.setVendorList(vendorList);
			vendorList.forEach(vendor -> {
				ReceiptCommonVo vo = new ReceiptCommonVo();
				vo.setId(vendor.getId());
				vo.setValue(vendor.getId());
				vo.setName(vendor.getName());
				vo.setCurrencyId(vendor.getCurrencyId());
				vo.setType(ReceiptConstants.RECEIPT_CONTACT_TYPE_VENDOR);
				contactList.add(vo);
			});

			List<StatutoryBodyVo> statutoryBodies = statutoryBodyDao
					.getBasicStatutoryBodiesOfAnOrganization(organizationId);
			statutoryBodies.forEach(statutoryBody -> {
				ReceiptCommonVo vo = new ReceiptCommonVo();
				vo.setId(statutoryBody.getId());
				vo.setValue(statutoryBody.getId());
				vo.setName(statutoryBody.getName());
				vo.setType(ReceiptConstants.RECEIPT_CONTACT_TYPE_STATUTORY_BODY);
				contactList.add(vo);
			});

			List<BasicEmployeeVo> employees = employeeDao.getAllActiveEmployees(organizationId, con);
			employees.forEach(statutoryBody -> {
				ReceiptCommonVo vo = new ReceiptCommonVo();
				vo.setId(statutoryBody.getId());
				vo.setValue(statutoryBody.getId());
				vo.setName(statutoryBody.getName());
				vo.setType(ReceiptConstants.RECEIPT_CONTACT_TYPE_EMPLOYEE);
				contactList.add(vo);
			});
			receiptDropdownVo.setContactList(contactList);
			closeResources(null, null, con);

			List<BankMasterAccountBaseVo> bankAccounts = bankMasterDao
					.getAllBankMasterDetailsOfAnOrganization(organizationId);
			for (BankMasterAccountBaseVo bankMasterAccountBaseVo : bankAccounts) {
				bankMasterAccountBaseVo.setName(bankMasterAccountBaseVo.getAccountName());
				bankMasterAccountBaseVo.setValue(bankMasterAccountBaseVo.getId() + "");
			}

			receiptDropdownVo.setBankList(bankAccounts);
			receiptDropdownVo.setReceiptTypesList(financeCommonDao.getReceiptTypes());

		} catch (Exception e) {
			logger.error("Error while retrieving getRefundDropDownData:", e);
			throw new ApplicationException(e);
		}
		return receiptDropdownVo;
	}

	public PayRunPeriodDropDownVo getPayRunReportsPeriod(Integer orgId) throws ApplicationException {
		logger.info("Entry into method:getReportsPeriod");
		Connection con = null;

		PayRunPeriodDropDownVo data = new PayRunPeriodDropDownVo();

		try {
			con = getAccountsPayable();
			data.setPayRunPeriod(payRunDao.getReportPeriod(orgId, con));

			closeResources(null, null, con);

		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(null, null, con);

		}
		return data;
	}

	public PayRunPaymentTypeDropDownVo getPayRunPaymentType(int orgId) throws ApplicationException {
		logger.info("Entry into getPayRunPaymentType");
		Connection con = null;
		PayRunPaymentTypeDropDownVo data = new PayRunPaymentTypeDropDownVo();
		try {
			con = getAccountsPayable();
			data.setData(payRunDao.getPayRunPaymentType(orgId, con));
			closeResources(null, null, con);
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(null, null, con);

		}
		return data;
	}

	public PayRunPaymentCycleDropDownVo getPayRunCycleType(int orgId) throws ApplicationException {
		logger.info("Entry into method: getPayRunCycleType");
		Connection con = null;

		PayRunPaymentCycleDropDownVo data = new PayRunPaymentCycleDropDownVo();

		try {
			con = getAccountsPayable();
			data.setData(payRunDao.getPayRunCycleType(orgId, con));

			closeResources(null, null, con);

		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(null, null, con);

		}
		return data;
	}

	public PayRunEmployeeDropDownVo getPayRunEmployees(int orgId) throws ApplicationException {
		logger.info("Entry into method: getPayRunEmployees");
		Connection con = null;

		PayRunEmployeeDropDownVo data = new PayRunEmployeeDropDownVo();

		try {
			con = getAccountsPayable();
			data.setData(payRunDao.getPayRunEmployees(orgId, con));

			closeResources(null, null, con);

		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(null, null, con);

		}
		return data;
	}

	public BasicVoucherEntriesVo getPayRunReferenceNumberSpec(int organizationId) throws ApplicationException {
		Connection con =null;
		BasicVoucherEntriesVo payRunVoucher =null;
		try {
			con =  getUserMgmConnection();
			payRunVoucher = organizationDao.getBasicVoucherEntries(organizationId, con,PaymentNonCoreConstants.PAYROLL_PAYRUN);
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(null, null, con);

		}
		
		
		return payRunVoucher;
	}

	public PayRunTableDropDownVo getPayRunTableDropDownData(Integer organizationId) throws ApplicationException {
		Connection conn = null;
		PayRunTableDropDownVo data = new PayRunTableDropDownVo();
		
		try {
			conn = getPayrollConnection();
			data.setData(payRunDao.getPayRunTableDropdownDAO(organizationId,conn));
		}catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(null, null, conn);
		}
		
		return data;
	}

	
	public List<ProductCategoryVo> getProductCategoryByType(String type, int organizationId) throws ApplicationException {
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null ;
		List<ProductCategoryVo> categoryVos = new ArrayList<ProductCategoryVo>();
		try {
			con =  getInventoryMgmtConnection();
			preparedStatement = con.prepareStatement(SettingsAndPreferencesConstants.GET_PRODUCT_CATEGORY);
			preparedStatement.setInt(1, organizationId);
			preparedStatement.setString(2, type);
			resultSet = preparedStatement.executeQuery();
			while(resultSet.next()) {
				ProductCategoryVo vo = new ProductCategoryVo();
				vo.setId(resultSet.getInt(1));
				vo.setCategory(resultSet.getString(2));
				vo.setType(resultSet.getString(3));
				categoryVos.add(vo);
			}
			logger.info("Categories:::"+ categoryVos.size());
		}catch(Exception e){
			logger.info("Error in method:createProductCategory");
			throw new ApplicationException(e);
		} finally {
			closeResources(resultSet, preparedStatement, con);
		}
		return categoryVos;
		
	}
	
	public BeneficiaryDropDownVo getYesBankBeneficiaryDropDown(Integer orgId) {
		BeneficiaryDropDownVo dropDownVo = new BeneficiaryDropDownVo();
		List<BeneficiaryTypeVo> beneficiaryTypes = new ArrayList<BeneficiaryTypeVo>();
		List<AccountTypeVo> accountTypes = new ArrayList<AccountTypeVo>();
		BeneficiaryTypeVo corporateType = new BeneficiaryTypeVo();
		corporateType.setId(1);
		corporateType.setName(YesBankConstants.BENEFICIARY_TYPE_CORPORATE_COMPANY);
		corporateType.setValue(YesBankConstants.BENEFICIARY_TYPE_CORPORATE_COMPANY);
		beneficiaryTypes.add(corporateType);
		BeneficiaryTypeVo individalType = new BeneficiaryTypeVo();
		individalType.setId(2);
		individalType.setName(YesBankConstants.BENEFICIARY_TYPE_INDIVIDUAL);
		individalType.setValue(YesBankConstants.BENEFICIARY_TYPE_INDIVIDUAL);
		beneficiaryTypes.add(individalType);
		BeneficiaryTypeVo otherType = new BeneficiaryTypeVo();
		otherType.setId(3);
		otherType.setName(YesBankConstants.BENEFICIARY_TYPE_OTHERS);
		otherType.setValue(YesBankConstants.BENEFICIARY_TYPE_OTHERS);
		beneficiaryTypes.add(otherType);
		AccountTypeVo withinBankType = new AccountTypeVo();
		withinBankType.setId(1);
		withinBankType.setName(YesBankConstants.ACCOUNT_TYPE_WITHIN_BANK);
		withinBankType.setValue(YesBankConstants.ACCOUNT_TYPE_WITHIN_BANK);
		accountTypes.add(withinBankType);
		AccountTypeVo otherBankType = new AccountTypeVo();
		otherBankType.setId(2);
		otherBankType.setName(YesBankConstants.ACCOUNT_TYPE_OTHER_BANK);
		otherBankType.setValue(YesBankConstants.ACCOUNT_TYPE_OTHER_BANK);
		accountTypes.add(otherBankType);
		dropDownVo.setAccountTypes(accountTypes);
		dropDownVo.setBeneficiaryTypes(beneficiaryTypes);
		return dropDownVo;
	}


	public List<CommonVo> getRejectionTypesDropDown() throws ApplicationException {
		
		return financeCommonDao.getRejectionTypes();
	}
	
}

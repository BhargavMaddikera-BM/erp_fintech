package com.blackstrawai.keycontact;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.ap.dropdowns.BasicCustomerVo;
import com.blackstrawai.ap.dropdowns.PaymentTypeVo;
import com.blackstrawai.ar.applycredits.CustomerBasicDetailsVo;
import com.blackstrawai.attachments.AttachmentsConstants;
import com.blackstrawai.attachments.AttachmentsDao;
import com.blackstrawai.attachments.AttachmentsVo;
import com.blackstrawai.attachments.UploadFileVo;
import com.blackstrawai.common.BaseAddressVo;
import com.blackstrawai.common.BaseDao;
import com.blackstrawai.common.BookKeepingSettingsVo;
import com.blackstrawai.common.CommonConstants;
import com.blackstrawai.common.FinanceCommonDao;
import com.blackstrawai.export.BankExportVo;
import com.blackstrawai.export.ContactsExportVo;
import com.blackstrawai.export.CustomerExportVo;
import com.blackstrawai.export.ExportVo;
import com.blackstrawai.keycontact.customer.CustomerBankDetailsVo;
import com.blackstrawai.keycontact.customer.CustomerBillingAddressVo;
import com.blackstrawai.keycontact.customer.CustomerContactsVo;
import com.blackstrawai.keycontact.customer.CustomerDeliveryAddressVo;
import com.blackstrawai.keycontact.customer.CustomerFinanceVo;
import com.blackstrawai.keycontact.customer.CustomerPrimaryInfoVo;
import com.blackstrawai.keycontact.customer.CustomerVo;
import com.blackstrawai.settings.CurrencyDao;
import com.blackstrawai.settings.CustomerGroupDao;
import com.blackstrawai.settings.PaymentTermsDao;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mysql.jdbc.Statement;

@Repository
public class CustomerDao extends BaseDao {

	private Logger logger = Logger.getLogger(CustomerDao.class);

	@Autowired
	private AttachmentsDao attachmentsDao;

	/*@Autowired
	private ChartOfAccountsDao chartOfAccountsDao;*/

	@Autowired
	private VendorDao vendorDao;

	@Autowired
	private FinanceCommonDao financeCommonDao;

	@Autowired
	private PaymentTermsDao paymentTermsDao;

	@Autowired
	private CurrencyDao currencyDao;

	@Autowired
	private CustomerGroupDao customerGroupDao;

	// Create New Customer
	public boolean createCustomer(CustomerVo customerVo) throws ApplicationException, SQLException {
		boolean isCreatedSuccessfully = false;
		logger.info("To create a new Customer in CustomerDao:::" + customerVo);
		Connection connection = null;
		if (customerVo != null) {
			try {
				connection = getAccountsReceivableConnection();
				connection.setAutoCommit(false);
				if (customerVo.getPrimaryInfo() != null) {
					createCustomerGeneralInfo(customerVo, connection);
				}
				if (customerVo.getId() != null) {
					if (customerVo.getBillingAddress() != null) {
						createCustomerBillingAddress(customerVo.getBillingAddress(), connection, customerVo.getId());
					}
					if (customerVo.getDeliveryAddress() != null) {
						createCustomerDeliveryAddress(customerVo.getDeliveryAddress(), connection, customerVo.getId());
					}
					if (customerVo.getContacts() != null) {
						for (CustomerContactsVo customer : customerVo.getContacts()) {
							createContactDetails(customer, connection, customerVo.getId());
						}
					}
					if (customerVo.getBankDetails() != null) {
						for (CustomerBankDetailsVo bankDetail : customerVo.getBankDetails()) {
							createBankDetails(bankDetail, connection, customerVo.getId());
						}
					}
					if (customerVo.getCustomerAdditionalAddresses() != null && customerVo.getCustomerAdditionalAddresses().size() > 0) {
						createCustomerAdditionalAddresses(customerVo.getId(), customerVo, connection);
					}
					if (customerVo.getCustomerFinance() != null ) {
						createCustomerFinance(customerVo.getId(), customerVo, connection);
					}

					if (customerVo.getAttachments() != null && customerVo.getAttachments().size() > 0) {
						attachmentsDao.createAttachments(customerVo.getOrganizationId(), customerVo.getUserId(),
								customerVo.getAttachments(), AttachmentsConstants.MODULE_TYPE_CUSTOMER,
								customerVo.getId(), customerVo.getRoleName());
					}
				}
				connection.commit();
				isCreatedSuccessfully = true;
				logger.info("Succecssfully created customer in CustomerDao");
			} catch (Exception e) {
				logger.error("Error in createCustomer:: ", e);
				isCreatedSuccessfully = false;
				connection.rollback();
				throw new ApplicationException(e.getMessage());
			} finally {
				closeResources(null, null, connection);
			}
		}
		return isCreatedSuccessfully;
	}



	private void createCustomerAdditionalAddresses(int customerId, CustomerVo customerVo, Connection con) throws ApplicationException {
		logger.info("Entry into method: createCustomerAdditionalAddresses");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			String sql = CustomerConstants.INSERT_INTO_CUSTOMER_ADDITIONAL_ADDRESS;
			for (int i = 0; i < customerVo.getCustomerAdditionalAddresses().size(); i++) {
				BaseAddressVo baseAddressVo = customerVo.getCustomerAdditionalAddresses().get(i);

				preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				preparedStatement.setString(1,
						baseAddressVo.getAttention() != null
						? baseAddressVo.getAttention()
								: null);
				preparedStatement.setObject(2,
						baseAddressVo.getCountry() != null
						? baseAddressVo.getCountry()
								: null);
				preparedStatement.setString(3,
						baseAddressVo.getAddress_1() != null
						? baseAddressVo.getAddress_1()
								: null);
				preparedStatement.setString(4,
						baseAddressVo.getAddress_2() != null
						? baseAddressVo.getAddress_2()
								: null);
				preparedStatement.setObject(5,
						baseAddressVo.getState() != null ? baseAddressVo.getState()
								: null);
				preparedStatement.setString(6,
						baseAddressVo.getCity() != null ? baseAddressVo.getCity()
								: null);
				preparedStatement.setString(7,
						baseAddressVo.getZipCode() != null
						? baseAddressVo.getZipCode()
								: null);
				preparedStatement.setString(8,
						baseAddressVo.getPhoneNo() != null
						? baseAddressVo.getPhoneNo()
								: null);
				preparedStatement.setString(9,
						baseAddressVo.getLandMark() != null
						? baseAddressVo.getLandMark()
								: null);
				preparedStatement.setInt(10, customerId);
				preparedStatement.setInt(11, baseAddressVo.getSelectedAddress() != null
						? baseAddressVo.getSelectedAddress()
								: 0);
				preparedStatement.setString(12, 	baseAddressVo.getGstNo() != null
						? baseAddressVo.getGstNo()
								: null);
				preparedStatement.setInt(13, 	baseAddressVo.getAddressType() != null
						? baseAddressVo.getAddressType()
								: 0);
				int rowAffected = preparedStatement.executeUpdate();
				if (rowAffected == 1) {
					rs = preparedStatement.getGeneratedKeys();
					if (rs.next()) {
						baseAddressVo.setId(rs.getInt(1));
					}
				}
			}
		} catch (Exception e) {
			logger.error("Exception in createCustomerAdditionalAddresses",e);
			throw new ApplicationException(e.getMessage());

		} finally {
			closeResources(rs, preparedStatement, null);

		}

	}

	private void createCustomerFinance(int customerId, CustomerVo customerVo, Connection con) throws ApplicationException {
		logger.info("Entry into method:createCustomerFinance");
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = con.prepareStatement(CustomerConstants.INSERT_INTO_CUSTOMER_FINANCE);
			preparedStatement.setInt(1,
					customerVo.getCustomerFinance().getCurrencyId() != null ? customerVo.getCustomerFinance().getCurrencyId()
							: null);
			preparedStatement.setInt(2,
					customerVo.getCustomerFinance().getPaymentTermsid() != null
					? customerVo.getCustomerFinance().getPaymentTermsid()
							: null);
			preparedStatement.setInt(3,
					customerVo.getCustomerFinance().getTdsId() != null ? customerVo.getCustomerFinance().getTdsId() : null);
			preparedStatement.setString(4,
					customerVo.getCustomerFinance().getOpeningBalance() != null
					? customerVo.getCustomerFinance().getOpeningBalance()
							: null);
			preparedStatement.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
			preparedStatement.setInt(6, customerId);
			preparedStatement.executeUpdate();
		} catch (Exception e) {
			logger.error("Exception in createCustomerFinance:",e);
			throw new ApplicationException(e.getMessage());
		}finally{
			closeResources(null, preparedStatement, null);
		}
	}


	/*public void insertIntoChartOfAccountsLevel6(int organizationId, String name, String userId, boolean isSuperAdmin,
			String displayName) throws ApplicationException {

		int level5Id = chartOfAccountsDao.getChartOfAccountsLevel5Id(organizationId, "Trade Receivable",
				"Trade receivables outstanding for a period exceeding six months from the date they were due for payment - Secured Considered good",
				"Sundry Debtor - Secured >6M");
		if (level5Id > 0) {
			chartOfAccountsDao.createLevel6(name, "Customer", organizationId, userId, isSuperAdmin, level5Id, false,
					displayName);
		}

		level5Id = chartOfAccountsDao.getChartOfAccountsLevel5Id(organizationId, "Trade Receivable",
				"Trade receivables outstanding for a period exceeding six months from the date they were due for payment - UnSecured Considered good",
				"Sundry Debtor - UnSecured >6M");
		if (level5Id > 0) {
			chartOfAccountsDao.createLevel6(name, "Customer", organizationId, userId, isSuperAdmin, level5Id, false,
					displayName);
		}

		level5Id = chartOfAccountsDao.getChartOfAccountsLevel5Id(organizationId, "Trade Receivable",
				"Trade receivables outstanding for a period exceeding six months from the date they were due for payment - Doubtful",
				"Sundry Debtor - Doubtful >6M");
		if (level5Id > 0) {
			chartOfAccountsDao.createLevel6(name, "Customer", organizationId, userId, isSuperAdmin, level5Id, false,
					displayName);
		}

		level5Id = chartOfAccountsDao.getChartOfAccountsLevel5Id(organizationId, "Trade Receivable",
				"Other Trade Receivable - Secured Considered good", "Sundry Debtor - Secured");
		if (level5Id > 0) {
			chartOfAccountsDao.createLevel6(name, "Customer", organizationId, userId, isSuperAdmin, level5Id, false,
					displayName);
		}

		level5Id = chartOfAccountsDao.getChartOfAccountsLevel5Id(organizationId, "Trade Receivable",
				"Other Trade Receivable - UnSecured Considered good", "Sundry Debtor - UnSecured");
		if (level5Id > 0) {
			chartOfAccountsDao.createLevel6(name, "Customer", organizationId, userId, isSuperAdmin, level5Id, false,
					displayName);
		}

		level5Id = chartOfAccountsDao.getChartOfAccountsLevel5Id(organizationId, "Trade Receivable",
				"Other Trade Receivable - Doubtful", "Sundry Debtor - Doubtful");
		if (level5Id > 0) {
			chartOfAccountsDao.createLevel6(name, "Customer", organizationId, userId, isSuperAdmin, level5Id, false,
					displayName);
		}

		level5Id = chartOfAccountsDao.getChartOfAccountsLevel5Id(organizationId, "Trade Receivable",
				"Provision for Doubtful Debt", "Provision for Doubtful Debt");
		if (level5Id > 0) {
			chartOfAccountsDao.createLevel6(name, "Customer", organizationId, userId, isSuperAdmin, level5Id, false,
					displayName);
		}

		level5Id = chartOfAccountsDao.getChartOfAccountsLevel5Id(organizationId, "Trade Receivable",
				"Trade receivables outstanding for a period exceeding six months from the date they were due for payment - Secured Considered good",
				"Sundry Debtors - JV");
		if (level5Id > 0) {
			chartOfAccountsDao.createLevel6(name, "Customer", organizationId, userId, isSuperAdmin, level5Id, false,
					displayName);
		}

		level5Id = chartOfAccountsDao.getChartOfAccountsLevel5Id(organizationId, "Trade Receivable",
				"Provision for Doubtful Debt", "Provision for Doubtful Debt - JV");
		if (level5Id > 0) {
			chartOfAccountsDao.createLevel6(name, "Customer", organizationId, userId, isSuperAdmin, level5Id, false,
					displayName);
		}

		level5Id = chartOfAccountsDao.getChartOfAccountsLevel5Id(organizationId, "Other Current Assets",
				"Unbilled revenue", "Unbilled revenue");
		if (level5Id > 0) {
			chartOfAccountsDao.createLevel6(name, "Customer", organizationId, userId, isSuperAdmin, level5Id, false,
					displayName);
		}

		level5Id = chartOfAccountsDao.getChartOfAccountsLevel5Id(organizationId, "Other non-current assets",
				"Long-term trade receivables", "Long-term trade receivables");
		if (level5Id > 0) {
			chartOfAccountsDao.createLevel6(name, "Customer", organizationId, userId, isSuperAdmin, level5Id, false,
					displayName);
		}

		level5Id = chartOfAccountsDao.getChartOfAccountsLevel5Id(organizationId, "Long-term borrowings",
				"Loans and advances from related parties - Secured",
				"NC - Loans and advances from related parties - Secured");
		if (level5Id > 0) {
			chartOfAccountsDao.createLevel6(name, "Customer", organizationId, userId, isSuperAdmin, level5Id, false,
					displayName);
		}

		level5Id = chartOfAccountsDao.getChartOfAccountsLevel5Id(organizationId, "Long-term borrowings",
				"Loans and advances from related parties - UnSecured",
				"NC - Loans and advances from related parties - UnSecured");
		if (level5Id > 0) {
			chartOfAccountsDao.createLevel6(name, "Customer", organizationId, userId, isSuperAdmin, level5Id, false,
					displayName);
		}

		level5Id = chartOfAccountsDao.getChartOfAccountsLevel5Id(organizationId, "Long-term borrowings",
				"Other loans and advances - Secured", "Other NC loans and advances - Secured");
		if (level5Id > 0) {
			chartOfAccountsDao.createLevel6(name, "Customer", organizationId, userId, isSuperAdmin, level5Id, false,
					displayName);
		}

		level5Id = chartOfAccountsDao.getChartOfAccountsLevel5Id(organizationId, "Long-term borrowings",
				"Other loans and advances - UnSecured", "Other NC loans and advances - UnSecured");
		if (level5Id > 0) {
			chartOfAccountsDao.createLevel6(name, "Customer", organizationId, userId, isSuperAdmin, level5Id, false,
					displayName);
		}

		level5Id = chartOfAccountsDao.getChartOfAccountsLevel5Id(organizationId, "Other long-term liabilities",
				"Others", "NC -  Trade / security deposits received");
		if (level5Id > 0) {
			chartOfAccountsDao.createLevel6(name, "Customer", organizationId, userId, isSuperAdmin, level5Id, false,
					displayName);
		}

		level5Id = chartOfAccountsDao.getChartOfAccountsLevel5Id(organizationId, "Other long-term liabilities",
				"Others", "NC -  Advances from customers");
		if (level5Id > 0) {
			chartOfAccountsDao.createLevel6(name, "Customer", organizationId, userId, isSuperAdmin, level5Id, false,
					displayName);
		}

		level5Id = chartOfAccountsDao.getChartOfAccountsLevel5Id(organizationId, "Short-term borrowings",
				"Other loans and advances -Secured", "Other loans and advances -Secured");
		if (level5Id > 0) {
			chartOfAccountsDao.createLevel6(name, "Customer", organizationId, userId, isSuperAdmin, level5Id, false,
					displayName);
		}

		level5Id = chartOfAccountsDao.getChartOfAccountsLevel5Id(organizationId, "Short-term borrowings",
				"Other loans and advances -unsecured", "Other loans and advances -unsecured");
		if (level5Id > 0) {
			chartOfAccountsDao.createLevel6(name, "Customer", organizationId, userId, isSuperAdmin, level5Id, false,
					displayName);
		}

	}*/

	// To get A customer based on Id
	public CustomerVo getCustomerById(Integer custId) throws ApplicationException {
		logger.info("To get Customer in CustomerDao");
		Connection connection = null;
		CustomerVo customerVo = null;
		try {
			if (custId != null) {
				connection = getAccountsReceivableConnection();
				customerVo = getCustomerPrimaryInfo(custId, connection);
				if (customerVo != null) {
					customerVo.setBillingAddress(
							getCustomerBillingAddress(customerVo.getBillingAddress(), custId, connection));
					customerVo.setDeliveryAddress(getCustomerDeliveryAddress(custId, connection));
					customerVo.setContacts(getCustomerContactDetails(custId, connection));
					customerVo.setBankDetails(getCustomerBankDetails(custId, connection));
					customerVo.setCustomerAdditionalAddresses(getCustomerAdditionalAddressDetailsByCustomerId(connection, custId));
					customerVo.setCustomerFinance(getCustomerFinanceDetails(connection, custId));
					List<UploadFileVo> uploadFileVos = new ArrayList<UploadFileVo>();
					for (AttachmentsVo attachments : attachmentsDao.getAttachmentDetails(custId,
							AttachmentsConstants.MODULE_TYPE_CUSTOMER)) {
						UploadFileVo uploadFileVo = new UploadFileVo();
						uploadFileVo.setId(attachments.getId());
						uploadFileVo.setName(attachments.getFileName());
						uploadFileVo.setSize(attachments.getSize());
						uploadFileVos.add(uploadFileVo);
					}
					customerVo.setAttachments(uploadFileVos);
				}
			}
			logger.info("Successfully fetched customer in CustomerDao ");
		} catch (Exception e) {
			logger.info("Error in createCustomer:: ", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(null, null, connection);
		}
		return customerVo;
	}

	// To get Custommer List
	public List<CustomerVo> getCustomerList(Integer orgId) throws ApplicationException {
		List<CustomerVo> customerList = null;
		logger.info("To fetch customerList ");
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			con = getAccountsReceivableConnection();
			preparedStatement = con.prepareStatement(CustomerConstants.GET_CUSTOMER_LIST);
			preparedStatement.setInt(1, orgId);
			rs = preparedStatement.executeQuery();
			customerList = new ArrayList<CustomerVo>();
			while (rs.next()) {
				CustomerVo vo = new CustomerVo();
				CustomerPrimaryInfoVo primaryInfoVo = new CustomerPrimaryInfoVo();
				CustomerBillingAddressVo addressVo = new CustomerBillingAddressVo();
				vo.setId(rs.getInt(1));
				primaryInfoVo.setCompanyName(rs.getString(2));
				primaryInfoVo.setCustomerDisplayName(rs.getString(3));
				primaryInfoVo.setPrimaryContact(rs.getString(4));
				primaryInfoVo.setPhoneNo(rs.getString(5));
				primaryInfoVo.setMobileNo(rs.getString(6));
				addressVo.setCity(rs.getString(7));
				vo.setStatus(rs.getString(8));
				vo.setPrimaryInfo(primaryInfoVo);
				vo.setBillingAddress(addressVo);
				customerList.add(vo);
			}
			logger.info("Successfully fetched customerList of size " + customerList.size());
		} catch (Exception e) {
			logger.info("Error in fetching customerList ", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return customerList;
	}

	public List<CustomerVo> getCustomerListForUserAndRole(Integer orgId, String userId, String roleName)
			throws ApplicationException {
		List<CustomerVo> customerList = null;
		logger.info("To fetch customerList ");
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			con = getAccountsReceivableConnection();
			if (roleName.equals("Super Admin")) {
				preparedStatement = con.prepareStatement(CustomerConstants.GET_CUSTOMER_ORGANIZATION);
			} else {
				preparedStatement = con.prepareStatement(CustomerConstants.GET_CUSTOMER_LIST_USER_ROLE);
			}
			preparedStatement.setInt(1, orgId);
			if (!(roleName.equals("Super Admin"))) {
				preparedStatement.setString(2, userId);
				preparedStatement.setString(3, roleName);
			}
			rs = preparedStatement.executeQuery();
			customerList = new ArrayList<CustomerVo>();
			while (rs.next()) {
				CustomerVo vo = new CustomerVo();
				CustomerPrimaryInfoVo primaryInfoVo = new CustomerPrimaryInfoVo();
				CustomerBillingAddressVo addressVo = new CustomerBillingAddressVo();
				vo.setId(rs.getInt(1));
				primaryInfoVo.setCompanyName(rs.getString(2));
				primaryInfoVo.setCustomerDisplayName(rs.getString(3));
				primaryInfoVo.setPrimaryContact(rs.getString(4));
				primaryInfoVo.setPhoneNo(rs.getString(5));
				primaryInfoVo.setMobileNo(rs.getString(6));
				addressVo.setCity(rs.getString(7));
				vo.setStatus(rs.getString(8));
				vo.setPrimaryInfo(primaryInfoVo);
				vo.setBillingAddress(addressVo);
				customerList.add(vo);
			}
			logger.info("Successfully fetched customerList of size " + customerList.size());
		} catch (Exception e) {
			logger.info("Error in fetching customerList ", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return customerList;
	}

	/*
	 * // To delete customer table entries public void
	 * deleteCustomerEntries(Integer id) throws ApplicationException {
	 * logger.info("To deleteCustomerEntries:: "); Connection connection = null;
	 * if (id != null) { try { connection = getAccountsReceivableConnection();
	 * // To remove from Customer info table changeStatusForCustomerTables(id,
	 * CommonConstants.STATUS_AS_DELETE, connection,
	 * CustomerConstants.MODIFY_CUSTOMER_STATUS); // To remove from Customer
	 * Contacts table changeStatusForCustomerTables(id,
	 * CommonConstants.STATUS_AS_DELETE, connection,
	 * CustomerConstants.MODIFY_CUSTOMER_CONTACT_STATUS); // To remove from
	 * Customer Bank table changeStatusForCustomerTables(id,
	 * CommonConstants.STATUS_AS_DELETE, connection,
	 * CustomerConstants.MODIFY_CUSTOMER_BANK_INFO_STATUS); // To remove from
	 * Attachments table attachmentsDao.changeStatusForAttachments(id,
	 * CommonConstants.STATUS_AS_DELETE,
	 * AttachmentsConstants.MODULE_TYPE_CUSTOMER); logger.info(
	 * "Deleted successfully in all tables "); } catch (Exception e) {
	 * logger.info("Error in deleteCustomerEntries:: ", e); throw new
	 * ApplicationException(e); } finally { closeResources(null, null,
	 * connection); } }
	 * 
	 * }
	 */

	// to activate or deactivate the customer
	public void activateOrDeactivateCustomer(Integer id, String status, String userId, String roleName)
			throws ApplicationException {
		Connection connection = null;
		logger.info("Entry to activateOrDeactivateCustomer ");
		if (id != null && status != null) {
			String statusToUpdate = null;
			if (status.equalsIgnoreCase(CommonConstants.STATUS_AS_ACTIVE)) {
				statusToUpdate = CommonConstants.STATUS_AS_ACTIVE;
			} else if (status.equalsIgnoreCase(CommonConstants.STATUS_AS_INACTIVE)) {
				statusToUpdate = CommonConstants.STATUS_AS_INACTIVE;
			}
			logger.info("Status to update is ::  " + statusToUpdate);
			if (statusToUpdate != null) {
				try {
					connection = getAccountsReceivableConnection();
					// To remove from Customer info table
					changeStatusForCustomerTables(id, statusToUpdate, connection,
							CustomerConstants.MODIFY_CUSTOMER_STATUS, userId, roleName);
				} catch (Exception e) {
					logger.info("Error in activateOrDeactivateCustomerEntries:: ", e);
					throw new ApplicationException(e);
				} finally {
					closeResources(null, null, connection);
				}
			}
		}

	}

	// To update the customer
	public boolean updateCustomer(CustomerVo customerVo) throws ApplicationException, SQLException {
		logger.info("To Update a Custoemr in CustomerDao");
		Connection connection = null;
		boolean isTxnSuccess = false;
		if (customerVo.getId() != null) {
			try {
				connection = getAccountsReceivableConnection();
				connection.setAutoCommit(false);
				if (customerVo.getContactsToRemove() != null && customerVo.getContactsToRemove().size() > 0) {
					for (Integer id : customerVo.getContactsToRemove()) {
						deleteCustomerContacts(id, connection);
					}
				}
				if (customerVo.getBankDetailsToRemove() != null && customerVo.getBankDetailsToRemove().size() > 0) {
					for (Integer id : customerVo.getBankDetailsToRemove()) {
						deleteCustomerBankDetails(id, connection);
					}
				}
				if (customerVo.getPrimaryInfo() != null) {
					updateCustomerGeneralInfo(customerVo, connection);
				}
				if (customerVo.getBillingAddress() != null) {
					updateCustomerBillingAddress(customerVo.getBillingAddress(), customerVo.getId(), connection);
				}
				if (customerVo.getDeliveryAddress() != null) {
					updateCustomerDeliveryAddress(customerVo.getDeliveryAddress(), customerVo.getId(), connection);
				}
				if (customerVo.getCustomerAdditionalAddresses() != null && customerVo.getCustomerAdditionalAddresses().size() > 0) {
					deleteCustomerAdditionalAddress(customerVo.getId(),connection);
					createCustomerAdditionalAddresses(customerVo.getId(), customerVo, connection);
				}

				if (customerVo.getContacts() != null && customerVo.getContacts().size() > 0) {
					for (CustomerContactsVo contact : customerVo.getContacts()) {
						if (contact.getStatus() != null) {
							switch (contact.getStatus().toUpperCase()) {
							case CommonConstants.STATUS_AS_NEW:
								createContactDetails(contact, connection, customerVo.getId());
								break;
							case CommonConstants.STATUS_AS_ACTIVE:
								updateContactDetails(contact, connection);
								break;
							case CommonConstants.STATUS_AS_DELETE:
								changeStatusForCustomerTables(contact.getId(), CommonConstants.STATUS_AS_DELETE,
										connection, CustomerConstants.MODIFY_CUSTOMER_SINGLE_CONTACT_STATUS);
								break;
							}
						}
					}
				}
				if (customerVo.getBankDetails() != null && customerVo.getBankDetails().size() > 0) {
					for (CustomerBankDetailsVo bankDetail : customerVo.getBankDetails()) {
						if (bankDetail.getStatus() != null) {
							switch (bankDetail.getStatus().toUpperCase()) {
							case CommonConstants.STATUS_AS_NEW:
								Boolean isExist = isBankDetailExist(customerVo.getId(), bankDetail.getAccountNumber(),
										bankDetail.getIfscCode(), connection);
								if (isExist) {
									throw new ApplicationException(
											CustomerConstants.CUSTOMER_BANK_DETAILS_ALREADY_EXISTS);
								}
								createBankDetails(bankDetail, connection, customerVo.getId());
								break;
							case CommonConstants.STATUS_AS_ACTIVE:
								updateBankDetails(bankDetail, connection, bankDetail.getId());
								break;
							case CommonConstants.STATUS_AS_DELETE:
								changeStatusForCustomerTables(bankDetail.getId(), CommonConstants.STATUS_AS_DELETE,
										connection, CustomerConstants.MODIFY_CUSTOMER_SINGLE_BANK_INFO_STATUS);
								break;
							}
						}
					}
				}
				if (customerVo.getAttachments() != null && customerVo.getAttachments().size() > 0) {
					attachmentsDao.createAttachments(customerVo.getOrganizationId(), customerVo.getUserId(),
							customerVo.getAttachments(), AttachmentsConstants.MODULE_TYPE_CUSTOMER, customerVo.getId(),
							customerVo.getRoleName());
				}

				if (customerVo.getAttachmentsToRemove() != null && customerVo.getAttachmentsToRemove().size() > 0) {
					for (Integer attachmentId : customerVo.getAttachmentsToRemove()) {
						attachmentsDao.changeStatusForSingleAttachment(attachmentId, CommonConstants.STATUS_AS_DELETE,
								customerVo.getUserId(), customerVo.getRoleName());
					}
				}
				connection.commit();
				isTxnSuccess = true;
			} catch (Exception e) {
				logger.info("Error in updateCustomer:: ", e);
				connection.rollback();
				isTxnSuccess = false;
				throw new ApplicationException(e.getMessage());
			} finally {
				closeResources(null, null, connection);
			}
		}
		return isTxnSuccess;
	}

	private CustomerPrimaryInfoVo createCustomerGeneralInfo(CustomerVo customerVo, Connection con)
			throws ApplicationException {
		logger.info("Entry into createCustomerGeneralInfo in DAO");
		if (customerVo != null && customerVo.getPrimaryInfo() != null && customerVo.getUserId() != null) {
			if (customerVo.getPrimaryInfo().getEmailAddress() != null) {
				Boolean isEmailExist = isEmailAlreadyExistForOrg(customerVo.getOrganizationId(),
						customerVo.getPrimaryInfo().getEmailAddress());
				if (isEmailExist != null && isEmailExist) {
					throw new ApplicationException(CustomerConstants.EMAIL_ALREADY_EXISTS);
				}
			}
			PreparedStatement preparedStatement = null;
			ResultSet rs = null;
			try {
				preparedStatement = con.prepareStatement(CustomerConstants.INSERT_INTO_CUSTOMER_GENERAL_INFO,
						Statement.RETURN_GENERATED_KEYS);
				preparedStatement.setString(1, customerVo.getPrimaryInfo().getPrimaryContact() != null
						? customerVo.getPrimaryInfo().getPrimaryContact() : null);
				preparedStatement.setString(2, customerVo.getPrimaryInfo().getCompanyName() != null
						? customerVo.getPrimaryInfo().getCompanyName() : null);
				preparedStatement.setString(3, customerVo.getPrimaryInfo().getCustomerDisplayName() != null
						? customerVo.getPrimaryInfo().getCustomerDisplayName() : null);
				preparedStatement.setString(4, customerVo.getPrimaryInfo().getEmailAddress() != null
						? customerVo.getPrimaryInfo().getEmailAddress() : null);
				preparedStatement.setString(5, customerVo.getPrimaryInfo().getPhoneNo() != null
						? customerVo.getPrimaryInfo().getPhoneNo() : null);
				preparedStatement.setString(6, customerVo.getPrimaryInfo().getWebsiteAddress() != null
						? customerVo.getPrimaryInfo().getWebsiteAddress() : null);
				preparedStatement.setString(7, customerVo.getPrimaryInfo().getMobileNo() != null
						? customerVo.getPrimaryInfo().getMobileNo() : null);
				preparedStatement.setString(8,
						customerVo.getPrimaryInfo().getPanNo() != null ? customerVo.getPrimaryInfo().getPanNo() : null);
				preparedStatement.setString(9,
						customerVo.getStatus() != null ? customerVo.getStatus() : CommonConstants.STATUS_AS_ACTIVE);
				preparedStatement.setObject(10, customerVo.getBillingAddress().getIsSameOriginDestAddress() != null
						? customerVo.getBillingAddress().getIsSameOriginDestAddress() : null);
				preparedStatement.setObject(11, customerVo.getPrimaryInfo().getGstRegnTypeId() != null
						? customerVo.getPrimaryInfo().getGstRegnTypeId() : null);
				preparedStatement.setObject(12, customerVo.getPrimaryInfo().getOrganisationTypeId() != null
						? customerVo.getPrimaryInfo().getOrganisationTypeId() : null);
				preparedStatement.setObject(13, customerVo.getPrimaryInfo().getCustomerGroupId() != null
						? customerVo.getPrimaryInfo().getCustomerGroupId() : null);
				preparedStatement.setObject(14, customerVo.getCustomerFinance() != null && customerVo.getCustomerFinance().getCurrencyId()!=null
						? customerVo.getCustomerFinance().getCurrencyId() : 0);
				preparedStatement.setObject(15, customerVo.getPrimaryInfo().getPaymentTermsId() != null
						? customerVo.getPrimaryInfo().getPaymentTermsId() : null);
				preparedStatement.setString(16, customerVo.getPrimaryInfo().getGstNumber() != null
						? customerVo.getPrimaryInfo().getGstNumber() : null);
				preparedStatement.setObject(17, customerVo.getPrimaryInfo().getOpeningBal() != null
						? customerVo.getPrimaryInfo().getOpeningBal() : null);
				preparedStatement.setDate(18, new Date(System.currentTimeMillis()));
				preparedStatement.setInt(19, Integer.valueOf(customerVo.getUserId()));
				preparedStatement.setInt(20, customerVo.getOrganizationId());
				preparedStatement.setBoolean(21, customerVo.getIsSuperAdmin());
				preparedStatement.setString(22, customerVo.getRoleName());
				preparedStatement.setInt(23,
						customerVo.getDefaultGlTallyId()!=null && customerVo.getDefaultGlTallyId() >0 ? customerVo.getDefaultGlTallyId() : 0);
				preparedStatement.setString(24,
						customerVo.getDefaultGlTallyName() != null ? customerVo.getDefaultGlTallyName() : null);
				BookKeepingSettingsVo bookKeepingSetting=customerVo.getCustomerBookKeepingSetting();
				preparedStatement.setInt(25,
						bookKeepingSetting!=null  ? bookKeepingSetting.getId() : 0);
				preparedStatement.setString(26,
						bookKeepingSetting!=null &&bookKeepingSetting.getDefaultGlName() != null ? bookKeepingSetting.getDefaultGlName() : null);
				preparedStatement.setInt(27,
						bookKeepingSetting!=null &&bookKeepingSetting.getLocationId() > 0 ? bookKeepingSetting.getLocationId() : 0);
				preparedStatement.setString(28,
						bookKeepingSetting!=null &&bookKeepingSetting.getGstNumber() != null ? bookKeepingSetting.getGstNumber() : null);
				ObjectMapper mapper = new ObjectMapper();
				String newJsonData = mapper.writeValueAsString(customerVo.getPrimaryInfo().getOtherGsts() != null
						? customerVo.getPrimaryInfo().getOtherGsts() : "");
				preparedStatement.setString(29, newJsonData);
				int rowAffected = preparedStatement.executeUpdate();
				if (rowAffected == 1) {
					rs = preparedStatement.getGeneratedKeys();
					while (rs.next()) {
						customerVo.setId(rs.getInt(1));
					}
				}
				logger.info("Successfully inserted into table customer_general_information ");
			} catch (Exception e) {
				logger.info("Error in inserting to table customer_general_information ", e);
				throw new ApplicationException(e.getMessage());
			} finally {
				closeResources(rs, preparedStatement, null);
			}
		}
		return customerVo.getPrimaryInfo();
	}

	private void updateCustomerGeneralInfo(CustomerVo customerVo, Connection con) throws ApplicationException {
		logger.info("Entry into updateCustomerGenerakInfo in DAO");
		if (customerVo != null && customerVo.getPrimaryInfo() != null && customerVo.getId() != null) {
			if (customerVo.getPrimaryInfo().getEmailAddress() != null) {
				String emailId = getEmailForCustomerId(customerVo.getId(), con);
				if (!customerVo.getPrimaryInfo().getEmailAddress().equalsIgnoreCase(emailId)) {
					logger.info("To validate if new Email exist for org");
					Boolean isEmailExists = isEmailAlreadyExistForOrg(customerVo.getOrganizationId(),
							customerVo.getPrimaryInfo().getEmailAddress());
					if (isEmailExists != null && isEmailExists) {
						throw new ApplicationException(CustomerConstants.EMAIL_ALREADY_EXISTS);
					}
				}
			}
			PreparedStatement preparedStatement = null;
			try {
				preparedStatement = con.prepareStatement(CustomerConstants.UPDATE_CUSTOMER_GENERAL_INFO);
				preparedStatement.setString(1, customerVo.getPrimaryInfo().getPrimaryContact() != null
						? customerVo.getPrimaryInfo().getPrimaryContact() : null);
				preparedStatement.setString(2, customerVo.getPrimaryInfo().getCompanyName() != null
						? customerVo.getPrimaryInfo().getCompanyName() : null);
				preparedStatement.setString(3, customerVo.getPrimaryInfo().getCustomerDisplayName() != null
						? customerVo.getPrimaryInfo().getCustomerDisplayName() : null);
				preparedStatement.setString(4, customerVo.getPrimaryInfo().getEmailAddress() != null
						? customerVo.getPrimaryInfo().getEmailAddress() : null);
				preparedStatement.setString(5, customerVo.getPrimaryInfo().getPhoneNo() != null
						? customerVo.getPrimaryInfo().getPhoneNo() : null);
				preparedStatement.setString(6, customerVo.getPrimaryInfo().getWebsiteAddress() != null
						? customerVo.getPrimaryInfo().getWebsiteAddress() : null);
				preparedStatement.setString(7, customerVo.getPrimaryInfo().getMobileNo() != null
						? customerVo.getPrimaryInfo().getMobileNo() : null);
				preparedStatement.setString(8,
						customerVo.getPrimaryInfo().getPanNo() != null ? customerVo.getPrimaryInfo().getPanNo() : null);
				preparedStatement.setString(9,
						customerVo.getStatus() != null ? customerVo.getStatus() : CommonConstants.STATUS_AS_ACTIVE);
				preparedStatement.setObject(10, customerVo.getBillingAddress().getIsSameOriginDestAddress() != null
						? customerVo.getBillingAddress().getIsSameOriginDestAddress() : null);
				preparedStatement.setObject(11, customerVo.getPrimaryInfo().getGstRegnTypeId() != null
						? customerVo.getPrimaryInfo().getGstRegnTypeId() : null);
				preparedStatement.setObject(12, customerVo.getPrimaryInfo().getOrganisationTypeId() != null
						? customerVo.getPrimaryInfo().getOrganisationTypeId() : null);
				preparedStatement.setObject(13, customerVo.getPrimaryInfo().getCustomerGroupId() != null
						? customerVo.getPrimaryInfo().getCustomerGroupId() : null);
				preparedStatement.setObject(14, customerVo.getCustomerFinance() != null && customerVo.getCustomerFinance().getCurrencyId()!=null
						? customerVo.getCustomerFinance().getCurrencyId() : 0);
				preparedStatement.setObject(15, customerVo.getPrimaryInfo().getPaymentTermsId() != null
						? customerVo.getPrimaryInfo().getPaymentTermsId() : null);
				preparedStatement.setString(16, customerVo.getPrimaryInfo().getGstNumber() != null
						? customerVo.getPrimaryInfo().getGstNumber() : null);
				preparedStatement.setObject(17, customerVo.getPrimaryInfo().getOpeningBal() != null
						? customerVo.getPrimaryInfo().getOpeningBal() : null);
				preparedStatement.setDate(18, new Date(System.currentTimeMillis()));
				preparedStatement.setInt(19, Integer.valueOf(customerVo.getUserId()));
				preparedStatement.setInt(20, customerVo.getOrganizationId());
				preparedStatement.setBoolean(21, customerVo.getIsSuperAdmin());
				preparedStatement.setString(22, customerVo.getRoleName());
				preparedStatement.setInt(23,
						customerVo.getDefaultGlTallyId()!= null ? customerVo.getDefaultGlTallyId() : 0);
				preparedStatement.setString(24,
						customerVo.getDefaultGlTallyName() != null ? customerVo.getDefaultGlTallyName() : null);
				BookKeepingSettingsVo bookKeepingSetting=customerVo.getCustomerBookKeepingSetting();
				preparedStatement.setInt(25,
						bookKeepingSetting!=null &&bookKeepingSetting.getId() >0 ? bookKeepingSetting.getId() : 0);
				preparedStatement.setString(26,
						bookKeepingSetting!=null &&bookKeepingSetting.getDefaultGlName() != null ? bookKeepingSetting.getDefaultGlName() : null);
				preparedStatement.setInt(27,
						bookKeepingSetting!=null &&bookKeepingSetting.getLocationId() > 0 ? bookKeepingSetting.getLocationId() : 0);
				preparedStatement.setString(28,
						bookKeepingSetting!=null &&bookKeepingSetting.getGstNumber() != null ? bookKeepingSetting.getGstNumber() : null);
				ObjectMapper mapper = new ObjectMapper();
				String newJsonData = mapper.writeValueAsString(customerVo.getPrimaryInfo().getOtherGsts() != null
						?customerVo.getPrimaryInfo().getOtherGsts() : "");
				preparedStatement.setString(29, newJsonData);
				preparedStatement.setInt(30, customerVo.getId());
				preparedStatement.executeUpdate();
				logger.info("Successfully updated table customer_general_information ");
			} catch (Exception e) {
				logger.info("Error in updating table customer_general_information ", e);
				throw new ApplicationException(e);
			} finally {
				closeResources(null, preparedStatement, null);
			}
		}
	}

	private void createCustomerBillingAddress(CustomerBillingAddressVo billingAddressVo, Connection con,
			Integer customerId) throws ApplicationException {
		if (billingAddressVo != null) {
			logger.info("inserting to table customer_billing_address ");
			PreparedStatement preparedStatement = null;
			try {
				preparedStatement = con.prepareStatement(CustomerConstants.INSERT_INTO_CUSTOMER_BILLING_ADDRESS);
				preparedStatement.setString(1,
						billingAddressVo.getAttention() != null ? billingAddressVo.getAttention() : null);
				preparedStatement.setString(2, billingAddressVo.getCountry() != null
						? new Integer(billingAddressVo.getCountry()).toString() : null);
				preparedStatement.setString(3,
						billingAddressVo.getAddress_1() != null ? billingAddressVo.getAddress_1() : null);
				preparedStatement.setString(4,
						billingAddressVo.getAddress_2() != null ? billingAddressVo.getAddress_2() : null);
				preparedStatement.setString(5, billingAddressVo.getState() != null
						? new Integer(billingAddressVo.getState()).toString() : null);
				preparedStatement.setString(6, billingAddressVo.getCity() != null ? billingAddressVo.getCity() : null);
				preparedStatement.setString(7,
						billingAddressVo.getZipCode() != null ? billingAddressVo.getZipCode() : null);
				preparedStatement.setString(8,
						billingAddressVo.getPhoneNo() != null ? billingAddressVo.getPhoneNo() : null);
				preparedStatement.setString(9,
						billingAddressVo.getLandMark() != null ? billingAddressVo.getLandMark() : null);
				preparedStatement.setDate(10, new Date(System.currentTimeMillis()));
				preparedStatement.setInt(11, customerId);
				preparedStatement.setInt(12, billingAddressVo.getSelectedAddress() != null
						? billingAddressVo.getSelectedAddress()
								: 0);
				preparedStatement.setString(13, 	billingAddressVo.getGstNo() != null
						? billingAddressVo.getGstNo()
								: null);
				preparedStatement.setInt(14, 	billingAddressVo.getAddressType() != null
						? billingAddressVo.getAddressType()
								: 0);
				preparedStatement.executeUpdate();
				logger.info("Successfully inserted to table customer_billing_address ");
			} catch (Exception e) {
				logger.info("Error in inserting to table customer_billing_address ", e);
				throw new ApplicationException(e);
			} finally {
				closeResources(null, preparedStatement, null);
			}
		}
	}

	public void updateCustomerBillingAddress(CustomerBillingAddressVo billingAddressVo, Integer customerId,
			Connection con) throws ApplicationException {
		if (billingAddressVo != null) {
			PreparedStatement preparedStatement = null;
			ResultSet rs = null;

			try {
				deleteCustomerAddress(customerId, true, con);
				createCustomerBillingAddress(billingAddressVo,  con,customerId);

			} catch (Exception e) {
				logger.error("Error in updateCustomerBillingAddress",e);
				throw new ApplicationException(e.getMessage());
			} finally {
				closeResources(rs, preparedStatement, null);
			}

		}
	}

	private void createCustomerDeliveryAddress(CustomerDeliveryAddressVo deliveryAddressVo, Connection con,
			Integer customerId) throws ApplicationException {
		if (deliveryAddressVo != null) {
			logger.info("inserting to table customer_delivery_address ");
			PreparedStatement preparedStatement = null;
			try {
				preparedStatement = con.prepareStatement(CustomerConstants.INSERT_INTO_CUSTOMER_DELIVERY_ADDRESS);
				preparedStatement.setString(1,
						deliveryAddressVo.getAttention() != null ? deliveryAddressVo.getAttention() : null);
				preparedStatement.setString(2, deliveryAddressVo.getCountry() != null
						? new Integer(deliveryAddressVo.getCountry()).toString() : null);
				preparedStatement.setString(3,
						deliveryAddressVo.getAddress_1() != null ? deliveryAddressVo.getAddress_1() : null);
				preparedStatement.setString(4,
						deliveryAddressVo.getAddress_2() != null ? deliveryAddressVo.getAddress_2() : null);
				preparedStatement.setString(5, deliveryAddressVo.getState() != null
						? new Integer(deliveryAddressVo.getState()).toString() : null);
				preparedStatement.setString(6,
						deliveryAddressVo.getCity() != null ? deliveryAddressVo.getCity() : null);
				preparedStatement.setString(7,
						deliveryAddressVo.getZipCode() != null ? deliveryAddressVo.getZipCode() : null);
				preparedStatement.setString(8,
						deliveryAddressVo.getPhoneNo() != null ? deliveryAddressVo.getPhoneNo() : null);
				preparedStatement.setString(9,
						deliveryAddressVo.getLandMark() != null ? deliveryAddressVo.getLandMark() : null);
				preparedStatement.setDate(10, new Date(System.currentTimeMillis()));
				preparedStatement.setInt(11, customerId);
				preparedStatement.setInt(12, deliveryAddressVo.getSelectedAddress() != null
						? deliveryAddressVo.getSelectedAddress()
								: 0);
				preparedStatement.setString(13, 	deliveryAddressVo.getGstNo() != null
						? deliveryAddressVo.getGstNo()
								: null);
				preparedStatement.setInt(14, 	deliveryAddressVo.getAddressType() != null
						? deliveryAddressVo.getAddressType()
								: 0);
				preparedStatement.executeUpdate();
				logger.info("Successfully inserted to table customer_delivery_address ");
			} catch (Exception e) {
				logger.info("Error in inserting to table customer_delivery_address ", e);
				throw new ApplicationException(e);
			} finally {
				closeResources(null, preparedStatement, null);
			}
		}
	}

	public void updateCustomerDeliveryAddress(CustomerDeliveryAddressVo deliveryAddressVo, Integer customerId,
			Connection con) throws ApplicationException {
		if (deliveryAddressVo != null) {
			PreparedStatement preparedStatement = null;
			ResultSet rs = null;

			try {
				deleteCustomerAddress(customerId, false, con);
				createCustomerDeliveryAddress(deliveryAddressVo,  con,customerId);

			} catch (Exception e) {
				logger.error("Error in updateCustomerDeliveryAddress",e);
				throw new ApplicationException(e.getMessage());
			} finally {
				closeResources(rs, preparedStatement, null);
			}
		}

	}

	private void createContactDetails(CustomerContactsVo contactsVo, Connection con, Integer customerId)
			throws ApplicationException {
		if (contactsVo != null) {
			logger.info("inserting to table customer_contact ");
			PreparedStatement preparedStatement = null;
			try {
				preparedStatement = con.prepareStatement(CustomerConstants.INSERT_INTO_CUSTOMER_CONTACT);
				preparedStatement.setString(1, contactsVo.getSalutation());
				preparedStatement.setString(2, contactsVo.getFirstName());
				preparedStatement.setString(3, contactsVo.getLastName());
				preparedStatement.setString(4, contactsVo.getWorkNo());
				preparedStatement.setString(5, contactsVo.getMobileNo());
				preparedStatement.setString(6, contactsVo.getEmailAddress());
				preparedStatement.setString(7, CommonConstants.STATUS_AS_ACTIVE);
				preparedStatement.setDate(8, new Date(System.currentTimeMillis()));
				preparedStatement.setInt(9, customerId);
				preparedStatement.executeUpdate();
				logger.info("Successfully inserted to table customer_contact ");
			} catch (Exception e) {
				logger.info("Error in inserting to table customer_contact ", e);
				throw new ApplicationException(e);
			} finally {
				closeResources(null, preparedStatement, null);
			}
		}
	}

	private void updateContactDetails(CustomerContactsVo contact, Connection con) throws ApplicationException {
		if (contact != null && contact.getId() != null) {
			logger.info("updating to table customer_contact for id:: " + contact.getId());
			PreparedStatement preparedStatement = null;
			try {
				preparedStatement = con.prepareStatement(CustomerConstants.UPDATE_CUSTOMER_CONTACT);
				preparedStatement.setString(1, contact.getSalutation());
				preparedStatement.setString(2, contact.getFirstName());
				preparedStatement.setString(3, contact.getLastName());
				preparedStatement.setString(4, contact.getWorkNo());
				preparedStatement.setString(5, contact.getMobileNo());
				preparedStatement.setString(6, contact.getEmailAddress());
				preparedStatement.setString(7, CommonConstants.STATUS_AS_ACTIVE);
				preparedStatement.setDate(8, new Date(System.currentTimeMillis()));
				preparedStatement.setInt(9, contact.getId());
				preparedStatement.executeUpdate();
				logger.info("Successfully updated to table customer_contact ");
			} catch (Exception e) {
				logger.info("Error in updating to table customer_contact ", e);
				throw new ApplicationException(e);
			} finally {
				closeResources(null, preparedStatement, null);
			}
		}

	}

	public void createBankDetails(CustomerBankDetailsVo bankDetailsVo, Connection con, Integer customerId)
			throws ApplicationException {
		if (bankDetailsVo != null) {
			logger.info("inserting to table customer_bank_details ");
			PreparedStatement preparedStatement = null;
			try {
				preparedStatement = con.prepareStatement(CustomerConstants.INSERT_INTO_CUSTOMER_BANK_DETAILS);
				preparedStatement.setString(1, bankDetailsVo.getBankName());
				preparedStatement.setString(2, bankDetailsVo.getBranchName());
				preparedStatement.setString(3, bankDetailsVo.getAccountHolderName());
				preparedStatement.setString(4, bankDetailsVo.getAccountNumber());
				preparedStatement.setString(5, bankDetailsVo.getIfscCode());
				preparedStatement.setString(6, bankDetailsVo.getUpiId());
				preparedStatement.setBoolean(7, bankDetailsVo.getIsDefault());
				preparedStatement.setString(8, CommonConstants.STATUS_AS_ACTIVE);
				preparedStatement.setDate(9, new Date(System.currentTimeMillis()));
				preparedStatement.setInt(10, customerId);
				preparedStatement.executeUpdate();
				logger.info("Successfully inserted to table customer_bank_details ");
			} catch (Exception e) {
				logger.info("Error in inserting to table customer_bank_details ", e);
				throw new ApplicationException(e);
			} finally {
				closeResources(null, preparedStatement, null);
			}
		}
	}

	public void updateBankDetails(CustomerBankDetailsVo bankDetailsVo, Connection con, Integer customerId)
			throws ApplicationException {
		if (bankDetailsVo != null && bankDetailsVo.getId() != null) {
			logger.info("updating to table customer_bank_details for id :: " + bankDetailsVo.getId());
			PreparedStatement preparedStatement = null;
			try {
				preparedStatement = con.prepareStatement(CustomerConstants.UPDATE_CUSTOMER_BANK_INFO);
				preparedStatement.setString(1, bankDetailsVo.getBankName());
				preparedStatement.setString(2, bankDetailsVo.getBranchName());
				preparedStatement.setString(3, bankDetailsVo.getAccountHolderName());
				preparedStatement.setString(4, bankDetailsVo.getAccountNumber());
				preparedStatement.setString(5, bankDetailsVo.getIfscCode());
				preparedStatement.setString(6, bankDetailsVo.getUpiId());
				preparedStatement.setBoolean(7, bankDetailsVo.getIsDefault());
				preparedStatement.setString(8, CommonConstants.STATUS_AS_ACTIVE);
				preparedStatement.setDate(9, new Date(System.currentTimeMillis()));
				preparedStatement.setInt(10, bankDetailsVo.getId());
				preparedStatement.executeUpdate();
				logger.info("Successfully updated  to table customer_bank_details ");
			} catch (Exception e) {
				logger.info("Error in updating to table customer_bank_details ", e);
				throw new ApplicationException(e);
			} finally {
				closeResources(null, preparedStatement, null);
			}
		}
	}

	public CustomerVo getCustomerPrimaryInfo(Integer custId, Connection con) throws ApplicationException {
		CustomerVo customerVo = null;
		CustomerPrimaryInfoVo infoVo = null;
		CustomerBillingAddressVo billingAddressVo = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			preparedStatement = con.prepareStatement(CustomerConstants.GET_CUSTOMER_GENERAL_INFO);
			logger.info("To fetch from table customer_general_information ");
			preparedStatement.setInt(1, custId);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				customerVo = new CustomerVo();
				infoVo = new CustomerPrimaryInfoVo();
				billingAddressVo = new CustomerBillingAddressVo();
				infoVo.setCustomerId(custId);
				infoVo.setPrimaryContact(rs.getString(1));
				infoVo.setCompanyName(rs.getString(2));
				infoVo.setCustomerDisplayName(rs.getString(3));
				infoVo.setEmailAddress(rs.getString(4));
				infoVo.setPhoneNo(rs.getString(5));
				infoVo.setWebsiteAddress(rs.getString(6));
				infoVo.setMobileNo(rs.getString(7));
				infoVo.setPanNo(rs.getString(8));
				customerVo.setStatus(rs.getString(9));
				billingAddressVo.setIsSameOriginDestAddress(rs.getBoolean(10));
				customerVo.setUserId(rs.getString(11));
				customerVo.setOrganizationId(rs.getInt(12));
				customerVo.setIsSuperAdmin(rs.getBoolean(13));
				infoVo.setGstRegnTypeId(rs.getInt(14));
				infoVo.setOrganisationTypeId(rs.getInt(15));
				infoVo.setCustomerGroupId(rs.getInt(16));
				infoVo.setCurrencyId(rs.getInt(17));
				infoVo.setPaymentTermsId(rs.getInt(18));
				infoVo.setGstNumber(rs.getString(19));
				infoVo.setOpeningBal(rs.getDouble(20));
				customerVo.setDefaultGlTallyId(rs.getInt(21));
				customerVo.setDefaultGlTallyName(rs.getString(22));
				BookKeepingSettingsVo bookKeepingSettingsVo=new BookKeepingSettingsVo();
				bookKeepingSettingsVo.setId(rs.getInt(23));
				bookKeepingSettingsVo.setDefaultGlName(rs.getString(24));
				bookKeepingSettingsVo.setLocationId(rs.getInt(25));
				bookKeepingSettingsVo.setGstNumber(rs.getString(26));
				String json = rs.getString(27);
				if (json != null && json.length()>2 ) {
					ObjectMapper mapper = new ObjectMapper();
					infoVo.setOtherGsts(mapper.readValue(json,String[].class));
				}
				customerVo.setId(custId);
				customerVo.setPrimaryInfo(infoVo);
				customerVo.setBillingAddress(billingAddressVo);
				customerVo.setCustomerBookKeepingSetting(bookKeepingSettingsVo);


			}
			logger.info("Successfully fetched from table customer_general_information ");
		} catch (Exception e) {
			logger.info("Error in fetching from table customer_general_information ", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, null);
		}
		return customerVo;
	}

	public CustomerBillingAddressVo getCustomerBillingAddress(CustomerBillingAddressVo billingAddressVo, Integer CustId,
			Connection con) throws ApplicationException {
		billingAddressVo = billingAddressVo != null ? billingAddressVo : new CustomerBillingAddressVo();
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			preparedStatement = con.prepareStatement(CustomerConstants.GET_CUSTOMER_BILLING_ADDRESS_INFO);
			logger.info("To fetch from table customer_bank_details ");
			preparedStatement.setInt(1, CustId);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				billingAddressVo.setAttention(rs.getString(1));
				billingAddressVo.setCountry(rs.getString(2)!=null?Integer.parseInt(rs.getString(2)):0);
				billingAddressVo.setCountryName(financeCommonDao
						.getCountryName(Integer.parseInt(rs.getString(2) != null ? rs.getString(2) : "0")));
				billingAddressVo.setAddress_1(rs.getString(3));
				billingAddressVo.setAddress_2(rs.getString(4));
				billingAddressVo.setState(rs.getString(5)!=null?Integer.parseInt(rs.getString(5)):0);
				billingAddressVo.setStateName(financeCommonDao
						.getStateName(Integer.parseInt(rs.getString(5) != null ? rs.getString(5) : "0")));
				billingAddressVo.setCity(rs.getString(6));
				billingAddressVo.setZipCode(rs.getString(7));
				billingAddressVo.setPhoneNo(rs.getString(8));
				billingAddressVo.setLandMark(rs.getString(9));
				billingAddressVo.setSelectedAddress(rs.getInt(10)==0?null:rs.getInt(10));
				billingAddressVo.setGstNo(rs.getString(11));
				billingAddressVo.setAddressType(rs.getInt(12));
			}
			logger.info("Successfully fetched from table customer_bank_details ");
		} catch (Exception e) {
			logger.info("Error in fetching from table customer_bank_details ", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, null);
		}
		return billingAddressVo;
	}

	public CustomerDeliveryAddressVo getCustomerDeliveryAddress(Integer custId, Connection con)
			throws ApplicationException {
		CustomerDeliveryAddressVo deliveryAddressVo = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			preparedStatement = con.prepareStatement(CustomerConstants.GET_CUSTOMER_DELIVERY_ADDRESS_INFO);
			logger.info("To fetch from table customer_delivery_address ");
			preparedStatement.setInt(1, custId);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				deliveryAddressVo = new CustomerDeliveryAddressVo();
				deliveryAddressVo.setAttention(rs.getString(1));
				deliveryAddressVo.setCountry(rs.getString(2)!=null?Integer.parseInt(rs.getString(2)):0);
				deliveryAddressVo.setCountryName(rs.getString(2) != null ? financeCommonDao
						.getCountryName(Integer.parseInt(rs.getString(2) )): "0");
				deliveryAddressVo.setAddress_1(rs.getString(3));
				deliveryAddressVo.setAddress_2(rs.getString(4));
				deliveryAddressVo.setState(rs.getString(5)!=null?Integer.parseInt(rs.getString(5)):0);
				deliveryAddressVo.setStateName(financeCommonDao
						.getStateName(Integer.parseInt(rs.getString(5) != null ? rs.getString(5) : "0")));
				deliveryAddressVo.setCity(rs.getString(6));
				deliveryAddressVo.setZipCode(rs.getString(7));
				deliveryAddressVo.setPhoneNo(rs.getString(8));
				deliveryAddressVo.setLandMark(rs.getString(9));
				deliveryAddressVo.setSelectedAddress(rs.getInt(10)==0?null:rs.getInt(10));
				deliveryAddressVo.setGstNo(rs.getString(11));
				deliveryAddressVo.setAddressType(rs.getInt(12));
			}
			logger.info("Successfully fetched from table customer_delivery_address ");
		} catch (Exception e) {
			logger.info("Error in fetching from table customer_delivery_address ", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, null);
		}
		return deliveryAddressVo;
	}

	private List<CustomerContactsVo> getCustomerContactDetails(Integer custId, Connection con)
			throws ApplicationException {
		List<CustomerContactsVo> customerList = null;
		logger.info("getCustomerContactDetails");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			preparedStatement = con.prepareStatement(CustomerConstants.GET_CUSTOMER_CONTACT_INFO);
			preparedStatement.setInt(1, custId);
			rs = preparedStatement.executeQuery();
			customerList = new ArrayList<CustomerContactsVo>();
			while (rs.next()) {
				CustomerContactsVo contactsVo = new CustomerContactsVo();
				contactsVo.setId(rs.getInt(1));
				contactsVo.setSalutation(rs.getString(2));
				contactsVo.setFirstName(rs.getString(3));
				contactsVo.setLastName(rs.getString(4));
				contactsVo.setWorkNo(rs.getString(5));
				contactsVo.setMobileNo(rs.getString(6));
				contactsVo.setEmailAddress(rs.getString(7));
				contactsVo.setStatus(rs.getString(8));
				customerList.add(contactsVo);
			}
			logger.info("Successfully fetched from table customer_contact ");
		} catch (Exception e) {
			logger.info("Error in fetching from table customer_contact ");
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, null);
		}
		return customerList;
	}

	private List<CustomerBankDetailsVo> getCustomerBankDetails(Integer custId, Connection con)
			throws ApplicationException {
		List<CustomerBankDetailsVo> bankDetailsList = null;
		logger.info("To fetch from table customer_bank_details ");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			preparedStatement = con.prepareStatement(CustomerConstants.GET_CUSTOMER_BANK_INFO);
			preparedStatement.setInt(1, custId);
			rs = preparedStatement.executeQuery();
			bankDetailsList = new ArrayList<CustomerBankDetailsVo>();
			while (rs.next()) {
				CustomerBankDetailsVo bankDetailsVo = new CustomerBankDetailsVo();
				bankDetailsVo.setId(rs.getInt(1));
				bankDetailsVo.setBankName(rs.getString(2));
				bankDetailsVo.setBranchName(rs.getString(3));
				bankDetailsVo.setAccountHolderName(rs.getString(4));
				bankDetailsVo.setAccountNumber(rs.getString(5));
				bankDetailsVo.setConfirmAccountNumber(rs.getString(5));
				bankDetailsVo.setIfscCode(rs.getString(6));
				bankDetailsVo.setUpiId(rs.getString(7));
				bankDetailsVo.setIsDefault(rs.getBoolean(8));
				bankDetailsVo.setStatus(rs.getString(9));
				bankDetailsList.add(bankDetailsVo);
			}
			logger.info("Successfully fetched from table customer_bank_details ");
		} catch (Exception e) {
			logger.info("Error in fetching from table customer_bank_details ", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, null);
		}
		return bankDetailsList;
	}
	
	
	public CustomerBankDetailsVo getCustomerDefaultBankDetails(Integer custId)
			throws ApplicationException {
		logger.info("To fetch from table customer_bank_details ");
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		CustomerBankDetailsVo bankDetailsVo = null;
		try {
			con = getAccountsReceivableConnection();
			preparedStatement = con.prepareStatement(CustomerConstants.GET_CUSTOMER_DEFAULT_BANK_INFO);
			preparedStatement.setInt(1, custId);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				bankDetailsVo = new CustomerBankDetailsVo();
				bankDetailsVo.setId(rs.getInt(1));
				bankDetailsVo.setBankName(rs.getString(2));
				bankDetailsVo.setBranchName(rs.getString(3));
				bankDetailsVo.setAccountHolderName(rs.getString(4));
				bankDetailsVo.setAccountNumber(rs.getString(5));
				bankDetailsVo.setConfirmAccountNumber(rs.getString(5));
				bankDetailsVo.setIfscCode(rs.getString(6));
				bankDetailsVo.setUpiId(rs.getString(7));
				bankDetailsVo.setIsDefault(rs.getBoolean(8));
				bankDetailsVo.setStatus(rs.getString(9));
			}
			logger.info("Successfully fetched from table customer_bank_details ");
		} catch (Exception e) {
			logger.info("Error in fetching from table customer_bank_details ", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return bankDetailsVo;
	}

	private void changeStatusForCustomerTables(Integer id, String status, Connection con, String query)
			throws ApplicationException {
		logger.info("To Change the status with query :: " + query);
		if (query != null) {
			PreparedStatement preparedStatement = null;
			try {
				preparedStatement = con.prepareStatement(query);
				preparedStatement.setString(1, status);
				preparedStatement.setDate(2, new Date(System.currentTimeMillis()));
				preparedStatement.setInt(3, id);
				preparedStatement.executeUpdate();
				logger.info("Status chaneged successfully ");
			} catch (Exception e) {
				logger.info("Error in changeCustPrimaryInfoStatus ", e);
				throw new ApplicationException(e);
			} finally {
				closeResources(null, preparedStatement, null);
			}
		}
	}

	private void changeStatusForCustomerTables(Integer id, String status, Connection con, String query, String userId,
			String roleName) throws ApplicationException {
		logger.info("To Change the status with query :: " + query);
		if (query != null) {
			PreparedStatement preparedStatement = null;
			try {
				preparedStatement = con.prepareStatement(query);
				preparedStatement.setString(1, status);
				preparedStatement.setDate(2, new Date(System.currentTimeMillis()));
				preparedStatement.setString(3, userId);
				preparedStatement.setString(4, roleName);
				preparedStatement.setInt(5, id);
				preparedStatement.executeUpdate();
				logger.info("Status chaneged successfully ");
			} catch (Exception e) {
				logger.info("Error in changeCustPrimaryInfoStatus ", e);
				throw new ApplicationException(e);
			} finally {
				closeResources(null, preparedStatement, null);
			}
		}
	}

	private Boolean isBankDetailExist(Integer custId, String accountNo, String ifscCode, Connection con)
			throws ApplicationException {
		Boolean isBankDetailExist = null;
		logger.info("To validate isBankDetailExist");
		if (custId != null && accountNo != null && ifscCode != null) {
			PreparedStatement preparedStatement = null;
			ResultSet rs = null;
			try {
				preparedStatement = con.prepareStatement(CustomerConstants.IS_BANK_DETAIL_EXISTS);
				preparedStatement.setInt(1, custId);
				preparedStatement.setString(2, accountNo);
				preparedStatement.setString(3, ifscCode);
				rs = preparedStatement.executeQuery();
				isBankDetailExist = rs.next();

				logger.info("Is exist result::" + isBankDetailExist);
			} catch (Exception e) {
				logger.info("Error  in validate isBankDetailExist ", e);
				throw new ApplicationException(e);
			} finally {
				closeResources(rs, preparedStatement, null);
			}
		}
		return isBankDetailExist;

	}


	private Boolean isEmailAlreadyExistForOrg(Integer orgId, String emailId) throws ApplicationException {
		Boolean isEmailAlreadyExistForOrg = null;
		logger.info("To validate isEmailAlreadyExistForOrg");
		if (orgId != null && emailId != null) {
			Connection con = null;
			PreparedStatement preparedStatement = null;
			ResultSet rs = null;
			try {
				con = getAccountsReceivableConnection();
				preparedStatement = con.prepareStatement(CustomerConstants.IS_EMAIL_ALREADY_EXIST_FOR_ORGANIZATION);
				preparedStatement.setInt(1, orgId);
				preparedStatement.setString(2, emailId);
				rs = preparedStatement.executeQuery();
				isEmailAlreadyExistForOrg = rs.next();

				logger.info("Is exist result::" + isEmailAlreadyExistForOrg);
			} catch (Exception e) {
				logger.info("Error  in validate isBankDetailExist ", e);
				throw new ApplicationException(e);
			} finally {
				closeResources(rs, preparedStatement, con);
			}
		}
		return isEmailAlreadyExistForOrg;
	}

	private String getEmailForCustomerId(Integer custId, Connection con) throws ApplicationException {
		String emailId = null;
		logger.info("To validate getGstForCustomerId");
		if (custId != null) {
			PreparedStatement preparedStatement = null;
			ResultSet rs = null;
			try {
				preparedStatement = con.prepareStatement(CustomerConstants.GET_EMAIL_FOR_CUTOMER_ID);
				preparedStatement.setInt(1, custId);
				rs = preparedStatement.executeQuery();
				while (rs.next()) {
					emailId = rs.getString(1);
				}
				logger.info("gstNumber::" + emailId);
			} catch (Exception e) {
				logger.info("Error  in getGstForCustomerId ", e);
				throw new ApplicationException(e);
			} finally {
				closeResources(rs, preparedStatement, null);
			}
		}
		return emailId;
	}

		public String getGstForCustomerId(Integer custId) throws ApplicationException {
		String gstNumber = null;
		logger.info("To validate getGstForCustomerId");
		if (custId != null) {
			 Connection con =null;
			PreparedStatement preparedStatement = null;
			ResultSet rs = null;
			try {
				con = getAccountsReceivableConnection();
				preparedStatement = con.prepareStatement(CustomerConstants.GET_GST_NUMBER_FOR_CUTOMER_ID);
				preparedStatement.setInt(1, custId);
				rs = preparedStatement.executeQuery();
				while (rs.next()) {
					gstNumber = rs.getString(1);
				}
				logger.info("gstNumber::" + gstNumber);
			} catch (Exception e) {
				logger.info("Error  in getGstForCustomerId ", e);
				throw new ApplicationException(e);
			} finally {
				closeResources(rs, preparedStatement, con);
			}
		}
		return gstNumber;
	}

	public void updateGstForCustomerId(String gstNumber, Integer custId, Integer userId, Connection con)
			throws ApplicationException {
		logger.info("To update getGstForCustomerId");
		if (custId != null) {
			PreparedStatement preparedStatement = null;
			try {
				preparedStatement = con.prepareStatement(CustomerConstants.UPDATE_GST_NUMBER_FOR_CUTOMER_ID);
				preparedStatement.setString(1, gstNumber);
				preparedStatement.setDate(2, new Date(System.currentTimeMillis()));
				preparedStatement.setInt(3, userId);
				preparedStatement.setInt(4, custId);
				preparedStatement.executeUpdate();
				logger.info("Updated the GST number::" + gstNumber);
			} catch (Exception e) {
				logger.info("Error  in getGstForCustomerId ", e);
				throw new ApplicationException(e);
			} finally {
				closeResources(null, preparedStatement, null);
			}
		}
	}

	// To get the basic customer details for the customer
	public List<BasicCustomerVo> getBasicCustomer(Integer organizationId, Connection con) throws ApplicationException {
		logger.info("Entry into method:getBasicCustomer");
		List<BasicCustomerVo> basicCustomerVos = new ArrayList<BasicCustomerVo>();
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			preparedStatement = con.prepareStatement(CustomerConstants.GET_BASIC_CUSTOMER_DETAIL);
			preparedStatement.setInt(1, organizationId);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				BasicCustomerVo customerVo = new BasicCustomerVo();
				customerVo.setValue(rs.getInt(1));
				customerVo.setName(rs.getString(2));
				customerVo.setComapnyName(rs.getString(3));
				basicCustomerVos.add(customerVo);
			}
		} catch (Exception e) {
			logger.info("Error in method:getBasicCustomer");
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, null);
		}
		return basicCustomerVos;
	}

	public String processUpload(CustomerVo customerVo, String currency, String billingAddressState,
			String billingAddressCountry, String deliveryAddressState, String deliveryAddressCountry, Boolean duplicacy)
					throws SQLException, ApplicationException {
		logger.info("Entry into processUpload");
		CustomerPrimaryInfoVo primaryInfo = customerVo.getPrimaryInfo();
		CustomerBillingAddressVo billingAddress = customerVo.getBillingAddress();
		CustomerDeliveryAddressVo deliveryAddress = customerVo.getDeliveryAddress();
		if (currency != null) {
			Integer currencyId = vendorDao.getCurrencyId(currency, customerVo.getOrganizationId());
			if (currencyId != null) {
				primaryInfo.setCurrencyId(vendorDao.getCurrencyId(currency, customerVo.getOrganizationId()));
			} else {
				throw new ApplicationException("Invalid Currency");
			}
		}
		if (billingAddressState != null) {
			Integer stateId = vendorDao.getStateId(billingAddressState);
			if (stateId != null) {
				billingAddress.setState(vendorDao.getStateId(billingAddressState));
			} else {
				throw new ApplicationException("Invalid State");
			}
		}
		if (billingAddressCountry != null) {
			Integer countryId = vendorDao.getCountryId(billingAddressCountry);
			if (countryId != null) {
				billingAddress.setCountry(vendorDao.getCountryId(billingAddressCountry));
			} else {
				throw new ApplicationException("Invalid Country");
			}
		}
		if (deliveryAddressState != null) {
			Integer stateId = vendorDao.getStateId(deliveryAddressState);
			if (stateId != null) {
				deliveryAddress.setState(vendorDao.getStateId(deliveryAddressState));
			} else {
				throw new ApplicationException("Invalid State");
			}
		}
		if (deliveryAddressCountry != null) {
			Integer countryId = vendorDao.getCountryId(deliveryAddressCountry);
			if (countryId != null) {
				deliveryAddress.setCountry(vendorDao.getCountryId(deliveryAddressCountry));
			} else {
				throw new ApplicationException("Invalid Country");
			}
		}
		customerVo.setPrimaryInfo(primaryInfo);
		customerVo.setBillingAddress(billingAddress);
		customerVo.setDeliveryAddress(deliveryAddress);
		boolean isCustomerExist = isEmailAlreadyExistForOrg(customerVo.getOrganizationId(),
				customerVo.getPrimaryInfo().getEmailAddress());
		if (isCustomerExist) {
			if (duplicacy) {
				int customerId = getCustomerId(customerVo.getPrimaryInfo().getEmailAddress(),
						customerVo.getOrganizationId());
				customerVo.setId(customerId);
				updateCustomer(customerVo);
			}
		} else {
			createCustomer(customerVo);
			return "Create";
		}
		return null;

	}

	private int getCustomerId(String emailAddress, Integer organizationId) throws ApplicationException {
		logger.info("Entry into getCustomerId");
		int customerId = 0;
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			con = getAccountsReceivableConnection();
			preparedStatement = con.prepareStatement(CustomerConstants.GET_CUSTOMER_ID);
			preparedStatement.setInt(1, organizationId);
			preparedStatement.setString(2, emailAddress);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				customerId = rs.getInt(1);
			}
		} catch (Exception e) {
			logger.info("Error in  getCustomerId:", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return customerId;
	}

	public Integer getCustomerIdByDisplayName(String customerName, Integer organizationId) throws ApplicationException {
		logger.info("Entry into getCustomerId");
		Integer customerId = 0;
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			con = getAccountsReceivableConnection();
			preparedStatement = con.prepareStatement(CustomerConstants.GET_CUSTOMER_ID_BY_DISPLAYNAME);
			preparedStatement.setInt(1, organizationId);
			preparedStatement.setString(2, customerName);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				customerId = rs.getInt(1);
			}
		} catch (Exception e) {
			logger.info("Error in  getCustomerId:", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return customerId;
	}

	public String getCustomerName(Integer customerId) throws ApplicationException {
		logger.info("Entry into getCustomerName");
		String customerName = null;
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			con = getAccountsReceivableConnection();
			preparedStatement = con.prepareStatement(CustomerConstants.GET_CUSTOMER_Name);
			preparedStatement.setInt(1, customerId);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				customerName = rs.getString(1);
			}
		} catch (Exception e) {
			logger.info("Error in  getCustomerName:", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return customerName;
	}

	public List<CustomerExportVo> getListCustomersById(ExportVo exportVo) throws ApplicationException {
		logger.info("Entry into getListCustomersById");
		List<CustomerExportVo> customers = new ArrayList<CustomerExportVo>();
		if (exportVo.getListOfId() != null && exportVo.getListOfId().size() > 0) {
			for (Integer id : exportVo.getListOfId()) {
				CustomerExportVo customer = new CustomerExportVo();
				customer = getCustomersGeneralinfoExport(customer, id, exportVo.getOrganizationId());
				getCustomerBillingAddressExport(customer, id);
				getCustomerDeliveryAddressExport(customer, id);
				customer.setBankDetails(getCustomerBankDetailsExport(id));
				customer.setContacts(getCustomerContactDetailsExport(id));
				customers.add(customer);
			}

		}
		return customers;

	}

	private void getCustomerDeliveryAddressExport(CustomerExportVo customer, Integer id) throws ApplicationException {
		logger.info("Entry into getCustomerDeliveryAddressExport");
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			con = getAccountsReceivableConnection();
			preparedStatement = con.prepareStatement(CustomerConstants.GET_CUSTOMER_DELIVERY_ADDRESS_EXPORT_BY_ID);
			preparedStatement.setInt(1, id);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				customer.setDeliveryAddrAttention(rs.getString(1));
				customer.setDeliveryAddrPhone(rs.getString(2));
				Integer countryId = rs.getInt(3);
				if (countryId != null) {
					customer.setDeliveryAddrCountry(financeCommonDao.getCountryName(countryId));
				}
				customer.setBillingAddrLine1(rs.getString(4));
				customer.setDeliveryAddrLine2(rs.getString(5));
				customer.setDeliveryAddrLandmark(rs.getString(6));
				Integer stateId = rs.getInt(7);
				if (stateId != null) {
					customer.setDeliveryAddrState(financeCommonDao.getStateName(stateId));
				}
				customer.setDeliveryAddrCity(rs.getString(8));
				customer.setDeliveryAddrPinCode(rs.getString(9));
			}
		} catch (Exception e) {
			logger.info("Error in  getCustomerDeliveryAddressExport:", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}

	}

	private void getCustomerBillingAddressExport(CustomerExportVo customer, Integer id) throws ApplicationException {
		logger.info("Entry into getCustomerBillingAddressExport");
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			con = getAccountsReceivableConnection();
			preparedStatement = con.prepareStatement(CustomerConstants.GET_CUSTOMER_BILLING_ADDRESS_EXPORT_BY_ID);
			preparedStatement.setInt(1, id);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				customer.setBillingAddrAttention(rs.getString(1));
				customer.setBillingAddrPhone(rs.getString(2));
				Integer countryId = rs.getInt(3);
				if (countryId != null) {
					customer.setBillingAddrCountry(financeCommonDao.getCountryName(countryId));
				}
				customer.setBillingAddrLine1(rs.getString(4));
				customer.setBillingAddrLine2(rs.getString(5));
				customer.setBillingAddrLandmark(rs.getString(6));
				Integer stateId = rs.getInt(7);
				if (stateId != null) {
					customer.setBillingAddrState(financeCommonDao.getStateName(stateId));
				}
				customer.setBillingAddrCity(rs.getString(8));
				customer.setBillingAddrPinCode(rs.getString(9));
			}
		} catch (Exception e) {
			logger.info("Error in  getCustomerBillingAddressExport:", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}

	}

	private List<ContactsExportVo> getCustomerContactDetailsExport(Integer id) throws ApplicationException {
		logger.info("Entry into getCustomerContactDetailsExport");
		List<ContactsExportVo> contacts = new ArrayList<ContactsExportVo>();
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try  {
			con = getAccountsReceivableConnection();
			preparedStatement = con.prepareStatement(CustomerConstants.GET_CUSTOMER_CONTACT_DETAILS_EXPORT_BY_ID);
			preparedStatement.setInt(1, id);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				ContactsExportVo contactVo = new ContactsExportVo();
				contactVo.setContactSalutation(rs.getString(1));
				contactVo.setContactFirstName(rs.getString(2));
				contactVo.setContactLastName(rs.getString(3));
				contactVo.setContactWorkNo(rs.getString(4));
				contactVo.setContactMobileNo(rs.getString(5));
				contactVo.setContactEmailAddress(rs.getString(6));
				contacts.add(contactVo);
			}
		} catch (Exception e) {
			logger.info("Error in  getCustomerContactDetailsExport:", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return contacts;

	}

	private List<BankExportVo> getCustomerBankDetailsExport(Integer id) throws ApplicationException {
		logger.info("Entry into getCustomerBankDetailsExport");
		List<BankExportVo> bankDetails = new ArrayList<BankExportVo>();
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			con = getAccountsReceivableConnection();
			preparedStatement = con.prepareStatement(CustomerConstants.GET_CUSTOMER_BANK_DETAILS_EXPORT_BY_ID);
			preparedStatement.setInt(1, id);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				BankExportVo bankVo = new BankExportVo();
				bankVo.setAccountHolderName(rs.getString(1));
				bankVo.setAccountNo(rs.getString(2));
				bankVo.setBankNname(rs.getString(3));
				bankVo.setBranchName(rs.getString(4));
				bankVo.setIfscCode(rs.getString(5));
				bankVo.setUpiId(rs.getString(6));
				bankVo.setIsDefault(rs.getBoolean(7));
				bankDetails.add(bankVo);
			}
		} catch (Exception e) {
			logger.info("Error in  getCustomerBankDetailsExport:", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return bankDetails;

	}

	private CustomerExportVo getCustomersGeneralinfoExport(CustomerExportVo customer, Integer id, Integer orgId)
			throws ApplicationException {
		logger.info("Entry into getCustomersGeneralinfoExport");
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			con = getAccountsReceivableConnection();
			preparedStatement = con.prepareStatement(CustomerConstants.GET_CUSTOMER_GENERAL_INFO_EXPORT_BY_ID);
			preparedStatement.setInt(1, id);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				customer.setId(id);
				customer.setPrimaryContact(rs.getString(1));
				customer.setCompanyName(rs.getString(2));
				customer.setCustomerDisplayName(rs.getString(3));
				customer.setEmail(rs.getString(4));
				customer.setPhone(rs.getString(5));
				customer.setMobileNo(rs.getString(6));
				customer.setWebsite(rs.getString(7));
				customer.setPan(rs.getString(8));
				Integer organizationTypeId = rs.getInt(9);
				if (organizationTypeId != null) {
					customer.setOrganizationType(financeCommonDao.getOrganizationType(organizationTypeId));
				}
				Integer customerGroupId = rs.getInt(10);
				if (customerGroupId != null) {
					customer.setCustomerGroup(customerGroupDao.getCustomerGroupName(customerGroupId, orgId));
				}
				Integer gdtTypeId = rs.getInt(11);
				if (gdtTypeId != null) {
					customer.setGstRegnType(financeCommonDao.getGstTypeName(gdtTypeId));
				}
				customer.setGstNumber(rs.getString(12));
				Integer currencyId = rs.getInt(13);
				if (currencyId != null) {
					customer.setCurrency(currencyDao.getCurrencyName(currencyId, orgId));
				}
				customer.setOpeningBalance(rs.getString(14));
				Integer paymentTermId = rs.getInt(15);
				if (paymentTermId != null) {
					customer.setPaymentTerms(paymentTermsDao.getPaymentTermName(paymentTermId, orgId));
				}
				customer.setStatus(rs.getString(16));
			}
		} catch (Exception e) {
			logger.info("Error in  getCustomerBankDetailsExport:", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return customer;

	}

	public List<CustomerBasicDetailsVo> getCustomerBasicDetails(int organizationId) throws ApplicationException {
		logger.info("Entry into getCustomerBasicDetails");
		List<CustomerBasicDetailsVo> customerDetails = new ArrayList<CustomerBasicDetailsVo>();
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			con = getAccountsReceivableConnection();
			preparedStatement = con.prepareStatement(CustomerConstants.GET_BASIC_CUSTOMER_FOR_ORG);
			preparedStatement.setInt(1, organizationId);
			preparedStatement.setString(2, CommonConstants.STATUS_AS_ACTIVE);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				CustomerBasicDetailsVo customerBasicDetailsVo = new CustomerBasicDetailsVo();
				customerBasicDetailsVo.setId(rs.getInt(1));
				customerBasicDetailsVo.setName(rs.getString(2));
				customerBasicDetailsVo.setCurrencyId(rs.getInt(3));
				customerDetails.add(customerBasicDetailsVo);
			}
		} catch (Exception e) {
			logger.info("Error in  getCustomerBasicDetails:", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}

		return customerDetails;
	}

	// To get the basic customer details for the customer not in DEL or INA state
	public List<PaymentTypeVo> getActiveCustomers(Integer organizationId, Connection con) throws ApplicationException {
		logger.info("Entry into method: getActiveCustomers");
		List<PaymentTypeVo> basicCustomerVos = new ArrayList<PaymentTypeVo>();
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			preparedStatement = con.prepareStatement(CustomerConstants.GET_ACTIVE_CUSTOMER_DETAIL);
			preparedStatement.setInt(1, organizationId);
			preparedStatement.setString(2, CommonConstants.STATUS_AS_ACTIVE);
			
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				PaymentTypeVo customerVo = new PaymentTypeVo();
				customerVo.setValue(rs.getInt(1));
				customerVo.setName(rs.getString(2));
				customerVo.setCurrencyId(rs.getInt(3));
				basicCustomerVos.add(customerVo);
			}
		} catch (Exception e) {
			logger.info("Error in method:getBasicCustomer");
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, null);
		}
		return basicCustomerVos;
	}

	private List<BaseAddressVo>  getCustomerAdditionalAddressDetailsByCustomerId(Connection con, int customerId)
			throws ApplicationException {
		logger.info("Entry into method: getCustomerAdditionalAddressDetailsByCustomerId");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		List<BaseAddressVo> vendorAdditionalAddressesList = new ArrayList<BaseAddressVo>();
		try {
			preparedStatement = con.prepareStatement(CustomerConstants.GET_CUSTOMER_ADDITIONAL_ADDRESS_BY_CUSTOMER_ID);
			preparedStatement.setInt(1, customerId);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				BaseAddressVo vendorBaseAddressVo=new BaseAddressVo();
				vendorBaseAddressVo.setId(rs.getInt(1));
				vendorBaseAddressVo.setAttention(rs.getString(2));
				vendorBaseAddressVo.setCountry(rs.getInt(3));
				vendorBaseAddressVo.setAddress_1(rs.getString(4));
				vendorBaseAddressVo.setAddress_2(rs.getString(5));
				vendorBaseAddressVo.setState(rs.getString(6)!=null?Integer.parseInt(rs.getString(6)):0);
				vendorBaseAddressVo.setCity(rs.getString(7));
				vendorBaseAddressVo.setZipCode(rs.getString(8));
				vendorBaseAddressVo.setPhoneNo(rs.getString(9));
				vendorBaseAddressVo.setLandMark(rs.getString(10));
				vendorBaseAddressVo.setSelectedAddress(rs.getInt(11)==0?null:rs.getInt(11));
				vendorBaseAddressVo.setGstNo(rs.getString(12));
				vendorBaseAddressVo.setAddressType(rs.getInt(13));
				vendorAdditionalAddressesList.add(vendorBaseAddressVo);

			}
		} catch (Exception e) {
			logger.error("Error in getCustomerAdditionalAddressDetailsByCustomerId:",e);
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, null);
		}
		return vendorAdditionalAddressesList;
	}

	private CustomerFinanceVo getCustomerFinanceDetails(Connection con, int customerId)
			throws ApplicationException {
		logger.info("Entry into method: getCustomersFinanceDetails");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		CustomerFinanceVo customerFinance = new CustomerFinanceVo();
		try {
			String query = CustomerConstants.GET_CUSTOMER_FINANCE_BY_CUST_ID;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, customerId);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				customerFinance.setId(rs.getObject(1) != null ? rs.getInt(1) : null);
				customerFinance.setCurrencyId(rs.getObject(2) != null ? rs.getInt(2) : null);
				customerFinance.setPaymentTermsid(rs.getObject(3) != null ? rs.getInt(3) : null);
				customerFinance.setTdsId(rs.getObject(4) != null ? rs.getInt(4) : null);
				customerFinance.setOpeningBalance(rs.getString(5));
			}
		} catch (Exception e) {
			logger.error("Error in getCustomerFinanceDetails: ",e);
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, null);
		}
		return customerFinance;
	}


	private void deleteCustomerBankDetails(Integer id,Connection con)
			throws ApplicationException {
		logger.info("Entry To deleteCustomerBankDetails :: " );
		PreparedStatement preparedStatement =null;
		try {
			preparedStatement = con.prepareStatement(CustomerConstants.DELETE_CUSTOMER_BANK_DETAILS);
			preparedStatement.setInt(1, id);
			preparedStatement.executeUpdate();
			logger.info("Customer Bank Details deleted successfully ");
		} catch (Exception e) {
			logger.info("Error in deleteCustomerBankDetails ", e);
			throw new ApplicationException(e.getMessage());
		}finally{
			closeResources(null, preparedStatement, null);
		}
	}

	private void deleteCustomerContacts(Integer id,Connection con)
			throws ApplicationException {
		logger.info("Entry To deleteCustomerContacts :: " );
		PreparedStatement preparedStatement =null;
		try {
			preparedStatement = con.prepareStatement(CustomerConstants.DELETE_CUSTOMER_CONTACT);
			preparedStatement.setInt(1, id);
			preparedStatement.executeUpdate();
			logger.info("Customer Contact Details deleted successfully ");
		} catch (Exception e) {
			logger.info("Error in deleteCustomerContacts ", e);
			throw new ApplicationException(e.getMessage());
		}finally{
			closeResources(null, preparedStatement, null);
		}
	}
	private void deleteCustomerAdditionalAddress(Integer id,Connection con)
			throws ApplicationException {
		logger.info("Entry To deleteCustomerAdditionalAddress :: " );
		PreparedStatement preparedStatement =null;
		try {
			preparedStatement = con.prepareStatement(CustomerConstants.DELETE_CUSTOMER_ADDITIONAL_ADDRESS);
			preparedStatement.setInt(1, id);
			preparedStatement.executeUpdate();
			logger.info("Vendor deleteCustomerAdditionalAddress Details deleted successfully ");
		} catch (Exception e) {
			logger.info("Error in deleteCustomerAdditionalAddress ", e);
			throw new ApplicationException(e.getMessage());
		}finally{
			closeResources(null, preparedStatement, null);
		}
	}
	private void deleteCustomerAddress(Integer customerId,boolean isBillingAddress,Connection con)
			throws ApplicationException {
		logger.info("Entry To deleteCustomerAddress :: for customerId :"+customerId );
		PreparedStatement preparedStatement =null;
		try {
			String query=CustomerConstants.DELETE_CUSTOMER_DELIVERY_ADDRESS;
			if(isBillingAddress) {
				query=CustomerConstants.DELETE_CUSTOMER_BILLING_ADDRESS;
			}
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, customerId);
			preparedStatement.executeUpdate();
			logger.info("Vendor deleteCustomerAddress Details deleted successfully ");
		} catch (Exception e) {
			logger.info("Error in deleteCustomerAddress ", e);
			throw new ApplicationException(e.getMessage());
		}finally{
			closeResources(null, preparedStatement, null);
		}
	}
}

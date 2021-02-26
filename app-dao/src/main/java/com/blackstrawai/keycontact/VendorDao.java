package com.blackstrawai.keycontact;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.ap.balanceconfirmation.VendorAddressVo;
import com.blackstrawai.ap.dropdowns.BasicVendorDetailsVo;
import com.blackstrawai.ap.dropdowns.BasicVendorVo;
import com.blackstrawai.ap.dropdowns.PaymentTypeVo;
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
import com.blackstrawai.export.ExportVo;
import com.blackstrawai.export.VendorExportVo;
import com.blackstrawai.keycontact.vendor.VendorBankDetailsVo;
import com.blackstrawai.keycontact.vendor.VendorBasedOnGstVo;
import com.blackstrawai.keycontact.vendor.VendorContactVo;
import com.blackstrawai.keycontact.vendor.VendorDestinationAddressVo;
import com.blackstrawai.keycontact.vendor.VendorFinanceVo;
import com.blackstrawai.keycontact.vendor.VendorGeneralInformationVo;
import com.blackstrawai.keycontact.vendor.VendorOriginAddressVo;
import com.blackstrawai.keycontact.vendor.VendorVo;
import com.blackstrawai.settings.CommonVo;
import com.blackstrawai.settings.CurrencyDao;
import com.blackstrawai.settings.PaymentTermsDao;
import com.blackstrawai.settings.VendorGroupDao;
import com.blackstrawai.vendorsettings.VamVendorContactVo;
import com.blackstrawai.vendorsettings.VamVendorNameVo;
import com.fasterxml.jackson.databind.ObjectMapper;

@Repository
public class VendorDao extends BaseDao {

	@Autowired
	private AttachmentsDao attachmentsDao;

	/*
	 * @Autowired private ChartOfAccountsDao chartOfAccountsDao;
	 */

	@Autowired
	private FinanceCommonDao financeCommonDao;

	@Autowired
	private PaymentTermsDao paymentTermsDao;

	@Autowired
	private CurrencyDao currencyDao;

	@Autowired
	private VendorGroupDao vendorGroupDao;
	

	private Logger logger = Logger.getLogger(VendorDao.class);

	public boolean createVendor(VendorVo vendorVo) throws ApplicationException {
		logger.info("Entry into method:createVendor");
		boolean isCreatedSuccessfully = false;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		try {

			con = getAccountsPayable();
			con.setAutoCommit(false);
			if (vendorVo.getVendorGeneralInformation().getPan() != null) {
				int availableVendorsForGivenEmailAndOrg = checkVendorExist(
						vendorVo.getVendorGeneralInformation().getPan(), vendorVo.getOrganizationId());
				if (availableVendorsForGivenEmailAndOrg > 0) {
					throw new ApplicationException("Vendor Exist for the Organization");
				}
			}
			String sql = VendorConstants.INSERT_INTO_VENDOR_GENERAL_INFORMATION;
			preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setString(1,
					vendorVo.getVendorGeneralInformation().getPrimaryContact() != null
							? vendorVo.getVendorGeneralInformation().getPrimaryContact()
							: null);
			preparedStatement.setString(2,
					vendorVo.getVendorGeneralInformation().getCompanyName() != null
							? vendorVo.getVendorGeneralInformation().getCompanyName()
							: vendorVo.getVendorGeneralInformation().getVendorDisplayName());
			preparedStatement.setString(3,
					vendorVo.getVendorGeneralInformation().getVendorDisplayName() != null
							? vendorVo.getVendorGeneralInformation().getVendorDisplayName()
							: null);
			preparedStatement.setString(4,
					vendorVo.getVendorGeneralInformation().getEmail() != null
							? vendorVo.getVendorGeneralInformation().getEmail()
							: null);
			preparedStatement.setString(5,
					vendorVo.getVendorGeneralInformation().getPhoneNo() != null
							? vendorVo.getVendorGeneralInformation().getPhoneNo()
							: null);
			preparedStatement.setString(6,
					vendorVo.getVendorGeneralInformation().getWebsite() != null
							? vendorVo.getVendorGeneralInformation().getWebsite()
							: null);
			preparedStatement.setString(7,
					vendorVo.getVendorGeneralInformation().getMobileNo() != null
							? vendorVo.getVendorGeneralInformation().getMobileNo()
							: null);
			preparedStatement.setObject(8, Integer.valueOf(vendorVo.getUserId()));
			preparedStatement.setObject(9, vendorVo.getOrganizationId());
			preparedStatement.setObject(10, vendorVo.getIsSuperAdmin());
			preparedStatement.setString(11,
					vendorVo.getVendorGeneralInformation().getPan() != null
							? vendorVo.getVendorGeneralInformation().getPan()
							: null);
			preparedStatement.setObject(12,
					vendorVo.getVendorGeneralInformation().getVendorOrganizationId() != null
							? vendorVo.getVendorGeneralInformation().getVendorOrganizationId()
							: null);
			preparedStatement.setObject(13,
					vendorVo.getVendorGeneralInformation().getVendorGroupId() != null
							? vendorVo.getVendorGeneralInformation().getVendorGroupId()
							: null);
			preparedStatement.setObject(14,
					vendorVo.getVendorGeneralInformation().getVendorGstTypeId() != null
							? vendorVo.getVendorGeneralInformation().getVendorGstTypeId()
							: null);
			preparedStatement.setString(15,
					vendorVo.getVendorGeneralInformation().getGstNo() != null
							? vendorVo.getVendorGeneralInformation().getGstNo()
							: null);
			if (vendorVo.getVendorOriginAddress() != null) {
				preparedStatement.setObject(16,
						vendorVo.getVendorOriginAddress().getSameBillingDestAddress() != null
								? vendorVo.getVendorOriginAddress().getSameBillingDestAddress()
								: null);
			}else {
				preparedStatement.setObject(16,null);
			}
			preparedStatement.setObject(17,
					vendorVo.getVendorGeneralInformation().getIsMsmeRegistered() != null
							? vendorVo.getVendorGeneralInformation().getIsMsmeRegistered()
							: null);
			preparedStatement.setObject(18,
					vendorVo.getVendorGeneralInformation().getMsmeNumber() != null
							? vendorVo.getVendorGeneralInformation().getMsmeNumber()
							: null);
			preparedStatement.setString(19, vendorVo.getRoleName());
			preparedStatement.setInt(20, vendorVo.getDefaultGlTallyId() != null ? vendorVo.getDefaultGlTallyId() : 0);
			preparedStatement.setString(21,
					vendorVo.getDefaultGlTallyName() != null ? vendorVo.getDefaultGlTallyName() : null);
			BookKeepingSettingsVo bookKeepingSetting = vendorVo.getVendorBookKeepingSetting();
			preparedStatement.setInt(22,
					bookKeepingSetting != null && bookKeepingSetting.getId() > 0 ? bookKeepingSetting.getId() : 0);
			preparedStatement.setString(23,
					bookKeepingSetting != null && bookKeepingSetting.getDefaultGlName() != null
							? bookKeepingSetting.getDefaultGlName()
							: null);
			preparedStatement.setInt(24,
					bookKeepingSetting != null && bookKeepingSetting.getLocationId() > 0
							? bookKeepingSetting.getLocationId()
							: 0);
			preparedStatement.setString(25,
					bookKeepingSetting != null && bookKeepingSetting.getGstNumber() != null
							? bookKeepingSetting.getGstNumber()
							: null);
			ObjectMapper mapper = new ObjectMapper();
			String newJsonData = mapper.writeValueAsString(vendorVo.getVendorGeneralInformation().getOtherGsts() != null
					? vendorVo.getVendorGeneralInformation().getOtherGsts()
					: "");
			preparedStatement.setString(26, newJsonData);
			preparedStatement.setString(27, vendorVo.getStatus() != null ? vendorVo.getStatus() : null);
			preparedStatement.setBoolean(28,
					vendorVo.getVendorGeneralInformation().getIsPanOrGstAvailable() != null
							? vendorVo.getVendorGeneralInformation().getIsPanOrGstAvailable()
							: Boolean.TRUE);
			preparedStatement.setBoolean(29,
					vendorVo.getVendorGeneralInformation().getOverSeasVendor() != null
							? vendorVo.getVendorGeneralInformation().getOverSeasVendor()
							: Boolean.FALSE);
			preparedStatement.setBoolean(30,
					vendorVo.getVendorGeneralInformation().getVendorWithoutPan() != null
							? !vendorVo.getVendorGeneralInformation().getVendorWithoutPan()
							: Boolean.FALSE);
			int rowAffected = preparedStatement.executeUpdate();
			if (rowAffected == 1) {
				rs = preparedStatement.getGeneratedKeys();
				if (rs.next()) {
					vendorVo.getVendorGeneralInformation().setId(rs.getInt(1));
				}
			}
			if (vendorVo.getVendorGeneralInformation().getId() != null) {
				if (vendorVo.getVendorFinance() != null) {
					createVendorFinance(vendorVo.getVendorGeneralInformation().getId(), vendorVo, con);
				}
//				if (vendorVo.getVendorOriginAddress() != null) {
//					createVendorOriginAddress(
//							vendorVo.getVendorGeneralInformation().getId(), vendorVo.getVendorOriginAddress(), con);
//				}
//				if (vendorVo.getVendorDestinationAddress() != null) {
//					createVendorDestinationAddress(vendorVo.getVendorGeneralInformation().getId(), vendorVo.getVendorDestinationAddress(), con);
//				}
				if (vendorVo.getBankDetails() != null && vendorVo.getBankDetails().size() > 0) {
					createVendorBankDetails(vendorVo.getVendorGeneralInformation().getId(), vendorVo, con);
				}
				createContactIfNotExist(vendorVo, con);
//				if (vendorVo.getVendorAdditionalAddresses() != null
//						&& vendorVo.getVendorAdditionalAddresses().size() > 0) {
//					createVendorAdditionalAddresses(vendorVo.getVendorGeneralInformation().getId(),
//							vendorVo.getVendorAdditionalAddresses().stream()
//									.filter(address -> address.getGstNo() == null).collect(Collectors.toList()),
//							con);
//				}
				if (vendorVo.getAttachments() != null && vendorVo.getAttachments().size() > 0) {
					attachmentsDao.createAttachments(vendorVo.getOrganizationId(), vendorVo.getUserId(),
							vendorVo.getAttachments(), AttachmentsConstants.MODULE_TYPE_VENDOR,
							vendorVo.getVendorGeneralInformation().getId(), vendorVo.getRoleName());
				}
				
				createVendorBasedGstAddress(vendorVo, con);
			}
			
			
			con.commit();
			isCreatedSuccessfully = true;
		} catch (ApplicationException e1) {
			throw e1;
		} catch (Exception e) {
			logger.info("Error in createVendor:: ", e);
			try {
				con.rollback();
			} catch (Exception e1) {
				throw new ApplicationException(e1.getMessage());
			}
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return isCreatedSuccessfully;
	}

	/**
	 * @param vendorVo
	 * @param con
	 * @throws ApplicationException
	 */
	private void createVendorBasedGstAddress(VendorVo vendorVo, Connection con) throws ApplicationException {
		for (VendorBasedOnGstVo vendorBasedOnGstVo : vendorVo.getOriginalAndDestinationAddressBasedOnGst()) {
			
			createVendorOriginAddress(vendorVo.getVendorGeneralInformation().getId(),
					vendorBasedOnGstVo.getVendorOriginAddress(), con);
			createVendorDestinationAddress(vendorVo.getVendorGeneralInformation().getId(),
					vendorBasedOnGstVo.getVendorDestinationAddress(), con);
			createVendorAdditionalAddresses(vendorVo.getVendorGeneralInformation().getId(),
					vendorBasedOnGstVo.getVendorAdditionalAddresses(), con);
		}
	}

	/**
	 * @param vendorVo
	 * @param con
	 * @throws ApplicationException
	 */
	private void createContactIfNotExist(VendorVo vendorVo, Connection con) throws ApplicationException {
		if (vendorVo.getContacts() == null || vendorVo.getContacts().isEmpty()) {
			VendorContactVo vendorContactVo = new VendorContactVo();
			vendorContactVo.setFirstName(vendorVo.getVendorGeneralInformation().getPrimaryContact());
			vendorContactVo.setEmailAddress(vendorVo.getVendorGeneralInformation().getEmail());
			vendorContactVo.setMobileNo(vendorVo.getVendorGeneralInformation().getMobileNo());
			vendorContactVo.setIsDefault(Boolean.TRUE);
			List<VendorContactVo> vendorContactVos = new ArrayList<>();
			vendorContactVos.add(vendorContactVo);
			vendorVo.setContacts(vendorContactVos);
		}
		createVendorContact(vendorVo.getVendorGeneralInformation().getId(), vendorVo, con);
	}

	/*
	 * public void insertIntoChartOfAccountsLevel6(int organizationId, String name,
	 * String userId, boolean isSuperAdmin, String displayName) throws
	 * ApplicationException {
	 * 
	 * int level5Id = chartOfAccountsDao.getChartOfAccountsLevel5Id(organizationId,
	 * "Short-term loans and advances", "Loans and advances to related parties",
	 * "Loans and advances to related parties"); if (level5Id > 0) {
	 * chartOfAccountsDao.createLevel6(name, "Vendor", organizationId, userId,
	 * isSuperAdmin, level5Id, false, displayName); } level5Id =
	 * chartOfAccountsDao.getChartOfAccountsLevel5Id(organizationId,
	 * "Short-term loans and advances", "Security deposits", "Security deposits");
	 * if (level5Id > 0) { chartOfAccountsDao.createLevel6(name, "Vendor",
	 * organizationId, userId, isSuperAdmin, level5Id, false, displayName); }
	 * 
	 * level5Id = chartOfAccountsDao.getChartOfAccountsLevel5Id(organizationId,
	 * "Short-term loans and advances", "Prepaid expenses", "Prepaid expenses"); if
	 * (level5Id > 0) { chartOfAccountsDao.createLevel6(name, "Vendor",
	 * organizationId, userId, isSuperAdmin, level5Id, false, displayName); }
	 * 
	 * level5Id = chartOfAccountsDao.getChartOfAccountsLevel5Id(organizationId,
	 * "Short-term loans and advances", "Others", "Advance to vendors"); if
	 * (level5Id > 0) { chartOfAccountsDao.createLevel6(name, "Vendor",
	 * organizationId, userId, isSuperAdmin, level5Id, false, displayName); }
	 * 
	 * level5Id = chartOfAccountsDao.getChartOfAccountsLevel5Id(organizationId,
	 * "Long-term loans and advances", "Capital advances", "NC - Capital advances");
	 * if (level5Id > 0) { chartOfAccountsDao.createLevel6(name, "Vendor",
	 * organizationId, userId, isSuperAdmin, level5Id, false, displayName); }
	 * 
	 * level5Id = chartOfAccountsDao.getChartOfAccountsLevel5Id(organizationId,
	 * "Long-term loans and advances", "Security deposits", "NC- Rental Deposit");
	 * if (level5Id > 0) { chartOfAccountsDao.createLevel6(name, "Vendor",
	 * organizationId, userId, isSuperAdmin, level5Id, false, displayName); }
	 * 
	 * level5Id = chartOfAccountsDao.getChartOfAccountsLevel5Id(organizationId,
	 * "Long-term loans and advances", "Security deposits",
	 * "NC - Electricity Deposit"); if (level5Id > 0) {
	 * chartOfAccountsDao.createLevel6(name, "Vendor", organizationId, userId,
	 * isSuperAdmin, level5Id, false, displayName); }
	 * 
	 * level5Id = chartOfAccountsDao.getChartOfAccountsLevel5Id(organizationId,
	 * "Long-term loans and advances", "Loans and advances to related parties",
	 * "NC - Advance to related party"); if (level5Id > 0) {
	 * chartOfAccountsDao.createLevel6(name, "Vendor", organizationId, userId,
	 * isSuperAdmin, level5Id, false, displayName); }
	 * 
	 * level5Id = chartOfAccountsDao.getChartOfAccountsLevel5Id(organizationId,
	 * "Long-term loans and advances", "Other loans and advances",
	 * "NC - Other loans and advances"); if (level5Id > 0) {
	 * chartOfAccountsDao.createLevel6(name, "Vendor", organizationId, userId,
	 * isSuperAdmin, level5Id, false, displayName); }
	 * 
	 * level5Id = chartOfAccountsDao.getChartOfAccountsLevel5Id(organizationId,
	 * "Long-term loans and advances", "Other loans and advances",
	 * "NC - Advance to vendors"); if (level5Id > 0) {
	 * chartOfAccountsDao.createLevel6(name, "Vendor", organizationId, userId,
	 * isSuperAdmin, level5Id, false, displayName); }
	 * 
	 * level5Id = chartOfAccountsDao.getChartOfAccountsLevel5Id(organizationId,
	 * "Long-term borrowings", "Deposits - Secured", "NC - Deposits - Secured"); if
	 * (level5Id > 0) { chartOfAccountsDao.createLevel6(name, "Vendor",
	 * organizationId, userId, isSuperAdmin, level5Id, false, displayName); }
	 * 
	 * level5Id = chartOfAccountsDao.getChartOfAccountsLevel5Id(organizationId,
	 * "Long-term borrowings", "Deposits - UnSecured", "NC - Deposits - UnSecured");
	 * if (level5Id > 0) { chartOfAccountsDao.createLevel6(name, "Vendor",
	 * organizationId, userId, isSuperAdmin, level5Id, false, displayName); }
	 * 
	 * level5Id = chartOfAccountsDao.getChartOfAccountsLevel5Id(organizationId,
	 * "Other long-term liabilities", "Trade Payables", "NC -  Acceptances"); if
	 * (level5Id > 0) { chartOfAccountsDao.createLevel6(name, "Vendor",
	 * organizationId, userId, isSuperAdmin, level5Id, false, displayName); }
	 * 
	 * level5Id = chartOfAccountsDao.getChartOfAccountsLevel5Id(organizationId,
	 * "Other long-term liabilities", "Trade Payables",
	 * "NC -  Other than Acceptances"); if (level5Id > 0) {
	 * chartOfAccountsDao.createLevel6(name, "Vendor", organizationId, userId,
	 * isSuperAdmin, level5Id, false, displayName); }
	 * 
	 * level5Id = chartOfAccountsDao.getChartOfAccountsLevel5Id(organizationId,
	 * "Other long-term liabilities", "Others",
	 * "NC -  Payables on purchase of fixed assets"); if (level5Id > 0) {
	 * chartOfAccountsDao.createLevel6(name, "Vendor", organizationId, userId,
	 * isSuperAdmin, level5Id, false, displayName); }
	 * 
	 * level5Id = chartOfAccountsDao.getChartOfAccountsLevel5Id(organizationId,
	 * "Other long-term liabilities", "Others",
	 * "NC -  Contractually reimbursable expenses"); if (level5Id > 0) {
	 * chartOfAccountsDao.createLevel6(name, "Vendor", organizationId, userId,
	 * isSuperAdmin, level5Id, false, displayName); }
	 * 
	 * level5Id = chartOfAccountsDao.getChartOfAccountsLevel5Id(organizationId,
	 * "Other long-term liabilities", "Others",
	 * "NC -  Interest accrued but not due on borrowings"); if (level5Id > 0) {
	 * chartOfAccountsDao.createLevel6(name, "Vendor", organizationId, userId,
	 * isSuperAdmin, level5Id, false, displayName); }
	 * 
	 * level5Id = chartOfAccountsDao.getChartOfAccountsLevel5Id(organizationId,
	 * "Other long-term liabilities", "Others",
	 * "NC -  Interest accrued on trade payables"); if (level5Id > 0) {
	 * chartOfAccountsDao.createLevel6(name, "Vendor", organizationId, userId,
	 * isSuperAdmin, level5Id, false, displayName); }
	 * 
	 * level5Id = chartOfAccountsDao.getChartOfAccountsLevel5Id(organizationId,
	 * "Trade payables", "Acceptances", "Sundry Creditors - Foreign currency"); if
	 * (level5Id > 0) { chartOfAccountsDao.createLevel6(name, "Vendor",
	 * organizationId, userId, isSuperAdmin, level5Id, false, displayName); }
	 * 
	 * level5Id = chartOfAccountsDao.getChartOfAccountsLevel5Id(organizationId,
	 * "Trade payables", "Acceptances",
	 * "Sundry Creditors for capital goods  - Foreign currency"); if (level5Id > 0)
	 * { chartOfAccountsDao.createLevel6(name, "Vendor", organizationId, userId,
	 * isSuperAdmin, level5Id, false, displayName); }
	 * 
	 * level5Id = chartOfAccountsDao.getChartOfAccountsLevel5Id(organizationId,
	 * "Trade payables", "Acceptances", "Sundry Creditors - Local currency"); if
	 * (level5Id > 0) { chartOfAccountsDao.createLevel6(name, "Vendor",
	 * organizationId, userId, isSuperAdmin, level5Id, false, displayName); }
	 * 
	 * level5Id = chartOfAccountsDao.getChartOfAccountsLevel5Id(organizationId,
	 * "Trade payables", "Acceptances",
	 * "Sundry Creditors for capex - Local currency"); if (level5Id > 0) {
	 * chartOfAccountsDao.createLevel6(name, "Vendor", organizationId, userId,
	 * isSuperAdmin, level5Id, false, displayName); }
	 * 
	 * level5Id = chartOfAccountsDao.getChartOfAccountsLevel5Id(organizationId,
	 * "Trade payables", "Acceptances", "Accrued Expenses - TDS deducted"); if
	 * (level5Id > 0) { chartOfAccountsDao.createLevel6(name, "Vendor",
	 * organizationId, userId, isSuperAdmin, level5Id, false, displayName); }
	 * 
	 * level5Id = chartOfAccountsDao.getChartOfAccountsLevel5Id(organizationId,
	 * "Trade payables", "Acceptances", "Accrued Expenses - Vendor"); if (level5Id >
	 * 0) { chartOfAccountsDao.createLevel6(name, "Vendor", organizationId, userId,
	 * isSuperAdmin, level5Id, false, displayName); }
	 * 
	 * }
	 */

	private VendorVo createVendorFinance(int vendorId, VendorVo vendorVo, Connection con) throws ApplicationException {
		logger.info("Entry into method:createVendorFinance");
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = con.prepareStatement(VendorConstants.INSERT_INTO_VENDOR_FINANCE);
			preparedStatement.setObject(1,
					vendorVo.getVendorFinance().getCurrencyId());
			preparedStatement.setObject(2,
					vendorVo.getVendorFinance().getPaymentTermsid());
			preparedStatement.setObject(3,
					vendorVo.getVendorFinance().getSourceOfSupplyId() != null
							? vendorVo.getVendorFinance().getSourceOfSupplyId()
							: 1);
			preparedStatement.setObject(4,
					vendorVo.getVendorFinance().getTdsId());
			preparedStatement.setString(5,
					vendorVo.getVendorFinance().getOpeningBalance());
			preparedStatement.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
			preparedStatement.setInt(7, vendorId);
			preparedStatement.executeUpdate();
		} catch (Exception e) {
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(null, preparedStatement, null);
		}
		return vendorVo;
	}

	private VendorOriginAddressVo createVendorOriginAddress(int vendorId, VendorOriginAddressVo vendorOriginAddressVo,
			Connection con) throws ApplicationException {
		logger.info("Entry into method:createVendorOriginAddress");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		if (vendorOriginAddressVo != null) {
			try {
				String sql = VendorConstants.INSERT_INTO_VENDOR_ORIGIN_ADDRESS;
				preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				preparedStatement.setString(1, vendorOriginAddressVo.getAttention());
				preparedStatement.setObject(2, vendorOriginAddressVo.getCountry());
				preparedStatement.setString(3, vendorOriginAddressVo.getAddress_1());
				preparedStatement.setString(4, vendorOriginAddressVo.getAddress_2());
				preparedStatement.setObject(5, vendorOriginAddressVo.getState());
				preparedStatement.setString(6, vendorOriginAddressVo.getCity());
				preparedStatement.setString(7, vendorOriginAddressVo.getZipCode());
				preparedStatement.setString(8, vendorOriginAddressVo.getPhoneNo());
				preparedStatement.setString(9, vendorOriginAddressVo.getLandMark());
				preparedStatement.setTimestamp(10, new Timestamp(System.currentTimeMillis()));
				preparedStatement.setInt(11,
						vendorOriginAddressVo.getSelectedAddress() != null ? vendorOriginAddressVo.getSelectedAddress()
								: 0);
				preparedStatement.setString(12, vendorOriginAddressVo.getGstNo());
				preparedStatement.setInt(13,
						vendorOriginAddressVo.getAddressType() != null ? vendorOriginAddressVo.getAddressType() : 0);
				preparedStatement.setInt(14, vendorId);
				int rowAffected = preparedStatement.executeUpdate();
				if (rowAffected == 1) {
					rs = preparedStatement.getGeneratedKeys();
					if (rs.next()) {
						vendorOriginAddressVo.setId(rs.getInt(1));
					}
				}
			} catch (Exception e) {
				throw new ApplicationException(e.getMessage());

			} finally {
				closeResources(rs, preparedStatement, null);

			}
		}

		return vendorOriginAddressVo;
	}

	/**
	 * Creates the vendor destination address.
	 *
	 * @param vendorId the vendor id
	 * @param vendorDestinationAddressVo the vendor destination address vo
	 * @param con the con
	 * @return the vendor destination address vo
	 * @throws ApplicationException the application exception
	 */
	private VendorDestinationAddressVo createVendorDestinationAddress(int vendorId, VendorDestinationAddressVo vendorDestinationAddressVo, Connection con)
			throws ApplicationException {
		logger.info("Entry into method:createVendorDestinationAddress");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		
		if (vendorDestinationAddressVo != null) {
			try {
				String sql = VendorConstants.INSERT_INTO_VENDOR_DESTINATION_ADDRESS;
				preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				preparedStatement.setString(1,
						vendorDestinationAddressVo.getAttention() != null ? vendorDestinationAddressVo.getAttention()
								: null);
				preparedStatement.setObject(2, vendorDestinationAddressVo.getCountry());
				preparedStatement.setString(3, vendorDestinationAddressVo.getAddress_1());
				preparedStatement.setString(4, vendorDestinationAddressVo.getAddress_2());
				preparedStatement.setObject(5, vendorDestinationAddressVo.getState());
				preparedStatement.setString(6, vendorDestinationAddressVo.getCity());
				preparedStatement.setString(7,
						vendorDestinationAddressVo.getZipCode() != null ? vendorDestinationAddressVo.getZipCode()
								: null);
				preparedStatement.setString(8,
						vendorDestinationAddressVo.getPhoneNo() != null ? vendorDestinationAddressVo.getPhoneNo()
								: null);
				preparedStatement.setString(9,
						vendorDestinationAddressVo.getLandMark() != null ? vendorDestinationAddressVo.getLandMark()
								: null);
				preparedStatement.setTimestamp(10, new Timestamp(System.currentTimeMillis()));
				preparedStatement.setInt(11,
						vendorDestinationAddressVo.getSelectedAddress() != null
								? vendorDestinationAddressVo.getSelectedAddress()
								: 0);
				preparedStatement.setString(12,
						vendorDestinationAddressVo.getGstNo() != null ? vendorDestinationAddressVo.getGstNo() : null);
				preparedStatement.setInt(13,
						vendorDestinationAddressVo.getAddressType() != null
								? vendorDestinationAddressVo.getAddressType()
								: 0);
				preparedStatement.setInt(14, vendorId);
				int rowAffected = preparedStatement.executeUpdate();
				if (rowAffected == 1) {
					rs = preparedStatement.getGeneratedKeys();
					if (rs.next()) {
						vendorDestinationAddressVo.setId(rs.getInt(1));
					}
				}
			} catch (Exception e) {
				throw new ApplicationException(e.getMessage());

			} finally {
				closeResources(rs, preparedStatement, null);

			}
		}

		return vendorDestinationAddressVo;
	}

	private VendorVo createVendorBankDetails(int vendorId, VendorVo vendorVo, Connection con)
			throws ApplicationException {
		logger.info("Entry into method:createVendorBankDetails");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			String sql = VendorConstants.INSERT_INTO_VENDOR_BANK_DETAILS;
			for (int i = 0; i < vendorVo.getBankDetails().size(); i++) {
				VendorBankDetailsVo vendorBankDetailsVo = vendorVo.getBankDetails().get(i);
				preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				preparedStatement.setString(1,
						vendorVo.getBankDetails().get(i).getBankName() != null
								? vendorVo.getBankDetails().get(i).getBankName()
								: null);
				preparedStatement.setString(2,
						vendorVo.getBankDetails().get(i).getAccountNumber() != null
								? vendorVo.getBankDetails().get(i).getAccountNumber()
								: null);
				preparedStatement.setString(3,
						vendorVo.getBankDetails().get(i).getAccountHolderName() != null
								? vendorVo.getBankDetails().get(i).getAccountHolderName()
								: null);
				preparedStatement.setString(4,
						vendorVo.getBankDetails().get(i).getBranchName() != null
								? vendorVo.getBankDetails().get(i).getBranchName()
								: null);
				preparedStatement.setString(5,
						vendorVo.getBankDetails().get(i).getIfscCode() != null
								? vendorVo.getBankDetails().get(i).getIfscCode()
								: null);
				preparedStatement.setString(6,
						vendorVo.getBankDetails().get(i).getUpiId() != null
								? vendorVo.getBankDetails().get(i).getUpiId()
								: null);
				preparedStatement.setBoolean(7,
						vendorVo.getBankDetails().get(i).getIsDefault() != null
								? vendorVo.getBankDetails().get(i).getIsDefault()
								: null);
				preparedStatement.setInt(8, vendorId);
				int rowAffected = preparedStatement.executeUpdate();
				if (rowAffected == 1) {
					rs = preparedStatement.getGeneratedKeys();
					if (rs.next()) {
						vendorBankDetailsVo.setId(rs.getInt(1));
					}
				}
			}
		} catch (Exception e) {
			throw new ApplicationException(e.getMessage());

		} finally {
			closeResources(rs, preparedStatement, null);

		}

		return vendorVo;
	}

	private void createVendorAdditionalAddresses(int vendorId, List<BaseAddressVo> baseAddressVos, Connection con)
			throws ApplicationException {
		logger.info("Entry into method: createVendorAdditionalAddresses");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		if (baseAddressVos != null) {
			try {
				String sql = VendorConstants.INSERT_INTO_VENDOR_ADDITIONAL_ADDRESS;
				for (int i = 0; i < baseAddressVos.size(); i++) {
					BaseAddressVo vendorBaseAddressVo = baseAddressVos.get(i);

					preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
					preparedStatement.setString(1,
							vendorBaseAddressVo.getAttention() != null ? vendorBaseAddressVo.getAttention() : null);
					preparedStatement.setObject(2,
							vendorBaseAddressVo.getCountry() != null ? vendorBaseAddressVo.getCountry() : null);
					preparedStatement.setString(3,
							vendorBaseAddressVo.getAddress_1() != null ? vendorBaseAddressVo.getAddress_1() : null);
					preparedStatement.setString(4,
							vendorBaseAddressVo.getAddress_2() != null ? vendorBaseAddressVo.getAddress_2() : null);
					preparedStatement.setObject(5,
							vendorBaseAddressVo.getState() != null ? vendorBaseAddressVo.getState() : null);
					preparedStatement.setString(6,
							vendorBaseAddressVo.getCity() != null ? vendorBaseAddressVo.getCity() : null);
					preparedStatement.setString(7,
							vendorBaseAddressVo.getZipCode() != null ? vendorBaseAddressVo.getZipCode() : null);
					preparedStatement.setString(8,
							vendorBaseAddressVo.getPhoneNo() != null ? vendorBaseAddressVo.getPhoneNo() : null);
					preparedStatement.setString(9,
							vendorBaseAddressVo.getLandMark() != null ? vendorBaseAddressVo.getLandMark() : null);
					preparedStatement.setInt(10, vendorId);
					preparedStatement.setInt(11,
							vendorBaseAddressVo.getSelectedAddress() != null ? vendorBaseAddressVo.getSelectedAddress()
									: 0);
					preparedStatement.setString(12,
							vendorBaseAddressVo.getGstNo() != null ? vendorBaseAddressVo.getGstNo() : null);
					preparedStatement.setInt(13,
							vendorBaseAddressVo.getAddressType() != null ? vendorBaseAddressVo.getAddressType() : 0);

					int rowAffected = preparedStatement.executeUpdate();
					if (rowAffected == 1) {
						rs = preparedStatement.getGeneratedKeys();
						if (rs.next()) {
							vendorBaseAddressVo.setId(rs.getInt(1));
						}
					}
				}
			} catch (Exception e) {
				throw new ApplicationException(e.getMessage());

			} finally {
				closeResources(rs, preparedStatement, null);

			}
		}

	}

	private VendorVo createVendorContact(int vendorId, VendorVo vendorVo, Connection con) throws ApplicationException {
		logger.info("Entry into method:createVendorContact");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			String sql = VendorConstants.INSERT_INTO_VENDOR_CONTACT;
			for (int i = 0; i < vendorVo.getContacts().size(); i++) {
				VendorContactVo vendorContactVo = vendorVo.getContacts().get(i);
				preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				preparedStatement.setString(1,
						vendorContactVo.getSalutation() != null ? vendorContactVo.getSalutation() : null);
				preparedStatement.setString(2,
						vendorContactVo.getFirstName() != null ? vendorContactVo.getFirstName() : null);
				preparedStatement.setString(3,
						vendorContactVo.getLastName() != null ? vendorContactVo.getLastName() : null);
				preparedStatement.setString(4,
						vendorContactVo.getWorkNo() != null ? vendorContactVo.getWorkNo() : null);
				preparedStatement.setString(5,
						vendorContactVo.getMobileNo() != null ? vendorContactVo.getMobileNo() : null);
				preparedStatement.setString(6,
						vendorContactVo.getEmailAddress() != null ? vendorContactVo.getEmailAddress() : null);
				preparedStatement.setInt(7, vendorId);
				preparedStatement.setBoolean(8,
						vendorContactVo.getIsDefault() != null ? vendorContactVo.getIsDefault() : Boolean.FALSE);
				int rowAffected = preparedStatement.executeUpdate();
				if (rowAffected == 1) {
					rs = preparedStatement.getGeneratedKeys();
					if (rs.next()) {
						vendorContactVo.setId(rs.getInt(1));
					}
				}
			}
		} catch (Exception e) {
			throw new ApplicationException(e.getMessage());

		} finally {
			closeResources(rs, preparedStatement, null);

		}

		return vendorVo;
	}

	private int checkVendorExist(String panNo, int organizationId) throws ApplicationException {
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		int records = 0;
		try {
			con = getAccountsPayable();
			String query = VendorConstants.CHECK_VENDOR_EXIST_FOR_ORGANIZATION;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, organizationId);
			preparedStatement.setString(2, panNo);
			rs = preparedStatement.executeQuery();
			if (rs.next()) {
				records = rs.getInt(1);
			}
		} catch (Exception e) {
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, con);

		}
		return records;
	}
	
	
	private int checkVendorExistForUpdateVendor(String panNo, int organizationId, int vendorGeneralInformationId) throws ApplicationException {
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		int records = 0;
		try {
			con = getAccountsPayable();
			String query = VendorConstants.CHECK_VENDOR_EXIST_FOR_UPDATE_VENDOR_GIVEN_ORGANIZATION;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, organizationId);
			preparedStatement.setString(2, panNo);
			preparedStatement.setInt(3, vendorGeneralInformationId);
			rs = preparedStatement.executeQuery();
			if (rs.next()) {
				records = rs.getInt(1);
			}
		} catch (Exception e) {
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, con);

		}
		return records;
	}

	public VendorVo deleteVendor(int vendorId, String status, String userId, String roleName)
			throws ApplicationException {
		logger.info("Entry into method: deleteVendor");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		VendorVo vendorVo = new VendorVo();
		try {
			con = getAccountsPayable();
			String sql = VendorConstants.DELETE_VENDOR;
			preparedStatement = con.prepareStatement(sql);
			preparedStatement.setString(1, status);
			preparedStatement.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
			preparedStatement.setString(3, userId);
			preparedStatement.setString(4, roleName);
			preparedStatement.setInt(5, vendorId);
			vendorVo.setVendorId(vendorId);
			preparedStatement.executeUpdate();
		} catch (Exception e) {
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return vendorVo;
	}

	/*
	 * public List<VendorVo> getAllVendorsOfAnOrganization(int organizationId)
	 * throws ApplicationException {
	 * logger.info("Entry into method:getAllVendorsOfAnOrganization"); Connection
	 * con = null; PreparedStatement preparedStatement = null; ResultSet rs = null;
	 * List<VendorVo> listAllVendors = new ArrayList<VendorVo>(); try { con =
	 * getAccountsPayable(); String query =
	 * VendorConstants.GET_VENDORS_GENERAL_INFO_BY_ORGANIZATION; preparedStatement =
	 * con.prepareStatement(query); preparedStatement.setInt(1, organizationId); rs
	 * = preparedStatement.executeQuery(); while (rs.next()) { VendorVo vendorVo =
	 * new VendorVo(); VendorGeneralInformationVo vendorGeneralInformation = new
	 * VendorGeneralInformationVo(); vendorGeneralInformation.setId(rs.getInt(1));
	 * vendorGeneralInformation.setPrimaryContact(rs.getString(2));
	 * vendorGeneralInformation.setCompanyName(rs.getString(3));
	 * vendorGeneralInformation.setVendorDisplayName(rs.getString(4));
	 * vendorGeneralInformation.setEmail(rs.getString(5));
	 * vendorGeneralInformation.setPhoneNo(rs.getString(6));
	 * vendorGeneralInformation.setWebsite(rs.getString(7));
	 * vendorGeneralInformation.setMobileNo(rs.getString(8));
	 * vendorVo.setStatus(rs.getString(9)); vendorVo.setUserId(rs.getString(10));
	 * vendorVo.setOrganizationId(rs.getInt(11));
	 * vendorVo.setIsSuperAdmin(rs.getBoolean(12));
	 * vendorGeneralInformation.setPan(rs.getString(13));
	 * vendorGeneralInformation.setVendorOrganizationId(rs.getInt(14));
	 * vendorGeneralInformation.setVendorGroupId(rs.getInt(15));
	 * vendorGeneralInformation.setVendorGstTypeId(rs.getInt(16));
	 * vendorGeneralInformation.setGstNo(rs.getString(17)); Integer
	 * poCountByVendorId = getPoCountByVendorId(con, rs.getInt(1), organizationId);
	 * vendorGeneralInformation.setPoCount(poCountByVendorId);
	 * vendorVo.setVendorGeneralInformation(vendorGeneralInformation);
	 * getVendorsFinanceDetails(con, organizationId, vendorVo);
	 * getVendorsOriginAddress(con, organizationId, vendorVo);
	 * getVendorsDestinationAddress(con, organizationId, vendorVo);
	 * getVendorsBankDetails(con, organizationId, vendorVo);
	 * getVendorsContactDetails(con, organizationId, vendorVo);
	 * listAllVendors.add(vendorVo);
	 * 
	 * } } catch (Exception e) { throw new ApplicationException(e.getMessage()); }
	 * finally { closeResources(rs, preparedStatement, con);
	 * 
	 * } return listAllVendors;
	 * 
	 * }
	 */

	public List<VendorVo> getAllVendorsOfAnOrganizationForUserAndRole(int organizationId, String userId,
			String roleName) throws ApplicationException {
		logger.info("Entry into method:getAllVendorsOfAnOrganization");
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		List<VendorVo> listAllVendors = new ArrayList<VendorVo>();
		try {
			con = getAccountsPayable();
			String query = "";
			if (roleName.equals("Super Admin")) {
				query = VendorConstants.GET_VENDOR_INFO_BY_ORGANIZATION;
			} else {
				query = VendorConstants.GET_VENDORS_GENERAL_INFO_BY_ORGANIZATION_USER_ROLE;
			}
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, organizationId);
			if (!(roleName.equals("Super Admin"))) {
				preparedStatement.setString(2, userId);
				preparedStatement.setString(3, roleName);
			}
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				VendorVo vendorVo = new VendorVo();
				VendorGeneralInformationVo vendorGeneralInformation = new VendorGeneralInformationVo();
				vendorGeneralInformation.setId(rs.getInt(1));
				vendorGeneralInformation.setPrimaryContact(rs.getString(2));
				vendorGeneralInformation.setCompanyName(rs.getString(3));
				vendorGeneralInformation.setVendorDisplayName(rs.getString(4));
				vendorGeneralInformation.setEmail(rs.getString(5));
				vendorGeneralInformation.setPhoneNo(rs.getString(6));
				vendorGeneralInformation.setWebsite(rs.getString(7));
				vendorGeneralInformation.setMobileNo(rs.getString(8));
				vendorVo.setStatus(rs.getString(9));
				vendorVo.setUserId(rs.getString(10));
				vendorVo.setOrganizationId(rs.getInt(11));
				vendorVo.setIsSuperAdmin(rs.getBoolean(12));
				vendorGeneralInformation.setPan(rs.getString(13));
				vendorGeneralInformation.setVendorOrganizationId(rs.getInt(14));
				vendorGeneralInformation.setVendorGroupId(rs.getInt(15));
				vendorGeneralInformation.setVendorGstTypeId(rs.getInt(16));
				vendorGeneralInformation.setGstNo(rs.getString(17));
				Integer poCountByVendorId = getPoCountByVendorId(con, rs.getInt(1), organizationId);
				vendorGeneralInformation.setPoCount(poCountByVendorId);
				vendorVo.setVendorGeneralInformation(vendorGeneralInformation);
//				getVendorsFinanceDetails(con, organizationId, vendorVo);
//				getVendorsOriginAddress(con, organizationId, vendorVo);
//				getVendorsDestinationAddress(con, organizationId, vendorVo);
//				getVendorsBankDetails(con, organizationId, vendorVo);
//				getVendorsContactDetails(con, organizationId, vendorVo);
				listAllVendors.add(vendorVo);

			}
		} catch (Exception e) {
			logger.error("Error in method:getAllVendorsOfAnOrganization", e);
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, con);

		}
		return listAllVendors;

	}

	private Integer getPoCountByVendorId(Connection con, int vendorId, int organizationId) throws ApplicationException {
		logger.info("Entry into method:getPoCountByVendorId");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Integer poCount = 0;
		try {
			String query = VendorConstants.GET_PO_COUNT_BY_VENDOR;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, vendorId);
			preparedStatement.setInt(2, organizationId);
			preparedStatement.setString(3, CommonConstants.STATUS_AS_DELETE);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				poCount = rs.getInt(1);
			}
		} catch (Exception e) {
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, null);

		}
		return poCount;
	}

	/*private VendorVo getVendorsFinanceDetails(Connection con, int organizationId, VendorVo vendorVo)
			throws ApplicationException {
		logger.info("Entry into method:getVendorsFinanceDetails");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			String query = VendorConstants.GET_VENDORS_FINANCE_BY_ORGANIZATION;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, organizationId);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				VendorFinanceVo vendorFinance = new VendorFinanceVo();
				vendorFinance.setId(rs.getInt(1));
				vendorFinance.setCurrencyId(rs.getInt(2));
				vendorFinance.setPaymentTermsid(rs.getInt(3));
				vendorFinance.setSourceOfSupplyId(rs.getInt(4));
				vendorFinance.setTdsId(rs.getInt(5));
				vendorFinance.setOpeningBalance(rs.getString(6));
				vendorVo.setVendorFinance(vendorFinance);
			}
		} catch (Exception e) {
			logger.error("Error in method:getVendorsFinanceDetails", e);
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, null);
		}
		return vendorVo;
	}
*/
	/*private VendorVo getVendorsOriginAddress(Connection con, int organizationId, VendorVo vendorVo)
			throws ApplicationException {
		logger.info("Entry into method:getVendorsOriginAddress");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			String query = VendorConstants.GET_VENDORS_ORIGIN_ADDRESS_BY_ORGANIZATION;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, organizationId);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				VendorOriginAddressVo vendorOriginAddress = new VendorOriginAddressVo();
				vendorOriginAddress.setId(rs.getInt(1));
				vendorOriginAddress.setAttention(rs.getString(2));
				vendorOriginAddress.setCountry(Integer.parseInt(rs.getString(3) != null ? rs.getString(3) : "0"));
				vendorOriginAddress.setAddress_1(rs.getString(4));
				vendorOriginAddress.setAddress_2(rs.getString(5));
				vendorOriginAddress.setState(Integer.parseInt(rs.getString(6) != null ? rs.getString(6) : "0"));
				vendorOriginAddress.setCity(rs.getString(7));
				vendorOriginAddress.setZipCode(rs.getString(8));
				vendorOriginAddress.setPhoneNo(rs.getString(9));
				vendorOriginAddress.setLandMark(rs.getString(10));
				vendorVo.setVendorOriginAddress(vendorOriginAddress);
			}
		} catch (Exception e) {
			logger.error("Error in method:getVendorsOriginAddress", e);
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, null);

		}
		return vendorVo;
	}
*/
	/*private VendorVo getVendorsDestinationAddress(Connection con, int organizationId, VendorVo vendorVo)
			throws ApplicationException {
		logger.info("Entry into method:getVendorsDestinationAddress");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;

		try {
			String query = VendorConstants.GET_VENDORS_DESTINATION_ADDRESS_BY_ORGANIZATION;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, organizationId);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				VendorDestinationAddressVo vendorDestinationAddress = new VendorDestinationAddressVo();
				vendorDestinationAddress.setId(rs.getInt(1));
				vendorDestinationAddress.setAttention(rs.getString(2));
				vendorDestinationAddress.setCountry(Integer.parseInt(rs.getString(3) != null ? rs.getString(3) : "0"));
				vendorDestinationAddress.setAddress_1(rs.getString(4));
				vendorDestinationAddress.setAddress_2(rs.getString(5));
				vendorDestinationAddress.setState(Integer.parseInt(rs.getString(6) != null ? rs.getString(6) : "0"));
				vendorDestinationAddress.setCity(rs.getString(7));
				vendorDestinationAddress.setZipCode(rs.getString(8));
				vendorDestinationAddress.setPhoneNo(rs.getString(9));
				vendorDestinationAddress.setLandMark(rs.getString(10));
				vendorVo.setVendorDestinationAddress(vendorDestinationAddress);
			}
		} catch (Exception e) {
			logger.error("Error in method:getVendorsDestinationAddress", e);
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, null);

		}

		return vendorVo;
	}

	private VendorVo getVendorsBankDetails(Connection con, int organizationId, VendorVo vendorVo)
			throws ApplicationException {
		logger.info("Entry into method:getVendorsBankDetails");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		List<VendorBankDetailsVo> vendorBankDetailsList = new ArrayList<VendorBankDetailsVo>();
		try {
			String query = VendorConstants.GET_VENDORS_BANK_DETAILS_BY_ORGANIZATION;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, organizationId);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				VendorBankDetailsVo vendorBankDetails = new VendorBankDetailsVo();
				vendorBankDetails.setId(rs.getInt(1));
				vendorBankDetails.setBankName(rs.getString(2));
				vendorBankDetails.setAccountNumber(rs.getString(3));
				vendorBankDetails.setConfirmAccountNumber(rs.getString(3));
				vendorBankDetails.setAccountHolderName(rs.getString(4));
				vendorBankDetails.setBranchName(rs.getString(5));
				vendorBankDetails.setIfscCode(rs.getString(6));
				vendorBankDetails.setUpiId(rs.getString(7));
				vendorBankDetails.setIsDefault(rs.getBoolean(8));
				vendorBankDetailsList.add(vendorBankDetails);
				vendorVo.setBankDetails(vendorBankDetailsList);
			}
		} catch (Exception e) {
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, null);

		}
		return vendorVo;
	}

	private VendorVo getVendorsContactDetails(Connection con, int organizationId, VendorVo vendorVo)
			throws ApplicationException {
		logger.info("Entry into method:getVendorsContactDetails");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		List<VendorContactVo> vendorContactList = new ArrayList<VendorContactVo>();
		try {
			String queryToGetVendorContact = VendorConstants.GET_VENDORS_CONTACT_BY_ORGANIZATION;
			preparedStatement = con.prepareStatement(queryToGetVendorContact);
			preparedStatement.setInt(1, organizationId);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				VendorContactVo vendorContact = new VendorContactVo();
				vendorContact.setId(rs.getInt(1));
				vendorContact.setSalutation(rs.getString(2));
				vendorContact.setFirstName(rs.getString(3));
				vendorContact.setLastName(rs.getString(4));
				vendorContact.setWorkNo(rs.getString(5));
				vendorContact.setMobileNo(rs.getString(6));
				vendorContact.setEmailAddress(rs.getString(7));
				vendorContact.setStatus(rs.getString(8));
				vendorContactList.add(vendorContact);
				vendorVo.setContacts(vendorContactList);
			}
		} catch (Exception e) {
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, null);

		}
		return vendorVo;
	}
*/
	public VendorVo getVendorByVendorId(Integer vendorId) throws ApplicationException {
		logger.info("Entry into method:getVendorByVendorId");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		VendorVo vendorVo = null;
		try {
			if (vendorId != null) {
				con = getAccountsPayable();
				vendorVo = getVendorGeneralInfo(vendorId, con);
				if (vendorVo != null) {
					vendorVo.setVendorId(vendorId);
					vendorVo.setVendorFinance(getVendorsFinanceDetailsByVendorId(con, vendorId, vendorVo));
					List<VendorOriginAddressVo> vendorOriginAddressVosForOtherGst = getVendorsOriginAddressByVendorId(
							con, vendorId, vendorVo);
					List<VendorDestinationAddressVo> vendorDestinationAddressVosForOtherGst = getVendorsDestinationAddressByVendorId(
							con, vendorId, vendorVo);
					vendorVo.setBankDetails(getVendorsBankDetailsByVendorId(con, vendorId));
					vendorVo.setContacts(getVendorsContactDetailsByVendorId(con, vendorId, vendorVo));					
					List<BaseAddressVo> vendorAdditionalAddressesOfOtherGst = getVendorAdditionalAddressDetailsByVendorId(
							con, vendorId, vendorVo);
					List<UploadFileVo> uploadFileVos = new ArrayList<>();
					List<VendorBasedOnGstVo> originalAndDestinationAddressBasedOnGst = setAddressBasedOnGst(
							vendorOriginAddressVosForOtherGst, vendorDestinationAddressVosForOtherGst,
							vendorAdditionalAddressesOfOtherGst);
					vendorVo.setOriginalAndDestinationAddressBasedOnGst(originalAndDestinationAddressBasedOnGst);

					for (AttachmentsVo attachments : attachmentsDao.getAttachmentDetails(vendorId,
							AttachmentsConstants.MODULE_TYPE_VENDOR)) {
						UploadFileVo uploadFileVo = new UploadFileVo();
						uploadFileVo.setId(attachments.getId());
						uploadFileVo.setName(attachments.getFileName());
						uploadFileVo.setSize(attachments.getSize());
						uploadFileVo.setDocumentType(attachments.getDocumentTypeId());
						uploadFileVos.add(uploadFileVo);
					}

					vendorVo.setAttachments(uploadFileVos);
				}
			}
		} catch (Exception e) {
			logger.error("Error in get by ID:", e);
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return vendorVo;
	}

	/**
	 * Sets the address based on gst.
	 *
	 * @param vendorOriginAddressVosForOtherGst the vendor origin address vos for other gst
	 * @param vendorDestinationAddressVosForOtherGst the vendor destination address vos for other gst
	 * @param vendorAdditionalAddressesOfOtherGst the vendor additional addresses of other gst
	 * @return the list
	 */
	private List<VendorBasedOnGstVo> setAddressBasedOnGst(List<VendorOriginAddressVo> vendorOriginAddressVosForOtherGst,
			List<VendorDestinationAddressVo> vendorDestinationAddressVosForOtherGst,
			List<BaseAddressVo> vendorAdditionalAddressesOfOtherGst) {
		List<VendorBasedOnGstVo> originalAndDestinationAddressBasedOnGst = new ArrayList<>();
		
		for(VendorOriginAddressVo vendorOriginalAddress: vendorOriginAddressVosForOtherGst) {
			VendorBasedOnGstVo vendorBasedOnGstVo = new VendorBasedOnGstVo();
			vendorBasedOnGstVo.setGstNo(vendorOriginalAddress.getGstNo());
			vendorBasedOnGstVo.setVendorOriginAddress(vendorOriginalAddress);
			Optional<VendorDestinationAddressVo> destinationAddressVo = vendorDestinationAddressVosForOtherGst
					.stream()
					.filter(destinationAddress -> compare(destinationAddress.getGstNo(), vendorOriginalAddress.getGstNo()))
					.findFirst();
			if(destinationAddressVo.isPresent()) {
				vendorBasedOnGstVo.setVendorDestinationAddress(destinationAddressVo.get());
			}
			vendorBasedOnGstVo.setVendorAdditionalAddresses(new ArrayList<>());
			vendorAdditionalAddressesOfOtherGst.stream()
					.filter(vendorAdditionalAddress -> compare(vendorAdditionalAddress.getGstNo(), vendorOriginalAddress.getGstNo()))
					.forEach(address -> vendorBasedOnGstVo.getVendorAdditionalAddresses().add(address));
			originalAndDestinationAddressBasedOnGst.add(vendorBasedOnGstVo);						
		}
		return originalAndDestinationAddressBasedOnGst;
	}
	
	public static boolean compare(String str1, String str2) {
	    return (str1 == null ? str2 == null : str1.equals(str2));
	}

	private VendorVo getVendorGeneralInfo(Integer vendorId, Connection con) throws ApplicationException {
		logger.info("Entry into method: getVendorGeneralInfo");
		String query = VendorConstants.GET_VENDOR_GENERAL_INFO_BY_VENDOR_ID;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		VendorGeneralInformationVo vendorGeneralInformation = new VendorGeneralInformationVo();
		VendorVo vendorVo = new VendorVo();
		VendorOriginAddressVo vendorOriginAddressVo = new VendorOriginAddressVo();
		try {
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, vendorId);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				vendorGeneralInformation.setId(rs.getInt(1));
				vendorGeneralInformation.setPrimaryContact(rs.getString(2));
				vendorGeneralInformation.setCompanyName(rs.getString(3));
				vendorGeneralInformation.setVendorDisplayName(rs.getString(4));
				vendorGeneralInformation.setEmail(rs.getString(5));
				vendorGeneralInformation.setPhoneNo(rs.getString(6));
				vendorGeneralInformation.setWebsite(rs.getString(7));
				vendorGeneralInformation.setMobileNo(rs.getString(8));
				vendorVo.setStatus(rs.getString(9));
				vendorVo.setUserId(rs.getString(10));
				vendorVo.setOrganizationId(rs.getInt(11));
				vendorVo.setIsSuperAdmin(rs.getBoolean(12));
				vendorGeneralInformation.setPan(rs.getString(13));
				vendorGeneralInformation.setVendorOrganizationId(rs.getObject(14) != null ? rs.getInt(14) : null);
				vendorGeneralInformation.setVendorGroupId(rs.getObject(15) != null ? rs.getInt(15) : null);
				vendorGeneralInformation.setVendorGstTypeId(rs.getObject(16) != null ? rs.getInt(16) : null);
				vendorGeneralInformation.setGstNo(rs.getString(17));
				Boolean SameBillingDestAddress = rs.getBoolean(18);
				vendorGeneralInformation.setIsMsmeRegistered(rs.getObject(19) != null ? rs.getBoolean(19) : null);
				vendorGeneralInformation.setMsmeNumber(rs.getString(20));
				vendorVo.setDefaultGlTallyId(rs.getInt(21));
				vendorVo.setDefaultGlTallyName(rs.getString(22));
				BookKeepingSettingsVo bookKeepingSettingsVo = new BookKeepingSettingsVo();
				bookKeepingSettingsVo.setId(rs.getInt(23));
				bookKeepingSettingsVo.setDefaultGlName(rs.getString(24));
				bookKeepingSettingsVo.setLocationId(rs.getInt(25));
				bookKeepingSettingsVo.setGstNumber(rs.getString(26));
				vendorVo.setVendorBookKeepingSetting(bookKeepingSettingsVo);
				vendorOriginAddressVo.setSameBillingDestAddress(SameBillingDestAddress);
				
				vendorVo.setVendorOriginAddress(vendorOriginAddressVo);
				String json = rs.getString(27);
				if (json != null && json.length() > 2) {
					ObjectMapper mapper = new ObjectMapper();
					vendorGeneralInformation.setOtherGsts(mapper.readValue(json, String[].class));
				}
				vendorGeneralInformation.setIsPanOrGstAvailable(rs.getObject(28) != null ? rs.getBoolean(28) : null);
				vendorGeneralInformation.setOverSeasVendor(rs.getObject(29) != null ? rs.getBoolean(29) : null);
				vendorGeneralInformation.setVendorWithoutPan(rs.getObject(30) != null ? rs.getBoolean(30) : null);	
				vendorVo.setVendorGeneralInformation(vendorGeneralInformation);
				}
		} catch (Exception e) {
			logger.error("Error in getby Id:", e);
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, null);
		}
		return vendorVo;
	}

	private VendorFinanceVo getVendorsFinanceDetailsByVendorId(Connection con, int vendorId, VendorVo vendorVo)
			throws ApplicationException {
		logger.info("Entry into method:getVendorFinanceDetailByVendorId");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		VendorFinanceVo vendorFinance = new VendorFinanceVo();
		try {
			String query = VendorConstants.GET_VENDOR_FINANCE_BY_VENDOR_ID;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, vendorId);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				vendorFinance.setId(rs.getObject(1) != null ? rs.getInt(1) : null);
				vendorFinance.setCurrencyId(rs.getObject(2) != null ? rs.getInt(2) : null);
				vendorFinance.setPaymentTermsid(rs.getObject(3) != null ? rs.getInt(3) : null);
				vendorFinance.setSourceOfSupplyId(rs.getObject(4) != null ? rs.getInt(4) : null);
				vendorFinance.setTdsId(rs.getObject(5) != null ? rs.getInt(5) : null);
				vendorFinance.setOpeningBalance(rs.getString(6));
			}
		} catch (Exception e) {
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, null);
		}
		return vendorFinance;
	}

	private List<VendorOriginAddressVo> getVendorsOriginAddressByVendorId(Connection con, int vendorId, VendorVo vendorVo)
			throws ApplicationException {
		logger.info("Entry into method: getVendorOriginAddressByVendorId");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		List<VendorOriginAddressVo> vendorOriginAddressVosForOtherGst=  new ArrayList<>();
		try {
			String query = VendorConstants.GET_VENDOR_ORIGIN_ADDRESS_BY_VENDOR_ID;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, vendorId);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				if (rs.getString(12) != null && vendorVo.getVendorGeneralInformation().getGstNo().equals(rs.getString(12)) || (rs.getString(12) == null && rs.getInt(11) == 3)) {
					VendorOriginAddressVo vendorOriginAddressVo =  convertQueryResultToVendorOriginalAddress(rs);
					vendorVo.setVendorOriginAddress(vendorOriginAddressVo);
					vendorOriginAddressVosForOtherGst.add(vendorOriginAddressVo);
				}else {
					vendorOriginAddressVosForOtherGst.add(convertQueryResultToVendorOriginalAddress(rs));
				}
			}
		} catch (Exception e) {
			logger.error("Error in Vendor Orgin address");
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, null);
		}
		return vendorOriginAddressVosForOtherGst;
	}

	/**
	 * Convert query result to vendor original address.
	 *
	 * @param rs the rs
	 * @return the vendor origin address vo
	 * @throws SQLException the SQL exception
	 */
	private VendorOriginAddressVo convertQueryResultToVendorOriginalAddress(ResultSet rs) throws SQLException {
		VendorOriginAddressVo vendorOriginAddressVo = new VendorOriginAddressVo();
		vendorOriginAddressVo.setId(rs.getInt(1));
		vendorOriginAddressVo.setAttention(rs.getString(2));
		vendorOriginAddressVo
				.setCountry(Integer.parseInt(rs.getString(3) != null ? rs.getString(3) : "0"));
		vendorOriginAddressVo.setAddress_1(rs.getString(4));
		vendorOriginAddressVo.setAddress_2(rs.getString(5));
		vendorOriginAddressVo
				.setState(Integer.parseInt(rs.getString(6) != null ? rs.getString(6) : "0"));
		vendorOriginAddressVo.setCity(rs.getString(7));
		vendorOriginAddressVo.setZipCode(rs.getString(8));
		vendorOriginAddressVo.setPhoneNo(rs.getString(9));
		vendorOriginAddressVo.setLandMark(rs.getString(10));
		vendorOriginAddressVo.setSelectedAddress(rs.getInt(11));
		vendorOriginAddressVo.setGstNo(rs.getString(12));
		vendorOriginAddressVo.setAddressType(rs.getInt(13));
		return vendorOriginAddressVo;
	}

	private List<VendorDestinationAddressVo> getVendorsDestinationAddressByVendorId(Connection con, int vendorId,
			VendorVo vendorVo) throws ApplicationException {
		logger.info("Entry into method: getVendorDestinationAddressByVendorId");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		List<VendorDestinationAddressVo> vendorDestinationAddressVosForOtherGst = new ArrayList<>();

		try {
			String query = VendorConstants.GET_VENDOR_DESTINATION_ADDRESS_BY_VENDOR_ID;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, vendorId);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				if (rs.getString(12) != null && vendorVo.getVendorGeneralInformation().getGstNo().equals(rs.getString(12)) || (rs.getString(12) == null && rs.getInt(11) == 3)) {
					VendorDestinationAddressVo vendorDestinationAddressVo =  convertQueryResultToVendorDestinationAddress(rs);
					vendorVo.setVendorDestinationAddress(vendorDestinationAddressVo);
					vendorDestinationAddressVosForOtherGst.add(vendorDestinationAddressVo);
				} else {
					vendorDestinationAddressVosForOtherGst.add(convertQueryResultToVendorDestinationAddress(rs));
				}

			}
		} catch (Exception e) {

			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, null);
		}
		return vendorDestinationAddressVosForOtherGst;
	}

	/**
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	private VendorDestinationAddressVo convertQueryResultToVendorDestinationAddress(ResultSet rs) throws SQLException {
		VendorDestinationAddressVo vendorDestinationAddress = new VendorDestinationAddressVo();
		vendorDestinationAddress.setId(rs.getInt(1));
		vendorDestinationAddress.setAttention(rs.getString(2));
		vendorDestinationAddress.setCountry(Integer.parseInt(rs.getString(3) != null ? rs.getString(3) : "0"));
		vendorDestinationAddress.setAddress_1(rs.getString(4));
		vendorDestinationAddress.setAddress_2(rs.getString(5));
		vendorDestinationAddress.setState(Integer.parseInt(rs.getString(6) != null ? rs.getString(6) : "0"));
		vendorDestinationAddress.setCity(rs.getString(7));
		vendorDestinationAddress.setZipCode(rs.getString(8));
		vendorDestinationAddress.setPhoneNo(rs.getString(9));
		vendorDestinationAddress.setLandMark(rs.getString(10));
		vendorDestinationAddress.setSelectedAddress(rs.getInt(11));
		vendorDestinationAddress.setGstNo(rs.getString(12));
		vendorDestinationAddress.setAddressType(rs.getInt(13));
		return vendorDestinationAddress;
	}

	public List<VendorBankDetailsVo> getVendorsBankDetailsByVendorId(Connection con, int vendorId)
			throws ApplicationException {
		logger.info("Entry into method: getVendorBankDetailsByVendorId");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		List<VendorBankDetailsVo> vendorBankDetailsList = new ArrayList<>();
		try {
			String query = VendorConstants.GET_VENDOR_BANK_DETAILS_BY_VENDOR_ID;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, vendorId);
			preparedStatement.setString(2, CommonConstants.STATUS_AS_DELETE);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				VendorBankDetailsVo vendorBankDetails = new VendorBankDetailsVo();
				vendorBankDetails.setId(rs.getInt(1));
				vendorBankDetails.setBankName(rs.getString(2));
				vendorBankDetails.setAccountNumber(rs.getString(3));
				vendorBankDetails.setConfirmAccountNumber(rs.getString(3));
				vendorBankDetails.setAccountHolderName(rs.getString(4));
				vendorBankDetails.setBranchName(rs.getString(5));
				vendorBankDetails.setIfscCode(rs.getString(6));
				vendorBankDetails.setUpiId(rs.getString(7));
				vendorBankDetails.setIsDefault(rs.getBoolean(8));
				vendorBankDetails.setStatus(rs.getString(9));
				vendorBankDetailsList.add(vendorBankDetails);
			}
		} catch (Exception e) {
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, null);
		}
		return vendorBankDetailsList;
	}

	public VendorBankDetailsVo getVendorsDefaultBankDetailsByVendorId(int vendorId) throws ApplicationException {
		logger.info("Entry into method: getVendorBankDetailsByVendorId");
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		VendorBankDetailsVo vendorBankDetails = null;
		try {
			con = getAccountsPayable();
			preparedStatement = con.prepareStatement(VendorConstants.GET_VENDOR_DEFAULT_BANK_DETAILS_BY_VENDOR_ID);
			preparedStatement.setInt(1, vendorId);
			preparedStatement.setString(2, CommonConstants.STATUS_AS_DELETE);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				vendorBankDetails = new VendorBankDetailsVo();
				vendorBankDetails.setId(rs.getInt(1));
				vendorBankDetails.setBankName(rs.getString(2));
				vendorBankDetails.setAccountNumber(rs.getString(3));
				vendorBankDetails.setConfirmAccountNumber(rs.getString(3));
				vendorBankDetails.setAccountHolderName(rs.getString(4));
				vendorBankDetails.setBranchName(rs.getString(5));
				vendorBankDetails.setIfscCode(rs.getString(6));
				vendorBankDetails.setUpiId(rs.getString(7));
				vendorBankDetails.setIsDefault(rs.getBoolean(8));
				vendorBankDetails.setStatus(rs.getString(9));
			}
		} catch (Exception e) {
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return vendorBankDetails;
	}

	private List<VendorContactVo> getVendorsContactDetailsByVendorId(Connection con, int vendorId, VendorVo vendorVo)
			throws ApplicationException {
		logger.info("Entry into method: getVendorContactDetailsByVendorId");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		List<VendorContactVo> vendorContactList = new ArrayList<VendorContactVo>();
		try {
			String queryToGetVendorContact = VendorConstants.GET_VENDOR_CONTACT_DETAILS_BY_VENDOR_ID;
			preparedStatement = con.prepareStatement(queryToGetVendorContact);
			preparedStatement.setInt(1, vendorId);
			preparedStatement.setString(2, CommonConstants.STATUS_AS_DELETE);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				VendorContactVo vendorContact = new VendorContactVo();
				vendorContact.setId(rs.getInt(1));
				vendorContact.setSalutation(rs.getString(2));
				vendorContact.setFirstName(rs.getString(3));
				vendorContact.setLastName(rs.getString(4));
				vendorContact.setWorkNo(rs.getString(5));
				vendorContact.setMobileNo(rs.getString(6));
				vendorContact.setEmailAddress(rs.getString(7));
				vendorContact.setStatus(rs.getString(8));
				vendorContact.setIsDefault(rs.getBoolean(9));
				vendorContactList.add(vendorContact);
				vendorVo.setContacts(vendorContactList);
			}
		} catch (Exception e) {
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, null);
		}
		return vendorContactList;
	}

	private List<BaseAddressVo> getVendorAdditionalAddressDetailsByVendorId(Connection con, int vendorId,
			VendorVo vendorVo) throws ApplicationException {
		logger.info("Entry into method: getVendorAdditionalAddressDetailsByVendorId");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		List<BaseAddressVo> vendorAdditionalAddressesList = new ArrayList<>();
		Set<BaseAddressVo> vendorAdditionalAddressesOfOtherGst = new HashSet<>();
		try {
			String queryToGetVendorContact = VendorConstants.GET_VENDOR_ADDITIONAL_ADDRESS_BY_VENDOR_ID;
			preparedStatement = con.prepareStatement(queryToGetVendorContact);
			preparedStatement.setInt(1, vendorId);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				if (rs.getString(12) == null
						|| vendorVo.getVendorGeneralInformation().getGstNo().equals(rs.getString(12))) {
					BaseAddressVo vendorBaseAddressVo = convertQueryResultToBaseAddress(rs);
					vendorAdditionalAddressesList.add(vendorBaseAddressVo);
					vendorVo.setVendorAdditionalAddresses(vendorAdditionalAddressesList);

				} else {
					vendorAdditionalAddressesOfOtherGst.add(convertQueryResultToBaseAddress(rs));
				}
			}
			vendorAdditionalAddressesOfOtherGst.addAll(vendorAdditionalAddressesList);
		} catch (Exception e) {
			logger.error("Error in getVendorAdditionalAddressDetailsByVendorId:", e);
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, null);
		}
		return new ArrayList<>(vendorAdditionalAddressesOfOtherGst);
	}

	/**
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	private BaseAddressVo convertQueryResultToBaseAddress(ResultSet rs) throws SQLException {
		BaseAddressVo vendorBaseAddressVo = new BaseAddressVo();
		vendorBaseAddressVo.setId(rs.getInt(1));
		vendorBaseAddressVo.setAttention(rs.getString(2));
		vendorBaseAddressVo.setCountry(rs.getInt(3));
		vendorBaseAddressVo.setAddress_1(rs.getString(4));
		vendorBaseAddressVo.setAddress_2(rs.getString(5));
		vendorBaseAddressVo.setState(rs.getString(6) != null ? Integer.parseInt(rs.getString(6)) : 0);
		vendorBaseAddressVo.setCity(rs.getString(7));
		vendorBaseAddressVo.setZipCode(rs.getString(8));
		vendorBaseAddressVo.setPhoneNo(rs.getString(9));
		vendorBaseAddressVo.setLandMark(rs.getString(10));
		vendorBaseAddressVo.setSelectedAddress(rs.getInt(11) == 0 ? null : rs.getInt(11));
		vendorBaseAddressVo.setGstNo(rs.getString(12));
		vendorBaseAddressVo.setAddressType(rs.getInt(13));
		return vendorBaseAddressVo;
	}

	public boolean updateVendor(VendorVo vendorVo) throws ApplicationException {
		logger.info("Entry into method: updateVendor");
		PreparedStatement preparedStatement = null;
		boolean isTxnSuccess = false;
		ResultSet rs = null;
		Connection con = null;
		if (vendorVo != null && vendorVo.getVendorGeneralInformation().getId() != null) {
			
			if (vendorVo.getVendorGeneralInformation().getPan() != null) {

				int availableVendorsForGivenEmailAndOrg = checkVendorExistForUpdateVendor(
						vendorVo.getVendorGeneralInformation().getPan(), vendorVo.getOrganizationId(),
						vendorVo.getVendorGeneralInformation().getId());
				if (availableVendorsForGivenEmailAndOrg > 0) {
					throw new ApplicationException("Vendor Exist for the Organization");
				}
			}
			try {
				con = getAccountsPayable();
				con.setAutoCommit(false);
				if (vendorVo.getContactsToRemove() != null && vendorVo.getContactsToRemove().size() > 0) {
					for (Integer id : vendorVo.getContactsToRemove()) {
						deleteVendorContacts(id, con);
					}
				}
				if (vendorVo.getBankDetailsToRemove() != null && vendorVo.getBankDetailsToRemove().size() > 0) {
					for (Integer id : vendorVo.getBankDetailsToRemove()) {
						deleteVendorBankDetails(id, con);
					}
				}
				if (vendorVo.getIsVendorOnBoarding() != null && vendorVo.getIsVendorOnBoarding()) {
					if (vendorVo.getVendorGeneralInformation() != null)
						updateVendorOnbordingGeneralnfo(vendorVo, con);
				}

				else {
					if (vendorVo.getVendorGeneralInformation() != null) {
						updateVendorGeneralInfo(vendorVo, con);
					}
					if (vendorVo.getVendorFinance() != null && vendorVo.getVendorFinance().getId() != null) {
						updateVendorFinance(vendorVo.getVendorFinance(), con);
					}
				}
				

				if (!CollectionUtils.isEmpty(vendorVo.getOriginalAndDestinationAddressBasedOnGst())) {
					for (VendorBasedOnGstVo vendorBasedOnGstVo : vendorVo
							.getOriginalAndDestinationAddressBasedOnGst()) {
						processVendorOriginAddressForUpdateVendor(vendorVo, con, vendorBasedOnGstVo);

						processVendorDestinationAddressForUpdateVendor(vendorVo, con, vendorBasedOnGstVo);

						processVendorAdditionalForUpdateVendor(vendorVo, con, vendorBasedOnGstVo);
					}

				} else {
					deleteVendorAddress(vendorVo.getVendorGeneralInformation().getId(), true, con, null);
					deleteVendorAddress(vendorVo.getVendorGeneralInformation().getId(), false, con, null);
					deleteVendorAdditionalAddress(vendorVo.getVendorGeneralInformation().getId(), con);
				}
				
				
				if (!CollectionUtils.isEmpty(vendorVo.getBankDetails())) {
					updateVendorBankDetails(vendorVo.getBankDetails(), vendorVo.getVendorGeneralInformation().getId(),
							con);
				}
				if (vendorVo.getContacts() != null && vendorVo.getContacts().size() > 0) {
					updateVendorContact(vendorVo.getContacts(), vendorVo.getVendorGeneralInformation().getId(), con);
				}
				
				if (vendorVo.getAttachments() != null && vendorVo.getAttachments().size() > 0) {
					attachmentsDao.createAttachments(vendorVo.getOrganizationId(), vendorVo.getUserId(),
							vendorVo.getAttachments(), AttachmentsConstants.MODULE_TYPE_VENDOR,
							vendorVo.getVendorGeneralInformation().getId(), vendorVo.getRoleName());
				}
				if (vendorVo.getAttachmentsToRemove() != null && vendorVo.getAttachmentsToRemove().size() > 0) {
					for (Integer attachmentId : vendorVo.getAttachmentsToRemove()) {
						attachmentsDao.changeStatusForSingleAttachment(attachmentId, CommonConstants.STATUS_AS_DELETE,
								vendorVo.getUserId(), vendorVo.getRoleName());
					}
				}

				if (vendorVo.getAttachmentsToRemove() != null && vendorVo.getAttachmentsToRemove().size() > 0) {
					for (Integer attachmentId : vendorVo.getAttachmentsToRemove()) {
						attachmentsDao.changeStatusForSingleAttachment(attachmentId, CommonConstants.STATUS_AS_DELETE,
								vendorVo.getUserId(), vendorVo.getRoleName());
					}
				}
				con.commit();
				isTxnSuccess = true;
			} catch (Exception e) {
				logger.info("Error in updateVendor:: ", e);
				try {
					con.rollback();
				} catch (SQLException e1) {
					throw new ApplicationException(e1.getMessage());
				}
				throw new ApplicationException(e.getMessage());
			} finally {
				closeResources(rs, preparedStatement, con);
			}
		}

		return isTxnSuccess;
	}

	/**
	 * @param vendorVo
	 * @param con
	 * @param vendorBasedOnGstVo
	 * @throws ApplicationException
	 */
	private void processVendorAdditionalForUpdateVendor(VendorVo vendorVo, Connection con,
			VendorBasedOnGstVo vendorBasedOnGstVo) throws ApplicationException {
		List<BaseAddressVo> updateBaseAddressVos = new ArrayList<>();
		List<BaseAddressVo> createBaseAddressVos = new ArrayList<>();
		
		for (BaseAddressVo baseAddressVo : vendorBasedOnGstVo.getVendorAdditionalAddresses()) {
			if (baseAddressVo.getId() != null) {
				updateBaseAddressVos.add(baseAddressVo);
			} else {
				createBaseAddressVos.add(baseAddressVo);
			}

		}
		updateVendorAdditionalAddress(updateBaseAddressVos, con);
		createVendorAdditionalAddresses(vendorVo.getVendorGeneralInformation().getId(),
				createBaseAddressVos, con);
	}

	/**
	 * @param vendorVo
	 * @param con
	 * @param vendorBasedOnGstVo
	 * @throws ApplicationException
	 */
	private void processVendorDestinationAddressForUpdateVendor(VendorVo vendorVo, Connection con,
			VendorBasedOnGstVo vendorBasedOnGstVo) throws ApplicationException {
		if (vendorBasedOnGstVo.getVendorDestinationAddress().getId() == null) {
			deleteVendorAddress(vendorVo.getVendorGeneralInformation().getId(), false, con,vendorBasedOnGstVo.getVendorOriginAddress().getGstNo());
			createVendorDestinationAddress(vendorVo.getVendorGeneralInformation().getId(),
					vendorBasedOnGstVo.getVendorDestinationAddress(), con);
		} else {
			updateVendorDestinationAddress(vendorBasedOnGstVo.getVendorDestinationAddress(), con, vendorVo.getVendorGeneralInformation().getId());
		}
	}

	/**
	 * Process vendor origin address for update vendor.
	 *
	 * @param vendorVo the vendor vo
	 * @param con the con
	 * @param vendorBasedOnGstVo the vendor based on gst vo
	 * @throws ApplicationException the application exception
	 */
	private void processVendorOriginAddressForUpdateVendor(VendorVo vendorVo, Connection con,
			VendorBasedOnGstVo vendorBasedOnGstVo) throws ApplicationException {
		if (vendorBasedOnGstVo.getVendorOriginAddress().getId() == null) {
			deleteVendorAddress(vendorVo.getVendorGeneralInformation().getId(), true, con, vendorBasedOnGstVo.getVendorOriginAddress().getGstNo());
			createVendorOriginAddress(vendorVo.getVendorGeneralInformation().getId(),
					vendorBasedOnGstVo.getVendorOriginAddress(), con);
		} else {
			updateVendorOriginAddress(vendorBasedOnGstVo.getVendorOriginAddress(), con, vendorVo.getVendorGeneralInformation().getId());
		}
	}


	private VendorVo updateVendorOnbordingGeneralnfo(VendorVo vendorVo, Connection con) throws ApplicationException {
		logger.info("Entry into method: updateVendorOnbordingGeneralnfo");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			String sql = VendorConstants.UPDATE_VENDOR_ONBOARDING_GENERAL_INFORMATION;
			preparedStatement = con.prepareStatement(sql);
			preparedStatement.setString(1,
					vendorVo.getVendorGeneralInformation().getPrimaryContact() != null
							? vendorVo.getVendorGeneralInformation().getPrimaryContact()
							: null);
			preparedStatement.setString(2,
					vendorVo.getVendorGeneralInformation().getCompanyName() != null
							? vendorVo.getVendorGeneralInformation().getCompanyName()
							: null);
			preparedStatement.setString(3,
					vendorVo.getVendorGeneralInformation().getEmail() != null
							? vendorVo.getVendorGeneralInformation().getEmail()
							: null);
			preparedStatement.setString(4,
					vendorVo.getVendorGeneralInformation().getMobileNo() != null
							? vendorVo.getVendorGeneralInformation().getMobileNo()
							: null);
			preparedStatement.setString(5,
					vendorVo.getVendorGeneralInformation().getPan() != null
							? vendorVo.getVendorGeneralInformation().getPan()
							: null);
			preparedStatement.setObject(6,
					vendorVo.getVendorGeneralInformation().getVendorOrganizationId() != null
							? vendorVo.getVendorGeneralInformation().getVendorOrganizationId()
							: null);
			preparedStatement.setObject(7,
					vendorVo.getVendorGeneralInformation().getVendorGstTypeId() != null
							? vendorVo.getVendorGeneralInformation().getVendorGstTypeId()
							: null);
			preparedStatement.setString(8,
					vendorVo.getVendorGeneralInformation().getGstNo() != null
							? vendorVo.getVendorGeneralInformation().getGstNo()
							: null);
			preparedStatement.setObject(9,
					vendorVo.getVendorGeneralInformation().getIsMsmeRegistered() != null
							? vendorVo.getVendorGeneralInformation().getIsMsmeRegistered()
							: null);
			preparedStatement.setObject(10,
					vendorVo.getVendorGeneralInformation().getMsmeNumber() != null
							? vendorVo.getVendorGeneralInformation().getMsmeNumber()
							: null);
			preparedStatement.setObject(11, vendorVo.getIsSuperAdmin() != null ? vendorVo.getIsSuperAdmin() : null);
			preparedStatement.setObject(12,
					vendorVo.getVendorOriginAddress().getSameBillingDestAddress() != null
							? vendorVo.getVendorOriginAddress().getSameBillingDestAddress()
							: null);
			preparedStatement.setObject(13, vendorVo.getOrganizationId() != null ? vendorVo.getOrganizationId() : null);
			preparedStatement.setTimestamp(14, new Timestamp(System.currentTimeMillis()));
			preparedStatement.setString(15, vendorVo.getUserId());
			preparedStatement.setString(16, vendorVo.getRoleName());
			preparedStatement.setInt(17, vendorVo.getVendorGeneralInformation().getId());
			preparedStatement.executeUpdate();
		} catch (Exception e) {
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, null);
		}
		return vendorVo;
	}

	private VendorVo updateVendorGeneralInfo(VendorVo vendorVo, Connection con) throws ApplicationException {
		logger.info("Entry into method: updateVendorGeneralInfo");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			String sql = VendorConstants.UPDATE_VENDOR_GENERAL_INFORMATION;
			preparedStatement = con.prepareStatement(sql);
			preparedStatement.setString(1,
					vendorVo.getVendorGeneralInformation().getPrimaryContact() != null
							? vendorVo.getVendorGeneralInformation().getPrimaryContact()
							: null);
			preparedStatement.setString(2,
					vendorVo.getVendorGeneralInformation().getCompanyName() != null
							? vendorVo.getVendorGeneralInformation().getCompanyName()
							: null);
			preparedStatement.setString(3,
					vendorVo.getVendorGeneralInformation().getVendorDisplayName() != null
							? vendorVo.getVendorGeneralInformation().getVendorDisplayName()
							: null);
			preparedStatement.setString(4,
					vendorVo.getVendorGeneralInformation().getEmail() != null
							? vendorVo.getVendorGeneralInformation().getEmail()
							: null);
			preparedStatement.setString(5,
					vendorVo.getVendorGeneralInformation().getPhoneNo() != null
							? vendorVo.getVendorGeneralInformation().getPhoneNo()
							: null);
			preparedStatement.setString(6,
					vendorVo.getVendorGeneralInformation().getWebsite() != null
							? vendorVo.getVendorGeneralInformation().getWebsite()
							: null);
			preparedStatement.setString(7,
					vendorVo.getVendorGeneralInformation().getMobileNo() != null
							? vendorVo.getVendorGeneralInformation().getMobileNo()
							: null);
			preparedStatement.setString(8,
					vendorVo.getVendorGeneralInformation().getPan() != null
							? vendorVo.getVendorGeneralInformation().getPan()
							: null);
			preparedStatement.setObject(9,
					vendorVo.getVendorGeneralInformation().getVendorGroupId() != null
							? vendorVo.getVendorGeneralInformation().getVendorGroupId()
							: null);
			preparedStatement.setObject(10,
					vendorVo.getVendorGeneralInformation().getVendorOrganizationId() != null
							? vendorVo.getVendorGeneralInformation().getVendorOrganizationId()
							: null);
			preparedStatement.setObject(11,
					vendorVo.getVendorGeneralInformation().getVendorGstTypeId() != null
							? vendorVo.getVendorGeneralInformation().getVendorGstTypeId()
							: null);
			preparedStatement.setString(12,
					vendorVo.getVendorGeneralInformation().getGstNo() != null
							? vendorVo.getVendorGeneralInformation().getGstNo()
							: null);
			preparedStatement.setObject(13, vendorVo.getOrganizationId() != null ? vendorVo.getOrganizationId() : null);
			preparedStatement.setObject(14, vendorVo.getIsSuperAdmin() != null ? vendorVo.getIsSuperAdmin() : null);
			if (vendorVo.getVendorOriginAddress() != null) {
				preparedStatement.setObject(15,
						vendorVo.getVendorOriginAddress().getSameBillingDestAddress());
			}else {
				preparedStatement.setObject(15,null);
			}
			preparedStatement.setTimestamp(16, new Timestamp(System.currentTimeMillis()));
			preparedStatement.setString(17, vendorVo.getUserId());
			preparedStatement.setString(18, vendorVo.getRoleName());
			preparedStatement.setInt(19, vendorVo.getDefaultGlTallyId() != null ? vendorVo.getDefaultGlTallyId() : 0);
			preparedStatement.setString(20,
					vendorVo.getDefaultGlTallyName() != null ? vendorVo.getDefaultGlTallyName() : null);
			BookKeepingSettingsVo bookKeepingSetting = vendorVo.getVendorBookKeepingSetting();
			preparedStatement.setInt(21,
					bookKeepingSetting != null && bookKeepingSetting.getId() > 0 ? bookKeepingSetting.getId() : 0);
			preparedStatement.setString(22,
					bookKeepingSetting != null && bookKeepingSetting.getDefaultGlName() != null
							? bookKeepingSetting.getDefaultGlName()
							: null);
			preparedStatement.setInt(23,
					bookKeepingSetting != null && bookKeepingSetting.getLocationId() > 0
							? bookKeepingSetting.getLocationId()
							: 0);
			preparedStatement.setString(24,
					bookKeepingSetting != null && bookKeepingSetting.getGstNumber() != null
							? bookKeepingSetting.getGstNumber()
							: null);
			ObjectMapper mapper = new ObjectMapper();
			String newJsonData = mapper.writeValueAsString(vendorVo.getVendorGeneralInformation().getOtherGsts() != null
					? vendorVo.getVendorGeneralInformation().getOtherGsts()
					: "");
			preparedStatement.setString(25, newJsonData);
			preparedStatement.setString(26, vendorVo.getStatus());
	
			preparedStatement.setBoolean(27,
					vendorVo.getVendorGeneralInformation().getIsPanOrGstAvailable() != null
							? vendorVo.getVendorGeneralInformation().getIsPanOrGstAvailable()
							: Boolean.TRUE);
			preparedStatement.setBoolean(28,
					vendorVo.getVendorGeneralInformation().getOverSeasVendor() != null
							? vendorVo.getVendorGeneralInformation().getOverSeasVendor()
							: Boolean.FALSE);
			preparedStatement.setBoolean(29,
					vendorVo.getVendorGeneralInformation().getVendorWithoutPan() != null
							? !vendorVo.getVendorGeneralInformation().getVendorWithoutPan()
							: Boolean.FALSE);
			
			preparedStatement.setInt(30, vendorVo.getVendorGeneralInformation().getId());
			preparedStatement.executeUpdate();
		} catch (Exception e) {
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, null);
		}
		return vendorVo;
	}

	private VendorFinanceVo updateVendorFinance(VendorFinanceVo vendorFinanceVo, Connection con)
			throws ApplicationException {
		logger.info("Entry into method: updatevandorFinanceDetails");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			String sql = VendorConstants.UPDATE_VENDOR_FINANCE;
			preparedStatement = con.prepareStatement(sql);
			preparedStatement.setObject(1,
					vendorFinanceVo.getCurrencyId());
			preparedStatement.setObject(2,
					vendorFinanceVo.getPaymentTermsid());
			preparedStatement.setObject(3,
					vendorFinanceVo.getSourceOfSupplyId() != null ? vendorFinanceVo.getSourceOfSupplyId() : 1);
			preparedStatement.setObject(4, vendorFinanceVo.getTdsId() != null);
			preparedStatement.setString(5,
					vendorFinanceVo.getOpeningBalance());
			preparedStatement.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
			preparedStatement.setInt(7, vendorFinanceVo.getId());
			preparedStatement.executeUpdate();
		} catch (Exception e) {
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, null);
		}
		return vendorFinanceVo;
	}

	public void updateVendorOriginAddress(VendorOriginAddressVo vendorOriginAddressVo, Connection con, Integer vendorId) throws ApplicationException {
		logger.info("Entry into method: updateVendorOriginAddressDetails");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;

		if (vendorOriginAddressVo != null) {
			try {
				preparedStatement = con.prepareStatement(VendorConstants.UPDATE_VENDOR_ORIGIN_ADDRESS_BY_ID_AND_GST_NO);
				preparedStatement.setString(1, vendorOriginAddressVo.getAttention());
				preparedStatement.setString(2, vendorOriginAddressVo.getCountry().toString());
				preparedStatement.setString(3, vendorOriginAddressVo.getAddress_1());
				preparedStatement.setString(4, vendorOriginAddressVo.getAddress_2());
				preparedStatement.setInt(5, vendorOriginAddressVo.getState());
				preparedStatement.setString(6, vendorOriginAddressVo.getCity());
				preparedStatement.setString(7, vendorOriginAddressVo.getZipCode());
				preparedStatement.setString(8, vendorOriginAddressVo.getPhoneNo());
				preparedStatement.setString(9, vendorOriginAddressVo.getLandMark());
				preparedStatement.setTimestamp(10, new Timestamp(new Date().getTime()));
				preparedStatement.setObject(11, vendorOriginAddressVo.getSelectedAddress());
				preparedStatement.setString(12, vendorOriginAddressVo.getGstNo());
				preparedStatement.setObject(13, vendorOriginAddressVo.getAddressType());				
				preparedStatement.setInt(14,vendorId);
				preparedStatement.setString(15, vendorOriginAddressVo.getGstNo());
				preparedStatement.executeUpdate();
			} catch (Exception e) {
				logger.error("Error in updateVendorOriginAddressDetails", e);
				throw new ApplicationException(e.getMessage());
			} finally {
				closeResources(rs, preparedStatement, null);
			}
		}else {
			
		}
	}

	public void updateVendorDestinationAddress(VendorDestinationAddressVo vendorDestinationAddressVo, Connection con, Integer vendorId) throws ApplicationException {
		logger.info("Entry into method: updateVendorDestinationAddressDetails");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;

		if (vendorDestinationAddressVo != null) {
			try {
				preparedStatement = con.prepareStatement(VendorConstants.UPDATE_VENDOR_DEST_ADDRESS_BY_ID_AND_GST_NO);
				preparedStatement.setString(1, vendorDestinationAddressVo.getAttention());
				preparedStatement.setString(2, vendorDestinationAddressVo.getCountry().toString());
				preparedStatement.setString(3, vendorDestinationAddressVo.getAddress_1());
				preparedStatement.setString(4, vendorDestinationAddressVo.getAddress_2());
				preparedStatement.setInt(5, vendorDestinationAddressVo.getState());
				preparedStatement.setString(6, vendorDestinationAddressVo.getCity());
				preparedStatement.setString(7, vendorDestinationAddressVo.getZipCode());
				preparedStatement.setString(8, vendorDestinationAddressVo.getPhoneNo());
				preparedStatement.setString(9, vendorDestinationAddressVo.getLandMark());
				preparedStatement.setTimestamp(10, new Timestamp(new Date().getTime()));
				preparedStatement.setObject(11, vendorDestinationAddressVo.getSelectedAddress());
				preparedStatement.setString(12, vendorDestinationAddressVo.getGstNo());
				preparedStatement.setObject(13, vendorDestinationAddressVo.getAddressType());				
				preparedStatement.setInt(14,vendorId);
				preparedStatement.setString(15, vendorDestinationAddressVo.getGstNo());
				preparedStatement.executeUpdate();
			} catch (Exception e) {
				logger.error("Error in updateVendorDestinationAddress", e);
				throw new ApplicationException(e.getMessage());
			} finally {
				closeResources(rs, preparedStatement, null);
			}
		}
	}
	
	
	private void updateVendorAdditionalAddress(List<BaseAddressVo> baseAddressVos, Connection con) throws ApplicationException {
		logger.info("Entry into method: updateVendorDestinationAddressDetails");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;

		if (baseAddressVos != null && !baseAddressVos.isEmpty()) {
			try {
				preparedStatement = con.prepareStatement(VendorConstants.UPDATE_VENDOR_ADDITIONAL_ADDRESS_BY_ID);
				for (BaseAddressVo baseAddressVo : baseAddressVos) {
					preparedStatement.setString(1, baseAddressVo.getAttention());
					preparedStatement.setString(2, baseAddressVo.getCountry().toString());
					preparedStatement.setString(3, baseAddressVo.getAddress_1());
					preparedStatement.setString(4, baseAddressVo.getAddress_2());
					preparedStatement.setInt(5, baseAddressVo.getState());
					preparedStatement.setString(6, baseAddressVo.getCity());
					preparedStatement.setString(7, baseAddressVo.getZipCode());
					preparedStatement.setString(8, baseAddressVo.getPhoneNo());
					preparedStatement.setString(9, baseAddressVo.getLandMark());
					preparedStatement.setTimestamp(10, new Timestamp(new Date().getTime()));
					preparedStatement.setObject(11, baseAddressVo.getSelectedAddress());
					preparedStatement.setString(12, baseAddressVo.getGstNo());
					preparedStatement.setObject(13, baseAddressVo.getAddressType());
					preparedStatement.setInt(14, baseAddressVo.getId());
					preparedStatement.addBatch();
				}
				preparedStatement.executeBatch();
			} catch (Exception e) {
				logger.error("Error in updateVendorDestinationAddress", e);
				throw new ApplicationException(e.getMessage());
			} finally {
				closeResources(rs, preparedStatement, null);
			}
		}
	}

	private List<VendorBankDetailsVo> updateVendorBankDetails(List<VendorBankDetailsVo> listVendorBankDetails,
			Integer vendorId, Connection con) throws ApplicationException {
		logger.info("Entry into method: updateVendorBankDetails");
		for (int i = 0; i < listVendorBankDetails.size(); i++) {
			VendorBankDetailsVo vendorBankDetailsVo = listVendorBankDetails.get(i);
			String status = listVendorBankDetails.get(i).getStatus();
			if (status.equalsIgnoreCase(CommonConstants.STATUS_AS_DELETE)
					|| status.equalsIgnoreCase(CommonConstants.STATUS_AS_ACTIVE)) {
				updateBankDetails(vendorBankDetailsVo, status, con);
			} else if (status.equalsIgnoreCase(CommonConstants.STATUS_AS_NEW)) {
				createBankDetails(vendorBankDetailsVo, vendorId, con);
			}
		}
		return listVendorBankDetails;
	}

	public void createBankDetails(VendorBankDetailsVo vendorBankDetailsVo, Integer vendorId, Connection con)
			throws ApplicationException {
		logger.info("Entry into method:createBankDetails");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		if (vendorBankDetailsVo != null) {
			try {
				boolean isBankDetailsExist = checkBankDetailsExist(vendorId, vendorBankDetailsVo.getAccountNumber(),
						vendorBankDetailsVo.getIfscCode(), con);
				if (isBankDetailsExist) {
					throw new ApplicationException("Bank Details Exist for the Organization");
				}
				String InsertQuery = VendorConstants.INSERT_INTO_VENDOR_BANK_DETAILS;
				preparedStatement = con.prepareStatement(InsertQuery, Statement.RETURN_GENERATED_KEYS);
				preparedStatement.setString(1,
						vendorBankDetailsVo.getBankName());
				preparedStatement.setString(2,
						vendorBankDetailsVo.getAccountNumber());
				preparedStatement.setString(3,
						vendorBankDetailsVo.getAccountHolderName());
				preparedStatement.setString(4,
						vendorBankDetailsVo.getBranchName());
				preparedStatement.setString(5,
						vendorBankDetailsVo.getIfscCode());
				preparedStatement.setString(6,
						vendorBankDetailsVo.getUpiId());
				preparedStatement.setBoolean(7,
						vendorBankDetailsVo.getIsDefault());
				preparedStatement.setInt(8, vendorId);
				int rowAffected = preparedStatement.executeUpdate();
				if (rowAffected == 1) {
					rs = preparedStatement.getGeneratedKeys();
					if (rs.next()) {
						vendorBankDetailsVo.setId(rs.getInt(1));
					}
				}
			} catch (Exception e) {
				throw new ApplicationException(e.getMessage());
			} finally {
				closeResources(rs, preparedStatement, null);
			}
		}
	}

	private boolean checkBankDetailsExist(Integer vendorId, String accountNumber, String ifscCode, Connection con)
			throws ApplicationException {
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			String query = VendorConstants.CHECK_BANK_DETAILS_EXIST_FOR_VENDOR;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, vendorId);
			preparedStatement.setString(2, accountNumber);
			preparedStatement.setString(3, ifscCode);
			preparedStatement.setString(4, CommonConstants.STATUS_AS_DELETE);
			rs = preparedStatement.executeQuery();
			if (rs.next()) {
				return true;
			}
		} catch (Exception e) {
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, null);
		}
		return false;
	}

	public void updateBankDetails(VendorBankDetailsVo vendorBankDetailsVo, String status, Connection con)
			throws ApplicationException {
		logger.info("Entry into method: updateBankDetails");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		if (vendorBankDetailsVo != null) {
			try {
				String sql = VendorConstants.UPDATE_VENDOR_BANK_DETAILS;
				preparedStatement = con.prepareStatement(sql);
				preparedStatement.setString(1,
						vendorBankDetailsVo.getAccountNumber());
				preparedStatement.setString(2,
						vendorBankDetailsVo.getAccountHolderName());
				preparedStatement.setString(3,
						vendorBankDetailsVo.getBankName());
				preparedStatement.setString(4,
						vendorBankDetailsVo.getBranchName());
				preparedStatement.setString(5,
						vendorBankDetailsVo.getIfscCode());
				preparedStatement.setString(6,
						vendorBankDetailsVo.getUpiId());
				preparedStatement.setBoolean(7,
						vendorBankDetailsVo.getIsDefault());
				if (status.equalsIgnoreCase(CommonConstants.STATUS_AS_DELETE)) {
					preparedStatement.setString(8, CommonConstants.STATUS_AS_DELETE);
				} else {
					preparedStatement.setString(8, CommonConstants.STATUS_AS_ACTIVE);
				}
				preparedStatement.setTimestamp(9, new Timestamp(System.currentTimeMillis()));
				preparedStatement.setInt(10, vendorBankDetailsVo.getId());
				preparedStatement.executeUpdate();
			} catch (Exception e) {
				throw new ApplicationException(e.getMessage());
			} finally {
				closeResources(rs, preparedStatement, null);
			}
		}
	}

	private List<VendorContactVo> updateVendorContact(List<VendorContactVo> listVendorContacts, Integer vendorId,
			Connection con) throws ApplicationException {
		logger.info("Entry into method: updateVendorContactDetails");
		for (int i = 0; i < listVendorContacts.size(); i++) {
			VendorContactVo vendorContactVo = listVendorContacts.get(i);
			String status = listVendorContacts.get(i).getStatus();
			if (status.equalsIgnoreCase(CommonConstants.STATUS_AS_DELETE)
					|| status.equalsIgnoreCase(CommonConstants.STATUS_AS_ACTIVE)) {
				updateContact(vendorContactVo, status, con);
			} else if (status.equalsIgnoreCase(CommonConstants.STATUS_AS_NEW)) {
				createContact(vendorContactVo, vendorId, con);
			}

		}
		return listVendorContacts;
	}

	private void createContact(VendorContactVo vendorContactVo, Integer vendorId, Connection con)
			throws ApplicationException {
		logger.info("Entry into method: createVendorContact");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		if (vendorContactVo != null) {
			try {
				/*
				 * boolean isContactExist = checkContactExist(vendorContactVo.getEmailAddress(),
				 * vendorContactVo.getMobileNo(), con); if (isContactExist) { throw new
				 * ApplicationException("Contact Details Exist for the Organization"); }
				 */
				String InsertQuery = VendorConstants.INSERT_INTO_VENDOR_CONTACT;
				preparedStatement = con.prepareStatement(InsertQuery, Statement.RETURN_GENERATED_KEYS);
				preparedStatement.setString(1,
						vendorContactVo.getSalutation());
				preparedStatement.setString(2,
						vendorContactVo.getFirstName());
				preparedStatement.setString(3,
						vendorContactVo.getLastName());
				preparedStatement.setString(4,
						vendorContactVo.getWorkNo());
				preparedStatement.setString(5,
						vendorContactVo.getMobileNo());
				preparedStatement.setString(6,
						vendorContactVo.getEmailAddress());
				preparedStatement.setInt(7, vendorId);
				preparedStatement.setBoolean(8,
						vendorContactVo.getIsDefault() != null ? vendorContactVo.getIsDefault() : Boolean.FALSE);
				int rowAffected = preparedStatement.executeUpdate();
				if (rowAffected == 1) {
					rs = preparedStatement.getGeneratedKeys();
					if (rs.next()) {
						vendorContactVo.setId(rs.getInt(1));
					}
				}
			} catch (Exception e) {
				throw new ApplicationException(e.getMessage());
			} finally {
				closeResources(rs, preparedStatement, null);
			}
		}
	}

	/*
	 * private boolean checkContactExist(String emailAddress, String mobileNo,
	 * Connection con) throws ApplicationException { PreparedStatement
	 * preparedStatement = null; ResultSet rs = null; try { String query =
	 * VendorConstants.CHECK_CONTACT_DETAILS_EXIST_FOR_VENDOR; preparedStatement =
	 * con.prepareStatement(query); preparedStatement.setString(1, emailAddress);
	 * preparedStatement.setString(2, mobileNo); rs =
	 * preparedStatement.executeQuery(); if (rs.next()) { return true; } } catch
	 * (Exception e) { throw new ApplicationException(e); } finally {
	 * closeResources(rs, preparedStatement, null); } return false; }
	 */

	private void updateContact(VendorContactVo vendorContactVo, String status, Connection con)
			throws ApplicationException {
		logger.info("Entry into method: updateVendorContactDetails");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		if (vendorContactVo != null) {
			try {
				String sql = VendorConstants.UPDATE_VENDOR_CONTACT;
				preparedStatement = con.prepareStatement(sql);
				preparedStatement.setString(1,
						vendorContactVo.getSalutation());
				preparedStatement.setString(2,
						vendorContactVo.getFirstName());
				preparedStatement.setString(3,
						vendorContactVo.getLastName());
				preparedStatement.setString(4,
						vendorContactVo.getWorkNo());
				preparedStatement.setString(5,
						vendorContactVo.getMobileNo());
				preparedStatement.setString(6,
						vendorContactVo.getEmailAddress());
				if (status.equalsIgnoreCase(CommonConstants.STATUS_AS_DELETE)) {
					preparedStatement.setString(7, CommonConstants.STATUS_AS_DELETE);
				} else {
					preparedStatement.setString(7, CommonConstants.STATUS_AS_ACTIVE);
				}
				preparedStatement.setTimestamp(8, new Timestamp(System.currentTimeMillis()));
				preparedStatement.setBoolean(9,
						vendorContactVo.getIsDefault() != null ? vendorContactVo.getIsDefault() : Boolean.FALSE);
				preparedStatement.setInt(10, vendorContactVo.getId());
				preparedStatement.executeUpdate();
			} catch (Exception e) {
				throw new ApplicationException(e.getMessage());
			} finally {
				closeResources(rs, preparedStatement, null);
			}
		}

	}

	// To delete Vendor table entries
	public void deleteVendorEntries(Integer id, String userId, String roleName) throws ApplicationException {
		logger.info("To deleteVendorEntries:: ");
		Connection connection = null;
		if (id != null) {
			try {
				connection = getAccountsPayable();
				// To remove from vendor info table
				changeStatusForVendorTables(id, CommonConstants.STATUS_AS_DELETE, connection,
						VendorConstants.MODIFY_VENDOR_STATUS);
				// To remove from vendor contact table
				changeStatusForVendorTables(id, CommonConstants.STATUS_AS_DELETE, connection,
						VendorConstants.MODIFY_VENDOR_CONTACT_STATUS);
				// To remove from vendor bank details table
				changeStatusForVendorTables(id, CommonConstants.STATUS_AS_DELETE, connection,
						VendorConstants.MODIFY_VENDOR_BANK_INFO_STATUS);
				// To remove from Attachments table
				attachmentsDao.changeStatusForAttachments(id, CommonConstants.STATUS_AS_DELETE,
						AttachmentsConstants.MODULE_TYPE_VENDOR, userId, roleName);
				logger.info("Deleted successfully in all tables ");
			} catch (Exception e) {
				logger.info("Error in deleteCustomerEntries:: ", e);
				throw new ApplicationException(e.getMessage());
			} finally {
				closeResources(null, null, connection);
			}
		}

	}

	private void deleteVendorBankDetails(Integer id, Connection con) throws ApplicationException {
		logger.info("Entry To deleteVendorBankDetails :: ");
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = con.prepareStatement(VendorConstants.DELETE_VENDOR_BANK_DETAILS);
			preparedStatement.setInt(1, id);
			preparedStatement.executeUpdate();
			logger.info("Vendor Bank Details deleted successfully ");
		} catch (Exception e) {
			logger.info("Error in deleteVendorBankDetails ", e);
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(null, preparedStatement, null);
		}
	}

	private void deleteVendorContacts(Integer id, Connection con) throws ApplicationException {
		logger.info("Entry To deleteVendorContacts :: ");
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = con.prepareStatement(VendorConstants.DELETE_VENDOR_CONTACT);
			preparedStatement.setInt(1, id);
			preparedStatement.executeUpdate();
			logger.info("Vendor Contact Details deleted successfully ");
		} catch (Exception e) {
			logger.info("Error in deleteVendorContacts ", e);
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(null, preparedStatement, null);
		}
	}

	private void deleteVendorAddress(Integer vendorId, boolean isOrigAddress, Connection con, String gstNo)
			throws ApplicationException {
		logger.info("Entry To deleteVendorAddress :: for vendor :" + vendorId);
		PreparedStatement preparedStatement = null;
		try {
			String query = "";
			if (isOrigAddress) {
				query = VendorConstants.DELETE_VENDOR_ORIGIN_ADDRESS;
			}else {
				query = VendorConstants.DELETE_VENDOR_DEST_ADDRESS;
			}
			
			
			if(gstNo != null) {
				query = query.concat(" and gst_no=?");
			}
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, vendorId);
			if(gstNo != null) {
				preparedStatement.setString(2, gstNo);
			}
			preparedStatement.executeUpdate();
			logger.info("Vendor deleteVendorAddress Details deleted successfully ");
		} catch (Exception e) {
			logger.info("Error in deleteVendorAddress ", e);
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(null, preparedStatement, null);
		}
	}

	private void deleteVendorAdditionalAddress(Integer id, Connection con) throws ApplicationException {
		logger.info("Entry To deleteVendorContacts :: ");
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = con.prepareStatement(VendorConstants.DELETE_VENDOR_ADDITIONAL_ADDRESS);
			preparedStatement.setInt(1, id);
			preparedStatement.executeUpdate();
			logger.info("Vendor deleteVendorAdditionalAddress Details deleted successfully ");
		} catch (Exception e) {
			logger.info("Error in deleteVendorContacts ", e);
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(null, preparedStatement, null);
		}
	}

	private void changeStatusForVendorTables(Integer id, String status, Connection con, String query)
			throws ApplicationException {
		logger.info("To Change the status with query :: " + query);
		if (query != null) {
			PreparedStatement preparedStatement = null;
			try {
				preparedStatement = con.prepareStatement(query);
				preparedStatement.setString(1, status);
				preparedStatement.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
				preparedStatement.setInt(3, id);
				preparedStatement.executeUpdate();
				logger.info("Status chaneged successfully ");
			} catch (Exception e) {
				logger.info("Error in changeVendortablesStatus ", e);
				throw new ApplicationException(e.getMessage());
			} finally {
				closeResources(null, preparedStatement, null);
			}
		}
	}

	// to get the Basic vendors available in the vendor module
	public List<BasicVendorVo> getBasicVendor(int organizationId, Connection con) throws ApplicationException {
		logger.info("Entry into method:getBasicVendor");
		List<BasicVendorVo> vendorList = new ArrayList<BasicVendorVo>();
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			preparedStatement = con.prepareStatement(VendorConstants.GET_VENDOR_BASIC_PER_ORGANIZATION);
			preparedStatement.setInt(1, organizationId);
			preparedStatement.setString(2, CommonConstants.STATUS_AS_ACTIVE);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				BasicVendorVo data = new BasicVendorVo();
				data.setId(rs.getInt(1));
				data.setName(rs.getString(2));
				data.setValue(rs.getInt(1));
				data.setPaymentTermsId(rs.getInt(3));
				data.setPaymentTermsDaysLimit(rs.getInt(4));
				data.setAddress_1(rs.getString(5));
				data.setAddress_2(rs.getString(6));
				data.setCity(rs.getString(7));
				data.setStateId(rs.getInt(8));
				data.setStateName(rs.getString(9));
				data.setCountryId(rs.getInt(10));
				data.setCountryName(rs.getString(11));
				data.setZipcode(rs.getString(12));
				data.setTdsId(rs.getInt(13));
				data.setGstNumber(rs.getString(14));
				data.setCurrencyId(rs.getInt(15));
				data.setPanNo(rs.getString(16));
				vendorList.add(data);
			}
		} catch (Exception e) {
			logger.info("Error in  method:getBasicVendor", e);
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, null);
		}
		return vendorList;

	}

	// to get the Basic vendors available in the vendor module
	public List<BasicVendorDetailsVo> getBasicVendorWithGSTNo(int organizationId, Connection con)
			throws ApplicationException {
		logger.info("Entry into getBasicVendorWithGSTNo");
		List<BasicVendorDetailsVo> vendorList = new ArrayList<BasicVendorDetailsVo>();
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			preparedStatement = con.prepareStatement(VendorConstants.GET_VENDOR_BASIC_PER_ORGANIZATION);
			preparedStatement.setInt(1, organizationId);
			preparedStatement.setString(2, CommonConstants.STATUS_AS_ACTIVE);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				BasicVendorDetailsVo data = new BasicVendorDetailsVo();
				data.setId(rs.getInt(1));
				data.setName(rs.getString(2));
				data.setValue(rs.getInt(1));
				data.setPaymentTermsId(rs.getInt(3));
				data.setPaymentTermsDaysLimit(rs.getInt(4));
				data.setAddress_1(rs.getString(5));
				data.setAddress_2(rs.getString(6));
				data.setCity(rs.getString(7));
				data.setStateId(rs.getInt(8));
				data.setStateName(rs.getString(9));
				data.setCountryId(rs.getInt(10));
				data.setCountryName(rs.getString(11));
				data.setZipcode(rs.getString(12));
				data.setTdsId(rs.getInt(13));
				data.setGstNumber(rs.getString(14));
				data.setCurrencyId(rs.getInt(15));
				logger.info("getBasicVendorWithGSTNo" + data);
				vendorList.add(data);
			}
			logger.info("Successfully fetched getBasicVendorWithGSTNo" + vendorList.size());
		} catch (Exception e) {
			logger.info("Error in  getBasicVendorWithGSTNo", e);
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, null);
		}
		return vendorList;
	}

	
	
	// to get the Basic vendors available in the vendor module
	public List<BasicVendorDetailsVo> getBasicVendorWithGSTNoAndAddress(int organizationId, Connection con)
			throws ApplicationException {
		logger.info("Entry into getBasicVendorWithGSTNo");
		List<BasicVendorDetailsVo> vendorList = new ArrayList<BasicVendorDetailsVo>();
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			preparedStatement = con.prepareStatement(VendorConstants.GET_VENDOR_BASIC_PER_ORGANIZATION_WITHOUT_ADDRESS);
			preparedStatement.setInt(1, organizationId);
			preparedStatement.setString(2, CommonConstants.STATUS_AS_ACTIVE);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				BasicVendorDetailsVo data = new BasicVendorDetailsVo();
				data.setId(rs.getInt(1));
				data.setName(rs.getString(2));
				data.setValue(rs.getInt(1));
				data.setPaymentTermsId(rs.getInt(3));
				data.setPaymentTermsDaysLimit(rs.getInt(4));
				data.setTdsId(rs.getInt(5));
				data.setGstNumber(rs.getString(6));
				data.setCurrencyId(rs.getInt(7));
				data.setPanNo(rs.getString(8));
				logger.info("getBasicVendorWithGSTNo" + data);
				vendorList.add(data);
			}

			if(Objects.nonNull(vendorList) && !vendorList.isEmpty()) {
				vendorList.forEach(data -> {
					try {
						data.setGstAndAddress(groupVendorAddressBasedOnGST(data.getId()));
					}
					catch(Exception ex) {
						logger.info("Error in vendor details fetching" + ex);
					}
					
					
				});
			}
			logger.info("Successfully fetched getBasicVendorWithGSTNo" + vendorList.size());
		} catch (Exception e) {
			logger.info("Error in  getBasicVendorWithGSTNo", e);
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, null);
		}
		return vendorList;
	}

	
	
	public List<BasicVendorDetailsVo> getAllActiveVendorsforOrg(int organizationId, Connection con)
			throws ApplicationException {
		logger.info("Entry into getAllActiveVendorsforOrg");
		List<BasicVendorDetailsVo> vendorList = new ArrayList<BasicVendorDetailsVo>();
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			preparedStatement = con.prepareStatement(VendorConstants.GET_ALL_ACTIVE_VENDORS_ORGANIZATION);
			preparedStatement.setInt(1, organizationId);
			preparedStatement.setString(2, CommonConstants.STATUS_AS_ACTIVE);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				BasicVendorDetailsVo data = new BasicVendorDetailsVo();
				data.setId(rs.getInt(1));
				data.setName(rs.getString(2));
				data.setValue(rs.getInt(1));
				data.setPaymentTermsId(rs.getInt(3));
				data.setPaymentTermsDaysLimit(rs.getInt(4));
				data.setAddress_1(rs.getString(5));
				data.setAddress_2(rs.getString(6));
				data.setCity(rs.getString(7));
				data.setStateId(rs.getInt(8));
				data.setStateName(rs.getString(9));
				data.setCountryId(rs.getInt(10));
				data.setCountryName(rs.getString(11));
				data.setZipcode(rs.getString(12));
				data.setTdsId(rs.getInt(13));
				data.setGstNumber(rs.getString(14));
				data.setCurrencyId(rs.getInt(15));
				vendorList.add(data);
			}
			logger.info("Successfully fetched getAllActiveVendorsforOrg" + vendorList.size());
		} catch (Exception e) {
			logger.info("Error in  getAllActiveVendorsforOrg", e);
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, null);
		}
		return vendorList;
	}

	private Integer getPaymentTermId(String paymentTerm, int orgId) throws ApplicationException {
		logger.info("Entry into getPaymentTermId");
		Integer paymentTermId = null;
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			con = getUserMgmConnection();
			preparedStatement = con.prepareStatement(VendorConstants.GET_PAYMENT_TERM_ID);
			preparedStatement.setString(2, paymentTerm);
			preparedStatement.setInt(1, orgId);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				paymentTermId = rs.getInt(1);
			}
		} catch (Exception e) {
			logger.info("Error in  getPaymentTermId:", e);
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return paymentTermId;
	}

	public Integer getCurrencyId(String currency, int orgId) throws ApplicationException {
		logger.info("Entry into getCurrencyId");
		Integer currencyId = null;
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			con = getUserMgmConnection();
			preparedStatement = con.prepareStatement(VendorConstants.GET_CURRENCY_ID);
			preparedStatement.setString(2, currency);
			preparedStatement.setInt(1, orgId);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				currencyId = rs.getInt(1);
			}
		} catch (Exception e) {
			logger.info("Error in  getCurrencyId:", e);
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return currencyId;
	}

	public Integer getCountryId(String countryName) throws ApplicationException {
		logger.info("Entry into getCountryId");
		Integer countryId = null;
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			con = getFinanceCommon();
			preparedStatement = con.prepareStatement(VendorConstants.GET_COUNTRY_ID);
			preparedStatement.setString(1, countryName);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				countryId = rs.getInt(1);
			}
		} catch (Exception e) {
			logger.info("Error in  getCountryId:", e);
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return countryId;
	}

	public Integer getStateId(String stateName) throws ApplicationException {
		logger.info("Entry into getStateId");
		Integer stateId = null;
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			con = getFinanceCommon();
			preparedStatement = con.prepareStatement(VendorConstants.GET_STATE_ID);
			preparedStatement.setString(1, stateName);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				stateId = rs.getInt(1);
			}
		} catch (Exception e) {
			logger.info("Error in  getStateId:", e);
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return stateId;
	}

	public int getVendorId(String email, int orgId) throws ApplicationException {
		logger.info("Entry into getVendorId");
		int vendorId = 0;
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			con = getAccountsPayable();
			preparedStatement = con.prepareStatement(VendorConstants.GET_VENDOR_ID);
			preparedStatement.setString(2, email);
			preparedStatement.setInt(1, orgId);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				vendorId = rs.getInt(1);
			}

		} catch (Exception e) {
			logger.info("Error in  getVendorId:", e);
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return vendorId;
	}

	public String getVendorName(int vendorId) throws ApplicationException {
		logger.info("Entry into getVendorName");
		String vendorName = null;
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			con = getAccountsPayable();
			preparedStatement = con.prepareStatement(VendorConstants.GET_VENDOR_NAME);
			preparedStatement.setInt(1, vendorId);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				vendorName = rs.getString(1);
			}
			logger.info("Vendor Name :: " + vendorName);
		} catch (Exception e) {
			logger.info("Error in  getVendorName:", e);
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return vendorName;
	}

	public String getVendorGSTNo(int vendorId) throws ApplicationException {
		logger.info("Entry into getVendorName");
		String vendorGST = null;
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			con = getAccountsPayable();
			preparedStatement = con.prepareStatement(VendorConstants.GET_VENDOR_GST_NO);
			preparedStatement.setInt(1, vendorId);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				vendorGST = rs.getString(1);
			}
			logger.info("Vendor Name :: " + vendorGST);
		} catch (Exception e) {
			logger.info("Error in  getVendorName:", e);
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return vendorGST;
	}

	public List<CommonVo> getVendorsForOrganization(int orgId) throws ApplicationException {
		logger.info("Entry into getVendorsForOrganization");
		List<CommonVo> vendors = new ArrayList<CommonVo>();
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			con = getAccountsPayable();
			preparedStatement = con.prepareStatement(VendorConstants.GET_VENDORS_NAME_BY_ORGANIZATION);
			preparedStatement.setInt(1, orgId);
			preparedStatement.setString(2, CommonConstants.STATUS_AS_ACTIVE);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				CommonVo vendor = new CommonVo();
				vendor.setId(rs.getInt(1));
				vendor.setName(rs.getString(2));
				vendors.add(vendor);
			}
		} catch (Exception e) {
			logger.info("Error in  getVendorsForOrganization:", e);
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return vendors;
	}

	private int getVendorDestinationId(int vendorId) throws ApplicationException {
		logger.info("Entry into getVendorDestinationId");
		int destinationId = 0;
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			con = getAccountsPayable();
			preparedStatement = con.prepareStatement(VendorConstants.GET_VENDOR_DESTINATION_ADDRESS_ID);
			preparedStatement.setInt(1, vendorId);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				destinationId = rs.getInt(1);
			}

		} catch (Exception e) {
			logger.info("Error in  getVendorDestinationId:", e);
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return destinationId;
	}

	private int getVendorOriginId(int vendorId) throws ApplicationException {
		logger.info("Entry into getVendorOriginId");
		int originId = 0;
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			con = getAccountsPayable();
			preparedStatement = con.prepareStatement(VendorConstants.GET_VENDOR_ORIGIN_ADDRESS_ID);
			preparedStatement.setInt(1, vendorId);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				originId = rs.getInt(1);
			}

		} catch (Exception e) {
			logger.info("Error in  getVendorOriginId:", e);
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return originId;
	}

	private int getVendorFinanceId(int vendorId) throws ApplicationException {
		logger.info("Entry into getVendorFinanceId");
		int financeId = 0;
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			con = getAccountsPayable();
			preparedStatement = con.prepareStatement(VendorConstants.GET_VENDOR_FINANCE_ID);
			preparedStatement.setInt(1, vendorId);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				financeId = rs.getInt(1);
			}

		} catch (Exception e) {
			logger.info("Error in  getVendorFinanceId:", e);
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return financeId;
	}

	public String getVendorCurrencySymbol(int vendorId) throws ApplicationException {
		logger.info("Entry into getVendorCurrencySymbol");
		String currencySymbol = null;
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			con = getAccountsPayable();
			preparedStatement = con.prepareStatement(VendorConstants.GET_VENDOR_CURRENCY_SYMBOL);
			preparedStatement.setInt(1, vendorId);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				currencySymbol = rs.getString(1);
			}
			logger.info("Entry into getVendorCurrencySymbol>>>" + currencySymbol);
		} catch (Exception e) {
			logger.info("Error in  getVendorFinanceId:", e);
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return currencySymbol;
	}

	public String processUpload(VendorVo vendorVo, String billingAddressCountry, String billingAddressState,
			String originAddressState, String originAddressCountry, String currency, String paymentTerms, String tds,
			Boolean duplicacy) throws ApplicationException {
		logger.info("Entry into processUpload");
		VendorGeneralInformationVo vendorInfo = vendorVo.getVendorGeneralInformation();
		VendorFinanceVo vendorFinance = vendorVo.getVendorFinance();
		VendorOriginAddressVo vendorOrigin = vendorVo.getVendorOriginAddress();
		VendorDestinationAddressVo vendorDestination = vendorVo.getVendorDestinationAddress();
		if (billingAddressCountry != null) {
			Integer countryId = getCountryId(billingAddressCountry);
			if (countryId != null) {
				vendorDestination.setCountry(countryId);
			} else {
				throw new ApplicationException("Invalid Country");
			}
		}
		if (billingAddressState != null) {
			Integer stateId = getStateId(billingAddressState);
			if (stateId != null) {
				vendorDestination.setState(stateId);
			} else {
				throw new ApplicationException("Invalid State");
			}
		}
		if (originAddressState != null) {
			Integer stateId = getStateId(originAddressState);
			if (stateId != null) {
				vendorOrigin.setState(stateId);
			} else {
				throw new ApplicationException("Invalid State");
			}
		}
		if (originAddressCountry != null) {
			Integer countryId = getCountryId(originAddressCountry);
			if (countryId != null) {
				vendorOrigin.setCountry(countryId);
			} else {
				throw new ApplicationException("Invalid Country");
			}
		}
		if (currency != null) {
			Integer currencyId = getCurrencyId(currency, vendorVo.getOrganizationId());
			if (currencyId != null) {
				vendorFinance.setCurrencyId(currencyId);
			} else {
				throw new ApplicationException("Invalid Currency");
			}
		}
		if (paymentTerms != null) {
			Integer paymentTermId = getPaymentTermId(paymentTerms, vendorVo.getOrganizationId());
			if (paymentTermId != null) {
				vendorFinance.setPaymentTermsid(paymentTermId);
			} else {
				throw new ApplicationException("Invalid Payment Terms");
			}
		}
		if (tds != null) {
			Integer tdsId = getTdsId(tds);
			if (tdsId != null) {
				vendorFinance.setTdsId(tdsId);
			} else {
				throw new ApplicationException("Invalid TDS");
			}
		}

		int availableVendorsForGivenEmailAndOrg = 0;
		if (vendorVo.getVendorGeneralInformation().getPan() != null) {
			availableVendorsForGivenEmailAndOrg = checkVendorExist(vendorVo.getVendorGeneralInformation().getPan(),
					vendorVo.getOrganizationId());
		}
		if(availableVendorsForGivenEmailAndOrg > 0) {
			if (duplicacy) {
				int vendorId = getVendorId(vendorVo.getVendorGeneralInformation().getEmail(),
						vendorVo.getOrganizationId());
				vendorInfo.setId(vendorId);
				vendorFinance.setId(getVendorFinanceId(vendorId));
				vendorOrigin.setId(getVendorOriginId(vendorId));
				vendorDestination.setId(getVendorDestinationId(vendorId));
				vendorVo.setVendorGeneralInformation(vendorInfo);
				vendorVo.setVendorFinance(vendorFinance);
				vendorVo.setVendorOriginAddress(vendorOrigin);
				vendorVo.setVendorDestinationAddress(vendorDestination);
				updateVendor(vendorVo);
			}
		} else {
			createVendor(vendorVo);
			return "Create";
		}
		return null;

	}

	private Integer getTdsId(String tds) throws ApplicationException {
		logger.info("Entry into getTdsId");
		Integer tdsId = null;
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			con = getFinanceCommon();
			preparedStatement = con.prepareStatement(VendorConstants.GET_TDS_ID);
			preparedStatement.setString(1, tds);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				tdsId = rs.getInt(1);
			}
		} catch (Exception e) {
			logger.info("Error in  getTdsId:", e);
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return tdsId;
	}

	public List<VendorExportVo> getListVendorsById(ExportVo exportVo) throws ApplicationException {
		logger.info("Entry into getListVendorsById");
		List<VendorExportVo> vendors = new ArrayList<VendorExportVo>();
		if (exportVo.getListOfId() != null && exportVo.getListOfId().size() > 0) {
			for (Integer id : exportVo.getListOfId()) {
				VendorExportVo vendor = new VendorExportVo();
				vendor = getVendorGeneralInfoExport(vendor, id, exportVo.getOrganizationId());
				getVendorFinanceExport(vendor, id, exportVo.getOrganizationId());
				getVendorBillingAddressExport(vendor, id);
				getVendorOriginAddressExport(vendor, id);
				vendor.setBankDetails(getVendorBankDetails(id));
				vendor.setContacts(getVendorContactDetails(id));
				vendors.add(vendor);
			}
		}
		return vendors;
	}

	private void getVendorFinanceExport(VendorExportVo vendor, Integer id, Integer organizationId)
			throws ApplicationException {
		logger.info("Entry into getVendorFinanceExport");
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			con = getAccountsPayable();
			preparedStatement = con.prepareStatement(VendorConstants.GET_VENDOR_FINANCE_EXPORT_BY_ID);
			preparedStatement.setInt(1, id);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				Integer paymentTermId = rs.getInt(1);
				if (paymentTermId != null) {
					vendor.setPaymentTerms(paymentTermsDao.getPaymentTermName(paymentTermId, organizationId));
				}
				Integer currencyId = rs.getInt(2);
				if (currencyId != null) {
					vendor.setCurrency(currencyDao.getCurrencyName(currencyId, organizationId));
				}
				vendor.setOpeningBalance(rs.getString(3));
				Integer tdsId = rs.getInt(4);
				if (tdsId != null) {
					vendor.setTds(financeCommonDao.getTdsTypeName(tdsId));
				}
			}
		} catch (Exception e) {
			logger.info("Error in  getVendorFinanceExport:", e);
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, con);
		}
	}

	private List<ContactsExportVo> getVendorContactDetails(Integer id) throws ApplicationException {
		logger.info("Entry into getVendorContactDetails");
		List<ContactsExportVo> contacts = new ArrayList<ContactsExportVo>();
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			con = getAccountsPayable();
			preparedStatement = con.prepareStatement(VendorConstants.GET_VENDOR_CONTACT_DETAILS_EXPORT_BY_ID);
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
			logger.info("Error in  getVendorContactDetailsExport:", e);
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return contacts;
	}

	private List<BankExportVo> getVendorBankDetails(Integer id) throws ApplicationException {
		logger.info("Entry into getVendorBankDetails");
		List<BankExportVo> bankDetails = new ArrayList<BankExportVo>();
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			con = getAccountsPayable();
			preparedStatement = con.prepareStatement(VendorConstants.GET_VENDOR_BANK_DETAILS_EXPORT_BY_ID);
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
			logger.info("Error in  getVendorBankDetailsExport:", e);
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return bankDetails;
	}

	private void getVendorOriginAddressExport(VendorExportVo vendor, Integer id) throws ApplicationException {
		logger.info("Entry into getVendorOriginAddressExport");
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			con = getAccountsPayable();
			preparedStatement = con.prepareStatement(VendorConstants.GET_VENDOR_ORIGIN_ADDRESS_EXPORT_BY_ID);
			preparedStatement.setInt(1, id);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				vendor.setOriginAddrAttention(rs.getString(1));
				vendor.setOriginAddrPhone(rs.getString(2));
				Integer countryId = rs.getInt(3);
				if (countryId != null) {
					vendor.setOriginAddrCountry(financeCommonDao.getCountryName(countryId));
				}
				vendor.setOriginAddrLine1(rs.getString(4));
				vendor.setOriginAddrLine2(rs.getString(5));
				vendor.setOriginAddrLandmark(rs.getString(6));
				Integer stateId = rs.getInt(7);
				if (stateId != null) {
					vendor.setOriginAddrState(financeCommonDao.getStateName(stateId));
				}
				vendor.setOriginAddrCity(rs.getString(8));
				vendor.setOriginAddrPinCode(rs.getString(9));
			}
		} catch (Exception e) {
			logger.info("Error in  getVendorOriginAddressExport:", e);
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, con);
		}
	}

	private void getVendorBillingAddressExport(VendorExportVo vendor, Integer id) throws ApplicationException {
		logger.info("Entry into getVendorBillingAddressExport");
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			con = getAccountsPayable();
			preparedStatement = con.prepareStatement(VendorConstants.GET_VENDOR_BILLING_ADDRESS_EXPORT_BY_ID);
			preparedStatement.setInt(1, id);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				vendor.setBillingAddrAttention(rs.getString(1));
				vendor.setBillingAddrPhone(rs.getString(2));
				Integer countryId = rs.getInt(3);
				if (countryId != null) {
					vendor.setBillingAddrCountry(financeCommonDao.getCountryName(countryId));
				}
				vendor.setBillingAddrLine1(rs.getString(4));
				vendor.setBillingAddrLine2(rs.getString(5));
				vendor.setBillingAddrLandmark(rs.getString(6));
				Integer stateId = rs.getInt(7);
				if (stateId != null) {
					vendor.setBillingAddrState(financeCommonDao.getStateName(stateId));
				}
				vendor.setBillingAddrCity(rs.getString(8));
				vendor.setBillingAddrPinCode(rs.getString(9));
			}
		} catch (Exception e) {
			logger.info("Error in  getVendorBillingAddressExport:", e);
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, con);
		}

	}

	private VendorExportVo getVendorGeneralInfoExport(VendorExportVo vendor, Integer id, Integer orgId)
			throws ApplicationException {
		logger.info("Entry into getVendorGeneralInfoExport");
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			con = getAccountsPayable();
			preparedStatement = con.prepareStatement(VendorConstants.GET_VENDOR_GENERAL_INFO_EXPORT_BY_ID);
			preparedStatement.setInt(1, id);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				vendor.setId(id);
				vendor.setPrimaryContact(rs.getString(1));
				vendor.setCompanyName(rs.getString(2));
				vendor.setVendorDisplayName(rs.getString(3));
				vendor.setEmail(rs.getString(4));
				vendor.setPhone(rs.getString(5));
				vendor.setMobileNo(rs.getString(6));
				vendor.setPan(rs.getString(7));
				Integer orgTypeId = rs.getInt(8);
				if (orgTypeId != null) {
					vendor.setOrganizationType(financeCommonDao.getOrganizationType(orgTypeId));
				}
				Integer vendorGroupId = rs.getInt(9);
				if (vendorGroupId != null) {
					vendor.setVendorGroup(vendorGroupDao.getVendorGroupName(vendorGroupId, orgId));
				}
				Integer gdtTypeId = rs.getInt(10);
				if (gdtTypeId != null) {
					vendor.setGstRegnType(financeCommonDao.getGstTypeName(gdtTypeId));
				}
				vendor.setGstNumber(rs.getString(11));
				vendor.setStatus(rs.getString(12));
			}
		} catch (Exception e) {
			logger.info("Error in  getVendorGeneralInfoExport:", e);
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return vendor;
	}

	public List<VamVendorNameVo> getVendorNames(int organizationId, Connection con) throws ApplicationException {
		logger.info("Entry into getVendorNames");
		List<VamVendorNameVo> vendorDetails = new ArrayList<VamVendorNameVo>();
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			preparedStatement = con.prepareStatement(VendorConstants.GET_VENDOR_NAMES);
			preparedStatement.setInt(1, organizationId);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				VamVendorNameVo vamVendorNameVo = new VamVendorNameVo();
				vamVendorNameVo.setId(rs.getInt(1));
				vamVendorNameVo.setVendorName(rs.getString(2));
				vendorDetails.add(vamVendorNameVo);
			}
		} catch (Exception e) {
			logger.info("Error in  getVendorBankDetailsExport:", e);
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, null);
		}
		return vendorDetails;
	}

	public List<VamVendorContactVo> getVendorContacts(Connection con, Integer organizationId)
			throws ApplicationException {
		logger.info("Entry into getVendorContacts");
		List<VamVendorContactVo> contactDetails = new ArrayList<VamVendorContactVo>();
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			preparedStatement = con.prepareStatement(VendorConstants.GET_VENDOR_CONTACTS_FROM_VENDOR_CONTACT);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				VamVendorContactVo vamVendorContactVo = new VamVendorContactVo();
				vamVendorContactVo.setContactId(rs.getInt(1));
				vamVendorContactVo.setContactName(rs.getString(2));
				vamVendorContactVo.setVendorId(rs.getInt(3));
				contactDetails.add(vamVendorContactVo);
			}
		} catch (Exception e) {
			logger.info("Error in  getVendorContacts:", e);
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, null);
		}
		return contactDetails;
	}

	public VendorAddressVo getVendorDetails(int id) throws ApplicationException {
		logger.info("Entry into getVendorDetails");
		VendorAddressVo vendorAddressVo = new VendorAddressVo();
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			con = getAccountsPayable();
			preparedStatement = con.prepareStatement(VendorConstants.GET_VENDOR_DETAILS);
			preparedStatement.setInt(1, id);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				vendorAddressVo.setVendorName(rs.getString(1));
				vendorAddressVo.setGstNo(rs.getString(2));
				vendorAddressVo.setMobileNo(rs.getString(3));
				vendorAddressVo.setAttention(rs.getString(4));
				Integer countryId = rs.getInt(5);
				if (countryId != null) {
					vendorAddressVo.setCountry(financeCommonDao.getCountryName(countryId));
				}

				vendorAddressVo.setAddressLine1(rs.getString(6));
				vendorAddressVo.setAddressLine2(rs.getString(7));
				Integer stateId = rs.getInt(8);
				if (stateId != null) {
					vendorAddressVo.setState(financeCommonDao.getStateName(stateId));
				}
				vendorAddressVo.setCity(rs.getString(9));
				vendorAddressVo.setPincode(rs.getString(10));
				vendorAddressVo.setPhoneNo(rs.getString(11));
				vendorAddressVo.setLandMark(rs.getString(12));
			}
		} catch (Exception e) {
			logger.info("Error in  getVendorDetails:", e);
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return vendorAddressVo;

	}

	public List<PaymentTypeVo> getActiveVendorNames(int organizationId, Connection con) throws ApplicationException {
		logger.info("Entry into getVendorNames");
		List<PaymentTypeVo> vendorDetails = new ArrayList<PaymentTypeVo>();
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			preparedStatement = con.prepareStatement(VendorConstants.GET_ACTIVE_VENDOR_NAMES);
			preparedStatement.setInt(1, organizationId);
			preparedStatement.setString(2, CommonConstants.STATUS_AS_ACTIVE);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				PaymentTypeVo vamVendorNameVo = new PaymentTypeVo();
				vamVendorNameVo.setId(rs.getInt(1));
				vamVendorNameVo.setValue(rs.getInt(1));
				vamVendorNameVo.setName(rs.getString(2));
				vamVendorNameVo.setCurrencyId(rs.getInt(3));
				vendorDetails.add(vamVendorNameVo);
			}
		} catch (Exception e) {
			logger.info("Error in  getVendorBankDetailsExport:", e);
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, null);
		}
		return vendorDetails;
	}

	public List<VendorOriginAddressVo> getVendorsAllOriginAddressByVendorId( int vendorId)
			throws ApplicationException {
		logger.info("Entry into method: getVendorOriginAddressByVendorId"+vendorId);
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		List<VendorOriginAddressVo> vendorOriginAddressVosForOtherGst=  new ArrayList<>();
		try {
			con= getAccountsPayable();
			String query = VendorConstants.GET_VENDOR_ORIGIN_ADDRESS_BY_VENDOR_ID;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, vendorId);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
					vendorOriginAddressVosForOtherGst.add(convertQueryResultToVendorOriginalAddress(rs));
				}
			
			logger.info("vendorOriginAddressVosForOtherGst::"+vendorOriginAddressVosForOtherGst.size());
		} catch (Exception e) {
			logger.error("Error in Vendor Orgin address");
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return vendorOriginAddressVosForOtherGst;
	}
	
	
	public VendorOriginAddressVo getVendorsOriginAddressByVendorIdAndGstNo(int vendorId, String gstNo, Connection con)
			throws ApplicationException {
		logger.info("Entry into method: getVendorOriginAddressByVendorId" + vendorId);
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		VendorOriginAddressVo vendorOriginAddressVosForOtherGst = null;
		try {
			String query = VendorConstants.GET_VENDOR_ORIGIN_ADDRESS_BY_VENDOR_ID_AND_GST_NO;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, vendorId);
			preparedStatement.setString(2, gstNo);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				return convertQueryResultToVendorOriginalAddress(rs);
			}

		} catch (Exception e) {
			logger.error("Error in Vendor Orgin address");
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, null);
		}
		return vendorOriginAddressVosForOtherGst;
	}


	public List<VendorDestinationAddressVo> getVendorsAllDestinationAddressByVendorId( int vendorId) throws ApplicationException {
		logger.info("Entry into method: getVendorDestinationAddressByVendorId");
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		List<VendorDestinationAddressVo> vendorDestinationAddressVosForOtherGst = new ArrayList<>();

		try {
			con = getAccountsPayable();
			String query = VendorConstants.GET_VENDOR_DESTINATION_ADDRESS_BY_VENDOR_ID;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, vendorId);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
					vendorDestinationAddressVosForOtherGst.add(convertQueryResultToVendorDestinationAddress(rs));
				}

		} catch (Exception e) {

			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return vendorDestinationAddressVosForOtherGst;
	}
	
	
	public VendorDestinationAddressVo getVendorDestinationAddressByVendorIdAndGstNo(int vendorId, String vendorGst, Connection con)
			throws ApplicationException {
		logger.info("Entry into method: getVendorDestinationAddressByVendorId");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		VendorDestinationAddressVo vendorDestinationAddressVosForOtherGst = null;

		try {
			String query = VendorConstants.GET_VENDOR_DESTINATION_ADDRESS_BY_VENDOR_ID_AND_GST_NO;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, vendorId);
			preparedStatement.setString(2, vendorGst);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				return convertQueryResultToVendorDestinationAddress(rs);
			}

		} catch (Exception e) {

			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, null);
		}
		return vendorDestinationAddressVosForOtherGst;
	}
	
	
	public List<BaseAddressVo> getVendorAllAdditionalAddressDetailsByVendorId(int vendorId) throws ApplicationException {
		logger.info("Entry into method: getVendorAdditionalAddressDetailsByVendorId");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		List<BaseAddressVo> vendorAdditionalAddressesList = new ArrayList<>();
		Connection con = null ;
		try {
			con = getAccountsPayable();
			String queryToGetVendorContact = VendorConstants.GET_VENDOR_ADDITIONAL_ADDRESS_BY_VENDOR_ID;
			preparedStatement = con.prepareStatement(queryToGetVendorContact);
			preparedStatement.setInt(1, vendorId);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				vendorAdditionalAddressesList.add(convertQueryResultToBaseAddress(rs));
			}
		} catch (Exception e) {
			logger.error("Error in getVendorAdditionalAddressDetailsByVendorId:", e);
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return vendorAdditionalAddressesList;
	}
	
	
	public List<VendorBasedOnGstVo> groupVendorAddressBasedOnGST(int vendorId) throws ApplicationException{
		List<VendorBasedOnGstVo> addressList  = new ArrayList<VendorBasedOnGstVo>();
		List<VendorOriginAddressVo>shippingAddressList =  getVendorsAllOriginAddressByVendorId( vendorId);
		List<VendorDestinationAddressVo>billingAddress =  getVendorsAllDestinationAddressByVendorId(vendorId);
		List<BaseAddressVo> allAddress = getVendorAllAdditionalAddressDetailsByVendorId(vendorId);
		for(VendorOriginAddressVo shipping : shippingAddressList) {
			VendorBasedOnGstVo address = new VendorBasedOnGstVo();
			address.setGstNo(shipping.getGstNo());
			address.setVendorOriginAddress(shipping);
			if(billingAddress!=null && !billingAddress.isEmpty() ) {
			Optional<VendorDestinationAddressVo> value = 	billingAddress.stream().filter(val -> val.getGstNo().equals(shipping.getGstNo())).findFirst();
			if(value.isPresent()) {
				address.setVendorDestinationAddress(value.get());
			}
			if(Objects.nonNull(allAddress) && !CollectionUtils.isEmpty(allAddress) ) {
			List<BaseAddressVo> otherAddress = allAddress.stream().filter(add -> add.getGstNo().equals(shipping.getGstNo())).collect(Collectors.toList());
			address.setVendorAdditionalAddresses(otherAddress);
			}
			}
			addressList.add(address);
		}
		logger.info("addressList" +addressList.size());
		return addressList;
		
		
	}

	public boolean updateVendorAddressesBasedOnGst(VendorBasedOnGstVo vendorBasedOnGstVo, Integer orgId,Integer vendorId, String userId, String roleName) throws ApplicationException {
			boolean isSuccess = false;
			if(vendorBasedOnGstVo!=null ) {
				Connection con =null;
				try {
					con = getAccountsPayable();
					updateVendorDestinationAddress(vendorBasedOnGstVo.getVendorDestinationAddress(), con, vendorId);
					updateVendorOriginAddress(vendorBasedOnGstVo.getVendorOriginAddress() , con, vendorId);
					isSuccess = true;
				} catch (ApplicationException e) {
					logger.info("eror in updateAddress");
					throw new ApplicationException(e.getMessage());
				}finally{
					closeResources(null, null, con);
				}
			}
		
		return isSuccess;
	}
	
	
}
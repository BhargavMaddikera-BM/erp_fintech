package com.blackstrawai.vendorsettings;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.accessandroles.AccessAndRolesDao;
import com.blackstrawai.accessandroles.Level1AccessVo;
import com.blackstrawai.accessandroles.Level2AccessVo;
import com.blackstrawai.common.BaseDao;
import com.blackstrawai.keycontact.VendorConstants;
import com.blackstrawai.keycontact.VendorDao;
import com.blackstrawai.keycontact.vendor.VendorVo;
import com.blackstrawai.onboarding.LoginAndRegistrationConstants;
import com.blackstrawai.settings.SettingsAndPreferencesConstants;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Repository
public class VendorAccessMgmtDao extends BaseDao {

	private Logger logger = Logger.getLogger(VendorAccessMgmtDao.class);

	@Autowired
	VendorSettingsDao vendorSettingsDao;

	@Autowired
	VendorDao vendorDao;

	@Autowired
	AccessAndRolesDao accessAndRolesDao;

	public boolean createVendorSettings(VamVendorVo vamVendorVo) throws ApplicationException {
		logger.info("Entry into createVendorSettings");
		String settingsData = null;
		String contacts = new String();
		PredefinedSettingsVo predefinedSettingsVo;
		if (vamVendorVo.getSettingId() != null) {
			predefinedSettingsVo = vendorSettingsDao.getPredefinedSettingById(vamVendorVo.getSettingId(),
					vamVendorVo.getOrgId());
			ObjectMapper mapper = new ObjectMapper();
			try {
				settingsData = mapper.writeValueAsString(predefinedSettingsVo);
			} catch (JsonProcessingException e1) {
				throw new ApplicationException(e1.getMessage());
			}
			if (vamVendorVo.getContactsId() != null && vamVendorVo.getContactsId().size() > 0) {
				List<Integer> contactsId = vamVendorVo.getContactsId();
				for (Integer id : contactsId) {
					contacts = contacts + String.valueOf(id) + ",";
				}
				contacts = contacts.substring(0, contacts.length() - 1);
			}
			if (predefinedSettingsVo != null) {
				Connection con = null;
				PreparedStatement preparedStatement = null;
				try {
					con = getAccountsPayable();
					preparedStatement = con.prepareStatement(VendorConstants.UPDATE_VENDOR_SETTINGS);
					preparedStatement.setString(1, settingsData);
					preparedStatement.setString(2,
							predefinedSettingsVo.getName() != null ? predefinedSettingsVo.getName() : null);
					preparedStatement.setInt(3,
							predefinedSettingsVo.getId() != null ? predefinedSettingsVo.getId() : null);
					preparedStatement.setString(4, vamVendorVo.getStatus() != null ? vamVendorVo.getStatus() : null);
					preparedStatement.setString(5, contacts != null ? contacts : null);
					preparedStatement.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
					preparedStatement.setInt(7, vamVendorVo.getVendorId());
					preparedStatement.executeUpdate();
					setVendorOnboradingType(predefinedSettingsVo, vamVendorVo.getVendorId());
					return registerVendor(vamVendorVo);
				} catch (Exception e) {
					logger.info("Error in  createVendorSettings:", e);
					throw new ApplicationException(e.getMessage());
				} finally {
					closeResources(null, preparedStatement, con);
				}
			}

		}
		return false;
	}

	private void setVendorOnboradingType(PredefinedSettingsVo predefinedSettingsVo, Integer vendorId)
			throws ApplicationException {
		logger.info("Entry into setVendorOnboradingType");
		Boolean isQuick = false;
		Map<String, List<SettingsTemplateVo>> templates = predefinedSettingsVo.getTemplate();
		for (Map.Entry<String, List<SettingsTemplateVo>> entry : templates.entrySet()) {
			List<SettingsTemplateVo> settings = entry.getValue();
			if (settings != null) {
				for (SettingsTemplateVo template : settings) {
					if (template.getTemplateType().equalsIgnoreCase("Vendor Onboarding")) {
						if (template.getTemplateName().equalsIgnoreCase("quick")) {
							isQuick = true;
						}
					}
				}
			}
		}
		Connection con = null;
		PreparedStatement preparedStatement = null;
		try {
			con = getAccountsPayable();
			preparedStatement = con.prepareStatement(VendorConstants.SET_VENDOR_ONBOARDING_TYPE);
			preparedStatement.setBoolean(1, isQuick);
			preparedStatement.setInt(2, vendorId);
			preparedStatement.executeUpdate();
		} catch (Exception e) {
			logger.info("Error in  setVendorOnboradingType:", e);
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(null, preparedStatement, con);
		}

	}

	private boolean registerVendor(VamVendorVo vamVendorVo) throws ApplicationException {
		logger.info("Entry into registerVendor");
		VendorDetailsVo vendorDetailsVo = getVendorDetails(vamVendorVo.getVendorId());
		if (vendorDetailsVo.getEmail() != null) {
			String password = checkInRegistration(vendorDetailsVo.getEmail());
			String accessData = getVendorRoles(vamVendorVo.getVendorId());
			if (password != null) {
				Connection con = null;
				PreparedStatement preparedStatement = null;
				try {
					con = getUserMgmConnection();
					preparedStatement = con.prepareStatement(VendorSettingsConstants.INSER_KEY_CONTACTS_REGISTRATION);
					preparedStatement.setString(1,
							vendorDetailsVo.getEmail() != null ? vendorDetailsVo.getEmail() : null);
					preparedStatement.setString(2,
							vendorDetailsVo.getPhoneNo() != null ? vendorDetailsVo.getPhoneNo() : null);
					preparedStatement.setString(3, password);
					preparedStatement.setString(4, null);
					preparedStatement.setString(5, "Vendor");
					preparedStatement.setInt(6, vamVendorVo.getOrgId());
					preparedStatement.setString(7, "ACT");
					preparedStatement.setString(8,
							vendorDetailsVo.getVendorDisplayName() != null ? vendorDetailsVo.getVendorDisplayName()
									: null);
					preparedStatement.setString(9, accessData);
					preparedStatement.executeUpdate();
				} catch (Exception e) {
					logger.info("Error in  registerVendor:", e);
					throw new ApplicationException(e.getMessage());
				} finally {
					closeResources(null, preparedStatement, con);
				}

				return true;
			} else {
				Connection con = null;
				PreparedStatement preparedStatement = null;
				try {
					con = getUserMgmConnection();
					preparedStatement = con.prepareStatement(VendorSettingsConstants.INSER_KEY_CONTACTS_REGISTRATION);
					preparedStatement.setString(1,
							vendorDetailsVo.getEmail() != null ? vendorDetailsVo.getEmail() : null);
					preparedStatement.setString(2,
							vendorDetailsVo.getPhoneNo() != null ? vendorDetailsVo.getPhoneNo() : null);
					preparedStatement.setString(3, null);
					preparedStatement.setString(4, null);
					preparedStatement.setString(5, "Vendor");
					preparedStatement.setInt(6, vamVendorVo.getOrgId());
					preparedStatement.setString(7, "INA");
					preparedStatement.setString(8,
							vendorDetailsVo.getVendorDisplayName() != null ? vendorDetailsVo.getVendorDisplayName()
									: null);
					preparedStatement.setString(9, accessData);
					preparedStatement.executeUpdate();

				} catch (Exception e) {
					logger.info("Error in  registerVendor:", e);
					throw new ApplicationException(e.getMessage());
				} finally {
					closeResources(null, preparedStatement, con);
				}

				String password1 = checkInKeyContactsRegistration(vendorDetailsVo.getEmail());
				if (password1 != null) {
					try {
						con = getUserMgmConnection();
						preparedStatement = con.prepareStatement(VendorSettingsConstants.UPDATE_PASSWORD);
						preparedStatement.setString(1, password1);
						preparedStatement.setString(2, "ACT");
						preparedStatement.setString(3, vendorDetailsVo.getEmail());
						preparedStatement.executeUpdate();
						return true;
					} catch (Exception e) {
						logger.info("Error in  updatePassword:", e);
						throw new ApplicationException(e.getMessage());
					} finally {
						closeResources(null, preparedStatement, con);
					}

				} else {
					return false;

				}
			}
		}
		return false;
	}

	private String getVendorRoles(Integer vendorId) throws ApplicationException {
		logger.info("Entry into getVendorRoles");
		String settingsData = new String();
		String accessData = null;
		boolean isActive = false, isQuick = false, isInvoiceQuick = false, isBalanceConfirmationQuick = false,
				isActivePurchaseOrder = false, isBalanceConfirmationActive = false;
		;
		boolean currentVendorStatement = false, historicalVendorStatement = false;
		boolean createInvoice = false, viewCreatedInvoice = false, viewAllInvoices = false;
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			con = getAccountsPayable();
			preparedStatement = con.prepareStatement(VendorConstants.GET_VENDOR_SETTINGS_DATA);
			preparedStatement.setInt(1, vendorId);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				settingsData = rs.getString(1);
			}
		} catch (Exception e) {
			logger.info("Error in  getVendorRoles:", e);
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, con);
		}

		try {
			PredefinedSettingsVo predefinedSettingsVo = new ObjectMapper().readValue(settingsData,
					PredefinedSettingsVo.class);
			List<SettingsModuleVo> modules = predefinedSettingsVo.getModules();
			for (SettingsModuleVo settingsModules : modules) {
				if (settingsModules.getModule().equalsIgnoreCase("Vendor Onboarding")) {
					isActive = settingsModules.getIsActive();
				}
				if (settingsModules.getModule().equalsIgnoreCase("View Current Vendor Statements")) {
					currentVendorStatement = settingsModules.getIsActive();
				}
				if (settingsModules.getModule().equalsIgnoreCase("View Historical Vendor Statements")) {
					historicalVendorStatement = settingsModules.getIsActive();
				}
				if (settingsModules.getModule().equalsIgnoreCase("Manage Purchase Order")) {
					isActivePurchaseOrder = settingsModules.getIsActive();
				}
				if (settingsModules.getModule().equalsIgnoreCase("Submit Balance Confirmation")) {
					isBalanceConfirmationActive = settingsModules.getIsActive();
				}
				if (settingsModules.getModule().equalsIgnoreCase("Create Invoice")) {
					createInvoice = settingsModules.getIsActive();
				}
				if (settingsModules.getModule().equalsIgnoreCase("View Created Invoice")) {
					viewCreatedInvoice = settingsModules.getIsActive();
				}
				if (settingsModules.getModule().equalsIgnoreCase("View All Invoices")) {
					viewAllInvoices = settingsModules.getIsActive();
				}

			}
			Map<String, List<SettingsTemplateVo>> templates = predefinedSettingsVo.getTemplate();
			for (Map.Entry<String, List<SettingsTemplateVo>> entry : templates.entrySet()) {
				List<SettingsTemplateVo> settings = entry.getValue();
				for (SettingsTemplateVo template : settings) {
					if (template.getTemplateType().equalsIgnoreCase("Vendor Onboarding")) {
						if (template.getTemplateName().equalsIgnoreCase("quick")) {
							if (template.getIsActive()) {
								isQuick = true;
							}

						}
					}
					if (template.getTemplateType().equalsIgnoreCase("Balance Confirmation")) {
						if (template.getTemplateName().equalsIgnoreCase("quick")) {
							if (template.getIsActive()) {
								isBalanceConfirmationQuick = true;
							}

						}
					}
					if (template.getTemplateType().equalsIgnoreCase("Invoice")) {
						if (template.getTemplateName().equalsIgnoreCase("quick")) {
							if (template.getIsActive()) {
								isInvoiceQuick = true;
							}
						}
					}
				}
			}
		} catch (Exception e) {
			throw new ApplicationException(e.getMessage());
		}

		List<Level1AccessVo> data = accessAndRolesDao.constructMinimalAccess("Vendor");
		int size = data.size();

		Level1AccessVo level1AccessVo = new Level1AccessVo();
		level1AccessVo.setId(++size);
		level1AccessVo.setName("Purchase Order");
		level1AccessVo.setKey("purchase-order");
		level1AccessVo.setHasChildren(false);
		level1AccessVo.setActions(null);
		List<String> tableColums = new ArrayList<String>();
		level1AccessVo.setTableColums(tableColums);
		level1AccessVo.setIsDetailed(false);
		level1AccessVo.setSubmodules(null);
		if (isActivePurchaseOrder) {
			level1AccessVo.setHasAccess(true);
		} else {
			level1AccessVo.setHasAccess(false);
		}

		data.add(level1AccessVo);

		level1AccessVo = new Level1AccessVo();
		level1AccessVo.setName("Vendor Onboarding");
		level1AccessVo.setKey("vendor-onboarding");
		level1AccessVo.setId(++size);
		if (isActive) {
			level1AccessVo.setHasAccess(true);
		} else {
			level1AccessVo.setHasAccess(false);
		}
		level1AccessVo.setHasChildren(false);
		Map<String, Boolean> actionsOnBoarding = new HashMap<String, Boolean>();
		tableColums = new ArrayList<String>();
		level1AccessVo.setTableColums(tableColums);
		if (isQuick) {
			level1AccessVo.setIsDetailed(false);
			actionsOnBoarding.put("isDetailed", false);
		} else {
			level1AccessVo.setIsDetailed(true);
			actionsOnBoarding.put("isDetailed", true);
		}
		level1AccessVo.setActions(actionsOnBoarding);
		level1AccessVo.setSubmodules(null);
		data.add(level1AccessVo);

		level1AccessVo = new Level1AccessVo();
		level1AccessVo.setName("Statments");
		level1AccessVo.setKey("statements");
		level1AccessVo.setId(++size);
		level1AccessVo.setHasAccess(true);
		level1AccessVo.setHasChildren(true);
		tableColums = new ArrayList<String>();
		level1AccessVo.setTableColums(tableColums);

		List<Level2AccessVo> submodules = new ArrayList<Level2AccessVo>();
		Level2AccessVo level2AccessVo = new Level2AccessVo();
		level2AccessVo.setId(1);
		level2AccessVo.setName("View Statements");
		level2AccessVo.setKey("view-statements");
		level2AccessVo.setHasAccess(true);
		level2AccessVo.setHasChildren(false);
		level2AccessVo.setIsDetailed(false);
		Map<String, Boolean> actionsViewStatement = new HashMap<String, Boolean>();
		actionsViewStatement.put("current", currentVendorStatement);
		actionsViewStatement.put("historical", historicalVendorStatement);
		level2AccessVo.setActions(actionsViewStatement);
		submodules.add(level2AccessVo);

		level2AccessVo = new Level2AccessVo();
		level2AccessVo.setId(2);
		level2AccessVo.setName("Balance Confirmations");
		level2AccessVo.setKey("balance-confirmations");
		if (isBalanceConfirmationActive) {
			level2AccessVo.setHasAccess(true);
		} else {
			level2AccessVo.setHasAccess(false);
		}
		level2AccessVo.setHasChildren(false);
		Map<String, Boolean> actionsOnBalanceConfirmation = new HashMap<String, Boolean>();
		if (isBalanceConfirmationQuick) {
			level2AccessVo.setIsDetailed(false);
			actionsOnBalanceConfirmation.put("isDetailed", false);
		} else {
			level2AccessVo.setIsDetailed(true);
			actionsOnBalanceConfirmation.put("isDetailed", true);
		}
		actionsOnBalanceConfirmation.put("create", true);
		actionsOnBalanceConfirmation.put("update", true);
		actionsOnBalanceConfirmation.put("withdraw", true);
		actionsOnBalanceConfirmation.put("view", true);
		actionsOnBalanceConfirmation.put("delete", true);
		level2AccessVo.setActions(actionsOnBalanceConfirmation);
		submodules.add(level2AccessVo);

		level1AccessVo.setSubmodules(submodules);
		data.add(level1AccessVo);

		level1AccessVo = new Level1AccessVo();
		level1AccessVo.setName("Invoices");
		level1AccessVo.setKey("invoices");
		level1AccessVo.setId(++size);
		level1AccessVo.setHasAccess(true);
		level1AccessVo.setHasChildren(false);
		Map<String, Boolean> actionsInvoice = new HashMap<String, Boolean>();
		tableColums = new ArrayList<String>();
		level1AccessVo.setTableColums(tableColums);
		if (isInvoiceQuick) {
			level1AccessVo.setIsDetailed(false);
			actionsInvoice.put("isDetailed", false);
		} else {
			level1AccessVo.setIsDetailed(true);
			actionsInvoice.put("isDetailed", true);
		}
		actionsInvoice.put("view", true);
		actionsInvoice.put("update", true);
		actionsInvoice.put("delete", true);
		actionsInvoice.put("create", createInvoice);
		actionsInvoice.put("viewAll", viewAllInvoices);
		actionsInvoice.put("viewCreated", viewCreatedInvoice);
		level1AccessVo.setActions(actionsInvoice);
		level1AccessVo.setSubmodules(null);
		data.add(level1AccessVo);

		ObjectMapper mapper = new ObjectMapper();
		try {
			accessData = mapper.writeValueAsString(data);
		} catch (JsonProcessingException e1) {
			throw new ApplicationException(e1.getMessage());
		}

		return accessData;

	}

	private String checkInKeyContactsRegistration(String email) throws ApplicationException {
		logger.info("Entry into checkInKeyContactsRegistration");
		String password = null;
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			con = getUserMgmConnection();
			preparedStatement = con.prepareStatement(VendorSettingsConstants.CHECK_KEY_CONTACT_REGISTRATION);
			preparedStatement.setString(1, email);
			preparedStatement.setString(2, "ACT");
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				password = rs.getString(1);
			}
		} catch (Exception e) {
			logger.info("Error in  checkInKeyContactsRegistration:", e);
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return password;
	}

	public VendorDetailsVo getVendorDetails(Integer vendorId) throws ApplicationException {
		logger.info("Entry into getVendorDetails");
		VendorDetailsVo vendorDetailsVo = new VendorDetailsVo();
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			con = getAccountsPayable();
			preparedStatement = con.prepareStatement(VendorConstants.GET_VENDOR_DETAILS1);
			preparedStatement.setInt(1, vendorId);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				vendorDetailsVo.setEmail(rs.getString(1));
				vendorDetailsVo.setPhoneNo(rs.getString(2));
				vendorDetailsVo.setVendorDisplayName(rs.getString(3));
			}
		} catch (Exception e) {
			logger.info("Error in  getVendorDetails:", e);
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return vendorDetailsVo;
	}

	private String checkInRegistration(String email) throws ApplicationException {
		logger.info("Entry into checkInRegistration");
		String password = null;
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			con = getUserMgmConnection();
			preparedStatement = con.prepareStatement(LoginAndRegistrationConstants.CHECK_EMAIL_REGISTRATION);
			preparedStatement.setString(1, email);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				password = rs.getString(1);
			}
		} catch (Exception e) {
			logger.info("Error in  checkInRegistration:", e);
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return password;

	}

	public Map<VamVendorVo, Boolean> createVendorGroupSettings(VamVendorGroupVo vamVendorGroupVo)
			throws ApplicationException {
		logger.info("Entry into createVendorGroupSettings");
		String settingsData = null;
		Map<VamVendorVo, Boolean> vendorList = new HashMap<VamVendorVo, Boolean>();
		if (vamVendorGroupVo.getSeetingsId() != null) {
			PredefinedSettingsVo predefinedSettingsVo = vendorSettingsDao
					.getPredefinedSettingById(vamVendorGroupVo.getSeetingsId(), vamVendorGroupVo.getOrgId());
			ObjectMapper mapper = new ObjectMapper();
			try {
				settingsData = mapper.writeValueAsString(predefinedSettingsVo);
			} catch (JsonProcessingException e1) {
				throw new ApplicationException(e1.getMessage());
			}
			Connection con = null;
			PreparedStatement preparedStatement = null;
			try {
				con = getUserMgmConnection();
				preparedStatement = con.prepareStatement(VendorConstants.UPDATE_VENDOR_GRUOP_SETTINGS);
				preparedStatement.setString(1, settingsData);
				preparedStatement.setString(2, predefinedSettingsVo.getName());
				preparedStatement.setInt(3, predefinedSettingsVo.getId());
				preparedStatement.setString(4, vamVendorGroupVo.getStatus());
				preparedStatement.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
				preparedStatement.setInt(6, vamVendorGroupVo.getVendorGroupId());
				preparedStatement.executeUpdate();
				vendorList = createVendorsSettings(vamVendorGroupVo);
			} catch (Exception e) {
				logger.info("Error in  createVendorGroupSettings:", e);
				throw new ApplicationException(e.getMessage());
			} finally {
				closeResources(null, preparedStatement, con);
			}
		}
		return vendorList;
	}

	private Map<VamVendorVo, Boolean> createVendorsSettings(VamVendorGroupVo vamVendorGroupVo)
			throws ApplicationException {
		logger.info("Entry into createVendorsSettings");
		Map<VamVendorVo, Boolean> vendorList = new HashMap<VamVendorVo, Boolean>();
		Boolean status;
		List<Integer> vendors = getVendorsId(vamVendorGroupVo.getVendorGroupId());
		if (vendors.size() > 0) {
			for (Integer id : vendors) {
				VamVendorVo VamVendorVo = new VamVendorVo();
				VamVendorVo.setVendorId(id);
				VamVendorVo.setOrgId(vamVendorGroupVo.getOrgId());
				VamVendorVo.setSettingId(vamVendorGroupVo.getSeetingsId());
				VamVendorVo.setSuperAdmin(vamVendorGroupVo.isSuperAdmin());
				VamVendorVo.setUserId(vamVendorGroupVo.getUserId());
				VamVendorVo.setStatus(vamVendorGroupVo.getStatus());
				status = createVendorSettings(VamVendorVo);
				vendorList.put(VamVendorVo, status);
			}
		}
		return vendorList;
	}

	private List<Integer> getVendorsId(Integer vendorGroupId) throws ApplicationException {
		logger.info("Entry into getVendorsId");
		List<Integer> vendors = new ArrayList<Integer>();
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			con = getAccountsPayable();
			preparedStatement = con.prepareStatement(VendorConstants.GET_VENDORS);
			preparedStatement.setInt(1, vendorGroupId);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				int id = rs.getInt(1);
				if (rs.getString(2) == null) {
					vendors.add(id);
				}
			}
		} catch (Exception e) {
			logger.info("Error in  getVendorsId:", e);
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return vendors;
	}

	public List<VamBasicVendorVo> getVendorSettings(Integer organizationId) throws ApplicationException {
		logger.info("Entry into getVendorSettings");
		List<VamBasicVendorVo> vendorList = new ArrayList<VamBasicVendorVo>();
		if (organizationId != null) {
			Connection con = null;
			PreparedStatement preparedStatement = null;
			ResultSet rs = null;
			try {
				con = getAccountsPayable();
				preparedStatement = con.prepareStatement(VendorConstants.GET_VENDOR_SETTINGS);
				preparedStatement.setInt(1, organizationId);
				rs = preparedStatement.executeQuery();
				while (rs.next()) {
					VamBasicVendorVo vendor = new VamBasicVendorVo();
					vendor.setVendorId(rs.getInt(1));
					vendor.setVendorName(rs.getString(2));
					vendor.setStatus(rs.getString(3));
					vendor.setSettingsName(rs.getString(4));
					vendor.setVendorEmail(rs.getString(5));
					String contacts = rs.getString(6);
					List<String> contactsEmail = new ArrayList<String>();
					if (contacts != null && !contacts.isEmpty()) {
						String[] cotactIds = contacts.split(",");
						for (String id : cotactIds) {
							String email = getContactsName(id);
							contactsEmail.add(email);
						}
						vendor.setContactsEmail(contactsEmail);
					}
					if (vendor.getSettingsName() != null && !vendor.getSettingsName().isEmpty()) {
						vendorList.add(vendor);
					}
				}
			} catch (Exception e) {
				logger.info("Error in  getVendorSettings:", e);
				throw new ApplicationException(e.getMessage());
			} finally {
				closeResources(rs, preparedStatement, con);
			}
		}
		return vendorList;
	}

	private String getContactsName(String id) throws ApplicationException {
		logger.info("Entry into getContactsName");
		String contactName = null;
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			con = getAccountsPayable();
			preparedStatement = con.prepareStatement(VendorConstants.GET_VENDOR_CONTACT_NAME);
			preparedStatement.setInt(1, Integer.valueOf(id));
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				contactName = rs.getString(1);
			}
		} catch (Exception e) {
			logger.info("Error in  getContactsName:", e);
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return contactName;
	}

	public List<VamBasicVendorGroupVo> getVendorGroupSettings(Integer organizationId) throws ApplicationException {
		logger.info("Entry into getVendorGroupSettings");
		List<VamBasicVendorGroupVo> vendorGroupList = new ArrayList<VamBasicVendorGroupVo>();
		if (organizationId != null) {
			Connection con = null;
			PreparedStatement preparedStatement = null;
			ResultSet rs = null;
			try {
				con = getUserMgmConnection();
				preparedStatement = con.prepareStatement(SettingsAndPreferencesConstants.GET_VENDOR_GROUP_SETTINGS);
				preparedStatement.setInt(1, organizationId);
				rs = preparedStatement.executeQuery();
				while (rs.next()) {
					VamBasicVendorGroupVo vamVendorGroupVo = new VamBasicVendorGroupVo();
					vamVendorGroupVo.setVendorGroupId(rs.getInt(1));
					vamVendorGroupVo.setVendorGroupName(rs.getString(2));
					vamVendorGroupVo.setStatus(rs.getString(3));
					vamVendorGroupVo.setSettingsName(rs.getString(4));
					if (vamVendorGroupVo.getSettingsName() != null && !vamVendorGroupVo.getSettingsName().isEmpty()) {
						vendorGroupList.add(vamVendorGroupVo);
					}

				}
			} catch (Exception e) {
				logger.info("Error in  getVendorGroupSettings:", e);
				throw new ApplicationException(e.getMessage());
			} finally {
				closeResources(rs, preparedStatement, con);
			}
		}
		return vendorGroupList;
	}

	public void ActiveAndDeactiveVendorSettings(int vendorId, String status) throws ApplicationException {
		logger.info("Entry into ActiveAndDeactiveVendorSettings");
		Connection con = null;
		PreparedStatement preparedStatement = null;
		try {
			con = getAccountsPayable();
			preparedStatement = con.prepareStatement(VendorConstants.ACTIVE_DEACTIVE_VENDOR_SETTINGS);
			preparedStatement.setString(1, status);
			preparedStatement.setInt(2, vendorId);
			preparedStatement.executeUpdate();
		} catch (Exception e) {
			logger.info("Error in  ActiveAndDeactiveVendorSettings:", e);
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(null, preparedStatement, con);
		}
	}

	public void ActiveAndDeactiveVendorGroupSettings(int vendorGroupId, String status) throws ApplicationException {
		logger.info("Entry into ActiveAndDeactiveVendorGroupSettings");
		Connection con = null;
		PreparedStatement preparedStatement = null;
		try {
			con = getUserMgmConnection();
			preparedStatement = con
					.prepareStatement(SettingsAndPreferencesConstants.ACTIVE_DEACTIVE_VENDOR_GROUP_SETTINGS);
			preparedStatement.setString(1, status);
			preparedStatement.setInt(2, vendorGroupId);
			preparedStatement.executeUpdate();
		} catch (Exception e) {
			logger.info("Error in  ActiveAndDeactiveVendorGroupSettings:", e);
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(null, preparedStatement, con);
		}
	}

	public VamVendorSettingsVo getVendorSettingsById(int vendorId) throws ApplicationException {
		logger.info("Entry into getVendorSettingsById");
		VamVendorSettingsVo vendorSettingsVo = new VamVendorSettingsVo();
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement preparedStatement = null;
		try {
			con = getAccountsPayable();
			preparedStatement = con.prepareStatement(VendorConstants.GET_VENDOR_SETTINGS_BY_ID);
			preparedStatement.setInt(1, vendorId);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				vendorSettingsVo.setVendorName(rs.getString(1));
				vendorSettingsVo.setVendorId(rs.getInt(2));
				vendorSettingsVo.setStatus(rs.getString(3));
				vendorSettingsVo.setSettingsName(rs.getString(4));
				vendorSettingsVo.setSettingsId(rs.getInt(5));
				vendorSettingsVo.setVendorEmail(rs.getString(6));
				String contacts = rs.getString(7);
				List<Integer> contactsId = new ArrayList<Integer>();
				if (contacts != null && !contacts.isEmpty()) {
					String[] cotactIds = contacts.split(",");
					for (String id : cotactIds) {
						contactsId.add(Integer.valueOf(id));
					}
				}
				vendorSettingsVo.setContactsId(contactsId);
				String settingsData = rs.getString(8);
				PredefinedSettingsVo predefinedSettingsVo = new ObjectMapper().readValue(settingsData,
						PredefinedSettingsVo.class);
				vendorSettingsVo.setSettingsData(predefinedSettingsVo);
			}
		} catch (Exception e) {
			logger.info("Error in  getVendorSettingsById:", e);
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return vendorSettingsVo;
	}

	public VamVendorGroupSettingsVo getVendorGroupSettingsById(int organizationId, int vendorGroupId)
			throws ApplicationException {
		logger.info("Entry into getVendorGroupSettingsById");
		VamVendorGroupSettingsVo vendorGroupSettingsVo = new VamVendorGroupSettingsVo();
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			con = getUserMgmConnection();
			preparedStatement = con.prepareStatement(SettingsAndPreferencesConstants.GET_VENDOR_GROUP_SETTINGS_BY_ID);
			preparedStatement.setInt(1, vendorGroupId);
			preparedStatement.setInt(2, organizationId);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				vendorGroupSettingsVo.setVendorGroupName(rs.getString(1));
				vendorGroupSettingsVo.setVendorGroupId(rs.getInt(2));
				vendorGroupSettingsVo.setStatus(rs.getString(3));
				vendorGroupSettingsVo.setSettingsName(rs.getString(4));
				vendorGroupSettingsVo.setSettingsId(rs.getInt(5));
				String settingsData = rs.getString(6);
				PredefinedSettingsVo predefinedSettingsVo = new ObjectMapper().readValue(settingsData,
						PredefinedSettingsVo.class);
				vendorGroupSettingsVo.setSettingsData(predefinedSettingsVo);
			}
		} catch (Exception e) {
			logger.info("Error in  getVendorGroupSettingsById:", e);
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return vendorGroupSettingsVo;
	}

	public void editVendorSettings(VamVendorSettingsVo vamVendorSettingsVo) throws ApplicationException {
		logger.info("Entry into editVendorSettings");
		String contacts = new String();
		Connection con = null;
		PreparedStatement preparedStatement = null;
		try {
			con = getAccountsPayable();
			preparedStatement = con.prepareStatement(VendorConstants.UPDATE_VENDOR_SETTINGS_BY_ID);
			preparedStatement.setString(1, vamVendorSettingsVo.getSettingsName());
			preparedStatement.setObject(2,
					vamVendorSettingsVo.getSettingsId() != null ? vamVendorSettingsVo.getSettingsId() : null);
			preparedStatement.setString(3, vamVendorSettingsVo.getStatus());
			if (vamVendorSettingsVo.getContactsId() != null && vamVendorSettingsVo.getContactsId().size() > 0) {
				List<Integer> contactsId = vamVendorSettingsVo.getContactsId();
				for (Integer id : contactsId) {
					contacts = contacts + String.valueOf(id) + ",";
				}
				contacts = contacts.substring(0, contacts.length() - 1);
			}
			preparedStatement.setString(4, contacts);
			PredefinedSettingsVo predefinedSettingsVo = vamVendorSettingsVo.getSettingsData();
			predefinedSettingsVo
					.setId(vamVendorSettingsVo.getSettingsId() != null ? vamVendorSettingsVo.getSettingsId() : null);
			predefinedSettingsVo.setName(vamVendorSettingsVo.getSettingsName());
			String settingsData = new String();
			ObjectMapper mapper = new ObjectMapper();
			try {
				settingsData = mapper.writeValueAsString(predefinedSettingsVo);
			} catch (JsonProcessingException e1) {
				throw new ApplicationException(e1.getMessage());
			}
			preparedStatement.setString(5, settingsData);
			preparedStatement.setInt(6, vamVendorSettingsVo.getVendorId());
			preparedStatement.executeUpdate();
			String accessData = getVendorRoles(vamVendorSettingsVo.getVendorId());
			updateKeyContactsForTypeVendor(vamVendorSettingsVo.getVendorId(), accessData,
					vamVendorSettingsVo.getOrganizationId());
		} catch (Exception e) {
			logger.info("Error in  editVendorSettings:", e);
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(null, preparedStatement, con);
		}
	}

	private void updateKeyContactsForTypeVendor(Integer vendorId, String settingsData, Integer orgId)
			throws ApplicationException {
		logger.info("Entry into updateKeyContactsForTypeVendor");
		Connection con = null;
		PreparedStatement preparedStatement = null;
		VendorVo vendorVo = vendorDao.getVendorByVendorId(vendorId);
		String email = vendorVo.getVendorGeneralInformation().getEmail();
		if (email != null) {
			try {
				con = getUserMgmConnection();
				preparedStatement = con
						.prepareStatement(VendorConstants.UPDATE_VENDOR_SETTINGS_IN_KEY_CONTACTS_REGISTRATION);
				preparedStatement.setString(1, settingsData);
				preparedStatement.setInt(2, orgId);
				preparedStatement.setString(3, email);
				preparedStatement.executeUpdate();
			} catch (Exception e) {
				logger.info("Error in  updateInKeyContactsTable:", e);
				throw new ApplicationException(e.getMessage());
			} finally {
				closeResources(null, preparedStatement, con);
			}
		}
	}

	public void editVendorGroupSettings(VamVendorGroupSettingsVo vamVendorGroupSettingsVo) throws ApplicationException {
		logger.info("Entry into editVendorGroupSettings");
		Connection con = null;
		PreparedStatement preparedStatement = null;
		try {
			con = getUserMgmConnection();
			preparedStatement = con
					.prepareStatement(SettingsAndPreferencesConstants.UPDATE_VENDOR_GROUP_SETTINGS_BY_ID);
			preparedStatement.setString(1, vamVendorGroupSettingsVo.getSettingsName());
			preparedStatement.setObject(2,
					vamVendorGroupSettingsVo.getSettingsId() != null ? vamVendorGroupSettingsVo.getSettingsId() : null);
			PredefinedSettingsVo predefinedSettingsVo = vamVendorGroupSettingsVo.getSettingsData();
			predefinedSettingsVo.setId(
					vamVendorGroupSettingsVo.getSettingsId() != null ? vamVendorGroupSettingsVo.getSettingsId() : null);
			predefinedSettingsVo.setName(vamVendorGroupSettingsVo.getSettingsName());
			String settingsData = new String();
			ObjectMapper mapper = new ObjectMapper();
			try {
				settingsData = mapper.writeValueAsString(predefinedSettingsVo);
			} catch (JsonProcessingException e1) {
				throw new ApplicationException(e1.getMessage());
			}
			preparedStatement.setString(3, vamVendorGroupSettingsVo.getStatus());
			preparedStatement.setString(4, settingsData);
			preparedStatement.setInt(5, vamVendorGroupSettingsVo.getVendorGroupId());
			preparedStatement.executeUpdate();
			editSettingsForAllvendors(vamVendorGroupSettingsVo);
		} catch (Exception e) {
			logger.info("Error in  editVendorGroupSettings:", e);
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(null, preparedStatement, con);
		}

	}

	private void editSettingsForAllvendors(VamVendorGroupSettingsVo vamVendorGroupSettingsVo)
			throws ApplicationException {
		logger.info("Entry into editSettingsForAllvendors");
		List<Integer> vendors = getVendorsIdForUpdate(vamVendorGroupSettingsVo.getVendorGroupId());
		Connection con = null;
		PreparedStatement preparedStatement = null;
		for (Integer id : vendors) {
			try {
				con = getAccountsPayable();
				preparedStatement = con.prepareStatement(VendorConstants.UPDATE_VENDOR_SETTINGS_BY_ID);
				preparedStatement.setString(1, vamVendorGroupSettingsVo.getSettingsName());
				preparedStatement.setObject(2,
						vamVendorGroupSettingsVo.getSettingsId() != null ? vamVendorGroupSettingsVo.getSettingsId()
								: null);
				PredefinedSettingsVo predefinedSettingsVo = vamVendorGroupSettingsVo.getSettingsData();
				predefinedSettingsVo.setId(
						vamVendorGroupSettingsVo.getSettingsId() != null ? vamVendorGroupSettingsVo.getSettingsId()
								: null);
				predefinedSettingsVo.setName(vamVendorGroupSettingsVo.getSettingsName());
				String settingsData = new String();
				ObjectMapper mapper = new ObjectMapper();

				try {
					settingsData = mapper.writeValueAsString(predefinedSettingsVo);
				} catch (JsonProcessingException e1) {
					throw new ApplicationException(e1.getMessage());
				}
				preparedStatement.setString(3, vamVendorGroupSettingsVo.getStatus());
				preparedStatement.setString(4, null);
				preparedStatement.setString(5, settingsData);
				preparedStatement.setInt(6, id);
				preparedStatement.executeUpdate();
				String accessData = getVendorRoles(id);
				updateKeyContactsForTypeVendor(id, accessData, vamVendorGroupSettingsVo.getOrganizationId());
			} catch (Exception e) {
				logger.info("Error in  editSettingsForAllvendors:", e);
				throw new ApplicationException(e.getMessage());
			} finally {
				closeResources(null, preparedStatement, con);
			}

		}

	}

	private List<Integer> getVendorsIdForUpdate(Integer vendorGroupId) throws ApplicationException {
		logger.info("Entry into getVendorsIdForUpdate");
		List<Integer> vendors = new ArrayList<Integer>();
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			con = getAccountsPayable();
			preparedStatement = con.prepareStatement(VendorConstants.GET_VENDORS);
			preparedStatement.setInt(1, vendorGroupId);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				int id = rs.getInt(1);
				String settingsName = rs.getString(2);
				if (settingsName != null && !settingsName.isEmpty()) {
					vendors.add(id);
				}
			}
		} catch (Exception e) {
			logger.info("Error in  getVendorsIdForUpdate:", e);
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return vendors;
	}

	public void updateTokenInKeyContacts(String email, String valueToken) throws ApplicationException {
		logger.info("Entry into updateTokenInKeyContacts");
		Connection con = null;
		PreparedStatement preparedStatement = null;
		try {
			con = getUserMgmConnection();
			preparedStatement = con.prepareStatement(VendorSettingsConstants.UPDATE_TOKEN_KEY_CONTACTS);
			preparedStatement.setString(1, valueToken);
			preparedStatement.setString(2, email);
			preparedStatement.executeUpdate();
		} catch (Exception e) {
			logger.info("Error in  updateTokenInKeyContacts:", e);
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(null, preparedStatement, con);
		}
	}

}

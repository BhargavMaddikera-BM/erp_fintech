package com.blackstrawai.vendorsettings;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.common.BaseDao;
import com.blackstrawai.common.CommonConstants;
import com.blackstrawai.common.FinanceCommonDao;
import com.blackstrawai.keycontact.VendorConstants;
import com.blackstrawai.settings.SettingsAndPreferencesConstants;
import com.blackstrawai.vendorsettings.dropdowns.SettingsDropDownVo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mysql.jdbc.Statement;

@Repository
public class VendorSettingsDao extends BaseDao {

	private Logger logger = Logger.getLogger(VendorSettingsDao.class);

	@Autowired
	private FinanceCommonDao financeCommonDao;

	public PredefinedSettingsVo getPredefinedSettingsFields(Integer orgId) throws ApplicationException {
		logger.info("To get getPredefinedSettingsFields");
		PredefinedSettingsVo predefinedSettingsVo = new PredefinedSettingsVo();
		predefinedSettingsVo.setOrganizationId(orgId);
		predefinedSettingsVo.setModules(financeCommonDao.getPredefinedSettingModules());
		List<SettingsTemplateVo> templates = financeCommonDao.getPredefinedSettingTemplates();
		Map<String, List<SettingsTemplateVo>> templateMap = templates.stream()
				.collect(Collectors.groupingBy(SettingsTemplateVo::getTemplateType));
		for (Map.Entry<String, List<SettingsTemplateVo>> entry : templateMap.entrySet()) {
			entry.getValue().get(0).setIsActive(true);
		}
		predefinedSettingsVo.setTemplate(templateMap);
		predefinedSettingsVo.setValidation(financeCommonDao.getPredefinedSettingValidation());
		logger.info("getPredefinedSettingsFields Retrieved");
		return predefinedSettingsVo;
	}

	public List<GeneralSettingsVo> getGeneralSettingsForOrganization(Integer orgId) throws ApplicationException {
		logger.info("To getGeneralSettingsForOrganization::");
		List<GeneralSettingsVo> generalSettings = new ArrayList<GeneralSettingsVo>();
		Connection con =null;
		PreparedStatement preparedStatement = null;
		ResultSet rs =null;
		try  {
			con = getUserMgmConnection();
			preparedStatement = con.prepareStatement(VendorSettingsConstants.GET_GENERAL_SETTINGS_FOR_ORG_ID);
			preparedStatement.setInt(1, orgId);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				GeneralSettingsVo settingsVo = new GeneralSettingsVo();
				settingsVo.setId(rs.getInt(1));
				settingsVo.setBaseId(rs.getInt(2));
				settingsVo.setName(rs.getString(3));
				settingsVo.setIsActive(rs.getBoolean(4));
				settingsVo.setOrganizationId(rs.getInt(5));
				settingsVo.setUserId(String.valueOf(rs.getInt(6)));
				settingsVo.setIsSuperAdmin(rs.getBoolean(7));
				generalSettings.add(settingsVo);
			}
			logger.info("getGeneralSettingsForOrganization retrieved::" + generalSettings.size());
		} catch (Exception e) {
			logger.info("Error in getGeneralSettingsForOrganization::", e);
			throw new ApplicationException(e.getMessage());
		}finally{
			closeResources(rs, preparedStatement, con);
		}
		return generalSettings;
	}

	public void activateOrDeactivateGeneralSettings(Integer generalSettingId, Boolean isActive)
			throws ApplicationException {
		logger.info("To activateOrDeactivateGeneralSettings for::" + generalSettingId + "value" + isActive);
		Connection con =null;
		PreparedStatement preparedStatement =null;
		try {
			con = getUserMgmConnection();
			preparedStatement = con.prepareStatement(VendorSettingsConstants.ACTIVATE_OR_DEACTIVATE_GENERAL_SETTINGS);
			preparedStatement.setBoolean(1, isActive);
			preparedStatement.setInt(2, generalSettingId);
			preparedStatement.executeUpdate();
			logger.info("activateOrDeactivateGeneralSettings updated::");
		} catch (Exception e) {
			logger.info("Error in activateOrDeactivateGeneralSettings::", e);
			throw new ApplicationException(e.getMessage());
		}finally{
			closeResources(null, preparedStatement, con);
		}
	}

	public void createPredefinedSetting(PredefinedSettingsVo settingsVo) throws ApplicationException {
		if (settingsVo.getName() != null) {
			logger.info("To create settings");
			boolean isExist = checkSettingsExist(settingsVo.getName(), settingsVo.getOrganizationId());
			if (isExist) {
				throw new ApplicationException("Settings name already exist for the Organization");
			}
			Connection con = null;
			PreparedStatement preparedStatement =null;
			ResultSet rs = null;
			try {
				con = getUserMgmConnection();
				preparedStatement = con.prepareStatement(VendorSettingsConstants.CREATE_PREDEFINED_SETTINGS, Statement.RETURN_GENERATED_KEYS); 
				preparedStatement.setString(1, settingsVo.getName());
				if (isDefaultSettingAvailable(settingsVo.getOrganizationId())) {
					preparedStatement.setBoolean(2, false);
				} else {
					preparedStatement.setBoolean(2, true);
				}
				preparedStatement.setString(3, CommonConstants.STATUS_AS_ACTIVE);
				preparedStatement.setInt(4, Integer.valueOf(settingsVo.getUserId()));
				preparedStatement.setInt(5, settingsVo.getOrganizationId());
				preparedStatement.setBoolean(6, settingsVo.getIsSuperAdmin());
				int rowAffected = preparedStatement.executeUpdate();
				if (rowAffected == 1) {
					rs = preparedStatement.getGeneratedKeys();
					while (rs.next()) {
						settingsVo.setId(rs.getInt(1));
					}
				}
				if (settingsVo.getId() != null) {
					createModulesForSettings(settingsVo, con);
					createTemplatesForSettings(settingsVo, con);
					createValidationsForSettings(settingsVo, con);
				}
				logger.info("Created settings Successfully");

			} catch (Exception e) {
				logger.info("Error in createPredefinedSetting::", e);
				throw new ApplicationException(e.getMessage());
			}finally{
				closeResources(rs, preparedStatement, con);
			}
		} else {
			throw new ApplicationException("Settings name is mandatory");
		}

	}

	private boolean checkSettingsExist(String name, Integer organizationId) throws ApplicationException {
		logger.info("To checkSettingsExist");
		Connection con =null;
		PreparedStatement preparedStatement =null;
		ResultSet rs =null;
		try {
			con = getUserMgmConnection();
			preparedStatement = con.prepareStatement(VendorSettingsConstants.CHECK_PREDEFINED_SETTINGS);
			preparedStatement.setInt(1, organizationId);
			preparedStatement.setString(2, name);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				return true;
			}
		} catch (Exception e) {
			logger.info("Error in checkSettingsExist::", e);
			throw new ApplicationException(e.getMessage());
		}finally{
			closeResources(rs, preparedStatement, con);
		}
		return false;
	}

	private void createValidationsForSettings(PredefinedSettingsVo settingsVo, Connection con)
			throws ApplicationException {
		logger.info("To createValidationsForSettings");
		if (settingsVo.getId() != null && settingsVo.getValidation() != null && settingsVo.getValidation().size() > 0) {
			PreparedStatement preparedStatement =null;
			for (SettingsValidationVo validation : settingsVo.getValidation()) {
				if (validation.getBaseId() != null) {
					try {
						preparedStatement = con.prepareStatement(VendorSettingsConstants.CREATE_VALIDATION_FOR_SETTINGS); 
						preparedStatement.setInt(1, validation.getBaseId());
						preparedStatement.setInt(2, settingsVo.getId());
						preparedStatement.setBoolean(3, validation.getIsActive());
						preparedStatement.setString(4, CommonConstants.STATUS_AS_ACTIVE);
						preparedStatement.setInt(5, Integer.valueOf(settingsVo.getUserId()));
						preparedStatement.setInt(6, settingsVo.getOrganizationId());
						preparedStatement.setBoolean(7, settingsVo.getIsSuperAdmin());
						preparedStatement.executeUpdate();
						logger.info(" created ValidationsForSettings successfully");

					} catch (Exception e) {
						logger.info("Error in createValidationsForSettings::", e);
						throw new ApplicationException(e.getMessage());
					}finally{
						closeResources(null, preparedStatement, null);
					}
				}
			}
		}
	}

	private void createTemplatesForSettings(PredefinedSettingsVo settingsVo, Connection con)
			throws ApplicationException {
		logger.info("To createTemplatesForSettings");
		if (settingsVo.getId() != null && settingsVo.getTemplate() != null && settingsVo.getTemplate().size() > 0) {
			PreparedStatement preparedStatement = null;
			for (Map.Entry<String, List<SettingsTemplateVo>> entry : settingsVo.getTemplate().entrySet()) {
				for (SettingsTemplateVo template : entry.getValue()) {
					if (template.getBaseId() != null) {
						try {
							preparedStatement = con.prepareStatement(VendorSettingsConstants.CREATE_TEMPLATES_FOR_SETTINGS);
							preparedStatement.setInt(1, template.getBaseId());
							preparedStatement.setInt(2, settingsVo.getId());
							preparedStatement.setBoolean(3, template.getIsActive());
							preparedStatement.setString(4, CommonConstants.STATUS_AS_ACTIVE);
							preparedStatement.setInt(5, Integer.valueOf(settingsVo.getUserId()));
							preparedStatement.setInt(6, settingsVo.getOrganizationId());
							preparedStatement.setBoolean(7, settingsVo.getIsSuperAdmin());
							preparedStatement.executeUpdate();
							logger.info("Successfully  created TemplatesForSettings");

						} catch (Exception e) {
							logger.info("Error in createTemplatesForSettings::", e);
							throw new ApplicationException(e.getMessage());
						}finally{
							closeResources(null, preparedStatement, null);
						}
					}
				}
			}
		}

	}

	private void createModulesForSettings(PredefinedSettingsVo settingsVo, Connection con) throws ApplicationException {
		logger.info("To createModulesForSettings");
		if (settingsVo.getId() != null && settingsVo.getModules() != null && settingsVo.getModules().size() > 0) {
			PreparedStatement preparedStatement=null;
			for (SettingsModuleVo module : settingsVo.getModules()) {
				if (module.getBaseId() != null) {
					try {
						preparedStatement = con.prepareStatement(VendorSettingsConstants.CREATE_MODULES_FOR_SETTINGS);
						preparedStatement.setInt(1, module.getBaseId());
						preparedStatement.setInt(2, settingsVo.getId());
						preparedStatement.setBoolean(3, module.getIsActive());
						preparedStatement.setString(4, CommonConstants.STATUS_AS_ACTIVE);
						preparedStatement.setInt(5, Integer.valueOf(settingsVo.getUserId()));
						preparedStatement.setInt(6, settingsVo.getOrganizationId());
						preparedStatement.setBoolean(7, settingsVo.getIsSuperAdmin());
						preparedStatement.executeUpdate();
						logger.info("Created  ModulesForSettings successfully");

					} catch (Exception e) {
						logger.info("Error in createModulesForSettings::", e);
						throw new ApplicationException(e.getMessage());
					}finally{
						closeResources(null, preparedStatement, null);
					}
				}
			}
		}

	}

	private String getDefaultSetting(Integer orgId) throws ApplicationException {
		String defaultSetting = null;
		Connection con =null;
		PreparedStatement preparedStatement = null;
		ResultSet rs =null;
		try {
			con = getUserMgmConnection();
			preparedStatement = con.prepareStatement(VendorSettingsConstants.GET_DEFAULT_SETTING_FOR_ORGANIZATION);
			preparedStatement.setInt(1, orgId);
			preparedStatement.setBoolean(2, true);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				defaultSetting = rs.getString(1);
			}
			logger.info("The default setting is ::" + defaultSetting);
		} catch (Exception e) {
			logger.info("Error in getDefaultSetting::", e);
			throw new ApplicationException(e.getMessage());
		}finally{
			closeResources(rs, preparedStatement, con);
		}
		return defaultSetting;
	}

	private Boolean isDefaultSettingAvailable(Integer orgId) throws ApplicationException {
		Boolean isDefaultSettingAvailable = false;
		if (getDefaultSetting(orgId) != null) {
			isDefaultSettingAvailable = true;
		}
		logger.info("isDefaultSettingAvailable is ::" + isDefaultSettingAvailable);
		return isDefaultSettingAvailable;
	}

	public PredefinedSettingsVo getPredefinedSettingById(Integer settingId, Integer orgId) throws ApplicationException {
		PredefinedSettingsVo predefinedSettingsVo = null;
		logger.info("To  getPredefinedSettingById::" + settingId);
		Connection con =null;
		PreparedStatement preparedStatement =null;
		ResultSet rs =null;
		try {
			con = getUserMgmConnection();
			preparedStatement = con.prepareStatement(VendorSettingsConstants.GET_PREDEFINED_SETTINGS);
			preparedStatement.setInt(1, settingId);
			preparedStatement.setInt(2, orgId);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				predefinedSettingsVo = new PredefinedSettingsVo();
				predefinedSettingsVo.setId(rs.getInt(1));
				predefinedSettingsVo.setName(rs.getString(2));
				predefinedSettingsVo.setIsDefault(rs.getBoolean(3));
				predefinedSettingsVo.setStatus(rs.getString(4));
				predefinedSettingsVo.setUserId(String.valueOf(rs.getInt(5)));
				predefinedSettingsVo.setOrganizationId(rs.getInt(6));
				predefinedSettingsVo.setIsSuperAdmin(rs.getBoolean(7));
			}

			if (predefinedSettingsVo != null) {
				predefinedSettingsVo.setModules(getModulesBySettingId(settingId, con));
				predefinedSettingsVo.setValidation(getValidationBySettingId(settingId, con));
				List<SettingsTemplateVo> templates = getTemplatesBySettingId(settingId, con);
				predefinedSettingsVo.setTemplate(
						templates.stream().collect(Collectors.groupingBy(SettingsTemplateVo::getTemplateType)));
			}
		} catch (Exception e) {
			logger.info("Error in getPredefinedSettingById::", e);
			throw new ApplicationException(e.getMessage());
		}finally{
			closeResources(rs, preparedStatement, con);
		}

		return predefinedSettingsVo;
	}

	private List<SettingsModuleVo> getModulesBySettingId(Integer settingId, Connection con)
			throws ApplicationException {
		List<SettingsModuleVo> moduleVos = null;
		logger.info("To  getModulesBySettingId::" + settingId);
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			preparedStatement = con.prepareStatement(VendorSettingsConstants.GET_MODULES_FOR_SETTINGS);
			moduleVos = new ArrayList<SettingsModuleVo>();
			preparedStatement.setInt(1, settingId);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				SettingsModuleVo moduleVo = new SettingsModuleVo();
				moduleVo.setId(rs.getInt(1));
				moduleVo.setBaseId(rs.getInt(2));
				moduleVo.setModule(rs.getString(3));
				moduleVo.setIsActive(rs.getBoolean(4));
				moduleVos.add(moduleVo);
			}
			logger.info("Modules retrieved succeessfully::" + moduleVos.size());
		} catch (Exception e) {
			logger.info("Error in getModulesBySettingId::", e);
			throw new ApplicationException(e.getMessage());
		}finally{
			closeResources(rs, preparedStatement, null);
		}

		return moduleVos;

	}

	private List<SettingsTemplateVo> getTemplatesBySettingId(Integer settingId, Connection con)
			throws ApplicationException {
		List<SettingsTemplateVo> templateVos = null;
		logger.info("To  getTemplatesBySettingId::" + settingId);
		PreparedStatement preparedStatement =null;
		ResultSet rs =null;
		try {
			preparedStatement = con.prepareStatement(VendorSettingsConstants.GET_TEMPLATES_FOR_SETTINGS);
			preparedStatement.setInt(1, settingId);
			templateVos = new ArrayList<SettingsTemplateVo>();
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				SettingsTemplateVo templateVo = new SettingsTemplateVo();
				templateVo.setId(rs.getInt(1));
				templateVo.setBaseId(rs.getInt(2));
				templateVo.setTemplateType(rs.getString(3));
				templateVo.setTemplateName(rs.getString(4));
				templateVo.setIsActive(rs.getBoolean(5));
				templateVos.add(templateVo);
			}
			logger.info("Templates retrieved succeessfully::" + templateVos.size());
		} catch (Exception e) {
			logger.info("Error in getTemplatesBySettingId::", e);
			throw new ApplicationException(e.getMessage());
		}finally{
			closeResources(rs, preparedStatement, null);
		}
		return templateVos;
	}

	private List<SettingsValidationVo> getValidationBySettingId(Integer settingId, Connection con)
			throws ApplicationException {
		List<SettingsValidationVo> validationVos = null;
		logger.info("To  getValidationBySettingId::" + settingId);
		PreparedStatement preparedStatement = null;
		ResultSet rs =null;
		try {
			preparedStatement = con.prepareStatement(VendorSettingsConstants.GET_VALIDATION_FOR_SETTINGS);
			preparedStatement.setInt(1, settingId);
			validationVos = new ArrayList<SettingsValidationVo>();
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				SettingsValidationVo validationVo = new SettingsValidationVo();
				validationVo.setId(rs.getInt(1));
				validationVo.setBaseId(rs.getInt(2));
				validationVo.setActivity(rs.getString(3));
				validationVo.setValidationRule(rs.getString(4));
				validationVo.setIsActive(rs.getBoolean(5));
				validationVos.add(validationVo);
			}
			logger.info("Validations retrieved succeessfully::" + validationVos.size());
		} catch (Exception e) {
			logger.info("Error in getValidationBySettingId::", e);
			throw new ApplicationException(e.getMessage());
		}finally{
			closeResources(rs, preparedStatement, null);
		}
		return validationVos;
	}

	public List<PredefinedSettingsVo> getPredefinedSettingsList(Integer orgId) throws ApplicationException {
		List<PredefinedSettingsVo> predefinedSettingsVos = null;
		logger.info("To get predefined settings list::");
		Connection connection =null;
		PreparedStatement preparedStatement = null;
		ResultSet rs =null;
		try {
			connection = getUserMgmConnection();
			preparedStatement = connection.prepareStatement(VendorSettingsConstants.GET_PREDEFINED_SETTINGS_LIST);
			preparedStatement.setInt(1, orgId);
			predefinedSettingsVos = new ArrayList<PredefinedSettingsVo>();
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				PredefinedSettingsVo settingsVo = new PredefinedSettingsVo();
				settingsVo.setId(rs.getInt(1));
				settingsVo.setName(rs.getString(2));
				settingsVo.setIsDefault(rs.getBoolean(3));
				settingsVo.setStatus(rs.getString(4));
				settingsVo.setUserId(String.valueOf(rs.getInt(5)));
				settingsVo.setOrganizationId(rs.getInt(6));
				settingsVo.setIsSuperAdmin(rs.getBoolean(7));
				predefinedSettingsVos.add(settingsVo);
			}
		} catch (Exception e) {
			logger.info("Error in getPredefinedSettingsList::", e);
			throw new ApplicationException(e.getMessage());
		}finally{
			closeResources(rs, preparedStatement, connection);
		}
		return predefinedSettingsVos;
	}

	public void updatePredefinedSettings(PredefinedSettingsVo settingsVo) throws ApplicationException {
		if (settingsVo.getId() != null) {
			logger.info("To update settings for id::" + settingsVo.getId());
			boolean isExist = checkSettingsExistForUpdate(settingsVo.getName(), settingsVo.getOrganizationId(),
					settingsVo.getId());
			if (isExist) {
				throw new ApplicationException("Settings name already exist for the Organization");
			}
			Connection con =null;
			PreparedStatement preparedStatement =null;
			try {
				con = getUserMgmConnection();
				preparedStatement = con.prepareStatement(VendorSettingsConstants.UPDATE_PREDEFINED_SETTINGS);
				preparedStatement.setString(1, settingsVo.getName());
				preparedStatement.setBoolean(2, settingsVo.getIsDefault());
				preparedStatement.setString(3, CommonConstants.STATUS_AS_ACTIVE);
				preparedStatement.setInt(4, Integer.valueOf(settingsVo.getUserId()));
				preparedStatement.setInt(5, settingsVo.getOrganizationId());
				preparedStatement.setBoolean(6, settingsVo.getIsSuperAdmin());
				preparedStatement.setDate(7, new Date(System.currentTimeMillis()));
				preparedStatement.setInt(8, settingsVo.getId());
				preparedStatement.executeUpdate();
				if (settingsVo.getId() != null) {
					updateModulesForSettings(settingsVo, con);
					updateTemplatesForSettings(settingsVo, con);
					updateValidationsForSettings(settingsVo, con);
				}
				logger.info("Updated settings Successfully");
				updateVendorAndVendorGroupSettings(settingsVo);

			} catch (Exception e) {
				logger.info("Error in updatePredefinedSettings::", e);
				throw new ApplicationException(e.getMessage());
			}finally{
				closeResources(null, preparedStatement, con);
			}
		} else {
			throw new ApplicationException("Settings name is mandatory");
		}
	}

	private void updateVendorAndVendorGroupSettings(PredefinedSettingsVo settingsVo) throws ApplicationException {
		logger.info("To updateVendorAndVendorGroupSettings");
		if (settingsVo != null) {
			updateVendorSettings(settingsVo);
			updateVendorGroupSettings(settingsVo);
		}
	}

	private void updateVendorGroupSettings(PredefinedSettingsVo settingsVo) throws ApplicationException {
		logger.info("To updateVendorAndVendorGroupSettings");
		String settingsData;
		ObjectMapper mapper = new ObjectMapper();
		try {
			settingsData = mapper.writeValueAsString(settingsVo);
		} catch (JsonProcessingException e1) {
			throw new ApplicationException(e1.getMessage());
		}
		Connection con =null;
		PreparedStatement preparedStatement =null;
		try {
			con = getUserMgmConnection();
			preparedStatement = con.prepareStatement(SettingsAndPreferencesConstants.UPDATE_VENDOR_GROUP_SETTINGS);
			preparedStatement.setString(1, settingsData);
			preparedStatement.setString(2, settingsVo.getName());
			preparedStatement.setInt(3, settingsVo.getId());
			preparedStatement.executeUpdate();
		} catch (Exception e) {
			logger.info("Error in updateVendorAndVendorGroupSettings::", e);
			throw new ApplicationException(e.getMessage());
		}finally{
			closeResources(null, preparedStatement, con);
		}

	}

	private void updateVendorSettings(PredefinedSettingsVo settingsVo) throws ApplicationException {
		logger.info("To updateVendorAndVendorGroupSettings");
		String settingsData;
		ObjectMapper mapper = new ObjectMapper();
		try {
			settingsData = mapper.writeValueAsString(settingsVo);
		} catch (JsonProcessingException e1) {
			throw new ApplicationException(e1.getMessage());
		}
		Connection con = null;
		PreparedStatement preparedStatement = null;
		try {
			con = getAccountsPayable();
			preparedStatement = con.prepareStatement(VendorConstants.UPDATE_VENDOR_SETTINGS_BY_PREDEFINED_SETTINGS); 
			preparedStatement.setString(1, settingsData);
			preparedStatement.setString(2, settingsVo.getName());
			preparedStatement.setInt(3, settingsVo.getId());
			preparedStatement.executeUpdate();
		} catch (Exception e) {
			logger.info("Error in updateVendorSettings::", e);
			throw new ApplicationException(e.getMessage());
		}finally{
			closeResources(null, preparedStatement, con);
		}
	}

	private boolean checkSettingsExistForUpdate(String name, Integer organizationId, Integer id)
			throws ApplicationException {
		logger.info("To checkSettingsExistForUpdate");
		Connection con =null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			con = getUserMgmConnection();
			preparedStatement = con.prepareStatement(VendorSettingsConstants.CHECK_PREDEFINED_SETTINGS_FOR_UPDATE);
			preparedStatement.setInt(1, organizationId);
			preparedStatement.setString(2, name);
			preparedStatement.setInt(3, id);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				return true;
			}
		} catch (Exception e) {
			logger.info("Error in checkSettingsExistForUpdate::", e);
			throw new ApplicationException(e.getMessage());
		}finally{
			closeResources(rs, preparedStatement, con);
		}
		return false;
	}

	private void updateValidationsForSettings(PredefinedSettingsVo settingsVo, Connection con)
			throws ApplicationException {
		logger.info("To updateValidationsForSettings");
		if (settingsVo.getId() != null && settingsVo.getValidation() != null && settingsVo.getValidation().size() > 0) {
			PreparedStatement preparedStatement =null;
			for (SettingsValidationVo validation : settingsVo.getValidation()) {
				if (validation.getBaseId() != null && validation.getId() != null) {
					try{
						preparedStatement = con.prepareStatement(VendorSettingsConstants.UPDATE_VALIDATION_FOR_SETTINGS); 
						preparedStatement.setBoolean(1, validation.getIsActive());
						preparedStatement.setInt(2, Integer.valueOf(settingsVo.getUserId()));
						preparedStatement.setInt(3, settingsVo.getOrganizationId());
						preparedStatement.setBoolean(4, settingsVo.getIsSuperAdmin());
						preparedStatement.setDate(5, new Date(System.currentTimeMillis()));
						preparedStatement.setInt(6, validation.getId());
						preparedStatement.executeUpdate();
						logger.info(" updated ValidationsForSettings successfully");

					} catch (Exception e) {
						logger.info("Error in updateValidationsForSettings::", e);
						throw new ApplicationException(e.getMessage());
					}finally{
						closeResources(null, preparedStatement, null);
					}
				}
			}
		}

	}

	private void updateTemplatesForSettings(PredefinedSettingsVo settingsVo, Connection con)
			throws ApplicationException {
		logger.info("To updateTemplatesForSettings");
		if (settingsVo.getId() != null && settingsVo.getTemplate() != null && settingsVo.getTemplate().size() > 0) {
			PreparedStatement preparedStatement = null;
			for (Map.Entry<String, List<SettingsTemplateVo>> entry : settingsVo.getTemplate().entrySet()) {
				for (SettingsTemplateVo template : entry.getValue()) {
					if (template.getBaseId() != null && template.getId() != null) {
						try {
							preparedStatement = con.prepareStatement(VendorSettingsConstants.UPDATE_TEMPLATE_FOR_SETTINGS);
							preparedStatement.setBoolean(1, template.getIsActive());
							preparedStatement.setInt(2, Integer.valueOf(settingsVo.getUserId()));
							preparedStatement.setInt(3, settingsVo.getOrganizationId());
							preparedStatement.setBoolean(4, settingsVo.getIsSuperAdmin());
							preparedStatement.setDate(5, new Date(System.currentTimeMillis()));
							preparedStatement.setInt(6, template.getId());
							preparedStatement.executeUpdate();
							logger.info("Successfully  updated TemplatesForSettings");

						} catch (Exception e) {
							logger.info("Error in updateTemplatesForSettings::", e);
							throw new ApplicationException(e.getMessage());
						}finally{
							closeResources(null, preparedStatement, null);
						}
					}
				}
			}
		}

	}

	private void updateModulesForSettings(PredefinedSettingsVo settingsVo, Connection con) throws ApplicationException {
		logger.info("To updateModulesForSettings");
		if (settingsVo.getId() != null && settingsVo.getModules() != null && settingsVo.getModules().size() > 0) {
			PreparedStatement preparedStatement =null;
			for (SettingsModuleVo module : settingsVo.getModules()) {
				if (module.getBaseId() != null && settingsVo.getId() != null) {
					try {
						preparedStatement = con.prepareStatement(VendorSettingsConstants.UPDATE_MODULES_FOR_SETTINGS);
						preparedStatement.setBoolean(1, module.getIsActive());
						preparedStatement.setInt(2, Integer.valueOf(settingsVo.getUserId()));
						preparedStatement.setInt(3, settingsVo.getOrganizationId());
						preparedStatement.setBoolean(4, settingsVo.getIsSuperAdmin());
						preparedStatement.setDate(5, new Date(System.currentTimeMillis()));
						preparedStatement.setInt(6, module.getId());
						preparedStatement.executeUpdate();
						logger.info("Updated  ModulesForSettings successfully" + preparedStatement.toString());

					} catch (Exception e) {
						logger.info("Error in updateModulesForSettings::", e);
						throw new ApplicationException(e.getMessage());
					}finally{
						closeResources(null, preparedStatement, null);
					}
				}
			}
		}

	}

	private void deselectAsDefault(Integer orgId, Connection con) throws ApplicationException {

		logger.info("To  deselectAsDefault::");
		String existingDefaultId = getDefaultSetting(orgId);
		PreparedStatement preparedStatement =null;
		try {
			preparedStatement = con.prepareStatement(VendorSettingsConstants.DESELECT_OR_SELECT_DEFAULT_SETTING);
			if (existingDefaultId != null) {
				preparedStatement.setBoolean(1, false);
				preparedStatement.setInt(2, orgId);
				preparedStatement.setInt(3, Integer.valueOf(existingDefaultId));
				preparedStatement.executeUpdate();
				logger.info("Successfully  deselectAsDefault::");
			}
		} catch (Exception e) {
			logger.info("Error in deselectAsDefault::", e);
			throw new ApplicationException(e.getMessage());
		}finally{
			closeResources(null, preparedStatement, null);
		}
	}

	public void selectAsDefault(Integer settingId, Integer orgId) throws ApplicationException {
		logger.info("To  selectAsDefault::");
		Connection con =null;
		PreparedStatement preparedStatement = null;
		try  {
			con = getUserMgmConnection();
			preparedStatement = con.prepareStatement(VendorSettingsConstants.DESELECT_OR_SELECT_DEFAULT_SETTING);
			deselectAsDefault(orgId, con);
			preparedStatement.setBoolean(1, true);
			preparedStatement.setInt(2, orgId);
			preparedStatement.setInt(3, settingId);
			preparedStatement.executeUpdate();
			logger.info("Successfully  selectAsDefault::");
		} catch (Exception e) {
			logger.info("Error in selectAsDefault::", e);
			throw new ApplicationException(e.getMessage());
		}finally{
			closeResources(null, preparedStatement, con);
		}
	}

	public void deActivatePredefinedSettings(Integer settingId, Integer orgId) throws ApplicationException {
		logger.info("To  deActivatePredefinedSettings::");
		Connection con =null;
		PreparedStatement preparedStatement = null;
		try {
			con = getUserMgmConnection();
			preparedStatement = con.prepareStatement(VendorSettingsConstants.DEACTIVATE_PREDEFINED_SETTINGS);
			preparedStatement.setString(1, CommonConstants.STATUS_AS_INACTIVE);
			preparedStatement.setInt(2, orgId);
			preparedStatement.setInt(3, settingId);
			preparedStatement.executeUpdate();
			logger.info("Successfully  deActivated PredefinedSettings::");
		} catch (Exception e) {
			logger.info("Error in deActivatePredefinedSettings::", e);
			throw new ApplicationException(e.getMessage());
		}finally{
			closeResources(null, preparedStatement, con);
		}
	}

	public List<SettingsDropDownVo> getSettingsNameOfAnOrganization(int organizationId, Connection con)
			throws ApplicationException {
		logger.info("Entry into getSettingsNameOfAnOrganization");
		List<SettingsDropDownVo> settingsDetails = new ArrayList<SettingsDropDownVo>();
		PreparedStatement preparedStatement =null;
		ResultSet rs =null;
		try {
			preparedStatement = con.prepareStatement(VendorSettingsConstants.GET_SETTINGS_OF_AN_ORGANIZATION);
			preparedStatement.setInt(1, organizationId);
			logger.info("PreparedStment::" + preparedStatement.toString());
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				SettingsDropDownVo settingsVo = new SettingsDropDownVo();
				settingsVo.setId(rs.getInt(1));
				settingsVo.setSettingName(rs.getString(2));
				settingsDetails.add(settingsVo);
			}
		} catch (Exception e) {
			logger.info("Error in  getSettingsNameOfAnOrganization:", e);
			throw new ApplicationException(e);
		}finally{
			closeResources(rs, preparedStatement, null);
		}
		return settingsDetails;
	}
}
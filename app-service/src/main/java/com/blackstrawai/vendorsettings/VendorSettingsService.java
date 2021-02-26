package com.blackstrawai.vendorsettings;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.common.BaseService;

@Service
public class VendorSettingsService extends BaseService{

	private Logger logger = Logger.getLogger(VendorSettingsService.class);

	
	@Autowired
	private VendorSettingsDao vendorSettingsDao;
	
	public List<GeneralSettingsVo> getGeneralSettings(Integer orgId) throws ApplicationException{
		return vendorSettingsDao.getGeneralSettingsForOrganization(orgId);
	}
	
	public void activateDeactivateGeneralSettings(Integer genSettingId , Boolean isActive) throws ApplicationException {
		vendorSettingsDao.activateOrDeactivateGeneralSettings(genSettingId, isActive);
	}
	
	public PredefinedSettingsVo getPredefinedSettingsDefaultValues(Integer orgId) throws ApplicationException {
		return vendorSettingsDao.getPredefinedSettingsFields(orgId);
	}

	public void createPredefinedSetting(PredefinedSettingsVo settingsVo) throws ApplicationException {
		logger.info("To create Setting in create setting" + settingsVo);
		vendorSettingsDao.createPredefinedSetting(settingsVo);
		logger.info("Successfully created in service layer " + settingsVo);

	}
	
	public PredefinedSettingsVo getPredefinedSettingById(Integer settingsId,Integer orgId) throws ApplicationException {
		logger.info("To get the Predefined Settingsby id " + settingsId);
		return vendorSettingsDao.getPredefinedSettingById(settingsId, orgId);
	}
	
	public List<PredefinedSettingsVo> getPredefinedSettingsList(Integer orgId) throws ApplicationException{
		return vendorSettingsDao.getPredefinedSettingsList(orgId);
	}

	public void updatePredefinedSetting(PredefinedSettingsVo settingsVo) throws ApplicationException {
		logger.info("To update Setting in create setting" + settingsVo);
		vendorSettingsDao.updatePredefinedSettings(settingsVo);
		logger.info("Successfully updated in service layer " + settingsVo);
		
	}
	
	public void setAsDefaultForPredefinedSettings(Integer settingId , Integer orgId) throws ApplicationException {
		vendorSettingsDao.selectAsDefault(settingId, orgId);
	}
	
	public void deactivatePredefinedSettings(Integer settingId , Integer orgId) throws ApplicationException {
		vendorSettingsDao.deActivatePredefinedSettings(settingId, orgId);
	}
}

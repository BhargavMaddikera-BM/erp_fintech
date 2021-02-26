package com.blackstrawai.vendorsettings;

import java.io.FileReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.common.BaseService;
import com.blackstrawai.common.DropDownDao;
import com.blackstrawai.onboarding.EmailThread;
import com.blackstrawai.onboarding.OrganizationDao;
import com.blackstrawai.vendorsettings.dropdowns.VamDropDownVo;

@Service
public class VendorAccessMgmtService extends BaseService {

	private Logger logger = Logger.getLogger(VendorAccessMgmtService.class);

	@Autowired
	private DropDownDao dropDownDao;

	@Autowired
	private VendorAccessMgmtDao vendorAccessMgmtDao;
	
	@Autowired
	OrganizationDao organizationDao;

	public VamDropDownVo getVendorAccessMgmtDropDown(int organizationId) throws ApplicationException {
		logger.info("Entry into method: getVendorAccessMgmtDropDown");
		return dropDownDao.getVendorAccessMgmtDropDown(organizationId);
	}

	public void createVendorSettings(VamVendorVo vamVendorVo) throws ApplicationException {
		logger.info("Entry into method: createVendorSettings");
		boolean status = vendorAccessMgmtDao.createVendorSettings(vamVendorVo);
		VendorDetailsVo vendorVo = vendorAccessMgmtDao.getVendorDetails(vamVendorVo.getVendorId());
		String url;
		FileReader reader;
		try {
			reader = new FileReader("/decifer/config/app_config.properties");
			Properties p = new Properties();
			p.load(reader);
			url = p.getProperty("decifer_url");
		} catch (Exception e) {
			url = "https://decifer.blackstrawlab.com";
		}

		EmailThread emailThread;
		if (status) {
			String organizationName = organizationDao.getOrganizationName(vamVendorVo.getOrgId());
			String message = "Hi " + vendorVo.getVendorDisplayName()
					+ ",\r\n\r\n  You are invited to Join Organization-" + organizationName + " as a Vendor.  Click on "
					+ url + " to proceed.\r\n\r\n Regards,\r\n Decifer Admin";
			emailThread = new EmailThread(message, vendorVo.getEmail(), "Decifer:Invite to Join Organization");
		} else {
			String keyToken = new Long(System.currentTimeMillis()).toString();
			String valueToken = generateHashToken(keyToken.concat(vendorVo.getEmail()));
			vendorAccessMgmtDao.updateTokenInKeyContacts(vendorVo.getEmail(), valueToken);
			String message = "Hi " + vendorVo.getVendorDisplayName()
					+ ",\r\n\r\n  Your Profile is Registered Successfully. Please Click on " + url
					+ "/auth/create-password/" + valueToken
					+ " to Create New Password.\r\n\r\n Regards,\r\n Decifer Admin";
			emailThread = new EmailThread(message, vendorVo.getEmail(), "Decifer:User Registration");
		}
		emailThread.start();
	}

	public void createVendorGroupSettings(VamVendorGroupVo vamVendorGroupVo) throws ApplicationException {
		logger.info("Entry into method: createVendorGroupSettings");
		Map<VamVendorVo, Boolean> vendorList = new HashMap<VamVendorVo, Boolean>();
		vendorList = vendorAccessMgmtDao.createVendorGroupSettings(vamVendorGroupVo);
		if (vendorList.size() > 0) {
			String url;
			FileReader reader;
			try {
				reader = new FileReader("/decifer/config/app_config.properties");
				Properties p = new Properties();
				p.load(reader);
				url = p.getProperty("decifer_url");
			} catch (Exception e) {
				url = "https://decifer.blackstrawlab.com";
			}

			EmailThread emailThread;
			for (Map.Entry<VamVendorVo, Boolean> vendor : vendorList.entrySet()) {
				VendorDetailsVo vendorVo = vendorAccessMgmtDao.getVendorDetails(vendor.getKey().getVendorId());
				if (vendor.getValue()) {
					String organizationName = organizationDao.getOrganizationName(vendor.getKey().getOrgId());
					String message = "Hi " + vendorVo.getVendorDisplayName()
							+ ",\r\n\r\n  You are invited to Join Organization-" + organizationName
							+ " as a Vendor.  Click on " + url + " to proceed.\r\n\r\n Regards,\r\n Decifer Admin";
					emailThread = new EmailThread(message, vendorVo.getEmail(), "Decifer:Invite to Join Organization");
				} else {
					String keyToken = new Long(System.currentTimeMillis()).toString();
					String valueToken = generateHashToken(keyToken.concat(vendorVo.getEmail()));
					vendorAccessMgmtDao.updateTokenInKeyContacts(vendorVo.getEmail(), valueToken);
					String message = "Hi " + vendorVo.getVendorDisplayName()
							+ ",\r\n\r\n  Your Profile is Registered Successfully. Please Click on " + url
							+ "/auth/create-password/" + valueToken
							+ " to Create New Password.\r\n\r\n Regards,\r\n Decifer Admin";
					emailThread = new EmailThread(message, vendorVo.getEmail(), "Decifer:User Registration");
				}
				emailThread.start();
			}
		}
	}

	public List<VamBasicVendorVo> getVendorSettings(int organizationId) throws ApplicationException {
		logger.info("Entry into method: getVendorSettings");
		return vendorAccessMgmtDao.getVendorSettings(organizationId);
	}

	public List<VamBasicVendorGroupVo> getVendorGroupSettings(int organizationId) throws ApplicationException {
		logger.info("Entry into method: getVendorGroupSettings");
		return vendorAccessMgmtDao.getVendorGroupSettings(organizationId);
	}

	public void ActiveAndDeactiveVendorSettings(int vendorId, String status) throws ApplicationException {
		logger.info("Entry into method: getVendorGroupSettings");
		vendorAccessMgmtDao.ActiveAndDeactiveVendorSettings(vendorId, status);
	}

	public void ActiveAndDeactiveVendorGroupSettings(int vendorGroupId, String status) throws ApplicationException {
		logger.info("Entry into method: ActiveAndDeactiveVendorGroupSettings");
		vendorAccessMgmtDao.ActiveAndDeactiveVendorGroupSettings(vendorGroupId, status);
	}

	public VamVendorSettingsVo getVendorSettingsById(int vendorId) throws ApplicationException {
		logger.info("Entry into method: getVendorSettingsById");
		return vendorAccessMgmtDao.getVendorSettingsById(vendorId);
	}

	public VamVendorGroupSettingsVo getVendorGroupSettingsById(int organizationId, int vendorGroupId)
			throws ApplicationException {
		logger.info("Entry into method: getVendorGroupSettingsById");
		return vendorAccessMgmtDao.getVendorGroupSettingsById(organizationId, vendorGroupId);
	}

	public void editVendorSettings(VamVendorSettingsVo vamVendorSettingsVo) throws ApplicationException {
		logger.info("Entry into method: editVendorSettings");
		vendorAccessMgmtDao.editVendorSettings(vamVendorSettingsVo);

	}

	public void editVendorGroupSettings(VamVendorGroupSettingsVo vamVendorGroupSettingsVo) throws ApplicationException {
		logger.info("Entry into method: editVendorGroupSettings");
		vendorAccessMgmtDao.editVendorGroupSettings(vamVendorGroupSettingsVo);

	}

}

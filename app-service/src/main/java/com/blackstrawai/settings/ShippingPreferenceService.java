package com.blackstrawai.settings;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.common.BaseService;

@Service
public class ShippingPreferenceService extends BaseService {

	@Autowired
	ShippingPreferenceDao shippingPreferenceDao;

	private Logger logger = Logger.getLogger(ShippingPreferenceService.class);

	public ShippingPreferenceVo createShippingPreference(ShippingPreferenceVo shippingPreferenceVo)
			throws ApplicationException {
		logger.info("Entry into method:createShippingPreference");
		shippingPreferenceDao.createShippingPreference(shippingPreferenceVo);
		return shippingPreferenceVo;
	}

	/*public List<ShippingPreferenceVo> getAllShippingPreferencesOfAnOrganization(int organizationId)
			throws ApplicationException {
		logger.info("Entry into method:getAllShippingPreferencesOfAnOrganization");
		return shippingPreferenceDao.getAllShippingPreferencesOfAnOrganization(organizationId);
	}*/
	
	public List<ShippingPreferenceVo> getAllShippingPreferencesOfAnOrganizationForUserAndRole(int organizationId,String userId,String roleName)
			throws ApplicationException {
		logger.info("Entry into method:getAllShippingPreferencesOfAnOrganization");
		return shippingPreferenceDao.getAllShippingPreferencesOfAnOrganizationForUserAndRole(organizationId,userId,roleName);
	}


	public ShippingPreferenceVo getShippingPreferenceById(int id) throws ApplicationException {
		logger.info("Entry into method:getShippingPreferenceById");
		return shippingPreferenceDao.getShippingPreferenceById(id);
	}

	public ShippingPreferenceVo deleteShippingPreference(int id,String status,String userId,String roleName) throws ApplicationException {
		logger.info("Entry into method:deleteShippingPreference");
		return shippingPreferenceDao.deleteShippingPreference(id,status,userId,roleName);
	}

	public ShippingPreferenceVo updateShippingPreference(ShippingPreferenceVo shippingPreferenceVo) throws ApplicationException {
		logger.info("Entry into method:updateShippingPreference");
		return shippingPreferenceDao.updateShippingPreference(shippingPreferenceVo);
	}
	 

}

package com.blackstrawai.settings;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blackstrawai.ApplicationException;

@Service
public class ListPageCustomizationService {
	
	@Autowired
	ListPageCustomizationDao listPageCustomizationDao;

	private Logger logger = Logger.getLogger(ListPageCustomizationService.class);

	

	public ListPageCustomizationVo updateCustomization(ListPageCustomizationVo listPageCustomizationVo) throws ApplicationException {
		logger.info("Entry into method:updateCustomization");
		return listPageCustomizationDao.updateCustomization(listPageCustomizationVo);
	}
	

	public List<ListPageCustomizationVo> getAllCustomizationForUserAndRole(int organizationId,String userId,String roleName,String moduleName) throws ApplicationException {
		logger.info("Entry into method:getAllCustomizationForUserAndRole");
		return listPageCustomizationDao.getAllCustomizationForUserAndRole(organizationId,userId,roleName,moduleName);
	}

}

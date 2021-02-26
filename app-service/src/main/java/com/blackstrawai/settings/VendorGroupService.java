package com.blackstrawai.settings;

import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.common.BaseService;

@Service
public class VendorGroupService extends BaseService {

	@Autowired
	VendorGroupDao vendorGroupDao;
	
	private Logger logger = Logger.getLogger(VendorGroupService.class);
	
	public VendorGroupVo createVendorGroup(VendorGroupVo VendorGroupVo) throws ApplicationException {
		logger.info("Entry into method: createVendorGroup");
		VendorGroupVo=vendorGroupDao.createVendorGroup(VendorGroupVo);
		return VendorGroupVo;	
	}

	public List<VendorGroupVo> getAllVendorGroupOfAnOrganization(int organizationId) throws ApplicationException {
		logger.info("Entry into method: getAllVendorGroupOfAnOrganization");
		List<VendorGroupVo> listOfVendorGroup=vendorGroupDao.getAllVendorGroupOfAnOrganization(organizationId);
		Collections.reverse(listOfVendorGroup);
		return listOfVendorGroup;	
	}
	
	public List<VendorGroupVo> getAllVendorGroupOfAnOrganizationForUserAndRole(int organizationId,String userId,String roleName) throws ApplicationException {
		logger.info("Entry into method: getAllVendorGroupOfAnOrganizationForUserAndRole");
		List<VendorGroupVo> listOfVendorGroup=vendorGroupDao.getAllVendorGroupOfAnOrganizationForUserAndRole(organizationId,userId,roleName);
		Collections.reverse(listOfVendorGroup);
		return listOfVendorGroup;	
	}
	
	public VendorGroupVo getVendorGroupById(int id) throws ApplicationException {
		logger.info("Entry into method: getVendorGroupById");
		VendorGroupVo vendorGroupVo=vendorGroupDao.getVendorGroupById(id);
		return vendorGroupVo;	
	}
	public VendorGroupVo deleteVendorGroup(int id,String status,String userId,String roleName) throws ApplicationException {
		logger.info("Entry into method: deleteVendorGroup");
		VendorGroupVo vendorGroupVo=vendorGroupDao.deleteVendorGroup(id,status,userId,roleName);
		return vendorGroupVo;	
	}
	public VendorGroupVo updateVendorGroup(VendorGroupVo vendorGroupVo) throws ApplicationException {
		logger.info("Entry into method: updateVendorGroup");
		 vendorGroupVo=vendorGroupDao.updateVendorGroup(vendorGroupVo);
		return vendorGroupVo;	
	}

}


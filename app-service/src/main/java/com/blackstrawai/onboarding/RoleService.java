package com.blackstrawai.onboarding;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.common.BaseService;
import com.blackstrawai.onboarding.role.RoleVo;


@Service
public class RoleService extends BaseService{

	@Autowired
	RoleDao roleDao;
	
	private  Logger logger = Logger.getLogger(RoleService.class);
	
	
	public List<RoleVo> getAllRolesOfAnOrganization(int organizationId,String organizationName,String userId,String roleName)throws ApplicationException {	
		logger.info("Entry into method:getAllRolesOfAnOrganization");
		return roleDao.getAllRolesOfAnOrganization(organizationId,organizationName,userId,roleName);
	}	
	
	public RoleVo getRoleDetails(int organizationId,int  roleId)throws ApplicationException {	
		logger.info("Entry into method:getRoleDetails");
		return roleDao.getRoleDetails(organizationId, roleId);
	}


	public RoleVo createRole(RoleVo roleVo)throws ApplicationException {	
		logger.info("Entry into method:createRole");
		return roleDao.createRole(roleVo);
	}
	
	public RoleVo updateRole(RoleVo roleVo)throws ApplicationException {	
		logger.info("Entry into method:updateRole");
		return roleDao.updateRole(roleVo);
	}
	
	public RoleVo deleteRole(int id,String status,String userId,String roleName)throws ApplicationException {	
		logger.info("Entry into method:deleteRole");
		return roleDao.deleteRole(id,status,userId,roleName);
	}
	
}

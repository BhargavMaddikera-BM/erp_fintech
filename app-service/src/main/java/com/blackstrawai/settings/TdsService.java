package com.blackstrawai.settings;

import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blackstrawai.ApplicationException;

@Service
public class TdsService {
	
	@Autowired
	TdsDao tdsDao;

	private Logger logger = Logger.getLogger(TdsService.class);

	public TdsVo createTds(TdsVo tdsVo) throws ApplicationException {
		logger.info("Entry into method:createTds");
		return tdsDao.createTds(tdsVo);
	}

	
	public TdsVo deleteTds(int id,String status,String userId,String roleName) throws ApplicationException {
		logger.info("Entry into method:deleteTds");
		return tdsDao.deleteTds(id,status,userId,roleName);
	}

	public TdsVo updateTds(TdsVo tdsVo) throws ApplicationException {
		logger.info("Entry into method:updateTds");
		return tdsDao.updateTds(tdsVo);
	}

	public TdsVo getTdsById(int id,Boolean isBase,int organizationId,String userId,String roleName) throws ApplicationException {
		logger.info("Entry into method:getTdsById");
		return tdsDao.getTdsById(id,isBase,organizationId,userId,roleName);
	}

	public List<TdsVo> getAllTdsOfAnOrganizationForUserAndRole(int organizationId,String userId,String roleName) throws ApplicationException {
		logger.info("Entry into method:getAllTdsOfAnOrganizationForUserAndRole");
		List<TdsVo> tdsList= tdsDao.getAllTdsOfAnOrganizationForUserAndRole(organizationId,userId,roleName);
		Collections.reverse(tdsList);
		return tdsList;
	}

}

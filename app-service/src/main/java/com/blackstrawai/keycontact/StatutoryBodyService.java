package com.blackstrawai.keycontact;

import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.common.BaseService;
import com.blackstrawai.common.DropDownDao;
import com.blackstrawai.keycontact.dropdowns.StatutoryBodyDropDownVo;
import com.blackstrawai.keycontact.statutorybody.StatutoryBodyVo;

@Service
public class StatutoryBodyService extends BaseService{

	@Autowired
	StatutoryBodyDao statutoryBodyDao;
	
	@Autowired
	DropDownDao dropDownDao;
	
	private Logger logger = Logger.getLogger(StatutoryBodyService.class);
	
	public StatutoryBodyVo createStatutoryBody(StatutoryBodyVo statutoryBodyVo) throws ApplicationException {
		logger.info("Entry into createStatutoryBody Service Class");
		return statutoryBodyDao.createStatutoryBody(statutoryBodyVo);
	}

	public List<StatutoryBodyVo> getAllStatutoryBodiesOfAnOrganizationForUserAndRole(int organizationId,String userId,String roleName) throws ApplicationException {
		List<StatutoryBodyVo> listOfStatutoryBody = statutoryBodyDao.getAllStatutoryBodiesOfAnOrganizationForUserAndRole(organizationId,userId,roleName);
		Collections.reverse(listOfStatutoryBody);
		return listOfStatutoryBody;	
	}
	
	
	public StatutoryBodyVo getStatutoryBodyById(int statutoryBodyId) throws ApplicationException {
		logger.info("Entry into getStatutoryBodyById");
		return statutoryBodyDao.getStatutoryBodyById(statutoryBodyId);
	}
	
	
	public StatutoryBodyVo deleteStatutoryBody(int id,String userId,String roleName,String status) throws ApplicationException {
		logger.info("Entry into method:deactivateStatus");
		return statutoryBodyDao.deleteStatutoryBody(id,userId,roleName,status);
	}
	
	public StatutoryBodyDropDownVo getStatutoryBodyDropDownData(int organizationId)throws ApplicationException {
		logger.info("Entry into getStatutoryBodyDropDownData");
		return dropDownDao.getStatutoryBodyDropDownData(organizationId);
	}	
	
	
	public StatutoryBodyVo updateStatutoryBody(StatutoryBodyVo statutoryBodyVo) throws ApplicationException {
		logger.info("Entry into updateStatutoryBody Service Class");
		return statutoryBodyDao.updateStatutoryBody(statutoryBodyVo);
	}
	
}

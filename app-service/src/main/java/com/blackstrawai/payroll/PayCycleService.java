package com.blackstrawai.payroll;

import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.common.BaseService;

@Service
public class PayCycleService extends BaseService{
	
	@Autowired
	private PayCycleDao payCycleDao;

	private Logger logger = Logger.getLogger(PayCycleService.class);

	public PayCycleVo createPayCycle(PayCycleVo payCycleVo) throws ApplicationException {
		logger.info("Entry into mathod: createPayCycle");
		return payCycleDao.createPayCycle(payCycleVo);
	}

	public List<PayCycleVo> getAllPayCyclesOfAnOrganization(int organizationId, String userId, String roleName) throws ApplicationException {
		logger.info("Entry into mathod: getAllPayCyclesOfAnOrganization");
		List<PayCycleVo> cycleList = payCycleDao.getAllPayCyclesOfAnOrganization(organizationId, userId, roleName);
		Collections.reverse(cycleList);
		return cycleList;
	}

	public PayCycleVo getPayCycleById(int id) throws ApplicationException {
		logger.info("Entry into mathod: getPayCycleById");
		return payCycleDao.getPayCycleById(id);
	}

	public PayCycleVo deletePayCycle(int id, String status, String userId, String roleName, int organizationId) throws ApplicationException {
		logger.info("Entry into mathod: deletePayCycle");
		return payCycleDao.deletePayCycle(id, status, userId, roleName, organizationId);
	}

	public PayCycleVo updatePayCycle(PayCycleVo payCycleVo) throws ApplicationException {
		logger.info("Entry into mathod: updatePayCycle");
		return payCycleDao.updatePayCycle(payCycleVo);
	}

}

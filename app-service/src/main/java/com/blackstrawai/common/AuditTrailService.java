package com.blackstrawai.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuditTrailService extends BaseService{

	@Autowired
	AuditTrailDao auditTrailDao;
	
	public void insertIntoAuditTrail(AuditTrailVo auditTrailVo){
		auditTrailDao.insertIntoAuditTrail(auditTrailVo);
	}
	

}
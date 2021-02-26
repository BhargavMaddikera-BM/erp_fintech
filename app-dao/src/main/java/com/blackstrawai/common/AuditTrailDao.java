package com.blackstrawai.common;

import org.springframework.stereotype.Repository;

import com.blackstrawai.common.BaseDao;

@Repository
public class AuditTrailDao extends BaseDao {

	public void insertIntoAuditTrail(AuditTrailVo auditTrailVo){
		// insert into audit_trail table
	}
}

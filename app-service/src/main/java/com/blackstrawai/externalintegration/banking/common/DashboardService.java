package com.blackstrawai.externalintegration.banking.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.common.BaseService;

@Service
public class DashboardService extends BaseService{

	@Autowired
	private DashboardDao dashboardDao;

	public void setDefaultAccount (BasicAccountDetailsVo defaultAccount) throws ApplicationException {
		dashboardDao.setDefautAccount(defaultAccount);
	}
	
}

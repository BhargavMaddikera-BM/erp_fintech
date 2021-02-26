package com.blackstrawai.externalintegration.compliance;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.common.BaseService;
import com.blackstrawai.externalintegration.compliance.employeeprovidentfund.EmployeeProvidentFundLoginVo;

@Service
public class IncomeTaxService extends BaseService{
	@Autowired
	private IncomeTaxDao incomeTaxDao;
	
	/**
	 * Hashes password
	 * Logs in or creates a new user
	 * 
	 * @param loginVo
	 * @throws ApplicationException
	 * @throws SQLException
	 */
	public void createIncomeTaxUser(EmployeeProvidentFundLoginVo loginVo) throws ApplicationException, SQLException {
	/*	loginVo.setLoginPassword(DeciferAES256.encrypt(loginVo.getLoginPassword()));
		incomeTaxDao.loginIncomeTaxUser(loginVo);*/
	}

}

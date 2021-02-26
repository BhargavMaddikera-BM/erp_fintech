package com.blackstrawai.externalintegration.banking.common;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.common.BaseDao;

@Repository
public class DashboardDao extends BaseDao{

	private Logger logger = Logger.getLogger(DashboardDao.class);

	
	public BasicAccountDetailsVo getDefaultAccountForOrg(Integer orgId, Integer userId , String rolename) throws ApplicationException {
		BasicAccountDetailsVo defaultAccount = null;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = getBankingConnection();
			ps = con.prepareStatement(DashboardConstants.GET_DEFAULT_ACCOUNT_FOR_ORG);
			ps.setInt(1, orgId);
			ps.setInt(2, userId);
			ps.setString(3, rolename);

			rs = ps.executeQuery();
			while(rs.next()) {
				defaultAccount = new BasicAccountDetailsVo();
				defaultAccount.setAccountNo(rs.getString(1));
				defaultAccount.setType(rs.getString(2));
			}
		} catch (Exception e) {
			logger.error("Error while fetching getStatementForAccount:",e);
			throw new ApplicationException("Error during method getStatementForAccount: ", e);
		} finally {
			closeResources(rs, ps, con);
		}
		return defaultAccount;
	}
	
	
	public void setDefautAccount (BasicAccountDetailsVo defaultAccount) throws ApplicationException {
		if(defaultAccount!=null && defaultAccount.getOrgId()!=null) {
		BasicAccountDetailsVo existingDefaultAccount = getDefaultAccountForOrg(defaultAccount.getOrgId(),defaultAccount.getUserId() , defaultAccount.getRoleName());
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = getBankingConnection();
			if(existingDefaultAccount!=null) {
				ps = con.prepareStatement(DashboardConstants.UPDATE_DEFAULT_ACCOUNT_FOR_ORG);
				String acctNo = defaultAccount.getType()!=null && "Perfios".equals( defaultAccount.getType())  ? defaultAccount.getName() : defaultAccount.getAccountNo() ; 
				ps.setString(1, acctNo);
				ps.setString(2, defaultAccount.getType());
				ps.setInt(3, defaultAccount.getCutomerId());
				ps.setDate(4, new Date(System.currentTimeMillis()));
				ps.setInt(5, defaultAccount.getUserId());
				ps.setString(6, defaultAccount.getRoleName());
				ps.setInt(7, defaultAccount.getOrgId());
				ps.setInt(8, defaultAccount.getUserId());
				ps.setString(9, defaultAccount.getRoleName());
				ps.executeUpdate();
				logger.info("Default account updated successfully");
			}else {
				ps = con.prepareStatement(DashboardConstants.CREATE_DEFAULT_ACCOUNT_FOR_ORG);
				String acctNo = defaultAccount.getType()!=null && "Perfios".equals( defaultAccount.getType())  ? defaultAccount.getName() : defaultAccount.getAccountNo() ; 
				ps.setString(1, acctNo);
				ps.setString(2, defaultAccount.getType());
				ps.setInt(3, defaultAccount.getCutomerId());
				ps.setDate(4, new Date(System.currentTimeMillis()));
				ps.setInt(5, defaultAccount.getUserId());
				ps.setInt(6, defaultAccount.getOrgId());
				ps.setString(7, defaultAccount.getRoleName());
				ps.executeUpdate();
				logger.info("Default account added successfully");
			}
			
		} catch (Exception e) {
			logger.error("Error while fetching setDefautAccount:",e);
			throw new ApplicationException("Error during method setDefautAccount: ", e);
		}finally {
			closeResources(null, ps, con);
		}
		
		}
	}
	
	
}

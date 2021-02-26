package com.blackstrawai.externalintegration.compliance;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.common.BaseDao;
import com.blackstrawai.common.CommonConstants;
import com.blackstrawai.externalintegration.compliance.employeeprovidentfund.EmployeeProvidentFundLoginVo;

@Repository
public class IncomeTaxDao extends BaseDao {
	private Logger logger = Logger.getLogger(IncomeTaxDao.class);
	
	/**
	 * Checks if user exists in database.
	 * 
	 * Logs in user if exists.
	 * Updates Remember Me boolean.
	 * 
	 * Creates a new user if user does not exist.
	 * Sets ID of user.
	 * 
	 * @param loginVo
	 * @throws SQLException
	 * @throws ApplicationException
	 */
	public void loginIncomeTaxUser(EmployeeProvidentFundLoginVo loginVo) throws SQLException, ApplicationException {
		logger.info("Entry into method: loginIncomeTaxUser");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = getUserMgmConnection();
			con.setAutoCommit(false);
			if (!isUserExist(loginVo, con)) {
				preparedStatement = con.prepareStatement(IncomeTaxConstants.CREATE_INCOME_TAX_USER,
						Statement.RETURN_GENERATED_KEYS);
				preparedStatement.setString(1, loginVo.getLoginName());
				preparedStatement.setString(2, loginVo.getLoginPassword());
				preparedStatement.setString(3, loginVo.getUserId());
				preparedStatement.setString(4, loginVo.getRoleName());
				preparedStatement.setInt(5, loginVo.getOrganizationId());
				preparedStatement.setBoolean(6, loginVo.isRememberMe());
				int rowAffected = preparedStatement.executeUpdate();
				if (rowAffected == 1) {
					rs = preparedStatement.getGeneratedKeys();
					if (rs.next()) {
						loginVo.setId(rs.getInt(1));
						loginVo.setLoginPassword(null);
					}
				}
			}
			con.commit();
		} catch (Exception e) {
			logger.error("Error during loginIncomeTaxUser ", e);
			con.rollback();
			logger.info("Before throw in dao :: " + e.getMessage());
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, con);
		}
	
	}
	
	/**
	 * Checks if user exists in database.
	 * Updates remember me and sets password to null if user exists. 
	 * 
	 * @param loginVo
	 * @param con
	 * @return true if user exists. false if not.
	 * @throws SQLException
	 * @throws ApplicationException
	 */
	private boolean isUserExist(EmployeeProvidentFundLoginVo loginVo, Connection con)
			throws SQLException, ApplicationException {
		logger.info("Entry into method: isUserExist");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		boolean flag = false;
		try {
			preparedStatement = con.prepareStatement(IncomeTaxConstants.SELECT_INCOME_TAX_USER);
			preparedStatement.setInt(1, loginVo.getOrganizationId());
			preparedStatement.setString(2, loginVo.getUserId());
			preparedStatement.setString(3, loginVo.getRoleName());
			preparedStatement.setString(4, loginVo.getLoginName());
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				int id = rs.getInt(1);
				flag = true;
				loginVo.setId(id);
				updateRememberMe(id, loginVo.isRememberMe(), con,loginVo.getLoginPassword());
				loginVo.setLoginPassword(null);
			}

		} catch (Exception e) {
			logger.info("Exception in isUserExist :: " + e.getMessage());
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, null);
		}
		return flag;

	}
	
	/**
	 * Updates remember me in database.
	 * 
	 * @param id
	 * @param rememberMe
	 * @param con
	 * @param password
	 * @throws SQLException
	 * @throws ApplicationException
	 */
	private void updateRememberMe(int id, boolean rememberMe, Connection con, String password)
			throws SQLException, ApplicationException {
		logger.info("Entry into method: updateRememberMe");
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = con.prepareStatement(IncomeTaxConstants.UPDATE_REMEMBER_ME_STATUS);
			preparedStatement.setBoolean(1, rememberMe);
			preparedStatement.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
			preparedStatement.setString(3, CommonConstants.STATUS_AS_ACTIVE);
			preparedStatement.setString(4, password);
			preparedStatement.setInt(5, id);
			preparedStatement.executeUpdate();
		} catch (Exception e) {
			logger.info("Exception in updateRememberMe:: " + e.getMessage());
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(null, preparedStatement, null);
		}

	}

}

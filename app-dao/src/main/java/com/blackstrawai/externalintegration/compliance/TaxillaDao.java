package com.blackstrawai.externalintegration.compliance;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.common.BaseDao;
import com.blackstrawai.common.CommonConstants;

/**
 * DAO layer for Taxilla
 * 
 * @author adityabharadwaj
 *
 */
@Repository
public class TaxillaDao extends BaseDao {

	private Logger logger = Logger.getLogger(TaxillaDao.class);

	/**
	 * Fetch saved OTP from the database
	 * 
	 * @param organizationId
	 * @param userId
	 * @param roleName
	 * @return OTP
	 * @throws ApplicationException
	 */
	public String getOtp(int organizationId, int userId, String roleName) throws ApplicationException {
		logger.info("Entry into method: getOtp");
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		String otp = null;
		try {
			con = getUserMgmConnection();
			preparedStatement = con.prepareStatement(TaxillaConstants.GET_OTP);
			preparedStatement.setInt(1, organizationId);
			preparedStatement.setInt(2, userId);
			preparedStatement.setString(3, roleName);
			preparedStatement.setString(4, CommonConstants.STATUS_AS_ACTIVE);
			preparedStatement.setString(5, CommonConstants.CONFLUENCE);
			preparedStatement.setString(6, CommonConstants.GST);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				otp = rs.getString(1);
			}

		} catch (Exception e) {
			logger.info("Exception in getOtp :: " + e.getMessage());
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return otp;

	}
	
	/**
	 * Get action as per search type.
	 * 
	 * @param gstSearchType
	 * @return action
	 * @throws ApplicationException
	 */
	public String getAction(String gstSearchType) throws ApplicationException {
		logger.info("Entry into method: getAction");
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		String action = null;
		try {
			con = getUserMgmConnection();
			preparedStatement = con.prepareStatement(TaxillaConstants.GET_ACTION);
			preparedStatement.setString(1, gstSearchType);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				action = rs.getString(1);
			}

		} catch (Exception e) {
			logger.info("Exception in getAction :: " + e.getMessage());
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return action;

	
	}
	
	/**
	 * Set OTP of the user.
	 * 
	 * @param organizationId
	 * @param userId
	 * @param roleName
	 * @param otp
	 * @throws ApplicationException
	 */
	public void setOtp(int organizationId, String userId, String roleName, String otp) throws ApplicationException {
		logger.info("Entry into method: setOtp");
		Connection con =null;
		PreparedStatement preparedStatement=null;
		try  {
			con = getUserMgmConnection();
			preparedStatement = con.prepareStatement(TaxillaConstants.SET_OTP);
			preparedStatement.setString(1, CommonConstants.CONFLUENCE);
			preparedStatement.setString(2, CommonConstants.GST);
			preparedStatement.setInt(3, organizationId);
			preparedStatement.setString(4, CommonConstants.STATUS_AS_ACTIVE);
			preparedStatement.setString(5, userId);
			preparedStatement.setString(6, roleName);
			preparedStatement.setString(7, otp);
			preparedStatement.executeUpdate();
		} catch (SQLException | ApplicationException e) {
			throw new ApplicationException("Unable to set OTP", e);
		} finally {
			closeResources(null, preparedStatement, con);
		}
	}


}

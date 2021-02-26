package com.blackstrawai.externalintegration.messaging;

import java.sql.Connection;
import java.sql.PreparedStatement;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.common.BaseDao;
import com.blackstrawai.common.CommonConstants;

/**
 * DAO layer for Messaging
 * 
 * @author Surendra Raavi
 *
 */
@Repository
public class MessagingDao extends BaseDao {

	private Logger logger = Logger.getLogger(MessagingDao.class);

	/**
	 * Fetch save OTP req ID to the database
	 * 
	 * @param organizationId
	 * @param userId
	 * @param roleName
	 * @return OTP
	 * @throws ApplicationException
	 */
	public void saveOtpRequest(int organizationId, int userId, String roleName,String mobileNo,String reqId,String type,String subType) throws ApplicationException {
		logger.info("Entry into method: saveOtpRequest");
		Connection con = null;
		PreparedStatement preparedStatement = null;
		try {
			con = getUserMgmConnection();
			preparedStatement = con.prepareStatement(MessagingConstants.SAVE_OTP_REQUEST);
			preparedStatement.setInt(1, organizationId);
			preparedStatement.setInt(2, userId);
			preparedStatement.setString(3, roleName);
			preparedStatement.setString(4, mobileNo);
			preparedStatement.setString(5, reqId);
			preparedStatement.setString(6, type);
			preparedStatement.setString(7, subType);
			preparedStatement.executeUpdate();

		} catch (Exception e) {
			logger.info("Exception in saveOtpRequest :: " + e.getMessage());
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(null, preparedStatement, con);
		}

	}
	
	public void updateOtp(String mobileNo,String type,String subType) throws ApplicationException {
		logger.info("Entry into method: updateOtp");
		Connection con = null;
		PreparedStatement preparedStatement = null;
		try {
			con = getUserMgmConnection();
			preparedStatement = con.prepareStatement(MessagingConstants.UPDATE_OTP_REQUEST);
			preparedStatement.setString(1, CommonConstants.STATUS_AS_INACTIVE);
			preparedStatement.setString(2, mobileNo);
			preparedStatement.setString(3, type);
			preparedStatement.setString(4, subType);
			preparedStatement.executeUpdate();

		} catch (Exception e) {
			logger.info("Exception in verifyOtp :: " + e.getMessage());
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(null, preparedStatement, con);
		}

	
	}

}

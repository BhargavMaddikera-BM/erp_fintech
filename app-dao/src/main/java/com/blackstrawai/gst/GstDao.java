package com.blackstrawai.gst;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.common.BaseDao;

@Repository
public class GstDao extends BaseDao {

	private Logger logger = Logger.getLogger(GstDao.class);

	public void addGstNumberForPan(PanGstVo panGstVo) throws ApplicationException {
		logger.info("Entry into method: addGstNumberForPan");
		Connection conn = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;

		try {
			conn = getUserMgmConnection();
			preparedStatement = conn.prepareStatement(GstConstant.INSERT_GST_FOR_GIVEN_PAN);
			preparedStatement.setString(1, panGstVo.getPanNo());
			preparedStatement.setString(2, panGstVo.getGstNumber());
			preparedStatement.setInt(3, panGstVo.getOrganizationId());
			preparedStatement.setString(4, panGstVo.getUserId());
			preparedStatement.setString(5, panGstVo.getRoleName());

			int rowAffected = preparedStatement.executeUpdate();
			if (rowAffected == 1) {
				logger.info("Successfully created GstNo: " + panGstVo.getGstNumber() + " for given PanNo:"
						+ panGstVo.getPanNo());
			}
		} catch (Exception e) {
			logger.info("Error in addGstNumberForPan ", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, conn);
		}

	}

	public List<String> getGstFromPanNumber(String panNO, Integer orgId) throws ApplicationException {
		logger.info("Entry into method: getGstFromPanNumber");
		Connection conn = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;

		List<String> gstNumbers = new ArrayList<>();

		try {
			conn = getUserMgmConnection();
			preparedStatement = conn.prepareStatement(GstConstant.GET_GST_FROM_PAN);
			preparedStatement.setInt(1, orgId);
			preparedStatement.setString(2, panNO);

			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				gstNumbers.add(rs.getString(1));
			}

			return gstNumbers;

		} catch (Exception e) {
			logger.info("Error in getGstFromPanNumber ", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, conn);
		}
	}

}

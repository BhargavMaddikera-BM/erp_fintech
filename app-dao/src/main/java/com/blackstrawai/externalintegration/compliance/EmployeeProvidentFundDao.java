package com.blackstrawai.externalintegration.compliance;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.attachments.AttachmentsConstants;
import com.blackstrawai.common.BaseDao;
import com.blackstrawai.common.CommonConstants;
import com.blackstrawai.externalintegration.compliance.employeeprovidentfund.EmployeeProvidentFundAttachmentVo;
import com.blackstrawai.externalintegration.compliance.employeeprovidentfund.EmployeeProvidentFundChallanVo;
import com.blackstrawai.externalintegration.compliance.employeeprovidentfund.EmployeeProvidentFundEcrVo;
import com.blackstrawai.externalintegration.compliance.employeeprovidentfund.EmployeeProvidentFundLoginVo;

@Repository
public class EmployeeProvidentFundDao extends BaseDao {
	private Logger logger = Logger.getLogger(EmployeeProvidentFundDao.class);

	public EmployeeProvidentFundLoginVo loginEpfUser(EmployeeProvidentFundLoginVo epfUser)
			throws ApplicationException, SQLException {
		logger.info("Entry into method: createEpfUser");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = getUserMgmConnection();
			con.setAutoCommit(false);
			if (!isUserExist(epfUser, con)) {
				preparedStatement = con.prepareStatement(EmployeeProvidentFundConstants.CREATE_EPF_USER,
						Statement.RETURN_GENERATED_KEYS);
				preparedStatement.setString(1, epfUser.getLoginName());
				preparedStatement.setString(2, epfUser.getLoginPassword());
				preparedStatement.setString(3, epfUser.getUserId());
				preparedStatement.setString(4, epfUser.getRoleName());
				preparedStatement.setInt(5, epfUser.getOrganizationId());
				preparedStatement.setBoolean(6, epfUser.isRememberMe());
				int rowAffected = preparedStatement.executeUpdate();
				if (rowAffected == 1) {
					rs = preparedStatement.getGeneratedKeys();
					if (rs.next()) {
						epfUser.setId(rs.getInt(1));
						epfUser.setLoginPassword(null);
					}
				}
			}
			con.commit();
		} catch (Exception e) {
			logger.error("Error during createEpfUser ", e);
			con.rollback();
			logger.info("Before throw in dao :: " + e.getMessage());
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return epfUser;
	}



	private boolean isUserExist(EmployeeProvidentFundLoginVo epfUser, Connection con)
			throws SQLException, ApplicationException {
		logger.info("Entry into method: isUserExist");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		boolean flag = false;
		try {
			preparedStatement = con.prepareStatement(EmployeeProvidentFundConstants.SELECT_EPF_USER);
			preparedStatement.setInt(1, epfUser.getOrganizationId());
			preparedStatement.setString(2, epfUser.getUserId());
			preparedStatement.setString(3, epfUser.getRoleName());
			preparedStatement.setString(4, epfUser.getLoginName());
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				int id = rs.getInt(1);
				flag = true;
				epfUser.setId(id);
				updateRememberMe(id, epfUser.isRememberMe(), con,epfUser.getLoginPassword());
				epfUser.setLoginPassword(null);
			}

		} catch (Exception e) {
			logger.info("Exception in isUserExist :: " + e.getMessage());
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, null);
		}
		return flag;

	}

	private void updateRememberMe(int id, boolean rememberMe, Connection con,String password)
			throws SQLException, ApplicationException {
		logger.info("Entry into method: updateRememberMe");
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = con.prepareStatement(EmployeeProvidentFundConstants.UPDATE_REMEMBER_ME_STATUS);
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

	public List<EmployeeProvidentFundChallanVo> getRecentChallans(int userId) throws ApplicationException {
		logger.info("Entry into method: getChallans");
		PreparedStatement preparedStatement = null;
		Connection con = null;
		ResultSet rs = null;
		List<EmployeeProvidentFundChallanVo> challans = new ArrayList<EmployeeProvidentFundChallanVo>();
		try {
			con = getUserMgmConnection();
			preparedStatement = con.prepareStatement(EmployeeProvidentFundConstants.SELECT_CHALLANS_BY_USER_ID);
			preparedStatement.setInt(1, userId);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				EmployeeProvidentFundChallanVo challan = new EmployeeProvidentFundChallanVo();
				challan.setTrrn(rs.getString(1));
				challan.setWageMonth(rs.getString(2));
				challan.setEcrType(rs.getString(3));
				challan.setUploadDate(rs.getString(4));
				challan.setStatus(rs.getString(5));
				challan.setAc1(rs.getString(6));
				challan.setAc2(rs.getString(7));
				challan.setAc10(rs.getString(8));
				challan.setAc21(rs.getString(9));
				challan.setAc22(rs.getString(10));
				challan.setTotalAmount(rs.getString(11));
				challan.setCrn(rs.getString(12));
				challan.setId(rs.getInt(13));
				challan.setAttachments(getEpfAttachments(AttachmentsConstants.MODULE_EMPLOYEE_PROVIDENT_FUND_CHALLAN,
						challan.getId(), con));
				challans.add(challan);
			}
		} catch (Exception e) {
			logger.error("Error during getRecentChallans ", e);
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return challans;
	}

	private List<EmployeeProvidentFundAttachmentVo> getEpfAttachments(String type, int epfId, Connection con)
			throws ApplicationException {
		logger.info("Entry into method: getEpfAttachments");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		List<EmployeeProvidentFundAttachmentVo> attachments = new ArrayList<EmployeeProvidentFundAttachmentVo>();
		try {
			preparedStatement = con.prepareStatement(EmployeeProvidentFundConstants.GET_EPF_ATTACHMENTS);
			preparedStatement.setString(1, type);
			preparedStatement.setInt(2, epfId);
			preparedStatement.setString(3, CommonConstants.STATUS_AS_ACTIVE);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				EmployeeProvidentFundAttachmentVo attachment = new EmployeeProvidentFundAttachmentVo();
				attachment.setId(rs.getInt(1));
				attachment.setEpfType(rs.getString(2));
				attachment.setEpfId(rs.getInt(3));
				attachment.setName(rs.getString(4));
				attachment.setSize(rs.getString(5));
				attachments.add(attachment);
			}
		} catch (Exception e) {
			logger.error("Error during getRecentChallans ", e);
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, null);
		}
		return attachments;
	}

	public List<EmployeeProvidentFundEcrVo> getRecentEcrs(int userId) throws ApplicationException {
		logger.info("Entry into method: getRecentEcrs");
		PreparedStatement preparedStatement = null;
		Connection con = null;
		ResultSet rs = null;
		List<EmployeeProvidentFundEcrVo> ecrs = new ArrayList<EmployeeProvidentFundEcrVo>();
		try {
			con = getUserMgmConnection();
			preparedStatement = con.prepareStatement(EmployeeProvidentFundConstants.SELECT_ECRS_BY_USER_ID);
			preparedStatement.setInt(1, userId);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				EmployeeProvidentFundEcrVo ecr = new EmployeeProvidentFundEcrVo();
				ecr.setTrrn(rs.getString(1));
				ecr.setWageMonth(rs.getString(2));
				ecr.setEcrType(rs.getString(3));
				ecr.setUploadDate(rs.getString(4));
				ecr.setStatus(rs.getString(5));
				ecr.setSalaryDisbDate(rs.getString(6));
				ecr.setContrRate(rs.getString(7));
				ecr.setId(rs.getInt(8));
				ecr.setAttachments(
						getEpfAttachments(AttachmentsConstants.MODULE_EMPLOYEE_PROVIDENT_FUND_ECR, ecr.getId(), con));
				ecrs.add(ecr);
			}
		} catch (Exception e) {
			logger.error("Error during getRecentEcrs ", e);
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return ecrs;
	}

	public void createRecentChallans(List<EmployeeProvidentFundChallanVo> challans, int userId)
			throws SQLException, ApplicationException {
		logger.info("Entry into method: createRecentChallans");
		PreparedStatement preparedStatement = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			con = getUserMgmConnection();
			con.setAutoCommit(false);
			for (EmployeeProvidentFundChallanVo challan : challans) {
				preparedStatement = con.prepareStatement(EmployeeProvidentFundConstants.INSERT_RECENT_CHALLAN,
						Statement.RETURN_GENERATED_KEYS);
				preparedStatement.setInt(1, userId);
				preparedStatement.setString(2, challan.getTrrn().trim());
				preparedStatement.setString(3, challan.getWageMonth());
				preparedStatement.setString(4, challan.getEcrType());
				preparedStatement.setString(5, challan.getUploadDate());
				preparedStatement.setString(6, challan.getStatus());
				preparedStatement.setString(7, challan.getAc1());
				preparedStatement.setString(8, challan.getAc2());
				preparedStatement.setString(9, challan.getAc10());
				preparedStatement.setString(10, challan.getAc21());
				preparedStatement.setString(11, challan.getAc22());
				preparedStatement.setString(12, challan.getTotalAmount());
				preparedStatement.setString(13, challan.getCrn());
				int rowsAffected = preparedStatement.executeUpdate();
				if (rowsAffected == 1) {
					rs = preparedStatement.getGeneratedKeys();
					if (rs.next()) {
						int epfId = rs.getInt(1);
						challan.setId(epfId);
						if (challan.getAttachments() != null) {
							for (EmployeeProvidentFundAttachmentVo attachment : challan.getAttachments()) {
								attachment.setEpfId(epfId);
								createEpfAttachment(attachment, con);
							}
						}

					}
				}
			}
			con.commit();
		} catch (Exception e) {
			logger.error("Error during createRecentChallans ", e);
			con.rollback();
			logger.info("Before throw in dao :: " + e.getMessage());
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, con);
		}
	}

	private void createEpfAttachment(EmployeeProvidentFundAttachmentVo attachment, Connection con)
			throws ApplicationException {
		logger.info("Entry into method: createEpfAttachment");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			preparedStatement = con.prepareStatement(EmployeeProvidentFundConstants.INSERT_EPF_ATTACHMENT,
					Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setString(1, attachment.getEpfType());
			preparedStatement.setInt(2, attachment.getEpfId());
			preparedStatement.setInt(3, 1);
			preparedStatement.setString(4, attachment.getName());
			preparedStatement.setString(5, attachment.getSize());
			preparedStatement.setString(6, CommonConstants.STATUS_AS_ACTIVE);
			int rowsAffected = preparedStatement.executeUpdate();
			if (rowsAffected == 1) {
				rs = preparedStatement.getGeneratedKeys();
				if (rs.next()) {
					attachment.setId(rs.getInt(1));
				}
			}
		} catch (Exception e) {
			logger.error("Error during createEpfAttachment ", e);
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, null);
		}

	}

	public void createRecentEcrs(List<EmployeeProvidentFundEcrVo> ecrs, int userId)
			throws SQLException, ApplicationException {
		logger.info("Entry into method: createRecentEcrs");
		PreparedStatement preparedStatement = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			con = getUserMgmConnection();
			con.setAutoCommit(false);
			for (EmployeeProvidentFundEcrVo ecr : ecrs) {
				preparedStatement = con.prepareStatement(EmployeeProvidentFundConstants.INSERT_RECENT_ECR,
						Statement.RETURN_GENERATED_KEYS);
				preparedStatement.setInt(1, userId);
				preparedStatement.setString(2, ecr.getTrrn().trim());
				preparedStatement.setString(3, ecr.getWageMonth());
				preparedStatement.setString(4, ecr.getEcrType());
				preparedStatement.setString(5, ecr.getUploadDate());
				preparedStatement.setString(6, ecr.getStatus());
				preparedStatement.setString(7, ecr.getSalaryDisbDate());
				preparedStatement.setString(8, ecr.getContrRate());
				int rowsAffected = preparedStatement.executeUpdate();
				if (rowsAffected == 1) {
					rs = preparedStatement.getGeneratedKeys();
					if (rs.next()) {
						int epfId = rs.getInt(1);
						ecr.setId(epfId);
						if (ecr.getAttachments() != null) {
							for (EmployeeProvidentFundAttachmentVo attachment : ecr.getAttachments()) {
								attachment.setEpfId(epfId);
								createEpfAttachment(attachment, con);
							}
						}

					}
				}
			}
			con.commit();
		} catch (Exception e) {
			logger.error("Error during createRecentEcrs ", e);
			con.rollback();
			logger.info("Before throw in dao :: " + e.getMessage());
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, con);
		}
	}

	public Map<String, String> getLoginUsernamePassword(int userId) throws ApplicationException {
		Map<String, String> userMap = new HashMap<String, String>();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = getUserMgmConnection();
			ps = con.prepareStatement(EmployeeProvidentFundConstants.SELECT_USERNAME_PASSWORD_BY_ID);
			ps.setInt(1, userId);
			rs = ps.executeQuery();
			while (rs.next()) {
				userMap.put(EmployeeProvidentFundConstants.USERNAME_KEY, rs.getString(1));
				userMap.put(EmployeeProvidentFundConstants.PASSWORD_KEY, rs.getString(2));
				userMap.put(EmployeeProvidentFundConstants.ORG_KEY, String.valueOf(rs.getInt(3)));
			}
		} catch (Exception e) {
			logger.error("Error during getLoginUsernamePassword ", e);
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, ps, con);
		}
		return userMap;
	}

	public void createOrUpdateChallan(List<EmployeeProvidentFundChallanVo> challans, int userId)
			throws ApplicationException, SQLException {
		Connection con = null;
		try {
			con = getUserMgmConnection();
			con.setAutoCommit(false);
			for (EmployeeProvidentFundChallanVo challan : challans) {
				int id = isRecordExist(challan.getTrrn(), userId, con,
						AttachmentsConstants.MODULE_EMPLOYEE_PROVIDENT_FUND_CHALLAN);
				if (id > 0) {
					challan.setId(id);
					updateChallan(challan, con, userId);
				} else {
					createChallan(challan, con, userId);
				}
			}
			con.commit();
		} catch (Exception e) {
			logger.error("Error during createOrUpdateChallan ", e);
			con.rollback();
			logger.info("Before throw in dao :: " + e.getMessage());
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(null, null, con);
		}

	}

	public void createOrUpdateEcr(List<EmployeeProvidentFundEcrVo> ecrs, int userId)
			throws ApplicationException, SQLException {
		Connection con = null;
		try {
			con = getUserMgmConnection();
			con.setAutoCommit(false);
			for (EmployeeProvidentFundEcrVo ecr : ecrs) {
				int id = isRecordExist(ecr.getTrrn(), userId, con,
						AttachmentsConstants.MODULE_EMPLOYEE_PROVIDENT_FUND_ECR);
				if (id > 0) {
					ecr.setId(id);
					updateEcr(ecr, con, userId);
				} else {
					createEcr(ecr, con, userId);
				}
			}
			con.commit();
		} catch (Exception e) {
			logger.error("Error during createOrUpdateChallan ", e);
			con.rollback();
			logger.info("Before throw in dao :: " + e.getMessage());
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(null, null, con);
		}

	}

	private void createEcr(EmployeeProvidentFundEcrVo ecr, Connection con, int userId) throws ApplicationException {
		ResultSet rs = null;
		logger.info("Entry into method: createEcr");
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = con.prepareStatement(EmployeeProvidentFundConstants.INSERT_RECENT_ECR,
					Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setInt(1, userId);
			preparedStatement.setString(2, ecr.getTrrn().trim());
			preparedStatement.setString(3, ecr.getWageMonth());
			preparedStatement.setString(4, ecr.getEcrType());
			preparedStatement.setString(5, ecr.getUploadDate());
			preparedStatement.setString(6, ecr.getStatus());
			preparedStatement.setString(7, ecr.getSalaryDisbDate());
			preparedStatement.setString(8, ecr.getContrRate());
			int rowsAffected = preparedStatement.executeUpdate();
			if (rowsAffected == 1) {
				rs = preparedStatement.getGeneratedKeys();
				if (rs.next()) {
					int epfId = rs.getInt(1);
					ecr.setId(epfId);
					if (ecr.getAttachments() != null) {
						for (EmployeeProvidentFundAttachmentVo attachment : ecr.getAttachments()) {
							attachment.setEpfId(epfId);
							createEpfAttachment(attachment, con);
						}
					}

				}
			}
		} catch (Exception e) {
			logger.error("Error during createEcr ", e);
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(null, preparedStatement, null);
		}

	}

	private void updateEcr(EmployeeProvidentFundEcrVo ecr, Connection con, int userId) throws ApplicationException {
		logger.info("Entry into method: updateEcr");
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = con.prepareStatement(EmployeeProvidentFundConstants.UPDATE_RECENT_ECR);
			preparedStatement.setString(1, ecr.getWageMonth());
			preparedStatement.setString(2, ecr.getEcrType());
			preparedStatement.setString(3, ecr.getUploadDate());
			preparedStatement.setString(4, ecr.getStatus());
			preparedStatement.setString(5, ecr.getSalaryDisbDate());
			preparedStatement.setString(6, ecr.getContrRate());
			preparedStatement.setTimestamp(7, new Timestamp(System.currentTimeMillis()));
			preparedStatement.setInt(8, userId);
			preparedStatement.setString(9, ecr.getTrrn().trim());
			int rowsAffected = preparedStatement.executeUpdate();
			updateEpfAttachmentStatus(ecr.getId(), AttachmentsConstants.MODULE_EMPLOYEE_PROVIDENT_FUND_ECR, con,
					CommonConstants.STATUS_AS_DELETE);
			if (rowsAffected == 1 && ecr.getAttachments() != null) {

				for (EmployeeProvidentFundAttachmentVo attachment : ecr.getAttachments()) {
					attachment.setEpfId(ecr.getId());
					createEpfAttachment(attachment, con);
				}

			}
		} catch (Exception e) {
			logger.error("Error during updateEcr ", e);
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(null, preparedStatement, null);
		}

	}

	private void updateChallan(EmployeeProvidentFundChallanVo challan, Connection con, int userId)
			throws ApplicationException {
		logger.info("Entry into method: updateChallan");
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = con.prepareStatement(EmployeeProvidentFundConstants.UPDATE_RECENT_CHALLAN);
			preparedStatement.setString(1, challan.getWageMonth());
			preparedStatement.setString(2, challan.getEcrType());
			preparedStatement.setString(3, challan.getUploadDate());
			preparedStatement.setString(4, challan.getStatus());
			preparedStatement.setString(5, challan.getAc1());
			preparedStatement.setString(6, challan.getAc2());
			preparedStatement.setString(7, challan.getAc10());
			preparedStatement.setString(8, challan.getAc21());
			preparedStatement.setString(9, challan.getAc22());
			preparedStatement.setString(10, challan.getTotalAmount());
			preparedStatement.setString(11, challan.getCrn());
			preparedStatement.setTimestamp(12, new Timestamp(System.currentTimeMillis()));
			preparedStatement.setInt(13, userId);
			preparedStatement.setString(14, challan.getTrrn().trim());
			int rowsAffected = preparedStatement.executeUpdate();
			updateEpfAttachmentStatus(challan.getId(), AttachmentsConstants.MODULE_EMPLOYEE_PROVIDENT_FUND_CHALLAN, con,
					CommonConstants.STATUS_AS_DELETE);
			if (rowsAffected == 1 && challan.getAttachments() != null) {
				for (EmployeeProvidentFundAttachmentVo attachment : challan.getAttachments()) {
					attachment.setEpfId(challan.getId());
					createEpfAttachment(attachment, con);
				}

			}

		} catch (Exception e) {
			logger.error("Error during updateChallan ", e);
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(null, preparedStatement, null);
		}

	}

	private void updateEpfAttachmentStatus(int epfId, String epfType, Connection con, String status)
			throws ApplicationException {
		logger.info("Entry into method: updateEpfAttachmentStatus");
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = con.prepareStatement(EmployeeProvidentFundConstants.UPDATE_EPF_ATTACHMENT_STATUS);
			preparedStatement.setString(1, status);
			preparedStatement.setInt(2, epfId);
			preparedStatement.setString(3, epfType);
			preparedStatement.executeUpdate();
		} catch (Exception e) {
			logger.error("Error during updateEpfAttachmentStatus ", e);
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(null, preparedStatement, null);
		}
	}

	private void createChallan(EmployeeProvidentFundChallanVo challan, Connection con, int userId)
			throws ApplicationException {
		logger.info("Entry into method: createChallan");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			preparedStatement = con.prepareStatement(EmployeeProvidentFundConstants.INSERT_RECENT_CHALLAN,
					Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setInt(1, userId);
			preparedStatement.setString(2, challan.getTrrn().trim());
			preparedStatement.setString(3, challan.getWageMonth());
			preparedStatement.setString(4, challan.getEcrType());
			preparedStatement.setString(5, challan.getUploadDate());
			preparedStatement.setString(6, challan.getStatus());
			preparedStatement.setString(7, challan.getAc1());
			preparedStatement.setString(8, challan.getAc2());
			preparedStatement.setString(9, challan.getAc10());
			preparedStatement.setString(10, challan.getAc21());
			preparedStatement.setString(11, challan.getAc22());
			preparedStatement.setString(12, challan.getTotalAmount());
			preparedStatement.setString(13, challan.getCrn());
			int rowsAffected = preparedStatement.executeUpdate();
			if (rowsAffected == 1) {
				rs = preparedStatement.getGeneratedKeys();
				if (rs.next()) {
					int epfId = rs.getInt(1);
					challan.setId(epfId);
					if (challan.getAttachments() != null) {
						for (EmployeeProvidentFundAttachmentVo attachment : challan.getAttachments()) {
							attachment.setEpfId(epfId);
							createEpfAttachment(attachment, con);
						}
					}

				}
			}

		} catch (Exception e) {
			logger.error("Error during createChallan ", e);
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, null);
		}

	}

	private int isRecordExist(String trrn, int userId, Connection con, String recordType) throws ApplicationException {
		ResultSet rs = null;
		PreparedStatement ps = null;
		String sql = null;
		int id = 0;
		try {
			if (recordType.equals(EmployeeProvidentFundConstants.CHALLAN))
				sql = EmployeeProvidentFundConstants.SELECT_CHALLAN_BY_TRRN;
			else
				sql = EmployeeProvidentFundConstants.SELECT_ECR_BY_TRRN;
			ps = con.prepareStatement(sql);
			ps.setInt(1, userId);
			ps.setString(2, trrn.trim());
			rs = ps.executeQuery();
			while (rs.next()) {
				id = rs.getInt(1);
				closeResources(rs, ps, null);
				return id;
			}

		} catch (Exception e) {
			logger.error("Error during isRecordExist ", e);
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, ps, null);
		}
		return id;
	}

	/*public EmployeeProvidentFundLoginVo getLoginDetailsForAnOrganization(int organizationId, String login)
			throws ApplicationException {
		logger.info("Entry into method:getLoginDetailsForAnOrganization");
		PreparedStatement preparedStatement = null;
		Connection con = null;
		ResultSet rs = null;
		EmployeeProvidentFundLoginVo data = new EmployeeProvidentFundLoginVo();
		try {
			con = getUserMgmConnection();
			String query = EmployeeProvidentFundConstants.FETCH_LOGIN;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, organizationId);
			preparedStatement.setString(2, login);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				data.setId(rs.getInt(1));
				data.setLoginName(login);
				data.setLoginPassword(rs.getString(2));
				data.setRememberMe(rs.getBoolean(3));
			}

		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);

		}
		return data;
	}
*/
	public void disconnectUser(int userId,String status) throws ApplicationException {
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = getUserMgmConnection();
			ps = con.prepareStatement(EmployeeProvidentFundConstants.DISCONNECT_USER);
			ps.setString(1, status);
			ps.setBoolean(2, false);
			ps.setInt(3, userId);
			ps.executeUpdate();
		} catch(Exception e) {
			logger.error("Error in disconnectUser:");
			throw new ApplicationException(e);
		} finally {
			closeResources(null, ps, con);

		}
		
	}

}

package com.blackstrawai.ap;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.ap.balanceconfirmation.BalanceConfirmationBasicVo;
import com.blackstrawai.ap.balanceconfirmation.BalanceConfirmationGeneralVo;
import com.blackstrawai.ap.balanceconfirmation.BalanceConfirmationVo;
import com.blackstrawai.ap.balanceconfirmation.VendorAddressVo;
import com.blackstrawai.attachments.AttachmentsConstants;
import com.blackstrawai.attachments.AttachmentsDao;
import com.blackstrawai.attachments.AttachmentsVo;
import com.blackstrawai.attachments.UploadFileVo;
import com.blackstrawai.common.BaseDao;
import com.blackstrawai.common.CommonConstants;
import com.blackstrawai.keycontact.VendorDao;
import com.blackstrawai.vendorsettings.VendorAccessMgmtDao;

@Repository
public class BalanceConfirmationDao extends BaseDao {

	@Autowired
	private AttachmentsDao attachmentsDao;

	@Autowired
	private VendorDao vendorDao;

	@Autowired
	VendorAccessMgmtDao vendorAccessMgmtDao;

	private Logger logger = Logger.getLogger(BalanceConfirmationDao.class);

	public Boolean createBalanceConfirmation(BalanceConfirmationVo balanceConfirmationVo) throws ApplicationException {
		logger.info("Entry into method: createBalanceConfirmation");
		boolean isCreatedSuccessfully = false;
		ResultSet rs = null;
		Connection con = null;
		PreparedStatement preparedStatement = null;
		if (balanceConfirmationVo != null) {
			try {
				con = getAccountsPayable();
				con.setAutoCommit(false);
				String sql = BalanceConfirmationConstants.INSERT_INTO_BALANCE_CONFIRMATION;
				preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				preparedStatement.setObject(1,
						balanceConfirmationVo.getVendorId() != null ? balanceConfirmationVo.getVendorId() : null);
				preparedStatement.setDate(2,
						balanceConfirmationVo.getStartDate() != null ? balanceConfirmationVo.getStartDate() : null);
				preparedStatement.setDate(3,
						balanceConfirmationVo.getEndDate() != null ? balanceConfirmationVo.getEndDate() : null);
				preparedStatement.setString(4,
						balanceConfirmationVo.getRoleName() != null ? balanceConfirmationVo.getRoleName() : null);
				preparedStatement.setBoolean(5,
						balanceConfirmationVo.getIsQuick() != null ? balanceConfirmationVo.getIsQuick() : null);
				preparedStatement.setInt(6, Integer.valueOf(balanceConfirmationVo.getUserId()));
				preparedStatement.setBoolean(7, balanceConfirmationVo.getIsSuperAdmin());
				preparedStatement.setInt(8, balanceConfirmationVo.getOrganizationId());
				preparedStatement.setString(9,
						balanceConfirmationVo.getStatus() != null ? balanceConfirmationVo.getStatus() : null);
				int rowAffected = preparedStatement.executeUpdate();
				if (rowAffected == 1) {
					rs = preparedStatement.getGeneratedKeys();
					if (rs.next()) {
						balanceConfirmationVo.setId(rs.getInt(1));
					}
				}

				if (balanceConfirmationVo.getGeneralInfo() != null
						&& balanceConfirmationVo.getGeneralInfo().size() > 0) {
					for (BalanceConfirmationGeneralVo balanceConfirmationGeneralVo : balanceConfirmationVo
							.getGeneralInfo()) {
						createBalanceInformationCurrencyInfo(balanceConfirmationGeneralVo,
								balanceConfirmationVo.getId(), con);
					}

				}
				if (balanceConfirmationVo.getAttachments() != null
						&& balanceConfirmationVo.getAttachments().size() > 0) {
					attachmentsDao.createAttachments(balanceConfirmationVo.getOrganizationId(),
							balanceConfirmationVo.getUserId(), balanceConfirmationVo.getAttachments(),
							AttachmentsConstants.MODULE_TYPE_VENDOR_PORTAL_BALANCE_CONFIRMATION,
							balanceConfirmationVo.getId(),balanceConfirmationVo.getRoleName());
				}
				con.commit();
				isCreatedSuccessfully = true;
			} catch (Exception e) {
				logger.info("Error in createBalanceConfirmation::", e);
				try {
					con.rollback();
				} catch (SQLException e1) {
					throw new ApplicationException(e1.getMessage());
				}
				throw new ApplicationException(e.getMessage());
			} finally {
				closeResources(rs, preparedStatement, con);
			}
		}
		return isCreatedSuccessfully;

	}

	private void createBalanceInformationCurrencyInfo(BalanceConfirmationGeneralVo BalanceConfirmationGeneralVo,
			Integer id, Connection con) throws ApplicationException {
		logger.info("Entry into method: createBalanceInformationCurrencyInfo");
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = con.prepareStatement(BalanceConfirmationConstants.INSERT_INTO_BALANCE_CONFIRMATION_CURRENCY);
			preparedStatement.setObject(1, id != null ? id : null);
			preparedStatement.setObject(2,
					BalanceConfirmationGeneralVo.getCurrencyId() != null ? BalanceConfirmationGeneralVo.getCurrencyId()
							: null);
			preparedStatement.setString(3,
					BalanceConfirmationGeneralVo.getOpeningBalance() != null
					? BalanceConfirmationGeneralVo.getOpeningBalance()
							: null);
			preparedStatement.setString(4,
					BalanceConfirmationGeneralVo.getClosingBalance() != null
					? BalanceConfirmationGeneralVo.getClosingBalance()
							: null);
			preparedStatement.executeUpdate();
		} catch (Exception e) {
			logger.info("Error in createBalanceInformationCurrencyInfo::", e);
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(null, preparedStatement, null);
		}

	}

	public List<BalanceConfirmationBasicVo> getAllBalanceConfirmation(Integer organizationId, Integer vendorId)
			throws ApplicationException {
		logger.info("Entry into method: getAllBalanceConfirmation");
		List<BalanceConfirmationBasicVo> lisstofBalanceConfirmation = new ArrayList<BalanceConfirmationBasicVo>();
		String query = null;
		if (vendorId == 0) {
			query = BalanceConfirmationConstants.GET_ALL_ORGANIZATION_BALANCE_CONFIRMATION;
		} else {
			query = BalanceConfirmationConstants.GET_ALL_VENDOR_BALANCE_CONFIRMATION;
		}
		Connection con =null;
		PreparedStatement preparedStatement =null;
		ResultSet rs = null;
		try  {
			con = getAccountsPayable();
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, vendorId == 0 ? organizationId : vendorId);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				BalanceConfirmationBasicVo balanceConfirmationBasicVo = new BalanceConfirmationBasicVo();
				balanceConfirmationBasicVo.setId(rs.getInt(1));
				Integer VendorId = rs.getInt(2);
				if (VendorId != null) {
					balanceConfirmationBasicVo.setVendorId(VendorId);
					VendorAddressVo vendorAddress = vendorDao.getVendorDetails(VendorId);
					balanceConfirmationBasicVo.setVendorName(vendorAddress.getVendorName());
				}
				String period = rs.getDate(3).toString() + " to " + rs.getDate(4).toString();
				balanceConfirmationBasicVo.setPeriod(period);
				String status = rs.getString(5);
				if (status != null) {
					if (status.equalsIgnoreCase(CommonConstants.STATUS_AS_AWAITING_CONFIRMATION)) {
						balanceConfirmationBasicVo
						.setStatus(CommonConstants.DISPLAY_STATUS_AS_AWAITING_CONFIRMATION);
					}
					if (status.equalsIgnoreCase(CommonConstants.STATUS_AS_ACKNOWLEDGE)) {
						balanceConfirmationBasicVo.setStatus(CommonConstants.DISPLAY_STATUS_AS_ACKNOWLEDGE);
					}
					if (status.equalsIgnoreCase(CommonConstants.STATUS_AS_DECLINE)) {
						balanceConfirmationBasicVo.setStatus(CommonConstants.DISPLAY_STATUS_AS_DECLINE);
					}
				}

				Date updatedDate = rs.getDate(6);
				Date createdDate = rs.getDate(7);
				if (updatedDate != null)
					balanceConfirmationBasicVo.setSubmittedDate(updatedDate);
				else
					balanceConfirmationBasicVo.setSubmittedDate(createdDate);
				Integer orgId = rs.getInt(8);
				if (orgId != null) {
					balanceConfirmationBasicVo.setOrgName(getOrgName(orgId));
				}
				lisstofBalanceConfirmation.add(balanceConfirmationBasicVo);
			}
		} catch (Exception e) {
			logger.info("Error in  getAllBalanceConfirmation:", e);
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return lisstofBalanceConfirmation;

	}

	private String getOrgName(Integer orgId) throws ApplicationException {
		logger.info("To get the getOrgName");
		String orgName = null;
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs =null;
		try {
			con = getUserMgmConnection();
			preparedStatement = con.prepareStatement(BalanceConfirmationConstants.GET_ORGANIZATION_NAME);
			preparedStatement.setInt(1, orgId);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				orgName = rs.getString(1);
			}
		} catch (Exception e) {
			logger.info("Error in getOrgName", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return orgName;
	}

	public BalanceConfirmationVo getBalanceConfirmationById(Integer id) throws ApplicationException {
		logger.info("Entry into method: getBalanceConfirmationById");
		BalanceConfirmationVo balanceConfirmationVo = new BalanceConfirmationVo();
		Connection con = null;
		PreparedStatement preparedStatement =null;
		ResultSet rs = null;
		try  {
			con = getAccountsPayable();
			preparedStatement = con.prepareStatement(BalanceConfirmationConstants.GET_ALL_BALANCE_CONFIRMATION_BY_ID);
			preparedStatement.setInt(1, id);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				balanceConfirmationVo.setId(id);
				balanceConfirmationVo.setStartDate(rs.getDate(1));
				balanceConfirmationVo.setEndDate(rs.getDate(2));
				balanceConfirmationVo.setStatus(rs.getString(3));
				balanceConfirmationVo.setOrganizationId(rs.getInt(4));
				balanceConfirmationVo.setUserId(rs.getString(5));
				balanceConfirmationVo.setIsSuperAdmin(rs.getBoolean(6));
				balanceConfirmationVo.setRoleName(rs.getString(7));
				balanceConfirmationVo.setVendorId(rs.getInt(8));
				balanceConfirmationVo.setIsQuick(rs.getBoolean(9));
				if (balanceConfirmationVo.getVendorId() != null) {
					balanceConfirmationVo
					.setCompanyDetails(vendorDao.getVendorDetails(balanceConfirmationVo.getVendorId()));
				}
			}

			balanceConfirmationVo.setGeneralInfo(getBalanceConfirmationCurrencyInfoById(id));
			List<UploadFileVo> uploadFileVos = new ArrayList<UploadFileVo>();

			for (AttachmentsVo attachments : attachmentsDao.getAttachmentDetails(id,
					AttachmentsConstants.MODULE_TYPE_VENDOR_PORTAL_BALANCE_CONFIRMATION)) {
				UploadFileVo uploadFileVo = new UploadFileVo();
				uploadFileVo.setId(attachments.getId());
				uploadFileVo.setName(attachments.getFileName());
				uploadFileVo.setSize(attachments.getSize());
				uploadFileVo.setDocumentType(attachments.getDocumentTypeId());
				uploadFileVos.add(uploadFileVo);
			}

			balanceConfirmationVo.setAttachments(uploadFileVos);

		} catch (Exception e) {
			logger.info("Error in  getBalanceConfirmationById:", e);
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return balanceConfirmationVo;

	}

	private List<BalanceConfirmationGeneralVo> getBalanceConfirmationCurrencyInfoById(Integer id)
			throws ApplicationException {
		logger.info("Entry into method:getBalanceConfirmationCurrencyInfoById");
		List<BalanceConfirmationGeneralVo> listBalanceConfirmation = new ArrayList<BalanceConfirmationGeneralVo>();
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs =null;
		try {
			con = getAccountsPayable();
			preparedStatement = con.prepareStatement(BalanceConfirmationConstants.GET_ALL_BALANCE_CONFIRMATION_CURRENCY_INFO_BY_ID); 
			preparedStatement.setInt(1, id);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				BalanceConfirmationGeneralVo balanceConfirmationGeneralVo = new BalanceConfirmationGeneralVo();
				balanceConfirmationGeneralVo.setCurrencyId(rs.getInt(1));
				balanceConfirmationGeneralVo.setOpeningBalance(rs.getString(2));
				balanceConfirmationGeneralVo.setClosingBalance(rs.getString(3));
				balanceConfirmationGeneralVo.setId(rs.getInt(4));
				balanceConfirmationGeneralVo.setStatus(rs.getString(5));
				listBalanceConfirmation.add(balanceConfirmationGeneralVo);
			}

		} catch (Exception e) {
			logger.info("Error in  getBalanceConfirmationCurrencyInfoById:", e);
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return listBalanceConfirmation;
	}

	public Boolean updateBalanceConfirmation(BalanceConfirmationVo balanceConfirmationVo) throws ApplicationException {
		logger.info("Entry into method: updateBalanceConfirmation");
		boolean isTxnSuccess = false;
		Connection con = null;
		if (balanceConfirmationVo != null) {
			try {
				con = getAccountsPayable();
				con.setAutoCommit(false);
				updateBalanceConfirmationInfo(balanceConfirmationVo, con);
				if (balanceConfirmationVo.getGeneralInfo() != null
						&& balanceConfirmationVo.getGeneralInfo().size() > 0) {
					for (BalanceConfirmationGeneralVo balanceConfirmationGeneralVo : balanceConfirmationVo
							.getGeneralInfo()) {
						if (balanceConfirmationGeneralVo.getStatus().equalsIgnoreCase(CommonConstants.STATUS_AS_NEW)) {
							createBalanceInformationCurrencyInfo(balanceConfirmationGeneralVo,
									balanceConfirmationVo.getId(), con);
						} else {
							updateBalanceConfirmationCurrencyInfo(balanceConfirmationGeneralVo, con);
						}

					}

				}

				if (balanceConfirmationVo.getAttachments() != null
						&& balanceConfirmationVo.getAttachments().size() > 0) {
					attachmentsDao.createAttachments(balanceConfirmationVo.getOrganizationId(),
							balanceConfirmationVo.getUserId(), balanceConfirmationVo.getAttachments(),
							AttachmentsConstants.MODULE_TYPE_VENDOR_PORTAL_BALANCE_CONFIRMATION,
							balanceConfirmationVo.getId(),balanceConfirmationVo.getRoleName());
				}
				if (balanceConfirmationVo.getAttachmentsToRemove() != null
						&& balanceConfirmationVo.getAttachmentsToRemove().size() > 0) {
					for (Integer attachmentId : balanceConfirmationVo.getAttachmentsToRemove()) {
						attachmentsDao.changeStatusForSingleAttachment(attachmentId, CommonConstants.STATUS_AS_DELETE,balanceConfirmationVo.getUserId(),balanceConfirmationVo.getRoleName());
					}
				}

				con.commit();
				isTxnSuccess = true;
			} catch (Exception e) {
				logger.info("Error in createBalanceConfirmation::", e);
				try {
					con.rollback();
				} catch (SQLException e1) {
					throw new ApplicationException(e1.getMessage());
				}
				throw new ApplicationException(e.getMessage());
			} finally {
				closeResources(null, null, con);
			}
		}
		return isTxnSuccess;
	}

	private void updateBalanceConfirmationInfo(BalanceConfirmationVo balanceConfirmationVo, Connection con)
			throws ApplicationException {
		logger.info("Entry into method: updateBalanceConfirmationInfo");
		PreparedStatement preparedStatement =null;
		try{
			preparedStatement = con.prepareStatement(BalanceConfirmationConstants.UPDATE_BALANCE_CONFIRMATION);
			preparedStatement.setObject(1,
					balanceConfirmationVo.getVendorId() != null ? balanceConfirmationVo.getVendorId() : null);
			preparedStatement.setDate(2,
					balanceConfirmationVo.getStartDate() != null ? balanceConfirmationVo.getStartDate() : null);
			preparedStatement.setDate(3,
					balanceConfirmationVo.getEndDate() != null ? balanceConfirmationVo.getEndDate() : null);
			preparedStatement.setString(4,
					balanceConfirmationVo.getRoleName() != null ? balanceConfirmationVo.getRoleName() : null);
			preparedStatement.setInt(5, Integer.valueOf(balanceConfirmationVo.getUserId()));
			preparedStatement.setBoolean(6, balanceConfirmationVo.getIsSuperAdmin());
			preparedStatement.setInt(7, balanceConfirmationVo.getOrganizationId());
			preparedStatement.setString(8,
					balanceConfirmationVo.getStatus() != null ? balanceConfirmationVo.getStatus() : null);
			preparedStatement.setBoolean(9,
					balanceConfirmationVo.getIsQuick() != null ? balanceConfirmationVo.getIsQuick() : null);
			preparedStatement.setInt(10, balanceConfirmationVo.getId());
			preparedStatement.executeUpdate();
		} catch (Exception e) {
			logger.info("Error in updateBalanceConfirmationInfo::", e);
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(null, preparedStatement, null);
		}
	}

	private void updateBalanceConfirmationCurrencyInfo(BalanceConfirmationGeneralVo balanceConfirmationGeneralVo,
			Connection con) throws ApplicationException {
		logger.info("Entry into method: updateBalanceConfirmationCurrencyInfo");
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = con.prepareStatement(BalanceConfirmationConstants.UPDATE_BALANCE_CONFIRMATION_CURRENCY);
			preparedStatement.setObject(1,
					balanceConfirmationGeneralVo.getCurrencyId() != null ? balanceConfirmationGeneralVo.getCurrencyId()
							: null);
			preparedStatement.setString(2,
					balanceConfirmationGeneralVo.getOpeningBalance() != null
					? balanceConfirmationGeneralVo.getOpeningBalance()
							: null);
			preparedStatement.setString(3,
					balanceConfirmationGeneralVo.getClosingBalance() != null
					? balanceConfirmationGeneralVo.getClosingBalance()
							: null);
			preparedStatement.setString(4,
					balanceConfirmationGeneralVo.getStatus() != null ? balanceConfirmationGeneralVo.getStatus() : null);
			preparedStatement.setInt(5, balanceConfirmationGeneralVo.getId());
			preparedStatement.executeUpdate();
		} catch (Exception e) {
			logger.info("Error in updateBalanceConfirmationCurrencyInfo::", e);
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(null, preparedStatement, null);
		}
	}

	public void statusChange(int id, String status) throws ApplicationException {
		logger.info("Entry into method: statusChange");
		Connection con =null;
		PreparedStatement preparedStatement = null;
		try {
			con = getAccountsPayable();
			preparedStatement = con.prepareStatement(BalanceConfirmationConstants.STATUS_CHANGE);
			preparedStatement.setString(1, status);
			preparedStatement.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
			preparedStatement.setInt(3, id);
			preparedStatement.executeUpdate();
		} catch (Exception e) {
			logger.info("Error in  statusChange:", e);
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(null, preparedStatement, con);
		}

	}

	public void deleteBalanceConfirmationEntries(Integer id,String userId,String roleName) throws ApplicationException {
		logger.info("To deleteBalanceConfirmationEntries:: ");
		Connection connection = null;
		if (id != null) {
			try {
				connection = getAccountsPayable();
				// To remove from Balance Confirmation Main table
				changeStatusForBalanceConfirmationTables(id, CommonConstants.STATUS_AS_WITHDRAW, connection,
						BalanceConfirmationConstants.MODIFY_BALANCE_CONFIRMATION_STATUS);
				// To remove from Balance Confirmation Currency table
				changeStatusForBalanceConfirmationTables(id, CommonConstants.STATUS_AS_DELETE, connection,
						BalanceConfirmationConstants.MODIFY_BALANCE_CONFIRMATION_CURRENCY_STATUS);
				// To remove from Attachments table
				attachmentsDao.changeStatusForAttachments(id, CommonConstants.STATUS_AS_DELETE,
						AttachmentsConstants.MODULE_TYPE_VENDOR_PORTAL_BALANCE_CONFIRMATION,userId,roleName);
				logger.info("Deleted successfully in all tables ");
			} catch (Exception e) {
				logger.info("Error in deleteBalanceConfirmationEntries:: ", e);
				throw new ApplicationException(e);
			} finally {
				closeResources(null, null, connection);
			}
		}
	}

	private void changeStatusForBalanceConfirmationTables(Integer id, String status, Connection con, String query)
			throws ApplicationException {
		logger.info("To Change the status with query :: " + query);
		if (query != null) {
			PreparedStatement preparedStatement =null;
			try  {
				preparedStatement = con.prepareStatement(query);
				preparedStatement.setString(1, status);
				preparedStatement.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
				preparedStatement.setInt(3, id);
				preparedStatement.executeUpdate();
				logger.info("Status chaneged successfully ");
			} catch (Exception e) {
				logger.info("Error in changeStatusForBalanceConfirmationTables ", e);
				throw new ApplicationException(e);
			} finally {
				closeResources(null, preparedStatement, null);
			}
		}

	}

}

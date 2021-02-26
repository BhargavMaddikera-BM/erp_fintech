package com.blackstrawai.attachments;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.common.BaseDao;
import com.blackstrawai.common.CommonConstants;

@Repository
public class AttachmentsDao extends BaseDao {

	private Logger logger = Logger.getLogger(AttachmentsDao.class);

	private void createAttachment(UploadFileVo uploadFileDetails, String moduleType, Integer moduleTypeId,int organizationId,String userId,String roleName)
			throws SQLException, ApplicationException {
		logger.info("To create a new attachments in AttachmentsDao");
		Connection connection = null;
		PreparedStatement preparedStatement =null;
		if (uploadFileDetails != null) {
			try {
				connection = getFinanceCommon();
				connection.setAutoCommit(false);
				Integer version = 1;
				preparedStatement = connection.prepareStatement(AttachmentsConstants.INSERT_INTO_ATTACHMENTS);
				preparedStatement.setString(1, moduleType);
				preparedStatement.setInt(2, moduleTypeId);
				preparedStatement.setInt(3, version);
				preparedStatement.setString(4, uploadFileDetails.getName());
				preparedStatement.setString(5, uploadFileDetails.getSize());
				preparedStatement.setString(6, CommonConstants.STATUS_AS_ACTIVE);
				preparedStatement.setDate(7, new Date(System.currentTimeMillis()));
				preparedStatement.setObject(8,uploadFileDetails.getDocumentType() != null ? uploadFileDetails.getDocumentType() : null);
				preparedStatement.setInt(9, organizationId);
				preparedStatement.setInt(10, Integer.parseInt(userId));
				preparedStatement.setString(11, roleName);
				preparedStatement.setBoolean(12, uploadFileDetails.getIsAkshar());
				preparedStatement.executeUpdate();
				connection.commit();
				logger.info("Successfully created new attachments in AttachmentsDao");
			} catch (Exception e) {
				logger.info("Error in createSingleAttachment:: ", e);
				connection.rollback();
				throw new ApplicationException(e);
			} finally {
				closeResources(null, preparedStatement, connection);
			}
		}
	}

	public void createAttachments(int organizationId,String userId,List<UploadFileVo> uploadList, String moduleType, Integer moduleTypeId,String roleName)
			throws SQLException, ApplicationException {
		if (!uploadList.isEmpty()) {
			logger.info("To create new attachemnts from list");
			for (UploadFileVo upload : uploadList)
				createAttachment(upload, moduleType, moduleTypeId,organizationId,userId,roleName);
		}

	}
	
	public void createProfileAttachment(String email,UploadFileVo attachment)throws ApplicationException {
		Connection connection =null;
		PreparedStatement preparedStatement =null;

		if (email!=null && attachment!=null) {
			try {
				connection = getFinanceCommon();
				preparedStatement = connection.prepareStatement(AttachmentsConstants.INSERT_INTO_PROFILE_ATTACHMENTS);
				preparedStatement.setString(1, email);
				preparedStatement.setString(2, attachment.getName());
				preparedStatement.executeUpdate();
			} catch (Exception e) {
				logger.error("Error in createProfileAttachment:: ", e);
				throw new ApplicationException(e);
			} finally {
				closeResources(null, preparedStatement, connection);
			}

		}

	}

	public UploadFileVo getProfileAttachment(String email)
			throws ApplicationException {
		UploadFileVo uploadFileVo=new UploadFileVo();
		if (email != null ) {
			logger.info("To get attachemnt details");
			Connection connection =null;
			PreparedStatement preparedStatement =null;
			ResultSet rs =null;
			try {
				connection = getFinanceCommon();
				preparedStatement = connection.prepareStatement(AttachmentsConstants.GET_PROFILE_ATTACHMENT_BY_EMAIL);
				preparedStatement.setString(1, email);
				rs = preparedStatement.executeQuery();
				while (rs.next()) {
					
					uploadFileVo.setId(rs.getInt(1));
					uploadFileVo.setName(rs.getString(3));
				}
				logger.info("To get attachemnt details fetched successfully with size :: " + uploadFileVo);
			} catch (Exception e) {
				logger.info("Error in get attachemnt details:: ", e);
				throw new ApplicationException(e);
			} finally {
				closeResources(rs, preparedStatement, connection);
			}
		}
		return uploadFileVo;
	}	

	public List<AttachmentsVo> getAttachmentDetails(Integer moduleTypeId, String moduleType)
			throws ApplicationException {
		List<AttachmentsVo> list = null;
		if (moduleTypeId != null && moduleType != null) {
			logger.info("To get attachemnt details");
			Connection connection =null;
			PreparedStatement preparedStatement =null;
			ResultSet rs =null;
			try {
				connection = getFinanceCommon();
				preparedStatement = connection.prepareStatement(AttachmentsConstants.GET_ATTACHMENT_FOR_TYPE_ID);
				preparedStatement.setString(1, moduleType);
				preparedStatement.setInt(2, moduleTypeId);
				rs = preparedStatement.executeQuery();
				list = new ArrayList<AttachmentsVo>();
				while (rs.next()) {
					AttachmentsVo attachmentsVo = new AttachmentsVo();
					attachmentsVo.setId(rs.getInt(1));
					attachmentsVo.setFileName(rs.getString(2));
					attachmentsVo.setSize(rs.getString(3));
					attachmentsVo.setDocumentTypeId(rs.getInt(4));
					attachmentsVo.setIsAkshar(rs.getBoolean(5));
					list.add(attachmentsVo);
				}
				logger.info("To get attachemnt details fetched successfully with size :: " + list.size());
			} catch (Exception e) {
				logger.info("Error in get attachemnt details:: ", e);
				throw new ApplicationException(e);
			} finally {
				closeResources(rs, preparedStatement, connection);
			}
		}
		return list;
	}

	public void changeStatusForAttachments(Integer typeId, String status, String type,String userId,String roleName) throws ApplicationException {
		logger.info("To Change the status in attachments for Type Id ::" + typeId);
		if (typeId != null && status != null) {
			Connection con =null;
			PreparedStatement preparedStatement =null;
			try  {
				con = getFinanceCommon();
				preparedStatement = con.prepareStatement(AttachmentsConstants.CHANGE_ATTACHMENTS_STATUS);
				preparedStatement.setString(1, status);
				preparedStatement.setDate(2, new Date(System.currentTimeMillis()));
				preparedStatement.setString(3, userId);
				preparedStatement.setString(4, roleName);
				preparedStatement.setString(5, type);
				preparedStatement.setInt(6, typeId);
				preparedStatement.executeUpdate();
				logger.info("Status chaneged successfully ");
			} catch (Exception e) {
				logger.info("Error in changeStatusForAttachments ", e);
				throw new ApplicationException(e);
			} finally {
				closeResources(null, preparedStatement, con);
			}
		}
	}

	public void changeStatusForSingleAttachment(Integer attachmentId, String status,String userId,String roleName) throws ApplicationException {
		logger.info("To Change the status in attachment for attachment Id ::" + attachmentId);
		if (attachmentId != null && status != null) {
			Connection con =null;
			PreparedStatement preparedStatement = null;
			try {
				con = getFinanceCommon();
				preparedStatement = con.prepareStatement(AttachmentsConstants.CHANGE_SINGLE_ATTACHMENT_STATUS);
				preparedStatement.setString(1, status);
				preparedStatement.setDate(2, new Date(System.currentTimeMillis()));
				preparedStatement.setString(3, userId);
				preparedStatement.setString(4, roleName);
				preparedStatement.setInt(5, attachmentId);
				preparedStatement.executeUpdate();
				logger.info("Status chaneged successfully ");
			} catch (Exception e) {
				logger.info("Error in changeStatusForAttachments ", e);
				throw new ApplicationException(e);
			} finally {
				closeResources(null, preparedStatement, con);
			}
		}
	}
}

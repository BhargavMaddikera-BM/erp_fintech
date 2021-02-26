package com.blackstrawai.template;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.ap.template.PurchaseOrderTemplateInformationVo;
import com.blackstrawai.ap.template.PurchaseOrderTemplateVo;
import com.blackstrawai.attachments.AttachmentsConstants;
import com.blackstrawai.attachments.AttachmentsDao;
import com.blackstrawai.attachments.AttachmentsVo;
import com.blackstrawai.attachments.UploadFileVo;
import com.blackstrawai.common.BaseDao;
import com.blackstrawai.common.CommonConstants;
import com.mysql.jdbc.Statement;

@Repository
public class ApTemplateDao extends BaseDao {

	private Logger logger = Logger.getLogger(ApTemplateDao.class);


	@Autowired
	private AttachmentsDao attachmentsDao;



	public PurchaseOrderTemplateVo createPurchaseOrderTemplate(PurchaseOrderTemplateVo purchaseOrderTemplateVo) throws ApplicationException {
		logger.info("To create a new PurchaseOrderTemplate" + purchaseOrderTemplateVo);
		Connection connection = null;
		if (purchaseOrderTemplateVo != null) {
			try {
				connection = getAccountsPayable();
				connection.setAutoCommit(false);
				boolean isTemplateNameUnique = templateNameUniqueCheck(purchaseOrderTemplateVo.getOrgId(),purchaseOrderTemplateVo.getTemplateInformation().getTemplateName(),ApTemplateConstants.TEMPLATE_TYPE_PURCHASE_ORDER);
				if (isTemplateNameUnique) {
					throw new ApplicationException("Template Exist for the Organization");
				}
				createPurchaseOrderTemplateInfo(purchaseOrderTemplateVo, connection);	
				logger.info("Attachment DAO values org id "+purchaseOrderTemplateVo.getOrgId());
				logger.info("Attachment DAO values user id "+purchaseOrderTemplateVo.getUserId());
				logger.info("Attachment DAO values  id "+ purchaseOrderTemplateVo.getTemplateId());
				logger.info("Attachment DAO values  rolename "+purchaseOrderTemplateVo.getRoleName());
				attachmentsDao.createAttachments(purchaseOrderTemplateVo.getOrgId(), purchaseOrderTemplateVo.getUserId(),
						purchaseOrderTemplateVo.getAttachmentLogo(), AttachmentsConstants.MODULE_TYPE_AP_PO_TEMPLATE_LOGO,
						purchaseOrderTemplateVo.getTemplateId(),purchaseOrderTemplateVo.getRoleName());
				attachmentsDao.createAttachments(purchaseOrderTemplateVo.getOrgId(),purchaseOrderTemplateVo.getUserId(),
						purchaseOrderTemplateVo.getAttachmentSign(), AttachmentsConstants.MODULE_TYPE_AP_PO_TEMPLATE_SIGN,
						purchaseOrderTemplateVo.getTemplateId(),purchaseOrderTemplateVo.getRoleName());
				connection.commit();
				logger.info("Succecssfully created purchase order in APPurchaseOrderTemplateDao");
			} catch (Exception e) {
				logger.info("Error in createPurchaseOrder:: ", e);
				try {
					connection.rollback();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					throw new ApplicationException(e1.getMessage());
				}
				logger.info("Before throw in dao :: " + e.getMessage());
				throw new ApplicationException(e.getMessage());
			}  finally {
				closeResources(null, null, connection);
			}
		}
		return purchaseOrderTemplateVo;
	}

	private boolean templateNameUniqueCheck(Integer orgId, String templateName,String templateType) throws ApplicationException{
		PreparedStatement preparedStatementnm=null;
		ResultSet templateNameCount = null;
		Connection connection = null;
		Integer cnt = null;
		logger.info("Template name unique check");
		logger.info("orgId "+orgId+" templateName "+templateName);
		try  {
			connection = getAccountsPayable();
			if(templateType.equalsIgnoreCase("purchaseorder")) {
				preparedStatementnm = connection.prepareStatement(ApTemplateConstants.TEMPLATE_NAME_PURCHASE_ORDER_UNIQUE_CHECK);
			}

			preparedStatementnm.setInt(1,orgId != null ? orgId : 0);
			preparedStatementnm.setString(2, templateName != null ? templateName : null);				

			templateNameCount = preparedStatementnm.executeQuery();
			if (templateNameCount.next()) {
				cnt = templateNameCount.getInt(1);
			}
			logger.info("Template name check completed");
		} catch (Exception e) {
			logger.info("Error in Template name check ", e);
		}finally{
			closeResources(templateNameCount, preparedStatementnm, connection);

		}

		return cnt == 0 ? false : true ;
	}

	private boolean templateNameUniqueCheckUpdate(Integer orgId, String templateName, Integer templateId , String templateType) throws ApplicationException{
		PreparedStatement preparedStatementnm=null;
		ResultSet templateNameCount = null;
		Connection connection = null;
		Integer cnt = null;
		logger.info("Template name unique check for update operation");
		logger.info("orgId "+orgId+" templateName "+templateName);
		try  {
			connection = getAccountsPayable();
			if(templateType.equalsIgnoreCase("purchaseorder")) {
				preparedStatementnm = connection.prepareStatement(ApTemplateConstants.TEMPLATE_NAME_UNIQUE_CHECK_UPD_PURCHASE_ORDER);
			}

			preparedStatementnm.setInt(1,orgId != null ? orgId : 0);
			preparedStatementnm.setString(2, templateName != null ? templateName : null);				
			preparedStatementnm.setInt(3, templateId != null ? templateId : 0);
			templateNameCount = preparedStatementnm.executeQuery();
			if (templateNameCount.next()) {
				cnt = templateNameCount.getInt(1);
			}
			logger.info("Template name check completed");
		} catch (Exception e) {
			logger.info("Error in Template name check ", e);
		}finally{
			closeResources(templateNameCount, preparedStatementnm, connection);

		}

		return cnt == 0 ? false : true ;
	}

	private PurchaseOrderTemplateVo createPurchaseOrderTemplateInfo(PurchaseOrderTemplateVo purchaseOrderTemplateVo, Connection connection)
			throws ApplicationException {
		logger.info("To insert into table ap_purchaseorder_template_information "+purchaseOrderTemplateVo.toString());

		if (purchaseOrderTemplateVo != null && purchaseOrderTemplateVo.getTemplateInformation() != null && purchaseOrderTemplateVo.getOrgId() != null) {
			logger.info("APPurchaseOrderTemplateDAO Contd1");
			PreparedStatement preparedStatementdel=null;
			try  {
				preparedStatementdel = connection.prepareStatement(
						ApTemplateConstants.DELETE_AP_PURCHASE_ORDER_TEMPLATE, Statement.RETURN_GENERATED_KEYS);
				preparedStatementdel.setString(1, "INA");
				preparedStatementdel.setInt(2,
						purchaseOrderTemplateVo.getTemplateInformation().getOrganizationId() != null
						? purchaseOrderTemplateVo.getTemplateInformation().getOrganizationId()
								: 0);
				preparedStatementdel.setInt(3,
						purchaseOrderTemplateVo.getTemplateInformation().getUserId() != null
						? purchaseOrderTemplateVo.getTemplateInformation().getUserId()
								: 0);				

				int rowAffecteddel = preparedStatementdel.executeUpdate();

				logger.info("Successfully deactivation of previous records in table AP_PurchaseOrder_Template " + rowAffecteddel);
			} catch (Exception e) {
				logger.info("Error in deactivation of previous records in table AP_PurchaseOrder_Template ", e);
				throw new ApplicationException(e.getMessage());
			}finally{
				closeResources(null, preparedStatementdel, null);
			}

			PreparedStatement preparedStatement = null;
			try {
				preparedStatement = connection.prepareStatement(
						ApTemplateConstants.INSERT_INTO_AP_PURCHASE_ORDER_TEMPLATE, Statement.RETURN_GENERATED_KEYS);
				logger.info("APPurchaseOrderTemplateDAO Contd2");

				preparedStatement.setString(1,
						purchaseOrderTemplateVo.getTemplateInformation().getLogoPosition() != null
						? purchaseOrderTemplateVo.getTemplateInformation().getLogoPosition()
								: null);				
				preparedStatement.setString(2,
						purchaseOrderTemplateVo.getTemplateInformation().getHeaderNotes() != null
						? purchaseOrderTemplateVo.getTemplateInformation().getHeaderNotes()
								: null);
				preparedStatement.setString(3,
						purchaseOrderTemplateVo.getTemplateInformation().getFooterSectionNotes() != null
						? purchaseOrderTemplateVo.getTemplateInformation().getFooterSectionNotes()
								: null);
				preparedStatement.setString(4,
						purchaseOrderTemplateVo.getTemplateInformation().getTermsAndContd() != null
						? purchaseOrderTemplateVo.getTemplateInformation().getTermsAndContd()
								: null);
				preparedStatement.setBoolean(5,
						purchaseOrderTemplateVo.getTemplateInformation().getBankAccInfo() != null
						? purchaseOrderTemplateVo.getTemplateInformation().getBankAccInfo()
								: false);
				preparedStatement.setString(6,
						purchaseOrderTemplateVo.getTemplateInformation().getFootNotes() != null
						? purchaseOrderTemplateVo.getTemplateInformation().getFootNotes()
								: null);
				preparedStatement.setString(7,
						purchaseOrderTemplateVo.getTemplateInformation().getFootNotePosition() != null
						? purchaseOrderTemplateVo.getTemplateInformation().getFootNotePosition()
								: null);
				preparedStatement.setBoolean(8,
						purchaseOrderTemplateVo.getTemplateInformation().getIsAnnex() != null
						? purchaseOrderTemplateVo.getTemplateInformation().getIsAnnex()
								: false);
				preparedStatement.setInt(9,
						purchaseOrderTemplateVo.getTemplateInformation().getUserId() != null
						? purchaseOrderTemplateVo.getTemplateInformation().getUserId()
								: 0);
				preparedStatement.setInt(10,
						purchaseOrderTemplateVo.getTemplateInformation().getOrganizationId() != null
						? purchaseOrderTemplateVo.getTemplateInformation().getOrganizationId()
								: 0);
				preparedStatement.setString(11, purchaseOrderTemplateVo.getRoleName()!= null
						? purchaseOrderTemplateVo.getRoleName()
								: null);
				preparedStatement.setString(12, "ACT");
				preparedStatement.setString(13, purchaseOrderTemplateVo.getTemplateInformation().getTemplateName() != null
						? purchaseOrderTemplateVo.getTemplateInformation().getTemplateName()
								: null);

				int rowAffected = preparedStatement.executeUpdate();
				if (rowAffected == 1) {
					try (ResultSet rs = preparedStatement.getGeneratedKeys()) {
						while (rs.next()) {
							purchaseOrderTemplateVo.setTemplateId(rs.getInt(1));
							//invoiceVo.setInvoiceId(rs.getInt(1));
						}
					}
				}


				logger.info("Successfully inserted into table AP_PurchaseOrder_Template " + purchaseOrderTemplateVo);
			} catch (Exception e) {
				logger.info("Error in inserting to table AP_PurchaseOrder_Template ", e);
				throw new ApplicationException(e.getMessage());
			}finally{
				closeResources(null, preparedStatement, null);
			}
		}
		return purchaseOrderTemplateVo;
	}

	public PurchaseOrderTemplateVo getPurchaseOrderTemplate(Integer orgId ,Integer templateId) throws ApplicationException {
		logger.info("To get Invoice template in APPurchaseOrderTemplateDao for:::" );
		PurchaseOrderTemplateVo purchaseOrderTemplateVo = null;
		Connection con=null;
		if (orgId != null && templateId != null ) {
			try{
				con = getAccountsPayable();
				purchaseOrderTemplateVo = new PurchaseOrderTemplateVo();
				purchaseOrderTemplateVo.setOrgId(orgId);
				Long orgContactNo = getOrgContactNo(orgId,con);
				logger.info("getPurchaseOrderTemplate info id"+ templateId);
				purchaseOrderTemplateVo.setTemplateId(templateId);
				PurchaseOrderTemplateInformationVo purchaseOrderGeneralTemplateInfo = getPurchaseOrderGeneralTemplateInfo(purchaseOrderTemplateVo, con);
				purchaseOrderTemplateVo.setTemplateInformation(purchaseOrderGeneralTemplateInfo);
				purchaseOrderTemplateVo.setRoleName(purchaseOrderGeneralTemplateInfo.getRoleName());
				logger.info("getPurchaseOrderTemplate info "+purchaseOrderTemplateVo.getTemplateInformation());

				List<UploadFileVo> uploadFileVos = new ArrayList<UploadFileVo>();

				for (AttachmentsVo attachments : attachmentsDao.getAttachmentDetails(templateId,
						AttachmentsConstants.MODULE_TYPE_AP_PO_TEMPLATE_LOGO)) {

					UploadFileVo uploadFileVo = new UploadFileVo();
					uploadFileVo.setId(attachments.getId());
					// uploadFileVo.setData(attachments.getD);
					uploadFileVo.setName(attachments.getFileName());
					uploadFileVo.setSize(attachments.getSize());
					uploadFileVos.add(uploadFileVo);
					logger.info("getPurchaseOrderTemplate logo "+ uploadFileVo);
				}
				purchaseOrderTemplateVo.setAttachmentLogo(uploadFileVos);
				logger.info("getPurchaseOrderTemplate logo "+ uploadFileVos);
				logger.info("getPurchaseOrderTemplate logo final "+ purchaseOrderTemplateVo.getAttachmentLogo());

				List<UploadFileVo> uploadFileVos2 = new ArrayList<UploadFileVo>();
				for (AttachmentsVo attachments : attachmentsDao.getAttachmentDetails(templateId,
						AttachmentsConstants.MODULE_TYPE_AP_PO_TEMPLATE_SIGN)) {
					UploadFileVo uploadFileVo2 = new UploadFileVo();
					uploadFileVo2.setId(attachments.getId());
					uploadFileVo2.setName(attachments.getFileName());
					uploadFileVo2.setSize(attachments.getSize());
					uploadFileVos2.add(uploadFileVo2);
				}
				purchaseOrderTemplateVo.setAttachmentSign(uploadFileVos2);
				purchaseOrderTemplateVo.setOrgContactNo(orgContactNo);
			} catch (Exception e) {
				logger.info("Error in getPurchaseorderTemplate:: ", e);
				throw new ApplicationException(e);
			}finally{
				closeResources(null, null, con);
			}
		}
		return purchaseOrderTemplateVo;
	}



	private Long getOrgContactNo(Integer orgId, Connection con) throws ApplicationException {
		Long orgContactNo = null ;
		PreparedStatement preparedStatement = null;
		ResultSet rs =null;
		try {
			preparedStatement = con.prepareStatement(ApTemplateConstants.GET_ORG_CONTACT_NO);
			preparedStatement.setInt(1, orgId);
			preparedStatement.setString(2, "ACT");
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				orgContactNo = rs.getLong(1)	;		
			}
			logger.info("getOrgContactNo "+ orgContactNo);
		} catch (Exception e) {
			logger.info("Error in getOrgContactNo ", e);
			throw new ApplicationException(e);
		}finally{
			closeResources(rs, preparedStatement, null);
		}		
		return orgContactNo;
	}

	private Integer getTemplateId(Integer orgId, String templateType, Connection con) throws ApplicationException {
		Integer templateId = 0;
		PreparedStatement preparedStatement = null;
		ResultSet rs =null;
		try {
			if(templateType.equalsIgnoreCase("purchaseorder")) {
				preparedStatement = con.prepareStatement(ApTemplateConstants.GET_TEMPLATE_ID_PURCHASE_ORDER);
			}
			preparedStatement.setInt(1, orgId);
			preparedStatement.setString(2, "ACT");
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				templateId = rs.getInt(1)	;		
			}
			logger.info("getTemplateId "+ templateId);
		} catch (Exception e) {
			logger.info("Error in getTemplateId ", e);
			throw new ApplicationException(e);
		}finally{
			closeResources(rs, preparedStatement, null);
		}
		return templateId ;
	}

	private PurchaseOrderTemplateInformationVo getPurchaseOrderGeneralTemplateInfo(PurchaseOrderTemplateVo purchaseOrderTemplateVo, Connection con) throws ApplicationException {
		PurchaseOrderTemplateInformationVo generalInfoTempVo = new PurchaseOrderTemplateInformationVo();
		ResultSet rs = null;
		PreparedStatement preparedStatement =null;
		try {
			preparedStatement = con.prepareStatement(ApTemplateConstants.GET_PURCHASE_ORDER_TEMPLATE_INFO); 
			preparedStatement.setInt(1, purchaseOrderTemplateVo.getOrgId());
			preparedStatement.setInt(2, purchaseOrderTemplateVo.getTemplateId());
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				generalInfoTempVo.setLogoPosition(rs.getString(1));
				generalInfoTempVo.setHeaderNotes(rs.getString(2));
				generalInfoTempVo.setFooterSectionNotes(rs.getString(3));
				generalInfoTempVo.setTermsAndContd(rs.getString(4));
				generalInfoTempVo.setBankAccInfo(rs.getBoolean(5));
				generalInfoTempVo.setFootNotes(rs.getString(6));
				generalInfoTempVo.setFootNotePosition(rs.getString(7));
				generalInfoTempVo.setIsAnnex(rs.getBoolean(8));
				generalInfoTempVo.setUserId(rs.getInt(9));
				generalInfoTempVo.setOrganizationId(rs.getInt(10));
				generalInfoTempVo.setRoleName(rs.getString(11));
				generalInfoTempVo.setStatus(rs.getString(12));
				generalInfoTempVo.setId(rs.getInt(13));
				generalInfoTempVo.setTemplateName(rs.getString(14));
			}

		} catch (Exception e) {
			logger.info("Error in getPurchaseOrderTemplate ", e);
			throw new ApplicationException(e);
		}finally{
			closeResources(rs, preparedStatement, null);
		}

		logger.info("getPurchaseOrderGeneralTemplateInfo " + generalInfoTempVo);
		return generalInfoTempVo;
	}

	public PurchaseOrderTemplateVo updatePurchaseOrderTemplate(PurchaseOrderTemplateVo purchaseOrderTemplateVo) throws ApplicationException {
		logger.info("To update a  Purchase Order template :::" + purchaseOrderTemplateVo);
		if (purchaseOrderTemplateVo != null && purchaseOrderTemplateVo.getTemplateId() != null) {
			PreparedStatement preparedStatementdel=null;
			Connection connection = null;
			try {
				connection = getAccountsPayable();
				connection.setAutoCommit(false);
				boolean isTemplateNameUnique = templateNameUniqueCheckUpdate(
						purchaseOrderTemplateVo.getOrgId(),purchaseOrderTemplateVo.getTemplateInformation().getTemplateName(),purchaseOrderTemplateVo.getTemplateId(),ApTemplateConstants.TEMPLATE_TYPE_PURCHASE_ORDER);
				if (isTemplateNameUnique) {
					throw new ApplicationException("Template Name already exists");
				}
				updatePurchaseOrderTemplateInfo(purchaseOrderTemplateVo, connection);	

				preparedStatementdel = connection.prepareStatement(
						ApTemplateConstants.DELETE_AP_PURCHASE_ORDER_TEMPLATE_UPD_PURCHASE_ORDER, Statement.RETURN_GENERATED_KEYS);
				preparedStatementdel.setString(1, "INA");
				preparedStatementdel.setInt(2,
						purchaseOrderTemplateVo.getOrgId() != null
						? purchaseOrderTemplateVo.getOrgId()
								: 0);
				preparedStatementdel.setString(3,
						purchaseOrderTemplateVo.getUserId() != null
						? purchaseOrderTemplateVo.getUserId() 
								: null);	
				preparedStatementdel.setInt(4,
						purchaseOrderTemplateVo.getTemplateId()!= null
						? purchaseOrderTemplateVo.getTemplateId()
								: 0);

				int rowAffecteddel = preparedStatementdel.executeUpdate();

				logger.info("Successfully deactivation of previous records in table AP_PurchaseOrder_Template " + rowAffecteddel);

				if (purchaseOrderTemplateVo.getAttachmentLogo() != null && purchaseOrderTemplateVo.getAttachmentLogo().size() > 0) {
					attachmentsDao.createAttachments(purchaseOrderTemplateVo.getOrgId(), purchaseOrderTemplateVo.getUserId(),
							purchaseOrderTemplateVo.getAttachmentLogo(), AttachmentsConstants.MODULE_TYPE_AP_PO_TEMPLATE_LOGO,
							purchaseOrderTemplateVo.getTemplateId(),purchaseOrderTemplateVo.getRoleName());

				}
				if (purchaseOrderTemplateVo.getAttachmentSign() != null && purchaseOrderTemplateVo.getAttachmentSign().size() > 0) {
					attachmentsDao.createAttachments(purchaseOrderTemplateVo.getOrgId(),purchaseOrderTemplateVo.getUserId(),
							purchaseOrderTemplateVo.getAttachmentSign(), AttachmentsConstants.MODULE_TYPE_AP_PO_TEMPLATE_SIGN,
							purchaseOrderTemplateVo.getTemplateId(),purchaseOrderTemplateVo.getRoleName());				
				}

				if (purchaseOrderTemplateVo.getAttachmentsToRemove() != null && purchaseOrderTemplateVo.getAttachmentsToRemove().size() > 0) {
					for (Integer attachmentId : purchaseOrderTemplateVo.getAttachmentsToRemove()) {
						attachmentsDao.changeStatusForSingleAttachment(attachmentId, CommonConstants.STATUS_AS_DELETE,
								purchaseOrderTemplateVo.getUserId(), purchaseOrderTemplateVo.getRoleName());
					}
				}
				connection.commit();
				logger.info("Succecssfully updated purchase order template  in APInvoiceTemplateDao");
			} catch (Exception e) {
				logger.info("Error in purchase order template update:: ", e);
				try {
					connection.rollback();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					throw new ApplicationException(e1.getMessage());
				}
				throw new ApplicationException(e.getMessage());
			} finally {
				closeResources(null, null, connection);
			}
		}
		return purchaseOrderTemplateVo;
	}

	private PurchaseOrderTemplateVo updatePurchaseOrderTemplateInfo(PurchaseOrderTemplateVo purchaseOrderTemplateVo, Connection connection)
			throws ApplicationException {
		logger.info("To update into table purchaseorder_template_information ");
		if (purchaseOrderTemplateVo != null && purchaseOrderTemplateVo.getTemplateId() != null && purchaseOrderTemplateVo.getTemplateInformation() != null) {
			if (purchaseOrderTemplateVo.getTemplateId() == null) {
				throw new ApplicationException("Template Id is mandatory");
			}
			PreparedStatement preparedStatement =null;
			try {
				preparedStatement = connection.prepareStatement(ApTemplateConstants.MODIFY_PURCHASE_ORDER_TEMPLATE_INFO_PO);

				preparedStatement.setString(1,
						purchaseOrderTemplateVo.getTemplateInformation().getLogoPosition() != null
						? purchaseOrderTemplateVo.getTemplateInformation().getLogoPosition()
								: null);				
				preparedStatement.setString(2,
						purchaseOrderTemplateVo.getTemplateInformation().getHeaderNotes() != null
						? purchaseOrderTemplateVo.getTemplateInformation().getHeaderNotes()
								: null);
				preparedStatement.setString(3,
						purchaseOrderTemplateVo.getTemplateInformation().getFooterSectionNotes() != null
						? purchaseOrderTemplateVo.getTemplateInformation().getFooterSectionNotes()
								: null);
				preparedStatement.setString(4,
						purchaseOrderTemplateVo.getTemplateInformation().getTermsAndContd() != null
						? purchaseOrderTemplateVo.getTemplateInformation().getTermsAndContd()
								: null);
				preparedStatement.setBoolean(5,
						purchaseOrderTemplateVo.getTemplateInformation().getBankAccInfo() != null
						? purchaseOrderTemplateVo.getTemplateInformation().getBankAccInfo()
								: false);
				preparedStatement.setString(6,
						purchaseOrderTemplateVo.getTemplateInformation().getFootNotes() != null
						? purchaseOrderTemplateVo.getTemplateInformation().getFootNotes()
								: null);
				preparedStatement.setString(7,
						purchaseOrderTemplateVo.getTemplateInformation().getFootNotePosition() != null
						? purchaseOrderTemplateVo.getTemplateInformation().getFootNotePosition()
								: null);
				preparedStatement.setBoolean(8,
						purchaseOrderTemplateVo.getTemplateInformation().getIsAnnex() != null
						? purchaseOrderTemplateVo.getTemplateInformation().getIsAnnex()
								: false);
				preparedStatement.setTimestamp(9,new Timestamp(System.currentTimeMillis()));
				preparedStatement.setInt(10,
						purchaseOrderTemplateVo.getTemplateInformation().getOrganizationId() != null
						? purchaseOrderTemplateVo.getTemplateInformation().getOrganizationId()
								: 0);
				preparedStatement.setString(11,
						purchaseOrderTemplateVo.getTemplateInformation().getUpdateUserId() != null
						? purchaseOrderTemplateVo.getTemplateInformation().getUpdateUserId()
								: null);
				preparedStatement.setString(12, purchaseOrderTemplateVo.getRoleName()!= null
						? purchaseOrderTemplateVo.getRoleName()
								: null);
				preparedStatement.setString(13, purchaseOrderTemplateVo.getStatus()!= null
						? purchaseOrderTemplateVo.getStatus()
								: null);
				preparedStatement.setString(14, purchaseOrderTemplateVo.getTemplateInformation().getTemplateName()!= null
						? purchaseOrderTemplateVo.getTemplateInformation().getTemplateName()
								: null);
				preparedStatement.setInt(15, purchaseOrderTemplateVo.getTemplateId());
				preparedStatement.executeUpdate();


				logger.info("Successfully updated into table purchaseorder_template_information ");
			} catch (Exception e) {
				logger.info("Error in updating  to table purchaseorder_template_information ", e);
				throw new ApplicationException(e.getMessage());
			}finally{
				closeResources(null, preparedStatement, null);
			}
		}

		return purchaseOrderTemplateVo;
	}

	public List<PurchaseOrderTemplateInformationVo> getPurchaseOrderTemplateList(Integer orgId, String userId, String roleName) throws ApplicationException {
		List<PurchaseOrderTemplateInformationVo> generalInfoTempVos = new ArrayList<PurchaseOrderTemplateInformationVo>();
		PurchaseOrderTemplateInformationVo generalInfoTempVo =null;
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement preparedStatement =null;
		try {
			con = getAccountsPayable();
			preparedStatement = con.prepareStatement(ApTemplateConstants.GET_PURCHASE_ORDER_TEMPLATE_INFO_ALL); 
			preparedStatement.setInt(1,orgId);
			preparedStatement.setString(2, userId);
			preparedStatement.setString(3, roleName);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				generalInfoTempVo = new PurchaseOrderTemplateInformationVo();
				generalInfoTempVo.setLogoPosition(rs.getString(1));
				generalInfoTempVo.setHeaderNotes(rs.getString(2));
				generalInfoTempVo.setFooterSectionNotes(rs.getString(3));
				generalInfoTempVo.setTermsAndContd(rs.getString(4));
				generalInfoTempVo.setBankAccInfo(rs.getBoolean(5));
				generalInfoTempVo.setFootNotes(rs.getString(6));
				generalInfoTempVo.setFootNotePosition(rs.getString(7));
				generalInfoTempVo.setIsAnnex(rs.getBoolean(8));
				generalInfoTempVo.setUserId(rs.getInt(9));
				generalInfoTempVo.setOrganizationId(rs.getInt(10));
				generalInfoTempVo.setRoleName(rs.getString(11));
				generalInfoTempVo.setStatus(rs.getString(12));
				generalInfoTempVo.setId(rs.getInt(13));
				generalInfoTempVo.setTemplateName(rs.getString(14));
				//generalInfoTempVo.setUpdated_time(rs.getString(12));	
				generalInfoTempVos.add(generalInfoTempVo);
			}

		} catch (Exception e) {
			logger.info("Error in getInvoiceTemplate ", e);
			throw new ApplicationException(e);
		}finally{
			closeResources(rs, preparedStatement, con);
		}

		logger.info("getInvoiceGeneralTemplateInfo " + generalInfoTempVos);
		return generalInfoTempVos;
	}

	public List<PurchaseOrderTemplateInformationVo> getAllPurchaseOrderTemplatePerOrganization(Integer orgId) throws ApplicationException {
		List<PurchaseOrderTemplateInformationVo> generalInfoTempVos = new ArrayList<PurchaseOrderTemplateInformationVo>();
		PurchaseOrderTemplateInformationVo generalInfoTempVo =null;
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement preparedStatement =null;
		try {
			con = getAccountsPayable();
			preparedStatement = con.prepareStatement(ApTemplateConstants.GET_PURCHASE_ORDER_TEMPLATE_INFO_ORGANIZATION); 
			preparedStatement.setInt(1,orgId);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				generalInfoTempVo = new PurchaseOrderTemplateInformationVo();
				generalInfoTempVo.setLogoPosition(rs.getString(1));
				generalInfoTempVo.setHeaderNotes(rs.getString(2));
				generalInfoTempVo.setFooterSectionNotes(rs.getString(3));
				generalInfoTempVo.setTermsAndContd(rs.getString(4));
				generalInfoTempVo.setBankAccInfo(rs.getBoolean(5));
				generalInfoTempVo.setFootNotes(rs.getString(6));
				generalInfoTempVo.setFootNotePosition(rs.getString(7));
				generalInfoTempVo.setIsAnnex(rs.getBoolean(8));
				generalInfoTempVo.setUserId(rs.getInt(9));
				generalInfoTempVo.setOrganizationId(rs.getInt(10));
				generalInfoTempVo.setRoleName(rs.getString(11));
				generalInfoTempVo.setStatus(rs.getString(12));
				generalInfoTempVo.setId(rs.getInt(13));
				generalInfoTempVo.setTemplateName(rs.getString(14));
				generalInfoTempVos.add(generalInfoTempVo);
			}

		} catch (Exception e) {
			logger.info("Error in getInvoiceTemplate ", e);
			throw new ApplicationException(e);
		}finally{
			closeResources(rs, preparedStatement, con);
		}

		logger.info("getInvoiceGeneralTemplateInfo " + generalInfoTempVos);
		return generalInfoTempVos;
	}

	public PurchaseOrderTemplateVo getDefaultPurchaseOrderTemplateForOrg(Integer orgId) throws ApplicationException {
		logger.info("To get Invoice template in APPurchaseOrderTemplateDao for:::" );
		PurchaseOrderTemplateVo purchaseOrderTemplateVo = null;
		Connection con=null;
		if (orgId != null  ) {
			try{
				con = getAccountsPayable();
				purchaseOrderTemplateVo = new PurchaseOrderTemplateVo();
				purchaseOrderTemplateVo.setOrgId(orgId);
				Long orgContactNo = getOrgContactNo(orgId,con);
				Integer templateId = getTemplateId(orgId,ApTemplateConstants.TEMPLATE_TYPE_PURCHASE_ORDER,con);
				logger.info("getInvoiceTemplate info id"+ templateId);
				purchaseOrderTemplateVo.setTemplateId(templateId);
				PurchaseOrderTemplateInformationVo purchaseOrderGeneralTemplateInfo = getPurchaseOrderGeneralTemplateInfo(purchaseOrderTemplateVo, con);
				purchaseOrderTemplateVo.setTemplateInformation(purchaseOrderGeneralTemplateInfo);
				purchaseOrderTemplateVo.setRoleName(purchaseOrderGeneralTemplateInfo.getRoleName());
				logger.info("getPurchaseOrderTemplate info "+purchaseOrderTemplateVo.getTemplateInformation());

				List<UploadFileVo> uploadFileVos = new ArrayList<UploadFileVo>();

				for (AttachmentsVo attachments : attachmentsDao.getAttachmentDetails(templateId,
						AttachmentsConstants.MODULE_TYPE_AP_PO_TEMPLATE_LOGO)) {

					UploadFileVo uploadFileVo = new UploadFileVo();
					uploadFileVo.setId(attachments.getId());
					// uploadFileVo.setData(attachments.getD);
					uploadFileVo.setName(attachments.getFileName());
					uploadFileVo.setSize(attachments.getSize());
					uploadFileVos.add(uploadFileVo);
					logger.info("getPurchaseOrderTemplate logo "+ uploadFileVo);
				}
				purchaseOrderTemplateVo.setAttachmentLogo(uploadFileVos);
				logger.info("getPurchaseOrderTemplate logo "+ uploadFileVos);
				logger.info("getPurchaseOrderTemplate logo final "+ purchaseOrderTemplateVo.getAttachmentLogo());

				List<UploadFileVo> uploadFileVos2 = new ArrayList<UploadFileVo>();
				for (AttachmentsVo attachments : attachmentsDao.getAttachmentDetails(templateId,
						AttachmentsConstants.MODULE_TYPE_AP_PO_TEMPLATE_SIGN)) {
					UploadFileVo uploadFileVo2 = new UploadFileVo();
					uploadFileVo2.setId(attachments.getId());
					uploadFileVo2.setName(attachments.getFileName());
					uploadFileVo2.setSize(attachments.getSize());
					uploadFileVos2.add(uploadFileVo2);
				}
				purchaseOrderTemplateVo.setAttachmentSign(uploadFileVos2);
				purchaseOrderTemplateVo.setOrgContactNo(orgContactNo);
			} catch (Exception e) {
				logger.info("Error in getPurchaseOrderTemplate:: ", e);
				throw new ApplicationException(e);
			}finally{
				closeResources(null, null, con);
			}
		}
		return purchaseOrderTemplateVo;
	}



	public List<Integer> getTemplateIds(Integer orgId ,  String templateType ) throws ApplicationException {
		logger.info("To retrieve all the template Ids ");
		List<Integer> templateIds = new ArrayList<Integer>();
		Connection con = null;
		ResultSet rs = null;
		Integer templateId = null;
		PreparedStatement preparedStatement =null;
		try {
			con = getAccountsPayable();

			if(templateType.equalsIgnoreCase("purchaseorder")) {
				preparedStatement = con.prepareStatement(ApTemplateConstants.GET_TEMPLATE_IDS_FOR_ORG_PURCHASE_ORDER); 
			} 
			preparedStatement.setInt(1,orgId);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				templateId = rs.getInt(1);
				templateIds.add(templateId);
			}

		} catch (Exception e) {
			logger.info("Error in getTemplateIds ", e);
			throw new ApplicationException(e);
		}finally{
			closeResources(rs, preparedStatement, con);
		}

		logger.info("getTemplateIds " + templateIds);
		return templateIds;
	}

	public PurchaseOrderTemplateVo activateDeActivatePurchaseOrderTemplate(Integer orgId, String orgName,
			String userId, Integer id, String roleName,
			String status) throws ApplicationException, SQLException {
		logger.info("activateDeActivatePurchaseOrderTemplate DAO :::");


		if (orgId != null && userId != null && id != null && status != null ) {
			if(status.equalsIgnoreCase("Activate")) {
				Connection con = null;
				PreparedStatement preparedStatementdel=null;
				try  {
					logger.info(" deactivate - orgid : "+orgId+" userId : "+userId+" status "+ status+" id "+id);
					con = getAccountsPayable();
					preparedStatementdel = con.prepareStatement(
							ApTemplateConstants.DELETE_AP_PURCHASE_ORDER_TEMPLATE, Statement.RETURN_GENERATED_KEYS);
					preparedStatementdel.setString(1, "INA");
					preparedStatementdel.setInt(2,orgId);
					preparedStatementdel.setString(3,userId);				

					int rowAffecteddel = preparedStatementdel.executeUpdate();

					logger.info("Successfully deactivation of previous records in table AP_PurchaseOrder_Template " + rowAffecteddel);
				} catch (Exception e) {
					logger.info("Error in deactivation of previous records in table AP_Invoice_Template ", e);
					throw new ApplicationException(e.getMessage());
				}finally{
					closeResources(null, preparedStatementdel, con);
				}

				PreparedStatement preparedStatementact=null;
				Connection conn = null;
				try  {
					logger.info(" activate - orgid : "+orgId+" userId : "+userId+" status "+ status+" id "+id);
					conn = getAccountsPayable();
					preparedStatementact = conn.prepareStatement(
							ApTemplateConstants.DELETE_PURCHASE_ORDER_TEMPLATE_ID, Statement.RETURN_GENERATED_KEYS);
					preparedStatementact.setString(1,"ACT");
					preparedStatementact.setInt(2,orgId);
					preparedStatementact.setString(3,userId);	
					preparedStatementact.setInt(4,id);

					int rowAffecteddel = preparedStatementact.executeUpdate();

					logger.info("Successfully activation of template in table AP_PurchaseOrder_Template " + rowAffecteddel);

				} catch (Exception e) {
					logger.info("Error in activation  of template in table AP_PurchaseOrder_Template ", e);
					throw new ApplicationException(e.getMessage());
				}finally{
					closeResources(null, preparedStatementdel, conn);					
				}
			}  else {
				PreparedStatement preparedStatementdeact=null;
				Connection conn = null;
				try  {
					logger.info(" activate - orgid : "+orgId+" userId : "+userId+" status "+ status+" id "+id);
					conn = getAccountsPayable();
					preparedStatementdeact = conn.prepareStatement(
							ApTemplateConstants.DELETE_PURCHASE_ORDER_TEMPLATE_ID, Statement.RETURN_GENERATED_KEYS);
					preparedStatementdeact.setString(1,"INA");
					preparedStatementdeact.setInt(2,orgId);
					preparedStatementdeact.setString(3,userId);	
					preparedStatementdeact.setInt(4,id);

					int rowAffecteddel = preparedStatementdeact.executeUpdate();

					logger.info("Successfully deactivation of template in table AP_PurchaseOrder_Template " + rowAffecteddel);

				} catch (Exception e) {
					logger.info("Error in deactivation  of template in table AP_PurchaseOrder_Template ", e);
					throw new ApplicationException(e.getMessage());
				}finally{
					closeResources(null, preparedStatementdeact, conn);					
				}
			}

		}
		return getPurchaseOrderTemplate(orgId,id);
	}

	}







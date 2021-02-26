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
import com.blackstrawai.ar.template.CreditNoteTemplateVo;
import com.blackstrawai.ar.template.CreditNoteTemplateinformationVo;
import com.blackstrawai.ar.template.InvoiceTemplateVo;
import com.blackstrawai.ar.template.InvoiceTemplateinformationVo;
import com.blackstrawai.attachments.AttachmentsConstants;
import com.blackstrawai.attachments.AttachmentsDao;
import com.blackstrawai.attachments.AttachmentsVo;
import com.blackstrawai.attachments.UploadFileVo;
import com.blackstrawai.common.BaseDao;
import com.blackstrawai.common.CommonConstants;
import com.mysql.jdbc.Statement;

@Repository
public class ArTemplateDao extends BaseDao {

	private Logger logger = Logger.getLogger(ArTemplateDao.class);


	@Autowired
	private AttachmentsDao attachmentsDao;



	public InvoiceTemplateVo createInvoiceTemplate(InvoiceTemplateVo invoiceTemplateVo) throws ApplicationException {
		logger.info("To create a new InvoiceTemplate" + invoiceTemplateVo);
		Connection connection = null;
		if (invoiceTemplateVo != null) {
			try {
				connection = getAccountsReceivableConnection();
				connection.setAutoCommit(false);
				boolean isTemplateNameUnique = templateNameUniqueCheck(invoiceTemplateVo.getOrgId(),invoiceTemplateVo.getTemplateInformation().getTemplateName(),ArTemplateConstants.TEMPLATE_TYPE_INV);
				if (isTemplateNameUnique) {
					throw new ApplicationException("Template Exist for the Organization");
				}
				createInvoiceTemplateInfo(invoiceTemplateVo, connection);	
				logger.info("Attachment DAO values org id "+invoiceTemplateVo.getOrgId());
				logger.info("Attachment DAO values user id "+invoiceTemplateVo.getUserId());
				logger.info("Attachment DAO values  id "+ invoiceTemplateVo.getTemplateId());
				logger.info("Attachment DAO values  rolename "+invoiceTemplateVo.getRoleName());
				attachmentsDao.createAttachments(invoiceTemplateVo.getOrgId(), invoiceTemplateVo.getUserId(),
						invoiceTemplateVo.getAttachmentLogo(), AttachmentsConstants.MODULE_TYPE_AR_INVOICE_TEMPLATE_LOGO,
						invoiceTemplateVo.getTemplateId(),invoiceTemplateVo.getRoleName());
				attachmentsDao.createAttachments(invoiceTemplateVo.getOrgId(),invoiceTemplateVo.getUserId(),
						invoiceTemplateVo.getAttachmentSign(), AttachmentsConstants.MODULE_TYPE_AR_INVOICE_TEMPLATE_SIGN,
						invoiceTemplateVo.getTemplateId(),invoiceTemplateVo.getRoleName());
				connection.commit();
				logger.info("Succecssfully created invoice in ARInvoiceTemplateDao");
			} catch (Exception e) {
				logger.info("Error in createInvoice:: ", e);
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
		return invoiceTemplateVo;
	}

	private boolean templateNameUniqueCheck(Integer orgId, String templateName,String templateType) throws ApplicationException{
		PreparedStatement preparedStatementnm=null;
		ResultSet templateNameCount = null;
		Connection connection = null;
		Integer cnt = null;
		logger.info("Template name unique check");
		logger.info("orgId "+orgId+" templateName "+templateName);
		try  {
			connection = getAccountsReceivableConnection();
			if(templateType.equalsIgnoreCase("invoice")) {
				preparedStatementnm = connection.prepareStatement(ArTemplateConstants.TEMPLATE_NAME_INV_UNIQUE_CHECK);
			}else {
				preparedStatementnm = connection.prepareStatement(ArTemplateConstants.TEMPLATE_NAME_CN_UNIQUE_CHECK);
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
			connection = getAccountsReceivableConnection();
			if(templateType.equalsIgnoreCase("invoice")) {
				preparedStatementnm = connection.prepareStatement(ArTemplateConstants.TEMPLATE_NAME_UNIQUE_CHECK_UPD_INV);
			}else {
				preparedStatementnm = connection.prepareStatement(ArTemplateConstants.TEMPLATE_NAME_UNIQUE_CHECK_UPD_CN);
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

	private InvoiceTemplateVo createInvoiceTemplateInfo(InvoiceTemplateVo invoiceTemplateVo, Connection connection)
			throws ApplicationException {
		logger.info("To insert into table ar_invoice_template_information "+invoiceTemplateVo.toString());

		if (invoiceTemplateVo != null && invoiceTemplateVo.getTemplateInformation() != null && invoiceTemplateVo.getOrgId() != null) {
			logger.info("ARInvoiceTemplateDAO Contd1");
			PreparedStatement preparedStatementdel=null;
			try  {
				preparedStatementdel = connection.prepareStatement(
						ArTemplateConstants.DELETE_AR_INV_TEMPLATE, Statement.RETURN_GENERATED_KEYS);
				preparedStatementdel.setString(1, "INA");
				preparedStatementdel.setInt(2,
						invoiceTemplateVo.getTemplateInformation().getOrganizationId() != null
						? invoiceTemplateVo.getTemplateInformation().getOrganizationId()
								: 0);
				preparedStatementdel.setInt(3,
						invoiceTemplateVo.getTemplateInformation().getUserId() != null
						? invoiceTemplateVo.getTemplateInformation().getUserId()
								: 0);				

				int rowAffecteddel = preparedStatementdel.executeUpdate();

				logger.info("Successfully deactivation of previous records in table AR_Invoice_Template " + rowAffecteddel);
			} catch (Exception e) {
				logger.info("Error in deactivation of previous records in table AR_Invoice_Template ", e);
				throw new ApplicationException(e.getMessage());
			}finally{
				closeResources(null, preparedStatementdel, null);
			}

			PreparedStatement preparedStatement = null;
			try {
				preparedStatement = connection.prepareStatement(
						ArTemplateConstants.INSERT_INTO_AR_INV_TEMPLATE, Statement.RETURN_GENERATED_KEYS);
				logger.info("ARInvoiceTemplateDAO Contd2");

				preparedStatement.setString(1,
						invoiceTemplateVo.getTemplateInformation().getLogoPosition() != null
						? invoiceTemplateVo.getTemplateInformation().getLogoPosition()
								: null);				
				preparedStatement.setString(2,
						invoiceTemplateVo.getTemplateInformation().getHeaderNotes() != null
						? invoiceTemplateVo.getTemplateInformation().getHeaderNotes()
								: null);
				preparedStatement.setString(3,
						invoiceTemplateVo.getTemplateInformation().getFooterSectionNotes() != null
						? invoiceTemplateVo.getTemplateInformation().getFooterSectionNotes()
								: null);
				preparedStatement.setString(4,
						invoiceTemplateVo.getTemplateInformation().getTermsAndContd() != null
						? invoiceTemplateVo.getTemplateInformation().getTermsAndContd()
								: null);
				preparedStatement.setBoolean(5,
						invoiceTemplateVo.getTemplateInformation().getBankAccInfo() != null
						? invoiceTemplateVo.getTemplateInformation().getBankAccInfo()
								: false);
				preparedStatement.setString(6,
						invoiceTemplateVo.getTemplateInformation().getFootNotes() != null
						? invoiceTemplateVo.getTemplateInformation().getFootNotes()
								: null);
				preparedStatement.setString(7,
						invoiceTemplateVo.getTemplateInformation().getFootNotePosition() != null
						? invoiceTemplateVo.getTemplateInformation().getFootNotePosition()
								: null);
				preparedStatement.setBoolean(8,
						invoiceTemplateVo.getTemplateInformation().getIsAnnex() != null
						? invoiceTemplateVo.getTemplateInformation().getIsAnnex()
								: false);
				preparedStatement.setInt(9,
						invoiceTemplateVo.getTemplateInformation().getUserId() != null
						? invoiceTemplateVo.getTemplateInformation().getUserId()
								: 0);
				preparedStatement.setInt(10,
						invoiceTemplateVo.getTemplateInformation().getOrganizationId() != null
						? invoiceTemplateVo.getTemplateInformation().getOrganizationId()
								: 0);
				preparedStatement.setString(11, invoiceTemplateVo.getRoleName()!= null
						? invoiceTemplateVo.getRoleName()
								: null);
				preparedStatement.setString(12, "ACT");
				preparedStatement.setString(13, invoiceTemplateVo.getTemplateInformation().getTemplateName() != null
						? invoiceTemplateVo.getTemplateInformation().getTemplateName()
								: null);

				int rowAffected = preparedStatement.executeUpdate();
				if (rowAffected == 1) {
					try (ResultSet rs = preparedStatement.getGeneratedKeys()) {
						while (rs.next()) {
							invoiceTemplateVo.setTemplateId(rs.getInt(1));
							//invoiceVo.setInvoiceId(rs.getInt(1));
						}
					}
				}


				logger.info("Successfully inserted into table AR_Invoice_Template " + invoiceTemplateVo);
			} catch (Exception e) {
				logger.info("Error in inserting to table AR_Invoice_Template ", e);
				throw new ApplicationException(e.getMessage());
			}finally{
				closeResources(null, preparedStatement, null);
			}
		}
		return invoiceTemplateVo;
	}

	public InvoiceTemplateVo getInvoiceTemplate(Integer orgId ,Integer templateId) throws ApplicationException {
		logger.info("To get Invoice template in ARInvoiceTemplateDao for:::" );
		InvoiceTemplateVo invoiceTemplateVo = null;
		Connection con=null;
		if (orgId != null && templateId != null ) {
			try{
				con = getAccountsReceivableConnection();
				invoiceTemplateVo = new InvoiceTemplateVo();
				invoiceTemplateVo.setOrgId(orgId);
				Long orgContactNo = getOrgContactNo(orgId,con);
				logger.info("getInvoiceTemplate info id"+ templateId);
				invoiceTemplateVo.setTemplateId(templateId);
				InvoiceTemplateinformationVo invoiceGeneralTemplateInfo = getInvoiceGeneralTemplateInfo(invoiceTemplateVo, con);
				invoiceTemplateVo.setTemplateInformation(invoiceGeneralTemplateInfo);
				invoiceTemplateVo.setRoleName(invoiceGeneralTemplateInfo.getRoleName());
				logger.info("getInvoiceTemplate info "+invoiceTemplateVo.getTemplateInformation());

				List<UploadFileVo> uploadFileVos = new ArrayList<UploadFileVo>();

				for (AttachmentsVo attachments : attachmentsDao.getAttachmentDetails(templateId,
						AttachmentsConstants.MODULE_TYPE_AR_INVOICE_TEMPLATE_LOGO)) {

					UploadFileVo uploadFileVo = new UploadFileVo();
					uploadFileVo.setId(attachments.getId());
					// uploadFileVo.setData(attachments.getD);
					uploadFileVo.setName(attachments.getFileName());
					uploadFileVo.setSize(attachments.getSize());
					uploadFileVos.add(uploadFileVo);
					logger.info("getInvoiceTemplate logo "+ uploadFileVo);
				}
				invoiceTemplateVo.setAttachmentLogo(uploadFileVos);
				logger.info("getInvoiceTemplate logo "+ uploadFileVos);
				logger.info("getInvoiceTemplate logo final "+ invoiceTemplateVo.getAttachmentLogo());

				List<UploadFileVo> uploadFileVos2 = new ArrayList<UploadFileVo>();
				for (AttachmentsVo attachments : attachmentsDao.getAttachmentDetails(templateId,
						AttachmentsConstants.MODULE_TYPE_AR_INVOICE_TEMPLATE_SIGN)) {
					UploadFileVo uploadFileVo2 = new UploadFileVo();
					uploadFileVo2.setId(attachments.getId());
					uploadFileVo2.setName(attachments.getFileName());
					uploadFileVo2.setSize(attachments.getSize());
					uploadFileVos2.add(uploadFileVo2);
				}
				invoiceTemplateVo.setAttachmentSign(uploadFileVos2);
				invoiceTemplateVo.setOrgContactNo(orgContactNo);
			} catch (Exception e) {
				logger.info("Error in getInvoiceTemplate:: ", e);
				throw new ApplicationException(e);
			}finally{
				closeResources(null, null, con);
			}
		}
		return invoiceTemplateVo;
	}



	private Long getOrgContactNo(Integer orgId, Connection con) throws ApplicationException {
		Long orgContactNo = null ;
		PreparedStatement preparedStatement = null;
		ResultSet rs =null;
		try {
			preparedStatement = con.prepareStatement(ArTemplateConstants.GET_ORG_CONTACT_NO);
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
			if(templateType.equalsIgnoreCase("invoice")) {
				preparedStatement = con.prepareStatement(ArTemplateConstants.GET_TEMPLATE_ID_INV);
			}else {
				preparedStatement = con.prepareStatement(ArTemplateConstants.GET_TEMPLATE_ID_CN);
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

	private InvoiceTemplateinformationVo getInvoiceGeneralTemplateInfo(InvoiceTemplateVo invoiceTemplateVo, Connection con) throws ApplicationException {
		InvoiceTemplateinformationVo generalInfoTempVo = new InvoiceTemplateinformationVo();
		ResultSet rs = null;
		PreparedStatement preparedStatement =null;
		try {
			preparedStatement = con.prepareStatement(ArTemplateConstants.GET_INVOICE_TEMPLATE_INFO); 
			preparedStatement.setInt(1, invoiceTemplateVo.getOrgId());
			preparedStatement.setInt(2, invoiceTemplateVo.getTemplateId());
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
			logger.info("Error in getInvoiceTemplate ", e);
			throw new ApplicationException(e);
		}finally{
			closeResources(rs, preparedStatement, null);
		}

		logger.info("getInvoiceGeneralTemplateInfo " + generalInfoTempVo);
		return generalInfoTempVo;
	}

	public InvoiceTemplateVo updateInvoiceTemplate(InvoiceTemplateVo invoiceTemplateVo) throws ApplicationException {
		logger.info("To update a  Invoice template :::" + invoiceTemplateVo);
		if (invoiceTemplateVo != null && invoiceTemplateVo.getTemplateId() != null) {
			PreparedStatement preparedStatementdel=null;
			Connection connection = null;
			try {
				connection = getAccountsReceivableConnection();
				connection.setAutoCommit(false);
				boolean isTemplateNameUnique = templateNameUniqueCheckUpdate(invoiceTemplateVo.getOrgId(),invoiceTemplateVo.getTemplateInformation().getTemplateName(),invoiceTemplateVo.getTemplateId(),ArTemplateConstants.GET_TEMPLATE_ID_INV);
				if (isTemplateNameUnique) {
					throw new ApplicationException("Template Name already exists");
				}
				updateInvoiceTemplateInfo(invoiceTemplateVo, connection);	

				preparedStatementdel = connection.prepareStatement(
						ArTemplateConstants.DELETE_AR_INV_TEMPLATE_UPD_INV, Statement.RETURN_GENERATED_KEYS);
				preparedStatementdel.setString(1, "INA");
				preparedStatementdel.setInt(2,
						invoiceTemplateVo.getOrgId() != null
						? invoiceTemplateVo.getOrgId()
								: 0);
				preparedStatementdel.setString(3,
						invoiceTemplateVo.getUserId() != null
						? invoiceTemplateVo.getUserId() 
								: null);	
				preparedStatementdel.setInt(4,
						invoiceTemplateVo.getTemplateId()!= null
						? invoiceTemplateVo.getTemplateId()
								: 0);

				int rowAffecteddel = preparedStatementdel.executeUpdate();

				logger.info("Successfully deactivation of previous records in table AR_Invoice_Template " + rowAffecteddel);

				if (invoiceTemplateVo.getAttachmentLogo() != null && invoiceTemplateVo.getAttachmentLogo().size() > 0) {
					attachmentsDao.createAttachments(invoiceTemplateVo.getOrgId(), invoiceTemplateVo.getUserId(),
							invoiceTemplateVo.getAttachmentLogo(), AttachmentsConstants.MODULE_TYPE_AR_INVOICE_TEMPLATE_LOGO,
							invoiceTemplateVo.getTemplateId(),invoiceTemplateVo.getRoleName());

				}
				if (invoiceTemplateVo.getAttachmentSign() != null && invoiceTemplateVo.getAttachmentSign().size() > 0) {
					attachmentsDao.createAttachments(invoiceTemplateVo.getOrgId(),invoiceTemplateVo.getUserId(),
							invoiceTemplateVo.getAttachmentSign(), AttachmentsConstants.MODULE_TYPE_AR_INVOICE_TEMPLATE_SIGN,
							invoiceTemplateVo.getTemplateId(),invoiceTemplateVo.getRoleName());				
				}

				if (invoiceTemplateVo.getAttachmentsToRemove() != null && invoiceTemplateVo.getAttachmentsToRemove().size() > 0) {
					for (Integer attachmentId : invoiceTemplateVo.getAttachmentsToRemove()) {
						attachmentsDao.changeStatusForSingleAttachment(attachmentId, CommonConstants.STATUS_AS_DELETE,
								invoiceTemplateVo.getUserId(), invoiceTemplateVo.getRoleName());
					}
				}
				connection.commit();
				logger.info("Succecssfully updated invoice template  in ARInvoiceTemplateDao");
			} catch (Exception e) {
				logger.info("Error in invoice template update:: ", e);
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
		return invoiceTemplateVo;
	}

	private InvoiceTemplateVo updateInvoiceTemplateInfo(InvoiceTemplateVo invoiceTemplateVo, Connection connection)
			throws ApplicationException {
		logger.info("To update into table invoice_template_information ");
		if (invoiceTemplateVo != null && invoiceTemplateVo.getTemplateId() != null && invoiceTemplateVo.getTemplateInformation() != null) {
			if (invoiceTemplateVo.getTemplateId() == null) {
				throw new ApplicationException("Template Id is mandatory");
			}
			PreparedStatement preparedStatement =null;
			try {
				preparedStatement = connection.prepareStatement(ArTemplateConstants.MODIFY_INVOICE_TEMPLATE_INFO_INV);

				preparedStatement.setString(1,
						invoiceTemplateVo.getTemplateInformation().getLogoPosition() != null
						? invoiceTemplateVo.getTemplateInformation().getLogoPosition()
								: null);				
				preparedStatement.setString(2,
						invoiceTemplateVo.getTemplateInformation().getHeaderNotes() != null
						? invoiceTemplateVo.getTemplateInformation().getHeaderNotes()
								: null);
				preparedStatement.setString(3,
						invoiceTemplateVo.getTemplateInformation().getFooterSectionNotes() != null
						? invoiceTemplateVo.getTemplateInformation().getFooterSectionNotes()
								: null);
				preparedStatement.setString(4,
						invoiceTemplateVo.getTemplateInformation().getTermsAndContd() != null
						? invoiceTemplateVo.getTemplateInformation().getTermsAndContd()
								: null);
				preparedStatement.setBoolean(5,
						invoiceTemplateVo.getTemplateInformation().getBankAccInfo() != null
						? invoiceTemplateVo.getTemplateInformation().getBankAccInfo()
								: false);
				preparedStatement.setString(6,
						invoiceTemplateVo.getTemplateInformation().getFootNotes() != null
						? invoiceTemplateVo.getTemplateInformation().getFootNotes()
								: null);
				preparedStatement.setString(7,
						invoiceTemplateVo.getTemplateInformation().getFootNotePosition() != null
						? invoiceTemplateVo.getTemplateInformation().getFootNotePosition()
								: null);
				preparedStatement.setBoolean(8,
						invoiceTemplateVo.getTemplateInformation().getIsAnnex() != null
						? invoiceTemplateVo.getTemplateInformation().getIsAnnex()
								: false);
				preparedStatement.setTimestamp(9,new Timestamp(System.currentTimeMillis()));
				preparedStatement.setInt(10,
						invoiceTemplateVo.getTemplateInformation().getOrganizationId() != null
						? invoiceTemplateVo.getTemplateInformation().getOrganizationId()
								: 0);
				preparedStatement.setString(11,
						invoiceTemplateVo.getTemplateInformation().getUpdateUserId() != null
						? invoiceTemplateVo.getTemplateInformation().getUpdateUserId()
								: null);
				preparedStatement.setString(12, invoiceTemplateVo.getRoleName()!= null
						? invoiceTemplateVo.getRoleName()
								: null);
				preparedStatement.setString(13, invoiceTemplateVo.getStatus()!= null
						? invoiceTemplateVo.getStatus()
								: null);
				preparedStatement.setString(14, invoiceTemplateVo.getTemplateInformation().getTemplateName()!= null
						? invoiceTemplateVo.getTemplateInformation().getTemplateName()
								: null);
				preparedStatement.setInt(15, invoiceTemplateVo.getTemplateId());
				preparedStatement.executeUpdate();


				logger.info("Successfully updated into table invoice_template_information ");
			} catch (Exception e) {
				logger.info("Error in updating  to table invoice_template_information ", e);
				throw new ApplicationException(e.getMessage());
			}finally{
				closeResources(null, preparedStatement, null);
			}
		}

		return invoiceTemplateVo;
	}

	public List<InvoiceTemplateinformationVo> getInvoiceTemplateList(Integer orgId, String userId, String roleName) throws ApplicationException {
		List<InvoiceTemplateinformationVo> generalInfoTempVos = new ArrayList<InvoiceTemplateinformationVo>();
		InvoiceTemplateinformationVo generalInfoTempVo =null;
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement preparedStatement =null;
		try {
			con = getAccountsReceivableConnection();
			preparedStatement = con.prepareStatement(ArTemplateConstants.GET_INVOICE_TEMPLATE_INFO_ALL); 
			preparedStatement.setInt(1,orgId);
			preparedStatement.setString(2, userId);
			preparedStatement.setString(3, roleName);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				generalInfoTempVo = new InvoiceTemplateinformationVo();
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

	public List<InvoiceTemplateinformationVo> getAllInvoiceTemplatePerOrganization(Integer orgId) throws ApplicationException {
		List<InvoiceTemplateinformationVo> generalInfoTempVos = new ArrayList<InvoiceTemplateinformationVo>();
		InvoiceTemplateinformationVo generalInfoTempVo =null;
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement preparedStatement =null;
		try {
			con = getAccountsReceivableConnection();
			preparedStatement = con.prepareStatement(ArTemplateConstants.GET_INVOICE_TEMPLATE_INFO_ORGANIZATION); 
			preparedStatement.setInt(1,orgId);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				generalInfoTempVo = new InvoiceTemplateinformationVo();
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

	public InvoiceTemplateVo getDefaultInvoiceTemplateForOrg(Integer orgId) throws ApplicationException {
		logger.info("To get Invoice template in ARInvoiceTemplateDao for:::" );
		InvoiceTemplateVo invoiceTemplateVo = null;
		Connection con=null;
		if (orgId != null  ) {
			try{
				con = getAccountsReceivableConnection();
				invoiceTemplateVo = new InvoiceTemplateVo();
				invoiceTemplateVo.setOrgId(orgId);
				Long orgContactNo = getOrgContactNo(orgId,con);
				Integer templateId = getTemplateId(orgId,ArTemplateConstants.TEMPLATE_TYPE_INV,con);
				logger.info("getInvoiceTemplate info id"+ templateId);
				invoiceTemplateVo.setTemplateId(templateId);
				InvoiceTemplateinformationVo invoiceGeneralTemplateInfo = getInvoiceGeneralTemplateInfo(invoiceTemplateVo, con);
				invoiceTemplateVo.setTemplateInformation(invoiceGeneralTemplateInfo);
				invoiceTemplateVo.setRoleName(invoiceGeneralTemplateInfo.getRoleName());
				logger.info("getInvoiceTemplate info "+invoiceTemplateVo.getTemplateInformation());

				List<UploadFileVo> uploadFileVos = new ArrayList<UploadFileVo>();

				for (AttachmentsVo attachments : attachmentsDao.getAttachmentDetails(templateId,
						AttachmentsConstants.MODULE_TYPE_AR_INVOICE_TEMPLATE_LOGO)) {

					UploadFileVo uploadFileVo = new UploadFileVo();
					uploadFileVo.setId(attachments.getId());
					// uploadFileVo.setData(attachments.getD);
					uploadFileVo.setName(attachments.getFileName());
					uploadFileVo.setSize(attachments.getSize());
					uploadFileVos.add(uploadFileVo);
					logger.info("getInvoiceTemplate logo "+ uploadFileVo);
				}
				invoiceTemplateVo.setAttachmentLogo(uploadFileVos);
				logger.info("getInvoiceTemplate logo "+ uploadFileVos);
				logger.info("getInvoiceTemplate logo final "+ invoiceTemplateVo.getAttachmentLogo());

				List<UploadFileVo> uploadFileVos2 = new ArrayList<UploadFileVo>();
				for (AttachmentsVo attachments : attachmentsDao.getAttachmentDetails(templateId,
						AttachmentsConstants.MODULE_TYPE_AR_INVOICE_TEMPLATE_SIGN)) {
					UploadFileVo uploadFileVo2 = new UploadFileVo();
					uploadFileVo2.setId(attachments.getId());
					uploadFileVo2.setName(attachments.getFileName());
					uploadFileVo2.setSize(attachments.getSize());
					uploadFileVos2.add(uploadFileVo2);
				}
				invoiceTemplateVo.setAttachmentSign(uploadFileVos2);
				invoiceTemplateVo.setOrgContactNo(orgContactNo);
			} catch (Exception e) {
				logger.info("Error in getInvoiceTemplate:: ", e);
				throw new ApplicationException(e);
			}finally{
				closeResources(null, null, con);
			}
		}
		return invoiceTemplateVo;
	}



	public List<Integer> getTemplateIds(Integer orgId ,  String templateType ) throws ApplicationException {
		logger.info("To retrieve all the template Ids ");
		List<Integer> templateIds = new ArrayList<Integer>();
		Connection con = null;
		ResultSet rs = null;
		Integer templateId = null;
		PreparedStatement preparedStatement =null;
		try {
			con = getAccountsReceivableConnection();

			if(templateType.equalsIgnoreCase("invoice")) {
				preparedStatement = con.prepareStatement(ArTemplateConstants.GET_TEMPLATE_IDS_FOR_ORG_INV); 
			} else {
				preparedStatement = con.prepareStatement(ArTemplateConstants.GET_TEMPLATE_IDS_FOR_ORG_CN); 
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

	public InvoiceTemplateVo activateDeActivateInvoiceTemplate(Integer orgId, String orgName,
			String userId, Integer id, String roleName,
			String status) throws ApplicationException, SQLException {
		logger.info("activateDeActivateInvoiceTemplate DAO :::");


		if (orgId != null && userId != null && id != null && status != null ) {
			if(status.equalsIgnoreCase("Activate")) {
				Connection con = null;
				PreparedStatement preparedStatementdel=null;
				try  {
					logger.info(" deactivate - orgid : "+orgId+" userId : "+userId+" status "+ status+" id "+id);
					con = getAccountsReceivableConnection();
					preparedStatementdel = con.prepareStatement(
							ArTemplateConstants.DELETE_AR_INV_TEMPLATE, Statement.RETURN_GENERATED_KEYS);
					preparedStatementdel.setString(1, "INA");
					preparedStatementdel.setInt(2,orgId);
					preparedStatementdel.setString(3,userId);				

					int rowAffecteddel = preparedStatementdel.executeUpdate();

					logger.info("Successfully deactivation of previous records in table AR_Invoice_Template " + rowAffecteddel);
				} catch (Exception e) {
					logger.info("Error in deactivation of previous records in table AR_Invoice_Template ", e);
					throw new ApplicationException(e.getMessage());
				}finally{
					closeResources(null, preparedStatementdel, con);
				}

				PreparedStatement preparedStatementact=null;
				Connection conn = null;
				try  {
					logger.info(" activate - orgid : "+orgId+" userId : "+userId+" status "+ status+" id "+id);
					conn = getAccountsReceivableConnection();
					preparedStatementact = conn.prepareStatement(
							ArTemplateConstants.DELETE_INV_TEMPLATE_ID, Statement.RETURN_GENERATED_KEYS);
					preparedStatementact.setString(1,"ACT");
					preparedStatementact.setInt(2,orgId);
					preparedStatementact.setString(3,userId);	
					preparedStatementact.setInt(4,id);

					int rowAffecteddel = preparedStatementact.executeUpdate();

					logger.info("Successfully activation of template in table AR_Invoice_Template " + rowAffecteddel);

				} catch (Exception e) {
					logger.info("Error in activation  of template in table AR_Invoice_Template ", e);
					throw new ApplicationException(e.getMessage());
				}finally{
					closeResources(null, preparedStatementdel, conn);					
				}
			}  else {
				PreparedStatement preparedStatementdeact=null;
				Connection conn = null;
				try  {
					logger.info(" activate - orgid : "+orgId+" userId : "+userId+" status "+ status+" id "+id);
					conn = getAccountsReceivableConnection();
					preparedStatementdeact = conn.prepareStatement(
							ArTemplateConstants.DELETE_INV_TEMPLATE_ID, Statement.RETURN_GENERATED_KEYS);
					preparedStatementdeact.setString(1,"INA");
					preparedStatementdeact.setInt(2,orgId);
					preparedStatementdeact.setString(3,userId);	
					preparedStatementdeact.setInt(4,id);

					int rowAffecteddel = preparedStatementdeact.executeUpdate();

					logger.info("Successfully deactivation of template in table AR_Invoice_Template " + rowAffecteddel);

				} catch (Exception e) {
					logger.info("Error in deactivation  of template in table AR_Invoice_Template ", e);
					throw new ApplicationException(e.getMessage());
				}finally{
					closeResources(null, preparedStatementdeact, conn);					
				}
			}

		}
		return getInvoiceTemplate(orgId,id);
	}

	public CreditNoteTemplateVo createCreditNoteTemplate(CreditNoteTemplateVo creditNoteTempVo) throws ApplicationException {
		logger.info("To create a new Credit Note Template" + creditNoteTempVo);
		Connection connection = null;
		if (creditNoteTempVo != null) {
			try {
				connection = getAccountsReceivableConnection();
				connection.setAutoCommit(false);
				boolean isTemplateNameUnique = templateNameUniqueCheck(creditNoteTempVo.getOrgId(),creditNoteTempVo.getTemplateInformation().getTemplateName(),ArTemplateConstants.TEMPLATE_TYPE_CN);
				if (isTemplateNameUnique) {
					throw new ApplicationException("Template Exist for the Organization");
				}
				createCreditNoteTemplateInfo(creditNoteTempVo, connection);	
				logger.info("Attachment DAO values org id "+creditNoteTempVo.getOrgId());
				logger.info("Attachment DAO values user id "+creditNoteTempVo.getUserId());
				logger.info("Attachment DAO values  id "+ creditNoteTempVo.getTemplateId());
				logger.info("Attachment DAO values  rolename "+creditNoteTempVo.getRoleName());
				attachmentsDao.createAttachments(creditNoteTempVo.getOrgId(), creditNoteTempVo.getUserId(),
						creditNoteTempVo.getAttachmentLogo(), AttachmentsConstants.MODULE_TYPE_AR_CREDIT_NOTE_TEMPLATE_LOGO,
						creditNoteTempVo.getTemplateId(),creditNoteTempVo.getRoleName());
				attachmentsDao.createAttachments(creditNoteTempVo.getOrgId(),creditNoteTempVo.getUserId(),
						creditNoteTempVo.getAttachmentSign(), AttachmentsConstants.MODULE_TYPE_AR_CREDIT_NOTE_TEMPLATE_SIGN,
						creditNoteTempVo.getTemplateId(),creditNoteTempVo.getRoleName());
				connection.commit();
				logger.info("Succecssfully created Credit Note in TemplateDao");
			} catch (Exception e) {
				logger.info("Error in createCreditNoteTemplate:: ", e);
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
		return creditNoteTempVo;
	}

	private CreditNoteTemplateVo createCreditNoteTemplateInfo(CreditNoteTemplateVo creditNoteTempVo, Connection connection) throws ApplicationException {
		logger.info("To insert into table CreditNotetemplate "+creditNoteTempVo.toString());

		if (creditNoteTempVo != null && creditNoteTempVo.getTemplateInformation() != null && creditNoteTempVo.getOrgId() != null) {

			PreparedStatement preparedStatementdel=null;
			try  {
				preparedStatementdel = connection.prepareStatement(
						ArTemplateConstants.DELETE_AR_CN_TEMPLATE, Statement.RETURN_GENERATED_KEYS);
				preparedStatementdel.setString(1, "INA");
				preparedStatementdel.setInt(2,
						creditNoteTempVo.getTemplateInformation().getOrganizationId() != null
						? creditNoteTempVo.getTemplateInformation().getOrganizationId()
								: 0);
				preparedStatementdel.setInt(3,
						creditNoteTempVo.getTemplateInformation().getUserId() != null
						? creditNoteTempVo.getTemplateInformation().getUserId()
								: 0);				

				int rowAffecteddel = preparedStatementdel.executeUpdate();

				logger.info("Successfully deactivation of previous records in table CreditNotetemplate " + rowAffecteddel);
			} catch (Exception e) {
				logger.info("Error in deactivation of previous records in table CreditNotetemplate ", e);
				throw new ApplicationException(e.getMessage());
			}finally{
				closeResources(null, preparedStatementdel, null);
			}

			PreparedStatement preparedStatement = null;
			ResultSet rs =null;
			try {
				preparedStatement = connection.prepareStatement(
						ArTemplateConstants.INSERT_INTO_AR_CN_TEMPLATE, Statement.RETURN_GENERATED_KEYS);


				preparedStatement.setString(1,
						creditNoteTempVo.getTemplateInformation().getLogoPosition() != null
						? creditNoteTempVo.getTemplateInformation().getLogoPosition()
								: null);				
				preparedStatement.setString(2,
						creditNoteTempVo.getTemplateInformation().getHeaderNotes() != null
						? creditNoteTempVo.getTemplateInformation().getHeaderNotes()
								: null);
				preparedStatement.setString(3,
						creditNoteTempVo.getTemplateInformation().getFooterSectionNotes() != null
						? creditNoteTempVo.getTemplateInformation().getFooterSectionNotes()
								: null);
				preparedStatement.setString(4,
						creditNoteTempVo.getTemplateInformation().getTermsAndContd() != null
						? creditNoteTempVo.getTemplateInformation().getTermsAndContd()
								: null);
				preparedStatement.setBoolean(5,
						creditNoteTempVo.getTemplateInformation().getBankAccInfo() != null
						? creditNoteTempVo.getTemplateInformation().getBankAccInfo()
								: false);
				preparedStatement.setString(6,
						creditNoteTempVo.getTemplateInformation().getFootNotes() != null
						? creditNoteTempVo.getTemplateInformation().getFootNotes()
								: null);
				preparedStatement.setString(7,
						creditNoteTempVo.getTemplateInformation().getFootNotePosition() != null
						? creditNoteTempVo.getTemplateInformation().getFootNotePosition()
								: null);
				preparedStatement.setBoolean(8,
						creditNoteTempVo.getTemplateInformation().getIsAnnex() != null
						? creditNoteTempVo.getTemplateInformation().getIsAnnex()
								: false);
				preparedStatement.setInt(9,
						creditNoteTempVo.getTemplateInformation().getUserId() != null
						? creditNoteTempVo.getTemplateInformation().getUserId()
								: 0);
				preparedStatement.setInt(10,
						creditNoteTempVo.getTemplateInformation().getOrganizationId() != null
						? creditNoteTempVo.getTemplateInformation().getOrganizationId()
								: 0);
				preparedStatement.setString(11, creditNoteTempVo.getRoleName()!= null
						? creditNoteTempVo.getRoleName()
								: null);
				preparedStatement.setString(12, "ACT");
				preparedStatement.setString(13, creditNoteTempVo.getTemplateInformation().getTemplateName() != null
						? creditNoteTempVo.getTemplateInformation().getTemplateName()
								: null);

				int rowAffected = preparedStatement.executeUpdate();
				if (rowAffected == 1) {
					rs = preparedStatement.getGeneratedKeys();
					while (rs.next()) {
						creditNoteTempVo.setTemplateId(rs.getInt(1));
						//invoiceVo.setInvoiceId(rs.getInt(1));
					}
				}


				logger.info("Successfully inserted into table CreditNotetemplate " + creditNoteTempVo);
			} catch (Exception e) {
				logger.info("Error in inserting to table CreditNotetemplate ", e);
				throw new ApplicationException(e.getMessage());
			}finally{
				closeResources(rs, preparedStatement, null);
			}
		}
		return creditNoteTempVo;

	}

	public CreditNoteTemplateVo getCreditNoteTemplate(Integer orgId, Integer templateId) throws ApplicationException {
		logger.info("CreditNoteTemplate :::" );
		CreditNoteTemplateVo creditNoteTemplateVo = null;
		Connection con=null;
		if (orgId != null && templateId != null ) {
			try{
				con = getAccountsReceivableConnection();
				creditNoteTemplateVo = new CreditNoteTemplateVo();
				creditNoteTemplateVo.setOrgId(orgId);
				Long orgContactNo = getOrgContactNo(orgId,con);
				logger.info("getCreditNoteTemplate info id"+ templateId);
				creditNoteTemplateVo.setTemplateId(templateId);
				CreditNoteTemplateinformationVo creditNoteTemplateinformationVo = getCreditNoteGeneralTemplateInfo(creditNoteTemplateVo, con);
				creditNoteTemplateVo.setTemplateInformation(creditNoteTemplateinformationVo);
				creditNoteTemplateVo.setRoleName(creditNoteTemplateinformationVo.getRoleName());
				logger.info("getInvoiceTemplate info "+creditNoteTemplateVo.getTemplateInformation());

				List<UploadFileVo> uploadFileVos = new ArrayList<UploadFileVo>();

				for (AttachmentsVo attachments : attachmentsDao.getAttachmentDetails(templateId,
						AttachmentsConstants.MODULE_TYPE_AR_CREDIT_NOTE_TEMPLATE_LOGO)) {

					UploadFileVo uploadFileVo = new UploadFileVo();
					uploadFileVo.setId(attachments.getId());
					// uploadFileVo.setData(attachments.getD);
					uploadFileVo.setName(attachments.getFileName());
					uploadFileVo.setSize(attachments.getSize());
					uploadFileVos.add(uploadFileVo);
					logger.info("getInvoiceTemplate logo "+ uploadFileVo);
				}
				creditNoteTemplateVo.setAttachmentLogo(uploadFileVos);
				logger.info("getInvoiceTemplate logo "+ uploadFileVos);
				logger.info("getInvoiceTemplate logo final "+ creditNoteTemplateVo.getAttachmentLogo());

				List<UploadFileVo> uploadFileVos2 = new ArrayList<UploadFileVo>();
				for (AttachmentsVo attachments : attachmentsDao.getAttachmentDetails(templateId,
						AttachmentsConstants.MODULE_TYPE_AR_CREDIT_NOTE_TEMPLATE_SIGN)) {
					UploadFileVo uploadFileVo2 = new UploadFileVo();
					uploadFileVo2.setId(attachments.getId());
					uploadFileVo2.setName(attachments.getFileName());
					uploadFileVo2.setSize(attachments.getSize());
					uploadFileVos2.add(uploadFileVo2);
				}
				creditNoteTemplateVo.setAttachmentSign(uploadFileVos2);
				creditNoteTemplateVo.setOrgContactNo(orgContactNo);
			} catch (Exception e) {
				logger.info("Error in getInvoiceTemplate:: ", e);
				throw new ApplicationException(e);
			}finally{
				closeResources(null, null, con);
			}
		}
		return creditNoteTemplateVo;
	}

	private CreditNoteTemplateinformationVo getCreditNoteGeneralTemplateInfo(CreditNoteTemplateVo creditNoteTemplateVo,
			Connection con) throws ApplicationException {
		CreditNoteTemplateinformationVo generalInfoTempVo = new CreditNoteTemplateinformationVo();
		ResultSet rs = null;
		PreparedStatement preparedStatement =null;
		try {
			preparedStatement = con.prepareStatement(ArTemplateConstants.GET_CREDIT_NOTE_TEMPLATE_INFO); 
			preparedStatement.setInt(1, creditNoteTemplateVo.getOrgId());
			preparedStatement.setInt(2, creditNoteTemplateVo.getTemplateId());
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
			logger.info("Error in getInvoiceTemplate ", e);
			throw new ApplicationException(e);
		}finally{
			closeResources(rs, preparedStatement, null);
		}

		logger.info("getInvoiceGeneralTemplateInfo " + generalInfoTempVo);
		return generalInfoTempVo;
	}

	public List<CreditNoteTemplateinformationVo> getCreditNoteTemplateList(int orgId, String userId, String roleName) throws ApplicationException {
		List<CreditNoteTemplateinformationVo>generalInfoTempVos = new ArrayList<CreditNoteTemplateinformationVo>();
		CreditNoteTemplateinformationVo generalInfoTempVo =null;
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement preparedStatement =null;
		try {
			con = getAccountsReceivableConnection();
			preparedStatement = con.prepareStatement(ArTemplateConstants.GET_CREDIT_NOTE_TEMPLATE_INFO_ALL); 
			preparedStatement.setInt(1,orgId);
			preparedStatement.setString(2, userId);
			preparedStatement.setString(3, roleName);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				generalInfoTempVo = new CreditNoteTemplateinformationVo();
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
			logger.info("Error in getCreditNoteTemplateList ", e);
			throw new ApplicationException(e);
		}finally{
			closeResources(rs, preparedStatement, con);
		}

		logger.info("getCreditNoteTemplateList " + generalInfoTempVos);
		return generalInfoTempVos;
	}

	public CreditNoteTemplateVo getDefaultCreditNoteTemplateForOrg(Integer orgId) throws ApplicationException {
		logger.info("getDefaultCreditNoteTemplateForOrg - DAO " );
		CreditNoteTemplateVo creditNoteTemplateVo = null;
		Connection con=null;
		if (orgId != null  ) {
			try{
				con = getAccountsReceivableConnection();
				creditNoteTemplateVo = new CreditNoteTemplateVo();
				creditNoteTemplateVo.setOrgId(orgId);
				Long orgContactNo = getOrgContactNo(orgId,con);
				Integer templateId = getTemplateId(orgId,ArTemplateConstants.TEMPLATE_TYPE_CN,con);
				logger.info("getTemplate info id"+ templateId);
				creditNoteTemplateVo.setTemplateId(templateId);
				CreditNoteTemplateinformationVo creditNoteGeneralTemplateInfo = getCreditNoteGeneralTemplateInfo(creditNoteTemplateVo, con);
				creditNoteTemplateVo.setTemplateInformation(creditNoteGeneralTemplateInfo);
				creditNoteTemplateVo.setRoleName(creditNoteGeneralTemplateInfo.getRoleName());
				logger.info("getInvoiceTemplate info "+creditNoteTemplateVo.getTemplateInformation());

				List<UploadFileVo> uploadFileVos = new ArrayList<UploadFileVo>();

				for (AttachmentsVo attachments : attachmentsDao.getAttachmentDetails(templateId,
						AttachmentsConstants.MODULE_TYPE_AR_CREDIT_NOTE_TEMPLATE_LOGO)) {

					UploadFileVo uploadFileVo = new UploadFileVo();
					uploadFileVo.setId(attachments.getId());
					// uploadFileVo.setData(attachments.getD);
					uploadFileVo.setName(attachments.getFileName());
					uploadFileVo.setSize(attachments.getSize());
					uploadFileVos.add(uploadFileVo);
					logger.info("getInvoiceTemplate logo "+ uploadFileVo);
				}
				creditNoteTemplateVo.setAttachmentLogo(uploadFileVos);
				logger.info("getInvoiceTemplate logo "+ uploadFileVos);
				logger.info("getInvoiceTemplate logo final "+ creditNoteTemplateVo.getAttachmentLogo());

				List<UploadFileVo> uploadFileVos2 = new ArrayList<UploadFileVo>();
				for (AttachmentsVo attachments : attachmentsDao.getAttachmentDetails(templateId,
						AttachmentsConstants.MODULE_TYPE_AR_CREDIT_NOTE_TEMPLATE_SIGN)) {
					UploadFileVo uploadFileVo2 = new UploadFileVo();
					uploadFileVo2.setId(attachments.getId());
					uploadFileVo2.setName(attachments.getFileName());
					uploadFileVo2.setSize(attachments.getSize());
					uploadFileVos2.add(uploadFileVo2);
				}
				creditNoteTemplateVo.setAttachmentSign(uploadFileVos2);
				creditNoteTemplateVo.setOrgContactNo(orgContactNo);
			} catch (Exception e) {
				logger.info("Error in getDefaultCreditNoteTemplateForOrg:: ", e);
				throw new ApplicationException(e);
			}finally{
				closeResources(null, null, con);
			}
		}
		return creditNoteTemplateVo;
	}

	public CreditNoteTemplateVo updateCreditNoteTemplate(CreditNoteTemplateVo creditNoteTempVo) throws ApplicationException {
		logger.info("updateCreditNoteTemplate :::" + creditNoteTempVo);
		if (creditNoteTempVo != null && creditNoteTempVo.getTemplateId() != null) {
			PreparedStatement preparedStatementdel=null;
			Connection connection = null;
			try {
				connection = getAccountsReceivableConnection();
				connection.setAutoCommit(false);
				boolean isTemplateNameUnique = templateNameUniqueCheckUpdate(creditNoteTempVo.getOrgId(),creditNoteTempVo.getTemplateInformation().getTemplateName(),creditNoteTempVo.getTemplateId(),ArTemplateConstants.TEMPLATE_TYPE_CN);
				if (isTemplateNameUnique) {
					throw new ApplicationException("Template Name already exists");
				}
				updateCreditNoteTemplateInfo(creditNoteTempVo, connection);	

				preparedStatementdel = connection.prepareStatement(
						ArTemplateConstants.DELETE_AR_INV_TEMPLATE_UPD_CN, Statement.RETURN_GENERATED_KEYS);
				preparedStatementdel.setString(1, "INA");
				preparedStatementdel.setInt(2,
						creditNoteTempVo.getOrgId() != null
						? creditNoteTempVo.getOrgId()
								: 0);
				preparedStatementdel.setString(3,
						creditNoteTempVo.getUserId() != null
						? creditNoteTempVo.getUserId() 
								: null);	
				preparedStatementdel.setInt(4,
						creditNoteTempVo.getTemplateId()!= null
						? creditNoteTempVo.getTemplateId()
								: 0);

				int rowAffecteddel = preparedStatementdel.executeUpdate();

				logger.info("Successfully deactivation of previous records in table CreditNotetemplate " + rowAffecteddel);

				if (creditNoteTempVo.getAttachmentLogo() != null && creditNoteTempVo.getAttachmentLogo().size() > 0) {
					attachmentsDao.createAttachments(creditNoteTempVo.getOrgId(), creditNoteTempVo.getUserId(),
							creditNoteTempVo.getAttachmentLogo(), AttachmentsConstants.MODULE_TYPE_AR_CREDIT_NOTE_TEMPLATE_LOGO,
							creditNoteTempVo.getTemplateId(),creditNoteTempVo.getRoleName());

				}
				if (creditNoteTempVo.getAttachmentSign() != null && creditNoteTempVo.getAttachmentSign().size() > 0) {
					attachmentsDao.createAttachments(creditNoteTempVo.getOrgId(),creditNoteTempVo.getUserId(),
							creditNoteTempVo.getAttachmentSign(), AttachmentsConstants.MODULE_TYPE_AR_CREDIT_NOTE_TEMPLATE_SIGN,
							creditNoteTempVo.getTemplateId(),creditNoteTempVo.getRoleName());				
				}

				if (creditNoteTempVo.getAttachmentsToRemove() != null && creditNoteTempVo.getAttachmentsToRemove().size() > 0) {
					for (Integer attachmentId : creditNoteTempVo.getAttachmentsToRemove()) {
						attachmentsDao.changeStatusForSingleAttachment(attachmentId, CommonConstants.STATUS_AS_DELETE,
								creditNoteTempVo.getUserId(), creditNoteTempVo.getRoleName());
					}
				}
				connection.commit();
				logger.info("Succecssfully updated invoice template  in updateCreditNoteTemplateDAO");
			} catch (Exception e) {
				logger.info("Error in Credit Note template update:: ", e);
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
		return creditNoteTempVo;
	}

	private CreditNoteTemplateVo updateCreditNoteTemplateInfo(CreditNoteTemplateVo creditNoteTempVo, Connection connection) throws ApplicationException {
		logger.info("To update into table CreditNotetemplate ");
		if (creditNoteTempVo != null && creditNoteTempVo.getTemplateId() != null && creditNoteTempVo.getTemplateInformation() != null) {
			if (creditNoteTempVo.getTemplateId() == null) {
				throw new ApplicationException("Template Id is mandatory");
			}
			PreparedStatement preparedStatement =null;
			try {
				preparedStatement = connection.prepareStatement(ArTemplateConstants.MODIFY_INVOICE_TEMPLATE_INFO_CN);

				preparedStatement.setString(1,
						creditNoteTempVo.getTemplateInformation().getLogoPosition() != null
						? creditNoteTempVo.getTemplateInformation().getLogoPosition()
								: null);				
				preparedStatement.setString(2,
						creditNoteTempVo.getTemplateInformation().getHeaderNotes() != null
						? creditNoteTempVo.getTemplateInformation().getHeaderNotes()
								: null);
				preparedStatement.setString(3,
						creditNoteTempVo.getTemplateInformation().getFooterSectionNotes() != null
						? creditNoteTempVo.getTemplateInformation().getFooterSectionNotes()
								: null);
				preparedStatement.setString(4,
						creditNoteTempVo.getTemplateInformation().getTermsAndContd() != null
						? creditNoteTempVo.getTemplateInformation().getTermsAndContd()
								: null);
				preparedStatement.setBoolean(5,
						creditNoteTempVo.getTemplateInformation().getBankAccInfo() != null
						? creditNoteTempVo.getTemplateInformation().getBankAccInfo()
								: false);
				preparedStatement.setString(6,
						creditNoteTempVo.getTemplateInformation().getFootNotes() != null
						? creditNoteTempVo.getTemplateInformation().getFootNotes()
								: null);
				preparedStatement.setString(7,
						creditNoteTempVo.getTemplateInformation().getFootNotePosition() != null
						? creditNoteTempVo.getTemplateInformation().getFootNotePosition()
								: null);
				preparedStatement.setBoolean(8,
						creditNoteTempVo.getTemplateInformation().getIsAnnex() != null
						? creditNoteTempVo.getTemplateInformation().getIsAnnex()
								: false);
				preparedStatement.setTimestamp(9,new Timestamp(System.currentTimeMillis()));
				preparedStatement.setInt(10,
						creditNoteTempVo.getTemplateInformation().getOrganizationId() != null
						? creditNoteTempVo.getTemplateInformation().getOrganizationId()
								: 0);
				preparedStatement.setString(11,
						creditNoteTempVo.getTemplateInformation().getUpdateUserId() != null
						? creditNoteTempVo.getTemplateInformation().getUpdateUserId()
								: null);
				preparedStatement.setString(12, creditNoteTempVo.getRoleName()!= null
						? creditNoteTempVo.getRoleName()
								: null);
				preparedStatement.setString(13, creditNoteTempVo.getStatus()!= null
						? creditNoteTempVo.getStatus()
								: null);
				preparedStatement.setString(14, creditNoteTempVo.getTemplateInformation().getTemplateName()!= null
						? creditNoteTempVo.getTemplateInformation().getTemplateName()
								: null);
				preparedStatement.setInt(15, creditNoteTempVo.getTemplateId());
				preparedStatement.executeUpdate();


				logger.info("Successfully updated into table CreditNotetemplate ");
			} catch (Exception e) {
				logger.info("Error in updating  to table CreditNotetemplate ", e);
				throw new ApplicationException(e.getMessage());
			}finally{
				closeResources(null, preparedStatement, null);
			}
		}

		return creditNoteTempVo;

	}

	public CreditNoteTemplateVo activateDeActivateCreditNoteTemplate(Integer orgId, String orgName, String userId,
			Integer id, String roleName, String status) throws ApplicationException {
		logger.info("activateDeActivateCreditNoteTemplate DAO :::");


		if (orgId != null && userId != null && id != null && status != null ) {
			if(status.equalsIgnoreCase("Activate")) {
				Connection con = null;
				PreparedStatement preparedStatementdel=null;
				try  {
					logger.info(" deactivate - orgid : "+orgId+" userId : "+userId+" status "+ status+" id "+id);
					con = getAccountsReceivableConnection();
					preparedStatementdel = con.prepareStatement(
							ArTemplateConstants.DELETE_AR_CN_TEMPLATE, Statement.RETURN_GENERATED_KEYS);
					preparedStatementdel.setString(1, "INA");
					preparedStatementdel.setInt(2,orgId);
					preparedStatementdel.setString(3,userId);				

					int rowAffecteddel = preparedStatementdel.executeUpdate();

					logger.info("Successfully deactivation of previous records in table CreditNotetemplate " + rowAffecteddel);
				} catch (Exception e) {
					logger.info("Error in deactivation of previous records in table CreditNotetemplate ", e);
					throw new ApplicationException(e.getMessage());
				}finally{
					closeResources(null, preparedStatementdel, con);
				}

				PreparedStatement preparedStatementact=null;
				Connection conn = null;
				try  {
					logger.info(" activate - orgid : "+orgId+" userId : "+userId+" status "+ status+" id "+id);
					conn = getAccountsReceivableConnection();
					preparedStatementact = conn.prepareStatement(
							ArTemplateConstants.DELETE_CN_TEMPLATE_ID, Statement.RETURN_GENERATED_KEYS);
					preparedStatementact.setString(1,"ACT");
					preparedStatementact.setInt(2,orgId);
					preparedStatementact.setString(3,userId);	
					preparedStatementact.setInt(4,id);

					int rowAffecteddel = preparedStatementact.executeUpdate();

					logger.info("Successfully activation of template in table CreditNotetemplate " + rowAffecteddel);

				} catch (Exception e) {
					logger.info("Error in activation  of template in table CreditNotetemplate ", e);
					throw new ApplicationException(e.getMessage());
				}finally{
					closeResources(null, preparedStatementdel, conn);					
				}
			}  else {
				PreparedStatement preparedStatementdeact=null;
				Connection conn = null;
				try  {
					logger.info(" activate - orgid : "+orgId+" userId : "+userId+" status "+ status+" id "+id);
					conn = getAccountsReceivableConnection();
					preparedStatementdeact = conn.prepareStatement(
							ArTemplateConstants.DELETE_CN_TEMPLATE_ID, Statement.RETURN_GENERATED_KEYS);
					preparedStatementdeact.setString(1,"INA");
					preparedStatementdeact.setInt(2,orgId);
					preparedStatementdeact.setString(3,userId);	
					preparedStatementdeact.setInt(4,id);

					int rowAffecteddel = preparedStatementdeact.executeUpdate();

					logger.info("Successfully deactivation of template in table CreditNotetemplate " + rowAffecteddel);

				} catch (Exception e) {
					logger.info("Error in deactivation  of template in table CreditNotetemplate ", e);
					throw new ApplicationException(e.getMessage());
				}finally{
					closeResources(null, preparedStatementdeact, conn);					
				}
			}

		}
		return getCreditNoteTemplate(orgId,id);
	}
}







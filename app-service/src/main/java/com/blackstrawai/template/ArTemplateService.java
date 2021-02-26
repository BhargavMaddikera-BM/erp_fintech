package com.blackstrawai.template;



import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.ar.ArBalanceUpdateDao;
import com.blackstrawai.ar.template.CreditNoteTemplateVo;
import com.blackstrawai.ar.template.CreditNoteTemplateinformationVo;
import com.blackstrawai.ar.template.InvoiceTemplateVo;
import com.blackstrawai.ar.template.InvoiceTemplateinformationVo;
import com.blackstrawai.attachments.AttachmentService;
import com.blackstrawai.attachments.AttachmentsConstants;
import com.blackstrawai.common.BaseService;


@Service
public class ArTemplateService extends BaseService{

	private Logger logger = Logger.getLogger(ArTemplateService.class);

	@Autowired
	private ArTemplateDao templateDao;
	
	@Autowired
	private AttachmentService attachmentService;
	
	@Autowired
	ArBalanceUpdateDao balanceUpdateDao;
	
	public InvoiceTemplateVo createInvoiceTemplate(InvoiceTemplateVo invoiceTemplateVo) throws ApplicationException {
		logger.info("Entry into createInvoiceTemplate" + invoiceTemplateVo.toString());
		try {
			invoiceTemplateVo = templateDao.createInvoiceTemplate(invoiceTemplateVo);
			logger.info("createInvoiceTemplate - service "+invoiceTemplateVo);
			if(invoiceTemplateVo.getTemplateId()!=null && !(invoiceTemplateVo.getTemplateInformation() == null) && invoiceTemplateVo.getAttachmentLogo() != null && invoiceTemplateVo.getAttachmentSign() != null) {
				logger.info("Entry into template logo upload");
				attachmentService.upload(AttachmentsConstants.MODULE_TYPE_AR_INVOICE_TEMPLATE_LOGO, invoiceTemplateVo.getTemplateId(), invoiceTemplateVo.getOrgId(), invoiceTemplateVo.getAttachmentLogo());
				logger.info("Entry into template logo upload");
				attachmentService.upload(AttachmentsConstants.MODULE_TYPE_AR_INVOICE_TEMPLATE_SIGN, invoiceTemplateVo.getTemplateId(), invoiceTemplateVo.getOrgId(), invoiceTemplateVo.getAttachmentSign());	 
					
				logger.info("Upload Successfull");
			}
			
		logger.info("Invoice Template created Successfully in service layer ");
	} catch (Exception e) {
		logger.info("Error in Invoice Template creation in service layer ");
		throw e;
	}
		return invoiceTemplateVo;
	}
	


	public InvoiceTemplateVo getInvoiceTemplate(Integer orgId , Integer templateId) throws ApplicationException {
		logger.info("Entry into getInvoiceTemplate");
		 InvoiceTemplateVo invoiceTemplateVo = templateDao.getInvoiceTemplate(orgId,templateId);
		logger.info("attachment trace - template id" + invoiceTemplateVo.getTemplateInformation().getId());
		logger.info("attachment trace - template info" + invoiceTemplateVo.getTemplateInformation());
		logger.info("attachment trace - template logo" + invoiceTemplateVo.getAttachmentLogo());
		if(invoiceTemplateVo.getTemplateInformation().getId() !=null && !(invoiceTemplateVo.getTemplateInformation() == null) ) {
				logger.info("Entry into template upload");
				if(invoiceTemplateVo.getAttachmentLogo() != null ) {
					  attachmentService.encodeAllFiles(invoiceTemplateVo.getOrgId(), invoiceTemplateVo.getTemplateInformation().getId(),AttachmentsConstants.MODULE_TYPE_AR_INVOICE_TEMPLATE_LOGO, invoiceTemplateVo.getAttachmentLogo());
					// attachmentService.encodeUploadedFile(invoiceTemplateVo.getOrgId(), invoiceTemplateVo.getTemplateId(), AttachmentsConstants.MODULE_TYPE_AR_TEMPLATE_LOGO, (UploadFileVo) invoiceTemplateVo.getAttachmentLogo());
				} else {
					logger.info("Error in get template logo");
				}
				
				if(invoiceTemplateVo.getAttachmentSign() != null ) {
					attachmentService.encodeAllFiles(invoiceTemplateVo.getOrgId(), invoiceTemplateVo.getTemplateInformation().getId(),AttachmentsConstants.MODULE_TYPE_AR_INVOICE_TEMPLATE_LOGO, invoiceTemplateVo.getAttachmentSign());
				} else {
					logger.info("Error in get template sign");
				}
				
					logger.info("Upload Successfull");
			}
		logger.info("getInvoiceTemplateById executed Successfully in service Layer " +invoiceTemplateVo);
		return invoiceTemplateVo;
	}
	
	public void UpdateInvoiceTemplate(InvoiceTemplateVo invoiceTempVo ) throws ApplicationException {
		logger.info("Entry into updateInvoiceTemplate " + invoiceTempVo);
		if(invoiceTempVo.getTemplateId() != null) {
			logger.info("Entry into updateInvoiceTemplate - Id is : " + invoiceTempVo.getTemplateId());
			 try {
				 invoiceTempVo =  templateDao.updateInvoiceTemplate(invoiceTempVo);
				 if(invoiceTempVo.getTemplateId() != null && !(invoiceTempVo.getTemplateInformation() == null) && invoiceTempVo.getAttachmentLogo() != null && invoiceTempVo.getAttachmentSign() != null) {
						logger.info("Entry into template logo upload");
						attachmentService.upload(AttachmentsConstants.MODULE_TYPE_AR_INVOICE_TEMPLATE_LOGO, invoiceTempVo.getTemplateId(), invoiceTempVo.getOrgId(), invoiceTempVo.getAttachmentLogo());
						logger.info("Entry into template logo upload");
						attachmentService.upload(AttachmentsConstants.MODULE_TYPE_AR_INVOICE_TEMPLATE_SIGN, invoiceTempVo.getTemplateId(), invoiceTempVo.getOrgId(), invoiceTempVo.getAttachmentSign());	 
							
						logger.info("Upload Successfull");
					}				
					
			} catch (Exception e) {
				logger.info("error in updateInvoiceTemplate in service Layer ",e);
				throw e;
			}
			 
		}
	}



	public List<InvoiceTemplateinformationVo> getAllInvoiceTemplate(Integer orgId, String userId, String roleName) throws ApplicationException {
		return templateDao.getInvoiceTemplateList(orgId,userId,roleName);
	}

	public List<InvoiceTemplateVo> getAllInvoiceTemplatePerOrganization(Integer orgId) throws ApplicationException {
		logger.info("Entry into getAllInvoiceTemplatePerOrganization");
		List<InvoiceTemplateVo> invoiceTemplateVos = new  ArrayList<InvoiceTemplateVo>();
		List<Integer> templateIds =  templateDao.getTemplateIds(orgId,ArTemplateConstants.TEMPLATE_TYPE_INV);
		templateIds.forEach(templateId -> {
		 InvoiceTemplateVo invoiceTemplateVo;
		try {
			invoiceTemplateVo = templateDao.getInvoiceTemplate(orgId,templateId);
		
		logger.info("attachment trace - template id" + invoiceTemplateVo.getTemplateInformation().getId());
		logger.info("attachment trace - template info" + invoiceTemplateVo.getTemplateInformation());
		logger.info("attachment trace - template logo" + invoiceTemplateVo.getAttachmentLogo());
		if(invoiceTemplateVo.getTemplateInformation().getId() !=null && !(invoiceTemplateVo.getTemplateInformation() == null) ) 
		{
				logger.info("Entry into template upload");
				if(invoiceTemplateVo.getAttachmentLogo() != null ) {
					  attachmentService.encodeAllFiles(invoiceTemplateVo.getOrgId(), invoiceTemplateVo.getTemplateInformation().getId(),AttachmentsConstants.MODULE_TYPE_AR_INVOICE_TEMPLATE_LOGO, invoiceTemplateVo.getAttachmentLogo());
					// attachmentService.encodeUploadedFile(invoiceTemplateVo.getOrgId(), invoiceTemplateVo.getTemplateId(), AttachmentsConstants.MODULE_TYPE_AR_TEMPLATE_LOGO, (UploadFileVo) invoiceTemplateVo.getAttachmentLogo());
				} else {
					logger.info("Error in get template logo");
				}
				
				if(invoiceTemplateVo.getAttachmentSign() != null ) {
					attachmentService.encodeAllFiles(invoiceTemplateVo.getOrgId(), invoiceTemplateVo.getTemplateInformation().getId(),AttachmentsConstants.MODULE_TYPE_AR_INVOICE_TEMPLATE_SIGN, invoiceTemplateVo.getAttachmentSign());
				} else {
					logger.info("Error in get template sign");
				}
				
					logger.info("Upload Successfull");
			}
		invoiceTemplateVos.add(invoiceTemplateVo);
		logger.info("getInvoiceTemplateById executed Successfully in service Layer " +invoiceTemplateVo);
		} catch (ApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		} );
		return invoiceTemplateVos;
	}
	
	public InvoiceTemplateVo getDefaultTemplateOfAnOrganization(Integer orgId) throws ApplicationException {
    
		logger.info("Entry into getInvoiceTemplate");
		 InvoiceTemplateVo invoiceTemplateVo = templateDao.getDefaultInvoiceTemplateForOrg(orgId);
		logger.info("attachment trace - template id" + invoiceTemplateVo.getTemplateInformation().getId());
		logger.info("attachment trace - template info" + invoiceTemplateVo.getTemplateInformation());
		logger.info("attachment trace - template logo" + invoiceTemplateVo.getAttachmentLogo());
		if(invoiceTemplateVo.getTemplateInformation().getId() !=null && !(invoiceTemplateVo.getTemplateInformation() == null) ) {
				logger.info("Entry into template upload");
				if(invoiceTemplateVo.getAttachmentLogo() != null ) {
					  attachmentService.encodeAllFiles(invoiceTemplateVo.getOrgId(), invoiceTemplateVo.getTemplateInformation().getId(),AttachmentsConstants.MODULE_TYPE_AR_INVOICE_TEMPLATE_LOGO, invoiceTemplateVo.getAttachmentLogo());
					// attachmentService.encodeUploadedFile(invoiceTemplateVo.getOrgId(), invoiceTemplateVo.getTemplateId(), AttachmentsConstants.MODULE_TYPE_AR_TEMPLATE_LOGO, (UploadFileVo) invoiceTemplateVo.getAttachmentLogo());
				} else {
					logger.info("Error in get template logo");
				}
				
				if(invoiceTemplateVo.getAttachmentSign() != null ) {
					attachmentService.encodeAllFiles(invoiceTemplateVo.getOrgId(), invoiceTemplateVo.getTemplateInformation().getId(),AttachmentsConstants.MODULE_TYPE_AR_INVOICE_TEMPLATE_SIGN, invoiceTemplateVo.getAttachmentSign());
				} else {
					logger.info("Error in get template sign");
				}
				
					logger.info("Upload Successfull");
			}
		logger.info("getInvoiceTemplateById executed Successfully in service Layer " +invoiceTemplateVo);
		return invoiceTemplateVo;
	}



	public InvoiceTemplateVo activateDeActivateInvoiceTemplate(int orgId, String orgName, String userId, int id,String roleName, String status) throws ApplicationException, SQLException {
		return templateDao.activateDeActivateInvoiceTemplate(orgId,orgName,userId,id,roleName,status);
	}



	public CreditNoteTemplateVo createCreditNoteTemplate(CreditNoteTemplateVo creditNoteTempVo) throws Exception {
		logger.info("Entry into createCreditNoteTemplate" + creditNoteTempVo.toString());
		try {
			creditNoteTempVo = templateDao.createCreditNoteTemplate(creditNoteTempVo);
			logger.info("createCreditNoteTemplate - service "+creditNoteTempVo);
			if(creditNoteTempVo.getTemplateId()!=null && !(creditNoteTempVo.getTemplateInformation() == null) && creditNoteTempVo.getAttachmentLogo() != null && creditNoteTempVo.getAttachmentSign() != null) {
				logger.info("Entry into template logo upload");
				attachmentService.upload(AttachmentsConstants.MODULE_TYPE_AR_CREDIT_NOTE_TEMPLATE_LOGO, creditNoteTempVo.getTemplateId(), creditNoteTempVo.getOrgId(), creditNoteTempVo.getAttachmentLogo());
				logger.info("Entry into template sign upload");
				attachmentService.upload(AttachmentsConstants.MODULE_TYPE_AR_CREDIT_NOTE_TEMPLATE_SIGN, creditNoteTempVo.getTemplateId(), creditNoteTempVo.getOrgId(), creditNoteTempVo.getAttachmentSign());	 
					
				logger.info("Upload Successfull");
			}
			
		logger.info("Credit Note Template created Successfully in service layer ");
	} catch (Exception e) {
		logger.info("Error in Credit Note Template creation in service layer ");
		throw e;
	}
		return creditNoteTempVo;
	}



	public CreditNoteTemplateVo getCreditNoteTemplate(Integer orgId, Integer templateId) throws ApplicationException {
		logger.info("Entry into getCreditNoteTemplate");
		CreditNoteTemplateVo creditNoteTemplateVo = templateDao.getCreditNoteTemplate(orgId,templateId);
		logger.info("attachment trace - template id" + creditNoteTemplateVo.getTemplateInformation().getId());
		logger.info("attachment trace - template info" + creditNoteTemplateVo.getTemplateInformation());
		logger.info("attachment trace - template logo" + creditNoteTemplateVo.getAttachmentLogo());
		if(creditNoteTemplateVo.getTemplateInformation().getId() !=null && !(creditNoteTemplateVo.getTemplateInformation() == null) ) {
				logger.info("Entry into template upload");
				if(creditNoteTemplateVo.getAttachmentLogo() != null ) {
					  attachmentService.encodeAllFiles(creditNoteTemplateVo.getOrgId(), creditNoteTemplateVo.getTemplateInformation().getId(),AttachmentsConstants.MODULE_TYPE_AR_CREDIT_NOTE_TEMPLATE_LOGO, creditNoteTemplateVo.getAttachmentLogo());
					// attachmentService.encodeUploadedFile(invoiceTemplateVo.getOrgId(), invoiceTemplateVo.getTemplateId(), AttachmentsConstants.MODULE_TYPE_AR_TEMPLATE_LOGO, (UploadFileVo) invoiceTemplateVo.getAttachmentLogo());
				} else {
					logger.info("Error in get template logo");
				}
				
				if(creditNoteTemplateVo.getAttachmentSign() != null ) {
					attachmentService.encodeAllFiles(creditNoteTemplateVo.getOrgId(), creditNoteTemplateVo.getTemplateInformation().getId(),AttachmentsConstants.MODULE_TYPE_AR_CREDIT_NOTE_TEMPLATE_SIGN, creditNoteTemplateVo.getAttachmentSign());
				} else {
					logger.info("Error in get template sign");
				}
				
					logger.info("Upload Successfull");
			}
		logger.info("getCreditNoteTemplate executed Successfully in service Layer " +creditNoteTemplateVo);
		return creditNoteTemplateVo;
	}



	public List<CreditNoteTemplateinformationVo> getAllCreditNoteTemplate(int orgId, String userId,
			String roleName) throws ApplicationException {
		return templateDao.getCreditNoteTemplateList(orgId,userId,roleName);
	}



	public List<CreditNoteTemplateVo> getAllCreditNoteTemplatePerOrganization(Integer orgId) throws ApplicationException {
		logger.info("Entry into getAllCreditNoteTemplatePerOrganization");
		List<CreditNoteTemplateVo> creditNoteTemplateVos = new  ArrayList<CreditNoteTemplateVo>();
		List<Integer> templateIds =  templateDao.getTemplateIds(orgId,ArTemplateConstants.TEMPLATE_TYPE_CN);
		templateIds.forEach(templateId -> {
			CreditNoteTemplateVo creditNoteTemplateVo;
		try {
			creditNoteTemplateVo = templateDao.getCreditNoteTemplate(orgId,templateId);
		
		logger.info("attachment trace - template id" + creditNoteTemplateVo.getTemplateInformation().getId());
		logger.info("attachment trace - template info" + creditNoteTemplateVo.getTemplateInformation());
		logger.info("attachment trace - template logo" + creditNoteTemplateVo.getAttachmentLogo());
		if(creditNoteTemplateVo.getTemplateInformation().getId() !=null && !(creditNoteTemplateVo.getTemplateInformation() == null) ) 
		{
				logger.info("Entry into template upload");
				if(creditNoteTemplateVo.getAttachmentLogo() != null ) {
					  attachmentService.encodeAllFiles(creditNoteTemplateVo.getOrgId(), creditNoteTemplateVo.getTemplateInformation().getId(),AttachmentsConstants.MODULE_TYPE_AR_CREDIT_NOTE_TEMPLATE_LOGO, creditNoteTemplateVo.getAttachmentLogo());
					// attachmentService.encodeUploadedFile(invoiceTemplateVo.getOrgId(), invoiceTemplateVo.getTemplateId(), AttachmentsConstants.MODULE_TYPE_AR_TEMPLATE_LOGO, (UploadFileVo) invoiceTemplateVo.getAttachmentLogo());
				} else {
					logger.info("Error in get template logo");
				}
				
				if(creditNoteTemplateVo.getAttachmentSign() != null ) {
					attachmentService.encodeAllFiles(creditNoteTemplateVo.getOrgId(), creditNoteTemplateVo.getTemplateInformation().getId(),AttachmentsConstants.MODULE_TYPE_AR_CREDIT_NOTE_TEMPLATE_SIGN, creditNoteTemplateVo.getAttachmentSign());
				} else {
					logger.info("Error in get template sign");
				}
				
					logger.info("Upload Successfull");
			}
		creditNoteTemplateVos.add(creditNoteTemplateVo);
		logger.info("getAllCreditNoteTemplatePerOrganization executed Successfully in service Layer " +creditNoteTemplateVo);
		} catch (ApplicationException e) {
			// TODO Auto-generated catch block
			logger.info(e);
		}
		
		} );
		return creditNoteTemplateVos;
	}



	public CreditNoteTemplateVo getDefaultCreditNoteTemplateOfAnOrganization(int orgId) throws ApplicationException {
		logger.info("Entry into getDefaultCreditNoteTemplateOfAnOrganization");
		CreditNoteTemplateVo creditNoteTemplateVo = templateDao.getDefaultCreditNoteTemplateForOrg(orgId);
		logger.info("attachment trace - template id" + creditNoteTemplateVo.getTemplateInformation().getId());
		logger.info("attachment trace - template info" + creditNoteTemplateVo.getTemplateInformation());
		logger.info("attachment trace - template logo" + creditNoteTemplateVo.getAttachmentLogo());
		if(creditNoteTemplateVo.getTemplateInformation().getId() !=null && !(creditNoteTemplateVo.getTemplateInformation() == null) ) {
				logger.info("Entry into template upload");
				if(creditNoteTemplateVo.getAttachmentLogo() != null ) {
					  attachmentService.encodeAllFiles(creditNoteTemplateVo.getOrgId(), creditNoteTemplateVo.getTemplateInformation().getId(),AttachmentsConstants.MODULE_TYPE_AR_CREDIT_NOTE_TEMPLATE_LOGO, creditNoteTemplateVo.getAttachmentLogo());
					// attachmentService.encodeUploadedFile(invoiceTemplateVo.getOrgId(), invoiceTemplateVo.getTemplateId(), AttachmentsConstants.MODULE_TYPE_AR_TEMPLATE_LOGO, (UploadFileVo) invoiceTemplateVo.getAttachmentLogo());
				} else {
					logger.info("Error in get template logo");
				}
				
				if(creditNoteTemplateVo.getAttachmentSign() != null ) {
					attachmentService.encodeAllFiles(creditNoteTemplateVo.getOrgId(), creditNoteTemplateVo.getTemplateInformation().getId(),AttachmentsConstants.MODULE_TYPE_AR_CREDIT_NOTE_TEMPLATE_SIGN, creditNoteTemplateVo.getAttachmentSign());
				} else {
					logger.info("Error in get template sign");
				}
				
					logger.info("Upload Successfull");
			}
		logger.info("getDefaultCreditNoteTemplateOfAnOrganization executed Successfully in service Layer " +creditNoteTemplateVo);
		return creditNoteTemplateVo;
	}



	public CreditNoteTemplateVo UpdateCreditNoteTemplate(CreditNoteTemplateVo creditNoteTempVo) throws Exception {
		logger.info("Entry into UpdateCreditNoteTemplate " + creditNoteTempVo);
		if(creditNoteTempVo.getTemplateId() != null) {
			logger.info("Entry into UpdateCreditNoteTemplate - Id is : " + creditNoteTempVo.getTemplateId());
			 try {
				 creditNoteTempVo =  templateDao.updateCreditNoteTemplate(creditNoteTempVo);
				 if(creditNoteTempVo.getTemplateId() != null && !(creditNoteTempVo.getTemplateInformation() == null) && creditNoteTempVo.getAttachmentLogo() != null && creditNoteTempVo.getAttachmentSign() != null) {
						logger.info("Entry into template logo upload");
						attachmentService.upload(AttachmentsConstants.MODULE_TYPE_AR_CREDIT_NOTE_TEMPLATE_LOGO, creditNoteTempVo.getTemplateId(), creditNoteTempVo.getOrgId(), creditNoteTempVo.getAttachmentLogo());
						logger.info("Entry into template logo upload");
						attachmentService.upload(AttachmentsConstants.MODULE_TYPE_AR_CREDIT_NOTE_TEMPLATE_SIGN, creditNoteTempVo.getTemplateId(), creditNoteTempVo.getOrgId(), creditNoteTempVo.getAttachmentSign());	 
							
						logger.info("Upload Successfull");
					}				
					
			} catch (Exception e) {
				logger.info("error in UpdateCreditNoteTemplate in service Layer ",e);
				throw e;
			}
			 
		}
		return creditNoteTempVo;
	}



	public CreditNoteTemplateVo activateDeActivateCreditNoteTemplate(Integer orgId, String orgName,
			String userId, Integer id, String roleName, String status) throws ApplicationException {
		return templateDao.activateDeActivateCreditNoteTemplate(orgId,orgName,userId,id,roleName,status);
	}
		
	}



	



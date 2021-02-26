package com.blackstrawai.template;



import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.ap.template.PurchaseOrderTemplateInformationVo;
import com.blackstrawai.ap.template.PurchaseOrderTemplateVo;
import com.blackstrawai.attachments.AttachmentService;
import com.blackstrawai.attachments.AttachmentsConstants;
import com.blackstrawai.common.BaseService;


@Service
public class ApTemplateService extends BaseService{

	private Logger logger = Logger.getLogger(ApTemplateService.class);

	@Autowired
	private ApTemplateDao templateDao;
	
	@Autowired
	private AttachmentService attachmentService;
	
		
	public PurchaseOrderTemplateVo createPurchaseOrderTemplate(PurchaseOrderTemplateVo purchaseOrderTemplateVo) throws ApplicationException {
		logger.info("Entry into createPurchaseOrderTemplate" + purchaseOrderTemplateVo.toString());
		try {
			purchaseOrderTemplateVo = templateDao.createPurchaseOrderTemplate(purchaseOrderTemplateVo);
			logger.info("createPurchaseOrderTemplate - service "+purchaseOrderTemplateVo);
			if(purchaseOrderTemplateVo.getTemplateId()!=null && !(purchaseOrderTemplateVo.getTemplateInformation() == null) && purchaseOrderTemplateVo.getAttachmentLogo() != null && purchaseOrderTemplateVo.getAttachmentSign() != null) {
				logger.info("Entry into template logo upload");
				attachmentService.upload(AttachmentsConstants.MODULE_TYPE_AP_PO_TEMPLATE_LOGO, purchaseOrderTemplateVo.getTemplateId(), purchaseOrderTemplateVo.getOrgId(), purchaseOrderTemplateVo.getAttachmentLogo());
				logger.info("Entry into template logo upload");
				attachmentService.upload(AttachmentsConstants.MODULE_TYPE_AP_PO_TEMPLATE_SIGN, purchaseOrderTemplateVo.getTemplateId(), purchaseOrderTemplateVo.getOrgId(), purchaseOrderTemplateVo.getAttachmentSign());	 
					
				logger.info("Upload Successfull");
			}
			
		logger.info("Purchase Order Template created Successfully in service layer ");
	} catch (Exception e) {
		logger.info("Error in Purchase Order Template creation in service layer ");
		throw e;
	}
		return purchaseOrderTemplateVo;
	}
	


	public PurchaseOrderTemplateVo getPurchaseOrderTemplate(Integer orgId , Integer templateId) throws ApplicationException {
		logger.info("Entry into getPurchaseOrderTemplate");
		PurchaseOrderTemplateVo purchaseOrderTemplateVo = templateDao.getPurchaseOrderTemplate(orgId,templateId);
		logger.info("attachment trace - template id" + purchaseOrderTemplateVo.getTemplateInformation().getId());
		logger.info("attachment trace - template info" + purchaseOrderTemplateVo.getTemplateInformation());
		logger.info("attachment trace - template logo" + purchaseOrderTemplateVo.getAttachmentLogo());
		if(purchaseOrderTemplateVo.getTemplateInformation().getId() !=null && !(purchaseOrderTemplateVo.getTemplateInformation() == null) ) {
				logger.info("Entry into template upload");
				if(purchaseOrderTemplateVo.getAttachmentLogo() != null ) {
					  attachmentService.encodeAllFiles(purchaseOrderTemplateVo.getOrgId(), purchaseOrderTemplateVo.getTemplateInformation().getId(),AttachmentsConstants.MODULE_TYPE_AP_PO_TEMPLATE_LOGO, purchaseOrderTemplateVo.getAttachmentLogo());
					// attachmentService.encodeUploadedFile(invoiceTemplateVo.getOrgId(), invoiceTemplateVo.getTemplateId(), AttachmentsConstants.MODULE_TYPE_AR_TEMPLATE_LOGO, (UploadFileVo) invoiceTemplateVo.getAttachmentLogo());
				} else {
					logger.info("Error in get template logo");
				}
				
				if(purchaseOrderTemplateVo.getAttachmentSign() != null ) {
					attachmentService.encodeAllFiles(purchaseOrderTemplateVo.getOrgId(), purchaseOrderTemplateVo.getTemplateInformation().getId(),AttachmentsConstants.MODULE_TYPE_AP_PO_TEMPLATE_SIGN, purchaseOrderTemplateVo.getAttachmentSign());
				} else {
					logger.info("Error in get template sign");
				}
				
					logger.info("Upload Successfull");
			}
		logger.info("getPurchaseOrderTemplateById executed Successfully in service Layer " +purchaseOrderTemplateVo);
		return purchaseOrderTemplateVo;
	}
	
	public void UpdatePurchaseOrderTemplate(PurchaseOrderTemplateVo purchaseOrderTempVo ) throws ApplicationException {
		logger.info("Entry into updatePurchaseOrderTemplate " + purchaseOrderTempVo);
		if(purchaseOrderTempVo.getTemplateId() != null) {
			logger.info("Entry into updatePurchaseOrderTemplate - Id is : " + purchaseOrderTempVo.getTemplateId());
			 try {
				 purchaseOrderTempVo =  templateDao.updatePurchaseOrderTemplate(purchaseOrderTempVo);
				 if(purchaseOrderTempVo.getTemplateId() != null && !(purchaseOrderTempVo.getTemplateInformation() == null) && purchaseOrderTempVo.getAttachmentLogo() != null && purchaseOrderTempVo.getAttachmentSign() != null) {
						logger.info("Entry into template logo upload");
						attachmentService.upload(AttachmentsConstants.MODULE_TYPE_AP_PO_TEMPLATE_LOGO, purchaseOrderTempVo.getTemplateId(), purchaseOrderTempVo.getOrgId(), purchaseOrderTempVo.getAttachmentLogo());
						logger.info("Entry into template logo upload");
						attachmentService.upload(AttachmentsConstants.MODULE_TYPE_AP_PO_TEMPLATE_SIGN, purchaseOrderTempVo.getTemplateId(), purchaseOrderTempVo.getOrgId(), purchaseOrderTempVo.getAttachmentSign());	 
							
						logger.info("Upload Successfull");
					}				
					
			} catch (Exception e) {
				logger.info("error in updatePurchaseOrderTemplate in service Layer ",e);
				throw e;
			}
			 
		}
	}



	public List<PurchaseOrderTemplateInformationVo> getAllPurchaseOrderTemplate(Integer orgId, String userId, String roleName) throws ApplicationException {
		return templateDao.getPurchaseOrderTemplateList(orgId,userId,roleName);
	}

	public List<PurchaseOrderTemplateVo> getAllPurchaseOrderTemplatePerOrganization(Integer orgId) throws ApplicationException {
		logger.info("Entry into getAllPurchaseOrderTemplatePerOrganization");
		List<PurchaseOrderTemplateVo> purchaseOrderTemplateVos = new  ArrayList<PurchaseOrderTemplateVo>();
		List<Integer> templateIds =  templateDao.getTemplateIds(orgId,ApTemplateConstants.TEMPLATE_TYPE_PURCHASE_ORDER);
		templateIds.forEach(templateId -> {
			PurchaseOrderTemplateVo purchaseOrderTemplateVo;
		try {
			purchaseOrderTemplateVo = templateDao.getPurchaseOrderTemplate(orgId,templateId);
		
		logger.info("attachment trace - template id" + purchaseOrderTemplateVo.getTemplateInformation().getId());
		logger.info("attachment trace - template info" + purchaseOrderTemplateVo.getTemplateInformation());
		logger.info("attachment trace - template logo" + purchaseOrderTemplateVo.getAttachmentLogo());
		if(purchaseOrderTemplateVo.getTemplateInformation().getId() !=null && !(purchaseOrderTemplateVo.getTemplateInformation() == null) ) 
		{
				logger.info("Entry into template upload");
				if(purchaseOrderTemplateVo.getAttachmentLogo() != null ) {
					  attachmentService.encodeAllFiles(purchaseOrderTemplateVo.getOrgId(), purchaseOrderTemplateVo.getTemplateInformation().getId(),AttachmentsConstants.MODULE_TYPE_AP_PO_TEMPLATE_LOGO, purchaseOrderTemplateVo.getAttachmentLogo());
					// attachmentService.encodeUploadedFile(invoiceTemplateVo.getOrgId(), invoiceTemplateVo.getTemplateId(), AttachmentsConstants.MODULE_TYPE_AR_TEMPLATE_LOGO, (UploadFileVo) invoiceTemplateVo.getAttachmentLogo());
				} else {
					logger.info("Error in get template logo");
				}
				
				if(purchaseOrderTemplateVo.getAttachmentSign() != null ) {
					attachmentService.encodeAllFiles(purchaseOrderTemplateVo.getOrgId(), purchaseOrderTemplateVo.getTemplateInformation().getId(),AttachmentsConstants.MODULE_TYPE_AP_PO_TEMPLATE_SIGN, purchaseOrderTemplateVo.getAttachmentSign());
				} else {
					logger.info("Error in get template sign");
				}
				
					logger.info("Upload Successfull");
			}
		purchaseOrderTemplateVos.add(purchaseOrderTemplateVo);
		logger.info("getPurchaseOrderTemplateById executed Successfully in service Layer " +purchaseOrderTemplateVo);
		} catch (ApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		} );
		return purchaseOrderTemplateVos;
	}
	
	public PurchaseOrderTemplateVo getDefaultTemplateOfAnOrganization(Integer orgId) throws ApplicationException {
    
		logger.info("Entry into getInvoiceTemplate");
		 PurchaseOrderTemplateVo purchaseOrderTemplateVo = templateDao.getDefaultPurchaseOrderTemplateForOrg(orgId);
		logger.info("attachment trace - template id" + purchaseOrderTemplateVo.getTemplateInformation().getId());
		logger.info("attachment trace - template info" + purchaseOrderTemplateVo.getTemplateInformation());
		logger.info("attachment trace - template logo" + purchaseOrderTemplateVo.getAttachmentLogo());
		if(purchaseOrderTemplateVo.getTemplateInformation().getId() !=null && !(purchaseOrderTemplateVo.getTemplateInformation() == null) ) {
				logger.info("Entry into template upload");
				if(purchaseOrderTemplateVo.getAttachmentLogo() != null ) {
					  attachmentService.encodeAllFiles(purchaseOrderTemplateVo.getOrgId(), purchaseOrderTemplateVo.getTemplateInformation().getId(),AttachmentsConstants.MODULE_TYPE_AP_PO_TEMPLATE_LOGO, purchaseOrderTemplateVo.getAttachmentLogo());
					// attachmentService.encodeUploadedFile(invoiceTemplateVo.getOrgId(), invoiceTemplateVo.getTemplateId(), AttachmentsConstants.MODULE_TYPE_AR_TEMPLATE_LOGO, (UploadFileVo) invoiceTemplateVo.getAttachmentLogo());
				} else {
					logger.info("Error in get template logo");
				}
				
				if(purchaseOrderTemplateVo.getAttachmentSign() != null ) {
					attachmentService.encodeAllFiles(purchaseOrderTemplateVo.getOrgId(), purchaseOrderTemplateVo.getTemplateInformation().getId(),AttachmentsConstants.MODULE_TYPE_AP_PO_TEMPLATE_SIGN, purchaseOrderTemplateVo.getAttachmentSign());
				} else {
					logger.info("Error in get template sign");
				}
				
					logger.info("Upload Successfull");
			}
		logger.info("getInvoiceTemplateById executed Successfully in service Layer " +purchaseOrderTemplateVo);
		return purchaseOrderTemplateVo;
	}



	public PurchaseOrderTemplateVo activateDeActivatePurchaseOrderTemplate(int orgId, String orgName, String userId, int id,String roleName, String status) throws ApplicationException, SQLException {
		return templateDao.activateDeActivatePurchaseOrderTemplate(orgId,orgName,userId,id,roleName,status);
	}


		
	}



	



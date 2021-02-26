package com.blackstrawai.helper;

import java.util.ArrayList;
import java.util.List;

import com.blackstrawai.ap.template.PurchaseOrderTemplateInformationVo;
import com.blackstrawai.ap.template.PurchaseOrderTemplateVo;
import com.blackstrawai.attachments.UploadFileVo;
import com.blackstrawai.request.ap.template.PurchaseOrderTemplateInfoRequest;
import com.blackstrawai.request.ap.template.PurchaseOrderTemplateRequest;

public class ApTemplateConvertToVoHelper {
	
	private static ApTemplateConvertToVoHelper apTemplateConvertToVoHelper;

	public static ApTemplateConvertToVoHelper getInstance() {
		if (apTemplateConvertToVoHelper == null) {
			apTemplateConvertToVoHelper = new ApTemplateConvertToVoHelper();
		}
		return apTemplateConvertToVoHelper;
	}
	
	public PurchaseOrderTemplateVo convertApPurchaseOrderTemplateVoFromRequest(PurchaseOrderTemplateRequest purchaseOrderTempRequest) {
		PurchaseOrderTemplateVo purchaseOrderTempVo = new PurchaseOrderTemplateVo();
		purchaseOrderTempVo.setTemplateId(purchaseOrderTempRequest.getTemplateId());
		purchaseOrderTempVo.setOrgId(purchaseOrderTempRequest.getOrgId());
		purchaseOrderTempVo.setRoleName(purchaseOrderTempRequest.getRoleName());
		purchaseOrderTempVo.setIsSuperAdmin(purchaseOrderTempRequest.getIsSuperAdmin());
		purchaseOrderTempVo.setUserId(purchaseOrderTempRequest.getUserId());
		purchaseOrderTempVo.setTemplateInformation(purchaseOrderTempRequest.getTemplateInformation() != null
				? convertApPurchaseOrderTemplateInfoVoFromRequest(purchaseOrderTempRequest.getTemplateInformation()):null);

		if(purchaseOrderTempRequest.getAttachmentLogo() != null && purchaseOrderTempRequest.getAttachmentLogo().size() > 0) {
			List<UploadFileVo> uploadList = new ArrayList<UploadFileVo>();
			purchaseOrderTempRequest.getAttachmentLogo().forEach(file -> {
				uploadList.add(ConvertToVoHelper.getInstance().convertAttachmentFromReq(file));
			});
			purchaseOrderTempVo.setAttachmentLogo(uploadList);
		}
		if(purchaseOrderTempRequest.getAttachmentSign() != null && purchaseOrderTempRequest.getAttachmentSign().size() > 0) {
			List<UploadFileVo> uploadList = new ArrayList<UploadFileVo>();
			purchaseOrderTempRequest.getAttachmentSign().forEach(file -> {
				uploadList.add(ConvertToVoHelper.getInstance().convertAttachmentFromReq(file));
			});
			purchaseOrderTempVo.setAttachmentSign(uploadList);
		}
		purchaseOrderTempVo.setAttachmentsToRemove(purchaseOrderTempRequest.getAttachmentsToRemove());
		purchaseOrderTempVo.setStatus(purchaseOrderTempRequest.getStatus());
		purchaseOrderTempVo.setOrgContactNo(purchaseOrderTempRequest.getOrgContactNo());
		return purchaseOrderTempVo;

	}

	private PurchaseOrderTemplateInformationVo convertApPurchaseOrderTemplateInfoVoFromRequest(
			PurchaseOrderTemplateInfoRequest generalTempInfoRequest) {
		PurchaseOrderTemplateInformationVo generalTemplateInfoVo = new PurchaseOrderTemplateInformationVo();
		generalTemplateInfoVo.setOrganizationId(generalTempInfoRequest.getOrganizationId());
		generalTemplateInfoVo.setLogoPosition(generalTempInfoRequest.getLogoPosition());
		generalTemplateInfoVo.setHeaderNotes(generalTempInfoRequest.getHeaderNotes());
		generalTemplateInfoVo.setFooterSectionNotes(generalTempInfoRequest.getFooterSectionNotes());
		generalTemplateInfoVo.setTermsAndContd(generalTempInfoRequest.getTermsAndContd());
		generalTemplateInfoVo.setBankAccInfo(generalTempInfoRequest.getBankAccInfo());
		generalTemplateInfoVo.setFootNotes(generalTempInfoRequest.getFootNotes());
		generalTemplateInfoVo.setFootNotePosition(generalTempInfoRequest.getFootNotePosition());
		generalTemplateInfoVo.setIsAnnex(generalTempInfoRequest.getIsAnnex());
		generalTemplateInfoVo.setUserId(generalTempInfoRequest.getUserId());
		generalTemplateInfoVo.setUpdateUserId(generalTempInfoRequest.getUpdateUserId());
		generalTemplateInfoVo.setUpdateRoleName(generalTempInfoRequest.getUpdateRoleName());
		generalTemplateInfoVo.setRoleName(generalTempInfoRequest.getRoleName());
		generalTemplateInfoVo.setStatus(generalTempInfoRequest.getStatus());
		generalTemplateInfoVo.setTemplateName(generalTempInfoRequest.getTemplateName());
		return generalTemplateInfoVo;

	}

	
		
}

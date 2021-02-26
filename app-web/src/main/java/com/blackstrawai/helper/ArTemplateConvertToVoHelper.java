package com.blackstrawai.helper;

import java.util.ArrayList;
import java.util.List;

import com.blackstrawai.ar.template.CreditNoteTemplateVo;
import com.blackstrawai.ar.template.CreditNoteTemplateinformationVo;
import com.blackstrawai.ar.template.InvoiceTemplateVo;
import com.blackstrawai.ar.template.InvoiceTemplateinformationVo;
import com.blackstrawai.attachments.UploadFileVo;
import com.blackstrawai.request.ar.template.CreditNoteTemplateInfoRequest;
import com.blackstrawai.request.ar.template.CreditNoteTemplateRequest;
import com.blackstrawai.request.ar.template.InvoiceTemplateInfoRequest;
import com.blackstrawai.request.ar.template.InvoiceTemplateRequest;

public class ArTemplateConvertToVoHelper {
	
	private static ArTemplateConvertToVoHelper arTemplateConvertToVoHelper;

	public static ArTemplateConvertToVoHelper getInstance() {
		if (arTemplateConvertToVoHelper == null) {
			arTemplateConvertToVoHelper = new ArTemplateConvertToVoHelper();
		}
		return arTemplateConvertToVoHelper;
	}
	
	public InvoiceTemplateVo convertArInvoiceTemplateVoFromRequest(InvoiceTemplateRequest invoiceTempRequest) {
		InvoiceTemplateVo invoiceTempVo = new InvoiceTemplateVo();
		invoiceTempVo.setTemplateId(invoiceTempRequest.getTemplateId());
		invoiceTempVo.setOrgId(invoiceTempRequest.getOrgId());
		invoiceTempVo.setRoleName(invoiceTempRequest.getRoleName());
		invoiceTempVo.setIsSuperAdmin(invoiceTempRequest.getIsSuperAdmin());
		invoiceTempVo.setUserId(invoiceTempRequest.getUserId());
		invoiceTempVo.setTemplateInformation(invoiceTempRequest.getTemplateInformation() != null
				? convertArInvoiceTemplateInfoVoFromRequest(invoiceTempRequest.getTemplateInformation()):null);

		if(invoiceTempRequest.getAttachmentLogo() != null && invoiceTempRequest.getAttachmentLogo().size() > 0) {
			List<UploadFileVo> uploadList = new ArrayList<UploadFileVo>();
			invoiceTempRequest.getAttachmentLogo().forEach(file -> {
				uploadList.add(ConvertToVoHelper.getInstance().convertAttachmentFromReq(file));
			});
			invoiceTempVo.setAttachmentLogo(uploadList);
		}
		if(invoiceTempRequest.getAttachmentSign() != null && invoiceTempRequest.getAttachmentSign().size() > 0) {
			List<UploadFileVo> uploadList = new ArrayList<UploadFileVo>();
			invoiceTempRequest.getAttachmentSign().forEach(file -> {
				uploadList.add(ConvertToVoHelper.getInstance().convertAttachmentFromReq(file));
			});
			invoiceTempVo.setAttachmentSign(uploadList);
		}
		invoiceTempVo.setAttachmentsToRemove(invoiceTempRequest.getAttachmentsToRemove());
		invoiceTempVo.setStatus(invoiceTempRequest.getStatus());
		invoiceTempVo.setOrgContactNo(invoiceTempRequest.getOrgContactNo());
		return invoiceTempVo;

	}

	private InvoiceTemplateinformationVo convertArInvoiceTemplateInfoVoFromRequest(
			InvoiceTemplateInfoRequest generalTempInfoRequest) {
		InvoiceTemplateinformationVo generalTemplateInfoVo = new InvoiceTemplateinformationVo();
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

	
	public CreditNoteTemplateVo convertArCreditNoteTemplateVoFromRequest(CreditNoteTemplateRequest creditNoteTempRequest) {
		CreditNoteTemplateVo creditNoteTempVo = new CreditNoteTemplateVo();
		creditNoteTempVo.setTemplateId(creditNoteTempRequest.getTemplateId());
		creditNoteTempVo.setOrgId(creditNoteTempRequest.getOrgId());
		creditNoteTempVo.setRoleName(creditNoteTempRequest.getRoleName());
		creditNoteTempVo.setIsSuperAdmin(creditNoteTempRequest.getIsSuperAdmin());
		creditNoteTempVo.setUserId(creditNoteTempRequest.getUserId());
		creditNoteTempVo.setTemplateInformation(creditNoteTempRequest.getTemplateInformation() != null
				? convertArCreditNoteTemplateInfoVoFromRequest(creditNoteTempRequest.getTemplateInformation()):null);

		if(creditNoteTempRequest.getAttachmentLogo() != null && creditNoteTempRequest.getAttachmentLogo().size() > 0) {
			List<UploadFileVo> uploadList = new ArrayList<UploadFileVo>();
			creditNoteTempRequest.getAttachmentLogo().forEach(file -> {
				uploadList.add(ConvertToVoHelper.getInstance().convertAttachmentFromReq(file));
			});
			creditNoteTempVo.setAttachmentLogo(uploadList);
		}
		if(creditNoteTempRequest.getAttachmentSign() != null && creditNoteTempRequest.getAttachmentSign().size() > 0) {
			List<UploadFileVo> uploadList = new ArrayList<UploadFileVo>();
			creditNoteTempRequest.getAttachmentSign().forEach(file -> {
				uploadList.add(ConvertToVoHelper.getInstance().convertAttachmentFromReq(file));
			});
			creditNoteTempVo.setAttachmentSign(uploadList);
		}
		creditNoteTempVo.setAttachmentsToRemove(creditNoteTempRequest.getAttachmentsToRemove());
		creditNoteTempVo.setStatus(creditNoteTempRequest.getStatus());
		creditNoteTempVo.setOrgContactNo(creditNoteTempRequest.getOrgContactNo());
		return creditNoteTempVo;
	}
	
	
	private CreditNoteTemplateinformationVo convertArCreditNoteTemplateInfoVoFromRequest(CreditNoteTemplateInfoRequest generalTempInfoRequest) {
		CreditNoteTemplateinformationVo generalTemplateInfoVo = new CreditNoteTemplateinformationVo();
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

package com.blackstrawai.onboarding;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.attachments.AttachmentService;
import com.blackstrawai.attachments.AttachmentsConstants;
import com.blackstrawai.common.BaseService;
import com.blackstrawai.onboarding.organization.BasicOrganizationVo;
import com.blackstrawai.onboarding.organization.IncomeTaxLoginVo;
import com.blackstrawai.onboarding.organization.MinimalOrganizationVo;
import com.blackstrawai.onboarding.organization.NewOrganizationVo;
import com.blackstrawai.onboarding.organization.OrganizationDropDownVo;
import com.blackstrawai.onboarding.organization.UserTypeOrganizationVo;
import com.blackstrawai.taxilla.Taxilla;
@Service
public class OrganizationService extends BaseService {
	
	@Autowired
	OrganizationDao organizationDao;
	
	@Autowired
	private AttachmentService attachmentService;
	
	private  Logger logger = Logger.getLogger(OrganizationService.class);	
	

	
	public BasicOrganizationVo createOrganization(NewOrganizationVo organizationVo)throws ApplicationException {	
		logger.info("Entry into method:createOrganization");
		BasicOrganizationVo data=organizationDao.createOrganization(organizationVo);		
		NewOrganizationThread orgThread=new NewOrganizationThread(attachmentService,organizationDao,organizationVo.getId(),organizationVo.getUserId(),organizationVo.getAttachments());
		orgThread.start();
		return data;
	}
	
	
	public BasicOrganizationVo createMinimalOrganization(MinimalOrganizationVo organizationVo)throws ApplicationException {	
		logger.info("Entry into method:createMinimalOrganization");
		Map<String,String> gstMap=organizationVo.getGst();
		if(gstMap.containsKey("gstNo")){
			String value=gstMap.get("gstNo");
			gstMap.put("gstData", Taxilla.getInstance().getByGstNo(value).toJSONString());
		}
		Map<String,String>otherGstData=new HashMap<String,String>();
		for(int i=0;i<organizationVo.getOtherGsts().size();i++){
			String otherGST=organizationVo.getOtherGsts().get(i);
			otherGstData.put(otherGST, Taxilla.getInstance().getByGstNo(otherGST).toJSONString());
		}
		BasicOrganizationVo data=organizationDao.createMinimalOrganization(organizationVo,otherGstData);		
		NewOrganizationThread orgThread=new NewOrganizationThread(attachmentService,organizationDao,organizationVo.getId(),organizationVo.getUserId(),null);
		orgThread.start();
		return data;
	}
	
	public BasicOrganizationVo updateOrganization(NewOrganizationVo organizationVo)throws ApplicationException {	
		logger.info("Entry into method:updateOrganization");	
		 BasicOrganizationVo basicOrganizationVo=organizationDao.updateOrganization(organizationVo);
		 attachmentService.upload(AttachmentsConstants.MODULE_TYPE_ORG_DOCUMENTS, organizationVo.getId(),  organizationVo.getId(), organizationVo.getAttachments());
		 return basicOrganizationVo;
	}
	
	public BasicOrganizationVo updateMinimalOrganization(MinimalOrganizationVo organizationVo)throws ApplicationException {	
		logger.info("Entry into method:updateOrganization");	
		 BasicOrganizationVo basicOrganizationVo=organizationDao.updateMinimalOrganization(organizationVo);
		 return basicOrganizationVo;
	}
	
	public BasicOrganizationVo deleteOrganization(int organizationId,String status)throws ApplicationException {	
		logger.info("Entry into method:deleteOrganization");
		return organizationDao.deleteOrganization(organizationId, status);
	}	

	
	public List<UserTypeOrganizationVo> getAllOrganizationsByEmailAndPhone(String emailId, String phoneNo)throws ApplicationException {	
		logger.info("Entry into method:getAllOrganizationsByEmailAndUserId");
		return organizationDao.getAllOrganizationsByEmailAndPhone (emailId,phoneNo);
	}
	
	public NewOrganizationVo getOrganization(int userId,int organizationId)throws ApplicationException {	
		logger.info("Entry into method:getOrganization");
		NewOrganizationVo organizationVo= organizationDao.getOrganization(organizationId);
		if(organizationVo!=null && organizationVo.getAttachments().size()>0 ) {
			attachmentService.encodeAllFiles(organizationVo.getId(), organizationVo.getId(), AttachmentsConstants.MODULE_TYPE_ORG_DOCUMENTS, organizationVo.getAttachments());
		}
		return organizationVo;		
	}
	
	
	public MinimalOrganizationVo getMinimalOrganization(int userId,int organizationId)throws ApplicationException {	
		logger.info("Entry into method:getOrganization");
		MinimalOrganizationVo organizationVo= organizationDao.getMinimalOrganization(organizationId);		
		return organizationVo;		
	}
	
	
	public int getOrganizationBaseCurrency(int organizationId)throws ApplicationException {	
		logger.info("Entry into method:getOrganizationBaseCurrency");
		return organizationDao.getOrganizationBaseCurrency(organizationId);
		
	}
	

	public OrganizationDropDownVo getOrganizationDropDownData()throws ApplicationException {
		logger.info("Entry into getOrganizationDropDownData");
		return organizationDao.getOrganizationDropDownDataV2();
	}
	
	public List<BasicOrganizationVo>getAllNewOrganizations(int userId)throws ApplicationException {	
		logger.info("Entry into method:getAllOrganizations");
		return organizationDao.getAllNewOrganizations(userId);
	}
	public List<BasicOrganizationVo>getOrganizationsByEmailWithRoles(String email)throws ApplicationException {	
		logger.info("Entry into method:getOrganizationsByEmailWithRoles");
		return organizationDao.getOrganizationsByEmailWithRoles(email);
	}
	
	public IncomeTaxLoginVo getIncomeTaxLoginDetails(int organizationId)throws ApplicationException {
		logger.info("Entry into getIncomeTaxLoginDetails");
		return organizationDao.getIncomeTaxLoginDetails(organizationId);
	}


}

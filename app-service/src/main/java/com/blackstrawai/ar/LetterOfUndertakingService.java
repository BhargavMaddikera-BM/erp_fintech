package com.blackstrawai.ar;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.ar.dropdowns.LetterOfUndertakingDropDownVo;
import com.blackstrawai.ar.lut.LetterOfUndertakingVo;
import com.blackstrawai.attachments.AttachmentService;
import com.blackstrawai.attachments.AttachmentsConstants;
import com.blackstrawai.common.BaseService;
import com.blackstrawai.common.DropDownDao;

@Service
public class LetterOfUndertakingService extends BaseService {

	@Autowired
	LetterOfUndertakingDao letterOfUndertakingDao;

	@Autowired
	DropDownDao dropDownDao;

	
	@Autowired
	private AttachmentService attachmentService;
	
	private Logger logger = Logger.getLogger(LetterOfUndertakingService.class);

	public LetterOfUndertakingVo createLetterOfUndertaking(LetterOfUndertakingVo letterOfUndertakingVo) throws ApplicationException {
		logger.info("Entry into method:createLetterOfUndertaking");
		letterOfUndertakingVo=letterOfUndertakingDao.createLetterOfUndertaking(letterOfUndertakingVo);
		if(letterOfUndertakingVo.getId()>0 && letterOfUndertakingVo.getAttachments()!=null && !letterOfUndertakingVo.getAttachments().isEmpty()) {
			logger.info("Entry into upload");
				attachmentService.upload(AttachmentsConstants.MODULE_TYPE_ACCOUNTS_RECEIVABLES_LUT, letterOfUndertakingVo.getId(), letterOfUndertakingVo.getOrganizationId(), letterOfUndertakingVo.getAttachments());
				logger.info("Upload Successfull");
		}
		return  letterOfUndertakingVo;
	}
	public List<LetterOfUndertakingVo> getAllLetterOfUndertakingsOfAnOrganizationForUserAndRole(int OrganizationId,String userId,String roleName) throws ApplicationException {
		logger.info("Entry into method:getAllLetterOfUndertakingsOfAnOrganization");
		List<LetterOfUndertakingVo> letterOfUndertakingList= letterOfUndertakingDao.getAllLetterOfUndertakingsOfAnOrganizationForUserAndRole(OrganizationId,userId,roleName);
		
		return letterOfUndertakingList;
	}

	public LetterOfUndertakingVo deleteLetterOfUndertaking(int id,String status,String userId,String roleName) throws ApplicationException {
		logger.info("Entry into method:deleteLetterOfUndertaking");
		return letterOfUndertakingDao.deleteLetterOfUndertaking(id, status,userId,roleName);
	}


	public LetterOfUndertakingVo updateLetterOfUndertaking(LetterOfUndertakingVo letterOfUndertakingVo) throws ApplicationException {
		logger.info("Entry into method:updateLetterOfUndertaking");
		if( letterOfUndertakingVo.getAttachments()!=null && !letterOfUndertakingVo.getAttachments().isEmpty() && letterOfUndertakingVo.getId()>0) {
			logger.info("Entry into upload");
			attachmentService.upload(AttachmentsConstants.MODULE_TYPE_ACCOUNTS_RECEIVABLES_LUT, letterOfUndertakingVo.getId(), letterOfUndertakingVo.getOrganizationId(), letterOfUndertakingVo.getAttachments());
			logger.info("Upload Successfull");
		}
		
		return letterOfUndertakingDao.updateLetterOfUndertaking(letterOfUndertakingVo);
	}
	public LetterOfUndertakingVo getLetterOfUndertakingById(int id) throws ApplicationException {
		logger.info("Entry into method:getLetterOfUndertakingById");
		LetterOfUndertakingVo letterOfUndertakingVo=letterOfUndertakingDao.getLetterOfUndertakingById(id);
		if(letterOfUndertakingVo!=null  && letterOfUndertakingVo.getAttachments()!=null && letterOfUndertakingVo.getAttachments().size()>0 && letterOfUndertakingVo.getId()>0) {
			attachmentService.encodeAllFiles(letterOfUndertakingVo.getOrganizationId(), letterOfUndertakingVo.getId(),AttachmentsConstants.MODULE_TYPE_ACCOUNTS_RECEIVABLES_LUT, letterOfUndertakingVo.getAttachments());
		}
		return letterOfUndertakingVo;
	}
	
	public LetterOfUndertakingDropDownVo getLutDropdownData(int organizationId) throws ApplicationException {
		logger.info("Entry into method: getLetterOfUndertakingDropdown");
		
		return dropDownDao.getLUTDropDown(organizationId);
	}
	
}


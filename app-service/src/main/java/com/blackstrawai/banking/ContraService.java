package com.blackstrawai.banking;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.attachments.AttachmentService;
import com.blackstrawai.attachments.AttachmentsConstants;
import com.blackstrawai.banking.contra.ContraBaseVo;
import com.blackstrawai.banking.contra.ContraVo;
import com.blackstrawai.banking.dropdowns.ContraDropDownVo;
import com.blackstrawai.common.BaseService;
import com.blackstrawai.common.DropDownDao;
import com.blackstrawai.export.ContraExportVo;
import com.blackstrawai.export.ExportVo;
import com.blackstrawai.journals.JournalEntriesConstants;
import com.blackstrawai.journals.JournalEntriesThread;
import com.blackstrawai.journals.JournalEntriesTransactionDao;
import com.blackstrawai.upload.ContraUploadVo;

@Service
public class ContraService extends BaseService {

	@Autowired
	ContraDao contraDao;

	@Autowired
	DropDownDao dropDownDao;

	@Autowired
	AttachmentService attachmentService;
	
	@Autowired
	private JournalEntriesTransactionDao journalEntriesTransactionDao;

	private Logger logger = Logger.getLogger(ContraService.class);

	public void createContraAccounts(ContraVo contraVo) throws ApplicationException {
		logger.info("Entry into method: createContraAccounts");
		try {
			ContraVo vo = contraDao.createContraAccounts(contraVo);
			if (!contraVo.getAttachments().isEmpty() && contraVo.getId() != null) {
				logger.info("Entry into upload");
				attachmentService.upload(AttachmentsConstants.MODULE_TYPE_BANK_CONTRA, contraVo.getId(),
						contraVo.getOrgId(), contraVo.getAttachments());
				logger.info("Upload Successful");
			}
			
			if ((vo.getId() != null) && (vo.getStatus().equals("ACT")|| vo.getStatus().equals("ACTIVE"))) {
				logger.info("To thread");
				JournalEntriesThread journalThread = new JournalEntriesThread(journalEntriesTransactionDao,
						vo.getId(), vo.getOrgId(),
						JournalEntriesConstants.SUB_MODULE_CONTRA);
				journalThread.start();
			}
		} catch (Exception e) {
			contraDao.deleteContraEntries(contraVo.getId(),contraVo.getUserId(),contraVo.getRoleName());
			throw new ApplicationException(e.getMessage());

		}

	}

	public List<ContraBaseVo> getAllContraAccountsOfAnOrganizationForUserAndRole(int organizationId,String userId,String roleName) throws ApplicationException {
		logger.info("Entry into method: getAllContraAccountsOfAnOrganization");
		return contraDao.getAllContraAccountsOfAnOrganizationForUserAndRole(organizationId,userId,roleName);
	}

	public ContraVo getContraAccountById(int id) throws ApplicationException {
		logger.info("Entry into method: getContraAccountById");
		ContraVo contraVo;
		try {
			contraVo = contraDao.getContraAccountById(id);
			if (contraVo != null && contraVo.getAttachments().size() > 0 && contraVo.getId() != null) {
				attachmentService.encodeAllFiles(contraVo.getOrgId(), contraVo.getId(),
						AttachmentsConstants.MODULE_TYPE_BANK_CONTRA, contraVo.getAttachments());
			}
		} catch (Exception e) {
			throw new ApplicationException(e.getMessage());

		}
		return contraVo;
	}

	public void updateContraAccount(ContraVo contraVo) throws ApplicationException {
		logger.info("Entry into method: updateContraAccount");
		try {
			boolean isTxnSuccess = contraDao.updateContraAccount(contraVo);
			if (isTxnSuccess && !contraVo.getAttachments().isEmpty() && contraVo.getId() != null) {
				logger.info("Entry into upload");
				attachmentService.upload(AttachmentsConstants.MODULE_TYPE_BANK_CONTRA, contraVo.getId(),
						contraVo.getOrgId(), contraVo.getAttachments());
				logger.info("Upload Successfull");
			}
			
			if ((contraVo.getId() != null) && (contraVo.getStatus().equals("ACT")|| contraVo.getStatus().equals("ACTIVE"))) {
				logger.info("To thread");
				JournalEntriesThread journalThread = new JournalEntriesThread(journalEntriesTransactionDao,
						contraVo.getId(), contraVo.getOrgId(),
						JournalEntriesConstants.SUB_MODULE_CONTRA);
				journalThread.start();
			}
			
		} catch (Exception e) {
			throw new ApplicationException(e.getMessage());

		}
	}

	public ContraDropDownVo getContraDropDownData(int organizationId) throws ApplicationException {
		logger.info("Entry into method: getContraDropDownData");
		return dropDownDao.getContraDropDownData(organizationId);
	}

	public Map<String, String> processUpload(List<ContraUploadVo> contraEntriesList, Integer orgId, String userId,
			boolean isSuperAdmin, boolean duplicacy, String roleName) {
		logger.info("Entry into processUpload");
		Map<String, String> map = new HashMap<String, String>();
		try {
			ContraUploadVo contraVo=contraDao.processUpload(contraEntriesList, orgId, userId, isSuperAdmin,duplicacy,roleName);
			if(contraVo!=null && contraVo.getStatus().equals("ACT")){
				JournalEntriesThread journalThread = new JournalEntriesThread(journalEntriesTransactionDao,
						Integer.parseInt(contraVo.getAccountype()), orgId,
						JournalEntriesConstants.SUB_MODULE_CONTRA);
				journalThread.start();
			}
			map.put("Success", "Success");
			return map;
		} catch (Exception e) {
			map.put("Failure", e.getMessage());
			logger.info("exception inside Accouning Entry service: " + map.toString());
			return map;
		}
	}
	
	public List<ContraExportVo> getListContraEntriesById(ExportVo exportVo) throws ApplicationException {
		logger.info("Entry into getListContraEntriesById");
		List<ContraExportVo> contraList = contraDao.getListContraEntriesById(exportVo);
		return contraList;
	}

}

package com.blackstrawai.accounting;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.accounting.dropdowns.LedgerDropDownVo;
import com.blackstrawai.accounting.dropdowns.LedgerFilterDropDownVo;
import com.blackstrawai.accounting.ledger.LedgerListVo;
import com.blackstrawai.accounting.ledger.LedgerVo;
import com.blackstrawai.attachments.AttachmentService;
import com.blackstrawai.attachments.AttachmentsConstants;
import com.blackstrawai.chartofaccounts.ChartOfAccountsDao;
import com.blackstrawai.chartofaccounts.ChartOfAccountsLedgerDao;
import com.blackstrawai.common.BaseService;
import com.blackstrawai.common.DropDownDao;
import com.blackstrawai.settings.chartofaccounts.ChartOfAccountsReportVo;
import com.blackstrawai.settings.chartofaccounts.MinimalChartOfAccountsLevel6Vo;


@Service
public class ChartOfAccountsLedgerService extends BaseService{

	@Autowired
	private DropDownDao dropDownDao;
	
	@Autowired
	private ChartOfAccountsLedgerDao chartOfAccountsLedgerDao;
	
	@Autowired
	private AttachmentService attachmentService;
	
	@Autowired
	private ChartOfAccountsDao chartOfAccountsDao;
	
    private Logger logger = Logger.getLogger(ChartOfAccountsLedgerService.class);
    
    
	public LedgerDropDownVo getLedgerDropDownData(int organizationId)throws ApplicationException {	
		logger.info("Entry into method:getLedgerDropDownData");
		return dropDownDao.getLedgerDropDownData(organizationId);
	}

	public void createLedgerAndSubLedger(LedgerVo ledgerVo) throws ApplicationException {
		logger.info("Entry to method:createLedgerAndSubLedger");
		try {
		ledgerVo = chartOfAccountsLedgerDao.createLedgerAndSubLedger(ledgerVo);
		if(ledgerVo.getId()!=null && !ledgerVo.getAttachments().isEmpty() && ledgerVo.getId()!=null) {
			logger.info("Entry into upload");
				attachmentService.upload(AttachmentsConstants.MODULE_TYPE_CHART_OF_ACCOUNTS, ledgerVo.getId(), ledgerVo.getOrganizationId(), ledgerVo.getAttachments());
				logger.info("Upload Successfull");
		}
		if(ledgerVo.getIsSubledgerMandatory()) {
			LedgerThread ledgerThread = new LedgerThread(chartOfAccountsLedgerDao, ledgerVo);
			ledgerThread.start();
		}
		
		logger.info("Ledger created Successfully in service layer ");
	}catch (Exception e) {
		logger.info("Error in Ledger create in service layer ");
		//chartOfAccountsLedgerDao.deleteFromLedgerTable(ledgerVo.getId());
		logger.info("befpre throw :: " + e.getMessage());
		throw new ApplicationException(e.getMessage());
	}
	}
	
	public LedgerVo getLedgerById(Integer ledgerId,Integer orgId) throws ApplicationException {
		logger.info("Entry into getLedgerById");
		LedgerVo ledgerVo = chartOfAccountsLedgerDao.getLedgerById(ledgerId,orgId);
				
		if(ledgerVo!=null && ledgerVo.getAttachments().size()>0 && ledgerVo.getId()!=null) {
			attachmentService.encodeAllFiles(ledgerVo.getOrganizationId(), ledgerVo.getId(),AttachmentsConstants.MODULE_TYPE_CHART_OF_ACCOUNTS, ledgerVo.getAttachments());
		}
		logger.info("getLedgerById executed Successfully in service Layer " +ledgerVo);
		return ledgerVo;
	}
	
	
	public void updateLedger(LedgerVo ledgerVo) throws ApplicationException {
		logger.info("Entry into updateLedger");
		try {
			Map<String , Integer> txnMap = chartOfAccountsLedgerDao.updateLedger(ledgerVo);
			 if(txnMap!=null && txnMap.containsKey("UpdateTxnSuccess") && txnMap.get("UpdateTxnSuccess")==1 && !ledgerVo.getAttachments().isEmpty() && ledgerVo.getId()!=null) {
					logger.info("Entry into upload");
					attachmentService.upload(AttachmentsConstants.MODULE_TYPE_CHART_OF_ACCOUNTS, ledgerVo.getId(), ledgerVo.getOrganizationId(), ledgerVo.getAttachments());
					logger.info("Upload Successfull");
				}
			 
			 if(txnMap!=null && txnMap.containsKey("UpdateTxnSuccess") && txnMap.get("UpdateTxnSuccess")==1) {
				 Integer currentMandatorySubledger = txnMap.containsKey("currentMandatorySubledger") ? txnMap.get("currentMandatorySubledger") : null;
				 Integer existingMandatorySubledger = txnMap.containsKey("existingMandatorySubledger") ? txnMap.get("existingMandatorySubledger") : null;
				 Integer existingEntityId = txnMap.containsKey("existingEntityId") ? txnMap.get("existingEntityId") : null;
				 if(currentMandatorySubledger!=null && existingMandatorySubledger!=null && existingEntityId!=null) {
					 if(currentMandatorySubledger.equals(existingMandatorySubledger)) {
						 logger.info("No change in the mandatory subledger ");
						 if(currentMandatorySubledger.intValue() == 1) {
							 if(existingEntityId.equals(ledgerVo.getEntityId())) {
								 logger.info("Existing entity and current entity are same so no change ");
							 }else {
								 logger.info("Existing entity and current entity are different ");
								 //To delete the Old values and add the new entity values
								 chartOfAccountsLedgerDao.deleteSubledgerForLedgerId(ledgerVo.getId());
								 LedgerThread ledgerThread = new LedgerThread(chartOfAccountsLedgerDao, ledgerVo);
								 ledgerThread.start();
							 }
						 }else {
							 logger.info("When mandatory subledger is false entity will always be false");
						 }
					 }else {
						 logger.info("There is  change in the mandatory subledger ");
						 if(currentMandatorySubledger.intValue() == 1) {
							 logger.info("The mandatory subledger is true and new entity to be added");
							 LedgerThread ledgerThread = new LedgerThread(chartOfAccountsLedgerDao, ledgerVo);
							 ledgerThread.start();
						 }else {
							 logger.info("The mandatory subledger is false and old entity to be removed");
							 chartOfAccountsLedgerDao.deleteSubledgerForLedgerId(ledgerVo.getId());
						 }
					 }
				 }
			 }
		} catch (ApplicationException e) {
			logger.info("Error in Ledger update in service layer ");
			throw new ApplicationException(e.getMessage());
		}
	}
	
	
	public List<LedgerListVo> getLedgersByOrdgId(Integer orgId ,String filterValue,String userId, String roleName) throws ApplicationException{
		logger.info("To getLedgersByOrdgId in service layer ");
		List<LedgerListVo>  list = chartOfAccountsLedgerDao.getLedgerAndSubLedgerList(orgId, filterValue,userId,roleName);
		list.sort(Comparator.comparing(LedgerListVo::getLedgerName));
		logger.info("Completed getLedgersByOrdgId in service layer " + list.size());
		return list;
	}
	
	public List<LedgerFilterDropDownVo> getFilterDropDown(Integer orgId) throws ApplicationException{
		return chartOfAccountsLedgerDao.getAccountTypeForFilterDropdown(orgId);
	}
	
	public List<MinimalChartOfAccountsLevel6Vo> getLedgerByType(int orgId,String type,int id) throws ApplicationException {
		return chartOfAccountsDao.getLedgerByType(orgId,type,id);
	}
}
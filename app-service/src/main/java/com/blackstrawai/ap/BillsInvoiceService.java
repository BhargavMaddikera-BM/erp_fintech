package com.blackstrawai.ap;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.ap.billsinvoice.BillsInvoiceDashboardVo;
import com.blackstrawai.ap.billsinvoice.InvoiceFilterVo;
import com.blackstrawai.ap.billsinvoice.InvoiceListVo;
import com.blackstrawai.ap.billsinvoice.InvoiceProductVo;
import com.blackstrawai.ap.billsinvoice.InvoiceTaxDetailsVo;
import com.blackstrawai.ap.billsinvoice.InvoiceTaxDistributionVo;
import com.blackstrawai.ap.billsinvoice.InvoiceVo;
import com.blackstrawai.ap.billsinvoice.QuickInvoiceVo;
import com.blackstrawai.ap.billsinvoice.TaxComputationVo;
import com.blackstrawai.ap.dropdowns.InvoiceDropDownVo;
import com.blackstrawai.attachments.AttachmentService;
import com.blackstrawai.attachments.AttachmentsConstants;
import com.blackstrawai.common.BaseService;
import com.blackstrawai.common.CommonConstants;
import com.blackstrawai.common.DropDownDao;
import com.blackstrawai.journals.JournalEntriesConstants;
import com.blackstrawai.journals.JournalEntriesThread;
import com.blackstrawai.journals.JournalEntriesTransactionDao;
import com.blackstrawai.settings.SettingsModuleOrganizationDao;
import com.blackstrawai.settings.SettingsModuleOrganizationVo;
import com.blackstrawai.settings.TaxGroupDao;
import com.blackstrawai.settings.TaxGroupVo;
import com.blackstrawai.workflow.WorkflowConstants;
import com.blackstrawai.workflow.WorkflowInvoiceVo;
import com.blackstrawai.workflow.WorkflowProcessHelperService;
import com.blackstrawai.workflow.WorkflowProcessService;
import com.blackstrawai.workflow.WorkflowSettingsVo;
import com.blackstrawai.workflow.WorkflowThread;

@Service
public class BillsInvoiceService extends BaseService{
	private Logger logger = Logger.getLogger(BillsInvoiceService.class);

	@Autowired
	private BillsInvoiceDao billsInvoiceDao;
	
	@Autowired
	private AttachmentService attachmentService;
	
	@Autowired
	private DropDownDao dropDownDao;
	
	@Autowired
	private TaxGroupDao taxGroupDao;
	
	@Autowired
	private JournalEntriesTransactionDao journalEntriesTransactionDao;
	
	@Autowired
	private WorkflowProcessHelperService workflowProcessHelperService;
	
	@Autowired
	private WorkflowProcessService workflowProcessService;
	
	@Autowired
	private SettingsModuleOrganizationDao settingsModuleOrganizationDao;
	
	@Autowired
	ApBalanceUpdateDao balanceUpdateDao;

	
	public void createInvoice(InvoiceVo invoiceVo ) throws ApplicationException{	
		logger.info("Entry into createInvoice");
		try {
			invoiceVo = billsInvoiceDao.createInvoice(invoiceVo);
			if(invoiceVo.getId()!=null && !invoiceVo.getAttachments().isEmpty() && invoiceVo.getId()!=null) {
				logger.info("Entry into upload");
				String mouduleType = invoiceVo.getIsInvoiceWithBills() ? AttachmentsConstants.MODULE_TYPE_INVOICES_BILL : AttachmentsConstants.MODULE_TYPE_INVOICES_WITHOUT_BILL;
					attachmentService.upload(mouduleType, invoiceVo.getId(), invoiceVo.getOrganizationId(), invoiceVo.getAttachments());
					logger.info("Upload Successfull");
			
			// To add entry in journal entry table 
			}
			logger.info("Before thread");
			if(invoiceVo.getId()!=null  && (CommonConstants.STATUS_AS_ACTIVE.equals(invoiceVo.getStatus()) || CommonConstants.STATUS_AS_OPEN.equals(invoiceVo.getStatus()) )) {
				logger.info("To thread");
				String module = invoiceVo.getIsInvoiceWithBills() ? JournalEntriesConstants.SUB_MODULE_INVOICE_WITH_BILLS : JournalEntriesConstants.SUB_MODULE_INVOICE_WITHOUT_BILLS;
				JournalEntriesThread journalThread =new JournalEntriesThread(journalEntriesTransactionDao, invoiceVo.getId(), invoiceVo.getOrganizationId(),module);
				journalThread.start();
				logger.info("Helper:"+workflowProcessHelperService);
				new ApBalanceUpdateThread(balanceUpdateDao, invoiceVo.getId(), JournalEntriesConstants.SUB_MODULE_INVOICE_WITH_BILLS).start();
				startWorkflow(invoiceVo, WorkflowConstants.WORKFLOW_OPERATION_CREATE);
				}
				
			
			logger.info("Invoices created Successfully in service layer ");
		} catch (Exception e) {
			logger.info("Error in Invoice create in service layer ");
			try {
				billsInvoiceDao.deleteInvoiceEntries(invoiceVo.getId() , invoiceVo.getIsInvoiceWithBills(),invoiceVo.getUserId(),invoiceVo.getRoleName());
			} catch (SQLException e1) {
				logger.info("Error in billsinvoice entries delete in service layer ");
				throw new ApplicationException(e.getMessage());
			}
			logger.info("befpre throew :: " + e.getMessage());
			throw new ApplicationException(e.getMessage());
		}
	}

	public InvoiceDropDownVo getInvoiceDropDownData(int organizationId)throws ApplicationException {	
		logger.info("Entry into method: getInvoiceDropDownData");
		return dropDownDao.getInvoiceDropDownData(organizationId);
	}
	
	/**
	 * Gets the all filtered invoices for org.
	 *
	 * @param filterVo the filter vo
	 * @return the all filtered invoices for org
	 * @throws ApplicationException the application exception
	 */
	public List<InvoiceListVo> getAllFilteredInvoicesForOrg(InvoiceFilterVo filterVo) throws ApplicationException{
		logger.info("Entry into getAllCustomerListForOrg");
		List<InvoiceListVo> filteredList = billsInvoiceDao.getInvoiceFilteredList(filterVo);
		filteredList.sort(Collections.reverseOrder(Comparator.comparing(InvoiceListVo::getInvoiceId)));
		return filteredList;
	}
	
	public List<InvoiceListVo> getAllInvoicesForOrg(Integer orgId , Boolean isInvoiceWithBills) throws ApplicationException{
		logger.info("Entry into getAllCustomerListForOrg");
		return billsInvoiceDao.getAllInvoicesWithBillsForOrg(orgId, isInvoiceWithBills);
	}
	
	public TaxComputationVo computeTaxCalculation(List<InvoiceProductVo> productList,Integer orgId, Boolean isExclusive) throws ApplicationException {
		logger.info("Entry into method: computeTaxCalculation");
		TaxComputationVo computationVo = new TaxComputationVo();
		List<InvoiceProductVo> prodList = computeTaxForEachProduct(productList,orgId);
		computationVo.setGroupedTax(calculateTotalTax(prodList));
		prodList = calculteIncluseExclusiveTotalAmount(productList,orgId, isExclusive);
		computationVo.setProducts(prodList);
		return computationVo;
	}
	
	public InvoiceVo getInvoiceById(Integer invoiceId) throws ApplicationException {
		logger.info("Entry into getInvoiceById");
		 InvoiceVo invoiceVo = billsInvoiceDao.getInvoiceById(invoiceId);
				
		if(invoiceVo!=null && invoiceVo.getAttachments().size()>0 && invoiceVo.getId()!=null) {
			String moduleType = invoiceVo.getIsInvoiceWithBills() ? AttachmentsConstants.MODULE_TYPE_INVOICES_BILL : AttachmentsConstants.MODULE_TYPE_INVOICES_WITHOUT_BILL;
			attachmentService.encodeAllFiles(invoiceVo.getOrganizationId(), invoiceVo.getId(),moduleType, invoiceVo.getAttachments());
		}
		if(invoiceVo.getTransactionDetails()!=null && invoiceVo.getTransactionDetails().getProducts()!=null){
			invoiceVo.getTransactionDetails().setGroupedTax(calculateTotalTax(invoiceVo.getTransactionDetails().getProducts()));
		}
		logger.info("getInvoiceById executed Successfully in service Layer " +invoiceVo);
		return invoiceVo;
	}
	
	public void updateInvoice(InvoiceVo invoiceVo ) throws ApplicationException {
		logger.info("Entry into updateInvoice");
		if(invoiceVo.getId() != null) {
			 try {
				 WorkflowInvoiceVo workflowInvoiceVo=billsInvoiceDao.getWorkflowRequiredDataForInvoiceById(invoiceVo.getId());
				 //TODO: May need to remove after balance updation implemented
				 if(workflowInvoiceVo!=null && workflowInvoiceVo.getStatus()!=null && workflowInvoiceVo.getStatus().equalsIgnoreCase(CommonConstants.STATUS_AS_OPEN) && invoiceVo!=null && invoiceVo.getStatus()!=null  && invoiceVo.getStatus().equalsIgnoreCase(CommonConstants.STATUS_AS_ACTIVE)) {
					 invoiceVo.setStatus(CommonConstants.STATUS_AS_OPEN);
				 }
				 
				 
				 invoiceVo = 	 billsInvoiceDao.updateInvoice(invoiceVo);
				 if(invoiceVo.getId()!=null && !invoiceVo.getAttachments().isEmpty() && invoiceVo.getId()!=null) {
						logger.info("Entry into upload");
						String mouduleType = invoiceVo.getIsInvoiceWithBills() ? AttachmentsConstants.MODULE_TYPE_INVOICES_BILL : AttachmentsConstants.MODULE_TYPE_INVOICES_WITHOUT_BILL;
						attachmentService.upload(mouduleType, invoiceVo.getId(), invoiceVo.getOrganizationId(), invoiceVo.getAttachments());
						logger.info("Upload Successfull");
						
				 }
				// To add entry in journal entry table 
				 if(invoiceVo.getId()!=null && (CommonConstants.STATUS_AS_ACTIVE.equals(invoiceVo.getStatus()) || CommonConstants.STATUS_AS_OPEN.equals(invoiceVo.getStatus())) ) {
						logger.info("To thread");
						String module = invoiceVo.getIsInvoiceWithBills() ? JournalEntriesConstants.SUB_MODULE_INVOICE_WITH_BILLS : JournalEntriesConstants.SUB_MODULE_INVOICE_WITHOUT_BILLS;
						JournalEntriesThread journalThread =new JournalEntriesThread(journalEntriesTransactionDao, invoiceVo.getId(), invoiceVo.getOrganizationId(),module);
						journalThread.start();
					}
				 logger.info("Invoice>>>>:"+invoiceVo);
				//Update Balance
					if(!invoiceVo.getStatus().equalsIgnoreCase(CommonConstants.STATUS_AS_DRAFT) && !invoiceVo.getStatus().equalsIgnoreCase(CommonConstants.STATUS_AS_VOID)) {
						new ApBalanceUpdateThread(balanceUpdateDao, invoiceVo.getId(), JournalEntriesConstants.SUB_MODULE_INVOICE_WITH_BILLS).start();
					}
				//Trigger Workflow
				 if (invoiceVo.getId() != null && invoiceVo.getStatus() != null
						&& !(invoiceVo.getStatus().equalsIgnoreCase(CommonConstants.STATUS_AS_VOID))) {

					 if(invoiceVo.getStatus().equalsIgnoreCase(CommonConstants.STATUS_AS_DRAFT)) {
						 workflowInvoiceVo=billsInvoiceDao.getWorkflowRequiredDataForInvoiceById(invoiceVo.getId());
						//If it is not regular draft
						 if(workflowInvoiceVo!=null && workflowInvoiceVo.getPendingApprovalStatus()!=null && (workflowInvoiceVo.getPendingApprovalStatus().equalsIgnoreCase(CommonConstants.DISPLAY_STATUS_AS_PENDING_FOR_APPROVAL) || workflowInvoiceVo.getPendingApprovalStatus().equalsIgnoreCase(CommonConstants.DISPLAY_STATUS_AS_APPROVAL_DENIED))) {
							//Update Balance : Will consider balance update,if it is not regular Draft
								new ApBalanceUpdateThread(balanceUpdateDao, invoiceVo.getId(), JournalEntriesConstants.SUB_MODULE_INVOICE_WITH_BILLS).start();
							 startWorkflow(invoiceVo, WorkflowConstants.WORKFLOW_OPERATION_UDATE);					 
						 }
					 } else {
						 startWorkflow(invoiceVo, WorkflowConstants.WORKFLOW_OPERATION_UDATE);
					 }
					
					}
			} catch (SQLException e) {
				logger.info("error in updateInvoice in service Layer ",e);
				throw new ApplicationException(e.getMessage());
			}
		}
	}

	

	private void startWorkflow(InvoiceVo invoiceVo,String workflowOperation) throws ApplicationException {
		logger.info("Entry To startWorkflow");
		try {

			// Get Settings for workflow
			SettingsModuleOrganizationVo settingsModuleVo = settingsModuleOrganizationDao
					.getSettingsModuleOrganizationBySubmodule(invoiceVo.getOrganizationId(),
							WorkflowConstants.MODULE_AP_INVOICE);
			if (settingsModuleVo != null && settingsModuleVo.isRequired()) {
				if(workflowOperation!=null && workflowOperation.equalsIgnoreCase(WorkflowConstants.WORKFLOW_OPERATION_UDATE)) {
					workflowProcessService.deletePreviousRules(WorkflowConstants.MODULE_AP_INVOICE,invoiceVo.getId(), invoiceVo.getUserId(), invoiceVo.getRoleName());
				}

				// Get All Applicable Rules
				List<WorkflowSettingsVo> applicableRules = workflowProcessHelperService
						.getApplicableRulesForModuleEntity(invoiceVo.getOrganizationId(), invoiceVo.getId(),
								WorkflowConstants.MODULE_AP_INVOICE);
				if (applicableRules != null && !applicableRules.isEmpty()) {

					// Triggering Workflow
					new WorkflowThread(WorkflowConstants.MODULE_AP_INVOICE, invoiceVo.getId(),
							workflowProcessService, applicableRules,workflowOperation)
					.start();
					Thread.sleep(1000);
				}
			}


		}catch(Exception e) {
			logger.error("Error in startWorkflow:",e);	
		}
	}
	
	private List<InvoiceProductVo> computeTaxForEachProduct(List<InvoiceProductVo> productList,Integer orgId) throws ApplicationException {
		logger.info("To calculate computeTaxForEachProduct");
		if(productList.size()>0 ) {
		List<TaxGroupVo> taxGroupList = taxGroupDao.getTaxGroupForOrganization(orgId);
		Map<String , String> taxRateMap = taxGroupDao.getTaxRatesMappingForOrganization(orgId);
		logger.info("TaxGroupListSize"+taxGroupList);
		Map<Integer , TaxGroupVo> taxGroupMap = new HashMap<Integer, TaxGroupVo>();
		if(taxGroupList!=null && taxGroupList.size()>0) {
		 taxGroupMap = taxGroupList.stream().collect(Collectors.toMap(TaxGroupVo::getId, vo -> vo));
		}
		logger.info("taxGroupMapSize"+taxGroupMap);
		if(taxGroupMap.size()>0) {
			for(InvoiceProductVo product : productList) {
				logger.info("taxGroupMapSize :::: "+taxGroupMap.size());
				if(product.getTaxRateId() !=null && product.getTaxRateId()!= 0 && !CommonConstants.STATUS_AS_DELETE.equals(product.getStatus())) {
				TaxGroupVo taxDetails = taxGroupMap.containsKey(product.getTaxRateId()) ?taxGroupMap.get(product.getTaxRateId()):null;
				logger.info("TaxGroupVo"+taxDetails);
				InvoiceTaxDetailsVo taxDetailVo = new InvoiceTaxDetailsVo();
				if(taxDetails!=null) {
					String[] taxIncuded = taxDetails.getTaxesIncluded().split(",");
					taxDetailVo.setGroupName(taxDetails.getName());
					List<InvoiceTaxDistributionVo> distributionVos = new ArrayList<InvoiceTaxDistributionVo>();
					for(int i=0; i<taxIncuded.length;i++) {
						logger.info("taxIncuded"+taxIncuded[i]);						
						String taxName = taxIncuded[i].trim();
						logger.info("taxName is ::"+taxName);
						//String name = taxName.split(" ")[0];
						//logger.info("taxName is ::"+name);
						String taxRate =  taxRateMap.containsKey(taxName) ?  taxRateMap.get(taxName) : null;
						logger.info("taxRate is ::"+taxRate);
						Double taxAmount =taxRate !=null ? (product.getAmount()*(Double.valueOf(taxRate)))/100 : 0;
						logger.info("taxAmount is ::"+taxAmount);
						InvoiceTaxDistributionVo distributionVo = new InvoiceTaxDistributionVo();
						distributionVo.setTaxName(taxName);
						distributionVo.setTaxRate(taxRate);
						distributionVo.setTaxAmount(Math.round(taxAmount * 100.0) / 100.0 );
						distributionVos.add(distributionVo);
						taxDetailVo.setTaxDistribution(distributionVos);
					}
					logger.info("taxDetailVo"+taxDetailVo);
				}
				product.setTaxDetails(taxDetailVo);
			}
			}
		}
		}
		logger.info("Completed calculate computeTaxForEachProduct");
		return productList;
	}
	
	private List<InvoiceTaxDistributionVo> calculateTotalTax(List<InvoiceProductVo> productList){
		logger.info("To method calculateTotalTax::"+productList.size());
		Map<String,Double> taxDetailsMap = new HashMap<String, Double>();
		if(productList.size()>0){
			productList.forEach(product ->{
				logger.info("CommonConstants.STATUS_AS_DELETE.equals(product.getStatus())" + CommonConstants.STATUS_AS_DELETE.equals(product.getStatus()));
				logger.info("To method product" + product.getTaxDetails());
				if(product.getTaxDetails()!=null && !(CommonConstants.STATUS_AS_DELETE.equals(product.getStatus()))) {
				product.getTaxDetails().getTaxDistribution().forEach(taxDistribution -> {
					logger.info("To method product tax size" + product.getTaxDetails().getTaxDistribution().size());
					String taxName = taxDistribution.getTaxName() +"~"+ taxDistribution.getTaxRate();
					if(taxName!=null && taxDetailsMap.containsKey(taxName)) {
						Double amount = taxDetailsMap.get(taxName);
						amount = amount +  taxDistribution.getTaxAmount();
						logger.info("amount"+amount +"for key "+taxName);
						taxDetailsMap.put(taxName , Math.round(amount * 100.0) / 100.0 );
					}else {
						logger.info("Adding new entry in map for key"+taxName);
						taxDetailsMap.put(taxName ,Math.round(taxDistribution.getTaxAmount() * 100.0) / 100.0  );
					}
				});
				}
			});
			
		}
		List<InvoiceTaxDistributionVo> taxDistributionList = new ArrayList<InvoiceTaxDistributionVo>();
		if(taxDetailsMap.size()>0) {
		taxDetailsMap.forEach((k,v)->{
			InvoiceTaxDistributionVo distribution = new InvoiceTaxDistributionVo();
			distribution.setTaxName(k.split("~")[0]);
			distribution.setTaxRate(k.split("~")[1]);
			distribution.setTaxAmount(v);
			taxDistributionList.add(distribution);
		});
		}
		logger.info("Completed method calculateTotalTax with taxDistributionList size :: " +taxDistributionList.size());
		return taxDistributionList;
		
	}
	
	private List<InvoiceProductVo> calculteIncluseExclusiveTotalAmount(List<InvoiceProductVo> productVos , Integer orgId, Boolean isExclusive) throws ApplicationException{
		logger.info("To calculteIncluseExclusiveTotalAmount ");
			if(isExclusive) {
				productVos.forEach(product -> {
					if(product.getQuantity()!=null ) {
					Double totalAmount = calculteExclusiveAmount(product.getUnitPrice() , Double.valueOf(product.getQuantity()));
					product.setAmount(totalAmount);
					}
					});
			}else {
				List<TaxGroupVo> taxGroupList = taxGroupDao.getTaxGroupForOrganization(orgId);
				logger.info("TaxGroupListSize"+taxGroupList.size());
				Map<Integer , TaxGroupVo> taxGroupMap = new HashMap<Integer, TaxGroupVo>();
				if(taxGroupList!=null && taxGroupList.size()>0) {
				 taxGroupMap = taxGroupList.stream().collect(Collectors.toMap(TaxGroupVo::getId, vo -> vo));
				}
				logger.info("taxGroupMapSize"+taxGroupMap);
				if(taxGroupMap.size()>0) {
				for(InvoiceProductVo product : productVos) {
					if(product.getTaxRateId() !=null && product.getTaxRateId()!= 0 && !CommonConstants.STATUS_AS_DELETE.equals(product.getStatus())) {
					TaxGroupVo taxDetails = taxGroupMap.containsKey(product.getTaxRateId()) ?taxGroupMap.get(product.getTaxRateId()):null;
					logger.info("TaxGroupVo"+taxDetails);
						if(taxDetails.getCombinedRate()!=null && product.getQuantity()!=null ) {
							Double totalAmount = calculteInclusiveAmount(Double.valueOf(taxDetails.getCombinedRate()), product.getUnitPrice(), Double.valueOf(product.getQuantity()));
							product.setAmount(totalAmount);
						}
					}
					}			
			}
			}
	
		return productVos;
		
	}
	
	private Double calculteInclusiveAmount(Double taxrate , Double unitPrice , Double quantity) {
		Double inclusiveTotAmnt = ((quantity * unitPrice )/(100+taxrate))*100;
		inclusiveTotAmnt = Math.round(inclusiveTotAmnt * 100.0) / 100.0  ;
		logger.info("Inclusive Amount is :: "+ inclusiveTotAmnt);
		return inclusiveTotAmnt;
	}
	
	private Double calculteExclusiveAmount( Double unitPrice , Double quantity) {
		Double exclusiveTotAmnt = (quantity * unitPrice );
		exclusiveTotAmnt = Math.round(exclusiveTotAmnt * 100.0) / 100.0  ;
		logger.info("Exclusive Amount is :: "+ exclusiveTotAmnt);
		return exclusiveTotAmnt;
	}
	
	
	public void activateOrDeActivateInvoice(int invoiceId , String status, String userId, String roleName) throws ApplicationException{
		logger.info("Entry into activateOrDeActivateInvoice");
		billsInvoiceDao.activateOrDeActivateInvoice(invoiceId, status,userId,roleName);
		
	}

	public Map<String, Integer> getMaxAmountForOrg(Integer organizationId ,Boolean isInvoiceWIthBills) throws ApplicationException {
		logger.info("Entry into getMaxAmountForOrg");
		return billsInvoiceDao.getMaxAmountForOrg(organizationId , isInvoiceWIthBills);
	}

	public void createQuickInvoice(QuickInvoiceVo invoiceVo) throws ApplicationException {
		logger.info("Entry into createInvoice");
		try {
			invoiceVo = billsInvoiceDao.createQuickInvoice(invoiceVo);
			if(invoiceVo.getId()!=null && !invoiceVo.getAttachments().isEmpty() && invoiceVo.getId()!=null) {
				logger.info("Entry into upload");
				String mouduleType = AttachmentsConstants.MODULE_TYPE_INVOICES_BILL;
					attachmentService.upload(mouduleType, invoiceVo.getId(), invoiceVo.getOrganizationId(), invoiceVo.getAttachments());
					logger.info("Upload Successfull");
			}
			logger.info("Invoices created Successfully in service layer ");
	} catch (Exception e) {
		logger.info("error in createdInvoice in service Layer ",e);
		throw new ApplicationException(e.getMessage());
	}
	}

	public QuickInvoiceVo getQuickInvoiceById(Integer invoiceId) throws ApplicationException {
		logger.info("Entry into getQuickInvoiceById");
		 QuickInvoiceVo invoiceVo = billsInvoiceDao.getQuickInvoiceById(invoiceId);
				
		if(invoiceVo!=null &&invoiceVo.getAttachments()!=null &&  invoiceVo.getAttachments().size()>0 && invoiceVo.getId()!=null) {
			String moduleType =  AttachmentsConstants.MODULE_TYPE_INVOICES_BILL ;
			attachmentService.encodeAllFiles(invoiceVo.getOrganizationId(), invoiceVo.getId(),moduleType, invoiceVo.getAttachments());
		}
		logger.info("getInvoiceById executed Successfully in service Layer " +invoiceVo);
		return invoiceVo;
	}

	public void updateQuickInvoice(QuickInvoiceVo invoiceVo) throws ApplicationException {
		logger.info("Entry into updateQuickInvoice");
		if(invoiceVo.getId() != null) {
			 try {
				 invoiceVo = 	 billsInvoiceDao.updateQuickInvoice(invoiceVo);
				 if(invoiceVo.getId()!=null &&  invoiceVo.getAttachments()!=null &&  !invoiceVo.getAttachments().isEmpty() && invoiceVo.getId()!=null) {
						logger.info("Entry into upload");
						String mouduleType =  AttachmentsConstants.MODULE_TYPE_INVOICES_BILL ;
						attachmentService.upload(mouduleType, invoiceVo.getId(), invoiceVo.getOrganizationId(), invoiceVo.getAttachments());
						logger.info("Upload Successfull");
						
				 }
				 
				 
			} catch (Exception e) {
				logger.info("error in updateInvoice in service Layer ",e);
				throw new ApplicationException(e.getMessage());
			}
		}
	}
	
	public List<BillsInvoiceDashboardVo> getRecentInvoicesOfAnOrganizationForUserAndRole(int organizationId,
			String userId, String roleName) throws ApplicationException {
		return billsInvoiceDao.getRecentInvoicesOfAnOrganizationForUserAndRole(organizationId, userId, roleName);
	}
	public List<InvoiceListVo> getDueInvoicesOfAnOrganizationForUserAndRole(int organizationId,
			String userId, String roleName) throws ApplicationException {
		return billsInvoiceDao.getDueInvoicesList(organizationId, userId, roleName);
	}
	
	public Map<String, Boolean> checkInvoiceNoExistForOrganization(Integer organizationId ,Integer vendorId,String invoiceNo) throws ApplicationException {
		logger.info("Entry into checkInvoiceNoExistForOrganizationService");
		Map<String, Boolean> isInvoiceIdExist=null;
		Boolean flag=false;
		try
		{
			isInvoiceIdExist = new HashMap<String, Boolean>();
			flag=billsInvoiceDao.checkInvoiceNoExist(organizationId,vendorId,invoiceNo);
			logger.info("The value of isInvoiceIdExist"+isInvoiceIdExist);
		    isInvoiceIdExist.put("invoiceflag",flag);
		}
		catch(Exception e)
		{
			logger.info("error in checkInvoiceNoExistForOrganization ",e);
			throw new ApplicationException(e.getMessage());

		}
		return isInvoiceIdExist;
	}

}

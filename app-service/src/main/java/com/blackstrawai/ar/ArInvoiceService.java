package com.blackstrawai.ar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.ar.dropdowns.ArInvoicesDropDownVo;
import com.blackstrawai.ar.dropdowns.BasicCustomerDetailsVo;
import com.blackstrawai.ar.invoice.ArInvoiceFilterVo;
import com.blackstrawai.ar.invoice.ArInvoiceListVo;
import com.blackstrawai.ar.invoice.ArInvoiceProductVo;
import com.blackstrawai.ar.invoice.ArInvoiceTaxDetailsVo;
import com.blackstrawai.ar.invoice.ArInvoiceTaxDistributionVo;
import com.blackstrawai.ar.invoice.ArInvoiceVo;
import com.blackstrawai.ar.invoice.ArTaxComputationVo;
import com.blackstrawai.attachments.AttachmentService;
import com.blackstrawai.attachments.AttachmentsConstants;
import com.blackstrawai.chartofaccounts.ChartOfAccountsDao;
import com.blackstrawai.common.BaseService;
import com.blackstrawai.common.CommonConstants;
import com.blackstrawai.common.DropDownDao;
import com.blackstrawai.common.FinanceCommonDao;
import com.blackstrawai.journals.JournalEntriesConstants;
import com.blackstrawai.journals.JournalEntriesThread;
import com.blackstrawai.journals.JournalEntriesTransactionDao;
import com.blackstrawai.keycontact.CustomerDao;
import com.blackstrawai.keycontact.customer.CustomerVo;
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
public class ArInvoiceService extends BaseService{

	private Logger logger = Logger.getLogger(ArInvoiceService.class);

	@Autowired
	private ArInvoiceDao invoiceDao;
	
	@Autowired
	private DropDownDao dropDownDao;
	
	@Autowired
	private CustomerDao customerDao;
	
	@Autowired
	private ChartOfAccountsDao chartOfAccountsDao;
	
	@Autowired
	private TaxGroupDao taxGroupDao;
	
	@Autowired
	private FinanceCommonDao financeCommonDao;
	
	@Autowired
	private AttachmentService attachmentService;
	
	@Autowired
	ArBalanceUpdateDao balanceUpdateDao;
	
	@Autowired
	private JournalEntriesTransactionDao journalEntriesTransactionDao;
	
	@Autowired
	private WorkflowProcessHelperService workflowProcessHelperService;
	
	@Autowired
	private WorkflowProcessService workflowProcessService;
	
	@Autowired
	private SettingsModuleOrganizationDao settingsModuleOrganizationDao;

	
	public ArInvoicesDropDownVo getInvoiceDropDownData(int organizationId)throws ApplicationException {	
		logger.info("Entry into method: getInvoiceDropDownData");
		return dropDownDao.getArInvoiceDropDownData(organizationId);
	}


	public BasicCustomerDetailsVo getCustomerById(Integer custId,Integer orgId) throws ApplicationException {
		logger.info("Entry into method: getCustomerById");
		CustomerVo customer = customerDao.getCustomerById(custId);
		BasicCustomerDetailsVo detailsVo = new BasicCustomerDetailsVo();
		detailsVo.setId(customer.getId());
		detailsVo.setCustomerLedgers(chartOfAccountsDao.getLedgerBySubLedegerDescription(orgId, custId, "Customer"));
		detailsVo.setBillingAddress(customer.getBillingAddress());
		detailsVo.setDeliveryAddress(customer.getDeliveryAddress());
		detailsVo.setPrimaryInfo(customer.getPrimaryInfo());
		return detailsVo;
	}


	public ArTaxComputationVo computeTaxCalculation(List<ArInvoiceProductVo> productVoList, Integer organizationId,Boolean isLocalType,Boolean isTaxExclusive) throws ApplicationException {
		logger.info("Entry into method: computeTaxCalculation");
		ArTaxComputationVo computationVo = new ArTaxComputationVo();
	//	List<ARInvoiceProductVo> prodList = computeTaxForEachProduct(productVoList,organizationId);
		//computationVo.setGroupedTax(calculateTotalTax(prodList));
	//	prodList = calculteIncluseExclusiveTotalAmount(productVoList,organizationId, isTaxExclusive);
		List<ArInvoiceProductVo> prodList = calculteIncluseExclusiveTotalAmount(productVoList,organizationId, isTaxExclusive);
		prodList = computeTaxForEachProduct(productVoList,organizationId);		
		computationVo.setGroupedTax(calculateTotalTax(prodList));	
		computationVo.setProducts(prodList);
		return computationVo;
	}
	

	private List<ArInvoiceProductVo> computeTaxForEachProduct(List<ArInvoiceProductVo> productList,Integer orgId) throws ApplicationException {
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
			for(ArInvoiceProductVo product : productList) {
				logger.info("taxGroupMapSize :::: "+taxGroupMap.size());
				if(product.getDiscount()!=null && product.getDiscountType()!=null && product.getDiscountType()!= 0  ) {
					calculateDiscount(product);
					if(product.getDiscountAmount()!=null) {
					product.setNetAmount(product.getAmount() - product.getDiscountAmount());
				}
				}else {
					logger.info("Discount Set To 0 ");
					product.setDiscountAmount(0.00);
					product.setNetAmount(product.getAmount() - product.getDiscountAmount());
				}
				if(product.getTaxRateId() !=null && product.getTaxRateId()!= 0 && !CommonConstants.STATUS_AS_DELETE.equals(product.getStatus())) {
				TaxGroupVo taxDetails = taxGroupMap.containsKey(product.getTaxRateId()) ?taxGroupMap.get(product.getTaxRateId()):null;
				logger.info("TaxGroupVo"+taxDetails);
				ArInvoiceTaxDetailsVo taxDetailVo = new ArInvoiceTaxDetailsVo();
				if(taxDetails!=null) {
					String[] taxIncuded = taxDetails.getTaxesIncluded().split(",");
					taxDetailVo.setGroupName(taxDetails.getName());
					List<ArInvoiceTaxDistributionVo> distributionVos = new ArrayList<ArInvoiceTaxDistributionVo>();
					for(int i=0; i<taxIncuded.length;i++) {
						logger.info("taxIncuded"+taxIncuded[i]);						
						String taxName = taxIncuded[i].trim();
						logger.info("taxName is ::"+taxName);
						//String name = taxName.split(" ")[0];
						//logger.info("taxName is ::"+name);
						String taxRate =  taxRateMap.containsKey(taxName) ?  taxRateMap.get(taxName) : null;
						logger.info("taxRate is ::"+taxRate);
						Double taxAmount =taxRate !=null ? ((product.getAmount()-product.getDiscountAmount())*(Double.valueOf(taxRate)))/100 : 0;
						logger.info("taxAmount is ::"+taxAmount);
						ArInvoiceTaxDistributionVo distributionVo = new ArInvoiceTaxDistributionVo();
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
	
	private void calculateDiscount(ArInvoiceProductVo product) throws ApplicationException {
		 Map<Integer, String> discountMap = financeCommonDao.getDiscountType();
		 if(discountMap!=null) {
			 if(discountMap.containsKey(product.getDiscountType())) {
				 if("Percentage".equals(discountMap.get(product.getDiscountType()))) {
						logger.info("To calculate Discount Percent");
						Double discountAmnt = (product.getAmount()*product.getDiscount())/100;
						logger.info("Discount::"+discountAmnt);
						product.setDiscountAmount(Math.round(discountAmnt * 100.0) / 100.0);
				 }if("Flat Discount".equals(discountMap.get(product.getDiscountType()))) {
						logger.info("To calculate Discount Flat");
						product.setDiscountAmount(product.getDiscount());
						logger.info("Discount::"+(product.getDiscount()));
				 }
			 }
		 }
	}


	public List<ArInvoiceTaxDistributionVo> calculateTotalTax(List<ArInvoiceProductVo> productList){
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
		List<ArInvoiceTaxDistributionVo> taxDistributionList = new ArrayList<ArInvoiceTaxDistributionVo>();
		if(taxDetailsMap.size()>0) {
		taxDetailsMap.forEach((k,v)->{
			ArInvoiceTaxDistributionVo distribution = new ArInvoiceTaxDistributionVo();
			distribution.setTaxName(k.split("~")[0]);
			distribution.setTaxRate(k.split("~")[1]);
			distribution.setTaxAmount(v);
			taxDistributionList.add(distribution);
		});
		}
		logger.info("Completed method calculateTotalTax with taxDistributionList size :: " +taxDistributionList.size());
		return taxDistributionList;
		
	}
	
	private List<ArInvoiceProductVo> calculteIncluseExclusiveTotalAmount(List<ArInvoiceProductVo> productVos , Integer orgId, Boolean isExclusive) throws ApplicationException{
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
				for(ArInvoiceProductVo product : productVos) {
					if(product.getTaxRateId() !=null && product.getTaxRateId()!= 0 && !CommonConstants.STATUS_AS_DELETE.equals(product.getStatus())) {
					TaxGroupVo taxDetails = taxGroupMap.containsKey(product.getTaxRateId()) ?taxGroupMap.get(product.getTaxRateId()):null;
					logger.info("TaxGroupVo"+taxDetails);
						if(taxDetails.getCombinedRate()!=null && product.getQuantity()!=null ) {
							Double totalAmount = calculteInclusiveAmount(Double.valueOf(taxDetails.getCombinedRate()), product.getUnitPrice(), Integer.valueOf(product.getQuantity()));
							product.setAmount(totalAmount);
						}
					}
					}			
			}
			}
	
		return productVos;
		
	}
	
	private Double calculteInclusiveAmount(Double taxrate , Double unitPrice , Integer quantity) {
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


	public void createInvoice(ArInvoiceVo invoiceVo) throws ApplicationException {
		logger.info("Entry into createInvoice");
		try {
			invoiceVo = invoiceDao.createInvoice(invoiceVo);
			if(invoiceVo.getInvoiceId()!=null && !invoiceVo.getAttachments().isEmpty() && invoiceVo.getInvoiceId()!=null) {
				logger.info("Entry into upload");
					attachmentService.upload(AttachmentsConstants.MODULE_TYPE_ACCOUNTS_RECEIVABLES_INVOICE, invoiceVo.getInvoiceId(), invoiceVo.getOrgId(), invoiceVo.getAttachments());
					logger.info("Upload Successfull");
			}
			// To add Journal Entries 
			logger.info("Before thread");
			if(invoiceVo.getInvoiceId()!=null  && (CommonConstants.STATUS_AS_ACTIVE.equals(invoiceVo.getStatus()) || CommonConstants.STATUS_AS_OPEN.equals(invoiceVo.getStatus()) )) {
				logger.info("To thread");
				JournalEntriesThread journalThread =new JournalEntriesThread(journalEntriesTransactionDao, invoiceVo.getInvoiceId(), invoiceVo.getOrgId(),JournalEntriesConstants.SUB_MODULE_INVOICES);
				journalThread.start();
				
				new ArBalanceUpdateThread(balanceUpdateDao, invoiceVo.getInvoiceId(), ArInvoiceConstants.MODULE_TYPE_AR_INVOICES).start();
				startWorkflow(invoiceVo, WorkflowConstants.WORKFLOW_OPERATION_CREATE);

			}
		logger.info("Invoices created Successfully in service layer ");
	} catch (Exception e) {
		logger.info("Error in Invoice create in service layer ");
		throw new ApplicationException(e.getMessage());
	}
	}
	
	public List<ArInvoiceListVo> getAllFilteredInvoicesForOrg(ArInvoiceFilterVo filterVo) throws ApplicationException{
		logger.info("Entry into getAllFilteredInvoicesForOrg");
		return invoiceDao.getInvoiceFilteredList(filterVo);
	}


	public ArInvoiceVo getInvoiceById(Integer invoiceId) throws ApplicationException {
		logger.info("Entry into getInvoiceById");
		 ArInvoiceVo invoiceVo = invoiceDao.getInvoiceById(invoiceId);
		if(invoiceVo!=null && invoiceVo.getProducts()!=null){
			invoiceVo.setGroupedTax(calculateTotalTax(invoiceVo.getProducts()));
		}
		if(invoiceVo!=null && invoiceVo.getAttachments().size()>0 && invoiceVo.getInvoiceId()!=null) {
			attachmentService.encodeAllFiles(invoiceVo.getOrgId(), invoiceVo.getInvoiceId(),AttachmentsConstants.MODULE_TYPE_ACCOUNTS_RECEIVABLES_INVOICE, invoiceVo.getAttachments());
		}
		logger.info("getInvoiceById executed Successfully in service Layer " +invoiceVo);
		return invoiceVo;
	}


	public void activateOrDeActivateInvoice(int invoiceId , String status, String userId, String roleName) throws ApplicationException{
		logger.info("Entry into activateOrDeActivateInvoice");
		invoiceDao.activateOrDeActivateInvoice(invoiceId, status,userId,roleName);
		
	}


	public Map<String, Integer> getMaxAmountForOrg(Integer organizationId) throws ApplicationException {
		logger.info("Entry into getMaxAmountForOrg");
		return invoiceDao.getMaxAmountForOrg(organizationId );
	}
	
	public void updateInvoice(ArInvoiceVo invoiceVo ) throws ApplicationException {
		logger.info("Entry into updateInvoice");
		if(invoiceVo.getInvoiceId() != null) {
			 try {
				 WorkflowInvoiceVo workflowInvoiceVo=invoiceDao.getWorkflowRequiredDataForInvoiceById(invoiceVo.getInvoiceId());
				 //TODO: May need to remove after balance updation implemented
				 if(workflowInvoiceVo!=null && workflowInvoiceVo.getStatus()!=null && workflowInvoiceVo.getStatus().equalsIgnoreCase(CommonConstants.STATUS_AS_OPEN) && invoiceVo!=null && invoiceVo.getStatus()!=null  && invoiceVo.getStatus().equalsIgnoreCase(CommonConstants.STATUS_AS_ACTIVE)) {
					 invoiceVo.setStatus(CommonConstants.STATUS_AS_OPEN);
				 }
				 
				 
				 invoiceVo =  invoiceDao.updateInvoice(invoiceVo);
				 if( !invoiceVo.getAttachments().isEmpty() ) {
						logger.info("Entry into upload");
						attachmentService.upload(AttachmentsConstants.MODULE_TYPE_ACCOUNTS_RECEIVABLES_INVOICE, invoiceVo.getInvoiceId(), invoiceVo.getOrgId(), invoiceVo.getAttachments());
						logger.info("Upload Successfull");
				 }
				 
				 
					// To add Journal Entries 
					logger.info("Before thread");
					if( (CommonConstants.STATUS_AS_ACTIVE.equals(invoiceVo.getStatus()) || CommonConstants.STATUS_AS_OPEN.equals(invoiceVo.getStatus()) )) {
						logger.info("To thread");
						JournalEntriesThread journalThread =new JournalEntriesThread(journalEntriesTransactionDao, invoiceVo.getInvoiceId(), invoiceVo.getOrgId(),JournalEntriesConstants.SUB_MODULE_INVOICES);
						journalThread.start();
					}
					//Update Balance
					if(!invoiceVo.getStatus().equalsIgnoreCase(CommonConstants.STATUS_AS_DRAFT) && !invoiceVo.getStatus().equalsIgnoreCase(CommonConstants.STATUS_AS_VOID)) {
						new ArBalanceUpdateThread(balanceUpdateDao, invoiceVo.getInvoiceId(), ArInvoiceConstants.MODULE_TYPE_AR_INVOICES).start();
					}
					
					//Trigger Workflow
					 if ( invoiceVo.getStatus() != null
							&& !(invoiceVo.getStatus().equalsIgnoreCase(CommonConstants.STATUS_AS_VOID))) {

						 if(invoiceVo.getStatus().equalsIgnoreCase(CommonConstants.STATUS_AS_DRAFT)) {
							 workflowInvoiceVo=invoiceDao.getWorkflowRequiredDataForInvoiceById(invoiceVo.getInvoiceId());
							//If it is not regular draft
							 if(workflowInvoiceVo!=null && workflowInvoiceVo.getPendingApprovalStatus()!=null && (workflowInvoiceVo.getPendingApprovalStatus().equalsIgnoreCase(CommonConstants.DISPLAY_STATUS_AS_PENDING_FOR_APPROVAL) || workflowInvoiceVo.getPendingApprovalStatus().equalsIgnoreCase(CommonConstants.DISPLAY_STATUS_AS_APPROVAL_DENIED))) {
								 //Update Balance : Will consider balance update,if it is not regular Draft
								 new ArBalanceUpdateThread(balanceUpdateDao, invoiceVo.getInvoiceId(), ArInvoiceConstants.MODULE_TYPE_AR_INVOICES).start();
								 startWorkflow(invoiceVo, WorkflowConstants.WORKFLOW_OPERATION_UDATE);					 
							 }
						 } else {
							 startWorkflow(invoiceVo, WorkflowConstants.WORKFLOW_OPERATION_UDATE);
						 }
						
						}
					
			} catch (Exception e) {
				logger.info("error in updateInvoice in service Layer ",e);
				throw new ApplicationException(e.getMessage());
			}
			 
		}
	}
	private void startWorkflow(ArInvoiceVo invoiceVo,String workflowOperation) throws ApplicationException {
		logger.info("Entry To startWorkflow");
		try {

			// Get Settings for workflow
			SettingsModuleOrganizationVo settingsModuleVo = settingsModuleOrganizationDao
					.getSettingsModuleOrganizationBySubmodule(invoiceVo.getOrgId(),
							WorkflowConstants.MODULE_AR_INVOICE);
			if (settingsModuleVo != null && settingsModuleVo.isRequired()) {
				if(workflowOperation!=null && workflowOperation.equalsIgnoreCase(WorkflowConstants.WORKFLOW_OPERATION_UDATE)) {
					workflowProcessService.deletePreviousRules(WorkflowConstants.MODULE_AR_INVOICE,invoiceVo.getInvoiceId(), invoiceVo.getUserId(), invoiceVo.getRoleName());
				}

				// Get All Applicable Rules
				List<WorkflowSettingsVo> applicableRules = workflowProcessHelperService
						.getApplicableRulesForModuleEntity(invoiceVo.getOrgId(), invoiceVo.getInvoiceId(),
								WorkflowConstants.MODULE_AR_INVOICE);
				if (applicableRules != null && !applicableRules.isEmpty()) {

					// Triggering Workflow
					new WorkflowThread(WorkflowConstants.MODULE_AR_INVOICE, invoiceVo.getInvoiceId(),
							workflowProcessService, applicableRules,workflowOperation)
					.start();
					Thread.sleep(1000);
				}
			}


		}catch(Exception e) {
			logger.error("Error in startWorkflow:",e);	
		}
	}


	public List<ArInvoiceListVo> getRecentInvoicesOfAnOrganizationForDashboard(int organizationId) throws ApplicationException {
		return invoiceDao.getDashboardInvoiceList(organizationId);
	}
}

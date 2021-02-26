package com.blackstrawai.workflow;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.ap.BillsInvoiceDao;
import com.blackstrawai.common.BaseDao;
import com.blackstrawai.common.FinanceCommonDao;
import com.blackstrawai.externalintegration.banking.yesbank.YesBankIntegrationDao;
import com.blackstrawai.externalintegration.yesbank.WorkflowBankingVo;
import com.blackstrawai.onboarding.OrganizationDao;
import com.blackstrawai.onboarding.organization.BasicLocationDetailsVo;
import com.blackstrawai.upload.ModuleTypeVo;

@Service
public class WorkflowProcessHelperService extends BaseDao {

	private Logger logger = Logger.getLogger(WorkflowProcessHelperService.class);
	@Autowired
	private WorkflowSettingsDao workflowSettingsDao;
	@Autowired
	private BillsInvoiceDao billsInvoiceDao;
	@Autowired
	private FinanceCommonDao financeCommonDao;
	@Autowired
	private OrganizationDao organizationDao;
	@Autowired
	private YesBankIntegrationDao yesBankIntegrationDao;

	public List<WorkflowSettingsVo> getApplicableRulesForModuleEntity(int organizationId,int moduleTypeId, String module)
			throws ApplicationException {
		logger.info("Entry into method: createWorkflowProcessForModule");
		List<WorkflowSettingsVo> listOfApplicableRules=new ArrayList<WorkflowSettingsVo>();
		try {
			logger.info("Module"+module);
			// Get module Details
			ModuleTypeVo moduleTypeVo = financeCommonDao.getWorkflowModuleByName(module);
			if(moduleTypeVo!=null && moduleTypeVo.getId()!=null) {
			logger.info("workflowSettingsDao:"+workflowSettingsDao+",moduleTypeVo.getId()"+moduleTypeVo.getId()+":organizationId:"+organizationId+",Module"+module);
			// Get ALL Rules for module
			List<WorkflowSettingsVo> listOfRules = workflowSettingsDao
					.getAllActiveWorkflowSettingsOfAnOrganization(organizationId, moduleTypeVo.getId());
			
			for (int i=0;i<listOfRules.size();i++) {//Remove rules which doesn't have approvers
				WorkflowSettingsVo rule=listOfRules.get(i);
				WorkflowSettingsRuleData data = rule.getData();
				
				if(data!=null ) {
					List<WorkflowSettingsCommonVo> approversList=new ArrayList<WorkflowSettingsCommonVo>();
					if(data.getUsersList()!=null ) {
						approversList.addAll(data.getUsersList());
					}	
//					if(data.getRolesList()!=null ) {
//						approversList.addAll(data.getRolesList());
//					}
					if(data.getSetSequence()!=null ) {
						approversList.addAll(data.getSetSequence());
					}
					if(approversList.isEmpty()) {
						listOfRules.remove(i);
					}
				}
			}
			// Sorting based on Priority
			listOfRules.sort((WorkflowSettingsVo o1, WorkflowSettingsVo o2) -> o1.getPriority() - o2.getPriority());
						

			switch (module) {
			case WorkflowConstants.MODULE_ACCOUNTING_ENTRIES:
				break;

			case WorkflowConstants.MODULE_AP_INVOICE:
				WorkflowInvoiceVo workFlowInvoiceVo = billsInvoiceDao.getWorkflowRequiredDataForInvoiceById(moduleTypeId);

				for (WorkflowSettingsVo workflowSettingsVo : listOfRules) {
					logger.info("Rule:"+workflowSettingsVo);
					WorkflowSettingsRuleData data = workflowSettingsVo.getData();
					
					if(data!=null ) {
						WorkflowRuleConditionVo workflowRuleConditionVo = financeCommonDao
							.getWorkflowConditionById(data.getValidationParameterId());
					List<WorkflowRuleChoiceVo> choices = workflowRuleConditionVo.getChoice();
					// Checking condition
					boolean isCondtionPassed = false;
					
					logger.info("CONDITION:"+workflowRuleConditionVo.getName());
					if(workflowRuleConditionVo!=null && workflowRuleConditionVo.getName()!=null) {

					switch (workflowRuleConditionVo.getName()) {
					case WorkflowConstants.CONDITION_INVOICE_VALUE:
						logger.info("CONDITION:"+WorkflowConstants.CONDITION_INVOICE_VALUE);
						String invoiceValString=workFlowInvoiceVo.getInvoiceValue();
						Double invoiceValue = invoiceValString!=null?Double.parseDouble(invoiceValString):0.0; 

						for (WorkflowRuleChoiceVo choice : choices) {
							if(choice!=null && choice.getName()!=null) {
							switch (choice.getName()) {
							case WorkflowConstants.CHOICE_EQUAL_TO:
								if (choice.getId()==data.getChoiceId() && invoiceValue
										.equals(Double.parseDouble(data.getEqual() != null && data.getEqual().length()>0 ? data.getEqual() : "0"))) {
									logger.info("CHICE PASSED:"+WorkflowConstants.CHOICE_EQUAL_TO);
									isCondtionPassed = true;
								}
								break;
							case WorkflowConstants.CHOICE_GREATER_THAN:
								if (choice.getId()==data.getChoiceId() && invoiceValue > Double
										.parseDouble(data.getGreaterThan() != null && data.getGreaterThan().length()>0? data.getGreaterThan() : "0")) {
									logger.info("CHICE PASSED:"+WorkflowConstants.CHOICE_GREATER_THAN);
									isCondtionPassed = true;
								}

								break;
							case WorkflowConstants.CHOICE_LESS_THAN:
								if (choice.getId()==data.getChoiceId() && invoiceValue < Double
										.parseDouble(data.getLessThan() != null && data.getLessThan().length()>0 ? data.getLessThan() : "0")) {
									logger.info("CHICE PASSED:"+WorkflowConstants.CHOICE_LESS_THAN);
									isCondtionPassed = true;
								}

								break;
							case WorkflowConstants.CHOICE_IN_RANGE:
								if (choice.getId()==data.getChoiceId() && invoiceValue < Double
										.parseDouble(data.getLessThan() != null && data.getLessThan().length()>0? data.getLessThan() : "0")
										&& invoiceValue > Double.parseDouble(
												data.getGreaterThan() != null && data.getGreaterThan().length()>0? data.getGreaterThan() : "0")) {
									logger.info("CHICE PASSED:"+WorkflowConstants.CHOICE_IN_RANGE);
									isCondtionPassed = true;
								}
								break;
							}
						}
						}
						break;
					case WorkflowConstants.CONDITION_LOCATION:
						BasicLocationDetailsVo location = organizationDao.getLocationById(organizationId,
								workFlowInvoiceVo.getLocationId(), workFlowInvoiceVo.isRegistered(), workFlowInvoiceVo.getGstNumber());
					if(location!=null) {	
						String locationName = location.getName();
						for (WorkflowRuleChoiceVo choice : choices) {
							if(choice!=null && choice.getName()!=null) {
							switch (choice.getName()) {
							case WorkflowConstants.CHOICE_IS:
//								List<Object> locationIds = data.getIs();
//								for (Object object : locationIds) {
//									BasicLocationVo locationId = (BasicLocationVo) object;
//									BasicLocationDetailsVo locationDetail = organizationDao.getLocationById(
//											organizationId, locationId.getId(), locationId.getIsRegistered(),
//											locationId.getGstNo());
//									if (choice.getId()==data.getChoiceId() &&  locationDetail!=null && locationName.equals(locationDetail.getName())) {
//										logger.info("CHICE PASSED:"+WorkflowConstants.CHOICE_IS);
//										isCondtionPassed = true;
//										break;
//									}
//								}
								//TODO:
								if (choice.getId()==data.getChoiceId()) {
									isCondtionPassed = true;
									}
								break;
							case WorkflowConstants.CHOICE_IS_NOT:
//								List<Object> locationIdsNot = data.getIsNot();
//								for (Object object : locationIdsNot) {
//									BasicLocationVo locationId = (BasicLocationVo) object;
//									BasicLocationDetailsVo locationDetail = organizationDao.getLocationById(
//											organizationId, locationId.getId(), locationId.getIsRegistered(),
//											locationId.getGstNo());
//									if (choice.getId()==data.getChoiceId() &&  locationDetail!=null && !locationName.equals(locationDetail.getName())) {
//										logger.info("CHICE PASSED:"+WorkflowConstants.CHOICE_IS_NOT);
//										isCondtionPassed = true;
//										break;
//									}
//								}
								//TODO:
								if (choice.getId()==data.getChoiceId()) {
								isCondtionPassed = true;
								}
								break;
							}
						}
						}
					}
						break;
					case WorkflowConstants.CONDITION_GST:
						for (WorkflowRuleChoiceVo choice : choices) {
							if(choice!=null && choice.getName()!=null) {
							switch (choice.getName()) {
							case WorkflowConstants.CHOICE_ENABLED:
								if (choice.getId()==data.getChoiceId() && workFlowInvoiceVo.isGstEnabled()) {
									logger.info("CHICE PASSED:"+WorkflowConstants.CHOICE_ENABLED);
									isCondtionPassed = true;
								}
								break;
							case WorkflowConstants.CHOICE_DISABLED:
								if (choice.getId()==data.getChoiceId() && !workFlowInvoiceVo.isGstEnabled()) {
									logger.info("CHICE PASSED:"+WorkflowConstants.CHOICE_DISABLED);
									isCondtionPassed = true;
								}

								break;
							}
						}
						}
						break;
					case WorkflowConstants.CONDITION_VENDOR:
						for (WorkflowRuleChoiceVo choice : choices) {
							if(choice!=null && choice.getName()!=null) {
							switch (choice.getName()) {
							case WorkflowConstants.CHOICE_IS:
								List<Object> vendorIds = data.getIs();
								for (Object object : vendorIds) {
									Long vendorId = (Long) object;
									if (choice.getId()==data.getChoiceId() && vendorId.equals(workFlowInvoiceVo.getVendorId())) {
										logger.info("CHICE PASSED:"+WorkflowConstants.CHOICE_IS);
										isCondtionPassed = true;
										break;
									}
								}

								break;
							case WorkflowConstants.CHOICE_IS_NOT:
								List<Object> vendorIdsIsNot = data.getIsNot();
								for (Object object : vendorIdsIsNot) {
									Long vendorId = (Long) object;
									if (choice.getId()==data.getChoiceId() && !vendorId.equals(workFlowInvoiceVo.getVendorId())) {
										logger.info("CHICE PASSED:"+WorkflowConstants.CHOICE_IS_NOT);
										isCondtionPassed = true;
										break;
									}
								}
								break;
							}
						}
						}
						break;

					default:
						
						// For system Default rules which doesn't have mandatory condition
					
						break;
					}
					
				}else {
					logger.info("Default case ,Rule:"+workFlowInvoiceVo);
					if (workflowSettingsVo!=null && workflowSettingsVo.getIsBase()) {						
						isCondtionPassed = true;
					}
				}
					if (isCondtionPassed ) {// IF Condition Passed then align Approvers
						listOfApplicableRules.add(workflowSettingsVo);
					}
					logger.info("Applicable Rules:"+listOfApplicableRules);

				}
				}
				break;

			case WorkflowConstants.MODULE_AR_APPLICATION_OF_FUNDS:
				break;
			case WorkflowConstants.MODULE_AR_CREDITNOTE:
				for (WorkflowSettingsVo workflowSettingsVo : listOfRules) {
					logger.info("Rule:"+workflowSettingsVo);
					WorkflowSettingsRuleData data = workflowSettingsVo.getData();
					
					if(data!=null ) {
						WorkflowRuleConditionVo workflowRuleConditionVo = financeCommonDao
							.getWorkflowConditionById(data.getValidationParameterId());
					List<WorkflowRuleChoiceVo> choices = workflowRuleConditionVo.getChoice();
					// Checking condition
					boolean isCondtionPassed = false;
					
					logger.info("CONDITION:"+workflowRuleConditionVo.getName());
					if(workflowRuleConditionVo!=null && workflowRuleConditionVo.getName()!=null) {
						//TODO:Yet to add conditions
						isCondtionPassed = true;
					}else {
					//	logger.info("Default case ,Rule:"+workFlowInvoiceVo);
						if (workflowSettingsVo!=null && workflowSettingsVo.getIsBase()) {						
							isCondtionPassed = true;
						}
					}
						if (isCondtionPassed ) {// IF Condition Passed then align Approvers
							listOfApplicableRules.add(workflowSettingsVo);
						}
						logger.info("Applicable Rules:"+listOfApplicableRules);

					}
					}

				break;
			case WorkflowConstants.MODULE_AR_INVOICE:

				for (WorkflowSettingsVo workflowSettingsVo : listOfRules) {
					logger.info("Rule:"+workflowSettingsVo);
					WorkflowSettingsRuleData data = workflowSettingsVo.getData();
					
					if(data!=null ) {
						WorkflowRuleConditionVo workflowRuleConditionVo = financeCommonDao
							.getWorkflowConditionById(data.getValidationParameterId());
					List<WorkflowRuleChoiceVo> choices = workflowRuleConditionVo.getChoice();
					// Checking condition
					boolean isCondtionPassed = false;
					
					logger.info("CONDITION:"+workflowRuleConditionVo.getName());
					if(workflowRuleConditionVo!=null && workflowRuleConditionVo.getName()!=null) {
						//TODO:Yet to add conditions
						isCondtionPassed = true;
					}else {
					//	logger.info("Default case ,Rule:"+workFlowInvoiceVo);
						if (workflowSettingsVo!=null && workflowSettingsVo.getIsBase()) {						
							isCondtionPassed = true;
						}
					}
						if (isCondtionPassed ) {// IF Condition Passed then align Approvers
							listOfApplicableRules.add(workflowSettingsVo);
						}
						logger.info("Applicable Rules:"+listOfApplicableRules);

					}
					}
						
				break;
			case WorkflowConstants.MODULE_AR_LUT:
				break;
			case WorkflowConstants.MODULE_AR_RECEIPTS:
				break;
			case WorkflowConstants.MODULE_AR_REFUND:
				break;
			case WorkflowConstants.MODULE_CONTRA:
				break;
			case WorkflowConstants.MODULE_EMPLOYEE_REIMBURSEMENT:
				break;
			case WorkflowConstants.MODULE_EXPENSES_NON_VENDOR:
				break;
			case WorkflowConstants.MODULE_PAYMENTS:
				break;
			case WorkflowConstants.MODULE_PAYROLL:
				break;
			case WorkflowConstants.MODULE_PURCHASE_ORDER:
				break;
			case WorkflowConstants.MODULE_RECONCIALLATION:
				break;
			case WorkflowConstants.MODULE_RECONCILLATION_BANK_STATEMENT:
				break;
			case WorkflowConstants.MODULE_RECONCILLSTION_GL:
				break;
			case WorkflowConstants.MODULE_VENDOR_PORTAL_BALANCE_CONFIRMATION:
				break;
			case WorkflowConstants.MODULE_BANKING:
					for (WorkflowSettingsVo workflowSettingsVo : listOfRules) {
						logger.info("Rule:" + workflowSettingsVo);
						WorkflowSettingsRuleData data = workflowSettingsVo.getData();

						if (data != null) {
							WorkflowRuleConditionVo workflowRuleConditionVo = financeCommonDao
									.getWorkflowConditionById(data.getValidationParameterId());
							List<WorkflowRuleChoiceVo> choices = workflowRuleConditionVo.getChoice();
							// Checking condition
							boolean isCondtionPassed = false;

							logger.info("CONDITION:" + workflowRuleConditionVo.getName());
							if (workflowRuleConditionVo != null && workflowRuleConditionVo.getName() != null) {
								switch (workflowRuleConditionVo.getName()) {
								case WorkflowConstants.CONDITION_TX_AMT:
									WorkflowBankingVo paymentVo = yesBankIntegrationDao
											.getWorfklowDataForPayment(moduleTypeId);
									Double amount = paymentVo.getAmount() != null
											? Double.parseDouble(paymentVo.getAmount())
											: 0.00;
									Double min = data.getMin()!= null && data.getMin().trim().length()>0  ? Double.parseDouble(data.getMin()) : 0.00;
									Double max = data.getMax()!= null && data.getMax().trim().length()>0  ? Double.parseDouble(data.getMax()) : 0.00;
									logger.info("Values: Amount" + amount + ",min:" + min + ",max:" + max);
									if (min > 0) {
										if (amount >= min) {
											isCondtionPassed = true;
										}
									}
									logger.info("Min Condition passed?" + isCondtionPassed);
									//Max is optional
									if (max > 0) {
										if (min < max) {
											if (amount>=min && amount <= max) {
												isCondtionPassed = true;
											} else {
												isCondtionPassed = false;
											}
										} else {
											isCondtionPassed = false;
										}

									}
									logger.info("Max Condition passed?" + isCondtionPassed);
									break;
								}

							}
							if (isCondtionPassed) {// IF Condition Passed then align Approvers
								listOfApplicableRules.add(workflowSettingsVo);
							}
							logger.info("Applicable Rules:" + listOfApplicableRules);

						}
					}
				break;
			default:
				break;
			}
			}

		} catch (Exception e) {
			logger.error("Error in createWorkflowSettings", e);
			throw new ApplicationException(e);
		}
		return listOfApplicableRules;
	}
	


}

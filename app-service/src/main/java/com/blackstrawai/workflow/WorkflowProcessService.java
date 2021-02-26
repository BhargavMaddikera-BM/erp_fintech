package com.blackstrawai.workflow;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.ap.ApBalanceUpdateDao;
import com.blackstrawai.ap.BillsInvoiceDao;
import com.blackstrawai.ar.ArBalanceUpdateDao;
import com.blackstrawai.ar.ArInvoiceDao;
import com.blackstrawai.ar.CreditNotesDao;
import com.blackstrawai.common.BaseDao;
import com.blackstrawai.common.CommonConstants;
import com.blackstrawai.common.DropDownDao;
import com.blackstrawai.common.FinanceCommonDao;
import com.blackstrawai.externalintegration.banking.yesbank.YesBankIntegrationDao;
import com.blackstrawai.externalintegration.banking.yesbank.YesBankPaymentHelperService;
import com.blackstrawai.externalintegration.yesbank.WorkflowBankingVo;
import com.blackstrawai.onboarding.EmailThread;
import com.blackstrawai.onboarding.RoleDao;
import com.blackstrawai.onboarding.UserDao;
import com.blackstrawai.onboarding.user.UserVo;
import com.blackstrawai.settings.CommonVo;
import com.blackstrawai.upload.ModuleTypeVo;

@Service
public class WorkflowProcessService extends BaseDao {

	private Logger logger = Logger.getLogger(WorkflowProcessService.class);
	@Autowired
	private WorkflowSettingsUserApprovalDao workflowSettingsUserApprovalDao;
	@Autowired
	private WorkflowSettingsDao workflowSettingsDao;
	@Autowired
	private FinanceCommonDao financeCommonDao;
	@Autowired
	private BillsInvoiceDao billsInvoiceDao;
	@Autowired
	private CreditNotesDao creditNotesDao;
	@Autowired
	private ArInvoiceDao arInvoiceDao;
	@Autowired
	private ArBalanceUpdateDao arBalanceUpdateDao;
	@Autowired
	private ApBalanceUpdateDao apBalanceUpdateDao;
	@Autowired
	private RoleDao roleDao;
	@Autowired
	private UserDao userDao;
	@Autowired
	private YesBankIntegrationDao yesBankIntegrationDao;
	@Autowired
	private YesBankPaymentHelperService yesBankPaymentHelperService;
	private int priority = 1;
	@Autowired
	private DropDownDao dropDownDao;
	
	
	public List<WorkflowSettingsUserApprovalVo> createApprovers(List<WorkflowSettingsVo> applicableRules,
			int moduleTypeId) throws ApplicationException {

		List<WorkflowSettingsUserApprovalVo> approvers = new ArrayList<WorkflowSettingsUserApprovalVo>();
		try {
			if (applicableRules != null && applicableRules.size() > 0) {
				int currentRuleId = applicableRules.get(0).getId();// Set First object as Current Rule

				for (WorkflowSettingsVo rule : applicableRules) {

					WorkflowSettingsRuleData data = rule.getData();
					//List<WorkflowSettingsCommonVo> roles = data.getRolesList();
					List<WorkflowSettingsCommonVo> users = data.getUsersList();
					if (data != null) {
						CommonVo approvalTypes = financeCommonDao.getWorkflowApprovalTypeById(data.getApprovalTypeId());

						if (approvalTypes != null) {
							logger.info("Approval Type:" + approvalTypes.getName() + ",First Rule Id:" + currentRuleId
									+ ",Priority:" + priority + ",Current Rule:" + rule.getId());
							switch (approvalTypes.getName()) {
							case WorkflowConstants.APPROVAL_TYPE_ANYONE_CAN_APPROVE:
								users.forEach(userVo -> {
									UserVo basicUserVo = null;
									try {
										basicUserVo = userDao.getBasicUserDetailsById(userVo.getId());
									} catch (ApplicationException e) {
										logger.error("Error while retrieving USerDetails :" + userVo.getId());
									}
									if (basicUserVo != null) {
										WorkflowSettingsUserApprovalVo approver = new WorkflowSettingsUserApprovalVo();
										approver.setPriority(priority);
										approver.setPriority(1);
										approver.setUserId(basicUserVo.getId());
										approver.setRoleName(basicUserVo.getRoleName());
										approver.setModuleId(rule.getModuleId());
										approver.setModuleTypeId(moduleTypeId);
										approver.setRuleId(rule.getId());
										approver.setOrganizationId(rule.getOrganizationId());
										approver.setEmail(data.isEmail());
										approver.setSms(data.isSms());
										approver.setWhatsApp(data.isWhatsApp());
										approver.setInApp(data.isInApp());
										String status = "NA";
										if (rule.getId() == currentRuleId && priority == 1) {
											status = CommonConstants.STATUS_AS_PENDING_FOR_APPROVAL;
										}
										approver.setStatus(status);
										approvers.add(approver);

									}
								});
								if (!approvers.isEmpty()) {
									priority++;
								}
								break;
							case WorkflowConstants.APPROVAL_TYPE_EVERYONE_SHOULD_APPROVE:

								logger.info("Approval Type:" + approvalTypes.getName()
								+ ",Approvers Size after adding Role: " + approvers.size());
								logger.info("Users size:" + users.size());
								users.forEach(userVo -> {

									UserVo basicUserVo = null;
									try {
										logger.info("OrgId:" + rule.getOrganizationId() + ",User ID:" + userVo.getId());
										basicUserVo = userDao.getBasicUserDetailsById(userVo.getId());
									} catch (ApplicationException e) {
										logger.error("Error while retrieving USerDetails :" + userVo.getId());
									}

									if (basicUserVo != null) {
										logger.info("User :" + basicUserVo.getId());
										WorkflowSettingsUserApprovalVo approver = new WorkflowSettingsUserApprovalVo();
										String status = "NA";
										if (rule.getId() == currentRuleId && priority == 1) {
											status = CommonConstants.STATUS_AS_PENDING_FOR_APPROVAL;
										}
										approver.setStatus(status);
										approver.setPriority(priority);
										approver.setUserId(basicUserVo.getId());
										approver.setRoleName(basicUserVo.getRoleName());
										approver.setModuleId(rule.getModuleId());
										approver.setModuleTypeId(moduleTypeId);
										approver.setRuleId(rule.getId());
										approver.setOrganizationId(rule.getOrganizationId());
										approver.setEmail(data.isEmail());
										approver.setSms(data.isSms());
										approver.setWhatsApp(data.isWhatsApp());
										approver.setInApp(data.isInApp());
										approvers.add(approver);
										// priority++;
									}
								});
								if (!approvers.isEmpty()) {
									priority++;
								}
								logger.info("Approval Type:" + approvalTypes.getName()
								+ ",Approvers Size after adding users: " + approvers.size());
								break;
							case WorkflowConstants.APPROVAL_TYPE_EVERYONE_APPROVES_SEQUENTIALY:
								List<WorkflowSettingsCommonVo> sequenceUsers = data.getSetSequence();
								logger.info("Sequence Users:" + sequenceUsers);

								sequenceUsers.forEach(user -> {
									if (user != null) {
										UserVo userVo = null;

										logger.info("Approval Type :"
												+ WorkflowConstants.APPROVAL_TYPE_EVERYONE_APPROVES_SEQUENTIALY
												+ ": user type: " + user.getType());
										if (user.getType().equalsIgnoreCase("User")) {

											try {
												userVo = userDao.getBasicUserDetailsById(user.getId());
											} catch (ApplicationException e) {
												logger.error("Error while retrieving USerDetails :" + user.getId());
											}
											logger.info("Retrieved User:" + userVo);
											if (userVo != null) {
												WorkflowSettingsUserApprovalVo approver = new WorkflowSettingsUserApprovalVo();
												String status = "NA";
												if (rule.getId() == currentRuleId && priority == 1) {
													status = CommonConstants.STATUS_AS_PENDING_FOR_APPROVAL;
												}
												approver.setStatus(status);
												approver.setPriority(priority);
												approver.setUserId(userVo.getId());
												approver.setRoleName(userVo.getRoleName());
												approver.setModuleId(rule.getModuleId());
												approver.setModuleTypeId(moduleTypeId);
												approver.setRuleId(rule.getId());
												approver.setOrganizationId(rule.getOrganizationId());
												approver.setEmail(data.isEmail());
												approver.setSms(data.isSms());
												approver.setWhatsApp(data.isWhatsApp());
												approver.setInApp(data.isInApp());
												approvers.add(approver);
												priority++;
											}
											logger.info("Approval Type:" + approvalTypes.getName()
											+ ",Approvers Size after adding ROles: " + approvers.size());
										} else if (user.getType().equalsIgnoreCase("Role")) {

											List<UserVo> roleUsers = null;
											try {
												roleUsers = roleDao.getUsersForRoleInOrganization(
														rule.getOrganizationId(), user.getId());
											} catch (ApplicationException e) {
												logger.error("Error while retrieving USerDetails :" + user.getId());
											}

											if (roleUsers != null) {
												logger.info("Retrieved ROle No. of Users:" + roleUsers.size());
												roleUsers.forEach(roleUserVo -> {
													WorkflowSettingsUserApprovalVo approver = new WorkflowSettingsUserApprovalVo();
													String status = "NA";
													if (rule.getId() == currentRuleId && priority == 1) {
														status = CommonConstants.STATUS_AS_PENDING_FOR_APPROVAL;
													}
													approver.setStatus(status);
													approver.setPriority(priority);
													approver.setUserId(roleUserVo.getId());
													approver.setRoleName(user.getName());
													approver.setModuleId(rule.getModuleId());
													approver.setModuleTypeId(moduleTypeId);
													approver.setRuleId(rule.getId());
													approver.setOrganizationId(rule.getOrganizationId());
													approver.setEmail(data.isEmail());
													approver.setSms(data.isSms());
													approver.setWhatsApp(data.isWhatsApp());
													approver.setInApp(data.isInApp());
													approvers.add(approver);
													priority++;
												});
											}
											logger.info("Approval Type:" + approvalTypes.getName()
											+ ",Approvers Size after adding users: " + approvers.size());
										}

									}

								});
								break;
							}
						}

					}
				}

				for (WorkflowSettingsUserApprovalVo approver : approvers) {
					workflowSettingsUserApprovalDao.createWorkflowSettingsApprovalUser(approver);
					if (approver != null && approver.getStatus() != null
							&& approver.getStatus().equalsIgnoreCase(CommonConstants.STATUS_AS_PENDING_FOR_APPROVAL)) {
						sendNotification(approver);
					}
				}

				priority = 1;
			}
		} catch (Exception e) {
			logger.error("Error in createApprovers", e);
		}
		return approvers;
	}

	public void updateCurrentRule(WorkflowSettingsUserApprovalVo approver, boolean isReject)
			throws ApplicationException {
		try {
			if (approver != null && approver.getModuleId() > 0 && approver.getModuleTypeId() > 0) {

				ModuleTypeVo moduleVo = financeCommonDao.getWorkflowModuleById(approver.getModuleId());
				boolean approvalsPendingForModuleTypeId = workflowSettingsUserApprovalDao.anyApprovalsPendingForModuleTypeId(approver.getOrganizationId(),approver.getModuleId(), approver.getModuleTypeId());


				if (moduleVo != null && moduleVo.getName() != null) {
					int ruleId = approver.getRuleId();

					switch (moduleVo.getName()) {
					case WorkflowConstants.MODULE_ACCOUNTING_ENTRIES:
						break;

					case WorkflowConstants.MODULE_AP_INVOICE:

						WorkflowInvoiceVo invoiceVo = billsInvoiceDao.getWorkflowRequiredDataForInvoiceById(approver.getModuleTypeId());
						logger.info("updateCurrentRule invoiceVo:" + invoiceVo + ",approvalsPendingForModuleTypeId:"
								+ approvalsPendingForModuleTypeId);
						if (invoiceVo != null && invoiceVo.getStatus() != null) {
							String status = invoiceVo.getStatus();
							String pendingApprovalstatus = invoiceVo.getPendingApprovalStatus();
							logger.info("updateCurrentRule status:" + status + ",pendingApprovalstatus:"
									+ pendingApprovalstatus);
							Map<String,Object> statusValues=setWorkflowStatusValues(status,pendingApprovalstatus,approvalsPendingForModuleTypeId,isReject,ruleId);
							ruleId=statusValues.get("ruleId")!=null?(Integer)statusValues.get("ruleId"):0;
							status=statusValues.get("status")!=null?(String)statusValues.get("status"):null;
							pendingApprovalstatus=statusValues.get("pendingApprovalstatus")!=null?(String)statusValues.get("pendingApprovalstatus"):null;
							// Update Current Rule
							billsInvoiceDao.updateCurrentWorkflowRule(approver.getModuleTypeId(),ruleId , status,pendingApprovalstatus);
							if (status != null && status.equalsIgnoreCase(CommonConstants.STATUS_AS_OPEN)) {
								apBalanceUpdateDao.updateInvoiceDueBalance(approver.getModuleTypeId());
							}
						}
						break;
					case WorkflowConstants.MODULE_AR_APPLICATION_OF_FUNDS:
						break;
					case WorkflowConstants.MODULE_AR_CREDITNOTE:
						WorkflowInvoiceVo creditNoteVo = creditNotesDao
						.getWorkflowRequiredDataForCreditNotebyId(approver.getModuleTypeId());
						logger.info("updateCurrentRule arInvoiceVo:" + creditNoteVo + ",approvalsPendingForModuleTypeId:"
								+ approvalsPendingForModuleTypeId);
						if (creditNoteVo != null && creditNoteVo.getStatus() != null) {
							String status = creditNoteVo.getStatus();
							String pendingApprovalstatus = creditNoteVo.getPendingApprovalStatus();
							Map<String,Object> statusValues=setWorkflowStatusValues(status,pendingApprovalstatus,approvalsPendingForModuleTypeId,isReject,ruleId);

							ruleId=statusValues.get("ruleId")!=null?(Integer)statusValues.get("ruleId"):0;
							status=statusValues.get("status")!=null?(String)statusValues.get("status"):null;
							pendingApprovalstatus=statusValues.get("pendingApprovalstatus")!=null?(String)statusValues.get("pendingApprovalstatus"):null;
							// Update Current Rule
							creditNotesDao.updateCurrentWorkflowRule(approver.getModuleTypeId(),ruleId , status,pendingApprovalstatus);
							arBalanceUpdateDao.updateInvoiceDueBalance(creditNoteVo.getOriginalInvoiceId());
						}
						break;
					case WorkflowConstants.MODULE_AR_INVOICE:

						WorkflowInvoiceVo arInvoiceVo = arInvoiceDao
						.getWorkflowRequiredDataForInvoiceById(approver.getModuleTypeId());
						logger.info("updateCurrentRule arInvoiceVo:" + arInvoiceVo + ",approvalsPendingForModuleTypeId:"
								+ approvalsPendingForModuleTypeId);
						if (arInvoiceVo != null && arInvoiceVo.getStatus() != null) {
							String status = arInvoiceVo.getStatus();
							String pendingApprovalstatus = arInvoiceVo.getPendingApprovalStatus();
							Map<String,Object> statusValues=setWorkflowStatusValues(status,pendingApprovalstatus,approvalsPendingForModuleTypeId,isReject,ruleId);

							ruleId=statusValues.get("ruleId")!=null?(Integer)statusValues.get("ruleId"):0;
							status=statusValues.get("status")!=null?(String)statusValues.get("status"):null;
							pendingApprovalstatus=statusValues.get("pendingApprovalstatus")!=null?(String)statusValues.get("pendingApprovalstatus"):null;
							// Update Current Rule
							arInvoiceDao.updateCurrentWorkflowRule(approver.getModuleTypeId(),ruleId , status,pendingApprovalstatus);
							if(status!=null && status.equalsIgnoreCase(CommonConstants.STATUS_AS_OPEN)) {
								arBalanceUpdateDao.updateInvoiceDueBalance(approver.getModuleTypeId());
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
						if(!approvalsPendingForModuleTypeId) {
							WorkflowBankingVo bankingVo=yesBankIntegrationDao.getWorfklowDataForPayment(approver.getModuleTypeId());
							String paymentType=bankingVo.getPaymentType();
							if(isReject) {
								yesBankPaymentHelperService.rejectOrDeclinePayment(approver.getModuleTypeId(), true,paymentType,bankingVo.getFileIdentifier());
							}else {
								if(paymentType!=null && paymentType.equalsIgnoreCase("Single")) {
								yesBankPaymentHelperService.makeSinglePayment(bankingVo.getPaymentTransferPaymentsVo(), bankingVo.getOrganizationId()+"", bankingVo.getUserId(), bankingVo.getRoleName(), bankingVo.getFinalJsonRequest(), bankingVo.getUiJsonRequest(), approver.getModuleTypeId());
								}else if(paymentType!=null && paymentType.equalsIgnoreCase("Bulk")) {
									yesBankPaymentHelperService.makeBulkPayment(approver.getModuleTypeId());
								}
							}
						}
						break;
					default:
						break;

					}
				}
			}
		} catch (Exception e) {
			logger.error("Error in updateCurrentRule", e);
		}

	}

	private Map<String,Object> setWorkflowStatusValues(String status,String pendingApprovalstatus,boolean approvalsPendingForModuleTypeId,boolean isReject,int ruleId){
		Map<String,Object> statusValues=new HashMap<String,Object>();
		// Direct submit or draft to submit
		if (status.equalsIgnoreCase(CommonConstants.STATUS_AS_ACTIVE)
				&& approvalsPendingForModuleTypeId) {
			status = CommonConstants.STATUS_AS_DRAFT;
			if (pendingApprovalstatus == null || (pendingApprovalstatus!=null && pendingApprovalstatus.equalsIgnoreCase(CommonConstants.DISPLAY_STATUS_AS_APPROVAL_DENIED))) {
				pendingApprovalstatus = CommonConstants.DISPLAY_STATUS_AS_PENDING_FOR_APPROVAL;
			}
		} else if (status.equalsIgnoreCase(CommonConstants.STATUS_AS_OPEN) || status.equalsIgnoreCase(CommonConstants.STATUS_AS_UNPAID) || status.equalsIgnoreCase(CommonConstants.STATUS_AS_OVERDUE)) {// Re-Open
			if (approvalsPendingForModuleTypeId) {
				status = CommonConstants.STATUS_AS_DRAFT;
				if (pendingApprovalstatus == null || (pendingApprovalstatus!=null && pendingApprovalstatus.equalsIgnoreCase(CommonConstants.DISPLAY_STATUS_AS_APPROVAL_DENIED))) {
					pendingApprovalstatus = CommonConstants.DISPLAY_STATUS_AS_PENDING_FOR_APPROVAL;
				}
			} else {
				pendingApprovalstatus = null;
				ruleId = 0;
			}
		} else if (status.equalsIgnoreCase(CommonConstants.STATUS_AS_PARTIALLY_PAID)
				|| status.equalsIgnoreCase(CommonConstants.STATUS_AS_PAID)				
				|| status.equalsIgnoreCase(CommonConstants.STATUS_AS_ADJUSTED)
				|| status.equalsIgnoreCase(CommonConstants.STATUS_AS_PARTIALLY_ADJUSTED)
				|| status.equalsIgnoreCase(CommonConstants.STATUS_AS_VOID)) {
			if (approvalsPendingForModuleTypeId) {
				pendingApprovalstatus = CommonConstants.DISPLAY_STATUS_AS_PENDING_FOR_APPROVAL;
			} else {
				pendingApprovalstatus = null;
				ruleId = 0;
			}

		} else if (status.equalsIgnoreCase(CommonConstants.STATUS_AS_DRAFT)	) {// End of workflow
			if (approvalsPendingForModuleTypeId) {
				pendingApprovalstatus = CommonConstants.DISPLAY_STATUS_AS_PENDING_FOR_APPROVAL;
			} else {
				
				pendingApprovalstatus = null;
				ruleId = 0;
				if(!isReject) {
					
					status = CommonConstants.STATUS_AS_OPEN;
				}
			}


		}

		logger.info("setWorkflowStatusValues Status:" + status + ",PendingApprovalstatus:" + pendingApprovalstatus);
		if (isReject) {// ON reject original status will retain
			pendingApprovalstatus = CommonConstants.DISPLAY_STATUS_AS_APPROVAL_DENIED;
			ruleId = 0;
		}

		statusValues.put("status", status);
		statusValues.put("pendingApprovalstatus", pendingApprovalstatus);
		statusValues.put("ruleId", ruleId);
		logger.info("statusValues:"+statusValues);
		return statusValues;
	}

	public boolean deletePreviousRules(String module, int moduleTypeId, String userId, String roleName)
			throws ApplicationException {
		return workflowSettingsUserApprovalDao.deleteOrRejectRules(module, moduleTypeId, true, userId, roleName,"",0);
	}

	public boolean approveRule(ApprovalVo approvalVoObj) throws ApplicationException {
		logger.info("Entry into method approveRule ID:" + approvalVoObj);
		boolean result = false;
		WorkflowSettingsUserApprovalVo currentRule = null;
		List<WorkflowSettingsUserApprovalVo> nextApprovers = approveRuleAndGetNextApprovers(approvalVoObj);
		logger.info("approveRule: ***nextApprovers:" + nextApprovers);
		if (nextApprovers != null && !nextApprovers.isEmpty()) {
			moveToNextApproverOrRule(nextApprovers);
			currentRule = nextApprovers.get(0);
		} else {
			if (approvalVoObj != null && approvalVoObj.getApprovalId() > 0) {
				WorkflowSettingsUserApprovalVo approvalVo = workflowSettingsUserApprovalDao
						.getApprovalById(approvalVoObj.getApprovalId());
				currentRule = approvalVo;
			}

		}
		logger.info("approveRule: nextApprovers:" + nextApprovers + ",currentRule:" + currentRule);
		if (currentRule != null) {
			updateCurrentRule(currentRule, false);
		}
		result = true;
		return result;
	}

	private List<WorkflowSettingsUserApprovalVo> approveRuleAndGetNextApprovers(ApprovalVo approvalVoObj)
			throws ApplicationException {
		logger.info("Entry into method approveRuleAndGetNextApprovers ID:" + approvalVoObj);
		List<WorkflowSettingsUserApprovalVo> nextApprovers = new ArrayList<WorkflowSettingsUserApprovalVo>();
		try {
			if (approvalVoObj != null && approvalVoObj.getApprovalId() > 0) {
				int approvalId = approvalVoObj.getApprovalId();
				int approverUserId = approvalVoObj.getUserId() != null ? Integer.parseInt(approvalVoObj.getUserId())
						: 0;
				String approverRoleName = approvalVoObj.getRoleName();
				WorkflowSettingsUserApprovalVo approvalVo = workflowSettingsUserApprovalDao.getApprovalById(approvalId);
				// IF it is pending approval
				if (approvalVo != null && approvalVo.getStatus() != null
						&& approvalVo.getStatus().equalsIgnoreCase(CommonConstants.STATUS_AS_PENDING_FOR_APPROVAL)) {

					// Update Approver
					workflowSettingsUserApprovalDao.approveRule(approvalId, approverUserId, approverRoleName);
					String ruleApprovalType = workflowSettingsUserApprovalDao.getApprovalRuleType(approvalId);
					// OtherApproversForRule Ordered by priority
					List<WorkflowSettingsUserApprovalVo> otherApproversForRule = workflowSettingsUserApprovalDao
							.getOtherPendingApprovalsByRule(approvalVo.getModuleId(), approvalVo.getModuleTypeId(),
									approvalVo.getRuleId(), approvalId);
					// NextRulesWithApprovers Ordered by priority
					List<WorkflowSettingsUserApprovalVo> nextRulesWithApprovers = workflowSettingsUserApprovalDao
							.getNextApprovalRulesForModuleEntity(approvalVo.getModuleId(), approvalVo.getModuleTypeId(),
									approvalVo.getRuleId());
					// CHeck any other approvals are pending from same rule
					boolean isApprovalsPendingForRule = workflowSettingsUserApprovalDao.anyApprovalsPendingForRule(
							approvalVo.getOrganizationId(), approvalVo.getModuleId(), approvalVo.getModuleTypeId(),
							approvalVo.getRuleId());
					logger.info(
							"approveRule ID:" + approvalId + " ,isApprovalsPendingForRule:" + isApprovalsPendingForRule
							+ ",ruleApprovalType:" + ruleApprovalType + ",otherApproversForRule:"
							+ otherApproversForRule + ",nextRulesWithApprovers:" + nextRulesWithApprovers);
					if (isApprovalsPendingForRule) {// Move to Next Approver
						if (ruleApprovalType != null && ruleApprovalType
								.equalsIgnoreCase(WorkflowConstants.APPROVAL_TYPE_ANYONE_CAN_APPROVE)) {
							// Auto Approve others in Same rule IF it is of type 'AnyoneCan Approve'
							for (WorkflowSettingsUserApprovalVo approver : otherApproversForRule) {
								workflowSettingsUserApprovalDao.approveRule(approver.getId(), approverUserId,
										approverRoleName);
								// updateCurrentRule(approver,false);//Update Rule
							}
							otherApproversForRule = new ArrayList<WorkflowSettingsUserApprovalVo>();// Reset
						}
						if (nextRulesWithApprovers != null && !nextRulesWithApprovers.isEmpty()) {
							// Combining Next Rule Approvers
							otherApproversForRule.addAll(nextRulesWithApprovers);

						}
						logger.info("Moving to Next Approver, Next Approvers:" + otherApproversForRule);
						nextApprovers = otherApproversForRule;
						// Move to Next Approver
						// moveToNextApproverOrRule(otherApproversForRule);

					} else { // Move to Next Rule

						if (nextRulesWithApprovers != null && !nextRulesWithApprovers.isEmpty()) {// Trigger Next Rule
							logger.info("Moving to Next RULE ,Next Approvers:" + nextRulesWithApprovers);
							nextApprovers = nextRulesWithApprovers;
							// moveToNextApproverOrRule(nextRulesWithApprovers);
							// updateCurrentRule(nextRulesWithApprovers.get(0),false);//Update Rule
						}
						// }else {
						// updateCurrentRule(approvalVo,false);//Update Rule
						// }

					}

				}
			}
		} catch (Exception e) {
			logger.error("Error in approveRuleAndGetNextApprovers", e);
		}
		return nextApprovers;
	}

	public boolean rejectRule(ApprovalVo approvalVoObj) throws ApplicationException {
		WorkflowSettingsUserApprovalVo approvalVo = workflowSettingsUserApprovalDao
				.getApprovalById(approvalVoObj.getApprovalId());
		if (approvalVo != null) {
			ModuleTypeVo moduleVo = financeCommonDao.getWorkflowModuleById(approvalVo.getModuleId());
			if (moduleVo != null) {
				workflowSettingsUserApprovalDao.deleteOrRejectRules(moduleVo.getName(), approvalVo.getModuleTypeId(),
						false, approvalVoObj.getUserId(), approvalVoObj.getRoleName(),approvalVoObj.getRejectionRemarks(),approvalVoObj.getRejectionTypeId());
				updateCurrentRule(approvalVo, true);
			}
		}
		return true;
	}

	public List<WorkflowUserApprovalDetailsVo> getPendingApprovalsByOrganization(int organizationId, int userId,
			String roleName) throws ApplicationException {
		logger.info("Entry into method getPendingApprovalsByOrganization :");
		return workflowSettingsUserApprovalDao.getPendingApprovalsByOrganization(organizationId, userId, roleName);
	}

	public void moveToNextApproverOrRule(List<WorkflowSettingsUserApprovalVo> nextApprovers) throws ApplicationException {
		logger.info("Entry into method moveToNextApproverOrRule :");
		try {

			if(nextApprovers!=null && !nextApprovers.isEmpty()) {
				int previousRule=nextApprovers.get(0).getRuleId();
				boolean isFirst=true;
				boolean isSequential=true;
				for(WorkflowSettingsUserApprovalVo approver:nextApprovers) {
					WorkflowSettingsVo ruleVo=workflowSettingsDao.getWorkflowSettingsById(approver.getRuleId());
					WorkflowSettingsRuleData ruleData=ruleVo.getData();
					CommonVo ruleApprovalType=financeCommonDao.getWorkflowApprovalTypeById(ruleData.getApprovalTypeId());

					String status="NA";
					if(isFirst) {
						status=CommonConstants.STATUS_AS_PENDING_FOR_APPROVAL;
						if( (previousRule==approver.getRuleId() && ruleApprovalType!=null && ruleApprovalType.getName()!=null && (ruleApprovalType.getName().equalsIgnoreCase(WorkflowConstants.APPROVAL_TYPE_ANYONE_CAN_APPROVE) || ruleApprovalType.getName().equalsIgnoreCase(WorkflowConstants.APPROVAL_TYPE_EVERYONE_SHOULD_APPROVE)))) {
							isSequential=false;
						}else {
							isFirst=false;
							if(!isSequential) {
								status="NA";
							}

						}
					}

					approver.setStatus(status);
					workflowSettingsUserApprovalDao.updateWorkflowSettingsApprovalUser(approver);
					if(approver!=null  && approver.getStatus()!=null && approver.getStatus().equalsIgnoreCase(CommonConstants.STATUS_AS_PENDING_FOR_APPROVAL) && isSequential) {
						sendNotification(approver);
					}
					//previousRule=approver.getRuleId();
				}
			}
		}catch(Exception e) {
			logger.error("Error in moveToNextApproverOrRule",e);
		}
	}
	private void sendNotification(WorkflowSettingsUserApprovalVo workflowSettingsUserApprovalVo)
			throws ApplicationException {
		logger.info("**Sennding Notification**"+workflowSettingsUserApprovalVo);
		UserVo user = userDao.getUserDetails(workflowSettingsUserApprovalVo.getOrganizationId(),
				workflowSettingsUserApprovalVo.getUserId());

		if (workflowSettingsUserApprovalVo != null && workflowSettingsUserApprovalVo.getRuleId()>0 ) {
			WorkflowSettingsVo rule=workflowSettingsDao.getWorkflowSettingsById(workflowSettingsUserApprovalVo.getRuleId());
			if(rule!=null) {
				WorkflowSettingsRuleData ruleData=rule.getData();
				if(ruleData!=null) {
					if(ruleData.isEmail()) {
						WorkflowUserApprovalDetailsVo approvalDetails = workflowSettingsUserApprovalDao
								.getApprovalDetails(workflowSettingsUserApprovalVo);
						String url;
						FileReader reader;
						try {
							reader = new FileReader("/decifer/config/app_config.properties");
							Properties p = new Properties();
							p.load(reader);
							url = p.getProperty("decifer_url");
						} catch (Exception e) {
							url = "https://decifer.blackstrawlab.com";
						}
						String message = "Hi " + approvalDetails.getRoleName()
						+ ",\r\n\r\n  You have been added as one of the approvers for " + approvalDetails.getModuleName()
						+ "-" + approvalDetails.getReference() + ". Please login to " + url + " to view and approve."
						+ "\r\n\r\n Regards,\r\n Decifer Admin";
						if(user!=null) {
							EmailThread emailThread = new EmailThread(message, user.getEmailId(), "Decifer:Pending for Approval");
							emailThread.start();
						}

					}
					if (ruleData.isSms()) {

					}
					if (ruleData.isWhatsApp()) {

					}
					if (ruleData.isInApp()) {
					}
				}
			}
		}
	}
	
	public List<CommonVo> getRejectionTypesDropDown() throws ApplicationException {
		return dropDownDao.getRejectionTypesDropDown();
	}
	
}

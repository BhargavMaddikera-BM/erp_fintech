package com.blackstrawai.workflow;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.DateConverter;
import com.blackstrawai.ap.BillsInvoiceDao;
import com.blackstrawai.ar.ArInvoiceDao;
import com.blackstrawai.ar.CreditNotesDao;
import com.blackstrawai.common.BaseDao;
import com.blackstrawai.common.CommonConstants;
import com.blackstrawai.common.FinanceCommonDao;
import com.blackstrawai.externalintegration.banking.yesbank.YesBankIntegrationDao;
import com.blackstrawai.externalintegration.yesbank.WorkflowBankingVo;
import com.blackstrawai.onboarding.OrganizationDao;
import com.blackstrawai.settings.CommonVo;
import com.blackstrawai.upload.ModuleTypeVo;


@Repository
public class WorkflowSettingsUserApprovalDao extends BaseDao {

	private Logger logger = Logger.getLogger(WorkflowSettingsUserApprovalDao.class);

	@Autowired
	private FinanceCommonDao financeCommonDao;
	@Autowired
	private OrganizationDao organizationDao;
	@Autowired
	private WorkflowSettingsDao workflowSettingsDao;

	@Autowired
	private BillsInvoiceDao billsInvoiceDao;

	@Autowired
	private ArInvoiceDao arInvoiceDao;

	@Autowired
	private CreditNotesDao creditNotesDao;

	@Autowired
	private YesBankIntegrationDao yesBankIntegrationDao;
	
	public WorkflowSettingsUserApprovalVo createWorkflowSettingsApprovalUser(WorkflowSettingsUserApprovalVo workflowSettingsUserApprovalVo) throws ApplicationException {
		logger.info("Entry into method: createWorkflowSettings");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		try {

			con = getUserMgmConnection();
			String sql = WorkflowSettingsUserApprovalConstants.INSERT_INTO_WORKFLOW_SETTINGS_USER_APPROVAL;
			preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setInt(1,workflowSettingsUserApprovalVo.getRuleId());
			preparedStatement.setInt(2,workflowSettingsUserApprovalVo.getPriority());
			preparedStatement.setInt(3,workflowSettingsUserApprovalVo.getUserId());
			preparedStatement.setString(4, workflowSettingsUserApprovalVo.getRoleName());
			preparedStatement.setString(5, workflowSettingsUserApprovalVo.getStatus());
			preparedStatement.setInt(6, workflowSettingsUserApprovalVo.getModuleId());
			preparedStatement.setInt(7, workflowSettingsUserApprovalVo.getModuleTypeId());
			preparedStatement.setInt(8, workflowSettingsUserApprovalVo.getOrganizationId());
			preparedStatement.setInt(9, workflowSettingsUserApprovalVo.getUserId());
			preparedStatement.setString(10, workflowSettingsUserApprovalVo.getRoleName());

			int rowAffected = preparedStatement.executeUpdate();
			if (rowAffected == 1) {
				rs = preparedStatement.getGeneratedKeys();
				if (rs.next()) {
					workflowSettingsUserApprovalVo.setId(rs.getInt(1));
				}
			}
		} catch (Exception e) {
			logger.error("Error in createWorkflowSettings",e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return workflowSettingsUserApprovalVo;
	}

	public WorkflowSettingsUserApprovalVo updateWorkflowSettingsApprovalUser(WorkflowSettingsUserApprovalVo workflowSettingsUserApprovalVo) throws ApplicationException {
		logger.info("Entry into method: updateWorkflowSettingsApprovalUser");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		try {

			con = getUserMgmConnection();
			String sql = WorkflowSettingsUserApprovalConstants.UPDATE_WORKFLOW_SETTINGS_USER_APPROVAL;
			preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setInt(1,workflowSettingsUserApprovalVo.getRuleId());
			preparedStatement.setInt(2,workflowSettingsUserApprovalVo.getPriority());
			preparedStatement.setInt(3,workflowSettingsUserApprovalVo.getUserId());
			preparedStatement.setString(4, workflowSettingsUserApprovalVo.getRoleName());
			preparedStatement.setString(5, workflowSettingsUserApprovalVo.getStatus());
			preparedStatement.setInt(6, workflowSettingsUserApprovalVo.getModuleId());
			preparedStatement.setInt(7, workflowSettingsUserApprovalVo.getModuleTypeId());
			preparedStatement.setInt(8, workflowSettingsUserApprovalVo.getOrganizationId());
			preparedStatement.setInt(9, workflowSettingsUserApprovalVo.getUserId());
			preparedStatement.setString(10, workflowSettingsUserApprovalVo.getRoleName());
			preparedStatement.setInt(11, workflowSettingsUserApprovalVo.getId());
			int rowAffected = preparedStatement.executeUpdate();
			if (rowAffected == 1) {
				rs = preparedStatement.getGeneratedKeys();
				if (rs.next()) {
					workflowSettingsUserApprovalVo.setId(rs.getInt(1));
				}
			}
		} catch (Exception e) {
			logger.error("Error in updateWorkflowSettingsApprovalUser",e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return workflowSettingsUserApprovalVo;
	}

	public WorkflowSettingsUserApprovalVo rejectWorkflow(WorkflowSettingsUserApprovalVo workflowSettingsUserApprovalVo) throws ApplicationException {
		logger.info("Entry into method: updateWorkflowSettingsApprovalUser");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		try {

			con = getUserMgmConnection();
			String sql = WorkflowSettingsUserApprovalConstants.UPDATE_WORKFLOW_SETTINGS_USER_APPROVAL;
			preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setInt(1,workflowSettingsUserApprovalVo.getRuleId());
			preparedStatement.setInt(2,workflowSettingsUserApprovalVo.getPriority());
			preparedStatement.setInt(3,workflowSettingsUserApprovalVo.getUserId());
			preparedStatement.setString(4, workflowSettingsUserApprovalVo.getRoleName());
			preparedStatement.setString(5, workflowSettingsUserApprovalVo.getStatus());
			preparedStatement.setInt(6, workflowSettingsUserApprovalVo.getModuleId());
			preparedStatement.setInt(7, workflowSettingsUserApprovalVo.getModuleTypeId());
			preparedStatement.setInt(8, workflowSettingsUserApprovalVo.getOrganizationId());
			preparedStatement.setInt(9, workflowSettingsUserApprovalVo.getUserId());
			preparedStatement.setString(10, workflowSettingsUserApprovalVo.getRoleName());
			preparedStatement.setInt(11, workflowSettingsUserApprovalVo.getId());
			int rowAffected = preparedStatement.executeUpdate();
			if (rowAffected == 1) {
				rs = preparedStatement.getGeneratedKeys();
				if (rs.next()) {
					workflowSettingsUserApprovalVo.setId(rs.getInt(1));
				}
			}
		} catch (Exception e) {
			logger.error("Error in updateWorkflowSettingsApprovalUser",e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return workflowSettingsUserApprovalVo;
	}


	public int getCurrentRuleForModuleEntity(int moduleTypeId) throws ApplicationException {
		logger.info("Entry into method: createWorkflowSettings");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		int currentRuleId=0;
		try {

			con = getUserMgmConnection();
			preparedStatement = con.prepareStatement(WorkflowSettingsUserApprovalConstants.GET_CURRENT_RULE_FROM_USER_APPROVAL);
			preparedStatement.setInt(1,moduleTypeId);
			preparedStatement.setString(2, CommonConstants.STATUS_AS_PENDING_FOR_APPROVAL);
			rs=preparedStatement.executeQuery();
			if (rs.next()) {
				currentRuleId=rs.getInt(1);
			}
		} catch (Exception e) {
			logger.error("Error in createWorkflowSettings",e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return currentRuleId;
	}

	public boolean deleteOrRejectRules(String module,int moduleTypeId,boolean isDelete,String userId,String roleName,String rejectionRemarks,int rejectionTypeId) throws ApplicationException {
		logger.info("Entry into deleteOrRejectRules");
		String query=WorkflowSettingsUserApprovalConstants.DELETE_RULES_FOR_MODULE_ENTITY;
		// Get module Details
		ModuleTypeVo moduleTypeVo = financeCommonDao.getWorkflowModuleByName(module);
		if(moduleTypeVo!=null) {
			Connection con =null;
			PreparedStatement preparedStatement =null;
			try  {
				con = getUserMgmConnection();
				preparedStatement = con.prepareStatement(query);
				String status=CommonConstants.STATUS_AS_REJECT;
				if(isDelete) {
					status=CommonConstants.STATUS_AS_DELETE;
				}
				preparedStatement.setString(1, status);
				preparedStatement.setString(2, userId);
				preparedStatement.setString(3, roleName);
				preparedStatement.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
				preparedStatement.setString(5, rejectionRemarks);
				preparedStatement.setInt(6, rejectionTypeId>0?rejectionTypeId:0);
				preparedStatement.setInt(7, moduleTypeVo.getId());
				preparedStatement.setInt(8, moduleTypeId);
				preparedStatement.setString(9, CommonConstants.STATUS_AS_PENDING_FOR_APPROVAL);
				preparedStatement.setString(10, "NA");
				preparedStatement.executeUpdate();
				return true;
			} catch (Exception e) {
				logger.info("Error in deleteOrRejectRules ", e);
				throw new ApplicationException(e);
			} finally {
				closeResources(null, preparedStatement, con);
			}
		}
		return false;
	}

	public boolean approveRule(int approvalId,int userId,String roleName) throws ApplicationException {
		logger.info("Entry into approveRule");
		String query=WorkflowSettingsUserApprovalConstants.APPROVE_RULE_FOR_MODULE_ENTITY;
		if(approvalId>0) {
			Connection con =null;
			PreparedStatement preparedStatement =null;
			try  {
				con = getUserMgmConnection();
				preparedStatement = con.prepareStatement(query);
				preparedStatement.setString(1, CommonConstants.STATUS_AS_APPROVED);
				preparedStatement.setInt(2, userId);
				preparedStatement.setString(3, roleName);
				preparedStatement.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
				preparedStatement.setInt(5, approvalId);
				preparedStatement.executeUpdate();
				return true;
			} catch (Exception e) {
				logger.info("Error in deletePreviousRules ", e);
				throw new ApplicationException(e);
			} finally {
				closeResources(null, preparedStatement, con);
			}
		}
		return false;
	}

	public boolean anyApprovalsPendingForRule(int organizationId,int moduleId,int moduleTypeId,int ruleId) throws ApplicationException {
		logger.info("Entry into anyApprovalsPendingForRule");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;

		if(moduleId>0 && moduleTypeId>0 && ruleId>0) {
			try {
				con = getUserMgmConnection();
				preparedStatement = con
						.prepareStatement(WorkflowSettingsUserApprovalConstants.ANY_APPROVALS_PENDING_FOR_RULE) ;
				preparedStatement.setInt(1, organizationId);
				preparedStatement.setInt(2, moduleId);
				preparedStatement.setInt(3, moduleTypeId);
				preparedStatement.setInt(4, ruleId);
				preparedStatement.setString(5, "NA");
				preparedStatement.setString(6, CommonConstants.STATUS_AS_PENDING_FOR_APPROVAL);
				logger.info("Query:"+preparedStatement.toString());
				rs=preparedStatement.executeQuery();
				if(rs.next()) {
					if(rs.getInt(1)>0) {
						return true;
					}
				}
			} catch (Exception e) {
				logger.info("Error in anyApprovalsPendingForRule ", e);
				throw new ApplicationException(e);
			}finally {
				closeResources(rs, preparedStatement, con);
			}
		}
		return false;
	}

	public boolean anyApprovalsPendingForModuleTypeId(int organizationId,int moduleId,int moduleTypeId) throws ApplicationException {
		logger.info("Entry into anyApprovalsPendingForModuleTypeId");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;

		if(moduleId>0 && moduleTypeId>0 ) {
			try {
				con = getUserMgmConnection();
				preparedStatement = con
						.prepareStatement(WorkflowSettingsUserApprovalConstants.ANY_APPROVALS_PENDING_FOR_MODULE_TYPE_ID) ;
				preparedStatement.setInt(1, organizationId);
				preparedStatement.setInt(2, moduleId);
				preparedStatement.setInt(3, moduleTypeId);
				preparedStatement.setString(4, "NA");
				preparedStatement.setString(5, CommonConstants.STATUS_AS_PENDING_FOR_APPROVAL);
				logger.info("Query:"+preparedStatement.toString());
				rs=preparedStatement.executeQuery();
				if(rs.next()) {
					if(rs.getInt(1)>0) {
						return true;
					}
				}
			} catch (Exception e) {
				logger.info("Error in anyApprovalsPendingForModuleTypeId ", e);
				throw new ApplicationException(e);
			}finally {
				closeResources(rs, preparedStatement, con);
			}
		}
		return false;
	}

	public WorkflowSettingsUserApprovalVo getApprovalById(int approvalId) throws ApplicationException {
		logger.info("Entry into getApprovalById");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		WorkflowSettingsUserApprovalVo approvalVo=new WorkflowSettingsUserApprovalVo();
		if(approvalId>0) {
			try {
				con = getUserMgmConnection();
				preparedStatement = con
						.prepareStatement(WorkflowSettingsUserApprovalConstants.GET_APPROVAL_BY_ID) ;
				preparedStatement.setInt(1, approvalId);
				rs=preparedStatement.executeQuery();
				if(rs.next()) {
					approvalVo.setId(rs.getInt(1));
					approvalVo.setRuleId(rs.getInt(2));
					approvalVo.setPriority(rs.getInt(3));
					approvalVo.setUserId(rs.getInt(4));
					approvalVo.setRoleName(rs.getString(5));
					approvalVo.setStatus(rs.getString(6));
					approvalVo.setModuleId(rs.getInt(7));
					approvalVo.setModuleTypeId(rs.getInt(8));
					approvalVo.setOrganizationId(rs.getInt(9));
				}
				logger.info("Successfully Feteched Approval Object:"+approvalVo);
			} catch (Exception e) {
				logger.info("Error in getApprovalById ", e);
				throw new ApplicationException(e);
			}finally {
				closeResources(rs, preparedStatement, con);
			}
		}

		return approvalVo;
	}

	public List<WorkflowSettingsUserApprovalVo> getOtherPendingApprovalsByRule(int moduleId,int moduleTypeId,int ruleId,int exclusionApprovalId) throws ApplicationException {
		logger.info("Entry into getOtherPendingApprovalsByRule moduleId:"+moduleId+",moduleTypeId:"+moduleTypeId+",ruleId:"+ruleId);
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		List<WorkflowSettingsUserApprovalVo> approvalObjects=new ArrayList<WorkflowSettingsUserApprovalVo>();
		if(ruleId>0) {
			try {
				con = getUserMgmConnection();
				preparedStatement = con
						.prepareStatement(WorkflowSettingsUserApprovalConstants.GET_PENDING_APPROVERS_BY_RULE) ;

				preparedStatement.setInt(1, moduleId);
				preparedStatement.setInt(2, moduleTypeId);
				preparedStatement.setInt(3, ruleId);
				preparedStatement.setInt(4, exclusionApprovalId);
				preparedStatement.setString(5, "NA");
				preparedStatement.setString(6, CommonConstants.STATUS_AS_PENDING_FOR_APPROVAL);
				logger.info("Query:"+preparedStatement.toString());
				rs=preparedStatement.executeQuery();
				while(rs.next()) {
					logger.info("getOtherPendingApprovalsByRule: Retrieved:"+rs.getInt(1));
					WorkflowSettingsUserApprovalVo approvalVo=new WorkflowSettingsUserApprovalVo();
					approvalVo.setId(rs.getInt(1));
					approvalVo.setRuleId(rs.getInt(2));
					approvalVo.setPriority(rs.getInt(3));
					approvalVo.setUserId(rs.getInt(4));
					approvalVo.setRoleName(rs.getString(5));
					approvalVo.setStatus(rs.getString(6));
					approvalVo.setModuleId(rs.getInt(7));
					approvalVo.setModuleTypeId(rs.getInt(8));
					approvalVo.setOrganizationId(rs.getInt(9));
					approvalObjects.add(approvalVo);
				}
				logger.info("getOtherPendingApprovalsByRule: Successfully Feteched Approval Objects:"+approvalObjects);
			} catch (Exception e) {
				logger.info("Error in getOtherPendingApprovalsByRule ", e);
				throw new ApplicationException(e);
			}finally {
				closeResources(rs, preparedStatement, con);
			}
		}

		return approvalObjects;
	}

	public List<WorkflowSettingsUserApprovalVo> getNextApprovalRulesForModuleEntity(int moduleId,int moduleTypeId,int currentRuleId) throws ApplicationException {
		logger.info("Entry into getNextApprovalRulesForModuleEntity moduleId:"+moduleId+",moduleTypeId:"+moduleTypeId+",currentRuleId:"+currentRuleId);
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		List<WorkflowSettingsUserApprovalVo> approvalObjects=new ArrayList<WorkflowSettingsUserApprovalVo>();
		if(moduleId>0) {
			try {
				con = getUserMgmConnection();
				preparedStatement = con
						.prepareStatement(WorkflowSettingsUserApprovalConstants.GET_NEXT_APPROVERS_RULES_BY_MODULE_ENTITY) ;
				preparedStatement.setInt(1, moduleId);
				preparedStatement.setInt(2, moduleTypeId);
				preparedStatement.setInt(3, currentRuleId);//Exclude Current Rule
				preparedStatement.setString(4, "NA");
				rs=preparedStatement.executeQuery();
				while(rs.next()) {
					WorkflowSettingsUserApprovalVo approvalVo=new WorkflowSettingsUserApprovalVo();
					approvalVo.setId(rs.getInt(1));
					approvalVo.setRuleId(rs.getInt(2));
					approvalVo.setPriority(rs.getInt(3));
					approvalVo.setUserId(rs.getInt(4));
					approvalVo.setRoleName(rs.getString(5));
					approvalVo.setStatus(rs.getString(6));
					approvalVo.setModuleId(rs.getInt(7));
					approvalVo.setModuleTypeId(rs.getInt(8));
					approvalVo.setOrganizationId(rs.getInt(9));
					approvalObjects.add(approvalVo);
				}
				logger.info("getNextApprovalRulesForModuleEntity: Successfully Feteched Approval Objects:"+approvalObjects);
			} catch (Exception e) {
				logger.info("Error in getNextApprovalRulesForModuleEntity ", e);
				throw new ApplicationException(e);
			}finally {
				closeResources(rs, preparedStatement, con);
			}
		}

		return approvalObjects;
	}

	public String getApprovalRuleType(int approvalId) throws ApplicationException {
		WorkflowSettingsUserApprovalVo approvalVo=getApprovalById(approvalId);
		WorkflowSettingsVo ruleVo=workflowSettingsDao.getWorkflowSettingsById(approvalVo.getRuleId());
		WorkflowSettingsRuleData ruleData=ruleVo.getData();
		CommonVo ruleApprovalType=financeCommonDao.getWorkflowApprovalTypeById(ruleData.getApprovalTypeId());
		return ruleApprovalType.getName();
	}

	public WorkflowUserApprovalDetailsVo getApprovalDetails(WorkflowSettingsUserApprovalVo workflowSettingsUserApprovalVo) throws ApplicationException{
		logger.info("Entry into getApprovalDetailsById ApprovalId:"+workflowSettingsUserApprovalVo);
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		WorkflowUserApprovalDetailsVo workflowUserApprovalDetailsVo=new WorkflowUserApprovalDetailsVo();
		if(workflowSettingsUserApprovalVo!=null && workflowSettingsUserApprovalVo.getId()>0) {
			try {
				con = getUserMgmConnection();
				preparedStatement = con
						.prepareStatement(WorkflowSettingsUserApprovalConstants.GET_MINIMAL_APPROVAL_DETAILS) ;
				logger.info("Query:"+preparedStatement.toString());
				preparedStatement.setInt(1, workflowSettingsUserApprovalVo.getId());
				rs=preparedStatement.executeQuery();
				while(rs.next()) {
					workflowUserApprovalDetailsVo.setModuleName(rs.getString(1));
					workflowUserApprovalDetailsVo.setOrganizationName(rs.getString(2));
					workflowUserApprovalDetailsVo.setRoleName(workflowSettingsUserApprovalVo.getRoleName());
					workflowUserApprovalDetailsVo.setModuleId(workflowSettingsUserApprovalVo.getModuleId());
					workflowUserApprovalDetailsVo=setModuleDetails(workflowSettingsUserApprovalVo.getModuleId(), workflowSettingsUserApprovalVo.getModuleTypeId(),workflowUserApprovalDetailsVo);

				}
				logger.info("getApprovalDetailsById: Successfully Feteched Approval Objects:"+workflowUserApprovalDetailsVo);
			} catch (Exception e) {
				logger.info("Error in getApprovalDetailsById ", e);
				throw new ApplicationException(e);
			}finally {
				closeResources(rs, preparedStatement, con);
			}
		}

		return workflowUserApprovalDetailsVo; 	
	}

	private WorkflowUserApprovalDetailsVo setModuleDetails(int moduleId,int moduleTypeId,WorkflowUserApprovalDetailsVo workflowUserApprovalDetailsVo) throws ApplicationException {
		ModuleTypeVo commonVo=financeCommonDao.getWorkflowModuleById(moduleId);
		switch(commonVo.getName()) {
		case WorkflowConstants.MODULE_ACCOUNTING_ENTRIES:
			break;
		case WorkflowConstants.MODULE_AP_INVOICE:
			WorkflowInvoiceVo invoiceVo=billsInvoiceDao.getWorkflowRequiredDataForInvoiceById(moduleTypeId);
			if(invoiceVo!=null) {
				workflowUserApprovalDetailsVo.setValue(invoiceVo.getCurrencySymbol()+" "+invoiceVo.getInvoiceValue());
				workflowUserApprovalDetailsVo.setReference(invoiceVo.getInvoiceNumber()!=null?invoiceVo.getInvoiceNumber():(moduleTypeId+""));
				workflowUserApprovalDetailsVo.setModuleName(commonVo.getName());
				String componentName="Invoice-Without Bills";
				if(invoiceVo.isInvoiceWithBills()) {
					componentName="Invoice-WithBills";
				}
				workflowUserApprovalDetailsVo.setComponentName(componentName);
			}
			break;
		case WorkflowConstants.MODULE_AR_APPLICATION_OF_FUNDS:
			break;
		case WorkflowConstants.MODULE_AR_CREDITNOTE:
			WorkflowInvoiceVo creditNoteVo=creditNotesDao.getWorkflowRequiredDataForCreditNotebyId(moduleTypeId);
			if(creditNoteVo!=null) {
				workflowUserApprovalDetailsVo.setValue(creditNoteVo.getCurrencySymbol()+" "+creditNoteVo.getInvoiceValue());
				workflowUserApprovalDetailsVo.setReference(creditNoteVo.getInvoiceNumber());
				workflowUserApprovalDetailsVo.setModuleName(commonVo.getName());
				workflowUserApprovalDetailsVo.setComponentName("AR-Credit Note");
			}

			break;
		case WorkflowConstants.MODULE_AR_INVOICE:
			WorkflowInvoiceVo arInvoiceVo=arInvoiceDao.getWorkflowRequiredDataForInvoiceById(moduleTypeId);
			if(arInvoiceVo!=null) {
				workflowUserApprovalDetailsVo.setValue(arInvoiceVo.getCurrencySymbol()+" "+arInvoiceVo.getInvoiceValue());
				workflowUserApprovalDetailsVo.setReference(arInvoiceVo.getInvoiceNumber());
				workflowUserApprovalDetailsVo.setModuleName(commonVo.getName());
				workflowUserApprovalDetailsVo.setComponentName("AR-Invoice");
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
			WorkflowBankingVo workflowBankingVo=yesBankIntegrationDao.getWorfklowDataForPayment(moduleTypeId);
			if(workflowBankingVo!=null) {
				workflowUserApprovalDetailsVo.setValue("INR "+workflowBankingVo.getAmount());
				workflowUserApprovalDetailsVo.setReference(workflowBankingVo.getPaymentTransferPaymentsVo() != null
						&& workflowBankingVo.getPaymentTransferPaymentsVo().getData() != null
						&& workflowBankingVo.getPaymentTransferPaymentsVo().getData().getInitiation() != null
								? workflowBankingVo.getPaymentTransferPaymentsVo().getData().getInitiation()
										.getInstructionIdentification()
								: "");
				workflowUserApprovalDetailsVo.setModuleName(commonVo.getName());
				workflowUserApprovalDetailsVo.setComponentName(workflowBankingVo.getPaymentType());
			}

			break;

		default:
			break;

		}
		return workflowUserApprovalDetailsVo;
	}

	public List<WorkflowUserApprovalDetailsVo> getPendingApprovalsByOrganization(int organizationId, int userId,
			String roleName) throws ApplicationException {
		logger.info("Entry into getPendingApprovalsByOrganization organizationId:" + organizationId);
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		List<WorkflowUserApprovalDetailsVo> workflowUserApprovalDetailsList = new ArrayList<WorkflowUserApprovalDetailsVo>();
		if (organizationId > 0 && userId > 0) {
			try {
				con = getUserMgmConnection();
				String query = "";
				//				if (roleName.equals("Super Admin")) {
				//					query = WorkflowSettingsUserApprovalConstants.GET_PENDING_APPROVALS_BY_ORGANIZATION;
				//				} else {
				query = WorkflowSettingsUserApprovalConstants.GET_PENDING_APPROVALS_BY_USER_ROLE;
				//}
				preparedStatement = con.prepareStatement(query);
				preparedStatement.setInt(1, organizationId);
				//if (!(roleName.equals("Super Admin"))) {
				preparedStatement.setInt(2, userId);
				preparedStatement.setString(3, roleName);
				//}
				logger.info("Query:" + preparedStatement.toString());
				rs = preparedStatement.executeQuery();
				while (rs.next()) {
					//	id,workflow_settings_id,priority,approver_user_id,approver_role_name,status,module_id,module_type_id,create_ts
					WorkflowUserApprovalDetailsVo workflowUserApprovalDetailsVo = new WorkflowUserApprovalDetailsVo();
					workflowUserApprovalDetailsVo.setApprovalId(rs.getInt(1));
					workflowUserApprovalDetailsVo.setRoleName(rs.getString(5));
					String status=rs.getString(6);
					if(status!=null) {
						switch (status) {
						case CommonConstants.STATUS_AS_PENDING_FOR_APPROVAL:
							status=CommonConstants.DISPLAY_STATUS_AS_PENDING_FOR_APPROVAL;
							break;
						case CommonConstants.STATUS_AS_APPROVED:
							status=CommonConstants.DISPLAY_STATUS_AS_APPROVED;
							break;

						case CommonConstants.STATUS_AS_REJECT:
							status=CommonConstants.DISPLAY_STATUS_AS_REJECT;
							break;
						case CommonConstants.STATUS_AS_DECLINE:
							status=CommonConstants.DISPLAY_STATUS_AS_DECLINE;
							break;
						}
					workflowUserApprovalDetailsVo.setStatus(status);
					}

					Connection con1=getUserMgmConnection();
					String dateFormat=organizationDao.getDefaultDateForOrganization(organizationId,con1);
					closeResources(null, null, con1);
					workflowUserApprovalDetailsVo.setDate(rs.getDate(9)!=null?DateConverter.getInstance().convertDateToGivenFormat(rs.getDate(9), dateFormat):null);
					workflowUserApprovalDetailsVo.setModuleId(rs.getInt(8));
					workflowUserApprovalDetailsVo=setModuleDetails(rs.getInt(7),rs.getInt(8),workflowUserApprovalDetailsVo);
										workflowUserApprovalDetailsList.add(workflowUserApprovalDetailsVo);
					workflowUserApprovalDetailsVo.setRejectionTypeId(rs.getInt(10));
				}
				logger.info("getPendingApprovalsByOrganization: Successfully Feteched Approval Objects:"
						+ workflowUserApprovalDetailsList);
			} catch (Exception e) {
				logger.info("Error in getPendingApprovalsByOrganization ", e);
				throw new ApplicationException(e);
			} finally {
				closeResources(rs, preparedStatement, con);
			}
		}
		return workflowUserApprovalDetailsList;
	}
}
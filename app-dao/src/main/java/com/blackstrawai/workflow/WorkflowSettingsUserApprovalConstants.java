package com.blackstrawai.workflow;

public class WorkflowSettingsUserApprovalConstants {
	//Workflow Approvals
	public static final String INSERT_INTO_WORKFLOW_SETTINGS_USER_APPROVAL="insert into workflow_settings_user_approval_organization(workflow_settings_id,priority,approver_user_id,approver_role_name,status,module_id,module_type_id,organization_id,user_id,role_name) values(?,?,?,?,?,?,?,?,?,?)";
	public static final String UPDATE_WORKFLOW_SETTINGS_USER_APPROVAL="update workflow_settings_user_approval_organization set workflow_settings_id=?,priority=?,approver_user_id=?,approver_role_name=?,status=?,module_id=?,module_type_id=?,organization_id=?,update_user_id=?,update_role_name=? where id=?";
	public static final String GET_CURRENT_RULE_FROM_USER_APPROVAL="SELECT workflow_settings_id FROM workflow_settings_user_approval_organization WHERE module_type_id=? AND STATUS=?";
	public static final String GET_APPROVAL_BY_ID="SELECT id,workflow_settings_id,priority,approver_user_id,approver_role_name,status,module_id,module_type_id,organization_id FROM workflow_settings_user_approval_organization WHERE id=?";
	public static final String GET_PENDING_APPROVERS_BY_RULE="SELECT id,workflow_settings_id,priority,approver_user_id,approver_role_name,status,module_id,module_type_id,organization_id FROM workflow_settings_user_approval_organization WHERE module_id=? AND module_type_id=? AND workflow_settings_id=? and id not in(?) and status in (?,?) ORDER BY priority asc";
	public static final String GET_NEXT_APPROVERS_RULES_BY_MODULE_ENTITY="SELECT id,workflow_settings_id,priority,approver_user_id,approver_role_name,status,module_id,module_type_id,organization_id FROM workflow_settings_user_approval_organization WHERE module_id=? AND module_type_id=? AND workflow_settings_id not in(?) AND STATUS=? ORDER BY priority asc";
	public static final String GET_PENDING_APPROVALS_BY_ORGANIZATION="SELECT id,workflow_settings_id,priority,approver_user_id,approver_role_name,status,module_id,module_type_id,create_ts FROM workflow_settings_user_approval_organization WHERE organization_id=? AND STATUS=? ORDER BY create_ts desc";
	public static final String GET_PENDING_APPROVALS_BY_USER_ROLE="SELECT id,workflow_settings_id,priority,approver_user_id,approver_role_name,status,module_id,module_type_id,create_ts,rejection_type_id FROM workflow_settings_user_approval_organization WHERE organization_id=? AND STATUS not IN ('DEL') and approver_user_id=? and approver_role_name=? ORDER BY create_ts desc";
	public static final String GET_MINIMAL_APPROVAL_DETAILS="SELECT (SELECT NAME FROM finance_common.workflow_module WHERE id=module_id) AS module_name," + 
			"(SELECT NAME FROM usermgmt.organization WHERE id=organization_id) AS org_name,module_type_id" + 
			" FROM workflow_settings_user_approval_organization WHERE id=?";
	//Deleting Rules
	public static final String DELETE_RULES_FOR_MODULE_ENTITY="UPDATE workflow_settings_user_approval_organization SET STATUS=?,update_user_id=?,update_role_name=?,update_ts=?,rejection_remarks=?,rejection_type_id=? WHERE module_id=? AND module_type_id=? and status in(?,?)";
	//Approve Rule
	public static final String APPROVE_RULE_FOR_MODULE_ENTITY="UPDATE workflow_settings_user_approval_organization SET STATUS=?,update_user_id=?,update_role_name=?,update_ts=? WHERE id=?";
	//Check is any approvals Pending For Rule
	public static final String ANY_APPROVALS_PENDING_FOR_RULE="SELECT COUNT(*) FROM workflow_settings_user_approval_organization WHERE organization_id=? AND module_id=? AND module_type_id=? AND workflow_settings_id=? AND STATUS in(?,?)";
	//Check is any approvals Pending For Module Type Id
	public static final String ANY_APPROVALS_PENDING_FOR_MODULE_TYPE_ID="SELECT COUNT(*) FROM workflow_settings_user_approval_organization WHERE organization_id=? AND module_id=? AND module_type_id=? AND STATUS in(?,?)";

}

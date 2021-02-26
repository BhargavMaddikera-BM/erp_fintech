package com.blackstrawai.workflow;

public class WorkflowSettingsConstants {
	public static final String GET_WORKFLOW_SETTINGS_BY_ORGANIZATION = "select id,module_id,name,priority,data,is_base,description,status,organization_id from workflow_settings_organization where organization_id=? and module_id=? and status in (?,?) ORDER BY create_ts DESC";
	public static final String GET_ACTIVE_WORKFLOW_SETTINGS_BY_ORGANIZATION = "select id,module_id,name,priority,data,is_base,description,status,organization_id from workflow_settings_organization where organization_id=? and module_id=? and status in (?) ORDER BY create_ts DESC";
	public static final String GET_WORKFLOW_SETTINGS_BY_ID = "select id,module_id,name,priority,data,is_base,description,status,organization_id from workflow_settings_organization where id=?";
	public static final String INSERT_INTO_WORKFLOW_SETTINGS = "insert into workflow_settings_organization(module_id,name,priority,data,is_base,description,organization_id,status,user_id,role_name) values(?,?,?,?,?,?,?,?,?,?)";
	public static final String UPDATE_WORKFLOW_SETTINGS = "update workflow_settings_organization set module_id=?,name=?,priority=?,data=?,is_base=?,description=?,organization_id=?,status=?,user_id=?,update_ts=?,role_name=? where id=?";
	public static final String UPDATE_WORKFLOW_SETTING_PRIORITY = "update workflow_settings_organization set priority=? where id=?";
	public static final String CHECK_WORKFLOW_SETTINGS_FOR_ORGANIZATION = "select id from workflow_settings_organization where organization_id=? and module_id=? and name=? and status not in('DEL')"; 
	public static final String CHECK_WORKFLOW_DEFAULT_RULE_EXIST_FOR_MODULE = "select id from workflow_settings_organization where organization_id=? and module_id=? and status=? and is_base=?";
	public static final String CHANGE_WORKFLOW_SETTINGS_STATUS="update workflow_settings_organization set status=?,update_ts=? where id=?";
	//TO include Banking module in workflow
	public static final String GET_UNIQUE_SETTINGS_ORG="SELECT distinct(organization_id),user_id,role_name FROM settings_module_organization";
	public static final String INSERT_BANKING_MODULE_SETTINGS_ORG="INSERT INTO settings_module_organization ( type, sub_module_name, is_required, organization_id, user_id, role_name) VALUES ( 'Workflow', 'Banking', true, ?, ?, ?)";
	public static final String INSERT_BANKING_MODULE_RULE_ORG="INSERT INTO workflow_settings_organization ( module_id, name, priority, data, is_base, description, organization_id, user_id,role_name) VALUES ( (select id from finance_common.workflow_module wm where wm.name='Banking'), 'System Default:Banking', '1', '{\"is\": [], \"sms\": false, \"email\": true, \"equal\": \"\", \"inApp\": true, \"isNot\": [], \"enabled\": false, \"choiceId\": 0, \"disabled\": false, \"lessThan\": \"\", \"whatsApp\": false, \"rolesList\": [], \"usersList\": [{\"id\": 15, \"name\": \"admin01 R\", \"type\": \"User\"}], \"choiceName\": null, \"greaterThan\": \"\", \"setSequence\": [{\"id\": 15, \"name\": \"admin01 R\", \"type\": \"User\"}], \"approvalTypeId\": 1, \"validationParameterId\": 0}', '1', 'All Transactions Require Approval Without Any Conditions', ?, ?, ?)";
}

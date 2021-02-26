package com.blackstrawai.workflow;

public class WorkflowGeneralSettingsConstants {
	
	public static final String INSERT_INTO_WORKFLOW_GENERAL_SETTINGS = "insert into workflow_general_settings_organization(module_id,data,organization_id,status,user_id ,role_name) values(?,?,?,?,?,?)";
	public static final String UPDATE_WORKFLOW_GENERAL_SETTINGS = "update workflow_general_settings_organization set module_id=?,data=?,organization_id=?,status=?,user_id =?,role_name=? where id=?";
	public static final String GET_WORKFLOW_GENERAL_SETTINGS_BY_ORGANIZATION_MODULE="select id,module_id,data,organization_id,status,user_id ,role_name from workflow_general_settings_organization where organization_id=? and module_id=?";
	
}

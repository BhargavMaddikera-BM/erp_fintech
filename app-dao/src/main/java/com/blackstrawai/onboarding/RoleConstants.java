package com.blackstrawai.onboarding;

public class RoleConstants {


	public static final String INSERT_INTO_ROLE="insert into role_organization(name,description,status,organization_id,access_data,create_user_id,create_role_name)values(?,?,?,?,?,?,?)";
	public static final String UPDATE_ROLE="update role_organization set name=?,description=?,status=?,update_ts=?,access_data=?,update_user_id=?,update_role_name=? where id=? and organization_id=?";
	public static final String DELETE_ROLE="update role_organization set status=?,update_ts=?,update_user_id=?,update_role_name=? where id=?";   
	public static final String GET_ROLES_BY_ORGANIZATION="select id,name,description,status,organization_id,access_data from role_organization where organization_id=? and status not in(?)";
	public static final String GET_ACTIVE_ROLES_BY_ORGANIZATION="select id,name,description,status,organization_id,access_data from role_organization where organization_id=? and status=?";
	public static final String GET_ROLE_IN_ORGANIZATION="select id,name,description,status,organization_id,access_data from role_organization where id=? and organization_id=? and status not in(?)";
	public static final String CHECK_ROLE_EXIST_FOR_ORGANIZATION="select * from role_organization where organization_id=? and name=? and status not in(?)";
	public static final String GET_ROLES_BY_NAME_ORGANIZATION="select id,name,description,status,organization_id,access_data from role_organization where organization_id=? and name=? and status not in(?)";
	public static final String GET_ROLES_BY_ORGANIZATION_USER_ROLE="select id,name,description,status,organization_id,access_data from role_organization where organization_id=? and status not in(?) and create_user_id=? and create_role_name=?";
	public static final String GET_ROLES_ORGANIZATION="select id,name,description,status,organization_id,access_data from role_organization where organization_id=? and status not in(?)";


}

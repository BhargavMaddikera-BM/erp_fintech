package com.blackstrawai.accessandroles;

public class AccessAndRolesConstants {

	public static final String GET_LEVEL1_ACCESS = "select id,name,key_name,has_children,has_access,table_columns from access_mgmt_level1 where access_mgmt_base_id=?";
	public static final String GET_LEVEL2_ACCESS = "select id,name,key_name,has_children,has_access,actions from access_mgmt_level2 where level1_id=?";
	public static final String GET_LEVEL3_ACCESS = "select id,name,key_name,has_children,has_access,actions from access_mgmt_level3 where level2_id=?";
	public static final String GET_LEVEL4_ACCESS = "select id,name,key_name,has_children,has_access,actions from access_mgmt_level4 where level3_id=?";
	public static final String GET_LEVEL5_ACCESS = "select id,name,key_name,has_children,has_access,actions from access_mgmt_level5 where level4_id=?";

}

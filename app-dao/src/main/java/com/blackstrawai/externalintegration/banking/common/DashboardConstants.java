package com.blackstrawai.externalintegration.banking.common;

public class DashboardConstants {


public static final String GET_DEFAULT_ACCOUNT_FOR_ORG = "select account_no , account_type from dashboard_default_account where organization_id = ? and user_id =? and role_name=?";

public static final String CREATE_DEFAULT_ACCOUNT_FOR_ORG = "insert into dashboard_default_account ( account_no, account_type, customer_no, create_ts, user_id,  organization_id, role_name ) values (?,?,?,?,?,?,?)";

public static final String UPDATE_DEFAULT_ACCOUNT_FOR_ORG = "update dashboard_default_account set account_no = ? ,account_type=? , customer_no=? , update_ts=? , update_user_id =? , update_role_name =? where organization_id = ? and user_id=? and  role_name =?";


}

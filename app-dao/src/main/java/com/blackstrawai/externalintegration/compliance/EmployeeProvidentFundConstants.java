package com.blackstrawai.externalintegration.compliance; 

public class EmployeeProvidentFundConstants {

	public static final String SELECT_EPF_USER = "SELECT id, login, password FROM employee_provident_fund.user_info WHERE organization_id=? AND user_id=? AND role_name=? AND login=?";// AND login = ? AND password = ?";
	public static final String CREATE_EPF_USER = "INSERT INTO employee_provident_fund.user_info (login, password, user_id, role_name, organization_id, remember_me) VALUES (?,?,?,?,?,?)";
	public static final String UPDATE_REMEMBER_ME_STATUS = "UPDATE employee_provident_fund.user_info SET remember_me=?, update_ts=?, status=?, password=? WHERE id=?";
	public static final String SELECT_CHALLANS_BY_USER_ID = "SELECT trrn, wage_month, ecr_type, upload_date, status, ac_1, ac_2, "
			+ "ac_10, ac_21, ac_22, total_amount, crn, id FROM employee_provident_fund.recent_challan WHERE user_id = ?";
	public static final String SELECT_ECRS_BY_USER_ID = "SELECT trrn, wage_month, ecr_type, upload_date, "
			+ "status, salary_disb_date, contr_rate, id FROM employee_provident_fund.recent_ecr WHERE user_id = ?";
	public static final String INSERT_RECENT_CHALLAN = "INSERT INTO employee_provident_fund.recent_challan (user_id, trrn, wage_month, ecr_type, upload_date, status, "
			+ "ac_1, ac_2, ac_10, ac_21, ac_22, total_amount, crn) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
	public static final String INSERT_RECENT_ECR = "INSERT INTO employee_provident_fund.recent_ecr (user_id, trrn, wage_month, ecr_type, upload_date, "
			+ "status, salary_disb_date, contr_rate) VALUES (?,?,?,?,?,?,?,?)";
	public static final String SELECT_USERNAME_PASSWORD_BY_ID = "SELECT login, password, organization_id FROM employee_provident_fund.user_info WHERE id = ?";
	public static final String USERNAME_KEY = "username";
	public static final String PASSWORD_KEY = "password";
	public static final String ORG_KEY = "organizationId";
	public static final String SELECT_CHALLAN_BY_TRRN = "SELECT id FROM employee_provident_fund.recent_challan WHERE user_id = ? AND trrn = ?";
	public static final String SELECT_ECR_BY_TRRN = "SELECT id FROM employee_provident_fund.recent_ecr WHERE user_id = ? AND trrn = ?";
	public static final String CHALLAN = "challan";
	public static final String ECR = "ecr";
	public static final String UPDATE_RECENT_CHALLAN = "UPDATE employee_provident_fund.recent_challan SET wage_month=?, ecr_type=?, upload_date=?, status=?, ac_1=?, ac_2=?, "
			+ "ac_10=?, ac_21=?, ac_22=?, total_amount=?, crn=?, update_ts=? WHERE user_id=? AND trrn=?";
	public static final String UPDATE_RECENT_ECR = "UPDATE employee_provident_fund.recent_ecr SET wage_month=?, ecr_type=?, upload_date=?, "
			+ "status=?, salary_disb_date=?, contr_rate=?, update_ts=? WHERE user_id = ? AND trrn=?";
	
/*	public static final String FETCH_LOGIN="select id,password,remember_me from employee_provident_fund.user_info where organization_id=? and login=?";
*/	public static final String INSERT_EPF_ATTACHMENT = "INSERT INTO document_mgmt.document_employee_provident_fund (type, type_id, version, file_name, size, status) VALUES (?,?,?,?,?,?)";
	public static final String GET_EPF_ATTACHMENTS = "SELECT id, type, type_id, file_name, size FROM document_mgmt.document_employee_provident_fund"
			+ " WHERE type=? AND type_id=? AND status=?";
	public static final String UPDATE_EPF_ATTACHMENT_STATUS = "UPDATE document_mgmt.document_employee_provident_fund SET status=? WHERE type_id=? AND type=?";
	public static final String DISCONNECT_USER = "UPDATE employee_provident_fund.user_info SET status=?, remember_me=?,password=null WHERE id=?";
	

	
	
}

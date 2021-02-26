package com.blackstrawai.keycontact;

public class EmployeeConstants {

	public static final String INSERT_INTO_EMPLOYEE = "insert into employee(employee_id,first_name,last_name,mobile_no,reporting_to_id,organization_id,user_id,doj,department_id,employee_type_id,employee_status,email,isSuperAdmin,role_name,status,default_gl_id,default_gl_name)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	public static final String INSERT_INTO_EMPLOYEE_BANK_DETAILS = "insert into employee_bank_details(bank_name,account_no,account_holder_name,branch_name,ifsc_code,upi_id,is_default,employee_id)values(?,?,?,?,?,?,?,?)";
	public static final String CHECK_EMPLOYEE_EXIST_FOR_ORGANIZATION = "select * from employee where organization_id=? and employee_id=?";
	public static final String GET_EMPLOYEE_BY_ORGANIZATION = "select employee_id,first_name,email,department_id,status,id,employee_type_id from employee where organization_id=? order by id desc ";
	public static final String GET_EMPLOYEE_BANK_DETAILS_BY_ID = "select id,bank_name,account_no,account_holder_name,branch_name,ifsc_code,upi_id,is_default,status from employee_bank_details where employee_id=? and status not IN(?)";
	public static final String GET_EMPLOYEE_BY_ID = "select id,employee_id,first_name,last_name,email,doj,mobile_no,department_id,reporting_to_id,employee_status,employee_type_id,status,update_ts,user_id,organization_id,isSuperAdmin,default_gl_id,default_gl_name  from employee where id=?";
	public static final String DELETE_EMPLOYEE = "update employee set status=?,update_ts=?,update_user_id=?,update_role_name=? where id=?";
	public static final String UPDATE_EMPLOYEE = "update employee set employee_id=?,first_name=?,last_name=?,mobile_no=?,reporting_to_id=?,organization_id=?,update_user_id=?,doj=?,department_id=?,employee_type_id=?,employee_status=?,email=?,isSuperAdmin=?,update_ts=?,update_role_name=?,status=?,default_gl_id=?,default_gl_name=? where id=?";
	public static final String UPDATE_EMPLOYEE_BANK_DETAILS = "update employee_bank_details set account_no=?,account_holder_name=?,bank_name=?,branch_name=?,ifsc_code=?,upi_id=?,is_default=?,status=?,update_ts=? where id=?";
	public static final String CHECK_BANK_DETAILS_EXIST_FOR_EMPLOYEE = "select * from employee_bank_details where employee_id=? and account_no=? and ifsc_code=? and status not IN(?)";
	public static final String MODIFY_EMPLOYEE_STATUS = "update employee set status=?,update_ts=? where id=?";
	public static final String MODIFY_EMPLOYEE_CONTACT_STATUS = "update employee_bank_details set status=?, update_ts=? where employee_id=?";
	public static final String GET_DEPARTMENT_ID = "select id from department_organization where organization_id=? and name=?";
	public static final String GET_EMPLOYEE_TYPE_ID = "select id from employee_type where name=?";
	public static final String GET_EMPLOYEE_ID = "select id from employee where organization_id=? and email=?";
	public static final String GET_EMPLOYEE_NAME = "select first_name,last_name from employee where id=? ";
	public static final String GET_EMPLOYEE_DETAILS_BY_ID = "select id,employee_id,first_name,last_name,mobile_no,department_id,employee_status,employee_type_id,status from employee where id=?";
	public static final String GET_EMPLOYEE_BANK_DETAILS_EXPORT_BY_ID = "select account_holder_name,account_no,bank_name,branch_name,ifsc_code,upi_id,is_default from employee_bank_details where employee_id=?";
	public static final String GET_BASIC_EMPLOYEES_FOR_ORG = " select id,first_name,last_name from employee where organization_id= ?  ";
	public static final String GET_EMPLOYEE_BY_ORGANIZATION_USER_ROLE = "select employee_id,first_name,email,department_id,status,id,employee_type_id from employee where organization_id=? and user_id=? and role_name=? order by id desc ";
	public static final String GET_EMPLOYEE_ORGANIZATION = "select employee_id,first_name,email,department_id,status,id,employee_type_id from employee where organization_id=? order by id desc ";
    public static final String DELETE_EMPLOYEE_BANK_DETAILS="update employee_bank_details set status=? where id=?";
	public static final String GET_EMPLOYEE_DEFAULT_BANK_DETAILS_BY_ID = "select id,bank_name,account_no,account_holder_name,branch_name,ifsc_code,upi_id,is_default,status from employee_bank_details where employee_id=? and status not IN(?) and is_default = 1";

}

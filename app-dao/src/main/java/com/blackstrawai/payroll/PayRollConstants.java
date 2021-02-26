package com.blackstrawai.payroll;

public class PayRollConstants {
	
	public static String CHECK_PAY_ITEM_EXIST_ORGANIZATION="select * from payroll.pay_item where organization_id=? and name=?";
	public static final String INSERT_INTO_PAY_ITEM_ORGANIZATION = "insert into payroll.pay_item(name,description,organization_id,user_id,isSuperAdmin,pay_type_id,ledger_id,ledger_name,is_base,role_name,show_column)values(?,?,?,?,?,?,?,?,?,?,?)";
	public static final String UPDATE_PAY_ITEM_ORGANIZATION = "update payroll.pay_item set name=?,description=?,organization_id=?,isSuperAdmin=?,update_user_id=?,update_ts=?,status=?,pay_type_id=?,ledger_id=?,ledger_name=?,update_role_name=? where id=?";
	public static final String DELETE_PAY_ITEM_ORGANIZATION = "update payroll.pay_item set status=?,update_ts=?,update_user_id=?,update_role_name=? where id=?";
	public static String GET_ALL_BASIC_PAY_ITEM_ORGANIZATION="select id,name,pay_type_id,status,description,organization_id,user_id,isSuperAdmin from payroll.pay_item where organization_id=?";

	public static String GET_ALL_BASIC_PAY_ITEM_ORGANIZATION_USER_ROLE="select id,name,pay_type_id,status,description,organization_id,user_id,isSuperAdmin from payroll.pay_item where organization_id=? and user_id=? and role_name=?";

	
	public static String GET_PAY_ITEM_ORGANIZATION="select id,name,description,pay_type_id,ledger_id,ledger_name,status,create_ts,update_ts,user_id,isSuperAdmin,organization_id from payroll.pay_item where id=?";
	public static String GET_PAY_ITEM_NAME_ORGANIZATION="select id,name,description,pay_type_id,ledger_id,ledger_name,status,create_ts,update_ts,user_id,isSuperAdmin,organization_id from payroll.pay_item where organization_id=? and name=?";

	
	public static String CHECK_PAY_TYPE_EXIST_ORGANIZATION="select * from payroll.pay_type where organization_id=? and name=?";
	public static final String INSERT_INTO_PAY_TYPE_ORGANIZATION = "insert into payroll.pay_type(name,description,organization_id,user_id,isSuperAdmin,parent_name,is_base ,debit_or_credit,role_name )values(?,?,?,?,?,?,?,?,?)";
	public static final String UPDATE_PAY_TYPE_ORGANIZATION = "update payroll.pay_type set name=?,description=?,organization_id=?,isSuperAdmin=?,update_user_id=?,update_ts=?,status=?,parent_name=?,update_role_name=? where id=?";
	public static final String DELETE_PAY_TYPE_ORGANIZATION = "update payroll.pay_type set status=?,update_ts=?,update_user_id=?,update_role_name=? where id=?";
	public static String GET_ALL_PAY_TYPE_ORGANIZATION="select id,name,description,parent_name,status,user_id,isSuperAdmin,organization_id,is_base,debit_or_credit  from payroll.pay_type where organization_id=?";
	
	public static String GET_ALL_PAY_TYPE_ORGANIZATION_USER_ROLE="select id,name,description,parent_name,status,user_id,isSuperAdmin,organization_id,is_base,debit_or_credit  from payroll.pay_type where organization_id=? and user_id=? and role_name=?";

	
	public static String GET_PAY_TYPE_ORGANIZATION="select id,name,description,parent_name,status,create_ts,update_ts,user_id,isSuperAdmin,organization_id,is_base,debit_or_credit from payroll.pay_type where id=?";
	public static String GET_PAY_TYPE_BY_NAME_ORGANIZATION="select id,name,description,parent_name,status,create_ts,update_ts,user_id,isSuperAdmin,organization_id from payroll.pay_type where organization_id=? and name=?";
	public static String GET_BASIC_PAY_TYPE_ORGANIZATION="select id,name from payroll.pay_type where organization_id=?";
	public static String GET_COUNT_PAY_ITEM_GIVEN_PAY_TYPE="select count(*)from payroll.pay_item where organization_id=? and pay_type_id=?";

	public static  String INSERT_INTO_PAYROLL_UPLOAD="insert into payroll.payroll_upload(employee_id,employee_name,pay_item_name,pay_item_value,organization_id,user_id,role_name ,journal_no,ledger_id,ledger_name,debit_or_credit )values(?,?,?,?,?,?,?,?,?,?,?)";
	public static String GET_MAX_VOUCHER_NO_PER_ORGANIZATION="select max(journal_no)from payroll.payroll_upload where organization_id=?";
    public static String GET_PAY_TYPE_ID_GIVEN_PAY_NAME="select pay_type_id from payroll.pay_item where organization_id=? and name=?";
    public static String GET_PARENT_PAY_TYPE_GIVEN_PAY_TYPE="select parent_name from  payroll.pay_type where organization_id=? and id=?";
    public static String GET_DEBIT_CREDIT_GIVEN_TYPE="select debit_or_credit from  payroll.pay_type where organization_id=? and name=?";
    public static String INSERT_INTO_PROCESSING_TEMP="insert into payroll.payroll_upload_processing_tmp(pay_item_name,pay_item_value,organization_id,user_id,role_name,debit_or_credit)values(?,?,?,?,?,?)";
    public static String GET_TEMP_DATA="select sum(pay_item_value) from payroll.payroll_upload_processing_tmp where organization_id=? and user_id=? and role_name=? and debit_or_credit=?";
    public static String GET_TEMP_DATA_GROUP_BY_PAY_ITEM="select sum(pay_item_value),pay_item_name from payroll.payroll_upload_processing_tmp where organization_id=? and user_id=? and role_name=? and debit_or_credit=? group by pay_item_name";
    public static String DELETE_TEMP_DATA="delete from payroll.payroll_upload_processing_tmp where organization_id=? and user_id=? and role_name=?";
    public static final String GET_PAY_RUN_PAYMENT_TYPE = "\r\n" + 
			"select id,name ,description ,parent_name , is_base , \r\n" + 
			"debit_or_credit from payroll.pay_type pt where organization_id = ? and status = ?";
  
    public static final String GET_PAY_RUN_PAYMENT_CYCLE_TYPE="SELECT id, "
    		+ "name, cycle, start_date,  end_date\r\n" + 
    		"FROM payroll.pay_cycle where organization_id =? and status = ?";
    
	public static final String GET_PAY_RUN_PERIOD_FREQUENCY = "select * from payroll.pay_period_frequency ppf where status = ? \r\n" + 
			"and pay_period_id\r\n" + 
			"in ( SELECT id  from payroll.pay_period pp where organization_id = ?  and status = ? )";
	
	public static final String GET_PAY_RUN_EMPLOYEE = "select id, employee_id , CONCAT(first_name,' ',last_name ), email,\r\n" + 
			"doj ,mobile_no ,department_id ,reporting_to_id ,employee_type_id , \r\n" + 
			"isSuperAdmin  from accounts_payable.employee e where organization_id = ? and status = ?";
	/*public static final String GET_PAY_TABLE_DROPDOWN = " select pi2.id ,"
			+ " pi2.name as name, "
			+ " pi2.name as value, " + 
			" case  when pt.name like '%arning' then  'Earnings'\r\n" + 
			"      when pt.name like '%llowance%' then  'Earnings'\r\n" + 
			"      when pt.name like '%eimbursement%' then 'Earnings'\r\n" + 
			"      when pt.name = 'Basic Salary' then 'Earnings'\r\n" + 
			"      when pt.name = 'Variable Pay' then 'Earnings'\r\n" + 
			"      else 'Deductions' end  as pay_type     ,\r\n" + 
			"      pi2.show_column \r\n" + 
			" from payroll.pay_item pi2 , payroll.pay_type pt \r\n" + 
			" where \r\n" + 
			" pi2.pay_type_id = pt.id \r\n" + 
			" and pi2.ledger_name not in('Employer Contribution to PF','Employer Contribution to ESI')\r\n" + 
			" and pi2.organization_id = ? " + 
			" and pi2.status = ? " ;*/

	//Below changes made for FIN820 point 4a issue
	public static final String GET_PAY_TABLE_DROPDOWN ="select pi2.id ,\n" +
			"\t\t\t  pi2.name as name, \n" +
			"\t\t\t  pi2.name as value,   \n" +
			"\t\t\t case  when pt.parent_name = 'Fixed Earnings' then  'Earnings'\n" +
			"\t\t\t      when pt.parent_name = 'Variable Earnings' then  'Earnings'\n" +
			"\t\t\t      when pt.parent_name = 'Reimbursements' then 'Earnings'\n" +
			"\t\t\t      when pt.parent_name = 'Deductions' then 'Deductions'\n" +
			"\t\t\t      end  as pay_type,\n" +
			"\t\t\t     case when  pi2.name = 'Basic' then 1\n" +
			"\t\t\t      when  pi2.name = 'HRA' then 1\n" +
			"\t\t\t      when  pi2.name = 'Special Allowance' then 1\n" +
			"\t\t\t      when  pi2.name = 'Conveyance' then 1\n" +
			"\t\t\t      when  pi2.name = 'Overtime' then 1\n" +
			"\t\t\t      when  pi2.name = 'Fuel' then 1\n" +
			"\t\t\t      when  pi2.name = 'Telephone' then 1\n" +
			"\t\t\t      when  pi2.name = 'Income Tax' then 1\n" +
			"\t\t\t      when  pi2.name = 'Professional Tax' then 1\n" +
			"\t\t\t     else pi2.show_column \n" +
			"\t\t\t     end as show_column\n" +
			"\t\t\t from payroll.pay_item pi2 , payroll.pay_type pt " +
			" where \r\n" +
			" pi2.pay_type_id = pt.id \r\n" +
			" and pi2.ledger_name not in('Employer Contribution to PF','Employer Contribution to ESI')\r\n" +
			" and pi2.organization_id = ? " +
			" and pi2.status = ? " ;

}

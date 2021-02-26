package com.blackstrawai.payroll;

public class PayRunConstants {

	/*
	 * public static final String Basic = "Basic"; public static final String HRA
	 * ="HRA"; public static final String specialAllowance = "Special Allowance";
	 * public static final String conveyance ="Conveyance"; public static final
	 * String pfEmployerContribution = "PF-Employer Contribution"; public static
	 * final String esiEmployerContribution = "ESI-Employer Contribution"; public
	 * static final String leaveEncashment = "Leave Encashment"; public static final
	 * String bonus = "Bonus"; public static final String overtime = "Overtime";
	 * public static final String pfEmployeeContribution =
	 * "PF-Employee Contribution"; public static final String
	 * esiEmployeeContribution = "ESI-Employee Contribution"; public static final
	 * String professionalTax ="Professional Tax"; public static final String
	 * incomeTax = "Income Tax"; public static final String salaryPayable =
	 * "Salary payable"; public static final String fule = "Fuel"; public static
	 * final String travel = "Travel"; public static final String telephone =
	 * "Telephone";
	 */

	public static final String GET_PAY_ITEM_LEDGERS_BY_ORG_ID = "SELECT name , ledger_id , ledger_name FROM payroll.pay_item where organization_id = ? ";
	public static final String GET_PAY_RUN_REF_BY_ID = "select payrun_reference from payrun where id = ?";

	public static final String INSERT_INTO_PAY_RUN = "INSERT INTO payroll.payrun\r\n"
			+ "(payrun_reference, payrun_date, pay_period, status, user_id,  organization_id,"
			+ " role_name,copy_previous_payrun_id , payment_cycle,general_ledger_data,"
			+ "payrun_info,settings_data,currency_id)\r\n" + "VALUES(?, ?, ?, ?,?, ?, ?,?,?,?,?,?,?)";
	/*
	 * public static final String INSERT_INTO_PAY_RUN_INFO =
	 * "INSERT INTO payroll.payrun_items_test\r\n" +
	 * "( payrun_id, employee_id, payitem_name, earnings_basic, earnings_HRA, " +
	 * "earnings_special_allowance, earnings_overtime, earnings_conveyance, " +
	 * "earnings_fuel, earnings_telephone, deductions_incometax, " +
	 * "deductions_professionaltax, deductions_pf, status,   " +
	 * " total_earnings, total_Deductions, net_payable ," +
	 * " earnings_leave_encashment, earnings_bonus, earnings_travel, " +
	 * " deductions_pf_employee_contribution, deductions_pf_employer_contribution,"
	 * +
	 * " deductions_esi_employee_contribution, deductions_esi_employer_contribution,other_columns ) "
	 * +
	 * "VALUES( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?,?,?,?,?,?,?)";
	 */
	public static final String SELECT_PAY_RUN = "SELECT id," + " payrun_reference, payrun_date, pay_period, "
			+ "status, user_id,  organization_id, "
			+ "role_name , copy_previous_payrun_id ,payment_cycle,general_ledger_data,payrun_info, "
			+ " settings_data,currency_id,create_ts FROM payroll.payrun " + "where id = ? and  status in (?,?,?,?,?) ";

	/*
	 * public static final String SELECT_PAY_RUN_INFO =
	 * "SELECT id, payrun_id, employee_id," + " payitem_name, earnings_basic, \r\n"
	 * + "earnings_HRA, earnings_special_allowance, earnings_overtime, \r\n" +
	 * "earnings_conveyance, earnings_fuel, earnings_telephone, \r\n" +
	 * "deductions_incometax, deductions_professionaltax, deductions_pf, \r\n" +
	 * "status, total_earnings, total_Deductions, net_payable , " +
	 * "  earnings_leave_encashment, earnings_bonus, earnings_travel, " +
	 * " deductions_pf_employee_contribution, deductions_pf_employer_contribution, "
	 * +
	 * " deductions_esi_employee_contribution, deductions_esi_employer_contribution "
	 * + "FROM payroll.payrun_items where  \r\n" +
	 * "  payrun_id = ?    and  status in (?,?,?,?) ";
	 */

	public static final String SELECT_PAY_RUN_USER_ROLE = "SELECT id," + " payrun_reference, payrun_date, pay_period, "
			+ "status, user_id,  organization_id, "
			+ "role_name , copy_previous_payrun_id ,payment_cycle,payrun_info , settings_data ,currency_id FROM payroll.payrun "
			+ "where organization_id = ? and  user_id = ? "
			+ "and role_name = ?  status in (?,?,?,?) order by id desc ";

	public static final String SELECT_PAY_RUN_USER_ROLE_ACT = "SELECT id,"
			+ " payrun_reference, payrun_date, pay_period, " + "status, user_id,  organization_id, "
			+ "role_name , copy_previous_payrun_id ,payment_cycle,payrun_info,currency_id FROM payroll.payrun "
			+ "where organization_id = ? and  user_id = ? " + "and role_name = ?  status in (?) order by id desc ";

	public static final String SELECT_PAY_RUN_ALL_ORGANIZATION = "SELECT id,"
			+ " payrun_reference, payrun_date, pay_period, " + "status, user_id,  organization_id, "
			+ "role_name , copy_previous_payrun_id ,payment_cycle,payrun_info , settings_data,currency_id FROM payroll.payrun "
			+ "where organization_id = ? and  status in (?,?,?,?) order by id desc ";

	public static final String SELECT_PAY_RUN_ALL_ORGANIZATION_ACT = "SELECT id,"
			+ " payrun_reference, payrun_date, pay_period, " + "status, user_id,  organization_id, "
			+ "role_name , copy_previous_payrun_id ,payment_cycle,payrun_info,currency_id FROM payroll.payrun "
			+ "where organization_id = ? and  status in (?) order by id desc ";

	/*
	 * public static final String SELECT_PAY_RUN_INFO_ALL =
	 * "SELECT id, payrun_id, employee_id, payitem_name, earnings_basic, \r\n" +
	 * "earnings_HRA, earnings_special_allowance, earnings_overtime, \r\n" +
	 * "earnings_conveyance, earnings_fuel, earnings_telephone, \r\n" +
	 * "deductions_incometax, deductions_professionaltax, deductions_pf, \r\n" +
	 * "status, total_earnings, total_Deductions, net_payable , " +
	 * "  earnings_leave_encashment, earnings_bonus, earnings_travel, " +
	 * "  deductions_pf_employee_contribution, deductions_pf_employer_contribution, "
	 * +
	 * "  deductions_esi_employee_contribution, deductions_esi_employer_contribution "
	 * + "FROM payroll.payrun_items where  \r\n" + " payrun_id = ? ";
	 */

	public static final String UPDATE_PAY_RUN = "UPDATE payroll.payrun "
			+ " SET payrun_reference=?, payrun_date=?, pay_period=?, "
			+ "status=?,update_user_id=?,  organization_id=?, " + "update_role_name=? ,copy_previous_payrun_id = ? , "
			+ "payment_cycle =?,general_ledger_data=? ,payrun_info=?, update_ts = current_timestamp() ,"
			+ " settings_data = ? , currency_id = ?" + " WHERE id = ? ";
	/*
	 * public static final String UPDATE_INTO_PAY_RUN_INFO =
	 * "UPDATE payroll.payrun_items\r\n" +
	 * "SET payrun_id=?, employee_id=?, payitem_name=?, earnings_basic=?, earnings_HRA=?, "
	 * +
	 * "earnings_special_allowance=?, earnings_overtime=?, earnings_conveyance=?, "
	 * + "earnings_fuel=?, earnings_telephone=?, deductions_incometax=?," +
	 * " deductions_professionaltax=?, deductions_pf=?, status=?,    " +
	 * "total_earnings=?, total_Deductions=?, " + "net_payable =? , " +
	 * "  earnings_leave_encashment  =? , earnings_bonus  =? , earnings_travel  =? ,"
	 * +
	 * "  deductions_pf_employee_contribution  =? , deductions_pf_employer_contribution  =?,"
	 * +
	 * "  deductions_esi_employee_contribution  =? , deductions_esi_employer_contribution  =? "
	 * + "WHERE id=?";
	 */
	public static final String SELECT_PAY_RUN_ORG = "SELECT id," + " payrun_reference FROM payroll.payrun "
			+ " where organization_id = ? and status in (?,?,?,?) ";
	/*
	 * public static final String GET_PAY_AMOUNT =
	 * "select sum(net_payable ) from payroll.payrun_items pi2  where payrun_id = ? and  status in (?,?,?,?) "
	 * ; public static final String GET_EMPLOYEE_COUNT =
	 * "select count(id) from payroll.payrun_items pi2  where payrun_id = ? and  status in (?,?,?,?) "
	 * ;
	 */

	public static final String CHECK_PAY_RUN_REF_EXIST_FOR_ORG = "SELECT payrun_reference from"
			+ " payroll.payrun where organization_id = ? and payrun_reference = ? ";
	public static final String CHECK_PAY_RUN_REF_EXIST_FOR_ORG_UPD = "SELECT payrun_reference from"
			+ " payroll.payrun where id != ?  and payrun_reference = ?  ";
	public static final String CHECK_PAY_RUN_ID_EXIST_FOR_ORG = "SELECT id from"
			+ " payroll.payrun where id = ? and organization_id =?  ";
	/*
	 * public static final String UPDATE_INTO_PAY_RUN_INFO_DEL =
	 * " UPDATE payroll.payrun_items" + " SET status = ? where id = ? ";
	 */
	public static final String DEACTIVATE_PAY_RUN = "update payroll.payrun " + " set status = ?  , organization_id = ? "
			+ " , update_user_id = ? , update_role_name= ? " + " , update_ts = CURRENT_TIMESTAMP()  where id = ? ";
	public static final String UPDATE_PAYRUN_TABLE_SETTINGS = " update  payroll.pay_item " + " set show_column = ?  "
			+ " where id = ? ";
	/*
	 * public static final String GET_DEDUCTIONS_COLUMN =
	 * "select jt.* from payrun_items_test j  ," +
	 * " json_table((json_keys(json_extract(other_columns,'$[*]'),'$[0]')) ,\r\n" +
	 * "		'$[*]' columns ( col varchar(30) PATH '$[0]')) as jt where j.id = ? and jt.col like '%eductio%'"
	 * ;
	 */
	public static final String GET_DEDUCTIONS_AMOUNT = null;

	public static final String GET_PAYRUN_REFERENCE_IMPORT = " select payrun_reference from payrun p2 "
			+ "  where organization_id = ? and id = (select max(id) "
			+ "		from payrun p2 where organization_id = ? ) ";
	public static final String SELECT_PAY_RUN_ORG_EMP_ID = "SELECT pr.id, pr.payrun_reference, pre.due_amount"
			+ " FROM payroll.payrun pr JOIN payroll.payrun_employee pre ON pr.payrun_reference = pre.payrun_reference"
			+ " WHERE pr.organization_id = ? and pr.status in (?,?,?)  and pre.status ='ACT' and pre.emp_id = ? ";

	public static final String DELETE_PAYRUN_EMPLOYEE = "delete from payroll.payrun_employee where payrun_reference=?";
	public static final String INSERT_PAYRUN_EMPLOYEE = "insert into payroll.payrun_employee(payrun_reference,emp_id,amount,due_amount)values(?,?,?,?)";
	public static final String SELECT_PAY_RUN_ORG_CURRENCY = "  select base_currency_id from usermgmt.organization o"
			+ " where id = ? and status = ? ";
	public static final String SELECT_EMPLOYEES_BY_PAYRUN_ID = "SELECT pre.emp_id, pre.due_amount"
			+ " FROM payroll.payrun pr JOIN payroll.payrun_employee pre ON pr.payrun_reference = pre.payrun_reference"
			+ " WHERE pr.organization_id = ? and pr.status in (?,?,?) and pr.id = ? AND pre.due_amount>0";
	public static final String GET_BASIC_PAYRUN = "SELECT currency_id,status FROM payroll.payrun WHERE id=?";
	public static final String GET_ADJ_PAYRUN_FOR_EMP_CURRENCY = "SELECT sum(net_pay) FROM payments_non_core_employee_details pnced join payments_non_core pnc on pnced.payments_non_core_id=pnc.id WHERE pnced.pay_run=? and pnced.employee_id=? AND pnc.currency_id=? AND pnc.status=? AND pnced.status=?";
	public static final String SELECT_ALL_EMPLOYEES_BY_PAYRUN_ID = "SELECT pre.id,pre.emp_id, pre.amount,pre.due_amount FROM payroll.payrun pr JOIN payroll.payrun_employee pre ON pr.payrun_reference = pre.payrun_reference WHERE pr.id = ?";
	public static final String UPDATE_PAYRUN_EMP_DUE_BALANCE = "UPDATE payrun_employee SET due_amount =  ? WHERE id = ?";
	public static final String UPDATE_PAYRUN_STATUS = "UPDATE payrun SET status = ? WHERE id = ?";
	
	public static final String GET_PAYROLL_PAYABLE = "SELECT pr.id, pr.pay_period, pre.payrun_reference, SUM(pre.amount), SUM(pre.due_amount), pr.status, co.symbol, pr.payrun_date "
			+ "FROM payrun pr, payrun_employee pre, usermgmt.currency_organization co "
			+ "WHERE pr.organization_id = ? AND pr.payrun_reference = pre.payrun_reference AND pr.status NOT IN (?,?,?,?) "
			+ "AND co.id = pr.currency_id GROUP BY pr.id ORDER BY pr.payrun_date ASC";
	
	public static final String GET_PAYROLL_PAYABLE_USER_ROLE = "SELECT pr.id, pr.pay_period, pre.payrun_reference, SUM(pre.amount), SUM(pre.due_amount), pr.status, co.symbol, pr.payrun_date "
			+ "FROM payrun pr, payrun_employee pre, usermgmt.currency_organization co "
			+ "WHERE pr.organization_id = ? AND pr.payrun_reference = pre.payrun_reference AND pr.status NOT IN (?,?,?,?) "
			+ "AND co.id = pr.currency_id AND pr.user_id = ? AND pr.role_name = ? GROUP BY pr.id ORDER BY pr.payrun_date ASC";

}

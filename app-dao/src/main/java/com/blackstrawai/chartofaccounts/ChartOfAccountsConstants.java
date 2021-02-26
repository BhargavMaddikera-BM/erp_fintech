package com.blackstrawai.chartofaccounts;

public class ChartOfAccountsConstants {
	
	public static final String MODULE_AR = "AR";
	
	public static final String MODULE_AP = "AP";
	
	public static final String MODULE_PAYROLL = "PayRoll";

	
	
	public static final String ENTITY_CUSTOMER = "Customer";
	
	public static final String ENTITY_VENDOR = "Vendor";
	
	public static final String ENTITY_EMPLOYEE = "Employee";
	
	public static final String ENTITY_EMPLOYEE_PAYABLE = "Employee Payable";
	
	public static final String ENTITY_EMPLOYEE_PROVISION = "Employee Provision";
	
	public static final String ENTITY_EMPLOYEE_RECEIVABLE = "	Employee Receivable";
	
	public static final String ENTITY_EMPLOYEE_EXPENSES = "Employee Expenses";
	
	public static final String ENTITY_ESIC_LIABILITY = "ESIC Liability";
	
	public static final String ENTITY_P_TAX_LIABILITY = "P-Tax Liability";
	
	public static final String ENTITY_PF_LIABILITY = "PF Liability";
	
	public static final String ENTITY_SALARY_PAYABLE = "Salary Payable";
	
	public static final String ENTITY_GST_RECEIVABLE_PAYABLE = "GST Receivable/Payable";
	
	public static final String ENTITY_ADVANCE_TAX_AND_TDS = "Advance Tax & TDS";
	
	public static final String ENTITY_TDS_PAYABLE = "TDS Payable";
	
	public static final String ENTITY_DISCOUNT_GIVEN = "Discount Given";
	
	public static final String ENTITY_DISCOUNT_RECEIVED = "Discount Received";
	
	public static final String ENTITY_ROUND_OFF = "Round Off";
	
	public static final String ENTITY_SALE = "Sale";
	
	public static final String ENTITY_GST = "GST";
	
	public static final String ENTITY_PAYMENT = "Payment";
	
	public static final String ENTITY_PURCHASE = "Purchase";
	
	public static final String ENTITY_TDS = "TDS";
	
	public static final String ENTITY_INDIRECT_EXPENSES = "Indirect Expenses";
	
	public static final String FIELD_DISCOUNT = "Discount";
	
	public static final String FIELD_ADJUSTMENTS = "Adjustments";
	
	public static final String FIELD_GOVERNMENT_BODIES_TDS = "Government Bodies(TDS)";
	
	public static final String FIELD_INTEREST_ON_TDS = "Interest on TDS";
	
	public static final String FIELD_INTEREST_ON_GST = "Interest on GST";
	
	public static final String FIELD_PENALTIES_ON_TDS = "Penalties on TDS";
	
	public static final String FIELD_PENALTIES_ON_GST = "Penalties on GST";
	
	public static final String FIELD_BANK_CHARGES = "Bank Charges";
	
	public static final String FIELD_EMPLOYEE_PAYRUN = "Employees [Payrun]";
	
	
	
	
	
	public  static final String GET_LEDGER_BY_ENTITY_NAME = "SELECT l5.id, l5.name ,coae.name FROM usermgmt.chart_of_accounts_level5_organization l5 " + 
			" join finance_common.chart_of_accounts_entity coae on coae.id = l5.chart_of_accounts_entity_id " + 
			" where l5.organization_id = ? and coae.name = (?)";

	public  static final String GET_LEDGER_BY_ORG_ID = "SELECT l5.id, l5.name ,coae.name FROM usermgmt.chart_of_accounts_level5_organization l5 " + 
			" join finance_common.chart_of_accounts_entity coae on coae.id = l5.chart_of_accounts_entity_id " + 
			" where l5.organization_id = ? ";

	public  static final String GET_LEDGER_BY_LEDGER_ID  = "SELECT l5.id, l5.name ,l5.description FROM usermgmt.chart_of_accounts_level5_organization l5 " +
			" where l5.organization_id = ? and l5.id =? ";
	
	
	public  static final String GET_LEDGER_BY_LEDGER_NAME  = "SELECT l5.id, l5.name ,l5.description FROM usermgmt.chart_of_accounts_level5_organization l5 " +
			" where l5.organization_id = ? and l5.name =? ";
	
	

	static final String GET_DEFAULT_LEDGER = "SELECT distinct(default_value)  FROM finance_common.chart_of_accounts_mapping   ";
	
	public  static final String INSERT_INTO_LEDGER = "insert into chart_of_accounts_level5_organization( name, description,is_base,status,organization_id,user_id,isSuperAdmin,chart_of_accounts_level4_id,account_code,ledger_balance,as_on,mandatory_sub_ledger,chart_of_accounts_module_id,display_name,chart_of_accounts_entity_id,role_name) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	
	public static final String GET_LEDGER_BY_ID ="SELECT l4.chart_of_accounts_level3_id, l5.chart_of_accounts_level4_id , l5.name, l5.account_code, l5.ledger_balance, l5.as_on, l5.chart_of_accounts_module_id," + 
			"    if(l5.status='ACT',true,false) as ledger_status,l5.mandatory_sub_ledger,l5.chart_of_accounts_entity_id,l5.is_base, l5.organization_id, " + 
			"    l5.user_id, l5.isSuperAdmin   FROM chart_of_accounts_level5_organization l5 " + 
			"    JOIN chart_of_accounts_level4_organization l4 ON l4.id = l5.chart_of_accounts_level4_id WHERE l5.id = ?";
	
	public static final String UPDATE_LEDGER_BY_ID = "update chart_of_accounts_level5_organization set name=?, description=?,is_base=?,status=?,organization_id=?,update_user_id=?,isSuperAdmin=?,chart_of_accounts_level4_id=?,account_code=?,ledger_balance=?,as_on=?,mandatory_sub_ledger=?,chart_of_accounts_module_id=?,display_name=?,chart_of_accounts_entity_id=?,update_ts=?,update_role_name=? where id = ?";
	
	public static final String GET_ACCOUNT_CODE_FOR_LEDGER= " select account_code from chart_of_accounts_level5_organization where id = ?";
	
	public static final String GET_LEDGER_NAME_FOR_LEDGER= " select name from chart_of_accounts_level5_organization where id = ?";

	public static final String GET_MANDATORY_SUBLEDGER_FOR_LEDGER= " select mandatory_sub_ledger from chart_of_accounts_level5_organization where id = ?";

	public static final String GET_ENTITY_FOR_LEDGER= " select chart_of_accounts_entity_id from chart_of_accounts_level5_organization where id = ?";
	
	public static final String INSERT_INTO_SUBLEDGER = "insert into chart_of_accounts_level6_organization (name, description, is_base, status, organization_id, user_id, isSuperAdmin, chart_of_accounts_level5_id, display_name) values(?,?,?,?,?,?,?,?,?)";

	public static final String IS_ACCOUNT_CODE_EXIST = "SELECT * FROM chart_of_accounts_level5_organization where organization_id =? and  account_code = ?";
	
	public static final String IS_LEDGER_EXIST = "SELECT * FROM chart_of_accounts_level5_organization where organization_id =? and  chart_of_accounts_level4_id = ? and name = ?";
	
/*	public static final String DELETE_LEDGER_BY_ID = "update chart_of_accounts_level5_organization set status = ? , update_ts=? where id = ?";
*/	
	public static final String DELETE_SUB_LEDGER_BY_LEDGER_ID = "delete from chart_of_accounts_level6_organization  where chart_of_accounts_level5_id = ?";

	public static final String GET_ALL_LEDGERS_USER_ROLE = "SELECT l5.id,l5.name,l5.status,l4.name as level_4_name,l3.name as level_3_name , l2.name as level_2_name , l1.name as level_1_name,l5.is_base  FROM chart_of_accounts_level5_organization l5 " + 
															"join chart_of_accounts_level4_organization l4 on l4.id = l5.chart_of_accounts_level4_id " + 
															"join chart_of_accounts_level3_organization l3 on l3.id = l4.chart_of_accounts_level3_id " + 
															"join chart_of_accounts_level2_organization l2 on l2.id = l3.chart_of_accounts_level2_id " + 
															"join chart_of_accounts_level1_organization l1 on l1.id = l2.chart_of_accounts_level1_id " + 
															"where l5.organization_id = ? and l5.user_id=? and l5.role_name=? ";
	
	public static final String GET_ALL_LEDGERS_ORGANIZATION = "SELECT l5.id,l5.name,l5.status,l4.name as level_4_name,l3.name as level_3_name , l2.name as level_2_name , l1.name as level_1_name,l5.is_base  FROM chart_of_accounts_level5_organization l5 " + 
			"join chart_of_accounts_level4_organization l4 on l4.id = l5.chart_of_accounts_level4_id " + 
			"join chart_of_accounts_level3_organization l3 on l3.id = l4.chart_of_accounts_level3_id " + 
			"join chart_of_accounts_level2_organization l2 on l2.id = l3.chart_of_accounts_level2_id " + 
			"join chart_of_accounts_level1_organization l1 on l1.id = l2.chart_of_accounts_level1_id " + 
			"where l5.organization_id = ? ";
	
	public static final String GET_LEVEL5_LEDGERS_FOR_ORGANIZATION = "select id,name from usermgmt.chart_of_accounts_level5_organization WHERE organization_id=? and status not in ('DEL')";

	public static final String GET_LEVEL3_CHART_OF_ACCOUNTS_BY_NAME_ORGANIZATION = "select id from chart_of_accounts_level3_organization where organization_id=? and name=?";
	public static final String GET_LEVEL4_CHART_OF_ACCOUNTS_BY_ID_NAME_ORGANIZATION = "select id from chart_of_accounts_level4_organization where organization_id=? and chart_of_accounts_level3_id =? and name=?";
	public static final String GET_LEVEL5_CHART_OF_ACCOUNTS_BY_ID_NAME_ORGANIZATION = "select id from chart_of_accounts_level5_organization where organization_id=? and chart_of_accounts_level4_id =? and name=?";

	public static final String INSERT_INTO_CHART_OF_ACCOUNTS1_ORGANIZATION = "insert into chart_of_accounts_level1_organization(name,type,organization_id ,user_id ,isSuperAdmin ,is_base)values(?,?,?,?,?,?)";
	public static final String INSERT_INTO_CHART_OF_ACCOUNTS2_ORGANIZATION = "insert into chart_of_accounts_level2_organization(name,chart_of_accounts_level1_id,description ,is_base,organization_id ,user_id ,isSuperAdmin )values(?,?,?,?,?,?,?)";
	public static final String INSERT_INTO_CHART_OF_ACCOUNTS3_ORGANIZATION = "insert into chart_of_accounts_level3_organization(name,chart_of_accounts_level2_id,description ,is_base,organization_id ,user_id ,isSuperAdmin )values(?,?,?,?,?,?,?)";
	public static final String INSERT_INTO_CHART_OF_ACCOUNTS4_ORGANIZATION = "insert into chart_of_accounts_level4_organization(name,chart_of_accounts_level3_id,description ,is_base,organization_id ,user_id ,isSuperAdmin )values(?,?,?,?,?,?,?)";
	public static final String INSERT_INTO_CHART_OF_ACCOUNTS5_ORGANIZATION = "insert into chart_of_accounts_level5_organization(name,chart_of_accounts_level4_id,description ,is_base,organization_id ,user_id ,isSuperAdmin,chart_of_accounts_entity_id,chart_of_accounts_module_id,mandatory_sub_ledger  )values(?,?,?,?,?,?,?,?,?,?)";
	public static final String INSERT_INTO_CHART_OF_ACCOUNTS6_ORGANIZATION = "insert into chart_of_accounts_level6_organization(name,description,is_base,organization_id,user_id,isSuperAdmin,chart_of_accounts_level5_id,display_name)values(?,?,?,?,?,?,?,?)";

	public static final String GET_LEVEL5_CHART_OF_ACCOUNTS_ORGANIZATION = "select id,name,description,chart_of_accounts_level4_id from chart_of_accounts_level5_organization where organization_id=? and status not in(?)";
	public static final String GET_LEVEL6_CHART_OF_ACCOUNTS_ORGANIZATION = "select id,name,display_name,description,chart_of_accounts_level5_id from chart_of_accounts_level6_organization where organization_id=? and status not in(?)";

	public static final String GET_LEVEL1_CHART_OF_ACCOUNTS_BY_ORG_ID = "SELECT id, name FROM chart_of_accounts_level1_organization where organization_id = ? and status not in ('DEL') ";
	public static final String GET_LEVEL2_CHART_OF_ACCOUNTS_BY_LEVEL1_ID_AND_ORG_ID = "SELECT id,name FROM chart_of_accounts_level2_organization where chart_of_accounts_level1_id = ? and  organization_id= ? and status not in ('DEL') ";
	public static final String GET_LEVEL3_CHART_OF_ACCOUNTS_BY_LEVEL2_ID_AND_ORG_ID = "SELECT id,name FROM chart_of_accounts_level3_organization where chart_of_accounts_level2_id = ? and  organization_id= ? and status not in ('DEL') ";
	public static final String GET_LEVEL4_CHART_OF_ACCOUNTS_BY_LEVEL3_ID_AND_ORG_ID = "SELECT id,name FROM chart_of_accounts_level4_organization where chart_of_accounts_level3_id = ? and  organization_id= ? and status not in ('DEL') ";
	public static final String GET_LEVEL5_CHART_OF_ACCOUNTS_BY_LEVEL4_ID_AND_ORG_ID = "SELECT id,name FROM chart_of_accounts_level5_organization where chart_of_accounts_level4_id = ? and  organization_id= ? and status not in ('DEL') ";
	public static final String GET_LEVEL6_CHART_OF_ACCOUNTS_BY_LEVEL5_ID_AND_ORG_ID = "SELECT id,name,display_name FROM chart_of_accounts_level6_organization where chart_of_accounts_level5_id = ? and  organization_id= ? and status not in ('DEL') ";

	public static final String GET_CHART_OF_ACCOUNTS_FOR_DROPDOWN_ORGANIZATION = "SELECT l5.id , l5.name ,l6.id ,l6.name, l6.display_name FROM chart_of_accounts_level5_organization l5 left JOIN "
			+ " usermgmt.chart_of_accounts_level6_organization l6 ON l6.chart_of_accounts_level5_id = l5.id WHERE l5.organization_id = ?";
	public static final String GET_CUSTOMER_LEDGERS_FOR_LEVEL5_ORGANIZATION = "select id,name from usermgmt.chart_of_accounts_level5_organization WHERE \r\n"
			+ "chart_of_accounts_entity_id=(select id from finance_common.chart_of_accounts_entity WHERE NAME='Customer' and status not in ('DEL')) and organization_id=?";
	public static final String GET_CHART_OF_ACCOUNTS_FOR_LEVEL2_GIVEN_TYPE_ORGANIZATION = "select l5.id , l5.name ,l6.id ,l6.name from usermgmt.chart_of_accounts_level5_organization l5"
			+ "	left join usermgmt.chart_of_accounts_level6_organization l6 on "
			+ " l5.id = l6.chart_of_accounts_level5_id" + "	join  usermgmt.chart_of_accounts_level4_organization l4 on "
			+ " l4.id = l5.chart_of_accounts_level4_id" + " join usermgmt.chart_of_accounts_level3_organization l3 on "
			+ " l3.id = l4.chart_of_accounts_level3_id "
			+ " join usermgmt.chart_of_accounts_level2_organization l2 on  "
			+ " l3.chart_of_accounts_level2_id  = l2.id  "
			+ " where l2.id =( SELECT l2.id FROM usermgmt.chart_of_accounts_level2_organization l2"
			+ " WHERE l2.organization_id = ? AND l2.name = ?) ";

	
	public static final String GET_ACCOUNT_GROUP_BY_ORG_ID = "SELECT id,name,chart_of_accounts_level2_id FROM chart_of_accounts_level3_organization where organization_id= ? and status not in ('DEL') ";
	public static final String GET_ACCOUNT_NAME_BY_ORG_ID = "SELECT id,name,chart_of_accounts_level3_id FROM chart_of_accounts_level4_organization where organization_id= ? and status not in ('DEL') ";
	public static final String GET_ACCOUNT_TYPE_BY_ORG_ID = "SELECT id,name,chart_of_accounts_level1_id FROM chart_of_accounts_level2_organization where organization_id= ? and status not in ('DEL') ";

	public static final String GET_ACCOUNT_TYPE_BY_LEDGER_ID = "SELECT l2.name FROM usermgmt.chart_of_accounts_level5_organization  l5 \r\n"
			+ "left join usermgmt.chart_of_accounts_level4_organization l4  on l4.id = l5.chart_of_accounts_level4_id\r\n"
			+ "left join usermgmt.chart_of_accounts_level3_organization l3 on l3.id = l4.chart_of_accounts_level3_id\r\n"
			+ "left join usermgmt.chart_of_accounts_level2_organization l2 on l2.id = l3.chart_of_accounts_level2_id\r\n"
			+ "where l5.id = ? ";

	public static final String GET_ACCOUNT_TYPE_BY_SUB_LEDGER_ID = "SELECT l2.name FROM usermgmt.chart_of_accounts_level6_organization  l6 \r\n"
			+ "left join usermgmt.chart_of_accounts_level5_organization l5  on l5.id = l6.chart_of_accounts_level5_id\r\n"
			+ "left join usermgmt.chart_of_accounts_level4_organization l4  on l4.id = l5.chart_of_accounts_level4_id\r\n"
			+ "left join usermgmt.chart_of_accounts_level3_organization l3 on l3.id = l4.chart_of_accounts_level3_id\r\n"
			+ "left join usermgmt.chart_of_accounts_level2_organization l2 on l2.id = l3.chart_of_accounts_level2_id\r\n"
			+ "where l6.id = ? ";

	public static final String GET_ALL_LEVELS_BY_ORG_ID_AND_LEDGER_ID = "SELECT l5.id,l5.name,l5.status,l4.name as level_4_name,l3.name as level_3_name , l2.name as level_2_name , l1.name as level_1_name,l5.is_base  FROM chart_of_accounts_level5_organization l5 "
			+ "join chart_of_accounts_level4_organization l4 on l4.id = l5.chart_of_accounts_level4_id "
			+ "join chart_of_accounts_level3_organization l3 on l3.id = l4.chart_of_accounts_level3_id "
			+ "join chart_of_accounts_level2_organization l2 on l2.id = l3.chart_of_accounts_level2_id "
			+ "join chart_of_accounts_level1_organization l1 on l1.id = l2.chart_of_accounts_level1_id "
			+ "where l5.organization_id = ? and l5.id= ? ";

	
	public static final String GET_LEDGER_BY_SUBLEDGER_DESCRIPTION = "SELECT l5.id ,l5.name  FROM usermgmt.chart_of_accounts_level6_organization l6\r\n" + 
			"join usermgmt.chart_of_accounts_level5_organization l5 on l5.id = l6.chart_of_accounts_level5_id " + 
			" where l6.organization_id = ? and l6.description =? and  l6.name  = ?";
	
	
	public static final String GET_LEDGER_AND_SIBLINGS_BY_LEDGER_NAME= "SELECT id,name FROM usermgmt.chart_of_accounts_level5_organization where organization_id = ? and chart_of_accounts_level4_id "
			+ " in (SELECT chart_of_accounts_level4_id FROM usermgmt.chart_of_accounts_level5_organization where organization_id = ? and name = ?) " ;
	
	public static final String GET_SUBLEDGER_DESCRIPTION_DETAILS = "SELECT description,name FROM chart_of_accounts_level6_organization where organization_id = ? and id = ? ";
	public static final String GET_LEDGER_NAME = "select name from chart_of_accounts_level5_organization where organization_id=? and id=?";
	public static final String GET_LEDGER_ID_GIVEN_NAME_ORGANIZATION="select id from chart_of_accounts_level5_organization where organization_id=? and name=?";
	public static final String GET_SUB_LEDGER_NAME = "select name from chart_of_accounts_level6_organization where organization_id=? and id=?";
	public static final String GET_ID_GIVEN_LEDGER_NAME_ORGANIZATION = "select id from chart_of_accounts_level5_organization where organization_id=? and name=?";
	public static final String GET_ACCOUNTING_ENTRY_SUB_LEDGER_ID_USING_DISPLAY_NAME = "select id from chart_of_accounts_level6_organization where organization_id=? and display_name=?";
	public static final String GET_ID_GIVEN_SUB_LEDGER_NAME_ORGANIZATION="select id from chart_of_accounts_level6_organization where organization_id=? and display_name=?";
	public static final String GET_SUB_LEDGER_BY_TYPE="select level5.id,level5.name from chart_of_accounts_level6_organization level6,chart_of_accounts_level5_organization level5 where level6.organization_id=? and level6.description=? and level6.name=? and level5.id=level6.chart_of_accounts_level5_id";

	public static final String GET_LEVEL4_CHART_OF_ACCOUNTS_NAME_ORGANIZATION = "select id from chart_of_accounts_level4_organization where organization_id=? and name=?";
	public static final String INSERT_INTO_CHART_OF_ACCOUNTS5_ORGANIZATION_ADDITIONAL = "insert into chart_of_accounts_level5_organization(name,chart_of_accounts_level4_id,description ,is_base,organization_id ,user_id ,isSuperAdmin,chart_of_accounts_entity_id,chart_of_accounts_module_id,mandatory_sub_ledger,role_name)values(?,?,?,?,?,?,?,?,?,?,?)";
	
	public static final String GET_COA_REPORT_BY_ORG="select level1.name,level2.name,level3.name,level4.name,level5.name,entity.name " + 
			"from chart_of_accounts_level1_organization level1,chart_of_accounts_level2_organization level2," + 
			"chart_of_accounts_level3_organization level3, chart_of_accounts_level4_organization level4," + 
			"chart_of_accounts_level5_organization level5,finance_common.chart_of_accounts_entity entity where " + 
			"level5.organization_id=? and " + 
			"level1.id=level2.chart_of_accounts_level1_id and " + 
			"level2.id=level3.chart_of_accounts_level2_id and " + 
			"level3.id=level4.chart_of_accounts_level3_id and " + 
			"level4.id=level5.chart_of_accounts_level4_id and " + 
			"entity.id=level5.chart_of_accounts_entity_id";
}



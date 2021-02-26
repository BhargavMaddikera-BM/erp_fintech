package com.blackstrawai.common;

public class FinanceCommonConstants {

	public static final String ITEM_TAX_RATE = "select id,name,description from item_rates_tax";
	public static final String DISCOUNT_TYPE = "select id,name,description from discount_type";
	public static final String PURCHASE_ORDER_TYPE = "select id,name,description from purchase_order_type";
	public static final String SHIPPING_METHOD = "select id,name,description from shipping_method";
	public static final String TAX_RATE = "select id,name,description from tax_rate";
	public static final String GST_TREATMENT = "select id,name,description from gst_treatment";
	public static final String TDS = "select id,name,tds_rate_identifier,tds_rate_percentage from tds";
	public static final String FULL_TDS = "select id,name,tds_rate_identifier,tds_rate_percentage,description,applicable_for from tds order by id asc";

	public static final String SOURCE_OF_SUPPLY = "select id,state_name,state_code from source_of_supply";
	public static final String GET_BASE_PAYMENT_TERMS = "select id,name,description,base_date,days_limit,account_type from base_payment_terms";
	public static final String GET_MINIMAL_BASE_CURRENCY = "select id,name from base_currency";
	public static final String GET_ORGANIZATION_DIVISION = "select id,name,description from organization_division where organization_type_id=?";
	public static final String GET_ALL_ORGANIZATION_TYPE = "select id,name,description from organization_type";
	public static final String GET_ORGANIZATION_TYPE_BY_NAME = "select id,name,description from organization_type where name=?";
	public static final String GET_ORGANIZATION_DIVISION_BY_NAME = "select id,name,description from organization_division where name=?";
	public static final String GET_ALL_ORGANIZATION_CONSTITUTION = "select id,name,description from organization_constitution";
	public static final String GET_ALL_BASE_CURRENCY = "select id,name,description,symbol,alternate_symbol,is_space_required,"
			+ "is_millions,number_of_decimal_places,decimal_value_denoter,no_of_decimals_for_amount_in_words,exchange_value,value_format from base_currency";
	/*
	 * public static final String
	 * GET_BASE_CURRENCY="select id,name,description,symbol,alternate_symbol,is_space_required,"
	 * +
	 * "is_millions,number_of_decimal_places,decimal_value_denoter,no_of_decimals_for_amount_in_words,exchange_value from base_currency"
	 * ;
	 */
	public static final String GET_EMPLOYEE_TYPE = "select id,name from employee_type";
	public static final String GET_ORGANIZATION_CONSTITUTION = "select id,name from organization_constitution";

	public static final String GET_UNIT_OF_MEASURE = "select id,quantity,unit_name,symbol from unit_of_measure";

	public static final String GET_UNIT_OF_MEASURE_BY_ID = "select unit_name from unit_of_measure where id =?";
	public static final String GET_COUNTTRY_STATE = "select c1.id,c1.name,s1.id,s1.name from country_list c1,state_list s1 where c1.id=s1.country_id";

	public static final String GET_ACCOUNTING_ASPECTS_TYPE = "select id,name from accounting_types";

	public static final String GET_MINIMAL_UNIT_OF_MEASURE = "select id,unit_name,quantity from unit_of_measure";

	public static final String GET_LEVEL1_CHART_OF_ACCOUNTS = "select id,name from chart_of_accounts_level1";
	public static final String GET_LEVEL2_CHART_OF_ACCOUNTS = "select level2.id,level2.name,level1.name from chart_of_accounts_level2 level2, chart_of_accounts_level1 level1 where level1.id=level2.chart_of_accounts_level1_id ";
	public static final String GET_LEVEL3_CHART_OF_ACCOUNTS = "select level3.id,level3.name,level2.name from chart_of_accounts_level3 level3, chart_of_accounts_level2 level2 where level2.id=level3.chart_of_accounts_level2_id ";
	public static final String GET_LEVEL4_CHART_OF_ACCOUNTS = "select level4.id,level4.name,level3.name from chart_of_accounts_level4 level4, chart_of_accounts_level3 level3 where level3.id=level4.chart_of_accounts_level3_id ";
	public static final String GET_LEVEL5_CHART_OF_ACCOUNTS = "select level5.id,level5.name,level4.name,level5.chart_of_accounts_level4_id from chart_of_accounts_level5 level5, chart_of_accounts_level4 level4 where level4.id=level5.chart_of_accounts_level4_id ";
	public static final String GET_LEVEL6_CHART_OF_ACCOUNTS = "select id,name from chart_of_accounts_level6";

	public static final String GET_BASE_TAX_RATE_TYPE = "select type,usage_type,is_inter  from tax_rate_type";
	public static final String GET_BASE_TAX_RATE_MAPPING = "select m.name,m.tax_rate_type_id,m.rate,r.type from tax_rate_mapping m,tax_rate_type r where r.id=m.tax_rate_type_id";
	public static final String GET_BASE_TAX_GROUP = "select name,taxes_included,combined_rate from tax_group";

	public static final String GET_BASE_CURRENCY_BY_ID = "select id,name,description,symbol,alternate_symbol,is_space_required,is_millions,"
			+ "number_of_decimal_places,decimal_value_denoter,no_of_decimals_for_amount_in_words,exchange_value,value_format,create_ts,update_ts"
			+ " from base_currency where id=?";
	public static final String GET_BANK_ACCOUNT_TYPE = "select id,name from finance_common.bank_account_type";
	public static final String GET_BANK_ACCOUNT_VARIANT = "select id,name,account_type_id from finance_common.bank_account_variant";
	public static final String GET_ACCOUNT_TYPE_BY_ID = "select name from accounting_types where id=?";
	public static final String GET_EMPLOYEE_TYPE_BY_ID = "select name from employee_type where id=?";

	public static final String GET_CHART_OF_ACCOUNTS_ENTITY = "select id,name,status from chart_of_accounts_entity where status not in ('DEL') ";
	public static final String GET_CHART_OF_ACCOUNTS_ENTITY_FROM_ID = "SELECT name FROM chart_of_accounts_entity where id = ?";
	public static final String GET_CHART_OF_ACCOUNTS_MODULE = "select id,name from chart_of_accounts_module where status not in ('DEL') ";
	public static final String GET_IMPORT_MODULES = "select id,module_name from import_module_type where status in('ACT')";
	public static final String GET_STATE_NAME = "select name from state_list where id=?";
	public static final String GET_COUNTRY_NAME = "select name from country_list where id=?";
	public static final String GET_TDS_TYPE_NAME = "select name from tds where id=?";
	public static final String GET_GST_TYPE_NAME = "select name from gst_treatment where id=?";
	public static final String GET_ORGANIZATION_TYPE_NAME = "select name from organization_constitution where id=?";
	public static final String GET_ACCOUNTING_ENTRY_TYPE_NAME = "select name from accounting_types where id=?";
	// Vendor Portal General Settings
	public static final String GET_BASE_GENERAL_SETTINGS = "select id,name,description from base_general_settings";
	// Vendor Portal Predefined Settings
	public static final String GET_PREDEFINED_SETTING_MODULES = "SELECT id,name FROM base_module_settings";
	public static final String GET_PREDEFINED_SETTING_TEMPLATES = "SELECT id,type,name FROM base_template_settings";
	public static final String GET_PREDEFINED_SETTING_VALIDATION = "SELECT id,activity,validation_rule FROM base_validation_settings";

	// vendor Portal Reasons
	public static final String GET_BASE_REASONS = "SELECT id,name FROM base_reason ";
	public static final String GET_REJECTION_TYPES = "SELECT id,name FROM workflow_rejection_types ";

	public static final String GET_BASE_PAY_TYPE = "select name,description ,parent_name ,debit_or_credit from base_pay_type";

	public static final String GET_BASE_PAY_ITEM = "select name,pay_type_name,ledger_name  from base_pay_item";

	public static final String GET_VOUCHER_TYPE = "select id,name from voucher_type";
	public static final String GET_DOCUMENT_TYPES = "select id,name,module from document_type";

	
	//AR invoices 
	
	public static final String GET_INVOICE_TYPES = "SELECT id,name FROM invoice_type" ;
	public static final String GET_CREDIT_NOTE_TYPE_BY_ID = "SELECT id,name FROM credit_note_type where id=?" ;
	public static final String GET_SUPPLY_SERVICES = "SELECT id,name FROM supply_service" ;
	public static final String GET_SUPPLY_SERVICES_BY_ID = "SELECT id,name FROM supply_service where id=?" ;
	public static final String GET_EXPORT_TYPE = "SELECT id,name FROM export_type" ;
	public static final String GET_PAYMENT_MODES = "select id,name from finance_common.payment_mode";
	public static final String GET_ACCOUNTING_TYPES = "select id from accounting_types where name=?";
	public static final String GET_CREDIT_NOTE_REASONS = "SELECT id,name FROM credit_note_reasons where status not in (?)" ;
	public static final String GET_REFUND_TYPES = "SELECT id,name FROM refund_types where status not in (?)" ;
	public static final String GET_REFUND_TYPE_BY_NAME = "SELECT id,name FROM refund_types where name=?" ;
	public static final String GET_INVOICE_TYPE = "SELECT name FROM finance_common.invoice_type it where it.id=(select invoice_type_id FROM invoice_general_information aigi WHERE id=?)";	
	public static final String GET_RECEIPT_TYPE ="SELECT ID, NAME FROM finance_common.receipt_type ";
	public static final String GET_TDS = "SELECT id,tds_rate_identifier FROM tds";

	
	//Workflow
	public static final String GET_WORFKFLOW_APPROVAL_TYPES = "SELECT id,name FROM workflow_approval_types" ;
	public static final String GET_WORFKFLOW_CONDITIONS = "SELECT id,name FROM workflow_rule_condition WHERE module_name=?" ;
	public static final String GET_WORFKFLOW_CONDITIONS_FOR_MODULE = "SELECT id,name FROM workflow_rule_condition where module_id=?" ;
	public static final String GET_WORFKFLOW_CONDITION_BY_ID = "SELECT id,name FROM workflow_rule_condition where id=?" ;
	public static final String GET_WORFKFLOW_CHOICES_FOR_CONDITION = "SELECT id,name FROM workflow_rule_choice where workflow_rule_condition_id=?" ;
	public static final String GET_WORFKFLOW_MODULES = "SELECT id,name FROM workflow_module where status=?" ;
	public static final String GET_PAYMENT_MODE_BY_ID = "select name from finance_common.payment_mode WHERE id = ?";
	public static final String GET_WORFKFLOW_GENERAL_SETTINGS_CONDITIONS_FOR_MODULE = "SELECT id,name,description FROM workflow_general_setting_condition where module_id=?" ;
	public static final String GET_WORFKFLOW_MODULE_BY_NAME="SELECT id,name FROM workflow_module where name=? and status=?";
	public static final String GET_WORFKFLOW_MODULE_BY_ID="SELECT id,name FROM workflow_module where id=? and status=?";
	public static final String GET_WORFKFLOW_APPROVAL_TYPE_BY_ID = "SELECT id,name FROM workflow_approval_types where id=?" ;
	
	//Reports Period
	
	public static final String GET_REPORTS_PERIOD =  "select id, period_name  from finance_common.gl_transactions_search gts where status = ?" ;
	public static final String GET_PAY_PERIOD_FREQUENCY = "select id, name from pay_period_frequency";
	public static final String GET_PAY_PERIOD_FINANCIAL_YEARS = "select id, name from pay_period_year";

	//Receipts
	public static final String GET_RECEIPT_BULK_DETAIL_TYPES = "SELECT id,name FROM receipt_bulk_detail_type" ;
	public static final String GET_RECEIPT_BULK_DETAIL_TYPE_BY_ID = "SELECT id,name FROM receipt_bulk_detail_type where id=?" ;
	public static final String GET_RECEIPT_BULK_DETAIL_TYPE_BY_NAME = "SELECT id,name FROM receipt_bulk_detail_type where name=?" ;

	// Pay Run
	

	
	
	public static final String GET_SA_CODES = "SELECT id,section_name,heading_id,heading_name,group_id,group_name,name_of_the_service,service_accounting_code,inter_state_gst_rate,intra_state_gst_rate FROM finance_common.sa_code ";

	public static final String GET_HSN_CODES = "select id,commodity_name,hsn_code_number,inter_state_gst_rate,intra_state_gst_rate from  finance_common.hsn_code ";

	public static final String GET_PAID_FOR_TYPES = "select id, name from payroll_type";
	
	public static final String GET_BASE_ENTITY_ID="select chart_of_accounts_entity_id from finance_common.chart_of_accounts_level5 where name=?";
	
}

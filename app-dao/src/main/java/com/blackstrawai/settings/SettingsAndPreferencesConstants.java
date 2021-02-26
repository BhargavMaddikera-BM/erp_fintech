package com.blackstrawai.settings;

public class SettingsAndPreferencesConstants {

	public static final String CATEGORY_GOODS = "goods";
	
	public static final String  CATEGORY_SERVICE = "services";

	public static final String  CATEGORY_FIXEDASSETS = "fixedAsset";

	public static final String INSERT_INTO_PAYMENT_TERMS_ORGANIZATION = "insert into payment_terms_organization "
			+ "(name,description,base_date,days_limit,account_type,organization_id,user_id,isSuperAdmin,is_base,role_name)"
			+ "values(?,?,?,?,?,?,?,?,?,?);";	

		
	public static final String UPDATE_PAYMENT_TERMS_ORGANIZATION = "update payment_terms_organization set name=?,description=?,base_date=?,days_limit=?,"
			+ "account_type=?,organization_id=?,isSuperAdmin=?,update_ts=?,status=?,update_user_id=?,update_role_name=? where id=?";
	public static final String DELETE_PAYMENT_TERMS_ORGANIZATION = "update payment_terms_organization set status=?,update_ts=?,update_user_id=?,update_role_name=? where id=?";
	/*public static final String GET_PAYMENT_TERMS_BY_ORGANIZATION = "select id,name,description,base_date,days_limit,account_type,organization_id,"
			+ "user_id,isSuperAdmin,status,create_ts from payment_terms_organization where organization_id=?";*/
	
	public static final String GET_PAYMENT_TERMS_BY_ORGANIZATION_USER_ROLE = "select id,name,description,base_date,days_limit,account_type,organization_id,"
			+ "user_id,isSuperAdmin,status,create_ts from payment_terms_organization where organization_id=? and user_id=? and role_name=?";
	
	public static final String GET_PAYMENT_TERMS_ORGANIZATION = "select id,name,description,base_date,days_limit,account_type,organization_id,"
			+ "user_id,isSuperAdmin,status,create_ts from payment_terms_organization where organization_id=?";
	
	public static final String GET_PAYMENT_TERMS_BY_ID_ORGANIZATION = "select id,name,description,base_date,days_limit,account_type,organization_id,user_id,"
			+ "isSuperAdmin,status,create_ts from payment_terms_organization where id=?";
	public static final String GET_MINIMAL_CURRENCY_ORGANIZATION = "select id,name,symbol ,alternate_symbol, decimal_value_denoter from currency_organization "
			+ "where organization_id=? and status not in(?)";
	public static final String GET_MINIMAL_PAYMENT_TERMS_ORGANIZATION = "select id,name,days_limit from payment_terms_organization "
			+ " where organization_id=?";
	public static final String GET_VOUCHER_DETAILS_ORGANIZATION = "select id,prefix,suffix,minimum_digits from voucher_management_organization"
			+ " where organization_id=? and type=?";
	public static final String GET_SHIPPING_PREFERENCE_ORGANIZATION = "select id,name from shipping_preference_organization "
			+ "where organization_id=?";
	public static final String GET_PRODUCT_PRGANIZATION = "select id,name,hsn,purchase_account_id ,minimim_order_quantity,unit_measure_id,unit_price_purchase from"
			+ " product_organization where organization_id=?";

	public static final String GET_PRODUCT_ORGANIZATION_WITH_TAX = "SELECT id,type,name,hsn,unit_measure_id,tax_group_id_inter,tax_group_id_intra,unit_price_purchase,purchase_account_id,purchase_account_level,purchase_account_name,minimim_order_quantity,sales_account_id,sales_account_level,sales_account_name,quantity,unit_price_sale,description  FROM product_organization where organization_id = ? and status not in ('DEL')";

	public static final String INSERT_INTO_VOUCHER_MANAGEMENT_ORGANIZATION = "insert into voucher_management_organization"
			+ "(type,description,prefix,suffix,organization_id,user_id,isSuperAdmin,name,minimum_digits,minimum_number_range,maximum_number_range,alert_value,is_base,role_name )values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

	public static final String INSERT_INTO_CURRENCY_ORGANIZATION = "insert into currency_organization"
			+ "(name,description,symbol,alternate_symbol,is_space_required,is_millions,number_of_decimal_places,"
			+ "decimal_value_denoter,no_of_decimals_for_amount_in_words,exchange_value,create_ts,organization_id,user_id,isSuperAdmin,value_format,is_base)"
			+ "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	public static final String UPDATE_CURRENCY_ORGANIZATION = "update currency_organization set "
			+ "name=?,description=?,symbol=?,alternate_symbol=?,is_space_required=?,is_millions=?,number_of_decimal_places=?,"
			+ "decimal_value_denoter=?,no_of_decimals_for_amount_in_words=?,exchange_value=?,update_ts=? where id=?";
	public static final String CHECK_CURRENCY_EXIST_ORGANIZATION = "select * from currency_organization where organization_id=? "
			+ "and name=? and status not in(?)";
	public static final String DELETE_CURRENCY_ORGANIZATION = "update currency_organization set status=?,update_ts=? where id=?";
	public static final String GET_CURRENCIES_ORGANIZATION = "select id,name,description,symbol,alternate_symbol,is_space_required,"
			+ "is_millions,number_of_decimal_places,decimal_value_denoter,no_of_decimals_for_amount_in_words,exchange_value,"
			+ "create_ts timestamp,update_ts timestamp,organization_id,user_id,isSuperAdmin,status from currency_organization "
			+ " where organization_id=? and status not in(?)";
	public static final String GET_CURRENCY_ORGANIZATION = "select id,name,description,symbol,alternate_symbol,is_space_required,is_millions,"
			+ "number_of_decimal_places,decimal_value_denoter,no_of_decimals_for_amount_in_words,exchange_value,"
			+ "create_ts timestamp,update_ts timestamp,organization_id,user_id,isSuperAdmin,status from currency_organization  where id=?";

	public static final String GET_VENDOR_GROUPS_ORGANIZATION = "select id,name from vendor_group_organization where organization_id=?";
	// Shipping preferences
	public static final String INSERT_SHIPPING_PREFERENCES_ORGANIZATION = "insert into shipping_preference_organization(name,mode,description,organization_id,user_id,isSuperAdmin,role_name)values(?,?,?,?,?,?,?);";
/*	public static final String GET_SHIPPING_PREFERENCES_ORGANIZATION = "select id,name,mode,description,organization_id,user_id,isSuperAdmin,status,create_ts from shipping_preference_organization where organization_id=?";
*/	
	public static final String GET_SHIPPING_PREFERENCES_ORGANIZATION_USER_ROLE = "select id,name,mode,description,organization_id,user_id,isSuperAdmin,status,create_ts from shipping_preference_organization where organization_id=? and user_id=? and role_name=?";
	public static final String GET_SHIPPING_PREFERENCES_ORGANIZATION = "select id,name,mode,description,organization_id,user_id,isSuperAdmin,status,create_ts from shipping_preference_organization where organization_id=?";

	
	public static final String GET_SHIPPING_PREFERENCES_BY_ID_ORGANIZATION = "select id,name,mode,description,organization_id,user_id,isSuperAdmin,status,create_ts from shipping_preference_organization where id=?";
	public static final String DELETE_SHIPPING_PREFERENCE_ORGANIZATION = "update shipping_preference_organization set status=?,update_ts=?,update_user_id=?,update_role_name=? where id=?";
	public static final String UPDATE_SHIPPING_PREFERENCE_ORGANIZATION = "update shipping_preference_organization set name=?,description=?,mode=?,organization_id=?,update_user_id=?,isSuperAdmin=?,status=?,update_ts=?,update_role_name=? where id=?";

	public static final String CHECK_SHIPPING_PREFERENCE_EXIST_ORGANIZATION = "select * from shipping_preference_organization"
			+ " where organization_id=? and name=?";

	public static final String CHECK_PAYMENT_TERMS_EXIST_ORGANIZATION = "select * from payment_terms_organization "
			+ "where organization_id=? and name=?";

	public static final String CHECK_VOUCHER_EXIST_ORGANIZATION = "select * from voucher_management_organization where "
			+ "organization_id=? and name=?";
	public static final String GET_VOUCHERS_ORGANIZATION = "select id,type,name,description,prefix,suffix,minimum_digits,minimum_number_range,maximum_number_range,alert_value,"
			+ "organization_id,user_id,isSuperAdmin,status,create_ts from voucher_management_organization where "
			+ "organization_id=?";
	
	public static final String GET_VOUCHERS_ORGANIZATION_USER_ROLE = "select id,type,name,description,prefix,suffix,minimum_digits,minimum_number_range,maximum_number_range,alert_value,"
			+ "organization_id,user_id,isSuperAdmin,status,create_ts from voucher_management_organization where "
			+ "organization_id=? and user_id=? and role_name=?";
		
	public static final String GET_VOUCHER_BY_ID_ORGANIZATION = "select id,type,name,description,prefix,suffix,minimum_digits,minimum_number_range,maximum_number_range,alert_value,"
			+ "organization_id,user_id,isSuperAdmin,status,create_ts from voucher_management_organization where id=?";
	public static final String DELETE_VOUCHER_ORGANIZATION = "update voucher_management_organization set status=?,update_ts=?,update_user_id=?,update_role_name=? where id=?";
	public static final String UPDATE_VOUCHER_ORGANIZATION = "update voucher_management_organization set type=?,name=?,description=?,prefix=?,"
			+ "suffix=?,minimum_digits=?,minimum_number_range =?,maximum_number_range =?,alert_value =?,organization_id=?,update_user_id=?,isSuperAdmin=?,status=?,update_ts=?,update_role_name=? "
			+ "where id=?";

	public static final String INSERT_INTO_PRODUCT_MANAGEMENT_ORGANIZATION = "insert into product_organization(type,product_id,name,hsn,description,unit_measure_id,default_tax_preference,tax_group_id_inter,tax_group_id_intra,unit_price_purchase,purchase_account_id,purchase_account_level,purchase_account_name,minimim_order_quantity,sales_account_id,sales_account_level,sales_account_name,quantity,unit_price_sale,organization_id,user_id,isSuperAdmin,role_name,mrp_purchase,mrp_sales,category,opening_stock_quantity,stock_value_currency_id,opening_stock_value,show_purchase_ledger,show_sales_ledger,show_inventory_mgmt)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	public static final String CHECK_PRODUCT_EXIST_ORGANIZATION = "select * from product_organization "
			+ "where organization_id=? and product_id=?";
	public static final String GET_PRODUCT_ORGANIZATION = "select id,product_id,name,unit_price_purchase,unit_price_sale,organization_id,user_id,isSuperAdmin,status from product_organization where organization_id=?";
	
	public static final String GET_PRODUCT_ORGANIZATION_USER_ROLE = "select id,product_id,name,unit_price_purchase,unit_price_sale,organization_id,user_id,isSuperAdmin,status from product_organization where organization_id=? and user_id=? and role_name=?";

	public static final String GET_PRODUCT_BY_ID_ORGANIZATION = "select id,type,product_id,name,hsn,description,unit_measure_id,default_tax_preference,tax_group_id_inter,tax_group_id_intra,unit_price_purchase,purchase_account_id,purchase_account_level,purchase_account_name,minimim_order_quantity,sales_account_id,sales_account_level,sales_account_name,quantity,unit_price_sale,organization_id,user_id,isSuperAdmin,status,mrp_purchase,mrp_sales,category,opening_stock_quantity,stock_value_currency_id,opening_stock_value,show_purchase_ledger,show_sales_ledger,show_inventory_mgmt from product_organization where id=?";
	public static final String DELETE_PRODUCT_ORGANIZATION = "update product_organization set status=?,update_ts=?,update_user_id=?,update_role_name=? where id=?";
	public static final String UPDATE_PRODUCT_ORGANIZATION = "update product_organization set type=?,product_id=?,name=?,hsn=?,description=?,unit_measure_id=?,default_tax_preference=?,tax_group_id_inter=?,tax_group_id_intra=?,unit_price_purchase=?,purchase_account_id=?,purchase_account_level=?,purchase_account_name=?,minimim_order_quantity=?,sales_account_id=?,sales_account_level=?,sales_account_name=?,quantity=?,unit_price_sale=?,organization_id=?,update_user_id=?,isSuperAdmin=?,update_ts=?,update_role_name=?,mrp_purchase=?,mrp_sales=?,category=?, opening_stock_quantity=?, stock_value_currency_id=?, opening_stock_value=? ,show_purchase_ledger=? ,show_sales_ledger=?, show_inventory_mgmt=? where id=?";

	public static final String GET_EMPLOYEES_ORGANIZATION = "select id,first_name,last_name from employee where organization_id=? and status not in(?)";
	public static final String GET_ALL_ACTIVE_EMPLOYEES_ORGANIZATION = "select id,first_name,last_name from employee where organization_id=? and status in(?)";
	public static final String GET_DEPARTMENT_ORGANIZATION_NO_DELETE = "select id,name from department_organization  where organization_id=? and status not in(?)";

	public static final String GET_DEPARTMENT_ORGANIZATION = "select id,name from department_organization  where organization_id=?";

	// Vendor Group
	public static final String INSERT_INTO_VENDOR_GROUP_ORGANIZATION = "insert into vendor_group_organization (name,description,organization_id,user_id,isSuperAdmin,role_name)values(?,?,?,?,?,?)";
	public static final String CHECK_VENDOR_GROUP_EXIST_ORGANIZATION = "select * from vendor_group_organization where name=? and organization_id=?";
	
	public static final String GET_VENDOR_GROUP_ORGANIZATION = "select id,name,description,organization_id,user_id,isSuperAdmin,status,create_ts,update_ts from vendor_group_organization where organization_id=?";
	public static final String GET_VENDOR_GROUP_ORGANIZATION_USER_ROLE = "select id,name,description,organization_id,user_id,isSuperAdmin,status,create_ts,update_ts from vendor_group_organization where organization_id=? and user_id=? and role_name=?";

	
	public static final String GET_VENDOR_GROUP_BY_ID_ORGANIZATION = "select id,name,description,organization_id,user_id,isSuperAdmin,status,create_ts,update_ts from vendor_group_organization where id=?";
	public static final String DELETE_VENDOR_GROUP_ORGANIZATION = "update vendor_group_organization set status=?,update_ts=?,update_user_id=?,update_role_name =? where id=?";
	public static final String UPDATE_VENDOR_GROUP_ORGANIZATION = "update vendor_group_organization set name=?,description=?,organization_id=?,update_user_id=?,isSuperAdmin=?,update_ts=?,status=?,update_role_name=? where id=?";

	// Customer Group
	public static final String INSERT_INTO_CUSTOMER_GROUP_ORGANIZATION = "insert into customer_group_organization(name,description,organization_id,user_id,isSuperAdmin,role_name)values(?,?,?,?,?,?)";
	public static final String CHECK_CUSTOMER_GROUP_EXIST_ORGANIZATION = "select * from customer_group_organization where name=? and organization_id=?";
	
	public static final String GET_CUSTOMER_GROUP_ORGANIZATION = "select id,name,description,organization_id,user_id,isSuperAdmin,status,create_ts,update_ts from customer_group_organization where organization_id=?";
	public static final String GET_CUSTOMER_GROUP_ORGANIZATION_USER_ROLE = "select id,name,description,organization_id,user_id,isSuperAdmin,status,create_ts,update_ts from customer_group_organization where organization_id=? and user_id=? and role_name=?";

	
	public static final String GET_CUSTOMER_GROUP_BY_ID_ORGANIZATION = "select id,name,description,organization_id,user_id,isSuperAdmin,status,create_ts,update_ts from customer_group_organization where id=?";
	public static final String DELETE_CUSTOMER_GROUP_ORGANIZATION = "update customer_group_organization set status=?,update_ts=?,update_user_id=?,update_role_name=? where id=?";
	public static final String UPDATE_CUSTOMER_GROUP_ORGANIZATION = "update customer_group_organization set name=?,description=?,organization_id=?,isSuperAdmin=?,update_user_id=?,update_ts=?,status=?,update_role_name=? where id=?";
	public static final String GET_MINIMAL_CUSTOMER_GROUPS_ORGANIZATION = "select id,name from customer_group_organization where organization_id=?";

	// Department
	public static final String INSERT_INTO_DEPARTMENT_ORGANIZATION = "insert into department_organization(name,description,organization_id,user_id,isSuperAdmin,role_name)values(?,?,?,?,?,?)";
	public static final String CHECK_DEPARTMENT_EXIST_ORGANIZATION = "select * from department_organization where name=? and organization_id=?";
	public static final String GET_DEPARTMENT_BY_ORGANIZATION = "select id,name,description,organization_id,user_id,isSuperAdmin,status,create_ts,update_ts from department_organization where organization_id=?";
	
	public static final String GET_DEPARTMENT_BY_ORGANIZATION_USER_ROLE = "select id,name,description,organization_id,user_id,isSuperAdmin,status,create_ts,update_ts from department_organization where organization_id=? and user_id=? and role_name=?";

	
	public static final String GET_DEPARTMENT_BY_ID_ORGANIZATION = "select id,name,description,organization_id,user_id,isSuperAdmin,status,create_ts,update_ts from department_organization where id=?";
	public static final String DELETE_DEPARTMENT_ORGANIZATION = "update department_organization set status=?,update_ts=?,update_user_id=?,update_role_name=? where id=?";
	public static final String UPDATE_DEPARTMENT_ORGANIZATION = "update department_organization set name=?,description=?,organization_id=?,isSuperAdmin=?,update_user_id=?,update_ts=?,status=?,update_role_name=? where id=?";

	public static final String INSERT_INTO_TAX_RATE_TYPE_ORGANIZATION = "insert into tax_rate_type_organization(type,usage_type,is_base,organization_id,user_id,isSuperAdmin,is_inter)values(?,?,?,?,?,?,?)";
	public static final String INSERT_INTO_TAX_RATE_MAPPING_ORGANIZATION = "insert into tax_rate_mapping_organization(name,tax_rate_type_id,rate,is_base,organization_id,user_id,isSuperAdmin,is_inter,role_name)values(?,?,?,?,?,?,?,?,?)";
	public static final String INSERT_INTO_TAX_GROUP_ORGANIZATION = "insert into tax_group_organization(name,taxes_included,combined_rate,is_inter,is_base,organization_id,user_id,isSuperAdmin,role_name)values(?,?,?,?,?,?,?,?,?)";
	public static final String GET_TAX_GROUP_ORGANIZATION = "select id,name,taxes_included,combined_rate from tax_group_organization where organization_id =?";
	public static final String GET_MINIMAL_TAX_RATE_FOR_ORG ="SELECT name, rate FROM tax_rate_mapping_organization where organization_id = ? " ;
	public static final String GET_TAX_TYPE_BY_TAX_RATE = "SELECT trm.name , trt.type , trm.rate FROM usermgmt.tax_rate_mapping_organization trm " + 
			"join usermgmt.tax_rate_type_organization trt on trt.id = trm.tax_rate_type_id where trm.organization_id = ? ";
	/*
	 * public static final String
	 * GET_BASE_CHART_OF_ACCOUNTS="select account_variant_level_1," +
	 * "account_type_level_2,account_group_level_3,account_name_level_4,ledger_name_level_5,sub_ledger_name_level_6,account_code,"
	 * +
	 * "module,account_entries,mandatory_sub_ledger_creation,company_type from base_chart_of_accounts"
	 * ;
	 * 
	 * public static final String
	 * INSERT_INTO_CHART_OF_ACCOUNTS_ORGANIZATION="insert into chart_of_accounts_organization("
	 * +
	 * "account_variant_level_1,account_type_level_2,account_group_level_3,account_name_level_4,"
	 * +
	 * "ledger_name_level_5,sub_ledger_name_level_6,account_code,module,account_entries,mandatory_sub_ledger_creation,"
	 * +
	 * "company_type,organization_id ,user_id,isSuperAdmin)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
	 * ; public static final String
	 * INSERT_INTO_CHART_OF_ACCOUNTS_ORGANIZATION_ACCOUNTING_TYPE="insert into chart_of_accounts_accounting_type_organization("
	 * +
	 * "account_variant_level_1,account_type_level_2,account_group_level_3,account_name_level_4,"
	 * +
	 * "ledger_name_level_5,sub_ledger_name_level_6,account_code,module,account_entries,mandatory_sub_ledger_creation,"
	 * +
	 * "company_type,organization_id ,user_id,isSuperAdmin,accounting_type_id)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
	 * ;
	 */
	// Unit of Measure

	public static final String GET_TAX_RATE_TYPE_ORGANIZATION = "select id,type from tax_rate_type_organization where organization_id=? and status=?";
	public static final String GET_TAX_RATE_TYPE_BY_ID = "select id,is_base,type,status,organization_id,user_id,isSuperAdmin,create_ts,is_inter,"
			+ "update_ts,type,usage_type from tax_rate_type_organization where id=?";
	public static final String CHECK_TAX_RATE_ORGANIZATION = "select * from tax_rate_mapping_organization "
			+ "where organization_id=? and name=?";
	public static final String UPDATE_TAX_RATE_ORGANIZATION = "update tax_rate_mapping_organization set name=?,tax_rate_type_id=?,rate=?,is_base=?,"
			+ "status=?,organization_id=?,update_user_id=?,isSuperAdmin=?,update_ts=?,is_inter=?,update_role_name=? where id=?";
	public static final String CHANGE_TAX_RATE_STATUS_ORGANIZATION = "update tax_rate_mapping_organization set status=?,update_ts=?,update_user_id=?,update_role_name=? where id=?";
	
	public static final String GET_TAX_RATE_ORGANIZATION = "select id,name,tax_rate_type_id,rate,is_base,status,organization_id,"
			+ "user_id,isSuperAdmin,create_ts,(select type from tax_rate_type_organization where id=tr.tax_rate_type_id) as tax_rate_type_name "
			+ "from tax_rate_mapping_organization tr where organization_id=?  ORDER BY create_ts DESC";
	
	
	public static final String GET_TAX_RATE_ORGANIZATION_USER_ROLE = "select id,name,tax_rate_type_id,rate,is_base,status,organization_id,"
			+ "user_id,isSuperAdmin,create_ts,(select type from tax_rate_type_organization where id=tr.tax_rate_type_id) as tax_rate_type_name "
			+ "from tax_rate_mapping_organization tr where organization_id=? and user_id=? and role_name=?  ORDER BY create_ts DESC";
	
	public static final String GET_TAX_RATE_BY_ID_ORGANIZATION = "select id,name,tax_rate_type_id,rate,is_base,status,organization_id,"
			+ "user_id,isSuperAdmin,create_ts,(select type from tax_rate_type_organization where id=tr.tax_rate_type_id) as tax_rate_type_name "
			+ "from tax_rate_mapping_organization tr where id=? ";
	public static final String GET_TAX_RATE_BY_NAME_ORGANIZATION = "select id,name,tax_rate_type_id,rate,is_base,status,organization_id,"
			+ "user_id,isSuperAdmin,create_ts,(select type from tax_rate_type_organization where id=tr.tax_rate_type_id) as tax_rate_type_name "
			+ "from tax_rate_mapping_organization tr where organization_id=? and name=? ";
	public static final String CHECK_TAX_GROUP_ORGANIZATION = "select * from tax_group_organization "
			+ "where organization_id=? and name=?";
	public static final String UPDATE_TAX_GROUP_ORGANIZATION = "update tax_group_organization set name=?,taxes_included=?,combined_rate=?,is_inter=?,is_base=?,"
			+ "status=?,organization_id=?,update_user_id=?,isSuperAdmin=?,update_ts=?,update_role_name=? where id=?";
	public static final String CHANGE_TAX_GROUP_STATUS_ORGANIZATION = "update tax_group_organization set status=?,update_ts=?,update_user_id=?,update_role_name=? where id=?";
	
	public static final String GET_TAX_GROUP_BY_ORGANIZATION = "select id,name,taxes_included,combined_rate,is_inter,is_base,status,organization_id,"
			+ "user_id,isSuperAdmin,create_ts from tax_group_organization where organization_id=? ORDER BY create_ts DESC";
	
	
	public static final String GET_TAX_GROUP_BY_ORGANIZATION_USER_ROLE = "select id,name,taxes_included,combined_rate,is_inter,is_base,status,organization_id,"
			+ "user_id,isSuperAdmin,create_ts from tax_group_organization where organization_id=? and user_id=? and role_name=? ORDER BY create_ts DESC";
	
	public static final String GET_TAX_GROUP_BY_ID_ORGANIZATION = "select id,name,taxes_included,combined_rate,is_inter,is_base,status,organization_id,"
			+ "user_id,isSuperAdmin,create_ts from tax_group_organization where id=?";
	public static final String GET_ALL_TAX_RATE_GROUP_ORGANIZATION = "select id,name,combined_rate,is_inter from tax_group_organization where organization_id=?  and status not in('DEL')";

	public static final String GET_INTER_TAX_GROUP_ORGANIZATION = "select id,name,taxes_included,combined_rate from tax_group_organization where organization_id=? and status not in(?) and is_inter=?";




	public static final String GET_LOCATION_ID_FROM_GST_DETAILS_ORGANIZATION = "select id from gst_details where organization_id = ? and gst_no = ? ";
	public static final String GET_LOCATION_ID_FROM_GST_LOCATION_ORGANIZATION = "SELECT id FROM gst_location where organization_id = ? and gst_no = ? ";
	public static final String GET_LOCATION_ID_FROM_ORG_ID_ORGANIZATION = "select id from gst_details where organization_id = ? limit 1";

	
	public static final String GET_DEPARTMENT_TYPE_BY_ID = "select name from department_organization where id=?";

	
	// To get level by Org Id

	
	public static final String GET_PAYMENT_TERM_NAME = "select name from payment_terms_organization where id=? and organization_id=?";
	public static final String GET_CURRENCY_NAME = "select name from currency_organization where id=? and organization_id=?";
	public static final String GET_VENDOR_GROUP_NAME = "select name from vendor_group_organization where id=? and organization_id=?";
	public static final String GET_CUSTOMER_GROUP_NAME = "select name from customer_group_organization where id=? and organization_id=?";

	
	public static final String GET_LOCATION_ID_FROM_GST_DETAILS = "select id from gst_details where organization_id=?";
	public static final String GET_LOCATION_NAME_FROM_GST_LOCATION = "select location_name from gst_location where organization_id=? and id=?";
	public static final String GET_VENDOR_GROUP_NAMES_OF_AN_ORGANIZATION = "select id,name from vendor_group_organization where organization_id=?";
	public static final String GET_CURRENCY_NAME_GIVEN_CURRENCY_ID = "select name from currency_organization where id=?";
	public static final String GET_CURRENCY_ID = "select id from currency_organization where organization_id=? and name=?";
	
	public static final String GET_BASIC_TAX_RATES_FOR_ORG = "SELECT id,name FROM tax_rate_mapping_organization where organization_id = ? ";

	public static final String GET_VENDOR_GROUP_ID = "select id from vendor_group_organization where settings_id=?";
	public static final String UPDATE_VENDOR_GROUP_SETTINGS = "update vendor_group_organization set settings_data=?,settings_name=? where settings_id=?";
	public static final String GET_VENDOR_GROUP_SETTINGS = "select id,name,settings_status,settings_name from vendor_group_organization where organization_id=? order by update_ts desc";
	public static final String ACTIVE_DEACTIVE_VENDOR_GROUP_SETTINGS = "update vendor_group_organization set settings_status=? where id=?";
	public static final String GET_VENDOR_GROUP_SETTINGS_BY_ID = "select name,id,settings_status,settings_name,settings_id,settings_data from vendor_group_organization where id=? and organization_id=?";
    public static final String UPDATE_VENDOR_GROUP_SETTINGS_BY_ID = "update vendor_group_organization set settings_name=?,settings_id=?,settings_status=?,settings_data=? where id=?";

    
    //statutory body query
    
    public static final String INSERT_STATUTORY_BODY = "insert into statutory_body "
			+ "(name,department_name,type,registration_no,date,state,city,addr_1,addr_2,pincode,website,status,organization_id,user_id,role_name,default_gl_id,default_gl_name)"
			+ "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
    
    
    public static final String GET_STATUTORY_BODY_LIST_ORGANIZATION_NAME = "select id,name,department_name,type,registration_no,date,state,city,addr_1,addr_2,"
    		+ "pincode,website,status,organization_id,user_id,role_name from statutory_body where organization_id=? and name=?";
  
    public static final String GET_STATUTORY_BODY_LIST_ORGANIZATION = "select id,name,department_name,type,registration_no,date,state,city,addr_1,addr_2,"
    		+ "pincode,website,status,organization_id,user_id,role_name from statutory_body where organization_id=?";
    
    public static final String GET_BASIC_STATUTORY_BODY_LIST_ORGANIZATION = "select id,name from statutory_body where organization_id=? AND STATUS=?";
    
    public static final String GET_STATUTORY_BODY_LIST_ORGANIZATION_USER_ROLE = "select id,name,department_name,type,registration_no,date,state,city,addr_1,addr_2,"
    		+ "pincode,website,status,organization_id,user_id,role_name from statutory_body where organization_id=? and user_id=? and role_name=?";
    
    public static final String ACTIVATE_DEACTIVATE_STATUTORY_BODY = "update statutory_body set status=?,update_ts=?,update_user_id=?,update_role_name=?  where id=?";
	    
    public static final String UPDATE_STATUTORY_BODY = "update statutory_body set name=?,department_name=?,type=?,registration_no=?,"
			+ "update_ts=?,state=?,city=?,addr_1=?,addr_2=?,pincode=?,website=?,status=?,date=?,update_user_id=?,update_role_name=?,default_gl_id=?,default_gl_name=? where id=?";

    
    public static final String GET_STATUTORY_BODY_BY_ID = "select id,name,department_name,type,registration_no,date,state,city,addr_1,addr_2,"
    		+ "pincode,website,status,organization_id,user_id,role_name,update_user_id,update_role_name,default_gl_id,default_gl_name from statutory_body where id=?";

	public static final String GET_PRODUCT_BY_NAME = "select id,type,product_id,name,hsn,description,unit_measure_id,default_tax_preference,tax_group_id_inter,tax_group_id_intra,unit_price_purchase,purchase_account_id,purchase_account_level,purchase_account_name,minimim_order_quantity,sales_account_id,sales_account_level,sales_account_name,quantity,unit_price_sale,organization_id,user_id,isSuperAdmin,status from product_organization where organization_id=? and name=?";

	public static final String CHECK_PAY_PERIOD_EXIST_ORGANIZATION = "select * from pay_period where organization_id=? and period=?";

	public static final String INSERT_INTO_PAY_PERIOD_ORGANIZATION = "insert into pay_period (name, custom, start_date,end_date, cycle, financial_year, status, organization_id, user_id, role_name, create_ts) values (?,?,?,?,?,?,?,?,?,?,?)";

	public static final String GET_PAY_PERIOD_BY_ORGANIZATION = "select f.name, f.cycle, f.start_date, f.end_date, f.status, f.id FROM pay_period_frequency f, pay_period p WHERE f.pay_period_id = p.id AND p.organization_id = ?";

	public static final String GET_PAY_PERIOD_BY_ORGANIZATION_USER_ROLE = "select f.name, f.cycle, f.start_date, f.end_date, f.status, f.id FROM pay_period_frequency f, pay_period p WHERE f.pay_period_id = p.id AND p.organization_id = ? AND  p.user_id = ? AND p.role_name = ? ";

	public static final String GET_PAY_PERIOD_BY_ID_ORGANIZATION = "select p.id, p.period, p.organization_id, p.role_name, p.update_role_name, " 
			+ "p.update_user_id, p.user_id, f.id, f.pay_period_id, f.name, f.cycle, f.start_date, f.end_date, f.status, f.pay_frequency_type FROM pay_period_frequency f, pay_period p "
			+ "WHERE f.pay_period_id = p.id AND f.id = ?";

	public static final String DELETE_PAY_PERIOD_FREQUENCY = "update pay_period_frequency set status=? where id=?";

	public static final String UPDATE_PAY_PERIOD = "update pay_period_frequency set name=?, cycle =?, start_date =?, end_date =?, status =? where id=?";

	public static final String INSERT_INTO_SETTINGS_MODULE_ORGANIZATION = "insert into settings_module_organization  (type,name,module_name,sub_module_name,is_required,organization_id,user_id,role_name) values (?,?,?,?,?,?,?,?)";

	public static final String GET_SETTINGS_MODULE_ORGANIZATION_BY_SUB_MODULE="select id,type,name,module_name,sub_module_name,is_required,organization_id,user_id,role_name from settings_module_organization where organization_id=? and sub_module_name=?";


	public static final String INSERT_INTO_PAY_PERIOD_FREQUENCY_ORGANIZATION = "INSERT INTO pay_period (period, organization_id, user_id, role_name, create_ts, status) VALUES (?,?,?,?,?,?)";
	
	public static final String CHECK_PAY_PERIOD_FREQUENCY_EXIST_ORGANIZATION = "select * from pay_period p, pay_period_frequency f where p.id = f.pay_period_id and p.organization_id=? and p.period=? and f.name=?";


	public static final String INSERT_INTO_PAY_PERIOD_FREQUENCY_CHILD_ORGANIZATION = "insert into pay_period_frequency (name, cycle, start_date, end_date, pay_period_id, pay_frequency_type, status) values (?,?,?,?,?,?,?)";


	public static final String DELETE_PAY_PERIOD = "update pay_period set update_user_id=?, update_role_name=?, update_ts=? where id=?";


	public static final String GET_PAY_PERIOD_CYCLES = "select id, name, cycle, start_date, end_date from pay_cycle where organization_id = ?";

	
	public static final String INSERT_INTO_PAY_CYCLE_ORGANIZATION = "insert into payroll.pay_cycle (name, cycle, start_date, end_date, status, organization_id, user_id, role_name, create_ts)" +
	"values (?,?,?,?,?,?,?,?,?)";


	public static final String CHECK_PAY_CYCLE_EXIST_ORGANIZATION = "select * from pay_cycle where organization_id = ? and name = ?";


	public static final String GET_PAY_CYCLE_BY_ORGANIZATION = "select name, cycle, start_date, end_date, status, id from pay_cycle where organization_id = ? and status=?";


	public static final String GET_PAY_CYCLE_BY_ORGANIZATION_USER_ROLE = "select name, cycle, start_date, end_date, status, id from pay_cycle where organization_id = ?  and status=? and user_id = ? and role_name = ?";


	public static final String GET_PAY_CYCLE_BY_ID_ORGANIZATION = "select name, cycle, start_date, end_date, status, id, organization_id, user_id, role_name, update_user_id, update_role_name from pay_cycle where id = ?";


	public static final String UPDATE_PAY_CYCLE = "update pay_cycle set name=?, cycle=?, start_date=?, end_date=?, status=?, "
			+ "update_user_id=?, update_role_name=? where id=?";
	public static final String DELETE_PAY_CYCLE = "update pay_cycle set status=?, update_user_id=?, update_role_name=? where organization_id=? and id=?";

	public static final String GET_PAY_RUN_PERIOD_FREQUENCY_ID_FOR_ORG="SELECT id,period from payroll.pay_period pp where organization_id = ? and status = ?";

	public static final String GET_PAY_RUN_PERIOD_FREQUENCY = "select id, name,cycle,start_date ,end_date , pay_frequency_type \r\n" + 
			"from payroll.pay_period_frequency ppf where pay_period_id = ? and status = ?";

	public static final String INSERT_INTO_PRODUCT_CATEGORY="insert into inventory_mgmt.PRODUCT_CATEGORY(category_name,type,user_id,organization_id,role_name)values(?,?,?,?,?)";

	public static final String UPDATE_PRODUCT_CATEGORY = "update inventory_mgmt.PRODUCT_CATEGORY set category_name=? ,type =? ,UPDATE_USER_ID = ? ,UPDATE_ROLE_NAME = ? ,UPDATE_TS=? where id = ? ";
	
	public static final String GET_PRODUCT_CATEGORY = "select id, category_name , type from  inventory_mgmt.PRODUCT_CATEGORY  where ORGANIZATION_ID = ? and type = ?";
	
	public static final String CHECK_CATEGORY_EXIST = "select category_name from  inventory_mgmt.PRODUCT_CATEGORY  where  ORGANIZATION_ID = ? and type = ? and category_name = ? ";
	
	public static final String GET_PRODUCT_CATEGORY_NAME = "select  category_name  from  inventory_mgmt.PRODUCT_CATEGORY  where id= ?";

	public static final String GET_ACTIVE_STATUTORY_BODY_LIST_ORGANIZATION = "select id,name"
    		+ " from statutory_body where organization_id=? and status = ?";


	public static final String GET_ACTIVE_STATUTORY_BODY_LIST_ORGANIZATION_USER_ROLE = "select id,name"
    		+ " from statutory_body where organization_id=? and status = ? and user_id=? and role_name=?";

//TDS query
	
	public static final String CHECK_TDS_EXIST_ORGANIZATION = "select * from tds_organization "
			+ "where organization_id=? and name=?";
	
	public static final String INSERT_INTO_TDS_ORGANIZATION = "insert into tds_organization "
			+ "(name,description,applicable_for,tds_rate_percentage,status,tds_rate_identifier,organization_id,user_id,role_name)"
			+ "values(?,?,?,?,?,?,?,?,?);"	;

	public static final String DELETE_TDS_ORGANIZATION = "update tds_organization set status=?,update_ts=?,update_user_id=?,update_role_name=? where id=?";
	
	public static final String UPDATE_TDS_ORGANIZATION = "update tds_organization set name=?,description=?,applicable_for=?,tds_rate_percentage=?,"
			+ "organization_id=?,tds_rate_identifier=?,update_ts=?,status=?,update_user_id=?,update_role_name=? where id=?";

	public static final String GET_TDS_BY_ID_ORGANIZATION = "select id,name,tds_rate_identifier,tds_rate_percentage,description,applicable_for,create_ts,status,organization_id,user_id,"
			+ "role_name,create_ts from tds_organization where id=?";

	public static final String GET_TDS_ORGANIZATION = "select id,name,description,applicable_for,tds_rate_percentage,status,tds_rate_identifier,organization_id,"
			+ "user_id,create_ts from tds_organization where organization_id=?";
	
	public static final String GET_TDS_BY_ORGANIZATION_USER_ROLE = "select id,name,description,applicable_for,tds_rate_percentage,status,tds_rate_identifier,organization_id,"
			+ "user_id,create_ts from tds_organization where organization_id=? and user_id=? and role_name=?";
	
	
	public static final String GET_LIST_PAGE_CUSTOMIZATION_ORGANIZATION = "select id,data,status from usermgmt.list_page_customize_organization where organization_id=? and module_name=?";
	
	public static final String GET_LIST_PAGE_CUSTOMIZATION_BY_ORGANIZATION_USER_ROLE = "select id,data,status from usermgmt.list_page_customize_organization where organization_id=? and module_name=? and user_id=? and role_name=?";

	public static final String INSERT_INTO_LIST_PAGE_CUSTOMIZATION = "insert into usermgmt.list_page_customize_organization "
			+ "(data,module_name,status,organization_id,user_id,role_name)"
			+ "values(?,?,?,?,?,?);"	;
	
	public static final String UPDATE_LIST_PAGE_CUSTOMIZATION = "update usermgmt.list_page_customize_organization set data=?,update_ts=?,update_user_id=?,update_role_name=? where  id=?";

	public static final String GET_FINANCE_COMMON_BY_ID_ORGANIZATION = "select id,name,tds_rate_identifier,tds_rate_percentage,description,applicable_for,create_ts from tds where id=?";


}

package com.blackstrawai.onboarding;

public class OrganizationConstants {
	
	public static final String INSERT_INTO_ORGANIZATION="insert into organization(name,organization_constitution_id,type_id,industry_id,base_currency_id,financial_year,user_id,create_ts,status)values(?,?,?,?,?,?,?,?,?)";

	public static final String INSERT_INTO_NEW_ORGANIZATION="insert into organization("
			+ "name,organization_constitution_id,cin_no,type_id,industry_id,base_currency_id,financial_year,status,user_id,create_ts,date_format,"
			+ "time_zone,contact_no,email_id,ie_code_no,isreport_cash,roc_data,is_super_admin)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

	public static final String INSERT_INTO_ORGANIZATION_GST_DETAILS="insert into gst_details("
			+ "gst_reg_type_id,gst_no,tax_id,address_1,address_2,city,state,country,pin_code,organization_id,is_multi_gst,create_ts)values("
			+ "?,?,?,?,?,?,?,?,?,?,?,?)";
	
	public static final String INSERT_INTO_ORGANIZATION_GST_DETAILS_MINIMAL="insert into gst_details("
			+ "gst_reg_type_id,gst_no,tax_id,address_1,address_2,city,state,country,pin_code,organization_id,is_multi_gst,create_ts,taxilla_data)values("
			+ "?,?,?,?,?,?,?,?,?,?,?,?,?)";
	
	public static final String INSERT_INTO_ORGANIZATION_GST_LOCATION="insert into gst_location("
			+ "location_name,gst_no,tax_id,address_1,address_2,city,state,country,pin_code,organization_id,create_ts)values("
			+ "?,?,?,?,?,?,?,?,?,?,?)";
	
	public static final String INSERT_INTO_ORGANIZATION_GST_LOCATION_MNIMAL="insert into gst_location("
			+ "location_name,gst_no,tax_id,address_1,address_2,city,state,country,pin_code,organization_id,create_ts,taxilla_data)values("
			+ "?,?,?,?,?,?,?,?,?,?,?,?)";
	
	public static final String GST_DETAILS_WITH_ADDRESS_BY_ID_GSTN = "SELECT gstlocdet.id,gstlocdet.gst_reg_type_id,gstlocdet.gst_no,gstlocdet.address_1,gstlocdet.address_2,gstlocdet.city,gstlocdet.state, state.name state_name, gstlocdet.country , country.name country_name ,gstlocdet.pin_code " + 
			"FROM gst_details gstlocdet  join finance_common.state_list state on state.id = gstlocdet.state join finance_common.country_list country on country.id = gstlocdet.country " + 
			"where gstlocdet.organization_id=? and gstlocdet.id=? ";

	public static final String GST_LOCATION_WITH_ADDRESS_BY_ID_GSTN= "SELECT gstlocdet.id , gstlocdet.location_name,gstlocdet.gst_no,gstlocdet.address_1,gstlocdet.address_2,gstlocdet.city,gstlocdet.state, state.name state_name, gstlocdet.country , country.name country_name ,gstlocdet.pin_code  " + 
			"FROM gst_location gstlocdet  join finance_common.state_list state on state.id = gstlocdet.state  " + 
			"join finance_common.country_list country on country.id = gstlocdet.country " + 
			"where gstlocdet.organization_id=? and gstlocdet.id=? and gstlocdet.status not in('DEL')";

	
	public static String GST_DETAILS="select * from gst_details where organization_id=?";
	
	public static String GST_LOCATION="select * from gst_location where  organization_id=? and status not in(?)";
	
	public static String GST_DETAILS_WITH_ADDRESS = "SELECT gd.id,gd.gst_reg_type_id,gd.gst_no,gd.address_1,gd.address_2,gd.city,gd.state, state.name state_name, gd.country , country.name country_name ,gd.pin_code ,(select name from  organization where id = ?) org_name  " + 
													"FROM gst_details gd  join finance_common.state_list state on state.id = gd.state join finance_common.country_list country on country.id = gd.country " + 
													"where gd.organization_id=?";
	
	public static String GST_LOCATION_WITH_ADDRESS= "SELECT gl.id , gl.location_name,gl.gst_no,gl.address_1,gl.address_2,gl.city,gl.state, state.name state_name, gl.country , country.name country_name ,gl.pin_code,(select name from  organization where id = ?) org_name  " + 
													"FROM gst_location gl  join finance_common.state_list state on state.id = gl.state  " + 
													"join finance_common.country_list country on country.id = gl.country " + 
													"where gl.organization_id=? and gl.status not in(?)";		
			
	public static final String GET_BASIC_USER_ORGANIZATIONS="select id,name,status,create_ts,date_format from organization where user_id=? and status not in(?)";
	public static final String GET_BASIC_USER_ORGANIZATIONS_BY_EMAIL="select org.id,org.name,org.status,org.create_ts,org.date_format,reg.access_data,reg.id  FROM usermgmt.organization org  " + 
																	 "join usermgmt.registration reg on reg.id = org.user_id where reg.email_id = ? and org.status not in (?)";

	public static final String GET_BASIC_USER_ORG_BY_EMAIL_IN_KEY_CONTACTS = "SELECT org.id,org.name,org.status,org.create_ts,org.date_format,kreg.access_data  FROM usermgmt.organization org  " + 
			"join usermgmt.key_contacts_registration kreg on kreg.organization_id = org.id where kreg.email_id =?  and  org.status not in (?)" ;
	
	public static final String GET_BASIC_USER_ORG_BY_EMAIL_IN_USER = "SELECT org.id,org.name,org.status,org.create_ts,org.date_format,usr.access_data,usr.role_id,usr.id,usr.status  FROM usermgmt.organization org  " + 
			"join usermgmt.user usr on usr.organization_id = org.id where usr.email_id =?  and  org.status not in (?)" ;
	
	public static final String INSERT_INTO_ORGANIZATION_ADDRESS="insert into organization_address(address_1,address_2,city,state,country,pin_code,organization_id,create_ts)values(?,?,?,?,?,?,?,?)";

	public static final String INSERT_INTO_ORGANIZATION_PROPRIETOR_OTHER_DETAILS="insert into organization_proprietor_other_details(date_format,time_zone,contact_number,email_id,tax_pan_number,gst_number,ie_code_number,isreport_cash,organization_id,create_ts)values(?,?,?,?,?,?,?,?,?,?)";
	public static final String INSERT_INTO_ORGANIZATION_PROPRIETOR="insert into organization_proprietor(name,email_id,mobile_no,organization_id,create_ts)values(?,?,?,?,?)";

	public static final String INSERT_INTO_ORGANIZATION_PARTNERSHIP_OTHER_DETAILS="insert into organization_partnership_other_details(date_format,time_zone,contact_number,email_id,tax_pan_number,gst_number,ie_code_number,isreport_cash,certificate_license,partnership_regsitration_no,organization_id,create_ts)values(?,?,?,?,?,?,?,?,?,?,?,?)";
	public static final String INSERT_INTO_ORGANIZATION_PARTNERSHIP_PARTNER="insert into organization_partnership_partner(name,email_id,mobile_no,ownership_percentage,organization_id,create_ts)values(?,?,?,?,?,?)";

	public static final String INSERT_INTO_ORGANIZATION_PRIVATE_LIMITED_OTHER_DETAILS="insert into organization_private_limited_other_details(date_format,time_zone,contact_number,email_id,tax_pan_number,gst_number,ie_code_number,isreport_cash,cin_no,incorporation_date,organization_id,create_ts)values(?,?,?,?,?,?,?,?,?,?,?,?)";
	public static final String INSERT_INTO_ORGANIZATION_PRIVATE_LIMITED_DIRECTOR="insert into organization_private_limited_director(name,email_id,mobile_no,ownership_percentage,din_no,organization_id,create_ts)values(?,?,?,?,?,?,?)";

	public static final String INSERT_INTO_ORGANIZATION_ONE_PERSON_OTHER_DETAILS="insert into organization_one_person_other_details(date_format,time_zone,contact_number,email_id,tax_pan_number,gst_number,ie_code_number,isreport_cash,cin_no,incorporation_date,organization_id,create_ts)values(?,?,?,?,?,?,?,?,?,?,?,?)";
	public static final String INSERT_INTO_ORGANIZATION_ONE_PERSON_DIRECTOR="insert into organization_one_person_director(name,email_id,mobile_no,din_no,organization_id,create_ts)values(?,?,?,?,?,?)";

	public static final String INSERT_INTO_ORGANIZATION_LIMITED_LIABILITY_OTHER_DETAILS="insert into organization_limited_liability_other_details(date_format,time_zone,contact_number,email_id,tax_pan_number,gst_number,ie_code_number,isreport_cash,llpin,incorporation_date,organization_id,create_ts)values(?,?,?,?,?,?,?,?,?,?,?,?)";
	public static final String INSERT_INTO_ORGANIZATION_LIMITED_LIABILITY_PARTNER="insert into organization_limited_liability_partner(name,email_id,mobile_no,ownership_percentage,organization_id,create_ts)values(?,?,?,?,?,?)";

	public static final String INSERT_INTO_ORGANIZATION_PUBLIC_LIMITED_OTHER_DETAILS="insert into organization_public_limited_other_details(date_format,time_zone,contact_number,email_id,tax_pan_number,gst_number,ie_code_number,isreport_cash,cin_no,incorporation_date,organization_id,create_ts)values(?,?,?,?,?,?,?,?,?,?,?,?)";
	public static final String INSERT_INTO_ORGANIZATION_PUBLIC_LIMITED_DIRECTOR="insert into organization_public_limited_director(name,email_id,mobile_no,ownership_percentage,din_no,organization_id,create_ts)values(?,?,?,?,?,?,?)";

	public static final String CHECK_ORGANIZATON_EXIST="select * from organization where name=? and status not in(?)";
	public static final String DELETE_ORGANIZATION="update organization set status=?,update_ts=? where id=?";   
	public static final String GET_USER_ORGANIZATIONS="select * from organization where user_id=? and status not in(?)";
	public static final String GET_ORGANIZATION="select * from organization where id=? and status not in(?)";
	public static final String GET_USER_ORGANIZATIONS_ADDRESS="select id,address_1,address_2,city,state,country,pin_code from organization_address where organization_id=?";
	public static final String GET_PROPRIETOR_OTHER_DETAILS="select id,date_format,time_zone,contact_number,email_id,tax_pan_number,gst_number,ie_code_number,isreport_cash from organization_proprietor_other_details where organization_id=?";
	public static final String PROPRIETOR="select id,name,email_id,mobile_no,status from organization_proprietor where organization_id=? and status not in(?)";
	public static final String GET_PARTNERSHIP_OTHER_DETAILS="select id,date_format,time_zone,contact_number,email_id,tax_pan_number,gst_number,ie_code_number,isreport_cash,certificate_license,partnership_regsitration_no from organization_partnership_other_details where organization_id=?";
	public static final String PARTNER="select id,name,email_id,mobile_no,ownership_percentage,status from organization_partnership_partner where organization_id=? and status not in(?)";
	public static final String GET_PRIVATE_LIMITED_OTHER_DETAILS="select id,date_format,time_zone,contact_number,email_id,tax_pan_number,gst_number,ie_code_number,isreport_cash,cin_no,incorporation_date from organization_private_limited_other_details where organization_id=?";
	public static final String PRIVATE_LIMITED_DIRECTOR="select id,name,email_id,mobile_no,ownership_percentage,din_no,status from organization_private_limited_director where organization_id=? and status not in(?)";
	public static final String GET_ONE_PERSON_OTHER_DETAILS="select id,date_format,time_zone,contact_number,email_id,tax_pan_number,gst_number,ie_code_number,isreport_cash,cin_no,incorporation_date from organization_one_person_other_details where organization_id=?";
	public static final String ONE_PERSON_DIRECTOR="select id,name,email_id,mobile_no,din_no,status from organization_one_person_director where organization_id=? and status not in(?)";
	public static final String GET_LIMITED_LIABILITY_OTHER_DETAILS="select id,date_format,time_zone,contact_number,email_id,tax_pan_number,gst_number,ie_code_number,isreport_cash,llpin,incorporation_date from organization_limited_liability_other_details where organization_id=?";
	public static final String LIMITED_LIABILITY_PARTNER="select id,name,email_id,mobile_no,ownership_percentage,status from organization_limited_liability_partner where organization_id=? and status not in(?)";
	public static final String GET_PUBLIC_LIMITED_OTHER_DETAILS="select id,date_format,time_zone,contact_number,email_id,tax_pan_number,gst_number,ie_code_number,isreport_cash,cin_no,incorporation_date from organization_public_limited_other_details where organization_id=?";
	public static final String PUBLIC_LIMITED_DIRECTOR="select id,name,email_id,mobile_no,ownership_percentage,din_no,status from organization_public_limited_director where organization_id=? and status not in(?)";

	public static final String UPDATE_ORGANIZATION="update organization set organization_constitution_id=?,cin_no=?,"
			+ "type_id=?,industry_id=?,"
			+ "base_currency_id=?,financial_year=?,update_ts=?,date_format=?,time_zone=?,contact_no=?,email_id=?,ie_code_no=?,"
			+ "isreport_cash=? where id=?";
	
	public static final String UPDATE_ORGANIZATION_MINIMAL="update organization set pan=?,organization_constitution_id=?,"
			+ "type_id=?,industry_id=?,"
			+ "update_ts=?,display_name=?,user_profile=?,brand_name=?,name=? where id=?";
	
	public static final String UPDATE_GST_ORGANIZATION="update gst_details set gst_reg_type_id=?,gst_no=?,"
			+ "tax_id=?,address_1=?,"
			+ "address_2=?,city=?,state=?,country=?,pin_code=?,is_multi_gst=?,update_ts=? where organization_id=?";
	
	public static final String UPDATE_LOCATION_ORGANIZATION="update gst_location set location_name=?,gst_no=?,"
			+ "tax_id=?,address_1=?,"
			+ "address_2=?,city=?,state=?,country=?,pin_code=?,status=?,update_ts=? where id=?";

	public static final String UPDATE_ADDRESS_ORGANIZATION="update organization_address set address_1=?,address_2=?,city=?,state=?,country=?,"
			+ "pin_code=?,update_ts=? where organization_id=?";

	public static final String UPDATE_PROPRIETOR_OTHER_DETAILS_ORGANIZATION="update organization_proprietor_other_details set date_format=?,time_zone=?,"
			+ "contact_number=?,email_id=?,tax_pan_number=?,gst_number=?,ie_code_number=?,isreport_cash=?,update_ts=? where organization_id=?";


	public static final String UPDATE_PARTNERSHIP_OTHER_DETAILS_ORGANIZATION="update organization_partnership_other_details set date_format=?,"
			+ "time_zone=?,contact_number=?,email_id=?,tax_pan_number=?,gst_number=?,ie_code_number=?,isreport_cash=?,certificate_license=?,"
			+ "partnership_regsitration_no=?, update_ts=? where organization_id=?";
	
	public static final String UPDATE_PRIVATE_OTHER_DETAILS_ORGANIZATION="update organization_private_limited_other_details set date_format=?,"
			+ "time_zone=?,contact_number=?,email_id=?,tax_pan_number=?,gst_number=?,ie_code_number=?,isreport_cash=?,"
			+ "cin_no=?,incorporation_date=?, update_ts=? where organization_id=?";
	
	public static final String UPDATE_ONE_PERSON_OTHER_DETAILS_ORGANIZATION="update organization_one_person_other_details "
			+ "set date_format=?,time_zone=?,contact_number=?,email_id=?,tax_pan_number=?,gst_number=?,ie_code_number=?,"
			+ "isreport_cash=?,cin_no=?,incorporation_date=?, update_ts=? where organization_id=?";
	
	public static final String UPDATE_LIMITED_LIABILITY_OTHER_DETAILS_ORGANIZATION="update organization_limited_liability_other_details "
			+ "set date_format=?,time_zone=?,contact_number=?,email_id=?,tax_pan_number=?,gst_number=?,incorporation_date=?,"
			+ "ie_code_number=?,isreport_cash=?,llpin=?, update_ts=? where organization_id=?";
	
	public static final String UPDATE_PUBLIC_LIMITED_OTHER_DETAILS_ORGANIZATION="update organization_public_limited_other_details "
			+ "set date_format=?,time_zone=?,contact_number=?,email_id=?,tax_pan_number=?,gst_number=?,cin_no=?,"
			+ "incorporation_date=?,ie_code_number=?,isreport_cash=?,update_ts=? where organization_id=?";
	
	public static final String UPDATE_PROPRIETOR_ORGANIZATION="update organization_proprietor set name=?,email_id=?,mobile_no=?,status=?,"
			+ "update_ts=? where id=?";	

	public static final String UPDATE_PARTNERSHIP_PARTNER_ORGANIZATION="update organization_partnership_partner set name=?,"
			+ "email_id=?,mobile_no=?,ownership_percentage=?,status=?,update_ts=? where id=?";
	
	public static final String UPDATE_PRIVATE_DIRECTOR_ORGANIZATION="update organization_private_limited_director set name=?,"
			+ "email_id=?,mobile_no=?,ownership_percentage=?,din_no=?,status=?,update_ts=? where id=?";	

	public static final String UPDATE_ONE_PERSON_DIRECTOR_ORGANIZATION="update organization_one_person_director "
			+ "set name=?,email_id=?,mobile_no=?,din_no=?,status=?,update_ts=? where id=?";

	public static final String UPDATE_LIMITED_LIABILITY_PARTNER_ORGANIZATION="update organization_limited_liability_partner "
			+ "set name=?,email_id=?,mobile_no=?,ownership_percentage=?,status=?,update_ts=? where id=?";

	public static final String UPDATE_PUBLIC_LIMITED_DIRECTOR_ORGANIZATION="update organization_public_limited_director "
			+ "set name=?,email_id=?,mobile_no=?,ownership_percentage=?,din_no=?,status=?,update_ts=? where id=?";

	public static final String GET_DEFAULT_DATE_FOR_ORGANIZATION ="select date_format from organization where id = ?";

/*	public static final String GET_DEFAULT_CURRENCY_FOR_ORGANIZATION = "select id,name,symbol ,alternate_symbol from currency_organization where id = (select base_currency_id from organization where id = ?) and status not in (?)";
*/
	public static final String GET_DEFAULT_CURRENCY_FROM_CURRENCY_ORGANIZATION = "select id,name,symbol ,alternate_symbol from usermgmt.currency_organization  uc "+
			"where  uc.organization_id = ? and name = (select name from finance_common.base_currency where id =( select base_currency_id from usermgmt.organization where id = ?))";

	
	public static final String GET_BASE_CURRENCY_ORGANIZATION="select base_currency_id from organization where id=? and status not in(?)";
	
	public static final String GET_BASE_VOUCHER_ENTRIES_ORGANIZATION="select id,prefix,suffix,minimum_digits from voucher_management_organization where organization_id=? and status not in(?) and type=?";
	
	public static final String EXCEL_FILE_FORMAT = ".xls";
	
	public static final String GET_ORGANIZATION_NAME = "select name from organization where id=?";
	public static final String GET_ORGANIZATION_CONTACT_NO = "select contact_no from organization where id=?";
	
	public static final String GET_LOCATION_ID="select id from gst_details where organization_id=?";
	public static final String GET_GST_LOCATION_ID="select id from gst_location where organization_id=? and location_name=?";
	
	public static final String GET_GST_DETAILS_TAXILLA_DATA="select id,gst_reg_type_id,gst_no,taxilla_data from gst_details where organization_id=?";
	
	public static final String GET_GST_LOCATION_TAXILLA_DATA="select id,gst_no,taxilla_data from gst_location where organization_id=?";

	
	public static final String INSERT_INTO_MINIMAL_ORGANIZATION="insert into organization("
			+ "name,display_name,organization_constitution_id,type_id,industry_id,base_currency_id,financial_year,status,user_id,create_ts,date_format,"
			+ "time_zone,is_super_admin,user_profile,brand_name,isreport_cash,pan)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

	public static final String FETCH_PAN_DETAILS_ORGANIZATION="select pan from usermgmt.organization where id=?";
	
	public static final String FETCH_INCOME_TAX_LOGIN="select id,password,remember_me from income_tax.user_info where organization_id=? and login=?";

	public static final String GET_DEFAULT_ORGANIZATION_USER = "select organization_id from organization_default where email=?";

}

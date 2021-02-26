package com.blackstrawai.keycontact;

public class VendorConstants {
	
	private VendorConstants() {
		super();
	}

	public static final String INSERT_INTO_VENDOR_GENERAL_INFORMATION = "insert into vendor_general_information(primary_contact,company_name,vendor_display_name,email,phone_no,website,mobile_no,user_id,organization_id,isSuperAdmin,pan,vend_organization_id,vendor_group_id,vendor_gst_type_id,gst_no,same_billing_dest_address,is_registered_msme,msme_number,role_name,default_tally_gl_id,default_tally_gl_name,default_gl_id,default_gl_name,location_id,book_keeping_gst_number,other_gstns,status,is_pan_or_gst_available,overseas_vendor,is_pan_exist)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	public static final String INSERT_INTO_VENDOR_FINANCE = "insert into vendor_finance(currency_id,payment_terms_id,source_of_supply_id,tds_id,opening_balance,create_ts,vendor_general_information_id)values(?,?,?,?,?,?,?)";
	public static final String INSERT_INTO_VENDOR_ORIGIN_ADDRESS = "insert into vendor_origin_address(attention,country,address_1,address_2,state,city,zip_code,phone_no,landmark,create_ts,selected_address_type_id,gst_no,address_type,vendor_general_information_id)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	public static final String INSERT_INTO_VENDOR_DESTINATION_ADDRESS = "insert into vendor_destination_address(attention,country,address_1,address_2,state,city,zip_code,phone_no,landmark,create_ts,selected_address_type_id,gst_no,address_type,vendor_general_information_id)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	public static final String INSERT_INTO_VENDOR_BANK_DETAILS = "insert into vendor_bank_details(bank_name,account_no,account_holder_name,branch_name,ifsc_code,upi_id,is_default,vendor_general_information_id)values(?,?,?,?,?,?,?,?)";
	public static final String INSERT_INTO_VENDOR_CONTACT = "insert into vendor_contact(salutation,first_name,last_name,work_no,mobile_no,email,vendor_general_information_id,is_default)values(?,?,?,?,?,?,?,?)";
	public static final String INSERT_INTO_VENDOR_BOOK_KEEPING_SETTING = "insert into vendor_book_keeping_setting(default_gl_code,default_gl_name,location_id,gst_number,vendor_general_information_id)values(?,?,?,?,?)";
	public static final String INSERT_INTO_VENDOR_ADDITIONAL_ADDRESS = "insert into vendor_additional_address(attention,country,address_1,address_2,state,city,zip_code,phone_no,landmark,vendor_general_information_id,selected_address_type_id,gst_no,address_type)values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
	public static final String DELETE_VENDOR = "update vendor_general_information set status=?,update_ts=?,update_user_id=?,update_role_name=? where id=?";
	public static final String CHECK_VENDOR_EXIST_FOR_ORGANIZATION = "select count(*) from vendor_general_information where organization_id=? and pan=?";
	public static final String CHECK_VENDOR_EXIST_FOR_UPDATE_VENDOR_GIVEN_ORGANIZATION = "select count(*) from vendor_general_information where organization_id=? and pan=? and id !=?";
	public static final String GET_VENDORS_NAME_BY_ORGANIZATION = "select id,vendor_display_name from vendor_general_information where organization_id=? and status=?";
	public static final String GET_VENDORS_GENERAL_INFO_BY_ORGANIZATION = "select u1.id,u1.primary_contact,u1.company_name,u1.vendor_display_name,u1.email,u1.phone_no,u1.website,u1.mobile_no,u1.status,u1.user_id,u1.organization_id,u1.isSuperAdmin,u1.pan,u1.vend_organization_id ,u1.vendor_group_id,u1.vendor_gst_type_id ,u1.gst_no from vendor_general_information u1 where u1.organization_id=? order by u1.id desc";
	public static final String GET_VENDORS_GENERAL_INFO_BY_ORGANIZATION_USER_ROLE = "select u1.id,u1.primary_contact,u1.company_name,u1.vendor_display_name,u1.email,u1.phone_no,u1.website,u1.mobile_no,u1.status,u1.user_id,u1.organization_id,u1.isSuperAdmin,u1.pan,u1.vend_organization_id ,u1.vendor_group_id,u1.vendor_gst_type_id ,u1.gst_no from vendor_general_information u1 where u1.organization_id=? and u1.user_id=? and u1.role_name=? order by u1.id desc";
	public static final String GET_VENDOR_INFO_BY_ORGANIZATION = "select u1.id,u1.primary_contact,u1.company_name,u1.vendor_display_name,u1.email,u1.phone_no,u1.website,u1.mobile_no,u1.status,u1.user_id,u1.organization_id,u1.isSuperAdmin,u1.pan,u1.vend_organization_id ,u1.vendor_group_id,u1.vendor_gst_type_id ,u1.gst_no from vendor_general_information u1 where u1.organization_id=? order by u1.id desc";
	public static final String GET_VENDORS_FINANCE_BY_ORGANIZATION = "select u2.id,u2.currency_id,u2.payment_terms_id,u2.source_of_supply_id,u2.tds_id,u2.opening_balance from vendor_general_information u1,vendor_finance u2 where u1.id=u2.vendor_general_information_id and u1.organization_id=?";
	public static final String GET_VENDORS_ORIGIN_ADDRESS_BY_ORGANIZATION = "select u3.id,u3.attention,u3.country,u3.address_1,u3.address_2,u3.state,u3.city,u3.zip_code,u3.phone_no,u3.landmark from vendor_general_information u1,vendor_origin_address u3 where u1.id=u3.vendor_general_information_id and u1.organization_id=?";
	public static final String GET_VENDORS_DESTINATION_ADDRESS_BY_ORGANIZATION = "select u4.id,u4.attention,u4.country,u4.address_1,u4.address_2,u4.state,u4.city,u4.zip_code,u4.phone_no,u4.landmark from vendor_general_information u1,vendor_destination_address u4 where u1.id=u4.vendor_general_information_id and u1.organization_id=?";
	public static final String GET_VENDORS_BANK_DETAILS_BY_ORGANIZATION = "select u5.id,u5.bank_name,u5.account_no,u5.account_holder_name,u5.branch_name,u5.ifsc_code,upi_id,u5.is_default from vendor_general_information u1,vendor_bank_details u5 where u1.id=u5.vendor_general_information_id and u1.organization_id=?";
	public static final String GET_VENDORS_CONTACT_BY_ORGANIZATION = "select u6.id,u6.salutation,u6.first_name,u6.last_name,u6.work_no,u6.mobile_no,u6.email,u6.status from vendor_general_information u1,vendor_contact u6 where u1.id=u6.vendor_general_information_id and u1.organization_id=?";
	public static final String GET_VENDOR_GENERAL_INFO_BY_VENDOR_ID = "select id,primary_contact,company_name,vendor_display_name,email,phone_no,website,mobile_no,status,user_id,organization_id,isSuperAdmin,pan,vend_organization_id,vendor_group_id,vendor_gst_type_id,gst_no,same_billing_dest_address,is_registered_msme,msme_number,default_tally_gl_id,default_tally_gl_name,default_gl_id,default_gl_name,location_id,book_keeping_gst_number,other_gstns,is_pan_or_gst_available, overseas_vendor,is_pan_exist from vendor_general_information where id=?";
	public static final String GET_VENDOR_FINANCE_BY_VENDOR_ID = "select id,currency_id,payment_terms_id,source_of_supply_id,tds_id,opening_balance from vendor_finance where vendor_general_information_id=?";
	public static final String GET_VENDOR_ORIGIN_ADDRESS_BY_VENDOR_ID = "select id,attention,country,address_1,address_2,state,city,zip_code,phone_no,landmark,selected_address_type_id,gst_no,address_type from vendor_origin_address where vendor_general_information_id=?";
	public static final String GET_VENDOR_ORIGIN_ADDRESS_BY_VENDOR_ID_AND_GST_NO = "select id,attention,country,address_1,address_2,state,city,zip_code,phone_no,landmark,selected_address_type_id,gst_no,address_type from vendor_origin_address where vendor_general_information_id=? and gst_no=?";
	public static final String GET_VENDOR_ADDITIONAL_ADDRESS_BY_VENDOR_ID = "select id,attention,country,address_1,address_2,state,city,zip_code,phone_no,landmark,selected_address_type_id,gst_no,address_type from vendor_additional_address where vendor_general_information_id=?";
	public static final String GET_VENDOR_BOOK_KEEPING_SETTING_BY_VENDOR_ID = "select id,default_gl_code,default_gl_name,location_id,gst_number,vendor_general_information_id from vendor_book_keeping_setting where vendor_general_information_id=?";
	public static final String GET_VENDOR_DESTINATION_ADDRESS_BY_VENDOR_ID = "select id,attention,country,address_1,address_2,state,city,zip_code,phone_no,landmark,selected_address_type_id,gst_no,address_type from vendor_destination_address where vendor_general_information_id=?";
	public static final String GET_VENDOR_DESTINATION_ADDRESS_BY_VENDOR_ID_AND_GST_NO = "select id,attention,country,address_1,address_2,state,city,zip_code,phone_no,landmark,selected_address_type_id,gst_no,address_type from vendor_destination_address where vendor_general_information_id=? and gst_no=?";
	public static final String GET_VENDOR_BANK_DETAILS_BY_VENDOR_ID = "select id,bank_name,account_no,account_holder_name,branch_name,ifsc_code,upi_id,is_default,status from vendor_bank_details where vendor_general_information_id=? and status not in(?)";
	public static final String GET_VENDOR_CONTACT_DETAILS_BY_VENDOR_ID = "select id,salutation,first_name,last_name,work_no,mobile_no,email,status,is_default from vendor_contact where vendor_general_information_id=? and status not in(?)";
	public static final String UPDATE_VENDOR_GENERAL_INFORMATION = "update vendor_general_information set primary_contact=?,company_name=?,vendor_display_name=?,email=?,phone_no=?,website=?,mobile_no=?,pan=?,vendor_group_id=?,vend_organization_id=?,vendor_gst_type_id=?,gst_no=?,organization_id=?,isSuperAdmin=?,same_billing_dest_address=?,update_ts=?,update_user_id=?,update_role_name=?,default_tally_gl_id=?,default_tally_gl_name=?,default_gl_id=?,default_gl_name=?,location_id=?,book_keeping_gst_number=?,other_gstns=?,status=?,is_pan_or_gst_available=?,overseas_vendor=?,is_pan_exist=? where id=?";
	public static final String UPDATE_VENDOR_FINANCE = "update vendor_finance set currency_id=?,payment_terms_id=?,source_of_supply_id=?,tds_id=?,opening_balance=?,update_ts=? where id=?";
	public static final String UPDATE_VENDOR_ORIGIN_ADDRESS = "update vendor_origin_address set attention=?,country=?,address_1=?,address_2=?,state=?,city=?,zip_code=?,phone_no=?,landmark=?,update_ts=?,selected_address_type_id=?,gst_no=?,address_type=? where id=?";
	public static final String UPDATE_VENDOR_DESTINATION_ADDRESS = "update vendor_destination_address set attention=?,country=?,address_1=?,address_2=?,state=?,city=?,zip_code=?,phone_no=?,landmark=?,update_ts=?,selected_address_type_id=?,gst_no=?,address_type=? where id=?";
	public static final String UPDATE_VENDOR_BANK_DETAILS = "update vendor_bank_details set account_no=?,account_holder_name=?,bank_name=?,branch_name=?,ifsc_code=?,upi_id =?,is_default =?,status=?,update_ts=? where id=?";
	public static final String UPDATE_VENDOR_CONTACT = "update vendor_contact set salutation=?,first_name=?,last_name=?,work_no=?,mobile_no=?,email=?,status=?,update_ts=?,is_default=? where id=?";
	public static final String GET_PO_COUNT_BY_VENDOR = "select count(vendor_id) from purchase_order_general_information where vendor_id=? and organization_id=? and status not in(?)";
	public static final String CHECK_BANK_DETAILS_EXIST_FOR_VENDOR = "select * from vendor_bank_details where vendor_general_information_id=? and account_no=? and ifsc_code=? and status not IN(?)";
	public static final String CHECK_CONTACT_DETAILS_EXIST_FOR_VENDOR = "select * from vendor_contact  where email=? and mobile_no=?";
	public static final String MODIFY_VENDOR_STATUS = "update vendor_general_information  set status = ? ,update_ts=? where id = ? ";
	public static final String MODIFY_VENDOR_CONTACT_STATUS = "update vendor_contact  set status = ? , update_ts=? where vendor_general_information_id = ?";
	public static final String MODIFY_VENDOR_BANK_INFO_STATUS = "update vendor_bank_details  set status = ? , update_ts=? where vendor_general_information_id =?";

	public static final String GET_VENDOR_DEFAULT_BANK_DETAILS_BY_VENDOR_ID = "select id,bank_name,account_no,account_holder_name,branch_name,ifsc_code,upi_id,is_default,status from vendor_bank_details where vendor_general_information_id=? and status not in(?) and is_default= 1";

	
	public static final String GET_VENDOR_BASIC_PER_ORGANIZATION = "SELECT vgi.id,vgi.vendor_display_name,vf.payment_terms_id, pto.days_limit ,oa.address_1,oa.address_2,oa.city,oa.state,state.name state_display,oa.country ,country.name country_display, oa.zip_code ,vf.tds_id ,vgi.gst_no,vf.currency_id,vgi.pan "
			+ "FROM vendor_general_information vgi LEFT JOIN vendor_destination_address oa ON oa.vendor_general_information_id = vgi.id "
			+ "LEFT JOIN vendor_finance vf ON vf.vendor_general_information_id = vgi.id "
			+ "LEFT join finance_common.state_list state on state.id = oa.state "
			+ "LEFT join finance_common.country_list country on country.id = oa.country "
			+ "LEFT join usermgmt.payment_terms_organization pto on pto.id = vf.payment_terms_id "
			+ "WHERE vgi.organization_id = ? and vgi.STATUS=?";
	
	
	public static final String GET_VENDOR_BASIC_PER_ORGANIZATION_WITHOUT_ADDRESS = "SELECT vgi.id,vgi.vendor_display_name,vf.payment_terms_id, pto.days_limit , vf.tds_id ,vgi.gst_no,vf.currency_id,vgi.pan \r\n" + 
			"FROM vendor_general_information vgi  LEFT JOIN vendor_finance vf ON vf.vendor_general_information_id = vgi.id  " + 
			"LEFT join usermgmt.payment_terms_organization pto on pto.id = vf.payment_terms_id " + 
			"WHERE vgi.organization_id = ? and vgi.STATUS=? ";
	
	public static final String GET_ALL_ACTIVE_VENDORS_ORGANIZATION = "SELECT vgi.id,vgi.vendor_display_name,vf.payment_terms_id, pto.days_limit ,oa.address_1,oa.address_2,oa.city,oa.state,state.name state_display,oa.country ,country.name country_display, oa.zip_code ,vf.tds_id ,vgi.gst_no,vf.currency_id "
			+ "FROM vendor_general_information vgi LEFT JOIN vendor_origin_address oa ON oa.vendor_general_information_id = vgi.id "
			+ "LEFT JOIN vendor_finance vf ON vf.vendor_general_information_id = vgi.id "
			+ "LEFT join finance_common.state_list state on state.id = oa.state "
			+ "LEFT join finance_common.country_list country on country.id = oa.country "
			+ "LEFT join usermgmt.payment_terms_organization pto on pto.id = vf.payment_terms_id "
			+ "WHERE vgi.organization_id = ? AND vgi.status=?";
	public static final String GET_PAYMENT_TERM_ID = "select id from payment_terms_organization where organization_id=? and name=?";
	public static final String GET_CURRENCY_ID = "select id from currency_organization where organization_id=? and name=?";
	public static final String GET_STATE_ID = "select id from state_list where name=?";
	public static final String GET_COUNTRY_ID = "select id from country_list where name=?";
	public static final String GET_VENDOR_ID = "select id from vendor_general_information where organization_id=? and email=?";
	public static final String GET_VENDOR_FINANCE_ID = "select id from vendor_finance where vendor_general_information_id=?";
	public static final String GET_VENDOR_CURRENCY_SYMBOL = "SELECT co.alternate_symbol FROM accounts_payable.vendor_finance vf " + 
			"join usermgmt.currency_organization co on co.id = vf.currency_id " + 
			"where vendor_general_information_id = ? ";
	public static final String GET_VENDOR_NAME = "select vendor_display_name from vendor_general_information where id = ?";
	public static final String GET_VENDOR_GST_NO = "select gst_no from vendor_general_information where id = ?";
	public static final String GET_VENDOR_DESTINATION_ADDRESS_ID = "select id from vendor_destination_address where vendor_general_information_id=?";
	public static final String GET_VENDOR_ORIGIN_ADDRESS_ID = "select id from vendor_origin_address where vendor_general_information_id=?";
	public static final String GET_VENDOR_CONTACT_DETAILS_EXPORT_BY_ID = "select salutation,first_name,last_name,work_no,mobile_no,email from vendor_contact where vendor_general_information_id=?";
	public static final String GET_VENDOR_BANK_DETAILS_EXPORT_BY_ID = "select account_holder_name,account_no,bank_name,branch_name,ifsc_code,upi_id,is_default from vendor_bank_details where vendor_general_information_id=?";
	public static final String GET_VENDOR_BILLING_ADDRESS_EXPORT_BY_ID = "select attention,phone_no,country,address_1,address_2,landmark,state,city,zip_code from vendor_destination_address where vendor_general_information_id=?";
	public static final String GET_VENDOR_ORIGIN_ADDRESS_EXPORT_BY_ID = "select attention,phone_no,country,address_1,address_2,landmark,state,city,zip_code from vendor_origin_address where vendor_general_information_id=?";
	public static final String GET_VENDOR_GENERAL_INFO_EXPORT_BY_ID = "select primary_contact,company_name,vendor_display_name,email,phone_no,mobile_no,pan,vend_organization_id,vendor_group_id,vendor_gst_type_id,gst_no,status from vendor_general_information where id=?";
	public static final String GET_VENDOR_FINANCE_EXPORT_BY_ID = "select payment_terms_id,currency_id,opening_balance,tds_id from vendor_finance where vendor_general_information_id=?";
	public static final String GET_VENDOR_NAMES = "select id,vendor_display_name from vendor_general_information where organization_id=?";
	public static final String GET_VENDOR_CONTACTS_FROM_VENDOR_CONTACT = "select id,email,vendor_general_information_id from vendor_contact";
	public static final String GET_TDS_ID = "select id from tds where name=?";
	public static final String UPDATE_VENDOR_ONBOARDING_GENERAL_INFORMATION = "update vendor_general_information set primary_contact=?,company_name=?,email=?,mobile_no=?,pan=?,vend_organization_id=?,vendor_gst_type_id=?,gst_no=?,is_registered_msme=?,msme_number=?,isSuperAdmin=?,same_billing_dest_address=?,organization_id=?,update_ts=?,update_user_id=?,update_role_name=? where id=?";
	public static final String GET_VENDOR_DETAILS = "select v1.company_name, v1.gst_no, v1.mobile_no, v2.attention, v2.country, v2.address_1, v2.address_2, v2.state, v2.city, v2.zip_code, v2.phone_no, v2.landmark from vendor_general_information v1 left join vendor_origin_address v2 on v1.id = v2.vendor_general_information_id where v1.id = ?";
	public static final String GET_BASIC_VENDOR_FOR_ORG = "SELECT id,vendor_display_name FROM vendor_general_information where organization_id = ? ";
	public static final String UPDATE_VENDOR_SETTINGS = "update vendor_general_information set settings_data=?,settings_name=?,settings_id=?,settings_status=?,settings_contact_ids=?,update_ts=? where id=?";

	public static final String UPDATE_VENDOR_GRUOP_SETTINGS = "update vendor_group_organization set settings_data=?,settings_name=?,settings_id=?,settings_status=?,update_ts=? where id=?";
	public static final String GET_VENDOR_SETTINGS = "select id,vendor_display_name,settings_status,settings_name,email,settings_contact_ids from vendor_general_information where organization_id=? order by update_ts desc";
	public static final String GET_VENDORS = "select id,settings_name from vendor_general_information where vendor_group_id=?";
	public static final String GET_VENDOR_CONTACT_NAME = "select email from vendor_contact where id=?";
	public static final String ACTIVE_DEACTIVE_VENDOR_SETTINGS = "update vendor_general_information set settings_status=? where id=?";
	public static final String GET_VENDOR_SETTINGS_BY_ID = "select vendor_display_name,id,settings_status,settings_name,settings_id,email,settings_contact_ids,settings_data from vendor_general_information where id=?";
	public static final String UPDATE_VENDOR_SETTINGS_BY_ID = "update vendor_general_information set settings_name=?,settings_id=?,settings_status=?,settings_contact_ids=?,settings_data=? where id=?";
	public static final String GET_VENDOR_EMAIL = "select email from vendor_general_information where id=?";
	public static final String UPDATE_VENDOR_SETTINGS_BY_PREDEFINED_SETTINGS = "update vendor_general_information set settings_data=?,settings_name=? where settings_id=?";
	public static final String GET_VENDOR_SETTINGS_DATA = "select settings_data from vendor_general_information where id=?";
	public static final String SET_VENDOR_ONBOARDING_TYPE = "update vendor_general_information set is_quick=? where id=?";
	public static final String GET_VENDOR_DETAILS1 = "select email,mobile_no,vendor_display_name from vendor_general_information where id=?";
	public static final String UPDATE_VENDOR_SETTINGS_IN_KEY_CONTACTS_REGISTRATION = "update key_contacts_registration set access_data=? where organization_id=? and email_id=?";
	public static final String GET_ACTIVE_VENDOR_NAMES = "select vgi.id, vgi.vendor_display_name, vf.currency_id from vendor_general_information vgi left join vendor_finance vf on vgi.id = vf.vendor_general_information_id where organization_id=? and status=?";
	
	public static final String UPDATE_VENDOR_ORIGIN_ADDRESS_BY_ID_AND_GST_NO = "update vendor_origin_address set attention = ?, country= ?, address_1=?, address_2=?, state=?, city=?, zip_code=?, phone_no=?, landmark=?, update_ts=?, selected_address_type_id=?, gst_no= ?, address_type=? where vendor_general_information_id =? and gst_no =?";
	public static final String UPDATE_VENDOR_DEST_ADDRESS_BY_ID_AND_GST_NO = "update vendor_destination_address set attention = ?, country= ?, address_1=?, address_2=?, state=?, city=?, zip_code=?, phone_no=?, landmark=?, update_ts=?, selected_address_type_id=?, gst_no= ?, address_type=? where vendor_general_information_id =? and gst_no =?";
	
	public static final String UPDATE_VENDOR_ADDITIONAL_ADDRESS_BY_ID = "update vendor_additional_address set attention = ?, country= ?, address_1=?, address_2=?, state=?, city=?, zip_code=?, phone_no=?, landmark=?, update_ts=?, selected_address_type_id=?, gst_no= ?, address_type=? where id =?";
	
	public static final String DELETE_VENDOR_BANK_DETAILS = "delete from vendor_bank_details where id=?";
	public static final String DELETE_VENDOR_CONTACT = "delete from vendor_contact where id=?";
	public static final String DELETE_VENDOR_ADDITIONAL_ADDRESS = "delete from vendor_additional_address where vendor_general_information_id=?";
	public static final String DELETE_VENDOR_ORIGIN_ADDRESS = "delete from vendor_origin_address where vendor_general_information_id=?";
	public static final String DELETE_VENDOR_DEST_ADDRESS = "delete from vendor_destination_address where vendor_general_information_id=?";
	
}

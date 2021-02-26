package com.blackstrawai.keycontact;

public class CustomerConstants {
	// Queries for customer_general_information table 
	public static final String INSERT_INTO_CUSTOMER_GENERAL_INFO= "insert into customer_general_information(primary_contact, company_name,customer_display_name,  email_id, phone_no, website,  mobile_no, pan_no, status, same_billing_dest_address, gst_treatment_id, org_constitution_id, customer_group_id, currency_id, payment_terms_id, gst_number, opening_balance, create_ts, user_id, organization_id, isSuperAdmin,role_name,default_tally_gl_id,default_tally_gl_name,default_gl_id,default_gl_name,location_id,book_keeping_gst_number,other_gstns) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	public static final String GET_CUSTOMER_GENERAL_INFO = "SELECT primary_contact, company_name, customer_display_name, email_id, phone_no, website, mobile_no, pan_no, status, same_billing_dest_address, user_id, organization_id, isSuperAdmin , gst_treatment_id, org_constitution_id, customer_group_id, currency_id, payment_terms_id, gst_number, opening_balance,default_tally_gl_id,default_tally_gl_name,default_gl_id,default_gl_name,location_id,book_keeping_gst_number,other_gstns FROM customer_general_information where id = ? ";
	public static final String MODIFY_CUSTOMER_STATUS = "update customer_general_information set status = ?,update_ts=?,update_user_id=?,update_role_name=? where id = ? ";
	public static final String UPDATE_CUSTOMER_GENERAL_INFO ="update customer_general_information set primary_contact =? , company_name=? , customer_display_name=? ,  email_id=? , phone_no=? , website=? ,  mobile_no=? , pan_no=? , status=? , same_billing_dest_address=? , gst_treatment_id=? , org_constitution_id=?, customer_group_id=?, currency_id=? , payment_terms_id=?, gst_number=?, opening_balance=?, update_ts=?, update_user_id=?, organization_id=?, isSuperAdmin=?,update_role_name=?,default_tally_gl_id=?,default_tally_gl_name=?,default_gl_id=?,default_gl_name=?,location_id=?,book_keeping_gst_number=?,other_gstns=?  where id = ?";
	
	//Queries for customer_billing_address table 
	public static final String INSERT_INTO_CUSTOMER_BILLING_ADDRESS = "insert into customer_billing_address( attention, country, address_1, address_2, state, city, zip_code, phone_no, landmark, create_ts, customer_general_information_id,selected_address_type_id,gst_no,address_type) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	public static final String GET_CUSTOMER_BILLING_ADDRESS_INFO = "SELECT attention, country, address_1, address_2, state, city, zip_code, phone_no, landmark,selected_address_type_id,gst_no,address_type FROM customer_billing_address where customer_general_information_id = ? ";
	public static final String UPDATE_CUSTOMER_BILLING_ADDRESS ="update customer_billing_address set attention = ? , country=? , address_1=? , address_2=? , state=? , city=?, zip_code=?, phone_no=?, landmark=?, update_ts=?,selected_address_type_id=?,gst_no=?,address_type=?  where customer_general_information_id = ? ";
	
	//Queries for customer_delivery_address table
	public static final String INSERT_INTO_CUSTOMER_DELIVERY_ADDRESS = "insert into customer_delivery_address( attention, country, address_1, address_2, state, city, zip_code, phone_no, landmark, create_ts, customer_general_information_id,selected_address_type_id,gst_no,address_type) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	public static final String GET_CUSTOMER_DELIVERY_ADDRESS_INFO = "SELECT attention, country, address_1, address_2, state, city, zip_code, phone_no, landmark,selected_address_type_id,gst_no,address_type FROM customer_delivery_address where customer_general_information_id = ? " ;
	public static final String UPDATE_CUSTOMER_DELIVERY_ADDRESS ="update customer_delivery_address set attention = ? , country=? , address_1=? , address_2=? , state=? , city=?, zip_code=?, phone_no=?, landmark=?, update_ts=?,selected_address_type_id=?,gst_no=?,address_type=?  where customer_general_information_id = ? ";
	public static final String INSERT_INTO_CUSTOMER_ADDITIONAL_ADDRESS = "insert into customer_additional_address(attention,country,address_1,address_2,state,city,zip_code,phone_no,landmark,customer_general_information_id,selected_address_type_id,gst_no,address_type)values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
	
	// Queries for customer_contact
	public static final String INSERT_INTO_CUSTOMER_CONTACT = "insert into customer_contact(salutation, first_name, last_name, work_no, mobile_no, email, status, create_ts, customer_general_information_id) values (?,?,?,?,?,?,?,?,?)";
	public static final String GET_CUSTOMER_CONTACT_INFO = "SELECT id, salutation, first_name, last_name, work_no, mobile_no, email, status FROM customer_contact where customer_general_information_id = ? and status not in ('DEL')";
	public static final String MODIFY_CUSTOMER_CONTACT_STATUS = "update customer_contact set status = ? , update_ts=? where customer_general_information_id = ?";
	public static final String UPDATE_CUSTOMER_CONTACT = "update customer_contact set salutation=?, first_name=?, last_name=?, work_no=?, mobile_no=?, email=?, status=?, update_ts=? where id=? ";
	public static final String MODIFY_CUSTOMER_SINGLE_CONTACT_STATUS = "update customer_contact set status = ? , update_ts=? where id = ?";

	// Queries for additional Address
	public static final String GET_CUSTOMER_ADDITIONAL_ADDRESS_BY_CUSTOMER_ID = "select id,attention,country,address_1,address_2,state,city,zip_code,phone_no,landmark,selected_address_type_id,gst_no,address_type from customer_additional_address where customer_general_information_id=?";

	// Queries for Finance
	public static final String INSERT_INTO_CUSTOMER_FINANCE = "insert into customer_finance(currency_id,payment_terms_id,tds_id,opening_balance,create_ts,customer_general_information_id)values(?,?,?,?,?,?)";
	
	// Queries for customer_bank_details
	public static final String INSERT_INTO_CUSTOMER_BANK_DETAILS = "insert into customer_bank_details(bank_name,branch_name  , account_holder_name, account_no, ifsc_code, upi_id, is_default, status, create_ts,customer_general_information_id) values (?,?,?,?,?,?,?,?,?,?)" ;
	public static final String GET_CUSTOMER_BANK_INFO = "SELECT id, bank_name,branch_name  , account_holder_name, account_no, ifsc_code, upi_id, is_default, status FROM customer_bank_details where customer_general_information_id = ? and status not in ('DEL')" ;
	public static final String MODIFY_CUSTOMER_BANK_INFO_STATUS = "update customer_bank_details set status = ? , update_ts=? where customer_general_information_id =?";
	public static final String UPDATE_CUSTOMER_BANK_INFO ="update customer_bank_details set bank_name=? ,branch_name=? , account_holder_name=?, account_no=?, ifsc_code=?, upi_id=?, is_default=?, status=?, update_ts=? where id=? ";
	public static final String MODIFY_CUSTOMER_SINGLE_BANK_INFO_STATUS = "update customer_bank_details set status = ? , update_ts=? where id =?";
	public static final String IS_BANK_DETAIL_EXISTS = "select * from customer_bank_details bd where bd.customer_general_information_id =? and bd.account_no = ? and bd.ifsc_code = ? and bd.status not in ('DEL')" ;
	public static final String GET_CUSTOMER_DEFAULT_BANK_INFO = "SELECT id, bank_name,branch_name  , account_holder_name, account_no, ifsc_code, upi_id, is_default, status FROM customer_bank_details where customer_general_information_id = ? and status not in ('DEL') and is_default = 1" ;

	
	public static final String IS_EMAIL_ALREADY_EXIST_FOR_ORGANIZATION = "select * from customer_general_information cg where cg.organization_id =? and cg.email_id = ? and cg.status not in ('DEL') ";
	public static final String GET_EMAIL_FOR_CUTOMER_ID = "select email_id from customer_general_information cg where cg.id = ?";

	public static final String IS_GST_ALREADY_EXIST_FOR_ORGANIZATION = "select * from customer_general_information cg where cg.organization_id =? and cg.gst_number = ? and cg.status not in ('DEL') ";
	public static final String GET_CUSTOMER_LIST ="select gi.id,gi.company_name,gi.customer_display_name,gi.primary_contact,gi.phone_no,gi.mobile_no,ba.city,gi.status from customer_general_information gi join customer_billing_address ba on gi.id = ba.customer_general_information_id where gi.organization_id = ? order by gi.id desc ";	
	public static final String GET_CUSTOMER_LIST_USER_ROLE ="select gi.id,gi.company_name,gi.customer_display_name,gi.primary_contact,gi.phone_no,gi.mobile_no,ba.city,gi.status from customer_general_information gi join customer_billing_address ba on gi.id = ba.customer_general_information_id where gi.organization_id = ? and gi.user_id=? and gi.role_name=? order by gi.id desc ";	
	public static final String GET_CUSTOMER_ORGANIZATION ="select gi.id,gi.company_name,gi.customer_display_name,gi.primary_contact,gi.phone_no,gi.mobile_no,ba.city,gi.status from customer_general_information gi join customer_billing_address ba on gi.id = ba.customer_general_information_id where gi.organization_id = ? order by gi.id desc ";	

	
	public static final String GET_GST_NUMBER_FOR_CUTOMER_ID = "select gst_number from customer_general_information cg where cg.id = ?";
	public static final String GET_CURRENCY_FOR_CUSTOMER_ID = "SELECT co.alternate_symbol  FROM accounts_receivable.customer_general_information cgi join usermgmt.currency_organization co on co.id =cgi.currency_id where cgi.id = ?";
	
	
	
	public static final String UPDATE_GST_NUMBER_FOR_CUTOMER_ID = "update customer_general_information set gst_number =? , update_ts=?, user_id=? where id =?";

	public static final String GET_BASIC_CUSTOMER_DETAIL = "SELECT id,customer_display_name,company_name FROM customer_general_information where organization_id = ? and status not in ('DEL')";
	public static final String GET_CUSTOMER_Name="select customer_display_name from customer_general_information where id=?";

	public static final String CUSTOMER_ALREADY_EXISTS = "Customer Exist for the Organization";
	public static final String EMAIL_ALREADY_EXISTS = "Email already Exist for the Organization";
	public static final String CUSTOMER_BANK_DETAILS_ALREADY_EXISTS = "Bank Details already exist for the Customer";
	public static final String GET_CUSTOMER_ID="select id from customer_general_information where organization_id=? and email_id=?";
	public static final String GET_CUSTOMER_ID_BY_DISPLAYNAME="select id from customer_general_information where organization_id=? and customer_display_name=?";

	
	public static final String GET_CUSTOMER_BANK_DETAILS_EXPORT_BY_ID="select account_holder_name,account_no,bank_name,branch_name,ifsc_code,upi_id,is_default from customer_bank_details where customer_general_information_id=?";
	public static final String GET_CUSTOMER_CONTACT_DETAILS_EXPORT_BY_ID="select salutation,first_name,last_name,work_no,mobile_no,email from customer_contact where customer_general_information_id=?";
	public static final String GET_CUSTOMER_GENERAL_INFO_EXPORT_BY_ID="select primary_contact,company_name,customer_display_name,email_id,phone_no,mobile_no,website,pan_no,org_constitution_id,customer_group_id,gst_treatment_id,gst_number,currency_id,opening_balance,payment_terms_id,status from customer_general_information where id=?";
	public static final String GET_CUSTOMER_BILLING_ADDRESS_EXPORT_BY_ID="select attention,phone_no,country,address_1,address_2,landmark,state,city,zip_code from customer_billing_address where customer_general_information_id=?";
	public static final String GET_CUSTOMER_DELIVERY_ADDRESS_EXPORT_BY_ID="select attention,phone_no,country,address_1,address_2,landmark,state,city,zip_code from customer_delivery_address where customer_general_information_id=?";
	public static final String GET_CUSTOMERS_FOR_ORGANIZATION = "select gi.id,gi.customer_display_name from customer_general_information gi where gi.organization_id = ?";
	public static final String GET_BASIC_CUSTOMER_FOR_ORG = "SELECT id,customer_display_name,currency_id FROM customer_general_information where organization_id = ? and status=?";
	public static final String GET_ACTIVE_CUSTOMER_DETAIL = "SELECT id,customer_display_name,currency_id FROM customer_general_information where organization_id = ? and status in (?)";
	public static final String GET_CUSTOMER_FINANCE_BY_CUST_ID = "select id,currency_id,payment_terms_id,tds_id,opening_balance from customer_finance where customer_general_information_id=?";
	
	public static final String DELETE_CUSTOMER_BANK_DETAILS = "delete from customer_bank_details where id=?";
	public static final String DELETE_CUSTOMER_CONTACT = "delete from customer_contact where id=?";
	public static final String DELETE_CUSTOMER_ADDITIONAL_ADDRESS = "delete from customer_additional_address where customer_general_information_id=?";
	public static final String DELETE_CUSTOMER_BILLING_ADDRESS = "delete from customer_billing_address where customer_general_information_id=?";
	public static final String DELETE_CUSTOMER_DELIVERY_ADDRESS = "delete from customer_delivery_address where customer_general_information_id=?";
	
}

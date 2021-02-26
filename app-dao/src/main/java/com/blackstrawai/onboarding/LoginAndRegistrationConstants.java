package com.blackstrawai.onboarding;

public class LoginAndRegistrationConstants {
	
	public static final String REGISTRATION_EMAIL_PASSWORD="select id,first_name,last_name,full_name,email_id,"
			+ "mobile_no,phone_no,is_individual,is_organizaton,subscription_id,password from registration where email_id=?";
	public static final String INSERT_INTO_REGISTRATION="insert into registration"
			+ "(first_name,last_name,full_name,email_id,mobile_no,phone_no,is_individual,is_organizaton,subscription_id,password,token,access_data)"
			+ "values(?,?,?,?,?,?,?,?,?,?,?,?)";
	public static final String GET_ALL_REGISTRATIONS="select id,first_name,last_name,full_name,email_id,"
			+ "mobile_no,phone_no,is_individual,is_organizaton,subscription_id from registration";
	public static final String REGISTRATION_EMAIL="select id,first_name,last_name,full_name,email_id,mobile_no,"
			+ "phone_no,is_individual,is_organizaton,subscription_id from registration where email_id=?";
	public static final String REGISTRATION_EMAIL_REGISTRATION="select id,first_name,last_name,full_name,email_id,mobile_no,"
			+ "phone_no,is_individual,is_organizaton,subscription_id,access_data from registration where email_id=?";
	public static final String UPDATE_REGISTRATION_TOKEN="update registration set token=? where email_id=?";
	public static final String CHECK_REGISTRATION_TOKEN_EXIST="select email_id from registration where token=?";
	public static final String UPDATE_REGISTRATION_PASSWORD="update registration set password=? where token=?";
	public static final String GET_REGISTRATION_GIVEN_ID="select id,first_name,last_name,full_name,email_id,mobile_no,"
			+ "phone_no,is_individual,is_organizaton,subscription_id from registration where id=?";
	public static final String UPDATE_USER_PROFILE = "update registration set first_name=?,last_name=?,mobile_no=? where email_id=?";
	public static final String UPDATE_REGISTRATION_PASSWORD_BY_EMAIL="update registration set password=? where email_id=?";
	public static final String UPDATE_USER_PASSWORD_BY_EMAIL="update user set password=? where email_id=?";
	
	public static final String GET_USER_PWD_BY_EMAIL = "SELECT PASSWORD FROM user WHERE email_id=?";
	public static final String GET_REGISTRATION_PWD_BY_EMAIL="SELECT PASSWORD FROM registration WHERE email_id=?";
	public static final String GET_KEY_CONTACT_REG_PWD_BY_EMAIL="SELECT PASSWORD FROM key_contacts_registration WHERE email_id=?";
	
	// Key contact tables 
	public static final String GET_EMAIL_FOR_TOKEN__IN_KEY_CONTACT = "select email_id from key_contacts_registration where token = ? ";
	
	public static final String UPDATE_PASSWORD_IN_KEY_CONTACT ="update key_contacts_registration set password =? ,token=? where email_id = ? ";

	public static final String KEY_CONTACTS_REGISTRATION_EMAIL_PASSWORD = "select id,name,phone_no,password from key_contacts_registration where email_id=?";
	
	public static final String UPDATE_KEY_CONTACTS__REGISTRATION_TOKEN="update key_contacts_registration set token=? where email_id=?";
	public static final String CHECK_EMAIL_REGISTRATION = "select password from registration where email_id=?";
	public static final String UPDATE_KEY_CONTACTS_PROFILE = "update key_contacts_registration set first_name=?,last_name=?,phone_no=? where email_id=?";
	public static final String UPDATE_KEY_CONTACTS_PASSWORD_BY_EMAIL="update key_contacts_registration set password=? where email_id=?";
	public static final String GET_PROFILE_REGISTRATION_BY_EMAIL = "select first_name,last_name,mobile_no,email_id from registration where email_id=?";
	public static final String GET_PROFILE_USER_BY_EMAIL = "select first_name,last_name,phone_no,email_id from user where email_id=?";
	public static final String GET_PROFILE_KEY_CONTACTS_BY_EMAIL = "select first_name,last_name,phone_no,email_id from key_contacts_registration where email_id=?";
	public static final String CHECK_ORG_DEFAULT_EXIST_FOR_EMAIL = "select id from organization_default where email=?";
	public static final String CREATE_ORG_DEFAULT = "insert into organization_default(email,organization_id) values(?,?)";
	public static final String UPDATE_ORG_DEFAULT = "update organization_default set email=?,organization_id=? where id=?";

	public static final String GET_REGISTERED_USER_NAME = "SELECT concat(first_name ,' ', last_name)  FROM usermgmt.registration where id = ? ";
}

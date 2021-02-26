package com.blackstrawai.onboarding;

public class UserConstants {

	public static final String INSERT_INTO_USER = "insert into user(organization_id,first_name,last_name,email_id,gender,dob,phone_no,status,"
			+ "role_id,access_data,create_user_id,create_role_name)values(?,?,?,?,?,?,?,?,?,?,?,?)";
	public static final String INSERT_INTO_USER_ADDRESS = "insert into user_address(address_1,address_2,city,state,country,pin_code,user_id)"
			+ "values(?,?,?,?,?,?,?)";
	public static final String DELETE_USER = "update user set status=?,update_ts=?,update_user_id=?,update_role_name=? where id=?";
	public static final String GET_USERS_BY_ORGANIZATION = "select u1.id,u1.organization_id,u1.first_name,u1.last_name,u1.email_id,"
			+ "u1.gender,u1.dob,u1.phone_no,u1.status,u1.role_id,u2.id,u2.address_1,u2.address_2,u2.city,u2.state,u2.country,u2.pin_code,"
			+ "u2.is_registered,u2.is_billing,u1.create_ts,u1.update_ts from user u1, user_address u2 where u1.id=u2.user_id and u1.organization_id=? and u1.status not in(?)";
	
	public static final String GET_USERS_BY_ORGANIZATION_USER_ROLE = "select u1.id,u1.organization_id,u1.first_name,u1.last_name,u1.email_id,"
			+ "u1.gender,u1.dob,u1.phone_no,u1.status,u1.role_id,u2.id,u2.address_1,u2.address_2,u2.city,u2.state,u2.country,u2.pin_code,"
			+ "u2.is_registered,u2.is_billing,u1.create_ts,u1.update_ts from user u1, user_address u2 where u1.id=u2.user_id and u1.organization_id=? and u1.status not in(?) and u1.create_user_id=? and u1.create_role_name=?";
	
	
	public static final String GET_USER_IN_ORGANIZATION = "select u1.id,u1.organization_id,u1.first_name,u1.last_name,u1.email_id,"
			+ "u1.gender,u1.dob,u1.phone_no,u1.status,u1.role_id,u2.id,u2.address_1,u2.address_2,u2.city,u2.state,"
			+ "u2.country,u2.pin_code,u2.is_registered,u2.is_billing,u1.create_ts,u1.update_ts,u1.access_data from user u1, user_address u2 "
			+ "where u1.id=u2.user_id and u1.id=? and u1.organization_id=? and u1.status not in(?)";
	public static final String CHECK_USER_EXIST_FOR_ORGANIZATION = "select * from user where email_id=? and phone_no=? and organization_id=?";
	public static final String CHECK_USER_EXIST_FOR_ORGANIZATION_WITHEMAIL = "select * from user where email_id=? and organization_id=?";
	public static final String CHECK_EMAIL_PHONE_EXIST_FOR_ORGANIZATION = "select * from user where id not in(?)"
			+ " and email_id=? and phone_no=? and organization_id=?";
	public static final String CHECK_USER_EXIST = "select u1.id,u1.organization_id,u1.first_name,u1.last_name,u1.email_id,"
			+ "u1.gender,u1.dob,u1.phone_no,u1.status,u1.role_id,u2.id,u2.address_1,u2.address_2,u2.city,"
			+ "u2.state,u2.country,u2.pin_code,u2.is_registered,u2.is_billing,u1.password from user u1, user_address u2 "
			+ "where u1.id=u2.user_id and u1.email_id=?";
	public static final String UPDATE_USER = "update user set first_name=?,last_name=?,email_id=?,gender=?,dob=?,"
			+ "phone_no=?,status=?,role_id=?,update_ts=?,access_data=?,update_user_id=?,update_role_name=? where id=?";
	public static final String UPDATE_USER_ADDRESS = "update user_address set address_1=?,address_2=?,city=?,state=?,"
			+ "country=?,pin_code=?,update_ts=? where id=?";
	public static final String UPDATE_USER_PROFILE = "update user set first_name=?,last_name=?,phone_no=? where email_id=?";
	public static final String USER_EMAIL = "select * from user where email_id=?";
	public static final String USER_BASIC_DETAILS_BY_ID = "select id,first_name,last_name,email_id,phone_no,role_id,(SELECT NAME FROM role_organization WHERE id=role_id) AS role_name from user where id=? and status not in(?)";
	public static final String USER_EMAIL_ORGANIZATION = "select * from user where organization_id=? and email_id=?";
	public static final String UPDATE_USER_TOKEN = "update user set token=? where email_id=?";
	public static final String UPDATE_USER_TOKEN_ID = "update user set token=? where id=?";
	public static final String UPDATE_USER_PASSWORD = "update user set password=? where token=?";
	public static final String GET_USER_COUNT_ORGANIZATION = "select count(*)from user where organization_id=? and status not in(?)";
	public static final String GET_USER_COUNT_FOR_ROLE = "select count(*)from user where role_id=? and status not in(?)";
	public static final String GET_ACTIVE_USERS_FOR_ROLE = "SELECT id,first_name,last_name,email_id,status from user where role_id=? and STATUS =?";
	public static final String GET_USERS_FOR_ROLE_ORGANIZATION = "select id,first_name,last_name,email_id from user WHERE organization_id=? and role_id=? and status not IN(?)";
	public static final String CHECK_USER_TOKEN_EXIST = "select * from user where token=?";
	public static final String GET_ORGANIZATIONS_ACTIVE_USER = "select organization_id from user where email_id=? and status not in(?)";
	public static final String GET_BASIC_USER_DETAILS_OF_ORGANIZATION = "select id,first_name,last_name,email_id,status from user where organization_id=? and status not in(?)";
	public static final String GET_ACTIVE_USER_DETAILS_OF_ORGANIZATION = "select id,first_name,last_name,email_id,status from user where organization_id=? and status=?";
	public static final String GET_USER_NAME = "select concat(first_name,' ',last_name) from user where organization_id=? and id=?";
	public static final String UPDATE_USER_ACCESS_DATA="update user set access_data=?,update_user_id=?,update_role_name=? where id=?";
	public static final String INSERT_INTO_USER_INVITE = "INSERT INTO invite_users (role_id, email_id, message, organization_id, user_id, role_name, status, create_ts) VALUES (?,?,?,?,?,?,?,?)";
	public static final String PENDING = "Pending";
	public static final String DRAFT = "DRAFT";
	public static final String LIST_USER_INVITES_OF_ORG = "SELECT email_id,create_ts,role_name,status,id,role_id,reason FROM invite_users WHERE organization_id=? ORDER BY id DESC";
	public static final String LIST_USER_INVITES_OF_ORG_USER_ROLE = "SELECT email_id,create_ts,role_name,status,id,role_id,reason FROM invite_users WHERE organization_id=? AND user_id=? AND role_name=? ORDER BY id DESC";
	public static final String EXP = "EXP";
	public static final String EXPIRED = "Expired";
	public static final String ACT = "ACT";
	public static final String ACCEPTED = "Accepted";
	public static final String WDRW="WDRW";
	public static final String WITHDRAW="Withdraw";
	public static final String UPDATE_USER_INVITE = "update invite_users set status=?,update_ts=?,update_user_id=?,update_role_name=? where id=?";
	public static final String GET_INVITES_BY_EMAIL_USER_ROLE = "SELECT id,user_id,organization_id,create_ts,role_id,status,role_name,message FROM invite_users WHERE email_id=?";
	public static final String UPDATE_ACTION_INVITE = "UPDATE invite_users SET status=?,reason=?,update_ts=? WHERE id=?";
	public static final String ACCEPT = "Accept";
	public static final String DECLINE = "Decline";
	public static final String DECL = "DECL";
	public static final String DECLINED = "Declined";
	public static final String LIST_SINGLE_USER_INVITE = "SELECT email_id,role_id,organization_id,user_id,role_name FROM invite_users WHERE id=? ORDER BY id DESC";
	
}

package com.blackstrawai.ar;

public class LUTConstants {
	public static final String GET_LUT_BY_ORGANIZATION_USER_ROLE = "SELECT id,ack_no,date,expiry_date,location_id,gst_no,financial_year,status,create_ts,update_ts,isSuperAdmin,user_id,organization_id,role_name FROM letter_of_undertaking WHERE organization_id=? and user_id=? and role_name=?  ORDER BY create_ts DESC";
	public static final String GET_LUT_BY_ORGANIZATION = "SELECT id,ack_no,date,expiry_date,location_id,gst_no,financial_year,status,create_ts,update_ts,isSuperAdmin,user_id,organization_id,role_name FROM letter_of_undertaking WHERE organization_id=? ORDER BY create_ts DESC";
	public static final String GET_LUT_BY_ID = "SELECT id,ack_no,date,expiry_date,location_id,gst_no,financial_year,status,create_ts,update_ts,isSuperAdmin,user_id,organization_id,role_name FROM letter_of_undertaking WHERE id=?";
	public static final String GET_BASIC_LUT = "SELECT id,ack_no,date FROM accounts_receivable.letter_of_undertaking where organization_id = ? and  financial_year =? and status =? ";
	public static final String INSERT_INTO_LUT = "insert into letter_of_undertaking(ack_no,date,expiry_date,location_id,gst_no,financial_year,status,create_ts,update_ts,isSuperAdmin,user_id,organization_id,role_name)values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
	public static final String CHECK_LUT_ORGANIZATION = "SELECT id FROM letter_of_undertaking WHERE organization_id=? and gst_no=? and financial_year=?";
	public static final String UPDATE_LUT_ORGANIZATION = "update letter_of_undertaking set ack_no=?,date=?,expiry_date=?,location_id=?,gst_no=?,financial_year=?,status=?,update_ts=?,isSuperAdmin=?,update_user_id=?,organization_id=?,update_role_name=? where id=?";
	public static final String DELETE_LUT_ORGANIZATION = "update letter_of_undertaking set status=?,update_ts=?,update_user_id=?,update_role_name=? where id=?";
	
}

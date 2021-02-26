package com.blackstrawai.externalintegration.compliance;

public class TaxillaConstants {

	public static final String GET_OTP = "SELECT otp_no FROM usermgmt.otp WHERE organization_id = ? AND user_id = ? AND role_name = ? AND status = ? AND type = ? AND sub_type = ?";
	public static final String GET_ACTION = "SELECT action FROM gst.taxilla_action WHERE gst_search_type = ?";
	public static final String SET_OTP = "INSERT INTO usermgmt.otp (type, sub_type, organization_id, status, user_id, role_name, otp_no) VALUES (?,?,?,?,?,?,?)";

}

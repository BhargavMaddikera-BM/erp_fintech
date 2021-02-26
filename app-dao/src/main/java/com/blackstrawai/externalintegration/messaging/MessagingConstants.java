package com.blackstrawai.externalintegration.messaging;

public class MessagingConstants {

	public static final String SAVE_OTP_REQUEST = "insert into otp(organization_id,user_id,role_name,mobile_no,request_id,type,sub_type) VALUES (?,?,?,?,?,?,?)";
	public static final String UPDATE_OTP_REQUEST = "update otp set status=? where mobile_no=? and type=? and sub_type=?";

}

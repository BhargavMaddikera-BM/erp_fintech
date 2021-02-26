package com.blackstrawai.gst;

public class GstConstant {
	
	
	private GstConstant() {
		super();
	}

	public static final String INSERT_GST_FOR_GIVEN_PAN = "INSERT INTO pan_gst_mapping(pan_number,gst_number,organization_id,user_id,role_name) VALUES (?,?,?,?,?)";
	public static final String GET_GST_FROM_PAN = "select gst_number from pan_gst_mapping where organization_id =? and pan_number = ?";
}

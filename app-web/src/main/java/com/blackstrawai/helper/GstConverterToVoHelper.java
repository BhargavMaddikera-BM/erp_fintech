package com.blackstrawai.helper;

import com.blackstrawai.gst.PanGstVo;
import com.blackstrawai.request.gst.PanGstRequest;

public class GstConverterToVoHelper {

	
	private static GstConverterToVoHelper gstConverterToVoHelper;

	public static GstConverterToVoHelper getInstance() {
		if (gstConverterToVoHelper == null) {
			gstConverterToVoHelper = new GstConverterToVoHelper();
		}
		return gstConverterToVoHelper;
	}
	
	
	public PanGstVo convertPanGstVoToPanGstRequest(PanGstRequest panGstRequest, String panNo) {

		PanGstVo panGstVo = new PanGstVo();

		if (panGstRequest.getGstNumber() != null)
			panGstVo.setGstNumber(panGstRequest.getGstNumber().trim());
		if (panGstRequest.getRoleName() != null)
			panGstVo.setRoleName(panGstRequest.getRoleName().trim());

		panGstVo.setOrganizationId(panGstRequest.getOrganizationId());
		panGstVo.setUserId(panGstRequest.getUserId());
		panGstVo.setPanNo(panNo);

		return panGstVo;

	}
}

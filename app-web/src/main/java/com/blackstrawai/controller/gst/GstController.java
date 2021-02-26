package com.blackstrawai.controller.gst;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blackstrawai.common.BaseController;
import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.common.Constants;
import com.blackstrawai.gst.GstService;
import com.blackstrawai.gst.PanGstVo;
import com.blackstrawai.helper.GstConverterToVoHelper;
import com.blackstrawai.request.JSONObject;
import com.blackstrawai.request.gst.PanGstRequest;
import com.blackstrawai.response.gst.PanGstResponse;

@RestController
@CrossOrigin
@RequestMapping("/decifer/gst")
public class GstController extends BaseController {

	private Logger logger = Logger.getLogger(GstController.class);

	@Autowired
	private GstService gstService;

	@PostMapping(value = "/v1/panNumber/{panNo}/gstNumber")
	public ResponseEntity<BaseResponse> createGstNumbersForPanNumber(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @RequestBody JSONObject<PanGstRequest> panGstRequest,
			@PathVariable String panNo) {

		logger.info("Entry into method:createGstNumbersForPanNumber");
		BaseResponse response = new PanGstResponse();
		try {
			logger.info("Request Payload:" + generateRequestAndResponseLogPaylod(panGstRequest));
			PanGstVo panGstVo = GstConverterToVoHelper.getInstance()
					.convertPanGstVoToPanGstRequest(panGstRequest.getData(), panNo);
//			gstService.createGstForPan(panGstVo);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			panGstVo.setKeyToken(null);
			panGstVo.setValueToken(null);
			((PanGstResponse) response).setData(panGstVo);
			response = constructResponse(response, Constants.SUCCESS, Constants.PAN_FOR_GST_CREATED_SUCCESS,
					Constants.PAN_FOR_GST_CREATED_SUCCESS, Constants.PAN_FOR_GST_CREATED_SUCCESSFULLY);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			if (e.getCause() != null) {
				response = constructResponse(response, Constants.FAILURE, Constants.FAILED_PAN_FOR_GST_CREATE,
						e.getCause().getMessage(), Constants.PAN_FOR_GST_CREATED_UNSUCCESSFULLY);
			} else {
				response = constructResponse(response, Constants.FAILURE, Constants.FAILED_PAN_FOR_GST_CREATE,
						e.getMessage(), Constants.PAN_FOR_GST_CREATED_UNSUCCESSFULLY);
			}
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

}

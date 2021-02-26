package com.blackstrawai.controller.externalintegration.messaging;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.blackstrawai.common.BaseController;
import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.common.Constants;
import com.blackstrawai.externalintegration.messaging.MessagingService;
import com.blackstrawai.msg91.MessagingApi;
import com.blackstrawai.response.externalintegration.messaging.MessagingResponse;

@RestController
@CrossOrigin
@RequestMapping("/decifer/messaging")
public class MessagingController extends BaseController {

	private static final Logger logger = Logger.getLogger(MessagingController.class);

	@Autowired
	private MessagingService messagingService;

	@RequestMapping(value = "/v1/otp/send/{organizationId}/{userId}/{roleName}/{type}/{subType}/{key1}/{key2}", method = RequestMethod.GET)
	public ResponseEntity<BaseResponse> sendOtp(HttpServletRequest httpRequest, HttpServletResponse httpResponse,
			@PathVariable int organizationId, @PathVariable int userId, @PathVariable String roleName,
			@PathVariable String type,@PathVariable String subType,@PathVariable String key1,@PathVariable String key2) {
		logger.info("Entered Into sendOtp");
		BaseResponse response = new MessagingResponse();
		HttpStatus httpStatus=HttpStatus.OK;
		try {
			String mobileNo=null;
			if(type!=null && subType!=null && type.equalsIgnoreCase("Yes Bank")&& subType.equalsIgnoreCase("Make Payment")) {
				mobileNo=messagingService.getYesBankRegisteredMobileNo(organizationId, userId, roleName, key1, key2);
			}
			if(mobileNo!=null) {
				String responseJson=MessagingApi.getInstance().sendOtp(mobileNo);
				JSONParser parser=new JSONParser();
				JSONObject responseObj = (JSONObject) parser.parse(responseJson);
				Object reqId=responseObj.get("request_id");
				Object status=responseObj.get("type");
				Object message=responseObj.get("message");
				if(null!=responseObj && null!=reqId && status!=null && status.toString().equals("success")) {//For Success
					messagingService.saveOtpRequest(organizationId, userId, roleName, mobileNo, reqId.toString(),type, subType);
					((MessagingResponse)response).setMobileNo(mobileNo);
					setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
					response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_SEND_OTP,
							Constants.SUCCESS_SEND_OTP, Constants.OTP_SENT_SUCCESSFULLY);
					logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
				}else {
					response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_SEND_OTP, message!=null?message.toString():"",
							Constants.OTP_SENT_UNSUCCESSFULLY);
					logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
					httpStatus=HttpStatus.INTERNAL_SERVER_ERROR;
				}
			}
		} catch (Exception e) {
			logger.error(":::Exception in sendOtp:::" + e);
			String errorMessage = e.getCause() != null ? e.getCause().getMessage() : e.getMessage();
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_SEND_OTP, errorMessage,
					Constants.OTP_SENT_UNSUCCESSFULLY);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			httpStatus=HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<BaseResponse>(response, httpStatus);

	}

	@RequestMapping(value = "/v1/otp/verify/{organizationId}/{userId}/{roleName}/{mobileNo}/{otp}/{type}/{subType}", method = RequestMethod.GET)
	public ResponseEntity<BaseResponse> verifyOtp(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse,@PathVariable int organizationId, @PathVariable String userId, @PathVariable String roleName,
			@PathVariable String mobileNo,@PathVariable String otp,@PathVariable String type,@PathVariable String subType) {
		logger.info("Entry into verifyOtp");
		BaseResponse response = new MessagingResponse();
		HttpStatus httpStatus=HttpStatus.OK;
		try {
			String responseJson=MessagingApi.getInstance().verifyOtp(mobileNo, otp);
			JSONParser parser=new JSONParser();
			JSONObject responseObj = (JSONObject) parser.parse(responseJson);
			Object message=responseObj.get("message");
			Object status=responseObj.get("type");
			String exceptionMessage="";
			if(null!=responseObj && null!=message && status!=null && status.toString().equals("success") && message.toString().equalsIgnoreCase("OTP verified success")) {//For Success
				messagingService.updateOtp(mobileNo,type,subType);
				response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_VERIFY_OTP,
						Constants.SUCCESS_VERIFY_OTP, Constants.OTP_VERIFY_SUCCESSFULLY);
				logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));	
			}else {
				if(message.toString().equals("OTP Not Match")|| message.toString().equals("OTP not match")){
					exceptionMessage="Invalid OTP.Please enter correct OTP or try again";
				}else{
					exceptionMessage=message.toString();
				}
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_VERIFY_OTP, exceptionMessage,
						Constants.OTP_VERIFY_UNSUCCESSFULLY);
				logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
				httpStatus=HttpStatus.INTERNAL_SERVER_ERROR;	

			}
		} catch (Exception e) {

			String errorMessage = e.getCause() != null ? e.getCause().getMessage() : e.getMessage();
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_VERIFY_OTP, errorMessage,
					Constants.OTP_VERIFY_UNSUCCESSFULLY);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			httpStatus=HttpStatus.INTERNAL_SERVER_ERROR;

		}
		return new ResponseEntity<BaseResponse>(response, httpStatus);
	}


}

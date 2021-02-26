package com.blackstrawai.controller.externalintegration.general;

import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blackstrawai.aadhar.AadhaarApi;
import com.blackstrawai.common.BaseController;
import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.common.Constants;
import com.blackstrawai.externalintegration.general.aadhaar.AadhaarBankAdvanceVo;
import com.blackstrawai.externalintegration.general.aadhaar.AadhaarBankBasicVo;
import com.blackstrawai.externalintegration.general.aadhaar.AadhaarGstnAdvanceVo;
import com.blackstrawai.externalintegration.general.aadhaar.AadhaarGstnBasicVo;
import com.blackstrawai.externalintegration.general.aadhaar.AadhaarPanAdvanceVo;
import com.blackstrawai.externalintegration.general.aadhaar.AadhaarPanBasicVo;
import com.blackstrawai.helper.ExternalIntegrationConvertToVoHelper;
import com.blackstrawai.response.externalintegration.general.AadhaarBankAdvanceResponse;
import com.blackstrawai.response.externalintegration.general.AadhaarBankBasicResponse;
import com.blackstrawai.response.externalintegration.general.AadhaarGstnAdvanceResponse;
import com.blackstrawai.response.externalintegration.general.AadhaarGstnBasicResponse;
import com.blackstrawai.response.externalintegration.general.AadhaarPanAdvanceResponse;
import com.blackstrawai.response.externalintegration.general.AadhaarPanBasicResponse;

@RestController
@CrossOrigin
@RequestMapping("/decifer/aadhaarapi")
public class AadhaarAPIController extends BaseController{

	private Logger logger = Logger.getLogger(AadhaarAPIController.class);

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/v1/pan/{panNumber}/{isBasic}")
	public ResponseEntity<BaseResponse> verifyPan(
			HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse,@PathVariable String panNumber,@PathVariable Boolean isBasic) {
		logger.info("Entry into method: verifyPan");
		BaseResponse response = new AadhaarPanBasicResponse();
		try {

			String responseJson=AadhaarApi.getInstance().verifyPAN(panNumber,isBasic);
			logger.info("PAN NUMBER:"+panNumber+",isBasic:"+isBasic+",AAdhaar API Response:" + responseJson);
			JSONParser parser=new JSONParser(); 
			JSONObject responseObj = (JSONObject) parser.parse(responseJson);
			if(null!=responseObj && null!=responseObj.get("response_code") && (responseObj.get("response_code").toString().equals("101") || responseObj.get("response_code").toString().equals("1"))) {//For Success

				ExternalIntegrationConvertToVoHelper voHelper=ExternalIntegrationConvertToVoHelper.getInstance();
				if(isBasic) {//For Basic verison
					JSONObject data = (JSONObject) parser.parse(responseObj.get("data").toString());
					AadhaarPanBasicVo aadharPanVo = voHelper.convertAadhaarPanBasicVoFromJsonData(data);
					aadharPanVo.setMessage(responseObj.get("response_msg")!=null?responseObj.get("response_msg").toString():null);
					setTokens(response,httpServletRequest.getHeader("keyToken"),httpServletRequest.getHeader("valueToken"));
					((AadhaarPanBasicResponse) response).setData(aadharPanVo);
					response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_AADHAR_PAN_FETCH,
							Constants.SUCCESS_AADHAR_PAN_FETCH, Constants.SUCCESS_DURING_GET);

				}else {//For Advanced Version
					JSONArray dataArray = (JSONArray) parser.parse(responseObj.get("data").toString());
					Iterator<JSONObject> iterator = dataArray.iterator();

					while(iterator.hasNext()) {
						JSONObject dataAdvanced=(JSONObject)iterator.next();	
						if(dataAdvanced!=null && dataAdvanced.get("pan_status")!=null && dataAdvanced.get("pan_status").equals("VALID")){
							BaseResponse advResponse = new AadhaarPanAdvanceResponse();
							AadhaarPanAdvanceVo aadharPanAdvancedVo = voHelper.convertAadhaarPanAdvanceVoFromJsonData(dataAdvanced);
							setTokens(response,httpServletRequest.getHeader("keyToken"),httpServletRequest.getHeader("valueToken"));
							((AadhaarPanAdvanceResponse) advResponse).setData(aadharPanAdvancedVo);
							response = constructResponse(advResponse, Constants.SUCCESS, Constants.SUCCESS_AADHAR_PAN_FETCH,
									Constants.SUCCESS_AADHAR_PAN_FETCH, Constants.SUCCESS_DURING_GET);

						}else {//All advance failure scenarios
							setTokens(response,httpServletRequest.getHeader("keyToken"),httpServletRequest.getHeader("valueToken"));
							((AadhaarPanBasicResponse) response).setData(null);
							response = constructResponse(response, Constants.FAILURE, Constants.INVALID_PAN,
									Constants.INVALID_PAN, Constants.FAILURE_DURING_GET);
						}
					}


				}

			}else {//For All negative scenarios
				setTokens(response,httpServletRequest.getHeader("keyToken"),httpServletRequest.getHeader("valueToken"));
				((AadhaarPanBasicResponse) response).setData(null);
				response = constructResponse(response, Constants.FAILURE, Constants.INVALID_PAN,
						Constants.INVALID_PAN, Constants.FAILURE_DURING_GET);

			}

			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (Exception e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_AADHAR_PAN_FETCH, e.getMessage(),
					Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}



	@RequestMapping(value = "/v1/bank/{accNumber}/{ifscCode}/{isBasic}")
	public ResponseEntity<BaseResponse> verifyBankAccount(
			HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse,@PathVariable String accNumber,@PathVariable String ifscCode,@PathVariable Boolean isBasic) {
		logger.info("Entry into method: verifyBankAccount");
		BaseResponse response = new AadhaarBankBasicResponse();
		try {

			String responseJson=AadhaarApi.getInstance().verifyBankAccount(accNumber, ifscCode,isBasic);
			logger.info("ACC NUMBER:"+accNumber+",IFSC:"+ifscCode+",isBasic:"+isBasic+",AAdhaar API Response:" + responseJson);
			JSONParser parser=new JSONParser();
			JSONObject responseObj = (JSONObject) parser.parse(responseJson);
			if(null!=responseObj && null!=responseObj.get("response_code") && responseObj.get("response_code").toString().equals("TXN")) {//For Success
				JSONObject data = (JSONObject) parser.parse(responseObj.get("data").toString());
				ExternalIntegrationConvertToVoHelper voHelper=ExternalIntegrationConvertToVoHelper.getInstance();
				if(isBasic) {
					AadhaarBankBasicVo aadharBankVo = voHelper.convertAadhaarBankBasicVoFromJsonData(data);
					aadharBankVo.setMessage(responseObj.get("response_message").toString());
					setTokens(response,httpServletRequest.getHeader("keyToken"),httpServletRequest.getHeader("valueToken"));
					((AadhaarBankBasicResponse) response).setData(aadharBankVo);
					response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_AADHAR_BANK_FETCH,
							Constants.SUCCESS_AADHAR_BANK_FETCH, Constants.SUCCESS_DURING_GET);
				}else {
					BaseResponse advResponse = new AadhaarBankAdvanceResponse();
					AadhaarBankAdvanceVo aadharBankVo = voHelper.convertAadhaarBankAdvanceVoFromJsonData(data);
					aadharBankVo.setMessage(responseObj.get("response_message")!=null?responseObj.get("response_message").toString():null);
					setTokens(response,httpServletRequest.getHeader("keyToken"),httpServletRequest.getHeader("valueToken"));
					((AadhaarBankAdvanceResponse) advResponse).setData(aadharBankVo);
					response = constructResponse(advResponse, Constants.SUCCESS, Constants.SUCCESS_AADHAR_BANK_FETCH,
							Constants.SUCCESS_AADHAR_BANK_FETCH, Constants.SUCCESS_DURING_GET);
				}
			}else{//For All failure scenarios
				setTokens(response,httpServletRequest.getHeader("keyToken"),httpServletRequest.getHeader("valueToken"));
				((AadhaarBankBasicResponse) response).setData(null);
				response = constructResponse(response, Constants.FAILURE, Constants.INVALID_BANK_ACCOUNT_IFSC,
						Constants.INVALID_BANK_ACCOUNT_IFSC, Constants.FAILURE_DURING_GET);			
			}


			logger.info("AAdhar API Response:" + responseJson);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_AADHAR_BANK_FETCH, e.getMessage(),
					Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}


	@RequestMapping(value = "/v1/gstn/{gstNumber}/{isBasic}")
	public ResponseEntity<BaseResponse> verifyGstn(
			HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse,@PathVariable String gstNumber,@PathVariable Boolean isBasic) {
		logger.info("Entry into method: verifyGstn");
		BaseResponse response = new AadhaarGstnBasicResponse();
		try {

			String responseJson=AadhaarApi.getInstance().verifyGstNumber(gstNumber,isBasic);
			logger.info("GST NUMBER:"+gstNumber+",isBasic:"+isBasic+",AAdhaar API Response:" + responseJson);
			JSONParser parser=new JSONParser(); 
			JSONObject responseObj = (JSONObject) parser.parse(responseJson);
			if(null!=responseObj && null!=responseObj.get("response_code") && responseObj.get("response_code").toString().equals("101") ) {//For Success

				JSONObject data = (JSONObject) parser.parse(responseObj.get("result").toString());
				ExternalIntegrationConvertToVoHelper voHelper=ExternalIntegrationConvertToVoHelper.getInstance();
				if(isBasic) {//For Basic verison

					AadhaarGstnBasicVo aadharGstnBasicVo = voHelper.convertAadhaarGstnBasicVoFromJsonData(data);
					setTokens(response,httpServletRequest.getHeader("keyToken"),httpServletRequest.getHeader("valueToken"));
					((AadhaarGstnBasicResponse) response).setData(aadharGstnBasicVo);
					response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_AADHAR_GSTN_FETCH,
							Constants.SUCCESS_AADHAR_GSTN_FETCH, Constants.SUCCESS_DURING_GET);

				}else {//For Advanced Version
					response=new AadhaarGstnAdvanceResponse();
					AadhaarGstnAdvanceVo gstnAdvanceVo = voHelper.convertAadhaarGstnAdvanceVoFromJsonData(data);
					setTokens(response,httpServletRequest.getHeader("keyToken"),httpServletRequest.getHeader("valueToken"));
					((AadhaarGstnAdvanceResponse) response).setData(gstnAdvanceVo);
					response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_AADHAR_GSTN_FETCH,
							Constants.SUCCESS_AADHAR_GSTN_FETCH, Constants.SUCCESS_DURING_GET);
				}

			}else {//For All negative scenarios
				setTokens(response,httpServletRequest.getHeader("keyToken"),httpServletRequest.getHeader("valueToken"));
				((AadhaarGstnBasicResponse) response).setData(null);
				response = constructResponse(response, Constants.FAILURE, Constants.INVALID_GSTN,
						Constants.INVALID_GSTN, Constants.FAILURE_DURING_GET);

			}

			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (Exception e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_AADHAR_GSTN_FETCH, e.getMessage(),
					Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}


}

package com.blackstrawai.controller.externalintegration.general;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blackstrawai.common.BaseController;
import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.common.Constants;
import com.blackstrawai.externalintegration.general.razorpay.IFSCCodeVo;
import com.blackstrawai.razorpay.IFSCCode;
import com.blackstrawai.response.externalintegration.general.IFSCCodeResponse;



@RestController
@CrossOrigin
@RequestMapping("/decifer/ifsc_code")
public class IFSCCodeController extends BaseController{
	
	private Logger logger = Logger.getLogger(IFSCCodeController.class);
	
	@RequestMapping(value = "/v1/ifsc_codes/{ifsc_code}")
	public ResponseEntity<BaseResponse> getIfscCode(HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse,@PathVariable String ifsc_code) {
		logger.info("Entry into method: getIfscCode");
		BaseResponse response = new IFSCCodeResponse();
		try {
			String data=IFSCCode.getInstance().getBankDetails(ifsc_code);
			JSONParser parser=new JSONParser();
			if(parser.parse(data).equals("Not Found")){
				setTokens(response,httpServletRequest.getHeader("keyToken"),httpServletRequest.getHeader("valueToken"));
				((IFSCCodeResponse) response).setData(null);
				response = constructResponse(response, Constants.FAILURE, Constants.INVALID_BANK_DETAILS_FETCH,
						Constants.INVALID_BANK_DETAILS_FETCH, Constants.FAILURE_DURING_GET);
			}else{
				JSONObject json = (JSONObject) parser.parse(data);
				IFSCCodeVo iFSCCodeVo=new IFSCCodeVo();
				iFSCCodeVo.setBankName((String) json.get("BANK"));
				iFSCCodeVo.setBranchName((String) json.get("BRANCH"));
				setTokens(response,httpServletRequest.getHeader("keyToken"),httpServletRequest.getHeader("valueToken"));
				((IFSCCodeResponse) response).setData(iFSCCodeVo);
				response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_BANK_DETAILS_FETCH,
						Constants.SUCCESS_BANK_DETAILS_FETCH, Constants.SUCCESS_DURING_GET);
			}
			
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (Exception e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_BANK_DETAILS_FETCH, e.getMessage(),
					Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

}

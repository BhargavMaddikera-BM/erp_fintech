package com.blackstrawai.controller.externalintegration.compliance;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.blackstrawai.common.BaseController;
import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.common.Constants;
import com.blackstrawai.externalintegration.compliance.IncomeTaxService;
import com.blackstrawai.externalintegration.compliance.employeeprovidentfund.EmployeeProvidentFundLoginVo;
import com.blackstrawai.helper.ExternalIntegrationConvertToVoHelper;
import com.blackstrawai.request.JSONObject;
import com.blackstrawai.request.externalintegration.compliance.EmployeeProvidentFundRequest;
import com.blackstrawai.response.externalintegration.compliance.EmployeeProvidentFundLoginResponse;

@RestController
@CrossOrigin
@RequestMapping("/decifer/income_tax")
public class IncomeTaxController extends BaseController  {
	@Autowired
	private IncomeTaxService incomeTaxService;
	private Logger logger = Logger.getLogger(IncomeTaxController.class);
	
	/**
	 * API to login to Income Tax module.
	 * 
	 * @param httpRequest
	 * @param httpResponse
	 * @param request
	 * @return Contents of body with password nulled.
	 */
	@RequestMapping(value = "/v1/login", method = RequestMethod.POST)
	public ResponseEntity<BaseResponse> incomeTaxLogin(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @RequestBody JSONObject<EmployeeProvidentFundRequest> request){
		logger.info("Entry into method: incomeTaxLogin");
		BaseResponse response = new EmployeeProvidentFundLoginResponse();
		try {
			EmployeeProvidentFundLoginVo loginVo = ExternalIntegrationConvertToVoHelper.getInstance().convertEpfLoginRequestToVo(request.getData());
			incomeTaxService.createIncomeTaxUser(loginVo);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((EmployeeProvidentFundLoginResponse) response).setData(loginVo);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_LOGIN,
					Constants.SUCCESS_LOGIN, Constants.USER_LOGIN_SUCCESS);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (Exception e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_LOGIN,
					e.getMessage(), Constants.USER_LOGIN_FAILURE);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}

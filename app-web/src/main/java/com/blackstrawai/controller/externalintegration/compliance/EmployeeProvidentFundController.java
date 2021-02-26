package com.blackstrawai.controller.externalintegration.compliance;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.blackstrawai.common.BaseController;
import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.common.Constants;
import com.blackstrawai.externalintegration.compliance.EmployeeProvidentFundService;
import com.blackstrawai.externalintegration.compliance.employeeprovidentfund.EmployeeProvidentFundChallanVo;
import com.blackstrawai.externalintegration.compliance.employeeprovidentfund.EmployeeProvidentFundEcrVo;
import com.blackstrawai.externalintegration.compliance.employeeprovidentfund.EmployeeProvidentFundLoginVo;
import com.blackstrawai.helper.ExternalIntegrationConvertToVoHelper;
import com.blackstrawai.request.JSONObject;
import com.blackstrawai.request.externalintegration.compliance.EmployeeProvidentFundRequest;
import com.blackstrawai.response.externalintegration.compliance.EmployeeProvidentFundChallanResponse;
import com.blackstrawai.response.externalintegration.compliance.EmployeeProvidentFundEcrResponse;
import com.blackstrawai.response.externalintegration.compliance.EmployeeProvidentFundLoginResponse;

@RestController
@CrossOrigin
@RequestMapping("/decifer/employee_provident_fund")
public class EmployeeProvidentFundController extends BaseController {
	@Autowired
	private EmployeeProvidentFundService employeeProvidentFundService;
	
	private Logger logger = Logger.getLogger(EmployeeProvidentFundController.class);

	@RequestMapping(value = "/v1/login", method = RequestMethod.POST)
	public ResponseEntity<BaseResponse> epfLogin(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @RequestBody JSONObject<EmployeeProvidentFundRequest> request){
		logger.info("Entry into method: EpfLogin");
		BaseResponse response = new EmployeeProvidentFundLoginResponse();
		try {
			EmployeeProvidentFundLoginVo loginVo = ExternalIntegrationConvertToVoHelper.getInstance().convertEpfLoginRequestToVo(request.getData());
			loginVo=employeeProvidentFundService.createEpfUser(loginVo);
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
	
	/*@RequestMapping(value = "/v1/login/{organizationId}/{organizationName}/{login}")
	public ResponseEntity<BaseResponse> getLoginDetails(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse,@PathVariable int organizationId,@PathVariable String organizationName
			,@PathVariable String loginName) {
		logger.info("Entry into getLoginDetails");
		BaseResponse response = new EmployeeProvidentFundLoginResponse();
		try {
			EmployeeProvidentFundLoginVo data = employeeProvidentFundService.getLoginDetailsForAnOrganization(organizationId,loginName);
			setTokens(response, httpServletRequest.getHeader("keyToken"), httpServletRequest.getHeader("valueToken"));
			((EmployeeProvidentFundLoginResponse) response).setData(data);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_LOGIN,
					Constants.SUCCESS_LOGIN, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);

		} catch (Exception e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_LOGIN, e.getMessage(),
					Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

*/	
	
	@RequestMapping(value = "/v1/recent_challan/{userId}", method = RequestMethod.GET)
	public ResponseEntity<BaseResponse> getRecentChallans(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @PathVariable int userId)  {
		logger.info("Entry into method: getRecentChallans");
		BaseResponse response = new EmployeeProvidentFundChallanResponse();
		try {
			List<EmployeeProvidentFundChallanVo> challans = employeeProvidentFundService.getRecentChallans(userId);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((EmployeeProvidentFundChallanResponse) response).setData(challans);
			response = constructResponse(response, Constants.SUCCESS, Constants.PF_CHALLAN_FETCH_SUCCESS,
					Constants.PF_CHALLAN_FETCH_SUCCESS, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (Exception e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_PF_CHALLAN_FETCH,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/v1/recent_ecr/{userId}", method = RequestMethod.GET)
	public ResponseEntity<BaseResponse> getRecentECRs(HttpServletRequest httpRequest, HttpServletResponse httpResponse,
			@PathVariable int userId) {
		logger.info("Entry into method: getRecentECRs");
		BaseResponse response = new EmployeeProvidentFundEcrResponse();
		try {
			List<EmployeeProvidentFundEcrVo> ecrs = employeeProvidentFundService.getRecentEcrs(userId);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((EmployeeProvidentFundEcrResponse) response).setData(ecrs);
			response = constructResponse(response, Constants.SUCCESS, Constants.PF_ECR_FETCH_SUCCESS,
					Constants.PF_ECR_FETCH_SUCCESS, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (Exception e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_PF_ECR_FETCH, e.getMessage(),
					Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/v1/recent_challan/refresh/{userId}", method = RequestMethod.PUT)
	public ResponseEntity<BaseResponse> refreshRecentChallan(HttpServletRequest httpRequest, HttpServletResponse httpResponse,
			@PathVariable int userId){
		logger.info("Entry into method: refreshRecentChallan");
		BaseResponse response = new EmployeeProvidentFundChallanResponse();
		try {
			List<EmployeeProvidentFundChallanVo> data = employeeProvidentFundService.refreshChallans(userId);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((EmployeeProvidentFundChallanResponse) response).setData(data);
			response = constructResponse(response, Constants.SUCCESS, Constants.PF_CHALLAN_REFRESH_SUCCESS,
					Constants.PF_CHALLAN_REFRESH_SUCCESS, Constants.PF_REFRESH_SUCCESS);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (Exception e) {
			response = constructResponse(response, Constants.FAILURE, Constants.PF_CHALLAN_REFRESH_FAILURE, e.getMessage(),
					Constants.PF_REFRESH_FAILURE);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	@RequestMapping(value = "/v1/recent_ecr/refresh/{userId}", method = RequestMethod.PUT)
	public ResponseEntity<BaseResponse> refreshRecentEcr(HttpServletRequest httpRequest, HttpServletResponse httpResponse,
			@PathVariable int userId)  {
		logger.info("Entry into method: refreshRecentEcr");
		BaseResponse response = new EmployeeProvidentFundEcrResponse();
		try {
			List<EmployeeProvidentFundEcrVo> data = employeeProvidentFundService.refreshEcr(userId);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((EmployeeProvidentFundEcrResponse) response).setData(data);
			response = constructResponse(response, Constants.SUCCESS, Constants.PF_ECR_REFRESH_SUCCESS,
					Constants.PF_ECR_REFRESH_SUCCESS, Constants.PF_REFRESH_SUCCESS);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (Exception e) {
			response = constructResponse(response, Constants.FAILURE, Constants.PF_ECR_REFRESH_FAILURE, e.getMessage(),
					Constants.PF_REFRESH_FAILURE);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/v1/disconnect/{userId}/{status}", method = RequestMethod.POST)
	public ResponseEntity<BaseResponse> epfDisconnect(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @PathVariable int userId,@PathVariable String status){
		logger.info("Entry into method: epfDisconnect");
		BaseResponse response = new EmployeeProvidentFundLoginResponse();
		try {
			employeeProvidentFundService.disconnectUser(userId,status);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((EmployeeProvidentFundLoginResponse) response).setData(null);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_DISCONNECT,
					Constants.SUCCESS_DISCONNECT, Constants.PF_DISCONNECT_SUCCESS);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (Exception e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_DISCONNECT,
					e.getMessage(), Constants.PF_DISCONNECT_FAILURE);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}

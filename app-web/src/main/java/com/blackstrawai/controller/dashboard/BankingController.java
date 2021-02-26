package com.blackstrawai.controller.dashboard;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.common.BaseController;
import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.common.Constants;
import com.blackstrawai.common.EntityConverter;
import com.blackstrawai.externalintegration.banking.common.BasicAccountDetailsVo;
import com.blackstrawai.externalintegration.banking.common.DashboardService;
import com.blackstrawai.externalintegration.banking.perfios.PerfiosService;
import com.blackstrawai.request.JSONObject;
import com.blackstrawai.request.externalintegration.banking.common.BasicAccountDetailsRequest;
import com.blackstrawai.response.externalintegration.banking.common.BankStatementResponse;
import com.blackstrawai.response.externalintegration.banking.perfios.PerfiosListAccountResponse;



@Controller
@CrossOrigin
@RequestMapping("/decifer/banking_dashboard")
public class BankingController extends BaseController{
	@Autowired
	private PerfiosService perfiosService;
	
	@Autowired
	private EntityConverter entityConverter;
	
	@Autowired
	private DashboardService dashboardService;
	
	

	
	private Logger logger = Logger.getLogger(BankingController.class);
	
	@RequestMapping(value = "/v1/accounts/{organizationId}/{userId}/{roleName}", method = RequestMethod.GET)
	public ResponseEntity<BaseResponse> getAllAccountNamesForUserRole(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @PathVariable int organizationId, @PathVariable int userId, @PathVariable String roleName) {
		logger.info("Entry into method: getAccountNames");
		BaseResponse response = new PerfiosListAccountResponse();
		try {
			List<BasicAccountDetailsVo> accounts = perfiosService.getAllAccountNamesForUserRole(organizationId, userId, roleName);
			
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((PerfiosListAccountResponse) response).setData(accounts);
			response = constructResponse(response, Constants.SUCCESS, Constants.CURRENT_ACCOUNT_FETCH_SUCCESS,
					Constants.CURRENT_ACCOUNT_FETCH_SUCCESS, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_CURRENT_ACCOUNT_FETCH,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	
	@RequestMapping(value = "/v1/accounts/default", method = RequestMethod.POST)
	public ResponseEntity<BaseResponse> setDefaultAccount(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @RequestBody JSONObject<BasicAccountDetailsRequest> defaultAccountRequest) {
		logger.info("Entry into method: setDefaultAccount" +defaultAccountRequest);
		BaseResponse response = new BankStatementResponse();
		try {
			BasicAccountDetailsVo  defaultAccount = entityConverter.convertToEntity(defaultAccountRequest.getData(), BasicAccountDetailsVo.class);
			logger.info(defaultAccount);
			dashboardService.setDefaultAccount(defaultAccount);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_SET_DEFAULT_ACCOUNT,
					Constants.SUCCESS_SET_DEFAULT_ACCOUNT, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_SET_DEFAULT_ACCOUNT,
					e.getMessage(), Constants.FAILURE_SET_DEFAULT_ACCOUNT);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
}

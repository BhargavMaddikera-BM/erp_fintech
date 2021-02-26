package com.blackstrawai.controller.banking;

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

import com.blackstrawai.ApplicationException;
import com.blackstrawai.banking.ContraService;
import com.blackstrawai.banking.contra.ContraBaseVo;
import com.blackstrawai.banking.contra.ContraVo;
import com.blackstrawai.banking.dropdowns.ContraDropDownVo;
import com.blackstrawai.common.BaseController;
import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.common.Constants;
import com.blackstrawai.helper.BankingConvertToVoHelper;
import com.blackstrawai.request.JSONObject;
import com.blackstrawai.request.banking.contra.ContraRequest;
import com.blackstrawai.response.banking.contra.ContraDropDownResponse;
import com.blackstrawai.response.banking.contra.ContraResponse;
import com.blackstrawai.response.banking.contra.ListContraResponse;
import com.blackstrawai.response.banking.dashboard.BankMasterCardResponse;

@RestController
@CrossOrigin
@RequestMapping("/decifer/contra")
public class ContraController extends BaseController {

	@Autowired
	ContraService contraService;

	private Logger logger = Logger.getLogger(ContraController.class);

	// For Creating Contra Accounts
	@RequestMapping(value = "/v1/contras", method = RequestMethod.POST)
	public ResponseEntity<BaseResponse> createContraAccounts(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @RequestBody JSONObject<ContraRequest> contraRequest) {
		logger.info("Entry into method: createContraAccounts");
		BaseResponse response = new ContraResponse();
		try {
			logger.info("Request Payload:" + generateRequestAndResponseLogPaylod(contraRequest));
			ContraVo contraVo = BankingConvertToVoHelper.getInstance()
					.convertContraAccountsVoFromContraAccountsRequest(contraRequest.getData());
			contraService.createContraAccounts(contraVo);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_CONTRA_ACCOUNTS_CREATED,
					Constants.SUCCESS_CONTRA_ACCOUNTS_CREATED, Constants.CONTRA_ACCOUNTS_CREATED_SUCCESSFULLY);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			logger.error("Error Payload:" + e.getMessage());
			if (e.getCause() != null) {
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_CONTRA_ACCOUNTS_CREATED,
						e.getCause().getMessage(), Constants.CONTRA_ACCOUNTS_CREATED_UNSUCCESSFULLY);
			} else {
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_CONTRA_ACCOUNTS_CREATED,
						e.getMessage(), Constants.CONTRA_ACCOUNTS_CREATED_UNSUCCESSFULLY);
			}

			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	// For Fetching all Contra Accounts details
	@RequestMapping(value = "/v1/contras/{organizationId}/{organizationName}/{userId}/{roleName}", method = RequestMethod.GET)
	public ResponseEntity<BaseResponse> getAllContraAccountsOfAnOrganizationForUserAndRole(	
			HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, 
			@PathVariable int organizationId, 
			@PathVariable String organizationName,
			@PathVariable String userId,
			@PathVariable String roleName) {
		logger.info("Entry into method: getAllContraAccountsOfAnOrganization");
		BaseResponse response = new ListContraResponse();
		try {
			List<ContraBaseVo> listContraVo = contraService.getAllContraAccountsOfAnOrganizationForUserAndRole(organizationId,userId,roleName);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((ListContraResponse) response).setData(listContraVo);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_CONTRA_ACCOUNT_FETCH,
					Constants.SUCCESS_CONTRA_ACCOUNT_FETCH, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_CONTRA_ACCOUNT_FETCH,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// For Fetching Contra Accounts detail by id
	@RequestMapping(value = "/v1/contras/{organizationId}/{organizationName}/{userId}/{roleName}/{id}", method = RequestMethod.GET)
	public ResponseEntity<BaseResponse> getContraAccountById(
			HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, 
			@PathVariable int organizationId, 
			@PathVariable String organizationName,
			@PathVariable String userId,
			@PathVariable String roleName,
			@PathVariable int id) {
		logger.info("Entry into method: getContraAccountById");
		BaseResponse response = new ContraResponse();
		try {
			ContraVo contraVo = contraService.getContraAccountById(id);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((ContraResponse) response).setData(contraVo);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_CONTRA_ACCOUNT_FETCH,
					Constants.SUCCESS_CONTRA_ACCOUNT_FETCH, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_CONTRA_ACCOUNT_FETCH,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// For Updating Contra Accounts

	@RequestMapping(value = "/v1/contras", method = RequestMethod.PUT)
	public ResponseEntity<BaseResponse> updateContraAccount(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @RequestBody JSONObject<ContraRequest> contraRequest) {
		logger.info("Entry into method: updateContraAccount");
		BaseResponse response = new BankMasterCardResponse();
		try {
			logger.info("Request Payload:" + generateRequestAndResponseLogPaylod(contraRequest));
			ContraVo contraVo = BankingConvertToVoHelper.getInstance()
					.convertContraAccountsVoFromContraAccountsRequest(contraRequest.getData());
			contraService.updateContraAccount(contraVo);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_CONTRA_ACCOUNTS_UPDATED,
					Constants.SUCCESS_CONTRA_ACCOUNTS_UPDATED, Constants.CONTRA_ACCOUNTS_UPDATED_SUCCESSFULLY);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			if (e.getCause() != null) {
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_CONTRA_ACCOUNTS_UPDATED,
						e.getCause().getMessage(), Constants.CONTRA_ACCOUNTS_UPDATED_UNSUCCESSFULLY);
			} else {
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_CONTRA_ACCOUNTS_UPDATED,
						e.getMessage(), Constants.CONTRA_ACCOUNTS_UPDATED_UNSUCCESSFULLY);
			}

			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// DropDowns for Contra Accounts
	@RequestMapping(value = "/v1/dropsdowns/contras/{organizationId}")
	public ResponseEntity<BaseResponse> getContraDropDownData(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, @PathVariable int organizationId) {
		logger.info("Entry into getAccountingAspectsDropDownData");
		BaseResponse response = new ContraDropDownResponse();
		try {
			ContraDropDownVo data = contraService.getContraDropDownData(organizationId);
			setTokens(response, httpServletRequest.getHeader("keyToken"), httpServletRequest.getHeader("valueToken"));
			((ContraDropDownResponse) response).setData(data);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_DROP_DOWN_FETCH,
					Constants.SUCCESS_DROP_DOWN_FETCH, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);

		} catch (Exception e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_DROP_DOWN_FETCH, e.getMessage(),
					Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

}

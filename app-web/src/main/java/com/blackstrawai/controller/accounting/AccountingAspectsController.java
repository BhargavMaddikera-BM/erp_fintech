package com.blackstrawai.controller.accounting;

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
import com.blackstrawai.accounting.AccountingAspectsBasicVo;
import com.blackstrawai.accounting.AccountingAspectsFilterVo;
import com.blackstrawai.accounting.AccountingAspectsService;
import com.blackstrawai.accounting.AccountingAspectsVo;
import com.blackstrawai.accounting.dropdowns.AccountingAspectsDropDownVo;
import com.blackstrawai.common.BaseController;
import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.common.CommonConstants;
import com.blackstrawai.common.Constants;
import com.blackstrawai.helper.AccountingConvertToVoHelper;
import com.blackstrawai.request.JSONObject;
import com.blackstrawai.request.accounting.AccountingAspectsFilterRequest;
import com.blackstrawai.request.accounting.AccountingAspectsRequest;
import com.blackstrawai.response.accounting.AccountingAspectsBasicResponse;
import com.blackstrawai.response.accounting.AccountingAspectsDropDownResponse;
import com.blackstrawai.response.accounting.AccountingAspectsResponse;
import com.blackstrawai.response.accounting.LedgerDetailsResponse;
import com.blackstrawai.settings.chartofaccounts.ChartOfAccountsLedgerDetailsVo;

@RestController
@CrossOrigin
@RequestMapping("/decifer/ap/accounting_aspect")
public class AccountingAspectsController extends BaseController {

	@Autowired
	AccountingAspectsService accountingAspectsService;
	

	private Logger logger = Logger.getLogger(AccountingAspectsController.class);

	// For Creating AccountingAspects
	@RequestMapping(value = "/v1/accountingAspects", method = RequestMethod.POST)
	public ResponseEntity<BaseResponse> createAccountingAspects(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse,
			@RequestBody JSONObject<AccountingAspectsRequest> accountingAspectsRequest) {
		logger.info("Entry into method: createAccountingAspects");
		BaseResponse response = new AccountingAspectsResponse();
		try {
			logger.info("Request Payload:" + generateRequestAndResponseLogPaylod(accountingAspectsRequest));
			AccountingAspectsVo accountingAspectsVo = AccountingConvertToVoHelper.getInstance()
					.convertAccountingAspectsVoFromAccountingAspectsRequest(accountingAspectsRequest.getData());
			accountingAspectsService.createAccountingAspects(accountingAspectsVo);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_ACCOUNTING_ASPECTS_CREATED,
					Constants.SUCCESS_ACCOUNTING_ASPECTS_CREATED, Constants.ACCOUNTING_ASPECTS_CREATED_SUCCESSFULLY);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			logger.error("Error Payload:" + e.getMessage());
			if (e.getCause() != null) {
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_ACCOUNTING_ASPECTS_CREATED,
						e.getCause().getMessage(), Constants.ACCOUNTING_ASPECTS_CREATED_UNSUCCESSFULLY);
			} else {
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_ACCOUNTING_ASPECTS_CREATED,
						e.getMessage(), Constants.ACCOUNTING_ASPECTS_CREATED_UNSUCCESSFULLY);
			}

			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	// For Deleting AccountingAspects

	@RequestMapping(value = "/v1/accountingAspects/{organizationId}/{organizationName}/{userId}/{id}/{status}/{roleName}", method = RequestMethod.DELETE)
	public ResponseEntity<BaseResponse> deleteAccountingAspects(
			HttpServletRequest httpRequest,
			HttpServletResponse httpResponse,
			@PathVariable int organizationId, 
			@PathVariable String organizationName,
			@PathVariable String userId,
			@PathVariable int id,
			@PathVariable String status,
			@PathVariable String roleName) {
		logger.info("Entry into method: deleteAccountingAspects");
		BaseResponse response = new AccountingAspectsResponse();
		try {
			accountingAspectsService.deleteAccountingAspects(id, status,userId,roleName);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			if (status.equals(CommonConstants.STATUS_AS_ACTIVE)) {
				response = constructResponse(response, Constants.SUCCESS,
						Constants.SUCCESS_ACCOUNTING_ASPECTS_ACTIVATED, Constants.SUCCESS_ACCOUNTING_ASPECTS_ACTIVATED,
						Constants.ACCOUNTING_ASPECTS_DELETED_SUCCESSFULLY);
				logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			} else if (status.equals(CommonConstants.STATUS_AS_INACTIVE)) {
				response = constructResponse(response, Constants.SUCCESS,
						Constants.SUCCESS_ACCOUNTING_ASPECTS_DEACTIVATED,
						Constants.SUCCESS_ACCOUNTING_ASPECTS_DEACTIVATED,
						Constants.ACCOUNTING_ASPECTS_DELETED_SUCCESSFULLY);
				logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			}
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_ACCOUNTING_ASPECTS_DELETED,
					e.getMessage(), Constants.ACCOUNTING_ASPECTS_DELETED_UNSUCCESSFULLY);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	
	// For Fetching all Accounting Aspects details
		@RequestMapping(value = "/v1/accountingAspects/{organizationId}/{organizationName}/{userId}/{roleName}", method = RequestMethod.GET)
		public ResponseEntity<BaseResponse> getAllAccountingAspectsOfAnOrganizationForUserAndRole(
				HttpServletRequest httpRequest,
				HttpServletResponse httpResponse, 
				@PathVariable int organizationId,
				@PathVariable String organizationName,
				@PathVariable String userId,
				@PathVariable String roleName) {
			logger.info("Entry into method: getAllAccountingAspectsOfAnOrganizationForUserAndRole");
			BaseResponse response = new AccountingAspectsBasicResponse();
			try {
				List<AccountingAspectsBasicVo> listAllAccountingAspects = accountingAspectsService
						.getAllAccountingAspectsOfAnOrganizationForUserAndRole(organizationId,userId,roleName);
				setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
				((AccountingAspectsBasicResponse) response).setData(listAllAccountingAspects);
				response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_ACCOUNTING_ASPECTS_FETCH,
						Constants.SUCCESS_ACCOUNTING_ASPECTS_FETCH, Constants.SUCCESS_DURING_GET);
				logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
				return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
			} catch (ApplicationException e) {
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_ACCOUNTING_ASPECTS_FETCH,
						e.getMessage(), Constants.FAILURE_DURING_GET);
				logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
				logger.info(response);
				return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
	
	
	// For Fetching Accounting Aspects detail by id
	@RequestMapping(value = "/v1/accountingAspects/{organizationId}/{organizationName}/{userId}/{roleName}/{id}", method = RequestMethod.GET)
	public ResponseEntity<BaseResponse> getAccountingAspectsById(
			HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, 
			@PathVariable int organizationId,
			@PathVariable String organizationName,			
			@PathVariable int userId,
			@PathVariable String roleName,
			@PathVariable int id) {
		logger.info("Entry into method: getAccountingAspectsById");
		BaseResponse response = new AccountingAspectsResponse();
		try {
			AccountingAspectsVo accountingAspectsVo = accountingAspectsService.getAccountingAspectsById(id);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((AccountingAspectsResponse) response).setData(accountingAspectsVo);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_ACCOUNTING_ASPECTS_FETCH,
					Constants.SUCCESS_ACCOUNTING_ASPECTS_FETCH, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_ACCOUNTING_ASPECTS_FETCH,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	

	// For Updating Accounting Aspects
	@RequestMapping(value = "/v1/accountingAspects", method = RequestMethod.PUT)
	public ResponseEntity<BaseResponse> updateAccountingAspects(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse,
			@RequestBody JSONObject<AccountingAspectsRequest> accountingAspectsRequest) {
		logger.info("Entry into method: updateAccountingAspects");
		BaseResponse response = new AccountingAspectsResponse();
		try {
			logger.info("Request Payload:" + generateRequestAndResponseLogPaylod(accountingAspectsRequest));
			AccountingAspectsVo accountingAspectsVo = AccountingConvertToVoHelper.getInstance()
					.convertAccountingAspectsVoFromAccountingAspectsRequest(accountingAspectsRequest.getData());
			accountingAspectsService.updateAccountingAspects(accountingAspectsVo);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_ACCOUNTING_ASPECTS_UPDATED,
					Constants.SUCCESS_ACCOUNTING_ASPECTS_UPDATED, Constants.ACCOUNTING_ASPECTS_UPDATED_SUCCESSFULLY);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			if (e.getCause() != null) {
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_ACCOUNTING_ASPECTS_UPDATED,
						e.getCause().getMessage(), Constants.ACCOUNTING_ASPECTS_UPDATED_UNSUCCESSFULLY);
			} else {
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_ACCOUNTING_ASPECTS_UPDATED,
						e.getMessage(), Constants.ACCOUNTING_ASPECTS_UPDATED_UNSUCCESSFULLY);
			}

			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/v1/dropsdowns/accountingAspects/{organizationId}")
	public ResponseEntity<BaseResponse> getAccountingAspectsDropDownData(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, @PathVariable int organizationId) {
		logger.info("Entry into getAccountingAspectsDropDownData");
		BaseResponse response = new AccountingAspectsDropDownResponse();
		try {
			AccountingAspectsDropDownVo data = accountingAspectsService
					.getAccountingAspectsDropDownData(organizationId);
			setTokens(response, httpServletRequest.getHeader("keyToken"), httpServletRequest.getHeader("valueToken"));
			((AccountingAspectsDropDownResponse) response).setData(data);
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

	// Accounting Aspects Filter

	@RequestMapping(value = "/v1/accountingAspects/filter")
	public ResponseEntity<BaseResponse> getAccountingAspectsFilterData(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse,
			@RequestBody JSONObject<AccountingAspectsFilterRequest> accountingAspectsFilterRequest) {
		logger.info("Entry into getAccountingAspectsFilterData");
		BaseResponse response = new AccountingAspectsBasicResponse();
		try {
			AccountingAspectsFilterVo accountingAspectsFilterVo = AccountingConvertToVoHelper.getInstance()
					.convertAccountingAspectsFilterVoFromAccountingAspectsFilterRequest(
							accountingAspectsFilterRequest.getData());
			List<AccountingAspectsBasicVo> listAllAccountingAspects = accountingAspectsService
					.getAccountingAspectsFilterData(accountingAspectsFilterVo);
			setTokens(response, httpServletRequest.getHeader("keyToken"), httpServletRequest.getHeader("valueToken"));
			((AccountingAspectsBasicResponse) response).setData(listAllAccountingAspects);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_ACCOUNTING_ASPECTS_FETCH,
					Constants.SUCCESS_ACCOUNTING_ASPECTS_FETCH, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_ACCOUNTING_ASPECTS_FETCH,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	
	@RequestMapping(value = "/v1/accountingAspects/getLedgerDetails/{organizationId}/{ledgerId}")
	public ResponseEntity<BaseResponse> getLedgerDetails(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse,
			@PathVariable Integer organizationId,@PathVariable Integer ledgerId) {
		logger.info("Entry into getAccountingAspectsFilterData");
		BaseResponse response = new LedgerDetailsResponse();
		try {
			ChartOfAccountsLedgerDetailsVo chartOfAccountsLedgerDetailsVo = accountingAspectsService
					.getLedgerDetailsByLedgerId(ledgerId, organizationId);
			setTokens(response, httpServletRequest.getHeader("keyToken"), httpServletRequest.getHeader("valueToken"));
			((LedgerDetailsResponse) response).setData(chartOfAccountsLedgerDetailsVo);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_LEDGER_FETCH,
					Constants.SUCCESS_LEDGER_FETCH, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_LEDGER_FETCH,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	/*// For Fetching all Accounting Aspects details
	@RequestMapping(value = "/v1/accountingAspects/{organizationId}/{userId}", method = RequestMethod.GET)
	public ResponseEntity<BaseResponse> getAllAccountingAspectsOfAnOrganization(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @PathVariable int organizationId, @PathVariable int userId) {
		logger.info("Entry into method: getAllAccountingAspectsOfAnOrganization");
		BaseResponse response = new AccountingAspectsBasicResponse();
		try {
			List<AccountingAspectsBasicVo> listAllAccountingAspects = accountingAspectsService
					.getAllAccountingAspectsOfAnOrganization(organizationId);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((AccountingAspectsBasicResponse) response).setData(listAllAccountingAspects);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_ACCOUNTING_ASPECTS_FETCH,
					Constants.SUCCESS_ACCOUNTING_ASPECTS_FETCH, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_ACCOUNTING_ASPECTS_FETCH,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}*/


}

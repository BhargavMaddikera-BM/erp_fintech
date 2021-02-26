package com.blackstrawai.controller.externalintegration.banking.common;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.common.BaseController;
import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.common.Constants;
import com.blackstrawai.externalintegration.banking.common.BankStatementService;
import com.blackstrawai.externalintegration.banking.common.BasicAccountDetailsVo;
import com.blackstrawai.externalintegration.banking.perfios.PerfiosService;
import com.blackstrawai.externalintegration.banking.perfios.RecentTransactionVo;
import com.blackstrawai.externalintegration.banking.yesbank.YesBankPaymentsService;
import com.blackstrawai.externalintegration.yesbank.Response.common.AccountVo;
import com.blackstrawai.externalintegration.yesbank.Response.common.RequestData;
import com.blackstrawai.externalintegration.yesbank.Response.fundconfirmation.FundsConfirmationResponseVo;
import com.blackstrawai.helper.YesBankIntegrationHelperService;
import com.blackstrawai.response.externalintegration.banking.perfios.PerfiosListAccountResponse;
import com.blackstrawai.response.externalintegration.banking.perfios.RecentTrasactionResponse;
import com.blackstrawai.response.externalintegration.banking.yesbank.AccountOverviewResponse;

@Controller
@CrossOrigin
@RequestMapping("/decifer/current_account")
public class CurrentAccountController extends BaseController {
	@Autowired
	private PerfiosService perfiosService;
	private Logger logger = Logger.getLogger(CurrentAccountController.class);
	@Autowired
	private YesBankPaymentsService yesBankPaymentsService;
	@Autowired
	private YesBankIntegrationHelperService yesBankIntegrationHelperService;
	@Autowired
	private BankStatementService bankStatementService;
	  
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
	
	@RequestMapping(value = "/v1/recent_transactions/{orgId}/{userId}/{roleName}/{accountName}/{type}/{key1}", method = RequestMethod.GET)
	public ResponseEntity<BaseResponse> getAccountRecentTransactions(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse,
			@PathVariable int orgId,
			  @PathVariable String userId, 
			  @PathVariable String roleName,
		      @PathVariable String accountName, @PathVariable String type, @PathVariable String key1) {
		logger.info("Entry into method: getAccountRecentTransactions");
		BaseResponse response = new RecentTrasactionResponse();
		try {
			List<RecentTransactionVo> perfiosVo=null;
			  if ( type.equalsIgnoreCase("Perfios")) {
				  perfiosVo= perfiosService.getAccountRecentTransactions(accountName,orgId,userId,roleName);
			  }else if (type.equalsIgnoreCase("Yes Bank")) {
				  perfiosVo =bankStatementService.getRecentTransactions(orgId, userId, key1, accountName,roleName);
			  }
			  
			
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((RecentTrasactionResponse) response).setData(perfiosVo);
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
	
	  @SuppressWarnings("rawtypes")
	@RequestMapping(value = "/v1/account_overview/{orgId}/{userId}/{roleName}/{custId}/{accNumber}/{type}", method = RequestMethod.GET)
	  public ResponseEntity<BaseResponse> getCurrentAccountOverview(
		      HttpServletRequest httpRequest,HttpServletResponse httpResponse,
			  @PathVariable int orgId,
			  @PathVariable String userId, 
			  @PathVariable String roleName,
		      @PathVariable String custId,
		      @PathVariable String accNumber,
		      @PathVariable String type) {
		  BaseResponse response=new AccountOverviewResponse();
		  try {
			  AccountVo vo=new AccountVo();	
			  if (type != null ) {
				  if(type.equalsIgnoreCase("Yes Bank")) {
					try {
					  RequestData requestData = yesBankIntegrationHelperService.getAvailableBalanceRequestObject(custId, accNumber);
					  FundsConfirmationResponseVo fundsConfirmationResponseVo = yesBankPaymentsService.fundsConfirmation(new Integer(orgId).toString(), custId,
							  accNumber, requestData,userId,roleName);
					  vo.setAvailableBalance(fundsConfirmationResponseVo.getData().getFundsAvailableVo().getBalanceAmount());
					  vo.setUserId(custId);
					}catch(Exception e) {
						vo.setAvailableBalance("0");
						  vo.setUserId(custId);
					}
				  }
			  if ( type.equalsIgnoreCase("Perfios")) {
				  vo.setAvailableBalance(perfiosService.getAvailableBalanceForAccount(orgId,userId,roleName,accNumber));
			  }
		  }
		  setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
		  ((AccountOverviewResponse) response).setData(vo);
		  response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_ACCOUNT_OVERVIEW,
				  Constants.SUCCESS_ACCOUNT_OVERVIEW, Constants.SUCCESS_DURING_GET);
		  logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
		  return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
	  }catch( Exception e)
	  {
		  logger.error("Error in current account overview:",e);
		  String errorMessage = e.getCause() != null ? e.getCause().getMessage() : e.getMessage();
		  response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_ACCOUNT_OVERVIEW, errorMessage,
				  Constants.FAILURE_ACCOUNT_OVERVIEW);
		  logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
		  logger.info(response);
		  return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	  }

	  }
	
}

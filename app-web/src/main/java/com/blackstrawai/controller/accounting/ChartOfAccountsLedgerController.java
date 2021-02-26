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

import com.blackstrawai.accounting.ChartOfAccountsLedgerService;
import com.blackstrawai.accounting.dropdowns.LedgerDropDownVo;
import com.blackstrawai.accounting.dropdowns.LedgerFilterDropDownVo;
import com.blackstrawai.accounting.ledger.LedgerListVo;
import com.blackstrawai.accounting.ledger.LedgerVo;
import com.blackstrawai.common.BaseController;
import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.common.Constants;
import com.blackstrawai.helper.AccountingConvertToVoHelper;
import com.blackstrawai.request.JSONObject;
import com.blackstrawai.request.accounting.ledger.LedgerRequest;
import com.blackstrawai.response.accounting.ledger.LedgerDropDownResponse;
import com.blackstrawai.response.accounting.ledger.LedgerFilterDropDownResponse;
import com.blackstrawai.response.accounting.ledger.LedgerListResponse;
import com.blackstrawai.response.accounting.ledger.LedgerResponse;
import com.blackstrawai.response.accounting.ledger.SubLedgerTypeResponse;
import com.blackstrawai.settings.chartofaccounts.MinimalChartOfAccountsLevel6Vo;


@RestController
@CrossOrigin
@RequestMapping("/decifer/accounting/ledger")
public class ChartOfAccountsLedgerController extends BaseController{

	private Logger logger = Logger.getLogger(ChartOfAccountsLedgerController.class);
	
	
	@Autowired
	private ChartOfAccountsLedgerService ledgerService;
	
	@RequestMapping(value="/v1/ledger" , method = RequestMethod.POST)
	public ResponseEntity<BaseResponse>createLedger(HttpServletRequest httpRequest, HttpServletResponse httpResponse,@RequestBody JSONObject<LedgerRequest> ledgerRequest){
		logger.info("Entered Into createLedger");
		BaseResponse response = new LedgerResponse();
		if(ledgerRequest.getData().getIsSuperAdmin()!=null && ledgerRequest.getData().getUserId()!=null && ledgerRequest.getData().getOrganizationId()!=null) {
		try {
			logger.info("Request Payload:"+generateRequestAndResponseLogPaylod(ledgerRequest));	
			LedgerVo ledgerVo = AccountingConvertToVoHelper.getInstance().convertLedgerVoFromRequest(ledgerRequest.getData());
			ledgerService.createLedgerAndSubLedger(ledgerVo);
			setTokens(response,httpRequest.getHeader("keyToken"),httpRequest.getHeader("valueToken"));
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_LEDGER_CREATED,Constants.SUCCESS_LEDGER_CREATED,Constants.LEDGER_CREATED_SUCCESSFULLY);
			logger.info("Response Payload:"+generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		}catch(Exception e) {
			if(e.getCause()!=null){
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_LEDGER_CREATED,e.getCause().getMessage(), Constants.LEDGER_CREATED_UNSUCCESSFULLY);
			}else{
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_LEDGER_CREATED,e.getMessage(), Constants.LEDGER_CREATED_UNSUCCESSFULLY);
			}
			logger.error("Error Payload:" , e);
			logger.error("Error Payload:"+generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}	
		}else {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_LEDGER_CREATED,Constants.FAILURE_IN_USER_ORGANIZATION_REQUEST_VALIDATION, Constants.LEDGER_CREATED_UNSUCCESSFULLY);
			logger.error("Error Payload:"+ generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value="/v1/ledger" , method = RequestMethod.PUT)
	public ResponseEntity<BaseResponse>updateLedger(HttpServletRequest httpRequest, HttpServletResponse httpResponse,@RequestBody JSONObject<LedgerRequest> ledgerRequest){
		logger.info("Entered Into update Ledger");
		BaseResponse response = new LedgerResponse();
		if(ledgerRequest.getData().getId()!=null && ledgerRequest.getData().getIsSuperAdmin()!=null && ledgerRequest.getData().getUserId()!=null && ledgerRequest.getData().getOrganizationId()!=null) {
		try {
			logger.info("Request Payload:"+generateRequestAndResponseLogPaylod(ledgerRequest));	
			LedgerVo ledgerVo= AccountingConvertToVoHelper.getInstance().convertLedgerVoFromRequest(ledgerRequest.getData());
			ledgerService.updateLedger(ledgerVo);
			setTokens(response,httpRequest.getHeader("keyToken"),httpRequest.getHeader("valueToken"));
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_LEDGER_UPDATED,Constants.SUCCESS_LEDGER_UPDATED,Constants.LEDGER_UPDATED_SUCCESSFULLY);
			logger.info("Response Payload:"+generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		}catch(Exception e) {
			if(e.getCause()!=null){
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_LEDGER_UPDATED,e.getCause().getMessage(), Constants.LEDGER_UPDATED_UNSUCCESSFULLY);
			}else{
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_LEDGER_UPDATED,e.getMessage(), Constants.LEDGER_UPDATED_UNSUCCESSFULLY);
			}
			logger.error("Error Payload:"+generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}	
		}else {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_LEDGER_UPDATED,Constants.FAILURE_IN_USER_ORGANIZATION_REQUEST_VALIDATION, Constants.LEDGER_UPDATED_UNSUCCESSFULLY);
			logger.error("Error Payload:"+ generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/v1/ledger/{organizationId}/{organizationName}/{id}")
	public ResponseEntity<BaseResponse> getLedger(
			HttpServletRequest httpRequest, 
			HttpServletResponse httpResponse,
			@PathVariable Integer organizationId,
			@PathVariable String organizationName,
			@PathVariable Integer id) {
		BaseResponse response = new LedgerResponse();
		try {
			LedgerVo ledgerVo = ledgerService.getLedgerById(id, organizationId);
			setTokens(response,httpRequest.getHeader("keyToken"),httpRequest.getHeader("valueToken"));
			((LedgerResponse) response).setData(ledgerVo);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_LEDGER_FETCH,
					Constants.SUCCESS_LEDGER_FETCH, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (Exception e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_LEDGER_FETCH, e.getMessage(),
					Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	
	@RequestMapping(value = "/v1/ledgers/{organizationId}/{organizationName}/{userId}/{roleName}/{filterValue}")
	public ResponseEntity<BaseResponse> getAllLedger(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse,
			@PathVariable Integer organizationId,
			@PathVariable String organizationName,
			@PathVariable String userId,
			@PathVariable String  roleName,
			@PathVariable String  filterValue) {
		logger.info("Entry into getAllLedger");
		BaseResponse response = new LedgerListResponse();
		try {
			if( organizationId!= null) {
				List<LedgerListVo> listVos = ledgerService.getLedgersByOrdgId(organizationId,filterValue,userId,roleName);
				logger.info("listSize::"+listVos.size());
				setTokens(response,httpServletRequest.getHeader("keyToken"),httpServletRequest.getHeader("valueToken"));
				((LedgerListResponse) response).setData(listVos);
				response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_LEDGER_FETCH,
						Constants.SUCCESS_LEDGER_FETCH, Constants.SUCCESS_DURING_GET);
				return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
			}else {
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_LEDGER_FETCH,Constants.FAILURE_IN_USER_ORGANIZATION_REQUEST_VALIDATION, Constants.FAILURE_DURING_GET);
				logger.error("Error Payload:"+generateRequestAndResponseLogPaylod(response));
				return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch (Exception e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_LEDGER_FETCH, e.getMessage(),
					Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	
	@RequestMapping(value = "/v1/dropsdowns/ledger/filter/{organizationId}")
	public ResponseEntity<BaseResponse> getLedgerFilterDropDown(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, @PathVariable int organizationId) {
		logger.info("Entry into getLedgerFilterDropDown");
		BaseResponse response = new LedgerFilterDropDownResponse();
		try {
			List<LedgerFilterDropDownVo> data = ledgerService.getFilterDropDown(organizationId);
			setTokens(response, httpServletRequest.getHeader("keyToken"), httpServletRequest.getHeader("valueToken"));
			((LedgerFilterDropDownResponse) response).setData(data);
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
	
	@RequestMapping(value = "/v1/dropsdowns/ledger/{organizationId}")
	public ResponseEntity<BaseResponse> getLedgerDropDown(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, @PathVariable int organizationId) {
		logger.info("Entry into getLedgerDropDown");
		BaseResponse response = new LedgerDropDownResponse();
		try {
			LedgerDropDownVo data = ledgerService.getLedgerDropDownData(organizationId);
			setTokens(response, httpServletRequest.getHeader("keyToken"), httpServletRequest.getHeader("valueToken"));
			((LedgerDropDownResponse) response).setData(data);
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
	
	
	@RequestMapping(value = "/v1/ledgers/{organizationId}/{organizationName}/{userId}/{roleName}/{type}/{id}")
	public ResponseEntity<BaseResponse> getLedgerByType(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse,
			@PathVariable Integer organizationId,
			@PathVariable String organizationName,
			@PathVariable String userId,
			@PathVariable String  roleName,
			@PathVariable String  type,
			@PathVariable int  id) {
		logger.info("Entry into getLedgerByType");
		BaseResponse response = new SubLedgerTypeResponse();
		try {
			if( organizationId!= null) {
				List<MinimalChartOfAccountsLevel6Vo> level6Data = ledgerService.getLedgerByType(organizationId,type,id);
				logger.info("listSize::"+level6Data.size());
				setTokens(response,httpServletRequest.getHeader("keyToken"),httpServletRequest.getHeader("valueToken"));
				((SubLedgerTypeResponse) response).setData(level6Data);
				response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_LEDGER_FETCH,
						Constants.SUCCESS_LEDGER_FETCH, Constants.SUCCESS_DURING_GET);
				return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
			}else {
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_LEDGER_FETCH,Constants.FAILURE_IN_USER_ORGANIZATION_REQUEST_VALIDATION, Constants.FAILURE_DURING_GET);
				logger.error("Error Payload:"+generateRequestAndResponseLogPaylod(response));
				return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch (Exception e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_LEDGER_FETCH, e.getMessage(),
					Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
}

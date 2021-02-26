package com.blackstrawai.controller.payroll;

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

import com.blackstrawai.ap.dropdowns.BasicVoucherEntriesVo;
import com.blackstrawai.common.BaseController;
import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.common.CommonConstants;
import com.blackstrawai.common.Constants;
import com.blackstrawai.helper.PayrollConvertToVoHelper;
import com.blackstrawai.payroll.PayRunService;
import com.blackstrawai.payroll.dropdowns.PayRunEmployeeDropDownVo;
import com.blackstrawai.payroll.dropdowns.PayRunPaymentCycleDropDownVo;
import com.blackstrawai.payroll.dropdowns.PayRunPeriodDropDownVo;
import com.blackstrawai.payroll.dropdowns.PayRunTableDropDownVo;
import com.blackstrawai.payroll.payrun.PayRunTableVo;
import com.blackstrawai.payroll.payrun.PayRunVo;
import com.blackstrawai.request.JSONObject;
import com.blackstrawai.request.payroll.PayRun.PayRunRequest;
import com.blackstrawai.request.payroll.PayRun.PayRunTableRequest;
import com.blackstrawai.response.payroll.payrun.PayRunDropDownResponse;
import com.blackstrawai.response.payroll.payrun.PayRunListResponse;
import com.blackstrawai.response.payroll.payrun.PayRunResponse;
import com.blackstrawai.response.payroll.payrun.PayRunTableDropDownResponse;
import com.blackstrawai.response.payroll.payrun.PayRunTableResponse;

@RestController
@CrossOrigin
@RequestMapping("/decifer/payrun")
public class PayRunController extends BaseController {
	private Logger logger = Logger.getLogger(PayRunController.class);
	
	@Autowired
	private PayRunService  payRunService;
	
	

	@RequestMapping(value="/v1/payruns" , method = RequestMethod.POST)
	public ResponseEntity<BaseResponse>createPayRunData(HttpServletRequest httpRequest, HttpServletResponse httpResponse,@RequestBody JSONObject<PayRunRequest> payRunRequest){
		logger.info("Entered Into createPayRun " + payRunRequest.toString());
		BaseResponse response = new PayRunResponse();
		logger.info("Org Id : "+payRunRequest.getData().getOrgId()+ " and user id : "+payRunRequest.getData().getUserId());
			try {
				logger.info("Request Payload:"+generateRequestAndResponseLogPaylod(payRunRequest));	
				PayRunVo payRunVo = PayrollConvertToVoHelper.getInstance().convertPayRunVoFromRequest(payRunRequest.getData());
				logger.info("Controller trace "+payRunVo);
				payRunService.createPayRun(payRunVo);
				setTokens(response,httpRequest.getHeader("keyToken"),httpRequest.getHeader("valueToken"));
				((PayRunResponse)response).setData(payRunVo);
				response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_PAY_RUN_CREATED,Constants.SUCCESS_PAY_RUN_CREATED,Constants.PAY_RUN_CREATED_SUCCESSFULLY);
				logger.info("Response Payload:"+generateRequestAndResponseLogPaylod(response));
				return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
			}catch(Exception e) {
				if(e.getCause()!=null){
					response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_PAY_RUN_CREATED,e.getCause().getMessage(), Constants.PAY_RUN_CREATED_UNSUCCESSFULLY);
				}else{
					response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_PAY_RUN_CREATED,e.getMessage(), Constants.PAY_RUN_CREATED_UNSUCCESSFULLY);
				}
				logger.error("Error Payload:" , e);
				logger.error("Error Payload:"+generateRequestAndResponseLogPaylod(response));
				return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		
	}
	
	@RequestMapping(value = "/v1/payruns/{organizationId}/{organizationName}/{userId}/{roleName}/{id}")
	public ResponseEntity<BaseResponse> getPayRunData(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, 
			@PathVariable int organizationId, 
			@PathVariable String organizationName,
			@PathVariable String userId,
			@PathVariable String roleName,
			@PathVariable int id)
	{
		logger.info("Entry into getPayRunData"); 
		BaseResponse response = new PayRunResponse(); 
		try { 
			PayRunVo payRunVo = payRunService.getPayRunDataById(organizationId,userId,roleName,id);
			setTokens(response,httpRequest.getHeader("keyToken"),httpRequest.getHeader("valueToken")); 
			((PayRunResponse)response).setData(payRunVo);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_PAY_RUN_FETCH,Constants.SUCCESS_PAY_RUN_FETCH,Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:"+generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (Exception e)
		{ 
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_PAY_RUN_FETCH,e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:"+ generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		} 
	}
	
	
	@RequestMapping(value = "/v1/payruns/{organizationId}/{organizationName}/{userId}/{roleName}")
	public ResponseEntity<BaseResponse> getPayRunDataAll(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, 
			@PathVariable int organizationId, 
			@PathVariable String organizationName,
			@PathVariable String userId,
			@PathVariable String roleName)
	{
		logger.info("Entry into getPayRunData"); 
		BaseResponse response = new PayRunListResponse(); 
		try { 
			List<PayRunVo> payRunVo = payRunService.getPayRunAll(organizationId,userId,roleName);
			setTokens(response,httpRequest.getHeader("keyToken"),httpRequest.getHeader("valueToken")); 
			((PayRunListResponse)response).setData(payRunVo);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_PAY_RUN_FETCH,Constants.SUCCESS_PAY_RUN_FETCH,Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:"+generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (Exception e)
		{ 
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_PAY_RUN_FETCH,e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:"+ generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		} 
	}
	
	@RequestMapping(value = "/v1/payruns/copy/{organizationId}/{organizationName}/{userId}/{roleName}" , method = RequestMethod.GET)
	public ResponseEntity<BaseResponse> getActivePayRunDataAll(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, 
			@PathVariable int organizationId, 
			@PathVariable String organizationName,
			@PathVariable String userId,
			@PathVariable String roleName)
	{
		logger.info("Entry into getPayRunData"); 
		BaseResponse response = new PayRunListResponse(); 
		try { 
			List<PayRunVo> payRunVo = payRunService.getActivePayRunAll(organizationId,userId,roleName);
			setTokens(response,httpRequest.getHeader("keyToken"),httpRequest.getHeader("valueToken")); 
			((PayRunListResponse)response).setData(payRunVo);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_PAY_RUN_FETCH,Constants.SUCCESS_PAY_RUN_FETCH,Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:"+generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (Exception e)
		{ 
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_PAY_RUN_FETCH,e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:"+ generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		} 
	}
	
	@RequestMapping(value="/v1/payruns" , method = RequestMethod.PUT)
	public ResponseEntity<BaseResponse>updatePayRunData(HttpServletRequest httpRequest, HttpServletResponse httpResponse,@RequestBody JSONObject<PayRunRequest> payRunRequest){
		logger.info("Entered Into updatePayRunData");
		BaseResponse response = new PayRunResponse();
		logger.info("Org Id : "+payRunRequest.getData().getOrgId()+ " and user id : "+payRunRequest.getData().getUserId());
			try {
				logger.info("Request Payload:"+generateRequestAndResponseLogPaylod(payRunRequest));	
				PayRunVo payRunVo = PayrollConvertToVoHelper.getInstance().convertPayRunVoFromRequest(payRunRequest.getData());
				logger.info("Controller trace "+payRunVo);
				payRunService.updatePayRun(payRunVo);
				setTokens(response,httpRequest.getHeader("keyToken"),httpRequest.getHeader("valueToken"));
				((PayRunResponse)response).setData(payRunVo);
				response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_PAY_RUN_UPDATED,Constants.SUCCESS_PAY_RUN_UPDATED,Constants.PAY_RUN_UPDATED_SUCCESSFULLY);
				logger.info("Response Payload:"+generateRequestAndResponseLogPaylod(response));
				return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
			}catch(Exception e) {
				if(e.getCause()!=null){
					response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_PAY_RUN_UPDATED,e.getCause().getMessage(), Constants.PAY_RUN_UPDATED_UNSUCCESSFULLY);
				}else{
					response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_PAY_RUN_UPDATED,e.getMessage(), Constants.PAY_RUN_UPDATED_UNSUCCESSFULLY);
				}
				logger.error("Error Payload:" , e);
				logger.error("Error Payload:"+generateRequestAndResponseLogPaylod(response));
				return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}	
		
	}
	
	@RequestMapping(value = "/v1/payruns/dropdowns/{organizationId}")
	public ResponseEntity<BaseResponse> getPayRunDropDown(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, @PathVariable int organizationId) {
		logger.info("Entry into getPayRunDropDown");
		BaseResponse response = new PayRunDropDownResponse();
		try {
			PayRunPeriodDropDownVo periodData = payRunService.getReportsPeriodDropDownData(organizationId);
			PayRunPaymentCycleDropDownVo paymentCycledata = payRunService.getPaymentCycleDropDownData(organizationId);
			PayRunEmployeeDropDownVo payRunEmployeeData = payRunService.getPayRunEmployeeDropDownData(organizationId);
			BasicVoucherEntriesVo payRunReferenceNumber = payRunService.getPayRunReferenceNumberSpec(organizationId);
			setTokens(response, httpServletRequest.getHeader("keyToken"), httpServletRequest.getHeader("valueToken"));
			((PayRunDropDownResponse) response).setPaymentCycleDropDowdata(paymentCycledata);
			((PayRunDropDownResponse) response).setPeriodDropDowndata(periodData);
			((PayRunDropDownResponse) response).setPayRunEmployeeDropDownVo(payRunEmployeeData);
			((PayRunDropDownResponse) response).setPayRunReferenceNumber(payRunReferenceNumber);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_PAY_RUN_FETCH,
					Constants.SUCCESS_PAY_RUN_FETCH, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);

		} catch (Exception e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_PAY_RUN_FETCH, e.getMessage(),
					Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}	
	
	@RequestMapping(value = "/v1/payruns/{organizationId}/{organizationName}/{userId}/{roleName}/{status}/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<BaseResponse> activateDeActivatePayRun(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @PathVariable int organizationId, 
			@PathVariable String organizationName,
			@PathVariable String userId,
			@PathVariable String roleName,
			@PathVariable int id,
			@PathVariable String status)
	{
		logger.info("Entry into activateDeActivatePayRun - Org id "+organizationId+" org name "+organizationName
				+" user id "+userId+" rolename "+roleName+" status "+status+" payrun id "+id); 
		BaseResponse response = new PayRunResponse(); 
		try { 
			PayRunVo payRunVo = payRunService.activateDeActivatePayRun(organizationId,organizationName,userId,id,roleName,status);
			setTokens(response,httpRequest.getHeader("keyToken"),httpRequest.getHeader("valueToken")); 
			((PayRunResponse)response).setData(payRunVo);
			
			if(status.equals(CommonConstants.STATUS_AS_ACTIVE)) {
				response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_PAY_RUN_ACTIVATED,
						Constants.SUCCESS_PAY_RUN_ACTIVATED, Constants.PAY_RUN_DELETED_SUCCESSFULLY);
				logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			}else if(status.equals(CommonConstants.STATUS_AS_INACTIVE)) {
				response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_PAY_RUN_DEACTIVATED,
						Constants.SUCCESS_PAY_RUN_DEACTIVATED, Constants.PAY_RUN_DELETED_SUCCESSFULLY);
				logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			}else if(status.equals(CommonConstants.STATUS_AS_VOID)) {
				response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_PAY_RUN_VOIDED,
						Constants.SUCCESS_PAY_RUN_VOIDED, Constants.SUCCESS_PAY_RUN_DELETED);
				logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			}		
			
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new  ResponseEntity<BaseResponse>(response, HttpStatus.OK); 
		} catch (Exception e)
		{ 
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_PAY_RUN_DELETED,
					e.getMessage(), Constants.PAY_RUN_DELETED_UNSUCCESSFULLY);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		} 
	}
	
	@RequestMapping(value = "/v1/payruns/settings/dropdowns/{organizationId}")
	public ResponseEntity<BaseResponse> getPayRunTableDropDown(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, @PathVariable int organizationId) {
		logger.info("Entry into getPayRunTableDropDown");
		BaseResponse response = new PayRunTableDropDownResponse();
		try {
			PayRunTableDropDownVo payrunTableData = payRunService.getPayRunTableDropDownData(organizationId);
			
			setTokens(response, httpServletRequest.getHeader("keyToken"), httpServletRequest.getHeader("valueToken"));
			((PayRunTableDropDownResponse) response).setPayRunTableDropDownData(payrunTableData);

			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_PAY_RUN_FETCH,
					Constants.SUCCESS_PAY_RUN_FETCH, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);

		} catch (Exception e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_PAY_RUN_FETCH, e.getMessage(),
					Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}	
	
	@RequestMapping(value="/v1/payruns/settings" , method = RequestMethod.PUT)
	public ResponseEntity<BaseResponse>updatePayRunTableSettings(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse,@RequestBody JSONObject<PayRunTableRequest> payRunTableRequest){
		logger.info("Entered Into updatePayRunTableSettings " + payRunTableRequest);
		BaseResponse response = new PayRunTableResponse();
		
			try {
				logger.info("Request Payload:"+generateRequestAndResponseLogPaylod(payRunTableRequest));	
				List<PayRunTableVo> payRunTableVos = PayrollConvertToVoHelper.getInstance().convertPayRunTableVoFromRequest(payRunTableRequest.getData());
				logger.info("Controller trace "+payRunTableVos);
				payRunService.updatePayRunTableSettings(payRunTableVos);
				setTokens(response,httpRequest.getHeader("keyToken"),httpRequest.getHeader("valueToken"));
				((PayRunTableResponse)response).setData(payRunTableVos);
				response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_PAY_RUN_SETTINGS_UPDATED,Constants.SUCCESS_PAY_RUN_SETTINGS_UPDATED,Constants.PAY_RUN_SETTINGS_UPDATED_SUCCESSFULLY);
				logger.info("Response Payload:"+generateRequestAndResponseLogPaylod(response));
				return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
			}catch(Exception e) {
				if(e.getCause()!=null){
					response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_PAY_RUN_SETTINGS_UPDATED,e.getCause().getMessage(), Constants.PAY_RUN_SETTINGS_UPDATED_UNSUCCESSFULLY);
				}else{
					response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_PAY_RUN_SETTINGS_UPDATED,e.getMessage(), Constants.PAY_RUN_SETTINGS_UPDATED_UNSUCCESSFULLY);
				}
				logger.error("Error Payload:" , e);
				logger.error("Error Payload:"+generateRequestAndResponseLogPaylod(response));
				return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
			
		
	}
	

	
}
	
	   
	



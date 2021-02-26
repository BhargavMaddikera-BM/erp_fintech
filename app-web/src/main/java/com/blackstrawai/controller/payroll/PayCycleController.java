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

import com.blackstrawai.ApplicationException;
import com.blackstrawai.common.BaseController;
import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.common.CommonConstants;
import com.blackstrawai.common.Constants;
import com.blackstrawai.helper.PayrollConvertToVoHelper;
import com.blackstrawai.payroll.PayCycleService;
import com.blackstrawai.payroll.PayCycleVo;
import com.blackstrawai.request.JSONObject;
import com.blackstrawai.request.payroll.PayCycleRequest;
import com.blackstrawai.response.payroll.ListPayCycleResponse;
import com.blackstrawai.response.payroll.PayCycleResponse;

@RestController
@CrossOrigin
@RequestMapping("/decifer/sp/pay_cycle")
public class PayCycleController extends BaseController{


	@Autowired
	PayCycleService payCycleService;

	private Logger logger = Logger.getLogger(PayCycleController.class);

	// Create new pay cycle
	@RequestMapping(value = "/v1/pay_cycles", method = RequestMethod.POST)
	public ResponseEntity<BaseResponse> createPayCycle(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @RequestBody JSONObject<PayCycleRequest> payCycleRequest) {

		logger.info("Entry into method: createPayCycle");
		BaseResponse response = new PayCycleResponse();
		try {
			logger.info("Request Payload:" + generateRequestAndResponseLogPaylod(payCycleRequest));
			PayCycleVo payCycleVo = PayrollConvertToVoHelper.getInstance()
					.convertPayCycleVoFromPayCycleRequest(payCycleRequest.getData());
			payCycleVo = payCycleService.createPayCycle(payCycleVo);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			payCycleVo.setKeyToken(null);
			payCycleVo.setValueToken(null);
			((PayCycleResponse) response).setData(payCycleVo);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_PAY_CYCLE_CREATED,
					Constants.SUCCESS_PAY_CYCLE_CREATED, Constants.PAY_PERIOD_CREATED_SUCCESSFULLY);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);

		}
		catch (ApplicationException e) {
			if (e.getCause() != null) {
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_PAY_CYCLE_CREATED,
						e.getCause().getMessage(), Constants.PAY_PERIOD_CREATED_UNSUCCESSFULLY);
			} else {
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_PAY_PERIOD_CREATED,
						e.getMessage(), Constants.PAY_PERIOD_CREATED_UNSUCCESSFULLY);
			}
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	// For Fetching all Pay Cycle
	@RequestMapping(value = "/v1/pay_cycles/{organizationId}/{organizationName}/{userId}/{roleName}", method = RequestMethod.GET)
	public ResponseEntity<BaseResponse> getAllPayCyclesOfAnOrganization(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, 
			@PathVariable int organizationId,
			@PathVariable String organizationName,
			@PathVariable String userId,
			@PathVariable String roleName) {
		logger.info("Entry into method: getAllPayCyclesOfAnOrganization");
		BaseResponse response = new ListPayCycleResponse();
		try {
			List<PayCycleVo> listAllPayPeriods = payCycleService.getAllPayCyclesOfAnOrganization(organizationId,
					userId, roleName);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((ListPayCycleResponse) response).setData(listAllPayPeriods);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_PAY_CYCLE_FETCH,
					Constants.SUCCESS_PAY_CYCLE_FETCH, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_PAY_CYCLE_FETCH,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// For Fetching Pay Cycle by Id
	@RequestMapping(value = "/v1/pay_cycles/{organizationId}/{organizationName}/{userId}/{roleName}/{id}", method = RequestMethod.GET)
	public ResponseEntity<BaseResponse> getPayCycleById(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, 
			@PathVariable int organizationId, 
			@PathVariable String organizationName,
			@PathVariable String userId,
			@PathVariable String roleName,
			@PathVariable int id) {
		logger.info("Entry into method: getPayCycleById");
		BaseResponse response = new PayCycleResponse();
		try {
			PayCycleVo payCycleVo = payCycleService.getPayCycleById(id);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((PayCycleResponse) response).setData(payCycleVo);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_PAY_CYCLE_FETCH,
					Constants.SUCCESS_PAY_CYCLE_FETCH, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_PAY_CYCLE_FETCH,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// For Activate/Deactivate Pay Cycle

	@RequestMapping(value = "/v1/pay_cycles/{organizationId}/{userId}/{id}/{roleName}/{status}", method = RequestMethod.DELETE)
	public ResponseEntity<BaseResponse> deletePayCycle(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @PathVariable int organizationId, @PathVariable String userId,
			@PathVariable int id, @PathVariable String roleName, @PathVariable String status) {
		logger.info("Entry into method: deletePayCycle");
		BaseResponse response = new PayCycleResponse();
		try {
			PayCycleVo payCycleVo = payCycleService.deletePayCycle(id, status, userId, roleName, organizationId);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			payCycleVo.setKeyToken(null);
			payCycleVo.setValueToken(null);
			((PayCycleResponse) response).setData(payCycleVo);
			if (status.equals(CommonConstants.STATUS_AS_ACTIVE)) {
				response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_PAY_CYCLE_ACTIVATED,
						Constants.SUCCESS_PAY_CYCLE_ACTIVATED, Constants.PAY_PERIOD_DELETED_SUCCESSFULLY);
				logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			} else if (status.equals(CommonConstants.STATUS_AS_INACTIVE)) {
				response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_PAY_CYCLE_DEACTIVATED,
						Constants.SUCCESS_PAY_CYCLE_DEACTIVATED, Constants.PAY_PERIOD_DELETED_SUCCESSFULLY);
				logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			}
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_PAY_CYCLE_DELETED,
					e.getMessage(), Constants.PAY_PERIOD_DELETED_UNSUCCESSFULLY);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	// For Update Pay Period
	@RequestMapping(value = "/v1/pay_cycles", method = RequestMethod.PUT)
	public ResponseEntity<BaseResponse> updatePayCycle(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @RequestBody JSONObject<PayCycleRequest> payCycleRequest) {
		logger.info("Entry into method:updatePayPeriod");
		BaseResponse response = new PayCycleResponse();
		try {
			logger.info("Request Payload:" + generateRequestAndResponseLogPaylod(payCycleRequest));
			PayCycleVo payCycleVo = PayrollConvertToVoHelper.getInstance()
					.convertPayCycleVoFromPayCycleRequest(payCycleRequest.getData());
			payCycleVo = payCycleService.updatePayCycle(payCycleVo);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			payCycleVo.setKeyToken(null);
			payCycleVo.setValueToken(null);
			((PayCycleResponse) response).setData(payCycleVo);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_PAY_CYCLE_UPDATED,
					Constants.SUCCESS_PAY_CYCLE_UPDATED, Constants.PAY_PERIOD_UPDATED_SUCCESSFULLY);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_PAY_CYCLE_UPDATED,
					e.getMessage(), Constants.PAY_PERIOD_UPDATED_UNSUCCESSFULLY);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
//	// For fetching Dropdowns by orgId
//	@RequestMapping(value = "/v1/pay_periods/dropdowns/{organizationId}", method = RequestMethod.GET)
//	public ResponseEntity<BaseResponse> getPayPeriodDropDownData(HttpServletRequest httpRequest,
//			HttpServletResponse httpResponse, @PathVariable int organizationId) {
//		logger.info("Entry into method: getPayPeriodDropDownData");
//		BaseResponse response = new PayPeriodDropDownResponse();
//		try {
//			PayPeriodDropDownVo payPeriodDropDown = payCycleService.getPayPeriodDropDownData(organizationId);
//			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
//			((PayPeriodDropDownResponse) response).setData(payPeriodDropDown);
//			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_DROP_DOWN_FETCH,
//					Constants.SUCCESS_DROP_DOWN_FETCH, Constants.SUCCESS_DURING_GET);
//			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
//			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
//		} catch (ApplicationException e) {
//			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_DROP_DOWN_FETCH, e.getMessage(),
//					Constants.FAILURE_DURING_GET);
//			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
//			logger.info(response);
//			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
//		}
//	}
//
//
}

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
import com.blackstrawai.payroll.PayFrequencyListVo;
import com.blackstrawai.payroll.PayPeriodMonthlyTableVo;
import com.blackstrawai.payroll.PayPeriodService;
import com.blackstrawai.payroll.PayPeriodVo;
import com.blackstrawai.payroll.dropdowns.PayPeriodDropDownVo;
import com.blackstrawai.request.JSONObject;
import com.blackstrawai.request.payroll.PayPeriodRequest;
import com.blackstrawai.response.payroll.ListPayPeriodResponse;
import com.blackstrawai.response.payroll.PayPeriodDropDownResponse;
import com.blackstrawai.response.payroll.PayPeriodMonthlyTableResponse;
import com.blackstrawai.response.payroll.PayPeriodResponse;

@RestController
@CrossOrigin
@RequestMapping("/decifer/sp/pay_period")
public class PayPeriodController extends BaseController {

	@Autowired
	PayPeriodService payPeriodservice;

	private Logger logger = Logger.getLogger(PayPeriodController.class);

	// Create new pay period
	@RequestMapping(value = "/v1/pay_periods", method = RequestMethod.POST)
	public ResponseEntity<BaseResponse> createPayPeriod(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @RequestBody JSONObject<PayPeriodRequest> payPeriodRequest) {

		logger.info("Entry into method: createPayPeriod");
		BaseResponse response = new PayPeriodResponse();
		try {
			logger.info("Request Payload:" + generateRequestAndResponseLogPaylod(payPeriodRequest));
			PayPeriodVo payPeriodVo = PayrollConvertToVoHelper.getInstance()
					.convertPayPeriodVoFromPayPeriodRequest(payPeriodRequest.getData());
			payPeriodVo = payPeriodservice.createPayPeriod(payPeriodVo);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			payPeriodVo.setKeyToken(null);
			payPeriodVo.setValueToken(null);
			((PayPeriodResponse) response).setData(payPeriodVo);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_PAY_PERIOD_CREATED,
					Constants.SUCCESS_PAY_PERIOD_CREATED, Constants.PAY_PERIOD_CREATED_SUCCESSFULLY);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);

		}
		catch (ApplicationException e) {
			if (e.getCause() != null) {
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_PAY_PERIOD_CREATED,
						e.getCause().getMessage(), Constants.PAY_PERIOD_CREATED_UNSUCCESSFULLY);
			} else {
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_PAY_PERIOD_CREATED,
						e.getMessage(), Constants.PAY_PERIOD_CREATED_UNSUCCESSFULLY);
			}
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	// For Fetching all Pay Period
	@RequestMapping(value = "/v1/pay_periods/{organizationId}/{organizationName}/{userId}/{roleName}", method = RequestMethod.GET)
	public ResponseEntity<BaseResponse> getAllPayPeriodOfAnOrganization(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, 
			@PathVariable int organizationId,
			@PathVariable String organizationName,
			@PathVariable String userId,
			@PathVariable String roleName) {
		logger.info("Entry into method:getAllPayPeriodOfAnOrganization");
		BaseResponse response = new ListPayPeriodResponse();
		try {
			List<PayFrequencyListVo> listAllPayPeriods = payPeriodservice.getAllPayPeriodsOfAnOrganization(organizationId,
					userId, roleName);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((ListPayPeriodResponse) response).setData(listAllPayPeriods);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_PAY_PERIOD_FETCH,
					Constants.SUCCESS_PAY_PERIOD_FETCH, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_PAY_PERIOD_FETCH,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// For Fetching Pay Period by Id
	@RequestMapping(value = "/v1/pay_periods/{organizationId}/{organizationName}/{userId}/{roleName}/{id}", method = RequestMethod.GET)
	public ResponseEntity<BaseResponse> getPayPeriodById(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, 
			@PathVariable int organizationId, 
			@PathVariable String organizationName,
			@PathVariable String userId,
			@PathVariable String roleName,
			@PathVariable int id) {
		logger.info("Entry into method:getPayPerodById");
		BaseResponse response = new PayPeriodResponse();
		try {
			PayPeriodVo payPeriodVo = payPeriodservice.getPayPeriodById(id);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((PayPeriodResponse) response).setData(payPeriodVo);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_PAY_PERIOD_FETCH,
					Constants.SUCCESS_PAY_PERIOD_FETCH, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_PAY_PERIOD_FETCH,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// For Deleting Pay Period

	@RequestMapping(value = "/v1/pay_periods/{organizationId}/{userId}/{id}/{roleName}/{status}", method = RequestMethod.DELETE)
	public ResponseEntity<BaseResponse> deletePayPeriod(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @PathVariable int organizationId, @PathVariable String userId,
			@PathVariable int id, @PathVariable String roleName, @PathVariable String status) {
		logger.info("Entry into method:deletePayPeriod");
		BaseResponse response = new PayPeriodResponse();
		try {
			PayPeriodVo payPeriodVo = payPeriodservice.deletePayPeriod(id, status, userId, roleName, organizationId);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			payPeriodVo.setKeyToken(null);
			payPeriodVo.setValueToken(null);
			((PayPeriodResponse) response).setData(payPeriodVo);
			if (status.trim().equalsIgnoreCase(CommonConstants.STATUS_AS_ACTIVE) || status.trim().equalsIgnoreCase(CommonConstants.DISPLAY_STATUS_AS_ACTIVE)) {
				response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_PAY_PERIOD_ACTIVATED,
						Constants.SUCCESS_PAY_PERIOD_ACTIVATED, Constants.PAY_PERIOD_DELETED_SUCCESSFULLY);
				logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			} else if (status.trim().equalsIgnoreCase(CommonConstants.STATUS_AS_INACTIVE) || status.trim().equalsIgnoreCase(CommonConstants.DISPLAY_STATUS_AS_INACTIVE)) {
				response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_PAY_PERIOD_DEACTIVATED,
						Constants.SUCCESS_PAY_PERIOD_DEACTIVATED, Constants.PAY_PERIOD_DELETED_SUCCESSFULLY);
				logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			}
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_PAY_PERIOD_DELETED,
					e.getMessage(), Constants.PAY_PERIOD_DELETED_UNSUCCESSFULLY);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	// For Update Pay Period
	@RequestMapping(value = "/v1/pay_periods", method = RequestMethod.PUT)
	public ResponseEntity<BaseResponse> updatePayPeriod(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @RequestBody JSONObject<PayPeriodRequest> payPeriodRequest) {
		logger.info("Entry into method:updatePayPeriod");
		BaseResponse response = new PayPeriodResponse();
		try {
			logger.info("Request Payload:" + generateRequestAndResponseLogPaylod(payPeriodRequest));
			PayPeriodVo payPeriodVo = PayrollConvertToVoHelper.getInstance()
					.convertPayPeriodVoFromPayPeriodRequest(payPeriodRequest.getData());
			payPeriodVo = payPeriodservice.updatePayPeriod(payPeriodVo);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			payPeriodVo.setKeyToken(null);
			payPeriodVo.setValueToken(null);
			((PayPeriodResponse) response).setData(payPeriodVo);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_PAY_PERIOD_UPDATED,
					Constants.SUCCESS_PAY_PERIOD_UPDATED, Constants.PAY_PERIOD_UPDATED_SUCCESSFULLY);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_PAY_PERIOD_UPDATED,
					e.getMessage(), Constants.PAY_PERIOD_UPDATED_UNSUCCESSFULLY);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	// For fetching Dropdowns by orgId
	@RequestMapping(value = "/v1/pay_periods/dropdowns/{organizationId}", method = RequestMethod.GET)
	public ResponseEntity<BaseResponse> getPayPeriodDropDownData(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @PathVariable int organizationId) {
		logger.info("Entry into method: getPayPeriodDropDownData");
		BaseResponse response = new PayPeriodDropDownResponse();
		try {
			PayPeriodDropDownVo payPeriodDropDown = payPeriodservice.getPayPeriodDropDownData(organizationId);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((PayPeriodDropDownResponse) response).setData(payPeriodDropDown);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_DROP_DOWN_FETCH,
					Constants.SUCCESS_DROP_DOWN_FETCH, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_DROP_DOWN_FETCH, e.getMessage(),
					Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	// For fetching Monthly Table
		@RequestMapping(value = "/v1/pay_periods/{cycle}/{year}", method = RequestMethod.GET)
		public ResponseEntity<BaseResponse> getPayPeriodMonthlyTable(HttpServletRequest httpRequest,
				HttpServletResponse httpResponse, @PathVariable String cycle, @PathVariable int year) {
			logger.info("Entry into method: getPayPeriodMonthlyTable");
			BaseResponse response = new PayPeriodMonthlyTableResponse();
			try {
				List<PayPeriodMonthlyTableVo> monthlyTable = payPeriodservice.getPayPeriodMonthlyTable(year, cycle);
				setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
				((PayPeriodMonthlyTableResponse) response).setData(monthlyTable);
				response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_DROP_DOWN_FETCH,
						Constants.SUCCESS_DROP_DOWN_FETCH, Constants.SUCCESS_DURING_GET);
				logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
				return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
			} catch (ApplicationException e) {
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_DROP_DOWN_FETCH, e.getMessage(),
						Constants.FAILURE_DURING_GET);
				logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
				logger.info(response);
				return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
		}
	
	

}
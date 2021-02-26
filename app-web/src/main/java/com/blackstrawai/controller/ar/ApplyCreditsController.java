package com.blackstrawai.controller.ar;

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
import com.blackstrawai.ar.ApplyCreditsService;
import com.blackstrawai.ar.applycredits.ApplyCreditsBasicVo;
import com.blackstrawai.ar.applycredits.ApplyCreditsVo;
import com.blackstrawai.ar.dropdowns.ApplyCreditsCustomerDropDownVo;
import com.blackstrawai.ar.dropdowns.ApplyCreditsDropDownVo;
import com.blackstrawai.common.BaseController;
import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.common.Constants;
import com.blackstrawai.helper.ArConvertToVoHelper;
import com.blackstrawai.request.JSONObject;
import com.blackstrawai.request.ar.applycredits.ApplyCreditsRequest;
import com.blackstrawai.response.ar.applycredits.ApplyCreditsCustomerDropDownResponse;
import com.blackstrawai.response.ar.applycredits.ApplyCreditsDropDownResponse;
import com.blackstrawai.response.ar.applycredits.ApplyCreditsListResponse;
import com.blackstrawai.response.ar.applycredits.ApplyCreditsResponse;
import com.blackstrawai.response.keycontact.employee.EmployeeResponse;

@RestController
@CrossOrigin
@RequestMapping("/decifer/ar/apply_credit")
public class ApplyCreditsController extends BaseController {

	private Logger logger = Logger.getLogger(ApplyCreditsController.class);

	@Autowired
	ApplyCreditsService applyCreditsService;

	// For Creating Apply Credits
	@RequestMapping(value = "/v1/apply_credits", method = RequestMethod.POST)
	public ResponseEntity<BaseResponse> createApplyCredits(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @RequestBody JSONObject<ApplyCreditsRequest> applyCreditsRequest) {
		logger.info("Entry into method: createApplyCredits");
		BaseResponse response = new ApplyCreditsResponse();
		try {
			logger.info("Request Payload:" + generateRequestAndResponseLogPaylod(applyCreditsRequest));
			ApplyCreditsVo applyCreditsVo = ArConvertToVoHelper.getInstance()
					.convertApplyCreditsVoFromApplyCreditsRequest(applyCreditsRequest.getData());
			applyCreditsService.createApplyCredits(applyCreditsVo);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_APPLY_CREDITS_CREATED,
					Constants.SUCCESS_APPLY_CREDITS_CREATED, Constants.APPLY_CREDITS_CREATED_SUCCESSFULLY);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			logger.error("Error Payload:" + e.getMessage());
			if (e.getCause() != null) {
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_APPLY_CREDITS_CREATED,
						e.getCause().getMessage(), Constants.APPLY_CREDITS_CREATED_UNSUCCESSFULLY);
			} else {
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_APPLY_CREDITS_CREATED,
						e.getMessage(), Constants.APPLY_CREDITS_CREATED_UNSUCCESSFULLY);
			}

			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	// For Fetching all Apply Credits
	@RequestMapping(value = "/v1/apply_credits/{organizationId}/{organizationName}/{userId}/{roleName}", method = RequestMethod.GET)
	public ResponseEntity<BaseResponse> getAllApplyCreditsOfAnOrganization(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, 
			@PathVariable int organizationId, 
			@PathVariable String organizationName,
			@PathVariable String userId,
			@PathVariable String roleName) {
		logger.info("Entry into method: getAllApplyCreditsOfAnOrganization");
		BaseResponse response = new ApplyCreditsListResponse();
		try {
			List<ApplyCreditsBasicVo> listApplyCredits = applyCreditsService
					.getAllApplyCreditsOfAnOrganization(organizationId, userId, roleName);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((ApplyCreditsListResponse) response).setData(listApplyCredits);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_APPLY_CREDITS_FETCH,
					Constants.SUCCESS_APPLY_CREDITS_FETCH, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_APPLY_CREDITS_FETCH,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// For Fetching Apply Credits by Id

	@RequestMapping(value = "/v1/apply_credits/{organizationId}/{organizationName}/{userId}/{roleName}/{id}", method = RequestMethod.GET)
	public ResponseEntity<BaseResponse> getApplyCreditsById(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, 
			@PathVariable int organizationId, 
			@PathVariable String organizationName,
			@PathVariable String userId,
			@PathVariable String roleName,
			@PathVariable int id) {
		logger.info("Entry into method: getApplyCreditsById");
		BaseResponse response = new ApplyCreditsResponse();
		try {
			ApplyCreditsVo applyCreditsVo = applyCreditsService.getApplyCreditsById(id);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((ApplyCreditsResponse) response).setData(applyCreditsVo);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_APPLY_CREDITS_FETCH,
					Constants.SUCCESS_APPLY_CREDITS_FETCH, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_APPLY_CREDITS_FETCH,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// update apply credits
	@RequestMapping(value = "/v1/apply_credits", method = RequestMethod.PUT)
	public ResponseEntity<BaseResponse> updateApplyCredits(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @RequestBody JSONObject<ApplyCreditsRequest> applyCreditsRequest) {
		logger.info("Entry into method: updateApplyCredits");
		BaseResponse response = new EmployeeResponse();
		try {
			logger.info("Request Payload:" + generateRequestAndResponseLogPaylod(applyCreditsRequest));
			ApplyCreditsVo applyCreditsVo = ArConvertToVoHelper.getInstance()
					.convertApplyCreditsVoFromApplyCreditsRequest(applyCreditsRequest.getData());
			applyCreditsService.updateApplyCredits(applyCreditsVo);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_APPLY_CREDITS_UPDATED,
					Constants.SUCCESS_APPLY_CREDITS_UPDATED, Constants.APPLY_CREDITS_UPDATED_SUCCESSFULLY);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			if (e.getCause() != null) {
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_APPLY_CREDITS_UPDATED,
						e.getCause().getMessage(), Constants.APPLY_CREDITS_UPDATED_UNSUCCESSFULLY);
			} else {
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_APPLY_CREDITS_UPDATED,
						e.getMessage(), Constants.APPLY_CREDITS_UPDATED_UNSUCCESSFULLY);
			}
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// to get all customer details for organization
	@RequestMapping(value = "/v1/dropsdowns/apply_credits/{organizationId}")
	public ResponseEntity<BaseResponse> getApplyCreditsCustomerDropDownData(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, @PathVariable int organizationId) {
		logger.info("Entry into getApplyCreditsCustomerDropDownData");
		BaseResponse response = new ApplyCreditsCustomerDropDownResponse();
		try {
			ApplyCreditsCustomerDropDownVo data = applyCreditsService
					.getApplyCreditsCustomerDropDownData(organizationId);
			setTokens(response, httpServletRequest.getHeader("keyToken"), httpServletRequest.getHeader("valueToken"));
			((ApplyCreditsCustomerDropDownResponse) response).setData(data);
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

	// to get all dropdown data apply credits
	@RequestMapping(value = "/v1/dropsdowns/customer/customers/{organizationId}/{organizationName}/{currencyId}/{userId}/{roleName}/{id}", method = RequestMethod.GET)
	public ResponseEntity<BaseResponse> getApplyCreditsDropDownData(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, 
			@PathVariable int organizationId, 
			@PathVariable String organizationName,
			@PathVariable int currencyId,
			@PathVariable String userId,
			@PathVariable String roleName,
			@PathVariable int id) {
		logger.info("Entry into getApplyCreditsDropDownData");
		BaseResponse response = new ApplyCreditsDropDownResponse();
		try {
			ApplyCreditsDropDownVo data = applyCreditsService.getApplyCreditsDropDownData(organizationId, id,currencyId);
			setTokens(response, httpResponse.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((ApplyCreditsDropDownResponse) response).setData(data);
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

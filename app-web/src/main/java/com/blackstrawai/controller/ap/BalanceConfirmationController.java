package com.blackstrawai.controller.ap;

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
import com.blackstrawai.ap.BalanceConfirmationService;
import com.blackstrawai.ap.balanceconfirmation.BalanceConfirmationBasicVo;
import com.blackstrawai.ap.balanceconfirmation.BalanceConfirmationVo;
import com.blackstrawai.ap.dropdowns.BalanceConfirmationDropDownVo;
import com.blackstrawai.common.BaseController;
import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.common.CommonConstants;
import com.blackstrawai.common.Constants;
import com.blackstrawai.helper.ApConvertToVoHelper;
import com.blackstrawai.request.JSONObject;
import com.blackstrawai.request.ap.balanceconfirmation.BalanceConfirmationRequest;
import com.blackstrawai.response.ap.balanceconfirmation.BalanceConfirmationDropDownResponse;
import com.blackstrawai.response.ap.balanceconfirmation.BalanceConfirmationListResponse;
import com.blackstrawai.response.ap.balanceconfirmation.BalanceConfirmationResponse;

@RestController
@CrossOrigin
@RequestMapping("/decifer/ap")
public class BalanceConfirmationController extends BaseController {

	@Autowired
	BalanceConfirmationService balanceConfirmationService;

	private Logger logger = Logger.getLogger(BalanceConfirmationController.class);

	// For Creating Balance Confirmation
	@RequestMapping(value = "/v1/balance_confirmation", method = RequestMethod.POST)
	public ResponseEntity<BaseResponse> createBalanceConfirmation(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse,
			@RequestBody JSONObject<BalanceConfirmationRequest> balanceConfirmationRequest) {
		logger.info("Entry into method: createBalanceConfirmation");
		BaseResponse response = new BalanceConfirmationResponse();
		try {
			logger.info("Request Payload:" + generateRequestAndResponseLogPaylod(balanceConfirmationRequest));
			BalanceConfirmationVo balanceConfirmationVo = ApConvertToVoHelper.getInstance()
					.convertBalanceConfirmationVoFromBalanceConfirmationRequest(balanceConfirmationRequest.getData());
			balanceConfirmationService.createBalanceConfirmation(balanceConfirmationVo);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_BALANCE_CONFIRMATION_CREATED,
					Constants.SUCCESS_BALANCE_CONFIRMATION_CREATED,
					Constants.BALANCE_CONFIRMATION_CREATED_SUCCESSFULLY);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			logger.error("Error Payload:" + e.getMessage());
			if (e.getCause() != null) {
				response = constructResponse(response, Constants.FAILURE,
						Constants.FAILURE_BALANCE_CONFIRMATION_CREATED, e.getCause().getMessage(),
						Constants.BALANCE_CONFIRMATION_CREATED_UNSUCCESSFULLY);
			} else {
				response = constructResponse(response, Constants.FAILURE,
						Constants.FAILURE_BALANCE_CONFIRMATION_CREATED, e.getMessage(),
						Constants.BALANCE_CONFIRMATION_CREATED_UNSUCCESSFULLY);
			}

			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	// For Fetching all Balance Confirmation details
	@RequestMapping(value = "/v1/balance_confirmation/{organizationId}/{vendorId}", method = RequestMethod.GET)
	public ResponseEntity<BaseResponse> getAllBalanceConfirmationOfAnOrganization(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @PathVariable int organizationId, @PathVariable int vendorId) {
		logger.info("Entry into method: getAllBalanceConfirmationOfAnOrganization");
		BaseResponse response = new BalanceConfirmationListResponse();
		try {
			List<BalanceConfirmationBasicVo> listAllBalanceConfirmation = balanceConfirmationService
					.getAllBalanceConfirmation(organizationId, vendorId);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((BalanceConfirmationListResponse) response).setData(listAllBalanceConfirmation);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_BALANCE_CONFIRMATION_FETCH,
					Constants.SUCCESS_BALANCE_CONFIRMATION_FETCH, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_BALANCE_CONFIRMATION_FETCH,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// For Fetching Balance Confirmation details by id
	@RequestMapping(value = "/v1/balance_confirmation/{organizationId}/{userId}/{id}", method = RequestMethod.GET)
	public ResponseEntity<BaseResponse> getBalanceConfirmationById(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @PathVariable int organizationId, @PathVariable int userId,
			@PathVariable int id) {
		logger.info("Entry into method: getBalanceConfirmationById");
		BaseResponse response = new BalanceConfirmationResponse();
		try {
			BalanceConfirmationVo balanceConfirmationVo = balanceConfirmationService.getBalanceConfirmationById(id);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((BalanceConfirmationResponse) response).setData(balanceConfirmationVo);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_BALANCE_CONFIRMATION_FETCH,
					Constants.SUCCESS_BALANCE_CONFIRMATION_FETCH, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_BALANCE_CONFIRMATION_FETCH,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/v1/dropsdowns/balance_confirmation/{organizationId}/{id}")
	public ResponseEntity<BaseResponse> getBalanceConfirmationDropDownData(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, @PathVariable int organizationId, @PathVariable int id) {
		logger.info("Entry into getAccountingAspectsDropDownData");
		BaseResponse response = new BalanceConfirmationDropDownResponse();
		try {
			BalanceConfirmationDropDownVo data = balanceConfirmationService
					.getBalanceConfimrationDropDownData(organizationId, id);
			setTokens(response, httpServletRequest.getHeader("keyToken"), httpServletRequest.getHeader("valueToken"));
			((BalanceConfirmationDropDownResponse) response).setData(data);
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

	// For Updating Balance Confirmation
	@RequestMapping(value = "/v1/balance_confirmation", method = RequestMethod.PUT)
	public ResponseEntity<BaseResponse> updateBalanceConfirmation(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse,
			@RequestBody JSONObject<BalanceConfirmationRequest> balanceConfirmationRequest) {
		logger.info("Entry into method: updateBalanceConfirmation");
		BaseResponse response = new BalanceConfirmationResponse();
		try {
			logger.info("Request Payload:" + generateRequestAndResponseLogPaylod(balanceConfirmationRequest));
			BalanceConfirmationVo balanceConfirmationVo = ApConvertToVoHelper.getInstance()
					.convertBalanceConfirmationVoFromBalanceConfirmationRequest(balanceConfirmationRequest.getData());
			balanceConfirmationService.updateBalanceConfirmation(balanceConfirmationVo);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_BALANCE_CONFIRMATION_UPDATED,
					Constants.SUCCESS_BALANCE_CONFIRMATION_UPDATED,
					Constants.BALANCE_CONFIRMATION_UPDATED_SUCCESSFULLY);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			logger.error("Error Payload:" + e.getMessage());
			if (e.getCause() != null) {
				response = constructResponse(response, Constants.FAILURE,
						Constants.FAILURE_BALANCE_CONFIRMATION_UPDATED, e.getCause().getMessage(),
						Constants.BALANCE_CONFIRMATION_UPDATED_UNSUCCESSFULLY);
			} else {
				response = constructResponse(response, Constants.FAILURE,
						Constants.FAILURE_BALANCE_CONFIRMATION_UPDATED, e.getMessage(),
						Constants.BALANCE_CONFIRMATION_UPDATED_UNSUCCESSFULLY);
			}

			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	// for acknowledge and decline a balance confirmation

	@RequestMapping(value = "/v1/balance_confirmation/{organizationId}/{userId}/{id}/{status}", method = RequestMethod.PUT)
	public ResponseEntity<BaseResponse> statusChange(HttpServletRequest httpRequest, HttpServletResponse httpResponse,
			@PathVariable int id, @PathVariable String status) {
		logger.info("Entry into method:deleteVoucher");
		BaseResponse response = new BalanceConfirmationResponse();
		try {
			balanceConfirmationService.statusChange(id, status);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			if (status.equals(CommonConstants.STATUS_AS_ACKNOWLEDGE)) {
				response = constructResponse(response, Constants.SUCCESS,
						Constants.SUCCESS_BALANCE_CONFIRMATION_ACKNOWLEDGED,
						Constants.SUCCESS_BALANCE_CONFIRMATION_ACKNOWLEDGED,
						Constants.BALANCE_CONFIRMATION_ACKNOWLEDGED_SUCCESSFULLY);
				logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			} else if (status.equals(CommonConstants.STATUS_AS_DECLINE)) {
				response = constructResponse(response, Constants.SUCCESS,
						Constants.SUCCESS_BALANCE_CONFIRMATION_DECLINED,
						Constants.SUCCESS_BALANCE_CONFIRMATION_DECLINED,
						Constants.BALANCE_CONFIRMATION_DECLINED_SUCCESSFULLY);
				logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			} else if (status.equals(CommonConstants.STATUS_AS_WITHDRAW)) {
				response = constructResponse(response, Constants.SUCCESS,
						Constants.SUCCESS_BALANCE_CONFIRMATION_WITHDRAW,
						Constants.SUCCESS_BALANCE_CONFIRMATION_WITHDRAW,
						Constants.BALANCE_CONFIRMATION_WITHDRAW_SUCCESSFULLY);
				logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			}
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			if (status.equals(CommonConstants.STATUS_AS_ACKNOWLEDGE)) {
				response = constructResponse(response, Constants.FAILURE,
						Constants.FAILURE_BALANCE_CONFIRMATION_ACKNOWLEDGED, e.getMessage(),
						Constants.BALANCE_CONFIRMATION_ACKNOWLEDGED_UNSUCCESSFULLY);
			} else if (status.equals(CommonConstants.STATUS_AS_DECLINE)) {
				response = constructResponse(response, Constants.FAILURE,
						Constants.FAILURE_BALANCE_CONFIRMATION_DECLINED, e.getMessage(),
						Constants.BALANCE_CONFIRMATION_DECLINED_UNSUCCESSFULLY);
			} else if (status.equals(CommonConstants.STATUS_AS_WITHDRAW)) {
				response = constructResponse(response, Constants.FAILURE,
						Constants.FAILURE_BALANCE_CONFIRMATION_WITHDRAW, e.getMessage(),
						Constants.BALANCE_CONFIRMATION_WITHDRAW_UNSUCCESSFULLY);
			}

			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

}

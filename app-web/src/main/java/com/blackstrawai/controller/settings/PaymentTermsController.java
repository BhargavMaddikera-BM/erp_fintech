package com.blackstrawai.controller.settings;

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
import com.blackstrawai.helper.SettingsConvertToVoHelper;
import com.blackstrawai.request.JSONObject;
import com.blackstrawai.request.settings.PaymentTermsRequest;
import com.blackstrawai.response.settings.ListPaymentTermsResponse;
import com.blackstrawai.response.settings.PaymentTermsResponse;
import com.blackstrawai.settings.PaymentTermsService;
import com.blackstrawai.settings.PaymentTermsVo;

@RestController
@CrossOrigin
//This will be part of Settings and Preferences. Hence sp.
@RequestMapping("/decifer/sp/payment_term")
public class PaymentTermsController extends BaseController {

	@Autowired
	PaymentTermsService paymentTermsservice;

	private Logger logger = Logger.getLogger(PaymentTermsController.class);

	// For Creating PaymentTerms
	@RequestMapping(value = "/v1/payment_terms", method = RequestMethod.POST)
	public ResponseEntity<BaseResponse> createPaymentTerms(HttpServletRequest httpRequest, HttpServletResponse httpResponse,
			@RequestBody JSONObject<PaymentTermsRequest> paymentTermsRequest) {
		logger.info("Entry into method: createPaymentTerms");
		BaseResponse response = new PaymentTermsResponse();
		try {
			logger.info("Request Payload:" + generateRequestAndResponseLogPaylod(paymentTermsRequest));
			PaymentTermsVo paymentTermsVo = SettingsConvertToVoHelper.getInstance()
					.convertPaymentTermsVoFromPaymentTermsRequest(paymentTermsRequest.getData());
			paymentTermsVo = paymentTermsservice.createPaymentTerms(paymentTermsVo);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			paymentTermsVo.setKeyToken(null);
			paymentTermsVo.setValueToken(null);
			((PaymentTermsResponse) response).setData(paymentTermsVo);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_PAYMENT_TERMS_CREATED,
					Constants.SUCCESS_PAYMENT_TERMS_CREATED, Constants.PAYMENT_TERMS_CREATED_SUCCESSFULLY);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);

		} catch (ApplicationException e) {
			if(e.getCause()!=null){
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_PAYMENT_TERMS_CREATED,
						e.getCause().getMessage(), Constants.PEYMENT_TERMS_CREATED_UNSUCCESSFULLY);
			}else{
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_PAYMENT_TERMS_CREATED,
						e.getMessage(), Constants.PEYMENT_TERMS_CREATED_UNSUCCESSFULLY);
			}			
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/*	// For Fetching all PaymentTerms details
	@RequestMapping(value = "/v1/payment_terms/{organizationId}/{organizationName}", method = RequestMethod.GET)
	public ResponseEntity<BaseResponse> getAllPaymentTermsOfAnOrganization(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @PathVariable int organizationId,@PathVariable String organizationName) {
		logger.info("Entry into method:getAllPaymentTermsOfAnOrganization");
		BaseResponse response = new ListPaymentTermsResponse();
		try {
			List<PaymentTermsVo> listAllPaymentTerms = paymentTermsservice
					.getAllPaymentTermsOfAnOrganization(organizationId);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((ListPaymentTermsResponse) response).setData(listAllPaymentTerms);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_PAYMENT_TERMS_FETCH,
					Constants.SUCCESS_PAYMENT_TERMS_FETCH, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_PAYMENT_TERMS_FETCH,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}*/

	// For Fetching PaymentTerms by Id
	@RequestMapping(value = "/v1/payment_terms/{organizationId}/{organizationName}/{userId}/{roleName}/{id}", method = RequestMethod.GET)
	public ResponseEntity<BaseResponse> getPaymentTermsById(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, 
			@PathVariable int organizationId, 
			@PathVariable String organizationName,
			@PathVariable String userId,
			@PathVariable String roleName,
			@PathVariable int id) {
		logger.info("Entry into method:getPaymentTermsById");
		BaseResponse response = new PaymentTermsResponse();
		try {
			PaymentTermsVo paymentTermsVo = paymentTermsservice.getPaymentTermsById(id);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((PaymentTermsResponse) response).setData(paymentTermsVo);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_PAYMENT_TERMS_FETCH,
					Constants.SUCCESS_PAYMENT_TERMS_FETCH, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_PAYMENT_TERMS_FETCH,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// For Fetching all PaymentTerms details
	@RequestMapping(value = "/v1/payment_terms/{organizationId}/{organizationName}/{userId}/{roleName}", method = RequestMethod.GET)
	public ResponseEntity<BaseResponse> getAllPaymentTermsOfAnOrganizationForUserAndRole(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @PathVariable int organizationId,@PathVariable String organizationName,
			@PathVariable String userId,@PathVariable String roleName) {
		logger.info("Entry into method:getAllPaymentTermsOfAnOrganizationForUserAndRole");
		BaseResponse response = new ListPaymentTermsResponse();
		try {
			List<PaymentTermsVo> listAllPaymentTerms = paymentTermsservice
					.getAllPaymentTermsOfAnOrganizationForUserAndRole(organizationId,userId,roleName);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((ListPaymentTermsResponse) response).setData(listAllPaymentTerms);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_PAYMENT_TERMS_FETCH,
					Constants.SUCCESS_PAYMENT_TERMS_FETCH, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_PAYMENT_TERMS_FETCH,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// For Deleting Payment Terms

	@RequestMapping(value = "/v1/payment_terms/{organizationId}/{organizationName}/{userId}/{id}/{roleName}/{status}", method = RequestMethod.DELETE)
	public ResponseEntity<BaseResponse> deletePaymentTerms(HttpServletRequest httpRequest, 
			HttpServletResponse httpResponse,
			@PathVariable int organizationId, 
			@PathVariable String organizationName,
			@PathVariable String userId, 
			@PathVariable int id,
			@PathVariable String roleName,
			@PathVariable String status) {
		logger.info("Entry into method:deletePaymentTerms");
		BaseResponse response = new PaymentTermsResponse();
		try {
			PaymentTermsVo paymentTermsVo = paymentTermsservice.deletePaymentTerms(id,status,userId,roleName);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			paymentTermsVo.setKeyToken(null);
			paymentTermsVo.setValueToken(null);
			((PaymentTermsResponse) response).setData(paymentTermsVo);
			if(status.equals(CommonConstants.STATUS_AS_ACTIVE)) {
				response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_PAYMENT_TERMS_ACTIVATED,
						Constants.SUCCESS_PAYMENT_TERMS_ACTIVATED, Constants.PAYMENT_TERMS_DELETED_SUCCESSFULLY);
				logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			}else if(status.equals(CommonConstants.STATUS_AS_INACTIVE)) {
				response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_PAYMENT_TERMS_DEACTIVATED,
						Constants.SUCCESS_PAYMENT_TERMS_DEACTIVATED, Constants.PAYMENT_TERMS_DELETED_SUCCESSFULLY);
				logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			}
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_PAYMENT_TERMS_DELETED,
					e.getMessage(), Constants.PAYMENT_TERMS_DELETED_UNSUCCESSFULLY);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	// For Update Payment Terms

	@RequestMapping(value = "/v1/payment_terms", method = RequestMethod.PUT)
	public ResponseEntity<BaseResponse> updatePaymentTerms(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @RequestBody JSONObject<PaymentTermsRequest> paymentTermsRequest) {
		logger.info("Entry into method:updatePaymentTerms");
		BaseResponse response = new PaymentTermsResponse();
		try {
			logger.info("Request Payload:" + generateRequestAndResponseLogPaylod(paymentTermsRequest));
			PaymentTermsVo paymentTermsVo = SettingsConvertToVoHelper.getInstance()
					.convertPaymentTermsVoFromPaymentTermsRequest(paymentTermsRequest.getData());
			paymentTermsVo = paymentTermsservice.updatePaymentTerms(paymentTermsVo);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			paymentTermsVo.setKeyToken(null);
			paymentTermsVo.setValueToken(null);
			((PaymentTermsResponse) response).setData(paymentTermsVo);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_PAYMENT_TERMS_UPDATED,
					Constants.SUCCESS_PAYMENT_TERMS_UPDATED, Constants.PAYMENT_TERMS_UPDATED_SUCCESSFULLY);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_PAYMENT_TERMS_UPDATED,
					e.getMessage(), Constants.PAYMENT_TERMS_UPDATED_UNSUCCESSFULLY);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}

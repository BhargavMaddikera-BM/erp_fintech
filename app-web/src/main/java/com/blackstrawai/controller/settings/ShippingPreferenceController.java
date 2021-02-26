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
import com.blackstrawai.request.settings.ShippingPreferenceRequest;
import com.blackstrawai.response.settings.ListshippingPreferenceResponse;
import com.blackstrawai.response.settings.ShippingPreferenceResponse;
import com.blackstrawai.settings.ShippingPreferenceService;
import com.blackstrawai.settings.ShippingPreferenceVo;

@RestController
@CrossOrigin
//This will be part of Settings and Preferences. Hence sp.
@RequestMapping("/decifer/sp/shipping_preference")
public class ShippingPreferenceController extends BaseController {

	@Autowired
	ShippingPreferenceService shippingPreferenceService;

	private Logger logger = Logger.getLogger(ShippingPreferenceController.class);

	// For Creating PaymentTerms
	@RequestMapping(value = "/v1/shippingPreferences", method = RequestMethod.POST)
	public ResponseEntity<BaseResponse> createShippingPreference(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse,
			@RequestBody JSONObject<ShippingPreferenceRequest> shippingPreferenceRequest) {
		logger.info("Entry into method:createShippingPreference");
		BaseResponse response = new ShippingPreferenceResponse();
		try {
			logger.info("Request Payload:" + generateRequestAndResponseLogPaylod(shippingPreferenceRequest));
			ShippingPreferenceVo shippingPreferenceVo = SettingsConvertToVoHelper.getInstance()
					.convertShippingPreferenceVoFromShippingPreferenceRequest(shippingPreferenceRequest.getData());
			shippingPreferenceVo = shippingPreferenceService.createShippingPreference(shippingPreferenceVo);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((ShippingPreferenceResponse) response).setData(shippingPreferenceVo);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_SHIPPING_PREFERENCE_CREATED,
					Constants.SUCCESS_SHIPPING_PREFERENCE_CREATED, Constants.SHIPPING_PREFERENCE_CREATED_SUCCESSFULLY);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);

		} catch (ApplicationException e) {
			if(e.getCause()!=null){
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_SHIPPING_PREFERENCE_CREATED,
						e.getCause().getMessage(), Constants.SHIPPING_PREFERENCE_CREATED_UNSUCCESSFULLY);
			}else{
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_SHIPPING_PREFERENCE_CREATED,
						e.getMessage(), Constants.SHIPPING_PREFERENCE_CREATED_UNSUCCESSFULLY);
			}

			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}


	// For Fetching all PaymentTerms details

	/*	@RequestMapping(value = "/v1/shippingPreferences/{organizationId}/{organizationName}", method = RequestMethod.GET)
	public ResponseEntity<BaseResponse> getAllShippingPreferencesOfAnOrganization(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @PathVariable int organizationId,@PathVariable String organizationName) {
		logger.info("Entry into method:getAllShippingPreferencesOfAnOrganization");
		BaseResponse response = new ListshippingPreferenceResponse();
		try {
			List<ShippingPreferenceVo> listAllShippingPreferences = shippingPreferenceService
					.getAllShippingPreferencesOfAnOrganization(organizationId);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((ListshippingPreferenceResponse) response).setData(listAllShippingPreferences);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_SHIPPING_PREFERENCES_FETCH,
					Constants.SUCCESS_SHIPPING_PREFERENCES_FETCH, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_SHIPPING_PREFERENCES_FETCH,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}*/

	@RequestMapping(value = "/v1/shippingPreferences/{organizationId}/{organizationName}/{userId}/{roleName}", method = RequestMethod.GET)
	public ResponseEntity<BaseResponse> getAllShippingPreferencesOfAnOrganizationForUserAndRole(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @PathVariable int organizationId,@PathVariable String organizationName,
			@PathVariable String userId,@PathVariable String roleName) {
		logger.info("Entry into method:getAllShippingPreferencesOfAnOrganization");
		BaseResponse response = new ListshippingPreferenceResponse();
		try {
			List<ShippingPreferenceVo> listAllShippingPreferences = shippingPreferenceService
					.getAllShippingPreferencesOfAnOrganizationForUserAndRole(organizationId,userId,roleName);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((ListshippingPreferenceResponse) response).setData(listAllShippingPreferences);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_SHIPPING_PREFERENCES_FETCH,
					Constants.SUCCESS_SHIPPING_PREFERENCES_FETCH, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_SHIPPING_PREFERENCES_FETCH,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// For Fetching PaymentTerms by Id

	@RequestMapping(value = "/v1/shippingPreferences/{organizationId}/{organizationName}/{userId}/{roleName}/{id}", method = RequestMethod.GET)
	public ResponseEntity<BaseResponse> getShippingPreferenceById(
			HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, 
			@PathVariable int organizationId, 
			@PathVariable String organizationName,
			@PathVariable String userId,
			@PathVariable String roleName,
			@PathVariable int id) {
		logger.info("Entry into method:getShippingPreferenceById");
		BaseResponse response = new ShippingPreferenceResponse();
		try {
			ShippingPreferenceVo shippingPreferenceVo = shippingPreferenceService.getShippingPreferenceById(id);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((ShippingPreferenceResponse) response).setData(shippingPreferenceVo);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_SHIPPING_PREFERENCES_FETCH,
					Constants.SUCCESS_SHIPPING_PREFERENCES_FETCH, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_SHIPPING_PREFERENCES_FETCH,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// For Deleting Payment Terms


	@RequestMapping(value = "/v1/shippingPreferences/{organizationId}/{organizationName}/{userId}/{id}/{roleName}/{status}", method = RequestMethod.DELETE)
	public ResponseEntity<BaseResponse> deleteShippingPreference(HttpServletRequest httpRequest, 
			HttpServletResponse httpResponse,
			@PathVariable int organizationId, 
			@PathVariable String organizationName,
			@PathVariable String userId, 
			@PathVariable int id,
			@PathVariable String roleName,
			@PathVariable String status) {
		logger.info("Entry into method:deleteShippingPreference");
		BaseResponse response = new ShippingPreferenceResponse();
		try {
			ShippingPreferenceVo shippingPreferenceVo = shippingPreferenceService.deleteShippingPreference(id,status,userId,roleName);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((ShippingPreferenceResponse) response).setData(shippingPreferenceVo);
			if(status.equals(CommonConstants.STATUS_AS_ACTIVE)) {
				response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_SHIPPING_PREFERENCES_ACTIVATED,
						Constants.SUCCESS_SHIPPING_PREFERENCES_ACTIVATED, Constants.SHIPPING_PREFERENCES_DELETED_SUCCESSFULLY);
			}else if(status.equals(CommonConstants.STATUS_AS_INACTIVE)) {
				response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_SHIPPING_PREFERENCES_DEACTIVATED,
						Constants.SUCCESS_SHIPPING_PREFERENCES_DEACTIVATED, Constants.SHIPPING_PREFERENCES_DELETED_SUCCESSFULLY);
			}
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_SHIPPING_PREFERENCES_DELETED,
					e.getMessage(), Constants.SHIPPING_PREFERENCE_DELETED_UNSUCCESSFULLY);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}



	// For Update Payment Terms

	@RequestMapping(value = "/v1/shippingPreferences", method = RequestMethod.PUT)
	public ResponseEntity<BaseResponse> updateShippingPreference(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @RequestBody JSONObject<ShippingPreferenceRequest> shippingPreferenceRequest) {
		logger.info("Entry into method:updateShippingPreference");
		BaseResponse response = new ShippingPreferenceResponse();
		try {
			logger.info("Request Payload:" + generateRequestAndResponseLogPaylod(shippingPreferenceRequest));
			ShippingPreferenceVo shippingPreferenceVo = SettingsConvertToVoHelper.getInstance()
					.convertShippingPreferenceVoFromShippingPreferenceRequest(shippingPreferenceRequest.getData());
			shippingPreferenceVo= shippingPreferenceService.updateShippingPreference(shippingPreferenceVo);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((ShippingPreferenceResponse) response).setData(shippingPreferenceVo);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_SHIPPING_PREFERENCES_UPDATED,
					Constants.SUCCESS_SHIPPING_PREFERENCES_UPDATED, Constants.SHIPPING_PREFERENCES_UPDATED_SUCCESSFULLY);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_SHIPPING_PREFERENCES_UPDATED,
					e.getMessage(), Constants.SHIPPING_PREFERENCE_UPDATED_UNSUCCESSFULLY);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}



}

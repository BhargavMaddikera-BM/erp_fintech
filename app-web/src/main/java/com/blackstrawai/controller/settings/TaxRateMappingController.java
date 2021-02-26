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
import com.blackstrawai.request.settings.TaxRateMappingRequest;
import com.blackstrawai.response.settings.ListTaxRateMappingResponse;
import com.blackstrawai.response.settings.ListTaxRateTypeResponse;
import com.blackstrawai.response.settings.TaxRateMappingResponse;
import com.blackstrawai.response.settings.TaxRateVariantsResponse;
import com.blackstrawai.settings.TaxRateMappingService;
import com.blackstrawai.settings.TaxRateMappingVo;
import com.blackstrawai.settings.TaxRateTypeService;
import com.blackstrawai.settings.TaxRateTypeVo;
import com.blackstrawai.settings.TaxRateVariantsVo;

@RestController
@CrossOrigin
//This will be part of Settings and Preferences. Hence sp.
@RequestMapping("/decifer/sp/tax_rate")
public class TaxRateMappingController extends BaseController {

	@Autowired
	TaxRateMappingService taxRateService;

	@Autowired
	TaxRateTypeService taxRateTypeService;

	private Logger logger = Logger.getLogger(TaxRateMappingController.class);

	// For Creating Tax rate
	@RequestMapping(value = "/v1/tax_rates", method = RequestMethod.POST)
	public ResponseEntity<BaseResponse> createTaxRate(HttpServletRequest httpRequest, HttpServletResponse httpResponse,
			@RequestBody JSONObject<TaxRateMappingRequest> taxRateRequest) {
		logger.info("Entry into method: createTaxRate");
		BaseResponse response = new TaxRateMappingResponse();
		try {
			logger.info("Request Payload:" + generateRequestAndResponseLogPaylod(taxRateRequest));
			TaxRateMappingVo taxRateMappingVo= SettingsConvertToVoHelper.getInstance()
					.convertTaxRateMappingVoFromTaxRateMappingRequest(taxRateRequest.getData());
			taxRateMappingVo= taxRateService.createTaxRate(taxRateMappingVo);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			//taxRateMappingVo.setKeyToken(null);
			//taxRateMappingVo.setValueToken(null);
			//((TaxRateMappingResponse) response).setData(taxRateMappingVo);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_TAX_RATE_CREATED,
					Constants.SUCCESS_TAX_RATE_CREATED, Constants.TAX_RATE_CREATED_SUCCESSFULLY);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);

		} catch (ApplicationException e) {
			if(e.getCause()!=null){
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_TAX_RATE_CREATED,
						e.getCause().getMessage(), Constants.TAX_RATE_CREATED_UNSUCCESSFULLY);
			}else{
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_TAX_RATE_CREATED,
						e.getMessage(), Constants.TAX_RATE_CREATED_UNSUCCESSFULLY);
			}			
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/*// For Fetching all Tax Rate details
	@RequestMapping(value = "/v1/tax_rates/{organizationId}/{organizationName}", method = RequestMethod.GET)
	public ResponseEntity<BaseResponse> getAllTaxRatesOfAnOrganization(HttpServletRequest httpRequest,HttpServletResponse httpResponse,
			@PathVariable int organizationId,@PathVariable String organizationName) {
		logger.info("Entry into method:getAllTaxRatesOfAnOrganization");
		BaseResponse response = new ListTaxRateMappingResponse();
		try {
			List<TaxRateMappingVo> listAllTaxRates = taxRateService.getAllTaxRatesOfAnOrganization(organizationId);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((ListTaxRateMappingResponse) response).setData(listAllTaxRates);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_TAX_RATE_FETCH,
					Constants.SUCCESS_TAX_RATE_FETCH, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_TAX_RATE_FETCH,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}*/


	// For Fetching all Tax Rate details
	@RequestMapping(value = "/v1/tax_rates/{organizationId}/{organizationName}/{userId}/{roleName}", method = RequestMethod.GET)
	public ResponseEntity<BaseResponse> getAllTaxRatesOfAnOrganizationByUserAndRole(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @PathVariable int organizationId,@PathVariable String organizationName,
			@PathVariable String userId,@PathVariable String roleName) {
		logger.info("Entry into getAllTaxRatesOfAnOrganizationByUserAndRole");
		BaseResponse response = new ListTaxRateMappingResponse();
		try {
			List<TaxRateMappingVo> listAllTaxRates = taxRateService.getAllTaxRatesOfAnOrganizationByUserAndRole(organizationId,userId,roleName);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((ListTaxRateMappingResponse) response).setData(listAllTaxRates);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_TAX_RATE_FETCH,
					Constants.SUCCESS_TAX_RATE_FETCH, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_TAX_RATE_FETCH,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}


	// For activateOrDeactivate TaxRate

	@RequestMapping(value = "/v1/tax_rates/{organizationId}/{organizationName}/{userId}/{id}/{roleName}/{status}", method = RequestMethod.DELETE)
	public ResponseEntity<BaseResponse> deleteTaxRate(HttpServletRequest httpRequest, 
			HttpServletResponse httpResponse,
			@PathVariable int organizationId, 
			@PathVariable String organizationName,
			@PathVariable String userId, 
			@PathVariable int id,
			@PathVariable String roleName,
			@PathVariable String status) {
		logger.info("Entry into deleteTaxRate");
		BaseResponse response = new TaxRateMappingResponse();
		try {
			TaxRateMappingVo taxRateMappingVo= taxRateService.deleteTaxRate(id, status,userId,roleName);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			//			taxRateMappingVo.setKeyToken(null);
			//			taxRateMappingVo.setValueToken(null);
			//((TaxRateMappingResponse) response).setData(taxRateMappingVo);
			if(status.equals(CommonConstants.STATUS_AS_ACTIVE)) {
				response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_TAX_RATE_ACTIVATED,
						Constants.SUCCESS_TAX_RATE_ACTIVATED, Constants.SUCCESS_TAX_RATE_DELETED);
				logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			}else if(status.equals(CommonConstants.STATUS_AS_INACTIVE)) {
				response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_TAX_RATE_DEACTIVATED,
						Constants.SUCCESS_TAX_RATE_DEACTIVATED, Constants.SUCCESS_TAX_RATE_DELETED);
				logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			}
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_TAX_RATE_DELETED,
					e.getMessage(), Constants.TAX_RATE_DELETED_UNSUCCESSFULLY);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@RequestMapping(value = "/v1/tax_rates", method = RequestMethod.PUT)
	public ResponseEntity<BaseResponse> updateTaxRate(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @RequestBody JSONObject<TaxRateMappingRequest> taxRateRequest) {
		logger.info("Entry into method:updateTaxRate");
		BaseResponse response = new TaxRateMappingResponse();
		try {
			logger.info("Request Payload:" + generateRequestAndResponseLogPaylod(taxRateRequest));
			TaxRateMappingVo taxRateMappingVo= SettingsConvertToVoHelper.getInstance()
					.convertTaxRateMappingVoFromTaxRateMappingRequest(taxRateRequest.getData());
			taxRateMappingVo= taxRateService.updateTaxRate(taxRateMappingVo);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			/*
			 * taxRateMappingVo.setKeyToken(null); taxRateMappingVo.setValueToken(null);
			 * ((TaxRateMappingResponse) response).setData(taxRateMappingVo);
			 */
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_TAX_RATE_UPDATED,
					Constants.SUCCESS_TAX_RATE_UPDATED, Constants.TAX_RATE_UPDATED_SUCCESSFULLY);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_TAX_RATE_UPDATED,
					e.getMessage(), Constants.TAX_RATE_UPDATED_UNSUCCESSFULLY);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// For Fetching TaxRate by Id
	@RequestMapping(value = "/v1/tax_rates/{organizationId}/{organizationName}/{userId}/{roleName}/{id}", method = RequestMethod.GET)
	public ResponseEntity<BaseResponse> getTaxRateById(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, 
			@PathVariable int organizationId, 
			@PathVariable String organizationName,
			@PathVariable String userId,
			@PathVariable String roleName,
			@PathVariable int id) {
		logger.info("Entry into method:getTaxRateById");
		BaseResponse response = new TaxRateMappingResponse();
		try {
			TaxRateMappingVo paymentTermsVo = taxRateService.getTaxRateMappingById(id);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((TaxRateMappingResponse) response).setData(paymentTermsVo);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_TAX_RATE_FETCH,
					Constants.SUCCESS_TAX_RATE_FETCH, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_TAX_RATE_FETCH,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/v1/dropsdowns/tax_rates/{organizationId}", method = RequestMethod.GET)
	public ResponseEntity<BaseResponse> getTaxRatesDropDown(HttpServletRequest httpRequest,HttpServletResponse httpResponse,@PathVariable int organizationId) {
		logger.info("Entry into method:getAllTaxGroupsOfAnOrganization");
		BaseResponse response = new ListTaxRateTypeResponse();
		try {
			List<TaxRateTypeVo> listAllTaxRateTypes = taxRateTypeService.getTaxRateDropDownData(organizationId);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((ListTaxRateTypeResponse) response).setData(listAllTaxRateTypes);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_DROP_DOWN_FETCH,
					Constants.SUCCESS_DROP_DOWN_FETCH, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_DROP_DOWN_FETCH,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}


	@RequestMapping(value = "/v1/tax_rates/variants/{organizationId}/{organizationName}", method = RequestMethod.GET)
	public ResponseEntity<BaseResponse> getTaxRateVariantsForOrganization(HttpServletRequest httpRequest,HttpServletResponse httpResponse,
			@PathVariable int organizationId,@PathVariable String organizationName) {

		logger.info("Entry into method:getTaxRateVariantsForOrganization");
		BaseResponse response = new TaxRateVariantsResponse();
		try {
			TaxRateVariantsVo taxVariants = taxRateService.getTaxRateVariantsForOrganization(organizationId);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((TaxRateVariantsResponse) response).setData(taxVariants);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_TAX_RATE_FETCH,
					Constants.SUCCESS_TAX_RATE_FETCH, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_TAX_RATE_FETCH,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}

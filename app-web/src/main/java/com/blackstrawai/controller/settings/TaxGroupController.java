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
import com.blackstrawai.request.settings.TaxGroupRequest;
import com.blackstrawai.response.settings.ListTaxGroupResponse;
import com.blackstrawai.response.settings.TaxGroupResponse;
import com.blackstrawai.settings.TaxGroupService;
import com.blackstrawai.settings.TaxGroupVo;

@RestController
@CrossOrigin
//This will be part of Settings and Preferences. Hence sp.
@RequestMapping("/decifer/sp/tax_group")
public class TaxGroupController extends BaseController {

	@Autowired
	TaxGroupService taxGroupService;

	private Logger logger = Logger.getLogger(TaxGroupController.class);

	// For Creating Tax rate
	@RequestMapping(value = "/v1/tax_groups", method = RequestMethod.POST)
	public ResponseEntity<BaseResponse> createTaxGroup(HttpServletRequest httpRequest, HttpServletResponse httpResponse,
			@RequestBody JSONObject<TaxGroupRequest> taxGroupRequest) {
		logger.info("Entry into method: createTaxGroup");
		BaseResponse response = new TaxGroupResponse();
		try {
			logger.info("Request Payload:" + generateRequestAndResponseLogPaylod(taxGroupRequest));
			TaxGroupVo taxGroupVo= SettingsConvertToVoHelper.getInstance()
					.convertTaxGroupVoFromTaxGroupRequest(taxGroupRequest.getData());
			taxGroupVo= taxGroupService.createTaxGroup(taxGroupVo);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			/*
			 * taxGroupVo.setKeyToken(null); taxGroupVo.setValueToken(null);
			 * ((TaxGroupResponse) response).setData(taxGroupVo);
			 */
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_TAX_GROUP_CREATED,
					Constants.SUCCESS_TAX_GROUP_CREATED, Constants.TAX_GROUP_CREATED_SUCCESSFULLY);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);

		} catch (ApplicationException e) {
			if(e.getCause()!=null){
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_TAX_GROUP_CREATED,
						e.getCause().getMessage(), Constants.TAX_GROUP_CREATED_UNSUCCESSFULLY);
			}else{
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_TAX_GROUP_CREATED,
						e.getMessage(), Constants.TAX_GROUP_CREATED_UNSUCCESSFULLY);
			}			
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/*// For Fetching all Tax Rate details
	@RequestMapping(value = "/v1/tax_groups/{organizationId}/{organizationName}", method = RequestMethod.GET)
	public ResponseEntity<BaseResponse> getAllTaxGroupsOfAnOrganization(HttpServletRequest httpRequest,HttpServletResponse httpResponse,
			@PathVariable int organizationId,@PathVariable String organizationName) {
		logger.info("Entry into method:getAllTaxGroupsOfAnOrganization");
		BaseResponse response = new ListTaxGroupResponse();
		try {
			List<TaxGroupVo> listAllTaxGroups = taxGroupService.getAllTaxGroupsOfAnOrganization(organizationId);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((ListTaxGroupResponse) response).setData(listAllTaxGroups);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_TAX_GROUP_FETCH,
					Constants.SUCCESS_TAX_GROUP_FETCH, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_TAX_GROUP_FETCH,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}*/



	// For Fetching all Tax Rate details
	@RequestMapping(value = "/v1/tax_groups/{organizationId}/{organizationName}/{userId}/{roleName}", method = RequestMethod.GET)
	public ResponseEntity<BaseResponse> getAllTaxGroupsOfAnOrganizationByUserAndRole(
			HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @PathVariable int organizationId,@PathVariable String organizationName,
			@PathVariable String userId,@PathVariable String roleName) {
		logger.info("Entry into method:getAllPaymentTermsOfAnOrganizationForUserAndRole");
		BaseResponse response = new ListTaxGroupResponse();
		try {
			List<TaxGroupVo> listAllTaxGroups = taxGroupService.getAllTaxGroupsOfAnOrganizationByUserAndRole(organizationId,userId,roleName);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((ListTaxGroupResponse) response).setData(listAllTaxGroups);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_TAX_GROUP_FETCH,
					Constants.SUCCESS_TAX_GROUP_FETCH, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_TAX_GROUP_FETCH,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}


	// For activateOrDeactivate TaxGroup

	@RequestMapping(value = "/v1/tax_groups/{organizationId}/{organizationName}/{userId}/{id}/{roleName}/{status}", method = RequestMethod.DELETE)
	public ResponseEntity<BaseResponse> deleteTaxGroup(HttpServletRequest httpRequest, 
			HttpServletResponse httpResponse,
			@PathVariable int organizationId, 
			@PathVariable String organizationName,
			@PathVariable String userId, 
			@PathVariable int id,
			@PathVariable String roleName,
			@PathVariable String status) {
		logger.info("Entry into method:deleteTaxGroup");
		BaseResponse response = new TaxGroupResponse();
		try {
			TaxGroupVo taxGroupVo= taxGroupService.deleteTaxGroup(id, status,userId,roleName);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			//			taxGroupVo.setKeyToken(null);
			//			taxGroupVo.setValueToken(null);
			//((TaxGroupResponse) response).setData(taxGroupVo);
			if(status.equals(CommonConstants.STATUS_AS_ACTIVE)) {
				response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_TAX_GROUP_ACTIVATED,
						Constants.SUCCESS_TAX_GROUP_ACTIVATED, Constants.SUCCESS_TAX_GROUP_DELETED);
				logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			}else if(status.equals(CommonConstants.STATUS_AS_INACTIVE)) {
				response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_TAX_GROUP_DEACTIVATED,
						Constants.SUCCESS_TAX_GROUP_DEACTIVATED, Constants.SUCCESS_TAX_GROUP_DELETED);
				logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			}
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_TAX_GROUP_DELETED,
					e.getMessage(), Constants.TAX_GROUP_DELETED_UNSUCCESSFULLY);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	// For Update Payment Terms

	@RequestMapping(value = "/v1/tax_groups", method = RequestMethod.PUT)
	public ResponseEntity<BaseResponse> updateTaxGroup(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @RequestBody JSONObject<TaxGroupRequest> taxGroupRequest) {
		logger.info("Entry into method:updateTaxGroup");
		BaseResponse response = new TaxGroupResponse();
		try {
			logger.info("Request Payload:" + generateRequestAndResponseLogPaylod(taxGroupRequest));
			TaxGroupVo taxGroupVo= SettingsConvertToVoHelper.getInstance()
					.convertTaxGroupVoFromTaxGroupRequest(taxGroupRequest.getData());
			taxGroupVo= taxGroupService.updateTaxGroup(taxGroupVo);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			/*
			 * taxGroupVo.setKeyToken(null); taxGroupVo.setValueToken(null);
			 * ((TaxGroupResponse) response).setData(taxGroupVo);
			 */
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_TAX_GROUP_UPDATED,
					Constants.SUCCESS_TAX_GROUP_UPDATED, Constants.TAX_GROUP_UPDATED_SUCCESSFULLY);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_TAX_GROUP_UPDATED,
					e.getMessage(), Constants.TAX_GROUP_UPDATED_UNSUCCESSFULLY);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// For Fetching TaxGroup by Id
	@RequestMapping(value = "/v1/tax_groups/{organizationId}/{organizationName}/{userId}/{roleName}/{id}", method = RequestMethod.GET)
	public ResponseEntity<BaseResponse> getTaxGroupById(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, 
			@PathVariable int organizationId, 
			@PathVariable String organizationName,
			@PathVariable String userId,
			@PathVariable String roleName,
			@PathVariable int id) {
		logger.info("Entry into method:getTaxGroupById");
		BaseResponse response = new TaxGroupResponse();
		try {
			TaxGroupVo taxGroupVo = taxGroupService.getTaxGroupById(organizationId,id);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((TaxGroupResponse) response).setData(taxGroupVo);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_TAX_GROUP_FETCH,
					Constants.SUCCESS_TAX_GROUP_FETCH, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_TAX_GROUP_FETCH,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}

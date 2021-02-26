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
import com.blackstrawai.request.settings.VendorGroupRequest;
import com.blackstrawai.response.settings.ListVendorGroupResponse;
import com.blackstrawai.response.settings.VendorGroupResponse;
import com.blackstrawai.settings.VendorGroupService;
import com.blackstrawai.settings.VendorGroupVo;

@RestController
@CrossOrigin
//This will be part of Settings and Preferences. Hence sp.
@RequestMapping("/decifer/sp/vendor_group")
public class VendorGroupController extends BaseController{

	@Autowired
	VendorGroupService vendorGroupService;

	private Logger logger = Logger.getLogger(VendorGroupController.class);


	// For Creating CustomerGroup
	@RequestMapping(value = "/v1/vendorGroups", method = RequestMethod.POST)
	public ResponseEntity<BaseResponse> createVendorGroup(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse,
			@RequestBody JSONObject<VendorGroupRequest> vendorGroupRequest) {
		logger.info("Entry into method: createVendorGroup");
		BaseResponse response = new VendorGroupResponse();
		try {
			logger.info("Request Payload:" + generateRequestAndResponseLogPaylod(vendorGroupRequest));
			VendorGroupVo vendorGroupVo = SettingsConvertToVoHelper.getInstance()
					.convertVendorGroupVoFromVendorGroupRequest(vendorGroupRequest.getData());
			vendorGroupVo = vendorGroupService.createVendorGroup(vendorGroupVo);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((VendorGroupResponse) response).setData(vendorGroupVo);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_VENDOR_GROUP_CREATED,
					Constants.SUCCESS_VENDOR_GROUP_CREATED, Constants.VENDOR_GROUP_CREATED_SUCCESSFULLY);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			if(e.getCause()!=null){
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_VENDOR_GROUP_CREATED,
						e.getCause().getMessage(), Constants.VENDOR_GROUP_CREATED_UNSUCCESSFULLY);
			}else{
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_VENDOR_GROUP_CREATED,
						e.getMessage(), Constants.VENDOR_GROUP_CREATED_UNSUCCESSFULLY);
			}				
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/*	// For Fetching all Customer Group details

			@RequestMapping(value = "/v1/vendorGroups/{organizationId}/{organizationName}", method = RequestMethod.GET)
			public ResponseEntity<BaseResponse> getAllVendorGroupOfAnOrganization(HttpServletRequest httpRequest,
					HttpServletResponse httpResponse, @PathVariable int organizationId,@PathVariable String organizationName) {
				logger.info("Entry into method: getAllVendorGroupOfAnOrganization");
				BaseResponse response = new ListVendorGroupResponse();
				try {
					List<VendorGroupVo> listAllVendorGroup = vendorGroupService.getAllVendorGroupOfAnOrganization(organizationId);
					setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
					((ListVendorGroupResponse) response).setData(listAllVendorGroup);
					response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_VENDOR_GROUP_FETCH,
							Constants.SUCCESS_VENDOR_GROUP_FETCH, Constants.SUCCESS_DURING_GET);
					logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
					return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
				} catch (ApplicationException e) {
					response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_VENDOR_GROUP_FETCH,
							e.getMessage(), Constants.FAILURE_DURING_GET);
					logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
					logger.info(response);
					return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
				}
			}*/

	@RequestMapping(value = "/v1/vendorGroups/{organizationId}/{organizationName}/{userId}/{roleName}", method = RequestMethod.GET)
	public ResponseEntity<BaseResponse> getAllVendorGroupOfAnOrganizationForUserAndRole(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @PathVariable int organizationId,@PathVariable String organizationName,
			@PathVariable String userId,@PathVariable String roleName) {
		logger.info("Entry into method: getAllVendorGroupOfAnOrganizationForUserAndRole");
		BaseResponse response = new ListVendorGroupResponse();
		try {
			List<VendorGroupVo> listAllVendorGroup = vendorGroupService.getAllVendorGroupOfAnOrganizationForUserAndRole(organizationId,userId,roleName);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((ListVendorGroupResponse) response).setData(listAllVendorGroup);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_VENDOR_GROUP_FETCH,
					Constants.SUCCESS_VENDOR_GROUP_FETCH, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_VENDOR_GROUP_FETCH,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// For Fetching Customer Group by Id

	@RequestMapping(value = "/v1/vendorGroups/{organizationId}/{organizationName}/{userId}/{roleName}/{id}", method = RequestMethod.GET)
	public ResponseEntity<BaseResponse> getVendorGroupById(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, 
			@PathVariable int organizationId, 
			@PathVariable String organizationName,
			@PathVariable String userId,
			@PathVariable String roleName,
			@PathVariable int id) {
		logger.info("Entry into method: getVendorGroupById");
		BaseResponse response = new VendorGroupResponse();
		try {
			VendorGroupVo vendorGroupVo = vendorGroupService.getVendorGroupById(id);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((VendorGroupResponse) response).setData(vendorGroupVo);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_VENDOR_GROUP_FETCH,
					Constants.SUCCESS_VENDOR_GROUP_FETCH, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_VENDOR_GROUP_FETCH,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// For Deleting Customer Group


	@RequestMapping(value = "/v1/vendorGroups/{organizationId}/{organizationName}/{userId}/{id}/{roleName}/{status}", method = RequestMethod.DELETE)
	public ResponseEntity<BaseResponse> deleteVendorGroup(HttpServletRequest httpRequest, 
			HttpServletResponse httpResponse,
			@PathVariable int organizationId, 
			@PathVariable String organizationName,
			@PathVariable String userId, 
			@PathVariable int id,
			@PathVariable String roleName,
			@PathVariable String status) {
		logger.info("Entry into method: deleteVendorGroup");
		BaseResponse response = new VendorGroupResponse();
		try {
			VendorGroupVo vendorGroupVo = vendorGroupService.deleteVendorGroup(id,status,userId,roleName);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((VendorGroupResponse) response).setData(vendorGroupVo);
			if(status.equals(CommonConstants.STATUS_AS_ACTIVE)) {
				response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_VENDOR_GROUP_ACTIVATED,
						Constants.SUCCESS_VENDOR_GROUP_ACTIVATED, Constants.VENDOR_GROUP_DELETED_SUCCESSFULLY);
				logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			}else if(status.equals(CommonConstants.STATUS_AS_INACTIVE)) {
				response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_VENDOR_GROUP_DEACTIVATED,
						Constants.SUCCESS_VENDOR_GROUP_DEACTIVATED, Constants.VENDOR_GROUP_DELETED_SUCCESSFULLY);
				logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			}
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_VENDOR_GROUP_DELETED,
					e.getMessage(), Constants.VENDOR_GROUP_DELETED_UNSUCCESSFULLY);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	// For Update Customer Group

	@RequestMapping(value = "/v1/vendorGroups", method = RequestMethod.PUT)
	public ResponseEntity<BaseResponse> updateVendorGroup(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @RequestBody JSONObject<VendorGroupRequest> VendorGroupRequest) {
		logger.info("Entry into method: updateVendorGroup");
		BaseResponse response = new VendorGroupResponse();
		try {
			logger.info("Request Payload:" + generateRequestAndResponseLogPaylod(VendorGroupRequest));
			VendorGroupVo vendorGroupVo = SettingsConvertToVoHelper.getInstance()
					.convertVendorGroupVoFromVendorGroupRequest(VendorGroupRequest.getData());
			vendorGroupVo= vendorGroupService.updateVendorGroup(vendorGroupVo);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((VendorGroupResponse) response).setData(vendorGroupVo);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_VENDOR_GROUP_UPDATED,
					Constants.SUCCESS_VENDOR_GROUP_UPDATED, Constants.VENDOR_GROUP_UPDATED_SUCCESSFULLY);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_VENDOR_GROUP_UPDATED,
					e.getMessage(), Constants.VENDOR_GROUP_UPDATED_UNSUCCESSFULLY);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}

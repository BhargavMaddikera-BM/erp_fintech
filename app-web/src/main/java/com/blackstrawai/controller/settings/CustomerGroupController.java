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
import com.blackstrawai.request.settings.CustomerGroupRequest;
import com.blackstrawai.response.settings.CustomerGroupResponse;
import com.blackstrawai.response.settings.ListCustomerGroupResponse;
import com.blackstrawai.settings.CustomerGroupService;
import com.blackstrawai.settings.CustomerGroupVo;

@RestController
@CrossOrigin
//This will be part of Settings and Preferences. Hence sp.
@RequestMapping("/decifer/sp/customer_group")
public class CustomerGroupController extends BaseController{

	@Autowired
	CustomerGroupService customerGroupService;

	private Logger logger = Logger.getLogger(CustomerGroupController.class);


	// For Creating CustomerGroup
	@RequestMapping(value = "/v1/customerGroups", method = RequestMethod.POST)
	public ResponseEntity<BaseResponse> createCustomerGroup(HttpServletRequest httpRequest,HttpServletResponse httpResponse,
			@RequestBody JSONObject<CustomerGroupRequest> customerGroupRequest) {
		logger.info("Entry into method: createCustomerGroup");
		BaseResponse response = new CustomerGroupResponse();
		try {
			logger.info("Request Payload:" + generateRequestAndResponseLogPaylod(customerGroupRequest));
			CustomerGroupVo customerGroupVo = SettingsConvertToVoHelper.getInstance()
					.convertCustomerGroupVoFromCustomerGroupRequest(customerGroupRequest.getData());
			customerGroupVo = customerGroupService.createCustomerGroup(customerGroupVo);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((CustomerGroupResponse) response).setData(customerGroupVo);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_CUSTOMER_GROUP_CREATED,
					Constants.SUCCESS_CUSTOMER_GROUP_CREATED, Constants.CUSTOMER_GROUP_CREATED_SUCCESSFULLY);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			if(e.getCause()!=null){
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_CUSTOMER_GROUP_CREATED,
						e.getCause().getMessage(), Constants.CUSTOMER_GROUP_CREATED_UNSUCCESSFULLY);
			}else{
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_CUSTOMER_GROUP_CREATED,
						e.getMessage(), Constants.CUSTOMER_GROUP_CREATED_UNSUCCESSFULLY);
			}				
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// For Fetching all Customer Group details

	/*	@RequestMapping(value = "/v1/customerGroups/{organizationId}/{organizationName}", method = RequestMethod.GET)
			public ResponseEntity<BaseResponse> getAllCustomerGroupOfAnOrganization(HttpServletRequest httpRequest,
					HttpServletResponse httpResponse, @PathVariable int organizationId,@PathVariable String organizationName) {
				logger.info("Entry into method: getAllCustomerGroupOfAnOrganization");
				BaseResponse response = new ListCustomerGroupResponse();
				try {
					List<CustomerGroupVo> listAllCustomerGroup = customerGroupService.getAllCustomerGroupOfAnOrganization(organizationId);
					setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
					((ListCustomerGroupResponse) response).setData(listAllCustomerGroup);
					response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_CUSTOMER_GROUP_FETCH,
							Constants.SUCCESS_CUSTOMER_GROUP_FETCH, Constants.SUCCESS_DURING_GET);
					logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
					return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
				} catch (ApplicationException e) {
					response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_CUSTOMER_GROUP_FETCH,
							e.getMessage(), Constants.FAILURE_DURING_GET);
					logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
					logger.info(response);
					return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
				}
			}*/

	@RequestMapping(value = "/v1/customerGroups/{organizationId}/{organizationName}/{userId}/{roleName}", method = RequestMethod.GET)
	public ResponseEntity<BaseResponse> getAllCustomerGroupOfAnOrganizationForUserAndRole(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @PathVariable int organizationId,@PathVariable String organizationName,
			@PathVariable String userId,@PathVariable String roleName) {
		logger.info("Entry into method: getAllCustomerGroupOfAnOrganizationForUserAndRole");
		BaseResponse response = new ListCustomerGroupResponse();
		try {
			List<CustomerGroupVo> listAllCustomerGroup = customerGroupService.getAllCustomerGroupOfAnOrganizationForUserAndRole(organizationId,userId,roleName);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((ListCustomerGroupResponse) response).setData(listAllCustomerGroup);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_CUSTOMER_GROUP_FETCH,
					Constants.SUCCESS_CUSTOMER_GROUP_FETCH, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_CUSTOMER_GROUP_FETCH,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// For Fetching Customer Group by Id

	@RequestMapping(value = "/v1/customerGroups/{organizationId}/{organizationName}/{userId}/{roleName}/{id}", method = RequestMethod.GET)
	public ResponseEntity<BaseResponse> getCustomerGroupById(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, 
			@PathVariable int organizationId, 
			@PathVariable String organizationName,
			@PathVariable String userId,
			@PathVariable String roleName,
			@PathVariable int id) {
		logger.info("Entry into method: getCustomerGroupById");
		BaseResponse response = new CustomerGroupResponse();
		try {
			CustomerGroupVo customerGroupVo = customerGroupService.getCustomerGroupById(id);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((CustomerGroupResponse) response).setData(customerGroupVo);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_CUSTOMER_GROUP_FETCH,
					Constants.SUCCESS_CUSTOMER_GROUP_FETCH, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_CUSTOMER_GROUP_FETCH,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// For Deleting Customer Group


	@RequestMapping(value = "/v1/customerGroups/{organizationId}/{organizationName}/{userId}/{id}/{roleName}/{status}", method = RequestMethod.DELETE)
	public ResponseEntity<BaseResponse> deleteCustomerGroup(HttpServletRequest httpRequest, 
			HttpServletResponse httpResponse,
			@PathVariable int organizationId, 
			@PathVariable String organizationName,
			@PathVariable String userId, 
			@PathVariable int id,
			@PathVariable String roleName,
			@PathVariable String status) {
		logger.info("Entry into method: deleteCustomerGroup");
		BaseResponse response = new CustomerGroupResponse();
		try {
			CustomerGroupVo customerGroupVo = customerGroupService.deleteCustomerGroup(id,status,userId,roleName);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((CustomerGroupResponse) response).setData(customerGroupVo);
			if(status.equals(CommonConstants.STATUS_AS_ACTIVE)) {
				response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_CUSTOMER_GROUP_ACTIVATED,
						Constants.SUCCESS_CUSTOMER_GROUP_ACTIVATED, Constants.CUSTOMER_GROUP_DELETED_SUCCESSFULLY);
				logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			}else if(status.equals(CommonConstants.STATUS_AS_INACTIVE)) {
				response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_CUSTOMER_GROUP_DEACTIVATED,
						Constants.SUCCESS_CUSTOMER_GROUP_DEACTIVATED, Constants.CUSTOMER_GROUP_DELETED_SUCCESSFULLY);
			}
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_CUSTOMER_GROUP_DELETED,
					e.getMessage(), Constants.CUSTOMER_GROUP_DELETED_UNSUCCESSFULLY);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	// For Update Customer Group

	@RequestMapping(value = "/v1/customerGroups", method = RequestMethod.PUT)
	public ResponseEntity<BaseResponse> updateCustomerGroup(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @RequestBody JSONObject<CustomerGroupRequest> customerGroupRequest) {
		logger.info("Entry into method: updateCustomerGroup");
		BaseResponse response = new CustomerGroupResponse();
		try {
			logger.info("Request Payload:" + generateRequestAndResponseLogPaylod(customerGroupRequest));
			CustomerGroupVo customerGroupVo = SettingsConvertToVoHelper.getInstance()
					.convertCustomerGroupVoFromCustomerGroupRequest(customerGroupRequest.getData());
			customerGroupVo= customerGroupService.updateCustomerGroup(customerGroupVo);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((CustomerGroupResponse) response).setData(customerGroupVo);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_CUSTOMER_GROUP_UPDATED,
					Constants.SUCCESS_CUSTOMER_GROUP_UPDATED, Constants.CUSTOMER_GROUP_UPDATED_SUCCESSFULLY);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_CUSTOMER_GROUP_UPDATED,
					e.getMessage(), Constants.CUSTOMER_GROUP_UPDATED_UNSUCCESSFULLY);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}


}

package com.blackstrawai.controller.onboarding;

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

import com.blackstrawai.common.BaseController;
import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.common.CommonConstants;
import com.blackstrawai.common.Constants;
import com.blackstrawai.helper.OnBoardingConvertToVoHelper;
import com.blackstrawai.onboarding.RoleService;
import com.blackstrawai.onboarding.role.RoleVo;
import com.blackstrawai.request.JSONObject;
import com.blackstrawai.request.onboarding.role.RoleRequest;
import com.blackstrawai.response.onboarding.role.ListRoleResponse;
import com.blackstrawai.response.onboarding.role.RoleResponse;


@RestController
@CrossOrigin
public class RoleController extends BaseController {

	@Autowired
	RoleService roleService;	

	private  Logger logger = Logger.getLogger(RoleController.class);

	//For Creating Role
	@RequestMapping(value = "/v1/roles", method = RequestMethod.POST)
	public ResponseEntity<BaseResponse> createRole(HttpServletRequest httpRequest, HttpServletResponse httpResponse,@RequestBody JSONObject<RoleRequest> roleRequest) {		
		logger.info("Entry into method:createRole");	
		BaseResponse response = new RoleResponse();
		try {	
			logger.info("Request Payload:"+generateRequestAndResponseLogPaylod(roleRequest));				
			RoleVo roleVo=OnBoardingConvertToVoHelper.getInstance().convertRoleVoFromRoleRequest(roleRequest.getData());		
			roleVo=roleService.createRole(roleVo);
			setTokens(response,httpRequest.getHeader("keyToken"),httpRequest.getHeader("valueToken"));
			roleVo.setKeyToken(null);
			roleVo.setValueToken(null);
			((RoleResponse) response).setData(roleVo);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_ROLE_CREATED,Constants.SUCCESS_ROLE_CREATED,Constants.ROLE_CREATED_SUCCESSFULLY);
			logger.info("Response Payload:"+generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (Exception e) {
			if(e.getCause()!=null){
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_ROLE_CREATED,e.getCause().getMessage(), Constants.ROLE_CREATED_UNSUCCESSFULLY);
			}else{
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_ROLE_CREATED,e.getMessage(), Constants.ROLE_CREATED_UNSUCCESSFULLY);
			}
			logger.error("Error Payload:"+generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		} 
	}


	//For Updating Role
	@RequestMapping(value = "/v1/roles", method = RequestMethod.PUT)
	public ResponseEntity<BaseResponse> updateRole(HttpServletRequest httpRequest, HttpServletResponse httpResponse,@RequestBody JSONObject<RoleRequest> roleRequest) {
		logger.info("Entry into method:updateRole");	
		BaseResponse response = new RoleResponse();
		try {	
			logger.info("Request Payload:"+generateRequestAndResponseLogPaylod(roleRequest));				
			RoleVo roleVo=OnBoardingConvertToVoHelper.getInstance().convertRoleVoFromRoleRequest(roleRequest.getData());		
			roleVo=roleService.updateRole(roleVo);
			setTokens(response,httpRequest.getHeader("keyToken"),httpRequest.getHeader("valueToken"));
			roleVo.setKeyToken(null);
			roleVo.setValueToken(null);
			((RoleResponse) response).setData(roleVo);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_ROLE_UPDATED,Constants.SUCCESS_ROLE_UPDATED,Constants.ROLE_UPDATED_SUCCESSFULLY);
			logger.info("Response Payload:"+generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (Exception e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_ROLE_UPDATED,e.getMessage(), Constants.ROLE_UPDATED_UNSUCCESSFULLY);
			logger.error("Error Payload:"+generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		} 
	}


	//For Deleting Role
	@RequestMapping(value = "/v1/roles/{organizationId}/{organizationName}/{userId}/{id}/{roleName}/{status}", method = RequestMethod.DELETE)
	public ResponseEntity<BaseResponse> deleteRole(
			HttpServletRequest httpRequest, 
			HttpServletResponse httpResponse,
			@PathVariable int organizationId, 
			@PathVariable String organizationName,
			@PathVariable String userId, 
			@PathVariable int id,
			@PathVariable String roleName,
			@PathVariable String status) {
		logger.info("Entry into method:deleteRole");	
		BaseResponse response = new RoleResponse();
		try {	
			RoleVo roleVo=roleService.deleteRole(id,status,userId,roleName);
			setTokens(response,httpRequest.getHeader("keyToken"),httpRequest.getHeader("valueToken"));
			roleVo.setKeyToken(null);
			roleVo.setValueToken(null);
			((RoleResponse) response).setData(roleVo);			
			if(status.equals(CommonConstants.STATUS_AS_ACTIVE)) {
				response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_ROLE_ACTIVATED,Constants.SUCCESS_ROLE_ACTIVATED,Constants.ROLE_DELETED_SUCCESSFULLY);
			}else if(status.equals(CommonConstants.STATUS_AS_INACTIVE)) {
				response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_ROLE_DEACTIVATED,Constants.SUCCESS_ROLE_DEACTIVATED,Constants.ROLE_DELETED_SUCCESSFULLY);
			}		
			logger.info("Response Payload:"+generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (Exception e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_ROLE_DELETED,e.getMessage(), Constants.ROLE_DELETED_UNSUCCESSFULLY);
			logger.error("Error Payload:"+generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		} 
	}



	//For Fetching all Roles belonging to an Organization
	@RequestMapping(value = "/v1/roles/organizations/{organizationId}/{organizationName}/{userId}/{roleName}")
	public ResponseEntity<BaseResponse> getAllRolesOfAnOrganization(
			HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, 
			@PathVariable int organizationId, 
			@PathVariable String organizationName,
			@PathVariable String userId,
			@PathVariable String roleName){
		logger.info("Entry into method:getAllRolesOfAnOrganization");
		BaseResponse response = new ListRoleResponse();
		try {
			List<RoleVo> listAllRoles = roleService.getAllRolesOfAnOrganization(organizationId,organizationName,userId,roleName );
			setTokens(response,httpRequest.getHeader("keyToken"),httpResponse.getHeader("valueToken"));
			((ListRoleResponse) response).setData(listAllRoles);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_ROLE_FETCH,
					Constants.SUCCESS_ROLE_FETCH, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (Exception e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_ROLE_FETCH, e.getMessage(),
					Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	//For Fetching a particular role
	@RequestMapping(value = "/v1/roles/{organizationId}/{organizationName}/{userId}/{roleName}/{id}")
	public ResponseEntity<BaseResponse> getRoleDetails(
			HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, 
			@PathVariable int organizationId, 
			@PathVariable String organizationName,
			@PathVariable String userId,
			@PathVariable String roleName,
			@PathVariable int id) {
		logger.info("Entry into method:getRoleDetails");
		BaseResponse response = new RoleResponse();
		try {
			RoleVo data = roleService.getRoleDetails(organizationId,id);
			setTokens(response,httpRequest.getHeader("keyToken"),httpRequest.getHeader("valueToken"));
			((RoleResponse) response).setData(data);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_ROLE_FETCH,
					Constants.SUCCESS_ROLE_FETCH, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (Exception e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_ROLE_FETCH, e.getMessage(),
					Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}


	}



}

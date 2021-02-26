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
import com.blackstrawai.request.settings.DepartmentRequest;
import com.blackstrawai.response.settings.DepartmentResponse;
import com.blackstrawai.response.settings.ListDepartmentResponse;
import com.blackstrawai.settings.DepartmentService;
import com.blackstrawai.settings.DepartmentVo;


@RestController
@CrossOrigin
//This will be part of Settings and Preferences. Hence sp.
@RequestMapping("/decifer/sp/department")
public class DepartmentController extends BaseController{

	@Autowired
	DepartmentService departmentService;

	private Logger logger = Logger.getLogger(DepartmentController.class);


	// For Creating Department
	@RequestMapping(value = "/v1/departments", method = RequestMethod.POST)
	public ResponseEntity<BaseResponse> createDepartment(HttpServletRequest httpRequest,HttpServletResponse httpResponse,
			@RequestBody JSONObject<DepartmentRequest> departmentRequest) {
		logger.info("Entry into method: createDepartment");
		BaseResponse response = new DepartmentResponse();
		try {
			logger.info("Request Payload:" + generateRequestAndResponseLogPaylod(departmentRequest));
			DepartmentVo departmentVo = SettingsConvertToVoHelper.getInstance()
					.convertDepartmentVoFromDepartmentRequest(departmentRequest.getData());
			departmentVo = departmentService.createDepartment(departmentVo);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((DepartmentResponse) response).setData(departmentVo);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_DEPARTMENT_CREATED,
					Constants.SUCCESS_DEPARTMENT_CREATED, Constants.DEPARTMENT_CREATED_SUCCESSFULLY);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			if(e.getCause()!=null){
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_DEPARTMENT_CREATED,
						e.getCause().getMessage(), Constants.DEPARTMENT_CREATED_UNSUCCESSFULLY);
			}else{
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_DEPARTMENT_CREATED,
						e.getMessage(), Constants.DEPARTMENT_CREATED_UNSUCCESSFULLY);
			}				
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// For Fetching all Department details

	/*	@RequestMapping(value = "/v1/departments/{organizationId}/{organizationName}", method = RequestMethod.GET)
			public ResponseEntity<BaseResponse> getAllDepartmentsOfAnOrganization(HttpServletRequest httpRequest,
					HttpServletResponse httpResponse, @PathVariable int organizationId,@PathVariable String organizationName) {
				logger.info("Entry into method: getAllDepartmentsOfAnOrganization");
				BaseResponse response = new ListDepartmentResponse();
				try {
					List<DepartmentVo> ListOfAllDepartment = departmentService.getAllDepartmentsOfAnOrganization(organizationId);
					setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
					((ListDepartmentResponse) response).setData(ListOfAllDepartment);
					response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_DEPARTMENT_FETCH,
							Constants.SUCCESS_DEPARTMENT_FETCH, Constants.SUCCESS_DURING_GET);
					logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
					return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
				} catch (ApplicationException e) {
					response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_DEPARTMENT_FETCH,
							e.getMessage(), Constants.FAILURE_DURING_GET);
					logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
					logger.info(response);
					return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
				}
			}
	 */

	@RequestMapping(value = "/v1/departments/{organizationId}/{organizationName}/{userId}/{roleName}", method = RequestMethod.GET)
	public ResponseEntity<BaseResponse> getAllDepartmentsOfAnOrganizationForUserAndRole(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @PathVariable int organizationId,@PathVariable String organizationName,
			@PathVariable String userId,@PathVariable String roleName) {
		logger.info("Entry into method:getAllDepartmentsOfAnOrganizationForUserAndRole");
		BaseResponse response = new ListDepartmentResponse();
		try {
			List<DepartmentVo> ListOfAllDepartment = departmentService.getAllDepartmentsOfAnOrganizationForUserAndRole(organizationId,userId,roleName);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((ListDepartmentResponse) response).setData(ListOfAllDepartment);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_DEPARTMENT_FETCH,
					Constants.SUCCESS_DEPARTMENT_FETCH, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_DEPARTMENT_FETCH,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// For Fetching Department by Id

	@RequestMapping(value = "/v1/departments/{organizationId}/{organizationName}/{userId}/{roleName}/{id}", method = RequestMethod.GET)
	public ResponseEntity<BaseResponse> getDepartmentById(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, 
			@PathVariable int organizationId, 
			@PathVariable String organizationName,
			@PathVariable String userId,
			@PathVariable String roleName,
			@PathVariable int id) {
		logger.info("Entry into method: getDepartmentById");
		BaseResponse response = new DepartmentResponse();
		try {
			DepartmentVo departmentVo = departmentService.getDepartmentById(id);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((DepartmentResponse) response).setData(departmentVo);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_DEPARTMENT_FETCH,
					Constants.SUCCESS_DEPARTMENT_FETCH, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_DEPARTMENT_FETCH,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// For Deleting Department


	@RequestMapping(value = "/v1/departments/{organizationId}/{organizationName}/{userId}/{id}/{roleName}/{status}", method = RequestMethod.DELETE)
	public ResponseEntity<BaseResponse> deleteDepartment(HttpServletRequest httpRequest, 
			HttpServletResponse httpResponse,
			@PathVariable int organizationId, 
			@PathVariable String organizationName,
			@PathVariable String userId, 
			@PathVariable int id,
			@PathVariable String roleName,
			@PathVariable String status) {
		logger.info("Entry into method: deleteDepartment");
		BaseResponse response = new DepartmentResponse();
		try {
			DepartmentVo departmentVo = departmentService.deleteDepartment(id,status,userId,roleName);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((DepartmentResponse) response).setData(departmentVo);
			if(status.equals(CommonConstants.STATUS_AS_ACTIVE)) {
				response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_DEPARTMENT_ACTIVATED,
						Constants.SUCCESS_DEPARTMENT_ACTIVATED, Constants.DEPARTMENT_DELETED_SUCCESSFULLY);
				logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			}else if(status.equals(CommonConstants.STATUS_AS_INACTIVE)) {
				response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_DEPARTMENT_DEACTIVATED,
						Constants.SUCCESS_DEPARTMENT_DEACTIVATED, Constants.DEPARTMENT_DELETED_SUCCESSFULLY);
				logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			}
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_DEPARTMENT_DELETED,
					e.getMessage(), Constants.DEPARTMENT_DELETED_UNSUCCESSFULLY);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	// For Update Department

	@RequestMapping(value = "/v1/departments", method = RequestMethod.PUT)
	public ResponseEntity<BaseResponse> updateDepartment(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @RequestBody JSONObject<DepartmentRequest> departmentRequest) {
		logger.info("Entry into method: updateDepartment");
		BaseResponse response = new DepartmentResponse();
		try {
			logger.info("Request Payload:" + generateRequestAndResponseLogPaylod(departmentRequest));
			DepartmentVo departmentVo = SettingsConvertToVoHelper.getInstance()
					.convertDepartmentVoFromDepartmentRequest(departmentRequest.getData());
			departmentVo= departmentService.updateDepartment(departmentVo);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((DepartmentResponse) response).setData(departmentVo);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_DEPARTMENT_UPDATED,
					Constants.SUCCESS_DEPARTMENT_UPDATED, Constants.DEPARTMENT_UPDATED_SUCCESSFULLY);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_DEPARTMENT_UPDATED,
					e.getMessage(), Constants.DEPARTMENT_UPDATED_UNSUCCESSFULLY);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}

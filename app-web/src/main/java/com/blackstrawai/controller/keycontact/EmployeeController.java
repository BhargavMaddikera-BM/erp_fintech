package com.blackstrawai.controller.keycontact;

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
import com.blackstrawai.helper.KeyContactConvertToVoHelper;
import com.blackstrawai.keycontact.EmployeeService;
import com.blackstrawai.keycontact.dropdowns.EmployeeDropDownVo;
import com.blackstrawai.keycontact.employee.EmployeeBasicDetailsVo;
import com.blackstrawai.keycontact.employee.EmployeeVo;
import com.blackstrawai.request.JSONObject;
import com.blackstrawai.request.keycontact.employee.EmployeeRequest;
import com.blackstrawai.response.keycontact.employee.EmployeeBasicDetailsResponse;
import com.blackstrawai.response.keycontact.employee.EmployeeDropDownResponse;
import com.blackstrawai.response.keycontact.employee.EmployeeResponse;

@RestController
@CrossOrigin
@RequestMapping("/decifer/ap/employee")
public class EmployeeController extends BaseController{

	@Autowired
	EmployeeService employeeService;
	
	private Logger logger = Logger.getLogger(EmployeeController.class);
	
	
	// For Creating Employee
		@RequestMapping(value = "/v1/employees", method = RequestMethod.POST)
		public ResponseEntity<BaseResponse> createEmployee(HttpServletRequest httpRequest,
				HttpServletResponse httpResponse,
				@RequestBody JSONObject<EmployeeRequest> employeeRequest) {
			logger.info("Entry into method: createEmployee");
			BaseResponse response = new EmployeeResponse();
			try {
				logger.info("Request Payload:" + generateRequestAndResponseLogPaylod(employeeRequest));
				EmployeeVo employeeVo = KeyContactConvertToVoHelper.getInstance()
						.convertEmployeeVoFromEmployeeRequest(employeeRequest.getData());
				employeeService.createEmployee(employeeVo);
				setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
				response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_EMPLOYEE_CREATED,
						Constants.SUCCESS_EMPLOYEE_CREATED, Constants.EMPLOYEE_CREATED_SUCCESSFULLY);
				logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
				return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
			} catch (ApplicationException e) {
				if(e.getCause()!=null){
					response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_EMPLOYEE_CREATED,
							e.getCause().getMessage(), Constants.EMPLOYEE_CREATED_UNSUCCESSFULLY);
				}
				else{
					response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_EMPLOYEE_CREATED,
							e.getMessage(), Constants.EMPLOYEE_CREATED_UNSUCCESSFULLY);
				}				
				logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
				return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}

		@RequestMapping(value = "/v1/employees/{organizationId}/{organizationName}/{userId}/{roleName}", method = RequestMethod.GET)
		public ResponseEntity<BaseResponse> getAllEmployeesOfAnOrganizationForUserAndRole(
				HttpServletRequest httpRequest,
				HttpServletResponse httpResponse, 
				@PathVariable int organizationId, 
				@PathVariable String organizationName,
				@PathVariable String userId,
				@PathVariable String roleName) {
			logger.info("Entry into method: getAllEmployeesOfAnOrganizationForUserAndRole");
			BaseResponse response = new EmployeeBasicDetailsResponse();
			try {
				List<EmployeeBasicDetailsVo> employeeBasicDetailsVo= employeeService.getAllEmployeesOfAnOrganizationForUserAndRole(organizationId,userId,roleName);
				setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
				((EmployeeBasicDetailsResponse) response).setData(employeeBasicDetailsVo);
				response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_EMPLOYEE_FETCH,
						Constants.SUCCESS_EMPLOYEE_FETCH, Constants.SUCCESS_DURING_GET);
				logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
				return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
			} catch (ApplicationException e) {
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_EMPLOYEE_FETCH,
						e.getMessage(), Constants.FAILURE_DURING_GET);
				logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
				logger.info(response);
				return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		
		
		// For Fetching Employee by Id

		@RequestMapping(value = "/v1/employees/{organizationId}/{organizationName}/{userId}/{roleName}/{id}", method = RequestMethod.GET)
		public ResponseEntity<BaseResponse> getEmployeeById(HttpServletRequest httpRequest,
				HttpServletResponse httpResponse, 
				@PathVariable int organizationId, 
				@PathVariable String organizationName,
				@PathVariable String userId,
				@PathVariable String roleName,
				@PathVariable int id) {
			logger.info("Entry into method: getEmployeeById");
			BaseResponse response = new EmployeeResponse();
			try {
				EmployeeVo employeeVo = employeeService.getEmployeeById(id);
				setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
				((EmployeeResponse) response).setData(employeeVo);
				response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_EMPLOYEE_FETCH,
						Constants.SUCCESS_EMPLOYEE_FETCH, Constants.SUCCESS_DURING_GET);
				logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
				return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
			} catch (ApplicationException e) {
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_EMPLOYEE_FETCH,
						e.getMessage(), Constants.FAILURE_DURING_GET);
				logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
				logger.info(response);
				return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}

		  // For Deleting Employee
		
		@RequestMapping(value = "/v1/employees/{organizationId}/{organizationName}/{userId}/{id}/{roleName}/{status}", method = RequestMethod.DELETE)
		public ResponseEntity<BaseResponse> deleteEmployee(
				HttpServletRequest httpRequest, 
				HttpServletResponse httpResponse,
				@PathVariable int organizationId, 
				@PathVariable String organizationName,
				@PathVariable String userId, 
				@PathVariable int id,
				@PathVariable String roleName,
				@PathVariable String status) {
			logger.info("Entry into method: deleteEmployee");
			BaseResponse response = new EmployeeResponse();
			try {
				employeeService.deleteEmployee(id,status,userId,roleName);
				setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
				if(status.equals(CommonConstants.STATUS_AS_ACTIVE)) {
					response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_EMPLOYEE_ACTIVATED,
							Constants.SUCCESS_EMPLOYEE_ACTIVATED, Constants.EMPLOYEE_DELETED_SUCCESSFULLY);
					logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
				}else if(status.equals(CommonConstants.STATUS_AS_INACTIVE)) {
				response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_EMPLOYEE_DEACTIVATED,
						Constants.SUCCESS_EMPLOYEE_DEACTIVATED, Constants.EMPLOYEE_DELETED_SUCCESSFULLY);
				logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
				}
				return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
			} catch (ApplicationException e) {
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_EMPLOYEE_DELETED,
						e.getMessage(), Constants.EMPLOYEE_DELETED_UNSUCCESSFULLY);
				logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
				return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}

		}
	
		
		  // For Update Employee
		  
		@RequestMapping(value = "/v1/employees", method = RequestMethod.PUT)
		public ResponseEntity<BaseResponse> updateEmployee(HttpServletRequest httpRequest,
				HttpServletResponse httpResponse, @RequestBody JSONObject<EmployeeRequest> employeeRequest) {
			logger.info("Entry into method: updateEmployee");
			BaseResponse response = new EmployeeResponse();
			try {
				logger.info("Request Payload:" + generateRequestAndResponseLogPaylod(employeeRequest));
				EmployeeVo employeeVo = KeyContactConvertToVoHelper.getInstance()
						.convertEmployeeVoFromEmployeeRequest(employeeRequest.getData());
				 employeeService.updateEmployee(employeeVo);
				setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
				response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_EMPLOYEE_UPDATED,
						Constants.SUCCESS_EMPLOYEE_UPDATED, Constants.EMPLOYEE_UPDATED_SUCCESSFULLY);
				logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
				return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
			} catch (ApplicationException e) {
				if(e.getCause()!=null){
					response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_EMPLOYEE_UPDATED,
							e.getCause().getMessage(), Constants.EMPLOYEE_UPDATED_UNSUCCESSFULLY);
				}
				else{
					response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_EMPLOYEE_UPDATED,
							e.getMessage(), Constants.EMPLOYEE_UPDATED_UNSUCCESSFULLY);
				}			
				logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
				return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		
		//employee DropDown
		@RequestMapping(value = "/v1/dropsdowns/employees/{organizationId}")
		public ResponseEntity<BaseResponse> getVendorDropDown(HttpServletRequest httpServletRequest,
				HttpServletResponse httpServletResponse, @PathVariable int organizationId) {
			logger.info("Entry into getVendorDropDown");
			BaseResponse response = new EmployeeDropDownResponse();
			try {
				EmployeeDropDownVo data = employeeService.getEmpoyeeDropDownData(organizationId);
				setTokens(response, httpServletRequest.getHeader("keyToken"), httpServletRequest.getHeader("valueToken"));
				((EmployeeDropDownResponse) response).setData(data);
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
		
		
	/*	// For Fetching all Employee details

		@RequestMapping(value = "/v1/employees/{organizationId}/{organizationName}", method = RequestMethod.GET)
		public ResponseEntity<BaseResponse> getAllEmployeesOfAnOrganization(HttpServletRequest httpRequest,
				HttpServletResponse httpResponse, @PathVariable int organizationId,@PathVariable String organizationName) {
			logger.info("Entry into method: getAllEmployeesOfAnOrganization");
			BaseResponse response = new EmployeeBasicDetailsResponse();
			try {
				List<EmployeeBasicDetailsVo> employeeBasicDetailsVo= employeeService.getAllEmployeesOfAnOrganization(organizationId);
				setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
				((EmployeeBasicDetailsResponse) response).setData(employeeBasicDetailsVo);
				response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_EMPLOYEE_FETCH,
						Constants.SUCCESS_EMPLOYEE_FETCH, Constants.SUCCESS_DURING_GET);
				logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
				return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
			} catch (ApplicationException e) {
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_EMPLOYEE_FETCH,
						e.getMessage(), Constants.FAILURE_DURING_GET);
				logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
				logger.info(response);
				return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		*/
}

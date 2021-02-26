package com.blackstrawai.controller.keycontact;

import java.util.ArrayList;
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
import com.blackstrawai.helper.ConvertToResponseHelper;
import com.blackstrawai.helper.KeyContactConvertToVoHelper;
import com.blackstrawai.keycontact.CustomerService;
import com.blackstrawai.keycontact.customer.CustomerVo;
import com.blackstrawai.keycontact.dropdowns.CustomerDropdownVo;
import com.blackstrawai.request.JSONObject;
import com.blackstrawai.request.keycontact.customer.CustomerRequest;
import com.blackstrawai.response.keycontact.customer.CustomerBriefResponse;
import com.blackstrawai.response.keycontact.customer.CustomerDropDownResponse;
import com.blackstrawai.response.keycontact.customer.CustomerResponse;
import com.blackstrawai.response.keycontact.customer.ListCustomerResponse;



@RestController
@CrossOrigin
@RequestMapping("/decifer/ar/customer")
public class CustomerController extends BaseController{

	private Logger logger = Logger.getLogger(CustomerController.class);
	
	@Autowired
	private CustomerService customerService;
	
	
	@RequestMapping(value="/v1/customers" , method = RequestMethod.POST)
	public ResponseEntity<BaseResponse>createCustomer(HttpServletRequest httpRequest, HttpServletResponse httpResponse,@RequestBody JSONObject<CustomerRequest> customerRequest){
		logger.info("Entered Into createCustomer");
		BaseResponse response = new CustomerResponse();
		if(customerRequest.getData().getIsSuperAdmin()!=null && customerRequest.getData().getUserId()!=null && customerRequest.getData().getOrganizationId()!=null) {
		try {
			logger.info("Request Payload:"+generateRequestAndResponseLogPaylod(customerRequest));	
			CustomerVo customerVo = KeyContactConvertToVoHelper.getInstance().convertCustVoFromCustRequest(customerRequest.getData());
			customerService.createCustomer(customerVo);
			setTokens(response,httpRequest.getHeader("keyToken"),httpRequest.getHeader("valueToken"));
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_CUSTOMER_CREATED,Constants.SUCCESS_CUSTOMER_CREATED,Constants.CUSTOMER_CREATED_SUCCESSFULLY);
			logger.info("Response Payload:"+generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		}catch(Exception e) {
			if(e.getCause()!=null){
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_CUSTOMER_CREATED,e.getCause().getMessage(), Constants.CUSTOMER_CREATED_UNSUCCESSFULLY);
			}else{
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_CUSTOMER_CREATED,e.getMessage(), Constants.CUSTOMER_CREATED_UNSUCCESSFULLY);
			}
			logger.error("Error Payload:" , e);
			logger.error("Error Payload:"+generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}	
		}else {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_CUSTOMER_CREATED,Constants.FAILURE_IN_USER_ORGANIZATION_REQUEST_VALIDATION, Constants.CUSTOMER_CREATED_UNSUCCESSFULLY);
			logger.error("Error Payload:"+ generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value="/v1/customers" , method = RequestMethod.PUT)
	public ResponseEntity<BaseResponse>updateCustomer(HttpServletRequest httpRequest, HttpServletResponse httpResponse,@RequestBody JSONObject<CustomerRequest> customerRequest){
		logger.info("Entered Into update Customer");
		BaseResponse response = new CustomerResponse();
		if(customerRequest.getData().getId()!=null && customerRequest.getData().getIsSuperAdmin()!=null && customerRequest.getData().getUserId()!=null && customerRequest.getData().getOrganizationId()!=null) {
		try {
			logger.info("Request Payload:"+generateRequestAndResponseLogPaylod(customerRequest));	
			CustomerVo customerVo= KeyContactConvertToVoHelper.getInstance().convertCustVoFromCustRequest(customerRequest.getData());
			customerService.updateCustomer(customerVo);
			setTokens(response,httpRequest.getHeader("keyToken"),httpRequest.getHeader("valueToken"));
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_CUSTOMER_UPDATED,Constants.SUCCESS_CUSTOMER_UPDATED,Constants.CUSTOMER_UPDATED_SUCCESSFULLY);
			logger.info("Response Payload:"+generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		}catch(Exception e) {
			if(e.getCause()!=null){
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_CUSTOMER_UPDATED,e.getCause().getMessage(), Constants.CUSTOMER_UPDATED_UNSUCCESSFULLY);
			}else{
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_CUSTOMER_UPDATED,e.getMessage(), Constants.CUSTOMER_UPDATED_UNSUCCESSFULLY);
			}
			logger.error("Error Payload:"+generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}	
		}else {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_CUSTOMER_UPDATED,Constants.FAILURE_IN_USER_ORGANIZATION_REQUEST_VALIDATION, Constants.CUSTOMER_UPDATED_UNSUCCESSFULLY);
			logger.error("Error Payload:"+ generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
		
	@RequestMapping(value = "/v1/customers/{organizationId}/{organizationName}/{userId}/{roleName}/{id}")
	public ResponseEntity<BaseResponse> getCustomer(
			HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, 
			@PathVariable int organizationId, 
			@PathVariable String organizationName,
			@PathVariable String userId,
			@PathVariable String roleName,
			@PathVariable int id) {
		logger.info("Entry into method:getCustomer");
		BaseResponse response = new CustomerResponse();
		try {
			CustomerVo customerVo = customerService.getCustomerById(id);
			setTokens(response,httpRequest.getHeader("keyToken"),httpRequest.getHeader("valueToken"));
			((CustomerResponse) response).setData(customerVo);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_CUSTOMER_FETCH,
					Constants.SUCCESS_CUSTOMER_FETCH, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (Exception e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_CUSTOMER_FETCH, e.getMessage(),
					Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/v1/customers/{organizationId}/{organizationName}/{userId}/{roleName}")
	public ResponseEntity<BaseResponse> getAllCustomersForUserAndRole(
			HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, 
			@PathVariable Integer organizationId, 
			@PathVariable String organizationName,
			@PathVariable String userId,
			@PathVariable String roleName) {
		logger.info("Entry into getAllCustomersForUserAndRole");
		BaseResponse response = new ListCustomerResponse();
		try {
			if( organizationId!= null) {
				List<CustomerVo> customerList = customerService.getAllCustomerListForUserAndRole(organizationId,userId,roleName);
				List<CustomerBriefResponse> briefCustomerList = new ArrayList<CustomerBriefResponse>();
				customerList.forEach(customer->{
					briefCustomerList.add(ConvertToResponseHelper.getInstance().convertCustVoToCustBriefResponse(customer));
				});
				logger.info("listSize"+briefCustomerList.size());
				setTokens(response,httpRequest.getHeader("keyToken"),httpRequest.getHeader("valueToken"));
				((ListCustomerResponse) response).setData(briefCustomerList.size()>0? briefCustomerList : null);
				response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_CUSTOMER_FETCH,
						Constants.SUCCESS_CUSTOMER_FETCH, Constants.SUCCESS_DURING_GET);
				logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
				return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
			}else {
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_CUSTOMER_FETCH,Constants.FAILURE_IN_USER_ORGANIZATION_REQUEST_VALIDATION, Constants.FAILURE_DURING_GET);
				logger.error("Error Payload:"+generateRequestAndResponseLogPaylod(response));
				return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch (Exception e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_CUSTOMER_FETCH, e.getMessage(),
					Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	
	
	
	@RequestMapping(value = "/v1/customers/{organizationId}/{organizationName}/{userId}/{id}/{roleName}/{status}", method = RequestMethod.DELETE)
	public ResponseEntity<BaseResponse> modifyCustomerStatus(
			HttpServletRequest httpRequest, 
			HttpServletResponse httpResponse,
			@PathVariable int organizationId, 
			@PathVariable String organizationName,
			@PathVariable String userId, 
			@PathVariable int id,
			@PathVariable String roleName,
			@PathVariable String status) {
		logger.info("Entry into modifyCustomerStatus");	
	
		BaseResponse response = new CustomerResponse();
			try {
				customerService.activateOrDeactivateCustomer(id, status,userId,roleName);
				setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
				if(status.equals(CommonConstants.STATUS_AS_ACTIVE)) {
					response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_CUSTOMER_ACTIVATED,
							Constants.SUCCESS_CUSTOMER_ACTIVATED, Constants.CUSTOMER_DELETED_SUCCESSFULLY);
					logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
				}else if(status.equals(CommonConstants.STATUS_AS_INACTIVE)) {
				response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_CUSTOMER_DEACTIVATED,
						Constants.SUCCESS_CUSTOMER_DEACTIVATED, Constants.CUSTOMER_DELETED_SUCCESSFULLY);
				logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
				}
				return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
			} catch (Exception e) {
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_CUSTOMER_UPDATED, e.getMessage(),
						Constants.CUSTOMER_DELETED_UNSUCCESSFULLY);
				logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
				return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
	}
	
	
	@RequestMapping(value = "/v1/dropsdowns/customers/{organizationId}")
	public ResponseEntity<BaseResponse> getCustomerDropDown(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, @PathVariable int organizationId) {
		logger.info("Entry into getCustomerDropDown");
		BaseResponse response = new CustomerDropDownResponse();
		try {
			CustomerDropdownVo data = customerService.getCustomerDropDownData(organizationId);
			setTokens(response, httpServletRequest.getHeader("keyToken"), httpServletRequest.getHeader("valueToken"));
			((CustomerDropDownResponse) response).setData(data);
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
	
	
	
/*	@RequestMapping(value = "/v1/customers/{organizationId}/{userId}")
	public ResponseEntity<BaseResponse> getAllCustomers(HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse,@PathVariable Integer organizationId,@PathVariable String userId) {
		logger.info("Entry into getAllCustomers");
		BaseResponse response = new ListCustomerResponse();
		try {
			if( organizationId!= null) {
				List<CustomerVo> customerList = customerService.getAllCustomerListForOrg(organizationId);
				List<CustomerBriefResponse> briefCustomerList = new ArrayList<CustomerBriefResponse>();
				customerList.forEach(customer->{
					briefCustomerList.add(ConvertToResponseHelper.getInstance().convertCustVoToCustBriefResponse(customer));
				});
				logger.info("listSixe"+briefCustomerList.size());
				setTokens(response,httpServletRequest.getHeader("keyToken"),httpServletRequest.getHeader("valueToken"));
				((ListCustomerResponse) response).setData(briefCustomerList.size()>0? briefCustomerList : null);
				response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_CUSTOMER_FETCH,
						Constants.SUCCESS_CUSTOMER_FETCH, Constants.SUCCESS_DURING_GET);
				logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
				return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
			}else {
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_CUSTOMER_FETCH,Constants.FAILURE_IN_USER_ORGANIZATION_REQUEST_VALIDATION, Constants.FAILURE_DURING_GET);
				logger.error("Error Payload:"+generateRequestAndResponseLogPaylod(response));
				return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch (Exception e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_CUSTOMER_FETCH, e.getMessage(),
					Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}*/
}

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
import com.blackstrawai.common.CacheService;
import com.blackstrawai.common.CommonConstants;
import com.blackstrawai.common.Constants;
import com.blackstrawai.helper.OnBoardingConvertToVoHelper;
import com.blackstrawai.onboarding.OrganizationService;
import com.blackstrawai.onboarding.organization.BasicOrganizationVo;
import com.blackstrawai.onboarding.organization.IncomeTaxLoginVo;
import com.blackstrawai.onboarding.organization.MinimalOrganizationVo;
import com.blackstrawai.onboarding.organization.NewOrganizationVo;
import com.blackstrawai.onboarding.organization.OrganizationDropDownVo;
import com.blackstrawai.onboarding.organization.UserTypeOrganizationVo;
import com.blackstrawai.request.JSONObject;
import com.blackstrawai.request.onboarding.organization.MinimalOrganizationRequest;
import com.blackstrawai.request.onboarding.organization.NewOrganizationRequest;
import com.blackstrawai.response.onboarding.organization.IncomeTaxLoginResponse;
import com.blackstrawai.response.onboarding.organization.ListBasicOrganizationResponse;
import com.blackstrawai.response.onboarding.organization.ListUserTypeOrganizationResponse;
import com.blackstrawai.response.onboarding.organization.MinimalOrganizationResponse;
import com.blackstrawai.response.onboarding.organization.NewMinimalOrganizationResponse;
import com.blackstrawai.response.onboarding.organization.NewOrganizationResponse;
import com.blackstrawai.response.onboarding.organization.OrganizationDropDownResponse;
import com.blackstrawai.response.onboarding.organization.OrganizationResponse;

@RestController
@CrossOrigin
public class OrganizationController extends BaseController{

	@Autowired
	OrganizationService organizationService;

	@Autowired
	CacheService cacheService;



	private  Logger logger = Logger.getLogger(OrganizationController.class);	

	/*//For Creating Organization
	@RequestMapping(value = "/v1/organizations", method = RequestMethod.POST)
	public ResponseEntity<BaseResponse> createOrganization(HttpServletRequest httpRequest, HttpServletResponse httpResponse,@RequestBody JSONObject<OrganizationRequest> organizationRequest) {
		logger.info("Entry into method:createOrganization");	
		BaseResponse response = new OrganizationResponse();
		try {	
			logger.info("Request Payload:"+generateRequestAndResponseLogPaylod(organizationRequest));	
			System.out.println("Payload is ***********************"+generateRequestAndResponseLogPaylod(organizationRequest) );
			OrganizationVo organizationVo=ConvertToVoHelper.getInstance().convertOrganizationVoFromOrganizationRequest(organizationRequest.getData());		
			organizationVo=organizationService.createOrganization(organizationVo);
			setTokens(response,httpRequest.getHeader("keyToken"),httpRequest.getHeader("valueToken"));
			organizationVo.setKeyToken(null);
			organizationVo.setValueToken(null);
			((OrganizationResponse) response).setData(organizationVo);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_ORGANIZATION_CREATED,Constants.SUCCESS_ORGANIZATION_CREATED,Constants.ORGANIZATION_CREATED_SUCCESSFULLY);
			logger.info("Response Payload:"+generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (Exception e) {

			if(e.getCause()!=null){
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_ORGANIZATION_CREATED,e.getCause().getMessage(), Constants.ORGANIZATION_CREATED_UNSUCCESSFULLY);
			}else{
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_ORGANIZATION_CREATED,e.getMessage(), Constants.ORGANIZATION_CREATED_UNSUCCESSFULLY);
			}
			logger.error("Error Payload:"+generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		} 		

	}
	 */

	//For Updating Organization
	@RequestMapping(value = "/v1/organizations", method = RequestMethod.PUT)
	public ResponseEntity<BaseResponse> updateOrganization(HttpServletRequest httpRequest, HttpServletResponse httpResponse,@RequestBody JSONObject<NewOrganizationRequest> organizationRequest) {
		logger.info("Entry into method:updateOrganization");	
		BaseResponse response = new NewOrganizationResponse();
		try {	
			logger.info("Request Payload:"+generateRequestAndResponseLogPaylod(organizationRequest));	
			System.out.println("Payload is ***********************"+generateRequestAndResponseLogPaylod(organizationRequest) );
			NewOrganizationVo organizationVo=OnBoardingConvertToVoHelper.getInstance().convertOrganizationVoFromOrganizationRequest(organizationRequest.getData());		
			BasicOrganizationVo basicOrganizationVo=organizationService.updateOrganization(organizationVo);
			setTokens(response,httpRequest.getHeader("keyToken"),httpRequest.getHeader("valueToken"));
			organizationVo.setKeyToken(null);
			organizationVo.setValueToken(null);
			((NewOrganizationResponse) response).setData(basicOrganizationVo);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_ORGANIZATION_UPDATED,Constants.SUCCESS_ORGANIZATION_UPDATED,Constants.ORGANIZATION_UPDATED_SUCCESSFULLY);
			logger.info("Response Payload:"+generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (Exception e) {

			if(e.getCause()!=null){
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_ORGANIZATION_UPDATED,e.getCause().getMessage(), Constants.ORGANIZATION_UPDATED_UNSUCCESSFULLY);
			}else{
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_ORGANIZATION_UPDATED,e.getMessage(), Constants.ORGANIZATION_UPDATED_UNSUCCESSFULLY);
			}
			logger.error("Error Payload:"+generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		} 		

	}	


	//For Updating Organization
	@RequestMapping(value = "/v1/organizations/minimal", method = RequestMethod.PUT)
	public ResponseEntity<BaseResponse> updateMinimalOrganization(HttpServletRequest httpRequest, HttpServletResponse httpResponse,@RequestBody JSONObject<MinimalOrganizationRequest> organizationRequest) {
		logger.info("Entry into method:updateOrganization");	
		BaseResponse response = new MinimalOrganizationResponse();
		try {	
			logger.info("Request Payload:"+generateRequestAndResponseLogPaylod(organizationRequest));	
			System.out.println("Payload is ***********************"+generateRequestAndResponseLogPaylod(organizationRequest) );
			MinimalOrganizationVo organizationVo=OnBoardingConvertToVoHelper.getInstance().convertMinimalOrganizationVoFromMinimalOrganizationRequest(organizationRequest.getData());		
			BasicOrganizationVo basicOrganizationVo=organizationService.updateMinimalOrganization(organizationVo);
			setTokens(response,httpRequest.getHeader("keyToken"),httpRequest.getHeader("valueToken"));
			organizationVo.setKeyToken(null);
			organizationVo.setValueToken(null);
			((MinimalOrganizationResponse) response).setData(basicOrganizationVo);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_ORGANIZATION_UPDATED,Constants.SUCCESS_ORGANIZATION_UPDATED,Constants.ORGANIZATION_UPDATED_SUCCESSFULLY);
			logger.info("Response Payload:"+generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (Exception e) {

			if(e.getCause()!=null){
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_ORGANIZATION_UPDATED,e.getCause().getMessage(), Constants.ORGANIZATION_UPDATED_UNSUCCESSFULLY);
			}else{
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_ORGANIZATION_UPDATED,e.getMessage(), Constants.ORGANIZATION_UPDATED_UNSUCCESSFULLY);
			}
			logger.error("Error Payload:"+generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		} 		

	}	

	//For Deleting Organization
	@RequestMapping(value = "/v1/organizations/{userId}/{id}/{status}", method = RequestMethod.DELETE)
	public ResponseEntity<BaseResponse> deleteOrganization(HttpServletRequest httpRequest, 
			HttpServletResponse httpResponse,
			@PathVariable Integer userId,
			@PathVariable Integer id,
			@PathVariable String status) {
		logger.info("Entry into method:deleteOrganization");	
		BaseResponse response = new NewOrganizationResponse();
		try {	
			organizationService.deleteOrganization(id,status);
			setTokens(response,httpRequest.getHeader("keyToken"),httpRequest.getHeader("valueToken"));			
			if(status.equals(CommonConstants.STATUS_AS_ACTIVE)) {
				response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_ORGANIZATION_ACTIVATED,
						Constants.SUCCESS_ORGANIZATION_ACTIVATED, Constants.CUSTOMER_DELETED_SUCCESSFULLY);
			}else if(status.equals(CommonConstants.STATUS_AS_INACTIVE)) {
				response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_ORGANIZATION_DEACTIVATED,
						Constants.SUCCESS_ORGANIZATION_DEACTIVATED, Constants.CUSTOMER_DELETED_SUCCESSFULLY);			
			}			
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (Exception e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_ORGANIZATION_DELETED,e.getMessage(), Constants.ORGANIZATION_DELETED_UNSUCCESSFULLY);
			logger.error("Error Payload:"+generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		} 
	}


	//For Fetching all Organization details
	@RequestMapping(value = "/v1/organizations/{userId}/{organizationId}")
	public ResponseEntity<BaseResponse> getOrganization(HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse,@PathVariable int userId,@PathVariable int organizationId) {
		logger.info("Entry into method:getOrganization");
		BaseResponse response = new OrganizationResponse();
		try {
			NewOrganizationVo data = organizationService.getOrganization(userId, organizationId);
			setTokens(response,httpServletRequest.getHeader("keyToken"),httpServletRequest.getHeader("valueToken"));
			((OrganizationResponse) response).setData(data);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_ORGANIZATION_FETCH,
					Constants.SUCCESS_ORGANIZATION_FETCH, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (Exception e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_ORGANIZATION_FETCH, e.getMessage(),
					Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}


	//For Fetching all Organization details
	@RequestMapping(value = "/v1/organizations/minimal/{userId}/{organizationId}")
	public ResponseEntity<BaseResponse> getMinimalOrganization(HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse,@PathVariable int userId,@PathVariable int organizationId) {
		logger.info("Entry into method:getOrganization");
		BaseResponse response = new NewMinimalOrganizationResponse();
		try {
			MinimalOrganizationVo data = organizationService.getMinimalOrganization(userId, organizationId);
			setTokens(response,httpServletRequest.getHeader("keyToken"),httpServletRequest.getHeader("valueToken"));
			((NewMinimalOrganizationResponse) response).setData(data);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_ORGANIZATION_FETCH,
					Constants.SUCCESS_ORGANIZATION_FETCH, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (Exception e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_ORGANIZATION_FETCH, e.getMessage(),
					Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	/*//For Fetching all Organization details
		@RequestMapping(value = "/v1/organizations/{userId}")
		public ResponseEntity<BaseResponse> getAllOrganizations(HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse,@PathVariable int userId) {
			logger.info("Entry into method:getAllOrganizations");
			BaseResponse response = new ListOrganizationResponse();
			try {
				 List<OrganizationVo> data = organizationService.getAllOrganizations(userId );
				setTokens(response,httpServletRequest.getHeader("keyToken"),httpServletRequest.getHeader("valueToken"));
				((ListOrganizationResponse) response).setData(data);
				response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_ORGANIZATION_FETCH,
						Constants.SUCCESS_ORGANIZATION_FETCH, Constants.SUCCESS_DURING_GET);
				logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
				return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
			} catch (Exception e) {
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_ORGANIZATION_FETCH, e.getMessage(),
						Constants.FAILURE_DURING_GET);
				logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
				return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}

		}*/


	/*	@RequestMapping(value = "/v1/dropsdowns/organizations")
		public ResponseEntity<BaseResponse> getOrganizationDropDown(HttpServletRequest httpServletRequest,
				HttpServletResponse httpServletResponse) {
			logger.info("Entry into getOrganizationDropDown");
			BaseResponse response = new OrganizationDropDownResponse();
			try {
				OrganizationDropDownVo data = organizationService.getOrganizationDropDownData();
				setTokens(response, httpServletRequest.getHeader("keyToken"), httpServletRequest.getHeader("valueToken"));
				((OrganizationDropDownResponse) response).setData(data);
				response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_DROP_DOWN_FETCH,
						Constants.SUCCESS_DROP_DOWN_FETCH, Constants.SUCCESS_DURING_GET);
				logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
				return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);

			} catch (Exception e) {
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_PO_FETCH, e.getMessage(),
						Constants.FAILURE_DURING_GET);
				logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
				return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}

		}*/

	@RequestMapping(value = "/v1/organizations/{emailId}/{phoneNo}/{type}", method = RequestMethod.GET)
	public ResponseEntity<BaseResponse> getAllOrganizationsByUserEmailAndPhone(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse,@PathVariable String emailId,
			@PathVariable String phoneNo,@PathVariable String type) {
		logger.info("Entry into method:getAllOrganizationsByEmailAndPhone");
		BaseResponse response = new ListUserTypeOrganizationResponse();
		try {
			List<UserTypeOrganizationVo> data = organizationService.getAllOrganizationsByEmailAndPhone(emailId,phoneNo );
			setTokens(response,httpServletRequest.getHeader("keyToken"),httpServletRequest.getHeader("valueToken"));
			((ListUserTypeOrganizationResponse) response).setData(data);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_ORGANIZATION_FETCH,
					Constants.SUCCESS_ORGANIZATION_FETCH, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (Exception e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_ORGANIZATION_FETCH, e.getMessage(),
					Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@RequestMapping(value = "/v1/organizations", method = RequestMethod.POST)
	public ResponseEntity<BaseResponse> createOrganization(HttpServletRequest httpRequest, HttpServletResponse httpResponse,@RequestBody JSONObject<NewOrganizationRequest> organizationRequest) {
		logger.info("Entry into method:createOrganization");	
		BaseResponse response = new NewOrganizationResponse();
		try {	
			logger.info("Request Payload:"+generateRequestAndResponseLogPaylod(organizationRequest));	
			System.out.println("Payload is ***********************"+generateRequestAndResponseLogPaylod(organizationRequest) );
			NewOrganizationVo organizationVo=OnBoardingConvertToVoHelper.getInstance().convertOrganizationVoFromOrganizationRequest(organizationRequest.getData());		
			if(cacheService.getCinNumber(organizationVo.getGeneralInfo().getCinno())!=null){
				organizationVo.setRocData(cacheService.getCinNumber(organizationVo.getGeneralInfo().getCinno()));
			}
			BasicOrganizationVo basicOrganizationVo=organizationService.createOrganization(organizationVo);
			setTokens(response,httpRequest.getHeader("keyToken"),httpRequest.getHeader("valueToken"));
			organizationVo.setKeyToken(null);
			organizationVo.setValueToken(null);
			((NewOrganizationResponse) response).setData(basicOrganizationVo);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_ORGANIZATION_CREATED,Constants.SUCCESS_ORGANIZATION_CREATED,Constants.ORGANIZATION_CREATED_SUCCESSFULLY);
			logger.info("Response Payload:"+generateRequestAndResponseLogPaylod(response));
			cacheService.removeCinNumber(organizationVo.getGeneralInfo().getCinno());
			Thread.sleep(10000);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (Exception e) {

			if(e.getCause()!=null){
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_ORGANIZATION_CREATED,e.getCause().getMessage(), Constants.ORGANIZATION_CREATED_UNSUCCESSFULLY);
			}else{
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_ORGANIZATION_CREATED,e.getMessage(), Constants.ORGANIZATION_CREATED_UNSUCCESSFULLY);
			}
			logger.error("Error Payload:"+generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		} 		

	}


	@RequestMapping(value = "/v1/organizations/minimal", method = RequestMethod.POST)
	public ResponseEntity<BaseResponse> createMinimalOrganization(HttpServletRequest httpRequest, HttpServletResponse httpResponse,@RequestBody JSONObject<MinimalOrganizationRequest> organizationRequest) {
		logger.info("Entry into method:createMinimalOrganization");	
		BaseResponse response = new MinimalOrganizationResponse();
		try {	
			logger.info("Request Payload:"+generateRequestAndResponseLogPaylod(organizationRequest));	
			System.out.println("Payload is ***********************"+generateRequestAndResponseLogPaylod(organizationRequest) );
			MinimalOrganizationVo organizationVo=OnBoardingConvertToVoHelper.getInstance().convertMinimalOrganizationVoFromMinimalOrganizationRequest(organizationRequest.getData());		
			BasicOrganizationVo basicOrganizationVo=organizationService.createMinimalOrganization(organizationVo);
			setTokens(response,httpRequest.getHeader("keyToken"),httpRequest.getHeader("valueToken"));
			organizationVo.setKeyToken(null);
			organizationVo.setValueToken(null);
			((MinimalOrganizationResponse) response).setData(basicOrganizationVo);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_ORGANIZATION_CREATED,Constants.SUCCESS_ORGANIZATION_CREATED,Constants.ORGANIZATION_CREATED_SUCCESSFULLY);
			logger.info("Response Payload:"+generateRequestAndResponseLogPaylod(response));
			Thread.sleep(10000);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (Exception e) {

			if(e.getCause()!=null){
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_ORGANIZATION_CREATED,e.getCause().getMessage(), Constants.ORGANIZATION_CREATED_UNSUCCESSFULLY);
			}else{
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_ORGANIZATION_CREATED,e.getMessage(), Constants.ORGANIZATION_CREATED_UNSUCCESSFULLY);
			}
			logger.error("Error Payload:"+generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		} 		

	}



	//For Fetching all Organization details
	@RequestMapping(value = "/v1/organizations/{userId}")
	public ResponseEntity<BaseResponse> getAllOrganizationsV2(HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse,@PathVariable int userId) {
		logger.info("Entry into method:getAllOrganizationsV2");
		BaseResponse response = new ListBasicOrganizationResponse();
		try {
			List<BasicOrganizationVo> data = organizationService.getAllNewOrganizations(userId );
			setTokens(response,httpServletRequest.getHeader("keyToken"),httpServletRequest.getHeader("valueToken"));
			((ListBasicOrganizationResponse) response).setData(data);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_ORGANIZATION_FETCH,
					Constants.SUCCESS_ORGANIZATION_FETCH, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (Exception e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_ORGANIZATION_FETCH, e.getMessage(),
					Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	//For Fetching all Organization details with Roles 
	@RequestMapping(value = "/v1/organization/list/{emailId}")
	public ResponseEntity<BaseResponse> getAllOrganizationsWithRolesV2(HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse,@PathVariable String emailId) {
		logger.info("Entry into method:getAllOrganizationsWithRolesV2");
		BaseResponse response = new ListBasicOrganizationResponse();
		try {
			List<BasicOrganizationVo> data = organizationService.getOrganizationsByEmailWithRoles(emailId );
			setTokens(response,httpServletRequest.getHeader("keyToken"),httpServletRequest.getHeader("valueToken"));
			((ListBasicOrganizationResponse) response).setData(data);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_ORGANIZATION_FETCH,
					Constants.SUCCESS_ORGANIZATION_FETCH, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (Exception e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_ORGANIZATION_FETCH, e.getMessage(),
					Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@RequestMapping(value = "/v1/dropsdowns/organizations")
	public ResponseEntity<BaseResponse> getOrganizationDropDownV2(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) {
		logger.info("Entry into getOrganizationDropDownV2");
		BaseResponse response = new OrganizationDropDownResponse();
		try {
			OrganizationDropDownVo data = organizationService.getOrganizationDropDownData();
			setTokens(response, httpServletRequest.getHeader("keyToken"), httpServletRequest.getHeader("valueToken"));
			((OrganizationDropDownResponse) response).setData(data);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_DROP_DOWN_FETCH,
					Constants.SUCCESS_DROP_DOWN_FETCH, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);

		} catch (Exception e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_PO_FETCH, e.getMessage(),
					Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	
	@RequestMapping(value = "/v1/income_tax/login/{organizationId}/{organizationName}")
	public ResponseEntity<BaseResponse> getIncomeTaxLoginDetails(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse,@PathVariable int organizationId,@PathVariable String organizationName) {
		logger.info("Entry into getIncomeTaxLoginDetails");
		BaseResponse response = new IncomeTaxLoginResponse();
		try {
			IncomeTaxLoginVo data = organizationService.getIncomeTaxLoginDetails(organizationId);
			setTokens(response, httpServletRequest.getHeader("keyToken"), httpServletRequest.getHeader("valueToken"));
			((IncomeTaxLoginResponse) response).setData(data);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_ORGANIZATION_FETCH,
					Constants.SUCCESS_ORGANIZATION_FETCH, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);

		} catch (Exception e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_ORGANIZATION_FETCH, e.getMessage(),
					Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	


}

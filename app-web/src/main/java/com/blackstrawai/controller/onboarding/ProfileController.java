package com.blackstrawai.controller.onboarding;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.server.PathParam;

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
import com.blackstrawai.common.Constants;
import com.blackstrawai.helper.OnBoardingConvertToVoHelper;
import com.blackstrawai.onboarding.LoginService;
import com.blackstrawai.onboarding.loginandregistration.ProfileVo;
import com.blackstrawai.request.JSONObject;
import com.blackstrawai.request.onboarding.loginandregistration.OrgDefaultRequest;
import com.blackstrawai.request.onboarding.loginandregistration.ProfileRequest;
import com.blackstrawai.response.onboarding.loginandregistration.ProfileResponse;

@RestController
@CrossOrigin
@RequestMapping("/decifer/profile")
public class ProfileController extends BaseController  {

	@Autowired
	LoginService loginService;	

	private  Logger logger = Logger.getLogger(ProfileController.class);


	@RequestMapping(value = "/v1/profiles", method = RequestMethod.PUT)
	public ResponseEntity<BaseResponse> updateProfile(HttpServletRequest httpRequest, HttpServletResponse httpResponse,@RequestBody JSONObject<ProfileRequest> profileRequest) {		

		BaseResponse response = new ProfileResponse();
		try {	
			logger.info("Entry into method: updateProfile");		
			logger.info("Request Payload:"+generateRequestAndResponseLogPaylod(profileRequest));				
			ProfileVo profileVo=OnBoardingConvertToVoHelper.getInstance().convertProfileVoFromProfileRequest(profileRequest.getData());		
			profileVo=loginService.updateProfile(profileVo);
			setTokens(response,httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((ProfileResponse) response).setData(profileVo);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_PROFILE_UPDATED,Constants.SUCCESS_PROFILE_UPDATED,Constants.PROFILE_SUCCESS);
			logger.info("Response Payload:"+generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (Exception e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_PROFILE_UPDATED,e.getMessage(), Constants.PROFILE_FAILURE);
			logger.error("Error Payload:"+generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		} 


	}
	
	@RequestMapping(value = "/v1/default_org", method = RequestMethod.PUT)
	public ResponseEntity<BaseResponse> setDeafultOrganization(HttpServletRequest httpRequest, HttpServletResponse httpResponse,@RequestBody JSONObject<OrgDefaultRequest> request) {		

		BaseResponse response = new ProfileResponse();
		try {	
			logger.info("Entry into method: setDeafultOrganization");		
			loginService.setDeafultOrganization(request.getData().getEmail(), request.getData().getOrganizationId());
			setTokens(response,httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			ProfileVo vo=new ProfileVo();
			vo.setEmailId(request.getData().getEmail());
			((ProfileResponse) response).setData(vo);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_SET_DEFAULT_ORG,Constants.SUCCESS_SET_DEFAULT_ORG,Constants.SET_DEFAULT_ORG_SUCCESS);
			logger.info("Response Payload:"+generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (Exception e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_SET_DEFAULT_ORG,e.getMessage(), Constants.SET_DEFAULT_ORG_FAILURE);
			logger.error("Error Payload:"+generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		} 


	}
	
	@RequestMapping(value = "/v1/profiles/{email}", method = RequestMethod.GET)
	public ResponseEntity<BaseResponse> getProfileByEmail(HttpServletRequest httpRequest, HttpServletResponse httpResponse,@PathVariable String email) {		

		BaseResponse response = new ProfileResponse();
		try {	
			logger.info("Entry into method: getProfileByEmail");		
			ProfileVo vo=loginService.getProfileByEmail(email);
			setTokens(response,httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((ProfileResponse) response).setData(vo);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_PROFILE_FETCH,Constants.SUCCESS_PROFILE_FETCH,Constants.PROFILE_SUCCESS);
			logger.info("Response Payload:"+generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (Exception e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_PROFILE_FETCH,e.getMessage(), Constants.PROFILE_FAILURE);
			logger.error("Error Payload:"+generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		} 


	}


}

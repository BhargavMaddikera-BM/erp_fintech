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

import com.blackstrawai.ApplicationException;
import com.blackstrawai.common.BaseController;
import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.common.CacheService;
import com.blackstrawai.common.Constants;
import com.blackstrawai.common.TokenVo;
import com.blackstrawai.general.PythonIntegration;
import com.blackstrawai.helper.OnBoardingConvertToVoHelper;
import com.blackstrawai.onboarding.LoginService;
import com.blackstrawai.onboarding.RegistrationService;
import com.blackstrawai.onboarding.loginandregistration.ChangePasswordVo;
import com.blackstrawai.onboarding.loginandregistration.LoginVo;
import com.blackstrawai.onboarding.loginandregistration.RecoverPasswordVo;
import com.blackstrawai.onboarding.loginandregistration.RegistrationVo;
import com.blackstrawai.onboarding.loginandregistration.ResetPasswordVo;
import com.blackstrawai.onboarding.user.UserVo;
import com.blackstrawai.request.JSONObject;
import com.blackstrawai.request.onboarding.loginandregistration.ChangePasswordRequest;
import com.blackstrawai.request.onboarding.loginandregistration.LoginRequest;
import com.blackstrawai.request.onboarding.loginandregistration.RecoverPasswordRequest;
import com.blackstrawai.request.onboarding.loginandregistration.RegisterationRequest;
import com.blackstrawai.request.onboarding.loginandregistration.ResetPasswordRequest;
import com.blackstrawai.response.onboarding.loginandregistration.ChangePasswordResponse;
import com.blackstrawai.response.onboarding.loginandregistration.ListRegistrationResponse;
import com.blackstrawai.response.onboarding.loginandregistration.LoginResponse;
import com.blackstrawai.response.onboarding.loginandregistration.LogoutResponse;
import com.blackstrawai.response.onboarding.loginandregistration.RecoverPasswordResponse;
import com.blackstrawai.response.onboarding.loginandregistration.RegistrationResponse;
import com.blackstrawai.response.onboarding.loginandregistration.ResetPasswordResponse;

@RestController
@CrossOrigin
public class LoginAndRegistrationController extends BaseController  {

	@Autowired
	LoginService loginService;	

	@Autowired
	RegistrationService registrationService;

	@Autowired
	CacheService cacheService;

	private  Logger logger = Logger.getLogger(LoginAndRegistrationController.class);


	@RequestMapping(value = "/v1/login", method = RequestMethod.POST)
	public ResponseEntity<BaseResponse> login(@RequestBody JSONObject<LoginRequest> loginRequest) {		
		logger.info("Entry into method:login");

		BaseResponse response = new LoginResponse();
		try {	
			logger.info("Request Payload:"+generateRequestAndResponseLogPaylod(loginRequest));				
			LoginVo loginVo=OnBoardingConvertToVoHelper.getInstance().convertLoginVoFromLoginRequest(loginRequest.getData());		
			TokenVo tokenVo=loginService.login(loginVo);
			setTokens(response,tokenVo.getKeyToken(),tokenVo.getValueToken());
			tokenVo.setKeyToken(null);
			tokenVo.setValueToken(null);
			if(tokenVo instanceof RegistrationVo){
				RegistrationVo regVo=(RegistrationVo) tokenVo;
				((LoginResponse) response).setType(regVo.getRoleName());
				if(regVo.getRoleName().equals("Super Admin")){
					((LoginResponse) response).setSuperAdmin(true);
				}else{
					((LoginResponse) response).setSuperAdmin(false);
				}				
				((LoginResponse) response).setRoleName(regVo.getRoleName());
			}else if(tokenVo instanceof UserVo){
				UserVo userVo=(UserVo) tokenVo;
				((LoginResponse) response).setType(userVo.getRoleName());
				((LoginResponse) response).setSuperAdmin(false);
				((LoginResponse) response).setRoleName(userVo.getRoleName());

			}
			((LoginResponse) response).setData(tokenVo);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_LOGIN,Constants.SUCCESS_LOGIN,Constants.USER_LOGIN_SUCCESS);
			logger.info("Response Payload:"+generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (Exception e) {
			if(e.getCause()!=null){
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_LOGIN,e.getCause().getMessage(), Constants.USER_LOGIN_FAILURE);
			}else{
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_LOGIN,e.getMessage(), Constants.USER_LOGIN_FAILURE);

			}
			logger.error("Error Payload:"+generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		} 	

	}

	//For Registration
	@RequestMapping(value = "/v1/registration", method = RequestMethod.POST)
	public ResponseEntity<BaseResponse> register(@RequestBody JSONObject<RegisterationRequest> registerationRequest) {		

		BaseResponse response = new RegistrationResponse();
		try {	
			logger.info("Entry into method:register");		
			logger.info("Request Payload:"+generateRequestAndResponseLogPaylod(registerationRequest));				
			RegistrationVo registrationVo=OnBoardingConvertToVoHelper.getInstance().convertRegistrationVoFromRegistrationRequest(registerationRequest.getData());		
			registrationVo=registrationService.register(registrationVo);
			setTokens(response,registrationVo.getKeyToken(),registrationVo.getValueToken());
			registrationVo.setKeyToken(null);
			registrationVo.setValueToken(null);
			((RegistrationResponse) response).setData(registrationVo);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_REGISTRATION,Constants.SUCCESS_REGISTRATION,Constants.REGISTRATION_SUCCESS);
			logger.info("Response Payload:"+generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (Exception e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_REGISTRATION,e.getMessage(), Constants.REGISTRATION_FAILURE);
			logger.error("Error Payload:"+generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		} 


	}

	//For Fetching All Registrations
	@RequestMapping(value = "/v1/registrations")
	public ResponseEntity<BaseResponse> getAllRegistrations(HttpServletRequest httpRequest, HttpServletResponse httpResponse) {	

		BaseResponse response = new ListRegistrationResponse();
		try {	
			logger.info("Entry into method:getAllRegistrations");		
			List<RegistrationVo> listAllRegistrations=registrationService.getAllRegistrations();
			((ListRegistrationResponse) response).setData(listAllRegistrations);
			setTokens(response,httpRequest.getHeader("keyToken"),httpRequest.getHeader("valueToken"));
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_ALL_REGISTRATIONS_FETCH,Constants.SUCCESS_ALL_REGISTRATIONS_FETCH,Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:"+generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (Exception e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_ALL_REGISTRATIONS_FETCH,e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:"+generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		} 


	}

	//For Fetching One Registration by Email
	@RequestMapping(value = "/v1/registrations/{email}")
	public ResponseEntity<BaseResponse> getRegistrationByEmailId(HttpServletRequest httpRequest, HttpServletResponse httpResponse,@PathVariable String email) {
		logger.info("Entry into method:getRegistrationByEmailId");
		BaseResponse response = new RegistrationResponse();
		try {	
			RegistrationVo registrationVo=registrationService.getRegistrationByEmail(email);
			setTokens(response,httpRequest.getHeader("keyToken"),httpRequest.getHeader("valueToken"));
			((RegistrationResponse) response).setData(registrationVo);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_REGISTRATION_FETCH,Constants.SUCCESS_REGISTRATION_FETCH,Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:"+generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (Exception e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_REGISTRATION_FETCH,e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:"+generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		} 

	}	


	@RequestMapping(value = "/v1/changePassword", method = RequestMethod.POST)
	public ResponseEntity<BaseResponse> changePassword(@RequestBody JSONObject<ChangePasswordRequest> changePasswordRequest) {	
		logger.info("Entry into method:changePassword");
		BaseResponse response = new ChangePasswordResponse();
		try {	
			logger.info("Request Payload:"+generateRequestAndResponseLogPaylod(changePasswordRequest));				
			ChangePasswordVo changePasswordVo=OnBoardingConvertToVoHelper.getInstance().convertChangePasswordVoFromChangePasswordRequest(changePasswordRequest.getData());		
			changePasswordVo=loginService.changePassword(changePasswordVo);
			((ChangePasswordResponse) response).setData(changePasswordVo);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_CHANGE_PASSWORD,Constants.SUCCESS_CHANGE_PASSWORD,Constants.CHANGE_PASSWORD_SUCCESS);
			logger.info("Response Payload:"+generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (Exception e) {
			if(e.getCause()!=null){
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_CHANGE_PASSWORD,e.getCause().getMessage(), Constants.CHANGE_PASSWORD_FAILURE);
			}else{
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_CHANGE_PASSWORD,e.getMessage(), Constants.CHANGE_PASSWORD_FAILURE);
			}
			logger.error("Error Payload:"+generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		} 			
	}

	@RequestMapping(value = "/v1/basic/recoverPassword", method = RequestMethod.POST)
	public ResponseEntity<BaseResponse> recoverPassword(HttpServletRequest httpRequest, HttpServletResponse httpResponse,@RequestBody JSONObject<RecoverPasswordRequest> recoverPasswordRequest) {

		BaseResponse response = new RecoverPasswordResponse();
		try {	
			logger.info("Entry into method:recoverPassword");		
			logger.info("Request Payload:"+generateRequestAndResponseLogPaylod(recoverPasswordRequest));				
			RecoverPasswordVo recoverPasswordVo=OnBoardingConvertToVoHelper.getInstance().convertRecoverPasswordVoFromRecoverPasswordRequest(recoverPasswordRequest.getData());		
			recoverPasswordVo=loginService.recoverPassword(recoverPasswordVo);
			((RecoverPasswordResponse) response).setData(recoverPasswordVo);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_RECOVERY_PASSWORD,Constants.SUCCESS_RECOVERY_PASSWORD,Constants.RECOVERY_PASSWORD_SUCCESS);
			logger.info("Response Payload:"+generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (Exception e) {
			if(e.getCause()!=null){
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_RECOVERY_PASSWORD,e.getCause().getMessage(), Constants.RECOVERY_PASSWORD_FAILURE);
			}else{
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_RECOVERY_PASSWORD,e.getMessage(), Constants.RECOVERY_PASSWORD_FAILURE);
			}
			logger.error("Error Payload:"+generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		} 	

	}

	@RequestMapping(value = "/v1/basic/resetPassword", method = RequestMethod.POST)
	public ResponseEntity<BaseResponse> resetPassword(HttpServletRequest httpRequest, HttpServletResponse httpResponse,@RequestBody JSONObject<ResetPasswordRequest> resetPasswordRequest) {		
		BaseResponse response = new ResetPasswordResponse();
		try {	
			logger.info("Entry into method:resetPassword");		
			logger.info("Request Payload:"+generateRequestAndResponseLogPaylod(resetPasswordRequest));				
			ResetPasswordVo resetPasswordVo=OnBoardingConvertToVoHelper.getInstance().convertResetPasswordVoFromResetPasswordRequest(resetPasswordRequest.getData());		
			resetPasswordVo=loginService.resetPassword(resetPasswordVo);
			resetPasswordVo.setPassword(null);
			resetPasswordVo.setResetToken(null);
			((ResetPasswordResponse) response).setData(resetPasswordVo);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_RESET_PASSWORD,Constants.SUCCESS_RESET_PASSWORD,Constants.RESET_PASSWORD_SUCCESS);
			logger.info("Response Payload:"+generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (Exception e) {
			if(e.getCause()!=null){
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_RESET_PASSWORD,e.getCause().getMessage(), Constants.RESET_PASSWORD_FAILURE);
			}else{
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_RESET_PASSWORD,e.getMessage(), Constants.RESET_PASSWORD_FAILURE);
			}
			logger.error("Error Payload:"+generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		} 		
	}

	@RequestMapping(value = "/v1/basic/createPassword", method = RequestMethod.POST)
	public ResponseEntity<BaseResponse> createPassword(HttpServletRequest httpRequest, HttpServletResponse httpResponse,@RequestBody JSONObject<ResetPasswordRequest> resetPasswordRequest) {		
		BaseResponse response = new ResetPasswordResponse();
		try {	
			logger.info("Entry into method:createPassword");		
			logger.info("Request Payload:"+generateRequestAndResponseLogPaylod(resetPasswordRequest));				
			ResetPasswordVo resetPasswordVo=OnBoardingConvertToVoHelper.getInstance().convertResetPasswordVoFromResetPasswordRequest(resetPasswordRequest.getData());		
			resetPasswordVo=loginService.createPassword(resetPasswordVo);
			resetPasswordVo.setPassword(null);
			resetPasswordVo.setResetToken(null);
			((ResetPasswordResponse) response).setData(resetPasswordVo);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_CREATE_PASSWORD,Constants.SUCCESS_CREATE_PASSWORD,Constants.CREATE_PASSWORD_SUCCESS);
			logger.info("Response Payload:"+generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (Exception e) {
			if(e.getCause()!=null){
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_CREATE_PASSWORD,e.getCause().getMessage(), Constants.CREATE_PASSWORD_FAILURE);
			}else{
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_CREATE_PASSWORD,e.getMessage(), Constants.CREATE_PASSWORD_FAILURE);
			}
			logger.error("Error Payload:"+generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		} 		
	}

	@RequestMapping(value = "/v1/logout")
	public ResponseEntity<BaseResponse>  logout(HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse) throws ApplicationException {

		logger.info("Entry into method:logout");	
		BaseResponse response = new LogoutResponse();
		try {	
			cacheService.removeToken(httpServletRequest.getHeader("keyToken"));
			PythonIntegration.getInstance().logout(httpServletRequest.getHeader("keyToken"));
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_LOGOUT,Constants.SUCCESS_LOGOUT,Constants.USER_LOGOUT_SUCCESS);
			logger.info("Response Payload:"+generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (Exception e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_LOGIN,e.getMessage(), Constants.USER_LOGOUT_FAILURE);
			logger.error("Error Payload:"+generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		} 	
	}



}

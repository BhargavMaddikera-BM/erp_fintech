package com.blackstrawai.controller.util;

import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.common.BaseController;
import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.common.CacheService;
import com.blackstrawai.common.Constants;
import com.blackstrawai.general.PythonIntegration;
import com.blackstrawai.onboarding.ApplicationService;
import com.blackstrawai.onboarding.SubscriptionService;
import com.blackstrawai.onboarding.loginandregistration.ApplicationsVo;
import com.blackstrawai.onboarding.loginandregistration.SubscriptionVo;
import com.blackstrawai.response.onboarding.loginandregistration.ApplicationResponse;
import com.blackstrawai.response.onboarding.loginandregistration.LogoutResponse;
import com.blackstrawai.response.onboarding.loginandregistration.SubscriptionResponse;
import com.blackstrawai.response.onboarding.loginandregistration.TimeZoneResponse;

@RestController
@CrossOrigin
public class NoTokenBasicController extends BaseController {	

	@Autowired
	ApplicationService applicationService;

	@Autowired
	SubscriptionService subscriptionService;	
	
	@Autowired
	CacheService cacheService;

	private Logger logger = Logger.getLogger(NoTokenBasicController.class);
	
	// For Fetching All Basic Applications
		@RequestMapping(value = "/v1/basic/test")
		public ResponseEntity<BaseResponse> testBasic(HttpServletRequest httpRequest,
				HttpServletResponse httpResponse) {
			System.out.println("**********In Test");
				return null;
		}

	// For Fetching All Basic Applications
	@RequestMapping(value = "/v1/basic/applications")
	public ResponseEntity<BaseResponse> getAllBasicApplications(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse) {
		BaseResponse response = new ApplicationResponse();
		try {
			logger.info("Entry into method:getAllBasicApplications");
			ApplicationsVo data = applicationService.getAllApplicationsAndModules();
			((ApplicationResponse) response).setData(data);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_APPLICATIONS_FETCH,
					Constants.SUCCESS_APPLICATIONS_FETCH, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (Exception e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_APPLICATIONS_FETCH, e.getMessage(),
					Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	// For Fetching All Subscriptions
	@RequestMapping(value = "/v1/basic/subscriptions")
	public ResponseEntity<BaseResponse> getAllSubscriptions(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse) {
		BaseResponse response = new SubscriptionResponse();
		try {
			logger.info("Entry into method:getAllSubscriptions");
			List<SubscriptionVo> data = subscriptionService.getAllSubscriptions();
			((SubscriptionResponse) response).setData(data);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_SUBSCRIPTIONS_FETCH,
					Constants.SUCCESS_SUBSCRIPTIONS_FETCH, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (Exception e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_SUBSCRIPTIONS_FETCH,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	// For Fetching All Subscriptions
	@RequestMapping(value = "/v1/basic/timezones")
	public ResponseEntity<BaseResponse> getTimeZones(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse) {
		BaseResponse response = new TimeZoneResponse();
		try {
			logger.info("Entry into method:getTimeZones");
			List<String>data=new ArrayList<String>();				

			String[] ids = TimeZone.getAvailableIDs();
			for (String id : ids) {
				String value=displayTimeZone(TimeZone.getTimeZone(id));
				data.add(value);
			}			
			((TimeZoneResponse) response).setData(data);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_TIME_ZONE_FETCH,
					Constants.SUCCESS_TIME_ZONE_FETCH, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (Exception e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_TIME_ZONE_FETCH,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	
	@RequestMapping(value = "/v1/basic/token_expired")
	public ResponseEntity<BaseResponse>  tokenExpired(HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse) throws ApplicationException {
		
		logger.info("Entry into method:tokenExpired");	
		BaseResponse response = new LogoutResponse();
		try {	
			cacheService.removeToken(httpServletRequest.getHeader("keyToken"));
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_TOKEN_EXPIRED,Constants.SUCCESS_TOKEN_EXPIRED,Constants.TOKEN_EXPIRATION_SUCCESS);
			logger.info("Response Payload:"+generateRequestAndResponseLogPaylod(response));
			PythonIntegration.getInstance().logout(httpServletRequest.getHeader("keyToken"));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (Exception e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_TOKEN_EXPIRED,e.getMessage(), Constants.TOKEN_EXPIRATION_FAILURE);
			logger.error("Error Payload:"+generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		} 	
	}


	private  String displayTimeZone(TimeZone tz) {

		long hours = TimeUnit.MILLISECONDS.toHours(tz.getRawOffset());
		long minutes = TimeUnit.MILLISECONDS.toMinutes(tz.getRawOffset()) 
				- TimeUnit.HOURS.toMinutes(hours);
		// avoid -4:-30 issue
		minutes = Math.abs(minutes);

		String result = "";
		if (hours > 0) {
			result = String.format("(GMT+%d:%02d) %s", hours, minutes, tz.getID());
		} else {
			result = String.format("(GMT%d:%02d) %s", hours, minutes, tz.getID());
		}

		return result;

	}

}

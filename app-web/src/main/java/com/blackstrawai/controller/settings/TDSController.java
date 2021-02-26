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
import com.blackstrawai.request.settings.TdsRequest;
import com.blackstrawai.response.settings.ListTdsResponse;
import com.blackstrawai.response.settings.TdsResponse;
import com.blackstrawai.settings.TdsService;
import com.blackstrawai.settings.TdsVo;

@RestController
@CrossOrigin
//This will be part of Settings and Preferences. Hence sp.
@RequestMapping("/decifer/sp/tds")

public class TDSController extends BaseController{
	
	@Autowired
	TdsService tdsService;

	private Logger logger = Logger.getLogger(TDSController.class);
	
	// For Creating Tds
		@RequestMapping(value = "/v1/tds", method = RequestMethod.POST)
		public ResponseEntity<BaseResponse> createTds(HttpServletRequest httpRequest, HttpServletResponse httpResponse,
				@RequestBody JSONObject<TdsRequest> tdsRequest) {
			logger.info("Entry into method: createTds");
			BaseResponse response = new TdsResponse();
			try {
				logger.info("Request Payload:" + generateRequestAndResponseLogPaylod(tdsRequest));
				TdsVo tdsVo = SettingsConvertToVoHelper.getInstance()
						.convertTdsVoFromTdsRequest(tdsRequest.getData());
				tdsVo = tdsService.createTds(tdsVo);
				setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
				tdsVo.setKeyToken(null);
				tdsVo.setValueToken(null);
				((TdsResponse) response).setData(tdsVo);
				response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_TDS_CREATED,
						Constants.SUCCESS_TDS_CREATED, Constants.TDS_CREATED_SUCCESSFULLY);
				logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
				return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);

			} catch (ApplicationException e) {
				if(e.getCause()!=null){
					response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_TDS_CREATED,
							e.getCause().getMessage(), Constants.TDS_CREATED_UNSUCCESSFULLY);
				}else{
					response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_TDS_CREATED,
							e.getMessage(), Constants.TDS_CREATED_UNSUCCESSFULLY);
				}			
				logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
				return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}



		// For Deleting Tds

		@RequestMapping(value = "/v1/tds/{organizationId}/{organizationName}/{userId}/{id}/{roleName}/{status}", method = RequestMethod.DELETE)
		public ResponseEntity<BaseResponse> deleteTds(HttpServletRequest httpRequest, 
				HttpServletResponse httpResponse,
				@PathVariable int organizationId, 
				@PathVariable String organizationName,
				@PathVariable String userId, 
				@PathVariable int id,
				@PathVariable String roleName,
				@PathVariable String status) {
			logger.info("Entry into method:deleteTds");
			BaseResponse response = new TdsResponse();
			try {
				TdsVo tdsVo = tdsService.deleteTds(id,status,userId,roleName);
				setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
				tdsVo.setKeyToken(null);
				tdsVo.setValueToken(null);
				((TdsResponse) response).setData(tdsVo);
				if(status.equals(CommonConstants.STATUS_AS_ACTIVE)) {
					response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_TDS_ACTIVATED,
							Constants.SUCCESS_TDS_ACTIVATED, Constants.TDS_DELETED_SUCCESSFULLY);
					logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
				}else if(status.equals(CommonConstants.STATUS_AS_INACTIVE)) {
					response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_TDS_DEACTIVATED,
							Constants.SUCCESS_TDS_DEACTIVATED, Constants.TDS_DELETED_SUCCESSFULLY);
					logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
				}
				return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
			} catch (ApplicationException e) {
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_TDS_DELETED,
						e.getMessage(), Constants.TDS_DELETED_UNSUCCESSFULLY);
				logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
				return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}

		}

		
		// For Update Tds

		@RequestMapping(value = "/v1/tds", method = RequestMethod.PUT)
		public ResponseEntity<BaseResponse> updateTds(HttpServletRequest httpRequest,
				HttpServletResponse httpResponse, @RequestBody JSONObject<TdsRequest> tdsRequest) {
			logger.info("Entry into method:updateTds");
			BaseResponse response = new TdsResponse();
			try {
				logger.info("Request Payload:" + generateRequestAndResponseLogPaylod(tdsRequest));
				TdsVo tdsVo = SettingsConvertToVoHelper.getInstance()
						.convertTdsVoFromTdsRequest(tdsRequest.getData());
				tdsVo = tdsService.updateTds(tdsVo);
				setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
				tdsVo.setKeyToken(null);
				tdsVo.setValueToken(null);
				((TdsResponse) response).setData(tdsVo);
				response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_TDS_UPDATED,
						Constants.SUCCESS_TDS_UPDATED, Constants.TDS_UPDATED_SUCCESSFULLY);
				logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
				return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
			} catch (ApplicationException e) {
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_TDS_UPDATED,
						e.getMessage(), Constants.TDS_UPDATED_UNSUCCESSFULLY);
				logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
				return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}

		// For Fetching Tds by Id
		@RequestMapping(value = "/v1/tds/{organizationId}/{organizationName}/{userId}/{roleName}/{id}/{isBase}", method = RequestMethod.GET)
		public ResponseEntity<BaseResponse> getTdsById(HttpServletRequest httpRequest,
				HttpServletResponse httpResponse, 
				@PathVariable int organizationId, 
				@PathVariable String organizationName,
				@PathVariable String userId,
				@PathVariable String roleName,
				@PathVariable int id,
				@PathVariable Boolean isBase) {
			logger.info("Entry into method:getTdsById");
			BaseResponse response = new TdsResponse();
			try {
				TdsVo tdsVo = tdsService.getTdsById(id,isBase,organizationId,userId,roleName);
				setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
				((TdsResponse) response).setData(tdsVo);
				response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_TDS_FETCH,
						Constants.SUCCESS_TDS_FETCH, Constants.SUCCESS_DURING_GET);
				logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
				return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
			} catch (ApplicationException e) {
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_TDS_FETCH,
						e.getMessage(), Constants.FAILURE_DURING_GET);
				logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
				logger.info(response);
				return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}

		// For Fetching all Tds details
		@RequestMapping(value = "/v1/tds/{organizationId}/{organizationName}/{userId}/{roleName}", method = RequestMethod.GET)
		public ResponseEntity<BaseResponse> getAllTdsOfAnOrganizationForUserAndRole(HttpServletRequest httpRequest,
				HttpServletResponse httpResponse, @PathVariable int organizationId,@PathVariable String organizationName,
				@PathVariable String userId,@PathVariable String roleName) {
			logger.info("Entry into method:getAllTdsOfAnOrganizationForUserAndRole");
			BaseResponse response = new ListTdsResponse();
			try {
				List<TdsVo> listAllTds = tdsService
						.getAllTdsOfAnOrganizationForUserAndRole(organizationId,userId,roleName);
				setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
				((ListTdsResponse) response).setData(listAllTds);
				response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_TDS_FETCH,
						Constants.SUCCESS_TDS_FETCH, Constants.SUCCESS_DURING_GET);
				logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
				return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
			} catch (ApplicationException e) {
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_TDS_FETCH,
						e.getMessage(), Constants.FAILURE_DURING_GET);
				logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
				logger.info(response);
				return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}

}

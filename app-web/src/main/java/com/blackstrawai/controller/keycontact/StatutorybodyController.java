/**  * Module: Statutory bodies module
	 * date:14-july-2020
	 * Table Name: statutory_body under usermanagement
	 */

package com.blackstrawai.controller.keycontact;

import java.text.ParseException;
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
import com.blackstrawai.keycontact.StatutoryBodyService;
import com.blackstrawai.keycontact.dropdowns.StatutoryBodyDropDownVo;
import com.blackstrawai.keycontact.statutorybody.StatutoryBodyVo;
import com.blackstrawai.request.JSONObject;
import com.blackstrawai.request.keycontact.statutorybody.StatutoryBodyRequest;
import com.blackstrawai.response.keycontact.statutorybody.ListStatutoryBodyResponse;
import com.blackstrawai.response.keycontact.statutorybody.StatutoryBodyResponse;
import com.blackstrawai.response.keycontact.statutorybody.StatutoryDropDownResponse;

@RestController
@CrossOrigin
//This will be part of Settings and Preferences. Hence sp.
@RequestMapping("/decifer/statutory_body")
public class StatutorybodyController extends BaseController {
	
	@Autowired
	StatutoryBodyService statutoryBodyService;
	
	private Logger logger = Logger.getLogger(StatutorybodyController.class);
	
	/** Method to create new Statutory body
	 * @param httpRequest
	 * @param httpResponse
	 * @param statutoryBodyRequest
	 * @return response
	 * @throws org.json.simple.parser.ParseException 
	 * @throws ParseException 
	 */
	@RequestMapping(value = "/v1/statutory_bodies", method = RequestMethod.POST)
	public ResponseEntity<BaseResponse> createStatutoryBody(HttpServletRequest httpRequest, HttpServletResponse httpResponse,
			@RequestBody JSONObject<StatutoryBodyRequest> statutoryBodyRequest){
		logger.info("Entry into method: createStatutoryBody");
		BaseResponse response = new StatutoryBodyResponse();
		logger.info("Request Payload:" + generateRequestAndResponseLogPaylod(statutoryBodyRequest));
		
		try {
		StatutoryBodyVo statutoryBodyVo =  KeyContactConvertToVoHelper.getInstance()
				.convertStatutoryBodyVoFromStatutoryBodyRequest(statutoryBodyRequest.getData());	
		statutoryBodyService.createStatutoryBody(statutoryBodyVo);
		setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
		statutoryBodyVo.setKeyToken(null);
		statutoryBodyVo.setValueToken(null);		
		((StatutoryBodyResponse) response).setData(statutoryBodyVo);		
		response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_STATUTORY_BODY_CREATED,
				Constants.SUCCESS_STATUTORY_BODY_CREATED, Constants.STATUTORY_BODY_CREATED_SUCCESSFULLY);
		
		logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
		return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);

	} catch (ApplicationException e) {
		if(e.getCause()!=null){
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_STATUTORY_BODY_CREATED,
					e.getCause().getMessage(), Constants.STATUTORY_BODY_CREATED_UNSUCCESSFULLY);
		}else{
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_STATUTORY_BODY_CREATED,
					e.getMessage(), Constants.STATUTORY_BODY_CREATED_UNSUCCESSFULLY);
		}			
		logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
		return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	}
	
	
	/**
	 * @param httpRequest
	 * @param httpResponse
	 * @param statutoryBodyRequest
	 * @return
	 * @throws org.json.simple.parser.ParseException 
	 * @throws ParseException 
	 */
	@RequestMapping(value = "/v1/statutory_bodies", method = RequestMethod.PUT)
	public ResponseEntity<BaseResponse> updateStatutoryBody(HttpServletRequest httpRequest, HttpServletResponse httpResponse,
			@RequestBody JSONObject<StatutoryBodyRequest> statutoryBodyRequest)  {
		logger.info("Entry into method: updateStatutoryBody");
		BaseResponse response = new StatutoryBodyResponse();
		try {
			logger.info("Request Payload:" + generateRequestAndResponseLogPaylod(statutoryBodyRequest));			
			StatutoryBodyVo statutoryBodyVo =  KeyContactConvertToVoHelper.getInstance()
					.convertStatutoryBodyVoFromStatutoryBodyRequest(statutoryBodyRequest.getData());			
			statutoryBodyVo=statutoryBodyService.updateStatutoryBody(statutoryBodyVo);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			statutoryBodyVo.setKeyToken(null);
			statutoryBodyVo.setValueToken(null);
			((StatutoryBodyResponse) response).setData(statutoryBodyVo);
			response = constructResponse(response, Constants.SUCCESS, Constants.STATUTORY_BODY_UPDATED_SUCCESSFULLY,
					Constants.SUCCESS_STATUTORY_BODY_UPDATED, Constants.STATUTORY_BODY_UPDATED_SUCCESSFULLY);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_STATUTORY_BODY_UPDATED,
					e.getMessage(), Constants.STATUTORY_BODY_UPDATED_UNSUCCESSFULLY);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	/** Method to return list of Statutory body 
	 * @param httpRequest
	 * @param httpResponse
	 * @return response
	 */
	@RequestMapping(value = "/v1/statutory_bodies/{organizationId}/{organizationName}/{userId}/{roleName}", method = RequestMethod.GET)
	public ResponseEntity<BaseResponse> getAllStatutoryBodiesOfAnOrganizationForUserAndRole(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @PathVariable int organizationId,@PathVariable String organizationName,
			@PathVariable String userId,@PathVariable String roleName) {
		
		logger.info("Entry into method: getAllStatutoryBodiesOfAnOrganizationForUserAndRole");
		BaseResponse response = new ListStatutoryBodyResponse();
		try {
		
			
			List<StatutoryBodyVo> ListOfStatutoryBody = statutoryBodyService.getAllStatutoryBodiesOfAnOrganizationForUserAndRole(organizationId,userId,roleName);			
			
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((ListStatutoryBodyResponse) response).setData(ListOfStatutoryBody);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_STATUTORY_BODY_FETCH,
					Constants.SUCCESS_STATUTORY_BODY_FETCH, Constants.SUCCESS_DURING_GET);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
			
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_STATUTORY_BODY_FETCH,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
				
	
	}
	
	/** Method to return list of Statutory body 
	 * @param httpRequest
	 * @param httpResponse
	 * @return response
	 */
	@RequestMapping(value = "/v1/statutory_bodies/{organizationId}/{organizationName}/{userId}/{roleName}/{id}", method = RequestMethod.GET)
	public ResponseEntity<BaseResponse> getStatutoryBodyById(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, 
			@PathVariable int organizationId, 
			@PathVariable String organizationName,
			@PathVariable String userId,
			@PathVariable String roleName,
			@PathVariable int id) {
		
		logger.info("Entry into method: getStatutoryBodyById");
		BaseResponse response = new StatutoryBodyResponse();
		try {			
			StatutoryBodyVo statutoryBodyVo = statutoryBodyService.getStatutoryBodyById(id);			
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((StatutoryBodyResponse) response).setData(statutoryBodyVo);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_STATUTORY_BODY_FETCH,
					Constants.SUCCESS_STATUTORY_BODY_FETCH, Constants.SUCCESS_DURING_GET);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
			
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_STATUTORY_BODY_FETCH,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
				
	
	}
	
	
	/** Method to deactivate the Statutory body status
	 * @param httpRequest
	 * @param httpResponse
	 * @param statutoryBodyRequest
	 * @return response
	 */
	@RequestMapping(value = "/v1/statutory_bodies/{organizationId}/{organizationName}/{userId}/{id}/{roleName}/{status}", method = RequestMethod.DELETE)
	public ResponseEntity<BaseResponse> deleteStatutoryBody(HttpServletRequest httpRequest, 
			HttpServletResponse httpResponse,
			@PathVariable int organizationId, 
			@PathVariable String organizationName,
			@PathVariable String userId, 
			@PathVariable int id,
			@PathVariable String roleName,
			@PathVariable String status) {
		logger.info("Entry into deleteStatutoryBody");
		BaseResponse response = new StatutoryBodyResponse();
		try {	
			StatutoryBodyVo data=statutoryBodyService.deleteStatutoryBody(id,userId,roleName,status);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((StatutoryBodyResponse) response).setData(data);
			if(status.equals(CommonConstants.STATUS_AS_ACTIVE)) {
				response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_STATUTORY_BODY_ACTIVATED,
						Constants.SUCCESS_STATUTORY_BODY_ACTIVATED, Constants.STATUTORY_BODY_DELETED_SUCCESSFULLY);
				logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			}else if(status.equals(CommonConstants.STATUS_AS_INACTIVE)) {
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_STATUTORY_BODY_DEACTIVATED,
					Constants.SUCCESS_STATUTORY_BODY_DEACTIVATED, Constants.STATUTORY_BODY_DELETED_SUCCESSFULLY);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			}			
			
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_STATUTORY_BODY_DELETE,
					e.getMessage(), Constants.STATUTORY_BODY_DELETED_UNSUCCESSFULLY);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/v1/dropsdowns/statutory_bodies/{organizationId}")
	public ResponseEntity<BaseResponse> getStatutoryBodyDropDown(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, @PathVariable int organizationId) {
		logger.info("Entry into getUserDropDown");
		BaseResponse response = new StatutoryDropDownResponse();
		try {
			StatutoryBodyDropDownVo data = statutoryBodyService.getStatutoryBodyDropDownData(organizationId);
			setTokens(response, httpServletRequest.getHeader("keyToken"), httpServletRequest.getHeader("valueToken"));
			((StatutoryDropDownResponse) response).setData(data);
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
	
}
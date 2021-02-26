package com.blackstrawai.controller.ar;

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
import com.blackstrawai.ar.LetterOfUndertakingService;
import com.blackstrawai.ar.dropdowns.LetterOfUndertakingDropDownVo;
import com.blackstrawai.ar.lut.LetterOfUndertakingVo;
import com.blackstrawai.common.BaseController;
import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.common.CommonConstants;
import com.blackstrawai.common.Constants;
import com.blackstrawai.helper.ArConvertToVoHelper;
import com.blackstrawai.request.JSONObject;
import com.blackstrawai.request.ar.lut.LetterOfUndertakingRequest;
import com.blackstrawai.response.ar.lut.LetterOfUndertakingDropDownResponse;
import com.blackstrawai.response.ar.lut.LetterOfUndertakingResponse;
import com.blackstrawai.response.ar.lut.ListLetterOfUndertakingResponse;


@RestController
@CrossOrigin
//This will be part of accountsreceivable. Hence ar.
@RequestMapping("/decifer/ar/lut")
public class LetterOfUndertakingController extends BaseController {

	@Autowired
	LetterOfUndertakingService letterOfUndertakingService;
	
	
	private Logger logger = Logger.getLogger(LetterOfUndertakingController.class);

	// For Creating LetterOfUndertaking
	@RequestMapping(value = "/v1/luts", method = RequestMethod.POST)
	public ResponseEntity<BaseResponse> createLetterOfUndertaking(HttpServletRequest httpRequest, HttpServletResponse httpResponse,
			@RequestBody JSONObject<LetterOfUndertakingRequest> letterOfUndertakingRequest) {
		logger.info("Entry into method: createLetterOfUndertaking");
		BaseResponse response = new LetterOfUndertakingResponse();
		try {
			logger.info("Request Payload:" + generateRequestAndResponseLogPaylod(letterOfUndertakingRequest));
			LetterOfUndertakingVo letterOfUndertakingVo= ArConvertToVoHelper.getInstance()
					.convertLetterOfUndertakingVoFromLetterOfUndertakingRequest(letterOfUndertakingRequest.getData());
			letterOfUndertakingVo= letterOfUndertakingService.createLetterOfUndertaking(letterOfUndertakingVo);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
		
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_LETTER_OF_UNDERSTANDING_CREATED,
					Constants.SUCCESS_LETTER_OF_UNDERSTANDING_CREATED, Constants.LETTER_OF_UNDERSTANDING_CREATED_SUCCESSFULLY);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);

		} catch (ApplicationException e) {
			if(e.getCause()!=null){
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_LETTER_OF_UNDERSTANDING_CREATED,
						e.getCause().getMessage(), Constants.LETTER_OF_UNDERSTANDING_CREATED_UNSUCCESSFULLY);
			}else{
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_LETTER_OF_UNDERSTANDING_CREATED,
						e.getMessage(), Constants.LETTER_OF_UNDERSTANDING_CREATED_UNSUCCESSFULLY);
			}			
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// For Fetching all LetterOfUndertaking details
	@RequestMapping(value = "/v1/luts/{organizationId}/{organizationName}/{userId}/{roleName}", method = RequestMethod.GET)
	public ResponseEntity<BaseResponse> getAllLetterOfUndertakingsForUserAndRole(HttpServletRequest httpRequest,HttpServletResponse httpResponse,
			@PathVariable int organizationId,@PathVariable String organizationName,@PathVariable String userId,
			@PathVariable String roleName) {
		logger.info("Entry into method:getAllLetterOfUndertakingsOfAnOrganization");
		BaseResponse response = new ListLetterOfUndertakingResponse();
		try {
			List<LetterOfUndertakingVo> listAllLetterOfUndertakings = letterOfUndertakingService.getAllLetterOfUndertakingsOfAnOrganizationForUserAndRole(organizationId,userId,roleName);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((ListLetterOfUndertakingResponse) response).setData(listAllLetterOfUndertakings);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_LETTER_OF_UNDERSTANDING_FETCH,
					Constants.SUCCESS_LETTER_OF_UNDERSTANDING_FETCH, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_LETTER_OF_UNDERSTANDING_FETCH,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}


	// For activateOrDeactivate LetterOfUndertaking

	@RequestMapping(value = "/v1/luts/{organizationId}/{organizationName}/{userId}/{id}/{roleName}/{status}", method = RequestMethod.DELETE)
	public ResponseEntity<BaseResponse> deleteLetterOfUndertaking(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, 
			@PathVariable int organizationId, 
			@PathVariable String organizationName,
			@PathVariable String userId, 
			@PathVariable int id,
			@PathVariable String roleName,
			@PathVariable String status) {
		logger.info("Entry into deleteLetterOfUndertaking");
		BaseResponse response = new LetterOfUndertakingResponse();
		try {
			LetterOfUndertakingVo letterOfUndertakingVo= letterOfUndertakingService.deleteLetterOfUndertaking(id, status,userId,roleName);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));

			if(status.equals(CommonConstants.STATUS_AS_ACTIVE)) {
				response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_LETTER_OF_UNDERSTANDING_ACTIVATED,
						Constants.SUCCESS_LETTER_OF_UNDERSTANDING_ACTIVATED, Constants.SUCCESS_LETTER_OF_UNDERSTANDING_DELETED);
				logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			}else if(status.equals(CommonConstants.STATUS_AS_INACTIVE)) {
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_LETTER_OF_UNDERSTANDING_DEACTIVATED,
					Constants.SUCCESS_LETTER_OF_UNDERSTANDING_DEACTIVATED, Constants.SUCCESS_LETTER_OF_UNDERSTANDING_DELETED);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			}
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_LETTER_OF_UNDERSTANDING_DELETED,
					e.getMessage(), Constants.LETTER_OF_UNDERSTANDING_DELETED_UNSUCCESSFULLY);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@RequestMapping(value = "/v1/luts", method = RequestMethod.PUT)
	public ResponseEntity<BaseResponse> updateLetterOfUndertaking(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @RequestBody JSONObject<LetterOfUndertakingRequest> letterOfUndertakingRequest) {
		logger.info("Entry into method:updateLetterOfUndertaking");
		BaseResponse response = new LetterOfUndertakingResponse();
		try {
			logger.info("Request Payload:" + generateRequestAndResponseLogPaylod(letterOfUndertakingRequest));
			LetterOfUndertakingVo letterOfUndertakingVo= ArConvertToVoHelper.getInstance()
					.convertLetterOfUndertakingVoFromLetterOfUndertakingRequest(letterOfUndertakingRequest.getData());
			letterOfUndertakingVo= letterOfUndertakingService.updateLetterOfUndertaking(letterOfUndertakingVo);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			/*
			 * letterOfUndertakingVo.setKeyToken(null); letterOfUndertakingVo.setValueToken(null);
			 * ((LetterOfUndertakingResponse) response).setData(letterOfUndertakingVo);
			 */
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_LETTER_OF_UNDERSTANDING_UPDATED,
					Constants.SUCCESS_LETTER_OF_UNDERSTANDING_UPDATED, Constants.LETTER_OF_UNDERSTANDING_UPDATED_SUCCESSFULLY);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_LETTER_OF_UNDERSTANDING_UPDATED,
					e.getMessage(), Constants.LETTER_OF_UNDERSTANDING_UPDATED_UNSUCCESSFULLY);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	// For Fetching LetterOfUndertaking by Id
	@RequestMapping(value = "/v1/luts/{organizationId}/{organizationName}/{userId}/{roleName}/{id}", method = RequestMethod.GET)
	public ResponseEntity<BaseResponse> getLetterOfUndertakingById(HttpServletRequest httpRequest, HttpServletResponse httpResponse,
			@PathVariable int organizationId, @PathVariable String organizationName, @PathVariable int userId,
			@PathVariable String roleName, @PathVariable int id) {
		logger.info("Entry into method:getLetterOfUndertakingById");
		BaseResponse response = new LetterOfUndertakingResponse();
		try {
			LetterOfUndertakingVo letterOfUndertakingVo = letterOfUndertakingService.getLetterOfUndertakingById(id);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((LetterOfUndertakingResponse) response).setData(letterOfUndertakingVo);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_LETTER_OF_UNDERSTANDING_FETCH,
					Constants.SUCCESS_LETTER_OF_UNDERSTANDING_FETCH, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_LETTER_OF_UNDERSTANDING_FETCH,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	// For Fetching Refund Dropdowns by orgId
	@RequestMapping(value = "/v1/dropdowns/luts/{organizationId}", method = RequestMethod.GET)
	public ResponseEntity<BaseResponse> getLutDropDownData(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse,@PathVariable int organizationId) {
		logger.info("Entry into method: getRefundDropDownData");
		BaseResponse response = new LetterOfUndertakingDropDownResponse();
		try {
			LetterOfUndertakingDropDownVo lutVo = letterOfUndertakingService.getLutDropdownData(organizationId);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((LetterOfUndertakingDropDownResponse) response).setData(lutVo);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_DROP_DOWN_FETCH,
					Constants.SUCCESS_DROP_DOWN_FETCH, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_DROP_DOWN_FETCH,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}		
}


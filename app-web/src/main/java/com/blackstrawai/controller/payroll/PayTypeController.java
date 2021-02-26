package com.blackstrawai.controller.payroll;

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
import com.blackstrawai.helper.PayrollConvertToVoHelper;
import com.blackstrawai.payroll.PayTypeService;
import com.blackstrawai.payroll.PayTypeVo;
import com.blackstrawai.payroll.dropdowns.PayTypeDropDownVo;
import com.blackstrawai.request.JSONObject;
import com.blackstrawai.request.payroll.PayTypeRequest;
import com.blackstrawai.response.payroll.ListPayTypeResponse;
import com.blackstrawai.response.payroll.PayTypeDropDownResponse;
import com.blackstrawai.response.payroll.PayTypeResponse;


@RestController
@CrossOrigin
//This will be part of Settings and Preferences. Hence sp.
@RequestMapping("/decifer/sp/pay_type")
public class PayTypeController extends BaseController{

	@Autowired
	PayTypeService payTypeService;

	private Logger logger = Logger.getLogger(PayTypeController.class);


	@RequestMapping(value = "/v1/pay_types", method = RequestMethod.POST)
	public ResponseEntity<BaseResponse> createPayType(HttpServletRequest httpRequest,HttpServletResponse httpResponse,
			@RequestBody JSONObject<PayTypeRequest> payTypeRequest) {
		logger.info("Entry into method: createPayTypes");
		BaseResponse response = new PayTypeResponse();
		try {
			logger.info("Request Payload:" + generateRequestAndResponseLogPaylod(payTypeRequest));
			PayTypeVo data = PayrollConvertToVoHelper.getInstance()
					.convertPayTypeVoFromPayTypeRequest(payTypeRequest.getData());
			data = payTypeService.createPayType(data);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((PayTypeResponse) response).setData(data);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_PAY_TYPE_CREATED,
					Constants.SUCCESS_PAY_TYPE_CREATED, Constants.PAY_TYPE_CREATED_SUCCESSFULLY);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			if(e.getCause()!=null){
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_PAY_TYPE_CREATED,
						e.getCause().getMessage(), Constants.PAY_TYPE_CREATED_UNSUCCESSFULLY);
			}else{
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_PAY_TYPE_CREATED,
						e.getMessage(), Constants.PAY_TYPE_CREATED_UNSUCCESSFULLY);
			}				
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/v1/pay_types", method = RequestMethod.PUT)
	public ResponseEntity<BaseResponse> updatePayType(HttpServletRequest httpRequest,HttpServletResponse httpResponse,
			@RequestBody JSONObject<PayTypeRequest> payTypeRequest) {
		logger.info("Entry into method: updatePayType");
		BaseResponse response = new PayTypeResponse();
		try {
			logger.info("Request Payload:" + generateRequestAndResponseLogPaylod(payTypeRequest));
			PayTypeVo data = PayrollConvertToVoHelper.getInstance()
					.convertPayTypeVoFromPayTypeRequest(payTypeRequest.getData());
			data = payTypeService.updatePayType(data);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((PayTypeResponse) response).setData(data);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_PAY_TYPE_UPDATED,
					Constants.SUCCESS_PAY_TYPE_UPDATED, Constants.PAY_TYPE_UPDATED_SUCCESSFULLY);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			if(e.getCause()!=null){
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_PAY_TYPE_UPDATED,
						e.getCause().getMessage(), Constants.PAY_TYPE_UPDATED_UNSUCCESSFULLY);
			}else{
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_PAY_TYPE_UPDATED,
						e.getMessage(), Constants.PAY_TYPE_UPDATED_UNSUCCESSFULLY);
			}				
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/v1/pay_types/{organizationId}/{organizationName}/{userId}/{id}/{roleName}/{status}", method = RequestMethod.DELETE)
	public ResponseEntity<BaseResponse> deletePayType(HttpServletRequest httpRequest, 
			HttpServletResponse httpResponse,
			@PathVariable int organizationId, 
			@PathVariable String organizationName,
			@PathVariable String userId, 
			@PathVariable int id,
			@PathVariable String roleName,
			@PathVariable String status) {
		logger.info("Entry into method: deletePayType");
		BaseResponse response = new PayTypeResponse();
		try {
			PayTypeVo data = payTypeService.deletePayType(id,status,userId,roleName);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((PayTypeResponse) response).setData(data);
			if(status.equals(CommonConstants.STATUS_AS_ACTIVE)) {
				response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_PAY_TYPE_ACTIVATED,
						Constants.SUCCESS_PAY_TYPE_ACTIVATED, Constants.PAY_TYPE_DELETED_SUCCESSFULLY);
				logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			}else if(status.equals(CommonConstants.STATUS_AS_INACTIVE)) {
				response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_PAY_TYPE_DEACTIVATED,
						Constants.SUCCESS_PAY_TYPE_DEACTIVATED, Constants.PAY_TYPE_DELETED_SUCCESSFULLY);
				logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			}
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_PAY_TYPE_DELETED,
					e.getMessage(), Constants.PAY_TYPE_DELETED_UNSUCCESSFULLY);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}



	/*@RequestMapping(value = "/v1/pay_types/{organizationId}/{organizationName}", method = RequestMethod.GET)
	public ResponseEntity<BaseResponse> getAllPayTypesOfAnOrganization(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @PathVariable int organizationId,@PathVariable String organizationName) {
		logger.info("Entry into method: getAllPayTypesOfAnOrganization");
		BaseResponse response = new ListPayTypeResponse();
		try {
			List<PayTypeVo> data = payTypeService.getAllPayTypesOfAnOrganization(organizationId);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((ListPayTypeResponse) response).setData(data);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_PAY_TYPE_FETCH,
					Constants.SUCCESS_PAY_TYPE_FETCH, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_PAY_TYPE_FETCH,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}*/
	
	@RequestMapping(value = "/v1/pay_types/{organizationId}/{organizationName}/{userId}/{roleName}", method = RequestMethod.GET)
	public ResponseEntity<BaseResponse> getAllPayTypesOfAnOrganizationForUserAndRole(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @PathVariable int organizationId,@PathVariable String organizationName,
			@PathVariable String userId,@PathVariable String roleName) {
		logger.info("Entry into method: getAllPayTypesOfAnOrganizationForUserAndRole");
		BaseResponse response = new ListPayTypeResponse();
		try {
			List<PayTypeVo> data = payTypeService.getAllPayTypesOfAnOrganizationForUserAndRole(organizationId,userId,roleName);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((ListPayTypeResponse) response).setData(data);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_PAY_TYPE_FETCH,
					Constants.SUCCESS_PAY_TYPE_FETCH, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_PAY_TYPE_FETCH,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}


	@RequestMapping(value = "/v1/pay_types/{organizationId}/{organizationName}/{userId}/{roleName}/{id}", method = RequestMethod.GET)
	public ResponseEntity<BaseResponse> getPayTypeById(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, 
			@PathVariable int organizationId, 
			@PathVariable String organizationName,
			@PathVariable String userId,
			@PathVariable String roleName,
			@PathVariable int id) {
		logger.info("Entry into method: getPayTypeById");
		BaseResponse response = new PayTypeResponse();
		try {
			PayTypeVo data = payTypeService.getPayTypeById(id);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((PayTypeResponse) response).setData(data);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_PAY_TYPE_FETCH,
					Constants.SUCCESS_PAY_TYPE_FETCH, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_PAY_TYPE_FETCH,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}



	@RequestMapping(value = "/v1/dropsdowns/pay_types/{organizationId}")
	public ResponseEntity<BaseResponse> getPayTypeDropDown(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, @PathVariable int organizationId) {
		logger.info("Entry into getPayTypeDropDown");
		BaseResponse response = new PayTypeDropDownResponse();
		try {
			PayTypeDropDownVo data = payTypeService.getPayTypeDropDownData(organizationId);
			setTokens(response, httpServletRequest.getHeader("keyToken"), httpServletRequest.getHeader("valueToken"));
			((PayTypeDropDownResponse) response).setData(data);
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

}

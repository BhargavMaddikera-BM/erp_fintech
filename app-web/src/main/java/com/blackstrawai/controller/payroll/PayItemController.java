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
import com.blackstrawai.payroll.PayItemService;
import com.blackstrawai.payroll.PayItemVo;
import com.blackstrawai.payroll.dropdowns.PayItemDropDownVo;
import com.blackstrawai.request.JSONObject;
import com.blackstrawai.request.payroll.PayItemRequest;
import com.blackstrawai.response.payroll.ListPayItemResponse;
import com.blackstrawai.response.payroll.PayItemDropDownResponse;
import com.blackstrawai.response.payroll.PayItemResponse;


@RestController
@CrossOrigin
//This will be part of Settings and Preferences. Hence sp.
@RequestMapping("/decifer/sp/pay_item")
public class PayItemController extends BaseController{

	@Autowired
	PayItemService payItemService;

	private Logger logger = Logger.getLogger(PayItemController.class);


	@RequestMapping(value = "/v1/pay_items", method = RequestMethod.POST)
	public ResponseEntity<BaseResponse> createPayItem(HttpServletRequest httpRequest,HttpServletResponse httpResponse,
			@RequestBody JSONObject<PayItemRequest> payItemRequest) {
		logger.info("Entry into method: createPayItem");
		BaseResponse response = new PayItemResponse();
		try {
			logger.info("Request Payload:" + generateRequestAndResponseLogPaylod(payItemRequest));
			PayItemVo data = PayrollConvertToVoHelper.getInstance()
					.convertPayItemVoFromPayItemRequest(payItemRequest.getData());
			data = payItemService.createPayItem(data);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((PayItemResponse) response).setData(data);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_PAY_ITEM_CREATED,
					Constants.SUCCESS_PAY_ITEM_CREATED, Constants.PAY_ITEM_CREATED_SUCCESSFULLY);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			if(e.getCause()!=null){
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_PAY_ITEM_CREATED,
						e.getCause().getMessage(), Constants.PAY_ITEM_CREATED_UNSUCCESSFULLY);
			}else{
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_PAY_ITEM_CREATED,
						e.getMessage(), Constants.PAY_ITEM_CREATED_UNSUCCESSFULLY);
			}				
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/v1/pay_items", method = RequestMethod.PUT)
	public ResponseEntity<BaseResponse> updatePayItem(HttpServletRequest httpRequest,HttpServletResponse httpResponse,
			@RequestBody JSONObject<PayItemRequest> payItemRequest) {
		logger.info("Entry into method: updatePayItem");
		BaseResponse response = new PayItemResponse();
		try {
			logger.info("Request Payload:" + generateRequestAndResponseLogPaylod(payItemRequest));
			PayItemVo data = PayrollConvertToVoHelper.getInstance()
					.convertPayItemVoFromPayItemRequest(payItemRequest.getData());
			data = payItemService.updatePayItem(data);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((PayItemResponse) response).setData(data);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_PAY_ITEM_UPDATED,
					Constants.SUCCESS_PAY_ITEM_UPDATED, Constants.PAY_ITEM_UPDATED_SUCCESSFULLY);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			if(e.getCause()!=null){
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_PAY_ITEM_UPDATED,
						e.getCause().getMessage(), Constants.PAY_ITEM_UPDATED_UNSUCCESSFULLY);
			}else{
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_PAY_ITEM_UPDATED,
						e.getMessage(), Constants.PAY_ITEM_UPDATED_UNSUCCESSFULLY);
			}				
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/v1/pay_items/{organizationId}/{organizationName}/{userId}/{id}/{roleName}/{status}", method = RequestMethod.DELETE)
	public ResponseEntity<BaseResponse> deletePayItem(HttpServletRequest httpRequest, 
			HttpServletResponse httpResponse,
			@PathVariable int organizationId, 
			@PathVariable String organizationName,
			@PathVariable String userId, 
			@PathVariable int id,
			@PathVariable String roleName,
			@PathVariable String status) {
		logger.info("Entry into method: deletePayItem");
		BaseResponse response = new PayItemResponse();
		try {
			PayItemVo data = payItemService.deletePayItem(id,status,userId,roleName);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((PayItemResponse) response).setData(data);
			if(status.equals(CommonConstants.STATUS_AS_ACTIVE)) {
				response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_PAY_ITEM_ACTIVATED,
						Constants.SUCCESS_PAY_ITEM_ACTIVATED, Constants.PAY_ITEM_DELETED_SUCCESSFULLY);
				logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			}else if(status.equals(CommonConstants.STATUS_AS_INACTIVE)) {
				response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_PAY_ITEM_DEACTIVATED,
						Constants.SUCCESS_PAY_ITEM_DEACTIVATED, Constants.PAY_ITEM_DELETED_SUCCESSFULLY);
				logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			}
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_PAY_ITEM_DELETED,
					e.getMessage(), Constants.PAY_ITEM_DELETED_UNSUCCESSFULLY);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	/*@RequestMapping(value = "/v1/pay_items/{organizationId}/{organizationName}", method = RequestMethod.GET)
	public ResponseEntity<BaseResponse> getAllPayItemsOfAnOrganization(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @PathVariable int organizationId,@PathVariable String organizationName) {
		logger.info("Entry into method: getAllPayItemsOfAnOrganization");
		BaseResponse response = new ListPayItemResponse();
		try {
			List<PayItemVo> data = payItemService.getAllPayItemsOfAnOrganization(organizationId);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((ListPayItemResponse) response).setData(data);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_PAY_ITEM_FETCH,
					Constants.SUCCESS_PAY_ITEM_FETCH, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_PAY_ITEM_FETCH,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}*/
	
	
	@RequestMapping(value = "/v1/pay_items/{organizationId}/{organizationName}/{userId}/{roleName}", method = RequestMethod.GET)
	public ResponseEntity<BaseResponse> getAllPayItemsOfAnOrganizationForUserAndRole(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @PathVariable int organizationId,@PathVariable String organizationName,
			@PathVariable String userId,@PathVariable String roleName) {
		logger.info("Entry into method: getAllPayItemsOfAnOrganizationForUserAndRole");
		BaseResponse response = new ListPayItemResponse();
		try {
			List<PayItemVo> data = payItemService.getAllPayItemsOfAnOrganizationForUserAndRole(organizationId,userId,roleName);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((ListPayItemResponse) response).setData(data);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_PAY_ITEM_FETCH,
					Constants.SUCCESS_PAY_ITEM_FETCH, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_PAY_ITEM_FETCH,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}





	@RequestMapping(value = "/v1/pay_items/{organizationId}/{organizationName}/{userId}/{roleName}/{id}", method = RequestMethod.GET)
	public ResponseEntity<BaseResponse> getPayItemById(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, 
			@PathVariable int organizationId, 
			@PathVariable String organizationName,
			@PathVariable String userId,
			@PathVariable String roleName,
			@PathVariable int id) {
		logger.info("Entry into method: getPayItemById");
		BaseResponse response = new PayItemResponse();
		try {
			PayItemVo data = payItemService.getPayItemById(id);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((PayItemResponse) response).setData(data);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_PAY_ITEM_FETCH,
					Constants.SUCCESS_PAY_ITEM_FETCH, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_PAY_ITEM_FETCH,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}



	@RequestMapping(value = "/v1/dropsdowns/pay_items/{organizationId}")
	public ResponseEntity<BaseResponse> getPayItemDropDown(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, @PathVariable int organizationId) {
		logger.info("Entry into getPayItemDropDown");
		BaseResponse response = new PayItemDropDownResponse();
		try {
			PayItemDropDownVo data = payItemService.getPayTypeDropDownData(organizationId);
			setTokens(response, httpServletRequest.getHeader("keyToken"), httpServletRequest.getHeader("valueToken"));
			((PayItemDropDownResponse) response).setData(data);
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

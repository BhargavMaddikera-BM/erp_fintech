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
import com.blackstrawai.request.settings.VoucherRequest;
import com.blackstrawai.response.settings.ListVoucherResponse;
import com.blackstrawai.response.settings.VoucherDropDownResponse;
import com.blackstrawai.response.settings.VoucherResponse;
import com.blackstrawai.settings.VoucherDropDownVo;
import com.blackstrawai.settings.VoucherService;
import com.blackstrawai.settings.VoucherVo;

@RestController
@CrossOrigin
//This will be part of Settings and Preferences. Hence sp.
@RequestMapping("/decifer/sp/voucher")
public class VoucherController extends BaseController {

	@Autowired
	VoucherService voucherService;

	private Logger logger = Logger.getLogger(VoucherController.class);


	// For Creating Vouchers
	@RequestMapping(value = "/v1/vouchers", method = RequestMethod.POST)
	public ResponseEntity<BaseResponse> createVoucher(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse,
			@RequestBody JSONObject<VoucherRequest> voucherRequest) {
		logger.info("Entry into method:createVoucher");
		BaseResponse response = new VoucherResponse();
		try {
			logger.info("Request Payload:" + generateRequestAndResponseLogPaylod(voucherRequest));
			VoucherVo voucherVo = SettingsConvertToVoHelper.getInstance()
					.convertVoucherVoFromVoucherRequest(voucherRequest.getData());
			voucherVo = voucherService.createVoucher(voucherVo);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((VoucherResponse) response).setData(voucherVo);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_VOUCHER_CREATED,
					Constants.SUCCESS_VOUCHER_CREATED, Constants.VOUCHER_CREATED_SUCCESSFULLY);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			if(e.getCause()!=null){
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_VOUCHER_CREATED,
						e.getCause().getMessage(), Constants.VOUCHER_CREATED_UNSUCCESSFULLY);
			}else{
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_VOUCHER_CREATED,
						e.getMessage(), Constants.VOUCHER_CREATED_UNSUCCESSFULLY);
			}				
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}


	// For Fetching all Voucher details
	/*
		@RequestMapping(value = "/v1/vouchers/{organizationId}/{organizationName}", method = RequestMethod.GET)
		public ResponseEntity<BaseResponse> getAllvouchersOfAnOrganization(HttpServletRequest httpRequest,
				HttpServletResponse httpResponse, @PathVariable int organizationId,@PathVariable String organizationName) {
			logger.info("Entry into method:getAllvouchersOfAnOrganization");
			BaseResponse response = new ListVoucherResponse();
			try {
				List<VoucherVo> listAllVouchers = voucherService
						.getAllvouchersOfAnOrganization(organizationId);
				setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
				((ListVoucherResponse) response).setData(listAllVouchers);
				response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_VOUCHER_FETCH,
						Constants.SUCCESS_VOUCHER_FETCH, Constants.SUCCESS_DURING_GET);
				logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
				return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
			} catch (ApplicationException e) {
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_VOUCHER_FETCH,
						e.getMessage(), Constants.FAILURE_DURING_GET);
				logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
				logger.info(response);
				return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
	 */
	@RequestMapping(value = "/v1/vouchers/{organizationId}/{organizationName}/{userId}/{roleName}", method = RequestMethod.GET)
	public ResponseEntity<BaseResponse> getAllvouchersOfAnOrganizationForUserAndRole(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @PathVariable int organizationId,@PathVariable String organizationName,
			@PathVariable String userId,@PathVariable String roleName) {
		logger.info("Entry into method:getAllvouchersOfAnOrganizationForUserAndRole");
		BaseResponse response = new ListVoucherResponse();
		try {
			List<VoucherVo> listAllVouchers = voucherService
					.getAllvouchersOfAnOrganizationForUserAndRole(organizationId,userId,roleName);;
					setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
					((ListVoucherResponse) response).setData(listAllVouchers);
					response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_VOUCHER_FETCH,
							Constants.SUCCESS_VOUCHER_FETCH, Constants.SUCCESS_DURING_GET);
					logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
					return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_VOUCHER_FETCH,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}


	// For Fetching PaymentTerms by Id

	@RequestMapping(value = "/v1/vouchers/{organizationId}/{organizationName}/{userId}/{roleName}/{id}", method = RequestMethod.GET)
	public ResponseEntity<BaseResponse> getVoucherById(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, 
			@PathVariable int organizationId, 
			@PathVariable String organizationName,
			@PathVariable String userId,
			@PathVariable String roleName,
			@PathVariable int id) {
		logger.info("Entry into method:getShippingPreferenceById");
		BaseResponse response = new VoucherResponse();
		try {
			VoucherVo voucherVo = voucherService.getVoucherById(id);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((VoucherResponse) response).setData(voucherVo);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_VOUCHER_FETCH,
					Constants.SUCCESS_VOUCHER_FETCH, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_VOUCHER_FETCH,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// For Deleting Vouchers


	@RequestMapping(value = "/v1/vouchers/{organizationId}/{organizationName}/{userId}/{id}/{roleName}/{status}", method = RequestMethod.DELETE)
	public ResponseEntity<BaseResponse> deleteVoucher(HttpServletRequest httpRequest, 
			HttpServletResponse httpResponse,
			@PathVariable int organizationId, 
			@PathVariable String organizationName,
			@PathVariable String userId, 
			@PathVariable int id,
			@PathVariable String roleName,
			@PathVariable String status) {
		logger.info("Entry into method:deleteVoucher");
		BaseResponse response = new VoucherResponse();
		try {
			VoucherVo voucherVo = voucherService.deleteVoucher(id,status,userId,roleName);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((VoucherResponse) response).setData(voucherVo);
			if(status.equals(CommonConstants.STATUS_AS_ACTIVE)) {
				response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_VOUCHER_ACTIVATED,
						Constants.SUCCESS_VOUCHER_ACTIVATED, Constants.VOUCHER_DELETED_SUCCESSFULLY);
				logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			}else if(status.equals(CommonConstants.STATUS_AS_INACTIVE)) {
				response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_VOUCHER_DEACTIVATED,
						Constants.SUCCESS_VOUCHER_DEACTIVATED, Constants.VOUCHER_DELETED_SUCCESSFULLY);
				logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			}
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_VOUCHER_DELETED,
					e.getMessage(), Constants.VOUCHER_DELETED_UNSUCCESSFULLY);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}


	// For Update Voucher

	@RequestMapping(value = "/v1/vouchers", method = RequestMethod.PUT)
	public ResponseEntity<BaseResponse> updateVoucher(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @RequestBody JSONObject<VoucherRequest> voucherRequest) {
		logger.info("Entry into method:updateVoucher");
		BaseResponse response = new VoucherResponse();
		try {
			logger.info("Request Payload:" + generateRequestAndResponseLogPaylod(voucherRequest));
			VoucherVo voucherVo = SettingsConvertToVoHelper.getInstance()
					.convertVoucherVoFromVoucherRequest(voucherRequest.getData());
			voucherVo= voucherService.updateVoucher(voucherVo);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((VoucherResponse) response).setData(voucherVo);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_VOUCHER_UPDATED,
					Constants.SUCCESS_VOUCHER_UPDATED, Constants.VOUCHER_UPDATED_SUCCESSFULLY);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_VOUCHER_UPDATED,
					e.getMessage(), Constants.VOUCHER_UPDATED_UNSUCCESSFULLY);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/v1/dropsdowns/vouchers")
	public ResponseEntity<BaseResponse> getVoucherDropDown(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) {
		logger.info("Entry into getVoucherDropDown");
		BaseResponse response = new VoucherDropDownResponse();
		try {
			VoucherDropDownVo data = voucherService.getVoucherDropDownData();
			setTokens(response, httpServletRequest.getHeader("keyToken"), httpServletRequest.getHeader("valueToken"));
			((VoucherDropDownResponse) response).setData(data);
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

package com.blackstrawai.controller.vendorsettings;

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
import com.blackstrawai.common.Constants;
import com.blackstrawai.helper.VendorSettingsConvertToVoHelper;
import com.blackstrawai.request.JSONObject;
import com.blackstrawai.request.vendorsettings.VamVendorGroupRequest;
import com.blackstrawai.request.vendorsettings.VamVendorGroupSettingsRequest;
import com.blackstrawai.request.vendorsettings.VamVendorRequest;
import com.blackstrawai.request.vendorsettings.VamVendorSettingsRequest;
import com.blackstrawai.response.keycontact.vendor.VendorResponse;
import com.blackstrawai.response.vendorsettings.VamDropDownResponse;
import com.blackstrawai.response.vendorsettings.VamVendorGroupResponse;
import com.blackstrawai.response.vendorsettings.VamVendorGroupSettingsResponse;
import com.blackstrawai.response.vendorsettings.VamVendorResponse;
import com.blackstrawai.response.vendorsettings.VamVendorSettingsResponse;
import com.blackstrawai.vendorsettings.VamBasicVendorGroupVo;
import com.blackstrawai.vendorsettings.VamBasicVendorVo;
import com.blackstrawai.vendorsettings.VamVendorGroupSettingsVo;
import com.blackstrawai.vendorsettings.VamVendorGroupVo;
import com.blackstrawai.vendorsettings.VamVendorSettingsVo;
import com.blackstrawai.vendorsettings.VamVendorVo;
import com.blackstrawai.vendorsettings.VendorAccessMgmtService;
import com.blackstrawai.vendorsettings.dropdowns.VamDropDownVo;

@RestController
@CrossOrigin
@RequestMapping("/decifer/vendorportal/vam")
public class VendorAccessMgmtController extends BaseController {

	private Logger logger = Logger.getLogger(VendorAccessMgmtController.class);

	@Autowired
	VendorAccessMgmtService vendorAccessMgmtService;

	// Drop downs for vendor and vendor Group in vendor access management
	@RequestMapping(value = "/v1/dropsdowns/{organizationId}")
	public ResponseEntity<BaseResponse> getVendorAccessMgmtDropDown(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, @PathVariable int organizationId) {
		logger.info("Entry into getVendorAccessMgmtDropDown");
		BaseResponse response = new VamDropDownResponse();
		try {
			VamDropDownVo data = vendorAccessMgmtService.getVendorAccessMgmtDropDown(organizationId);
			setTokens(response, httpServletRequest.getHeader("keyToken"), httpServletRequest.getHeader("valueToken"));
			((VamDropDownResponse) response).setData(data);
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

	// create settings for vendor

	@RequestMapping(value = "/v1/vendor", method = RequestMethod.POST)
	public ResponseEntity<BaseResponse> createVendorSettings(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @RequestBody JSONObject<VamVendorRequest> vamVendorRequest) {
		logger.info("Entry into method: createVendorSettings");
		BaseResponse response = new VendorResponse();
		try {
			logger.info("Request Payload:" + generateRequestAndResponseLogPaylod(vamVendorRequest));
			VamVendorVo vamVendorVo = VendorSettingsConvertToVoHelper.getInstance()
					.convertVamVendorVoFromVamVendorRequest(vamVendorRequest.getData());
			vendorAccessMgmtService.createVendorSettings(vamVendorVo);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_VENDOR_SETTINGS_CREATED,
					Constants.SUCCESS_VENDOR_SETTINGS_CREATED, Constants.VENDOR_SETTINGS_CREATED_SUCCESSFULLY);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			logger.error("Error Payload:" + e.getMessage());
			if (e.getCause() != null) {
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_VENDOR_SETTINGS_CREATED,
						e.getCause().getMessage(), Constants.VENDOR_SETTINGS_CREATED_UNSUCCESSFULLY);
			} else {
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_VENDOR_SETTINGS_CREATED,
						e.getMessage(), Constants.VENDOR_CREATED_UNSUCCESSFULLY);
			}

			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	// create settings for vendor Group

	@RequestMapping(value = "/v1/vendor_group", method = RequestMethod.POST)
	public ResponseEntity<BaseResponse> createVendorGroupSettings(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @RequestBody JSONObject<VamVendorGroupRequest> vamVendorGroupRequest) {
		logger.info("Entry into method: createVendorGroupSettings");
		BaseResponse response = new VendorResponse();
		try {
			logger.info("Request Payload:" + generateRequestAndResponseLogPaylod(vamVendorGroupRequest));
			VamVendorGroupVo vamVendorGroupVo = VendorSettingsConvertToVoHelper.getInstance()
					.convertVamVendorGroupVoFromVamVendorGroupRequest(vamVendorGroupRequest.getData());
			vendorAccessMgmtService.createVendorGroupSettings(vamVendorGroupVo);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_VENDOR_GROUP_SETTINGS_CREATED,
					Constants.SUCCESS_VENDOR_GROUP_SETTINGS_CREATED,
					Constants.VENDOR_GROUP_SETTINGS_CREATED_SUCCESSFULLY);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			logger.error("Error Payload:" + e.getMessage());
			if (e.getCause() != null) {
				response = constructResponse(response, Constants.FAILURE,
						Constants.FAILURE_VENDOR_GROUP_SETTINGS_CREATED, e.getCause().getMessage(),
						Constants.VENDOR_GROUP_SETTINGS_CREATED_UNSUCCESSFULLY);
			} else {
				response = constructResponse(response, Constants.FAILURE,
						Constants.FAILURE_VENDOR_GROUP_SETTINGS_CREATED, e.getMessage(),
						Constants.VENDOR_GROUP_SETTINGS_CREATED_UNSUCCESSFULLY);
			}

			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	// get vendors settings

	@RequestMapping(value = "/v1/vendor/{organizationId}", method = RequestMethod.GET)
	public ResponseEntity<BaseResponse> getVendorSettings(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @PathVariable int organizationId) {
		logger.info("Entry into method: getVendorSettings");
		BaseResponse response = new VamVendorResponse();
		try {
			logger.info("Request Payload:" + generateRequestAndResponseLogPaylod(organizationId));
			List<VamBasicVendorVo> vendorList = vendorAccessMgmtService.getVendorSettings(organizationId);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((VamVendorResponse) response).setData(vendorList);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_VENDOR_SETTINS_FETCH,
					Constants.SUCCESS_VENDOR_SETTINS_FETCH, Constants.VENDOR_SETTINGS_FETCHED_SUCCESSFULLY);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			logger.error("Error Payload:" + e.getMessage());
			if (e.getCause() != null) {
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_VENDOR_SETTINGS_FETCH,
						e.getCause().getMessage(), Constants.VENDOR_SETTINGS_FETCH_UNSUCCESSFULLY);
			} else {
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_VENDOR_SETTINGS_FETCH,
						e.getMessage(), Constants.VENDOR_SETTINGS_FETCH_UNSUCCESSFULLY);
			}

			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	// get vendor groups settings

	@RequestMapping(value = "/v1/vendor_group/{organizationId}", method = RequestMethod.GET)
	public ResponseEntity<BaseResponse> getVendorGroupSettings(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @PathVariable int organizationId) {
		logger.info("Entry into method: getVendorGroupSettings");
		BaseResponse response = new VamVendorGroupResponse();
		try {
			logger.info("Request Payload:" + generateRequestAndResponseLogPaylod(organizationId));
			List<VamBasicVendorGroupVo> vendorGroupList = vendorAccessMgmtService
					.getVendorGroupSettings(organizationId);
			((VamVendorGroupResponse) response).setData(vendorGroupList);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_VENDOR_GROUP_SETTINGS_FETCH,
					Constants.SUCCESS_VENDOR_GROUP_SETTINGS_FETCH,
					Constants.VENDOR_GROUP_SETTINGS_FETCHED_SUCCESSFULLY);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			logger.error("Error Payload:" + e.getMessage());
			if (e.getCause() != null) {
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_VENDOR_GROUP_SETTINGS_FETCH,
						e.getCause().getMessage(), Constants.VENDOR_GROUP_SETTINGS_FETCHED_UNSUCCESSFULLY);
			} else {
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_VENDOR_GROUP_SETTINGS_FETCH,
						e.getMessage(), Constants.VENDOR_GROUP_SETTINGS_FETCHED_UNSUCCESSFULLY);
			}

			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	// Active and DeActive vendor settings

	@RequestMapping(value = "/v1/vendor/{vendorId}/{status}", method = RequestMethod.PUT)
	public ResponseEntity<BaseResponse> ActiveAndDeactiveVendorSettings(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @PathVariable int vendorId, @PathVariable String status) {
		logger.info("Entry into method: ActiveAndDeactiveVendorSettings");
		BaseResponse response = new VamVendorGroupResponse();
		try {
			vendorAccessMgmtService.ActiveAndDeactiveVendorSettings(vendorId, status);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			if (status.equals("ACT")) {
				response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_VENDOR_SETTINGS_ACTIVATED,
						Constants.SUCCESS_VENDOR_SETTINGS_ACTIVATED, Constants.VENDOR_SETTINGS_ACTIVATED_SUCCESSFULLY);
				logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			} else {
				response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_VENDOR_SETTINGS_DEACTIVATED,
						Constants.SUCCESS_VENDOR_SETTINGS_DEACTIVATED,
						Constants.VENDOR_SETTINGS_DEACTIVATED_SUCCESSFULLY);
				logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			}
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			logger.error("Error Payload:" + e.getMessage());
			if (e.getCause() != null) {
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_VENDOR_SETTINGS_UPDATE,
						e.getCause().getMessage(), Constants.VENDOR_SETTINGS_UPDATED_UNSUCCESSFULLY);
			} else {
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_VENDOR_SETTINGS_UPDATE,
						e.getMessage(), Constants.VENDOR_SETTINGS_UPDATED_UNSUCCESSFULLY);
			}

			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	// Active and DeActive vendor Group settings

	@RequestMapping(value = "/v1/vendor_group/{vendorGroupId}/{status}", method = RequestMethod.PUT)
	public ResponseEntity<BaseResponse> ActiveAndDeactiveVendorGroupSettings(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @PathVariable int vendorGroupId, @PathVariable String status) {
		logger.info("Entry into method: ActiveAndDeactiveVendorGroupSettings");
		BaseResponse response = new VamVendorGroupResponse();
		try {
			vendorAccessMgmtService.ActiveAndDeactiveVendorGroupSettings(vendorGroupId, status);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			if (status.equals("ACT")) {
				response = constructResponse(response, Constants.SUCCESS,
						Constants.SUCCESS_VENDOR_GROUP_SETTINGS_ACTIVATED,
						Constants.SUCCESS_VENDOR_GROUP_SETTINGS_ACTIVATED,
						Constants.VENDOR_GROUP_SETTINGS_ACTIVATED_SUCCESSFULLY);
			} else {
				response = constructResponse(response, Constants.SUCCESS,
						Constants.SUCCESS_VENDOR_GROUP_SETTINGS_DEACTIVATED,
						Constants.SUCCESS_VENDOR_GROUP_SETTINGS_DEACTIVATED,
						Constants.VENDOR_GROUP_SETTINGS_DEACTIVATED_SUCCESSFULLY);
			}

			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			logger.error("Error Payload:" + e.getMessage());
			if (e.getCause() != null) {
				response = constructResponse(response, Constants.FAILURE,
						Constants.FAILURE_VENDOR_GROUP_SETTINGS_UPDATE, e.getCause().getMessage(),
						Constants.VENDOR_GROUP_SETTINGS_UPDATED_UNSUCCESSFULLY);
			} else {
				response = constructResponse(response, Constants.FAILURE,
						Constants.FAILURE_VENDOR_GROUP_SETTINGS_UPDATE, e.getMessage(),
						Constants.VENDOR_GROUP_SETTINGS_UPDATED_UNSUCCESSFULLY);
			}

			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	// get vendor settings By Id
	@RequestMapping(value = "/v1/vendor/{organizationId}/{vendorId}", method = RequestMethod.GET)
	public ResponseEntity<BaseResponse> getVendorSettingsById(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @PathVariable int organizationId, @PathVariable int vendorId) {
		logger.info("Entry into method: getVendorSettingsById");
		BaseResponse response = new VamVendorSettingsResponse();
		try {
			VamVendorSettingsVo vendor = vendorAccessMgmtService.getVendorSettingsById(vendorId);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((VamVendorSettingsResponse) response).setData(vendor);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_VENDOR_SETTINS_FETCH,
					Constants.SUCCESS_VENDOR_SETTINS_FETCH, Constants.VENDOR_SETTINGS_FETCHED_SUCCESSFULLY);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			logger.error("Error Payload:" + e.getMessage());
			if (e.getCause() != null) {
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_VENDOR_SETTINGS_FETCH,
						e.getCause().getMessage(), Constants.VENDOR_SETTINGS_FETCH_UNSUCCESSFULLY);
			} else {
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_VENDOR_SETTINGS_FETCH,
						e.getMessage(), Constants.VENDOR_SETTINGS_FETCH_UNSUCCESSFULLY);
			}

			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	// get vendor Group settings By Id
	@RequestMapping(value = "/v1/vendor_group/{organizationId}/{vendorGroupId}", method = RequestMethod.GET)
	public ResponseEntity<BaseResponse> getVendorGroupSettingsById(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @PathVariable int organizationId, @PathVariable int vendorGroupId) {
		logger.info("Entry into method: getVendorGroupSettingsById");
		BaseResponse response = new VamVendorGroupSettingsResponse();
		try {
			VamVendorGroupSettingsVo vendorGroupSettings = vendorAccessMgmtService
					.getVendorGroupSettingsById(organizationId, vendorGroupId);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((VamVendorGroupSettingsResponse) response).setData(vendorGroupSettings);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_VENDOR_GROUP_SETTINGS_FETCH,
					Constants.SUCCESS_VENDOR_GROUP_SETTINGS_FETCH,
					Constants.VENDOR_GROUP_SETTINGS_FETCHED_SUCCESSFULLY);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			logger.error("Error Payload:" + e.getMessage());
			if (e.getCause() != null) {
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_VENDOR_GROUP_SETTINGS_FETCH,
						e.getCause().getMessage(), Constants.VENDOR_GROUP_SETTINGS_FETCHED_UNSUCCESSFULLY);
			} else {
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_VENDOR_GROUP_SETTINGS_FETCH,
						e.getMessage(), Constants.VENDOR_GROUP_SETTINGS_FETCHED_UNSUCCESSFULLY);
			}

			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	// Edit vendor settings

	@RequestMapping(value = "/v1/vendor", method = RequestMethod.PUT)
	public ResponseEntity<BaseResponse> editVendorSettings(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse,
			@RequestBody JSONObject<VamVendorSettingsRequest> vamVendorSettingsRequest) {
		logger.info("Entry into method: editVendorSettings");
		BaseResponse response = new VamVendorGroupResponse();
		try {
			logger.info("Request Payload:" + generateRequestAndResponseLogPaylod(vamVendorSettingsRequest));
			VamVendorSettingsVo vamVendorSettingsVo = VendorSettingsConvertToVoHelper.getInstance()
					.convertVamVendorSettingsVoFromVamVendorSettingsRequest(vamVendorSettingsRequest.getData());
			vendorAccessMgmtService.editVendorSettings(vamVendorSettingsVo);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_VENDOR_SETTINGS_UPDATED,
					Constants.SUCCESS_VENDOR_SETTINGS_UPDATED, Constants.VENDOR_SETTINGS_UPDATED_SUCCESSFULLY);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			logger.error("Error Payload:" + e.getMessage());
			if (e.getCause() != null) {
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_VENDOR_SETTINGS_UPDATE,
						e.getCause().getMessage(), Constants.VENDOR_SETTINGS_UPDATED_UNSUCCESSFULLY);
			} else {
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_VENDOR_SETTINGS_UPDATE,
						e.getMessage(), Constants.VENDOR_SETTINGS_UPDATED_UNSUCCESSFULLY);
			}

			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	// Edit vendor Group settings

	@RequestMapping(value = "/v1/vendor_group", method = RequestMethod.PUT)
	public ResponseEntity<BaseResponse> editVendorGroupSettings(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse,
			@RequestBody JSONObject<VamVendorGroupSettingsRequest> vamVendorGroupSettingsRequest) {
		logger.info("Entry into method: editVendorGroupSettings");
		BaseResponse response = new VamVendorGroupResponse();
		try {
			logger.info("Request Payload:" + generateRequestAndResponseLogPaylod(vamVendorGroupSettingsRequest));
			VamVendorGroupSettingsVo vamVendorGroupSettingsVo = VendorSettingsConvertToVoHelper.getInstance()
					.convertVamVendorGroupSettingsVoFromVamVendorGroupSettingsRequest(
							vamVendorGroupSettingsRequest.getData());
			vendorAccessMgmtService.editVendorGroupSettings(vamVendorGroupSettingsVo);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_VENDOR_GROUP_SETTINGS_UPDATED,
					Constants.SUCCESS_VENDOR_GROUP_SETTINGS_UPDATED,
					Constants.VENDOR_GROUP_SETTINGS_UPDATED_SUCCESSFULLY);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			logger.error("Error Payload:" + e.getMessage());
			if (e.getCause() != null) {
				response = constructResponse(response, Constants.FAILURE,
						Constants.FAILURE_VENDOR_GROUP_SETTINGS_UPDATE, e.getCause().getMessage(),
						Constants.VENDOR_GROUP_SETTINGS_UPDATED_UNSUCCESSFULLY);
			} else {
				response = constructResponse(response, Constants.FAILURE,
						Constants.FAILURE_VENDOR_GROUP_SETTINGS_UPDATE, e.getMessage(),
						Constants.VENDOR_GROUP_SETTINGS_UPDATED_UNSUCCESSFULLY);
			}

			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

}

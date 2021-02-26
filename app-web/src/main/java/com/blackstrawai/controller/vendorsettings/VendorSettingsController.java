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

import com.blackstrawai.common.BaseController;
import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.common.Constants;
import com.blackstrawai.helper.VendorSettingsConvertToVoHelper;
import com.blackstrawai.request.JSONObject;
import com.blackstrawai.request.vendorsettings.PredefinedSettingsRequest;
import com.blackstrawai.response.vendorsettings.GeneralSettingsListResponse;
import com.blackstrawai.response.vendorsettings.PredefinedSettingListResponse;
import com.blackstrawai.response.vendorsettings.PredefinedSettingsResponse;
import com.blackstrawai.vendorsettings.GeneralSettingsVo;
import com.blackstrawai.vendorsettings.PredefinedSettingsVo;
import com.blackstrawai.vendorsettings.VendorSettingsService;

@RestController
@CrossOrigin
@RequestMapping("/decifer/vendorportal/setting")
public class VendorSettingsController extends BaseController {

	private Logger logger = Logger.getLogger(VendorSettingsController.class);

	@Autowired
	private VendorSettingsService vendorSettingsService;

	@RequestMapping(value = "/v1/predefinedsettings/list/{organizationId}")
	public ResponseEntity<BaseResponse> getAllpredefinedSettings(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, @PathVariable Integer organizationId) {
		logger.info("Entry into getAllpredefinedSettings");
		BaseResponse response = new PredefinedSettingListResponse();
		try {
			if (organizationId != null) {
				List<PredefinedSettingsVo> listVos = vendorSettingsService.getPredefinedSettingsList(organizationId);
				logger.info("listSize" + listVos.size());
				setTokens(response, httpServletRequest.getHeader("keyToken"),
						httpServletRequest.getHeader("valueToken"));
				((PredefinedSettingListResponse) response).setData(listVos.size() > 0 ? listVos : null);
				response = constructResponse(response, Constants.SUCCESS,
						Constants.SUCCESS_VENDOR_PREDEFINED_SETTINGS_FETCH,
						Constants.SUCCESS_VENDOR_PREDEFINED_SETTINGS_FETCH, Constants.SUCCESS_DURING_GET);
				logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
				return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
			} else {
				response = constructResponse(response, Constants.FAILURE,
						Constants.FAILURE_VENDOR_PREDEFINED_SETTINGS_FETCH,
						Constants.FAILURE_IN_USER_ORGANIZATION_REQUEST_VALIDATION, Constants.FAILURE_DURING_GET);
				logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
				return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch (Exception e) {
			response = constructResponse(response, Constants.FAILURE,
					Constants.FAILURE_VENDOR_PREDEFINED_SETTINGS_FETCH, e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@RequestMapping(value = "/v1/predefinedsettings", method = RequestMethod.POST)
	public ResponseEntity<BaseResponse> createPredefinedSetting(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @RequestBody JSONObject<PredefinedSettingsRequest> settingRequest) {
		logger.info("Entered Into createPredefinedSetting");
		BaseResponse response = new PredefinedSettingsResponse();
		if (settingRequest.getData().getIsSuperAdmin() != null && settingRequest.getData().getUserId() != null
				&& settingRequest.getData().getOrganizationId() != null) {
			try {
				logger.info("Request Payload:" + generateRequestAndResponseLogPaylod(settingRequest));
				PredefinedSettingsVo settingsVo = VendorSettingsConvertToVoHelper.getInstance()
						.convertPredefinedSettingsVoFromRequest(settingRequest.getData());
				vendorSettingsService.createPredefinedSetting(settingsVo);
				setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
				response = constructResponse(response, Constants.SUCCESS,
						Constants.VENDOR_PREDEFINED_SETTINGS_CREATED_SUCCESSFULLY,
						Constants.SUCCESS_VENDOR_PREDEFINED_SETTINGS_CREATED,
						Constants.VENDOR_PREDEFINED_SETTINGS_CREATED_SUCCESSFULLY);
				logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
				return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
			} catch (Exception e) {
				if (e.getCause() != null) {
					response = constructResponse(response, Constants.FAILURE,
							Constants.FAILURE_VENDOR_PREDEFINED_SETTINGS_CREATED, e.getCause().getMessage(),
							Constants.VENDOR_PREDEFINED_SETTINGS_CREATED_UNSUCCESSFULLY);
				} else {
					response = constructResponse(response, Constants.FAILURE,
							Constants.FAILURE_VENDOR_PREDEFINED_SETTINGS_CREATED, e.getMessage(),
							Constants.VENDOR_PREDEFINED_SETTINGS_CREATED_UNSUCCESSFULLY);
				}
				logger.error("Error Payload:", e);
				logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
				return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} else {
			response = constructResponse(response, Constants.FAILURE,
					Constants.FAILURE_VENDOR_PREDEFINED_SETTINGS_CREATED,
					Constants.FAILURE_IN_USER_ORGANIZATION_REQUEST_VALIDATION,
					Constants.VENDOR_PREDEFINED_SETTINGS_CREATED_UNSUCCESSFULLY);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/v1/predefinedsettings", method = RequestMethod.PUT)
	public ResponseEntity<BaseResponse> updatePredefinedSetting(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @RequestBody JSONObject<PredefinedSettingsRequest> settingRequest) {
		logger.info("Entered Into updatePredefinedSetting");
		BaseResponse response = new PredefinedSettingsResponse();
		if (settingRequest.getData().getIsSuperAdmin() != null && settingRequest.getData().getUserId() != null
				&& settingRequest.getData().getOrganizationId() != null) {
			try {
				logger.info("Request Payload:" + generateRequestAndResponseLogPaylod(settingRequest));
				PredefinedSettingsVo settingsVo = VendorSettingsConvertToVoHelper.getInstance()
						.convertPredefinedSettingsVoFromRequest(settingRequest.getData());
				vendorSettingsService.updatePredefinedSetting(settingsVo);
				setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
				response = constructResponse(response, Constants.SUCCESS,
						Constants.VENDOR_PREDEFINED_SETTINGS_UPDATED_SUCCESSFULLY,
						Constants.SUCCESS_VENDOR_PREDEFINED_SETTINGS_UPDATED,
						Constants.VENDOR_PREDEFINED_SETTINGS_UPDATED_SUCCESSFULLY);
				logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
				return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
			} catch (Exception e) {
				if (e.getCause() != null) {
					response = constructResponse(response, Constants.FAILURE,
							Constants.FAILURE_VENDOR_PREDEFINED_SETTINGS_UPDATED, e.getCause().getMessage(),
							Constants.VENDOR_PREDEFINED_SETTINGS_UPDATED_UNSUCCESSFULLY);
				} else {
					response = constructResponse(response, Constants.FAILURE,
							Constants.FAILURE_VENDOR_PREDEFINED_SETTINGS_UPDATED, e.getMessage(),
							Constants.VENDOR_PREDEFINED_SETTINGS_UPDATED_UNSUCCESSFULLY);
				}
				logger.error("Error Payload:", e);
				logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
				return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} else {
			response = constructResponse(response, Constants.FAILURE,
					Constants.FAILURE_VENDOR_PREDEFINED_SETTINGS_UPDATED,
					Constants.FAILURE_IN_USER_ORGANIZATION_REQUEST_VALIDATION,
					Constants.VENDOR_PREDEFINED_SETTINGS_UPDATED_UNSUCCESSFULLY);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/v1/predefinedsettings/{organizationId}/{id}")
	public ResponseEntity<BaseResponse> getPredefinedSettingById(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @PathVariable Integer organizationId, @PathVariable Integer id) {
		logger.info("Entry into getPredefinedSettingById");
		BaseResponse response = new PredefinedSettingsResponse();
		try {
			PredefinedSettingsVo predefinedSetting = vendorSettingsService.getPredefinedSettingById(id, organizationId);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((PredefinedSettingsResponse) response).setData(predefinedSetting);
			response = constructResponse(response, Constants.SUCCESS,
					Constants.SUCCESS_VENDOR_PREDEFINED_SETTINGS_FETCH,
					Constants.SUCCESS_VENDOR_PREDEFINED_SETTINGS_FETCH, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (Exception e) {
			response = constructResponse(response, Constants.FAILURE,
					Constants.FAILURE_VENDOR_PREDEFINED_SETTINGS_FETCH, e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/v1/predefinedsetting/{organizationId}")
	public ResponseEntity<BaseResponse> getPredefinedSettingsDefaultFields(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, @PathVariable Integer organizationId) {
		logger.info("Entry into getPredefinedSettingsDefaultFields");
		BaseResponse response = new PredefinedSettingsResponse();
		try {
			if (organizationId != null) {
				PredefinedSettingsVo predefinedSettingsVo = vendorSettingsService
						.getPredefinedSettingsDefaultValues(organizationId);
				setTokens(response, httpServletRequest.getHeader("keyToken"),
						httpServletRequest.getHeader("valueToken"));
				((PredefinedSettingsResponse) response).setData(predefinedSettingsVo);
				response = constructResponse(response, Constants.SUCCESS,
						Constants.SUCCESS_VENDOR_PREDEFINED_SETTINGS_FETCH,
						Constants.SUCCESS_VENDOR_PREDEFINED_SETTINGS_FETCH, Constants.SUCCESS_DURING_GET);
				return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
			} else {
				response = constructResponse(response, Constants.FAILURE,
						Constants.FAILURE_VENDOR_PREDEFINED_SETTINGS_FETCH,
						Constants.FAILURE_IN_USER_ORGANIZATION_REQUEST_VALIDATION, Constants.FAILURE_DURING_GET);
				logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
				return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch (Exception e) {
			response = constructResponse(response, Constants.FAILURE,
					Constants.FAILURE_VENDOR_PREDEFINED_SETTINGS_FETCH, e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@RequestMapping(value = "/v1/predefinedsetting/setDefault/{organizationId}/{settingId}")
	public ResponseEntity<BaseResponse> setAsDefaultForPredefinedSetting(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, @PathVariable Integer organizationId,
			@PathVariable Integer settingId) {
		logger.info("Entry into setAsDefaultForPredefinedSetting");
		BaseResponse response = new PredefinedSettingsResponse();
		try {
			if (organizationId != null && settingId != null) {
				vendorSettingsService.setAsDefaultForPredefinedSettings(settingId, organizationId);
				setTokens(response, httpServletRequest.getHeader("keyToken"),
						httpServletRequest.getHeader("valueToken"));
				response = constructResponse(response, Constants.SUCCESS,
						Constants.SUCCESS_VENDOR_PREDEFINED_SETTINGS_UPDATED,
						Constants.SUCCESS_VENDOR_PREDEFINED_SETTINGS_UPDATED,
						Constants.VENDOR_GENERAL_SETTINGS_UPDATED_UNSUCCESSFULLY);
				return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
			} else {
				response = constructResponse(response, Constants.FAILURE,
						Constants.FAILURE_VENDOR_PREDEFINED_SETTINGS_UPDATED,
						Constants.FAILURE_IN_USER_ORGANIZATION_REQUEST_VALIDATION,
						Constants.VENDOR_PREDEFINED_SETTINGS_UPDATED_UNSUCCESSFULLY);
				logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
				return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch (Exception e) {
			response = constructResponse(response, Constants.FAILURE,
					Constants.FAILURE_VENDOR_PREDEFINED_SETTINGS_UPDATED, e.getMessage(),
					Constants.VENDOR_PREDEFINED_SETTINGS_UPDATED_UNSUCCESSFULLY);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@RequestMapping(value = "/v1/predefinedsetting/{organizationId}/{settingId}", method = RequestMethod.PUT)
	public ResponseEntity<BaseResponse> deActivatePredefinedSetting(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, @PathVariable Integer organizationId,
			@PathVariable Integer settingId) {
		logger.info("Entry into deActivatePredefinedSetting");
		BaseResponse response = new PredefinedSettingsResponse();
		try {
			if (organizationId != null && settingId != null) {
				vendorSettingsService.deactivatePredefinedSettings(settingId, organizationId);
				setTokens(response, httpServletRequest.getHeader("keyToken"),
						httpServletRequest.getHeader("valueToken"));
				response = constructResponse(response, Constants.SUCCESS,
						Constants.SUCCESS_VENDOR_PREDEFINED_SETTINGS_DELETED,
						Constants.SUCCESS_VENDOR_PREDEFINED_SETTINGS_DELETED,
						Constants.VENDOR_PREDEFINED_SETTINGS_DELETED_SUCCESSFULLY);
				return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
			} else {
				response = constructResponse(response, Constants.FAILURE,
						Constants.FAILURE_VENDOR_PREDEFINED_SETTINGS_DELETED,
						Constants.FAILURE_IN_USER_ORGANIZATION_REQUEST_VALIDATION,
						Constants.VENDOR_PREDEFINED_SETTINGS_DELETED_UNSUCCESSFULLY);
				logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
				return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch (Exception e) {
			response = constructResponse(response, Constants.FAILURE,
					Constants.FAILURE_VENDOR_PREDEFINED_SETTINGS_DELETED, e.getMessage(),
					Constants.VENDOR_PREDEFINED_SETTINGS_DELETED_UNSUCCESSFULLY);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@RequestMapping(value = "/v1/generalsettings/{organizationId}")
	public ResponseEntity<BaseResponse> getGeneralSettingsForOrg(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, @PathVariable Integer organizationId) {
		logger.info("Entry into getGeneralSettingsForOrg");
		BaseResponse response = new GeneralSettingsListResponse();
		try {
			if (organizationId != null) {
				List<GeneralSettingsVo> listVos = vendorSettingsService.getGeneralSettings(organizationId);
				logger.info("listSize::" + listVos.size());
				setTokens(response, httpServletRequest.getHeader("keyToken"),
						httpServletRequest.getHeader("valueToken"));
				((GeneralSettingsListResponse) response).setData(listVos);
				response = constructResponse(response, Constants.SUCCESS,
						Constants.SUCCESS_VENDOR_GENERAL_SETTINGS_FETCH,
						Constants.SUCCESS_VENDOR_GENERAL_SETTINGS_FETCH, Constants.SUCCESS_DURING_GET);
				return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
			} else {
				response = constructResponse(response, Constants.FAILURE,
						Constants.FAILURE_VENDOR_GENERAL_SETTINGS_FETCH,
						Constants.FAILURE_IN_USER_ORGANIZATION_REQUEST_VALIDATION, Constants.FAILURE_DURING_GET);
				logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
				return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch (Exception e) {
			response = constructResponse(response, Constants.FAILURE,
					Constants.FAILURE_VENDOR_GENERAL_SETTINGS_FETCH, e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@RequestMapping(value = "/v1/generalsettings/modify/{generalSettingId}/{isActive}")
	public ResponseEntity<BaseResponse> modifyGeneralSettings(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, @PathVariable Integer generalSettingId,
			@PathVariable Boolean isActive) {
		logger.info("Entry into modifyGeneralSettings");
		BaseResponse response = new GeneralSettingsListResponse();
		try {
			vendorSettingsService.activateDeactivateGeneralSettings(generalSettingId, isActive);
			setTokens(response, httpServletRequest.getHeader("keyToken"), httpServletRequest.getHeader("valueToken"));
			((GeneralSettingsListResponse) response).setData(null);
			if (isActive) {
				response = constructResponse(response, Constants.SUCCESS,
						Constants.SUCCESS_VENDOR_GENERAL_SETTINGS_ACTIVATED,
						Constants.SUCCESS_VENDOR_GENERAL_SETTINGS_ACTIVATED,
						Constants.VENDOR_GENERAL_SETTINGS_UPDATED_SUCCESSFULLY);
				logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			} else {
				response = constructResponse(response, Constants.SUCCESS,
						Constants.SUCCESS_VENDOR_GENERAL_SETTINGS_DEACTIVATED,
						Constants.SUCCESS_VENDOR_GENERAL_SETTINGS_DEACTIVATED,
						Constants.VENDOR_GENERAL_SETTINGS_UPDATED_SUCCESSFULLY);
				logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			}
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (Exception e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_VENDOR_GENERAL_SETTINGS_UPDATE,
					e.getMessage(), Constants.VENDOR_GENERAL_SETTINGS_UPDATED_UNSUCCESSFULLY);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}

package com.blackstrawai.controller.settings;

import java.util.ArrayList;
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
import com.blackstrawai.helper.SettingsConvertToVoHelper;
import com.blackstrawai.request.JSONObject;
import com.blackstrawai.request.settings.ListPageCustomizationRequest;
import com.blackstrawai.response.settings.ListPageCustomizationResponse;
import com.blackstrawai.settings.ListPageCustomizationService;
import com.blackstrawai.settings.ListPageCustomizationVo;

@RestController
@CrossOrigin
//This will be part of Settings and Preferences. Hence sp.
@RequestMapping("/decifer/sp/lp")

public class ListPageCustomizationController extends BaseController{
	
	@Autowired
	ListPageCustomizationService listPageCustomizationService;

	private Logger logger = Logger.getLogger(ListPageCustomizationController.class);
	

		
		// For Update Tds

		@RequestMapping(value = "/v1/customize", method = RequestMethod.PUT)
		public ResponseEntity<BaseResponse> updateCustomization(HttpServletRequest httpRequest,
				HttpServletResponse httpResponse, @RequestBody JSONObject<ListPageCustomizationRequest> listPageCustomizationRequest) {
			logger.info("Entry into method:updateCustomization");
			BaseResponse response = new ListPageCustomizationResponse();
			try {
				logger.info("Request Payload:" + generateRequestAndResponseLogPaylod(listPageCustomizationRequest));
				ListPageCustomizationVo data = SettingsConvertToVoHelper.getInstance()
						.convertListPageCustomizeVoFromListPageCustomizeRequest(listPageCustomizationRequest.getData());
				data = listPageCustomizationService.updateCustomization(data);
				setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
				List<ListPageCustomizationVo>listData=new ArrayList<ListPageCustomizationVo>();
				listData.add(data);
				data.setKeyToken(null);
				data.setValueToken(null);
				if(data!=null && listData.size()>0){
					((ListPageCustomizationResponse) response).setData(listData.get(0));
				}else{
					((ListPageCustomizationResponse) response).setData(null);

				}
				response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_LIST_PAGE_CUSTOMIZATION_UPDATED,
						Constants.SUCCESS_LIST_PAGE_CUSTOMIZATION_UPDATED, Constants.LIST_PAGE_CUSTOMIZE_UPDATED_SUCCESSFULLY);
				logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
				return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
			} catch (ApplicationException e) {
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_LIST_PAGE_CUSTOMIZATION_UPDATED,
						e.getMessage(), Constants.LIST_PAGE_CUSTOMIZE_UPDATED_UNSUCCESSFULLY);
				logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
				return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}

	
		// For Fetching all Tds details
		@RequestMapping(value = "/v1/customize/{organizationId}/{organizationName}/{userId}/{roleName}/{moduleName}", method = RequestMethod.GET)
		public ResponseEntity<BaseResponse> getAllCustomizationForUserAndRole(HttpServletRequest httpRequest,
				HttpServletResponse httpResponse, @PathVariable int organizationId,@PathVariable String organizationName,
				@PathVariable String userId,@PathVariable String roleName,@PathVariable String moduleName) {
			logger.info("Entry into method:getAllCustomizationForUserAndRole");
			BaseResponse response = new ListPageCustomizationResponse();
			try {
				List<ListPageCustomizationVo>data  = listPageCustomizationService.getAllCustomizationForUserAndRole(organizationId,userId,roleName,moduleName);
				setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
				if(data!=null && data.size()>0){
					((ListPageCustomizationResponse) response).setData(data.get(0));
				}else{
					((ListPageCustomizationResponse) response).setData(null);

				}
				response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_LIST_PAGE_CUSTOMIZATION_FETCH,
						Constants.SUCCESS_LIST_PAGE_CUSTOMIZATION_FETCH, Constants.SUCCESS_DURING_GET);
				logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
				return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
			} catch (ApplicationException e) {
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_LIST_PAGE_CUSTOMIZATION_FETCH,
						e.getMessage(), Constants.FAILURE_DURING_GET);
				logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
				logger.info(response);
				return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}

}

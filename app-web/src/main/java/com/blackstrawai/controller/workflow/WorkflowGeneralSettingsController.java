package com.blackstrawai.controller.workflow;

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
import com.blackstrawai.helper.WorkflowConvertToVoHelper;
import com.blackstrawai.request.JSONObject;
import com.blackstrawai.request.workflow.WorkflowGeneralSettingsRequest;
import com.blackstrawai.response.workflow.ListWorkflowGeneralSettingsResponse;
import com.blackstrawai.response.workflow.WorkflowGeneralSettingsResponse;
import com.blackstrawai.workflow.WorkflowGeneralSettingsService;
import com.blackstrawai.workflow.WorkflowGeneralSettingsVo;


@RestController
@CrossOrigin
//This will be part of settings. Hence s.
@RequestMapping("/decifer/settings/general_setting")
public class WorkflowGeneralSettingsController extends BaseController {

	@Autowired
	WorkflowGeneralSettingsService workflowGeneralSettingsService;


	private Logger logger = Logger.getLogger(WorkflowGeneralSettingsController.class);

	// For Creating WorkflowGeneralSettings
	@RequestMapping(value = "/v1/workflow_general_setting", method = RequestMethod.POST)
	public ResponseEntity<BaseResponse> createWorkflowGeneralSettings(HttpServletRequest httpRequest, HttpServletResponse httpResponse,
			@RequestBody JSONObject<WorkflowGeneralSettingsRequest> workflowGeneralSettingsRequest) {
		logger.info("Entry into method: createWorkflowGeneralSettings");
		BaseResponse response = new WorkflowGeneralSettingsResponse();
		try {
			logger.info("Request Payload:" + generateRequestAndResponseLogPaylod(workflowGeneralSettingsRequest));
			WorkflowGeneralSettingsVo workflowGeneralSettingsVo= WorkflowConvertToVoHelper.getInstance()
					.convertWorkflowGeneralSettingsVoFromWorkflowGeneralSettingsRequest(workflowGeneralSettingsRequest.getData());
			workflowGeneralSettingsVo= workflowGeneralSettingsService.createWorkflowGeneralSetting(workflowGeneralSettingsVo);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));

			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_WORKFLOW_GENERAL_SETTINGS_CREATED,
					Constants.SUCCESS_WORKFLOW_GENERAL_SETTINGS_CREATED, Constants.WORKFLOW_GENERAL_SETTINGS_CREATED_SUCCESSFULLY);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);

		} catch (ApplicationException e) {
			if(e.getCause()!=null){
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_WORKFLOW_GENERAL_SETTINGS_CREATED,
						e.getCause().getMessage(), Constants.WORKFLOW_GENERAL_SETTINGS_CREATED_UNSUCCESSFULLY);
			}else{
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_WORKFLOW_GENERAL_SETTINGS_CREATED,
						e.getMessage(), Constants.WORKFLOW_GENERAL_SETTINGS_CREATED_UNSUCCESSFULLY);
			}			
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// For Fetching all WorkflowGeneralSettings details
	@RequestMapping(value = "/v1/workflow_general_setting/{organizationId}/{moduleId}", method = RequestMethod.GET)
	public ResponseEntity<BaseResponse> getAllWorkflowGeneralSettingsOfAnOrganization(HttpServletRequest httpRequest,HttpServletResponse httpResponse,
			@PathVariable int organizationId,@PathVariable int moduleId) {
		logger.info("Entry into method:getAllWorkflowGeneralSettingsOfAnOrganization");
		BaseResponse response = new ListWorkflowGeneralSettingsResponse();
		try {
			List<WorkflowGeneralSettingsVo> listAllWorkflowGeneralSettings = workflowGeneralSettingsService.getAllWorkflowGeneralSettingsOfAnOrganization(organizationId,moduleId);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((ListWorkflowGeneralSettingsResponse) response).setData(listAllWorkflowGeneralSettings);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_WORKFLOW_GENERAL_SETTINGS_FETCH,
					Constants.SUCCESS_WORKFLOW_GENERAL_SETTINGS_FETCH, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_WORKFLOW_GENERAL_SETTINGS_FETCH,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}




	@RequestMapping(value = "/v1/workflow_general_setting", method = RequestMethod.PUT)
	public ResponseEntity<BaseResponse> updateWorkflowGeneralSettings(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @RequestBody JSONObject<WorkflowGeneralSettingsRequest> workflowGeneralSettingsRequest) {
		logger.info("Entry into method:updateWorkflowGeneralSettings");
		BaseResponse response = new WorkflowGeneralSettingsResponse();
		try {
			logger.info("Request Payload:" + generateRequestAndResponseLogPaylod(workflowGeneralSettingsRequest));
			WorkflowGeneralSettingsVo workflowGeneralSettingsVo= WorkflowConvertToVoHelper.getInstance()
					.convertWorkflowGeneralSettingsVoFromWorkflowGeneralSettingsRequest(workflowGeneralSettingsRequest.getData());
			workflowGeneralSettingsVo= workflowGeneralSettingsService.updateWorkflowGeneralSetting(workflowGeneralSettingsVo);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_WORKFLOW_GENERAL_SETTINGS_UPDATED,
					Constants.SUCCESS_WORKFLOW_GENERAL_SETTINGS_UPDATED, Constants.WORKFLOW_GENERAL_SETTINGS_UPDATED_SUCCESSFULLY);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_WORKFLOW_GENERAL_SETTINGS_UPDATED,
					e.getMessage(), Constants.WORKFLOW_GENERAL_SETTINGS_UPDATED_UNSUCCESSFULLY);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}



}


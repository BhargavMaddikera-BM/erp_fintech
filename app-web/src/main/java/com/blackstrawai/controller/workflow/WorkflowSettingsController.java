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
import com.blackstrawai.common.CommonConstants;
import com.blackstrawai.common.Constants;
import com.blackstrawai.helper.WorkflowConvertToVoHelper;
import com.blackstrawai.request.JSONObject;
import com.blackstrawai.request.workflow.WorkflowSettingsPriorityRequest;
import com.blackstrawai.request.workflow.WorkflowSettingsRequest;
import com.blackstrawai.response.workflow.ListWorkflowSettingsResponse;
import com.blackstrawai.response.workflow.WorkflowSettingsDropDownResponse;
import com.blackstrawai.response.workflow.WorkflowSettingsModuleDropDownResponse;
import com.blackstrawai.response.workflow.WorkflowSettingsResponse;
import com.blackstrawai.upload.dropdowns.ModuleTypeDropDownVo;
import com.blackstrawai.workflow.WorkflowSettingsPriorityVo;
import com.blackstrawai.workflow.WorkflowSettingsService;
import com.blackstrawai.workflow.WorkflowSettingsVo;
import com.blackstrawai.workflow.dropdowns.WorkflowSettingsDropDownVo;


@RestController
@CrossOrigin
//This will be part of usermanagement. Hence um.
@RequestMapping("/decifer/settings/workflow")
public class WorkflowSettingsController extends BaseController {

	@Autowired
	WorkflowSettingsService workflowSettingsService;


	private Logger logger = Logger.getLogger(WorkflowSettingsController.class);

	// For Creating WorkflowSettings
	@RequestMapping(value = "/v1/workflow_settings", method = RequestMethod.POST)
	public ResponseEntity<BaseResponse> createWorkflowSettings(HttpServletRequest httpRequest, HttpServletResponse httpResponse,
			@RequestBody JSONObject<WorkflowSettingsRequest> workflowSettingsRequest) {
		logger.info("Entry into method: createWorkflowSettings");
		BaseResponse response = new WorkflowSettingsResponse();
		try {
			logger.info("Request Payload:" + generateRequestAndResponseLogPaylod(workflowSettingsRequest));
			WorkflowSettingsVo workflowSettingsVo= WorkflowConvertToVoHelper.getInstance()
					.convertWorkflowSettingsVoFromWorkflowSettingsRequest(workflowSettingsRequest.getData());
			workflowSettingsVo= workflowSettingsService.createWorkflowSettings(workflowSettingsVo);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));

			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_WORKFLOW_SETTINGS_CREATED,
					Constants.SUCCESS_WORKFLOW_SETTINGS_CREATED, Constants.WORKFLOW_SETTINGS_CREATED_SUCCESSFULLY);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);

		} catch (ApplicationException e) {
			if(e.getCause()!=null){
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_WORKFLOW_SETTINGS_CREATED,
						e.getCause().getMessage(), Constants.WORKFLOW_SETTINGS_CREATED_UNSUCCESSFULLY);
			}else{
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_WORKFLOW_SETTINGS_CREATED,
						e.getMessage(), Constants.WORKFLOW_SETTINGS_CREATED_UNSUCCESSFULLY);
			}			
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// For Fetching all WorkflowSettings details
	@RequestMapping(value = "/v1/workflow_settings/{organizationId}/{organizationName}/{moduleId}", method = RequestMethod.GET)
	public ResponseEntity<BaseResponse> getAllWorkflowSettingsOfAnOrganization(HttpServletRequest httpRequest,HttpServletResponse httpResponse,
			@PathVariable int organizationId, @PathVariable String organizationName,@PathVariable int moduleId) {
		logger.info("Entry into method:getAllWorkflowSettingssOfAnOrganization");
		BaseResponse response = new ListWorkflowSettingsResponse();
		try {
			List<WorkflowSettingsVo> listAllWorkflowSettingss = workflowSettingsService.getAllWorkflowSettingssOfAnOrganization(organizationId,moduleId);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((ListWorkflowSettingsResponse) response).setData(listAllWorkflowSettingss);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_WORKFLOW_SETTINGS_FETCH,
					Constants.SUCCESS_WORKFLOW_SETTINGS_FETCH, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_WORKFLOW_SETTINGS_FETCH,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}


	// For activateOrDeactivate WorkflowSettings

	@RequestMapping(value = "/v1/workflow_settings/{organizationId}/{organizationName}/{userId}/{id}/{roleName}/{status}", method = RequestMethod.DELETE)
	public ResponseEntity<BaseResponse> deleteWorkflowSettings(HttpServletRequest httpRequest, HttpServletResponse httpResponse,
			@PathVariable int organizationId, @PathVariable String organizationName, @PathVariable String userId,
			@PathVariable int id, @PathVariable String roleName, @PathVariable String status) {
		logger.info("Entry into deleteWorkflowSettings");
		BaseResponse response = new WorkflowSettingsResponse();
		try {
			WorkflowSettingsVo workflowSettingsVo= workflowSettingsService.deleteWorkflowSettings(id, status);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			if(status.equals(CommonConstants.STATUS_AS_ACTIVE)) {
				response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_WORKFLOW_SETTINGS_ACTIVATED,
						Constants.SUCCESS_WORKFLOW_SETTINGS_ACTIVATED, Constants.SUCCESS_WORKFLOW_SETTINGS_DELETED);
				logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			}else if(status.equals(CommonConstants.STATUS_AS_INACTIVE) || status.equals(CommonConstants.STATUS_AS_DELETE)) {
				response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_WORKFLOW_SETTINGS_DEACTIVATED,
						Constants.SUCCESS_WORKFLOW_SETTINGS_DEACTIVATED, Constants.SUCCESS_WORKFLOW_SETTINGS_DELETED);
				logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			}
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_WORKFLOW_SETTINGS_DELETED,
					e.getMessage(), Constants.WORKFLOW_SETTINGS_DELETED_UNSUCCESSFULLY);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	// For Fetching all WorkflowSettings details
	@RequestMapping(value = "/v1/workflow_settings/active/{organizationId}/{organizationName}/{moduleId}", method = RequestMethod.GET)
	public ResponseEntity<BaseResponse> getAllActiveWorkflowSettingsOfAnOrganization(HttpServletRequest httpRequest,HttpServletResponse httpResponse,
			@PathVariable int organizationId, @PathVariable String organizationName,@PathVariable int moduleId) {
		logger.info("Entry into method:getAllWorkflowSettingssOfAnOrganization");
		BaseResponse response = new ListWorkflowSettingsResponse();
		try {
			List<WorkflowSettingsVo> listAllWorkflowSettings = workflowSettingsService.getAllActiveWorkflowSettingsOfAnOrganization(organizationId,moduleId);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((ListWorkflowSettingsResponse) response).setData(listAllWorkflowSettings);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_WORKFLOW_SETTINGS_FETCH,
					Constants.SUCCESS_WORKFLOW_SETTINGS_FETCH, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_WORKFLOW_SETTINGS_FETCH,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/v1/workflow_settings/set_priority", method = RequestMethod.PUT)
	public ResponseEntity<BaseResponse> updateWorkflowSettingsPriority(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @RequestBody JSONObject<WorkflowSettingsPriorityRequest> workflowSettingsPriorityRequest) {
		logger.info("Entry into method: updateWorkflowSettingsPriority");
		BaseResponse response = new WorkflowSettingsResponse();
		try {

			logger.info("Request Payload:" + generateRequestAndResponseLogPaylod(workflowSettingsPriorityRequest));
			if(workflowSettingsPriorityRequest.getData()!=null) {
				for(WorkflowSettingsPriorityVo workflowSettingsPriorityVo:workflowSettingsPriorityRequest.getData().getData()) {
					workflowSettingsService.updateWorkflowSettingPriority(workflowSettingsPriorityVo.getId(),workflowSettingsPriorityVo.getPriority());
				}
			}
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_WORKFLOW_SETTINGS_UPDATED,
					Constants.SUCCESS_WORKFLOW_SETTINGS_UPDATED, Constants.WORKFLOW_SETTINGS_UPDATED_SUCCESSFULLY);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (Exception e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_WORKFLOW_SETTINGS_UPDATED,
					e.getMessage(), Constants.WORKFLOW_SETTINGS_UPDATED_UNSUCCESSFULLY);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}


	@RequestMapping(value = "/v1/workflow_settings", method = RequestMethod.PUT)
	public ResponseEntity<BaseResponse> updateWorkflowSettings(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @RequestBody JSONObject<WorkflowSettingsRequest> workflowSettingsRequest) {
		logger.info("Entry into method:updateWorkflowSettings");
		BaseResponse response = new WorkflowSettingsResponse();
		try {
			logger.info("Request Payload:" + generateRequestAndResponseLogPaylod(workflowSettingsRequest));
			WorkflowSettingsVo workflowSettingsVo= WorkflowConvertToVoHelper.getInstance()
					.convertWorkflowSettingsVoFromWorkflowSettingsRequest(workflowSettingsRequest.getData());
			workflowSettingsVo= workflowSettingsService.updateWorkflowSettings(workflowSettingsVo);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));

			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_WORKFLOW_SETTINGS_UPDATED,
					Constants.SUCCESS_WORKFLOW_SETTINGS_UPDATED, Constants.WORKFLOW_SETTINGS_UPDATED_SUCCESSFULLY);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_WORKFLOW_SETTINGS_UPDATED,
					e.getMessage(), Constants.WORKFLOW_SETTINGS_UPDATED_UNSUCCESSFULLY);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}


	// For Fetching WorkflowSettings by Id
	@RequestMapping(value = "/v1/workflow_settings/{organizationId}/{organizationName}/{userId}/{roleName}/{id}", method = RequestMethod.GET)
	public ResponseEntity<BaseResponse> getWorkflowSettingsById(HttpServletRequest httpRequest, HttpServletResponse httpResponse,
			@PathVariable int organizationId, @PathVariable String organizationName, @PathVariable int userId,
			@PathVariable String roleName, @PathVariable int id) {
		logger.info("Entry into method:getWorkflowSettingsById");
		BaseResponse response = new WorkflowSettingsResponse();
		try {
			WorkflowSettingsVo workflowSettingsVo = workflowSettingsService.getWorkflowSettingsById(id);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((WorkflowSettingsResponse) response).setData(workflowSettingsVo);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_WORKFLOW_SETTINGS_FETCH,
					Constants.SUCCESS_WORKFLOW_SETTINGS_FETCH, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_WORKFLOW_SETTINGS_FETCH,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}


	// For Fetching WorkflowSettings Dropdowns by orgId
	@RequestMapping(value = "/v1/dropsdown/workflow_settings/{organizationId}/{moduleId}", method = RequestMethod.GET)
	public ResponseEntity<BaseResponse> getWorkflowSettingsDropDownData(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse,@PathVariable int organizationId,@PathVariable int moduleId) {
		logger.info("Entry into method: getWorkflowSettingsDropDownData");
		BaseResponse response = new WorkflowSettingsDropDownResponse();
		try {
			WorkflowSettingsDropDownVo WorkflowSettingsVo = workflowSettingsService.getWorkflowSettingsDropdownData(organizationId,moduleId);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((WorkflowSettingsDropDownResponse) response).setData(WorkflowSettingsVo);
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



	// For Fetching WorkflowSettings Dropdowns by orgId
	@RequestMapping(value = "/v1/dropsdown/workflow_settings/modules", method = RequestMethod.GET)
	public ResponseEntity<BaseResponse> getWorkflowModuleDropDownData(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse) {
		logger.info("Entry into method: getWorkflowSettingsDropDownData");
		BaseResponse response = new WorkflowSettingsModuleDropDownResponse();
		try {
			ModuleTypeDropDownVo WorkflowSettingsVo = workflowSettingsService.getWorkflowModuleDropDownData();
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((WorkflowSettingsModuleDropDownResponse) response).setData(WorkflowSettingsVo);
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
	@RequestMapping(value = "/v1/workflow_settings/sync", method = RequestMethod.GET)
	public ResponseEntity<BaseResponse> syncWorflow(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse) {
		BaseResponse response = new WorkflowSettingsResponse();
		try {
			workflowSettingsService.syncWorflow();
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_WORKFLOW_SETTINGS_FETCH,
					Constants.SUCCESS_WORKFLOW_SETTINGS_FETCH, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_WORKFLOW_SETTINGS_FETCH,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}

	


}


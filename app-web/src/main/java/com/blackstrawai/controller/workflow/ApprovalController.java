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
import com.blackstrawai.request.workflow.ApprovalRequest;
import com.blackstrawai.response.ar.receipt.ListReceiptBulkDropdownResponse;
import com.blackstrawai.response.workflow.ApprovalDropDownResponse;
import com.blackstrawai.response.workflow.ListApprovalResponse;
import com.blackstrawai.response.workflow.WorkflowSettingsResponse;
import com.blackstrawai.settings.CommonVo;
import com.blackstrawai.workflow.ApprovalVo;
import com.blackstrawai.workflow.WorkflowProcessService;
import com.blackstrawai.workflow.WorkflowUserApprovalDetailsVo;


@RestController
@CrossOrigin
@RequestMapping("/decifer/approval")
public class ApprovalController extends BaseController {

	@Autowired
	WorkflowProcessService workflowProcessService;


	private Logger logger = Logger.getLogger(ApprovalController.class);

	// For Fetching all WorkflowSettings details
	@RequestMapping(value = "/v1/workflow/approval/approvals", method = RequestMethod.POST)
	public ResponseEntity<BaseResponse> approverWorkflowRequestOfAnOrganization(HttpServletRequest httpRequest,HttpServletResponse httpResponse,
			@RequestBody JSONObject<ApprovalRequest> approvalRequest) {
		logger.info("Entry into method: approverWorkflowRequestOfAnOrganization");
		BaseResponse response = new WorkflowSettingsResponse();
		try {
			logger.info("Request Payload:" + generateRequestAndResponseLogPaylod(approvalRequest));
			ApprovalVo approvalVo= WorkflowConvertToVoHelper.getInstance()
					.convertApprovalVoFromApprovalRequest(approvalRequest.getData());

			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));

			boolean result=false;
			if(approvalVo!=null && approvalVo.getStatus().equalsIgnoreCase("APR")) {
				result=workflowProcessService.approveRule(approvalVo);
				response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_WORKFLOW_APPROVED,
						Constants.SUCCESS_WORKFLOW_APPROVED, Constants.WORKFLOW_APPROVAL_CREATED_SUCCESSFULLY);	
			}else if(approvalVo!=null && approvalVo.getStatus().equalsIgnoreCase("REJ")) {
				result=workflowProcessService.rejectRule(approvalVo);
				response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_WORKFLOW_REJECTED,
						Constants.SUCCESS_WORKFLOW_REJECTED, Constants.WORKFLOW_APPROVAL_CREATED_SUCCESSFULLY);	
			}
			if(!result){
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_WORKFLOW_APPROVAL_CREATED,
						Constants.FAILURE_WORKFLOW_APPROVAL_CREATED, Constants.WORKFLOW_APPROVAL_CREATED_UNSUCCESSFULLY);	
			}

			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);

		} catch (Exception e) {
			if(e.getCause()!=null){
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_WORKFLOW_APPROVAL_CREATED,
						Constants.FAILURE_WORKFLOW_APPROVAL_CREATED, Constants.WORKFLOW_APPROVAL_CREATED_UNSUCCESSFULLY);
			}else{
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_WORKFLOW_APPROVAL_CREATED,
						Constants.FAILURE_WORKFLOW_APPROVAL_CREATED, Constants.WORKFLOW_APPROVAL_CREATED_UNSUCCESSFULLY);
			}			
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@RequestMapping(value = "/v1/workflow/approval/approvals/{organizationId}/{organizationName}/{userId}/{roleName}")
	public ResponseEntity<BaseResponse> getAllPendingRequestsForUserAndRole(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @PathVariable int organizationId, @PathVariable String organizationName,
			@PathVariable int userId, @PathVariable String roleName) {
		logger.info("Entry into getAllPendingRequestsForUserAndRole ");
		BaseResponse response = new ListApprovalResponse();
		try {
			List<WorkflowUserApprovalDetailsVo> listAllUsers = workflowProcessService.getPendingApprovalsByOrganization(organizationId, userId, roleName);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((ListApprovalResponse) response).setData(listAllUsers);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_WORKFLOW_APPROVAL_FETCH,
					Constants.SUCCESS_WORKFLOW_APPROVAL_FETCH, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (Exception e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_WORKFLOW_APPROVAL_FETCH, e.getMessage(),
					Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@RequestMapping(value = "/v1/dropdowns/rejection_types")
	public ResponseEntity<BaseResponse> getRejectionTypesDropDown(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse) {
		logger.info("Entry into getRejectionTypesDropDown ");
		BaseResponse response = new ApprovalDropDownResponse();
		try {
			List<CommonVo> data=workflowProcessService.getRejectionTypesDropDown();
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((ApprovalDropDownResponse) response).setData(data);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_DROP_DOWN_FETCH,
					Constants.SUCCESS_DROP_DOWN_FETCH, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_DROP_DOWN_FETCH, e.getMessage(),
					Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}


}


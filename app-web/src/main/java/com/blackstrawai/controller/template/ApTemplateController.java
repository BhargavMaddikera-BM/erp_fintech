package com.blackstrawai.controller.template;

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

import com.blackstrawai.ap.template.PurchaseOrderTemplateInformationVo;
import com.blackstrawai.ap.template.PurchaseOrderTemplateVo;
import com.blackstrawai.common.BaseController;
import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.common.Constants;
import com.blackstrawai.helper.ApTemplateConvertToVoHelper;
import com.blackstrawai.request.JSONObject;
import com.blackstrawai.request.ap.template.PurchaseOrderTemplateRequest;
import com.blackstrawai.response.ap.template.PurchaseOrderTemplateInfoListResponse;
import com.blackstrawai.response.ap.template.PurchaseOrderTemplateListResponse;
import com.blackstrawai.response.ap.template.PurchaseOrderTemplateResponse;
import com.blackstrawai.template.ApTemplateService;

@RestController
@CrossOrigin
@RequestMapping("/decifer/ap/template")
public class ApTemplateController extends BaseController {
	private Logger logger = Logger.getLogger(ApTemplateController.class);

	@Autowired
	private ApTemplateService templateService;

	@RequestMapping(value = "/v1/templates/purchaseorder", method = RequestMethod.POST)
	public ResponseEntity<BaseResponse> createPurchaseOrderTemplate(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse,
			@RequestBody JSONObject<PurchaseOrderTemplateRequest> apPurchaseOrderTemplateRequest) {
		logger.info("Entered Into createPurchaseOrderTemplate");
		BaseResponse response = new PurchaseOrderTemplateResponse();
		if (apPurchaseOrderTemplateRequest.getData().getOrgId() != null) {
			try {
				logger.info("Request Payload:" + generateRequestAndResponseLogPaylod(apPurchaseOrderTemplateRequest));
				PurchaseOrderTemplateVo purchaseOrderTempVo = ApTemplateConvertToVoHelper.getInstance()
						.convertApPurchaseOrderTemplateVoFromRequest(apPurchaseOrderTemplateRequest.getData());
				purchaseOrderTempVo = templateService.createPurchaseOrderTemplate(purchaseOrderTempVo);
				setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
				((PurchaseOrderTemplateResponse) response).setData(purchaseOrderTempVo);
				response = constructResponse(response, Constants.SUCCESS,Constants.PO_CREATED_SUCCESSFULLY,
						Constants.SUCCESS_PO_CREATED,Constants.PO_CREATED_SUCCESSFULLY);
				logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
				return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
			} catch (Exception e) {
				if (e.getCause() != null) {
					response = constructResponse(response, Constants.FAILURE,
							Constants.FAILURE_PO_CREATED, e.getCause().getMessage(),
							Constants.PO_CREATED_UNSUCCESSFULLY);
				} else {
					response = constructResponse(response, Constants.FAILURE,
							Constants.FAILURE_PO_CREATED, e.getMessage(),
							Constants.PO_CREATED_UNSUCCESSFULLY);
				}
				logger.error("Error Payload:", e);
				logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
				return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} else {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_PO_CREATED,
					Constants.FAILURE_IN_USER_ORGANIZATION_REQUEST_VALIDATION,
					Constants.PO_CREATED_UNSUCCESSFULLY);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/v1/templates/purchaseorder/{organizationId}/{organizationName}/{userId}/{roleName}/{id}")
	public ResponseEntity<BaseResponse> getPurchaseOrderTemplate(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @PathVariable int organizationId, @PathVariable String organizationName,
			@PathVariable String userId, @PathVariable String roleName, @PathVariable int id) {
		logger.info("Entry into getPurchaseOrderTemplate");
		BaseResponse response = new PurchaseOrderTemplateResponse();
		try {
			PurchaseOrderTemplateVo purchaseOrderTempVo = templateService.getPurchaseOrderTemplate(organizationId, id);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((PurchaseOrderTemplateResponse) response).setData(purchaseOrderTempVo);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_PO_FETCH,
					Constants.SUCCESS_PO_FETCH, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (Exception e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_PO_FETCH,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/v1/templates/purchaseorder/{organizationId}/{organizationName}/{userId}/{roleName}")
	public ResponseEntity<BaseResponse> getAllPurchaseOrderTemplate(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @PathVariable int organizationId, @PathVariable String organizationName,
			@PathVariable String userId, @PathVariable String roleName) {
		logger.info("Entry into getAllPurchaseOrderTemplate");
		BaseResponse response = new PurchaseOrderTemplateInfoListResponse();
		try {
			List<PurchaseOrderTemplateInformationVo> purchaseOrderTempVo = templateService.getAllPurchaseOrderTemplate(organizationId,
					userId, roleName);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((PurchaseOrderTemplateInfoListResponse) response).setPurchaseOrderTemplate(purchaseOrderTempVo);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_PO_FETCH,
					Constants.SUCCESS_PO_FETCH, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (Exception e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_PO_FETCH,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/v1/templates/purchaseorder/{organizationId}")
	public ResponseEntity<BaseResponse> getAllPurchaseOrderTemplateOfAnOrganization(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @PathVariable int organizationId) {
		logger.info("Entry into getAllPurchaseOrderTemplate");
		BaseResponse response = new PurchaseOrderTemplateListResponse();
		try {
			List<PurchaseOrderTemplateVo> purchaseOrderTempVo = templateService
					.getAllPurchaseOrderTemplatePerOrganization(organizationId);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((PurchaseOrderTemplateListResponse) response).setPurchaseOrderTemplate(purchaseOrderTempVo);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_PO_FETCH,
					Constants.SUCCESS_PO_FETCH, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (Exception e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_PO_FETCH,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/v1/templates/purchaseorder/default/{organizationId}")
	public ResponseEntity<BaseResponse> getDefaultPurchaseOrderTemplateOfAnOrganization(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @PathVariable int organizationId) {
		logger.info("Entry into getDefaultPurchaseOrderTemplateOfAnOrganization");
		BaseResponse response = new PurchaseOrderTemplateResponse();
		try {
			PurchaseOrderTemplateVo purchaseOrderTempVo = templateService.getDefaultTemplateOfAnOrganization(organizationId);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((PurchaseOrderTemplateResponse) response).setData(purchaseOrderTempVo);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_PO_FETCH,
					Constants.SUCCESS_PO_FETCH, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (Exception e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_PO_FETCH,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/v1/templates/purchaseorder", method = RequestMethod.PUT)
	public ResponseEntity<BaseResponse> updatePurchaseOrderTemplate(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse,
			@RequestBody JSONObject<PurchaseOrderTemplateRequest> apPurchaseOrderTemplateRequest) {
		logger.info("Entered Into update Purchase Order Template");
		BaseResponse response = new PurchaseOrderTemplateResponse();
		if (apPurchaseOrderTemplateRequest.getData().getOrgId() != null) {
			try {
				logger.info("Request Payload:" + generateRequestAndResponseLogPaylod(apPurchaseOrderTemplateRequest));
				PurchaseOrderTemplateVo purchaseOrderTempVo = ApTemplateConvertToVoHelper.getInstance()
						.convertApPurchaseOrderTemplateVoFromRequest(apPurchaseOrderTemplateRequest.getData());
				// invoiceService.updateInvoice(invoiceVo);
				templateService.UpdatePurchaseOrderTemplate(purchaseOrderTempVo);
				setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
				response = constructResponse(response, Constants.SUCCESS,
						Constants.PO_UPDATED_SUCCESSFULLY,
						Constants.SUCCESS_PO_UPDATED,
						Constants.PO_UPDATED_SUCCESSFULLY);
				logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
				return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
			} catch (Exception e) {
				if (e.getCause() != null) {
					response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_PO_UPDATED,
							e.getCause().getMessage(), Constants.PO_UPDATED_UNSUCCESSFULLY);
				} else {
					response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_PO_UPDATED,
							e.getMessage(), Constants.PO_UPDATED_UNSUCCESSFULLY);
				}
				logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
				return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} else {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_PO_UPDATED,
					Constants.FAILURE_IN_USER_ORGANIZATION_REQUEST_VALIDATION,
					Constants.PO_UPDATED_UNSUCCESSFULLY);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/v1/templates/purchaseorder/{organizationId}/{organizationName}/{userId}/{id}/{roleName}/{status}", method = RequestMethod.DELETE)
	public ResponseEntity<BaseResponse> activateDeActivatePurchaseOrderTemplate(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @PathVariable int organizationId, @PathVariable String organizationName,
			@PathVariable String userId, @PathVariable String roleName, @PathVariable int id,
			@PathVariable String status) {
		logger.info("Entry into activateDeActivatePurchaseOrderTemplate");
		BaseResponse response = new PurchaseOrderTemplateResponse();
		try {
			PurchaseOrderTemplateVo purchaseOrderTempVo = templateService.activateDeActivatePurchaseOrderTemplate(organizationId,
					organizationName, userId, id, roleName, status);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((PurchaseOrderTemplateResponse) response).setData(purchaseOrderTempVo);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_PO_ACTIVATED,
					Constants.SUCCESS_PO_ACTIVATED, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (Exception e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_PO_DELETED,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}

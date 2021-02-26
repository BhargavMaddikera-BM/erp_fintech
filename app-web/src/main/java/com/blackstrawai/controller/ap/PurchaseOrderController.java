package com.blackstrawai.controller.ap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.blackstrawai.ap.PurchaseOrderService;
import com.blackstrawai.ap.dropdowns.PurchaseOrderDropDownVo;
import com.blackstrawai.ap.purchaseorder.PoFilterVo;
import com.blackstrawai.ap.purchaseorder.PoListVo;
import com.blackstrawai.ap.purchaseorder.PoProductVo;
import com.blackstrawai.ap.purchaseorder.PoTaxComputationVo;
import com.blackstrawai.ap.purchaseorder.PurchaseOrderVo;
import com.blackstrawai.common.BaseController;
import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.common.CommonConstants;
import com.blackstrawai.common.Constants;
import com.blackstrawai.helper.ApConvertToVoHelper;
import com.blackstrawai.request.JSONObject;
import com.blackstrawai.request.ap.purchaseorder.PoFilterRequest;
import com.blackstrawai.request.ap.purchaseorder.PoTaxComputationRequest;
import com.blackstrawai.request.ap.purchaseorder.PurchaseOrderRequest;
import com.blackstrawai.response.ap.purchaseorder.ListPurchaseOrderResponse;
import com.blackstrawai.response.ap.purchaseorder.PoTaxComputationResponse;
import com.blackstrawai.response.ap.purchaseorder.PurchaseOrderDropDownResponse;
import com.blackstrawai.response.ap.purchaseorder.PurchaseOrderResponse;

@RestController
@CrossOrigin
@RequestMapping("/decifer/ap/po")
public class PurchaseOrderController extends BaseController {
	
	@Autowired
	private PurchaseOrderService purchaseOrderService;
	
	private Logger logger = Logger.getLogger(PurchaseOrderController.class);

	
	@RequestMapping(value="/v1/pos" , method = RequestMethod.POST)
	public ResponseEntity<BaseResponse>createPurchaseOrder(HttpServletRequest httpRequest, HttpServletResponse httpResponse,@RequestBody JSONObject<PurchaseOrderRequest> purchaseOrderRequest){
		logger.info("Entered Into createPurchaseOrder");
		BaseResponse response = new PurchaseOrderResponse();
		if(purchaseOrderRequest.getData().getUserId()!=null && purchaseOrderRequest.getData().getOrganizationId()!=null) {
		try {
			logger.info("Request Payload:"+generateRequestAndResponseLogPaylod(purchaseOrderRequest));	
			PurchaseOrderVo purchaseOrderVo = ApConvertToVoHelper.getInstance().convertPoVoFromRequest(purchaseOrderRequest.getData());
			purchaseOrderService.createPurchaseOrder(purchaseOrderVo);
			setTokens(response,httpRequest.getHeader("keyToken"),httpRequest.getHeader("valueToken"));
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_PO_CREATED,Constants.SUCCESS_PO_CREATED,Constants.PO_CREATED_SUCCESSFULLY);
			logger.info("Response Payload:"+generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		}catch(Exception e) {
			if(e.getCause()!=null){
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_PO_CREATED,e.getCause().getMessage(), Constants.PO_CREATED_UNSUCCESSFULLY);
			}else{
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_PO_CREATED,e.getMessage(), Constants.PO_CREATED_UNSUCCESSFULLY);
			}
			logger.error("Error Payload:"+generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}	
		}else {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_PO_CREATED,Constants.FAILURE_IN_USER_ORGANIZATION_REQUEST_VALIDATION, Constants.PO_CREATED_UNSUCCESSFULLY);
			logger.error("Error Payload:"+ generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	@RequestMapping(value="/v1/pos/computeTax" , method = RequestMethod.POST)
	public ResponseEntity<BaseResponse>computeTaxCalculation(HttpServletRequest httpRequest, HttpServletResponse httpResponse,@RequestBody JSONObject<PoTaxComputationRequest> taxComputationrequest){
		logger.info("Entered Into computeTaxCalculation");
		BaseResponse response = new PoTaxComputationResponse();
		try {
			logger.info("Request Payload:"+generateRequestAndResponseLogPaylod(taxComputationrequest));	
			List<PoProductVo> productVoList = new ArrayList<PoProductVo>();
			if(taxComputationrequest.getData()!=null && taxComputationrequest.getData().getProducts()!=null && taxComputationrequest.getData().getOrganizationId()!=null) {
			taxComputationrequest.getData().getProducts().forEach(product->{
				PoProductVo productVo = ApConvertToVoHelper.getInstance().convertPoProductVoFromRequest(product);
				productVoList.add(productVo);
			});
			PoTaxComputationVo computedValue = purchaseOrderService.computeTaxCalculation(productVoList,taxComputationrequest.getData().getOrganizationId(),taxComputationrequest.getData().getIsExclusive());
			setTokens(response,httpRequest.getHeader("keyToken"),httpRequest.getHeader("valueToken"));
			((PoTaxComputationResponse) response).setData(computedValue);
			response = constructResponse(response, Constants.SUCCESS, Constants.CALCULATION_DONE_SUCCESSFULLY,Constants.SUCCESS_ON_CALCULATION,Constants.CALCULATION_DONE_SUCCESSFULLY);
			logger.info("Response Payload:"+generateRequestAndResponseLogPaylod(response));
			}
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		}catch(Exception e) {
			if(e.getCause()!=null){
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_DURING_CALCULATION,e.getCause().getMessage(), Constants.CALCULATION_DONE_UNSUCCESSFULLY);
			}else{
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_DURING_CALCULATION,e.getMessage(), Constants.CALCULATION_DONE_UNSUCCESSFULLY);
			}
			logger.error("Error Payload:" , e);
			logger.error("Error Payload:"+generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}	
		
	}
	
	@RequestMapping(value = "/v1/dropsdowns/pos/{organizationId}")
	public ResponseEntity<BaseResponse> getPurchaseOrderDropDown(HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse,@PathVariable int organizationId) {
		logger.info("Entry into getPurchaseOrderDropDown");
		BaseResponse response = new PurchaseOrderDropDownResponse();
		try {
				PurchaseOrderDropDownVo data=purchaseOrderService.getPurchaseOrderDropDownData(organizationId);
				setTokens(response,httpServletRequest.getHeader("keyToken"),httpServletRequest.getHeader("valueToken"));
				((PurchaseOrderDropDownResponse) response).setData(data);
				response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_DROP_DOWN_FETCH,
						Constants.SUCCESS_DROP_DOWN_FETCH, Constants.SUCCESS_DURING_GET);
				logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
				return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
			
		} catch (Exception e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_PO_FETCH, e.getMessage(),
					Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	
	
	@RequestMapping(value = "/v1/pos/list")
	public ResponseEntity<BaseResponse> getAllPurchaseOrders(HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse,@RequestBody JSONObject<PoFilterRequest> filterRequest) {
		logger.info("Entry into getAllPurchaseOrders");
		BaseResponse response = new ListPurchaseOrderResponse();
		try {
			if( filterRequest.getData()!= null && filterRequest.getData().getOrgId()!=null) {
				PoFilterVo filterVo = ApConvertToVoHelper.getInstance().convertPoFilterVoFromPoRequest(filterRequest.getData());

				List<PoListVo> poList = purchaseOrderService.getAllPurchaseOrderForOrg(filterVo);
				setTokens(response,httpServletRequest.getHeader("keyToken"),httpServletRequest.getHeader("valueToken"));
				((ListPurchaseOrderResponse) response).setData(poList.size()>0? poList : null);
				response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_PO_FETCH,
						Constants.SUCCESS_PO_FETCH, Constants.SUCCESS_DURING_GET);
				logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
				return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
			}else {
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_PO_FETCH,Constants.FAILURE_IN_USER_ORGANIZATION_REQUEST_VALIDATION, Constants.FAILURE_DURING_GET);
				logger.error("Error Payload:"+generateRequestAndResponseLogPaylod(response));
				return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch (Exception e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_PO_FETCH, e.getMessage(),
					Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	
	@RequestMapping(value = "/v1/pos/filterDropdowns/{organizationId}/{organizationName}")
	public ResponseEntity<Map<String, Object>> getFilterDropdownValues(HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse,@PathVariable Integer organizationId,@PathVariable String organizationName) {
		logger.info("Entry into getAllPurchaseOrders");
		Map<String, Object> response = new HashMap<String,Object>();
		try {
				response = purchaseOrderService.getFilterDropdownValues(organizationId);
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	
	
	@RequestMapping(value = "/v1/pos/{organizationId}/{organizationName}/{userId}/{roleName}/{id}")
	public ResponseEntity<BaseResponse> getPurchaseOrder(HttpServletRequest httpRequest, HttpServletResponse httpResponse,
			@PathVariable int organizationId,
			@PathVariable String organizationName,
			@PathVariable int userId,
			@PathVariable String roleName,
			@PathVariable int id) {
		logger.info("Entry into method:getPurchaseOrder");
		BaseResponse response = new PurchaseOrderResponse();
		try {
			PurchaseOrderVo poVo = purchaseOrderService.getPurchaseOrder(id);
			setTokens(response,httpRequest.getHeader("keyToken"),httpRequest.getHeader("valueToken"));
			((PurchaseOrderResponse) response).setData(poVo);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_PO_FETCH,
					Constants.SUCCESS_PO_FETCH, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (Exception e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_PO_FETCH, e.getMessage(),
					Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/v1/pos/{organizationId}/{organizationName}/{userId}/{id}/{roleName}/{status}", method = RequestMethod.DELETE)
	public ResponseEntity<BaseResponse> deletePurchaseOrder(HttpServletRequest httpRequest, HttpServletResponse httpResponse,
			@PathVariable Integer organizationId,			
			@PathVariable String organizationName,
			@PathVariable String userId, 
			@PathVariable int id,
			@PathVariable String roleName,
			@PathVariable String status) {		
		logger.info("Entry into deletePurchaseOrder");	
		BaseResponse response = new PurchaseOrderResponse();
			try {
				purchaseOrderService.activateOrDeactivatePurchaseOrder(id , status,userId,roleName);
				setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
				if(status.equals(CommonConstants.STATUS_AS_ACTIVE)) {
					response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_PO_ACTIVATED,
							Constants.SUCCESS_PO_ACTIVATED, Constants.PO_DELETED_SUCCESSFULLY);
					logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
				}else if(status.equals(CommonConstants.STATUS_AS_INACTIVE)) {
				response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_PO_DEACTIVATED,
						Constants.SUCCESS_PO_DEACTIVATED, Constants.PO_DELETED_SUCCESSFULLY);
				logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
				}
				return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
			} catch (Exception e) {
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_PO_UPDATED, e.getMessage(),
						Constants.PO_DELETED_UNSUCCESSFULLY);
				logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
				return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
	}
	
	@RequestMapping(value="/v1/pos" , method = RequestMethod.PUT)
	public ResponseEntity<BaseResponse>updatePurchaseOrder(HttpServletRequest httpRequest, HttpServletResponse httpResponse,@RequestBody JSONObject<PurchaseOrderRequest> purchaseOrderRequest){
		logger.info("Entered Into createPurchaseOrder");
		BaseResponse response = new PurchaseOrderResponse();
		if(purchaseOrderRequest.getData().getId()!=null &&  purchaseOrderRequest.getData().getUserId()!=null && purchaseOrderRequest.getData().getOrganizationId()!=null) {
		try {
			logger.info("Request Payload:"+generateRequestAndResponseLogPaylod(purchaseOrderRequest));	
			PurchaseOrderVo purchaseOrderVo = ApConvertToVoHelper.getInstance().convertPoVoFromRequest(purchaseOrderRequest.getData());
			purchaseOrderService.updatePurchaseOrder(purchaseOrderVo);
			setTokens(response,httpRequest.getHeader("keyToken"),httpRequest.getHeader("valueToken"));
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_PO_UPDATED,Constants.SUCCESS_PO_UPDATED,Constants.PO_UPDATED_SUCCESSFULLY);
			logger.info("Response Payload:"+generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		}catch(Exception e) {
			if(e.getCause()!=null){
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_PO_UPDATED,e.getCause().getMessage(), Constants.PO_UPDATED_UNSUCCESSFULLY);
			}else{
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_PO_UPDATED,e.getMessage(), Constants.PO_UPDATED_UNSUCCESSFULLY);
			}
			logger.error("Error Payload:"+generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}	
		}else {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_PO_UPDATED,Constants.FAILURE_IN_USER_ORGANIZATION_REQUEST_VALIDATION, Constants.PO_UPDATED_UNSUCCESSFULLY);
			logger.error("Error Payload:"+ generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
			
}

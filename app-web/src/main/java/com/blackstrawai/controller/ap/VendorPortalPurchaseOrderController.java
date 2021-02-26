package com.blackstrawai.controller.ap;

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
import com.blackstrawai.ap.vendorportal.purchaseorder.DeclinePoVo;
import com.blackstrawai.ap.vendorportal.purchaseorder.VendorPortalPoFilterVo;
import com.blackstrawai.ap.vendorportal.purchaseorder.VendorPortalPoListVo;
import com.blackstrawai.common.BaseController;
import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.common.Constants;
import com.blackstrawai.helper.ApConvertToVoHelper;
import com.blackstrawai.request.JSONObject;
import com.blackstrawai.request.ap.vendorportal.purchaseorder.DeclinePoRequest;
import com.blackstrawai.request.ap.vendorportal.purchaseorder.VendorPortalPoFilterRequest;
import com.blackstrawai.response.ap.purchaseorder.PurchaseOrderResponse;
import com.blackstrawai.response.ap.vendorportal.purchaseorder.VpPurchaseOrderListResponse;

@RestController
@CrossOrigin
@RequestMapping("/decifer/vendorportal/po")
public class VendorPortalPurchaseOrderController extends BaseController{

	@Autowired
	private PurchaseOrderService purchaseOrderService;
	
	private Logger logger = Logger.getLogger(VendorPortalPurchaseOrderController.class);

	
	@RequestMapping(value = "/v1/pos/filterdropdown/{vendorId}")
	public ResponseEntity<Map<String, Object>> getFilterDropdownValues(HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse,@PathVariable Integer vendorId) {
		logger.info("Entry into getFilterDropdownValues::"+vendorId);
		Map<String, Object> response = new HashMap<String,Object>();
		try {
				response = purchaseOrderService.getVpPoFilterDropDowns(vendorId);
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	

	@RequestMapping(value = "/v1/pos/dropdown/{vendorId}")
	public ResponseEntity<Map<String, Object>> getDropdownValues(HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse,@PathVariable Integer vendorId) {
		logger.info("Entry into getDropdownValues::"+vendorId);
		Map<String, Object> response = new HashMap<String,Object>();
		try {
				response = purchaseOrderService.getDropDowns();
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	
	@RequestMapping(value = "/v1/pos/list")
	public ResponseEntity<BaseResponse> getAllPurchaseOrders(HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse,@RequestBody JSONObject<VendorPortalPoFilterRequest> filterRequest) {
		logger.info("Entry into getAllPurchaseOrders");
		BaseResponse response = new VpPurchaseOrderListResponse();
		try {
			if( filterRequest.getData()!= null && filterRequest.getData().getOrgId()!=null && filterRequest.getData().getVendorId()!=null) {
				VendorPortalPoFilterVo filterVo = ApConvertToVoHelper.getInstance().convertVendorPortalPoFilterVoFromPoRequest(filterRequest.getData());

				List<VendorPortalPoListVo> poList = purchaseOrderService.getPoListForvendorPortal(filterVo);
				setTokens(response,httpServletRequest.getHeader("keyToken"),httpServletRequest.getHeader("valueToken"));
				((VpPurchaseOrderListResponse) response).setData(poList.size()>0? poList : null);
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
	
	@RequestMapping(value="/v1/pos/accept/{vendorId}/{purchaseOrderId}/{organizationId}/{organizationName}/{userId}/{roleName}" )
	public ResponseEntity<BaseResponse>acceptPurchaseOrder(HttpServletRequest httpRequest, HttpServletResponse httpResponse,
			@PathVariable Integer vendorId ,
			@PathVariable Integer purchaseOrderId,
			@PathVariable Integer organizationId,			
			@PathVariable String organizationName,
			@PathVariable String userId, 
			@PathVariable String roleName){
		logger.info("Entered Into acceptPurchaseOrder");
		BaseResponse response = new PurchaseOrderResponse();
		if(vendorId!=null && purchaseOrderId!=null ) {
		try {
			logger.info("Request Payload: vendor Id::"+vendorId + "PoId" + purchaseOrderId);	
			purchaseOrderService.acceptPo(purchaseOrderId,userId,roleName);
			setTokens(response,httpRequest.getHeader("keyToken"),httpRequest.getHeader("valueToken"));
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_PO_ACCEPT,Constants.SUCCESS_PO_ACCEPT,Constants.PO_UPDATED_SUCCESSFULLY);
			logger.info("Response Payload:"+generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		}catch(Exception e) {
			if(e.getCause()!=null){
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_PO_ACCEPT,e.getCause().getMessage(), Constants.PO_UPDATED_UNSUCCESSFULLY);
			}else{
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_PO_ACCEPT,e.getMessage(), Constants.PO_UPDATED_UNSUCCESSFULLY);
			}
			logger.error("Error Payload:"+generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}	
		}else {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_PO_ACCEPT,Constants.FAILURE_IN_USER_ORGANIZATION_REQUEST_VALIDATION, Constants.PO_UPDATED_UNSUCCESSFULLY);
			logger.error("Error Payload:"+ generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/v1/pos/decline", method = RequestMethod.POST)
	public ResponseEntity<BaseResponse> declinePurchaseOrders(HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse,@RequestBody JSONObject<DeclinePoRequest> declineRequest) {
		logger.info("Entry into getAllPurchaseOrders");
		BaseResponse response = new PurchaseOrderResponse();
		try {
			if( declineRequest.getData()!= null && declineRequest.getData().getOrgId()!=null && declineRequest.getData().getPoId()!=null) {
				DeclinePoVo declinePoVo = ApConvertToVoHelper.getInstance().convertVpDeclineVoFromRequest(declineRequest.getData());
				purchaseOrderService.declinePo(declinePoVo);
				setTokens(response,httpServletRequest.getHeader("keyToken"),httpServletRequest.getHeader("valueToken"));
				response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_PO_DECLINE,
						Constants.SUCCESS_PO_DECLINE, Constants.PO_UPDATED_SUCCESSFULLY);
				logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
				return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
			}else {
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_PO_DECLINE,Constants.FAILURE_IN_USER_ORGANIZATION_REQUEST_VALIDATION, Constants.PO_UPDATED_UNSUCCESSFULLY);
				logger.error("Error Payload:"+generateRequestAndResponseLogPaylod(response));
				return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch (Exception e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_PO_DECLINE, e.getMessage(),
					Constants.PO_UPDATED_UNSUCCESSFULLY);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}

package com.blackstrawai.controller.ar;

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

import com.blackstrawai.ar.ArInvoiceService;
import com.blackstrawai.ar.dropdowns.ArInvoicesDropDownVo;
import com.blackstrawai.ar.dropdowns.BasicCustomerDetailsVo;
import com.blackstrawai.ar.invoice.ArInvoiceFilterVo;
import com.blackstrawai.ar.invoice.ArInvoiceListVo;
import com.blackstrawai.ar.invoice.ArInvoiceProductVo;
import com.blackstrawai.ar.invoice.ArInvoiceVo;
import com.blackstrawai.ar.invoice.ArTaxComputationVo;
import com.blackstrawai.common.BaseController;
import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.common.CommonConstants;
import com.blackstrawai.common.Constants;
import com.blackstrawai.helper.ArConvertToVoHelper;
import com.blackstrawai.request.JSONObject;
import com.blackstrawai.request.ar.invoice.ArInvoiceFilterRequest;
import com.blackstrawai.request.ar.invoice.ArInvoiceRequest;
import com.blackstrawai.request.ar.invoice.ArTaxComputationRequest;
import com.blackstrawai.response.ar.invoice.ArInvoiceDropDownResponse;
import com.blackstrawai.response.ar.invoice.ArInvoiceListResponse;
import com.blackstrawai.response.ar.invoice.ArInvoiceResponse;
import com.blackstrawai.response.ar.invoice.ArTaxComputationResponse;
import com.blackstrawai.response.ar.invoice.CustomerDetailsResoponse;

@RestController
@CrossOrigin
@RequestMapping("/decifer/ar/invoice")
public class InvoiceController extends BaseController {
	private Logger logger = Logger.getLogger(InvoiceController.class);
	
	@Autowired
	private ArInvoiceService  invoiceService;
	
	

	@RequestMapping(value="/v1/invoices" , method = RequestMethod.POST)
	public ResponseEntity<BaseResponse>createInvoice(HttpServletRequest httpRequest, HttpServletResponse httpResponse,@RequestBody JSONObject<ArInvoiceRequest> invoiceRequest){
		logger.info("Entered Into createInvoice");
		BaseResponse response = new ArInvoiceResponse();
		if(invoiceRequest.getData().getOrgId()!=null) {
		try {
			logger.info("Request Payload:"+generateRequestAndResponseLogPaylod(invoiceRequest));	
			ArInvoiceVo invoiceVo = ArConvertToVoHelper.getInstance().convertArInvoiceVoFromRequest(invoiceRequest.getData());
			invoiceService.createInvoice(invoiceVo);
			setTokens(response,httpRequest.getHeader("keyToken"),httpRequest.getHeader("valueToken"));
			response = constructResponse(response, Constants.SUCCESS, Constants.BILLS_INVOICE_CREATED_SUCCESSFULLY,Constants.SUCCESS_BILLS_INVOICE_CREATED,Constants.BILLS_INVOICE_CREATED_SUCCESSFULLY);
			logger.info("Response Payload:"+generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		}catch(Exception e) {
			if(e.getCause()!=null){
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_BILLS_INVOICE_CREATED,e.getCause().getMessage(), Constants.BILLS_INVOICE_CREATED_UNSUCCESSFULLY);
			}else{
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_BILLS_INVOICE_CREATED,e.getMessage(), Constants.BILLS_INVOICE_CREATED_UNSUCCESSFULLY);
			}
			logger.error("Error Payload:" , e);
			logger.error("Error Payload:"+generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}	
		}else {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_BILLS_INVOICE_CREATED,Constants.FAILURE_IN_USER_ORGANIZATION_REQUEST_VALIDATION, Constants.BILLS_INVOICE_CREATED_UNSUCCESSFULLY);
			logger.error("Error Payload:"+ generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	// To get the List by applying filters and also without filters 
		@RequestMapping(value = "/v1/invoices/list")
		public ResponseEntity<BaseResponse> getAllInvoices(HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse,@RequestBody JSONObject<ArInvoiceFilterRequest> filterRequest) {
			logger.info("Entry into getAllInvoices");
			BaseResponse response = new ArInvoiceListResponse();
			try {
				if( filterRequest.getData() != null) {
					logger.info("Request Payload:"+generateRequestAndResponseLogPaylod(filterRequest));	
					ArInvoiceFilterVo filterVo = ArConvertToVoHelper.getInstance().convertArInvoiceFilterVoFromRequest(filterRequest.getData());

					List<ArInvoiceListVo> listVos = invoiceService.getAllFilteredInvoicesForOrg(filterVo);
					logger.info("listSize"+listVos.size());
					setTokens(response,httpServletRequest.getHeader("keyToken"),httpServletRequest.getHeader("valueToken"));
					((ArInvoiceListResponse) response).setData(listVos.size()>0? listVos : null);
					response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_BILLS_INVOICE_FETCH,
							Constants.SUCCESS_BILLS_INVOICE_FETCH, Constants.SUCCESS_DURING_GET);
					logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
					return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
				}else {
					response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_BILLS_INVOICE_FETCH,Constants.FAILURE_IN_USER_ORGANIZATION_REQUEST_VALIDATION, Constants.FAILURE_DURING_GET);
					logger.error("Error Payload:"+generateRequestAndResponseLogPaylod(response));
					return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
				}
			} catch (Exception e) {
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_BILLS_INVOICE_FETCH, e.getMessage(),
						Constants.FAILURE_DURING_GET);
				logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
				return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}

		}
		
		@RequestMapping(value = "/v1/invoices/{organizationId}/{organizationName}/{userId}/{roleName}/{id}")
		public ResponseEntity<BaseResponse> getInvoice(HttpServletRequest httpRequest, HttpServletResponse httpResponse,
				@PathVariable int organizationId,
				@PathVariable String organizationName,
				@PathVariable int userId,
				@PathVariable String roleName,
				@PathVariable int id) {
			logger.info("Entry into getInvoice");
			BaseResponse response = new ArInvoiceResponse();
			try {
				ArInvoiceVo invoiceVo = invoiceService.getInvoiceById(id);
				setTokens(response,httpRequest.getHeader("keyToken"),httpRequest.getHeader("valueToken"));
				((ArInvoiceResponse) response).setData(invoiceVo);
				response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_BILLS_INVOICE_FETCH,
						Constants.SUCCESS_BILLS_INVOICE_FETCH, Constants.SUCCESS_DURING_GET);
				logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
				return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
			} catch (Exception e) {
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_BILLS_INVOICE_FETCH, e.getMessage(),
						Constants.FAILURE_DURING_GET);
				logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
				return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		
	@RequestMapping(value = "/v1/dropsdowns/invoices/{organizationId}")
	public ResponseEntity<BaseResponse> getInvoicesDropDown(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, @PathVariable int organizationId) {
		logger.info("Entry into getCustomerDropDown");
		BaseResponse response = new ArInvoiceDropDownResponse();
		try {
			ArInvoicesDropDownVo data = invoiceService.getInvoiceDropDownData(organizationId);
			setTokens(response, httpServletRequest.getHeader("keyToken"), httpServletRequest.getHeader("valueToken"));
			((ArInvoiceDropDownResponse) response).setData(data);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_DROP_DOWN_FETCH,
					Constants.SUCCESS_DROP_DOWN_FETCH, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);

		} catch (Exception e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_BILLS_INVOICE_FETCH, e.getMessage(),
					Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	
	@RequestMapping(value = "/v1/dropsdowns/customer/customers/{organizationId}/{organizationName}/{userId}/{roleName}/{id}")
	public ResponseEntity<BaseResponse> getCustomerDetails(
			HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, 
			@PathVariable int organizationId, 
			@PathVariable String organizationName,
			@PathVariable String userId,
			@PathVariable String roleName,
			@PathVariable int id) {
		logger.info("Entry into method:getCustomer");
		BaseResponse response = new CustomerDetailsResoponse();
		try {
			BasicCustomerDetailsVo customerVo = invoiceService.getCustomerById(id,organizationId);
			setTokens(response,httpRequest.getHeader("keyToken"),httpRequest.getHeader("valueToken"));
			((CustomerDetailsResoponse) response).setData(customerVo);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_CUSTOMER_FETCH,
					Constants.SUCCESS_CUSTOMER_FETCH, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (Exception e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_CUSTOMER_FETCH, e.getMessage(),
					Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value="/v1/invoices/computeTax" )
	public ResponseEntity<BaseResponse>computeTaxCalculation(HttpServletRequest httpRequest, HttpServletResponse httpResponse,@RequestBody JSONObject<ArTaxComputationRequest> taxComputationrequest){
		logger.info("Entered Into computeTaxCalculation");
		BaseResponse response = new ArTaxComputationResponse();
		try {
			logger.info("Request Payload:"+generateRequestAndResponseLogPaylod(taxComputationrequest));	
			List<ArInvoiceProductVo> productVoList = new ArrayList<ArInvoiceProductVo>();
			if(taxComputationrequest.getData()!=null && taxComputationrequest.getData().getProducts()!=null && taxComputationrequest.getData().getOrganizationId()!=null) {
			taxComputationrequest.getData().getProducts().forEach(product->{
				ArInvoiceProductVo productVo = ArConvertToVoHelper.getInstance().convertArInvoiceProductVoFromRequest(product);
				productVoList.add(productVo);
			});
			ArTaxComputationVo computedValue = invoiceService.computeTaxCalculation(productVoList,taxComputationrequest.getData().getOrganizationId(),taxComputationrequest.getData().getIsLocalType(),taxComputationrequest.getData().getIstaxExclusive());
			setTokens(response,httpRequest.getHeader("keyToken"),httpRequest.getHeader("valueToken"));
			((ArTaxComputationResponse) response).setData(computedValue);
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
	
	@RequestMapping(value = "/v1/invoices/{organizationId}/{organizationName}/{userId}/{id}/{roleName}/{status}", method = RequestMethod.DELETE)
	public ResponseEntity<BaseResponse> modifyInvoiceStatus(HttpServletRequest httpRequest, HttpServletResponse httpResponse,
			@PathVariable Integer organizationId,			
			@PathVariable String organizationName,
			@PathVariable String userId, 
			@PathVariable int id,
			@PathVariable String roleName,
			@PathVariable String status) {
		logger.info("Entry into modifyInvoiceStatus");	
	
		BaseResponse response = new ArInvoiceResponse();
			try {
				invoiceService.activateOrDeActivateInvoice(id, status,userId,roleName);
				setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
				if(status.equals(CommonConstants.STATUS_AS_ACTIVE)) {
					response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_BILLS_INVOICE_ACTIVATED,
							Constants.SUCCESS_BILLS_INVOICE_ACTIVATED, Constants.BILLS_INVOICE_DELETED_SUCCESSFULLY);
					logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
				}else if(status.equals(CommonConstants.STATUS_AS_INACTIVE)) {
				response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_BILLS_INVOICE_DEACTIVATED,
						Constants.SUCCESS_BILLS_INVOICE_DEACTIVATED, Constants.BILLS_INVOICE_DELETED_SUCCESSFULLY);
				logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
				}else if(status.equals(CommonConstants.STATUS_AS_VOID)) {
					response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_BILLS_INVOICE_VOIDED,
							Constants.SUCCESS_BILLS_INVOICE_VOIDED, Constants.SUCCESS_CREDIT_NOTES_DELETED);
					logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
				}
				return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
			} catch (Exception e) {
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_BILLS_INVOICE_DELETED, e.getMessage(),
						Constants.BILLS_INVOICE_DELETED_UNSUCCESSFULLY);
				logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
				return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
	}
	
	
	@RequestMapping(value = "/v1/invoices/maxvalue/{organizationId}/{organizationName}")
	public ResponseEntity<Map<String, Integer>> getMaximumAmount(HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse,@PathVariable Integer organizationId,@PathVariable String organizationName ) {
		logger.info("Entry into getMaximumAmount");
		Map<String, Integer> response = new HashMap<String,Integer>();
		try {
				response = invoiceService.getMaxAmountForOrg(organizationId);
				return new ResponseEntity<Map<String, Integer>>(response, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<Map<String, Integer>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	

	@RequestMapping(value="/v1/invoices" , method = RequestMethod.PUT)
	public ResponseEntity<BaseResponse>updateInvoice(HttpServletRequest httpRequest, HttpServletResponse httpResponse,@RequestBody JSONObject<ArInvoiceRequest> invoiceRequest){
		logger.info("Entered Into update Invoice");
		BaseResponse response = new ArInvoiceResponse();
		if(invoiceRequest.getData().getInvoiceId()!=null && invoiceRequest.getData().getOrgId()!=null) {
		try {
			logger.info("Request Payload:"+generateRequestAndResponseLogPaylod(invoiceRequest));	
			ArInvoiceVo invoiceVo= ArConvertToVoHelper.getInstance().convertArInvoiceVoFromRequest(invoiceRequest.getData());
			invoiceService.updateInvoice(invoiceVo);
			setTokens(response,httpRequest.getHeader("keyToken"),httpRequest.getHeader("valueToken"));
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_BILLS_INVOICE_UPDATED,Constants.SUCCESS_BILLS_INVOICE_UPDATED,Constants.BILLS_INVOICE_UPDATED_SUCCESSFULLY);
			logger.info("Response Payload:"+generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		}catch(Exception e) {
			if(e.getCause()!=null){
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_BILLS_INVOICE_UPDATED,e.getCause().getMessage(), Constants.BILLS_INVOICE_UPDATED_UNSUCCESSFULLY);
			}else{
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_BILLS_INVOICE_UPDATED,e.getMessage(), Constants.BILLS_INVOICE_UPDATED_UNSUCCESSFULLY);
			}
			logger.error("Error Payload:"+generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}	
		}else {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_BILLS_INVOICE_UPDATED,Constants.FAILURE_IN_USER_ORGANIZATION_REQUEST_VALIDATION, Constants.BILLS_INVOICE_UPDATED_UNSUCCESSFULLY);
			logger.error("Error Payload:"+ generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/v1/invoices/dashboard/{organizationId}/{organizationName}/{userId}/{roleName}", method = RequestMethod.GET)
	public ResponseEntity<BaseResponse> getRecentInvoicesOfAnOrganizationForDashboard(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @PathVariable int organizationId, @PathVariable String organizationName,
			@PathVariable String userId, @PathVariable String roleName) {
		logger.info("Entry into getRecentInvoicesOfAnOrganizationForDashboard");
		BaseResponse response = new ArInvoiceListResponse();
		try {
			List<ArInvoiceListVo> listAllInvoices = invoiceService
					.getRecentInvoicesOfAnOrganizationForDashboard(organizationId);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((ArInvoiceListResponse) response).setData(listAllInvoices);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_BILLS_INVOICE_FETCH,
					Constants.SUCCESS_BILLS_INVOICE_FETCH, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (Exception e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_BILLS_INVOICE_FETCH,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
}

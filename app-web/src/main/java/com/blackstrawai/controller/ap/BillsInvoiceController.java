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

import com.blackstrawai.ApplicationException;
import com.blackstrawai.ap.BillsInvoiceService;
import com.blackstrawai.ap.billsinvoice.BillsInvoiceDashboardVo;
import com.blackstrawai.ap.billsinvoice.InvoiceFilterVo;
import com.blackstrawai.ap.billsinvoice.InvoiceListVo;
import com.blackstrawai.ap.billsinvoice.InvoiceProductVo;
import com.blackstrawai.ap.billsinvoice.InvoiceVo;
import com.blackstrawai.ap.billsinvoice.QuickInvoiceVo;
import com.blackstrawai.ap.billsinvoice.TaxComputationVo;
import com.blackstrawai.ap.dropdowns.InvoiceDropDownVo;
import com.blackstrawai.common.BaseController;
import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.common.CommonConstants;
import com.blackstrawai.common.Constants;
import com.blackstrawai.helper.ApConvertToVoHelper;
import com.blackstrawai.request.JSONObject;
import com.blackstrawai.request.ap.billsinvoice.InvoiceFilterRequest;
import com.blackstrawai.request.ap.billsinvoice.InvoiceRequest;
import com.blackstrawai.request.ap.billsinvoice.QuickInvoiceRequest;
import com.blackstrawai.request.ap.billsinvoice.TaxComputationRequest;
import com.blackstrawai.response.ap.ListPaymentNonCoreDashboardResponse;
import com.blackstrawai.response.ap.billsinvoice.InvoiceListResponse;
import com.blackstrawai.response.ap.billsinvoice.InvoiceResponse;
import com.blackstrawai.response.ap.billsinvoice.InvoicesDropDownResponse;
import com.blackstrawai.response.ap.billsinvoice.QuickInvoiceResponse;
import com.blackstrawai.response.ap.billsinvoice.TaxComputationResponse;

@RestController
@CrossOrigin
@RequestMapping("/decifer/ap/billsinvoice")
public class BillsInvoiceController extends BaseController{
	private Logger logger = Logger.getLogger(BillsInvoiceController.class);
	
	@Autowired
	private BillsInvoiceService  billsInvoiceService;
	
	@RequestMapping(value="/v1/invoices" , method = RequestMethod.POST)
	public ResponseEntity<BaseResponse>createInvoice(HttpServletRequest httpRequest, HttpServletResponse httpResponse,@RequestBody JSONObject<InvoiceRequest> invoiceRequest){
		logger.info("Entered Into createInvoice");
		BaseResponse response = new InvoiceResponse();
		if(invoiceRequest.getData().getIsSuperAdmin()!=null && invoiceRequest.getData().getUserId()!=null && invoiceRequest.getData().getOrganizationId()!=null) {
		try {
			logger.info("Request Payload:"+generateRequestAndResponseLogPaylod(invoiceRequest));	
			InvoiceVo invoiceVo = ApConvertToVoHelper.getInstance().convertBillsInvoiceVoFromRequest(invoiceRequest.getData());
			billsInvoiceService.createInvoice(invoiceVo);
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
	
	@RequestMapping(value = "/v1/dropsdowns/invoices/{organizationId}")
	public ResponseEntity<BaseResponse> getInvoicesDropDown(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, @PathVariable int organizationId) {
		logger.info("Entry into getCustomerDropDown");
		BaseResponse response = new InvoicesDropDownResponse();
		try {
			InvoiceDropDownVo data = billsInvoiceService.getInvoiceDropDownData(organizationId);
			setTokens(response, httpServletRequest.getHeader("keyToken"), httpServletRequest.getHeader("valueToken"));
			((InvoicesDropDownResponse) response).setData(data);
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

	
	@RequestMapping(value="/v1/invoices/computeTax" , method = RequestMethod.POST)
	public ResponseEntity<BaseResponse>computeTaxCalculation(HttpServletRequest httpRequest, HttpServletResponse httpResponse,@RequestBody JSONObject<TaxComputationRequest> taxComputationrequest){
		logger.info("Entered Into computeTaxCalculation");
		BaseResponse response = new TaxComputationResponse();
		try {
			logger.info("Request Payload:"+generateRequestAndResponseLogPaylod(taxComputationrequest));	
			List<InvoiceProductVo> productVoList = new ArrayList<InvoiceProductVo>();
			if(taxComputationrequest.getData()!=null && taxComputationrequest.getData().getProducts()!=null && taxComputationrequest.getData().getOrganizationId()!=null) {
			taxComputationrequest.getData().getProducts().forEach(product->{
				InvoiceProductVo productVo = ApConvertToVoHelper.getInstance().convertInvoiceProductVoFromRequest(product);
				productVoList.add(productVo);
			});
			TaxComputationVo computedValue = billsInvoiceService.computeTaxCalculation(productVoList,taxComputationrequest.getData().getOrganizationId(),taxComputationrequest.getData().getIsExclusive());
			setTokens(response,httpRequest.getHeader("keyToken"),httpRequest.getHeader("valueToken"));
			((TaxComputationResponse) response).setData(computedValue);
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

	// To get all the Invoices --> Currently not using this as this API is merged along with filter 
	@RequestMapping(value = "/v1/invoices/{organizationId}/{userId}/{isInvoiceWithBills}")
	public ResponseEntity<BaseResponse> getAllInvoices(HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse,@PathVariable Integer organizationId,@PathVariable String userId,@PathVariable Boolean isInvoiceWithBills) {
		logger.info("Entry into getAllInvoices");
		BaseResponse response = new InvoiceListResponse();
		try {
			if( organizationId != null) {
				List<InvoiceListVo> listVos = billsInvoiceService.getAllInvoicesForOrg(organizationId, isInvoiceWithBills);
				logger.info("listSize"+listVos.size());
				setTokens(response,httpServletRequest.getHeader("keyToken"),httpServletRequest.getHeader("valueToken"));
				((InvoiceListResponse) response).setData(listVos.size()>0? listVos : null);
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
	
	// To get the List by applying filters and also without filters 
	@RequestMapping(value = "/v1/invoices/list")
	public ResponseEntity<BaseResponse> getAllInvoices(HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse,@RequestBody JSONObject<InvoiceFilterRequest> filterRequest) {
		logger.info("Entry into getAllInvoices");
		BaseResponse response = new InvoiceListResponse();
		try {
			if( filterRequest.getData() != null) {
				logger.info("Request Payload:"+generateRequestAndResponseLogPaylod(filterRequest));	
				InvoiceFilterVo filterVo = ApConvertToVoHelper.getInstance().convertInvoiceFilterVoFromRequest(filterRequest.getData());

				List<InvoiceListVo> listVos = billsInvoiceService.getAllFilteredInvoicesForOrg(filterVo);
				logger.info("listSize"+listVos.size());
				setTokens(response,httpServletRequest.getHeader("keyToken"),httpServletRequest.getHeader("valueToken"));
				((InvoiceListResponse) response).setData(listVos.size()>0? listVos : null);
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
		BaseResponse response = new InvoiceResponse();
		try {
			InvoiceVo invoiceVo = billsInvoiceService.getInvoiceById(id);
			setTokens(response,httpRequest.getHeader("keyToken"),httpRequest.getHeader("valueToken"));
			((InvoiceResponse) response).setData(invoiceVo);
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
	
	@RequestMapping(value = "/v1/invoices/{organizationId}/{organizationName}/{userId}/{id}/{roleName}/{status}", method = RequestMethod.DELETE)
	public ResponseEntity<BaseResponse> modifyInvoiceStatus(HttpServletRequest httpRequest, HttpServletResponse httpResponse,
			@PathVariable Integer organizationId,			
			@PathVariable String organizationName,
			@PathVariable String userId, 
			@PathVariable int id,
			@PathVariable String roleName,
			@PathVariable String status) {
		logger.info("Entry into modifyInvoiceStatus");	
	
		BaseResponse response = new InvoiceResponse();
			try {
				billsInvoiceService.activateOrDeActivateInvoice(id, status,userId,roleName);
				setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
				if(status.equals(CommonConstants.STATUS_AS_ACTIVE)) {
					response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_BILLS_INVOICE_ACTIVATED,
							Constants.SUCCESS_BILLS_INVOICE_ACTIVATED, Constants.BILLS_INVOICE_DELETED_SUCCESSFULLY);
					
				}else if(status.equals(CommonConstants.STATUS_AS_VOID)) {
				response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_BILLS_INVOICE_VOIDED,
						Constants.SUCCESS_BILLS_INVOICE_VOIDED, Constants.BILLS_INVOICE_VOIDED_SUCCESSFULLY);
				}else if(status.equals(CommonConstants.STATUS_AS_INACTIVE)) {
					response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_BILLS_INVOICE_DEACTIVATED,
							Constants.SUCCESS_BILLS_INVOICE_DEACTIVATED, Constants.BILLS_INVOICE_DELETED_SUCCESSFULLY);

				}
				logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
				
				return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
			} catch (Exception e) {
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_BILLS_INVOICE_DELETED, e.getMessage(),
						Constants.BILLS_INVOICE_DELETED_UNSUCCESSFULLY);
				logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
				return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
	}
	
	@RequestMapping(value="/v1/invoices" , method = RequestMethod.PUT)
	public ResponseEntity<BaseResponse>updateInvoice(HttpServletRequest httpRequest, HttpServletResponse httpResponse,@RequestBody JSONObject<InvoiceRequest> invoiceRequest){
		logger.info("Entered Into update Invoice");
		BaseResponse response = new InvoiceResponse();
		if(invoiceRequest.getData().getId()!=null && invoiceRequest.getData().getIsSuperAdmin()!=null && invoiceRequest.getData().getUserId()!=null && invoiceRequest.getData().getOrganizationId()!=null) {
		try {
			logger.info("Request Payload:"+generateRequestAndResponseLogPaylod(invoiceRequest));	
			InvoiceVo invoiceVo= ApConvertToVoHelper.getInstance().convertBillsInvoiceVoFromRequest(invoiceRequest.getData());
			billsInvoiceService.updateInvoice(invoiceVo);
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
	
	
	@RequestMapping(value = "/v1/invoices/maxvalue/{organizationId}/{organizationName}/{isInvoiceWIthBills}")
	public ResponseEntity<Map<String, Integer>> getMaximumAmount(HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse,@PathVariable Integer organizationId,@PathVariable String organizationName ,@PathVariable Boolean isInvoiceWIthBills) {
		logger.info("Entry into getMaximumAmount");
		Map<String, Integer> response = new HashMap<String,Integer>();
		try {
				response = billsInvoiceService.getMaxAmountForOrg(organizationId,isInvoiceWIthBills);
				return new ResponseEntity<Map<String, Integer>>(response, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<Map<String, Integer>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	
	
	//Vendor portal Quick invoices 
	@RequestMapping(value="/v1/quickinvoices" , method = RequestMethod.POST)
	public ResponseEntity<BaseResponse>createQuickInvoice(HttpServletRequest httpRequest, HttpServletResponse httpResponse,@RequestBody JSONObject<QuickInvoiceRequest> invoiceRequest){
		logger.info("Entered Into createInvoice");
		BaseResponse response = new QuickInvoiceResponse();
		if(invoiceRequest.getData().getIsSuperAdmin()!=null && invoiceRequest.getData().getOrganizationId()!=null) {
		try {
			logger.info("Request Payload:"+generateRequestAndResponseLogPaylod(invoiceRequest));	
			QuickInvoiceVo invoiceVo = ApConvertToVoHelper.getInstance().convertQuickInvoiceVoFromRequest(invoiceRequest.getData());
			billsInvoiceService.createQuickInvoice(invoiceVo);
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
	
	@RequestMapping(value = "/v1/quickinvoices/{organizationId}/{organizationName}/{userId}/{roleName}/{id}")
	public ResponseEntity<BaseResponse> getQuickInvoice(HttpServletRequest httpRequest, HttpServletResponse httpResponse,
			@PathVariable int organizationId,
			@PathVariable String organizationName,
			@PathVariable int userId,
			@PathVariable String roleName,
			@PathVariable int id) {
		logger.info("Entry into getQuickInvoice");
		BaseResponse response = new QuickInvoiceResponse();
		try {
			QuickInvoiceVo invoiceVo = billsInvoiceService.getQuickInvoiceById(id);
			setTokens(response,httpRequest.getHeader("keyToken"),httpRequest.getHeader("valueToken"));
			((QuickInvoiceResponse) response).setData(invoiceVo);
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
	
	
	@RequestMapping(value="/v1/quickinvoices" , method = RequestMethod.PUT)
	public ResponseEntity<BaseResponse>updateQuickInvoice(HttpServletRequest httpRequest, HttpServletResponse httpResponse,@RequestBody JSONObject<QuickInvoiceRequest> invoiceRequest){
		logger.info("Entered Into update Invoice");
		BaseResponse response = new QuickInvoiceResponse();
		if(invoiceRequest.getData().getId()!=null && invoiceRequest.getData().getIsSuperAdmin()!=null && invoiceRequest.getData().getUserId()!=null && invoiceRequest.getData().getOrganizationId()!=null) {
		try {
			logger.info("Request Payload:"+generateRequestAndResponseLogPaylod(invoiceRequest));	
			QuickInvoiceVo invoiceVo= ApConvertToVoHelper.getInstance().convertQuickInvoiceVoFromRequest(invoiceRequest.getData());
			billsInvoiceService.updateQuickInvoice(invoiceVo);
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
	
	@RequestMapping(value = "/v1/dashboard/{organizationId}/{organizationName}/{userId}/{roleName}", method = RequestMethod.GET)
	public ResponseEntity<BaseResponse> getRecentInvoicesOfAnOrganizationForUserAndRole(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @PathVariable int organizationId, @PathVariable String organizationName,
			@PathVariable String userId, @PathVariable String roleName) {
		logger.info("Entry into getRecentPaymentsOfAnOrganizationForUserAndRole");
		BaseResponse response = new ListPaymentNonCoreDashboardResponse();
		try {
			List<BillsInvoiceDashboardVo> listAllPayments = billsInvoiceService
					.getRecentInvoicesOfAnOrganizationForUserAndRole(organizationId, userId, roleName);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((ListPaymentNonCoreDashboardResponse) response).setData(listAllPayments);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_BILLS_INVOICE_FETCH,
					Constants.SUCCESS_BILLS_INVOICE_FETCH, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_BILLS_INVOICE_FETCH,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	@RequestMapping(value = "/v1/due_invoices/{organizationId}/{organizationName}/{userId}/{roleName}", method = RequestMethod.GET)
	public ResponseEntity<BaseResponse> getDueInvoicesOfAnOrganizationForUserAndRole(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @PathVariable int organizationId, @PathVariable String organizationName,
			@PathVariable String userId, @PathVariable String roleName) {
		logger.info("Entry into getRecentPaymentsOfAnOrganizationForUserAndRole");
		BaseResponse response = new InvoiceListResponse();
		try {
			List<InvoiceListVo> listVos = billsInvoiceService.getDueInvoicesOfAnOrganizationForUserAndRole(organizationId, userId, roleName);
			logger.info("listSize"+listVos.size());
			setTokens(response,httpRequest.getHeader("keyToken"),httpRequest.getHeader("valueToken"));
			((InvoiceListResponse) response).setData(listVos.size()>0? listVos : null);
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

	@RequestMapping(value = "/v1/invoices/invoice_exist/{organizationId}/{vendorId}/{invoiceNo}")
	public ResponseEntity<Map<String, Boolean>> checkInvoiceNoExistForOrganization(HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse,@PathVariable Integer organizationId,@PathVariable Integer vendorId ,@PathVariable String invoiceNo) {
		logger.info("Entry into checkInvoiceNoExistForOrganization"
				+ "checkInvoiceNoExistForOrganization");
		Map<String, Boolean> response = new HashMap<String,Boolean>();
		try {
			response = billsInvoiceService.checkInvoiceNoExistForOrganization(organizationId,vendorId,invoiceNo);
				return new ResponseEntity<Map<String, Boolean>>(response, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<Map<String, Boolean>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

}

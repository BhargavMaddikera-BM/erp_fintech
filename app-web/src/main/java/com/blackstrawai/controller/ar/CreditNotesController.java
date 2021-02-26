package com.blackstrawai.controller.ar;

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
import com.blackstrawai.ar.CreditNotesService;
import com.blackstrawai.ar.creditnotes.CreditNotesVo;
import com.blackstrawai.ar.dropdowns.CreditNoteDropdownVo;
import com.blackstrawai.ar.dropdowns.CustomerAccountsDropdownVo;
import com.blackstrawai.ar.invoice.ArInvoiceFilterVo;
import com.blackstrawai.common.BaseController;
import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.common.CommonConstants;
import com.blackstrawai.common.Constants;
import com.blackstrawai.helper.ArConvertToVoHelper;
import com.blackstrawai.request.JSONObject;
import com.blackstrawai.request.ar.creditnotes.CreditNotesRequest;
import com.blackstrawai.request.ar.invoice.ArInvoiceFilterRequest;
import com.blackstrawai.response.ar.creditnotes.CreditNotesDropdownResponse;
import com.blackstrawai.response.ar.creditnotes.CreditNotesResponse;
import com.blackstrawai.response.ar.creditnotes.CustomerAccountsDropdownResponse;
import com.blackstrawai.response.ar.creditnotes.ListCreditNotesResponse;


@RestController
@CrossOrigin
//This will be part of accountsreceivable. Hence ar.
@RequestMapping("/decifer/ar/credit_note")
public class CreditNotesController extends BaseController {

	@Autowired
	CreditNotesService creditNotesService;
	
	
	private Logger logger = Logger.getLogger(CreditNotesController.class);

	// For Creating CreditNotes
	@RequestMapping(value = "/v1/credit_notes", method = RequestMethod.POST)
	public ResponseEntity<BaseResponse> createCreditNotes(HttpServletRequest httpRequest, HttpServletResponse httpResponse,
			@RequestBody JSONObject<CreditNotesRequest> creditNotesRequest) {
		logger.info("Entry into method: createCreditNotes");
		BaseResponse response = new CreditNotesResponse();
		try {
			logger.info("Request Payload:" + generateRequestAndResponseLogPaylod(creditNotesRequest));
			CreditNotesVo creditNotesVo= ArConvertToVoHelper.getInstance()
					.convertCreditNotesVoFromCreditNotesRequest(creditNotesRequest.getData());
			creditNotesVo= creditNotesService.createCreditNotes(creditNotesVo);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
		
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_CREDIT_NOTES_CREATED,
					Constants.SUCCESS_CREDIT_NOTES_CREATED, Constants.CREDIT_NOTES_CREATED_SUCCESSFULLY);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);

		} catch (ApplicationException e) {
			if(e.getCause()!=null){
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_CREDIT_NOTES_CREATED,
						e.getCause().getMessage(), Constants.CREDIT_NOTES_CREATED_UNSUCCESSFULLY);
			}else{
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_CREDIT_NOTES_CREATED,
						e.getMessage(), Constants.CREDIT_NOTES_CREATED_UNSUCCESSFULLY);
			}			
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// For Fetching all CreditNotes details
	@RequestMapping(value = "/v1/credit_notes/list/{userId}/{roleName}", method = RequestMethod.POST)
	public ResponseEntity<BaseResponse> getAllCreditNotesByFIlter(HttpServletRequest httpRequest,HttpServletResponse httpResponse,@PathVariable String userId,
			@PathVariable String roleName,@RequestBody JSONObject<ArInvoiceFilterRequest> filterRequest) {
		logger.info("Entry into method:getAllCreditNoteOfAnOrganization");
		BaseResponse response = new ListCreditNotesResponse();
		try {			
			if( filterRequest.getData() != null) {
			logger.info("Request Payload:"+generateRequestAndResponseLogPaylod(filterRequest));	
			ArInvoiceFilterVo filterVo = ArConvertToVoHelper.getInstance().convertArInvoiceFilterVoFromRequest(filterRequest.getData());

			List<CreditNotesVo> listVos = creditNotesService.getAllCreditNotesByFilter(filterVo,userId,roleName);
			logger.info("listSize"+listVos.size());
			setTokens(response,httpRequest.getHeader("keyToken"),httpRequest.getHeader("valueToken"));
			((ListCreditNotesResponse) response).setData(listVos.size()>0? listVos : null);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_CREDIT_NOTES_FETCH,
					Constants.SUCCESS_CREDIT_NOTES_FETCH, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		}else {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_CREDIT_NOTES_FETCH,Constants.FAILURE_IN_USER_ORGANIZATION_REQUEST_VALIDATION, Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:"+generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_CREDIT_NOTES_FETCH,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/v1/credit_notes/maxvalue/{organizationId}/{organizationName}")
	public ResponseEntity<Map<String, Integer>> getMaximumAmount(HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse,@PathVariable Integer organizationId,@PathVariable String organizationName ) {
		logger.info("Entry into getMaximumAmount");
		Map<String, Integer> response = new HashMap<String,Integer>();
		try {
				response = creditNotesService.getMaxAmountForOrg(organizationId);
				return new ResponseEntity<Map<String, Integer>>(response, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<Map<String, Integer>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	// For activateOrDeactivate CreditNotes

	@RequestMapping(value = "/v1/credit_notes/{organizationId}/{organizationName}/{userId}/{id}/{roleName}/{status}", method = RequestMethod.DELETE)
	public ResponseEntity<BaseResponse> deleteCreditNotes(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, 
			@PathVariable int organizationId, 
			@PathVariable String organizationName,
			@PathVariable String userId, 
			@PathVariable int id,
			@PathVariable String roleName,
			@PathVariable String status) {
		logger.info("Entry into deleteCreditNotes");
		BaseResponse response = new CreditNotesResponse();
		try {
			creditNotesService.deleteCreditNotes(id, status,userId,roleName);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));

			if(status.equals(CommonConstants.STATUS_AS_ACTIVE)) {
				response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_CREDIT_NOTES_ACTIVATED,
						Constants.SUCCESS_CREDIT_NOTES_ACTIVATED, Constants.SUCCESS_CREDIT_NOTES_DELETED);
				logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			}else if(status.equals(CommonConstants.STATUS_AS_INACTIVE)) {
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_CREDIT_NOTES_DEACTIVATED,
					Constants.SUCCESS_CREDIT_NOTES_DEACTIVATED, Constants.SUCCESS_CREDIT_NOTES_DELETED);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			}else if(status.equals(CommonConstants.STATUS_AS_VOID)) {
				response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_CREDIT_NOTES_VOIDED,
						Constants.SUCCESS_CREDIT_NOTES_VOIDED, Constants.SUCCESS_CREDIT_NOTES_DELETED);
				logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			}
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_CREDIT_NOTES_DELETED,
					e.getMessage(), Constants.CREDIT_NOTES_DELETED_UNSUCCESSFULLY);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@RequestMapping(value = "/v1/credit_notes", method = RequestMethod.PUT)
	public ResponseEntity<BaseResponse> updateCreditNotes(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @RequestBody JSONObject<CreditNotesRequest> creditNotesRequest) {
		logger.info("Entry into method:updateCreditNotes");
		BaseResponse response = new CreditNotesResponse();
		try {
			logger.info("Request Payload:" + generateRequestAndResponseLogPaylod(creditNotesRequest));
			CreditNotesVo creditNotesVo= ArConvertToVoHelper.getInstance()
					.convertCreditNotesVoFromCreditNotesRequest(creditNotesRequest.getData());
			creditNotesVo= creditNotesService.updateCreditNotes(creditNotesVo);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			/*
			 * creditNotesVo.setKeyToken(null); creditNotesVo.setValueToken(null);
			 * ((CreditNotesResponse) response).setData(creditNotesVo);
			 */
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_CREDIT_NOTES_UPDATED,
					Constants.SUCCESS_CREDIT_NOTES_UPDATED, Constants.CREDIT_NOTES_UPDATED_SUCCESSFULLY);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_CREDIT_NOTES_UPDATED,
					e.getMessage(), Constants.CREDIT_NOTES_UPDATED_UNSUCCESSFULLY);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	// For Fetching CreditNotes by Id
	@RequestMapping(value = "/v1/credit_notes/{organizationId}/{organizationName}/{userId}/{roleName}/{id}", method = RequestMethod.GET)
	public ResponseEntity<BaseResponse> getCreditNotesById(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse,@PathVariable int organizationId, @PathVariable String organizationName, @PathVariable int userId,
			@PathVariable String roleName, @PathVariable int id) {
		logger.info("Entry into method:getCreditNotesById");
		BaseResponse response = new CreditNotesResponse();
		try {
			CreditNotesVo creditNotesVo = creditNotesService.getCreditNotesById(id);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((CreditNotesResponse) response).setData(creditNotesVo);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_CREDIT_NOTES_FETCH,
					Constants.SUCCESS_CREDIT_NOTES_FETCH, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_CREDIT_NOTES_FETCH,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	

	
	// For Fetching CreditNote Dropdowns by orgId
		@RequestMapping(value = "/v1/dropsdowns/customer/customers/{organizationId}/{organizationName}/{userId}/{roleName}/{customerId}/{creditNoteId}", method = RequestMethod.GET)
		public ResponseEntity<BaseResponse> getAllInvoicesDropdownDataForCustomerByOrg(HttpServletRequest httpRequest,
				HttpServletResponse httpResponse, 
				@PathVariable int organizationId, 
				@PathVariable String organizationName,
				@PathVariable String userId,
				@PathVariable String roleName,
				@PathVariable int customerId,@PathVariable int creditNoteId) {
			logger.info("Entry into method: getAllInvoicesDropdownDataForCustomerByOrg");
			BaseResponse response = new CreditNotesDropdownResponse();
			try {
				CreditNoteDropdownVo CreditNoteVo = creditNotesService.getCreditNoteDropdownData(organizationId,customerId,creditNoteId);
				setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
				((CreditNotesDropdownResponse) response).setData(CreditNoteVo);
				response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_DROP_DOWN_FETCH,
						Constants.SUCCESS_DROP_DOWN_FETCH, Constants.SUCCESS_DURING_GET);
				//logger.debug("Response Payload:" + generateRequestAndResponseLogPaylod(response));
				return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
			} catch (ApplicationException e) {
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_DROP_DOWN_FETCH,
						e.getMessage(), Constants.FAILURE_DURING_GET);
				logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
				logger.info(response);
				return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		
		// For Fetching CreditNote Dropdowns by orgId
		@RequestMapping(value = "/v1/dropsdown/customers_accounts/{organizationId}", method = RequestMethod.GET)
		public ResponseEntity<BaseResponse> getCustomersAndAccountsDropdownDataForOrg(HttpServletRequest httpRequest,
				HttpServletResponse httpResponse,@PathVariable int organizationId) {
			logger.info("Entry into method: getAllCustomersDropdownDataForOrg");
			BaseResponse response = new CustomerAccountsDropdownResponse();
			try {
				CustomerAccountsDropdownVo creditNoteVo = creditNotesService.getCustomersAndAccountsDropdownData(organizationId);
				setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
				((CustomerAccountsDropdownResponse) response).setData(creditNoteVo);
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

}


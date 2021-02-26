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

import com.blackstrawai.ar.template.CreditNoteTemplateVo;
import com.blackstrawai.ar.template.CreditNoteTemplateinformationVo;
import com.blackstrawai.ar.template.InvoiceTemplateVo;
import com.blackstrawai.ar.template.InvoiceTemplateinformationVo;
import com.blackstrawai.common.BaseController;
import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.common.Constants;
import com.blackstrawai.helper.ArTemplateConvertToVoHelper;
import com.blackstrawai.request.JSONObject;
import com.blackstrawai.request.ar.template.CreditNoteTemplateRequest;
import com.blackstrawai.request.ar.template.InvoiceTemplateRequest;
import com.blackstrawai.response.ar.template.CreditNoteTemplateInfoListResponse;
import com.blackstrawai.response.ar.template.CreditNoteTemplateListResponse;
import com.blackstrawai.response.ar.template.CreditNoteTemplateResponse;
import com.blackstrawai.response.ar.template.InvoiceTemplateInfoListResponse;
import com.blackstrawai.response.ar.template.InvoiceTemplateListResponse;
import com.blackstrawai.response.ar.template.InvoiceTemplateResponse;
import com.blackstrawai.template.ArTemplateService;

@RestController
@CrossOrigin
@RequestMapping("/decifer/ar/template")
public class ArTemplateController extends BaseController {
	private Logger logger = Logger.getLogger(ArTemplateController.class);

	@Autowired
	private ArTemplateService  templateService;

	@RequestMapping(value="/v1/templates/invoice" , method = RequestMethod.POST)
	public ResponseEntity<BaseResponse>createInvoiceTemplate(HttpServletRequest httpRequest, HttpServletResponse httpResponse,@RequestBody JSONObject<InvoiceTemplateRequest> arinvoiceTemplateRequest){
		logger.info("Entered Into createInvoiceTemplate");
		BaseResponse response = new InvoiceTemplateResponse();
		if(arinvoiceTemplateRequest.getData().getOrgId()!=null) {
			try {
				logger.info("Request Payload:"+generateRequestAndResponseLogPaylod(arinvoiceTemplateRequest));	
				InvoiceTemplateVo invoiceTempVo = ArTemplateConvertToVoHelper.getInstance().convertArInvoiceTemplateVoFromRequest(arinvoiceTemplateRequest.getData());
				invoiceTempVo = templateService.createInvoiceTemplate(invoiceTempVo);
				setTokens(response,httpRequest.getHeader("keyToken"),httpRequest.getHeader("valueToken"));
				((InvoiceTemplateResponse)response).setData(invoiceTempVo);
				response = constructResponse(response, Constants.SUCCESS, Constants.AR_INVOICE_TEMPLATE_CREATED_SUCCESSFULLY,Constants.SUCCESS_AR_INVOICE_TEMPLATE_CREATED,Constants.AR_INVOICE_TEMPLATE_CREATED_SUCCESSFULLY);
				logger.info("Response Payload:"+generateRequestAndResponseLogPaylod(response));
				return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
			}catch(Exception e) {
				if(e.getCause()!=null){
					response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_AR_INVOICE_TEMPLATE_CREATED,e.getCause().getMessage(), Constants.AR_INVOICE_TEMPLATE_CREATED_UNSUCCESSFULLY);
				}else{
					response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_AR_INVOICE_TEMPLATE_CREATED,e.getMessage(), Constants.AR_INVOICE_TEMPLATE_CREATED_UNSUCCESSFULLY);
				}
				logger.error("Error Payload:" , e);
				logger.error("Error Payload:"+generateRequestAndResponseLogPaylod(response));
				return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}	
		}else {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_AR_INVOICE_TEMPLATE_CREATED,Constants.FAILURE_IN_USER_ORGANIZATION_REQUEST_VALIDATION, Constants.AR_INVOICE_TEMPLATE_CREATED_UNSUCCESSFULLY);
			logger.error("Error Payload:"+ generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}



	@RequestMapping(value = "/v1/templates/invoice/{organizationId}/{organizationName}/{userId}/{roleName}/{id}")
	public ResponseEntity<BaseResponse> getInvoiceTemplate(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, 
			@PathVariable int organizationId, 
			@PathVariable String organizationName,
			@PathVariable String userId,
			@PathVariable String roleName,
			@PathVariable int id)
	{
		logger.info("Entry into getInvoiceTemplate"); BaseResponse
		response = new InvoiceTemplateResponse(); 
		try { 
			InvoiceTemplateVo invoiceTempVo = templateService.getInvoiceTemplate(organizationId,id);
			setTokens(response,httpRequest.getHeader("keyToken"),httpRequest.getHeader("valueToken")); 
			((InvoiceTemplateResponse)response).setData(invoiceTempVo);
			response = constructResponse(response,Constants.SUCCESS, Constants.SUCCESS_AR_INVOICE_TEMPLATE_FETCH,Constants.SUCCESS_AR_INVOICE_TEMPLATE_FETCH, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new  ResponseEntity<BaseResponse>(response, HttpStatus.OK); 
		} catch (Exception e)
		{ 
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_AR_INVOICE_TEMPLATE_FETCH, e.getMessage(),Constants.FAILURE_DURING_GET); logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		} 
	}
	
	@RequestMapping(value = "/v1/templates/invoice/{organizationId}/{organizationName}/{userId}/{roleName}")
	public ResponseEntity<BaseResponse> getAllInvoiceTemplate(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @PathVariable int organizationId,@PathVariable String organizationName,
			@PathVariable String userId,@PathVariable String roleName)
	{
		logger.info("Entry into getAllInvoiceTemplate"); BaseResponse
		response = new InvoiceTemplateInfoListResponse(); 
		try { 
			List<InvoiceTemplateinformationVo> invoiceTempVo = templateService.getAllInvoiceTemplate(organizationId,userId,roleName);
			setTokens(response,httpRequest.getHeader("keyToken"),httpRequest.getHeader("valueToken")); 
			((InvoiceTemplateInfoListResponse)response).setInvoiceTemplate(invoiceTempVo);
			response = constructResponse(response,Constants.SUCCESS, Constants.SUCCESS_AR_INVOICE_TEMPLATE_FETCH,Constants.SUCCESS_AR_INVOICE_TEMPLATE_FETCH, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new  ResponseEntity<BaseResponse>(response, HttpStatus.OK); 
		} catch (Exception e)
		{ 
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_AR_INVOICE_TEMPLATE_FETCH, e.getMessage(),Constants.FAILURE_DURING_GET); logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		} 
	}
	
	
	@RequestMapping(value = "/v1/templates/invoice/{organizationId}")
	public ResponseEntity<BaseResponse> getAllInvoiceTemplateOfAnOrganization(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @PathVariable int organizationId)
	{
		logger.info("Entry into getAllInvoiceTemplate"); BaseResponse
		response = new InvoiceTemplateListResponse(); 
		try { 
			List<InvoiceTemplateVo> invoiceTempVo = templateService.getAllInvoiceTemplatePerOrganization(organizationId);
			setTokens(response,httpRequest.getHeader("keyToken"),httpRequest.getHeader("valueToken")); 
			((InvoiceTemplateListResponse)response).setInvoiceTemplate(invoiceTempVo);
			response = constructResponse(response,Constants.SUCCESS, Constants.SUCCESS_AR_INVOICE_TEMPLATE_FETCH,Constants.SUCCESS_AR_INVOICE_TEMPLATE_FETCH, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new  ResponseEntity<BaseResponse>(response, HttpStatus.OK); 
		} catch (Exception e)
		{ 
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_AR_INVOICE_TEMPLATE_FETCH, e.getMessage(),Constants.FAILURE_DURING_GET); logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		} 
	}
	
	
	@RequestMapping(value = "/v1/templates/invoice/default/{organizationId}")
	public ResponseEntity<BaseResponse> getDefaultInvoiceTemplateOfAnOrganization(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @PathVariable int organizationId)
	{
		logger.info("Entry into getDefaultInvoiceTemplateOfAnOrganization"); 
		BaseResponse response = new InvoiceTemplateResponse(); 
		try { 
			InvoiceTemplateVo invoiceTempVo = templateService.getDefaultTemplateOfAnOrganization(organizationId);
			setTokens(response,httpRequest.getHeader("keyToken"),httpRequest.getHeader("valueToken")); 
			((InvoiceTemplateResponse)response).setData(invoiceTempVo);
			response = constructResponse(response,Constants.SUCCESS, Constants.SUCCESS_AR_INVOICE_TEMPLATE_FETCH,Constants.SUCCESS_AR_INVOICE_TEMPLATE_FETCH, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new  ResponseEntity<BaseResponse>(response, HttpStatus.OK); 
		} catch (Exception e)
		{ 
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_AR_INVOICE_TEMPLATE_FETCH, e.getMessage(),Constants.FAILURE_DURING_GET); logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		} 
	}


	@RequestMapping(value="/v1/templates/invoice" , method = RequestMethod.PUT)
	public ResponseEntity<BaseResponse>updateInvoiceTemplate(HttpServletRequest httpRequest, HttpServletResponse httpResponse,@RequestBody JSONObject<InvoiceTemplateRequest> arinvoiceTemplateRequest){
		logger.info("Entered Into update Invoice Template");
		BaseResponse response = new InvoiceTemplateResponse();
		if(arinvoiceTemplateRequest.getData().getOrgId()!=null){
			try 
			{
				logger.info("Request Payload:"+generateRequestAndResponseLogPaylod(arinvoiceTemplateRequest)); 
				InvoiceTemplateVo invoiceTempVo = ArTemplateConvertToVoHelper.getInstance().convertArInvoiceTemplateVoFromRequest(arinvoiceTemplateRequest.getData());
				//invoiceService.updateInvoice(invoiceVo);
				templateService.UpdateInvoiceTemplate(invoiceTempVo);
				setTokens(response,httpRequest.getHeader("keyToken"),httpRequest.getHeader("valueToken")); 
				response = constructResponse(response, Constants.SUCCESS,Constants.AR_INVOICE_TEMPLATE_UPDATED_SUCCESSFULLY,Constants.SUCCESS_AR_INVOICE_TEMPLATE_UPDATED,Constants.AR_INVOICE_TEMPLATE_UPDATED_SUCCESSFULLY);
				logger.info("Response Payload:"+generateRequestAndResponseLogPaylod(response));
				return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
			}
			catch(Exception e)
			{ 
				if(e.getCause()!=null)
				{ 
					response = constructResponse(response, Constants.FAILURE,Constants.FAILURE_BILLS_INVOICE_UPDATED,e.getCause().getMessage(),Constants.BILLS_INVOICE_UPDATED_UNSUCCESSFULLY);
				}
				else
				{ 
					response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_BILLS_INVOICE_UPDATED,e.getMessage(),Constants.BILLS_INVOICE_UPDATED_UNSUCCESSFULLY);
				}
				logger.error("Error Payload:"+generateRequestAndResponseLogPaylod(response));
				return new ResponseEntity<BaseResponse>(response,HttpStatus.INTERNAL_SERVER_ERROR); } }else { response =
						constructResponse(response, Constants.FAILURE,Constants.FAILURE_AR_INVOICE_TEMPLATE_UPDATED,Constants.FAILURE_IN_USER_ORGANIZATION_REQUEST_VALIDATION,Constants.AR_INVOICE_TEMPLATE_UPDATED_UNSUCCESSFULLY);
				logger.error("Error Payload:"+ generateRequestAndResponseLogPaylod(response));
				return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR); } }
	
	@RequestMapping(value = "/v1/templates/invoice/{organizationId}/{organizationName}/{userId}/{id}/{roleName}/{status}", method = RequestMethod.DELETE)
	public ResponseEntity<BaseResponse> activateDeActivateInvoiceTemplate(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @PathVariable int organizationId, 
			@PathVariable String organizationName,
			@PathVariable String userId,
			@PathVariable String roleName,
			@PathVariable int id,
			@PathVariable String status)
	{
		logger.info("Entry into activateDeActivateInvoiceTemplate"); 
		BaseResponse response = new InvoiceTemplateResponse(); 
		try { 
			InvoiceTemplateVo invoiceTempVo = templateService.activateDeActivateInvoiceTemplate(organizationId,organizationName,userId,id,roleName,status);
			setTokens(response,httpRequest.getHeader("keyToken"),httpRequest.getHeader("valueToken")); 
			((InvoiceTemplateResponse)response).setData(invoiceTempVo);
			response = constructResponse(response,Constants.SUCCESS, Constants.SUCCESS_AR_INVOICE_ACTIVATED,Constants.SUCCESS_AR_INVOICE_ACTIVATED, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new  ResponseEntity<BaseResponse>(response, HttpStatus.OK); 
		} catch (Exception e)
		{ 
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_AR_INVOICE_DELETED, e.getMessage(),Constants.FAILURE_DURING_GET); logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		} 
	}

	@RequestMapping(value="/v1/templates/credit_note" , method = RequestMethod.POST)
	public ResponseEntity<BaseResponse>createCreditNoteTemplate(HttpServletRequest httpRequest, HttpServletResponse httpResponse,@RequestBody JSONObject<CreditNoteTemplateRequest> arCreditNoteTemplateRequest){
		logger.info("Entered Into createCreditNoteTemplate");
		BaseResponse response = new CreditNoteTemplateResponse();
		if(arCreditNoteTemplateRequest.getData().getOrgId()!=null) {
			try {
				logger.info("Request Payload:"+generateRequestAndResponseLogPaylod(arCreditNoteTemplateRequest));	
				CreditNoteTemplateVo CreditNoteTempVo = ArTemplateConvertToVoHelper.getInstance().convertArCreditNoteTemplateVoFromRequest(arCreditNoteTemplateRequest.getData());
				CreditNoteTempVo = templateService.createCreditNoteTemplate(CreditNoteTempVo);
				setTokens(response,httpRequest.getHeader("keyToken"),httpRequest.getHeader("valueToken"));
				((CreditNoteTemplateResponse)response).setData(CreditNoteTempVo);
				response = constructResponse(response, Constants.SUCCESS, Constants.AR_CREDIT_NOTE_TEMPLATE_CREATED_SUCCESSFULLY,Constants.SUCCESS_AR_CREDIT_NOTE_TEMPLATE_CREATED,Constants.AR_CREDIT_NOTE_TEMPLATE_CREATED_SUCCESSFULLY);
				logger.info("Response Payload:"+generateRequestAndResponseLogPaylod(response));
				return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
			}catch(Exception e) {
				if(e.getCause()!=null){
					response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_AR_CREDIT_NOTE_TEMPLATE_CREATED,e.getCause().getMessage(), Constants.AR_CREDIT_NOTE_TEMPLATE_CREATED_UNSUCCESSFULLY);
				}else{
					response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_AR_CREDIT_NOTE_TEMPLATE_CREATED,e.getMessage(), Constants.AR_CREDIT_NOTE_TEMPLATE_CREATED_UNSUCCESSFULLY);
				}
				logger.error("Error Payload:" , e);
				logger.error("Error Payload:"+generateRequestAndResponseLogPaylod(response));
				return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}	
		}else {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_AR_CREDIT_NOTE_TEMPLATE_CREATED,Constants.FAILURE_IN_USER_ORGANIZATION_REQUEST_VALIDATION, Constants.AR_CREDIT_NOTE_TEMPLATE_CREATED_UNSUCCESSFULLY);
			logger.error("Error Payload:"+ generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}



	@RequestMapping(value = "/v1/templates/credit_note/{organizationId}/{organizationName}/{userId}/{roleName}/{id}")
	public ResponseEntity<BaseResponse> getCreditNoteTemplate(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, 
			@PathVariable int organizationId, 
			@PathVariable String organizationName,
			@PathVariable String userId,
			@PathVariable String roleName,
			@PathVariable int id)
	{
		logger.info("Entry into getCreditNoteTemplate"); BaseResponse
		response = new CreditNoteTemplateResponse(); 
		try { 
			CreditNoteTemplateVo CreditNoteTempVo = templateService.getCreditNoteTemplate(organizationId,id);
			setTokens(response,httpRequest.getHeader("keyToken"),httpRequest.getHeader("valueToken")); 
			((CreditNoteTemplateResponse)response).setData(CreditNoteTempVo);
			response = constructResponse(response,Constants.SUCCESS, Constants.SUCCESS_AR_CREDIT_NOTE_TEMPLATE_FETCH,Constants.SUCCESS_AR_CREDIT_NOTE_TEMPLATE_FETCH, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new  ResponseEntity<BaseResponse>(response, HttpStatus.OK); 
		} catch (Exception e)
		{ 
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_AR_CREDIT_NOTE_TEMPLATE_FETCH, e.getMessage(),Constants.FAILURE_DURING_GET); logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		} 
	}
	
	@RequestMapping(value = "/v1/templates/credit_note/{organizationId}/{organizationName}/{userId}/{roleName}")
	public ResponseEntity<BaseResponse> getAllCreditNoteTemplate(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @PathVariable int organizationId,@PathVariable String organizationName,
			@PathVariable String userId,@PathVariable String roleName)
	{
		logger.info("Entry into getAllCreditNoteTemplate"); BaseResponse
		response = new CreditNoteTemplateInfoListResponse(); 
		try { 
			List<CreditNoteTemplateinformationVo> CreditNoteTempVo = templateService.getAllCreditNoteTemplate(organizationId,userId,roleName);
			setTokens(response,httpRequest.getHeader("keyToken"),httpRequest.getHeader("valueToken")); 
			((CreditNoteTemplateInfoListResponse)response).setCreditNoteTemplateInfo(CreditNoteTempVo);
			response = constructResponse(response,Constants.SUCCESS, Constants.SUCCESS_AR_CREDIT_NOTE_TEMPLATE_FETCH,Constants.SUCCESS_AR_CREDIT_NOTE_TEMPLATE_FETCH, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new  ResponseEntity<BaseResponse>(response, HttpStatus.OK); 
		} catch (Exception e)
		{ 
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_AR_CREDIT_NOTE_TEMPLATE_FETCH, e.getMessage(),Constants.FAILURE_DURING_GET); logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		} 
	}
	
	
	@RequestMapping(value = "/v1/templates/credit_note/{organizationId}")
	public ResponseEntity<BaseResponse> getAllCreditNoteTemplateOfAnOrganization(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @PathVariable int organizationId)
	{
		logger.info("Entry into getAllCreditNoteTemplate"); BaseResponse
		response = new CreditNoteTemplateListResponse(); 
		try { 
			List<CreditNoteTemplateVo> CreditNoteTempVo = templateService.getAllCreditNoteTemplatePerOrganization(organizationId);
			setTokens(response,httpRequest.getHeader("keyToken"),httpRequest.getHeader("valueToken")); 
			((CreditNoteTemplateListResponse)response).setCreditNoteTemplate(CreditNoteTempVo);
			response = constructResponse(response,Constants.SUCCESS, Constants.SUCCESS_AR_CREDIT_NOTE_TEMPLATE_FETCH,Constants.SUCCESS_AR_CREDIT_NOTE_TEMPLATE_FETCH, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new  ResponseEntity<BaseResponse>(response, HttpStatus.OK); 
		} catch (Exception e)
		{ 
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_AR_CREDIT_NOTE_TEMPLATE_FETCH, e.getMessage(),Constants.FAILURE_DURING_GET); logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		} 
	}
	
	
	@RequestMapping(value = "/v1/templates/credit_note/default/{organizationId}")
	public ResponseEntity<BaseResponse> getDefaultCreditNoteTemplateOfAnOrganization(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @PathVariable int organizationId)
	{
		logger.info("Entry into getDefaultCreditNoteTemplateOfAnOrganization"); 
		BaseResponse response = new CreditNoteTemplateResponse(); 
		try { 
			CreditNoteTemplateVo CreditNoteTempVo = templateService.getDefaultCreditNoteTemplateOfAnOrganization(organizationId);
			setTokens(response,httpRequest.getHeader("keyToken"),httpRequest.getHeader("valueToken")); 
			((CreditNoteTemplateResponse)response).setData(CreditNoteTempVo);
			response = constructResponse(response,Constants.SUCCESS, Constants.SUCCESS_AR_CREDIT_NOTE_TEMPLATE_FETCH,Constants.SUCCESS_AR_CREDIT_NOTE_TEMPLATE_FETCH, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new  ResponseEntity<BaseResponse>(response, HttpStatus.OK); 
		} catch (Exception e)
		{ 
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_AR_CREDIT_NOTE_TEMPLATE_FETCH, e.getMessage(),Constants.FAILURE_DURING_GET); logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		} 
	}


	@RequestMapping(value="/v1/templates/credit_note" , method = RequestMethod.PUT)
	public ResponseEntity<BaseResponse>updateCreditNoteTemplate(HttpServletRequest httpRequest, HttpServletResponse httpResponse,@RequestBody JSONObject<CreditNoteTemplateRequest> arCreditNoteTemplateRequest){
		logger.info("Entered Into update CreditNote Template");
		BaseResponse response = new CreditNoteTemplateResponse();
		if(arCreditNoteTemplateRequest.getData().getOrgId()!=null){
			try 
			{
				logger.info("Request Payload:"+generateRequestAndResponseLogPaylod(arCreditNoteTemplateRequest)); 
				CreditNoteTemplateVo CreditNoteTempVo = ArTemplateConvertToVoHelper.getInstance().convertArCreditNoteTemplateVoFromRequest(arCreditNoteTemplateRequest.getData());
				//CreditNoteService.updateCreditNote(CreditNoteVo);
				templateService.UpdateCreditNoteTemplate(CreditNoteTempVo);
				setTokens(response,httpRequest.getHeader("keyToken"),httpRequest.getHeader("valueToken")); 
				response = constructResponse(response, Constants.SUCCESS,Constants.AR_CREDIT_NOTE_TEMPLATE_UPDATED_SUCCESSFULLY,Constants.SUCCESS_AR_CREDIT_NOTE_TEMPLATE_UPDATED,Constants.AR_CREDIT_NOTE_TEMPLATE_UPDATED_SUCCESSFULLY);
				logger.info("Response Payload:"+generateRequestAndResponseLogPaylod(response));
				return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
			}
			catch(Exception e)
			{ 
				if(e.getCause()!=null)
				{ 
					response = constructResponse(response, Constants.FAILURE,Constants.FAILURE_CREDIT_NOTE_UPDATED,e.getCause().getMessage(),Constants.CREDIT_NOTE_UPDATED_UNSUCCESSFULLY);
				}
				else
				{ 
					response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_CREDIT_NOTE_UPDATED,e.getMessage(),Constants.CREDIT_NOTE_UPDATED_UNSUCCESSFULLY);
				}
				logger.error("Error Payload:"+generateRequestAndResponseLogPaylod(response));
				return new ResponseEntity<BaseResponse>(response,HttpStatus.INTERNAL_SERVER_ERROR); } }else { response =
						constructResponse(response, Constants.FAILURE,Constants.FAILURE_AR_CREDIT_NOTE_TEMPLATE_UPDATED,Constants.FAILURE_IN_USER_ORGANIZATION_REQUEST_VALIDATION,Constants.AR_CREDIT_NOTE_TEMPLATE_UPDATED_UNSUCCESSFULLY);
				logger.error("Error Payload:"+ generateRequestAndResponseLogPaylod(response));
				return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR); } }
	
	@RequestMapping(value = "/v1/templates/credit_note/{organizationId}/{organizationName}/{userId}/{id}/{roleName}/{status}", method = RequestMethod.DELETE)
	public ResponseEntity<BaseResponse> activateDeActivateCreditNoteTemplate(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @PathVariable int organizationId, 
			@PathVariable String organizationName,
			@PathVariable String userId,
			@PathVariable String roleName,
			@PathVariable int id,
			@PathVariable String status)
	{
		logger.info("Entry into activateDeActivateCreditNoteTemplate"); 
		BaseResponse response = new CreditNoteTemplateResponse(); 
		try { 
			CreditNoteTemplateVo CreditNoteTempVo = templateService.activateDeActivateCreditNoteTemplate(organizationId,organizationName,userId,id,roleName,status);
			setTokens(response,httpRequest.getHeader("keyToken"),httpRequest.getHeader("valueToken")); 
			((CreditNoteTemplateResponse)response).setData(CreditNoteTempVo);
			response = constructResponse(response,Constants.SUCCESS, Constants.SUCCESS_AR_CREDIT_NOTE_ACTIVATED,Constants.SUCCESS_AR_CREDIT_NOTE_ACTIVATED, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new  ResponseEntity<BaseResponse>(response, HttpStatus.OK); 
		} catch (Exception e)
		{ 
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_AR_CREDIT_NOTE_DELETED, e.getMessage(),Constants.FAILURE_DURING_GET); logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		} 
	}

}

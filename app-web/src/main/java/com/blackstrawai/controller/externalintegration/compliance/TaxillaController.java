package com.blackstrawai.controller.externalintegration.compliance;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
//import org.json.simple.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.common.BaseController;
import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.common.Constants;
import com.blackstrawai.externalintegration.compliance.TaxillaService;
import com.blackstrawai.externalintegration.compliance.taxilla.B2BInvoiceVo;
import com.blackstrawai.externalintegration.compliance.taxilla.B2clInvoiceVo;
import com.blackstrawai.externalintegration.compliance.taxilla.B2csInvoiceVo;
import com.blackstrawai.externalintegration.compliance.taxilla.CdnInvoiceVo;
import com.blackstrawai.externalintegration.compliance.taxilla.NilRatedSuppliesVo;
import com.blackstrawai.externalintegration.compliance.taxilla.TaxillaVo;
import com.blackstrawai.helper.ExternalIntegrationConvertToVoHelper;
import com.blackstrawai.request.JSONObject;
import com.blackstrawai.request.externalintegration.compliance.TaxillaB2bInvoiceRequest;
import com.blackstrawai.request.externalintegration.compliance.TaxillaOtpRequest;
import com.blackstrawai.request.externalintegration.compliance.TaxillaSearchTaxPayerRequest;
import com.blackstrawai.response.externalintegration.compliance.TaxillaB2bInvoiceResponse;
import com.blackstrawai.response.externalintegration.compliance.TaxillaB2clInvoiceResponse;
import com.blackstrawai.response.externalintegration.compliance.TaxillaB2csInvoiceResponse;
import com.blackstrawai.response.externalintegration.compliance.TaxillaCdnInvoiceResponse;
import com.blackstrawai.response.externalintegration.compliance.TaxillaGenericResponse;
import com.blackstrawai.response.externalintegration.compliance.TaxillaListResponse;
import com.blackstrawai.response.externalintegration.compliance.TaxillaNilRatedSuppliesResponse;
import com.blackstrawai.response.externalintegration.compliance.TaxillaOtpResponse;
import com.blackstrawai.response.externalintegration.compliance.TaxillaResponse;
import com.blackstrawai.response.externalintegration.compliance.TaxillaStatusResponse;
import com.blackstrawai.taxilla.Taxilla;
import com.blackstrawai.taxilla.TaxillaConstants;

@RestController
@CrossOrigin
@RequestMapping("/decifer/taxilla")
public class TaxillaController extends BaseController{
	@Autowired
	private TaxillaService taxillaService;

	private Logger logger = Logger.getLogger(TaxillaController.class);
	
	@RequestMapping(value = "/v1/otp/{organizationId}/{userId}/{roleName}", method = RequestMethod.GET)
	public ResponseEntity<BaseResponse> getGstOtp(
			HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse, @PathVariable int organizationId, @PathVariable int userId, @PathVariable String roleName) {
		BaseResponse response = new TaxillaOtpResponse();
		try {  
			String otp = taxillaService.getOtp(organizationId, userId, roleName);
			((TaxillaOtpResponse) response).setData(otp);
			setTokens(response, httpServletRequest.getHeader("keyToken"), httpServletRequest.getHeader("valueToken"));
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_OTP,
					Constants.SUCCESS_OTP, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error Payload:" + e.getMessage());
			if (e.getCause() != null) {
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_OTP,
						e.getCause().getMessage(), Constants.FAILURE_DURING_GET);
			} else {
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_JOURNAL_ENTRIES_REPORT,
						e.getMessage(), Constants.FAILURE_DURING_GET);
			}

			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/v1/otp", method = RequestMethod.POST)
	public ResponseEntity<BaseResponse> setGstOtp(
			HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse, @RequestBody JSONObject<TaxillaOtpRequest> request) {
		BaseResponse response = new TaxillaOtpResponse();
		try {  
			taxillaService.setOtp(request.getData().getOrganizationId(), request.getData().getUserId(), request.getData().getRoleName(), request.getData().getOtp());
			((TaxillaOtpResponse) response).setData(null);
			setTokens(response, httpServletRequest.getHeader("keyToken"), httpServletRequest.getHeader("valueToken"));
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_SET_OTP,
					Constants.SUCCESS_SET_OTP, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error Payload:" + e.getMessage());
			if (e.getCause() != null) {
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_SET_OTP,
						e.getCause().getMessage(), Constants.FAILURE_DURING_GET);
			} else {
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_JOURNAL_ENTRIES_REPORT,
						e.getMessage(), Constants.FAILURE_DURING_GET);
			}

			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	@RequestMapping(value = "/v1/search_tax_payer/{gstNo}", method = RequestMethod.GET)
	public ResponseEntity<BaseResponse> getGSTDetailsByGST(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @PathVariable String gstNo) {
		logger.info("Entry into method: getGSTDetailsByGST");
		BaseResponse response = new TaxillaResponse();
		try {
			org.json.simple.JSONObject taxillaJson = Taxilla.getInstance().getByGstNo(gstNo);
			TaxillaVo taxilla = ExternalIntegrationConvertToVoHelper.getInstance().convertTaxillaFromJson(taxillaJson);		
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((TaxillaResponse) response).setData(taxilla);
			response = constructResponse(response, Constants.SUCCESS, Constants.TAXILLA_FETCH_SUCCESS,
					Constants.TAXILLA_FETCH_SUCCESS, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_TAXILLA_FETCH,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	
	@PostMapping(value = "/v1/search_tax_payer/address")
	public ResponseEntity<BaseResponse> getGSTDetails(HttpServletRequest httpRequest, HttpServletResponse httpResponse,
			@RequestBody JSONObject<TaxillaSearchTaxPayerRequest> taxillaSearchTaxPayerRequest) {
		logger.info("Entry into method: getGSTDetails");
		BaseResponse response = new TaxillaListResponse();

		List<String> gstList = taxillaSearchTaxPayerRequest.getData().getGst();

		List<TaxillaVo> taxillaVos = gstList.parallelStream().map(gst -> {
			return taxillaService.getAddressDetailsByGstNumberFromTaxilla(gst);
		}).filter(Objects::nonNull)
				.map(gts -> ExternalIntegrationConvertToVoHelper.getInstance().convertTaxillaFromJson(gts))
				.collect(Collectors.toList());

		setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
		((TaxillaListResponse) response).setData(taxillaVos);
		response = constructResponse(response, Constants.SUCCESS, Constants.TAXILLA_FETCH_SUCCESS,
				Constants.TAXILLA_FETCH_SUCCESS, Constants.SUCCESS_DURING_GET);
		logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
		return new ResponseEntity<>(response, HttpStatus.OK);

	}

	

	// fetch status of list of gst numbers
	@RequestMapping(value = "/v1/search_tax_payer", method = RequestMethod.POST)
	public ResponseEntity<BaseResponse> getGSTStatus(HttpServletRequest httpRequest, HttpServletResponse httpResponse,
			@RequestBody JSONObject<TaxillaSearchTaxPayerRequest> taxillaSearchTaxPayerRequest) {
		logger.info("Entry into method: getGSTStatus");
		BaseResponse response = new TaxillaStatusResponse();
		try {
			logger.info("Request Payload:" + generateRequestAndResponseLogPaylod(taxillaSearchTaxPayerRequest));
			List<String> gstList = taxillaSearchTaxPayerRequest.getData().getGst();
			Map<String, String> statusMap = Taxilla.getInstance().getGSTStatus(gstList, Taxilla.getTaxillaMap().get(TaxillaConstants.GSTIN_URL_KEY));
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((TaxillaStatusResponse) response).setData(statusMap);
			response = constructResponse(response, Constants.SUCCESS, Constants.TAXILLA_STATUS_FETCH_SUCCESS,
					Constants.TAXILLA_STATUS_FETCH_SUCCESS, Constants.RECEIPT_CREATED_SUCCESSFULLY);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);

		} catch (ApplicationException e) {
			if (e.getCause() != null) {
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_PAYMENT_CREATED,
						e.getCause().getMessage(), Constants.RECEIPT_CREATED_UNSUCCESSFULLY);
			} else {
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_PAYMENT_CREATED,
						e.getMessage(), Constants.RECEIPT_CREATED_UNSUCCESSFULLY);
			}
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/v1/gstr1/b2b_invoice", method = RequestMethod.POST)
	public ResponseEntity<BaseResponse> getGstr1B2bInvoices(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @RequestBody JSONObject<TaxillaB2bInvoiceRequest> request) throws ParseException {
		logger.info("Entry into method: getGstr1B2bInvoices");
		BaseResponse response = new TaxillaB2bInvoiceResponse();
		try {
			String otp = taxillaService.getOtp(request.getData().getOrganizationId(), Integer.parseInt(request.getData().getUserId()), request.getData().getRoleName());
			org.json.simple.JSONObject taxillaJson = Taxilla.getInstance().getGspResponse(request.getData().getGstin(), request.getData().getUsername(), request.getData().getRequestId(), 
					request.getData().getRetPeriod(), Taxilla.getTaxillaMap().get(TaxillaConstants.GSTR1_KEY), TaxillaConstants.B2B_ACTION, otp);
			List<B2BInvoiceVo> taxilla = ExternalIntegrationConvertToVoHelper.getInstance().convertB2bInvoicesFromJson(taxillaJson, "b2b");		
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((TaxillaB2bInvoiceResponse) response).setData(taxilla);
			response = constructResponse(response, Constants.SUCCESS, Constants.TAXILLA_B2B_INVOICES_FETCH_SUCCESS,
					Constants.TAXILLA_B2B_INVOICES_FETCH_SUCCESS, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.TAXILLA_B2B_INVOICES_FETCH_FAILURE,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/v1/gstr1/b2ba_invoice", method = RequestMethod.POST)
	public ResponseEntity<BaseResponse> getGstr1B2baInvoices(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @RequestBody JSONObject<TaxillaB2bInvoiceRequest> request) throws ParseException {
		logger.info("Entry into method: getGstr1B2baInvoices");
		BaseResponse response = new TaxillaB2bInvoiceResponse();
		try {
			String otp = taxillaService.getOtp(request.getData().getOrganizationId(), Integer.parseInt(request.getData().getUserId()), request.getData().getRoleName());
			org.json.simple.JSONObject taxillaJson = Taxilla.getInstance().getGspResponse(request.getData().getGstin(), request.getData().getUsername(), request.getData().getRequestId(), 
					request.getData().getRetPeriod(), Taxilla.getTaxillaMap().get(TaxillaConstants.GSTR1_KEY), TaxillaConstants.B2BA_ACTION, otp);
			List<B2BInvoiceVo> taxilla = ExternalIntegrationConvertToVoHelper.getInstance().convertB2bInvoicesFromJson(taxillaJson, "b2ba");		
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((TaxillaB2bInvoiceResponse) response).setData(taxilla);
			response = constructResponse(response, Constants.SUCCESS, Constants.TAXILLA_B2B_INVOICES_FETCH_SUCCESS,
					Constants.TAXILLA_B2B_INVOICES_FETCH_SUCCESS, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.TAXILLA_B2B_INVOICES_FETCH_FAILURE,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/v1/gstr1/b2cl_invoice", method = RequestMethod.POST)
	public ResponseEntity<BaseResponse> getGstr1B2clInvoices(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @RequestBody JSONObject<TaxillaB2bInvoiceRequest> request) throws ParseException {
		logger.info("Entry into method: getGstr1B2clInvoices");
		BaseResponse response = new TaxillaB2clInvoiceResponse();
		try {
			String otp = taxillaService.getOtp(request.getData().getOrganizationId(), Integer.parseInt(request.getData().getUserId()), request.getData().getRoleName());
			org.json.simple.JSONObject taxillaJson = Taxilla.getInstance().getGspResponse(request.getData().getGstin(), request.getData().getUsername(), request.getData().getRequestId(), 
					request.getData().getRetPeriod(), Taxilla.getTaxillaMap().get(TaxillaConstants.GSTR1_KEY), TaxillaConstants.B2CL_ACTION, otp);
			List<B2clInvoiceVo> taxilla = ExternalIntegrationConvertToVoHelper.getInstance().convertB2clInvoicesFromJson(taxillaJson, "b2cl");		
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((TaxillaB2clInvoiceResponse) response).setData(taxilla);
			response = constructResponse(response, Constants.SUCCESS, Constants.TAXILLA_B2B_INVOICES_FETCH_SUCCESS,
					Constants.TAXILLA_B2B_INVOICES_FETCH_SUCCESS, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.TAXILLA_B2B_INVOICES_FETCH_FAILURE,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/v1/gstr1/b2cla_invoice", method = RequestMethod.POST)
	public ResponseEntity<BaseResponse> getGstr1B2claInvoices(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @RequestBody JSONObject<TaxillaB2bInvoiceRequest> request) throws ParseException {
		logger.info("Entry into method: getGstr1B2claInvoices");
		BaseResponse response = new TaxillaB2clInvoiceResponse();
		try {
			String otp = taxillaService.getOtp(request.getData().getOrganizationId(), Integer.parseInt(request.getData().getUserId()), request.getData().getRoleName());
			org.json.simple.JSONObject taxillaJson = Taxilla.getInstance().getGspResponse(request.getData().getGstin(), request.getData().getUsername(), request.getData().getRequestId(), 
					request.getData().getRetPeriod(), Taxilla.getTaxillaMap().get(TaxillaConstants.GSTR1_KEY), TaxillaConstants.B2CLA_ACTION, otp); 
			List<B2clInvoiceVo> taxilla = ExternalIntegrationConvertToVoHelper.getInstance().convertB2clInvoicesFromJson(taxillaJson, "b2cla");		
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((TaxillaB2clInvoiceResponse) response).setData(taxilla);
			response = constructResponse(response, Constants.SUCCESS, Constants.TAXILLA_B2B_INVOICES_FETCH_SUCCESS,
					Constants.TAXILLA_B2B_INVOICES_FETCH_SUCCESS, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.TAXILLA_B2B_INVOICES_FETCH_FAILURE,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/v1/gstr1/b2cs_invoice", method = RequestMethod.POST)
	public ResponseEntity<BaseResponse> getGstr1B2csInvoices(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @RequestBody JSONObject<TaxillaB2bInvoiceRequest> request) throws ParseException {
		logger.info("Entry into method: getGstr1B2csInvoices");
		BaseResponse response = new TaxillaB2csInvoiceResponse();
		try {
			String otp = taxillaService.getOtp(request.getData().getOrganizationId(), Integer.parseInt(request.getData().getUserId()), request.getData().getRoleName());
			org.json.simple.JSONObject taxillaJson = Taxilla.getInstance().getGspResponse(request.getData().getGstin(), request.getData().getUsername(), request.getData().getRequestId(), 
					request.getData().getRetPeriod(), Taxilla.getTaxillaMap().get(TaxillaConstants.GSTR1_KEY), TaxillaConstants.B2CS_ACTION, otp);
			List<B2csInvoiceVo> taxilla = ExternalIntegrationConvertToVoHelper.getInstance().convertB2csInvoicesFromJson(taxillaJson, "b2cs");		
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((TaxillaB2csInvoiceResponse) response).setData(taxilla);
			response = constructResponse(response, Constants.SUCCESS, Constants.TAXILLA_B2B_INVOICES_FETCH_SUCCESS,
					Constants.TAXILLA_B2B_INVOICES_FETCH_SUCCESS, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.TAXILLA_B2B_INVOICES_FETCH_FAILURE,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/v1/gstr1/b2csa_invoice", method = RequestMethod.POST)
	public ResponseEntity<BaseResponse> getGstr1B2csaInvoices(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @RequestBody JSONObject<TaxillaB2bInvoiceRequest> request) throws ParseException {
		logger.info("Entry into method: getGstr1B2csInvoices");
		BaseResponse response = new TaxillaGenericResponse();
		try {
			String otp = taxillaService.getOtp(request.getData().getOrganizationId(), Integer.parseInt(request.getData().getUserId()), request.getData().getRoleName());
			org.json.simple.JSONObject taxillaJson = Taxilla.getInstance().getGspResponse(request.getData().getGstin(), request.getData().getUsername(), request.getData().getRequestId(), 
					request.getData().getRetPeriod(), Taxilla.getTaxillaMap().get(TaxillaConstants.GSTR1_KEY), TaxillaConstants.B2CSA_ACTION, otp);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((TaxillaGenericResponse) response).setData(taxillaJson);
			response = constructResponse(response, Constants.SUCCESS, Constants.TAXILLA_B2B_INVOICES_FETCH_SUCCESS,
					Constants.TAXILLA_B2B_INVOICES_FETCH_SUCCESS, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.TAXILLA_B2B_INVOICES_FETCH_FAILURE,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/v1/gstr1/cdnr_invoice", method = RequestMethod.POST)
	public ResponseEntity<BaseResponse> getGstr1CdnrInvoices(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @RequestBody JSONObject<TaxillaB2bInvoiceRequest> request) throws ParseException {
		logger.info("Entry into method: getGstr1CdnrInvoices");
		BaseResponse response = new TaxillaCdnInvoiceResponse();
		try {
			String otp = taxillaService.getOtp(request.getData().getOrganizationId(), Integer.parseInt(request.getData().getUserId()), request.getData().getRoleName());
			org.json.simple.JSONObject taxillaJson = Taxilla.getInstance().getGspResponse(request.getData().getGstin(), request.getData().getUsername(), request.getData().getRequestId(), 
					request.getData().getRetPeriod(), Taxilla.getTaxillaMap().get(TaxillaConstants.GSTR1_KEY), TaxillaConstants.CDNR_ACTION, otp);
			List<CdnInvoiceVo> taxilla = ExternalIntegrationConvertToVoHelper.getInstance().convertCdnInvoicesFromJson(taxillaJson, "cdnr");		
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((TaxillaCdnInvoiceResponse) response).setData(taxilla);
			response = constructResponse(response, Constants.SUCCESS, Constants.TAXILLA_B2B_INVOICES_FETCH_SUCCESS,
					Constants.TAXILLA_B2B_INVOICES_FETCH_SUCCESS, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.TAXILLA_B2B_INVOICES_FETCH_FAILURE,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/v1/gstr1/cdnra_invoice", method = RequestMethod.POST)
	public ResponseEntity<BaseResponse> getGstr1CdnraInvoices(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @RequestBody JSONObject<TaxillaB2bInvoiceRequest> request) throws ParseException {
		logger.info("Entry into method: getGstr1CdnraInvoices");
		BaseResponse response = new TaxillaCdnInvoiceResponse();
		try {
			String otp = taxillaService.getOtp(request.getData().getOrganizationId(), Integer.parseInt(request.getData().getUserId()), request.getData().getRoleName());
			org.json.simple.JSONObject taxillaJson = Taxilla.getInstance().getGspResponse(request.getData().getGstin(), request.getData().getUsername(), request.getData().getRequestId(), 
					request.getData().getRetPeriod(), Taxilla.getTaxillaMap().get(TaxillaConstants.GSTR1_KEY), TaxillaConstants.CDNRA_ACTION, otp);
			List<CdnInvoiceVo> taxilla = ExternalIntegrationConvertToVoHelper.getInstance().convertCdnInvoicesFromJson(taxillaJson, "cdnra");		
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((TaxillaCdnInvoiceResponse) response).setData(taxilla);
			response = constructResponse(response, Constants.SUCCESS, Constants.TAXILLA_B2B_INVOICES_FETCH_SUCCESS,
					Constants.TAXILLA_B2B_INVOICES_FETCH_SUCCESS, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.TAXILLA_B2B_INVOICES_FETCH_FAILURE,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/v1/gstr1/nil_rated_supplies", method = RequestMethod.POST)
	public ResponseEntity<BaseResponse> getGstr1NilRatedSupplies(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @RequestBody JSONObject<TaxillaB2bInvoiceRequest> request) throws ParseException {
		logger.info("Entry into method: getGstr1CNilRatedSupplies");
		BaseResponse response = new TaxillaNilRatedSuppliesResponse();
		try {
			String otp = taxillaService.getOtp(request.getData().getOrganizationId(), Integer.parseInt(request.getData().getUserId()), request.getData().getRoleName());
			org.json.simple.JSONObject taxillaJson = Taxilla.getInstance().getGspResponse(request.getData().getGstin(), request.getData().getUsername(), request.getData().getRequestId(), 
					request.getData().getRetPeriod(), Taxilla.getTaxillaMap().get(TaxillaConstants.GSTR1_KEY), TaxillaConstants.NIL_ACTION, otp);
			NilRatedSuppliesVo taxilla = ExternalIntegrationConvertToVoHelper.getInstance().convertNilRatedSuppliesFromJson(taxillaJson, "nil");		
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((TaxillaNilRatedSuppliesResponse) response).setData(taxilla);
			response = constructResponse(response, Constants.SUCCESS, Constants.TAXILLA_B2B_INVOICES_FETCH_SUCCESS,
					Constants.TAXILLA_B2B_INVOICES_FETCH_SUCCESS, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.TAXILLA_B2B_INVOICES_FETCH_FAILURE,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/v1/gstr1/exp", method = RequestMethod.POST)
	public ResponseEntity<BaseResponse> getGstr1Exp(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @RequestBody JSONObject<TaxillaB2bInvoiceRequest> request) throws ParseException {
		logger.info("Entry into method: getGstr1Exp");
		BaseResponse response = new TaxillaGenericResponse();
		try {
			String otp = taxillaService.getOtp(request.getData().getOrganizationId(), Integer.parseInt(request.getData().getUserId()), request.getData().getRoleName());
			org.json.simple.JSONObject taxillaJson = Taxilla.getInstance().getGspResponse(request.getData().getGstin(), request.getData().getUsername(), request.getData().getRequestId(), 
					request.getData().getRetPeriod(), Taxilla.getTaxillaMap().get(TaxillaConstants.GSTR1_KEY), TaxillaConstants.EXP_ACTION, otp);
//			List<ExpVo> taxilla = ExternalIntegrationConvertToVoHelper.getInstance().convertExpFromJson(taxillaJson, "nil");		
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((TaxillaGenericResponse) response).setData(taxillaJson);
			response = constructResponse(response, Constants.SUCCESS, Constants.TAXILLA_B2B_INVOICES_FETCH_SUCCESS,
					Constants.TAXILLA_B2B_INVOICES_FETCH_SUCCESS, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.TAXILLA_B2B_INVOICES_FETCH_FAILURE,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/v1/gstr1/expa", method = RequestMethod.POST)
	public ResponseEntity<BaseResponse> getGstr1Expa(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @RequestBody JSONObject<TaxillaB2bInvoiceRequest> request) throws ParseException {
		logger.info("Entry into method: getGstr1Expa");
		BaseResponse response = new TaxillaGenericResponse();
		try {
			String otp = taxillaService.getOtp(request.getData().getOrganizationId(), Integer.parseInt(request.getData().getUserId()), request.getData().getRoleName());
			org.json.simple.JSONObject taxillaJson = Taxilla.getInstance().getGspResponse(request.getData().getGstin(), request.getData().getUsername(), request.getData().getRequestId(), 
					request.getData().getRetPeriod(), Taxilla.getTaxillaMap().get(TaxillaConstants.GSTR1_KEY), TaxillaConstants.EXPA_ACTION, otp);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((TaxillaGenericResponse) response).setData(taxillaJson);
			response = constructResponse(response, Constants.SUCCESS, Constants.TAXILLA_B2B_INVOICES_FETCH_SUCCESS,
					Constants.TAXILLA_B2B_INVOICES_FETCH_SUCCESS, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.TAXILLA_B2B_INVOICES_FETCH_FAILURE,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/v1/gstr1/at", method = RequestMethod.POST)
	public ResponseEntity<BaseResponse> getGstr1At(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @RequestBody JSONObject<TaxillaB2bInvoiceRequest> request) throws ParseException {
		logger.info("Entry into method: getGstr1At");
		BaseResponse response = new TaxillaGenericResponse();
		try {
			String otp = taxillaService.getOtp(request.getData().getOrganizationId(), Integer.parseInt(request.getData().getUserId()), request.getData().getRoleName());
			org.json.simple.JSONObject taxillaJson = Taxilla.getInstance().getGspResponse(request.getData().getGstin(), request.getData().getUsername(), request.getData().getRequestId(), 
					request.getData().getRetPeriod(), Taxilla.getTaxillaMap().get(TaxillaConstants.GSTR1_KEY), TaxillaConstants.AT_ACTION, otp);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((TaxillaGenericResponse) response).setData(taxillaJson);
			response = constructResponse(response, Constants.SUCCESS, Constants.TAXILLA_B2B_INVOICES_FETCH_SUCCESS,
					Constants.TAXILLA_B2B_INVOICES_FETCH_SUCCESS, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.TAXILLA_B2B_INVOICES_FETCH_FAILURE,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/v1/gstr1/ata", method = RequestMethod.POST)
	public ResponseEntity<BaseResponse> getGstr1Ata(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @RequestBody JSONObject<TaxillaB2bInvoiceRequest> request) throws ParseException {
		logger.info("Entry into method: getGstr1At");
		BaseResponse response = new TaxillaGenericResponse();
		try {
			String otp = taxillaService.getOtp(request.getData().getOrganizationId(), Integer.parseInt(request.getData().getUserId()), request.getData().getRoleName());
			org.json.simple.JSONObject taxillaJson = Taxilla.getInstance().getGspResponse(request.getData().getGstin(), request.getData().getUsername(), request.getData().getRequestId(), 
					request.getData().getRetPeriod(), Taxilla.getTaxillaMap().get(TaxillaConstants.GSTR1_KEY), TaxillaConstants.ATA_ACTION, otp);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((TaxillaGenericResponse) response).setData(taxillaJson);
			response = constructResponse(response, Constants.SUCCESS, Constants.TAXILLA_B2B_INVOICES_FETCH_SUCCESS,
					Constants.TAXILLA_B2B_INVOICES_FETCH_SUCCESS, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.TAXILLA_B2B_INVOICES_FETCH_FAILURE,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/v1/gstr1/txp", method = RequestMethod.POST)
	public ResponseEntity<BaseResponse> getGstr1Txp(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @RequestBody JSONObject<TaxillaB2bInvoiceRequest> request) throws ParseException {
		logger.info("Entry into method: getGstr1Txp");
		BaseResponse response = new TaxillaGenericResponse();
		try {
			String otp = taxillaService.getOtp(request.getData().getOrganizationId(), Integer.parseInt(request.getData().getUserId()), request.getData().getRoleName());
			org.json.simple.JSONObject taxillaJson = Taxilla.getInstance().getGspResponse(request.getData().getGstin(), request.getData().getUsername(), request.getData().getRequestId(), 
					request.getData().getRetPeriod(), Taxilla.getTaxillaMap().get(TaxillaConstants.GSTR1_KEY), TaxillaConstants.TXP_ACTION, otp);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((TaxillaGenericResponse) response).setData(taxillaJson);
			response = constructResponse(response, Constants.SUCCESS, Constants.TAXILLA_B2B_INVOICES_FETCH_SUCCESS,
					Constants.TAXILLA_B2B_INVOICES_FETCH_SUCCESS, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.TAXILLA_B2B_INVOICES_FETCH_FAILURE,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/v1/gstr1/hsn_summary", method = RequestMethod.POST)
	public ResponseEntity<BaseResponse> getGstr1HsnSummary(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @RequestBody JSONObject<TaxillaB2bInvoiceRequest> request) throws ParseException {
		logger.info("Entry into method: getGstr1HsnSummary");
		BaseResponse response = new TaxillaGenericResponse();
		try {
			String otp = taxillaService.getOtp(request.getData().getOrganizationId(), Integer.parseInt(request.getData().getUserId()), request.getData().getRoleName());
			org.json.simple.JSONObject taxillaJson = Taxilla.getInstance().getGspResponse(request.getData().getGstin(), request.getData().getUsername(), request.getData().getRequestId(), 
					request.getData().getRetPeriod(), Taxilla.getTaxillaMap().get(TaxillaConstants.GSTR1_KEY), TaxillaConstants.HSNSUM_ACTION, otp);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((TaxillaGenericResponse) response).setData(taxillaJson);
			response = constructResponse(response, Constants.SUCCESS, Constants.TAXILLA_B2B_INVOICES_FETCH_SUCCESS,
					Constants.TAXILLA_B2B_INVOICES_FETCH_SUCCESS, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.TAXILLA_B2B_INVOICES_FETCH_FAILURE,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/v1/gstr1/gstr1_summary", method = RequestMethod.POST)
	public ResponseEntity<BaseResponse> getGstr1Summary(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @RequestBody JSONObject<TaxillaB2bInvoiceRequest> request) throws ParseException {
		logger.info("Entry into method: getGstr1Summary");
		BaseResponse response = new TaxillaGenericResponse();
		try {
			String otp = taxillaService.getOtp(request.getData().getOrganizationId(), Integer.parseInt(request.getData().getUserId()), request.getData().getRoleName());
			org.json.simple.JSONObject taxillaJson = Taxilla.getInstance().getGspResponse(request.getData().getGstin(), request.getData().getUsername(), request.getData().getRequestId(), 
					request.getData().getRetPeriod(), Taxilla.getTaxillaMap().get(TaxillaConstants.GSTR1_KEY), TaxillaConstants.RETSUM_ACTION, otp);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((TaxillaGenericResponse) response).setData(taxillaJson);
			response = constructResponse(response, Constants.SUCCESS, Constants.TAXILLA_B2B_INVOICES_FETCH_SUCCESS,
					Constants.TAXILLA_B2B_INVOICES_FETCH_SUCCESS, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.TAXILLA_B2B_INVOICES_FETCH_FAILURE,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/v1/gstr1/cdnur_invoices", method = RequestMethod.POST)
	public ResponseEntity<BaseResponse> getGstr1CdnurInvoices(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @RequestBody JSONObject<TaxillaB2bInvoiceRequest> request) throws ParseException {
		logger.info("Entry into method: getCdnurInvoices");
		BaseResponse response = new TaxillaGenericResponse();
		try {
			String otp = taxillaService.getOtp(request.getData().getOrganizationId(), Integer.parseInt(request.getData().getUserId()), request.getData().getRoleName());
			org.json.simple.JSONObject taxillaJson = Taxilla.getInstance().getGspResponse(request.getData().getGstin(), request.getData().getUsername(), request.getData().getRequestId(), 
					request.getData().getRetPeriod(), Taxilla.getTaxillaMap().get(TaxillaConstants.GSTR1_KEY), TaxillaConstants.CDNUR_ACTION, otp);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((TaxillaGenericResponse) response).setData(taxillaJson);
			response = constructResponse(response, Constants.SUCCESS, Constants.TAXILLA_B2B_INVOICES_FETCH_SUCCESS,
					Constants.TAXILLA_B2B_INVOICES_FETCH_SUCCESS, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.TAXILLA_B2B_INVOICES_FETCH_FAILURE,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/v1/gstr1/cdnura_invoices", method = RequestMethod.POST)
	public ResponseEntity<BaseResponse> getGstr1CdnuraInvoices(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @RequestBody JSONObject<TaxillaB2bInvoiceRequest> request) throws ParseException {
		logger.info("Entry into method: getCdnuraInvoices");
		BaseResponse response = new TaxillaGenericResponse();
		try {
			String otp = taxillaService.getOtp(request.getData().getOrganizationId(), Integer.parseInt(request.getData().getUserId()), request.getData().getRoleName());
			org.json.simple.JSONObject taxillaJson = Taxilla.getInstance().getGspResponse(request.getData().getGstin(), request.getData().getUsername(), request.getData().getRequestId(), 
					request.getData().getRetPeriod(), Taxilla.getTaxillaMap().get(TaxillaConstants.GSTR1_KEY), TaxillaConstants.CDNURA_ACTION, otp);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((TaxillaGenericResponse) response).setData(taxillaJson);
			response = constructResponse(response, Constants.SUCCESS, Constants.TAXILLA_B2B_INVOICES_FETCH_SUCCESS,
					Constants.TAXILLA_B2B_INVOICES_FETCH_SUCCESS, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.TAXILLA_B2B_INVOICES_FETCH_FAILURE,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/v1/gstr1/doc_issued", method = RequestMethod.POST)
	public ResponseEntity<BaseResponse> getGstr1DocIssued(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @RequestBody JSONObject<TaxillaB2bInvoiceRequest> request) throws ParseException {
		logger.info("Entry into method: getDocIssued");
		BaseResponse response = new TaxillaGenericResponse();
		try {
			String otp = taxillaService.getOtp(request.getData().getOrganizationId(), Integer.parseInt(request.getData().getUserId()), request.getData().getRoleName());
			org.json.simple.JSONObject taxillaJson = Taxilla.getInstance().getGspResponse(request.getData().getGstin(), request.getData().getUsername(), request.getData().getRequestId(), 
					request.getData().getRetPeriod(), Taxilla.getTaxillaMap().get(TaxillaConstants.GSTR1_KEY), TaxillaConstants.DOCISS_ACTION, otp);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((TaxillaGenericResponse) response).setData(taxillaJson);
			response = constructResponse(response, Constants.SUCCESS, Constants.TAXILLA_B2B_INVOICES_FETCH_SUCCESS,
					Constants.TAXILLA_B2B_INVOICES_FETCH_SUCCESS, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.TAXILLA_B2B_INVOICES_FETCH_FAILURE,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/v1/gstr1/txpa", method = RequestMethod.POST)
	public ResponseEntity<BaseResponse> getGstr1Txpa(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @RequestBody JSONObject<TaxillaB2bInvoiceRequest> request) throws ParseException {
		logger.info("Entry into method: getGstr1Txpa");
		BaseResponse response = new TaxillaGenericResponse();
		try {
			String otp = taxillaService.getOtp(request.getData().getOrganizationId(), Integer.parseInt(request.getData().getUserId()), request.getData().getRoleName());
			org.json.simple.JSONObject taxillaJson = Taxilla.getInstance().getGspResponse(request.getData().getGstin(), request.getData().getUsername(), request.getData().getRequestId(), 
					request.getData().getRetPeriod(), Taxilla.getTaxillaMap().get(TaxillaConstants.GSTR1_KEY), TaxillaConstants.TXPA_ACTION, otp);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((TaxillaGenericResponse) response).setData(taxillaJson);
			response = constructResponse(response, Constants.SUCCESS, Constants.TAXILLA_B2B_INVOICES_FETCH_SUCCESS,
					Constants.TAXILLA_B2B_INVOICES_FETCH_SUCCESS, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.TAXILLA_B2B_INVOICES_FETCH_FAILURE,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/v1/gstr2a/b2b_invoice", method = RequestMethod.POST)
	public ResponseEntity<BaseResponse> getGstr2B2bInvoices(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @RequestBody JSONObject<TaxillaB2bInvoiceRequest> request) throws ParseException {
		logger.info("Entry into method: getGstr1B2baInvoices");
		BaseResponse response = new TaxillaB2bInvoiceResponse();
		try {
			String otp = taxillaService.getOtp(request.getData().getOrganizationId(), Integer.parseInt(request.getData().getUserId()), request.getData().getRoleName());
			org.json.simple.JSONObject taxillaJson = Taxilla.getInstance().getGspResponse(request.getData().getGstin(), request.getData().getUsername(), request.getData().getRequestId(), 
					request.getData().getRetPeriod(), Taxilla.getTaxillaMap().get(TaxillaConstants.GSTR2A_KEY), TaxillaConstants.B2B_ACTION, otp);
			List<B2BInvoiceVo> taxilla = ExternalIntegrationConvertToVoHelper.getInstance().convertB2bInvoicesFromJson(taxillaJson, "b2b");		
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((TaxillaB2bInvoiceResponse) response).setData(taxilla);
			response = constructResponse(response, Constants.SUCCESS, Constants.TAXILLA_B2B_INVOICES_FETCH_SUCCESS,
					Constants.TAXILLA_B2B_INVOICES_FETCH_SUCCESS, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.TAXILLA_B2B_INVOICES_FETCH_FAILURE,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/v1/gstr2a/b2ba_invoice", method = RequestMethod.POST)
	public ResponseEntity<BaseResponse> getGstr2B2baInvoices(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @RequestBody JSONObject<TaxillaB2bInvoiceRequest> request) throws ParseException {
		logger.info("Entry into method: getGstr1B2baInvoices");
		BaseResponse response = new TaxillaB2bInvoiceResponse();
		try {
			String otp = taxillaService.getOtp(request.getData().getOrganizationId(), Integer.parseInt(request.getData().getUserId()), request.getData().getRoleName());
			org.json.simple.JSONObject taxillaJson = Taxilla.getInstance().getGspResponse(request.getData().getGstin(), request.getData().getUsername(), request.getData().getRequestId(), 
					request.getData().getRetPeriod(), Taxilla.getTaxillaMap().get(TaxillaConstants.GSTR2A_KEY), TaxillaConstants.B2BA_ACTION, otp);
			List<B2BInvoiceVo> taxilla = ExternalIntegrationConvertToVoHelper.getInstance().convertB2bInvoicesFromJson(taxillaJson, "b2ba");		
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((TaxillaB2bInvoiceResponse) response).setData(taxilla);
			response = constructResponse(response, Constants.SUCCESS, Constants.TAXILLA_B2B_INVOICES_FETCH_SUCCESS,
					Constants.TAXILLA_B2B_INVOICES_FETCH_SUCCESS, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.TAXILLA_B2B_INVOICES_FETCH_FAILURE,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/v1/gstr2a/cdn_invoice", method = RequestMethod.POST)
	public ResponseEntity<BaseResponse> getGstr2aCdnInvoices(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @RequestBody JSONObject<TaxillaB2bInvoiceRequest> request) throws ParseException {
		logger.info("Entry into method: getGstr2aCdnInvoices");
		BaseResponse response = new TaxillaCdnInvoiceResponse();
		try {
			String otp = taxillaService.getOtp(request.getData().getOrganizationId(), Integer.parseInt(request.getData().getUserId()), request.getData().getRoleName());
			org.json.simple.JSONObject taxillaJson = Taxilla.getInstance().getGspResponse(request.getData().getGstin(), request.getData().getUsername(), request.getData().getRequestId(), 
					request.getData().getRetPeriod(), Taxilla.getTaxillaMap().get(TaxillaConstants.GSTR2A_KEY), TaxillaConstants.CDN_ACTION, otp);
			List<CdnInvoiceVo> taxilla = ExternalIntegrationConvertToVoHelper.getInstance().convertCdnInvoicesFromJson(taxillaJson, "cdn");		
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((TaxillaCdnInvoiceResponse) response).setData(taxilla);
			response = constructResponse(response, Constants.SUCCESS, Constants.TAXILLA_B2B_INVOICES_FETCH_SUCCESS,
					Constants.TAXILLA_B2B_INVOICES_FETCH_SUCCESS, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.TAXILLA_B2B_INVOICES_FETCH_FAILURE,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/v1/gstr2a/cdna_invoice", method = RequestMethod.POST)
	public ResponseEntity<BaseResponse> getGstr2aCdnaInvoices(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @RequestBody JSONObject<TaxillaB2bInvoiceRequest> request) throws ParseException {
		logger.info("Entry into method: getGstr2aCdnInvoices");
		BaseResponse response = new TaxillaCdnInvoiceResponse();
		try {
			String otp = taxillaService.getOtp(request.getData().getOrganizationId(), Integer.parseInt(request.getData().getUserId()), request.getData().getRoleName());
			org.json.simple.JSONObject taxillaJson = Taxilla.getInstance().getGspResponse(request.getData().getGstin(), request.getData().getUsername(), request.getData().getRequestId(), 
					request.getData().getRetPeriod(), Taxilla.getTaxillaMap().get(TaxillaConstants.GSTR2A_KEY), TaxillaConstants.CDNA_ACTION, otp);
			List<CdnInvoiceVo> taxilla = ExternalIntegrationConvertToVoHelper.getInstance().convertCdnInvoicesFromJson(taxillaJson, "cdna");		
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((TaxillaCdnInvoiceResponse) response).setData(taxilla);
			response = constructResponse(response, Constants.SUCCESS, Constants.TAXILLA_B2B_INVOICES_FETCH_SUCCESS,
					Constants.TAXILLA_B2B_INVOICES_FETCH_SUCCESS, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.TAXILLA_B2B_INVOICES_FETCH_FAILURE,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/v1/gstr2a/isd_credit", method = RequestMethod.POST)
	public ResponseEntity<BaseResponse> getGstr2aIsdCredit(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @RequestBody JSONObject<TaxillaB2bInvoiceRequest> request) throws ParseException {
		logger.info("Entry into method: getGstr2aIsdCredit");
		BaseResponse response = new TaxillaGenericResponse();
		try {
			String otp = taxillaService.getOtp(request.getData().getOrganizationId(), Integer.parseInt(request.getData().getUserId()), request.getData().getRoleName());
			org.json.simple.JSONObject taxillaJson = Taxilla.getInstance().getGspResponse(request.getData().getGstin(), request.getData().getUsername(), request.getData().getRequestId(), 
					request.getData().getRetPeriod(), Taxilla.getTaxillaMap().get(TaxillaConstants.GSTR2A_KEY), TaxillaConstants.ISD_ACTION, otp);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((TaxillaGenericResponse) response).setData(taxillaJson);
			response = constructResponse(response, Constants.SUCCESS, Constants.TAXILLA_B2B_INVOICES_FETCH_SUCCESS,
					Constants.TAXILLA_B2B_INVOICES_FETCH_SUCCESS, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.TAXILLA_B2B_INVOICES_FETCH_FAILURE,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/v1/gstr2a/isda_credit", method = RequestMethod.POST)
	public ResponseEntity<BaseResponse> getGstr2aIsdaCredit(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @RequestBody JSONObject<TaxillaB2bInvoiceRequest> request) throws ParseException {
		logger.info("Entry into method: getGstr2aIsdaCredit");
		BaseResponse response = new TaxillaGenericResponse();
		try {
			String otp = taxillaService.getOtp(request.getData().getOrganizationId(), Integer.parseInt(request.getData().getUserId()), request.getData().getRoleName());
			org.json.simple.JSONObject taxillaJson = Taxilla.getInstance().getGspResponse(request.getData().getGstin(), request.getData().getUsername(), request.getData().getRequestId(), 
					request.getData().getRetPeriod(), Taxilla.getTaxillaMap().get(TaxillaConstants.GSTR2A_KEY), TaxillaConstants.ISDA_ACTION, otp);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((TaxillaGenericResponse) response).setData(taxillaJson);
			response = constructResponse(response, Constants.SUCCESS, Constants.TAXILLA_B2B_INVOICES_FETCH_SUCCESS,
					Constants.TAXILLA_B2B_INVOICES_FETCH_SUCCESS, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.TAXILLA_B2B_INVOICES_FETCH_FAILURE,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/v1/gstr2a/tds_credit", method = RequestMethod.POST)
	public ResponseEntity<BaseResponse> getGstr2aTdsCredit(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @RequestBody JSONObject<TaxillaB2bInvoiceRequest> request) throws ParseException {
		logger.info("Entry into method: getGstr2aTdsCredit");
		BaseResponse response = new TaxillaGenericResponse();
		try {
			String otp = taxillaService.getOtp(request.getData().getOrganizationId(), Integer.parseInt(request.getData().getUserId()), request.getData().getRoleName());
			org.json.simple.JSONObject taxillaJson = Taxilla.getInstance().getGspResponse(request.getData().getGstin(), request.getData().getUsername(), request.getData().getRequestId(), 
					request.getData().getRetPeriod(), Taxilla.getTaxillaMap().get(TaxillaConstants.GSTR2A_KEY), TaxillaConstants.TDS_ACTION, otp);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((TaxillaGenericResponse) response).setData(taxillaJson);
			response = constructResponse(response, Constants.SUCCESS, Constants.TAXILLA_B2B_INVOICES_FETCH_SUCCESS,
					Constants.TAXILLA_B2B_INVOICES_FETCH_SUCCESS, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.TAXILLA_B2B_INVOICES_FETCH_FAILURE,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/v1/gstr2a/tdsa_credit", method = RequestMethod.POST)
	public ResponseEntity<BaseResponse> getGstr2aTdsaCredit(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @RequestBody JSONObject<TaxillaB2bInvoiceRequest> request) throws ParseException {
		logger.info("Entry into method: getGstr2aTdsaCredit");
		BaseResponse response = new TaxillaGenericResponse();
		try {
			String otp = taxillaService.getOtp(request.getData().getOrganizationId(), Integer.parseInt(request.getData().getUserId()), request.getData().getRoleName());
			org.json.simple.JSONObject taxillaJson = Taxilla.getInstance().getGspResponse(request.getData().getGstin(), request.getData().getUsername(), request.getData().getRequestId(), 
					request.getData().getRetPeriod(), Taxilla.getTaxillaMap().get(TaxillaConstants.GSTR2A_KEY), TaxillaConstants.TDSA_ACTION, otp);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((TaxillaGenericResponse) response).setData(taxillaJson);
			response = constructResponse(response, Constants.SUCCESS, Constants.TAXILLA_B2B_INVOICES_FETCH_SUCCESS,
					Constants.TAXILLA_B2B_INVOICES_FETCH_SUCCESS, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.TAXILLA_B2B_INVOICES_FETCH_FAILURE,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/v1/gstr2a/tcs_credit", method = RequestMethod.POST)
	public ResponseEntity<BaseResponse> getGstr2aTcsCredit(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @RequestBody JSONObject<TaxillaB2bInvoiceRequest> request) throws ParseException {
		logger.info("Entry into method: getGstr2aTcsCredit");
		BaseResponse response = new TaxillaGenericResponse();
		try {
			String otp = taxillaService.getOtp(request.getData().getOrganizationId(), Integer.parseInt(request.getData().getUserId()), request.getData().getRoleName());
			org.json.simple.JSONObject taxillaJson = Taxilla.getInstance().getGspResponse(request.getData().getGstin(), request.getData().getUsername(), request.getData().getRequestId(), 
					request.getData().getRetPeriod(), Taxilla.getTaxillaMap().get(TaxillaConstants.GSTR2A_KEY), TaxillaConstants.TCS_ACTION, otp);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((TaxillaGenericResponse) response).setData(taxillaJson);
			response = constructResponse(response, Constants.SUCCESS, Constants.TAXILLA_B2B_INVOICES_FETCH_SUCCESS,
					Constants.TAXILLA_B2B_INVOICES_FETCH_SUCCESS, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.TAXILLA_B2B_INVOICES_FETCH_FAILURE,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/v1/gstr2a/amdhist", method = RequestMethod.POST)
	public ResponseEntity<BaseResponse> getGstr2aAmdhist(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @RequestBody JSONObject<TaxillaB2bInvoiceRequest> request) throws ParseException {
		logger.info("Entry into method: getGstr2aAmdhist");
		BaseResponse response = new TaxillaGenericResponse();
		try {
			String otp = taxillaService.getOtp(request.getData().getOrganizationId(), Integer.parseInt(request.getData().getUserId()), request.getData().getRoleName());
			org.json.simple.JSONObject taxillaJson = Taxilla.getInstance().getGspResponse(request.getData().getGstin(), request.getData().getUsername(), request.getData().getRequestId(), 
					request.getData().getRetPeriod(), Taxilla.getTaxillaMap().get(TaxillaConstants.GSTR2A_KEY), TaxillaConstants.AMDHIST_ACTION, otp);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((TaxillaGenericResponse) response).setData(taxillaJson);
			response = constructResponse(response, Constants.SUCCESS, Constants.TAXILLA_B2B_INVOICES_FETCH_SUCCESS,
					Constants.TAXILLA_B2B_INVOICES_FETCH_SUCCESS, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.TAXILLA_B2B_INVOICES_FETCH_FAILURE,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/v1/gstr2a/impg", method = RequestMethod.POST)
	public ResponseEntity<BaseResponse> getGstr2aImpg(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @RequestBody JSONObject<TaxillaB2bInvoiceRequest> request) throws ParseException {
		logger.info("Entry into method: getGstr2aImpg");
		BaseResponse response = new TaxillaGenericResponse();
		try {
			String otp = taxillaService.getOtp(request.getData().getOrganizationId(), Integer.parseInt(request.getData().getUserId()), request.getData().getRoleName());
			org.json.simple.JSONObject taxillaJson = Taxilla.getInstance().getGspResponse(request.getData().getGstin(), request.getData().getUsername(), request.getData().getRequestId(), 
					request.getData().getRetPeriod(), Taxilla.getTaxillaMap().get(TaxillaConstants.GSTR2A_KEY), TaxillaConstants.IMPG_ACTION, otp);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((TaxillaGenericResponse) response).setData(taxillaJson);
			response = constructResponse(response, Constants.SUCCESS, Constants.TAXILLA_B2B_INVOICES_FETCH_SUCCESS,
					Constants.TAXILLA_B2B_INVOICES_FETCH_SUCCESS, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.TAXILLA_B2B_INVOICES_FETCH_FAILURE,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/v1/gstr2a/impgsez", method = RequestMethod.POST)
	public ResponseEntity<BaseResponse> getGstr2aImpgsez(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @RequestBody JSONObject<TaxillaB2bInvoiceRequest> request) throws ParseException {
		logger.info("Entry into method: getGstr2aImpg");
		BaseResponse response = new TaxillaGenericResponse();
		try {
			String otp = taxillaService.getOtp(request.getData().getOrganizationId(), Integer.parseInt(request.getData().getUserId()), request.getData().getRoleName());
			org.json.simple.JSONObject taxillaJson = Taxilla.getInstance().getGspResponse(request.getData().getGstin(), request.getData().getUsername(), request.getData().getRequestId(), 
					request.getData().getRetPeriod(), Taxilla.getTaxillaMap().get(TaxillaConstants.GSTR2A_KEY), TaxillaConstants.IMPGSEZ_ACTION, otp);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((TaxillaGenericResponse) response).setData(taxillaJson);
			response = constructResponse(response, Constants.SUCCESS, Constants.TAXILLA_B2B_INVOICES_FETCH_SUCCESS,
					Constants.TAXILLA_B2B_INVOICES_FETCH_SUCCESS, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.TAXILLA_B2B_INVOICES_FETCH_FAILURE,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/v1/gstr2b/all_details", method = RequestMethod.POST)
	public ResponseEntity<BaseResponse> getGstr2bAllDetails(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @RequestBody JSONObject<TaxillaB2bInvoiceRequest> request) throws ParseException {
		logger.info("Entry into method: getGstr2bAllDetails");
		BaseResponse response = new TaxillaGenericResponse();
		try {
			String otp = taxillaService.getOtp(request.getData().getOrganizationId(), Integer.parseInt(request.getData().getUserId()), request.getData().getRoleName());
			org.json.simple.JSONObject taxillaJson = Taxilla.getInstance().getGspResponse(request.getData().getGstin(), request.getData().getUsername(), request.getData().getRequestId(), 
					request.getData().getRetPeriod(), Taxilla.getTaxillaMap().get(TaxillaConstants.GSTR2B_KEY), TaxillaConstants.GET2B_ACTION, otp);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((TaxillaGenericResponse) response).setData(taxillaJson);
			response = constructResponse(response, Constants.SUCCESS, Constants.TAXILLA_B2B_INVOICES_FETCH_SUCCESS,
					Constants.TAXILLA_B2B_INVOICES_FETCH_SUCCESS, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.TAXILLA_B2B_INVOICES_FETCH_FAILURE,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/v1/gstr3b/all_details", method = RequestMethod.POST)
	public ResponseEntity<BaseResponse> getGstr3bAllDetails(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @RequestBody JSONObject<TaxillaB2bInvoiceRequest> request) throws ParseException {
		
		BaseResponse response = new TaxillaGenericResponse();
		try {
			String otp = taxillaService.getOtp(request.getData().getOrganizationId(), Integer.parseInt(request.getData().getUserId()), request.getData().getRoleName());
			org.json.simple.JSONObject taxillaJson = Taxilla.getInstance().getGspResponse(request.getData().getGstin(), request.getData().getUsername(), request.getData().getRequestId(), 
					request.getData().getRetPeriod(), Taxilla.getTaxillaMap().get(TaxillaConstants.GSTR3B_KEY), TaxillaConstants.RETSUM_ACTION, otp);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((TaxillaGenericResponse) response).setData(taxillaJson);
			response = constructResponse(response, Constants.SUCCESS, Constants.TAXILLA_B2B_INVOICES_FETCH_SUCCESS,
					Constants.TAXILLA_B2B_INVOICES_FETCH_SUCCESS, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.TAXILLA_B2B_INVOICES_FETCH_FAILURE,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/v1/gstr3b/gstr1_liability_auto_calc_details", method = RequestMethod.POST)
	public ResponseEntity<BaseResponse> getGstr3bGastr1LiabilityAutoCalcDetails(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @RequestBody JSONObject<TaxillaB2bInvoiceRequest> request) throws ParseException {
		BaseResponse response = new TaxillaGenericResponse();
		try {
			String otp = taxillaService.getOtp(request.getData().getOrganizationId(), Integer.parseInt(request.getData().getUserId()), request.getData().getRoleName());
			org.json.simple.JSONObject taxillaJson = Taxilla.getInstance().getGspResponse(request.getData().getGstin(), request.getData().getUsername(), request.getData().getRequestId(), 
					request.getData().getRetPeriod(), Taxilla.getTaxillaMap().get(TaxillaConstants.GSTR3B_KEY), TaxillaConstants.AUTOLIAB_ACTION, otp);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((TaxillaGenericResponse) response).setData(taxillaJson);
			response = constructResponse(response, Constants.SUCCESS, Constants.TAXILLA_B2B_INVOICES_FETCH_SUCCESS,
					Constants.TAXILLA_B2B_INVOICES_FETCH_SUCCESS, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.TAXILLA_B2B_INVOICES_FETCH_FAILURE,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/v1/gstr4/b2b_invoices", method = RequestMethod.POST)
	public ResponseEntity<BaseResponse> getGstr4B2bInvoices(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @RequestBody JSONObject<TaxillaB2bInvoiceRequest> request) throws ParseException {
		BaseResponse response = new TaxillaGenericResponse();
		try {
			String otp = taxillaService.getOtp(request.getData().getOrganizationId(), Integer.parseInt(request.getData().getUserId()), request.getData().getRoleName());
			org.json.simple.JSONObject taxillaJson = Taxilla.getInstance().getGspResponse(request.getData().getGstin(), request.getData().getUsername(), request.getData().getRequestId(), 
					request.getData().getRetPeriod(), Taxilla.getTaxillaMap().get(TaxillaConstants.GSTR4_KEY), TaxillaConstants.B2B_ACTION, otp);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((TaxillaGenericResponse) response).setData(taxillaJson);
			response = constructResponse(response, Constants.SUCCESS, Constants.TAXILLA_B2B_INVOICES_FETCH_SUCCESS,
					Constants.TAXILLA_B2B_INVOICES_FETCH_SUCCESS, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.TAXILLA_B2B_INVOICES_FETCH_FAILURE,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/v1/gstr4/b2b_unregistered_invoices", method = RequestMethod.POST)
	public ResponseEntity<BaseResponse> getGstr4B2bUnregisteredInvoices(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @RequestBody JSONObject<TaxillaB2bInvoiceRequest> request) throws ParseException {
		BaseResponse response = new TaxillaGenericResponse();
		try {
			String otp = taxillaService.getOtp(request.getData().getOrganizationId(), Integer.parseInt(request.getData().getUserId()), request.getData().getRoleName());
			org.json.simple.JSONObject taxillaJson = Taxilla.getInstance().getGspResponse(request.getData().getGstin(), request.getData().getUsername(), request.getData().getRequestId(), 
					request.getData().getRetPeriod(), Taxilla.getTaxillaMap().get(TaxillaConstants.GSTR4_KEY), TaxillaConstants.B2BUR_ACTION, otp);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((TaxillaGenericResponse) response).setData(taxillaJson);
			response = constructResponse(response, Constants.SUCCESS, Constants.TAXILLA_B2B_INVOICES_FETCH_SUCCESS,
					Constants.TAXILLA_B2B_INVOICES_FETCH_SUCCESS, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.TAXILLA_B2B_INVOICES_FETCH_FAILURE,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/v1/gstr4/imports_of_services", method = RequestMethod.POST)
	public ResponseEntity<BaseResponse> getGstr4ImportsOfServices(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @RequestBody JSONObject<TaxillaB2bInvoiceRequest> request) throws ParseException {
		BaseResponse response = new TaxillaGenericResponse();
		try {
			String otp = taxillaService.getOtp(request.getData().getOrganizationId(), Integer.parseInt(request.getData().getUserId()), request.getData().getRoleName());
			org.json.simple.JSONObject taxillaJson = Taxilla.getInstance().getGspResponse(request.getData().getGstin(), request.getData().getUsername(), request.getData().getRequestId(), 
					request.getData().getRetPeriod(), Taxilla.getTaxillaMap().get(TaxillaConstants.GSTR4_KEY), TaxillaConstants.IMPS_ACTION, otp);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((TaxillaGenericResponse) response).setData(taxillaJson);
			response = constructResponse(response, Constants.SUCCESS, Constants.TAXILLA_B2B_INVOICES_FETCH_SUCCESS,
					Constants.TAXILLA_B2B_INVOICES_FETCH_SUCCESS, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.TAXILLA_B2B_INVOICES_FETCH_FAILURE,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/v1/gstr4/cdnr", method = RequestMethod.POST)
	public ResponseEntity<BaseResponse> getGstr4Cdnr(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @RequestBody JSONObject<TaxillaB2bInvoiceRequest> request) throws ParseException {
		BaseResponse response = new TaxillaGenericResponse();
		try {
			String otp = taxillaService.getOtp(request.getData().getOrganizationId(), Integer.parseInt(request.getData().getUserId()), request.getData().getRoleName());
			org.json.simple.JSONObject taxillaJson = Taxilla.getInstance().getGspResponse(request.getData().getGstin(), request.getData().getUsername(), request.getData().getRequestId(), 
					request.getData().getRetPeriod(), Taxilla.getTaxillaMap().get(TaxillaConstants.GSTR4_KEY), TaxillaConstants.CDNR_ACTION, otp);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((TaxillaGenericResponse) response).setData(taxillaJson);
			response = constructResponse(response, Constants.SUCCESS, Constants.TAXILLA_B2B_INVOICES_FETCH_SUCCESS,
					Constants.TAXILLA_B2B_INVOICES_FETCH_SUCCESS, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.TAXILLA_B2B_INVOICES_FETCH_FAILURE,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/v1/gstr4/cdnur", method = RequestMethod.POST)
	public ResponseEntity<BaseResponse> getGstr4Cdnur(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @RequestBody JSONObject<TaxillaB2bInvoiceRequest> request) throws ParseException {
		BaseResponse response = new TaxillaGenericResponse();
		try {
			String otp = taxillaService.getOtp(request.getData().getOrganizationId(), Integer.parseInt(request.getData().getUserId()), request.getData().getRoleName());
			org.json.simple.JSONObject taxillaJson = Taxilla.getInstance().getGspResponse(request.getData().getGstin(), request.getData().getUsername(), request.getData().getRequestId(), 
					request.getData().getRetPeriod(), Taxilla.getTaxillaMap().get(TaxillaConstants.GSTR4_KEY), TaxillaConstants.CDNUR_ACTION, otp);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((TaxillaGenericResponse) response).setData(taxillaJson);
			response = constructResponse(response, Constants.SUCCESS, Constants.TAXILLA_B2B_INVOICES_FETCH_SUCCESS,
					Constants.TAXILLA_B2B_INVOICES_FETCH_SUCCESS, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.TAXILLA_B2B_INVOICES_FETCH_FAILURE,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/v1/gstr4/tax_on_outward_supplies", method = RequestMethod.POST)
	public ResponseEntity<BaseResponse> getGstr4TaxOnOutwardSupplies(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @RequestBody JSONObject<TaxillaB2bInvoiceRequest> request) throws ParseException {
		BaseResponse response = new TaxillaGenericResponse();
		try {
			String otp = taxillaService.getOtp(request.getData().getOrganizationId(), Integer.parseInt(request.getData().getUserId()), request.getData().getRoleName());
			org.json.simple.JSONObject taxillaJson = Taxilla.getInstance().getGspResponse(request.getData().getGstin(), request.getData().getUsername(), request.getData().getRequestId(), 
					request.getData().getRetPeriod(), Taxilla.getTaxillaMap().get(TaxillaConstants.GSTR4_KEY), TaxillaConstants.TXOS_ACTION, otp);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((TaxillaGenericResponse) response).setData(taxillaJson);
			response = constructResponse(response, Constants.SUCCESS, Constants.TAXILLA_B2B_INVOICES_FETCH_SUCCESS,
					Constants.TAXILLA_B2B_INVOICES_FETCH_SUCCESS, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.TAXILLA_B2B_INVOICES_FETCH_FAILURE,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/v1/gstr4/advances_paid", method = RequestMethod.POST)
	public ResponseEntity<BaseResponse> getGstr4AvancesPaid(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @RequestBody JSONObject<TaxillaB2bInvoiceRequest> request) throws ParseException {
		BaseResponse response = new TaxillaGenericResponse();
		try {
			String otp = taxillaService.getOtp(request.getData().getOrganizationId(), Integer.parseInt(request.getData().getUserId()), request.getData().getRoleName());
			org.json.simple.JSONObject taxillaJson = Taxilla.getInstance().getGspResponse(request.getData().getGstin(), request.getData().getUsername(), request.getData().getRequestId(), 
					request.getData().getRetPeriod(), Taxilla.getTaxillaMap().get(TaxillaConstants.GSTR4_KEY), "AT", otp);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((TaxillaGenericResponse) response).setData(taxillaJson);
			response = constructResponse(response, Constants.SUCCESS, Constants.TAXILLA_B2B_INVOICES_FETCH_SUCCESS,
					Constants.TAXILLA_B2B_INVOICES_FETCH_SUCCESS, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.TAXILLA_B2B_INVOICES_FETCH_FAILURE,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/v1/gstr4/advances_adjusted", method = RequestMethod.POST)
	public ResponseEntity<BaseResponse> getGstr4AdvancesAdjusted(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @RequestBody JSONObject<TaxillaB2bInvoiceRequest> request) throws ParseException {
		BaseResponse response = new TaxillaGenericResponse();
		try {
			String otp = taxillaService.getOtp(request.getData().getOrganizationId(), Integer.parseInt(request.getData().getUserId()), request.getData().getRoleName());
			org.json.simple.JSONObject taxillaJson = Taxilla.getInstance().getGspResponse(request.getData().getGstin(), request.getData().getUsername(), request.getData().getRequestId(), 
					request.getData().getRetPeriod(), Taxilla.getTaxillaMap().get(TaxillaConstants.GSTR4_KEY), "TXP", otp);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((TaxillaGenericResponse) response).setData(taxillaJson);
			response = constructResponse(response, Constants.SUCCESS, Constants.TAXILLA_B2B_INVOICES_FETCH_SUCCESS,
					Constants.TAXILLA_B2B_INVOICES_FETCH_SUCCESS, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.TAXILLA_B2B_INVOICES_FETCH_FAILURE,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/v1/gstr4/summary", method = RequestMethod.POST)
	public ResponseEntity<BaseResponse> getGstr4Summary(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @RequestBody JSONObject<TaxillaB2bInvoiceRequest> request) throws ParseException {
		BaseResponse response = new TaxillaGenericResponse();
		try {
			String otp = taxillaService.getOtp(request.getData().getOrganizationId(), Integer.parseInt(request.getData().getUserId()), request.getData().getRoleName());
			org.json.simple.JSONObject taxillaJson = Taxilla.getInstance().getGspResponse(request.getData().getGstin(), request.getData().getUsername(), request.getData().getRequestId(), 
					request.getData().getRetPeriod(), Taxilla.getTaxillaMap().get(TaxillaConstants.GSTR4_KEY), "RETSUM", otp);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((TaxillaGenericResponse) response).setData(taxillaJson);
			response = constructResponse(response, Constants.SUCCESS, Constants.TAXILLA_B2B_INVOICES_FETCH_SUCCESS,
					Constants.TAXILLA_B2B_INVOICES_FETCH_SUCCESS, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.TAXILLA_B2B_INVOICES_FETCH_FAILURE,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/v1/gstr4/tds", method = RequestMethod.POST)
	public ResponseEntity<BaseResponse> getGstr4Tds(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @RequestBody JSONObject<TaxillaB2bInvoiceRequest> request) throws ParseException {
		BaseResponse response = new TaxillaGenericResponse();
		try {
			String otp = taxillaService.getOtp(request.getData().getOrganizationId(), Integer.parseInt(request.getData().getUserId()), request.getData().getRoleName());
			org.json.simple.JSONObject taxillaJson = Taxilla.getInstance().getGspResponse(request.getData().getGstin(), request.getData().getUsername(), request.getData().getRequestId(), 
					request.getData().getRetPeriod(), Taxilla.getTaxillaMap().get(TaxillaConstants.GSTR4_KEY), "TDS", otp);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((TaxillaGenericResponse) response).setData(taxillaJson);
			response = constructResponse(response, Constants.SUCCESS, Constants.TAXILLA_B2B_INVOICES_FETCH_SUCCESS,
					Constants.TAXILLA_B2B_INVOICES_FETCH_SUCCESS, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.TAXILLA_B2B_INVOICES_FETCH_FAILURE,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/v1/gstr4/advances_adjusted_amendment", method = RequestMethod.POST)
	public ResponseEntity<BaseResponse> getGstr4AdjustedAmendments(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @RequestBody JSONObject<TaxillaB2bInvoiceRequest> request) throws ParseException {
		BaseResponse response = new TaxillaGenericResponse();
		try {
			String otp = taxillaService.getOtp(request.getData().getOrganizationId(), Integer.parseInt(request.getData().getUserId()), request.getData().getRoleName());
			org.json.simple.JSONObject taxillaJson = Taxilla.getInstance().getGspResponse(request.getData().getGstin(), request.getData().getUsername(), request.getData().getRequestId(), 
					request.getData().getRetPeriod(), Taxilla.getTaxillaMap().get(TaxillaConstants.GSTR4_KEY), "TXPA", otp);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((TaxillaGenericResponse) response).setData(taxillaJson);
			response = constructResponse(response, Constants.SUCCESS, Constants.TAXILLA_B2B_INVOICES_FETCH_SUCCESS,
					Constants.TAXILLA_B2B_INVOICES_FETCH_SUCCESS, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.TAXILLA_B2B_INVOICES_FETCH_FAILURE,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/v1/gstr4/advances_paid_amendment", method = RequestMethod.POST)
	public ResponseEntity<BaseResponse> getGstr4PaidAmendment(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @RequestBody JSONObject<TaxillaB2bInvoiceRequest> request) throws ParseException {
		BaseResponse response = new TaxillaGenericResponse();
		try {
			String otp = taxillaService.getOtp(request.getData().getOrganizationId(), Integer.parseInt(request.getData().getUserId()), request.getData().getRoleName());
			org.json.simple.JSONObject taxillaJson = Taxilla.getInstance().getGspResponse(request.getData().getGstin(), request.getData().getUsername(), request.getData().getRequestId(), 
					request.getData().getRetPeriod(), Taxilla.getTaxillaMap().get(TaxillaConstants.GSTR4_KEY), "ATA", otp);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((TaxillaGenericResponse) response).setData(taxillaJson);
			response = constructResponse(response, Constants.SUCCESS, Constants.TAXILLA_B2B_INVOICES_FETCH_SUCCESS,
					Constants.TAXILLA_B2B_INVOICES_FETCH_SUCCESS, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.TAXILLA_B2B_INVOICES_FETCH_FAILURE,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/v1/gstr4/b2b_amendment", method = RequestMethod.POST)
	public ResponseEntity<BaseResponse> getGstr4B2bAmendment(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @RequestBody JSONObject<TaxillaB2bInvoiceRequest> request) throws ParseException {
		BaseResponse response = new TaxillaGenericResponse();
		try {
			String otp = taxillaService.getOtp(request.getData().getOrganizationId(), Integer.parseInt(request.getData().getUserId()), request.getData().getRoleName());
			org.json.simple.JSONObject taxillaJson = Taxilla.getInstance().getGspResponse(request.getData().getGstin(), request.getData().getUsername(), request.getData().getRequestId(), 
					request.getData().getRetPeriod(), Taxilla.getTaxillaMap().get(TaxillaConstants.GSTR4_KEY), "B2BA", otp);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((TaxillaGenericResponse) response).setData(taxillaJson);
			response = constructResponse(response, Constants.SUCCESS, Constants.TAXILLA_B2B_INVOICES_FETCH_SUCCESS,
					Constants.TAXILLA_B2B_INVOICES_FETCH_SUCCESS, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.TAXILLA_B2B_INVOICES_FETCH_FAILURE,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/v1/gstr4/b2bur_amendment", method = RequestMethod.POST)
	public ResponseEntity<BaseResponse> getGstr4B2burAmendment(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @RequestBody JSONObject<TaxillaB2bInvoiceRequest> request) throws ParseException {
		BaseResponse response = new TaxillaGenericResponse();
		try {
			String otp = taxillaService.getOtp(request.getData().getOrganizationId(), Integer.parseInt(request.getData().getUserId()), request.getData().getRoleName());
			org.json.simple.JSONObject taxillaJson = Taxilla.getInstance().getGspResponse(request.getData().getGstin(), request.getData().getUsername(), request.getData().getRequestId(), 
					request.getData().getRetPeriod(), Taxilla.getTaxillaMap().get(TaxillaConstants.GSTR4_KEY), "B2BURA", otp);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((TaxillaGenericResponse) response).setData(taxillaJson);
			response = constructResponse(response, Constants.SUCCESS, Constants.TAXILLA_B2B_INVOICES_FETCH_SUCCESS,
					Constants.TAXILLA_B2B_INVOICES_FETCH_SUCCESS, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.TAXILLA_B2B_INVOICES_FETCH_FAILURE,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/v1/gstr4/cdnr_amendment", method = RequestMethod.POST)
	public ResponseEntity<BaseResponse> getGstr4CdnrAmendment(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @RequestBody JSONObject<TaxillaB2bInvoiceRequest> request) throws ParseException {
		BaseResponse response = new TaxillaGenericResponse();
		try {
			String otp = taxillaService.getOtp(request.getData().getOrganizationId(), Integer.parseInt(request.getData().getUserId()), request.getData().getRoleName());
			org.json.simple.JSONObject taxillaJson = Taxilla.getInstance().getGspResponse(request.getData().getGstin(), request.getData().getUsername(), request.getData().getRequestId(), 
					request.getData().getRetPeriod(), Taxilla.getTaxillaMap().get(TaxillaConstants.GSTR4_KEY), "CDNRA", otp);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((TaxillaGenericResponse) response).setData(taxillaJson);
			response = constructResponse(response, Constants.SUCCESS, Constants.TAXILLA_B2B_INVOICES_FETCH_SUCCESS,
					Constants.TAXILLA_B2B_INVOICES_FETCH_SUCCESS, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.TAXILLA_B2B_INVOICES_FETCH_FAILURE,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/v1/gstr4/cdnur_amendment", method = RequestMethod.POST)
	public ResponseEntity<BaseResponse> getGstr4CdnurAmendment(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @RequestBody JSONObject<TaxillaB2bInvoiceRequest> request) throws ParseException {
		BaseResponse response = new TaxillaGenericResponse();
		try {
			String otp = taxillaService.getOtp(request.getData().getOrganizationId(), Integer.parseInt(request.getData().getUserId()), request.getData().getRoleName());
			org.json.simple.JSONObject taxillaJson = Taxilla.getInstance().getGspResponse(request.getData().getGstin(), request.getData().getUsername(), request.getData().getRequestId(), 
					request.getData().getRetPeriod(), Taxilla.getTaxillaMap().get(TaxillaConstants.GSTR4_KEY), "CDNURA", otp);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((TaxillaGenericResponse) response).setData(taxillaJson);
			response = constructResponse(response, Constants.SUCCESS, Constants.TAXILLA_B2B_INVOICES_FETCH_SUCCESS,
					Constants.TAXILLA_B2B_INVOICES_FETCH_SUCCESS, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.TAXILLA_B2B_INVOICES_FETCH_FAILURE,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/v1/gstr4/imports_of_services_amendment", method = RequestMethod.POST)
	public ResponseEntity<BaseResponse> getGstr4ImportsOfServicesAmendment(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @RequestBody JSONObject<TaxillaB2bInvoiceRequest> request) throws ParseException {
		BaseResponse response = new TaxillaGenericResponse();
		try {
			String otp = taxillaService.getOtp(request.getData().getOrganizationId(), Integer.parseInt(request.getData().getUserId()), request.getData().getRoleName());
			org.json.simple.JSONObject taxillaJson = Taxilla.getInstance().getGspResponse(request.getData().getGstin(), request.getData().getUsername(), request.getData().getRequestId(), 
					request.getData().getRetPeriod(), Taxilla.getTaxillaMap().get(TaxillaConstants.GSTR4_KEY), "IMPSA", otp);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((TaxillaGenericResponse) response).setData(taxillaJson);
			response = constructResponse(response, Constants.SUCCESS, Constants.TAXILLA_B2B_INVOICES_FETCH_SUCCESS,
					Constants.TAXILLA_B2B_INVOICES_FETCH_SUCCESS, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.TAXILLA_B2B_INVOICES_FETCH_FAILURE,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/v1/gstr4/txos_amendment", method = RequestMethod.POST)
	public ResponseEntity<BaseResponse> getGstr4TxosAmendment(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @RequestBody JSONObject<TaxillaB2bInvoiceRequest> request) throws ParseException {
		BaseResponse response = new TaxillaGenericResponse();
		try {
			String otp = taxillaService.getOtp(request.getData().getOrganizationId(), Integer.parseInt(request.getData().getUserId()), request.getData().getRoleName());
			org.json.simple.JSONObject taxillaJson = Taxilla.getInstance().getGspResponse(request.getData().getGstin(), request.getData().getUsername(), request.getData().getRequestId(), 
					request.getData().getRetPeriod(), Taxilla.getTaxillaMap().get(TaxillaConstants.GSTR4_KEY), "TXOSA", otp);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((TaxillaGenericResponse) response).setData(taxillaJson);
			response = constructResponse(response, Constants.SUCCESS, Constants.TAXILLA_B2B_INVOICES_FETCH_SUCCESS,
					Constants.TAXILLA_B2B_INVOICES_FETCH_SUCCESS, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.TAXILLA_B2B_INVOICES_FETCH_FAILURE,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/v1/gstr9/auto_calculated_details", method = RequestMethod.POST)
	public ResponseEntity<BaseResponse> getGstr9AutoCalc(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @RequestBody JSONObject<TaxillaB2bInvoiceRequest> request) throws ParseException {
		BaseResponse response = new TaxillaGenericResponse();
		try {
			String otp = taxillaService.getOtp(request.getData().getOrganizationId(), Integer.parseInt(request.getData().getUserId()), request.getData().getRoleName());
			org.json.simple.JSONObject taxillaJson = Taxilla.getInstance().getGspResponse(request.getData().getGstin(), request.getData().getUsername(), request.getData().getRequestId(), 
					request.getData().getRetPeriod(), Taxilla.getTaxillaMap().get(TaxillaConstants.GSTR9_KEY), "CALRCDS", otp);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((TaxillaGenericResponse) response).setData(taxillaJson);
			response = constructResponse(response, Constants.SUCCESS, Constants.TAXILLA_B2B_INVOICES_FETCH_SUCCESS,
					Constants.TAXILLA_B2B_INVOICES_FETCH_SUCCESS, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.TAXILLA_B2B_INVOICES_FETCH_FAILURE,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/v1/gstr9/all_details", method = RequestMethod.POST)
	public ResponseEntity<BaseResponse> getGstr9AllDetails(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @RequestBody JSONObject<TaxillaB2bInvoiceRequest> request) throws ParseException {
		BaseResponse response = new TaxillaGenericResponse();
		try {
			String otp = taxillaService.getOtp(request.getData().getOrganizationId(), Integer.parseInt(request.getData().getUserId()), request.getData().getRoleName());
			org.json.simple.JSONObject taxillaJson = Taxilla.getInstance().getGspResponse(request.getData().getGstin(), request.getData().getUsername(), request.getData().getRequestId(), 
					request.getData().getRetPeriod(), Taxilla.getTaxillaMap().get(TaxillaConstants.GSTR9_KEY), "RECORDS", otp);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((TaxillaGenericResponse) response).setData(taxillaJson);
			response = constructResponse(response, Constants.SUCCESS, Constants.TAXILLA_B2B_INVOICES_FETCH_SUCCESS,
					Constants.TAXILLA_B2B_INVOICES_FETCH_SUCCESS, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.TAXILLA_B2B_INVOICES_FETCH_FAILURE,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/v1/gstr9/8a_details", method = RequestMethod.POST)
	public ResponseEntity<BaseResponse> getGstr98aDetails(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @RequestBody JSONObject<TaxillaB2bInvoiceRequest> request) throws ParseException {
		BaseResponse response = new TaxillaGenericResponse();
		try {
			String otp = taxillaService.getOtp(request.getData().getOrganizationId(), Integer.parseInt(request.getData().getUserId()), request.getData().getRoleName());
			org.json.simple.JSONObject taxillaJson = Taxilla.getInstance().getGspResponse(request.getData().getGstin(), request.getData().getUsername(), request.getData().getRequestId(), 
					request.getData().getRetPeriod(), Taxilla.getTaxillaMap().get(TaxillaConstants.GSTR9_KEY), "FILEDETL8A", otp);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((TaxillaGenericResponse) response).setData(taxillaJson);
			response = constructResponse(response, Constants.SUCCESS, Constants.TAXILLA_B2B_INVOICES_FETCH_SUCCESS,
					Constants.TAXILLA_B2B_INVOICES_FETCH_SUCCESS, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.TAXILLA_B2B_INVOICES_FETCH_FAILURE,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/v1/gstr9a/auto_calculated_details", method = RequestMethod.POST)
	public ResponseEntity<BaseResponse> getGstr9aAutoCalcDetails(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @RequestBody JSONObject<TaxillaB2bInvoiceRequest> request) throws ParseException {
		BaseResponse response = new TaxillaGenericResponse();
		try {
			String otp = taxillaService.getOtp(request.getData().getOrganizationId(), Integer.parseInt(request.getData().getUserId()), request.getData().getRoleName());
			org.json.simple.JSONObject taxillaJson = Taxilla.getInstance().getGspResponse(request.getData().getGstin(), request.getData().getUsername(), request.getData().getRequestId(), 
					request.getData().getRetPeriod(), Taxilla.getTaxillaMap().get(TaxillaConstants.GSTR9A_KEY), "CALRCDS", otp);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((TaxillaGenericResponse) response).setData(taxillaJson);
			response = constructResponse(response, Constants.SUCCESS, Constants.TAXILLA_B2B_INVOICES_FETCH_SUCCESS,
					Constants.TAXILLA_B2B_INVOICES_FETCH_SUCCESS, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.TAXILLA_B2B_INVOICES_FETCH_FAILURE,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/v1/gstr9a/all_details", method = RequestMethod.POST)
	public ResponseEntity<BaseResponse> getGstr9aAllDetails(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @RequestBody JSONObject<TaxillaB2bInvoiceRequest> request) throws ParseException {
		BaseResponse response = new TaxillaGenericResponse();
		try {
			String otp = taxillaService.getOtp(request.getData().getOrganizationId(), Integer.parseInt(request.getData().getUserId()), request.getData().getRoleName());
			org.json.simple.JSONObject taxillaJson = Taxilla.getInstance().getGspResponse(request.getData().getGstin(), request.getData().getUsername(), request.getData().getRequestId(), 
					request.getData().getRetPeriod(), Taxilla.getTaxillaMap().get(TaxillaConstants.GSTR9A_KEY), "RECORDS", otp);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((TaxillaGenericResponse) response).setData(taxillaJson);
			response = constructResponse(response, Constants.SUCCESS, Constants.TAXILLA_B2B_INVOICES_FETCH_SUCCESS,
					Constants.TAXILLA_B2B_INVOICES_FETCH_SUCCESS, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.TAXILLA_B2B_INVOICES_FETCH_FAILURE,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/v1/gstr9c/9_records_for_9c", method = RequestMethod.POST)
	public ResponseEntity<BaseResponse> getGstr9cRecordsFor9c(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @RequestBody JSONObject<TaxillaB2bInvoiceRequest> request) throws ParseException {
		BaseResponse response = new TaxillaGenericResponse();
		try {
			String otp = taxillaService.getOtp(request.getData().getOrganizationId(), Integer.parseInt(request.getData().getUserId()), request.getData().getRoleName());
			org.json.simple.JSONObject taxillaJson = Taxilla.getInstance().getGspResponse(request.getData().getGstin(), request.getData().getUsername(), request.getData().getRequestId(), 
					request.getData().getRetPeriod(), Taxilla.getTaxillaMap().get(TaxillaConstants.GSTR9C_KEY), "RECDS", otp);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((TaxillaGenericResponse) response).setData(taxillaJson);
			response = constructResponse(response, Constants.SUCCESS, Constants.TAXILLA_B2B_INVOICES_FETCH_SUCCESS,
					Constants.TAXILLA_B2B_INVOICES_FETCH_SUCCESS, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.TAXILLA_B2B_INVOICES_FETCH_FAILURE,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/v1/gstr9c/summary", method = RequestMethod.POST)
	public ResponseEntity<BaseResponse> getGstr9cSummary(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @RequestBody JSONObject<TaxillaB2bInvoiceRequest> request) throws ParseException {
		BaseResponse response = new TaxillaGenericResponse();
		try {
			String otp = taxillaService.getOtp(request.getData().getOrganizationId(), Integer.parseInt(request.getData().getUserId()), request.getData().getRoleName());
			org.json.simple.JSONObject taxillaJson = Taxilla.getInstance().getGspResponse(request.getData().getGstin(), request.getData().getUsername(), request.getData().getRequestId(), 
					request.getData().getRetPeriod(), Taxilla.getTaxillaMap().get(TaxillaConstants.GSTR9C_KEY), "RETSUM", otp);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((TaxillaGenericResponse) response).setData(taxillaJson);
			response = constructResponse(response, Constants.SUCCESS, Constants.TAXILLA_B2B_INVOICES_FETCH_SUCCESS,
					Constants.TAXILLA_B2B_INVOICES_FETCH_SUCCESS, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.TAXILLA_B2B_INVOICES_FETCH_FAILURE,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/v1/itc03/4a_invoices", method = RequestMethod.POST)
	public ResponseEntity<BaseResponse> getItc034aInvoices(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @RequestBody JSONObject<TaxillaB2bInvoiceRequest> request) throws ParseException {
		BaseResponse response = new TaxillaGenericResponse();
		try {
			String otp = taxillaService.getOtp(request.getData().getOrganizationId(), Integer.parseInt(request.getData().getUserId()), request.getData().getRoleName());
			org.json.simple.JSONObject taxillaJson = Taxilla.getInstance().getGspResponse(request.getData().getGstin(), request.getData().getUsername(), request.getData().getRequestId(), 
					request.getData().getRetPeriod(), Taxilla.getTaxillaMap().get(TaxillaConstants.ITC03_KEY), "INV4a", otp);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((TaxillaGenericResponse) response).setData(taxillaJson);
			response = constructResponse(response, Constants.SUCCESS, Constants.TAXILLA_B2B_INVOICES_FETCH_SUCCESS,
					Constants.TAXILLA_B2B_INVOICES_FETCH_SUCCESS, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.TAXILLA_B2B_INVOICES_FETCH_FAILURE,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/v1/itc03/4b_invoices", method = RequestMethod.POST)
	public ResponseEntity<BaseResponse> getItc034bInvoices(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @RequestBody JSONObject<TaxillaB2bInvoiceRequest> request) throws ParseException {
		BaseResponse response = new TaxillaGenericResponse();
		try {
			String otp = taxillaService.getOtp(request.getData().getOrganizationId(), Integer.parseInt(request.getData().getUserId()), request.getData().getRoleName());
			org.json.simple.JSONObject taxillaJson = Taxilla.getInstance().getGspResponse(request.getData().getGstin(), request.getData().getUsername(), request.getData().getRequestId(), 
					request.getData().getRetPeriod(), Taxilla.getTaxillaMap().get(TaxillaConstants.ITC03_KEY), "INV4B", otp);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((TaxillaGenericResponse) response).setData(taxillaJson);
			response = constructResponse(response, Constants.SUCCESS, Constants.TAXILLA_B2B_INVOICES_FETCH_SUCCESS,
					Constants.TAXILLA_B2B_INVOICES_FETCH_SUCCESS, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.TAXILLA_B2B_INVOICES_FETCH_FAILURE,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/v1/itc03/4a_summary", method = RequestMethod.POST)
	public ResponseEntity<BaseResponse> getItc034aSummary(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @RequestBody JSONObject<TaxillaB2bInvoiceRequest> request) throws ParseException {
		BaseResponse response = new TaxillaGenericResponse();
		try {
			String otp = taxillaService.getOtp(request.getData().getOrganizationId(), Integer.parseInt(request.getData().getUserId()), request.getData().getRoleName());
			org.json.simple.JSONObject taxillaJson = Taxilla.getInstance().getGspResponse(request.getData().getGstin(), request.getData().getUsername(), request.getData().getRequestId(), 
					request.getData().getRetPeriod(), Taxilla.getTaxillaMap().get(TaxillaConstants.ITC03_KEY), "SUM4A", otp);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((TaxillaGenericResponse) response).setData(taxillaJson);
			response = constructResponse(response, Constants.SUCCESS, Constants.TAXILLA_B2B_INVOICES_FETCH_SUCCESS,
					Constants.TAXILLA_B2B_INVOICES_FETCH_SUCCESS, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.TAXILLA_B2B_INVOICES_FETCH_FAILURE,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/v1/itc03/4b_summary", method = RequestMethod.POST)
	public ResponseEntity<BaseResponse> getItc034bSummary(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @RequestBody JSONObject<TaxillaB2bInvoiceRequest> request) throws ParseException {
		BaseResponse response = new TaxillaGenericResponse();
		try {
			String otp = taxillaService.getOtp(request.getData().getOrganizationId(), Integer.parseInt(request.getData().getUserId()), request.getData().getRoleName());
			org.json.simple.JSONObject taxillaJson = Taxilla.getInstance().getGspResponse(request.getData().getGstin(), request.getData().getUsername(), request.getData().getRequestId(), 
					request.getData().getRetPeriod(), Taxilla.getTaxillaMap().get(TaxillaConstants.ITC03_KEY), "SUM4B", otp);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((TaxillaGenericResponse) response).setData(taxillaJson);
			response = constructResponse(response, Constants.SUCCESS, Constants.TAXILLA_B2B_INVOICES_FETCH_SUCCESS,
					Constants.TAXILLA_B2B_INVOICES_FETCH_SUCCESS, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.TAXILLA_B2B_INVOICES_FETCH_FAILURE,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/v1/itc04/invoices", method = RequestMethod.POST)
	public ResponseEntity<BaseResponse> getItc04invoices(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @RequestBody JSONObject<TaxillaB2bInvoiceRequest> request) throws ParseException {
		BaseResponse response = new TaxillaGenericResponse();
		try {
			String otp = taxillaService.getOtp(request.getData().getOrganizationId(), Integer.parseInt(request.getData().getUserId()), request.getData().getRoleName());
			org.json.simple.JSONObject taxillaJson = Taxilla.getInstance().getGspResponse(request.getData().getGstin(), request.getData().getUsername(), request.getData().getRequestId(), 
					request.getData().getRetPeriod(), Taxilla.getTaxillaMap().get(TaxillaConstants.ITC04_KEY), "GET", otp);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((TaxillaGenericResponse) response).setData(taxillaJson);
			response = constructResponse(response, Constants.SUCCESS, Constants.TAXILLA_B2B_INVOICES_FETCH_SUCCESS,
					Constants.TAXILLA_B2B_INVOICES_FETCH_SUCCESS, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.TAXILLA_B2B_INVOICES_FETCH_FAILURE,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/v1/itc04/summary", method = RequestMethod.POST)
	public ResponseEntity<BaseResponse> getItc04Summary(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @RequestBody JSONObject<TaxillaB2bInvoiceRequest> request) throws ParseException {
		BaseResponse response = new TaxillaGenericResponse();
		try {
			String otp = taxillaService.getOtp(request.getData().getOrganizationId(), Integer.parseInt(request.getData().getUserId()), request.getData().getRoleName());
			org.json.simple.JSONObject taxillaJson = Taxilla.getInstance().getGspResponse(request.getData().getGstin(), request.getData().getUsername(), request.getData().getRequestId(), 
					request.getData().getRetPeriod(), Taxilla.getTaxillaMap().get(TaxillaConstants.ITC04_KEY), "RETSUM", otp);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((TaxillaGenericResponse) response).setData(taxillaJson);
			response = constructResponse(response, Constants.SUCCESS, Constants.TAXILLA_B2B_INVOICES_FETCH_SUCCESS,
					Constants.TAXILLA_B2B_INVOICES_FETCH_SUCCESS, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.TAXILLA_B2B_INVOICES_FETCH_FAILURE,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}

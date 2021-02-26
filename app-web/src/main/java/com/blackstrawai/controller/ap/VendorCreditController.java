package com.blackstrawai.controller.ap;

import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.ap.VendorCreditService;
import com.blackstrawai.ap.creditnote.ApplyFundVo;
import com.blackstrawai.ap.creditnote.VendorCreditCreateVo;
import com.blackstrawai.ap.dropdowns.VendorCreditNoteDropDownVo;
import com.blackstrawai.common.BaseController;
import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.common.Constants;
import com.blackstrawai.helper.ApConvertToVoHelper;
import com.blackstrawai.request.JSONObject;
import com.blackstrawai.request.ap.creditnote.ApplyFundCreateRequest;
import com.blackstrawai.request.ap.creditnote.VendorCreditCreateRequest;
import com.blackstrawai.response.ap.creditnote.CreditNoteDropDownResponse;
import com.blackstrawai.response.ap.creditnote.VendorCreditResponse;
import com.blackstrawai.response.keycontact.vendor.VendorResponse;

@RestController
@CrossOrigin
@RequestMapping("/decifer/ap/vendorcredit")
public class VendorCreditController extends BaseController {
	private Logger logger = Logger.getLogger(VendorCreditController.class);

	@Autowired
	private VendorCreditService vendorCreditService;

	/**
	 * Creates the vendor credit.
	 *
	 * @param httpServletRequest  the http servlet request
	 * @param httpServletResponse the http servlet response
	 * @param jsonObject          the json object
	 * @return the response entity
	 */
	@PostMapping(value = "/v1")
	public ResponseEntity<BaseResponse> createVendorCredit(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, @RequestBody JSONObject<VendorCreditCreateRequest> jsonObject) {

		logger.info("Inside createVendorCredit");

		BaseResponse response = new VendorResponse();
		try {
			VendorCreditCreateVo vendorCreditCreateVo = ApConvertToVoHelper.getInstance()
					.convertToVendorCreditCreateVoFromRequest(jsonObject.getData());
			vendorCreditService.createVendorCreditNote(vendorCreditCreateVo);
			setTokens(response, httpServletRequest.getHeader("keyToken"), httpServletResponse.getHeader("valueToken"));
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_CREATED_VENDOR_CREDIT_NOTE,
					Constants.SUCCESS_CREATED_VENDOR_CREDIT_NOTE, Constants.VENDOR_CREDIT_CREATE_SUCCESSFULLY);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			logger.error("Error Payload:" + e.getMessage());
			if (e.getCause() != null) {
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_CREATED_VENDOR_CREDIT_NOTE,
						e.getCause().getMessage(), Constants.VENDOR_CREDIT_CREATE_UNSUCCESSFULLY);
			} else {
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_CREATED_VENDOR_CREDIT_NOTE,
						e.getMessage(), Constants.VENDOR_CREDIT_CREATE_UNSUCCESSFULLY);
			}

			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	/**
	 * Update vendor credit.
	 *
	 * @param httpServletRequest  the http servlet request
	 * @param httpServletResponse the http servlet response
	 * @param jsonObject          the json object
	 * @return the response entity
	 */
	@PutMapping(value = "/v1")
	public ResponseEntity<BaseResponse> updateVendorCredit(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, @RequestBody JSONObject<VendorCreditCreateRequest> jsonObject) {

		logger.info("Inside updateVendorCredit");

		BaseResponse response = new VendorResponse();
		try {
			VendorCreditCreateVo vendorCreditCreateVo = ApConvertToVoHelper.getInstance()
					.convertToVendorCreditCreateVoFromRequest(jsonObject.getData());
			vendorCreditService.updateVendorCreditNote(vendorCreditCreateVo);
			setTokens(response, httpServletRequest.getHeader("keyToken"), httpServletResponse.getHeader("valueToken"));
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_UPDATED_VENDOR_CREDIT_NOTE,
					Constants.SUCCESS_UPDATED_VENDOR_CREDIT_NOTE, Constants.VENDOR_CREDIT_UPDATE_SUCCESSFULLY);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			logger.error("Error Payload:" + e.getMessage());
			if (e.getCause() != null) {
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_UPDATED_VENDOR_CREDIT_NOTE,
						e.getCause().getMessage(), Constants.VENDOR_CREDIT_UPDATE_UNSUCCESSFULLY);
			} else {
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_UPDATED_VENDOR_CREDIT_NOTE,
						e.getMessage(), Constants.VENDOR_CREDIT_UPDATE_UNSUCCESSFULLY);
			}

			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

//	@GetM̥apping(value = "/v1/{vendorCreditId}")

	@GetMapping(value = "/v1/dropsdowns/{organizationId}")
	public ResponseEntity<BaseResponse> getVendorCreditDropdown(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, @PathVariable int organizationId) {
		logger.info("Entry into getCustomerDropDown");
		BaseResponse response = new CreditNoteDropDownResponse();
		try {
			VendorCreditNoteDropDownVo data = vendorCreditService.getVendorCreditNoteDropDownData(organizationId);
			setTokens(response, httpServletRequest.getHeader("keyToken"), httpServletRequest.getHeader("valueToken"));
			((CreditNoteDropDownResponse) response).setData(data);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_DROP_DOWN_FETCH,
					Constants.SUCCESS_DROP_DOWN_FETCH, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<>(response, HttpStatus.OK);

		} catch (Exception e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_BILLS_INVOICE_FETCH,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	/**
	 * Gets the vendor credit by id.
	 *
	 * @param httpServletRequest  the http servlet request
	 * @param httpServletResponse the http servlet response
	 * @param vendorCreditId      the vendor credit id
	 * @return the vendor credit by id
	 */
	@GetMapping(value = "/v1/{vendorCreditId}")
	public ResponseEntity<BaseResponse> getVendorCreditById(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, @PathVariable int vendorCreditId) {
		logger.info("Entry into getCustomerDropDown");
		BaseResponse response = new VendorCreditResponse();
		try {
			VendorCreditCreateVo data = vendorCreditService.getVendorCreditById(vendorCreditId);
			setTokens(response, httpServletRequest.getHeader("keyToken"), httpServletRequest.getHeader("valueToken"));
			((VendorCreditResponse) response).setData(data);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_FETCH_VENDOR_CREDIT_NOTE,
					Constants.SUCCESS_FETCH_VENDOR_CREDIT_NOTE, Constants.VENDOR_CREDIT_NOTE_GET_SUCCESSFULLY);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<>(response, HttpStatus.OK);

		} catch (Exception e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_FETCH_VENDOR_CREDIT_NOTE,
					e.getMessage(), Constants.VENDOR_CREDIT_NOTE_GET_UNSUCCESSFULLY);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PostMapping("/v1/{vendorCreditId}/applyfunds")
	public ResponseEntity<BaseResponse> applyFunds(HttpServletRequest httpRequest, HttpServletResponse httpResponse,
			@RequestBody JSONObject<List<ApplyFundCreateRequest>> applyFundRequests, @PathVariable int vendorCreditId)
			throws ApplicationException, SQLException {
		logger.info("Inside ApplyFunds");
		BaseResponse response = new VendorResponse();

		try {
			logger.info("Request Payload:" + generateRequestAndResponseLogPaylod(applyFundRequests));

			List<ApplyFundVo> applyFundVos = ApConvertToVoHelper.getInstance()
					.convertVendorCreditVoFromRequest(applyFundRequests.getData());

			vendorCreditService.applyFunds(applyFundVos, vendorCreditId);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_APPLY_FUND_VENDOR_CREDIT_NOTE,
					Constants.SUCCESS_APPLY_FUND_VENDOR_CREDIT_NOTE,
					Constants.APPLY_FUND_FOR_VENDOR_CREDIT_SUCCESSFULLY);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			logger.error("Error Payload:" + e.getMessage());
			if (e.getCause() != null) {
				response = constructResponse(response, Constants.FAILURE,
						Constants.FAILURE_APPLY_FUND_VENDOR_CREDIT_NOTE, e.getCause().getMessage(),
						Constants.APPLY_FUND_FOR_VENDOR_CREDIT_UNSUCCESSFULLY);
			} else {
				response = constructResponse(response, Constants.FAILURE,
						Constants.FAILURE_APPLY_FUND_VENDOR_CREDIT_NOTE, e.getMessage(),
						Constants.APPLY_FUND_FOR_VENDOR_CREDIT_UNSUCCESSFULLY);
			}

			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

}

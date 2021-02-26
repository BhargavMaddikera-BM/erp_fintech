package com.blackstrawai.controller.ar;

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
import com.blackstrawai.ar.ReceiptService;
import com.blackstrawai.ar.dropdowns.BasicInvoicesDropdownVo;
import com.blackstrawai.ar.dropdowns.ReceiptBulkDropdownVo;
import com.blackstrawai.ar.dropdowns.ReceiptDropdownVo;
import com.blackstrawai.ar.receipt.ReceiptVo;
import com.blackstrawai.common.BaseController;
import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.common.Constants;
import com.blackstrawai.helper.ArConvertToVoHelper;
import com.blackstrawai.keycontact.dropdowns.BasicVendorInvoiceDropDownVo;
import com.blackstrawai.request.JSONObject;
import com.blackstrawai.request.ar.receipt.ReceiptRequest;
import com.blackstrawai.response.ar.receipt.BasicVendorInvoiceDropDownResponse;
import com.blackstrawai.response.ar.receipt.ListReceiptBulkDropdownResponse;
import com.blackstrawai.response.ar.receipt.ListReceiptResponse;
import com.blackstrawai.response.ar.receipt.ReceiptDropDownResponse;
import com.blackstrawai.response.ar.receipt.ReceiptResponse;
import com.blackstrawai.response.ar.refund.BasicInvoicesDropdownResponse;

@RestController
@CrossOrigin
//This will be part of accountsreceivable. Hence ar.
@RequestMapping("/decifer/ar/receipt")
public class ReceiptController extends BaseController {

	@Autowired
	ReceiptService receiptService;

	private Logger logger = Logger.getLogger(ReceiptController.class);

	// For Creating Receipt
	@RequestMapping(value = "/v1/receipts", method = RequestMethod.POST)
	public ResponseEntity<BaseResponse> createReceipt(HttpServletRequest httpRequest, HttpServletResponse httpResponse,
			@RequestBody JSONObject<ReceiptRequest> receiptRequest) {
		logger.info("Entry into method: createReceipt");
		BaseResponse response = new ReceiptResponse();
		try {
			logger.info("Request Payload:" + generateRequestAndResponseLogPaylod(receiptRequest));
			ReceiptVo receiptVo = ArConvertToVoHelper.getInstance()
					.convertReceiptVoFromReceiptRequest(receiptRequest.getData());
			receiptVo = receiptService.createReceipt(receiptVo);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));

			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_RECEIPT_CREATED,
					Constants.SUCCESS_RECEIPT_CREATED, Constants.RECEIPT_CREATED_SUCCESSFULLY);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);

		} catch (ApplicationException e) {
			if (e.getCause() != null) {
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_RECEIPT_CREATED,
						e.getCause().getMessage(), Constants.RECEIPT_CREATED_UNSUCCESSFULLY);
			} else {
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_RECEIPT_CREATED,
						e.getMessage(), Constants.RECEIPT_CREATED_UNSUCCESSFULLY);
			}
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// For Fetching all Receipt details
	@RequestMapping(value = "/v1/receipts/{organizationId}/{organizationName}/{userId}/{roleName}", method = RequestMethod.GET)
	public ResponseEntity<BaseResponse> getAllReceiptsOfAnOrganizationForUserAndRole(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @PathVariable int organizationId, @PathVariable String organizationName,
			@PathVariable String userId, @PathVariable String roleName) {
		logger.info("Entry into method:getAllReceiptsOfAnOrganization");
		BaseResponse response = new ListReceiptResponse();
		try {
			List<ReceiptVo> listAllReceipts = receiptService
					.getAllReceiptsOfAnOrganizationForUserAndRole(organizationId, userId, roleName);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((ListReceiptResponse) response).setData(listAllReceipts);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_RECEIPT_FETCH,
					Constants.SUCCESS_RECEIPT_FETCH, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_RECEIPT_FETCH, e.getMessage(),
					Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/v1/receipts", method = RequestMethod.PUT)
	public ResponseEntity<BaseResponse> updateReceipt(HttpServletRequest httpRequest, HttpServletResponse httpResponse,
			@RequestBody JSONObject<ReceiptRequest> receiptRequest) {
		logger.info("Entry into method:updateReceipt");
		BaseResponse response = new ReceiptResponse();
		try {
			logger.info("Request Payload:" + generateRequestAndResponseLogPaylod(receiptRequest));
			ReceiptVo receiptVo = ArConvertToVoHelper.getInstance()
					.convertReceiptVoFromReceiptRequest(receiptRequest.getData());
			receiptVo = receiptService.updateReceipt(receiptVo);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_RECEIPT_UPDATED,
					Constants.SUCCESS_RECEIPT_UPDATED, Constants.RECEIPT_UPDATED_SUCCESSFULLY);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_RECEIPT_UPDATED, e.getMessage(),
					Constants.RECEIPT_UPDATED_UNSUCCESSFULLY);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// For Fetching Receipt by Id
	@RequestMapping(value = "/v1/receipts/{organizationId}/{organizationName}/{userId}/{roleName}/{id}", method = RequestMethod.GET)
	public ResponseEntity<BaseResponse> getReceiptById(HttpServletRequest httpRequest, HttpServletResponse httpResponse,
			@PathVariable int organizationId, @PathVariable String organizationName, @PathVariable int userId,
			@PathVariable String roleName, @PathVariable int id) {
		logger.info("Entry into method:getReceiptById");
		BaseResponse response = new ReceiptResponse();
		try {
			ReceiptVo receiptVo = receiptService.getReceiptById(id);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((ReceiptResponse) response).setData(receiptVo);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_RECEIPT_FETCH,
					Constants.SUCCESS_RECEIPT_FETCH, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_RECEIPT_FETCH, e.getMessage(),
					Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// For Fetching Refund Dropdowns by orgId
	@RequestMapping(value = "/v1/dropdowns/receipts/{organizationId}", method = RequestMethod.GET)
	public ResponseEntity<BaseResponse> getReceiptDropDownData(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @PathVariable int organizationId) {
		logger.info("Entry into method: getReceiptDropDownData");
		BaseResponse response = new ReceiptDropDownResponse();
		try {
			ReceiptDropdownVo receiptVo = receiptService.getReceiptDropdownData(organizationId);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((ReceiptDropDownResponse) response).setData(receiptVo);
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

	@RequestMapping(value = "/v1/dropsdowns/customer/customers/{organizationId}/{organizationName}/{userId}/{roleName}/{currencyId}/{customerId}/{receiptId}", method = RequestMethod.GET)
	public ResponseEntity<BaseResponse> getReceiptInvoicesForCustomer(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @PathVariable int organizationId, @PathVariable String organizationName,
			@PathVariable String userId, @PathVariable String roleName, @PathVariable int currencyId,
			@PathVariable int customerId, @PathVariable int receiptId) {
		logger.info("Entry into method: getReceiptInvoicesForCustomer");
		BaseResponse response = new BasicInvoicesDropdownResponse();
		try {
			BasicInvoicesDropdownVo receiptVo = receiptService.getReceiptInvoicesForCustomer(organizationId, currencyId,
					customerId, receiptId);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((BasicInvoicesDropdownResponse) response).setData(receiptVo);
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

	@RequestMapping(value = "/v1/dropsdowns/customer/customers/{organizationId}/{organizationName}/{userId}/{roleName}/{customerId}", method = RequestMethod.GET)
	public ResponseEntity<BaseResponse> getDueBalancesForCustomer(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @PathVariable int organizationId, @PathVariable String organizationName,
			@PathVariable String userId, @PathVariable String roleName, @PathVariable int customerId) {
		logger.info("Entry into method: getDueBalancesForCustomer");
		BaseResponse response = new BasicInvoicesDropdownResponse();
		try {
			BasicInvoicesDropdownVo receiptVo = receiptService.getDueBalancesForCustomer(organizationId, customerId);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((BasicInvoicesDropdownResponse) response).setData(receiptVo);
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

	@RequestMapping(value = "/v1/dropsdowns/customer/bulk_receipt_data/{organizationId}/{organizationName}/{userId}/{roleName}/{currencyId}/{customerId}/{receiptId}", method = RequestMethod.GET)
	public ResponseEntity<BaseResponse> getBulkReceiptDataForCustomer(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @PathVariable int organizationId, @PathVariable String organizationName,
			@PathVariable String userId, @PathVariable String roleName, @PathVariable int currencyId,
			@PathVariable int customerId, @PathVariable int receiptId) {
		logger.info("Entry into method: getBulkReceiptDataForCustomer");
		BaseResponse response = new ListReceiptBulkDropdownResponse();
		try {
			List<ReceiptBulkDropdownVo> receiptVo = receiptService.getBulkReceiptDataForCustomer(organizationId,
					currencyId, customerId, receiptId);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((ListReceiptBulkDropdownResponse) response).setData(receiptVo);
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

	@RequestMapping(value = "/v1/dropsdowns/vendor/vendors/{organizationId}/{organizationName}/{userId}/{roleName}/{currencyId}/{vendorId}/{receiptId}", method = RequestMethod.GET)
	public ResponseEntity<BaseResponse> getReceiptInvoicesForVendor(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @PathVariable int organizationId, @PathVariable String organizationName,
			@PathVariable String userId, @PathVariable String roleName, @PathVariable int currencyId,
			@PathVariable int vendorId, @PathVariable int receiptId) {
		logger.info("Entry into method: getReceiptInvoicesForVendor");
		BaseResponse response = new BasicVendorInvoiceDropDownResponse();
		try {
			BasicVendorInvoiceDropDownVo receiptVo = receiptService.getReceiptInvoicesForVendor(organizationId,
					currencyId, vendorId);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((BasicVendorInvoiceDropDownResponse) response).setData(receiptVo);
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

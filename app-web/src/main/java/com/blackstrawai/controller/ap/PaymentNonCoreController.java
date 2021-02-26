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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.ap.PaymentNonCoreConstants;
import com.blackstrawai.ap.PaymentNonCoreService;
import com.blackstrawai.ap.dropdowns.PaymentNonCoreDropDownVo;
import com.blackstrawai.ap.dropdowns.PaymentTypeVo;
import com.blackstrawai.ap.payment.noncore.BillsPayableVo;
import com.blackstrawai.ap.payment.noncore.ListPaymentNonCoreVo;
import com.blackstrawai.ap.payment.noncore.MultiLevelInvoiceVo;
import com.blackstrawai.ap.payment.noncore.PaymentAdviceVo;
import com.blackstrawai.ap.payment.noncore.PaymentNonCoreBillAndCreditDetailsVo;
import com.blackstrawai.ap.payment.noncore.PaymentNonCoreVo;
import com.blackstrawai.ap.payment.noncore.PayrollDueVo;
import com.blackstrawai.ap.purchaseorder.PoReferenceNumberVo;
import com.blackstrawai.ar.applycredits.InvoiceDetailsVo;
import com.blackstrawai.common.BaseController;
import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.common.Constants;
import com.blackstrawai.helper.ApConvertToVoHelper;
import com.blackstrawai.payroll.payrun.PayRunEmployeeAmountVo;
import com.blackstrawai.request.JSONObject;
import com.blackstrawai.request.ap.payment.noncore.PaymentNonCoreRequest;
import com.blackstrawai.response.ap.PaymentAdviceResponse;
import com.blackstrawai.response.ap.PaymentNonCoreBillsPayable;
import com.blackstrawai.response.ap.PaymentNonCoreInvoiceListResponse;
import com.blackstrawai.response.ap.PaymentNonCoreOnAccountAdvancePaymentResponse;
import com.blackstrawai.response.ap.PaymentNonCorePayrollDueResponse;
import com.blackstrawai.response.ap.PaymentNonCoreTypeResponse;
import com.blackstrawai.response.ap.payment.noncore.ListPaymentNonCoreResponse;
import com.blackstrawai.response.ap.payment.noncore.PaymentNonCoreBillResponse;
import com.blackstrawai.response.ap.payment.noncore.PaymentNonCoreCustomerResponse;
import com.blackstrawai.response.ap.payment.noncore.PaymentNonCoreDropDownResponse;
import com.blackstrawai.response.ap.payment.noncore.PaymentNonCoreEmployeePayRunResponse;
import com.blackstrawai.response.ap.payment.noncore.PaymentNonCoreResponse;
import com.blackstrawai.response.ap.payment.noncore.PaymentNonCoreVendorResponse;

@RestController
@CrossOrigin
@RequestMapping("/decifer/non_core/payment")
public class PaymentNonCoreController extends BaseController {

	@Autowired
	PaymentNonCoreService paymentService;
	private Logger logger = Logger.getLogger(PaymentNonCoreController.class);

	// Create new payment
	@RequestMapping(value = "/v1/payments", method = RequestMethod.POST)
	public ResponseEntity<BaseResponse> createPayment(HttpServletRequest httpRequest, HttpServletResponse httpResponse,
			@RequestBody JSONObject<PaymentNonCoreRequest> newPaymentRequest) throws SQLException {
		logger.info("Entry into method: createPayment");
		BaseResponse response = new PaymentNonCoreResponse();
		try {
			logger.info("Request Payload:" + generateRequestAndResponseLogPaylod(newPaymentRequest));
			PaymentNonCoreVo paymentVo = ApConvertToVoHelper.getInstance()
					.convertPaymentNonCoreVoFromPaymentRequest(newPaymentRequest.getData());
			paymentVo = paymentService.createPayment(paymentVo);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));

			response = constructResponse(response, Constants.SUCCESS, Constants.NON_CORE_PAYMENT_CREATED_SUCCESSFULLY,
					Constants.NON_CORE_PAYMENT_CREATED_SUCCESSFULLY, Constants.RECEIPT_CREATED_SUCCESSFULLY);
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

	// For Listing all Payment details of an organization
	@RequestMapping(value = "/v1/payments/{organizationId}/{organizationName}/{userId}/{roleName}", method = RequestMethod.GET)
	public ResponseEntity<BaseResponse> getAllPaymentsOfAnOrganizationForUserAndRole(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @PathVariable int organizationId, @PathVariable String organizationName,
			@PathVariable String userId, @PathVariable String roleName) throws SQLException {
		logger.info("Entry into method:getAllPaymentsOfAnOrganization");
		BaseResponse response = new ListPaymentNonCoreResponse();
		try {
			List<ListPaymentNonCoreVo> listAllPayments = paymentService
					.getAllPaymentsOfAnOrganizationForUserAndRole(organizationId, userId, roleName);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((ListPaymentNonCoreResponse) response).setData(listAllPayments);
			response = constructResponse(response, Constants.SUCCESS, Constants.NON_CORE_PAYMENT_FETCH_SUCCESS,
					Constants.NON_CORE_PAYMENT_FETCH_SUCCESS, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_NON_CORE_PAYMENT_FETCH,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// For fetching Dropdowns by orgId
	@RequestMapping(value = "/v1/payments/dropdowns/{organizationId}", method = RequestMethod.GET)
	public ResponseEntity<BaseResponse> getPaymentsDropDownData(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @PathVariable int organizationId) {
		logger.info("Entry into method: getPaymentsDropDownData");
		BaseResponse response = new PaymentNonCoreDropDownResponse();
		try {
			PaymentNonCoreDropDownVo paymentVo = paymentService.getPaymentDropdownData(organizationId);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((PaymentNonCoreDropDownResponse) response).setData(paymentVo);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_DROP_DOWN_FETCH,
					Constants.SUCCESS_DROP_DOWN_FETCH, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException | SQLException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_DROP_DOWN_FETCH, e.getMessage(),
					Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// Fetch payment by id
	@RequestMapping(value = "/v1/payments/{organizationId}/{organizationName}/{userId}/{roleName}/{id}", method = RequestMethod.GET)
	public ResponseEntity<BaseResponse> getPaymentById(HttpServletRequest httpRequest, HttpServletResponse httpResponse,
			@PathVariable int organizationId, @PathVariable String organizationName, @PathVariable String userId,
			@PathVariable String roleName, @PathVariable int id) {
		logger.info("Entry into getPaymentById");
		BaseResponse response = new PaymentNonCoreResponse();
		try {
			PaymentNonCoreVo paymentVo = paymentService.getPaymentById(organizationId, userId, roleName, id);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((PaymentNonCoreResponse) response).setData(paymentVo);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_NON_CORE_PAYMENT_FETCH,
					Constants.SUCCESS_NON_CORE_PAYMENT_FETCH, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_PAYMENT_NON_CORE_FETCH,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// Fetch poRefNo by vendor id and org ID
	@RequestMapping(value = "/v1/payments/vendor/vendors/{organizationId}/{organizationName}/{userId}/{roleName}/{id}", method = RequestMethod.GET)
	public ResponseEntity<BaseResponse> getVendorReferenceNumber(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @PathVariable int organizationId, @PathVariable String organizationName,
			@PathVariable String userId, @PathVariable String roleName, @PathVariable int id) {
		logger.info("Entry into getVendorReferenceNumber");
		BaseResponse response = new PaymentNonCoreVendorResponse();
		try {
			List<PoReferenceNumberVo> data = paymentService.getVendorReferenceNumber(organizationId, id);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((PaymentNonCoreVendorResponse) response).setData(data);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_NON_CORE_PAYMENT_FETCH,
					Constants.SUCCESS_NON_CORE_PAYMENT_FETCH, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_PAYMENT_NON_CORE_FETCH,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// Fetch bill and credit reference by orgId and vendorId
	@RequestMapping(value = "/v1/payments/bill/bills/{organizationId}/{organizationName}/{userId}/{roleName}/{id}/{currencyId}/{paymentId}", method = RequestMethod.GET)
	public ResponseEntity<BaseResponse> getBillReferenceNumBer(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @PathVariable int organizationId, @PathVariable String organizationName,
			@PathVariable String userId, @PathVariable String roleName, @PathVariable int id,
			@PathVariable int currencyId, @PathVariable int paymentId) {
		logger.info("Entry into getBillReferenceNumBer");
		BaseResponse response = new PaymentNonCoreBillResponse();
		try {
			PaymentNonCoreBillAndCreditDetailsVo data = paymentService.getBillReferenceNumber(organizationId, id,
					currencyId, paymentId);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((PaymentNonCoreBillResponse) response).setData(data);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_NON_CORE_PAYMENT_FETCH,
					Constants.SUCCESS_NON_CORE_PAYMENT_FETCH, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_PAYMENT_NON_CORE_FETCH,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// Update payment
	@RequestMapping(value = "/v1/payments", method = RequestMethod.PUT)
	public ResponseEntity<BaseResponse> updatePayment(HttpServletRequest httpRequest, HttpServletResponse httpResponse,
			@RequestBody JSONObject<PaymentNonCoreRequest> paymentRequest) throws SQLException {
		logger.info("Entry into updatePayment");
		BaseResponse response = new PaymentNonCoreResponse();
		try {
			logger.info("Request Payload:" + generateRequestAndResponseLogPaylod(paymentRequest));
			PaymentNonCoreVo paymentVo = ApConvertToVoHelper.getInstance()
					.convertPaymentNonCoreVoFromPaymentRequest(paymentRequest.getData());
			paymentVo = paymentService.updatePayment(paymentVo);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_NON_CORE_PAYMENT_UPDATED,
					Constants.SUCCESS_NON_CORE_PAYMENT_UPDATED, Constants.SUCCESS_NON_CORE_PAYMENT_UPDATED);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_PAYMENT_NON_CORE_UPDATE,
					e.getMessage(), Constants.FAILURE_DURING_UPDATE);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// Fetch receipts and credit notes by customer id and org ID
	@RequestMapping(value = "/v1/payments/customer/customers/{organizationId}/{organizationName}/{userId}/{roleName}/{customerId}/{currencyId}/{invoiceId}/{paymentId}", method = RequestMethod.GET)
	public ResponseEntity<BaseResponse> getInvoiceDetails(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @PathVariable int organizationId, @PathVariable String organizationName,
			@PathVariable String userId, @PathVariable String roleName, @PathVariable int customerId,
			@PathVariable int currencyId, @PathVariable int invoiceId, @PathVariable int paymentId) {
		logger.info("Entry into getInvoiceDetails");
		BaseResponse response = new PaymentNonCoreCustomerResponse();
		try {
			List<MultiLevelInvoiceVo> data = paymentService.getInvoiceDetails(organizationId, customerId, currencyId,
					invoiceId, paymentId);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((PaymentNonCoreCustomerResponse) response).setData(data);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_NON_CORE_PAYMENT_FETCH,
					Constants.SUCCESS_NON_CORE_PAYMENT_FETCH, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_PAYMENT_NON_CORE_FETCH,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// For Listing all Customer Refunds of an organization
	@RequestMapping(value = "/v1/payments/refund/refunds/{organizationId}/{organizationName}/{userId}/{roleName}", method = RequestMethod.GET)
	public ResponseEntity<BaseResponse> getAllCustomerRefundsOfAnOrganizationForUserAndRole(
			HttpServletRequest httpRequest, HttpServletResponse httpResponse, @PathVariable int organizationId,
			@PathVariable String organizationName, @PathVariable String userId, @PathVariable String roleName) {
		logger.info("Entry into method:getAllPaymentsOfAnOrganization");
		BaseResponse response = new ListPaymentNonCoreResponse();
		try {
			List<ListPaymentNonCoreVo> listAllPayments = paymentService.getAllPaymentsOfAnOrganizationForUserAndRole(
					organizationId, userId, roleName, PaymentNonCoreConstants.CUSTOMER_REFUNDS);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((ListPaymentNonCoreResponse) response).setData(listAllPayments);
			response = constructResponse(response, Constants.SUCCESS, Constants.NON_CORE_PAYMENT_FETCH_SUCCESS,
					Constants.NON_CORE_PAYMENT_FETCH_SUCCESS, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_NON_CORE_PAYMENT_FETCH,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// Fetch invoice list by customer id and org ID
	@RequestMapping(value = "/v1/payments/customer/invoices/{organizationId}/{organizationName}/{userId}/{roleName}/{customerId}/{currencyId}/{paymentId}", method = RequestMethod.GET)
	public ResponseEntity<BaseResponse> getInvoiceList(HttpServletRequest httpRequest, HttpServletResponse httpResponse,
			@PathVariable int organizationId, @PathVariable String organizationName, @PathVariable String userId,
			@PathVariable String roleName, @PathVariable int customerId, @PathVariable int currencyId,
			@PathVariable int paymentId) {
		logger.info("Entry into getInvoiceList");
		BaseResponse response = new PaymentNonCoreInvoiceListResponse();
		try {
			List<InvoiceDetailsVo> data = paymentService.getInvoiceList(organizationId, customerId, currencyId,
					paymentId);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((PaymentNonCoreInvoiceListResponse) response).setData(data);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_NON_CORE_PAYMENT_FETCH,
					Constants.SUCCESS_NON_CORE_PAYMENT_FETCH, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_PAYMENT_NON_CORE_FETCH,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// Fetch on Account list by customer id and org ID
	@RequestMapping(value = "/v1/payments/customer/on_account/{organizationId}/{organizationName}/{userId}/{roleName}/{customerId}/{currencyId}/{paymentId}", method = RequestMethod.GET)
	public ResponseEntity<BaseResponse> getOnAccountList(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @PathVariable int organizationId, @PathVariable String organizationName,
			@PathVariable String userId, @PathVariable String roleName, @PathVariable int customerId,
			@PathVariable int currencyId, @PathVariable int paymentId) {
		logger.info("Entry into getOnAccountList");
		BaseResponse response = new PaymentNonCoreOnAccountAdvancePaymentResponse();
		try {
			List<MultiLevelInvoiceVo> data = paymentService.getOnAccount(organizationId, customerId, currencyId,
					paymentId);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((PaymentNonCoreOnAccountAdvancePaymentResponse) response).setData(data);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_NON_CORE_PAYMENT_FETCH,
					Constants.SUCCESS_NON_CORE_PAYMENT_FETCH, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_PAYMENT_NON_CORE_FETCH,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// Fetch advance payments list by customer id and org ID
	@RequestMapping(value = "/v1/payments/customer/advance_payment/{organizationId}/{organizationName}/{userId}/{roleName}/{customerId}/{currencyId}/{paymentId}", method = RequestMethod.GET)
	public ResponseEntity<BaseResponse> getAdvancePayments(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @PathVariable int organizationId, @PathVariable String organizationName,
			@PathVariable String userId, @PathVariable String roleName, @PathVariable int customerId,
			@PathVariable int currencyId, @PathVariable int paymentId) {
		logger.info("Entry into getAdvancePayments");
		BaseResponse response = new PaymentNonCoreOnAccountAdvancePaymentResponse();
		try {
			List<MultiLevelInvoiceVo> data = paymentService.getAdvancePayments(organizationId, customerId, currencyId,
					paymentId);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((PaymentNonCoreOnAccountAdvancePaymentResponse) response).setData(data);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_NON_CORE_PAYMENT_FETCH,
					Constants.SUCCESS_NON_CORE_PAYMENT_FETCH, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_PAYMENT_NON_CORE_FETCH,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// Fetch payrun by employee ID
	@RequestMapping(value = "/v1/payments/employee/payruns/{organizationId}/{organizationName}/{userId}/{roleName}/{employeeId}", method = RequestMethod.GET)
	public ResponseEntity<BaseResponse> getPayrunByEmployeeId(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @PathVariable int organizationId, @PathVariable String organizationName,
			@PathVariable String userId, @PathVariable String roleName, @PathVariable int employeeId) {
		logger.info("Entry into getPayRunByEmployeeId");
		BaseResponse response = new PaymentNonCoreEmployeePayRunResponse();
		try {
			List<PayRunEmployeeAmountVo> data = paymentService.getPayRunByEmployeeId(organizationId, employeeId);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((PaymentNonCoreEmployeePayRunResponse) response).setData(data);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_NON_CORE_PAYMENT_FETCH,
					Constants.SUCCESS_NON_CORE_PAYMENT_FETCH, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_PAYMENT_NON_CORE_FETCH,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// Fetch employees by payRun ID
	@RequestMapping(value = "/v1/payments/employee/employees/{organizationId}/{organizationName}/{userId}/{roleName}/{payrunId}", method = RequestMethod.GET)
	public ResponseEntity<BaseResponse> getEmployeesByPayrunId(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @PathVariable int organizationId, @PathVariable String organizationName,
			@PathVariable String userId, @PathVariable String roleName, @PathVariable int payrunId) {
		logger.info("Entry into getPayRunByEmployeeId");
		BaseResponse response = new PaymentNonCoreEmployeePayRunResponse();
		try {
			List<PayRunEmployeeAmountVo> data = paymentService.getEmployeesByPayrunId(organizationId, payrunId);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((PaymentNonCoreEmployeePayRunResponse) response).setData(data);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_NON_CORE_PAYMENT_FETCH,
					Constants.SUCCESS_NON_CORE_PAYMENT_FETCH, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_PAYMENT_NON_CORE_FETCH,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// Fetch bills payable for landing page of Payments Screen
	@RequestMapping(value = "/v1/payments/bills_payable/{organizationId}/{userId}/{roleName}", method = RequestMethod.GET)
	public ResponseEntity<BaseResponse> getBillsPayable(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @PathVariable int organizationId, @PathVariable String userId,
			@PathVariable String roleName) {
		logger.info("Entry into getBillsPayable");
		BaseResponse response = new PaymentNonCoreBillsPayable();
		try {
			BillsPayableVo data = paymentService.getBillsPayable(organizationId, userId, roleName);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((PaymentNonCoreBillsPayable) response).setData(data);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_BILLS_PAYABLE,
					Constants.SUCCESS_BILLS_PAYABLE, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_BILLS_PAYABLE, e.getMessage(),
					Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// Fetch Payroll Due for landing page of Payments Screen
	@RequestMapping(value = "/v1/payments/payroll/{organizationId}/{userId}/{roleName}", method = RequestMethod.GET)
	public ResponseEntity<BaseResponse> getPayrollDue(HttpServletRequest httpRequest, HttpServletResponse httpResponse,
			@PathVariable int organizationId, @PathVariable String userId, @PathVariable String roleName) {
		logger.info("Entry into getPayrollDue");
		BaseResponse response = new PaymentNonCorePayrollDueResponse();
		try {
			PayrollDueVo data = paymentService.getPayrollDue(organizationId, userId, roleName);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((PaymentNonCorePayrollDueResponse) response).setData(data);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_PAYROLL_PAYABLE,
					Constants.SUCCESS_PAYROLL_PAYABLE, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_PAYROLL_PAYABLE, e.getMessage(),
					Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// Fetch payment types for menu of landing page of Payments Screen
	@RequestMapping(value = "/v1/payments/payment_types", method = RequestMethod.GET)
	public ResponseEntity<BaseResponse> getPaymentTypeDropdown(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse) throws SQLException {
		logger.info("Entry into getPaymentTypeDropdown");
		BaseResponse response = new PaymentNonCoreTypeResponse();
		try {
			List<PaymentTypeVo> paymentTypes = paymentService.getPaymentTypes();
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((PaymentNonCoreTypeResponse) response).setData(paymentTypes);
			response = constructResponse(response, Constants.SUCCESS, Constants.NON_CORE_PAYMENT_FETCH_SUCCESS,
					Constants.NON_CORE_PAYMENT_FETCH_SUCCESS, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_NON_CORE_PAYMENT_FETCH,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// Fetch paymants of each type for landing page of Payments Screen
	@RequestMapping(value = "/v1/payments/type/{organizationId}/{userId}/{roleName}/{type}", method = RequestMethod.GET)
	public ResponseEntity<BaseResponse> getPaymentsByType(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @PathVariable int organizationId, @PathVariable String userId,
			@PathVariable String roleName, @PathVariable int type) {
		logger.info("Entry into getPaymentsByType");
		BaseResponse response = new ListPaymentNonCoreResponse();
		try {
			List<ListPaymentNonCoreVo> listAllPayments = paymentService
					.getTop5PaymentsOfAnOrganizationForUserRoleType(organizationId, userId, roleName, type);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((ListPaymentNonCoreResponse) response).setData(listAllPayments);
			response = constructResponse(response, Constants.SUCCESS, Constants.NON_CORE_PAYMENT_FETCH_SUCCESS,
					Constants.NON_CORE_PAYMENT_FETCH_SUCCESS, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_NON_CORE_PAYMENT_FETCH,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	// Fetch payment advice for payment ID
	@RequestMapping(value = "/v1/payment_advice/{organizationId}/{userId}/{roleName}/{id}", method = RequestMethod.GET)
	public ResponseEntity<BaseResponse> getPaymentAdvice(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @PathVariable int organizationId, @PathVariable String userId,
			@PathVariable String roleName, @PathVariable int id) {
		logger.info("Entry into getPaymentAdvice");
		BaseResponse response = new PaymentAdviceResponse();
		try {
			PaymentAdviceVo paymentAdvice = paymentService
					.getPaymentAdvice(organizationId, userId, roleName, id);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((PaymentAdviceResponse) response).setData(paymentAdvice);
			response = constructResponse(response, Constants.SUCCESS, Constants.PAYMENT_ADVICE_FETCH_SUCCESS,
					Constants.PAYMENT_ADVICE_FETCH_SUCCESS, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.PAYMENT_ADVICE_FETCH_FAILURE,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}

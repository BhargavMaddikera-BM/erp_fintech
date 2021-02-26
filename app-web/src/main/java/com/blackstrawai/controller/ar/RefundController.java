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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.ap.PaymentNonCoreConstants;
import com.blackstrawai.ap.PaymentNonCoreService;
import com.blackstrawai.ap.payment.noncore.ListPaymentNonCoreVo;
import com.blackstrawai.ar.RefundService;
import com.blackstrawai.common.BaseController;
import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.common.Constants;
import com.blackstrawai.response.ap.payment.noncore.ListPaymentNonCoreResponse;

@RestController
@CrossOrigin
//This will be part of accountsreceivable. Hence ar.
@RequestMapping("/decifer/ar/refund")
public class RefundController extends BaseController {

	@Autowired
	RefundService refundService;
	@Autowired
	PaymentNonCoreService paymentService;

	private Logger logger = Logger.getLogger(RefundController.class);

	@RequestMapping(value = "/v1/payments/refund/refunds/{organizationId}/{organizationName}/{userId}/{roleName}", method = RequestMethod.GET)
	public ResponseEntity<BaseResponse> getAllCustomerRefundsOfAnOrganizationForUserAndRole(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @PathVariable int organizationId, @PathVariable String organizationName,
			@PathVariable String userId, @PathVariable String roleName) {
		logger.info("Entry into method:getAllCustomerRefundsOfAnOrganizationForUserAndRole");
		BaseResponse response = new ListPaymentNonCoreResponse();
		try {
			List<ListPaymentNonCoreVo> listAllPayments = paymentService
					.getAllPaymentsOfAnOrganizationForUserAndRole(organizationId, userId, roleName, PaymentNonCoreConstants.CUSTOMER_REFUNDS);
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

}

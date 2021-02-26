package com.blackstrawai.controller.externalintegration.banking.yesbank;

import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

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
import com.blackstrawai.common.BaseController;
import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.common.Constants;
import com.blackstrawai.externalintegration.banking.yesbank.ERPBulkPaymentService;
import com.blackstrawai.externalintegration.banking.yesbank.YesBankIntegrationService;
import com.blackstrawai.externalintegration.yesbank.YesBankCustomerInformationVo;
import com.blackstrawai.externalintegration.yesbank.Request.bulkpayments.BulkPaymentVo;
import com.blackstrawai.externalintegration.yesbank.Request.paymenttransfer.PaymentTransferVo;
import com.blackstrawai.externalintegration.yesbank.Response.bulkpayments.BulkPaymentsResponse;
import com.blackstrawai.externalintegration.yesbank.Response.payments.ErpSinglePaymentTransactionVo;
import com.blackstrawai.externalintegration.yesbank.Response.paymenttransfer.PaymentTransferResponseVo;
import com.blackstrawai.externalintegration.yesbank.dropdowns.APEmployeesPayRunDropDownVo;
import com.blackstrawai.externalintegration.yesbank.dropdowns.APVendorBillsDropDownVo;
import com.blackstrawai.externalintegration.yesbank.erpbulkpayments.BaseERPPaymentVo;
import com.blackstrawai.helper.ERPPaymentsHelperService;
import com.blackstrawai.request.JSONObject;
import com.blackstrawai.request.externalintegration.banking.yesbank.BaseERPPaymentRequest;
import com.blackstrawai.request.externalintegration.banking.yesbank.ErpSinglePaymentRequest;
import com.blackstrawai.response.externalintegration.banking.yesbank.APEmployeePayrunDropDownResponse;
import com.blackstrawai.response.externalintegration.banking.yesbank.APVendorBillsDropDownResponse;
import com.blackstrawai.response.externalintegration.banking.yesbank.BeneficiaryDropDownResponse;
<<<<<<< HEAD
=======
import com.blackstrawai.response.externalintegration.banking.yesbank.BulkPaymentsListResponse;
>>>>>>> 0a694d861a9a446c4b7c10a39cf5c7647a3014bd
import com.blackstrawai.response.externalintegration.banking.yesbank.ERPPaymentResponse;
import com.blackstrawai.response.externalintegration.banking.yesbank.TransferResponse;

@RestController
@CrossOrigin
@RequestMapping("/decifer/yesbank/erp_payments")
public class ERPBulkPaymentsController extends BaseController {

	private static final Logger logger = Logger.getLogger(ERPBulkPaymentsController.class);

	@Autowired
	private ERPBulkPaymentService erpBulkPaymentService;

	@Autowired
	private YesBankIntegrationService yesBankIntegrationService;
	
	@Autowired
	private ERPPaymentsHelperService paymentHelperService;
	
	@RequestMapping(value = {
			"v1/dropdown/bulk_invoice/{organizationId}/{organizationName}/{roleName}/{userId}/{currencyId}" }, method = RequestMethod.GET)
	public ResponseEntity<BaseResponse> viewBulkTransaction(HttpServletRequest httpRequest,
			@PathVariable("organizationId") Integer organizationId,
			@PathVariable("organizationName") String organizationName, @PathVariable("roleName") String roleName,
			@PathVariable("userId") String userId,@PathVariable("currencyId") String currencyId) {

		BaseResponse response = new APVendorBillsDropDownResponse();
		HttpStatus responseStatus;
		try {

			List<APVendorBillsDropDownVo> vendorBllsDetails = erpBulkPaymentService
					.getVendorsBillsAndBankDetails(organizationId, roleName, userId,currencyId);

			((APVendorBillsDropDownResponse) response).setData(vendorBllsDetails);
			responseStatus = HttpStatus.OK;
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_DROP_DOWN_FETCH,
					Constants.SUCCESS_DROP_DOWN_FETCH, Constants.SUCCESS_DURING_GET);

		} catch (Exception ex) {
			logger.error(":::Exception in getting the payment transaction:::" + ex);
			responseStatus = HttpStatus.INTERNAL_SERVER_ERROR;
			String errorMessage = ex.getCause() != null ? ex.getCause().getMessage() : ex.getMessage();

			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_DROP_DOWN_FETCH, errorMessage,
					Constants.YBL_PAYMENT_FETCH_UNSUCCESSFULLY);
		}
		return new ResponseEntity(response, responseStatus);
	}

	
	@RequestMapping(value = {"v1/dropdown/bulk_payrun/{organizationId}/{organizationName}/{roleName}/{userId}/{currencyId}" }, method = RequestMethod.GET)
public ResponseEntity<BaseResponse> getPayrunDropdowns(HttpServletRequest httpRequest,
	@PathVariable("organizationId") Integer organizationId,
	@PathVariable("organizationName") String organizationName, @PathVariable("roleName") String roleName,
	@PathVariable("userId") String userId,@PathVariable("currencyId") String currencyId) {

BaseResponse response = new APEmployeePayrunDropDownResponse();
HttpStatus responseStatus;
try {

	List<APEmployeesPayRunDropDownVo> employeeListDetails = erpBulkPaymentService
			.getEmployeePayRunAndBankDetails(organizationId, roleName, userId, currencyId);

	((APEmployeePayrunDropDownResponse) response).setData(employeeListDetails);
	responseStatus = HttpStatus.OK;
	response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_DROP_DOWN_FETCH,
			Constants.SUCCESS_DROP_DOWN_FETCH, Constants.SUCCESS_DURING_GET);

} catch (Exception ex) {
	logger.error(":::Exception in getting the payment transaction:::" + ex);
	responseStatus = HttpStatus.INTERNAL_SERVER_ERROR;
	String errorMessage = ex.getCause() != null ? ex.getCause().getMessage() : ex.getMessage();

	response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_DROP_DOWN_FETCH, errorMessage,
			Constants.YBL_PAYMENT_FETCH_UNSUCCESSFULLY);
}
return new ResponseEntity(response, responseStatus);
}

	
	//Drop down for payemnt setting 
	  @RequestMapping(value = "v1/dropdown/setting/{organizationId}/{organizationName}/{roleName}/{userId}", method = RequestMethod.GET)
	  public ResponseEntity<BaseResponse> paymentDropDown(
	      HttpServletRequest httpRequest, @PathVariable int organizationId,
	      @PathVariable("organizationName") String organizationName, @PathVariable("roleName") String roleName,
			@PathVariable("userId") String userId) {

	    BaseResponse response = new BeneficiaryDropDownResponse();
	    HttpStatus responseStatus;

	    try {

	      setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));

	      ((BeneficiaryDropDownResponse) response)
	          .setData(erpBulkPaymentService.getERPAccountDropDownList(organizationId));
	      responseStatus = HttpStatus.OK;
	      response =
	          constructResponse(
	              response,
	              Constants.SUCCESS,
	              Constants.SUCCESS_YBL_PAYMENT_MODE_LIST,
	              Constants.SUCCESS_YBL_PAYMENT_MODE_LIST,
	              Constants.SUCCESS_DURING_GET);

	    } catch (Exception ex) {
	      logger.error(":::Exception in getting the payment list:::" + ex);
	      responseStatus = HttpStatus.INTERNAL_SERVER_ERROR;
	      String errorMessage = ex.getCause() != null ? ex.getCause().getMessage() : ex.getMessage();

	      response =
	          constructResponse(
	              response,
	              Constants.FAILURE,
	              Constants.ERROR_YBL_PAYMENT_MODE_LIST,
	              errorMessage,
	              Constants.YBL_PAYMENT_FETCH_UNSUCCESSFULLY);
	    }

	    return new ResponseEntity(response, responseStatus);
	  }
	  
		// ERP payments transfer
		@RequestMapping(value = {"v1/bulk/payment_transfer" }, method = RequestMethod.POST)
		public ResponseEntity<BaseResponse> initiateBulkPayment(HttpServletRequest httpRequest,@RequestBody JSONObject<BaseERPPaymentRequest> paymentRequest) {

		    BaseResponse response = new TransferResponse();
		    HttpStatus responseStatus ;
		    try {
		    
		    	if(Objects.nonNull(paymentRequest) && Objects.nonNull(paymentRequest.getData()) ) {
		    	      double amount = paymentRequest.getData().getTotalAmount()!=null && !paymentRequest.getData().getTotalAmount().isEmpty()   ?
		    	    		  Double.parseDouble(paymentRequest.getData().getTotalAmount()) : 0;

		    	      if(Objects.isNull(paymentRequest.getData().getPaymentDetails()) || paymentRequest.getData().getPaymentDetails().isEmpty()){
		      	        throw new ApplicationException(Constants.ATLEAST_ONE_PAYMENT_IS_MANDATORY);
		      	      }
		    	      
		    	      if(amount <0){
		    	        throw new ApplicationException(Constants.ERROR_YBL_NEGATIVE_PAYMENT_TRANSFER);
		    	      }
		    	      
		    	      
		    	      BaseERPPaymentVo erpPaymentVo  = null;
		    	      YesBankCustomerInformationVo debtorDetails = null;
		    	      BulkPaymentVo bulkPaymentData  = null;
		    	    if(Objects.nonNull(paymentRequest.getData().getCustomerId()) && Objects.nonNull(paymentRequest.getData().getDebitAccountId())) {
		    	    	 debtorDetails = yesBankIntegrationService.getYesBankAccountDetails(paymentRequest.getData().getDebitAccountId() ,(paymentRequest.getData().getOrgId() ));
		    	    	 erpPaymentVo = paymentHelperService.convertRequestToVo(paymentRequest.getData() );
		    	    	 bulkPaymentData =paymentHelperService.convertERPBulkPaymentRequestToYesBankRequestFormat(erpPaymentVo, debtorDetails);
		    	    	 logger.info("<< payment Transfer request object>>" + bulkPaymentData);

		    	    }
		    	    BulkPaymentsResponse paymentTransferResponseVo= null;
		    if(debtorDetails!=null && bulkPaymentData!=null) {
		       paymentTransferResponseVo = erpBulkPaymentService.bulkPaymentTransfer(
		        		  bulkPaymentData,erpPaymentVo ,debtorDetails);
		    }
		    	
		 //   if (paymentTransferResponseVo!=null && Objects.isNull(paymentTransferResponseVo.getMeta())) {
		        response =
		            constructResponse(
		                response,
		                Constants.SUCCESS,
		                Constants.SUCCESS_YBL_PAYMENT_TRANSFER,
		                Constants.SUCCESS_YBL_PAYMENT_TRANSFER,
		                Constants.SUCCESS_DURING_GET);
		        /*   } else if (
		    		  paymentTransferResponseVo!=null && Objects.nonNull(paymentTransferResponseVo.getMeta())) {

		        response =
		            constructResponse(
		                response,
		                Constants.FAILURE,
		                Constants.ERROR_YBL_PAYMENT_TRANSFER,
		                Constants.ERROR_YBL_PAYMENT_TRANSFER,
		                Constants.FAILURE_DURING_GET);
		      } */
		    	}
		    	responseStatus = HttpStatus.OK;
		    } catch (Exception ex) {
		      logger.error("Exception in payment transfer" + ex);
		      response =
		          constructResponse(
		              response,
		              Constants.FAILURE,
		              Constants.ERROR_YBL_PAYMENT_TRANSFER,
		              ex.getCause() != null
		                  ? Constants.ERROR_YBL_PAYMENT_TRANSFER
		                  : ex.getMessage(),
		              Constants.FAILURE_DURING_GET);
		      responseStatus = HttpStatus.INTERNAL_SERVER_ERROR;
		    }
		    return new ResponseEntity<>(response, responseStatus);
		  }
		
		@RequestMapping(value = {
		"v1/dropdown/po/{organizationId}/{organizationName}/{roleName}/{userId}/{currencyId}" }, method = RequestMethod.GET)
public ResponseEntity<BaseResponse> purchaseDropDown(HttpServletRequest httpRequest,
		@PathVariable("organizationId") Integer organizationId,
		@PathVariable("organizationName") String organizationName, @PathVariable("roleName") String roleName,
		@PathVariable("userId") String userId,@PathVariable("currencyId") String currencyId) {

	BaseResponse response = new APVendorBillsDropDownResponse();
	HttpStatus responseStatus;
	try {

		List<APVendorBillsDropDownVo> vendorBllsDetails = erpBulkPaymentService
				.getPurchaseOrderAndBankDetails(organizationId, roleName, userId,currencyId);


		((APVendorBillsDropDownResponse) response).setData(vendorBllsDetails);
		responseStatus = HttpStatus.OK;
		response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_DROP_DOWN_FETCH,
				Constants.SUCCESS_DROP_DOWN_FETCH, Constants.SUCCESS_DURING_GET);

	} catch (Exception ex) {
		logger.error(":::Exception in getting the payment transaction:::" + ex);
		responseStatus = HttpStatus.INTERNAL_SERVER_ERROR;
		String errorMessage = ex.getCause() != null ? ex.getCause().getMessage() : ex.getMessage();

		response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_DROP_DOWN_FETCH, errorMessage,
				Constants.YBL_PAYMENT_FETCH_UNSUCCESSFULLY);
	}
	return new ResponseEntity(response, responseStatus);
}

		// ERP payments General Ledger
<<<<<<< HEAD
		@RequestMapping(value = {"/v1/general_ledger" }, method = RequestMethod.POST)
=======
		@RequestMapping(value = {"/v1/general_ledgers" }, method = RequestMethod.POST)
>>>>>>> 0a694d861a9a446c4b7c10a39cf5c7647a3014bd
		public ResponseEntity<BaseResponse> getGeneralLedger(HttpServletRequest httpRequest,@RequestBody JSONObject<BaseERPPaymentRequest> paymentRequest) {

		    BaseResponse response = new ERPPaymentResponse();
		    HttpStatus responseStatus ;
		    try {
				logger.info("Request Payload:"+generateRequestAndResponseLogPaylod(paymentRequest));	
		    	if(Objects.nonNull(paymentRequest) && Objects.nonNull(paymentRequest.getData()) ) {
		    	      double amount = paymentRequest.getData().getTotalAmount()!=null && !paymentRequest.getData().getTotalAmount().isEmpty()   ?
		    	    		  Double.parseDouble(paymentRequest.getData().getTotalAmount()) : 0;

		    	      if(Objects.isNull(paymentRequest.getData().getPaymentDetails()) || paymentRequest.getData().getPaymentDetails().isEmpty()){
		      	        throw new ApplicationException(Constants.ATLEAST_ONE_PAYMENT_IS_MANDATORY);
		      	      }
		    	      
		    	      if(amount <0){
		    	        throw new ApplicationException(Constants.ERROR_YBL_NEGATIVE_PAYMENT_TRANSFER);
		    	      }
		    	      
		    	      
		    	      BaseERPPaymentVo erpPaymentVo  = null;
		    	      YesBankCustomerInformationVo debtorDetails = null;
		    	      BulkPaymentVo bulkPaymentData  = null;
		    	    if(Objects.nonNull(paymentRequest.getData().getCustomerId()) && Objects.nonNull(paymentRequest.getData().getDebitAccountId())) {
		    	    	 debtorDetails = yesBankIntegrationService.getYesBankAccountDetails(paymentRequest.getData().getDebitAccountId() ,(paymentRequest.getData().getOrgId() ));
		    	    	 erpPaymentVo = paymentHelperService.convertRequestToVo(paymentRequest.getData() );
		    	    	// bulkPaymentData =paymentHelperService.convertERPBulkPaymentRequestToYesBankRequestFormat(erpPaymentVo, debtorDetails);
		    	    	 erpPaymentVo = erpBulkPaymentService.getGeneralLedger(erpPaymentVo,debtorDetails);
		    	    	 logger.info("<< payment Transfer request object>>" + erpPaymentVo);

		    	    }
		    	    ((ERPPaymentResponse) response).setData(erpPaymentVo);		    	
		 //   if (paymentTransferResponseVo!=null && Objects.isNull(paymentTransferResponseVo.getMeta())) {
		        response =
		            constructResponse(
		                response,
		                Constants.SUCCESS,
		                Constants.SUCCESS_DROP_DOWN_FETCH,
		                Constants.SUCCESS_DROP_DOWN_FETCH,
		                Constants.SUCCESS_DURING_GET);
		          }
		    
		    	responseStatus = HttpStatus.OK;
		    } catch (Exception ex) {
		      logger.error("Exception in payment transfer" + ex);
		      response =
		    		  constructResponse(
				                response,
				                Constants.FAILURE,
				                Constants.FAILURE_DROP_DOWN_FETCH,
				                ex.getCause() != null
				                  ? Constants.ERROR_YBL_PAYMENT_TRANSFER
				                  : ex.getMessage(),
				              Constants.FAILURE_DURING_GET
				              
				              );
		      
		         
		      responseStatus = HttpStatus.INTERNAL_SERVER_ERROR;
		    }
		    return new ResponseEntity<>(response, responseStatus);
		  }
		
<<<<<<< HEAD
        @RequestMapping(value = {"/v1/single/payment_transfer" }, method = RequestMethod.POST)
		  public ResponseEntity<BaseResponse> paymentTransfer(
		      HttpServletRequest httpRequest,
		      @RequestBody JSONObject<ErpSinglePaymentRequest> paymentSingleTransferRequest) {

		    BaseResponse response = new TransferResponse();
		    HttpStatus responseStatus;
		    try {

		      setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
		      
		      String transferType = paymentSingleTransferRequest.getData().getSinglePayment().getPaymentMode();
		      double amount =
		              Double.parseDouble(paymentSingleTransferRequest.getData().getSinglePayment().getAmount());

		      if(amount <0){
		        throw new ApplicationException(Constants.ERROR_YBL_NEGATIVE_PAYMENT_TRANSFER);
		      }

		      if (Constants.RTGS_PAYMENT_MODE.equalsIgnoreCase(transferType)) {
		        if (amount < 200000.00) {
		          throw new ApplicationException(Constants.ERROR_YBL_RTGS_PAYMENT_TRANSFER);
		        }
		      } else if (Constants.IMPS_PAYMENT_MODE.equalsIgnoreCase(transferType)) {
		        if (amount > 200000.00) {
		          throw new ApplicationException(Constants.ERROR_YBL_IMPS_PAYMENT_TRANSFER);
		        }
		      }
		      
		      PaymentTransferVo paymentTransferDomesticPaymentsVo =
		    		  paymentHelperService.convertPaymentTransferRequestToVo(paymentSingleTransferRequest.getData());

		      ErpSinglePaymentTransactionVo paymentSingleTransactionVo =
		    	  paymentHelperService.convertSingleRequestToVo(paymentSingleTransferRequest.getData());

		      
		          logger.info("<< payment Transfer request object>>" + paymentTransferDomesticPaymentsVo);
		          
		          PaymentTransferResponseVo paymentTransferResponseVo =
		        		  erpBulkPaymentService.paymentTransfer(
		                      paymentTransferDomesticPaymentsVo,
		                      paymentSingleTransferRequest.getData().getOrgId(),
		                      paymentSingleTransferRequest.getData().getUserId(),
		                      paymentSingleTransferRequest.getData().getRoleName(),
		                      paymentSingleTransactionVo);
		          
		          if (Objects.isNull(paymentTransferResponseVo.getMeta())) {
		              response =
		                  constructResponse(
		                      response,
		                      Constants.SUCCESS,
		                      Constants.SUCCESS_YBL_PAYMENT_TRANSFER,
		                      Constants.SUCCESS_YBL_PAYMENT_TRANSFER,
		                      Constants.SUCCESS_DURING_GET);
		            } else if (Objects.nonNull(paymentTransferResponseVo.getMeta())) {

		              response =
		                  constructResponse(
		                      response,
		                      Constants.FAILURE,
		                      Constants.ERROR_YBL_PAYMENT_TRANSFER,
		                      Constants.ERROR_YBL_PAYMENT_TRANSFER,
		                      Constants.FAILURE_DURING_GET);
		            }
		            responseStatus = HttpStatus.OK;

		    }
		    catch(Exception e)
		    {
		    	e.printStackTrace();
		      logger.error("Exception in payment transfer" + e);
		      response =
		          constructResponse(
		              response,
		              Constants.FAILURE,
		              Constants.ERROR_YBL_PAYMENT_TRANSFER,
		              e.getCause() != null
		                  ? Constants.ERROR_YBL_PAYMENT_TRANSFER
		                  : e.getMessage(),
		              Constants.FAILURE_DURING_GET);
		      responseStatus = HttpStatus.INTERNAL_SERVER_ERROR;
		    }
		    return new ResponseEntity<>(response, responseStatus);
	 }

=======
>>>>>>> 0a694d861a9a446c4b7c10a39cf5c7647a3014bd
}

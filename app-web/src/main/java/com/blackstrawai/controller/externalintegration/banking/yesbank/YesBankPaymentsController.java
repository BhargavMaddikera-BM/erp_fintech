package com.blackstrawai.controller.externalintegration.banking.yesbank;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
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
import com.blackstrawai.externalintegration.banking.yesbank.YesBankIntegrationService;
import com.blackstrawai.externalintegration.banking.yesbank.YesBankPaymentsService;
import com.blackstrawai.externalintegration.yesbank.YesBankCustomerInformationVo;
import com.blackstrawai.externalintegration.yesbank.Request.bulkpayments.BulkPaymentVo;
import com.blackstrawai.externalintegration.yesbank.Request.bulkpayments.BulkPaymentsListVo;
import com.blackstrawai.externalintegration.yesbank.Request.paymenttransfer.PaymentTransferVo;
import com.blackstrawai.externalintegration.yesbank.Response.bulkpayments.BulkPaymentsResponse;
import com.blackstrawai.externalintegration.yesbank.Response.bulkpayments.BulkTransactionVo;
import com.blackstrawai.externalintegration.yesbank.Response.common.RequestData;
import com.blackstrawai.externalintegration.yesbank.Response.fundconfirmation.FundsConfirmationResponseVo;
import com.blackstrawai.externalintegration.yesbank.Response.payments.CopyPaymentTransactionVo;
import com.blackstrawai.externalintegration.yesbank.Response.payments.PaymentResponseVo;
import com.blackstrawai.externalintegration.yesbank.Response.payments.PaymentTransactionResponseVo;
import com.blackstrawai.externalintegration.yesbank.Response.paymenttransfer.PaymentTransferResponseVo;
import com.blackstrawai.helper.YesBankIntegrationHelperService;
import com.blackstrawai.request.JSONObject;
import com.blackstrawai.request.externalintegration.banking.yesbank.AvailableBalanceRequest;
import com.blackstrawai.request.externalintegration.banking.yesbank.BulkPaymentRequest;
import com.blackstrawai.request.externalintegration.banking.yesbank.BulkPaymentTransactionInfoRequest;
import com.blackstrawai.request.externalintegration.banking.yesbank.PaymentTransactionRequest;
import com.blackstrawai.request.externalintegration.banking.yesbank.PaymentTransferRequest;
import com.blackstrawai.response.externalintegration.banking.yesbank.BulkPaymentStatusResponse;
import com.blackstrawai.response.externalintegration.banking.yesbank.BulkPaymentsListResponse;
import com.blackstrawai.response.externalintegration.banking.yesbank.BulkTransactionResponse;
import com.blackstrawai.response.externalintegration.banking.yesbank.CopyPaymentTransactionResponse;
import com.blackstrawai.response.externalintegration.banking.yesbank.FundsAvailableResponse;
import com.blackstrawai.response.externalintegration.banking.yesbank.PaymentModeResponse;
import com.blackstrawai.response.externalintegration.banking.yesbank.PaymentTransactionResponse;
import com.blackstrawai.response.externalintegration.banking.yesbank.PaymentTransactionsResponse;
import com.blackstrawai.response.externalintegration.banking.yesbank.TransferResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@CrossOrigin
@RequestMapping("/decifer/yesbank/payments")
public class YesBankPaymentsController extends BaseController {

  private static final Logger logger = Logger.getLogger(YesBankPaymentsController.class);

  private final YesBankPaymentsService transferService;
  private final YesBankIntegrationHelperService paymentsHelperService;
  private final YesBankIntegrationService yesBankIntegrationService;

  public YesBankPaymentsController(
      YesBankPaymentsService transferService,
      YesBankIntegrationHelperService paymentsHelperService , YesBankIntegrationService yesBankIntegrationService) {
    this.transferService = transferService;
    this.paymentsHelperService = paymentsHelperService;
    this.yesBankIntegrationService = yesBankIntegrationService;
    
  }

  @RequestMapping(value = "/v1/paymentTransfer", method = RequestMethod.POST)
  public ResponseEntity<BaseResponse> paymentTransfer(
      HttpServletRequest httpRequest,
      @RequestBody JSONObject<PaymentTransferRequest> paymentTransferRequest) {

    BaseResponse response = new TransferResponse();
    HttpStatus responseStatus;
    try {

      setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));

      String transferType = paymentTransferRequest.getData().getSinglePayment().getPaymentMode();
      double amount =
              Double.parseDouble(paymentTransferRequest.getData().getSinglePayment().getAmount());

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
          paymentsHelperService.convertPaymentTransferRequestToVo(paymentTransferRequest.getData());

      CopyPaymentTransactionVo paymentTransactionVo =
          paymentsHelperService.convertRequestToVo(paymentTransferRequest.getData());

      logger.info("<< payment Transfer request object>>" + paymentTransferDomesticPaymentsVo);

      PaymentTransferResponseVo paymentTransferResponseVo =
          transferService.paymentTransfer(
              paymentTransferDomesticPaymentsVo,
              paymentTransferRequest.getData().getOrgId(),
              paymentTransferRequest.getData().getUserId(),
              paymentTransferRequest.getData().getRoleName(),
              paymentTransactionVo);

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

    } catch (Exception ex) {
    	ex.printStackTrace();
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

  @RequestMapping(value = "/v1/fundsAvailable", method = RequestMethod.POST)
  public ResponseEntity<BaseResponse> fundsConfirmation(
      HttpServletRequest httpRequest,
      @RequestBody JSONObject<AvailableBalanceRequest> availableBalanceRequest) {

    BaseResponse response = new FundsAvailableResponse();
    HttpStatus responseStatus = null;
    try {

      setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));

      AvailableBalanceRequest data = availableBalanceRequest.getData();

      RequestData requestData = paymentsHelperService.convertAvailableBalanceRequestToVo(data);

      logger.info("<<<customerId in retrieving user information>>>>" + requestData);

      if (!(data.getOrgId().isEmpty()
          || data.getCustomerId().isEmpty()
          || data.getAccountNumber().isEmpty())) {

        FundsConfirmationResponseVo fundsConfirmationResponseVo =
            transferService.fundsConfirmation(
                data.getOrgId(),
                data.getCustomerId(),
                data.getAccountNumber(),
                requestData,
                data.getUserId(),
                data.getRoleName());

        if (Boolean.parseBoolean(
            fundsConfirmationResponseVo.getData().getFundsAvailableVo().getFundsAvailable())) {

          ((FundsAvailableResponse) response)
              .setData(fundsConfirmationResponseVo.getData().getFundsAvailableVo());
          responseStatus = HttpStatus.OK;
          response =
              constructResponse(
                  response,
                  Constants.SUCCESS,
                  Constants.SUCCESS_YBL_FUNDS_AVAILABLE,
                  Constants.SUCCESS_YBL_FUNDS_AVAILABLE,
                  Constants.SUCCESS_DURING_GET);
        }
      } else {
        responseStatus = HttpStatus.BAD_REQUEST;
        response =
            constructResponse(
                response,
                Constants.FAILURE,
                Constants.ERROR_YBL_FUNDS_AVAIALBLE,
                Constants.ERROR_YBL_FUNDS_AVAIALBLE,
                Constants.FAILURE_DURING_GET);
      }

    } catch (Exception ex) {
      logger.error("Exception in funds available" + ex);
      response =
          constructResponse(
              response,
              Constants.FAILURE,
              Constants.ERROR_YBL_FUNDS_AVAIALBLE,
              ex.getCause() != null ? Constants.ERROR_YBL_FUNDS_AVAIALBLE : ex.getMessage(),
              Constants.FAILURE_DURING_GET);
      responseStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    }
    return new ResponseEntity<>(response, responseStatus);
  }

  @RequestMapping(value = "v1/paymentTransactions", method = RequestMethod.POST)
  public ResponseEntity<BaseResponse> getPaymentList(
      HttpServletRequest httpRequest,
      @RequestBody JSONObject<PaymentTransactionRequest> requestJSONObject) {

    BaseResponse response = new PaymentTransactionsResponse();
    HttpStatus responseStatus;

    try {

      PaymentTransactionRequest data = requestJSONObject.getData();

      setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
      PaymentResponseVo paymentTransactionResponse =
          transferService.getPaymentTransactionList(
              data.getOrgId(),
              data.getAccountNumber(),
              data.getBeneficiaryId(),
              data.getSearchParam(),
              data.getRoleName(),
                  data.getUserId());

      ((PaymentTransactionsResponse) response).setData(paymentTransactionResponse);
      responseStatus = HttpStatus.OK;
      response =
          constructResponse(
              response,
              Constants.SUCCESS,
              Constants.SUCCESS_YBL_PAYMENT_LIST,
              Objects.isNull(paymentTransactionResponse)
                  ? Constants.ERROR_YBL_NO_PAYMENT_TRANSACTIONS
                  : Constants.SUCCESS_YBL_PAYMENT_LIST,
              Constants.SUCCESS_DURING_GET);

    } catch (Exception ex) {
      logger.error(":::Exception in getting the payment list:::" + ex);
      responseStatus = HttpStatus.INTERNAL_SERVER_ERROR;
      String errorMessage = ex.getCause() != null ? ex.getCause().getMessage() : ex.getMessage();

      response =
          constructResponse(
              response,
              Constants.FAILURE,
              Constants.ERROR_YBL_PAYMENT_LIST,
              errorMessage,
              Constants.YBL_PAYMENT_FETCH_UNSUCCESSFULLY);
    }

    return new ResponseEntity(response, responseStatus);
  }


  @RequestMapping(value = "v1/paymentDropdown/{organizationId}", method = RequestMethod.GET)
  public ResponseEntity<BaseResponse> paymentDropDown(
      HttpServletRequest httpRequest, @PathVariable int organizationId) {

    BaseResponse response = new PaymentModeResponse();
    HttpStatus responseStatus;

    try {

      setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));

      ((PaymentModeResponse) response)
          .setData(transferService.getPaymentDropDownList(organizationId));
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

  @RequestMapping(
      value = {"v1/viewTransaction/{organizationId}/{transactionId}"},
      method = RequestMethod.GET)
  public ResponseEntity<BaseResponse> viewTransaction(
      HttpServletRequest httpRequest,
      @PathVariable("organizationId") String organizationId,
      @PathVariable("transactionId") String transactionId) {

    BaseResponse response = new PaymentTransactionResponse();
    HttpStatus responseStatus;
    try {

      setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));

      PaymentTransactionResponseVo paymentTransactionResponse =
          transferService.viewPaymentTransaction(organizationId, transactionId);

      if (Objects.isNull(paymentTransactionResponse)) {
        throw new ApplicationException(Constants.ERROR_YBL_NO_PAYMENT_TRANSACTIONS);
      }

      if (Objects.nonNull(paymentTransactionResponse)) {
        ((PaymentTransactionResponse) response).setData(paymentTransactionResponse);
        responseStatus = HttpStatus.OK;
        response =
            constructResponse(
                response,
                Constants.SUCCESS,
                Constants.SUCCESS_YBL_PAYMENT_LIST,
                Constants.SUCCESS_YBL_PAYMENT_LIST,
                Constants.SUCCESS_DURING_GET);
      } else {
        responseStatus = HttpStatus.OK;
        response =
            constructResponse(
                response,
                Constants.SUCCESS,
                Constants.ERROR_YBL_NO_PAYMENT_TRANSACTIONS,
                Constants.ERROR_YBL_NO_PAYMENT_TRANSACTIONS,
                Constants.SUCCESS_DURING_GET);
      }

    } catch (Exception ex) {
      logger.error(":::Exception in getting the payment transaction:::" + ex);
      responseStatus = HttpStatus.INTERNAL_SERVER_ERROR;
      String errorMessage = ex.getCause() != null ? ex.getCause().getMessage() : ex.getMessage();

      response =
          constructResponse(
              response,
              Constants.FAILURE,
              Constants.ERROR_YBL_PAYMENT_LIST,
              errorMessage,
              Constants.YBL_PAYMENT_FETCH_UNSUCCESSFULLY);
    }
    return new ResponseEntity(response, responseStatus);
  }

  @RequestMapping(
      value = {"v1/copyTransaction/{organizationId}/{transactionId}"},
      method = RequestMethod.GET)
  public ResponseEntity<BaseResponse> copyTransaction(
      HttpServletRequest httpRequest,
      @PathVariable("organizationId") String organizationId,
      @PathVariable("transactionId") String transactionId) {

    BaseResponse response = new CopyPaymentTransactionResponse();
    HttpStatus responseStatus;
    try {

      setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));

      CopyPaymentTransactionVo paymentTransactionResponse =
          transferService.copyPaymentTransaction(organizationId, transactionId);

      if (Objects.isNull(paymentTransactionResponse)) {
        throw new ApplicationException(Constants.ERROR_YBL_NO_PAYMENT_TRANSACTIONS);
      }

      if (Objects.nonNull(paymentTransactionResponse)) {
        ((CopyPaymentTransactionResponse) response).setData(paymentTransactionResponse);
        responseStatus = HttpStatus.OK;
        response =
            constructResponse(
                response,
                Constants.SUCCESS,
                Constants.SUCCESS_YBL_PAYMENT_LIST,
                Constants.SUCCESS_YBL_PAYMENT_LIST,
                Constants.SUCCESS_DURING_GET);
      } else {
        responseStatus = HttpStatus.OK;
        response =
            constructResponse(
                response,
                Constants.SUCCESS,
                Constants.ERROR_YBL_NO_PAYMENT_TRANSACTIONS,
                Constants.ERROR_YBL_NO_PAYMENT_TRANSACTIONS,
                Constants.SUCCESS_DURING_GET);
      }

    } catch (Exception ex) {
      logger.error(":::Exception in getting the payment transaction:::" + ex);
      responseStatus = HttpStatus.INTERNAL_SERVER_ERROR;
      String errorMessage = ex.getCause() != null ? ex.getCause().getMessage() : ex.getMessage();

      response =
          constructResponse(
              response,
              Constants.FAILURE,
              Constants.ERROR_YBL_PAYMENT_LIST,
              errorMessage,
              Constants.YBL_PAYMENT_FETCH_UNSUCCESSFULLY);
    }
    return new ResponseEntity(response, responseStatus);
  }
  

  @RequestMapping(value = "/v1/bulk_payment_transfer", method = RequestMethod.POST)
  public ResponseEntity<BaseResponse> bulkPaymentTransfer(
      HttpServletRequest httpRequest,
      @RequestBody JSONObject<BulkPaymentRequest> bulkPaymentTransferRequest) {

    BaseResponse response = new TransferResponse();
    HttpStatus responseStatus ;
    try {
    
    	if(Objects.nonNull(bulkPaymentTransferRequest) && Objects.nonNull(bulkPaymentTransferRequest.getData()) ) {
    	      double amount =
    	              Double.parseDouble(bulkPaymentTransferRequest.getData().getControSum());

    	      if(Objects.isNull(bulkPaymentTransferRequest.getData().getPaymentDetails()) || bulkPaymentTransferRequest.getData().getPaymentDetails().isEmpty()){
      	        throw new ApplicationException(Constants.ATLEAST_ONE_PAYMENT_IS_MANDATORY);
      	      }
    	      
    	      if(amount <0){
    	        throw new ApplicationException(Constants.ERROR_YBL_NEGATIVE_PAYMENT_TRANSFER);
    	      }
    	      
    	      
    	      BulkPaymentVo bulkPaymentData  = null;
    	      YesBankCustomerInformationVo debtorDetails = null;
    	    if(Objects.nonNull(bulkPaymentTransferRequest.getData().getCustomerId()) && Objects.nonNull(bulkPaymentTransferRequest.getData().getDebitAccountId())) {
    	    	 debtorDetails = yesBankIntegrationService.getYesBankAccountDetails(bulkPaymentTransferRequest.getData().getDebitAccountId() ,(bulkPaymentTransferRequest.getData().getOrgId() ));
    	    	 bulkPaymentData =
    	    			paymentsHelperService.convertBulkPaymentTransferRequestToVo(bulkPaymentTransferRequest.getData() , debtorDetails);
    	    	 logger.info("<< payment Transfer request object>>" + bulkPaymentData);

    	    }
    	    ObjectMapper objectMapper = new ObjectMapper();
    	    String uiReqJson = objectMapper.writeValueAsString(bulkPaymentTransferRequest);

    	    BulkPaymentsResponse paymentTransferResponseVo= null;
    if(debtorDetails!=null && bulkPaymentData!=null) {
       paymentTransferResponseVo =
          transferService.bulkPaymentTransfer(
        		  bulkPaymentData,
        		  bulkPaymentTransferRequest.getData().getOrgId(),
        		  bulkPaymentTransferRequest.getData().getUserId(),
        		  bulkPaymentTransferRequest.getData().getRoleName() , uiReqJson,debtorDetails);
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

  @RequestMapping(value = "v1/bulk/payment_transaction" , method = RequestMethod.POST)
  public ResponseEntity<BaseResponse> getBulkPaymentList(
      HttpServletRequest httpRequest,
      @RequestBody JSONObject<PaymentTransactionRequest> requestJSONObject) {

    BaseResponse response = new BulkPaymentsListResponse();
    HttpStatus responseStatus;

    try {

      setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
      PaymentTransactionRequest paymentReq = requestJSONObject!=null && requestJSONObject.getData()!= null ? requestJSONObject.getData() : null;
      Integer organizationId = paymentReq!=null && paymentReq.getOrgId()!=null ? Integer.valueOf(paymentReq.getOrgId()) : 0 ;
    		  Integer	  userId =  paymentReq!=null && paymentReq.getUserId()!=null ? Integer.valueOf(paymentReq.getUserId()) : 0 ;
    		  String roleName = paymentReq.getRoleName();
    		  String accountNo =paymentReq.getAccountNumber(); 
    		List<BulkPaymentsListVo> paymentsList =   transferService.getBulkPaymentTransferList(organizationId, userId, roleName , accountNo);

      ((BulkPaymentsListResponse) response).setData(paymentsList);
      responseStatus = HttpStatus.OK;
      response =
          constructResponse(
              response,
              Constants.SUCCESS,
              Constants.SUCCESS_YBL_PAYMENT_LIST,
              Constants.SUCCESS_YBL_PAYMENT_LIST, Constants.SUCCESS_DURING_GET);

    } catch (Exception ex) {
      logger.error(":::Exception in getting the payment list:::" + ex);
      responseStatus = HttpStatus.INTERNAL_SERVER_ERROR;
      String errorMessage = ex.getCause() != null ? ex.getCause().getMessage() : ex.getMessage();

      response =
          constructResponse(
              response,
              Constants.FAILURE,
              Constants.ERROR_YBL_PAYMENT_LIST,
              errorMessage,
              Constants.YBL_PAYMENT_FETCH_UNSUCCESSFULLY);
    }

    return new ResponseEntity(response, responseStatus);
  }
  @RequestMapping(
	      value = {"v1/bulk/view_transaction/{organizationId}/{organizationName}/{roleName}/{userId}/{transactionNo}"},
	      method = RequestMethod.GET)
	  public ResponseEntity<BaseResponse> viewBulkTransaction(
	      HttpServletRequest httpRequest,
	      @PathVariable("organizationId") Integer organizationId,
	      @PathVariable("organizationName") String organizationName,
	      @PathVariable("roleName") String roleName,
	      @PathVariable("userId") String userId,
	      @PathVariable("transactionNo") String transactionNo) {

	    BaseResponse response = new BulkTransactionResponse();
	    HttpStatus responseStatus;
	    try {

	      setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));

	      BulkTransactionVo paymentTransactionResponse =
	          transferService.viewBulkTransactionDetails(organizationId,roleName,userId,transactionNo);
	      
	      

	      if (Objects.isNull(paymentTransactionResponse)) {
	        throw new ApplicationException(Constants.ERROR_YBL_NO_PAYMENT_TRANSACTIONS);
	      }

	      if (Objects.nonNull(paymentTransactionResponse)) {
	        ((BulkTransactionResponse) response).setData(paymentTransactionResponse);
	        responseStatus = HttpStatus.OK;
	        response =
	            constructResponse(
	                response,
	                Constants.SUCCESS,
	                Constants.SUCCESS_YBL_PAYMENT_LIST,
	                Constants.SUCCESS_YBL_PAYMENT_LIST,
	                Constants.SUCCESS_DURING_GET);
	      } else {
	        responseStatus = HttpStatus.OK;
	        response =
	            constructResponse(
	                response,
	                Constants.SUCCESS,
	                Constants.ERROR_YBL_NO_PAYMENT_TRANSACTIONS,
	                Constants.ERROR_YBL_NO_PAYMENT_TRANSACTIONS,
	                Constants.SUCCESS_DURING_GET);
	      }

	    } catch (Exception ex) {
	      logger.error(":::Exception in getting the payment transaction:::" + ex);
	      responseStatus = HttpStatus.INTERNAL_SERVER_ERROR;
	      String errorMessage = ex.getCause() != null ? ex.getCause().getMessage() : ex.getMessage();

	      response =
	          constructResponse(
	              response,
	              Constants.FAILURE,
	              Constants.ERROR_YBL_PAYMENT_LIST,
	              errorMessage,
	              Constants.YBL_PAYMENT_FETCH_UNSUCCESSFULLY);
	    }
	    return new ResponseEntity(response, responseStatus);
	  }

  
  
  @RequestMapping(
	      value = {"v1/bulk/get_transaction_status"},
	      method = RequestMethod.POST)
	  public ResponseEntity<BaseResponse> getBulkTransactionPaymentStatus(
	      HttpServletRequest httpRequest, @RequestBody JSONObject<BulkPaymentTransactionInfoRequest> bulkPaymnetInfoRequest) {

	    BaseResponse response = new BulkPaymentStatusResponse();
	    HttpStatus responseStatus;
	    try {

	      setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));

	      Map<String , String > responseMap =
	          transferService.getBulkTxnPaymentStatus(bulkPaymnetInfoRequest.getData().getFileIdentifier() ,
	        		  bulkPaymnetInfoRequest.getData().getOrganizationId(), bulkPaymnetInfoRequest.getData().getTransactionNo(), bulkPaymnetInfoRequest.getData().getCustomerId(),
	        		  bulkPaymnetInfoRequest.getData().getAccountNo(), bulkPaymnetInfoRequest.getData().getUserId(), bulkPaymnetInfoRequest.getData().getRoleName());
	    
		      

	      if (Objects.isNull(responseMap)) {
	        throw new ApplicationException(Constants.ERROR_YBL_NO_PAYMENT_TRANSACTIONS);
	      }

	      if (Objects.nonNull(responseMap)) {
	        ((BulkPaymentStatusResponse) response).setData(responseMap);
	        responseStatus = HttpStatus.OK;
	        response =
	            constructResponse(
	                response,
	                Constants.SUCCESS,
	                Constants.SUCCESS_YBL_PAYMENT_LIST,
	                Constants.SUCCESS_YBL_PAYMENT_LIST,
	                Constants.SUCCESS_DURING_GET);
	      } else {
	        responseStatus = HttpStatus.OK;
	        response =
	            constructResponse(
	                response,
	                Constants.SUCCESS,
	                Constants.ERROR_YBL_NO_PAYMENT_TRANSACTIONS,
	                Constants.ERROR_YBL_NO_PAYMENT_TRANSACTIONS,
	                Constants.SUCCESS_DURING_GET);
	      }

	    } catch (Exception ex) {
	      logger.error(":::Exception in getting the payment transaction:::" + ex);
	      responseStatus = HttpStatus.INTERNAL_SERVER_ERROR;
	      String errorMessage = ex.getCause() != null ? ex.getCause().getMessage() : ex.getMessage();

	      response =
	          constructResponse(
	              response,
	              Constants.FAILURE,
	              Constants.ERROR_YBL_PAYMENT_LIST,
	              errorMessage,
	              Constants.YBL_PAYMENT_FETCH_UNSUCCESSFULLY);
	    }
	    return new ResponseEntity(response, responseStatus);
	  }
  
  
}

package com.blackstrawai.controller.externalintegration.banking.yesbank;

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

import com.blackstrawai.common.BaseController;
import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.common.Constants;
import com.blackstrawai.externalintegration.banking.yesbank.YesBankIntegrationService;
import com.blackstrawai.externalintegration.yesbank.YesBankCustomerInformationVo;
import com.blackstrawai.externalintegration.yesbank.YesBankNewAccountSetupVo;
import com.blackstrawai.helper.YesBankIntegrationHelperService;
import com.blackstrawai.request.JSONObject;
import com.blackstrawai.request.externalintegration.banking.yesbank.YesBankAccountSettingRequest;
import com.blackstrawai.request.externalintegration.banking.yesbank.YesBankCustomerInformationRequest;
import com.blackstrawai.request.externalintegration.banking.yesbank.YesBankNewAccountSetupRequest;
import com.blackstrawai.response.externalintegration.banking.yesbank.YesBankCustomerInformationResponse;
import com.blackstrawai.response.externalintegration.banking.yesbank.YesBankNewBankAccountSetupResponse;

@RestController
@CrossOrigin
@RequestMapping("/decifer/yesbank/externalIntegration")
public class YesBankIntegrationController extends BaseController {

  private final Logger logger = Logger.getLogger(YesBankIntegrationController.class);

  private final YesBankIntegrationService yesBankIntegrationService;
  private final YesBankIntegrationHelperService yesBankIntegrationHelperService;

  public YesBankIntegrationController(
      YesBankIntegrationService yesBankIntegrationService,
      YesBankIntegrationHelperService yesBankIntegrationHelperService) {
    this.yesBankIntegrationService = yesBankIntegrationService;
    this.yesBankIntegrationHelperService = yesBankIntegrationHelperService;
  }

  @RequestMapping(value = "/v1/apibanking/enable", method = RequestMethod.POST)
  public ResponseEntity<BaseResponse> enableAPIBanking(
      HttpServletRequest httpRequest,
      @RequestBody JSONObject<YesBankCustomerInformationRequest> yesBankCustomerInformation) {

    BaseResponse response = new YesBankCustomerInformationResponse();
    HttpStatus HttpStatus;
    try {
      int success = 0;
      setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));

      YesBankCustomerInformationVo yesBankCustomerInfoVo =
          yesBankIntegrationHelperService.convertRequestToVo(yesBankCustomerInformation.getData());

      if (Objects.nonNull(yesBankCustomerInfoVo)) {
        success = yesBankIntegrationService.enableAPIRequest(yesBankCustomerInfoVo);
      }

      if (success == 1) {
        response =
            constructResponse(
                response,
                Constants.SUCCESS,
                Constants.SUCCESS_YBL_API_BANKING_ENABLE,
                Constants.SUCCESS_YBL_API_BANKING_ENABLE,
                Constants.YBL_API_BANKING_ENABLED_SUCCESSFULLY);
      }
      HttpStatus = org.springframework.http.HttpStatus.OK;

    } catch (Exception ex) {
      logger.error("Exception in api banking enable");
      response =
          constructResponse(
              response,
              Constants.FAILURE,
              Constants.ERROR_YBL_API_BANKING_ENABLE,
              ex.getMessage() != null ? ex.getMessage() : Constants.ERROR_YBL_API_BANKING_ENABLE,
              Constants.YBL_API_BANKING_ENABLED_UNSUCCESSFULLY);
      HttpStatus = org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
    }
    return new ResponseEntity<>(response, HttpStatus);
  }

  @RequestMapping(value = "/v1/apibanking/register", method = RequestMethod.POST)
  public ResponseEntity<BaseResponse> registerAPIBanking(
      HttpServletRequest httpRequest,
      @RequestBody JSONObject<YesBankCustomerInformationRequest> yesBankCustomerInformation) {

    BaseResponse response = new YesBankCustomerInformationResponse();
    HttpStatus HttpStatus;
    try {
      int success = 0;
      setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));

      YesBankCustomerInformationVo yesBankCustomerInfoVo =
          yesBankIntegrationHelperService.convertRequestToVo(yesBankCustomerInformation.getData());

      if (Objects.nonNull(yesBankCustomerInfoVo)) {
        success = yesBankIntegrationService.requestAPIBanking(yesBankCustomerInfoVo);
      }

      if (success == 1) {
        response =
            constructResponse(
                response,
                Constants.SUCCESS,
                Constants.SUCCESS_YBL_API_BANKING_REGISTER,
                Constants.SUCCESS_YBL_API_BANKING_REGISTER,
                Constants.YBL_API_BANKING_REGISTERED_SUCCESSFULLY);
      }
      HttpStatus = org.springframework.http.HttpStatus.OK;

    } catch (Exception ex) {
      logger.error("Exception in api banking enable");
      response =
          constructResponse(
              response,
              Constants.FAILURE,
              Constants.ERROR_YBL_API_BANKING_REGISTER,
              ex.getMessage() != null ? ex.getMessage() : Constants.ERROR_YBL_API_BANKING_REGISTER,
              Constants.YBL_API_BANKING_REGISTERED_UNSUCCESSFULLY);
      HttpStatus = org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
    }
    return new ResponseEntity<>(response, HttpStatus);
  }

  @RequestMapping(value = "/v1/dashboard/donutchart/{organizationId}", method = RequestMethod.GET)
  public Map<String, Map<String, String>> donutChart(
      HttpServletRequest httpRequest, @PathVariable int organizationId) {
    Map<String, Map<String, String>> invoicesMap = null;
    BaseResponse response = new YesBankCustomerInformationResponse();
    HttpStatus httpStatus=null;
    try {
      setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));

      if (Objects.nonNull(organizationId)) {
        invoicesMap =
            yesBankIntegrationService.getBillsInvoiceDonutChartCalculation(organizationId);
      }

      if (Objects.nonNull(invoicesMap)) {
        response =
            constructResponse(
                response,
                Constants.SUCCESS,
                Constants.SUCCESS_DROP_DOWN_FETCH,
                Constants.SUCCESS_DROP_DOWN_FETCH,
                Constants.SUCCESS_DURING_GET);
      }
      httpStatus = org.springframework.http.HttpStatus.OK;

    } catch (Exception ex) {
      logger.error("Exception in api banking enable");
      response =
          constructResponse(
              response,
              Constants.FAILURE,
              Constants.FAILURE_DROP_DOWN_FETCH,
              ex.getMessage(),
              Constants.FAILURE_DURING_GET);
      httpStatus = org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
    }
    return invoicesMap;
  }

  @RequestMapping(value = "/v1/bank_account/{organizationId}/{organizationName}/{userId}/{roleName}/{id}", method = RequestMethod.GET)
  public BaseResponse getYesBankAccountDetails(
      HttpServletRequest httpRequest, 
      @PathVariable Integer organizationId,
      @PathVariable String organizationName,
      @PathVariable int userId,
      @PathVariable String roleName,
      @PathVariable Integer id
      ) {
	  
    BaseResponse response = new YesBankCustomerInformationResponse();
    HttpStatus HttpStatus;
    try {
      setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
      YesBankCustomerInformationVo info = null;
      if (Objects.nonNull(organizationId) && Objects.nonNull(id)) {
    	  info =
            yesBankIntegrationService.getYesBankAccountDetails(id, organizationId);
			((YesBankCustomerInformationResponse) response).setData(info);
      }

      if (Objects.nonNull(info)) {
        response =
            constructResponse(
                response,
                Constants.SUCCESS,
                Constants.SUCCESS_ACCOUNT_OVERVIEW,
                Constants.SUCCESS_ACCOUNT_OVERVIEW,
                Constants.SUCCESS_DURING_GET);
      }
      HttpStatus = org.springframework.http.HttpStatus.OK;

    } catch (Exception ex) {
      logger.error("Exception in fetch bank details");
      response =
          constructResponse(
              response,
              Constants.FAILURE,
              Constants.FAILURE_ACCOUNT_OVERVIEW,
              ex.getMessage(),
              Constants.FAILURE_DURING_GET);
      HttpStatus = org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
    }
    return response;
  }
  
  @RequestMapping(value = "/v1/newAccountSetup", method = RequestMethod.POST)
  public ResponseEntity<BaseResponse> saveYesBankNewAccount(
      HttpServletRequest httpRequest,
      @RequestBody JSONObject<YesBankNewAccountSetupRequest> yesBankNewAccountRequest) {
    HttpStatus status = HttpStatus.OK;
    BaseResponse response = new YesBankNewBankAccountSetupResponse();

    try {
      int success = 0;
      setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));

      YesBankNewAccountSetupVo yesBankCustomerInfoVo =
          yesBankIntegrationHelperService.convertRequestToNewAccountSetupVo(
              yesBankNewAccountRequest.getData());

      if (Objects.nonNull(yesBankCustomerInfoVo)) {
        success = yesBankIntegrationService.saveNewAccountSetupInfo(yesBankCustomerInfoVo);
      }

      if (success == 1) {
        response =
            constructResponse(
                response,
                Constants.SUCCESS,
                Constants.SUCCESS_YBL_NEW_ACCOUNT_SETUP,
                Constants.SUCCESS_YBL_NEW_ACCOUNT_SETUP,
                Constants.YBL_API_BANKING_ENABLED_SUCCESSFULLY);
      }

    } catch (Exception ex) {
      logger.error("Exception in save new account ");
      response =
          constructResponse(
              response,
              Constants.FAILURE,
              Constants.ERROR_YBL_NEW_ACCOUNT_SETUP,
              ex.getMessage() != null ? ex.getMessage() : Constants.ERROR_YBL_NEW_ACCOUNT_SETUP,
              Constants.YBL_API_BANKING_ENABLED_UNSUCCESSFULLY);
      status = org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
    }
    return new ResponseEntity<>(response, status);
  }


  @RequestMapping(value = "/v1/setting/bank_account", method = RequestMethod.PUT)
  public ResponseEntity<BaseResponse> updatebankAccountSetting(
      HttpServletRequest httpRequest,
      @RequestBody JSONObject<YesBankAccountSettingRequest> settingRequest) {

    BaseResponse response = new YesBankCustomerInformationResponse();
    HttpStatus HttpStatus;
    try {
      int success = 0;
      setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));

      YesBankCustomerInformationVo yesBankCustomerInfoVo =
          yesBankIntegrationHelperService.convertRequestToVo(settingRequest.getData());

      if (Objects.nonNull(yesBankCustomerInfoVo)) {
        success = yesBankIntegrationService.updateAccountSetting(yesBankCustomerInfoVo);
      }

      if (success == 1) {
        response =
            constructResponse(
                response,
                Constants.SUCCESS,
                Constants.SUCCESS_YBL_API_BANKING_SETTING,
                Constants.SUCCESS_YBL_API_BANKING_SETTING,
                Constants.YBL_API_BANKING_REGISTERED_SUCCESSFULLY);
      }
      HttpStatus = org.springframework.http.HttpStatus.OK;

    } catch (Exception ex) {
      logger.error("Exception in api banking enable");
      response =
          constructResponse(
              response,
              Constants.FAILURE,
              Constants.ERROR_YBL_API_BANKING_SETTING,
              ex.getMessage() != null ? ex.getMessage() : Constants.ERROR_YBL_API_BANKING_SETTING,
              Constants.YBL_API_BANKING_REGISTERED_UNSUCCESSFULLY);
      HttpStatus = org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
    }
    return new ResponseEntity<>(response, HttpStatus);
  }

}

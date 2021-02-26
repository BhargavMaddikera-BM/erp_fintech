package com.blackstrawai.controller.externalintegration.banking.yesbank;

import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.blackstrawai.banking.dashboard.BankMasterAccountVo;
import com.blackstrawai.common.BaseController;
import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.common.Constants;
import com.blackstrawai.common.EntityConverter;
import com.blackstrawai.externalintegration.banking.yesbank.YesBankBeneficiaryService;
import com.blackstrawai.externalintegration.banking.yesbank.YesBankConstants;
import com.blackstrawai.externalintegration.yesbank.beneficiary.BeneficiaryListVo;
import com.blackstrawai.externalintegration.yesbank.beneficiary.YesBankBeneficiaryVo;
import com.blackstrawai.externalintegration.yesbank.dropdowns.BeneficiaryDropDownVo;
import com.blackstrawai.helper.YesBankIntegrationHelperService;
import com.blackstrawai.keycontact.customer.CustomerBankDetailsVo;
import com.blackstrawai.keycontact.vendor.VendorBankDetailsVo;
import com.blackstrawai.request.JSONObject;
import com.blackstrawai.request.externalintegration.banking.yesbank.BeneficiaryRequest;
import com.blackstrawai.response.externalintegration.banking.yesbank.BeneficiaryDropDownResponse;
import com.blackstrawai.response.externalintegration.banking.yesbank.BeneficiaryListResponse;
import com.blackstrawai.response.externalintegration.banking.yesbank.BeneficiaryResponse;
import com.blackstrawai.response.externalintegration.banking.yesbank.VendorBankResponse;

@RestController
@CrossOrigin
@RequestMapping("/decifer/yesbank/beneficiary")
public class YesBankBeneficiaryController extends BaseController {

  private static final Logger logger = Logger.getLogger(YesBankBeneficiaryController.class);

  private final YesBankIntegrationHelperService yesBankIntegrationHelperService;
  private final YesBankBeneficiaryService yesBankBeneficiaryService;
  private final EntityConverter entityConverter;

  public YesBankBeneficiaryController(
      YesBankBeneficiaryService yesBankBeneficiaryService,
      YesBankIntegrationHelperService yesBankIntegrationHelperService,
      EntityConverter entityConverter) {
    this.yesBankBeneficiaryService = yesBankBeneficiaryService;
    this.yesBankIntegrationHelperService = yesBankIntegrationHelperService;
    this.entityConverter = entityConverter;
  }

  @RequestMapping(value = "/v1/beneficiaries", method = RequestMethod.POST)
  public ResponseEntity<BaseResponse> createBeneficiary(
      HttpServletRequest httpRequest,
      HttpServletResponse httpResponse,
      @RequestBody JSONObject<BeneficiaryRequest> beneficiaryRequest) {
    logger.info("Entered Into createInvoice");
    BaseResponse response = new BeneficiaryResponse();
    HttpStatus responseStatus;
    try {
      logger.info("Request Payload:" + generateRequestAndResponseLogPaylod(beneficiaryRequest));
      if (Objects.nonNull(beneficiaryRequest.getData())) {
        BeneficiaryRequest request = beneficiaryRequest.getData();
        if (Objects.isNull(request.getOrganizationId())) {
          throw new ApplicationException(Constants.FAILURE_IN_USER_ORGANIZATION_REQUEST_VALIDATION);
        }
        if (!(Objects.equals(request.getAccountNumber(), request.getConfirmAccountNumber()))) {
          throw new ApplicationException(Constants.ERROR_YBL_NOT_VALID_ACCOUNT_NO);
        }

        YesBankBeneficiaryVo beneficiaryVo =
            entityConverter.convertToEntity(request, YesBankBeneficiaryVo.class);
        yesBankBeneficiaryService.createBeneficiary(beneficiaryVo);
      }
      setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
      responseStatus = HttpStatus.OK;
      response =
          constructResponse(
              response,
              Constants.SUCCESS,
              Constants.SUCCESS_YBL_CREATE_BENEFICIARY,
              Constants.SUCCESS_YBL_CREATE_BENEFICIARY,
              Constants.YBL_BENEFICIARY_CREATED_SUCCESSFULLY);
      logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));

    } catch (Exception e) {
      logger.error(":::Exception in createBeneficiary:::" + e);
      responseStatus = HttpStatus.INTERNAL_SERVER_ERROR;
      String errorMessage = e.getCause() != null ? e.getCause().getMessage() : e.getMessage();
      response =
          constructResponse(
              response,
              Constants.FAILURE,
              Constants.ERROR_YBL_CREATE_BENEFICIARY,
              errorMessage,
              Constants.YBL_BENEFICIARY_CREATED_UNSUCCESSFULLY);
      logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
    }
    return new ResponseEntity<>(response, responseStatus);
  }

  @RequestMapping(
      value = "/v1/dropsdowns/beneficiaries/{organizationId}",
      method = RequestMethod.GET)
  public ResponseEntity<BaseResponse> getBeneficiaryDropDown(
      HttpServletRequest httpServletRequest,
      HttpServletResponse httpServletResponse,
      @PathVariable int organizationId) {
    logger.info("Entry into getBeneficiaryDropDown");
    BaseResponse response = new BeneficiaryDropDownResponse();
    try {
      BeneficiaryDropDownVo data = yesBankBeneficiaryService.getDropDowns(organizationId);
      setTokens(
          response,
          httpServletRequest.getHeader("keyToken"),
          httpServletRequest.getHeader("valueToken"));
      ((BeneficiaryDropDownResponse) response).setData(data);
      response =
          constructResponse(
              response,
              Constants.SUCCESS,
              Constants.SUCCESS_DROP_DOWN_FETCH,
              Constants.SUCCESS_DROP_DOWN_FETCH,
              Constants.SUCCESS_DURING_GET);
      logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
      return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);

    } catch (Exception e) {
      response =
          constructResponse(
              response,
              Constants.FAILURE,
              Constants.FAILURE_DROP_DOWN_FETCH,
              e.getMessage(),
              Constants.FAILURE_DURING_GET);
      logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
      return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @RequestMapping(value = "/v1/beneficiaries/list/{organizationId}/{organizationName}/{userId}/{roleName}", method = RequestMethod.GET)
  public ResponseEntity<BaseResponse> getBeneficiaryList(
      HttpServletRequest httpServletRequest,
      HttpServletResponse httpServletResponse,
      @PathVariable int organizationId) {
    logger.info("Entry into getBeneficiaryList");
    BaseResponse response = new BeneficiaryListResponse();
    try {
      List<BeneficiaryListVo> data = yesBankBeneficiaryService.getBeneficiaryList(organizationId);
      setTokens(
          response,
          httpServletRequest.getHeader("keyToken"),
          httpServletRequest.getHeader("valueToken"));
      ((BeneficiaryListResponse) response).setData(data);
      response =
          constructResponse(
              response,
              Constants.SUCCESS,
              Constants.SUCCESS_DROP_DOWN_FETCH,
              Constants.SUCCESS_DROP_DOWN_FETCH,
              Constants.SUCCESS_DURING_GET);
      logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
      return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);

    } catch (Exception e) {
      response =
          constructResponse(
              response,
              Constants.FAILURE,
              Constants.FAILURE_DROP_DOWN_FETCH,
              e.getMessage(),
              Constants.FAILURE_DURING_GET);
      logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
      return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @RequestMapping(
      value =
          "/v1/beneficiaries/{organizationId}/{organizationName}/{userId}/{roleName}/{id}/{beneficiaryType}")
  public ResponseEntity<BaseResponse> getBeneficiaryById(
      HttpServletRequest httpRequest,
      @PathVariable int organizationId,
      @PathVariable String organizationName,
      @PathVariable int userId,
      @PathVariable String roleName,
      @PathVariable int id,
      @PathVariable String beneficiaryType) {

    BaseResponse response = new BeneficiaryResponse();
    HttpStatus responseStatus;

    try {

      setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
      if (Objects.nonNull(beneficiaryType)) {
        switch (beneficiaryType) {
          case YesBankConstants.OTHER_BENEFICIARY:
            YesBankBeneficiaryVo beneficiaryVo =
                yesBankBeneficiaryService.getBankingBeneficiary(organizationId, id);
            response = new BeneficiaryResponse();
            ((BeneficiaryResponse) response).setData(beneficiaryVo);
            logger.info("::: Beneficiary Details :::" + beneficiaryVo);
            break;
          case YesBankConstants.BENEFICIARY_TYPE_VENDOR:
            VendorBankDetailsVo vendorBankVo =
                yesBankBeneficiaryService.getVendorBeneficiaryyById(organizationId, id);
            response = new VendorBankResponse();
            ((VendorBankResponse) response).setData(vendorBankVo);
            logger.info("::: Beneficiary Details :::" + vendorBankVo);
            break;
          case YesBankConstants.BENEFICIARY_TYPE_EMPLOYEE:
            VendorBankDetailsVo employeeBankVo =
                yesBankBeneficiaryService.getEmployeeBeneficiary(organizationId, id);
            response = new VendorBankResponse();
            ((VendorBankResponse) response).setData(employeeBankVo);
            logger.info("::: Beneficiary Details :::" + employeeBankVo);
            break;
          case YesBankConstants.BENEFICIARY_TYPE_CUSTOMER:
            VendorBankDetailsVo customerBankVo =
                yesBankBeneficiaryService.getCustomerBeneficiary(organizationId, id);
            response = new VendorBankResponse();
            ((VendorBankResponse) response).setData(customerBankVo);
            logger.info("::: Beneficiary Details :::" + customerBankVo);
            break;
        }
      }
      responseStatus = HttpStatus.OK;
      response =
          constructResponse(
              response,
              Constants.SUCCESS,
              Constants.SUCCESS_YBL_GET_BENEFICIARY,
              Constants.SUCCESS_YBL_GET_BENEFICIARY,
              Constants.YBL_BENEFICIARY_FETCH_SUCCESSFULLY);

    } catch (Exception ex) {
      logger.error(":::Exception in getBeneficiary:::" + ex);
      responseStatus = HttpStatus.INTERNAL_SERVER_ERROR;
      String errorMessage = ex.getCause() != null ? ex.getCause().getMessage() : ex.getMessage();

      response =
          constructResponse(
              response,
              Constants.FAILURE,
              Constants.ERROR_YBL_GET_BENEFICIARY,
              errorMessage,
              Constants.YBL_BENEFICIARY_FETCH_UNSUCCESSFULLY);
    }
    return new ResponseEntity<>(response, responseStatus);
  }

  @RequestMapping(
      value = "/v1/disableBeneficiary/{id}/{beneficiaryType}/{status}",
      method = RequestMethod.DELETE)
  public ResponseEntity<BaseResponse> disableBeneficiary(
      HttpServletRequest httpRequest,
      @PathVariable String id,
      @PathVariable String beneficiaryType,
      @PathVariable String status) {

    BaseResponse response = new BeneficiaryResponse();
    HttpStatus responseStatus;

    try {

      if (Objects.isNull(id) && Objects.isNull(beneficiaryType)) {
        throw new ApplicationException(Constants.VALIDATE_YBL_DISABLE_BENEFICIARY);
      }
      setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
      yesBankBeneficiaryService.disableBeneficiary(id, beneficiaryType, status);

      responseStatus = HttpStatus.OK;
      response =
          constructResponse(
              response,
              Constants.SUCCESS,
              Constants.SUCCESS_YBL_DISABLE_BENEFICIARY,
              Constants.SUCCESS_YBL_DISABLE_BENEFICIARY,
              Constants.YBL_BENEFICIARY_DELETED_SUCCESSFULLY);

    } catch (Exception ex) {
      logger.error(":::Exception in updateBeneficiary:::" + ex);
      responseStatus = HttpStatus.INTERNAL_SERVER_ERROR;
      String errorMessage = ex.getCause() != null ? ex.getCause().getMessage() : ex.getMessage();

      response =
          constructResponse(
              response,
              Constants.FAILURE,
              Constants.ERROR_YBL_EDIT_BENEFICIARY,
              errorMessage,
              Constants.YBL_BENEFICIARY_DELETED_UNSUCCESSFULLY);
    }
    return new ResponseEntity<>(response, responseStatus);
  }

  @RequestMapping(value = "/v1/updateBankingBeneficiary", method = RequestMethod.PUT)
  public ResponseEntity<BaseResponse> updateBeneficiaryDetails(
      HttpServletRequest httpRequest,
      @RequestBody JSONObject<BeneficiaryRequest> beneficiaryRequest) {

    BaseResponse response = new BeneficiaryResponse();
    HttpStatus responseStatus;

    try {

      BeneficiaryRequest requestData = beneficiaryRequest.getData();

      logger.info(
          ":::Request Payload:::" + generateRequestAndResponseLogPaylod(beneficiaryRequest));
      setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));

      switch (requestData.getType()) {
        case YesBankConstants.OTHER_BENEFICIARY:
          YesBankBeneficiaryVo beneficiaryVo =
              entityConverter.convertToEntity(requestData, YesBankBeneficiaryVo.class);
          logger.info("::: Beneficiary Details :::" + beneficiaryVo);

          if (Objects.nonNull(beneficiaryVo)) {
            yesBankBeneficiaryService.updateBankingBeneficiary(beneficiaryVo);
          }
          break;
        case YesBankConstants.BENEFICIARY_TYPE_VENDOR:
        case YesBankConstants.BENEFICIARY_TYPE_EMPLOYEE:
          VendorBankDetailsVo vendorBankDetailsVo =
              entityConverter.convertToEntity(requestData, VendorBankDetailsVo.class);
          logger.info("::: Vendor Beneficiary Details :::" + vendorBankDetailsVo);

          if (Objects.nonNull(vendorBankDetailsVo)) {
            if (Objects.equals(requestData.getType(), YesBankConstants.BENEFICIARY_TYPE_VENDOR)) {
              yesBankBeneficiaryService.updateVendorBeneficiary(vendorBankDetailsVo);
            } else if (Objects.equals(
                requestData.getType(), YesBankConstants.BENEFICIARY_TYPE_EMPLOYEE)) {
              yesBankBeneficiaryService.updateEmployeeBeneficiary(vendorBankDetailsVo);
            }
          }
          break;
        case YesBankConstants.BENEFICIARY_TYPE_CUSTOMER:
          CustomerBankDetailsVo customerBankDetailsVo =
              entityConverter.convertToEntity(requestData, CustomerBankDetailsVo.class);
          logger.info("::: Customer Beneficiary Details :::" + customerBankDetailsVo);

          if (Objects.nonNull(customerBankDetailsVo)) {
            yesBankBeneficiaryService.updateCustomerBeneficiary(customerBankDetailsVo);
          }
          break;
        case YesBankConstants.BENEFICIARY_TYPE_BANK_MASTER:
          BankMasterAccountVo bankMasterAccountVo =
              yesBankIntegrationHelperService.convertBankMasterAccountsVoFromRequest(requestData);
          logger.info("::: Customer Beneficiary Details :::" + bankMasterAccountVo);
          yesBankBeneficiaryService.updateBankMaster(bankMasterAccountVo);
          break;
        default:
          throw new ApplicationException(Constants.ERROR_YBL_BENEFICIARY_TYPE);
      }

      responseStatus = HttpStatus.OK;
      response =
          constructResponse(
              response,
              Constants.SUCCESS,
              Constants.SUCCESS_YBL_UPDATE_BENEFICIARY,
              Constants.SUCCESS_YBL_UPDATE_BENEFICIARY,
              Constants.YBL_BENEFICIARY_UPDATED_SUCCESSFULLY);

    } catch (Exception ex) {
      logger.error(":::Exception in updateBeneficiary:::" + ex);
      responseStatus = HttpStatus.INTERNAL_SERVER_ERROR;
      String errorMessage = ex.getCause() != null ? ex.getCause().getMessage() : ex.getMessage();

      response =
          constructResponse(
              response,
              Constants.FAILURE,
              Constants.ERROR_YBL_EDIT_BENEFICIARY,
              errorMessage,
              Constants.YBL_BENEFICIARY_UPDATED_UNSUCCESSFULLY);
    }
    return new ResponseEntity<>(response, responseStatus);
  }
}

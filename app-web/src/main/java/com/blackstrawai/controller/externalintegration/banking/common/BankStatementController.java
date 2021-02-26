package com.blackstrawai.controller.externalintegration.banking.common;

import com.blackstrawai.common.BaseController;
import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.common.Constants;
import com.blackstrawai.externalintegration.banking.common.BankStatementService;
import com.blackstrawai.externalintegration.banking.perfios.PerfiosService;
import com.blackstrawai.externalintegration.yesbank.statement.BankStatementDetailsResponseVo;
import com.blackstrawai.externalintegration.yesbank.statement.YesBankStatementVo;
import com.blackstrawai.helper.YesBankIntegrationHelperService;
import com.blackstrawai.report.ReportPeriodDatesVo;
import com.blackstrawai.report.ReportsService;
import com.blackstrawai.request.JSONObject;
import com.blackstrawai.request.externalintegration.banking.common.BankStatementFilterRequest;
import com.blackstrawai.request.externalintegration.banking.common.BankStatementRequest;
import com.blackstrawai.response.externalintegration.banking.common.BankStatementResponse;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@RestController
@CrossOrigin
@RequestMapping("/decifer/bank_statement")
public class BankStatementController extends BaseController {

  private final Logger logger = Logger.getLogger(BankStatementController.class);

  private final BankStatementService bankStatementService;
  private final YesBankIntegrationHelperService yesBankIntegrationHelperService;
  // private final EntityConverter entityConverter;
  private final PerfiosService perfiosService;
  private final ReportsService reportsService;

  public BankStatementController(
      BankStatementService bankStatementService,
      YesBankIntegrationHelperService yesBankIntegrationHelperService,
      PerfiosService perfiosService,
      ReportsService reportsService) {
    this.bankStatementService = bankStatementService;
    this.yesBankIntegrationHelperService = yesBankIntegrationHelperService;
    this.perfiosService = perfiosService;
    this.reportsService = reportsService;
  }

  @RequestMapping(value = "/v1/adhoc_statement", method = RequestMethod.POST)
  public ResponseEntity<BaseResponse> adhocStatement(
      HttpServletRequest httpRequest,
      @RequestBody JSONObject<BankStatementRequest> bankStatementRequest) {
    HttpStatus status = HttpStatus.OK;
    BaseResponse response = new BankStatementResponse();

    try {

      setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));

      BankStatementRequest requestData = bankStatementRequest.getData();
      YesBankStatementVo yesBankStatementVo =
          yesBankIntegrationHelperService.convertRequestToBankStatementVo(requestData);
      if (requestData != null && requestData.getType() != null) {
        switch (requestData.getType()) {
          case "Yes Bank":
            if (Objects.nonNull(yesBankStatementVo)) {
              BankStatementDetailsResponseVo resBodyVo =
                  bankStatementService.getBankStatement(
                      yesBankStatementVo,
                      bankStatementRequest.getData().getOrgId(),
                      bankStatementRequest.getData().getUserId(),
                      bankStatementRequest.getData().getCustomerId(),
                      bankStatementRequest.getData().getAccountNo(),
                      bankStatementRequest.getData().getRoleName());

              if (Objects.nonNull(resBodyVo)) {

                ((BankStatementResponse) response).setData(resBodyVo);
                response =
                    constructResponse(
                        response,
                        Constants.SUCCESS,
                        Constants.SUCCESS_BANK_STATEMENT,
                        Constants.SUCCESS_BANK_STATEMENT,
                        Constants.SUCCESS_DURING_GET);
              } else {
                response =
                    constructResponse(
                        response,
                        Constants.SUCCESS,
                        Constants.NO_BANK_STATEMENT,
                        Constants.NO_BANK_STATEMENT,
                        Constants.SUCCESS_DURING_GET);
              }
            }
            break;
          case "Perfios":
            BankStatementDetailsResponseVo resBodyVo =
                perfiosService.getStatementForAccount(
                    Integer.parseInt(requestData.getOrgId() != null ? requestData.getOrgId() : "0"),
                    requestData.getUserId(),
                    requestData.getRoleName(),
                    requestData.getAccountName(),
                    requestData.getSearchParam());
            if (Objects.nonNull(resBodyVo)) {

              ((BankStatementResponse) response).setData(resBodyVo);
              response =
                  constructResponse(
                      response,
                      Constants.SUCCESS,
                      Constants.SUCCESS_BANK_STATEMENT,
                      Constants.SUCCESS_BANK_STATEMENT,
                      Constants.SUCCESS_DURING_GET);
            } else {
              response =
                  constructResponse(
                      response,
                      Constants.SUCCESS,
                      Constants.NO_BANK_STATEMENT,
                      Constants.NO_BANK_STATEMENT,
                      Constants.SUCCESS_DURING_GET);
            }
            break;
        }
      }

    } catch (Exception ex) {
      logger.error("Exception in fetching bank statement" + ex);
      response =
          constructResponse(
              response,
              Constants.FAILURE,
              Constants.FAILURE_BANK_STATEMENT,
              ex.getMessage() != null ? ex.getMessage() : Constants.FAILURE_BANK_STATEMENT,
              Constants.FAILURE_DURING_GET);
      status = org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
    }
    return new ResponseEntity<>(response, status);
  }

  @RequestMapping(value = "/v1/refresh", method = RequestMethod.POST)
  public ResponseEntity<BaseResponse> refresh(
      HttpServletRequest httpRequest,
      @RequestBody JSONObject<BankStatementRequest> bankStatementRequest) {
    HttpStatus status = HttpStatus.OK;
    BaseResponse response = new BankStatementResponse();

    try {
      setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
      BankStatementRequest requestData = bankStatementRequest.getData();
      if (requestData != null && requestData.getType() != null) {
        switch (requestData.getType()) {
          case "Yes Bank":
            break;
          case "Perfios":
            perfiosService.refresh(
                Integer.parseInt(requestData.getOrgId()),
                Integer.parseInt(requestData.getUserId()),
                requestData.getRoleName(),
                requestData.getAccountName());
            ((BankStatementResponse) response).setData(null);
            break;
        }
        response =
            constructResponse(
                response,
                Constants.SUCCESS,
                Constants.SUCCESS_BANK_STATEMENT_REFRESH,
                Constants.SUCCESS_BANK_STATEMENT_REFRESH,
                Constants.BANK_STATEMENT_REFRESHED_SUCCESSFULLY);
      }

    } catch (Exception ex) {
      logger.error("Exception in refreshing bank statement" + ex);
      response =
          constructResponse(
              response,
              Constants.FAILURE,
              ex.getMessage() != null ? ex.getMessage() : Constants.FAILURE_BANK_STATEMENT_REFRESH,
              Constants.FAILURE_BANK_STATEMENT_REFRESH,
              Constants.BANK_STATEMENT_REFRESHED_UNSUCCESSFULLY);
      status = org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
    }
    return new ResponseEntity<>(response, status);
  }

  @RequestMapping(value = "/v1/filter", method = RequestMethod.POST)
  public ResponseEntity<BaseResponse> adhocStatementWithFilter(
      HttpServletRequest httpRequest,
      @RequestBody JSONObject<BankStatementFilterRequest> bankStatementFilterRequest) {
    HttpStatus status = HttpStatus.OK;
    BaseResponse response = new BankStatementResponse();

    try {

      setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
      BankStatementDetailsResponseVo resBodyVo = null;
      BankStatementFilterRequest requestData = bankStatementFilterRequest.getData();
      if (requestData != null && requestData.getType() != null) {
        ReportPeriodDatesVo period = null;
        switch (requestData.getType()) {
          case "Yes Bank":
            if (requestData.getPeriodId() != 1) {
              period =
                  reportsService.getReportDates(
                      requestData.getStartDate(),
                      requestData.getEndDate(),
                      requestData.getPeriodId());
            }

            resBodyVo =
                bankStatementService.getStatementForAccountWithFilter(
                    requestData.getOrgId(),
                    requestData.getUserId(),
                    requestData.getRoleName(),
                    requestData.getCustomerId(),
                    requestData.getAccountNo(),
                    period,
                    requestData.isAll(),
                    requestData.isCr(),
                    requestData.isDr());
            break;
          case "Perfios":
            if (requestData.getPeriodId() == 1) {
              String searchParam = "All";
              if (requestData.isCr()) {
                searchParam = "Cr";
              }
              if (requestData.isDr()) {
                searchParam = "Dr";
              }
              resBodyVo =
                  perfiosService.getStatementForAccount(
                      Integer.parseInt(
                          requestData.getOrgId() != null ? requestData.getOrgId() : "0"),
                      requestData.getUserId(),
                      requestData.getRoleName(),
                      requestData.getAccountName(),
                      searchParam);
            } else {
              period =
                  reportsService.getReportDates(
                      requestData.getStartDate(),
                      requestData.getEndDate(),
                      requestData.getPeriodId());
              resBodyVo =
                  perfiosService.getStatementForAccountWithFilter(
                      Integer.parseInt(
                          requestData.getOrgId() != null ? requestData.getOrgId() : "0"),
                      requestData.getUserId(),
                      requestData.getRoleName(),
                      requestData.getAccountName(),
                      period,
                      requestData.isAll(),
                      requestData.isCr(),
                      requestData.isDr());
            }

            break;
        }
      }

      if (Objects.nonNull(resBodyVo)) {

        ((BankStatementResponse) response).setData(resBodyVo);
        response =
            constructResponse(
                response,
                Constants.SUCCESS,
                Constants.SUCCESS_BANK_STATEMENT,
                Constants.SUCCESS_BANK_STATEMENT,
                Constants.SUCCESS_DURING_GET);
      } else {
        response =
            constructResponse(
                response,
                Constants.SUCCESS,
                Constants.NO_BANK_STATEMENT,
                Constants.NO_BANK_STATEMENT,
                Constants.SUCCESS_DURING_GET);
      }

    } catch (Exception ex) {
      logger.error("Exception in fetching bank statement", ex);
      response =
          constructResponse(
              response,
              Constants.FAILURE,
              ex.getMessage() != null ? ex.getMessage() : Constants.FAILURE_BANK_STATEMENT,
              Constants.FAILURE_BANK_STATEMENT,
              Constants.FAILURE_DURING_GET);
      status = org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
    }
    return new ResponseEntity<>(response, status);
  }
}

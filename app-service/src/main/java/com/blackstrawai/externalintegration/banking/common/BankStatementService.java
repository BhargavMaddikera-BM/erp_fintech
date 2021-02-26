package com.blackstrawai.externalintegration.banking.common;

import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.export.ExportHelperService;
import com.blackstrawai.externalintegration.banking.perfios.RecentTransactionVo;
import com.blackstrawai.externalintegration.banking.yesbank.YesBankConstants;
import com.blackstrawai.externalintegration.banking.yesbank.YesBankIntegrationDao;
import com.blackstrawai.externalintegration.yesbank.Response.YesBankAuthorizationVo;
import com.blackstrawai.externalintegration.yesbank.statement.BankStatementDetailsResponseVo;
import com.blackstrawai.externalintegration.yesbank.statement.BankStatementInfoVo;
import com.blackstrawai.externalintegration.yesbank.statement.BankStatementResponseVo;
import com.blackstrawai.externalintegration.yesbank.statement.ReqBodyVo;
import com.blackstrawai.externalintegration.yesbank.statement.ServiceContextVo;
import com.blackstrawai.externalintegration.yesbank.statement.StatementVo;
import com.blackstrawai.externalintegration.yesbank.statement.YesBankStatementVo;
import com.blackstrawai.report.ReportPeriodDatesVo;
import com.blackstrawai.yesbank.YesBank;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class BankStatementService {

  private static final Logger logger = Logger.getLogger(BankStatementService.class);

  private final BankStatementDao bankStatementDao;
  private final YesBankIntegrationDao yesBankIntegrationDao;
  private final ObjectMapper objectMapper = new ObjectMapper();
  private final ExportHelperService exportHelperService;

  public BankStatementService(
      BankStatementDao bankStatementDao,
      YesBankIntegrationDao yesBankIntegrationDao,
      ExportHelperService exportHelperService) {
    this.bankStatementDao = bankStatementDao;
    this.yesBankIntegrationDao = yesBankIntegrationDao;
    this.exportHelperService = exportHelperService;
  }

  public List<RecentTransactionVo> getRecentTransactions(
      int organizationId, String userId, String customerId, String accountNo, String roleName)
      throws ApplicationException {

    return bankStatementDao.getRecentTransactions(
        organizationId, customerId, accountNo, userId, roleName);
  }

  public BankStatementDetailsResponseVo getBankStatement(
      YesBankStatementVo yesBankStatementVo,
      String organizationId,
      String userId,
      String customerId,
      String accountNo,String roleName)
      throws ApplicationException, IOException, ParseException {

    BankStatementDetailsResponseVo response = null;

    try {

      boolean result =
          bankStatementDao.checkIfStatementAvailable(organizationId, customerId, accountNo);

      logger.info("Is statement already available::::" + result);

      if (result) {
        response = bankStatementDao.getAvailableStatement(organizationId, customerId, accountNo);
      } else {

        YesBankAuthorizationVo authorization =
                yesBankIntegrationDao.getCustomerAuthorization(organizationId, customerId, accountNo,userId,roleName);

        if (Objects.isNull(authorization)) {
          throw new ApplicationException(YesBankConstants.ERROR_YBL_CUST_INFO_NOT_AVAIALBLE);
        }

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -18);

        String startDate;
        String endDate;
        String finalJsonRequest;
        SimpleDateFormat inputFormat = new SimpleDateFormat("dd-MM-yy");
        String currentStrDate = inputFormat.format(Calendar.getInstance().getTime());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-dd-MM'T'HH:mm:ss");

        List<BankStatementInfoVo> statementInfoVoList = new ArrayList<>();
        for (int i = 0; i < 6; i++) {

          startDate = inputFormat.format(cal.getTime());
          cal.add(Calendar.MONTH, 3);
          cal.add(Calendar.DATE, -1);
          endDate = inputFormat.format(cal.getTime());
          if (inputFormat.parse(endDate).after(inputFormat.parse(currentStrDate))) {
            endDate = currentStrDate;
          }

          ServiceContextVo serviceContextVo =
              yesBankStatementVo.getAdhocStatementReq().getReqHdr().getServiceContext();
          ReqBodyVo reqBodyVo = yesBankStatementVo.getAdhocStatementReq().getReqBody();
          serviceContextVo.setReqRefNum(generateRequestRefNo());
          serviceContextVo.setReqRefTimeStamp(
              dateFormat.format(new Date(System.currentTimeMillis())));
          reqBodyVo.setTxnStartDate(startDate);
          reqBodyVo.setTxnEndDate(endDate);

          logger.info(
              "startDate::"
                  + reqBodyVo.getTxnStartDate()
                  + ":::endDate::::"
                  + reqBodyVo.getTxnEndDate()
                  + ":::req ref no:::"
                  + serviceContextVo.getReqRefNum()
                  + ":::req ref timestamp:::"
                  + serviceContextVo.getReqRefTimeStamp());

          finalJsonRequest = objectMapper.writeValueAsString(yesBankStatementVo);
          
          logger.info("Bank Statement:Request Body is:"+finalJsonRequest);
          
          String responseJson =YesBank.getInstance().getSingleResponseEntity("bankStatement", finalJsonRequest, authorization);

          BankStatementResponseVo bankStatementResponseVo;
          if (Objects.nonNull(responseJson)) {

            bankStatementResponseVo =
                objectMapper.readValue(responseJson, BankStatementResponseVo.class);
            logger.info("<<<<payment Transfer Response >>>>" + bankStatementResponseVo);

            if (Objects.equals(
                bankStatementResponseVo.getAdhocStatementRes().getResBody().getErrorCode(), 0)) {
              bankStatementDao.saveBankStatement(
                  organizationId, userId, startDate, endDate, bankStatementResponseVo);

              response = new BankStatementDetailsResponseVo();
              response.setCustomerId(
                  bankStatementResponseVo.getAdhocStatementRes().getResBody().getCustomerId());
              response.setAccountNumber(
                  bankStatementResponseVo.getAdhocStatementRes().getResBody().getAccountNumber());

              BankStatementInfoVo bankStatementInfoVo;
              for (StatementVo statementVo :
                  bankStatementResponseVo.getAdhocStatementRes().getResBody().getStatement()) {
                bankStatementInfoVo = new BankStatementInfoVo();
                bankStatementInfoVo.setTxnDate(statementVo.getTxnDate());
                bankStatementInfoVo.setValueDate(statementVo.getValueDate());
                bankStatementInfoVo.setTxnDesc(statementVo.getTxnDesc());
                bankStatementInfoVo.setRefChqNum(
                    statementVo.getRefChqNum().equals("")
                        ? statementVo.getRefUsrNo()
                        : statementVo.getRefChqNum());
                bankStatementInfoVo.setAmtWithdrawal(statementVo.getAmtWithdrawal());
                bankStatementInfoVo.setAmtDeposit(statementVo.getAmtDeposit());
                bankStatementInfoVo.setBalance(statementVo.getBalance());
                statementInfoVoList.add(bankStatementInfoVo);
                logger.info("statementInfoVoList:::" + statementInfoVoList.size());
              }
            }else{
              logger.info("Error code:::"+bankStatementResponseVo.getAdhocStatementRes().getResBody().getErrorCode());
              throw new ApplicationException(bankStatementResponseVo.getAdhocStatementRes().getResBody().getErrorReason());
            }
          }
        }
        response.setStatement(statementInfoVoList);
      }

    } catch (Exception ex) {
      throw ex;
    }
    return response;
  }

  private String generateRequestRefNo() {
    Random objGenerator = new Random();
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("ddMMyyyyHHmm");
    LocalDateTime localDate = LocalDateTime.now();

    int sequenceNo = objGenerator.nextInt(100000);
    String requestRefNo = dtf.format(localDate) + sequenceNo;
    return requestRefNo;
  }

  public String exportBankingBankStatement(List<BankStatementInfoVo> statementList, String filetype)
      throws ApplicationException {
    return exportHelperService.exportBankingBankStatement(statementList, filetype);
  }

  public BankStatementDetailsResponseVo getStatementForAccountWithFilter(
      String organizationId,
      String userId,
      String roleName,
      String customerId,
      String accountNo,
      ReportPeriodDatesVo reportPeriodDatesVo,
      boolean isAll,
      boolean isCr,
      boolean isDr) throws ApplicationException {
    return bankStatementDao.getStatementForAccountWithFilter(
        organizationId,
        userId,
        roleName,
        customerId,
        accountNo,
        reportPeriodDatesVo,
        isAll,
        isCr,
        isDr);
  }


}

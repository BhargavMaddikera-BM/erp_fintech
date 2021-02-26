package com.blackstrawai.externalintegration.banking.common;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.common.BaseDao;
import com.blackstrawai.externalintegration.banking.perfios.RecentTransactionVo;
import com.blackstrawai.externalintegration.banking.yesbank.YesBankConstants;
import com.blackstrawai.externalintegration.yesbank.statement.BankStatementDetailsResponseVo;
import com.blackstrawai.externalintegration.yesbank.statement.BankStatementInfoVo;
import com.blackstrawai.externalintegration.yesbank.statement.BankStatementResponseVo;
import com.blackstrawai.externalintegration.yesbank.statement.ResBodyVo;
import com.blackstrawai.report.ReportPeriodDatesVo;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class BankStatementDao extends BaseDao {

  private final Logger logger = Logger.getLogger(BankStatementDao.class);

  public boolean saveBankStatement(
      String organizationId,
      String userId,
      String txnStartDate,
      String txnEndDate,
      BankStatementResponseVo bankStatementResponseVo)
      throws ApplicationException {

    Connection connection = null;
    PreparedStatement preparedStatement = null;
    boolean result = false;

    try {

      ResBodyVo bankStatement = bankStatementResponseVo.getAdhocStatementRes().getResBody();

      connection = getBankingConnection();
      preparedStatement =
          connection.prepareStatement(
              YesBankConstants.SAVE_BANK_STATEMENT, Statement.RETURN_GENERATED_KEYS);
      preparedStatement.setString(1, String.valueOf(bankStatement.getCustomerId()));
      preparedStatement.setString(2, bankStatement.getAccountNumber());
      preparedStatement.setString(3, txnStartDate);
      preparedStatement.setString(4, txnEndDate);
      preparedStatement.setString(5, organizationId);
      preparedStatement.setDate(6, new Date(System.currentTimeMillis()));
      preparedStatement.setString(7, userId);

      int rowAffected = preparedStatement.executeUpdate();
      ResultSet rs;
      PreparedStatement finalPreparedStatement;
      if (rowAffected == 1) {
        rs = preparedStatement.getGeneratedKeys();
        int id = 0;
        if (rs.next()) {
          id = rs.getInt(1);
        }

        finalPreparedStatement =
            connection.prepareStatement(
                YesBankConstants.SAVE_BANK_STATEMENT_INFO, Statement.RETURN_GENERATED_KEYS);

        int finalId = id;
        bankStatement
            .getStatement()
            .forEach(
                statementVo -> {
                  try {
                    finalPreparedStatement.setString(1, String.valueOf(finalId));
                    finalPreparedStatement.setString(2, statementVo.getTxnDate());
                    finalPreparedStatement.setString(3, statementVo.getValueDate());
                    finalPreparedStatement.setString(4, statementVo.getTxnDesc().trim());
                    finalPreparedStatement.setString(
                        5,
                        statementVo.getRefChqNum().equals("")
                            ? statementVo.getRefUsrNo()
                            : statementVo.getRefChqNum());
                    finalPreparedStatement.setString(6, statementVo.getAmtWithdrawal());
                    finalPreparedStatement.setString(7, statementVo.getAmtDeposit());
                    finalPreparedStatement.setString(8, statementVo.getBalance());
                    finalPreparedStatement.setDate(9, new Date(System.currentTimeMillis()));
                    finalPreparedStatement.setString(10, userId);

                    finalPreparedStatement.executeUpdate();

                  } catch (Exception ex) {
                    logger.error("Exception while saving bank statement information>>>" + ex);
                  }
                });
        result = true;
        closeResources(rs, finalPreparedStatement, null);
      }

    } catch (Exception ex) {
      logger.error("Exception while saving bank statement >>>" + ex);
      throw new ApplicationException(ex);
    } finally {
      closeResources(null, preparedStatement, connection);
    }

    return result;
  }

  public boolean checkIfStatementAvailable(
      String organizationId, String customerId, String accountNo) throws ApplicationException {

    Connection con = null;
    PreparedStatement preparedStatement = null;
    ResultSet rs = null;
    boolean isStatementAvailable = false;

    try {
      con = getBankingConnection();
      preparedStatement = con.prepareStatement(YesBankConstants.CHECK_IF_BANK_STATEMENT_AVAILABLE);
      preparedStatement.setString(1, organizationId);
      preparedStatement.setString(2, customerId);
      preparedStatement.setString(3, accountNo);
      //  preparedStatement.setString(4, txnStartDate);
      //   preparedStatement.setString(5, txnEndDate);
      rs = preparedStatement.executeQuery();

      if (rs.next()) {
        if (rs.getInt(1) > 0) {
          isStatementAvailable = true;
        }
      }

    } catch (Exception e) {
      logger.info("Error in checkIfStatementAvailable  ", e);
      throw new ApplicationException(e);
    } finally {
      closeResources(rs, preparedStatement, con);
    }
    return isStatementAvailable;
  }

  public BankStatementDetailsResponseVo getAvailableStatement(
      String organizationId, String customerId, String accountNo)
      //  String txnStartDate,
      //  String txnEndDate)
      throws ApplicationException {

    Connection con = null;
    PreparedStatement preparedStatement = null;
    ResultSet rs = null;
    BankStatementDetailsResponseVo resBodyVo = null;
    try {
      con = getBankingConnection();
      preparedStatement = con.prepareStatement(YesBankConstants.GET_AVAILABLE_BANK_STATEMENT);
      preparedStatement.setString(1, organizationId);
      preparedStatement.setString(2, customerId);
      preparedStatement.setString(3, accountNo);
      // preparedStatement.setString(4, txnStartDate);
      // preparedStatement.setString(5, txnEndDate);
      rs = preparedStatement.executeQuery();

      List<BankStatementInfoVo> statementList = new ArrayList<>();
      while (rs.next()) {
        resBodyVo = new BankStatementDetailsResponseVo();

        resBodyVo.setCustomerId(Integer.parseInt(rs.getString(1)));
        resBodyVo.setAccountNumber(rs.getString(2));

        BankStatementInfoVo statementVo = new BankStatementInfoVo();
        statementVo.setTxnDate(rs.getString(3));
        statementVo.setValueDate(rs.getString(4));
        statementVo.setTxnDesc(rs.getString(5));
        statementVo.setRefChqNum(rs.getString(6));
        statementVo.setAmtWithdrawal(rs.getString(7));
        statementVo.setAmtDeposit(rs.getString(8));
        statementVo.setBalance(rs.getString(9));
        statementList.add(statementVo);
      }
      resBodyVo.setStatement(statementList);

    } catch (Exception e) {
      logger.info("Error in checkIfStatementAvailable  ", e);
      throw new ApplicationException(e);
    } finally {
      closeResources(rs, preparedStatement, con);
    }
    return resBodyVo;
  }

  public List<RecentTransactionVo> getRecentTransactions(
      int organizationId, String customerId, String accountNo, String userId, String roleName)
      throws ApplicationException {

    Connection con = null;
    PreparedStatement preparedStatement = null;
    ResultSet rs = null;
    List<RecentTransactionVo> recentTransactions = new ArrayList<RecentTransactionVo>();
    try {
      con = getBankingConnection();
      preparedStatement = con.prepareStatement(YesBankConstants.GET_RECENT_TRANSACTIONS);
      preparedStatement.setInt(1, organizationId);
      preparedStatement.setString(2, customerId);
      preparedStatement.setString(3, accountNo);
      preparedStatement.setString(4, userId);
      preparedStatement.setString(5, roleName);
      rs = preparedStatement.executeQuery();

      while (rs.next()) {
        RecentTransactionVo recentTransactionVo = new RecentTransactionVo();
        recentTransactionVo.setDescription(rs.getString(1));
        Double debit = rs.getDouble(2);
        Double credit = rs.getDouble(3);
        Double amount = 0.00;
        String type = "";
        if (debit != null && (debit < 0 || debit > 0)) {
          type = "Debit";
          amount = debit;
          if (debit < 0) {
            amount *= -1;
          }
        }
        if (credit != null && credit > 0) {
          type = "Credit";
          amount = credit;
        }
        recentTransactionVo.setType(type);
        recentTransactionVo.setAmount(amount);
        recentTransactions.add(recentTransactionVo);
      }

    } catch (Exception e) {
      logger.error("Error in getRecentTransactions  ", e);
      throw new ApplicationException(e);
    } finally {
      closeResources(rs, preparedStatement, con);
    }
    return recentTransactions;
  }

  public BankStatementDetailsResponseVo getStatementForAccountWithFilter(
      String organizationId,
      String userId,
      String roleName,
      String customerId,
      String accountNo,
      ReportPeriodDatesVo vo,
      boolean isAll,
      boolean isCr,
      boolean isDr)
      throws ApplicationException {

    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    BankStatementDetailsResponseVo bankStatementDetailsResponseVo =
        new BankStatementDetailsResponseVo();

    try {

      con = getBankingConnection();

      StringBuffer query = new StringBuffer(YesBankConstants.GET_ACCOUNT_STATEMENT_WITH_FILTER);

      /*String startDate = DateConverter.getInstance().convertTimestampToDate("dd-MM-yy","yyyy-MM-dd",vo.getStartDate());
      String endDate = DateConverter.getInstance().convertTimestampToDate("dd-MM-yy","yyyy-MM-dd",vo.getEndDate());*/

      if (vo != null) {
        String startDate = vo.getStartDate();
        String endDate = vo.getEndDate();

        if (startDate != null && endDate != null) {
          startDate = startDate + " 00:00:00";
          endDate = endDate + " 23:59:00";
          query.append(" AND ybsi.txn_date BETWEEN '" + startDate + "' and '" + endDate + "'");
        }
      }

      query.append(" ORDER BY ybsi.txn_date DESC");
      ps = con.prepareStatement(query.toString());
      ps.setString(1, organizationId);
      ps.setString(2, customerId);
      ps.setString(3, accountNo);
      ps.setString(4, userId);
      ps.setString(5, roleName);

      logger.info("Query:" + ps.toString());
      rs = ps.executeQuery();

      BankStatementInfoVo bankStatementInfoVo;
      List<BankStatementInfoVo> statement = new ArrayList<>();

      while (rs.next()) {
        bankStatementInfoVo = new BankStatementInfoVo();

        bankStatementDetailsResponseVo.setCustomerId(rs.getInt(1));
        bankStatementDetailsResponseVo.setAccountNumber(rs.getString(2));

        bankStatementInfoVo.setTxnDate(rs.getString(3));
        bankStatementInfoVo.setValueDate(rs.getString(4));
        bankStatementInfoVo.setTxnDesc(rs.getString(5));
        bankStatementInfoVo.setRefChqNum(rs.getString(6));
        bankStatementInfoVo.setAmtDeposit(rs.getString(7));
        bankStatementInfoVo.setAmtWithdrawal(rs.getString(8));
        bankStatementInfoVo.setBalance(rs.getString(9));

        if (isAll) {
          statement.add(bankStatementInfoVo);
        }
        if (isCr) {
          if (Double.parseDouble(bankStatementInfoVo.getAmtDeposit()) > 0) {
            statement.add(bankStatementInfoVo);
          }
        }
        if (isDr) {
          if (Double.parseDouble(bankStatementInfoVo.getAmtWithdrawal()) > 0) {
            statement.add(bankStatementInfoVo);
          }
        }
      }
      bankStatementDetailsResponseVo.setStatement(statement);
    } catch (Exception e) {
      logger.error("Error while fetching getStatementForAccount:", e);
      throw new ApplicationException("Error during method getStatementForAccount: ", e);
    } finally {
      closeResources(rs, ps, con);
    }

    return bankStatementDetailsResponseVo;
  }
}

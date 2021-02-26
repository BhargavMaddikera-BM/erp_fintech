package com.blackstrawai.externalintegration.banking.perfios;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.DeciferAES256;
import com.blackstrawai.common.BaseDao;
import com.blackstrawai.externalintegration.banking.common.BasicAccountDetailsVo;
import com.blackstrawai.externalintegration.banking.common.DashboardDao;
import com.blackstrawai.externalintegration.banking.yesbank.YesBankConstants;
import com.blackstrawai.externalintegration.yesbank.statement.BankStatementDetailsResponseVo;
import com.blackstrawai.externalintegration.yesbank.statement.BankStatementInfoVo;
import com.blackstrawai.report.ReportPeriodDatesVo;

@Repository
public class PerfiosDao extends BaseDao {

	@Autowired
	private DashboardDao dashboardDao;
	
	private Logger logger = Logger.getLogger(PerfiosDao.class);

	public Integer createUser(int userId, int organizationId, String roleName) throws ApplicationException {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = getUserMgmConnection();
			ps = con.prepareStatement(PerfiosConstants.CREATE_USER, Statement.RETURN_GENERATED_KEYS);
			ps.setInt(1, userId);
			ps.setString(2, roleName);
			ps.setInt(3, organizationId);
			ps.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
			int rowsAffected = ps.executeUpdate();
			if (rowsAffected == 1) {
				rs = ps.getGeneratedKeys();
				if (rs.next()) {
					return rs.getInt(1);
				}
			}
			return 0;
		} catch (Exception e) {
			throw new ApplicationException("Error during method createUser: ", e);
		} finally {
			closeResources(rs, ps, con);
		}
	}

	public String getAvailableBalanceForAccount(int organizationId,String userId,String roleName,String accountName) throws ApplicationException {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String balance="";
		try {
			String hashValue=getPerfiosId(Integer.parseInt(userId),organizationId,roleName);
			con = getBankingConnection();
			ps = con.prepareStatement(PerfiosConstants.GET_ACCOUNT_BALANCE);
			ps.setString(1, hashValue);
			ps.setString(2, accountName);
			rs = ps.executeQuery();
			if (rs.next()) {
				if(rs.getString(1)!=null) {
					balance=DeciferAES256.decrypt_banking(rs.getString(1),"perfios_key");
				}
			}
		} catch (Exception e) {
			logger.error("Error while fetching availble balance:",e);
			throw new ApplicationException("Error during method getAvailableBalanceForAccount: ", e);
		} finally {
			closeResources(rs, ps, con);
		}
		return balance;
	}

	public List<String> getListOfAccountId(String userId,String accountName) throws ApplicationException {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<String>accountIdList=new ArrayList<String>();
		try {
			con = getBankingConnection();
			ps = con.prepareStatement(PerfiosConstants.GET_LIST_OF_ACCOUNT_ID);
			ps.setString(1, userId);
			ps.setString(2, accountName);
			rs = ps.executeQuery();
			while(rs.next()){
				accountIdList.add(rs.getString(1));
			}
		} catch (Exception e) {
			logger.error("Error while fetching getListOfAccountId:",e);
			throw new ApplicationException("Error during method getListOfAccountId: ", e);
		} finally {
			closeResources(rs, ps, con);
		}
		return accountIdList;
	}

	public String getPerfiosId(int userId, int organizationId, String roleName) throws ApplicationException {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String hashValue = null;
		try {
			con = getUserMgmConnection();
			ps = con.prepareStatement(PerfiosConstants.FETCH_HASH_VALUE);
			ps.setInt(1, organizationId);
			ps.setInt(2, userId);
			ps.setString(3, roleName);
			rs = ps.executeQuery();
			while (rs.next()) {
				rs.getInt(1);
				hashValue = rs.getString(2);
			}

		} catch (Exception e) {
			throw new ApplicationException("Error during method getPerfiosId: ", e);
		} finally {
			closeResources(rs, ps, con);
		}
		return hashValue;
	}

	public boolean isUserExist(int userId, int organizationId, String roleName) throws ApplicationException {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = getUserMgmConnection();
			ps = con.prepareStatement(PerfiosConstants.IS_USER_EXISTS);
			ps.setInt(1, organizationId);
			ps.setInt(2, userId);
			ps.setString(3, roleName);
			rs = ps.executeQuery();
			while (rs.next()) {
				return true;
			}
			return false;
		} catch (Exception e) {
			throw new ApplicationException("Error during method isUserExist: ", e);
		} finally {
			closeResources(rs, ps, con);
		}
	}

	public void updateUser(int id, String hashValue) throws ApplicationException {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = getUserMgmConnection();
			ps = con.prepareStatement(PerfiosConstants.UPDATE_USER);
			ps.setString(1, hashValue);
			ps.setInt(2, id);
			ps.executeUpdate();

		} catch (Exception e) {
			throw new ApplicationException("Error during method updateUser: ", e);
		} finally {
			closeResources(rs, ps, con);
		}
	}


	public void createOrUpdateUserAccountTransaction(List<PerfiosUserAccountTransactionVo> perfiosList, String uniqueUserId)
			throws ApplicationException {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = getUserMgmConnection();
			con.setAutoCommit(false);
			PerfiosUserVo user = getUserInfo(uniqueUserId, con);
			if (user != null) {
				for (PerfiosUserAccountTransactionVo perfios : perfiosList) {				
					boolean recordExist=checkUserTransactionExist(uniqueUserId,perfios.getAccountId(),perfios.getTxnSeqId());
					String str=perfios.getXnDate();
					int val=str.indexOf("-");
					String year=str.substring(0,val);
					String month=str.substring(str.indexOf("-")+1,str.lastIndexOf("-"));
					String day=str.substring(str.lastIndexOf("-")+1,str.length());
					Date txnDate=new Date(Integer.parseInt(year)-1900,Integer.parseInt(month)-1,Integer.parseInt(day));					
					if(recordExist){
						ps = con.prepareStatement(PerfiosConstants.UPDATE_USER_ACCOUNT_TRANSACTION);	
						ps.setString(1, perfios.getXnDate());
						if(perfios.getXnDetails()!=null){
							ps.setString(2, DeciferAES256.encrypt_banking(perfios.getXnDetails(),"perfios_key"));
						}else{
							ps.setString(2, perfios.getXnDetails());
						}
						if(perfios.getChequeNum()!=null){
							ps.setString(3, DeciferAES256.encrypt_banking(perfios.getChequeNum(),"perfios_key"));
						}else{
							ps.setString(3, perfios.getChequeNum());
						}
						if(perfios.getXnAmount()!=null){
							ps.setString(4, DeciferAES256.encrypt_banking(perfios.getXnAmount(),"perfios_key"));
						}else{
							ps.setString(4, perfios.getXnAmount());
						}
						if(perfios.getBalance()!=null){
							ps.setString(5, DeciferAES256.encrypt_banking(perfios.getBalance(),"perfios_key"));
						}else{
							ps.setString(5,perfios.getBalance());
						}		
						ps.setString(6,perfios.getCategoryId());
						ps.setString(7, perfios.getUserComment());
						ps.setString(8, perfios.getSplitRefId());
						ps.setString(9, perfios.getXnId());
						ps.setString(10,perfios.getCategory());
						ps.setString(11,perfios.getiType());
						/*if(perfios.getAccountName()!=null){
							ps.setString(12,DeciferAES256.encrypt_banking(perfios.getAccountName(),"perfios_key"));
						}else{
							ps.setString(12,perfios.getAccountName());
						}*/
						ps.setString(12,perfios.getAccountName());
						ps.setString(13,perfios.getCurrency());
						ps.setDate(14, txnDate);
						ps.setString(15, uniqueUserId);
						ps.setString(16, perfios.getAccountId());
						ps.setString(17, perfios.getTxnSeqId());
						ps.executeUpdate();


					}else{
						ps = con.prepareStatement(PerfiosConstants.CREATE_USER_ACCOUNT_TRANSACTION,Statement.RETURN_GENERATED_KEYS);						
						ps.setString(1, uniqueUserId);
						ps.setString(2,perfios.getTxnSeqId());
						ps.setString(3, perfios.getXnDate());
						if(perfios.getXnDetails()!=null){
							ps.setString(4, DeciferAES256.encrypt_banking(perfios.getXnDetails(),"perfios_key"));
						}else{
							ps.setString(4, perfios.getXnDetails());
						}

						if(perfios.getChequeNum()!=null){
							ps.setString(5, DeciferAES256.encrypt_banking(perfios.getChequeNum(),"perfios_key"));
						}else{
							ps.setString(5, perfios.getChequeNum());
						}

						if(perfios.getXnAmount()!=null){
							ps.setString(6, DeciferAES256.encrypt_banking(perfios.getXnAmount(),"perfios_key"));
						}else{
							ps.setString(6, perfios.getXnAmount());
						}						

						if(perfios.getBalance()!=null){
							ps.setString(7, DeciferAES256.encrypt_banking(perfios.getBalance(),"perfios_key"));
						}else{
							ps.setString(7, perfios.getBalance());
						}					
						ps.setString(8, perfios.getCategoryId());
						ps.setString(9, perfios.getUserComment());
						ps.setString(10,perfios.getSplitRefId());
						ps.setString(11,perfios.getXnId());
						ps.setString(12,perfios.getCategory());
						ps.setString(13,perfios.getiType());
						/*if(perfios.getAccountName()!=null){
							ps.setString(14,DeciferAES256.encrypt_banking(perfios.getAccountName(),"perfios_key"));
						}else{
							ps.setString(14,perfios.getAccountName());
						}*/
						ps.setString(14,perfios.getAccountName());
						ps.setLong(15,perfios.getInstId());
						ps.setString(16,perfios.getAccountId());
						if(perfios.getInstName()!=null){
							ps.setString(17,DeciferAES256.encrypt_banking(perfios.getInstName(),"perfios_key"));
						}else{
							ps.setString(17,perfios.getInstName());
						}
						ps.setString(18,perfios.getCurrency());
						ps.setDate(19, txnDate);
						ps.executeUpdate();
					}
				}
			}
			con.commit();

		} catch (Exception e) {
			try {
				con.rollback();
			} catch (SQLException e1) {
				throw new ApplicationException(e1);
			}
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, ps, con);
		}
	}

	private PerfiosUserVo getUserInfo(String uniqueUserId, Connection con) throws ApplicationException {
		PerfiosUserVo user = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = con.prepareStatement(PerfiosConstants.GET_USER_INFO);
			ps.setString(1, uniqueUserId);
			rs = ps.executeQuery();
			while(rs.next()) {
				user = new PerfiosUserVo();
				user.setId(rs.getInt(1));
				user.setOrgId(rs.getInt(2));
				user.setUserId(rs.getInt(3));
				user.setRoleName(rs.getString(4));
			}
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, ps, null);
		}
		return user;
	}

	public List<BasicAccountDetailsVo> getAllAccountNamesForUserRole(int orgId,int userId,String roleName) throws ApplicationException {

		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection con=null;
		List<BasicAccountDetailsVo> accounts=new ArrayList<BasicAccountDetailsVo>();
		try {
			con = getBankingConnection();
			Map<String,Integer>dataMap=new HashMap<String,Integer>();
			Map<String,String>instMap=new HashMap<String,String>();

			ps = con.prepareStatement(PerfiosConstants.GET_ACCOUNT_NAMES_FOR_USER_ROLE);
			ps.setInt(1, orgId);
			ps.setInt(2, userId);
			ps.setString(3, roleName);
			rs = ps.executeQuery();
			logger.info("ps::"+ps.toString());
			while(rs.next()) {
				//dataMap.put(DeciferAES256.decrypt_banking(rs.getString(2),"perfios_key"),rs.getInt(1));
				dataMap.put(rs.getString(2),rs.getInt(1));
				instMap.put(rs.getString(2),rs.getString(3));
			}
			logger.info("dataMap::"+dataMap.size());
			logger.info("accounts::"+instMap.size());

			@SuppressWarnings("rawtypes")
			Iterator it=dataMap.entrySet().iterator();
			while(it.hasNext()){
				@SuppressWarnings("rawtypes")
				Map.Entry entry=(Map.Entry)it.next();
				BasicAccountDetailsVo accountVo = new BasicAccountDetailsVo();
				String key=(String) entry.getKey();
				int value=(int) entry.getValue();
				accountVo.setId(value);
				accountVo.setValue(value);
				accountVo.setName(key);
				accountVo.setCutomerId(0);
				accountVo.setType("Perfios");
				accountVo.setAPIBankingEnabled(true);
				accountVo.setBankName(instMap!=null && instMap.containsKey(key) && instMap.get(key)!=null ? DeciferAES256.decrypt_banking(instMap.get(key),"perfios_key") : null);
				accountVo.setOrgId(orgId);
				accountVo.setUserId(userId);
				accountVo.setRoleName(roleName);
				accounts.add(accountVo);
			}
			dataMap.clear();
			instMap.clear();
			closeResources(rs, ps, null);
			ps = con.prepareStatement(YesBankConstants.GET_ACCOUNT_NAMES_FOR_USER_ROLE);
			ps.setInt(1, orgId);
			ps.setInt(2, userId);
			ps.setString(3, roleName);
			rs = ps.executeQuery();
			while(rs.next()) {
				BasicAccountDetailsVo accountVo = new BasicAccountDetailsVo();
				accountVo.setId(rs.getInt(1));
				accountVo.setValue(rs.getInt(1));
				accountVo.setCutomerId(rs.getInt(2));
				accountVo.setName(rs.getString(3));
				accountVo.setAccountNo(rs.getString(3));
				accountVo.setAPIBankingEnabled(rs.getString(4)!=null && "Y".equals(rs.getString(4)) ? true : false );
				accountVo.setType("Yes Bank");
				accountVo.setBankName("Yes Bank");
				accountVo.setOrgId(orgId);
				accountVo.setUserId(userId);
				accountVo.setRoleName(roleName);
				accounts.add(accountVo);

			}
			BasicAccountDetailsVo defaultAccount =dashboardDao.getDefaultAccountForOrg (orgId ,userId ,roleName);
			logger.info("defaultAccount"+defaultAccount);	
			if(defaultAccount!=null) {
					for(BasicAccountDetailsVo defaultAcc : accounts) {
						if(defaultAcc.getType()!=null  ) {
							if("Perfios".equals(defaultAccount.getType()) && defaultAcc.getName().equals(defaultAccount.getAccountNo())) {
								defaultAcc.setIsDefault(true);
							}else {
								if(defaultAccount.getAccountNo().equals( defaultAcc.getAccountNo())) {
									defaultAcc.setIsDefault(true);
								}
							}
						}
					}
					}else {
						if(!accounts.isEmpty())
						accounts.get(0).setIsDefault(true);
					}
			
					
		} catch (Exception e) {
			logger.error(e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, ps, con);
		}
		return accounts;
	}

	public List<RecentTransactionVo> getAccountRecentTransactions(String accountName,int orgId,String userId,String roleName) throws ApplicationException {

		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection con=null;
		List<RecentTransactionVo> accounts=new ArrayList<RecentTransactionVo>();
		try {
			con = getBankingConnection();

			String hashValue=getPerfiosId(Integer.parseInt(userId),orgId,roleName);

			ps = con.prepareStatement(PerfiosConstants.GET_RECENT_TRANSACTION_BY_ACCOUNT_NAME);
			//ps.setString(1, DeciferAES256.encrypt_banking(accountName,"perfios_key"));
			ps.setString(1,hashValue);
			ps.setString(2,accountName);

			rs = ps.executeQuery();
			while(rs.next()) {
				RecentTransactionVo txnVo = new RecentTransactionVo();
				txnVo.setDescription(DeciferAES256.decrypt_banking(rs.getString(1),"perfios_key"));
				double amount=Double.parseDouble(DeciferAES256.decrypt_banking(rs.getString(2),"perfios_key"));
				String type="";
				if(amount>0) {
					type="Credit";
				}else if(amount<0) {
					type="Debit";
				}
				txnVo.setType(type);
				amount*=-1;
				txnVo.setAmount(amount);
				accounts.add(txnVo);
			}
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, ps, con);
		}
		return accounts;
	}

	public boolean checkUserTransactionExist(String uniqueUserId, String accountId, String txnSeqId) throws ApplicationException {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = getUserMgmConnection();
			ps = con.prepareStatement(PerfiosConstants.CHECK_ACCOUNT_TXN_SEQ_ID);
			ps.setString(1, uniqueUserId);
			ps.setString(2, accountId);
			ps.setString(3, txnSeqId);
			rs = ps.executeQuery();
			while (rs.next()) {
				return true;
			}

		} catch (Exception e) {
			throw new ApplicationException("Error during method checkUserTransactionExist: ", e);
		} finally {
			closeResources(rs, ps, con);
		}
		return false;
	}
	public BankStatementDetailsResponseVo getStatementForAccount(int organizationId,String userId,String roleName,String accountName,String searchParam) throws ApplicationException {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		BankStatementDetailsResponseVo bankStatementDetailsResponseVo=new BankStatementDetailsResponseVo();
		bankStatementDetailsResponseVo.setAccountNumber(accountName);
		List<BankStatementInfoVo> statement=new ArrayList<BankStatementInfoVo>();
		Double amount=0.0;
		try {
			String hashValue=getPerfiosId(Integer.parseInt(userId),organizationId,roleName);
			con = getBankingConnection();
			ps = con.prepareStatement(PerfiosConstants.GET_ACCOUNT_STATEMENT);
			ps.setString(1, hashValue);
			ps.setString(2, accountName);
			rs = ps.executeQuery();
			while (rs.next()) {
				BankStatementInfoVo bankStatementInfoVo=new BankStatementInfoVo();
				bankStatementInfoVo.setTxnDate(rs.getString(1));
				if(rs.getString(2)!=null) {
					bankStatementInfoVo.setTxnDesc(DeciferAES256.decrypt_banking(rs.getString(2),"perfios_key"));
					//bankStatementInfoVo.setTxnDesc(rs.getString(2));
				}
				if (rs.getString(3) != null) {

					String amountStr = DeciferAES256.decrypt_banking(rs.getString(3), "perfios_key");
					amount = Double.parseDouble(amountStr);
					if (amount > 0) {
						bankStatementInfoVo.setAmtDeposit(amountStr);
					} else if (amount < 0) {
						if (amountStr != null && amountStr.contains("-")) {
							amountStr = amountStr.replaceAll("-", "");
						}
						bankStatementInfoVo.setAmtWithdrawal(amountStr);
					}

				}
				bankStatementInfoVo.setRefChqNum(rs.getString(4));
				if(rs.getString(5)!=null) {
					bankStatementInfoVo.setBalance(DeciferAES256.decrypt_banking(rs.getString(5),"perfios_key"));
					//bankStatementInfoVo.setBalance(rs.getString(5));	
				}
				if(searchParam!=null) {
					switch (searchParam) {
					case "All":
						statement.add(bankStatementInfoVo);
						break;
					case "Cr":
						if (amount > 0) {
							statement.add(bankStatementInfoVo);
						}
						break;
					case "Dr":
						if (amount < 0) {
							statement.add(bankStatementInfoVo);
						}

						break;
					}
				
				}
			}
			bankStatementDetailsResponseVo.setStatement(statement);
		} catch (Exception e) {
			logger.error("Error while fetching getStatementForAccount:",e);
			throw new ApplicationException("Error during method getStatementForAccount: ", e);
		} finally {
			closeResources(rs, ps, con);
		}
		return bankStatementDetailsResponseVo;
	}

	public BankStatementDetailsResponseVo getStatementForAccountWithFilter(int organizationId,String userId, String roleName,String accountName,ReportPeriodDatesVo vo,boolean isAll,boolean isCr,boolean isDr) throws ApplicationException {
logger.info("Entry into getStatementForAccountWithFilter:"+isAll+isCr+isDr);
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		BankStatementDetailsResponseVo bankStatementDetailsResponseVo=new BankStatementDetailsResponseVo();
		bankStatementDetailsResponseVo.setAccountNumber(accountName);
		List<BankStatementInfoVo> statement=new ArrayList<BankStatementInfoVo>();
		Double amount=0.0;
		try {
			String hashValue=getPerfiosId(Integer.parseInt(userId),organizationId,roleName);
			con = getBankingConnection();
			StringBuffer query=new StringBuffer(PerfiosConstants.GET_ACCOUNT_STATEMENT_WITH_FILTER);
			String startDate=vo.getStartDate();
			String endDate=vo.getEndDate();
			if(vo!=null && startDate!=null && endDate!=null) {
				startDate=startDate+" 00:00:00";
				endDate=endDate+" 23:59:00";
				query.append(" AND txn_date BETWEEN '"+startDate+"' and '"+endDate+"'");
			}
			query.append(" ORDER BY txn_date DESC");
			ps = con.prepareStatement(query.toString());
			ps.setString(1, hashValue);
			ps.setString(2, accountName);
			logger.info("Query:"+ps.toString());
			rs = ps.executeQuery();
			
			while (rs.next()) {
				BankStatementInfoVo bankStatementInfoVo=new BankStatementInfoVo();
				bankStatementInfoVo.setTxnDate(rs.getString(1));
				if(rs.getString(2)!=null) {
					bankStatementInfoVo.setTxnDesc(DeciferAES256.decrypt_banking(rs.getString(2),"perfios_key"));
					//bankStatementInfoVo.setTxnDesc(rs.getString(2));
				}
				if(rs.getString(3)!=null) {
					String amountStr=DeciferAES256.decrypt_banking(rs.getString(3),"perfios_key");
					amount=Double.parseDouble(amountStr);
					if(amount>0) {
						bankStatementInfoVo.setAmtDeposit(amountStr);
					}else if(amount<0) {
						if(amountStr!=null && amountStr.contains("-")) {
							amountStr=amountStr.replaceAll("-", "");
						}
						bankStatementInfoVo.setAmtWithdrawal(amountStr);
					}

				}
				bankStatementInfoVo.setRefChqNum(rs.getString(4));
				if(rs.getString(5)!=null) {
					bankStatementInfoVo.setBalance(DeciferAES256.decrypt_banking(rs.getString(5),"perfios_key"));
					//bankStatementInfoVo.setBalance(rs.getString(5));	
				}
				if(isAll) {
				statement.add(bankStatementInfoVo);
				}
				if(isCr) {
					if(amount>0) {
						statement.add(bankStatementInfoVo);
					}
				}
				if(isDr) {
					if(amount<0) {
						statement.add(bankStatementInfoVo);
					}
				}
				
			}
			bankStatementDetailsResponseVo.setStatement(statement);
		} catch (Exception e) {
			logger.error("Error while fetching getStatementForAccount:",e);
			throw new ApplicationException("Error during method getStatementForAccount: ", e);
		} finally {
			closeResources(rs, ps, con);
		}
		return bankStatementDetailsResponseVo;
	}
	
	
	
	/*public void deleteUser(String uniqueUserId) throws ApplicationException {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = getUserMgmConnection();
			ps = con.prepareStatement(PerfiosConstants.UPDATE_STATUS);
			ps.setString(1, CommonConstants.STATUS_AS_DELETE);
			ps.setString(2, uniqueUserId);
			ps.executeUpdate();

		} catch (Exception e) {
			throw new ApplicationException("Error during method deleteUser: ", e);
		} finally {
			closeResources(rs, ps, con);
		}

	}*/

	//	public List<PerfiosUserAccountTransactionVo> getUserAccountTransactionByUserIdAndInstId(String uniqueUserId,
	//			Long instId) throws ApplicationException {
	//		Connection con = null;
	//		PreparedStatement ps = null;
	//		ResultSet rs = null;
	//		List<PerfiosUserAccountTransactionVo> transactions = new ArrayList<PerfiosUserAccountTransactionVo>();
	//		try {
	//			con = getUserMgmConnection();
	//			ps = con.prepareStatement(PerfiosConstants.FETCH_USER_ACCOUNT_TRANSACTION_USER_ID_INST_ID);
	//			ps.setString(1, uniqueUserId);
	//			ps.setLong(2, instId);
	//			rs = ps.executeQuery();
	//			
	//			while (rs.next()) {
	//				PerfiosUserAccountTransactionVo uat = new PerfiosUserAccountTransactionVo();
	//				uat.setStatus(rs.getString(1));
	//				uat.setTxnSeqId(rs.getString(2));
	//				uat.setXnDate(rs.getString(3));
	//				uat.setXnDetails(rs.getString(4));
	//				uat.setChequeNum(rs.getString(5));
	//				uat.setXnAmount(rs.getString(6));
	//				uat.setBalance(rs.getString(7));
	//				uat.setCategoryId(rs.getString(8));
	//				uat.setUserComment(rs.getString(9));
	//				uat.setSplitRefId(rs.getString(10));
	//				uat.setXnId(rs.getString(11));
	//				uat.setCategory(rs.getString(12));
	//				uat.setiType(rs.getString(13));
	//				uat.setName(rs.getString(14));
	//				uat.setInstId(rs.getLong(15));
	//				
	//				transactions.add(uat);
	//			}
	//			
	//		} catch (Exception e) {
	//			throw new ApplicationException("Error during method getUserAccountTransactionByUserIdAndInstId: ", e);
	//		} finally {
	//			closeResources(rs, ps, con);
	//		}
	//		return transactions;
	//	}

	/*public boolean isUserIdInstIdExist(String uniqueUserId,
			Long instId) throws ApplicationException {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = getUserMgmConnection();
			ps = con.prepareStatement(PerfiosConstants.IS_USER_ID_INST_ID_EXIST);
			ps.setString(1, uniqueUserId);
			ps.setLong(2, instId);
			rs = ps.executeQuery();

			while (rs.next()) {
				return true;
			}

		} catch (Exception e) {
			throw new ApplicationException("Error during method isUserIdInstIdExist: ", e);
		} finally {
			closeResources(rs, ps, con);
		}
		return false;
	}
	 */
	/*public String getAccountIdFromUserTransactions(String uniqueUserId, Long instId) throws ApplicationException {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String accountId = null;
		try {
			con = getUserMgmConnection();
			ps = con.prepareStatement(PerfiosConstants.GET_ACCOUNT_ID_FROM_USER_TRANSACTION);
			ps.setString(1, uniqueUserId);
			ps.setLong(2, instId);
			rs = ps.executeQuery();

			while (rs.next()) {
				accountId = rs.getString(1);
			}

		} catch (Exception e) {
			throw new ApplicationException("Error during method getAccountIdFromUserTransactions: ", e);
		} finally {
			closeResources(rs, ps, con);
		}
		return accountId;
	}*/

	/*public void deleteUserAccountTransaction(String uniqueUserId, Long instId) throws ApplicationException {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = getUserMgmConnection();
			ps = con.prepareStatement(PerfiosConstants.DELETE_USER_TRANSACTIONS_USER_ID_INST_ID);
			ps.setString(1, uniqueUserId);
			ps.setLong(2, instId);
			ps.executeUpdate();

		} catch (Exception e) {
			throw new ApplicationException("Error during method getAccountIdFromUserTransactions: ", e);
		} finally {
			closeResources(rs, ps, con);
		}
	}*/

	/*public void createUserAccountTransaction(List<PerfiosUserAccountTransactionVo> perfiosList, String uniqueUserId)
	throws ApplicationException {
Connection con = null;
PreparedStatement ps = null;
ResultSet rs = null;
try {
	con = getUserMgmConnection();
	con.setAutoCommit(false);
//	String id="";
//	String orgId="";
//	String userId="";
//	String role="";
//
//	if(uniqueUserId!=null) {
//		String []values=uniqueUserId.split("-");
//		id=values[0]!=null?values[0].replaceAll("Id:", ""):"";
//		orgId=values[1]!=null?values[1].replaceAll("OrgId:", ""):"";
//		userId=values[2]!=null?values[2].replaceAll("UserId:", ""):"";
//		role=values[3]!=null?values[3].replaceAll("RoleName:", ""):"";
//	}
	PerfiosUserVo user = getUserInfo(uniqueUserId, con);
	if (user != null) {
		for (PerfiosUserAccountTransactionVo perfios : perfiosList) {
			String query=PerfiosConstants.CREATE_BANK_STATEMENT_TRANSACTION;
			boolean txnExists=checkTrasactionExists(user.getOrgId(), perfios.getXnId(), con);
			if(txnExists) {
				query=PerfiosConstants.UPDATE_BANK_STATEMENT_TRANSACTION;
			}
			ps = con.prepareStatement(query,
					Statement.RETURN_GENERATED_KEYS);	
			ps.setInt(1, user.getUserId());
			ps.setInt(2, user.getOrgId());
			ps.setString(3, user.getRoleName());
			ps.setString(4, perfios.getAccountId());
			ps.setString(5, "");
			ps.setString(6, perfios.getInstName());
			ps.setString(7, perfios.getXnDate());
			ps.setString(8, perfios.getXnDate());
			ps.setString(9, perfios.getChequeNum());
			ps.setString(10, perfios.getXnDetails());
			String creditAmount="";
			String debitAmount="";
			if(perfios.getXnAmount()!=null) {
				double txnAmount=Double.parseDouble(perfios.getXnAmount());
				if(txnAmount>0) {//>0 credit/ <0 debit
					creditAmount=perfios.getXnAmount();
					debitAmount="0";
				}else if(txnAmount<0) {
					debitAmount=perfios.getXnAmount();
					creditAmount="0";
				}

			}
			ps.setString(11, debitAmount);
			ps.setString(12, creditAmount);
			ps.setString(13, perfios.getBalance());
			ps.setString(14, "Perfios");
			ObjectMapper mapper = new ObjectMapper();
			String data = mapper.writeValueAsString(perfios);
			ps.setString(15, data);
			ps.setString(16, perfios.getUserComment());
			ps.setString(17, perfios.getXnId());
			ps.setString(18, perfios.getCategory());
			if(txnExists) {
				ps.setString(19, perfios.getXnId());
			}
			ps.executeUpdate();
			closeResources(null, ps, null);
			//insert into banking.user_financial_institution_perfios
			ps = con.prepareStatement(PerfiosConstants.CREATE_USER_ACCOUNT_TRANSACTION,Statement.RETURN_GENERATED_KEYS);	
			//user_id ,txn_seq_id ,xn_date,xn_details,cheque_number,xn_amount,
			//balance,category_id,user_comment,split_reference_id,xn_id,
			//category,i_type,name,instId,account_id,inst_name
			ps.setString(1, uniqueUserId);
			ps.setString(2, perfios.getTxnSeqId());
			ps.setString(3, perfios.getXnDate());
			ps.setString(4, perfios.getXnDetails());
			ps.setString(5, perfios.getChequeNum());
			ps.setString(6, perfios.getXnAmount());
			ps.setString(7, perfios.getBalance());
			ps.setString(8, perfios.getCategoryId());
			ps.setString(9, perfios.getUserComment());
			ps.setString(10, perfios.getSplitRefId());
			ps.setString(11, perfios.getXnId());
			ps.setString(12, perfios.getCategory());
			ps.setString(13, perfios.getiType());
			ps.setString(14, perfios.getAccountName());
			ps.setLong(15, perfios.getInstId());
			ps.setString(16, perfios.getAccountId());
			ps.setString(17, perfios.getInstName());
			ps.setString(18, perfios.getCurrency());
			ps.executeUpdate();
		}
	}


	con.commit();

} catch (Exception e) {
	try {
		con.rollback();
	} catch (SQLException e1) {
		throw new ApplicationException(e1);
	}
	throw new ApplicationException("Error during method createUserAccountTransaction: ", e);
} finally {
	closeResources(rs, ps, con);
}
}*/


	/*private boolean checkTrasactionExists(int organizationId,String txnId,Connection connection) throws ApplicationException {
boolean result=false;
PreparedStatement ps = null;
ResultSet rs = null;
if(txnId==null || organizationId==0) {
	return false;
}
try {
	ps = connection.prepareStatement(PerfiosConstants.CHECK_BANK_STATEMENT_TRANSACTION_EXISTS);
	ps.setInt(1, organizationId);
	ps.setString(2, txnId);
	rs = ps.executeQuery();
	if (rs.next()) {
		result=true;
	}
} catch (Exception e) {
	throw new ApplicationException(e);
} finally {
	closeResources(rs, ps, null);
}
return result;
}*/

}

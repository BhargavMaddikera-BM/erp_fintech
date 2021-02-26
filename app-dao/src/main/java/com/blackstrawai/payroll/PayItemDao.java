package com.blackstrawai.payroll;

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
import com.blackstrawai.accounting.AccountingAspectsApproversVo;
import com.blackstrawai.accounting.AccountingAspectsConstants;
import com.blackstrawai.accounting.AccountingAspectsGeneralVo;
import com.blackstrawai.accounting.AccountingAspectsItemsVo;
import com.blackstrawai.accounting.AccountingAspectsVo;
import com.blackstrawai.chartofaccounts.ChartOfAccountsDao;
import com.blackstrawai.common.BaseDao;
import com.blackstrawai.onboarding.OrganizationConstants;
import com.blackstrawai.settings.VoucherDao;
import com.blackstrawai.upload.UploadPayrollVo;

@Repository
public class PayItemDao extends BaseDao{

	private Logger logger = Logger.getLogger(PayItemDao.class);

	@Autowired
	PayTypeDao payTypeDao;

	@Autowired
	VoucherDao voucherDao;

	@Autowired
	ChartOfAccountsDao accountsDao;


	public PayItemVo createPayItem(PayItemVo payItemVo) throws ApplicationException {
		logger.info("Entry into method: createPayItem");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		//Boolean showColumn = false;
		try {
			con = getUserMgmConnection();
			boolean isPayItemExist=checkPayItemExist(payItemVo.getName(),payItemVo.getOrganizationId(),con);
			if(isPayItemExist){
				throw new Exception("Pay Item Exist for the Organization");
			}
			String sql = PayRollConstants.INSERT_INTO_PAY_ITEM_ORGANIZATION;
			preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);	
			preparedStatement.setString(1, payItemVo.getName());
			preparedStatement.setString(2, payItemVo.getDescription());
			preparedStatement.setInt(3, payItemVo.getOrganizationId());
			preparedStatement.setInt(4, Integer.valueOf(payItemVo.getUserId()));
			preparedStatement.setBoolean(5, payItemVo.getIsSuperAdmin());
			preparedStatement.setInt(6,payItemVo.getPayType());
			preparedStatement.setInt(7, payItemVo.getLedgerId());
			preparedStatement.setString(8,payItemVo.getLedgerName());
			preparedStatement.setBoolean(9, false);
			preparedStatement.setString(10, payItemVo.getRoleName());
			/*if( Arrays.asList("Leave Encashment","Bonus","Travel",
					"ESI-Employee Contribution",
					"ESI-Employer Contribution").contains(payItemVo.getName())) {
				showColumn = false;				
			} else {
				showColumn = true;		
			}*/
			preparedStatement.setBoolean(11, false);
			
			int rowAffected = preparedStatement.executeUpdate();
			if (rowAffected == 1) {
				rs = preparedStatement.getGeneratedKeys();
				if (rs.next()) {
					payItemVo.setId(rs.getInt(1));
				}
			}
		}catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return payItemVo;
	}


	private boolean checkPayItemExist(String name, int organizationId, Connection con) throws ApplicationException {
		logger.info("Entry into method: checkPayItemExist");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			String query =PayRollConstants.CHECK_PAY_ITEM_EXIST_ORGANIZATION;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, organizationId);
			preparedStatement.setString(2, name);
			rs = preparedStatement.executeQuery();
			if (rs.next()) {
				return true;
			}
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, null);

		}
		return false;
	}




	public PayItemVo getPayItemById(int id) throws ApplicationException {
		logger.info("Entry into method: getPayItemById");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		PayItemVo data = new PayItemVo();
		try {
			con = getUserMgmConnection();
			String query = PayRollConstants.GET_PAY_ITEM_ORGANIZATION;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, id);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				data.setId(rs.getInt(1));
				data.setName(rs.getString(2));
				data.setDescription(rs.getString(3));
				data.setPayType(rs.getInt(4));
				data.setLedgerId(rs.getInt(5));
				data.setLedgerName(rs.getString(6));
				data.setStatus(rs.getString(7));
				rs.getTimestamp(8);
				rs.getTimestamp(9);
				data.setUserId(rs.getString(10));
				data.setIsSuperAdmin(rs.getBoolean(11));
				data.setOrganizationId(rs.getInt(12));
			}
		}catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return data;
	}

	public PayItemVo getPayItemByName(int organizationId,String name) throws ApplicationException {
		logger.info("Entry into method: getPayItemById");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		PayItemVo data = new PayItemVo();
		try {
			con = getUserMgmConnection();
			String query = PayRollConstants.GET_PAY_ITEM_NAME_ORGANIZATION;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, organizationId);
			preparedStatement.setString(2, name);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				data.setId(rs.getInt(1));
				data.setName(rs.getString(2));
				data.setDescription(rs.getString(3));
				data.setPayType(rs.getInt(4));
				data.setLedgerId(rs.getInt(5));
				data.setLedgerName(rs.getString(6));
				data.setStatus(rs.getString(7));
				rs.getTimestamp(8);
				rs.getTimestamp(9);
				data.setUserId(rs.getString(10));
				data.setIsSuperAdmin(rs.getBoolean(11));
				data.setOrganizationId(rs.getInt(12));
			}
		}catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return data;
	}

	public PayItemVo deletePayItem(int id,String status,String userId,String roleName) throws ApplicationException {
		logger.info("Entry into method: deletePayItem");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		PayItemVo data = new PayItemVo();
		try {
			con = getUserMgmConnection();
			String sql = PayRollConstants.DELETE_PAY_ITEM_ORGANIZATION;
			preparedStatement = con.prepareStatement(sql);
			preparedStatement.setString(1, status);
			preparedStatement.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
			preparedStatement.setString(3, userId);
			preparedStatement.setString(4, roleName);
			preparedStatement.setInt(5, id);
			data.setId(id);
			preparedStatement.executeUpdate();
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return data;
	}


	public PayItemVo updatePayItem(PayItemVo data) throws ApplicationException {
		logger.info("Entry into method: updatePayItem");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con =getUserMgmConnection();
			String sql = PayRollConstants.UPDATE_PAY_ITEM_ORGANIZATION;
			preparedStatement = con.prepareStatement(sql);
			preparedStatement.setString(1, data.getName());
			preparedStatement.setString(2, data.getDescription());
			preparedStatement.setInt(3, data.getOrganizationId());
			preparedStatement.setBoolean(4, data.getIsSuperAdmin());
			preparedStatement.setInt(5, Integer.valueOf(data.getUserId()));
			preparedStatement.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
			preparedStatement.setString(7, "ACT");			
			preparedStatement.setInt(8,data.getPayType());
			preparedStatement.setInt(9,data.getLedgerId());
			preparedStatement.setString(10, data.getLedgerName());	
			preparedStatement.setString(11, data.getRoleName());
			preparedStatement.setInt(12, data.getId());
			preparedStatement.executeUpdate();
		}catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return data;
	}

	public List<PayItemVo> getAllPayItemsOfAnOrganization(int organizationId)throws ApplicationException{
		logger.info("Entry into method: getAllPayItemsOfAnOrganization");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con=null;
		List<PayItemVo> data=new ArrayList<PayItemVo>();
		try
		{
			con=getUserMgmConnection();
			String query=PayRollConstants.GET_ALL_BASIC_PAY_ITEM_ORGANIZATION;
			preparedStatement=con.prepareStatement(query);
			preparedStatement.setInt(1,organizationId);
			rs = preparedStatement.executeQuery();			
			while (rs.next()) {
				PayItemVo payItemVo=new PayItemVo();
				payItemVo.setId(rs.getInt(1));
				payItemVo.setName(rs.getString(2));
				payItemVo.setPayType(rs.getInt(3));
				payItemVo.setStatus(rs.getString(4));
				payItemVo.setDescription(rs.getString(5));
				payItemVo.setOrganizationId(rs.getInt(6));
				payItemVo.setUserId(rs.getString(7));
				payItemVo.setIsSuperAdmin(rs.getBoolean(8));
				PayTypeVo payTypeVo=payTypeDao.getPayTypeById(payItemVo.getPayType());
				payItemVo.setPayTypeName(payTypeVo.getName());
				data.add(payItemVo);				
			}
		}catch(Exception e){

			throw new ApplicationException(e);
		}finally {
			closeResources(rs, preparedStatement, con);
		}	
		return data;
	}


	public List<PayItemVo> getAllPayItemsOfAnOrganizationForUserAndRole(int organizationId,String userId,String roleName)throws ApplicationException{
		logger.info("Entry into method: getAllPayItemsOfAnOrganizationForUserAndRole");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con=null;
		List<PayItemVo> data=new ArrayList<PayItemVo>();
		try
		{
			con=getUserMgmConnection();
			String query="";
			if(roleName.equals("Super Admin")){
				query=PayRollConstants.GET_ALL_BASIC_PAY_ITEM_ORGANIZATION;
			}else{
				query=PayRollConstants.GET_ALL_BASIC_PAY_ITEM_ORGANIZATION_USER_ROLE;
			}
			preparedStatement=con.prepareStatement(query);
			preparedStatement.setInt(1,organizationId);
			if(!(roleName.equals("Super Admin"))){
				preparedStatement.setString(2, userId);
				preparedStatement.setString(3, roleName);
			}	
			rs = preparedStatement.executeQuery();			
			while (rs.next()) {
				PayItemVo payItemVo=new PayItemVo();
				payItemVo.setId(rs.getInt(1));
				payItemVo.setName(rs.getString(2));
				payItemVo.setPayType(rs.getInt(3));
				payItemVo.setStatus(rs.getString(4));
				payItemVo.setDescription(rs.getString(5));
				payItemVo.setOrganizationId(rs.getInt(6));
				payItemVo.setUserId(rs.getString(7));
				payItemVo.setIsSuperAdmin(rs.getBoolean(8));
				PayTypeVo payTypeVo=payTypeDao.getPayTypeById(payItemVo.getPayType());
				payItemVo.setPayTypeName(payTypeVo.getName());
				data.add(payItemVo);				
			}
		}catch(Exception e){

			throw new ApplicationException(e);
		}finally {
			closeResources(rs, preparedStatement, con);
		}	
		return data;
	}

	public PayItemVo createPayItemBaseOrganizationData(PayItemVo payItemVo) throws ApplicationException {
		logger.info("Entry into method: createPayItemBaseOrganizationData");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = getUserMgmConnection();		
			String sql = PayRollConstants.INSERT_INTO_PAY_ITEM_ORGANIZATION;
			preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);	
			preparedStatement.setString(1, payItemVo.getName());
			preparedStatement.setString(2, payItemVo.getDescription());
			preparedStatement.setInt(3, payItemVo.getOrganizationId());
			preparedStatement.setInt(4, Integer.valueOf(payItemVo.getUserId()));
			preparedStatement.setBoolean(5, payItemVo.getIsSuperAdmin());
			preparedStatement.setInt(6,payItemVo.getPayType());
			preparedStatement.setInt(7, payItemVo.getLedgerId());
			preparedStatement.setString(8,payItemVo.getLedgerName());
			preparedStatement.setBoolean(9, true);
			preparedStatement.setString(10, payItemVo.getRoleName());
			preparedStatement.setBoolean(11, payItemVo.getShowColumn());
			int rowAffected = preparedStatement.executeUpdate();
			if (rowAffected == 1) {
				rs = preparedStatement.getGeneratedKeys();
				if (rs.next()) {
					payItemVo.setId(rs.getInt(1));
				}
			}
		}catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return payItemVo;
	}




	public void uploadPayrollData(int currency,
									String roleName,
									String userId,
									int organizationId,									
									List<UploadPayrollVo> dataList) throws ApplicationException {
		logger.info("Entry into method: uploadPayrollData");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		

		
		int journalNo=getMaxJournalNoPerOrganization(organizationId);
		++journalNo;
		String journal_no_str=new Integer(journalNo).toString();
		/*if(journal_no_str.length()==1){
			journal_no_str="000"+journalNo;
		}else if(journal_no_str.length()==2){
			journal_no_str="00"+journalNo;
		}else if(journal_no_str.length()==3){
			journal_no_str="0"+journalNo;
		}*/

		Double totalDebit=null;
		Double totalCredit=null;
		Double difference=null;
		Map<String,String>debitMap=new HashMap<String,String>();
		Map<String,String>creditMap=new HashMap<String,String>();
		try {
			con = getUserMgmConnection();	
			con.setAutoCommit(false);
			String sql = PayRollConstants.INSERT_INTO_PAYROLL_UPLOAD;
			preparedStatement = con.prepareStatement(sql);
			for(int i=0;i<dataList.size();i++){
				UploadPayrollVo data=dataList.get(i);								
				preparedStatement.setString(1, data.getEmployeeId());
				preparedStatement.setString(2, data.getEmployeeName());
				preparedStatement.setString(3, data.getPayItemName());
				preparedStatement.setString(4, data.getPayItemValue());
				preparedStatement.setInt(5, organizationId);
				preparedStatement.setInt(6, Integer.parseInt(userId));		
				preparedStatement.setString(7, roleName);
				preparedStatement.setInt(8,journalNo);		
				preparedStatement.setInt(9, data.getLedgerId());
				preparedStatement.setString(10,data.getLedgerName());
				preparedStatement.setString(11, data.getType());
				preparedStatement.executeUpdate();		

			}
			sql = PayRollConstants.INSERT_INTO_PROCESSING_TEMP;
			preparedStatement = con.prepareStatement(sql);
			for(int i=0;i<dataList.size();i++){
				UploadPayrollVo data=dataList.get(i);								
				preparedStatement.setString(1, data.getPayItemName());
				preparedStatement.setString(2, data.getPayItemValue());
				preparedStatement.setInt(3, organizationId);
				preparedStatement.setInt(4, Integer.parseInt(userId));		
				preparedStatement.setString(5, roleName);
				preparedStatement.setString(6, data.getType());
				preparedStatement.executeUpdate();		
			}		

			sql= PayRollConstants.GET_TEMP_DATA;
			preparedStatement = con.prepareStatement(sql);
			preparedStatement.setInt(1, organizationId);
			preparedStatement.setInt(2, Integer.parseInt(userId));	
			preparedStatement.setString(3, roleName);
			preparedStatement.setString(4, "Debit");
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				totalDebit = new Double(rs.getString(1));
			}
			closeResources(rs, preparedStatement, null);

			sql = PayRollConstants.GET_TEMP_DATA;
			preparedStatement = con.prepareStatement(sql);
			preparedStatement.setInt(1, organizationId);
			preparedStatement.setInt(2, Integer.parseInt(userId));	
			preparedStatement.setString(3, roleName);
			preparedStatement.setString(4, "Credit");
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				totalCredit = new Double(rs.getString(1));
			}
			closeResources(rs, preparedStatement, null);	


			sql = PayRollConstants.GET_TEMP_DATA_GROUP_BY_PAY_ITEM;
			preparedStatement = con.prepareStatement(sql);
			preparedStatement.setInt(1, organizationId);
			preparedStatement.setInt(2, Integer.parseInt(userId));	
			preparedStatement.setString(3, roleName);
			preparedStatement.setString(4, "Debit");
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				String debitValue=rs.getString(1);
				String payName=rs.getString(2);
				debitMap.put(payName, debitValue);
			}
			closeResources(rs, preparedStatement, null);	

			sql = PayRollConstants.GET_TEMP_DATA_GROUP_BY_PAY_ITEM;
			preparedStatement = con.prepareStatement(sql);
			preparedStatement.setInt(1, organizationId);
			preparedStatement.setInt(2, Integer.parseInt(userId));	
			preparedStatement.setString(3, roleName);
			preparedStatement.setString(4, "Credit");
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				String creditValue=rs.getString(1);
				String payName=rs.getString(2);
				creditMap.put(payName, creditValue);
			}
			closeResources(rs, preparedStatement, null);	

			sql=PayRollConstants.DELETE_TEMP_DATA;
			preparedStatement = con.prepareStatement(sql);
			preparedStatement.setInt(1, organizationId);
			preparedStatement.setInt(2, Integer.parseInt(userId));	
			preparedStatement.setString(3, roleName);
			preparedStatement.executeUpdate();

			con.commit();



		}catch (Exception e) {
			try {
				con.rollback();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				throw new ApplicationException(e1);
			}
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}

		difference=totalCredit-totalDebit;
		AccountingAspectsVo  accountingAspectsVo=new AccountingAspectsVo();
		accountingAspectsVo.setOrganizationId(organizationId);
		accountingAspectsVo.setUserId(userId);
		accountingAspectsVo.setRoleName(roleName);
		if(roleName.equals("SuperAdmin")){
			accountingAspectsVo.setIsSuperAdmin(true);
		}else{
			accountingAspectsVo.setIsSuperAdmin(false);
		}			

		AccountingAspectsGeneralVo accountingAspectsGeneralVo= new AccountingAspectsGeneralVo();
		accountingAspectsGeneralVo.setDateOfCreation(new Date(System.currentTimeMillis()));
		accountingAspectsGeneralVo.setTypeId(3);
		accountingAspectsGeneralVo.setJournalNo(journal_no_str);
		accountingAspectsGeneralVo.setIsRegisteredLocation(true);
		accountingAspectsGeneralVo.setLocationId(getGSTDetails(organizationId));
		accountingAspectsGeneralVo.setCurrencyId(currency);
		accountingAspectsGeneralVo.setTotalCredits(totalCredit.toString());
		accountingAspectsGeneralVo.setTotalDebits(totalDebit.toString());
		accountingAspectsGeneralVo.setDifference(difference.toString());
		accountingAspectsVo.setAccountingAspectsGeneralInfo(accountingAspectsGeneralVo);
		AccountingAspectsApproversVo accountingAspectsApproversVo=new AccountingAspectsApproversVo();
		accountingAspectsVo.setApproversList(accountingAspectsApproversVo);
		if(roleName.equals("SuperAdmin")){
			accountingAspectsVo.setIsSuperAdmin(true);
		}else{
			accountingAspectsVo.setIsSuperAdmin(false);
		}
		if((difference!=null) && (difference>0.0 || difference<0.0)){
			accountingAspectsVo.setStatus("DRAFT");

		}else{
			accountingAspectsVo.setStatus("ACT");
		}
		createAccountingAspects(debitMap,creditMap,accountingAspectsVo);


	}

	private Integer getGSTDetails(Integer orgId) throws ApplicationException {
		logger.info("Enter into Method: getAccountingEntryLocationId");
		Integer locationId = null;
		Connection con =null;
		PreparedStatement preparedStatement =null;
		ResultSet rs = null;
		try  {
			con = getUserMgmConnection();
			preparedStatement = con.prepareStatement(OrganizationConstants.GET_LOCATION_ID);
			preparedStatement.setInt(1, orgId);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				locationId = rs.getInt(1);
			}
		} catch (Exception e) {
			logger.info("Error in  getAccountingEntryTypeId:", e);
			throw new ApplicationException(e.getMessage());
		}finally{
			closeResources(rs, preparedStatement, con);
		}
		return locationId;

	} 

	private int getMaxJournalNoPerOrganization(int organizationId) throws ApplicationException {
		logger.info("Entry into method: getMaxJournalNoPerOrganization");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = getUserMgmConnection();
			String query = PayRollConstants.GET_MAX_VOUCHER_NO_PER_ORGANIZATION;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, organizationId);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				return rs.getInt(1);
			}
		}catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return 0;
	}

	public String getDebitOrCredit(Integer orgId,String name) throws ApplicationException {
		logger.info("Enter into Method: getAccountingEntryLocationId");
		Integer payTypeId = null;
		String parentName=null;
		String type=null;
		Connection con =null;
		PreparedStatement preparedStatement =null;
		ResultSet rs = null;
		try {
			con = getUserMgmConnection();
			preparedStatement = con.prepareStatement(PayRollConstants.GET_PAY_TYPE_ID_GIVEN_PAY_NAME);
			preparedStatement.setInt(1, orgId);
			preparedStatement.setString(2, name);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				payTypeId = rs.getInt(1);
			}
		} catch (Exception e) {
			logger.info("Error in  getAccountingEntryTypeId:", e);
			throw new ApplicationException(e.getMessage());
		}finally{
			closeResources(rs, preparedStatement, con);
		}
		if(payTypeId!=null){
			try{
				con = getUserMgmConnection();
				preparedStatement = con.prepareStatement(PayRollConstants.GET_PARENT_PAY_TYPE_GIVEN_PAY_TYPE);
				preparedStatement.setInt(1, orgId);
				preparedStatement.setInt(2, payTypeId);
				rs = preparedStatement.executeQuery();
				while (rs.next()) {
					parentName = rs.getString(1);
				}
			} catch (Exception e) {
				logger.info("Error in  getAccountingEntryTypeId:", e);
				throw new ApplicationException(e.getMessage());
			}finally{
				closeResources(rs, preparedStatement, con);
			}
			if(parentName!=null){
				try  {
					con = getUserMgmConnection();
					preparedStatement = con.prepareStatement(PayRollConstants.GET_DEBIT_CREDIT_GIVEN_TYPE);
					preparedStatement.setInt(1, orgId);
					preparedStatement.setString(2, parentName);
					rs = preparedStatement.executeQuery();
					while (rs.next()) {
						type = rs.getString(1);
					}
				} catch (Exception e) {
					logger.info("Error in  getAccountingEntryTypeId:", e);
					throw new ApplicationException(e.getMessage());
				}finally{
					closeResources(rs, preparedStatement, con);
				}
			}
		}
		return type;
	}
	
	public Map<String , String > getPayItemLedgerByOrgId(Integer orgId) throws ApplicationException{
		Map<String , String > payItemLedgerMap = new HashMap<String, String>();
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			con = getPayrollConnection();
			preparedStatement = con.prepareStatement(PayRunConstants.GET_PAY_ITEM_LEDGERS_BY_ORG_ID);
			preparedStatement.setInt(1, orgId);	
			resultSet = preparedStatement.executeQuery();
			while(resultSet.next()) {
				payItemLedgerMap.put(resultSet.getString(1), resultSet.getString(3)+"~"+resultSet.getString(2));
			}
		
		}catch(Exception e) {
			logger.info("Errro in get ledgers of payItems", e);
			throw new ApplicationException(e.getMessage());
		}finally {
			closeResources(resultSet, preparedStatement, con);
		}
		
		return payItemLedgerMap;
		
	}

	@SuppressWarnings({ "rawtypes" })
	private AccountingAspectsVo createAccountingAspects(Map<String, String> debitMap,Map<String, String> creditMap,AccountingAspectsVo accountingAspectsVo)
			throws ApplicationException {
		logger.info("Entry into createAccountingAspects");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = getUserMgmConnection();
			con.setAutoCommit(false);			
			String sql = AccountingAspectsConstants.INSERT_INTO_ACCOUTING_ASPECTS;
			preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setDate(1,
					accountingAspectsVo.getAccountingAspectsGeneralInfo().getDateOfCreation() != null
					? accountingAspectsVo.getAccountingAspectsGeneralInfo().getDateOfCreation()
							: null);
			preparedStatement.setObject(2,
					accountingAspectsVo.getApproversList().getApprover1() != null
					? accountingAspectsVo.getApproversList().getApprover1()
							: null);
			preparedStatement.setObject(3,
					accountingAspectsVo.getApproversList().getApprover2() != null
					? accountingAspectsVo.getApproversList().getApprover2()
							: null);
			preparedStatement.setObject(4,
					accountingAspectsVo.getApproversList().getApprover3() != null
					? accountingAspectsVo.getApproversList().getApprover3()
							: null);
			preparedStatement.setObject(5,
					accountingAspectsVo.getAccountingAspectsGeneralInfo().getCurrencyId() != null
					? accountingAspectsVo.getAccountingAspectsGeneralInfo().getCurrencyId()
							: null);
			preparedStatement.setString(6,
					accountingAspectsVo.getAccountingAspectsGeneralInfo().getJournalNo() != null
					? accountingAspectsVo.getAccountingAspectsGeneralInfo().getJournalNo()
							: null);
			preparedStatement.setObject(7,
					accountingAspectsVo.getAccountingAspectsGeneralInfo().getLocationId() != null
					? accountingAspectsVo.getAccountingAspectsGeneralInfo().getLocationId()
							: null);
			preparedStatement.setString(8,
					accountingAspectsVo.getAccountingAspectsGeneralInfo().getNotes() != null
					? accountingAspectsVo.getAccountingAspectsGeneralInfo().getNotes()
							: null);
			preparedStatement.setObject(9,
					accountingAspectsVo.getAccountingAspectsGeneralInfo().getTypeId() != null
					? accountingAspectsVo.getAccountingAspectsGeneralInfo().getTypeId()
							: null);
			preparedStatement.setString(10,
					accountingAspectsVo.getAccountingAspectsGeneralInfo().getTotalCredits() != null
					? accountingAspectsVo.getAccountingAspectsGeneralInfo().getTotalCredits()
							: null);
			preparedStatement.setString(11,
					accountingAspectsVo.getAccountingAspectsGeneralInfo().getTotalDebits() != null
					? accountingAspectsVo.getAccountingAspectsGeneralInfo().getTotalDebits()
							: null);
			preparedStatement.setString(12,
					accountingAspectsVo.getAccountingAspectsGeneralInfo().getDifference() != null
					? accountingAspectsVo.getAccountingAspectsGeneralInfo().getDifference()
							: null);
			preparedStatement.setObject(13,
					accountingAspectsVo.getOrganizationId() != null ? accountingAspectsVo.getOrganizationId() : null);
			preparedStatement.setObject(14,
					Integer.valueOf(accountingAspectsVo.getUserId()) != null
					? Integer.valueOf(accountingAspectsVo.getUserId())
							: null);
			preparedStatement.setBoolean(15,
					accountingAspectsVo.getIsSuperAdmin() != null ? accountingAspectsVo.getIsSuperAdmin() : null);
			preparedStatement.setBoolean(16,
					accountingAspectsVo.getAccountingAspectsGeneralInfo().getIsRegisteredLocation() != null
					? accountingAspectsVo.getAccountingAspectsGeneralInfo().getIsRegisteredLocation()
							: null);
			preparedStatement.setString(17,
					accountingAspectsVo.getStatus() != null ? accountingAspectsVo.getStatus() : null);
			preparedStatement.setString(18, accountingAspectsVo.getRoleName());
			int rowAffected = preparedStatement.executeUpdate();
			if (rowAffected == 1) {
				rs = preparedStatement.getGeneratedKeys();
				if (rs.next()) {
					accountingAspectsVo.setId(rs.getInt(1));
				}
			}
			List<AccountingAspectsItemsVo> itemDetails=new ArrayList<AccountingAspectsItemsVo>();
			Iterator it=debitMap.entrySet().iterator();
			while(it.hasNext()){
				Map.Entry entry=(Map.Entry)it.next();
				String key=(String) entry.getKey();
				String value=(String) entry.getValue();
				PayItemVo payItemVo=getPayItemByName(accountingAspectsVo.getOrganizationId(),key);
				int id=accountsDao.getLedgerIdGivenName(payItemVo.getLedgerName(),accountingAspectsVo.getOrganizationId());
			//	int subLedgerId=accountsDao.createLevel6ReturnId(key, key, accountingAspectsVo.getOrganizationId(), accountingAspectsVo.getUserId(), accountingAspectsVo.getIsSuperAdmin(), id, false, key);
				AccountingAspectsItemsVo itemVo=new AccountingAspectsItemsVo();
				itemVo.setAccountsId(id);
				itemVo.setAccountsName(payItemVo.getLedgerName());
				itemVo.setAccountsLevel("L5");
				itemVo.setSubLedgerId(0);
				itemVo.setSubLedgerName(key);
				itemVo.setCurrencyId(accountingAspectsVo.getAccountingAspectsGeneralInfo().getCurrencyId());
				itemVo.setExchangeRate("1");
				itemVo.setDebits( value);
				itemVo.setTotalDebitsEx(value);
				itemDetails.add(itemVo);
			}

			it=creditMap.entrySet().iterator();
			while(it.hasNext()){
				Map.Entry entry=(Map.Entry)it.next();
				String key=(String) entry.getKey();
				String value=(String) entry.getValue();
				PayItemVo payItemVo=getPayItemByName(accountingAspectsVo.getOrganizationId(),key);
				if( key.equals("Salary payable")){
					payItemVo.setLedgerName("Salaries and Bonus Payable");
				}					
				int id=accountsDao.getLedgerIdGivenName(payItemVo.getLedgerName(),accountingAspectsVo.getOrganizationId());
				//int subLedgerId=accountsDao.createLevel6ReturnId(key, key, accountingAspectsVo.getOrganizationId(), accountingAspectsVo.getUserId(), accountingAspectsVo.getIsSuperAdmin(), id, false, key);
				AccountingAspectsItemsVo itemVo=new AccountingAspectsItemsVo();
				itemVo.setAccountsId(id);
				itemVo.setAccountsName(payItemVo.getLedgerName());
				itemVo.setAccountsLevel("L5");
				itemVo.setSubLedgerId(0);
				itemVo.setSubLedgerName(key);
				itemVo.setCurrencyId(accountingAspectsVo.getAccountingAspectsGeneralInfo().getCurrencyId());
				itemVo.setExchangeRate("1");
				itemVo.setCredits( value);
				itemVo.setTotalCreditsEx(value);
				itemDetails.add(itemVo);
			}
			createAccoutingAspectsBaseDetail(accountingAspectsVo.getId(),itemDetails,con);
			con.commit();
		} catch (Exception e) {
			logger.info("Error in createAccountingEntry:: ", e);
			try {
				con.rollback();
			} catch (SQLException e1) {
				throw new ApplicationException(e1.getMessage());
			}
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return accountingAspectsVo;
	}

	private void createAccoutingAspectsBaseDetail(Integer id, List<AccountingAspectsItemsVo> itemDetails,
			Connection con) throws ApplicationException {
		logger.info("Entry into createAccoutingAspectsBaseDetail");
		PreparedStatement preparedStatement=null;
		try {
			preparedStatement = con.prepareStatement(AccountingAspectsConstants.INSERT_INTO_ACCOUTING_ASPECTS_ITEM);
			for (AccountingAspectsItemsVo sinlgeItem : itemDetails) {
				if (sinlgeItem != null && sinlgeItem.getAccountsId() != null && !sinlgeItem.getAccountsId().equals(0)) {
					preparedStatement.setObject(1,
							sinlgeItem.getAccountsId() != null ? sinlgeItem.getAccountsId() : null);
					preparedStatement.setObject(2,
							sinlgeItem.getCurrencyId() != null ? sinlgeItem.getCurrencyId() : null);
					preparedStatement.setString(3,
							sinlgeItem.getDescription() != null ? sinlgeItem.getDescription() : null);
					preparedStatement.setString(4,
							sinlgeItem.getExchangeRate() != null ? sinlgeItem.getExchangeRate() : null);
					preparedStatement.setObject(5,
							sinlgeItem.getSubLedgerId() != null ? sinlgeItem.getSubLedgerId() : null);
					preparedStatement.setString(6, sinlgeItem.getCredits() != null ? sinlgeItem.getCredits() : null);
					preparedStatement.setString(7, sinlgeItem.getDebits() != null ? sinlgeItem.getDebits() : null);
					preparedStatement.setInt(8, id);
					preparedStatement.setString(9,
							sinlgeItem.getAccountsName() != null ? sinlgeItem.getAccountsName() : null);
					preparedStatement.setString(10,
							sinlgeItem.getAccountsLevel() != null ? sinlgeItem.getAccountsLevel() : null);
					preparedStatement.setString(11,
							sinlgeItem.getSubLedgerName() != null ? sinlgeItem.getSubLedgerName() : null);
					preparedStatement.setString(12,
							sinlgeItem.getTotalCreditsEx() != null ? sinlgeItem.getTotalCreditsEx() : null);
					preparedStatement.setString(13,
							sinlgeItem.getTotalDebitsEx() != null ? sinlgeItem.getTotalDebitsEx() : null);
					preparedStatement.executeUpdate();
				}
			}

		} catch (Exception e) {
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(null, preparedStatement, null);
		}

	}
}

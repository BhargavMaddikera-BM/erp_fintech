package com.blackstrawai.accounting;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.DateConverter;
import com.blackstrawai.ap.dropdowns.BasicCurrencyVo;
import com.blackstrawai.attachments.AttachmentsConstants;
import com.blackstrawai.attachments.AttachmentsDao;
import com.blackstrawai.attachments.AttachmentsVo;
import com.blackstrawai.attachments.UploadFileVo;
import com.blackstrawai.chartofaccounts.ChartOfAccountsDao;
import com.blackstrawai.common.BaseDao;
import com.blackstrawai.common.CommonConstants;
import com.blackstrawai.common.FinanceCommonDao;
import com.blackstrawai.export.AccountingEntryExportVo;
import com.blackstrawai.export.AccountingEntryItemExportVo;
import com.blackstrawai.export.ExportVo;
import com.blackstrawai.onboarding.OrganizationDao;
import com.blackstrawai.onboarding.UserDao;
import com.blackstrawai.settings.CurrencyDao;
import com.blackstrawai.settings.VoucherDao;
import com.blackstrawai.settings.VoucherVo;
import com.blackstrawai.upload.AccountingEntriesUploadVo;

@Repository
public class AccountingAspectsDao extends BaseDao {

	@Autowired
	private AttachmentsDao attachmentsDao;

	@Autowired
	private FinanceCommonDao financeCommonDao;

	@Autowired
	private UserDao userDao;

	@Autowired
	private ChartOfAccountsDao chartOfAccountsDao;

	@Autowired
	private CurrencyDao currencyDao;

	@Autowired
	private OrganizationDao organizationDao;

	@Autowired
	private VoucherDao voucherDao;

	private Logger logger = Logger.getLogger(AccountingAspectsDao.class);

	public AccountingAspectsVo createAccountingAspects(AccountingAspectsVo accountingAspectsVo)
			throws ApplicationException {
		logger.info("Entry into createAccountingAspects");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = getUserMgmConnection();
			con.setAutoCommit(false);
			boolean isJournalNoExist = checkJournalNoExist(
					accountingAspectsVo.getAccountingAspectsGeneralInfo().getJournalNo(),
					accountingAspectsVo.getOrganizationId());
			if (isJournalNoExist) {
				throw new ApplicationException("Journal Number Exist for the Organization");
			}
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
			if (accountingAspectsVo.getId() != null) {
				createAccoutingAspectsBaseDetail(accountingAspectsVo.getId(), accountingAspectsVo.getItemDetails(),
						con);
				attachmentsDao.createAttachments(accountingAspectsVo.getOrganizationId(),accountingAspectsVo.getUserId(),accountingAspectsVo.getAttachments(),
						AttachmentsConstants.MODULE_TYPE_ACCOUNTING_ASPECTS, accountingAspectsVo.getId(),accountingAspectsVo.getRoleName());
			}
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
		logger.info("Entry into createAccoutingEntryBaseDetail");
		PreparedStatement preparedStatement =null;
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
		}
		finally{
			closeResources(null, preparedStatement, null);
		}

	}

	private boolean checkJournalNoExist(String journalNo, Integer organizationId) throws ApplicationException {
		logger.info("Enter into Method: checkJournalNoExist");
		Connection con =null;
		PreparedStatement preparedStatement=null;
		ResultSet rs = null;
		try {
			con = getAccountingConnection();
			preparedStatement = con.prepareStatement(AccountingAspectsConstants.CHECK_JOURNAL_NUMBER_EXIST);
			preparedStatement.setInt(1, organizationId);
			preparedStatement.setString(2, journalNo);
			rs = preparedStatement.executeQuery();
			if (rs.next()) {
				return true;
			}
		} catch (Exception e) {
			logger.info("Error in  checkJournalNoExist:", e);
			throw new ApplicationException(e.getMessage());
		}	finally{
			closeResources(rs, preparedStatement, con);
		}
		return false;
	}

	private AccountingAspectsBasicVo getAccountingEntryByJournal(String journalNo, Integer organizationId) throws ApplicationException {
		logger.info("Enter into Method: getAccountingEntryByJournal");
		Connection con =null;
		PreparedStatement preparedStatement=null;
		ResultSet rs = null;
		AccountingAspectsBasicVo data=null;
		try {
			con = getAccountingConnection();
			preparedStatement = con.prepareStatement(AccountingAspectsConstants.CHECK_JOURNAL_NUMBER_EXIST);
			preparedStatement.setInt(1, organizationId);
			preparedStatement.setString(2, journalNo);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				data=new AccountingAspectsBasicVo();
				data.setId(rs.getInt(1));
				break;
			}
		} catch (Exception e) {
			logger.info("Error in  checkJournalNoExist:", e);
			throw new ApplicationException(e.getMessage());
		}	finally{
			closeResources(rs, preparedStatement, con);
		}
		return data;
	}


	private AccountingAspectsItemsVo checkLedgerExistForJournal(String journalNo, Integer organizationId,String ledgerName) throws ApplicationException {
		logger.info("Enter into Method: checkLedgerExistForJournal");
		Connection con =null;
		PreparedStatement preparedStatement=null;
		ResultSet rs = null;
		try {
			con = getAccountingConnection();
			preparedStatement = con.prepareStatement(AccountingAspectsConstants.CHECK_LEDGER_EXIST);
			preparedStatement.setInt(1, organizationId);
			preparedStatement.setString(2, journalNo);
			preparedStatement.setString(3, ledgerName);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				AccountingAspectsItemsVo data=new AccountingAspectsItemsVo();
				data.setId(rs.getInt(1));
				data.setCredits(rs.getString(2));
				data.setDebits(rs.getString(3));
				return data;
			}
		} catch (Exception e) {
			logger.info("Error in  checkJournalNoExist:", e);
			throw new ApplicationException(e.getMessage());
		}	finally{
			closeResources(rs, preparedStatement, con);
		}
		return null;
	}
	
	
	private List<AccountingAspectsItemsVo> getAllItemsForJournal(String journalNo, Integer organizationId) throws ApplicationException {
		logger.info("Enter into Method: getAllItemsForJournal");
		Connection con =null;
		PreparedStatement preparedStatement=null;
		ResultSet rs = null;
		List<AccountingAspectsItemsVo> listAccountingItems=new ArrayList<AccountingAspectsItemsVo>();
		try {
			con = getAccountingConnection();			
			preparedStatement = con.prepareStatement(AccountingAspectsConstants.GET_CREDIT_DEBIT_FOR_JOURNAL);
			preparedStatement.setInt(1, organizationId);
			preparedStatement.setString(2, journalNo);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				AccountingAspectsItemsVo data=new AccountingAspectsItemsVo();
				data.setId(rs.getInt(1));
				data.setCredits(rs.getString(2));
				data.setDebits(rs.getString(3));
				data.setExchangeRate(rs.getString(4));
				listAccountingItems.add(data);
			}
		} catch (Exception e) {
			logger.info("Error in  checkJournalNoExist:", e);
			throw new ApplicationException(e.getMessage());
		}	finally{
			closeResources(rs, preparedStatement, con);
		}
		return listAccountingItems;
	}

	public void deleteAccountingAspects(Integer id, String status,String userId,String roleName) throws ApplicationException {
		logger.info("Entry into method: deleteAccountingAspects");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = getUserMgmConnection();
			String sql = AccountingAspectsConstants.DELETE_ACCOUNTING_ASPECTS;
			preparedStatement = con.prepareStatement(sql);
			preparedStatement.setString(1, status);
			preparedStatement.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
			preparedStatement.setString(3, userId);
			preparedStatement.setString(4, roleName);
			preparedStatement.setInt(5, id);
			preparedStatement.executeUpdate();
		} catch (Exception e) {
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, con);
		}
	}

	/*public List<AccountingAspectsBasicVo> getAllAccountingAspectsOfAnOrganization(int organizationId)
			throws ApplicationException {
		logger.info("Entry into getAllAccountingAspectsOfAnOrganization");
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		List<AccountingAspectsBasicVo> listOfAccountingEntries = new ArrayList<AccountingAspectsBasicVo>();
		try {

			con = getUserMgmConnection();
			String query = AccountingAspectsConstants.GET_ACCOUNTING_ASPECTS_INFO_BY_ORGANIZATION;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, organizationId);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				AccountingAspectsBasicVo accountingEntriesBasicdetails = new AccountingAspectsBasicVo();
				accountingEntriesBasicdetails.setDoc(rs.getDate(1));
				accountingEntriesBasicdetails.setJournalNo(rs.getString(2));
				Integer typeId = rs.getInt(3);
				accountingEntriesBasicdetails.setType(financeCommonDao.findAccountingTypeName(typeId));
				accountingEntriesBasicdetails.setStatus(rs.getString(4));
				accountingEntriesBasicdetails.setAmount(rs.getString(5));
				Integer currencyId = rs.getInt(6);
				accountingEntriesBasicdetails.setCurrency(currencyDao.getCurrencyName(currencyId));
				accountingEntriesBasicdetails.setId(rs.getInt(7));
				listOfAccountingEntries.add(accountingEntriesBasicdetails);
			}
		} catch (Exception e) {
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return listOfAccountingEntries;
	}*/



	public List<AccountingAspectsBasicVo> getAllAccountingAspectsOfAnOrganizationForUserAndRole(int organizationId,String userId,String roleName)
			throws ApplicationException {
		logger.info("Entry into getAllAccountingAspectsOfAnOrganizationForUserAndRole");
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		List<AccountingAspectsBasicVo> listOfAccountingEntries = new ArrayList<AccountingAspectsBasicVo>();
		try {

			con = getUserMgmConnection();
			String query="";
			if(roleName.equals("Super Admin")){
				query = AccountingAspectsConstants.GET_ACCOUNTING_ASPECTS_INFO_ORGANIZATION;
			}else{
				query = AccountingAspectsConstants.GET_ACCOUNTING_ASPECTS_INFO_BY_ORGANIZATION_USER_ROLE;
			}
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, organizationId);
			if(!(roleName.equals("Super Admin"))){
				preparedStatement.setString(2, userId);
				preparedStatement.setString(3, roleName);
			}		
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				AccountingAspectsBasicVo accountingEntriesBasicdetails = new AccountingAspectsBasicVo();
				accountingEntriesBasicdetails.setDoc(rs.getDate(1));
				accountingEntriesBasicdetails.setJournalNo(rs.getString(2));
				Integer typeId = rs.getInt(3);
				accountingEntriesBasicdetails.setType(financeCommonDao.findAccountingTypeName(typeId));
				accountingEntriesBasicdetails.setStatus(rs.getString(4));
				accountingEntriesBasicdetails.setAmount(rs.getString(5));
				Integer currencyId = rs.getInt(6);
				accountingEntriesBasicdetails.setCurrency(currencyDao.getCurrencyName(currencyId));
				accountingEntriesBasicdetails.setId(rs.getInt(7));
				listOfAccountingEntries.add(accountingEntriesBasicdetails);
			}
		} catch (Exception e) {
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return listOfAccountingEntries;
	}



	public AccountingAspectsVo getAccountingAspectsById(Integer id) throws ApplicationException {
		logger.info("Entry into method: getAccountingEntriesById");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		AccountingAspectsVo accountingAspectsVo = null;
		try {
			if (id != null) {
				con = getUserMgmConnection();
				accountingAspectsVo = getAccountingAspectsGeneralInfo(id, con);
				if (accountingAspectsVo != null) {
					accountingAspectsVo.setRoleName(accountingAspectsVo.getAccountingAspectsGeneralInfo().getRoleNameTemp());
					accountingAspectsVo.setItemDetails(getItemDetails(id, con, accountingAspectsVo));
					List<UploadFileVo> uploadFileVos = new ArrayList<UploadFileVo>();
					for (AttachmentsVo attachments : attachmentsDao.getAttachmentDetails(id,
							AttachmentsConstants.MODULE_TYPE_ACCOUNTING_ASPECTS)) {
						UploadFileVo uploadFileVo = new UploadFileVo();
						uploadFileVo.setId(attachments.getId());
						uploadFileVo.setName(attachments.getFileName());
						uploadFileVo.setSize(attachments.getSize());
						uploadFileVos.add(uploadFileVo);
					}
					accountingAspectsVo.setAttachments(uploadFileVos);
				}
			}
		} catch (ApplicationException e) {
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return accountingAspectsVo;
	}

	private AccountingAspectsVo getAccountingAspectsGeneralInfo(Integer id, Connection con)
			throws ApplicationException {
		logger.info("Entry into method: getAccountingEntriesGeneralInfo");
		String query = AccountingAspectsConstants.GET_ACCOUNTING_ASPECTS_GENERAL_INFO_BY_ID;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		AccountingAspectsGeneralVo accountingEntriesGeneralVo = new AccountingAspectsGeneralVo();
		AccountingAspectsVo accountingEntriesVo = new AccountingAspectsVo();
		AccountingAspectsApproversVo approvers = new AccountingAspectsApproversVo();
		try {
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, id);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				accountingEntriesVo.setId(rs.getInt(1));
				accountingEntriesGeneralVo.setDateOfCreation(rs.getDate(2));
				accountingEntriesGeneralVo.setTypeId(rs.getInt(3));
				accountingEntriesGeneralVo.setJournalNo(rs.getString(4));
				accountingEntriesGeneralVo.setLocationId(rs.getInt(5));
				approvers.setApprover1(rs.getInt(6));
				approvers.setApprover2(rs.getInt(7));
				approvers.setApprover3(rs.getInt(8));
				accountingEntriesGeneralVo.setCurrencyId(rs.getInt(9));
				accountingEntriesGeneralVo.setNotes(rs.getString(10));
				accountingEntriesGeneralVo.setTotalCredits(rs.getString(11));
				accountingEntriesGeneralVo.setTotalDebits(rs.getString(12));
				accountingEntriesGeneralVo.setDifference(rs.getString(13));
				accountingEntriesVo.setStatus(rs.getString(14));
				accountingEntriesVo.setUserId(rs.getString(15));
				accountingEntriesVo.setOrganizationId(rs.getInt(16));
				accountingEntriesVo.setIsSuperAdmin(rs.getBoolean(17));
				accountingEntriesGeneralVo.setIsRegisteredLocation(rs.getBoolean(18));
				accountingEntriesGeneralVo.setCreate_ts(rs.getDate(19));
				accountingEntriesGeneralVo.setRoleNameTemp(rs.getString(20));
				accountingEntriesVo.setAccountingAspectsGeneralInfo(accountingEntriesGeneralVo);
				accountingEntriesVo.setApproversList(approvers);
			}
		} catch (Exception e) {
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, null);
		}
		return accountingEntriesVo;
	}

	private List<AccountingAspectsItemsVo> getItemDetails(Integer id, Connection con,
			AccountingAspectsVo accountingEntriesVo) throws ApplicationException {
		logger.info("Entry into method: getItemDetails");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		List<AccountingAspectsItemsVo> listOfitems = new ArrayList<AccountingAspectsItemsVo>();
		try {
			String query = AccountingAspectsConstants.GET_ACCOUNTING_ASPECTS_ITEMS_BY_ID;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, id);
			preparedStatement.setString(2, CommonConstants.STATUS_AS_DELETE);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				AccountingAspectsItemsVo accountingEntriesItemsVo = new AccountingAspectsItemsVo();
				accountingEntriesItemsVo.setId(rs.getInt(1));
				accountingEntriesItemsVo.setAccountsId(rs.getInt(2));
				accountingEntriesItemsVo.setSubLedgerId(rs.getInt(3));
				accountingEntriesItemsVo.setDescription(rs.getString(4));
				accountingEntriesItemsVo.setCurrencyId(rs.getInt(5));
				accountingEntriesItemsVo.setExchangeRate(rs.getString(6));
				accountingEntriesItemsVo.setCredits(rs.getString(7));
				accountingEntriesItemsVo.setDebits(rs.getString(8));
				accountingEntriesItemsVo.setStatus(rs.getString(9));
				accountingEntriesItemsVo.setAccountingEntriesId(rs.getInt(10));
				accountingEntriesItemsVo.setAccountsLevel(rs.getString(11));
				accountingEntriesItemsVo.setAccountsName(rs.getString(12));
				accountingEntriesItemsVo.setSubLedgerName(rs.getString(13));
				accountingEntriesItemsVo.setTotalCreditsEx(rs.getString(14));
				accountingEntriesItemsVo.setTotalDebitsEx(rs.getString(15));
				listOfitems.add(accountingEntriesItemsVo);
			}
		} catch (Exception e) {
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, null);
		}
		return listOfitems;
	}

	public AccountingAspectsVo updateAccountingAspects(AccountingAspectsVo accountingAspectsVo)
			throws ApplicationException {
		logger.info("Entry into method: updateAccountingEntries");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		if (accountingAspectsVo.getId() != null) {
			try {
				con = getUserMgmConnection();
				con.setAutoCommit(false);
				updateAccountingAspectsGeneralInfo(accountingAspectsVo, con);
				if (accountingAspectsVo.getItemDetails() != null && accountingAspectsVo.getItemDetails().size() > 0) {
					updateEccountingAspectsItemsDetail(accountingAspectsVo.getItemDetails(),
							accountingAspectsVo.getId(), con);
				}
				if (accountingAspectsVo.getItemsToRemove() != null
						&& accountingAspectsVo.getItemsToRemove().size() > 0) {
					for (Integer itemId : accountingAspectsVo.getItemsToRemove()) {
						changeStatusForSingleItem(itemId, CommonConstants.STATUS_AS_DELETE, con);
					}
				}
				if (accountingAspectsVo.getAttachments() != null && accountingAspectsVo.getAttachments().size() > 0) {
					attachmentsDao.createAttachments(accountingAspectsVo.getOrganizationId(),accountingAspectsVo.getUserId(),accountingAspectsVo.getAttachments(),
							AttachmentsConstants.MODULE_TYPE_ACCOUNTING_ASPECTS, accountingAspectsVo.getId(),accountingAspectsVo.getRoleName());
				}
				if (accountingAspectsVo.getAttachmentsToRemove() != null
						&& accountingAspectsVo.getAttachmentsToRemove().size() > 0) {
					for (Integer attachmentId : accountingAspectsVo.getAttachmentsToRemove()) {
						attachmentsDao.changeStatusForSingleAttachment(attachmentId, CommonConstants.STATUS_AS_DELETE,accountingAspectsVo.getUserId(),accountingAspectsVo.getRoleName());
					}
				}
				con.commit();
			} catch (Exception e) {
				logger.info("Error in updateAccountingEntries:: ", e);
				try {
					con.rollback();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					throw new ApplicationException(e1.getMessage());
				}
				throw new ApplicationException(e.getMessage());
			} finally {
				closeResources(rs, preparedStatement, con);
			}
		}
		return accountingAspectsVo;
	}

	private void changeStatusForSingleItem(Integer itemId, String status, Connection con) throws ApplicationException {
		logger.info("To Change the status in attachment for attachment Id ::" + itemId);
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		if (itemId != null && status != null) {
			try {
				String sql = AccountingAspectsConstants.CHANGE_SINGLE_ITEM_STATUS;
				preparedStatement = con.prepareStatement(sql);
				preparedStatement.setString(1, status);
				preparedStatement.setDate(2, new Date(System.currentTimeMillis()));
				preparedStatement.setInt(3, itemId);
				preparedStatement.executeUpdate();
			} catch (Exception e) {
				logger.info("Error in changeStatusForAttachments ", e);
				throw new ApplicationException(e.getMessage());
			} finally {
				closeResources(rs, preparedStatement, null);
			}
		}
	}

	private void updateAccountingAspectsGeneralInfo(AccountingAspectsVo accountingEntriesVo, Connection con)
			throws ApplicationException {
		logger.info("Entry into method: updateAccountingEntriesGeneralInfo");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			String sql = AccountingAspectsConstants.UPDATE_ACCOUNTING_ASPECTS_GENERAL_INFORMATION;
			preparedStatement = con.prepareStatement(sql);
			preparedStatement.setDate(1,
					accountingEntriesVo.getAccountingAspectsGeneralInfo().getDateOfCreation() != null
					? accountingEntriesVo.getAccountingAspectsGeneralInfo().getDateOfCreation()
							: null);
			preparedStatement.setInt(2,
					accountingEntriesVo.getApproversList().getApprover1() != null
					? accountingEntriesVo.getApproversList().getApprover1()
							: 0);
			preparedStatement.setInt(3,
					accountingEntriesVo.getApproversList().getApprover2() != null
					? accountingEntriesVo.getApproversList().getApprover2()
							: 0);
			preparedStatement.setInt(4,
					accountingEntriesVo.getApproversList().getApprover3() != null
					? accountingEntriesVo.getApproversList().getApprover3()
							: 0);
			preparedStatement.setInt(5,
					accountingEntriesVo.getAccountingAspectsGeneralInfo().getCurrencyId() != null
					? accountingEntriesVo.getAccountingAspectsGeneralInfo().getCurrencyId()
							: 0);
			preparedStatement.setString(6,
					accountingEntriesVo.getAccountingAspectsGeneralInfo().getJournalNo() != null
					? accountingEntriesVo.getAccountingAspectsGeneralInfo().getJournalNo()
							: null);
			preparedStatement.setInt(7,
					accountingEntriesVo.getAccountingAspectsGeneralInfo().getLocationId() != null
					? accountingEntriesVo.getAccountingAspectsGeneralInfo().getLocationId()
							: 0);
			preparedStatement.setString(8,
					accountingEntriesVo.getAccountingAspectsGeneralInfo().getNotes() != null
					? accountingEntriesVo.getAccountingAspectsGeneralInfo().getNotes()
							: null);
			preparedStatement.setInt(9,
					accountingEntriesVo.getAccountingAspectsGeneralInfo().getTypeId() != null
					? accountingEntriesVo.getAccountingAspectsGeneralInfo().getTypeId()
							: 0);
			preparedStatement.setString(10,
					accountingEntriesVo.getAccountingAspectsGeneralInfo().getTotalCredits() != null
					? accountingEntriesVo.getAccountingAspectsGeneralInfo().getTotalCredits()
							: null);
			preparedStatement.setString(11,
					accountingEntriesVo.getAccountingAspectsGeneralInfo().getTotalDebits() != null
					? accountingEntriesVo.getAccountingAspectsGeneralInfo().getTotalDebits()
							: null);
			preparedStatement.setString(12,
					accountingEntriesVo.getAccountingAspectsGeneralInfo().getDifference() != null
					? accountingEntriesVo.getAccountingAspectsGeneralInfo().getDifference()
							: null);
			preparedStatement.setInt(13,
					accountingEntriesVo.getOrganizationId() != null ? accountingEntriesVo.getOrganizationId() : null);
			preparedStatement.setInt(14,
					Integer.valueOf(accountingEntriesVo.getUserId()) != null
					? Integer.valueOf(accountingEntriesVo.getUserId())
							: 0);
			preparedStatement.setBoolean(15,
					accountingEntriesVo.getIsSuperAdmin() != null ? accountingEntriesVo.getIsSuperAdmin() : null);
			preparedStatement.setTimestamp(16, new Timestamp(System.currentTimeMillis()));
			preparedStatement.setBoolean(17,
					accountingEntriesVo.getAccountingAspectsGeneralInfo().getIsRegisteredLocation() != null
					? accountingEntriesVo.getAccountingAspectsGeneralInfo().getIsRegisteredLocation()
							: null);
			preparedStatement.setString(18,
					accountingEntriesVo.getStatus() != null ? accountingEntriesVo.getStatus() : null);
			preparedStatement.setString(19, accountingEntriesVo.getRoleName());
			preparedStatement.setInt(20, accountingEntriesVo.getId());
			preparedStatement.executeUpdate();
		} catch (Exception e) {
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, null);
		}

	}

	private void updateEccountingAspectsItemsDetail(List<AccountingAspectsItemsVo> accountingEntriesItemsList,
			Integer id, Connection con) throws ApplicationException {
		logger.info("Entry into method: updateAccountingEntriesItemsDetail");
		for (int i = 0; i < accountingEntriesItemsList.size(); i++) {
			AccountingAspectsItemsVo accountingEntriesItemsVo = accountingEntriesItemsList.get(i);
			String status = accountingEntriesItemsList.get(i).getStatus();
			if (status.equalsIgnoreCase(CommonConstants.STATUS_AS_ACTIVE)) {
				updateAccountingAspectsItems(accountingEntriesItemsVo, status, con);
			} else if (status.equalsIgnoreCase(CommonConstants.STATUS_AS_NEW)) {
				createAccountingAspectsItems(accountingEntriesItemsVo, id, con);
			}
		}
	}

	private void createAccountingAspectsItems(AccountingAspectsItemsVo accountingEntriesItemsVo, Integer id,
			Connection con) throws ApplicationException {
		logger.info("Entry into createAccountingEntriesItems");
		PreparedStatement preparedStatement=null;
		try  {
			preparedStatement = con.prepareStatement(AccountingAspectsConstants.INSERT_INTO_ACCOUTING_ASPECTS_ITEM);
			preparedStatement.setInt(1,
					accountingEntriesItemsVo.getAccountsId() != null ? accountingEntriesItemsVo.getAccountsId() : 0);
			preparedStatement.setInt(2,
					accountingEntriesItemsVo.getCurrencyId() != null ? accountingEntriesItemsVo.getCurrencyId() : 0);
			preparedStatement.setString(3,
					accountingEntriesItemsVo.getDescription() != null ? accountingEntriesItemsVo.getDescription()
							: null);
			preparedStatement.setString(4,
					accountingEntriesItemsVo.getExchangeRate() != null ? accountingEntriesItemsVo.getExchangeRate()
							: null);
			preparedStatement.setInt(5,
					accountingEntriesItemsVo.getSubLedgerId() != null ? accountingEntriesItemsVo.getSubLedgerId() : 0);
			preparedStatement.setString(6,
					accountingEntriesItemsVo.getCredits() != null ? accountingEntriesItemsVo.getCredits() : null);
			preparedStatement.setString(7,
					accountingEntriesItemsVo.getDebits() != null ? accountingEntriesItemsVo.getDebits() : null);
			preparedStatement.setInt(8, id);
			preparedStatement.setString(9,
					accountingEntriesItemsVo.getAccountsName() != null ? accountingEntriesItemsVo.getAccountsName()
							: null);
			preparedStatement.setString(10,
					accountingEntriesItemsVo.getAccountsLevel() != null ? accountingEntriesItemsVo.getAccountsLevel()
							: null);
			preparedStatement.setString(11,
					accountingEntriesItemsVo.getSubLedgerName() != null ? accountingEntriesItemsVo.getSubLedgerName()
							: null);
			preparedStatement.setString(12,
					accountingEntriesItemsVo.getTotalCreditsEx() != null ? accountingEntriesItemsVo.getTotalCreditsEx()
							: null);
			preparedStatement.setString(13,
					accountingEntriesItemsVo.getTotalDebitsEx() != null ? accountingEntriesItemsVo.getTotalDebitsEx()
							: null);
			preparedStatement.executeUpdate();

		} catch (Exception e) {
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(null, preparedStatement, null);
		}

	}

	private void updateAccountingAspectsItems(AccountingAspectsItemsVo accountingEntriesItemsVo, String status,
			Connection con) throws ApplicationException {
		logger.info("Entry into updateAccountingEntriesItems");
		PreparedStatement preparedStatement =null;
		try  {
			preparedStatement = con.prepareStatement(AccountingAspectsConstants.UPDATE_ACCOUNTING_ASPECTS_ITEM_INFORMATION);
			preparedStatement.setInt(1,
					accountingEntriesItemsVo.getAccountsId() != null ? accountingEntriesItemsVo.getAccountsId() : null);
			preparedStatement.setInt(2,
					accountingEntriesItemsVo.getCurrencyId() != null ? accountingEntriesItemsVo.getCurrencyId() : null);
			preparedStatement.setString(3,
					accountingEntriesItemsVo.getDescription() != null ? accountingEntriesItemsVo.getDescription()
							: null);
			preparedStatement.setString(4,
					accountingEntriesItemsVo.getExchangeRate() != null ? accountingEntriesItemsVo.getExchangeRate()
							: null);
			preparedStatement.setInt(5,
					accountingEntriesItemsVo.getSubLedgerId() != null ? accountingEntriesItemsVo.getSubLedgerId()
							: null);
			preparedStatement.setString(6,
					accountingEntriesItemsVo.getCredits() != null ? accountingEntriesItemsVo.getCredits() : null);
			preparedStatement.setString(7,
					accountingEntriesItemsVo.getDebits() != null ? accountingEntriesItemsVo.getDebits() : null);
			if (status.equalsIgnoreCase(CommonConstants.STATUS_AS_DELETE)) {
				preparedStatement.setString(8, CommonConstants.STATUS_AS_DELETE);
			} else {
				preparedStatement.setString(8, CommonConstants.STATUS_AS_ACTIVE);
			}
			preparedStatement.setTimestamp(9, new Timestamp(System.currentTimeMillis()));

			preparedStatement.setString(10,
					accountingEntriesItemsVo.getAccountsName() != null ? accountingEntriesItemsVo.getAccountsName()
							: null);
			preparedStatement.setString(11,
					accountingEntriesItemsVo.getAccountsLevel() != null ? accountingEntriesItemsVo.getAccountsLevel()
							: null);
			preparedStatement.setString(12,
					accountingEntriesItemsVo.getSubLedgerName() != null ? accountingEntriesItemsVo.getSubLedgerName()
							: null);
			preparedStatement.setString(13,
					accountingEntriesItemsVo.getTotalCreditsEx() != null ? accountingEntriesItemsVo.getTotalCreditsEx()
							: null);
			preparedStatement.setString(14,
					accountingEntriesItemsVo.getTotalDebitsEx() != null ? accountingEntriesItemsVo.getTotalDebitsEx()
							: null);
			preparedStatement.setInt(15, accountingEntriesItemsVo.getId());
			preparedStatement.executeUpdate();
		} catch (Exception e) {
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(null, preparedStatement, null);
		}

	}

	// To delete Accounting Aspects table entries
	public void deleteAccountingEntries(Integer id,String userId,String roleName) throws ApplicationException {
		logger.info("To deleteVendorEntries:: ");
		Connection connection = null;
		if (id != null) {
			try {
				connection = getUserMgmConnection();
				// To remove from AccountingAspects genral info table
				changeStatusForAccountingAspectsTables(id, CommonConstants.STATUS_AS_DELETE, connection,
						AccountingAspectsConstants.MODIFY_ACCOUNTING_ASPECTS_STATUS);
				// To remove from AccountingAspects Items table
				changeStatusForAccountingAspectsTables(id, CommonConstants.STATUS_AS_DELETE, connection,
						AccountingAspectsConstants.MODIFY_ACCOUNTING_ASPECTS_ITMES_STATUS);
				// To remove from Attachments table
				attachmentsDao.changeStatusForAttachments(id, CommonConstants.STATUS_AS_DELETE,
						AttachmentsConstants.MODULE_TYPE_ACCOUNTING_ASPECTS,userId,roleName);
				logger.info("Deleted successfully in all tables ");
			} catch (ApplicationException e) {
				logger.info("Error in deleteAccountingEntries:: ", e);
				throw new ApplicationException(e.getMessage());
			} finally {
				closeResources(null, null, connection);
			}
		}

	}

	private void changeStatusForAccountingAspectsTables(Integer id, String status, Connection con, String query)
			throws ApplicationException {
		logger.info("To Change the status with query :: " + query);
		PreparedStatement preparedStatement =null;
		if (query != null) {
			try  {
				preparedStatement = con.prepareStatement(query);
				preparedStatement.setString(1, status);
				preparedStatement.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
				preparedStatement.setInt(3, id);
				preparedStatement.executeUpdate();
				logger.info("Status chaneged successfully ");
			} catch (Exception e) {
				logger.info("Error in changeAccountingAspectstablesStatus ", e);
				throw new ApplicationException(e.getMessage());
			} finally {
				closeResources(null, preparedStatement, null);
			}
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<AccountingAspectsBasicVo> getAccountingAspectsFilterData(AccountingAspectsFilterVo filterVo)
			throws ApplicationException {
		Connection connection = null;
		List<AccountingAspectsBasicVo> accountingAspectsList = new ArrayList<AccountingAspectsBasicVo>();
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			connection = getUserMgmConnection();
			closeResources(null, null, connection);
			StringBuilder filterQuery = new StringBuilder(
					AccountingAspectsConstants.GET_ALL_FILTERED_ACOOUNTING_ASPECTS);
			List paramsList = new ArrayList<>();
			paramsList.add(filterVo.getOrgId());
			// paramsList.add(CommonConstants.STATUS_AS_DELETE);
			if (filterVo.getStatus() != null && !(filterVo.getStatus().isEmpty())) {
				filterQuery.append(" and status = ?");
				paramsList.add(filterVo.getStatus());
			}

			if (filterVo.getStartDate() != null && filterVo.getEndDate() != null) {
				filterQuery.append(" and date_of_creation between ? and ?");
				paramsList.add(filterVo.getStartDate());
				paramsList.add(filterVo.getEndDate());
			}

			if (filterVo.getUserId() != null && !(filterVo.getUserId().isEmpty())) {
				filterQuery.append(" and user_id = ?");
				paramsList.add(filterVo.getUserId());
			}

			if (filterVo.getRoleName() != null && !(filterVo.getRoleName().isEmpty())) {
				filterQuery.append(" and role_name = ?");
				paramsList.add(filterVo.getRoleName());
			}

			logger.info(filterQuery.toString());
			logger.info(paramsList);
			connection = getUserMgmConnection();
			preparedStatement = connection.prepareStatement(filterQuery.toString());

			// To iterate list and setparam index
			int counter = 1;
			for (int i = 0; i < paramsList.size(); i++) {
				logger.info(counter);
				preparedStatement.setObject(counter, paramsList.get(i));
				counter++;
			}
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				AccountingAspectsBasicVo accountingAspectsBasicVo = new AccountingAspectsBasicVo();
				accountingAspectsBasicVo.setId(rs.getInt(1));
				accountingAspectsBasicVo.setJournalNo(rs.getString(2));
				accountingAspectsBasicVo.setDoc(rs.getDate(3));
				accountingAspectsBasicVo.setUserId(rs.getString(4));
				accountingAspectsBasicVo.setStatus(rs.getString(5));
				accountingAspectsBasicVo.setAmount(rs.getString(6));
				Integer currencyId = rs.getInt(7);
				accountingAspectsBasicVo.setCurrency(currencyDao.getCurrencyName(currencyId));
				Integer typeId = rs.getInt(8);
				accountingAspectsBasicVo.setType(financeCommonDao.findAccountingTypeName(typeId));
				accountingAspectsList.add(accountingAspectsBasicVo);
			}
			logger.info(accountingAspectsList.size());
			logger.info("Retrieved AccountingAspectsList ");
		} catch (Exception e) {
			logger.info("Error in getAccountingAspectsList:: ", e);
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, connection);
		}
		return accountingAspectsList;
	}

	public AccountingEntriesUploadVo processUpload(List<AccountingEntriesUploadVo> accountingEntriesList, Integer orgId, String userId,
			boolean isSuperAdmin, boolean duplicacy,String roleName) throws ApplicationException {
		logger.info("Enter into Method: processUpload");
		Integer typeId = null;
		Integer locationId = null;
		Integer ledgerId = null;
		Integer subLedgerId = null;
		Integer currencyId = null;
		Integer accountingEntryId = null;
		Connection con = null;
		boolean isJournalTransactionRequired=false;
		boolean isCreate=false;
		int count = 0;
		con = getUserMgmConnection();
		String dateFormat=organizationDao.getDefaultDateForOrganization(orgId,con);
		closeResources(null, null, con);
		String journalNo = accountingEntriesList.get(0).getJournalNo();
		try {
			Double debit=0.0;
			Double credit=0.0;
			for(int i=0;i<accountingEntriesList.size();i++){
				AccountingEntriesUploadVo data=accountingEntriesList.get(i);
				credit=credit+(Double.parseDouble(data.getExchangeRate())*Double.parseDouble(data.getCredits()));
				debit=debit+(Double.parseDouble(data.getExchangeRate())*Double.parseDouble(data.getDebits()));
			}
			con = getAccountingConnection();
			con.setAutoCommit(false);
			if (journalNo != null) {
				for (AccountingEntriesUploadVo accountingEntry : accountingEntriesList) {
					if (accountingEntry.getType() != null) {
						typeId = financeCommonDao.getAccountingTypes(accountingEntry.getType());
						if (typeId == null) {
							throw new ApplicationException("Accounting Type is Invalid");
						}
					}				

					if (accountingEntry.getLocation() != null) {
						if (accountingEntry.getLocation().equalsIgnoreCase("Registered Address")) {
							locationId = organizationDao.getGSTDetails(orgId);
						} else {
							locationId = organizationDao.getGstLocationId(accountingEntry.getLocation(), orgId);
						}
						if (locationId == null) {
							throw new ApplicationException("Location is Invalid");
						}
					}

					if (accountingEntry.getLedger() != null) {
						ledgerId = chartOfAccountsDao.getAccountingEntryLedgerId(accountingEntry.getLedger(), orgId);
						if (ledgerId == null) {
							throw new ApplicationException("Ledger Name is Invalid");
						}
					}

					if(accountingEntry.getJournalNo()!=null){
						int length=0;
						try{
							VoucherVo voucherVo=voucherDao.getVoucherBaseOnType(orgId,"Accounting Entries");
							length=Integer.parseInt(voucherVo.getMinimumDigits());							
							if(accountingEntry.getJournalNo().length()>length){
								throw new ApplicationException("Journal no. should be a "+ length +" digit number.");
							}
							Integer.parseInt(accountingEntry.getJournalNo());

						}catch(Exception e){
							throw new ApplicationException("Journal no. should be a "+ length +" digit number.");
						}
					}	

					if(accountingEntry.getDate()!=null){
						try{
							SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
							simpleDateFormat.parse(accountingEntry.getDate());							
						}catch(Exception e){
							throw new ApplicationException("Date Format should be:"+dateFormat);

						}

					}

					/*	if (accountingEntry.getSubLedger() != null && accountingEntry.getSubLedger().trim().length()>0) {
						subLedgerId = chartOfAccountsDao.getAccountingEntryBySubLedgerId(accountingEntry.getSubLedger(), orgId);					  
						if (subLedgerId == null){ 
							throw new ApplicationException("Sub Ledger Name is Invalid"); 
						}					 
					}			*/		 

					if (accountingEntry.getCurrency() != null) {
						currencyId = currencyDao.getCurrencyId(accountingEntry.getCurrency(), orgId);
						if (currencyId == null) {
							throw new ApplicationException("Invalid Currency");
						}
					}

					Boolean isJournalNoExist = checkJournalNoExist(journalNo, orgId);
					if (isJournalNoExist) {
						if(!isCreate){
							AccountingAspectsBasicVo accountingAspectsBasicVo = getAccountingEntryByJournal(journalNo, orgId);
							if(accountingAspectsBasicVo!=null){
								accountingEntryId=accountingAspectsBasicVo.getId();
								Double totalDebitExValue=new Double(accountingEntry.getExchangeRate()) * new Double(accountingEntry.getDebits());
								Double totalCreditExValue=new Double(accountingEntry.getExchangeRate()) * new Double(accountingEntry.getCredits());
								accountingEntry.setTotalDebitsEx(totalDebitExValue.toString());
								accountingEntry.setTotalCreditsEx(totalCreditExValue.toString());
								AccountingAspectsItemsVo accountingAspectsItemsVo=checkLedgerExistForJournal(journalNo,orgId,accountingEntry.getLedger());
								if(accountingAspectsItemsVo!=null){		
									PreparedStatement preparedStatement = con.prepareStatement(AccountingAspectsConstants.UPDATE_ACCOUNTING_ASPECTS_ITEM_INFORMATION_MINIMAL);
									preparedStatement.setString(1,accountingEntry.getExchangeRate());
									preparedStatement.setString(2,accountingEntry.getCredits());
									preparedStatement.setString(3,accountingEntry.getDebits());								
									preparedStatement.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
									preparedStatement.setString(5,accountingEntry.getTotalCreditsEx());
									preparedStatement.setString(6,accountingEntry.getTotalDebitsEx());
									preparedStatement.setLong(7, accountingAspectsItemsVo.getId());
									preparedStatement.executeUpdate();

								}else{
									createAccountingEntriesItemsUpload(accountingEntry, accountingEntryId, ledgerId,
											subLedgerId, currencyId, con);
								}
							}
						}						

					} else {
						if (count < 1) {
							accountingEntry.setDifference(new Double(credit-debit).toString());
							if(credit-debit>0 || credit-debit<0){
								accountingEntry.setStatus("DRAFT");
							}else{
								accountingEntry.setStatus("ACT");
							}
							accountingEntry.setTotalDebits(new Double(debit).toString());
							accountingEntry.setTotalCredits(new Double(credit).toString());	
							BasicCurrencyVo currencyData=currencyDao.getDefaultCurrencyForOrganization(orgId, con);
							accountingEntryId = createAccountingEntriesUpload(accountingEntry, typeId, locationId,
									orgId, userId, isSuperAdmin, con,roleName,currencyData.getId());
							isJournalTransactionRequired=true;
							isCreate=true;
						}
						if (accountingEntryId != null) {
							Double totalDebitExValue=new Double(accountingEntry.getExchangeRate()) * new Double(accountingEntry.getDebits());
							Double totalCreditExValue=new Double(accountingEntry.getExchangeRate()) * new Double(accountingEntry.getCredits());
							accountingEntry.setTotalDebitsEx(totalDebitExValue.toString());
							accountingEntry.setTotalCreditsEx(totalCreditExValue.toString());
							createAccountingEntriesItemsUpload(accountingEntry, accountingEntryId, ledgerId,
									subLedgerId, currencyId, con);

						}

					}
					count++;
				}
				con.commit();
				closeResources(null, null, con);
				
				String status="DRAFT";
				if(!isCreate){
					Double totalDebits_lc=0.0;
					Double totalCredits_lc=0.0;
					List<AccountingAspectsItemsVo> listData=getAllItemsForJournal(journalNo,orgId);
					for(int i=0;i<listData.size();i++){
						AccountingAspectsItemsVo data=listData.get(i);
						Double debitAfterExchangeRate=(Double.parseDouble(data.getDebits())) * (Double.parseDouble(data.getExchangeRate()));
						Double creditAfterExchangeRate=(Double.parseDouble(data.getCredits())) * (Double.parseDouble(data.getExchangeRate()));
						totalDebits_lc=totalDebits_lc + debitAfterExchangeRate; 
						totalCredits_lc=totalCredits_lc + creditAfterExchangeRate;						
					}
					
					/*AccountingAspectsVo data=getAccountingAspectsById(accountingEntryId);
					String totalDebits=data.getAccountingAspectsGeneralInfo().getTotalDebits();
					String totalCredits=data.getAccountingAspectsGeneralInfo().getTotalCredits();*/
					
					/*if(totalDebits!=null){
						totalDebits_lc=Double.parseDouble(totalDebits)+debit;
					}
					if(totalCredits!=null){
						totalCredits_lc=Double.parseDouble(totalCredits)+credit;
					}*/
					Double difference=totalCredits_lc-totalDebits_lc;
					PreparedStatement preparedStatement = null;
					ResultSet rs = null;
					con=getUserMgmConnection();
					if((difference==0)|| (difference==0.0)){
						status="ACT";
					}
					try {
						String sql = AccountingAspectsConstants.UPDATE_GENERAL_INFO;
						preparedStatement = con.prepareStatement(sql);
						preparedStatement.setString(1, status);
						preparedStatement.setString(2,  totalCredits_lc.toString());
						preparedStatement.setString(3,totalDebits_lc.toString());
						preparedStatement.setString(4,difference.toString());						
						preparedStatement.setInt(5, accountingEntryId);
						preparedStatement.executeUpdate();
					}catch (Exception e) {
						throw new ApplicationException(e);
					} finally {
						closeResources(rs, preparedStatement, con);
					}

				}


				if(isCreate){
					if((isJournalTransactionRequired) && (credit-debit==0 || credit-debit==0.0)){
						AccountingEntriesUploadVo accountingEntriesUploadVo=new AccountingEntriesUploadVo();
						accountingEntriesUploadVo.setStatus("ACT");
						accountingEntriesUploadVo.setJournalNo(accountingEntryId.toString());
						return accountingEntriesUploadVo;
					}
				}else{
					if(status.equals("ACT")){
						AccountingEntriesUploadVo accountingEntriesUploadVo=new AccountingEntriesUploadVo();
						accountingEntriesUploadVo.setStatus("ACT");
						accountingEntriesUploadVo.setJournalNo(accountingEntryId.toString());
						return accountingEntriesUploadVo;
					}
				}

			}
		} catch (Exception e) {
			try {
				con.rollback();
			} catch (Exception e1) {
				throw new ApplicationException(e1.getMessage());
			}			
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(null, null, con);
		}
		return null;

	}







	private void createAccountingEntriesItemsUpload(AccountingEntriesUploadVo accountingEntriesUploadVo,
			Integer accountingEntryId, Integer ledgerId, Integer subLedgerId, Integer currencyId, Connection con)
					throws ApplicationException {
		logger.info("Enter into Method: createAccountingEntriesItemsUpload");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {			
			String sql = AccountingAspectsConstants.CREATE_ACCOUTING_ENTRY_ITEM_UPLOAD;
			preparedStatement = con.prepareStatement(sql);
			preparedStatement.setObject(1, ledgerId != null ? ledgerId : null);
			preparedStatement.setString(2, accountingEntriesUploadVo.getLedger()!=null?accountingEntriesUploadVo.getLedger():null);	
			preparedStatement.setString(3, "L5");
			preparedStatement.setObject(4, subLedgerId != null ? subLedgerId : null);
			preparedStatement.setObject(5, currencyId != null ? currencyId : null);
			preparedStatement.setString(6,
					accountingEntriesUploadVo.getExchangeRate() != null ? accountingEntriesUploadVo.getExchangeRate()
							: null);
			preparedStatement.setString(7,
					accountingEntriesUploadVo.getCredits() != null ? accountingEntriesUploadVo.getCredits() : null);
			preparedStatement.setString(8,
					accountingEntriesUploadVo.getDebits() != null ? accountingEntriesUploadVo.getDebits() : null);
			preparedStatement.setObject(9, accountingEntryId != null ? accountingEntryId : null);
			preparedStatement.setString(10, accountingEntriesUploadVo.getTotalDebitsEx());
			preparedStatement.setString(11, accountingEntriesUploadVo.getTotalCreditsEx());
			preparedStatement.setString(12, accountingEntriesUploadVo.getSubLedger());
			preparedStatement.executeUpdate();
		} catch (Exception e) {
			logger.info("Error in  createAccountingEntriesItemsUpload:", e);
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, null);
		}

	}

	private String convertDateVal(String date){
		try
		{
			int firstIndexIdx=date.indexOf("/");		
			int lastIndexIdx=date.lastIndexOf("/");
			if(firstIndexIdx==1){
				String val="";
				String firstIndex="0"+date.substring(0,date.indexOf("/"))+"/";
				if(lastIndexIdx==3){
					val=firstIndex+"0"+date.charAt(2)+date.substring(3,date.length());
				}
				if(lastIndexIdx==4){
					val=firstIndex+date.substring(2,date.length());
				}
				date=val;

			}

			firstIndexIdx=date.indexOf("-");		
			lastIndexIdx=date.lastIndexOf("-");
			if(firstIndexIdx==1){
				String val="";
				String firstIndex="0"+date.substring(0,date.indexOf("-"))+"-";
				if(lastIndexIdx==3){
					val=firstIndex+"0"+date.charAt(2)+date.substring(3,date.length());
				}
				if(lastIndexIdx==4){
					val=firstIndex+date.substring(2,date.length());
				}
				date=val;

			}
		}catch(Exception e){
			e.getMessage();
		}

		return date;
	}

	private Integer createAccountingEntriesUpload(AccountingEntriesUploadVo accountingEntriesUploadVo, Integer typeId,
			Integer locationId, Integer orgId, String userId, boolean isSuperAdmin, Connection con,String roleName,int currencyId)
					throws ApplicationException {
		logger.info("Enter into Method: createAccountingEntriesUpload");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Integer accountingEntryId = null;
		try {
			String sql = AccountingAspectsConstants.CREATE_ACCOUTING_ENTRY_UPLOAD;
			preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

			preparedStatement.setDate(1,
					accountingEntriesUploadVo.getDate() != null
					? DateConverter.getInstance().convertStringToDate(convertDateVal(accountingEntriesUploadVo.getDate()),
							"dd/MM/yyyy")
							: null);
			preparedStatement.setString(2,
					accountingEntriesUploadVo.getJournalNo() != null ? accountingEntriesUploadVo.getJournalNo() : null);
			preparedStatement.setObject(3, typeId != null ? typeId : null);
			preparedStatement.setObject(4, locationId != null ? locationId : null);
			preparedStatement.setObject(5, orgId != null ? orgId : null);
			preparedStatement.setObject(6, Integer.valueOf(userId) != null ? Integer.valueOf(userId) : null);
			preparedStatement.setBoolean(7, isSuperAdmin);
			preparedStatement.setString(8, accountingEntriesUploadVo.getDifference());
			preparedStatement.setString(9, accountingEntriesUploadVo.getStatus());
			preparedStatement.setString(10, accountingEntriesUploadVo.getTotalDebits());
			preparedStatement.setString(11, accountingEntriesUploadVo.getTotalCredits());
			preparedStatement.setString(12,roleName );
			preparedStatement.setInt(13, currencyId);
			int rowAffected = preparedStatement.executeUpdate();
			if (rowAffected == 1) {
				rs = preparedStatement.getGeneratedKeys();
				if (rs.next()) {
					accountingEntryId = rs.getInt(1);
				}
			}

		} catch (Exception e) {
			logger.info("Error in  createAccountingEntriesUpload:", e);
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, null);
		}
		return accountingEntryId;
	}



	public List<AccountingEntryExportVo> getListAccountingEntriesById(ExportVo exportVo) throws ApplicationException {
		logger.info("Enter into Method: getListAccountingEntriesById");
		List<AccountingEntryExportVo> accountingEntries = new ArrayList<AccountingEntryExportVo>();
		if (exportVo != null && exportVo.getListOfId().size() > 0) {
			for (Integer id : exportVo.getListOfId()) {
				AccountingEntryExportVo accountingEntry = new AccountingEntryExportVo();
				accountingEntry = getAccountingEntryGenaralInfo(id, exportVo.getOrganizationId(), accountingEntry);
				accountingEntry.setAccountingEntryItems(getAccountingEntryItemDetails(id, exportVo));
				accountingEntries.add(accountingEntry);
			}
		}
		return accountingEntries;

	}

	private List<AccountingEntryItemExportVo> getAccountingEntryItemDetails(Integer id, ExportVo exportVo)
			throws ApplicationException {
		logger.info("Entry into getAccountingEntryItemDetails");
		List<AccountingEntryItemExportVo> accountingEntryItems = new ArrayList<AccountingEntryItemExportVo>();
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs=null;
		try{
			con = getAccountingConnection();
			preparedStatement = con.prepareStatement(AccountingAspectsConstants.GET_ACCOUNTING_ENTRY_ITEMS_INFO_BY_ID);
			preparedStatement.setInt(1, id);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				AccountingEntryItemExportVo accountingEntryItem = new AccountingEntryItemExportVo();
				Integer ledgerId = rs.getInt(1);
				if (ledgerId != null) {
					accountingEntryItem
					.setLedger(chartOfAccountsDao.getLedgerName(ledgerId, exportVo.getOrganizationId()));
				}
				Integer subLedgerId = rs.getInt(2);
				if (subLedgerId != null) {
					accountingEntryItem.setSubLedger(
							chartOfAccountsDao.getSubLedgeName(subLedgerId, exportVo.getOrganizationId()));
				}
				Integer currencyId = rs.getInt(3);
				if (currencyId != null) {
					accountingEntryItem
					.setCurrency(currencyDao.getCurrencyName(currencyId, exportVo.getOrganizationId()));
				}
				accountingEntryItem.setExchangeRate(rs.getString(4));
				accountingEntryItem.setCredits(rs.getString(5));
				accountingEntryItem.setDebits(rs.getString(6));
				accountingEntryItems.add(accountingEntryItem);
			}
		} catch (Exception e) {
			logger.info("Error in  getAccountingEntryItemDetails:", e);
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return accountingEntryItems;

	}

	private AccountingEntryExportVo getAccountingEntryGenaralInfo(Integer id, Integer organizationId,
			AccountingEntryExportVo accountingEntry) throws ApplicationException {
		logger.info("Entry into getAccountingEntryGenaralInfo");
		Connection con = null;
		PreparedStatement preparedStatement =null;
		ResultSet rs = null;
		try {
			con = getAccountingConnection();
			preparedStatement = con.prepareStatement(AccountingAspectsConstants.GET_ACCOUNTING_ENTRY_GENERAL_INFO_BY_ID);
			preparedStatement.setInt(1, organizationId);
			preparedStatement.setInt(2, id);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				accountingEntry.setId(id);
				accountingEntry.setDate(rs.getDate(1));
				Integer typeId = rs.getInt(2);
				if (typeId != null) {
					accountingEntry.setType(financeCommonDao.getAccountingEntryTypeName(typeId));
				}
				accountingEntry.setJournalNo(rs.getString(3));

				Boolean isRegeisteredLocation = rs.getBoolean(4);
				if (isRegeisteredLocation) {
					accountingEntry.setLocation("Registered Address");
				}

				Integer locationId = rs.getInt(5);
				if (locationId != null && !isRegeisteredLocation) {
					accountingEntry.setLocation(organizationDao.getLocationNameFromId(locationId, organizationId));
				}
				Integer level1Id = rs.getInt(6);
				if (level1Id != null) {
					accountingEntry.setLevel1Email(userDao.getUserName(level1Id, organizationId));
				}
				Integer level2Id = rs.getInt(7);
				if (level2Id != null) {
					accountingEntry.setLevel2Email(userDao.getUserName(level2Id, organizationId));
				}
				Integer level3Id = rs.getInt(8);
				if (level2Id != null) {
					accountingEntry.setLevel3Email(userDao.getUserName(level3Id, organizationId));
				}
				accountingEntry.setStatus(rs.getString(9));
			}
		} catch (Exception e) {
			logger.info("Error in  getAccountingEntryGenaralInfo:", e);
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, con);
		}

		return accountingEntry;

	}

	/*
	 * private Integer getAccountingEntryItemId(Integer accountingEntryId, Integer
	 * ledgerId, Integer subLedgerId, Connection con) throws ApplicationException {
	 * logger.info("Enter into Method: getAccountingEntryItemId"); Integer
	 * accountingEntryItemId = null; try (final PreparedStatement preparedStatement
	 * = con
	 * .prepareStatement(AccountingAspectsConstants.GET_ACCOUNTING_ENTRY_ITEM_ID)) {
	 * preparedStatement.setInt(1, accountingEntryId); preparedStatement.setInt(2,
	 * ledgerId); preparedStatement.setInt(3, subLedgerId); try (ResultSet rs =
	 * preparedStatement.executeQuery()) { while (rs.next()) { accountingEntryItemId
	 * = rs.getInt(1); } } } catch (Exception e) {
	 * logger.info("Error in  getAccountingEntryItemId:", e); throw new
	 * ApplicationException(e.getMessage()); } return accountingEntryItemId; }
	 * 
	 * private void updateAccountingEntryItemUpload(AccountingEntriesUploadVo
	 * accountingEntriesUploadVo, Integer accountingEntryItemId, Integer ledgerId,
	 * Integer subLedgerId, Integer currencyId, Connection con) throws
	 * ApplicationException {
	 * logger.info("Enter into Method: updateAccountingEntryItemUpload");
	 * PreparedStatement preparedStatement = null; ResultSet rs = null; try { String
	 * sql = AccountingAspectsConstants.UPDATE_ACCOUTING_ENTRY_ITEM_UPLOAD;
	 * preparedStatement = con.prepareStatement(sql); preparedStatement.setObject(1,
	 * ledgerId != null ? ledgerId : null); preparedStatement.setObject(2,
	 * subLedgerId != null ? subLedgerId : null); preparedStatement.setObject(3,
	 * currencyId != null ? currencyId : null); preparedStatement.setString(4,
	 * accountingEntriesUploadVo.getExchangeRate() != null ?
	 * accountingEntriesUploadVo.getExchangeRate() : null);
	 * preparedStatement.setString(5, accountingEntriesUploadVo.getCredits() != null
	 * ? accountingEntriesUploadVo.getCredits() : null);
	 * preparedStatement.setString(6, accountingEntriesUploadVo.getDebits() != null
	 * ? accountingEntriesUploadVo.getDebits() : null); preparedStatement.setInt(7,
	 * accountingEntryItemId != null ? accountingEntryItemId : null);
	 * preparedStatement.executeUpdate(); } catch (Exception e) {
	 * logger.info("Error in  updateAccountingEntryItemUpload:", e); throw new
	 * ApplicationException(e.getMessage()); } finally { closeResources(rs,
	 * preparedStatement, null); } }
	 */

	/*private void updateAccountingEntryUpload(AccountingEntriesUploadVo accountingEntriesUploadVo,
			Integer accountingEntryId, Integer typeId, Integer locationId, Integer orgId, String userId,
			boolean isSuperAdmin, Connection con) throws ApplicationException {
		logger.info("Enter into Method: updateAccountingEntryUpload");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			String sql = AccountingAspectsConstants.UPDATE_ACCOUTING_ENTRY_UPLOAD;
			preparedStatement = con.prepareStatement(sql);
			preparedStatement.setDate(1,
					accountingEntriesUploadVo.getDate() != null
							? DateConverter.getInstance().convertStringToDate(accountingEntriesUploadVo.getDate(),
									"dd/MM/yyyy")
							: null);
			preparedStatement.setString(2,
					accountingEntriesUploadVo.getJournalNo() != null ? accountingEntriesUploadVo.getJournalNo() : null);
			preparedStatement.setObject(3, typeId != null ? typeId : null);
			preparedStatement.setObject(4, locationId != null ? locationId : null);
			preparedStatement.setObject(5, orgId != null ? orgId : null);
			preparedStatement.setObject(6, Integer.valueOf(userId) != null ? Integer.valueOf(userId) : null);
			preparedStatement.setBoolean(7, isSuperAdmin);
			preparedStatement.setInt(8, accountingEntryId);
			preparedStatement.executeUpdate();
		} catch (Exception e) {
			logger.info("Error in  updateAccountingEntryUpload:", e);
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, null);
		}

	}*/
}



/*if (duplicacy) {
accountingEntryId = getAccountingEntryId(journalNo, orgId);
if (accountingEntryId != null) {
	if (count < 1) {
		updateAccountingEntryUpload(accountingEntry, accountingEntryId, typeId, locationId,
				orgId, userId, isSuperAdmin, con);
	}

	createAccountingEntriesItemsUpload(accountingEntry, accountingEntryId, ledgerId,
			subLedgerId, currencyId, con);


 * Integer accountingEntryItemId = getAccountingEntryItemId(accountingEntryId,
 * ledgerId, subLedgerId, con);
 * 
 * if (accountingEntryItemId != null && subLedgerId != null) {
 * updateAccountingEntryItemUpload(accountingEntry, accountingEntryItemId,
 * ledgerId, subLedgerId, currencyId, con); } else {
 * createAccountingEntriesItemsUpload(accountingEntry, accountingEntryId,
 * ledgerId, subLedgerId, currencyId, con); }


}
}*/

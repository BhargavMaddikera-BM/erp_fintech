package com.blackstrawai.banking;

import java.io.PrintWriter;
import java.io.StringWriter;
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
import com.blackstrawai.banking.contra.ContraBaseVo;
import com.blackstrawai.banking.contra.ContraEntriesVo;
import com.blackstrawai.banking.contra.ContraVo;
import com.blackstrawai.common.BaseDao;
import com.blackstrawai.common.CommonConstants;
import com.blackstrawai.export.ContraEntriesExportVo;
import com.blackstrawai.export.ContraExportVo;
import com.blackstrawai.export.ExportVo;
import com.blackstrawai.onboarding.OrganizationDao;
import com.blackstrawai.settings.CurrencyDao;
import com.blackstrawai.settings.VoucherDao;
import com.blackstrawai.settings.VoucherVo;
import com.blackstrawai.upload.ContraUploadVo;

@Repository
public class ContraDao extends BaseDao {

	private Logger logger = Logger.getLogger(ContraDao.class);

	@Autowired
	AttachmentsDao attachmentsDao;

	@Autowired
	private OrganizationDao organizationDao;

	@Autowired
	private CurrencyDao currencyDao;

	@Autowired
	private BankMasterDao bankMasterDao;
	
	@Autowired
	private VoucherDao voucherDao;

	public ContraVo createContraAccounts(ContraVo contraVo) throws ApplicationException {
		logger.info("Entry into method: createContraAccounts");
		ResultSet rs = null;
		Connection con = null;
		PreparedStatement preparedStatement = null;
		if (contraVo != null) {
			try {
				con = getFinanceCommon();
				con.setAutoCommit(false);
				boolean isreferenceNoExist = checkReferenceNoExist(contraVo.getReferenceNo(), contraVo.getOrgId());
				if (isreferenceNoExist) {
					throw new ApplicationException("Reference Number Exist for the Organization");
				}

				String sql = ContraConstants.INSERT_INTO_CONTRA;
				preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				preparedStatement.setString(1, contraVo.getReferenceNo() != null ? contraVo.getReferenceNo() : null);
				preparedStatement.setDate(2, contraVo.getDate() != null ? contraVo.getDate() : null);
				preparedStatement.setObject(3, contraVo.getBaseCurrency() != null ? contraVo.getBaseCurrency() : null);
				preparedStatement.setString(4, contraVo.getRemark() != null ? contraVo.getRemark() : null);
				preparedStatement.setString(5, contraVo.getTotalDebit() != null ? contraVo.getTotalDebit() : null);
				preparedStatement.setString(6, contraVo.getTotalCredit() != null ? contraVo.getTotalCredit() : null);
				preparedStatement.setString(7, contraVo.getStatus() != null ? contraVo.getStatus() : null);
				preparedStatement.setBoolean(8, contraVo.getIsSuperAdmin());
				preparedStatement.setInt(9, contraVo.getOrgId());
				preparedStatement.setInt(10, Integer.valueOf(contraVo.getUserId()));
				preparedStatement.setString(11, contraVo.getDifference() != null ? contraVo.getDifference() : null);
				preparedStatement.setString(12, contraVo.getRoleName());
				int rowAffected = preparedStatement.executeUpdate();
				if (rowAffected == 1) {
					rs = preparedStatement.getGeneratedKeys();
					if (rs.next()) {
						contraVo.setId(rs.getInt(1));
					}
				}
				if (contraVo.getContraEntries() != null && contraVo.getContraEntries().size() > 0) {
					for (ContraEntriesVo contraEntry : contraVo.getContraEntries()) {
						createContraAccountEntries(contraEntry, contraVo.getId(), con);
					}
				}

				if (contraVo.getAttachments() != null && contraVo.getAttachments().size() > 0) {
					attachmentsDao.createAttachments(contraVo.getOrgId(), contraVo.getUserId(),
							contraVo.getAttachments(), AttachmentsConstants.MODULE_TYPE_BANK_CONTRA, contraVo.getId(),
							contraVo.getRoleName());
				}
				con.commit();
			} catch (Exception e) {
				logger.info("Error in createContraAccounts::", e);
				try {
					con.rollback();
				} catch (SQLException e1) {
					throw new ApplicationException(e1.getMessage());
				}
				throw new ApplicationException(e.getMessage());
			} finally {
				closeResources(rs, preparedStatement, con);
			}
		}
		return contraVo;

	}

	private boolean checkReferenceNoExist(String referenceNo, Integer orgId) throws ApplicationException {
		logger.info("Enter into Method: checkReferenceNoExist");
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			con = getAccountingConnection();
			preparedStatement = con.prepareStatement(ContraConstants.CHECK_REFERENCE_NUMBER_EXIST);
			preparedStatement.setInt(1, orgId);
			preparedStatement.setString(2, referenceNo);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				return true;
			}
		} catch (Exception e) {
			logger.info("Error in  checkReferenceNoExist:", e);
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return false;
	}
	
	private ContraBaseVo getContraByJournalNo(String referenceNo, Integer orgId) throws ApplicationException {
		logger.info("Enter into Method: getContraByJournalNo");
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		ContraBaseVo data=null;
		try {
			con = getAccountingConnection();
			preparedStatement = con.prepareStatement(ContraConstants.CHECK_REFERENCE_NUMBER_EXIST);
			preparedStatement.setInt(1, orgId);
			preparedStatement.setString(2, referenceNo);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				data=new ContraBaseVo();
				data.setId(rs.getInt(1));
			}
		} catch (Exception e) {
			logger.info("Error in  checkReferenceNoExist:", e);
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return data;
	}

	private void createContraAccountEntries(ContraEntriesVo contraEntriesVo, Integer id, Connection con)
			throws ApplicationException {
		logger.info("Entry into method: createContraAccountEntries");
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = con.prepareStatement(ContraConstants.INSERT_INTO_CONTRA_ACCOUNT_ENTRIES);
			preparedStatement.setObject(1, id != null ? id : null);
			preparedStatement.setObject(2,
					contraEntriesVo.getAccountId() != null ? contraEntriesVo.getAccountId() : null);
			preparedStatement.setObject(3,
					contraEntriesVo.getCurrencyId() != null ? contraEntriesVo.getCurrencyId() : null);
			preparedStatement.setString(4,
					contraEntriesVo.getExchangeRate() != null ? contraEntriesVo.getExchangeRate() : null);
			preparedStatement.setString(5, contraEntriesVo.getDebit() != null ? contraEntriesVo.getDebit() : null);
			preparedStatement.setString(6, contraEntriesVo.getCredit() != null ? contraEntriesVo.getCredit() : null);
			preparedStatement.setString(7,
					contraEntriesVo.getTotalCreditsEx() != null ? contraEntriesVo.getTotalCreditsEx() : null);
			preparedStatement.setString(8,
					contraEntriesVo.getTotalDebitsEx() != null ? contraEntriesVo.getTotalDebitsEx() : null);
			preparedStatement.setString(9, contraEntriesVo.getAccountType());
			preparedStatement.setString(10, contraEntriesVo.getTransRefNo());
			preparedStatement.executeUpdate();
		} catch (Exception e) {
			logger.info("Error in createContraAccountEntries::", e);
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(null, preparedStatement, null);
		}
	}

	public List<ContraBaseVo> getAllContraAccountsOfAnOrganizationForUserAndRole(int organizationId, String userId,
			String roleName) throws ApplicationException {
		logger.info("Entry into method: getAllContraAccountsOfAnOrganizationForUserAndRole");
		List<ContraBaseVo> listContraAccounts = new ArrayList<ContraBaseVo>();
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			con = getFinanceCommon();
			if (roleName.equals("Super Admin")) {
				preparedStatement = con.prepareStatement(ContraConstants.GET_ALL_CONTRA_ORGANIZATION);
			} else {
				preparedStatement = con.prepareStatement(ContraConstants.GET_ALL_CONTRA_USER_ROLE);
			}
			preparedStatement.setInt(1, organizationId);
			if (!(roleName.equals("Super Admin"))) {
				preparedStatement.setString(2, userId);
				preparedStatement.setString(3, roleName);
			}
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				ContraBaseVo contraAccount = new ContraBaseVo();
				contraAccount.setId(rs.getInt(1));
				contraAccount.setReferenceNo(rs.getString(2));
				contraAccount.setAmount(rs.getString(3));
				contraAccount.setDate(rs.getDate(4));
				contraAccount.setStatus(rs.getString(5));
				listContraAccounts.add(contraAccount);
			}
		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			String sStackTrace = sw.toString();
			logger.info("Error in  getAllContraAccountsOfAnOrganization:", e);
			throw new ApplicationException(sStackTrace);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return listContraAccounts;
	}

	public ContraVo getContraAccountById(int id) throws ApplicationException {
		logger.info("Entry into method: getContraAccountById");
		ContraVo contraVo = new ContraVo();
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			con = getFinanceCommon();
			preparedStatement = con.prepareStatement(ContraConstants.GET_CONTRA_ACCOUNT_BY_ID);
			preparedStatement.setInt(1, id);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				contraVo.setId(id);
				contraVo.setReferenceNo(rs.getString(1));
				contraVo.setDate(rs.getDate(2));
				contraVo.setBaseCurrency(rs.getInt(3));
				contraVo.setRemark(rs.getString(4));
				contraVo.setTotalDebit(rs.getString(5));
				contraVo.setTotalCredit(rs.getString(6));
				contraVo.setIsSuperAdmin(rs.getBoolean(7));
				contraVo.setOrgId(rs.getInt(8));
				contraVo.setStatus(rs.getString(9));
				contraVo.setUserId(rs.getString(10));
				contraVo.setDifference(rs.getString(11));
				contraVo.setCreateTs(rs.getDate(12));
				contraVo.setRoleName(rs.getString(13));
			}
			contraVo.setContraEntries(getContraAccountEntriesById(id, con));

			List<UploadFileVo> uploadFileVos = new ArrayList<UploadFileVo>();

			for (AttachmentsVo attachments : attachmentsDao.getAttachmentDetails(id,
					AttachmentsConstants.MODULE_TYPE_BANK_CONTRA)) {
				UploadFileVo uploadFileVo = new UploadFileVo();
				uploadFileVo.setId(attachments.getId());
				uploadFileVo.setName(attachments.getFileName());
				uploadFileVo.setSize(attachments.getSize());
				uploadFileVo.setDocumentType(attachments.getDocumentTypeId());
				uploadFileVos.add(uploadFileVo);
			}

			contraVo.setAttachments(uploadFileVos);
		} catch (Exception e) {
			logger.info("Error in  getContraAccountById:", e);
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, con);
		}

		return contraVo;
	}

	private List<ContraEntriesVo> getContraAccountEntriesById(int id, Connection con) throws ApplicationException {
		logger.info("Entry into method: getContraAccountEntriesById");
		List<ContraEntriesVo> ListOfContra = new ArrayList<ContraEntriesVo>();
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			preparedStatement = con.prepareStatement(ContraConstants.GET_CONTRA_ACCOUNT_ENTRIES_BY_ID);
			preparedStatement.setInt(1, id);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				ContraEntriesVo contraGeneralInfoVo = new ContraEntriesVo();
				contraGeneralInfoVo.setId(rs.getInt(1));
				contraGeneralInfoVo.setAccountId(rs.getInt(2));
				contraGeneralInfoVo.setCurrencyId(rs.getInt(3));
				contraGeneralInfoVo.setExchangeRate(rs.getString(4));
				contraGeneralInfoVo.setDebit(rs.getString(5));
				contraGeneralInfoVo.setCredit(rs.getString(6));
				contraGeneralInfoVo.setStatus(rs.getString(7));
				contraGeneralInfoVo.setTotalCreditsEx(rs.getString(8));
				contraGeneralInfoVo.setTotalDebitsEx(rs.getString(9));
				contraGeneralInfoVo.setAccountType(rs.getString(10));
				contraGeneralInfoVo.setTransRefNo(rs.getString(11));
				ListOfContra.add(contraGeneralInfoVo);
			}

		} catch (Exception e) {
			logger.info("Error in  getContraAccountEntriesById:", e);
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, null);
		}
		return ListOfContra;
	}

	public boolean updateContraAccount(ContraVo contraVo) throws ApplicationException {
		logger.info("Entry into method: updateContraAccount");
		Connection con = null;
		boolean isTxnSuccess = false;
		if (contraVo != null) {
			try {
				con = getFinanceCommon();
				con.setAutoCommit(false);
				updateContra(contraVo, con);
				if (contraVo.getContraEntries() != null && contraVo.getContraEntries().size() > 0) {
					for (ContraEntriesVo contraGeneralInfoVo : contraVo.getContraEntries()) {
						if (contraGeneralInfoVo.getStatus().equalsIgnoreCase(CommonConstants.STATUS_AS_NEW)) {
							createContraAccountEntries(contraGeneralInfoVo, contraVo.getId(), con);

						} else {
							UpdateContraAccountEntries(contraGeneralInfoVo, con);
						}
					}
				}
				if (contraVo.getItemsToRemove() != null && contraVo.getItemsToRemove().size() > 0) {
					for (Integer itemId : contraVo.getItemsToRemove()) {
						changeStatusForSingleItem(itemId, CommonConstants.STATUS_AS_DELETE, con);
					}
				}
				if (contraVo.getAttachments() != null && contraVo.getAttachments().size() > 0) {
					attachmentsDao.createAttachments(contraVo.getOrgId(), contraVo.getUserId(),
							contraVo.getAttachments(), AttachmentsConstants.MODULE_TYPE_BANK_CONTRA, contraVo.getId(),
							contraVo.getRoleName());
				}
				if (contraVo.getAttachmentsToRemove() != null && contraVo.getAttachmentsToRemove().size() > 0) {
					for (Integer attachmentId : contraVo.getAttachmentsToRemove()) {
						attachmentsDao.changeStatusForSingleAttachment(attachmentId, CommonConstants.STATUS_AS_DELETE,
								contraVo.getUserId(), contraVo.getRoleName());
					}
				}
				con.commit();
				isTxnSuccess = true;
			} catch (Exception e) {
				logger.info("Error in  updateContraAccount:", e);
				try {
					con.rollback();
				} catch (SQLException e1) {
					throw new ApplicationException(e1.getMessage());
				}
				throw new ApplicationException(e.getMessage());
			} finally {
				closeResources(null, null, con);
			}

		}
		return isTxnSuccess;

	}

	public void updateContra(ContraVo contraVo, Connection con) throws ApplicationException {
		logger.info("Entry into method: updateContra");
		PreparedStatement preparedStatement = null;
		if (contraVo != null) {
			try {
				preparedStatement = con.prepareStatement(ContraConstants.UPDATE_CONTRA_ACCOUNT);
				preparedStatement.setDate(1, contraVo.getDate() != null ? contraVo.getDate() : null);
				preparedStatement.setObject(2, contraVo.getBaseCurrency() != null ? contraVo.getBaseCurrency() : null);
				preparedStatement.setString(3, contraVo.getRemark() != null ? contraVo.getRemark() : null);
				preparedStatement.setString(4, contraVo.getTotalDebit() != null ? contraVo.getTotalDebit() : null);
				preparedStatement.setString(5, contraVo.getTotalCredit() != null ? contraVo.getTotalCredit() : null);
				preparedStatement.setString(6, contraVo.getStatus() != null ? contraVo.getStatus() : null);
				preparedStatement.setBoolean(7, contraVo.getIsSuperAdmin());
				preparedStatement.setInt(8, contraVo.getOrgId());
				preparedStatement.setInt(9, Integer.valueOf(contraVo.getUserId()));
				preparedStatement.setString(10, contraVo.getDifference());
				preparedStatement.setString(11, contraVo.getRoleName());
				preparedStatement.setInt(12, contraVo.getId());
				preparedStatement.executeUpdate();
			} catch (Exception e) {
				logger.info("Error in  updateContra:", e);
				throw new ApplicationException(e.getMessage());
			} finally {
				closeResources(null, preparedStatement, null);
			}
		}
	}

	private void UpdateContraAccountEntries(ContraEntriesVo contraGeneralInfoVo, Connection con)
			throws ApplicationException {
		logger.info("Entry into Method: UpdateContraAccountEntries");
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = con.prepareStatement(ContraConstants.UPDATE_CONTRA_ACCOUNT_ENTRIES);
			preparedStatement.setObject(1,
					contraGeneralInfoVo.getAccountId() != null ? contraGeneralInfoVo.getAccountId() : null);
			preparedStatement.setObject(2,
					contraGeneralInfoVo.getCurrencyId() != null ? contraGeneralInfoVo.getCurrencyId() : null);
			preparedStatement.setString(3,
					contraGeneralInfoVo.getExchangeRate() != null ? contraGeneralInfoVo.getExchangeRate() : null);
			preparedStatement.setString(4,
					contraGeneralInfoVo.getDebit() != null ? contraGeneralInfoVo.getDebit() : null);
			preparedStatement.setString(5,
					contraGeneralInfoVo.getCredit() != null ? contraGeneralInfoVo.getCredit() : null);
			preparedStatement.setString(6,
					contraGeneralInfoVo.getStatus() != null ? contraGeneralInfoVo.getStatus() : null);
			preparedStatement.setTimestamp(7, new Timestamp(System.currentTimeMillis()));
			preparedStatement.setString(8,
					contraGeneralInfoVo.getTotalCreditsEx() != null ? contraGeneralInfoVo.getTotalCreditsEx() : null);
			preparedStatement.setString(9,
					contraGeneralInfoVo.getTotalDebitsEx() != null ? contraGeneralInfoVo.getTotalDebitsEx() : null);
			preparedStatement.setString(10, contraGeneralInfoVo.getAccountType());
			preparedStatement.setString(11, contraGeneralInfoVo.getTransRefNo());
			preparedStatement.setInt(12, contraGeneralInfoVo.getId());
			preparedStatement.executeUpdate();

		} catch (Exception e) {
			logger.info("Error in  UpdateContraAccountEntries:", e);
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(null, preparedStatement, null);
		}
	}

	private void changeStatusForSingleItem(Integer itemId, String status, Connection con) throws ApplicationException {
		logger.info("Entry into Method changeStatusForSingleItem:");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		if (itemId != null && status != null) {
			try {
				String sql = ContraConstants.CHANGE_SINGLE_ITEM_STATUS;
				preparedStatement = con.prepareStatement(sql);
				preparedStatement.setString(1, status);
				preparedStatement.setDate(2, new Date(System.currentTimeMillis()));
				preparedStatement.setInt(3, itemId);
				preparedStatement.executeUpdate();
			} catch (Exception e) {
				logger.info("Error in changeStatusForSingleItem ", e);
				throw new ApplicationException(e.getMessage());
			} finally {
				closeResources(rs, preparedStatement, null);
			}
		}
	}

	public void deleteContraEntries(Integer id, String userId, String roleName) throws ApplicationException {
		logger.info("To deleteContraEntries:: ");
		Connection connection = null;
		if (id != null) {
			try {
				connection = getAccountsPayable();
				// To remove from Contra Main table
				changeStatusForContraTables(id, CommonConstants.STATUS_AS_DELETE, connection,
						ContraConstants.MODIFY_CONTRA_STATUS);
				// To remove from Contra entries table
				changeStatusForContraTables(id, CommonConstants.STATUS_AS_DELETE, connection,
						ContraConstants.MODIFY_CONTRA_ENTRIES_STATUS);
				// To remove from Attachments table
				attachmentsDao.changeStatusForAttachments(id, CommonConstants.STATUS_AS_DELETE,
						AttachmentsConstants.MODULE_TYPE_BANK_CONTRA, userId, roleName);
				logger.info("Deleted successfully in all tables ");
			} catch (Exception e) {
				logger.info("Error in deleteContraEntries:: ", e);
				throw new ApplicationException(e);
			} finally {
				closeResources(null, null, connection);
			}
		}
	}

	private void changeStatusForContraTables(Integer id, String status, Connection con, String query)
			throws ApplicationException {
		logger.info("To Change the status with query :: " + query);
		PreparedStatement preparedStatement = null;
		if (query != null) {
			try {
				preparedStatement = con.prepareStatement(query);
				preparedStatement.setString(1, status);
				preparedStatement.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
				preparedStatement.setInt(3, id);
				preparedStatement.executeUpdate();
				logger.info("Status chaneged successfully ");
			} catch (Exception e) {
				logger.info("Error in changeStatusForContraTables ", e);
				throw new ApplicationException(e);
			} finally {
				closeResources(null, preparedStatement, null);
			}
		}

	}

	public ContraUploadVo processUpload(List<ContraUploadVo> contraEntriesList, Integer orgId, String userId,
			boolean isSuperAdmin, boolean duplicacy, String roleName) throws ApplicationException {
		logger.info("Enter into Method: processUpload");
		Integer accountId = null;
		Integer currencyId = null;
		Integer contraId = null;
		Connection con = null;
		boolean isCreate=false;
		boolean isJournalTransactionRequired=false;
		int count = 0;
		con = getUserMgmConnection();
		String dateFormat = organizationDao.getDefaultDateForOrganization(orgId, con);
		closeResources(null, null, con);
		String journalNo = contraEntriesList.get(0).getReferenceNo();
		try {
			Double debit = 0.0;
			Double credit = 0.0;
			for (int i = 0; i < contraEntriesList.size(); i++) {
				ContraUploadVo data = contraEntriesList.get(i);
				credit = credit + (Double.parseDouble(data.getExchangeRate()) * Double.parseDouble(data.getCredit()));
				debit = debit + (Double.parseDouble(data.getExchangeRate()) * Double.parseDouble(data.getDebit()));
			}
			con = getAccountingConnection();
			con.setAutoCommit(false);
			if (journalNo != null) {
				for (ContraUploadVo contraUploadVo : contraEntriesList) {
					if (contraUploadVo.getAccountName() != null && contraUploadVo.getAccountype() != null) {
						if (contraUploadVo.getAccountype().equalsIgnoreCase("Bank Account")) {
							accountId = bankMasterDao.getBankMasterBankAccountIdByName(contraUploadVo.getAccountName(),
									orgId);
						}
						if (contraUploadVo.getAccountype().equalsIgnoreCase("Credit Card")) {
							accountId = bankMasterDao.getMasterCardIdByName(contraUploadVo.getAccountName(), orgId);
						}
						if (contraUploadVo.getAccountype().equalsIgnoreCase("Wallet")) {
							accountId = bankMasterDao.getMasterWalletIdByName(contraUploadVo.getAccountName(), orgId);
						}
						if (accountId == null) {
							throw new ApplicationException("Invalid account");
						}
					}

					if (contraUploadVo.getReferenceNo() != null) {
						int length=0;
						try {
							VoucherVo voucherVo=voucherDao.getVoucherBaseOnType(orgId,"Banking-Contra");
							length=Integer.parseInt(voucherVo.getMinimumDigits());
							if (contraUploadVo.getReferenceNo().length() > length) {
								throw new ApplicationException("Journal no. should be a "+ length +" digit number.");
							}
							Integer.parseInt(contraUploadVo.getReferenceNo());

						} catch (Exception e) {
							throw new ApplicationException("Journal no. should be a "+ length +" digit number.");
						}
					}

					if (contraUploadVo.getDate() != null) {
						try {
							SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
							simpleDateFormat.parse(contraUploadVo.getDate());
						} catch (Exception e) {
							throw new ApplicationException("Date Format should be:" + dateFormat);

						}

					}

					if (contraUploadVo.getCurrency() != null) {
						currencyId = currencyDao.getCurrencyId(contraUploadVo.getCurrency(), orgId);
						if (currencyId == null) {
							throw new ApplicationException("Invalid Currency");
						}
					}

					Boolean isJournalNoExist = checkReferenceNoExist(journalNo, orgId);
					if (isJournalNoExist) {
						if(!isCreate){
							ContraBaseVo contraBaseVo = getContraByJournalNo(journalNo, orgId);
							if(contraBaseVo!=null){
								contraId=contraBaseVo.getId();
								Double totalDebitExValue=new Double(contraUploadVo.getExchangeRate()) * new Double(contraUploadVo.getDebit());
								Double totalCreditExValue=new Double(contraUploadVo.getExchangeRate()) * new Double(contraUploadVo.getCredit());
								contraUploadVo.setTotalDebitsEx(totalDebitExValue.toString());
								contraUploadVo.setTotalCreditsEx(totalCreditExValue.toString());
								createContraItemsUpload(contraUploadVo, contraId, accountId, currencyId, con);
							}
						}			

					} else {
						if (count < 1) {
							contraUploadVo.setDifference(new Double(credit - debit).toString());
							if (credit - debit > 0 || credit - debit < 0) {
								contraUploadVo.setStatus("DRAFT");
							} else {
								contraUploadVo.setStatus("ACT");
							}
							contraUploadVo.setTotalDebits(new Double(debit).toString());
							contraUploadVo.setTotalCredits(new Double(credit).toString());
							BasicCurrencyVo currencyData=currencyDao.getDefaultCurrencyForOrganization(orgId, con);
							contraId = createContraUpload(contraUploadVo, orgId, userId, isSuperAdmin, con, roleName,currencyData.getId());							
							isJournalTransactionRequired=true;
							isCreate=true;
						}
						if (contraId != null) {
							Double totalDebitExValue = new Double(contraUploadVo.getExchangeRate())
									* new Double(contraUploadVo.getDebit());
							Double totalCreditExValue = new Double(contraUploadVo.getExchangeRate())
									* new Double(contraUploadVo.getCredit());
							contraUploadVo.setTotalDebitsEx(totalDebitExValue.toString());
							contraUploadVo.setTotalCreditsEx(totalCreditExValue.toString());
							createContraItemsUpload(contraUploadVo, contraId, accountId, currencyId, con);
						}

					}
					count++;

				}
				
				String status="DRAFT";
				if(!isCreate){
					ContraVo data=getContraAccountById(contraId);
					String totalDebits=data.getTotalDebit();
					String totalCredits=data.getTotalCredit();
					Double totalDebits_lc=null;
					Double totalCredits_lc=null;
					if(totalDebits!=null){
						totalDebits_lc=Double.parseDouble(totalDebits)+debit;
					}
					if(totalCredits!=null){
						totalCredits_lc=Double.parseDouble(totalCredits)+credit;
					}
					Double difference=totalCredits_lc-totalDebits_lc;
					PreparedStatement preparedStatement = null;
					ResultSet rs = null;
					
					if((difference==0)|| (difference==0.0)){
						status="ACT";
					}
					try {
						String sql = ContraConstants.UPDATE_GENERAL_INFO;
						preparedStatement = con.prepareStatement(sql);
						preparedStatement.setString(1, status);
						preparedStatement.setString(2,  totalCredits_lc.toString());
						preparedStatement.setString(3,totalDebits_lc.toString());
						preparedStatement.setString(4,difference.toString());						
						preparedStatement.setInt(5, contraId);
						preparedStatement.executeUpdate();
					}catch (Exception e) {
						throw new ApplicationException(e);
					} finally {
						closeResources(rs, preparedStatement, null);
					}
									
				}
				
				
				con.commit();
				if(isCreate){
					if((isJournalTransactionRequired) && (credit-debit==0 || credit-debit==0.0)){
						ContraUploadVo contraUploadVo=new ContraUploadVo();
						contraUploadVo.setStatus("ACT");
						contraUploadVo.setAccountype(contraId.toString());
						return contraUploadVo;
					}
				}else{
					if(status.equals("ACT")){
						ContraUploadVo contraUploadVo=new ContraUploadVo();
						contraUploadVo.setStatus("ACT");
						contraUploadVo.setAccountype(contraId.toString());
						return contraUploadVo;
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
	
	
	private Integer createContraUpload(ContraUploadVo contraUploadVo, Integer orgId, String userId,
			boolean isSuperAdmin, Connection con, String roleName,int currencyId) throws ApplicationException {
		logger.info("Enter into Method: createContraUpload");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Integer contraId = null;
		try {
			String sql = ContraConstants.CREATE_CONTRA_UPLOAD;
			preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setDate(1,
					contraUploadVo.getDate() != null
							? DateConverter.getInstance().convertStringToDate(convertDateVal(contraUploadVo.getDate()), "dd/MM/yyyy")
							: null);
			preparedStatement.setString(2,
					contraUploadVo.getReferenceNo() != null ? contraUploadVo.getReferenceNo() : null);
			preparedStatement.setString(3, contraUploadVo.getTotalDebits());
			preparedStatement.setString(4, contraUploadVo.getTotalCredits());
			preparedStatement.setString(5, contraUploadVo.getDifference());
			preparedStatement.setObject(6, orgId != null ? orgId : null);
			preparedStatement.setObject(7, Integer.valueOf(userId) != null ? Integer.valueOf(userId) : null);
			preparedStatement.setBoolean(8, isSuperAdmin);
			preparedStatement.setString(9, contraUploadVo.getStatus());
			preparedStatement.setString(10, roleName);
			preparedStatement.setInt(11, currencyId);
			int rowAffected = preparedStatement.executeUpdate();
			if (rowAffected == 1) {
				rs = preparedStatement.getGeneratedKeys();
				if (rs.next()) {
					contraId = rs.getInt(1);
				}
			}

		} catch (Exception e) {
			logger.info("Error in  createAccountingEntriesUpload:", e);
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, null);
		}
		return contraId;
	}

	private void createContraItemsUpload(ContraUploadVo contraUploadVo, Integer contraId, Integer accountId,
			Integer currencyId, Connection con) throws ApplicationException {
		logger.info("Enter into Method: createContraItemsUpload");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			String sql = ContraConstants.CREATE_CONTRA_ITEM_UPLOAD;
			preparedStatement = con.prepareStatement(sql);
			preparedStatement.setObject(1, contraId != null ? contraId : null);
			preparedStatement.setObject(2, accountId != null ? accountId : null);
			preparedStatement.setString(3, contraUploadVo.getAccountype());
			preparedStatement.setObject(4, currencyId != null ? currencyId : null);
			preparedStatement.setString(5,
					contraUploadVo.getExchangeRate() != null ? contraUploadVo.getExchangeRate() : null);
			preparedStatement.setString(6, contraUploadVo.getDebit() != null ? contraUploadVo.getDebit() : null);
			preparedStatement.setString(7, contraUploadVo.getCredit() != null ? contraUploadVo.getCredit() : null);
			preparedStatement.setString(8, contraUploadVo.getTotalCreditsEx());
			preparedStatement.setString(9, contraUploadVo.getTotalDebitsEx());
			preparedStatement.executeUpdate();
		} catch (Exception e) {
			logger.info("Error in  createContraItemsUpload:", e);
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, null);
		}

	}

	public List<ContraExportVo> getListContraEntriesById(ExportVo exportVo) throws ApplicationException {
		logger.info("Enter into Method: getListContraEntriesById");
		List<ContraExportVo> contraEntries = new ArrayList<ContraExportVo>();
		if (exportVo != null && exportVo.getListOfId().size() > 0) {
			for (Integer id : exportVo.getListOfId()) {
				ContraExportVo contraEntry = new ContraExportVo();
				contraEntry = getContraGenaralInfo(id, exportVo.getOrganizationId(), contraEntry);
				contraEntry.setContraEntries(getContraEntriesDetails(id, exportVo));
				contraEntries.add(contraEntry);
			}
		}
		return contraEntries;
	}

	private List<ContraEntriesExportVo> getContraEntriesDetails(Integer id, ExportVo exportVo)
			throws ApplicationException {
		logger.info("Entry into getAccountingEntryGenaralInfo");
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		List<ContraEntriesExportVo> contraEntriesList = new ArrayList<ContraEntriesExportVo>();
		try {
			con = getAccountingConnection();
			preparedStatement = con.prepareStatement(ContraConstants.GET_CONTRA_ENTRIES_INFO_BY_ID);
			preparedStatement.setInt(1, id);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				ContraEntriesExportVo contraEntriesExportVo = new ContraEntriesExportVo();
				Integer accountId = rs.getInt(1);
				String accountType = rs.getString(2);
				if (accountId != null && accountType != null) {
					contraEntriesExportVo.setAccountName(bankMasterDao.getAccountName(accountId, accountType));
				}

				Integer currencyId = rs.getInt(3);
				if (currencyId != null) {
					contraEntriesExportVo
							.setCurrency(currencyDao.getCurrencyName(currencyId, exportVo.getOrganizationId()));
				}

				contraEntriesExportVo.setExchnageRate(rs.getString(4));
				contraEntriesExportVo.setCredit(rs.getString(5));
				contraEntriesExportVo.setDebit(rs.getString(6));
				contraEntriesList.add(contraEntriesExportVo);
			}
		} catch (Exception e) {
			logger.info("Error in  getAccountingEntryGenaralInfo:", e);
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return contraEntriesList;

	}

	private ContraExportVo getContraGenaralInfo(Integer id, Integer organizationId, ContraExportVo contraEntry)
			throws ApplicationException {
		logger.info("Entry into getAccountingEntryGenaralInfo");
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			con = getAccountingConnection();
			preparedStatement = con.prepareStatement(ContraConstants.GET_CONTRA_GENERAL_INFO_BY_ID);
			preparedStatement.setInt(2, organizationId);
			preparedStatement.setInt(1, id);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				contraEntry.setId(id);
				contraEntry.setDate(rs.getDate(1));
				contraEntry.setReferenceNo(rs.getString(2));
				contraEntry.setStatus(rs.getString(3));
			}
		} catch (Exception e) {
			logger.info("Error in  getAccountingEntryGenaralInfo:", e);
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return contraEntry;
	}

}

package com.blackstrawai.banking;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.banking.dashboard.BankMasterAccountBaseVo;
import com.blackstrawai.banking.dashboard.BankMasterAccountVo;
import com.blackstrawai.banking.dashboard.BankMasterCardVo;
import com.blackstrawai.banking.dashboard.BankMasterCashAccountVo;
import com.blackstrawai.banking.dashboard.BankMasterWalletVo;
import com.blackstrawai.banking.dashboard.BasicBankAccountVo;
import com.blackstrawai.banking.dashboard.BasicBankMasterCardAccountVo;
import com.blackstrawai.banking.dashboard.BasicBankMasterCashAccountVo;
import com.blackstrawai.banking.dashboard.BasicBankMasterWalletVo;
import com.blackstrawai.banking.dashboard.BasicBankingVo;
import com.blackstrawai.common.BaseDao;

@Repository
public class BankMasterDao extends BaseDao {

	private Logger logger = Logger.getLogger(BankMasterDao.class);

	public void createBankMasterAccounts(BankMasterAccountVo bankMasterAccountVo) throws ApplicationException {
		logger.info("Entry into createBankMasterAccounts");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = getUserMgmConnection();
			if (bankMasterAccountVo.getAccountNumber() != null && bankMasterAccountVo.getAccountName()!= null) {
				boolean isBankMasterAccountsExist = checkBankMasterAccountsExist(
						bankMasterAccountVo.getOrganizationId(), bankMasterAccountVo.getAccountName(),bankMasterAccountVo.getBankName());
				if (isBankMasterAccountsExist) {
					throw new ApplicationException("Bank Account Exist for the Organization");
				}
			}

			String sql = BankingConstants.INSERT_INTO_BANK_MASTER_ACCOUNTS;
			preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setString(1,
					bankMasterAccountVo.getAccountName() != null ? bankMasterAccountVo.getAccountName() : null);
			preparedStatement.setInt(2,
					bankMasterAccountVo.getAccountType() != null ? bankMasterAccountVo.getAccountType() : null);
			preparedStatement.setString(3,
					bankMasterAccountVo.getAccountNumber() != null ? bankMasterAccountVo.getAccountNumber() : null);
			preparedStatement.setInt(4,
					bankMasterAccountVo.getAccountVariant() != null ? bankMasterAccountVo.getAccountVariant() : null);
			preparedStatement.setString(5,
					bankMasterAccountVo.getIfscCode() != null ? bankMasterAccountVo.getIfscCode() : null);
			preparedStatement.setString(6,
					bankMasterAccountVo.getBankName() != null ? bankMasterAccountVo.getBankName() : null);
			preparedStatement.setString(7,
					bankMasterAccountVo.getBranchName() != null ? bankMasterAccountVo.getBranchName() : null);
			preparedStatement.setString(8,
					bankMasterAccountVo.getCurrentBalance() != null ? bankMasterAccountVo.getCurrentBalance() : null);
			preparedStatement.setString(9,
					bankMasterAccountVo.getInterestRate() != null ? bankMasterAccountVo.getInterestRate() : null);
			preparedStatement.setDate(10,
					bankMasterAccountVo.getOpeningDate() != null ? bankMasterAccountVo.getOpeningDate() : null);
			preparedStatement.setInt(11,
					bankMasterAccountVo.getAccountCurrencyId() != null ? bankMasterAccountVo.getAccountCurrencyId()
							: 0);
			preparedStatement.setInt(12,
					bankMasterAccountVo.getTermMonth() != null ? bankMasterAccountVo.getTermMonth() : 0);
			preparedStatement.setInt(13,
					bankMasterAccountVo.getTermYear() != null ? bankMasterAccountVo.getTermYear() : 0);
			preparedStatement.setDate(14,
					bankMasterAccountVo.getMaturityDate() != null ? bankMasterAccountVo.getMaturityDate() : null);
			preparedStatement.setString(15,
					bankMasterAccountVo.getLimit() != null ? bankMasterAccountVo.getLimit() : null);
			preparedStatement.setString(16,
					bankMasterAccountVo.getAccountCode() != null ? bankMasterAccountVo.getAccountCode() : null);
			preparedStatement.setInt(17,
					Integer.valueOf(bankMasterAccountVo.getUserId()) != null
							? Integer.valueOf(bankMasterAccountVo.getUserId())
							: 0);
			preparedStatement.setInt(18,
					bankMasterAccountVo.getOrganizationId() != null ? bankMasterAccountVo.getOrganizationId() : 0);
			preparedStatement.setBoolean(19, bankMasterAccountVo.getIsSuperAdmin());
			preparedStatement.setString(20, bankMasterAccountVo.getRoleName());
			int rowAffected = preparedStatement.executeUpdate();
			if (rowAffected == 1) {
				rs = preparedStatement.getGeneratedKeys();
				if (rs.next()) {
					bankMasterAccountVo.setId(rs.getInt(1));
				}
			}
		} catch (Exception e) {
			logger.info("Error in createBankMasterAccounts:: ", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
	}

	private boolean checkBankMasterAccountsExist(int organizationId, String accountName,String bankName) throws ApplicationException {
		logger.info("Entry into checkBankMasterAccountsExist");
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			con = getUserMgmConnection();
			String query = BankingConstants.CHECK_BANK_MASTER_ACCOUNTS_EXIST_FOR_ORGANIZATION;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, organizationId);
			preparedStatement.setString(2, accountName);
			preparedStatement.setString(3, bankName);
			rs = preparedStatement.executeQuery();
			if (rs.next()) {
				return true;
			}
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);

		}

		return false;
	}

	public void createBankMasterCard(BankMasterCardVo bankMasterCardVo) throws ApplicationException {
		logger.info("Entry into createBankMasterCard");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = getUserMgmConnection();
			if (bankMasterCardVo.getCardNumber() != null && !(bankMasterCardVo.getCardNumber().isEmpty())) {
				boolean isBankMasterCardExist = checkBankMasterCardExist(bankMasterCardVo.getOrganizationId(),
						bankMasterCardVo.getCardNumber());
				if (isBankMasterCardExist) {
					throw new ApplicationException("Credit Card Account Exist for the Organization");
				}
			}

			String sql = BankingConstants.INSERT_INTO_BANK_MASTER_CARDS;
			preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setString(1,
					bankMasterCardVo.getAccountName() != null ? bankMasterCardVo.getAccountName() : null);
			preparedStatement.setString(2,
					bankMasterCardVo.getAuthorizedPerson() != null ? bankMasterCardVo.getAuthorizedPerson() : null);
			preparedStatement.setDate(3,
					bankMasterCardVo.getOpeningDate() != null ? bankMasterCardVo.getOpeningDate() : null);
			preparedStatement.setString(4,
					bankMasterCardVo.getCardNumber() != null ? bankMasterCardVo.getCardNumber() : null);
			preparedStatement.setString(5,
					bankMasterCardVo.getIssuingBankName() != null ? bankMasterCardVo.getIssuingBankName() : null);
			preparedStatement.setString(6,
					bankMasterCardVo.getCurrentBalance() != null ? bankMasterCardVo.getCurrentBalance() : null);
			preparedStatement.setDate(7,
					bankMasterCardVo.getBillingDate() != null ? bankMasterCardVo.getBillingDate() : null);
			preparedStatement.setString(8, bankMasterCardVo.getLimit() != null ? bankMasterCardVo.getLimit() : null);
			preparedStatement.setString(9,
					bankMasterCardVo.getAccountCode() != null ? bankMasterCardVo.getAccountCode() : null);
			preparedStatement.setBoolean(10, bankMasterCardVo.getIsSuperAdmin());
			preparedStatement.setInt(11, bankMasterCardVo.getOrganizationId());
			preparedStatement.setInt(12,
					Integer.valueOf(bankMasterCardVo.getUserId()) != null
							? Integer.valueOf(bankMasterCardVo.getUserId())
							: 0);
			preparedStatement.setString(13, bankMasterCardVo.getRoleName());
			int rowAffected = preparedStatement.executeUpdate();
			if (rowAffected == 1) {
				rs = preparedStatement.getGeneratedKeys();
				if (rs.next()) {
					bankMasterCardVo.setId(rs.getInt(1));
				}
			}
		} catch (Exception e) {
			logger.info("Error in createBankMasterCard:: ", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
	}

	private boolean checkBankMasterCardExist(Integer organizationId, String cardNumber) throws ApplicationException {
		logger.info("Entry into checkBankMasterCardExist");
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			con = getUserMgmConnection();
			String query = BankingConstants.CHECK_BANK_MASTER_CARD_EXIST_FOR_ORGANIZATION;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, organizationId);
			preparedStatement.setString(2, cardNumber);
			rs = preparedStatement.executeQuery();
			if (rs.next()) {
				return true;
			}
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);

		}

		return false;
	}

	public void createBankMasterWallet(BankMasterWalletVo bankMasterWalletVo) throws ApplicationException {
		logger.info("Entry into createBankMasterWallet");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = getUserMgmConnection();
			if (bankMasterWalletVo.getWalletNumber() != null && !(bankMasterWalletVo.getWalletNumber().isEmpty())) {
				boolean isBankMasterWalletExist = checkBankMasterWalletExist(bankMasterWalletVo.getOrganizationId(),
						bankMasterWalletVo.getWalletNumber());
				if (isBankMasterWalletExist) {
					throw new ApplicationException("Wallet Account Exist for the Organization");
				}
			}

			String sql = BankingConstants.INSERT_INTO_BANK_MASTER_WALLETS;
			preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setString(1,
					bankMasterWalletVo.getWalletAccountName() != null ? bankMasterWalletVo.getWalletAccountName()
							: null);
			preparedStatement.setString(2,
					bankMasterWalletVo.getAuthorizedPerson() != null ? bankMasterWalletVo.getAuthorizedPerson() : null);
			preparedStatement.setDate(3,
					bankMasterWalletVo.getOpeningDate() != null ? bankMasterWalletVo.getOpeningDate() : null);
			preparedStatement.setString(4,
					bankMasterWalletVo.getWalletNumber() != null ? bankMasterWalletVo.getWalletNumber() : null);
			preparedStatement.setString(5,
					bankMasterWalletVo.getWalletProvider() != null ? bankMasterWalletVo.getWalletProvider() : null);
			preparedStatement.setString(6,
					bankMasterWalletVo.getCurrentBalance() != null ? bankMasterWalletVo.getCurrentBalance() : null);
			preparedStatement.setString(7,
					bankMasterWalletVo.getTransactionLimit() != null ? bankMasterWalletVo.getTransactionLimit() : null);
			preparedStatement.setString(8,
					bankMasterWalletVo.getAccountCode() != null ? bankMasterWalletVo.getAccountCode() : null);
			preparedStatement.setBoolean(9,
					bankMasterWalletVo.getIsSuperAdmin() != null ? bankMasterWalletVo.getIsSuperAdmin() : null);
			preparedStatement.setInt(10,
					bankMasterWalletVo.getOrganizationId() != null ? bankMasterWalletVo.getOrganizationId() : 0);
			preparedStatement.setInt(11,
					Integer.valueOf(bankMasterWalletVo.getUserId()) != null
							? Integer.valueOf(bankMasterWalletVo.getUserId())
							: 0);
			preparedStatement.setString(12, bankMasterWalletVo.getRoleName());
			int rowAffected = preparedStatement.executeUpdate();
			if (rowAffected == 1) {
				rs = preparedStatement.getGeneratedKeys();
				if (rs.next()) {
					bankMasterWalletVo.setId(rs.getInt(1));
				}
			}
		} catch (Exception e) {
			logger.info("Error in createBankMasterWallet:: ", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}

	}

	private boolean checkBankMasterWalletExist(Integer organizationId, String walletNumber)
			throws ApplicationException {
		logger.info("Entry into checkBankMasterWalletExist");
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			con = getUserMgmConnection();
			String query = BankingConstants.CHECK_BANK_MASTER_WALLET_EXIST_FOR_ORGANIZATION;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, organizationId);
			preparedStatement.setString(2, walletNumber);
			rs = preparedStatement.executeQuery();
			if (rs.next()) {
				return true;
			}
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);

		}

		return false;
	}

	public void createBankMasterCashAccounts(BankMasterCashAccountVo bankMasterCashAccountVo)
			throws ApplicationException {
		logger.info("Entry into createBankMasterCashAccounts");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = getUserMgmConnection();
			if (bankMasterCashAccountVo.getCashAccountName() != null
					&& !(bankMasterCashAccountVo.getCashAccountName().isEmpty())) {
				if (bankMasterCashAccountVo.getLocationId() != null) {
					boolean isBankMasterCashAccountExistForLocation = checkBankMasterCashAccountExistForLocation(
							bankMasterCashAccountVo.getOrganizationId(), bankMasterCashAccountVo.getLocationId(),
							bankMasterCashAccountVo.getCashAccountName());
					if (isBankMasterCashAccountExistForLocation) {
						throw new ApplicationException("Cash Account Exist for the Location");
					}
				} else {
					boolean isBankMasterCashAccountExistForOrganization = checkBankMasterCashAccountExistForOrganization(
							bankMasterCashAccountVo.getOrganizationId(), bankMasterCashAccountVo.getCashAccountName());
					if (isBankMasterCashAccountExistForOrganization) {
						throw new ApplicationException("Cash Account Exist for the Organization");
					}
				}

			}

			String sql = BankingConstants.INSERT_INTO_BANK_MASTER_ACCOUNT_CASH;
			preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setString(1,
					bankMasterCashAccountVo.getCashAccountName() != null ? bankMasterCashAccountVo.getCashAccountName()
							: null);
			preparedStatement.setString(2,
					bankMasterCashAccountVo.getAccountCode() != null ? bankMasterCashAccountVo.getAccountCode() : null);
			preparedStatement.setString(3,
					bankMasterCashAccountVo.getCurrentBalance() != null ? bankMasterCashAccountVo.getCurrentBalance()
							: null);
			preparedStatement.setInt(4,
					bankMasterCashAccountVo.getCurrencyId() != null ? bankMasterCashAccountVo.getCurrencyId() : 0);
			preparedStatement.setInt(5,
					bankMasterCashAccountVo.getLocationId() != null ? bankMasterCashAccountVo.getLocationId() : 0);
			preparedStatement.setInt(6,
					bankMasterCashAccountVo.getOrganizationId() != null ? bankMasterCashAccountVo.getOrganizationId()
							: 0);
			preparedStatement.setBoolean(7, bankMasterCashAccountVo.getIsSuperAdmin());
			preparedStatement.setInt(8,
					Integer.valueOf(bankMasterCashAccountVo.getUserId()) != null
							? Integer.valueOf(bankMasterCashAccountVo.getUserId())
							: 0);
			preparedStatement.setString(9, bankMasterCashAccountVo.getRoleName());
			int rowAffected = preparedStatement.executeUpdate();
			if (rowAffected == 1) {
				rs = preparedStatement.getGeneratedKeys();
				if (rs.next()) {
					bankMasterCashAccountVo.setId(rs.getInt(1));
				}
			}
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);

		}
	}

	private boolean checkBankMasterCashAccountExistForOrganization(Integer organizationId, String cashAccountName)
			throws ApplicationException {
		logger.info("Entry into checkBankMasterCashAccountExistForOrganization");
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			con = getUserMgmConnection();
			preparedStatement = con
					.prepareStatement(BankingConstants.CHECK_BANK_MASTER_CASH_ACCOUNT_EXIST_FOR_ORGANIZATION);
			preparedStatement.setInt(1, organizationId);
			preparedStatement.setString(2, cashAccountName);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				return true;
			}

		} catch (Exception e) {
			logger.info("Error in  checkBankMasterCashAccountExistForOrganization:", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);

		}
		return false;
	}

	private boolean checkBankMasterCashAccountExistForLocation(Integer organizationId, Integer locationId,
			String cashAccountName) throws ApplicationException {
		logger.info("Entry into checkBankMasterCashAccountExist");
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			con = getUserMgmConnection();
			String query = BankingConstants.CHECK_BANK_MASTER_CASH_ACCOUNT_EXIST_FOR_LOCATION;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, organizationId);
			preparedStatement.setInt(2, locationId);
			preparedStatement.setString(3, cashAccountName);
			rs = preparedStatement.executeQuery();
			if (rs.next()) {
				return true;
			}
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}

		return false;
	}

	public List<BankMasterAccountBaseVo> getAllBankMasterAccountsOfAnOrganization(int organizationId)
			throws ApplicationException {
		logger.info("Entry into getAllBankMasterAccountsOfAnOrganization");
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		List<BankMasterAccountBaseVo> listOfAccounts = new ArrayList<BankMasterAccountBaseVo>();
		try {
			con = getUserMgmConnection();
			String query = BankingConstants.GET_BANK_MASTER_ACCOUNT_INFO_BY_ORGANIZATION;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, organizationId);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				BankMasterAccountBaseVo bankMasterAccountBaseVo = new BankMasterAccountBaseVo();
				bankMasterAccountBaseVo.setId(rs.getInt(1));
				bankMasterAccountBaseVo.setAccountName(rs.getString(2));
				bankMasterAccountBaseVo.setAccountType("Bank Account");
				bankMasterAccountBaseVo.setFinancialInstitution(rs.getString(3));
				bankMasterAccountBaseVo.setDeciferBalance(rs.getString(4));
				bankMasterAccountBaseVo.setActualBalance(rs.getString(4));
				listOfAccounts.add(bankMasterAccountBaseVo);
			}
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return listOfAccounts;
	}

	public List<BankMasterAccountBaseVo> getAllBankMasterAccountsOfAnOrganizationForUserAndRole(int organizationId,
			String userId, String roleName) throws ApplicationException {
		logger.info("Entry into getAllBankMasterAccountsOfAnOrganizationForUserAndRole");
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		List<BankMasterAccountBaseVo> listOfAccounts = new ArrayList<BankMasterAccountBaseVo>();
		try {
			con = getUserMgmConnection();
			String query = "";
			if (roleName.equals("Super Admin")) {
				query = BankingConstants.GET_BANK_MASTER_ACCOUNT_INFO_ORGANIZATION;
			} else {
				query = BankingConstants.GET_BANK_MASTER_ACCOUNT_INFO_BY_ORGANIZATION_FOR_USER_ROLE;
			}

			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, organizationId);
			if (!(roleName.equals("Super Admin"))) {
				preparedStatement.setString(2, userId);
				preparedStatement.setString(3, roleName);
			}
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				BankMasterAccountBaseVo bankMasterAccountBaseVo = new BankMasterAccountBaseVo();
				bankMasterAccountBaseVo.setId(rs.getInt(1));
				bankMasterAccountBaseVo.setAccountName(rs.getString(2));
				bankMasterAccountBaseVo.setAccountType("Bank Account");
				bankMasterAccountBaseVo.setFinancialInstitution(rs.getString(3));
				bankMasterAccountBaseVo.setDeciferBalance(rs.getString(4));
				bankMasterAccountBaseVo.setActualBalance(rs.getString(4));
				listOfAccounts.add(bankMasterAccountBaseVo);
			}
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return listOfAccounts;
	}

	public List<BankMasterAccountBaseVo> getAllBankMasterCardsOfAnOrganization(int organizationId)
			throws ApplicationException {
		logger.info("Entry into getAllBankMasterCardsOfAnOrganization");
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		List<BankMasterAccountBaseVo> listOfCards = new ArrayList<BankMasterAccountBaseVo>();
		try {
			con = getUserMgmConnection();
			String query = BankingConstants.GET_BANK_MASTER_CARD_INFO_BY_ORGANIZATION;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, organizationId);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				BankMasterAccountBaseVo bankMasterCardBaseVo = new BankMasterAccountBaseVo();
				bankMasterCardBaseVo.setId(rs.getInt(1));
				bankMasterCardBaseVo.setAccountName(rs.getString(2));
				bankMasterCardBaseVo.setAccountType("Credit Card");
				bankMasterCardBaseVo.setFinancialInstitution(rs.getString(3));
				bankMasterCardBaseVo.setDeciferBalance(rs.getString(4));
				bankMasterCardBaseVo.setActualBalance(rs.getString(4));
				listOfCards.add(bankMasterCardBaseVo);
			}
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return listOfCards;
	}

	public List<BankMasterAccountBaseVo> getAllBankMasterCardsOfAnOrganizationForUserAndRole(int organizationId,
			String userId, String roleName) throws ApplicationException {
		logger.info("Entry into getAllBankMasterCardsOfAnOrganizationForUserAndRole");
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		List<BankMasterAccountBaseVo> listOfCards = new ArrayList<BankMasterAccountBaseVo>();
		try {
			con = getUserMgmConnection();
			String query = "";
			if (roleName.equals("Super Admin")) {
				query = BankingConstants.GET_BANK_MASTER_CARD_INFO_ORGANIZATION;
			} else {
				query = BankingConstants.GET_BANK_MASTER_CARD_INFO_BY_ORGANIZATION_FOR_USER_ROLE;
			}
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, organizationId);
			if (!(roleName.equals("Super Admin"))) {
				preparedStatement.setString(2, userId);
				preparedStatement.setString(3, roleName);
			}
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				BankMasterAccountBaseVo bankMasterCardBaseVo = new BankMasterAccountBaseVo();
				bankMasterCardBaseVo.setId(rs.getInt(1));
				bankMasterCardBaseVo.setAccountName(rs.getString(2));
				bankMasterCardBaseVo.setAccountType("Credit Card");
				bankMasterCardBaseVo.setFinancialInstitution(rs.getString(3));
				bankMasterCardBaseVo.setDeciferBalance(rs.getString(4));
				bankMasterCardBaseVo.setActualBalance(rs.getString(4));
				listOfCards.add(bankMasterCardBaseVo);
			}
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return listOfCards;
	}

	public List<BankMasterAccountBaseVo> getAllBankMasterWalletsOfAnOrganization(int organizationId)
			throws ApplicationException {
		logger.info("Entry into getAllBankMasterWalletsOfAnOrganization");
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		List<BankMasterAccountBaseVo> listOfAccounts = new ArrayList<BankMasterAccountBaseVo>();
		try {
			con = getUserMgmConnection();
			String query = BankingConstants.GET_BANK_MASTER_WALLET_INFO_BY_ORGANIZATION;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, organizationId);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				BankMasterAccountBaseVo bankMasterWalletBaseVo = new BankMasterAccountBaseVo();
				bankMasterWalletBaseVo.setId(rs.getInt(1));
				bankMasterWalletBaseVo.setAccountName(rs.getString(2));
				bankMasterWalletBaseVo.setAccountType("Wallet");
				bankMasterWalletBaseVo.setFinancialInstitution(rs.getString(3));
				bankMasterWalletBaseVo.setDeciferBalance(rs.getString(4));
				bankMasterWalletBaseVo.setActualBalance(rs.getString(4));
				listOfAccounts.add(bankMasterWalletBaseVo);
			}
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return listOfAccounts;
	}

	public List<BankMasterAccountBaseVo> getAllBankMasterWalletsOfAnOrganizationForUserAndRole(int organizationId,
			String userId, String roleName) throws ApplicationException {
		logger.info("Entry into getAllBankMasterWalletsOfAnOrganizationForUserAndRole");
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		List<BankMasterAccountBaseVo> listOfAccounts = new ArrayList<BankMasterAccountBaseVo>();
		try {
			con = getUserMgmConnection();
			String query = "";
			if (roleName.equals("Super Admin")) {
				query = BankingConstants.GET_BANK_MASTER_WALLET_INFO_ORGANIZATION;
			} else {
				query = BankingConstants.GET_BANK_MASTER_WALLET_INFO_BY_ORGANIZATION_FOR_USER_ROLE;
			}
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, organizationId);
			if (!(roleName.equals("Super Admin"))) {
				preparedStatement.setString(2, userId);
				preparedStatement.setString(3, roleName);
			}
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				BankMasterAccountBaseVo bankMasterWalletBaseVo = new BankMasterAccountBaseVo();
				bankMasterWalletBaseVo.setId(rs.getInt(1));
				bankMasterWalletBaseVo.setAccountName(rs.getString(2));
				bankMasterWalletBaseVo.setAccountType("Wallet");
				bankMasterWalletBaseVo.setFinancialInstitution(rs.getString(3));
				bankMasterWalletBaseVo.setDeciferBalance(rs.getString(4));
				bankMasterWalletBaseVo.setActualBalance(rs.getString(4));
				listOfAccounts.add(bankMasterWalletBaseVo);
			}
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return listOfAccounts;
	}

	public List<BankMasterAccountBaseVo> getAllBankMasterCashAccountsOfAnOrganization(int organizationId)
			throws ApplicationException {
		logger.info("Entry into getAllBankMasterCashAccountsOfAnOrganization");
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		List<BankMasterAccountBaseVo> listOfCashAccounts = new ArrayList<BankMasterAccountBaseVo>();
		try {
			con = getUserMgmConnection();
			String query = BankingConstants.GET_BANK_MASTER_CASH_ACCOUNT_INFO_BY_ORGANIZATION;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, organizationId);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				BankMasterAccountBaseVo bankMasterCashAccountVo = new BankMasterAccountBaseVo();
				bankMasterCashAccountVo.setId(rs.getInt(1));
				bankMasterCashAccountVo.setAccountName(rs.getString(2));
				bankMasterCashAccountVo.setAccountType("Cash Account");
				bankMasterCashAccountVo.setDeciferBalance(rs.getString(3));
				bankMasterCashAccountVo.setActualBalance(rs.getString(3));
				listOfCashAccounts.add(bankMasterCashAccountVo);
			}
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return listOfCashAccounts;

	}

	public List<BankMasterAccountBaseVo> getAllBankMasterCashAccountsOfAnOrganizationForUserAndrole(int organizationId,
			String userId, String roleName) throws ApplicationException {
		logger.info("Entry into getAllBankMasterCashAccountsOfAnOrganizationForUserAndrole");
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		List<BankMasterAccountBaseVo> listOfCashAccounts = new ArrayList<BankMasterAccountBaseVo>();
		try {
			con = getUserMgmConnection();
			String query = "";
			if (roleName.equals("Super Admin")) {
				query = BankingConstants.GET_BANK_MASTER_CASH_ACCOUNT_INFO_ORGANIZATION;
			} else {
				query = BankingConstants.GET_BANK_MASTER_CASH_ACCOUNT_INFO_BY_ORGANIZATION_FOR_USER_ROLE;
			}
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, organizationId);
			if (!(roleName.equals("Super Admin"))) {
				preparedStatement.setString(2, userId);
				preparedStatement.setString(3, roleName);
			}
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				BankMasterAccountBaseVo bankMasterCashAccountVo = new BankMasterAccountBaseVo();
				bankMasterCashAccountVo.setId(rs.getInt(1));
				bankMasterCashAccountVo.setAccountName(rs.getString(2));
				bankMasterCashAccountVo.setAccountType("Cash Account");
				bankMasterCashAccountVo.setDeciferBalance(rs.getString(3));
				bankMasterCashAccountVo.setActualBalance(rs.getString(3));
				listOfCashAccounts.add(bankMasterCashAccountVo);
			}
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return listOfCashAccounts;

	}

	public BankMasterAccountVo getBankMasterAccountsById(int id) throws ApplicationException {
		logger.info("Entry into getBankMasterAccountsById");
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		BankMasterAccountVo BankMasterAccountVo = new BankMasterAccountVo();
		try {
			con = getUserMgmConnection();
			String query = BankingConstants.GET_BANK_MASTER_ACCOUNT_INFO_BY_ID;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, id);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				BankMasterAccountVo.setId(rs.getInt(1));
				BankMasterAccountVo.setAccountName(rs.getString(2));
				BankMasterAccountVo.setAccountType(rs.getInt(3));
				BankMasterAccountVo.setAccountVariant(rs.getInt(4));
				BankMasterAccountVo.setAccountNumber(rs.getString(5));
				BankMasterAccountVo.setAccountCode(rs.getString(6));
				BankMasterAccountVo.setBankName(rs.getString(7));
				BankMasterAccountVo.setBranchName(rs.getString(8));
				BankMasterAccountVo.setIfscCode(rs.getString(9));
				BankMasterAccountVo.setCurrentBalance(rs.getString(10));
				BankMasterAccountVo.setInterestRate(rs.getString(11));
				BankMasterAccountVo.setAccountCurrencyId(rs.getInt(12));
				BankMasterAccountVo.setLimit(rs.getString(13));
				BankMasterAccountVo.setMaturityDate(rs.getDate(14));
				BankMasterAccountVo.setOpeningDate(rs.getDate(15));
				BankMasterAccountVo.setTermMonth(rs.getInt(16));
				BankMasterAccountVo.setTermYear(rs.getInt(17));
				BankMasterAccountVo.setIsSuperAdmin(rs.getBoolean(18));
				BankMasterAccountVo.setOrganizationId(rs.getInt(19));
				BankMasterAccountVo.setStatus(rs.getString(20));
				BankMasterAccountVo.setUserId(rs.getString(21));
			}
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return BankMasterAccountVo;
	}

	public BankMasterCardVo getBankMasterCardsById(int id) throws ApplicationException {
		logger.info("Entry into getBankMasterCardsById");
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		BankMasterCardVo bankMasterCardVo = new BankMasterCardVo();
		try {
			con = getUserMgmConnection();
			String query = BankingConstants.GET_BANK_MASTER_CARD_INFO_BY_ID;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, id);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				bankMasterCardVo.setId(rs.getInt(1));
				bankMasterCardVo.setAccountCode(rs.getString(2));
				bankMasterCardVo.setAccountName(rs.getString(3));
				bankMasterCardVo.setAuthorizedPerson(rs.getString(4));
				bankMasterCardVo.setBillingDate(rs.getDate(5));
				bankMasterCardVo.setCardNumber(rs.getString(6));
				bankMasterCardVo.setCurrentBalance(rs.getString(7));
				bankMasterCardVo.setIssuingBankName(rs.getString(8));
				bankMasterCardVo.setIsSuperAdmin(rs.getBoolean(9));
				bankMasterCardVo.setLimit(rs.getString(10));
				bankMasterCardVo.setOpeningDate(rs.getDate(11));
				bankMasterCardVo.setOrganizationId(rs.getInt(12));
				bankMasterCardVo.setStatus(rs.getString(13));
				bankMasterCardVo.setUserId(rs.getString(14));
			}
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return bankMasterCardVo;
	}

	public BankMasterWalletVo getBankMasterWalletsById(int id) throws ApplicationException {
		logger.info("Entry into getBankMasterWalletsById");
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		BankMasterWalletVo bankMasterWalletVo = new BankMasterWalletVo();
		try {
			con = getUserMgmConnection();
			String query = BankingConstants.GET_BANK_MASTER_WALLET_INFO_BY_ID;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, id);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				bankMasterWalletVo.setId(rs.getInt(1));
				bankMasterWalletVo.setWalletAccountName(rs.getString(2));
				bankMasterWalletVo.setWalletNumber(rs.getString(3));
				bankMasterWalletVo.setWalletProvider(rs.getString(4));
				bankMasterWalletVo.setAccountCode(rs.getString(5));
				bankMasterWalletVo.setAuthorizedPerson(rs.getString(6));
				bankMasterWalletVo.setCurrentBalance(rs.getString(7));
				bankMasterWalletVo.setOpeningDate(rs.getDate(8));
				bankMasterWalletVo.setTransactionLimit(rs.getString(9));
				bankMasterWalletVo.setIsSuperAdmin(rs.getBoolean(10));
				bankMasterWalletVo.setOrganizationId(rs.getInt(11));
				bankMasterWalletVo.setStatus(rs.getString(12));
				bankMasterWalletVo.setUserId(rs.getString(13));
			}
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return bankMasterWalletVo;
	}

	public BankMasterCashAccountVo getBankMasterCashAccountById(int id) throws ApplicationException {
		logger.info("Entry into getBankMasterCashAccountsById");
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		BankMasterCashAccountVo bankMasterCashAccountVo = new BankMasterCashAccountVo();
		try {
			con = getUserMgmConnection();
			String query = BankingConstants.GET_BANK_MASTER_CASH_ACCOUNT_INFO_BY_ID;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, id);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				bankMasterCashAccountVo.setAccountCode(rs.getString(1));
				bankMasterCashAccountVo.setCashAccountName(rs.getString(2));
				bankMasterCashAccountVo.setCurrencyId(rs.getInt(3));
				bankMasterCashAccountVo.setCurrentBalance(rs.getString(4));
				bankMasterCashAccountVo.setId(rs.getInt(5));
				bankMasterCashAccountVo.setIsSuperAdmin(rs.getBoolean(6));
				bankMasterCashAccountVo.setLocationId(rs.getInt(7));
				bankMasterCashAccountVo.setOrganizationId(rs.getInt(8));
				bankMasterCashAccountVo.setStatus(rs.getString(9));
				bankMasterCashAccountVo.setUserId(rs.getString(10));
			}
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return bankMasterCashAccountVo;
	}

	public void updateBankMasterAccounts(BankMasterAccountVo bankMasterAccountVo) throws ApplicationException {
		logger.info("Entry into updateBankMasterAccounts");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = getUserMgmConnection();
			if (bankMasterAccountVo.getAccountNumber() != null && bankMasterAccountVo.getAccountName()!=null) {
				boolean isBankMasterAccountExist = checkBankMasterAccountExistUpdate(
						bankMasterAccountVo.getOrganizationId(), bankMasterAccountVo.getAccountName(),
						bankMasterAccountVo.getId(),bankMasterAccountVo.getBankName());
				if (isBankMasterAccountExist) {
					throw new ApplicationException("Bank Account Exist for the Organization");

				}
			}
			String sql = BankingConstants.UPDATE_BANK_MASTER_ACCOUNTS;
			preparedStatement = con.prepareStatement(sql);
			preparedStatement.setString(1,
					bankMasterAccountVo.getAccountName() != null ? bankMasterAccountVo.getAccountName() : null);
			preparedStatement.setInt(2,
					bankMasterAccountVo.getAccountType() != null ? bankMasterAccountVo.getAccountType() : null);
			preparedStatement.setString(3,
					bankMasterAccountVo.getAccountNumber() != null ? bankMasterAccountVo.getAccountNumber() : null);
			preparedStatement.setInt(4,
					bankMasterAccountVo.getAccountVariant() != null ? bankMasterAccountVo.getAccountVariant() : null);
			preparedStatement.setString(5,
					bankMasterAccountVo.getIfscCode() != null ? bankMasterAccountVo.getIfscCode() : null);
			preparedStatement.setString(6,
					bankMasterAccountVo.getBankName() != null ? bankMasterAccountVo.getBankName() : null);
			preparedStatement.setString(7,
					bankMasterAccountVo.getBranchName() != null ? bankMasterAccountVo.getBranchName() : null);
			preparedStatement.setString(8,
					bankMasterAccountVo.getCurrentBalance() != null ? bankMasterAccountVo.getCurrentBalance() : null);
			preparedStatement.setString(9,
					bankMasterAccountVo.getInterestRate() != null ? bankMasterAccountVo.getInterestRate() : null);
			preparedStatement.setDate(10,
					bankMasterAccountVo.getOpeningDate() != null ? bankMasterAccountVo.getOpeningDate() : null);
			preparedStatement.setInt(11,
					bankMasterAccountVo.getAccountCurrencyId() != null ? bankMasterAccountVo.getAccountCurrencyId()
							: 0);
			preparedStatement.setInt(12,
					bankMasterAccountVo.getTermMonth() != null ? bankMasterAccountVo.getTermMonth() : 0);
			preparedStatement.setInt(13,
					bankMasterAccountVo.getTermYear() != null ? bankMasterAccountVo.getTermYear() : 0);
			preparedStatement.setDate(14,
					bankMasterAccountVo.getMaturityDate() != null ? bankMasterAccountVo.getMaturityDate() : null);
			preparedStatement.setString(15,
					bankMasterAccountVo.getLimit() != null ? bankMasterAccountVo.getLimit() : null);
			preparedStatement.setString(16,
					bankMasterAccountVo.getAccountCode() != null ? bankMasterAccountVo.getAccountCode() : null);
			preparedStatement.setInt(17,
					Integer.valueOf(bankMasterAccountVo.getUserId()) != null
							? Integer.valueOf(bankMasterAccountVo.getUserId())
							: 0);
			preparedStatement.setInt(18,
					bankMasterAccountVo.getOrganizationId() != null ? bankMasterAccountVo.getOrganizationId() : 0);
			preparedStatement.setBoolean(19, bankMasterAccountVo.getIsSuperAdmin());
			preparedStatement.setString(20, bankMasterAccountVo.getRoleName());
			preparedStatement.setInt(21, bankMasterAccountVo.getId());
			preparedStatement.executeUpdate();
		} catch (Exception e) {
			logger.info("Error in updateBankMasterAccounts:: ", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
	}

	private boolean checkBankMasterAccountExistUpdate(int organizationId, String accountName, int id,String bankName)
			throws ApplicationException {
		logger.info("Entry into checkBankMasterAccountExistUpdate");
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			con = getUserMgmConnection();
			String query = BankingConstants.CHECK_BANK_MASTER_ACCOUNT_EXIST_UPDATE_FOR_ORGANIZATION;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, organizationId);
			preparedStatement.setString(2, accountName);
			preparedStatement.setString(3, bankName);
			preparedStatement.setInt(4, id);
			rs = preparedStatement.executeQuery();
			if (rs.next()) {
				return true;
			}
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);

		}
		return false;

	}

	public void updateBankMasterCards(BankMasterCardVo bankMasterCardVo) throws ApplicationException {
		logger.info("Entry into updateBankMasterCards");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = getUserMgmConnection();
			if (bankMasterCardVo.getCardNumber() != null && !(bankMasterCardVo.getCardNumber().isEmpty())) {
				boolean isBankMasterCardExist = checkBankMasterCardExistUpdate(bankMasterCardVo.getOrganizationId(),
						bankMasterCardVo.getCardNumber(), bankMasterCardVo.getId());
				if (isBankMasterCardExist) {
					throw new ApplicationException("Credit Card Account Exist for the Organization");
				}
			}
			String sql = BankingConstants.UPDATE_BANK_MASTER_CARDS;
			preparedStatement = con.prepareStatement(sql);
			preparedStatement.setString(1,
					bankMasterCardVo.getAccountName() != null ? bankMasterCardVo.getAccountName() : null);
			preparedStatement.setString(2,
					bankMasterCardVo.getAuthorizedPerson() != null ? bankMasterCardVo.getAuthorizedPerson() : null);
			preparedStatement.setDate(3,
					bankMasterCardVo.getOpeningDate() != null ? bankMasterCardVo.getOpeningDate() : null);
			preparedStatement.setString(4,
					bankMasterCardVo.getCardNumber() != null ? bankMasterCardVo.getCardNumber() : null);
			preparedStatement.setString(5,
					bankMasterCardVo.getIssuingBankName() != null ? bankMasterCardVo.getIssuingBankName() : null);
			preparedStatement.setString(6,
					bankMasterCardVo.getCurrentBalance() != null ? bankMasterCardVo.getCurrentBalance() : null);
			preparedStatement.setDate(7,
					bankMasterCardVo.getBillingDate() != null ? bankMasterCardVo.getBillingDate() : null);
			preparedStatement.setString(8, bankMasterCardVo.getLimit() != null ? bankMasterCardVo.getLimit() : null);
			preparedStatement.setString(9,
					bankMasterCardVo.getAccountCode() != null ? bankMasterCardVo.getAccountCode() : null);
			preparedStatement.setBoolean(10, bankMasterCardVo.getIsSuperAdmin());
			preparedStatement.setInt(11, bankMasterCardVo.getOrganizationId());
			preparedStatement.setInt(12,
					Integer.valueOf(bankMasterCardVo.getUserId()) != null
							? Integer.valueOf(bankMasterCardVo.getUserId())
							: 0);
			preparedStatement.setString(13, bankMasterCardVo.getRoleName());
			preparedStatement.setInt(14, bankMasterCardVo.getId());
			preparedStatement.executeUpdate();
		} catch (Exception e) {
			logger.info("Error in updateBankMasterCards:: ", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
	}

	private boolean checkBankMasterCardExistUpdate(int organizationId, String cardNumber, int id)
			throws ApplicationException {
		logger.info("Entry into checkBankMasterAccountExistUpdate");
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			con = getUserMgmConnection();
			String query = BankingConstants.CHECK_BANK_MASTER_CARD_EXIST_UPDATE_FOR_ORGANIZATION;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, organizationId);
			preparedStatement.setString(2, cardNumber);
			preparedStatement.setInt(3, id);
			rs = preparedStatement.executeQuery();
			if (rs.next()) {
				return true;
			}
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);

		}
		return false;

	}

	public void updateBankMasterWallets(BankMasterWalletVo bankMasterWalletVo) throws ApplicationException {
		logger.info("Entry into updateBankMasterWallets");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = getUserMgmConnection();
			if (bankMasterWalletVo.getWalletNumber() != null && !(bankMasterWalletVo.getWalletNumber().isEmpty())) {
				boolean isBankMasterWalletExist = checkBankMasterWalletExistUpdate(
						bankMasterWalletVo.getOrganizationId(), bankMasterWalletVo.getWalletNumber(),
						bankMasterWalletVo.getId());
				if (isBankMasterWalletExist) {
					throw new ApplicationException("Wallet Account Exist for the Organization");
				}
			}
			String sql = BankingConstants.UPDATE_BANK_MASTER_WALLETS;
			preparedStatement = con.prepareStatement(sql);
			preparedStatement.setString(1,
					bankMasterWalletVo.getWalletAccountName() != null ? bankMasterWalletVo.getWalletAccountName()
							: null);
			preparedStatement.setString(2,
					bankMasterWalletVo.getAuthorizedPerson() != null ? bankMasterWalletVo.getAuthorizedPerson() : null);
			preparedStatement.setDate(3,
					bankMasterWalletVo.getOpeningDate() != null ? bankMasterWalletVo.getOpeningDate() : null);
			preparedStatement.setString(4,
					bankMasterWalletVo.getWalletNumber() != null ? bankMasterWalletVo.getWalletNumber() : null);
			preparedStatement.setString(5,
					bankMasterWalletVo.getWalletProvider() != null ? bankMasterWalletVo.getWalletProvider() : null);
			preparedStatement.setString(6,
					bankMasterWalletVo.getCurrentBalance() != null ? bankMasterWalletVo.getCurrentBalance() : null);
			preparedStatement.setString(7,
					bankMasterWalletVo.getTransactionLimit() != null ? bankMasterWalletVo.getTransactionLimit() : null);
			preparedStatement.setString(8,
					bankMasterWalletVo.getAccountCode() != null ? bankMasterWalletVo.getAccountCode() : null);
			preparedStatement.setBoolean(9,
					bankMasterWalletVo.getIsSuperAdmin() != null ? bankMasterWalletVo.getIsSuperAdmin() : null);
			preparedStatement.setInt(10,
					bankMasterWalletVo.getOrganizationId() != null ? bankMasterWalletVo.getOrganizationId() : 0);
			preparedStatement.setInt(11,
					Integer.valueOf(bankMasterWalletVo.getUserId()) != null
							? Integer.valueOf(bankMasterWalletVo.getUserId())
							: 0);
			preparedStatement.setString(12, bankMasterWalletVo.getRoleName());
			preparedStatement.setInt(13, bankMasterWalletVo.getId());
			preparedStatement.executeUpdate();
		} catch (Exception e) {
			logger.info("Error in updateBankMasterWallets:: ", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}

	}

	public void updateBankMasterCashAccounts(BankMasterCashAccountVo bankMasterCashAccountVo)
			throws ApplicationException {
		logger.info("Entry into updateBankMasterCashAccounts");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = getUserMgmConnection();
			if (bankMasterCashAccountVo.getCashAccountName() != null
					&& !(bankMasterCashAccountVo.getCashAccountName().isEmpty())) {
				if (bankMasterCashAccountVo.getLocationId() != null) {
					boolean isBankMasterCashAccountExist = checkBankMasterCashAccountExistUpdateForLocation(
							bankMasterCashAccountVo.getOrganizationId(), bankMasterCashAccountVo.getCashAccountName(),
							bankMasterCashAccountVo.getLocationId(), bankMasterCashAccountVo.getId());
					if (isBankMasterCashAccountExist) {
						throw new ApplicationException("Cash Account Exist for the Location");
					}
				} else {
					boolean isBankMasterCashAccountExist = checkBankMasterCashAccountExistUpdateForOrganization(
							bankMasterCashAccountVo.getOrganizationId(), bankMasterCashAccountVo.getCashAccountName(),
							bankMasterCashAccountVo.getId());
					if (isBankMasterCashAccountExist) {
						throw new ApplicationException("Cash Account Exist for the Organization");
					}
				}

			}
			String sql = BankingConstants.UPDATE_BANK_MASTER_CASH_ACCOUNT;
			preparedStatement = con.prepareStatement(sql);
			preparedStatement.setString(1,
					bankMasterCashAccountVo.getCashAccountName() != null ? bankMasterCashAccountVo.getCashAccountName()
							: null);
			preparedStatement.setString(2,
					bankMasterCashAccountVo.getAccountCode() != null ? bankMasterCashAccountVo.getAccountCode() : null);
			preparedStatement.setString(3,
					bankMasterCashAccountVo.getCurrentBalance() != null ? bankMasterCashAccountVo.getCurrentBalance()
							: null);
			preparedStatement.setInt(4,
					bankMasterCashAccountVo.getCurrencyId() != null ? bankMasterCashAccountVo.getCurrencyId() : 0);
			preparedStatement.setInt(5,
					bankMasterCashAccountVo.getLocationId() != null ? bankMasterCashAccountVo.getLocationId() : 0);
			preparedStatement.setInt(6,
					bankMasterCashAccountVo.getOrganizationId() != null ? bankMasterCashAccountVo.getOrganizationId()
							: 0);
			preparedStatement.setBoolean(7,
					bankMasterCashAccountVo.getIsSuperAdmin() != null ? bankMasterCashAccountVo.getIsSuperAdmin()
							: null);
			preparedStatement.setInt(8,
					Integer.valueOf(bankMasterCashAccountVo.getUserId()) != null
							? Integer.valueOf(bankMasterCashAccountVo.getUserId())
							: 0);
			preparedStatement.setTimestamp(9, new Timestamp(System.currentTimeMillis()));
			preparedStatement.setString(10, bankMasterCashAccountVo.getRoleName());
			preparedStatement.setInt(11, bankMasterCashAccountVo.getId());
			preparedStatement.executeUpdate();
		} catch (Exception e) {
			logger.info("Error in updateBankMasterCashAccounts:: ", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
	}

	private boolean checkBankMasterCashAccountExistUpdateForOrganization(Integer organizationId, String cashAccountName,
			Integer id) throws ApplicationException {
		logger.info("Entry into checkBankMasterCashAccountExistForOrganization");
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			con = getUserMgmConnection();
			preparedStatement = con
					.prepareStatement(BankingConstants.CHECK_BANK_MASTER_CASH_ACCOUNT_EXIST_UPDATE_FOR_ORGANIZATION);
			preparedStatement.setInt(1, organizationId);
			preparedStatement.setString(2, cashAccountName);
			preparedStatement.setInt(3, id);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				return true;
			}

		} catch (Exception e) {
			logger.info("Error in  checkBankMasterCashAccountExistForOrganization:", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return false;
	}

	private boolean checkBankMasterCashAccountExistUpdateForLocation(Integer organizationId, String cashAccountName,
			Integer locationId, Integer id) throws ApplicationException {
		logger.info("Entry into checkBankMasterCashAccountExistUpdate");
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			con = getUserMgmConnection();
			String query = BankingConstants.CHECK_BANK_MASTER_CASH_ACCOUNT_EXIST_UPDATE_FOR_LOCATION;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, organizationId);
			preparedStatement.setInt(2, locationId);
			preparedStatement.setString(3, cashAccountName);
			preparedStatement.setInt(4, id);
			rs = preparedStatement.executeQuery();
			if (rs.next()) {
				return true;
			}
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);

		}
		return false;
	}

	private boolean checkBankMasterWalletExistUpdate(int organizationId, String walletNumber, int id)
			throws ApplicationException {
		logger.info("Entry into checkBankMasterAccountExistUpdate");
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			con = getUserMgmConnection();
			String query = BankingConstants.CHECK_BANK_MASTER_WALLET_EXIST_UPDATE_FOR_ORGANIZATION;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, organizationId);
			preparedStatement.setString(2, walletNumber);
			preparedStatement.setInt(3, id);
			rs = preparedStatement.executeQuery();
			if (rs.next()) {
				return true;
			}
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);

		}
		return false;

	}

	public List<BankMasterAccountBaseVo> getAllBankMasterDetailsOfAnOrganization(int organizationId)
			throws ApplicationException {
		logger.info("Entry into createBankMasterWallet");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		List<BankMasterAccountBaseVo> bankMasterAllAccountsVo = new ArrayList<BankMasterAccountBaseVo>();
		try {
			List<BankMasterAccountBaseVo> bankMasterAccounts = getAllBankMasterAccountsOfAnOrganization(organizationId);
			List<BankMasterAccountBaseVo> bankMasterCards = getAllBankMasterCardsOfAnOrganization(organizationId);
			List<BankMasterAccountBaseVo> bankMasterWallets = getAllBankMasterWalletsOfAnOrganization(organizationId);
			List<BankMasterAccountBaseVo> bankMasterCashAccounts = getAllBankMasterCashAccountsOfAnOrganization(
					organizationId);
			if (bankMasterAccounts.size() > 0)
				bankMasterAllAccountsVo.addAll(bankMasterAccounts);
			if (bankMasterCards.size() > 0)
				bankMasterAllAccountsVo.addAll(bankMasterCards);
			if (bankMasterWallets.size() > 0)
				bankMasterAllAccountsVo.addAll(bankMasterWallets);
			if (bankMasterCashAccounts.size() > 0)
				bankMasterAllAccountsVo.addAll(bankMasterCashAccounts);
		} catch (Exception e) {
			logger.info("Error in getAllBankMasterDetailsOfAnOrganization:: ", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return bankMasterAllAccountsVo;
	}

	public List<BankMasterAccountBaseVo> getAllBankMasterDetailsOfAnOrganizationForUserAndRole(int organizationId,
			String userId, String roleName) throws ApplicationException {
		logger.info("Entry into getAllBankMasterDetailsOfAnOrganizationForUserAndRole");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		List<BankMasterAccountBaseVo> bankMasterAllAccountsVo = new ArrayList<BankMasterAccountBaseVo>();
		try {
			List<BankMasterAccountBaseVo> bankMasterAccounts = getAllBankMasterAccountsOfAnOrganizationForUserAndRole(
					organizationId, userId, roleName);
			List<BankMasterAccountBaseVo> bankMasterCards = getAllBankMasterCardsOfAnOrganizationForUserAndRole(
					organizationId, userId, roleName);
			List<BankMasterAccountBaseVo> bankMasterWallets = getAllBankMasterWalletsOfAnOrganizationForUserAndRole(
					organizationId, userId, roleName);
			List<BankMasterAccountBaseVo> bankMasterCashAccounts = getAllBankMasterCashAccountsOfAnOrganizationForUserAndrole(
					organizationId, userId, roleName);
			if (bankMasterAccounts.size() > 0)
				bankMasterAllAccountsVo.addAll(bankMasterAccounts);
			if (bankMasterCards.size() > 0)
				bankMasterAllAccountsVo.addAll(bankMasterCards);
			if (bankMasterWallets.size() > 0)
				bankMasterAllAccountsVo.addAll(bankMasterWallets);
			if (bankMasterCashAccounts.size() > 0)
				bankMasterAllAccountsVo.addAll(bankMasterCashAccounts);
		} catch (Exception e) {
			logger.info("Error in getAllBankMasterDetailsOfAnOrganization:: ", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return bankMasterAllAccountsVo;
	}

	public BasicBankingVo getAllBasicBankingDeatilsForOrg(Integer orgId) throws ApplicationException {
		BasicBankingVo bankingVo = new BasicBankingVo();
		bankingVo.setBankAccounts(getAllBasicBankAccountDeatilsForOrg(orgId));
		bankingVo.setCardAccounts(getAllBasicCardAccountsDetailsForOrg(orgId));
		bankingVo.setCashAccounts(getAllBasicCashAccountsDetailsForOrg(orgId));
		bankingVo.setWallets(getAllBasicWalletDeatilsForOrg(orgId));
		return bankingVo;

	}

	private List<BasicBankMasterWalletVo> getAllBasicWalletDeatilsForOrg(Integer orgId) throws ApplicationException {
		List<BasicBankMasterWalletVo> wallets = new ArrayList<BasicBankMasterWalletVo>();
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			con = getUserMgmConnection();
			preparedStatement = con.prepareStatement(BankingConstants.GET_BASIC_WALLET_DETAILS_FOR_ORG);
			preparedStatement.setInt(1, orgId);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				BasicBankMasterWalletVo vo = new BasicBankMasterWalletVo();
				vo.setValue(rs.getInt(1));
				vo.setName(rs.getString(2));
				vo.setAuthorisedPerson(rs.getString(3));
				vo.setWalletNumber(rs.getString(4));
				vo.setWalletProvider(rs.getString(5));
				vo.setAccountCode(rs.getString(6));
				wallets.add(vo);
			}
			logger.info("Fetched getAllBasicWalletDeatilsForOrg:: " + wallets.size() + preparedStatement.toString());
		} catch (Exception e) {
			logger.info("Error in getAllBasicWalletDeatilsForOrg:: ", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}

		return wallets;

	}

	private List<BasicBankMasterCashAccountVo> getAllBasicCashAccountsDetailsForOrg(Integer orgId)
			throws ApplicationException {
		List<BasicBankMasterCashAccountVo> cashAccounts = new ArrayList<BasicBankMasterCashAccountVo>();
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			con = getUserMgmConnection();
			preparedStatement = con.prepareStatement(BankingConstants.GET_BASIC_CASH_ACCOUNT_DETAILS_FOR_ORG);
			preparedStatement.setInt(1, orgId);
			logger.info("Fetched getAllBasicCashAccountsDeatilsForOrg:: " + cashAccounts.size()
					+ preparedStatement.toString());
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				BasicBankMasterCashAccountVo vo = new BasicBankMasterCashAccountVo();
				vo.setValue(rs.getInt(1));
				vo.setName(rs.getString(2));
				vo.setAccountCode(rs.getString(3));
				cashAccounts.add(vo);
			}
			logger.info("Fetched getAllBasicCashAccountsDeatilsForOrg:: " + cashAccounts.size()
					+ preparedStatement.toString());
		} catch (Exception e) {
			logger.info("Error in getAllBasicCashAccountsDeatilsForOrg:: ", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return cashAccounts;

	}

	private List<BasicBankMasterCardAccountVo> getAllBasicCardAccountsDetailsForOrg(Integer orgId)
			throws ApplicationException {
		List<BasicBankMasterCardAccountVo> cardAccounts = new ArrayList<BasicBankMasterCardAccountVo>();
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			con = getUserMgmConnection();
			preparedStatement = con.prepareStatement(BankingConstants.GET_BASIC_CARD_DETAILS_FOR_ORG);
			preparedStatement.setInt(1, orgId);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				BasicBankMasterCardAccountVo vo = new BasicBankMasterCardAccountVo();
				vo.setValue(rs.getInt(1));
				vo.setName(rs.getString(2));
				vo.setAuthorisedPerson(rs.getString(3));
				vo.setCardNumber(rs.getString(4));
				vo.setIssuerBank(rs.getString(5));
				vo.setAccountCode(rs.getString(6));
				cardAccounts.add(vo);
			}
			logger.info("Fetched getAllBasicCardAccountsDeatilsForOrg:: " + cardAccounts.size()
					+ preparedStatement.toString());
		} catch (Exception e) {
			logger.info("Error in getAllBasicCardAccountsDetailsForOrg:: ", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return cardAccounts;

	}

	private List<BasicBankAccountVo> getAllBasicBankAccountDeatilsForOrg(Integer orgId) throws ApplicationException {
		List<BasicBankAccountVo> bankAccounts = new ArrayList<BasicBankAccountVo>();
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			con = getUserMgmConnection();
			preparedStatement = con.prepareStatement(BankingConstants.GET_BASIC_BANK_ACCOUNT_DETAILS_FOR_ORG);
			preparedStatement.setInt(1, orgId);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				BasicBankAccountVo vo = new BasicBankAccountVo();
				vo.setValue(rs.getInt(1));
				vo.setName(rs.getString(2));
				vo.setAccountNumber(rs.getString(3));
				vo.setIfsc(rs.getString(4));
				vo.setBankName(rs.getString(5));
				vo.setBranchName(rs.getString(6));
				vo.setAccountCode(rs.getString(7));
				bankAccounts.add(vo);
			}
			logger.info("Fetched getAllBasicBankAccountDeatilsForOrg:: " + bankAccounts.size()
					+ preparedStatement.toString());
		} catch (Exception e) {
			logger.info("Error in getAllBasicBankAccountDeatilsForOrg:: ", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return bankAccounts;

	}

	public Integer getBankMasterBankAccountIdByName(String accountName, int orgId) throws ApplicationException {
		logger.info("Entry into getBankMasterBankAccountIdByName");
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Integer accountId = null;
		try {
			con = getUserMgmConnection();
			preparedStatement = con.prepareStatement(BankingConstants.GET_BANK_ACCOUNT_ID);
			preparedStatement.setInt(1, orgId);
			preparedStatement.setString(2, accountName);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				accountId = rs.getInt(1);
			}
		} catch (Exception e) {
			logger.info("Error in getBankMasterBankAccountIdByName:: ", e);
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return accountId;
	}

	public Integer getMasterCardIdByName(String accountName, int orgId) throws ApplicationException {
		logger.info("Entry into getBankMasterBankAccountIdByName");
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Integer cardId = null;
		try {
			con = getUserMgmConnection();
			preparedStatement = con.prepareStatement(BankingConstants.GET_BANK_MASTER_CARD_ID);
			preparedStatement.setInt(1, orgId);
			preparedStatement.setString(2, accountName);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				cardId = rs.getInt(1);
			}
		} catch (Exception e) {
			logger.info("Error in getMasterCardIdByName:: ", e);
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return cardId;
	}

	public Integer getMasterWalletIdByName(String accountName, int orgId) throws ApplicationException {
		logger.info("Entry into getMasterWalletIdByName");
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Integer walletId = null;
		try {
			con = getUserMgmConnection();
			preparedStatement = con.prepareStatement(BankingConstants.GET_BANK_MASTER_WALLET_ID);
			preparedStatement.setInt(1, orgId);
			preparedStatement.setString(2, accountName);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				walletId = rs.getInt(1);
			}
		} catch (Exception e) {
			logger.info("Error in getMasterWalletIdByName:: ", e);
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return walletId;
	}

	public String getAccountName(Integer accountId, String accountType) throws ApplicationException {
		logger.info("Entry into getAccountName");
		Connection con = null;
		String accountName = null;
		try {
			con = getUserMgmConnection();
			if (accountType.equalsIgnoreCase("Bank Account")) {
				accountName = getBankAccountName(accountId, con);
			}
			if (accountType.equalsIgnoreCase("Credit Card")) {
				accountName = getCreditCardName(accountId, con);
			}
			if (accountType.equalsIgnoreCase("Wallet")) {
				accountName = getWalletName(accountId, con);
			}
			if (accountType.equalsIgnoreCase("Cash Account")) {
				accountName = getCashAccountName(accountId, con);
			}
		} catch (Exception e) {
			logger.info("Error in getAccountName:: ", e);
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(null, null, con);
		}
		return accountName;
	}

	private String getWalletName(Integer accountId, Connection con) throws ApplicationException {
		logger.info("Entry into getWalletName");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		String walletName = null;
		try {
			preparedStatement = con.prepareStatement(BankingConstants.GET_BANK_MASTER_WALLET_NAME);
			preparedStatement.setInt(1, accountId);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				walletName = rs.getString(1);
			}
		} catch (Exception e) {
			logger.info("Error in getWalletName:: ", e);
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, null);
		}
		return walletName;

	}

	private String getCreditCardName(Integer accountId, Connection con) throws ApplicationException {
		logger.info("Entry into getCreditCardName");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		String cardName = null;
		try {
			preparedStatement = con.prepareStatement(BankingConstants.GET_BANK_MASTER_CARD_NAME);
			preparedStatement.setInt(1, accountId);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				cardName = rs.getString(1);
			}
		} catch (Exception e) {
			logger.info("Error in getCreditCardName:: ", e);
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, null);
		}
		return cardName;
	}

	private String getBankAccountName(Integer accountId, Connection con) throws ApplicationException {
		logger.info("Entry into getBankAccountName");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		String accountName = null;
		try {
			preparedStatement = con.prepareStatement(BankingConstants.GET_BANK_MASTER_BANK_ACCOUNT_NAME);
			preparedStatement.setInt(1, accountId);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				accountName = rs.getString(1);
			}
		} catch (Exception e) {
			logger.info("Error in getBankAccountName:: ", e);
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, null);
		}
		return accountName;
	}
	
	private String getCashAccountName(Integer accountId, Connection con) throws ApplicationException {
		logger.info("Entry into getBankAccountName");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		String accountName = null;
		try {
			preparedStatement = con.prepareStatement(BankingConstants.GET_BANK_MASTER_CASH_ACCOUNT_NAME);
			preparedStatement.setInt(1, accountId);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				accountName = rs.getString(1);
			}
		} catch (Exception e) {
			logger.info("Error in getBankAccountName:: ", e);
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, null);
		}
		return accountName;
	}
}

package com.blackstrawai.banking;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.banking.dashboard.BankMasterAccountBaseVo;
import com.blackstrawai.banking.dashboard.BankMasterAccountVo;
import com.blackstrawai.banking.dashboard.BankMasterCardVo;
import com.blackstrawai.banking.dashboard.BankMasterCashAccountVo;
import com.blackstrawai.banking.dashboard.BankMasterWalletVo;
import com.blackstrawai.banking.dropdowns.BankMasterDropDownVo;
import com.blackstrawai.chartofaccounts.ChartOfAccountsDao;
import com.blackstrawai.common.BaseService;
import com.blackstrawai.common.DropDownDao;

@Service
public class BankMasterService extends BaseService {

	@Autowired
	BankMasterDao bankMasterDao;

	@Autowired
	DropDownDao dropDownDao;

	@Autowired
	ChartOfAccountsDao chartOfAccountsDao;

	private Logger logger = Logger.getLogger(BankMasterService.class);

	public void createBankMasterAccounts(BankMasterAccountVo bankMasterAccountVo) throws ApplicationException {
		logger.info("Entry into createBankMasterAccounts");
		try {
			bankMasterDao.createBankMasterAccounts(bankMasterAccountVo);
			String displayName=bankMasterAccountVo.getAccountName();
			if (bankMasterAccountVo.getAccountType() == 1 && bankMasterAccountVo.getAccountVariant() == 1) {			
				int id=chartOfAccountsDao.getLevel4IdGivenName(bankMasterAccountVo.getOrganizationId(),"Bank Accounts - Current");
				chartOfAccountsDao.createLevel5WithMinimumDetails(displayName, 
						id, "Bank Accounts - Current", bankMasterAccountVo.getOrganizationId().intValue(),
						bankMasterAccountVo.getUserId(), 
						bankMasterAccountVo.getIsSuperAdmin().booleanValue(),
						false,
						6,
						false,
						bankMasterAccountVo.getRoleName()
						);	

			} else if (bankMasterAccountVo.getAccountType() == 1 && bankMasterAccountVo.getAccountVariant() == 2) {				
				int id=chartOfAccountsDao.getLevel4IdGivenName(bankMasterAccountVo.getOrganizationId(),"Bank Accounts - EEFC");
				chartOfAccountsDao.createLevel5WithMinimumDetails(displayName, 
						id, "Bank Accounts - EEFC", bankMasterAccountVo.getOrganizationId().intValue(),
						bankMasterAccountVo.getUserId(), 
						bankMasterAccountVo.getIsSuperAdmin().booleanValue(),
						false,
						6,
						false,
						bankMasterAccountVo.getRoleName()
						);			

			} else if (bankMasterAccountVo.getAccountType() == 2) {				
				int id=chartOfAccountsDao.getLevel4IdGivenName(bankMasterAccountVo.getOrganizationId(),"Bank Accounts - Savings");
				chartOfAccountsDao.createLevel5WithMinimumDetails(displayName, 
						id, "Bank Accounts - Savings", bankMasterAccountVo.getOrganizationId().intValue(),
						bankMasterAccountVo.getUserId(), 
						bankMasterAccountVo.getIsSuperAdmin().booleanValue(),
						false,
						6,
						false,
						bankMasterAccountVo.getRoleName()
						);		
			} else if (bankMasterAccountVo.getAccountType() == 4 && bankMasterAccountVo.getAccountVariant() == 6) {				
				int id=chartOfAccountsDao.getLevel4IdGivenName(bankMasterAccountVo.getOrganizationId(),"Bank Accounts - Term Deposits");
				chartOfAccountsDao.createLevel5WithMinimumDetails(displayName, 
						id, "Bank Accounts - Term Deposits", bankMasterAccountVo.getOrganizationId().intValue(),
						bankMasterAccountVo.getUserId(), 
						bankMasterAccountVo.getIsSuperAdmin().booleanValue(),
						false,
						6,
						false,
						bankMasterAccountVo.getRoleName()
						);				

			} else if (bankMasterAccountVo.getAccountType() == 4 && bankMasterAccountVo.getAccountVariant() == 7) {				
				int id=chartOfAccountsDao.getLevel4IdGivenName(bankMasterAccountVo.getOrganizationId(),"Bank Accounts - Recurring Deposits");
				chartOfAccountsDao.createLevel5WithMinimumDetails(displayName, 
						id, "Bank Accounts - Recurring Deposits", bankMasterAccountVo.getOrganizationId().intValue(),
						bankMasterAccountVo.getUserId(), 
						bankMasterAccountVo.getIsSuperAdmin().booleanValue(),
						false,
						6,
						false,
						bankMasterAccountVo.getRoleName()
						);		

			} else if (bankMasterAccountVo.getAccountType() == 3 && bankMasterAccountVo.getAccountVariant() == 4) {
				int id=chartOfAccountsDao.getLevel4IdGivenName(bankMasterAccountVo.getOrganizationId(),"Overdraft facility");
				chartOfAccountsDao.createLevel5WithMinimumDetails(displayName, 
						id, "Overdraft facility", bankMasterAccountVo.getOrganizationId().intValue(),
						bankMasterAccountVo.getUserId(), 
						bankMasterAccountVo.getIsSuperAdmin().booleanValue(),
						false,
						6,
						false,
						bankMasterAccountVo.getRoleName()
						);	

			} else if (bankMasterAccountVo.getAccountType() == 3 && bankMasterAccountVo.getAccountVariant() == 5) {

				int id=chartOfAccountsDao.getLevel4IdGivenName(bankMasterAccountVo.getOrganizationId(),"Cash Credit from Bank");
				chartOfAccountsDao.createLevel5WithMinimumDetails(displayName, 
						id, "Cash Credit from Bank", bankMasterAccountVo.getOrganizationId().intValue(),
						bankMasterAccountVo.getUserId(), 
						bankMasterAccountVo.getIsSuperAdmin().booleanValue(),
						false,
						6,
						false,
						bankMasterAccountVo.getRoleName()
						);	
			}

			else if (bankMasterAccountVo.getAccountType() == 5 && bankMasterAccountVo.getAccountVariant() == 8) {

				int id=chartOfAccountsDao.getLevel4IdGivenName(bankMasterAccountVo.getOrganizationId(),"Unpaid dividend accounts");
				chartOfAccountsDao.createLevel5WithMinimumDetails(displayName, 
						id, "Unpaid dividend accounts", bankMasterAccountVo.getOrganizationId().intValue(),
						bankMasterAccountVo.getUserId(), 
						bankMasterAccountVo.getIsSuperAdmin().booleanValue(),
						false,
						6,
						false,
						bankMasterAccountVo.getRoleName()
						);				

			} else if (bankMasterAccountVo.getAccountType() == 5 && bankMasterAccountVo.getAccountVariant() == 9) {
				int id=chartOfAccountsDao.getLevel4IdGivenName(bankMasterAccountVo.getOrganizationId(),"Unpaid matured deposits");
				chartOfAccountsDao.createLevel5WithMinimumDetails(displayName, 
						id, "Unpaid matured deposits", bankMasterAccountVo.getOrganizationId().intValue(),
						bankMasterAccountVo.getUserId(), 
						bankMasterAccountVo.getIsSuperAdmin().booleanValue(),
						false,
						6,
						false,
						bankMasterAccountVo.getRoleName()
						);		

			} else if (bankMasterAccountVo.getAccountType() == 5 && bankMasterAccountVo.getAccountVariant() == 10) {				
				int id=chartOfAccountsDao.getLevel4IdGivenName(bankMasterAccountVo.getOrganizationId(),"Unpaid matured debentures");
				chartOfAccountsDao.createLevel5WithMinimumDetails(displayName, 
						id, "Unpaid matured debentures", bankMasterAccountVo.getOrganizationId().intValue(),
						bankMasterAccountVo.getUserId(), 
						bankMasterAccountVo.getIsSuperAdmin().booleanValue(),
						false,
						6,
						false,
						bankMasterAccountVo.getRoleName()
						);		

			} else if (bankMasterAccountVo.getAccountType() == 5 && bankMasterAccountVo.getAccountVariant() == 11) {				
				int id=chartOfAccountsDao.getLevel4IdGivenName(bankMasterAccountVo.getOrganizationId(),"Bank Account-Share allotment money pending refund");
				chartOfAccountsDao.createLevel5WithMinimumDetails(displayName, 
						id, "Bank Account-Share allotment money pending refund", bankMasterAccountVo.getOrganizationId().intValue(),
						bankMasterAccountVo.getUserId(), 
						bankMasterAccountVo.getIsSuperAdmin().booleanValue(),
						false,
						6,
						false,
						bankMasterAccountVo.getRoleName()
						);				

			} else if (bankMasterAccountVo.getAccountType() == 5 && bankMasterAccountVo.getAccountVariant() == 12) {
				int id=chartOfAccountsDao.getLevel4IdGivenName(bankMasterAccountVo.getOrganizationId(),"Margin Money Deposit");
				chartOfAccountsDao.createLevel5WithMinimumDetails(displayName, 
						id, "Margin Money Deposit", bankMasterAccountVo.getOrganizationId().intValue(),
						bankMasterAccountVo.getUserId(), 
						bankMasterAccountVo.getIsSuperAdmin().booleanValue(),
						false,
						6,
						false,
						bankMasterAccountVo.getRoleName()
						);					

			} else if (bankMasterAccountVo.getAccountType() == 5 && bankMasterAccountVo.getAccountVariant() == 13) {				
				int id=chartOfAccountsDao.getLevel4IdGivenName(bankMasterAccountVo.getOrganizationId(),"Frozen Bank Accounts");
				chartOfAccountsDao.createLevel5WithMinimumDetails(displayName, 
						id, "Frozen Bank Accounts", bankMasterAccountVo.getOrganizationId().intValue(),
						bankMasterAccountVo.getUserId(), 
						bankMasterAccountVo.getIsSuperAdmin().booleanValue(),
						false,
						6,
						false,
						bankMasterAccountVo.getRoleName()
						);		
			}


		} catch (Exception e) {
			//throw new ApplicationException(e);
			throw e;
		}

	}

	public void createBankMasterCard(BankMasterCardVo bankMasterCardVo) throws ApplicationException {
		logger.info("Entry into createBankMasterCard");
		try {
			String displayName=bankMasterCardVo.getAccountName();
			bankMasterDao.createBankMasterCard(bankMasterCardVo);
			int id=chartOfAccountsDao.getLevel4IdGivenName(bankMasterCardVo.getOrganizationId(),"Credit Cards");
			chartOfAccountsDao.createLevel5WithMinimumDetails(displayName, 
					id, "Credit Cards", bankMasterCardVo.getOrganizationId().intValue(),
					bankMasterCardVo.getUserId(), 
					bankMasterCardVo.getIsSuperAdmin().booleanValue(),
					false,
					6,
					false,
					bankMasterCardVo.getRoleName()
					);

		} catch (Exception e) {
			throw e;
		}
	}

	public void createBankMasterWallet(BankMasterWalletVo bankMasterWalletVo) throws ApplicationException {
		logger.info("Entry into createBankMasterWallet");
		try {
			String displayName=bankMasterWalletVo.getWalletAccountName();
			bankMasterDao.createBankMasterWallet(bankMasterWalletVo);
			int id=chartOfAccountsDao.getLevel4IdGivenName(bankMasterWalletVo.getOrganizationId(),"Wallets");		
			chartOfAccountsDao.createLevel5WithMinimumDetails(displayName, 
					id, "Wallets", bankMasterWalletVo.getOrganizationId().intValue(),
					bankMasterWalletVo.getUserId(), 
					bankMasterWalletVo.getIsSuperAdmin().booleanValue(),
					false,
					6,
					false,
					bankMasterWalletVo.getRoleName()
					);

		} catch (Exception e) {
			throw e;
		}
	}

	public void createBankMasterCashAccounts(BankMasterCashAccountVo bankMasterCashAccountVo)
			throws ApplicationException {
		logger.info("Entry into createBankMasterCashAccounts");
		try {
			String displayName=bankMasterCashAccountVo.getCashAccountName();
			bankMasterDao.createBankMasterCashAccounts(bankMasterCashAccountVo);

			if(bankMasterCashAccountVo.getCurrencyId()!=null && bankMasterCashAccountVo.getCurrencyId()>0){
				int id=chartOfAccountsDao.getLevel4IdGivenName(bankMasterCashAccountVo.getOrganizationId(),"Cash in Hand - Foreign Currency");		
				chartOfAccountsDao.createLevel5WithMinimumDetails(displayName, 
						id, "Cash in Hand - Foreign Currency", bankMasterCashAccountVo.getOrganizationId().intValue(),
						bankMasterCashAccountVo.getUserId(), 
						bankMasterCashAccountVo.getIsSuperAdmin().booleanValue(),
						false,
						6,
						false,
						bankMasterCashAccountVo.getRoleName()
						);						
			}else{
				int id=chartOfAccountsDao.getLevel4IdGivenName(bankMasterCashAccountVo.getOrganizationId(),"Cash in Hand");		
				chartOfAccountsDao.createLevel5WithMinimumDetails(displayName, 
						id, "Cash in Hand", bankMasterCashAccountVo.getOrganizationId().intValue(),
						bankMasterCashAccountVo.getUserId(), 
						bankMasterCashAccountVo.getIsSuperAdmin().booleanValue(),
						false,
						6,
						false,
						bankMasterCashAccountVo.getRoleName()
						);		

			}



		} catch (Exception e) {
			throw e;
		}
	}

	public BankMasterDropDownVo getBankMastersDropDownData(int organizationId) throws ApplicationException {
		logger.info("Entry into getBankMastersDropDownData");
		return dropDownDao.getBankMastersDropDownData(organizationId);
	}

	public List<BankMasterAccountBaseVo> getAllBankMasterAccountsOfAnOrganization(int organizationId)
			throws ApplicationException {
		logger.info("Entry into getAllBankMasterAccountsOfAnOrganization");
		return bankMasterDao.getAllBankMasterAccountsOfAnOrganization(organizationId);
	}

	public List<BankMasterAccountBaseVo> getAllBankMasterAccountsOfAnOrganizationForUserAndRole(int organizationId,String userId,String roleName)
			throws ApplicationException {
		logger.info("Entry into getAllBankMasterAccountsOfAnOrganization");
		return bankMasterDao.getAllBankMasterAccountsOfAnOrganizationForUserAndRole(organizationId,userId,roleName);
	}

	public List<BankMasterAccountBaseVo> getAllBankMasterCardsOfAnOrganization(int organizationId)
			throws ApplicationException {
		logger.info("Entry into getAllBankMasterCardsOfAnOrganization");
		return bankMasterDao.getAllBankMasterCardsOfAnOrganization(organizationId);
	}

	public List<BankMasterAccountBaseVo> getAllBankMasterCardsOfAnOrganizationForUserAndRole(int organizationId,String userId,String roleName)
			throws ApplicationException {
		logger.info("Entry into getAllBankMasterCardsOfAnOrganizationForUserAndRole");
		return bankMasterDao.getAllBankMasterCardsOfAnOrganizationForUserAndRole(organizationId,userId,roleName);
	}

	public List<BankMasterAccountBaseVo> getAllBankMasterCashAccountsOfAnOrganization(int organizationId)
			throws ApplicationException {
		logger.info("Entry into getAllBankMasterCashAccountsOfAnOrganization");
		return bankMasterDao.getAllBankMasterCashAccountsOfAnOrganization(organizationId);
	}

	public List<BankMasterAccountBaseVo> getAllBankMasterCashAccountsOfAnOrganizationForUserAndrole(int organizationId,String userId,String roleName)
			throws ApplicationException {
		logger.info("Entry into getAllBankMasterCashAccountsOfAnOrganizationForUserAndrole");
		return bankMasterDao.getAllBankMasterCashAccountsOfAnOrganizationForUserAndrole(organizationId,userId,roleName);
	}

	public List<BankMasterAccountBaseVo> getAllBankMasterWalletsOfAnOrganization(int organizationId)
			throws ApplicationException {
		logger.info("Entry into getAllBankMasterWalletsOfAnOrganization");
		return bankMasterDao.getAllBankMasterWalletsOfAnOrganization(organizationId);
	}

	public List<BankMasterAccountBaseVo> getAllBankMasterWalletsOfAnOrganizationForUserAndRole(int organizationId,String userId,String roleName)
			throws ApplicationException {
		logger.info("Entry into getAllBankMasterWalletsOfAnOrganizationForUserAndRole");
		return bankMasterDao.getAllBankMasterWalletsOfAnOrganizationForUserAndRole(organizationId,userId,roleName);
	}

	public BankMasterAccountVo getBankMasterAccountsById(int id) throws ApplicationException {
		logger.info("Entry into getBankMasterAccountsById");
		return bankMasterDao.getBankMasterAccountsById(id);
	}

	public BankMasterCardVo getBankMasterCardsById(int id) throws ApplicationException {
		logger.info("Entry into getBankMasterCardsById");
		return bankMasterDao.getBankMasterCardsById(id);
	}

	public BankMasterWalletVo getBankMasterWalletsById(int id) throws ApplicationException {
		logger.info("Entry into getBankMasterWalletsById");
		return bankMasterDao.getBankMasterWalletsById(id);
	}

	public BankMasterCashAccountVo getBankMasterCashAccountById(int id) throws ApplicationException {
		logger.info("Entry into getBankMasterCashAccountById");
		return bankMasterDao.getBankMasterCashAccountById(id);
	}

	public void updateBankMasterAccounts(BankMasterAccountVo bankMasterAccountVo) throws ApplicationException {
		logger.info("Entry into updateBankMasterAccounts");
		try {
			bankMasterDao.updateBankMasterAccounts(bankMasterAccountVo);
		} catch (Exception e) {
			//throw new ApplicationException(e);
			throw e;
		}

	}

	public void updateBankMasterCards(BankMasterCardVo bankMasterCardVo) throws ApplicationException {
		logger.info("Entry into updateBankMasterCards");
		try {
			bankMasterDao.updateBankMasterCards(bankMasterCardVo);
		} catch (Exception e) {
			//throw new ApplicationException(e);
			throw e;
		}
	}

	public void updateBankMasterWallets(BankMasterWalletVo bankMasterWalletVo) throws ApplicationException {
		logger.info("Entry into updateBankMasterWallets");
		try {
			bankMasterDao.updateBankMasterWallets(bankMasterWalletVo);
		} catch (Exception e) {
			//throw new ApplicationException(e);
			throw e;
		}
	}

	public void updateBankMasterCashAccounts(BankMasterCashAccountVo bankMasterCashAccountVo)
			throws ApplicationException {
		logger.info("Entry into updateBankMasterCashAccounts");
		try {
			bankMasterDao.updateBankMasterCashAccounts(bankMasterCashAccountVo);
		} catch (Exception e) {
			//throw new ApplicationException(e);
			throw e;
		}
	}

	public List<BankMasterAccountBaseVo> getAllBankMasterDetailsOfAnOrganization(int organizationId)
			throws ApplicationException {
		logger.info("Entry into getAllBankMasterDetailsOfAnOrganization");
		return bankMasterDao.getAllBankMasterDetailsOfAnOrganization(organizationId);
	}

	public List<BankMasterAccountBaseVo> getAllBankMasterDetailsOfAnOrganizationForUserAndRole(int organizationId,String userId,String roleName)
			throws ApplicationException {
		logger.info("Entry into getAllBankMasterDetailsOfAnOrganization");
		return bankMasterDao.getAllBankMasterDetailsOfAnOrganizationForUserAndRole(organizationId,userId,roleName);
	}
}

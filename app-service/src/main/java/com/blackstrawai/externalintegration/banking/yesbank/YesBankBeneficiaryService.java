package com.blackstrawai.externalintegration.banking.yesbank;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.banking.BankMasterDao;
import com.blackstrawai.banking.dashboard.BankMasterAccountVo;
import com.blackstrawai.common.BaseService;
import com.blackstrawai.externalintegration.yesbank.beneficiary.BeneficiaryListVo;
import com.blackstrawai.externalintegration.yesbank.beneficiary.YesBankBeneficiaryVo;
import com.blackstrawai.externalintegration.yesbank.dropdowns.BeneficiaryDropDownVo;
import com.blackstrawai.keycontact.customer.CustomerBankDetailsVo;
import com.blackstrawai.keycontact.vendor.VendorBankDetailsVo;

@Service
public class YesBankBeneficiaryService extends BaseService {

  private final Logger logger = Logger.getLogger(YesBankPaymentsService.class);

  private final YesBankBeneficiaryDao yesBankBeneficiaryDao;
  private final BankMasterDao bankMasterDao;
  private final YesBankIntegrationDao yesBankIntegrationDao;
  private final YesBankPaymentsService yesBankPaymentsService;

  public YesBankBeneficiaryService(
      YesBankBeneficiaryDao yesBankBeneficiaryDao,
      BankMasterDao bankMasterDao,
      YesBankIntegrationDao yesBankIntegrationDao,
      YesBankPaymentsService yesBankPaymentsService) {
    this.yesBankBeneficiaryDao = yesBankBeneficiaryDao;
    this.bankMasterDao = bankMasterDao;
    this.yesBankIntegrationDao = yesBankIntegrationDao;
    this.yesBankPaymentsService = yesBankPaymentsService;
  }

  public void createBeneficiary(YesBankBeneficiaryVo beneficiaryVo) throws ApplicationException {
    logger.info("Create new Beneficiary");
    if (beneficiaryVo != null) {
      yesBankBeneficiaryDao.createNewBeneficiary(beneficiaryVo);
    }
  }

  public BeneficiaryDropDownVo getDropDowns(Integer orgId) {
    return yesBankBeneficiaryDao.getDropDownValues(orgId);
  }

  public List<BeneficiaryListVo> getBeneficiaryList(Integer orgId) throws ApplicationException {
    return yesBankBeneficiaryDao.getBeneficiaryListForOrg(orgId);
  }

  public void updateBankingBeneficiary(YesBankBeneficiaryVo beneficiaryVo)
      throws ApplicationException {
    logger.info(":::update Bank Beneficiary Service::::");
    if (beneficiaryVo != null) {
      yesBankBeneficiaryDao.updateBankingBeneficiary(beneficiaryVo);
    }
  }

  public void updateVendorBeneficiary(VendorBankDetailsVo beneficiaryVo)
      throws ApplicationException {
    logger.info(":::update Vendor Beneficiary Service::::");
    if (beneficiaryVo != null) {
      yesBankBeneficiaryDao.updateVendorBeneficiary(beneficiaryVo);
    }
  }

  public void updateCustomerBeneficiary(CustomerBankDetailsVo beneficiaryVo)
      throws ApplicationException {
    logger.info(":::update Customer Beneficiary Service::::");
    if (beneficiaryVo != null) {
      yesBankBeneficiaryDao.updateCustomerBeneficiary(beneficiaryVo);
    }
  }

  public void updateEmployeeBeneficiary(VendorBankDetailsVo beneficiaryVo)
      throws ApplicationException {
    logger.info(":::update Employee Beneficiary Service::::");
    if (beneficiaryVo != null) {
      yesBankBeneficiaryDao.updateEmployeeBeneficiary(beneficiaryVo);
    }
  }

  public void disableBeneficiary(String id, String beneficiaryType, String status)
      throws ApplicationException {
    logger.info("::: Disable beneficiary service");
    yesBankBeneficiaryDao.disableBeneficiary(id, beneficiaryType, status);
  }

  public YesBankBeneficiaryVo getBankingBeneficiary(int orgId, int contactId)
      throws ApplicationException {
    logger.info(":::get Bank Beneficiary Service::::");
    return yesBankBeneficiaryDao.getBankingBeneficiaryById(contactId);
  }

  public VendorBankDetailsVo getVendorBeneficiaryyById(int orgId, int contactId)
      throws ApplicationException {
    logger.info(":::get Vendor Beneficiary Service::::");
    return yesBankBeneficiaryDao.getVendorBeneficiaryById(contactId);
  }

  public VendorBankDetailsVo getCustomerBeneficiary(int orgId, int contactId)
      throws ApplicationException {
    logger.info(":::update Customer Beneficiary Service::::");
    return yesBankBeneficiaryDao.getCustomerBeneficiaryById(contactId);
  }

  public VendorBankDetailsVo getEmployeeBeneficiary(int orgId, int contactId)
      throws ApplicationException {
    logger.info(":::update Employee Beneficiary Service::::");
    return yesBankBeneficiaryDao.getEmployeeBeneficiaryById(contactId);
  }

  public void updateBankMaster(BankMasterAccountVo bankMasterAccountVo)
      throws ApplicationException {
    bankMasterDao.updateBankMasterAccounts(bankMasterAccountVo);
  }


}

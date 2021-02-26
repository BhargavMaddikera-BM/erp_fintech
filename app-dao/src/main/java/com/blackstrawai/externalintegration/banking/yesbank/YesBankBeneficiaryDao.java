package com.blackstrawai.externalintegration.banking.yesbank;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.common.BaseDao;
import com.blackstrawai.common.CommonConstants;
import com.blackstrawai.common.DropDownDao;
import com.blackstrawai.externalintegration.yesbank.beneficiary.BeneficiaryListVo;
import com.blackstrawai.externalintegration.yesbank.beneficiary.BeneficiaryTypeVo;
import com.blackstrawai.externalintegration.yesbank.beneficiary.YesBankBeneficiaryVo;
import com.blackstrawai.externalintegration.yesbank.dropdowns.BeneficiaryDropDownVo;
import com.blackstrawai.externalintegration.yesbank.dropdowns.PaymentBeneficiaryListVo;
import com.blackstrawai.externalintegration.yesbank.dropdowns.PaymentBillReferenceVo;
import com.blackstrawai.keycontact.CustomerDao;
import com.blackstrawai.keycontact.EmployeeDao;
import com.blackstrawai.keycontact.VendorDao;
import com.blackstrawai.keycontact.customer.CustomerBankDetailsVo;
import com.blackstrawai.keycontact.vendor.VendorBankDetailsVo;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Repository
public class YesBankBeneficiaryDao extends BaseDao {

	private final Logger logger = Logger.getLogger(YesBankBeneficiaryDao.class);

	private final DropDownDao dropDownDao;
	private final VendorDao vendorDao;
	private final CustomerDao customerDao;
	private final EmployeeDao employeeDao;

	public YesBankBeneficiaryDao(
			DropDownDao dropDownDao,
			VendorDao vendorDao,
			CustomerDao customerDao,
			EmployeeDao employeeDao) {
		this.dropDownDao = dropDownDao;
		this.vendorDao = vendorDao;
		this.customerDao = customerDao;
		this.employeeDao = employeeDao;
	}

	// Add Beneficiary
	public YesBankBeneficiaryVo createNewBeneficiary(YesBankBeneficiaryVo beneficiary)
			throws ApplicationException {
		logger.info("Create New Beneficiary");
		Connection con = null;
		PreparedStatement preparedStatement = null;
		try {
			con = getBankingConnection();
			if (beneficiary.getId() != null && checkBeneficiaryExistForAnOrganization(beneficiary)) {
				throw new ApplicationException(YesBankConstants.ERROR_YBL_BENEFICIARY_EXIST);
			}
			preparedStatement = con.prepareStatement(YesBankConstants.CREATE_NEW_BENEFICIARY);
			preparedStatement.setString(1, beneficiary.getBeneficiaryType());
			preparedStatement.setString(2, beneficiary.getBeneficiaryName());
			preparedStatement.setString(3, beneficiary.getAccountNumber());
			preparedStatement.setString(4, beneficiary.getAccountType());
			preparedStatement.setString(5, beneficiary.getIfscCode());
			preparedStatement.setInt(6, beneficiary.getOrganizationId());
			preparedStatement.setString(7, CommonConstants.STATUS_AS_ACTIVE);
			preparedStatement.setDate(8, new Date(System.currentTimeMillis()));
			preparedStatement.setString(9, beneficiary.getUserId());
			preparedStatement.setString(10, beneficiary.getRoleName());
			preparedStatement.setString(11, beneficiary.getMobileNo());
			preparedStatement.setString(12, beneficiary.getEmail());
			preparedStatement.executeUpdate();
		} catch (Exception e) {
			logger.info("Error in creating new Beneficiary" + e);
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(null, preparedStatement, con);
		}
		return beneficiary;
	}

	public BeneficiaryDropDownVo getDropDownValues(Integer orgId) {
		return dropDownDao.getYesBankBeneficiaryDropDown(orgId);
	}

	public List<BeneficiaryTypeVo> getPaymentDropDownValues(Integer orgId)
			throws ApplicationException {

		List<BeneficiaryTypeVo> beneficiaryTypes = new ArrayList<>();
		List<PaymentBeneficiaryListVo> beneficiaryList;
		BeneficiaryTypeVo beneficiaryTypeVo = new BeneficiaryTypeVo();

		Connection con = getAccountsPayable();

		beneficiaryTypeVo.setId(1);
		beneficiaryTypeVo.setName(YesBankConstants.KEY_CONTACTS_CONTACT);
		beneficiaryTypeVo.setValue(YesBankConstants.KEY_CONTACTS_CONTACT);
		beneficiaryTypes.add(beneficiaryTypeVo);
		beneficiaryList = new ArrayList<>();
		beneficiaryList.addAll(getPaymentBeneficiaryDetailsResults(orgId,con,YesBankConstants.GET_PAYMENT_VENDOR_BENEFICIARY_INFO,YesBankConstants.BENEFICIARY_TYPE_VENDOR));
		closeResources(null, null, con);


		con = getAccountsReceivableConnection();
		beneficiaryList.addAll(getPaymentBeneficiaryDetailsResults(orgId,con,YesBankConstants.GET_PAYMENT_CUSTOMER_BENEFICIARY_INFO,YesBankConstants.BENEFICIARY_TYPE_CUSTOMER));
		closeResources(null, null, con);

		con = getAccountsPayable();
		beneficiaryList.addAll(getPaymentBeneficiaryDetailsResults(orgId,con,YesBankConstants.GET_PAYMENT_EMPLOYEE_BENEFICIARY_INFO,YesBankConstants.BENEFICIARY_TYPE_EMPLOYEE));
		closeResources(null, null, con);

		beneficiaryTypeVo.setBeneficiaryList(beneficiaryList);
		beneficiaryTypeVo = new BeneficiaryTypeVo();
		beneficiaryTypeVo.setId(2);
		beneficiaryTypeVo.setName(YesBankConstants.OTHER_BENEFICIARY);
		beneficiaryTypeVo.setValue(YesBankConstants.OTHER_BENEFICIARY);
		beneficiaryTypes.add(beneficiaryTypeVo);
		beneficiaryList = new ArrayList<>();

		con = getBankingConnection();
		beneficiaryList.addAll(getPaymentBeneficiaryDetailsResults(orgId,con,YesBankConstants.GET_PAYMENT_YES_BANK_BENEFICIARY_INFO,YesBankConstants.OTHER_BENEFICIARY));
		closeResources(null, null, con);

		beneficiaryTypeVo.setBeneficiaryList(beneficiaryList);
		beneficiaryTypeVo = new BeneficiaryTypeVo();
		beneficiaryTypeVo.setId(3);
		beneficiaryTypeVo.setName(YesBankConstants.SELF_BENEFICIARY);
		beneficiaryTypeVo.setValue(YesBankConstants.SELF_BENEFICIARY);
		beneficiaryTypes.add(beneficiaryTypeVo);
		beneficiaryList = new ArrayList<>();

		con = getBankingConnection();
		beneficiaryList.addAll(getPaymentBeneficiaryDetailsResults(orgId,con,YesBankConstants.GET_PAYMENT_ORG_BENEFICIARY_INFO,YesBankConstants.SELF_BENEFICIARY));
		beneficiaryTypeVo.setBeneficiaryList(beneficiaryList);
		closeResources(null, null, con);


		return beneficiaryTypes;
	}

	private boolean checkBeneficiaryExistForAnOrganization(YesBankBeneficiaryVo beneficiaryVo)
			throws ApplicationException {
		Boolean isBeneficiaryExist = false;
		Connection con =null;
		PreparedStatement preparedStatement=null;
		ResultSet rs =null;
		try {
			con = getBankingConnection();
			preparedStatement =con.prepareStatement(YesBankConstants.CHECK_BENEFICIARY_EXIST);
			preparedStatement.setInt(1, beneficiaryVo.getOrganizationId());
			preparedStatement.setString(2, beneficiaryVo.getAccountNumber());
			preparedStatement.setString(3, beneficiaryVo.getIfscCode());
			rs = preparedStatement.executeQuery();
			if (rs.next()) {
				if (beneficiaryVo.getId() == rs.getInt(1)) {
					isBeneficiaryExist = true;
				}
			}
			logger.info("checkBeneficiaryExistForAnOrganization " + isBeneficiaryExist);
		} catch (Exception e) {
			logger.info("Error in checkBeneficiaryExistForAnOrganization  ", e);
			throw new ApplicationException(e);
		}finally{
			closeResources(rs, preparedStatement, con);
		}
		return isBeneficiaryExist;
	}

	private boolean checkBeneficiaryExist(YesBankBeneficiaryVo beneficiaryVo)
			throws ApplicationException {
		Boolean isBeneficiaryExist = false;
		Connection con =null;
		PreparedStatement preparedStatement=null;
		ResultSet rs =null;
		try {
			con = getBankingConnection();
			preparedStatement =con.prepareStatement(YesBankConstants.CHECK_BENEFICIARY_IF_EXIST);
			preparedStatement.setInt(1, beneficiaryVo.getOrganizationId());
			preparedStatement.setString(2, beneficiaryVo.getAccountNumber());
			preparedStatement.setString(3, beneficiaryVo.getIfscCode());
			preparedStatement.setInt(4, beneficiaryVo.getId());
			rs = preparedStatement.executeQuery();
			if (rs.next()) {
					isBeneficiaryExist = true;
			}
			logger.info("checkBeneficiaryExistForAnOrganization " + isBeneficiaryExist);
		} catch (Exception e) {
			logger.info("Error in checkBeneficiaryExistForAnOrganization  ", e);
			throw new ApplicationException(e);
		}finally{
			closeResources(rs, preparedStatement, con);
		}
		return isBeneficiaryExist;
	}

	public YesBankBeneficiaryVo updateBankingBeneficiary(YesBankBeneficiaryVo beneficiary)
			throws ApplicationException {
		logger.info("::: update Beneficiary :::");

		if (checkBeneficiaryExist(beneficiary)) {
      throw new ApplicationException(YesBankConstants.ERROR_YBL_BENEFICIARY_EXIST);
    }

		Connection con =null;
		PreparedStatement preparedStatement=null;

		try {
			con = getBankingConnection();
			preparedStatement =con.prepareStatement(YesBankConstants.UPDATE_BENEFICIARY);
			preparedStatement.setString(1, beneficiary.getBeneficiaryType());
			preparedStatement.setString(2, beneficiary.getBeneficiaryName());
			preparedStatement.setString(3, beneficiary.getAccountNumber());
			preparedStatement.setString(4, beneficiary.getAccountType());
			preparedStatement.setString(5, beneficiary.getIfscCode());
			preparedStatement.setDate(6, new Date(System.currentTimeMillis()));
			preparedStatement.setString(7, beneficiary.getUserId());
			preparedStatement.setString(8, beneficiary.getRoleName());
			preparedStatement.setString(9, beneficiary.getMobileNo());
			preparedStatement.setString(10, beneficiary.getEmail());
			preparedStatement.setInt(11, beneficiary.getId());
			preparedStatement.executeUpdate();

		} catch (Exception e) {
			logger.info("::: Error in updating Beneficiary Details ::::" + e);
			throw new ApplicationException(e.getMessage());
		}finally{
			closeResources(null, preparedStatement, con);
		}
		return beneficiary;
	}

	public void updateVendorBeneficiary(VendorBankDetailsVo beneficiary) throws ApplicationException {
		logger.info("::: update vendor Beneficiary :::");
		Connection con = null;
		try {
			con = getAccountsPayable();
			vendorDao.updateBankDetails(beneficiary, beneficiary.getStatus(), con);
		} catch (Exception e) {
			logger.info("::: Error in updating vendor Beneficiary Details ::::" + e);
			throw new ApplicationException(e.getMessage());
		}finally{
			closeResources(null, null, con);
		}
	}

	public void updateCustomerBeneficiary(CustomerBankDetailsVo beneficiary)
			throws ApplicationException {
		logger.info("::: update vendor Beneficiary :::");
		Connection con = null;
		try{
			con = getAccountsReceivableConnection();
			customerDao.updateBankDetails(beneficiary, con, null);
		} catch (Exception e) {
			logger.info("::: Error in updating vendor Beneficiary Details ::::" + e);
			throw new ApplicationException(e.getMessage());
		}finally{
			closeResources(null, null, con);
		}
	}

	public void updateEmployeeBeneficiary(VendorBankDetailsVo beneficiary)
			throws ApplicationException {
		logger.info("::: update vendor Beneficiary :::");
		Connection con = null;
		try {
			con = getAccountsPayable();
			employeeDao.updateBankDetails(beneficiary, beneficiary.getStatus(), con);
		} catch (Exception e) {
			logger.info("::: Error in updating vendor Beneficiary Details ::::" + e);
			throw new ApplicationException(e.getMessage());
		}finally{
			closeResources(null, null, con);
		}
	}

	public List<BeneficiaryListVo> getBeneficiaryListForOrg(int orgId) throws ApplicationException {
		List<BeneficiaryListVo> beneficiaryList = new ArrayList<BeneficiaryListVo>();
		logger.info("To get Beneficiary list");
		beneficiaryList.addAll(getYesBankBeneficiary(orgId));
		beneficiaryList.addAll(getVendorBeneficiary(orgId));
		beneficiaryList.addAll(getCustomerBeneficiary(orgId));
		beneficiaryList.addAll(getEmployeeBeneficiary(orgId));
		beneficiaryList.addAll(getOrgBeneficiary(orgId));
		beneficiaryList.sort(Comparator.comparing(BeneficiaryListVo::getAccountName));
		return beneficiaryList;
	}

	public List<BeneficiaryListVo> getVendorBeneficiary(int orgId) throws ApplicationException {
		List<BeneficiaryListVo> vedorBeneficiaryList = new ArrayList<BeneficiaryListVo>();
		logger.info("To get vendor Beneficiary list");
		Connection con =null;
		PreparedStatement preparedStatement=null;
		ResultSet rs =null;
		try {
			con = getAccountsPayable();
			preparedStatement =con.prepareStatement(YesBankConstants.GET_VENDOR_BENEFICIARY_FOR_ORG);
			preparedStatement.setInt(1, orgId);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				BeneficiaryListVo beneficiary = new BeneficiaryListVo();
				beneficiary.setContactId(rs.getInt(1));
				beneficiary.setAccountName(rs.getString(2));
				beneficiary.setBeneficiaryType(YesBankConstants.BENEFICIARY_TYPE_VENDOR);
				beneficiary.setType(YesBankConstants.BENEFICIARY_TYPE_VENDOR);
				beneficiary.setStatus(rs.getString(4));
				beneficiary.setBankId(rs.getInt(5));
				beneficiary.setAccountNumber(rs.getString(6));
				beneficiary.setIfscCode(rs.getString(7));
				beneficiary.setAmountPaid(getTotalPaidAmountByBeneficiary(orgId,rs.getInt(5) ,YesBankConstants.BENEFICIARY_TYPE_VENDOR));
				beneficiary.setLastTransaction(getLastTxnMadeByBeneficiary(orgId,rs.getInt(5) ,YesBankConstants.BENEFICIARY_TYPE_VENDOR));
				vedorBeneficiaryList.add(beneficiary);
			}
		} catch (Exception e) {
			logger.info("::: Exception in getting vendor Beneficiary Details ::::" + e);
			throw new ApplicationException(e.getMessage());
		}finally{
			closeResources(rs, preparedStatement, con);
		}
		return vedorBeneficiaryList;
	}

	public List<BeneficiaryListVo> getCustomerBeneficiary(int orgId) throws ApplicationException {
		List<BeneficiaryListVo> customerBeneficiaryList = new ArrayList<BeneficiaryListVo>();
		logger.info("To get customer Beneficiary list");
		Connection con =null;
		PreparedStatement preparedStatement=null;
		ResultSet rs =null;
		try {
			con = getAccountsReceivableConnection();
			preparedStatement =con.prepareStatement(YesBankConstants.GET_CUSTOMER_BENEFICIARY_FOR_ORG);
			preparedStatement.setInt(1, orgId);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				BeneficiaryListVo beneficiary = new BeneficiaryListVo();
				beneficiary.setContactId(rs.getInt(1));
				beneficiary.setAccountName(rs.getString(2));
				beneficiary.setBeneficiaryType(YesBankConstants.BENEFICIARY_TYPE_CUSTOMER);
				beneficiary.setType(YesBankConstants.BENEFICIARY_TYPE_CUSTOMER);
				beneficiary.setStatus(rs.getString(4));
				beneficiary.setBankId(rs.getInt(5));
				beneficiary.setAccountNumber(rs.getString(6));
				beneficiary.setIfscCode(rs.getString(7));
				beneficiary.setAmountPaid(getTotalPaidAmountByBeneficiary(orgId,rs.getInt(5) ,YesBankConstants.BENEFICIARY_TYPE_CUSTOMER));
				beneficiary.setLastTransaction(getLastTxnMadeByBeneficiary(orgId,rs.getInt(5) ,YesBankConstants.BENEFICIARY_TYPE_CUSTOMER));
				customerBeneficiaryList.add(beneficiary);
			}
		} catch (Exception e) {
			logger.info("::: Exception in getting customer Beneficiary Details ::::" + e);
			throw new ApplicationException(e.getMessage());
		}finally{
			closeResources(rs, preparedStatement, con);
		}
		return customerBeneficiaryList;
	}

	public List<BeneficiaryListVo> getEmployeeBeneficiary(int orgId) throws ApplicationException {
		List<BeneficiaryListVo> employeeBeneficiaryList = new ArrayList<BeneficiaryListVo>();
		logger.info("To get vendor Beneficiary list");
		Connection con =null;
		PreparedStatement preparedStatement=null;
		ResultSet rs =null;
		try{
			con = getAccountsPayable();
			preparedStatement =con.prepareStatement(YesBankConstants.GET_EMPLOYEE_BENEFICIARY_FOR_ORG);
			preparedStatement.setInt(1, orgId);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				BeneficiaryListVo beneficiary = new BeneficiaryListVo();
				beneficiary.setContactId(rs.getInt(1));
				beneficiary.setAccountName(rs.getString(2));
				beneficiary.setBeneficiaryType(YesBankConstants.BENEFICIARY_TYPE_EMPLOYEE);
				beneficiary.setType(YesBankConstants.BENEFICIARY_TYPE_EMPLOYEE);
				beneficiary.setStatus(rs.getString(4));
				beneficiary.setBankId(rs.getInt(5));
				beneficiary.setAccountNumber(rs.getString(6));
				beneficiary.setIfscCode(rs.getString(7));
				beneficiary.setAmountPaid(getTotalPaidAmountByBeneficiary(orgId,rs.getInt(5) ,YesBankConstants.BENEFICIARY_TYPE_EMPLOYEE));
				beneficiary.setLastTransaction(getLastTxnMadeByBeneficiary(orgId,rs.getInt(5) ,YesBankConstants.BENEFICIARY_TYPE_EMPLOYEE));
				employeeBeneficiaryList.add(beneficiary);
			}
		} catch (Exception e) {
			logger.info("::: Exception in getting vendor Beneficiary Details ::::" + e);
			throw new ApplicationException(e.getMessage());
		}finally{
			closeResources(rs, preparedStatement, con);
		}
		return employeeBeneficiaryList;
	}

	public List<BeneficiaryListVo> getOrgBeneficiary(int orgId) throws ApplicationException {
		List<BeneficiaryListVo> employeeBeneficiaryList = new ArrayList<BeneficiaryListVo>();
		logger.info("To get vendor Beneficiary list");
		Connection con =null;
		PreparedStatement preparedStatement=null;
		ResultSet rs =null;
		try {
			con = getUserMgmConnection();
			preparedStatement =con.prepareStatement(YesBankConstants.GET_ORG_BENEFICIARY_FOR_ORG);
			preparedStatement.setInt(1, orgId);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				BeneficiaryListVo beneficiary = new BeneficiaryListVo();
				beneficiary.setContactId(rs.getInt(1));
				beneficiary.setAccountName(rs.getString(2));
				beneficiary.setBeneficiaryType(YesBankConstants.BENEFICIARY_TYPE_BANK_MASTER);
				beneficiary.setType(YesBankConstants.BENEFICIARY_TYPE_BANK_MASTER);
				beneficiary.setStatus(rs.getString(3));
				beneficiary.setBankId(rs.getInt(1));
				beneficiary.setAccountNumber(rs.getString(4));
				beneficiary.setIfscCode(rs.getString(5));
				beneficiary.setAmountPaid(getTotalPaidAmountByBeneficiary(orgId,rs.getInt(1) ,YesBankConstants.BENEFICIARY_TYPE_BANK_MASTER ));
				beneficiary.setLastTransaction(getLastTxnMadeByBeneficiary(orgId,rs.getInt(1) ,YesBankConstants.BENEFICIARY_TYPE_BANK_MASTER ));
				employeeBeneficiaryList.add(beneficiary);
			}
		} catch (Exception e) {
			logger.info("::: Exception in getting vendor Beneficiary Details ::::" + e);
			throw new ApplicationException(e.getMessage());
		}finally{
			closeResources(rs, preparedStatement, con);
		}
		return employeeBeneficiaryList;
	}

	public List<BeneficiaryListVo> getYesBankBeneficiary(int orgId) throws ApplicationException {
		List<BeneficiaryListVo> employeeBeneficiaryList = new ArrayList<BeneficiaryListVo>();
		logger.info("To get vendor Beneficiary list");
		Connection con =null;
		PreparedStatement preparedStatement=null;
		ResultSet rs =null;
		try{
			con = getUserMgmConnection();
			preparedStatement =con.prepareStatement(YesBankConstants.GET_YES_BANK_BENEFICIARY_FOR_ORG);
			preparedStatement.setInt(1, orgId);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				BeneficiaryListVo beneficiary = new BeneficiaryListVo();
				beneficiary.setContactId(rs.getInt(1));
				beneficiary.setAccountName(rs.getString(2));
				beneficiary.setBeneficiaryType(rs.getString(3));
				beneficiary.setType(YesBankConstants.OTHER_BENEFICIARY);
				beneficiary.setStatus(rs.getString(4));
				beneficiary.setBankId(rs.getInt(1));
				beneficiary.setAccountNumber(rs.getString(5));
				beneficiary.setIfscCode(rs.getString(6));
				beneficiary.setAmountPaid(getTotalPaidAmountByBeneficiary(orgId,rs.getInt(1) ,rs.getString(3) ));
				beneficiary.setLastTransaction(getLastTxnMadeByBeneficiary(orgId,rs.getInt(1) ,rs.getString(3) ));
				employeeBeneficiaryList.add(beneficiary);
			}
		} catch (Exception e) {
			logger.info("::: Exception in getting vendor Beneficiary Details ::::" + e);
			throw new ApplicationException(e.getMessage());
		}finally{
			closeResources(rs, preparedStatement, con);
		}
		return employeeBeneficiaryList;
	}

	
	public String getTotalPaidAmountByBeneficiary(int orgId, int beneficiaryId, String beneficiaryType)
			throws ApplicationException {
		String totalPaidAmnt = null;
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		if (orgId != 0 && beneficiaryId != 0 && beneficiaryType != null) {
			try {
				con = getBankingConnection();
				preparedStatement = con.prepareStatement(YesBankConstants.GET_TOTAL_AMNT_PAID_BY_BENEFICIARY);
				preparedStatement.setInt(1, orgId);
				preparedStatement.setInt(2, beneficiaryId);
				preparedStatement.setString(3, beneficiaryType);
				rs = preparedStatement.executeQuery();
				while (rs.next()) {
					totalPaidAmnt = rs.getString(1);
				}
			} catch (Exception e) {
				logger.info("::: Exception in getting vendor Beneficiary Details ::::" + e);
				throw new ApplicationException(e.getMessage());
			} finally {
				closeResources(rs, preparedStatement, con);
			}
		}
		return totalPaidAmnt;
	}

	public String getLastTxnMadeByBeneficiary(int orgId, int beneficiaryId, String beneficiaryType)
			throws ApplicationException {
		String lastTxn = null;
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		if (orgId != 0 && beneficiaryId != 0 && beneficiaryType != null) {
			try {
				con = getBankingConnection();
				preparedStatement = con.prepareStatement(YesBankConstants.GET_LAST_TXN_DONE_BY_BENEFICIARY);
				preparedStatement.setInt(1, orgId);
				preparedStatement.setInt(2, beneficiaryId);
				preparedStatement.setString(3, beneficiaryType);
				rs = preparedStatement.executeQuery();
				while (rs.next()) {
					lastTxn = rs.getString(1);
				}
			} catch (Exception e) {
				logger.info("::: Exception in getting vendor Beneficiary Details ::::" + e);
				throw new ApplicationException(e.getMessage());
			} finally {
				closeResources(rs, preparedStatement, con);
			}
		}
		return lastTxn;
	}

	public void disableBeneficiary(String id, String beneficiaryType, String status)
			throws ApplicationException {

		Connection con = null;
		PreparedStatement preparedStatement = null;
		String query;
		try {

			switch (beneficiaryType) {
			case YesBankConstants.OTHER_BENEFICIARY:
				con = getBankingConnection();
				query = YesBankConstants.DISABLE_BENEFICIARY;
				break;
			case YesBankConstants.BENEFICIARY_TYPE_VENDOR:
				con = getAccountsPayable();
				query = YesBankConstants.DISABLE_VENDOR_BENEFICIARY;
				break;
			case YesBankConstants.BENEFICIARY_TYPE_EMPLOYEE:
				con = getAccountsPayable();
				query = YesBankConstants.DISABLE_EMPLOYEE_BENEFICIARY;
				break;
			case YesBankConstants.BENEFICIARY_TYPE_CUSTOMER:
				con = getAccountsReceivableConnection();
				query = YesBankConstants.DISABLE_CUSTOMER_BENEFICIARY;
				break;
			case YesBankConstants.BENEFICIARY_TYPE_BANK_MASTER:
				con = getBankingConnection();
				query = YesBankConstants.DISABLE_BANK_MASTER_BENEFICIARY;
				break;
			default:
				throw new ApplicationException("No Beneficiary Details " + beneficiaryType);
			}

			preparedStatement = con.prepareStatement(query);
			preparedStatement.setString(1, status);
			preparedStatement.setDate(2, new Date(System.currentTimeMillis()));
			preparedStatement.setInt(3, Integer.parseInt(id));
			preparedStatement.executeUpdate();

		} catch (Exception e) {
			logger.info("::: Error in disable Beneficiary Details ::::" + e);
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(null, preparedStatement, con);
		}
	}

	public VendorBankDetailsVo getVendorBeneficiaryById(Integer beneficiaryId)
			throws ApplicationException {
		logger.info("::: get Beneficiary :::");
		VendorBankDetailsVo vendorBankDetailsVo =
				vendorDao.getVendorsDefaultBankDetailsByVendorId(beneficiaryId);
		vendorBankDetailsVo.setType(YesBankConstants.BENEFICIARY_TYPE_VENDOR);
		return vendorBankDetailsVo;
	}

	public VendorBankDetailsVo getCustomerBeneficiaryById(Integer beneficiaryId)
			throws ApplicationException {
		logger.info("::: get Beneficiary :::");
		CustomerBankDetailsVo customerBankDetailsVo =
				customerDao.getCustomerDefaultBankDetails(beneficiaryId);
		VendorBankDetailsVo bankDetails = new VendorBankDetailsVo();
		bankDetails.setAccountHolderName(customerBankDetailsVo.getAccountHolderName());
		bankDetails.setAccountNumber(customerBankDetailsVo.getAccountNumber());
		bankDetails.setBankName(customerBankDetailsVo.getBankName());
		bankDetails.setBranchName(customerBankDetailsVo.getBranchName());
		bankDetails.setConfirmAccountNumber(customerBankDetailsVo.getConfirmAccountNumber());
		bankDetails.setId(customerBankDetailsVo.getId());
		bankDetails.setIfscCode(customerBankDetailsVo.getIfscCode());
		bankDetails.setIsDefault(customerBankDetailsVo.getIsDefault());
		bankDetails.setStatus(customerBankDetailsVo.getStatus());
		bankDetails.setUpiId(customerBankDetailsVo.getUpiId());
		bankDetails.setType(YesBankConstants.BENEFICIARY_TYPE_CUSTOMER);

		return bankDetails;
	}

	public VendorBankDetailsVo getEmployeeBeneficiaryById(Integer beneficiaryId)
			throws ApplicationException {
		logger.info("::: get Beneficiary :::");
		VendorBankDetailsVo vendorBankDetailsVo =
				employeeDao.getEmployeeDefaultBankDetails(beneficiaryId);
		vendorBankDetailsVo.setType(YesBankConstants.BENEFICIARY_TYPE_EMPLOYEE);
		return vendorBankDetailsVo;
	}

	public YesBankBeneficiaryVo getBankingBeneficiaryById(Integer beneficiaryId)
			throws ApplicationException {
		logger.info("::: get Beneficiary :::");
		YesBankBeneficiaryVo beneficiary = null;
		Connection con=null;
		PreparedStatement preparedStatement=null;
		ResultSet rs =null;
		try {
			con = getBankingConnection();
			preparedStatement =con.prepareStatement(YesBankConstants.GET_BENEFICIARY_BY_ID);
			preparedStatement.setInt(1, beneficiaryId);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				beneficiary = new YesBankBeneficiaryVo();
				beneficiary.setId(beneficiaryId);
				beneficiary.setBeneficiaryType(rs.getString(1));
				beneficiary.setBeneficiaryName(rs.getString(2));
				beneficiary.setAccountNumber(rs.getString(3));
				beneficiary.setAccountType(rs.getString(4));
				beneficiary.setIfscCode(rs.getString(5));
				beneficiary.setOrganizationId(rs.getInt(6));
				beneficiary.setStatus(rs.getString(7));
				beneficiary.setUserId(rs.getString(8));
				beneficiary.setRoleName(rs.getString(9));
				beneficiary.setMobileNo(rs.getString(10));
				beneficiary.setEmail(rs.getString(11));
				beneficiary.setType(YesBankConstants.OTHER_BENEFICIARY);
			}
		} catch (Exception e) {
			logger.info("::: Error in get Beneficiary Details ::::" + e);
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return beneficiary;
	}

	private List<PaymentBeneficiaryListVo> getPaymentBeneficiaryDetailsResults(
			int orgId, Connection con, String query, String beneficiaryType) throws ApplicationException {
		List<PaymentBeneficiaryListVo> paymentBeneficiaryListVo = new ArrayList<>();
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {

			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, orgId);
			rs = preparedStatement.executeQuery();

			List<String> billPaymentReferenceList = new ArrayList<>();
			while (rs.next()) {
				PaymentBeneficiaryListVo beneficiary = new PaymentBeneficiaryListVo();
				beneficiary.setContactId(rs.getInt(1));
				beneficiary.setBankAccountName(rs.getString(2));
				beneficiary.setBeneficiaryType(beneficiaryType.equalsIgnoreCase(YesBankConstants.OTHER_BENEFICIARY)	? rs.getString(3): beneficiaryType);
				beneficiary.setType(beneficiaryType);
				beneficiary.setBeneficiaryName(beneficiaryType.equalsIgnoreCase(YesBankConstants.OTHER_BENEFICIARY)	? rs.getString(2): rs.getString(3));
				beneficiary.setStatus(rs.getString(4));
				beneficiary.setBankId(rs.getInt(5));
				beneficiary.setAccountNo(rs.getString(6));
				beneficiary.setIFSCCode(rs.getString(7));
				beneficiary.setMobileNo(rs.getString(8));
				beneficiary.setEmailId(rs.getString(9));

				PaymentBillReferenceVo paymentBillReferenceVo =null;

				if(!beneficiaryType.equalsIgnoreCase(YesBankConstants.OTHER_BENEFICIARY)){
					Connection con1 = getAccountsPayable();
					PreparedStatement preparedStatement1 = con1.prepareStatement(YesBankConstants.BILL_REFERENCE);
					preparedStatement1.setInt(1, beneficiary.getContactId());
					ResultSet rs1 = preparedStatement1.executeQuery();
					while (rs1.next()) {
						paymentBillReferenceVo = new PaymentBillReferenceVo();
						billPaymentReferenceList.add(rs.getString(1));
						paymentBillReferenceVo.setBillReference(billPaymentReferenceList);
					}
					closeResources(rs1, preparedStatement1, con1);

				}

				if(beneficiaryType.equalsIgnoreCase(YesBankConstants.BENEFICIARY_TYPE_VENDOR)){
					Connection con1 = getAccountsReceivableConnection();
					PreparedStatement preparedStatement1 = con1.prepareStatement(YesBankConstants.PAYMENT_REFERENCE);
					preparedStatement1.setInt(1, beneficiary.getContactId());
					ResultSet rs1 = preparedStatement1.executeQuery();
					while (rs1.next()) {
						paymentBillReferenceVo = new PaymentBillReferenceVo();
						billPaymentReferenceList.add(rs.getString(1));
						paymentBillReferenceVo.setPaymentReference(billPaymentReferenceList);
					}
					closeResources(rs1, preparedStatement1, con1);
				}
				beneficiary.setPaymentBillReference(paymentBillReferenceVo);
				paymentBeneficiaryListVo.add(beneficiary);
			}

		} catch (Exception e) {
			logger.info("::: Exception in getting vendor Beneficiary Details ::::" + e);
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, null);
		}
		return paymentBeneficiaryListVo;
	}

	/*private PaymentBillReferenceVo getBillAndPaymentReference(int contactId,String beneficiaryType) throws ApplicationException {

    PreparedStatement preparedStatement = null;
    ResultSet rs = null;
    Connection con =null;
    PaymentBillReferenceVo paymentBillReferenceVo =null;
    try{


      List<String> billPaymentReferenceList = new ArrayList<>();
      if(!beneficiaryType.equalsIgnoreCase(YesBankConstants.OTHER_BENEFICIARY)){
        con = getAccountsPayable();
        preparedStatement = con.prepareStatement(YesBankConstants.BILL_REFERENCE);
        preparedStatement.setInt(1, contactId);
        rs = preparedStatement.executeQuery();
        while (rs.next()) {
          paymentBillReferenceVo = new PaymentBillReferenceVo();
          billPaymentReferenceList.add(rs.getString(1));
          paymentBillReferenceVo.setBillReference(billPaymentReferenceList);
        }

      }

      if(beneficiaryType.equalsIgnoreCase(YesBankConstants.BENEFICIARY_TYPE_VENDOR)){

        con = getAccountsReceivableConnection();
        preparedStatement = con.prepareStatement(YesBankConstants.PAYMENT_REFERENCE);
        preparedStatement.setInt(1, contactId);
        rs = preparedStatement.executeQuery();
        while (rs.next()) {
          paymentBillReferenceVo = new PaymentBillReferenceVo();
          billPaymentReferenceList.add(rs.getString(1));
          paymentBillReferenceVo.setPaymentReference(billPaymentReferenceList);
        }

      }

    } catch (Exception ex) {
      logger.error("Exception in payment bill reference");
    } finally{
      closeResources(rs,preparedStatement,con);
    }

    return paymentBillReferenceVo;
  }*/
}

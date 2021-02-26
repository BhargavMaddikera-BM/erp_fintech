package com.blackstrawai.externalintegration.banking.yesbank;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.blackstrawai.ApplicationException;
import com.blackstrawai.ap.BillsInvoiceDao;
import com.blackstrawai.ap.PaymentNonCoreConstants;
import com.blackstrawai.ap.PurchaseOrderDao;
import com.blackstrawai.ap.dropdowns.PaymentNonCoreBillDetailsDropDownVo;
<<<<<<< HEAD
import com.blackstrawai.ap.dropdowns.PaymentTypeVo;
=======
>>>>>>> 0a694d861a9a446c4b7c10a39cf5c7647a3014bd
import com.blackstrawai.ap.dropdowns.PoDetailsDropDownVo;
import com.blackstrawai.common.BaseDao;
import com.blackstrawai.externalintegration.yesbank.Request.paymenttransfer.PaymentTransferVo;
import com.blackstrawai.externalintegration.yesbank.Response.payments.ErpSinglePaymentTransactionVo;
import com.blackstrawai.externalintegration.yesbank.dropdowns.APEmployeesPayRunDropDownVo;
import com.blackstrawai.externalintegration.yesbank.dropdowns.APVendorBillsDropDownVo;
import com.blackstrawai.externalintegration.yesbank.dropdowns.PaymentBeneficiaryListVo;
import com.blackstrawai.externalintegration.yesbank.erpbulkpayments.BaseERPPaymentVo;
import com.blackstrawai.externalintegration.yesbank.erpbulkpayments.ERPPaymentDetailVo;
import com.blackstrawai.keycontact.EmployeeDao;
import com.blackstrawai.keycontact.VendorDao;
import com.blackstrawai.keycontact.vendor.VendorVo;
import com.blackstrawai.onboarding.OrganizationDao;
import com.blackstrawai.payroll.PayRunDao;

@Repository
public class ERPBulkPaymentsDao extends BaseDao{

	@Autowired
	private VendorDao vendorDao;

	@Autowired
	private BillsInvoiceDao billsInvoiceDao;

	@Autowired
	private OrganizationDao organizationDao;

	@Autowired
	private PurchaseOrderDao purchaseOrderDao;
<<<<<<< HEAD
	
	@Autowired
	private EmployeeDao employeeDao;
	
	@Autowired
	private PayRunDao payrunDao;
=======

>>>>>>> 0a694d861a9a446c4b7c10a39cf5c7647a3014bd
	
	private final Logger logger = Logger.getLogger(ERPBulkPaymentsDao.class);

	public List<APVendorBillsDropDownVo> getVendorsBillsAndBankDetails(int orgId, String roleName, String userId ,String currencyId) throws ApplicationException{
		List<APVendorBillsDropDownVo> vendorDetails = new ArrayList<APVendorBillsDropDownVo>();
		try {
			//	Integer currencyId = currencyDao.getCurrencyId("Indian Rupee", orgId);
			List<VendorVo> vendorList = vendorDao.getAllVendorsOfAnOrganizationForUserAndRole(orgId, userId,roleName);
			if(currencyId!=null && vendorList!=null && !vendorList.isEmpty()) {
				Connection con = getAccountsPayable();
				for(VendorVo vendor : vendorList) {
					if(vendor!=null && vendor.getVendorGeneralInformation()!=null && vendor.getVendorGeneralInformation().getId()!=null ) {
						APVendorBillsDropDownVo vendorBillDetails = new APVendorBillsDropDownVo();
						vendorBillDetails.setVendorId(vendor.getVendorGeneralInformation().getId());
						vendorBillDetails.setVendorName(vendor.getVendorGeneralInformation().getVendorDisplayName());
						vendorBillDetails.setOrgId(orgId);
						vendorBillDetails.setUserId(userId);
						vendorBillDetails.setBills(billsInvoiceDao.getBillReferenceNumberByOrgVendor(orgId, vendor.getVendorGeneralInformation().getId(), Integer.valueOf(currencyId), 0));
						vendorBillDetails.setBankdetails(vendorDao.getVendorsBankDetailsByVendorId(con, vendor.getVendorGeneralInformation().getId()));
						vendorDetails.add(vendorBillDetails);
					}
				}
				closeResources(null, null, con);
				if(vendorDetails!=null && !vendorDetails.isEmpty())
				{

					for(APVendorBillsDropDownVo vendor:vendorDetails)
					{
						if(vendor.getBills()!=null && !vendor.getBills().isEmpty())
						{
							for(PaymentNonCoreBillDetailsDropDownVo invoice:vendor.getBills()) {
								if(vendor.getVendorId()!=null && invoice.getId()!=null)
								{
									Boolean isPaymentInitiated = checkPaymentInitiated(orgId,vendor.getVendorId(),YesBankConstants.KEYCONTACT_VENDOR,invoice.getId(),YesBankConstants.MODULE_INVOICE_PAYMENT);	
									invoice.setIsPaymentInitiated(isPaymentInitiated);
								}
							}
						}
						con = getUserMgmConnection();
						vendor.setPaymentRefNoVoucher(organizationDao.getBasicVoucherEntries(orgId, con,	PaymentNonCoreConstants.PAYMENTS));
						closeResources(null, null, con);
					}
				}


			}
			logger.info(vendorDetails);
		} catch (ApplicationException e) {
			logger.info("Error in getVendorsBillsAndBankDetails "+ e);
			throw new ApplicationException(e);
		}

		return vendorDetails;
	}


	public List<PaymentBeneficiaryListVo> getERPBankingAccountDetails(
			int orgId) throws ApplicationException {
		Connection con = null;
		List<PaymentBeneficiaryListVo> paymentBeneficiaryListVo = new ArrayList<>();
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			con = getBankingConnection();
			preparedStatement = con.prepareStatement(YesBankConstants.GET_PAYMENT_ORG_BANK_ACCOUNT);
			preparedStatement.setInt(1, orgId);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				PaymentBeneficiaryListVo beneficiary = new PaymentBeneficiaryListVo();
				beneficiary.setContactId(rs.getInt(1));
				beneficiary.setBankAccountName(rs.getString(3));
				beneficiary.setBeneficiaryType(YesBankConstants.SELF_BENEFICIARY);
				beneficiary.setType("Bank Account");
				beneficiary.setBeneficiaryName( rs.getString(2)!=null	? rs.getString(2): rs.getString(3));
				beneficiary.setStatus(rs.getString(4));
				beneficiary.setBankId(rs.getInt(5));
				beneficiary.setAccountNo(rs.getString(6));
				beneficiary.setIFSCCode(rs.getString(7));
				beneficiary.setMobileNo(rs.getString(8));
				beneficiary.setEmailId(rs.getString(9));
				paymentBeneficiaryListVo.add(beneficiary);
			}

		} catch (Exception e) {
			logger.info("::: Exception in  getERPBankingAccountDetails Details ::::" + e);
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return paymentBeneficiaryListVo;
	}

	public void insertIntoErpBulkPayment(ERPPaymentDetailVo paymentDetail ,BaseERPPaymentVo erpPaymentVo ) throws ApplicationException{
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			con = getBankingConnection();
			preparedStatement = con.prepareStatement(YesBankConstants.INSERT_INTO_ERP_PAYMENTS);
			preparedStatement.setInt(1, paymentDetail.getKeyContactId());
			preparedStatement.setString(2, paymentDetail.getKeyContactType());
			preparedStatement.setInt(3, paymentDetail.getReferenceModuleId());
			preparedStatement.setString(4,erpPaymentVo.getModuleName());
			preparedStatement.setString(5,paymentDetail.getKeyContactAccountNo());
			preparedStatement.setString(6,paymentDetail.getKeyContactIfsc());
			preparedStatement.setString(7,paymentDetail.getPaymentMode());
			preparedStatement.setString(8,paymentDetail.getAmount());
			preparedStatement.setString(9,paymentDetail.getAmountDescription());
			preparedStatement.setDate(10, erpPaymentVo.getTransactionDate());
			preparedStatement.setInt(11, erpPaymentVo.getOrgId());
			preparedStatement.setDate(12, new Date (System.currentTimeMillis()));
			preparedStatement.setString(13, erpPaymentVo.getUserId());
			preparedStatement.setString(14, erpPaymentVo.getRoleName());
			preparedStatement.setString(15, erpPaymentVo.getFileIdentifier());
			preparedStatement.setString(16, paymentDetail.getUniqueIdentifier());

			preparedStatement.executeUpdate();
		} catch (Exception e) {
			logger.info("::: Exception in  insertIntoErpBulkPayment Details ::::" + e);
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, con);
		}
	}

	public Boolean checkPaymentInitiated(int orgId,int keyContactId,String keyContactType,int moduleId,String moduleType)throws ApplicationException
	{
		Connection con = null;
		PreparedStatement preparedStatement = null;
		Boolean isPaymentInitiated =false;
		ResultSet rs = null;
		try {
			con = getBankingConnection();
			preparedStatement = con.prepareStatement(YesBankConstants.CHECK_PAYMENT_INITIATED);
			preparedStatement.setInt(1, orgId);
			preparedStatement.setInt(2, keyContactId);
			preparedStatement.setString(3, keyContactType);
			preparedStatement.setString(4, moduleType);
			preparedStatement.setInt(5,moduleId);
			rs=preparedStatement.executeQuery();
			while (rs.next()) {

				isPaymentInitiated=true;
			}
		} catch (Exception e) {
			logger.info("::: Exception in  checkPaymentInitiated Details ::::" + e);
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return isPaymentInitiated;
	}

	public List<APVendorBillsDropDownVo> getPurchaseOrderAndBankDetails(int orgId, String roleName, String userId ,String currencyId) throws ApplicationException{
		List<APVendorBillsDropDownVo> vendorDetails = new ArrayList<APVendorBillsDropDownVo>();
		try {
			//	Integer currencyId = currencyDao.getCurrencyId("Indian Rupee", orgId);
			List<VendorVo> vendorList = vendorDao.getAllVendorsOfAnOrganizationForUserAndRole(orgId, userId,roleName);
			if(currencyId!=null && vendorList!=null && !vendorList.isEmpty()) {
				Connection con = getAccountsPayable();
				for(VendorVo vendor : vendorList) {

					if(vendor!=null && vendor.getVendorGeneralInformation()!=null && vendor.getVendorGeneralInformation().getId()!=null ) {
						APVendorBillsDropDownVo vendorBillDetails = new APVendorBillsDropDownVo();
						vendorBillDetails.setVendorId(vendor.getVendorGeneralInformation().getId());
						vendorBillDetails.setVendorName(vendor.getVendorGeneralInformation().getVendorDisplayName());
						vendorBillDetails.setOrgId(orgId);
						vendorBillDetails.setUserId(userId);
						vendorBillDetails.setPurchaseOrders(purchaseOrderDao.getPurchaseBillReferenceNumberByOrgVendor(orgId, vendor.getVendorGeneralInformation().getId(), Integer.valueOf(currencyId), 0));
						vendorBillDetails.setBankdetails(vendorDao.getVendorsBankDetailsByVendorId(con, vendor.getVendorGeneralInformation().getId()));
						vendorDetails.add(vendorBillDetails);
					}
				}
				closeResources(null, null, con);
				if(vendorDetails!=null && !vendorDetails.isEmpty())
				{

					for(APVendorBillsDropDownVo vendor:vendorDetails)
					{
						if(vendor.getPurchaseOrders()!=null && !vendor.getPurchaseOrders().isEmpty())
						{
							for(PoDetailsDropDownVo po:vendor.getPurchaseOrders()) {
								if(vendor.getVendorId()!=null && po.getId()!=null)
								{
									Boolean isPaymentInitiated = checkPaymentInitiated(orgId,vendor.getVendorId(),YesBankConstants.KEYCONTACT_VENDOR,po.getId(),YesBankConstants.MODULE_PO_PAYMENT);	
									po.setIsPaymentInitiated(isPaymentInitiated);
								}
							}
						}

						con = getUserMgmConnection();
						vendor.setPaymentRefNoVoucher(organizationDao.getBasicVoucherEntries(orgId, con,PaymentNonCoreConstants.PAYMENTS));
						closeResources(null, null, con);
					}

				}
					
			}
			logger.info(vendorDetails);
		} catch (ApplicationException e) {
			logger.info("Error in getPurchaseBillsAndBankDetails "+ e);
			throw new ApplicationException(e);
		}

		return vendorDetails;
	}

<<<<<<< HEAD

	public List<APEmployeesPayRunDropDownVo> getEmployeePayRunAndBankDetails(Integer orgId, String roleName,
			String userId, String currencyId) throws ApplicationException {

		List<APEmployeesPayRunDropDownVo> employeeDetails = new ArrayList<APEmployeesPayRunDropDownVo>();
		try {
			//	Integer currencyId = currencyDao.getCurrencyId("Indian Rupee", orgId);
			List<PaymentTypeVo> employeeList = employeeDao.getAllActiveEmployees(orgId);
			logger.info("employeeList:"+employeeList.size());
			if(currencyId!=null && employeeList!=null && !employeeList.isEmpty()) {
				Connection con = getAccountsPayable();
				for(PaymentTypeVo employee : employeeList) {
					if(employee!=null && employee.getId()!=0 ) {
						APEmployeesPayRunDropDownVo employeeDetail = new APEmployeesPayRunDropDownVo();
						employeeDetail.setEmployeeId(employee.getId());
						employeeDetail.setEmployeeName(employee.getName());
						employeeDetail.setOrgId(orgId);
						employeeDetail.setUserId(userId);
						employeeDetail.setBankdetails(employeeDao.getEmployeeBankDetails( con, employee.getId()));
						employeeDetail.setPayruns(payrunDao.getPayRunDataByOrganizationEmployeeId(orgId, employee.getId()));
						employeeDetails.add(employeeDetail);
					}
				}
				closeResources(null, null, con);
				/*if(employeeDetails!=null && !employeeDetails.isEmpty())
				{

					for(APEmployeesPayRunDropDownVo emp:employeeDetails)
					{
						if(vendor.getBills()!=null && !vendor.getBills().isEmpty())
						{
							for(PaymentNonCoreBillDetailsDropDownVo invoice:vendor.getBills()) {
								if(vendor.getVendorId()!=null && invoice.getId()!=null)
								{
									Boolean isPaymentInitiated = checkPaymentInitiated(orgId,vendor.getVendorId(),YesBankConstants.KEYCONTACT_VENDOR,invoice.getId(),YesBankConstants.MODULE_INVOICE_PAYMENT);	
									invoice.setIsPaymentInitiated(isPaymentInitiated);
								}
							}
						}
						con = getUserMgmConnection();
						vendor.setPaymentRefNoVoucher(organizationDao.getBasicVoucherEntries(orgId, con,	PaymentNonCoreConstants.PAYMENTS));
						closeResources(null, null, con);
					}
				}*/


			}
			logger.info(employeeDetails);
		} catch (ApplicationException e) {
			logger.info("Error in getVendorsBillsAndBankDetails "+ e);
			throw new ApplicationException(e);
		}

		return employeeDetails;
	
	}
	
	public void insertIntoErpSinglePayment(PaymentTransferVo paymentTransferPaymentsVo,
			ErpSinglePaymentTransactionVo paymentTransactionVo) throws ApplicationException {
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			con = getBankingConnection();
			
			preparedStatement = con.prepareStatement(YesBankConstants.INSERT_INTO_ERP_PAYMENTS);
		    preparedStatement.setString(1, paymentTransactionVo.getSinglePayment().getBeneficiaryId());
		    preparedStatement.setString(2, paymentTransactionVo.getSinglePayment().getBeneficiaryType()!=null?paymentTransactionVo.getSinglePayment().getBeneficiaryType().toLowerCase():null);
			preparedStatement.setInt(3, paymentTransactionVo.getSinglePayment().getPaymentRefId());
			preparedStatement.setString(4,YesBankConstants.MODULE_INVOICE_PAYMENT);
			preparedStatement.setString(5,paymentTransactionVo.getSinglePayment().getBeneficiaryAccountNumber());
			preparedStatement.setString(6,paymentTransactionVo.getSinglePayment().getIfscCode());
			preparedStatement.setString(7,paymentTransactionVo.getSinglePayment().getPaymentMode());
			preparedStatement.setString(8,paymentTransactionVo.getSinglePayment().getAmount());
			preparedStatement.setString(9,paymentTransactionVo.getSinglePayment().getDescription());
			preparedStatement.setDate(10, new Date(System.currentTimeMillis()));
			preparedStatement.setInt(11,Integer.valueOf(paymentTransactionVo.getOrgId())); 
			preparedStatement.setDate(12, new Date(System.currentTimeMillis()));
		    preparedStatement.setString(13, paymentTransactionVo.getUserId());
			preparedStatement.setString(14, paymentTransactionVo.getRoleName());
			preparedStatement.setString(15,null); 
			preparedStatement.setString(16,paymentTransferPaymentsVo.getData().getInitiation().getInstructionIdentification());
			   
			preparedStatement.executeUpdate();
		} catch (Exception e) {
			logger.info("::: Exception in  insertIntoErpSinglePayment Details ::::" + e);
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, con);
		}
	}


=======
>>>>>>> 0a694d861a9a446c4b7c10a39cf5c7647a3014bd
}

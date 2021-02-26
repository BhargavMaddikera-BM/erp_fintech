package com.blackstrawai.keycontact;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.ap.dropdowns.BasicEmployeeVo;
import com.blackstrawai.ap.dropdowns.PaymentTypeVo;
import com.blackstrawai.attachments.AttachmentsConstants;
import com.blackstrawai.attachments.AttachmentsDao;
import com.blackstrawai.attachments.AttachmentsVo;
import com.blackstrawai.attachments.UploadFileVo;
import com.blackstrawai.chartofaccounts.ChartOfAccountsDao;
import com.blackstrawai.common.BaseDao;
import com.blackstrawai.common.BookKeepingSettingsVo;
import com.blackstrawai.common.CommonConstants;
import com.blackstrawai.common.FinanceCommonDao;
import com.blackstrawai.export.BankExportVo;
import com.blackstrawai.export.EmployeeExportVo;
import com.blackstrawai.export.ExportVo;
import com.blackstrawai.keycontact.employee.EmployeeBasicDetailsVo;
import com.blackstrawai.keycontact.employee.EmployeeGeneralInfoVo;
import com.blackstrawai.keycontact.employee.EmployeeVo;
import com.blackstrawai.keycontact.vendor.VendorBankDetailsVo;
import com.blackstrawai.settings.DepartmentDao;
import com.blackstrawai.settings.SettingsAndPreferencesConstants;

@Repository
public class EmployeeDao extends BaseDao {

	@Autowired
	private AttachmentsDao attachmentsDao;

	@Autowired
	private DepartmentDao departmentDao;

	@Autowired
	private FinanceCommonDao financeCommonDao;

	@Autowired
	private ChartOfAccountsDao chartOfAccountsDao;

	private Logger logger = Logger.getLogger(EmployeeDao.class);

	public Boolean createEmployee(EmployeeVo employeeVo) throws ApplicationException {
		logger.info("Entry into method: createEmployee");
		boolean isCreatedSuccessfully = false;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = getAccountsPayable();
			con.setAutoCommit(false);
			boolean isEmployeeExist = checkEmployeeExist(employeeVo.getEmployeeGeneralInfo().getEmployeeId(),
					employeeVo.getOrganizationId());
			if (isEmployeeExist) {
				throw new ApplicationException("Employee Exist for the Organization");
			}
			if (employeeVo.getEmployeeGeneralInfo() != null) {
				employeeVo = createEmployeeGeneralInfo(employeeVo, con);
			}
			if (employeeVo.getBankDetails() != null) {
				createEmployeeBankDetails(employeeVo.getId(), employeeVo.getBankDetails(), con);
			}
			if (employeeVo.getAttachments() != null && employeeVo.getAttachments().size() > 0) {
				attachmentsDao.createAttachments(employeeVo.getOrganizationId(),employeeVo.getUserId(),employeeVo.getAttachments(), AttachmentsConstants.MODULE_TYPE_EMPLOYEE,
						employeeVo.getId(),employeeVo.getRoleName());
			}
			con.commit();
			isCreatedSuccessfully = true;
		} catch (ApplicationException e1) {
			throw e1;
		} catch (Exception e) {
			logger.info("Error in createEmployee:: ", e);
			try {
				con.rollback();
			} catch (Exception e1) {
				throw new ApplicationException(e1);
			}
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return isCreatedSuccessfully;
	}

	private EmployeeVo createEmployeeGeneralInfo(EmployeeVo employeeVo, Connection con) throws ApplicationException {
		logger.info("Entry into method: createEmployeeGeneralInfo");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		EmployeeGeneralInfoVo employeeGeneralInfo = employeeVo.getEmployeeGeneralInfo();
		
		if(employeeVo.getStatus()!=null && employeeVo.getStatus().equals("ACTIVE")){
			employeeVo.setStatus("ACT");
		}
		
		if(employeeVo.getBookKeepingSettings()==null){
			BookKeepingSettingsVo bookKeepingSettings=new BookKeepingSettingsVo();
			bookKeepingSettings.setDefaultGlName("Payables to employees");
			bookKeepingSettings.setId(chartOfAccountsDao.getLedgerIdGivenName("Payables to employees", employeeVo.getOrganizationId()));
			employeeVo.setBookKeepingSettings(bookKeepingSettings);
		}
		
		try {
			String sql = EmployeeConstants.INSERT_INTO_EMPLOYEE;
			preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setString(1,
					employeeGeneralInfo.getEmployeeId() != null ? employeeGeneralInfo.getEmployeeId() : null);
			preparedStatement.setString(2,
					employeeGeneralInfo.getFirstName() != null ? employeeGeneralInfo.getFirstName() : null);
			preparedStatement.setString(3,
					employeeGeneralInfo.getLastName() != null ? employeeGeneralInfo.getLastName() : null);
			preparedStatement.setString(4,
					employeeGeneralInfo.getMobileNo() != null ? employeeGeneralInfo.getMobileNo() : null);
			preparedStatement.setObject(5,
					employeeGeneralInfo.getReportingTo() != null ? employeeGeneralInfo.getReportingTo() : null);
			preparedStatement.setObject(6,
					employeeVo.getOrganizationId() != null ? employeeVo.getOrganizationId() : null);
			preparedStatement.setString(8,
					employeeGeneralInfo.getDateOfJoining() != null ? employeeGeneralInfo.getDateOfJoining() : null);
			preparedStatement.setObject(9,
					employeeGeneralInfo.getDepartment() != null ? employeeGeneralInfo.getDepartment() : null);
			preparedStatement.setObject(10,
					employeeGeneralInfo.getEmployeeType() != null ? employeeGeneralInfo.getEmployeeType() : null);
			preparedStatement.setString(11,
					employeeGeneralInfo.getEmployeeStatus() != null ? employeeGeneralInfo.getEmployeeStatus() : null);
			preparedStatement.setInt(7, Integer.valueOf(employeeVo.getUserId()));
			preparedStatement.setString(12,
					employeeGeneralInfo.getEmail() != null ? employeeGeneralInfo.getEmail() : null);
			preparedStatement.setObject(13, employeeVo.getIsSuperAdmin() != null ? employeeVo.getIsSuperAdmin() : null);
			preparedStatement.setString(14, employeeVo.getRoleName());
			preparedStatement.setString(15, employeeVo.getStatus());
			preparedStatement.setInt(16, employeeVo.getBookKeepingSettings().getId());
			preparedStatement.setString(17, employeeVo.getBookKeepingSettings().getDefaultGlName());
			int rowAffected = preparedStatement.executeUpdate();
			if (rowAffected == 1) {
				rs = preparedStatement.getGeneratedKeys();
				if (rs.next()) {
					employeeVo.setId(rs.getInt(1));
				}
			}
			employeeVo.setEmployeeGeneralInfo(employeeGeneralInfo);
		} catch (Exception e) {
			throw new ApplicationException(e);

		} finally {
			closeResources(rs, preparedStatement, null);
		}
		return employeeVo;
	}

	/*public void insertIntoChartOfAccountsLevel6(int organizationId, String name, String userId, boolean isSuperAdmin,
			String displayName) throws ApplicationException {

		int level5Id = chartOfAccountsDao.getChartOfAccountsLevel5Id(organizationId, "Short-term loans and advances",
				"Loans and advances to employees", "Loans and advances to employees");
		if (level5Id > 0) {
			chartOfAccountsDao.createLevel6(name, "Employee", organizationId, userId, isSuperAdmin, level5Id, false,
					displayName);
		}

		level5Id = chartOfAccountsDao.getChartOfAccountsLevel5Id(organizationId, "Long-term loans and advances",
				"Loans and advances to employees", "NC - Advance to Employee");
		if (level5Id > 0) {
			chartOfAccountsDao.createLevel6(name, "Employee", organizationId, userId, isSuperAdmin, level5Id, false,
					displayName);
		}

		level5Id = chartOfAccountsDao.getChartOfAccountsLevel5Id(organizationId, "Long-term provisions",
				"Provision - Others", "NC - Provision for advance to employees");
		if (level5Id > 0) {
			chartOfAccountsDao.createLevel6(name, "Employee", organizationId, userId, isSuperAdmin, level5Id, false,
					displayName);
		}

		level5Id = chartOfAccountsDao.getChartOfAccountsLevel5Id(organizationId, "Trade payables", "Acceptances",
				"Payables to employees");
		if (level5Id > 0) {
			chartOfAccountsDao.createLevel6(name, "Employee", organizationId, userId, isSuperAdmin, level5Id, false,
					displayName);
		}

		level5Id = chartOfAccountsDao.getChartOfAccountsLevel5Id(organizationId, "Short-term provisions",
				"Provision - Others", "Provision for advance to employees");
		if (level5Id > 0) {
			chartOfAccountsDao.createLevel6(name, "Employee", organizationId, userId, isSuperAdmin, level5Id, false,
					displayName);
		}

	}*/

	private void createEmployeeBankDetails(Integer id, List<VendorBankDetailsVo> employeebankdetailsVo, Connection con)
			throws ApplicationException {
		logger.info("Entry into method: createBankDetails");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			String sql = EmployeeConstants.INSERT_INTO_EMPLOYEE_BANK_DETAILS;
			for (int i = 0; i < employeebankdetailsVo.size(); i++) {
				VendorBankDetailsVo vendorBankDetailsVo = employeebankdetailsVo.get(i);
				preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				preparedStatement.setString(1,
						employeebankdetailsVo.get(i).getBankName() != null ? employeebankdetailsVo.get(i).getBankName()
								: null);
				preparedStatement.setString(2,
						employeebankdetailsVo.get(i).getAccountNumber() != null
						? employeebankdetailsVo.get(i).getAccountNumber()
								: null);
				preparedStatement.setString(3,
						employeebankdetailsVo.get(i).getAccountHolderName() != null
						? employeebankdetailsVo.get(i).getAccountHolderName()
								: null);
				preparedStatement.setString(4,
						employeebankdetailsVo.get(i).getBranchName() != null
						? employeebankdetailsVo.get(i).getBranchName()
								: null);
				preparedStatement.setString(5,
						employeebankdetailsVo.get(i).getIfscCode() != null ? employeebankdetailsVo.get(i).getIfscCode()
								: null);
				preparedStatement.setString(6,
						employeebankdetailsVo.get(i).getUpiId() != null ? employeebankdetailsVo.get(i).getUpiId()
								: null);
				preparedStatement.setObject(7,
						employeebankdetailsVo.get(i).getIsDefault() != null
						? employeebankdetailsVo.get(i).getIsDefault()
								: null);
				preparedStatement.setInt(8, id);
				int rowAffected = preparedStatement.executeUpdate();
				if (rowAffected == 1) {
					rs = preparedStatement.getGeneratedKeys();
					if (rs.next()) {
						vendorBankDetailsVo.setId(rs.getInt(1));
					}
				}
			}
		} catch (Exception e) {
			throw new ApplicationException(e);

		} finally {
			closeResources(rs, preparedStatement, null);
		}
	}

	private boolean checkEmployeeExist(String employeeId, Integer organizationId) throws ApplicationException {
		logger.info("Entry into method: checkEmployeeExist");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = getAccountsPayable();
			String query = EmployeeConstants.CHECK_EMPLOYEE_EXIST_FOR_ORGANIZATION;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, organizationId);
			preparedStatement.setString(2, employeeId);
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

	/*public List<EmployeeBasicDetailsVo> getAllEmployeesOfAnOrganization(int OrganizationId)
			throws ApplicationException {
		logger.info("Entry into method: getAllEmployeesOfAnOrganization");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		List<EmployeeBasicDetailsVo> listOfEmployees = new ArrayList<EmployeeBasicDetailsVo>();
		try {
			con = getAccountsPayable();
			String query = EmployeeConstants.GET_EMPLOYEE_BY_ORGANIZATION;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, OrganizationId);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				EmployeeBasicDetailsVo employeeBasicDetailsVo = new EmployeeBasicDetailsVo();
				employeeBasicDetailsVo.setEmployeeId(rs.getString(1));
				employeeBasicDetailsVo.setFirstName(rs.getString(2));
				employeeBasicDetailsVo.setEmail(rs.getString(3));
				Integer departmentId = rs.getInt(4);
				if (departmentId != null) {
					employeeBasicDetailsVo.setDepartment(departmentDao.getDepartmentById(departmentId));
				}
				employeeBasicDetailsVo.setStatus(rs.getString(5));
				employeeBasicDetailsVo.setId(rs.getInt(6));
				Integer employeeTypeId = rs.getInt(7);
				if (employeeTypeId != null) {
					employeeBasicDetailsVo.setEmployeeType(financeCommonDao.getEmployeeTypeById(employeeTypeId));
				}
				listOfEmployees.add(employeeBasicDetailsVo);
			}
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return listOfEmployees;
	}
	 */


	public List<EmployeeBasicDetailsVo> getAllEmployeesOfAnOrganizationForUserAndRole(int organizationId,String userId,String roleName)
			throws ApplicationException {
		logger.info("Entry into method: getAllEmployeesOfAnOrganizationForUserAndRole");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		List<EmployeeBasicDetailsVo> listOfEmployees = new ArrayList<EmployeeBasicDetailsVo>();
		try {
			con = getAccountsPayable();
			String query ="";
			if(roleName.equals("Super Admin")){
				query = EmployeeConstants.GET_EMPLOYEE_ORGANIZATION;
			}else{
				query = EmployeeConstants.GET_EMPLOYEE_BY_ORGANIZATION_USER_ROLE;
			}		

			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, organizationId);
			if(!(roleName.equals("Super Admin"))){
				preparedStatement.setString(2, userId);
				preparedStatement.setString(3, roleName);
			}	
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				EmployeeBasicDetailsVo employeeBasicDetailsVo = new EmployeeBasicDetailsVo();
				employeeBasicDetailsVo.setEmployeeId(rs.getString(1));
				employeeBasicDetailsVo.setFirstName(rs.getString(2));
				employeeBasicDetailsVo.setEmail(rs.getString(3));
				Integer departmentId = rs.getInt(4);
				if (departmentId != null) {
					employeeBasicDetailsVo.setDepartment(departmentDao.getDepartmentById(departmentId));
				}
				employeeBasicDetailsVo.setStatus(rs.getString(5));
				employeeBasicDetailsVo.setId(rs.getInt(6));
				Integer employeeTypeId = rs.getInt(7);
				if (employeeTypeId != null) {
					employeeBasicDetailsVo.setEmployeeType(financeCommonDao.getEmployeeTypeById(employeeTypeId));
				}
				listOfEmployees.add(employeeBasicDetailsVo);
			}
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return listOfEmployees;
	}



	public EmployeeVo getEmployeeById(Integer id) throws ApplicationException {
		logger.info("Entry into method: getEmployeeById");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		EmployeeVo employeeVo = new EmployeeVo();
		try {
			con = getAccountsPayable();
			if (id != null) {
				employeeVo = getEmployeeGeneralInfo(id, con);
				if (employeeVo.getId() != null) {
					List<VendorBankDetailsVo> bankList = getEmployeeBankDetails( con, id);
					employeeVo.setBankDetails(bankList);
					List<UploadFileVo> uploadFileVos = new ArrayList<UploadFileVo>();
					for (AttachmentsVo attachments : attachmentsDao.getAttachmentDetails(id,
							AttachmentsConstants.MODULE_TYPE_EMPLOYEE)) {
						UploadFileVo uploadFileVo = new UploadFileVo();
						uploadFileVo.setId(attachments.getId());
						uploadFileVo.setName(attachments.getFileName());
						uploadFileVo.setSize(attachments.getSize());
						uploadFileVos.add(uploadFileVo);
					}
					employeeVo.setAttachments(uploadFileVos);
				}
			}
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return employeeVo;
	}

	private EmployeeVo getEmployeeGeneralInfo(Integer id, Connection con) throws ApplicationException {
		logger.info("Entry into method: getEmployeeGeneralInfo");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		EmployeeGeneralInfoVo employeeGeneralInfoVo = new EmployeeGeneralInfoVo();
		EmployeeVo employeeVo = new EmployeeVo();
		try {
			String query = EmployeeConstants.GET_EMPLOYEE_BY_ID;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, id);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				employeeVo.setId(rs.getInt(1));
				employeeGeneralInfoVo.setEmployeeId(rs.getString(2));
				employeeGeneralInfoVo.setFirstName(rs.getString(3));
				employeeGeneralInfoVo.setLastName(rs.getString(4));
				employeeGeneralInfoVo.setEmail(rs.getString(5));
				employeeGeneralInfoVo.setDateOfJoining(rs.getString(6));
				employeeGeneralInfoVo.setMobileNo(rs.getString(7));
				employeeGeneralInfoVo.setDepartment(rs.getInt(8));
				employeeGeneralInfoVo.setReportingTo(rs.getInt(9));
				employeeGeneralInfoVo.setEmployeeStatus(rs.getString(10));
				employeeGeneralInfoVo.setEmployeeType(rs.getInt(11));
				employeeVo.setStatus(rs.getString(12));
				employeeVo.setUpdateTs(rs.getTimestamp(13));
				employeeVo.setUserId(rs.getString(14));
				employeeVo.setOrganizationId(rs.getInt(15));
				employeeVo.setIsSuperAdmin(rs.getBoolean(16));
				BookKeepingSettingsVo bookKeepingSettingsVo=new BookKeepingSettingsVo();
				bookKeepingSettingsVo.setId(rs.getInt(17));
				bookKeepingSettingsVo.setDefaultGlName(rs.getString(18));
				employeeVo.setBookKeepingSettings(bookKeepingSettingsVo);
				employeeVo.setEmployeeGeneralInfo(employeeGeneralInfoVo);
			}
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, null);
		}
		return employeeVo;
	}

	public List<VendorBankDetailsVo> getEmployeeBankDetails(Connection con, int id)
			throws ApplicationException {
		logger.info("Entry into method: getEmployeeBankDetails");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		List<VendorBankDetailsVo> vendorBankDetailsList = new ArrayList<VendorBankDetailsVo>();
		try {
			String query = EmployeeConstants.GET_EMPLOYEE_BANK_DETAILS_BY_ID;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, id);
			preparedStatement.setString(2, CommonConstants.STATUS_AS_DELETE);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				VendorBankDetailsVo vendorBankDetails = new VendorBankDetailsVo();
				vendorBankDetails.setId(rs.getInt(1));
				vendorBankDetails.setBankName(rs.getString(2));
				vendorBankDetails.setAccountNumber(rs.getString(3));
				vendorBankDetails.setConfirmAccountNumber(rs.getString(3));
				vendorBankDetails.setAccountHolderName(rs.getString(4));
				vendorBankDetails.setBranchName(rs.getString(5));
				vendorBankDetails.setIfscCode(rs.getString(6));
				vendorBankDetails.setUpiId(rs.getString(7));
				vendorBankDetails.setIsDefault(rs.getBoolean(8));
				vendorBankDetails.setStatus(rs.getString(9));
				vendorBankDetailsList.add(vendorBankDetails);
				//employeeVo.setBankDetails(vendorBankDetailsList);
			}
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, null);
		}
		return vendorBankDetailsList;
	}
	
	public VendorBankDetailsVo getEmployeeDefaultBankDetails(int empId)
			throws ApplicationException {
		logger.info("Entry into method: getEmployeeBankDetails");
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		VendorBankDetailsVo vendorBankDetails  = null;
		try {
			con = getAccountsPayable();
			preparedStatement = con.prepareStatement(EmployeeConstants.GET_EMPLOYEE_DEFAULT_BANK_DETAILS_BY_ID);
			preparedStatement.setInt(1, empId);
			preparedStatement.setString(2, CommonConstants.STATUS_AS_DELETE);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				vendorBankDetails = new VendorBankDetailsVo();
				vendorBankDetails.setId(rs.getInt(1));
				vendorBankDetails.setBankName(rs.getString(2));
				vendorBankDetails.setAccountNumber(rs.getString(3));
				vendorBankDetails.setConfirmAccountNumber(rs.getString(3));
				vendorBankDetails.setAccountHolderName(rs.getString(4));
				vendorBankDetails.setBranchName(rs.getString(5));
				vendorBankDetails.setIfscCode(rs.getString(6));
				vendorBankDetails.setUpiId(rs.getString(7));
				vendorBankDetails.setIsDefault(rs.getBoolean(8));
				vendorBankDetails.setStatus(rs.getString(9));
			}
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return vendorBankDetails;
	}

	public void deleteEmployee(int id, String status,String userId,String roleName) throws ApplicationException {
		logger.info("Entry into method: deleteEmployee");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = getAccountsPayable();
			String sql = EmployeeConstants.DELETE_EMPLOYEE;
			preparedStatement = con.prepareStatement(sql);
			preparedStatement.setString(1, status);
			preparedStatement.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
			preparedStatement.setString(3, userId);
			preparedStatement.setString(4, roleName);
			preparedStatement.setInt(5, id);
			preparedStatement.executeUpdate();
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
	}

	public boolean updateEmployee(EmployeeVo employeeVo) throws ApplicationException {
		logger.info("Entry into method: updateEmployee");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		boolean isTxnSuccess = false;
		if (employeeVo.getId() != null) {
			try {
				con = getAccountsPayable();
				con.setAutoCommit(false);
				if (employeeVo.getEmployeeGeneralInfo() != null) {
					updateEmployeeGeneralInfo(employeeVo, con);
				}
				if (employeeVo.getBankDetails() != null) {
					updateEmployeeBankDetails(employeeVo, con);
				}
				if (employeeVo.getAttachments() != null && employeeVo.getAttachments().size() > 0) {
					attachmentsDao.createAttachments(employeeVo.getOrganizationId(),employeeVo.getUserId(),employeeVo.getAttachments(),
							AttachmentsConstants.MODULE_TYPE_EMPLOYEE, employeeVo.getId(),employeeVo.getRoleName());
				}
				if (employeeVo.getAttachmentsToRemove() != null && employeeVo.getAttachmentsToRemove().size() > 0) {
					for (Integer attachmentId : employeeVo.getAttachmentsToRemove()) {
						attachmentsDao.changeStatusForSingleAttachment(attachmentId, CommonConstants.STATUS_AS_DELETE,employeeVo.getUserId(),employeeVo.getRoleName());
					}
				}
				con.commit();
				isTxnSuccess = true;
			} catch (Exception e) {
				try {
					con.rollback();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					throw new ApplicationException(e1);
				}
				if(e!=null && e instanceof ApplicationException){
					ApplicationException appException=(ApplicationException) e;
					throw appException;
				}else{
					throw new ApplicationException(e);
				}
				
			} finally {
				closeResources(rs, preparedStatement, con);
			}
		}
		return isTxnSuccess;
	}

	private void updateEmployeeGeneralInfo(EmployeeVo employeeVo, Connection con) throws ApplicationException {
		logger.info("Entry into method: updateEmployeeGeneralInfo");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		EmployeeGeneralInfoVo employeeGeneralInfoVo = employeeVo.getEmployeeGeneralInfo();
		
		if(employeeVo.getStatus()!=null && employeeVo.getStatus().equals("ACTIVE")){
			employeeVo.setStatus("ACT");
		}
		if(employeeVo.getBookKeepingSettings()==null){
			BookKeepingSettingsVo bookKeepingSettings=new BookKeepingSettingsVo();
			bookKeepingSettings.setDefaultGlName("Payables to employees");
			bookKeepingSettings.setId(chartOfAccountsDao.getLedgerIdGivenName("Payables to employees", employeeVo.getOrganizationId()));
			employeeVo.setBookKeepingSettings(bookKeepingSettings);
		}
		
		try {
			String sql = EmployeeConstants.UPDATE_EMPLOYEE;
			preparedStatement = con.prepareStatement(sql);
			preparedStatement.setString(1,
					employeeGeneralInfoVo.getEmployeeId() != null ? employeeGeneralInfoVo.getEmployeeId() : null);
			preparedStatement.setString(2,
					employeeGeneralInfoVo.getFirstName() != null ? employeeGeneralInfoVo.getFirstName() : null);
			preparedStatement.setString(3,
					employeeGeneralInfoVo.getLastName() != null ? employeeGeneralInfoVo.getLastName() : null);
			preparedStatement.setString(4,
					employeeGeneralInfoVo.getMobileNo() != null ? employeeGeneralInfoVo.getMobileNo() : null);
			preparedStatement.setObject(5,
					employeeGeneralInfoVo.getReportingTo() != null ? employeeGeneralInfoVo.getReportingTo() : null);
			preparedStatement.setObject(6,
					employeeVo.getOrganizationId() != null ? employeeVo.getOrganizationId() : null);
			preparedStatement.setObject(7, Integer.valueOf(employeeVo.getUserId()));
			preparedStatement.setString(8,
					employeeGeneralInfoVo.getDateOfJoining() != null ? employeeGeneralInfoVo.getDateOfJoining() : null);
			preparedStatement.setObject(9,
					employeeGeneralInfoVo.getDepartment() != null ? employeeGeneralInfoVo.getDepartment() : null);
			preparedStatement.setObject(10,
					employeeGeneralInfoVo.getEmployeeType() != null ? employeeGeneralInfoVo.getEmployeeType() : null);
			preparedStatement.setString(11,
					employeeGeneralInfoVo.getEmployeeStatus() != null ? employeeGeneralInfoVo.getEmployeeStatus()
							: null);
			preparedStatement.setString(12,
					employeeGeneralInfoVo.getEmail() != null ? employeeGeneralInfoVo.getEmail() : null);
			preparedStatement.setObject(13, employeeVo.getIsSuperAdmin() != null ? employeeVo.getIsSuperAdmin() : null);
			preparedStatement.setTimestamp(14, new Timestamp(System.currentTimeMillis()));
			preparedStatement.setString(15, employeeVo.getRoleName());
			preparedStatement.setString(16, employeeVo.getStatus());
			preparedStatement.setInt(17, employeeVo.getBookKeepingSettings().getId());
			preparedStatement.setString(18,employeeVo.getBookKeepingSettings().getDefaultGlName());
			preparedStatement.setInt(19, employeeVo.getId());
			preparedStatement.executeUpdate();
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, null);
		}
	}

	private void updateEmployeeBankDetails(EmployeeVo employeeVo, Connection con) throws ApplicationException {
		logger.info("Entry into method: updateEmployeeBankDetails");
		Integer id = employeeVo.getId();
		for (int i = 0; i < employeeVo.getBankDetails().size(); i++) {
			VendorBankDetailsVo vendorBankDetailsVo = employeeVo.getBankDetails().get(i);
			String status = employeeVo.getBankDetails().get(i).getStatus();
			if(vendorBankDetailsVo.getId()==null){
				status=CommonConstants.STATUS_AS_NEW;
			}
			if (status.equalsIgnoreCase(CommonConstants.STATUS_AS_DELETE)
					|| status.equalsIgnoreCase(CommonConstants.STATUS_AS_ACTIVE)) {
				updateBankDetails(vendorBankDetailsVo, status, con);
			} else if (status.equalsIgnoreCase(CommonConstants.STATUS_AS_NEW)) {
				createBankDetails(vendorBankDetailsVo, id, con);
			}
		}
		
		if(employeeVo.getItemsToRemove()!=null){
			for(int i=0;i<employeeVo.getItemsToRemove().size();i++){
				String sql = EmployeeConstants.DELETE_EMPLOYEE_BANK_DETAILS;
				PreparedStatement preparedStatement=null;
				try {
					preparedStatement = con.prepareStatement(sql);
					preparedStatement.setString(1,"DEL");
					preparedStatement.setInt(2,employeeVo.getItemsToRemove().get(i));
					preparedStatement.executeUpdate();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					throw new ApplicationException(e);
				}finally{
					closeResources(null, preparedStatement,null);
				}
				
			}
		}
	}

	public void updateBankDetails(VendorBankDetailsVo vendorBankDetailsVo, String status, Connection con)
			throws ApplicationException {
		logger.info("Entry into method: updateBankDetails");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			String sql = EmployeeConstants.UPDATE_EMPLOYEE_BANK_DETAILS;
			preparedStatement = con.prepareStatement(sql);
			preparedStatement.setString(1,
					vendorBankDetailsVo.getAccountNumber() != null ? vendorBankDetailsVo.getAccountNumber() : null);
			preparedStatement.setString(2,
					vendorBankDetailsVo.getAccountHolderName() != null ? vendorBankDetailsVo.getAccountHolderName()
							: null);
			preparedStatement.setString(3,
					vendorBankDetailsVo.getBankName() != null ? vendorBankDetailsVo.getBankName() : null);
			preparedStatement.setString(4,
					vendorBankDetailsVo.getBranchName() != null ? vendorBankDetailsVo.getBranchName() : null);
			preparedStatement.setString(5,
					vendorBankDetailsVo.getIfscCode() != null ? vendorBankDetailsVo.getIfscCode() : null);
			preparedStatement.setString(6,
					vendorBankDetailsVo.getUpiId() != null ? vendorBankDetailsVo.getUpiId() : null);
			preparedStatement.setObject(7,
					vendorBankDetailsVo.getIsDefault() != null ? vendorBankDetailsVo.getIsDefault() : null);
			if (status.equalsIgnoreCase(CommonConstants.STATUS_AS_DELETE)) {
				preparedStatement.setString(8, CommonConstants.STATUS_AS_DELETE);
			} else {
				preparedStatement.setString(8, CommonConstants.STATUS_AS_ACTIVE);
			}
			preparedStatement.setTimestamp(9, new Timestamp(System.currentTimeMillis()));
			preparedStatement.setInt(10, vendorBankDetailsVo.getId());
			preparedStatement.executeUpdate();
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, null);
		}
	}

	public void createBankDetails(VendorBankDetailsVo vendorBankDetailsVo, Integer id, Connection con)
			throws ApplicationException {
		logger.info("Entry into method:createBankDetails");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			boolean isBankDetailsExist = checkBankDetailsExist(id, vendorBankDetailsVo.getAccountNumber(),
					vendorBankDetailsVo.getIfscCode(), con);
			if (isBankDetailsExist) {
				throw new ApplicationException("Bank Details Exist for the Employee");
			}
			String InsertQuery = EmployeeConstants.INSERT_INTO_EMPLOYEE_BANK_DETAILS;
			preparedStatement = con.prepareStatement(InsertQuery, Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setString(1,
					vendorBankDetailsVo.getBankName() != null ? vendorBankDetailsVo.getBankName() : null);
			preparedStatement.setString(2,
					vendorBankDetailsVo.getAccountNumber() != null ? vendorBankDetailsVo.getAccountNumber() : null);
			preparedStatement.setString(3,
					vendorBankDetailsVo.getAccountHolderName() != null ? vendorBankDetailsVo.getAccountHolderName()
							: null);
			preparedStatement.setString(4,
					vendorBankDetailsVo.getBranchName() != null ? vendorBankDetailsVo.getBranchName() : null);
			preparedStatement.setString(5,
					vendorBankDetailsVo.getIfscCode() != null ? vendorBankDetailsVo.getIfscCode() : null);
			preparedStatement.setString(6,
					vendorBankDetailsVo.getUpiId() != null ? vendorBankDetailsVo.getUpiId() : null);
			preparedStatement.setObject(7,
					vendorBankDetailsVo.getIsDefault() != null ? vendorBankDetailsVo.getIsDefault() : null);
			preparedStatement.setInt(8, id);
			int rowAffected = preparedStatement.executeUpdate();
			if (rowAffected == 1) {
				rs = preparedStatement.getGeneratedKeys();
				if (rs.next()) {
					vendorBankDetailsVo.setId(rs.getInt(1));
				}
			}
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, null);
		}
	}

	private boolean checkBankDetailsExist(Integer id, String accountNumber, String ifscCode, Connection con)
			throws ApplicationException {
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			String query = EmployeeConstants.CHECK_BANK_DETAILS_EXIST_FOR_EMPLOYEE;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, id);
			preparedStatement.setString(2, accountNumber);
			preparedStatement.setString(3, ifscCode);
			preparedStatement.setString(4, CommonConstants.STATUS_AS_DELETE);
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

	public void deleteEmployeeEntries(Integer id,String userId,String roleName) throws ApplicationException {
		logger.info("To deleteEmployeeEntries:: ");
		Connection connection = null;
		if (id != null) {
			try {
				connection = getAccountsPayable();
				// To remove from vendor info table
				changeStatusForEmployeeTables(id, CommonConstants.STATUS_AS_DELETE, connection,
						EmployeeConstants.MODIFY_EMPLOYEE_STATUS);
				// To remove from vendor contact table
				changeStatusForEmployeeTables(id, CommonConstants.STATUS_AS_DELETE, connection,
						EmployeeConstants.MODIFY_EMPLOYEE_CONTACT_STATUS);
				// To remove from Attachments table
				attachmentsDao.changeStatusForAttachments(id, CommonConstants.STATUS_AS_DELETE,
						AttachmentsConstants.MODULE_TYPE_EMPLOYEE,userId,roleName);
				logger.info("Deleted successfully in all tables ");
			} catch (Exception e) {
				logger.info("Error in deleteEmployeeEntries:: ", e);
				throw new ApplicationException(e);
			} finally {
				closeResources(null, null, connection);
			}
		}

	}

	private void changeStatusForEmployeeTables(Integer id, String status, Connection con, String query)
			throws ApplicationException {
		logger.info("To Change the status with query :: " + query);
		if (query != null) {
			PreparedStatement preparedStatement=null;
			try{
				preparedStatement = con.prepareStatement(query);
				preparedStatement.setString(1, status);
				preparedStatement.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
				preparedStatement.setInt(3, id);
				preparedStatement.executeUpdate();
				logger.info("Status chaneged successfully ");
			} catch (Exception e) {
				logger.info("Error in changeEmployeetablesStatus ", e);
				throw new ApplicationException(e);
			} finally {
				closeResources(null, preparedStatement, null);
			}
		}
	}

	public List<BasicEmployeeVo> getBasicEmployees(int organizationId, Connection con) throws ApplicationException {
		logger.info("Entry into method: getBasicEmployees");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		List<BasicEmployeeVo> employeeList = new ArrayList<BasicEmployeeVo>();
		try {
			String query = SettingsAndPreferencesConstants.GET_EMPLOYEES_ORGANIZATION;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, organizationId);
			preparedStatement.setString(2, CommonConstants.STATUS_AS_INACTIVE);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				BasicEmployeeVo data = new BasicEmployeeVo();
				data.setId(rs.getInt(1));
				String name = rs.getString(2) + " " + rs.getString(3);
				data.setName(name);
				employeeList.add(data);
			}
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, null);
		}
		return employeeList;
	}

	
	public List<BasicEmployeeVo> getAllActiveEmployees(int organizationId, Connection con) throws ApplicationException {
		logger.info("Entry into method: getBasicEmployees");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		List<BasicEmployeeVo> employeeList = new ArrayList<BasicEmployeeVo>();
		try {
			String query = SettingsAndPreferencesConstants.GET_ALL_ACTIVE_EMPLOYEES_ORGANIZATION;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, organizationId);
			preparedStatement.setString(2, CommonConstants.STATUS_AS_ACTIVE);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				BasicEmployeeVo data = new BasicEmployeeVo();
				data.setId(rs.getInt(1));
				String name = rs.getString(2) + " " + rs.getString(3);
				data.setName(name);
				employeeList.add(data);
			}
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, null);
		}
		return employeeList;
	}
	
	public List<PaymentTypeVo> getAllActiveEmployees(int organizationId) throws ApplicationException {
		logger.info("Entry into method: getAllActiveEmployees");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		List<PaymentTypeVo> employeeList = new ArrayList<PaymentTypeVo>();
		try {
			con = getAccountsPayable();
			String query = SettingsAndPreferencesConstants.GET_ALL_ACTIVE_EMPLOYEES_ORGANIZATION;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, organizationId);
			preparedStatement.setString(2, CommonConstants.STATUS_AS_ACTIVE);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				PaymentTypeVo data = new PaymentTypeVo();
				data.setId(rs.getInt(1));
				String name = rs.getString(2) + " " + rs.getString(3);
				data.setName(name);
				data.setValue(rs.getInt(1));
				employeeList.add(data);
			}
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return employeeList;
	}
	
	
	public Integer getDepartmentId(String departmentName, int orgId) throws ApplicationException {
		logger.info("Entry into getDepartmentId");
		Integer departmentId = null;
		Connection con =null;
		PreparedStatement preparedStatement =null;
		ResultSet rs =null;
		try {
			con = getUserMgmConnection();
			preparedStatement = con.prepareStatement(EmployeeConstants.GET_DEPARTMENT_ID);
			preparedStatement.setString(2, departmentName);
			preparedStatement.setInt(1, orgId);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				departmentId = rs.getInt(1);
			}

		} catch (Exception e) {
			logger.info("Error in  getDepartmentId:", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return departmentId;
	}

	public Integer getEmployeeTypeId(String employeeType) throws ApplicationException {
		logger.info("Entry into getEmployeeTypeId");
		Integer employeeTypeId = null;
		Connection con =null;
		PreparedStatement preparedStatement = null;
		ResultSet rs =null;
		try  {
			con = getFinanceCommon();
			preparedStatement = con.prepareStatement(EmployeeConstants.GET_EMPLOYEE_TYPE_ID);
			preparedStatement.setString(1, employeeType);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				employeeTypeId = rs.getInt(1);
			}

		} catch (Exception e) {
			logger.info("Error in  getEmployeeTypeId:", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return employeeTypeId;
	}

	public Integer getEmployeeId(String email, Integer organizationId) throws ApplicationException {
		logger.info("Entry into getEmployeeId");
		Integer employeeId = 0;
		Connection con = null;
		PreparedStatement preparedStatement =null;
		ResultSet rs =null;
		try  {
			con = getAccountsPayable();
			preparedStatement = con.prepareStatement(EmployeeConstants.GET_EMPLOYEE_ID);
			preparedStatement.setString(2, email);
			preparedStatement.setInt(1, organizationId);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				employeeId = rs.getInt(1);
			}

		} catch (Exception e) {
			logger.info("Error in  getEmployeeId:", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return employeeId;
	}
	
	public String  getEmployeeName(Integer employeeId ) throws ApplicationException {
		logger.info("Entry into getEmployeeId");
		String  employeeName = null;
		Connection con = null;
		PreparedStatement preparedStatement =null;
		ResultSet rs =null;
		try  {
			con = getAccountsPayable();
			preparedStatement = con.prepareStatement(EmployeeConstants.GET_EMPLOYEE_NAME);
			preparedStatement.setInt(1, employeeId);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				employeeName = rs.getString(1) + " " + rs.getString(2);
			}

		} catch (Exception e) {
			logger.info("Error in  getEmployeeId:", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return employeeName;
	}

	public String processUpload(EmployeeVo employeeVo, String departmentName, String employeeType,Boolean duplicacy)
			throws ApplicationException {
		logger.info("Entry into method: processUpload");
		EmployeeGeneralInfoVo employeeGeneralInfo = employeeVo.getEmployeeGeneralInfo();
		employeeVo.setStatus("ACT");
		if (employeeType != null) {
			Integer employeeTypeId = getEmployeeTypeId(employeeType);
			if (employeeTypeId != null) {
				employeeGeneralInfo.setEmployeeType(getEmployeeTypeId(employeeType));
			} else {
				throw new ApplicationException("Invalid Employee Type");
			}
		}
		if (departmentName != null) {
			Integer departmentId = getDepartmentId(departmentName, employeeVo.getOrganizationId());
			if (departmentId != null) {
				employeeGeneralInfo.setDepartment(getDepartmentId(departmentName, employeeVo.getOrganizationId()));
			} else {
				throw new ApplicationException("Invalid Department Name");
			}
		}
		employeeVo.setEmployeeGeneralInfo(employeeGeneralInfo);
		boolean isEmployeeExist = checkEmployeeExist(employeeVo.getEmployeeGeneralInfo().getEmployeeId(),
				employeeVo.getOrganizationId());
		if (isEmployeeExist) {
			if(duplicacy) {
				int employeeId = getEmployeeId(employeeVo.getEmployeeGeneralInfo().getEmail(),
						employeeVo.getOrganizationId());
				employeeVo.setId(employeeId);
				updateEmployee(employeeVo);
			}
		}

		else{
			createEmployee(employeeVo);
			return "Create";
		}
		return null;

	}

	public List<EmployeeExportVo> getListEmployeesById(ExportVo exportVo) throws SQLException, ApplicationException {
		logger.info("Entry into method: getListEmployeesById");
		List<EmployeeExportVo> employees = new ArrayList<EmployeeExportVo>();
		if (exportVo.getListOfId() != null && exportVo.getListOfId().size() > 0) {
			List<Integer> listOfId = exportVo.getListOfId();
			for (Integer Id : listOfId) {
				EmployeeExportVo employee = getEmployeeDetailsForExport(Id);
				List<BankExportVo> bankDetails = getEmployeeBankDetailsExport(Id);
				employee.setBankDetails(bankDetails);
				employees.add(employee);
			}
		}
		return employees;

	}

	private List<BankExportVo> getEmployeeBankDetailsExport(Integer id) throws SQLException, ApplicationException {
		logger.info("Entry into method: getEmployeeBankDetailsEmport");
		List<BankExportVo> bankDetails = new ArrayList<BankExportVo>();
		Connection con =null;
		PreparedStatement preparedStatement =null;
		ResultSet rs =null;
		try {
			con = getAccountsPayable();
			preparedStatement = con.prepareStatement(EmployeeConstants.GET_EMPLOYEE_BANK_DETAILS_EXPORT_BY_ID);
			preparedStatement.setInt(1, id);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				BankExportVo exportEmployeeBankVo = new BankExportVo();
				exportEmployeeBankVo.setAccountHolderName(rs.getString(1));
				exportEmployeeBankVo.setAccountNo(rs.getString(2));
				exportEmployeeBankVo.setBankNname(rs.getString(3));
				exportEmployeeBankVo.setBranchName(rs.getString(4));
				exportEmployeeBankVo.setIfscCode(rs.getString(5));
				exportEmployeeBankVo.setUpiId(rs.getString(6));
				exportEmployeeBankVo.setIsDefault(rs.getBoolean(7));
				bankDetails.add(exportEmployeeBankVo);
			}
		} catch (Exception e) {
			logger.info("Error in  getEmployeeBankDetailsEmport:", e);
			throw new ApplicationException(e);
		}finally{
			closeResources(rs, preparedStatement, con);
		}
		return bankDetails;
	}

	private EmployeeExportVo getEmployeeDetailsForExport(Integer id) throws ApplicationException {
		logger.info("Entry into method: getEmployeeDetailsForExport");
		EmployeeExportVo employeeExportVo = new EmployeeExportVo();
		Connection con =null;
		PreparedStatement preparedStatement =null;
		ResultSet rs =null;
		try{
			con = getAccountsPayable();
			preparedStatement = con.prepareStatement(EmployeeConstants.GET_EMPLOYEE_DETAILS_BY_ID);
			preparedStatement.setInt(1, id);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				employeeExportVo.setId(rs.getInt(1));
				employeeExportVo.setEmployeeId(rs.getString(2));
				employeeExportVo.setFirstName(rs.getString(3));
				employeeExportVo.setLastName(rs.getString(4));
				employeeExportVo.setMobileNo(rs.getString(5));
				Integer departmentId = rs.getInt(6);
				if (departmentId != null)
					employeeExportVo.setDepartmentName(departmentDao.getDepartmentById(departmentId));
				employeeExportVo.setEmployeeStatus(rs.getString(7));
				Integer employeeTypeId = rs.getInt(8);
				if (employeeTypeId != null)
					employeeExportVo.setEmployeeType(financeCommonDao.getEmployeeTypeById(employeeTypeId));
				employeeExportVo.setStatus(rs.getString(9));
			}
		} catch (Exception e) {
			logger.info("Error in  getEmployeeDetailsForExport:", e);
			throw new ApplicationException(e);
		}finally{
			closeResources(rs, preparedStatement, con);
		}
		return employeeExportVo;
	}
}

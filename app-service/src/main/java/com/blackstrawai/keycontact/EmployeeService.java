package com.blackstrawai.keycontact;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.attachments.AttachmentService;
import com.blackstrawai.attachments.AttachmentsConstants;
import com.blackstrawai.common.BaseService;
import com.blackstrawai.common.ChartOfAccountsThread;
import com.blackstrawai.common.DropDownDao;
import com.blackstrawai.export.EmployeeExportVo;
import com.blackstrawai.export.ExportVo;
import com.blackstrawai.keycontact.dropdowns.EmployeeDropDownVo;
import com.blackstrawai.keycontact.employee.EmployeeBasicDetailsVo;
import com.blackstrawai.keycontact.employee.EmployeeVo;

@Service
public class EmployeeService extends BaseService {

	@Autowired
	EmployeeDao employeeDao;

	@Autowired
	private AttachmentService attachmentService;

	@Autowired
	DropDownDao dropDownDao;

	private Logger logger = Logger.getLogger(EmployeeService.class);

	public void createEmployee(EmployeeVo employeeVo) throws ApplicationException {
		logger.info("Entry into createEmployee");
		try {
			boolean isTxnSuccess = employeeDao.createEmployee(employeeVo);
			if (isTxnSuccess && !employeeVo.getAttachments().isEmpty() && employeeVo.getId() != null) {
				logger.info("Entry into upload");
				attachmentService.upload(AttachmentsConstants.MODULE_TYPE_EMPLOYEE, employeeVo.getId(),
						employeeVo.getOrganizationId(), employeeVo.getAttachments());
				logger.info("Upload Successful");
			}
			// String name=employeeVo.getEmployeeGeneralInfo().getFirstName()+" "
		/*	// +employeeVo.getEmployeeGeneralInfo().getLastName()+"-"+employeeVo.getId();
			String name = employeeVo.getEmployeeGeneralInfo().getFirstName() + " "
					+ employeeVo.getEmployeeGeneralInfo().getLastName() + "-"
					+ employeeVo.getEmployeeGeneralInfo().getEmployeeId();
		*/	
		/*	String name=""+employeeVo.getId();
			String displayName = employeeVo.getEmployeeGeneralInfo().getFirstName() + " "
					+ employeeVo.getEmployeeGeneralInfo().getLastName();
			ChartOfAccountsThread chartOfAccountsThread = new ChartOfAccountsThread(employeeDao,
					employeeVo.getOrganizationId(), employeeVo.getUserId(), name, displayName);
			chartOfAccountsThread.start();*/

		} catch (ApplicationException e1) {
			throw e1;
		} catch (Exception e) {
			try {
				employeeDao.deleteEmployeeEntries(employeeVo.getId(),employeeVo.getUserId(),employeeVo.getRoleName());
			} catch (Exception e1) {
				logger.info("Error in Employee entries delete in service layer ");
				throw e1;
			}
			throw e;

		}
		logger.info("Employee created Successfully in service layer ");
	}

	/*public List<EmployeeBasicDetailsVo> getAllEmployeesOfAnOrganization(int organizationId)
			throws ApplicationException {
		logger.info("Entry into method: getAllEmployeesOfAnOrganization");
		List<EmployeeBasicDetailsVo> employeeList = employeeDao.getAllEmployeesOfAnOrganization(organizationId);
		return employeeList;
	}*/
	
	public List<EmployeeBasicDetailsVo> getAllEmployeesOfAnOrganizationForUserAndRole(int organizationId,String userId,String roleName)
			throws ApplicationException {
		logger.info("Entry into method: getAllEmployeesOfAnOrganizationForUserAndRole");
		List<EmployeeBasicDetailsVo> employeeList = employeeDao.getAllEmployeesOfAnOrganizationForUserAndRole(organizationId,userId,roleName);
		return employeeList;
	}

	public EmployeeVo getEmployeeById(int id) throws ApplicationException {
		logger.info("Entry into method: getEmployeeById");
		EmployeeVo employeeVo;
		try {
			employeeVo = employeeDao.getEmployeeById(id);
			if (employeeVo != null && employeeVo.getAttachments().size() > 0 && employeeVo.getId() != null) {
				attachmentService.encodeAllFiles(employeeVo.getOrganizationId(), employeeVo.getId(),
						AttachmentsConstants.MODULE_TYPE_EMPLOYEE, employeeVo.getAttachments());
			}

		} catch (Exception e) {
			throw new ApplicationException(e);
		}
		return employeeVo;
	}

	public void deleteEmployee(int id, String status,String userId,String roleName) throws ApplicationException {
		logger.info("Entry into method: deleteEmployee");
		employeeDao.deleteEmployee(id, status,userId,roleName);
	}

	public void updateEmployee(EmployeeVo employeeVo) throws ApplicationException {
		logger.info("Entry into method: updateEmployee");
		if (employeeVo.getId() != null) {
			try {
				boolean isTxnSuccess = employeeDao.updateEmployee(employeeVo);
				if (isTxnSuccess && !employeeVo.getAttachments().isEmpty() && employeeVo.getId() != null) {
					logger.info("Entry into upload");
					attachmentService.upload(AttachmentsConstants.MODULE_TYPE_EMPLOYEE, employeeVo.getId(),
							employeeVo.getOrganizationId(), employeeVo.getAttachments());
					logger.info("Upload Successfull");
				}
			} catch (Exception e) {
				throw e;
			}
		}
	}

	public EmployeeDropDownVo getEmpoyeeDropDownData(int organizationId) throws ApplicationException {
		logger.info("Entry into method: getEmployeeDropDownData");
		return dropDownDao.getEmployeeDropDownData(organizationId);
	}

	public Map<String, String> processUpload(EmployeeVo employeeVo, String departmentName, String employeeType,Boolean duplicacy)
			throws ApplicationException {
		logger.info("Entry into method: processUpload");
		Map<String, String> map = new HashMap<String, String>();
		try {
			String status=employeeDao.processUpload(employeeVo, departmentName, employeeType,duplicacy);
			map.put("Success", "Success");
			if(status!=null && status.equals("Create")){
				String name=""+employeeVo.getId();
				String displayName = employeeVo.getEmployeeGeneralInfo().getFirstName() + " "
						+ employeeVo.getEmployeeGeneralInfo().getLastName();
				ChartOfAccountsThread chartOfAccountsThread = new ChartOfAccountsThread(employeeDao,
						employeeVo.getOrganizationId(), employeeVo.getUserId(), name, displayName);
				chartOfAccountsThread.start();
			}
		
			return map;
		} catch (ApplicationException e) {
			map.put("Failure", e.getMessage());
			return map;
		}
	}

	public List<EmployeeExportVo> getListEmployeesById(ExportVo exportVo) throws SQLException, ApplicationException {
		logger.info("Entry into method: getListEmployeesById");
		List<EmployeeExportVo> employees=employeeDao.getListEmployeesById(exportVo);
		return employees;
		
	}
}

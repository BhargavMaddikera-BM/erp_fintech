package com.blackstrawai.settings;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.common.BaseService;

@Service
public class DepartmentService extends BaseService{

	@Autowired
	DepartmentDao departmentDao;
	
	public DepartmentVo createDepartment(DepartmentVo departmentVo) throws ApplicationException {
		departmentVo=departmentDao.createDepartment(departmentVo);
		return departmentVo;	
	}

	public List<DepartmentVo> getAllDepartmentsOfAnOrganization(int organizationId) throws ApplicationException {
		List<DepartmentVo> listOfDepartment=departmentDao.getAllDepartmentsOfAnOrganization(organizationId);
		Collections.reverse(listOfDepartment);
		return listOfDepartment;	
	}
	
	public List<DepartmentVo> getAllDepartmentsOfAnOrganizationForUserAndRole(int organizationId,String userId,String roleName) throws ApplicationException {
		List<DepartmentVo> listOfDepartment=departmentDao.getAllDepartmentsOfAnOrganizationForUserAndRole(organizationId,userId,roleName);
		Collections.reverse(listOfDepartment);
		return listOfDepartment;	
	}
	
	public DepartmentVo getDepartmentById(int id) throws ApplicationException {
		DepartmentVo departmentVo=departmentDao.getDepartmentById(id);
		return departmentVo;	
	}
	public DepartmentVo deleteDepartment(int id,String status,String userId,String roleName) throws ApplicationException {
		DepartmentVo departmentVo=departmentDao.deleteDepartment(id,status,userId,roleName);
		return departmentVo;	
	}
	public DepartmentVo updateDepartment(DepartmentVo departmentVo) throws ApplicationException {
		departmentVo=departmentDao.updateDepartment(departmentVo);
		return departmentVo;	
	}
}

package com.blackstrawai.settings;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.common.BaseService;

@Service
public class CustomerGroupService extends BaseService{
	
	@Autowired
	CustomerGroupDao customerGroupDao;
	
	public CustomerGroupVo createCustomerGroup(CustomerGroupVo customerGroupVo) throws ApplicationException {
		customerGroupVo=customerGroupDao.createCustomerGroup(customerGroupVo);
		return customerGroupVo;	
	}

	public List<CustomerGroupVo> getAllCustomerGroupOfAnOrganization(int organizationId) throws ApplicationException {
		List<CustomerGroupVo> listOfCustomerGroup=customerGroupDao.getAllCustomerGroupOfAnOrganization(organizationId);
		Collections.reverse(listOfCustomerGroup);
		return listOfCustomerGroup;	
	}
	
	public List<CustomerGroupVo> getAllCustomerGroupOfAnOrganizationForUserAndRole(int organizationId,String userId,String roleName) throws ApplicationException {
		List<CustomerGroupVo> listOfCustomerGroup=customerGroupDao.getAllCustomerGroupOfAnOrganizationForUserAndRole(organizationId,userId,roleName);
		Collections.reverse(listOfCustomerGroup);
		return listOfCustomerGroup;	
	}
	
	public CustomerGroupVo getCustomerGroupById(int id) throws ApplicationException {
		CustomerGroupVo customerGroupVo=customerGroupDao.getCustomerGroupById(id);
		return customerGroupVo;	
	}
	public CustomerGroupVo deleteCustomerGroup(int id,String status,String userId,String roleName) throws ApplicationException {
		CustomerGroupVo customerGroupVo=customerGroupDao.deleteCustomerGroup(id,status,userId,roleName);
		return customerGroupVo;	
	}
	public CustomerGroupVo updateCustomerGroup(CustomerGroupVo customerGroupVo) throws ApplicationException {
		customerGroupVo=customerGroupDao.updateCustomerGroup(customerGroupVo);
		return customerGroupVo;	
	}
	
}

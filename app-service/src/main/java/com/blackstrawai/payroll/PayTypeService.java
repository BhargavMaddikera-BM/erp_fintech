package com.blackstrawai.payroll;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.common.BaseService;
import com.blackstrawai.common.DropDownDao;
import com.blackstrawai.payroll.dropdowns.PayTypeDropDownVo;

@Service
public class PayTypeService extends BaseService{
	
	@Autowired
	PayTypeDao payTypeDao;
	
	@Autowired
	DropDownDao dropDownDao;
	
	
	public PayTypeVo createPayType(PayTypeVo data ) throws ApplicationException {
		data=payTypeDao.createPayType(data);
		return data;	
	}
	

	public PayTypeVo updatePayType(PayTypeVo data ) throws ApplicationException {
		data=payTypeDao.updatePayType(data);
		return data;	
	}
	
	public PayTypeVo deletePayType(int id,String status,String userId,String roleName) throws ApplicationException {
		return payTypeDao.deletePayType(id,status,userId,roleName);
	}

	public List<PayTypeVo> getAllPayTypesOfAnOrganization(int organizationId) throws ApplicationException {
		List<PayTypeVo> data=payTypeDao.getAllPayTypesOfAnOrganization(organizationId);
		Collections.reverse(data);
		return data;	
	}	
	
	public List<PayTypeVo> getAllPayTypesOfAnOrganizationForUserAndRole(int organizationId,String userId,String roleName) throws ApplicationException {
		List<PayTypeVo> data=payTypeDao.getAllPayTypesOfAnOrganizationForUserAndRole(organizationId,userId,roleName);
		Collections.reverse(data);
		return data;	
	}	
	
	public PayTypeVo getPayTypeById(int id) throws ApplicationException {
		PayTypeVo data=payTypeDao.getPayTypeById(id);
		return data;	
	}
	
	public PayTypeDropDownVo getPayTypeDropDownData(int organizationId) throws ApplicationException {
		PayTypeDropDownVo data=dropDownDao.getPayTypeDownData(organizationId);
		return data;	
	}
	
	
}

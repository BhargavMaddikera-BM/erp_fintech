package com.blackstrawai.settings;

import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.common.BaseService;
import com.blackstrawai.common.DropDownDao;

@Service
public class VoucherService extends BaseService{

	@Autowired
	VoucherDao voucherdao;
	
	@Autowired
	DropDownDao dropDownDao;
	
	private Logger logger = Logger.getLogger(VoucherService.class);
	
	public VoucherVo createVoucher(VoucherVo voucherVo) throws ApplicationException{
		logger.info("Entry into method:createVoucher");
		return voucherdao.createVoucher(voucherVo);
	}
	
	public List<VoucherVo> getAllvouchersOfAnOrganization(int organizationId) throws ApplicationException{
		logger.info("Entry into method:getAllvouchersOfAnOrganization");
		List<VoucherVo> voucherList=voucherdao.getAllvouchersOfAnOrganization(organizationId);
		Collections.reverse(voucherList);
		return voucherList;
	}
	
	public List<VoucherVo> getAllvouchersOfAnOrganizationForUserAndRole(int organizationId,String userId,String roleName) throws ApplicationException{
		logger.info("Entry into method:getAllvouchersOfAnOrganization");
		List<VoucherVo> voucherList=voucherdao.getAllvouchersOfAnOrganizationForUserAndRole(organizationId,userId,roleName);
		Collections.reverse(voucherList);
		return voucherList;
	}
	
	
	public VoucherVo getVoucherById(int voucherId) throws ApplicationException{
		logger.info("Entry into method:getVoucherById");
		return voucherdao.getVoucherById(voucherId);
	}
	
	public VoucherVo deleteVoucher(int voucherId,String status,String userId,String roleName) throws ApplicationException {
		logger.info("Entry into method:deleteVoucher");
		return voucherdao.deleteVoucher(voucherId,status,userId,roleName);
	}
	
	public VoucherVo updateVoucher(VoucherVo voucherVo) throws ApplicationException {
		logger.info("Entry into method:updateVoucher");
		return voucherdao.updateVoucher(voucherVo);
	}
	

	public VoucherDropDownVo getVoucherDropDownData() throws ApplicationException {
		logger.info("Entry into method: getVoucherDropDownData");
		return dropDownDao.getVoucherDropDownData();
	}
	
	
}

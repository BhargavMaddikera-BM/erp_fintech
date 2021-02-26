package com.blackstrawai.settings;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.common.BaseService;
import com.blackstrawai.common.DropDownDao;

@Service
public class TaxRateTypeService extends BaseService {

	@Autowired
	DropDownDao dropDownDao;

	private Logger logger = Logger.getLogger(TaxRateTypeService.class);
	
	public List<TaxRateTypeVo> getTaxRateDropDownData(int organizationId)throws ApplicationException {	
		logger.info("Entry into method: getTaxRateDropDownData");
		return dropDownDao.getTaxRateDropDown(organizationId);
	}



}

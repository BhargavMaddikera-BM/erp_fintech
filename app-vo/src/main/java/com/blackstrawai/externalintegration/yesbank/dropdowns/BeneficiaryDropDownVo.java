package com.blackstrawai.externalintegration.yesbank.dropdowns;

import java.util.List;

import com.blackstrawai.common.BaseVo;
import com.blackstrawai.externalintegration.yesbank.beneficiary.AccountTypeVo;
import com.blackstrawai.externalintegration.yesbank.beneficiary.BeneficiaryTypeVo;

public class BeneficiaryDropDownVo extends BaseVo {

	private List<BeneficiaryTypeVo> beneficiaryTypes;
	
	private List<AccountTypeVo> accountTypes;

	public List<BeneficiaryTypeVo> getBeneficiaryTypes() {
		return beneficiaryTypes;
	}

	public void setBeneficiaryTypes(List<BeneficiaryTypeVo> beneficiaryTypes) {
		this.beneficiaryTypes = beneficiaryTypes;
	}

	public List<AccountTypeVo> getAccountTypes() {
		return accountTypes;
	}

	public void setAccountTypes(List<AccountTypeVo> accountTypes) {
		this.accountTypes = accountTypes;
	}

	
	
	

}

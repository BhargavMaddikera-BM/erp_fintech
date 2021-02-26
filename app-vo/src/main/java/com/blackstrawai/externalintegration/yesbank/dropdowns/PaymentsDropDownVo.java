package com.blackstrawai.externalintegration.yesbank.dropdowns;

import com.blackstrawai.common.BaseVo;
import com.blackstrawai.externalintegration.yesbank.beneficiary.BeneficiaryTypeVo;

import java.util.List;

public class PaymentsDropDownVo extends BaseVo {

  private List<BeneficiaryTypeVo> BeneficiaryDropDown;
  private List<DropDownVo> paymentsDropDown;

  public List<BeneficiaryTypeVo> getBeneficiaryDropDown() {
    return BeneficiaryDropDown;
  }

  public void setBeneficiaryDropDown(List<BeneficiaryTypeVo> beneficiaryDropDown) {
    BeneficiaryDropDown = beneficiaryDropDown;
  }

  public List<DropDownVo> getPaymentsDropDown() {
    return paymentsDropDown;
  }

  public void setPaymentsDropDown(List<DropDownVo> paymentsDropDown) {
    this.paymentsDropDown = paymentsDropDown;
  }
}

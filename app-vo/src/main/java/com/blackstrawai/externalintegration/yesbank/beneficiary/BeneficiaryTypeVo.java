package com.blackstrawai.externalintegration.yesbank.beneficiary;

import com.blackstrawai.externalintegration.yesbank.dropdowns.DropDownVo;
import com.blackstrawai.externalintegration.yesbank.dropdowns.PaymentBeneficiaryListVo;

import java.util.List;

public class BeneficiaryTypeVo extends DropDownVo {

  private List<PaymentBeneficiaryListVo> beneficiaryList;

  public List<PaymentBeneficiaryListVo> getBeneficiaryList() {
    return beneficiaryList;
  }

  public void setBeneficiaryList(List<PaymentBeneficiaryListVo> beneficiaryList) {
    this.beneficiaryList = beneficiaryList;
  }
}

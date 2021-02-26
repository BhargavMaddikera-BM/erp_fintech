package com.blackstrawai.externalintegration.compliance.taxilla;

public class TaxillaGstResponseVo {
	
	private TaxillaGsonVo result;

	public TaxillaGsonVo getResult() {
		return result;
	}

	public void setResult(TaxillaGsonVo result) {
		this.result = result;
	}

	@Override
	public String toString() {
		return "TaxillaGstResponseVo [result=" + result + "]";
	}
	
	

}

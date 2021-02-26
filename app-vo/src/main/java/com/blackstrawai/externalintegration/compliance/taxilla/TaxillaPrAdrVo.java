package com.blackstrawai.externalintegration.compliance.taxilla;

public class TaxillaPrAdrVo {
	private TaxillaAddr addr;
	private String ntr;

	public TaxillaAddr getAddr() {
		return addr;
	}

	public void setAddr(TaxillaAddr addr) {
		this.addr = addr;
	}

	public String getNtr() {
		return ntr;
	}

	public void setNtr(String ntr) {
		this.ntr = ntr;
	}

	@Override
	public String toString() {
		return "TaxillaPrAdrVo [addr=" + addr + ", ntr=" + ntr + "]";
	}

}

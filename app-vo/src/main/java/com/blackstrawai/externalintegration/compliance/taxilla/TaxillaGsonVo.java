package com.blackstrawai.externalintegration.compliance.taxilla;

import java.util.List;

public class TaxillaGsonVo {

	private String stjCd;
	private String lgnm;
	private String dty;
	private String stj;
	private List<TaxillaPrAdrVo> adadr;
	private String cxdt;
	private String gstin;
	private List<String> nba;
	private String lstupdt;
	private String rgdt;
	private String ctb;
	private TaxillaPrAdrVo pradr;
	private String tradeNam;
	private String ctjCd;
	private String sts;
	private String ctj;

	public String getStjCd() {
		return stjCd;
	}

	public String getLgnm() {
		return lgnm;
	}

	public String getDty() {
		return dty;
	}

	public String getStj() {
		return stj;
	}

	public List<TaxillaPrAdrVo> getAdadr() {
		return adadr;
	}

	public String getCxdt() {
		return cxdt;
	}

	public String getGstin() {
		return gstin;
	}

	public List<String> getNba() {
		return nba;
	}

	public String getLstupdt() {
		return lstupdt;
	}

	public String getRgdt() {
		return rgdt;
	}

	public String getCtb() {
		return ctb;
	}

	public TaxillaPrAdrVo getPradr() {
		return pradr;
	}

	public String getTradeNam() {
		return tradeNam;
	}

	public String getCtjCd() {
		return ctjCd;
	}

	public String getSts() {
		return sts;
	}

	public String getCtj() {
		return ctj;
	}

	public void setStjCd(String stjCd) {
		this.stjCd = stjCd;
	}

	public void setLgnm(String lgnm) {
		this.lgnm = lgnm;
	}

	public void setDty(String dty) {
		this.dty = dty;
	}

	public void setStj(String stj) {
		this.stj = stj;
	}

	public void setAdadr(List<TaxillaPrAdrVo> adadr) {
		this.adadr = adadr;
	}

	public void setCxdt(String cxdt) {
		this.cxdt = cxdt;
	}

	public void setGstin(String gstin) {
		this.gstin = gstin;
	}

	public void setNba(List<String> nba) {
		this.nba = nba;
	}

	public void setLstupdt(String lstupdt) {
		this.lstupdt = lstupdt;
	}

	public void setRgdt(String rgdt) {
		this.rgdt = rgdt;
	}

	public void setCtb(String ctb) {
		this.ctb = ctb;
	}

	public void setPradr(TaxillaPrAdrVo pradr) {
		this.pradr = pradr;
	}

	public void setTradeNam(String tradeNam) {
		this.tradeNam = tradeNam;
	}

	public void setCtjCd(String ctjCd) {
		this.ctjCd = ctjCd;
	}

	public void setSts(String sts) {
		this.sts = sts;
	}

	public void setCtj(String ctj) {
		this.ctj = ctj;
	}

	@Override
	public String toString() {
		return "TaxillaGsonVo [stjCd=" + stjCd + ", lgnm=" + lgnm + ", dty=" + dty + ", stj=" + stj + ", adadr=" + adadr
				+ ", cxdt=" + cxdt + ", gstin=" + gstin + ", nba=" + nba + ", lstupdt=" + lstupdt + ", rgdt=" + rgdt
				+ ", ctb=" + ctb + ", pradr=" + pradr + ", tradeNam=" + tradeNam + ", ctjCd=" + ctjCd + ", sts=" + sts
				+ ", ctj=" + ctj + "]";
	}

}

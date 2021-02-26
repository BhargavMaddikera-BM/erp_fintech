package com.blackstrawai.upload;

import java.util.List;

import com.blackstrawai.common.BaseVo;

public class ListContraUploadVo extends BaseVo{

	private List<ContraUploadVo> contra;

	public List<ContraUploadVo> getContra() {
		return contra;
	}

	public void setContra(List<ContraUploadVo> contra) {
		this.contra = contra;
	}

}

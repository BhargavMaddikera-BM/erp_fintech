package com.blackstrawai.response.gst;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.gst.PanGstVo;

public class PanGstResponse extends BaseResponse {

	private PanGstVo data;

	public PanGstVo getData() {
		return data;
	}

	public void setData(PanGstVo data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "PanGstResponse [data=" + data + "]";
	}

}

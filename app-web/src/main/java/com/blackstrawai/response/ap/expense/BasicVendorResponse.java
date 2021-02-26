package com.blackstrawai.response.ap.expense;

import java.util.List;

import com.blackstrawai.ap.dropdowns.BasicVendorVo;
import com.blackstrawai.common.BaseResponse;

public class BasicVendorResponse extends BaseResponse {

    List<BasicVendorVo> data;

    public List<BasicVendorVo> getData() {
        return data;
    }

    public void setData(List<BasicVendorVo> data) {
        this.data = data;
    }
}

package com.blackstrawai.response.ap.expense;

import java.util.List;

import com.blackstrawai.ap.dropdowns.BasicCustomerVo;
import com.blackstrawai.common.BaseResponse;

public class BasicCustomerResponse extends BaseResponse {

    List<BasicCustomerVo> data;

    public List<BasicCustomerVo> getData() {
        return data;
    }

    public void setData(List<BasicCustomerVo> data) {
        this.data = data;
    }
}

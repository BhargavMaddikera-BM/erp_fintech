package com.blackstrawai.response.ap.expense;

import java.util.List;

import com.blackstrawai.ap.dropdowns.BasicEmployeeVo;
import com.blackstrawai.common.BaseResponse;

public class BasicEmployeeResponse extends BaseResponse {

    List<BasicEmployeeVo> data;

    public List<BasicEmployeeVo> getData() {
        return data;
    }

    public void setData(List<BasicEmployeeVo> data) {
        this.data = data;
    }
}

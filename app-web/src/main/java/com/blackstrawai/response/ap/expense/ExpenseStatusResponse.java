package com.blackstrawai.response.ap.expense;

import com.blackstrawai.ap.expense.StatusVO;
import com.blackstrawai.common.BaseResponse;

import java.util.List;

public class ExpenseStatusResponse extends BaseResponse {

    List<StatusVO> data;

    public List<StatusVO> getData() {
        return data;
    }

    public void setData(List<StatusVO> data) {
        this.data = data;
    }
}

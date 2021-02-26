package com.blackstrawai.response.ap.expense;

import com.blackstrawai.ap.expense.ExpensesAddedVo;
import com.blackstrawai.common.BaseResponse;

import java.util.List;

public class ExpensesAddedResponse extends BaseResponse {

    List<ExpensesAddedVo> data;

    public List<ExpensesAddedVo> getData() {
        return data;
    }

    public void setData(List<ExpensesAddedVo> data) {
        this.data = data;
    }
}

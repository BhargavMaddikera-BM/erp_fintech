package com.blackstrawai.response.ap.expense;

import com.blackstrawai.ap.expense.ExpensesVO;
import com.blackstrawai.common.BaseResponse;

import java.util.List;

public class ExpensesResponse extends BaseResponse {

    List<ExpensesVO> data;

    public List<ExpensesVO> getData() {
        return data;
    }

    public void setData(List<ExpensesVO> data) {
        this.data = data;
    }
}

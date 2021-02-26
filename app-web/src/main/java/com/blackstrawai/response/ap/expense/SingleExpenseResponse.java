package com.blackstrawai.response.ap.expense;

import com.blackstrawai.ap.expense.ExpensesVO;
import com.blackstrawai.common.BaseResponse;

public class SingleExpenseResponse extends BaseResponse {

    ExpensesVO data;

    public ExpensesVO getData() {
        return data;
    }

    public void setData(ExpensesVO data) {
        this.data = data;
    }
}

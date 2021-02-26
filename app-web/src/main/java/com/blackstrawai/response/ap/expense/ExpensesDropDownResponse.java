package com.blackstrawai.response.ap.expense;

import com.blackstrawai.ap.dropdowns.ExpenseDropdownVo;
import com.blackstrawai.common.BaseResponse;

public class ExpensesDropDownResponse extends BaseResponse {

    ExpenseDropdownVo data;

    public ExpenseDropdownVo getData() {
        return data;
    }

    public void setData(ExpenseDropdownVo data) {
        this.data = data;
    }
}

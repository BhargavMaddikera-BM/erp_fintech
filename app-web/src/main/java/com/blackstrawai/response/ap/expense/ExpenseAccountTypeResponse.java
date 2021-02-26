package com.blackstrawai.response.ap.expense;

import com.blackstrawai.ap.expense.AccountTypeVO;
import com.blackstrawai.common.BaseResponse;

import java.util.List;

public class ExpenseAccountTypeResponse extends BaseResponse {

    List<AccountTypeVO> data;

    public List<AccountTypeVO> getData() {
        return data;
    }

    public void setData(List<AccountTypeVO> data) {
        this.data = data;
    }
}

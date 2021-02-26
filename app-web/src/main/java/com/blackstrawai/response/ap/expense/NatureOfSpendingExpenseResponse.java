package com.blackstrawai.response.ap.expense;

import com.blackstrawai.ap.expense.NatureOfSpendingVO;
import com.blackstrawai.common.BaseResponse;

import java.util.List;

public class NatureOfSpendingExpenseResponse extends BaseResponse {

    List<NatureOfSpendingVO> data;

	public List<NatureOfSpendingVO> getData() {
		return data;
	}

	public void setData(List<NatureOfSpendingVO> data) {
		this.data = data;
	}
}

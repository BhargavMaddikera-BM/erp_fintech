package com.blackstrawai.ar.dropdowns;

import java.util.List;

import com.blackstrawai.ar.receipt.ReceiptCommonVo;

public class ReceiptBulkDropdownVo {
	private int id;
	private String name;
	private int value;

	private List<ReceiptCommonVo> list;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	public List<ReceiptCommonVo> getList() {
		return list;
	}
	public void setList(List<ReceiptCommonVo> list) {
		this.list = list;
	}	
			
}

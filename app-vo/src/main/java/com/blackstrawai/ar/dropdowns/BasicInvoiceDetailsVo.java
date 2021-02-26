package com.blackstrawai.ar.dropdowns;

import java.util.List;

import com.blackstrawai.ar.creditnotes.BasicCreditNotesVo;
import com.blackstrawai.ar.receipt.BasicReceiptVo;

public class BasicInvoiceDetailsVo{
	private int id;
	private String name;
	private String parentName;
	private int parentId;
	private String invoiceNumber;
	private String value;
	private String type;
	private String dueAmount;
	private String invoiceAmount;
	private String receiptRefundableAmount;
	private String total;
	private String status;
	private int currencyId;
	private int customerId;
	private int organizationBasecurrencyId;
	private List<BasicCreditNotesVo> creditNotesList;
	private List<BasicReceiptVo> receiptsList;
	private boolean isLocal;
	
	
	
	public int getParentId() {
		return parentId;
	}
	public void setParentId(int parentId) {
		this.parentId = parentId;
	}
	public String getParentName() {
		return parentName;
	}
	public void setParentName(String parentName) {
		this.parentName = parentName;
	}
	public String getInvoiceNumber() {
		return invoiceNumber;
	}
	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}
	public String getInvoiceAmount() {
		return invoiceAmount;
	}
	public void setInvoiceAmount(String invoiceAmount) {
		this.invoiceAmount = invoiceAmount;
	}
	public int getOrganizationBasecurrencyId() {
		return organizationBasecurrencyId;
	}
	public void setOrganizationBasecurrencyId(int organizationBasecurrencyId) {
		this.organizationBasecurrencyId = organizationBasecurrencyId;
	}
	public int getCurrencyId() {
		return currencyId;
	}
	public void setCurrencyId(int currencyId) {
		this.currencyId = currencyId;
	}
	public boolean isLocal() {
		return isLocal;
	}
	public void setLocal(boolean isLocal) {
		this.isLocal = isLocal;
	}
	public List<BasicCreditNotesVo> getCreditNotesList() {
		return creditNotesList;
	}
	public void setCreditNotesList(List<BasicCreditNotesVo> creditNotesList) {
		this.creditNotesList = creditNotesList;
	}

	public List<BasicReceiptVo> getReceiptsList() {
		return receiptsList;
	}
	public void setReceiptsList(List<BasicReceiptVo> receiptsList) {
		this.receiptsList = receiptsList;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getTotal() {
		return total;
	}
	public void setTotal(String total) {
		this.total = total;
	}
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
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getDueAmount() {
		return dueAmount;
	}
	public void setDueAmount(String dueAmount) {
		this.dueAmount = dueAmount;
	}
	public int getCustomerId() {
		return customerId;
	}
	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}
	public String getReceiptRefundableAmount() {
		return receiptRefundableAmount;
	}
	public void setReceiptRefundableAmount(String receiptRefundableAmount) {
		this.receiptRefundableAmount = receiptRefundableAmount;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((creditNotesList == null) ? 0 : creditNotesList.hashCode());
		result = prime * result + currencyId;
		result = prime * result + customerId;
		result = prime * result + ((dueAmount == null) ? 0 : dueAmount.hashCode());
		result = prime * result + id;
		result = prime * result + ((invoiceAmount == null) ? 0 : invoiceAmount.hashCode());
		result = prime * result + ((invoiceNumber == null) ? 0 : invoiceNumber.hashCode());
		result = prime * result + (isLocal ? 1231 : 1237);
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + organizationBasecurrencyId;
		result = prime * result + parentId;
		result = prime * result + ((parentName == null) ? 0 : parentName.hashCode());
		result = prime * result + ((receiptsList == null) ? 0 : receiptsList.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + ((total == null) ? 0 : total.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BasicInvoiceDetailsVo other = (BasicInvoiceDetailsVo) obj;
		if (creditNotesList == null) {
			if (other.creditNotesList != null)
				return false;
		} else if (!creditNotesList.equals(other.creditNotesList))
			return false;
		if (currencyId != other.currencyId)
			return false;
		if (customerId != other.customerId)
			return false;
		if (dueAmount == null) {
			if (other.dueAmount != null)
				return false;
		} else if (!dueAmount.equals(other.dueAmount))
			return false;
		if (id != other.id)
			return false;
		if (invoiceAmount == null) {
			if (other.invoiceAmount != null)
				return false;
		} else if (!invoiceAmount.equals(other.invoiceAmount))
			return false;
		if (invoiceNumber == null) {
			if (other.invoiceNumber != null)
				return false;
		} else if (!invoiceNumber.equals(other.invoiceNumber))
			return false;
		if (isLocal != other.isLocal)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (organizationBasecurrencyId != other.organizationBasecurrencyId)
			return false;
		if (parentId != other.parentId)
			return false;
		if (parentName == null) {
			if (other.parentName != null)
				return false;
		} else if (!parentName.equals(other.parentName))
			return false;
		if (receiptsList == null) {
			if (other.receiptsList != null)
				return false;
		} else if (!receiptsList.equals(other.receiptsList))
			return false;
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;
		if (total == null) {
			if (other.total != null)
				return false;
		} else if (!total.equals(other.total))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "BasicInvoiceDetailsVo [id=" + id + ", name=" + name + ", parentName=" + parentName + ", parentId="
				+ parentId + ", invoiceNumber=" + invoiceNumber + ", value=" + value + ", type=" + type + ", dueAmount="
				+ dueAmount + ", invoiceAmount=" + invoiceAmount + ", total=" + total + ", status=" + status
				+ ", currencyId=" + currencyId + ", customerId=" + customerId + ", organizationBasecurrencyId="
				+ organizationBasecurrencyId + ", creditNotesList=" + creditNotesList + ", receiptsList=" + receiptsList
				+ ", isLocal=" + isLocal + "]";
	}

	
		
}

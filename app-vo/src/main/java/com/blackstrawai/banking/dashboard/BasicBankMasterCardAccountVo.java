package com.blackstrawai.banking.dashboard;

public class BasicBankMasterCardAccountVo {

		//Column : id 
		private Integer value;
		
		//Column :account_name
		private String name;
		
		private String authorisedPerson;
		
		private String cardNumber;
		
		private String issuerBank;
		
		private String accountCode;

		public Integer getValue() {
			return value;
		}

		public void setValue(Integer value) {
			this.value = value;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getAuthorisedPerson() {
			return authorisedPerson;
		}

		public void setAuthorisedPerson(String authorisedPerson) {
			this.authorisedPerson = authorisedPerson;
		}

		public String getCardNumber() {
			return cardNumber;
		}

		public void setCardNumber(String cardNumber) {
			this.cardNumber = cardNumber;
		}

		public String getIssuerBank() {
			return issuerBank;
		}

		public void setIssuerBank(String issuerBank) {
			this.issuerBank = issuerBank;
		}

		public String getAccountCode() {
			return accountCode;
		}

		public void setAccountCode(String accountCode) {
			this.accountCode = accountCode;
		}
		
		
}

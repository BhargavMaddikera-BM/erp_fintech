package com.blackstrawai.common;

import java.io.FileReader;
import java.util.Properties;

public class ConnectionHelper {

	private static String userMgmt_url;
	private static String userMgmt_userName;
	private static String userMgmt_password;

	private static String financeCommon_url;
	private static String financeCommon_userName;
	private static String financeCommon_password;

	private static String ap_url;
	private static String ap_userName;
	private static String ap_password;

	private static String ar_url;
	private static String ar_userName;
	private static String ar_password;

	private static String accounting_url;
	private static String accounting_userName;
	private static String accounting_password;

	private static String banking_url;
	private static String banking_userName;
	private static String banking_password;

	private static String journal_transaction_url;
	private static String journal_transaction_userName;
	private static String journal_transaction_password;
	
	private static String payroll_url;
	private static String payroll_userName;
	private static String payroll_password;

	private static String inventory_mgmt_url;
	private static String inventory_mgmt_userName;
	private static String inventory_mgmt_password;
	
	private static String epf_url;
	private static String epf_userName;
	private static String epf_password;
	
	private static void setDefault() {
		if (userMgmt_url == null) {
			userMgmt_url = "jdbc:mysql://127.0.0.1:3306/usermgmt";
		}
		if (userMgmt_userName == null) {
			userMgmt_userName = "root";
		}
		if (userMgmt_password == null) {
			userMgmt_password = "root";
		}

		if (financeCommon_url == null) {
			financeCommon_url = "jdbc:mysql://127.0.0.1:3306/finance_common";
		}
		if (financeCommon_userName == null) {
			financeCommon_userName = "root";
		}
		if (financeCommon_password == null) {
			financeCommon_password = "root";
		}

		if (ap_url == null) {
			ap_url = "jdbc:mysql://127.0.0.1:3306/accounts_payable";
		}
		if (ap_userName == null) {
			ap_userName = "root";
		}
		if (ap_password == null) {
			ap_password = "root";
		}

		if (ar_url == null) {
			ar_url = "jdbc:mysql://127.0.0.1:3306/accounts_receivable";
		}
		if (ar_userName == null) {
			ar_userName = "root";
		}
		if (ar_password == null) {
			ar_password = "root";
		}

		if (accounting_url == null) {
			accounting_url = "jdbc:mysql://127.0.0.1:3306/accounting";
		}
		if (accounting_userName == null) {
			accounting_userName = "root";
		}
		if (accounting_password == null) {
			accounting_password = "root";
		}
		
		if (banking_url == null) {
			banking_url = "jdbc:mysql://127.0.0.1:3306/banking";
		}
		if (banking_userName == null) {
			banking_userName = "root";
		}
		if (banking_password == null) {
			banking_password = "root";
		}
		
		if (journal_transaction_url == null) {
			journal_transaction_url = "jdbc:mysql://127.0.0.1:3306/journal_transaction";
		}
		if (journal_transaction_userName == null) {
			journal_transaction_userName = "root";
		}
		if (journal_transaction_password == null) {
			journal_transaction_password = "root";
		}
		
		if (payroll_url == null) {
			payroll_url = "jdbc:mysql://127.0.0.1:3306/payroll";
		}
		if (payroll_userName == null) {
			payroll_userName = "root";
		}
		if (payroll_password == null) {
			payroll_password = "root";
		}
		
		
		if (inventory_mgmt_url == null) {
			inventory_mgmt_url = "jdbc:mysql://127.0.0.1:3306/inventory_mgmt";
		}
		if (inventory_mgmt_userName == null) {
			inventory_mgmt_userName = "root";
		}
		if (inventory_mgmt_password == null) {
			inventory_mgmt_password = "root";
		}
		
		if (epf_url == null) {
			epf_url = "jdbc:mysql://127.0.0.1:3306/employee_provident_fund";
		}
		if (epf_userName == null) {
			epf_userName = "root";
		}
		if (epf_password == null) {
			epf_password = "root";
		}
	}

	private static ConnectionHelper connectionHelper;

	private ConnectionHelper() {

	}

	public static ConnectionHelper getInstance() {
		if (connectionHelper == null) {
			FileReader reader;
			try {
				reader = new FileReader("/decifer/config/app_config.properties");
				Properties p = new Properties();
				p.load(reader);
				userMgmt_url = p.getProperty("userMgmt_url");
				userMgmt_userName = p.getProperty("userMgmt_userName");
				userMgmt_password = p.getProperty("userMgmt_password");

				financeCommon_url = p.getProperty("financeCommon_url");
				financeCommon_userName = p.getProperty("financeCommon_userName");
				financeCommon_password = p.getProperty("financeCommon_password");

				accounting_url = p.getProperty("accounting_url");
				accounting_userName = p.getProperty("accounting_userName");
				accounting_password = p.getProperty("accounting_password");

				ap_url = p.getProperty("ap_url");
				ap_userName = p.getProperty("ap_userName");
				ap_password = p.getProperty("ap_password");

				ar_url = p.getProperty("ar_url");
				ar_userName = p.getProperty("ar_userName");
				ar_password = p.getProperty("ar_password");
				
				banking_url = p.getProperty("banking_url");
				banking_userName = p.getProperty("banking_userName");
				banking_password = p.getProperty("banking_password");
								
				journal_transaction_url = p.getProperty("journal_transaction_url");
				journal_transaction_userName = p.getProperty("journal_transaction_userName");
				journal_transaction_password = p.getProperty("journal_transaction_password"); 
				
				payroll_url = p.getProperty("payroll_url");
				payroll_userName = p.getProperty("payroll_userName");
				payroll_password = p.getProperty("payroll_password"); 
				

				inventory_mgmt_url = p.getProperty("inventory_mgmt_url");
				inventory_mgmt_userName = p.getProperty("inventory_mgmt_userName");
				inventory_mgmt_password = p.getProperty("inventory_mgmt_password"); 
				
				epf_url = p.getProperty("epf_url");
				epf_userName = p.getProperty("epf_userName");
				epf_password = p.getProperty("epf_password");
				
				setDefault();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				setDefault();
			}
			connectionHelper = new ConnectionHelper();
		}
		return connectionHelper;
	}

	public String getUserMgmtUrl() {
		return userMgmt_url;
	}

	public String getUserMgmtUserName() {
		return userMgmt_userName;
	}

	public String getUserMgmtPassword() {
		return userMgmt_password;
	}

	public String getFinanceCommonUrl() {
		return financeCommon_url;
	}

	public String getFinanceCommonUserName() {
		return financeCommon_userName;
	}

	public String getFinanceCommonPassword() {
		return financeCommon_password;
	}

	public String getAPUrl() {
		return ap_url;
	}

	public String getAPUserName() {
		return ap_userName;
	}

	public String getAPUserPassword() {
		return ap_password;
	}

	public String getARUrl() {
		return ar_url;
	}

	public String getARUserName() {
		return ar_userName;
	}

	public String getARUserPassword() {
		return ar_password;
	}
	
	public String getAccountingUrl() {
		return accounting_url;
	}

	public String getAccountingUserName() {
		return accounting_userName;
	}

	public String getAccountingUserPassword() {
		return accounting_password;
	}
	
	public String getBankingUrl() {
		return banking_url;
	}

	public String getBankingUserName() {
		return banking_userName;
	}

	public String getBankingUserPassword() {
		return banking_password;
	}

	public String getJournalTransactionUrl() {
		return journal_transaction_url;
	}

	public String getJournalTransactionUserName() {
		return journal_transaction_userName;
	}

	public String getJournalTransactionUserPassword() {
		return journal_transaction_password;
	}
	
	
	public String getPayrollUrl() {
		return payroll_url;
	}

	public String getPayrollUserName() {
		return payroll_userName;
	}

	public String getPayrollUserPassword() {
		return payroll_password;
	}
	
	public String getInventoryMgmtUrl() {
		return inventory_mgmt_url;
	}

	public String getInventoryMgmtName() {
		return inventory_mgmt_userName;
	}

	public String getInventoryMgmtPassword() {
		return inventory_mgmt_password;
	}

	public String getEpfUrl() {
		return epf_url;
	}

	public String getEpfUserName() {
		return epf_userName;
	}

	public String getEpfPassword() {
		return epf_password;
	}
	
}



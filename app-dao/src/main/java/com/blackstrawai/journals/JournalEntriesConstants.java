package com.blackstrawai.journals;

public class JournalEntriesConstants {

	//Ledgers 
	public static final String TDS_PAYABLE = "TDS Payable";
	public static final String TDS_RECEIVABLE_LEDGER = "TDS Receivable";
	public static final String DISCOUNT_LEDGER ="Product Expenses";
	public static final String ADJUSTMENT_LEDGER="Round Off";
	public static final String SUNDRY_DEBTORS_SECURED="Sundry Debtor - Secured";
	public static final String SUNDRY_DEBTORS_UNSECURED = "Sundry Debtor - UnSecured";
	public static final String LOANS_AND_ADVANCES_TO_EMPLOYEES = "Loans and advances to employees";
	public static final String INPUT_GST = "Input GST";
	public static final String OUTPUT_GST = "Output GST Control A/c";
	public static final String TAX_PAID_EXPENSE = "Tax Paid Expense";
	public static final String SUNDRY_CREDITORS_LOCAL_CURRENCY ="Sundry Creditors - Local currency";
	public static final String SUNDRY_CREDITORS_FOREIGN_CURRENCY  = "Sundry Creditors - Foreign currency";
	
	//Payrun ledgers
	public static final String SALARIES ="Salaries";	
	public static final String EMPLOYER_CONTRIBUTION_TO_PF ="Employer Contribution to PF";	
	public static final String EMPLOYER_CONTRIBUTION_TO_ESI ="Employer Contribution to ESI";	
	public static final String LEAVE_ENCASHMENT ="Leave Encashment";	
	public static final String BONUS ="Bonus";
	public static final String PF_PAYABLE_EMPLOYEE_CONTRIBUTION ="PF Payable - Employee Contribution";
	public static final String PF_PAYABLE_EMPLOYER_CONTRIBUTION_ADMIN_CHARGES ="PF Payable - Employer Contribution & admin charges";
	public static final String ESIC_PAYABLE_EMPLOYEE_CONTRIBUTION ="ESIC Payable - Employee Contribution";
	public static final String ESIC_PAYABLE_EMPLOYER_CONTRIBUTION_ADMIN_CHARGES ="ESIC Payable - Employer Contribution & admin charges";
	public static final String SALARIES_AND_BONUS_PAYABLE ="Salaries and Bonus Payable";
	public static final String POWER_AND_FUEL ="Power and fuel";
	public static final String CONVEYANCE ="Conveyance";
	public static final String TELEPHONE_CHARGES ="Telephone charges";
	public static final String OTHER_TAX_PAYABLE ="Other Tax Payable";
	
	//Modules
	public static final String MODULE_AP = "Accounts Payable";
	public static final String MODULE_MANUAL_JOURNALS = "Manual Journals";
	public static final String MODULE_AR = "Accounts Receivable";

	//Sub modules
	public static final String SUB_MODULE_INVOICE_WITH_BILLS = "Invoices with Bills";
	public static final String SUB_MODULE_INVOICE_WITHOUT_BILLS = "Invoices without Bills";
	public static final String SUB_MODULE_INVOICES = "Invoices";
	public static final String SUB_MODULE_RECEIPTS = "Receipts";
	public static final String SUB_MODULE_REFUNDS = "Refunds";
	public static final String SUB_MODULE_CONTRA = "Contra"; 
	public static final String SUB_MODULE_CREDIT_NOTES = "Credit Notes";
	public static final String SUB_MODULE_APPLICATION_OF_FUNDS = "Application of Funds"; 
	public static final String SUB_MODULE_PAYMENTS = "Payments";
	public static final String SUB_MODULE_PAY_RUN = "Payrun"; 

	
	
	//Voucher types
	public static final String VOUCHER_TYPE_ACCOUNTING_ASPECTS = "Accounting Entries";
	public static final String VOUCHER_TYPE_ACCOUNTS_RECEIVABLES_INVOICES = "Accounts Receivable-Invoice";
	public static final String VOUCHER_TYPE_ACCOUNTS_RECEIVABLES_RECEIPTS = "Accounts Receivable-Receipt";
	public static final String VOUCHER_TYPE_ACCOUNTS_RECEIVABLES_REFUNDS ="Accounts Receivable-Refund";
	public static final String VOUCHER_TYPE_ACCOUNTS_RECEIVABLES_CREDIT_NOTES ="Accounts Receivable-Credit Note";
	public static final String VOUCHER_TYPE_CONTRA = "Contra"; 
	public static final String VOUCHER_TYPE_APPLICATION_OF_FUNDS =	"Accounts Receivable-Application of Funds";
	public static final String VOUCHER_TYPE_PAYMENTS = "Payments";
	public static final String VOUCHER_TYPE_PAY_RUN = "Pay Run";
	
	//Subledger description
	public static final String SUBLEDGER_TYPE_CUSTOMER = "Customer";
	public static final String SUBLEDGER_TYPE_VENDOR= "Vendor";
	public static final String SUBLEDGER_TYPE_GST= "GST";
	public static final String SUBLEDGER_TYPE_TDS= "TDS";
	public static final String SUBLEDGER_TYPE_BANKING= "Banking";
	public static final String SUBLEDGER_TYPE_EMPLOYEE= "Employee";
	
	public static final String TYPE_DISCOUNT= "Discount";
	public static final String TYPE_TDS= "TDS";
	public static final String TYPE_ADJUSTMENT= "Adjustment";
	public static final String TYPE_PRODUCTS ="Products";
	
	//banktype
	public static final String BANK_TYPE_BANK_ACCOUNT="Bank Account";
	public static final String BANK_TYPE_CREDIT_CARD="Credit Card";
	public static final String BANK_TYPE_WALLET="Wallet";
	public static final String BANK_TYPE_CASH_ACCOUNT="Cash Account";

	
	//Others 
	public static final String TAX= "tax";
	public static final String INTEREST= "interest";
	public static final String INTEREST_DESCRIPTION= "Interest";
	public static final String PENALTY_DESCRIPTION= "Penalty";
	public static final String PENALTY= "penalty";
	public static final String OTHERS1= "others1";
	public static final String OTHERS2= "others2";
	public static final String OTHERS3= "others3";
	public static final String INVOICE = "Invoice";
	public static final String BILL = "Bill";
	public static final String ADVANCE = "Advance";
	public static final String ON_ACCOUNT  = "On Account";
	public static final String BANK_CHARGES  = "Bank Charges";
	public static final String TDS_Deducted  = "TDS Deducted";
	public static final String PAY_RUN_TYPE_EARNINGS  = "Earnings";
	public static final String PAY_RUN_TYPE_DEDUCTIONS  = "Deductions";
	public static final String PAY_RUN_TYPE_NET_PAYABLE  = "Net Payable";
	public static final String PAY_RUN_TYPE_NET_PAY  = "netPayable";
	public static final String PAYABLE_AMOUNT  = "Payable Amount";

	
	
	public static final String INSERT_JOURNAL_ENTRIES ="insert into journal_entries_transaction(transaction_no,original_transaction_no,transaction_line_no,module,sub_module,voucher_type,voucher_no,date_of_entry," + 
			"effective_date,invoice_date,particulars,customer_id,credit_note_no,customer_invoice_no,customer_po_no,vendor_id,debit_note_no,vendor_invoice_no,vendor_po_no,bank_id,gst_level,gst_percentage,tds_id," + 
			"employee_id,cost_center,billing_basis,location_id,gst,amount_debit_original_currency,amount_credit_original_currency,currency_id,conversion_factor,amount_debit,amount_credit,gst_input_credit," + 
			"nature,remarks,isSuperAdmin,user_id,organization_id,bank_type,ledger_id,voucher_naration,receipt_type) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	public static final String DELETE_JOURNAL_ENTRIES ="update journal_entries_transaction set status=?, update_ts=?  where organization_id = ? and module = ? and sub_module =? and transaction_no = ? and status=?";

	public static final String DELETE_JOURNAL_ENTRIES_WITH_VOUCHER_TYPE ="update journal_entries_transaction set status=?, update_ts=?  where organization_id = ? and module = ? and voucher_type =? and transaction_no = ? and status=? ";
	public static final String UPDATE_JOURNAL_ENTRIES_LEDGER_NAMES =" update journal_entries_transaction set particulars =? where ledger_id =? ";

	
	public static final String GET_VENDOR_FOR_SUNDRY_CREDITORS = "SELECT vgi.vendor_display_name , l6.display_name  FROM usermgmt.chart_of_accounts_level6_organization l6 " + 
			"join accounts_payable.vendor_general_information vgi on vgi.organization_id = l6.organization_id and vgi.id = l6.name and l6.description = 'Vendor'  " + 
			"where l6.organization_id = ? and   l6.chart_of_accounts_level5_id = (select id from usermgmt.chart_of_accounts_level5_organization l6 where organization_id = ? and name = ?)   ";

	public static final String GET_JOURNAL_ENTRIES_BW_DATES_FOR_ORGANIZATION ="select transaction_no,original_transaction_no,transaction_line_no,module,sub_module,voucher_type,voucher_no,date_of_entry,effective_date,invoice_date,\r\n" + 
			"particulars,(SELECT customer_display_name from accounts_receivable.customer_general_information cgi WHERE cgi.id=customer_id) AS customer_name,\r\n" + 
			"credit_note_no,customer_invoice_no,customer_po_no,\r\n" + 
			"(SELECT vendor_display_name from accounts_payable.vendor_general_information vgi WHERE vgi.id=vendor_id) AS vendor_name,\r\n" + 
			"debit_note_no,vendor_invoice_no,vendor_po_no,bank_id,gst_level,gst_percentage,\r\n" + 
			"(SELECT tds_rate_identifier FROM finance_common.tds WHERE tds.id=tds_id) AS tds_name, \r\n" + 
			"(SELECT concat(first_name,' ',last_name) FROM accounts_payable.employee e WHERE e.id=jet.employee_id) AS employee_name,cost_center,billing_basis,\r\n" + 
			"location_id,gst,amount_debit_original_currency,amount_credit_original_currency,\r\n" + 
			"(select name from usermgmt.currency_organization co WHERE co.id=currency_id) AS currency,conversion_factor,\r\n" + 
			"amount_debit,amount_credit,\r\n" + 
			"gst_input_credit,nature,remarks,\r\n" + 
			"(select concat(first_name,' ',last_name) from usermgmt.user usr WHERE usr.id=user_id) as user,bank_type,status, role_name, user_id " + 
			"from journal_entries_transaction jet where organization_id=? AND effective_date BETWEEN  ? AND ? AND STATUS IN(?)";
	public static final String GET_JE_GST_DETAILS_BY_LOCATION_GST ="select count(*)from gst_details where id=? and gst_no=?";
	public static final String GET_JE_GST_LOCATION_BY_LOCATION_GST ="select location_name from gst_location where id=? and gst_no=?";


// LEDGER BALANCE TABLE ENTRIES
	public static final String INSERT_INTO_LEDGER_BALANCE = "insert into ledger_balance(ledger_name, ledger_id, date, opening_balance, closing_balance, organization_id, user_id, role_name, status, journal_transaction_id, create_ts) values (?,?,?,?,?,?,?,?,?,?,?) ";
	public static final String GET_LEDGER_BALANCE_BY_ORG_ID_AND_LEDGER_NAME = "select id, ledger_name, ledger_id, date, opening_balance, closing_balance, organization_id, user_id, role_name, status, journal_transaction_id from ledger_balance where organization_id = ? and ledger_name = ?  order by create_ts desc  limit 1";
	public static final String UPDATE_LEDGER_BALANCE_BY_ID = "update ledger_balance set opening_balance=?, closing_balance=?, journal_transaction_id=?,  update_ts =? where id = ? ";
}

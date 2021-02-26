
package com.blackstrawai.attachments;

public class AttachmentsConstants {
	// The module name must be exactly the name of the folder that is created during
	// the Org creation
	public static final String MODULE_TYPE_CUSTOMER = "customer";
	public static final String MODULE_TYPE_VENDOR = "vendor";
	public static final String MODULE_TYPE_EMPLOYEE = "employee";
	public static final String MODULE_TYPE_AR_STATUTORY_BODY = "statutory_body";
	public static final String MODULE_TYPE_PO = "po";
	public static final String MODULE_TYPE_INVOICES_BILL = "invoices_bills";
	public static final String MODULE_TYPE_INVOICES_WITHOUT_BILL = "invoices_without_bills";
	public static final String MODULE_TYPE_EXPENSES = "expenses";
	public static final String MODULE_TYPE_REIMBUSEMENTS = "reimbursements";
	public static final String MODULE_TYPE_ORG_DOCUMENTS = "organization_documents";
	public static final String MODULE_TYPE_BANK_DOCUMENTS = "bank_documents";
	public static final String MODULE_TYPE_ACCOUNTING_ASPECTS = "accounting_aspects";
	public static final String MODULE_TYPE_EXPENSES_VENDOR = "expenses_vendor";
	public static final String MODULE_TYPE_OTHER_REIMBURSEMENTS = "others_reimbursements";
	public static final String MODULE_TYPE_DIGITAL_INVOICES = "digital_invoices";
	public static final String MODULE_TYPE_CHART_OF_ACCOUNTS = "chart_of_accounts";
	public static final String MODULE_TYPE_OTHER_DOCUMENTS = "other_documents";	
	public static final String MODULE_TYPE_PAYROLL = "payroll";
	public static final String MODULE_TYPE_VENDOR_PORTAL_BALANCE_CONFIRMATION = "vendor_portal_balance_confirmation";
	public static final String MODULE_TYPE_NON_ERP = "non_erp";
	public static final String MODULE_TYPE_BANK_CONTRA = "bank_contra";
	public static final String MODULE_TYPE_AP_PAYMENTS = "ap_payments";
	public static final String MODULE_TYPE_ACCOUNTS_RECEIVABLES_LUT = "accounts_receivables_lut";
	public static final String MODULE_TYPE_ACCOUNTS_RECEIVABLES_RECEIPTS = "accounts_receivables_receipt";
	public static final String MODULE_TYPE_ACCOUNTS_RECEIVABLES_REFUND = "accounts_receivables_refund";
	public static final String MODULE_TYPE_ACCOUNTS_RECEIVABLES_CREDIT_NOTE = "accounts_receivables_credit_note";
	public static final String MODULE_TYPE_ACCOUNTS_RECEIVABLES_INVOICE = "accounts_receivables_invoice";
	public static final String MODULE_TYPE_ACCOUNTS_RECEIVABLES_APPLY_CREDIT = "accounts_receivables_apply_credit";
	public static final String MODULE_TYPE_AR_INVOICE_TEMPLATE_LOGO = "accounts_receivables_invoice_logo";
	public static final String MODULE_TYPE_AR_INVOICE_TEMPLATE_SIGN = "accounts_receivables_invoice_sign";
	public static final String MODULE_TYPE_AR_CREDIT_NOTE_TEMPLATE_LOGO = "accounts_receivables_credit_note_logo";
	public static final String MODULE_TYPE_AR_CREDIT_NOTE_TEMPLATE_SIGN = "accounts_receivables_credit_note_sign";
	public static final String MODULE_TYPE_PAYROLL_PAYRUN = "payroll_payrun";
	public static final String MODULE_EMPLOYEE_PROVIDENT_FUND="employee_provident_fund";
	public static final String MODULE_EMPLOYEE_PROVIDENT_FUND_CHALLAN="challan";
	public static final String MODULE_EMPLOYEE_PROVIDENT_FUND_ECR="ecr";	
	public static final String MODULE_TYPE_AP_PO_TEMPLATE_LOGO = "accounts_payables_po_logo";
	public static final String MODULE_TYPE_AP_PO_TEMPLATE_SIGN = "accounts_payables_po_sign";
<<<<<<< HEAD
	public static final String MODULE_TYPE_VENDOR_CREDIT="accounts_payables_vendor_credit";
=======
	public static final String MODULE_TYPE_VENDOR_CREDIT="vendor_credit";
>>>>>>> 0a694d861a9a446c4b7c10a39cf5c7647a3014bd
		
	
	public static final String STATUTORY_BODY="statutory_body";
	public static final String GST="gst";
	public static final String INCOME_TAX="income_tax";
	public static final String API_BANKING="api_banking";
	public static final String VIRTUAL_BANK="virtual_bank";
	public static final String PRODUCT="product";



	// Queries for Attachments Table
	public static final String INSERT_INTO_ATTACHMENTS = "insert into document_mgmt.document_erp( type, type_id, version, file_name ,size , status, create_ts,document_type_id,organization_id,user_id,role_name,is_akshar) values (?,?,?,?,?,?,?,?,?,?,?,?)";
	public static final String GET_ATTACHMENT_FOR_TYPE_ID = "select id,file_name,size,document_type_id,is_akshar from document_mgmt.document_erp where type = ? and type_id = ? and status not in ('DEL')";
	public static final String CHANGE_ATTACHMENTS_STATUS = "update  document_mgmt.document_erp set status =? , update_ts =?,update_user_id=?,update_role_name=? where type =? and type_id = ?";
	public static final String CHANGE_SINGLE_ATTACHMENT_STATUS = "update  document_mgmt.document_erp set status =? , update_ts =?,update_user_id=?,update_role_name=? where id = ?";
	
	public static final String INSERT_INTO_PROFILE_ATTACHMENTS = "insert into document_mgmt.document_profile(email, file_name) values (?,?)";
	public static final String GET_PROFILE_ATTACHMENT_BY_EMAIL = "select id,email, file_name from document_mgmt.document_profile where email=?";
}

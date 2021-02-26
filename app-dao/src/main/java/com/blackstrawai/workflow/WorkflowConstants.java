package com.blackstrawai.workflow;

public class WorkflowConstants {
	//Modules
	public static final String MODULE_ACCOUNTING_ENTRIES = "Accounting Entries";
	public static final String MODULE_AP_INVOICE = "AP-Invoice";
	public static final String MODULE_AR_APPLICATION_OF_FUNDS = "AR-Application of Fund";
	public static final String MODULE_AR_CREDITNOTE = "AR-Credit Note";
	public static final String MODULE_AR_INVOICE = "AR-Invoice";
	public static final String MODULE_AR_LUT = "AR-LUT";
	public static final String MODULE_AR_RECEIPTS = "AR-Receipts";
	public static final String MODULE_AR_REFUND = "AR-Refund";
	public static final String MODULE_CONTRA = "Contra";
	public static final String MODULE_EMPLOYEE_REIMBURSEMENT = "Employee Reimbursement";
	public static final String MODULE_EXPENSES_NON_VENDOR = "Expenses-Non Vendor";
	public static final String MODULE_PAYMENTS = "Payments";
	public static final String MODULE_PAYROLL = "Payroll";
	public static final String MODULE_PURCHASE_ORDER = "Purchase Order";
	public static final String MODULE_RECONCIALLATION = "Reconciliation";
	public static final String MODULE_RECONCILLATION_BANK_STATEMENT = "Reconciliation-Bank Statement";
	public static final String MODULE_RECONCILLSTION_GL = "Reconciliation-GL";
	public static final String MODULE_VENDOR_PORTAL_BALANCE_CONFIRMATION = "Vendor Portal-Balance Confirmation";
	public static final String MODULE_BANKING = "Banking";
	
	
	//Conditions
	public static final String CONDITION_INVOICE_VALUE = "Invoice Value";
	public static final String CONDITION_LOCATION = "Location";
	public static final String CONDITION_GST = "GST";
	public static final String CONDITION_VENDOR = "Vendor";
	public static final String CONDITION_TX_AMT = "Transaction Amount";
	
	//Choices
	public static final String CHOICE_EQUAL_TO = "Is equal to";
	public static final String CHOICE_GREATER_THAN = "Is greater than";
	public static final String CHOICE_LESS_THAN = "Is less than";
	public static final String CHOICE_IN_RANGE = "Is in range";
	public static final String CHOICE_IS = "Is";
	public static final String CHOICE_IS_NOT = "Is not";
	public static final String CHOICE_ENABLED = "Enabled";
	public static final String CHOICE_DISABLED = "Disabled";
	
	//Approval Types
	public static final String APPROVAL_TYPE_ANYONE_CAN_APPROVE = "Anyone can approve it";
	public static final String APPROVAL_TYPE_EVERYONE_SHOULD_APPROVE = "Everyone should approve";
	public static final String APPROVAL_TYPE_EVERYONE_APPROVES_SEQUENTIALY = "Everyone approves sequentially";

	//Work flow Operation
	public static final String WORKFLOW_OPERATION_CREATE = "WF_CREATE";
	public static final String WORKFLOW_OPERATION_UDATE = "WF_UDATE";
	public static final String WORKFLOW_OPERATION_APPROVED = "WF_APROVED";
}


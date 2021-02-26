package com.blackstrawai.ar;

public class ReceiptConstants {

	public static final String AR_RECEIPT_TYPE_INVOICE = "Invoice";
	public static final String AR_RECEIPT_TYPE_ON_ACCOUNT = "On Account";
	public static final String AR_RECEIPT_TYPE_ADVANCE_PAYMENT = "Advance Payment";
	public static final String AR_RECEIPT_TYPE_CREATE_NEW_ADVANCE = "Create New Advance";
	public static final String AR_RECEIPT_TYPE_CREATE_NEW_ON_ACCOUNT = "Create New On Account";

	public static final int AR_RECEIPT_ON_ACCOUNT_REFERENCE = -42798;
	public static final int AR_RECEIPT_ADVANCE_PAYMENT_REFERENCE = -42799;
	public static final String AR_RECEIPT_TYPE_CREDIT_NOTE = "Credit Note";
	public static final String AR_RECEIPT_TYPE_CUSTOMER_PAYMENTS = "Customer Receipts";
	public static final String AR_RECEIPT_TYPE_VENDOR_REFUNDS = "Vendor Refunds";
	public static final String AR_RECEIPT_TYPE_OTHER_RECEIPTS = "Other Receipts";
	public static final String RECEIPT_DEFAULT_INVOICE_AMOUT_LEDGER = "Sundry Debtor - Secured";
	public static final String RECEIPT_DEFAULT_TDS_RECEIVABLE_LEDGER = "TDS Receivable";
	public static final String RECEIPT_DEFAULT_BANK_CHARGES_LEDGER = "Bank Charges";
	public static final String RECEIPT_TYPE_CUSTOMER_PAYMENTS = "Customer Receipts";
	public static final String RECEIPT_TYPE_VENDOR_REFUNDS = "Vendor Refunds";
	public static final String RECEIPT_TYPE_OTHER_RECEIPTS = "Other Receipts";
	public static final String RECEIPT_CONTACT_TYPE_CUSTOMER = "Customer";
	public static final String RECEIPT_CONTACT_TYPE_VENDOR = "Vendor";
	public static final String RECEIPT_CONTACT_TYPE_STATUTORY_BODY = "Statutory";
	public static final String RECEIPT_CONTACT_TYPE_EMPLOYEE = "Employee";

	public static final String INSERT_INTO_RECEIPT_ORGANIZATION = "INSERT INTO receipt (receipt_type, receipt_no, receipt_date, customer_id, vendor_id, contact_id,contact_type, bank_id, bank_type, currency_id, amount, total, organization_id, is_bulk,settings_data,notes, user_id, role_name,exchange_rate,add_notes,status,adjusted_amount,difference_amount,record_bill_details,general_ledger_data) "
			+ "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
	public static final String INSERT_INTO_VENDOR_CREDIT_NOTE = "INSERT INTO vendor_refund_credit_note_details (vendor_credit_note_id, vendor_credit_note_no, credit_amount, refund_amount, status, receipt_id) VALUES (?,?,?,?,?,?)";
	
	//public static final String GET_VENDOR_CREDIT_BY_RECEIPT_ID = "select "
	
	public static final String UPDATE_RECEIPT_ORGANIZATION = "update receipt set receipt_type=?, receipt_no=?, receipt_date=?, customer_id=?, vendor_id=?, contact_id=?,contact_type=?, bank_id=?, bank_type=?, currency_id=?, amount=?, total=?, organization_id=?, is_bulk=?,settings_data=?,notes=?, update_user_id=?, update_role_name=?,exchange_rate=?,add_notes=?,status=?,adjusted_amount=?,difference_amount=?,record_bill_details=?,general_ledger_data=? where id=?";
	public static final String INSERT_INTO_RECEIPT_BULK_DETAILS = "INSERT INTO receipt_bulk_details (type_id, reference_id,reference_type, amount,receipt_id,data,status,invoice_due_amount,refundable_amount) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
	public static final String GET_RECEIPTS_FOR_ORGNIZATION_USER_ROLE = "SELECT id,receipt_type,"
			+ "(select customer_display_name from customer_general_information cgi where cgi.id=rec.customer_id) as customer_name,"
			+ "(select vendor_display_name from accounts_payable.vendor_general_information vgi where vgi.id=rec.vendor_id) as vendor_name,"
			+ "receipt_date,bank_type,amount,currency_id,"
			+ "STATUS,receipt_no,contact_id,contact_type FROM receipt rec where organization_id = ? and rec.user_id=? and rec.role_name=? and status not in ('DEL') ORDER BY create_ts DESC";
	public static final String GET_RECEIPTS_FOR_ORGNIZATION = "SELECT id,receipt_type,"
			+ "(select customer_display_name from customer_general_information cgi where cgi.id=rec.customer_id) as customer_name,"
			+ "(select vendor_display_name from accounts_payable.vendor_general_information vgi where vgi.id=rec.vendor_id) as vendor_name,"
			+ "receipt_date,bank_type,amount,currency_id,"
			+ "STATUS,receipt_no,contact_id,contact_type FROM receipt rec where organization_id = ? and status not in ('DEL') ORDER BY create_ts DESC";
	public static final String GET_TOTAL_ADVANCE_ON_ACCOUNT_AMOUNT_FOR_CUSTOMER_CURRENCY = "select SUM(refundable_amount) from receipt_bulk_details rbd join receipt rec on rbd.receipt_id=rec.id where rec.organization_id=? and rec.customer_id=? AND rec.receipt_type=? AND rec.currency_id=? AND rec.STATUS NOT IN(?,?) and rbd.reference_type IN(?,?) AND rbd.status=?";
	public static final String GET_TOTAL_ADVANCE_ON_ACCOUNT_AMOUNT_FOR_CUSTOMER = "select SUM(refundable_amount) from receipt_bulk_details rbd join receipt rec on rbd.receipt_id=rec.id where rec.organization_id=? and rec.customer_id=? AND rec.receipt_type=? AND rec.STATUS NOT IN(?,?) and rbd.reference_type IN(?,?) AND rbd.status=? ";
	public static final String GET_RECEIPT_BY_ID = "select id,receipt_type, receipt_no, receipt_date, customer_id, vendor_id, contact_id,contact_type, bank_id, bank_type, currency_id, amount, total, organization_id, is_bulk,settings_data,notes, user_id, role_name,status,exchange_rate,add_notes,adjusted_amount,difference_amount,record_bill_details,general_ledger_data,create_ts from receipt where id = ? and status not in ('DEL')";
	public static final String GET_MINIMAL_RECEIPT_BY_ID = "select id,receipt_type, receipt_no,status from receipt where id = ? and status not in ('DEL')";
	public static final String GET_BULK_DETAILS_FOR_RECEIPT = "select id,type_id, reference_id,reference_type, amount,receipt_id,data,invoice_due_amount,status from receipt_bulk_details where receipt_id=? and status not in ('DEL')";

	public static final String GET_RECEIPT_NO_BY_ID = "select  receipt_no from receipt where id = ?";

	public static final String GET_INVOICES_FOR_RECEIPT = "select (select aigi.id from invoice_general_information aigi where aigi.id=rbd.reference_id) as invoice_id,"
			+ "(select invoice_number from invoice_general_information aigi where aigi.id=rbd.reference_id) as invoice_no,"
			+ "(select aigi.total from invoice_general_information aigi where aigi.id=rbd.reference_id) as total_invoice_amount, "
			+ "(select aigi.balance_due from invoice_general_information aigi where aigi.id=rbd.reference_id) as balance_due,"
			+ "(select aigi.currency_id from invoice_general_information aigi where aigi.id=rbd.reference_id) as currency_id,invoice_due_amount,"
			+ "(select aigi.customer_id from invoice_general_information aigi where aigi.id=rbd.reference_id) as customer_id,refundable_amount"
			+ " from receipt_bulk_details rbd where receipt_id=? AND reference_type=? and status not IN('DEL') ";
	public static final String GET_CREDITNOTES_FOR_RECEIPT = "SELECT reference_id,"
			+ "(select cn.credit_note_number from credit_note cn where cn.id=rbd.reference_id) as invoice_no,"
			+ "(select cn.total from credit_note cn where cn.id=rbd.reference_id) as total_invoice_amount, "
			+ "(select cn.balance_due from credit_note cn where cn.id=rbd.reference_id) as balance_due,invoice_due_amount"
			+ " from receipt_bulk_details rbd where receipt_id=? AND reference_type=? and status not IN('DEL')  and receipt_id IN(SELECT id from receipt WHERE id=rbd.receipt_id and customer_id=? and currency_id=?) ";

	public static final String GET_ADVANCES_FOR_RECEIPT = "SELECT rec.customer_id,rec.currency_id,rbd.refundable_amount,rbd.reference_type FROM receipt rec join receipt_bulk_details rbd on rec.id=rbd.receipt_id WHERE  rbd.receipt_id = ? and rbd.reference_type IN(?,?)  and rbd.status =?  and rec.STATUS NOT IN('DEL')";

	public static final String CHECK_RECEIPT_FOR_ORGANIZATION = "SELECT * FROM receipt WHERE organization_id=? and receipt_no=?";
	public static final String GET_ONACCOUNT_OR_ADV_RECEIPTS_BY_CUSTOMER_CURRENCY = "select rec.id,rec.receipt_no,rbd.refundable_amount,rbd.reference_type from receipt_bulk_details rbd join receipt rec on rbd.receipt_id=rec.id where rec.organization_id=? and rec.customer_id=? AND rec.receipt_type=? AND rec.currency_id=? AND rec.STATUS IN(?,?,?) and rbd.reference_type IN(?) AND rbd.status=? AND rbd.refundable_amount>0 ";
	public static final String DELETE_RECEIPT_BULK_DETAILS_BY_ID = "update accounts_receivable.receipt_bulk_details set  status=?, update_ts=? where id = ? ";
	public static final String DELETE_RECEIPT_BULK_DETAILS_BY_RECEIPT_ID = "delete from accounts_receivable.receipt_bulk_details where receipt_id = ? ";
	public static final String GET_RECEIPTS_BY_CUSTOMER_ID = "select rec.id,rec.receipt_no,rbd.invoice_due_amount,rbd.refundable_amount,rbd.reference_type from receipt_bulk_details rbd join receipt rec on rbd.receipt_id=rec.id where rec.organization_id=? and rec.customer_id=? AND rec.receipt_type=?  AND rec.currency_id=? AND rec.STATUS NOT IN(?,?,?)  AND rbd.status=? AND rbd.reference_type IN(?,?) AND refundable_amount>0";
	public static final String GET_VENDOR_REFUND_INVOICES = "select  id, vendor_invoice_id, vendor_invoice_no, bill_amount, refund_amount, status,receipt_id from accounts_receivable.vendor_refund_receipt_details where receipt_id= ? and status not in ('DEL')";
	public static final String INSERT_INTO_VENDOR_REFUND_INVOICES = "insert into  accounts_receivable.vendor_refund_receipt_details (vendor_invoice_id, vendor_invoice_no, bill_amount, refund_amount, status, receipt_id, create_ts) values (?,?,?,?,?,?,?) ";
	public static final String UPDATE_VENDOR_REFUND_INVOICES = "update accounts_receivable.vendor_refund_receipt_details set vendor_invoice_id=?, vendor_invoice_no=?, bill_amount=?, refund_amount=?, status=?, receipt_id=?, update_ts=? where id = ? ";
	public static final String DELETE_VENDOR_REFUND_INVOICES = "update accounts_receivable.vendor_refund_receipt_details set  status=?, update_ts=? where id = ? ";
	public static final String DELETE_VENDOR_REFUND_BY_RECEIPT_ID = "delete from accounts_receivable.vendor_refund_receipt_details where receipt_id = ? ";
	public static final String GET_RECEIPT_BY_INVOICE_ID = "SELECT rec.id,rec.receipt_no,rbd.refundable_amount FROM receipt rec join receipt_bulk_details rbd on rec.id=rbd.receipt_id and rbd.refundable_amount>0 where rbd.reference_id = ? and rbd.reference_type=? and rbd.status NOT IN(?) and rec.STATUS IN(?,?,?)";
	public static final String GET_ONACC_OR_ADV_PAYMENTS_FOR_RECEIPT = "SELECT reference_id,(select receipt_no FROM receipt rec WHERE rec.id=rbd.reference_id ) AS receipt_no,reference_type,invoice_due_amount,(select refundable_amount FROM receipt_bulk_details bulk where bulk.receipt_id=rbd.reference_id and bulk.reference_type=?) FROM receipt_bulk_details rbd WHERE rbd.receipt_id=? AND reference_type=? and receipt_id IN(SELECT id from receipt WHERE id=rbd.receipt_id and customer_id=? and currency_id=?)";

	// Balance Update
	public static final String GET_ON_ACCOUNT_ADV_PAYMENT_AMOUNT_FROM_RECEIPT = "SELECT sum(invoice_due_amount) FROM receipt_bulk_details rbd WHERE receipt_id=? AND  reference_type=? and type_id IN (SELECT id FROM finance_common.receipt_bulk_detail_type where NAME=?) and receipt_id=(select id from receipt where id=rbd.receipt_id and status NOT IN(?,?,?)) and status not in ('DEL') ";
	public static final String UPDATE_RECEIPT_DUE_BALANCE = "update receipt set advance_due_amount=?,on_account_due_amount=?,applied_funds=?,status=?,update_ts=? where id=?";
	public static final String GET_ALL_ACTIVE_RECEIPTS_FOR_ORG_INVOICE = "SELECT id FROM receipt rec WHERE  rec.id IN(SELECT receipt_id FROM receipt_bulk_details WHERE reference_id=? and status not IN('DEL')) and STATUS IN (?,?,?,?)";
	public static final String GET_TOTAL_FOR_INVOICE_FROM_RECEIPTS = "SELECT sum(invoice_due_amount) FROM receipt_bulk_details rbd WHERE reference_id=? and receipt_id IN (SELECT id FROM receipt WHERE id=rbd.receipt_id and STATUS IN (?,?,?,?) ) and reference_type=? and status not IN('DEL')";
	public static final String GET_TOTAL_OF_CN_FOR_RECEIPT = "SELECT SUM(invoice_due_amount) FROM receipt_bulk_details rbd where receipt_id=? AND reference_type=? and status not IN('DEL')";
	public static final String GET_TOTAL_AMOUNT_FOR_MODULE = "SELECT SUM(invoice_due_amount) FROM receipt_bulk_details rbd where reference_id=? AND reference_type=? and type_id=(select id from finance_common.receipt_bulk_detail_type where name=?) and receipt_id IN(select id from receipt rc where id=rbd.receipt_id and status NOT IN(?,?,?)) and status not IN('DEL')";

	public static final String GET_VENDOR_NAME_BY_ID = "select vendor_display_name from accounts_payable.vendor_general_information vgi where vgi.id=?";
	public static final String GET_EMPLOYEE_NAME_BY_ID = "select CONCAT(first_name,' ',last_name) from accounts_payable.employee where id=?";
	public static final String GET_STATUTORY_BODY_NAME_BY_ID = "select name from usermgmt.statutory_body where id=?";
	public static final String GET_CUSTOMER_NAME_BY_ID = "select customer_display_name from customer_general_information cgi where cgi.id=?";
	public static final String GET_TOTAL_FOR_INVOICE_FROM_REFUND = "SELECT sum(refund_amount) from vendor_refund_receipt_details vrrd WHERE receipt_id = (select id from receipt rc where rc.id=vrrd.receipt_id and status=? ) and vendor_invoice_id=? AND STATUS=? and status not IN('DEL')";
	public static final String GET_ALL_REFUNDS_OF_INVOICE = "SELECT id,"
			+ "(select receipt_no from receipt rc where rc.id=vrrd.receipt_id) AS reference,"
			+ "(SELECT symbol from usermgmt.currency_organization WHERE id="
			+ "(select currency_id from receipt rc where rc.id=vrrd.receipt_id)) AS currency_symbol,"
			+ "refund_amount from vendor_refund_receipt_details vrrd WHERE vendor_invoice_id=? AND vrrd.receipt_id ="
			+ " (select id from receipt rc WHERE rc.id=vrrd.receipt_id and STATUS NOT IN (?,?) ) "
			+ "AND STATUS=? AND refund_amount>0";

	public static final String GET_ON_ACCOUNT_ADVANCE_RECEIPTS = "select rec.id,rec.receipt_no,rbd.refundable_amount from receipt_bulk_details rbd join receipt rec on rbd.receipt_id=rec.id  where rec.organization_id=? and rec.customer_id=? AND rec.receipt_type=? AND rec.currency_id=? AND  rec.STATUS IN(?,?,?) and rbd.reference_type IN(?) AND rbd.status=? AND rbd.refundable_amount>0 ";
	public static final String GET_RECEIPT_BY_REFERENCE_ID = "SELECT rec.id,rec.receipt_no,rbd.refundable_amount FROM receipt rec join receipt_bulk_details rbd on rec.id=rbd.receipt_id where rec.id = ?";
	public static final String GET_MINIMAL_RECEIPT_DETAILS_BY_REFERENCE = "select rec.id,rec.receipt_no,rbd.refundable_amount from receipt_bulk_details rbd join receipt rec on rbd.receipt_id=rec.id where rec.organization_id=? and rec.customer_id=? AND rec.receipt_type=?  AND rec.currency_id=?  AND rbd.status=? AND rbd.reference_type IN(?) and rec.id=? AND rbd.reference_id=?";
	public static final String GET_ALL_INVOICES_FOR_RECEIPT = "select rbd.id,rbd.reference_id,rbd.invoice_due_amount,rbd.reference_type from receipt_bulk_details rbd where receipt_id=? AND reference_type IN (?,?,?) and status not IN('DEL') ";
	public static final String UPDATE_RECEIPT_INVOICE_REFUND_AMOUNT = "update receipt_bulk_details set refundable_amount=?,update_ts=? where id=?";
}
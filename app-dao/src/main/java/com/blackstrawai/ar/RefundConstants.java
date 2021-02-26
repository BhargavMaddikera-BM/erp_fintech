package com.blackstrawai.ar;

public class RefundConstants {
	
	public static final String REFUND_TYPE_RECEIPT="Receipt";
	public static final String REFUND_TYPE_CREDIT_NOTES="Credit Notes";
	
	public static final String INSERT_INTO_REFUND_ORGANIZATION = "insert into refund(refund_date,customer_id,invoice_id,payment_mode_id,refund_reference,amount,ledger_id,ledger_name,location_id,is_registered,gst_number,currency_id,currency_symbol,organization_id,user_id,isSuperAdmin,payment_account_id,role_name,credit_note_id,receipt_id,refund_type_id,exchange_rate)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	public static final String CHECK_REFUND_ORGANIZATION = "select * from refund where organization_id=? and refund_reference=?";
	public static final String UPDATE_REFUND_ORGANIZATION = "update refund set refund_date=?,customer_id=?,invoice_id=?,payment_mode_id=?,refund_reference=?,amount=?,ledger_id=?,ledger_name=?,location_id=?,is_registered=?,gst_number=?,currency_id=?,currency_symbol=?,organization_id=?,update_user_id=?,isSuperAdmin=?,update_ts=?,payment_account_id=?,update_role_name=?,credit_note_id=?,receipt_id=?,refund_type_id=?,exchange_rate=? where id=?";
	public static final String DELETE_REFUND_ORGANIZATION = "update refund set status=?,update_ts=?,update_user_id=?,update_role_name=?  where id=?";
	public static final String GET_REFUND_BY_ORGANIZATION_USER_ROLE = "select id,refund_date,customer_id,"
			+ "(select customer_display_name from customer_general_information cgi where cgi.id=rfnd.customer_id) as customer_name,"
			+ "invoice_id,"
			+ "(select invoice_number from invoice_general_information aigi where aigi.id=rfnd.invoice_id) as invoice_reference,"
			+ "payment_mode_id,"
			+ "(select name from finance_common.payment_mode pmtmode where pmtmode.id=rfnd.payment_mode_id) as payment_mode,"
			+ "refund_reference,amount,"
			+ "ledger_id,ledger_name,currency_symbol,organization_id,user_id,isSuperAdmin,create_ts,update_ts,status,role_name from refund rfnd where organization_id=? and user_id=? and role_name=? ORDER BY create_ts DESC";
	
	public static final String GET_REFUND_BY_ORGANIZATION = "select id,refund_date,customer_id,"
			+ "(select customer_display_name from customer_general_information cgi where cgi.id=rfnd.customer_id) as customer_name,"
			+ "invoice_id,"
			+ "(select invoice_number from invoice_general_information aigi where aigi.id=rfnd.invoice_id) as invoice_reference,"
			+ "payment_mode_id,"
			+ "(select name from finance_common.payment_mode pmtmode where pmtmode.id=rfnd.payment_mode_id) as payment_mode,"
			+ "refund_reference,amount,"
			+ "ledger_id,ledger_name,currency_symbol,organization_id,user_id,isSuperAdmin,create_ts,update_ts,status,role_name from refund rfnd where organization_id=? ORDER BY create_ts DESC";
	
	public static final String GET_REFUND_BY_ID_ORGANIZATION = "select id,refund_date,customer_id,"
			+ "(select customer_display_name from customer_general_information cgi where cgi.id=rfnd.customer_id) as customer_name,"
			+ "invoice_id,"
			+ "(select invoice_number from invoice_general_information aigi where aigi.id=rfnd.invoice_id) as invoice_reference,"
			+ "payment_mode_id,"
			+ "(select name from finance_common.payment_mode pmtmode where pmtmode.id=rfnd.payment_mode_id) as payment_mode,"
			+ "refund_reference,amount,"
			+ "ledger_id,ledger_name,location_id,is_registered,gst_number,currency_id,currency_symbol,organization_id,user_id,isSuperAdmin,create_ts,update_ts,status,payment_account_id,role_name,credit_note_id,receipt_id,refund_type_id,exchange_rate"
			+ " from refund rfnd where rfnd.id=?";

	public static final String GET_TOTAL_FOR_INVOICE_FROM_REFUND="SELECT sum(invoice_amount) from payments_non_core_customer_refund_details pncr WHERE invoice_id=? AND STATUS=? and reference_type =? and payments_non_core_id = (select id from payments_non_core where id=pncr.payments_non_core_id and status=? ) and status not IN('DEL')";
	public static final String GET_TOTAL_FOR_INVOICE_FROM_REFUND_FOR_RECEIPT="SELECT sum(invoice_amount) from payments_non_core_customer_refund_details pncr WHERE invoice_id=? and reference_id=? AND STATUS=? and reference_type IN(?,?,?) and payments_non_core_id = (select id from payments_non_core where id=pncr.payments_non_core_id and STATUS=? ) and status not IN('DEL')";
	public static final String GET_TOTAL_FOR_CREDIT_NOTE_FROM_AF="SELECT sum(adjustment_amount) from apply_credit_credits WHERE reference_id=? AND credit_type=? AND STATUS=?";
	public static final String GET_INVOICE_FOR_REFUND="SELECT invoice_id from refund WHERE id=?";
	public static final String GET_INVOICES_FOR_REFUND="SELECT (select aigi.id from invoice_general_information aigi where aigi.id=rf.invoice_id) as invoice_id," + 
			"(select invoice_number from invoice_general_information aigi where aigi.id=rf.invoice_id) as invoice_no," + 
			"(select aigi.currency_id from invoice_general_information aigi where aigi.id=rf.invoice_id) as currency_id," + 
			"(SELECT name FROM finance_common.invoice_type it where it.id=(select aigi.invoice_type_id from invoice_general_information aigi where aigi.id=rf.invoice_id)) as invoice_type," + 
			"amount,(SELECT name FROM finance_common.refund_types rt where rt.id=rf.refund_type_id),credit_note_id,receipt_id from refund rf where rf.id=?";
	public static final String GET_ALL_REFUND_ELIGIBLE_INVOICES_FOR_ORG = "select id,invoice_number, balance_due,total,currency_id,(SELECT name FROM finance_common.invoice_type it where it.id=aigi.invoice_type_id) as invoice_type from invoice_general_information aigi where organization_id =? AND customer_id=? AND STATUS IN (?,?)";
	public static final String GET_CREDITNOTES_FOR_INVOICE = "SELECT id,credit_note_number,balance_due,(SELECT currency_id FROM invoice_general_information WHERE id=original_invoice_id) as currency_id FROM credit_note where original_invoice_id=?";
	public static final String GET_RECEIPTS_FOR_INVOICE = "SELECT id,receipt_no,currency_id FROM receipt where id IN(select receipt_id FROM invoice_category_receipt WHERE invoice_on_account_id=?)";
	public static final String GET_INVOICE_AMOUT_FROM_RECEIPTS_FOR_INVOICE = "select invoice_amount FROM invoice_category_receipt WHERE invoice_on_account_id=?";
}



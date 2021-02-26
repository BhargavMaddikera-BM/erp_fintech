package com.blackstrawai.ar;

public class ArInvoiceConstants {

	public static final String MODULE_TYPE_AR_INVOICES = "ar_invoice";
	public static final String MODULE_TYPE_AR_CREDITNOTE = "ar_creditnote";
	public static final String MODULE_TYPE_AR_RECEIPT = "ar_receipt";

	public static final String INSERT_INTO_INVOICE_GENERAL_INFO = "insert into invoice_general_information ( org_location, org_gst, invoice_type_id, customer_id, customer_ledger_id, customer_ledger_name, customer_gst, receiver_name, place_of_supply_id, supply_service_id, reverse_charge, sold_by_ecommerce, ecommerce_gstin, wop_under_lut, lut_number, lut_date, po_number, invoice_number, invoice_date, payment_terms_id, due_date, item_rates_tax_id, export_type_id, port_code, shipping_bill_no, shipping_bill_date, sub_total, tds_id, tds_value, discount_ledger_id, discount_ledger_name, discount_value, adjustment_value, adjustment_ledger_id, adjustment_ledger_name, total, terms_conditions, bank_details_id, bank_type, currency_id, exchange_rate, status, create_ts,  user_id, organization_id, is_Super_Admin, role_name,adjustment_account_level,discount_account_level,balance_due,is_registered,notes,is_online_payment,general_ledger_data) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

	public static final String MODIFY_GENERAL_INFO_BY_ID = "update invoice_general_information  set org_location=?, org_gst=?, invoice_type_id=?, customer_id=?, customer_ledger_id=?, customer_ledger_name=?, customer_gst=?, receiver_name=?, place_of_supply_id=?, supply_service_id=?, reverse_charge=?, sold_by_ecommerce=?, ecommerce_gstin=?, wop_under_lut=?, lut_number=?, lut_date=?, po_number=?, invoice_number=?, invoice_date=?, payment_terms_id=?, due_date=?, item_rates_tax_id=?, export_type_id=?, port_code=?, shipping_bill_no=?, shipping_bill_date=?, sub_total=?, tds_id=?, tds_value=?, discount_ledger_id=?, discount_ledger_name=?, discount_value=?, adjustment_value=?, adjustment_ledger_id=?, adjustment_ledger_name=?, total=?, terms_conditions=?, bank_details_id=?, bank_type=?, currency_id=?, exchange_rate=?, status=?, update_ts=?,  update_user_id=?, organization_id=?, is_Super_Admin=?, update_role_name=?,adjustment_account_level=?,discount_account_level=?,balance_due=?,is_registered=?,notes=?,is_online_payment=? , general_ledger_data=? where id =? ";

	public static final String GET_INVOICE_GENERAL_INFO_BY_ID = "SELECT  org_location, org_gst, invoice_type_id, customer_id, customer_ledger_id, customer_ledger_name, customer_gst, receiver_name, place_of_supply_id, supply_service_id, reverse_charge, sold_by_ecommerce, ecommerce_gstin, wop_under_lut, lut_number, lut_date, po_number, invoice_number, invoice_date, payment_terms_id, due_date, item_rates_tax_id, export_type_id, port_code, shipping_bill_no, shipping_bill_date, sub_total, tds_id, tds_value, discount_ledger_id, discount_ledger_name, discount_value, adjustment_value, adjustment_ledger_id, adjustment_ledger_name, total, terms_conditions, bank_details_id, bank_type, currency_id, exchange_rate, status, user_id, organization_id, is_Super_Admin, role_name,adjustment_account_level,discount_account_level,balance_due,is_registered,create_ts,notes,is_online_payment,general_ledger_data  FROM invoice_general_information where id = ?";

	public static final String GET_INVOICE_FOR_INVOICE_ID = "select invoice_number from invoice_general_information where organization_id = ? and id = ?";

	public static final String INSERT_INTO_INVOICE_ITEMS = "insert into invoice_items ( product_id, product_account_id, product_ledger_name, product_account_level, quantity, unit_measure_id, unit_price, discount, discount_type_id, discount_amount, tax_rate_id, amount, status, create_ts, invoice_general_information_id,net_amount,base_unit_price,description,temp_id ) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

	public static final String MODIFY_INVOICE_ITEMS_BY_ID = "update invoice_items set product_id=?, product_account_id=?, product_ledger_name=?, product_account_level=?, quantity=?, unit_measure_id=?, unit_price=?, discount=?, discount_type_id=?, discount_amount=?, tax_rate_id=?, amount=?, status=?, update_ts=?,net_amount=? ,base_unit_price= ?,description=?,temp_id =? where id = ? and invoice_general_information_id=?";

	public static final String MODIFY_INVOICE_PRODUCT_STATUS_FOR_PRODUCT_ID = "update invoice_items set status = ? , update_ts=? where id = ? ";

	public static final String INSERT_INTO_TAX_DETAILS = "insert into tax_details (component_name, component_product_id, tax_group_name, tax_name, tax_rate, tax_amount, create_ts) values (?,?,?,?,?,?,?)";

	public static final String UPDATE_TAX_DETAILS = "update tax_details set component_name=?, component_product_id=?, tax_group_name=?, tax_name=?, tax_rate=?, tax_amount=?, update_ts=? where id=?";

	public static final String CHECK_INVOICE_EXIST_FOR_ORG = "select id from invoice_general_information where organization_id = ? and invoice_number = ?";

	public static final String GET_ALL_INVOICE_FOR_ORG = "select igi.id , cgi.customer_display_name, igi.invoice_number,igi.po_number, igi.invoice_date,igi.due_date,igi.status,igi.total,igi.balance_due,cur.symbol,igi.currency_id,cur.no_of_decimals_for_amount_in_words,igi.customer_id,igi.pending_approval_status from accounts_receivable.invoice_general_information  igi  "
			+ "join accounts_receivable.customer_general_information cgi on cgi.id = igi.customer_id   "
			+ " left join usermgmt.currency_organization cur on cur.id = igi.currency_id "
			+ "where igi.organization_id = ?  and igi.status not in (?) ";

	public static final String GET_ITEMS_BY_INVOICE_ID = "select ait.id , ait.product_id,pr.name,pr.type ,ait.product_account_id,ait.product_ledger_name,ait.product_account_level, "
			+ "ait.quantity,ait.unit_measure_id,ait.unit_price,ait.tax_rate_id,ait.discount,ait.discount_type_id, "
			+ "ait.discount_amount,ait.amount,ait.status,pr.hsn,ait.net_amount,ait.base_unit_price,ait.description,ait.temp_id  from accounts_receivable.invoice_items ait "
			+ "join usermgmt.product_organization pr on ait.product_id = pr.id "
			+ "where ait.invoice_general_information_id = ? and ait.status not in (?)";

	public static final String GET_TAX_DETAILS_BY_ITEM_ID = "SELECT id, component_name, component_product_id, tax_group_name, tax_name, tax_rate, tax_amount FROM tax_details txd where component_product_id =? and component_name = ? and status not in ('DEL')";

	public static final String ACTIVATE_DEACTIVATE_INVOICE = "update invoice_general_information set status=?,update_ts=?,update_role_name=?,update_user_id=? where id=?";

	public static final String MODIFY_TAX_DETAILS_STATUS = " update tax_details set update_ts =? ,status =? where component_name =? and component_product_id =? ";

	public static final String MODIFY_CUSTOMER_GST_NUMBER ="update accounts_receivable.invoice_general_information set customer_gst=? where organization_id = ? and customer_id = ? ";
	
	
	
	// Receipts
	public static final String UPDATE_INVOICE_DUE_BALANCE = "update invoice_general_information set balance_due=?,update_ts=?,status=? where id=?";

	public static final String GET_ALL_RECEIPT_ELIGIBLE_INVOICES_FOR_ORG = "select id,invoice_number,balance_due,currency_id,(SELECT name FROM finance_common.invoice_type it where it.id=aigi.invoice_type_id) as invoice_type,total from invoice_general_information aigi where organization_id =? AND customer_id=? AND currency_id=? and  STATUS IN (?,?,?,?,?)";
	public static final String GET_ALL_CREDITNOTE_ELIGIBLE_INVOICES_FOR_ORG = "select id,invoice_number from invoice_general_information aigi where organization_id =? AND customer_id=? and STATUS IN (?,?,?,?,?,?)";
	
	public static final String GET_MAX_AMOUNT_FOR_ORG = "select CEILING(max(CAST(igi.total as DECIMAL(9,2)))/1000)*1000 from  invoice_general_information igi where igi.organization_id = ? ";

	public static final String GET_CURRENCY_FOR_INVOICE = "SELECT currency_id FROM invoice_general_information WHERE id=?";
	public static final String GET_BASIC_INVOICE_BY_ID = "select total,status,due_date,invoice_date,organization_id,pending_approval_status from invoice_general_information aigi where id =?";
		public static final String GET_ACTIVE_RECEIPTS_FOR_INVOICE = "select rc.id,icr.invoice_amount,icr.received_amount,rt.id refund_type_id,rc.status,rc.receipt_no from invoice_category_receipt icr "
			+ " left join receipt rc on rc.id = icr.receipt_id "
			+ " left join finance_common.refund_types rt on rt.name =  ? "
			+ " where rc.status in (?) and icr.invoice_on_account_id = ? ";
		public static final String GET_INVOICE_DETAILS_FOR_CUSTOMER = "select id,invoice_number,balance_due from invoice_general_information where organization_id=? and customer_id=? and currency_id=? and status in (?,?,?,?)";
		
		public static final String GET_ALL_INVOICES_FOR_CUSTOMER_CURRENCY = "select id,invoice_number,balance_due from invoice_general_information where organization_id=? and customer_id=? and currency_id=? and status NOT in (?,?)";
		public static final String GET_WORKFLOW_DATA_FOR_INVOICE = "SELECT id,organization_id,total,igi.current_rule_id,status,pending_approval_status,invoice_number,balance_due,currency_id FROM invoice_general_information igi WHERE igi.id=?";
		public static final String UPDATE_CURRENT_RULE="update invoice_general_information set current_rule_id=?,status=?,pending_approval_status=? where id=?";
		
		public static final String GET_INVOICE_DETAILS_FOR_CUSTOMER_CURRENCY = "select id,invoice_number,balance_due from invoice_general_information where organization_id=? and customer_id=? and currency_id=? and status in (?,?,?) and id > 0";
		
		
	public static final String GET_CREDIT_NOTE_BY_INVOICE_ID = "SELECT id,credit_note_number,balance_due FROM credit_note WHERE original_invoice_id=? and STATUS IN(?,?,?)";
	public static final String GET_CREDIT_NOTE_BY_REFERENCE_ID = "SELECT id,credit_note_number,balance_due FROM credit_note WHERE original_invoice_id=? and id=?";
		
	public static final String GET_DASHBOARD_INVOICE_LIST = 
			" select igi.id , cgi.customer_display_name, igi.invoice_number,igi.po_number, igi.invoice_date,igi.due_date,igi.status,igi.total,igi.balance_due,cur.symbol,igi.currency_id,cur.no_of_decimals_for_amount_in_words,igi.customer_id,igi.pending_approval_status from accounts_receivable.invoice_general_information  igi "+	
			" join accounts_receivable.customer_general_information cgi on cgi.id = igi.customer_id  "+
			" left join usermgmt.currency_organization cur on cur.id = igi.currency_id "+
			" where igi.organization_id = ? and igi.status  in (? , ?, ?) order by igi.due_date asc limit 10 ";
	
	public static final String GET_STATUS_AND_ITS_PERCENT = " select inv.status , round(((count(inv.status)  / inv.total_count) * 100 ),0) percent from  " + 
			" (SELECT status,(SELECT  count(status) FROM accounts_receivable.invoice_general_information where organization_id = ? ) as total_count FROM accounts_receivable.invoice_general_information where organization_id = ? ) inv  " + 
			" group by inv.status " ;
}

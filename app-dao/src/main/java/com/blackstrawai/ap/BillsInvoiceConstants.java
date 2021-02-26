package com.blackstrawai.ap;

public class BillsInvoiceConstants {

	public static final String MODULE_TYPE_INVOICES_BILL = "invoices_bills";
	public static final String MODULE_TYPE_INVOICES_WITHOUT_BILL = "invoices_without_bills";

	// Queries for invoice_general_information table
	public static final String INSERT_INTO_INVOICE_GENERAL_INFO = "insert into invoice_general_information (vendor_id, invoice_date, location_id, gstnumber_id, invoice_no, po_reference_no, payment_terms_id, due_date, notes, terms_conditions, status, is_invoice_with_bills, create_ts, user_id, organization_id, is_Super_Admin,role_name,is_registered,akshar_data,general_ledger_data,vendor_gst_no) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	public static final String MODIFY_INVOICE_GENERAL_INFO_STATUS = "update invoice_general_information set status = ? , update_ts=? where id = ? ";
	public static final String GET_INVOICE_BY_ID = "SELECT vendor_id, invoice_date, location_id, gstnumber_id, invoice_no, po_reference_no, payment_terms_id, due_date, notes, terms_conditions, status, is_invoice_with_bills, user_id, organization_id, is_Super_Admin,create_ts,is_registered,is_quick,update_user_role_name,role_name,akshar_data,general_ledger_data,vendor_gst_no FROM invoice_general_information where id = ?";
	public static final String MODIFY_INVOICE_GENERAL_INFO = "update invoice_general_information set vendor_id=?, invoice_date=?, location_id=?, gstnumber_id=?, invoice_no=?, po_reference_no=?, payment_terms_id=?, due_date=?, notes=?, terms_conditions=?, status=?, is_invoice_with_bills=?, update_ts=?, update_user_id=?, organization_id=?, is_Super_Admin=?,update_user_role_name=?,is_registered=?,akshar_data=?,general_ledger_data=? ,vendor_gst_no=? where id = ?;";
	public static final String CHECK_INVOICE_EXIST_FOR_ORG = "select invoice_no from invoice_general_information where organization_id = ? and vendor_id=? and invoice_no = ?";
	public static final String GET_INVOICE_FOR_INVOICE_ID = "select invoice_no from invoice_general_information where organization_id = ? and id = ?";

	// Queries for invoice_product table
	public static final String INSERT_INTO_INVOICE_PRODUCT = "insert into invoice_product (product_id, product_account_id, quantity, unit_measure_id, unit_price, tax_rate_id, customer_id, is_billable, input_tax_credit, amount, status, create_ts, invoice_general_information_id,product_account_name,product_account_level,description) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	public static final String MODIFY_INVOICE_PRODUCT_STATUS_FOR_INVOICE_ID = "update invoice_product set status = ? , update_ts=? where invoice_general_information_id = ? ";
	public static final String GET_INVOICE_PRODUCT_INFO = "select ipr.id,ipr.product_id,pr.name ,pr.hsn ,ipr.product_account_id,ipr.quantity ,ipr.unit_measure_id, um.unit_name ,ipr.unit_price,ipr.tax_rate_id, "
			+ " tgo.name ,ipr.customer_id , cgi.customer_display_name , ipr.is_billable ,ipr.input_tax_credit ,ipr.amount,ipr.status,ipr.product_account_name,ipr.product_account_level,ipr.description ,pr.type "
			+ " from accounts_payable.invoice_product ipr "
			+ " join usermgmt.product_organization pr on ipr.product_id = pr.id "
			+ " left join finance_common.unit_of_measure um on um.id = ipr.unit_measure_id "
			+ " left join usermgmt.tax_group_organization tgo on tgo.id = ipr.tax_rate_id "
			+ " left join accounts_receivable.customer_general_information cgi on cgi.id = ipr.customer_id "
			+ " where ipr.invoice_general_information_id = ? and ipr.status not in ('DEL') ";
	public static final String MODIFY_INVOICE_PRODUCT = "update invoice_product set product_id=?, product_account_id=?, quantity=?, unit_measure_id=?, unit_price=?, tax_rate_id=?, customer_id=?, is_billable=?, input_tax_credit=?, amount=?, status=?, update_ts=?,product_account_name=?,product_account_level=? ,description =? where id = ?  and invoice_general_information_id=?";
	public static final String MODIFY_INVOICE_PRODUCT_STATUS_FOR_PRODUCT_ID = "update invoice_product set status = ? , update_ts=? where id = ? ";

	// Queries for accounts_payable_tax_details table
	public static final String INSERT_INTO_TAX_DETAILS = "insert into accounts_payable_tax_details (component_name, component_product_id, tax_group_name, tax_name, tax_rate, tax_amount, create_ts) values (?,?,?,?,?,?,?)";
	public static final String GET_TAX_DETAILS = "SELECT id, component_name, component_product_id, tax_group_name, tax_name, tax_rate, tax_amount FROM accounts_payable_tax_details txd where component_product_id =? and component_name = ? and status not in ('DEL')";
	public static final String MODIFY_TAX_DETAILS_STATUS = " update accounts_payable_tax_details set update_ts =? ,status =? where component_name =? and component_product_id =? ";

	// Queries for invoice_transaction_details table
	public static final String INSERT_INTO_INVOICE_TRANSACTION_DETAILS = "insert into invoice_transaction_details(source_of_supply_id, destination_of_supply_id, item_rates_tax_id, currency_organization_id, exchange_rate, sub_total, discount_value, discount_type, discount_amount, discount_account, is_apply_after_tax, tds_id, tds_value, adjustment, adjustment_account, total, invoice_general_information_id, create_ts,discount_account_name,discount_account_level,adjustment_account_name,adjustment_account_level,balance_due) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	public static final String GET_INVOICE_TRANSACTION_DETAILS = "SELECT source_of_supply_id, destination_of_supply_id, item_rates_tax_id, currency_organization_id, exchange_rate, sub_total, discount_value, discount_type, discount_amount, discount_account, is_apply_after_tax, tds_id, tds_value, adjustment, adjustment_account, total,discount_account_name,discount_account_level,adjustment_account_name,adjustment_account_level FROM invoice_transaction_details where invoice_general_information_id = ?";
	public static final String MODIFY_INVOICE_TRANSACTION_DETAILS = "update invoice_transaction_details set source_of_supply_id = ? , destination_of_supply_id = ? , item_rates_tax_id = ? , currency_organization_id = ? , exchange_rate = ? , sub_total = ? , discount_value = ? , discount_type= ? , discount_amount = ? , discount_account= ? , is_apply_after_tax= ? , tds_id= ? , tds_value= ? , adjustment= ? , adjustment_account= ? , total= ? , update_ts= ? ,discount_account_name= ? ,discount_account_level= ? ,adjustment_account_name= ? ,adjustment_account_level= ? where  invoice_general_information_id = ?";

	// Query to get All the Invoices for Organization
	public static final String GET_ALL_INVOICES = "select igi.id ,vi.vendor_display_name ,igi.invoice_no,igi.po_reference_no,igi.invoice_date,igi.due_date,igi.status,itx.total,org.name,itx.balance_due,itx.total_amount,igi.update_user_role_name,igi.is_quick,igi.role_name,igi.pending_approval_status,vi.id,itx.currency_organization_id  from invoice_general_information igi join invoice_transaction_details itx on igi.id = itx.invoice_general_information_id join vendor_general_information vi on igi.vendor_id = vi.id join usermgmt.organization org on org.id = igi.organization_id where  igi.organization_id = ? and igi.is_invoice_with_bills =? ";

	public static final String ACTIVATE_DEACTIVATE_INVOICE = "update invoice_general_information set status=?,update_ts=?,update_user_id=? ,update_user_role_name=? where id=?";

	public static final String GET_MAX_AMOUNT_FOR_ORG = " select CEILING(max(CAST(itx.total as DECIMAL(9,2)))/1000)*1000 from invoice_transaction_details itx join  invoice_general_information igi on itx.invoice_general_information_id =  igi.id where igi.organization_id = ? and igi.is_invoice_with_bills = ?";

	// QuickInvoice
	public static final String INSERT_INTO_GENERAL_INFO_FOR_QUICKINVOICE = "insert into invoice_general_information (vendor_id,invoice_no,invoice_date,due_date,is_invoice_with_bills,remarks,status,create_ts,user_id,organization_id,is_Super_Admin,role_name,is_quick,akshar_data) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	public static final String INSERT_INTO_TRANSACTION_FOR_QUICKINVOICE = "insert into invoice_transaction_details(currency_organization_id,base_amount,gst_amount,total_amount,is_rcm_applicable,invoice_general_information_id,create_ts) value(?,?,?,?,?,?,?) ";

	public static final String GET_GENERAL_INFO_FOR_QUICKINVOICE = "select vendor_id,invoice_no,invoice_date,due_date,is_invoice_with_bills,remarks,status,user_id,organization_id,is_Super_Admin,role_name,is_quick,update_user_role_name,status,akshar_data  from invoice_general_information where id = ?";
	public static final String GET_TRANSACTION_FOR_QUICKINVOICE = "select currency_organization_id,base_amount,gst_amount,total_amount,is_rcm_applicable from invoice_transaction_details where invoice_general_information_id = ? ";

	public static final String UPDATE_GENERAL_INFO_FOR_QUICKINVOICE = "update invoice_general_information  set vendor_id =? ,invoice_no =? ,invoice_date =? ,due_date =? ,is_invoice_with_bills=? ,remarks=? ,status=? ,update_ts=? ,update_user_id=? ,organization_id=? ,is_Super_Admin=? ,update_user_role_name =?,akshar_data=? where id = ?";
	public static final String UPDATE_TRANSACTION_FOR_QUICKINVOICE = "update invoice_transaction_details set currency_organization_id =?,base_amount=?,gst_amount=?,total_amount=?,is_rcm_applicable=?,update_ts=? where invoice_general_information_id = ? ";

	public static final String GET_ALL_INVOICES_FOR_VENDOR = "SELECT id FROM invoice_general_information WHERE vendor_id=? and STATUS NOT IN(?,?,?,?)  AND id IN (SELECT invoice_general_information_id  from invoice_transaction_details  where currency_organization_id=?)";
	public static final String GET_QUICKINVOICE_TXN_DETAILS_FOR_INVOICE_ID = "SELECT total_amount,base_amount,gst_amount FROM invoice_transaction_details where invoice_general_information_id = ?";
	
	public static final String GET_INVOICE_TXN_DETAILS_FOR_INVOICE_IDS = "SELECT invoice_general_information_id,balance_due FROM invoice_transaction_details where invoice_general_information_id IN (%s) and balance_due > 0.0";
	public static final String UPDATE_INVOICE_DUE_BALANCE_FOR_INVOICE_IDS = "update invoice_transaction_details set balance_due = ? where invoice_general_information_id = ?"; 
	
	
	public static final String GET_ALL_INVOICES_ORG_VENDOR = "select igi.id ,vi.vendor_display_name ,igi.invoice_no,igi.po_reference_no,igi.invoice_date,igi.due_date,igi.status,itx.total,org.name,itx.balance_due,itx.total_amount,igi.update_user_role_name,igi.is_quick,igi.role_name  from invoice_general_information igi join invoice_transaction_details itx on igi.id = itx.invoice_general_information_id  AND itx.balance_due>=0 join vendor_general_information vi on igi.vendor_id = vi.id join usermgmt.organization org on org.id = igi.organization_id where  igi.organization_id = ? and igi.is_invoice_with_bills = true and vendor_id = ? and itx.currency_organization_id = ? and igi.status in (?,?,?,?,?,?)";
	public static final String GET_INVOICES_BY_ORG_VENDOR_STATUS = "select igi.id ,vi.vendor_display_name ,igi.invoice_no,igi.po_reference_no,igi.invoice_date,igi.due_date,igi.status,itx.total,org.name,itx.balance_due,itx.total_amount,igi.update_user_role_name,igi.is_quick,igi.role_name  from invoice_general_information igi join invoice_transaction_details itx on igi.id = itx.invoice_general_information_id  join vendor_general_information vi on igi.vendor_id = vi.id join usermgmt.organization org on org.id = igi.organization_id where  igi.organization_id = ? and igi.is_invoice_with_bills = true and vendor_id = ? and igi.status not in (?,?,?,?) AND itx.currency_organization_id=? ";

	public static final String GET_WORKFLOW_DATA_FOR_INVOICE = "SELECT id,organization_id,location_id,gstnumber_id,is_registered,"
			+ "(SELECT vendor_display_name FROM vendor_general_information vgi WHERE vgi.id=igi.vendor_id) AS vendor_name,"
			+ "(SELECT gst_no FROM vendor_general_information vgi WHERE vgi.id=igi.vendor_id) AS vendor_gst,"
			+ "(SELECT total FROM invoice_transaction_details itd WHERE itd.invoice_general_information_id=igi.id) AS invoice_amount,"
			+ "igi.vendor_id,igi.current_rule_id,status,pending_approval_status,invoice_no,is_invoice_with_bills,"
			+ "(SELECT currency_organization_id FROM invoice_transaction_details itd WHERE itd.invoice_general_information_id=igi.id) AS currency"
			+ " FROM invoice_general_information igi WHERE igi.id=?";

	public static final String UPDATE_CURRENT_RULE = "update invoice_general_information set current_rule_id=?,status=?,pending_approval_status=? where id=?";
	public static final String GET_CURRENT_RULE = "select current_rule_id from invoice_general_information where id=?";
	public static final String GET_BASIC_INVOICE_BY_ID = "select (SELECT total FROM invoice_transaction_details itd WHERE itd.invoice_general_information_id=igi.id) AS invoice_amount,"
			+ "status,due_date,invoice_date,organization_id,pending_approval_status from invoice_general_information igi where id =?";
	public static final String UPDATE_INVOICE_DUE_BALANCE = "update invoice_transaction_details set balance_due=?,update_ts=? where invoice_general_information_id=?";
	public static final String UPDATE_INVOICE_STATUS = "update invoice_general_information set status=?,update_ts=? where id=?";

	public static final String GET_ALL_INVOICE_AMOUNT_FROM_PAYMENTS = "SELECT * FROM payments_non_core_bill_details pncbd WHERE bill_reference=? and payments_non_core_id IN (SELECT id FROM payments_non_core pnc WHERE pnc.id=pncbd.payments_non_core_id and STATUS IN (?) ) and type=? and status not IN('DEL')";
	public static final String GET_BALANCE_DUE_BY_INVOICE_ID = "select itx.balance_due, igi.invoice_no, igi.due_date from invoice_general_information igi join invoice_transaction_details itx on igi.id = itx.invoice_general_information_id join vendor_general_information vi on igi.vendor_id = vi.id join usermgmt.organization org on org.id = igi.organization_id where  igi.organization_id = ? and igi.is_invoice_with_bills = true and vendor_id = ? and itx.currency_organization_id = ? and igi.status in (?,?,?,?,?,?,?) and igi.id = ?";

	public static final String GET_STATUS_AND_ITS_PERCENT = " select inv.status , round(((count(inv.status)  / inv.total_count) * 100 ),0) percent from  "
			+ " (SELECT status,(SELECT  count(status) FROM accounts_payable.invoice_general_information where organization_id = ? and is_invoice_with_bills = 1 ) as total_count FROM accounts_payable.invoice_general_information where organization_id = ? and is_invoice_with_bills = 1 ) inv  "
			+ " group by inv.status ";

	public static final String GET_DASH_BOARD_INVOICES = "select igi.id ,igi.invoice_no,igi.due_date,igi.status,itx.balance_due,itx.currency_organization_id from invoice_general_information igi join invoice_transaction_details itx on igi.id = itx.invoice_general_information_id join vendor_general_information vi on igi.vendor_id = vi.id join usermgmt.organization org on org.id = igi.organization_id where  igi.organization_id = ? and igi.is_invoice_with_bills =1 AND igi.status IN(?,?,?,?) ";
	public static final String GET_ALL_DUE_INVOICES = "select igi.id ,vi.vendor_display_name ,igi.invoice_no,igi.po_reference_no,igi.invoice_date,igi.due_date,igi.status,itx.total,org.name,itx.balance_due,itx.total_amount,igi.update_user_role_name,igi.is_quick,igi.role_name,igi.pending_approval_status,vi.id,itx.currency_organization_id  from invoice_general_information igi join invoice_transaction_details itx on igi.id = itx.invoice_general_information_id join vendor_general_information vi on igi.vendor_id = vi.id join usermgmt.organization org on org.id = igi.organization_id where  igi.organization_id = ? and igi.is_invoice_with_bills =? ";
	
	public static final String GET_ALL_INVOICES_FOR_VENDOR_CREDIT = "select igi.id ,igi.invoice_no,igi.invoice_date from invoice_general_information igi where igi.organization_id = ? and igi.status IN(?,?,?,?,?)";
	
	public static final String GET_BILLS_PAYABLE_USER_ROLE = "SELECT igi.id, igi.invoice_no, vgi.vendor_display_name, igi.due_date, itx.balance_due, igi.status, co.symbol, igi.invoice_date , igi.vendor_id ,itx.currency_organization_id "
			+ "FROM invoice_general_information igi, invoice_transaction_details itx, vendor_general_information vgi, usermgmt.currency_organization co "
			+ "WHERE igi.organization_id = ? AND igi.id = itx.invoice_general_information_id AND igi.vendor_id = vgi.id "
			+ "AND igi.status NOT IN (?,?,?,?) AND co.id = itx.currency_organization_id "
			+ "AND igi.user_id = ? AND igi.role_name = ? ORDER BY igi.due_date ASC";
	
	public static final String GET_BILLS_PAYABLE = "SELECT igi.id, igi.invoice_no, vgi.vendor_display_name, igi.due_date, itx.balance_due, igi.status, co.symbol, igi.invoice_date , igi.vendor_id ,itx.currency_organization_id  "
			+ "FROM invoice_general_information igi, invoice_transaction_details itx, vendor_general_information vgi, usermgmt.currency_organization co "
			+ "WHERE igi.organization_id = ? AND igi.id = itx.invoice_general_information_id AND igi.vendor_id = vgi.id "
			+ "AND igi.status NOT IN (?,?,?,?) AND co.id = itx.currency_organization_id ORDER BY igi.due_date ASC";
}

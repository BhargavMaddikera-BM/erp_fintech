package com.blackstrawai.ar;

public class CreditNoteConstants {

	public static final String INSERT_INTO_CREDIT_NOTES_ORGANIZATION = "insert into accounts_receivable.credit_note(customer_id,original_invoice_id,credit_note_type_id,credit_note_number,credit_note_date,"
			+ "reason, terms_conditions,sub_total,  tds_id,  tds_value,  discount_ledger_id,  discount_ledger_name,"
			+ "  discount_value,  discount_account_level,  adjustment_value,  adjustment_ledger_id,  adjustment_ledger_name,"
			+ "  adjustment_account_level,  total,  status,  create_ts,  user_id,  organization_id,  is_Super_Admin,"
			+ "  role_name,balance_due,note,reason_id,exchange_rate,general_ledger_data ) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	public static final String INSERT_INTO_CREDIT_NOTES_ITEMS = "insert into credit_note_items ( product_id, product_account_id, product_ledger_name, product_account_level, quantity, unit_measure_id, unit_price, discount, discount_type_id, discount_amount, tax_rate_id, amount, status, create_ts, credit_note_id,net_amount,base_unit_price,description,base_quantity,temp_id ) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	public static final String UPDATE_CREDIT_NOTES_ITEMS = "update accounts_receivable.credit_note_items set product_id=?, product_account_id=?,"
			+ "product_ledger_name=?, product_account_level=?, quantity=?, unit_measure_id=?, unit_price=?, discount=?, discount_type_id=?, "
			+ "discount_amount=?, tax_rate_id=?, amount=?, status=?, update_ts=?, credit_note_id=?,net_amount=? ,base_unit_price=?,description=?,base_quantity=? ,temp_id =? where id=?";
	public static final String CHECK_CREDIT_NOTES_EXISTS_FOR_ORGANIZATION = "select id,credit_note_number from accounts_receivable.credit_note where organization_id=? and credit_note_number=?";
	public static final String UPDATE_CREDIT_NOTES_ORGANIZATION = "update accounts_receivable.credit_note set customer_id=?,original_invoice_id=?,credit_note_type_id=?,credit_note_number=?,credit_note_date=?,"
			+ "reason=?, terms_conditions=?,sub_total=?,  tds_id=?,  tds_value=?,  discount_ledger_id=?,  discount_ledger_name=?,"
			+ "  discount_value=?,  discount_account_level=?,  adjustment_value=?,  adjustment_ledger_id=?,  adjustment_ledger_name=?,"
			+ "  adjustment_account_level=?,  total=?,  status=?,  update_ts=?,  update_user_id=?,  organization_id=?,  is_Super_Admin=?,"
			+ "  update_role_name=?,balance_due=?,note=?,reason_id=?,exchange_rate=?, general_ledger_data =? where id=?";
	public static final String GET_ALL_CREDIT_NOTES_FOR_ORGANIZATION_USER_ROLE = "select id,customer_id,"
			+ "(select customer_display_name from customer_general_information cgi where cgi.id=cn.customer_id) as customer_name,"
			+ "(select invoice_number from invoice_general_information ig where ig.id=cn.original_invoice_id),credit_note_type_id,credit_note_number,credit_note_date,"
			+ "(select name from finance_common.credit_note_reasons cnr where cnr.id=cn.reason_id), terms_conditions,sub_total,  tds_id,  tds_value,  discount_ledger_id,  discount_ledger_name,discount_value,  discount_account_level, "
			+ " adjustment_value,  adjustment_ledger_id,  adjustment_ledger_name,adjustment_account_level,  total,balance_due,status,  create_ts,  user_id,  "
			+ "organization_id,  is_Super_Admin,role_name,note,original_invoice_id,pending_approval_status from accounts_receivable.credit_note cn where organization_id=? and user_id=? and role_name=? and status not in (?) ";

	public static final String GET_ALL_CREDIT_NOTES_FOR_ORGANIZATION = "select id,customer_id,"
			+ "(select customer_display_name from customer_general_information cgi where cgi.id=cn.customer_id) as customer_name,"
			+ "(select invoice_number from invoice_general_information ig where ig.id=cn.original_invoice_id),credit_note_type_id,credit_note_number,credit_note_date,"
			+ "(select name from finance_common.credit_note_reasons cnr where cnr.id=cn.reason_id), terms_conditions,sub_total,  tds_id,  tds_value,  discount_ledger_id,  discount_ledger_name,discount_value,  discount_account_level, "
			+ " adjustment_value,  adjustment_ledger_id,  adjustment_ledger_name,adjustment_account_level,  total,balance_due,status,  create_ts,  user_id,  "
			+ "organization_id,  is_Super_Admin,role_name,note,original_invoice_id,pending_approval_status from accounts_receivable.credit_note cn where organization_id=? and status not in (?) ";

	public static final String GET_CREDIT_NOTES_BY_ID = "select id,customer_id,"
			+ "cn.original_invoice_id,credit_note_type_id,credit_note_number,credit_note_date,"
			+ "reason, terms_conditions,sub_total,  tds_id,  tds_value,  discount_ledger_id,  discount_ledger_name,discount_value,  discount_account_level, "
			+ " adjustment_value,  adjustment_ledger_id,  adjustment_ledger_name,adjustment_account_level,  total,balance_due,status,  create_ts,  user_id,  "
			+ "organization_id,  is_Super_Admin,role_name,note,reason_id,exchange_rate,general_ledger_data  from accounts_receivable.credit_note cn where id=?";
	public static final String GET_ITEMS_FOR_CREDIT_NOTE_ID = "select cit.id , cit.product_id,pr.name,pr.type ,cit.product_account_id,cit.product_ledger_name,cit.product_account_level,   \r\n"
			+ "			cit.quantity,cit.unit_measure_id,cit.unit_price,cit.tax_rate_id,cit.discount,cit.discount_type_id,   "
			+ "			cit.discount_amount,cit.amount,cit.status,pr.hsn,cit.net_amount,cit.base_unit_price,cit.description,cit.base_quantity,cit.temp_id  from accounts_receivable.credit_note_items cit "
			+ "			join usermgmt.product_organization pr on cit.product_id = pr.id "
			+ "			where cit.credit_note_id =?  and cit.status not IN (?)";
	public static final String GET_MAX_AMOUNT_FOR_ORG = "select CEILING(max(CAST(cn.total as DECIMAL(9,2)))/1000)*1000 from  credit_note cn where cn.organization_id = ? ";
	public static final String DELETE_CREDIT_NOTE_ITEM_BY_ID = "DELETE from credit_note_items where id=?";
	public static final String CHANGE_CREDIT_NOTE_STATUS_ORGANIZATION = "update credit_note set status=?,update_ts=?,update_user_id=?,update_role_name=? where id=?";
	public static final String GET_REFUND_ELIGIBLE_CREDITNOTES = "select id,credit_note_number,balance_due from credit_note where organization_id=? and status in (?,?,?)";
	public static final String UPDATE_CN_DUE_BALANCE = "update credit_note set balance_due=?,update_ts=?,status=? where id=?";
	public static final String GET_TOTAL_FOR_INVOICE_FROM_CN = "SELECT sum(total) FROM credit_note WHERE original_invoice_id=? AND pending_approval_status IS NULL AND  STATUS IN (?,?,?,?)";
	public static final String GET_ALL_ACT_CN_FOR_INVOICE = "SELECT id FROM credit_note WHERE original_invoice_id=? AND STATUS IN (?,?,?,?)";
	public static final String GET_TOTAL_AMOINT_FOR_CREDITNOTE = "SELECT total FROM credit_note WHERE id=?";
	public static final String GET_TOTAL_CREDIT_NOTES_AMOUNT_FOR_CUSTOMER_CURRENCY = "select sum(balance_due) from credit_note cn where organization_id=? and customer_id=? and "
			+ "original_invoice_id IN(select id from invoice_general_information igi where igi.organization_id=cn.organization_id and igi.customer_id=cn.customer_id and currency_id=?)"
			+ "and STATUS IN (?,?,?)";
	
	public static final String GET_TOTAL_CREDIT_NOTES_AMOUNT_FOR_CUSTOMER = "select sum(balance_due) from credit_note where organization_id=? and customer_id=? and STATUS IN (?,?,?)";
	public static final String GET_CREDIT_DETAILS_FOR_CUSTOMER = "select id,credit_note_number,total,balance_due  from credit_note where organization_id=? and customer_id=? AND original_invoice_id IN(select id from invoice_general_information where currency_id=?) and STATUS IN (?,?,?)";
	public static final String GET_INVOICE_FOR_CREDIT_NOTES = "select original_invoice_id from credit_note where id=?";
	public static final String GET_BASIC_CREDIT_NOTE_BY_ID = "select total,status,original_invoice_id,pending_approval_status,organization_id,(select balance_due from invoice_general_information igi where igi.id=cn.original_invoice_id) as invoice_balance_due from credit_note cn where id =?";
	public static final String MODIFY_CN_PRODUCT_STATUS_FOR_PRODUCT_ID = "update credit_note_items set status = ? , update_ts=? where id = ? ";
	public static final String GET_WORKFLOW_DATA_FOR_CN = "SELECT id,organization_id,total,cn.current_rule_id,status,pending_approval_status,credit_note_number,original_invoice_id,"
			+ "(SELECT currency_id FROM invoice_general_information igi WHERE igi.id=cn.original_invoice_id) AS currencyId"
			+ " FROM credit_note cn WHERE cn.id=?";
	public static final String UPDATE_CURRENT_RULE="update credit_note set current_rule_id=?,status=?,pending_approval_status=? where id=?";	

	public static final String GET_CREDITNOTE_NUMBER="SELECT credit_note_number FROM credit_note where id =? ";	

	

}

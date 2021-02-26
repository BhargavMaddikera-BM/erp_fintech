package com.blackstrawai.ar;

public class ApplyCreditsConstants {

	public static final String INSERT_INTO_APPLY_CREDITS = "insert into apply_credit(voucher_no,date,customer_id,customer_ledger_id,customer_ledger_name,currency_id,invoice_total_amount,adjusted_amount,available_funds,organization_id,status,isSuperAdmin,user_id,customTableList,role_name,exchange_rate)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	public static final String INSERT_INTO_APPLY_CREDITS_INVOICE_DETAILS = "insert into apply_credit_invoice(invoice_id,invoice_amount,applied_amount,apply_credit_id,status,data)values(?,?,?,?,?,?)";
	public static final String INSERT_INTO_APPLY_CREDITS_CREDIT_DETAILS = "insert into apply_credit_credits(reference_id,credit_type,original_amount,available_amount,adjustment_amount,apply_credit_id,status,ledger_id,ledger_name)values(?,?,?,?,?,?,?,?,?)";
	public static final String GET_ALL_APPLY_CREDITS_USER_ROLE = "select id,voucher_no,customer_id,date,invoice_total_amount,currency_id,status from apply_credit where organization_id=? and user_id=? and role_name=? and status not in('DEL') order by create_ts desc";
	public static final String GET_APPLY_CREDIT_BY_ID = "select id,voucher_no,date,customer_id,customer_ledger_id,customer_ledger_name,currency_id,customTableList,invoice_total_amount,adjusted_amount,available_funds,organization_id,status,isSuperAdmin,user_id,role_name,exchange_rate,create_ts from apply_credit where id=?";
	public static final String GET_APPLY_CREDIT_INVOICE_DETAILS_BY_ID = "select id,invoice_id,invoice_amount,applied_amount,status,data from apply_credit_invoice where apply_credit_id=? and status not in('DEL')";
	public static final String GET_APPLY_CREDIT_CREDITS_DETAILS_BY_ID = "select id,reference_id,credit_type,original_amount,available_amount,adjustment_amount,status,ledger_name,ledger_id from apply_credit_credits where apply_credit_id=?  and status not in('DEL')";
	public static final String UPDATE_APPLY_CREDITS = "update apply_credit set voucher_no=?,date=?,customer_id=?,customer_ledger_id=?,customer_ledger_name=?,currency_id=?,invoice_total_amount=?,adjusted_amount=?,available_funds=?,organization_id=?,status=?,isSuperAdmin=?,update_user_id=?,customTableList=?,update_role_name=?,exchange_rate=? where id=?";
	public static final String UPDATE_APPLY_CREDITS_INVOICE_DETAILS = "update apply_credit_invoice set invoice_id=?,invoice_amount=?,applied_amount=?,status=?,data=? where id=?";
	public static final String UPDATE_APPLY_CREDITS_CREDIT_DETAILS = "update apply_credit_credits set reference_id=?,credit_type=?,original_amount=?,available_amount=?,adjustment_amount=?,status=?,ledger_id=?,ledger_name=? where id=?";
	public static final String MODIFY_APPLY_CREDIT_STATUS = "update apply_credit set status=?,update_ts=? where id=?";
	public static final String GET_ALL_APPLY_CREDITS_ORGANIZATION = "select id,voucher_no,customer_id,date,invoice_total_amount,currency_id,status from apply_credit where organization_id=? and status not in('DEL') order by create_ts desc";
	public static final String DELETE_INVOICE_TABLE_ENTRIES = "DELETE from apply_credit_invoice where id=?";
	public static final String DELETE_CREDIT_TABLE_ENTRIES = "DELETE from apply_credit_credits where id=?";
	public static final String CHECK_AC_ORGANIZATION="SELECT id FROM apply_credit WHERE organization_id=? and voucher_no=?";

}

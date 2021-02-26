package com.blackstrawai.ap;

public class BalanceConfirmationConstants {

	public static final String INSERT_INTO_BALANCE_CONFIRMATION = "insert into balance_confirmation (vendor_id,start_date,end_date,role_name,is_quick,user_id,is_Super_Admin,organization_id,status)values(?,?,?,?,?,?,?,?,?)";
	public static final String INSERT_INTO_BALANCE_CONFIRMATION_CURRENCY = "insert into balance_confirmation_currency(balance_confirmation_id,currency_id,opening_balance,closing_balance)values(?,?,?,?)";
	public static final String GET_ALL_ORGANIZATION_BALANCE_CONFIRMATION = "select id,vendor_id,start_date,end_date,status,update_ts,create_ts,organization_id from balance_confirmation where organization_id=? and status not in('WDRW') order by create_ts desc";
	public static final String GET_ALL_VENDOR_BALANCE_CONFIRMATION = "select id,vendor_id,start_date,end_date,status,update_ts,create_ts,organization_id from balance_confirmation where vendor_id=? and status not in('WDRW') order by create_ts desc";
	public static final String GET_ALL_BALANCE_CONFIRMATION_BY_ID = "select start_date,end_date,status,organization_id,user_id,is_Super_Admin,role_name,vendor_id,is_quick from balance_confirmation where id=?";
	public static final String GET_ALL_BALANCE_CONFIRMATION_CURRENCY_INFO_BY_ID = "select currency_id,opening_balance,closing_balance,id,status from balance_confirmation_currency where balance_confirmation_id=? and status!='DEL'";
	public static final String UPDATE_BALANCE_CONFIRMATION = "update balance_confirmation set vendor_id=?,start_date=?,end_date=?,role_name=?,user_id=?,is_Super_Admin=?,organization_id=?,status=?,is_quick=? where id=?";
	public static final String UPDATE_BALANCE_CONFIRMATION_CURRENCY = "update balance_confirmation_currency set currency_id=?,opening_balance=?,closing_balance=?,status=? where id=?";
	public static final String STATUS_CHANGE = "update balance_confirmation set status=?,update_ts=? where id=?";
	public static final String MODIFY_BALANCE_CONFIRMATION_STATUS = "update balance_confirmation set status=?,update_ts=? where id=?";
	public static final String MODIFY_BALANCE_CONFIRMATION_CURRENCY_STATUS = "update balance_confirmation_currency set status=?,update_ts=? where balance_confirmation_id=?";
	public static final String GET_ORGANIZATION_NAME = "select name from organization where id=?";
}

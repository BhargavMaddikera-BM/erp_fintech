package com.blackstrawai.banking;

public class ContraConstants {

	public static final String INSERT_INTO_CONTRA = "insert into banking.contra(reference_no,date,base_currency_id,remark,total_debit,total_credit,status,is_Super_Admin,organization_id,user_id,difference,role_name)values(?,?,?,?,?,?,?,?,?,?,?,?)";
	public static final String INSERT_INTO_CONTRA_ACCOUNT_ENTRIES = "insert into banking.contra_entries(contra_id,account_id,currency_id,exchange_rate,debit,credit,total_credits_ex,total_debits_ex,account_type_name,trans_ref_no)values(?,?,?,?,?,?,?,?,?,?)";
	public static final String GET_ALL_CONTRA_USER_ROLE = "select id,reference_no,total_credit,date,status from banking.contra where organization_id=? and user_id=? and role_name=? order by create_ts desc";
	public static final String GET_ALL_CONTRA_ORGANIZATION = "select id,reference_no,total_credit,date,status from banking.contra where organization_id=? order by create_ts desc";
	public static final String GET_CONTRA_ACCOUNT_BY_ID = "  select reference_no,date,base_currency_id,remark,total_debit,total_credit,is_Super_Admin,organization_id,status,user_id,difference,create_ts,role_name from banking.contra where id=?";
	public static final String GET_CONTRA_ACCOUNT_ENTRIES_BY_ID = "select id,account_id,currency_id,exchange_rate,debit,credit,status,total_credits_ex,total_debits_ex,account_type_name,trans_ref_no from banking.contra_entries where contra_id=? and status!='DEL'";
	public static final String UPDATE_CONTRA_ACCOUNT = "update banking.contra set date=?,base_currency_id=?,remark=?,total_debit=?,total_credit=?,status=?,is_Super_Admin=?,organization_id=?,update_user_id=?,difference=?,update_role_name=? where id=?";
	public static final String UPDATE_CONTRA_ACCOUNT_ENTRIES = "update banking.contra_entries set account_id=?,currency_id=?,exchange_rate=?,debit=?,credit=?,status=?,update_ts=?,total_credits_ex=?,total_debits_ex=?,account_type_name=?,trans_ref_no=? where id=?";
	public static final String CHECK_REFERENCE_NUMBER_EXIST = "select * from banking.contra where organization_id=? and reference_no=?";
	public static final String CHANGE_SINGLE_ITEM_STATUS = "update banking.contra_entries set status=?,update_ts=? where id=?";
	public static final String MODIFY_CONTRA_STATUS = "update table banking.contra set status=?,update_ts=? where id=?";
	public static final String MODIFY_CONTRA_ENTRIES_STATUS = "update table banking.contra_entries set status=?,update_ts=? where contra_id=?";
	public static final String CREATE_CONTRA_UPLOAD = "insert into banking.contra(date,reference_no,total_debit,total_credit,difference,organization_id,user_id,is_Super_Admin,status,role_name,base_currency_id)values(?,?,?,?,?,?,?,?,?,?,?)";
	public static final String CREATE_CONTRA_ITEM_UPLOAD = "insert into banking.contra_entries(contra_id,account_id,account_type_name,currency_id,exchange_rate,debit,credit,total_credits_ex,total_debits_ex)values(?,?,?,?,?,?,?,?,?)";
	public static final String GET_CONTRA_GENERAL_INFO_BY_ID = "select date,reference_no,status from banking.contra where id=? and organization_id=?";
	public static final String GET_CONTRA_ENTRIES_INFO_BY_ID = "select account_id,account_type_name,currency_id,exchange_rate,credit,debit from banking.contra_entries where contra_id=?";
	public static final String UPDATE_GENERAL_INFO="update banking.contra set status=?,total_credit=?,total_debit=?,difference=? where id=?";

}

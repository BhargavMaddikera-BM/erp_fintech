package com.blackstrawai.accounting;

public class AccountingAspectsConstants {

	public static final String INSERT_INTO_ACCOUTING_ASPECTS = "insert into accounting.accounting_entries(date_of_creation,approver1_id,approver2_id,approver3_id,currency_id,journal_no,location_id,notes,type_id,total_credits,total_debits,difference,organization_id,user_id,isSuperAdmin,is_registered_location,status,role_name)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	public static final String INSERT_INTO_ACCOUTING_ASPECTS_ITEM = "insert into accounting.accounting_entries_item_details(accounts_id,currency_id,description,exchange_rate,sub_ledger_id,credits,debits,accounting_entries_id,accounts_name,accounts_level,sub_ledger_name,total_credits_ex,total_debits_ex)values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
	public static final String DELETE_ACCOUNTING_ASPECTS = "update accounting.accounting_entries set status=?,update_ts=?,update_user_id=?,update_role_name=? where id=?";
	public static final String GET_ACCOUNTING_ASPECTS_INFO_BY_ORGANIZATION = "select date_of_creation,journal_no,type_id,status,total_debits,currency_id,id from accounting.accounting_entries where organization_id=?";
	public static final String GET_ACCOUNTING_ASPECTS_INFO_BY_ORGANIZATION_USER_ROLE = "select date_of_creation,journal_no,type_id,status,total_debits,currency_id,id from accounting.accounting_entries where organization_id=? and user_id=? and role_name=?";
	public static final String GET_ACCOUNTING_ASPECTS_INFO_ORGANIZATION= "select date_of_creation,journal_no,type_id,status,total_debits,currency_id,id from accounting.accounting_entries where organization_id=?";
	public static final String GET_ACCOUNTING_ASPECTS_GENERAL_INFO_BY_ID = "select id,date_of_creation,type_id,journal_no,location_id,approver1_id,approver2_id,approver3_id,currency_id,notes,total_credits,total_debits,difference,status,user_id,organization_id,isSuperAdmin,is_registered_location,create_ts,role_name from accounting.accounting_entries where id=?";
	public static final String GET_ACCOUNTING_ASPECTS_ITEMS_BY_ID = "select id,accounts_id,sub_ledger_id,description,currency_id,exchange_rate,credits,debits,status,accounting_entries_id,accounts_level,accounts_name,sub_ledger_name,total_credits_ex,total_debits_ex from accounting.accounting_entries_item_details where accounting_entries_id=? and status not IN(?)";
	public static final String UPDATE_ACCOUNTING_ASPECTS_GENERAL_INFORMATION = "update accounting.accounting_entries set date_of_creation=?,approver1_id=?,approver2_id=?,approver3_id=?,currency_id=?,journal_no=?,location_id=?,notes=?,type_id=?,total_credits=?,total_debits=?,difference=?,organization_id=?,update_user_id=?,isSuperAdmin=?,update_ts=?,is_registered_location=?,status=?,update_role_name=? where id=?";
	public static final String UPDATE_ACCOUNTING_ASPECTS_ITEM_INFORMATION = "update accounting.accounting_entries_item_details set accounts_id=?,currency_id=?,description=?,exchange_rate=?,sub_ledger_id=?,credits=?,debits=?,status=?,update_ts=?,accounts_name=?,accounts_level =?,sub_ledger_name=?,total_credits_ex=?,total_debits_ex=? where id=?";
	public static final String MODIFY_ACCOUNTING_ASPECTS_STATUS = "update accounting.accounting_entries set status=?,update_ts=? where id=?";
	public static final String MODIFY_ACCOUNTING_ASPECTS_ITMES_STATUS = "update accounting.accounting_entries_item_details set status=?,update_ts=? where accounting_entries_id=?";
	public static final String GET_ALL_FILTERED_ACOOUNTING_ASPECTS = "select id,journal_no,date_of_creation,user_id,status,total_debits,currency_id,type_id from accounting.accounting_entries where organization_id=?";
	public static final String CHANGE_SINGLE_ITEM_STATUS = "update accounting.accounting_entries_item_details set status=?,update_ts=? where id=?";
	public static final String CHECK_JOURNAL_NUMBER_EXIST = "select * from accounting.accounting_entries where organization_id=? and journal_no=?";
	public static final String CREATE_ACCOUTING_ENTRY_UPLOAD = "insert into accounting.accounting_entries(date_of_creation,journal_no,type_id,location_id,organization_id,user_id,isSuperAdmin,difference,status,total_debits,total_credits,role_name,currency_id)values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
	public static final String CREATE_ACCOUTING_ENTRY_ITEM_UPLOAD = "insert into accounting.accounting_entries_item_details(accounts_id,accounts_name,accounts_level,sub_ledger_id,currency_id,exchange_rate,credits,debits,accounting_entries_id,total_debits_ex,total_credits_ex,sub_ledger_name)values(?,?,?,?,?,?,?,?,?,?,?,?)";
	public static final String GET_ACCOUNTING_ENTRY_ID="select id from accounting.accounting_entries where organization_id=? and journal_no=?";
	public static final String GET_ACCOUNTING_ENTRY_ITEM_ID="select id from accounting.accounting_entries_item_details where accounting_entries_id=? and accounts_id=? and sub_ledger_id=?";
	public static final String GET_ACCOUNTING_ENTRY_GENERAL_INFO_BY_ID="select date_of_creation,type_id,journal_no,is_registered_location,location_id,approver1_id,approver2_id,approver3_id,status from accounting_entries where organization_id=? and id=?";
	public static final String GET_ACCOUNTING_ENTRY_ITEMS_INFO_BY_ID="select accounts_id,sub_ledger_id,currency_id,exchange_rate,credits,debits from accounting_entries_item_details where accounting_entries_id=?";
	public static final String UPDATE_GENERAL_INFO="update accounting.accounting_entries set status=?,total_credits=?,total_debits=?,difference=? where id=?";
    public static final String CHECK_LEDGER_EXIST="select aeid.id,aeid.credits,aeid.debits from accounting.accounting_entries ae, accounting.accounting_entries_item_details aeid "
    		+ " where ae.organization_id=? and ae.journal_no=? and ae.id=aeid.accounting_entries_id and "
    		+ " aeid.accounts_name=?";  
    
    public static final String GET_CREDIT_DEBIT_FOR_JOURNAL="select aeid.id,aeid.credits,aeid.debits,aeid.exchange_rate from accounting.accounting_entries ae, accounting.accounting_entries_item_details aeid "
    		+ " where ae.organization_id=? and ae.journal_no=? and ae.id=aeid.accounting_entries_id"; 
    
	public static final String UPDATE_ACCOUNTING_ASPECTS_ITEM_INFORMATION_MINIMAL = "update accounting.accounting_entries_item_details set"
			+ " exchange_rate=?,credits=?,debits=?,update_ts=?,total_credits_ex=?,total_debits_ex=? where id=?";




}

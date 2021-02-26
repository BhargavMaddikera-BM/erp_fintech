package com.blackstrawai.externalintegration.banking.perfios;

public class PerfiosConstants {
 public static final String CREATE_USER = "INSERT INTO banking.user_registration_perfios (user_id, role_name, organization_id, create_ts) VALUES (?,?,?,?)";
public static final String IS_USER_EXISTS = "SELECT * from banking.user_registration_perfios where organization_id=? and user_id=? and role_name=?";
public static final String FETCH_HASH_VALUE = "SELECT id,hash_value from banking.user_registration_perfios where organization_id=? and user_id=? and role_name=?";
public static final String UPDATE_USER="update banking.user_registration_perfios set hash_value=? where id=?";
/*public static final String CREATE_USER_ACCOUNT_TRANSACTION = "insert into banking.perfios_user_account_transaction "
		+ "(user_id, create_ts, status, txn_seq_id, xn_date, xn_details, cheque_number, xn_amount, balance, category_id, "
		+ "user_comment, split_reference_id, xn_id, category, i_type, name, instId, account_id) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
public static final String UPDATE_STATUS = "update banking.perfios_user_account_transaction set status=? where user_id=?";
public static final String FETCH_USER_ACCOUNT_TRANSACTION_USER_ID_INST_ID = "SELECT status, txn_seq_id, xn_date, xn_details, cheque_number, xn_amount, balance, category_id, "
		+ "user_comment, split_reference_id, xn_id, category, i_type, name, instId FROM banking.perfios_user_account_transaction WHERE user_id=? AND instId=?";
public static final String IS_USER_ID_INST_ID_EXIST = "SELECT * FROM banking.perfios_user_account_transaction WHERE user_id=? AND instId=?";
public static final String GET_ACCOUNT_ID_FROM_USER_TRANSACTION = "SELECT DISTINCT account_id FROM banking.perfios_user_account_transaction WHERE user_id=? AND instId=? AND account_id IS NOT NULL";
public static final String DELETE_USER_TRANSACTIONS_USER_ID_INST_ID = "DELETE FROM banking.perfios_user_account_transaction WHERE user_id=? AND instId=?";
*/
public static final String CREATE_BANK_STATEMENT_TRANSACTION = "insert into banking.bank_statement (user_id,organization_id,role_name,account_id,currency,bank_name,transaction_date," + 
		"value_date,reference_no,description,debit_amount,credit_amount,running_balance,source,data,user_comment,transaction_id,category) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
public static final String UPDATE_BANK_STATEMENT_TRANSACTION = "update banking.bank_statement set user_id=?,organization_id=?,role_name=?,account_id=?,currency=?,bank_name=?,transaction_date=?," + 
		"value_date=?,reference_no=?,description=?,debit_amount=?,credit_amount=?,running_balance=?,source=?,data=?,user_comment=?,transaction_id=?,category=? where transaction_id=?";
public static final String CHECK_BANK_STATEMENT_TRANSACTION_EXISTS ="SELECT * FROM banking.bank_statement WHERE organization_id=? and transaction_id=?";
public static final String CREATE_USER_ACCOUNT_TRANSACTION= "insert into banking.user_account_transaction_perfios(user_id ,txn_seq_id ,xn_date,xn_details,cheque_number,xn_amount,balance,category_id,user_comment,split_reference_id,xn_id,category,i_type,account_name,inst_id,account_id,inst_name,currency,txn_date ) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
public static final String GET_USER_INFO = "SELECT id, organization_id, user_id, role_name FROM banking.user_registration_perfios WHERE hash_value = ?";
public static final String GET_ACCOUNT_NAMES_FOR_USER_ROLE = "select uatp.id,uatp.account_name,uatp.inst_name from user_account_transaction_perfios uatp JOIN user_registration_perfios urp ON uatp.user_id=urp.hash_value where urp.organization_id=? and urp.user_id=? AND urp.role_name=?";

public static final String GET_RECENT_TRANSACTION_BY_ACCOUNT_NAME ="SELECT xn_details,xn_amount FROM user_account_transaction_perfios WHERE user_id=? and account_name=? ORDER BY xn_date DESC LIMIT 5";

public static final String CHECK_ACCOUNT_TXN_SEQ_ID="select * from banking.user_account_transaction_perfios where user_id=? and account_id=? and txn_seq_id=?";

public static final String UPDATE_USER_ACCOUNT_TRANSACTION="update banking.user_account_transaction_perfios "
		+ "set xn_date=?,xn_details=?,cheque_number=?,"
		+ "xn_amount=?,balance=?,category_id=?,user_comment=?,"
		+ "split_reference_id=?,xn_id=?,category=?,"
		+ "i_type=?,account_name=?,"
		+ "currency=?,txn_date=? where user_id=? and account_id=? and txn_seq_id=?";
public static final String GET_ACCOUNT_BALANCE="SELECT balance FROM user_account_transaction_perfios WHERE user_id=? and account_name=? ORDER BY xn_date DESC LIMIT 1";
public static final String GET_ACCOUNT_STATEMENT="SELECT xn_date,xn_details,xn_amount,cheque_number,balance FROM user_account_transaction_perfios WHERE user_id=? and account_name=? ORDER BY xn_date DESC ";
public static final String GET_ACCOUNT_STATEMENT_WITH_FILTER="SELECT xn_date,xn_details,xn_amount,cheque_number,balance FROM user_account_transaction_perfios WHERE user_id=? and account_name=? ";
public static final String GET_LIST_OF_ACCOUNT_ID="SELECT distinct(account_id) FROM user_account_transaction_perfios WHERE user_id=? and account_name=?";


}



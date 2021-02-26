package com.blackstrawai.ap;

public class ExpensesConstants {


    public static final String GET_ALL_NATURE_OF_SPENDING = "select id, spending_desc from nature_of_spending where is_valid = 1";
    public static final String GET_ALL_EXPENSE_STATUS = "select id, status_desc from expense_status where is_valid = 1";
    public static final String GET_ALL_ACCOUNT_TYPE = "select id, account_type from expense_account_type where is_valid = 1";

    public static final String INSERT_EXPENSES = "insert into expenses(create_ts,nature_of_spending_id,expense_status_id,is_cash,user_id,organization_id,employee_id,issuperadmin)values(?,?,?,?,?,?,?,?)";
    public static final String INSERT_ADDED_EXPENSES = "insert into added_expenses(expenses_account_id,expenses_id,notes,amount,vendor_id,customer_id,create_ts,update_ts,status,expenses_account_level,expenses_account_name)values(?,?,?,?,?,?,?,?,?,?,?)";

    public static final String GET_ALL_EXPENSES_BY_ORGANIZATION_ID = "Select e.id, e.create_ts, e.nature_of_spending_id,nos.spending_desc ,e.expense_status_id,es.status_desc ,e.is_cash, e.organization_id,e.employee_id, e.user_id,e.isSuperAdmin,e.status , sum(case when ae.status ='DEL' THEN 0 ELSE Cast(ae.amount as decimal(65,2)) END) as amount " +
            " from expenses e join nature_of_spending nos on e.nature_of_spending_id = nos.id join expense_status es on es.id = e.expense_status_id  left join added_expenses ae on ae.expenses_id = e.id " +
            " where organization_id =  ? and e.status not in ('DEL')  group by e.id, e.create_ts, e.nature_of_spending_id,nos.spending_desc ,e.expense_status_id,es.status_desc ,e.is_cash, e.organization_id,e.employee_id, e.user_id,e.isSuperAdmin,e.status ";

    public static final String GET_EXPENSE_BY_EXPENSE_ID = "Select e.id, e.create_ts, e.nature_of_spending_id,nos.spending_desc ,e.expense_status_id,es.status_desc ,e.is_cash, e.organization_id,e.employee_id, e.user_id,e.isSuperAdmin,e.status  " +
            " from expenses e join nature_of_spending nos on e.nature_of_spending_id = nos.id join expense_status es on es.id = e.expense_status_id  left join added_expenses ae on ae.expenses_id = e.id " +
            " where organization_id =  ? and e.id = ? and e.status not in ('DEL')  ";

    public static final String GET_ADDED_EXPENSE_BY_EXPENSE_ID = "Select ae.id, expenses_account_id,expenses_id,notes, Cast(amount as decimal(65,2)) as amount ,vendor_id,customer_id,ae.status,expenses_account_name,expenses_account_level,coalesce(cgi.customer_display_name,'NA'), coalesce(vgi.vendor_display_name,'NA') " +
            " from added_expenses ae Left join accounts_receivable.customer_general_information cgi on ae.customer_id = cgi.id Left join vendor_general_information vgi on ae.vendor_id = vgi.id " +
            "   where expenses_id = ? and ae.status not in ('DEL')";
    public static final String MODIFY_EXPENSE_STATUS = "update expenses set status=?,update_ts=? where id=?";
    public static final String MODIFY_EXPENSE_ADDED_STATUS = "update added_expenses set status=?,update_ts=? where expenses_id=?";

    public static final String GET_ALL_FILTERED_EXPENSE_BASE_QUERY = "Select e.id, e.create_ts, e.nature_of_spending_id,nos.spending_desc ,e.expense_status_id,es.status_desc ,e.is_cash, e.organization_id,e.employee_id, e.user_id,e.isSuperAdmin,e.status , sum(Cast(ae.amount as decimal(65,2))) as amount " +
            " from expenses e join nature_of_spending nos on e.nature_of_spending_id = nos.id join expense_status es on es.id = e.expense_status_id  join added_expenses ae on ae.expenses_id = e.id ";
    public static final String GET_ALL_FILTERED_EXPENSE_ATTACHMENT_QUERY =" join document_mgmt.document_erp a on a.type_id = e.id and a.status not in ('DEL') and a.type = 'expenses' "  ;
    public static final String GET_ALL_FILTERED_EXPENSE_WHERE_QUERY = " where organization_id =  ?  AND  e.status not in ('DEL') ";
    public static final String GET_ALL_FILTERED_EXPENSE_ATTACHMENT_WHERE_QUERY = " AND a.type_id is null ";
    public static final String GET_ALL_FILTERED_EXPENSE_WHERE_DATE_QUERY = " AND  e.create_ts >= ? AND e.create_ts < ?  ";
    public static final String GET_ALL_FILTERED_EXPENSE_WHERE_STATUS_QUERY = " AND e.expense_status_id =  ?  ";
    public static final String GET_ALL_FILTERED_EXPENSE_WHERE_NATURE_QUERY = " AND  e.nature_of_spending_id =  ?   ";
    public static final String GET_ALL_FILTERED_EXPENSE_GROUP_BY = "group by e.id, e.create_ts, e.nature_of_spending_id,nos.spending_desc ,e.expense_status_id,es.status_desc ,e.is_cash, e.organization_id,e.employee_id, e.user_id,e.isSuperAdmin,e.status ";
    public static final String GET_ALL_FILTERED_EXPENSE_HAVING_QUERY = " Having sum(Cast(ae.amount as decimal(65,5))) between ?  and ?  ";
    public static final String LEFT =  " left ";

    public static final String UPDATE_EXPENSES = " update expenses set  create_ts = ? , nature_of_spending_id = ? , expense_status_id = ? , is_cash = ? , user_id= ? , organization_id= ? , employee_id= ? , issuperadmin =? , update_ts = ? , status = ? Where id = ? ";
    public static final String UPDATE_ADDED_EXPENSES = " update added_expenses set expenses_account_id = ? , expenses_id = ? , notes = ? , amount = ? , vendor_id = ? , customer_id = ? , create_ts = ? , update_ts = ? ,  status  = ? , expenses_account_level = ? , expenses_account_name = ?  where id = ? and expenses_id = ? ";
}

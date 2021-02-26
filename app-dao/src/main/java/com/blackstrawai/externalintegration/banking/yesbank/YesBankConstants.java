package com.blackstrawai.externalintegration.banking.yesbank;

import java.util.HashMap;
import java.util.Map;

public class YesBankConstants {

  // Query

  public static final String RETRIEVE_USER_INFO =
      "Select user_name,auth_key_1,auth_key_2 from yesbank_api_banking_registration " +
              "where organization_id=? and customer_id=? " +
              "and account_number=? " +
              "and user_id=? " +
              "and role_name=? " +
              "and API_banking_enabled='Y' " +
              "and status='ACT'";

  public static final String SAVE_PAYMENT_INFO =
      "INSERT INTO yesbank_payment_information (payment_transfer_reference_no_decifer,customer_id, account_number,payment_mode, " +
              "amount,request_json,response_json,payment_transfer_reference_no_yesbank,organization_id,role_name,user_id," +
              "beneficiary_id,beneficiary_type,bill_reference,payment_reference,ui_request_json,status) "
          + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
  public static final String UPDATE_PAYMENT_INFO =
	      "update yesbank_payment_information set payment_transfer_reference_no_decifer=?,customer_id=?, account_number=?,payment_mode=?, " +
	              "amount=?,request_json=?,response_json=?,payment_transfer_reference_no_yesbank=?,organization_id=?,role_name=?,user_id=?," +
	              "beneficiary_id=?,beneficiary_type=?,bill_reference=?,payment_reference=?,ui_request_json=?,status=? where id=? ";
  
  public static final String UPDATE_BULK_PAYMENT_INFO =
	      "update yesbank_payment_information set response_json=?,status=? where  organization_id=?  and file_identifier=? ";
  
  public static final String UPDATE_SINGLE_TXN_BULK_PAYMENT =
	      "update yesbank_payment_information set response_json=?,status=?, payment_transfer_reference_no_yesbank =? ,update_ts=? , update_user_id=? ,update_role_name=? where  organization_id=?  and payment_transfer_reference_no_decifer=? and customer_id =?";

  public static final String SAVE_BULK_PAYMENT_INFO = "INSERT INTO yesbank_payment_information (payment_transfer_reference_no_decifer,customer_id, account_number,payment_mode, " +
          "amount,request_json,response_json,payment_transfer_reference_no_yesbank,organization_id,role_name,user_id," +
          "beneficiary_name,beneficiary_account_no,bill_reference,payment_reference,ui_request_json,status,payment_type,file_identifier,beneficiary_ifsc) "
      + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
  
  public static final String RETRIEVE_PAYMENT_LIST =
      "Select id,beneficiary_type,response_json,status ,amount,request_json,payment_transfer_reference_no_decifer,create_ts from yesbank_payment_information where organization_id=? " +
              "and role_name=? and user_id=? and account_number=? and payment_type='Single' order by create_ts";
  
  
  public static final String RETRIEVE_BULK_PAYMENT_LIST = "SELECT file_identifier,amount,create_ts ,status,id FROM banking.yesbank_payment_information where organization_id = ?  and payment_type='Bulk' and account_number =? order by id desc";
  
  public static final String RETRIEVE_BULK_PAYMENT_LIST_USER_ROLE = "SELECT file_identifier,amount,create_ts ,status,id FROM banking.yesbank_payment_information where organization_id = ?  and payment_type='Bulk' and account_number =? and role_name = ? and user_id = ? order by id desc";

  
  public static final String RETRIEVE_BULK_PAYMENT_DETAIL = "select payment_transfer_reference_no_decifer ,account_number , payment_mode,amount , beneficiary_ifsc,beneficiary_name,beneficiary_account_no ,status,request_json,customer_id,ui_request_json FROM banking.yesbank_payment_information where organization_id = ? and payment_type = 'Bulk' and file_identifier = ?";
  
  
  public static final String FILTER_PAYMENT_LIST =
      "Select\n"
          + "          ypi.id,ypi.beneficiary_type,ypi.response_json,ypi.status , ypi.amount,ypi.request_json,ypi.payment_transfer_reference_no_decifer,ypi.create_ts "
          + "          from\n"
          + "          yesbank_payment_information ypi\n"
          + "          join yesbank_beneficiary_information ybi on\n"
          + "          ypi.beneficiary_id = ybi.id\n"
          + "          where\n "
          + "         ypi.organization_id=?  and ypi.role_name=? and ypi.user_id=? and ypi.account_number=?";

  public static final String VIEW_BENEFICIARY_TRANSACTION_LIST =
      "Select id,beneficiary_type,response_json,status ,amount,request_json,payment_transfer_reference_no_decifer,create_ts from yesbank_payment_information " +
              "where organization_id=? and role_name=? and user_id=? and beneficiary_id=? order by create_ts";


  // Beneficiary
  public static final String CREATE_NEW_BENEFICIARY =
      "insert into yesbank_beneficiary_information( beneficiary_type, beneficiary_name, account_number, account_type, IFSC_code, organization_id, status, create_ts, user_id ,role_name,mobile_no,email) values (?,?,?,?,?,?,?,?,?,?,?,?)";

  public static final String GET_BENEFICIARY_BY_ID_TYPE =
	      "SELECT beneficiary_NAME FROM yesbank_beneficiary_information WHERE id=? AND beneficiary_type=?";

  
  public static final String UPDATE_BENEFICIARY =
      "update yesbank_beneficiary_information set beneficiary_type=?, beneficiary_name=?, account_number=?, account_type=?, IFSC_code=?,update_ts=?,update_user_id=? ,update_role_name=?, mobile_no=?, email=? where id=?";

  public static final String CHECK_BENEFICIARY_EXIST =
      "select * from yesbank_beneficiary_information  where organization_id= ? and account_number = ? and IFSC_code=?";

  public static final String CHECK_BENEFICIARY_IF_EXIST =
          "select * from yesbank_beneficiary_information  where organization_id= ? and account_number = ?  and IFSC_code=? and id not in(?)";

  public static final String CHECK_BANK_MASTER_BENEFICIARY_IF_EXIST =
          "select * from bank_master_bank_account  where organization_id= ? and account_number = ? and ifsc_code=? and account_name=? and beneficiary_type=?";

  public static final String GET_BENEFICIARY_BY_ID =
      "select beneficiary_type, beneficiary_name, account_number, account_type, IFSC_code, organization_id, status,  user_id,  role_name,mobile_no,email  FROM banking.yesbank_beneficiary_information where id = ?";

  public static final String GET_VENDOR_BENEFICIARY_FOR_ORG =
      "SELECT vgi.id, vbd.account_holder_name, vgi.vendor_display_name ,vbd.beneficiary_status,vbd.id ,vbd.account_no , vbd.ifsc_code FROM "
          + "  accounts_payable.vendor_general_information vgi JOIN accounts_payable.vendor_bank_details vbd ON vbd.vendor_general_information_id = vgi.id "
          + "  where vgi.organization_id =  ? and  vbd.is_default = 1 and vgi.status = 'ACT' and vbd.status = 'ACT' ";

  public static final String GET_CUSTOMER_BENEFICIARY_FOR_ORG =
      "SELECT cgi.id, cbd.account_holder_name, cgi.customer_display_name ,cbd.beneficiary_status,cbd.id  ,cbd.account_no , cbd.ifsc_code FROM accounts_receivable.customer_general_information cgi "
          + " JOIN accounts_receivable.customer_bank_details cbd ON cbd.customer_general_information_id = cgi.id "
          + " where cgi.organization_id =  ? and  cbd.is_default = 1 and cgi.status = 'ACT' and cbd.status = 'ACT' ";

  public static final String GET_EMPLOYEE_BENEFICIARY_FOR_ORG =
      "SELECT em.id, ebd.account_holder_name, CONCAT( em.first_name ,' ' , em.last_name ) as employee_name ,ebd.beneficiary_status,ebd.id ,ebd.account_no ,ebd.ifsc_code"
          + " FROM accounts_payable.employee em JOIN accounts_payable.employee_bank_details ebd ON ebd.employee_id = em.id "
          + " where em.organization_id = ? and  ebd.is_default = 1 and em.status = 'ACT' and ebd.status = 'ACT' ";

  public static final String GET_ORG_BENEFICIARY_FOR_ORG =
      "SELECT id, account_name,beneficiary_status,account_number,ifsc_code FROM banking.bank_master_bank_account where organization_id = ? and status = 'ACT' ";

  public static final String GET_YES_BANK_BENEFICIARY_FOR_ORG =
      "SELECT id,beneficiary_name,beneficiary_type,status,account_number ,IFSC_code FROM banking.yesbank_beneficiary_information where organization_id= ?";

  public static final String DISABLE_BENEFICIARY =
      "update yesbank_beneficiary_information set status=?,update_ts=? where id=?";

  public static final String DISABLE_EMPLOYEE_BENEFICIARY =
      "update employee_bank_details set beneficiary_status=?,update_ts=? where id=?";

  public static final String DISABLE_VENDOR_BENEFICIARY =
      "update vendor_bank_details set beneficiary_status=?,update_ts=? where id=?";

  public static final String DISABLE_CUSTOMER_BENEFICIARY =
      "update customer_bank_details set beneficiary_status=?,update_ts=? where id=?";

  public static final String DISABLE_BANK_MASTER_BENEFICIARY =
      "update bank_master_bank_account set beneficiary_status=?,update_ts=? where id=?";
  
  public static final String GET_TOTAL_AMNT_PAID_BY_BENEFICIARY =
	      " select sum(amount) from banking.yesbank_payment_information ypi where ypi.organization_id= ? and ypi.beneficiary_id = ? and ypi.beneficiary_type = ? ";

  public static final String GET_LAST_TXN_DONE_BY_BENEFICIARY =
	      " select create_ts from banking.yesbank_payment_information ypi where ypi.organization_id= ? and ypi.beneficiary_id = ? and ypi.beneficiary_type = ?  order by create_ts desc limit  1 ";

  // YesBank API registration
  public static final String GET_API_BANKING_REQUESTED_ID =
      " SELECT id,API_banking_enabled FROM banking.yesbank_api_banking_registration where organization_id = ? and customer_id = ? and account_number=?  ";

  public static final String GET_YES_BANK_ACCOUNT_DETAILS = "select id, customer_id, account_number, ecollect_code, account_name, branch, mobile_no, API_banking_enabled, status,is_payments_allowed,erp_banking_account_id,erp_banking_account_type,ifsc_code  FROM banking.yesbank_api_banking_registration where id  = ?" ;
  
  public static final String ENABLE_API_BANKING_WHEN_ALREADY_REQUESTED =
      "update banking.yesbank_api_banking_registration set  user_name =?, auth_key_1=? ,auth_key_2=?, API_banking_enabled =?, ecollect_code =? ,mobile_no = ? where  id =? ";

  public static final String UPDATE_BANK_ACCOUNT_SETTING ="update banking.yesbank_api_banking_registration set account_name =? ,ifsc_code=? ,mobile_no=?,is_payments_allowed=?,erp_banking_account_id=? ,erp_banking_account_type=?,API_banking_enabled=? where id =? ";
		  
  public static final String ENABLE_API_BANKING =
      " insert into banking.yesbank_api_banking_registration (customer_id, account_number, user_name, auth_key_1,auth_key_2, ecollect_code, API_banking_enabled, status, organization_id, role_name, create_ts,  user_id ,mobile_no)  values (?,?,?,?,?,?,?,?,?,?,?,?,?)";

  public static final String REQUEST_API_BANKING =
      " insert into banking.yesbank_api_banking_registration (customer_id, account_number, account_name, branch , mobile_no , ecollect_code, API_banking_enabled, status, organization_id, role_name, create_ts,  user_id )  values (?,?,?,?,?,?,?,?,?,?,?,?)";
  
  public static final String GET_ACCOUNT_NAMES_FOR_USER_ROLE = "SELECT id,customer_id,account_number,API_banking_enabled FROM yesbank_api_banking_registration WHERE organization_id=? and user_id=? AND role_name=?";


  public static final String GET_PAYMENT_VENDOR_BENEFICIARY_INFO="SELECT vgi.id, vbd.account_holder_name, vgi.vendor_display_name ,vbd.beneficiary_status,vbd.id,vbd.account_no,vbd.ifsc_code,vgi.mobile_no ,vgi.email FROM \n" +
          "           vendor_general_information vgi JOIN vendor_bank_details vbd ON vbd.vendor_general_information_id = vgi.id \n" +
          "            where vgi.organization_id = ? and  vbd.is_default = 1 and vgi.status = 'ACT' and vbd.status = 'ACT' and vbd.beneficiary_status = 'ACT'";

  public static final String GET_PAYMENT_CUSTOMER_BENEFICIARY_INFO =
          "SELECT cgi.id, cbd.account_holder_name, cgi.customer_display_name ,cbd.beneficiary_status,cbd.id,cbd.account_no,cbd.ifsc_code,cgi.mobile_no ,cgi.email_id " +
                  "  FROM customer_general_information cgi "
                  + " JOIN customer_bank_details cbd ON cbd.customer_general_information_id = cgi.id "
                  + " where cgi.organization_id =  ? and  cbd.is_default = 1 and cgi.status = 'ACT' and cbd.status = 'ACT' and cbd.beneficiary_status = 'ACT' ";

  public static final String GET_PAYMENT_EMPLOYEE_BENEFICIARY_INFO =
          "SELECT em.id, ebd.account_holder_name, CONCAT( em.first_name , ' ' ,em.last_name ) as employee_name ,beneficiary_status,ebd.id ,ebd.account_no,ebd.ifsc_code,em.mobile_no ,em.email "
                  + " FROM employee em JOIN employee_bank_details ebd ON ebd.employee_id = em.id "
                  + " where em.organization_id = ? and  ebd.is_default = 1 and em.status = 'ACT' and ebd.status = 'ACT'  and ebd.beneficiary_status = 'ACT' ";

  public static final String GET_PAYMENT_ORG_BENEFICIARY_INFO =
          "SELECT id, account_name,bank_name,beneficiary_status,id,account_number,ifsc_code,'' as mobile ,'' as email FROM bank_master_bank_account where organization_id = ? and status = 'ACT' and beneficiary_status = 'ACT' ";

  public static final String GET_PAYMENT_YES_BANK_BENEFICIARY_INFO =
          "SELECT id,beneficiary_name,beneficiary_type,status,id,account_number,IFSC_code,mobile_no,email  FROM yesbank_beneficiary_information where organization_id= ? and status = 'ACT'";

  public static final String BILL_REFERENCE= "select igi.invoice_no from invoice_general_information igi\n" +
          "          where vendor_id =? and status='PD' ";

  public static final String PAYMENT_REFERENCE= "select igi.invoice_number from invoice_general_information igi\n" +
          "          where customer_id  =? and status='PD' ";

  public static final String SAVE_NEW_ACCOUNT_INFO =
          "INSERT INTO yesbank_new_account_setup (organization_name,consitution, contact_person,contact_number, " +
                  "branch,organization_id ,role_name,create_ts,user_id) "
                  + "VALUES (?,?,?,?,?,?,?,?,?)";

  public static final String SAVE_BANK_STATEMENT =
          "INSERT INTO yesbank_bank_statement (customer_id,account_number, txn_start_date,txn_end_date, " +
                  "organization_id,create_ts,create_user_id) "
                  + "VALUES (?,?,?,?,?,?,?)";

  public static final String SAVE_BANK_STATEMENT_INFO =
          "INSERT INTO yesbank_bank_statement_info (bank_statement_id,txn_date, value_date,description, " +
                  "cheque_ref_no,debit_amount,credit_amount,balance_amount,create_ts,create_user_id) "
                  + "VALUES (?,?,?,?,?,?,?,?,?,?)";

  public static final String GET_AVAILABLE_BANK_STATEMENT =
      "select ybs.customer_id ,ybs.account_number,ybsi.txn_date , ybsi.value_date , ybsi.description , ybsi.cheque_ref_no ,\n"
          + "ybsi.debit_amount ,ybsi.credit_amount ,ybsi.balance_amount \n"
          + "from yesbank_bank_statement ybs join yesbank_bank_statement_info ybsi\n"
          + "on ybs.id = ybsi.bank_statement_id\n"
          + "where  ybs.organization_id =?\n"
          + "and ybs.customer_id =?\n"
          + "and ybs.account_number =?\n";
        //  + "and ybs.txn_start_date =?\n"
       //   + "and ybs.txn_end_date =?\n";

  public static final String CHECK_IF_BANK_STATEMENT_AVAILABLE =
          "select count(1) as count " +
                  " from yesbank_bank_statement ybs \n"
                  + "where  ybs.organization_id =?\n"
                  + "and ybs.customer_id =?\n"
                  + "and ybs.account_number =?\n";
               //   + "and ybs.txn_start_date =?\n"
               //   + "and ybs.txn_end_date =?\n";

  public static final String PAYMENT_TRANSACTIONS = "select id,ui_request_json,create_ts,payment_transfer_reference_no_decifer,status " +
          "from yesbank_payment_information ypi where organization_id=? and id=? " ;

  public static final String GET_MOBILE_NO_FOR_CUST="SELECT mobile_no FROM yesbank_api_banking_registration WHERE organization_id=? AND user_id=? AND  role_name=? AND  account_number=? AND customer_id=?";
 
	public static final String GET_RECENT_TRANSACTIONS = "select ybsi.description , ybsi.debit_amount ,ybsi.credit_amount from yesbank_bank_statement ybs join yesbank_bank_statement_info ybsi" + 
			" on ybs.id = ybsi.bank_statement_id where  ybs.organization_id =? and ybs.customer_id =? and ybs.account_number =? and ybsi.create_user_id=? and ybsi.role_name=? ORDER BY ybsi.txn_date ASC LIMIT 5" ;

  public static final String GET_ACCOUNT_STATEMENT_WITH_FILTER =
      "select ybs.customer_id ,ybs.account_number,ybsi.txn_date , ybsi.value_date , ybsi.description , ybsi.cheque_ref_no ,\n"
          + "         ybsi.debit_amount ,ybsi.credit_amount ,ybsi.balance_amount \n"
          + "         from yesbank_bank_statement ybs join yesbank_bank_statement_info ybsi\n"
          + "        on ybs.id = ybsi.bank_statement_id\n"
          + "        where  ybs.organization_id =?\n"
          + "        and ybs.customer_id =?\n"
          + "        and ybs.account_number =?\n"
          + "        and ybs.create_user_id =?\n"
          + "        and ybs.role_name =?\n";
  
  public static final String GET_WORKFLOW_DATA_FOR_PAYMENT =
	      "Select id,amount,status,customer_id,request_json,ui_request_json,organization_id,role_name,user_id,payment_transfer_reference_no_decifer,account_number,payment_mode,beneficiary_id,beneficiary_type,bill_reference,payment_reference,payment_type,file_identifier from yesbank_payment_information where id=? ";

  public static final String REJECT_OR_DECLINE_PAYMENT =
	      "update yesbank_payment_information set status=? where id=? ";

  public static final String GET_TOTAL_AMOUNT = "select sum(amount) from banking.yesbank_payment_information where  organization_id = ?  and  status=? and file_identifier=? ";
 
  public static final String UPDATE_BULK_PAYMENT = "UPDATE yesbank_payment_information ybpi SET response_json=?,ybpi.STATUS=?,ybpi.file_name=? WHERE ybpi.file_identifier=?";
  
  public static final String REJECT_OR_DECLINE_BULK_PAYMENT = "UPDATE yesbank_payment_information ybpi SET ybpi.STATUS=? WHERE ybpi.file_identifier=?";
  
  public static final String GET_BULK_PAYMENT_AMOUNT = "SELECT sum(yp.amount) FROM banking.yesbank_payment_information yp where yp.file_identifier=?";
  

  public static final String GET_PAYMENT_ORG_BANK_ACCOUNT =
          "SELECT id, account_name,bank_name,beneficiary_status,id,account_number,ifsc_code,'' as mobile ,'' as email FROM bank_master_bank_account where organization_id = ? and status = 'ACT' ";

  //ERP Constants 
  public static final String MODULE_VENDOR_PAYMENT ="vendor";
  public static final String MODULE_INVOICE_PAYMENT ="invoice";
  public static final String MODULE_PAYRUN_PAYMENT ="payrun";
  public static final String MODULE_PO_PAYMENT ="po";

  public static final String KEYCONTACT_VENDOR ="vendor";
  public static final String KEYCONTACT_EMPLOYEE ="employee";

  public static final String INSERT_INTO_ERP_PAYMENTS ="insert into erp_bulk_payment (key_contact_id, key_contact_type, module_type_id, module_type, account_number, account_ifsc, payment_mode, amount, amount_description, txn_date, organization_id, create_ts,  create_user_id,  role_name ,file_identifier,unique_identifier) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
  

  // Error constants
  public static final String ERROR_YBL_CUST_INFO_NOT_AVAIALBLE =
      "Customer Information Is Not Valid";
  public static final String ERROR_YBL_BENEFICIARY_EXIST =
      "Beneficiary details already exist for the organization";
  public static final String ERROR_YBL_NOT_VALID_REQUEST =
      "Account is not available in YES Bank. Please register to proceed";

  public static final String ERROR_YBL_PAYMENT_NOT_AVAILABLE =
          "No Payment Information Is Available For The Requested Transaction";

  // Drop Down Constants
  public static final String BENEFICIARY_TYPE_CORPORATE_COMPANY = "Corporate/Company";
  public static final String BENEFICIARY_TYPE_INDIVIDUAL = "Individual";
  public static final String BENEFICIARY_TYPE_OTHERS = "Others";
  public static final String BENEFICIARY_TYPE_VENDOR = "Vendor";
  public static final String BENEFICIARY_TYPE_CUSTOMER = "Customer";
  public static final String BENEFICIARY_TYPE_EMPLOYEE = "Employee";
  public static final String BENEFICIARY_TYPE_BANK_MASTER = "Own Account";
  public static final String OTHER_BENEFICIARY = "Other Beneficiary";

  public static final String ACCOUNT_TYPE_WITHIN_BANK = "Within Bank";
  public static final String ACCOUNT_TYPE_OTHER_BANK = "Other Bank";

  public static final String KEY_CONTACTS_BENEFICIARY = "Beneficiary";
  public static final String KEY_CONTACTS_CONTACT = "Contact";

  public static final String SELF_BENEFICIARY = "Self";

  public static final Map<Integer, String> paymentModeMapping;

  public static final String SettlementCompleted = "SettlementCompleted";
  public static final String SettlementInProcess = "SettlementInProcess";
  public static final String SettlementReversed = "SettlementReversed";
  public static final String Success = "Success";
  public static final String Failed = "Failed";

  public static final String CHECK_PAYMENT_INITIATED ="select ypi.status from yesbank_payment_information ypi\r\n"
  		+ "join erp_bulk_payment ebp on\r\n"
  		+ "ypi.organization_id = ebp.organization_id and \r\n"
  		+ "ebp.unique_identifier = ypi.payment_transfer_reference_no_decifer\r\n"
  		+ "where   ypi.organization_id=? and ebp.key_contact_id=? and ebp.key_contact_type=?\r\n"
  		+ "and ebp.module_type=? and ebp.module_type_id=? and ypi.status in( 'DRAFT','PENDING')";
  
  
  
  static {
    paymentModeMapping = new HashMap<>();
    paymentModeMapping.put(1, "FT");
    paymentModeMapping.put(2, "NEFT");
    paymentModeMapping.put(3, "RTGS");
    paymentModeMapping.put(4, "IMPS");
    paymentModeMapping.put(5, "ANY");
  }
}

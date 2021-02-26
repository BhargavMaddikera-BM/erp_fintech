package com.blackstrawai.ap;

public class PaymentNonCoreConstants {
	public static final String INSERT_INTO_PAYMENT_NON_CORE = "INSERT INTO payments_non_core(paid_via_id, payment_type_id, payment_ref_no, payment_date, vendor_id, vendor_account_ledger_id, vendor_account_ledger_name, \n"
			+ "currency_id, notes, adjusted_amount, total_amount, po_reference_no, contact_account_ledger_id, contact_account_ledger_name, contact_id, amount_paid, \n"
			+ "difference_amount, pay_type_id, pay_period, pay_run_reference, \n"
			+ "user_id, organization_id, role_name, isSuperAdmin, status, paid_to, create_ts, custom_table_list, payment_mode_id, contact_type, statutory_body, customer_name, employee_name, paid_for, currency_code, "
			+ "exchange_rate, bill_reference, reference_id, invoice_id, reference_type, is_bulk, paid_via_name, contact_name, invoice_reference_name, base_currency_code,general_ledger_data, due_amount, bank_reference_number) "
			+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

	public static final String GET_PAYMENT_TYPES = "SELECT id, payment_type_name FROM payment_type";
	public static final String GET_PAYMENT_REF_NOS = "SELECT payment_ref_no FROM payments_non_core WHERE organization_id = ?";
	public static final String GET_PAYMENT_BY_ID = "SELECT paid_via_id, payment_type_id, payment_ref_no, payment_date, vendor_id, vendor_account_ledger_id, vendor_account_ledger_name, "
			+ "currency_id, notes, adjusted_amount, total_amount, po_reference_no, contact_account_ledger_id, contact_account_ledger_name, contact_id, amount_paid, "
			+ "difference_amount, pay_type_id, pay_period, pay_run_reference, user_id, organization_id, role_name, isSuperAdmin, status, paid_to, custom_table_list, payment_mode_id, contact_type, "
			+ "statutory_body, customer_name, employee_name, paid_for, currency_code, exchange_rate, bill_reference, reference_id, invoice_id, reference_type, is_bulk, paid_via_name, contact_name, invoice_reference_name, base_currency_code, general_ledger_data ,create_ts, due_amount, bank_reference_number "
			+ " FROM payments_non_core WHERE id = ?";

	public static final String GET_PAYMENT_TYPE_BY_ID = "SELECT payment_type_name FROM payment_type WHERE id = ?";
	public static final String BILL_PAYMENTS = "Bill Payments";
	public static final String VENDOR_ADVANCE = "Vendor Advance";
	public static final String GST_PAYMENTS = "GST Payments";
	public static final String TDS_PAYMENTS = "TDS Payments";
	public static final String CUSTOMER_REFUNDS = "Customer Refunds";
	public static final String OTHERS = "Other Payments";
	public static final String EMPLOYEE_PAYMENTS = "Employee Payments";
	public static final String BILL_PAYMENTS_TYPE_BILL = "Bill";
	public static final String BILL_PAYMENTS_TYPE_ADVANCE = "Advance";

	public static final String INSERT_BILL_DETAILS = "INSERT INTO payments_non_core_bill_details (payments_non_core_id, status, bill_reference, bill_amount, total_amount, data, type,vendor_id)  \n"
			+ "VALUES (?,?,?,?,?,?,?,?)";

	public static final String INSERT_GST_DETAILS = "INSERT INTO payments_non_core_gst_details (payments_non_core_id, status, gst_type, paid_amount, data) "
			+ "VALUES (?,?,?,?,?)";

	public static final String INSERT_TDS_DETAILS = "INSERT INTO payments_non_core_tds_details (payments_non_core_id, status, tds_section, type,"
			+ "paid_amount, data) VALUES (?,?,?,?,?,?)";

	public static final String INSERT_CUSTOMER_REFUND = "INSERT INTO payments_non_core_customer_refund_details (payments_non_core_id, status, invoice_id, "
			+ "invoice_amount, reference_id, reference_type, invoice_reference_name) VALUES (?,?,?,?,?,?,?)";

//	public static final String INSERT_CUSTOMER_REFUND = "INSERT INTO payments_non_core_customer_refund_details (payments_non_core_id, status, invoice_id, "
//			+ "invoice_amount, reference_id, reference_type, invoice_reference_name, due_amount) VALUES (?,?,?,?,?,?,?,?)";

	public static final String INSERT_EMPLOYEE_DETAILS = "INSERT INTO payments_non_core_employee_details (payments_non_core_id, status, employee_id, "
			+ "net_pay, data, pay_run, due_amount) VALUES (?,?,?,?,?,?,?)";

	public static final String GET_CREDIT_DETAILS_DROPDOWN = "SELECT p1.id, p1.payment_reference, p1.original_amount, p1.available_amount, "
			+ "p1.vendor_id FROM payments_non_core_vendor_advance p1, payments_non_core p2 WHERE p1.organization_id = ? AND p1.vendor_id = ? AND p2.id = p1.payment_non_core_id AND p2.status in (?,?) AND p2.currency_id = ? AND p1.available_amount > ?";

	public static final String GET_VENDOR_ADVANCE_REFERENCE_BY_ID = "SELECT payment_reference FROM payments_non_core_vendor_advance WHERE  organization_id=? and id=?";

	public static final String ZERO_STRING = "0";
	public static final String EMPTY = "";
	public static final int ZERO = 0;

	public static final String UPDATE_AVAILABLE_AMOUNT_VENDOR_ADVANCE = "UPDATE payments_non_core_vendor_advance SET available_amount =  ? WHERE id = ?";

	public static final String UPDATE_PAYMENT_STATUS = "UPDATE payments_non_core SET status =  ? WHERE id = ?";

	public static final String GET_BILL_DETAILS_DROPDOWN = null;

	public static final Boolean IS_INVOICE_WITH_BILLS = true;

	public static final String FETCH_BILL_DETAILS_BY_PAYMENT_ID = "SELECT id, payments_non_core_id, status, bill_reference, bill_amount, total_amount, "
			+ "data, type,vendor_id FROM payments_non_core_bill_details WHERE payments_non_core_id = ?";

	public static final String FETCH_BILL_DETAILS_BY_ID = "SELECT total_amount FROM payments_non_core_bill_details WHERE id = ?";

	public static final String FETCH_GST_DETAILS_BY_PAYMENT_ID = "SELECT id, payments_non_core_id, status, gst_type, paid_amount, data FROM payments_non_core_gst_details WHERE payments_non_core_id = ?";

	public static final String FETCH_TDS_DETAILS_BY_PAYMENT_ID = "SELECT id, payments_non_core_id, status, tds_section, type, paid_amount, data FROM payments_non_core_tds_details WHERE payments_non_core_id = ?";

	public static final String FETCH_CUSTOMER_REFUND_DETAILS_BY_PAYMENT_ID = "SELECT id, payments_non_core_id, status, invoice_id, invoice_amount, reference_id, reference_type, invoice_reference_name FROM payments_non_core_customer_refund_details WHERE payments_non_core_id = ?";

	public static final String FETCH_CR_BY_PAYMENT_ID_AND_REFEENCE_TYPE = "SELECT id, payments_non_core_id, status, invoice_id, invoice_amount, reference_id, reference_type, invoice_reference_name FROM payments_non_core_customer_refund_details WHERE payments_non_core_id = ? and reference_type=?";

//	public static final String FETCH_CUSTOMER_REFUND_DETAILS_BY_PAYMENT_ID = "SELECT id, payments_non_core_id, status, invoice_id, invoice_amount, reference_id, reference_type, invoice_reference_name, due_amount FROM payments_non_core_customer_refund_details WHERE payments_non_core_id = ?";

	public static final String FETCH_EMPLOYEE_DETAILS_BY_PAYMENT_ID = "SELECT id, status, employee_id, net_pay, data, pay_run, due_amount FROM payments_non_core_employee_details WHERE payments_non_core_id = ?";

	public static final String INSERT_VENDOR_ADVANCE = "INSERT INTO payments_non_core_vendor_advance"
			+ "(payment_reference, original_amount, available_amount, vendor_id, organization_id, payment_non_core_id) VALUES (?,?,?,?,?,?)";

	public static final String INSERT_CREDIT_DETAILS = "INSERT INTO payments_non_core_credit_details "
			+ "(payments_non_core_id, status, vendor_advance_id, original_amount, available_amount, adjustment_amount, isDeleted, isCreatedNew, vendor_account_ledger_id)"
			+ "VALUES (?,?,?,?,?,?,?,?,?)";

	public static final String UPDATE_NON_CORE_PAYMENT = "UPDATE payments_non_core SET paid_via_id=?, payment_type_id=?, payment_ref_no=?, payment_date=?, vendor_id=?, vendor_account_ledger_id=?, vendor_account_ledger_name=?, "
			+ "currency_id=?, notes=?, adjusted_amount=?, total_amount=?, po_reference_no=?, contact_account_ledger_id=?, contact_account_ledger_name=?, contact_id=?, amount_paid=?, "
			+ "difference_amount=?, pay_type_id=?, pay_period=?, pay_run_reference=?, organization_id=?, isSuperAdmin=?, status=?, paid_to=?, custom_table_list=?, "
			+ "update_user_id=?, update_role_name=?, update_ts=?, payment_mode_id=?, contact_type=?, statutory_body=?, customer_name=?, employee_name=?, paid_for=?, "
			+ "currency_code=?, exchange_rate=?, bill_reference=?, reference_id=?, invoice_id=?, reference_type=?, is_bulk=?, paid_via_name=?, contact_name=?, invoice_reference_name=?, base_currency_code=?, general_ledger_data=?, due_amount=?, bank_reference_number=?  WHERE id = ?";

	public static final String FETCH_CREDIT_DETAILS_ON_PAYMENT_REF_ID = "SELECT id, status, vendor_advance_id, original_amount, available_amount, "
			+ "adjustment_amount, isDeleted, isCreatedNew, vendor_account_ledger_id FROM payments_non_core_credit_details WHERE payments_non_core_id = ?";

	public static final String PAY_RUN_PREFIX = "payrun";
	public static final String PAY_RUN_MIN_DIGITS = "1";
	public static final String PAY_RUN_SUFFIX = "";

	public static final String UPDATE_BILL_DETAILS = "UPDATE payments_non_core_bill_details SET payments_non_core_id=?, status=?, bill_reference=?, bill_amount=?, total_amount=?, data=?, type=? ,vendor_id=? WHERE id = ?";

	public static final String UPDATE_GST_DETAILS = "UPDATE payments_non_core_gst_details SET payments_non_core_id=?, status=?, gst_type=?, paid_amount=?, data=? WHERE id = ?";

	public static final String UPDATE_TDS_DETAILS = "UPDATE payments_non_core_tds_details SET payments_non_core_id=?, status=?, tds_section=?, type=?, paid_amount=?, data=? WHERE id = ?";

	public static final String UPDATE_CUSTOMER_REFUND = "UPDATE payments_non_core_customer_refund_details SET payments_non_core_id=?, status=?, invoice_id=?, invoice_amount=?, reference_id=?, reference_type=?, invoice_reference_name=? WHERE id = ?";

//	public static final String UPDATE_CUSTOMER_REFUND = "UPDATE payments_non_core_customer_refund_details SET payments_non_core_id=?, status=?, invoice_id=?, invoice_amount=?, reference_id=?, reference_type=?, invoice_reference_name=?, due_amount=? WHERE id = ?";

	public static final String UPDATE_CREDIT_DETAILS = "UPDATE payments_non_core_credit_details SET payments_non_core_id=?, status=?, vendor_advance_id=?, original_amount=?, available_amount=?, adjustment_amount=?, isDeleted=?, isCreatedNew=?, vendor_account_ledger_id=? WHERE id = ?";

	public static final String DEFAULT_DUE_AMOUNT_LEDGER = "Sundry Creditors - Local currency";

	public static final String DEFAULT_TDS_DEDUCTIONS_LEDGER = "TDS Payable";

	public static final String DEFAULT_TDS_TAX_LEDGER = "Other Tax Payable";

	public static final String DEFAULT_TDS_INTEREST_LEDGER = "Interest on delayed payment of TDS";

	public static final String DEFAULT_GST_TAX_LEDGER = "GST Payable";

	public static final String DEFAULT_GST_INTEREST_LEDGER = "Interest on delayed payment of GST";

	public static final String DEFAULT_PENALTY_LEDGER = "Fines and Penalties";

	public static final String PAYMENTS = "Payments";

	public static final String PURCHASE_ORDER = "Purchase Order";

	public static final String UPDATE_EMPLOYEE_DETAILS = "UPDATE payments_non_core_employee_details SET payments_non_core_id=?, status=?, employee_id=?, "
			+ "net_pay=?, data=?, pay_run=?, due_amount=? WHERE id = ?";

	public static final String DEFAULT_DUE_DATE_LEDGER = "Due Date";

	public static final String STATUTORY_BODY = "Statutory Body";

	public static final String CUSTOMER = "Customers";

	public static final String EMPLOYEE = "Employees";

	public static final String VENDOR = "Vendors";

	public static final String DUE_AMOUNT = "Due Amount";

	public static final String DUE_DATE = "Due Date";

	public static final String BANK_FEES = "Bank Fees";

	public static final String TDS_DEDUCTIONS = "TDS Deductions";

	public static final String TAX = "Tax";

	public static final String INTEREST = "Interest";

	public static final String PENALTY = "Penalty";

	public static final String PAYABLE = "Payable";

	public static final String ADVANCE = "Advance";

	public static final String DELETE_BILL_DETAIL_BY_ID = "DELETE from payments_non_core_bill_details where id=?";

	public static final String DELETE_GST_DETAIL_BY_ID = "DELETE from payments_non_core_gst_details where id=?";

	public static final String DELETE_TDS_DETAIL_BY_ID = "DELETE from payments_non_core_tds_details where id=?";

	public static final String DELETE_CR_DETAIL_BY_ID = "DELETE from payments_non_core_customer_refund_details where id=?";

	public static final String DELETE_EMPLOYEE_DETAIL_BY_ID = "DELETE from payments_non_core_employee_details where id=?";

	public static final String INR = "Indian Rupee";

	public static final String DEFAULT_FOREIGN_DUE_AMOUNT_LEDGER = "Sundry Creditors - Foreign currency";

	public static final String CHECK_PAYMENT_FOR_ORGANIZATION = "SELECT * FROM payments_non_core WHERE organization_id=? and payment_ref_no=?";

	public static final String PAYROLL_PAYRUN = "Payroll-PayRun";

	public static final String DELETE_CREDIT_DETAIL = "DELETE from payments_non_core_credit_details where id=?";

	public static final String UPDATE_VENDOR_ADVANCE = "UPDATE payments_non_core_vendor_advance SET payment_reference=?, vendor_id=? where payment_non_core_id=?";

	public static final String GET_PAYMENT_TYPE_ID_BY_STRING = "SELECT id FROM payment_type WHERE payment_type_name = ?";

	public static final String GET_PAYMENTS_OF_ORG_BY_TYPE = "SELECT p1.id, p1.payment_date, p1.payment_ref_no, p2.payment_type_name, p1.paid_via_id, p1.contact_id, \n"
			+ "  p1.total_amount, p1.status, p1.contact_type, p1.payment_mode_id, p1.amount_paid, p1.currency_id, p1.vendor_id, p1.statutory_body, p1.paid_to FROM payments_non_core p1, finance_common.payment_type p2 WHERE p1.payment_type_id = p2.id AND p1.organization_id = ? AND p1.payment_type_id=? ORDER BY p1.id DESC";

	public static final String GET_PAYMENTS_OF_ORG_USER_ROLE_BY_TYPE = "SELECT p1.id, p1.payment_date, p1.payment_ref_no, p2.payment_type_name, p1.paid_via_id, p1.contact_id, \n"
			+ "  p1.total_amount, p1.status, p1.contact_type, p1.payment_mode_id, p1.amount_paid, p1.currency_id, p1.vendor_id, p1.statutory_body, p1.paid_to FROM payments_non_core p1, finance_common.payment_type p2 WHERE p1.payment_type_id = p2.id AND p1.organization_id = ? AND p1.payment_type_id=? AND p1.user_id=? AND p1.role_name=? ORDER BY p1.id DESC";;;

	// Balance Update
	public static final String GET_TOTAL_INVOICE_AMOUNT_FROM_PAYMENTS = "SELECT sum(total_amount) FROM payments_non_core_bill_details pncbd WHERE bill_reference=? and payments_non_core_id IN (SELECT id FROM payments_non_core pnc WHERE pnc.id=pncbd.payments_non_core_id and STATUS IN (?) ) and type=? and status not IN('DEL')";

	public static final String GET_PAYMENTS_OF_INVOICE = "SELECT  id,"
			+ "(SELECT payment_ref_no FROM payments_non_core pnc WHERE  pnc.id=pncbd.payments_non_core_id) AS reference,"
			+ "(SELECT symbol from usermgmt.currency_organization WHERE id=(SELECT currency_id FROM payments_non_core pnc WHERE  pnc.id=pncbd.payments_non_core_id)) AS currency_symbol,total_amount"
			+ " FROM payments_non_core_bill_details pncbd WHERE bill_reference=? and payments_non_core_id IN (SELECT id FROM payments_non_core pnc WHERE pnc.id=pncbd.payments_non_core_id and STATUS IN (?) ) and type=? and status not IN('DEL') AND  total_amount>0";

	public static final String GET_CREDIT_DETAILS = "SELECT id, payment_reference, original_amount, available_amount, vendor_id FROM payments_non_core_vendor_advance WHERE id = ?";

	public static final String GET_DASHBOARD_PAYMENTS_OF_ORG = "SELECT p1.id, p1.payment_date, p1.payment_ref_no, p2.payment_type_name, \n"
			+ "  p1.amount_paid, p1.currency_id FROM payments_non_core p1, finance_common.payment_type p2 WHERE p1.payment_type_id = p2.id AND p1.organization_id = ? ORDER BY p1.payment_date DESC LIMIT 7";

	public static final String GET_DASHBOARD_PAYMENTS_OF_ORG_USER_ROLE = "SELECT p1.id, p1.payment_date, p1.payment_ref_no, p2.payment_type_name, \n"
			+ "  p1.amount_paid, p1.currency_id FROM payments_non_core p1, finance_common.payment_type p2 WHERE p1.payment_type_id = p2.id AND p1.organization_id = ? AND p1.user_id = ? AND p1.role_name = ? ORDER BY p1.payment_date DESC LIMIT 7";;

	public static final String GET_BASIC_VENDOR_ADVANCE_BY_ID = "SELECT pnc.id,pnc.status,pncva.original_amount FROM payments_non_core_vendor_advance pncva JOIN payments_non_core pnc ON pncva.payment_non_core_id=pnc.id WHERE pncva.id=?";

	public static final String GET_ADJUSTMENTS_FOR_VENDOR_ADVANCE_BY_ID = "SELECT sum(pncbd.total_amount) FROM payments_non_core_bill_details pncbd  JOIN payments_non_core pnc ON pncbd.payments_non_core_id=pnc.id WHERE pncbd.bill_reference=? AND pncbd.type=? AND pnc.status=? AND pncbd.status=?";

	public static final String GET_VENDOR_ADVANCES_FOR_VENDOR_CURRENCY = "SELECT pncva.id FROM payments_non_core_vendor_advance pncva JOIN payments_non_core pnc ON pncva.payment_non_core_id=pnc.id WHERE pnc.vendor_id=? AND pnc.currency_id=? AND pnc.status IN (?,?,?)";

	public static final String GET_PAYMENT_MODES_QUERY = "SELECT id, name FROM finance_common.payment_mode";

	public static final String GET_STATUTORY_MAP_ORG_USER_ROLE_QUERY = "SELECT id, name FROM usermgmt.statutory_body WHERE organization_id = ? AND user_id = ? AND role_name = ?";

	public static final String GET_STATUTORY_MAP_QUERY = "SELECT id, name FROM usermgmt.statutory_body WHERE organization_id = ? AND user_id = ?";

	public static final String GET_CUSTOMER_MAP_ORG_USER_ROLE_QUERY = "SELECT id, customer_display_name FROM accounts_receivable.customer_general_information WHERE organization_id = ? AND user_id = ? AND role_name = ?";

	public static final String GET_CUSTOMER_MAP_QUERY = "SELECT id, customer_display_name FROM accounts_receivable.customer_general_information WHERE organization_id = ? AND user_id = ?";

	public static final String GET_VENDOR_MAP_ORG_USER_ROLE_QUERY = "SELECT id, vendor_display_name FROM accounts_payable.vendor_general_information WHERE organization_id = ? AND user_id = ? AND role_name = ?";

	public static final String GET_VENDOR_MAP_QUERY = "SELECT id, vendor_display_name FROM accounts_payable.vendor_general_information WHERE organization_id = ? AND user_id = ?";

	public static final String GET_EMPLOYEE_MAP_ORG_USER_ROLE_QUERY = "SELECT id, first_name, last_name FROM accounts_payable.employee WHERE organization_id = ? AND user_id = ? AND role_name = ?";

	public static final String GET_EMPLOYEE_MAP_QUERY = "SELECT id, first_name, last_name FROM accounts_payable.employee WHERE organization_id = ? AND user_id = ?";

	public static final String GET_CURRENCY_MAP_ORG_USER_ROLE_QUERY = "SELECT id, symbol FROM usermgmt.currency_organization WHERE organization_id = ? AND user_id = ? AND role_name = ?";

	public static final String GET_CURRENCY_MAP_QUERY = "SELECT id, symbol FROM usermgmt.currency_organization WHERE organization_id = ? AND user_id = ?";

	public static final String LIST_PAYMENTS = "SELECT pnc.id AS id, pnc.payment_date AS date, pnc.payment_ref_no AS pay_ref, pt.payment_type_name AS type, \n"
			+ "pm.name AS mode, bmca.cash_account_name AS cash_name, bmba.bank_name AS bank_name, \n"
			+ "bmw.wallet_account_name AS wallet_name, bmc.account_name AS account_name, co.symbol AS symbol, \n"
			+ "sb.name AS statutory, vgi.vendor_display_name AS vendor, cgi.customer_display_name AS customer,\n"
			+ "org.date_format AS date_format, pnc.payment_mode_id AS mode_id,\n"
			+ "pnc.paid_via_id AS paid_via_id, pnc.contact_type AS contact_type, pnc.amount_paid AS amount_paid, \n"
			+ "pnc.currency_id AS currency_id, pnc.status AS status, pnc.statutory_body AS statutory_id, pnc.vendor_id AS vendor_id,\n"
			+ "pnc.paid_to AS customer_id, pnc.contact_id AS contact_id, payroll_type.name AS paid_for,\n" + "CASE\n"
			+ "	WHEN pt.payment_type_name = 'Bill Payments' THEN GROUP_CONCAT(DISTINCT apigi.invoice_no)\n"
			+ "	WHEN pt.payment_type_name = 'Vendor Advance' THEN GROUP_CONCAT(DISTINCT pogi.purchase_order_no)\n"
			+ "    WHEN pt.payment_type_name = 'GST Payments' THEN GROUP_CONCAT(DISTINCT trto.type)\n"
			+ "    WHEN pt.payment_type_name = 'TDS Payments' THEN GROUP_CONCAT(DISTINCT tds.tds_rate_identifier)\n"
			+ "    WHEN pt.payment_type_name = 'Customer Refunds' THEN GROUP_CONCAT(DISTINCT rec.receipt_no)\n"
			+ "    WHEN pt.payment_type_name = 'Employee Payments' THEN GROUP_CONCAT(DISTINCT payrun.payrun_reference)\n"
			+ "END AS invoices, GROUP_CONCAT(DISTINCT CONCAT(emp.first_name, \" \", emp.last_name)) AS emp_name, GROUP_CONCAT(DISTINCT pncva.payment_reference) AS advance,\n"
			+ "GROUP_CONCAT(DISTINCT credit.credit_note_number) AS credit_note\n"
			+ "FROM accounts_payable.payments_non_core pnc LEFT JOIN finance_common.payment_type pt ON pnc.payment_type_id = pt.id\n"
			+ "LEFT JOIN finance_common.payment_mode pm ON pm.id = pnc.payment_mode_id\n"
			+ "LEFT JOIN banking.bank_master_cash_account bmca ON pnc.paid_via_id = bmca.id\n"
			+ "LEFT JOIN banking.bank_master_bank_account bmba ON pnc.paid_via_id = bmba.id\n"
			+ "LEFT JOIN banking.bank_master_wallet bmw ON pnc.paid_via_id = bmw.id\n"
			+ "LEFT JOIN banking.bank_master_card bmc ON pnc.paid_via_id = bmc.id\n"
			+ "LEFT JOIN usermgmt.currency_organization co ON pnc.currency_id = co.id\n"
			+ "LEFT JOIN usermgmt.statutory_body sb ON pnc.statutory_body = sb.id\n"
			+ "LEFT JOIN accounts_payable.vendor_general_information vgi ON pnc.vendor_id = vgi.id\n"
			+ "LEFT JOIN accounts_receivable.customer_general_information cgi ON pnc.paid_to = cgi.id\n"
			+ "LEFT JOIN usermgmt.organization org ON pnc.organization_id = org.id\n"
			+ "LEFT JOIN finance_common.payroll_type payroll_type ON pnc.paid_for = payroll_type.id\n"
			+ "LEFT JOIN accounts_payable.payments_non_core_bill_details pncbd ON pnc.id = pncbd.payments_non_core_id\n"
			+ "LEFT JOIN accounts_payable.invoice_general_information apigi ON pncbd.bill_reference = apigi.id  AND pncbd.type = \"Bill\"\n"
			+ "LEFT JOIN accounts_payable.payments_non_core_vendor_advance pncva ON pncbd.bill_reference = pncva.id  AND pncbd.type = \"Advance\"\n"
			+ "LEFT JOIN accounts_payable.purchase_order_general_information pogi ON pnc.po_reference_no = pogi.id\n"
			+ "LEFT JOIN accounts_payable.payments_non_core_gst_details pncgst ON pnc.id = pncgst.payments_non_core_id\n"
			+ "LEFT JOIN usermgmt.tax_rate_type_organization trto ON pncgst.gst_type = trto.id\n"
			+ "LEFT JOIN accounts_payable.payments_non_core_tds_details pnctds ON pnc.id = pnctds.payments_non_core_id\n"
			+ "LEFT JOIN finance_common.tds tds ON pnctds.tds_section = tds.id\n"
			+ "LEFT JOIN accounts_payable.payments_non_core_employee_details pnced ON pnc.id = pnced.payments_non_core_id\n"
			+ "LEFT JOIN accounts_payable.employee emp ON pnced.employee_id = emp.id OR pnc.contact_id = emp.id\n"
			+ "LEFT JOIN payroll.payrun payrun ON pnced.pay_run = payrun.id\n"
			+ "LEFT JOIN accounts_payable.payments_non_core_customer_refund_details pnccrd ON pnc.id = pnccrd.payments_non_core_id\n"
			+ "LEFT JOIN accounts_receivable.invoice_general_information arigi ON pnccrd.invoice_id = arigi.id\n"
			+ "LEFT JOIN accounts_receivable.receipt_bulk_details rbd ON arigi.id = rbd.reference_id AND rbd.reference_type = \"Invoice\"\n"
			+ "LEFT JOIN accounts_receivable.receipt rec ON rbd.receipt_id = rec.id AND pnccrd.reference_id = rec.id\n"
			+ "LEFT JOIN accounts_receivable.credit_note credit ON arigi.id = credit.original_invoice_id AND pnccrd.reference_id = credit.id\n";

	public static final String GET_PAYMENTS_OF_ORG = LIST_PAYMENTS
			+ "WHERE pnc.organization_id = ? AND pnc.status NOT IN (?,?,?) GROUP BY pnc.id ORDER BY pnc.id DESC";

	public static final String GET_PAYMENTS_OF_ORG_USER_ROLE = LIST_PAYMENTS
			+ "WHERE pnc.organization_id = ? AND pnc.status NOT IN (?,?,?) AND pnc.user_id = ? AND pnc.role_name = ? GROUP BY pnc.id ORDER BY pnc.id DESC";

	public static final String GET_TOP5_PAYMENTS_OF_ORG = LIST_PAYMENTS
			+ "WHERE pnc.organization_id = ? AND pnc.payment_type_id = ? GROUP BY pnc.id ORDER BY pnc.id DESC";

	public static final String GET_TOP5_PAYMENTS_OF_ORG_USER_ROLE = LIST_PAYMENTS
			+ "WHERE pnc.organization_id = ? AND pnc.payment_type_id = ? AND pnc.user_id = ? AND pnc.role_name = ? GROUP BY pnc.id ORDER BY pnc.id DESC";

	public static final String GET_TOP5_PAYMENTS_OF_ORG_SUMMARY = LIST_PAYMENTS
			+ "WHERE pnc.organization_id = ? GROUP BY pnc.id ORDER BY pnc.id DESC";

	public static final String GET_TOP5_PAYMENTS_OF_ORG__SUMMARY_USER_ROLE = LIST_PAYMENTS
			+ "WHERE pnc.organization_id = ? AND pnc.user_id = ? AND pnc.role_name = ? GROUP BY pnc.id ORDER BY pnc.id DESC";

	public static final String PAYMENT_ADVICE = "SELECT org.display_name AS org_name, org_address.org_gst AS org_gst, org_address.org_state AS org_state, org_address.org_country AS org_country,\n"
			+ "org_address.org_address_1 AS org_address_1, org_address.org_address_2 AS org_address_2, org_address.org_pincode AS org_pincode, org_address.org_city,\n"
			+ "pnc.payment_ref_no AS payment_number, org.date_format AS date_format, pnc.payment_date AS payment_date,\n"
			+ "pnc.bank_reference_number AS bank_reference_number, pt.payment_type_name AS payment_type, \n"
			+ "pnc.contact_type AS contact_type, pm.name AS mode, bmca.cash_account_name AS cash_name, bmba.bank_name AS bank_name, \n"
			+ "bmw.wallet_account_name AS wallet_name, bmc.account_name AS account_name, co.symbol AS symbol, \n"
			+ "sb.name AS statutory, vgi.vendor_display_name AS vendor, cgi.customer_display_name AS customer,\n"
			+ "pnc.amount_paid AS amount_paid, sb.addr_1 AS statutory_address_1, sb.addr_2 AS statutory_address_2, \n"
			+ "sb.city AS statutory_city, state.name AS state, sb.pincode AS statutory_pincode,\n"
			+ "voa.address_1 AS vendor_address_1, voa.address_2 AS vendor_address_2, voa.city AS vendor_city,\n"
			+ "country.name AS country, vgi.gst_no AS vendor_gst, voa.zip_code AS vendor_pincode, cba.address_1 AS customer_address_1, \n"
			+ "cba.address_2 as customer_address_2, cba.city AS customer_city, cba.zip_code as customer_pincode,\n"
			+ "cgi.gst_number AS customer_gst, sos.state_name AS state_name, sos.state_code AS state_code,\n"
			+ "emp_custom.emp_name AS emp_name, emp_custom2.emp_name AS emp_name_other, apsos.ap_state_name AS ap_state_name, apsos.ap_state_code AS ap_state_code\n"
			+ "FROM accounts_payable.payments_non_core pnc \n"
			+ "LEFT JOIN usermgmt.organization org ON pnc.organization_id = org.id\n"
			+ "LEFT JOIN finance_common.payment_type pt ON pnc.payment_type_id = pt.id\n"
			+ "LEFT JOIN finance_common.payment_mode pm ON pm.id = pnc.payment_mode_id\n"
			+ "LEFT JOIN banking.bank_master_cash_account bmca ON pnc.paid_via_id = bmca.id\n"
			+ "LEFT JOIN banking.bank_master_bank_account bmba ON pnc.paid_via_id = bmba.id\n"
			+ "LEFT JOIN banking.bank_master_wallet bmw ON pnc.paid_via_id = bmw.id\n"
			+ "LEFT JOIN banking.bank_master_card bmc ON pnc.paid_via_id = bmc.id\n"
			+ "LEFT JOIN usermgmt.currency_organization co ON pnc.currency_id = co.id\n"
			+ "LEFT JOIN usermgmt.statutory_body sb ON pnc.statutory_body = sb.id\n"
			+ "LEFT JOIN accounts_payable.vendor_general_information vgi ON pnc.vendor_id = vgi.id\n"
			+ "LEFT JOIN accounts_receivable.customer_general_information cgi ON pnc.paid_to = cgi.id\n"
			+ "LEFT JOIN accounts_payable.vendor_origin_address voa ON vgi.id = voa.vendor_general_information_id\n"
			+ "LEFT JOIN accounts_receivable.customer_billing_address AS cba ON cgi.id = cba.customer_general_information_id\n"
			+ "LEFT JOIN finance_common.state_list state ON state.id = sb.state OR state.id = voa.state OR state.id = cba.state\n"
			+ "LEFT JOIN finance_common.country_list AS country ON country.id = voa.country OR country.id = cba.country\n"
			+ "LEFT JOIN (SELECT pnc2.id AS pncId, GROUP_CONCAT(CONCAT(emp.first_name, \" \", emp.last_name)) AS emp_name FROM accounts_payable.employee emp, accounts_payable.payments_non_core_employee_details pnced, \n"
			+ "accounts_payable.payments_non_core pnc2 WHERE pnc2.id = pnced.payments_non_core_id\n"
			+ "AND pnced.employee_id = emp.id GROUP BY pnc2.id) emp_custom ON emp_custom.pncId = pnc.id\n"
			+ "LEFT JOIN accounts_payable.payments_non_core_customer_refund_details pnccrd ON pnc.id = pnccrd.payments_non_core_id\n"
			+ "LEFT JOIN accounts_receivable.invoice_general_information arigi ON pnccrd.invoice_id = arigi.id\n"
			+ "LEFT JOIN finance_common.source_of_supply sos ON sos.id = arigi.place_of_supply_id\n"
			+ "LEFT JOIN (SELECT org2.organization_id AS id, state2.name AS org_state, country2.name AS org_country, org2.gst_no AS org_gst,\n"
			+ "org2.address_1 AS org_address_1, org2.address_2 AS org_address_2, org2.city AS org_city, org2.pin_code AS org_pincode \n"
			+ "FROM usermgmt.gst_location org2, finance_common.state_list state2, finance_common.country_list country2 \n"
			+ "WHERE state2.id = org2.state AND country2.id = org2.country) org_address ON pnc.organization_id = org_address.id\n"
			+ "LEFT JOIN (SELECT pnc2.id AS pncId, CONCAT(emp.first_name, \" \", emp.last_name) AS emp_name FROM accounts_payable.employee emp, \n"
			+ "accounts_payable.payments_non_core pnc2 WHERE pnc2.contact_id = emp.id) emp_custom2 ON emp_custom2.pncId = pnc.id\n"
			+ "LEFT JOIN (SELECT pnc3.id AS appncid, sos2.state_name AS ap_state_name, sos2.state_code AS ap_state_code \n"
			+ "FROM accounts_payable.payments_non_core pnc3, accounts_payable.payments_non_core_bill_details pncbd2, accounts_payable.invoice_general_information apigi, \n"
			+ "finance_common.source_of_supply sos2, accounts_payable.invoice_transaction_details itx\n"
			+ "WHERE pnc3.id = pncbd2.payments_non_core_id AND pncbd2.bill_reference = apigi.id AND apigi.id = itx.invoice_general_information_id AND itx.source_of_supply_id = sos2.id) apsos\n"
			+ "ON apsos.appncid = pnc.id\n";

	public static final String GET_PAYMENT_ADVICE = PAYMENT_ADVICE + "WHERE pnc.organization_id = ? AND pnc.id = ?";

	public static final String GET_PAYMENT_ADVICE_USER_ROLE = PAYMENT_ADVICE
			+ "WHERE pnc.organization_id = ? AND pnc.id = ? AND pnc.user_id = ? AND pnc.role_name = ?";
}

package com.blackstrawai.common;

public class Constants {

	public static final String SUCCESS = "Success";
	public static final String FAILURE = "Failure";

	/*
	 * ****************************************************************************
	 * codes
	 */

	public static final String USER_LOGIN_SUCCESS = "S-10000";
	public static final String USER_LOGIN_FAILURE = "E-10000";
	public static final String USER_LOGOUT_SUCCESS = "S-10001";
	public static final String TOKEN_EXPIRATION_SUCCESS = "S-10002";
	public static final String USER_LOGOUT_FAILURE = "E-10001";
	public static final String TOKEN_EXPIRATION_FAILURE = "E-10002";
	public static final String REGISTRATION_SUCCESS = "S-100";
	public static final String REGISTRATION_FAILURE = "E-100";
	public static final String RECOVERY_PASSWORD_SUCCESS = "S-200";
	public static final String RECOVERY_PASSWORD_FAILURE = "E-200";
	public static final String RESET_PASSWORD_SUCCESS = "S-201";
	public static final String RESET_PASSWORD_FAILURE = "E-201";

	public static final String CREATE_PASSWORD_SUCCESS = "S-202";
	public static final String CREATE_PASSWORD_FAILURE = "E-202";

	public static final String SUCCESS_DURING_GET = "S-99999";
	public static final String FAILURE_DURING_GET = "E-99999";

	public static final String ORGANIZATION_CREATED_SUCCESSFULLY = "S-001";
	public static final String ORGANIZATION_UPDATED_SUCCESSFULLY = "S-002";
	public static final String ORGANIZATION_DELETED_SUCCESSFULLY = "S-003";
	public static final String ORGANIZATION_CREATED_UNSUCCESSFULLY = "E-001";
	public static final String ORGANIZATION_UPDATED_UNSUCCESSFULLY = "E-002";
	public static final String ORGANIZATION_DELETED_UNSUCCESSFULLY = "E-003";

	public static final String USER_CREATED_SUCCESSFULLY = "S-004";
	public static final String USER_UPDATED_SUCCESSFULLY = "S-005";
	public static final String USER_DELETED_SUCCESSFULLY = "S-006";
	public static final String USER_CREATED_UNSUCCESSFULLY = "E-004";
	public static final String USER_UPDATED_UNSUCCESSFULLY = "E-005";
	public static final String USER_DELETED_UNSUCCESSFULLY = "E-006";

	public static final String ROLE_CREATED_SUCCESSFULLY = "S-007";
	public static final String ROLE_UPDATED_SUCCESSFULLY = "S-008";
	public static final String ROLE_DELETED_SUCCESSFULLY = "S-009";
	public static final String ROLE_CREATED_UNSUCCESSFULLY = "E-007";
	public static final String ROLE_UPDATED_UNSUCCESSFULLY = "E-008";
	public static final String ROLE_DELETED_UNSUCCESSFULLY = "E-009";

	public static final String VENDOR_CREATED_SUCCESSFULLY = "S-010";
	public static final String VENDOR_UPDATED_SUCCESSFULLY = "S-011";
	public static final String VENDOR_DELETED_SUCCESSFULLY = "S-012";
	public static final String VENDOR_NAMES_FETCHED_SUCCESSFULLY = "S-013";
	public static final String VENDOR_CREATED_UNSUCCESSFULLY = "E-010";
	public static final String VENDOR_UPDATED_UNSUCCESSFULLY = "E-011";
	public static final String VENDOR_DELETED_UNSUCCESSFULLY = "E-012";
	public static final String VENDOR_NAMES_FETCHED_UNSUCCESSFULLY = "E-013";

	public static final String EMPLOYEE_CREATED_SUCCESSFULLY = "S-014";
	public static final String EMPLOYEE_UPDATED_SUCCESSFULLY = "S-015";
	public static final String EMPLOYEE_DELETED_SUCCESSFULLY = "S-016";
	public static final String EMPLOYEE_CREATED_UNSUCCESSFULLY = "E-014";
	public static final String EMPLOYEE_UPDATED_UNSUCCESSFULLY = "E-015";
	public static final String EMPLOYEE_DELETED_UNSUCCESSFULLY = "E-016";

	public static final String CUSTOMER_CREATED_SUCCESSFULLY = "S-017";
	public static final String CUSTOMER_UPDATED_SUCCESSFULLY = "S-018";
	public static final String CUSTOMER_DELETED_SUCCESSFULLY = "S-019";
	public static final String CUSTOMER_CREATED_UNSUCCESSFULLY = "E-017";
	public static final String CUSTOMER_UPDATED_UNSUCCESSFULLY = "E-018";
	public static final String CUSTOMER_DELETED_UNSUCCESSFULLY = "E-019";

	public static final String CALCULATION_DONE_SUCCESSFULLY = "S-020";
	public static final String CALCULATION_DONE_UNSUCCESSFULLY = "E-020";

	public static final String PO_CREATED_SUCCESSFULLY = "S-021";
	public static final String PO_UPDATED_SUCCESSFULLY = "S-022";
	public static final String PO_DELETED_SUCCESSFULLY = "S-023";
	public static final String PO_CREATED_UNSUCCESSFULLY = "E-021";
	public static final String PO_UPDATED_UNSUCCESSFULLY = "E-022";
	public static final String PO_DELETED_UNSUCCESSFULLY = "E-023";

	public static final String BILLS_INVOICE_CREATED_SUCCESSFULLY = "S-024";
	public static final String BILLS_INVOICE_UPDATED_SUCCESSFULLY = "S-025";
	public static final String BILLS_INVOICE_DELETED_SUCCESSFULLY = "S-026";
	public static final String BILLS_INVOICE_VOIDED_SUCCESSFULLY = "S-142";
	public static final String BILLS_INVOICE_CREATED_UNSUCCESSFULLY = "E-024";
	public static final String BILLS_INVOICE_UPDATED_UNSUCCESSFULLY = "E-025";
	public static final String BILLS_INVOICE_DELETED_UNSUCCESSFULLY = "E-026";


	public static final String EXPENSE_CREATED_SUCCESSFULLY = "S-027";
	public static final String EXPENSE_UPDATED_SUCCESSFULLY = "S-028";
	public static final String EXPENSE_DELETED_SUCCESSFULLY = "S-029";
	public static final String EXPENSE_CREATED_UNSUCCESSFULLY = "E-027";
	public static final String EXPENSE_UPDATED_UNSUCCESSFULLY = "E-028";
	public static final String EXPENSE_DELETED_UNSUCCESSFULLY = "E-029";

	public static final String BANK_MASTER_ACCOUNTS_CREATED_SUCCESSFULLY = "S-030";
	public static final String BANK_MASTER_CARD_CREATED_SUCCESSFULLY = "S-031";
	public static final String BANK_MASTER_WALLET_CREATED_SUCCESSFULLY = "S-032";
	public static final String BANK_MASTER_CASH_ACCOUNT_CREATED_SUCCESSFULLY = "S-033";
	public static final String BANK_MASTER_ACCOUNTS_CREATED_UNSUCCESSFULLY = "E-030";
	public static final String BANK_MASTER_CARD_CREATED_UNSUCCESSFULLY = "E-031";
	public static final String BANK_MASTER_WALLET_CREATED_UNSUCCESSFULLY = "E-032";
	public static final String BANK_MASTER_CASH_ACCOUNT_CREATED_UNSUCCESSFULLY = "E-033";

	public static final String BANK_MASTER_ACCOUNTS_UPDATED_SUCCESSFULLY = "S-034";
	public static final String BANK_MASTER_CARD_UPDATED_SUCCESSFULLY = "S-035";
	public static final String BANK_MASTER_WALLET_UPDATED_SUCCESSFULLY = "S-036";
	public static final String BANK_MASTER_CASH_ACCOUNT_UPDATED_SUCCESSFULLY = "S-037";
	public static final String BANK_MASTER_ACCOUNTS_UPDATED_UNSUCCESSFULLY = "E-034";
	public static final String BANK_MASTER_CARD_UPDATED_UNSUCCESSFULLY = "E-035";
	public static final String BANK_MASTER_WALLET_UPDATED_UNSUCCESSFULLY = "E-036";
	public static final String BANK_MASTER_CASH_ACCOUNT_UPDATED_UNSUCCESSFULLY = "E-037";

	public static final String BANK_MASTER_ACCOUNTS_DELETED_SUCCESSFULLY = "S-038";
	public static final String BANK_MASTER_CARD_DELETED_SUCCESSFULLY = "S-039";
	public static final String BANK_MASTER_WALLET_DELETED_SUCCESSFULLY = "S-040";
	public static final String BANK_MASTER_CASH_ACCOUNT_DELETED_SUCCESSFULLY = "S-041";
	public static final String BANK_MASTER_ACCOUNTS_DELETED_UNSUCCESSFULLY = "E-038";
	public static final String BANK_MASTER_CARD_DELETED_UNSUCCESSFULLY = "E-039";
	public static final String BANK_MASTER_WALLET_DELETED_UNSUCCESSFULLY = "E-040";
	public static final String BANK_MASTER_CASH_ACCOUNT_DELETED_UNSUCCESSFULLY = "E-041";

	public static final String ACCOUNTING_ASPECTS_CREATED_SUCCESSFULLY = "S-042";
	public static final String ACCOUNTING_ASPECTS_UPDATED_SUCCESSFULLY = "S-043";
	public static final String ACCOUNTING_ASPECTS_DELETED_SUCCESSFULLY = "S-044";
	public static final String ACCOUNTING_ASPECTS_CREATED_UNSUCCESSFULLY = "E-042";
	public static final String ACCOUNTING_ASPECTS_UPDATED_UNSUCCESSFULLY = "E-043";
	public static final String ACCOUNTING_ASPECTS_DELETED_UNSUCCESSFULLY = "E-044";

	public static final String LEDGER_CREATED_SUCCESSFULLY = "S-045";
	public static final String LEDGER_UPDATED_SUCCESSFULLY = "S-046";
	public static final String LEDGER_DELETED_SUCCESSFULLY = "S-047";
	public static final String LEDGER_CREATED_UNSUCCESSFULLY = "E-045";
	public static final String LEDGER_UPDATED_UNSUCCESSFULLY = "E-046";
	public static final String LEDGER_DELETED_UNSUCCESSFULLY = "E-047";

	public static final String VENDOR_GENERAL_SETTINGS_UPDATED_SUCCESSFULLY = "S-048";
	public static final String VENDOR_GENERAL_SETTINGS_UPDATED_UNSUCCESSFULLY = "E-048";
	public static final String VENDOR_PREDEFINED_SETTINGS_CREATED_SUCCESSFULLY = "S-049";
	public static final String VENDOR_PREDEFINED_SETTINGS_UPDATED_SUCCESSFULLY = "S-050";
	public static final String VENDOR_PREDEFINED_SETTINGS_DELETED_SUCCESSFULLY = "S-051";
	public static final String VENDOR_PREDEFINED_SETTINGS_CREATED_UNSUCCESSFULLY = "E-049";
	public static final String VENDOR_PREDEFINED_SETTINGS_UPDATED_UNSUCCESSFULLY = "E-050";
	public static final String VENDOR_PREDEFINED_SETTINGS_DELETED_UNSUCCESSFULLY = "E-051";

	public static final String PAY_ITEM_CREATED_SUCCESSFULLY = "S-052";
	public static final String PAY_ITEM_UPDATED_SUCCESSFULLY = "S-053";
	public static final String PAY_ITEM_DELETED_SUCCESSFULLY = "S-054";
	public static final String PAY_ITEM_CREATED_UNSUCCESSFULLY = "E-052";
	public static final String PAY_ITEM_UPDATED_UNSUCCESSFULLY = "E-053";
	public static final String PAY_ITEM_DELETED_UNSUCCESSFULLY = "E-054";

	public static final String PAY_TYPE_CREATED_SUCCESSFULLY = "S-055";
	public static final String PAY_TYPE_UPDATED_SUCCESSFULLY = "S-056";
	public static final String PAY_TYPE_DELETED_SUCCESSFULLY = "S-057";
	public static final String PAY_TYPE_CREATED_UNSUCCESSFULLY = "E-055";
	public static final String PAY_TYPE_UPDATED_UNSUCCESSFULLY = "E-056";
	public static final String PAY_TYPE_DELETED_UNSUCCESSFULLY = "E-057";

	public static final String STATUTORY_BODY_CREATED_SUCCESSFULLY = "S-058";
	public static final String STATUTORY_BODY_CREATED_UNSUCCESSFULLY = "E-058";
	public static final String STATUTORY_BODY_UPDATED_SUCCESSFULLY = "S-059";
	public static final String STATUTORY_BODY_UPDATED_UNSUCCESSFULLY = "E-059";
	public static final String STATUTORY_BODY_DELETED_SUCCESSFULLY = "S-060";
	public static final String STATUTORY_BODY_DELETED_UNSUCCESSFULLY = "E-060";

	public static final String CONTRA_ACCOUNTS_CREATED_SUCCESSFULLY = "S-061";
	public static final String CONTRA_ACCOUNTS_UPDATED_SUCCESSFULLY = "S-062";
	public static final String CONTRA_ACCOUNTS_CREATED_UNSUCCESSFULLY = "E-061";
	public static final String CONTRA_ACCOUNTS_UPDATED_UNSUCCESSFULLY = "E-062";

	public static final String WORKFLOW_SETTINGS_CREATED_SUCCESSFULLY="S-063";
	public static final String WORKFLOW_SETTINGS_UPDATED_SUCCESSFULLY="S-064";
	public static final String WORKFLOW_SETTINGS_DELETED_SUCCESSFULLY="S-065";
	public static final String WORKFLOW_SETTINGS_CREATED_UNSUCCESSFULLY="E-063";
	public static final String WORKFLOW_SETTINGS_UPDATED_UNSUCCESSFULLY="E-064";
	public static final String WORKFLOW_SETTINGS_DELETED_UNSUCCESSFULLY="E-065";

	public static final String PAYMENT_TERMS_CREATED_SUCCESSFULLY = "S-066";
	public static final String PAYMENT_TERMS_UPDATED_SUCCESSFULLY = "S-067";
	public static final String PAYMENT_TERMS_DELETED_SUCCESSFULLY = "S-068";
	public static final String PEYMENT_TERMS_CREATED_UNSUCCESSFULLY = "E-066";
	public static final String PAYMENT_TERMS_UPDATED_UNSUCCESSFULLY = "E-067";
	public static final String PAYMENT_TERMS_DELETED_UNSUCCESSFULLY = "E-068";

	public static final String CURRENCY_CREATED_SUCCESSFULLY = "S-069";
	public static final String CURRENCY_UPDATED_SUCCESSFULLY = "S-070";
	public static final String CURRENCY_DELETED_SUCCESSFULLY = "S-071";
	public static final String CURRENCY_CREATED_UNSUCCESSFULLY = "E-069";
	public static final String CURRENCY_UPDATED_UNSUCCESSFULLY = "E-070";
	public static final String CURRENCY_DELETED_UNSUCCESSFULLY = "E-071";

	public static final String SHIPPING_PREFERENCE_CREATED_SUCCESSFULLY = "S-072";
	public static final String SHIPPING_PREFERENCES_UPDATED_SUCCESSFULLY = "S-073";
	public static final String SHIPPING_PREFERENCES_DELETED_SUCCESSFULLY = "S-074";
	public static final String SHIPPING_PREFERENCE_CREATED_UNSUCCESSFULLY = "E-072";
	public static final String SHIPPING_PREFERENCE_UPDATED_UNSUCCESSFULLY = "E-073";
	public static final String SHIPPING_PREFERENCE_DELETED_UNSUCCESSFULLY = "E-074";

	public static final String VOUCHER_CREATED_SUCCESSFULLY = "S-075";
	public static final String VOUCHER_UPDATED_SUCCESSFULLY = "S-076";
	public static final String VOUCHER_DELETED_SUCCESSFULLY = "S-077";
	public static final String VOUCHER_CREATED_UNSUCCESSFULLY = "E-075";
	public static final String VOUCHER_UPDATED_UNSUCCESSFULLY = "E-076";
	public static final String VOUCHER_DELETED_UNSUCCESSFULLY = "E-077";

	public static final String PRODUCT_CREATED_SUCCESSFULLY = "S-078";
	public static final String PRODUCT_UPDATED_SUCCESSFULLY = "S-079";
	public static final String PRODUCT_DELETED_SUCCESSFULLY = "S-080";
	public static final String PRODUCT_CREATED_UNSUCCESSFULLY = "E-078";
	public static final String PRODUCT_UPDATED_UNSUCCESSFULLY = "E-079";
	public static final String PRODUCT_DELETED_UNSUCCESSFULLY = "E-080";

	public static final String CUSTOMER_GROUP_CREATED_SUCCESSFULLY = "S-081";
	public static final String CUSTOMER_GROUP_UPDATED_SUCCESSFULLY = "S-082";
	public static final String CUSTOMER_GROUP_DELETED_SUCCESSFULLY = "S-083";
	public static final String CUSTOMER_GROUP_CREATED_UNSUCCESSFULLY = "E-081";
	public static final String CUSTOMER_GROUP_UPDATED_UNSUCCESSFULLY = "E-082";
	public static final String CUSTOMER_GROUP_DELETED_UNSUCCESSFULLY = "E-083";

	public static final String VENDOR_GROUP_CREATED_SUCCESSFULLY = "S-084";
	public static final String VENDOR_GROUP_UPDATED_SUCCESSFULLY = "S-085";
	public static final String VENDOR_GROUP_DELETED_SUCCESSFULLY = "S-086";
	public static final String VENDOR_GROUP_CREATED_UNSUCCESSFULLY = "E-084";
	public static final String VENDOR_GROUP_UPDATED_UNSUCCESSFULLY = "E-085";
	public static final String VENDOR_GROUP_DELETED_UNSUCCESSFULLY = "E-086";

	public static final String DEPARTMENT_CREATED_SUCCESSFULLY = "S-087";
	public static final String DEPARTMENT_UPDATED_SUCCESSFULLY = "S-088";
	public static final String DEPARTMENT_DELETED_SUCCESSFULLY = "S-089";
	public static final String DEPARTMENT_CREATED_UNSUCCESSFULLY = "E-087";
	public static final String DEPARTMENT_UPDATED_UNSUCCESSFULLY = "E-088";
	public static final String DEPARTMENT_DELETED_UNSUCCESSFULLY = "E-089";

	public static final String CHART_OF_ACCOUNTS_CREATED_SUCCESSFULLY = "S-090";
	public static final String CHART_OF_ACCOUNTS_UPDATED_SUCCESSFULLY = "S-091";
	public static final String CHART_OF_ACCOUNTS_DELETED_SUCCESSFULLY = "S-092";
	public static final String CHART_OF_ACCOUNTS_CREATED_UNSUCCESSFULLY = "E-090";
	public static final String CHART_OF_ACCOUNTS_UPDATED_UNSUCCESSFULLY = "E-091";
	public static final String CHART_OF_ACCOUNTS_DELETED_UNSUCCESSFULLY = "E-092";

	public static final String TAX_RATE_CREATED_SUCCESSFULLY = "S-093";
	public static final String TAX_RATE_UPDATED_SUCCESSFULLY = "S-094";
	public static final String TAX_RATE_DELETED_SUCCESSFULLY = "S-095";
	public static final String TAX_RATE_CREATED_UNSUCCESSFULLY = "E-093";
	public static final String TAX_RATE_UPDATED_UNSUCCESSFULLY = "E-094";
	public static final String TAX_RATE_DELETED_UNSUCCESSFULLY = "E-095";

	public static final String TAX_GROUP_CREATED_SUCCESSFULLY = "S-096";
	public static final String TAX_GROUP_UPDATED_SUCCESSFULLY = "S-097";
	public static final String TAX_GROUP_DELETED_SUCCESSFULLY = "S-098";
	public static final String TAX_GROUP_CREATED_UNSUCCESSFULLY = "E-096";
	public static final String TAX_GROUP_UPDATED_UNSUCCESSFULLY = "E-097";
	public static final String TAX_GROUP_DELETED_UNSUCCESSFULLY = "E-098";

	public static final String IMPORT_PREVIEWED_SUCCESSFULLY = "S-099";
	public static final String IMPORT_PREVIEWED_UNSUCCESSFULLY = "E-099";

	public static final String IMPORT_PROCESSED_SUCCESSFULLY = "S-101";
	public static final String IMPORT_PROCESSED_UNSUCCESSFULLY = "E-101";

	public static final String EXPORT_PROCESSED_SUCCESSFULLY = "S-102";
	public static final String EXPORT_PROCESSED_UNSUCCESSFULLY = "E-102";
	public static final String JOURNAL_ENTRIES_REPORT_SUCCESSFULLY = "S-103";
	public static final String JOURNAL_ENTRIES_REPORT_UNSUCCESSFULLY = "E-103";

	public static final String VENDOR_SETTINGS_CREATED_SUCCESSFULLY = "S-104";
	public static final String VENDOR_SETTINGS_CREATED_UNSUCCESSFULLY = "E-104";
	public static final String VENDOR_GROUP_SETTINGS_CREATED_SUCCESSFULLY = "S-105";
	public static final String VENDOR_GROUP_SETTINGS_CREATED_UNSUCCESSFULLY = "E-105";
	public static final String VENDOR_SETTINGS_FETCHED_SUCCESSFULLY = "S-106";
	public static final String VENDOR_SETTINGS_FETCH_UNSUCCESSFULLY = "E-106";
	public static final String VENDOR_GROUP_SETTINGS_FETCHED_SUCCESSFULLY = "S-107";
	public static final String VENDOR_GROUP_SETTINGS_FETCHED_UNSUCCESSFULLY = "E-107";

	public static final String VENDOR_SETTINGS_ACTIVATED_SUCCESSFULLY = "S-108";
	public static final String VENDOR_SETTINGS_DEACTIVATED_SUCCESSFULLY = "S-109";
	public static final String VENDOR_GROUP_SETTINGS_ACTIVATED_SUCCESSFULLY = "S-110";
	public static final String VENDOR_GROUP_SETTINGS_DEACTIVATED_SUCCESSFULLY = "S-111";
	public static final String VENDOR_SETTINGS_UPDATED_UNSUCCESSFULLY = "E-108";
	public static final String VENDOR_GROUP_SETTINGS_UPDATED_UNSUCCESSFULLY = "E-109";
	public static final String VENDOR_SETTINGS_UPDATED_SUCCESSFULLY = "S-110";
	public static final String VENDOR_GROUP_SETTINGS_UPDATED_SUCCESSFULLY = "S-111";

	public static final String REFUND_CREATED_SUCCESSFULLY = "S-112";
	public static final String REFUND_UPDATED_SUCCESSFULLY = "S-113";
	public static final String REFUND_DELETED_SUCCESSFULLY = "S-114";
	public static final String REFUND_CREATED_UNSUCCESSFULLY = "E-112";
	public static final String REFUND_UPDATED_UNSUCCESSFULLY = "E-113";
	public static final String REFUND_DELETED_UNSUCCESSFULLY = "E-114";

	public static final String LETTER_OF_UNDERSTANDING_CREATED_SUCCESSFULLY = "S-115";
	public static final String LETTER_OF_UNDERSTANDING_UPDATED_SUCCESSFULLY = "S-116";
	public static final String LETTER_OF_UNDERSTANDING_DELETED_SUCCESSFULLY = "S-117";
	public static final String LETTER_OF_UNDERSTANDING_CREATED_UNSUCCESSFULLY = "E-115";
	public static final String LETTER_OF_UNDERSTANDING_UPDATED_UNSUCCESSFULLY = "E-116";
	public static final String LETTER_OF_UNDERSTANDING_DELETED_UNSUCCESSFULLY = "E-117";

	public static final String RECEIPT_CREATED_SUCCESSFULLY = "S-118";
	public static final String RECEIPT_UPDATED_SUCCESSFULLY = "S-119";
	public static final String RECEIPT_DELETED_SUCCESSFULLY = "S-120";
	public static final String RECEIPT_CREATED_UNSUCCESSFULLY = "E-118";
	public static final String RECEIPT_UPDATED_UNSUCCESSFULLY = "E-119";
	public static final String RECEIPT_DELETED_UNSUCCESSFULLY = "E-120";

	public static String BALANCE_CONFIRMATION_CREATED_SUCCESSFULLY = "S-121";
	public static final String BALANCE_CONFIRMATION_CREATED_UNSUCCESSFULLY = "E-121";
	public static final String BALANCE_CONFIRMATION_UPDATED_SUCCESSFULLY = "S-122";
	public static final String BALANCE_CONFIRMATION_UPDATED_UNSUCCESSFULLY = "E-122";
	public static final String BALANCE_CONFIRMATION_DECLINED_SUCCESSFULLY = "S-123";
	public static final String BALANCE_CONFIRMATION_DECLINED_UNSUCCESSFULLY = "E-123";
	public static final String BALANCE_CONFIRMATION_ACKNOWLEDGED_UNSUCCESSFULLY = "E-124";
	public static final String BALANCE_CONFIRMATION_ACKNOWLEDGED_SUCCESSFULLY = "S-124";
	public static final String BALANCE_CONFIRMATION_WITHDRAW_SUCCESSFULLY = "S-125";
	public static final String BALANCE_CONFIRMATION_WITHDRAW_UNSUCCESSFULLY = "E-125";

	public static final String CREDIT_NOTES_CREATED_SUCCESSFULLY = "S-126";
	public static final String CREDIT_NOTES_UPDATED_SUCCESSFULLY = "S-127";
	public static final String CREDIT_NOTES_DELETED_SUCCESSFULLY = "S-128";
	public static final String CREDIT_NOTES_CREATED_UNSUCCESSFULLY = "E-126";
	public static final String CREDIT_NOTES_UPDATED_UNSUCCESSFULLY = "E-127";
	public static final String CREDIT_NOTES_DELETED_UNSUCCESSFULLY = "E-128";

	public static final String APPLY_CREDITS_CREATED_SUCCESSFULLY = "S-129";
	public static final String APPLY_CREDITS_CREATED_UNSUCCESSFULLY = "E-129";
	public static final String APPLY_CREDITS_UPDATED_SUCCESSFULLY = "S-130";
	public static final String APPLY_CREDITS_UPDATED_UNSUCCESSFULLY = "E-130";

	public static final String WORKFLOW_GENERAL_SETTINGS_CREATED_SUCCESSFULLY="S-131";
	public static final String WORKFLOW_GENERAL_SETTINGS_UPDATED_SUCCESSFULLY="S-132";
	public static final String WORKFLOW_GENERAL_SETTINGS_DELETED_SUCCESSFULLY="S-133";
	public static final String WORKFLOW_GENERAL_SETTINGS_CREATED_UNSUCCESSFULLY="E-131";
	public static final String WORKFLOW_GENERAL_SETTINGS_UPDATED_UNSUCCESSFULLY="E-132";
	public static final String WORKFLOW_GENERAL_SETTINGS_DELETED_UNSUCCESSFULLY="E-133";

	public static final String WORKFLOW_APPROVAL_CREATED_SUCCESSFULLY="S-134";
	public static final String WORKFLOW_APPROVAL_CREATED_UNSUCCESSFULLY="E-134";



	public static final String PAY_PERIOD_CREATED_SUCCESSFULLY = "S-135";
	public static final String PAY_PERIOD_UPDATED_SUCCESSFULLY = "S-136";
	public static final String PAY_PERIOD_DELETED_SUCCESSFULLY = "S-137";
	public static final String PAY_PERIOD_CREATED_UNSUCCESSFULLY = "E-135";
	public static final String PAY_PERIOD_UPDATED_UNSUCCESSFULLY = "E-136";
	public static final String PAY_PERIOD_DELETED_UNSUCCESSFULLY = "E-137";


	public static final String PAY_RUN_CREATED_SUCCESSFULLY = "S-138";
	public static final String PAY_RUN_UPDATED_SUCCESSFULLY = "S-139";
	public static final String PAY_RUN_DELETED_SUCCESSFULLY = "S-140";
	public static final String PAY_RUN_SETTINGS_UPDATED_SUCCESSFULLY = "S-141";
	public static final String PAY_RUN_CREATED_UNSUCCESSFULLY = "E-138";
	public static final String PAY_RUN_UPDATED_UNSUCCESSFULLY = "E-139";
	public static final String PAY_RUN_DELETED_UNSUCCESSFULLY = "E-140";
	public static final String PAY_RUN_SETTINGS_UPDATED_UNSUCCESSFULLY = "E-141";

	public static final String PROFILE_SUCCESS = "S-142";
	public static final String PROFILE_FAILURE = "E-142";

	public static final String CHANGE_PASSWORD_SUCCESS = "S-143";
	public static final String CHANGE_PASSWORD_FAILURE = "E-143";

	public static final String SET_DEFAULT_ORG_SUCCESS = "S-144";
	public static final String SET_DEFAULT_ORG_FAILURE = "E-144";

	public static final String PF_REFRESH_SUCCESS="S-145";
	public static final String PF_REFRESH_FAILURE="E-145";
	
	public static final String PF_DISCONNECT_SUCCESS="S-146";
	public static final String PF_DISCONNECT_FAILURE="E-146";
	


	//Yes Bank Integeration

	public static final String YBL_BENEFICIARY_CREATED_SUCCESSFULLY = "S-147";
	public static final String YBL_BENEFICIARY_UPDATED_SUCCESSFULLY = "S-148";
	public static final String YBL_BENEFICIARY_DELETED_SUCCESSFULLY = "S-149";
	public static final String YBL_BENEFICIARY_FETCH_SUCCESSFULLY = "S-150";
	public static final String YBL_BENEFICIARY_CREATED_UNSUCCESSFULLY = "E-147";
	public static final String YBL_BENEFICIARY_UPDATED_UNSUCCESSFULLY = "E-148";
	public static final String YBL_BENEFICIARY_DELETED_UNSUCCESSFULLY = "E-149";
	public static final String YBL_BENEFICIARY_FETCH_UNSUCCESSFULLY = "E-150";
	public static final String YBL_PAYMENT_FETCH_UNSUCCESSFULLY = "E-151";


	public static final String YBL_API_BANKING_ENABLED_SUCCESSFULLY = "S-152";
	public static final String YBL_API_BANKING_ENABLED_UNSUCCESSFULLY = "E-152";
	public static final String YBL_API_BANKING_REGISTERED_SUCCESSFULLY = "S-152";
	public static final String YBL_API_BANKING_REGISTERED_UNSUCCESSFULLY = "E-152";
	
	public static final String OTP_SENT_SUCCESSFULLY = "S-153";
	public static final String OTP_SENT_UNSUCCESSFULLY = "E-153";

	public static final String OTP_VERIFY_SUCCESSFULLY = "S-154";
	public static final String OTP_VERIFY_UNSUCCESSFULLY = "E-154";
	
	public static final String BANK_STATEMENT_REFRESHED_SUCCESSFULLY = "S-155";
	public static final String BANK_STATEMENT_REFRESHED_UNSUCCESSFULLY = "E-155";
	
	//Gst for given PAN No
	
	public static final String PAN_FOR_GST_CREATED_SUCCESSFULLY = "S-156";
	public static final String PAN_FOR_GST_CREATED_UNSUCCESSFULLY = "E-156";
	
	
	public static final String TDS_CREATED_SUCCESSFULLY="S-157";
	public static final String TDS_CREATED_UNSUCCESSFULLY="E-157";	
	public static final String TDS_UPDATED_SUCCESSFULLY="S-158";
	public static final String TDS_UPDATED_UNSUCCESSFULLY="E-158";
	public static final String TDS_DELETED_SUCCESSFULLY="S-159";
	public static final String TDS_DELETED_UNSUCCESSFULLY="E-159";
	
	public static final String LIST_PAGE_CUSTOMIZE_UPDATED_SUCCESSFULLY="S-160";
	public static final String LIST_PAGE_CUSTOMIZE_UPDATED_UNSUCCESSFULLY="E-160";
	
	
	public static final String APPLY_FUND_FOR_VENDOR_CREDIT_SUCCESSFULLY="S-161";
	public static final String APPLY_FUND_FOR_VENDOR_CREDIT_UNSUCCESSFULLY="E-161";
	
	public static final String VENDOR_CREDIT_CREATE_SUCCESSFULLY="S-162";
	public static final String VENDOR_CREDIT_CREATE_UNSUCCESSFULLY="E-162";
	
	public static final String VENDOR_CREDIT_UPDATE_SUCCESSFULLY="S-163";
	public static final String VENDOR_CREDIT_UPDATE_UNSUCCESSFULLY="E-163";
	
	public static final String VENDOR_CREDIT_NOTE_GET_SUCCESSFULLY="S-164";
	public static final String VENDOR_CREDIT_NOTE_GET_UNSUCCESSFULLY="E-164";
	
	/*
	 * ****************************************************************************
	 * codes
	 */

	public static final String SUCCESS_SUBSCRIPTIONS_FETCH = "Subscriptions Fetched Successfully";
	public static final String FAILURE_SUBSCRIPTIONS_FETCH = "Error During Fetch Subscriptions";
	public static final String SUCCESS_APPLICATIONS_FETCH = "Applications Fetched Successfully";
	public static final String FAILURE_APPLICATIONS_FETCH = "Error During Fetch Applications And Modules";
	public static final String SUCCESS_ORGANIZATION_INDUSTRY_FETCH = "Organization Industry Fetched Successfully";
	public static final String FAILURE_ORGANIZATION_INDUSTRY_FETCH = "Error During Fetch Organization Industry";
	public static final String SUCCESS_ORGANIZATION_CONSTITUTION_FETCH = "Organization Constitution Fetched Successfully";
	public static final String FAILURE_ORGANIZATION_CONSTITUTION_FETCH = "Error During Fetch Organization Constitution";
	public static final String SUCCESS_ORGANIZATION_TYPE = "Organization Type Fetched Successfully";
	public static final String FAILURE_ORGANIZATION_TYPE = "Error During Fetch Organization Type";
	public static final String SUCCESS_BASE_CURRENCY = "Base Currency Fetched Successfully";
	public static final String FAILURE_BASE_CURRENCY = "Error During Fetch Base Currency";
	public static final String SUCCESS_BANK_DETAILS_FETCH = "Bank Details Fetched Successfully";
	public static final String FAILURE_BANK_DETAILS_FETCH = "Error During Fetch Bank Details";
	public static final String INVALID_BANK_DETAILS_FETCH = "Invalid IFSC Code";
	public static final String SUCCESS_TIME_ZONE_FETCH = "Time Zone Fetched Successfully";
	public static final String FAILURE_TIME_ZONE_FETCH = "Error During Fetch Time Zone";


	public static final String SUCCESS_ORGANIZATION_CREATED = "Organization Created Successfully";
	public static final String SUCCESS_ORGANIZATION_UPDATED = "Organization Updated Successfully";
	public static final String SUCCESS_ORGANIZATION_DELETED = "Organization Deleted Successfully";
	public static final String SUCCESS_ORGANIZATION_FETCH = "Organization Fetched Successfully";
	public static final String FAILURE_ORGANIZATION_CREATED = "Error During Organization Creation";
	public static final String FAILURE_ORGANIZATION_UPDATED = "Error During Organization Updation";
	public static final String FAILURE_ORGANIZATION_DELETED = "Error During Organization Deletion";
	public static final String FAILURE_ORGANIZATION_FETCH = "Error During Organization Fetch";
	public static final String SUCCESS_ORGANIZATION_ACTIVATED = "Organization Activated Successfully";
	public static final String SUCCESS_ORGANIZATION_DEACTIVATED = "Organization DeActivated Successfully";

	public static final String SUCCESS_USER_CREATED = "User Created Successfully";
	public static final String SUCCESS_USER_UPDATED = "User Updated Successfully";
	public static final String SUCCESS_USER_DELETED = "User Deleted Successfully";
	public static final String SUCCESS_USER_FETCH = "User Fetched Successfully";
	public static final String FAILURE_USER_CREATED = "Error During User Creation";
	public static final String FAILURE_USER_UPDATED = "Error During User Updation";
	public static final String FAILURE_USER_DELETED = "Error During User Deletion";
	public static final String FAILURE_USER_FETCH = "Error During User Fetch";
	public static final String SUCCESS_USER_ACTIVATED = "User Activated Successfully";
	public static final String SUCCESS_USER_DEACTIVATED = "User DeActivated Successfully";

	public static final String SUCCESS_PO_CREATED = "Purchase Order Created Successfully";
	public static final String SUCCESS_PO_UPDATED = "Purchase Order Updated Successfully";
	public static final String SUCCESS_PO_DELETED = "Purchase Order Deleted Successfully";
	public static final String SUCCESS_PO_FETCH = "Purchase Order Fetched Successfully";
	public static final String FAILURE_PO_CREATED = "Error During Purchase Order Creation";
	public static final String FAILURE_PO_UPDATED = "Error During Purchase Order Updation";
	public static final String FAILURE_PO_DELETED = "Error During Purchase Order Deletion";
	public static final String FAILURE_PO_FETCH = "Error During Purchase Order Fetch";
	public static final String SUCCESS_PO_DEACTIVATED = "Purchase Order DeActivated Successfully";
	public static final String SUCCESS_PO_ACTIVATED = "Purchase Order Activated Successfully";
	public static final String SUCCESS_PO_ACCEPT = "Purchase Order Accepted Successfully";
	public static final String FAILURE_PO_ACCEPT = "Error During Purchase Order Accept";
	public static final String SUCCESS_PO_DECLINE = "Purchase Order Declined Successfully";
	public static final String FAILURE_PO_DECLINE = "Error During Purchase Order Decline";

	public static final String SUCCESS_ROLE_CREATED = "Role Created Successfully";
	public static final String SUCCESS_ROLE_UPDATED = "Role Updated Successfully";
	public static final String SUCCESS_ROLE_DELETED = "Role Deleted Successfully";
	public static final String SUCCESS_ROLE_FETCH = "Role Fetched Successfully";
	public static final String FAILURE_ROLE_CREATED = "Error During Role Creation";
	public static final String FAILURE_ROLE_UPDATED = "Error During Role Updation";
	public static final String FAILURE_ROLE_DELETED = "Error During Role Deletion";
	public static final String FAILURE_ROLE_FETCH = "Error During Role Fetch";
	public static final String SUCCESS_ROLE_ACTIVATED = "Role Activated Successfully";
	public static final String SUCCESS_ROLE_DEACTIVATED = "Role DeActivated Successfully";

	public static final String SUCCESS_CUSTOMER_CREATED = "Customer Created Successfully";
	public static final String SUCCESS_CUSTOMER_UPDATED = "Customer Updated Successfully";
	public static final String SUCCESS_CUSTOMER_FETCH = "Customer details Fetched Successfully";
	public static final String FAILURE_CUSTOMER_CREATED = "Error During Customer Creation";
	public static final String FAILURE_CUSTOMER_UPDATED = "Error During Customer Updation";
	public static final String FAILURE_CUSTOMER_DEACTIVATED = "Error During Customer Deactivation";
	public static final String FAILURE_CUSTOMER_FETCH = "Error During Retrieving Customer";
	public static final String SUCCESS_CUSTOMER_DEACTIVATED = "Customer DeActivated Successfully";
	public static final String SUCCESS_CUSTOMER_ACTIVATED = "Customer Activated Successfully";

	public static final String SUCCESS_LEDGER_CREATED = "Ledger Created Successfully";
	public static final String SUCCESS_LEDGER_UPDATED = "Ledger Updated Successfully";
	public static final String SUCCESS_LEDGER_FETCH = "Ledger details Fetched Successfully";
	public static final String FAILURE_LEDGER_CREATED = "Error During Ledger Creation";
	public static final String FAILURE_LEDGER_UPDATED = "Error During Ledger Updation";
	public static final String FAILURE_LEDGER_DEACTIVATED = "Error During Ledger Deactivation";
	public static final String FAILURE_LEDGER_FETCH = "Error During Retrieving Ledger";
	public static final String SUCCESS_LEDGER_DEACTIVATED = "Ledger DeActivated Successfully";
	public static final String SUCCESS_LEDGER_ACTIVATED = "Ledger Activated Successfully";

	public static final String SUCCESS_BILLS_INVOICE_CREATED = "Invoice Created Successfully";
	public static final String SUCCESS_BILLS_INVOICE_UPDATED = "Invoice Updated Successfully";
	public static final String SUCCESS_BILLS_INVOICE_DELETED = "Invoice Deleted Successfully";
	public static final String SUCCESS_BILLS_INVOICE_FETCH = "Invoice details Fetched Successfully";
	public static final String FAILURE_BILLS_INVOICE_CREATED = "Error During Invoice Creation";
	public static final String FAILURE_BILLS_INVOICE_UPDATED = "Error During Invoice Updation";
	public static final String FAILURE_BILLS_INVOICE_DELETED = "Error During Invoice Deletion";
	public static final String FAILURE_BILLS_INVOICE_DEACTIVATED = "Error During Invoice Deactivation";
	public static final String FAILURE_BILLS_INVOICE_FETCH = "Error During Retrieving Invoice";
	public static final String SUCCESS_BILLS_INVOICE_DEACTIVATED = "Invoice DeActivated Successfully";
	public static final String SUCCESS_BILLS_INVOICE_ACTIVATED = "Invoice Activated Successfully";
	public static final String SUCCESS_BILLS_INVOICE_VOIDED = "Invoice Voided Successfully";

	public static final String SUCCESS_ON_CALCULATION = "Calculated Successfully";
	public static final String FAILURE_DURING_CALCULATION = "Error During Calculation";

	public static final String SUCCESS_VENDOR_CREATED = "Vendor Created Successfully";
	public static final String SUCCESS_VENDOR_UPDATED = "Vendor Updated Successfully";
	public static final String SUCCESS_VENDOR_DELETED = "Vendor Deleted Successfully";
	public static final String SUCCESS_VENDORS_NAMES_FETCH = "Vendor names fetched successfully";
	public static final String SUCCESS_VENDOR_FETCH = "Vendor Fetched Successfully";
	public static final String FAILURE_VENDOR_CREATED = "Error During Vendor Creation";
	public static final String FAILURE_VENDOR_UPDATED = "Error During Vendor Updation";
	public static final String FAILURE_VENDOR_DELETED = "Error During Vendor Deletion";
	public static final String FAILURE_VENDOR_FETCH = "Error During Vendor Fetch";
	public static final String FAILURE_VENDOR_NAMES_FETCH = "Error During vendor names Fetch";
	public static final String SUCCESS_VENDOR_ACTIVATED = "Vendor Activated Successfully";
	public static final String SUCCESS_VENDOR_DEACTIVATED = "Vendor DeActivated Successfully";

	public static final String SUCCESS_EMPLOYEE_CREATED = "Employee Created Successfully";
	public static final String SUCCESS_EMPLOYEE_UPDATED = "Employee Updated Successfully";
	public static final String SUCCESS_EMPLOYEE_DELETED = "Employee Deleted Successfully";
	public static final String FAILURE_EMPLOYEE_CREATED = "Error during Employee Creation";
	public static final String FAILURE_EMPLOYEE_UPDATED = "Error during Employee Updation";
	public static final String FAILURE_EMPLOYEE_DELETED = "Error during Employee Deletion";
	public static final String SUCCESS_EMPLOYEE_FETCH = "Employee Fetched Successfully";
	public static final String FAILURE_EMPLOYEE_FETCH = "Error during Employee Fetch";
	public static final String SUCCESS_EMPLOYEE_ACTIVATED = "Employee Activated Successfully";
	public static final String SUCCESS_EMPLOYEE_DEACTIVATED = "Employee DeActivated Successfully";

	public static final String SUCCESS_PAYMENT_TERMS_CREATED = "Payment Terms Created Successfully";
	public static final String SUCCESS_PAYMENT_TERMS_UPDATED = "Payment Terms Updated Successfully";
	public static final String SUCCESS_PAYMENT_TERMS_DELETED = "Payment Terms Deleted Successfully";
	public static final String FAILURE_PAYMENT_TERMS_CREATED = "Error during Payment Terms Creation";
	public static final String FAILURE_PAYMENT_TERMS_UPDATED = "Error during Payment Terms Updation";
	public static final String FAILURE_PAYMENT_TERMS_DELETED = "Error during Payment Terms Deletion";
	public static final String FAILURE_PAYMENT_TERMS_FETCH = "Error during Payment Terms Fetch";
	public static final String SUCCESS_PAYMENT_TERMS_FETCH = "Payment Terms fetched Successfully";
	public static final String SUCCESS_PAYMENT_TERMS_ACTIVATED = "Payment Terms Activated Successfully";
	public static final String SUCCESS_PAYMENT_TERMS_DEACTIVATED = "Payment Terms DeActivated Successfully";

	public static final String SUCCESS_TAX_RATE_CREATED = "Tax Rate Created Successfully";
	public static final String SUCCESS_TAX_RATE_UPDATED = "Tax Rate Updated Successfully";
	public static final String SUCCESS_TAX_RATE_DELETED = "Tax Rate Deleted Successfully";
	public static final String FAILURE_TAX_RATE_CREATED = "Error during Tax Rate Creation";
	public static final String FAILURE_TAX_RATE_UPDATED = "Error during Tax Rate Updation";
	public static final String FAILURE_TAX_RATE_DELETED = "Error during Tax Rate Deletion";
	public static final String SUCCESS_TAX_RATE_FETCH = "Tax Rate Fetched Successfully";
	public static final String FAILURE_TAX_RATE_FETCH = "Error during Tax Rate Fetch";
	public static final String SUCCESS_TAX_RATE_ACTIVATED = "Tax Rate Activated Successfully";
	public static final String SUCCESS_TAX_RATE_DEACTIVATED = "Tax Rate DeActivated Successfully";

	public static final String SUCCESS_TAX_GROUP_CREATED = "Tax Group Created Successfully";
	public static final String SUCCESS_TAX_GROUP_UPDATED = "Tax Group Updated Successfully";
	public static final String SUCCESS_TAX_GROUP_DELETED = "Tax Group Deleted Successfully";
	public static final String FAILURE_TAX_GROUP_CREATED = "Error during Tax Group Creation";
	public static final String FAILURE_TAX_GROUP_UPDATED = "Error during Tax Group Updation";
	public static final String FAILURE_TAX_GROUP_DELETED = "Error during Tax Group Deletion";
	public static final String SUCCESS_TAX_GROUP_FETCH = "Tax Group Fetched Successfully";
	public static final String FAILURE_TAX_GROUP_FETCH = "Error during Tax Group Fetch";
	public static final String SUCCESS_TAX_GROUP_ACTIVATED = "Tax Group Activated Successfully";
	public static final String SUCCESS_TAX_GROUP_DEACTIVATED = "Tax Group DeActivated Successfully";

	public static final String SUCCESS_CURRENCY_CREATED = "Currency Created Successfully";
	public static final String SUCCESS_CURRENCY_UPDATED = "Currency Updated Successfully";
	public static final String SUCCESS_CURRENCY_DELETED = "Currency Deleted Successfully";
	public static final String SUCCESS_CURRENCY_FETCH = "Currency Fetched Successfully";
	public static final String FAILURE_CURRENCY_CREATED = "Error During Currency Creation";
	public static final String FAILURE_CURRENCY_UPDATED = "Error During Currency Updation";
	public static final String FAILURE_CURRENCY_DELETED = "Error During Currency Deletion";
	public static final String FAILURE_CURRENCY_FETCH = "Error During Currency Fetch";
	public static final String SUCCESS_CURRENCY_ACTIVATED = "Currency Activated Successfully";
	public static final String SUCCESS_CURRENCY_DEACTIVATED = "Currency DeActivated Successfully";

	public static final String SUCCESS_SHIPPING_PREFERENCE_CREATED = "Shipping Preferences Created Successfully";
	public static final String SUCCESS_SHIPPING_PREFERENCES_UPDATED = "Shipping Preferences Updated Successfully";
	public static final String SUCCESS_SHIPPING_PREFERENCES_DELETED = "Shipping Preferences Deleted Successfully";
	public static final String FAILURE_SHIPPING_PREFERENCE_CREATED = "Error during Shipping Preference Creation";
	public static final String FAILURE_SHIPPING_PREFERENCES_UPDATED = "Error during Shipping Preference Updation";
	public static final String FAILURE_SHIPPING_PREFERENCES_DELETED = "Error during Shipping Preferences Deletion";
	public static final String SUCCESS_SHIPPING_PREFERENCES_FETCH = "Shipping Preferences fetched Successfully";
	public static final String FAILURE_SHIPPING_PREFERENCES_FETCH = "Error during Shipping Preferences Fetch";
	public static final String SUCCESS_SHIPPING_PREFERENCES_ACTIVATED = "Shipping Preferences Activated Successfully";
	public static final String SUCCESS_SHIPPING_PREFERENCES_DEACTIVATED = "Shipping Preferences DeActivated Successfully";

	public static final String SUCCESS_VOUCHER_CREATED = "Voucher Created Successfully";
	public static final String SUCCESS_VOUCHER_UPDATED = "Voucher Updated Successfully";
	public static final String SUCCESS_VOUCHER_DELETED = "Voucher Deleted Successfully";
	public static final String FAILURE_VOUCHER_CREATED = "Error during Voucher Creation";
	public static final String FAILURE_VOUCHER_UPDATED = "Error during Voucher Updation";
	public static final String FAILURE_VOUCHER_DELETED = "Error during Voucher Deletion";
	public static final String SUCCESS_VOUCHER_FETCH = "Voucher Fetched Successfully";
	public static final String FAILURE_VOUCHER_FETCH = "Error during Voucher Fetch";
	public static final String SUCCESS_VOUCHER_ACTIVATED = "Voucher Activated Successfully";
	public static final String SUCCESS_VOUCHER_DEACTIVATED = "Voucher DeActivated Successfully";

	public static final String SUCCESS_PRODUCT_CREATED = "Product Created Successfully";
	public static final String SUCCESS_PRODUCT_UPDATED = "Product Updated Successfully";
	public static final String SUCCESS_PRODUCT_DELETED = "Product Deleted Successfully";
	public static final String FAILURE_PRODUCT_CREATED = "Error during Product Creation";
	public static final String FAILURE_PRODUCT_UPDATED = "Error during Product Updation";
	public static final String FAILURE_PRODUCT_DELETED = "Error during Product Deletion";
	public static final String SUCCESS_PRODUCT_FETCH = "Product Fetched Successfully";
	public static final String FAILURE_PRODUCT_FETCH = "Error during Product Fetch";
	public static final String SUCCESS_PRODUCT_ACTIVATED = "Product Activated Successfully";
	public static final String SUCCESS_PRODUCT_DEACTIVATED = "Product DeActivated Successfully";



	public static final String SUCCESS_PRODUCT_CATEGORY_CREATED = "Product Category Created Successfully";
	public static final String FAILURE_PRODUCT_CATEGORY_CREATED = "Error during Product Category Creation";
	public static final String SUCCESS_PRODUCT_CATEGORY_UPDATED = "Product Category Updated Successfully";
	public static final String FAILURE_PRODUCT_CATEGORY_UPDATED = "Error during Product Category Updation";



	public static final String SUCCESS_CUSTOMER_GROUP_CREATED = "Customer Group Created Successfully";
	public static final String SUCCESS_CUSTOMER_GROUP_UPDATED = "Customer Group Updated Successfully";
	public static final String SUCCESS_CUSTOMER_GROUP_DELETED = "Customer Group Deleted Successfully";
	public static final String FAILURE_CUSTOMER_GROUP_CREATED = "Error during Customer Group Creation";
	public static final String FAILURE_CUSTOMER_GROUP_UPDATED = "Error during Customer Group Updation";
	public static final String FAILURE_CUSTOMER_GROUP_DELETED = "Error during Customer Group Deletion";
	public static final String SUCCESS_CUSTOMER_GROUP_FETCH = "Customer Group Fetched Successfully";
	public static final String FAILURE_CUSTOMER_GROUP_FETCH = "Error during Customer Group Fetch";
	public static final String SUCCESS_CUSTOMER_GROUP_ACTIVATED = "Customer Group Activated Successfully";
	public static final String SUCCESS_CUSTOMER_GROUP_DEACTIVATED = "Customer Group DeActivated Successfully";

	public static final String SUCCESS_VENDOR_GROUP_CREATED = "Vendor Group Created Successfully";
	public static final String SUCCESS_VENDOR_GROUP_UPDATED = "Vendor Group Updated Successfully";
	public static final String SUCCESS_VENDOR_GROUP_DELETED = "Vendor Group Deleted Successfully";
	public static final String FAILURE_VENDOR_GROUP_CREATED = "Error during Vendor Group Creation";
	public static final String FAILURE_VENDOR_GROUP_UPDATED = "Error during Vendor Group Updation";
	public static final String FAILURE_VENDOR_GROUP_DELETED = "Error during Vendor Group Deletion";
	public static final String SUCCESS_VENDOR_GROUP_FETCH = "Vendor Group Fetched Successfully";
	public static final String FAILURE_VENDOR_GROUP_FETCH = "Error during Vendor Group Fetch";
	public static final String SUCCESS_VENDOR_GROUP_ACTIVATED = "Vendor Group Activated Successfully";
	public static final String SUCCESS_VENDOR_GROUP_DEACTIVATED = "Vendor Group DeActivated Successfully";

	public static final String SUCCESS_DEPARTMENT_CREATED = "Department Created Successfully";
	public static final String SUCCESS_DEPARTMENT_UPDATED = "Department Updated Successfully";
	public static final String SUCCESS_DEPARTMENT_DELETED = "Department Deleted Successfully";
	public static final String FAILURE_DEPARTMENT_CREATED = "Error during Department Creation";
	public static final String FAILURE_DEPARTMENT_UPDATED = "Error during Department Updation";
	public static final String FAILURE_DEPARTMENT_DELETED = "Error during Department Deletion";
	public static final String SUCCESS_DEPARTMENT_FETCH = "Department Fetched Successfully";
	public static final String FAILURE_DEPARTMENT_FETCH = "Error during Department Fetch";
	public static final String SUCCESS_DEPARTMENT_ACTIVATED = "Department Activated Successfully";
	public static final String SUCCESS_DEPARTMENT_DEACTIVATED = "Department DeActivated Successfully";

	public static final String SUCCESS_ACCOUNTING_ASPECTS_CREATED = "Accounting Entries Created Successfully";
	public static final String SUCCESS_ACCOUNTING_ASPECTS_UPDATED = "Accounting Entries Updated Successfully";
	public static final String FAILURE_ACCOUNTING_ASPECTS_CREATED = "Error during Accounting Entries Creation";
	public static final String FAILURE_ACCOUNTING_ASPECTS_UPDATED = "Erro during Accounting Entries Updation";
	public static final String FAILURE_ACCOUNTING_ASPECTS_DELETED = "Error during Accounting Entries Deletion";
	public static final String SUCCESS_ACCOUNTING_ASPECTS_FETCH = "Accounting Entries Fetched Successfully";
	public static final String FAILURE_ACCOUNTING_ASPECTS_FETCH = "Error During Accounting Entries Fetch";
	public static final String SUCCESS_ACCOUNTING_ASPECTS_ACTIVATED = "Accounting Entries Activated Successfully";
	public static final String SUCCESS_ACCOUNTING_ASPECTS_DEACTIVATED = "Accounting Entries DeActivated Successfully";

	public static final String SUCCESS_BANK_MASTER_ACCOUNTS_CREATED = "Bank Master Account Created Successfully";
	public static final String SUCCESS_BANK_MASTER_CARD_CREATED = "Bank Master Card Created Successfully";
	public static final String SUCCESS_BANK_MASTER_WALLET_CREATED = "Bank Master Wallet Created Successfully";
	public static final String SUCCESS_BANK_MASTER_CASH_ACCOUNT_CREATED = "Bank Master Cash Account Created Successfully";
	public static final String SUCCESS_BANK_MASTER_ACCOUNT_UPDATED = "Bank Master Account Updated Successfully";
	public static final String SUCCESS_BANK_MASTER_CARD_UPDATED = "Bank Master Card Updated Successfully";
	public static final String SUCCESS_BANK_MASTER_WALLET_UPDATED = "Bank Master Wallet Updated Successfully";
	public static final String SUCCESS_BANK_MASTER_CASH_ACCOUNT_UPDATED = "Bank Master Cash Account Updated Successfully";

	public static final String FAILURE_BANK_MASTER_ACCOUNTS_CREATED = "Error during Bank Master Account Creation";
	public static final String FAILURE_BANK_MASTER_CARD_CREATED = "Error during Bank Master Card Creation";
	public static final String FAILURE_BANK_MASTER_WALLET_CREATED = "Error during Bank Master Wallet Creation";
	public static final String FAILURE_BANK_MASTER_CASH_ACCOUNT_CREATED = "Error during Bank Master Account Cash Creation";
	public static final String FAILURE_BANK_MASTER_ACCOUNT_UPDATED = "Error during Bank Master Account Updation";
	public static final String FAILURE_BANK_MASTER_CARD_UPDATED = "Error during Bank Master Card Updation";
	public static final String FAILURE_BANK_MASTER_WALLET_UPDATED = "Error during Bank Master Wallet Updation";
	public static final String FAILURE_BANK_MASTER_CASH_ACCOUNT_UPDATED = "Error during Bank Master Cash Account Updation";

	public static final String SUCCESS_BANK_MASTER_ACCOUNT_FETCH = "Bank Master Account Fetched Successfully";
	public static final String FAILURE_BANK_MASTER_ACCOUNT_FETCH = "Bank Master Account Fetched UnSuccessfully";
	public static final String SUCCESS_BANK_MASTER_CARD_FETCH = "Bank Master Card Fetched Successfully";
	public static final String FAILURE_BANK_MASTER_CARD_FETCH = "Bank Master Card Fetched UnSuccessfully";
	public static final String SUCCESS_BANK_MASTER_WALLET_FETCH = "Bank Master Wallet Fetched Successfully";
	public static final String FAILURE_BANK_MASTER_WALLET_FETCH = "Bank Master Wallet Fetched UnSuccessfully";
	public static final String SUCCESS_BANK_MASTER_DETAILS_FETCH = "Bank Master All Accounts Fetched Successfully";
	public static final String FAILURE_BANK_MASTER_DETAILS_FETCH = "Bank Master All Accounts Fetched UnSuccessfully";
	public static final String SUCCESS_BANK_MASTER_CASH_ACCOUNT_FETCH = "Bank Master Cash Accounts Fetched Successfully";
	public static final String FAILURE_BANK_MASTER_CASH_ACCOUNT_FETCH = "Bank Master Cash Account Fetched Successfully";

	// Import And Export
	public static final String SUCCESS_IMPORT_PROCESSED = "Import Processed Successfully";
	public static final String FAILURE_IMPORT_PROCESSED = "Import Processed UnSuccessfully";
	public static final String SUCCESS_EXPORT_PROCESSED = "Export Processed Successfully";
	public static final String FAILURE_EXPORT_PROCESSED = "Export Processed UnSuccessfully";

	public static final String SUCCESS_IMPORT_PREVIEWED = "Import reviewed Successfully";
	public static final String FAILURE_IMPORT_PREVIEWED = "Import Previewed UnSuccessfully";

	public static final String SUCCESS_PAY_ITEM_CREATED = "Pay Item Created Successfully";
	public static final String SUCCESS_PAY_ITEM_UPDATED = "Pay Item Updated Successfully";
	public static final String SUCCESS_PAY_ITEM_DELETED = "Pay Item Deleted Successfully";
	public static final String FAILURE_PAY_ITEM_CREATED = "Error during Pay Item Creation";
	public static final String FAILURE_PAY_ITEM_UPDATED = "Error during Pay Item Updation";
	public static final String FAILURE_PAY_ITEM_DELETED = "Error during Pay Item Deletion";
	public static final String SUCCESS_PAY_ITEM_FETCH = "Pay Item Fetched Successfully";
	public static final String FAILURE_PAY_ITEM_FETCH = "Error during Pay Item Fetch";
	public static final String SUCCESS_PAY_ITEM_ACTIVATED = "Pay Item Activated Successfully";
	public static final String SUCCESS_PAY_ITEM_DEACTIVATED = "Pay Item DeActivated Successfully";

	public static final String SUCCESS_PAY_TYPE_CREATED = "Pay Type Created Successfully";
	public static final String SUCCESS_PAY_TYPE_UPDATED = "Pay Type Updated Successfully";
	public static final String SUCCESS_PAY_TYPE_DELETED = "Pay Type Deleted Successfully";
	public static final String FAILURE_PAY_TYPE_CREATED = "Error during Pay Type Creation";
	public static final String FAILURE_PAY_TYPE_UPDATED = "Error during Pay Type Updation";
	public static final String FAILURE_PAY_TYPE_DELETED = "Error during Pay Type Deletion";
	public static final String SUCCESS_PAY_TYPE_FETCH = "Pay Type Fetched Successfully";
	public static final String FAILURE_PAY_TYPE_FETCH = "Error during Pay Type Fetch";
	public static final String SUCCESS_PAY_TYPE_ACTIVATED = "Pay Type Activated Successfully";
	public static final String SUCCESS_PAY_TYPE_DEACTIVATED = "Pay Type DeActivated Successfully";

	// Login ,Registration and Logout

	public static final String SUCCESS_LOGIN = "Login Successful";
	public static final String SUCCESS_LOGOUT = "Logout Successful";
	public static final String SUCCESS_TOKEN_EXPIRED = "Token Expiration Successful";
	public static final String FAILURE_LOGIN = "Login Failure";
	public static final String FAILURE_TOKEN_EXPIRED = "Token Expiration Failure";
	public static final String FAILURE_LOGOUT = "Logout Failure";
	public static final String SUCCESS_REGISTRATION = "Registration Successful";
	public static final String SUCCESS_ALL_REGISTRATIONS_FETCH = "Registrations Fetched Successfully";
	public static final String SUCCESS_REGISTRATION_FETCH = "Registration Fetched Successfully";
	public static final String FAILURE_REGISTRATION = "Registration Failure";
	public static final String FAILURE_ALL_REGISTRATIONS_FETCH = "Error During Fetch All Registrations";
	public static final String FAILURE_REGISTRATION_FETCH = "Error During Registration Fetch";
	public static final String SUCCESS_RECOVERY_PASSWORD = "Recover Password Successful";
	public static final String FAILURE_RECOVERY_PASSWORD = "Recovery Password Failure";
	public static final String SUCCESS_RESET_PASSWORD = "Reset Password Successful";
	public static final String FAILURE_RESET_PASSWORD = "Reset Password Failure";



	public static final String SUCCESS_CREATE_PASSWORD = "Create Password Successful";
	public static final String FAILURE_CREATE_PASSWORD = "Create Password Failure";

	public static final String SUCCESS_CHANGE_PASSWORD = "Change Password Successful";
	public static final String FAILURE_CHANGE_PASSWORD = "Change Password Failure";

	public static final String SUCCESS_DROP_DOWN_FETCH = "Drop Down Fetched Successfully";
	public static final String FAILURE_DROP_DOWN_FETCH = "Error During Drop Down Fetch";

	public static final String TEMPLATE_SUCCESS_FILE_FETCH = "Template File Fetched Successfully";
	public static final String TEMPLATE_FAILURE_FILE_FETCH = "Error During Template File Fetch";
	// Get and Authentication Failure

	public static final String AUTHENTICATION_FAILED = "Authentication Failure";

	public static final String FAILURE_IN_USER_ORGANIZATION_REQUEST_VALIDATION = "The User And Organization Details cannot be null";

	// Expenses

	public static final String SUCCESS_EXPENSES_NATURE_OF_SPENDING_FETCH = "Nature of Spending Fetched Successfully";
	public static final String FAILURE_EXPENSES_NATURE_OF_SPENDING_FETCH = "Error while fetching Nature of Spending";

	public static final String SUCCESS_EXPENSES_STATUS_FETCH = "Expense Status Fetched Successfully";
	public static final String FAILURE_EXPENSES_STATUS_FETCH = "Error while fetching Expense Status";

	public static final String SUCCESS_EXPENSE_CREATED = "Expense Created Successfully";
	public static final String SUCCESS_EXPENSE_UPDATED = "Expense Updated Successfully";
	public static final String SUCCESS_EXPENSE_DELETED = "Expense Deleted Successfully";

	public static final String FAILURE_EXPENSE_CREATED = "Error during Expense Creation";
	public static final String FAILURE_EXPENSE_UPDATED = "Error during Expense Updation";
	public static final String FAILURE_EXPENSE_DELETED = "Error during Expense Deletion";
	public static final String SUCCESS_EXPENSE_FETCH = "Expense Fetched Successfully";
	public static final String FAILURE_EXPENSE_FETCH = "Error during Expense Fetch";

	public static final String SUCCESS_EXPENSE_ACTIVATED = "Expense Activated Successfully";
	public static final String SUCCESS_EXPENSE_DEACTIVATED = "Expense DeActivated Successfully";

	public static final String SUCCESS_EXPENSE_EMPLOYEE_FETCH = "Employee Fetched Successfully";
	public static final String FAILURE_EXPENSE_EMPLOYEE_FETCH = "Error During Employee Fetch";

	public static final String SUCCESS_CUSTOMER_VENDOR_FETCH = "Customer Vendor Data Fetched Successfully";
	public static final String FAILURE_CUSTOMER_VENDOR_FETCH = "Error during Customer Vendor Data Fetch";

	public static final String SUCCESS_EXPENSES_ACCOUNT_TYPES_FETCH = "Expense Account Types Fetched Successfully";
	public static final String FAILURE_EXPENSES_ACCOUNT_TYPES_FETCH = "Error During Account Types Fetch";

	// Exchange Rate
	public static final String SUCCESS_EXCHANGE_RATE_DETAILS_FETCH = "Exchange Rate Details Fetched Successfully";
	public static final String FAILURE_EXCHANGE_RATE_FETCH = "Error During Fetch Exchange Rate";
	public static final String INVALID_BASE_CURRENCY_FOR_ORGANIZATION_ID = "Invalid Base currency for Organization";

	// Insta Financial
	public static final String SUCCESS_INSTA_FINANCIALS_DETAILS_FETCH = "Insta Financials Summary Fetched Successfully";
	public static final String FAILURE_INSTA_FINANCIALS_FETCH = "Error During Fetch Insta Financials Summary";
	public static final String INVALID_CIN = "Invalid CIN Number";

	// Aadhar PAN
	public static final String SUCCESS_AADHAR_PAN_FETCH = "PAN Details Fetched Successfully";
	public static final String FAILURE_AADHAR_PAN_FETCH = "Error During Fetch PAN ";
	public static final String INVALID_PAN = "Invalid PAN Number";
	// Aadhar Bank
	public static final String SUCCESS_AADHAR_BANK_FETCH = "Bank Details Fetched Successfully";
	public static final String FAILURE_AADHAR_BANK_FETCH = "Error During Fetch Bank ";
	public static final String INVALID_BANK_ACCOUNT_IFSC = "Invalid Bank Account Number or IFSC Code";

	// Aadhar GSTN
	public static final String SUCCESS_AADHAR_GSTN_FETCH = "GSTN Details Fetched Successfully";
	public static final String FAILURE_AADHAR_GSTN_FETCH = "Error During Fetch GSTN ";
	public static final String INVALID_GSTN = "Invalid GST NUmber";

	public static final String SUCCESS_JOURNAL_ENTRIES_REPORT = "Journal Entries Report Generated Successfully";
	public static final String FAILURE_JOURNAL_ENTRIES_REPORT = "Error During Generating Journal Entries Report";

	// Vendor Portal Vendor Settings
	public static final String SUCCESS_VENDOR_GENERAL_SETTINGS_FETCH = "General Settings Fetched Successfully";
	public static final String FAILURE_VENDOR_GENERAL_SETTINGS_FETCH = "General Settings Fetched UnSuccessfully";
	public static final String SUCCESS_VENDOR_GENERAL_SETTINGS_DEACTIVATED = "General Settings DeActivated Successfully";
	public static final String SUCCESS_VENDOR_GENERAL_SETTINGS_ACTIVATED = "General Settings Activated Successfully";
	public static final String FAILURE_VENDOR_GENERAL_SETTINGS_UPDATE = "Error During Update";
	public static final String SUCCESS_VENDOR_PREDEFINED_SETTINGS_FETCH = "Predefined Settings Fetched Successfully";
	public static final String FAILURE_VENDOR_PREDEFINED_SETTINGS_FETCH = "Predefined Settings Fetched UnSuccessfully";
	public static final String SUCCESS_VENDOR_PREDEFINED_SETTINGS_CREATED = "Predefined Setting Created Successfully";
	public static final String SUCCESS_VENDOR_PREDEFINED_SETTINGS_UPDATED = "Predefined Setting Updated Successfully";
	public static final String SUCCESS_VENDOR_PREDEFINED_SETTINGS_DELETED = "Predefined Setting Deleted Successfully";
	public static final String FAILURE_VENDOR_PREDEFINED_SETTINGS_CREATED = "Error During Predefined Setting Creation";
	public static final String FAILURE_VENDOR_PREDEFINED_SETTINGS_UPDATED = "Error During Predefined Setting Updation";
	public static final String FAILURE_VENDOR_PREDEFINED_SETTINGS_DELETED = "Error During Predefined Setting Deletion";

	public static final String SUCCESS_VENDOR_SETTINGS_CREATED = "Vendor Settings Created Successfully";
	public static final String FAILURE_VENDOR_SETTINGS_CREATED = "Error During Vendor settings Creation";
	public static final String SUCCESS_VENDOR_GROUP_SETTINGS_CREATED = "Vendor Group Settings Created Successfully";
	public static final String FAILURE_VENDOR_GROUP_SETTINGS_CREATED = "Error During Vendor Group settings Creation";
	public static final String SUCCESS_VENDOR_SETTINS_FETCH = "Vendor settings Fetched Successfully";
	public static final String FAILURE_VENDOR_SETTINGS_FETCH = "Error During Vendor Settings Fetch";
	public static final String SUCCESS_VENDOR_GROUP_SETTINGS_FETCH = "Vendor Group settings Fetched Successfully";
	public static final String FAILURE_VENDOR_GROUP_SETTINGS_FETCH = "Error During Vendor Group Settings Fetch";

	public static final String SUCCESS_VENDOR_SETTINGS_ACTIVATED = "Vendor Settings Activated Successfully";
	public static final String SUCCESS_VENDOR_SETTINGS_DEACTIVATED = "Vendor Settings DeActivated Successfully";
	public static final String FAILURE_VENDOR_SETTINGS_UPDATE = "Error During Vendor Settings Update";
	public static final String SUCCESS_VENDOR_GROUP_SETTINGS_ACTIVATED = "Vendor Group Settings Activated Successfully";
	public static final String SUCCESS_VENDOR_GROUP_SETTINGS_DEACTIVATED = "Vendor Group Settings DeActivated Successfully";
	public static final String FAILURE_VENDOR_GROUP_SETTINGS_UPDATE = "Error During Vendor Group Settings update";
	public static final String SUCCESS_VENDOR_SETTINGS_UPDATED = "Vendor Settings Updated Sucessfully";
	public static final String SUCCESS_VENDOR_GROUP_SETTINGS_UPDATED = "Vendor Group Settings Updates Successfully";

	public static final String SUCCESS_REFUND_CREATED = "Refund Created Successfully";
	public static final String SUCCESS_REFUND_UPDATED = "Refund Updated Successfully";
	public static final String SUCCESS_REFUND_DELETED = "Refund Deleted Successfully";
	public static final String FAILURE_REFUND_CREATED = "Error during Refund Creation";
	public static final String FAILURE_REFUND_UPDATED = "Error during Refund Updation";
	public static final String FAILURE_REFUND_DELETED = "Error during Refund Deletion";
	public static final String SUCCESS_REFUND_FETCH = "Refund Fetched Successfully";
	public static final String FAILURE_REFUND_FETCH = "Error during Refund Fetch";
	public static final String SUCCESS_REFUND_ACTIVATED = "Refund Activated Successfully";
	public static final String SUCCESS_REFUND_DEACTIVATED = "Refund DeActivated Successfully";

	public static final String SUCCESS_LETTER_OF_UNDERSTANDING_CREATED = "Letter Of Understanding Created Successfully";
	public static final String SUCCESS_LETTER_OF_UNDERSTANDING_UPDATED = "Letter Of Understanding Updated Successfully";
	public static final String SUCCESS_LETTER_OF_UNDERSTANDING_DELETED = "Letter Of Understanding Deleted Successfully";
	public static final String FAILURE_LETTER_OF_UNDERSTANDING_CREATED = "Error during Letter Of Understanding Creation";
	public static final String FAILURE_LETTER_OF_UNDERSTANDING_UPDATED = "Error during Letter Of Understanding Updation";
	public static final String FAILURE_LETTER_OF_UNDERSTANDING_DELETED = "Error during Letter Of Understanding Deletion";
	public static final String SUCCESS_LETTER_OF_UNDERSTANDING_FETCH = "Letter Of Understanding Fetched Successfully";
	public static final String FAILURE_LETTER_OF_UNDERSTANDING_FETCH = "Error during Letter Of Understanding Fetch";
	public static final String SUCCESS_LETTER_OF_UNDERSTANDING_ACTIVATED = "Letter Of Understanding Activated Successfully";
	public static final String SUCCESS_LETTER_OF_UNDERSTANDING_DEACTIVATED = "Letter Of Understanding DeActivated Successfully";

	public static final String SUCCESS_RECEIPT_CREATED = "Receipt Created Successfully";
	public static final String SUCCESS_RECEIPT_UPDATED = "Receipt Updated Successfully";
	public static final String SUCCESS_RECEIPT_DELETED = "Receipt Deleted Successfully";
	public static final String FAILURE_RECEIPT_CREATED = "Error during Receipt Creation";
	public static final String FAILURE_RECEIPT_UPDATED = "Error during Receipt Updation";
	public static final String FAILURE_RECEIPT_DELETED = "Error during Receipt Deletion";
	public static final String SUCCESS_RECEIPT_FETCH = "Receipt Fetched Successfully";
	public static final String FAILURE_RECEIPT_FETCH = "Error during Receipt Fetch";
	public static final String SUCCESS_RECEIPT_ACTIVATED = "Receipt Activated Successfully";
	public static final String SUCCESS_RECEIPT_DEACTIVATED = "Receipt DeActivated Successfully";
	public static final String SUCCESS_BALANCE_CONFIRMATION_CREATED = "Balance Confirmation Created Successfully";
	public static final String FAILURE_BALANCE_CONFIRMATION_CREATED = "Balance Confirmation Created UnSuccessfully";
	public static final String SUCCESS_BALANCE_CONFIRMATION_FETCH = "Balance Confirmation Fetched Successfully";
	public static final String FAILURE_BALANCE_CONFIRMATION_FETCH = "Balance Confirmation Fetched UnSuccessfully";
	public static final String SUCCESS_BALANCE_CONFIRMATION_UPDATED = "Balance Confirmation Updated Successfully";
	public static final String FAILURE_BALANCE_CONFIRMATION_UPDATED = "Balance Confirmation Updated UnSuccessfully";
	public static final String SUCCESS_BALANCE_CONFIRMATION_ACKNOWLEDGED = "Balance Confirmation Acknowledged Successfully";
	public static final String SUCCESS_BALANCE_CONFIRMATION_DECLINED = "Balance Confirmation Declined Successfully";
	public static final String FAILURE_BALANCE_CONFIRMATION_ACKNOWLEDGED = "Failure During Acknowledging Balance Confirmation";
	public static final String FAILURE_BALANCE_CONFIRMATION_DECLINED = "Failure Duirng Declining Balance Confirmation";
	public static final String SUCCESS_CREDIT_NOTES_CREATED = "Credit Notes Created Successfully";
	public static final String SUCCESS_CREDIT_NOTES_UPDATED = "Credit Notes Updated Successfully";
	public static final String SUCCESS_CREDIT_NOTES_DELETED = "Credit Notes Deleted Successfully";
	public static final String FAILURE_CREDIT_NOTES_CREATED = "Error during Credit Notes Creation";
	public static final String FAILURE_CREDIT_NOTES_UPDATED = "Error during Credit Notes Updation";
	public static final String FAILURE_CREDIT_NOTES_DELETED = "Error during Credit Notes Deletion";
	public static final String SUCCESS_CREDIT_NOTES_FETCH = "Credit Notes Fetched Successfully";
	public static final String FAILURE_CREDIT_NOTES_FETCH = "Error during Credit Notes Fetch";
	public static final String SUCCESS_CREDIT_NOTES_ACTIVATED = "Credit Notes Activated Successfully";
	public static final String SUCCESS_CREDIT_NOTES_VOIDED = "Credit Notes Voided Successfully";
	public static final String SUCCESS_CREDIT_NOTES_DEACTIVATED = "Credit Notes DeActivated Successfully";
	public static final String SUCCESS_BALANCE_CONFIRMATION_WITHDRAW = "Balance Confirmation Withdrawn Successfully";
	public static final String FAILURE_BALANCE_CONFIRMATION_WITHDRAW = "Balance Confirmation Withdrawn UnSuccessfully";

	public static final String SUCCESS_CONTRA_ACCOUNTS_CREATED = "Contra Entry Created Successfully";
	public static final String FAILURE_CONTRA_ACCOUNTS_CREATED = "Contra Entry Created UnSuccessfully";
	public static final String SUCCESS_CONTRA_ACCOUNT_FETCH = "Contra Entries Fetched Successfully";
	public static final String FAILURE_CONTRA_ACCOUNT_FETCH = "Contra Entries Fetched UnSuccessfully";
	public static final String SUCCESS_CONTRA_ACCOUNTS_UPDATED = "Contra Entries Updated Successfully";
	public static final String FAILURE_CONTRA_ACCOUNTS_UPDATED = "Contra Entries Updated UnSuccessfully";

	public static final String NON_CORE_PAYMENT_CREATED_SUCCESSFULLY = "Payment Created Successfully";
	public static final String SUCCESS_RECORDS_COUNT_FETCH = "Count Fetched Successfully";
	public static final String FAILURE_RECORDS_COUNT_FETCH = "Count Fetched UnSuccessfully";
	public static final String SUCCESS_NON_CORE_PAYMENT_FETCH = "Successfully fetched non core payment";
	public static final String FAILURE_PAYMENT_NON_CORE_FETCH = "Failed to fetch non core payment.";

	public static final String SUCCESS_APPLY_CREDITS_CREATED = "Apply Credits Created Successfully";
	public static final String FAILURE_APPLY_CREDITS_CREATED = "Apply Credits Created UnSuccessfully";
	public static final String SUCCESS_APPLY_CREDITS_FETCH = "Apply Credits Fetched Successfully";
	public static final String FAILURE_APPLY_CREDITS_FETCH = "Apply Credits Fetched UnSuccessfully";
	public static final String SUCCESS_APPLY_CREDITS_UPDATED = "Apply Credits Updated Successfully";
	public static final String FAILURE_APPLY_CREDITS_UPDATED = "Apply Credits Updated UnSuccessfully";
	public static final String NON_CORE_PAYMENT_FETCH_SUCCESS = "Non Core Payments fetched successfully";
	public static final String FAILURE_NON_CORE_PAYMENT_FETCH = "Could not fetch non core payments";
	public static final String SUCCESS_NON_CORE_PAYMENT_UPDATED = "Payment Updated Successfully";
	public static final String FAILURE_PAYMENT_NON_CORE_UPDATE = "Failed to update non core payment";
	public static final String FAILURE_DURING_UPDATE = "";

	public static final String SUCCESS_WORKFLOW_SETTINGS_CREATED="Workflow Settings Created Successfully";
	public static final String SUCCESS_WORKFLOW_SETTINGS_UPDATED="Workflow Settings Updated Successfully";
	public static final String SUCCESS_WORKFLOW_SETTINGS_DELETED="Workflow Settings Deleted Successfully";
	public static final String FAILURE_WORKFLOW_SETTINGS_CREATED="Error during Workflow Settings Creation";
	public static final String FAILURE_WORKFLOW_SETTINGS_UPDATED="Error during Workflow Settings Updation";
	public static final String FAILURE_WORKFLOW_SETTINGS_DELETED="Error during Workflow Settings Deletion";
	public static final String SUCCESS_WORKFLOW_SETTINGS_FETCH="Workflow Settings Fetched Successfully";
	public static final String FAILURE_WORKFLOW_SETTINGS_FETCH="Error during Workflow Settings Fetch";
	public static final String SUCCESS_WORKFLOW_SETTINGS_ACTIVATED="Workflow Settings Activated Successfully";
	public static final String SUCCESS_WORKFLOW_SETTINGS_DEACTIVATED="Workflow Settings DeActivated Successfully";

	//For Statutory bodies module

	public static final String SUCCESS_STATUTORY_BODY_CREATED = "Statutory Body Created Successfully";
	public static final String FAILURE_STATUTORY_BODY_CREATED = "Error during Statutory body Creation";
	public static final String SUCCESS_STATUTORY_BODY_FETCH = "Statutory Fetched Successfully";
	public static final String SUCCESS_STATUTORY_BODY_UPDATED = "Statutory Body Updated Successfully";
	public static final String FAILURE_STATUTORY_BODY_UPDATED = "Error during Statutory Body Updation";
	public static final String SUCCESS_STATUTORY_BODY_DELETE = "Statutory Body deleted Successfully";
	public static final String FAILURE_STATUTORY_BODY_DELETE = "Error during Statutory Body deletion";
	public static final String FAILURE_STATUTORY_BODY_FETCH = "Error during Statutory body Fetch";
	public static final String SUCCESS_STATUTORY_BODY_ACTIVATED = "Statutory Body Activated Successfully";
	public static final String SUCCESS_STATUTORY_BODY_DEACTIVATED = "Statutory Body DeActivated Successfully";
	public static final String AR_INVOICE_TEMPLATE_CREATED_SUCCESSFULLY = "FIN-513";
	public static final String SUCCESS_AR_INVOICE_TEMPLATE_CREATED = "AR Invoice Template Created successfully";
	public static final String FAILURE_AR_INVOICE_TEMPLATE_CREATED = "AR Invoice Template creation failed";
	public static final String AR_INVOICE_TEMPLATE_CREATED_UNSUCCESSFULLY = "AR Invoice Template creation failed";
	public static final String SUCCESS_AR_INVOICE_TEMPLATE_FETCH = "AR Invoice Templates fetched successfully";
	public static final String FAILURE_AR_INVOICE_TEMPLATE_FETCH = "AR Invoice Templates fetch unsuccessful";
	public static final String AR_INVOICE_TEMPLATE_UPDATED_SUCCESSFULLY = "AR Invoice Template updated successfully";
	public static final String SUCCESS_AR_INVOICE_TEMPLATE_UPDATED = "AR Invoice Template updated successfully";
	public static final String FAILURE_AR_INVOICE_TEMPLATE_UPDATED = "AR Invoice Template updated failed";
	public static final String AR_INVOICE_TEMPLATE_UPDATED_UNSUCCESSFULLY = "AR Invoice Template updated failed";

	public static final String SUCCESS_AR_INVOICE_ACTIVATED = "Invoice Activated Successfully";
	public static final String FAILURE_AR_INVOICE_DELETED = "Error during Invoice Deletion";


	public static final String SUCCESS_USER_INVITED = "Invitation Successful";
	public static final String FAILURE_USER_INVITED = "Failed to Invite User";
	public static final String SUCCESS_INVITES_FETCH = "Successfully fetched all invites";
	public static final String FAILURE_INVITES_FETCH = "Failed to fetch invites";
	public static final String SUCCESS_WITHDRAW_INVITE = "Successfully withdrawn invite";
	public static final String FAILURE_WITHDRAW_INVITE = "Failed to withdraw invite";
	public static final String SUCCESS_ACTION_TAKEN = "Successfully performed action on invite";
	public static final String FAILURE_ACTION_TAKEN = "Failed to performaction on invite";

	public static final String SUCCESS_WORKFLOW_GENERAL_SETTINGS_CREATED="Workflow General Settings Created Successfully";
	public static final String SUCCESS_WORKFLOW_GENERAL_SETTINGS_UPDATED="Workflow General Settings Updated Successfully";
	public static final String SUCCESS_WORKFLOW_GENERAL_SETTINGS_DELETED="Workflow General Settings Deleted Successfully";
	public static final String FAILURE_WORKFLOW_GENERAL_SETTINGS_CREATED="Error during Workflow General Settings Creation";
	public static final String FAILURE_WORKFLOW_GENERAL_SETTINGS_UPDATED="Error during Workflow General Settings Updation";
	public static final String FAILURE_WORKFLOW_GENERAL_SETTINGS_DELETED="Error during Workflow General Settings Deletion";
	public static final String SUCCESS_WORKFLOW_GENERAL_SETTINGS_FETCH="Workflow General Settings Fetched Successfully";
	public static final String FAILURE_WORKFLOW_GENERAL_SETTINGS_FETCH="Error during Workflow General Settings Fetch";
	public static final String SUCCESS_WORKFLOW_GENERAL_SETTINGS_ACTIVATED="Workflow General Settings Activated Successfully";
	public static final String SUCCESS_WORKFLOW_GENERAL_SETTINGS_DEACTIVATED="Workflow General Settings DeActivated Successfully";
	public static final String FAILURE_PAYMENT_CREATED = "Error duing payment creation";

	public static final String SUCCESS_TRIAL_BALANCE_FETCH = "Trial Balance Report generated successfully";
	public static final String FAILURE_TRIAL_BALANCE_FETCH = "Error during Trial Balance Report Fetch";

	public static final String SUCCESS_BALANCE_SHEET_FETCH = "Balance Sheet Report generated successfully";
	public static final String FAILURE_BALANCE_SHEET_FETCH = "Error during Balance Sheet Report Fetch";

	public static final String SUCCESS_PROFIT_AND_LOSS_FETCH = "Profit and Loss Report generated successfully";
	public static final String FAILURE_PROFIT_AND_LOSS_FETCH = "Error during Profit and Loss Report Fetch";

	public static final String SUCCESS_INVENTORY_MGMT_REPORT_FETCH = "Inventory Management Report generated successfully";
	public static final String FAILURE_INVENTORY_MGMT_REPORT_FETCH = "Error during Inventory Management Report Fetch";

	public static final String SUCCESS_REPORT_PERIOD_FETCH = "Reports Period Fetched successfully";
	public static final String FAILURE_REPORT_PERIOD_FETCH = "Error during Report Fetch ";

	public static final String SUCCESS_PAY_RUN_REPORT_PERIOD_FETCH = "Pay Run Reports Period fetched successfully";
	public static final String FAILURE_PAY_RUN_REPORT_PERIOD_FETCH = "Pay Run Reports Period not fetched ";

	public static final String SUCCESS_REPORT_LEDGER_DETAILS_FETCH = "Report Ledger details retrieval successfully";
	public static final String FAILURE_REPORT_LEDGER_DETAILS_FETCH = "Report Ledger details retrieval failed";

	public static final String SUCCESS_WORKFLOW_APPROVAL_FETCH = "Pending Approvals Feteched successfully";
	public static final String FAILURE_WORKFLOW_APPROVAL_FETCH="Error during Pending Approvals Fetech";
	public static final String SUCCESS_WORKFLOW_APPROVED="Workflow Request Approved  Successfully";
	public static final String SUCCESS_WORKFLOW_REJECTED="Workflow Request Rejected  Successfully";
	public static final String FAILURE_WORKFLOW_APPROVAL_CREATED="Error during Approval Process ";


	public static final String SUCCESS_PAY_PERIOD_CREATED = "Pay Period Created Successfully";
	public static final String SUCCESS_PAY_PERIOD_UPDATED = "Pay Period Updated Successfully";
	public static final String SUCCESS_PAY_PERIOD_DELETED = "Pay Period Deleted Successfully";
	public static final String FAILURE_PAY_PERIOD_CREATED = "Error during Pay Period Creation";
	public static final String FAILURE_PAY_PERIOD_UPDATED = "Error during Pay Period Updation";
	public static final String FAILURE_PAY_PERIOD_DELETED = "Error during Pay Period Deletion";
	public static final String FAILURE_PAY_PERIOD_FETCH = "Error during Pay Period Fetch";
	public static final String SUCCESS_PAY_PERIOD_FETCH = "Pay Period fetched Successfully";
	public static final String SUCCESS_PAY_PERIOD_ACTIVATED = "Pay Period Activated Successfully";
	public static final String SUCCESS_PAY_PERIOD_DEACTIVATED = "Pay Period DeActivated Successfully";
	public static final String TAXILLA_FETCH_SUCCESS = "Taxilla fetched successfully";
	public static final String FAILURE_TAXILLA_FETCH = "Failed to fetch taxilla";
	public static final String TAXILLA_STATUS_FETCH_SUCCESS = "Taxilla Status fetched successfully";
	public static final String SUCCESS_PAY_CYCLE_CREATED = "Created Pay Cycle Successfully";
	public static final String FAILURE_PAY_CYCLE_CREATED = "Failed to create Pay Cycle";
	public static final String SUCCESS_PAY_CYCLE_FETCH = "Fetched Pay Cycles Successfully";
	public static final String FAILURE_PAY_CYCLE_FETCH = "Failed to fetch pay cycles";
	public static final String SUCCESS_PAY_CYCLE_ACTIVATED = "Activated Pay Cycle Successfully";
	public static final String SUCCESS_PAY_CYCLE_DEACTIVATED = "Deactivated Pay Cycle Successfully";
	public static final String FAILURE_PAY_CYCLE_DELETED = "Failed to Activate/Deactivate Pay Cycle";
	public static final String SUCCESS_PAY_CYCLE_UPDATED = "Updated Pay Cycle Successfully";
	public static final String FAILURE_PAY_CYCLE_UPDATED = "Failed to update pay cycle";

	public static final String PERFIOS_FETCH_SUCCESS = "Perfios fetched Successfully";
	public static final String FAILURE_PERFIOS_FETCH = "Error during Perfios Fetch";
	public static final String FAILURE_PERFIOS_FETCH_MESSAGE= "Unable to retrieve details from Perfios. Please try later";


	public static final String CURRENT_ACCOUNT_FETCH_SUCCESS = "Current Accounts fetched Successfully";
	public static final String FAILURE_CURRENT_ACCOUNT_FETCH = "Error during Current Account Fetch";

	public static final String SUCCESS_PAY_RUN_CREATED = "Pay Run Created Successfully";
	public static final String FAILURE_PAY_RUN_CREATED = "Error during Pay Run Creation";
	public static final String SUCCESS_PAY_RUN_UPDATED = "Pay Run Updated Successfully";
	public static final String FAILURE_PAY_RUN_UPDATED = "Error during Pay Run Updation";
	public static final String SUCCESS_PAY_RUN_FETCH = "Pay Run fetched Successfully";
	public static final String FAILURE_PAY_RUN_FETCH = "Error during Pay Run Fetch";
	public static final String SUCCESS_PAY_RUN_DELETED = "Pay Run Deleted Successfully";
	public static final String FAILURE_PAY_RUN_DELETED = "Error during Pay Run Deletion";

	public static final String AR_CREDIT_NOTE_TEMPLATE_CREATED_SUCCESSFULLY = "AR Credit Note Template Created successfully";
	public static final String SUCCESS_AR_CREDIT_NOTE_TEMPLATE_CREATED = "AR Credit Note Template Created successfully";
	public static final String FAILURE_AR_CREDIT_NOTE_TEMPLATE_CREATED = "AR Credit Note Template Created unsuccessfully";
	public static final String AR_CREDIT_NOTE_TEMPLATE_CREATED_UNSUCCESSFULLY = "AR Credit Note Template Created unsuccessfully";
	public static final String SUCCESS_AR_CREDIT_NOTE_TEMPLATE_FETCH = "AR Credit Note Template retrieval is successfully";
	public static final String FAILURE_AR_CREDIT_NOTE_TEMPLATE_FETCH = "AR Credit Note Template retrieval is failed";
	public static final String AR_CREDIT_NOTE_TEMPLATE_UPDATED_SUCCESSFULLY = "AR Credit Note Template updation is successful";
	public static final String SUCCESS_AR_CREDIT_NOTE_TEMPLATE_UPDATED = "AR Credit Note Template updation is successful";
	public static final String FAILURE_CREDIT_NOTE_UPDATED = "AR Credit Note Template updation is failed";
	public static final String CREDIT_NOTE_UPDATED_UNSUCCESSFULLY = "AR Credit Note Template updation is failed";
	public static final String AR_CREDIT_NOTE_TEMPLATE_UPDATED_UNSUCCESSFULLY = "AR Credit Note Template updation is failed";
	public static final String FAILURE_AR_CREDIT_NOTE_TEMPLATE_UPDATED =  "AR Credit Note Template updation is failed";
	public static final String SUCCESS_AR_CREDIT_NOTE_ACTIVATED = "AR Credit Note Template activation is successful";
	public static final String FAILURE_AR_CREDIT_NOTE_DELETED = "AR Credit Note Template activation is unsuccessful";
	public static final String SUCCESS_PAY_RUN_ACTIVATED = "Pay Run Activated Successfully";
	public static final String SUCCESS_PAY_RUN_DEACTIVATED = "Pay Run DeActivated Successfully";
	public static final String SUCCESS_PAY_RUN_VOIDED = "Pay Run Voided Successfully";;
	public static final String PERFIOS_USER_UNREGISTRATION_SUCCESS = "User UnRegistered Successfully";
	public static final String PERFIOS_USER_UNREGISTRATION_FAILURE = "Error during User UnRegistration";
	public static final String PERFIOS_FETCH_METADATA_SUCCESS = "Metadata fetched Successfully";
	public static final String PERFIOS_FETCH_METADATA_FAILURE = "Error during Metadata Fetch";


	/*public static final String SUCCESS_PAY_RUN_SETTINGS_DROPDOWN_FETCH = "PayRun Settings dropdown fetched Successfully";
	public static final String FAILURE_PAY_RUN_TABLE_DROPDOWN_FETCH = "PayRun Table dropdown fetch unsuccessful";*/
	public static final String SUCCESS_PAY_RUN_SETTINGS_UPDATED = "Pay Run settings updated successfully";
	public static final String FAILURE_PAY_RUN_SETTINGS_UPDATED = "Error during Pay Run settings";
	public static final String TAXILLA_B2B_INVOICES_FETCH_SUCCESS = "B2B Invoices fetched successfully";
	public static final String TAXILLA_B2B_INVOICES_FETCH_FAILURE = "Failed to fetch B2B Invoices";

	public static final String SUCCESS_COA_REPORT_FETCH = "Chart Of Accounts Fetched Successfully";
	public static final String FAILURE_COA_REPORT_FETCH = "Failed to Fetch Chart Of Accounts Report";

	//Yes bank Integration
	public static final String RTGS_PAYMENT_MODE = "RTGS";
	public static final String IMPS_PAYMENT_MODE = "IMPS";

	public static final String SUCCESS_YBL_PAYMENT_TRANSFER = "Payment Is Transferred Successfully";
	public static final String ERROR_YBL_PAYMENT_TRANSFER = "Error Occurred While Making Payment Transfer";

	public static final String ERROR_YBL_NEGATIVE_PAYMENT_TRANSFER=	"The Transaction Amount Cannot Be In Negative Value";
	public static final String ERROR_YBL_RTGS_PAYMENT_TRANSFER=	"The Transaction Amount Is Less Than RTGS Minimum Limit";
	public static final String ERROR_YBL_IMPS_PAYMENT_TRANSFER=	"The Transaction Amount Exceeds IMPS Maximum Limit";

	public static final String SUCCESS_YBL_API_BANKING_ENABLE = "API Banking Is Enabled Successfully";
	public static final String ERROR_YBL_API_BANKING_ENABLE = "Error Occurred While Enabling API Banking ";

	public static final String SUCCESS_YBL_API_BANKING_REGISTER = "Request to enable API is registered successfully";
	public static final String ERROR_YBL_API_BANKING_REGISTER = "Error Occurred Request to enable API  ";
	
	public static final String SUCCESS_YBL_API_BANKING_SETTING = "Settings updated successfully";
	public static final String ERROR_YBL_API_BANKING_SETTING = "Error During Settings Updation";

	public static final String SUCCESS_YBL_FUNDS_AVAILABLE = "Customer Available Fund Is Fetched Successfully ";
	public static final String ERROR_YBL_FUNDS_AVAIALBLE = "Error Occurred While Fectching The Available Funds";

	public static final String SUCCESS_YBL_CREATE_BENEFICIARY = "Beneficiary Created Successfully";
	public static final String SUCCESS_YBL_UPDATE_BENEFICIARY = "Beneficiary Updated Successfully";
	public static final String SUCCESS_YBL_DISABLE_BENEFICIARY = "Beneficiary Disabled Successfully";
	public static final String SUCCESS_YBL_GET_BENEFICIARY = "Beneficiary Retrieved Successfully";
	public static final String ERROR_YBL_CREATE_BENEFICIARY = "Error During Beneficiary Creation";
	public static final String ERROR_YBL_NOT_VALID_ACCOUNT_NO = "Account Number And Confirm Account Should Be Same";
	public static final String ERROR_YBL_EDIT_VALID_BENEFICIARY = "Beneficiary Id Cannot Be Empty";
	public static final String ERROR_YBL_EDIT_BENEFICIARY = "Error During Beneficiary Updation";
	public static final String ERROR_YBL_GET_BENEFICIARY = "Error During Beneficiary Fetch";

	public static final String SUCCESS_YBL_NEW_ACCOUNT_SETUP = "New Account Setup Information Saved Successfully";
	public static final String ERROR_YBL_NEW_ACCOUNT_SETUP = "Error Occurred While Saving New Account Setup Information";


	public static final String VALIDATE_YBL_DISABLE_BENEFICIARY = "Beneficiary Details Cannot Be Empty";

	public static final String SUCCESS_YBL_PAYMENT_LIST = "Payment Transaction(s) Are Fetched Successfully";
	public static final String ERROR_YBL_PAYMENT_LIST = "Error Occurred While Fetching The Payment List";
	public static final String ERROR_YBL_NO_PAYMENT_TRANSACTIONS= "No Transaction(s) Are Available For The Selected Account";
	public static final String ERROR_YBL_BENEFICIARY_TYPE = "Pass Valid Beneficiary Type";

	public static final String SUCCESS_YBL_VIEW_TRANSACTIONS = "Transaction(s) Are Fetched Successfully ";
	public static final String ERROR_YBL_VIEW_TRANSACTIONS= "Error Occurred While Fetching The Transactions";
	public static final String ERROR_YBL_NO_VIEW_TRANSACTIONS= "No Transaction(s) Are Available For The Selected Beneficiary";

	public static final String SUCCESS_YBL_PAYMENT_MODE_LIST = "Payment Mode Is Fetched Successfully";
	public static final String ERROR_YBL_PAYMENT_MODE_LIST = "Error Occurred While Fetching The Payment Mode";
	
	public static final String SUCCESS_ACCOUNT_OVERVIEW = "Account Overview Fetched Successfully";
	public static final String FAILURE_ACCOUNT_OVERVIEW = "Error Occurred While Fetching Account Overview";

	public static final String SUCCESS_BANK_STATEMENT = "Bank Statement Fetched Successfully";
	public static final String FAILURE_BANK_STATEMENT = "Error Occurred While Fetching Bank Statement";
	public static final String SUCCESS_BANK_STATEMENT_REFRESH = "Bank Statement Refreshed Successfully";
	public static final String FAILURE_BANK_STATEMENT_REFRESH = "Error Occurred While Refreshing Bank Statement";
	public static final String NO_BANK_STATEMENT = "No Bank Statement is available for the selected period";
	public static final String ATLEAST_ONE_PAYMENT_IS_MANDATORY  = "Please Enter Atleast One Payment Information";

	
	public static final String SUCCESS_SET_DEFAULT_ACCOUNT = "Default Account Updated Successfully";
	public static final String FAILURE_SET_DEFAULT_ACCOUNT = "Failed to update Default Account";	
	
	//Yes bank Integration ends

	 //Know your GST
	 public static final String SUCCESS_KNOW_YOUR_GST_FETCH="GST Details Fetched Successfully";
     public static final String FAILURE_KNOW_YOUR_GST_FETCH="Error During Fetch GST Details";




	public static final String SUCCESS_PROFILE_UPDATED = "Profile Updated Successfully";
	public static final String FAILURE_PROFILE_UPDATED = "Failed to Update Profile";
	public static final String SUCCESS_PROFILE_FETCH = "Profile Fetched Successfully";
	public static final String FAILURE_PROFILE_FETCH = "Failed to Fetch Profile";

	public static final String PF_CHALLAN_FETCH_SUCCESS = "PF Challan Fetched Successfully";
	public static final String FAILURE_PF_CHALLAN_FETCH = "Error During Fetch PF Challan";
	public static final String PF_ECR_FETCH_SUCCESS = "PF Ecr Fetched Successfully";
	public static final String FAILURE_PF_ECR_FETCH = "Error During Fetch PF Ecr";

	public static final String PF_CHALLAN_REFRESH_SUCCESS = "PF Challan Refreshed Successfully";
	public static final String PF_ECR_REFRESH_SUCCESS = "PF ECR Refreshed Successfully";
	public static final String PF_CHALLAN_REFRESH_FAILURE = "Error During PF Challan Refresh";
	public static final String PF_ECR_REFRESH_FAILURE = "Error During PF Ecr Refresh";


	public static final String SUCCESS_SET_DEFAULT_ORG = "Default Organization Updated Successfully";
	public static final String FAILURE_SET_DEFAULT_ORG = "Failed to update Default Organization";
	public static final String SUCCESS_DISCONNECT = "User Disconnected Successfully";
	public static final String FAILURE_DISCONNECT = "Error During User Disconnection";
	public static final String SUCCESS_OTP = "Successfully fetched OTP";
	public static final String FAILURE_OTP = "Failed to fetch OTP";
	public static final String SUCCESS_GST_REPORT = "Successfully fetched GST report";
	public static final String FAILURE_GST_REPORT = "Failed to fetch GST report";
	
	
	public static final String SUCCESS_SEND_OTP = "OTP Sent Successfully";
	public static final String FAILURE_SEND_OTP = "Failed to send OTP";
	
	public static final String SUCCESS_VERIFY_OTP = "OTP Verified Successfully";
	public static final String FAILURE_VERIFY_OTP = "Failed to Verify OTP";
	public static final String SUCCESS_SET_OTP = "Successfully registered OTP";
	public static final String FAILURE_SET_OTP = "Failed to register OTP";
	public static final String SUCCESS_BILLS_PAYABLE = "Successfully fetched Bills Payable";
	public static final String FAILURE_BILLS_PAYABLE = "Error During Bills Payable Fetch";

	public static final String SUCCESS_PAYROLL_PAYABLE = "Successfully fetched Payroll";
	public static final String FAILURE_PAYROLL_PAYABLE = "Error During Payroll Fetch";

	
	public static final String PAN_FOR_GST_CREATED_SUCCESS = "Successfully created GST for given PANNo";
	public static final String FAILED_PAN_FOR_GST_CREATE = "Failed to create GST NO for given PAN";
	public static final String PAYMENT_ADVICE_FETCH_SUCCESS = "Successfully fetched payment advice";
	public static final String PAYMENT_ADVICE_FETCH_FAILURE = "Failed to fetch payment advice";
	
	public static final String SUCCESS_TDS_CREATED = "Tds Created Successfully";
	public static final String FAILURE_TDS_CREATED = "Error during Tds Creation";
	
	public static final String SUCCESS_TDS_ACTIVATED = "Tds Activated Successfully";
	public static final String SUCCESS_TDS_DEACTIVATED = "Tds DeActivated Successfully";
	
	public static final String FAILURE_TDS_DELETED = "Error during Tds Deletion";
	
	public static final String SUCCESS_TDS_UPDATED = "Tds Updated Successfully";
	public static final String FAILURE_TDS_UPDATED = "Error during Tds Updation";

	public static final String SUCCESS_TDS_FETCH = "Tds fetched Successfully";
	public static final String FAILURE_TDS_FETCH = "Error during TDS Fetch";
	
	public static final String SUCCESS_LIST_PAGE_CUSTOMIZATION_FETCH = "List Page Fetched Successfully";
	public static final String FAILURE_LIST_PAGE_CUSTOMIZATION_FETCH = "Error during List Page Fetch";

	
	public static final String SUCCESS_LIST_PAGE_CUSTOMIZATION_UPDATED = "List Page Customization Updated Successfully";
	public static final String FAILURE_LIST_PAGE_CUSTOMIZATION_UPDATED = "Error during List Page Customization Updation";
	
	public static final String SUCCESS_APPLY_FUND_VENDOR_CREDIT_NOTE = "Successfully apply fund for given Vendor Credit";
	public static final String FAILURE_APPLY_FUND_VENDOR_CREDIT_NOTE = "Error during apply fund for given Vendor Credit";

	public static final String SUCCESS_CREATED_VENDOR_CREDIT_NOTE = "Successfully created given Vendor Credit";
	public static final String FAILURE_CREATED_VENDOR_CREDIT_NOTE = "Error during create Vendor Credit";
	
	public static final String SUCCESS_UPDATED_VENDOR_CREDIT_NOTE = "Successfully updated given Vendor Credit";
	public static final String FAILURE_UPDATED_VENDOR_CREDIT_NOTE = "Error during update Vendor Credit";
	
	public static final String SUCCESS_FETCH_VENDOR_CREDIT_NOTE = "Successfully fetched Vendor Credit note";
	public static final String FAILURE_FETCH_VENDOR_CREDIT_NOTE = "Error while fetching Vendor Credit note";
	
}

package com.blackstrawai.inventorymgmt;

public class ProductInventoryConstants {

	
	
	public static final String MODULE_AR_INVOICE = "AR Invoice";
	public static final String MODULE_AP_INVOICE = "AP Invoice";
	public static final String MODULE_PRODUCT = "Products";
	public static final String MODULE_AR_CREDIT_NOTES= "AR CreditNotes";
	public static final String TXN_TYPE_PURCHSE = "Purchase";
	public static final String TXN_TYPE_SALES = "Sales";
	public static final String TXN_TYPE_ADJUSTMENT = "Adjustment";
	public static final String TXN_TYPE_SALES_REVERSAL ="Sales Reversal";
	public static final String INSERT_INTO_PRODUCT_INVENTORY ="insert into inventory_mgmt.PRODUCT_INVENTORY_TRANSACTION(DATE_OF_ENTRY, MODULE_NAME, MODULE_ID, MODULE_PRODUCT_ID, PRODUCT_ID, TRANSACTION_TYPE, CONTACT_NAME, ORDER_REFERENCE, E_WAY_BILL, GRN_NO, PRODUCT_CODE, PRODUCT_CATEGORY, PRODUCT_NAME, DESCRIPTION, REFERENCE_NUMBER, REFERENCE_NUMBER_DATE, QUANTITY, MEASUREMENT, RATE, TOTAL_COST, IGST, CGST, SGST, CESS, TOTAL_VALUE, CURRENCY, EXCHANGE_RATE, TOTAL_VALUE_IN_BCY, LOCATION, GST_NO, STATUS, USER_ID, ORGANIZATION_ID, ROLE_NAME, CREATE_TS) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	public static final String UPDATE_INTO_PRODUCT_INVENTORY ="update inventory_mgmt.PRODUCT_INVENTORY_TRANSACTION SET PRODUCT_ID =?, PRODUCT_CODE=?, PRODUCT_CATEGORY =?, PRODUCT_NAME=?, DESCRIPTION =?,  QUANTITY =?, MEASUREMENT =?, RATE =?, TOTAL_COST =?, IGST =?, CGST =?, SGST =?, CESS =?, TOTAL_VALUE=?, CURRENCY=?, EXCHANGE_RATE =?, TOTAL_VALUE_IN_BCY=?, LOCATION=?, GST_NO =?,  UPDATE_USER_ID =?, UPDATE_ROLE_NAME=? , UPDATE_TS=?  where ORGANIZATION_ID = ? and MODULE_ID =? and MODULE_PRODUCT_ID =? and MODULE_NAME =? ";
	public static final String DELETE_PRODUCT_INVENTORY = "update inventory_mgmt.PRODUCT_INVENTORY_TRANSACTION SET  STATUS =? ,  UPDATE_USER_ID =?, UPDATE_ROLE_NAME=? , UPDATE_TS=?  where ORGANIZATION_ID = ? and MODULE_ID =? and MODULE_PRODUCT_ID =? and MODULE_NAME =?";
	public static final String SELECT_PRODUCT_INVENTORY = "select id from inventory_mgmt.PRODUCT_INVENTORY_TRANSACTION  where ORGANIZATION_ID = ? and MODULE_ID =? and MODULE_PRODUCT_ID =? and MODULE_NAME =? ";

}

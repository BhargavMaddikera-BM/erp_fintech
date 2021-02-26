package com.blackstrawai.ap;

public class VendorCreditConstants {

	private VendorCreditConstants() {
		super();
	}
	
	public static final String MODULE_TYPE_VENDOR_CREDIT="accounts_payables_vendor_credit";

	public static final String UPDATE_VENDOR_CREDIT_STATUS_AND_BALANCE_BY_ID = "update vendor_credit_transaction_details set available_balance= ?, credit_status= ? where vendor_credit_general_information_id=?";

	public static final String FETCH_VENDOR_CREDIT_STATUS_AND_BALANCE_BY_ID = "select available_balance, credit_status from vendor_credit_transaction_details where vendor_credit_general_information_id=?";

	public static final String INSERT_VENDOR_CREDIT_GENERAL_INFO = "Insert into vendor_credit_general_information"
			+ "(organization_id, user_id, role_name, vendor_id, vendor_gst, location_id, organization_gst_no, invoice_id, credit_note_no, credit_note_date, voucher_no, voucher_date, additional_terms, notes, reason, general_ledger_data, akshar_data) "
			+ "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	
	public static final String VENDOR_CREDIT_GENERAL_INFO_BY_ID = "select organization_id, user_id, role_name, vendor_id, vendor_gst, location_id, organization_gst_no, invoice_id, credit_note_no, credit_note_date, voucher_no, voucher_date, additional_terms, notes, reason, general_ledger_data, akshar_data "
			+ "from vendor_credit_general_information where id = ?";

	public static final String UPDATE_VENDOR_CREDIT_GENERAL_INFO = "update vendor_credit_general_information "
			+ "set organization_id = ?, update_user_id=?, update_role_name=?,update_ts=?, vendor_id=?,vendor_gst=?, location_id=?,organization_gst_no=?, invoice_id=?,credit_note_no=?,credit_note_date=?,voucher_no=?,voucher_date=?,additional_terms=? , notes=?,reason=?, general_ledger_data=?,akshar_data=? where id=?";

	public static final String INSERT_VENDOR_CREDIT_ORG_DELIVERY_ADDRESS = "Insert into vendor_credit_org_delivery_address"
			+ "(phone_no, country,address_1,address_2, state, city, zip_code, landmark, gst_no, create_ts, vendor_credit_general_information_id, delivery_billing_address_same) "
			+ "values (?,?,?,?,?,?,?,?,?,?,?,?)";

	public static final String UPDATE_VENDOR_CREDIT_ORG_DELIVERY_ADDRESS = "update vendor_credit_org_delivery_address "
			+ "set phone_no= ?, country=?,address_1=?,address_2=?,state=?,city=?,zip_code=?,landmark=?,gst_no=?,update_ts=?,vendor_credit_general_information_id=?,delivery_billing_address_same=? where id = ?";

	public static final String INSERT_VENDOR_CREDIT_ORG_BILLING_ADDRESS = "Insert into vendor_credit_org_billing_address"
			+ "(phone_no, country,address_1,address_2, state, city, zip_code, landmark, gst_no,create_ts, vendor_credit_general_information_id) "
			+ "values (?,?,?,?,?,?,?,?,?,?,?)";
	
	public static final String GET_VENDOR_CREDIT_ORG_BILLING_ADDRESS = "select "
			+ "id, phone_no, country,address_1,address_2, state, city, zip_code, landmark, gst_no "
			+ "from vendor_credit_org_billing_address where vendor_credit_general_information_id=?";
	

	public static final String GET_VENDOR_CREDIT_ORG_DELIVERY_ADDRESS = "select "
			+ "id, phone_no, country,address_1,address_2, state, city, zip_code, landmark, gst_no, delivery_billing_address_same "
			+ "from vendor_credit_org_delivery_address where vendor_credit_general_information_id=?";
	
	
	public static final String UPDATE_VENDOR_CREDIT_ORG_BILLING_ADDRESS = "update vendor_credit_org_billing_address "
			+ "set phone_no= ?, country=?,address_1=?,address_2=?,state=?,city=?,zip_code=?,landmark=?,gst_no=?,update_ts=?,vendor_credit_general_information_id=? where id = ?";

	public static final String INSERT_VENDOR_CREDIT_TRANSACTION_DETAILS = "Insert into vendor_credit_transaction_details"
			+ "(source_of_supply_id, destination_of_supply_id,item_rates_tax_id, currency_organization_id, exchange_rate,sub_total,discount_value,discount_type,discount_amount,tds_id,tds_value,adjustment,total,vendor_credit_general_information_id,create_ts, is_apply_after_tax) "
			+ "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	
	public static final String GET_VENDOR_CREDIT_TRANSACTION_DETAILS_BY_CREDIT_NOTE_ID = "select "
			+ "source_of_supply_id, destination_of_supply_id,item_rates_tax_id, currency_organization_id, exchange_rate,sub_total,discount_value,discount_type,discount_amount,tds_id,tds_value,adjustment,total,is_apply_after_tax "
			+ "from vendor_credit_transaction_details where vendor_credit_general_information_id = ?";

	public static final String UPDATE_VENDOR_CREDIT_TRANSACTION_DETAILS = "update vendor_credit_transaction_details "
			+ "set source_of_supply_id=?, destination_of_supply_id=?,item_rates_tax_id=?, currency_organization_id=?, exchange_rate=?, sub_total=?, discount_value=?, discount_amount=?, tds_id=?,tds_value=?, adjustment=?, total=?,update_ts=?, is_apply_after_tax=? where vendor_credit_general_information_id=?";

	public static final String INSERT_VENDOR_CREDIT_PRODUCT = "Insert into vendor_credit_note_product"
			+ "(product_id, description, quantity, unit_measure_id,unit_price, tax_rate_id, amount, create_ts, vendor_credit_general_information_id, product_account_id, product_account_name, product_account_level) "
			+ "values (?,?,?,?,?,?,?,?,?,?,?,?)";
	
	public static final String UPDATE_VENDOR_CREDIT_PRODUCT = "update vendor_credit_note_product "
			+ "set product_id=?, description=?, quantity=?, unit_measure_id=?, unit_price=?,tax_rate_id=?,amount=?,update_ts=?, vendor_credit_general_information_id=?, product_account_id = ?, product_account_name= ?, product_account_level = ? where id = ?";
	
	
	public static final String GET_VENDOR_CREDIT_PRODUCT_INFO = "select vcnp.id,vcnp.product_id,pr.name ,pr.hsn ,vcnp.product_account_id,vcnp.quantity ,vcnp.unit_measure_id, um.unit_name ,vcnp.unit_price,vcnp.tax_rate_id, "
			+ " tgo.name ,vcnp.amount,vcnp.status,vcnp.product_account_name,vcnp.product_account_level,vcnp.description ,pr.type "
			+ " from accounts_payable.vendor_credit_note_product vcnp "
			+ " join usermgmt.product_organization pr on vcnp.product_id = pr.id "
			+ " left join finance_common.unit_of_measure um on um.id = vcnp.unit_measure_id "
			+ " left join usermgmt.tax_group_organization tgo on tgo.id = vcnp.tax_rate_id "
			+ " where vcnp.vendor_credit_general_information_id = ? and vcnp.status not in ('DEL') ";
	
	public static final String INSERT_INTO_TAX_DETAILS = "insert into accounts_payable_tax_details (component_name, component_product_id, tax_group_name, tax_name, tax_rate, tax_amount, create_ts) values (?,?,?,?,?,?,?)";
	public static final String GET_TAX_DETAILS = "SELECT id, component_name, component_product_id, tax_group_name, tax_name, tax_rate, tax_amount FROM accounts_payable_tax_details txd where component_product_id =? and component_name = ? and status not in ('DEL')";
	public static final String MODIFY_TAX_DETAILS_STATUS = " update accounts_payable_tax_details set update_ts =? ,status =? where component_name =? and component_product_id =? ";


}

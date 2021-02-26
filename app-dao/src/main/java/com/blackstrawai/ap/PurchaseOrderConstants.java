package com.blackstrawai.ap;

public class PurchaseOrderConstants {
	
	public  static final String MODULE_TYPE_PO ="po";
	
	
	//Queries for purchase_order table
	public static final String INSERT_INTO_PURCHASE_ORDER="insert into purchase_order_general_information(vendor_id, vendor_gst_no, purchase_order_date, purchase_order_no, org_location_id, org_location_gst_no, po_delivery_date, po_reference_no,purchase_order_type_id, org_shipping_preference_id, shipping_method_id, payment_terms_id, notes, terms_conditions,is_billing_address_same, status, create_ts, user_id, organization_id, is_Super_Admin,role_name) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	public static final String CHECK_PO_EXIST_FOR_ORG ="select purchase_order_no from purchase_order_general_information where organization_id = ? and purchase_order_no = ?";
	public static final String GET_PO_NUMBER_FOR_PO_ID ="select purchase_order_no from purchase_order_general_information where id = ?";
	public static final String DELETE_PURCHASE_ORDER="update purchase_order_general_information  set status = ? ,update_ts = ? where id = ?";
	public static final String ACTIVATE_DEACTIVATE_PURCHASE_ORDER="update purchase_order_general_information  set status = ? ,update_ts = ?,update_user_id=? ,update_role_name=? where id = ?";

	public static final String GET_ALL_FILTERED_PURCHASE_ORDER = "select po.id , po.vendor_id,po.purchase_order_date, po.po_delivery_date, po.status, poi.total , po.purchase_order_no ,vo.vendor_display_name,cur.symbol from "+
	"purchase_order_general_information po join vendor_general_information vo on vo.id = po.vendor_id  "+
	"join purchase_order_items_details poi on poi.purchase_order_id = po.id  "+
	"left join usermgmt.currency_organization cur on cur.id = poi.currency_organization_id  "+
    "where  po.organization_id = ? and po.status not in('DEL')";
	public static final String GET_PURCHASE_ORDER_BY_ID="SELECT vendor_id, vendor_gst_no, purchase_order_date, purchase_order_no, org_location_id, org_location_gst_no, po_delivery_date, po_reference_no, purchase_order_type_id, org_shipping_preference_id, shipping_method_id, payment_terms_id, notes, terms_conditions, is_billing_address_same, status, user_id, organization_id, is_Super_Admin FROM purchase_order_general_information where id = ? ";
	public static final String UPDATE_PURCHASE_ORDER="update purchase_order_general_information set vendor_id =? , vendor_gst_no =? , purchase_order_date = ? , purchase_order_no =?, org_location_id =? , org_location_gst_no =?, po_delivery_date =?, po_reference_no=?,purchase_order_type_id=?, org_shipping_preference_id =?, shipping_method_id =?, payment_terms_id =? , notes=? , terms_conditions=? ,is_billing_address_same=? , status=? , update_ts=?, update_user_id=?, organization_id=? , is_Super_Admin=?,update_role_name=? where id = ?";
	
	
	
	
	//Queries for purchase_order_billing_address table
	public static final String INSERT_INTO_PO_BILLING_ADDRESS="insert into purchase_order_billing_address( phone_no, country, address_1, address_2, state, city, zip_code, landmark, create_ts, purchase_order_id,attention) values (?,?,?,?,?,?,?,?,?,?,?)";
	public static final String GET_BILLING_ADDRESS="SELECT phone_no, country, address_1, address_2, state, city, zip_code, landmark,attention FROM purchase_order_billing_address where purchase_order_id = ?";
	public static final String UPDATE_PO_BILLING_ADDRESS="update purchase_order_billing_address set phone_no =?, country =?, address_1 =?, address_2=? , state=? , city=? , zip_code=? , landmark=? , update_ts=? ,attention=? where purchase_order_id = ?";
	
	
	
	

	//Queries for purchase_order_delivery_address  table
	public static final String INSERT_INTO_PO_DELIVERY_ADDRESS="insert into purchase_order_delivery_address(phone_no, country, address_1, address_2, state, city, zip_code, landmark, create_ts, purchase_order_id,attention) values (?,?,?,?,?,?,?,?,?,?,?)";
	public static final String GET_DELIVERY_ADDRESS= "SELECT phone_no, country, address_1, address_2, state, city, zip_code, landmark,attention FROM purchase_order_delivery_address where purchase_order_id = ?";
	public static final String UPDATE_PO_DELIVERY_ADDRESS="update purchase_order_delivery_address set phone_no =?, country =?, address_1 =?, address_2=? , state=? , city=? , zip_code=? , landmark=? , update_ts=? ,attention=? where purchase_order_id = ?";


	
	
	
	
	//Queries for purchase_order_items_details table
	public static final String INSERT_INTO_PO_ITEM_DETAILS="insert into purchase_order_items_details(source_of_supply_id, item_rates_tax_id, currency_organization_id, exchange_rate, sub_total, discount_value, discount_type, discount_amount, is_apply_after_tax, tds_id, tds_amount, adjustment_amount, total, purchase_order_id, create_ts,is_rcm_applicable) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	public static final String GET_PO_ITEMS_DETAILS_BY_PO_ID = "SELECT  source_of_supply_id, item_rates_tax_id, currency_organization_id, exchange_rate, sub_total, discount_value, discount_type, discount_amount, is_apply_after_tax, tds_id, tds_amount, adjustment_amount, total ,is_rcm_applicable FROM purchase_order_items_details where purchase_order_id = ? ";
	public static final String UPDATE_PO_ITEM_DETAILS="update  purchase_order_items_details set source_of_supply_id=?, item_rates_tax_id =?, currency_organization_id =?, exchange_rate=?, sub_total=?, discount_value=?, discount_type=?, discount_amount=?, is_apply_after_tax=?, tds_id=?, tds_amount=?, adjustment_amount=?, total=?, update_ts=? ,is_rcm_applicable=? where purchase_order_id =?";
	
	
	//Queries for purchase_order_product table
	public static final String INSERT_INTO_PO_PRODUCT="insert into purchase_order_product(product_id, product_account_id, product_account_name, product_account_level, quantity, unit_measure_id, unit_price, tax_rate_id, amount, status, create_ts, purchase_order_id,net_amount,base_unit_price,description) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	public static final String DELETE_PO_PRODUCT_FOR_PO_ID="update purchase_order_product  set status = ? ,update_ts = ? where purchase_order_id =? ";
	public static final String GET_ALL_PRODUCTS_BY_PO_ID="SELECT pop.id , pop.product_id,pr.name, pr.hsn, pop.product_account_id ,pop.product_account_name, pop.product_account_level, " + 
													"pop.quantity,pop.unit_measure_id,um.unit_name,pop.unit_price,pop.tax_rate_id,tgo.name,pop.amount , pop.status ,pr.type, " + 
													"pop.net_amount,pop.base_unit_price,pop.description  " + 
													"FROM accounts_payable.purchase_order_product  pop " + 
													"left join finance_common.unit_of_measure um on um.id = pop.unit_measure_id " + 
													"left join usermgmt.product_organization pr on pop.product_id = pr.id  " + 
													"left join usermgmt.tax_group_organization tgo on tgo.id = pop.tax_rate_id  " + 
													"where pop.purchase_order_id = ? and pop.status not in ('DEL')";
	
	public static final String UPDATE_PO_PRODUCT="update purchase_order_product set product_id=? , product_account_id =?, product_account_name=?, product_account_level=?, quantity=?, unit_measure_id=?, unit_price=?, tax_rate_id=?, amount=?, status=?, update_ts=? ,net_amount=? ,base_unit_price=?,description=? where id = ? and  purchase_order_id = ? ";
	public static final String DELETE_PO_PRODUCT="update purchase_order_product  set status = ? ,update_ts = ? where id = ? ";
	
	
	
	
	public static final String GET_MAX_AMOUNT_FOR_ORG = "select CEILING(max(CAST(poi.total as DECIMAL(9,2)))/1000)*1000 from purchase_order_items_details poi join 	purchase_order_general_information po on po.id = poi.purchase_order_id where po.organization_id = ?"; 

	
	
	// Queries for accounts_payable_tax_details table 
	public static final String INSERT_INTO_TAX_DETAILS="insert into accounts_payable_tax_details (component_name, component_product_id, tax_group_name, tax_name, tax_rate, tax_amount, create_ts) values (?,?,?,?,?,?,?)";
	public static final String GET_TAX_DETAILS ="SELECT id, component_name, component_product_id, tax_group_name, tax_name, tax_rate, tax_amount FROM accounts_payable_tax_details txd where component_product_id =? and component_name = ?  and status not in ('DEL')";
	public static final String MODIFY_TAX_DETAILS_STATUS = " update accounts_payable_tax_details set update_ts =? ,status =? where component_name =? and component_product_id =? ";
	
	
	//Vendor Portal Purchase order
	public static final String GET_PURCHASE_ORDER_FOR_VENDOR = "select po.id , po.vendor_id, po.purchase_order_date, po.po_delivery_date, po.status, poi.total , po.purchase_order_no ,org.name org_name from  " + 
															"	purchase_order_general_information po join vendor_general_information vo on vo.id = po.vendor_id   " + 
															"	join purchase_order_items_details poi on poi.purchase_order_id = po.id  " + 
															"   join usermgmt.organization org on org.id = po.organization_id " + 
															"   where  po.vendor_id = ? and po.status not in(?) ";

	public static final String ACCEPT_PO_BY_VENDOR = " update purchase_order_general_information set status = ?  , update_ts = ?,update_user_id=? ,update_role_name=? where id = ? ";

	public static final String DECLINE_PO_BY_VENDOR = "update purchase_order_general_information set status = ?,reason_type_id =? ,reason =?, update_ts = ?,update_user_id=? ,update_role_name=? where id = ? ";
	
	public static final String GET_MAX_AMOUNT_FOR_VENDOR ="select CEILING(max(CAST(poi.total as DECIMAL(9,2)))/1000)*1000 from purchase_order_items_details poi join purchase_order_general_information po on po.id = poi.purchase_order_id where po.vendor_id = ? ";

	public static final String GET_PO_REFERENCE_NO="select id, purchase_order_no from purchase_order_general_information where organization_id=? and vendor_id=? and status=?";
	
	public static final String GET_ALL_PURCHASES_ORG_VENDOR = "SELECT pgi.id , pgi.vendor_id , REPLACE(pgi.purchase_order_no, '~', '/') po_number,pgi.status , pid.total,pid.currency_organization_id  "
			+ "	FROM accounts_payable.purchase_order_general_information pgi  "
			+ "	join accounts_payable.purchase_order_items_details pid on pid.purchase_order_id = pgi.id "
			+ "	 where pgi.organization_id = ? and pgi.vendor_id =? and pgi.status in ('ACT','OPEN','ACPT') ";

}

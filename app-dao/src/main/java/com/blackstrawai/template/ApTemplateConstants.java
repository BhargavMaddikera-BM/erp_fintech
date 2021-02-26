package com.blackstrawai.template;

public class ApTemplateConstants {


	public static final String INSERT_INTO_AP_PURCHASE_ORDER_TEMPLATE = "INSERT INTO accounts_payable.purchase_order_template\r\n" + 
	"(logo_position, header_notes, footer_section_notes, terms_and_contd, bank_acc_info, foot_notes, foot_note_position, is_annex, user_id,   organization_id,  role_name, status, template_name)\r\n" + 
	"VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?,?)";


	public static final String GET_PURCHASE_ORDER_TEMPLATE_INFO = "select logo_position, header_notes, footer_section_notes, terms_and_contd, bank_acc_info, foot_notes, foot_note_position, is_annex, user_id,organization_id,  role_name, status , id , template_name from accounts_payable.purchase_order_template where organization_id = ?  and id=?";
    
	
	public static final String GET_PURCHASE_ORDER_TEMPLATE_INFO_ALL = "select logo_position, header_notes, footer_section_notes, terms_and_contd, bank_acc_info, foot_notes, foot_note_position, is_annex, user_id,organization_id,  role_name, status , id , template_name from accounts_payable.purchase_order_template where organization_id = ? and user_id = ? and role_name=?";

	
	
	public static final String MODIFY_PURCHASE_ORDER_TEMPLATE_INFO_PO = "UPDATE accounts_payable.purchase_order_template SET logo_position=?, header_notes=?, footer_section_notes=?, terms_and_contd=?, bank_acc_info=?, foot_notes=?, foot_note_position=?, is_annex=?,  update_ts=?, organization_id=?,  update_user_id=?, update_role_name=?, status=?,template_name=? WHERE id=?";

	
	public static final String DELETE_AP_PURCHASE_ORDER_TEMPLATE = "update accounts_payable.purchase_order_template set status = ?  where organization_id = ? and user_id = ?";
	

	public static final String DELETE_PURCHASE_ORDER_TEMPLATE_ID = "update accounts_payable.purchase_order_template set status = ?  where organization_id = ? and user_id = ? and id = ?";
	
	
	public static final String DELETE_AP_PURCHASE_ORDER_TEMPLATE_UPD_PURCHASE_ORDER = "update accounts_payable.purchase_order_template set status = ?  where organization_id = ? and user_id = ? and id != ?";

	
	public static final String GET_TEMPLATE_ID_PURCHASE_ORDER = "select id from accounts_payable.purchase_order_template   where organization_id = ? and status=?";
	
	
	public static final String GET_ORG_CONTACT_NO = " select contact_no from usermgmt.organization o where id =? and status = ?";
	
	public static final String GET_PURCHASE_ORDER_TEMPLATE_INFO_ORGANIZATION = "select logo_position, header_notes, footer_section_notes, terms_and_contd, bank_acc_info, foot_notes, foot_note_position, is_annex, user_id,organization_id,  role_name, status , id , template_name from accounts_payable.purchase_order_template where organization_id = ?";


    public static final String GET_TEMPLATE_IDS_FOR_ORG_PURCHASE_ORDER = "select id  from accounts_payable.purchase_order_template it where organization_id = ?";
    

	
	public static final String TEMPLATE_NAME_PURCHASE_ORDER_UNIQUE_CHECK = "select count(1) from accounts_payable.purchase_order_template it where organization_id =? and template_name = ? ";
	
    
	public static final String TEMPLATE_NAME_UNIQUE_CHECK_UPD_PURCHASE_ORDER = "select count(1) from accounts_payable.purchase_order_template it where organization_id =? and template_name = ? and id !=? ";
	
	public static final String TEMPLATE_TYPE_PURCHASE_ORDER = "PurchaseOrder";	
}


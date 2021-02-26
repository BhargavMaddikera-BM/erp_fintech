package com.blackstrawai.template;

public class ArTemplateConstants {


	public static final String INSERT_INTO_AR_INV_TEMPLATE = "INSERT INTO accounts_receivable.invoice_template\r\n" + 
	"(logo_position, header_notes, footer_section_notes, terms_and_contd, bank_acc_info, foot_notes, foot_note_position, is_annex, user_id,   organization_id,  role_name, status, template_name)\r\n" + 
	"VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?,?)";
	
	public static final String INSERT_INTO_AR_CN_TEMPLATE = "INSERT INTO accounts_receivable.credit_note_template\r\n" + 
			"(logo_position, header_notes, footer_section_notes, terms_and_contd, bank_acc_info, foot_notes, foot_note_position, is_annex, user_id,   organization_id,  role_name, status, template_name)\r\n" + 
			"VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)\r\n" + 
			"";
	
	public static final String GET_INVOICE_TEMPLATE_INFO = "select logo_position, header_notes, footer_section_notes, terms_and_contd, bank_acc_info, foot_notes, foot_note_position, is_annex, user_id,organization_id,  role_name, status , id , template_name from accounts_receivable.invoice_template where organization_id = ?  and id=?";
    
	public static final String GET_CREDIT_NOTE_TEMPLATE_INFO = "select logo_position, header_notes, footer_section_notes, terms_and_contd, bank_acc_info, foot_notes, foot_note_position, is_annex, user_id,organization_id,  role_name, status , id , template_name\r\n" + 
			"FROM accounts_receivable.credit_note_template where organization_id = ?  and id=? ";
	
	public static final String GET_INVOICE_TEMPLATE_INFO_ALL = "select logo_position, header_notes, footer_section_notes, terms_and_contd, bank_acc_info, foot_notes, foot_note_position, is_annex, user_id,organization_id,  role_name, status , id , template_name from accounts_receivable.invoice_template where organization_id = ? and user_id = ? and role_name=?";

	public static final String GET_CREDIT_NOTE_TEMPLATE_INFO_ALL = "select logo_position, header_notes, footer_section_notes, terms_and_contd, bank_acc_info, foot_notes, foot_note_position, is_annex, user_id,organization_id,  role_name, status , id , template_name\r\n" + 
			"FROM accounts_receivable.credit_note_template where organization_id = ? and user_id = ? and role_name=?";
	
	public static final String MODIFY_INVOICE_TEMPLATE_INFO_INV = "UPDATE accounts_receivable.invoice_template SET logo_position=?, header_notes=?, footer_section_notes=?, terms_and_contd=?, bank_acc_info=?, foot_notes=?, foot_note_position=?, is_annex=?,  update_ts=?, organization_id=?,  update_user_id=?, update_role_name=?, status=?,template_name=? WHERE id=?";

	public static final String MODIFY_INVOICE_TEMPLATE_INFO_CN = "UPDATE accounts_receivable.credit_note_template SET logo_position=?, header_notes=?, footer_section_notes=?, terms_and_contd=?, bank_acc_info=?, foot_notes=?, foot_note_position=?, is_annex=?,  update_ts=?, organization_id=?,  update_user_id=?, update_role_name=?, status=?,template_name=? WHERE id=?";
	
	public static final String DELETE_AR_INV_TEMPLATE = "update accounts_receivable.invoice_template set status = ?  where organization_id = ? and user_id = ?";
	
	public static final String DELETE_AR_CN_TEMPLATE = "update accounts_receivable.credit_note_template set status = ?  where organization_id = ? and user_id = ?";
	
	public static final String DELETE_INV_TEMPLATE_ID = "update accounts_receivable.invoice_template set status = ?  where organization_id = ? and user_id = ? and id = ?";
	
	public static final String DELETE_CN_TEMPLATE_ID = "update accounts_receivable.credit_note_template set status = ?  where organization_id = ? and user_id = ? and id = ?";
	
	public static final String DELETE_AR_INV_TEMPLATE_UPD_INV = "update accounts_receivable.invoice_template set status = ?  where organization_id = ? and user_id = ? and id != ?";

	public static final String DELETE_AR_INV_TEMPLATE_UPD_CN = "update accounts_receivable.credit_note_template set status = ?  where organization_id = ? and user_id = ? and id != ?";
	
	public static final String GET_TEMPLATE_ID_INV = "select id from accounts_receivable.invoice_template   where organization_id = ? and status=?";
	
	public static final String GET_TEMPLATE_ID_CN = "select id from accounts_receivable.credit_note_template   where organization_id = ? and status=?";
	
	public static final String GET_ORG_CONTACT_NO = " select contact_no from usermgmt.organization o where id =? and status = ?";
	
	public static final String GET_INVOICE_TEMPLATE_INFO_ORGANIZATION = "select logo_position, header_notes, footer_section_notes, terms_and_contd, bank_acc_info, foot_notes, foot_note_position, is_annex, user_id,organization_id,  role_name, status , id , template_name from accounts_receivable.invoice_template where organization_id = ?";

	public static final String GET_DEFAULT_TEMPLATE_INFO_ORGANIZATION = "select logo_position, header_notes, footer_section_notes, terms_and_contd, bank_acc_info, foot_notes, foot_note_position, is_annex, user_id,organization_id,  role_name, status , id , template_name from accounts_receivable.invoice_template where organization_id = ? and status=?";

	public static final String GET_TEMPLATE_IDS_FOR_ORG_INV = "select id  from accounts_receivable.invoice_template it where organization_id = ?";
    
	public static final String GET_TEMPLATE_IDS_FOR_ORG_CN = "select id  from accounts_receivable.credit_note_template it where organization_id = ?";

	
	public static final String TEMPLATE_NAME_INV_UNIQUE_CHECK = "select count(1) from accounts_receivable.invoice_template it where organization_id =? and template_name = ? ";
	
	public static final String TEMPLATE_NAME_CN_UNIQUE_CHECK = "select count(1) from accounts_receivable.credit_note_template it where organization_id =? and template_name = ? ";
    
	public static final String TEMPLATE_NAME_UNIQUE_CHECK_UPD_INV = "select count(1) from accounts_receivable.invoice_template it where organization_id =? and template_name = ? and id !=? ";
	
	public static final String TEMPLATE_NAME_UNIQUE_CHECK_UPD_CN = "select count(1) from accounts_receivable.credit_note_template it where organization_id =? and template_name = ? and id !=? ";
	
	public static final String TEMPLATE_TYPE_CN = "Credit Note";
	
	public static final String TEMPLATE_TYPE_INV = "Invoice";
}


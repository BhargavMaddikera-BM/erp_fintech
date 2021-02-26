package com.blackstrawai.vendorsettings;

public class VendorSettingsConstants {

	// General Settings
	public static final String GET_GENERAL_SETTINGS_FOR_ORG_ID = " SELECT gs.id,gs.general_settings_base_id,bgs.name,gs.is_active,gs.organization_id,gs.user_id,gs.is_Super_Admin FROM usermgmt.general_settings_organization gs "
			+ " join finance_common.base_general_settings bgs on bgs.id = gs.general_settings_base_id where organization_id = ? ";

	public static final String ACTIVATE_OR_DEACTIVATE_GENERAL_SETTINGS = "update general_settings_organization set is_active = ? where id = ?";

	public static final String INSERT_INTO_GENERAL_SETTINGS_ORGANIZATION = "insert into general_settings_organization(general_settings_base_id ,"
			+ "is_active ,user_id ,organization_id ,is_Super_Admin)values(?,?,?,?,?)";

	// Predefined Settings
	public static final String CREATE_PREDEFINED_SETTINGS = "insert into settings_organization(settings_name,is_default,status,user_id,organization_id,is_Super_Admin) values (?,?,?,?,?,?)";
	public static final String CREATE_MODULES_FOR_SETTINGS = "insert into module_settings_organization(module_settings_base_id,settings_id,is_active,status,user_id,organization_id,is_Super_Admin) values (?,?,?,?,?,?,?)";
	public static final String CREATE_TEMPLATES_FOR_SETTINGS = "insert into template_settings_organization(template_settings_base_id,settings_id,is_active,status,user_id,organization_id,is_Super_Admin) values (?,?,?,?,?,?,?)";
	public static final String CREATE_VALIDATION_FOR_SETTINGS = "insert into validation_settings_organization(validation_settings_base_id,settings_id,is_active,status,user_id,organization_id,is_Super_Admin) values (?,?,?,?,?,?,?)";
	public static final String GET_DEFAULT_SETTING_FOR_ORGANIZATION = "select id from settings_organization where organization_id = ? and is_default = ?";
	public static final String GET_PREDEFINED_SETTINGS = "select id,settings_name,is_default,status,user_id,organization_id,is_Super_Admin from settings_organization where id = ? and organization_id =? ";
	public static final String GET_MODULES_FOR_SETTINGS = "select mso.id, mso.module_settings_base_id,bms.name ,mso.is_active from usermgmt.module_settings_organization mso "
			+ " join finance_common.base_module_settings  bms on bms.id = mso.module_settings_base_id  where mso.settings_id = ? ";
	public static final String GET_TEMPLATES_FOR_SETTINGS = "select tso.id, tso.template_settings_base_id,bts.type,bts.name ,tso.is_active from usermgmt.template_settings_organization tso "
			+ "join finance_common.base_template_settings  bts on bts.id = tso.template_settings_base_id where tso.settings_id = ? ";
	public static final String GET_VALIDATION_FOR_SETTINGS = "select vso.id, vso.validation_settings_base_id,bvs.activity,bvs.validation_rule ,vso.is_active from usermgmt.validation_settings_organization vso  "
			+ "join finance_common.base_validation_settings  bvs on bvs.id = vso.validation_settings_base_id  where vso.settings_id = ?";
	public static final String GET_PREDEFINED_SETTINGS_LIST = "select id,settings_name,is_default,status,user_id,organization_id,is_Super_Admin from settings_organization where organization_id = ? and status not in ('INA','DEL')";
	public static final String UPDATE_PREDEFINED_SETTINGS = " update settings_organization set  settings_name=? ,is_default=? ,status=?,user_id=?,organization_id=?,is_Super_Admin=? ,update_ts=? where id = ? ";
	public static final String UPDATE_MODULES_FOR_SETTINGS = "update module_settings_organization set is_active = ? , user_id=?, organization_id=?, is_Super_Admin=? ,update_ts=? where id =? ";
	public static final String UPDATE_TEMPLATE_FOR_SETTINGS = "update template_settings_organization set is_active = ? ,user_id=?, organization_id=?, is_Super_Admin=? ,update_ts=? where id =? ";
	public static final String UPDATE_VALIDATION_FOR_SETTINGS = "update validation_settings_organization set is_active = ? ,user_id=?, organization_id=?, is_Super_Admin=? ,update_ts=? where id =? ";
	public static final String DESELECT_OR_SELECT_DEFAULT_SETTING = "update settings_organization set  is_default =? where organization_id =? and id = ?";
	public static final String DEACTIVATE_PREDEFINED_SETTINGS = "update settings_organization set  status = ? where organization_id =? and id = ?";
	public static final String CHECK_PREDEFINED_SETTINGS = "select * from settings_organization where organization_id=? and settings_name=?";
	public static final String CHECK_PREDEFINED_SETTINGS_FOR_UPDATE = "select * from settings_organization where organization_id=? and settings_name=? and id not in(?)";
	// Vendor And Vendor Group

	public static final String GET_ORGANIZATION_SETTINGS_NAME = "select settings_name from settings_organization where id=?";
	public static final String GET_SETTINGS_ID = "select id from settings_organization where settings_name=?";
	// Drop down
	public static final String GET_SETTINGS_OF_AN_ORGANIZATION = "select id,settings_name from settings_organization where organization_id=? and status not in ('DEL','INA')";
	// public static final String CHECK_VENDOR_EMAIL_EXIST = "select * from
	// key_contacts_registration where organization_id=? and email_id=?";
	public static final String UPDATE_KEY_CONTACTS_REGISTRATION = "update key_contacts_registration set status=? where email_id=?";	
	public static final String INSER_KEY_CONTACTS_REGISTRATION = "insert into key_contacts_registration (email_id,phone_no,password,token,type,organization_id,status,name,access_data)values(?,?,?,?,?,?,?,?,?)";
	public static final String CHECK_KEY_CONTACT_REGISTRATION = "select password from key_contacts_registration where email_id=? and status=?";
	public static final String UPDATE_PASSWORD = "update key_contacts_registration set password=?,status=? where email_id=?";
	public static final String UPDATE_TOKEN_KEY_CONTACTS = "update key_contacts_registration set token=? where email_id=?";

	// submitted records count

}

package com.blackstrawai.externalintegration.compliance;

public class IncomeTaxConstants {

	public static final String CREATE_INCOME_TAX_USER = "INSERT INTO income_tax.user_info (login, password, user_id, role_name, organization_id, remember_me) VALUES (?,?,?,?,?,?)";
	public static final String SELECT_INCOME_TAX_USER = "SELECT id, login, password FROM income_tax.user_info WHERE organization_id=? AND user_id=? AND role_name=? AND login=?";
	public static final String UPDATE_REMEMBER_ME_STATUS = "UPDATE income_tax.user_info SET remember_me=?, update_ts=?, status=?, password=? WHERE id=?";
	
}

package com.blackstrawai.vendorsettings;

public class RecordsSubmittedConstants {
	public static final String BALANCE_CONFIRMATION_QUICK_COUNT = "select count(*)from balance_confirmation where organization_id=? and is_quick=true and status!='WDRW'";
	public static final String BALANCE_CONFIRMATION_DETAILED_COUNT = "select count(*)from balance_confirmation where organization_id=? and is_quick=false and status!='WDRW'";
	public static final String INVOICE_QUICK_COUNT = "select count(*)from invoice_general_information where organization_id=? and is_quick=true and role_name='Vendor'";
	public static final String INVOICE_DETAILED_COUNT = "select count(*)from invoice_general_information where organization_id=? and is_quick=false and role_name='Vendor'";
	public static final String VENDOR_ONBOARDING_QUICK_COUNT = "select count(*)from vendor_general_information where organization_id=? and is_quick=true and status!='DEL'";
	public static final String VENDOR_ONBOARDING_DETAILED_COUNT = "select count(*)from vendor_general_information where organization_id=? and is_quick=false and status!='DEL'";

}

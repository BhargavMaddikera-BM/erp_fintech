package com.blackstrawai.common;

public class CommonConstants {

	public static final String GET_ALL_SUBSCRIPTIONS = "select id,type,start_period,end_period,pre_discount_amount,discount,final_amount,modules from subscription";
	public static final String GET_ALL_APPLICATIONS = "select a.id,a.name,a.description,a.url,a.status from application a";
	public static final String GET_ALL_MODULES_IN_APPLICATION = "select b.id,b.name,b.description,b.url,b.status from module b where b.application_id=?";

	// Status
	public static final String STATUS_AS_DELETE = "DEL";

	public static final String STATUS_AS_OPEN = "OPEN";
	public static final String DISPLAY_STATUS_AS_OPEN = "Open";

	public static final String STATUS_AS_DRAFT = "DRAFT";
	public static final String DISPLAY_STATUS_AS_DRAFT = "Draft";
	
	public static final String STATUS_AS_FAILED = "FAILED";
	public static final String DISPLAY_STATUS_AS_FAILED = "Failed";

	public static final String STATUS_AS_ACTIVE = "ACT";
	public static final String DISPLAY_STATUS_AS_ACTIVE = "Active";

	public static final String STATUS_AS_NEW = "NEW";

	public static final String STATUS_AS_INACTIVE = "INA";
	public static final String DISPLAY_STATUS_AS_INACTIVE = "Inactive";

	public static final String STATUS_AS_ACCEPT = "ACPT";
	public static final String DISPLAY_STATUS_AS_ACCEPT = "Accepted";

	public static final String STATUS_AS_WITHDRAW = "WDRW";
	public static final String DISPLAY_STATUS_AS_WITHDRAW = "Withdraw";

	public static final String STATUS_AS_DECLINE = "DECL";
	public static final String DISPLAY_STATUS_AS_DECLINE = "Declined";

	public static final String STATUS_AS_EXPIRED = "EXP";
	public static final String DISPLAY_STATUS_AS_EXPIRED = "Expired";

	public static final String STATUS_AS_OVERDUE = "OD";
	public static final String DISPLAY_STATUS_AS_OVERDUE = "Overdue";

	public static final String STATUS_AS_PAID = "PD";
	public static final String DISPLAY_STATUS_AS_PAID = "Paid";

	public static final String STATUS_AS_UNPAID = "UNPD";
	public static final String DISPLAY_STATUS_AS_UNPAID = "Unpaid";

	public static final String STATUS_AS_PARTIALLY_PAID = "PARTPD";
	public static final String DISPLAY_STATUS_AS_PARTIALLY_PAID = "Partially Paid";

	public static final String STATUS_AS_ACKNOWLEDGE = "ACK";
	public static final String DISPLAY_STATUS_AS_ACKNOWLEDGE = "Acknowledged";

	public static final String STATUS_AS_AWAITING_CONFIRMATION = "AWCN";
	public static final String DISPLAY_STATUS_AS_AWAITING_CONFIRMATION = "Awaiting Confirmation ";

	public static final String STATUS_AS_PENDING_FOR_APPROVAL = "PFAP";
	public static final String DISPLAY_STATUS_AS_PENDING_FOR_APPROVAL = "Pending For Approval";

	public static final String STATUS_AS_ADJUSTED = "ADJ";
	public static final String DISPLAY_STATUS_AS_ADJUSTED = "Adjusted";
	
	public static final String STATUS_AS_UN_ADJUSTED = "UNADJ";
	public static final String DISPLAY_STATUS_AS_UN_ADJUSTED = "Un Adjusted";

	public static final String STATUS_AS_PARTIALLY_ADJUSTED = "PADJ";
	public static final String DISPLAY_STATUS_AS_PARTIALLY_ADJUSTED = "Partially Adjusted";

	public static final String STATUS_AS_VOID = "VOID";
	public static final String DISPLAY_STATUS_AS_VOID = "Void";

	public static final String STATUS_AS_REJECT = "REJECT";
	public static final String DISPLAY_STATUS_AS_REJECT = "Rejected";
	
	public static final String STATUS_AS_APPROVED = "APR";
	public static final String DISPLAY_STATUS_AS_APPROVED = "Approved";
	
	public static final String STATUS_AS_APPROVAL_DENIED = "APRDEN";
	public static final String DISPLAY_STATUS_AS_APPROVAL_DENIED = "Approval Denied";
	
	// Roles
	public static final String ROLE_SUPER_ADMIN = "Super Admin";
	public static final String ROLE_VENDOR = "Vendor";

	// Records Submitted
	public static final String MODULE_NAME_INVOICE = "Invoice";
	public static final String MODULE_NAME_BALANCE_CONFIRMATION = "Balance Confirmation";
	public static final String MODULE_NAME_VENDOR_ONBOARDING = "Vendor Onboarding";
	public static final String TYPE_NAME_QUICK = "Quick";
	public static final String TYPE_NAME_DETAILED = "Detailed";
	public static final String NULL = "null";
	public static final String STATUS_AS_DISCONNECT = "DSCNT";
	public static final String GST = "GST";
	public static final String CONFLUENCE = "Confluence";
}

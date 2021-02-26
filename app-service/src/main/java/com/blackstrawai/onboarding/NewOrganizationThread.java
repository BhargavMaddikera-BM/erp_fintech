package com.blackstrawai.onboarding;

import java.io.File;
import java.io.FileReader;
import java.util.List;
import java.util.Properties;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.ApplicationRuntimeException;
import com.blackstrawai.attachments.AttachmentService;
import com.blackstrawai.attachments.AttachmentsConstants;
import com.blackstrawai.attachments.UploadFileVo;

public class NewOrganizationThread extends Thread{
	
	private OrganizationDao organizationDao;
	private int organizationId;
	private String userId;
	private AttachmentService attachmentService;
	private List<UploadFileVo>attachments;
	
	public NewOrganizationThread(AttachmentService attachmentService,OrganizationDao organizationDao,int organizationId, String userId,List<UploadFileVo>attachments){
		this.organizationDao=organizationDao;
		this.organizationId=organizationId;
		this.userId=userId;
		this.attachmentService=attachmentService;
		this.attachments=attachments;
	}
	
	public void run(){
		try {
			organizationDao.createSettingsAndPreferences(organizationId,userId);
			createFolder();
		} catch (ApplicationException e) {
			// TODO Auto-generated catch block
			throw new ApplicationRuntimeException(e);
		}
	}
	
	private void createFolder(){
		
		FileReader reader;
		try {
			reader = new FileReader("/decifer/config/app_config.properties");
			Properties p=new Properties();  
			p.load(reader);  
			String path=p.getProperty("attachment_path");
			File file =null;
			path=path+"/"+organizationId;
			file = new File(path);
			file.mkdir();			
		    file = new File(path+"/"+"customer");
		    file.mkdir();
		    file = new File(path+"/"+"vendor");
		    file.mkdir();
		    file = new File(path+"/"+"employee");
		    file.mkdir();
		    file = new File(path+"/"+"po");
		    file.mkdir();
		    file = new File(path+"/"+"invoices_bills");
		    file.mkdir();
		    file = new File(path+"/"+"invoices_without_bills");
		    file.mkdir();
		    file = new File(path+"/"+"expenses");
		    file.mkdir();
		    file = new File(path+"/"+"employee_reimbursements");
		    file.mkdir();
		    file = new File(path+"/"+"organization_documents");
		    file.mkdir();
		    file = new File(path+"/"+"bank_documents");
		    file.mkdir();
		    file = new File(path+"/"+"chart_of_accounts");
		    file.mkdir();
		    file = new File(path+"/"+"expenses_vendor");
		    file.mkdir();
		    file = new File(path+"/"+"others_reimbursements");
		    file.mkdir();
		    file = new File(path+"/"+"accounting_aspects");
		    file.mkdir();
		    file = new File(path+"/"+"digital_invoices");
		    file.mkdir();
		    file = new File(path+"/"+"other_documents");
		    file.mkdir();
		    file = new File(path+"/"+"accounts_receivables_lut");
		    file.mkdir();
		    file = new File(path+"/"+"payroll");
		    file.mkdir();
		    file = new File(path+"/"+"vendor_portal_balance_confirmation");
		    file.mkdir();
		    file = new File(path+"/"+"non_erp");
		    file.mkdir();
		    file = new File(path+"/"+"bank_contra");
		    file.mkdir();
		    file = new File(path+"/"+"ap_payments");
		    file.mkdir();
		    file = new File(path+"/"+"accounts_receivables_receipt");
		    file.mkdir();
		    file = new File(path+"/"+"accounts_receivables_refund");
		    file.mkdir();
		    file = new File(path+"/"+"accounts_receivables_credit_note");
		    file.mkdir();
		    file = new File(path+"/"+"accounts_receivables_invoice");
		    file.mkdir();
		    file = new File(path+"/"+"accounts_receivables_apply_credit");
		    file.mkdir();
		    file = new File(path+"/"+"accounts_receivables_invoice_logo");
		    file.mkdir();
		    file = new File(path+"/"+"accounts_receivables_invoice_sign");
		    file.mkdir();
		    file = new File(path+"/"+"accounts_receivables_credit_note_logo");
		    file.mkdir();
		    file = new File(path+"/"+"accounts_receivables_credit_note_sign");
		    file.mkdir();
		    file = new File(path+"/"+"statutory_body");
		    file.mkdir();
		    file = new File(path+"/"+"gst");
		    file.mkdir();
		    file = new File(path+"/"+"income_tax");
		    file.mkdir();
		    file = new File(path+"/"+"api_banking");
		    file.mkdir();
		    file = new File(path+"/"+"virtual_bank");
		    file.mkdir();
		    file = new File(path+"/"+"product");
		    file.mkdir();
		    file = new File(path+"/"+"income_tax/it_return");
		    file.mkdir();
		    file = new File(path+"/"+"income_tax/26as");
		    file.mkdir();		    
		    file = new File(path+"/"+"accounts_receivables_receipt_logo");
		    file.mkdir();
		    file = new File(path+"/"+"accounts_receivables_receipt_sign");
		    file.mkdir();
		    file = new File(path+"/"+"accounts_receivables_refund_logo");
		    file.mkdir();
		    file = new File(path+"/"+"accounts_receivables_refund_sign");
		    file.mkdir();		    
		    file = new File(path+"/"+"accounts_payables_invoice_logo");
		    file.mkdir();
		    file = new File(path+"/"+"accounts_payables_invoice_sign");
		    file.mkdir();
		    file = new File(path+"/"+"accounts_payables_credit_note_logo");
		    file.mkdir();
		    file = new File(path+"/"+"accounts_payables_credit_note_sign");
		    file.mkdir();

		    file = new File(path+"/"+"accounts_payables_po_logo");
		    file.mkdir();
		    file = new File(path+"/"+"accounts_payables_po_sign");
		    file.mkdir();
		    
		    file = new File(path+"/"+"payroll_payrun");
		    file.mkdir();		    
		    file = new File(path+"/"+"employee_provident_fund");
		    file.mkdir();	    
		    file = new File(path+"/"+"employee_provident_fund/challan");
		    file.mkdir();	    
		    file = new File(path+"/"+"employee_provident_fund/ecr");
		    file.mkdir();		    
		    file = new File(path+"/"+"vendor_credit");
		    file.mkdir();	   
		    
		    attachmentService.upload(AttachmentsConstants.MODULE_TYPE_ORG_DOCUMENTS, organizationId,  organizationId, attachments);
		    
		    
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw new ApplicationRuntimeException(e);
		}        
		
	}

}

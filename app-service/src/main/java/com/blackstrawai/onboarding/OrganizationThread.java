package com.blackstrawai.onboarding;

import java.io.File;
import java.io.FileReader;
import java.util.Properties;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.ApplicationRuntimeException;

public class OrganizationThread extends Thread{
	
	private OrganizationDao organizationDao;
	private int organizationId;
	private String userId;
	
	public OrganizationThread(OrganizationDao organizationDao,int organizationId, String userId){
		this.organizationDao=organizationDao;
		this.organizationId=organizationId;
		this.userId=userId;
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

		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw new ApplicationRuntimeException(e);
		}        
		
	}

}

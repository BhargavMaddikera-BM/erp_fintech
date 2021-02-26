package com.blackstrawai.journals;

import org.springframework.beans.factory.annotation.Autowired;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.ApplicationRuntimeException;

public class JournalEntriesUploadThread extends Thread{

	@Autowired
	private JournalEntriesTransactionDao journalEntriesTransactionDao;
	
	private Integer transactionId;
	
	private Integer orgId;
	
	private String subModule;
	
	public JournalEntriesUploadThread(Integer transactionId,Integer orgId,String subModule) {
		this.transactionId = transactionId;
		this.orgId = orgId;
		this.subModule = subModule;
	}
	
	public void run(){
		try {
			journalEntriesTransactionDao.insetInvoiceJournalEntries(orgId,transactionId,subModule);
		} catch (ApplicationException e) {
			// TODO Auto-generated catch block
			throw new ApplicationRuntimeException(e);
		}
	}
	
}

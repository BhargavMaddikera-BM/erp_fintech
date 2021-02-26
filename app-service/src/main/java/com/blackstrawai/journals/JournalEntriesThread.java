package com.blackstrawai.journals;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.ApplicationRuntimeException;

public class JournalEntriesThread extends Thread{

	private JournalEntriesTransactionDao journalEntriesTransactionDao;
	
	private Integer transactionId;
	
	private Integer orgId;
	
	private String subModule;
	
	public JournalEntriesThread(JournalEntriesTransactionDao journalEntriesTransactionDao, Integer transactionId,Integer orgId,String subModule) {
		this.journalEntriesTransactionDao = journalEntriesTransactionDao;
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

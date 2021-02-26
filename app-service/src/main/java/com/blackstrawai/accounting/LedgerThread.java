package com.blackstrawai.accounting;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.ApplicationRuntimeException;
import com.blackstrawai.accounting.ledger.LedgerVo;
import com.blackstrawai.chartofaccounts.ChartOfAccountsLedgerDao;

public class LedgerThread extends Thread{

	private ChartOfAccountsLedgerDao accountsLedgerDao;
	
	private LedgerVo ledgerVo;
	
	public LedgerThread(ChartOfAccountsLedgerDao accountsLedgerDao, LedgerVo ledgerVo) {
		this.accountsLedgerDao = accountsLedgerDao;
		this.ledgerVo = ledgerVo;
	}
	
	public void run(){
		try {
			accountsLedgerDao.createSubLedger(ledgerVo);
		} catch (ApplicationException e) {
			// TODO Auto-generated catch block
			throw new ApplicationRuntimeException(e);
		}
	}
	
	
}

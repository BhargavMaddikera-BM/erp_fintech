package com.blackstrawai.ar;

import com.blackstrawai.ApplicationRuntimeException;

public class ArBalanceUpdateThread extends Thread {

	ArBalanceUpdateDao balanceUpdateDao;

	private Integer primaryId;

	private String module;

	public ArBalanceUpdateThread(ArBalanceUpdateDao balanceUpdateDao, Integer primaryId, String module) {
		this.balanceUpdateDao = balanceUpdateDao;
		this.primaryId = primaryId;
		this.module = module; 
	}

	public void run() {

		try {
			if (module != null  && primaryId!=null) {
				if(module.equalsIgnoreCase(ArInvoiceConstants.MODULE_TYPE_AR_INVOICES)) {
				balanceUpdateDao.updateInvoiceDueBalance(primaryId);
				}else if(module.equalsIgnoreCase(ArInvoiceConstants.MODULE_TYPE_AR_CREDITNOTE)) {
					balanceUpdateDao.updateCreditNotesDueBalance(primaryId);
				}else if(module.equalsIgnoreCase(ArInvoiceConstants.MODULE_TYPE_AR_RECEIPT)) {
					balanceUpdateDao.updateReceiptDueBalance(primaryId);
				}
			}
		} catch (Exception e) {

			throw new ApplicationRuntimeException(e);
		}

	}

}
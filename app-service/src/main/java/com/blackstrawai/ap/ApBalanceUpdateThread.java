package com.blackstrawai.ap;

import com.blackstrawai.ApplicationRuntimeException;
import com.blackstrawai.journals.JournalEntriesConstants;

public class ApBalanceUpdateThread extends Thread {

	ApBalanceUpdateDao balanceUpdateDao;

	private Integer primaryId;

	private String module;

	public ApBalanceUpdateThread(ApBalanceUpdateDao balanceUpdateDao, Integer primaryId, String module) {
		this.balanceUpdateDao = balanceUpdateDao;
		this.primaryId = primaryId;
		this.module = module; 
	}

	public void run() {

		try {
			if (module != null  && primaryId!=null) {
				switch (module) {
				case JournalEntriesConstants.SUB_MODULE_INVOICE_WITH_BILLS:
					balanceUpdateDao.updateInvoiceDueBalance(primaryId);	
					break;
				case PaymentNonCoreConstants.VENDOR_ADVANCE:
					balanceUpdateDao.updateVendorAdvanceBalance(primaryId);
					break;
				case JournalEntriesConstants.SUB_MODULE_PAY_RUN:
					balanceUpdateDao.updatePayRunBalance(primaryId);
					break;
				case JournalEntriesConstants.SUB_MODULE_CREDIT_NOTES:
					balanceUpdateDao.updateCreditNoteBalance(primaryId);
					break;
				}
			}
		} catch (Exception e) {

			throw new ApplicationRuntimeException(e);
		}

	}

}
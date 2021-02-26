package com.blackstrawai.payroll;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.ap.ApBalanceUpdateDao;
import com.blackstrawai.ap.ApBalanceUpdateThread;
import com.blackstrawai.ap.PaymentNonCoreConstants;
import com.blackstrawai.ap.dropdowns.BasicVoucherEntriesVo;
import com.blackstrawai.attachments.AttachmentService;
import com.blackstrawai.attachments.AttachmentsConstants;
import com.blackstrawai.common.BaseService;
import com.blackstrawai.common.BaseVo;
import com.blackstrawai.common.CommonConstants;
import com.blackstrawai.common.DropDownDao;
import com.blackstrawai.journals.JournalEntriesConstants;
import com.blackstrawai.journals.JournalEntriesThread;
import com.blackstrawai.journals.JournalEntriesTransactionDao;
import com.blackstrawai.payroll.dropdowns.PayRunEmployeeDropDownVo;
import com.blackstrawai.payroll.dropdowns.PayRunPaymentCycleDropDownVo;
import com.blackstrawai.payroll.dropdowns.PayRunPaymentTypeDropDownVo;
import com.blackstrawai.payroll.dropdowns.PayRunPeriodDropDownVo;
import com.blackstrawai.payroll.dropdowns.PayRunTableDropDownVo;
import com.blackstrawai.payroll.payrun.PayRunImportVo;
import com.blackstrawai.payroll.payrun.PayRunTableVo;
import com.blackstrawai.payroll.payrun.PayRunVo;
import com.blackstrawai.upload.UploadHelperService;


@Service
public class PayRunService extends BaseService {
	private Logger logger = Logger.getLogger(PayRunService.class);

	@Autowired
	private PayRunDao payRunDao;
	
	@Autowired
	private AttachmentService attachmentService;
	
	@Autowired
	private DropDownDao dropDownDao;
	
	@Autowired
	private UploadHelperService uploadHelperService;
	
	@Autowired
	ApBalanceUpdateDao apBalanceUpdateDao;
	
	@Autowired
	private JournalEntriesTransactionDao journalEntriesTransactionDao;

	public PayRunVo createPayRun(PayRunVo payRunVo) throws Exception {
		logger.info("Entry into createPayRun" + payRunVo);
		try {
			logger.info("createPayRun - service ");
			payRunVo = payRunDao.createPayRun(payRunVo);			
			if(payRunVo.getPayRunId() !=null &&  payRunVo.getAttachments() != null ) {
				logger.info("Entry into payrun attachments import");
				attachmentService.upload(AttachmentsConstants.MODULE_TYPE_PAYROLL_PAYRUN, payRunVo.getPayRunId(), payRunVo.getOrgId(), payRunVo.getAttachments());
					
				logger.info("Upload Successfull");
			}
			
			logger.info("Before thread");
			if(payRunVo.getPayRunId()!=null  && (CommonConstants.STATUS_AS_ACTIVE.equals(payRunVo.getStatus()) || CommonConstants.STATUS_AS_OPEN.equals(payRunVo.getStatus()) )) {
				logger.info("To thread");
				JournalEntriesThread journalThread =new JournalEntriesThread(journalEntriesTransactionDao, payRunVo.getPayRunId(), payRunVo.getOrgId(),JournalEntriesConstants.SUB_MODULE_PAY_RUN);
				journalThread.start();
			}
						
		logger.info("Pay Run created Successfully in service layer ");
	} catch (Exception e) {
		logger.info("Error in Pay Run creation in service layer ");
		throw e;
	}
		return payRunVo;
	}

	public PayRunVo getPayRunDataById(Integer organizationId, String userId, String roleName, Integer payRunid) throws ApplicationException {
		PayRunVo payRunVo = payRunDao.getPayRunDataById( payRunid);
		if(payRunVo.getPayRunId() !=null && !(payRunVo.getPayRunInformation() == null) && payRunVo.getAttachments() != null) {
			logger.info("Entry into payrun attachments retrieve");			
			 attachmentService.encodeAllFiles(payRunVo.getOrgId(), payRunVo.getPayRunId()   ,AttachmentsConstants.MODULE_TYPE_PAYROLL_PAYRUN, payRunVo.getAttachments());	
			logger.info("attachments retrieval Successfull");
		}
		return payRunVo;
	}

	public List<PayRunVo> getPayRunAll(int organizationId, String userId, String roleName) throws ApplicationException, SQLException {
		
		 List<PayRunVo> payRunVos =payRunDao.getAllPayRun(organizationId, userId,  roleName);
		
		 payRunVos.forEach(info -> {
			
			 if(info.getPayRunId() !=null && !(info.getPayRunInformation() == null))  {
					logger.info("Entry into payrun attachments retrieve " + info.getPayRunId());			
					attachmentService.encodeAllFiles(info.getOrgId(), info.getPayRunId()   ,AttachmentsConstants.MODULE_TYPE_PAYROLL_PAYRUN, info.getAttachments());	
					logger.info("attachments retrieval");
				}
			 
		 });
		 
		return payRunVos;
	}

	public PayRunVo updatePayRun(PayRunVo payRunVo) throws Exception {
		logger.info("Entry into createPayRun" + payRunVo);
		try {
			payRunVo = payRunDao.updatePayRun(payRunVo);
			logger.info("createPayRun - service ");
			if(payRunVo.getPayRunId() !=null && !(payRunVo.getPayRunInformation() == null) && payRunVo.getAttachments() != null) {
				logger.info("Entry into payrun attachments update");
				attachmentService.upload(AttachmentsConstants.MODULE_TYPE_PAYROLL_PAYRUN, payRunVo.getPayRunId(), payRunVo.getOrgId(), payRunVo.getAttachments());
					
				logger.info("Upload update Successfull");
			}
			
			
			// To add entry in journal entry table 
			 if(payRunVo.getPayRunId()!=null && (CommonConstants.STATUS_AS_ACTIVE.equals(payRunVo.getStatus()) || CommonConstants.STATUS_AS_OPEN.equals(payRunVo.getStatus())) ) {
					logger.info("To thread");
					JournalEntriesThread journalThread =new JournalEntriesThread(journalEntriesTransactionDao, payRunVo.getPayRunId(), payRunVo.getOrgId(),JournalEntriesConstants.SUB_MODULE_PAY_RUN);
					journalThread.start();
				}
			 if(payRunVo!=null && payRunVo.getPayRunId()!=null) {
			 new ApBalanceUpdateThread(apBalanceUpdateDao, payRunVo.getPayRunId(), PaymentNonCoreConstants.EMPLOYEE_PAYMENTS).start();
			 }
						
		logger.info("Pay Run created Successfully in service layer ");
	} catch (Exception e) {
		logger.info("Error in Pay Run creation in service layer ");
		throw e;
	}
		return payRunVo;
		
	}

	public PayRunPeriodDropDownVo getReportsPeriodDropDownData( Integer orgId) throws ApplicationException {
		// TODO Auto-generated method stub
		return dropDownDao.getPayRunReportsPeriod(orgId);
	}

	public PayRunPaymentTypeDropDownVo getPaymentTypeDropDownData(int orgId) throws ApplicationException {
		// TODO Auto-generated method stub
		return dropDownDao.getPayRunPaymentType(orgId);
	}

	public PayRunPaymentCycleDropDownVo getPaymentCycleDropDownData(int organizationId) throws ApplicationException {
		// TODO Auto-generated method stub
		return dropDownDao.getPayRunCycleType(organizationId);
	}

	public PayRunEmployeeDropDownVo getPayRunEmployeeDropDownData(int organizationId) throws ApplicationException {
		// TODO Auto-generated method stub
		return dropDownDao.getPayRunEmployees(organizationId);
	}

	public BasicVoucherEntriesVo getPayRunReferenceNumberSpec(int organizationId) throws ApplicationException {
		// TODO Auto-generated method stub
		return dropDownDao.getPayRunReferenceNumberSpec(organizationId);
	}

	public PayRunVo activateDeActivatePayRun(int organizationId, String organizationName, String userId, int id,
			String roleName, String status) throws ApplicationException {
		
		return payRunDao.activateDeActivatePayRun( organizationId,  organizationName,  userId,  id,
				 roleName,  status);
	}

	public PayRunTableDropDownVo getPayRunTableDropDownData(Integer organizationId) throws ApplicationException {
		// TODO Auto-generated method stub
		return dropDownDao.getPayRunTableDropDownData(organizationId);
	}

	public List<PayRunTableVo> updatePayRunTableSettings(List<PayRunTableVo> payRunTableVos) throws Exception {
		logger.info("Entry into updatePayRunTableSettings" + payRunTableVos);
		try {
			logger.info("updatePayRunTableSettings - service ");
			payRunTableVos = payRunDao.updatePayRunTableSettings(payRunTableVos);			
									
		logger.info("Pay Run Table Settings updated Successfully in service layer ");
	} catch (Exception e) {
		logger.info("Error on pay Run Table Settings updatein service layer ");
		throw e;
	}
		return payRunTableVos;
	}

	public BaseVo importPreviewPayRunData(PayRunImportVo payRunImportVo) throws InvalidFormatException, ParseException, ApplicationException {
		BaseVo previewImportFile = null;
		if(payRunImportVo.getOrgId() != null && payRunImportVo.getUserId() != null && 
				payRunImportVo.getRoleName() != null && payRunImportVo.getPayRunAttachment() != null ) {
			logger.info("Entry into payrun attachments import " + payRunImportVo);
			
			previewImportFile = uploadHelperService.previewImportFile(payRunImportVo.getPayRunAttachment());
			
			logger.info("Upload Successfull " + previewImportFile.toString());
		}
		
		return previewImportFile;
	}

	public PayRunVo saveImportPayRun(PayRunVo data) throws ApplicationException {
		// TODO Auto-generated method stub
		PayRunVo payRunVo = payRunDao.createImportPayRun(data);
		return payRunVo;
		
	}

	public List<PayRunVo> getActivePayRunAll(int organizationId, String userId, String roleName) throws ApplicationException {
		 List<PayRunVo> payRunVos =payRunDao.getACtiveAllPayRun(organizationId, userId,  roleName);
			
		 payRunVos.forEach(info -> {
			
			 if(info.getPayRunId() !=null && !(info.getPayRunInformation() == null))  {
					logger.info("Entry into payrun attachments retrieve " + info.getPayRunId());			
					attachmentService.encodeAllFiles(info.getOrgId(), info.getPayRunId()   ,AttachmentsConstants.MODULE_TYPE_PAYROLL_PAYRUN, info.getAttachments());	
					logger.info("attachments retrieval");
				}
			 
		 });
		 
		return payRunVos;
	}
		
	

	

}

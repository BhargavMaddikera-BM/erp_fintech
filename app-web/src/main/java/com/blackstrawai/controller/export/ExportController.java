package com.blackstrawai.controller.export;

import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.blackstrawai.accounting.AccountingAspectsService;
import com.blackstrawai.banking.ContraService;
import com.blackstrawai.common.BaseController;
import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.common.Constants;
import com.blackstrawai.common.EntityConverter;
import com.blackstrawai.export.AccountingEntryExportVo;
import com.blackstrawai.export.ContraExportVo;
import com.blackstrawai.export.CustomerExportVo;
import com.blackstrawai.export.EmployeeExportVo;
import com.blackstrawai.export.ExportService;
import com.blackstrawai.export.ExportVo;
import com.blackstrawai.export.VendorExportVo;
import com.blackstrawai.externalintegration.banking.common.BankStatementService;
import com.blackstrawai.externalintegration.yesbank.statement.BankStatementInfoRequest;
import com.blackstrawai.helper.ExportConvertToVoHelper;
import com.blackstrawai.keycontact.CustomerService;
import com.blackstrawai.keycontact.EmployeeService;
import com.blackstrawai.keycontact.VendorService;
import com.blackstrawai.report.ReportsService;
import com.blackstrawai.request.JSONObject;
import com.blackstrawai.request.export.ExportRequest;
import com.blackstrawai.response.export.ExportResponse;

@RestController
@CrossOrigin
@RequestMapping("/decifer/export")
public class ExportController extends BaseController {

	@Autowired
	ExportService exportService;

	@Autowired
	VendorService vendorService;

	@Autowired
	EmployeeService employeeService;

	@Autowired
	CustomerService customerService;
	
	@Autowired
	AccountingAspectsService accountingEntryService;
	
	@Autowired
	ContraService contraService;
	
	@Autowired
	ReportsService reportsService;
	
	@Autowired
	EntityConverter entityConverter;
	
	@Autowired
	BankStatementService bankStatementService;


	
	private Logger logger = Logger.getLogger(ExportController.class);

	@RequestMapping(value = "/v1/exports", method = RequestMethod.POST)
	public ResponseEntity<BaseResponse> export(HttpServletRequest httpRequest, HttpServletResponse httpResponse,
			@RequestBody JSONObject<ExportRequest> exportRequest) {
		logger.info("Entry into method: export");
		BaseResponse response = new ExportResponse();
		String encodedString;
		try {
			logger.info("Request Payload:" + generateRequestAndResponseLogPaylod(exportRequest));
			ExportVo exportVo = ExportConvertToVoHelper.getInstance()
					.convertExportVoFromExportRequest(exportRequest.getData());
			if (exportRequest.getData().getModuleName().equalsIgnoreCase("vendor")) {
				List<VendorExportVo> data=vendorService.getListVendorsById(exportVo);
				encodedString=exportService.exportVendor(data, exportRequest.getData().getModuleName(),exportRequest.getData().getFileType());
				 ((ExportResponse) response).setData(encodedString);
			}
			if (exportRequest.getData().getModuleName().equalsIgnoreCase("employee")) {
				List<EmployeeExportVo> data = employeeService.getListEmployeesById(exportVo);
				 encodedString=exportService.exportEmployee(data, exportRequest.getData().getModuleName(),exportRequest.getData().getFileType());
				((ExportResponse) response).setData(encodedString);
			}
			if (exportRequest.getData().getModuleName().equalsIgnoreCase("customer")) {
				List<CustomerExportVo> data =customerService.getListCustomersById(exportVo);
				encodedString=exportService.exportCustomer(data, exportRequest.getData().getModuleName(),exportRequest.getData().getFileType());
				((ExportResponse) response).setData(encodedString);	
			}
			if (exportRequest.getData().getModuleName().equalsIgnoreCase("accounting entries")) {
				List<AccountingEntryExportVo> data =accountingEntryService.getListAccountingEntriesById(exportVo);
				encodedString=exportService.exportAccountingEntry(data, exportRequest.getData().getModuleName(),exportRequest.getData().getFileType());
				((ExportResponse) response).setData(encodedString);
			}
			if (exportRequest.getData().getModuleName().equalsIgnoreCase("contra")) {
				List<ContraExportVo> data =contraService.getListContraEntriesById(exportVo);
				encodedString=exportService.exportContraEntry(data, exportRequest.getData().getModuleName(),exportRequest.getData().getFileType());
				((ExportResponse) response).setData(encodedString);
			}
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_EXPORT_PROCESSED,
					Constants.SUCCESS_EXPORT_PROCESSED, Constants.EXPORT_PROCESSED_SUCCESSFULLY);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error Payload:" + e.getMessage());
			if (e.getCause() != null) {
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_EXPORT_PROCESSED,
						e.getCause().getMessage(), Constants.EXPORT_PROCESSED_UNSUCCESSFULLY);
			} else {
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_EXPORT_PROCESSED,
						e.getMessage(), Constants.EXPORT_PROCESSED_UNSUCCESSFULLY);
			}

			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	@RequestMapping(value = "/v1/exports/coa/{organizationId}", method = RequestMethod.POST)
	public ResponseEntity<BaseResponse> getDownloadableCoaReportForOrganization(
			HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse,@PathVariable int organizationId) {
		BaseResponse response = new ExportResponse();
		String encodedString;
		
		try {  
			encodedString=reportsService.getCoaDownloadableReportForOrganization(organizationId);
			((ExportResponse) response).setData(encodedString);
			setTokens(response, httpServletRequest.getHeader("keyToken"), httpServletRequest.getHeader("valueToken"));
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_COA_REPORT_FETCH,
					Constants.SUCCESS_COA_REPORT_FETCH, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error Payload:" + e.getMessage());
			if (e.getCause() != null) {
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_COA_REPORT_FETCH,
						e.getCause().getMessage(), Constants.FAILURE_DURING_GET);
			} else {
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_COA_REPORT_FETCH,
						e.getMessage(), Constants.FAILURE_DURING_GET);
			}

			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	 @RequestMapping(value = "/v1/exports/adhoc_statement", method = RequestMethod.POST)
	  public ResponseEntity<BaseResponse> exportAdhocStatement(
	      HttpServletRequest httpRequest,
	      @RequestBody JSONObject<BankStatementInfoRequest> yesBankStatementRequest) {
	    HttpStatus status = HttpStatus.OK;
	    BaseResponse response = new ExportResponse();
	    String encodedString ; 
	    try {

	      setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));

	    logger.info("statmentList::"+yesBankStatementRequest.getData().getStatement());
	      if (Objects.nonNull(yesBankStatementRequest) && Objects.nonNull(yesBankStatementRequest.getData()) && Objects.nonNull(yesBankStatementRequest.getData().getStatement()) &&  !yesBankStatementRequest.getData().getStatement().isEmpty()) {
	    	  encodedString=bankStatementService.exportBankingBankStatement(yesBankStatementRequest.getData().getStatement(),"xls");
				 ((ExportResponse) response).setData(encodedString);
				 response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_EXPORT_PROCESSED,
							Constants.SUCCESS_EXPORT_PROCESSED, Constants.EXPORT_PROCESSED_SUCCESSFULLY);
					logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
	        }

	    } catch (Exception ex) {
	      logger.error("Exception in exporting bank statement" + ex);
	      response =
	          constructResponse(
	              response,
	              Constants.FAILURE,
	              ex.getMessage() != null ? ex.getMessage() : Constants.FAILURE_EXPORT_PROCESSED,
	              Constants.FAILURE_EXPORT_PROCESSED,
	              Constants.EXPORT_PROCESSED_UNSUCCESSFULLY);
	      status = org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
	    }
	    return new ResponseEntity<>(response, status);
	  }

}

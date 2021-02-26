package com.blackstrawai.controller.upload;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

import com.blackstrawai.ApplicationException;
import com.blackstrawai.accounting.AccountingAspectsService;
import com.blackstrawai.ap.dropdowns.BasicVoucherEntriesVo;
import com.blackstrawai.banking.ContraService;
import com.blackstrawai.common.BaseController;
import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.common.BaseVo;
import com.blackstrawai.common.Constants;
import com.blackstrawai.helper.ConvertToResponseHelper;
import com.blackstrawai.helper.UploadConvertToVoHelper;
import com.blackstrawai.keycontact.CustomerService;
import com.blackstrawai.keycontact.EmployeeService;
import com.blackstrawai.keycontact.VendorService;
import com.blackstrawai.keycontact.customer.CustomerVo;
import com.blackstrawai.keycontact.employee.EmployeeVo;
import com.blackstrawai.keycontact.vendor.VendorVo;
import com.blackstrawai.payroll.PayItemService;
import com.blackstrawai.payroll.PayRunService;
import com.blackstrawai.payroll.payrun.PayRunVo;
import com.blackstrawai.request.JSONObject;
import com.blackstrawai.request.payroll.PayrollUploadRequest;
import com.blackstrawai.request.upload.AccountingEntriesUploadRequest;
import com.blackstrawai.request.upload.BulkPaymentUploadRequest;
import com.blackstrawai.request.upload.ContraUploadFileRequest;
import com.blackstrawai.request.upload.CreateImportFileRequest;
import com.blackstrawai.request.upload.CustomerUploadRequest;
import com.blackstrawai.request.upload.EmployeeUploadRequest;
import com.blackstrawai.request.upload.ImportFileRequest;
import com.blackstrawai.request.upload.VendorUploadRequest;
import com.blackstrawai.response.upload.ImportFileResponse;
import com.blackstrawai.response.upload.ProcessImportFileResponse;
import com.blackstrawai.response.upload.TemplateFileResponse;
import com.blackstrawai.response.upload.UploadDropDownResponse;
import com.blackstrawai.upload.AccountingEntriesUploadVo;
import com.blackstrawai.upload.ContraUploadVo;
import com.blackstrawai.upload.CustomerUploadVo;
import com.blackstrawai.upload.EmployeeUploadVo;
import com.blackstrawai.upload.PayrollUploadVo;
import com.blackstrawai.upload.UploadFileVo;
import com.blackstrawai.upload.UploadPayrollVo;
import com.blackstrawai.upload.UploadService;
import com.blackstrawai.upload.VendorUploadVo;
import com.blackstrawai.upload.dropdowns.ModuleTypeDropDownVo;
import com.blackstrawai.upload.externalintegration.yesbank.BulkPaymentsUploadVo;

@RestController
@CrossOrigin
@RequestMapping("/decifer/upload")
public class UploadController extends BaseController {

	@Autowired
	UploadService uploadService;

	@Autowired
	VendorService vendorService;

	@Autowired
	private CustomerService customerService;

	@Autowired
	EmployeeService employeeService;

	@Autowired
	AccountingAspectsService accountingAspectsService;

	@Autowired
	PayItemService payItemService;

	@Autowired
	ContraService contraService;
	
	@Autowired
	private PayRunService  payRunService;

	private Logger logger = Logger.getLogger(UploadController.class);

	// For creating Import Drop Downs
	@RequestMapping(value = "/v1/dropsdowns/uploads")
	public ResponseEntity<BaseResponse> getUploadDropDownData(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) {
		logger.info("Entry into getUploadDropDownData");
		BaseResponse response = new UploadDropDownResponse();
		try {
			ModuleTypeDropDownVo data = uploadService.getUploadDropDownData();
			setTokens(response, httpServletRequest.getHeader("keyToken"), httpServletRequest.getHeader("valueToken"));
			((UploadDropDownResponse) response).setData(data);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_DROP_DOWN_FETCH,
					Constants.SUCCESS_DROP_DOWN_FETCH, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);

		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_DROP_DOWN_FETCH, e.getMessage(),
					Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	// For Getting template file
	@RequestMapping(value = "/v1/template/{moduleName}/{type}")
	public ResponseEntity<BaseResponse> fetchTemplate(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, @PathVariable String moduleName, @PathVariable String type) {
		logger.info("Entry into fetchTemplate");
		BaseResponse response = new TemplateFileResponse();
		try {
			String data = uploadService.getTemplateFile(moduleName, type);
			setTokens(response, httpServletRequest.getHeader("keyToken"), httpServletRequest.getHeader("valueToken"));
			((TemplateFileResponse) response).setData(data);
			response = constructResponse(response, Constants.SUCCESS, Constants.TEMPLATE_SUCCESS_FILE_FETCH,
					Constants.TEMPLATE_SUCCESS_FILE_FETCH, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);

		} catch (Exception e) {
			logger.error("Error Payload:" + e.getMessage());
			if (e.getCause() != null) {
				response = constructResponse(response, Constants.FAILURE, Constants.TEMPLATE_FAILURE_FILE_FETCH,
						e.getCause().getMessage(), Constants.FAILURE_DURING_GET);
			} else {
				response = constructResponse(response, Constants.FAILURE, Constants.TEMPLATE_FAILURE_FILE_FETCH,
						e.getMessage(), Constants.FAILURE_DURING_GET);
			}
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	// For preview file
	@RequestMapping(value = "/v1/preview", method = RequestMethod.POST)
	public ResponseEntity<BaseResponse> previewImport(HttpServletRequest httpRequest, HttpServletResponse httpResponse,
			@RequestBody JSONObject<ImportFileRequest> importFileRequest) {
		logger.info("Entry into method: previewImport");
		BaseResponse response = new ImportFileResponse();
		try {
			logger.info("Request Payload:" + generateRequestAndResponseLogPaylod(importFileRequest));
			UploadFileVo importFileVo = UploadConvertToVoHelper.getInstance()
					.convertImportFileVoFromImportFileRequest(importFileRequest.getData());
			BaseVo data = uploadService.previewImportFile(importFileVo);
			((ImportFileResponse) response).setData(data);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_IMPORT_PREVIEWED,
					Constants.SUCCESS_IMPORT_PREVIEWED, Constants.IMPORT_PREVIEWED_SUCCESSFULLY);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error Payload:" + e.getMessage());
			if (e.getCause() != null) {
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_IMPORT_PREVIEWED,
						e.getCause().getMessage(), Constants.IMPORT_PREVIEWED_UNSUCCESSFULLY);
			} else {
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_IMPORT_PREVIEWED,
						e.getMessage(), Constants.IMPORT_PREVIEWED_UNSUCCESSFULLY);
			}

			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// For Create Import file
	@RequestMapping(value = "/v1/process", method = RequestMethod.POST)
	public ResponseEntity<BaseResponse> processImport(HttpServletRequest httpRequest, HttpServletResponse httpResponse,
			@RequestBody JSONObject<CreateImportFileRequest> createImportFileRequest) {
		logger.info("Entry into method: processImport");
		List<VendorUploadVo> vendors = new ArrayList<VendorUploadVo>();
		List<EmployeeUploadVo> employees = new ArrayList<EmployeeUploadVo>();
		List<CustomerUploadVo> customers = new ArrayList<CustomerUploadVo>();
		List<AccountingEntriesUploadVo> accoutingEntries = new ArrayList<AccountingEntriesUploadVo>();
		List<ContraUploadVo> contraList = new ArrayList<ContraUploadVo>();
		List<BulkPaymentsUploadVo> bulkpayment = new ArrayList<BulkPaymentsUploadVo>();
		VendorUploadVo vendoruploadVo = new VendorUploadVo();
		EmployeeUploadVo employeeUploadVo = new EmployeeUploadVo();
		CustomerUploadVo customerUploadVo = new CustomerUploadVo();
		BaseResponse response = new ProcessImportFileResponse();
		Map<String, String> processStatus = null;
		try {
			logger.info("Request Payload:" + generateRequestAndResponseLogPaylod(createImportFileRequest));
			CreateImportFileRequest data = createImportFileRequest.getData();
			logger.info("data - "+data);
			if (createImportFileRequest.getData().getModuleName().equalsIgnoreCase("vendor")) {
				List<VendorUploadRequest> vendorRequest = data.getVendor();
				for (VendorUploadRequest vendor : vendorRequest) {
					VendorVo vendorVo = UploadConvertToVoHelper.getInstance()
							.convertVendorImportFileVoFromVendorImportFileRequest(vendor, data.getOrgId(),
									data.getUserId(), data.isSuperAdmin(), data.getRoleName());
					processStatus = vendorService.processUpload(vendorVo, vendor.getBillingAddressCountry(),
							vendor.getBillingAddressState(), vendor.getOriginAddressState(),
							vendor.getOriginAddressCountry(), vendor.getCurrency(), vendor.getPaymentTerms(),
							vendor.getTds(), data.isDuplicacy());
					vendoruploadVo = UploadConvertToVoHelper.getInstance()
							.convertVendorUploadVoFromVendorUploadRequest(vendor);
					if (processStatus.get("Success") != null
							&& processStatus.get("Success").equalsIgnoreCase("Success")) {
						vendoruploadVo.setResponseStatus("Success");
						vendoruploadVo.setResponseMessage(processStatus.get("Success").toString());
					} else {
						vendoruploadVo.setResponseStatus("Failure");
						vendoruploadVo.setResponseMessage(processStatus.get("Failure").toString());
					}
					vendors.add(vendoruploadVo);
				}
				((ProcessImportFileResponse) response).setVendor(vendors);
			}
			if (createImportFileRequest.getData().getModuleName().equalsIgnoreCase("employee")) {
				List<EmployeeUploadRequest> employeeRequest = data.getEmployee();
				for (EmployeeUploadRequest employee : employeeRequest) {
					EmployeeVo employeeVo = UploadConvertToVoHelper.getInstance()
							.convertEmployeeImportFileVoFromEmployeeImportFileRequest(employee, data.getOrgId(),
									data.getUserId(), data.isSuperAdmin(), data.getRoleName());
					processStatus = employeeService.processUpload(employeeVo, employee.getDepartmentName(),
							employee.getEmployeeType(), data.isDuplicacy());
					employeeUploadVo = UploadConvertToVoHelper.getInstance()
							.convertEmployeeUploadFromEmployeeUloadRequest(employee);
					if (processStatus.get("Success") != null
							&& processStatus.get("Success").equalsIgnoreCase("Success")) {
						employeeUploadVo.setResponseStatus("Success");
						employeeUploadVo.setResponseMessage(processStatus.get("Success").toString());
					} else {
						employeeUploadVo.setResponseStatus("Failure");
						employeeUploadVo.setResponseMessage(processStatus.get("Failure").toString());
					}
					employees.add(employeeUploadVo);
				}
				((ProcessImportFileResponse) response).setEmployee(employees);
			}
			if (createImportFileRequest.getData().getModuleName().equalsIgnoreCase("customer")) {
				List<CustomerUploadRequest> customerRequest = data.getCustomer();
				for (CustomerUploadRequest customer : customerRequest) {
					CustomerVo customerVo = UploadConvertToVoHelper.getInstance()
							.convertCustomerImportFileVoFromCustomerImportFileRequest(customer, data.getOrgId(),
									data.getUserId(), data.isSuperAdmin(), data.getRoleName());
					processStatus = customerService.processUpload(customerVo, customer.getCurrency(),
							customer.getBillingAddressState(), customer.getBillingAddressCountry(),
							customer.getDeliveryAddressState(), customer.getDeliveryAddressCountry(),
							data.isDuplicacy());
					customerUploadVo = UploadConvertToVoHelper.getInstance()
							.convertCustomerUploadVoFromCustomerUploadRequest(customer);
					if (processStatus.get("Success") != null
							&& processStatus.get("Success").equalsIgnoreCase("Success")) {
						customerUploadVo.setResponseStatus("Success");
						customerUploadVo.setResponseMessage(processStatus.get("Success").toString());
					} else {
						customerUploadVo.setResponseStatus("Failure");
						customerUploadVo.setResponseMessage(processStatus.get("Failure"));
					}
					customers.add(customerUploadVo);
				}
				((ProcessImportFileResponse) response).setCustomer(customers);
			}
			if (createImportFileRequest.getData().getModuleName().equalsIgnoreCase("accounting entries")) {
				List<AccountingEntriesUploadRequest> accountingEntriesRequest = data.getAccountingEntries();

				Map<String, List<AccountingEntriesUploadVo>> accountingEntriesMap = UploadConvertToVoHelper.getInstance()
						.convertAccountingEntryImportFileVoFromAccountingEntryImportFileRequest(
								accountingEntriesRequest);

				for (Map.Entry<String, List<AccountingEntriesUploadVo>> entry : accountingEntriesMap.entrySet()) {
					String journalNo = entry.getKey();
					List<AccountingEntriesUploadVo> accountingEntriesList = entry.getValue();
					processStatus = accountingAspectsService.processUpload(accountingEntriesList, data.getOrgId(),
							data.getUserId(), data.isSuperAdmin(), data.isDuplicacy(), data.getRoleName());
					for (AccountingEntriesUploadVo accountingEntry : accountingEntriesList) {
						if (journalNo.equalsIgnoreCase(accountingEntry.getJournalNo())) {
							if (processStatus.get("Success") != null
									&& processStatus.get("Success").equalsIgnoreCase("Success")) {
								accountingEntry.setResponseStatus("Success");
								accountingEntry.setResponseMessage(processStatus.get("Success").toString());
							} else {
								accountingEntry.setResponseStatus("Failure");
								accountingEntry.setResponseMessage(processStatus.get("Failure"));
							}
						}
						accoutingEntries.add(accountingEntry);
					}

				}
				((ProcessImportFileResponse) response).setAccountingEntries(accoutingEntries);
			}
			if (createImportFileRequest.getData().getModuleName().equalsIgnoreCase("contra")) {
				List<ContraUploadFileRequest> contraRequestList = data.getContra();

				Map<String, List<ContraUploadVo>> contraMap = UploadConvertToVoHelper.getInstance()
						.convertContraImportFileVoFromContraImportFileRequest(contraRequestList);

				for (Map.Entry<String, List<ContraUploadVo>> entry : contraMap.entrySet()) {
					String journalNo = entry.getKey();
					List<ContraUploadVo> contraEntriesList = entry.getValue();
					processStatus = contraService.processUpload(contraEntriesList, data.getOrgId(), data.getUserId(),
							data.isSuperAdmin(), data.isDuplicacy(), data.getRoleName());
					for (ContraUploadVo contraEntry : contraEntriesList) {
						if (journalNo.equalsIgnoreCase(contraEntry.getReferenceNo())) {
							if (processStatus.get("Success") != null
									&& processStatus.get("Success").equalsIgnoreCase("Success")) {
								contraEntry.setResponseStatus("Success");
								contraEntry.setResponseMessage(processStatus.get("Success").toString());
							} else {
								contraEntry.setResponseStatus("Failure");
								contraEntry.setResponseMessage(processStatus.get("Failure").toString());
							}
						}
						contraList.add(contraEntry);
					}

				}
				((ProcessImportFileResponse) response).setContra(contraList);
			}

			if (createImportFileRequest.getData().getModuleName().equalsIgnoreCase("payroll")) {
				List<PayrollUploadRequest> payrollRequest = data.getPayRunInformation();
				List<UploadPayrollVo> dataVo = UploadConvertToVoHelper.getInstance().convertUploadPayrollVoFromPayrollRequest(
						payrollRequest, data.getOrgId(), data.getUserId(), data.getRoleName());
				List<PayrollUploadVo> payRunData = UploadConvertToVoHelper.getInstance().convertPayrollUploadVoFromPayrollRequest(payrollRequest);
				PayRunVo payRunVo = new PayRunVo();
				 payRunVo.setStatus(createImportFileRequest.getData().getStatus());
				BasicVoucherEntriesVo payRunReferenceNumber = payRunService.getPayRunReferenceNumberSpec(data.getOrgId());
				payRunVo.setPayRunRefPrefix(payRunReferenceNumber.getPrefix());
				payRunVo.setPayRunRefSuffix(payRunReferenceNumber.getSuffix());
				logger.info("payrollRequest "+payrollRequest );
				logger.info("Controller - payRunReferenceNumber "+payRunReferenceNumber);
				payRunVo.setPayRunInformation(payRunData);
				payRunVo.setOrgId(data.getOrgId());
				payRunVo.setUserId(data.getUserId());
				payRunVo.setRoleName(data.getRoleName());
				logger.info("PayRunVo "+ payRunVo);
     			payRunService.saveImportPayRun(payRunVo);
		
				processStatus = payItemService.processUpload(data.getRoleName(), 
															 data.getUserId(), 
															 data.getOrgId(),
															 dataVo);
//				processStatus = "success";
				logger.info("processStatus "+processStatus);
				List<PayrollUploadVo> resultDataVo = ConvertToResponseHelper.getInstance()
						.convertPayrollUploadVoFromPayrollRequest(payrollRequest, processStatus);
				logger.info("resultDataVo "+resultDataVo.toString());
				
				((ProcessImportFileResponse) response).setPayRunInformation(resultDataVo);
				
			}
			
			if (createImportFileRequest.getData().getModuleName().equalsIgnoreCase("bulk_payments_yes_bank")) {
				List<BulkPaymentUploadRequest> bulkpaymentRequestList = data.getBulkPayment();

				 List<BulkPaymentsUploadVo> bulkpaymentList = UploadConvertToVoHelper.getInstance()
						.convertBulkpaymentImportFileVoFromBulkpaymentImportFileRequest(bulkpaymentRequestList);

					/*
					 * for (Map.Entry<String, List<ContraUploadVo>> entry : contraMap.entrySet()) {
					 * String journalNo = entry.getKey(); List<ContraUploadVo> contraEntriesList =
					 * entry.getValue(); processStatus =
					 * contraService.processUpload(contraEntriesList, data.getOrgId(),
					 * data.getUserId(), data.isSuperAdmin(), data.isDuplicacy(),
					 * data.getRoleName()); for (ContraUploadVo contraEntry : contraEntriesList) {
					 * if (journalNo.equalsIgnoreCase(contraEntry.getReferenceNo())) { if
					 * (processStatus.get("Success") != null &&
					 * processStatus.get("Success").equalsIgnoreCase("Success")) {
					 * contraEntry.setResponseStatus("Success");
					 * contraEntry.setResponseMessage(processStatus.get("Success").toString()); }
					 * else { contraEntry.setResponseStatus("Failure");
					 * contraEntry.setResponseMessage(processStatus.get("Failure").toString()); } }
					 * contraList.add(contraEntry); }
					 * 
					 * }
					 */
				((ProcessImportFileResponse) response).setContra(contraList);
			}
			
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_IMPORT_PROCESSED,
					Constants.SUCCESS_IMPORT_PROCESSED, Constants.IMPORT_PROCESSED_SUCCESSFULLY);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error Payload:" + e.getMessage());
			if (e.getCause() != null) {
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_IMPORT_PROCESSED,
						e.getCause().getMessage(), Constants.IMPORT_PROCESSED_UNSUCCESSFULLY);
			} else {
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_IMPORT_PROCESSED,
						e.getMessage(), Constants.IMPORT_PROCESSED_UNSUCCESSFULLY);
			}
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}

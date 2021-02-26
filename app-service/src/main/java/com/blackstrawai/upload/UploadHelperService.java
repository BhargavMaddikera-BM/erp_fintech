package com.blackstrawai.upload;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Service;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.ApplicationRuntimeException;
import com.blackstrawai.common.BaseVo;
import com.blackstrawai.upload.externalintegration.yesbank.BulkPaymentsUploadVo;
import com.blackstrawai.upload.externalintegration.yesbank.ListBulkPaymentsUploadVo;

@Service
public class UploadHelperService {

	private Logger logger = Logger.getLogger(UploadHelperService.class);
	private CSVParser parser;

	// For Import and Export
	public String getTemplateFile(String moduleName, String type)
			throws ApplicationRuntimeException, FileNotFoundException {
		logger.info("Inside method getTemplateFile");
		FileReader reader;
		String encodedString = new String();
		try {
			if (moduleName.endsWith("entries") || moduleName.endsWith("Entries")) {
				moduleName = "accounting_entries";
			}
			reader = new FileReader("/decifer/config/app_config.properties");
			Properties p = new Properties();
			p.load(reader);
			StringBuilder path = new StringBuilder(p.getProperty("upload_template_path"));
			logger.info(path.toString() + "is path retrieved from properties");
			File f = new File(path.toString());
			logger.info(path.toString() + " to navigate ");
			if (f.exists() && f.isDirectory() && type != null && moduleName != null) {
				String uploadedpath = path.toString() + "/" + moduleName.toLowerCase() + "." + type;
				logger.info(uploadedpath + " is path to get File bites ");
				byte[] fileBytes = Files.readAllBytes(new File(uploadedpath).toPath()) ;
				if (fileBytes.length > 0) {
					logger.info("To encode the bytes  ::");
					encodedString = Base64.getEncoder().encodeToString(fileBytes);
				}
			}
			logger.info("Uploaded files are retieved and encoded successfully");
		} catch (FileNotFoundException e) {
			logger.info("File not found during decode and upload ", e);
			throw new ApplicationRuntimeException(e);
		} catch (IOException e) {
			logger.info("IO Exception during decode and upload ", e);
			throw new ApplicationRuntimeException(e);
		}
		return encodedString;

	}

	// For Import and Export
	public BaseVo previewImportFile(UploadFileVo importFileVo)
			throws InvalidFormatException, ParseException, ApplicationException {
		logger.info("Entry into method: createImportFile");
		FileReader reader;
		BaseVo data = null;
		try {
			reader = new FileReader("/decifer/config/app_config.properties");
			Properties p = new Properties();
			p.load(reader);
			StringBuilder path = new StringBuilder(p.getProperty("file_store_path"));
			if (importFileVo.getData() != null && !importFileVo.getData().isEmpty()) {
				logger.info("got the encoded string");
				File f = new File(path.toString());
				logger.info(path.toString() + " to navigate ");
				if (f.exists() && f.isDirectory()) {
					String pathtoSave = path.toString();
					logger.info(pathtoSave + " " + "file path to save the data");
					File fpath = new File(
							pathtoSave + "/" + importFileVo.getModuleName() + "." + importFileVo.getFileType());
					logger.info(fpath + " " + "file created to save data");
					byte[] encodedData = Base64.getDecoder().decode(importFileVo.getData().getBytes());
					BufferedOutputStream scanStream;
					scanStream = new BufferedOutputStream(new FileOutputStream(fpath));
					scanStream.write(encodedData);
					String fileName = importFileVo.getModuleName() + "." + importFileVo.getFileType();
					scanStream.close();
					data = getFileResponse(fpath, fileName);
					logger.info("File Uploaded Successfully for " + importFileVo.getModuleName());
				} else {
					logger.info(importFileVo.getModuleName() + " folder not exist for Given organization");
				}
			}
		} catch (FileNotFoundException e) {
			logger.info("File not found ", e);
			throw new ApplicationRuntimeException(e);
		} catch (IOException e) {
			logger.info("IO Exception ", e);
			throw new ApplicationRuntimeException(e);
		}
		return data;
	}

	public BaseVo getFileResponse(File filepath, String moduleName)
			throws IOException, InvalidFormatException, ParseException, ApplicationException {
		logger.info("Entry into Method: getFileResponse");
		BaseVo baseVo = null;
		if (moduleName.equalsIgnoreCase("employee.csv")) {
			ListEmployeeUploadVo listEmployeeVo = new ListEmployeeUploadVo();
			listEmployeeVo.setEmployee(getEmployeeCsv(filepath));
			baseVo = listEmployeeVo;
		}
		if (moduleName.equalsIgnoreCase("employee.xls")) {
			ListEmployeeUploadVo listEmployeeVo = new ListEmployeeUploadVo();
			listEmployeeVo.setEmployee(getEmployeexlsx(filepath));
			baseVo = listEmployeeVo;
		}

		if (moduleName.equalsIgnoreCase("vendor.csv")) {
			ListVendorUploadVo listVendorImportVo = new ListVendorUploadVo();
			listVendorImportVo.setVendor(getVendorCsv(filepath));
			baseVo = listVendorImportVo;
		}
		if (moduleName.equalsIgnoreCase("vendor.xls")) {
			ListVendorUploadVo listVendorImportVo = new ListVendorUploadVo();
			listVendorImportVo.setVendor(getVendorXlsx(filepath));
			baseVo = listVendorImportVo;
		}
		if (moduleName.equalsIgnoreCase("customer.csv")) {
			ListCustomerUploadVo listCustomerImportVo = new ListCustomerUploadVo();
			listCustomerImportVo.setCustomer(getCustomerCsv(filepath));
			baseVo = listCustomerImportVo;
		}
		if (moduleName.equalsIgnoreCase("customer.xls")) {
			ListCustomerUploadVo listCustomerImportVo = new ListCustomerUploadVo();
			listCustomerImportVo.setCustomer(getCustomerXlsx(filepath));
			baseVo = listCustomerImportVo;
		}
		if (moduleName.equalsIgnoreCase("accounting entries.csv")) {
			ListAccountingEntriesUploadVo listAccountingEntriesVo = new ListAccountingEntriesUploadVo();
			listAccountingEntriesVo.setAccountingEntries(getAccountingEntriesCsv(filepath));
			baseVo = listAccountingEntriesVo;
		}
		if (moduleName.equalsIgnoreCase("accounting entries.xls")) {
			ListAccountingEntriesUploadVo listAccountingEntriesVo = new ListAccountingEntriesUploadVo();
			listAccountingEntriesVo.setAccountingEntries(getAccountingEntriesXlsx(filepath));
			baseVo = listAccountingEntriesVo;
		}
		if (moduleName.equalsIgnoreCase("payroll.csv")) {
			ListPayrollUploadVo listPayrollVo = new ListPayrollUploadVo();
			listPayrollVo.setPayRunInformation(getPayrollCsv(filepath));
			baseVo = listPayrollVo;
		}
		if (moduleName.equalsIgnoreCase("payroll.xls")) {
			ListPayrollUploadVo listPayrollVo = new ListPayrollUploadVo();
			listPayrollVo.setPayRunInformation(getPayrollXls(filepath));
			baseVo = listPayrollVo;
		}
		
		if (moduleName.equalsIgnoreCase("contra.csv")) {
			ListContraUploadVo listContraVo = new ListContraUploadVo();
			listContraVo.setContra(getContraCsv(filepath));
			baseVo = listContraVo;
		}
		if (moduleName.equalsIgnoreCase("contra.xls")) {
			ListContraUploadVo listContraVo = new ListContraUploadVo();
			listContraVo.setContra(getContraXlsx(filepath));
			baseVo = listContraVo;
		}
		
		if (moduleName.equalsIgnoreCase("bulk_payments_yes_bank.csv")) {
			ListBulkPaymentsUploadVo listbulkpaymentVo = new ListBulkPaymentsUploadVo();	
			listbulkpaymentVo.setBulkpayments(getYesBankBulkPaymentsCsv(filepath));
			baseVo = listbulkpaymentVo;
		}
		if (moduleName.equalsIgnoreCase("bulk_payments_yes_bank.xls")) {
			ListBulkPaymentsUploadVo listbulkpaymentVo = new ListBulkPaymentsUploadVo();
			listbulkpaymentVo.setBulkpayments(getYesBankBulkPaymentsXlsx(filepath));
			baseVo = listbulkpaymentVo;
		}

		return baseVo;

	}

	private List<ContraUploadVo> getContraXlsx(File filepath)
			throws InvalidFormatException, IOException, ApplicationException {
		logger.info("Entry into Method: getContraXlsx");
		Workbook workbook;
		Sheet sheet = null;
		List<ContraUploadVo> contraList = new ArrayList<ContraUploadVo>();
		workbook = WorkbookFactory.create(filepath);
		sheet = workbook.getSheetAt(0);
		int rowCount = 0;
		for (Row row : sheet) {
			if (rowCount == 0) {
				if ((row.getCell(0).toString().isEmpty()) || (!row.getCell(0).toString().equals("reference_no"))) {
					throw new ApplicationException("Column reference_no is required");
				}
				if ((row.getCell(1).toString().isEmpty()) || (!row.getCell(1).toString().equals("date"))) {
					throw new ApplicationException("Column date is required");
				}
				if ((row.getCell(2).toString().isEmpty()) || (!row.getCell(2).toString().equals("account_name"))) {
					throw new ApplicationException("Column account_name is required");
				}
				if ((row.getCell(3).toString().isEmpty()) || (!row.getCell(3).toString().equals("account_type"))) {
					throw new ApplicationException("Column account_type is required");
				}
				if ((row.getCell(4).toString().isEmpty()) || (!row.getCell(4).toString().equals("currency_name"))) {
					throw new ApplicationException("Column currency_name is required");
				}
				if ((row.getCell(5).toString().isEmpty()) || (!row.getCell(5).toString().equals("exchange_rate"))) {
					throw new ApplicationException("Column exchange_rate is required");
				}
				if ((row.getCell(6).toString().isEmpty()) || (!row.getCell(6).toString().equals("debit"))) {
					throw new ApplicationException("Column debit is required");
				}
				if ((row.getCell(7).toString().isEmpty()) || (!row.getCell(7).toString().equals("credit"))) {
					throw new ApplicationException("Column credit is required");
				}

			}

			if (rowCount > 0) {
				ContraUploadVo contraUploadVo = new ContraUploadVo();
				contraUploadVo.setReferenceNo(row.getCell(0).toString());
				contraUploadVo.setDate(row.getCell(1).toString());
				contraUploadVo.setAccountName(row.getCell(2).toString());
				contraUploadVo.setAccountype(row.getCell(3).toString());
				contraUploadVo.setCurrency(row.getCell(4).toString());
				contraUploadVo.setExchangeRate(row.getCell(5).toString());
				contraUploadVo.setDebit(row.getCell(6).toString());
				contraUploadVo.setCredit(row.getCell(7).toString());
				contraList.add(contraUploadVo);
			}
			rowCount++;
		}
		filepath.delete();
		return contraList;
	}

	private List<BulkPaymentsUploadVo> getYesBankBulkPaymentsXlsx(File filepath)
			throws InvalidFormatException, IOException, ApplicationException {
		logger.info("Entry into Method: getYesBankBulkPaymentsXlsx::");
		Workbook workbook;
		Sheet sheet = null;
		List<BulkPaymentsUploadVo> bulkpaymentList = new ArrayList<BulkPaymentsUploadVo>();
		workbook = WorkbookFactory.create(filepath);
		sheet = workbook.getSheetAt(0);
		int rowCount = 0;
		DecimalFormat df = new DecimalFormat("0.00");

		for (Row row : sheet) {
			if (rowCount == 0) {
				if ((row.getCell(0).toString().isEmpty()) || (!row.getCell(0).toString().contains("Transaction type") )) {
					throw new ApplicationException("Column Transaction type is required");
				}
				if ((row.getCell(1).toString().isEmpty()) || (!row.getCell(1).toString().contains("IFSC"))) {
					throw new ApplicationException("Column IFSC is required");
				}
				if ((row.getCell(2).toString().isEmpty()) || (!row.getCell(2).toString().contains("Beneficiary Account No"))) {
					throw new ApplicationException("Column Beneficiary Account No is required");
				}
				if ((row.getCell(3).toString().isEmpty()) || (!row.getCell(3).toString().contains("Beneficiary Name"))) {
					throw new ApplicationException("Column Beneficiary Name is required");
				}
				if ((row.getCell(4).toString().isEmpty()) || (!row.getCell(4).toString().contains("Amount"))) {
					throw new ApplicationException("Column Amount is required");
				}
				if ((row.getCell(5).toString().isEmpty()) || (!row.getCell(5).toString().contains("Remarks for Beneficiary"))) {
					throw new ApplicationException("Column Remarks for Beneficiary is required");
				}
			}
			

			if (rowCount > 0 &&  row!=null) {
				logger.info("To process API ");
				if(row.getCell(0)!=null) {
				BulkPaymentsUploadVo bulkpayment = new BulkPaymentsUploadVo();
				bulkpayment.setTransactionType(row.getCell(0)!=null? row.getCell(0).toString() :null);
				bulkpayment.setIfsc(row.getCell(1)!=null ? row.getCell(1).toString(): null);
				//DataFormatter formatter = new DataFormatter();
				//String cell = formatter.formatCellValue(row.getCell(2));
				String accounNO = null;
				if(row.getCell(2)!=null ) {
				switch (row.getCell(2).getCellType()) { 
				case Cell.CELL_TYPE_STRING: 
					accounNO = row.getCell(2).getStringCellValue() ;
				break; 
				case Cell.CELL_TYPE_NUMERIC: 
					accounNO = BigDecimal.valueOf(row.getCell(2).getNumericCellValue()).toPlainString();
					break; 
				default:
					DataFormatter formatter = new DataFormatter();
					accounNO = formatter.formatCellValue(row.getCell(2));
					break;
				}
				logger.info("accountNO ::"+accounNO);
				}
				//logger.info("infoo::"+row.getCell(2).getStringCellValue());
				bulkpayment.setBeneficiaryAccountNo(accounNO);
				bulkpayment.setBeneficiaryName(row.getCell(3)!=null ? row.getCell(3).toString() : null);
				Double amount  = row.getCell(4)!=null ?   Double.valueOf(row.getCell(4).toString()): null ;
				bulkpayment.setAmount(amount!=null ? String.valueOf(df.format(amount)) : null);
				bulkpayment.setRemarks(row.getCell(5)!=null ? row.getCell(5).toString() : null);
				bulkpaymentList.add(bulkpayment);
				logger.info("bulkpayment");
				}
			}
			rowCount++;
		}
		logger.info("bulkpayment::"+bulkpaymentList.size());
		filepath.delete();
		if(bulkpaymentList.isEmpty()) {
			throw new ApplicationException("The file does not contain valid payment information. Kindly Check and Upload again");
		}
		return bulkpaymentList;
	}
	private List<BulkPaymentsUploadVo> getYesBankBulkPaymentsCsv(File filepath)
			throws ApplicationException, FileNotFoundException, IOException {
		logger.info("Entry into Method: getYesBankBulkPaymentsCsv");
		CSVFormat format = CSVFormat.RFC4180.withHeader().withDelimiter(',');
		parser = new CSVParser(new FileReader(filepath), format);
		List<BulkPaymentsUploadVo> bulkpaymentList = new ArrayList<BulkPaymentsUploadVo>();
		Map<String, Integer> header = parser.getHeaderMap();
		DecimalFormat df = new DecimalFormat("0.00");
		for (Map.Entry<String, Integer> entry : header.entrySet()) {
			if (entry.getValue().toString().equals("0")
					&& (entry.getKey().toString().isEmpty() || !(entry.getKey().toString().contains("Transaction type")))) {
				throw new ApplicationException("Column Transaction type is required");
			}
			if (entry.getValue().toString().equals("1")
					&& (entry.getKey().toString().isEmpty() || !(entry.getKey().toString().contains("IFSC")))) {
				throw new ApplicationException("Column IFSC is required");
			}
			if (entry.getValue().toString().equals("2")
					&& (entry.getKey().toString().isEmpty() || !(entry.getKey().toString().contains("Beneficiary Account No")))) {
				throw new ApplicationException("Column Beneficiary Account No is required");
			}
			if (entry.getValue().toString().equals("3")
					&& (entry.getKey().toString().isEmpty() || !(entry.getKey().toString().contains("Beneficiary Name")))) {
				throw new ApplicationException("Column Beneficiary Name is required");
			}
			if (entry.getValue().toString().equals("4")
					&& (entry.getKey().toString().isEmpty() || !(entry.getKey().toString().contains("Amount")))) {
				throw new ApplicationException("Column Amount is required");
			}
			if (entry.getValue().toString().equals("5")
					&& (entry.getKey().toString().isEmpty() || !(entry.getKey().toString().contains("Remarks for Beneficiary")))) {
				throw new ApplicationException("Column Remarks for Beneficiary is required");
			}
		}

		for (CSVRecord record : parser) {
			logger.info("record.get(0)::"+record);
			BulkPaymentsUploadVo bulkpayment = new BulkPaymentsUploadVo();
			bulkpayment.setTransactionType(record.get(0));
			bulkpayment.setIfsc(record.get(1));
			bulkpayment.setBeneficiaryAccountNo(record.get(2)!=null && !record.get(2).isEmpty() ? new BigDecimal(record.get(2)).toBigInteger().toString() : null);
			bulkpayment.setBeneficiaryName(record.get(3));
			bulkpayment.setAmount(record.get(4));
			bulkpayment.setRemarks(record.get(5));
			bulkpaymentList.add(bulkpayment);
			
		}
		parser.close();
		filepath.delete();
		if(bulkpaymentList.isEmpty()) {
			throw new ApplicationException("The file does not contain valid payment information. Kindly Check and Upload again");
		}
		return bulkpaymentList;
	}
	
	
	private List<ContraUploadVo> getContraCsv(File filepath)
			throws ApplicationException, FileNotFoundException, IOException {
		logger.info("Entry into Method: getContraCsv");
		CSVFormat format = CSVFormat.RFC4180.withHeader().withDelimiter(',');
		parser = new CSVParser(new FileReader(filepath), format);
		List<ContraUploadVo> contraList = new ArrayList<ContraUploadVo>();
		Map<String, Integer> header = parser.getHeaderMap();

		for (Map.Entry<String, Integer> entry : header.entrySet()) {
			if (entry.getValue().toString().equals("0")
					&& (entry.getKey().toString().isEmpty() || !(entry.getKey().toString().equals("reference_no")))) {
				throw new ApplicationException("Column reference_no is required");
			}
			if (entry.getValue().toString().equals("1")
					&& (entry.getKey().toString().isEmpty() || !(entry.getKey().toString().equals("date")))) {
				throw new ApplicationException("Column date is required");
			}
			if (entry.getValue().toString().equals("2")
					&& (entry.getKey().toString().isEmpty() || !(entry.getKey().toString().equals("account_name")))) {
				throw new ApplicationException("Column account_name is required");
			}
			if (entry.getValue().toString().equals("3")
					&& (entry.getKey().toString().isEmpty() || !(entry.getKey().toString().equals("account_type")))) {
				throw new ApplicationException("Column account_type is required");
			}
			if (entry.getValue().toString().equals("4")
					&& (entry.getKey().toString().isEmpty() || !(entry.getKey().toString().equals("currency_name")))) {
				throw new ApplicationException("Column currency_name is required");
			}
			if (entry.getValue().toString().equals("5")
					&& (entry.getKey().toString().isEmpty() || !(entry.getKey().toString().equals("exchange_rate")))) {
				throw new ApplicationException("Column exchange_rate is required");
			}
			if (entry.getValue().toString().equals("6")
					&& (entry.getKey().toString().isEmpty() || !(entry.getKey().toString().equals("debit")))) {
				throw new ApplicationException("Column debit is required");
			}
			if (entry.getValue().toString().equals("7")
					&& (entry.getKey().toString().isEmpty() || !(entry.getKey().toString().equals("credit")))) {
				throw new ApplicationException("Column credit is required");
			}
		}

		for (CSVRecord record : parser) {
			ContraUploadVo contraUploadVo = new ContraUploadVo();
			contraUploadVo.setReferenceNo(record.get("reference_no"));
			contraUploadVo.setDate(record.get("date"));
			contraUploadVo.setAccountName(record.get("account_name"));
			contraUploadVo.setAccountype(record.get("account_type"));
			contraUploadVo.setCurrency(record.get("currency_name"));
			contraUploadVo.setExchangeRate(record.get("exchange_rate"));
			contraUploadVo.setDebit(record.get("debit"));
			contraUploadVo.setCredit(record.get("credit"));
			contraList.add(contraUploadVo);
			logger.info("contraUploadVo::"+contraUploadVo);
		}
		parser.close();
		filepath.delete();
		return contraList;
	}

	private List<PayrollUploadVo> getPayrollXls(File filepath)
			throws InvalidFormatException, IOException, ApplicationException {
		logger.info("Entry into Method: getPayrollXls::"+filepath);
		Workbook workbook;
		Sheet sheet = null;
		List<PayrollUploadVo> payrollList = new ArrayList<PayrollUploadVo>();
		workbook = WorkbookFactory.create(filepath);
		DataFormatter dmt = new DataFormatter();
		sheet = workbook.getSheetAt(0);
		int rowCount = 0;
		for (Row row : sheet) {
			
			if (rowCount == 0) {
				if ((row.getCell(0).toString().isEmpty()) || (!row.getCell(0).toString().equals("E-code"))) {
					throw new ApplicationException("Column E-code is required");
				}
				if ((row.getCell(1).toString().isEmpty()) || (!row.getCell(1).toString().equals("Name"))) {
					throw new ApplicationException("Column Name is required");
				}
				if ((row.getCell(2).toString().isEmpty()) || (!row.getCell(2).toString().equals("Basic"))) {
					throw new ApplicationException("Column Basic is required");
				}
				if ((row.getCell(3).toString().isEmpty()) || (!row.getCell(3).toString().equals("HRA"))) {
					throw new ApplicationException("Column HRA is required");
				}
				if ((row.getCell(4).toString().isEmpty()) || (!row.getCell(4).toString().equals("Special Allowance"))) {
					throw new ApplicationException("Column Special Allowance is required");
				}
				if ((row.getCell(5).toString().isEmpty()) || (!row.getCell(5).toString().equals("Conveyance"))) {
					throw new ApplicationException("Column Conveyance is required");
				}
				if ((row.getCell(6).toString().isEmpty())
						|| (!row.getCell(6).toString().equals("PF-Employer Contribution"))) {
					throw new ApplicationException("Column PF-Employer Contribution is required");
				}
				if ((row.getCell(7).toString().isEmpty())
						|| (!row.getCell(7).toString().equals("ESI-Employer Contribution"))) {
					throw new ApplicationException("Column ESI-Employer Contribution is required");
				}
				if ((row.getCell(8).toString().isEmpty()) || (!row.getCell(8).toString().equals("Leave Encashment"))) {
					throw new ApplicationException("Column Leave Encashment is required");
				}
				if ((row.getCell(9).toString().isEmpty()) || (!row.getCell(9).toString().equals("Bonus"))) {
					throw new ApplicationException("Column Bonus is required");
				}
				if ((row.getCell(10).toString().isEmpty()) || (!row.getCell(10).toString().equals("Overtime"))) {
					throw new ApplicationException("Column Overtime is required");
				}
				if ((row.getCell(11).toString().isEmpty())
						|| (!row.getCell(11).toString().equals("PF-Employee Contribution"))) {
					throw new ApplicationException("Column PF-Employee Contribution is required");
				}
				if ((row.getCell(12).toString().isEmpty())
						|| (!row.getCell(12).toString().equals("PF Employer payable"))) {
					throw new ApplicationException("Column PF Employer payable is required");
				}
				if ((row.getCell(13).toString().isEmpty())
						|| (!row.getCell(13).toString().equals("ESI-Employee Contribution"))) {
					throw new ApplicationException("Column ESI-Employee Contribution is required");
				}
				if ((row.getCell(14).toString().isEmpty())
						|| (!row.getCell(14).toString().equals("ESI Employer payable"))) {
					throw new ApplicationException("Column ESI Employer payable is required");
				}
				if ((row.getCell(15).toString().isEmpty())
						|| (!row.getCell(15).toString().equals("Professional Tax"))) {
					throw new ApplicationException("Column Professional Tax is required");
				}
				if ((row.getCell(16).toString().isEmpty()) || (!row.getCell(16).toString().equals("Income Tax"))) {
					throw new ApplicationException("Column Income Tax is required");
				}
				if ((row.getCell(17).toString().isEmpty()) || (!row.getCell(17).toString().equals("Salary payable"))) {
					throw new ApplicationException("Column Salary payable is required");
				}
				if ((row.getCell(18).toString().isEmpty()) || (!row.getCell(18).toString().equals("Pay Period"))) {
					throw new ApplicationException("Column Pay Period is required");
				}
				if ((row.getCell(19).toString().isEmpty()) || (!row.getCell(19).toString().equals("Pay Cycle"))) {
					throw new ApplicationException("Column Pay Cycle is required");
				}
				
				
			}
			if (rowCount > 0) {
				if (row.getCell(0) != null) {
					
					PayrollUploadVo payroll = new PayrollUploadVo();
					payroll.setEmployeeId(row.getCell(0) != null ?  row.getCell(0).toString() : null);
					payroll.setEmployeeName(row.getCell(1) != null ?  row.getCell(1).toString() : null);
					payroll.setEarningsBasic(row.getCell(2) != null ? dmt.formatCellValue( row.getCell(2) ) : null);
					payroll.setEarningsHRA(row.getCell(3) != null ? dmt.formatCellValue( row.getCell(3) ) : null);
					payroll.setEarningsSpecialAllowance(row.getCell(4) != null ? dmt.formatCellValue( row.getCell(4) ) : null);
					payroll.setEarningsConveyance(row.getCell(5) != null ? dmt.formatCellValue( row.getCell(5) ) : null);
					payroll.setDeductionsPfEmployerContribution(row.getCell(6) != null ? dmt.formatCellValue( row.getCell(6) ) : null);
					payroll.setDeductionsEsiEmployerContribution(row.getCell(7) != null ? dmt.formatCellValue( row.getCell(7) ) : null);
					payroll.setEarningsLeaveEncashment(row.getCell(8) != null ? dmt.formatCellValue( row.getCell(8) ) : null);
					payroll.setEarningsBonus(row.getCell(9) != null ? dmt.formatCellValue( row.getCell(9) ) : null);
					payroll.setEarningsOvertime(row.getCell(10) != null ? dmt.formatCellValue( row.getCell(10) ) : null);
					payroll.setDeductionsPfEmployeeContribution(row.getCell(11) != null ? dmt.formatCellValue( row.getCell(11) ) : null);
					payroll.setDeductionsEsiEmployeeContribution(row.getCell(13) != null ? dmt.formatCellValue( row.getCell(13) ) : null);
					payroll.setDeductionsProfessionalTax(row.getCell(15) != null ? dmt.formatCellValue( row.getCell(15) ) : null);
					payroll.setDeductionsIncomeTax(row.getCell(16) != null ? dmt.formatCellValue( row.getCell(16) ) : null);
					payroll.setNetPayable(row.getCell(17) != null ? dmt.formatCellValue( row.getCell(17) ) : null);
					payroll.setPayPeriod(row.getCell(18) != null ? row.getCell(18).toString() : null);
					payroll.setPaymentCycle(row.getCell(19) != null ?  row.getCell(19).toString() : null);
					payrollList.add(payroll);
				}
			}
			rowCount++;
		}
		logger.info(" payrollList -xls "+payrollList);
		filepath.delete();
		return payrollList;
	}

	private List<PayrollUploadVo> getPayrollCsv(File filepath)
			throws ApplicationException, FileNotFoundException, IOException {
		logger.info("Entry into Method: getPayrollCsv");
		CSVFormat format = CSVFormat.RFC4180.withHeader().withDelimiter(',');
		parser = new CSVParser(new FileReader(filepath), format);
		List<PayrollUploadVo> payrollList = new ArrayList<PayrollUploadVo>();
		Map<String, Integer> header = parser.getHeaderMap();
		List<CSVRecord> records = parser.getRecords();
		logger.info("parser "+parser.getHeaderMap());
		logger.info(" header.entrySet() "+header.entrySet());

		for (Map.Entry<String, Integer> entry : header.entrySet()) {
			if (entry.getValue().toString().equals("0")
					&& (entry.getKey().toString().isEmpty() || !(entry.getKey().toString().equals("E-code")))) {
				throw new ApplicationException("Column E-code is required");
			}
			
			if (entry.getValue().toString().equals("1")
					&& (entry.getKey().toString().isEmpty() || !(entry.getKey().toString().equals("Name")))) {
				throw new ApplicationException("Column Name is required");
			}
			if (entry.getValue().toString().equals("2")
					&& (entry.getKey().toString().isEmpty() || !(entry.getKey().toString().equals("Basic")))) {
				throw new ApplicationException("Column Basic is required");
			}
			if (entry.getValue().toString().equals("3")
					&& (entry.getKey().toString().isEmpty() || !(entry.getKey().toString().equals("HRA")))) {
				throw new ApplicationException("Column HRA is required");
			}
			if (entry.getValue().toString().equals("4") && (entry.getKey().toString().isEmpty()
					|| !(entry.getKey().toString().equals("Special Allowance")))) {
				throw new ApplicationException("Column Special Allowance is required");
			}
			if (entry.getValue().toString().equals("5")
					&& (entry.getKey().toString().isEmpty() || !(entry.getKey().toString().equals("Conveyance")))) {
				throw new ApplicationException("Column Conveyance is required");
			}
			if (entry.getValue().toString().equals("6") && (entry.getKey().toString().isEmpty()
					|| !(entry.getKey().toString().equals("PF-Employer Contribution")))) {
				throw new ApplicationException("Column PF-Employer Contribution is required");
			}
			if (entry.getValue().toString().equals("7") && (entry.getKey().toString().isEmpty()
					|| !(entry.getKey().toString().equals("ESI-Employer Contribution")))) {
				throw new ApplicationException("Column ESI-Employer Contribution is required");
			}
			if (entry.getValue().toString().equals("8") && (entry.getKey().toString().isEmpty()
					|| !(entry.getKey().toString().equals("Leave Encashment")))) {
				throw new ApplicationException("Column Leave Encashment is required");
			}
			if (entry.getValue().toString().equals("9")
					&& (entry.getKey().toString().isEmpty() || !(entry.getKey().toString().equals("Bonus")))) {
				throw new ApplicationException("Column Bonus is required");
			}
			if (entry.getValue().toString().equals("10")
					&& (entry.getKey().toString().isEmpty() || !(entry.getKey().toString().equals("Overtime")))) {
				throw new ApplicationException("Column Overtime is required");
			}
			if (entry.getValue().toString().equals("11") && (entry.getKey().toString().isEmpty()
					|| !(entry.getKey().toString().equals("PF-Employee Contribution")))) {
				throw new ApplicationException("Column PF-Employee Contribution is required");
			}
			if (entry.getValue().toString().equals("12") && (entry.getKey().toString().isEmpty()
					|| !(entry.getKey().toString().equals("PF Employer payable")))) {
				throw new ApplicationException("Column PF Employer payable is required");
			}
			if (entry.getValue().toString().equals("13") && (entry.getKey().toString().isEmpty()
					|| !(entry.getKey().toString().equals("ESI-Employee Contribution")))) {
				throw new ApplicationException("Column ESI-Employee Contribution is required");
			}
			if (entry.getValue().toString().equals("14") && (entry.getKey().toString().isEmpty()
					|| !(entry.getKey().toString().equals("ESI Employer payable")))) {
				throw new ApplicationException("Column ESI Employer payable is required");
			}
			if (entry.getValue().toString().equals("15") && (entry.getKey().toString().isEmpty()
					|| !(entry.getKey().toString().equals("Professional Tax")))) {
				throw new ApplicationException("Column Professional Tax is required");
			}
			if (entry.getValue().toString().equals("16")
					&& (entry.getKey().toString().isEmpty() || !(entry.getKey().toString().equals("Income Tax")))) {
				throw new ApplicationException("Column Income Tax is required");
			}
			if (entry.getValue().toString().equals("17")
					&& (entry.getKey().toString().isEmpty() || !(entry.getKey().toString().equals("Salary payable")))) {
				throw new ApplicationException("Column Salary payable is required");
			}
		}
		
		for (CSVRecord record : records) {
			logger.info(" record "+record);
			PayrollUploadVo payroll = new PayrollUploadVo();
			payroll.setEmployeeId(record.get("E-code"));
			payroll.setEmployeeName(record.get("Name"));
			payroll.setEarningsBasic(record.get("Basic"));
			payroll.setEarningsHRA(record.get("HRA"));
			payroll.setEarningsSpecialAllowance(record.get("Special Allowance"));
			payroll.setEarningsConveyance(record.get("Conveyance"));
			payroll.setDeductionsPfEmployerContribution(record.get("PF-Employer Contribution"));
			payroll.setDeductionsEsiEmployerContribution(record.get("ESI-Employer Contribution"));
			payroll.setEarningsLeaveEncashment(record.get("Leave Encashment"));
			payroll.setEarningsBonus(record.get("Bonus"));
			payroll.setEarningsOvertime(record.get("Overtime"));
			payroll.setDeductionsPfEmployeeContribution(record.get("PF-Employee Contribution"));
			payroll.setDeductionsEsiEmployeeContribution(record.get("ESI-Employee Contribution"));
			payroll.setDeductionsProfessionalTax(record.get("Professional Tax"));
			payroll.setDeductionsIncomeTax(record.get("Income Tax"));
			payroll.setNetPayable(record.get("Salary payable"));
			payroll.setPayPeriod(record.get("Pay Period"));
			payroll.setPaymentCycle(record.get("Pay Cycle"));
			payrollList.add(payroll);
		}
		logger.info("payrollList ");
		parser.close();
		filepath.delete();
		return payrollList;
	}

	private List<AccountingEntriesUploadVo> getAccountingEntriesXlsx(File filepath)
			throws InvalidFormatException, IOException, ParseException, ApplicationException {
		logger.info("Entry into Method: getAccountingEntriesXlsx");
		Workbook workbook;
		Sheet sheet = null;
		List<AccountingEntriesUploadVo> accountingEntries = new ArrayList<AccountingEntriesUploadVo>();
		workbook = WorkbookFactory.create(filepath);
		sheet = workbook.getSheetAt(0);
		int rowCount = 0;
		for (Row row : sheet) {
			if (rowCount == 0) {
				if ((row.getCell(0).toString().isEmpty()) || (!row.getCell(0).toString().equals("date"))) {
					throw new ApplicationException("Column date is required");
				}
				if ((row.getCell(1).toString().isEmpty()) || (!row.getCell(1).toString().equals("type"))) {
					throw new ApplicationException("Column type is required");
				}
				if ((row.getCell(2).toString().isEmpty()) || (!row.getCell(2).toString().equals("journal_no"))) {
					throw new ApplicationException("Column journal_no is required");
				}
				if ((row.getCell(3).toString().isEmpty()) || (!row.getCell(3).toString().equals("location"))) {
					throw new ApplicationException("Column location is required");
				}
				if ((row.getCell(4).toString().isEmpty()) || (!row.getCell(4).toString().equals("ledger"))) {
					throw new ApplicationException("Column ledger is required");
				}
				if ((row.getCell(5).toString().isEmpty()) || (!row.getCell(5).toString().equals("sub_ledger"))) {
					throw new ApplicationException("Column sub_ledger is required");
				}
				if ((row.getCell(6).toString().isEmpty()) || (!row.getCell(6).toString().equals("currency"))) {
					throw new ApplicationException("Column currency is required");
				}
				if ((row.getCell(7).toString().isEmpty()) || (!row.getCell(7).toString().equals("exchange_rate"))) {
					throw new ApplicationException("Column exchange_rate is required");
				}
				if ((row.getCell(8).toString().isEmpty()) || (!row.getCell(8).toString().equals("credits"))) {
					throw new ApplicationException("Column credits is required");
				}
				if ((row.getCell(9).toString().isEmpty()) || (!row.getCell(9).toString().equals("debits"))) {
					throw new ApplicationException("Column debits is required");
				}

			}

			if (rowCount > 0) {
				AccountingEntriesUploadVo accountingEntry = new AccountingEntriesUploadVo();
				accountingEntry.setDate(row.getCell(0).toString());
				accountingEntry.setType(row.getCell(1).toString());
				accountingEntry.setJournalNo(row.getCell(2).toString());
				accountingEntry.setLocation(row.getCell(3).toString());
				accountingEntry.setLedger(row.getCell(4).toString());
				accountingEntry.setSubLedger(row.getCell(5).toString());
				accountingEntry.setCurrency(row.getCell(6).toString());
				accountingEntry.setExchangeRate(row.getCell(7).toString());
				accountingEntry.setCredits(row.getCell(8).toString());
				accountingEntry.setDebits(row.getCell(9).toString());
				accountingEntries.add(accountingEntry);
			}
			rowCount++;
		}
		filepath.delete();
		return accountingEntries;
	}

	private List<AccountingEntriesUploadVo> getAccountingEntriesCsv(File filepath)
			throws FileNotFoundException, IOException, ParseException, ApplicationException {
		logger.info("Entry into Method: getAccountingEntriesCsv");
		CSVFormat format = CSVFormat.RFC4180.withHeader().withDelimiter(',');
		parser = new CSVParser(new FileReader(filepath), format);
		List<AccountingEntriesUploadVo> accountingEntries = new ArrayList<AccountingEntriesUploadVo>();
		Map<String, Integer> header = parser.getHeaderMap();

		for (Map.Entry<String, Integer> entry : header.entrySet()) {
			if (entry.getValue().toString().equals("0")
					&& (entry.getKey().toString().isEmpty() || !(entry.getKey().toString().equals("date")))) {
				throw new ApplicationException("Column date is required");
			}
			if (entry.getValue().toString().equals("1")
					&& (entry.getKey().toString().isEmpty() || !(entry.getKey().toString().equals("type")))) {
				throw new ApplicationException("Column type is required");
			}
			if (entry.getValue().toString().equals("2")
					&& (entry.getKey().toString().isEmpty() || !(entry.getKey().toString().equals("journal_no")))) {
				throw new ApplicationException("Column journal_no is required");
			}
			if (entry.getValue().toString().equals("3")
					&& (entry.getKey().toString().isEmpty() || !(entry.getKey().toString().equals("location")))) {
				throw new ApplicationException("Column location is required");
			}
			if (entry.getValue().toString().equals("4")
					&& (entry.getKey().toString().isEmpty() || !(entry.getKey().toString().equals("ledger")))) {
				throw new ApplicationException("Column ledger is required");
			}
			if (entry.getValue().toString().equals("5")
					&& (entry.getKey().toString().isEmpty() || !(entry.getKey().toString().equals("sub_ledger")))) {
				throw new ApplicationException("Column sub_ledger is required");
			}
			if (entry.getValue().toString().equals("6")
					&& (entry.getKey().toString().isEmpty() || !(entry.getKey().toString().equals("currency")))) {
				throw new ApplicationException("Column currency is required");
			}
			if (entry.getValue().toString().equals("7")
					&& (entry.getKey().toString().isEmpty() || !(entry.getKey().toString().equals("exchange_rate")))) {
				throw new ApplicationException("Column exchange_rate is required");
			}
			if (entry.getValue().toString().equals("8")
					&& (entry.getKey().toString().isEmpty() || !(entry.getKey().toString().equals("credits")))) {
				throw new ApplicationException("Column credits is required");
			}
			if (entry.getValue().toString().equals("9")
					&& (entry.getKey().toString().isEmpty() || !(entry.getKey().toString().equals("debits")))) {
				throw new ApplicationException("Column debits is required");
			}
		}

		for (CSVRecord record : parser) {
			AccountingEntriesUploadVo accountingEntry = new AccountingEntriesUploadVo();
			accountingEntry.setDate(record.get("date"));
			accountingEntry.setType(record.get("type"));
			accountingEntry.setJournalNo(record.get("journal_no"));
			accountingEntry.setLocation(record.get("location"));
			accountingEntry.setLedger(record.get("ledger"));
			accountingEntry.setSubLedger(record.get("sub_ledger"));
			accountingEntry.setCurrency(record.get("currency"));
			accountingEntry.setExchangeRate(record.get("exchange_rate"));
			accountingEntry.setCredits(record.get("credits"));
			accountingEntry.setDebits(record.get("debits"));
			accountingEntries.add(accountingEntry);
		}
		parser.close();
		filepath.delete();
		return accountingEntries;
	}

	private List<CustomerUploadVo> getCustomerXlsx(File filepath)
			throws InvalidFormatException, IOException, ApplicationException {
		logger.info("Entry into Method: getCustomerXlsx");
		Workbook workbook;
		Sheet sheet = null;
		List<CustomerUploadVo> customers = new ArrayList<CustomerUploadVo>();
		workbook = WorkbookFactory.create(filepath);
		sheet = workbook.getSheetAt(0);
		int rowCount = 0;
		for (Row row : sheet) {
			if (rowCount == 0) {
				if ((row.getCell(0).toString().isEmpty()) || (!row.getCell(0).toString().equals("primary_contact"))) {
					throw new ApplicationException("Column primary_contact is required");
				}
				if ((row.getCell(1).toString().isEmpty()) || (!row.getCell(1).toString().equals("company_name"))) {
					throw new ApplicationException("Column company_name is required");
				}
				if ((row.getCell(2).toString().isEmpty()) || (!row.getCell(2).toString().equals("display_name"))) {
					throw new ApplicationException("Column display_name is required");
				}
				if ((row.getCell(3).toString().isEmpty()) || (!row.getCell(3).toString().equals("email"))) {
					throw new ApplicationException("Column email is required");
				}
				if ((row.getCell(4).toString().isEmpty()) || (!row.getCell(4).toString().equals("mobile"))) {
					throw new ApplicationException("Column mobile is required");
				}
				if ((row.getCell(5).toString().isEmpty()) || (!row.getCell(5).toString().equals("currency"))) {
					throw new ApplicationException("Column currency is required");
				}
				if ((row.getCell(6).toString().isEmpty())
						|| (!row.getCell(6).toString().equals("delivery_addr_attention"))) {
					throw new ApplicationException("Column delivery_addr_attention is required");
				}
				if ((row.getCell(7).toString().isEmpty())
						|| (!row.getCell(7).toString().equals("delivery_addr_phone"))) {
					throw new ApplicationException("Column delivery_addr_phone is required");
				}
				if ((row.getCell(8).toString().isEmpty())
						|| (!row.getCell(8).toString().equals("delivery_addr_country"))) {
					throw new ApplicationException("Column delivery_addr_country is required");
				}
				if ((row.getCell(9).toString().isEmpty())
						|| (!row.getCell(9).toString().equals("delivery_addr_address_line1"))) {
					throw new ApplicationException("Column delivery_addr_address_line1 is required");
				}
				if ((row.getCell(10).toString().isEmpty())
						|| (!row.getCell(10).toString().equals("delivery_addr_state"))) {
					throw new ApplicationException("Column delivery_addr_state is required");
				}
				if ((row.getCell(11).toString().isEmpty())
						|| (!row.getCell(11).toString().equals("delivery_addr_city"))) {
					throw new ApplicationException("Column delivery_addr_city is required");
				}
				if ((row.getCell(12).toString().isEmpty())
						|| (!row.getCell(12).toString().equals("delivery_addr_pin"))) {
					throw new ApplicationException("Column delivery_addr_pin is required");
				}
				if ((row.getCell(13).toString().isEmpty())
						|| (!row.getCell(13).toString().equals("billing_addr_attention"))) {
					throw new ApplicationException("Column billing_addr_attention is required");
				}
				if ((row.getCell(14).toString().isEmpty())
						|| (!row.getCell(14).toString().equals("billing_addr_phone"))) {
					throw new ApplicationException("Column billing_addr_phone is required");
				}
				if ((row.getCell(15).toString().isEmpty())
						|| (!row.getCell(15).toString().equals("billing_addr_country"))) {
					throw new ApplicationException("Column billing_addr_country is required");
				}
				if ((row.getCell(16).toString().isEmpty())
						|| (!row.getCell(16).toString().equals("billing_addr_address_line1"))) {
					throw new ApplicationException("Column billing_addr_address_line1 is required");
				}
				if ((row.getCell(17).toString().isEmpty())
						|| (!row.getCell(17).toString().equals("billing_addr_state"))) {
					throw new ApplicationException("Column billing_addr_state is required");
				}
				if ((row.getCell(18).toString().isEmpty())
						|| (!row.getCell(18).toString().equals("billing_addr_city"))) {
					throw new ApplicationException("Column billing_addr_city is required");
				}
				if ((row.getCell(19).toString().isEmpty())
						|| (!row.getCell(19).toString().equals("billing_addr_pin"))) {
					throw new ApplicationException("Column billing_addr_pin is required");
				}

			}
			if (rowCount > 0) {
				CustomerUploadVo customer = new CustomerUploadVo();
				customer.setPrimaryContact(row.getCell(0).toString());
				customer.setCompanyName(row.getCell(1).toString());
				customer.setDisplayName(row.getCell(2).toString());
				customer.setEmail(row.getCell(3).toString());
				customer.setMobile(row.getCell(4).toString());
				customer.setCurrency(row.getCell(5).toString());
				customer.setDeliveryAddressAttention(row.getCell(6).toString());
				customer.setDeliveryAddressPhoneNo(row.getCell(7).toString());
				customer.setDeliveryAddressCountry(row.getCell(8).toString());
				customer.setDeliveryAddressLine1(row.getCell(9).toString());
				customer.setDeliveryAddressState(row.getCell(10).toString());
				customer.setDeliveryAddressCity(row.getCell(11).toString());
				customer.setDeliveryAddressPin(row.getCell(12).toString());
				customer.setBillingAddressAttention(row.getCell(13).toString());
				customer.setBillingAddressPhoneNo(row.getCell(14).toString());
				customer.setBillingAddressCountry(row.getCell(15).toString());
				customer.setBillingAddressLine1(row.getCell(16).toString());
				customer.setBillingAddressState(row.getCell(17).toString());
				customer.setBillingAddressCity(row.getCell(18).toString());
				customer.setBillingAddressPin(row.getCell(19).toString());
				customers.add(customer);
			}
			rowCount++;
		}
		filepath.delete();
		return customers;

	}

	private List<CustomerUploadVo> getCustomerCsv(File filepath)
			throws FileNotFoundException, IOException, ApplicationException {
		logger.info("Entry into Method: getCustomerCsv");
		CSVFormat format = CSVFormat.RFC4180.withHeader().withDelimiter(',');
		parser = new CSVParser(new FileReader(filepath), format);
		List<CustomerUploadVo> customers = new ArrayList<CustomerUploadVo>();
		Map<String, Integer> header = parser.getHeaderMap();

		for (Map.Entry<String, Integer> entry : header.entrySet()) {
			if (entry.getValue().toString().equals("0") && (entry.getKey().toString().isEmpty()
					|| !(entry.getKey().toString().equals("primary_contact")))) {
				throw new ApplicationException("Column primary_contact is required");
			}
			if (entry.getValue().toString().equals("1")
					&& (entry.getKey().toString().isEmpty() || !(entry.getKey().toString().equals("company_name")))) {
				throw new ApplicationException("Column company_name is required");
			}
			if (entry.getValue().toString().equals("2")
					&& (entry.getKey().toString().isEmpty() || !(entry.getKey().toString().equals("display_name")))) {
				throw new ApplicationException("Column display_name is required");
			}
			if (entry.getValue().toString().equals("3")
					&& (entry.getKey().toString().isEmpty() || !(entry.getKey().toString().equals("email")))) {
				throw new ApplicationException("Column email is required");
			}
			if (entry.getValue().toString().equals("4")
					&& (entry.getKey().toString().isEmpty() || !(entry.getKey().toString().equals("mobile")))) {
				throw new ApplicationException("Column mobile is required");
			}
			if (entry.getValue().toString().equals("5")
					&& (entry.getKey().toString().isEmpty() || !(entry.getKey().toString().equals("currency")))) {
				throw new ApplicationException("Column currency is required");
			}
			if (entry.getValue().toString().equals("6") && (entry.getKey().toString().isEmpty()
					|| !(entry.getKey().toString().equals("delivery_addr_attention")))) {
				throw new ApplicationException("Column delivery_addr_attention is required");
			}
			if (entry.getValue().toString().equals("7") && (entry.getKey().toString().isEmpty()
					|| !(entry.getKey().toString().equals("delivery_addr_phone")))) {
				throw new ApplicationException("Column delivery_addr_phone is required");
			}
			if (entry.getValue().toString().equals("8") && (entry.getKey().toString().isEmpty()
					|| !(entry.getKey().toString().equals("delivery_addr_country")))) {
				throw new ApplicationException("Column delivery_addr_country is required");
			}
			if (entry.getValue().toString().equals("9") && (entry.getKey().toString().isEmpty()
					|| !(entry.getKey().toString().equals("delivery_addr_address_line1")))) {
				throw new ApplicationException("Column delivery_addr_address_line1 is required");
			}
			if (entry.getValue().toString().equals("10") && (entry.getKey().toString().isEmpty()
					|| !(entry.getKey().toString().equals("delivery_addr_state")))) {
				throw new ApplicationException("Column delivery_addr_state is required");
			}
			if (entry.getValue().toString().equals("11") && (entry.getKey().toString().isEmpty()
					|| !(entry.getKey().toString().equals("delivery_addr_city")))) {
				throw new ApplicationException("Column delivery_addr_city is required");
			}
			if (entry.getValue().toString().equals("12") && (entry.getKey().toString().isEmpty()
					|| !(entry.getKey().toString().equals("delivery_addr_pin")))) {
				throw new ApplicationException("Column delivery_addr_pin is required");
			}
			if (entry.getValue().toString().equals("13") && (entry.getKey().toString().isEmpty()
					|| !(entry.getKey().toString().equals("billing_addr_attention")))) {
				throw new ApplicationException("Column billing_addr_attention is required");
			}
			if (entry.getValue().toString().equals("14") && (entry.getKey().toString().isEmpty()
					|| !(entry.getKey().toString().equals("billing_addr_phone")))) {
				throw new ApplicationException("Coumn billing_addr_phone is required");
			}
			if (entry.getValue().toString().equals("15") && (entry.getKey().toString().isEmpty()
					|| !(entry.getKey().toString().equals("billing_addr_country")))) {
				throw new ApplicationException("Column billing_addr_country is required");
			}
			if (entry.getValue().toString().equals("16") && (entry.getKey().toString().isEmpty()
					|| !(entry.getKey().toString().equals("billing_addr_address_line1")))) {
				throw new ApplicationException("Column billing_addr_address_line1 is required");
			}
			if (entry.getValue().toString().equals("17") && (entry.getKey().toString().isEmpty()
					|| !(entry.getKey().toString().equals("billing_addr_state")))) {
				throw new ApplicationException("Column billing_addr_state is required");
			}
			if (entry.getValue().toString().equals("18") && (entry.getKey().toString().isEmpty()
					|| !(entry.getKey().toString().equals("billing_addr_city")))) {
				throw new ApplicationException("Column billing_addr_city is required");
			}

			if (entry.getValue().toString().equals("19") && (entry.getKey().toString().isEmpty()
					|| !(entry.getKey().toString().equals("billing_addr_pin")))) {
				throw new ApplicationException("Column billing_addr_pin is required");
			}

		}
		for (CSVRecord record : parser) {
			CustomerUploadVo customer = new CustomerUploadVo();
			customer.setPrimaryContact(record.get("primary_contact"));
			customer.setCompanyName(record.get("company_name"));
			customer.setDisplayName(record.get("display_name"));
			customer.setEmail(record.get("email"));
			customer.setMobile(record.get("mobile"));
			customer.setCurrency(record.get("currency"));
			customer.setDeliveryAddressAttention(record.get("delivery_addr_attention"));
			customer.setDeliveryAddressPhoneNo(record.get("delivery_addr_phone"));
			customer.setDeliveryAddressCountry(record.get("delivery_addr_country"));
			customer.setDeliveryAddressLine1(record.get("delivery_addr_address_line1"));
			customer.setDeliveryAddressState(record.get("delivery_addr_state"));
			customer.setDeliveryAddressCity(record.get("delivery_addr_city"));
			customer.setDeliveryAddressPin(record.get("delivery_addr_pin"));
			customer.setBillingAddressAttention(record.get("billing_addr_attention"));
			customer.setBillingAddressPhoneNo(record.get("billing_addr_phone"));
			customer.setBillingAddressCountry(record.get("billing_addr_country"));
			customer.setBillingAddressLine1(record.get("billing_addr_address_line1"));
			customer.setBillingAddressState(record.get("billing_addr_state"));
			customer.setBillingAddressCity(record.get("billing_addr_city"));
			customer.setBillingAddressPin(record.get("billing_addr_pin"));
			customers.add(customer);
		}
		parser.close();
		filepath.delete();
		return customers;
	}

	private List<VendorUploadVo> getVendorXlsx(File filepath)
			throws IOException, InvalidFormatException, ApplicationException {
		logger.info("Entry into Method: getVendorXlsx");
		Workbook workbook;
		Sheet sheet = null;
		List<VendorUploadVo> vendors = new ArrayList<VendorUploadVo>();
		workbook = WorkbookFactory.create(filepath);
		sheet = workbook.getSheetAt(0);
		int rowCount = 0;
		for (Row row : sheet) {
			if (rowCount == 0) {
				if ((row.getCell(0).toString().isEmpty()) || (!row.getCell(0).toString().equals("primary_contact"))) {
					throw new ApplicationException("Column primary_contact is required");
				}
				if ((row.getCell(1).toString().isEmpty()) || (!row.getCell(1).toString().equals("company_name"))) {
					throw new ApplicationException("Column company_name is required");
				}
				if ((row.getCell(2).toString().isEmpty())
						|| (!row.getCell(2).toString().equals("vendor_display_name"))) {
					throw new ApplicationException("Column vendor_display_name is required");
				}
				if ((row.getCell(3).toString().isEmpty()) || (!row.getCell(3).toString().equals("email"))) {
					throw new ApplicationException("Column email is required");
				}
				if ((row.getCell(4).toString().isEmpty()) || (!row.getCell(4).toString().equals("payment_terms"))) {
					throw new ApplicationException("Column payment_terms is required");
				}
				if ((row.getCell(5).toString().isEmpty()) || (!row.getCell(5).toString().equals("currency"))) {
					throw new ApplicationException("Column currency is required");
				}
				if ((row.getCell(6).toString().isEmpty()) || (!row.getCell(6).toString().equals("tds"))) {
					throw new ApplicationException("Column tds is required");
				}
				if ((row.getCell(7).toString().isEmpty())
						|| (!row.getCell(7).toString().equals("origin_addr_attention"))) {
					throw new ApplicationException("Column origin_addr_attention is required");
				}
				if ((row.getCell(8).toString().isEmpty()) || (!row.getCell(8).toString().equals("origin_addr_phone"))) {
					throw new ApplicationException("Column origin_addr_phone is required");
				}
				if ((row.getCell(9).toString().isEmpty())
						|| (!row.getCell(9).toString().equals("origin_addr_country"))) {
					throw new ApplicationException("Column origin_addr_country is required");
				}
				if ((row.getCell(10).toString().isEmpty())
						|| (!row.getCell(10).toString().equals("origin_addr_address_line1"))) {
					throw new ApplicationException("Column origin_addr_address_line1 is required");
				}
				if ((row.getCell(11).toString().isEmpty())
						|| (!row.getCell(11).toString().equals("origin_addr_state"))) {
					throw new ApplicationException("Column origin_addr_state is required");
				}
				if ((row.getCell(12).toString().isEmpty())
						|| (!row.getCell(12).toString().equals("origin_addr_city"))) {
					throw new ApplicationException("Column origin_addr_city is required");
				}
				if ((row.getCell(13).toString().isEmpty()) || (!row.getCell(13).toString().equals("origin_addr_pin"))) {
					throw new ApplicationException("Column origin_addr_pin is required");
				}
				if ((row.getCell(14).toString().isEmpty())
						|| (!row.getCell(14).toString().equals("billing_addr_attention"))) {
					throw new ApplicationException("Column billing_addr_attention is required");
				}
				if ((row.getCell(15).toString().isEmpty())
						|| (!row.getCell(15).toString().equals("billing_addr_phone"))) {
					throw new ApplicationException("Column billing_addr_phone is required");
				}
				if ((row.getCell(16).toString().isEmpty())
						|| (!row.getCell(16).toString().equals("billing_addr_country"))) {
					throw new ApplicationException("Column billing_addr_country is required");
				}
				if ((row.getCell(17).toString().isEmpty())
						|| (!row.getCell(17).toString().equals("billing_addr_address_line1"))) {
					throw new ApplicationException("Column billing_addr_address_line1 is required");
				}
				if ((row.getCell(18).toString().isEmpty())
						|| (!row.getCell(18).toString().equals("billing_addr_state"))) {
					throw new ApplicationException("Column billing_addr_state is required");
				}
				if ((row.getCell(19).toString().isEmpty())
						|| (!row.getCell(19).toString().equals("billing_addr_city"))) {
					throw new ApplicationException("Column billing_addr_city is required");
				}
				if ((row.getCell(20).toString().isEmpty())
						|| (!row.getCell(20).toString().equals("billing_addr_pin"))) {
					throw new ApplicationException("Column billing_addr_pin is required");
				}
			}
			if (rowCount > 0) {
				VendorUploadVo vendor = new VendorUploadVo();
				vendor.setPrimaryContact(row.getCell(0).toString());
				vendor.setCompanyName(row.getCell(1).toString());
				vendor.setVendorDisplayName(row.getCell(2).toString());
				vendor.setEmail(row.getCell(3).toString());
				vendor.setPaymentTerms(row.getCell(4).toString());
				vendor.setCurrency(row.getCell(5).toString());
				vendor.setTds(row.getCell(6).toString());
				vendor.setOriginAddressAttention(row.getCell(7).toString());
				vendor.setOriginAddressPhone(row.getCell(8).toString());
				vendor.setOriginAddressCountry(row.getCell(9).toString());
				vendor.setOriginAddressLine1(row.getCell(10).toString());
				vendor.setOriginAddressState(row.getCell(11).toString());
				vendor.setOriginAddressCity(row.getCell(12).toString());
				vendor.setOriginAddressPin(row.getCell(13).toString());
				vendor.setBillingAddressAttention(row.getCell(14).toString());
				vendor.setBillingAddressPhone(row.getCell(15).toString());
				vendor.setBillingAddressCountry(row.getCell(16).toString());
				vendor.setBillingAddressLine1(row.getCell(17).toString());
				vendor.setBillingAddressState(row.getCell(18).toString());
				vendor.setBillingAddressCity(row.getCell(19).toString());
				vendor.setBillingAddressPin(row.getCell(20).toString());
				vendors.add(vendor);
			}
			rowCount++;
		}
		filepath.delete();
		return vendors;

	}

	private List<VendorUploadVo> getVendorCsv(File filepath)
			throws FileNotFoundException, IOException, ApplicationException {
		logger.info("Entry into method: getVendorCsv");
		CSVFormat format = CSVFormat.RFC4180.withHeader().withDelimiter(',');
		parser = new CSVParser(new FileReader(filepath), format);
		Map<String, Integer> header = parser.getHeaderMap();

		for (Map.Entry<String, Integer> entry : header.entrySet()) {
			if (entry.getValue().toString().equals("0") && (entry.getKey().toString().isEmpty()
					|| !(entry.getKey().toString().equals("primary_contact")))) {
				throw new ApplicationException("Column primary_contact is required");
			}
			if (entry.getValue().toString().equals("1")
					&& (entry.getKey().toString().isEmpty() || !(entry.getKey().toString().equals("company_name")))) {
				throw new ApplicationException("Column company_name is required");
			}
			if (entry.getValue().toString().equals("2") && (entry.getKey().toString().isEmpty()
					|| !(entry.getKey().toString().equals("vendor_display_name")))) {
				throw new ApplicationException("Column vendor_display_name is required");
			}
			if (entry.getValue().toString().equals("3")
					&& (entry.getKey().toString().isEmpty() || !(entry.getKey().toString().equals("email")))) {
				throw new ApplicationException("Column email is required");
			}
			if (entry.getValue().toString().equals("4")
					&& (entry.getKey().toString().isEmpty() || !(entry.getKey().toString().equals("payment_terms")))) {
				throw new ApplicationException("Column payment_terms is required");
			}
			if (entry.getValue().toString().equals("5")
					&& (entry.getKey().toString().isEmpty() || !(entry.getKey().toString().equals("currency")))) {
				throw new ApplicationException("Column currency is required");
			}
			if (entry.getValue().toString().equals("6")
					&& (entry.getKey().toString().isEmpty() || !(entry.getKey().toString().equals("tds")))) {
				throw new ApplicationException("Column tds is required");
			}
			if (entry.getValue().toString().equals("7") && (entry.getKey().toString().isEmpty()
					|| !(entry.getKey().toString().equals("origin_addr_attention")))) {
				throw new ApplicationException("Column origin_addr_attention is required");
			}
			if (entry.getValue().toString().equals("8") && (entry.getKey().toString().isEmpty()
					|| !(entry.getKey().toString().equals("origin_addr_phone")))) {
				throw new ApplicationException("Column origin_addr_phone is required");
			}
			if (entry.getValue().toString().equals("9") && (entry.getKey().toString().isEmpty()
					|| !(entry.getKey().toString().equals("origin_addr_country")))) {
				throw new ApplicationException("Column origin_addr_country is required");
			}
			if (entry.getValue().toString().equals("10") && (entry.getKey().toString().isEmpty()
					|| !(entry.getKey().toString().equals("origin_addr_address_line1")))) {
				throw new ApplicationException("COlumn origin_addr_address_line1 is required");
			}
			if (entry.getValue().toString().equals("11") && (entry.getKey().toString().isEmpty()
					|| !(entry.getKey().toString().equals("origin_addr_state")))) {
				throw new ApplicationException("Column origin_addr_state is required");
			}
			if (entry.getValue().toString().equals("12") && (entry.getKey().toString().isEmpty()
					|| !(entry.getKey().toString().equals("origin_addr_city")))) {
				throw new ApplicationException("Column origin_addr_city is required");
			}
			if (entry.getValue().toString().equals("13") && (entry.getKey().toString().isEmpty()
					|| !(entry.getKey().toString().equals("origin_addr_pin")))) {
				throw new ApplicationException("Column origin_addr_pin is required");
			}
			if (entry.getValue().toString().equals("14") && (entry.getKey().toString().isEmpty()
					|| !(entry.getKey().toString().equals("billing_addr_attention")))) {
				throw new ApplicationException("COlumn billing_addr_attention is required");
			}
			if (entry.getValue().toString().equals("15") && (entry.getKey().toString().isEmpty()
					|| !(entry.getKey().toString().equals("billing_addr_phone")))) {
				throw new ApplicationException("Column billing_addr_phone is required");
			}
			if (entry.getValue().toString().equals("16") && (entry.getKey().toString().isEmpty()
					|| !(entry.getKey().toString().equals("billing_addr_country")))) {
				throw new ApplicationException("Column billing_addr_country is required");
			}
			if (entry.getValue().toString().equals("17") && (entry.getKey().toString().isEmpty()
					|| !(entry.getKey().toString().equals("billing_addr_address_line1")))) {
				throw new ApplicationException("Column billing_addr_address_line1 is required");
			}
			if (entry.getValue().toString().equals("18") && (entry.getKey().toString().isEmpty()
					|| !(entry.getKey().toString().equals("billing_addr_state")))) {
				throw new ApplicationException("Column billing_addr_state is required");
			}
			if (entry.getValue().toString().equals("19") && (entry.getKey().toString().isEmpty()
					|| !(entry.getKey().toString().equals("billing_addr_city")))) {
				throw new ApplicationException("Column billing_addr_city is required");
			}

			if (entry.getValue().toString().equals("20") && (entry.getKey().toString().isEmpty()
					|| !(entry.getKey().toString().equals("billing_addr_pin")))) {
				throw new ApplicationException("Column billing_addr_pin is required");
			}

		}

		List<VendorUploadVo> vendors = new ArrayList<VendorUploadVo>();
		try {
			for (CSVRecord record : parser) {
				VendorUploadVo vendor = new VendorUploadVo();
				vendor.setPrimaryContact(record.get("primary_contact"));
				vendor.setCompanyName(record.get("company_name"));
				vendor.setVendorDisplayName(record.get("vendor_display_name"));
				vendor.setEmail(record.get("email"));
				vendor.setPaymentTerms(record.get("payment_terms"));
				vendor.setCurrency(record.get("currency"));
				vendor.setTds(record.get("tds"));
				vendor.setOriginAddressAttention(record.get("origin_addr_attention"));
				vendor.setOriginAddressPhone(record.get("origin_addr_phone"));
				vendor.setOriginAddressCountry(record.get("origin_addr_country"));
				vendor.setOriginAddressLine1(record.get("origin_addr_address_line1"));
				vendor.setOriginAddressState(record.get("origin_addr_state"));
				vendor.setOriginAddressCity(record.get("origin_addr_city"));
				vendor.setOriginAddressPin(record.get("origin_addr_pin"));
				vendor.setBillingAddressAttention(record.get("billing_addr_attention"));
				vendor.setBillingAddressPhone(record.get("billing_addr_phone"));
				vendor.setBillingAddressCountry(record.get("billing_addr_country"));
				vendor.setBillingAddressLine1(record.get("billing_addr_address_line1"));
				vendor.setBillingAddressState(record.get("billing_addr_state"));
				vendor.setBillingAddressCity(record.get("billing_addr_city"));
				vendor.setBillingAddressPin(record.get("billing_addr_pin"));
				vendors.add(vendor);
			}
			parser.close();
			filepath.delete();

		} catch (Exception e) {
			throw new ApplicationException(e.getMessage());
		}

		return vendors;
	}

	private List<EmployeeUploadVo> getEmployeexlsx(File filepath)
			throws InvalidFormatException, IOException, ApplicationException {
		logger.info("Entry into method: getEmployeexlsx");
		Workbook workbook;
		Sheet sheet = null;
		List<EmployeeUploadVo> employees = new ArrayList<EmployeeUploadVo>();
		workbook = WorkbookFactory.create(filepath);
		sheet = workbook.getSheetAt(0);
		int rowCount = 0;
		for (Row row : sheet) {
			if (rowCount == 0) {
				if ((row.getCell(0).toString().isEmpty()) || (!row.getCell(0).toString().equals("employee_id"))) {
					throw new ApplicationException("Column employee_id is required");
				}
				if ((row.getCell(1).toString().isEmpty()) || (!row.getCell(1).toString().equals("first_name"))) {
					throw new ApplicationException("Column first_name is required");
				}
				if ((row.getCell(2).toString().isEmpty()) || (!row.getCell(2).toString().equals("last_name"))) {
					throw new ApplicationException("Column last_name is required");
				}
				if ((row.getCell(3).toString().isEmpty()) || (!row.getCell(3).toString().equals("mobile_no"))) {
					throw new ApplicationException("Column mobile_no is required");
				}
				if ((row.getCell(4).toString().isEmpty()) || (!row.getCell(4).toString().equals("department_name"))) {
					throw new ApplicationException("Column department_name is required");
				}
				if ((row.getCell(5).toString().isEmpty()) || (!row.getCell(5).toString().equals("employee_status"))) {
					throw new ApplicationException("Column employee_status is required");
				}
				if ((row.getCell(6).toString().isEmpty()) || (!row.getCell(6).toString().equals("employee_type"))) {
					throw new ApplicationException("Column employee_type is required");
				}
			}
			if (rowCount > 0) {
				EmployeeUploadVo employee = new EmployeeUploadVo();
				employee.setEmployeeId(row.getCell(0).toString());
				employee.setFirstName(row.getCell(1).toString());
				employee.setLastName(row.getCell(2).toString());
				employee.setMobileNo(row.getCell(3).toString());
				employee.setDepartmentName(row.getCell(4).toString());
				employee.setEmployeeStatus(row.getCell(5).toString());
				employee.setEmployeeType(row.getCell(6).toString());
				employees.add(employee);
			}
			rowCount++;
		}
		filepath.delete();
		return employees;
	}

	private List<EmployeeUploadVo> getEmployeeCsv(File filepath)
			throws FileNotFoundException, IOException, ApplicationException {
		logger.info("Entry into method: getEmployeeCsv");
		CSVFormat format = CSVFormat.RFC4180.withHeader().withDelimiter(',');
		parser = new CSVParser(new FileReader(filepath), format);
		List<EmployeeUploadVo> employees = new ArrayList<EmployeeUploadVo>();
		Map<String, Integer> header = parser.getHeaderMap();

		for (Map.Entry<String, Integer> entry : header.entrySet()) {
			if (entry.getValue().toString().equals("0")
					&& (entry.getKey().toString().isEmpty() || !(entry.getKey().toString().equals("employee_id")))) {
				throw new ApplicationException("Column employee_id is required");
			}
			if (entry.getValue().toString().equals("1")
					&& (entry.getKey().toString().isEmpty() || !(entry.getKey().toString().equals("first_name")))) {
				throw new ApplicationException("Column first_name is required");
			}
			if (entry.getValue().toString().equals("2")
					&& (entry.getKey().toString().isEmpty() || !(entry.getKey().toString().equals("last_name")))) {
				throw new ApplicationException("Column last_name is required");
			}
			if (entry.getValue().toString().equals("3")
					&& (entry.getKey().toString().isEmpty() || !(entry.getKey().toString().equals("mobile_no")))) {
				throw new ApplicationException("Column mobile_no is required");
			}
			if (entry.getValue().toString().equals("4") && (entry.getKey().toString().isEmpty()
					|| !(entry.getKey().toString().equals("department_name")))) {
				throw new ApplicationException("Column department_name is required");
			}
			if (entry.getValue().toString().equals("5") && (entry.getKey().toString().isEmpty()
					|| !(entry.getKey().toString().equals("employee_status")))) {
				throw new ApplicationException("Column employee_status is required");
			}
			if (entry.getValue().toString().equals("6")
					&& (entry.getKey().toString().isEmpty() || !(entry.getKey().toString().equals("employee_type")))) {
				throw new ApplicationException("Column employee_type is required");
			}
		}
		for (CSVRecord record : parser) {
			EmployeeUploadVo employee = new EmployeeUploadVo();
			employee.setEmployeeId(record.get("employee_id"));
			employee.setFirstName(record.get("first_name"));
			employee.setLastName(record.get("last_name"));
			employee.setMobileNo(record.get("mobile_no"));
			employee.setDepartmentName(record.get("department_name"));
			employee.setEmployeeStatus(record.get("employee_status"));
			employee.setEmployeeType(record.get("employee_type"));
			employees.add(employee);
		}
		parser.close();
		filepath.delete();
		return employees;
	}
}

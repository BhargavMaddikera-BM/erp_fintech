package com.blackstrawai.export;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.externalintegration.yesbank.statement.BankStatementInfoVo;

@Service
public class ExportHelperService {

	private Logger logger = Logger.getLogger(ExportHelperService.class);

	public String exportEmployeeXlsx(List<EmployeeExportVo> data, String moduleName, String fileType)
			throws ApplicationException {
		logger.info("Entry into method: exportEmployeeXlsx");
		String encodedString = new String();
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet(moduleName);
		/** Header Row */
		Row headerRow = sheet.createRow(0);
		// Creating cells
		headerRow.createCell(0).setCellValue("id");
		headerRow.createCell(1).setCellValue("employee_id");
		headerRow.createCell(2).setCellValue("first_name");
		headerRow.createCell(3).setCellValue("last_name");
		headerRow.createCell(4).setCellValue("mobile_no");
		headerRow.createCell(5).setCellValue("department_name");
		headerRow.createCell(6).setCellValue("employee_status");
		headerRow.createCell(7).setCellValue("employee_type");
		headerRow.createCell(8).setCellValue("status");
		headerRow.createCell(9).setCellValue("bank_name");
		headerRow.createCell(10).setCellValue("branch_name");
		headerRow.createCell(11).setCellValue("account_holder_name");
		headerRow.createCell(12).setCellValue("account_no");
		headerRow.createCell(13).setCellValue("ifsc_code");
		headerRow.createCell(14).setCellValue("upi_id");
		headerRow.createCell(15).setCellValue("is_default");
		int rowNum = 1;

		/** Writing Data */
		for (EmployeeExportVo employee : data) {

			// Create ROW
			Row row = sheet.createRow(rowNum++);
			// Creating cells
			row.createCell(0).setCellValue(employee.getId());
			row.createCell(1).setCellValue(employee.getEmployeeId());
			row.createCell(2).setCellValue(employee.getFirstName());
			row.createCell(3).setCellValue(employee.getLastName());
			row.createCell(4).setCellValue(employee.getMobileNo());
			row.createCell(5).setCellValue(employee.getDepartmentName());
			row.createCell(6).setCellValue(employee.getEmployeeStatus());
			row.createCell(7).setCellValue(employee.getEmployeeType());
			row.createCell(8).setCellValue(employee.getStatus());
			if (employee.getBankDetails() != null && employee.getBankDetails().size() == 1) {
				BankExportVo employeeBank = employee.getBankDetails().get(0);
				row.createCell(9).setCellValue(employeeBank.getBankNname());
				row.createCell(10).setCellValue(employeeBank.getBranchName());
				row.createCell(11).setCellValue(employeeBank.getAccountHolderName());
				row.createCell(12).setCellValue(employeeBank.getAccountNo());
				row.createCell(13).setCellValue(employeeBank.getIfscCode());
				row.createCell(14).setCellValue(employeeBank.getUpiId());
				row.createCell(15).setCellValue(employeeBank.getIsDefault());
			} else {
				if (employee.getBankDetails() != null && employee.getBankDetails().size() > 0) {
					BankExportVo employeeBank = employee.getBankDetails().get(0);
					row.createCell(9).setCellValue(employeeBank.getBankNname());
					row.createCell(10).setCellValue(employeeBank.getBranchName());
					row.createCell(11).setCellValue(employeeBank.getAccountHolderName());
					row.createCell(12).setCellValue(employeeBank.getAccountNo());
					row.createCell(13).setCellValue(employeeBank.getIfscCode());
					row.createCell(14).setCellValue(employeeBank.getUpiId());
					row.createCell(15).setCellValue(employeeBank.getIsDefault());
					int count = 0;
					for (BankExportVo employeeBankDetails : employee.getBankDetails()) {
						if (count > 0) {
							row = sheet.createRow(rowNum++);
							row.createCell(0).setCellValue(employee.getId());
							row.createCell(1).setCellValue(employee.getEmployeeId());
							row.createCell(2).setCellValue(employee.getFirstName());
							row.createCell(3).setCellValue(employee.getLastName());
							row.createCell(4).setCellValue(employee.getMobileNo());
							row.createCell(5).setCellValue(employee.getDepartmentName());
							row.createCell(6).setCellValue(employee.getEmployeeStatus());
							row.createCell(7).setCellValue(employee.getEmployeeType());
							row.createCell(8).setCellValue(employee.getStatus());
							row.createCell(9).setCellValue(employeeBankDetails.getBankNname());
							row.createCell(10).setCellValue(employeeBankDetails.getBranchName());
							row.createCell(11).setCellValue(employeeBankDetails.getAccountHolderName());
							row.createCell(12).setCellValue(employeeBankDetails.getAccountNo());
							row.createCell(13).setCellValue(employeeBankDetails.getIfscCode());
							row.createCell(14).setCellValue(employeeBankDetails.getUpiId());
							row.createCell(15).setCellValue(employeeBankDetails.getIsDefault());
						}
						count++;
					}
				}
			}
		}
		encodedString = encodingXLSXFile(moduleName, fileType, workbook);
		return encodedString;

	}

	public String exportBankingBankStatement(List<BankStatementInfoVo> statmentList, String filetype) throws ApplicationException {
		logger.info("Entry into method: exportContraXlsx");
		String encodedString = new String();
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("Bank Statement");
		/** Header Row */
		Row headerRow = sheet.createRow(0);
		// Creating cells
		headerRow.createCell(0).setCellValue("Transaction Date");
		headerRow.createCell(1).setCellValue("Transaction Description");
		headerRow.createCell(2).setCellValue("Value Date");
		headerRow.createCell(3).setCellValue("Debit");
		headerRow.createCell(4).setCellValue("Credit");
		headerRow.createCell(5).setCellValue("Reference Cheque No");
		headerRow.createCell(6).setCellValue("Balance");
		int rowNum = 1;

		/** Writing Data */
		for (BankStatementInfoVo stmnt : statmentList) {

			// Create ROW
			Row row = sheet.createRow(rowNum++);
			// Creating cells
			row.createCell(0).setCellValue(stmnt.getTxnDate());
			row.createCell(1).setCellValue(stmnt.getTxnDesc());
			row.createCell(2).setCellValue(stmnt.getValueDate());
			row.createCell(3).setCellValue(stmnt.getAmtWithdrawal());
			row.createCell(4).setCellValue(stmnt.getAmtDeposit());
			row.createCell(5).setCellValue(stmnt.getRefChqNum());
			row.createCell(6).setCellValue(stmnt.getBalance());
			}
		encodedString = encodingXLSXFile("Bank Statement", filetype, workbook);
		return encodedString;

	}


	private String encodingXLSXFile(String moduleName, String fileType, XSSFWorkbook workbook)
			throws ApplicationException {
		logger.info("Entry into method: encodingXLSXFile");
		String encodedString = new String();
		try {
			FileReader reader;
			reader = new FileReader("/decifer/config/app_config.properties");
			Properties p = new Properties();
			p.load(reader);
			StringBuilder path = new StringBuilder(p.getProperty("file_store_path"));
			String pathtoSave = path.toString();
			File filePath = new File(pathtoSave + "/" + moduleName + "." + fileType);
			FileOutputStream outputStream = new FileOutputStream(filePath);
			workbook.write(outputStream);
			workbook.close();
			String exportPath = filePath.toString();
			byte[] fileBytes = Files.readAllBytes(new File(exportPath).toPath());
			if (fileBytes.length > 0) {
				logger.info("To encode the bytes  ::");
				encodedString = Base64.getEncoder().encodeToString(fileBytes);
				// filePath.delete();
			}
		} catch (FileNotFoundException e) {
			throw new ApplicationException(e);
		} catch (IOException e) {
			throw new ApplicationException(e);
		}
		return encodedString;

	}

	public String exportEmployeeCsv(List<EmployeeExportVo> data, String moduleName, String fileType)
			throws ApplicationException {
		logger.info("Entry into method: exportEmployeeCsv");
		FileWriter fileWriter = null;
		File filePath;
		String encodedString = new String();
		final String COMMA_DELIMITER = ",";
		final String NEW_LINE_SEPARATOR = "\n";
		final String EMPLOYEE_FILE_HEADER = "id,employee_id,first_name,last_name,mobile_no,department_name,employee_status,employee_type,status,bank_name,branch_name,account_holder_name,account_no,ifsc_code,upi_id,is_default";
		try {
			FileReader reader;
			reader = new FileReader("/decifer/config/app_config.properties");
			Properties p = new Properties();
			p.load(reader);
			StringBuilder path = new StringBuilder(p.getProperty("file_store_path"));
			String pathtoSave = path.toString();
			filePath = new File(pathtoSave + "/" + moduleName + "." + fileType);

			fileWriter = new FileWriter(filePath);

			// Write the CSV file header
			fileWriter.append(EMPLOYEE_FILE_HEADER.toString());

			// Add a new line separator after the header
			fileWriter.append(NEW_LINE_SEPARATOR);

			for (EmployeeExportVo employee : data) {
				fileWriter.append(String.valueOf(employee.getId()));
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(employee.getEmployeeId());
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(employee.getFirstName() != null ? employee.getFirstName() : "");
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(employee.getLastName() != null ? employee.getLastName() : "");
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(employee.getMobileNo() != null ? employee.getMobileNo() : "");
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(employee.getDepartmentName() != null ? employee.getDepartmentName() : "");
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(employee.getEmployeeStatus() != null ? employee.getEmployeeStatus() : "");
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(employee.getEmployeeType() != null ? employee.getEmployeeType() : "");
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(employee.getStatus() != null ? employee.getStatus() : "");
				if (employee.getBankDetails().size() == 0) {
					fileWriter.append(COMMA_DELIMITER);
					fileWriter.append(COMMA_DELIMITER);
					fileWriter.append(COMMA_DELIMITER);
					fileWriter.append(COMMA_DELIMITER);
					fileWriter.append(COMMA_DELIMITER);
					fileWriter.append(COMMA_DELIMITER);
					fileWriter.append(COMMA_DELIMITER);
				}
				if (employee.getBankDetails() != null && employee.getBankDetails().size() == 1) {
					BankExportVo employeeBank = employee.getBankDetails().get(0);
					fileWriter.append(COMMA_DELIMITER);
					fileWriter.append(employeeBank.getBankNname() != null ? employeeBank.getBankNname() : "");
					fileWriter.append(COMMA_DELIMITER);
					fileWriter.append(employeeBank.getBranchName() != null ? employeeBank.getBranchName() : "");
					fileWriter.append(COMMA_DELIMITER);
					fileWriter.append(
							employeeBank.getAccountHolderName() != null ? employeeBank.getAccountHolderName() : "");
					fileWriter.append(COMMA_DELIMITER);
					fileWriter.append(employeeBank.getAccountNo() != null ? employeeBank.getAccountNo() : "");
					fileWriter.append(COMMA_DELIMITER);
					fileWriter.append(employeeBank.getIfscCode() != null ? employeeBank.getIfscCode() : "");
					fileWriter.append(COMMA_DELIMITER);
					fileWriter.append(employeeBank.getUpiId() != null ? employeeBank.getUpiId() : "");
					fileWriter.append(COMMA_DELIMITER);
					fileWriter.append(Boolean.toString(employeeBank.getIsDefault()));
					fileWriter.append(NEW_LINE_SEPARATOR);
				} else {
					if (employee.getBankDetails() != null && employee.getBankDetails().size() > 0) {
						BankExportVo employeeBank = employee.getBankDetails().get(0);
						fileWriter.append(COMMA_DELIMITER);
						fileWriter.append(employeeBank.getBankNname() != null ? employeeBank.getBankNname() : "");
						fileWriter.append(COMMA_DELIMITER);
						fileWriter.append(employeeBank.getBranchName() != null ? employeeBank.getBranchName() : "");
						fileWriter.append(COMMA_DELIMITER);
						fileWriter.append(
								employeeBank.getAccountHolderName() != null ? employeeBank.getAccountHolderName() : "");
						fileWriter.append(COMMA_DELIMITER);
						fileWriter.append(employeeBank.getAccountNo() != null ? employeeBank.getAccountNo() : "");
						fileWriter.append(COMMA_DELIMITER);
						fileWriter.append(employeeBank.getIfscCode() != null ? employeeBank.getIfscCode() : "");
						fileWriter.append(COMMA_DELIMITER);
						fileWriter.append(employeeBank.getUpiId() != null ? employeeBank.getUpiId() : "");
						fileWriter.append(COMMA_DELIMITER);
						fileWriter.append(Boolean.toString(employeeBank.getIsDefault()));
						int count = 0;
						for (BankExportVo employeeBankDetails : employee.getBankDetails()) {
							if (count > 0) {
								fileWriter.append(NEW_LINE_SEPARATOR);
								fileWriter.append(String.valueOf(employee.getId()));
								fileWriter.append(COMMA_DELIMITER);
								fileWriter.append(employee.getEmployeeId());
								fileWriter.append(COMMA_DELIMITER);
								fileWriter.append(employee.getFirstName() != null ? employee.getFirstName() : "");
								fileWriter.append(COMMA_DELIMITER);
								fileWriter.append(employee.getLastName() != null ? employee.getLastName() : "");
								fileWriter.append(COMMA_DELIMITER);
								fileWriter.append(employee.getMobileNo() != null ? employee.getMobileNo() : "");
								fileWriter.append(COMMA_DELIMITER);
								fileWriter.append(
										employee.getDepartmentName() != null ? employee.getDepartmentName() : "");
								fileWriter.append(COMMA_DELIMITER);
								fileWriter.append(
										employee.getEmployeeStatus() != null ? employee.getEmployeeStatus() : "");
								fileWriter.append(COMMA_DELIMITER);
								fileWriter.append(employee.getEmployeeType() != null ? employee.getEmployeeType() : "");
								fileWriter.append(COMMA_DELIMITER);
								fileWriter.append(employee.getStatus() != null ? employee.getStatus() : "");
								fileWriter.append(COMMA_DELIMITER);
								fileWriter.append(
										employeeBankDetails.getBankNname() != null ? employeeBankDetails.getBankNname()
												: "");
								fileWriter.append(COMMA_DELIMITER);
								fileWriter.append(employeeBankDetails.getBranchName() != null
										? employeeBankDetails.getBranchName()
										: "");
								fileWriter.append(COMMA_DELIMITER);
								fileWriter.append(employeeBankDetails.getAccountHolderName() != null
										? employeeBankDetails.getAccountHolderName()
										: "");
								fileWriter.append(COMMA_DELIMITER);
								fileWriter.append(
										employeeBankDetails.getAccountNo() != null ? employeeBankDetails.getAccountNo()
												: "");
								fileWriter.append(COMMA_DELIMITER);
								fileWriter.append(
										employeeBankDetails.getIfscCode() != null ? employeeBankDetails.getIfscCode()
												: "");
								fileWriter.append(COMMA_DELIMITER);
								fileWriter.append(
										employeeBankDetails.getUpiId() != null ? employeeBankDetails.getUpiId() : "");
								fileWriter.append(COMMA_DELIMITER);
								fileWriter.append(Boolean.toString(employeeBankDetails.getIsDefault()));
							}
							count++;
						}
					}
				}
				fileWriter.append(NEW_LINE_SEPARATOR);
			}
		} catch (Exception e) {
			System.out.println("Error in CsvFileWriter !!!");
			throw new ApplicationException(e);
		} finally {
			try {
				fileWriter.flush();
				fileWriter.close();
			} catch (IOException e) {
				System.out.println("Error while flushing/closing fileWriter !!!");
				throw new ApplicationException(e);
			}

		}

		String exportPath = filePath.toString();
		byte[] fileBytes = null;
		try {
			fileBytes = Files.readAllBytes(new File(exportPath).toPath());
		} catch (IOException e) {
			throw new ApplicationException(e);
		}
		if (fileBytes.length > 0) {
			logger.info("To encode the bytes  ::");
			encodedString = Base64.getEncoder().encodeToString(fileBytes);
			filePath.delete();
		}

		return encodedString;
	}

	public String exportVendorXlsx(List<VendorExportVo> data, String moduleName, String fileType)
			throws ApplicationException {
		logger.info("Entry into method: exportVendorXlsx");
		String encodedString = new String();
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet(moduleName);
		/** Header Row */
		Row headerRow = sheet.createRow(0);
		// Creating cells
		headerRow.createCell(0).setCellValue("id");
		headerRow.createCell(1).setCellValue("primary_contact");
		headerRow.createCell(2).setCellValue("company_name");
		headerRow.createCell(3).setCellValue("vendor_display_name");
		headerRow.createCell(4).setCellValue("email");
		headerRow.createCell(5).setCellValue("phone");
		headerRow.createCell(6).setCellValue("mobile_no");
		headerRow.createCell(7).setCellValue("pan");
		headerRow.createCell(8).setCellValue("organization_type");
		headerRow.createCell(9).setCellValue("vendor_group");
		headerRow.createCell(10).setCellValue("gst_regn_type");
		headerRow.createCell(11).setCellValue("gst_number");
		headerRow.createCell(12).setCellValue("payment_terms");
		headerRow.createCell(13).setCellValue("currency");
		headerRow.createCell(14).setCellValue("opening_balance");
		headerRow.createCell(15).setCellValue("tds");
		headerRow.createCell(16).setCellValue("origin_addr_attention");
		headerRow.createCell(17).setCellValue("origin_addr_phone");
		headerRow.createCell(18).setCellValue("origin_addr_country");
		headerRow.createCell(19).setCellValue("origin_addr_line1");
		headerRow.createCell(20).setCellValue("origin_addr_line2");
		headerRow.createCell(21).setCellValue("origin_addr_landmark");
		headerRow.createCell(22).setCellValue("origin_addr_state");
		headerRow.createCell(23).setCellValue("origin_addr_city");
		headerRow.createCell(24).setCellValue("origin_addr_pin_code");
		headerRow.createCell(25).setCellValue("billing_addr_attention");
		headerRow.createCell(26).setCellValue("billing_addr_phone");
		headerRow.createCell(27).setCellValue("billing_addr_country");
		headerRow.createCell(28).setCellValue("billing_addr_line1");
		headerRow.createCell(29).setCellValue("billing_addr_line2");
		headerRow.createCell(30).setCellValue("billing_addr_landmark");
		headerRow.createCell(31).setCellValue("billing_addr_state");
		headerRow.createCell(32).setCellValue("billing_addr_city");
		headerRow.createCell(33).setCellValue("billing_addr_pin_code");
		headerRow.createCell(34).setCellValue("status");
		headerRow.createCell(35).setCellValue("contact_salutation");
		headerRow.createCell(36).setCellValue("contact_first_name");
		headerRow.createCell(37).setCellValue("contact_last_name");
		headerRow.createCell(38).setCellValue("contact_work_no");
		headerRow.createCell(39).setCellValue("contact_mobile_no");
		headerRow.createCell(40).setCellValue("contact_email_address");
		headerRow.createCell(41).setCellValue("bank_name");
		headerRow.createCell(42).setCellValue("branch_name");
		headerRow.createCell(43).setCellValue("account_holder_name");
		headerRow.createCell(44).setCellValue("account_no");
		headerRow.createCell(45).setCellValue("ifsc_code");
		headerRow.createCell(46).setCellValue("upi_id");
		headerRow.createCell(47).setCellValue("is_default");
		int rowNum = 1;

		/** Writing Data */
		for (VendorExportVo vendor : data) {

			// Create ROW
			Row row = sheet.createRow(rowNum++);
			// Creating cells
			row.createCell(0).setCellValue(vendor.getId());
			row.createCell(1).setCellValue(vendor.getPrimaryContact());
			row.createCell(2).setCellValue(vendor.getCompanyName());
			row.createCell(3).setCellValue(vendor.getVendorDisplayName());
			row.createCell(4).setCellValue(vendor.getEmail());
			row.createCell(5).setCellValue(vendor.getPhone());
			row.createCell(6).setCellValue(vendor.getMobileNo());
			row.createCell(7).setCellValue(vendor.getPan());
			row.createCell(8).setCellValue(vendor.getOrganizationType());
			row.createCell(9).setCellValue(vendor.getVendorGroup());
			row.createCell(10).setCellValue(vendor.getGstRegnType());
			row.createCell(11).setCellValue(vendor.getGstNumber());
			row.createCell(12).setCellValue(vendor.getPaymentTerms());
			row.createCell(13).setCellValue(vendor.getCurrency());
			row.createCell(14).setCellValue(vendor.getOpeningBalance());
			row.createCell(15).setCellValue(vendor.getTds());
			row.createCell(16).setCellValue(vendor.getOriginAddrAttention());
			row.createCell(17).setCellValue(vendor.getOriginAddrPhone());
			row.createCell(18).setCellValue(vendor.getOriginAddrCountry());
			row.createCell(19).setCellValue(vendor.getOriginAddrLine1());
			row.createCell(20).setCellValue(vendor.getOriginAddrLine2());
			row.createCell(21).setCellValue(vendor.getOriginAddrLandmark());
			row.createCell(22).setCellValue(vendor.getOriginAddrState());
			row.createCell(23).setCellValue(vendor.getOriginAddrCity());
			row.createCell(24).setCellValue(vendor.getOriginAddrPinCode());
			row.createCell(25).setCellValue(vendor.getBillingAddrAttention());
			row.createCell(26).setCellValue(vendor.getBillingAddrPhone());
			row.createCell(27).setCellValue(vendor.getBillingAddrCountry());
			row.createCell(28).setCellValue(vendor.getBillingAddrLine1());
			row.createCell(29).setCellValue(vendor.getBillingAddrLine2());
			row.createCell(30).setCellValue(vendor.getBillingAddrLandmark());
			row.createCell(31).setCellValue(vendor.getBillingAddrState());
			row.createCell(32).setCellValue(vendor.getBillingAddrCity());
			row.createCell(33).setCellValue(vendor.getBillingAddrPinCode());
			row.createCell(34).setCellValue(vendor.getStatus());
			if (vendor.getContacts() != null && vendor.getContacts().size() == 1) {
				ContactsExportVo vendorContact = vendor.getContacts().get(0);
				row.createCell(35).setCellValue(vendorContact.getContactSalutation());
				row.createCell(36).setCellValue(vendorContact.getContactFirstName());
				row.createCell(37).setCellValue(vendorContact.getContactLastName());
				row.createCell(38).setCellValue(vendorContact.getContactWorkNo());
				row.createCell(39).setCellValue(vendorContact.getContactMobileNo());
				row.createCell(40).setCellValue(vendorContact.getContactEmailAddress());
			} else {
				if (vendor.getContacts() != null && vendor.getContacts().size() > 0) {
					ContactsExportVo vendorContact = vendor.getContacts().get(0);
					row.createCell(35).setCellValue(vendorContact.getContactSalutation());
					row.createCell(36).setCellValue(vendorContact.getContactFirstName());
					row.createCell(37).setCellValue(vendorContact.getContactLastName());
					row.createCell(38).setCellValue(vendorContact.getContactWorkNo());
					row.createCell(39).setCellValue(vendorContact.getContactMobileNo());
					row.createCell(40).setCellValue(vendorContact.getContactEmailAddress());
					int count = 0;
					for (ContactsExportVo vendorcontactDetails : vendor.getContacts()) {
						if (count > 0) {
							row = sheet.createRow(rowNum++);
							row.createCell(0).setCellValue(vendor.getId());
							row.createCell(1).setCellValue(vendor.getPrimaryContact());
							row.createCell(2).setCellValue(vendor.getCompanyName());
							row.createCell(3).setCellValue(vendor.getVendorDisplayName());
							row.createCell(4).setCellValue(vendor.getEmail());
							row.createCell(5).setCellValue(vendor.getPhone());
							row.createCell(6).setCellValue(vendor.getMobileNo());
							row.createCell(7).setCellValue(vendor.getPan());
							row.createCell(8).setCellValue(vendor.getOrganizationType());
							row.createCell(9).setCellValue(vendor.getVendorGroup());
							row.createCell(10).setCellValue(vendor.getGstRegnType());
							row.createCell(11).setCellValue(vendor.getGstNumber());
							row.createCell(12).setCellValue(vendor.getPaymentTerms());
							row.createCell(13).setCellValue(vendor.getCurrency());
							row.createCell(14).setCellValue(vendor.getOpeningBalance());
							row.createCell(15).setCellValue(vendor.getTds());
							row.createCell(16).setCellValue(vendor.getOriginAddrAttention());
							row.createCell(17).setCellValue(vendor.getOriginAddrPhone());
							row.createCell(18).setCellValue(vendor.getOriginAddrCountry());
							row.createCell(19).setCellValue(vendor.getOriginAddrLine1());
							row.createCell(20).setCellValue(vendor.getOriginAddrLine2());
							row.createCell(21).setCellValue(vendor.getOriginAddrLandmark());
							row.createCell(22).setCellValue(vendor.getOriginAddrState());
							row.createCell(23).setCellValue(vendor.getOriginAddrCity());
							row.createCell(24).setCellValue(vendor.getOriginAddrPinCode());
							row.createCell(25).setCellValue(vendor.getBillingAddrAttention());
							row.createCell(26).setCellValue(vendor.getBillingAddrPhone());
							row.createCell(27).setCellValue(vendor.getBillingAddrCountry());
							row.createCell(28).setCellValue(vendor.getBillingAddrLine1());
							row.createCell(29).setCellValue(vendor.getBillingAddrLine2());
							row.createCell(30).setCellValue(vendor.getBillingAddrLandmark());
							row.createCell(31).setCellValue(vendor.getBillingAddrState());
							row.createCell(32).setCellValue(vendor.getBillingAddrCity());
							row.createCell(33).setCellValue(vendor.getBillingAddrPinCode());
							row.createCell(34).setCellValue(vendor.getStatus());
							row.createCell(35).setCellValue(vendorcontactDetails.getContactSalutation());
							row.createCell(36).setCellValue(vendorcontactDetails.getContactFirstName());
							row.createCell(37).setCellValue(vendorcontactDetails.getContactLastName());
							row.createCell(38).setCellValue(vendorcontactDetails.getContactWorkNo());
							row.createCell(39).setCellValue(vendorcontactDetails.getContactMobileNo());
							row.createCell(40).setCellValue(vendorcontactDetails.getContactEmailAddress());
						}
						count++;
					}
				}
			}
			if (vendor.getContacts().size() == 0 && vendor.getContacts().isEmpty()) {
				if (vendor.getBankDetails() != null && vendor.getBankDetails().size() == 1) {
					BankExportVo vendorBank = vendor.getBankDetails().get(0);
					row.createCell(41).setCellValue(vendorBank.getBankNname());
					row.createCell(42).setCellValue(vendorBank.getBranchName());
					row.createCell(43).setCellValue(vendorBank.getAccountHolderName());
					row.createCell(44).setCellValue(vendorBank.getAccountNo());
					row.createCell(45).setCellValue(vendorBank.getIfscCode());
					row.createCell(46).setCellValue(vendorBank.getUpiId());
					row.createCell(47).setCellValue(vendorBank.getIsDefault());
				} else {
					if (vendor.getBankDetails() != null && vendor.getBankDetails().size() > 0) {
						BankExportVo vendorBank = vendor.getBankDetails().get(0);
						row.createCell(41).setCellValue(vendorBank.getBankNname());
						row.createCell(42).setCellValue(vendorBank.getBranchName());
						row.createCell(43).setCellValue(vendorBank.getAccountHolderName());
						row.createCell(44).setCellValue(vendorBank.getAccountNo());
						row.createCell(45).setCellValue(vendorBank.getIfscCode());
						row.createCell(46).setCellValue(vendorBank.getUpiId());
						row.createCell(47).setCellValue(vendorBank.getIsDefault());
						int count = 0;
						for (BankExportVo vendorBankDetails : vendor.getBankDetails()) {
							if (count > 0) {
								row = sheet.createRow(rowNum++);
								row.createCell(0).setCellValue(vendor.getId());
								row.createCell(1).setCellValue(vendor.getPrimaryContact());
								row.createCell(2).setCellValue(vendor.getCompanyName());
								row.createCell(3).setCellValue(vendor.getVendorDisplayName());
								row.createCell(4).setCellValue(vendor.getEmail());
								row.createCell(5).setCellValue(vendor.getPhone());
								row.createCell(6).setCellValue(vendor.getMobileNo());
								row.createCell(7).setCellValue(vendor.getPan());
								row.createCell(8).setCellValue(vendor.getOrganizationType());
								row.createCell(9).setCellValue(vendor.getVendorGroup());
								row.createCell(10).setCellValue(vendor.getGstRegnType());
								row.createCell(11).setCellValue(vendor.getGstNumber());
								row.createCell(12).setCellValue(vendor.getPaymentTerms());
								row.createCell(13).setCellValue(vendor.getCurrency());
								row.createCell(14).setCellValue(vendor.getOpeningBalance());
								row.createCell(15).setCellValue(vendor.getTds());
								row.createCell(16).setCellValue(vendor.getOriginAddrAttention());
								row.createCell(17).setCellValue(vendor.getOriginAddrPhone());
								row.createCell(18).setCellValue(vendor.getOriginAddrCountry());
								row.createCell(19).setCellValue(vendor.getOriginAddrLine1());
								row.createCell(20).setCellValue(vendor.getOriginAddrLine2());
								row.createCell(21).setCellValue(vendor.getOriginAddrLandmark());
								row.createCell(22).setCellValue(vendor.getOriginAddrState());
								row.createCell(23).setCellValue(vendor.getOriginAddrCity());
								row.createCell(24).setCellValue(vendor.getOriginAddrPinCode());
								row.createCell(25).setCellValue(vendor.getBillingAddrAttention());
								row.createCell(26).setCellValue(vendor.getBillingAddrPhone());
								row.createCell(27).setCellValue(vendor.getBillingAddrCountry());
								row.createCell(28).setCellValue(vendor.getBillingAddrLine1());
								row.createCell(29).setCellValue(vendor.getBillingAddrLine2());
								row.createCell(30).setCellValue(vendor.getBillingAddrLandmark());
								row.createCell(31).setCellValue(vendor.getBillingAddrState());
								row.createCell(32).setCellValue(vendor.getBillingAddrCity());
								row.createCell(33).setCellValue(vendor.getBillingAddrPinCode());
								row.createCell(34).setCellValue(vendor.getStatus());
								row.createCell(41).setCellValue(vendorBankDetails.getBankNname());
								row.createCell(42).setCellValue(vendorBankDetails.getBranchName());
								row.createCell(43).setCellValue(vendorBankDetails.getAccountHolderName());
								row.createCell(44).setCellValue(vendorBankDetails.getAccountNo());
								row.createCell(45).setCellValue(vendorBankDetails.getIfscCode());
								row.createCell(46).setCellValue(vendorBankDetails.getUpiId());
								row.createCell(47).setCellValue(vendorBankDetails.getIsDefault());
							}
							count++;
						}
					}
				}

			} else {
				if (vendor.getBankDetails() != null && vendor.getBankDetails().size() > 0) {
					for (BankExportVo vendroBankDetails : vendor.getBankDetails()) {
						row = sheet.createRow(rowNum++);
						row.createCell(0).setCellValue(vendor.getId());
						row.createCell(1).setCellValue(vendor.getPrimaryContact());
						row.createCell(2).setCellValue(vendor.getCompanyName());
						row.createCell(3).setCellValue(vendor.getVendorDisplayName());
						row.createCell(4).setCellValue(vendor.getEmail());
						row.createCell(5).setCellValue(vendor.getPhone());
						row.createCell(6).setCellValue(vendor.getMobileNo());
						row.createCell(7).setCellValue(vendor.getPan());
						row.createCell(8).setCellValue(vendor.getOrganizationType());
						row.createCell(9).setCellValue(vendor.getVendorGroup());
						row.createCell(10).setCellValue(vendor.getGstRegnType());
						row.createCell(11).setCellValue(vendor.getGstNumber());
						row.createCell(12).setCellValue(vendor.getPaymentTerms());
						row.createCell(13).setCellValue(vendor.getCurrency());
						row.createCell(14).setCellValue(vendor.getOpeningBalance());
						row.createCell(15).setCellValue(vendor.getTds());
						row.createCell(16).setCellValue(vendor.getOriginAddrAttention());
						row.createCell(17).setCellValue(vendor.getOriginAddrPhone());
						row.createCell(18).setCellValue(vendor.getOriginAddrCountry());
						row.createCell(19).setCellValue(vendor.getOriginAddrLine1());
						row.createCell(20).setCellValue(vendor.getOriginAddrLine2());
						row.createCell(21).setCellValue(vendor.getOriginAddrLandmark());
						row.createCell(22).setCellValue(vendor.getOriginAddrState());
						row.createCell(23).setCellValue(vendor.getOriginAddrCity());
						row.createCell(24).setCellValue(vendor.getOriginAddrPinCode());
						row.createCell(25).setCellValue(vendor.getBillingAddrAttention());
						row.createCell(26).setCellValue(vendor.getBillingAddrPhone());
						row.createCell(27).setCellValue(vendor.getBillingAddrCountry());
						row.createCell(28).setCellValue(vendor.getBillingAddrLine1());
						row.createCell(29).setCellValue(vendor.getBillingAddrLine2());
						row.createCell(30).setCellValue(vendor.getBillingAddrLandmark());
						row.createCell(31).setCellValue(vendor.getBillingAddrState());
						row.createCell(32).setCellValue(vendor.getBillingAddrCity());
						row.createCell(33).setCellValue(vendor.getBillingAddrPinCode());
						row.createCell(34).setCellValue(vendor.getStatus());
						row.createCell(41).setCellValue(vendroBankDetails.getBankNname());
						row.createCell(42).setCellValue(vendroBankDetails.getBranchName());
						row.createCell(43).setCellValue(vendroBankDetails.getAccountHolderName());
						row.createCell(44).setCellValue(vendroBankDetails.getAccountNo());
						row.createCell(45).setCellValue(vendroBankDetails.getIfscCode());
						row.createCell(46).setCellValue(vendroBankDetails.getUpiId());
						row.createCell(47).setCellValue(vendroBankDetails.getIsDefault());

					}
				}
			}

		}

		encodedString = encodingXLSXFile(moduleName, fileType, workbook);
		return encodedString;
	}

	public String exportVendorCsv(List<VendorExportVo> data, String moduleName, String fileType)
			throws ApplicationException {
		logger.info("Entry into method: exportVendorCsv");
		final String VENDOR_FILE_HEADER = "id,primary_contact,company_name,vendor_display_name,email,phone,mobile_no,pan,organization_type,vendor_group,gst_regn_type,gst_number,payment_terms,currency,opening_balance,tds,origin_addr_attention,origin_addr_phone,origin_addr_country,origin_addr_line1,origin_addr_line2,origin_addr_landmark,origin_addr_state,origin_addr_city,origin_addr_pin_code,billing_addr_attention,billing_addr_phone,billing_addr_country,billing_addr_line1,billing_addr_line2,billing_addr_landmark,billing_addr_state,billing_addr_city,billing_addr_pin_code,status,contact_salutation,contact_first_name,contact_last_name,contact_work_no,contact_mobile_no,contact_email_address,bank_name,branch_name,account_holder_name,account_no,ifsc_code,upi_id,is_default";
		final String COMMA_DELIMITER = ",";
		final String NEW_LINE_SEPARATOR = "\n";
		FileWriter fileWriter = null;
		File filePath;
		String encodedString = new String();
		try {
			FileReader reader;
			reader = new FileReader("/decifer/config/app_config.properties");
			Properties p = new Properties();
			p.load(reader);
			StringBuilder path = new StringBuilder(p.getProperty("file_store_path"));
			String pathtoSave = path.toString();
			filePath = new File(pathtoSave + "/" + moduleName + "." + fileType);

			fileWriter = new FileWriter(filePath);

			// Write the CSV file header
			fileWriter.append(VENDOR_FILE_HEADER.toString());

			// Add a new line separator after the header
			fileWriter.append(NEW_LINE_SEPARATOR);
			for (VendorExportVo vendor : data) {
				fileWriter.append(String.valueOf(vendor.getId()));
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(vendor.getPrimaryContact() != null ? vendor.getPrimaryContact() : "");
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(vendor.getCompanyName() != null ? vendor.getCompanyName() : "");
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(vendor.getVendorDisplayName() != null ? vendor.getVendorDisplayName() : "");
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(vendor.getEmail() != null ? vendor.getEmail() : "");
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(vendor.getPhone() != null ? vendor.getPhone() : "");
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(vendor.getMobileNo() != null ? vendor.getMobileNo() : "");
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(vendor.getPan() != null ? vendor.getPan() : "");
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(vendor.getOrganizationType() != null ? vendor.getOrganizationType() : "");
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(vendor.getVendorGroup() != null ? vendor.getVendorGroup() : "");
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(vendor.getGstRegnType() != null ? vendor.getGstRegnType() : "");
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(vendor.getGstNumber() != null ? vendor.getGstNumber() : "");
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(vendor.getPaymentTerms() != null ? vendor.getPaymentTerms() : "");
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(vendor.getCurrency() != null ? vendor.getCurrency() : "");
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(vendor.getOpeningBalance() != null ? vendor.getOpeningBalance() : "");
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(vendor.getTds() != null ? vendor.getTds() : "");
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(vendor.getOriginAddrAttention() != null ? vendor.getOriginAddrAttention() : "");
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(vendor.getOriginAddrPhone() != null ? vendor.getOriginAddrPhone() : "");
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(vendor.getOriginAddrCountry() != null ? vendor.getOriginAddrCountry() : "");
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(vendor.getOriginAddrLine1() != null ? vendor.getOriginAddrLine1() : "");
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(vendor.getOriginAddrLine2() != null ? vendor.getOriginAddrLine2() : "");
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(vendor.getOriginAddrLandmark() != null ? vendor.getOriginAddrLandmark() : "");
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(vendor.getOriginAddrState() != null ? vendor.getOriginAddrState() : "");
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(vendor.getOriginAddrCity() != null ? vendor.getOriginAddrCity() : "");
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(vendor.getOriginAddrPinCode() != null ? vendor.getOriginAddrPinCode() : "");
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(vendor.getBillingAddrAttention() != null ? vendor.getBillingAddrAttention() : "");
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(vendor.getBillingAddrPhone() != null ? vendor.getBillingAddrPhone() : "");
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(vendor.getBillingAddrCountry() != null ? vendor.getBillingAddrCountry() : "");
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(vendor.getBillingAddrLine1() != null ? vendor.getBillingAddrLine1() : "");
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(vendor.getBillingAddrLine2() != null ? vendor.getBillingAddrLine2() : "");
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(vendor.getBillingAddrLandmark() != null ? vendor.getBillingAddrLandmark() : "");
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(vendor.getBillingAddrState() != null ? vendor.getBillingAddrState() : "");
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(vendor.getBillingAddrCity() != null ? vendor.getBillingAddrCity() : "");
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(vendor.getBillingAddrPinCode() != null ? vendor.getBillingAddrPinCode() : "");
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(vendor.getStatus() != null ? vendor.getStatus() : "");
				if (vendor.getContacts().size() == 0) {
					fileWriter.append(COMMA_DELIMITER);
					fileWriter.append(COMMA_DELIMITER);
					fileWriter.append(COMMA_DELIMITER);
					fileWriter.append(COMMA_DELIMITER);
					fileWriter.append(COMMA_DELIMITER);
					fileWriter.append(COMMA_DELIMITER);
				}
				if (vendor.getContacts() != null && vendor.getContacts().size() == 1) {
					ContactsExportVo vendorContact = vendor.getContacts().get(0);
					fileWriter.append(COMMA_DELIMITER);
					fileWriter.append(
							vendorContact.getContactSalutation() != null ? vendorContact.getContactSalutation() : "");
					fileWriter.append(COMMA_DELIMITER);
					fileWriter.append(
							vendorContact.getContactFirstName() != null ? vendorContact.getContactFirstName() : "");
					fileWriter.append(COMMA_DELIMITER);
					fileWriter.append(
							vendorContact.getContactLastName() != null ? vendorContact.getContactLastName() : "");
					fileWriter.append(COMMA_DELIMITER);
					fileWriter.append(vendorContact.getContactWorkNo() != null ? vendorContact.getContactWorkNo() : "");
					fileWriter.append(COMMA_DELIMITER);
					fileWriter.append(
							vendorContact.getContactMobileNo() != null ? vendorContact.getContactMobileNo() : "");
					fileWriter.append(COMMA_DELIMITER);
					fileWriter.append(
							vendorContact.getContactEmailAddress() != null ? vendorContact.getContactEmailAddress()
									: "");
				} else {
					if (vendor.getContacts() != null && vendor.getContacts().size() > 0) {
						ContactsExportVo vendorContact = vendor.getContacts().get(0);
						fileWriter.append(COMMA_DELIMITER);
						fileWriter.append(
								vendorContact.getContactSalutation() != null ? vendorContact.getContactSalutation()
										: "");
						fileWriter.append(COMMA_DELIMITER);
						fileWriter.append(
								vendorContact.getContactFirstName() != null ? vendorContact.getContactFirstName() : "");
						fileWriter.append(COMMA_DELIMITER);
						fileWriter.append(
								vendorContact.getContactLastName() != null ? vendorContact.getContactLastName() : "");
						fileWriter.append(COMMA_DELIMITER);
						fileWriter.append(
								vendorContact.getContactWorkNo() != null ? vendorContact.getContactWorkNo() : "");
						fileWriter.append(COMMA_DELIMITER);
						fileWriter.append(
								vendorContact.getContactMobileNo() != null ? vendorContact.getContactMobileNo() : "");
						fileWriter.append(COMMA_DELIMITER);
						fileWriter.append(
								vendorContact.getContactEmailAddress() != null ? vendorContact.getContactEmailAddress()
										: "");
						int count = 0;
						for (ContactsExportVo vendorContactDetails : vendor.getContacts()) {
							if (count > 0) {
								fileWriter.append(NEW_LINE_SEPARATOR);
								fileWriter.append(String.valueOf(vendor.getId()));
								fileWriter.append(COMMA_DELIMITER);
								fileWriter.append(vendor.getPrimaryContact() != null ? vendor.getPrimaryContact() : "");
								fileWriter.append(COMMA_DELIMITER);
								fileWriter.append(vendor.getCompanyName() != null ? vendor.getCompanyName() : "");
								fileWriter.append(COMMA_DELIMITER);
								fileWriter.append(
										vendor.getVendorDisplayName() != null ? vendor.getVendorDisplayName() : "");
								fileWriter.append(COMMA_DELIMITER);
								fileWriter.append(vendor.getEmail() != null ? vendor.getEmail() : "");
								fileWriter.append(COMMA_DELIMITER);
								fileWriter.append(vendor.getPhone() != null ? vendor.getPhone() : "");
								fileWriter.append(COMMA_DELIMITER);
								fileWriter.append(vendor.getMobileNo() != null ? vendor.getMobileNo() : "");
								fileWriter.append(COMMA_DELIMITER);
								fileWriter.append(vendor.getPan() != null ? vendor.getPan() : "");
								fileWriter.append(COMMA_DELIMITER);
								fileWriter.append(
										vendor.getOrganizationType() != null ? vendor.getOrganizationType() : "");
								fileWriter.append(COMMA_DELIMITER);
								fileWriter.append(vendor.getVendorGroup() != null ? vendor.getVendorGroup() : "");
								fileWriter.append(COMMA_DELIMITER);
								fileWriter.append(vendor.getGstRegnType() != null ? vendor.getGstRegnType() : "");
								fileWriter.append(COMMA_DELIMITER);
								fileWriter.append(vendor.getGstNumber() != null ? vendor.getGstNumber() : "");
								fileWriter.append(COMMA_DELIMITER);
								fileWriter.append(vendor.getPaymentTerms() != null ? vendor.getPaymentTerms() : "");
								fileWriter.append(COMMA_DELIMITER);
								fileWriter.append(vendor.getCurrency() != null ? vendor.getCurrency() : "");
								fileWriter.append(COMMA_DELIMITER);
								fileWriter.append(vendor.getOpeningBalance() != null ? vendor.getOpeningBalance() : "");
								fileWriter.append(COMMA_DELIMITER);
								fileWriter.append(vendor.getTds() != null ? vendor.getTds() : "");
								fileWriter.append(COMMA_DELIMITER);
								fileWriter.append(
										vendor.getOriginAddrAttention() != null ? vendor.getOriginAddrAttention() : "");
								fileWriter.append(COMMA_DELIMITER);
								fileWriter
										.append(vendor.getOriginAddrPhone() != null ? vendor.getOriginAddrPhone() : "");
								fileWriter.append(COMMA_DELIMITER);
								fileWriter.append(
										vendor.getOriginAddrCountry() != null ? vendor.getOriginAddrCountry() : "");
								fileWriter.append(COMMA_DELIMITER);
								fileWriter
										.append(vendor.getOriginAddrLine1() != null ? vendor.getOriginAddrLine1() : "");
								fileWriter.append(COMMA_DELIMITER);
								fileWriter
										.append(vendor.getOriginAddrLine2() != null ? vendor.getOriginAddrLine2() : "");
								fileWriter.append(COMMA_DELIMITER);
								fileWriter.append(
										vendor.getOriginAddrLandmark() != null ? vendor.getOriginAddrLandmark() : "");
								fileWriter.append(COMMA_DELIMITER);
								fileWriter
										.append(vendor.getOriginAddrState() != null ? vendor.getOriginAddrState() : "");
								fileWriter.append(COMMA_DELIMITER);
								fileWriter.append(vendor.getOriginAddrCity() != null ? vendor.getOriginAddrCity() : "");
								fileWriter.append(COMMA_DELIMITER);
								fileWriter.append(
										vendor.getOriginAddrPinCode() != null ? vendor.getOriginAddrPinCode() : "");
								fileWriter.append(COMMA_DELIMITER);
								fileWriter.append(
										vendor.getBillingAddrAttention() != null ? vendor.getBillingAddrAttention()
												: "");
								fileWriter.append(COMMA_DELIMITER);
								fileWriter.append(
										vendor.getBillingAddrPhone() != null ? vendor.getBillingAddrPhone() : "");
								fileWriter.append(COMMA_DELIMITER);
								fileWriter.append(
										vendor.getBillingAddrCountry() != null ? vendor.getBillingAddrCountry() : "");
								fileWriter.append(COMMA_DELIMITER);
								fileWriter.append(
										vendor.getBillingAddrLine1() != null ? vendor.getBillingAddrLine1() : "");
								fileWriter.append(COMMA_DELIMITER);
								fileWriter.append(
										vendor.getBillingAddrLine2() != null ? vendor.getBillingAddrLine2() : "");
								fileWriter.append(COMMA_DELIMITER);
								fileWriter.append(
										vendor.getBillingAddrLandmark() != null ? vendor.getBillingAddrLandmark() : "");
								fileWriter.append(COMMA_DELIMITER);
								fileWriter.append(
										vendor.getBillingAddrState() != null ? vendor.getBillingAddrState() : "");
								fileWriter.append(COMMA_DELIMITER);
								fileWriter
										.append(vendor.getBillingAddrCity() != null ? vendor.getBillingAddrCity() : "");
								fileWriter.append(COMMA_DELIMITER);
								fileWriter.append(
										vendor.getBillingAddrPinCode() != null ? vendor.getBillingAddrPinCode() : "");
								fileWriter.append(COMMA_DELIMITER);
								fileWriter.append(vendor.getStatus() != null ? vendor.getStatus() : "");
								fileWriter.append(COMMA_DELIMITER);
								fileWriter.append(vendorContactDetails.getContactSalutation() != null
										? vendorContactDetails.getContactSalutation()
										: "");
								fileWriter.append(COMMA_DELIMITER);
								fileWriter.append(vendorContactDetails.getContactFirstName() != null
										? vendorContactDetails.getContactFirstName()
										: "");
								fileWriter.append(COMMA_DELIMITER);
								fileWriter.append(vendorContactDetails.getContactLastName() != null
										? vendorContactDetails.getContactLastName()
										: "");
								fileWriter.append(COMMA_DELIMITER);
								fileWriter.append(vendorContactDetails.getContactWorkNo() != null
										? vendorContactDetails.getContactWorkNo()
										: "");
								fileWriter.append(COMMA_DELIMITER);
								fileWriter.append(vendorContactDetails.getContactMobileNo() != null
										? vendorContactDetails.getContactMobileNo()
										: "");
								fileWriter.append(COMMA_DELIMITER);
								fileWriter.append(vendorContactDetails.getContactEmailAddress() != null
										? vendorContactDetails.getContactEmailAddress()
										: "");
							}
							count++;
						}
					}
				}
				if (vendor.getBankDetails().size() == 0) {
					fileWriter.append(COMMA_DELIMITER);
					fileWriter.append(COMMA_DELIMITER);
					fileWriter.append(COMMA_DELIMITER);
					fileWriter.append(COMMA_DELIMITER);
					fileWriter.append(COMMA_DELIMITER);
					fileWriter.append(COMMA_DELIMITER);
					fileWriter.append(COMMA_DELIMITER);
				}
				if (vendor.getContacts().size() == 0 && vendor.getContacts().isEmpty()) {
					if (vendor.getBankDetails() != null && vendor.getBankDetails().size() == 1) {
						fileWriter.append(COMMA_DELIMITER);
						BankExportVo vendorBank = vendor.getBankDetails().get(0);
						fileWriter.append(vendorBank.getBankNname() != null ? vendorBank.getBankNname() : "");
						fileWriter.append(COMMA_DELIMITER);
						fileWriter.append(vendorBank.getBranchName() != null ? vendorBank.getBranchName() : "");
						fileWriter.append(COMMA_DELIMITER);
						fileWriter.append(
								vendorBank.getAccountHolderName() != null ? vendorBank.getAccountHolderName() : "");
						fileWriter.append(COMMA_DELIMITER);
						fileWriter.append(vendorBank.getAccountNo() != null ? vendorBank.getAccountNo() : "");
						fileWriter.append(COMMA_DELIMITER);
						fileWriter.append(vendorBank.getIfscCode() != null ? vendorBank.getIfscCode() : "");
						fileWriter.append(COMMA_DELIMITER);
						fileWriter.append(vendorBank.getUpiId() != null ? vendorBank.getUpiId() : "");
						fileWriter.append(COMMA_DELIMITER);
						fileWriter.append(String.valueOf(vendorBank.getIsDefault().toString()));
					} else {
						if (vendor.getBankDetails() != null && vendor.getBankDetails().size() > 0) {
							BankExportVo vendorBank = vendor.getBankDetails().get(0);
							fileWriter.append(COMMA_DELIMITER);
							fileWriter.append(vendorBank.getBankNname() != null ? vendorBank.getBankNname() : "");
							fileWriter.append(COMMA_DELIMITER);
							fileWriter.append(vendorBank.getBranchName() != null ? vendorBank.getBranchName() : "");
							fileWriter.append(COMMA_DELIMITER);
							fileWriter.append(
									vendorBank.getAccountHolderName() != null ? vendorBank.getAccountHolderName() : "");
							fileWriter.append(COMMA_DELIMITER);
							fileWriter.append(vendorBank.getAccountNo() != null ? vendorBank.getAccountNo() : "");
							fileWriter.append(COMMA_DELIMITER);
							fileWriter.append(vendorBank.getIfscCode() != null ? vendorBank.getIfscCode() : "");
							fileWriter.append(COMMA_DELIMITER);
							fileWriter.append(vendorBank.getUpiId() != null ? vendorBank.getUpiId() : "");
							fileWriter.append(COMMA_DELIMITER);
							fileWriter.append(String.valueOf(vendorBank.getIsDefault().toString()));
							int count = 0;
							for (BankExportVo vendorBankDetails : vendor.getBankDetails()) {
								if (count > 0) {
									fileWriter.append(NEW_LINE_SEPARATOR);
									fileWriter.append(String.valueOf(vendor.getId()));
									fileWriter.append(COMMA_DELIMITER);
									fileWriter.append(
											vendor.getPrimaryContact() != null ? vendor.getPrimaryContact() : "");
									fileWriter.append(COMMA_DELIMITER);
									fileWriter.append(vendor.getCompanyName() != null ? vendor.getCompanyName() : "");
									fileWriter.append(COMMA_DELIMITER);
									fileWriter.append(
											vendor.getVendorDisplayName() != null ? vendor.getVendorDisplayName() : "");
									fileWriter.append(COMMA_DELIMITER);
									fileWriter.append(vendor.getEmail() != null ? vendor.getEmail() : "");
									fileWriter.append(COMMA_DELIMITER);
									fileWriter.append(vendor.getPhone() != null ? vendor.getPhone() : "");
									fileWriter.append(COMMA_DELIMITER);
									fileWriter.append(vendor.getMobileNo() != null ? vendor.getMobileNo() : "");
									fileWriter.append(COMMA_DELIMITER);
									fileWriter.append(vendor.getPan() != null ? vendor.getPan() : "");
									fileWriter.append(COMMA_DELIMITER);
									fileWriter.append(
											vendor.getOrganizationType() != null ? vendor.getOrganizationType() : "");
									fileWriter.append(COMMA_DELIMITER);
									fileWriter.append(vendor.getVendorGroup() != null ? vendor.getVendorGroup() : "");
									fileWriter.append(COMMA_DELIMITER);
									fileWriter.append(vendor.getGstRegnType() != null ? vendor.getGstRegnType() : "");
									fileWriter.append(COMMA_DELIMITER);
									fileWriter.append(vendor.getGstNumber() != null ? vendor.getGstNumber() : "");
									fileWriter.append(COMMA_DELIMITER);
									fileWriter.append(vendor.getPaymentTerms() != null ? vendor.getPaymentTerms() : "");
									fileWriter.append(COMMA_DELIMITER);
									fileWriter.append(vendor.getCurrency() != null ? vendor.getCurrency() : "");
									fileWriter.append(COMMA_DELIMITER);
									fileWriter.append(
											vendor.getOpeningBalance() != null ? vendor.getOpeningBalance() : "");
									fileWriter.append(COMMA_DELIMITER);
									fileWriter.append(vendor.getTds() != null ? vendor.getTds() : "");
									fileWriter.append(COMMA_DELIMITER);
									fileWriter.append(
											vendor.getOriginAddrAttention() != null ? vendor.getOriginAddrAttention()
													: "");
									fileWriter.append(COMMA_DELIMITER);
									fileWriter.append(
											vendor.getOriginAddrPhone() != null ? vendor.getOriginAddrPhone() : "");
									fileWriter.append(COMMA_DELIMITER);
									fileWriter.append(
											vendor.getOriginAddrCountry() != null ? vendor.getOriginAddrCountry() : "");
									fileWriter.append(COMMA_DELIMITER);
									fileWriter.append(
											vendor.getOriginAddrLine1() != null ? vendor.getOriginAddrLine1() : "");
									fileWriter.append(COMMA_DELIMITER);
									fileWriter.append(
											vendor.getOriginAddrLine2() != null ? vendor.getOriginAddrLine2() : "");
									fileWriter.append(COMMA_DELIMITER);
									fileWriter.append(
											vendor.getOriginAddrLandmark() != null ? vendor.getOriginAddrLandmark()
													: "");
									fileWriter.append(COMMA_DELIMITER);
									fileWriter.append(
											vendor.getOriginAddrState() != null ? vendor.getOriginAddrState() : "");
									fileWriter.append(COMMA_DELIMITER);
									fileWriter.append(
											vendor.getOriginAddrCity() != null ? vendor.getOriginAddrCity() : "");
									fileWriter.append(COMMA_DELIMITER);
									fileWriter.append(
											vendor.getOriginAddrPinCode() != null ? vendor.getOriginAddrPinCode() : "");
									fileWriter.append(COMMA_DELIMITER);
									fileWriter.append(
											vendor.getBillingAddrAttention() != null ? vendor.getBillingAddrAttention()
													: "");
									fileWriter.append(COMMA_DELIMITER);
									fileWriter.append(
											vendor.getBillingAddrPhone() != null ? vendor.getBillingAddrPhone() : "");
									fileWriter.append(COMMA_DELIMITER);
									fileWriter.append(
											vendor.getBillingAddrCountry() != null ? vendor.getBillingAddrCountry()
													: "");
									fileWriter.append(COMMA_DELIMITER);
									fileWriter.append(
											vendor.getBillingAddrLine1() != null ? vendor.getBillingAddrLine1() : "");
									fileWriter.append(COMMA_DELIMITER);
									fileWriter.append(
											vendor.getBillingAddrLine2() != null ? vendor.getBillingAddrLine2() : "");
									fileWriter.append(COMMA_DELIMITER);
									fileWriter.append(
											vendor.getBillingAddrLandmark() != null ? vendor.getBillingAddrLandmark()
													: "");
									fileWriter.append(COMMA_DELIMITER);
									fileWriter.append(
											vendor.getBillingAddrState() != null ? vendor.getBillingAddrState() : "");
									fileWriter.append(COMMA_DELIMITER);
									fileWriter.append(
											vendor.getBillingAddrCity() != null ? vendor.getBillingAddrCity() : "");
									fileWriter.append(COMMA_DELIMITER);
									fileWriter.append(
											vendor.getBillingAddrPinCode() != null ? vendor.getBillingAddrPinCode()
													: "");
									fileWriter.append(COMMA_DELIMITER);
									fileWriter.append(vendor.getStatus() != null ? vendor.getStatus() : "");
									fileWriter.append(COMMA_DELIMITER);
									fileWriter.append(COMMA_DELIMITER);
									fileWriter.append(COMMA_DELIMITER);
									fileWriter.append(COMMA_DELIMITER);
									fileWriter.append(COMMA_DELIMITER);
									fileWriter.append(COMMA_DELIMITER);
									fileWriter.append(COMMA_DELIMITER);
									fileWriter.append(
											vendorBankDetails.getBankNname() != null ? vendorBankDetails.getBankNname()
													: "");
									fileWriter.append(COMMA_DELIMITER);
									fileWriter.append(vendorBankDetails.getBranchName() != null
											? vendorBankDetails.getBranchName()
											: "");
									fileWriter.append(COMMA_DELIMITER);
									fileWriter.append(vendorBankDetails.getAccountHolderName() != null
											? vendorBankDetails.getAccountHolderName()
											: "");
									fileWriter.append(COMMA_DELIMITER);
									fileWriter.append(
											vendorBankDetails.getAccountNo() != null ? vendorBankDetails.getAccountNo()
													: "");
									fileWriter.append(COMMA_DELIMITER);
									fileWriter.append(
											vendorBankDetails.getIfscCode() != null ? vendorBankDetails.getIfscCode()
													: "");
									fileWriter.append(COMMA_DELIMITER);
									fileWriter.append(
											vendorBankDetails.getUpiId() != null ? vendorBankDetails.getUpiId() : "");
									fileWriter.append(COMMA_DELIMITER);
									fileWriter.append(String.valueOf(vendorBankDetails.getIsDefault().toString()));
								}
								count++;
							}

						}
					}

				} else {
					if (vendor.getBankDetails() != null && vendor.getBankDetails().size() > 0) {
						for (BankExportVo vendorBankDetails : vendor.getBankDetails()) {
							fileWriter.append(NEW_LINE_SEPARATOR);
							fileWriter.append(String.valueOf(vendor.getId()));
							fileWriter.append(COMMA_DELIMITER);
							fileWriter.append(vendor.getPrimaryContact() != null ? vendor.getPrimaryContact() : "");
							fileWriter.append(COMMA_DELIMITER);
							fileWriter.append(vendor.getCompanyName() != null ? vendor.getCompanyName() : "");
							fileWriter.append(COMMA_DELIMITER);
							fileWriter
									.append(vendor.getVendorDisplayName() != null ? vendor.getVendorDisplayName() : "");
							fileWriter.append(COMMA_DELIMITER);
							fileWriter.append(vendor.getEmail() != null ? vendor.getEmail() : "");
							fileWriter.append(COMMA_DELIMITER);
							fileWriter.append(vendor.getPhone() != null ? vendor.getPhone() : "");
							fileWriter.append(COMMA_DELIMITER);
							fileWriter.append(vendor.getMobileNo() != null ? vendor.getMobileNo() : "");
							fileWriter.append(COMMA_DELIMITER);
							fileWriter.append(vendor.getPan() != null ? vendor.getPan() : "");
							fileWriter.append(COMMA_DELIMITER);
							fileWriter.append(vendor.getOrganizationType() != null ? vendor.getOrganizationType() : "");
							fileWriter.append(COMMA_DELIMITER);
							fileWriter.append(vendor.getVendorGroup() != null ? vendor.getVendorGroup() : "");
							fileWriter.append(COMMA_DELIMITER);
							fileWriter.append(vendor.getGstRegnType() != null ? vendor.getGstRegnType() : "");
							fileWriter.append(COMMA_DELIMITER);
							fileWriter.append(vendor.getGstNumber() != null ? vendor.getGstNumber() : "");
							fileWriter.append(COMMA_DELIMITER);
							fileWriter.append(vendor.getPaymentTerms() != null ? vendor.getPaymentTerms() : "");
							fileWriter.append(COMMA_DELIMITER);
							fileWriter.append(vendor.getCurrency() != null ? vendor.getCurrency() : "");
							fileWriter.append(COMMA_DELIMITER);
							fileWriter.append(vendor.getOpeningBalance() != null ? vendor.getOpeningBalance() : "");
							fileWriter.append(COMMA_DELIMITER);
							fileWriter.append(vendor.getTds() != null ? vendor.getTds() : "");
							fileWriter.append(COMMA_DELIMITER);
							fileWriter.append(
									vendor.getOriginAddrAttention() != null ? vendor.getOriginAddrAttention() : "");
							fileWriter.append(COMMA_DELIMITER);
							fileWriter.append(vendor.getOriginAddrPhone() != null ? vendor.getOriginAddrPhone() : "");
							fileWriter.append(COMMA_DELIMITER);
							fileWriter
									.append(vendor.getOriginAddrCountry() != null ? vendor.getOriginAddrCountry() : "");
							fileWriter.append(COMMA_DELIMITER);
							fileWriter.append(vendor.getOriginAddrLine1() != null ? vendor.getOriginAddrLine1() : "");
							fileWriter.append(COMMA_DELIMITER);
							fileWriter.append(vendor.getOriginAddrLine2() != null ? vendor.getOriginAddrLine2() : "");
							fileWriter.append(COMMA_DELIMITER);
							fileWriter.append(
									vendor.getOriginAddrLandmark() != null ? vendor.getOriginAddrLandmark() : "");
							fileWriter.append(COMMA_DELIMITER);
							fileWriter.append(vendor.getOriginAddrState() != null ? vendor.getOriginAddrState() : "");
							fileWriter.append(COMMA_DELIMITER);
							fileWriter.append(vendor.getOriginAddrCity() != null ? vendor.getOriginAddrCity() : "");
							fileWriter.append(COMMA_DELIMITER);
							fileWriter
									.append(vendor.getOriginAddrPinCode() != null ? vendor.getOriginAddrPinCode() : "");
							fileWriter.append(COMMA_DELIMITER);
							fileWriter.append(
									vendor.getBillingAddrAttention() != null ? vendor.getBillingAddrAttention() : "");
							fileWriter.append(COMMA_DELIMITER);
							fileWriter.append(vendor.getBillingAddrPhone() != null ? vendor.getBillingAddrPhone() : "");
							fileWriter.append(COMMA_DELIMITER);
							fileWriter.append(
									vendor.getBillingAddrCountry() != null ? vendor.getBillingAddrCountry() : "");
							fileWriter.append(COMMA_DELIMITER);
							fileWriter.append(vendor.getBillingAddrLine1() != null ? vendor.getBillingAddrLine1() : "");
							fileWriter.append(COMMA_DELIMITER);
							fileWriter.append(vendor.getBillingAddrLine2() != null ? vendor.getBillingAddrLine2() : "");
							fileWriter.append(COMMA_DELIMITER);
							fileWriter.append(
									vendor.getBillingAddrLandmark() != null ? vendor.getBillingAddrLandmark() : "");
							fileWriter.append(COMMA_DELIMITER);
							fileWriter.append(vendor.getBillingAddrState() != null ? vendor.getBillingAddrState() : "");
							fileWriter.append(COMMA_DELIMITER);
							fileWriter.append(vendor.getBillingAddrCity() != null ? vendor.getBillingAddrCity() : "");
							fileWriter.append(COMMA_DELIMITER);
							fileWriter.append(
									vendor.getBillingAddrPinCode() != null ? vendor.getBillingAddrPinCode() : "");
							fileWriter.append(COMMA_DELIMITER);
							fileWriter.append(vendor.getStatus() != null ? vendor.getStatus() : "");
							fileWriter.append(COMMA_DELIMITER);
							fileWriter.append(COMMA_DELIMITER);
							fileWriter.append(COMMA_DELIMITER);
							fileWriter.append(COMMA_DELIMITER);
							fileWriter.append(COMMA_DELIMITER);
							fileWriter.append(COMMA_DELIMITER);
							fileWriter.append(COMMA_DELIMITER);
							fileWriter.append(
									vendorBankDetails.getBankNname() != null ? vendorBankDetails.getBankNname() : "");
							fileWriter.append(COMMA_DELIMITER);
							fileWriter.append(
									vendorBankDetails.getBranchName() != null ? vendorBankDetails.getBranchName() : "");
							fileWriter.append(COMMA_DELIMITER);
							fileWriter.append(vendorBankDetails.getAccountHolderName() != null
									? vendorBankDetails.getAccountHolderName()
									: "");
							fileWriter.append(COMMA_DELIMITER);
							fileWriter.append(
									vendorBankDetails.getAccountNo() != null ? vendorBankDetails.getAccountNo() : "");
							fileWriter.append(COMMA_DELIMITER);
							fileWriter.append(
									vendorBankDetails.getIfscCode() != null ? vendorBankDetails.getIfscCode() : "");
							fileWriter.append(COMMA_DELIMITER);
							fileWriter.append(vendorBankDetails.getUpiId() != null ? vendorBankDetails.getUpiId() : "");
							fileWriter.append(COMMA_DELIMITER);
							fileWriter.append(String.valueOf(vendorBankDetails.getIsDefault().toString()));
						}
					}
				}
				fileWriter.append(NEW_LINE_SEPARATOR);
			}

		} catch (Exception e) {
			System.out.println("Error in CsvFileWriter !!!");
			throw new ApplicationException(e);
		} finally {
			try {
				fileWriter.flush();
				fileWriter.close();
			} catch (IOException e) {
				System.out.println("Error while flushing/closing fileWriter !!!");
				throw new ApplicationException(e);
			}

		}

		String exportPath = filePath.toString();
		byte[] fileBytes = null;
		try {
			fileBytes = Files.readAllBytes(new File(exportPath).toPath());
		} catch (IOException e) {
			throw new ApplicationException(e);
		}
		if (fileBytes.length > 0) {
			logger.info("To encode the bytes  ::");
			encodedString = Base64.getEncoder().encodeToString(fileBytes);
			filePath.delete();
		}

		return encodedString;
	}

	public String exportCustomerXlsx(List<CustomerExportVo> data, String moduleName, String fileType)
			throws ApplicationException {
		logger.info("Entry into method: exportCustomerXlsx");
		String encodedString = new String();
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet(moduleName);
		/** Header Row */
		Row headerRow = sheet.createRow(0);
		// Creating cells
		headerRow.createCell(0).setCellValue("id");
		headerRow.createCell(1).setCellValue("primary_contact");
		headerRow.createCell(2).setCellValue("company_name");
		headerRow.createCell(3).setCellValue("customer_display_name");
		headerRow.createCell(4).setCellValue("email");
		headerRow.createCell(5).setCellValue("phone");
		headerRow.createCell(6).setCellValue("mobile_no");
		headerRow.createCell(7).setCellValue("website");
		headerRow.createCell(8).setCellValue("pan");
		headerRow.createCell(9).setCellValue("organization_type");
		headerRow.createCell(10).setCellValue("customer_group");
		headerRow.createCell(11).setCellValue("gst_regn_type");
		headerRow.createCell(12).setCellValue("gst_number");
		headerRow.createCell(13).setCellValue("currency");
		headerRow.createCell(14).setCellValue("opening_balance");
		headerRow.createCell(15).setCellValue("payment_terms");
		headerRow.createCell(16).setCellValue("delivery_addr_attention");
		headerRow.createCell(17).setCellValue("delivery_addr_phone");
		headerRow.createCell(18).setCellValue("delivery_addr_country");
		headerRow.createCell(19).setCellValue("delivery_addr_line1");
		headerRow.createCell(20).setCellValue("delivery_addr_line2");
		headerRow.createCell(21).setCellValue("delivery_addr_landmark");
		headerRow.createCell(22).setCellValue("delivery_addr_state");
		headerRow.createCell(23).setCellValue("delivery_addr_city");
		headerRow.createCell(24).setCellValue("delivery_addr_pin_code");
		headerRow.createCell(25).setCellValue("billing_addr_attention");
		headerRow.createCell(26).setCellValue("billing_addr_phone");
		headerRow.createCell(27).setCellValue("billing_addr_country");
		headerRow.createCell(28).setCellValue("billing_addr_line1");
		headerRow.createCell(29).setCellValue("billing_addr_line2");
		headerRow.createCell(30).setCellValue("billing_addr_landmark");
		headerRow.createCell(31).setCellValue("billing_addr_state");
		headerRow.createCell(32).setCellValue("billing_addr_city");
		headerRow.createCell(33).setCellValue("billing_addr_pin_code");
		headerRow.createCell(34).setCellValue("status");
		headerRow.createCell(35).setCellValue("contact_salutation");
		headerRow.createCell(36).setCellValue("contact_first_name");
		headerRow.createCell(37).setCellValue("contact_last_name");
		headerRow.createCell(38).setCellValue("contact_work_no");
		headerRow.createCell(39).setCellValue("contact_mobile_no");
		headerRow.createCell(40).setCellValue("contact_email_address");
		headerRow.createCell(41).setCellValue("bank_name");
		headerRow.createCell(42).setCellValue("branch_name");
		headerRow.createCell(43).setCellValue("account_holder_name");
		headerRow.createCell(44).setCellValue("account_no");
		headerRow.createCell(45).setCellValue("ifsc_code");
		headerRow.createCell(46).setCellValue("upi_id");
		headerRow.createCell(47).setCellValue("is_default");
		int rowNum = 1;

		/** Writing Data */
		for (CustomerExportVo customer : data) {

			// Create ROW
			Row row = sheet.createRow(rowNum++);
			// Creating cells
			row.createCell(0).setCellValue(customer.getId());
			row.createCell(1).setCellValue(customer.getPrimaryContact());
			row.createCell(2).setCellValue(customer.getCompanyName());
			row.createCell(3).setCellValue(customer.getCustomerDisplayName());
			row.createCell(4).setCellValue(customer.getEmail());
			row.createCell(5).setCellValue(customer.getPhone());
			row.createCell(6).setCellValue(customer.getMobileNo());
			row.createCell(7).setCellValue(customer.getWebsite());
			row.createCell(8).setCellValue(customer.getPan());
			row.createCell(9).setCellValue(customer.getOrganizationType());
			row.createCell(10).setCellValue(customer.getCustomerGroup());
			row.createCell(11).setCellValue(customer.getGstRegnType());
			row.createCell(12).setCellValue(customer.getGstNumber());
			row.createCell(13).setCellValue(customer.getCurrency());
			row.createCell(14).setCellValue(customer.getOpeningBalance());
			row.createCell(15).setCellValue(customer.getPaymentTerms());
			row.createCell(16).setCellValue(customer.getDeliveryAddrAttention());
			row.createCell(17).setCellValue(customer.getDeliveryAddrPhone());
			row.createCell(18).setCellValue(customer.getDeliveryAddrCountry());
			row.createCell(19).setCellValue(customer.getDeliveryAddrLine1());
			row.createCell(20).setCellValue(customer.getDeliveryAddrLine2());
			row.createCell(21).setCellValue(customer.getDeliveryAddrLandmark());
			row.createCell(22).setCellValue(customer.getDeliveryAddrState());
			row.createCell(23).setCellValue(customer.getDeliveryAddrCity());
			row.createCell(24).setCellValue(customer.getDeliveryAddrPinCode());
			row.createCell(25).setCellValue(customer.getBillingAddrAttention());
			row.createCell(26).setCellValue(customer.getBillingAddrPhone());
			row.createCell(27).setCellValue(customer.getBillingAddrCountry());
			row.createCell(28).setCellValue(customer.getBillingAddrLine1());
			row.createCell(29).setCellValue(customer.getBillingAddrLine2());
			row.createCell(30).setCellValue(customer.getBillingAddrLandmark());
			row.createCell(31).setCellValue(customer.getBillingAddrState());
			row.createCell(32).setCellValue(customer.getBillingAddrCity());
			row.createCell(33).setCellValue(customer.getBillingAddrPinCode());
			row.createCell(34).setCellValue(customer.getStatus());
			if (customer.getContacts() != null && customer.getContacts().size() == 1) {
				ContactsExportVo customerContact = customer.getContacts().get(0);
				row.createCell(35).setCellValue(customerContact.getContactSalutation());
				row.createCell(36).setCellValue(customerContact.getContactFirstName());
				row.createCell(37).setCellValue(customerContact.getContactLastName());
				row.createCell(38).setCellValue(customerContact.getContactWorkNo());
				row.createCell(39).setCellValue(customerContact.getContactMobileNo());
				row.createCell(40).setCellValue(customerContact.getContactEmailAddress());
			} else {
				if (customer.getContacts() != null && customer.getContacts().size() > 0) {
					ContactsExportVo customerContact = customer.getContacts().get(0);
					row.createCell(35).setCellValue(customerContact.getContactSalutation());
					row.createCell(36).setCellValue(customerContact.getContactFirstName());
					row.createCell(37).setCellValue(customerContact.getContactLastName());
					row.createCell(38).setCellValue(customerContact.getContactWorkNo());
					row.createCell(39).setCellValue(customerContact.getContactMobileNo());
					row.createCell(40).setCellValue(customerContact.getContactEmailAddress());
					int count = 0;
					for (ContactsExportVo customercontactDetails : customer.getContacts()) {
						if (count > 0) {
							row = sheet.createRow(rowNum++);
							row.createCell(0).setCellValue(customer.getId());
							row.createCell(1).setCellValue(customer.getPrimaryContact());
							row.createCell(2).setCellValue(customer.getCompanyName());
							row.createCell(3).setCellValue(customer.getCustomerDisplayName());
							row.createCell(4).setCellValue(customer.getEmail());
							row.createCell(5).setCellValue(customer.getPhone());
							row.createCell(6).setCellValue(customer.getMobileNo());
							row.createCell(7).setCellValue(customer.getWebsite());
							row.createCell(8).setCellValue(customer.getPan());
							row.createCell(9).setCellValue(customer.getOrganizationType());
							row.createCell(10).setCellValue(customer.getCustomerGroup());
							row.createCell(11).setCellValue(customer.getGstRegnType());
							row.createCell(12).setCellValue(customer.getGstNumber());
							row.createCell(13).setCellValue(customer.getCurrency());
							row.createCell(14).setCellValue(customer.getOpeningBalance());
							row.createCell(15).setCellValue(customer.getPaymentTerms());
							row.createCell(16).setCellValue(customer.getDeliveryAddrAttention());
							row.createCell(17).setCellValue(customer.getDeliveryAddrPhone());
							row.createCell(18).setCellValue(customer.getDeliveryAddrCountry());
							row.createCell(19).setCellValue(customer.getDeliveryAddrLine1());
							row.createCell(20).setCellValue(customer.getDeliveryAddrLine2());
							row.createCell(21).setCellValue(customer.getDeliveryAddrLandmark());
							row.createCell(22).setCellValue(customer.getDeliveryAddrState());
							row.createCell(23).setCellValue(customer.getDeliveryAddrCity());
							row.createCell(24).setCellValue(customer.getDeliveryAddrPinCode());
							row.createCell(25).setCellValue(customer.getBillingAddrAttention());
							row.createCell(26).setCellValue(customer.getBillingAddrPhone());
							row.createCell(27).setCellValue(customer.getBillingAddrCountry());
							row.createCell(28).setCellValue(customer.getBillingAddrLine1());
							row.createCell(29).setCellValue(customer.getBillingAddrLine2());
							row.createCell(30).setCellValue(customer.getBillingAddrLandmark());
							row.createCell(31).setCellValue(customer.getBillingAddrState());
							row.createCell(32).setCellValue(customer.getBillingAddrCity());
							row.createCell(33).setCellValue(customer.getBillingAddrPinCode());
							row.createCell(34).setCellValue(customer.getStatus());
							row.createCell(35).setCellValue(customercontactDetails.getContactSalutation());
							row.createCell(36).setCellValue(customercontactDetails.getContactFirstName());
							row.createCell(37).setCellValue(customercontactDetails.getContactLastName());
							row.createCell(38).setCellValue(customercontactDetails.getContactWorkNo());
							row.createCell(39).setCellValue(customercontactDetails.getContactMobileNo());
							row.createCell(40).setCellValue(customercontactDetails.getContactEmailAddress());
						}
						count++;
					}
				}
			}
			if (customer.getContacts().size() == 0 && customer.getContacts().isEmpty()) {
				if (customer.getBankDetails() != null && customer.getBankDetails().size() == 1) {
					BankExportVo customerBank = customer.getBankDetails().get(0);
					row.createCell(41).setCellValue(customerBank.getBankNname());
					row.createCell(42).setCellValue(customerBank.getBranchName());
					row.createCell(43).setCellValue(customerBank.getAccountHolderName());
					row.createCell(44).setCellValue(customerBank.getAccountNo());
					row.createCell(45).setCellValue(customerBank.getIfscCode());
					row.createCell(46).setCellValue(customerBank.getUpiId());
					row.createCell(47).setCellValue(customerBank.getIsDefault());
				} else {
					if (customer.getBankDetails() != null && customer.getBankDetails().size() > 0) {
						BankExportVo customerBank = customer.getBankDetails().get(0);
						row.createCell(41).setCellValue(customerBank.getBankNname());
						row.createCell(42).setCellValue(customerBank.getBranchName());
						row.createCell(43).setCellValue(customerBank.getAccountHolderName());
						row.createCell(44).setCellValue(customerBank.getAccountNo());
						row.createCell(45).setCellValue(customerBank.getIfscCode());
						row.createCell(46).setCellValue(customerBank.getUpiId());
						row.createCell(47).setCellValue(customerBank.getIsDefault());
						int count = 0;
						for (BankExportVo customerBankDetails : customer.getBankDetails()) {
							if (count > 0) {
								row = sheet.createRow(rowNum++);
								row.createCell(0).setCellValue(customer.getId());
								row.createCell(1).setCellValue(customer.getPrimaryContact());
								row.createCell(2).setCellValue(customer.getCompanyName());
								row.createCell(3).setCellValue(customer.getCustomerDisplayName());
								row.createCell(4).setCellValue(customer.getEmail());
								row.createCell(5).setCellValue(customer.getPhone());
								row.createCell(6).setCellValue(customer.getMobileNo());
								row.createCell(7).setCellValue(customer.getWebsite());
								row.createCell(8).setCellValue(customer.getPan());
								row.createCell(9).setCellValue(customer.getOrganizationType());
								row.createCell(10).setCellValue(customer.getCustomerGroup());
								row.createCell(11).setCellValue(customer.getGstRegnType());
								row.createCell(12).setCellValue(customer.getGstNumber());
								row.createCell(13).setCellValue(customer.getCurrency());
								row.createCell(14).setCellValue(customer.getOpeningBalance());
								row.createCell(15).setCellValue(customer.getPaymentTerms());
								row.createCell(16).setCellValue(customer.getDeliveryAddrAttention());
								row.createCell(17).setCellValue(customer.getDeliveryAddrPhone());
								row.createCell(18).setCellValue(customer.getDeliveryAddrCountry());
								row.createCell(19).setCellValue(customer.getDeliveryAddrLine1());
								row.createCell(20).setCellValue(customer.getDeliveryAddrLine2());
								row.createCell(21).setCellValue(customer.getDeliveryAddrLandmark());
								row.createCell(22).setCellValue(customer.getDeliveryAddrState());
								row.createCell(23).setCellValue(customer.getDeliveryAddrCity());
								row.createCell(24).setCellValue(customer.getDeliveryAddrPinCode());
								row.createCell(25).setCellValue(customer.getBillingAddrAttention());
								row.createCell(26).setCellValue(customer.getBillingAddrPhone());
								row.createCell(27).setCellValue(customer.getBillingAddrCountry());
								row.createCell(28).setCellValue(customer.getBillingAddrLine1());
								row.createCell(29).setCellValue(customer.getBillingAddrLine2());
								row.createCell(30).setCellValue(customer.getBillingAddrLandmark());
								row.createCell(31).setCellValue(customer.getBillingAddrState());
								row.createCell(32).setCellValue(customer.getBillingAddrCity());
								row.createCell(33).setCellValue(customer.getBillingAddrPinCode());
								row.createCell(34).setCellValue(customer.getStatus());
								row.createCell(41).setCellValue(customerBankDetails.getBankNname());
								row.createCell(42).setCellValue(customerBankDetails.getBranchName());
								row.createCell(43).setCellValue(customerBankDetails.getAccountHolderName());
								row.createCell(44).setCellValue(customerBankDetails.getAccountNo());
								row.createCell(45).setCellValue(customerBankDetails.getIfscCode());
								row.createCell(46).setCellValue(customerBankDetails.getUpiId());
								row.createCell(47).setCellValue(customerBankDetails.getIsDefault());
							}
							count++;
						}
					}
				}

			} else {
				if (customer.getBankDetails() != null && customer.getBankDetails().size() > 0) {
					for (BankExportVo customerBankDetails : customer.getBankDetails()) {
						row = sheet.createRow(rowNum++);
						row.createCell(0).setCellValue(customer.getId());
						row.createCell(1).setCellValue(customer.getPrimaryContact());
						row.createCell(2).setCellValue(customer.getCompanyName());
						row.createCell(3).setCellValue(customer.getCustomerDisplayName());
						row.createCell(4).setCellValue(customer.getEmail());
						row.createCell(5).setCellValue(customer.getPhone());
						row.createCell(6).setCellValue(customer.getMobileNo());
						row.createCell(7).setCellValue(customer.getWebsite());
						row.createCell(8).setCellValue(customer.getPan());
						row.createCell(9).setCellValue(customer.getOrganizationType());
						row.createCell(10).setCellValue(customer.getCustomerGroup());
						row.createCell(11).setCellValue(customer.getGstRegnType());
						row.createCell(12).setCellValue(customer.getGstNumber());
						row.createCell(13).setCellValue(customer.getCurrency());
						row.createCell(14).setCellValue(customer.getOpeningBalance());
						row.createCell(15).setCellValue(customer.getPaymentTerms());
						row.createCell(16).setCellValue(customer.getDeliveryAddrAttention());
						row.createCell(17).setCellValue(customer.getDeliveryAddrPhone());
						row.createCell(18).setCellValue(customer.getDeliveryAddrCountry());
						row.createCell(19).setCellValue(customer.getDeliveryAddrLine1());
						row.createCell(20).setCellValue(customer.getDeliveryAddrLine2());
						row.createCell(21).setCellValue(customer.getDeliveryAddrLandmark());
						row.createCell(22).setCellValue(customer.getDeliveryAddrState());
						row.createCell(23).setCellValue(customer.getDeliveryAddrCity());
						row.createCell(24).setCellValue(customer.getDeliveryAddrPinCode());
						row.createCell(25).setCellValue(customer.getBillingAddrAttention());
						row.createCell(26).setCellValue(customer.getBillingAddrPhone());
						row.createCell(27).setCellValue(customer.getBillingAddrCountry());
						row.createCell(28).setCellValue(customer.getBillingAddrLine1());
						row.createCell(29).setCellValue(customer.getBillingAddrLine2());
						row.createCell(30).setCellValue(customer.getBillingAddrLandmark());
						row.createCell(31).setCellValue(customer.getBillingAddrState());
						row.createCell(32).setCellValue(customer.getBillingAddrCity());
						row.createCell(33).setCellValue(customer.getBillingAddrPinCode());
						row.createCell(34).setCellValue(customer.getStatus());
						row.createCell(41).setCellValue(customerBankDetails.getBankNname());
						row.createCell(42).setCellValue(customerBankDetails.getBranchName());
						row.createCell(43).setCellValue(customerBankDetails.getAccountHolderName());
						row.createCell(44).setCellValue(customerBankDetails.getAccountNo());
						row.createCell(45).setCellValue(customerBankDetails.getIfscCode());
						row.createCell(46).setCellValue(customerBankDetails.getUpiId());
						row.createCell(47).setCellValue(customerBankDetails.getIsDefault());
					}
				}

			}

		}

		encodedString = encodingXLSXFile(moduleName, fileType, workbook);
		return encodedString;
	}

	public String exportCustomerCsv(List<CustomerExportVo> data, String moduleName, String fileType)
			throws ApplicationException {
		logger.info("Entry into method: exportCustomerCsv");
		final String COMMA_DELIMITER = ",";
		final String NEW_LINE_SEPARATOR = "\n";
		final String CUSTOMER_FILE_HEADER = "id,primary_contact,company_name,customer_display_name,email,phone,mobile_no,website,pan,organization_type,customer_group,gst_regn_type,gst_number,currency,opening_balance,payment_terms,delivery_addr_attention,delivery_addr_phone,delivery_addr_country,delivery_addr_line1,delivery_addr_line2,delivery_addr_landmark,delivery_addr_state,delivery_addr_city,delivery_addr_pin_code,billing_addr_attention,billing_addr_phone,billing_addr_country,billing_addr_line1,billing_addr_line2,billing_addr_landmark,billing_addr_state,billing_addr_city,billing_addr_pin_code,status,contact_salutation,contact_first_name,contact_last_name,contact_work_no,contact_mobile_no,contact_email_address,bank_name,branch_name,account_holder_name,account_no,ifsc_code,upi_id,is_default";
		FileWriter fileWriter = null;
		File filePath;
		String encodedString = new String();
		try {
			FileReader reader;
			reader = new FileReader("/decifer/config/app_config.properties");
			Properties p = new Properties();
			p.load(reader);
			StringBuilder path = new StringBuilder(p.getProperty("file_store_path"));
			String pathtoSave = path.toString();
			filePath = new File(pathtoSave + "/" + moduleName + "." + fileType);

			fileWriter = new FileWriter(filePath);

			// Write the CSV file header
			fileWriter.append(CUSTOMER_FILE_HEADER.toString());

			// Add a new line separator after the header
			fileWriter.append(NEW_LINE_SEPARATOR);

			for (CustomerExportVo customer : data) {
				fileWriter.append(String.valueOf(customer.getId()));
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(customer.getPrimaryContact() != null ? customer.getPrimaryContact() : "");
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(customer.getCompanyName() != null ? customer.getCompanyName() : "");
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(customer.getCustomerDisplayName() != null ? customer.getCustomerDisplayName() : "");
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(customer.getEmail() != null ? customer.getEmail() : "");
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(customer.getPhone() != null ? customer.getPhone() : "");
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(customer.getMobileNo() != null ? customer.getMobileNo() : "");
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(customer.getWebsite() != null ? customer.getWebsite() : "");
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(customer.getPan() != null ? customer.getPan() : "");
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(customer.getOrganizationType() != null ? customer.getOrganizationType() : "");
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(customer.getCustomerGroup() != null ? customer.getCustomerGroup() : "");
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(customer.getGstRegnType() != null ? customer.getGstRegnType() : "");
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(customer.getGstNumber() != null ? customer.getGstNumber() : "");
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(customer.getCurrency() != null ? customer.getCurrency() : "");
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(customer.getOpeningBalance() != null ? customer.getOpeningBalance() : "");
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(customer.getPaymentTerms() != null ? customer.getPaymentTerms() : "");
				fileWriter.append(COMMA_DELIMITER);
				fileWriter
						.append(customer.getDeliveryAddrAttention() != null ? customer.getDeliveryAddrAttention() : "");
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(customer.getDeliveryAddrPhone() != null ? customer.getDeliveryAddrPhone() : "");
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(customer.getDeliveryAddrCountry() != null ? customer.getDeliveryAddrCountry() : "");
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(customer.getDeliveryAddrLine1() != null ? customer.getDeliveryAddrLine1() : "");
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(customer.getDeliveryAddrLine2() != null ? customer.getDeliveryAddrLine2() : "");
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(customer.getDeliveryAddrLandmark() != null ? customer.getDeliveryAddrLandmark() : "");
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(customer.getDeliveryAddrState() != null ? customer.getDeliveryAddrState() : "");
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(customer.getDeliveryAddrCity() != null ? customer.getDeliveryAddrCity() : "");
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(customer.getDeliveryAddrPinCode() != null ? customer.getDeliveryAddrPinCode() : "");
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(customer.getBillingAddrAttention() != null ? customer.getBillingAddrAttention() : "");
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(customer.getBillingAddrPhone() != null ? customer.getBillingAddrPhone() : "");
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(customer.getBillingAddrCountry() != null ? customer.getBillingAddrCountry() : "");
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(customer.getBillingAddrLine1() != null ? customer.getBillingAddrLine1() : "");
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(customer.getBillingAddrLine2() != null ? customer.getBillingAddrLine2() : "");
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(customer.getBillingAddrLandmark() != null ? customer.getBillingAddrLandmark() : "");
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(customer.getBillingAddrState() != null ? customer.getBillingAddrState() : "");
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(customer.getBillingAddrCity() != null ? customer.getBillingAddrCity() : "");
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(customer.getBillingAddrPinCode() != null ? customer.getBillingAddrPinCode() : "");
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(customer.getStatus());
				if (customer.getContacts().size() == 0) {
					fileWriter.append(COMMA_DELIMITER);
					fileWriter.append(COMMA_DELIMITER);
					fileWriter.append(COMMA_DELIMITER);
					fileWriter.append(COMMA_DELIMITER);
					fileWriter.append(COMMA_DELIMITER);
					fileWriter.append(COMMA_DELIMITER);
				}
				if (customer.getContacts() != null && customer.getContacts().size() == 1) {
					ContactsExportVo customerContact = customer.getContacts().get(0);
					fileWriter.append(COMMA_DELIMITER);
					fileWriter.append(
							customerContact.getContactSalutation() != null ? customerContact.getContactSalutation()
									: "");
					fileWriter.append(COMMA_DELIMITER);
					fileWriter.append(
							customerContact.getContactFirstName() != null ? customerContact.getContactFirstName() : "");
					fileWriter.append(COMMA_DELIMITER);
					fileWriter.append(
							customerContact.getContactLastName() != null ? customerContact.getContactLastName() : "");
					fileWriter.append(COMMA_DELIMITER);
					fileWriter.append(
							customerContact.getContactWorkNo() != null ? customerContact.getContactWorkNo() : "");
					fileWriter.append(COMMA_DELIMITER);
					fileWriter.append(
							customerContact.getContactMobileNo() != null ? customerContact.getContactMobileNo() : "");
					fileWriter.append(COMMA_DELIMITER);
					fileWriter.append(
							customerContact.getContactEmailAddress() != null ? customerContact.getContactEmailAddress()
									: "");
				} else {
					if (customer.getContacts() != null && customer.getContacts().size() > 0) {
						ContactsExportVo customerContact = customer.getContacts().get(0);
						fileWriter.append(COMMA_DELIMITER);
						fileWriter.append(
								customerContact.getContactSalutation() != null ? customerContact.getContactSalutation()
										: "");
						fileWriter.append(COMMA_DELIMITER);
						fileWriter.append(
								customerContact.getContactFirstName() != null ? customerContact.getContactFirstName()
										: "");
						fileWriter.append(COMMA_DELIMITER);
						fileWriter.append(
								customerContact.getContactLastName() != null ? customerContact.getContactLastName()
										: "");
						fileWriter.append(COMMA_DELIMITER);
						fileWriter.append(
								customerContact.getContactWorkNo() != null ? customerContact.getContactWorkNo() : "");
						fileWriter.append(COMMA_DELIMITER);
						fileWriter.append(
								customerContact.getContactMobileNo() != null ? customerContact.getContactMobileNo()
										: "");
						fileWriter.append(COMMA_DELIMITER);
						fileWriter.append(customerContact.getContactEmailAddress() != null
								? customerContact.getContactEmailAddress()
								: "");
						int count = 0;
						for (ContactsExportVo customerContactDetails : customer.getContacts()) {
							if (count > 0) {
								fileWriter.append(NEW_LINE_SEPARATOR);
								fileWriter.append(String.valueOf(customer.getId()));
								fileWriter.append(COMMA_DELIMITER);
								fileWriter.append(
										customer.getPrimaryContact() != null ? customer.getPrimaryContact() : "");
								fileWriter.append(COMMA_DELIMITER);
								fileWriter.append(customer.getCompanyName() != null ? customer.getCompanyName() : "");
								fileWriter.append(COMMA_DELIMITER);
								fileWriter.append(
										customer.getCustomerDisplayName() != null ? customer.getCustomerDisplayName()
												: "");
								fileWriter.append(COMMA_DELIMITER);
								fileWriter.append(customer.getEmail() != null ? customer.getEmail() : "");
								fileWriter.append(COMMA_DELIMITER);
								fileWriter.append(customer.getPhone() != null ? customer.getPhone() : "");
								fileWriter.append(COMMA_DELIMITER);
								fileWriter.append(customer.getMobileNo() != null ? customer.getMobileNo() : "");
								fileWriter.append(COMMA_DELIMITER);
								fileWriter.append(customer.getWebsite() != null ? customer.getWebsite() : "");
								fileWriter.append(COMMA_DELIMITER);
								fileWriter.append(customer.getPan() != null ? customer.getPan() : "");
								fileWriter.append(COMMA_DELIMITER);
								fileWriter.append(
										customer.getOrganizationType() != null ? customer.getOrganizationType() : "");
								fileWriter.append(COMMA_DELIMITER);
								fileWriter
										.append(customer.getCustomerGroup() != null ? customer.getCustomerGroup() : "");
								fileWriter.append(COMMA_DELIMITER);
								fileWriter.append(customer.getGstRegnType() != null ? customer.getGstRegnType() : "");
								fileWriter.append(COMMA_DELIMITER);
								fileWriter.append(customer.getGstNumber() != null ? customer.getGstNumber() : "");
								fileWriter.append(COMMA_DELIMITER);
								fileWriter.append(customer.getCurrency() != null ? customer.getCurrency() : "");
								fileWriter.append(COMMA_DELIMITER);
								fileWriter.append(
										customer.getOpeningBalance() != null ? customer.getOpeningBalance() : "");
								fileWriter.append(COMMA_DELIMITER);
								fileWriter.append(customer.getPaymentTerms() != null ? customer.getPaymentTerms() : "");
								fileWriter.append(COMMA_DELIMITER);
								fileWriter.append(customer.getDeliveryAddrAttention() != null
										? customer.getDeliveryAddrAttention()
										: "");
								fileWriter.append(COMMA_DELIMITER);
								fileWriter.append(
										customer.getDeliveryAddrPhone() != null ? customer.getDeliveryAddrPhone() : "");
								fileWriter.append(COMMA_DELIMITER);
								fileWriter.append(
										customer.getDeliveryAddrCountry() != null ? customer.getDeliveryAddrCountry()
												: "");
								fileWriter.append(COMMA_DELIMITER);
								fileWriter.append(
										customer.getDeliveryAddrLine1() != null ? customer.getDeliveryAddrLine1() : "");
								fileWriter.append(COMMA_DELIMITER);
								fileWriter.append(
										customer.getDeliveryAddrLine2() != null ? customer.getDeliveryAddrLine2() : "");
								fileWriter.append(COMMA_DELIMITER);
								fileWriter.append(
										customer.getDeliveryAddrLandmark() != null ? customer.getDeliveryAddrLandmark()
												: "");
								fileWriter.append(COMMA_DELIMITER);
								fileWriter.append(
										customer.getDeliveryAddrState() != null ? customer.getDeliveryAddrState() : "");
								fileWriter.append(COMMA_DELIMITER);
								fileWriter.append(
										customer.getDeliveryAddrCity() != null ? customer.getDeliveryAddrCity() : "");
								fileWriter.append(COMMA_DELIMITER);
								fileWriter.append(
										customer.getDeliveryAddrPinCode() != null ? customer.getDeliveryAddrPinCode()
												: "");
								fileWriter.append(COMMA_DELIMITER);
								fileWriter.append(
										customer.getBillingAddrAttention() != null ? customer.getBillingAddrAttention()
												: "");
								fileWriter.append(COMMA_DELIMITER);
								fileWriter.append(
										customer.getBillingAddrPhone() != null ? customer.getBillingAddrPhone() : "");
								fileWriter.append(COMMA_DELIMITER);
								fileWriter.append(
										customer.getBillingAddrCountry() != null ? customer.getBillingAddrCountry()
												: "");
								fileWriter.append(COMMA_DELIMITER);
								fileWriter.append(
										customer.getBillingAddrLine1() != null ? customer.getBillingAddrLine1() : "");
								fileWriter.append(COMMA_DELIMITER);
								fileWriter.append(
										customer.getBillingAddrLine2() != null ? customer.getBillingAddrLine2() : "");
								fileWriter.append(COMMA_DELIMITER);
								fileWriter.append(
										customer.getBillingAddrLandmark() != null ? customer.getBillingAddrLandmark()
												: "");
								fileWriter.append(COMMA_DELIMITER);
								fileWriter.append(
										customer.getBillingAddrState() != null ? customer.getBillingAddrState() : "");
								fileWriter.append(COMMA_DELIMITER);
								fileWriter.append(
										customer.getBillingAddrCity() != null ? customer.getBillingAddrCity() : "");
								fileWriter.append(COMMA_DELIMITER);
								fileWriter.append(
										customer.getBillingAddrPinCode() != null ? customer.getBillingAddrPinCode()
												: "");
								fileWriter.append(COMMA_DELIMITER);
								fileWriter.append(customer.getStatus() != null ? customer.getStatus() : "");
								fileWriter.append(COMMA_DELIMITER);
								fileWriter.append(customerContactDetails.getContactSalutation() != null
										? customerContactDetails.getContactSalutation()
										: "");
								fileWriter.append(COMMA_DELIMITER);
								fileWriter.append(customerContactDetails.getContactFirstName() != null
										? customerContactDetails.getContactFirstName()
										: "");
								fileWriter.append(COMMA_DELIMITER);
								fileWriter.append(customerContactDetails.getContactLastName() != null
										? customerContactDetails.getContactLastName()
										: "");
								fileWriter.append(COMMA_DELIMITER);
								fileWriter.append(customerContactDetails.getContactWorkNo() != null
										? customerContactDetails.getContactWorkNo()
										: "");
								fileWriter.append(COMMA_DELIMITER);
								fileWriter.append(customerContactDetails.getContactMobileNo() != null
										? customerContactDetails.getContactMobileNo()
										: "");
								fileWriter.append(COMMA_DELIMITER);
								fileWriter.append(customerContactDetails.getContactEmailAddress() != null
										? customerContactDetails.getContactEmailAddress()
										: "");
							}
							count++;
						}
					}
				}
				if (customer.getBankDetails().size() == 0) {
					fileWriter.append(COMMA_DELIMITER);
					fileWriter.append(COMMA_DELIMITER);
					fileWriter.append(COMMA_DELIMITER);
					fileWriter.append(COMMA_DELIMITER);
					fileWriter.append(COMMA_DELIMITER);
					fileWriter.append(COMMA_DELIMITER);
					fileWriter.append(COMMA_DELIMITER);
				}
				if (customer.getContacts().size() == 0 && customer.getContacts().isEmpty()) {
					if (customer.getBankDetails() != null && customer.getBankDetails().size() == 1) {
						fileWriter.append(COMMA_DELIMITER);
						BankExportVo customerBank = customer.getBankDetails().get(0);
						fileWriter.append(customerBank.getBankNname() != null ? customerBank.getBankNname() : "");
						fileWriter.append(COMMA_DELIMITER);
						fileWriter.append(customerBank.getBranchName() != null ? customerBank.getBranchName() : "");
						fileWriter.append(COMMA_DELIMITER);
						fileWriter.append(
								customerBank.getAccountHolderName() != null ? customerBank.getAccountHolderName() : "");
						fileWriter.append(COMMA_DELIMITER);
						fileWriter.append(customerBank.getAccountNo() != null ? customerBank.getAccountNo() : "");
						fileWriter.append(COMMA_DELIMITER);
						fileWriter.append(customerBank.getIfscCode() != null ? customerBank.getIfscCode() : "");
						fileWriter.append(COMMA_DELIMITER);
						fileWriter.append(customerBank.getUpiId() != null ? customerBank.getUpiId() : "");
						fileWriter.append(COMMA_DELIMITER);
						fileWriter.append(String.valueOf(customerBank.getIsDefault().toString()));
					} else {
						if (customer.getBankDetails() != null && customer.getBankDetails().size() > 0) {
							BankExportVo customerBank = customer.getBankDetails().get(0);
							fileWriter.append(COMMA_DELIMITER);
							fileWriter.append(customerBank.getBankNname() != null ? customerBank.getBankNname() : "");
							fileWriter.append(COMMA_DELIMITER);
							fileWriter.append(customerBank.getBranchName() != null ? customerBank.getBranchName() : "");
							fileWriter.append(COMMA_DELIMITER);
							fileWriter.append(
									customerBank.getAccountHolderName() != null ? customerBank.getAccountHolderName()
											: "");
							fileWriter.append(COMMA_DELIMITER);
							fileWriter.append(customerBank.getAccountNo() != null ? customerBank.getAccountNo() : "");
							fileWriter.append(COMMA_DELIMITER);
							fileWriter.append(customerBank.getIfscCode() != null ? customerBank.getIfscCode() : "");
							fileWriter.append(COMMA_DELIMITER);
							fileWriter.append(customerBank.getUpiId() != null ? customerBank.getUpiId() : "");
							fileWriter.append(COMMA_DELIMITER);
							fileWriter.append(String.valueOf(customerBank.getIsDefault().toString()));
							int count = 0;
							for (BankExportVo customerBankDetails : customer.getBankDetails()) {
								if (count > 0) {
									fileWriter.append(NEW_LINE_SEPARATOR);
									fileWriter.append(String.valueOf(customer.getId()));
									fileWriter.append(COMMA_DELIMITER);
									fileWriter.append(
											customer.getPrimaryContact() != null ? customer.getPrimaryContact() : "");
									fileWriter.append(COMMA_DELIMITER);
									fileWriter
											.append(customer.getCompanyName() != null ? customer.getCompanyName() : "");
									fileWriter.append(COMMA_DELIMITER);
									fileWriter.append(customer.getCustomerDisplayName() != null
											? customer.getCustomerDisplayName()
											: "");
									fileWriter.append(COMMA_DELIMITER);
									fileWriter.append(customer.getEmail() != null ? customer.getEmail() : "");
									fileWriter.append(COMMA_DELIMITER);
									fileWriter.append(customer.getPhone() != null ? customer.getPhone() : "");
									fileWriter.append(COMMA_DELIMITER);
									fileWriter.append(customer.getMobileNo() != null ? customer.getMobileNo() : "");
									fileWriter.append(COMMA_DELIMITER);
									fileWriter.append(customer.getWebsite() != null ? customer.getWebsite() : "");
									fileWriter.append(COMMA_DELIMITER);
									fileWriter.append(customer.getPan() != null ? customer.getPan() : "");
									fileWriter.append(COMMA_DELIMITER);
									fileWriter.append(
											customer.getOrganizationType() != null ? customer.getOrganizationType()
													: "");
									fileWriter.append(COMMA_DELIMITER);
									fileWriter.append(
											customer.getCustomerGroup() != null ? customer.getCustomerGroup() : "");
									fileWriter.append(COMMA_DELIMITER);
									fileWriter
											.append(customer.getGstRegnType() != null ? customer.getGstRegnType() : "");
									fileWriter.append(COMMA_DELIMITER);
									fileWriter.append(customer.getGstNumber() != null ? customer.getGstNumber() : "");
									fileWriter.append(COMMA_DELIMITER);
									fileWriter.append(customer.getCurrency() != null ? customer.getCurrency() : "");
									fileWriter.append(COMMA_DELIMITER);
									fileWriter.append(
											customer.getOpeningBalance() != null ? customer.getOpeningBalance() : "");
									fileWriter.append(COMMA_DELIMITER);
									fileWriter.append(
											customer.getPaymentTerms() != null ? customer.getPaymentTerms() : "");
									fileWriter.append(COMMA_DELIMITER);
									fileWriter.append(customer.getDeliveryAddrAttention() != null
											? customer.getDeliveryAddrAttention()
											: "");
									fileWriter.append(COMMA_DELIMITER);
									fileWriter.append(
											customer.getDeliveryAddrPhone() != null ? customer.getDeliveryAddrPhone()
													: "");
									fileWriter.append(COMMA_DELIMITER);
									fileWriter.append(customer.getDeliveryAddrCountry() != null
											? customer.getDeliveryAddrCountry()
											: "");
									fileWriter.append(COMMA_DELIMITER);
									fileWriter.append(
											customer.getDeliveryAddrLine1() != null ? customer.getDeliveryAddrLine1()
													: "");
									fileWriter.append(COMMA_DELIMITER);
									fileWriter.append(
											customer.getDeliveryAddrLine2() != null ? customer.getDeliveryAddrLine2()
													: "");
									fileWriter.append(COMMA_DELIMITER);
									fileWriter.append(customer.getDeliveryAddrLandmark() != null
											? customer.getDeliveryAddrLandmark()
											: "");
									fileWriter.append(COMMA_DELIMITER);
									fileWriter.append(
											customer.getDeliveryAddrState() != null ? customer.getDeliveryAddrState()
													: "");
									fileWriter.append(COMMA_DELIMITER);
									fileWriter.append(
											customer.getDeliveryAddrCity() != null ? customer.getDeliveryAddrCity()
													: "");
									fileWriter.append(COMMA_DELIMITER);
									fileWriter.append(customer.getDeliveryAddrPinCode() != null
											? customer.getDeliveryAddrPinCode()
											: "");
									fileWriter.append(COMMA_DELIMITER);
									fileWriter.append(customer.getBillingAddrAttention() != null
											? customer.getBillingAddrAttention()
											: "");
									fileWriter.append(COMMA_DELIMITER);
									fileWriter.append(
											customer.getBillingAddrPhone() != null ? customer.getBillingAddrPhone()
													: "");
									fileWriter.append(COMMA_DELIMITER);
									fileWriter.append(
											customer.getBillingAddrCountry() != null ? customer.getBillingAddrCountry()
													: "");
									fileWriter.append(COMMA_DELIMITER);
									fileWriter.append(
											customer.getBillingAddrLine1() != null ? customer.getBillingAddrLine1()
													: "");
									fileWriter.append(COMMA_DELIMITER);
									fileWriter.append(
											customer.getBillingAddrLine2() != null ? customer.getBillingAddrLine2()
													: "");
									fileWriter.append(COMMA_DELIMITER);
									fileWriter.append(customer.getBillingAddrLandmark() != null
											? customer.getBillingAddrLandmark()
											: "");
									fileWriter.append(COMMA_DELIMITER);
									fileWriter.append(
											customer.getBillingAddrState() != null ? customer.getBillingAddrState()
													: "");
									fileWriter.append(COMMA_DELIMITER);
									fileWriter.append(
											customer.getBillingAddrCity() != null ? customer.getBillingAddrCity() : "");
									fileWriter.append(COMMA_DELIMITER);
									fileWriter.append(
											customer.getBillingAddrPinCode() != null ? customer.getBillingAddrPinCode()
													: "");
									fileWriter.append(COMMA_DELIMITER);
									fileWriter.append(customer.getStatus() != null ? customer.getStatus() : "");
									fileWriter.append(COMMA_DELIMITER);
									fileWriter.append(COMMA_DELIMITER);
									fileWriter.append(COMMA_DELIMITER);
									fileWriter.append(COMMA_DELIMITER);
									fileWriter.append(COMMA_DELIMITER);
									fileWriter.append(COMMA_DELIMITER);
									fileWriter.append(COMMA_DELIMITER);
									fileWriter.append(customerBankDetails.getBankNname() != null
											? customerBankDetails.getBankNname()
											: "");
									fileWriter.append(COMMA_DELIMITER);
									fileWriter.append(customerBankDetails.getBranchName() != null
											? customerBankDetails.getBranchName()
											: "");
									fileWriter.append(COMMA_DELIMITER);
									fileWriter.append(customerBankDetails.getAccountHolderName() != null
											? customerBankDetails.getAccountHolderName()
											: "");
									fileWriter.append(COMMA_DELIMITER);
									fileWriter.append(customerBankDetails.getAccountNo() != null
											? customerBankDetails.getAccountNo()
											: "");
									fileWriter.append(COMMA_DELIMITER);
									fileWriter.append(customerBankDetails.getIfscCode() != null
											? customerBankDetails.getIfscCode()
											: "");
									fileWriter.append(COMMA_DELIMITER);
									fileWriter.append(
											customerBankDetails.getUpiId() != null ? customerBankDetails.getUpiId()
													: "");
									fileWriter.append(COMMA_DELIMITER);
									fileWriter.append(String.valueOf(customerBankDetails.getIsDefault().toString()));
								}
								count++;
							}

						}
					}

				} else {
					if (customer.getBankDetails() != null && customer.getBankDetails().size() > 0) {
						for (BankExportVo customerBankDetails : customer.getBankDetails()) {
							fileWriter.append(NEW_LINE_SEPARATOR);
							fileWriter.append(String.valueOf(customer.getId()));
							fileWriter.append(COMMA_DELIMITER);
							fileWriter.append(customer.getPrimaryContact() != null ? customer.getPrimaryContact() : "");
							fileWriter.append(COMMA_DELIMITER);
							fileWriter.append(customer.getCompanyName() != null ? customer.getCompanyName() : "");
							fileWriter.append(COMMA_DELIMITER);
							fileWriter.append(
									customer.getCustomerDisplayName() != null ? customer.getCustomerDisplayName() : "");
							fileWriter.append(COMMA_DELIMITER);
							fileWriter.append(customer.getEmail() != null ? customer.getEmail() : "");
							fileWriter.append(COMMA_DELIMITER);
							fileWriter.append(customer.getPhone() != null ? customer.getPhone() : "");
							fileWriter.append(COMMA_DELIMITER);
							fileWriter.append(customer.getMobileNo() != null ? customer.getMobileNo() : "");
							fileWriter.append(COMMA_DELIMITER);
							fileWriter.append(customer.getWebsite() != null ? customer.getWebsite() : "");
							fileWriter.append(COMMA_DELIMITER);
							fileWriter.append(customer.getPan() != null ? customer.getPan() : "");
							fileWriter.append(COMMA_DELIMITER);
							fileWriter.append(
									customer.getOrganizationType() != null ? customer.getOrganizationType() : "");
							fileWriter.append(COMMA_DELIMITER);
							fileWriter.append(customer.getCustomerGroup() != null ? customer.getCustomerGroup() : "");
							fileWriter.append(COMMA_DELIMITER);
							fileWriter.append(customer.getGstRegnType() != null ? customer.getGstRegnType() : "");
							fileWriter.append(COMMA_DELIMITER);
							fileWriter.append(customer.getGstNumber() != null ? customer.getGstNumber() : "");
							fileWriter.append(COMMA_DELIMITER);
							fileWriter.append(customer.getCurrency() != null ? customer.getCurrency() : "");
							fileWriter.append(COMMA_DELIMITER);
							fileWriter.append(customer.getOpeningBalance() != null ? customer.getOpeningBalance() : "");
							fileWriter.append(COMMA_DELIMITER);
							fileWriter.append(customer.getPaymentTerms() != null ? customer.getPaymentTerms() : "");
							fileWriter.append(COMMA_DELIMITER);
							fileWriter.append(
									customer.getDeliveryAddrAttention() != null ? customer.getDeliveryAddrAttention()
											: "");
							fileWriter.append(COMMA_DELIMITER);
							fileWriter.append(
									customer.getDeliveryAddrPhone() != null ? customer.getDeliveryAddrPhone() : "");
							fileWriter.append(COMMA_DELIMITER);
							fileWriter.append(
									customer.getDeliveryAddrCountry() != null ? customer.getDeliveryAddrCountry() : "");
							fileWriter.append(COMMA_DELIMITER);
							fileWriter.append(
									customer.getDeliveryAddrLine1() != null ? customer.getDeliveryAddrLine1() : "");
							fileWriter.append(COMMA_DELIMITER);
							fileWriter.append(
									customer.getDeliveryAddrLine2() != null ? customer.getDeliveryAddrLine2() : "");
							fileWriter.append(COMMA_DELIMITER);
							fileWriter.append(
									customer.getDeliveryAddrLandmark() != null ? customer.getDeliveryAddrLandmark()
											: "");
							fileWriter.append(COMMA_DELIMITER);
							fileWriter.append(
									customer.getDeliveryAddrState() != null ? customer.getDeliveryAddrState() : "");
							fileWriter.append(COMMA_DELIMITER);
							fileWriter.append(
									customer.getDeliveryAddrCity() != null ? customer.getDeliveryAddrCity() : "");
							fileWriter.append(COMMA_DELIMITER);
							fileWriter.append(
									customer.getDeliveryAddrPinCode() != null ? customer.getDeliveryAddrPinCode() : "");
							fileWriter.append(COMMA_DELIMITER);
							fileWriter.append(
									customer.getBillingAddrAttention() != null ? customer.getBillingAddrAttention()
											: "");
							fileWriter.append(COMMA_DELIMITER);
							fileWriter.append(
									customer.getBillingAddrPhone() != null ? customer.getBillingAddrPhone() : "");
							fileWriter.append(COMMA_DELIMITER);
							fileWriter.append(
									customer.getBillingAddrCountry() != null ? customer.getBillingAddrCountry() : "");
							fileWriter.append(COMMA_DELIMITER);
							fileWriter.append(
									customer.getBillingAddrLine1() != null ? customer.getBillingAddrLine1() : "");
							fileWriter.append(COMMA_DELIMITER);
							fileWriter.append(
									customer.getBillingAddrLine2() != null ? customer.getBillingAddrLine2() : "");
							fileWriter.append(COMMA_DELIMITER);
							fileWriter.append(
									customer.getBillingAddrLandmark() != null ? customer.getBillingAddrLandmark() : "");
							fileWriter.append(COMMA_DELIMITER);
							fileWriter.append(
									customer.getBillingAddrState() != null ? customer.getBillingAddrState() : "");
							fileWriter.append(COMMA_DELIMITER);
							fileWriter
									.append(customer.getBillingAddrCity() != null ? customer.getBillingAddrCity() : "");
							fileWriter.append(COMMA_DELIMITER);
							fileWriter.append(
									customer.getBillingAddrPinCode() != null ? customer.getBillingAddrPinCode() : "");
							fileWriter.append(COMMA_DELIMITER);
							fileWriter.append(customer.getStatus() != null ? customer.getStatus() : "");
							fileWriter.append(COMMA_DELIMITER);
							fileWriter.append(COMMA_DELIMITER);
							fileWriter.append(COMMA_DELIMITER);
							fileWriter.append(COMMA_DELIMITER);
							fileWriter.append(COMMA_DELIMITER);
							fileWriter.append(COMMA_DELIMITER);
							fileWriter.append(COMMA_DELIMITER);
							fileWriter.append(
									customerBankDetails.getBankNname() != null ? customerBankDetails.getBankNname()
											: "");
							fileWriter.append(COMMA_DELIMITER);
							fileWriter.append(
									customerBankDetails.getBranchName() != null ? customerBankDetails.getBranchName()
											: "");
							fileWriter.append(COMMA_DELIMITER);
							fileWriter.append(customerBankDetails.getAccountHolderName() != null
									? customerBankDetails.getAccountHolderName()
									: "");
							fileWriter.append(COMMA_DELIMITER);
							fileWriter.append(
									customerBankDetails.getAccountNo() != null ? customerBankDetails.getAccountNo()
											: "");
							fileWriter.append(COMMA_DELIMITER);
							fileWriter.append(
									customerBankDetails.getIfscCode() != null ? customerBankDetails.getIfscCode() : "");
							fileWriter.append(COMMA_DELIMITER);
							fileWriter.append(
									customerBankDetails.getUpiId() != null ? customerBankDetails.getUpiId() : "");
							fileWriter.append(COMMA_DELIMITER);
							fileWriter.append(String.valueOf(customerBankDetails.getIsDefault().toString()));
						}
					}
				}
				fileWriter.append(NEW_LINE_SEPARATOR);
			}
		} catch (Exception e) {
			System.out.println("Error in CsvFileWriter !!!");
			throw new ApplicationException(e);
		} finally {
			try {
				fileWriter.flush();
				fileWriter.close();
			} catch (IOException e) {
				System.out.println("Error while flushing/closing fileWriter !!!");
				throw new ApplicationException(e);
			}

		}

		String exportPath = filePath.toString();
		byte[] fileBytes = null;
		try {
			fileBytes = Files.readAllBytes(new File(exportPath).toPath());
		} catch (IOException e) {
			throw new ApplicationException(e);
		}
		if (fileBytes.length > 0) {
			logger.info("To encode the bytes  ::");
			encodedString = Base64.getEncoder().encodeToString(fileBytes);
			filePath.delete();
		}

		return encodedString;
	}

	public String exportAccountingEntryXlsx(List<AccountingEntryExportVo> data, String moduleName, String fileType)
			throws ApplicationException {
		logger.info("Entry into method: exportAccountingEntryXlsx");
		String encodedString = new String();
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet(moduleName);
		/** Header Row */
		Row headerRow = sheet.createRow(0);
		// Creating cells
		headerRow.createCell(0).setCellValue("id");
		headerRow.createCell(1).setCellValue("date");
		headerRow.createCell(2).setCellValue("type");
		headerRow.createCell(3).setCellValue("journal_no");
		headerRow.createCell(4).setCellValue("location");
		headerRow.createCell(5).setCellValue("level1_email");
		headerRow.createCell(6).setCellValue("level2_email");
		headerRow.createCell(7).setCellValue("level3_email");
		headerRow.createCell(8).setCellValue("status");
		headerRow.createCell(9).setCellValue("ledger");
		headerRow.createCell(10).setCellValue("sub_ledger");
		headerRow.createCell(11).setCellValue("currency");
		headerRow.createCell(12).setCellValue("exchange_rate");
		headerRow.createCell(13).setCellValue("credits");
		headerRow.createCell(14).setCellValue("debits");
		int rowNum = 1;

		/** Writing Data */
		for (AccountingEntryExportVo accountingEntry : data) {

			// Create ROW
			Row row = sheet.createRow(rowNum++);
			// Creating cells
			row.createCell(0).setCellValue(accountingEntry.getId());
			row.createCell(1).setCellValue(accountingEntry.getDate().toString());
			row.createCell(2).setCellValue(accountingEntry.getType());
			row.createCell(3).setCellValue(accountingEntry.getJournalNo());
			row.createCell(4).setCellValue(accountingEntry.getLocation());
			row.createCell(5).setCellValue(accountingEntry.getLevel1Email());
			row.createCell(6).setCellValue(accountingEntry.getLevel2Email());
			row.createCell(7).setCellValue(accountingEntry.getLevel3Email());
			row.createCell(8).setCellValue(accountingEntry.getStatus());
			if (accountingEntry.getAccountingEntryItems() != null
					&& accountingEntry.getAccountingEntryItems().size() == 1) {
				AccountingEntryItemExportVo accountingEntryItem = accountingEntry.getAccountingEntryItems().get(0);
				row.createCell(9).setCellValue(accountingEntryItem.getLedger());
				row.createCell(10).setCellValue(accountingEntryItem.getSubLedger());
				row.createCell(11).setCellValue(accountingEntryItem.getCurrency());
				row.createCell(12).setCellValue(accountingEntryItem.getExchangeRate());
				row.createCell(13).setCellValue(accountingEntryItem.getCredits());
				row.createCell(14).setCellValue(accountingEntryItem.getDebits());
			} else {
				if (accountingEntry.getAccountingEntryItems() != null
						&& accountingEntry.getAccountingEntryItems().size() > 0) {
					AccountingEntryItemExportVo accountingEntryItem = accountingEntry.getAccountingEntryItems().get(0);
					row.createCell(9).setCellValue(accountingEntryItem.getLedger());
					row.createCell(10).setCellValue(accountingEntryItem.getSubLedger());
					row.createCell(11).setCellValue(accountingEntryItem.getCurrency());
					row.createCell(12).setCellValue(accountingEntryItem.getExchangeRate());
					row.createCell(13).setCellValue(accountingEntryItem.getCredits());
					row.createCell(14).setCellValue(accountingEntryItem.getDebits());
					int count = 0;
					for (AccountingEntryItemExportVo accountingEntryItemDetails : accountingEntry
							.getAccountingEntryItems()) {
						if (count > 0) {
							row = sheet.createRow(rowNum++);
							row.createCell(0).setCellValue(accountingEntry.getId());
							row.createCell(1).setCellValue(accountingEntry.getDate().toString());
							row.createCell(2).setCellValue(accountingEntry.getType());
							row.createCell(3).setCellValue(accountingEntry.getJournalNo());
							row.createCell(4).setCellValue(accountingEntry.getLocation());
							row.createCell(5).setCellValue(accountingEntry.getLevel1Email());
							row.createCell(6).setCellValue(accountingEntry.getLevel2Email());
							row.createCell(7).setCellValue(accountingEntry.getLevel3Email());
							row.createCell(8).setCellValue(accountingEntry.getStatus());
							row.createCell(9).setCellValue(accountingEntryItemDetails.getLedger());
							row.createCell(10).setCellValue(accountingEntryItemDetails.getSubLedger());
							row.createCell(11).setCellValue(accountingEntryItemDetails.getCurrency());
							row.createCell(12).setCellValue(accountingEntryItemDetails.getExchangeRate());
							row.createCell(13).setCellValue(accountingEntryItemDetails.getCredits());
							row.createCell(14).setCellValue(accountingEntryItemDetails.getDebits());
						}
						count++;

					}
				}
			}

		}
		encodedString = encodingXLSXFile(moduleName, fileType, workbook);
		return encodedString;
	}

	public String exportAccountingEntryCsv(List<AccountingEntryExportVo> data, String moduleName, String fileType)
			throws ApplicationException {
		logger.info("Entry into method: exportAccountingEntryCsv");
		final String COMMA_DELIMITER = ",";
		final String NEW_LINE_SEPARATOR = "\n";
		final String ACCOUNTING_ENTRY_FILE_HEADER = "id,date,type,journal_no,location,level1_email,level2_email,level3_email,status,ledger,sub_ledger,currency,exchange_rate,credits,debits";
		FileWriter fileWriter = null;
		File filePath;
		String encodedString = new String();
		try {
			FileReader reader;
			reader = new FileReader("/decifer/config/app_config.properties");
			Properties p = new Properties();
			p.load(reader);
			StringBuilder path = new StringBuilder(p.getProperty("file_store_path"));
			String pathtoSave = path.toString();
			filePath = new File(pathtoSave + "/" + moduleName + "." + fileType);

			fileWriter = new FileWriter(filePath);

			// Write the CSV file header
			fileWriter.append(ACCOUNTING_ENTRY_FILE_HEADER.toString());

			// Add a new line separator after the header
			fileWriter.append(NEW_LINE_SEPARATOR);

			for (AccountingEntryExportVo accountingEntry : data) {
				fileWriter.append(String.valueOf(accountingEntry.getId()));
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(accountingEntry.getDate().toString());
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(accountingEntry.getType() != null ? accountingEntry.getType() : "");
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(accountingEntry.getJournalNo() != null ? accountingEntry.getJournalNo() : "");
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(accountingEntry.getLocation() != null ? accountingEntry.getLocation() : "");
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(accountingEntry.getLevel1Email() != null ? accountingEntry.getLevel1Email() : "");
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(accountingEntry.getLevel2Email() != null ? accountingEntry.getLevel2Email() : "");
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(accountingEntry.getLevel3Email() != null ? accountingEntry.getLevel3Email() : "");
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(accountingEntry.getStatus() != null ? accountingEntry.getStatus() : "");
				if (accountingEntry.getAccountingEntryItems().size() == 0) {
					fileWriter.append(COMMA_DELIMITER);
					fileWriter.append(COMMA_DELIMITER);
					fileWriter.append(COMMA_DELIMITER);
					fileWriter.append(COMMA_DELIMITER);
					fileWriter.append(COMMA_DELIMITER);
					fileWriter.append(COMMA_DELIMITER);
				}
				if (accountingEntry.getAccountingEntryItems() != null
						&& accountingEntry.getAccountingEntryItems().size() == 1) {
					AccountingEntryItemExportVo accountingEntryItem = accountingEntry.getAccountingEntryItems().get(0);
					fileWriter.append(COMMA_DELIMITER);
					fileWriter.append(accountingEntryItem.getLedger() != null ? accountingEntryItem.getLedger() : "");
					fileWriter.append(COMMA_DELIMITER);
					fileWriter.append(
							accountingEntryItem.getSubLedger() != null ? accountingEntryItem.getSubLedger() : "");
					fileWriter.append(COMMA_DELIMITER);
					fileWriter
							.append(accountingEntryItem.getCurrency() != null ? accountingEntryItem.getCurrency() : "");
					fileWriter.append(COMMA_DELIMITER);
					fileWriter.append(
							accountingEntryItem.getExchangeRate() != null ? accountingEntryItem.getExchangeRate() : "");
					fileWriter.append(COMMA_DELIMITER);
					fileWriter.append(accountingEntryItem.getCredits() != null ? accountingEntryItem.getCredits() : "");
					fileWriter.append(COMMA_DELIMITER);
					fileWriter.append(accountingEntryItem.getDebits() != null ? accountingEntryItem.getDebits() : "");
				} else {
					if (accountingEntry.getAccountingEntryItems() != null
							&& accountingEntry.getAccountingEntryItems().size() > 0) {
						AccountingEntryItemExportVo accountingEntryItem = accountingEntry.getAccountingEntryItems()
								.get(0);
						fileWriter.append(COMMA_DELIMITER);
						fileWriter
								.append(accountingEntryItem.getLedger() != null ? accountingEntryItem.getLedger() : "");
						fileWriter.append(COMMA_DELIMITER);
						fileWriter.append(
								accountingEntryItem.getSubLedger() != null ? accountingEntryItem.getSubLedger() : "");
						fileWriter.append(COMMA_DELIMITER);
						fileWriter.append(
								accountingEntryItem.getCurrency() != null ? accountingEntryItem.getCurrency() : "");
						fileWriter.append(COMMA_DELIMITER);
						fileWriter.append(
								accountingEntryItem.getExchangeRate() != null ? accountingEntryItem.getExchangeRate()
										: "");
						fileWriter.append(COMMA_DELIMITER);
						fileWriter.append(
								accountingEntryItem.getCredits() != null ? accountingEntryItem.getCredits() : "");
						fileWriter.append(COMMA_DELIMITER);
						fileWriter
								.append(accountingEntryItem.getDebits() != null ? accountingEntryItem.getDebits() : "");
						int count = 0;
						for (AccountingEntryItemExportVo accountingEntryItemDetails : accountingEntry
								.getAccountingEntryItems()) {
							if (count > 0) {
								fileWriter.append(NEW_LINE_SEPARATOR);
								fileWriter.append(String.valueOf(accountingEntry.getId()));
								fileWriter.append(COMMA_DELIMITER);
								fileWriter.append(accountingEntry.getDate().toString());
								fileWriter.append(COMMA_DELIMITER);
								fileWriter.append(accountingEntry.getType() != null ? accountingEntry.getType() : "");
								fileWriter.append(COMMA_DELIMITER);
								fileWriter.append(
										accountingEntry.getJournalNo() != null ? accountingEntry.getJournalNo() : "");
								fileWriter.append(COMMA_DELIMITER);
								fileWriter.append(
										accountingEntry.getLocation() != null ? accountingEntry.getLocation() : "");
								fileWriter.append(COMMA_DELIMITER);
								fileWriter.append(
										accountingEntry.getLevel1Email() != null ? accountingEntry.getLevel1Email()
												: "");
								fileWriter.append(COMMA_DELIMITER);
								fileWriter.append(
										accountingEntry.getLevel2Email() != null ? accountingEntry.getLevel2Email()
												: "");
								fileWriter.append(COMMA_DELIMITER);
								fileWriter.append(
										accountingEntry.getLevel3Email() != null ? accountingEntry.getLevel3Email()
												: "");
								fileWriter.append(COMMA_DELIMITER);
								fileWriter
										.append(accountingEntry.getStatus() != null ? accountingEntry.getStatus() : "");
								fileWriter.append(COMMA_DELIMITER);
								fileWriter.append(accountingEntryItemDetails.getLedger() != null
										? accountingEntryItemDetails.getLedger()
										: "");
								fileWriter.append(COMMA_DELIMITER);
								fileWriter.append(accountingEntryItemDetails.getSubLedger() != null
										? accountingEntryItemDetails.getSubLedger()
										: "");
								fileWriter.append(COMMA_DELIMITER);
								fileWriter.append(accountingEntryItemDetails.getCurrency() != null
										? accountingEntryItemDetails.getCurrency()
										: "");
								fileWriter.append(COMMA_DELIMITER);
								fileWriter.append(accountingEntryItemDetails.getExchangeRate() != null
										? accountingEntryItemDetails.getExchangeRate()
										: "");
								fileWriter.append(COMMA_DELIMITER);
								fileWriter.append(accountingEntryItemDetails.getCredits() != null
										? accountingEntryItemDetails.getCredits()
										: "");
								fileWriter.append(COMMA_DELIMITER);
								fileWriter.append(accountingEntryItemDetails.getDebits() != null
										? accountingEntryItemDetails.getDebits()
										: "");
							}
							count++;
						}

					}
				}
				fileWriter.append(NEW_LINE_SEPARATOR);
			}

		} catch (Exception e) {
			System.out.println("Error in CsvFileWriter !!!");
			throw new ApplicationException(e);
		} finally {
			try {
				fileWriter.flush();
				fileWriter.close();
			} catch (IOException e) {
				System.out.println("Error while flushing/closing fileWriter !!!");
				throw new ApplicationException(e);
			}

		}

		String exportPath = filePath.toString();
		byte[] fileBytes = null;
		try {
			fileBytes = Files.readAllBytes(new File(exportPath).toPath());
		} catch (IOException e) {
			throw new ApplicationException(e);
		}
		if (fileBytes.length > 0) {
			logger.info("To encode the bytes  ::");
			encodedString = Base64.getEncoder().encodeToString(fileBytes);
			filePath.delete();
		}

		return encodedString;
	}

	public String exportContraXlsx(List<ContraExportVo> data, String moduleName, String fileType)
			throws ApplicationException {
		logger.info("Entry into method: exportContraXlsx");
		String encodedString = new String();
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet(moduleName);
		
		/** Header Row */
		Row headerRow = sheet.createRow(0);
		// Creating cells
		headerRow.createCell(0).setCellValue("id");
		headerRow.createCell(1).setCellValue("date");
		headerRow.createCell(2).setCellValue("reference_no");
		headerRow.createCell(3).setCellValue("status");
		headerRow.createCell(4).setCellValue("account_name");
		headerRow.createCell(5).setCellValue("currency");
		headerRow.createCell(6).setCellValue("exchange_rate");
		headerRow.createCell(7).setCellValue("credit");
		headerRow.createCell(8).setCellValue("debit");
		int rowNum = 1;

		/** Writing Data */
		for (ContraExportVo contra : data) {

			// Create ROW
			Row row = sheet.createRow(rowNum++);
			// Creating cells
			row.createCell(0).setCellValue(contra.getId());
			row.createCell(1).setCellValue(contra.getDate().toString());
			row.createCell(2).setCellValue(contra.getReferenceNo());
			row.createCell(3).setCellValue(contra.getStatus());
			if (contra.getContraEntries() != null && contra.getContraEntries().size() == 1) {
				ContraEntriesExportVo contraEntries = contra.getContraEntries().get(0);
				row.createCell(4).setCellValue(contraEntries.getAccountName());
				row.createCell(5).setCellValue(contraEntries.getCurrency());
				row.createCell(6).setCellValue(contraEntries.getExchnageRate());
				row.createCell(7).setCellValue(contraEntries.getCredit());
				row.createCell(8).setCellValue(contraEntries.getDebit());
			} else {
				if (contra.getContraEntries() != null && contra.getContraEntries().size() > 0) {
					ContraEntriesExportVo contraEntries = contra.getContraEntries().get(0);
					row.createCell(4).setCellValue(contraEntries.getAccountName());
					row.createCell(5).setCellValue(contraEntries.getCurrency());
					row.createCell(6).setCellValue(contraEntries.getExchnageRate());
					row.createCell(7).setCellValue(contraEntries.getCredit());
					row.createCell(8).setCellValue(contraEntries.getDebit());
					int count = 0;
					for (ContraEntriesExportVo contraEntriesDetails : contra.getContraEntries()) {
						if (count > 0) {
							row = sheet.createRow(rowNum++);
							row.createCell(0).setCellValue(contra.getId());
							row.createCell(1).setCellValue(contra.getDate().toString());
							row.createCell(2).setCellValue(contra.getReferenceNo());
							row.createCell(3).setCellValue(contra.getStatus());
							row.createCell(4).setCellValue(contraEntriesDetails.getAccountName());
							row.createCell(5).setCellValue(contraEntriesDetails.getCurrency());
							row.createCell(6).setCellValue(contraEntriesDetails.getExchnageRate());
							row.createCell(7).setCellValue(contraEntriesDetails.getCredit());
							row.createCell(8).setCellValue(contraEntriesDetails.getDebit());
						}
						count++;

					}
				}
			}

		}
		encodedString = encodingXLSXFile(moduleName, fileType, workbook);
		return encodedString;
	}

	public String exportContraCsv(List<ContraExportVo> data, String moduleName, String fileType)
			throws ApplicationException {
		logger.info("Entry into method: exportContraCsv");
		final String COMMA_DELIMITER = ",";
		final String NEW_LINE_SEPARATOR = "\n";
		final String CONTRA_FILE_HEADER = "id,date,reference_no,status,account_name,currency,exchange_rate,credit,debit";
		FileWriter fileWriter = null;
		File filePath;
		String encodedString = new String();
		try {
			FileReader reader;
			reader = new FileReader("/decifer/config/app_config.properties");
			Properties p = new Properties();
			p.load(reader);
			StringBuilder path = new StringBuilder(p.getProperty("file_store_path"));
			String pathtoSave = path.toString();
			filePath = new File(pathtoSave + "/" + moduleName + "." + fileType);

			fileWriter = new FileWriter(filePath);

			// Write the CSV file header
			fileWriter.append(CONTRA_FILE_HEADER.toString());

			// Add a new line separator after the header
			fileWriter.append(NEW_LINE_SEPARATOR);

			for (ContraExportVo contra : data) {
				fileWriter.append(String.valueOf(contra.getId()));
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(contra.getDate().toString());
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(contra.getReferenceNo() != null ? contra.getReferenceNo() : "");
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(contra.getStatus() != null ? contra.getStatus() : "");
				if (contra.getContraEntries().size() == 0) {
					fileWriter.append(COMMA_DELIMITER);
					fileWriter.append(COMMA_DELIMITER);
					fileWriter.append(COMMA_DELIMITER);
					fileWriter.append(COMMA_DELIMITER);
				}
				if (contra.getContraEntries() != null && contra.getContraEntries().size() == 1) {
					ContraEntriesExportVo contraEntriesExportVo = contra.getContraEntries().get(0);
					fileWriter.append(COMMA_DELIMITER);
					fileWriter.append(
							contraEntriesExportVo.getAccountName() != null ? contraEntriesExportVo.getAccountName()
									: "");
					fileWriter.append(COMMA_DELIMITER);
					fileWriter.append(
							contraEntriesExportVo.getCurrency() != null ? contraEntriesExportVo.getCurrency() : "");
					fileWriter.append(COMMA_DELIMITER);
					fileWriter.append(
							contraEntriesExportVo.getExchnageRate() != null ? contraEntriesExportVo.getExchnageRate()
									: "");
					fileWriter.append(COMMA_DELIMITER);
					fileWriter
							.append(contraEntriesExportVo.getCredit() != null ? contraEntriesExportVo.getCredit() : "");
					fileWriter.append(COMMA_DELIMITER);
					fileWriter.append(contraEntriesExportVo.getDebit() != null ? contraEntriesExportVo.getDebit() : "");
				} else {
					if (contra.getContraEntries() != null && contra.getContraEntries().size() > 0) {
						ContraEntriesExportVo contraEntries = contra.getContraEntries().get(0);
						fileWriter.append(COMMA_DELIMITER);
						fileWriter.append(contraEntries.getAccountName() != null ? contraEntries.getAccountName() : "");
						fileWriter.append(COMMA_DELIMITER);
						fileWriter.append(contraEntries.getCurrency() != null ? contraEntries.getCurrency() : "");
						fileWriter.append(COMMA_DELIMITER);
						fileWriter
								.append(contraEntries.getExchnageRate() != null ? contraEntries.getExchnageRate() : "");
						fileWriter.append(COMMA_DELIMITER);
						fileWriter.append(contraEntries.getCredit() != null ? contraEntries.getCredit() : "");
						fileWriter.append(COMMA_DELIMITER);
						fileWriter.append(contraEntries.getDebit() != null ? contraEntries.getDebit() : "");
						int count = 0;
						for (ContraEntriesExportVo contraEntriesDetails : contra.getContraEntries()) {
							if (count > 0) {
								fileWriter.append(NEW_LINE_SEPARATOR);
								fileWriter.append(String.valueOf(contra.getId()));
								fileWriter.append(COMMA_DELIMITER);
								fileWriter.append(contra.getDate().toString());
								fileWriter.append(COMMA_DELIMITER);
								fileWriter.append(contra.getReferenceNo() != null ? contra.getReferenceNo() : "");
								fileWriter.append(COMMA_DELIMITER);
								fileWriter.append(contra.getStatus() != null ? contra.getStatus() : "");
								fileWriter.append(COMMA_DELIMITER);
								fileWriter.append(contraEntriesDetails.getAccountName() != null
										? contraEntriesDetails.getAccountName()
										: "");
								fileWriter.append(COMMA_DELIMITER);
								fileWriter.append(
										contraEntriesDetails.getCurrency() != null ? contraEntriesDetails.getCurrency()
												: "");
								fileWriter.append(COMMA_DELIMITER);
								fileWriter.append(contraEntriesDetails.getExchnageRate() != null
										? contraEntriesDetails.getExchnageRate()
										: "");
								fileWriter.append(COMMA_DELIMITER);
								fileWriter.append(
										contraEntriesDetails.getCredit() != null ? contraEntriesDetails.getCredit()
												: "");
								fileWriter.append(COMMA_DELIMITER);
								fileWriter.append(
										contraEntriesDetails.getDebit() != null ? contraEntriesDetails.getDebit() : "");
							}
							count++;
						}

					}
				}
				fileWriter.append(NEW_LINE_SEPARATOR);
			}

		} catch (Exception e) {
			System.out.println("Error in CsvFileWriter !!!");
			throw new ApplicationException(e);
		} finally {
			try {
				fileWriter.flush();
				fileWriter.close();
			} catch (IOException e) {
				System.out.println("Error while flushing/closing fileWriter !!!");
				throw new ApplicationException(e);
			}

		}

		String exportPath = filePath.toString();
		byte[] fileBytes = null;
		try {
			fileBytes = Files.readAllBytes(new File(exportPath).toPath());
		} catch (IOException e) {
			throw new ApplicationException(e);
		}
		if (fileBytes.length > 0) {
			logger.info("To encode the bytes  ::");
			encodedString = Base64.getEncoder().encodeToString(fileBytes);
			filePath.delete();
		}

		return encodedString;
	}
}

package com.blackstrawai.report;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.json.simple.JSONObject;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.DateConverter;
import com.blackstrawai.onboarding.OrganizationConstants;
import com.blackstrawai.settings.chartofaccounts.ChartOfAccountsReportVo;

public class ReportsHelperService {

	private static Logger logger = Logger.getLogger(ReportsHelperService.class);

	@SuppressWarnings("resource")
	public static String convertJournalEntriesToString(List<JournalEntriesTransactionReportVo> data,String dateFormat)
			throws ApplicationException {
		logger.info("Enty into convertJournalEntriesToString");
		Workbook workbook = new HSSFWorkbook();
		Sheet sheet = workbook.createSheet("JournalEntries_Transaction_Report");
		/** Header Row */
		Row headerRow = sheet.createRow(0);
		// Creating cells		
		//headerRow.createCell(0).setCellValue("id");
		headerRow.createCell(0).setCellValue("transaction_no");
		headerRow.createCell(1).setCellValue("original_transaction_no");
		headerRow.createCell(2).setCellValue("transaction_line_no");
		headerRow.createCell(3).setCellValue("module");
		headerRow.createCell(4).setCellValue("sub_module");
		headerRow.createCell(5).setCellValue("voucher_type");
		headerRow.createCell(6).setCellValue("voucher_no");
		headerRow.createCell(7).setCellValue("date_of_entry");
		headerRow.createCell(8).setCellValue("effective_date");
		headerRow.createCell(9).setCellValue("invoice_date");
		headerRow.createCell(10).setCellValue("particulars");
		headerRow.createCell(11).setCellValue("customer_name");
		headerRow.createCell(12).setCellValue("credit_note_no");
		headerRow.createCell(13).setCellValue("customer_invoice_no");
		headerRow.createCell(14).setCellValue("customer_po_no");
		headerRow.createCell(15).setCellValue("vendor_name");
		headerRow.createCell(16).setCellValue("debit_note_no");
		headerRow.createCell(17).setCellValue("vendor_invoice_no");
		headerRow.createCell(18).setCellValue("vendor_po_no");
		headerRow.createCell(19).setCellValue("banking_account_name");
		headerRow.createCell(20).setCellValue("gst_level");
		headerRow.createCell(21).setCellValue("gst_percentage");
		headerRow.createCell(22).setCellValue("tds_name");
		headerRow.createCell(23).setCellValue("employee_name");
		headerRow.createCell(24).setCellValue("cost_center");
		headerRow.createCell(25).setCellValue("billing_basis");
		headerRow.createCell(26).setCellValue("location");
		headerRow.createCell(27).setCellValue("gst no");
		headerRow.createCell(28).setCellValue("amount_debit_original_currency");
		headerRow.createCell(29).setCellValue("amount_credit_original_currency");
		headerRow.createCell(30).setCellValue("currency");
		headerRow.createCell(31).setCellValue("conversion_factor");
		headerRow.createCell(32).setCellValue("amount_debit");
		headerRow.createCell(33).setCellValue("amount_credit");
		headerRow.createCell(34).setCellValue("gst_input_credit");
		headerRow.createCell(35).setCellValue("nature");
		headerRow.createCell(36).setCellValue("remarks");
		headerRow.createCell(37).setCellValue("user_name");
		headerRow.createCell(38).setCellValue("bank_type");
		headerRow.createCell(39).setCellValue("status");
		DateConverter dateConverter=DateConverter.getInstance();
		int rowNum = 1;
		/** Writing Data */
		for (JournalEntriesTransactionReportVo journalEntriesVo : data) {
			// Create ROW
			Row row = sheet.createRow(rowNum++);
			// Creating cells
			try {
				row.createCell(0).setCellValue(journalEntriesVo.getTransactionNo());
				row.createCell(1).setCellValue(journalEntriesVo.getOriginalTransactionNo());
				row.createCell(2).setCellValue(journalEntriesVo.getTransactionLineNo());
				row.createCell(3).setCellValue(journalEntriesVo.getModule());
				row.createCell(4).setCellValue(journalEntriesVo.getSubModule());
				row.createCell(5).setCellValue(journalEntriesVo.getVoucherType());
				row.createCell(6).setCellValue(journalEntriesVo.getVoucherNo());
				
				Cell cellDoe=row.createCell(7);
				if(journalEntriesVo.getDateOfEntry()!=null) {
				Map<String,Integer> dmyMap=dateConverter.getDayMonthYearFromDate(journalEntriesVo.getDateOfEntry());
				cellDoe.setCellType(Cell.CELL_TYPE_FORMULA);
				cellDoe.setCellFormula("TEXT(DATE("+dmyMap.get("Year")+","+dmyMap.get("Month")+","+dmyMap.get("Day")+"),\""+dateFormat+"\")");
				cellDoe.setCellValue(journalEntriesVo.getDateOfEntry());
				}else {
					cellDoe.setCellValue("");		
				}
				
				Cell cellEffDt=row.createCell(8);
				if(journalEntriesVo.getEffectiveDate()!=null) {
				Map<String,Integer> dmyMap=dateConverter.getDayMonthYearFromDate(journalEntriesVo.getEffectiveDate());
				cellEffDt.setCellType(Cell.CELL_TYPE_FORMULA);
				cellEffDt.setCellFormula("TEXT(DATE("+dmyMap.get("Year")+","+dmyMap.get("Month")+","+dmyMap.get("Day")+"),\""+dateFormat+"\")");
				cellEffDt.setCellValue(journalEntriesVo.getEffectiveDate());
				}else {
					cellEffDt.setCellValue("");		
				}
				
				Cell cellInvoiceDt=row.createCell(9);
				if(journalEntriesVo.getInvoiceDate()!=null) {
				Map<String,Integer> dmyMap=dateConverter.getDayMonthYearFromDate(journalEntriesVo.getInvoiceDate());
				cellInvoiceDt.setCellType(Cell.CELL_TYPE_FORMULA);
				cellInvoiceDt.setCellFormula("TEXT(DATE("+dmyMap.get("Year")+","+dmyMap.get("Month")+","+dmyMap.get("Day")+"),\""+dateFormat+"\")");
				cellInvoiceDt.setCellValue(journalEntriesVo.getInvoiceDate());
				}else {
					cellInvoiceDt.setCellValue("");		
				}				

				row.createCell(10).setCellValue(journalEntriesVo.getParticulars());
				row.createCell(11).setCellValue(journalEntriesVo.getCustomerName());
				row.createCell(12).setCellValue(journalEntriesVo.getCreditNoteNo());
				row.createCell(13).setCellValue(journalEntriesVo.getCustomerInvoiceNo());
				row.createCell(14).setCellValue(journalEntriesVo.getCustomerPoNo());
				row.createCell(15).setCellValue(journalEntriesVo.getVendorName());
				row.createCell(16).setCellValue(journalEntriesVo.getDebitNoteNo());
				row.createCell(17).setCellValue(journalEntriesVo.getVendorInvoiceNo());
				row.createCell(18).setCellValue(journalEntriesVo.getVendorPoNo());
				row.createCell(19).setCellValue(journalEntriesVo.getBankDetails());					
				row.createCell(20).setCellValue(journalEntriesVo.getGstLevel());
				row.createCell(21).setCellValue(journalEntriesVo.getGstPercentage());
				row.createCell(22).setCellValue(journalEntriesVo.getTdsDetails());
				row.createCell(23).setCellValue(journalEntriesVo.getEmployeeName());
				row.createCell(24).setCellValue(journalEntriesVo.getCostCenter());
				row.createCell(25).setCellValue(journalEntriesVo.getBillingBasis());
				row.createCell(26).setCellValue(journalEntriesVo.getLocation());
				row.createCell(27).setCellValue(journalEntriesVo.getGst());
				row.createCell(28).setCellValue(journalEntriesVo.getAmountDebitOriginalCurrrency());
				row.createCell(29).setCellValue(journalEntriesVo.getAmountCreditOriginalCurrency());
				row.createCell(30).setCellValue(journalEntriesVo.getCurrency());
				row.createCell(31).setCellValue(journalEntriesVo.getConversionFactor());
				row.createCell(32).setCellValue(journalEntriesVo.getAmountDebit());
				row.createCell(33).setCellValue(journalEntriesVo.getAmountCredit());
				row.createCell(34).setCellValue(journalEntriesVo.getGstInputCerdit());
				row.createCell(35).setCellValue(journalEntriesVo.getNature());
				row.createCell(36).setCellValue(journalEntriesVo.getRemarks());
				row.createCell(37).setCellValue(journalEntriesVo.getUserName());
				row.createCell(38).setCellValue(journalEntriesVo.getBankType());
				row.createCell(39).setCellValue(journalEntriesVo.getStatus());
			}catch(Exception e) {
				logger.error("Error during while writing data to excel:",e);
				throw new ApplicationException(e);
				
			}
			
		}
		String encodedString = new String();
		try {
			FileReader reader;
			reader = new FileReader("/decifer/config/app_config.properties");
			Properties p = new Properties();
			p.load(reader);
			StringBuilder path = new StringBuilder(p.getProperty("file_store_path"));
			String pathtoSave = path.toString();
			File filePath = new File(pathtoSave + "/journal_entries_"+new SimpleDateFormat("yyyyMMddHHmmsss").format(new Date())+OrganizationConstants.EXCEL_FILE_FORMAT);
			//File filePath = new File("C:/decifer/journal_entries_"+new SimpleDateFormat("yyyyMMddHHmmsss").format(new Date())+OrganizationConstants.EXCEL_FILE_FORMAT);
			FileOutputStream outputStream = new FileOutputStream(filePath);
			workbook.write(outputStream);
			workbook.close();
			String exportPath = filePath.toString();
			byte[] fileBytes = Files.readAllBytes(new File(exportPath).toPath());
			if (fileBytes.length > 0) {
				encodedString = Base64.getEncoder().encodeToString(fileBytes);
				filePath.delete();
			}
		} catch (Exception e) {
			logger.error("Error during while encrypting excel data:",e);
			throw new ApplicationException(e);
		}
		return encodedString;

	}

	@SuppressWarnings("resource")
	public static String convertCoaEntriesToString(List<ChartOfAccountsReportVo> data)
			throws ApplicationException {
		logger.info("Enty into convertCoaEntriesToString");
		Workbook workbook = new HSSFWorkbook();
		Sheet sheet = workbook.createSheet("Chart Of Accounts_Report");
		/** Header Row */
		Row headerRow = sheet.createRow(0);
		// Creating cells		
		headerRow.createCell(0).setCellValue("Account Variant");
		headerRow.createCell(1).setCellValue("Primary Groups");
		headerRow.createCell(2).setCellValue("Account Group");
		headerRow.createCell(3).setCellValue("Sub-Group/User created group");
		headerRow.createCell(4).setCellValue("Ledger Name");
		headerRow.createCell(5).setCellValue("Account Type");
		int rowNum = 1;
		/** Writing Data */
		for (ChartOfAccountsReportVo coaReportVo : data) {
			// Create ROW
			Row row = sheet.createRow(rowNum++);
			// Creating cells
			try {
				row.createCell(0).setCellValue(coaReportVo.getLevel1Name());
				row.createCell(1).setCellValue(coaReportVo.getLevel2Name());
				row.createCell(2).setCellValue(coaReportVo.getLevel3Name());
				row.createCell(3).setCellValue(coaReportVo.getLevel4Name());
				row.createCell(4).setCellValue(coaReportVo.getLevel5Name());
				row.createCell(5).setCellValue(coaReportVo.getEntityName());
			}catch(Exception e) {
				logger.error("Error during while writing data to excel:",e);
				throw new ApplicationException(e);
				
			}
			
		}
		String encodedString = new String();
		try {
			FileReader reader;
			reader = new FileReader("/decifer/config/app_config.properties");
			Properties p = new Properties();
			p.load(reader);
			StringBuilder path = new StringBuilder(p.getProperty("file_store_path"));
			String pathtoSave = path.toString();
			File filePath = new File(pathtoSave + "/coa_report_"+new SimpleDateFormat("yyyyMMddHHmmsss").format(new Date())+OrganizationConstants.EXCEL_FILE_FORMAT);
			//File filePath = new File("C:/decifer/coa_report_"+new SimpleDateFormat("yyyyMMddHHmmsss").format(new Date())+OrganizationConstants.EXCEL_FILE_FORMAT);
			FileOutputStream outputStream = new FileOutputStream(filePath);
			workbook.write(outputStream);
			workbook.close();
			String exportPath = filePath.toString();
			byte[] fileBytes = Files.readAllBytes(new File(exportPath).toPath());
			if (fileBytes.length > 0) {
				encodedString = Base64.getEncoder().encodeToString(fileBytes);
				filePath.delete();
			}
		} catch (Exception e) {
			logger.error("Error during while encrypting excel data:",e);
			throw new ApplicationException(e);
		}
		return encodedString;

	}

	public static String encode(File file) throws Exception {
		String encodedString = null;
		byte[] fileBytes = Files.readAllBytes(file.toPath());
		if (fileBytes.length > 0) {
			encodedString = Base64.getEncoder().encodeToString(fileBytes);
			file.delete();
		}
		return encodedString;
	}

	public static String getGstReportFileName(String gstType, String gstSearchType) throws ApplicationException {
		if (isGstValid(gstType, gstSearchType))
			return gstType + "_" + gstSearchType + ".csv";
		throw new ApplicationException("GST Type/GST Search Type is null or empty");
	}

	private static boolean isGstValid(String gstType, String gstSearchType) {
		return gstType != null && gstSearchType != null && gstType.length() > 0 && gstSearchType.length() > 0;
	}
	
	public static String loadProperty(String property) throws IOException {
		FileReader reader;
		reader = new FileReader("/decifer/config/app_config.properties");
		Properties p = new Properties();
		p.load(reader);
		return p.getProperty(property);
	}

	public static boolean isValidResponse(JSONObject response) {
		if (response.get("status") != null) {
			Integer status = (Integer)response.get("status");
			if (status > 201) {
				return false;
			}
		}
		return true;
	}

}

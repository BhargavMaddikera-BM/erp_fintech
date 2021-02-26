package com.blackstrawai.export;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.common.BaseService;

@Service
public class ExportService extends BaseService {

	@Autowired
	ExportHelperService exportHelperService;

	private Logger logger = Logger.getLogger(ExportService.class);

	public String exportEmployee(List<EmployeeExportVo> data, String moduleName, String fileType)
			throws ApplicationException {
		logger.info("Entry into method: exportEmployee");
		String encodedString = null;
		if (fileType.equalsIgnoreCase("xls")) {
			encodedString = exportHelperService.exportEmployeeXlsx(data, moduleName, fileType);
		}
		if (fileType.equalsIgnoreCase("csv")) {
			encodedString = exportHelperService.exportEmployeeCsv(data, moduleName, fileType);
		}
		return encodedString;
	}

	public String exportVendor(List<VendorExportVo> data, String moduleName, String fileType) throws ApplicationException {
		logger.info("Entry into method: exportVendor");
		String encodedString = null;
		if (fileType.equalsIgnoreCase("xls")) {
			encodedString = exportHelperService.exportVendorXlsx(data, moduleName, fileType);
		}
		if (fileType.equalsIgnoreCase("csv")) {
			encodedString = exportHelperService.exportVendorCsv(data, moduleName, fileType);
		}
		return encodedString;
	}

	public String exportCustomer(List<CustomerExportVo> data, String moduleName, String fileType) throws ApplicationException {
		logger.info("Entry into method: exportCustomer");
		String encodedString = null;
		if (fileType.equalsIgnoreCase("xls")) {
			encodedString = exportHelperService.exportCustomerXlsx(data, moduleName, fileType);
		}
		if (fileType.equalsIgnoreCase("csv")) {
			encodedString = exportHelperService.exportCustomerCsv(data, moduleName, fileType);
		}
		return encodedString;
	}

	public String exportAccountingEntry(List<AccountingEntryExportVo> data, String moduleName, String fileType) throws ApplicationException {
		logger.info("Entry into method: exportAccountingEntry");
		String encodedString = null;
		if (fileType.equalsIgnoreCase("xls")) {
			encodedString = exportHelperService.exportAccountingEntryXlsx(data, moduleName, fileType);
		}
		if (fileType.equalsIgnoreCase("csv")) {
			encodedString = exportHelperService.exportAccountingEntryCsv(data, moduleName, fileType);
		}
		return encodedString;
	}

	public String exportContraEntry(List<ContraExportVo> data, String moduleName, String fileType) throws ApplicationException {
		logger.info("Entry into method: exportContraEntry");
		String encodedString = null;
		if (fileType.equalsIgnoreCase("xls")) {
			encodedString = exportHelperService.exportContraXlsx(data, moduleName, fileType);
		}
		if (fileType.equalsIgnoreCase("csv")) {
			encodedString = exportHelperService.exportContraCsv(data, moduleName, fileType);
		}
		return encodedString;
	}

}

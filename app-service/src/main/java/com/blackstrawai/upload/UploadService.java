package com.blackstrawai.upload;

import java.io.FileNotFoundException;

import org.apache.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.ApplicationRuntimeException;
import com.blackstrawai.common.BaseService;
import com.blackstrawai.common.BaseVo;
import com.blackstrawai.common.DropDownDao;
import com.blackstrawai.upload.dropdowns.ModuleTypeDropDownVo;

@Service
public class UploadService extends BaseService {

	@Autowired
	DropDownDao dropDownDao;

	@Autowired
	UploadHelperService uploadHelperService;

	private Logger logger = Logger.getLogger(UploadService.class);

	public ModuleTypeDropDownVo getUploadDropDownData() throws ApplicationException {
		logger.info("Entry into getUploadDropDownData");
		return dropDownDao.getUploadDropDownData();
	}

	public String getTemplateFile(String moduleName, String type)
			throws ApplicationRuntimeException, FileNotFoundException {
		logger.info("Entry into getTemplateFile");
		String str = uploadHelperService.getTemplateFile(moduleName, type);
		return str;
	}

	public BaseVo previewImportFile(UploadFileVo importFileVo)
			throws ApplicationRuntimeException, ParseException, InvalidFormatException, java.text.ParseException, ApplicationException {
		logger.info("Entry into createImportFile");
		BaseVo data = uploadHelperService.previewImportFile(importFileVo);
		return data;
	}

}

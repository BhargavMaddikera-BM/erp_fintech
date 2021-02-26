package com.blackstrawai.helper;

import java.util.ArrayList;
import java.util.List;

import com.blackstrawai.export.ExportVo;
import com.blackstrawai.request.export.ExportRequest;

public class ExportConvertToVoHelper {

	private static ExportConvertToVoHelper exportConvertToVoHelper;

	public static ExportConvertToVoHelper getInstance() {
		if (exportConvertToVoHelper == null) {
			exportConvertToVoHelper = new ExportConvertToVoHelper();
		}
		return exportConvertToVoHelper;
	}

	public ExportVo convertExportVoFromExportRequest(ExportRequest exportRequest) {
		ExportVo exportVo = new ExportVo();
		exportVo.setOrganizationId(exportRequest.getOrganizationId());
		List<Integer> Ids = new ArrayList<Integer>();
		exportVo.setFileType(exportRequest.getFileType());
		exportVo.setModuleName(exportRequest.getModuleName());
		for (Integer id : exportRequest.getListOfId()) {
			Ids.add(id);
		}
		exportVo.setListOfId(Ids);
		return exportVo;
	}

}

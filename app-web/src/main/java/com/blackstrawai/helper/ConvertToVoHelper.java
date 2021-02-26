package com.blackstrawai.helper;

import com.blackstrawai.attachments.UploadFileVo;
import com.blackstrawai.request.attachments.UploadFileRequest;

public class ConvertToVoHelper {

	private static ConvertToVoHelper convertToVoHelper;

	private ConvertToVoHelper() {

	}

	public static ConvertToVoHelper getInstance() {
		if (convertToVoHelper == null) {
			convertToVoHelper = new ConvertToVoHelper();
		}
		return convertToVoHelper;
	}

	public UploadFileVo convertAttachmentFromReq(UploadFileRequest uploadfileRequest) {
		UploadFileVo uploadVO = new UploadFileVo();
		uploadVO.setName(uploadfileRequest.getName());
		uploadVO.setData(uploadfileRequest.getData());
		uploadVO.setSize(uploadfileRequest.getSize());
		uploadVO.setIsAkshar(uploadfileRequest.getIsAkshar());
		uploadVO.setDocumentType(uploadfileRequest.getDocumentType());
		return uploadVO;

	}
	
	public com.blackstrawai.upload.UploadFileVo convertAttachmentFromReqImport(UploadFileRequest uploadfileRequest) {
		com.blackstrawai.upload.UploadFileVo uploadVO = new com.blackstrawai.upload.UploadFileVo();
		uploadVO.setData(uploadfileRequest.getData());
		uploadVO.setDuplicacy(uploadfileRequest.isDuplicacy());
		uploadVO.setFileType(uploadfileRequest.getFileType());
		uploadVO.setModuleName(uploadfileRequest.getModuleName());
		return uploadVO;

	}

}

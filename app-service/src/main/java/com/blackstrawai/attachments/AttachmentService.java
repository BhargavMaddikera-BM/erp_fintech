package com.blackstrawai.attachments;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.blackstrawai.ApplicationRuntimeException;
import com.blackstrawai.common.BaseService;

@Service
public class AttachmentService extends BaseService {

	private Logger logger = Logger.getLogger(AttachmentService.class);

	public void upload(String type, Integer typeId, Integer organizationId, List<UploadFileVo> attachments) {
		FileReader reader;
		try {

			reader = new FileReader("/decifer/config/app_config.properties");
			Properties p = new Properties();
			p.load(reader);
			StringBuilder path = new StringBuilder(p.getProperty("attachment_path"));
			if (type != null && typeId != null && organizationId != null) {
				logger.info(path.toString() + " is path retrieved from properties");
				path = path.append("/").append(organizationId).append("/").append(type);
			   			
				File f = new File(path.toString());
				logger.info(path.toString() + " to navigate ");
				
				if(type.equalsIgnoreCase(AttachmentsConstants.MODULE_TYPE_AR_INVOICE_TEMPLATE_LOGO)
						|| type.equalsIgnoreCase(AttachmentsConstants.MODULE_TYPE_AR_INVOICE_TEMPLATE_SIGN) 
						|| type.equalsIgnoreCase(AttachmentsConstants.MODULE_TYPE_AR_CREDIT_NOTE_TEMPLATE_LOGO) 
						|| type.equalsIgnoreCase(AttachmentsConstants.MODULE_TYPE_AR_CREDIT_NOTE_TEMPLATE_SIGN)
						|| type.equalsIgnoreCase(AttachmentsConstants.MODULE_TYPE_PAYROLL_PAYRUN)) {
					
					if(!f.exists() && !f.isDirectory() ) {
	                    f.mkdir();
						logger.info("missing folder is created "+f.toString());
					}
				}
				
				
				if (f.exists() && f.isDirectory() && typeId != 0) {
					logger.info("To create folder for type " + type);
					String pathtoSave = path.append("/").append(typeId).toString();
					File fpath = new File(pathtoSave);
					fpath.mkdir();
					logger.info("Created Path ::" + pathtoSave + "/");
					if (attachments != null && attachments.size() > 0) {
						attachments.forEach(uploadFile -> {
							decodeAndUploadFile(uploadFile, pathtoSave + "/");
						});
					}
					logger.info("File Uploaded Successfully for " + type);
				} else {
					
					logger.info(type + " folder not exist for Given organization");
				}
			}
		} catch (FileNotFoundException e) {
			logger.info("File not found ", e);
			throw new ApplicationRuntimeException(e);
		} catch (IOException e) {
			logger.info("IO Exception ", e);
			throw new ApplicationRuntimeException(e);
		}
	}
	
	public void uploadProfile(String fileName, UploadFileVo attachment) {
		try {

			String profilePath = "/decifer/attachments/profile/";
			//String profilePath = "C:\\decifer\\config\\";
			if (fileName != null) {
				File f = new File(profilePath);
				
					if(!f.exists() && !f.isDirectory() ) {
	                    f.mkdir();
						logger.info("missing folder is created "+f.toString());
					}
				
				if (f.exists() && f.isDirectory() ) {
							decodeAndUploadFile(attachment, profilePath);
					}
			}
		} catch (Exception e) {
			logger.error("Error in uploadProfile ", e);
			throw new ApplicationRuntimeException(e);
		}
	}


	@Async
	public void decodeAndUploadFile(UploadFileVo uploadFileVo, String path) {
		logger.info("Async call at decode and upload");
		logger.info("To upload file with file name:: " + uploadFileVo.getName() + " in Path:: " + path);
		try {
			if (uploadFileVo.getName() != null && uploadFileVo.getData() != null) {
				File toUploadFile = new File(path + uploadFileVo.getName());
				byte[] imageByte = Base64.getDecoder().decode(uploadFileVo.getData().getBytes());
				BufferedOutputStream scanStream;
				scanStream = new BufferedOutputStream(new FileOutputStream(toUploadFile));
				scanStream.write(imageByte);
				scanStream.close();
			}
		} catch (FileNotFoundException e) {
			logger.info("File not found during decode and upload ", e);
			throw new ApplicationRuntimeException(e);
		} catch (IOException e) {
			logger.info("IO Exception during decode and upload ", e);
			throw new ApplicationRuntimeException(e);
		}

	}

	public void encodeAllFiles(Integer orgId, Integer typeId, String type, List<UploadFileVo> fileVoList) {
		logger.info("To encode files for module ::  " + type + "with id:: " + typeId + "with list size"
				+ fileVoList.size());
		if (fileVoList.size() > 0) {
			fileVoList.forEach(vo -> {
				encodeUploadedFile(orgId, typeId, type, vo);
			});
		}
	}
	


	@Async
	public void encodeUploadedFile(Integer orgId, Integer moduleTypeId, String moduleType, UploadFileVo fileVo) {
		logger.info("Async call at encode ");
		logger.info("To encode file with name " + fileVo.getName());
		FileReader reader;
		try {
			reader = new FileReader("/decifer/config/app_config.properties");
			Properties p = new Properties();
			p.load(reader);
			StringBuilder path = new StringBuilder(p.getProperty("attachment_path"));
			logger.info(path.toString() + "is path retrieved from properties");
			path.append("/").append(orgId).append("/").append(moduleType).append("/").append(moduleTypeId);
			File f = new File(path.toString());
			logger.info(path.toString() + " to navigate ");
			if (f.exists() && f.isDirectory() && moduleTypeId != 0) {
				String uploadedpath = path.toString() + "/" + fileVo.getName();
				logger.info(uploadedpath + " is path to get File bites ");
				byte[] fileBytes = Files.readAllBytes(new File(uploadedpath).toPath());
				// logger.info("Converted to bytes ::"+fileBytes);
				if (fileBytes.length > 0) {
					logger.info("To encode the bytes  ::");
					String encodedString = Base64.getEncoder().encodeToString(fileBytes);
					fileVo.setData(encodedString);
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

	}

	@Async
	public void encodeUploadedProfile(UploadFileVo fileVo) {
		logger.info("To encodeUploadedProfile " + fileVo.getName());
		try {
			String profilePath = "/decifer/attachments/profile/";
			//String profilePath = "C:\\decifer\\config\\";
			File f = new File(profilePath);
			if (f.exists() && f.isDirectory() && fileVo.getName() != null) {
				String uploadedpath = profilePath.toString() +  fileVo.getName();
				logger.info(uploadedpath + " is path to get File bites ");
				byte[] fileBytes = Files.readAllBytes(new File(uploadedpath).toPath());
				// logger.info("Converted to bytes ::"+fileBytes);
				if (fileBytes.length > 0) {
					logger.info("To encode the bytes  ::");
					String encodedString = Base64.getEncoder().encodeToString(fileBytes);
					fileVo.setData(encodedString);
				}
			}
			logger.info("Uploaded files are retieved and encoded successfully");
		} catch (FileNotFoundException e) {
			logger.error("File not found during decode and upload ", e);
			throw new ApplicationRuntimeException(e);
		} catch (IOException e) {
			logger.error("IO Exception during decode and upload ", e);
			throw new ApplicationRuntimeException(e);
		}

	}

	
		
}

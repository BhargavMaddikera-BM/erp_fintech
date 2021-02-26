package com.blackstrawai.employeeProvidentFunds;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.blackstrawai.attachments.AttachmentsConstants;
import com.blackstrawai.externalintegration.compliance.employeeprovidentfund.EmployeeProvidentFundAttachmentVo;

public class EmployeeProvidentFundFilePolling implements Runnable {
	private static File dir;
	private static boolean flag = false;
	private static List<List<EmployeeProvidentFundAttachmentVo>> attachments = new ArrayList<List<EmployeeProvidentFundAttachmentVo>>();

	public EmployeeProvidentFundFilePolling(File dir) {
		EmployeeProvidentFundFilePolling.dir = dir;
		flag = true;
	}

	/**
	 * Polls the download directory of EPF to create attachment objects
	 */
	@Override
	public void run() {
		File[] newlyDownloadedFiles = new File[2];
		while (flag) {
			try {
				synchronized (dir) {
					newlyDownloadedFiles = dir.listFiles((dir1, name) -> !name.endsWith(".bkp") && !name.endsWith(".crdownload"));
					if (allFilesDownloaded(newlyDownloadedFiles)) {
						Thread.sleep(500);
						if (!checkStartOfElementAtIndex(newlyDownloadedFiles, 1, "GRVSP")) {
							Collections.reverse(Arrays.asList(newlyDownloadedFiles));
						}
						List<EmployeeProvidentFundAttachmentVo> uFiles = new ArrayList<EmployeeProvidentFundAttachmentVo>();
						for (File file : newlyDownloadedFiles) {
							EmployeeProvidentFundAttachmentVo uFile = new EmployeeProvidentFundAttachmentVo();
							uFile.setName(file.getName());
							uFile.setSize(String.valueOf(file.length()));
							uFile.setEpfType(AttachmentsConstants.MODULE_EMPLOYEE_PROVIDENT_FUND_ECR);
							EmployeeProvidentFunds.setAttachmentData(uFile, file);
							uFiles.add(uFile);
							File temp = new File(dir + "/" + file.getName() + ".bkp");
							file.renameTo(temp);
						}
						attachments.add(uFiles);
						dir.notifyAll();
					}
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}

	}

	public static List<List<EmployeeProvidentFundAttachmentVo>> getAttachments() {
		return attachments;
	}

	public static void setFlag(boolean flag) {
		EmployeeProvidentFundFilePolling.flag = flag;
	}

	private boolean allFilesDownloaded(File[] newlyDownloadedFiles) {
		return newlyDownloadedFiles != null && newlyDownloadedFiles.length == 2;
	}

	public boolean checkStartOfElementAtIndex(File[] files, int index, String startsWith) {
		return files[index].getName().startsWith(startsWith);
	}
}

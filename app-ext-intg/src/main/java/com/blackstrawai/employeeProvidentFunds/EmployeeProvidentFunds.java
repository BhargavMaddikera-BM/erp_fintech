package com.blackstrawai.employeeProvidentFunds;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.comparator.LastModifiedFileComparator;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.ApplicationRuntimeException;
import com.blackstrawai.attachments.AttachmentsConstants;
import com.blackstrawai.externalintegration.compliance.employeeprovidentfund.EmployeeProvidentFundAttachmentVo;
import com.blackstrawai.externalintegration.compliance.employeeprovidentfund.EmployeeProvidentFundChallanVo;
import com.blackstrawai.externalintegration.compliance.employeeprovidentfund.EmployeeProvidentFundEcrVo;

public class EmployeeProvidentFunds {
	private static EmployeeProvidentFunds employeePf;
	private Logger logger = Logger.getLogger(EmployeeProvidentFunds.class);
	private static String attachmentsPath = null;

	/**
	 * Reads the attachment path from properties file
	 * 
	 * @return path of attachment folder
	 * @throws IOException
	 */
	public static String getAttachmentsPath() throws IOException {
		FileReader reader = new FileReader("/decifer/config/app_config.properties");
		Properties p = new Properties();
		p.load(reader);
		return p.getProperty("attachment_path");
	}

	/**
	 * Sets attachmentPath
	 * 
	 * @param attachmentsPath
	 */
	public static void setAttachmentsPath(String attachmentsPath) {
		EmployeeProvidentFunds.attachmentsPath = attachmentsPath;
	}

	/**
	 * Creates a static instance of EmployeeProvidentFunds, Sets the attachment path
	 * 
	 * @return EmployeeProvidentFunds object
	 * @throws IOException
	 */
	public static EmployeeProvidentFunds getInstance() throws IOException {

		if (employeePf == null) {
			employeePf = new EmployeeProvidentFunds();
			setAttachmentsPath(getAttachmentsPath());
		}

		return employeePf;

	}

	/**
	 * Initializes the ChromeDriver depending on OS
	 * 
	 * @param osName           - Windows/Linux/macOS
	 * @param downloadFilePath - default download location of chromeDriver
	 * @return ChromeDriver instance
	 */
	private ChromeDriver getChromeDriver(String osName, String downloadFilePath) {
		if (osName != null && osName.startsWith("Windows")) {
			return getWindowsDriver(downloadFilePath);
		} else {
			return getLinuxOrMacDriver(downloadFilePath);
		}
	}

	/**
	 * Initializes ChromeDriver in Windows environment
	 * 
	 * @param downloadFilePath
	 * @return ChromeDriver instance
	 */
	private ChromeDriver getWindowsDriver(String downloadFilePath) {
		System.setProperty("webdriver.chrome.driver", "/chromedriver/chromedriver.exe");
		HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
		chromePrefs.put("profile.default_content_settings.popups", 0);
		chromePrefs.put("download.prompt_for_download", "false");
		chromePrefs.put("download.default_directory", downloadFilePath);
		ChromeOptions chromeOptions = new ChromeOptions();
		chromeOptions.setExperimentalOption("prefs", chromePrefs);
		return new ChromeDriver(chromeOptions);
	}

	/**
	 * Initializes ChromeDriver in Linux/macOS environment
	 * 
	 * @param downloadFilePath
	 * @return ChromeDriver instance
	 */
	private ChromeDriver getLinuxOrMacDriver(String downloadFilePath) {
		System.setProperty("webdriver.chrome.driver", "/chromedriver/chromedriver");
		HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
		chromePrefs.put("profile.default_content_settings.popups", 0);
		chromePrefs.put("download.prompt_for_download", "false");
		chromePrefs.put("download.default_directory", downloadFilePath);
		ChromeOptions chromeOptions = new ChromeOptions();
		chromeOptions.addArguments("--no-sandbox");
		chromeOptions.addArguments("--headless");
		chromeOptions.addArguments("disable-gpu");
		chromeOptions.addArguments("window-size=1400,2100"); // Linux should be activate
		chromeOptions.setExperimentalOption("prefs", chromePrefs);
		chromeOptions.addArguments("disable-infobars");
		return new ChromeDriver(chromeOptions);
	}

	/**
	 * Creates a directory for downloads, or deletes all files if exists
	 * 
	 * @param path
	 * @return file directory
	 * @throws IOException
	 */
	private File createAndCleanDirectory(String path) throws IOException {
		File dir = new File(path);
		if (dir.exists() && dir.isDirectory()) {
			FileUtils.cleanDirectory(dir);
		} else {
			FileUtils.forceMkdir(dir);
		}
		return dir;
	}

	/**
	 * Opens EPF web site and logs in
	 * 
	 * @param driver
	 * @param username
	 * @param password
	 * @throws InterruptedException
	 */
	private void loginToEpfPortal(ChromeDriver driver, String username, String password) throws InterruptedException {
		driver.get("https://unifiedportal-emp.epfindia.gov.in/epfo/");
		// Login
		driver.findElement(By.id("username")).sendKeys(username);
		driver.findElement(By.id("password")).sendKeys(password);
		driver.findElement(By.xpath("//*[@id=\"AuthenticationForm\"]/div[4]/div[1]/button")).click();
		Thread.sleep(1000);
	}

	/**
	 * Scrapes the web to get recent challans, Called during refresh and if records
	 * not available in DB
	 * 
	 * @param userName
	 * @param password
	 * @param orgId
	 * @return
	 * @throws ApplicationException
	 */
	public List<EmployeeProvidentFundChallanVo> getChallans(String userName, String password, int orgId)
			throws ApplicationException {
		List<EmployeeProvidentFundChallanVo> challans = new ArrayList<EmployeeProvidentFundChallanVo>();
		ChromeDriver driver = null;
		try {
			String downloadFilePath = constructDownloadPath(orgId,
					AttachmentsConstants.MODULE_EMPLOYEE_PROVIDENT_FUND_CHALLAN, userName, attachmentsPath);
			String osName = System.getProperty("os.name");
			driver = getChromeDriver(osName, downloadFilePath);
			File dir = createAndCleanDirectory(downloadFilePath);
			loginToEpfPortal(driver, userName, password);
			navigateToTable(driver, "//*[@id=\"menu\"]/li[4]/ul/li[2]/a");
			int pages = fetchNumberOfPages(driver, "//*[@id=\"ui-id-4\"]/div/span[1]");
			fetchChallansFromTable(driver, pages, challans);
			setAttachmentsForChallans(challans, dir);
		} catch (Exception e) {
			return challans;
		} finally {
			if (driver != null) {
				// driver.close();
				driver.quit();
			}
		}

		return challans;

	}

	/**
	 * Sets attachments for each challan depending on trrn
	 * 
	 * @param challans
	 * @param dir
	 */
	private void setAttachmentsForChallans(List<EmployeeProvidentFundChallanVo> challans, File dir) {
		for (EmployeeProvidentFundChallanVo challan : challans) {
			File[] files = dir.listFiles((dir1, name) -> name.matches(".*" + challan.getTrrn() + ".*"));
			if (!files[1].getName().startsWith("PaymentReceipt")) {
				Collections.reverse(Arrays.asList(files));
			}
			List<EmployeeProvidentFundAttachmentVo> uFiles = new ArrayList<EmployeeProvidentFundAttachmentVo>();
			for (File file : files) {
				EmployeeProvidentFundAttachmentVo uFile = new EmployeeProvidentFundAttachmentVo();
				uFile.setName(file.getName().replace(".crdownload", ""));
				uFile.setSize(String.valueOf(file.length()));
				uFile.setEpfType(AttachmentsConstants.MODULE_EMPLOYEE_PROVIDENT_FUND_CHALLAN);
				setAttachmentData(uFile, file);
				uFiles.add(uFile);
			}
			challan.setAttachments(uFiles);
		}

	}

	/**
	 * fetch all challans from recent challans table
	 * 
	 * @param driver
	 * @param pages
	 * @param challans
	 * @throws InterruptedException
	 */
	private void fetchChallansFromTable(ChromeDriver driver, int pages, List<EmployeeProvidentFundChallanVo> challans)
			throws InterruptedException {

		int page = 1;
		do {
			if (page == 2) {
				driver.findElement(By.xpath("//*[@id=\"ui-id-4\"]/div/span[2]/a[1]")).click();
				Thread.sleep(500);
			} else if (page > 2) {
				driver.findElement(By.xpath("//*[@id=\"ui-id-4\"]/div/span[2]/a[" + String.valueOf(page + 1) + "]"))
						.click();
				Thread.sleep(500);
			}

			List<WebElement> rows = driver.findElements(By.xpath("//*[@id=\"tbRecentECRChallanList\"]/tbody/tr[*]"));
			Integer records = rows.size();
			for (int i = 1; i <= records; i++) {
				EmployeeProvidentFundChallanVo challan = new EmployeeProvidentFundChallanVo();
				challan.setTrrn(
						driver.findElement(By.xpath("//*[@id=\"tbRecentECRChallanList\"]/tbody/tr[" + i + "]/td[1]"))
								.getText());
				challan.setWageMonth(
						driver.findElement(By.xpath("//*[@id=\"tbRecentECRChallanList\"]/tbody/tr[" + i + "]/td[2]"))
								.getText());
				challan.setEcrType(
						driver.findElement(By.xpath("//*[@id=\"tbRecentECRChallanList\"]/tbody/tr[" + i + "]/td[3]"))
								.getText());
				challan.setUploadDate(
						driver.findElement(By.xpath("//*[@id=\"tbRecentECRChallanList\"]/tbody/tr[" + i + "]/td[4]"))
								.getText());
				challan.setStatus(
						driver.findElement(By.xpath("//*[@id=\"tbRecentECRChallanList\"]/tbody/tr[" + i + "]/td[5]"))
								.getAttribute("title"));
				challan.setAc1(
						driver.findElement(By.xpath("//*[@id=\"tbRecentECRChallanList\"]/tbody/tr[" + i + "]/td[6]"))
								.getText());
				challan.setAc2(
						driver.findElement(By.xpath("//*[@id=\"tbRecentECRChallanList\"]/tbody/tr[" + i + "]/td[7]"))
								.getText());
				challan.setAc10(
						driver.findElement(By.xpath("//*[@id=\"tbRecentECRChallanList\"]/tbody/tr[" + i + "]/td[8]"))
								.getText());
				challan.setAc21(
						driver.findElement(By.xpath("//*[@id=\"tbRecentECRChallanList\"]/tbody/tr[" + i + "]/td[9]"))
								.getText());
				challan.setAc22(
						driver.findElement(By.xpath("//*[@id=\"tbRecentECRChallanList\"]/tbody/tr[" + i + "]/td[10]"))
								.getText());
				challan.setTotalAmount(
						driver.findElement(By.xpath("//*[@id=\"tbRecentECRChallanList\"]/tbody/tr[" + i + "]/td[11]"))
								.getText());
				challan.setCrn(
						driver.findElement(By.xpath("//*[@id=\"tbRecentECRChallanList\"]/tbody/tr[" + i + "]/td[12]"))
								.getText());
				driver.findElement(By.xpath("//*[@id=\"tbRecentECRChallanList\"]/tbody/tr[" + i + "]/td[13]/a"))
						.click();
				driver.findElement(By.xpath("//*[@id=\"tbRecentECRChallanList\"]/tbody/tr[" + i + "]/td[14]/a"))
						.click();
				challans.add(challan);
			}
			page++;
		} while (page <= pages);
		// Wait till last download is done
		Thread.sleep(2000);
	}

	/**
	 * fetches number of pages present in the table, each containing 20 records
	 * 
	 * @param driver
	 * @param xPathToTotalRecordsInfo
	 * @return number of pages
	 */
	private int fetchNumberOfPages(ChromeDriver driver, String xPathToTotalRecordsInfo) {
		String totalString = driver.findElement(By.xpath(xPathToTotalRecordsInfo)).getText().split(" ")[0];
		Integer total = null;
		if (totalString.equalsIgnoreCase("No"))
			return 0;
		else
			total = Integer.parseInt(totalString);
		logger.info(total + " records found");
		return (int) Math.ceil(Double.valueOf(total) / 20.0);
	}

	/**
	 * Navigates to recent challans table
	 * 
	 * @param driver
	 * @param subMenu
	 * @throws InterruptedException
	 */
	private void navigateToTable(ChromeDriver driver, String subMenu) throws InterruptedException {
		// Click Payments in menu
		driver.findElement(By.xpath("//*[@id=\"menu\"]/li[4]/a")).click();
		Thread.sleep(200);
		// Click Payment (ECR) in sub-menu
		driver.findElement(By.xpath(subMenu)).click();
		Thread.sleep(1000);
	}

	/**
	 * Scrapes the web to get recent ECRs Called during refresh and if ECRs not
	 * available in DB
	 * 
	 * @param userName
	 * @param password
	 * @param orgId
	 * @return
	 * @throws ApplicationException
	 */
	public List<EmployeeProvidentFundEcrVo> getECRs(String userName, String password, int orgId)
			throws ApplicationException {
		ChromeDriver driver = null;
		List<EmployeeProvidentFundEcrVo> ecrs = new ArrayList<EmployeeProvidentFundEcrVo>();
		try {
			String downloadFilePath = constructDownloadPath(orgId,
					AttachmentsConstants.MODULE_EMPLOYEE_PROVIDENT_FUND_ECR, userName, attachmentsPath);
			String osName = System.getProperty("os.name");
			driver = getChromeDriver(osName, downloadFilePath);
			File dir = createAndCleanDirectory(downloadFilePath);
			loginToEpfPortal(driver, userName, password);
			navigateToTable(driver, "//*[@id=\"menu\"]/li[4]/ul/li[1]/a",
					"//*[@id=\"dataTable\"]/tbody/tr[1]/td[1]/b/a");
			int pages = fetchNumberOfPages(driver, "//*[@id=\"ui-id-10\"]/div/span[1]");
//			startPollingThread(new EmployeeProvidentFundFilePolling(dir));
			fetchEcrFromTable(driver, pages, ecrs, dir);
			EmployeeProvidentFundFilePolling.setFlag(false);
			setAttachmentsForEcr(ecrs, dir);
//			resetFilesInPollingDirectory(dir);
		} catch (Exception e) {
			return ecrs;
		} finally {
			if (driver != null) {
				// driver.close();
				driver.quit();

			}
		}

		return ecrs;

	}

	/**
	 * Sets attachments for each ECR, depending on last modified time
	 * 
	 * @param ecrs
	 * @param dir
	 */
	private void setAttachmentsForEcr(List<EmployeeProvidentFundEcrVo> ecrs, File dir) {
		File[] files = dir.listFiles();
		int i = 0;
		if (files.length > 0) {
			Arrays.sort(files, LastModifiedFileComparator.LASTMODIFIED_COMPARATOR);

			for (EmployeeProvidentFundEcrVo ecr : ecrs) {
				List<EmployeeProvidentFundAttachmentVo> attachments = new ArrayList<EmployeeProvidentFundAttachmentVo>();
				for (int j = 0; j < 2; j++) {
					EmployeeProvidentFundAttachmentVo attachment = new EmployeeProvidentFundAttachmentVo();
					attachment.setName(files[i].getName().replace(".crdownload", ""));
					attachment.setSize(String.valueOf(files[i].length()));
					attachment.setEpfType(AttachmentsConstants.MODULE_EMPLOYEE_PROVIDENT_FUND_ECR);
					setAttachmentData(attachment, files[i]);
					attachments.add(attachment);
					i++;
				}

				ecr.setAttachments(attachments);
			}
		}
	}

	/**
	 * Fetches ECRs from recent ECR table
	 * 
	 * @param driver
	 * @param pages
	 * @param ecrs
	 * @param dir
	 * @throws InterruptedException
	 */
	private void fetchEcrFromTable(ChromeDriver driver, int pages, List<EmployeeProvidentFundEcrVo> ecrs, File dir)
			throws InterruptedException {
		int page = 1;
		do {
			if (page == 2) {
				driver.findElement(By.xpath("//*[@id=\"ui-id-10\"]/div/span[2]/a[1]")).click();
				Thread.sleep(500);
			} else if (page > 2) {
				driver.findElement(By.xpath("//*[@id=\"ui-id-10\"]/div/span[2]/a[" + String.valueOf(page + 1) + "]"))
						.click();
				Thread.sleep(500);
			}

			List<WebElement> rows = driver.findElements(By.xpath("//*[@id=\"tbRecentClaimList\"]/tbody/tr[*]"));
			Integer records = rows.size();
			for (int i = 1; i <= records; i++) {
				EmployeeProvidentFundEcrVo ecr = new EmployeeProvidentFundEcrVo();
				ecr.setTrrn(driver.findElement(By.xpath("//*[@id=\"tbRecentClaimList\"]/tbody/tr[" + i + "]/td[2]"))
						.getText());
				ecr.setWageMonth(driver
						.findElement(By.xpath("//*[@id=\"tbRecentClaimList\"]/tbody/tr[" + i + "]/td[3]")).getText());
				ecr.setEcrType(driver.findElement(By.xpath("//*[@id=\"tbRecentClaimList\"]/tbody/tr[" + i + "]/td[4]"))
						.getText());
				ecr.setSalaryDisbDate(driver
						.findElement(By.xpath("//*[@id=\"tbRecentClaimList\"]/tbody/tr[" + i + "]/td[5]")).getText());
				ecr.setContrRate(driver
						.findElement(By.xpath("//*[@id=\"tbRecentClaimList\"]/tbody/tr[" + i + "]/td[6]")).getText());
				ecr.setUploadDate(driver
						.findElement(By.xpath("//*[@id=\"tbRecentClaimList\"]/tbody/tr[" + i + "]/td[7]")).getText());
				ecr.setStatus(driver.findElement(By.xpath("//*[@id=\"tbRecentClaimList\"]/tbody/tr[" + i + "]/td[8]"))
						.getText());
/*
				synchronized (dir) {
					while (dir.listFiles(
							(dir1, name) -> !name.endsWith(".bkp") && !name.endsWith(".crdownload")).length != 0) {
						dir.wait(10000);
					}
*/
					driver.findElement(By.xpath("//*[@id=\"tbRecentClaimList\"]/tbody/tr[" + i + "]/td[9]/a")).click();
					driver.findElement(By.xpath("//*[@id=\"tbRecentClaimList\"]/tbody/tr[" + i + "]/td[10]/a")).click();
					Thread.sleep(5000);
//				}
				

				ecrs.add(ecr);
			}

			page++;
		} while (page <= pages);
		// Wait till files are downloaded
		Thread.sleep(2000);
	}

	private void navigateToTable(ChromeDriver driver, String subMenu, String ecrUpload) throws InterruptedException {
		// Click Payments in menu
		driver.findElement(By.xpath("//*[@id=\"menu\"]/li[4]/a")).click();
		Thread.sleep(200);
		// Click ECR?Return Filing in sub-menu
		driver.findElement(By.xpath(subMenu)).click();
		Thread.sleep(1000);
		// Click ECR Upload
		driver.findElement(By.xpath(ecrUpload)).click();
		Thread.sleep(1000);
	}

	/**
	 * Sets data field of given attachment using given file
	 * 
	 * @param attachment
	 * @param file
	 */
	public static void setAttachmentData(EmployeeProvidentFundAttachmentVo attachment, File file) {
		try {
			attachment.setData(encode(file));
		} catch (FileNotFoundException e) {
			throw new ApplicationRuntimeException(e);
		} catch (IOException e) {
			throw new ApplicationRuntimeException(e);
		}
	}

	/**
	 * Encodes the given file in Base64 format
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	private static String encode(File file) throws IOException, FileNotFoundException {
		byte[] fileBytes = Files.readAllBytes(file.toPath());
		if (fileBytes != null && fileBytes.length > 0)
			return Base64.getEncoder().encodeToString(fileBytes);
		throw new IOException("Could not read file :" + file.getName());
	}

	/**
	 * Constructs the default download path from the given parameters
	 * 
	 * @param orgId
	 * @param type
	 * @param userName
	 * @param attachmentsPath
	 * @return
	 */
	public static String constructDownloadPath(int orgId, String type, String userName, String attachmentsPath) {
		return attachmentsPath + "/" + orgId + "/" + AttachmentsConstants.MODULE_EMPLOYEE_PROVIDENT_FUND + "/" + type
				+ "/" + userName;
	}


}

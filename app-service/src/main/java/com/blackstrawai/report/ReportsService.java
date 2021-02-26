package com.blackstrawai.report;

import java.io.File;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.JSONHelper;
import com.blackstrawai.chartofaccounts.ChartOfAccountsDao;
import com.blackstrawai.common.BaseService;
import com.blackstrawai.common.DropDownDao;
import com.blackstrawai.externalintegration.compliance.TaxillaDao;
import com.blackstrawai.externalintegration.compliance.taxilla.TaxillaReportVo;
import com.blackstrawai.journals.JournalEntriesTransactionDao;
import com.blackstrawai.report.dropdowns.ReportsPeriodDropDownVo;
import com.blackstrawai.settings.chartofaccounts.ChartOfAccountsReportVo;
import com.blackstrawai.taxilla.Taxilla;

@Service
public class ReportsService extends BaseService {

	private Logger logger = Logger.getLogger(ReportsService.class);

	@Autowired
	private JournalEntriesTransactionDao journalEntriesTransactionDao;

	@Autowired
	private FinanceReportDao finReportDao;
	
	@Autowired
	private InventoryManagementReportDao invMgmtDAO;

	@Autowired
	private DropDownDao dropDownDao;

	@Autowired
	private ChartOfAccountsDao chartOfAccountsDao;
	
	@Autowired
	private TaxillaDao taxillaDao;

	public String getJournalEntriesBetweenDatesForOrganization(Integer organizationId, String startDate, String endDate,
			String dateFormat) throws ApplicationException {
		logger.info("Entry into method: getJournalEntriesBetweenDatesForOrganization");
		List<JournalEntriesTransactionReportVo> journalEntries = journalEntriesTransactionDao
				.getJournalEntriesBetweenDatesForOrganization(organizationId, startDate, endDate, dateFormat);
		return ReportsHelperService.convertJournalEntriesToString(journalEntries, dateFormat);
	}

	public TrialBalanceReportVo getTrialBalanceReport(Integer orgId, String oldStartDate, String oldEndDate, Integer PeriodId)
			throws ApplicationException {
		logger.info("Enter into getTrialBalanceReport - service layer");
		TrialBalanceReportVo trialBalanceVo = new TrialBalanceReportVo();
		String startDateFinal, endDateFinal = null;

		try {
			logger.info("Enter into try block- getTrialBalanceReport - service layer");
			if (PeriodId == 1) {
				logger.info("Enter into try block- getBalanceSheetAllData - service layer");
				trialBalanceVo = finReportDao.getTrialBalanceAllDates(orgId);
			} else {

				ReportPeriodDatesVo reportDates = getReportDates(oldStartDate, oldEndDate, PeriodId);
				startDateFinal = reportDates.getStartDate();
				endDateFinal = reportDates.getEndDate();
				trialBalanceVo = finReportDao.getTrialBalanceReport(orgId, startDateFinal, endDateFinal);
			}
		} catch (ApplicationException e) {
			logger.info("Error getTrialBalanceReport - service layer " + e.getMessage());
			throw e;

		}

		return trialBalanceVo;
	}

	public BalanceSheetReportVo getBalanceSheet(Integer orgId, String oldStartDate, String oldEndDate, Integer PeriodId)
			throws ApplicationException {
		logger.info("Enter into getBalanceSheet - service layer");
		BalanceSheetReportVo balanceSheetVo = new BalanceSheetReportVo();

		String startDateFinal, endDateFinal = null;
		try {
			logger.info("Enter into try block- getBalanceSheet - service layer");
			if (PeriodId == 1) {
				logger.info("Enter into try block- getBalanceSheetAllData - service layer");
				balanceSheetVo = finReportDao.getBalanceSheetAllDates(orgId);
			} else {
				ReportPeriodDatesVo reportDates = getReportDates(oldStartDate, oldEndDate, PeriodId);
				startDateFinal = reportDates.getStartDate();
				endDateFinal = reportDates.getEndDate();
				balanceSheetVo = finReportDao.getBalanceSheet(orgId, startDateFinal, endDateFinal);
			}
		} catch (ApplicationException e) {
			logger.info("Error getBalanceSheet - service layer " + e.getMessage());
			throw e;

		}

		return balanceSheetVo;
	}

	public ProfitAndLossReportVo getProfitAndLoss(int organizationId, String startDate, String endDate, int periodId)
			throws ApplicationException {
		logger.info("Enter into getProfitAndLoss - service layer");
		ProfitAndLossReportVo profitAndLossVo = new ProfitAndLossReportVo();

		String startDateFinal, endDateFinal = null;
		logger.info("Enter into try block- getProfitAndLoss - service layer");
		if (periodId == 1) {
			logger.info("Enter into try block- getProfitAndLossAllData - service layer");
			profitAndLossVo = finReportDao.getProfitAndLossAllDates(organizationId);
		} else {
			ReportPeriodDatesVo reportDates = getReportDates(startDate, endDate, periodId);
			startDateFinal = reportDates.getStartDate();
			endDateFinal = reportDates.getEndDate();
			profitAndLossVo = finReportDao.getProfitAndLoss(organizationId, startDateFinal, endDateFinal);
		}

		return profitAndLossVo;
	}

	public InventoryMgmtReportVo getInventoryMgmtReport(int organizationId, String startDate, String endDate,  int rateType, int periodId) throws ApplicationException {
		logger.info("Enter into getInventoryMgmtReport - service layer");

 		ReportPeriodDatesVo reportDates = new ReportPeriodDatesVo();
		if (periodId > 1) {
			reportDates  = getReportDates(startDate, endDate, periodId);
		}
		InventoryMgmtReportVo inventoryMgmtReportVo = invMgmtDAO.getInventoryManagementReport(organizationId, reportDates.getStartDate(),
				reportDates.getEndDate() ,rateType,periodId);

		return inventoryMgmtReportVo;
	}

	public ReportLedgerDetailsVo getLedgerDetails(Integer organizationId, String startDate, String endDate,
			Integer ledgerId) throws ApplicationException {
		logger.info("Enter into getLedgerDetails - service layer");
		ReportLedgerDetailsVo reportLedgerDetailsVo = new ReportLedgerDetailsVo();
		logger.info("Enter into try block- getLedgerDetails - service layer");
		if (ledgerId != null) {
			if (startDate.equalsIgnoreCase("alldate") && endDate.equalsIgnoreCase("alldate")) {
				reportLedgerDetailsVo = finReportDao.getLedgerDetailsAll(organizationId, ledgerId);
			} else {
				logger.info("Enter into try block- getProfitAndLossAllData - service layer");
				reportLedgerDetailsVo = finReportDao.getLedgerDetails(organizationId, startDate, endDate, ledgerId);
			}
		} else {
			logger.info("No Ledger Id  - Servicelayer ");
		}

		return reportLedgerDetailsVo;
	}

	public ReportsPeriodDropDownVo getReportsPeriodDropDownData() throws ApplicationException {
		logger.info("Entry into method: getReportsPeriodDropDownData");

		return dropDownDao.getReportsPeriod();
	}

	public ReportPeriodDatesVo getReportDates(String oldStartDate, String oldEndDate, Integer PeriodId) {
		ReportPeriodDatesVo reportDates = new ReportPeriodDatesVo();
		LocalDate date = LocalDate.now();
		// LocalDate date = LocalDate.parse("2020-01-01");

//		LocalDate date  = LocalDate.parse("2020-04-01");
		TemporalField fieldISO = WeekFields.of(Locale.US).dayOfWeek();
		LocalDate startDate = null, endDate = null;
		switch (PeriodId) {
		case 2:
			logger.info("custom dates");
			startDate = LocalDate.parse(oldStartDate);
			endDate = LocalDate.parse(oldEndDate);
			break;
		case 3:
			logger.info("today");
			startDate = date;
			endDate = date;
			break;

		case 4:
			logger.info("This week");
			startDate = date.with(fieldISO, 1);
			endDate = date.with(fieldISO, 7);
			break;

		case 5:
			logger.info("This Week-to-date");

			startDate = date.with(fieldISO, 1);
			endDate = LocalDate.now();
			break;

		case 6:
			logger.info("This Month");

			startDate = date.withDayOfMonth(1);
			endDate = date.withDayOfMonth(date.lengthOfMonth());
			break;
		case 7:
			logger.info("This Month-to-date");
			startDate = date.withDayOfMonth(1);
			endDate = LocalDate.now();
			break;

		case 8:
			logger.info("This Calendar Quarter");

			startDate = date.with(date.getMonth().firstMonthOfQuarter()).with(TemporalAdjusters.firstDayOfMonth());
			endDate = startDate.plusMonths(2).with(TemporalAdjusters.lastDayOfMonth());
			break;

		case 9:
			logger.info("This Calendar Quarter-to-date");

			startDate = date.with(date.getMonth().firstMonthOfQuarter()).with(TemporalAdjusters.firstDayOfMonth());
			endDate = LocalDate.now();
			break;
		case 10:
			logger.info("This Financial Quarter");

			startDate = date.with(date.getMonth().firstMonthOfQuarter()).with(TemporalAdjusters.firstDayOfMonth());
			endDate = startDate.plusMonths(2).with(TemporalAdjusters.lastDayOfMonth());
			break;

		case 11:
			logger.info("This financial Quarter-to-date");
			startDate = date.with(date.getMonth().firstMonthOfQuarter()).with(TemporalAdjusters.firstDayOfMonth());
			endDate = date;
			break;

		case 12:
			logger.info("Calendar year");
			startDate = date.with(TemporalAdjusters.firstDayOfYear());
			endDate = date.with(TemporalAdjusters.lastDayOfYear());
			break;
		case 13:
			logger.info("Calendar year to date");
			startDate = date.with(TemporalAdjusters.firstDayOfYear());
			endDate = LocalDate.now();
			break;
		case 14:
			logger.info("Calendar year to last month");
//			 startDate = date.with(TemporalAdjusters.firstDayOfYear());
			endDate = date.with(TemporalAdjusters.firstDayOfMonth()).minusDays(1);
			startDate = endDate.with(TemporalAdjusters.firstDayOfYear());
			break;

		case 15:
			logger.info("This Financial Year");
			startDate = date.with(TemporalAdjusters.firstDayOfYear()).plusMonths(3);
			endDate = date.with(TemporalAdjusters.lastDayOfYear()).plusMonths(3);
			break;

		case 16:
			logger.info("This Financial Year to date");
			startDate = date.with(TemporalAdjusters.firstDayOfYear()).plusMonths(3);
			endDate = LocalDate.now();
			break;
		case 17:
			logger.info("This Financial Year to to last month");
			startDate = date.with(TemporalAdjusters.firstDayOfYear()).plusMonths(3);
			endDate = date.with(TemporalAdjusters.firstDayOfMonth()).minusDays(1).compareTo(startDate) > 0
					? date.with(TemporalAdjusters.firstDayOfMonth()).minusDays(1)
					: startDate;
			break;
		case 18:
			logger.info("yesterday");
			startDate = date.minusDays(1);
			endDate = date;
			break;
		case 20:
			logger.info("Last week");
			// LocalDate date2 = LocalDate.
			startDate = date.with(fieldISO, 1).minusDays(7);
			endDate = date.with(fieldISO, 1).minusDays(1);
			break;
		case 21:
			logger.info("Last week - to date");

			startDate = date.with(fieldISO, 1).minusDays(7);
			endDate = date;
			break;
		case 22:
			logger.info("Last month");
			startDate = date.withDayOfMonth(1).minusMonths(1);
			endDate = date.withDayOfMonth(date.lengthOfMonth()).minusMonths(1);
			break;
		case 23:
			logger.info("Last month to date");

			startDate = date.withDayOfMonth(1).minusMonths(1);
			endDate = date;
			break;
		case 24:
			logger.info("Last Calendar Quarter");

			startDate = date.with(date.getMonth().firstMonthOfQuarter()).with(TemporalAdjusters.firstDayOfMonth())
					.minusMonths(3);
			endDate = startDate.plusMonths(2).with(TemporalAdjusters.lastDayOfMonth());
			break;
		case 25:
			logger.info("Last Calendar Quarter to date");

			startDate = date.with(date.getMonth().firstMonthOfQuarter()).with(TemporalAdjusters.firstDayOfMonth())
					.minusMonths(3);
			endDate = date;
			break;

		case 26:
			logger.info("Last Financial Quarter");

			startDate = date.with(date.getMonth().firstMonthOfQuarter()).with(TemporalAdjusters.firstDayOfMonth())
					.minusMonths(6);
			endDate = startDate.plusMonths(2).with(TemporalAdjusters.lastDayOfMonth());
			break;
		case 27:
			logger.info("Last Financial Quarter - to - date");

			startDate = date.with(date.getMonth().firstMonthOfQuarter()).with(TemporalAdjusters.firstDayOfMonth())
					.minusMonths(6);
			endDate = date;
			break;
		case 28:
			logger.info("Last Calendar year");

			startDate = date.with(TemporalAdjusters.firstDayOfYear()).minusYears(1);
			endDate = date.with(TemporalAdjusters.lastDayOfYear()).minusYears(1);
			break;
		case 29:
			logger.info("Last Calendar year to date");

			startDate = date.with(TemporalAdjusters.firstDayOfYear()).minusYears(1);
			endDate = date;
			break;

		case 30:
			logger.info("Last Financial Year");

			startDate = date.with(TemporalAdjusters.firstDayOfYear()).plusMonths(3).minusYears(1);
			endDate = date.with(TemporalAdjusters.lastDayOfYear()).plusMonths(3).minusYears(1);
			break;
		case 31:
			logger.info("Last Financial Year to date");

			startDate = date.with(TemporalAdjusters.firstDayOfYear()).plusMonths(3).minusYears(1);
			endDate = date;
			break;

		default:
			startDate = null;
			endDate = null;
		}

		logger.info("Start Date " + startDate.toString());
		logger.info("End date " + endDate.toString());

		reportDates.setStartDate(startDate.toString());
		reportDates.setEndDate(endDate.toString());

		return reportDates;
	}

	public String getCoaDownloadableReportForOrganization(Integer organizationId) throws ApplicationException {
		logger.info("Entry into method: getJournalEntriesBetweenDatesForOrganization");
		List<ChartOfAccountsReportVo> coaEntries = chartOfAccountsDao.getCoaReportOfAnOrganization(organizationId);
				
		return ReportsHelperService.convertCoaEntriesToString(coaEntries);
	}
	
	public List<ChartOfAccountsReportVo> getCoaReportOfAnOrganization(int orgId) throws ApplicationException {
		return chartOfAccountsDao.getCoaReportOfAnOrganization(orgId);
	}
	
	/**
	 * Get GST Report by GST Type and GST Search Type
	 * 
	 * @param taxilla
	 * @return encoded string of generated report
	 * @throws Exception
	 */
	public String getGst(TaxillaReportVo taxilla) throws Exception {
		String targetUrl = Taxilla.getInstance().getTaxillaMap().get(taxilla.getGstType());
		if (targetUrl == null) {
			throw new ApplicationException("GSTR Type not found");
		}
		String otp = taxillaDao.getOtp(taxilla.getOrganizationId(), Integer.parseInt(taxilla.getUserId()), taxilla.getRoleName());
		if (otp == null) {
			throw new ApplicationException("Invalid OTP");
		}
		String action = taxillaDao.getAction(taxilla.getGstSearchType());
		if (action == null) {
			throw new ApplicationException("Invalid GST Search Type");
		}
		JSONObject response = Taxilla.getInstance().getGspResponse(taxilla.getGstin(), taxilla.getUsername(), String.valueOf(System.currentTimeMillis()), taxilla.getRetPeriod(), targetUrl, action, otp);
		if (!ReportsHelperService.isValidResponse(response) && response.get("message") != null) {
			return (String)response.get("message");
		}
		String reportFileName = ReportsHelperService.getGstReportFileName(taxilla.getGstType(), taxilla.getGstSearchType());
		String filePath = ReportsHelperService.loadProperty("file_store_path") + "/" + reportFileName;
		JSONHelper.getInstance().exportJsonToCsv(response.toJSONString(), filePath);
		return ReportsHelperService.encode(new File(filePath));
	}

}

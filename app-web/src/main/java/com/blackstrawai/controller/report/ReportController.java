package com.blackstrawai.controller.report;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.blackstrawai.common.BaseController;
import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.common.Constants;
import com.blackstrawai.helper.ReportConvertToVoHelper;
import com.blackstrawai.report.BalanceSheetReportVo;
import com.blackstrawai.report.InventoryMgmtReportVo;
import com.blackstrawai.report.ProfitAndLossReportVo;
import com.blackstrawai.report.ReportLedgerDetailsVo;
import com.blackstrawai.report.ReportsService;
import com.blackstrawai.report.TrialBalanceReportVo;
import com.blackstrawai.report.dropdowns.ReportsPeriodDropDownVo;
import com.blackstrawai.request.JSONObject;
import com.blackstrawai.request.externalintegration.compliance.TaxillaReportRequest;
import com.blackstrawai.request.report.JournalEntriesReportRequest;
import com.blackstrawai.response.export.ExportResponse;
import com.blackstrawai.response.report.BalanceSheetReportResponse;
import com.blackstrawai.response.report.InventoryMgmtReportResponse;
import com.blackstrawai.response.report.ProfitAndLossReportResponse;
import com.blackstrawai.response.report.ReportLedgerDetailsResponse;
import com.blackstrawai.response.report.ReportPeriodDropDownResponse;
import com.blackstrawai.response.report.TrialBalanceReportResponse;
import com.blackstrawai.response.settings.ListChartOfAccountsReportResponse;
import com.blackstrawai.settings.chartofaccounts.ChartOfAccountsReportVo;

@RestController
@CrossOrigin
@RequestMapping("/decifer/reports")
public class ReportController extends BaseController {

	private Logger logger = Logger.getLogger(ReportController.class);

	@Autowired
	ReportsService reportsService;

	@RequestMapping(value = "/v1/report/journal_entry", method = RequestMethod.POST)
	public ResponseEntity<BaseResponse> getJournalReportForOrganization(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, @RequestBody JSONObject<JournalEntriesReportRequest> request) {
		BaseResponse response = new ExportResponse();
		String encodedString;
		JournalEntriesReportRequest journalEntriesRequest = request.getData();
		try {
			logger.info("Request Payload:" + generateRequestAndResponseLogPaylod(request));
			encodedString = reportsService.getJournalEntriesBetweenDatesForOrganization(
					journalEntriesRequest.getOrganizationId(), journalEntriesRequest.getStartDate(),
					journalEntriesRequest.getEndDate(), journalEntriesRequest.getDateFormat());
			((ExportResponse) response).setData(encodedString);
			setTokens(response, httpServletRequest.getHeader("keyToken"), httpServletRequest.getHeader("valueToken"));
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_JOURNAL_ENTRIES_REPORT,
					Constants.SUCCESS_JOURNAL_ENTRIES_REPORT, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error Payload:" + e.getMessage());
			if (e.getCause() != null) {
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_JOURNAL_ENTRIES_REPORT,
						e.getCause().getMessage(), Constants.FAILURE_DURING_GET);
			} else {
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_JOURNAL_ENTRIES_REPORT,
						e.getMessage(), Constants.FAILURE_DURING_GET);
			}

			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/v1/report/trialbalance/{organizationId}/{startDate}/{endDate}/{periodId}")
	public ResponseEntity<BaseResponse> getTrialBalance(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @PathVariable int organizationId, @PathVariable String startDate,
			@PathVariable String endDate, @PathVariable int periodId) {
		logger.info("Entry into getTrialBalance" + " Org Id " + organizationId + " start date " + startDate
				+ " end date " + endDate);
		BaseResponse response = new TrialBalanceReportResponse();
		try {
			logger.info("Controller - entered into try block");
			TrialBalanceReportVo trialBalanceVo = reportsService.getTrialBalanceReport(organizationId, startDate,
					endDate, periodId);
			logger.info("Controller - reportsService methodcalls-end");
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			logger.info("Controller - setTokens completed");
			((TrialBalanceReportResponse) response).setData(trialBalanceVo);
			logger.info("Controller - response set Data completed");
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_TRIAL_BALANCE_FETCH,
					Constants.SUCCESS_TRIAL_BALANCE_FETCH, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (Exception e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_TRIAL_BALANCE_FETCH,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/v1/report/balancesheet/{organizationId}/{startDate}/{endDate}/{periodId}")
	public ResponseEntity<BaseResponse> getBalanceSheet(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @PathVariable int organizationId, @PathVariable String startDate,
			@PathVariable String endDate, @PathVariable int periodId) {
		logger.info("Entry into getBalanceSheet" + " Org Id " + organizationId + " start date " + startDate
				+ " end date " + endDate + " period Id " + periodId);
		BaseResponse response = new BalanceSheetReportResponse();
		try {
			logger.info("Controller - entered into try block");
			BalanceSheetReportVo balanceSheetVo = reportsService.getBalanceSheet(organizationId, startDate, endDate,
					periodId);
			logger.info("Controller - reportsService methodcalls-end");
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			logger.info("Controller - setTokens completed");
			((BalanceSheetReportResponse) response).setData(balanceSheetVo);
			logger.info("Controller - response set Data completed");
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_BALANCE_SHEET_FETCH,
					Constants.SUCCESS_BALANCE_SHEET_FETCH, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (Exception e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_BALANCE_SHEET_FETCH,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/v1/report/profitandloss/{organizationId}/{startDate}/{endDate}/{periodId}")
	public ResponseEntity<BaseResponse> getProfitAndLoss(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @PathVariable int organizationId, @PathVariable String startDate,
			@PathVariable String endDate, @PathVariable int periodId) {
		logger.info("Entry into getProfitAndLoss" + " Org Id " + organizationId + " start date " + startDate
				+ " end date " + endDate + " period Id " + periodId);
		BaseResponse response = new ProfitAndLossReportResponse();
		try {
			logger.info("Controller - entered into try block");
			ProfitAndLossReportVo profitAndLossVo = reportsService.getProfitAndLoss(organizationId, startDate, endDate,
					periodId);
			logger.info("Controller - reportsService methodcalls-end");
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			logger.info("Controller - setTokens completed");
			((ProfitAndLossReportResponse) response).setData(profitAndLossVo);
			logger.info("Controller - response set Data completed");
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_PROFIT_AND_LOSS_FETCH,
					Constants.SUCCESS_PROFIT_AND_LOSS_FETCH, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (Exception e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_PROFIT_AND_LOSS_FETCH,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/v1/dropsdowns/periods/{organizationId}")
	public ResponseEntity<BaseResponse> getReportsPeriodDropDown(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, @PathVariable int organizationId) {
		logger.info("Entry into getReportsPeriodDropDown");
		BaseResponse response = new ReportPeriodDropDownResponse();
		try {
			ReportsPeriodDropDownVo data = reportsService.getReportsPeriodDropDownData();
			setTokens(response, httpServletRequest.getHeader("keyToken"), httpServletRequest.getHeader("valueToken"));
			((ReportPeriodDropDownResponse) response).setData(data);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_REPORT_PERIOD_FETCH,
					Constants.SUCCESS_REPORT_PERIOD_FETCH, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);

		} catch (Exception e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_REPORT_PERIOD_FETCH,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@RequestMapping(value = "/v1/report/ledgers/ledger/{organizationId}/{startDate}/{endDate}/{id}")
	public ResponseEntity<BaseResponse> getLedgerDetails(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @PathVariable int organizationId, @PathVariable String startDate,
			@PathVariable String endDate, @PathVariable int id) {
		logger.info("Entry into getProfitAndLoss" + " Org Id " + organizationId + " start date " + startDate
				+ " end date " + endDate + " Ledger Id " + id);
		BaseResponse response = new ReportLedgerDetailsResponse();
		try {
			logger.info("Controller - entered into try block");
			ReportLedgerDetailsVo reportLedgerDetailsVo = reportsService.getLedgerDetails(organizationId, startDate,
					endDate, id);
			logger.info("Controller - reportsService methodcalls-end");
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			logger.info("Controller - setTokens completed");
			((ReportLedgerDetailsResponse) response).setData(reportLedgerDetailsVo);
			logger.info("Controller - response set Data completed");
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_REPORT_LEDGER_DETAILS_FETCH,
					Constants.SUCCESS_REPORT_LEDGER_DETAILS_FETCH, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (Exception e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_REPORT_LEDGER_DETAILS_FETCH,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/v1/report/inventory/summary/{organizationId}/{startDate}/{endDate}/{rateType}/{periodId}")
	public ResponseEntity<BaseResponse> getInventoryMgmtReport(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @PathVariable int organizationId, @PathVariable String startDate,
			@PathVariable String endDate, @PathVariable int rateType, @PathVariable int periodId) {
		logger.info("Entry into getInventoryMgmtReport" + " Org Id " + organizationId + " start date " + startDate
				+ " end date " + endDate + " period Id " + periodId);
		BaseResponse response = new InventoryMgmtReportResponse();
		try {
			logger.info("Controller - entered into try block");
			InventoryMgmtReportVo inventoryMgmtReportVo = reportsService.getInventoryMgmtReport(organizationId,
					startDate, endDate, rateType, periodId);
			logger.info("Controller - reportsService methodcalls-end");
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			logger.info("Controller - setTokens completed");
			((InventoryMgmtReportResponse) response).setData(inventoryMgmtReportVo);
			logger.info("Controller - response set Data completed");
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_INVENTORY_MGMT_REPORT_FETCH,
					Constants.SUCCESS_INVENTORY_MGMT_REPORT_FETCH, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (Exception e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_INVENTORY_MGMT_REPORT_FETCH,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// For Fetching all Currencies belonging to an Organization
	@RequestMapping(value = "/v1/report/coa/{organizationId}")
	public ResponseEntity<BaseResponse> getCoaReportForOrganization(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, @PathVariable int organizationId) {
		logger.info("Entry into getCoaReportForOrganization");
		BaseResponse response = new ListChartOfAccountsReportResponse();
		try {
			List<ChartOfAccountsReportVo> listAllCurrencies = reportsService
					.getCoaReportOfAnOrganization(organizationId);
			setTokens(response, httpServletRequest.getHeader("keyToken"), httpServletRequest.getHeader("valueToken"));
			((ListChartOfAccountsReportResponse) response).setData(listAllCurrencies);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_COA_REPORT_FETCH,
					Constants.SUCCESS_COA_REPORT_FETCH, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (Exception e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_COA_REPORT_FETCH,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@RequestMapping(value = "/v1/report/gst", method = RequestMethod.POST)
	public ResponseEntity<BaseResponse> getGstReport(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, @RequestBody JSONObject<TaxillaReportRequest> request) {
		BaseResponse response = new ExportResponse();
		String encodedString;
		try {
			logger.info("Request Payload:" + generateRequestAndResponseLogPaylod(request));
			encodedString = reportsService.getGst(ReportConvertToVoHelper.getInstance().convertTaxillaReportRequestToVo(request.getData()));
			((ExportResponse) response).setData(encodedString);
			setTokens(response, httpServletRequest.getHeader("keyToken"), httpServletRequest.getHeader("valueToken"));
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_GST_REPORT,
					Constants.SUCCESS_GST_REPORT, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error Payload:" + e.getMessage());
			if (e.getCause() != null) {
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_GST_REPORT,
						e.getCause().getMessage(), Constants.FAILURE_DURING_GET);
			} else {
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_GST_REPORT,
						e.getMessage(), Constants.FAILURE_DURING_GET);
			}

			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}

package com.blackstrawai.report;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.common.BaseDao;

@Repository
public class FinanceReportDao extends BaseDao {

	private Logger logger = Logger.getLogger(FinanceReportDao.class);
	
	public TrialBalanceReportVo getTrialBalanceReport(Integer orgId,String startDate , String endDate) throws ApplicationException {
		logger.info("To get TrialBalance for the org " + orgId + " between "+ startDate + " and "+endDate);
		TrialBalanceReportVo trialBalanceVo = null;
		if (orgId != null && startDate != null &&  endDate != null ) {
			Connection con = null;
			try {
				con = getUserMgmConnection(); 
				trialBalanceVo = new TrialBalanceReportVo();
				trialBalanceVo.setOrgId(orgId);
				trialBalanceVo.setStartDate(startDate);
				trialBalanceVo.setEndDate(endDate);
				List<TrialBalanceReportGeneralVo> trialBalanceData = getTrialBalanceData(orgId,startDate,endDate, con);
				trialBalanceVo.setTrialBalanceData(trialBalanceData);
			} catch (Exception e) {
				logger.info("Error in getTrialBalanceReport:: ", e);
				throw new ApplicationException(e);
			}finally{
				closeResources(null,null,con);
			}
		}
		return trialBalanceVo;
	}

	public List<TrialBalanceReportGeneralVo> getTrialBalanceData(Integer orgId,String startDate , String endDate, Connection con)
			throws ApplicationException {
		logger.info("To get  in getTrialBalanceData ");
		List<TrialBalanceReportGeneralVo> trialBalanceDataVos = new ArrayList<TrialBalanceReportGeneralVo>();
		PreparedStatement preparedStatement =null;
		ResultSet rs =null;
		try {
			preparedStatement = con.prepareStatement(FinanceReportsConstants.GET_TRIAL_BALANCE_DATA);
			preparedStatement.setInt(1,orgId );
			preparedStatement.setString(2,startDate );
			preparedStatement.setInt(3,orgId );
			preparedStatement.setString(4,startDate );
			preparedStatement.setString(5,endDate );
			preparedStatement.setInt(6,orgId );
			preparedStatement.setString(7,startDate );
			preparedStatement.setInt(8,orgId );
			preparedStatement.setString(9,startDate );
			preparedStatement.setString(10,endDate );
			preparedStatement.setInt(11,orgId );
			preparedStatement.setString(12,startDate );
			preparedStatement.setInt(13,orgId );
			preparedStatement.setString(14,startDate );
			preparedStatement.setString(15,endDate );
			preparedStatement.setInt(16,orgId );
			preparedStatement.setString(17,startDate );
			preparedStatement.setInt(18,orgId );
			preparedStatement.setString(19,startDate );
			preparedStatement.setString(20,endDate );
			rs = preparedStatement.executeQuery(); 
			while (rs.next()) {
				TrialBalanceReportGeneralVo trialBalanceDataVo = new TrialBalanceReportGeneralVo();
				trialBalanceDataVo.setLevelType(rs.getString(1));
				trialBalanceDataVo.setLevel2Name(rs.getString(2));
				trialBalanceDataVo.setLevel3Name(rs.getString(3));
				trialBalanceDataVo.setLevel4Name(rs.getString(4));
				trialBalanceDataVo.setLevel5Name(rs.getString(5));
				trialBalanceDataVo.setLedgerId(rs.getString(6));
				trialBalanceDataVo.setJournalBookName(rs.getString(7));
				trialBalanceDataVo.setOpeningBalance(rs.getBigDecimal(8));
				trialBalanceDataVo.setClosingBalance(rs.getBigDecimal(9));					
				logger.info(trialBalanceDataVo);
				trialBalanceDataVos.add(trialBalanceDataVo);			}
			logger.info("Successfully fetched  getTrialBalanceData " + trialBalanceDataVos);
		} catch (Exception e) {
			logger.info("Error in getTrialBalanceData ", e);
			throw new ApplicationException(e);
		}finally{
			closeResources(rs, preparedStatement, null);
		}
		return trialBalanceDataVos;
	}

	public TrialBalanceReportVo getTrialBalanceAllDates(Integer orgId) throws ApplicationException {
		logger.info("To get Trial Balance - All Data for the org " + orgId );
		TrialBalanceReportVo trialBalanceVo = null;
		if (orgId != null  ) {
			Connection con =null;
			try {
				con = getUserMgmConnection();
				trialBalanceVo = new TrialBalanceReportVo();
				trialBalanceVo.setOrgId(orgId);
				List<TrialBalanceReportGeneralVo> trialBalanceData = getTrialBalanceAllData(orgId, con);
				trialBalanceVo.setTrialBalanceData(trialBalanceData);
			} catch (Exception e) {
				logger.info("Error in getTrialBalanceAllDates:: ", e);
				throw new ApplicationException(e);
			}finally{
				closeResources(null,null,con);
			}
		}
		return trialBalanceVo;
	}

	private List<TrialBalanceReportGeneralVo> getTrialBalanceAllData(Integer orgId, Connection con) throws ApplicationException {
		logger.info("To get  in getTrialBalanceAllData  for the org " + orgId );
		List<TrialBalanceReportGeneralVo> trialBalanceDataVos = new ArrayList<TrialBalanceReportGeneralVo>();
		PreparedStatement preparedStatement =null;
		ResultSet rs =null;
		try {
			preparedStatement = con.prepareStatement(FinanceReportsConstants.GET_TRIAL_BALANCE_DATA_ALL);
			preparedStatement.setInt(1,orgId );		
			preparedStatement.setInt(2,orgId );
			preparedStatement.setInt(3,orgId );		
			preparedStatement.setInt(4,orgId );
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				TrialBalanceReportGeneralVo trialBalanceDataVo = new TrialBalanceReportGeneralVo();
				trialBalanceDataVo.setLevelType(rs.getString(1));
				trialBalanceDataVo.setLevel2Name(rs.getString(2));
				trialBalanceDataVo.setLevel3Name(rs.getString(3));
				trialBalanceDataVo.setLevel4Name(rs.getString(4));
				trialBalanceDataVo.setLevel5Name(rs.getString(5));
				trialBalanceDataVo.setLedgerId(rs.getString(6));
				trialBalanceDataVo.setJournalBookName(rs.getString(7));
				trialBalanceDataVo.setOpeningBalance(rs.getBigDecimal(8));
				trialBalanceDataVo.setClosingBalance(rs.getBigDecimal(9));					
				logger.info(trialBalanceDataVo);
				trialBalanceDataVos.add(trialBalanceDataVo);			}
			logger.info("Successfully fetched  getBalanceSheetData " + trialBalanceDataVos);
		} catch (Exception e) {
			logger.info("Error in getBalanceSheetData ", e);
			throw new ApplicationException(e);
		}finally{
			closeResources(rs, preparedStatement, null);
		}
		return trialBalanceDataVos;
	}


	public BalanceSheetReportVo getBalanceSheet(Integer orgId,String startDate , String endDate) throws ApplicationException {
		logger.info("To get Balance Sheet for the org " + orgId + " between "+ startDate + " and "+endDate);
		BalanceSheetReportVo balanceSheetVo = null;
		if (orgId != null && startDate != null &&  endDate != null  ) {
			Connection con = null;
			try  {
				con = getUserMgmConnection();
				balanceSheetVo = new BalanceSheetReportVo();
				balanceSheetVo.setOrgId(orgId);
				balanceSheetVo.setStartDate(startDate);
				balanceSheetVo.setEndDate(endDate);
				List<BalanceSheetReportGeneralVo> balanceSheetData = getBalanceSheetData(orgId,startDate,endDate, con);
				balanceSheetVo.setBalanceSheetData(balanceSheetData);
			} catch (Exception e) {
				logger.info("Error in getBalanceSheet:: ", e);
				throw new ApplicationException(e);
			}finally{
				closeResources(null, null, con);
			}
		}
		return balanceSheetVo;
	}

	public List<BalanceSheetReportGeneralVo> getBalanceSheetData(Integer orgId,String startDate , String endDate, Connection con)
			throws ApplicationException {
		logger.info("To get  in getBalanceSheetData  for the org " + orgId + " between "+ startDate + " and "+startDate);
		List<BalanceSheetReportGeneralVo> balanceSheetDataVos = new ArrayList<BalanceSheetReportGeneralVo>();
		PreparedStatement preparedStatement =null;
		ResultSet rs =null;
		try {
			preparedStatement = con.prepareStatement(FinanceReportsConstants.GET_BALANCE_SHEET_DATA);
			preparedStatement.setInt(1,orgId );
			preparedStatement.setString(2,startDate );
			preparedStatement.setInt(3,orgId );
			preparedStatement.setString(4,startDate );
			preparedStatement.setString(5,endDate);
			preparedStatement.setInt(6,orgId );
			preparedStatement.setString(7,startDate );
			preparedStatement.setInt(8,orgId );
			preparedStatement.setString(9,startDate);
			preparedStatement.setString(10,endDate);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				BalanceSheetReportGeneralVo balanceSheetDataVo = new BalanceSheetReportGeneralVo();
				balanceSheetDataVo.setLevelType(rs.getString(1));
				balanceSheetDataVo.setLevel2Name(rs.getString(2));
				balanceSheetDataVo.setLevel3Name(rs.getString(3));
				balanceSheetDataVo.setLevel4Name(rs.getString(4));
				balanceSheetDataVo.setLevel5Name(rs.getString(5));
				balanceSheetDataVo.setLedgerId(rs.getString(6));
				balanceSheetDataVo.setJournalBookName(rs.getString(7));
				balanceSheetDataVo.setOpeningBalance(rs.getBigDecimal(8));
				balanceSheetDataVo.setClosingBalance(rs.getBigDecimal(9));				
				logger.info(balanceSheetDataVo);
				balanceSheetDataVos.add(balanceSheetDataVo);
			}
			logger.info("Successfully fetched  getBalanceSheetData " + balanceSheetDataVos);
		} catch (Exception e) {
			logger.info("Error in getBalanceSheetData ", e);
			throw new ApplicationException(e);
		}finally{
			closeResources(rs, preparedStatement, null);
		}
		return balanceSheetDataVos;
	}

	public BalanceSheetReportVo getBalanceSheetAllDates(Integer orgId) throws ApplicationException {
		logger.info("To get Balance Sheet for the org " + orgId );
		BalanceSheetReportVo balanceSheetVo = null;
		if (orgId != null  ) {
			Connection con =null;
			try  {
				con = getUserMgmConnection();
				balanceSheetVo = new BalanceSheetReportVo();
				balanceSheetVo.setOrgId(orgId);
				List<BalanceSheetReportGeneralVo> balanceSheetData = getBalanceSheetAllData(orgId, con);
				balanceSheetVo.setBalanceSheetData(balanceSheetData);
			} catch (Exception e) {
				logger.info("Error in getBalanceSheet:: ", e);
				throw new ApplicationException(e);
			}finally{
				closeResources(null, null, con);
			}
		}
		return balanceSheetVo;
	}


	private List<BalanceSheetReportGeneralVo> getBalanceSheetAllData(Integer orgId, Connection con) throws ApplicationException {
		logger.info("To get  in getBalanceSheetData  for the org " + orgId );
		List<BalanceSheetReportGeneralVo> balanceSheetDataVos = new ArrayList<BalanceSheetReportGeneralVo>();
		PreparedStatement preparedStatement =null;
		ResultSet rs =null;
		try {
			preparedStatement = con.prepareStatement(FinanceReportsConstants.GET_BALANCE_SHEET_DATA_ALL);
			preparedStatement.setInt(1,orgId );		
			preparedStatement.setInt(2,orgId );
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				BalanceSheetReportGeneralVo balanceSheetDataVo = new BalanceSheetReportGeneralVo();
				balanceSheetDataVo.setLevelType(rs.getString(1));
				balanceSheetDataVo.setLevel2Name(rs.getString(2));
				balanceSheetDataVo.setLevel3Name(rs.getString(3));
				balanceSheetDataVo.setLevel4Name(rs.getString(4));
				balanceSheetDataVo.setLevel5Name(rs.getString(5));
				balanceSheetDataVo.setLedgerId(rs.getString(6));
				balanceSheetDataVo.setJournalBookName(rs.getString(7));
				balanceSheetDataVo.setOpeningBalance(rs.getBigDecimal(8));
				balanceSheetDataVo.setClosingBalance(rs.getBigDecimal(9));					
				logger.info(balanceSheetDataVo);
				balanceSheetDataVos.add(balanceSheetDataVo);
			}
			logger.info("Successfully fetched  getBalanceSheetData " + balanceSheetDataVos);
		} catch (Exception e) {
			logger.info("Error in getBalanceSheetData ", e);
			throw new ApplicationException(e);
		}finally{
			closeResources(rs, preparedStatement, null);
		}
		return balanceSheetDataVos;
	}   
	public ProfitAndLossReportVo getProfitAndLoss(Integer orgId, String startDate, String endDate) throws ApplicationException {
		logger.info("To get Profit and Loss for the org " + orgId + " between "+ startDate + " and "+endDate);
		ProfitAndLossReportVo profitAndLossVo = null;
		if (orgId != null && startDate != null &&  endDate != null  ) {
			Connection con = null;
			try  {
				con = getUserMgmConnection();
				profitAndLossVo = new ProfitAndLossReportVo();
				profitAndLossVo.setOrgId(orgId);
				profitAndLossVo.setStartDate(startDate);
				profitAndLossVo.setEndDate(endDate);
				List<ProfitAndLossReportGeneralVo> profitAndLossData = getProfitAndLossData(orgId,startDate,endDate, con);
				profitAndLossVo.setProfitAndLossData(profitAndLossData);
			} catch (Exception e) {
				logger.info("Error in getProfitAndLoss:: ", e);
				throw new ApplicationException(e);
			}finally{
				closeResources(null, null, con);
			}
		}
		return profitAndLossVo;
	}


	private List<ProfitAndLossReportGeneralVo> getProfitAndLossData(Integer orgId, String startDate, String endDate,
			Connection con) throws ApplicationException {
		logger.info("To get  in getProfitAndLossData  for the org " + orgId + " between "+ startDate + " and "+startDate);
		List<ProfitAndLossReportGeneralVo> profitAndLossGeneralVos = new ArrayList<ProfitAndLossReportGeneralVo>();

		PreparedStatement preparedStatement =null;
		ResultSet rs =null;
		try {
			preparedStatement = con.prepareStatement(FinanceReportsConstants.GET_PROFIT_AND_LOSS_DATA);
			preparedStatement.setInt(1,orgId );
			preparedStatement.setString(2,startDate );
			preparedStatement.setInt(3,orgId );
			preparedStatement.setString(4,startDate );
			preparedStatement.setString(5,endDate);
			preparedStatement.setInt(6,orgId );
			preparedStatement.setString(7,startDate );
			preparedStatement.setInt(8,orgId );
			preparedStatement.setString(9,startDate);
			preparedStatement.setString(10,endDate);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				ProfitAndLossReportGeneralVo profitAndLossDataVo = new ProfitAndLossReportGeneralVo();
				profitAndLossDataVo.setLevelType(rs.getString(1));
				profitAndLossDataVo.setLevel2Name(rs.getString(2));
				profitAndLossDataVo.setLevel3Name(rs.getString(3));
				profitAndLossDataVo.setLevel4Name(rs.getString(4));
				profitAndLossDataVo.setLevel5Name(rs.getString(5));
				profitAndLossDataVo.setLedgerId(rs.getString(6));
				profitAndLossDataVo.setJournalBookName(rs.getString(7));
				profitAndLossDataVo.setOpeningBalance(rs.getBigDecimal(8));
				profitAndLossDataVo.setClosingBalance(rs.getBigDecimal(9));				
				logger.info(profitAndLossDataVo);
				profitAndLossGeneralVos.add(profitAndLossDataVo);
			}
			logger.info("Successfully fetched  getProfitAndLossData " + profitAndLossGeneralVos);
		} catch (Exception e) {
			logger.info("Error in getBalanceSheetData ", e);
			throw new ApplicationException(e);
		}finally{
			closeResources(rs, preparedStatement, null);
		}
		return profitAndLossGeneralVos;
	}

	public ProfitAndLossReportVo getProfitAndLossAllDates(Integer orgId) throws ApplicationException {
		logger.info("To get Profit and Loss for the org " + orgId );
		ProfitAndLossReportVo profitAndLossVo = null;
		if (orgId != null   ) {
			Connection con = null;
			try  {
				con = getUserMgmConnection();
				profitAndLossVo = new ProfitAndLossReportVo();
				profitAndLossVo.setOrgId(orgId);
				List<ProfitAndLossReportGeneralVo> profitAndLossData = getProfitAndLossAllData(orgId, con);
				profitAndLossVo.setProfitAndLossData(profitAndLossData);
			} catch (Exception e) {
				logger.info("Error in getProfitAndLossAllDates:: ", e);
				throw new ApplicationException(e);
			}finally{
				closeResources(null, null, con);
			}
		}
		return profitAndLossVo;
	}

	private List<ProfitAndLossReportGeneralVo> getProfitAndLossAllData(Integer orgId, Connection con) throws ApplicationException {
		logger.info("To get  in getProfitAndLossAllData  for the org " + orgId );
		List<ProfitAndLossReportGeneralVo> profitAndLossGeneralVos = new ArrayList<ProfitAndLossReportGeneralVo>();
		PreparedStatement preparedStatement =null;
		ResultSet rs =null;
		try {
			preparedStatement = con.prepareStatement(FinanceReportsConstants.GET_PROFIT_AND_LOSS_ALL_DATA);
			preparedStatement.setInt(1,orgId );
			preparedStatement.setInt(2,orgId );
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				ProfitAndLossReportGeneralVo profitAndLossDataVo = new ProfitAndLossReportGeneralVo();
				profitAndLossDataVo.setLevelType(rs.getString(1));
				profitAndLossDataVo.setLevel2Name(rs.getString(2));
				profitAndLossDataVo.setLevel3Name(rs.getString(3));
				profitAndLossDataVo.setLevel4Name(rs.getString(4));
				profitAndLossDataVo.setLevel5Name(rs.getString(5));
				profitAndLossDataVo.setLedgerId(rs.getString(6));
				profitAndLossDataVo.setJournalBookName(rs.getString(7));
				profitAndLossDataVo.setOpeningBalance(rs.getBigDecimal(8));
				profitAndLossDataVo.setClosingBalance(rs.getBigDecimal(9));						
				logger.info(profitAndLossDataVo);
				profitAndLossGeneralVos.add(profitAndLossDataVo);
			}
			logger.info("Successfully fetched  getProfitAndLossAllData " + profitAndLossGeneralVos);
		} catch (Exception e) {
			logger.info("Error in getBalanceSheetData ", e);
			throw new ApplicationException(e);
		}finally{
			closeResources(rs, preparedStatement, null);
		}
		return profitAndLossGeneralVos;
	}
	
	

	public ReportLedgerDetailsVo getLedgerDetails(Integer orgId, String startDate, String endDate,
			Integer ledgerId) throws ApplicationException {
		logger.info("To get Ledger details for the org " + orgId + " between "+ startDate + " and "+endDate);
		ReportLedgerDetailsVo reportLedgerDetailsVo = null;
		if (ledgerId != null && startDate != null &&  endDate != null  ) {
			Connection con = null;
			try  {
				con = getUserMgmConnection();
				reportLedgerDetailsVo = new ReportLedgerDetailsVo();
				reportLedgerDetailsVo.setOrgId(orgId);
				reportLedgerDetailsVo.setStartDate(startDate);
				reportLedgerDetailsVo.setEndDate(endDate);
				List<ReportLedgerDataVo> reportLedgerData = getReportLedgerData(orgId,startDate,endDate,ledgerId, con);
				reportLedgerDetailsVo.setReportLedgerData(reportLedgerData);
			} catch (Exception e) {
				logger.info("Error in getLedgerDetails:: ", e);
				throw new ApplicationException(e);
			}finally{
				closeResources(null, null, con);
			}
		}
		return reportLedgerDetailsVo;
	}

	private List<ReportLedgerDataVo> getReportLedgerData(Integer orgId, String startDate, String endDate,
			Integer ledgerId, Connection con) throws ApplicationException {
		logger.info("To get  in getReportLedgerData  for the org " + orgId + " between "+ startDate + " and "+startDate);
		List<ReportLedgerDataVo> reportLedgerDataVos = new ArrayList<ReportLedgerDataVo>();

		PreparedStatement preparedStatement =null;
		ResultSet rs =null;
		try {
			preparedStatement = con.prepareStatement(FinanceReportsConstants.GET_REPORT_LEDGER_DATA);
			preparedStatement.setInt(1,orgId );
			preparedStatement.setString(2,startDate );
			preparedStatement.setInt(3,orgId );
			preparedStatement.setString(4,startDate );
			preparedStatement.setString(5,endDate);
			preparedStatement.setInt(6,ledgerId );
			preparedStatement.setInt(7,orgId );
			preparedStatement.setString(8,startDate);
			preparedStatement.setString(9,endDate);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				ReportLedgerDataVo reportLedgerDataVo = new ReportLedgerDataVo();
				reportLedgerDataVo.setDataId(rs.getString(1));	
				reportLedgerDataVo.setParticulars(rs.getString(2));
				reportLedgerDataVo.setEffectiveDate(rs.getString(3));	
				reportLedgerDataVo.setVendorInvoiceNumber(rs.getString(4));
				reportLedgerDataVo.setVendorPONumber(rs.getString(5));	
				reportLedgerDataVo.setVoucherNumber(rs.getString(6));
				reportLedgerDataVo.setCustomerInvoiceNumber(rs.getString(7));	
				reportLedgerDataVo.setCustomerPONumber(rs.getString(8));
				reportLedgerDataVo.setCreditNoteNumber(rs.getString(9));	
				reportLedgerDataVo.setDebitNoteNumber(rs.getString(10));
				reportLedgerDataVo.setType(rs.getString(11));	
				reportLedgerDataVo.setAccountCode(rs.getString(12));
				reportLedgerDataVo.setOpeningBalance(rs.getBigDecimal(13));	
				reportLedgerDataVo.setPeriodBalance(rs.getBigDecimal(14));
				reportLedgerDataVo.setCreditAmount(rs.getBigDecimal(15));	
				reportLedgerDataVo.setDebitAmount(rs.getBigDecimal(16));
				logger.info(reportLedgerDataVo);
				reportLedgerDataVos.add(reportLedgerDataVo);
			}
			logger.info("Successfully fetched  getReportLedgerData " + reportLedgerDataVos);
		} catch (Exception e) {
			logger.info("Error in getReportLedgerData ", e);
			throw new ApplicationException(e);
		}finally{
			closeResources(rs, preparedStatement, null);
		}
		return reportLedgerDataVos;
	}

	public ReportLedgerDetailsVo getLedgerDetailsAll(Integer orgId, Integer ledgerId) throws ApplicationException {
		logger.info("To get all the Ledger details for the org " + orgId + " and the ledger id "+ledgerId);
		ReportLedgerDetailsVo reportLedgerDetailsVo = null;
		if (ledgerId != null && orgId !=null  ) {
			Connection con = null;
			try  {
				con = getUserMgmConnection();
				reportLedgerDetailsVo = new ReportLedgerDetailsVo();
				reportLedgerDetailsVo.setOrgId(orgId);
				List<ReportLedgerDataVo> reportLedgerData = getReportLedgerDataAll(orgId,ledgerId, con);
				reportLedgerDetailsVo.setReportLedgerData(reportLedgerData);
			} catch (Exception e) {
				logger.info("Error in getLedgerDetails:: ", e);
				throw new ApplicationException(e);
			}finally{
				closeResources(null, null, con);
			}
		}
		return reportLedgerDetailsVo;
	}

	private List<ReportLedgerDataVo> getReportLedgerDataAll(Integer orgId, Integer ledgerId, Connection con) throws ApplicationException {
		logger.info("To get  in getReportLedgerDataAll  for the org " + orgId + "for the ledgerId "+ledgerId);
		List<ReportLedgerDataVo> reportLedgerDataVos = new ArrayList<ReportLedgerDataVo>();

		PreparedStatement preparedStatement =null;
		ResultSet rs =null;
		try {
			preparedStatement = con.prepareStatement(FinanceReportsConstants.GET_REPORT_LEDGER_DATA_ALL);
			preparedStatement.setInt(1,orgId );		
			preparedStatement.setInt(2,orgId );
			preparedStatement.setInt(3,ledgerId );
			preparedStatement.setInt(4,orgId );

			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				ReportLedgerDataVo reportLedgerDataVo = new ReportLedgerDataVo();
				reportLedgerDataVo.setDataId(rs.getString(1));	
				reportLedgerDataVo.setParticulars(rs.getString(2));
				reportLedgerDataVo.setEffectiveDate(rs.getString(3));	
				reportLedgerDataVo.setVendorInvoiceNumber(rs.getString(4));
				reportLedgerDataVo.setVendorPONumber(rs.getString(5));	
				reportLedgerDataVo.setVoucherNumber(rs.getString(6));
				reportLedgerDataVo.setCustomerInvoiceNumber(rs.getString(7));	
				reportLedgerDataVo.setCustomerPONumber(rs.getString(8));
				reportLedgerDataVo.setCreditNoteNumber(rs.getString(9));	
				reportLedgerDataVo.setDebitNoteNumber(rs.getString(10));
				reportLedgerDataVo.setType(rs.getString(11));	
				reportLedgerDataVo.setAccountCode(rs.getString(12));
				reportLedgerDataVo.setOpeningBalance(rs.getBigDecimal(13));	
				reportLedgerDataVo.setPeriodBalance(rs.getBigDecimal(14));
				reportLedgerDataVo.setCreditAmount(rs.getBigDecimal(15));	
				reportLedgerDataVo.setDebitAmount(rs.getBigDecimal(16));
				logger.info(reportLedgerDataVo);
				reportLedgerDataVos.add(reportLedgerDataVo);
			}
			logger.info("Successfully fetched  getReportLedgerDataAll " + reportLedgerDataVos);
		} catch (Exception e) {
			logger.info("Error in getReportLedgerDataAll ", e);
			throw new ApplicationException(e);
		}finally{
			closeResources(rs, preparedStatement, null);
		}
		return reportLedgerDataVos;
	}

	

	

}

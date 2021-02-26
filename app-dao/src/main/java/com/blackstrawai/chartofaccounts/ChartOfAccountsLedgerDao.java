package com.blackstrawai.chartofaccounts;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.DateConverter;
import com.blackstrawai.accounting.dropdowns.LedgerFilterDropDownVo;
import com.blackstrawai.accounting.ledger.LedgerListVo;
import com.blackstrawai.accounting.ledger.LedgerVo;
import com.blackstrawai.accounting.ledger.SubLedgerVo;
import com.blackstrawai.attachments.AttachmentsConstants;
import com.blackstrawai.attachments.AttachmentsDao;
import com.blackstrawai.attachments.AttachmentsVo;
import com.blackstrawai.attachments.UploadFileVo;
import com.blackstrawai.common.BaseDao;
import com.blackstrawai.common.CommonConstants;
import com.blackstrawai.common.FinanceCommonConstants;
import com.blackstrawai.common.FinanceCommonDao;
import com.blackstrawai.journals.JournalEntriesTransactionDao;
import com.blackstrawai.keycontact.CustomerConstants;
import com.blackstrawai.keycontact.EmployeeConstants;
import com.blackstrawai.keycontact.VendorConstants;
import com.blackstrawai.settings.SettingsAndPreferencesConstants;
import com.blackstrawai.settings.chartofaccounts.ChartOfAccountsLevel6Vo;
import com.blackstrawai.settings.chartofaccounts.MinimalChartOfAccountsLevel2Vo;

@Repository
public class ChartOfAccountsLedgerDao extends BaseDao{
	private Logger logger = Logger.getLogger(ChartOfAccountsLedgerDao.class);

	@Autowired
	private FinanceCommonDao financeCommonDao;

	@Autowired
	private AttachmentsDao attachmentsDao; 

	@Autowired
	private ChartOfAccountsDao chartOfAccountsDao;
	
	@Autowired
	private JournalEntriesTransactionDao journalEntriesTransactionDao;

	public LedgerVo createLedgerAndSubLedger(LedgerVo ledgerVo) throws ApplicationException {
		logger.info("Entry to method createLedgerAndSubLedger in Dao");
		try(final Connection con = getUserMgmConnection();final PreparedStatement preparedStatement = con.prepareStatement(ChartOfAccountsConstants.INSERT_INTO_LEDGER,Statement.RETURN_GENERATED_KEYS)){
			if(ledgerVo.getAccountCode()!=null && !ledgerVo.getAccountCode().isEmpty()) {
				Boolean isExistingAccountCode = isExistingAccountCode(ledgerVo.getAccountCode(),ledgerVo.getOrganizationId());
				if(isExistingAccountCode) {
					throw new ApplicationException("Account code already exist");
				}
			}

			/*if(ledgerVo.getModuleId()==null ||(ledgerVo.getModuleId()!=null && ledgerVo.getModuleId().equals(0))) {
				throw new ApplicationException("Module is Mandatory");
			}*/
			if(ledgerVo.getLedgerName()!=null && !ledgerVo.getLedgerName().isEmpty()) {
				Boolean isExistingLedger = isExistingLedgerName(ledgerVo.getLedgerName(),ledgerVo.getOrganizationId(),ledgerVo.getAccountNameId());
				if(isExistingLedger) {
					throw new ApplicationException("Ledger Exist for the Organization");
				}
			}
			
			if( ledgerVo.getModuleId()==null){
				ledgerVo.setModuleId(0);
			}
			preparedStatement.setString(1, ledgerVo.getLedgerName()!=null ? ledgerVo.getLedgerName() : null);
			preparedStatement.setString(2,  ledgerVo.getLedgerName()!=null ? ledgerVo.getLedgerName() : null);
			preparedStatement.setBoolean(3, false);
			preparedStatement.setString(4, ledgerVo.getLedgerStatus() ? CommonConstants.STATUS_AS_ACTIVE : CommonConstants.STATUS_AS_INACTIVE);
			preparedStatement.setInt(5, ledgerVo.getOrganizationId());
			preparedStatement.setInt(6, Integer.valueOf(ledgerVo.getUserId()));
			preparedStatement.setBoolean(7, ledgerVo.getIsSuperAdmin());
			preparedStatement.setInt(8, ledgerVo.getAccountNameId());
			preparedStatement.setString(9, ledgerVo.getAccountCode()!=null  && !ledgerVo.getAccountCode().isEmpty() ? ledgerVo.getAccountCode() : null);
			preparedStatement.setDouble(10, ledgerVo.getLedgerBalance() !=null  ? ledgerVo.getLedgerBalance() : 0.00);
			if(ledgerVo.getDateOfCreation()!=null && !ledgerVo.getDateOfCreation().isEmpty()) {
				String asOnDate = DateConverter.getInstance().correctDatePickerDateToString(ledgerVo.getDateOfCreation());
				logger.info("AsOnDAte::"+ asOnDate);
				if (asOnDate != null) {
				//	String date = DateConverter.getInstance().convertDateToGivenFormat(asOnDate, "yyyy-MM-dd");
					Date asonDate = Date.valueOf(asOnDate);
					logger.info("Converted Date is " +asonDate);
					preparedStatement.setDate(11,asonDate );
				}else {
					logger.info("converted poDate set as null");
					preparedStatement.setString(11, null);
				}
			}else {
				logger.info("converted poDate set as null");
				preparedStatement.setString(11, null);
			}
			preparedStatement.setBoolean(12, ledgerVo.getIsSubledgerMandatory());
			preparedStatement.setInt(13, ledgerVo.getModuleId());
			preparedStatement.setString(14,  ledgerVo.getLedgerName()!=null ? ledgerVo.getLedgerName() : null);
			preparedStatement.setInt(15, ledgerVo.getEntityId());
			preparedStatement.setString(16, ledgerVo.getRoleName());
			logger.info("prep stmnt::"+ preparedStatement.toString());
			int rowAffected = preparedStatement.executeUpdate();
			if (rowAffected == 1) {
				try (ResultSet rs = preparedStatement.getGeneratedKeys()) {
					while (rs.next()) {
						ledgerVo.setId(rs.getInt(1));
					}
				}
			}
			if(ledgerVo.getAttachments()!=null ) {
				attachmentsDao.createAttachments(ledgerVo.getOrganizationId(),ledgerVo.getUserId(),ledgerVo.getAttachments(), AttachmentsConstants.MODULE_TYPE_CHART_OF_ACCOUNTS, ledgerVo.getId(),ledgerVo.getRoleName());
			}
			return ledgerVo;
		} catch (Exception e) {
			logger.info("Exception in createLedgerAndSubLedger" , e);
			throw new ApplicationException(e.getMessage());
		}
	}

	// To be called by the Ledger thread 
	public void createSubLedger(LedgerVo ledgerVo) throws ApplicationException{
		logger.info("To create subledger " +ledgerVo);
		if(ledgerVo.getId()!=null && ledgerVo.getEntityId()!=null) {
			try {
				String entity = financeCommonDao.getEntity(ledgerVo.getEntityId());
				if(entity!=null) {
					switch (entity) {
					case ChartOfAccountsConstants.ENTITY_CUSTOMER:
						for (SubLedgerVo customer : getBasicCustomers(ledgerVo)) {
							insertSubLedger(customer);
						}
						break;
					case ChartOfAccountsConstants.ENTITY_EMPLOYEE:
						for (SubLedgerVo employee : getBasicEmployees(ledgerVo)) {
							insertSubLedger(employee);
						}
						break;
					case ChartOfAccountsConstants.ENTITY_VENDOR:
						for (SubLedgerVo vendor : getBasicVendors(ledgerVo)) {
							insertSubLedger(vendor);
						}
						break;
					case ChartOfAccountsConstants.ENTITY_TDS:
						for (SubLedgerVo tds : getBasicTds(ledgerVo)) {
							insertSubLedger(tds);
						}
						break;
					case ChartOfAccountsConstants.ENTITY_GST:
						for (SubLedgerVo taxRate : getBasicTaxRates(ledgerVo)) {
							insertSubLedger(taxRate);
						}
						break;
					}
				}
			} catch (Exception e) {
				logger.info("Exception in createSubLedger" , e);
				throw new ApplicationException(e.getMessage());
			}
		}
	}

	public void insertSubLedger(SubLedgerVo subLedger) throws ApplicationException{
		logger.info("Insert Subledger ");
		try(final Connection con = getUserMgmConnection();final PreparedStatement preparedStatement = con.prepareStatement(ChartOfAccountsConstants.INSERT_INTO_SUBLEDGER)){
			preparedStatement.setString(1, subLedger.getName());
			preparedStatement.setString(2, subLedger.getDescription());
			preparedStatement.setBoolean(3, subLedger.isBase());
			preparedStatement.setString(4, subLedger.getStatus());
			preparedStatement.setInt(5, subLedger.getOrgId());
			preparedStatement.setInt(6, subLedger.getUserId());
			preparedStatement.setBoolean(7, subLedger.getIsSuperAdmin());
			preparedStatement.setInt(8, subLedger.getLevel5Id());
			preparedStatement.setString(9, subLedger.getDisplayName());
			preparedStatement.executeUpdate();
			logger.info("Successfully insterted with name " + subLedger.getName());
		}catch (Exception e) {
			logger.info("Exception in insertSubLedger" , e);
			throw new ApplicationException(e.getMessage());
		}
	}


	public LedgerVo getLedgerById(Integer ledgerId,Integer orgId) throws ApplicationException {
		logger.info("To getLedgerById" + ledgerId);
		LedgerVo ledgerVo =null;
		try(final Connection con = getUserMgmConnection();final PreparedStatement preparedStatement = con.prepareStatement(ChartOfAccountsConstants.GET_LEDGER_BY_ID)){
			preparedStatement.setInt(1, ledgerId);
			try(ResultSet rs = preparedStatement.executeQuery()){
				while(rs.next()) {
					ledgerVo = new LedgerVo();
					ledgerVo.setAccountGroupId(rs.getInt(1));
					ledgerVo.setAccountNameId(rs.getInt(2));
					ledgerVo.setLedgerName(rs.getString(3));
					ledgerVo.setAccountCode(rs.getString(4));
					ledgerVo.setLedgerBalance(rs.getDouble(5));
					Date asOnDate = rs.getDate(6);
					if(asOnDate!=null ) {
						String date = DateConverter.getInstance().convertDateToGivenFormat(asOnDate, "yyyy-MM-dd");
						ledgerVo.setDateOfCreation(date);
					}
					ledgerVo.setModuleId(rs.getInt(7));
					ledgerVo.setLedgerStatus(rs.getBoolean(8));
					ledgerVo.setIsSubledgerMandatory(rs.getBoolean(9));
					ledgerVo.setEntityId(rs.getInt(10));
					ledgerVo.setIsBase(rs.getBoolean(11));
					ledgerVo.setOrganizationId(rs.getInt(12));
					ledgerVo.setUserId(String.valueOf(rs.getInt(13)));
					ledgerVo.setIsSuperAdmin(rs.getBoolean(14));
					ledgerVo.setId(ledgerId);
				}
			}
			if(ledgerVo.getIsBase()) {
				ledgerVo.setSubledgers(chartOfAccountsDao.getChartOfAccountsLevel6ByLevel5Id(ledgerId, orgId, con));
			}
			List<UploadFileVo> uploadFileVos = new ArrayList<UploadFileVo>();
			for(AttachmentsVo attachments : attachmentsDao.getAttachmentDetails(ledgerId, AttachmentsConstants.MODULE_TYPE_CHART_OF_ACCOUNTS))
			{
				UploadFileVo uploadFileVo = new UploadFileVo();
				uploadFileVo.setId(attachments.getId());
				uploadFileVo.setName(attachments.getFileName());
				uploadFileVo.setSize(attachments.getSize());
				uploadFileVos.add(uploadFileVo);
			}
			ledgerVo.setAttachments(uploadFileVos);
		}catch(Exception e) {
			logger.info("Exception in getLedgerById" , e);
			throw new ApplicationException(e.getMessage());
		}
		return ledgerVo;
	}



	// to get the default date of an organization
	/*private String getDefaultDateOfOrg(Integer orgId) throws ApplicationException {
		logger.info("To getDefaultDateOfOrg ");
		String dateFormat = null;
		try (final Connection conn = getUserMgmConnection()) {
			dateFormat = organizationDao.getDefaultDateForOrganization(orgId, conn);

		} catch (ApplicationException | SQLException e) {
			logger.info("Error in getDefaultDateOfOrg ", e);
			throw new ApplicationException(e);
		}
		return dateFormat;

	} */

	private List<SubLedgerVo> getBasicEmployees(LedgerVo ledgerVo) throws ApplicationException {
		logger.info("To getBasicEmployees ");
		List<SubLedgerVo> employees = null;
		try(final Connection con = getAccountsPayable() ;final PreparedStatement preparedStatement = con.prepareStatement(EmployeeConstants.GET_BASIC_EMPLOYEES_FOR_ORG)){
			preparedStatement.setInt(1, ledgerVo.getOrganizationId());
			employees = new ArrayList<SubLedgerVo>();
			try(ResultSet rs = preparedStatement.executeQuery()){
				while (rs.next()) {
					SubLedgerVo data=new SubLedgerVo();
					data.setName(""+rs.getInt(1));
					data.setDescription(ChartOfAccountsConstants.ENTITY_EMPLOYEE);
					data.setBase(false);
					data.setStatus(CommonConstants.STATUS_AS_ACTIVE);
					data.setOrgId(ledgerVo.getOrganizationId());
					data.setUserId(Integer.valueOf(ledgerVo.getUserId()));
					data.setLevel5Id(ledgerVo.getId());
					data.setDisplayName(rs.getString(2)+" "+ rs.getString(3));
					data.setIsSuperAdmin(ledgerVo.getIsSuperAdmin());
					employees.add(data);		
				}
			}
			logger.info("Successfully  getBasicEmployees size:: " + employees.size());
		} catch (ApplicationException | SQLException e) {
			logger.info("Error in getBasicEmployees ", e);
			throw new ApplicationException(e);
		}
		return employees;
	}

	private List<SubLedgerVo> getBasicCustomers(LedgerVo ledgerVo) throws ApplicationException {
		logger.info("To getBasicCustomers ");
		List<SubLedgerVo> customers = null;
		try(final Connection con = getAccountsReceivableConnection() ;final PreparedStatement preparedStatement = con.prepareStatement(CustomerConstants.GET_BASIC_CUSTOMER_FOR_ORG)){
			preparedStatement.setInt(1, ledgerVo.getOrganizationId());
			customers = new ArrayList<SubLedgerVo>();
			try(ResultSet rs = preparedStatement.executeQuery()){
				while (rs.next()) {
					SubLedgerVo data=new SubLedgerVo();
					data.setName(""+rs.getInt(1));
					data.setDescription(ChartOfAccountsConstants.ENTITY_CUSTOMER);
					data.setBase(false);
					data.setStatus(CommonConstants.STATUS_AS_ACTIVE);
					data.setOrgId(ledgerVo.getOrganizationId());
					data.setUserId(Integer.valueOf(ledgerVo.getUserId()));
					data.setLevel5Id(ledgerVo.getId());
					data.setDisplayName(rs.getString(2));
					data.setIsSuperAdmin(ledgerVo.getIsSuperAdmin());
					customers.add(data);		
				}
			}
			logger.info("Successfully  getBasicCustomers size:: " + customers.size());
		} catch (ApplicationException | SQLException e) {
			logger.info("Error in getBasicCustomers ", e);
			throw new ApplicationException(e);
		}
		return customers;
	}

	private List<SubLedgerVo> getBasicVendors(LedgerVo ledgerVo) throws ApplicationException {
		logger.info("To getBasicVendors ");
		List<SubLedgerVo> vendors = null;
		try(final Connection con = getAccountsPayable() ;final PreparedStatement preparedStatement = con.prepareStatement(VendorConstants.GET_BASIC_VENDOR_FOR_ORG)){
			preparedStatement.setInt(1, ledgerVo.getOrganizationId());
			vendors = new ArrayList<SubLedgerVo>();
			try(ResultSet rs = preparedStatement.executeQuery()){
				while (rs.next()) {
					SubLedgerVo data=new SubLedgerVo();
					data.setName(""+rs.getInt(1));
					data.setDescription(ChartOfAccountsConstants.ENTITY_VENDOR);
					data.setBase(false);
					data.setStatus(CommonConstants.STATUS_AS_ACTIVE);
					data.setOrgId(ledgerVo.getOrganizationId());
					data.setUserId(Integer.valueOf(ledgerVo.getUserId()));
					data.setLevel5Id(ledgerVo.getId());
					data.setDisplayName(rs.getString(2));
					data.setIsSuperAdmin(ledgerVo.getIsSuperAdmin());
					vendors.add(data);		
				}
			}
			logger.info("Successfully  getBasicVendors size:: " + vendors.size());
		} catch (ApplicationException | SQLException e) {
			logger.info("Error in getBasicVendors ", e);
			throw new ApplicationException(e);
		}
		return vendors;
	}


	private List<SubLedgerVo> getBasicTaxRates(LedgerVo ledgerVo) throws ApplicationException {
		logger.info("To getBasicTaxRates ");
		List<SubLedgerVo> taxRates = null;
		try(final Connection con = getUserMgmConnection() ;final PreparedStatement preparedStatement = con.prepareStatement(SettingsAndPreferencesConstants.GET_BASIC_TAX_RATES_FOR_ORG)){
			preparedStatement.setInt(1, ledgerVo.getOrganizationId());
			taxRates = new ArrayList<SubLedgerVo>();
			try(ResultSet rs = preparedStatement.executeQuery()){
				while (rs.next()) {
					SubLedgerVo data=new SubLedgerVo();
					data.setName(rs.getString(2));
					data.setDescription(ChartOfAccountsConstants.ENTITY_GST);
					data.setBase(false);
					data.setStatus(CommonConstants.STATUS_AS_ACTIVE);
					data.setOrgId(ledgerVo.getOrganizationId());
					data.setUserId(Integer.valueOf(ledgerVo.getUserId()));
					data.setLevel5Id(ledgerVo.getId());
					data.setDisplayName(rs.getString(2));
					data.setIsSuperAdmin(ledgerVo.getIsSuperAdmin());
					taxRates.add(data);		
				}
			}
			logger.info("Successfully  getBasicTaxRates size:: " + taxRates.size());
		} catch (ApplicationException | SQLException e) {
			logger.info("Error in getBasicTaxRates ", e);
			throw new ApplicationException(e);
		}
		return taxRates;
	}


	private List<SubLedgerVo> getBasicTds(LedgerVo ledgerVo) throws ApplicationException {
		logger.info("To getBasicTds ");
		List<SubLedgerVo> taxRates = null;
		try(final Connection con = getFinanceCommon() ;final  PreparedStatement preparedStatement = con.prepareStatement(FinanceCommonConstants.GET_TDS)){
			taxRates = new ArrayList<SubLedgerVo>();
			try(ResultSet rs = preparedStatement.executeQuery()){
				while (rs.next()) {
					SubLedgerVo data=new SubLedgerVo();
					data.setName(""+rs.getString(1));
					data.setDescription(ChartOfAccountsConstants.ENTITY_TDS);
					data.setBase(false);
					data.setStatus(CommonConstants.STATUS_AS_ACTIVE);
					data.setOrgId(ledgerVo.getOrganizationId());
					data.setUserId(Integer.valueOf(ledgerVo.getUserId()));
					data.setLevel5Id(ledgerVo.getId());
					data.setDisplayName(rs.getString(2));
					data.setIsSuperAdmin(ledgerVo.getIsSuperAdmin());
					taxRates.add(data);		
				}
			}
			logger.info("Successfully  getBasicTds size:: " + taxRates.size());
		} catch (ApplicationException | SQLException e) {
			logger.info("Error in getBasicTds ", e);
			throw new ApplicationException(e);
		}
		return taxRates;
	}


	private Boolean isExistingAccountCode(String code, Integer orgId) throws ApplicationException {
		Boolean isExistingCode = false;
		logger.info("To get getAccountCode ");
		try(final Connection con = getUserMgmConnection();final PreparedStatement preparedStatement = con.prepareStatement(ChartOfAccountsConstants.IS_ACCOUNT_CODE_EXIST)){
			preparedStatement.setInt(1, orgId);
			preparedStatement.setString(2, code);
			try(ResultSet rs = preparedStatement.executeQuery()){
				while(rs.next()) {
					isExistingCode = true;
				}
			}
			logger.info("isExistingCode::: "+ isExistingCode);
		}catch (ApplicationException | SQLException e) {
			logger.info("Error in getAccountCode ", e);
			throw new ApplicationException(e);
		}		
		return isExistingCode;
	}


	private Boolean isExistingLedgerName(String ledger, Integer orgId,Integer level4Id) throws ApplicationException {
		Boolean isExistingCode = false;
		logger.info("To get getAccountCode ");
		try(final Connection con = getUserMgmConnection();final PreparedStatement preparedStatement = con.prepareStatement(ChartOfAccountsConstants.IS_LEDGER_EXIST)){
			preparedStatement.setInt(1, orgId);
			preparedStatement.setInt(2, level4Id);
			preparedStatement.setString(3, ledger);
			try(ResultSet rs = preparedStatement.executeQuery()){
				while(rs.next()) {
					isExistingCode = true;
				}
			}
			logger.info("isExistingCode::: "+ isExistingCode);
		}catch (ApplicationException | SQLException e) {
			logger.info("Error in getAccountCode ", e);
			throw new ApplicationException(e);
		}		
		return isExistingCode;
	}

	private String getAccountCode(Integer ledgerId) throws ApplicationException {
		String accCode = null;
		logger.info("To get getAccountCode ");
		try(final Connection con = getUserMgmConnection();final PreparedStatement preparedStatement = con.prepareStatement(ChartOfAccountsConstants.GET_ACCOUNT_CODE_FOR_LEDGER)){
			preparedStatement.setInt(1, ledgerId);
			try(ResultSet rs = preparedStatement.executeQuery()){
				while(rs.next()) {
					accCode = rs.getString(1);
				}
			}
			logger.info("getAccountCode::: "+ accCode);
		}catch (ApplicationException | SQLException e) {
			logger.info("Error in getAccountCode ", e);
			throw new ApplicationException(e);
		}		
		return accCode;
	}


	private String getLedgerName(Integer ledgerId) throws ApplicationException {
		String lName = null;
		logger.info("To get getLedgerName ");
		try(final Connection con = getUserMgmConnection();final PreparedStatement preparedStatement = con.prepareStatement(ChartOfAccountsConstants.GET_LEDGER_NAME_FOR_LEDGER)){
			preparedStatement.setInt(1, ledgerId);
			try(ResultSet rs = preparedStatement.executeQuery()){
				while(rs.next()) {
					lName = rs.getString(1);
				}
			}
			logger.info("getLedgerName::: "+ lName);
		}catch (ApplicationException | SQLException e) {
			logger.info("Error in getLedgerName ", e);
			throw new ApplicationException(e);
		}		
		return lName;
	}

/*	private void changeStatusForLedgerTables(Integer id, String status, String query) throws ApplicationException {
		logger.info("To Change the status with query :: " +query);
		if(query!=null) {
			try(final Connection con = getUserMgmConnection();final PreparedStatement preparedStatement = con.prepareStatement(query)){
				preparedStatement.setString(1, status);
				preparedStatement.setDate(2, new Date(System.currentTimeMillis()));
				preparedStatement.setInt(3, id);
				preparedStatement.executeUpdate();
				logger.info("Status chaneged successfully ");
			}catch (Exception e) {
				logger.info("Error in changeStatusForInvoiceTables ", e); 
				throw new ApplicationException(e);
			}
		}

	}*/

	/*public void	deleteFromLedgerTable(Integer ledgerId) throws ApplicationException{
		try {
			changeStatusForLedgerTables(ledgerId, CommonConstants.STATUS_AS_DELETE,	ChartOfAccountsConstants.DELETE_LEDGER_BY_ID);
		} catch (ApplicationException e) {
			logger.info("Error in deleteFromLedgerTable ", e); 
			throw new ApplicationException(e);
		}

	}*/

	public Integer getEntityIdForLedgerId(Integer ledgerId) throws ApplicationException {
		Integer entityId = null;
		logger.info("To getEntityIdForLedgerId ");
		try(final Connection con = getUserMgmConnection();final PreparedStatement preparedStatement = con.prepareStatement(ChartOfAccountsConstants.GET_ENTITY_FOR_LEDGER)){
			preparedStatement.setInt(1, ledgerId);
			try(ResultSet rs = preparedStatement.executeQuery()){
				while(rs.next()) {
					entityId = rs.getInt(1);
				}
			}
		} catch (Exception e) {
			logger.info("Error in getEntityIdForLedgerId ", e); 
			throw new ApplicationException(e);
		}
		return entityId;
	}

	public boolean getMandatorySubLedgerForLedgerId(Integer ledgerId) throws ApplicationException {
		boolean mandatorySubledger =false;
		logger.info("To getMandatorySubLedgerForLedgerId ");
		try(final Connection con = getUserMgmConnection();final PreparedStatement preparedStatement = con.prepareStatement(ChartOfAccountsConstants.GET_MANDATORY_SUBLEDGER_FOR_LEDGER)){
			preparedStatement.setInt(1, ledgerId);
			try(ResultSet rs = preparedStatement.executeQuery()){
				while(rs.next()) {
					mandatorySubledger = rs.getBoolean(1);
					logger.info("To getMandatorySubLedgerForLedgerId return values is "+ mandatorySubledger);
				}
			}
		} catch (Exception e) {
			logger.info("Error in getEntityIdForLedgerId ", e); 
			throw new ApplicationException(e);
		}
		return mandatorySubledger;
	}


	public Map<String , Integer > updateLedger(LedgerVo ledgerVo) throws ApplicationException{
		logger.info("To updateLedger for Ledger id::" + ledgerVo.getId());
		Map <String , Integer > transactionStatusMap = new HashMap<String, Integer>();
		if(ledgerVo.getId()!=null) {
			try(final Connection con= getUserMgmConnection();final PreparedStatement preparedStatement = con.prepareStatement(ChartOfAccountsConstants.UPDATE_LEDGER_BY_ID)){
				String accountcodeToUpdate = null;
				String ledgerNameToUpdate = null;
				if(ledgerVo.getAccountCode()!=null && !ledgerVo.getAccountCode().isEmpty()) {
					String existingAccountCode = getAccountCode(ledgerVo.getId());
					if(existingAccountCode!=null && existingAccountCode.equals(ledgerVo.getAccountCode())) {
						accountcodeToUpdate = ledgerVo.getAccountCode();
					}else {
						Boolean isExistingAccountCode = isExistingAccountCode(ledgerVo.getAccountCode(),ledgerVo.getOrganizationId());
						if(isExistingAccountCode) {
							throw new ApplicationException("Account code already exist");
						}else {
							accountcodeToUpdate = ledgerVo.getAccountCode()!=null && !ledgerVo.getAccountCode().isEmpty() ? ledgerVo.getAccountCode() : null;
						}
					}
					logger.info("existingAccountCode to be updated is :: "+ accountcodeToUpdate);
				}
				/*if(ledgerVo.getModuleId()==null ||(ledgerVo.getModuleId()!=null && ledgerVo.getModuleId().equals(0))) {
					throw new ApplicationException("Module is Mandatory");
				}*/
				
				if( ledgerVo.getModuleId()==null){
					ledgerVo.setModuleId(0);
				}
				
				if(ledgerVo.getLedgerName()!=null && !ledgerVo.getLedgerName().isEmpty()) {
					String existingLedger = getLedgerName(ledgerVo.getId());
					if(existingLedger!=null && existingLedger.equals(ledgerVo.getLedgerName())) {
						ledgerNameToUpdate = ledgerVo.getLedgerName();
					}else {
						Boolean isExistingLedger = isExistingLedgerName(ledgerVo.getLedgerName(), ledgerVo.getOrganizationId(), ledgerVo.getAccountNameId());
						if(isExistingLedger) {
							throw new ApplicationException("Ledger Exist for the Organization");
						}else {
							ledgerNameToUpdate = ledgerVo.getLedgerName()!=null && !ledgerVo.getLedgerName().isEmpty() ? ledgerVo.getLedgerName() : null;
							journalEntriesTransactionDao.updateLedgerName(ledgerVo.getId(), ledgerNameToUpdate);
						}
					}
					logger.info("existingLedgerName to be updated is :: "+ ledgerNameToUpdate);
				}

				transactionStatusMap.put("existingEntityId", getEntityIdForLedgerId(ledgerVo.getId()));
				if( getMandatorySubLedgerForLedgerId(ledgerVo.getId())) {
					transactionStatusMap.put("existingMandatorySubledger", 1);
				}else {
					transactionStatusMap.put("existingMandatorySubledger", 0);
				}

				if(ledgerVo.getIsSubledgerMandatory()) {
					transactionStatusMap.put("currentMandatorySubledger", 1);
				}else {
					transactionStatusMap.put("currentMandatorySubledger", 0);
				}
				preparedStatement.setString(1, ledgerVo.getLedgerName()!=null ? ledgerVo.getLedgerName() : null);
				preparedStatement.setString(2,  ledgerNameToUpdate!=null ? ledgerNameToUpdate : null);
				preparedStatement.setBoolean(3, ledgerVo.getIsBase());
				preparedStatement.setString(4, ledgerVo.getLedgerStatus() ? CommonConstants.STATUS_AS_ACTIVE : CommonConstants.STATUS_AS_INACTIVE);
				preparedStatement.setInt(5, ledgerVo.getOrganizationId());
				preparedStatement.setInt(6, Integer.valueOf(ledgerVo.getUserId()));
				preparedStatement.setBoolean(7, ledgerVo.getIsSuperAdmin());
				preparedStatement.setInt(8, ledgerVo.getAccountNameId());
				preparedStatement.setString(9, accountcodeToUpdate);
				preparedStatement.setDouble(10, ledgerVo.getLedgerBalance() !=null  ? ledgerVo.getLedgerBalance() : 0.00);
				if(ledgerVo.getDateOfCreation()!=null && !ledgerVo.getDateOfCreation().isEmpty()) {
					String asOnDate = DateConverter.getInstance().correctDatePickerDateToString(ledgerVo.getDateOfCreation());
					logger.info("AsOnDAte::"+ asOnDate);
					if (asOnDate != null) {
					//	String date = DateConverter.getInstance().convertDateToGivenFormat(asOnDate, "yyyy-MM-dd");
						Date asonDate = Date.valueOf(asOnDate);
						logger.info("Converted Date is " +asonDate);
						preparedStatement.setDate(11,asonDate );
					}else {
						logger.info("converted poDate set as null");
						preparedStatement.setString(11, null);
					}
				}else {
					logger.info("converted poDate set as null");
					preparedStatement.setString(11, null);
				}
				preparedStatement.setBoolean(12, ledgerVo.getIsSubledgerMandatory());
				preparedStatement.setInt(13, ledgerVo.getModuleId());
				preparedStatement.setString(14,  ledgerVo.getLedgerName()!=null ? ledgerVo.getLedgerName() : null);
				preparedStatement.setInt(15, ledgerVo.getEntityId());
				preparedStatement.setDate(16, new Date(System.currentTimeMillis()));
				preparedStatement.setString(17, ledgerVo.getRoleName());
				preparedStatement.setInt(18, ledgerVo.getId());
				preparedStatement.executeUpdate();
				if(ledgerVo.getAttachments()!=null && ledgerVo.getAttachments().size()>0) {
					attachmentsDao.createAttachments(ledgerVo.getOrganizationId(),ledgerVo.getUserId(),ledgerVo.getAttachments(), AttachmentsConstants.MODULE_TYPE_CHART_OF_ACCOUNTS, ledgerVo.getId(),ledgerVo.getRoleName());
				}

				if(ledgerVo.getAttachmentsToRemove()!=null && ledgerVo.getAttachmentsToRemove().size()>0) {
					for(Integer attachmentId : ledgerVo.getAttachmentsToRemove()) {
						attachmentsDao.changeStatusForSingleAttachment(attachmentId, CommonConstants.STATUS_AS_DELETE,ledgerVo.getUserId(),ledgerVo.getRoleName());  
					}
				}				
				transactionStatusMap.put("UpdateTxnSuccess", 1);
				logger.info("updateLedger Txn Map"+transactionStatusMap.toString());
			}catch (Exception e) {
				logger.info("Error in updateLedger ", e); 
				throw new ApplicationException(e.getMessage());
			}
		}
		return transactionStatusMap;
	}

	public void deleteSubledgerForLedgerId(Integer ledgerId) throws ApplicationException{
		logger.info("deleteSubledgerForLedgerId" );
		try(Connection con = getUserMgmConnection() ; PreparedStatement preparedStatement = con.prepareStatement(ChartOfAccountsConstants.DELETE_SUB_LEDGER_BY_LEDGER_ID)){
			preparedStatement.setInt(1, ledgerId);
			preparedStatement.execute();
			logger.info(" Success in deleteSubledgerForLedgerId" );
		}catch (Exception e) {
			logger.info("Error in deleteSubledgerForLedgerId ", e); 
			throw new ApplicationException(e.getMessage());
		}
	}


	public List<LedgerListVo> getLedgerAndSubLedgerList(Integer orgId, String filterValue,String userId,String roleName) throws ApplicationException {
		List<LedgerListVo> ledgerListVos = new ArrayList<LedgerListVo>();
		logger.info("getLedgerAndSubLedgerList");
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;

		try {
			StringBuilder filterQuery =null;
			if(roleName.equals("Super Admin")){
				filterQuery = new StringBuilder(ChartOfAccountsConstants.GET_ALL_LEDGERS_ORGANIZATION);
			}else{
				filterQuery = new StringBuilder(ChartOfAccountsConstants.GET_ALL_LEDGERS_USER_ROLE);
			}
			
			ArrayList<Object> paramsList = new ArrayList<>();
			paramsList.add(orgId);
			if(!(roleName.equals("Super Admin"))){
				paramsList.add(userId);
				paramsList.add(roleName);
			}	
			
			if (filterValue != null && !"0".equals(filterValue)) {
				if ("ACT".equalsIgnoreCase(filterValue)) {
					filterQuery.append("and l5.status = ? ");
					paramsList.add("ACT");
				} else if ("INA".equalsIgnoreCase(filterValue)) {
					filterQuery.append("and l5.status = ? ");
					paramsList.add("INA");
				} else {
					filterQuery.append("and l2.id = ? ");
					paramsList.add(Integer.valueOf(filterValue));
				}
			}
			logger.info(filterQuery.toString());
			logger.info(paramsList);
			connection = getUserMgmConnection();
			preparedStatement = connection.prepareStatement(filterQuery.toString());
			int counter = 1;
			for (int i = 0; i < paramsList.size(); i++) {
				logger.info(counter);
				preparedStatement.setObject(counter, paramsList.get(i));
				counter++;
			}
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				LedgerListVo ledgerListVo = new LedgerListVo();
				ledgerListVo.setLedgerId(rs.getInt(1));
				ledgerListVo.setLedgerName(rs.getString(2));
				ledgerListVo.setLedgerStatus(rs.getString(3));
				ledgerListVo.setLevel4Name(rs.getString(4));
				ledgerListVo.setLevel3Name(rs.getString(5));
				ledgerListVo.setLevel2Name(rs.getString(6));
				ledgerListVo.setLevel1Name(rs.getString(7));
				ledgerListVo.setIsBase(rs.getBoolean(8));
				ledgerListVos.add(ledgerListVo);
			}
			if (ledgerListVos != null && ledgerListVos.size() > 0) {
				for (LedgerListVo ledgerListVo : ledgerListVos) {
					List<ChartOfAccountsLevel6Vo> accountsLevel6Vos = chartOfAccountsDao.getChartOfAccountsLevel6ByLevel5Id(ledgerListVo.getLedgerId(), orgId, connection);
					ledgerListVo.setSubledgers(accountsLevel6Vos);
				}
			}
			logger.info("Successfully getLedgerAndSubLedgerList :" + ledgerListVos.size());
		} catch (Exception e) {
			logger.info("Error in getLedgerAndSubLedgerList ", e);
			throw new ApplicationException(e.getMessage());
		}finally {
			closeResources(rs, preparedStatement, connection);
		}
		return ledgerListVos;
	}

	public List<LedgerFilterDropDownVo> getAccountTypeForFilterDropdown(Integer orgId) throws ApplicationException{
		List<LedgerFilterDropDownVo> accountTypeVos = new ArrayList<LedgerFilterDropDownVo>();
		LedgerFilterDropDownVo allAccountsVo = new LedgerFilterDropDownVo();
		allAccountsVo.setId(1);
		allAccountsVo.setLevel("L2");
		allAccountsVo.setName("All Accounts");
		allAccountsVo.setValue("0");
		accountTypeVos.add(allAccountsVo);
		LedgerFilterDropDownVo activeAccountsVo = new LedgerFilterDropDownVo();
		activeAccountsVo.setId(1);
		activeAccountsVo.setLevel("L2");
		activeAccountsVo.setName("Active Accounts");
		activeAccountsVo.setValue("ACT");
		accountTypeVos.add(activeAccountsVo);
		LedgerFilterDropDownVo inActiveAccountsVo = new LedgerFilterDropDownVo(); 
		inActiveAccountsVo.setId(1);
		inActiveAccountsVo.setLevel("L2");
		inActiveAccountsVo.setName("Inactive Accounts");
		inActiveAccountsVo.setValue("INA");
		accountTypeVos.add(inActiveAccountsVo);
		Connection con = getUserMgmConnection();
		List<MinimalChartOfAccountsLevel2Vo> accountsLevel2Vos = chartOfAccountsDao.getAccountTypesByOrgId(orgId, con);
		accountsLevel2Vos.forEach(accType ->{
			LedgerFilterDropDownVo accTypeVo = new LedgerFilterDropDownVo();
			accTypeVo.setId(accType.getId());
			accTypeVo.setLevel("L2");
			accTypeVo.setName(accType.getName());
			accTypeVo.setValue(String.valueOf(accType.getId()));
			accountTypeVos.add(accTypeVo);
		});
		return accountTypeVos;
	}
}

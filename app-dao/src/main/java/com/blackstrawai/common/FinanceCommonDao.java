package com.blackstrawai.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.accounting.AccountingAspectsTypeVo;
import com.blackstrawai.ap.dropdowns.BasicCurrencyVo;
import com.blackstrawai.ap.dropdowns.BasicEmployeeTypeVo;
import com.blackstrawai.ap.dropdowns.BasicUnitOfMeasureVo;
import com.blackstrawai.ap.dropdowns.DiscountTypeVo;
import com.blackstrawai.ap.dropdowns.GSTTreatmentVo;
import com.blackstrawai.ap.dropdowns.ItemTaxRateVo;
import com.blackstrawai.ap.dropdowns.OrganizationConstitutionDropDownVo;
import com.blackstrawai.ap.dropdowns.PaymentTypeVo;
import com.blackstrawai.ap.dropdowns.PurchaseOrderTypeVo;
import com.blackstrawai.ap.dropdowns.ShippingMethodVo;
import com.blackstrawai.ap.dropdowns.SourceOfSupplyVo;
import com.blackstrawai.ap.dropdowns.TDSVo;
import com.blackstrawai.ar.dropdowns.PaymentModeVo;
import com.blackstrawai.ar.dropdowns.ReceiptTypeVo;
import com.blackstrawai.banking.dashboard.BankMasterAccountTypeVo;
import com.blackstrawai.banking.dashboard.BankMasterAccountVariantVo;
import com.blackstrawai.onboarding.organization.OrganizationConstitutionVo;
import com.blackstrawai.onboarding.organization.OrganizationIndustryVo;
import com.blackstrawai.onboarding.organization.OrganizationTypeVo;
import com.blackstrawai.payroll.PayItemVo;
import com.blackstrawai.payroll.PayTypeVo;
import com.blackstrawai.report.ReportPeriodVo;
import com.blackstrawai.settings.BaseCurrencyVo;
import com.blackstrawai.settings.CommonVo;
import com.blackstrawai.settings.PaymentTermsVo;
import com.blackstrawai.settings.TaxGroupVo;
import com.blackstrawai.settings.TaxRateMappingVo;
import com.blackstrawai.settings.TaxRateTypeVo;
import com.blackstrawai.settings.UnitOfMeasureVo;
import com.blackstrawai.settings.VoucherTypeVo;
import com.blackstrawai.settings.chartofaccounts.ChartOfAccountsEntityVo;
import com.blackstrawai.settings.chartofaccounts.ChartOfAccountsModuleVo;
import com.blackstrawai.upload.ModuleTypeVo;
import com.blackstrawai.vendorsettings.BaseGeneralSettingsVo;
import com.blackstrawai.vendorsettings.SettingsModuleVo;
import com.blackstrawai.vendorsettings.SettingsTemplateVo;
import com.blackstrawai.vendorsettings.SettingsValidationVo;
import com.blackstrawai.workflow.WorkflowCommonVo;
import com.blackstrawai.workflow.WorkflowRuleChoiceVo;
import com.blackstrawai.workflow.WorkflowRuleConditionVo;

@Repository
public class FinanceCommonDao extends BaseDao {

	private Logger logger = Logger.getLogger(FinanceCommonDao.class);

	public List<ItemTaxRateVo> getItemTaxRate(Connection con) throws ApplicationException {
		logger.info("Entry into method:getItemTaxRate");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		List<ItemTaxRateVo> itemTaxtRateList = new ArrayList<ItemTaxRateVo>();
		try {
			String query = FinanceCommonConstants.ITEM_TAX_RATE;
			preparedStatement = con.prepareStatement(query);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				ItemTaxRateVo data = new ItemTaxRateVo();
				data.setId(rs.getInt(1));
				data.setName(rs.getString(2));
				data.setDescription(rs.getString(3));
				data.setValue(rs.getInt(1));
				itemTaxtRateList.add(data);
			}
		} catch (Exception e) {

			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, null);

		}
		return itemTaxtRateList;

	}

	public List<DiscountTypeVo> getDiscountType(Connection con) throws ApplicationException {
		logger.info("Entry into method:getDiscountType");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		List<DiscountTypeVo> discountType = new ArrayList<DiscountTypeVo>();
		try {
			String query = FinanceCommonConstants.DISCOUNT_TYPE;
			preparedStatement = con.prepareStatement(query);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				DiscountTypeVo data = new DiscountTypeVo();
				data.setId(rs.getInt(1));
				data.setName(rs.getString(2));
				data.setDescription(rs.getString(3));
				data.setValue(rs.getInt(1));
				discountType.add(data);
			}
		} catch (Exception e) {

			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, null);

		}
		return discountType;

	}

	public Map<Integer, String> getDiscountType() throws ApplicationException{
		Connection con = getFinanceCommon();
		Map<Integer, String> discountTypes = getDiscountType(con).stream().collect(Collectors.toMap(DiscountTypeVo::getId, DiscountTypeVo::getName));;
		closeResources(null, null, con);
		return discountTypes;
	}

	public List<PurchaseOrderTypeVo> getPurchaseOrderType(Connection con) throws ApplicationException {
		logger.info("Entry into method:getPurchaseOrderType");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		List<PurchaseOrderTypeVo> purchaseOrderType = new ArrayList<PurchaseOrderTypeVo>();
		try {
			String query = FinanceCommonConstants.PURCHASE_ORDER_TYPE;
			preparedStatement = con.prepareStatement(query);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				PurchaseOrderTypeVo data = new PurchaseOrderTypeVo();
				data.setId(rs.getInt(1));
				data.setName(rs.getString(2));
				data.setDescription(rs.getString(3));
				purchaseOrderType.add(data);
			}
		} catch (Exception e) {

			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, null);

		}
		return purchaseOrderType;

	}

	public List<ShippingMethodVo> getShippingMethod(Connection con) throws ApplicationException {
		logger.info("Entry into method:getShippingMethod");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		List<ShippingMethodVo> shippingMethod = new ArrayList<ShippingMethodVo>();
		try {
			String query = FinanceCommonConstants.SHIPPING_METHOD;
			preparedStatement = con.prepareStatement(query);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				ShippingMethodVo data = new ShippingMethodVo();
				data.setId(rs.getInt(1));
				data.setName(rs.getString(2));
				data.setDescription(rs.getString(3));
				shippingMethod.add(data);
			}
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, null);

		}
		return shippingMethod;

	}

	public List<GSTTreatmentVo> gstTreatment(Connection con) throws ApplicationException {
		logger.info("Entry into method:gstTreatment");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		List<GSTTreatmentVo> gst = new ArrayList<GSTTreatmentVo>();
		try {
			String query = FinanceCommonConstants.GST_TREATMENT;
			preparedStatement = con.prepareStatement(query);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				GSTTreatmentVo data = new GSTTreatmentVo();
				data.setId(rs.getInt(1));
				data.setName(rs.getString(2));
				data.setDescription(rs.getString(3));
				gst.add(data);
			}
		} catch (Exception e) {

			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, null);

		}
		return gst;

	}

	public List<TDSVo> getTDS(Connection con) throws ApplicationException {
		logger.info("Entry into method:getTDS");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		List<TDSVo> gst = new ArrayList<TDSVo>();
		try {
			String query = FinanceCommonConstants.TDS;
			preparedStatement = con.prepareStatement(query);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				TDSVo data = new TDSVo();
				data.setId(rs.getInt(1));
				data.setName(rs.getString(2));
				data.setTdsRateIdentifier(rs.getString(3));
				data.setTdsRatePercentage(rs.getString(4));
				data.setValue(rs.getInt(1));
				gst.add(data);
			}
		} catch (Exception e) {

			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, null);

		}
		return gst;

	}
	
	
	public List<TDSVo> getFullTDS(Connection con) throws ApplicationException {
		logger.info("Entry into method:getFullTDS");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		List<TDSVo> gst = new ArrayList<TDSVo>();
		try {
			String query = FinanceCommonConstants.FULL_TDS;
			preparedStatement = con.prepareStatement(query);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				TDSVo data = new TDSVo();
				data.setId(rs.getInt(1));
				data.setName(rs.getString(2));
				data.setTdsRateIdentifier(rs.getString(3));
				data.setTdsRatePercentage(rs.getString(4));
				data.setValue(rs.getInt(1));
				data.setDescription(rs.getString(5));
				data.setApplicableFor(rs.getString(6));
				gst.add(data);
			}
		} catch (Exception e) {

			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, null);

		}
		return gst;

	}

	public List<SourceOfSupplyVo> sourceOfSupply(Connection con) throws ApplicationException {
		logger.info("Entry into method:sourceOfSupply");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		List<SourceOfSupplyVo> sourceOfSupply = new ArrayList<SourceOfSupplyVo>();
		try {
			String query = FinanceCommonConstants.SOURCE_OF_SUPPLY;
			preparedStatement = con.prepareStatement(query);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				SourceOfSupplyVo data = new SourceOfSupplyVo();
				data.setId(rs.getInt(1));
				data.setStateName(rs.getString(2));
				data.setStateCode(rs.getString(3));
				data.setName(rs.getString(2));
				data.setValue(rs.getInt(1));
				sourceOfSupply.add(data);
			}
		} catch (Exception e) {

			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, null);

		}
		return sourceOfSupply;

	}

	public List<OrganizationConstitutionDropDownVo> getorganizationConstitution(Connection con)
			throws ApplicationException {
		logger.info("Entry into method: getorganizationConstitution");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		List<OrganizationConstitutionDropDownVo> organizationTypes = new ArrayList<OrganizationConstitutionDropDownVo>();
		try {
			String query = FinanceCommonConstants.GET_ORGANIZATION_CONSTITUTION;
			preparedStatement = con.prepareStatement(query);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				OrganizationConstitutionDropDownVo data = new OrganizationConstitutionDropDownVo();
				data.setId(rs.getInt(1));
				data.setName(rs.getString(2));
				organizationTypes.add(data);
			}
		} catch (Exception e) {

			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, null);

		}
		return organizationTypes;
	}
	
	
	
	public String getUnitOfMeasureById(Integer id) throws ApplicationException {
		logger.info("Entry into method: getUnitOfMeasure");
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		String data = null;
		try {
			con = getFinanceCommon();
			preparedStatement = con.prepareStatement(FinanceCommonConstants.GET_UNIT_OF_MEASURE_BY_ID);
			preparedStatement.setInt(1, id);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				data = rs.getString(1);
			}
		} catch (Exception e) {
			logger.info("Error in UnitMeasure Fetch ",e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);

		}
		return data;
	}
	
	public List<UnitOfMeasureVo> getUnitOfMeasure(Connection con) throws ApplicationException {
		logger.info("Entry into method: getUnitOfMeasure");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		List<UnitOfMeasureVo> data = new ArrayList<UnitOfMeasureVo>();
		try {
			String query = FinanceCommonConstants.GET_UNIT_OF_MEASURE;
			preparedStatement = con.prepareStatement(query);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				UnitOfMeasureVo unitOfMeasureVo = new UnitOfMeasureVo();
				unitOfMeasureVo.setId(rs.getInt(1));
				unitOfMeasureVo.setQuantity(rs.getString(2));
				unitOfMeasureVo.setUnitName(rs.getString(3));
				unitOfMeasureVo.setSymbol(rs.getString(4));
				data.add(unitOfMeasureVo);
			}
		} catch (Exception e) {

			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, null);

		}
		return data;
	}

	public List<BasicEmployeeTypeVo> getEmployeeType(Connection con) throws ApplicationException {
		logger.info("Entry into method: getEmployeeType");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		List<BasicEmployeeTypeVo> employeeTypes = new ArrayList<BasicEmployeeTypeVo>();
		try {
			String query = FinanceCommonConstants.GET_EMPLOYEE_TYPE;
			preparedStatement = con.prepareStatement(query);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				BasicEmployeeTypeVo data = new BasicEmployeeTypeVo();
				data.setId(rs.getInt(1));
				data.setName(rs.getString(2));
				employeeTypes.add(data);
			}
		} catch (Exception e) {

			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, null);
		}
		return employeeTypes;
	}

	/*
	 * public List<BaseChartOfAccountsVo> getBaseChartOfAccounts()throws
	 * ApplicationException{
	 * logger.info("Entry into method: getBaseChartOfAccounts"); PreparedStatement
	 * preparedStatement = null; ResultSet rs = null; List<BaseChartOfAccountsVo>
	 * baseChartOfAccountsVoList=new ArrayList<BaseChartOfAccountsVo>(); Connection
	 * con=getFinanceCommon(); try { String
	 * query=SettingsAndPreferencesConstants.GET_BASE_CHART_OF_ACCOUNTS;
	 * preparedStatement=con.prepareStatement(query); rs =
	 * preparedStatement.executeQuery(); while (rs.next()) { BaseChartOfAccountsVo
	 * data=new BaseChartOfAccountsVo(); data.setLevel1(rs.getString(1));
	 * data.setLevel2(rs.getString(2)); data.setLevel3(rs.getString(3));
	 * data.setLevel4(rs.getString(4)); data.setLevel5(rs.getString(5));
	 * data.setLevel6(rs.getString(6)); data.setAccountCode(rs.getString(7));
	 * data.setModule(rs.getString(8)); data.setAccountingEntries(rs.getString(9));
	 * data.setMandatorySubLedger(rs.getString(10));
	 * data.setCompanyType(rs.getString(11)); baseChartOfAccountsVoList.add(data); }
	 * }catch(Exception e){
	 * 
	 * throw new ApplicationException(e); }finally { closeResources(rs,
	 * preparedStatement, con); } return baseChartOfAccountsVoList; }
	 */

	@SuppressWarnings("rawtypes")
	public List<CountryVo> getCountryAndStateList() throws ApplicationException {
		logger.info("Entry into method:getCountryAndStateList");
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		List<CountryVo> data = new ArrayList<CountryVo>();
		Map<String, CountryVo> tempSet = new HashMap<String, CountryVo>();
		try {
			con = getFinanceCommon();

			String query = FinanceCommonConstants.GET_COUNTTRY_STATE;
			preparedStatement = con.prepareStatement(query);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				int countryId = rs.getInt(1);
				String countryName = rs.getString(2);
				int stateId = rs.getInt(3);
				String stateName = rs.getString(4);
				if ((tempSet.containsKey(countryName))) {
					CountryVo countryVo = tempSet.get(countryName);
					StateVo stateVo = new StateVo();
					stateVo.setId(stateId);
					stateVo.setName(stateName);
					countryVo.getState().add(stateVo);
				} else {
					CountryVo countryVo = new CountryVo();
					countryVo.setId(countryId);
					countryVo.setName(countryName);
					StateVo stateVo = new StateVo();
					stateVo.setId(rs.getInt(3));
					stateVo.setName(rs.getString(4));
					countryVo.getState().add(stateVo);
					tempSet.put(countryVo.getName(), countryVo);
				}

			}
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);

		}
		Iterator it = tempSet.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			data.add((CountryVo) entry.getValue());
		}
		tempSet.clear();
		return data;

	}

	@SuppressWarnings("rawtypes")
	public List<CountryVo> getCountryAndStateList(Connection con) throws ApplicationException {
		logger.info("Entry into method:getCountryAndStateList");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		List<CountryVo> data = new ArrayList<CountryVo>();
		Map<String, CountryVo> tempSet = new HashMap<String, CountryVo>();
		try {
			String query = FinanceCommonConstants.GET_COUNTTRY_STATE;
			preparedStatement = con.prepareStatement(query);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				int countryId = rs.getInt(1);
				String countryName = rs.getString(2);
				int stateId = rs.getInt(3);
				String stateName = rs.getString(4);
				if ((tempSet.containsKey(countryName))) {
					CountryVo countryVo = tempSet.get(countryName);
					StateVo stateVo = new StateVo();
					stateVo.setId(stateId);
					stateVo.setName(stateName);
					countryVo.getState().add(stateVo);
				} else {
					CountryVo countryVo = new CountryVo();
					countryVo.setId(countryId);
					countryVo.setName(countryName);
					StateVo stateVo = new StateVo();
					stateVo.setId(rs.getInt(3));
					stateVo.setName(rs.getString(4));
					countryVo.getState().add(stateVo);
					tempSet.put(countryVo.getName(), countryVo);
				}

			}
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, null);

		}
		Iterator it = tempSet.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			data.add((CountryVo) entry.getValue());
		}
		tempSet.clear();
		return data;

	}

	public List<BasicUnitOfMeasureVo> getBasicUnitOfMeasure(Connection con) throws ApplicationException {
		logger.info("To get  getBasicUnitOfMeasure");
		List<BasicUnitOfMeasureVo> basicUnitOfMeasureVos = null;
		PreparedStatement preparedStatement =null;
		ResultSet rs=null;
		try {
			preparedStatement = con.prepareStatement(FinanceCommonConstants.GET_MINIMAL_UNIT_OF_MEASURE);
			rs = preparedStatement.executeQuery();
			basicUnitOfMeasureVos = new ArrayList<BasicUnitOfMeasureVo>();
			while (rs.next()) {
				BasicUnitOfMeasureVo unitOfMeasureVo = new BasicUnitOfMeasureVo();
				unitOfMeasureVo.setValue(rs.getInt(1));
				unitOfMeasureVo.setName(rs.getString(2));
				unitOfMeasureVo.setQuantityType(rs.getString(3));
				basicUnitOfMeasureVos.add(unitOfMeasureVo);
			}
		} catch (Exception e) {
			logger.info("Error in  getBasicUnitOfMeasure", e);
			throw new ApplicationException(e);
		}
		finally {
			closeResources(rs, preparedStatement, null);

		}


		return basicUnitOfMeasureVos;

	}

	public List<AccountingAspectsTypeVo> getAccountingAspectsTypes(Connection con) throws ApplicationException {
		logger.info("Entry into method: getAccountingTypes");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		List<AccountingAspectsTypeVo> accountingTypes = new ArrayList<AccountingAspectsTypeVo>();
		try {
			String query = FinanceCommonConstants.GET_ACCOUNTING_ASPECTS_TYPE;
			preparedStatement = con.prepareStatement(query);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				AccountingAspectsTypeVo data = new AccountingAspectsTypeVo();
				data.setId(rs.getInt(1));
				data.setName(rs.getString(2));
				data.setValue(data.getId());
				accountingTypes.add(data);
			}
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, null);
		}
		return accountingTypes;
	}

	public Map<String, String> getLevel1ChartOfAccounts() throws ApplicationException {
		logger.info("Entry into getLevel1ChartOfAccounts");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = getFinanceCommon();
		Map<String, String> data = new LinkedHashMap<String, String>();
		try {
			String query = FinanceCommonConstants.GET_LEVEL1_CHART_OF_ACCOUNTS;
			preparedStatement = con.prepareStatement(query);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				rs.getInt(1);
				String name = rs.getString(2);
				data.put(name, name);
			}
		} catch (Exception e) {
			throw new ApplicationException(e);

		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return data;

	}

	public Map<String, String> getLevel2ChartOfAccounts() throws ApplicationException {
		logger.info("Entry into getLevel2ChartOfAccounts");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = getFinanceCommon();
		Map<String, String> data = new LinkedHashMap<String, String>();
		try {
			String query = FinanceCommonConstants.GET_LEVEL2_CHART_OF_ACCOUNTS;
			preparedStatement = con.prepareStatement(query);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				rs.getInt(1);
				String level2Name = rs.getString(2);
				String level1Name = rs.getString(3);
				data.put(level2Name, level1Name);
			}
		} catch (Exception e) {
			throw new ApplicationException(e);

		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return data;

	}

	public Map<String, String> getLevel3ChartOfAccounts() throws ApplicationException {
		logger.info("Entry into getLevel3ChartOfAccounts");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = getFinanceCommon();
		Map<String, String> data = new LinkedHashMap<String, String>();
		try {
			String query = FinanceCommonConstants.GET_LEVEL3_CHART_OF_ACCOUNTS;
			preparedStatement = con.prepareStatement(query);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				rs.getInt(1);
				String level3Name = rs.getString(2);
				String level2Name = rs.getString(3);
				data.put(level3Name, level2Name);
			}
		} catch (Exception e) {
			throw new ApplicationException(e);

		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return data;

	}

	public Map<String, String> getLevel4ChartOfAccounts() throws ApplicationException {
		logger.info("Entry into getLevel4ChartOfAccounts");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = getFinanceCommon();
		Map<String, String> data = new LinkedHashMap<String, String>();
		try {
			String query = FinanceCommonConstants.GET_LEVEL4_CHART_OF_ACCOUNTS;
			preparedStatement = con.prepareStatement(query);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				int id = rs.getInt(1);
				String level4Name = rs.getString(2);
				String level3Name = rs.getString(3);
				data.put(level4Name + "~@" + id, level3Name);
			}
		} catch (Exception e) {
			throw new ApplicationException(e);

		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return data;

	}

	public Map<String, String> getLevel5ChartOfAccounts() throws ApplicationException {
		logger.info("Entry into getLevel5ChartOfAccounts");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = getFinanceCommon();
		Map<String, String> data = new LinkedHashMap<String, String>();
		try {
			String query = FinanceCommonConstants.GET_LEVEL5_CHART_OF_ACCOUNTS;
			preparedStatement = con.prepareStatement(query);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				int id = rs.getInt(1);
				String level5Name = rs.getString(2);
				String level4Name = rs.getString(3);
				int level4Id = rs.getInt(4);
				data.put(level5Name + "~@" + id, level4Name + "~@" + level4Id);
			}
		} catch (Exception e) {
			throw new ApplicationException(e);

		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return data;

	}

	public List<BasicCurrencyVo> getMinimalBasicCurrency() throws ApplicationException {
		logger.info("Entry into method:getBasicCurrency");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		List<BasicCurrencyVo> currencyList = new ArrayList<BasicCurrencyVo>();
		Connection con = getFinanceCommon();
		try {
			String query = FinanceCommonConstants.GET_MINIMAL_BASE_CURRENCY;
			preparedStatement = con.prepareStatement(query);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				BasicCurrencyVo data = new BasicCurrencyVo();
				data.setId(rs.getInt(1));
				data.setName(rs.getString(2));
				currencyList.add(data);
			}
		} catch (Exception e) {

			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);

		}
		return currencyList;

	}

	public List<BaseCurrencyVo> getBasicBaseCurrency() throws ApplicationException {
		logger.info("Entry into method:getBasicBaseCurrency");
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		List<BaseCurrencyVo> data = new ArrayList<BaseCurrencyVo>();
		try {
			con = getFinanceCommon();
			String query = FinanceCommonConstants.GET_ALL_BASE_CURRENCY;
			preparedStatement = con.prepareStatement(query);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {

				BaseCurrencyVo baseCurrencyVo = new BaseCurrencyVo();
				baseCurrencyVo.setId(rs.getInt(1));
				baseCurrencyVo.setName(rs.getString(2));
				baseCurrencyVo.setDescription(rs.getString(3));
				baseCurrencyVo.setSymbol(rs.getString(4));
				baseCurrencyVo.setAlternateSymbol(rs.getString(5));
				baseCurrencyVo.setSpaceRequired(rs.getBoolean(6));
				baseCurrencyVo.setMillions(rs.getBoolean(7));
				baseCurrencyVo.setNumberOfDecimalPlaces(rs.getInt(8));
				baseCurrencyVo.setDecimalValueDenoter(rs.getString(9));
				baseCurrencyVo.setNoOfDecimalsForAmoutInWords(rs.getInt(10));
				baseCurrencyVo.setExchangeValue(rs.getString(11));
				baseCurrencyVo.setValueFormat(rs.getString(12));
				data.add(baseCurrencyVo);

			}
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);

		}
		return data;

	}

	public List<PaymentTermsVo> getAllBasicPaymentTerms() throws ApplicationException {
		logger.info("Entry into method: getAllBasicPaymentTerms");
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		List<PaymentTermsVo> listPaymentTerms = new ArrayList<PaymentTermsVo>();
		try {
			con = getFinanceCommon();
			String query = FinanceCommonConstants.GET_BASE_PAYMENT_TERMS;
			preparedStatement = con.prepareStatement(query);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				PaymentTermsVo paymentTermsVo = new PaymentTermsVo();
				paymentTermsVo.setId(rs.getInt(1));
				paymentTermsVo.setPaymentTermsName(rs.getString(2));
				paymentTermsVo.setDescription(rs.getString(3));
				paymentTermsVo.setBaseDate(rs.getString(4));
				paymentTermsVo.setDaysLimit(rs.getInt(5));
				List<String> accountTypes = new ArrayList<String>();
				String types[] = rs.getString(6).split(",");
				for (String str : types) {
					accountTypes.add(str);
				}
				paymentTermsVo.setAccountType(accountTypes);
				listPaymentTerms.add(paymentTermsVo);
			}
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return listPaymentTerms;
	}

	public List<OrganizationTypeVo> getBasicOrganizationType() throws ApplicationException {
		logger.info("Entry into method:getBasicOrganizationType");
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		List<OrganizationTypeVo> data = new ArrayList<OrganizationTypeVo>();

		try {
			con = getFinanceCommon();
			String query = FinanceCommonConstants.GET_ALL_ORGANIZATION_TYPE;
			preparedStatement = con.prepareStatement(query);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				OrganizationTypeVo organizationTypeVo = new OrganizationTypeVo();
				organizationTypeVo.setId(rs.getInt(1));
				organizationTypeVo.setName(rs.getString(2));
				organizationTypeVo.setDescription(rs.getString(3));
				data.add(organizationTypeVo);
			}
			closeResources(rs, preparedStatement, null);
			for (int i = 0; i < data.size(); i++) {
				OrganizationTypeVo organizationTypeVo = data.get(i);
				query = FinanceCommonConstants.GET_ORGANIZATION_DIVISION;
				preparedStatement = con.prepareStatement(query);
				preparedStatement.setInt(1, organizationTypeVo.getId());
				rs = preparedStatement.executeQuery();
				List<OrganizationIndustryVo> organizationIndustry = new ArrayList<OrganizationIndustryVo>();
				organizationIndustry.clear();
				while (rs.next()) {
					OrganizationIndustryVo organizationIndustryVo = new OrganizationIndustryVo();
					organizationIndustryVo.setId(rs.getInt(1));
					organizationIndustryVo.setName(rs.getString(2));
					organizationIndustryVo.setDescription(rs.getString(3));
					organizationIndustry.add(organizationIndustryVo);
				}
				organizationTypeVo.setOrganizationIndustry(organizationIndustry);
			}

		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);

		}
		return data;

	}

	public List<OrganizationConstitutionVo> getBasicOrganizationConstitution() throws ApplicationException {
		logger.info("Entry into method:getBasicOrganizationConstitution");
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		List<OrganizationConstitutionVo> data = new ArrayList<OrganizationConstitutionVo>();
		try {
			con = getFinanceCommon();
			String query = FinanceCommonConstants.GET_ALL_ORGANIZATION_CONSTITUTION;
			preparedStatement = con.prepareStatement(query);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				OrganizationConstitutionVo organizationConstitutionVo = new OrganizationConstitutionVo();
				organizationConstitutionVo.setId(rs.getInt(1));
				organizationConstitutionVo.setName(rs.getString(2));
				organizationConstitutionVo.setDescription(rs.getString(3));
				data.add(organizationConstitutionVo);

			}
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);

		}
		return data;

	}

	public BaseCurrencyVo getBaseCurrencyById(int id) throws ApplicationException {
		logger.info("Entry into method: getBaseCurrencyById");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		BaseCurrencyVo baseCurrencyVo = new BaseCurrencyVo();
		try {
			con = getFinanceCommon();
			String query = FinanceCommonConstants.GET_BASE_CURRENCY_BY_ID;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, id);
			rs = preparedStatement.executeQuery();

			while (rs.next()) {
				baseCurrencyVo.setId(rs.getInt(1));
				baseCurrencyVo.setName(rs.getString(2));
				baseCurrencyVo.setDescription(rs.getString(3));
				baseCurrencyVo.setSymbol(rs.getString(4));
				baseCurrencyVo.setAlternateSymbol(rs.getString(5));
				baseCurrencyVo.setSpaceRequired(rs.getBoolean(6));
				baseCurrencyVo.setMillions(rs.getBoolean(7));
				baseCurrencyVo.setNumberOfDecimalPlaces(rs.getInt(8));
				baseCurrencyVo.setDecimalValueDenoter(rs.getString(9));
				baseCurrencyVo.setNoOfDecimalsForAmoutInWords(rs.getInt(10));
				baseCurrencyVo.setExchangeValue(rs.getString(11));
				baseCurrencyVo.setValueFormat(rs.getString(12));
				baseCurrencyVo.setCreateTs(rs.getTimestamp(13));
				baseCurrencyVo.setUpdateTs(rs.getTimestamp(14));
			}
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return baseCurrencyVo;
	}

	public List<TaxRateTypeVo> getBaseTaxRate() throws ApplicationException {
		logger.info("Entry into method: getBaseTaxRate");
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		List<TaxRateTypeVo> data = new ArrayList<TaxRateTypeVo>();
		try {
			con = getFinanceCommon();
			String query = FinanceCommonConstants.GET_BASE_TAX_RATE_TYPE;
			preparedStatement = con.prepareStatement(query);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				TaxRateTypeVo taxRateTypeVo = new TaxRateTypeVo();
				taxRateTypeVo.setType(rs.getString(1));
				taxRateTypeVo.setUsageType(rs.getString(2));
				taxRateTypeVo.setIsInter(rs.getString(3));
				data.add(taxRateTypeVo);
			}
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return data;
	}

	public List<TaxRateMappingVo> getBaseTaxRateMapping() throws ApplicationException {
		logger.info("Entry into method: getBaseTaxRateMapping");
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		List<TaxRateMappingVo> data = new ArrayList<TaxRateMappingVo>();
		try {
			con = getFinanceCommon();
			String query = FinanceCommonConstants.GET_BASE_TAX_RATE_MAPPING;
			preparedStatement = con.prepareStatement(query);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				TaxRateMappingVo taxRateMappingVo = new TaxRateMappingVo();
				taxRateMappingVo.setName(rs.getString(1));
				taxRateMappingVo.setTaxRateTypeId(rs.getInt(2));
				taxRateMappingVo.setRate(rs.getString(3));
				taxRateMappingVo.setTaxRateTypeName(rs.getString(4));
				data.add(taxRateMappingVo);
			}
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return data;
	}

	public List<TaxGroupVo> getBaseTaxGroup() throws ApplicationException {
		logger.info("Entry into method: getBaseTaxGroup");
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		List<TaxGroupVo> data = new ArrayList<TaxGroupVo>();
		try {
			con = getFinanceCommon();
			String query = FinanceCommonConstants.GET_BASE_TAX_GROUP;
			preparedStatement = con.prepareStatement(query);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				TaxGroupVo taxGroupVo = new TaxGroupVo();
				taxGroupVo.setName(rs.getString(1));
				taxGroupVo.setTaxesIncluded(rs.getString(2));
				taxGroupVo.setCombinedRate(rs.getString(3));
				data.add(taxGroupVo);
			}
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return data;
	}

	public List<BankMasterAccountTypeVo> getAccountType(Connection con) throws ApplicationException {
		logger.info("Entry into method: getAccountType");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		List<BankMasterAccountTypeVo> data = new ArrayList<BankMasterAccountTypeVo>();
		try {
			String query = FinanceCommonConstants.GET_BANK_ACCOUNT_TYPE;
			preparedStatement = con.prepareStatement(query);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				BankMasterAccountTypeVo bankMasterAccountTypeVo = new BankMasterAccountTypeVo();
				bankMasterAccountTypeVo.setId(rs.getInt(1));
				bankMasterAccountTypeVo.setName(rs.getString(2));
				data.add(bankMasterAccountTypeVo);
			}
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, null);
		}
		return data;
	}

	public List<BankMasterAccountVariantVo> getAccountVariant(Connection con) throws ApplicationException {
		logger.info("Entry into method: getAccountType");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		List<BankMasterAccountVariantVo> data = new ArrayList<BankMasterAccountVariantVo>();
		try {
			String query = FinanceCommonConstants.GET_BANK_ACCOUNT_VARIANT;
			preparedStatement = con.prepareStatement(query);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				BankMasterAccountVariantVo bankMasterAccountVariantVo = new BankMasterAccountVariantVo();
				bankMasterAccountVariantVo.setId(rs.getInt(1));
				bankMasterAccountVariantVo.setName(rs.getString(2));
				bankMasterAccountVariantVo.setAccountTypeId(rs.getInt(3));
				data.add(bankMasterAccountVariantVo);
			}
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, null);
		}
		return data;
	}

	public String findAccountingTypeName(Integer typeId) throws ApplicationException {
		logger.info("Entry into method: findTypename");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		String accountingType = null;
		try {
			con = getFinanceCommon();
			String query = FinanceCommonConstants.GET_ACCOUNT_TYPE_BY_ID;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, typeId);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				accountingType = rs.getString(1);
			}
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return accountingType;
	}

	public String getEmployeeTypeById(Integer id) throws ApplicationException {
		logger.info("Entry into method: getEmployeeType");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		String employeeType = null;
		try {
			con = getFinanceCommon();
			String query = FinanceCommonConstants.GET_EMPLOYEE_TYPE_BY_ID;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, id);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				employeeType = rs.getString(1);
			}
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return employeeType;
	}

	public List<ChartOfAccountsEntityVo> getChartOfAccountsEntity(Connection con) throws ApplicationException {
		logger.info("To get the getChartOfAccountsEntity");
		List<ChartOfAccountsEntityVo> entities = null;
		PreparedStatement preparedStatement =null;
		ResultSet rs=null;
		try {
			preparedStatement = con.prepareStatement(FinanceCommonConstants.GET_CHART_OF_ACCOUNTS_ENTITY);
			entities = new ArrayList<ChartOfAccountsEntityVo>();
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				ChartOfAccountsEntityVo entityVo = new ChartOfAccountsEntityVo();
				entityVo.setId(rs.getInt(1));
				entityVo.setName(rs.getString(2));
				entityVo.setStatus(rs.getString(3));
				entityVo.setValue(rs.getInt(1));
				entities.add(entityVo);
			}
		} catch (Exception e) {
			logger.info("Error in getChartOfAccountsEntity", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, null);
		}
		return entities;
	}

	public List<ChartOfAccountsModuleVo> getChartOfAccountsModule(Connection con) throws ApplicationException {
		logger.info("To get the getChartOfAccountsModule");
		List<ChartOfAccountsModuleVo> modules = null;
		PreparedStatement preparedStatement =null;
		ResultSet rs=null;
		try {
			preparedStatement = con.prepareStatement(FinanceCommonConstants.GET_CHART_OF_ACCOUNTS_MODULE); 
			modules = new ArrayList<ChartOfAccountsModuleVo>();
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				ChartOfAccountsModuleVo moduleVo = new ChartOfAccountsModuleVo();
				moduleVo.setId(rs.getInt(1));
				moduleVo.setName(rs.getString(2));
				moduleVo.setValue(rs.getInt(1));
				modules.add(moduleVo);
			}
		} catch (Exception e) {
			logger.info("Error in getChartOfAccountsModule", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, null);
		}
		return modules;
	}

	public String getEntity(Integer entityId) throws ApplicationException {
		String entity = null;
		logger.info("Entered into  getEntity ");
		Connection con=null;
		PreparedStatement preparedStatement =null;
		ResultSet rs =null;
		try  {
			con = getFinanceCommon();
			preparedStatement = con.prepareStatement(FinanceCommonConstants.GET_CHART_OF_ACCOUNTS_ENTITY_FROM_ID);
			preparedStatement.setInt(1, entityId);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				entity = rs.getString(1);
			}
			logger.info("Entity is  " + entity);
		} catch (ApplicationException | SQLException e) {
			logger.info("Error in getEntity ", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return entity;
	}

	public List<ModuleTypeVo> getUploadModuleTypeDropDown(Connection con) throws ApplicationException {
		logger.info("To get the getUploadModuleTypeDropDown");
		List<ModuleTypeVo> modules = null;
		PreparedStatement preparedStatement =null;
		ResultSet rs =null;
		try {
			preparedStatement = con.prepareStatement(FinanceCommonConstants.GET_IMPORT_MODULES);
			modules = new ArrayList<ModuleTypeVo>();
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				ModuleTypeVo moduleVo = new ModuleTypeVo();
				moduleVo.setId(rs.getInt(1));
				moduleVo.setValue(rs.getString(2));
				moduleVo.setName(rs.getString(2));
				if(moduleVo.getName().equals("Bank Statement")){
					logger.info("Bank Statement");
				}else{
					modules.add(moduleVo);
				}
				
			}
		} catch (Exception e) {
			logger.info("Error in getUploadModuleTypeDropDown", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, null);
		}
		return modules;
	}

	public String getOrganizationType(int id) throws ApplicationException {
		logger.info("To get the getOrganizationType");
		String organizationName = null;
		Connection con = null;
		PreparedStatement preparedStatement =null;
		ResultSet rs =null;
		try {
			con = getFinanceCommon();
			preparedStatement = con.prepareStatement(FinanceCommonConstants.GET_ORGANIZATION_TYPE_NAME);
			preparedStatement.setInt(1, id);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				organizationName = rs.getString(1);
			}
		} catch (Exception e) {
			logger.info("Error in getOrganizationType", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return organizationName;
	}

	public String getGstTypeName(int id) throws ApplicationException {
		logger.info("To get the getGstTypeName");
		String gstTypeName = null;
		Connection con = null;
		PreparedStatement preparedStatement =null;
		ResultSet rs =null;
		try {
			con = getFinanceCommon();
			preparedStatement = con.prepareStatement(FinanceCommonConstants.GET_GST_TYPE_NAME);
			preparedStatement.setInt(1, id);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				gstTypeName = rs.getString(1);
			}
		} catch (Exception e) {
			logger.info("Error in getGstTypeName", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return gstTypeName;
	}

	public String getTdsTypeName(int id) throws ApplicationException {
		logger.info("To get the getTdsTypeName");
		String tdsName = null;
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			con = getFinanceCommon();
			preparedStatement = con.prepareStatement(FinanceCommonConstants.GET_TDS_TYPE_NAME);
			preparedStatement.setInt(1, id);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				tdsName = rs.getString(1);
			}
		} catch (Exception e) {
			logger.info("Error in getTdsTypeName", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return tdsName;
	}

	public String getCountryName(int id) throws ApplicationException {
		logger.info("To get the getCountryName");
		String countryName = null;
		Connection con =null;
		PreparedStatement preparedStatement = null;
		ResultSet rs =null;
		try {
			con = getFinanceCommon();
			preparedStatement = con.prepareStatement(FinanceCommonConstants.GET_COUNTRY_NAME); 
			preparedStatement.setInt(1, id);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				countryName = rs.getString(1);
			}
		} catch (Exception e) {
			logger.info("Error in getCountryName", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return countryName;
	}

	public String getStateName(int id) throws ApplicationException {
		logger.info("To get the getStateName");
		String stateName = null;
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			con = getFinanceCommon();
			preparedStatement = con.prepareStatement(FinanceCommonConstants.GET_STATE_NAME);
			preparedStatement.setInt(1, id);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				stateName = rs.getString(1);
			}
		} catch (Exception e) {
			logger.info("Error in getStateName", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return stateName;
	}

	public String getAccountingEntryTypeName(int id) throws ApplicationException {
		logger.info("To get the getAccountingEntryTypeName");
		String typeName = null;
		Connection con = null;
		PreparedStatement preparedStatement =null;
		ResultSet rs = null;
		try {
			con = getFinanceCommon();
			preparedStatement = con.prepareStatement(FinanceCommonConstants.GET_ACCOUNTING_ENTRY_TYPE_NAME);
			preparedStatement.setInt(1, id);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				typeName = rs.getString(1);
			}
		} catch (Exception e) {
			logger.info("Error in getAccountingEntryTypeName", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return typeName;
	}

	// Vendor Portal Base General settings
	public List<BaseGeneralSettingsVo> getBaseGeneralSettings() throws ApplicationException {
		logger.info("Entry into method:getBaseGeneralSettings");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		List<BaseGeneralSettingsVo> data = new ArrayList<BaseGeneralSettingsVo>();
		try {
			con = getFinanceCommon();
			String query = FinanceCommonConstants.GET_BASE_GENERAL_SETTINGS;
			preparedStatement = con.prepareStatement(query);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				BaseGeneralSettingsVo generalSettingsData = new BaseGeneralSettingsVo();
				generalSettingsData.setId(rs.getInt(1));
				generalSettingsData.setName(rs.getString(2));
				generalSettingsData.setDescription(rs.getString(3));
				data.add(generalSettingsData);
			}
		} catch (Exception e) {

			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);

		}
		return data;

	}

	// Vendor Portal Predefined Settings to get Module
	public List<SettingsModuleVo> getPredefinedSettingModules() throws ApplicationException {
		List<SettingsModuleVo> modules = null;
		logger.info("To getPredefinedSettingModules ");
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			con = getFinanceCommon();
			preparedStatement = con.prepareStatement(FinanceCommonConstants.GET_PREDEFINED_SETTING_MODULES);
			rs = preparedStatement.executeQuery();
			modules = new ArrayList<SettingsModuleVo>();
			while (rs.next()) {
				SettingsModuleVo module = new SettingsModuleVo();
				module.setBaseId(rs.getInt(1));
				module.setModule(rs.getString(2));
				module.setIsActive(false);
				modules.add(module);
			}
			logger.info(" getPredefinedSettingModules fetched::" + modules.size());
		} catch (Exception e) {
			logger.info("Error in getPredefinedSettingModules", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);

		}
		return modules;

	}

	// Vendor Portal Predefined Settings to get Templates
	public List<SettingsTemplateVo> getPredefinedSettingTemplates() throws ApplicationException {
		List<SettingsTemplateVo> templates = null;
		logger.info("To getPredefinedSettingTemplates ");
		Connection con =null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			con = getFinanceCommon();
			preparedStatement = con.prepareStatement(FinanceCommonConstants.GET_PREDEFINED_SETTING_TEMPLATES);
			rs = preparedStatement.executeQuery();
			templates = new ArrayList<SettingsTemplateVo>();
			while (rs.next()) {
				SettingsTemplateVo template = new SettingsTemplateVo();
				template.setBaseId(rs.getInt(1));
				template.setTemplateType(rs.getString(2));
				template.setTemplateName(rs.getString(3));
				template.setIsActive(false);
				templates.add(template);
			}
			logger.info(" getPredefinedSettingTemplates fetched::" + templates.size());
		} catch (Exception e) {
			logger.info("Error in getPredefinedSettingTemplates", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return templates;
	}

	// Vendor Portal Predefined Settings to get Validations
	public List<SettingsValidationVo> getPredefinedSettingValidation() throws ApplicationException {
		List<SettingsValidationVo> validations = null;
		logger.info("To getPredefinedSettingValidation ");
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try{
			con = getFinanceCommon();
			preparedStatement = con.prepareStatement(FinanceCommonConstants.GET_PREDEFINED_SETTING_VALIDATION);
			rs = preparedStatement.executeQuery();
			validations = new ArrayList<SettingsValidationVo>();
			while (rs.next()) {
				SettingsValidationVo validation = new SettingsValidationVo();
				validation.setBaseId(rs.getInt(1));
				validation.setActivity(rs.getString(2));
				validation.setValidationRule(rs.getString(3));
				validation.setIsActive(false);
				validations.add(validation);
			}
		} catch (Exception e) {
			logger.info("Error in getPredefinedSettingValidation", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return validations;

	}


	public List<PaymentModeVo> getPaymentMode() throws ApplicationException {
		logger.info("Entry into method: getPaymentMode");
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		List<PaymentModeVo> paymentModesList = new ArrayList<PaymentModeVo>();
		try {
			con = getFinanceCommon();
			preparedStatement = con.prepareStatement(FinanceCommonConstants.GET_PAYMENT_MODES);
			rs = preparedStatement.executeQuery();

			while (rs.next()) {
				PaymentModeVo paymentModeVo = new PaymentModeVo();
				paymentModeVo.setId(rs.getInt(1));
				paymentModeVo.setName(rs.getString(2));
				paymentModesList.add(paymentModeVo);
			}
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return paymentModesList;
	}

	public List<CommonVo> getReasons() throws ApplicationException {
		List<CommonVo> reasons = null;
		Connection con = null;
		PreparedStatement preparedStatement =null;
		ResultSet rs =null;
		try {
			con = getFinanceCommon();
			preparedStatement = con.prepareStatement(FinanceCommonConstants.GET_BASE_REASONS);
			reasons = new ArrayList<CommonVo>();
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				CommonVo vo = new CommonVo();
				vo.setId(rs.getInt(1));
				vo.setName(rs.getString(2));
				reasons.add(vo);
			}
		} catch (Exception e) {
			logger.info("Error in getPredefinedSettingValidation", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return reasons;

	}
	
	public List<CommonVo> getRejectionTypes() throws ApplicationException {
		List<CommonVo> reasons = null;
		Connection con = null;
		PreparedStatement preparedStatement =null;
		ResultSet rs =null;
		try {
			con = getFinanceCommon();
			preparedStatement = con.prepareStatement(FinanceCommonConstants.GET_REJECTION_TYPES);
			reasons = new ArrayList<CommonVo>();
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				CommonVo vo = new CommonVo();
				vo.setId(rs.getInt(1));
				vo.setName(rs.getString(2));
				vo.setValue(rs.getInt(1));
				reasons.add(vo);
			}
		} catch (Exception e) {
			logger.info("Error in getRejectionTypes", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return reasons;

	}

	public List<PayTypeVo> getBasePayType() throws ApplicationException {
		logger.info("Entry into method: getBasePayType");
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		List<PayTypeVo> data = new ArrayList<PayTypeVo>();
		try {
			con = getFinanceCommon();
			String query = FinanceCommonConstants.GET_BASE_PAY_TYPE;
			preparedStatement = con.prepareStatement(query);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				PayTypeVo payTypeVo = new PayTypeVo();
				payTypeVo.setName(rs.getString(1));
				payTypeVo.setDescription(rs.getString(2));
				payTypeVo.setParentName(rs.getString(3));
				payTypeVo.setDeditOrCredit(rs.getString(4));
				data.add(payTypeVo);
			}
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return data;
	}

	public List<PayItemVo> getBasePayItem() throws ApplicationException {
		logger.info("Entry into method: getBasePayItem");
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		List<PayItemVo> data = new ArrayList<PayItemVo>();
		try {
			con = getFinanceCommon();
			String query = FinanceCommonConstants.GET_BASE_PAY_ITEM;
			preparedStatement = con.prepareStatement(query);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				PayItemVo payItemVo = new PayItemVo();
				payItemVo.setName(rs.getString(1));
				payItemVo.setPayTypeName(rs.getString(2));
				payItemVo.setLedgerName(rs.getString(3));
				data.add(payItemVo);
			}
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return data;
	}

	public List<VoucherTypeVo> getVoucherTypes() throws ApplicationException {
		logger.info("Entry into method: getVoucherTypes");
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		List<VoucherTypeVo> data = new ArrayList<VoucherTypeVo>();
		try {
			con = getFinanceCommon();
			String query = FinanceCommonConstants.GET_VOUCHER_TYPE;
			preparedStatement = con.prepareStatement(query);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				VoucherTypeVo voucherTypeVo = new VoucherTypeVo();
				voucherTypeVo.setId(rs.getInt(1));
				voucherTypeVo.setValue(rs.getString(2));
				voucherTypeVo.setName(voucherTypeVo.getValue());
				data.add(voucherTypeVo);
			}
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return data;
	}

	public List<DocumentTypeVo> getDocumentTypes(Connection con) throws ApplicationException {
		List<DocumentTypeVo> documentTypes = null;
		logger.info("To getDocumentTypes ");
		PreparedStatement preparedStatement =null;
		ResultSet rs = null;
		try {
			preparedStatement = con.prepareStatement(FinanceCommonConstants.GET_DOCUMENT_TYPES);
			rs = preparedStatement.executeQuery();
			documentTypes = new ArrayList<DocumentTypeVo>();
			while (rs.next()) {
				DocumentTypeVo documentTypeVo = new DocumentTypeVo();
				documentTypeVo.setId(rs.getInt(1));
				documentTypeVo.setName(rs.getString(2));
				documentTypeVo.setModule(rs.getString(3));
				documentTypes.add(documentTypeVo);
			}
		} catch (Exception e) {
			logger.info("Error in getDocumentTypes", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, null);
		}
		return documentTypes;
	}


	public List<CommonVo> getInvoiceTypes() throws ApplicationException {
		List<CommonVo> invoiceTypes = null;
		Connection con =null;
		PreparedStatement preparedStatement =null;
		ResultSet rs = null;
		try {
			con = getFinanceCommon();
			preparedStatement = con.prepareStatement(FinanceCommonConstants.GET_INVOICE_TYPES); 
			invoiceTypes = new ArrayList<CommonVo>();
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				CommonVo vo = new CommonVo();
				vo.setId(rs.getInt(1));
				vo.setName(rs.getString(2));
				invoiceTypes.add(vo);
			}
		} catch (Exception e) {
			logger.info("Error in getInvoiceTypes", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return invoiceTypes;

	}

	public List<CommonVo> getSupplyServices() throws ApplicationException {
		List<CommonVo> supplyServices = null;
		Connection con =null;
		PreparedStatement preparedStatement =null;
		ResultSet rs = null;
		try {
			con = getFinanceCommon();
			preparedStatement = con.prepareStatement(FinanceCommonConstants.GET_SUPPLY_SERVICES);
			supplyServices = new ArrayList<CommonVo>();
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				CommonVo vo = new CommonVo();
				vo.setId(rs.getInt(1));
				vo.setName(rs.getString(2));
				supplyServices.add(vo);
			}
			logger.info("Fetched in getSupplyServices"+ supplyServices.size());

		} catch (Exception e) {
			logger.info("Error in getSupplyServices", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return supplyServices;

	}

	public CommonVo getSupplyServicesById(int id) throws ApplicationException {
		CommonVo supplyService = new CommonVo();
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			con = getFinanceCommon();
			preparedStatement = con.prepareStatement(FinanceCommonConstants.GET_SUPPLY_SERVICES_BY_ID);
			preparedStatement.setInt(1, id);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				supplyService.setId(rs.getInt(1));
				supplyService.setName(rs.getString(2));
			}
			logger.info("Fetched in getSupplyServicesById"+ supplyService);
		} catch (Exception e) {
			logger.info("Error in getSupplyServicesById", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return supplyService;
	}

	public List<CommonVo> getExportTypes() throws ApplicationException {
		List<CommonVo> exportTypes = null;
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs =null;
		try{
			con = getFinanceCommon();
			preparedStatement = con.prepareStatement(FinanceCommonConstants.GET_EXPORT_TYPE);
			exportTypes = new ArrayList<CommonVo>();
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				CommonVo vo = new CommonVo();
				vo.setId(rs.getInt(1));
				vo.setName(rs.getString(2));
				exportTypes.add(vo);
			}
			logger.info("Fetched in getExportTypes"+ exportTypes.size());
		} catch (Exception e) {
			logger.info("Error in getExportTypes", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return exportTypes;

	}
	
	public Integer getAccountingTypes(String type) throws ApplicationException {
		logger.info("Enter into Method: getAccountingTypes");
		Integer typeId = null;
		Connection con =null;
		PreparedStatement preparedStatement=null;
		ResultSet rs = null;
		try {
			con = getFinanceCommon();
			preparedStatement = con.prepareStatement(FinanceCommonConstants.GET_ACCOUNTING_TYPES);
			preparedStatement.setString(1, type);
			rs = preparedStatement.executeQuery();
				while (rs.next()) {
					typeId = rs.getInt(1);
				}
		} catch (Exception e) {
			logger.info("Error in  getAccountingTypes:", e);
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return typeId;
	}
	
	public List<CommonVo> getCreditNoteReasons() throws ApplicationException {
		List<CommonVo> exportTypes = null;
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs =null;
		try{
			con = getFinanceCommon();
			preparedStatement = con.prepareStatement(FinanceCommonConstants.GET_CREDIT_NOTE_REASONS);
			preparedStatement.setString(1, CommonConstants.STATUS_AS_DELETE);
			exportTypes = new ArrayList<CommonVo>();
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				CommonVo vo = new CommonVo();
				vo.setId(rs.getInt(1));
				vo.setName(rs.getString(2));
				exportTypes.add(vo);
			}
			logger.info("Fetched in getCreditNoteReasons"+ exportTypes.size());
		} catch (Exception e) {
			logger.info("Error in getCreditNoteReasons", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return exportTypes;

	}
	

	public List<CommonVo> getRefundTypes() throws ApplicationException {
		List<CommonVo> exportTypes = null;
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs =null;
		try{
			con = getFinanceCommon();
			preparedStatement = con.prepareStatement(FinanceCommonConstants.GET_REFUND_TYPES);
			preparedStatement.setString(1, CommonConstants.STATUS_AS_DELETE);
			exportTypes = new ArrayList<CommonVo>();
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				CommonVo vo = new CommonVo();
				vo.setId(rs.getInt(1));
				vo.setName(rs.getString(2));
				exportTypes.add(vo);
			}
			logger.info("Fetched in getRefundTypes"+ exportTypes.size());
		} catch (Exception e) {
			logger.info("Error in getRefundTypes", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return exportTypes;

	}
	
	public List<ReceiptTypeVo> getReceiptTypes() throws ApplicationException {
		List<ReceiptTypeVo> receiptTypes = null;
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs =null;
		try{
			con = getFinanceCommon();
			preparedStatement = con.prepareStatement(FinanceCommonConstants.GET_RECEIPT_TYPE);
			receiptTypes = new ArrayList<ReceiptTypeVo>();
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				ReceiptTypeVo vo = new ReceiptTypeVo();
				vo.setName(rs.getString(2));
				vo.setValue(rs.getString(2));
				receiptTypes.add(vo);
			}
			logger.info("Fetched in getRefundTypes"+ receiptTypes.size());
		} catch (Exception e) {
			logger.info("Error in getRefundTypes", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return receiptTypes;

	}
	
	public List<ModuleTypeVo> getWorkflowModules() throws ApplicationException {
		List<ModuleTypeVo> modules = new ArrayList<ModuleTypeVo>();
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs =null;
		try{
			con = getFinanceCommon();
			preparedStatement = con.prepareStatement(FinanceCommonConstants.GET_WORFKFLOW_MODULES);
			preparedStatement.setString(1, CommonConstants.STATUS_AS_ACTIVE);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				ModuleTypeVo moduleTypeVo=new ModuleTypeVo();
				moduleTypeVo.setId(rs.getInt(1));
				moduleTypeVo.setName(rs.getString(2));
				modules.add(moduleTypeVo);
			}
			logger.info("Fetched in getWorkflowModules:"+ modules);
		} catch (Exception e) {
			logger.info("Error in getWorkflowModules", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return modules;

	}
	
	public ModuleTypeVo getWorkflowModuleByName(String moduleName) throws ApplicationException {
		ModuleTypeVo moduleTypeVo=new ModuleTypeVo();
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs =null;
		try{
			con = getFinanceCommon();
			preparedStatement = con.prepareStatement(FinanceCommonConstants.GET_WORFKFLOW_MODULE_BY_NAME);
			preparedStatement.setString(1, moduleName);
			preparedStatement.setString(2, CommonConstants.STATUS_AS_ACTIVE);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				
				moduleTypeVo.setId(rs.getInt(1));
				moduleTypeVo.setName(rs.getString(2));
			}
			logger.info("Fetched in getWorkflowModuleByName:"+ moduleTypeVo);
		} catch (Exception e) {
			logger.info("Error in getWorkflowModuleByName", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return moduleTypeVo;

	}
	
	public ModuleTypeVo getWorkflowModuleById(int moduleId) throws ApplicationException {
		ModuleTypeVo moduleTypeVo=new ModuleTypeVo();
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs =null;
		try{
			con = getFinanceCommon();
			preparedStatement = con.prepareStatement(FinanceCommonConstants.GET_WORFKFLOW_MODULE_BY_ID);
			preparedStatement.setInt(1, moduleId);
			preparedStatement.setString(2, CommonConstants.STATUS_AS_ACTIVE);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				
				moduleTypeVo.setId(rs.getInt(1));
				moduleTypeVo.setName(rs.getString(2));
			}
			logger.info("Fetched in getWorkflowModuleById:"+ moduleTypeVo);
		} catch (Exception e) {
			logger.info("Error in getWorkflowModuleById", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return moduleTypeVo;

	}
	
	public List<WorkflowCommonVo> getWorkflowGeneralSettingsForModule(int moduleId) throws ApplicationException {
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs =null;
		List<WorkflowCommonVo>  conditions=new ArrayList<WorkflowCommonVo> ();
		try{
			con = getFinanceCommon();
			preparedStatement = con.prepareStatement(FinanceCommonConstants.GET_WORFKFLOW_GENERAL_SETTINGS_CONDITIONS_FOR_MODULE);
			preparedStatement.setInt(1, moduleId);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				WorkflowCommonVo condition=new WorkflowCommonVo();
				condition.setId(rs.getInt(1));
				condition.setName(rs.getString(2));
				condition.setDescription(rs.getString(3));
				conditions.add(condition);
				}
			logger.info("Fetched in getWorkflowGeneralSettingsForModule:"+ conditions);
		} catch (Exception e) {
			logger.info("Error in getWorkflowModules", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return conditions;

	}
	
	public CommonVo getRefundTypeByName(String name) throws ApplicationException {
		CommonVo vo = new CommonVo();
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs =null;
		try{
			con = getFinanceCommon();
			preparedStatement = con.prepareStatement(FinanceCommonConstants.GET_REFUND_TYPE_BY_NAME);
			preparedStatement.setString(1, name);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				
				vo.setId(rs.getInt(1));
				vo.setName(rs.getString(2));
			}
			logger.info("Fetched in getRefundTypeById:"+ vo);
		} catch (Exception e) {
			logger.info("Error in getRefundTypeById", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return vo;

	}
	
	public List<CommonVo> getWorkflowApprovalTypes() throws ApplicationException {
		List<CommonVo> types = new ArrayList<CommonVo>();
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs =null;
		try{
			con = getFinanceCommon();
			preparedStatement = con.prepareStatement(FinanceCommonConstants.GET_WORFKFLOW_APPROVAL_TYPES);
			
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				CommonVo approvalType=new CommonVo();
				approvalType.setId(rs.getInt(1));
				approvalType.setName(rs.getString(2));
				types.add(approvalType);
			}
			logger.info("Fetched in getWorkflowApprovalTypes:"+ types);
		} catch (Exception e) {
			logger.info("Error in getWorkflowApprovalTypes", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return types;

	}
	
	public CommonVo getWorkflowApprovalTypeById(int id) throws ApplicationException {
		CommonVo type = null;
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs =null;
		try{
			con = getFinanceCommon();
			preparedStatement = con.prepareStatement(FinanceCommonConstants.GET_WORFKFLOW_APPROVAL_TYPE_BY_ID);
			preparedStatement.setInt(1, id);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				type=new CommonVo();
				type.setId(rs.getInt(1));
				type.setName(rs.getString(2));
			}
			logger.info("Fetched in getWorkflowApprovalTypeById:"+ type);
		} catch (Exception e) {
			logger.info("Error in getWorkflowApprovalTypeById", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return type;

	}

	public List<WorkflowRuleConditionVo> getWorkflowConditions(String moduleName) throws ApplicationException {
		List<WorkflowRuleConditionVo> conditions = new ArrayList<WorkflowRuleConditionVo>();
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs =null;
		try{
			con = getFinanceCommon();
			preparedStatement = con.prepareStatement(FinanceCommonConstants.GET_WORFKFLOW_CONDITIONS);
			preparedStatement.setString(1, moduleName);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				WorkflowRuleConditionVo workflowConditionVo=new WorkflowRuleConditionVo();
				workflowConditionVo.setId(rs.getInt(1));
				workflowConditionVo.setName(rs.getString(2));
				List<WorkflowRuleChoiceVo> choices=getWorkflowChoicesForCondition(rs.getInt(1));
				workflowConditionVo.setChoice(choices);
				conditions.add(workflowConditionVo);
			}
			logger.info("Fetched in getWorkflowConditions:"+ conditions);
		} catch (Exception e) {
			logger.info("Error in getWorkflowConditions", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return conditions;

	}
	public List<WorkflowRuleConditionVo> getWorkflowConditionByModule(int moduleId) throws ApplicationException {
		List<WorkflowRuleConditionVo> conditions = new ArrayList<WorkflowRuleConditionVo>();
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs =null;
		try{
			con = getFinanceCommon();
			preparedStatement = con.prepareStatement(FinanceCommonConstants.GET_WORFKFLOW_CONDITIONS_FOR_MODULE);
			preparedStatement.setInt(1, moduleId);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				WorkflowRuleConditionVo workflowConditionVo=new WorkflowRuleConditionVo();
				workflowConditionVo.setId(rs.getInt(1));
				workflowConditionVo.setName(rs.getString(2));
				List<WorkflowRuleChoiceVo> choices=getWorkflowChoicesForCondition(rs.getInt(1));
				workflowConditionVo.setChoice(choices);
				conditions.add(workflowConditionVo);
			}
			logger.info("Fetched in getWorkflowConditionByModule:"+ conditions);
		} catch (Exception e) {
			logger.info("Error in getWorkflowConditionByModule", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return conditions;

	}
	
	public WorkflowRuleConditionVo getWorkflowConditionById(int id) throws ApplicationException {
		WorkflowRuleConditionVo condition = new WorkflowRuleConditionVo();
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs =null;
		try{
			con = getFinanceCommon();
			preparedStatement = con.prepareStatement(FinanceCommonConstants.GET_WORFKFLOW_CONDITION_BY_ID);			
			preparedStatement.setInt(1, id);
			rs = preparedStatement.executeQuery();
			
			while (rs.next()) {
				condition.setId(rs.getInt(1));
				condition.setName(rs.getString(2));
				List<WorkflowRuleChoiceVo> choices=getWorkflowChoicesForCondition(rs.getInt(1));
				condition.setChoice(choices);
			}
			logger.info("Fetched in getWorkflowConditionById:"+ condition);
		} catch (Exception e) {
			logger.info("Error in getWorkflowConditionById", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return condition;

	}
	
	public List<WorkflowRuleChoiceVo> getWorkflowChoicesForCondition(int conditionId) throws ApplicationException {
		List<WorkflowRuleChoiceVo> conditions = new ArrayList<WorkflowRuleChoiceVo>();
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs =null;
		try{
			con = getFinanceCommon();
			preparedStatement = con.prepareStatement(FinanceCommonConstants.GET_WORFKFLOW_CHOICES_FOR_CONDITION);
			preparedStatement.setInt(1, conditionId);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				WorkflowRuleChoiceVo workflowChoiceVo=new WorkflowRuleChoiceVo();
				workflowChoiceVo.setId(rs.getInt(1));
				workflowChoiceVo.setName(rs.getString(2));
				conditions.add(workflowChoiceVo);
			}
			logger.info("Fetched in getWorkflowConditions:"+ conditions);
		} catch (Exception e) {
			logger.info("Error in getWorkflowConditions", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return conditions;

	}
	
	public List<CommonVo> getReceiptBulkDetailTypes() throws ApplicationException {
		List<CommonVo> types = new ArrayList<CommonVo>();
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs =null;
		try{
			con = getFinanceCommon();
			preparedStatement = con.prepareStatement(FinanceCommonConstants.GET_RECEIPT_BULK_DETAIL_TYPES);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				CommonVo CommonVo=new CommonVo();
				CommonVo.setId(rs.getInt(1));
				CommonVo.setName(rs.getString(2));
				CommonVo.setValue(rs.getInt(1));
				types.add(CommonVo);
			}
			logger.info("Fetched in getReceiptBulkDetailTypes:"+ types);
		} catch (Exception e) {
			logger.info("Error in getReceiptBulkDetailTypes", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return types;

	}

	public String getReceiptBulkDetailTypeByID(int typeId) throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		String type = "";
		try {
			connection = getFinanceCommon();
			preparedStatement = connection.prepareStatement(FinanceCommonConstants.GET_RECEIPT_BULK_DETAIL_TYPE_BY_ID);
			preparedStatement.setInt(1, typeId);

			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				type = rs.getString(1);
			}
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, connection);
		}
		return type;
	}
	public int getReceiptBulkDetailTypeByName(String type) throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		int id=0;
		try {
			connection = getFinanceCommon();
			preparedStatement = connection.prepareStatement(FinanceCommonConstants.GET_RECEIPT_BULK_DETAIL_TYPE_BY_NAME);
			preparedStatement.setString(1, type);

			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				id = rs.getInt(1);
			}
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, connection);
		}
		return id;
	}
	
	public String getInvoiceType(int invoiceId) throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		String invoiceType = "";
		try {
			connection = getAccountsReceivableConnection();
			preparedStatement = connection.prepareStatement(FinanceCommonConstants.GET_INVOICE_TYPE);
			preparedStatement.setInt(1, invoiceId);

			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				invoiceType = rs.getString(1);
			}
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, connection);
		}
		return invoiceType;
	}
	
	public CommonVo getCreditnoteTypeById(int creditNoteTypeId) throws ApplicationException {
		CommonVo vo = new CommonVo();
		Connection con =null;
		PreparedStatement preparedStatement =null;
		ResultSet rs = null;
		try {
			con = getFinanceCommon();
			preparedStatement = con.prepareStatement(FinanceCommonConstants.GET_CREDIT_NOTE_TYPE_BY_ID);
			preparedStatement.setInt(1, creditNoteTypeId);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				
				vo.setId(rs.getInt(1));
				vo.setName(rs.getString(2));
			}
		} catch (Exception e) {
			logger.info("Error in getCreditnoteTypeById", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return vo;

	}
	
	public String getPaymentModeById(int id) throws ApplicationException {
		logger.info("Entry into method: getPaymentModeById");
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		String paymentMode = null;
		try {
			con = getFinanceCommon();
			preparedStatement = con.prepareStatement(FinanceCommonConstants.GET_PAYMENT_MODE_BY_ID);
			preparedStatement.setInt(1, id);
			rs = preparedStatement.executeQuery();

			while (rs.next()) {
				paymentMode = rs.getString(1);
			}
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return paymentMode;
	}

	public List<ReportPeriodVo> getReportPeriod() throws ApplicationException {
		List<ReportPeriodVo> periods = new ArrayList<ReportPeriodVo>();
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs =null;
		try{
			con = getFinanceCommon();
			preparedStatement = con.prepareStatement(FinanceCommonConstants.GET_REPORTS_PERIOD);
			preparedStatement.setString(1, CommonConstants.STATUS_AS_ACTIVE);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				ReportPeriodVo period=new ReportPeriodVo();
				period.setPeriodId(rs.getInt(1));
				period.setPeriodName(rs.getString(2));
				periods.add(period);
			}
			logger.info("Fetched in getReportPeriods:"+ periods);
		} catch (Exception e) {
			logger.info("Error in getReportPeriods", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return periods;
		
	}

	public List<PaymentTypeVo> getPayPeriodFrequency() throws ApplicationException {

		List<PaymentTypeVo> periods = new ArrayList<PaymentTypeVo>();
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs =null;
		try{
			con = getFinanceCommon();
			preparedStatement = con.prepareStatement(FinanceCommonConstants.GET_PAY_PERIOD_FREQUENCY);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				PaymentTypeVo period=new PaymentTypeVo();
				period.setId(rs.getInt(1));
				period.setName(rs.getString(2));
				period.setValue(rs.getInt(1));
				periods.add(period);
			}
			logger.info("Fetched in getPayPeriodFrequency:"+ periods);
		} catch (Exception e) {
			logger.info("Error in getPayPeriodFrequency", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return periods;
		
	
	}

	public List<PaymentTypeVo> getPayPeriodFinancialYears() throws ApplicationException {

		List<PaymentTypeVo> periods = new ArrayList<PaymentTypeVo>();
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs =null;
		try{
			con = getFinanceCommon();
			preparedStatement = con.prepareStatement(FinanceCommonConstants.GET_PAY_PERIOD_FINANCIAL_YEARS);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				PaymentTypeVo period=new PaymentTypeVo();
				period.setId(rs.getInt(1));
				period.setName(rs.getString(2));
				period.setValue(rs.getInt(1));
				periods.add(period);
			}
			logger.info("Fetched in getPayPeriodFrequency:"+ periods);
		} catch (Exception e) {
			logger.info("Error in getPayPeriodFrequency", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return periods;
		
	
	}

	
	public List<SaCodeVo> getSaCodes() throws ApplicationException{
		logger.info("To get SaCodes");
		List<SaCodeVo> saCodes = new ArrayList<SaCodeVo>();
		try(final Connection con = getFinanceCommon() ; final PreparedStatement preparedStatement = con.prepareStatement(FinanceCommonConstants.GET_SA_CODES) ; final ResultSet rs = preparedStatement.executeQuery()){
			while(rs.next()) {
				SaCodeVo saCode = new SaCodeVo();
				saCode.setId(rs.getInt(1));
				saCode.setSectionName(rs.getString(2));
				saCode.setHeadingId(rs.getString(3));
				saCode.setHeadingName(rs.getString(4));
				saCode.setGroupId(rs.getString(5));
				saCode.setGroupName(rs.getString(6));
				saCode.setSectionName(rs.getString(7));
				saCode.setServiceCode(rs.getString(8));
				saCode.setInterStateGst(rs.getString(9));
				saCode.setIntraStateGst(rs.getString(10));
				saCodes.add(saCode);
			}
			logger.info("To get SaCodes size ::"+ saCodes.size());
		} catch (Exception e) {
			logger.info("Error in getSaCodes", e);
			throw new ApplicationException(e);
		} 
		return saCodes;
	}
	
	
	
	public List<HsnCodeVo> getHsnCodes() throws ApplicationException{
		logger.info("To get getHsnCodes");
		List<HsnCodeVo> hsnCodes = new ArrayList<HsnCodeVo>();
		try(final Connection con = getFinanceCommon() ; final PreparedStatement preparedStatement = con.prepareStatement(FinanceCommonConstants.GET_HSN_CODES) ; final ResultSet rs = preparedStatement.executeQuery()){
			while(rs.next()) {
				HsnCodeVo hsnCode = new HsnCodeVo();
				hsnCode.setId(rs.getInt(1));
				hsnCode.setCommodityName(rs.getString(2));
				hsnCode.setHsnCode(rs.getString(3));
				hsnCode.setInterStateGst(rs.getString(4));
				hsnCode.setIntraStateGst(rs.getString(5));
				hsnCodes.add(hsnCode);
			}
			logger.info("To get getHsnCodes size ::"+ hsnCodes.size());
		} catch (Exception e) {
			logger.info("Error in getSaCodes", e);
			throw new ApplicationException(e);
		} 
		return hsnCodes;
	}
	
	public List<PaymentTypeVo> getPayrollPaidForType(Connection con) throws ApplicationException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<PaymentTypeVo> paidForList = new ArrayList<PaymentTypeVo>();
		try {
			ps = con.prepareStatement(FinanceCommonConstants.GET_PAID_FOR_TYPES);
			rs = ps.executeQuery();
			while (rs.next()) {
				PaymentTypeVo paidFor = new PaymentTypeVo();
				paidFor.setId(rs.getInt(1));
				paidFor.setValue(rs.getInt(1));
				paidFor.setName(rs.getString(2));
				paidForList.add(paidFor);
			}
		} catch (Exception e) {
			logger.error("Error during getPaidForTypes", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, ps, null);
		}
		return paidForList;
	}
	
}

package com.blackstrawai.settings;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.chartofaccounts.ChartOfAccountsDao;
import com.blackstrawai.common.BaseDao;

@Repository
public class TaxRateMappingDao extends BaseDao {

	private Logger logger = Logger.getLogger(TaxRateMappingDao.class);
	@Autowired
	ChartOfAccountsDao chartOfAccountsDao;


	public TaxRateMappingVo createTaxRate(TaxRateMappingVo taxRateVo) throws ApplicationException {
		logger.info("Entry into method: createTaxRate");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = getUserMgmConnection();
			boolean isTaxRateExist=checkTaxRateExist(taxRateVo.getName(),taxRateVo.getOrganizationId(),con);
			if(isTaxRateExist){
			throw new Exception("Tax Rate Exist for the Organization");
			}
			String sql = SettingsAndPreferencesConstants.INSERT_INTO_TAX_RATE_MAPPING_ORGANIZATION;
			preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			//(name,tax_rate_type_id,rate,is_base,organization_id,user_id,isSuperAdmin,is_inter)
			preparedStatement.setString(1, taxRateVo.getName());
			preparedStatement.setInt(2, taxRateVo.getTaxRateTypeId());
			preparedStatement.setString(3, taxRateVo.getRate());
			preparedStatement.setBoolean(4, taxRateVo.isBase());
			preparedStatement.setLong(5, taxRateVo.getOrganizationId());
			preparedStatement.setString(6, taxRateVo.getUserId());
			preparedStatement.setBoolean(7, taxRateVo.isSuperAdmin());
			preparedStatement.setBoolean(8, taxRateVo.isInter());
			preparedStatement.setString(9, taxRateVo.getRoleName());
			int rowAffected = preparedStatement.executeUpdate();
			if (rowAffected == 1) {
				rs = preparedStatement.getGeneratedKeys();
				if (rs.next()) {
					taxRateVo.setId(rs.getInt(1));
				}
			}
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return taxRateVo;
	}

	public TaxRateMappingVo updateTaxRate(TaxRateMappingVo taxRateVo) throws ApplicationException {
		logger.info("Entry into method: updateTaxRate");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = getUserMgmConnection();
			String sql = SettingsAndPreferencesConstants.UPDATE_TAX_RATE_ORGANIZATION;
			preparedStatement = con.prepareStatement(sql);
			preparedStatement.setString(1, taxRateVo.getName());
			preparedStatement.setLong(2, taxRateVo.getTaxRateTypeId());
			preparedStatement.setString(3, taxRateVo.getRate());
			preparedStatement.setBoolean(4, taxRateVo.isBase());
			preparedStatement.setString(5, taxRateVo.getStatus());
			preparedStatement.setInt(6, taxRateVo.getOrganizationId());
			preparedStatement.setString(7, taxRateVo.getUserId());
			preparedStatement.setBoolean(8, taxRateVo.isSuperAdmin());
			preparedStatement.setTimestamp(9, new Timestamp(System.currentTimeMillis()));
			preparedStatement.setBoolean(10, taxRateVo.isInter());
			preparedStatement.setString(11, taxRateVo.getRoleName());
			preparedStatement.setInt(12, taxRateVo.getId());
			
			preparedStatement.executeUpdate();
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return taxRateVo;
	}

	public TaxRateMappingVo deleteTaxRate(int id,String status,String userId,String roleName) throws ApplicationException {
		logger.info("Entry into method: deleteTaxRate");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		
		TaxRateMappingVo taxRateVo = new TaxRateMappingVo();
		try {
			con = getUserMgmConnection();
			String sql = SettingsAndPreferencesConstants.CHANGE_TAX_RATE_STATUS_ORGANIZATION;
			preparedStatement = con.prepareStatement(sql);
			preparedStatement.setString(1, status);
			preparedStatement.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
			preparedStatement.setString(3, userId);
			preparedStatement.setString(4, roleName);
			preparedStatement.setInt(5, id);
			taxRateVo.setId(id);
			preparedStatement.executeUpdate();
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return taxRateVo;
	}

	public List<TaxRateMappingVo> getAllTaxRatesOfAnOrganization(int organizationId) throws ApplicationException {
		logger.info("Entry into method: getAllTaxRatesOfAnOrganization");
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		List<TaxRateMappingVo> listTaxRates = new ArrayList<TaxRateMappingVo>();
		try {
			con = getUserMgmConnection();
			String query = SettingsAndPreferencesConstants.GET_TAX_RATE_ORGANIZATION;
			preparedStatement = con.prepareStatement(query);
			//id,name,tax_rate_type_id,rate,is_base,status,organization_id,user_id,isSuperAdmin,create_ts
			preparedStatement.setInt(1, organizationId);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				TaxRateMappingVo taxRateVo = new TaxRateMappingVo();
				taxRateVo.setId(rs.getInt(1));
				taxRateVo.setName(rs.getString(2));
				taxRateVo.setTaxRateTypeId(rs.getInt(3));
				taxRateVo.setRate(rs.getString(4));
				taxRateVo.setBase(rs.getBoolean(5));
				taxRateVo.setStatus(rs.getString(6));
				taxRateVo.setOrganizationId(rs.getInt(7));
				taxRateVo.setUserId(rs.getString(8));
				taxRateVo.setSuperAdmin(rs.getBoolean(9));
				taxRateVo.setCreateTs(rs.getTimestamp(10));
				taxRateVo.setTaxRateTypeName(rs.getString(11));
				listTaxRates.add(taxRateVo);
			}
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		
		return listTaxRates;
	}
	
	
	public List<TaxRateMappingVo> getAllTaxRatesOfAnOrganizationByUserAndRole(int organizationId,String userId,String roleName) throws ApplicationException {
		logger.info("Entry into method: getAllTaxRatesOfAnOrganizationByUserAndRole");
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		List<TaxRateMappingVo> listTaxRates = new ArrayList<TaxRateMappingVo>();
		try {
			con = getUserMgmConnection();
			String query="";
			if(roleName.equals("Super Admin")){
				query = SettingsAndPreferencesConstants.GET_TAX_RATE_ORGANIZATION;
			}else{
				query = SettingsAndPreferencesConstants.GET_TAX_RATE_ORGANIZATION_USER_ROLE;
			}
			preparedStatement = con.prepareStatement(query);
			//id,name,tax_rate_type_id,rate,is_base,status,organization_id,user_id,isSuperAdmin,create_ts
			preparedStatement.setInt(1, organizationId);
			if(!(roleName.equals("Super Admin"))){
				preparedStatement.setString(2, userId);
				preparedStatement.setString(3, roleName);
			}	
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				TaxRateMappingVo taxRateVo = new TaxRateMappingVo();
				taxRateVo.setId(rs.getInt(1));
				taxRateVo.setName(rs.getString(2));
				taxRateVo.setTaxRateTypeId(rs.getInt(3));
				taxRateVo.setRate(rs.getString(4));
				taxRateVo.setBase(rs.getBoolean(5));
				taxRateVo.setStatus(rs.getString(6));
				taxRateVo.setOrganizationId(rs.getInt(7));
				taxRateVo.setUserId(rs.getString(8));
				taxRateVo.setSuperAdmin(rs.getBoolean(9));
				taxRateVo.setCreateTs(rs.getTimestamp(10));
				taxRateVo.setTaxRateTypeName(rs.getString(11));
				listTaxRates.add(taxRateVo);
			}
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		
		return listTaxRates;
	}

	private boolean checkTaxRateExist(String name,int organizationId,Connection conn) throws ApplicationException {
		logger.info("Entry into method: checkTaxRateExist");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			String query = SettingsAndPreferencesConstants.CHECK_TAX_RATE_ORGANIZATION;
			preparedStatement = conn.prepareStatement(query);
			preparedStatement.setInt(1, organizationId);
			preparedStatement.setString(2, name);
			rs = preparedStatement.executeQuery();
			if (rs.next()) {
				return true;
			}
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, null);
		}
		return false;
	}
	
	public TaxRateMappingVo getTaxRateMappingById(int id) throws ApplicationException {
		logger.info("Entry into method: getTaxRateMappingById");
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		TaxRateMappingVo taxRateVo = new TaxRateMappingVo();
		try {
			con = getUserMgmConnection();
			String query = SettingsAndPreferencesConstants.GET_TAX_RATE_BY_ID_ORGANIZATION;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, id);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				
				taxRateVo.setId(rs.getInt(1));
				taxRateVo.setName(rs.getString(2));
				taxRateVo.setTaxRateTypeId(rs.getInt(3));
				taxRateVo.setRate(rs.getString(4));
				taxRateVo.setBase(rs.getBoolean(5));
				taxRateVo.setStatus(rs.getString(6));
				taxRateVo.setOrganizationId(rs.getInt(7));
				taxRateVo.setUserId(rs.getString(8));
				taxRateVo.setSuperAdmin(rs.getBoolean(9));
				taxRateVo.setCreateTs(rs.getTimestamp(10));
				taxRateVo.setTaxRateTypeName(rs.getString(11));
				String taxRate=taxRateVo.getRate();
				if(taxRate.contains("%")){
					taxRateVo.setRate(taxRate.replaceAll("%",""));
				}
			}
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return taxRateVo;
	}
	public TaxRateMappingVo getTaxRateMappingByName(int organizationId,String name) throws ApplicationException {
		logger.info("Entry into method: getTaxRateMappingByName");
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		TaxRateMappingVo taxRateVo = null;
		try {
			con = getUserMgmConnection();
			String query = SettingsAndPreferencesConstants.GET_TAX_RATE_BY_NAME_ORGANIZATION;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, organizationId);
			preparedStatement.setString(2, name);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				taxRateVo = new TaxRateMappingVo();
				taxRateVo.setId(rs.getInt(1));
				taxRateVo.setName(rs.getString(2));
				taxRateVo.setTaxRateTypeId(rs.getInt(3));
				taxRateVo.setRate(rs.getString(4));
				taxRateVo.setBase(rs.getBoolean(5));
				taxRateVo.setStatus(rs.getString(6));
				taxRateVo.setOrganizationId(rs.getInt(7));
				taxRateVo.setUserId(rs.getString(8));
				taxRateVo.setSuperAdmin(rs.getBoolean(9));
				taxRateVo.setCreateTs(rs.getTimestamp(10));
				taxRateVo.setTaxRateTypeName(rs.getString(11));
			}
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return taxRateVo;
	}
	
	/*public void insertIntoChartOfAccountsLevel6(int organizationId,String name,String userId,boolean isSuperAdmin,String displayName)throws ApplicationException{
		
		int level5Id=chartOfAccountsDao.getChartOfAccountsLevel5Id(organizationId, "Short-term loans and advances", "Balances with government authorities", "GST Refund Receivable");
		chartOfAccountsDao.createLevel6(name, "GST", organizationId, userId, true, level5Id,true,displayName);
		
		level5Id=chartOfAccountsDao.getChartOfAccountsLevel5Id(organizationId, "Other Current Assets", "Others", "Input GST");
		chartOfAccountsDao.createLevel6(name, "GST", organizationId, userId, true, level5Id,true,displayName);
		
		level5Id=chartOfAccountsDao.getChartOfAccountsLevel5Id(organizationId, "Other Current Assets", "Others", "GST Refund");
		chartOfAccountsDao.createLevel6(name, "GST", organizationId, userId, true, level5Id,true,displayName);

		level5Id=chartOfAccountsDao.getChartOfAccountsLevel5Id(organizationId, "Long-term loans and advances", "Balances with government authorities", "NC - GST Input Credit");
		chartOfAccountsDao.createLevel6(name, "GST", organizationId, userId, true, level5Id,true,displayName);
		
		level5Id=chartOfAccountsDao.getChartOfAccountsLevel5Id(organizationId, "Long-term provisions", "Provision - Others", "NC - Provision for doubtful GST refund");
		chartOfAccountsDao.createLevel6(name, "GST", organizationId, userId, true, level5Id,true,displayName);
		
		level5Id=chartOfAccountsDao.getChartOfAccountsLevel5Id(organizationId, "Other current liabilities", "Statutory remittances", "GST Payable");
		chartOfAccountsDao.createLevel6(name, "GST", organizationId, userId, true, level5Id,true,displayName);
		
		level5Id=chartOfAccountsDao.getChartOfAccountsLevel5Id(organizationId, "Other current liabilities", "Statutory remittances", "GST-RCM Payable");
		chartOfAccountsDao.createLevel6(name, "GST", organizationId, userId, true, level5Id,true,displayName);
		
		level5Id=chartOfAccountsDao.getChartOfAccountsLevel5Id(organizationId, "Other current liabilities", "Output GST", "Output GST Control A/c");
		chartOfAccountsDao.createLevel6(name, "GST", organizationId, userId, true, level5Id,true,displayName);

		level5Id=chartOfAccountsDao.getChartOfAccountsLevel5Id(organizationId, "Short-term provisions", "Provision - Others", "Provision for doubtful GST refund");
		chartOfAccountsDao.createLevel6(name, "GST", organizationId, userId, true, level5Id,true,displayName);
		
	}
*/


}

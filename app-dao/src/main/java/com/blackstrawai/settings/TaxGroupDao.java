package com.blackstrawai.settings;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.ap.dropdowns.TaxRateVo;
import com.blackstrawai.common.BaseDao;

@Repository
public class TaxGroupDao extends BaseDao {


	private Logger logger = Logger.getLogger(TaxGroupDao.class);

	public TaxGroupVo createTaxGroup(TaxGroupVo taxGroupVo) throws ApplicationException {
		logger.info("Entry into method: createTaxGroup");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = getUserMgmConnection();
			boolean isTaxGroupExist=checkTaxGroupExist(taxGroupVo.getName(),taxGroupVo.getOrganizationId(),con);
			if(isTaxGroupExist){
				throw new Exception("Tax Group Exist for the Organization");
			}
			String sql = SettingsAndPreferencesConstants.INSERT_INTO_TAX_GROUP_ORGANIZATION;
			preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setString(1, taxGroupVo.getName());
			preparedStatement.setString(2, taxGroupVo.getTaxesIncluded());
			preparedStatement.setString(3, taxGroupVo.getCombinedRate());
			preparedStatement.setBoolean(4, taxGroupVo.isInter());
			preparedStatement.setBoolean(5, taxGroupVo.isBase());
			preparedStatement.setLong(6, taxGroupVo.getOrganizationId());
			preparedStatement.setString(7, taxGroupVo.getUserId());
			preparedStatement.setBoolean(8, taxGroupVo.isSuperAdmin());
			preparedStatement.setString(9, taxGroupVo.getRoleName());
			int rowAffected = preparedStatement.executeUpdate();
			if (rowAffected == 1) {
				rs = preparedStatement.getGeneratedKeys();
				if (rs.next()) {
					taxGroupVo.setId(rs.getInt(1));
				}
			}
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return taxGroupVo;
	}

	public TaxGroupVo updateTaxGroup(TaxGroupVo taxGroupVo) throws ApplicationException {
		logger.info("Entry into method: updateTaxGroup");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = getUserMgmConnection();
			String sql = SettingsAndPreferencesConstants.UPDATE_TAX_GROUP_ORGANIZATION;
			preparedStatement = con.prepareStatement(sql);
			preparedStatement.setString(1, taxGroupVo.getName());
			preparedStatement.setString(2, taxGroupVo.getTaxesIncluded());
			preparedStatement.setString(3, taxGroupVo.getCombinedRate());
			preparedStatement.setBoolean(4, taxGroupVo.isInter());
			preparedStatement.setBoolean(5, taxGroupVo.isBase());
			preparedStatement.setString(6, taxGroupVo.getStatus());
			preparedStatement.setInt(7, taxGroupVo.getOrganizationId());
			preparedStatement.setString(8, taxGroupVo.getUserId());
			preparedStatement.setBoolean(9, taxGroupVo.isSuperAdmin());
			preparedStatement.setTimestamp(10, new Timestamp(System.currentTimeMillis()));
			preparedStatement.setString(11,taxGroupVo.getRoleName());
			preparedStatement.setInt(12, taxGroupVo.getId());

			preparedStatement.executeUpdate();
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return taxGroupVo;
	}

	public TaxGroupVo deleteTaxGroup(int id,String status,String userId,String roleName) throws ApplicationException {
		logger.info("Entry into method: deleteTaxGroup");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;

		TaxGroupVo taxGroupVo = new TaxGroupVo();
		try {
			con = getUserMgmConnection();
			String sql = SettingsAndPreferencesConstants.CHANGE_TAX_GROUP_STATUS_ORGANIZATION;
			preparedStatement = con.prepareStatement(sql);
			preparedStatement.setString(1, status);
			preparedStatement.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
			preparedStatement.setString(3, userId);
			preparedStatement.setString(4, roleName);
			preparedStatement.setInt(5, id);
			taxGroupVo.setId(id);
			preparedStatement.executeUpdate();
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return taxGroupVo;
	}

	public List<TaxGroupVo> getAllTaxGroupsOfAnOrganization(int organizationId) throws ApplicationException {
		logger.info("Entry into method: getAllTaxGroupsOfAnOrganization");
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		List<TaxGroupVo> listTaxGroups = new ArrayList<TaxGroupVo>();
		try {
			con = getUserMgmConnection();
			String query = SettingsAndPreferencesConstants.GET_TAX_GROUP_BY_ORGANIZATION;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, organizationId);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				TaxGroupVo taxGroupVo = new TaxGroupVo();
				taxGroupVo.setId(rs.getInt(1));
				taxGroupVo.setName(rs.getString(2));
				taxGroupVo.setTaxesIncluded(rs.getString(3));
				taxGroupVo.setCombinedRate(rs.getString(4));
				taxGroupVo.setInter(rs.getBoolean(5));		
				taxGroupVo.setBase(rs.getBoolean(6));
				taxGroupVo.setStatus(rs.getString(7));
				taxGroupVo.setOrganizationId(rs.getInt(8));
				taxGroupVo.setUserId(rs.getString(9));
				taxGroupVo.setSuperAdmin(rs.getBoolean(10));
				taxGroupVo.setCreateTs(rs.getTimestamp(11));
				if(!(taxGroupVo.getCombinedRate().contains("%"))){
					taxGroupVo.setCombinedRate(taxGroupVo.getCombinedRate()+"%");
				}
				listTaxGroups.add(taxGroupVo);
			}
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return listTaxGroups;
	}
	
	public List<TaxGroupVo> getAllTaxGroupsOfAnOrganizationByUserAndRole(int organizationId,String userId,String roleName) throws ApplicationException {
		logger.info("Entry into method: getAllTaxGroupsOfAnOrganizationByUserAndRole");
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		List<TaxGroupVo> listTaxGroups = new ArrayList<TaxGroupVo>();
		try {
			con = getUserMgmConnection();
			String query="";
			if(roleName.equals("Super Admin")){
				query = SettingsAndPreferencesConstants.GET_TAX_GROUP_BY_ORGANIZATION;
			}else{
				query = SettingsAndPreferencesConstants.GET_TAX_GROUP_BY_ORGANIZATION_USER_ROLE;
			}
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, organizationId);
			if(!(roleName.equals("Super Admin"))){
				preparedStatement.setString(2, userId);
				preparedStatement.setString(3, roleName);
			}	
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				TaxGroupVo taxGroupVo = new TaxGroupVo();
				taxGroupVo.setId(rs.getInt(1));
				taxGroupVo.setName(rs.getString(2));
				taxGroupVo.setTaxesIncluded(rs.getString(3));
				taxGroupVo.setCombinedRate(rs.getString(4));
				taxGroupVo.setInter(rs.getBoolean(5));		
				taxGroupVo.setBase(rs.getBoolean(6));
				taxGroupVo.setStatus(rs.getString(7));
				taxGroupVo.setOrganizationId(rs.getInt(8));
				taxGroupVo.setUserId(rs.getString(9));
				taxGroupVo.setSuperAdmin(rs.getBoolean(10));
				taxGroupVo.setCreateTs(rs.getTimestamp(11));
				if(!(taxGroupVo.getCombinedRate().contains("%"))){
					taxGroupVo.setCombinedRate(taxGroupVo.getCombinedRate()+"%");
				}
				listTaxGroups.add(taxGroupVo);
			}
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return listTaxGroups;
	}

	private boolean checkTaxGroupExist(String name,int organizationId,Connection conn) throws ApplicationException {
		logger.info("Entry into method: checkTaxGroupExist");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			String query = SettingsAndPreferencesConstants.CHECK_TAX_GROUP_ORGANIZATION;
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

	public TaxGroupVo getTaxGroupById(int id) throws ApplicationException {
		logger.info("Entry into method: getTaxRateMappingById");
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		TaxGroupVo taxGroupVo = new TaxGroupVo();
		try {
			con = getUserMgmConnection();
			String query = SettingsAndPreferencesConstants.GET_TAX_GROUP_BY_ID_ORGANIZATION;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, id);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				taxGroupVo.setId(rs.getInt(1));
				taxGroupVo.setName(rs.getString(2));
				taxGroupVo.setTaxesIncluded(rs.getString(3));
				taxGroupVo.setCombinedRate(rs.getString(4));
				taxGroupVo.setInter(rs.getBoolean(5));
				taxGroupVo.setBase(rs.getBoolean(6));
				taxGroupVo.setStatus(rs.getString(7));
				taxGroupVo.setOrganizationId(rs.getInt(8));
				taxGroupVo.setUserId(rs.getString(9));
				taxGroupVo.setSuperAdmin(rs.getBoolean(10));				
				taxGroupVo.setCreateTs(rs.getTimestamp(11));
				if(!(taxGroupVo.getCombinedRate().contains("%"))){
					taxGroupVo.setCombinedRate(taxGroupVo.getCombinedRate()+"%");
				}
			}
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return taxGroupVo;
	}

	public List<TaxGroupVo> getTaxGroupForProduct(int organizationId,boolean isInter) throws ApplicationException {
		logger.info("Entry into method: getTaxRateMappingById");
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		List<TaxGroupVo>data=new ArrayList<TaxGroupVo>();
		try {
			con = getUserMgmConnection();
			String query = SettingsAndPreferencesConstants.GET_INTER_TAX_GROUP_ORGANIZATION;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, organizationId);
			preparedStatement.setString(2, "DEL");
			preparedStatement.setBoolean(3, isInter);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				TaxGroupVo taxGroupVo = new TaxGroupVo();
				taxGroupVo.setId(rs.getInt(1));
				taxGroupVo.setName(rs.getString(2));
				taxGroupVo.setTaxesIncluded(rs.getString(3));
				taxGroupVo.setCombinedRate(rs.getString(4));			
				data.add(taxGroupVo);
			}
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return data;
	}

	// to get the Tax group and its split for each organization, This will be used in the tax calculation 
	public List<TaxGroupVo> getTaxGroupForOrganization(Integer organizationId) throws ApplicationException {
		logger.info("Entry into method:: getTaxGroupForOrganization");
		List<TaxGroupVo> taxDetailsList= new ArrayList<TaxGroupVo>();
		Connection con = null;
		PreparedStatement preparedStatement =null;
		ResultSet rs =null;
		try
		{
			con = getUserMgmConnection();
			preparedStatement = con.prepareStatement(SettingsAndPreferencesConstants.GET_TAX_GROUP_ORGANIZATION);
			preparedStatement.setInt(1,organizationId);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				TaxGroupVo taxGroupVo= new TaxGroupVo();
				taxGroupVo.setId(rs.getInt(1));
				taxGroupVo.setName(rs.getString(2));
				taxGroupVo.setTaxesIncluded(rs.getString(3));
				taxGroupVo.setCombinedRate(rs.getString(4));
				taxDetailsList.add(taxGroupVo);
			}
		}catch(Exception e){

			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return taxDetailsList;


	}

	public List<TaxRateVo> getTaxGroups(Integer organizationId,Connection con) throws ApplicationException{
		logger.info("To get  getTaxGroups");
		List<TaxRateVo> taxRateVos =null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try{
			preparedStatement =  con.prepareStatement(SettingsAndPreferencesConstants.GET_ALL_TAX_RATE_GROUP_ORGANIZATION);
			preparedStatement.setInt(1,organizationId);
			rs = preparedStatement.executeQuery();	
			taxRateVos = new ArrayList<TaxRateVo>();
			while (rs.next()) {
				TaxRateVo taxRateVo = new TaxRateVo();
				taxRateVo.setId(rs.getInt(1));
				taxRateVo.setName(rs.getString(2));
				taxRateVo.setCombinedRate(rs.getInt(3));
				taxRateVo.setIsInter(rs.getBoolean(4));
				taxRateVo.setValue(rs.getInt(1));
				taxRateVos.add(taxRateVo);
			}
		}catch(Exception e){
			logger.info("Error in  getTaxGroups",e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, null);
		}
		return taxRateVos;

	}


	//To get Minimal Tax rates 
	public Map<String , String> getTaxRatesMappingForOrganization(Integer orgId) throws ApplicationException{
		Map<String , String> taxrateMap = null;
		logger.info("To get  getTaxRatesMappingForOrganization");
		Connection con =null;
		PreparedStatement preparedStatement =null;
		ResultSet rs =null;
		try{
			con = getUserMgmConnection();
			preparedStatement =  con.prepareStatement(SettingsAndPreferencesConstants.GET_MINIMAL_TAX_RATE_FOR_ORG);
			preparedStatement.setInt(1,orgId);
			rs = preparedStatement.executeQuery();		
			taxrateMap = new HashMap<String, String>();
			while (rs.next()) {
				taxrateMap.put(rs.getString(1), rs.getString(2).replace("%", ""));
			}
			logger.info("getTaxRatesMappingForOrganization ::"+taxrateMap.size());
		}catch(Exception e){
			logger.info("Error in  getTaxGroups",e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return taxrateMap;
	}

	
	

	//To get Minimal Tax rates 
	public Map<String , String> getTaxRatesAndTypeMappingForOrganization(Integer orgId) throws ApplicationException{
		Map<String , String> taxrateMap = null;
		logger.info("To get  getTaxRatesAntTypeMappingForOrganization");
		Connection con =null;
		PreparedStatement preparedStatement =null;
		ResultSet rs =null;
		try{
			con = getUserMgmConnection();
			preparedStatement =  con.prepareStatement(SettingsAndPreferencesConstants.GET_TAX_TYPE_BY_TAX_RATE);
			preparedStatement.setInt(1,orgId);
			rs = preparedStatement.executeQuery();		
			taxrateMap = new HashMap<String, String>();
			while (rs.next()) {
				taxrateMap.put(rs.getString(1), rs.getString(2)+"~"+rs.getString(3).replace("%", ""));
			}
			logger.info("getTaxRatesAntTypeMappingForOrganization ::"+taxrateMap.size());
		}catch(Exception e){
			logger.info("Error in  getTaxGroups",e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return taxrateMap;
	}

}

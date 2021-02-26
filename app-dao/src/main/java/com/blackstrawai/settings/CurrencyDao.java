package com.blackstrawai.settings;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.ap.dropdowns.BasicCurrencyVo;
import com.blackstrawai.common.BaseDao;
import com.blackstrawai.onboarding.OrganizationConstants;

@Repository
public class CurrencyDao extends BaseDao{


	private  Logger logger = Logger.getLogger(CurrencyDao.class);	

	public CurrencyVo createCurrency(CurrencyVo currencyVo)throws ApplicationException{
		logger.info("Entry into method:createCurrency");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		try {		
			con = getUserMgmConnection();			
			boolean isCurrencyExist=checkCurrencyExist(currencyVo.getName(),currencyVo.getOrganizationId(),con);
			if(isCurrencyExist){
				throw new Exception("Currency Exist for the Organization");
			}

			String sql = SettingsAndPreferencesConstants.INSERT_INTO_CURRENCY_ORGANIZATION;

			preparedStatement = con.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setString(1,currencyVo.getName());
			preparedStatement.setString(2,currencyVo.getDescription());
			preparedStatement.setString(3, currencyVo.getSymbol());
			preparedStatement.setString(4, currencyVo.getAlternateSymbol());
			preparedStatement.setBoolean(5, currencyVo.isSpaceRequired());
			preparedStatement.setBoolean(6, currencyVo.isMillions());
			preparedStatement.setInt(7, currencyVo.getNoOfDecimalPlaces());
			preparedStatement.setString(8, currencyVo.getDecimalValueDenoter());
			preparedStatement.setInt(9, currencyVo.getNoOfDecimalsForAmount());
			preparedStatement.setString(10, currencyVo.getExchangeValue());
			preparedStatement.setTimestamp(11,new Timestamp(System.currentTimeMillis()));
			preparedStatement.setInt(12, currencyVo.getOrganizationId());
			preparedStatement.setInt(13, Integer.parseInt(currencyVo.getUserId()));
			preparedStatement.setBoolean(14, currencyVo.isSuperAdmin());
			int rowAffected = preparedStatement.executeUpdate();	
			if (rowAffected == 1) {
				rs = preparedStatement.getGeneratedKeys();
				if (rs.next()){
					currencyVo.setId(rs.getInt(1));
				}
			}		
		}catch(Exception e){
			throw new ApplicationException(e);
		}finally {
			closeResources(rs, preparedStatement, con);

		}	

		return currencyVo;
	}	


	public CurrencyVo updateCurrency(CurrencyVo currencyVo)throws ApplicationException{
		logger.info("Entry into method:updateCurrency");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		try {		
			con = getUserMgmConnection();			
			/*	boolean isCurrencyExist=checkCurrencyExist(currencyVo.getName(),currencyVo.getOrganizationId(),con);
			if(isCurrencyExist){
				throw new Exception("Currency Exist for the Organization");
			}			*/
			String sql = SettingsAndPreferencesConstants.UPDATE_CURRENCY_ORGANIZATION;
			preparedStatement = con.prepareStatement(sql);
			preparedStatement.setString(1,currencyVo.getName());
			preparedStatement.setString(2,currencyVo.getDescription());
			preparedStatement.setString(3, currencyVo.getSymbol());
			preparedStatement.setString(4, currencyVo.getAlternateSymbol());
			preparedStatement.setBoolean(5, currencyVo.isSpaceRequired());
			preparedStatement.setBoolean(6, currencyVo.isMillions());
			preparedStatement.setInt(7, currencyVo.getNoOfDecimalPlaces());
			preparedStatement.setString(8, currencyVo.getDecimalValueDenoter());
			preparedStatement.setInt(9, currencyVo.getNoOfDecimalsForAmount());
			preparedStatement.setString(10, currencyVo.getExchangeValue());
			preparedStatement.setTimestamp(11,new Timestamp(System.currentTimeMillis()));
			preparedStatement.setInt(12, currencyVo.getId());
			preparedStatement.executeUpdate();	

		}catch(Exception e){
			throw new ApplicationException(e);
		}finally {
			closeResources(rs, preparedStatement, con);

		}	

		return currencyVo;
	}	

	private boolean checkCurrencyExist(String name, int organizationId,Connection con)throws ApplicationException {	
		logger.info("Entry into method:checkCurrencyExist");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try{
			String query=SettingsAndPreferencesConstants.CHECK_CURRENCY_EXIST_ORGANIZATION;
			preparedStatement=con.prepareStatement(query);
			preparedStatement.setInt(1,organizationId);	
			preparedStatement.setString(2,name);
			preparedStatement.setString(3,"DEL");
			rs = preparedStatement.executeQuery();			
			if (rs.next()) {
				return true;				
			}
		}catch(Exception e){
			throw new ApplicationException(e);
		}finally {
			closeResources(rs, preparedStatement, null);

		}	
		return false;
	}	


	public CurrencyVo deleteCurrency(int currencyId,String status)throws ApplicationException {	
		logger.info("Entry into method:deleteCurrency");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		CurrencyVo currencyVo=new CurrencyVo();
		try {			
			con = getUserMgmConnection();			
			String sql = SettingsAndPreferencesConstants.DELETE_CURRENCY_ORGANIZATION;			
			preparedStatement = con.prepareStatement(sql);
			preparedStatement.setString(1, status);
			preparedStatement.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
			preparedStatement.setInt(3, currencyId);
			currencyVo.setId(currencyId);
			preparedStatement.executeUpdate();			
		}catch(Exception e){
			throw new ApplicationException(e);
		}finally {
			closeResources(rs, preparedStatement, con);

		}	

		return currencyVo;
	}	


	public List<CurrencyVo> getAllCurrenciesOfAnOrganization(int organizationId,String organizationName)throws ApplicationException {	
		logger.info("Entry into method:getAllCurrenciesOfAnOrganization");
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		List<CurrencyVo>listAllCurrencies=new ArrayList<CurrencyVo>();
		try{
			con = getUserMgmConnection();
			String query=SettingsAndPreferencesConstants.GET_CURRENCIES_ORGANIZATION;		

			preparedStatement=con.prepareStatement(query);
			preparedStatement.setInt(1,organizationId);
			preparedStatement.setString(2, "DEL");
			rs = preparedStatement.executeQuery();			
			while (rs.next()) {
				CurrencyVo currencyVo=new CurrencyVo();
				currencyVo.setId(rs.getInt(1));
				currencyVo.setName(rs.getString(2));
				currencyVo.setDescription(rs.getString(3));
				currencyVo.setSymbol(rs.getString(4));
				currencyVo.setAlternateSymbol(rs.getString(5));
				currencyVo.setSpaceRequired(rs.getBoolean(6));
				currencyVo.setMillions(rs.getBoolean(7));
				currencyVo.setNoOfDecimalPlaces(rs.getInt(8));
				currencyVo.setDecimalValueDenoter(rs.getString(9));
				currencyVo.setNoOfDecimalsForAmount(rs.getInt(10));
				currencyVo.setExchangeValue(rs.getString(11));
				currencyVo.setCreateTs(rs.getTimestamp(12));
				currencyVo.setUpdateTs(rs.getTimestamp(13));
				currencyVo.setOrganizationId(rs.getInt(14));
				currencyVo.setUserId(new Integer(rs.getInt(15)).toString());
				currencyVo.setSuperAdmin(rs.getBoolean(16));
				currencyVo.setStatus(rs.getString(17));
				listAllCurrencies.add(currencyVo);
			}
		}catch(Exception e){
			throw new ApplicationException(e);
		}finally {
			closeResources(rs, preparedStatement, con);

		}	
		return listAllCurrencies;
	}

	public CurrencyVo getCurrency(int currencyId)throws ApplicationException {	
		logger.info("Entry into method:getCurrency");
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		CurrencyVo currencyVo=null;
		try{
			con = getUserMgmConnection();
			String query=SettingsAndPreferencesConstants.GET_CURRENCY_ORGANIZATION;					
			preparedStatement=con.prepareStatement(query);
			preparedStatement.setInt(1,currencyId);
			rs = preparedStatement.executeQuery();			
			while (rs.next()) {
				currencyVo=new CurrencyVo();
				currencyVo.setId(rs.getInt(1));
				currencyVo.setName(rs.getString(2));
				currencyVo.setDescription(rs.getString(3));
				currencyVo.setSymbol(rs.getString(4));
				currencyVo.setAlternateSymbol(rs.getString(5));
				currencyVo.setSpaceRequired(rs.getBoolean(6));
				currencyVo.setMillions(rs.getBoolean(7));
				currencyVo.setNoOfDecimalPlaces(rs.getInt(8));
				currencyVo.setDecimalValueDenoter(rs.getString(9));
				currencyVo.setNoOfDecimalsForAmount(rs.getInt(10));
				currencyVo.setExchangeValue(rs.getString(11));
				currencyVo.setCreateTs(rs.getTimestamp(12));
				currencyVo.setUpdateTs(rs.getTimestamp(13));
				currencyVo.setOrganizationId(rs.getInt(14));
				currencyVo.setUserId(new Integer(rs.getInt(15)).toString());
				currencyVo.setSuperAdmin(rs.getBoolean(16));
				currencyVo.setStatus(rs.getString(17));
			}
		}catch(Exception e){
			throw new ApplicationException(e);
		}finally {
			closeResources(rs, preparedStatement, con);

		}	
		return currencyVo;
	}


	public List<BasicCurrencyVo> getMinimalCurrencyDetails(int organizationId,Connection con)throws ApplicationException{
		logger.info("Entry into method:getMinimalCurrencyDetails");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		List<BasicCurrencyVo>currencyList=new ArrayList<BasicCurrencyVo>();
		try
		{
			String query=SettingsAndPreferencesConstants.GET_MINIMAL_CURRENCY_ORGANIZATION;
			preparedStatement=con.prepareStatement(query);
			preparedStatement.setInt(1,organizationId);
			preparedStatement.setString(2, "DEL");
			rs = preparedStatement.executeQuery();			
			while (rs.next()) {
				BasicCurrencyVo data=new BasicCurrencyVo();
				data.setId(rs.getInt(1));
				data.setName(rs.getString(2));
				data.setSymbol(rs.getString(3));
				data.setAlternateSymbol(rs.getString(4));
				data.setValue(rs.getInt(1));
				data.setDecimalValueDenoter(rs.getString(5));
				currencyList.add(data);				
			}
		}catch(Exception e){

			throw new ApplicationException(e);
		}finally {
			closeResources(rs, preparedStatement, null);

		}	
		return currencyList;

	}



	public BasicCurrencyVo getBasicCurrencyForOrganization(int organizationId,Connection con)throws ApplicationException{
		logger.info("Entry into method:getBasicCurrency");	
		return getDefaultCurrencyForOrganization(organizationId,con);		
	}


	public String getCurrencyName(int id,int organizationId) throws ApplicationException {
		logger.info("To get the getCurrencyName");
		String currencyName = null;
		Connection con =null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			con = getUserMgmConnection();
			preparedStatement = con.prepareStatement(SettingsAndPreferencesConstants.GET_CURRENCY_NAME); 
			preparedStatement.setInt(1, id);
			preparedStatement.setInt(2, organizationId);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				currencyName = rs.getString(1);
			}
		} catch (Exception e) {
			logger.info("Error in getCurrencyName", e);
			throw new ApplicationException(e);
		}finally {
			closeResources(rs, preparedStatement, con);

		}	
		return currencyName;
	}


	// to get the default currency from currency organization 
	public BasicCurrencyVo getDefaultCurrencyForOrganization(int organizationId,Connection con)throws ApplicationException{
		logger.info("Entry into method:getDefaultCurrencyForOrganization");
		BasicCurrencyVo data = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try
		{
			preparedStatement = con.prepareStatement(OrganizationConstants.GET_DEFAULT_CURRENCY_FROM_CURRENCY_ORGANIZATION);
			preparedStatement.setInt(1,organizationId);
			preparedStatement.setInt(2, organizationId);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				data=new BasicCurrencyVo();
				data.setId(rs.getInt(1));
				data.setName(rs.getString(2));
				data.setSymbol(rs.getString(3));
				data.setAlternateSymbol(rs.getString(4));
				data.setValue(rs.getInt(1));
			}
		}catch(Exception e){
			logger.info("Exception in method:getDefaultCurrencyForOrganization");
			throw new ApplicationException(e);
		}finally {
			closeResources(rs, preparedStatement, null);

		}	
		return data;


	}

	public String getCurrencyName(int currencyId) throws ApplicationException {
		logger.info("Entry into getCurrencyName");
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		String baseCurrencyName = null;
		try {
			con = getUserMgmConnection();
			String query = SettingsAndPreferencesConstants.GET_CURRENCY_NAME_GIVEN_CURRENCY_ID;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, currencyId);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				baseCurrencyName = rs.getString(1);
			}
			logger.info("findBaseCurrencyName:: " + baseCurrencyName);

		} catch (Exception e) {
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return baseCurrencyName;
	}

	public Integer getCurrencyId(String currency, Integer orgId) throws ApplicationException {
		logger.info("Enter into Method: getCurrencyId");
		Integer currencyId = null;
		Connection con =null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			con = getUserMgmConnection();
			preparedStatement = con.prepareStatement(SettingsAndPreferencesConstants.GET_CURRENCY_ID);
			preparedStatement.setInt(1, orgId);
			preparedStatement.setString(2, currency);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				currencyId = rs.getInt(1);
			}
		} catch (Exception e) {
			logger.info("Error in  getCurrencyId:", e);
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return currencyId;
	}
}

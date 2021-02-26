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
import com.blackstrawai.ap.dropdowns.BasicShippingPreferenceVo;
import com.blackstrawai.common.BaseDao;

@Repository
public class ShippingPreferenceDao extends BaseDao{

	private Logger logger = Logger.getLogger(ShippingPreferenceDao.class);

	public ShippingPreferenceVo createShippingPreference(ShippingPreferenceVo shippingPreferenceVo) throws ApplicationException {
		logger.info("Entry into method:createShippingPreference");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = getUserMgmConnection();

			boolean isShippingPreferenceExist=checkShippingPreferenceExist(shippingPreferenceVo.getName(),shippingPreferenceVo.getOrganizationId(),con);
			if(isShippingPreferenceExist){
				throw new Exception("Shipping Preference Exist for the Organization");
			}
			String sql = SettingsAndPreferencesConstants.INSERT_SHIPPING_PREFERENCES_ORGANIZATION;
			preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);	
			preparedStatement.setString(1, shippingPreferenceVo.getName());
			preparedStatement.setString(2,shippingPreferenceVo.getMode());
			preparedStatement.setString(3,shippingPreferenceVo.getDescription());
			preparedStatement.setInt(4, shippingPreferenceVo.getOrganizationId());
			preparedStatement.setInt(5,Integer.valueOf(shippingPreferenceVo.getUserId()));
			preparedStatement.setBoolean(6, shippingPreferenceVo.getIsSuperAdmin());
			preparedStatement.setString(7, shippingPreferenceVo.getRoleName());
			int rowAffected = preparedStatement.executeUpdate();
			if (rowAffected == 1) {
				rs = preparedStatement.getGeneratedKeys();
				if (rs.next()) {
					shippingPreferenceVo.setId(rs.getInt(1));
				}
			}
		}catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}

		return shippingPreferenceVo;
	}


	/*public List<ShippingPreferenceVo> getAllShippingPreferencesOfAnOrganization(int organizationId) throws ApplicationException {
		logger.info("Entry into method:getAllShippingPreferencesOfAnOrganization");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		List<ShippingPreferenceVo> listOfShippingPreferences=new ArrayList<ShippingPreferenceVo>();
		try {
			con = getUserMgmConnection();
			String query = SettingsAndPreferencesConstants.GET_SHIPPING_PREFERENCES_ORGANIZATION;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, organizationId);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				ShippingPreferenceVo shippingPreferenceVo = new ShippingPreferenceVo();
				shippingPreferenceVo.setId(rs.getInt(1));
				shippingPreferenceVo.setName(rs.getString(2));
				shippingPreferenceVo.setMode(rs.getString(3));
				shippingPreferenceVo.setDescription(rs.getString(4));
				shippingPreferenceVo.setOrganizationId(rs.getInt(5));
				shippingPreferenceVo.setUserId(rs.getString(6));
				shippingPreferenceVo.setIsSuperAdmin(rs.getBoolean(7));
				shippingPreferenceVo.setStatus(rs.getString(8));
				shippingPreferenceVo.setCreateTs(rs.getTimestamp(9));
				listOfShippingPreferences.add(shippingPreferenceVo);
			}
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);

		}
		return listOfShippingPreferences;
	}
	*/
	public List<ShippingPreferenceVo> getAllShippingPreferencesOfAnOrganizationForUserAndRole(int organizationId,String userId,String roleName) throws ApplicationException {
		logger.info("Entry into method:getAllShippingPreferencesOfAnOrganizationForUserAndRole");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		List<ShippingPreferenceVo> listOfShippingPreferences=new ArrayList<ShippingPreferenceVo>();
		try {
			con = getUserMgmConnection();
			String query="";
			if(roleName.equals("Super Admin")){
				 query = SettingsAndPreferencesConstants.GET_SHIPPING_PREFERENCES_ORGANIZATION;
			}else{
				 query = SettingsAndPreferencesConstants.GET_SHIPPING_PREFERENCES_ORGANIZATION_USER_ROLE;
			}
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, organizationId);
			if(!(roleName.equals("Super Admin"))){
				preparedStatement.setString(2, userId);
				preparedStatement.setString(3, roleName);
			}	
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				ShippingPreferenceVo shippingPreferenceVo = new ShippingPreferenceVo();
				shippingPreferenceVo.setId(rs.getInt(1));
				shippingPreferenceVo.setName(rs.getString(2));
				shippingPreferenceVo.setMode(rs.getString(3));
				shippingPreferenceVo.setDescription(rs.getString(4));
				shippingPreferenceVo.setOrganizationId(rs.getInt(5));
				shippingPreferenceVo.setUserId(rs.getString(6));
				shippingPreferenceVo.setIsSuperAdmin(rs.getBoolean(7));
				shippingPreferenceVo.setStatus(rs.getString(8));
				shippingPreferenceVo.setCreateTs(rs.getTimestamp(9));
				listOfShippingPreferences.add(shippingPreferenceVo);
			}
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);

		}
		return listOfShippingPreferences;
	}

	public ShippingPreferenceVo getShippingPreferenceById(int id) throws ApplicationException {

		logger.info("Entry into method:getShippingPreferenceById");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		ShippingPreferenceVo shippingPreferenceVo = new ShippingPreferenceVo();
		try {
			con = getUserMgmConnection();
			String query = SettingsAndPreferencesConstants.GET_SHIPPING_PREFERENCES_BY_ID_ORGANIZATION;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1,id);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				shippingPreferenceVo.setId(rs.getInt(1));
				shippingPreferenceVo.setName(rs.getString(2));
				shippingPreferenceVo.setMode(rs.getString(3));
				shippingPreferenceVo.setDescription(rs.getString(4));
				shippingPreferenceVo.setOrganizationId(rs.getInt(5));
				shippingPreferenceVo.setUserId(rs.getString(6));
				shippingPreferenceVo.setIsSuperAdmin(rs.getBoolean(7));
				shippingPreferenceVo.setStatus(rs.getString(8));
				shippingPreferenceVo.setCreateTs(rs.getTimestamp(9));
			}
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);

		}
		return shippingPreferenceVo;
	}


	public ShippingPreferenceVo deleteShippingPreference(int id,String status,String userId,String roleName) throws ApplicationException {

		logger.info("Entry into method:deleteShippingPreference");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		ShippingPreferenceVo shippingPreferencesVo=new ShippingPreferenceVo();
		try {
			con = getUserMgmConnection();
			String sql = SettingsAndPreferencesConstants.DELETE_SHIPPING_PREFERENCE_ORGANIZATION;
			preparedStatement = con.prepareStatement(sql);
			preparedStatement.setString(1, status);
			preparedStatement.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
			preparedStatement.setString(3, userId);
			preparedStatement.setString(4, roleName);
			preparedStatement.setInt(5, id);
			shippingPreferencesVo.setId(id);
			preparedStatement.executeUpdate();
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);

		}
		return shippingPreferencesVo;
	}

	public ShippingPreferenceVo updateShippingPreference(ShippingPreferenceVo shippingPreferenceVo) throws ApplicationException{

		logger.info("Entry into method:updateShippingPreference");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = getUserMgmConnection();
			String sql = SettingsAndPreferencesConstants.UPDATE_SHIPPING_PREFERENCE_ORGANIZATION;
			preparedStatement = con.prepareStatement(sql);
			preparedStatement.setString(1,shippingPreferenceVo.getName());
			preparedStatement.setString(2, shippingPreferenceVo.getDescription());
			preparedStatement.setString(3, shippingPreferenceVo.getMode());
			preparedStatement.setInt(4, shippingPreferenceVo.getOrganizationId());
			preparedStatement.setString(5, shippingPreferenceVo.getUserId());
			preparedStatement.setBoolean(6, shippingPreferenceVo.getIsSuperAdmin());
			preparedStatement.setString(7, shippingPreferenceVo.getStatus());
			preparedStatement.setTimestamp(8,new Timestamp(System.currentTimeMillis()));	
			preparedStatement.setString(9,shippingPreferenceVo.getRoleName());
			preparedStatement.setInt(10, shippingPreferenceVo.getId());
			preparedStatement.executeUpdate();
		}catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return shippingPreferenceVo;

	}
	
	private boolean checkShippingPreferenceExist(String name, int organizationId,Connection con)throws ApplicationException {
		logger.info("Entry into method:checkShippingPreferenceExist");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try{
		String query=SettingsAndPreferencesConstants.CHECK_SHIPPING_PREFERENCE_EXIST_ORGANIZATION;
		preparedStatement=con.prepareStatement(query);
		preparedStatement.setInt(1,organizationId);
		preparedStatement.setString(2,name);
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

	public List<BasicShippingPreferenceVo> getBasicShippingPreference(int organizationId,Connection con)throws ApplicationException{
		logger.info("Entry into method:getShippingPreference");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		List<BasicShippingPreferenceVo>shippingPreferenceList=new ArrayList<BasicShippingPreferenceVo>();
		try
		{
			String query=SettingsAndPreferencesConstants.GET_SHIPPING_PREFERENCE_ORGANIZATION;
			preparedStatement=con.prepareStatement(query);
			preparedStatement.setInt(1,organizationId);
			rs = preparedStatement.executeQuery();			
			while (rs.next()) {
				BasicShippingPreferenceVo data=new BasicShippingPreferenceVo();
				data.setId(rs.getInt(1));
				data.setName(rs.getString(2));
				data.setValue(rs.getInt(1));
				shippingPreferenceList.add(data);				
			}
		}catch(Exception e){

			throw new ApplicationException(e);
		}finally {
			closeResources(rs, preparedStatement, null);

		}	
		return shippingPreferenceList;


	}

}
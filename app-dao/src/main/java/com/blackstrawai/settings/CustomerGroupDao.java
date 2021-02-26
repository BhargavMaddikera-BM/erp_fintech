package com.blackstrawai.settings;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.ap.dropdowns.BasicCustomerGroupVo;
import com.blackstrawai.common.BaseDao;

@Repository
public class CustomerGroupDao extends BaseDao{

	private Logger logger = Logger.getLogger(CustomerGroupDao.class);

	public CustomerGroupVo createCustomerGroup(CustomerGroupVo customerGroupVo) throws ApplicationException {
		logger.info("Entry into method: createCustomerGroup");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = getUserMgmConnection();
			boolean isCustomerGroupExist=checkCustomerGroupExist(customerGroupVo.getName(),customerGroupVo.getOrganizationId(),con);
			if(isCustomerGroupExist){
				throw new Exception("Customer Group Exist for the Organization");
			}
			String sql = SettingsAndPreferencesConstants.INSERT_INTO_CUSTOMER_GROUP_ORGANIZATION;
			preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);	
			preparedStatement.setString(1, customerGroupVo.getName()!=null ? customerGroupVo.getName():null);
			preparedStatement.setString(2, customerGroupVo.getDescription()!=null ? customerGroupVo.getDescription():null);
			preparedStatement.setInt(3, customerGroupVo.getOrganizationId()!=null ? customerGroupVo.getOrganizationId():null);
			preparedStatement.setInt(4, Integer.valueOf(customerGroupVo.getUserId()));
			preparedStatement.setBoolean(5, customerGroupVo.getIsSuperAdmin());
			preparedStatement.setString(6, customerGroupVo.getRoleName());
			int rowAffected = preparedStatement.executeUpdate();
			if (rowAffected == 1) {
				rs = preparedStatement.getGeneratedKeys();
				if (rs.next()) {
					customerGroupVo.setId(rs.getInt(1));
				}
			}
		}catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return customerGroupVo;
	}


	private boolean checkCustomerGroupExist(String name, int organizationId, Connection con) throws ApplicationException {
		logger.info("Entry into method: checkCustomerGroupExist");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			String query = SettingsAndPreferencesConstants.CHECK_CUSTOMER_GROUP_EXIST_ORGANIZATION;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setString(1, name);
			preparedStatement.setInt(2, organizationId);
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


	public List<CustomerGroupVo> getAllCustomerGroupOfAnOrganization(int OrganizationId) throws ApplicationException{
		logger.info("Entry into method: getAllCustomerGroupOfAnOrganization");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		List<CustomerGroupVo> listOfCustomerGroup=new ArrayList<CustomerGroupVo>();
		try {
			con = getUserMgmConnection();
			String query = SettingsAndPreferencesConstants.GET_CUSTOMER_GROUP_ORGANIZATION;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, OrganizationId);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				CustomerGroupVo customerGroupVo=new CustomerGroupVo();
				customerGroupVo.setId(rs.getInt(1));
				customerGroupVo.setName(rs.getString(2));
				customerGroupVo.setDescription(rs.getString(3));
				customerGroupVo.setOrganizationId(rs.getInt(4));
				customerGroupVo.setUserId(rs.getString(5));
				customerGroupVo.setIsSuperAdmin(rs.getBoolean(6));
				customerGroupVo.setStatus(rs.getString(7));
				customerGroupVo.setCreateTs(rs.getTimestamp(8));
				customerGroupVo.setUpdateTs(rs.getTimestamp(9));
				listOfCustomerGroup.add(customerGroupVo);
			}
		}catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return listOfCustomerGroup;
	}
	
	public List<CustomerGroupVo> getAllCustomerGroupOfAnOrganizationForUserAndRole(int OrganizationId,String userId,String roleName) throws ApplicationException{
		logger.info("Entry into method: getAllCustomerGroupOfAnOrganizationForUserAndRole");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		List<CustomerGroupVo> listOfCustomerGroup=new ArrayList<CustomerGroupVo>();
		try {
			con = getUserMgmConnection();
			String query="";
			if(roleName.equals("Super Admin")){
				query = SettingsAndPreferencesConstants.GET_CUSTOMER_GROUP_ORGANIZATION;
			}else{
				query = SettingsAndPreferencesConstants.GET_CUSTOMER_GROUP_ORGANIZATION_USER_ROLE;
			}
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, OrganizationId);
			if(!(roleName.equals("Super Admin"))){
				preparedStatement.setString(2, userId);
				preparedStatement.setString(3, roleName);
			}	
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				CustomerGroupVo customerGroupVo=new CustomerGroupVo();
				customerGroupVo.setId(rs.getInt(1));
				customerGroupVo.setName(rs.getString(2));
				customerGroupVo.setDescription(rs.getString(3));
				customerGroupVo.setOrganizationId(rs.getInt(4));
				customerGroupVo.setUserId(rs.getString(5));
				customerGroupVo.setIsSuperAdmin(rs.getBoolean(6));
				customerGroupVo.setStatus(rs.getString(7));
				customerGroupVo.setCreateTs(rs.getTimestamp(8));
				customerGroupVo.setUpdateTs(rs.getTimestamp(9));
				listOfCustomerGroup.add(customerGroupVo);
			}
		}catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return listOfCustomerGroup;
	}


	public CustomerGroupVo getCustomerGroupById(int id) throws ApplicationException {
		logger.info("Entry into method: getCustomerGroupById");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		CustomerGroupVo customerGroupVo = new CustomerGroupVo();
		try {
			con = getUserMgmConnection();
			String query = SettingsAndPreferencesConstants.GET_CUSTOMER_GROUP_BY_ID_ORGANIZATION;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, id);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				customerGroupVo.setId(rs.getInt(1));
				customerGroupVo.setName(rs.getString(2));
				customerGroupVo.setDescription(rs.getString(3));
				customerGroupVo.setOrganizationId(rs.getInt(4));
				customerGroupVo.setUserId(rs.getString(5));
				customerGroupVo.setIsSuperAdmin(rs.getBoolean(6));
				customerGroupVo.setStatus(rs.getString(7));
				customerGroupVo.setCreateTs(rs.getTimestamp(8));
				customerGroupVo.setUpdateTs(rs.getTimestamp(9));
			}
		}catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return customerGroupVo;
	}

	public CustomerGroupVo deleteCustomerGroup(int id,String status,String userId,String roleName) throws ApplicationException {
		logger.info("Entry into method: deleteCustomerGroup");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		CustomerGroupVo customerGroupVo = new CustomerGroupVo();
		try {
			con = getUserMgmConnection();
			String sql = SettingsAndPreferencesConstants.DELETE_CUSTOMER_GROUP_ORGANIZATION;
			preparedStatement = con.prepareStatement(sql);
			preparedStatement.setString(1, status);
			preparedStatement.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
			preparedStatement.setString(3, userId);
			preparedStatement.setString(4, roleName);
			preparedStatement.setInt(5, id);
			customerGroupVo.setId(id);
			preparedStatement.executeUpdate();
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return customerGroupVo;
	}


	public CustomerGroupVo updateCustomerGroup(CustomerGroupVo customerGroupVo) throws ApplicationException {
		logger.info("Entry into method: updateCustomerGroup");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = getUserMgmConnection();
			String sql = SettingsAndPreferencesConstants.UPDATE_CUSTOMER_GROUP_ORGANIZATION;
			preparedStatement = con.prepareStatement(sql);
			preparedStatement.setString(1, customerGroupVo.getName() !=null ? customerGroupVo.getName():null);
			preparedStatement.setString(2, customerGroupVo.getDescription() !=null ? customerGroupVo.getDescription():null);
			preparedStatement.setInt(3, customerGroupVo.getOrganizationId() !=null ? customerGroupVo.getOrganizationId():null);
			preparedStatement.setBoolean(4, customerGroupVo.getIsSuperAdmin());
			preparedStatement.setInt(5, Integer.valueOf(customerGroupVo.getUserId()));
			preparedStatement.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
			preparedStatement.setString(7, customerGroupVo.getStatus());
			preparedStatement.setString(8, customerGroupVo.getRoleName());
			preparedStatement.setInt(9, customerGroupVo.getId());
			preparedStatement.executeUpdate();
		}catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return customerGroupVo;
	}

	public List<BasicCustomerGroupVo> getBasicCustomerGroup(Integer orgId , Connection connection) throws ApplicationException{
		logger.info("Entry into method:getBasicCustomerGroup");
		List<BasicCustomerGroupVo> customerGroupVos = null;
		PreparedStatement preparedStatement =null;
		ResultSet resultSet =null;
		try{
			preparedStatement = connection.prepareStatement(SettingsAndPreferencesConstants.GET_MINIMAL_CUSTOMER_GROUPS_ORGANIZATION);
			preparedStatement.setInt(1, orgId);
			resultSet = preparedStatement.executeQuery();
			customerGroupVos = new ArrayList<BasicCustomerGroupVo>();
			while(resultSet.next()) {
				BasicCustomerGroupVo customerGroupVo = new BasicCustomerGroupVo();
				customerGroupVo.setId(resultSet.getInt(1));
				customerGroupVo.setName(resultSet.getString(2));
				customerGroupVos.add(customerGroupVo);
			}
		} catch (SQLException e) {
			logger.info("Error in method:getBasicCustomerGroup",e);
			throw new ApplicationException(e);
		} finally {
			closeResources(resultSet, preparedStatement, null);
		}
		return customerGroupVos;
	}

	public String getCustomerGroupName(int id,int organizationId) throws ApplicationException {
		logger.info("To get the getCustomerGroupName");
		String customerGroupName = null;
		Connection con =null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			con = getUserMgmConnection();
			preparedStatement = con.prepareStatement(SettingsAndPreferencesConstants.GET_CUSTOMER_GROUP_NAME);
			preparedStatement.setInt(1, id);
			preparedStatement.setInt(2, organizationId);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				customerGroupName = rs.getString(1);
			}
		} catch (Exception e) {
			logger.info("Error in getCustomerGroupName", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return customerGroupName;
	}
}

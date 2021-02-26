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
import com.blackstrawai.ap.dropdowns.BasicVendorGroupVo;
import com.blackstrawai.common.BaseDao;
import com.blackstrawai.vendorsettings.dropdowns.VamVendorGroupDropDownVo;

@Repository
public class VendorGroupDao extends BaseDao {

	private Logger logger = Logger.getLogger(VendorGroupDao.class);

	public VendorGroupVo createVendorGroup(VendorGroupVo vendorGroupVo) throws ApplicationException {
		logger.info("Entry into method: createVendorGroup");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = getUserMgmConnection();
			boolean isVendorGroupExist = checkVendorGroupExist(vendorGroupVo.getName(),
					vendorGroupVo.getOrganizationId(), con);
			if (isVendorGroupExist) {
				throw new Exception("Vendor Group Exist for the Organization");
			}
			String sql = SettingsAndPreferencesConstants.INSERT_INTO_VENDOR_GROUP_ORGANIZATION;
			preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setString(1, vendorGroupVo.getName() != null ? vendorGroupVo.getName() : null);
			preparedStatement.setString(2,
					vendorGroupVo.getDescription() != null ? vendorGroupVo.getDescription() : null);
			preparedStatement.setInt(3,
					vendorGroupVo.getOrganizationId() != null ? vendorGroupVo.getOrganizationId() : null);
			preparedStatement.setInt(4, Integer.valueOf(vendorGroupVo.getUserId()));
			preparedStatement.setBoolean(5, vendorGroupVo.getIsSuperAdmin());
			preparedStatement.setString(6, vendorGroupVo.getRoleName());
			int rowAffected = preparedStatement.executeUpdate();
			if (rowAffected == 1) {
				rs = preparedStatement.getGeneratedKeys();
				if (rs.next()) {
					vendorGroupVo.setId(rs.getInt(1));
				}
			}
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return vendorGroupVo;
	}

	private boolean checkVendorGroupExist(String name, int organizationId, Connection con) throws ApplicationException {
		logger.info("Entry into method: checkVendorGroupExist");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			String query = SettingsAndPreferencesConstants.CHECK_VENDOR_GROUP_EXIST_ORGANIZATION;
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

	public List<VendorGroupVo> getAllVendorGroupOfAnOrganization(int organizationId) throws ApplicationException {
		logger.info("Entry into method: getAllVendorGroupOfAnOrganization");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		List<VendorGroupVo> listOfVendorGroup = new ArrayList<VendorGroupVo>();
		try {
			con = getUserMgmConnection();
			String query = SettingsAndPreferencesConstants.GET_VENDOR_GROUP_ORGANIZATION;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, organizationId);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				VendorGroupVo vendorGroupVo = new VendorGroupVo();
				vendorGroupVo.setId(rs.getInt(1));
				vendorGroupVo.setName(rs.getString(2));
				vendorGroupVo.setDescription(rs.getString(3));
				vendorGroupVo.setOrganizationId(rs.getInt(4));
				vendorGroupVo.setUserId(rs.getString(5));
				vendorGroupVo.setIsSuperAdmin(rs.getBoolean(6));
				vendorGroupVo.setStatus(rs.getString(7));
				vendorGroupVo.setCreateTs(rs.getTimestamp(8));
				vendorGroupVo.setUpdateTs(rs.getTimestamp(9));
				listOfVendorGroup.add(vendorGroupVo);
			}
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return listOfVendorGroup;
	}

	
	public List<VendorGroupVo> getAllVendorGroupOfAnOrganizationForUserAndRole(int organizationId,String userId,String roleName) throws ApplicationException {
		logger.info("Entry into method:getAllVendorGroupOfAnOrganizationForUserAndRole");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		List<VendorGroupVo> listOfVendorGroup = new ArrayList<VendorGroupVo>();
		try {
			con = getUserMgmConnection();
			String query="";
			if(roleName.equals("Super Admin")){
				 query = SettingsAndPreferencesConstants.GET_VENDOR_GROUP_ORGANIZATION;
			}else{
				 query = SettingsAndPreferencesConstants.GET_VENDOR_GROUP_ORGANIZATION_USER_ROLE;
			}
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, organizationId);
			if(!(roleName.equals("Super Admin"))){
				preparedStatement.setString(2, userId);
				preparedStatement.setString(3, roleName);
			}	
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				VendorGroupVo vendorGroupVo = new VendorGroupVo();
				vendorGroupVo.setId(rs.getInt(1));
				vendorGroupVo.setName(rs.getString(2));
				vendorGroupVo.setDescription(rs.getString(3));
				vendorGroupVo.setOrganizationId(rs.getInt(4));
				vendorGroupVo.setUserId(rs.getString(5));
				vendorGroupVo.setIsSuperAdmin(rs.getBoolean(6));
				vendorGroupVo.setStatus(rs.getString(7));
				vendorGroupVo.setCreateTs(rs.getTimestamp(8));
				vendorGroupVo.setUpdateTs(rs.getTimestamp(9));
				listOfVendorGroup.add(vendorGroupVo);
			}
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return listOfVendorGroup;
	}
	
	public VendorGroupVo getVendorGroupById(int id) throws ApplicationException {
		logger.info("Entry into method: getVendorGroupById");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		VendorGroupVo vendorGroupVo = new VendorGroupVo();
		try {
			con = getUserMgmConnection();
			String query = SettingsAndPreferencesConstants.GET_VENDOR_GROUP_BY_ID_ORGANIZATION;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, id);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				vendorGroupVo.setId(rs.getInt(1));
				vendorGroupVo.setName(rs.getString(2));
				vendorGroupVo.setDescription(rs.getString(3));
				vendorGroupVo.setOrganizationId(rs.getInt(4));
				vendorGroupVo.setUserId(rs.getString(5));
				vendorGroupVo.setIsSuperAdmin(rs.getBoolean(6));
				vendorGroupVo.setStatus(rs.getString(7));
				vendorGroupVo.setCreateTs(rs.getTimestamp(8));
				vendorGroupVo.setUpdateTs(rs.getTimestamp(9));
			}
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return vendorGroupVo;
	}

	public VendorGroupVo deleteVendorGroup(int id,String status,String userId,String roleName) throws ApplicationException {
		logger.info("Entry into method: deleteVendorGroup");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		VendorGroupVo vendorGroupVo = new VendorGroupVo();
		try {
			con = getUserMgmConnection();
			String sql = SettingsAndPreferencesConstants.DELETE_VENDOR_GROUP_ORGANIZATION;
			preparedStatement = con.prepareStatement(sql);
			preparedStatement.setString(1, status);
			preparedStatement.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
			preparedStatement.setString(3, userId);
			preparedStatement.setString(4, roleName);
			preparedStatement.setInt(5, id);
			vendorGroupVo.setId(id);
			preparedStatement.executeUpdate();
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return vendorGroupVo;
	}

	public VendorGroupVo updateVendorGroup(VendorGroupVo vendorGroupVo) throws ApplicationException {
		logger.info("Entry into method: updateVendorGroup");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = getUserMgmConnection();
			String sql = SettingsAndPreferencesConstants.UPDATE_VENDOR_GROUP_ORGANIZATION;
			preparedStatement = con.prepareStatement(sql);
			preparedStatement.setString(1, vendorGroupVo.getName() != null ? vendorGroupVo.getName() : null);
			preparedStatement.setString(2,
					vendorGroupVo.getDescription() != null ? vendorGroupVo.getDescription() : null);
			preparedStatement.setInt(3,
					vendorGroupVo.getOrganizationId() != null ? vendorGroupVo.getOrganizationId() : null);
			preparedStatement.setInt(4, Integer.valueOf(vendorGroupVo.getUserId()));
			preparedStatement.setBoolean(5, vendorGroupVo.getIsSuperAdmin());
			preparedStatement.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
			preparedStatement.setString(7, vendorGroupVo.getStatus());
			preparedStatement.setString(8, vendorGroupVo.getRoleName());
			preparedStatement.setInt(9, vendorGroupVo.getId());
			preparedStatement.executeUpdate();
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return vendorGroupVo;
	}

	public List<BasicVendorGroupVo> getBasicVendorGroup(int organizationId, Connection con)
			throws ApplicationException {
		logger.info("Entry into method: getBasicVendorGroup");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		List<BasicVendorGroupVo> vendorGroupList = new ArrayList<BasicVendorGroupVo>();
		try {
			String query = SettingsAndPreferencesConstants.GET_VENDOR_GROUPS_ORGANIZATION;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, organizationId);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				BasicVendorGroupVo data = new BasicVendorGroupVo();
				data.setId(rs.getInt(1));
				data.setName(rs.getString(2));
				vendorGroupList.add(data);
			}
		} catch (Exception e) {

			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, null);

		}
		return vendorGroupList;
	}

	public String getVendorGroupName(int id, int organizationId) throws ApplicationException {
		logger.info("To get the getVendorGroupName");
		String vendorGroupName = null;
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs =null;
		try {
			con = getUserMgmConnection();
			preparedStatement = con.prepareStatement(SettingsAndPreferencesConstants.GET_VENDOR_GROUP_NAME);
			preparedStatement.setInt(1, id);
			preparedStatement.setInt(2, organizationId);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				vendorGroupName = rs.getString(1);
			}
		} catch (Exception e) {
			logger.info("Error in getVendorGroupName", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);

		}
		return vendorGroupName;
	}

	public List<VamVendorGroupDropDownVo> getVendorGroupNamesOfOrganization(int organizationId, Connection con)
			throws ApplicationException {
		logger.info("To get the getVendorGroupNamesOfOrganization");
		List<VamVendorGroupDropDownVo> vendorGroupDetails = new ArrayList<VamVendorGroupDropDownVo>();
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			preparedStatement = con.prepareStatement(SettingsAndPreferencesConstants.GET_VENDOR_GROUP_NAMES_OF_AN_ORGANIZATION); 
			preparedStatement.setInt(1, organizationId);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				VamVendorGroupDropDownVo vamVendorGroupVo = new VamVendorGroupDropDownVo();
				vamVendorGroupVo.setId(rs.getInt(1));
				vamVendorGroupVo.setVendorGroupName(rs.getString(2));
				vendorGroupDetails.add(vamVendorGroupVo);
			}
		} catch (Exception e) {
			logger.info("Error in getVendorGroupNamesOfOrganization", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, null);

		}
		return vendorGroupDetails;
	}

}

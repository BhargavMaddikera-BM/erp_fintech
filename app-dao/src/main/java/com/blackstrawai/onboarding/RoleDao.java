package com.blackstrawai.onboarding;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.accessandroles.AccessAndRolesDao;
import com.blackstrawai.common.BaseDao;
import com.blackstrawai.common.CommonConstants;
import com.blackstrawai.onboarding.role.RoleVo;
import com.blackstrawai.onboarding.user.RegisteredAddressVo;
import com.blackstrawai.onboarding.user.RolesVo;
import com.blackstrawai.onboarding.user.UserVo;
import com.blackstrawai.settings.CommonVo;

@Repository
public class RoleDao extends BaseDao {

	private Logger logger = Logger.getLogger(RoleDao.class);

	@Autowired
	AccessAndRolesDao accessAndRolesDao;


	public List<RoleVo> getAllRolesOfAnOrganization(int organizationId,String organizationName,String userId,String roleName)
			throws ApplicationException {
		logger.info("Entry into method:getAllRolesOfAnOrganization");
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		List<RoleVo> listAllRoles = new ArrayList<RoleVo>();
		try {
			con = getUserMgmConnection();
			String query="";
			if(roleName.equals("Super Admin")){
				query = RoleConstants.GET_ROLES_ORGANIZATION;
			}else{
				query = RoleConstants.GET_ROLES_BY_ORGANIZATION_USER_ROLE;
			}
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, organizationId);
			preparedStatement.setString(2, "DEL");
			if(!(roleName.equals("Super Admin"))){
				preparedStatement.setString(3, userId);
				preparedStatement.setString(4, roleName);
			}	
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				RoleVo roleVo = new RoleVo();
				roleVo.setId(rs.getInt(1));
				roleVo.setName(rs.getString(2));
				roleVo.setDescription(rs.getString(3));
				roleVo.setStatus(rs.getString(4));
				roleVo.setOrganizationId(rs.getInt(5));
				int count = getUserCountForARole(roleVo.getId(), con);
				/*if(roleVo.getName().equals("Super Admin")){
					++count;
				}		*/		
				roleVo.setNumberOfUsers(count);
				roleVo.setAccessData(rs.getString(6));
				//listAllRoles.add(roleVo);
				if(roleVo.getName().equals("Vendor")|| roleVo.getName().equals("Customer")|| roleVo.getName().equals("Super Admin")){
					logger.info("RoleName is:"+roleVo.getName() );
				}else{
					listAllRoles.add(roleVo);
				}

			}
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);

		}
		return listAllRoles;
	}

	public RoleVo getRoleDetails(int organizationId, int roleId) throws ApplicationException {
		logger.info("Entry into getRoleDetails");
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		RoleVo roleVo = null;
		try {
			con = getUserMgmConnection();
			String query = RoleConstants.GET_ROLE_IN_ORGANIZATION;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, roleId);
			preparedStatement.setInt(2, organizationId);
			preparedStatement.setString(3, "DEL");
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				roleVo = new RoleVo();
				roleVo.setId(rs.getInt(1));
				roleVo.setName(rs.getString(2));
				roleVo.setDescription(rs.getString(3));
				roleVo.setStatus(rs.getString(4));
				roleVo.setOrganizationId(rs.getInt(5));
				int count = getUserCountForARole(roleVo.getId(), con);
				roleVo.setNumberOfUsers(count);
				roleVo.setAccessData(rs.getString(6));
			}
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);

		}
		return roleVo;
	}

	private boolean checkRoleExistForAnOrganization(String roleName, int organizationId) throws ApplicationException {
		logger.info("Entry into method:checkRoleExistForAnOrganization");
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			con = getUserMgmConnection();
			String query = RoleConstants.CHECK_ROLE_EXIST_FOR_ORGANIZATION;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, organizationId);
			preparedStatement.setString(2, roleName);
			preparedStatement.setString(3, "DEL");
			rs = preparedStatement.executeQuery();
			if (rs.next()) {
				return true;
			}
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);

		}
		return false;
	}

	public RoleVo createRole(RoleVo roleVo) throws ApplicationException {
		logger.info("Entry into method:createRole");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			boolean isRoleExist = checkRoleExistForAnOrganization(roleVo.getName(), roleVo.getOrganizationId());
			if (isRoleExist) {
				if(roleVo.getRoleName().equals("Vendor")|| roleVo.getRoleName().equals("Customer") || roleVo.getRoleName().equals("Super Admin")){
					throw new Exception("Vendor, Customer, Super Admin are System Defined Roles.Please create any another Role");
				}else{
					throw new Exception("Role Exist for the Organization");
				}
				
			}
			con = getUserMgmConnection();
			String sql = RoleConstants.INSERT_INTO_ROLE;

			preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setString(1, roleVo.getName());
			preparedStatement.setString(2, roleVo.getDescription());
			preparedStatement.setString(3, roleVo.getStatus());
			preparedStatement.setInt(4, roleVo.getOrganizationId());
			if(roleVo.getAccessData()!=null && roleVo.getAccessData().length()>0){
				preparedStatement.setString(5, roleVo.getAccessData());
			}else{
				preparedStatement.setString(5, accessAndRolesDao.getAccess(1));				
			}
			preparedStatement.setString(6, roleVo.getUserId());
			preparedStatement.setString(7, roleVo.getRoleName());
			int rowAffected = preparedStatement.executeUpdate();
			if (rowAffected == 1) {
				rs = preparedStatement.getGeneratedKeys();
				if (rs.next()) {
					roleVo.setId(rs.getInt(1));
				}
			}
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);

		}

		return roleVo;
	}

	public RoleVo updateRole(RoleVo roleVo) throws ApplicationException {
		logger.info("Entry into method:updateRole");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = getUserMgmConnection();
			String sql = RoleConstants.UPDATE_ROLE;

			preparedStatement = con.prepareStatement(sql);
			preparedStatement.setString(1, roleVo.getName());
			preparedStatement.setString(2, roleVo.getDescription());
			preparedStatement.setString(3, roleVo.getStatus());
			preparedStatement.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
			preparedStatement.setString(5, roleVo.getAccessData());
			preparedStatement.setString(6, roleVo.getUserId());
			preparedStatement.setString(7, roleVo.getRoleName());
			preparedStatement.setInt(8, roleVo.getId());
			preparedStatement.setInt(9, roleVo.getOrganizationId());
			preparedStatement.executeUpdate();
			closeResources(rs, preparedStatement, con);
			Thread t = new Thread(new Runnable() 
			{ 
				public void run() 
				{ 		            	
					try {
						Connection con1 = getUserMgmConnection();
						List<UserVo> listOfUsers = getAllUsersOfAnOrganization(roleVo.getOrganizationId(),null);
						String sql1 = UserConstants.UPDATE_USER_ACCESS_DATA;
						PreparedStatement preparedStatement1 = con1.prepareStatement(sql1);
						for(int i=0;i<listOfUsers.size();i++){
							UserVo data=listOfUsers.get(i);		
							if(data.getRoleId()==roleVo.getId()){			   					
								preparedStatement1.setString(1, roleVo.getAccessData());
								preparedStatement1.setString(2, roleVo.getUserId());
								preparedStatement1.setString(3, roleVo.getRoleName());
								preparedStatement1.setInt(4, data.getId());			   				
								preparedStatement1.executeUpdate();
							}
						}
						closeResources(null, preparedStatement1, con1);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}


				} 
			}); 

			t.start(); 


		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);

		}

		return roleVo;
	}

	public RoleVo deleteRole(int id, String status,String userId,String roleName) throws ApplicationException {
		logger.info("Entry into method:deleteRole");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		RoleVo roleVo = new RoleVo();
		try {
			con = getUserMgmConnection();
			String sql = RoleConstants.DELETE_ROLE;
			preparedStatement = con.prepareStatement(sql);
			preparedStatement.setString(1, status);
			preparedStatement.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
			preparedStatement.setString(3, userId);
			preparedStatement.setString(4, roleName);
			preparedStatement.setInt(5, id);
			roleVo.setId(id);
			preparedStatement.executeUpdate();
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);

		}

		return roleVo;
	}

	private int getUserCountForARole(int roleId, Connection con) throws ApplicationException {
		logger.info("Entry into method:getUserCountForARole");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		int count = 0;
		try {
			String sql = UserConstants.GET_USER_COUNT_FOR_ROLE;
			preparedStatement = con.prepareStatement(sql);
			preparedStatement.setInt(1, roleId);
			preparedStatement.setString(2, "DEL");
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				return rs.getInt(1);
			}
		} catch (Exception e) {
			throw new ApplicationException(e);
		}

		return count;
	}
	
	
	private List<UserVo> getActiveUsersForARole(int roleId, Connection con) throws ApplicationException {
		logger.info("Entry into method:getActiveUsersForARole");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		List<UserVo> users=new ArrayList<UserVo>();
		try {
			String sql = UserConstants.GET_ACTIVE_USERS_FOR_ROLE;
			preparedStatement = con.prepareStatement(sql);
			preparedStatement.setInt(1, roleId);
			preparedStatement.setString(2, CommonConstants.STATUS_AS_ACTIVE);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				UserVo userVo = new UserVo();
				userVo.setId(rs.getInt(1));
				userVo.setFirstName(rs.getString(2));
				userVo.setLastName(rs.getString(3));
				userVo.setFullName(rs.getString(2)+" "+rs.getString(3));
				userVo.setEmailId(rs.getString(4));
				userVo.setStatus(rs.getString(5));
				users.add(userVo);
			}
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, null);

		}


		return users;
	}
	
	public List<UserVo> getUsersForRoleInOrganization(int orgId,int roleId) throws ApplicationException {
		logger.info("Entry into getUsersForRoleInOrganization");
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		List<UserVo> users=new ArrayList<UserVo>();
		try {
			con = getUserMgmConnection();
			String sql = UserConstants.GET_USERS_FOR_ROLE_ORGANIZATION;
			preparedStatement = con.prepareStatement(sql);
			preparedStatement.setInt(1, orgId);
			preparedStatement.setInt(2, roleId);
			preparedStatement.setString(3, "DEL");
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				UserVo user=new UserVo();
				user.setId(rs.getInt(1));
				user.setFirstName(rs.getString(2));
				user.setLastName(rs.getString(3));
				user.setFullName(rs.getString(2)+" "+rs.getString(3));
				user.setEmailId(rs.getString(4));
				users.add(user);
			}
			logger.info("SUcessfully Fetched getUsersForRoleInOrganization:"+users.size());
		} catch (Exception e) {
			logger.error("Error in getUsersForRoleInOrganization",e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);

		}


		return users;
	}
	

	public Map<Integer, String> getAllRolesOfAnOrganization(int organizationId) throws ApplicationException {
		logger.info("Entry into method:getAllRolesOfAnOrganization");
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Map<Integer, String> data = new HashMap<Integer, String>();
		try {
			con = getUserMgmConnection();
			String query = RoleConstants.GET_ROLES_BY_ORGANIZATION;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, organizationId);
			preparedStatement.setString(2, "DEL");
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				data.put(rs.getInt(1), rs.getString(2));
			}
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);

		}
		return data;
	}


	public RoleVo getRoleByName(int organizationId, String roleName)
			throws ApplicationException {
		logger.info("Entry into method:getRoleByName");
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		RoleVo roleVo = new RoleVo();
		try {
			con = getUserMgmConnection();
			String query = RoleConstants.GET_ROLES_BY_NAME_ORGANIZATION;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, organizationId);
			preparedStatement.setString(2, roleName);
			preparedStatement.setString(3, "DEL");
			rs = preparedStatement.executeQuery();
			while (rs.next()) {

				roleVo.setId(rs.getInt(1));
				roleVo.setName(rs.getString(2));
				roleVo.setDescription(rs.getString(3));
				roleVo.setStatus(rs.getString(4));
				roleVo.setOrganizationId(rs.getInt(5));
				roleVo.setAccessData(rs.getString(6));
				int count = getUserCountForARole(roleVo.getId(), con);
				roleVo.setNumberOfUsers(count);
			}
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);

		}
		return roleVo;
	}

	private List<UserVo> getAllUsersOfAnOrganization(int organizationId, String organizationName)
			throws ApplicationException {
		logger.info("Entry into method:getAllUsersOfAnOrganization");
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		List<UserVo> listAllUsers = new ArrayList<UserVo>();
		try {
			con = getUserMgmConnection();
			String query = UserConstants.GET_USERS_BY_ORGANIZATION;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, organizationId);
			preparedStatement.setString(2, "DEL");
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				UserVo userVo = new UserVo();
				userVo.setId(rs.getInt(1));
				userVo.setOrganizationId(rs.getInt(2));
				userVo.setFirstName(rs.getString(3));
				userVo.setLastName(rs.getString(4));
				userVo.setEmailId(rs.getString(5));
				userVo.setGender(rs.getString(6));
				userVo.setDob(rs.getString(7));
				userVo.setPhoneNo(rs.getString(8));
				userVo.setStatus(rs.getString(9));
				userVo.setRoleId(rs.getInt(10));				
				RegisteredAddressVo address = new RegisteredAddressVo();
				address.setId(rs.getInt(11));
				address.setAddressLine1(rs.getString(12));
				address.setAddressLine2(rs.getString(13));
				address.setCity(rs.getString(14));
				address.setState(Integer.parseInt(rs.getString(15)));
				address.setCountry(Integer.parseInt(rs.getString(16)));
				address.setPinCode(rs.getString(17));
				address.setRegistered(rs.getBoolean(18));
				address.setBilling(rs.getBoolean(19));
				userVo.setRegisteredAddress(address);
				userVo.setCreateTs(rs.getTimestamp(20));
				userVo.setUpdateTs(rs.getTimestamp(21));
				userVo.setFullName(userVo.getFirstName() + " " + userVo.getLastName());
				listAllUsers.add(userVo);
			}
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);

		}
		return listAllUsers;
	}
	public List<RolesVo> getAllActiveRolesOfAnOrganizationByCount(int organizationId) throws ApplicationException {
		logger.info("Entry into getAllActiveRolesOfAnOrganizationByCount");
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		List<RolesVo> roles = new ArrayList<RolesVo>();
		try {
			con = getUserMgmConnection();
			String query = RoleConstants.GET_ACTIVE_ROLES_BY_ORGANIZATION;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, organizationId);
			preparedStatement.setString(2, CommonConstants.STATUS_AS_ACTIVE);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				int id=rs.getInt(1);
				String roleName=rs.getString(2);
				List<UserVo> users = getActiveUsersForARole(id, con);
				if(users!=null && users.size()>0) {//IF there are users then only include
					RolesVo role=new RolesVo();
					role.setId(id);
					role.setName(roleName);
					role.setUsers(users);
					roles.add(role);
					
				}
			}
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);

		}
		return roles;
	}

}





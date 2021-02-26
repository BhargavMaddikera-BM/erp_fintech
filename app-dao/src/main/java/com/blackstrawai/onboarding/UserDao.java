package com.blackstrawai.onboarding;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.common.BaseDao;
import com.blackstrawai.common.CommonConstants;
import com.blackstrawai.onboarding.loginandregistration.ProfileVo;
import com.blackstrawai.onboarding.organization.NewOrganizationVo;
import com.blackstrawai.onboarding.role.RoleVo;
import com.blackstrawai.onboarding.user.InviteActionVo;
import com.blackstrawai.onboarding.user.InviteUserVo;
import com.blackstrawai.onboarding.user.ListSentInvitesVo;
import com.blackstrawai.onboarding.user.ManageInvitesVo;
import com.blackstrawai.onboarding.user.RegisteredAddressVo;
import com.blackstrawai.onboarding.user.RoleUserVo;
import com.blackstrawai.onboarding.user.UserVo;
import com.blackstrawai.onboarding.user.WithdrawInviteVo;

@Repository
public class UserDao extends BaseDao {

	private Logger logger = Logger.getLogger(UserDao.class);

	@Autowired
	RoleDao roleDao;
	@Autowired
	@Lazy
	OrganizationDao organizationDao;

	@SuppressWarnings("resource")
	public UserVo createUser(UserVo userVo) throws ApplicationException {
		logger.info("Entry into method:createUser");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			boolean isUserExist = checkUserExist(userVo.getEmailId(), userVo.getPhoneNo(), userVo.getOrganizationId());
			if (isUserExist) {
				throw new Exception("User Exist for the Organization");
			}
			con = getUserMgmConnection();
			String sql = UserConstants.INSERT_INTO_USER;
			preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setInt(1, userVo.getOrganizationId());
			preparedStatement.setString(2, userVo.getFirstName());
			preparedStatement.setString(3, userVo.getLastName());
			preparedStatement.setString(4, userVo.getEmailId());
			preparedStatement.setString(5, userVo.getGender());
			preparedStatement.setString(6, userVo.getDob());
			preparedStatement.setString(7, userVo.getPhoneNo());
			preparedStatement.setString(8, userVo.getStatus());
			preparedStatement.setInt(9, userVo.getRoleId());
			if (userVo.getAccessData() == null) {
				RoleVo roleVo = roleDao.getRoleDetails(userVo.getOrganizationId(), userVo.getRoleId());
				preparedStatement.setString(10,
						roleDao.getRoleByName(userVo.getOrganizationId(), roleVo.getName()).getAccessData());
			} else {
				preparedStatement.setString(10, userVo.getAccessData());
			}
			preparedStatement.setString(11, userVo.getUserId());
			preparedStatement.setString(12, userVo.getRoleName());
			int rowAffected = preparedStatement.executeUpdate();
			if (rowAffected == 1) {
				rs = preparedStatement.getGeneratedKeys();
				if (rs.next()) {
					userVo.setId(rs.getInt(1));
					if( userVo.getRegisteredAddress()!=null){
						sql = UserConstants.INSERT_INTO_USER_ADDRESS;
						preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
						preparedStatement.setString(1, userVo.getRegisteredAddress().getAddressLine1());
						preparedStatement.setString(2, userVo.getRegisteredAddress().getAddressLine2());
						preparedStatement.setString(3, userVo.getRegisteredAddress().getCity());
						preparedStatement.setString(4, new Integer(userVo.getRegisteredAddress().getState()).toString());
						preparedStatement.setString(5, new Integer(userVo.getRegisteredAddress().getCountry()).toString());
						preparedStatement.setString(6, userVo.getRegisteredAddress().getPinCode());
						preparedStatement.setInt(7, userVo.getId());
						rowAffected = preparedStatement.executeUpdate();
						if (rowAffected == 1) {
							rs = preparedStatement.getGeneratedKeys();
							if (rs.next()) {
								userVo.getRegisteredAddress().setId(rs.getInt(1));
							}
						}
					}					
				}
			}
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);

		}

		return userVo;
	}

	private UserVo updateUserDetails(UserVo userVo) throws ApplicationException {
		logger.info("Entry into method:updateUserDetails");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			String accessData = null;
			if (userVo.getAccessData() == null) {
				RoleVo roleVo = roleDao.getRoleDetails(userVo.getOrganizationId(), userVo.getRoleId());
				accessData = roleDao.getRoleByName(userVo.getOrganizationId(), roleVo.getName()).getAccessData();
			} else {
				accessData = userVo.getAccessData();
			}
			con = getUserMgmConnection();
			String sql = UserConstants.UPDATE_USER;
			preparedStatement = con.prepareStatement(sql);
			preparedStatement.setString(1, userVo.getFirstName());
			preparedStatement.setString(2, userVo.getLastName());
			preparedStatement.setString(3, userVo.getEmailId());
			preparedStatement.setString(4, userVo.getGender());
			preparedStatement.setString(5, userVo.getDob());
			preparedStatement.setString(6, userVo.getPhoneNo());
			preparedStatement.setString(7, userVo.getStatus());
			preparedStatement.setInt(8, userVo.getRoleId());
			preparedStatement.setTimestamp(9, new Timestamp(System.currentTimeMillis()));
			preparedStatement.setString(10, accessData);
			preparedStatement.setString(11, userVo.getUserId());
			preparedStatement.setString(12, userVo.getRoleName());
			preparedStatement.setInt(13, userVo.getId());
			preparedStatement.executeUpdate();
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);

		}

		return userVo;
	}

	private UserVo updateUserAddress(UserVo userVo) throws ApplicationException {
		logger.info("Entry into method:updateUserAddress");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = getUserMgmConnection();
			String sql = UserConstants.UPDATE_USER_ADDRESS;
			preparedStatement = con.prepareStatement(sql);
			preparedStatement.setString(1, userVo.getRegisteredAddress().getAddressLine1());
			preparedStatement.setString(2, userVo.getRegisteredAddress().getAddressLine2());
			preparedStatement.setString(3, userVo.getRegisteredAddress().getCity());
			preparedStatement.setString(4, new Integer(userVo.getRegisteredAddress().getState()).toString());
			preparedStatement.setString(5, new Integer(userVo.getRegisteredAddress().getCountry()).toString());
			preparedStatement.setString(6, userVo.getRegisteredAddress().getPinCode());
			preparedStatement.setTimestamp(7, new Timestamp(System.currentTimeMillis()));
			preparedStatement.setInt(8, userVo.getRegisteredAddress().getId());
			preparedStatement.executeUpdate();
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);

		}

		return userVo;
	}

	public UserVo updateUser(UserVo userVo) throws ApplicationException {
		logger.info("Entry into method:updateUser");
		boolean isUserExist = isUpdateRequired(userVo.getEmailId(), userVo.getPhoneNo(), userVo.getId(),
				userVo.getOrganizationId());
		if (isUserExist) {
			throw new ApplicationException(
					"User Exist for the Organization with the given Email and Phone Combination");
		}
		userVo = updateUserDetails(userVo);
		userVo = updateUserAddress(userVo);
		return userVo;

	}
	
	public ProfileVo updateUserProfile(ProfileVo profileVo) throws ApplicationException {
		logger.info("Entry into method: updateUserProfile");
		Connection con = null;
		PreparedStatement preparedStatement = null;
		try {
			con = getUserMgmConnection();
			String query = UserConstants.UPDATE_USER_PROFILE;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setString(1, profileVo.getFirstName());
			preparedStatement.setString(2, profileVo.getLastName());
			preparedStatement.setString(3, profileVo.getMobileNo());
			preparedStatement.setString(4, profileVo.getEmailId());
			preparedStatement.executeUpdate();
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(null, preparedStatement, con);

		}

		return profileVo;

	}

	private boolean isUpdateRequired(String emailId, String phoneNo, int id, int organizationId)
			throws ApplicationException {
		logger.info("Entry into method:isUpdateRequired");
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			con = getUserMgmConnection();
			String query = UserConstants.CHECK_EMAIL_PHONE_EXIST_FOR_ORGANIZATION;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, id);
			preparedStatement.setString(2, emailId);
			preparedStatement.setString(3, phoneNo);
			preparedStatement.setInt(4, organizationId);
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

	private boolean checkUserExist(String emailId, String phoneNo, int organizationId) throws ApplicationException {
		logger.info("Entry into method:checkUserExist");
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			con = getUserMgmConnection();
			String query = UserConstants.CHECK_USER_EXIST_FOR_ORGANIZATION;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setString(1, emailId);
			preparedStatement.setString(2, phoneNo);
			preparedStatement.setInt(3, organizationId);
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

	private boolean checkUserExist(String emailId, int organizationId) throws ApplicationException {
		logger.info("Entry into method:checkUserExist");
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			con = getUserMgmConnection();
			String query = UserConstants.CHECK_USER_EXIST_FOR_ORGANIZATION_WITHEMAIL;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setString(1, emailId);
			preparedStatement.setInt(2, organizationId);
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

	public UserVo deleteUser(int id, String status, String userId, String roleName) throws ApplicationException {
		logger.info("Entry into method:deleteUser");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		UserVo userVo = new UserVo();
		try {
			con = getUserMgmConnection();
			String sql = UserConstants.DELETE_USER;
			preparedStatement = con.prepareStatement(sql);
			preparedStatement.setString(1, status);
			preparedStatement.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
			preparedStatement.setString(3, userId);
			preparedStatement.setString(4, roleName);
			preparedStatement.setInt(5, id);
			userVo.setId(id);
			preparedStatement.executeUpdate();
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);

		}

		return userVo;
	}

	public List<UserVo> getAllUsersOfAnOrganization(int organizationId, String organizationName)
			throws ApplicationException {
		logger.info("Entry into method:getAllUsersOfAnOrganization");
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		List<UserVo> listAllUsers = new ArrayList<UserVo>();
		Map<Integer, String> data = roleDao.getAllRolesOfAnOrganization(organizationId);
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
				if (data.containsKey(userVo.getRoleId())) {
					userVo.setRoleName(data.get(userVo.getRoleId()));
				}
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

	public List<UserVo> getAllUsersOfAnOrganizationUserAndRole(int organizationId, String organizationName,
			String userId, String roleName) throws ApplicationException {
		logger.info("Entry into getAllUsersOfAnOrganizationUserAndRole");
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		List<UserVo> listAllUsers = new ArrayList<UserVo>();
		Map<Integer, String> data = roleDao.getAllRolesOfAnOrganization(organizationId);
		try {
			con = getUserMgmConnection();
			String query = "";
			if (roleName.equals("Super Admin")) {
				query = UserConstants.GET_USERS_BY_ORGANIZATION;
			} else {
				query = UserConstants.GET_USERS_BY_ORGANIZATION_USER_ROLE;
			}
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, organizationId);
			preparedStatement.setString(2, "DEL");
			if (!(roleName.equals("Super Admin"))) {
				preparedStatement.setString(3, userId);
				preparedStatement.setString(4, roleName);
			}
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
				if (data.containsKey(userVo.getRoleId())) {
					userVo.setRoleName(data.get(userVo.getRoleId()));
				}
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

	public UserVo getUserDetails(int organizationId, int userId) throws ApplicationException {
		logger.info("Entry into method:getUserDetails");
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		UserVo userVo = null;
		Map<Integer, String> data = roleDao.getAllRolesOfAnOrganization(organizationId);
		try {
			con = getUserMgmConnection();
			String query = UserConstants.GET_USER_IN_ORGANIZATION;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, userId);
			preparedStatement.setInt(2, organizationId);
			preparedStatement.setString(3, "DEL");
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				userVo = new UserVo();
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
				if (data.containsKey(userVo.getRoleId())) {
					userVo.setRoleName(data.get(userVo.getRoleId()));
				}
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
				userVo.setAccessData(rs.getString(22));
			}
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);

		}
		return userVo;
	}

	public int getUserCount(int organizationId, Connection con) throws ApplicationException {
		ResultSet rs = null;
		PreparedStatement preparedStatement = null;
		try {
			String query = UserConstants.GET_USER_COUNT_ORGANIZATION;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, organizationId);
			preparedStatement.setString(2, "DEL");
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				int count = rs.getInt(1);
				return count;
			}
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, null);
		}
		return 0;

	}

	public UserVo getBasicUserDetails(String email) throws ApplicationException {
		logger.info("Entry into method:getBasicUserDetails");
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		UserVo data = null;
		try {
			con = getUserMgmConnection();
			String query = UserConstants.USER_EMAIL;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setString(1, email);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				data = new UserVo();
				rs.getInt(1);
				rs.getInt(2);
				data.setFirstName(rs.getString(3));
				data.setLastName(rs.getString(4));
				
			}
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);

		}
		return data;
	}
	
	public UserVo getBasicUserDetailsById(int userId) throws ApplicationException {
		logger.info("Entry into method:getBasicUserDetails");
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		UserVo data = null;
		try {
			con = getUserMgmConnection();
			String query = UserConstants.USER_BASIC_DETAILS_BY_ID;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, userId);
			preparedStatement.setString(2, CommonConstants.STATUS_AS_DELETE);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				data = new UserVo();
				data.setId(rs.getInt(1));
				data.setFirstName(rs.getString(2));
				data.setLastName(rs.getString(3));
				data.setEmailId(rs.getString(4));
				data.setPhoneNo(rs.getString(5));
				data.setRoleId(rs.getInt(6));
				data.setRoleName(rs.getString(7));
				
			}
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);

		}
		return data;
	}

	public List<UserVo> getBasicUserDetailsInAnOrganization(Connection con, int organizationId)
			throws ApplicationException {
		logger.info("Entry into method:getBasicUserDetailsInAnOrganization");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		List<UserVo> data = new ArrayList<UserVo>();
		try {
			String query = UserConstants.GET_BASIC_USER_DETAILS_OF_ORGANIZATION;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, organizationId);
			preparedStatement.setString(2, "DEL");
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				UserVo userVo = new UserVo();
				userVo.setId(rs.getInt(1));
				userVo.setFirstName(rs.getString(2));
				userVo.setLastName(rs.getString(3));
				userVo.setEmailId(rs.getString(4));
				userVo.setStatus(rs.getString(5));
				data.add(userVo);
			}
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, null);

		}
		return data;
	}
	
	public List<UserVo> getActiveUserDetailsInAnOrganization(Connection con, int organizationId)
			throws ApplicationException {
		logger.info("Entry into method: getActiveUserDetailsInAnOrganization");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		List<UserVo> data = new ArrayList<UserVo>();
		try {
			String query = UserConstants.GET_ACTIVE_USER_DETAILS_OF_ORGANIZATION;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, organizationId);
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
				data.add(userVo);
			}
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, null);

		}
		return data;
	}

	public String getUserName(int id, int organizationId) throws ApplicationException {
		logger.info("To get the getUserName");
		String userName = null;
		try (final Connection con = getUserMgmConnection();
				final PreparedStatement preparedStatement = con.prepareStatement(UserConstants.GET_USER_NAME)) {
			preparedStatement.setInt(2, id);
			preparedStatement.setInt(1, organizationId);
			try (ResultSet rs = preparedStatement.executeQuery()) {
				while (rs.next()) {
					userName = rs.getString(1) ;
				}
			}
		} catch (Exception e) {
			logger.info("Error in getUserName", e);
			throw new ApplicationException(e);
		}
		return userName;
	}

	public boolean getUserByEmail(String email) throws ApplicationException {
		logger.info("Entry into method:getUserByEmail");
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			con = getUserMgmConnection();
			String query = UserConstants.USER_EMAIL;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setString(1, email);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				return true;
			}
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);

		}
		return false;
	}

	public InviteUserVo createUserInvite(InviteUserVo inviteVo) throws ApplicationException {
		logger.info("Entry into method: createUserInvite");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = getUserMgmConnection();
			String sql = UserConstants.INSERT_INTO_USER_INVITE;
			preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

			for (RoleUserVo role : inviteVo.getRoleUser()) {
				for (String email : role.getEmailIdList()) {
					preparedStatement.setInt(1, role.getRoleId());
					preparedStatement.setString(2, email);
					preparedStatement.setString(3, role.getMessage());
					preparedStatement.setInt(4, inviteVo.getOrgId());
					preparedStatement.setString(5, inviteVo.getUserId());
					preparedStatement.setString(6, inviteVo.getRoleName());
					preparedStatement.setString(7, UserConstants.DRAFT);
					preparedStatement.setTimestamp(8, new Timestamp(System.currentTimeMillis()));
					preparedStatement.executeUpdate();
				}

			}
		} catch (Exception e) {
			logger.error("Error during createUserInvite ", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return inviteVo;
	}

	public List<ListSentInvitesVo> getAllUserInvitesOfAnOrganizationUserAndRole(int organizationId, String userId,
			String roleName) throws ApplicationException {
		logger.info("Entry into method: getAllUserInvitesOfAnOrganizationUserAndRole");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		List<ListSentInvitesVo> inviteList = new ArrayList<ListSentInvitesVo>();
		try {
			con = getUserMgmConnection();

			String query = "";
			if (roleName.equals("Super Admin")) {
				query = UserConstants.LIST_USER_INVITES_OF_ORG;
			} else {
				query = UserConstants.LIST_USER_INVITES_OF_ORG_USER_ROLE;
			}
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, organizationId);
			if (!(roleName.equals("Super Admin"))) {
				preparedStatement.setString(2, userId);
				preparedStatement.setString(3, roleName);
			}
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				ListSentInvitesVo invite = new ListSentInvitesVo();
				invite.setTo(rs.getString(1));
				invite.setSentOn(rs.getTimestamp(2));
				invite.setRole(rs.getString(3));
				String status = rs.getString(4);
				if (status != null) {
					switch (status) {
					case UserConstants.DRAFT:
						invite.setStatus(UserConstants.PENDING);
						break;
					case UserConstants.EXP:
						invite.setStatus(UserConstants.EXPIRED);
						break;
					case UserConstants.ACT:
						invite.setStatus(UserConstants.ACCEPTED);
						break;
					case UserConstants.WDRW:
						invite.setStatus(UserConstants.WITHDRAW);
						break;
					case UserConstants.DECL:
						invite.setStatus(UserConstants.DECLINED);
						break;
					}

				}
				invite.setId(rs.getInt(5));
				int roleId = rs.getInt(6);
				RoleVo roleVo = roleDao.getRoleDetails(organizationId, roleId);
				if (roleVo != null) {
					invite.setRole(roleVo.getName());
				}
				invite.setReason(rs.getString(7));
				inviteList.add(invite);
			}
		} catch (Exception e) {
			logger.error("Error during getAllUserInvitesOfAnOrganizationUserAndRole ", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return inviteList;
	}

	public WithdrawInviteVo withdrawInvite(int id, String userId, String roleName) throws ApplicationException {
		// TODO Auto-generated method stub
		logger.info("Entry into method:withdrawInvite");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		WithdrawInviteVo data = null;
		try {
			con = getUserMgmConnection();
			String sql = UserConstants.UPDATE_USER_INVITE;
			preparedStatement = con.prepareStatement(sql);
			preparedStatement.setString(1, "WDRW");
			preparedStatement.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
			preparedStatement.setString(3, userId);
			preparedStatement.setString(4, roleName);
			preparedStatement.setInt(5, id);
			preparedStatement.executeUpdate();
			data = new WithdrawInviteVo();
			data.setId(id);
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);

		}

		return data;
	}

	/*public List<ManageInvitesVo> getManageInvitesDetails(String emailId) throws ApplicationException {
		logger.info("Entry into method: getManageInvitesDetails");
		List<ManageInvitesVo> invites = new ArrayList<ManageInvitesVo>();
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			UserVo user = getUserDetails(organizationId, userId);
			con = getUserMgmConnection();
			String sql = UserConstants.GET_INVITES_BY_EMAIL_USER_ROLE;
			preparedStatement = con.prepareStatement(sql);
			if (user != null) {
				preparedStatement.setString(1, user.getEmailId());
				rs = preparedStatement.executeQuery();
				while (rs.next()) {
					ManageInvitesVo invite = new ManageInvitesVo();
					invite.setId(rs.getInt(1));
					int fromUserId = rs.getInt(2);
					int fromOrgId = rs.getInt(3);
					invite.setReceivedOn(rs.getTimestamp(4));
					int roleId = rs.getInt(5);
					invite.setStatus(rs.getString(6));

					UserVo fromUser = getUserDetails(fromOrgId, fromUserId);
					invite.setFrom(fromUser.getFullName());
					invite.setOrganization(organizationDao.getOrganizationName(fromOrgId));

					RoleVo role = roleDao.getRoleDetails(fromOrgId, roleId);
					invite.setRole(role.getRoleName());
					invites.add(invite);
				}
			}
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);

		}

		return invites;
	}
	 */


	public List<ManageInvitesVo> getManageInvitesDetails(String emailId) throws ApplicationException {
		logger.info("Entry into method: getManageInvitesDetails");
		List<ManageInvitesVo> invites = new ArrayList<ManageInvitesVo>();
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = getUserMgmConnection();
			String sql = UserConstants.GET_INVITES_BY_EMAIL_USER_ROLE;
			preparedStatement = con.prepareStatement(sql);
			preparedStatement.setString(1,emailId);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				ManageInvitesVo invite = new ManageInvitesVo();
				invite.setId(rs.getInt(1));
				int fromUserId = rs.getInt(2);
				int fromOrgId = rs.getInt(3);
				Timestamp receivedOn=rs.getTimestamp(4);					
				int roleId = rs.getInt(5);
				String status=rs.getString(6);
				String fromRoleName=rs.getString(7);
				String message=rs.getString(8);

				NewOrganizationVo newOrganizationVo=organizationDao.getOrganization(fromOrgId);
				invite.setOrganization(newOrganizationVo.getGeneralInfo().getName());
				invite.setReceivedOn(receivedOn);
				//invite.setStatus(status);
				RoleVo role = roleDao.getRoleDetails(fromOrgId, roleId);
				invite.setRole(role.getName());				
				invite.setMessage(message);
				if (status != null) {
					switch (status) {
					case UserConstants.DRAFT:
						invite.setStatus(UserConstants.PENDING);
						break;
					case UserConstants.EXP:
						invite.setStatus(UserConstants.EXPIRED);
						break;
					case UserConstants.ACT:
						invite.setStatus(UserConstants.ACCEPTED);
						break;
					case UserConstants.WDRW:
						invite.setStatus(UserConstants.WITHDRAW);
						break;
					case UserConstants.DECL:
						invite.setStatus(UserConstants.DECLINED);
						break;
					}

				}				
				if(fromRoleName.equals("Super Admin")){
					Connection con1 = getUserMgmConnection();
					String query = "";
					query = LoginAndRegistrationConstants.GET_REGISTRATION_GIVEN_ID;			
					PreparedStatement preparedStatement1 = con1.prepareStatement(query);
					preparedStatement1.setInt(1, fromUserId);
					ResultSet rs1 = preparedStatement1.executeQuery();
					while (rs1.next()) {
						rs1.getInt(1);
						String name=rs1.getString(2)+" " + rs1.getString(3);
						invite.setFrom(name);
						break;
					}
					closeResources(rs1, preparedStatement1, con1);
				}else{
					UserVo fromUser = getUserDetails(fromOrgId, fromUserId);
					invite.setFrom(fromUser.getFirstName()+" " +fromUser.getLastName());
				}	
				invites.add(invite);
			}
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);

		}

		return invites;
	}




	public InviteActionVo takeInviteAction(InviteActionVo inviteActionVo) throws ApplicationException {

		logger.info("Entry into method: takeInviteAction");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;

		try {
			con = getUserMgmConnection();
			String sql = UserConstants.UPDATE_ACTION_INVITE;
			preparedStatement = con.prepareStatement(sql);

			if (inviteActionVo.getAction().equalsIgnoreCase(UserConstants.ACCEPT))
				preparedStatement.setString(1, UserConstants.ACT);
			else if (inviteActionVo.getAction().equalsIgnoreCase(UserConstants.DECLINE))
				preparedStatement.setString(1, UserConstants.DECL);

			preparedStatement.setString(2, inviteActionVo.getReason());
			preparedStatement.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
			/*preparedStatement.setString(4, inviteActionVo.getUserId());
			preparedStatement.setString(5, inviteActionVo.getRoleName());*/
			preparedStatement.setInt(4, inviteActionVo.getId());
			preparedStatement.executeUpdate();

			if (inviteActionVo.getAction().equalsIgnoreCase(UserConstants.ACCEPT)){
				UserVo userVo=getUserInviteDetails(inviteActionVo.getId());		
				RoleVo roleVo = roleDao.getRoleDetails(userVo.getOrganizationId(), userVo.getRoleId());
				String accessData=roleVo.getAccessData();	
				boolean userExist=checkUserExist(userVo.getEmailId(),userVo.getOrganizationId());
				if(userExist){
					UserVo userVo_lc=getIdGivenEmail(userVo.getEmailId(),userVo.getOrganizationId());						
					String sql1 = UserConstants.UPDATE_USER_ACCESS_DATA;
					PreparedStatement preparedStatement1 = con.prepareStatement(sql1);
					preparedStatement1.setString(1, accessData);
					preparedStatement1.setString(2, roleVo.getUserId());
					preparedStatement1.setString(3, roleVo.getRoleName());
					preparedStatement1.setInt(4,userVo_lc.getId());			   				
					preparedStatement1.executeUpdate();

				}else{
					UserVo data= new UserVo();
					data.setFirstName(userVo.getEmailId());
					data.setLastName("User");
					data.setOrganizationId(userVo.getOrganizationId());
					data.setFullName(data.getFirstName());
					data.setEmailId(userVo.getEmailId());
					data.setStatus("ACT");
					data.setRoleId(userVo.getRoleId());
					data.setUserId(userVo.getUserId());
					data.setRoleName(userVo.getRoleName());	
					data.setAccessData(accessData);
					//data.setGender("Others");
					data.setPhoneNo("99999999999");
					//data.setDob(new Timestamp(System.currentTimeMillis()).toString());
					RegisteredAddressVo addressVo=new RegisteredAddressVo();
					addressVo.setAddressLine1("Addr1");
					addressVo.setAddressLine2("Addr2");
					addressVo.setState(0);
					addressVo.setCountry(0);
					addressVo.setPinCode("");
					addressVo.setCity("");
					addressVo.setUserId(userVo.getUserId());
					data.setRegisteredAddress(addressVo);
					createUser(data);
				}
			}

		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);

		}

		return inviteActionVo;

	}


	public UserVo getUserInviteDetails(int id) throws ApplicationException {
		logger.info("Entry into method: getUserInviteDetails");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		UserVo userVo=null;
		try {
			con = getUserMgmConnection();
			String query = "";
			query = UserConstants.LIST_SINGLE_USER_INVITE;			
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, id);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				userVo = new UserVo();
				userVo.setEmailId(rs.getString(1));
				userVo.setRoleId(rs.getInt(2));
				userVo.setOrganizationId(rs.getInt(3));
				userVo.setUserId(rs.getString(4));
				userVo.setRoleName(rs.getString(5));
			}
		} catch (Exception e) {
			logger.error("Error during getAUserInvitesOfAnOrganization ", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return userVo;
	}


	private UserVo getIdGivenEmail(String email,int organizationId) throws ApplicationException {
		logger.info("Entry into getIdGivenEmail");
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		UserVo data = null;
		try {
			con = getUserMgmConnection();
			String query = UserConstants.USER_EMAIL_ORGANIZATION;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, organizationId);
			preparedStatement.setString(2, email);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				data = new UserVo();
				data.setId(rs.getInt(1));
			}
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);

		}
		return data;
	}

	
}

package com.blackstrawai.onboarding;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.accessandroles.AccessAndRolesDao;
import com.blackstrawai.common.BaseDao;
import com.blackstrawai.onboarding.loginandregistration.LoginVo;
import com.blackstrawai.onboarding.loginandregistration.ProfileVo;
import com.blackstrawai.onboarding.loginandregistration.RegistrationVo;

@Repository
public class RegistrationDao extends BaseDao {

	private Logger logger = Logger.getLogger(RegistrationDao.class);

	@Autowired
	AccessAndRolesDao accessAndRolesDao;

	public RegistrationVo register(RegistrationVo registrationVo) throws ApplicationException {
		logger.info("Entry into method:register");
		String accessData;
		RegistrationVo regVo = getRegistrationByEmail(registrationVo.getEmailId());
		if (regVo != null) {
			throw new ApplicationException("User Already Exist");
		}
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = getUserMgmConnection();
			String sql = LoginAndRegistrationConstants.INSERT_INTO_REGISTRATION;

			preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setString(1, registrationVo.getFirstName());
			preparedStatement.setString(2, registrationVo.getLastName());
			preparedStatement.setString(3, registrationVo.getFullName());
			preparedStatement.setString(4, registrationVo.getEmailId());
			preparedStatement.setString(5, registrationVo.getMobileNo());
			preparedStatement.setString(6, registrationVo.getPhoneNo());
			preparedStatement.setBoolean(7, registrationVo.isIndividual());
			preparedStatement.setBoolean(8, registrationVo.isOrganization());
			preparedStatement.setInt(9, registrationVo.getSubscriptionId());
			preparedStatement.setString(10, registrationVo.getPassword());
			preparedStatement.setString(11, registrationVo.getUserToken());
			accessData = accessAndRolesDao.getAccess(1);
			preparedStatement.setString(12, accessData);
			int rowAffected = preparedStatement.executeUpdate();
			if (rowAffected == 1) {
				rs = preparedStatement.getGeneratedKeys();
				if (rs.next()) {
					registrationVo.setId(rs.getInt(1));
				}
			}

		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);

		}

		return registrationVo;
	}
	
	public ProfileVo updateUserProfile(ProfileVo profileVo) throws ApplicationException {
		logger.info("Entry into method: updateUserProfile");
		Connection con = null;
		PreparedStatement preparedStatement = null;
		try {
			con = getUserMgmConnection();
			String query = LoginAndRegistrationConstants.UPDATE_USER_PROFILE;
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
	
	public ProfileVo updateKeyContactsProfile(ProfileVo profileVo) throws ApplicationException {
		logger.info("Entry into method: updateUserProfile");
		Connection con = null;
		PreparedStatement preparedStatement = null;
		try {
			con = getUserMgmConnection();
			String query = LoginAndRegistrationConstants.UPDATE_KEY_CONTACTS_PROFILE;
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

	public List<RegistrationVo> getAllRegistrations() throws ApplicationException {
		logger.info("Entry into method:getAllRegistrations");
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		List<RegistrationVo> listAllRegistrations = new ArrayList<RegistrationVo>();
		try {
			con = getUserMgmConnection();
			String query = LoginAndRegistrationConstants.GET_ALL_REGISTRATIONS;
			preparedStatement = con.prepareStatement(query);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				RegistrationVo registrationVo = new RegistrationVo();
				registrationVo.setId(rs.getInt(1));
				registrationVo.setFirstName(rs.getString(2));
				registrationVo.setLastName(rs.getString(3));
				registrationVo.setFullName(rs.getString(4));
				registrationVo.setEmailId(rs.getString(5));
				registrationVo.setMobileNo(rs.getString(6));
				registrationVo.setPhoneNo(rs.getString(7));
				registrationVo.setIndividual(rs.getBoolean(8));
				registrationVo.setOrganization(rs.getBoolean(9));
				registrationVo.setSubscriptionId(rs.getInt(10));
				listAllRegistrations.add(registrationVo);
			}
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);

		}
		return listAllRegistrations;
	}

	@SuppressWarnings("resource")
	public RegistrationVo getRegistrationByEmail(String email) throws ApplicationException {
		logger.info("Entry into method:getRegistrationByEmail");
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		RegistrationVo registrationVo = null;
		try {
			con = getUserMgmConnection();
			String query = LoginAndRegistrationConstants.REGISTRATION_EMAIL;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setString(1, email);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				registrationVo = new RegistrationVo();
				registrationVo.setId(rs.getInt(1));
				registrationVo.setFirstName(rs.getString(2));
				registrationVo.setLastName(rs.getString(3));
				registrationVo.setFullName(rs.getString(4));
				registrationVo.setEmailId(rs.getString(5));
				registrationVo.setMobileNo(rs.getString(6));
				registrationVo.setPhoneNo(rs.getString(7));
				registrationVo.setIndividual(rs.getBoolean(8));
				registrationVo.setOrganization(rs.getBoolean(9));
				registrationVo.setSubscriptionId(rs.getInt(10));
				return registrationVo;
			}

			// To verify in key contact table
			query = LoginAndRegistrationConstants.KEY_CONTACTS_REGISTRATION_EMAIL_PASSWORD;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setString(1, email);
			// preparedStatement.setString(2,loginVo.getPassword());
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				registrationVo = new RegistrationVo();
				registrationVo.setId(rs.getInt(1));
				registrationVo.setFirstName(rs.getString(2));
				registrationVo.setLastName("");
				registrationVo.setEmailId(email);
				registrationVo.setPhoneNo(rs.getString(3));
				registrationVo.setPassword(null);
				return registrationVo;
			}
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);

		}
		return registrationVo;
	}
	
	
	public RegistrationVo getRegistrationByEmailFromRegistrationEntity(String email) throws ApplicationException {
		logger.info("Entry into method:getRegistrationByEmailFromRegistrationEntity");
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		RegistrationVo registrationVo = null;
		try {
			con = getUserMgmConnection();
			String query = LoginAndRegistrationConstants.REGISTRATION_EMAIL_REGISTRATION;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setString(1, email);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				registrationVo = new RegistrationVo();
				registrationVo.setId(rs.getInt(1));
				registrationVo.setFirstName(rs.getString(2));
				registrationVo.setLastName(rs.getString(3));
				registrationVo.setFullName(rs.getString(4));
				registrationVo.setEmailId(rs.getString(5));
				registrationVo.setMobileNo(rs.getString(6));
				registrationVo.setPhoneNo(rs.getString(7));
				registrationVo.setIndividual(rs.getBoolean(8));
				registrationVo.setOrganization(rs.getBoolean(9));
				registrationVo.setSubscriptionId(rs.getInt(10));
				registrationVo.setAccessData(rs.getString(11));
				return registrationVo;
			}

		
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);

		}
		return registrationVo;
	}

	@SuppressWarnings("resource")
	public RegistrationVo checkRegisteredUserExist(LoginVo loginVo) throws ApplicationException {
		logger.info("Entry into method:checkRegisteredUserExist");
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		RegistrationVo registrationVo = null;
		try {
			con = getUserMgmConnection();
			String query = LoginAndRegistrationConstants.REGISTRATION_EMAIL_PASSWORD;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setString(1, loginVo.getUserId());
			// preparedStatement.setString(2,loginVo.getPassword());
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				registrationVo = new RegistrationVo();
				registrationVo.setId(rs.getInt(1));
				registrationVo.setFirstName(rs.getString(2));
				registrationVo.setLastName(rs.getString(3));
				registrationVo.setFullName(rs.getString(4));
				registrationVo.setEmailId(rs.getString(5));
				registrationVo.setMobileNo(rs.getString(6));
				registrationVo.setPhoneNo(rs.getString(7));
				registrationVo.setIndividual(rs.getBoolean(8));
				registrationVo.setOrganization(rs.getBoolean(9));
				registrationVo.setSubscriptionId(rs.getInt(10));
				String password = rs.getString(11);
				if (password.equals(loginVo.getPassword())) {
					registrationVo.setPassword(null);
				} else {
					throw new ApplicationException("Password does not match");
				}
				registrationVo.setRoleName("Super Admin");
				return registrationVo;
			}
			// To verify the vendor login is successfull
			query = LoginAndRegistrationConstants.KEY_CONTACTS_REGISTRATION_EMAIL_PASSWORD;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setString(1, loginVo.getUserId());
			// preparedStatement.setString(2,loginVo.getPassword());
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				registrationVo = new RegistrationVo();
				registrationVo.setId(rs.getInt(1));
				registrationVo.setFirstName(rs.getString(2));
				registrationVo.setLastName("");
				registrationVo.setEmailId(loginVo.getUserId());
				registrationVo.setPhoneNo(rs.getString(3));
				String password = rs.getString(4);
				if (password.equals(loginVo.getPassword())) {
					registrationVo.setPassword(null);
				} else {
					throw new ApplicationException("Password does not match");
				}
				registrationVo.setRoleName("Vendor");
				return registrationVo;
			}

		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);

		}
		return registrationVo;
	}

	
	public String getRegisteredUserName(int id) throws ApplicationException {
		logger.info("To get the getUserName");
		String userName = null;
		try (final Connection con = getUserMgmConnection();
				final PreparedStatement preparedStatement = con.prepareStatement(LoginAndRegistrationConstants.GET_REGISTERED_USER_NAME)) {
			preparedStatement.setInt(1, id);
			try (ResultSet rs = preparedStatement.executeQuery()) {
				while (rs.next()) {
					userName = rs.getString(1);
				}
			}
		} catch (Exception e) {
			logger.info("Error in getUserName", e);
			throw new ApplicationException(e);
		}
		return userName;
	}
}

package com.blackstrawai.onboarding;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.attachments.AttachmentsDao;
import com.blackstrawai.attachments.UploadFileVo;
import com.blackstrawai.common.BaseDao;
import com.blackstrawai.common.TokenVo;
import com.blackstrawai.onboarding.loginandregistration.LoginVo;
import com.blackstrawai.onboarding.loginandregistration.ProfileVo;
import com.blackstrawai.onboarding.loginandregistration.RecoverPasswordVo;
import com.blackstrawai.onboarding.loginandregistration.RegistrationVo;
import com.blackstrawai.onboarding.loginandregistration.ResetPasswordVo;
import com.blackstrawai.onboarding.role.RoleVo;
import com.blackstrawai.onboarding.user.RegisteredAddressVo;
import com.blackstrawai.onboarding.user.UserVo;

@Repository
public class LoginDao extends BaseDao{

	@Autowired
	RegistrationDao registrationDao;
	
	@Autowired
	RoleDao roleDao;
	
	@Autowired
	UserDao userDao;
	
	@Autowired
	AttachmentsDao attachmentsDao;

	private  Logger logger = Logger.getLogger(LoginDao.class);

	public TokenVo login(LoginVo loginVo)throws ApplicationException {	
		logger.info("Entry into method:login");
		RegistrationVo registrationVo=registrationDao.checkRegisteredUserExist(loginVo);
		if(registrationVo!=null){
			return registrationVo;
		}else{
			UserVo userVo=checkUserExist(loginVo);
			if(userVo!=null){
				return userVo;
			}else{
				throw new ApplicationException("User Does Not Exist");
			}			
		}
	}


	private UserVo checkUserExist(LoginVo loginVo)throws ApplicationException {	
		logger.info("Entry into method:checkUserExist");
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		UserVo userVo=null;
		try{
			con = getUserMgmConnection();
			String query=UserConstants.CHECK_USER_EXIST;		
			preparedStatement=con.prepareStatement(query);
			preparedStatement.setString(1,loginVo.getUserId());
			//preparedStatement.setString(2,loginVo.getPassword());
			
			rs = preparedStatement.executeQuery();			
			if (rs.next()) {
				userVo=new UserVo();
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
				RegisteredAddressVo address=new RegisteredAddressVo();
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
				String password=rs.getString(20);
				RoleVo roleVo=roleDao.getRoleDetails(userVo.getOrganizationId(), userVo.getRoleId());
				userVo.setRoleName(roleVo.getRoleName());
				if(password.equals(loginVo.getPassword())){
					logger.info("Password matches");
				}else{
					throw new ApplicationException("Password does not match");
				}
			}
			
		}catch(Exception e){
			throw new ApplicationException(e);
		}finally {
			closeResources(rs, preparedStatement, con);
		}	
		RoleVo roleVo=roleDao.getRoleDetails(userVo.getOrganizationId(), userVo.getRoleId());
		if(roleVo!=null){
			userVo.setRoleName(roleVo.getName());
		}
		return userVo;
	}


	public void  updateUserToken(int userId, String token)throws ApplicationException{
		logger.info("Entry into method:updateUserToken");
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs=null;
		try
		{
			con = getUserMgmConnection();
			String sql = UserConstants.UPDATE_USER_TOKEN_ID;			
			preparedStatement = con.prepareStatement(sql);
			preparedStatement.setString(1, token);
			preparedStatement.setInt(2, userId);
			preparedStatement.executeUpdate();		
		}catch(Exception e){
			throw new ApplicationException(e);
		}finally {
			closeResources(rs, preparedStatement, con);

		}	

	}


	@SuppressWarnings("resource")
	public RecoverPasswordVo recoverPassword(RecoverPasswordVo recoverPasswordVo)throws ApplicationException {	
		logger.info("Entry into method:recoverPassword");
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs=null;
		try
		{
			con = getUserMgmConnection();
			RegistrationVo registrationVo=registrationDao.getRegistrationByEmail(recoverPasswordVo.getEmailId());			
			if(registrationVo!=null){
				String sql = LoginAndRegistrationConstants.UPDATE_REGISTRATION_TOKEN;			
				preparedStatement = con.prepareStatement(sql);
				preparedStatement.setString(1, recoverPasswordVo.getUserToken());
				preparedStatement.setString(2, recoverPasswordVo.getEmailId());
				preparedStatement.executeUpdate();	
				
				sql = LoginAndRegistrationConstants.UPDATE_KEY_CONTACTS__REGISTRATION_TOKEN;			
				preparedStatement = con.prepareStatement(sql);
				preparedStatement.setString(1, recoverPasswordVo.getUserToken());
				preparedStatement.setString(2, recoverPasswordVo.getEmailId());
				preparedStatement.executeUpdate();	
				return recoverPasswordVo;
			}else{
				boolean isUserExist=userDao.getUserByEmail(recoverPasswordVo.getEmailId());
				if(isUserExist){
					String sql = UserConstants.UPDATE_USER_TOKEN;			
					preparedStatement = con.prepareStatement(sql);
					preparedStatement.setString(1, recoverPasswordVo.getUserToken());
					preparedStatement.setString(2, recoverPasswordVo.getEmailId());
					preparedStatement.executeUpdate();		
					return recoverPasswordVo;
				}else{
					throw new ApplicationException("User Email Does Not Exist");
				}			
			}
		}catch(Exception e){
			throw new ApplicationException(e);
		}finally {
			closeResources(rs, preparedStatement, con);

		}	

	}	

	@SuppressWarnings("resource")
	public void changePassword(String existingpassword,String password,String email)throws ApplicationException {	
		logger.info("Entry into method: changePassword");
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs=null;
		boolean pwdMatch=false;
		
		try
		{


			con = getUserMgmConnection();	
			con.setAutoCommit(false);
			preparedStatement = con.prepareStatement(LoginAndRegistrationConstants.GET_USER_PWD_BY_EMAIL);
			preparedStatement.setString(1, email);
			rs=preparedStatement.executeQuery();
			while(rs.next()){
				String pwd=rs.getString(1);
				if(pwd!=null && pwd.equals(existingpassword)) {
				pwdMatch=true;
				}
			}
			preparedStatement = con.prepareStatement(LoginAndRegistrationConstants.GET_REGISTRATION_PWD_BY_EMAIL);
			preparedStatement.setString(1, email);
			rs=preparedStatement.executeQuery();
			while(rs.next()){
				String pwd=rs.getString(1);
				if(pwd!=null && pwd.equals(existingpassword)) {
				pwdMatch=true;
				}
			}
			preparedStatement = con.prepareStatement(LoginAndRegistrationConstants.GET_KEY_CONTACT_REG_PWD_BY_EMAIL);
			preparedStatement.setString(1, email);
			rs=preparedStatement.executeQuery();
			while(rs.next()){
				String pwd=rs.getString(1);
				if(pwd!=null && pwd.equals(existingpassword)) {
				pwdMatch=true;
				}
			}

			
			if(!pwdMatch) {
				throw new ApplicationException("Old Pasword doesn't match");
			}
			
		
			
				preparedStatement = con.prepareStatement(LoginAndRegistrationConstants.UPDATE_REGISTRATION_PASSWORD_BY_EMAIL);
				preparedStatement.setString(1, password);
				preparedStatement.setString(2, email);
				preparedStatement.executeUpdate();
			
				preparedStatement = con.prepareStatement(LoginAndRegistrationConstants.UPDATE_USER_PASSWORD_BY_EMAIL);
				preparedStatement.setString(1, password);
				preparedStatement.setString(2, email);
				preparedStatement.executeUpdate();
				
				preparedStatement = con.prepareStatement(LoginAndRegistrationConstants.UPDATE_KEY_CONTACTS_PASSWORD_BY_EMAIL);
				preparedStatement.setString(1, password);
				preparedStatement.setString(2, email);
				preparedStatement.executeUpdate();
				logger.info("Updated in user");

				con.commit();
		}catch(Exception e){
			try {
				con.rollback();
			} catch (SQLException e1) {
				throw new ApplicationException(e);

			}
			logger.error("Error changePassword:",e);
			throw new ApplicationException(e);
		}finally {
			closeResources(rs, preparedStatement, con);

		}	

	}
	
	@SuppressWarnings("resource")
	public ResetPasswordVo resetPassword(ResetPasswordVo resetPasswordVo)throws ApplicationException {	
		logger.info("Entry into method:resetPassword");
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs=null;
		try
		{
			con = getUserMgmConnection();
			String sql = LoginAndRegistrationConstants.CHECK_REGISTRATION_TOKEN_EXIST;			
			preparedStatement = con.prepareStatement(sql);
			preparedStatement.setString(1,resetPasswordVo.getResetToken() );
			rs = preparedStatement.executeQuery();		
			while(rs.next()){	
				String email = rs.getString(1);
				sql = LoginAndRegistrationConstants.UPDATE_REGISTRATION_PASSWORD;			
				preparedStatement = con.prepareStatement(sql);
				preparedStatement.setString(1, resetPasswordVo.getPassword());
				preparedStatement.setString(2, resetPasswordVo.getResetToken());
				preparedStatement.executeUpdate();
				logger.info("Updated in reg table ");
				logger.info("Passwrd to be updated for email :: "+ email);
				sql = LoginAndRegistrationConstants.UPDATE_PASSWORD_IN_KEY_CONTACT;
				preparedStatement = con.prepareStatement(sql);
				preparedStatement.setString(1, resetPasswordVo.getPassword());
				preparedStatement.setString(2, null);
				preparedStatement.setString(3, email);
				preparedStatement.executeUpdate();
				return resetPasswordVo;						
			}
			
			//To update the Password if email exist in Key contact table 
			sql = LoginAndRegistrationConstants.GET_EMAIL_FOR_TOKEN__IN_KEY_CONTACT;	
			preparedStatement = con.prepareStatement(sql);
			preparedStatement.setString(1,resetPasswordVo.getResetToken() );
			rs = preparedStatement.executeQuery();	
			while(rs.next()){		
				String email = rs.getString(1);
				logger.info("Passwrd to be updated for email :: "+ email);
				sql = LoginAndRegistrationConstants.UPDATE_PASSWORD_IN_KEY_CONTACT;
				preparedStatement = con.prepareStatement(sql);
				preparedStatement.setString(1, resetPasswordVo.getPassword());
				preparedStatement.setString(2, null);
				preparedStatement.setString(3, email);
				preparedStatement.executeUpdate();
				logger.info("Passwrd updated in key contact .. ");
				return resetPasswordVo;						

			}
			
			sql = UserConstants.CHECK_USER_TOKEN_EXIST;			
			preparedStatement = con.prepareStatement(sql);
			preparedStatement.setString(1,resetPasswordVo.getResetToken() );
			rs = preparedStatement.executeQuery();	
			while(rs.next()){
				sql = UserConstants.UPDATE_USER_PASSWORD;			
				preparedStatement = con.prepareStatement(sql);
				preparedStatement.setString(1, resetPasswordVo.getPassword());
				preparedStatement.setString(2, resetPasswordVo.getResetToken());
				preparedStatement.executeUpdate();
				logger.info("Updated in user");

				return resetPasswordVo;	
			}
			throw new ApplicationException("Invalid Credentials");

		}catch(Exception e){
			throw new ApplicationException(e);
		}finally {
			closeResources(rs, preparedStatement, con);

		}	

	}

	
	@SuppressWarnings("resource")
	public ResetPasswordVo createPassword(ResetPasswordVo resetPasswordVo)throws ApplicationException {	
		logger.info("Entry into method:createPassword");
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs=null;
		try
		{
			con = getUserMgmConnection();
			String sql = LoginAndRegistrationConstants.CHECK_REGISTRATION_TOKEN_EXIST;			
			preparedStatement = con.prepareStatement(sql);
			preparedStatement.setString(1,resetPasswordVo.getResetToken() );
			rs = preparedStatement.executeQuery();		
			while(rs.next()){
				String email = rs.getString(1);
				sql = LoginAndRegistrationConstants.UPDATE_REGISTRATION_PASSWORD;			
				preparedStatement = con.prepareStatement(sql);
				preparedStatement.setString(1, resetPasswordVo.getPassword());
				preparedStatement.setString(2, resetPasswordVo.getResetToken());
				preparedStatement.executeUpdate();
				//To update in Key contatct table when the use is available in Reg table
				logger.info("Passwrd to be updated for email :: "+ email);
				sql = LoginAndRegistrationConstants.UPDATE_PASSWORD_IN_KEY_CONTACT;
				preparedStatement = con.prepareStatement(sql);
				preparedStatement.setString(1, resetPasswordVo.getPassword());
				preparedStatement.setString(2, null);
				preparedStatement.setString(3, email);
				preparedStatement.executeUpdate();
				logger.info("Passwrd updated.. ");
				
				return resetPasswordVo;						
			}	
			
			//To update the Password if email exist in Key contact table and not in reg table
			sql = LoginAndRegistrationConstants.GET_EMAIL_FOR_TOKEN__IN_KEY_CONTACT;	
			preparedStatement = con.prepareStatement(sql);
			preparedStatement.setString(1,resetPasswordVo.getResetToken() );
			rs = preparedStatement.executeQuery();	
			while(rs.next()){		
				String email = rs.getString(1);
				logger.info("Passwrd to be updated for email :: "+ email);
				sql = LoginAndRegistrationConstants.UPDATE_PASSWORD_IN_KEY_CONTACT;
				preparedStatement = con.prepareStatement(sql);
				preparedStatement.setString(1, resetPasswordVo.getPassword());
				preparedStatement.setString(2, null);
				preparedStatement.setString(3, email);
				preparedStatement.executeUpdate();
				logger.info("Passwrd updated.. ");
				return resetPasswordVo;						

			}
			
			sql = UserConstants.CHECK_USER_TOKEN_EXIST;			
			preparedStatement = con.prepareStatement(sql);
			preparedStatement.setString(1,resetPasswordVo.getResetToken() );
			rs = preparedStatement.executeQuery();	
			while(rs.next()){
				sql = UserConstants.UPDATE_USER_PASSWORD;			
				preparedStatement = con.prepareStatement(sql);
				preparedStatement.setString(1, resetPasswordVo.getPassword());
				preparedStatement.setString(2, resetPasswordVo.getResetToken());
				preparedStatement.executeUpdate();
				return resetPasswordVo;	
			}
			throw new ApplicationException("Invalid Credentials");

		}catch(Exception e){
			throw new ApplicationException(e);
		}finally {
			closeResources(rs, preparedStatement, con);

		}	

	}
	
	public ProfileVo updateProfile(ProfileVo profileVo)throws ApplicationException {
		if(profileVo!=null) {
		userDao.updateUserProfile(profileVo);
		registrationDao.updateUserProfile(profileVo);
		registrationDao.updateKeyContactsProfile(profileVo);
		attachmentsDao.createProfileAttachment(profileVo.getEmailId(), profileVo.getProfilePic());
		}
		return profileVo;
	}
	
	public void setDeafultOrganization(String email,int organizationId) throws ApplicationException {
		logger.info("Entry into setDeafultOrganization");
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Integer id=null;
		try
		{
			con = getUserMgmConnection();
			preparedStatement = con.prepareStatement(LoginAndRegistrationConstants.CHECK_ORG_DEFAULT_EXIST_FOR_EMAIL);
			preparedStatement.setString(1,email);
			rs = preparedStatement.executeQuery();		
			String query=LoginAndRegistrationConstants.CREATE_ORG_DEFAULT;
			while(rs.next()){	
				id= rs.getInt(1);
			}
			if(id!=null) {
				query=LoginAndRegistrationConstants.UPDATE_ORG_DEFAULT;
			}	
			closeResources(rs, preparedStatement, null);
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setString(1, email);
			preparedStatement.setInt(2, organizationId);
			if(id!=null) {
				preparedStatement.setInt(3, id);
			}
			preparedStatement.executeUpdate();

		}catch(Exception e){
			logger.error(" Error in setDeafultOrganization",e);
			throw new ApplicationException(e);
		}finally {
			closeResources(rs, preparedStatement, con);

		}	
	}


	@SuppressWarnings("resource")
	public ProfileVo getProfileByEmail(String email)throws ApplicationException {	
		logger.info("Entry into method: getProfileByEmail");
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs=null;
		ProfileVo profileVo=null;
		try
		{
			con = getUserMgmConnection();
			preparedStatement = con.prepareStatement(LoginAndRegistrationConstants.GET_PROFILE_REGISTRATION_BY_EMAIL);
			preparedStatement.setString(1,email );
			rs = preparedStatement.executeQuery();		
			
			while(rs.next()){	
				profileVo=new ProfileVo();
				profileVo.setFirstName(rs.getString(1));
				profileVo.setLastName(rs.getString(2));
				profileVo.setMobileNo(rs.getString(3));
				profileVo.setEmailId(rs.getString(4));
				logger.info("Found in Registation");
			}
			if(profileVo==null) {
			preparedStatement = con.prepareStatement(LoginAndRegistrationConstants.GET_PROFILE_USER_BY_EMAIL);
			preparedStatement.setString(1,email );
			rs = preparedStatement.executeQuery();	
			while(rs.next()){
				profileVo=new ProfileVo();
				profileVo.setFirstName(rs.getString(1));
				profileVo.setLastName(rs.getString(2));
				profileVo.setMobileNo(rs.getString(3));
				profileVo.setEmailId(rs.getString(4));
				logger.info("Found in User");

			}
			}
			if(profileVo==null) {
			preparedStatement = con.prepareStatement(LoginAndRegistrationConstants.GET_PROFILE_KEY_CONTACTS_BY_EMAIL);
			preparedStatement.setString(1,email );
			rs = preparedStatement.executeQuery();	
			while(rs.next()){
				profileVo=new ProfileVo();
				profileVo.setFirstName(rs.getString(1));
				profileVo.setLastName(rs.getString(2));
				profileVo.setMobileNo(rs.getString(3));
				profileVo.setEmailId(rs.getString(4));
				logger.info("Found in Key Contact");
			}
			}
			if(profileVo!=null && profileVo.getEmailId()!=null) {
				UploadFileVo fileVo=attachmentsDao.getProfileAttachment(profileVo.getEmailId());
				logger.info("fileVo:"+fileVo);
				profileVo.setProfilePic(fileVo);
			}

		}catch(Exception e){
			logger.error("Error in getProfileByEmail",e);
			throw new ApplicationException(e);
		}finally {
			closeResources(rs, preparedStatement, con);

		}	
		return profileVo;

	}


	


}
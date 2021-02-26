package com.blackstrawai.onboarding;

import java.io.FileReader;
import java.util.Properties;
import java.util.Random;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.attachments.AttachmentService;
import com.blackstrawai.attachments.UploadFileVo;
import com.blackstrawai.common.BaseService;
import com.blackstrawai.common.CacheService;
import com.blackstrawai.common.TokenVo;
import com.blackstrawai.general.PythonIntegration;
import com.blackstrawai.onboarding.loginandregistration.ChangePasswordVo;
import com.blackstrawai.onboarding.loginandregistration.LoginVo;
import com.blackstrawai.onboarding.loginandregistration.LogoutVo;
import com.blackstrawai.onboarding.loginandregistration.ProfileVo;
import com.blackstrawai.onboarding.loginandregistration.RecoverPasswordVo;
import com.blackstrawai.onboarding.loginandregistration.RegistrationVo;
import com.blackstrawai.onboarding.loginandregistration.ResetPasswordVo;
import com.blackstrawai.onboarding.user.UserVo;

@Service
public class LoginService extends BaseService {
	
	private  Logger logger = Logger.getLogger(LoginService.class);

	@Autowired
	LoginDao loginDao;
	
	@Autowired
	RegistrationDao registrationDao;
	
	@Autowired
	UserDao userDao;
	
	@Autowired
	CacheService cacheService;
	
	@Autowired
	private AttachmentService attachmentService;
	
	public TokenVo login(LoginVo loginVo)throws ApplicationException {	
		logger.info("Entry into method:login");
		String hashedPassword=generateHashToken(loginVo.getPassword());
		loginVo.setPassword(hashedPassword);
		TokenVo tokenVo=loginDao.login(loginVo);		
		String keyToken=null;
		String valueToken=null;
		synchronized (cacheService) {
			keyToken=new Long(System.currentTimeMillis()).toString();
			valueToken=generateHashToken(keyToken.concat(loginVo.getUserId()));
			tokenVo.setKeyToken(keyToken);
			tokenVo.setValueToken(valueToken);
			cacheService.addToken(keyToken, valueToken);
			loginVo.setPassword(null);
			PythonIntegration.getInstance().login(keyToken,valueToken);
		}		
		
		return tokenVo;
	}
	
	public void logout(LogoutVo logoutVo)throws ApplicationException  {
		logger.info("Entry into method:logout");
		cacheService.removeToken(logoutVo.getKeyToken());
	}
	
	public RecoverPasswordVo recoverPassword(RecoverPasswordVo forgotPasswordVo)throws ApplicationException  {	
		logger.info("Entry into method:recoverPassword");		
		RegistrationVo registrationVo=registrationDao.getRegistrationByEmail(forgotPasswordVo.getEmailId());
		String firstName=null,lastName=null;
		if(registrationVo!=null){
			firstName=registrationVo.getFirstName();
			lastName=registrationVo.getLastName();
		}else{
			UserVo userVo=userDao.getBasicUserDetails(forgotPasswordVo.getEmailId());
			if(userVo==null){
				throw new ApplicationException("User Email Does Not Exist");
			}else{
				firstName=userVo.getFirstName();
				lastName=userVo.getLastName();
			}
		}
		Random rand = new Random(); 
		int value=rand.nextInt(1000);
		long time=System.currentTimeMillis();
		forgotPasswordVo.setUserToken(generateHashToken("generateHashToken"+value+time));
		RecoverPasswordVo recoverPasswordVo=loginDao.recoverPassword(forgotPasswordVo);
		
		String url;
		FileReader reader;
		try {
			reader = new FileReader("/decifer/config/app_config.properties");
			Properties p=new Properties();  
			p.load(reader);  
			url=p.getProperty("decifer_url");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			url="https://decifer.blackstrawlab.com";
		}        
		
		String message="Hi " +firstName + " " +lastName + ",\r\n\r\n  Please use the following link " +url+"/auth/reset-password/" +forgotPasswordVo.getUserToken()+ " to reset password.\r\n\r\n Regards,\r\n Decifer Admin";
		EmailThread emailThread=new EmailThread(message,forgotPasswordVo.getEmailId(),"Reset Password");
		emailThread.start();
	    return recoverPasswordVo;	
	}
	
	public ChangePasswordVo changePassword(ChangePasswordVo changePasswordVo)throws ApplicationException  {		
		String password=generateHashToken(changePasswordVo.getNewPassword());
		changePasswordVo.setNewPassword(password);
			loginDao.changePassword(generateHashToken(changePasswordVo.getOldPassword()),changePasswordVo.getNewPassword(), changePasswordVo.getEmail());
		return changePasswordVo;
	}
		
	public ResetPasswordVo resetPassword(ResetPasswordVo resetPasswordVo)throws ApplicationException  {		
		logger.info("Entry into method:resetPassword");
		String password=generateHashToken(resetPasswordVo.getPassword());
		resetPasswordVo.setPassword(password);
		return loginDao.resetPassword(resetPasswordVo);
	}
	
	public ResetPasswordVo createPassword(ResetPasswordVo resetPasswordVo)throws ApplicationException  {		
		logger.info("Entry into method:createPassword");
		String password=generateHashToken(resetPasswordVo.getPassword());
		resetPasswordVo.setPassword(password);
		return loginDao.createPassword(resetPasswordVo);
	}
	
	public ProfileVo updateProfile(ProfileVo profileVo)throws ApplicationException {
		logger.info("Entry into method: updateProfile");
		UploadFileVo fileVo=null;
		if(profileVo!=null ) {
			fileVo=profileVo.getProfilePic();
			
			if (fileVo != null) {
				long uid = System.currentTimeMillis();
				String fileName = fileVo.getName() != null ? uid + "-" + fileVo.getName()
						: uid + System.currentTimeMillis() + ".jpg";
				fileVo.setName(fileName);
				profileVo.setProfilePic(fileVo);
			}
			
		}
		profileVo=loginDao.updateProfile(profileVo);
		if(fileVo!=null) {
			attachmentService.uploadProfile(profileVo.getProfilePic().getName(), profileVo.getProfilePic());
		}
		return profileVo;
	}
	
	public void setDeafultOrganization(String email,int orgId)throws ApplicationException {
		logger.info("Entry into method: setDeafultOrganization");
		
		loginDao.setDeafultOrganization(email, orgId);
	}
	
	public ProfileVo getProfileByEmail(String email)throws ApplicationException {
		logger.info("Entry into method: getProfileByEmail");
		
		ProfileVo profileVo=loginDao.getProfileByEmail(email );
		if (profileVo != null && profileVo.getProfilePic()!=null ) {
			attachmentService.encodeUploadedProfile(profileVo.getProfilePic());
		}
		return profileVo;
	}
	
	
}

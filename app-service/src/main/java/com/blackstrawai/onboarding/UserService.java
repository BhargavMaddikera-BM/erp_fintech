package com.blackstrawai.onboarding;

import java.io.FileReader;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.common.BaseService;
import com.blackstrawai.common.DropDownDao;
import com.blackstrawai.keycontact.dropdowns.UserDropDownVo;
import com.blackstrawai.onboarding.loginandregistration.RegistrationVo;
import com.blackstrawai.onboarding.role.RoleVo;
import com.blackstrawai.onboarding.user.InviteActionVo;
import com.blackstrawai.onboarding.user.InviteUserVo;
import com.blackstrawai.onboarding.user.ListSentInvitesVo;
import com.blackstrawai.onboarding.user.ManageInvitesVo;
import com.blackstrawai.onboarding.user.RoleUserVo;
import com.blackstrawai.onboarding.user.UserVo;
import com.blackstrawai.onboarding.user.WithdrawInviteVo;


@Service
public class UserService extends BaseService{

	@Autowired
	UserDao userDao;

	@Autowired
	LoginDao loginDao;

	@Autowired
	DropDownDao dropDownDao;

	@Autowired
	RegistrationDao registrationDao;

	@Autowired
	OrganizationDao organizationDao;

	@Autowired
	RoleDao roleDao;

	private  Logger logger = Logger.getLogger(UserService.class);


	public UserVo createUser(UserVo userVo)throws ApplicationException {	
		logger.info("Entry into method:createUser");
		userVo=userDao.createUser(userVo);
		Random rand = new Random(); 
		int value=rand.nextInt(1000);
		long time=System.currentTimeMillis();
		String token=generateHashToken("generateHashToken"+value+time);
		loginDao.updateUserToken(userVo.getId(),token);
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
		RegistrationVo registrationVo=registrationDao.getRegistrationByEmail(userVo.getEmailId());
		String message=null;
		if(registrationVo==null){
			message="Hi " +userVo.getFirstName() + " " +userVo.getLastName() + ",\r\n\r\n  Your Profile is Successfully Created. Please Click on "+url+"/auth/create-password/"+token+  " to Create New Password.\r\n\r\n Regards,\r\n Decifer Admin";
		}else{
			String organizationName = organizationDao.getOrganizationName(userVo.getOrganizationId());
			RoleVo roleVo=roleDao.getRoleDetails(userVo.getOrganizationId(), userVo.getRoleId());
			message = "Hi " + userVo.getFirstName() + " " +userVo.getLastName()
			+ ",\r\n\r\n  You are invited to Join Organization-" + organizationName + " as " + roleVo.getName()+".Click on "
			+ url + " to proceed.\r\n\r\n Regards,\r\n Decifer Admin";
		}
		EmailThread emailThread=new EmailThread(message,userVo.getEmailId(),"Decifer:User Registration");
		emailThread.start();
		return userVo;


	}

	public UserVo updateUser(UserVo userVo)throws ApplicationException {	
		logger.info("Entry into method:updateUser");
		return userDao.updateUser(userVo);
	}

	public UserVo deleteUser(int id,String status,String userId,String roleName)throws ApplicationException {	
		logger.info("Entry into method:deleteUser");
		return userDao.deleteUser(id,status,userId,roleName);
	}

	public List<UserVo> getAllUsersOfAnOrganizationUserAndRole(int organizationId,String organizationName,String userId,String roleName)throws ApplicationException {	
		logger.info("Entry into method:getAllUsersOfAnOrganization");
		return userDao.getAllUsersOfAnOrganizationUserAndRole(organizationId,organizationName,userId,roleName);
	}

	public UserVo getUserDetails(int organizationId, int userId)throws ApplicationException {	
		logger.info("Entry into method:getUserDetails");
		return userDao.getUserDetails(organizationId,userId);
	}

	public UserDropDownVo getUserDropDownData()throws ApplicationException {
		logger.info("Entry into getUserDropDownData");
		return dropDownDao.getUserDropDownData();
	}

	public InviteUserVo createUserInvite(InviteUserVo inviteUserVo) throws ApplicationException {
		logger.info("Entry into createUserInvite");
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
		InviteUserVo data=userDao.createUserInvite(inviteUserVo);
		String organizationName = organizationDao.getOrganizationName(inviteUserVo.getOrgId());
		for(int i=0;i<inviteUserVo.getRoleUser().size();i++){
			RoleUserVo roleUserVo=inviteUserVo.getRoleUser().get(i);			
			RoleVo roleVo=roleDao.getRoleDetails(inviteUserVo.getOrgId(),roleUserVo.getRoleId());
			for (String email : roleUserVo.getEmailIdList()) {
				String message = "Hi User"+ ",\r\n\r\n  You are invited to Join Organization-" + organizationName + " as " + roleVo.getName()+".Click on "
						+ url + " to proceed.\r\n\r\n Regards,\r\n Decifer Admin";						
				EmailThread emailThread=new EmailThread(message,email,roleUserVo.getMessage());
				emailThread.start();
			}

		}

		return data;
	}

	public List<ListSentInvitesVo> getAllUserInvitesOfAnOrganizationUserAndRole(int organizationId, String organizationName,
			String userId, String roleName) throws ApplicationException {
		logger.info("Entry into getAllUserInvitesOfAnOrganizationUserAndRole");
		return userDao.getAllUserInvitesOfAnOrganizationUserAndRole(organizationId, userId, roleName);
	}

	public WithdrawInviteVo withdrawInvite(int id, String userId, String roleName)throws ApplicationException {
		logger.info("Entry into withdrawInvite");
		WithdrawInviteVo data=userDao.withdrawInvite(id, userId, roleName);
		UserVo userVo=userDao.getUserInviteDetails(id);	
		String organizationName = organizationDao.getOrganizationName(userVo.getOrganizationId());
		String message = "Hi User"+ ",\r\n\r\n  Your request to Join Organization-" + organizationName + " is Withdrawn in Decifer.\r\n\r\n Regards,\r\n Decifer Admin";						
		EmailThread emailThread=new EmailThread(message,userVo.getEmailId(),"Invite Withdrawn");
		emailThread.start();
		return data;
	}

	public List<ManageInvitesVo> getManageInvitesDetails(String emailId) throws ApplicationException {
		logger.info("Entry into getManageInvitesDetails");
		return userDao.getManageInvitesDetails(emailId);
	}

	public InviteActionVo takeInviteAction(InviteActionVo inviteActionVo) throws ApplicationException {
		logger.info("Entry into takeInviteAction");
		return userDao.takeInviteAction(inviteActionVo);
	}




}

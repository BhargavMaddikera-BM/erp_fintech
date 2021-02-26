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
import com.blackstrawai.common.CacheService;
import com.blackstrawai.onboarding.loginandregistration.RegistrationVo;

@Service
public class RegistrationService extends BaseService{
	
	@Autowired
	RegistrationDao registrationDao;
	
	@Autowired
	CacheService cacheService;
	
	private  Logger logger = Logger.getLogger(RegistrationService.class);
	
	public RegistrationVo register(RegistrationVo registrationVo)throws ApplicationException {
		logger.info("Entry into method:register");
		RegistrationVo regVo;
		try{
		//	String hashedPassword=generateHashToken(registrationVo.getPassword());
		//	registrationVo.setPassword(hashedPassword);
			
			Random rand = new Random(); 
			int value=rand.nextInt(1000);
			long time=System.currentTimeMillis();
			registrationVo.setUserToken(generateHashToken("generateHashToken"+value+time));						
			regVo=registrationDao.register(registrationVo);
			regVo.setPassword(null);
			String keyToken=null;
			String valueToken=null;			
			synchronized (registrationVo) {
				keyToken=new Long(System.currentTimeMillis()).toString();
				valueToken=generateHashToken(keyToken.concat(registrationVo.getEmailId()));
				registrationVo.setKeyToken(keyToken);
				registrationVo.setValueToken(valueToken);
				cacheService.addToken(keyToken, valueToken);
				
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
				String message="Hi " +registrationVo.getFirstName() + " " +registrationVo.getLastName() + ",\r\n\r\n  Your Profile is Registered Successfully. Please Click on "+url+"/auth/create-password/"+registrationVo.getUserToken()+  " to Create New Password.\r\n\r\n Regards,\r\n Decifer Admin";
				EmailThread emailThread=new EmailThread(message,registrationVo.getEmailId(),"Registration Successful");
				emailThread.start();				
			}
		}catch(Exception e){
			throw e;
		}
		
		return regVo;
	}
	
	
	public List<RegistrationVo> getAllRegistrations()throws ApplicationException {	
		logger.info("Entry into method:getAllRegistrations");
		return registrationDao.getAllRegistrations();
	}
	
	public RegistrationVo getRegistrationByEmail(String email)throws ApplicationException {	
		logger.info("Entry into method:getRegistrationByEmail");
		return registrationDao.getRegistrationByEmail(email);
	}

}

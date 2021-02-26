package com.blackstrawai.externalintegration.banking.perfios;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.common.BaseService;
import com.blackstrawai.externalintegration.banking.common.BasicAccountDetailsVo;
import com.blackstrawai.externalintegration.yesbank.statement.BankStatementDetailsResponseVo;
import com.blackstrawai.onboarding.EmailThread;
import com.blackstrawai.perfios.Perfios;
import com.blackstrawai.report.ReportPeriodDatesVo;

@Service
public class PerfiosService extends BaseService {
	@Autowired
	PerfiosDao perfiosDao;
	

	private Logger logger = Logger.getLogger(PerfiosService.class);

	public PerfiosWidgetUrlVo getWidgetUrl(int organizationId, int userId, String roleName,int instId) throws ApplicationException {
		logger.info("Entry into method: getWidgetUrl");	
		Perfios perfios = Perfios.getInstance();
		String hashValue;
		String partnerAuth = perfios.partnerLogin();
		if (perfiosDao.isUserExist(userId, organizationId, roleName)) {
			hashValue = perfiosDao.getPerfiosId(userId, organizationId, roleName);
		} else {
			int id = perfiosDao.createUser(userId, organizationId, roleName);
			hashValue="Id"+id+"OrgId"+organizationId+"UserId"+userId+"RoleName"+roleName/*.replace(" ", "")*/;
			perfiosDao.updateUser(id, hashValue);
			perfios.userRegistration(partnerAuth, hashValue);			
		}
		PerfiosWidgetUrlVo perfiosVo = new PerfiosWidgetUrlVo();
		String autoUpdateWidgetUrl = null;
		String userAuth = perfios.userLogin(partnerAuth, hashValue);
		if (userAuth != null) {
			autoUpdateWidgetUrl = perfios.getAutoUpdateWidgetUrl(partnerAuth, userAuth,instId);
		}
		perfiosVo.setAuthToken(partnerAuth);
		perfiosVo.setUserAuth(userAuth);
		perfiosVo.setAutoUpdateWidgetUrl(autoUpdateWidgetUrl);
		perfiosVo.setUniqueUserId(hashValue);
		return perfiosVo;
	}

	public void sendEmail(String accountId, String userId,String uniqueUserId)throws ApplicationException{

		String message=accountId+ "-" +userId + "-" +uniqueUserId;
		EmailThread emailThread=new EmailThread(message,"bhargav.maddikera@blackstraw.ai","Test Callback");
		emailThread.start();
	}

	public List<PerfiosUserAccountTransactionVo> getUserAccountTransaction(String accountId, String uniqueUserId) throws ApplicationException {
		List<PerfiosUserAccountTransactionVo> userTransactionVo = new ArrayList<PerfiosUserAccountTransactionVo>();
		Perfios perfios = Perfios.getInstance();
		String auth = perfios.partnerLogin();
		String userAuth = perfios.userLogin(auth, uniqueUserId);
		if (auth != null && userAuth != null)
			userTransactionVo = perfios.getUserAccountTransaction(accountId, auth, userAuth);
		return userTransactionVo;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void refresh( int organizationId, int userId, String roleName,String accountName) throws ApplicationException {
		Perfios perfios = Perfios.getInstance();
		String auth = perfios.partnerLogin();
		String uniqueUserId=perfiosDao.getPerfiosId(userId,organizationId,roleName);
		String userAuth = perfios.userLogin(auth, uniqueUserId);
		List<String>  data=perfiosDao.getListOfAccountId(uniqueUserId,accountName);
		for(int i=0;i<data.size();i++){
			String accountId=data.get(i);		
			if (auth != null && userAuth != null){
				List userTransactionVo=perfios.getUserAccountTransaction(accountId, auth, userAuth);
				if(userTransactionVo!=null){
					perfiosDao.createOrUpdateUserAccountTransaction(userTransactionVo,uniqueUserId);
					userTransactionVo.clear();
				}
			}
		}		
	}

	public void createUserAccountTransaction(List<PerfiosUserAccountTransactionVo> userTransactionVo,
			String uniqueUserId) throws Exception {
		logger.info("Entry into method: createUserAccountTransaction");	
		perfiosDao.createOrUpdateUserAccountTransaction(userTransactionVo, uniqueUserId);

	}

	public List<PerfiosStatementInstitutionVo> getStatementInstitution() throws ApplicationException {
		Perfios perfios = Perfios.getInstance();
		String auth = perfios.partnerLogin();
		String hashValue = new Long(System.currentTimeMillis()).toString();
		perfios.userRegistration(auth, hashValue);
		String userAuth = perfios.userLogin(auth, hashValue);
		List<PerfiosStatementInstitutionVo> statements = perfios.getStatementInstitution(auth, userAuth);
		perfios.unRegisterUser(hashValue, auth, userAuth);
		//List<PerfiosStatementInstitutionVo> statementsData=new ArrayList<PerfiosStatementInstitutionVo>();
		/*for(int i=0;i<statements.size();i++){
			PerfiosStatementInstitutionVo data=statements.get(i);
			if(data.getName().contains("Yes Bank")){
				logger.info("Yes Bank Data");	
			}else{
				statementsData.add(data);
			}
		}
		statements.clear();
		 */return statements;
	}
	public List<BasicAccountDetailsVo> getAllAccountNamesForUserRole(int orgId,int userId,String roleName) throws ApplicationException {
		return perfiosDao.getAllAccountNamesForUserRole(orgId, userId, roleName);
	}

	public List<RecentTransactionVo> getAccountRecentTransactions(String accountName,int orgId,String userId,String roleName) throws ApplicationException {
		return perfiosDao.getAccountRecentTransactions(accountName,orgId,userId,roleName);
	}
	public String getAvailableBalanceForAccount(int organizationId,String userId, String roleName,String accountName) throws ApplicationException {
		return perfiosDao.getAvailableBalanceForAccount(organizationId,userId,roleName,accountName);
	}

	public BankStatementDetailsResponseVo getStatementForAccount(int organizationId,String userId, String roleName,String accountName,String searchParam) throws ApplicationException {
		return perfiosDao.getStatementForAccount(organizationId, userId, roleName, accountName, searchParam);
	}
	public BankStatementDetailsResponseVo getStatementForAccountWithFilter(int organizationId,String userId, String roleName,String accountName,ReportPeriodDatesVo vo,boolean isAll,boolean isCr,boolean isDr) throws ApplicationException {
		return  perfiosDao.getStatementForAccountWithFilter(organizationId,userId,roleName,accountName,vo,isAll,isCr,isDr);
	}
	
	
	/*public void deleteUser(String uniqueUserId) throws ApplicationException {
		logger.info("Entry into method: deleteUser");	
		Perfios perfios = Perfios.getInstance();
		perfiosDao.deleteUser(uniqueUserId);
		String partnerAuth = perfios.partnerLogin();
		String userAuth = perfios.userLogin(partnerAuth, uniqueUserId);
		perfios.unRegisterUser(uniqueUserId, partnerAuth, userAuth);
	}*/

	/*	public List<PerfiosMetadataVo> getMetadata() throws ApplicationException {
		logger.info("Entry into method: getMetadata");	
		Perfios perfios = Perfios.getInstance();
		String partnerAuth = perfios.partnerLogin();
		return perfios.getMetadata(partnerAuth);
	}
	 */
	/*	public boolean isUserIdInstIdExist(String uniqueUserId,
			Long instId) throws ApplicationException {
		logger.info("Entry into method: isUserIdInstIdExist");
		return perfiosDao.isUserIdInstIdExist(uniqueUserId, instId);
	}
	 */
	/*public String getAccountIdFromUserTransactions(String uniqueUserId, Long instId) throws ApplicationException {
		logger.info("Entry into method: getAccountIdFromUserTransactions");
		return perfiosDao.getAccountIdFromUserTransactions(uniqueUserId, instId);
	}*/

	/*public void deleteUserAccountTransaction(String uniqueUserId, Long instId) throws ApplicationException {
		logger.info("Entry into method: deleteUserAccountTransaction");
		perfiosDao.deleteUserAccountTransaction(uniqueUserId, instId);
	}
	 */
}

package com.blackstrawai.externalintegration.compliance;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.DeciferAES256;
import com.blackstrawai.attachments.AttachmentsConstants;
import com.blackstrawai.common.BaseService;
import com.blackstrawai.employeeProvidentFunds.EmployeeProvidentFunds;
import com.blackstrawai.externalintegration.compliance.employeeprovidentfund.EmployeeProvidentFundAttachmentVo;
import com.blackstrawai.externalintegration.compliance.employeeprovidentfund.EmployeeProvidentFundChallanVo;
import com.blackstrawai.externalintegration.compliance.employeeprovidentfund.EmployeeProvidentFundEcrVo;
import com.blackstrawai.externalintegration.compliance.employeeprovidentfund.EmployeeProvidentFundLoginVo;

@Service
public class EmployeeProvidentFundService extends BaseService {

	@Autowired
	EmployeeProvidentFundDao employeeProvidentFundDao;

	public EmployeeProvidentFundLoginVo createEpfUser(EmployeeProvidentFundLoginVo epfUser)
			throws ApplicationException, SQLException {
		epfUser.setLoginPassword(DeciferAES256.encrypt_compliance(epfUser.getLoginPassword(),"employee_provident_fund_key"));
		return employeeProvidentFundDao.loginEpfUser(epfUser);
	}

	public List<EmployeeProvidentFundChallanVo> getRecentChallans(int userId)
			throws ApplicationException, SQLException, NumberFormatException, IOException {
		List<EmployeeProvidentFundChallanVo> challans = new ArrayList<EmployeeProvidentFundChallanVo>();
		challans = employeeProvidentFundDao.getRecentChallans(userId);
		Map<String, String> userMap = employeeProvidentFundDao.getLoginUsernamePassword(userId);
		if (challans.size() == 0) {
			challans = EmployeeProvidentFunds.getInstance().getChallans(
					userMap.get(EmployeeProvidentFundConstants.USERNAME_KEY),
					DeciferAES256.decrypt_compliance(userMap.get(EmployeeProvidentFundConstants.PASSWORD_KEY),"employee_provident_fund_key"),
					Integer.valueOf(userMap.get(EmployeeProvidentFundConstants.ORG_KEY)));
			employeeProvidentFundDao.createRecentChallans(challans, userId);
		} else {
			String downloadFilePath = EmployeeProvidentFunds.constructDownloadPath(
					Integer.valueOf(userMap.get(EmployeeProvidentFundConstants.ORG_KEY)),
					AttachmentsConstants.MODULE_EMPLOYEE_PROVIDENT_FUND_CHALLAN,
					userMap.get(EmployeeProvidentFundConstants.USERNAME_KEY),
					EmployeeProvidentFunds.getAttachmentsPath()) + "/";
			for (EmployeeProvidentFundChallanVo challan : challans) {
				for (EmployeeProvidentFundAttachmentVo attachment : challan.getAttachments()) {
					EmployeeProvidentFunds.setAttachmentData(attachment,
							new File(downloadFilePath + attachment.getName()));
				}
			}
		}
		return challans;
	}

	public List<EmployeeProvidentFundEcrVo> getRecentEcrs(int userId)
			throws ApplicationException, SQLException, IOException {
		List<EmployeeProvidentFundEcrVo> ecrs = new ArrayList<EmployeeProvidentFundEcrVo>();
		ecrs = employeeProvidentFundDao.getRecentEcrs(userId);
		Map<String, String> userMap = employeeProvidentFundDao.getLoginUsernamePassword(userId);
		if (ecrs.size() == 0) {
			ecrs = EmployeeProvidentFunds.getInstance().getECRs(
					userMap.get(EmployeeProvidentFundConstants.USERNAME_KEY),
					DeciferAES256.decrypt_compliance(userMap.get(EmployeeProvidentFundConstants.PASSWORD_KEY),"employee_provident_fund_key"),
					Integer.valueOf(userMap.get(EmployeeProvidentFundConstants.ORG_KEY)));
			employeeProvidentFundDao.createRecentEcrs(ecrs, userId);
		} else {
			String downloadFilePath = EmployeeProvidentFunds.getAttachmentsPath() + "/"
					+ userMap.get(EmployeeProvidentFundConstants.ORG_KEY) + "/"
					+ AttachmentsConstants.MODULE_EMPLOYEE_PROVIDENT_FUND + "/"
					+ AttachmentsConstants.MODULE_EMPLOYEE_PROVIDENT_FUND_ECR + "/"
					+ userMap.get(EmployeeProvidentFundConstants.USERNAME_KEY) + "/";
			for (EmployeeProvidentFundEcrVo ecr : ecrs) {
				for (EmployeeProvidentFundAttachmentVo attachment : ecr.getAttachments()) {
					EmployeeProvidentFunds.setAttachmentData(attachment,
							new File(downloadFilePath + attachment.getName()));
				}
			}
		}
		return ecrs;
	}

	public List<EmployeeProvidentFundChallanVo> refreshChallans(int userId)
			throws ApplicationException, SQLException, NumberFormatException, IOException {
		List<EmployeeProvidentFundChallanVo> challans = new ArrayList<EmployeeProvidentFundChallanVo>();
		Map<String, String> userMap = employeeProvidentFundDao.getLoginUsernamePassword(userId);
		challans = EmployeeProvidentFunds.getInstance().getChallans(
				userMap.get(EmployeeProvidentFundConstants.USERNAME_KEY),
				DeciferAES256.decrypt_compliance(userMap.get(EmployeeProvidentFundConstants.PASSWORD_KEY),"employee_provident_fund_key"),
				Integer.valueOf(userMap.get(EmployeeProvidentFundConstants.ORG_KEY)));
		if (challans.size() > 0) {
			employeeProvidentFundDao.createOrUpdateChallan(challans, userId);
		}
		return challans;
	}

	public List<EmployeeProvidentFundEcrVo> refreshEcr(int userId)
			throws ApplicationException, SQLException, IOException {
		List<EmployeeProvidentFundEcrVo> ecrs = new ArrayList<EmployeeProvidentFundEcrVo>();
		Map<String, String> userMap = employeeProvidentFundDao.getLoginUsernamePassword(userId);
		ecrs = EmployeeProvidentFunds.getInstance().getECRs(userMap.get(EmployeeProvidentFundConstants.USERNAME_KEY),
				DeciferAES256.decrypt_compliance(userMap.get(EmployeeProvidentFundConstants.PASSWORD_KEY),"employee_provident_fund_key"),
				Integer.valueOf(userMap.get(EmployeeProvidentFundConstants.ORG_KEY)));
		if (ecrs.size() > 0) {
			employeeProvidentFundDao.createOrUpdateEcr(ecrs, userId);
		}
		return ecrs;
	}

	/*
	 * public EmployeeProvidentFundLoginVo getLoginDetailsForAnOrganization(int
	 * organizationId,String login)throws ApplicationException { return
	 * employeeProvidentFundDao.getLoginDetailsForAnOrganization(organizationId,
	 * login); }
	 */

	public void disconnectUser(int userId, String status) throws ApplicationException {
		employeeProvidentFundDao.disconnectUser(userId, status);
	}

}

package com.blackstrawai.onboarding;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.accessandroles.AccessAndRolesDao;
import com.blackstrawai.ap.dropdowns.BasicVoucherEntriesVo;
import com.blackstrawai.attachments.AttachmentsConstants;
import com.blackstrawai.attachments.AttachmentsDao;
import com.blackstrawai.attachments.AttachmentsVo;
import com.blackstrawai.attachments.UploadFileVo;
import com.blackstrawai.chartofaccounts.ChartOfAccountsConstants;
import com.blackstrawai.chartofaccounts.ChartOfAccountsDao;
import com.blackstrawai.common.BaseDao;
import com.blackstrawai.common.CommonConstants;
import com.blackstrawai.common.FinanceCommonConstants;
import com.blackstrawai.common.FinanceCommonDao;
import com.blackstrawai.keycontact.VendorDao;
import com.blackstrawai.keycontact.vendor.VendorVo;
import com.blackstrawai.onboarding.organization.BasicGSTLocationDetailsVo;
import com.blackstrawai.onboarding.organization.BasicLocationDetailsVo;
import com.blackstrawai.onboarding.organization.BasicOrganizationVo;
import com.blackstrawai.onboarding.organization.GSTDetailsVo;
import com.blackstrawai.onboarding.organization.IncomeTaxLoginVo;
import com.blackstrawai.onboarding.organization.KeyMembersVo;
import com.blackstrawai.onboarding.organization.LocationVo;
import com.blackstrawai.onboarding.organization.MinimalOrganizationVo;
import com.blackstrawai.onboarding.organization.NewOrganizationVo;
import com.blackstrawai.onboarding.organization.OrganizationAddressVo;
import com.blackstrawai.onboarding.organization.OrganizationDropDownVo;
import com.blackstrawai.onboarding.organization.OrganizationGeneralInfoVo;
import com.blackstrawai.onboarding.organization.OrganizationLimitedLiabilityOtherDetailsVo;
import com.blackstrawai.onboarding.organization.OrganizationLimitedLiabilityPartnerVo;
import com.blackstrawai.onboarding.organization.OrganizationOnePersonDirectorVo;
import com.blackstrawai.onboarding.organization.OrganizationOnePersonOtherDetailsVo;
import com.blackstrawai.onboarding.organization.OrganizationPartnershipOtherDetailsVo;
import com.blackstrawai.onboarding.organization.OrganizationPartnershipPartnerVo;
import com.blackstrawai.onboarding.organization.OrganizationPrivateLimitedDirectorVo;
import com.blackstrawai.onboarding.organization.OrganizationPrivateLimitedOtherDetailsVo;
import com.blackstrawai.onboarding.organization.OrganizationProprietorOtherDetailsVo;
import com.blackstrawai.onboarding.organization.OrganizationProprietorVo;
import com.blackstrawai.onboarding.organization.OrganizationPublicLimitedDirectorVo;
import com.blackstrawai.onboarding.organization.OrganizationPublicLimitedOtherDetailsVo;
import com.blackstrawai.onboarding.organization.UserTypeOrganizationVo;
import com.blackstrawai.onboarding.role.RoleVo;
import com.blackstrawai.payroll.PayItemDao;
import com.blackstrawai.payroll.PayItemVo;
import com.blackstrawai.payroll.PayTypeDao;
import com.blackstrawai.payroll.PayTypeVo;
import com.blackstrawai.settings.BaseCurrencyVo;
import com.blackstrawai.settings.CurrencyDao;
import com.blackstrawai.settings.PaymentTermsDao;
import com.blackstrawai.settings.PaymentTermsVo;
import com.blackstrawai.settings.SettingsAndPreferencesConstants;
import com.blackstrawai.settings.TaxDao;
import com.blackstrawai.settings.TaxGroupVo;
import com.blackstrawai.settings.TaxRateMappingVo;
import com.blackstrawai.settings.TaxRateTypeVo;
import com.blackstrawai.upload.ModuleTypeVo;
import com.blackstrawai.vendorsettings.BaseGeneralSettingsVo;
import com.blackstrawai.vendorsettings.VendorSettingsConstants;
import com.blackstrawai.workflow.WorkflowGeneralSettingsDao;
import com.blackstrawai.workflow.WorkflowGeneralSettingsVo;
import com.blackstrawai.workflow.WorkflowSettingsCommonVo;
import com.blackstrawai.workflow.WorkflowSettingsDao;
import com.blackstrawai.workflow.WorkflowSettingsRuleData;
import com.blackstrawai.workflow.WorkflowSettingsVo;

@Repository
public class OrganizationDao extends BaseDao {

	@Autowired
	OrganizationTypeDao organizationTypeDao;

	@Autowired
	OrganizationIndustryDao organizationIndustryDao;

	@Autowired
	OrganizationConstitutionDao organizationConstitutionDao;

	@Autowired
	CurrencyDao currencyDao;

	@Autowired
	FinanceCommonDao financeDao;

	@Autowired
	PaymentTermsDao paymentTermsDao;

	@Autowired
	TaxDao taxDao;

	@Autowired
	UserDao userDao;

	@Autowired
	RoleDao roleDao;

	@Autowired
	AttachmentsDao attachmentsDao;

	@Autowired
	ChartOfAccountsDao chartOfAccountsDao;

	@Autowired
	PayTypeDao payTypeDao;

	@Autowired
	PayItemDao payItemDao;

	@Autowired
	private VendorDao vendorDao;

	@Autowired
	private AccessAndRolesDao accessAndRolesDao;

	@Autowired
	private  WorkflowSettingsDao workflowSettingsDao;

	@Autowired
	private WorkflowGeneralSettingsDao workflowGeneralSettingsDao;

	private Logger logger = Logger.getLogger(OrganizationDao.class);

	public BasicOrganizationVo createOrganization(NewOrganizationVo organizationVo) throws ApplicationException {
		logger.info("Entry into method:createOrganization");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		con = getUserMgmConnection();

		try {
			con.setAutoCommit(false);
			/*boolean isOrganizationExist = checkOrganizationExist(organizationVo.getGeneralInfo().getName(), con);
			if (isOrganizationExist) {
				throw new Exception("Organization already exist");
			}*/

			String sql = OrganizationConstants.INSERT_INTO_NEW_ORGANIZATION;

			preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setString(1, organizationVo.getGeneralInfo().getName());
			preparedStatement.setInt(2, organizationVo.getGeneralInfo().getConstitutionId());
			preparedStatement.setString(3, organizationVo.getGeneralInfo().getCinno());
			preparedStatement.setInt(4, organizationVo.getGeneralInfo().getOrganizationTypeId());
			preparedStatement.setInt(5, organizationVo.getGeneralInfo().getIndustryOfOrganization());
			preparedStatement.setInt(6, organizationVo.getGeneralInfo().getBaseCurrency());
			preparedStatement.setString(7, organizationVo.getGeneralInfo().getFinancialYear());
			preparedStatement.setString(8, "ACT");
			preparedStatement.setInt(9, Integer.parseInt(organizationVo.getUserId()));
			preparedStatement.setTimestamp(10, new Timestamp(System.currentTimeMillis()));
			preparedStatement.setString(11, organizationVo.getGeneralInfo().getDateFormat());
			preparedStatement.setString(12, organizationVo.getGeneralInfo().getTimeZone());
			preparedStatement.setString(13, organizationVo.getGeneralInfo().getContactNumber());
			preparedStatement.setString(14, organizationVo.getGeneralInfo().getEmailId());
			preparedStatement.setString(15, organizationVo.getGeneralInfo().getIeCodeNo());
			preparedStatement.setBoolean(16, organizationVo.getGeneralInfo().getIsCash());
			preparedStatement.setString(17, organizationVo.getRocData());
			preparedStatement.setBoolean(18, organizationVo.isSuperAdmin());

			int rowAffected = preparedStatement.executeUpdate();
			if (rowAffected == 1) {
				rs = preparedStatement.getGeneratedKeys();
				if (rs.next()) {
					organizationVo.setId(rs.getInt(1));
					createGSTDetails(organizationVo.getId(), organizationVo, con);
					createLocation(organizationVo.getId(), organizationVo, con);
					if (organizationVo.getKeyMembers().getOrganizationProprietor() != null) {
						createOrganizationProprietor(organizationVo.getId(), organizationVo, con);
					} else if (organizationVo.getKeyMembers().getOrganizationPartnershipPartner() != null) {
						createOrganizationPartnershipPartner(organizationVo.getId(), organizationVo, con);
					} else if (organizationVo.getKeyMembers().getOrganizationPrivateLimitedDirector() != null) {
						createOrganizationPrivateLimitedDirector(organizationVo.getId(), organizationVo, con);
					} else if (organizationVo.getKeyMembers().getOrganizationOnePersonDirector() != null) {
						createOrganizationOnePersonDirector(organizationVo.getId(), organizationVo, con);
					} else if (organizationVo.getKeyMembers().getOrganizationLimitedLiabilityPartner() != null) {
						createOrganizationLimitedLiabilityPartner(organizationVo.getId(), organizationVo, con);
					} else if (organizationVo.getKeyMembers().getOrganizationPublicLimitedDirector() != null) {
						createOrganizationPublicLimitedDirector(organizationVo.getId(), organizationVo, con);
					}

				}
				attachmentsDao.createAttachments(organizationVo.getId(),organizationVo.getUserId(),organizationVo.getAttachments(),
						AttachmentsConstants.MODULE_TYPE_ORG_DOCUMENTS, organizationVo.getId(),"Super Admin");
			}
			con.commit();
		} catch (Exception e) {
			try {
				con.rollback();
			} catch (Exception e1) {
				throw new ApplicationException(e);
			}
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);

		}
		BasicOrganizationVo basicOrganizationVo = new BasicOrganizationVo();
		basicOrganizationVo.setId(organizationVo.getId());
		basicOrganizationVo.setName(organizationVo.getGeneralInfo().getName());

		return basicOrganizationVo;
	}


	public BasicOrganizationVo createMinimalOrganization(MinimalOrganizationVo organizationVo,Map<String,String>otherGstData) throws ApplicationException {
		logger.info("Entry into method:createMinimalOrganization");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		con = getUserMgmConnection();

		try {
			con.setAutoCommit(false);
			/*boolean isOrganizationExist = checkOrganizationExist(organizationVo.getLegalName(), con);
			if (isOrganizationExist) {
				throw new Exception("Organization already exist");
			}
			 */

			String sql = OrganizationConstants.INSERT_INTO_MINIMAL_ORGANIZATION;

			int baseCurrencyId=8;

			List<BaseCurrencyVo> baseCurrencyList=financeDao.getBasicBaseCurrency();
			for(int i=0;i<baseCurrencyList.size();i++){
				BaseCurrencyVo currencyData=baseCurrencyList.get(i);
				if(currencyData.getName().equals("Indian Rupee")){
					baseCurrencyId=currencyData.getId();
					break;
				}
			}

			preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setString(1, organizationVo.getLegalName());
			preparedStatement.setString(2, organizationVo.getDisplayName());
			preparedStatement.setInt(3, organizationVo.getConstitutionId());
			preparedStatement.setInt(4, organizationVo.getOrganizationTypeId());
			preparedStatement.setInt(5, organizationVo.getIndustryOfOrganization());
			preparedStatement.setInt(6, baseCurrencyId);
			preparedStatement.setString(7, organizationVo.getFinancialYear());
			preparedStatement.setString(8, "ACT");
			preparedStatement.setInt(9, Integer.parseInt(organizationVo.getUserId()));
			preparedStatement.setTimestamp(10, new Timestamp(System.currentTimeMillis()));
			preparedStatement.setString(11, organizationVo.getDateFormat());
			preparedStatement.setString(12, organizationVo.getTimeZone());
			preparedStatement.setBoolean(13, true);
			preparedStatement.setString(14, organizationVo.getUserProfile());
			preparedStatement.setString(15, organizationVo.getBrandName());
			preparedStatement.setBoolean(16, true);
			preparedStatement.setString(17, organizationVo.getPan());
			int rowAffected = preparedStatement.executeUpdate();
			if (rowAffected == 1) {
				rs = preparedStatement.getGeneratedKeys();
				if (rs.next()) {
					organizationVo.setId(rs.getInt(1));
					createGSTDetailsForMinimalOrganization(organizationVo.getId(), organizationVo, con,otherGstData);
					/*	
					createLocation(organizationVo.getId(), organizationVo, con);
					if (organizationVo.getKeyMembers().getOrganizationProprietor() != null) {
						createOrganizationProprietor(organizationVo.getId(), organizationVo, con);
					} else if (organizationVo.getKeyMembers().getOrganizationPartnershipPartner() != null) {
						createOrganizationPartnershipPartner(organizationVo.getId(), organizationVo, con);
					} else if (organizationVo.getKeyMembers().getOrganizationPrivateLimitedDirector() != null) {
						createOrganizationPrivateLimitedDirector(organizationVo.getId(), organizationVo, con);
					} else if (organizationVo.getKeyMembers().getOrganizationOnePersonDirector() != null) {
						createOrganizationOnePersonDirector(organizationVo.getId(), organizationVo, con);
					} else if (organizationVo.getKeyMembers().getOrganizationLimitedLiabilityPartner() != null) {
						createOrganizationLimitedLiabilityPartner(organizationVo.getId(), organizationVo, con);
					} else if (organizationVo.getKeyMembers().getOrganizationPublicLimitedDirector() != null) {
						createOrganizationPublicLimitedDirector(organizationVo.getId(), organizationVo, con);
					}*/

				}
				/*attachmentsDao.createAttachments(organizationVo.getId(),organizationVo.getUserId(),organizationVo.getAttachments(),
						AttachmentsConstants.MODULE_TYPE_ORG_DOCUMENTS, organizationVo.getId(),"Super Admin");*/
			}
			con.commit();
		} catch (Exception e) {
			try {
				con.rollback();
			} catch (Exception e1) {
				throw new ApplicationException(e);
			}
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);

		}
		BasicOrganizationVo basicOrganizationVo = new BasicOrganizationVo();
		basicOrganizationVo.setId(organizationVo.getId());
		basicOrganizationVo.setName(organizationVo.getLegalName());

		return basicOrganizationVo;
	}

	/*private boolean checkOrganizationExist(String name, Connection con) throws ApplicationException {
		logger.info("Entry into method:checkOrganizationExist");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			String query = OrganizationConstants.CHECK_ORGANIZATON_EXIST;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setString(1, name);
			preparedStatement.setString(2, "DEL");
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
	}*/

	private NewOrganizationVo createGSTDetails(int orgId, NewOrganizationVo data, Connection con)
			throws ApplicationException {
		logger.info("Entry into method:createGSTDetails");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {

			String sql = OrganizationConstants.INSERT_INTO_ORGANIZATION_GST_DETAILS;

			preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setInt(1, data.getGstDetails().getGstRegnTypeId());
			preparedStatement.setString(2, data.getGstDetails().getGstNo());
			preparedStatement.setString(3, data.getGstDetails().getTaxPanId());
			preparedStatement.setString(4, data.getGstDetails().getAddress_1());
			preparedStatement.setString(5, data.getGstDetails().getAddress_2());
			preparedStatement.setString(6, data.getGstDetails().getCity());
			preparedStatement.setString(7, new Integer(data.getGstDetails().getState()).toString());
			preparedStatement.setString(8, new Integer(data.getGstDetails().getCountry()).toString());
			preparedStatement.setString(9, data.getGstDetails().getPinCode());
			preparedStatement.setInt(10, orgId);
			preparedStatement.setBoolean(11, data.getGstDetails().getIsMultiGST());
			preparedStatement.setTimestamp(12, new Timestamp(System.currentTimeMillis()));
			int rowAffected = preparedStatement.executeUpdate();
			if (rowAffected == 1) {
				rs = preparedStatement.getGeneratedKeys();
				if (rs.next()) {
					data.getGstDetails().setId(rs.getInt(1));
				}
			}
		} catch (Exception e) {
			throw new ApplicationException(e);

		} finally {
			closeResources(rs, preparedStatement, null);
		}

		return data;
	}


	@SuppressWarnings("rawtypes")
	private MinimalOrganizationVo createGSTDetailsForMinimalOrganization(int orgId, MinimalOrganizationVo data, Connection con,Map<String,String>otherGstData)
			throws ApplicationException {
		logger.info("Entry into method:createGSTDetailsForMinimalOrganization");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {

			String sql = OrganizationConstants.INSERT_INTO_ORGANIZATION_GST_DETAILS_MINIMAL;

			Iterator it=data.getGst().entrySet().iterator();
			String gstNo="";
			String gstData="";
			while(it.hasNext()){
				Map.Entry entry=(Map.Entry)it.next();
				if(entry.getKey().equals("gstNo")){
					gstNo=(String) entry.getValue();
				}
				if(entry.getKey().equals("gstData")){
					gstData=(String) entry.getValue();
				}

			}
			boolean isMultiGST=false;
			if(data.getOtherGsts()!=null && data.getOtherGsts().size()>0){
				isMultiGST=true;
			}


			preparedStatement = con.prepareStatement(sql);
			preparedStatement.setInt(1, data.getGstRegnTypeId());
			preparedStatement.setString(2, gstNo);
			preparedStatement.setString(3, null);
			preparedStatement.setString(4, "addr1");
			preparedStatement.setString(5, "addr2");
			preparedStatement.setString(6, "Chennai");
			preparedStatement.setString(7, "1");
			preparedStatement.setString(8,"1");
			preparedStatement.setString(9, "600100");
			preparedStatement.setInt(10, orgId);
			preparedStatement.setBoolean(11, isMultiGST);
			preparedStatement.setTimestamp(12, new Timestamp(System.currentTimeMillis()));
			preparedStatement.setString(13, gstData);
			preparedStatement.executeUpdate();


			if(isMultiGST){
				sql = OrganizationConstants.INSERT_INTO_ORGANIZATION_GST_LOCATION_MNIMAL;
				Iterator it1=otherGstData.entrySet().iterator();
				int i=0;
				while(it1.hasNext()){
					++i;
					Map.Entry entry=(Map.Entry)it1.next();
					preparedStatement = con.prepareStatement(sql);
					preparedStatement.setString(1, "Location"+i);
					preparedStatement.setString(2, (String) entry.getKey());
					preparedStatement.setString(3, null);
					preparedStatement.setString(4,"addr1");
					preparedStatement.setString(5, "addr2");
					preparedStatement.setString(6, "Chennai");
					preparedStatement.setString(7, "1");
					preparedStatement.setString(8, "1");
					preparedStatement.setString(9,"600100");
					preparedStatement.setInt(10, orgId);
					preparedStatement.setTimestamp(11, new Timestamp(System.currentTimeMillis()));
					preparedStatement.setString(12, (String) entry.getValue());
					preparedStatement.executeUpdate();
				}
			}
		} catch (Exception e) {
			throw new ApplicationException(e);

		} finally {
			closeResources(rs, preparedStatement, null);
		}

		return data;
	}

	private NewOrganizationVo createLocation(int orgId, NewOrganizationVo data, Connection con)
			throws ApplicationException {
		logger.info("Entry into method:createLocation");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {

			String sql = OrganizationConstants.INSERT_INTO_ORGANIZATION_GST_LOCATION;
			if (data.getGstDetails().getLocation() != null) {
				for (int i = 0; i < data.getGstDetails().getLocation().size(); i++) {
					LocationVo locationVo = data.getGstDetails().getLocation().get(i);
					preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
					preparedStatement.setString(1, locationVo.getName());
					preparedStatement.setString(2, locationVo.getGstNo());
					preparedStatement.setString(3, locationVo.getTaxPanId());
					preparedStatement.setString(4, locationVo.getAddress_1());
					preparedStatement.setString(5, locationVo.getAddress_2());
					preparedStatement.setString(6, locationVo.getCity());
					preparedStatement.setString(7, new Integer(locationVo.getState()).toString());
					preparedStatement.setString(8, new Integer(locationVo.getCountry()).toString());
					preparedStatement.setString(9, locationVo.getPinCode());
					preparedStatement.setInt(10, orgId);
					preparedStatement.setTimestamp(11, new Timestamp(System.currentTimeMillis()));
					int rowAffected = preparedStatement.executeUpdate();
					if (rowAffected == 1) {
						rs = preparedStatement.getGeneratedKeys();
						if (rs.next()) {
							locationVo.setId(rs.getInt(1));
						}
					}
				}
			}
		} catch (Exception e) {
			throw new ApplicationException(e);

		} finally {
			closeResources(rs, preparedStatement, null);
		}

		return data;
	}

	private NewOrganizationVo createOrganizationProprietor(int orgId, NewOrganizationVo data, Connection con)
			throws ApplicationException {
		logger.info("Entry into method:createOrganizationProprietor");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			String sql = OrganizationConstants.INSERT_INTO_ORGANIZATION_PROPRIETOR;
			if (data.getKeyMembers().getOrganizationProprietor() != null) {
				for (int i = 0; i < data.getKeyMembers().getOrganizationProprietor().size(); i++) {
					OrganizationProprietorVo orgProprietorVo = data.getKeyMembers().getOrganizationProprietor().get(i);
					preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
					preparedStatement.setString(1, orgProprietorVo.getName());
					preparedStatement.setString(2, orgProprietorVo.getEmailId());
					preparedStatement.setString(3, orgProprietorVo.getMobileNo());
					preparedStatement.setInt(4, orgId);
					preparedStatement.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
					int rowAffected = preparedStatement.executeUpdate();
					if (rowAffected == 1) {
						rs = preparedStatement.getGeneratedKeys();
						if (rs.next()) {
							orgProprietorVo.setId(rs.getInt(1));
						}
					}
				}
			}

		} catch (Exception e) {
			throw new ApplicationException(e);

		} finally {
			closeResources(rs, preparedStatement, null);
		}

		return data;
	}

	private NewOrganizationVo createOrganizationPartnershipPartner(int orgId, NewOrganizationVo data, Connection con)
			throws ApplicationException {
		logger.info("Entry into method:createOrganizationPartnershipPartner");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			String sql = OrganizationConstants.INSERT_INTO_ORGANIZATION_PARTNERSHIP_PARTNER;
			if (data.getKeyMembers().getOrganizationPartnershipPartner() != null) {
				for (int i = 0; i < data.getKeyMembers().getOrganizationPartnershipPartner().size(); i++) {
					OrganizationPartnershipPartnerVo organizationPartnershipPartnerVo = data.getKeyMembers()
							.getOrganizationPartnershipPartner().get(i);
					preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
					preparedStatement.setString(1, organizationPartnershipPartnerVo.getName());
					preparedStatement.setString(2, organizationPartnershipPartnerVo.getEmailId());
					preparedStatement.setString(3, organizationPartnershipPartnerVo.getMobileNo());
					preparedStatement.setString(4, organizationPartnershipPartnerVo.getOwnershipPercentage());
					preparedStatement.setInt(5, orgId);
					preparedStatement.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
					int rowAffected = preparedStatement.executeUpdate();
					if (rowAffected == 1) {
						rs = preparedStatement.getGeneratedKeys();
						if (rs.next()) {
							organizationPartnershipPartnerVo.setId(rs.getInt(1));
						}
					}
				}
			}

		} catch (Exception e) {
			throw new ApplicationException(e);

		} finally {
			closeResources(rs, preparedStatement, null);
		}

		return data;
	}

	private NewOrganizationVo createOrganizationPrivateLimitedDirector(int orgId, NewOrganizationVo data,
			Connection con) throws ApplicationException {
		logger.info("Entry into method:createOrganizationPrivateLimitedDirector");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			String sql = OrganizationConstants.INSERT_INTO_ORGANIZATION_PRIVATE_LIMITED_DIRECTOR;
			if (data.getKeyMembers().getOrganizationPrivateLimitedDirector() != null) {
				for (int i = 0; i < data.getKeyMembers().getOrganizationPrivateLimitedDirector().size(); i++) {
					OrganizationPrivateLimitedDirectorVo organizationPrivateLimitedDirectorVo = data.getKeyMembers()
							.getOrganizationPrivateLimitedDirector().get(i);
					preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
					preparedStatement.setString(1, organizationPrivateLimitedDirectorVo.getName());
					preparedStatement.setString(2, organizationPrivateLimitedDirectorVo.getEmailId());
					preparedStatement.setString(3, organizationPrivateLimitedDirectorVo.getMobileNo());
					preparedStatement.setString(4, organizationPrivateLimitedDirectorVo.getOwnershipPercentage());
					preparedStatement.setString(5, organizationPrivateLimitedDirectorVo.getDin());
					preparedStatement.setInt(6, orgId);
					preparedStatement.setTimestamp(7, new Timestamp(System.currentTimeMillis()));
					int rowAffected = preparedStatement.executeUpdate();
					if (rowAffected == 1) {
						rs = preparedStatement.getGeneratedKeys();
						if (rs.next()) {
							organizationPrivateLimitedDirectorVo.setId(rs.getInt(1));
						}
					}
				}
			}

		} catch (Exception e) {
			throw new ApplicationException(e);

		} finally {
			closeResources(rs, preparedStatement, null);
		}

		return data;
	}

	private NewOrganizationVo createOrganizationOnePersonDirector(int orgId, NewOrganizationVo data, Connection con)
			throws ApplicationException {
		logger.info("Entry into method:createOrganizationOnePersonDirector");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			String sql = OrganizationConstants.INSERT_INTO_ORGANIZATION_ONE_PERSON_DIRECTOR;
			if (data.getKeyMembers().getOrganizationOnePersonDirector() != null) {
				for (int i = 0; i < data.getKeyMembers().getOrganizationOnePersonDirector().size(); i++) {
					OrganizationOnePersonDirectorVo organizationOnePersonDirectorVo = data.getKeyMembers()
							.getOrganizationOnePersonDirector().get(i);
					preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
					preparedStatement.setString(1, organizationOnePersonDirectorVo.getName());
					preparedStatement.setString(2, organizationOnePersonDirectorVo.getEmailId());
					preparedStatement.setString(3, organizationOnePersonDirectorVo.getMobileNo());
					preparedStatement.setString(4, organizationOnePersonDirectorVo.getDin());
					preparedStatement.setInt(5, orgId);
					preparedStatement.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
					int rowAffected = preparedStatement.executeUpdate();
					if (rowAffected == 1) {
						rs = preparedStatement.getGeneratedKeys();
						if (rs.next()) {
							organizationOnePersonDirectorVo.setId(rs.getInt(1));
						}
					}
				}
			}

		} catch (Exception e) {
			throw new ApplicationException(e);

		} finally {
			closeResources(rs, preparedStatement, null);
		}

		return data;
	}

	private NewOrganizationVo createOrganizationLimitedLiabilityPartner(int orgId, NewOrganizationVo data,
			Connection con) throws ApplicationException {
		logger.info("Entry into method:createOrganizationLimitedLiabilityPartner");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			String sql = OrganizationConstants.INSERT_INTO_ORGANIZATION_LIMITED_LIABILITY_PARTNER;
			if (data.getKeyMembers().getOrganizationLimitedLiabilityPartner() != null) {
				for (int i = 0; i < data.getKeyMembers().getOrganizationLimitedLiabilityPartner().size(); i++) {
					OrganizationLimitedLiabilityPartnerVo organizationLimitedLiabilityPartnerVo = data.getKeyMembers()
							.getOrganizationLimitedLiabilityPartner().get(i);
					preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
					preparedStatement.setString(1, organizationLimitedLiabilityPartnerVo.getName());
					preparedStatement.setString(2, organizationLimitedLiabilityPartnerVo.getEmailId());
					preparedStatement.setString(3, organizationLimitedLiabilityPartnerVo.getMobileNo());
					preparedStatement.setString(4, organizationLimitedLiabilityPartnerVo.getOwnershipPercentage());
					preparedStatement.setInt(5, orgId);
					preparedStatement.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
					int rowAffected = preparedStatement.executeUpdate();
					if (rowAffected == 1) {
						rs = preparedStatement.getGeneratedKeys();
						if (rs.next()) {
							organizationLimitedLiabilityPartnerVo.setId(rs.getInt(1));
						}
					}
				}
			}

		} catch (Exception e) {
			throw new ApplicationException(e);

		} finally {
			closeResources(rs, preparedStatement, null);
		}

		return data;
	}

	private NewOrganizationVo createOrganizationPublicLimitedDirector(int orgId, NewOrganizationVo data, Connection con)
			throws ApplicationException {
		logger.info("Entry into method:createOrganizationPublicLimitedDirector");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {

			String sql = OrganizationConstants.INSERT_INTO_ORGANIZATION_PUBLIC_LIMITED_DIRECTOR;
			if (data.getKeyMembers().getOrganizationPublicLimitedDirector() != null) {
				for (int i = 0; i < data.getKeyMembers().getOrganizationPublicLimitedDirector().size(); i++) {
					OrganizationPublicLimitedDirectorVo organizationPublicLimitedDirectorVo = data.getKeyMembers()
							.getOrganizationPublicLimitedDirector().get(i);
					preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
					preparedStatement.setString(1, organizationPublicLimitedDirectorVo.getName());
					preparedStatement.setString(2, organizationPublicLimitedDirectorVo.getEmailId());
					preparedStatement.setString(3, organizationPublicLimitedDirectorVo.getMobileNo());
					preparedStatement.setString(4, organizationPublicLimitedDirectorVo.getOwnershipPercentage());
					preparedStatement.setString(5, organizationPublicLimitedDirectorVo.getDin());
					preparedStatement.setInt(6, orgId);
					preparedStatement.setTimestamp(7, new Timestamp(System.currentTimeMillis()));
					int rowAffected = preparedStatement.executeUpdate();
					if (rowAffected == 1) {
						rs = preparedStatement.getGeneratedKeys();
						if (rs.next()) {
							organizationPublicLimitedDirectorVo.setId(rs.getInt(1));
						}
					}
				}
			}

		} catch (Exception e) {
			throw new ApplicationException(e);

		} finally {
			closeResources(rs, preparedStatement, null);
		}

		return data;
	}

	public BasicOrganizationVo deleteOrganization(int organizationId, String status) throws ApplicationException {
		logger.info("Entry into method:deleteOrganization");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		BasicOrganizationVo data = new BasicOrganizationVo();
		data.setId(organizationId);
		try {
			con = getUserMgmConnection();
			String sql = OrganizationConstants.DELETE_ORGANIZATION;
			preparedStatement = con.prepareStatement(sql);
			preparedStatement.setString(1, status);
			preparedStatement.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
			preparedStatement.setInt(3, organizationId);
			data.setId(organizationId);
			preparedStatement.executeUpdate();
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);

		}

		return data;
	}

	public List<BasicOrganizationVo> getAllNewOrganizations(int userId) throws ApplicationException {
		logger.info("Entry into method:method:getAllNewOrganizations");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		List<BasicOrganizationVo> data = new ArrayList<BasicOrganizationVo>();
		try {
			con = getUserMgmConnection();
			String query = OrganizationConstants.GET_BASIC_USER_ORGANIZATIONS;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, userId);
			preparedStatement.setString(2, "DEL");
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				BasicOrganizationVo orgVo = new BasicOrganizationVo();
				orgVo.setId(rs.getInt(1));
				orgVo.setName(rs.getString(2));
				orgVo.setStatus(rs.getString(3));
				orgVo.setCreateTs(rs.getTimestamp(4));
				orgVo.setDateFormat(rs.getString(5));
				orgVo.setNumberOfApplications(2);
				orgVo.setNumberOfUsers(userDao.getUserCount(orgVo.getId(), con));
				data.add(orgVo);

			}
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);

		}

		return data;
	}

	public NewOrganizationVo getOrganization(int organizationId) throws ApplicationException {
		logger.info("Entry into method:method:getOrganization");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		NewOrganizationVo data = new NewOrganizationVo();
		OrganizationGeneralInfoVo organizationGeneralInfoVo = new OrganizationGeneralInfoVo();
		KeyMembersVo keyMembers = new KeyMembersVo();
		data.setGeneralInfo(organizationGeneralInfoVo);
		data.setKeyMembers(keyMembers);
		try {
			con = getUserMgmConnection();
			String query = OrganizationConstants.GET_ORGANIZATION;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, organizationId);
			preparedStatement.setString(2, "DEL");
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				data.setId(rs.getInt(1));
				data.getGeneralInfo().setName(rs.getString(2));
				data.getGeneralInfo().setConstitutionId(rs.getInt(3));
				data.getGeneralInfo().setCinno(rs.getString(4));
				data.getGeneralInfo().setOrganizationTypeId(rs.getInt(5));
				data.getGeneralInfo().setIndustryOfOrganization(rs.getInt(6));
				data.getGeneralInfo().setBaseCurrency(rs.getInt(7));
				data.getGeneralInfo().setFinancialYear(rs.getString(8));
				data.setIsSuperAdmin(rs.getBoolean(9));
				data.setStatus(rs.getString(10));
				data.setUserId(rs.getString(11));
				data.setCreatTs(rs.getTimestamp(12));
				data.setUpdateTs(rs.getTimestamp(13));
				data.getGeneralInfo().setDateFormat(rs.getString(14));
				data.getGeneralInfo().setTimeZone(rs.getString(15));
				data.getGeneralInfo().setContactNumber(rs.getString(16));
				data.getGeneralInfo().setEmailId(rs.getString(17));
				data.getGeneralInfo().setIeCodeNo(rs.getString(18));
				data.getGeneralInfo().setIsCash(rs.getBoolean(19));
				data.setRocData(rs.getString(20));
				data.setGstDetails(getGSTDetails(data.getId(), con));
				if (data.getGeneralInfo().getConstitutionId() == 1) {
					data.getKeyMembers().setOrganizationProprietor(proprietor(data.getId(), con));

				} else if (data.getGeneralInfo().getConstitutionId() == 2) {
					data.getKeyMembers().setOrganizationPartnershipPartner(partner(data.getId(), con));

				} else if (data.getGeneralInfo().getConstitutionId() == 3) {
					data.getKeyMembers()
					.setOrganizationPrivateLimitedDirector(privateLimitedDirector(data.getId(), con));

				} else if (data.getGeneralInfo().getConstitutionId() == 4) {
					data.getKeyMembers().setOrganizationOnePersonDirector(onePersonDirector(data.getId(), con));

				} else if (data.getGeneralInfo().getConstitutionId() == 5) {
					data.getKeyMembers()
					.setOrganizationLimitedLiabilityPartner(limitedLiabilityPartner(data.getId(), con));

				} else if (data.getGeneralInfo().getConstitutionId() == 6) {
					data.getKeyMembers().setOrganizationPublicLimitedDirector(publicLimitedDirector(data.getId(), con));

				}

				List<UploadFileVo> uploadFileVos = new ArrayList<UploadFileVo>();
				for (AttachmentsVo attachments : attachmentsDao.getAttachmentDetails(data.getId(),
						AttachmentsConstants.MODULE_TYPE_ORG_DOCUMENTS)) {
					UploadFileVo uploadFileVo = new UploadFileVo();
					uploadFileVo.setId(attachments.getId());
					uploadFileVo.setName(attachments.getFileName());
					uploadFileVo.setSize(attachments.getSize());
					uploadFileVos.add(uploadFileVo);
				}
				data.setAttachments(uploadFileVos);

			}
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);

		}

		return data;
	}


	public MinimalOrganizationVo getMinimalOrganization(int organizationId) throws ApplicationException {
		logger.info("Entry into method:method:getMinimalOrganization");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		MinimalOrganizationVo data = new MinimalOrganizationVo();
		try {
			con = getUserMgmConnection();
			String query = OrganizationConstants.GET_ORGANIZATION;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, organizationId);
			preparedStatement.setString(2, "DEL");
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				data.setId(rs.getInt(1));
				data.setLegalName(rs.getString(2));
				data.setConstitutionId(rs.getInt(3));
				rs.getString(4);
				data.setOrganizationTypeId(rs.getInt(5));
				data.setIndustryOfOrganization(rs.getInt(6));
				data.setBaseCurrency(rs.getInt(7));
				data.setFinancialYear(rs.getString(8));
				rs.getBoolean(9);
				data.setStatus(rs.getString(10));
				data.setUserId(rs.getString(11));
				data.setCreateTs(rs.getTimestamp(12));
				rs.getTimestamp(13);
				data.setDateFormat(rs.getString(14));
				data.setTimeZone(rs.getString(15));
				rs.getString(16);
				rs.getString(17);
				rs.getString(18);
				rs.getBoolean(19);
				rs.getString(20);
				data.setDisplayName(rs.getString(21));
				data.setUserProfile(rs.getString(22));
				data.setBrandName(rs.getString(23));
				data.setPan(rs.getString(24));
			}
			closeResources(rs, preparedStatement, null);

			Map<String,String>gst=new HashMap<String,String>();
			query = OrganizationConstants.GET_GST_DETAILS_TAXILLA_DATA;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, organizationId);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				data.setGstRegnTypeId(rs.getInt(2));
				gst.put("gstNo", rs.getString(3));
				gst.put("gstData", null);
				//gst.put("gstData", rs.getString(4));
			}
			data.setGst(gst);
			closeResources(rs, preparedStatement, null);

			List<String>otherGsts =new ArrayList<String>();
			query = OrganizationConstants.GET_GST_LOCATION_TAXILLA_DATA;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, organizationId);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				otherGsts.add(rs.getString(2));
			}
			data.setOtherGsts(otherGsts);
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);

		}

		return data;
	}

	private OrganizationAddressVo getOrganizationAddress(int organizationId, Connection con)
			throws ApplicationException {

		logger.info("Entry into method:method:getOrganizationAddress");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		OrganizationAddressVo data = null;
		try {
			String query = OrganizationConstants.GET_USER_ORGANIZATIONS_ADDRESS;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, organizationId);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				data = new OrganizationAddressVo();
				data.setId(rs.getInt(1));
				data.setAddress1(rs.getString(2));
				data.setAddress2(rs.getString(3));
				data.setCity(rs.getString(4));
				data.setState(rs.getString(5));
				data.setCountry(rs.getString(6));
				data.setZipCode(rs.getString(7));
			}
		} catch (Exception e) {
			throw new ApplicationException(e);

		} finally {
			closeResources(rs, preparedStatement, null);
		}
		return data;
	}

	private OrganizationProprietorOtherDetailsVo getProprietorOtherDetails(int organizationId, Connection con)
			throws ApplicationException {

		logger.info("Entry into method:method:getProprietorOtherDetails");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		OrganizationProprietorOtherDetailsVo data = null;

		try {
			String query = OrganizationConstants.GET_PROPRIETOR_OTHER_DETAILS;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, organizationId);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				data = new OrganizationProprietorOtherDetailsVo();
				data.setId(rs.getInt(1));
				data.setDateFormat(rs.getString(2));
				data.setTimeZone(rs.getString(3));
				data.setContactNumber(rs.getString(4));
				data.setEmailId(rs.getString(5));
				data.setTaxPanNumber(rs.getString(6));
				data.setGstNumber(rs.getString(7));
				data.setIeCodeNumber(rs.getString(8));
				data.setReportCash(new Boolean(rs.getBoolean(9)).toString());
			}
		} catch (Exception e) {
			throw new ApplicationException(e);

		} finally {
			closeResources(rs, preparedStatement, null);
		}
		return data;
	}

	private OrganizationPartnershipOtherDetailsVo getPartnershipOtherDetails(int organizationId, Connection con)
			throws ApplicationException {

		logger.info("Entry into method:method:getPartnershipOtherDetails");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		OrganizationPartnershipOtherDetailsVo data = null;

		try {
			String query = OrganizationConstants.GET_PARTNERSHIP_OTHER_DETAILS;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, organizationId);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				data = new OrganizationPartnershipOtherDetailsVo();
				data.setId(rs.getInt(1));
				data.setDateFormat(rs.getString(2));
				data.setTimeZone(rs.getString(3));
				data.setContactNumber(rs.getString(4));
				data.setEmailId(rs.getString(5));
				data.setTaxPanNumber(rs.getString(6));
				data.setGstNumber(rs.getString(7));
				data.setIeCodeNumber(rs.getString(8));
				data.setReportCash(new Boolean(rs.getBoolean(9)).toString());
				data.setCertificateLicense(rs.getString(10));
				data.setPartnershipRegistrationNo(rs.getString(11));
			}
		} catch (Exception e) {
			throw new ApplicationException(e);

		} finally {
			closeResources(rs, preparedStatement, null);
		}
		return data;
	}

	private OrganizationOnePersonOtherDetailsVo getOnePersonOtherDetails(int organizationId, Connection con)
			throws ApplicationException {

		logger.info("Entry into method:method:getOnePersonOtherDetails");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		OrganizationOnePersonOtherDetailsVo data = null;

		try {
			String query = OrganizationConstants.GET_ONE_PERSON_OTHER_DETAILS;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, organizationId);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				data = new OrganizationOnePersonOtherDetailsVo();
				data.setId(rs.getInt(1));
				data.setDateFormat(rs.getString(2));
				data.setTimeZone(rs.getString(3));
				data.setContactNumber(rs.getString(4));
				data.setEmailId(rs.getString(5));
				data.setTaxPanNumber(rs.getString(6));
				data.setGstNumber(rs.getString(7));
				data.setIeCodeNumber(rs.getString(8));
				data.setReportCash(new Boolean(rs.getBoolean(9)).toString());
				data.setCin(rs.getString(10));
				data.setIncorporationDate(rs.getString(11));
			}
		} catch (Exception e) {
			throw new ApplicationException(e);

		} finally {
			closeResources(rs, preparedStatement, null);
		}
		return data;
	}

	private OrganizationPrivateLimitedOtherDetailsVo getPrivateLimitedOtherDetails(int organizationId, Connection con)
			throws ApplicationException {

		logger.info("Entry into method:method:getPrivateLimitedOtherDetails");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		OrganizationPrivateLimitedOtherDetailsVo data = null;

		try {
			String query = OrganizationConstants.GET_PRIVATE_LIMITED_OTHER_DETAILS;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, organizationId);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				data = new OrganizationPrivateLimitedOtherDetailsVo();
				data.setId(rs.getInt(1));
				data.setDateFormat(rs.getString(2));
				data.setTimeZone(rs.getString(3));
				data.setContactNumber(rs.getString(4));
				data.setEmailId(rs.getString(5));
				data.setTaxPanNumber(rs.getString(6));
				data.setGstNumber(rs.getString(7));
				data.setIeCodeNumber(rs.getString(8));
				data.setReportCash(new Boolean(rs.getBoolean(9)).toString());
				data.setCin(rs.getString(10));
				data.setIncorporationDate(rs.getString(11));
			}
		} catch (Exception e) {
			throw new ApplicationException(e);

		} finally {
			closeResources(rs, preparedStatement, null);
		}
		return data;
	}

	private OrganizationLimitedLiabilityOtherDetailsVo getLimitedLiabilityOtherDetails(int organizationId,
			Connection con) throws ApplicationException {

		logger.info("Entry into method:method:getLimitedLiabilityOtherDetails");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		OrganizationLimitedLiabilityOtherDetailsVo data = null;

		try {
			String query = OrganizationConstants.GET_LIMITED_LIABILITY_OTHER_DETAILS;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, organizationId);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				data = new OrganizationLimitedLiabilityOtherDetailsVo();
				data.setId(rs.getInt(1));
				data.setDateFormat(rs.getString(2));
				data.setTimeZone(rs.getString(3));
				data.setContactNumber(rs.getString(4));
				data.setEmailId(rs.getString(5));
				data.setTaxPanNumber(rs.getString(6));
				data.setGstNumber(rs.getString(7));
				data.setIeCodeNumber(rs.getString(8));
				data.setReportCash(new Boolean(rs.getBoolean(9)).toString());
				data.setLlpin(rs.getString(10));
				data.setIncorporationDate(rs.getString(11));
			}
		} catch (Exception e) {
			throw new ApplicationException(e);

		} finally {
			closeResources(rs, preparedStatement, null);
		}
		return data;
	}

	private OrganizationPublicLimitedOtherDetailsVo getPublicLimitedOtherDetails(int organizationId, Connection con)
			throws ApplicationException {

		logger.info("Entry into method:method:getPublicLimitedOtherDetails");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		OrganizationPublicLimitedOtherDetailsVo data = null;

		try {
			String query = OrganizationConstants.GET_PUBLIC_LIMITED_OTHER_DETAILS;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, organizationId);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				data = new OrganizationPublicLimitedOtherDetailsVo();
				data.setId(rs.getInt(1));
				data.setDateFormat(rs.getString(2));
				data.setTimeZone(rs.getString(3));
				data.setContactNumber(rs.getString(4));
				data.setEmailId(rs.getString(5));
				data.setTaxPanNumber(rs.getString(6));
				data.setGstNumber(rs.getString(7));
				data.setIeCodeNumber(rs.getString(8));
				data.setReportCash(new Boolean(rs.getBoolean(9)).toString());
				data.setCin(rs.getString(10));
				data.setIncorporationDate(rs.getString(11));
			}
		} catch (Exception e) {
			throw new ApplicationException(e);

		} finally {
			closeResources(rs, preparedStatement, null);
		}
		return data;
	}

	private List<OrganizationProprietorVo> proprietor(int organizationId, Connection con) throws ApplicationException {

		logger.info("Entry into method:method:proprietor");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		List<OrganizationProprietorVo> data = new ArrayList<OrganizationProprietorVo>();

		try {
			String query = OrganizationConstants.PROPRIETOR;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, organizationId);
			preparedStatement.setString(2, "DEL");
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				OrganizationProprietorVo data1 = new OrganizationProprietorVo();
				data1.setId(rs.getInt(1));
				data1.setName(rs.getString(2));
				data1.setEmailId(rs.getString(3));
				data1.setMobileNo(rs.getString(4));
				data1.setStatus(rs.getString(5));
				data.add(data1);

			}
		} catch (Exception e) {
			throw new ApplicationException(e);

		} finally {
			closeResources(rs, preparedStatement, null);
		}
		return data;
	}

	private List<OrganizationPartnershipPartnerVo> partner(int organizationId, Connection con)
			throws ApplicationException {

		logger.info("Entry into method:method:partner");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		List<OrganizationPartnershipPartnerVo> data = new ArrayList<OrganizationPartnershipPartnerVo>();

		try {
			String query = OrganizationConstants.PARTNER;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, organizationId);
			preparedStatement.setString(2, "DEL");
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				OrganizationPartnershipPartnerVo data1 = new OrganizationPartnershipPartnerVo();
				data1.setId(rs.getInt(1));
				data1.setName(rs.getString(2));
				data1.setEmailId(rs.getString(3));
				data1.setMobileNo(rs.getString(4));
				data1.setOwnershipPercentage(rs.getString(5));
				data1.setStatus(rs.getString(6));
				data.add(data1);

			}
		} catch (Exception e) {
			throw new ApplicationException(e);

		} finally {
			closeResources(rs, preparedStatement, null);
		}
		return data;
	}

	private List<OrganizationPrivateLimitedDirectorVo> privateLimitedDirector(int organizationId, Connection con)
			throws ApplicationException {

		logger.info("Entry into privateLimitedDirector");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		List<OrganizationPrivateLimitedDirectorVo> data = new ArrayList<OrganizationPrivateLimitedDirectorVo>();

		try {
			String query = OrganizationConstants.PRIVATE_LIMITED_DIRECTOR;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, organizationId);
			preparedStatement.setString(2, "DEL");
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				OrganizationPrivateLimitedDirectorVo data1 = new OrganizationPrivateLimitedDirectorVo();
				data1.setId(rs.getInt(1));
				data1.setName(rs.getString(2));
				data1.setEmailId(rs.getString(3));
				data1.setMobileNo(rs.getString(4));
				data1.setOwnershipPercentage(rs.getString(5));
				data1.setDin(rs.getString(6));
				data1.setStatus(rs.getString(7));
				data.add(data1);

			}
		} catch (Exception e) {
			throw new ApplicationException(e);

		} finally {
			closeResources(rs, preparedStatement, null);
		}
		return data;
	}

	private List<OrganizationOnePersonDirectorVo> onePersonDirector(int organizationId, Connection con)
			throws ApplicationException {

		logger.info("Entry into onePersonDirector");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		List<OrganizationOnePersonDirectorVo> data = new ArrayList<OrganizationOnePersonDirectorVo>();

		try {
			String query = OrganizationConstants.ONE_PERSON_DIRECTOR;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, organizationId);
			preparedStatement.setString(2, "DEL");
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				OrganizationOnePersonDirectorVo data1 = new OrganizationOnePersonDirectorVo();
				data1.setId(rs.getInt(1));
				data1.setName(rs.getString(2));
				data1.setEmailId(rs.getString(3));
				data1.setMobileNo(rs.getString(4));
				data1.setDin(rs.getString(5));
				data1.setStatus(rs.getString(6));
				data.add(data1);

			}
		} catch (Exception e) {
			throw new ApplicationException(e);

		} finally {
			closeResources(rs, preparedStatement, null);
		}
		return data;
	}

	private List<OrganizationLimitedLiabilityPartnerVo> limitedLiabilityPartner(int organizationId, Connection con)
			throws ApplicationException {

		logger.info("Entry into limitedLiabilityPartner");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		List<OrganizationLimitedLiabilityPartnerVo> data = new ArrayList<OrganizationLimitedLiabilityPartnerVo>();

		try {
			String query = OrganizationConstants.LIMITED_LIABILITY_PARTNER;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, organizationId);
			preparedStatement.setString(2, "DEL");
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				OrganizationLimitedLiabilityPartnerVo data1 = new OrganizationLimitedLiabilityPartnerVo();
				data1.setId(rs.getInt(1));
				data1.setName(rs.getString(2));
				data1.setEmailId(rs.getString(3));
				data1.setMobileNo(rs.getString(4));
				data1.setOwnershipPercentage(rs.getString(5));
				data1.setStatus(rs.getString(6));
				data.add(data1);

			}
		} catch (Exception e) {
			throw new ApplicationException(e);

		} finally {
			closeResources(rs, preparedStatement, null);
		}
		return data;
	}

	private List<OrganizationPublicLimitedDirectorVo> publicLimitedDirector(int organizationId, Connection con)
			throws ApplicationException {

		logger.info("Entry into publicLimitedDirector");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		List<OrganizationPublicLimitedDirectorVo> data = new ArrayList<OrganizationPublicLimitedDirectorVo>();

		try {
			String query = OrganizationConstants.PUBLIC_LIMITED_DIRECTOR;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, organizationId);
			preparedStatement.setString(2, "DEL");
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				OrganizationPublicLimitedDirectorVo data1 = new OrganizationPublicLimitedDirectorVo();
				data1.setId(rs.getInt(1));
				data1.setName(rs.getString(2));
				data1.setEmailId(rs.getString(3));
				data1.setMobileNo(rs.getString(4));
				data1.setOwnershipPercentage(rs.getString(5));
				data1.setDin(rs.getString(6));
				data1.setStatus(rs.getString(7));
				data.add(data1);

			}
		} catch (Exception e) {
			throw new ApplicationException(e);

		} finally {
			closeResources(rs, preparedStatement, null);
		}
		return data;
	}

	private GSTDetailsVo getGSTDetails(int organizationId, Connection con) throws ApplicationException {

		logger.info("Entry into getGSTDetails");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		GSTDetailsVo data = new GSTDetailsVo();
		List<LocationVo> locationVo = new ArrayList<LocationVo>();
		data.setLocation(locationVo);
		boolean isMultiGst = false;

		try {
			String query = OrganizationConstants.GST_DETAILS;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, organizationId);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				data.setId(rs.getInt(1));
				data.setGstRegnTypeId(rs.getInt(2));
				data.setGstNo(rs.getString(3));
				data.setTaxPanId(rs.getString(4));
				data.setAddress_1(rs.getString(5));
				data.setAddress_2(rs.getString(6));
				data.setCity(rs.getString(7));
				data.setState(Integer.parseInt(rs.getString(8)));
				data.setCountry(Integer.parseInt(rs.getString(9)));
				data.setPinCode(rs.getString(10));
				rs.getInt(11);
				isMultiGst = rs.getBoolean(12);
				data.setIsMultiGST(isMultiGst);

				closeResources(rs, preparedStatement, null);

				query = OrganizationConstants.GST_LOCATION;
				preparedStatement = con.prepareStatement(query);
				preparedStatement.setInt(1, organizationId);
				preparedStatement.setString(2, "DEL");
				rs = preparedStatement.executeQuery();
				while (rs.next()) {
					LocationVo locVo = new LocationVo();
					locVo.setId(rs.getInt(1));
					locVo.setName(rs.getString(2));
					locVo.setGstNo(rs.getString(3));
					locVo.setTaxPanId(rs.getString(4));
					locVo.setAddress_1(rs.getString(5));
					locVo.setAddress_2(rs.getString(6));
					locVo.setCity(rs.getString(7));
					locVo.setState(Integer.parseInt(rs.getString(8)));
					locVo.setCountry(Integer.parseInt(rs.getString(9)));
					locVo.setPinCode(rs.getString(10));
					rs.getInt(11);
					locVo.setStatus(rs.getString(12));
					locVo.setIsMultiGST(isMultiGst);
					locationVo.add(locVo);
				}
			}
		} catch (Exception e) {
			throw new ApplicationException(e);

		} finally {
			closeResources(rs, preparedStatement, null);
		}
		return data;
	}

	@SuppressWarnings("rawtypes")
	public void createSettingsAndPreferences(int organizationId, String userId) throws ApplicationException {
		logger.info("Entry into method:createSettingsAndPreferences");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		con = getUserMgmConnection();

		try {
			// List<BaseChartOfAccountsVo>
			// chartOfAccountsList=financeDao.getBaseChartOfAccounts();
			con.setAutoCommit(false);
			List<PaymentTermsVo> paymentTerms = financeDao.getAllBasicPaymentTerms();
			String sql = SettingsAndPreferencesConstants.INSERT_INTO_PAYMENT_TERMS_ORGANIZATION;
			preparedStatement = con.prepareStatement(sql);
			for (int i = 0; i < paymentTerms.size(); i++) {
				PaymentTermsVo data = paymentTerms.get(i);
				preparedStatement.setString(1, data.getPaymentTermsName());
				preparedStatement.setString(2, data.getDescription());
				preparedStatement.setString(3, data.getBaseDate());
				preparedStatement.setInt(4, data.getDaysLimit());
				String accountsTypes = String.join(",", data.getAccountType());
				preparedStatement.setString(5, accountsTypes);
				preparedStatement.setInt(6, organizationId);
				preparedStatement.setInt(7, Integer.valueOf(userId));
				preparedStatement.setBoolean(8, true);
				preparedStatement.setBoolean(9, true);
				preparedStatement.setString(10, "Super Admin");
				preparedStatement.executeUpdate();
			}
			closeResources(null, preparedStatement, null);

			List<ModuleTypeVo> workflowModules=financeDao.getWorkflowModules();
			for(int i=0;i<workflowModules.size();i++){
				ModuleTypeVo data=workflowModules.get(i);
				WorkflowSettingsVo workflowSettingsVo=new WorkflowSettingsVo();
				workflowSettingsVo.setModuleId(data.getId());
				workflowSettingsVo.setName("System Default:"+data.getName());
				workflowSettingsVo.setPriority(1);
				workflowSettingsVo.setIsBase(true);
				WorkflowSettingsRuleData ruleData = new WorkflowSettingsRuleData();
				ruleData.setValidationParameterId(null);
				ruleData.setChoiceId(null);
				ruleData.setChoiceName("");
				ruleData.setLessThan("");					
				ruleData.setEqual("");					
				ruleData.setIs(new ArrayList<Object>());					
				ruleData.setIsNot(new ArrayList<Object>());
				ruleData.setDisabled(false);
				ruleData.setEnabled(false);
				ruleData.setGreaterThan("");
				ruleData.setApprovalTypeId(0);			
				ruleData.setEmail(false);
				ruleData.setInApp(true);
				ruleData.setSms(false);
				ruleData.setWhatsApp(false);
				ruleData.setUsersList(new ArrayList<WorkflowSettingsCommonVo>());					
				//ruleData.setRolesList(new ArrayList<WorkflowSettingsCommonVo>());			
				ruleData.setSetSequence(new ArrayList<WorkflowSettingsCommonVo>());
				workflowSettingsVo.setData(ruleData);
				workflowSettingsVo.setDescription("All Transactions Require Approval Without Any Conditions");
				workflowSettingsVo.setOrganizationId(organizationId);
				workflowSettingsVo.setRoleName("Super Admin");
				workflowSettingsVo.setStatus(CommonConstants.STATUS_AS_ACTIVE);
				workflowSettingsVo.setUserId(userId);
				WorkflowGeneralSettingsVo workflowGeneralSettingsVo=new WorkflowGeneralSettingsVo();
				workflowGeneralSettingsVo.setModuleId(data.getId());
				workflowGeneralSettingsVo.setData(financeDao.getWorkflowGeneralSettingsForModule(data.getId()));
				workflowGeneralSettingsVo.setOrganizationId(organizationId);
				workflowGeneralSettingsVo.setRoleName("Super Admin");
				workflowGeneralSettingsVo.setStatus(CommonConstants.STATUS_AS_ACTIVE);
				workflowGeneralSettingsVo.setUserId(userId);
				workflowSettingsDao.createWorkflowSettings(workflowSettingsVo);
				workflowGeneralSettingsDao.createWorkflowGeneralSetting(workflowGeneralSettingsVo);
			}			

			Iterator it=getWorkflowConfiguration().entrySet().iterator();
			while(it.hasNext()){
				Map.Entry entry=(Map.Entry)it.next();
				String key=(String) entry.getKey();
				Boolean value=(Boolean) entry.getValue();
				sql = SettingsAndPreferencesConstants.INSERT_INTO_SETTINGS_MODULE_ORGANIZATION;
				preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				preparedStatement.setString(1,"Workflow");
				preparedStatement.setString(2, null);
				preparedStatement.setString(3, null);
				preparedStatement.setString(4, key);
				preparedStatement.setBoolean(5, value);
				preparedStatement.setInt(6, organizationId);
				preparedStatement.setInt(7, Integer.valueOf(userId));
				preparedStatement.setString(8, "Super Admin");
				preparedStatement.executeUpdate();
			}
			closeResources(null, preparedStatement, null);


			it=getJournalEntriesConfiguration().entrySet().iterator();
			while(it.hasNext()){
				Map.Entry entry=(Map.Entry)it.next();
				String key=(String) entry.getKey();
				Boolean value=(Boolean) entry.getValue();
				sql = SettingsAndPreferencesConstants.INSERT_INTO_SETTINGS_MODULE_ORGANIZATION;
				preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				preparedStatement.setString(1,"Journal Transactions");
				preparedStatement.setString(2, null);
				preparedStatement.setString(3, null);
				preparedStatement.setString(4, key);
				preparedStatement.setBoolean(5, value);
				preparedStatement.setInt(6, organizationId);
				preparedStatement.setInt(7, Integer.valueOf(userId));
				preparedStatement.setString(8, "Super Admin");
				preparedStatement.executeUpdate();
			}
			closeResources(null, preparedStatement, null);

			List<TaxRateTypeVo> taxRateType = financeDao.getBaseTaxRate();
			Map<String, Integer> taxRateTypeMap = new LinkedHashMap<String, Integer>();
			sql = SettingsAndPreferencesConstants.INSERT_INTO_TAX_RATE_TYPE_ORGANIZATION;
			preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			for (int i = 0; i < taxRateType.size(); i++) {
				TaxRateTypeVo data = taxRateType.get(i);
				preparedStatement.setString(1, data.getType());
				preparedStatement.setString(2, data.getUsageType());
				preparedStatement.setBoolean(3, true);
				preparedStatement.setInt(4, organizationId);
				preparedStatement.setInt(5, Integer.valueOf(userId));
				preparedStatement.setBoolean(6, true);
				preparedStatement.setString(7, data.getIsInter());
				int rowAffected = preparedStatement.executeUpdate();
				if (rowAffected == 1) {
					rs = preparedStatement.getGeneratedKeys();
					if (rs.next()) {
						taxRateTypeMap.put(data.getType(), rs.getInt(1));
					}
				}
			}
			closeResources(null, preparedStatement, null);

			List<TaxRateMappingVo> taxRateMapping = financeDao.getBaseTaxRateMapping();
			sql = SettingsAndPreferencesConstants.INSERT_INTO_TAX_RATE_MAPPING_ORGANIZATION;
			preparedStatement = con.prepareStatement(sql);
			for (int i = 0; i < taxRateMapping.size(); i++) {
				TaxRateMappingVo data = taxRateMapping.get(i);
				preparedStatement.setString(1, data.getName());
				if (taxRateTypeMap.containsKey(data.getTaxRateTypeName()))
					preparedStatement.setInt(2, taxRateTypeMap.get(data.getTaxRateTypeName()));
				preparedStatement.setString(3, data.getRate());
				preparedStatement.setBoolean(4, true);
				preparedStatement.setInt(5, organizationId);
				preparedStatement.setInt(6, Integer.valueOf(userId));
				preparedStatement.setBoolean(7, true);
				if (data.getTaxRateTypeName().contains("IGST")) {
					preparedStatement.setBoolean(8, true);
				} else {
					preparedStatement.setBoolean(8, false);
				}
				preparedStatement.setString(9, "Super Admin");
				preparedStatement.executeUpdate();
			}

			closeResources(null, preparedStatement, null);
			taxRateTypeMap.clear();
			List<TaxGroupVo> taxGroup = financeDao.getBaseTaxGroup();

			sql = SettingsAndPreferencesConstants.INSERT_INTO_TAX_GROUP_ORGANIZATION;
			preparedStatement = con.prepareStatement(sql);
			for (int i = 0; i < taxGroup.size(); i++) {
				TaxGroupVo data = taxGroup.get(i);
				preparedStatement.setString(1, data.getName());
				preparedStatement.setString(2, data.getTaxesIncluded());
				preparedStatement.setString(3, data.getCombinedRate());
				if (data.getName().contains("IGST")) {
					preparedStatement.setBoolean(4, new Boolean(true));
				} else {
					preparedStatement.setBoolean(4, new Boolean(false));
				}
				preparedStatement.setBoolean(5, true);
				preparedStatement.setInt(6, organizationId);
				preparedStatement.setInt(7, Integer.valueOf(userId));
				preparedStatement.setBoolean(8, true);
				preparedStatement.setString(9, "Super Admin");
				preparedStatement.executeUpdate();
			}
			closeResources(null, preparedStatement, null);

			List<BaseCurrencyVo> baseCurrency = financeDao.getBasicBaseCurrency();
			sql = SettingsAndPreferencesConstants.INSERT_INTO_CURRENCY_ORGANIZATION;
			preparedStatement = con.prepareStatement(sql);
			for (int i = 0; i < baseCurrency.size(); i++) {
				BaseCurrencyVo data = baseCurrency.get(i);
				preparedStatement.setString(1, data.getName());
				preparedStatement.setString(2, data.getDescription());
				preparedStatement.setString(3, data.getSymbol());
				preparedStatement.setString(4, data.getAlternateSymbol());
				preparedStatement.setBoolean(5, data.isSpaceRequired());
				preparedStatement.setBoolean(6, data.isMillions());
				preparedStatement.setInt(7, data.getNumberOfDecimalPlaces());
				preparedStatement.setString(8, data.getDecimalValueDenoter());
				preparedStatement.setInt(9, data.getNoOfDecimalsForAmoutInWords());
				preparedStatement.setString(10, data.getExchangeValue());
				preparedStatement.setTimestamp(11, new Timestamp(System.currentTimeMillis()));
				preparedStatement.setInt(12, organizationId);
				preparedStatement.setInt(13, Integer.parseInt(userId));
				preparedStatement.setBoolean(14, true);
				preparedStatement.setString(15, data.getValueFormat());
				preparedStatement.setBoolean(16, true);
				preparedStatement.executeUpdate();

			}
			closeResources(null, preparedStatement, null);

			Random objGenerator = new Random();
			sql = SettingsAndPreferencesConstants.INSERT_INTO_VOUCHER_MANAGEMENT_ORGANIZATION;
			preparedStatement = con.prepareStatement(sql);
			preparedStatement.setString(1, "Purchase Order");
			preparedStatement.setString(2, "Voucher Management of:" + organizationId);
			preparedStatement.setString(3, "" + objGenerator.nextInt(100000));
			preparedStatement.setString(4, "" + objGenerator.nextInt(1000000));
			preparedStatement.setInt(5, organizationId);
			preparedStatement.setInt(6, Integer.parseInt(userId));
			preparedStatement.setBoolean(7, true);
			preparedStatement.setString(8, "Purchase Order");
			preparedStatement.setString(9, "4");
			preparedStatement.setString(10, null);
			preparedStatement.setString(11, null);
			preparedStatement.setString(12, null);
			preparedStatement.setBoolean(13, true);
			preparedStatement.setString(14, "Super Admin");
			preparedStatement.executeUpdate();

			closeResources(null, preparedStatement, null);

			sql = SettingsAndPreferencesConstants.INSERT_INTO_VOUCHER_MANAGEMENT_ORGANIZATION;
			preparedStatement = con.prepareStatement(sql);
			preparedStatement.setString(1, "Invoice");
			preparedStatement.setString(2, "Voucher Management of:" + organizationId);
			preparedStatement.setString(3, "" + objGenerator.nextInt(1000000));
			preparedStatement.setString(4, "" + objGenerator.nextInt(1000000));
			preparedStatement.setInt(5, organizationId);
			preparedStatement.setInt(6, Integer.parseInt(userId));
			preparedStatement.setBoolean(7, true);
			preparedStatement.setString(8, "Invoice");
			preparedStatement.setString(9, "4");
			preparedStatement.setString(10, null);
			preparedStatement.setString(11, null);
			preparedStatement.setString(12, null);
			preparedStatement.setBoolean(13, true);
			preparedStatement.setString(14, "Super Admin");
			preparedStatement.executeUpdate();

			closeResources(null, preparedStatement, null);

			sql = SettingsAndPreferencesConstants.INSERT_INTO_VOUCHER_MANAGEMENT_ORGANIZATION;
			preparedStatement = con.prepareStatement(sql);
			preparedStatement.setString(1, "Expense-Vendor");
			preparedStatement.setString(2, "Voucher Management of:" + organizationId);
			preparedStatement.setString(3, "" + objGenerator.nextInt(1000000));
			preparedStatement.setString(4, "" + objGenerator.nextInt(1000000));
			preparedStatement.setInt(5, organizationId);
			preparedStatement.setInt(6, Integer.parseInt(userId));
			preparedStatement.setBoolean(7, true);
			preparedStatement.setString(8, "Expense-Vendor");
			preparedStatement.setString(9, "4");
			preparedStatement.setString(10, null);
			preparedStatement.setString(11, null);
			preparedStatement.setString(12, null);
			preparedStatement.setBoolean(13, true);
			preparedStatement.setString(14, "Super Admin");
			preparedStatement.executeUpdate();

			closeResources(null, preparedStatement, null);

			sql = SettingsAndPreferencesConstants.INSERT_INTO_VOUCHER_MANAGEMENT_ORGANIZATION;
			preparedStatement = con.prepareStatement(sql);
			preparedStatement.setString(1, "Expense-Non Vendor");
			preparedStatement.setString(2, "Voucher Management of:" + organizationId);
			preparedStatement.setString(3, "" + objGenerator.nextInt(1000000));
			preparedStatement.setString(4, "" + objGenerator.nextInt(1000000));
			preparedStatement.setInt(5, organizationId);
			preparedStatement.setInt(6, Integer.parseInt(userId));
			preparedStatement.setBoolean(7, true);
			preparedStatement.setString(8, "Expense-Non Vendor");
			preparedStatement.setString(9, "4");
			preparedStatement.setString(10, null);
			preparedStatement.setString(11, null);
			preparedStatement.setString(12, null);
			preparedStatement.setBoolean(13, true);
			preparedStatement.setString(14, "Super Admin");
			preparedStatement.executeUpdate();

			sql = SettingsAndPreferencesConstants.INSERT_INTO_VOUCHER_MANAGEMENT_ORGANIZATION;
			preparedStatement = con.prepareStatement(sql);
			preparedStatement.setString(1, "Accounting Entries");
			preparedStatement.setString(2, "Voucher Management of:" + organizationId);
			preparedStatement.setString(3, "" + objGenerator.nextInt(1000000));
			preparedStatement.setString(4, "" + objGenerator.nextInt(1000000));
			preparedStatement.setInt(5, organizationId);
			preparedStatement.setInt(6, Integer.parseInt(userId));
			preparedStatement.setBoolean(7, true);
			preparedStatement.setString(8, "Accounting Entries");
			preparedStatement.setString(9, "4");
			preparedStatement.setString(10, null);
			preparedStatement.setString(11, null);
			preparedStatement.setString(12, null);
			preparedStatement.setBoolean(13, true);
			preparedStatement.setString(14, "Super Admin");
			preparedStatement.executeUpdate();

			sql = SettingsAndPreferencesConstants.INSERT_INTO_VOUCHER_MANAGEMENT_ORGANIZATION;
			preparedStatement = con.prepareStatement(sql);
			preparedStatement.setString(1, "Payroll-PayRun");
			preparedStatement.setString(2, "Voucher Management of:" + organizationId);
			preparedStatement.setString(3, "" + objGenerator.nextInt(1000000));
			preparedStatement.setString(4, "" + objGenerator.nextInt(1000000));
			preparedStatement.setInt(5, organizationId);
			preparedStatement.setInt(6, Integer.parseInt(userId));
			preparedStatement.setBoolean(7, true);
			preparedStatement.setString(8, "Payroll-PayRun");
			preparedStatement.setString(9, "4");
			preparedStatement.setString(10, null);
			preparedStatement.setString(11, null);
			preparedStatement.setString(12, null);
			preparedStatement.setBoolean(13, true);
			preparedStatement.setString(14, "Super Admin");
			preparedStatement.executeUpdate();

			sql = SettingsAndPreferencesConstants.INSERT_INTO_VOUCHER_MANAGEMENT_ORGANIZATION;
			preparedStatement = con.prepareStatement(sql);
			preparedStatement.setString(1, "Accounts Receivable-Refund");
			preparedStatement.setString(2, "Voucher Management of:" + organizationId);
			preparedStatement.setString(3, "" + objGenerator.nextInt(1000000));
			preparedStatement.setString(4, "" + objGenerator.nextInt(1000000));
			preparedStatement.setInt(5, organizationId);
			preparedStatement.setInt(6, Integer.parseInt(userId));
			preparedStatement.setBoolean(7, true);
			preparedStatement.setString(8, "Accounts Receivable-Refund");
			preparedStatement.setString(9, "4");
			preparedStatement.setString(10, null);
			preparedStatement.setString(11, null);
			preparedStatement.setString(12, null);
			preparedStatement.setBoolean(13, true);
			preparedStatement.setString(14, "Super Admin");
			preparedStatement.executeUpdate();

			sql = SettingsAndPreferencesConstants.INSERT_INTO_VOUCHER_MANAGEMENT_ORGANIZATION;
			preparedStatement = con.prepareStatement(sql);
			preparedStatement.setString(1, "Accounts Receivable-Receipt");
			preparedStatement.setString(2, "Voucher Management of:" + organizationId);
			preparedStatement.setString(3, "" + objGenerator.nextInt(1000000));
			preparedStatement.setString(4, "" + objGenerator.nextInt(1000000));
			preparedStatement.setInt(5, organizationId);
			preparedStatement.setInt(6, Integer.parseInt(userId));
			preparedStatement.setBoolean(7, true);
			preparedStatement.setString(8, "Accounts Receivable-Receipt");
			preparedStatement.setString(9, "4");
			preparedStatement.setString(10, null);
			preparedStatement.setString(11, null);
			preparedStatement.setString(12, null);
			preparedStatement.setBoolean(13, true);
			preparedStatement.setString(14, "Super Admin");
			preparedStatement.executeUpdate();

			sql = SettingsAndPreferencesConstants.INSERT_INTO_VOUCHER_MANAGEMENT_ORGANIZATION;
			preparedStatement = con.prepareStatement(sql);
			preparedStatement.setString(1, "Accounts Receivable-Invoice");
			preparedStatement.setString(2, "Voucher Management of:" + organizationId);
			preparedStatement.setString(3, "" + objGenerator.nextInt(1000000));
			preparedStatement.setString(4, "" + objGenerator.nextInt(1000000));
			preparedStatement.setInt(5, organizationId);
			preparedStatement.setInt(6, Integer.parseInt(userId));
			preparedStatement.setBoolean(7, true);
			preparedStatement.setString(8, "Accounts Receivable-Invoice");
			preparedStatement.setString(9, "4");
			preparedStatement.setString(10, null);
			preparedStatement.setString(11, null);
			preparedStatement.setString(12, null);
			preparedStatement.setBoolean(13, true);
			preparedStatement.setString(14, "Super Admin");
			preparedStatement.executeUpdate();

			sql = SettingsAndPreferencesConstants.INSERT_INTO_VOUCHER_MANAGEMENT_ORGANIZATION;
			preparedStatement = con.prepareStatement(sql);
			preparedStatement.setString(1, "Accounts Receivable-Credit Note");
			preparedStatement.setString(2, "Voucher Management of:" + organizationId);
			preparedStatement.setString(3, "" + objGenerator.nextInt(1000000));
			preparedStatement.setString(4, "" + objGenerator.nextInt(1000000));
			preparedStatement.setInt(5, organizationId);
			preparedStatement.setInt(6, Integer.parseInt(userId));
			preparedStatement.setBoolean(7, true);
			preparedStatement.setString(8, "Accounts Receivable-Credit Note");
			preparedStatement.setString(9, "4");
			preparedStatement.setString(10, null);
			preparedStatement.setString(11, null);
			preparedStatement.setString(12, null);
			preparedStatement.setBoolean(13, true);
			preparedStatement.setString(14, "Super Admin");
			preparedStatement.executeUpdate();


			sql = SettingsAndPreferencesConstants.INSERT_INTO_VOUCHER_MANAGEMENT_ORGANIZATION;
			preparedStatement = con.prepareStatement(sql);
			preparedStatement.setString(1, "Payments");
			preparedStatement.setString(2, "Voucher Management of:" + organizationId);
			preparedStatement.setString(3, "" + objGenerator.nextInt(1000000));
			preparedStatement.setString(4, "" + objGenerator.nextInt(1000000));
			preparedStatement.setInt(5, organizationId);
			preparedStatement.setInt(6, Integer.parseInt(userId));
			preparedStatement.setBoolean(7, true);
			preparedStatement.setString(8, "Payments");
			preparedStatement.setString(9, "4");
			preparedStatement.setString(10, null);
			preparedStatement.setString(11, null);
			preparedStatement.setString(12, null);
			preparedStatement.setBoolean(13, true);
			preparedStatement.setString(14, "Super Admin");
			preparedStatement.executeUpdate();

			sql = SettingsAndPreferencesConstants.INSERT_INTO_VOUCHER_MANAGEMENT_ORGANIZATION;
			preparedStatement = con.prepareStatement(sql);
			preparedStatement.setString(1, "Banking-Contra");
			preparedStatement.setString(2, "Voucher Management of:" + organizationId);
			preparedStatement.setString(3, "" + objGenerator.nextInt(1000000));
			preparedStatement.setString(4, "" + objGenerator.nextInt(1000000));
			preparedStatement.setInt(5, organizationId);
			preparedStatement.setInt(6, Integer.parseInt(userId));
			preparedStatement.setBoolean(7, true);
			preparedStatement.setString(8, "Banking-Contra");
			preparedStatement.setString(9, "4");
			preparedStatement.setString(10, null);
			preparedStatement.setString(11, null);
			preparedStatement.setString(12, null);
			preparedStatement.setBoolean(13, true);
			preparedStatement.setString(14, "Super Admin");
			preparedStatement.executeUpdate();

			sql = SettingsAndPreferencesConstants.INSERT_INTO_VOUCHER_MANAGEMENT_ORGANIZATION;
			preparedStatement = con.prepareStatement(sql);
			preparedStatement.setString(1, "Accounts Receivable-Application of Funds");
			preparedStatement.setString(2, "Voucher Management of:" + organizationId);
			preparedStatement.setString(3, "" + objGenerator.nextInt(1000000));
			preparedStatement.setString(4, "" + objGenerator.nextInt(1000000));
			preparedStatement.setInt(5, organizationId);
			preparedStatement.setInt(6, Integer.parseInt(userId));
			preparedStatement.setBoolean(7, true);
			preparedStatement.setString(8, "Accounts Receivable-Application of Funds");
			preparedStatement.setString(9, "4");
			preparedStatement.setString(10, null);
			preparedStatement.setString(11, null);
			preparedStatement.setString(12, null);
			preparedStatement.setBoolean(13, true);
			preparedStatement.setString(14, "Super Admin");
			preparedStatement.executeUpdate();

			RoleVo roleVo = new RoleVo();
			roleVo.setName("Super Admin");
			roleVo.setDescription("Super Admin");
			roleVo.setOrganizationId(organizationId);
			roleVo.setStatus("ACT");
			roleVo.setUserId(userId);
			roleVo.setRoleName("Super Admin");
			roleVo.setAccessData(accessAndRolesDao.getAccess(1));
			roleDao.createRole(roleVo);

			roleVo = new RoleVo();
			roleVo.setName("User");
			roleVo.setDescription("User");
			roleVo.setOrganizationId(organizationId);
			roleVo.setStatus("ACT");
			roleVo.setUserId(userId);
			roleVo.setRoleName("Super Admin");
			roleVo.setAccessData(accessAndRolesDao.getAccess(1));
			roleDao.createRole(roleVo);

			roleVo = new RoleVo();
			roleVo.setName("Admin");
			roleVo.setDescription("Admin");
			roleVo.setOrganizationId(organizationId);
			roleVo.setStatus("ACT");
			roleVo.setUserId(userId);
			roleVo.setRoleName("Super Admin");
			roleVo.setAccessData(accessAndRolesDao.getAccess(1));
			roleDao.createRole(roleVo);

			roleVo = new RoleVo();
			roleVo.setName("Accountant");
			roleVo.setDescription("Accountant");
			roleVo.setOrganizationId(organizationId);
			roleVo.setStatus("ACT");
			roleVo.setUserId(userId);
			roleVo.setRoleName("Super Admin");
			roleVo.setAccessData(accessAndRolesDao.getAccess(1));
			roleDao.createRole(roleVo);

			roleVo = new RoleVo();
			roleVo.setName("Procurement");
			roleVo.setDescription("Procurement");
			roleVo.setOrganizationId(organizationId);
			roleVo.setStatus("ACT");
			roleVo.setUserId(userId);
			roleVo.setRoleName("Super Admin");
			roleVo.setAccessData(accessAndRolesDao.getAccess(1));
			roleDao.createRole(roleVo);

			roleVo = new RoleVo();
			roleVo.setName("Vendor");
			roleVo.setDescription("Vendor");
			roleVo.setOrganizationId(organizationId);
			roleVo.setStatus("ACT");
			roleVo.setUserId(userId);
			roleVo.setRoleName("Super Admin");
			roleVo.setAccessData(accessAndRolesDao.getAccess(2));
			roleDao.createRole(roleVo);

			roleVo = new RoleVo();
			roleVo.setName("Customer");
			roleVo.setDescription("Customer");
			roleVo.setOrganizationId(organizationId);
			roleVo.setStatus("ACT");
			roleVo.setUserId(userId);
			roleVo.setRoleName("Super Admin");
			roleVo.setAccessData(accessAndRolesDao.getAccess(3));
			roleDao.createRole(roleVo);




			closeResources(null, preparedStatement, null);

			List<BaseGeneralSettingsVo> baseGeneralSettingsData=financeDao.getBaseGeneralSettings();
			sql = VendorSettingsConstants.INSERT_INTO_GENERAL_SETTINGS_ORGANIZATION;
			preparedStatement = con.prepareStatement(sql);
			for (int i = 0; i < baseGeneralSettingsData.size(); i++) {
				BaseGeneralSettingsVo data = baseGeneralSettingsData.get(i);
				preparedStatement.setInt(1, data.getId());
				preparedStatement.setBoolean(2, true);
				preparedStatement.setInt(3, Integer.valueOf(userId));
				preparedStatement.setInt(4, organizationId);				
				preparedStatement.setBoolean(5, true);

				preparedStatement.executeUpdate();
			}
			closeResources(null, preparedStatement, null);

			sql= SettingsAndPreferencesConstants.INSERT_INTO_PAY_CYCLE_ORGANIZATION;
			preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setString(1, "Regular");
			preparedStatement.setString(2,"Regular");
			preparedStatement.setDate(3,null);
			preparedStatement.setDate(4,null);
			preparedStatement.setString(5, "ACT");
			preparedStatement.setInt(6, organizationId);
			preparedStatement.setString(7,userId);
			preparedStatement.setString(8, "Super Admin");
			preparedStatement.setTimestamp(9, new Timestamp(System.currentTimeMillis()));
			preparedStatement.executeUpdate();

			closeResources(null, preparedStatement, null);

			sql= SettingsAndPreferencesConstants.INSERT_INTO_PAY_CYCLE_ORGANIZATION;
			preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setString(1, "OffSeason");
			preparedStatement.setString(2,"OffSeason");
			preparedStatement.setDate(3,null);
			preparedStatement.setDate(4,null);
			preparedStatement.setString(5, "ACT");
			preparedStatement.setInt(6, organizationId);
			preparedStatement.setString(7,userId);
			preparedStatement.setString(8, "Super Admin");
			preparedStatement.setTimestamp(9, new Timestamp(System.currentTimeMillis()));
			preparedStatement.executeUpdate();

			closeResources(null, preparedStatement, null);
			/*
			 * sql =
			 * SettingsAndPreferencesConstants.INSERT_INTO_CHART_OF_ACCOUNTS_ORGANIZATION;
			 * preparedStatement = con.prepareStatement(sql); for (int i = 0; i <
			 * chartOfAccountsList.size(); i++) { BaseChartOfAccountsVo data =
			 * chartOfAccountsList.get(i); preparedStatement.setString(1, data.getLevel1());
			 * preparedStatement.setString(2, data.getLevel2());
			 * preparedStatement.setString(3, data.getLevel3());
			 * preparedStatement.setString(4, data.getLevel4());
			 * preparedStatement.setString(5, data.getLevel5());
			 * preparedStatement.setString(6, data.getLevel6());
			 * preparedStatement.setString(7, data.getAccountCode());
			 * preparedStatement.setString(8, data.getModule());
			 * preparedStatement.setString(9, data.getAccountingEntries());
			 * preparedStatement.setString(10, data.getMandatorySubLedger());
			 * preparedStatement.setString(11, data.getCompanyType());
			 * preparedStatement.setInt(12, organizationId); preparedStatement.setInt(13,
			 * Integer.parseInt(userId)); preparedStatement.setBoolean(14, true);
			 * preparedStatement.executeUpdate();
			 * 
			 * } closeResources(null, preparedStatement, null);
			 * 
			 * sql = SettingsAndPreferencesConstants.
			 * INSERT_INTO_CHART_OF_ACCOUNTS_ORGANIZATION_ACCOUNTING_TYPE; preparedStatement
			 * = con.prepareStatement(sql); for(int j=1;j<=7;j++){ for (int i = 0; i <
			 * chartOfAccountsList.size(); i++) { BaseChartOfAccountsVo data =
			 * chartOfAccountsList.get(i); preparedStatement.setString(1, data.getLevel1());
			 * preparedStatement.setString(2, data.getLevel2());
			 * preparedStatement.setString(3, data.getLevel3());
			 * preparedStatement.setString(4, data.getLevel4());
			 * preparedStatement.setString(5, data.getLevel5());
			 * preparedStatement.setString(6, data.getLevel6());
			 * preparedStatement.setString(7, data.getAccountCode());
			 * preparedStatement.setString(8, data.getModule());
			 * preparedStatement.setString(9, data.getAccountingEntries());
			 * preparedStatement.setString(10, data.getMandatorySubLedger());
			 * preparedStatement.setString(11, data.getCompanyType());
			 * preparedStatement.setInt(12, organizationId); preparedStatement.setInt(13,
			 * Integer.parseInt(userId)); preparedStatement.setBoolean(14, true);
			 * preparedStatement.setInt(15, j); preparedStatement.executeUpdate(); }
			 * 
			 * } closeResources(null, preparedStatement, null);
			 */

			insertIntoChartOfAccounts(con, organizationId, userId);			
			insertIntoProductCategory(organizationId, userId,con);

			con.commit();

			/*int level5Id_gstRefundReceivable = chartOfAccountsDao.getChartOfAccountsLevel5Id(organizationId,
					"Short-term loans and advances", "Balances with government authorities", "GST Refund Receivable");
			int level5Id_inputGST = chartOfAccountsDao.getChartOfAccountsLevel5Id(organizationId,
					"Other Current Assets", "Others", "Input GST");
			int level5Id_gstRefund = chartOfAccountsDao.getChartOfAccountsLevel5Id(organizationId,
					"Other Current Assets", "Others", "GST Refund");
			int level5Id_ncGstInputCredit = chartOfAccountsDao.getChartOfAccountsLevel5Id(organizationId,
					"Long-term loans and advances", "Balances with government authorities", "NC - GST Input Credit");
			int level5Id_ncProvisionForDoubtful = chartOfAccountsDao.getChartOfAccountsLevel5Id(organizationId,
					"Long-term provisions", "Provision - Others", "NC - Provision for doubtful GST refund");
			int level5Id_gstPayable = chartOfAccountsDao.getChartOfAccountsLevel5Id(organizationId,
					"Other current liabilities", "Statutory remittances", "GST Payable");
			int level5Id_gstRCMPayable = chartOfAccountsDao.getChartOfAccountsLevel5Id(organizationId,
					"Other current liabilities", "Statutory remittances", "GST-RCM Payable");
			int level5Id_outputGst = chartOfAccountsDao.getChartOfAccountsLevel5Id(organizationId,
					"Other current liabilities", "Output GST", "Output GST Control A/c");
			int level5Id_provisionForDoutbful = chartOfAccountsDao.getChartOfAccountsLevel5Id(organizationId,
					"Short-term provisions", "Provision - Others", "Provision for doubtful GST refund");*/

			/*for (int i = 0; i < taxRateMapping.size(); i++) {
				TaxRateMappingVo taxRateMappingVo = taxRateMapping.get(i);
				chartOfAccountsDao.createLevel6(taxRateMappingVo.getName(), "GST", organizationId, userId, true,
						level5Id_gstRefundReceivable, true, taxRateMappingVo.getName());
				chartOfAccountsDao.createLevel6(taxRateMappingVo.getName(), "GST", organizationId, userId, true,
						level5Id_inputGST, true, taxRateMappingVo.getName());
				chartOfAccountsDao.createLevel6(taxRateMappingVo.getName(), "GST", organizationId, userId, true,
						level5Id_gstRefund, true, taxRateMappingVo.getName());
				chartOfAccountsDao.createLevel6(taxRateMappingVo.getName(), "GST", organizationId, userId, true,
						level5Id_ncGstInputCredit, true, taxRateMappingVo.getName());
				chartOfAccountsDao.createLevel6(taxRateMappingVo.getName(), "GST", organizationId, userId, true,
						level5Id_ncProvisionForDoubtful, true, taxRateMappingVo.getName());
				chartOfAccountsDao.createLevel6(taxRateMappingVo.getName(), "GST", organizationId, userId, true,
						level5Id_gstPayable, true, taxRateMappingVo.getName());
				chartOfAccountsDao.createLevel6(taxRateMappingVo.getName(), "GST", organizationId, userId, true,
						level5Id_gstRCMPayable, true, taxRateMappingVo.getName());
				chartOfAccountsDao.createLevel6(taxRateMappingVo.getName(), "GST", organizationId, userId, true,
						level5Id_outputGst, true, taxRateMappingVo.getName());
				chartOfAccountsDao.createLevel6(taxRateMappingVo.getName(), "GST", organizationId, userId, true,
						level5Id_provisionForDoutbful, true, taxRateMappingVo.getName());

			}*/

			/*	con = getFinanceCommon();
			int level5Id_tdsPayable = chartOfAccountsDao.getChartOfAccountsLevel5Id(organizationId,
					"Other current liabilities", "Statutory remittances", "TDS Payable");
			int level5Id_tcsPayable = chartOfAccountsDao.getChartOfAccountsLevel5Id(organizationId,
					"Other current liabilities", "Statutory remittances", "TCS Payable");
			List<TDSVo> tdsList = financeDao.getTDS(con);
			for (int i = 0; i < tdsList.size(); i++) {
				TDSVo tdsVo = tdsList.get(i);
				chartOfAccountsDao.createLevel6("" + tdsVo.getId(), "TDS", organizationId, userId, true,
						level5Id_tdsPayable, true, tdsVo.getTdsRateIdentifier());
				chartOfAccountsDao.createLevel6("" + tdsVo.getId(), "TDS", organizationId, userId, true,
						level5Id_tcsPayable, true, tdsVo.getTdsRateIdentifier());
			}

			closeResources(null, null, con);
			 */

			List<PayTypeVo> payTypeList=financeDao.getBasePayType();
			for(int i=0;i<payTypeList.size();i++){
				PayTypeVo data=payTypeList.get(i);
				data.setIsBase(true);
				data.setOrganizationId(organizationId);
				data.setUserId(userId);
				data.setIsSuperAdmin(true);
				payTypeDao.createPayType(data);
			}

			List<PayItemVo> payItemList=financeDao.getBasePayItem();
			for(int i=0;i<payItemList.size();i++){
				PayItemVo data=payItemList.get(i);
				data.setDescription(data.getName());
				data.setOrganizationId(organizationId);
				data.setIsSuperAdmin(true);
				data.setUserId(userId);
				data.setIsBase(true);
				data.setPayTypeName(data.getPayTypeName());
				data.setPayType(payTypeDao.getPayTypeByName(organizationId,data.getPayTypeName()).getId());
				data.setLedgerName(data.getLedgerName());
				int ledgerId=chartOfAccountsDao.getLedgerIdGivenName(data.getLedgerName(), organizationId);
				if(ledgerId>0){
					data.setLedgerId(ledgerId);
					payItemDao.createPayItemBaseOrganizationData(data);
				}
			}

		} catch (Exception e) {
			try {
				con.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				throw new ApplicationException(e);
			}
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);

		}
	}


	public OrganizationDropDownVo getOrganizationDropDownDataV2() throws ApplicationException {
		logger.info("Entry into method:getOrganizationDropDownDataV2");
		OrganizationDropDownVo data = new OrganizationDropDownVo();
		try {

			data.setOrganizationConstitution(financeDao.getBasicOrganizationConstitution());
			// data.setOrganizationIndustry(organizationIndustryDao.getBasicOrganizationIndustry());
			data.setOrganizationType(financeDao.getBasicOrganizationType());
			data.setCurrency(financeDao.getMinimalBasicCurrency());
			data.setCountry(financeDao.getCountryAndStateList());

		} catch (Exception e) {
			throw new ApplicationException(e);
		}
		return data;
	}

	public BasicOrganizationVo updateOrganization(NewOrganizationVo organizationVo) throws ApplicationException {
		logger.info("Entry into method:updateOrganization");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		con = getUserMgmConnection();
		try {
			con.setAutoCommit(false);
			updateBaseOrganizationDetails(organizationVo, con);
			updateGstAndLocationDetails(organizationVo, con);
			if (organizationVo.getKeyMembers().getOrganizationProprietor() != null) {
				updateOrganizationProprietor(organizationVo, con);
			} else if (organizationVo.getKeyMembers().getOrganizationPartnershipPartner() != null) {
				updateOrganizationPartner(organizationVo, con);
			} else if (organizationVo.getKeyMembers().getOrganizationPrivateLimitedDirector() != null) {
				updateOrganizationPrivateLimited(organizationVo, con);
			} else if (organizationVo.getKeyMembers().getOrganizationOnePersonDirector() != null) {
				updateOrganizationOnePerson(organizationVo, con);
			} else if (organizationVo.getKeyMembers().getOrganizationLimitedLiabilityPartner() != null) {
				updateOrganizationLimitedLiability(organizationVo, con);
			} else if (organizationVo.getKeyMembers().getOrganizationPublicLimitedDirector() != null) {
				updateOrganizationPublicLimited(organizationVo, con);
			}
			if (organizationVo.getAttachments() != null && organizationVo.getAttachments().size() > 0) {
				attachmentsDao.createAttachments(organizationVo.getId(),organizationVo.getUserId(),organizationVo.getAttachments(),
						AttachmentsConstants.MODULE_TYPE_ORG_DOCUMENTS, organizationVo.getId(),"Super Admin");
			}

			if (organizationVo.getAttachmentsToRemove() != null && organizationVo.getAttachmentsToRemove().size() > 0) {
				for (Integer attachmentId : organizationVo.getAttachmentsToRemove()) {
					attachmentsDao.changeStatusForSingleAttachment(attachmentId, CommonConstants.STATUS_AS_DELETE,organizationVo.getUserId(),"Super Admin");
				}

			}
			con.commit();

		} catch (Exception e) {
			try {
				con.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				throw new ApplicationException(e1);
			}
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);

		}
		BasicOrganizationVo basicOrganizationVo = new BasicOrganizationVo();
		basicOrganizationVo.setId(organizationVo.getId());
		basicOrganizationVo.setName(organizationVo.getGeneralInfo().getName());

		return basicOrganizationVo;

	}


	public BasicOrganizationVo updateMinimalOrganization(MinimalOrganizationVo organizationVo) throws ApplicationException {
		logger.info("Entry into method:updateMinimalOrganization");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		con = getUserMgmConnection();
		try {
			con.setAutoCommit(false);

			String sql = OrganizationConstants.UPDATE_ORGANIZATION_MINIMAL;

			preparedStatement = con.prepareStatement(sql);
			preparedStatement.setString(1, organizationVo.getPan());
			preparedStatement.setInt(2, organizationVo.getConstitutionId());			
			preparedStatement.setInt(3, organizationVo.getOrganizationTypeId());
			preparedStatement.setInt(4, organizationVo.getIndustryOfOrganization());
			preparedStatement.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
			preparedStatement.setString(6, organizationVo.getDisplayName());
			preparedStatement.setString(7, organizationVo.getUserProfile());
			preparedStatement.setString(8, organizationVo.getBrandName());
			preparedStatement.setString(9, organizationVo.getLegalName());
			preparedStatement.setInt(10, organizationVo.getId());
			preparedStatement.executeUpdate();
			con.commit();

		} catch (Exception e) {
			try {
				con.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				throw new ApplicationException(e1);
			}
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);

		}
		BasicOrganizationVo basicOrganizationVo = new BasicOrganizationVo();
		basicOrganizationVo.setId(organizationVo.getId());
		basicOrganizationVo.setName(organizationVo.getLegalName());

		return basicOrganizationVo;

	}

	private void updateBaseOrganizationDetails(NewOrganizationVo organizationVo, Connection con)
			throws ApplicationException {
		logger.info("Entry into method:updateBaseOrganizationDetailsAndAddress");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {

			String sql = OrganizationConstants.UPDATE_ORGANIZATION;

			preparedStatement = con.prepareStatement(sql);
			preparedStatement.setInt(1, organizationVo.getGeneralInfo().getConstitutionId());
			preparedStatement.setString(2, organizationVo.getGeneralInfo().getCinno());
			preparedStatement.setInt(3, organizationVo.getGeneralInfo().getOrganizationTypeId());
			preparedStatement.setInt(4, organizationVo.getGeneralInfo().getIndustryOfOrganization());
			preparedStatement.setInt(5, organizationVo.getGeneralInfo().getBaseCurrency());
			preparedStatement.setString(6, organizationVo.getGeneralInfo().getFinancialYear());
			preparedStatement.setTimestamp(7, new Timestamp(System.currentTimeMillis()));
			preparedStatement.setString(8, organizationVo.getGeneralInfo().getDateFormat());
			preparedStatement.setString(9, organizationVo.getGeneralInfo().getTimeZone());
			preparedStatement.setString(10, organizationVo.getGeneralInfo().getContactNumber());
			preparedStatement.setString(11, organizationVo.getGeneralInfo().getEmailId());
			preparedStatement.setString(12, organizationVo.getGeneralInfo().getIeCodeNo());
			preparedStatement.setBoolean(13, organizationVo.getGeneralInfo().getIsCash());
			preparedStatement.setInt(14, organizationVo.getId());
			preparedStatement.executeUpdate();
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, null);

		}
	}

	private void updateGstAndLocationDetails(NewOrganizationVo organizationVo, Connection con)
			throws ApplicationException {
		logger.info("Entry into method:updateGstAndLocationDetails");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {

			String sql = OrganizationConstants.UPDATE_GST_ORGANIZATION;

			preparedStatement = con.prepareStatement(sql);
			preparedStatement.setInt(1, organizationVo.getGstDetails().getGstRegnTypeId());
			preparedStatement.setString(2, organizationVo.getGstDetails().getGstNo());
			preparedStatement.setString(3, organizationVo.getGstDetails().getTaxPanId());
			preparedStatement.setString(4, organizationVo.getGstDetails().getAddress_1());
			preparedStatement.setString(5, organizationVo.getGstDetails().getAddress_2());
			preparedStatement.setString(6, organizationVo.getGstDetails().getCity());
			preparedStatement.setString(7, new Integer(organizationVo.getGstDetails().getState()).toString());
			preparedStatement.setString(8, new Integer(organizationVo.getGstDetails().getCountry()).toString());
			preparedStatement.setString(9, organizationVo.getGstDetails().getPinCode());
			preparedStatement.setBoolean(10, organizationVo.getGstDetails().getIsMultiGST());
			preparedStatement.setTimestamp(11, new Timestamp(System.currentTimeMillis()));
			preparedStatement.setInt(12, organizationVo.getId());
			preparedStatement.executeUpdate();
			closeResources(rs, preparedStatement, null);

			String sqlInsert = OrganizationConstants.INSERT_INTO_ORGANIZATION_GST_LOCATION;
			String sqlUpdate = OrganizationConstants.UPDATE_LOCATION_ORGANIZATION;

			if (organizationVo.getGstDetails().getLocation() != null) {
				for (int i = 0; i < organizationVo.getGstDetails().getLocation().size(); i++) {
					LocationVo locationVo = organizationVo.getGstDetails().getLocation().get(i);
					if (locationVo.getStatus().equals(CommonConstants.STATUS_AS_NEW)) {
						preparedStatement = con.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS);
						preparedStatement.setString(1, locationVo.getName());
						preparedStatement.setString(2, locationVo.getGstNo());
						preparedStatement.setString(3, locationVo.getTaxPanId());
						preparedStatement.setString(4, locationVo.getAddress_1());
						preparedStatement.setString(5, locationVo.getAddress_2());
						preparedStatement.setString(6, locationVo.getCity());
						preparedStatement.setString(7, new Integer(locationVo.getState()).toString());
						preparedStatement.setString(8, new Integer(locationVo.getCountry()).toString());
						preparedStatement.setString(9, locationVo.getPinCode());
						preparedStatement.setInt(10, organizationVo.getId());
						preparedStatement.setTimestamp(11, new Timestamp(System.currentTimeMillis()));
						int rowAffected = preparedStatement.executeUpdate();
						if (rowAffected == 1) {
							rs = preparedStatement.getGeneratedKeys();
							if (rs.next()) {
								locationVo.setId(rs.getInt(1));
							}
						}
					} else {
						preparedStatement = con.prepareStatement(sqlUpdate);
						preparedStatement.setString(1, locationVo.getName());
						preparedStatement.setString(2, locationVo.getGstNo());
						preparedStatement.setString(3, locationVo.getTaxPanId());
						preparedStatement.setString(4, locationVo.getAddress_1());
						preparedStatement.setString(5, locationVo.getAddress_2());
						preparedStatement.setString(6, locationVo.getCity());
						preparedStatement.setString(7, new Integer(locationVo.getState()).toString());
						preparedStatement.setString(8, new Integer(locationVo.getCountry()).toString());
						preparedStatement.setString(9, locationVo.getPinCode());
						preparedStatement.setString(10, locationVo.getStatus());
						preparedStatement.setTimestamp(11, new Timestamp(System.currentTimeMillis()));
						preparedStatement.setInt(12, locationVo.getId());
						preparedStatement.executeUpdate();
					}

				}
			}
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, null);

		}
	}

	private void updateOrganizationProprietor(NewOrganizationVo organizationVo, Connection con)
			throws ApplicationException {
		logger.info("Entry into method:updateOrganizationProprietor");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			String sqlInsert = OrganizationConstants.INSERT_INTO_ORGANIZATION_PROPRIETOR;
			String sqlUpdate = OrganizationConstants.UPDATE_PROPRIETOR_ORGANIZATION;

			if (organizationVo.getKeyMembers().getOrganizationProprietor() != null) {
				for (int i = 0; i < organizationVo.getKeyMembers().getOrganizationProprietor().size(); i++) {
					OrganizationProprietorVo orgProprietorVo = organizationVo.getKeyMembers()
							.getOrganizationProprietor().get(i);
					if (orgProprietorVo.getStatus().equals(CommonConstants.STATUS_AS_NEW)) {
						preparedStatement = con.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS);
						preparedStatement.setString(1, orgProprietorVo.getName());
						preparedStatement.setString(2, orgProprietorVo.getEmailId());
						preparedStatement.setString(3, orgProprietorVo.getMobileNo());
						preparedStatement.setInt(4, organizationVo.getId());
						preparedStatement.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
						int rowAffected = preparedStatement.executeUpdate();
						if (rowAffected == 1) {
							rs = preparedStatement.getGeneratedKeys();
							if (rs.next()) {
								orgProprietorVo.setId(rs.getInt(1));
							}
						}
					} else {
						preparedStatement = con.prepareStatement(sqlUpdate);
						preparedStatement.setString(1, orgProprietorVo.getName());
						preparedStatement.setString(2, orgProprietorVo.getEmailId());
						preparedStatement.setString(3, orgProprietorVo.getMobileNo());
						preparedStatement.setString(4, orgProprietorVo.getStatus());
						preparedStatement.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
						preparedStatement.setInt(6, orgProprietorVo.getId());
						preparedStatement.executeUpdate();
					}

				}
			}

		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, null);

		}
	}

	private void updateOrganizationPartner(NewOrganizationVo organizationVo, Connection con)
			throws ApplicationException {
		logger.info("Entry into method:updateOrganizationPartner");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			String sqlInsert = OrganizationConstants.INSERT_INTO_ORGANIZATION_PARTNERSHIP_PARTNER;
			String sqlUpdate = OrganizationConstants.UPDATE_PARTNERSHIP_PARTNER_ORGANIZATION;

			if (organizationVo.getKeyMembers().getOrganizationPartnershipPartner() != null) {
				for (int i = 0; i < organizationVo.getKeyMembers().getOrganizationPartnershipPartner().size(); i++) {
					OrganizationPartnershipPartnerVo data = organizationVo.getKeyMembers()
							.getOrganizationPartnershipPartner().get(i);
					if (data.getStatus().equals(CommonConstants.STATUS_AS_NEW)) {
						preparedStatement = con.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS);
						preparedStatement.setString(1, data.getName());
						preparedStatement.setString(2, data.getEmailId());
						preparedStatement.setString(3, data.getMobileNo());
						preparedStatement.setString(4, data.getOwnershipPercentage());
						preparedStatement.setInt(5, organizationVo.getId());
						preparedStatement.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
						int rowAffected = preparedStatement.executeUpdate();
						if (rowAffected == 1) {
							rs = preparedStatement.getGeneratedKeys();
							if (rs.next()) {
								data.setId(rs.getInt(1));
							}
						}
					} else {
						preparedStatement = con.prepareStatement(sqlUpdate);
						preparedStatement.setString(1, data.getName());
						preparedStatement.setString(2, data.getEmailId());
						preparedStatement.setString(3, data.getMobileNo());
						preparedStatement.setString(4, data.getOwnershipPercentage());
						preparedStatement.setString(5, data.getStatus());
						preparedStatement.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
						preparedStatement.setInt(7, data.getId());
						preparedStatement.executeUpdate();
					}

				}

			}

		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, null);

		}
	}

	private void updateOrganizationPrivateLimited(NewOrganizationVo organizationVo, Connection con)
			throws ApplicationException {
		logger.info("Entry into method:updateOrganizationPrivateLimited");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {

			String sqlInsert = OrganizationConstants.INSERT_INTO_ORGANIZATION_PRIVATE_LIMITED_DIRECTOR;
			String sqlUpdate = OrganizationConstants.UPDATE_PRIVATE_DIRECTOR_ORGANIZATION;

			if (organizationVo.getKeyMembers().getOrganizationPrivateLimitedDirector() != null) {
				for (int i = 0; i < organizationVo.getKeyMembers().getOrganizationPrivateLimitedDirector()
						.size(); i++) {
					OrganizationPrivateLimitedDirectorVo data = organizationVo.getKeyMembers()
							.getOrganizationPrivateLimitedDirector().get(i);
					if (data.getStatus().equals(CommonConstants.STATUS_AS_NEW)) {
						preparedStatement = con.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS);
						preparedStatement.setString(1, data.getName());
						preparedStatement.setString(2, data.getEmailId());
						preparedStatement.setString(3, data.getMobileNo());
						preparedStatement.setString(4, data.getOwnershipPercentage());
						preparedStatement.setString(5, data.getDin());
						preparedStatement.setInt(6, organizationVo.getId());
						preparedStatement.setTimestamp(7, new Timestamp(System.currentTimeMillis()));
						int rowAffected = preparedStatement.executeUpdate();
						if (rowAffected == 1) {
							rs = preparedStatement.getGeneratedKeys();
							if (rs.next()) {
								data.setId(rs.getInt(1));
							}
						}
					} else {
						preparedStatement = con.prepareStatement(sqlUpdate);
						preparedStatement.setString(1, data.getName());
						preparedStatement.setString(2, data.getEmailId());
						preparedStatement.setString(3, data.getMobileNo());
						preparedStatement.setString(4, data.getOwnershipPercentage());
						preparedStatement.setString(5, data.getDin());
						preparedStatement.setString(6, data.getStatus());
						preparedStatement.setTimestamp(7, new Timestamp(System.currentTimeMillis()));
						preparedStatement.setInt(8, data.getId());
						preparedStatement.executeUpdate();
					}

				}
			}

		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, null);

		}
	}

	private void updateOrganizationOnePerson(NewOrganizationVo organizationVo, Connection con)
			throws ApplicationException {
		logger.info("Entry into method:updateOrganizationOnePerson");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {

			String sqlInsert = OrganizationConstants.INSERT_INTO_ORGANIZATION_ONE_PERSON_DIRECTOR;
			String sqlUpdate = OrganizationConstants.UPDATE_ONE_PERSON_DIRECTOR_ORGANIZATION;

			if (organizationVo.getKeyMembers().getOrganizationOnePersonDirector() != null) {
				for (int i = 0; i < organizationVo.getKeyMembers().getOrganizationOnePersonDirector().size(); i++) {
					OrganizationOnePersonDirectorVo data = organizationVo.getKeyMembers()
							.getOrganizationOnePersonDirector().get(i);
					if (data.getStatus().equals(CommonConstants.STATUS_AS_NEW)) {
						preparedStatement = con.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS);
						preparedStatement.setString(1, data.getName());
						preparedStatement.setString(2, data.getEmailId());
						preparedStatement.setString(3, data.getMobileNo());
						preparedStatement.setString(4, data.getDin());
						preparedStatement.setInt(5, organizationVo.getId());
						preparedStatement.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
						int rowAffected = preparedStatement.executeUpdate();
						if (rowAffected == 1) {
							rs = preparedStatement.getGeneratedKeys();
							if (rs.next()) {
								data.setId(rs.getInt(1));
							}
						}
					} else {
						preparedStatement = con.prepareStatement(sqlUpdate);
						preparedStatement.setString(1, data.getName());
						preparedStatement.setString(2, data.getEmailId());
						preparedStatement.setString(3, data.getMobileNo());
						preparedStatement.setString(4, data.getDin());
						preparedStatement.setString(5, data.getStatus());
						preparedStatement.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
						preparedStatement.setInt(7, data.getId());
						preparedStatement.executeUpdate();
					}

				}
			}

		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, null);

		}
	}

	private void updateOrganizationLimitedLiability(NewOrganizationVo organizationVo, Connection con)
			throws ApplicationException {
		logger.info("Entry into method:updateOrganizationLimitedLiability");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {

			String sqlInsert = OrganizationConstants.INSERT_INTO_ORGANIZATION_LIMITED_LIABILITY_PARTNER;
			String sqlUpdate = OrganizationConstants.UPDATE_LIMITED_LIABILITY_PARTNER_ORGANIZATION;

			if (organizationVo.getKeyMembers().getOrganizationLimitedLiabilityPartner() != null) {
				for (int i = 0; i < organizationVo.getKeyMembers().getOrganizationLimitedLiabilityPartner()
						.size(); i++) {
					OrganizationLimitedLiabilityPartnerVo data = organizationVo.getKeyMembers()
							.getOrganizationLimitedLiabilityPartner().get(i);
					if (data.getStatus().equals(CommonConstants.STATUS_AS_NEW)) {
						preparedStatement = con.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS);
						preparedStatement.setString(1, data.getName());
						preparedStatement.setString(2, data.getEmailId());
						preparedStatement.setString(3, data.getMobileNo());
						preparedStatement.setString(4, data.getOwnershipPercentage());
						preparedStatement.setInt(5, organizationVo.getId());
						preparedStatement.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
						int rowAffected = preparedStatement.executeUpdate();
						if (rowAffected == 1) {
							rs = preparedStatement.getGeneratedKeys();
							if (rs.next()) {
								data.setId(rs.getInt(1));
							}
						}
					} else {
						preparedStatement = con.prepareStatement(sqlUpdate);
						preparedStatement.setString(1, data.getName());
						preparedStatement.setString(2, data.getEmailId());
						preparedStatement.setString(3, data.getMobileNo());
						preparedStatement.setString(4, data.getOwnershipPercentage());
						preparedStatement.setString(5, data.getStatus());
						preparedStatement.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
						preparedStatement.setInt(7, data.getId());
						preparedStatement.executeUpdate();
					}

				}
			}

		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, null);

		}
	}

	private void updateOrganizationPublicLimited(NewOrganizationVo organizationVo, Connection con)
			throws ApplicationException {
		logger.info("Entry into method:updateOrganizationPublicLimited");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {

			String sqlInsert = OrganizationConstants.INSERT_INTO_ORGANIZATION_PUBLIC_LIMITED_DIRECTOR;
			String sqlUpdate = OrganizationConstants.UPDATE_PUBLIC_LIMITED_DIRECTOR_ORGANIZATION;

			if (organizationVo.getKeyMembers().getOrganizationPublicLimitedDirector() != null) {
				for (int i = 0; i < organizationVo.getKeyMembers().getOrganizationPublicLimitedDirector().size(); i++) {
					OrganizationPublicLimitedDirectorVo data = organizationVo.getKeyMembers()
							.getOrganizationPublicLimitedDirector().get(i);
					if (data.getStatus().equals(CommonConstants.STATUS_AS_NEW)) {
						preparedStatement = con.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS);
						preparedStatement.setString(1, data.getName());
						preparedStatement.setString(2, data.getEmailId());
						preparedStatement.setString(3, data.getMobileNo());
						preparedStatement.setString(4, data.getOwnershipPercentage());
						preparedStatement.setString(5, data.getDin());
						preparedStatement.setInt(6, organizationVo.getId());
						preparedStatement.setTimestamp(7, new Timestamp(System.currentTimeMillis()));
						int rowAffected = preparedStatement.executeUpdate();
						if (rowAffected == 1) {
							rs = preparedStatement.getGeneratedKeys();
							if (rs.next()) {
								data.setId(rs.getInt(1));
							}
						}
					} else {
						preparedStatement = con.prepareStatement(sqlUpdate);
						preparedStatement.setString(1, data.getName());
						preparedStatement.setString(2, data.getEmailId());
						preparedStatement.setString(3, data.getMobileNo());
						preparedStatement.setString(4, data.getOwnershipPercentage());
						preparedStatement.setString(5, data.getDin());
						preparedStatement.setString(6, data.getStatus());
						preparedStatement.setTimestamp(7, new Timestamp(System.currentTimeMillis()));
						preparedStatement.setInt(8, data.getId());
						preparedStatement.executeUpdate();
					}
				}
			}

		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, null);

		}
	}

	public List<UserTypeOrganizationVo> getAllOrganizationsByEmailAndPhone(String emailId, String phoneNo)
			throws ApplicationException {
		logger.info("Entry into method:method:getAllOrganizationsByEmailAndPhone");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;

		List<UserTypeOrganizationVo> data = new ArrayList<UserTypeOrganizationVo>();
		Map<Integer, UserTypeOrganizationVo> orgMap = new HashMap<Integer, UserTypeOrganizationVo>();
		try {
			con = getUserMgmConnection();
			String query = null;
			query = LoginAndRegistrationConstants.REGISTRATION_EMAIL;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setString(1, emailId);
			// preparedStatement.setString(2, "DEL");
			rs = preparedStatement.executeQuery();
			int id = 0;
			while (rs.next()) {
				id = rs.getInt(1);
				break;
			}
			closeResources(rs, preparedStatement, null);
			if (id > 0) {
				query = OrganizationConstants.GET_USER_ORGANIZATIONS;
				preparedStatement = con.prepareStatement(query);
				preparedStatement.setInt(1, id);
				preparedStatement.setString(2, "DEL");
				rs = preparedStatement.executeQuery();
				while (rs.next()) {
					UserTypeOrganizationVo userTypeOrgVo = getOrgData(con, rs);
					userTypeOrgVo.setSuperAdmin(true);
					data.add(userTypeOrgVo);
					orgMap.put(userTypeOrgVo.getId(), userTypeOrgVo);
				}
				closeResources(rs, preparedStatement, null);
			}

			query = UserConstants.GET_ORGANIZATIONS_ACTIVE_USER;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setString(1, emailId);
			preparedStatement.setString(2, "DEL");
			rs = preparedStatement.executeQuery();
			List<Integer> orgIds = new ArrayList<Integer>();
			while (rs.next()) {
				orgIds.add(rs.getInt(1));
			}
			closeResources(rs, preparedStatement, null);
			if (orgIds.size() > 0) {
				query = OrganizationConstants.GET_ORGANIZATION;
				for (int i = 0; i < orgIds.size(); i++) {
					Integer val = orgIds.get(i);
					preparedStatement = con.prepareStatement(query);
					preparedStatement.setInt(1, val);
					preparedStatement.setString(2, "DEL");
					rs = preparedStatement.executeQuery();
					while (rs.next()) {
						UserTypeOrganizationVo userTypeOrgVo = getOrgData(con, rs);
						userTypeOrgVo.setUser(true);
						if (!(orgMap.containsKey(userTypeOrgVo.getId()))) {
							data.add(userTypeOrgVo);
							orgMap.put(userTypeOrgVo.getId(), userTypeOrgVo);

						}
					}
				}
			}

			orgMap.clear();

		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);

		}

		return data;
	}

	private UserTypeOrganizationVo getOrgData(Connection con, ResultSet rs) throws ApplicationException {
		try {
			UserTypeOrganizationVo orgVo = new UserTypeOrganizationVo();
			orgVo.setId(rs.getInt(1));
			orgVo.setName(rs.getString(2));
			orgVo.setOrganizationConstitution(rs.getInt(3));
			orgVo.setOrganizationType(rs.getInt(4));
			orgVo.setOrganizationIndustry(rs.getInt(5));
			orgVo.setBaseCurrency(rs.getInt(6));
			orgVo.setFinancialYear(rs.getString(7));
			orgVo.setStatus(rs.getString(8));
			orgVo.setUserId(new Integer(rs.getInt(9)).toString());
			orgVo.setCreateTs(rs.getTimestamp(10));
			orgVo.setUpdateTs(rs.getTimestamp(11));
			orgVo.setOrganizationAddress(getOrganizationAddress(orgVo.getId(), con));
			if (orgVo.getOrganizationConstitution() == 1) {
				orgVo.setOrganizationProprietorOtherDetails(getProprietorOtherDetails(orgVo.getId(), con));
				orgVo.setOrganizationProprietor(proprietor(orgVo.getId(), con));

			} else if (orgVo.getOrganizationConstitution() == 2) {
				orgVo.setOrganizationPartnershipOtherDetails(getPartnershipOtherDetails(orgVo.getId(), con));
				orgVo.setOrganizationPartnershipPartner(partner(orgVo.getId(), con));

			} else if (orgVo.getOrganizationConstitution() == 3) {
				orgVo.setOrganizationPrivateLimitedOtherDetails(getPrivateLimitedOtherDetails(orgVo.getId(), con));
				orgVo.setOrganizationPrivateLimitedDirector(privateLimitedDirector(orgVo.getId(), con));

			} else if (orgVo.getOrganizationConstitution() == 4) {
				orgVo.setOrganizationOnePersonOtherDetails(getOnePersonOtherDetails(orgVo.getId(), con));
				orgVo.setOrganizationOnePersonDirector(onePersonDirector(orgVo.getId(), con));

			} else if (orgVo.getOrganizationConstitution() == 5) {
				orgVo.setOrganizationLimitedLiabilityOtherDetails(getLimitedLiabilityOtherDetails(orgVo.getId(), con));
				orgVo.setOrganizationLimitedLiabilityPartner(limitedLiabilityPartner(orgVo.getId(), con));

			} else if (orgVo.getOrganizationConstitution() == 6) {
				orgVo.setOrganizationPublicLimitedOtherDetails(getPublicLimitedOtherDetails(orgVo.getId(), con));
				orgVo.setOrganizationPublicLimitedDirector(publicLimitedDirector(orgVo.getId(), con));

			}
			orgVo.setApplications(2);
			orgVo.setUsers(userDao.getUserCount(orgVo.getId(), con));
			return orgVo;
		} catch (Exception e) {
			throw new ApplicationException(e);
		}

	}

	public BasicGSTLocationDetailsVo getLocationGSTDetails(Connection con, int organizationId)
			throws ApplicationException {
		logger.info("Entry into method:method:getLocationGSTDetails");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		BasicGSTLocationDetailsVo basicGSTLocationDetailsVo = new BasicGSTLocationDetailsVo();
		List<BasicLocationDetailsVo> data = new ArrayList<BasicLocationDetailsVo>();
		try {
			String query = OrganizationConstants.GST_DETAILS;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, organizationId);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				BasicLocationDetailsVo basicLocationDetailsVo = new BasicLocationDetailsVo();
				int id = rs.getInt(1);
				rs.getInt(2);
				basicLocationDetailsVo.setId(id);
				basicLocationDetailsVo.setValue(id);
				basicLocationDetailsVo.setGstNo(rs.getString(3));
				basicLocationDetailsVo.setName("Registered Address");
				basicLocationDetailsVo.setRegistered(true);
				data.add(basicLocationDetailsVo);
			}
			closeResources(rs, preparedStatement, null);

			query = OrganizationConstants.GST_LOCATION;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, organizationId);
			preparedStatement.setString(2, "DEL");
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				BasicLocationDetailsVo basicLocationDetailsVo = new BasicLocationDetailsVo();
				int id = rs.getInt(1);
				basicLocationDetailsVo.setId(id);
				basicLocationDetailsVo.setValue(id);
				basicLocationDetailsVo.setName(rs.getString(2));
				basicLocationDetailsVo.setGstNo(rs.getString(3));
				basicLocationDetailsVo.setRegistered(false);
				data.add(basicLocationDetailsVo);
			}

		} catch (Exception e) {
			throw new ApplicationException(e);

		} finally {
			closeResources(rs, preparedStatement, null);
		}
		basicGSTLocationDetailsVo.setGstLocation(data);
		return basicGSTLocationDetailsVo;
	}

	public BasicLocationDetailsVo getLocationById(int orgId,int id, boolean isRegistered, String gstNumber)
			throws ApplicationException {
		logger.info("Entry into getLocationById "+id);
		BasicLocationDetailsVo basicLocationDetailsVo = null;
		PreparedStatement preparedStatement = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder query =new StringBuilder( OrganizationConstants.GST_LOCATION_WITH_ADDRESS_BY_ID_GSTN);
			if (isRegistered) {
				query = new StringBuilder(OrganizationConstants.GST_DETAILS_WITH_ADDRESS_BY_ID_GSTN);
			}
			if(gstNumber!=null && !gstNumber.equalsIgnoreCase("NA")) {//May be gst NA
				query.append("and gstlocdet.gst_no=?"); 
			}
			logger.info("Query:"+query.toString());
			con = getUserMgmConnection();
			preparedStatement = con.prepareStatement(query.toString());
			preparedStatement.setInt(1, orgId);
			preparedStatement.setInt(2, id);
			if(gstNumber!=null && !gstNumber.equalsIgnoreCase("NA")) {//May be gst NA
				preparedStatement.setString(3, gstNumber);
			}
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				basicLocationDetailsVo = new BasicLocationDetailsVo();
				String locationName = rs.getString(2);
				if (isRegistered) {
					locationName = "Registered Address";
					basicLocationDetailsVo.setRegistered(true);
				}
				basicLocationDetailsVo.setId(id);
				basicLocationDetailsVo.setValue(id);
				basicLocationDetailsVo.setGstNo(rs.getString(3));
				basicLocationDetailsVo.setName(locationName);
				basicLocationDetailsVo.setAddress_1(rs.getString(4));
				basicLocationDetailsVo.setAddress_2(rs.getString(5));
				basicLocationDetailsVo.setCity(rs.getString(6));
				basicLocationDetailsVo.setStateId(rs.getInt(7));
				basicLocationDetailsVo.setStateName(rs.getString(8));
				basicLocationDetailsVo.setCountryId(rs.getInt(9));
				basicLocationDetailsVo.setCountryName(rs.getString(10));
				basicLocationDetailsVo.setZipcode(rs.getString(11));

			}

			if (basicLocationDetailsVo == null && !isRegistered) {// IN a Worst case for legacy classes use
				logger.info("Not found in non registered address,checking match with registered address");
				basicLocationDetailsVo = getLocationById(orgId,id, true, gstNumber);
			}
			logger.info("Successfully fetched location:"+basicLocationDetailsVo);

		} catch (Exception e) {
			logger.error("Error in getLocationById", e);
			throw new ApplicationException(e);

		} finally {
			closeResources(rs, preparedStatement, con);
		}

		return basicLocationDetailsVo;
	}

	public BasicGSTLocationDetailsVo getLocationGSTDetailsWithAddress(Connection con, int organizationId)
			throws ApplicationException {
		logger.info("Entry into method:method:getLocationGSTDetails");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		BasicGSTLocationDetailsVo basicGSTLocationDetailsVo = new BasicGSTLocationDetailsVo();
		List<BasicLocationDetailsVo> data = new ArrayList<BasicLocationDetailsVo>();
		try {
			String query = OrganizationConstants.GST_DETAILS_WITH_ADDRESS;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, organizationId);
			preparedStatement.setInt(2, organizationId);
			String phoneNo = getOrganizationContactNo(organizationId);

			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				BasicLocationDetailsVo basicLocationDetailsVo = new BasicLocationDetailsVo();
				int id = rs.getInt(1);
				rs.getInt(2);
				basicLocationDetailsVo.setId(id);
				basicLocationDetailsVo.setValue(id);
				basicLocationDetailsVo.setGstNo(rs.getString(3));
				basicLocationDetailsVo.setName("Registered Address");
				basicLocationDetailsVo.setAddress_1(rs.getString(4));
				basicLocationDetailsVo.setAddress_2(rs.getString(5));
				basicLocationDetailsVo.setCity(rs.getString(6));
				basicLocationDetailsVo.setStateId(rs.getInt(7));
				basicLocationDetailsVo.setStateName(rs.getString(8));
				basicLocationDetailsVo.setCountryId(rs.getInt(9));
				basicLocationDetailsVo.setCountryName(rs.getString(10));
				basicLocationDetailsVo.setZipcode(rs.getString(11));
				basicLocationDetailsVo.setRegistered(true);
				basicLocationDetailsVo.setOrgname(rs.getString(12));
				basicLocationDetailsVo.setPhoneNo(phoneNo);
				data.add(basicLocationDetailsVo);
			}
			closeResources(rs, preparedStatement, null);

			query = OrganizationConstants.GST_LOCATION_WITH_ADDRESS;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, organizationId);
			preparedStatement.setInt(2, organizationId);
			preparedStatement.setString(3, "DEL");
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				BasicLocationDetailsVo basicLocationDetailsVo = new BasicLocationDetailsVo();
				int id = rs.getInt(1);
				basicLocationDetailsVo.setId(id);
				basicLocationDetailsVo.setValue(id);
				basicLocationDetailsVo.setName(rs.getString(2));
				basicLocationDetailsVo.setGstNo(rs.getString(3));
				basicLocationDetailsVo.setAddress_1(rs.getString(4));
				basicLocationDetailsVo.setAddress_2(rs.getString(5));
				basicLocationDetailsVo.setCity(rs.getString(6));
				basicLocationDetailsVo.setStateId(rs.getInt(7));
				basicLocationDetailsVo.setStateName(rs.getString(8));
				basicLocationDetailsVo.setCountryId(rs.getInt(9));
				basicLocationDetailsVo.setCountryName(rs.getString(10));
				basicLocationDetailsVo.setZipcode(rs.getString(11));
				basicLocationDetailsVo.setRegistered(false);
				basicLocationDetailsVo.setOrgname(rs.getString(12));
				basicLocationDetailsVo.setPhoneNo(phoneNo);
				data.add(basicLocationDetailsVo);
			}

		} catch (Exception e) {
			throw new ApplicationException(e);

		} finally {
			closeResources(rs, preparedStatement, null);
		}
		basicGSTLocationDetailsVo.setGstLocation(data);
		return basicGSTLocationDetailsVo;
	}

	public int getOrganizationBaseCurrency(int organizationId) throws ApplicationException {
		logger.info("Entry into getOrganizationBaseCurrency");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		int baseCurrencyId = 0;
		Connection con = null;
		try {
			con = getUserMgmConnection();
			String query = OrganizationConstants.GET_BASE_CURRENCY_ORGANIZATION;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, organizationId);
			preparedStatement.setString(2, "DEL");
			rs = preparedStatement.executeQuery();

			while (rs.next()) {
				baseCurrencyId = rs.getInt(1);

			}
		} catch (Exception e) {
			throw new ApplicationException(e);

		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return baseCurrencyId;

	}

	public BasicVoucherEntriesVo getBasicVoucherEntries(int organizationId, Connection con, String type)
			throws ApplicationException {
		logger.info("Entry into getBasicVoucherEntries");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		BasicVoucherEntriesVo basicVoucherEntriesVo = new BasicVoucherEntriesVo();
		try {
			String query = OrganizationConstants.GET_BASE_VOUCHER_ENTRIES_ORGANIZATION;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, organizationId);
			preparedStatement.setString(2, "DEL");
			preparedStatement.setString(3, type);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				basicVoucherEntriesVo.setId(rs.getInt(1));
				basicVoucherEntriesVo.setPrefix(rs.getString(2));
				basicVoucherEntriesVo.setSuffix(rs.getString(3));
				basicVoucherEntriesVo.setMinimumDigits(rs.getString(4) != null ? rs.getString(4) : null);
			}
		} catch (Exception e) {
			throw new ApplicationException(e);

		} finally {
			closeResources(rs, preparedStatement, null);
		}
		return basicVoucherEntriesVo;

	}


	private int entityId(String name)throws ApplicationException{

		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = getFinanceCommon();
			String query = FinanceCommonConstants.GET_BASE_ENTITY_ID;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setString(1, name);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				return rs.getInt(1);
			}
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);

		}
		return 0;

	}

	@SuppressWarnings("rawtypes")
	private void insertIntoChartOfAccounts(Connection con, int organizationId, String userId)
			throws ApplicationException {
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Map<String, Integer> level1Map = new LinkedHashMap<String, Integer>();
		Map<String, Integer> level2Map = new LinkedHashMap<String, Integer>();
		Map<String, Integer> level3Map = new LinkedHashMap<String, Integer>();
		Map<String, Integer> level4Map = new LinkedHashMap<String, Integer>();
		Map<String, Integer> level5Map = new LinkedHashMap<String, Integer>();
		Map<String, String> data = financeDao.getLevel1ChartOfAccounts();
		try {
			String sql = ChartOfAccountsConstants.INSERT_INTO_CHART_OF_ACCOUNTS1_ORGANIZATION;
			preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			Iterator it = data.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry entry = (Map.Entry) it.next();
				String key = (String) entry.getKey();
				preparedStatement.setString(1, key);
				preparedStatement.setString(2, "General");
				preparedStatement.setInt(3, organizationId);
				preparedStatement.setInt(4, Integer.parseInt(userId));
				preparedStatement.setBoolean(5, true);
				preparedStatement.setBoolean(6, true);
				int rowAffected = preparedStatement.executeUpdate();
				if (rowAffected == 1) {
					rs = preparedStatement.getGeneratedKeys();
					if (rs.next()) {
						level1Map.put(key, rs.getInt(1));
					}
				}
			}

			closeResources(null, preparedStatement, null);
			data.clear();
			data = financeDao.getLevel2ChartOfAccounts();
			sql = ChartOfAccountsConstants.INSERT_INTO_CHART_OF_ACCOUNTS2_ORGANIZATION;
			preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			it = data.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry entry = (Map.Entry) it.next();
				String key = (String) entry.getKey();
				String value = (String) entry.getValue();
				preparedStatement.setString(1, key);
				if (level1Map.containsKey(value)) {
					preparedStatement.setInt(2, level1Map.get(value));
				}
				preparedStatement.setString(3, key);
				preparedStatement.setBoolean(4, true);
				preparedStatement.setInt(5, organizationId);
				preparedStatement.setInt(6, Integer.parseInt(userId));
				preparedStatement.setBoolean(7, true);
				int rowAffected = preparedStatement.executeUpdate();
				if (rowAffected == 1) {
					rs = preparedStatement.getGeneratedKeys();
					if (rs.next()) {
						level2Map.put(key, rs.getInt(1));
					}
				}
			}

			closeResources(null, preparedStatement, null);
			data.clear();
			data = financeDao.getLevel3ChartOfAccounts();
			sql = ChartOfAccountsConstants.INSERT_INTO_CHART_OF_ACCOUNTS3_ORGANIZATION;
			preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			it = data.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry entry = (Map.Entry) it.next();
				String key = (String) entry.getKey();
				String value = (String) entry.getValue();
				preparedStatement.setString(1, key);
				if (level2Map.containsKey(value)) {
					preparedStatement.setInt(2, level2Map.get(value));
				}
				preparedStatement.setString(3, key);
				preparedStatement.setBoolean(4, true);
				preparedStatement.setInt(5, organizationId);
				preparedStatement.setInt(6, Integer.parseInt(userId));
				preparedStatement.setBoolean(7, true);
				int rowAffected = preparedStatement.executeUpdate();
				if (rowAffected == 1) {
					rs = preparedStatement.getGeneratedKeys();
					if (rs.next()) {
						level3Map.put(key, rs.getInt(1));
					}
				}
			}

			closeResources(null, preparedStatement, null);
			data.clear();
			data = financeDao.getLevel4ChartOfAccounts();
			sql = ChartOfAccountsConstants.INSERT_INTO_CHART_OF_ACCOUNTS4_ORGANIZATION;
			preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			it = data.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry entry = (Map.Entry) it.next();
				String key = (String) entry.getKey();
				String level5MapKey = new String(key);
				key = key.substring(0, key.lastIndexOf("~@"));
				String value = (String) entry.getValue();
				preparedStatement.setString(1, key);
				if (level3Map.containsKey(value)) {
					preparedStatement.setInt(2, level3Map.get(value));
				}
				preparedStatement.setString(3, key);
				preparedStatement.setBoolean(4, true);
				preparedStatement.setInt(5, organizationId);
				preparedStatement.setInt(6, Integer.parseInt(userId));
				preparedStatement.setBoolean(7, true);
				int rowAffected = preparedStatement.executeUpdate();
				if (rowAffected == 1) {
					rs = preparedStatement.getGeneratedKeys();
					if (rs.next()) {
						level4Map.put(level5MapKey, rs.getInt(1));
					}
				}
			}

			closeResources(null, preparedStatement, null);
			data.clear();
			data = financeDao.getLevel5ChartOfAccounts();
			sql = ChartOfAccountsConstants.INSERT_INTO_CHART_OF_ACCOUNTS5_ORGANIZATION;
			preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			it = data.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry entry = (Map.Entry) it.next();
				String key = (String) entry.getKey();
				key = key.substring(0, key.lastIndexOf("~@"));
				String value = (String) entry.getValue();
				preparedStatement.setString(1, key);
				if (level4Map.containsKey(value)) {
					preparedStatement.setInt(2, level4Map.get(value));
				}
				preparedStatement.setString(3, key);
				preparedStatement.setBoolean(4, true);
				preparedStatement.setInt(5, organizationId);
				preparedStatement.setInt(6, Integer.parseInt(userId));
				preparedStatement.setBoolean(7, true);
				int entityId=entityId(key);
				preparedStatement.setInt(8, entityId);
				preparedStatement.setInt(9, 0);
				preparedStatement.setBoolean(10, false);
				/*if ((key.equals("Cash in Hand")) || (key.equals("Cash in Hand - Foreign currency"))
						|| (key.equals("Cheques, drafts on hand")) || (key.equals("Bank Accounts - Current"))
						|| (key.equals("Bank Accounts - Savings")) || (key.equals("Bank Accounts - EEFC"))
						|| (key.equals("Bank Accounts - Wallets")) || (key.equals("Fixed Deposit"))
						|| (key.equals("Recurring Deposit")) || (key.equals("Unpaid dividend accounts"))
						|| (key.equals("Unpaid matured deposits")) || (key.equals("Unpaid matured debentures"))
						|| (key.equals("Bank Account-Share allotment money pending refund"))
						|| (key.equals("Margin Money Deposit")) || (key.equals("Frozen Bank Accounts"))
						|| (key.equals("Bank OD A/c"))) {
					preparedStatement.setInt(8, 6);
					preparedStatement.setInt(9, 2);
					preparedStatement.setBoolean(10, true);

				} else if ((key.equals("Cash Credit from Bank - Secured")) || (key.equals("Credit Cards"))) {
					preparedStatement.setInt(8, 6);
					preparedStatement.setInt(9, 1);
					preparedStatement.setBoolean(10, true);
				} else if ((key.equals("TDS Payable")) || (key.equals("TCS Payable"))) {
					preparedStatement.setInt(8, 5);
					preparedStatement.setInt(9, 1);
					preparedStatement.setBoolean(10, true);
				} else if ((key.equals("GST Refund Receivable")) || (key.equals("Input GST"))
						|| (key.equals("GST Refund")) || (key.equals("NC - GST Input Credit"))
						|| (key.equals("NC - Provision for doubtful GST refund")) || (key.equals("GST Payable"))
						|| (key.equals("GST-RCM Payable")) || (key.equals("Output GST Control A/c"))
						|| (key.equals("Provision for doubtful GST refund")) || (key.equals("Tax Paid Expense"))) {
					preparedStatement.setInt(8, 4);
					preparedStatement.setInt(9, 1);
					preparedStatement.setBoolean(10, true);

				} else if ((key.equals("Loans and advances to employees"))
						|| (key.equals("NC - Advance to Employee"))) {
					preparedStatement.setInt(8, 3);
					preparedStatement.setInt(9, 5);
					preparedStatement.setBoolean(10, true);

				} else if ((key.equals("NC - Provision for advance to employees"))
						|| (key.equals("Provision for advance to employees"))) {
					preparedStatement.setInt(8, 3);
					preparedStatement.setInt(9, 1);
					preparedStatement.setBoolean(10, true);

				} else if ((key.equals("Payables to employees"))) {
					preparedStatement.setInt(8, 3);
					preparedStatement.setInt(9, 7);
					preparedStatement.setBoolean(10, true);

				} else if ((key.equals("Sundry Debtor - Secured >6M")) || (key.equals("Sundry Debtor - UnSecured >6M"))
						|| (key.equals("Sundry Debtor - Doubtful >6M")) || (key.equals("Sundry Debtor - Secured"))
						|| (key.equals("Sundry Debtor - UnSecured")) || (key.equals("Sundry Debtor - Doubtful"))
						|| (key.equals("Provision for Doubtful Debt")) || (key.equals("Sundry Debtors - JV"))
						|| (key.equals("Provision for Doubtful Debt - JV")) || (key.equals("Unbilled revenue"))) {
					preparedStatement.setInt(8, 1);
					preparedStatement.setInt(9, 3);
					preparedStatement.setBoolean(10, true);

				} else if ((key.equals("Long-term trade receivables"))
						|| (key.equals("NC - Loans and advances from related parties - Secured"))
						|| (key.equals("NC - Loans and advances from related parties - UnSecured"))
						|| (key.equals("Other NC loans and advances - Secured"))
						|| (key.equals("Other NC loans and advances - UnSecured"))
						|| (key.equals("NC -  Trade / security deposits received"))
						|| (key.equals("NC -  Advances from customers"))
						|| (key.equals("Other loans and advances -Secured"))
						|| (key.equals("Other loans and advances -unsecured"))) {
					preparedStatement.setInt(8, 1);
					preparedStatement.setInt(9, 1);
					preparedStatement.setBoolean(10, true);

				} else if ((key.equals("Loans and advances to related parties"))
						|| (key.equals("NC - Capital advances")) || (key.equals("NC - Advance to related party"))) {
					preparedStatement.setInt(8, 2);
					preparedStatement.setInt(9, 3);
					preparedStatement.setBoolean(10, true);

				} else if ((key.equals("Security deposits")) || (key.equals("Prepaid Expenses"))
						|| (key.equals("Advance to vendors")) || (key.equals("NC- Rental Deposit"))
						|| (key.equals("NC - Electricity Deposit")) || (key.equals("NC - Other loans and advances"))
						|| (key.equals("NC - Advance to vendors")) || (key.equals("NC - Deposits - Secured"))
						|| (key.equals("NC - Deposits - UnSecured"))
						|| (key.equals("NC -  Payables on purchase of fixed assets"))
						|| (key.equals("NC -  Contractually reimbursable expenses"))
						|| (key.equals("NC -  Interest accrued but not due on borrowings"))
						|| (key.equals("NC -  Interest accrued on trade payables"))) {
					preparedStatement.setInt(8, 2);
					preparedStatement.setInt(9, 1);
					preparedStatement.setBoolean(10, true);

				} else if ((key.equals("NC -  Acceptances")) || (key.equals("NC -  Other than Acceptances"))
						|| (key.equals("Sundry Creditors - Foreign currency"))
						|| (key.equals("Sundry Creditors for capital goods  - Foreign currency"))
						|| (key.equals("Sundry Creditors - Local currency"))
						|| (key.equals("Sundry Creditors for capex - Local currency"))
						|| (key.equals("Accrued Expenses - TDS deducted"))
						|| (key.equals("Accrued Expenses - Vendor"))) {
					preparedStatement.setInt(8, 2);
					preparedStatement.setInt(9, 7);
					preparedStatement.setBoolean(10, true);

				} else {
					preparedStatement.setInt(8, 7);
					preparedStatement.setInt(9, 1);
					preparedStatement.setBoolean(10, false);

				}*/
				preparedStatement.executeUpdate();
			}

		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, null);

		}
		level1Map.clear();
		level2Map.clear();
		level3Map.clear();
		level4Map.clear();
		level5Map.clear();
		data.clear();

	}

	public Integer getLocationIdFromOrgId(Integer orgId) throws ApplicationException {
		logger.info("Entry into method:getLocationIdFromOrgId");
		Integer locationId = null;
		Connection con =null;
		PreparedStatement preparedStatement =null;
		ResultSet rs =null;
		try  {
			con = getUserMgmConnection();
			preparedStatement = con.prepareCall(SettingsAndPreferencesConstants.GET_LOCATION_ID_FROM_ORG_ID_ORGANIZATION);
			preparedStatement.setInt(1, orgId);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				locationId = rs.getInt(1);
			}
		} catch (Exception e) {
			logger.info("Error  into method:getLocationIdFromOrgId", e);
			throw new ApplicationException(e);
		}finally{
			closeResources(rs, preparedStatement, con);
		}
		return locationId;

	}

	public Integer getLocationIdFromGstNumber(String gstNumber, Integer orgId) throws ApplicationException {
		logger.info("Entry into method:getLocationIdFromGstNumber");
		Integer locationId = null;
		Connection con =null;
		PreparedStatement preparedStatement =null;
		ResultSet rs =null;
		try {
			con = getUserMgmConnection();
			preparedStatement = con.prepareCall(SettingsAndPreferencesConstants.GET_LOCATION_ID_FROM_GST_DETAILS_ORGANIZATION);
			preparedStatement.setInt(1, orgId);
			preparedStatement.setString(2, gstNumber);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				locationId = rs.getInt(1);
			}
			closeResources(rs, preparedStatement, con);
			if (locationId == null) {
				con= getUserMgmConnection();
				preparedStatement = con.prepareCall(SettingsAndPreferencesConstants.GET_LOCATION_ID_FROM_GST_LOCATION_ORGANIZATION);
				preparedStatement.setInt(1, orgId);
				preparedStatement.setString(2, gstNumber);
				rs = preparedStatement.executeQuery();
				while (rs.next()) {
					locationId = rs.getInt(1);
				}
			}
			closeResources(rs, preparedStatement, con);
		} catch (Exception e) {
			logger.info("Error  into method:getLocationIdFromGstNumber", e);
			throw new ApplicationException(e);
		}finally{
			closeResources(rs, preparedStatement, con);
		}
		return locationId;

	}

	// To get the default date format for Organization
	public String getDefaultDateForOrganization(Integer orgId, Connection con) throws ApplicationException {
		String dateFormat = null;
		logger.info("Entry into getDefaultDateForOrganization");
		PreparedStatement preparedStatement =null;
		ResultSet rs =null;
		try  {
			preparedStatement = con.prepareStatement(OrganizationConstants.GET_DEFAULT_DATE_FOR_ORGANIZATION);
			preparedStatement.setInt(1, orgId);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				dateFormat = rs.getString(1);
			}
		} catch (Exception e) {
			logger.info("Error in  method:getDefaultDateForOrganization", e);
			throw new ApplicationException(e);
		}finally{
			closeResources(rs, preparedStatement, null);
		}
		return dateFormat;

	}

	public String getLocationNameFromId(Integer id, Integer orgId) throws ApplicationException {
		logger.info("Entry into getLocationNameFromId");
		String locationName = null;
		Connection con =null;
		PreparedStatement preparedStatement =null;
		ResultSet rs =null;
		try  {
			con = getUserMgmConnection();
			preparedStatement = con.prepareCall(SettingsAndPreferencesConstants.GET_LOCATION_NAME_FROM_GST_LOCATION);
			preparedStatement.setInt(1, orgId);
			preparedStatement.setInt(2,id);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				locationName = rs.getString(1);
			}
		} catch (Exception e) {
			logger.info("Error  into getLocationNameFromId", e);
			throw new ApplicationException(e);
		}finally{
			closeResources(rs, preparedStatement, con);
		}
		return locationName;

	}


	public List<BasicOrganizationVo> getOrganizationsByEmailWithRoles(String email) throws ApplicationException {
		logger.info("Entry into method:getOrganizationsByEmailWithRoles :::"+email);
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		List<BasicOrganizationVo> data = new ArrayList<BasicOrganizationVo>();
		try {
			con = getUserMgmConnection();
			String query = OrganizationConstants.GET_BASIC_USER_ORGANIZATIONS_BY_EMAIL;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setString(1, email);
			preparedStatement.setString(2, "DEL");
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				BasicOrganizationVo orgVo = new BasicOrganizationVo();
				orgVo.setId(rs.getInt(1));
				orgVo.setName(rs.getString(2));
				orgVo.setStatus(rs.getString(3));
				orgVo.setCreateTs(rs.getTimestamp(4));
				orgVo.setDateFormat(rs.getString(5));
				orgVo.setNumberOfApplications(2);
				orgVo.setAccess(rs.getString(6));
				orgVo.setNumberOfUsers(userDao.getUserCount(orgVo.getId(), con));
				orgVo.setRoleName("Super Admin");
				orgVo.setKeyId(rs.getInt(7));
				orgVo.setKeyIdStatus("ACT");
				data.add(orgVo);
			}
			closeResources(rs, preparedStatement, con);
			con = getUserMgmConnection();
			query = OrganizationConstants.GET_BASIC_USER_ORG_BY_EMAIL_IN_KEY_CONTACTS;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setString(1, email);
			preparedStatement.setString(2, "DEL");
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				BasicOrganizationVo orgVo = new BasicOrganizationVo();
				orgVo.setId(rs.getInt(1));
				orgVo.setName(rs.getString(2));
				orgVo.setStatus(rs.getString(3));
				orgVo.setCreateTs(rs.getTimestamp(4));
				orgVo.setDateFormat(rs.getString(5));
				orgVo.setNumberOfApplications(2);
				orgVo.setNumberOfUsers(userDao.getUserCount(orgVo.getId(), con));
				orgVo.setRoleName("Vendor");
				orgVo.setKeyId(vendorDao.getVendorId(email, orgVo.getId()));				
				orgVo.setAccess(rs.getString(6));
				VendorVo vendor=vendorDao.getVendorByVendorId(orgVo.getKeyId());
				orgVo.setKeyIdStatus(vendor.getStatus());
				data.add(orgVo);
			}
			closeResources(rs, preparedStatement, con);
			con = getUserMgmConnection();
			query = OrganizationConstants.GET_BASIC_USER_ORG_BY_EMAIL_IN_USER;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setString(1, email);
			preparedStatement.setString(2, "DEL");
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				BasicOrganizationVo orgVo = new BasicOrganizationVo();
				orgVo.setId(rs.getInt(1));
				orgVo.setName(rs.getString(2));
				orgVo.setStatus(rs.getString(3));
				orgVo.setCreateTs(rs.getTimestamp(4));
				orgVo.setDateFormat(rs.getString(5));
				orgVo.setNumberOfApplications(2);
				orgVo.setNumberOfUsers(userDao.getUserCount(orgVo.getId(), con));				
				orgVo.setAccess(rs.getString(6));
				int roleId=rs.getInt(7);
				RoleVo roleVo=roleDao.getRoleDetails(orgVo.getId(), roleId);
				orgVo.setRoleName(roleVo.getName());
				orgVo.setKeyId(rs.getInt(8));
				orgVo.setKeyIdStatus(rs.getString(9));
				data.add(orgVo);
			}			
			closeResources(rs, preparedStatement, con);
			logger.info("Executed method:getOrganizationsByEmailWithRoles :::"+data.size());

		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);

		}
		/*if(data.size()==0){
			RegistrationVo registrationVo=registrationDao.getRegistrationByEmailFromRegistrationEntity(email);
			BasicOrganizationVo orgVo = new BasicOrganizationVo();
			orgVo.setAccess(registrationVo.getAccessData());
			orgVo.setRoleName("Super Admin");
			orgVo.setKeyId(registrationVo.getId());
			orgVo.setKeyIdStatus("ACT");
			data.add(orgVo);
		}
		 */
		if(data!=null && data.size()>0){
			int organizationId=0;
			try{
				con = getUserMgmConnection();
				preparedStatement = con.prepareStatement(OrganizationConstants.GET_DEFAULT_ORGANIZATION_USER);
				preparedStatement.setString(1, email);
				rs = preparedStatement.executeQuery();
				while (rs.next()) {
					organizationId=rs.getInt(1);
					break;
				}
			}catch(Exception e){
				logger.info(e.getMessage());

			}finally{
				closeResources(rs, preparedStatement, con);
			}

			if(organizationId==0){
				BasicOrganizationVo organizationVo=data.get(0);
				organizationVo.setDefault(true);
			}else{
				for(int i=0;i<data.size();i++){
					BasicOrganizationVo organizationVo=data.get(i);
					if(organizationVo.getId()==organizationId){
						organizationVo.setDefault(true);
						break;
					}
				}				
			}
		}
		return data;
	}


	public String getOrganizationName(Integer orgId) throws ApplicationException {
		logger.info("Entry into getOrganizationName");
		String orgName = null;
		Connection con = null;
		PreparedStatement preparedStatement =null;
		ResultSet rs =null;
		try  {
			con = getUserMgmConnection();
			preparedStatement = con.prepareStatement(OrganizationConstants.GET_ORGANIZATION_NAME);
			preparedStatement.setInt(1, orgId);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				orgName = rs.getString(1);
			}
		} catch (Exception e) {
			logger.info("Error in  getOrganizationName:", e);
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return orgName;
	}

	

	public String getOrganizationContactNo(Integer orgId) throws ApplicationException {
		logger.info("Entry into getOrganizationName");
		String orgContactNo = null;
		Connection con = null;
		PreparedStatement preparedStatement =null;
		ResultSet rs =null;
		try  {
			con = getUserMgmConnection();
			preparedStatement = con.prepareStatement(OrganizationConstants.GET_ORGANIZATION_CONTACT_NO);
			preparedStatement.setInt(1, orgId);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				orgContactNo = rs.getString(1);
			}
		} catch (Exception e) {
			logger.info("Error in  orgContactNo:", e);
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return orgContactNo;
	}

	

	public Integer getGSTDetails(Integer orgId) throws ApplicationException {
		logger.info("Enter into Method: getAccountingEntryLocationId");
		Integer locationId = null;
		Connection con =null;
		PreparedStatement preparedStatement =null;
		ResultSet rs =null;
		try {
			con = getUserMgmConnection();
			preparedStatement = con.prepareStatement(OrganizationConstants.GET_LOCATION_ID);
			preparedStatement.setInt(1, orgId);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				locationId = rs.getInt(1);
			}
		} catch (Exception e) {
			logger.info("Error in  getAccountingEntryTypeId:", e);
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return locationId;

	}

	public Integer getGstLocationId(String location, Integer orgId) throws ApplicationException {
		logger.info("Enter into Method: getGstLocationId");
		Integer locationId = null;
		Connection con =null;
		PreparedStatement preparedStatement =null;
		ResultSet rs = null;
		try {
			con = getUserMgmConnection();
			preparedStatement = con.prepareStatement(OrganizationConstants.GET_GST_LOCATION_ID);
			preparedStatement.setInt(1, orgId);
			preparedStatement.setString(2, location);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				locationId = rs.getInt(1);
			}
		} catch (Exception e) {
			logger.info("Error in  getAccountingEntryTypeId:", e);
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return locationId;
	}


	private Map<String,Boolean> getWorkflowConfiguration(){
		Map<String,Boolean> data=new HashMap<String,Boolean>();
		data.put("AP-Invoice", true);
		data.put("AR-Invoice", true);
		data.put("Payments", true);
		data.put("Purchase Order", true);
		data.put("AR-Credit Note", true);
		data.put("Payroll", true);
		data.put("AR-Receipts", false);
		data.put("AR-Refund", false);
		data.put("AR-LUT", false);
		data.put("AR-Application of Fund", false);
		data.put("Contra", false);
		data.put("Reconciliation-GL", false);
		data.put("Vendor Portal-Balance Confirmation", false);
		data.put("Accounting Entries", true);
		data.put("Reconciliation", false);
		data.put("Employee Reimbursement", false);		
		data.put("Expenses-Non Vendor", false);
		data.put("Reconciliation-Bank Statement", false);
		data.put("Banking", true);	
		return data;

	}


	private Map<String,Boolean> getJournalEntriesConfiguration(){
		Map<String,Boolean> data=new HashMap<String,Boolean>();

		data.put("AR-Invoice", true);
		data.put("AR-Receipts", true);
		data.put("AR-Refund", true);
		data.put("AR-Application of Fund", true);
		data.put("AR-Credit Note", true);		
		data.put("Payments", true);
		data.put("AP-Invoice", true);		
		data.put("Payroll", true);		
		data.put("Contra", true);		
		data.put("Accounting Entries", true);		
		data.put("Expenses-Non Vendor", true);
		return data;

	}

	private void insertIntoProductCategory(int organizationId,String userId,Connection con)throws ApplicationException{
		PreparedStatement preparedStatement = null;
		try
		{
			String sql = SettingsAndPreferencesConstants.INSERT_INTO_PRODUCT_CATEGORY;
			preparedStatement = con.prepareStatement(sql);
			preparedStatement.setString(1, "UnCategorized");
			preparedStatement.setString(2, "goods");
			preparedStatement.setInt(3, Integer.parseInt(userId));
			preparedStatement.setInt(4, organizationId);		
			preparedStatement.setString(5, "Super Admin");
			preparedStatement.executeUpdate();			
			closeResources(null, preparedStatement, null);

			sql = SettingsAndPreferencesConstants.INSERT_INTO_PRODUCT_CATEGORY;
			preparedStatement = con.prepareStatement(sql);
			preparedStatement.setString(1, "UnCategorized");
			preparedStatement.setString(2, "services");
			preparedStatement.setInt(3, Integer.parseInt(userId));
			preparedStatement.setInt(4, organizationId);		
			preparedStatement.setString(5, "Super Admin");
			preparedStatement.executeUpdate();			
			closeResources(null, preparedStatement, null);

			sql = SettingsAndPreferencesConstants.INSERT_INTO_PRODUCT_CATEGORY;
			preparedStatement = con.prepareStatement(sql);
			preparedStatement.setString(1, "UnCategorized");
			preparedStatement.setString(2, "fixedAsset");
			preparedStatement.setInt(3, Integer.parseInt(userId));
			preparedStatement.setInt(4, organizationId);		
			preparedStatement.setString(5, "Super Admin");
			preparedStatement.executeUpdate();
			closeResources(null, preparedStatement, null);

		}catch(Exception e){
			throw new ApplicationException(e);
		}finally{
			closeResources(null, preparedStatement, null);
		}

	}



	public IncomeTaxLoginVo getIncomeTaxLoginDetails(int organizationId) throws ApplicationException {
		logger.info("Entry into method:getIncomeTaxLoginDetails");
		PreparedStatement preparedStatement = null;
		Connection con=null;
		ResultSet rs = null;
		IncomeTaxLoginVo data=new IncomeTaxLoginVo();
		try{
			con = getUserMgmConnection();
			String query = OrganizationConstants.FETCH_PAN_DETAILS_ORGANIZATION;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, organizationId);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				data.setPan(rs.getString(1));
			}
			closeResources(rs, preparedStatement, con);

			con = getUserMgmConnection();
			query = OrganizationConstants.FETCH_INCOME_TAX_LOGIN;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, organizationId);
			preparedStatement.setString(2, data.getPan());
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				data.setId(rs.getInt(1));
				data.setPassword(rs.getString(2));
				data.setRememberMe(rs.getBoolean(3));
			}
			closeResources(rs, preparedStatement, con);



		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);

		}
		return data;
	}
}

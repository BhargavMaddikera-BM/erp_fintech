package com.blackstrawai.keycontact;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.ap.dropdowns.PaymentTypeVo;
import com.blackstrawai.chartofaccounts.ChartOfAccountsDao;
import com.blackstrawai.common.BaseDao;
import com.blackstrawai.common.BookKeepingSettingsVo;
import com.blackstrawai.common.CommonConstants;
import com.blackstrawai.keycontact.statutorybody.StatutoryBodyVo;
import com.blackstrawai.settings.SettingsAndPreferencesConstants;

@Repository
public class StatutoryBodyDao extends BaseDao {
	
	@Autowired
	private ChartOfAccountsDao chartOfAccountsDao;

	private Logger logger = Logger.getLogger(StatutoryBodyDao.class);

	public StatutoryBodyVo createStatutoryBody(StatutoryBodyVo statutoryBodyVo) throws ApplicationException {
		logger.info("Entry into method: createStatutoryBody");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		
		if(statutoryBodyVo.getBookKeepingSettings()==null){
			BookKeepingSettingsVo bookKeepingSettings=new BookKeepingSettingsVo();
			bookKeepingSettings.setDefaultGlName("PF Payable");
			bookKeepingSettings.setId(chartOfAccountsDao.getLedgerIdGivenName("PF Payable", statutoryBodyVo.getOrganizationId()));
			statutoryBodyVo.setBookKeepingSettings(bookKeepingSettings);
		}
		
		
		try {
			con = getUserMgmConnection();		   
			boolean isStatutoryBodyExist=checkStatutoryBodyExist(statutoryBodyVo.getStatutoryName(),statutoryBodyVo.getOrganizationId(),con);
			if(isStatutoryBodyExist){
				throw new Exception("Statutory Body Exist for the Organization");
			}
			String sql = SettingsAndPreferencesConstants.INSERT_STATUTORY_BODY;
			preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setString(1, statutoryBodyVo.getStatutoryName());
			preparedStatement.setString(2, statutoryBodyVo.getDepartmentName());
			preparedStatement.setString(3, statutoryBodyVo.getType());
			preparedStatement.setString(4, statutoryBodyVo.getRegistrationNo());
			preparedStatement.setString(5, statutoryBodyVo.getDate());
			preparedStatement.setInt(6, statutoryBodyVo.getState());
			preparedStatement.setString(7, statutoryBodyVo.getCity());
			preparedStatement.setString(8, statutoryBodyVo.getAddr1());
			preparedStatement.setString(9, statutoryBodyVo.getAddr2());
			preparedStatement.setString(10, statutoryBodyVo.getPincode());
			preparedStatement.setString(11, statutoryBodyVo.getWebsite());
			preparedStatement.setString(12, statutoryBodyVo.getStatus());
			preparedStatement.setInt(13, statutoryBodyVo.getOrganizationId());
			preparedStatement.setString(14, statutoryBodyVo.getUserId());
			preparedStatement.setString(15, statutoryBodyVo.getRoleName());
			preparedStatement.setInt(16, statutoryBodyVo.getBookKeepingSettings().getId());
			preparedStatement.setString(17, statutoryBodyVo.getBookKeepingSettings().getDefaultGlName());

			int rowAffected = preparedStatement.executeUpdate();
			if (rowAffected == 1) {
				rs = preparedStatement.getGeneratedKeys();
				if (rs.next()) {
					statutoryBodyVo.setId(rs.getInt(1));
				}
			}
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return statutoryBodyVo;
	}

	
	private boolean checkStatutoryBodyExist(String name, int organizationId, Connection con)
			throws ApplicationException {
		logger.info("Entry into method: checkStatutoryBodyExist");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			String query = SettingsAndPreferencesConstants.GET_STATUTORY_BODY_LIST_ORGANIZATION_NAME;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, organizationId);
			preparedStatement.setString(2, name);
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
	}
	
	
	public StatutoryBodyVo updateStatutoryBody(StatutoryBodyVo statutoryBodyVo) throws ApplicationException {
		logger.info("Entry into method: updateStatutoryBody");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = getUserMgmConnection();		
			
			if(statutoryBodyVo.getBookKeepingSettings()==null){
				BookKeepingSettingsVo bookKeepingSettings=new BookKeepingSettingsVo();
				bookKeepingSettings.setDefaultGlName("PF Payable");
				bookKeepingSettings.setId(chartOfAccountsDao.getLedgerIdGivenName("PF Payable", statutoryBodyVo.getOrganizationId()));
				statutoryBodyVo.setBookKeepingSettings(bookKeepingSettings);
			}
		  
			String sql = SettingsAndPreferencesConstants.UPDATE_STATUTORY_BODY;
			preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setString(1, statutoryBodyVo.getStatutoryName());
			preparedStatement.setString(2, statutoryBodyVo.getDepartmentName());
			preparedStatement.setString(3, statutoryBodyVo.getType());
			preparedStatement.setString(4, statutoryBodyVo.getRegistrationNo());
			preparedStatement.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
			preparedStatement.setInt(6, statutoryBodyVo.getState());
			preparedStatement.setString(7, statutoryBodyVo.getCity());
			preparedStatement.setString(8, statutoryBodyVo.getAddr1());
			preparedStatement.setString(9, statutoryBodyVo.getAddr2());
			preparedStatement.setString(10, statutoryBodyVo.getPincode());
			preparedStatement.setString(11, statutoryBodyVo.getWebsite());
			preparedStatement.setString(12, statutoryBodyVo.getStatus());
			preparedStatement.setString(13, statutoryBodyVo.getDate());
			preparedStatement.setInt(14, statutoryBodyVo.getUpdateUserId());
			preparedStatement.setString(15, statutoryBodyVo.getUpdateRoleName());
			preparedStatement.setInt(16, statutoryBodyVo.getBookKeepingSettings().getId());
			preparedStatement.setString(17,statutoryBodyVo.getBookKeepingSettings().getDefaultGlName());
			preparedStatement.setInt(18, statutoryBodyVo.getId());
			preparedStatement.executeUpdate();

		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return statutoryBodyVo;
	}

	public List<StatutoryBodyVo> getAllStatutoryBodiesOfAnOrganizationForUserAndRole(int organizationId,String userId,String roleName) throws ApplicationException {
		logger.info("Entry into method: getAllStatutoryBodiesOfAnOrganizationForUserAndRole");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		List<StatutoryBodyVo> listOfStatutoryBody = new ArrayList<StatutoryBodyVo>();
		try {
			con = getUserMgmConnection();
			String query="";			
			if(roleName.equals("Super Admin")){
				 query = SettingsAndPreferencesConstants.GET_STATUTORY_BODY_LIST_ORGANIZATION;
			}else{
				 query = SettingsAndPreferencesConstants.GET_STATUTORY_BODY_LIST_ORGANIZATION_USER_ROLE;
			}
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, organizationId);
			if(!(roleName.equals("Super Admin"))){
				preparedStatement.setString(2, userId);
				preparedStatement.setString(3, roleName);
			}	
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				StatutoryBodyVo statutoryBodyVo = new StatutoryBodyVo();
				statutoryBodyVo.setId(rs.getInt(1));
				statutoryBodyVo.setName(rs.getString(2));
				statutoryBodyVo.setDepartmentName(rs.getString(3));
				statutoryBodyVo.setType(rs.getString(4));
				statutoryBodyVo.setRegistrationNo(rs.getString(5));
				statutoryBodyVo.setDate(rs.getString(6));
				statutoryBodyVo.setState(rs.getInt(7));
				statutoryBodyVo.setCity(rs.getString(8));
				statutoryBodyVo.setAddr1(rs.getString(9));
				statutoryBodyVo.setAddr2(rs.getString(10));
				statutoryBodyVo.setPincode(rs.getString(11));
				statutoryBodyVo.setWebsite(rs.getString(12));
				statutoryBodyVo.setStatus(rs.getString(13));
				statutoryBodyVo.setOrganizationId(rs.getInt(14));
				statutoryBodyVo.setUserId(rs.getString(15));
				statutoryBodyVo.setRoleName(rs.getString(16));
				statutoryBodyVo.setStatutoryName(statutoryBodyVo.getName());
				listOfStatutoryBody.add(statutoryBodyVo);
			}
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return listOfStatutoryBody;
	}

	public List<StatutoryBodyVo> getBasicStatutoryBodiesOfAnOrganization(int organizationId) throws ApplicationException {
		logger.info("Entry into method: getBasicStatutoryBodiesOfAnOrganization");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		List<StatutoryBodyVo> listOfStatutoryBody = new ArrayList<StatutoryBodyVo>();
		try {
			con = getUserMgmConnection();
			preparedStatement = con.prepareStatement(SettingsAndPreferencesConstants.GET_BASIC_STATUTORY_BODY_LIST_ORGANIZATION);
			preparedStatement.setInt(1, organizationId);
			preparedStatement.setString(2, CommonConstants.STATUS_AS_ACTIVE);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				StatutoryBodyVo statutoryBodyVo = new StatutoryBodyVo();
				statutoryBodyVo.setId(rs.getInt(1));
				statutoryBodyVo.setName(rs.getString(2));
				listOfStatutoryBody.add(statutoryBodyVo);
			}
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}	
		return listOfStatutoryBody;
	}
	public StatutoryBodyVo getStatutoryBodyById(int id) throws ApplicationException {
		logger.info("Entry into method: getStatutoryBodyById");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;

		StatutoryBodyVo statutoryBodyVo = new StatutoryBodyVo();
		try {
			con = getUserMgmConnection();
			String query = SettingsAndPreferencesConstants.GET_STATUTORY_BODY_BY_ID;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, id);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {

				statutoryBodyVo.setId(rs.getInt(1));
				statutoryBodyVo.setName(rs.getString(2));
				statutoryBodyVo.setDepartmentName(rs.getString(3));
				statutoryBodyVo.setType(rs.getString(4));
				statutoryBodyVo.setRegistrationNo(rs.getString(5));
				statutoryBodyVo.setDate(rs.getString(6));
				statutoryBodyVo.setState(rs.getInt(7));
				statutoryBodyVo.setCity(rs.getString(8));
				statutoryBodyVo.setAddr1(rs.getString(9));
				statutoryBodyVo.setAddr2(rs.getString(10));
				statutoryBodyVo.setPincode(rs.getString(11));
				statutoryBodyVo.setWebsite(rs.getString(12));
				statutoryBodyVo.setStatus(rs.getString(13));
				statutoryBodyVo.setOrganizationId(rs.getInt(14));
				statutoryBodyVo.setUserId(String.valueOf(rs.getInt(15)));
				statutoryBodyVo.setRoleName(rs.getString(16));
				statutoryBodyVo.setUpdateUserId(rs.getInt(17));
				statutoryBodyVo.setUpdateRoleName(rs.getString(18));				
				BookKeepingSettingsVo bookKeepingSettingsVo=new BookKeepingSettingsVo();
				bookKeepingSettingsVo.setId(rs.getInt(19));
				bookKeepingSettingsVo.setDefaultGlName(rs.getString(20));
				statutoryBodyVo.setBookKeepingSettings(bookKeepingSettingsVo);						
				statutoryBodyVo.setStatutoryName(statutoryBodyVo.getName());
			}
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return statutoryBodyVo;
	}

	public StatutoryBodyVo deleteStatutoryBody(int id,String userId,String roleName,String status) throws ApplicationException {
		logger.info("Entry into method: deleteStatutoryBody");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		StatutoryBodyVo statutoryBodyVo = new StatutoryBodyVo();
		try {
			con = getUserMgmConnection();					
			String sql = SettingsAndPreferencesConstants.ACTIVATE_DEACTIVATE_STATUTORY_BODY;
			preparedStatement = con.prepareStatement(sql);
			preparedStatement.setString(1, status);
			preparedStatement.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
			preparedStatement.setString(3, userId);
			preparedStatement.setString(4, roleName);
			preparedStatement.setInt(5, id);
			statutoryBodyVo.setId(id);
			preparedStatement.executeUpdate();
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return statutoryBodyVo;
	}

	public List<PaymentTypeVo> getActiveStatutoryBodiesOfAnOrganizationForUserAndRole(int organizationId,String userId,String roleName) throws ApplicationException {
		logger.info("Entry into method: getAllStatutoryBodiesOfAnOrganizationForUserAndRole");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		List<PaymentTypeVo> listOfStatutoryBody = new ArrayList<PaymentTypeVo>();
		try {
			con = getUserMgmConnection();
			String query="";			
			if(roleName.equals("Super Admin")){
				 query = SettingsAndPreferencesConstants.GET_ACTIVE_STATUTORY_BODY_LIST_ORGANIZATION;
			}else{
				 query = SettingsAndPreferencesConstants.GET_ACTIVE_STATUTORY_BODY_LIST_ORGANIZATION_USER_ROLE;
			}
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, organizationId);
			preparedStatement.setString(2, CommonConstants.STATUS_AS_ACTIVE);
			if(!(roleName.equals("Super Admin"))){
				preparedStatement.setString(3, userId);
				preparedStatement.setString(4, roleName);
			}	
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				PaymentTypeVo statutoryBodyVo = new PaymentTypeVo();
				statutoryBodyVo.setId(rs.getInt(1));
				statutoryBodyVo.setValue(rs.getInt(1));
				statutoryBodyVo.setName(rs.getString(2));
				listOfStatutoryBody.add(statutoryBodyVo);
			}
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return listOfStatutoryBody;
	}
}

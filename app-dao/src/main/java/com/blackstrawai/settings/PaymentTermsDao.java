package com.blackstrawai.settings;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.ap.dropdowns.BasicPaymentTermsVo;
import com.blackstrawai.common.BaseDao;

@Repository
public class PaymentTermsDao extends BaseDao {

	private Logger logger = Logger.getLogger(PaymentTermsDao.class);

	public PaymentTermsVo createPaymentTerms(PaymentTermsVo paymentTermsVo) throws ApplicationException {
		logger.info("Entry into method: createPaymentTerms");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = getUserMgmConnection();
			boolean isPaymentTermsExist=checkPaymentTermsExist(paymentTermsVo.getPaymentTermsName(),paymentTermsVo.getOrganizationId(),con);
			if(isPaymentTermsExist){
				throw new Exception("Payment Terms Exist for the Organization");
			}
			String sql = SettingsAndPreferencesConstants.INSERT_INTO_PAYMENT_TERMS_ORGANIZATION;
			preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setString(1, paymentTermsVo.getPaymentTermsName());
			preparedStatement.setString(2, paymentTermsVo.getDescription());
			preparedStatement.setString(3, paymentTermsVo.getBaseDate());
			preparedStatement.setInt(4, paymentTermsVo.getDaysLimit());
			String accountsTypes = String.join(",", paymentTermsVo.getAccountType());
			preparedStatement.setString(5, accountsTypes);
			preparedStatement.setInt(6, paymentTermsVo.getOrganizationId());
			preparedStatement.setInt(7, Integer.valueOf(paymentTermsVo.getUserId()));
			preparedStatement.setBoolean(8, paymentTermsVo.getIsSuperAdmin());
			preparedStatement.setBoolean(9, false);
			preparedStatement.setString(10, paymentTermsVo.getRoleName());
			int rowAffected = preparedStatement.executeUpdate();
			if (rowAffected == 1) {
				rs = preparedStatement.getGeneratedKeys();
				if (rs.next()) {
					paymentTermsVo.setId(rs.getInt(1));
				}
			}
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return paymentTermsVo;
	}

	public PaymentTermsVo updatePaymentTerms(PaymentTermsVo paymentTermsVo) throws ApplicationException {
		logger.info("Entry into method: updatePaymentTerms");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = getUserMgmConnection();
			String sql = SettingsAndPreferencesConstants.UPDATE_PAYMENT_TERMS_ORGANIZATION;
			preparedStatement = con.prepareStatement(sql);
			preparedStatement.setString(1, paymentTermsVo.getPaymentTermsName());
			preparedStatement.setString(2, paymentTermsVo.getDescription());
			preparedStatement.setString(3, paymentTermsVo.getBaseDate());
			preparedStatement.setInt(4, paymentTermsVo.getDaysLimit());
			String accountsTypes = String.join(",", paymentTermsVo.getAccountType());
			preparedStatement.setString(5, accountsTypes);
			preparedStatement.setInt(6, paymentTermsVo.getOrganizationId());
			preparedStatement.setBoolean(7, paymentTermsVo.getIsSuperAdmin());
			preparedStatement.setTimestamp(8, new Timestamp(System.currentTimeMillis()));
			preparedStatement.setString(9, paymentTermsVo.getStatus());
			preparedStatement.setString(10, paymentTermsVo.getUserId());
			preparedStatement.setString(11, paymentTermsVo.getRoleName());
			preparedStatement.setInt(12, paymentTermsVo.getId());
			preparedStatement.executeUpdate();
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return paymentTermsVo;
	}

	public PaymentTermsVo deletePaymentTerms(int id,String status,String userId,String roleName) throws ApplicationException {
		logger.info("Entry into method: deletePaymentTerms");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		PaymentTermsVo paymentTermsVo = new PaymentTermsVo();
		try {
			con = getUserMgmConnection();
			String sql = SettingsAndPreferencesConstants.DELETE_PAYMENT_TERMS_ORGANIZATION;
			preparedStatement = con.prepareStatement(sql);
			preparedStatement.setString(1, status);
			preparedStatement.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
			preparedStatement.setString(3, userId);
			preparedStatement.setString(4, roleName);
			preparedStatement.setInt(5, id);
			paymentTermsVo.setId(id);
			preparedStatement.executeUpdate();
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return paymentTermsVo;
	}

	public List<PaymentTermsVo> getAllPaymentTermsOfAnOrganizationForUserAndRole(int organizationId,String userId,String roleName) throws ApplicationException {
		logger.info("Entry into method: getAllPaymentTermsOfAnOrganizationForUserAndRole");
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		List<PaymentTermsVo> listPaymentTerms = new ArrayList<PaymentTermsVo>();
		try {
			con = getUserMgmConnection();
			String query="";
			if(roleName.equals("Super Admin")){
				 query = SettingsAndPreferencesConstants.GET_PAYMENT_TERMS_ORGANIZATION;
			}else{
				 query = SettingsAndPreferencesConstants.GET_PAYMENT_TERMS_BY_ORGANIZATION_USER_ROLE;
			}
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, organizationId);
			if(!(roleName.equals("Super Admin"))){
				preparedStatement.setString(2, userId);
				preparedStatement.setString(3, roleName);
			}	
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				PaymentTermsVo paymentTermsVo = new PaymentTermsVo();
				paymentTermsVo.setId(rs.getInt(1));
				paymentTermsVo.setPaymentTermsName(rs.getString(2));
				paymentTermsVo.setDescription(rs.getString(3));
				paymentTermsVo.setBaseDate(rs.getString(4));
				paymentTermsVo.setDaysLimit(rs.getInt(5));
				List<String> accountTypes = new ArrayList<String>();
				String type=rs.getString(6);
				if(type!=null) {
					String types[] = type.split(",");
					for (String str : types) {
						accountTypes.add(str);
					}
				}
				paymentTermsVo.setAccountType(accountTypes);
				paymentTermsVo.setOrganizationId(rs.getInt(7));
				paymentTermsVo.setUserId(rs.getString(8));
				paymentTermsVo.setIsSuperAdmin(rs.getBoolean(9));
				paymentTermsVo.setStatus(rs.getString(10));
				paymentTermsVo.setCreateTs(rs.getTimestamp(11));
				listPaymentTerms.add(paymentTermsVo);
			}
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return listPaymentTerms;
	}
	
/*	public List<PaymentTermsVo> getAllPaymentTermsOfAnOrganization(int organizationId) throws ApplicationException {
		logger.info("Entry into method: getAllPaymentTermsOfAnOrganization");
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		List<PaymentTermsVo> listPaymentTerms = new ArrayList<PaymentTermsVo>();
		try {
			con = getUserMgmConnection();
			String query = SettingsAndPreferencesConstants.GET_PAYMENT_TERMS_BY_ORGANIZATION;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, organizationId);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				PaymentTermsVo paymentTermsVo = new PaymentTermsVo();
				paymentTermsVo.setId(rs.getInt(1));
				paymentTermsVo.setPaymentTermsName(rs.getString(2));
				paymentTermsVo.setDescription(rs.getString(3));
				paymentTermsVo.setBaseDate(rs.getString(4));
				paymentTermsVo.setDaysLimit(rs.getInt(5));
				List<String> accountTypes = new ArrayList<String>();
				String type=rs.getString(6);
				if(type!=null) {
					String types[] = type.split(",");
					for (String str : types) {
						accountTypes.add(str);
					}
				}
				paymentTermsVo.setAccountType(accountTypes);
				paymentTermsVo.setOrganizationId(rs.getInt(7));
				paymentTermsVo.setUserId(rs.getString(8));
				paymentTermsVo.setIsSuperAdmin(rs.getBoolean(9));
				paymentTermsVo.setStatus(rs.getString(10));
				paymentTermsVo.setCreateTs(rs.getTimestamp(11));
				listPaymentTerms.add(paymentTermsVo);
			}
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return listPaymentTerms;
	}*/

	public PaymentTermsVo getPaymentTermsById(int id) throws ApplicationException {
		logger.info("Entry into method: getPaymentTermsById");
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		PaymentTermsVo paymentTermsVo = new PaymentTermsVo();
		try {
			con = getUserMgmConnection();
			String query = SettingsAndPreferencesConstants.GET_PAYMENT_TERMS_BY_ID_ORGANIZATION;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, id);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				paymentTermsVo.setId(rs.getInt(1));
				paymentTermsVo.setPaymentTermsName(rs.getString(2));
				paymentTermsVo.setDescription(rs.getString(3));
				paymentTermsVo.setBaseDate(rs.getString(4));
				paymentTermsVo.setDaysLimit(rs.getInt(5));
				List<String> accountTypes = new ArrayList<String>();
				String types[] = rs.getString(6).split(",");
				for (String str : types) {
					accountTypes.add(str);
				}
				paymentTermsVo.setAccountType(accountTypes);
				paymentTermsVo.setOrganizationId(rs.getInt(7));
				paymentTermsVo.setUserId(rs.getString(8));
				paymentTermsVo.setIsSuperAdmin(rs.getBoolean(9));
				paymentTermsVo.setStatus(rs.getString(10));
				paymentTermsVo.setCreateTs(rs.getTimestamp(11));
			}
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return paymentTermsVo;
	}


	private boolean checkPaymentTermsExist(String name, int organizationId, Connection con)
			throws ApplicationException {
		logger.info("Entry into method: checkPaymentTermsExist");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			String query = SettingsAndPreferencesConstants.CHECK_PAYMENT_TERMS_EXIST_ORGANIZATION;
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

	public List<BasicPaymentTermsVo> getBasicPaymentTerms(int organizationId,Connection con)throws ApplicationException{
		logger.info("Entry into method:getBasicPaymentTerms");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		List<BasicPaymentTermsVo>paymentTermsList=new ArrayList<BasicPaymentTermsVo>();
		try
		{
			String query=SettingsAndPreferencesConstants.GET_MINIMAL_PAYMENT_TERMS_ORGANIZATION;
			preparedStatement=con.prepareStatement(query);
			preparedStatement.setInt(1,organizationId);
			rs = preparedStatement.executeQuery();			
			while (rs.next()) {
				BasicPaymentTermsVo data=new BasicPaymentTermsVo();
				data.setId(rs.getInt(1));
				data.setName(rs.getString(2));
				data.setValue(rs.getInt(1));
				data.setDayLimit(rs.getInt(3));
				paymentTermsList.add(data);				
			}
		}catch(Exception e){

			throw new ApplicationException(e);
		}finally {
			closeResources(rs, preparedStatement, null);

		}	
		return paymentTermsList;

	}


	public List<BasicPaymentTermsVo> getBasicPaymentTermsWithCustomValue(int organizationId,Connection con)throws ApplicationException{
		logger.info("Entry into method:getBasicPaymentTerms");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		List<BasicPaymentTermsVo>paymentTermsList=new ArrayList<BasicPaymentTermsVo>();
		try
		{

			String query=SettingsAndPreferencesConstants.GET_MINIMAL_PAYMENT_TERMS_ORGANIZATION;
			preparedStatement=con.prepareStatement(query);
			preparedStatement.setInt(1,organizationId);
			rs = preparedStatement.executeQuery();	
			BasicPaymentTermsVo customData=new BasicPaymentTermsVo();
			customData.setId(0);
			customData.setName("Custom");
			customData.setValue(0);
			customData.setDayLimit(0);
			paymentTermsList.add(customData);
			while (rs.next()) {
				BasicPaymentTermsVo data=new BasicPaymentTermsVo();
				data.setId(rs.getInt(1));
				data.setName(rs.getString(2));
				data.setValue(rs.getInt(1));
				data.setDayLimit(rs.getInt(3));
				paymentTermsList.add(data);				
			}
		}catch(Exception e){

			throw new ApplicationException(e);
		}finally {
			closeResources(rs, preparedStatement, null);

		}	
		return paymentTermsList;

	}

	public String getPaymentTermName(int id,int organizationId) throws ApplicationException {
		logger.info("To get the getPaymentTermName");
		String paymentTermName = null;
		Connection con =null;
		PreparedStatement preparedStatement =null;
		ResultSet rs = null;
		try {
			con = getUserMgmConnection();
			preparedStatement = con.prepareStatement(SettingsAndPreferencesConstants.GET_PAYMENT_TERM_NAME);
			preparedStatement.setInt(1, id);
			preparedStatement.setInt(2, organizationId);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				paymentTermName = rs.getString(1);
			}
		} catch (Exception e) {
			logger.info("Error in getPaymentTermName", e);
			throw new ApplicationException(e);
		}finally {
			closeResources(rs, preparedStatement, con);

		}	
		return paymentTermName;
	}

}

package com.blackstrawai.payroll;

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
import com.blackstrawai.common.BaseDao;

@Repository
public class PayTypeDao extends BaseDao{
	
	

private Logger logger = Logger.getLogger(PayTypeDao.class);
	
	public PayTypeVo createPayType(PayTypeVo payTypeVo) throws ApplicationException {
		logger.info("Entry into method: createPayType");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = getUserMgmConnection();
			boolean isPayTypeExist=checkPayTypeExist(payTypeVo.getName(),payTypeVo.getOrganizationId(),con);
			if(isPayTypeExist){
				throw new Exception("Pay Type Exist for the Organization");
			}
			String sql = PayRollConstants.INSERT_INTO_PAY_TYPE_ORGANIZATION;
			preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);	
			preparedStatement.setString(1, payTypeVo.getName());
			preparedStatement.setString(2, payTypeVo.getDescription());
			preparedStatement.setInt(3, payTypeVo.getOrganizationId());
			preparedStatement.setInt(4, Integer.valueOf(payTypeVo.getUserId()));
			preparedStatement.setBoolean(5, payTypeVo.getIsSuperAdmin());
			preparedStatement.setString(6,payTypeVo.getParentName());
			preparedStatement.setBoolean(7, payTypeVo.getIsBase());
			preparedStatement.setString(8, payTypeVo.getDeditOrCredit());
			preparedStatement.setString(9, payTypeVo.getRoleName());
			int rowAffected = preparedStatement.executeUpdate();
			if (rowAffected == 1) {
				rs = preparedStatement.getGeneratedKeys();
				if (rs.next()) {
					payTypeVo.setId(rs.getInt(1));
				}
			}
		}catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return payTypeVo;
	}


	private boolean checkPayTypeExist(String name, int organizationId, Connection con) throws ApplicationException {
		logger.info("Entry into method: checkPayTypeExist");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			String query =PayRollConstants.CHECK_PAY_TYPE_EXIST_ORGANIZATION;
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
	
	
	
	
	public PayTypeVo getPayTypeById(int id) throws ApplicationException {
		logger.info("Entry into method: getPayTypeById");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		PayTypeVo data = new PayTypeVo();
		try {
			con = getUserMgmConnection();
			String query = PayRollConstants.GET_PAY_TYPE_ORGANIZATION;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, id);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				data.setId(rs.getInt(1));
				data.setName(rs.getString(2));
				data.setDescription(rs.getString(3));
				data.setParentName(rs.getString(4));
				data.setStatus(rs.getString(5));
				rs.getTimestamp(6);
				rs.getTimestamp(7);
				data.setUserId(rs.getString(8));				
				data.setIsSuperAdmin(rs.getBoolean(9));
				data.setOrganizationId(rs.getInt(10));
				data.setIsBase(rs.getBoolean(11));
				data.setDeditOrCredit(rs.getString(12));
				data.setParentId(getPayTypeByName(data.getOrganizationId(),data.getParentName()).getId());

			}
		}catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
			}
		return data;
	}
	
	public PayTypeVo getPayTypeByName(int organizationId,String  name) throws ApplicationException {
		logger.info("Entry into method: getPayTypeByName");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		PayTypeVo data = new PayTypeVo();
		try {
			con = getUserMgmConnection();
			String query = PayRollConstants.GET_PAY_TYPE_BY_NAME_ORGANIZATION;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, organizationId);
			preparedStatement.setString(2, name);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				data.setId(rs.getInt(1));
				data.setName(rs.getString(2));
				data.setDescription(rs.getString(3));
				data.setParentName(rs.getString(4));
				data.setStatus(rs.getString(5));
				rs.getTimestamp(6);
				rs.getTimestamp(7);
				data.setUserId(rs.getString(8));				
				data.setIsSuperAdmin(rs.getBoolean(9));
				data.setOrganizationId(rs.getInt(10));
			}
		}catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
			}
		return data;
	}
	
	public PayTypeVo deletePayType(int id,String status,String userId,String roleName) throws ApplicationException {
		logger.info("Entry into method: deletePayType");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		PayTypeVo data = new PayTypeVo();
		try {
			con = getUserMgmConnection();
			String sql = PayRollConstants.DELETE_PAY_TYPE_ORGANIZATION;
			preparedStatement = con.prepareStatement(sql);
			preparedStatement.setString(1, status);
			preparedStatement.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
			preparedStatement.setString(3, userId);
			preparedStatement.setString(4, roleName);
			preparedStatement.setInt(5, id);
			data.setId(id);
			preparedStatement.executeUpdate();
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return data;
	}
	
	
	public PayTypeVo updatePayType(PayTypeVo data) throws ApplicationException {
		logger.info("Entry into method: updatePayType");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con =getUserMgmConnection();
			String sql = PayRollConstants.UPDATE_PAY_TYPE_ORGANIZATION;
			preparedStatement = con.prepareStatement(sql);
			preparedStatement.setString(1, data.getName());
			preparedStatement.setString(2, data.getDescription());
			preparedStatement.setInt(3, data.getOrganizationId());
			preparedStatement.setBoolean(4, data.getIsSuperAdmin());
			preparedStatement.setInt(5, Integer.valueOf(data.getUserId()));
			preparedStatement.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
			preparedStatement.setString(7, "ACT");			
			preparedStatement.setString(8,data.getParentName());
			preparedStatement.setString(9, data.getRoleName());
			preparedStatement.setInt(10, data.getId());
			preparedStatement.executeUpdate();
		}catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
	return data;
	}
	
	public List<PayTypeVo> getAllPayTypesOfAnOrganization(int organizationId)throws ApplicationException{
		logger.info("Entry into method: getAllPayTypesOfAnOrganization");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con=null;
		List<PayTypeVo> data=new ArrayList<PayTypeVo>();
		try
		{
			con=getUserMgmConnection();
			String query=PayRollConstants.GET_ALL_PAY_TYPE_ORGANIZATION;
			preparedStatement=con.prepareStatement(query);
			preparedStatement.setInt(1,organizationId);
			rs = preparedStatement.executeQuery();			
			while (rs.next()) {
				PayTypeVo payTypeVo=new PayTypeVo();
				payTypeVo.setId(rs.getInt(1));
				payTypeVo.setName(rs.getString(2));
				payTypeVo.setDescription(rs.getString(3));
				payTypeVo.setParentName(rs.getString(4));
				payTypeVo.setStatus(rs.getString(5));	
				payTypeVo.setUserId(rs.getString(6));
				payTypeVo.setIsSuperAdmin(rs.getBoolean(7));
				payTypeVo.setOrganizationId(rs.getInt(8));
				payTypeVo.setIsBase(rs.getBoolean(9));
				payTypeVo.setDeditOrCredit(rs.getString(10));
				payTypeVo.setParentId(getPayTypeByName(organizationId,payTypeVo.getParentName()).getId());
			//	payTypeVo.setParentName(getPayTypeById(payTypeVo.getParentId()).getName());
				payTypeVo.setNoOfPayItems(getPayItemCountForGivenPayType(organizationId, payTypeVo.getId()));
				data.add(payTypeVo);				
			}
		}catch(Exception e){

			throw new ApplicationException(e);
		}finally {
			closeResources(rs, preparedStatement, con);
		}	
		return data;
	}
	
	public List<PayTypeVo> getAllPayTypesOfAnOrganizationForUserAndRole(int organizationId,String userId,String roleName)throws ApplicationException{
		logger.info("Entry into method: getAllPayTypesOfAnOrganizationForUserAndRole");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con=null;
		List<PayTypeVo> data=new ArrayList<PayTypeVo>();
		try
		{
			con=getUserMgmConnection();
			String query="";
			if(roleName.equals("Super Admin")){
				query=PayRollConstants.GET_ALL_PAY_TYPE_ORGANIZATION;
			}else{
				query=PayRollConstants.GET_ALL_PAY_TYPE_ORGANIZATION_USER_ROLE;
			}
			preparedStatement=con.prepareStatement(query);
			preparedStatement.setInt(1,organizationId);
			if(!(roleName.equals("Super Admin"))){
				preparedStatement.setString(2, userId);
				preparedStatement.setString(3, roleName);
			}		
			rs = preparedStatement.executeQuery();			
			while (rs.next()) {
				PayTypeVo payTypeVo=new PayTypeVo();
				payTypeVo.setId(rs.getInt(1));
				payTypeVo.setName(rs.getString(2));
				payTypeVo.setDescription(rs.getString(3));
				payTypeVo.setParentName(rs.getString(4));
				payTypeVo.setStatus(rs.getString(5));	
				payTypeVo.setUserId(rs.getString(6));
				payTypeVo.setIsSuperAdmin(rs.getBoolean(7));
				payTypeVo.setOrganizationId(rs.getInt(8));
				payTypeVo.setIsBase(rs.getBoolean(9));
				payTypeVo.setDeditOrCredit(rs.getString(10));
				payTypeVo.setParentId(getPayTypeByName(organizationId,payTypeVo.getParentName()).getId());
			//	payTypeVo.setParentName(getPayTypeById(payTypeVo.getParentId()).getName());
				payTypeVo.setNoOfPayItems(getPayItemCountForGivenPayType(organizationId, payTypeVo.getId()));
				data.add(payTypeVo);				
			}
		}catch(Exception e){

			throw new ApplicationException(e);
		}finally {
			closeResources(rs, preparedStatement, con);
		}	
		return data;
	}
	
	
	
	public List<BasicPayTypeVo> getBasicPayTypesOfAnOrganization(int organizationId)throws ApplicationException{
		logger.info("Entry into method: getBasicPayTypesOfAnOrganization");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con=null;
		List<BasicPayTypeVo> data=new ArrayList<BasicPayTypeVo>();
		try
		{
			con=getUserMgmConnection();
			String query=PayRollConstants.GET_BASIC_PAY_TYPE_ORGANIZATION;
			preparedStatement=con.prepareStatement(query);
			preparedStatement.setInt(1,organizationId);
			rs = preparedStatement.executeQuery();			
			while (rs.next()) {
				BasicPayTypeVo payTypeVo=new BasicPayTypeVo();
				payTypeVo.setId(rs.getInt(1));
				payTypeVo.setName(rs.getString(2));
				payTypeVo.setValue(payTypeVo.getId());
				data.add(payTypeVo);				
			}
		}catch(Exception e){

			throw new ApplicationException(e);
		}finally {
			closeResources(rs, preparedStatement, con);
		}	
		return data;
	}
	
	
	private int getPayItemCountForGivenPayType(int organizationId,int payTypeId) throws ApplicationException {
		logger.info("Entry into method: getPayItemCountForGivenPayType");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = getUserMgmConnection();
			String query = PayRollConstants.GET_COUNT_PAY_ITEM_GIVEN_PAY_TYPE;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, organizationId);
			preparedStatement.setInt(2, payTypeId);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				return rs.getInt(1);				
			}
		}catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
			}
		return 0;
	}
}

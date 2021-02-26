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
import com.blackstrawai.ap.dropdowns.BasicDepartmentVo;
import com.blackstrawai.common.BaseDao;

@Repository
public class DepartmentDao extends BaseDao{

private Logger logger = Logger.getLogger(DepartmentDao.class);
	
	public DepartmentVo createDepartment(DepartmentVo departmentVo) throws ApplicationException {
		logger.info("Entry into method: createDepartment");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = getUserMgmConnection();
			boolean isDepartmentExist=checkDepartmentExist(departmentVo.getName(),departmentVo.getOrganizationId(),con);
			if(isDepartmentExist){
				throw new Exception("Department Exist for the Organization");
			}
			String sql = SettingsAndPreferencesConstants.INSERT_INTO_DEPARTMENT_ORGANIZATION;
			preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);	
			preparedStatement.setString(1, departmentVo.getName()!=null ? departmentVo.getName():null);
			preparedStatement.setString(2, departmentVo.getDescription()!=null ? departmentVo.getDescription():null);
			preparedStatement.setInt(3, departmentVo.getOrganizationId()!=null ? departmentVo.getOrganizationId():null);
			preparedStatement.setInt(4, Integer.valueOf(departmentVo.getUserId()));
			preparedStatement.setBoolean(5, departmentVo.getIsSuperAdmin());
			preparedStatement.setString(6, departmentVo.getRoleName());
			int rowAffected = preparedStatement.executeUpdate();
			if (rowAffected == 1) {
				rs = preparedStatement.getGeneratedKeys();
				if (rs.next()) {
					departmentVo.setId(rs.getInt(1));
				}
			}
		}catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return departmentVo;
	}


	private boolean checkDepartmentExist(String name, int organizationId, Connection con) throws ApplicationException {
		logger.info("Entry into method: checkDepartmentExist");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			String query = SettingsAndPreferencesConstants.CHECK_DEPARTMENT_EXIST_ORGANIZATION;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setString(1, name);
			preparedStatement.setInt(2, organizationId);
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
	
	
	public List<DepartmentVo> getAllDepartmentsOfAnOrganization(int organizationId) throws ApplicationException{
		logger.info("Entry into method: getAllDepartmentsOfAnOrganization");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		List<DepartmentVo> listOfDepartment=new ArrayList<DepartmentVo>();
		try {
			con = getUserMgmConnection();
			String query = SettingsAndPreferencesConstants.GET_DEPARTMENT_BY_ORGANIZATION;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, organizationId);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				DepartmentVo departmentVo=new DepartmentVo();
				departmentVo.setId(rs.getInt(1));
				departmentVo.setName(rs.getString(2));
				departmentVo.setDescription(rs.getString(3));
				departmentVo.setOrganizationId(rs.getInt(4));
				departmentVo.setUserId(rs.getString(5));
				departmentVo.setIsSuperAdmin(rs.getBoolean(6));
				departmentVo.setStatus(rs.getString(7));
				departmentVo.setCreateTs(rs.getTimestamp(8));
				departmentVo.setUpdateTs(rs.getTimestamp(9));
				listOfDepartment.add(departmentVo);
			}
		}catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
			}
		return listOfDepartment;
	}
	
	
	public List<DepartmentVo> getAllDepartmentsOfAnOrganizationForUserAndRole(int organizationId,String userId,String roleName) throws ApplicationException{
		logger.info("Entry into method: getAllDepartmentsOfAnOrganizationForUserAndRole");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		List<DepartmentVo> listOfDepartment=new ArrayList<DepartmentVo>();
		try {
			con = getUserMgmConnection();
			String query="";
			if(roleName.equals("Super Admin")){
				query = SettingsAndPreferencesConstants.GET_DEPARTMENT_BY_ORGANIZATION;
			}else{
				query = SettingsAndPreferencesConstants.GET_DEPARTMENT_BY_ORGANIZATION_USER_ROLE;
			}
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, organizationId);
			if(!(roleName.equals("Super Admin"))){
				preparedStatement.setString(2, userId);
				preparedStatement.setString(3, roleName);
			}	
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				DepartmentVo departmentVo=new DepartmentVo();
				departmentVo.setId(rs.getInt(1));
				departmentVo.setName(rs.getString(2));
				departmentVo.setDescription(rs.getString(3));
				departmentVo.setOrganizationId(rs.getInt(4));
				departmentVo.setUserId(rs.getString(5));
				departmentVo.setIsSuperAdmin(rs.getBoolean(6));
				departmentVo.setStatus(rs.getString(7));
				departmentVo.setCreateTs(rs.getTimestamp(8));
				departmentVo.setUpdateTs(rs.getTimestamp(9));
				listOfDepartment.add(departmentVo);
			}
		}catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
			}
		return listOfDepartment;
	}
	
	
	public DepartmentVo getDepartmentById(int id) throws ApplicationException {
		logger.info("Entry into method: getDepartmentById");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		DepartmentVo departmentVo = new DepartmentVo();
		try {
			con = getUserMgmConnection();
			String query = SettingsAndPreferencesConstants.GET_DEPARTMENT_BY_ID_ORGANIZATION;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, id);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				departmentVo.setId(rs.getInt(1));
				departmentVo.setName(rs.getString(2));
				departmentVo.setDescription(rs.getString(3));
				departmentVo.setOrganizationId(rs.getInt(4));
				departmentVo.setUserId(rs.getString(5));
				departmentVo.setIsSuperAdmin(rs.getBoolean(6));
				departmentVo.setStatus(rs.getString(7));
				departmentVo.setCreateTs(rs.getTimestamp(8));
				departmentVo.setUpdateTs(rs.getTimestamp(9));
			}
		}catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
			}
		return departmentVo;
	}
	
	public DepartmentVo deleteDepartment(int id,String status,String userId,String roleName) throws ApplicationException {
		logger.info("Entry into method: deleteDepartment");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		DepartmentVo departmentVo = new DepartmentVo();
		try {
			con = getUserMgmConnection();
			String sql = SettingsAndPreferencesConstants.DELETE_DEPARTMENT_ORGANIZATION;
			preparedStatement = con.prepareStatement(sql);
			preparedStatement.setString(1, status);
			preparedStatement.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
			preparedStatement.setString(3, userId);
			preparedStatement.setString(4, roleName);
			preparedStatement.setInt(5, id);
			departmentVo.setId(id);
			preparedStatement.executeUpdate();
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return departmentVo;
	}
	
	
	public DepartmentVo updateDepartment(DepartmentVo departmentVo) throws ApplicationException {
		logger.info("Entry into method: updateDepartment");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = getUserMgmConnection();
			String sql = SettingsAndPreferencesConstants.UPDATE_DEPARTMENT_ORGANIZATION;
			preparedStatement = con.prepareStatement(sql);
			preparedStatement.setString(1, departmentVo.getName() !=null ? departmentVo.getName():null);
			preparedStatement.setString(2, departmentVo.getDescription() !=null ? departmentVo.getDescription():null);
			preparedStatement.setInt(3, departmentVo.getOrganizationId() !=null ? departmentVo.getOrganizationId():null);
			preparedStatement.setBoolean(4, departmentVo.getIsSuperAdmin());
			preparedStatement.setInt(5, Integer.valueOf(departmentVo.getUserId()));
			preparedStatement.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
			preparedStatement.setString(7, departmentVo.getStatus());
			preparedStatement.setString(8, departmentVo.getRoleName());
			preparedStatement.setInt(9, departmentVo.getId());
			preparedStatement.executeUpdate();
		}catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
	return departmentVo;
	}
	
	public List<BasicDepartmentVo> getBasicDepartment(int organizationId,Connection con)throws ApplicationException{
		logger.info("Entry into method: getBasicDepartment");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		List<BasicDepartmentVo> departmentList=new ArrayList<BasicDepartmentVo>();
		try
		{
			String query=SettingsAndPreferencesConstants.GET_DEPARTMENT_ORGANIZATION;
			preparedStatement=con.prepareStatement(query);
			preparedStatement.setInt(1,organizationId);
			rs = preparedStatement.executeQuery();			
			while (rs.next()) {
				BasicDepartmentVo data=new BasicDepartmentVo();
				data.setId(rs.getInt(1));
				data.setName(rs.getString(2));
				departmentList.add(data);				
			}
		}catch(Exception e){

			throw new ApplicationException(e);
		}finally {
			closeResources(rs, preparedStatement, null);
		}	
		return departmentList;
	}
	
	public String getDepartmentById(Integer id) throws ApplicationException {
		logger.info("Entry into method: getDepartmentType");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		String departmentType=null;
		try {
			con = getUserMgmConnection();
			String query = SettingsAndPreferencesConstants.GET_DEPARTMENT_TYPE_BY_ID;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, id);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				departmentType=rs.getString(1);
			}
		}catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return departmentType;
	}
}

package com.blackstrawai.settings;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.ap.dropdowns.TDSVo;
import com.blackstrawai.common.BaseDao;
import com.blackstrawai.common.FinanceCommonDao;

@Repository
public class TdsDao extends BaseDao{

	@Autowired
	FinanceCommonDao financeCommonDao;

	private Logger logger = Logger.getLogger(TdsDao.class);

	public TdsVo createTds(TdsVo tdsVo) throws ApplicationException {
		logger.info("Entry into method: createTds");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = getUserMgmConnection();
			boolean isTdsExist=checkTdsExist(tdsVo.getTdsName(),tdsVo.getOrganizationId(),con);
			if(isTdsExist){
				throw new Exception("Tds Exist for the Organization");
			}
			String sql = SettingsAndPreferencesConstants.INSERT_INTO_TDS_ORGANIZATION;
			preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

			preparedStatement.setString(1, tdsVo.getTdsName());
			preparedStatement.setString(2, tdsVo.getDescription());
			preparedStatement.setString(3, tdsVo.getApplicableFor());
			preparedStatement.setString(4, tdsVo.getTdsRatePercentage());
			preparedStatement.setString(5, tdsVo.getStatus());
			preparedStatement.setString(6, tdsVo.getTdsRateIdentifier());
			preparedStatement.setInt(7, tdsVo.getOrganizationId());
			preparedStatement.setInt(8,Integer.valueOf(tdsVo.getUserId()));
			preparedStatement.setString(9, tdsVo.getRoleName());

			int rowAffected = preparedStatement.executeUpdate();
			if (rowAffected == 1) {
				rs = preparedStatement.getGeneratedKeys();
				if (rs.next()) {
					tdsVo.setId(rs.getInt(1));
				}
			}
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return tdsVo;
	}



	private boolean checkTdsExist(String name, int organizationId, Connection con)
			throws ApplicationException {
		logger.info("Entry into method: checkTdsExist");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			String query = SettingsAndPreferencesConstants.CHECK_TDS_EXIST_ORGANIZATION;
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

	public TdsVo deleteTds(int id,String status,String userId,String roleName) throws ApplicationException {
		logger.info("Entry into method: deleteTds");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		TdsVo tdsVo = new TdsVo();
		try {
			con = getUserMgmConnection();
			String sql = SettingsAndPreferencesConstants.DELETE_TDS_ORGANIZATION;
			preparedStatement = con.prepareStatement(sql);
			preparedStatement.setString(1, status);
			preparedStatement.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
			preparedStatement.setString(3, userId);
			preparedStatement.setString(4, roleName);
			preparedStatement.setInt(5, id);
			tdsVo.setId(id);
			preparedStatement.executeUpdate();
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return tdsVo;
	}

	public TdsVo updateTds(TdsVo tdsVo) throws ApplicationException {
		logger.info("Entry into method: updateTds");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = getUserMgmConnection();
			String sql = SettingsAndPreferencesConstants.UPDATE_TDS_ORGANIZATION;
			preparedStatement = con.prepareStatement(sql);
			preparedStatement.setString(1, tdsVo.getTdsName());
			preparedStatement.setString(2, tdsVo.getDescription());
			preparedStatement.setString(3, tdsVo.getApplicableFor());
			preparedStatement.setString(4, tdsVo.getTdsRatePercentage());
			preparedStatement.setInt(5, tdsVo.getOrganizationId());
			preparedStatement.setString(6, tdsVo.getTdsRateIdentifier());
			preparedStatement.setTimestamp(7, new Timestamp(System.currentTimeMillis()));
			preparedStatement.setString(8, tdsVo.getStatus());
			preparedStatement.setString(9, tdsVo.getUserId());
			preparedStatement.setString(10, tdsVo.getRoleName());
			preparedStatement.setInt(11, tdsVo.getId());
			preparedStatement.executeUpdate();
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return tdsVo;
	}

	public TdsVo getTdsById(int id,Boolean isBase,int organizationId,String userId,String roleName) throws ApplicationException {
		logger.info("Entry into method: getTdsById");
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		TdsVo tdsVo = new TdsVo();
		String query = null;
		try {

			if(isBase.equals(true))
			{
				con = getFinanceCommon();
				query = SettingsAndPreferencesConstants.GET_FINANCE_COMMON_BY_ID_ORGANIZATION;	
			}
			else
			{
				con = getUserMgmConnection();
				query = SettingsAndPreferencesConstants.GET_TDS_BY_ID_ORGANIZATION;
			}
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, id);
			rs = preparedStatement.executeQuery();

			while (rs.next()) {

				tdsVo.setId(rs.getInt(1));
				tdsVo.setTdsName(rs.getString(2));
				tdsVo.setTdsRateIdentifier(rs.getString(3));
				tdsVo.setTdsRatePercentage(rs.getString(4));
				tdsVo.setDescription(rs.getString(5));
				tdsVo.setApplicableFor(rs.getString(6));
				tdsVo.setCreateTs(rs.getTimestamp(7));

				if(isBase.equals(true))
				{
					tdsVo.setStatus("ACT");
					tdsVo.setOrganizationId(organizationId);
					tdsVo.setUserId(userId);
					tdsVo.setRoleName(roleName);
					tdsVo.setBase(true);
				}
				else
				{
					tdsVo.setStatus(rs.getString(8));
					tdsVo.setOrganizationId(rs.getInt(9));
					tdsVo.setUserId(rs.getString(10));
					tdsVo.setRoleName(rs.getString(11));


				}

			}
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return tdsVo;
	}

	public List<TdsVo> getAllTdsOfAnOrganizationForUserAndRole(int organizationId,String userId,String roleName) throws ApplicationException {
		logger.info("Entry into method: getAllTdsOfAnOrganizationForUserAndRole");
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		List<TdsVo> finalTdsList = new ArrayList<TdsVo>();
		try {
			Map<String,TdsVo>tdsMap=new HashMap<String,TdsVo>();
			con = getUserMgmConnection();
			String query="";
			if(roleName.equals("Super Admin")){
				query = SettingsAndPreferencesConstants.GET_TDS_ORGANIZATION;
			}else{
				query = SettingsAndPreferencesConstants.GET_TDS_BY_ORGANIZATION_USER_ROLE;
			}
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, organizationId);
			if(!(roleName.equals("Super Admin"))){
				preparedStatement.setString(2, userId);
				preparedStatement.setString(3, roleName);
			}	
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				TdsVo tdsVo = new TdsVo();
				tdsVo.setId(rs.getInt(1));
				tdsVo.setTdsName(rs.getString(2));
				tdsVo.setDescription(rs.getString(3));
				tdsVo.setApplicableFor(rs.getString(4));
				tdsVo.setTdsRatePercentage(rs.getString(5));
				tdsVo.setStatus(rs.getString(6));
				tdsVo.setTdsRateIdentifier(rs.getString(7));
				tdsVo.setOrganizationId(rs.getInt(8));
				tdsVo.setUserId(rs.getString(9));
				tdsVo.setCreateTs(rs.getTimestamp(10));
				tdsMap.put(tdsVo.getTdsName(), tdsVo);
			}
			closeResources(rs, preparedStatement, con);
			con = getFinanceCommon();
			List<TDSVo> financeCommonTdsList=financeCommonDao.getFullTDS(con);
			closeResources(rs, preparedStatement, con);
			for(int i=0;i<financeCommonTdsList.size();i++){
				TDSVo financeCommonTDS=financeCommonTdsList.get(i);
				TdsVo data=new TdsVo();
				data.setId(financeCommonTDS.getId());
				data.setTdsName(financeCommonTDS.getName());
				data.setDescription(financeCommonTDS.getDescription());
				data.setApplicableFor(financeCommonTDS.getApplicableFor());
				data.setTdsRatePercentage(financeCommonTDS.getTdsRatePercentage());
				data.setStatus("ACT");
				data.setTdsRateIdentifier(financeCommonTDS.getTdsRateIdentifier());
				data.setOrganizationId(organizationId);
				data.setUserId(userId);
				data.setBase(true);
				finalTdsList.add(data);
			}
			@SuppressWarnings("rawtypes")
			Iterator it=tdsMap.entrySet().iterator();
			while(it.hasNext()){
				@SuppressWarnings("rawtypes")
				Map.Entry entry=(Map.Entry)it.next();
				@SuppressWarnings("unused")
				String key=(String) entry.getKey();
				TdsVo tdsVo=(TdsVo) entry.getValue();
				if(!(tdsVo.getTdsRatePercentage()!=null && tdsVo.getTdsRatePercentage().contains("%"))){
					tdsVo.setTdsRatePercentage(tdsVo.getTdsRatePercentage().concat("%"));
				}
				tdsVo.setBase(false);
				finalTdsList.add(tdsVo);
			}
			tdsMap.clear();

		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}

		return finalTdsList;
	}

}


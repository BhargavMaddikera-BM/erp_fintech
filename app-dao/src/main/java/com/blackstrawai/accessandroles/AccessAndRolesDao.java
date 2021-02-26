package com.blackstrawai.accessandroles;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.common.BaseDao;
import com.fasterxml.jackson.databind.ObjectMapper;

@Repository
public class AccessAndRolesDao extends BaseDao {

	private Logger logger = Logger.getLogger(AccessAndRolesDao.class);

	public String getAccess(int typeId) throws ApplicationException {
		logger.info("Entry into getAllAccess");
		String accessData;
		List<Level1AccessVo> listLevel1Access = new ArrayList<Level1AccessVo>();
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			con = getFinanceCommon();
			preparedStatement = con.prepareStatement(AccessAndRolesConstants.GET_LEVEL1_ACCESS);
			preparedStatement.setInt(1, typeId);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				Level1AccessVo level1Access = new Level1AccessVo();
				List<String> allColumns = new ArrayList<String>();
				level1Access.setId(rs.getInt(1));
				level1Access.setName(rs.getString(2));
				level1Access.setKey(rs.getString(3));
				level1Access.setHasChildren(rs.getBoolean(4));
				level1Access.setHasAccess(rs.getBoolean(5));
				String columns = rs.getString(6);
				String[] tableColumns = columns.split(",");
				for (String column : tableColumns) {
					allColumns.add(column);
				}
				level1Access.setTableColums(allColumns);

				List<Level2AccessVo> level2Access = getLevel2Access(level1Access.getId(),con);
				level1Access.setSubmodules(level2Access);
				listLevel1Access.add(level1Access);
			}

			ObjectMapper mapper = new ObjectMapper();
			accessData = mapper.writeValueAsString(listLevel1Access);

		} catch (Exception e) {
			logger.info("Error in  getAllAccess:", e);
			throw new ApplicationException(e);
		}finally{
			closeResources(rs, preparedStatement,con);
		}
		return accessData;
	}

	private List<Level2AccessVo> getLevel2Access(Integer id,Connection con) throws ApplicationException {
		logger.info("Entry into getLevel2Access");
		List<Level2AccessVo> listLevel2Access = new ArrayList<Level2AccessVo>();
		PreparedStatement preparedStatement = null;
		ResultSet rs =null;
		try
		{
			preparedStatement = con.prepareStatement(AccessAndRolesConstants.GET_LEVEL2_ACCESS);
			preparedStatement.setInt(1, id);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				Level2AccessVo level2Access = new Level2AccessVo();
				level2Access.setId(rs.getInt(1));
				level2Access.setName(rs.getString(2));
				level2Access.setKey(rs.getString(3));
				level2Access.setHasChildren(rs.getBoolean(4));
				level2Access.setHasAccess(rs.getBoolean(5));
				String actions = rs.getString(6);
				if (actions != null && !actions.isEmpty()) {
					Map<String, Boolean> actionMap = new HashMap<String, Boolean>();
					String parts[] = actions.split(",");
					for (String part : parts) {
						String data[] = part.split(":");
						String actionName = data[0].trim();
						Boolean actionValue = Boolean.valueOf(data[1].trim());
						actionMap.put(actionName, actionValue);
					}
					level2Access.setActions(actionMap);
				}
				List<Level3AccessVo> Level3Access = getLevel3Access(level2Access.getId(),con);
				level2Access.setSubmodules(Level3Access);
				listLevel2Access.add(level2Access);
			}
		} catch (Exception e) {
			logger.info("Error in  getLevel2Access:", e);
			throw new ApplicationException(e.getMessage());
		}
		finally{
			closeResources(rs, preparedStatement, null);
		}
		if (listLevel2Access.size() > 0) {
			return listLevel2Access;
		} else {
			return null;
		}


	}

	private List<Level3AccessVo> getLevel3Access(Integer id,Connection con) throws ApplicationException {
		logger.info("Entry into getLevel3Access");
		List<Level3AccessVo> listLevel3Access = new ArrayList<Level3AccessVo>();
		PreparedStatement preparedStatement =null;
		ResultSet rs =null;
		try {
			preparedStatement = con.prepareStatement(AccessAndRolesConstants.GET_LEVEL3_ACCESS);
			preparedStatement.setInt(1, id);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				Level3AccessVo level3Access = new Level3AccessVo();
				level3Access.setId(rs.getInt(1));
				level3Access.setName(rs.getString(2));
				level3Access.setKey(rs.getString(3));
				level3Access.setHasChildren(rs.getBoolean(4));
				level3Access.setHasAccess(rs.getBoolean(5));
				String actions = rs.getString(6);
				if (actions != null && !actions.isEmpty()) {
					Map<String, Boolean> actionMap = new HashMap<String, Boolean>();
					String parts[] = actions.split(",");
					for (String part : parts) {
						String data[] = part.split(":");
						String actionName = data[0].trim();
						Boolean actionValue = Boolean.valueOf(data[1].trim());
						actionMap.put(actionName, actionValue);
					}
					level3Access.setActions(actionMap);
				}
				List<Level4AccessVo> Level4Access = getLevel4Access(level3Access.getId(),con);
				level3Access.setSubmodules(Level4Access);
				listLevel3Access.add(level3Access);
			}
		} catch (Exception e) {
			logger.info("Error in  getLevel3Access:", e);
			throw new ApplicationException(e.getMessage());
		}
		finally{
			closeResources(rs, preparedStatement, null);
		}
		if (listLevel3Access.size() > 0) {
			return listLevel3Access;
		} else {
			return null;
		}

	}

	private List<Level4AccessVo> getLevel4Access(Integer id,Connection con) throws ApplicationException {
		logger.info("Entry into getLevel4Access");
		List<Level4AccessVo> listLevel4Access = new ArrayList<Level4AccessVo>();
		PreparedStatement preparedStatement =null;
		ResultSet rs =null;
		try 
		{
			preparedStatement = con.prepareStatement(AccessAndRolesConstants.GET_LEVEL4_ACCESS);
			preparedStatement.setInt(1, id);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				Level4AccessVo level4Access = new Level4AccessVo();
				level4Access.setId(rs.getInt(1));
				level4Access.setName(rs.getString(2));
				level4Access.setKey(rs.getString(3));
				level4Access.setHasChildren(rs.getBoolean(4));
				level4Access.setHasAccess(rs.getBoolean(5));
				String actions = rs.getString(6);
				if (actions != null && !actions.isEmpty()) {
					Map<String, Boolean> actionMap = new HashMap<String, Boolean>();
					String parts[] = actions.split(",");
					for (String part : parts) {
						String data[] = part.split(":");
						String actionName = data[0].trim();
						Boolean actionValue = Boolean.valueOf(data[1].trim());
						actionMap.put(actionName, actionValue);
					}
					level4Access.setActions(actionMap);
				}
				List<Level5AccessVo> level5Access = getLevel5Access(level4Access.getId(),con);
				level4Access.setSubmodules(level5Access);
				listLevel4Access.add(level4Access);
			}
		} catch (Exception e) {
			logger.info("Error in  getLevel4Access:", e);
			throw new ApplicationException(e.getMessage());
		}
		finally{
			closeResources(rs, preparedStatement, null);
		}
		if (listLevel4Access.size() > 0) {
			return listLevel4Access;
		} else {
			return null;
		}

	}


	private List<Level5AccessVo> getLevel5Access(Integer id,Connection con) throws ApplicationException {
		logger.info("Entry into getLevel5Access");
		List<Level5AccessVo> listLevel5Access = new ArrayList<Level5AccessVo>();
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try 
		{
			preparedStatement = con.prepareStatement(AccessAndRolesConstants.GET_LEVEL5_ACCESS);
			preparedStatement.setInt(1, id);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				Level5AccessVo level5Access = new Level5AccessVo();
				level5Access.setId(rs.getInt(1));
				level5Access.setName(rs.getString(2));
				level5Access.setKey(rs.getString(3));
				level5Access.setHasChildren(rs.getBoolean(4));
				level5Access.setHasAccess(rs.getBoolean(5));
				String actions = rs.getString(6);
				if (actions != null && !actions.isEmpty()) {
					Map<String, Boolean> actionMap = new HashMap<String, Boolean>();
					String parts[] = actions.split(",");
					for (String part : parts) {
						String data[] = part.split(":");
						String actionName = data[0].trim();
						Boolean actionValue = Boolean.valueOf(data[1].trim());
						actionMap.put(actionName, actionValue);
					}
					level5Access.setActions(actionMap);
				}
				listLevel5Access.add(level5Access);
			}
		} catch (Exception e) {
			logger.info("Error in  getLevel4Access:", e);
			throw new ApplicationException(e.getMessage());
		}finally{
			closeResources(rs, preparedStatement, null);
		}
		if (listLevel5Access.size() > 0) {
			return listLevel5Access;
		} else {
			return null;
		}

	}

	public List<Level1AccessVo> constructMinimalAccess(String name) throws ApplicationException {
		logger.info("Entry into constructMinimalAccess");
		List<Level1AccessVo> listLevel1Access = new ArrayList<Level1AccessVo>();
		if(name.equals("Vendor")){

			try {
				Level1AccessVo level1AccessVo=new Level1AccessVo();	
				level1AccessVo.setId(1);
				level1AccessVo.setName("Dashboard");
				level1AccessVo.setKey("dashboard");
				level1AccessVo.setHasAccess(true);
				level1AccessVo.setHasChildren(false);
				level1AccessVo.setActions(null);
				List<String> tableColums =new ArrayList<String>();
				level1AccessVo.setTableColums(tableColums);
				level1AccessVo.setIsDetailed(false);
				level1AccessVo.setSubmodules(null);
				listLevel1Access.add(level1AccessVo);



			} catch (Exception e) {
				logger.info("Error in  getAllAccess:", e);
				throw new ApplicationException(e);
			}
		}
		/*ObjectMapper mapper = new ObjectMapper();
		try {
			accessData = mapper.writeValueAsString(listLevel1Access);
		} catch (JsonProcessingException e1) {
			throw new ApplicationException(e1.getMessage());
		}
		 */
		return listLevel1Access;
	}

}

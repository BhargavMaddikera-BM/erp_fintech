package com.blackstrawai.workflow;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Repository;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.common.BaseDao;
import com.fasterxml.jackson.databind.ObjectMapper;

@Repository
public class WorkflowGeneralSettingsDao extends BaseDao {

	private Logger logger = Logger.getLogger(WorkflowGeneralSettingsDao.class);

	public WorkflowGeneralSettingsVo createWorkflowGeneralSetting(WorkflowGeneralSettingsVo workflowGeneralSettingsVo) throws ApplicationException {
		logger.info("Entry into method: createWorkflowGeneralSettings");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = getUserMgmConnection();
			String sql = WorkflowGeneralSettingsConstants.INSERT_INTO_WORKFLOW_GENERAL_SETTINGS;
			preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setInt(1, workflowGeneralSettingsVo.getModuleId());
			ObjectMapper mapper = new ObjectMapper();
			String newJsonData = mapper.writeValueAsString(workflowGeneralSettingsVo.getData());
			preparedStatement.setString(2, newJsonData);
			preparedStatement.setInt(3, workflowGeneralSettingsVo.getOrganizationId());
			preparedStatement.setString(4, workflowGeneralSettingsVo.getStatus());
			preparedStatement.setString(5, workflowGeneralSettingsVo.getUserId());
			preparedStatement.setString(6, workflowGeneralSettingsVo.getRoleName());
			
			int rowAffected = preparedStatement.executeUpdate();
			if (rowAffected == 1) {
				rs = preparedStatement.getGeneratedKeys();
				if (rs.next()) {
					workflowGeneralSettingsVo.setId(rs.getInt(1));
				}
			}
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return workflowGeneralSettingsVo;
	}

	public WorkflowGeneralSettingsVo updateWorkflowGeneralSetting(WorkflowGeneralSettingsVo workflowGeneralSettingsVo) throws ApplicationException {
		logger.info("Entry into method: updateWorkflowGeneralSettings");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = getUserMgmConnection();
			String sql = WorkflowGeneralSettingsConstants.UPDATE_WORKFLOW_GENERAL_SETTINGS;
			preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setInt(1, workflowGeneralSettingsVo.getModuleId());
			ObjectMapper mapper = new ObjectMapper();
			String newJsonData = mapper.writeValueAsString(workflowGeneralSettingsVo.getData());
			preparedStatement.setString(2, newJsonData);
			preparedStatement.setInt(3, workflowGeneralSettingsVo.getOrganizationId());
			preparedStatement.setString(4, workflowGeneralSettingsVo.getStatus());
			preparedStatement.setString(5, workflowGeneralSettingsVo.getUserId());
			preparedStatement.setString(6, workflowGeneralSettingsVo.getRoleName());
			preparedStatement.setInt(7, workflowGeneralSettingsVo.getId());
			preparedStatement.executeUpdate();
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return workflowGeneralSettingsVo;
	}


	public List<WorkflowGeneralSettingsVo> getAllWorkflowGeneralSettingsOfAnOrganization(int organizationId,int moduleId) throws ApplicationException {
		logger.info("Entry into method: getAllWorkflowGeneralSettingssOfAnOrganization");
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		List<WorkflowGeneralSettingsVo> listWorkflowGeneralSettingss = new ArrayList<WorkflowGeneralSettingsVo>();
		try {
			con = getUserMgmConnection();
			String query = WorkflowGeneralSettingsConstants.GET_WORKFLOW_GENERAL_SETTINGS_BY_ORGANIZATION_MODULE;
			preparedStatement = con.prepareStatement(query);
			//id,name,tax_rate_type_id,rate,is_base,status,organization_id,user_id,isSuperAdmin,create_ts
			preparedStatement.setInt(1, organizationId);
			preparedStatement.setInt(2,moduleId);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				WorkflowGeneralSettingsVo workflowGeneralSettingsVo = new WorkflowGeneralSettingsVo();
				workflowGeneralSettingsVo.setId(rs.getInt(1));
				workflowGeneralSettingsVo.setModuleId(rs.getInt(2));
				String data=rs.getString(3);
				List<WorkflowCommonVo> dataList=new ArrayList<WorkflowCommonVo>();	
				if (data != null ) {
						JSONParser parser = new JSONParser();
						JSONArray dataFields = (JSONArray) parser.parse(data);
						Iterator<JSONObject> iterator = dataFields.iterator();					
						while(iterator.hasNext()) {
							WorkflowCommonVo workflowCommonVo=new WorkflowCommonVo();
							JSONObject object=(JSONObject)iterator.next();
							logger.info("Data:"+object.toString());
							workflowCommonVo.setId(object.get("id")!=null?Integer.parseInt(object.get("id").toString()):0);
							workflowCommonVo.setName(object.get("name")!=null?object.get("name").toString():"");
							workflowCommonVo.setDescription(object.get("description")!=null?object.get("description").toString():"");
							workflowCommonVo.setActive(object.get("isActive")!=null?Boolean.parseBoolean(object.get("isActive").toString()):false);
							dataList.add(workflowCommonVo);
						}
						}
				
				workflowGeneralSettingsVo.setData(dataList);
				workflowGeneralSettingsVo.setOrganizationId(rs.getInt(4));
				workflowGeneralSettingsVo.setStatus(rs.getString(5));
				workflowGeneralSettingsVo.setUserId(rs.getString(6));
				workflowGeneralSettingsVo.setRoleName(rs.getString(7));				
				listWorkflowGeneralSettingss.add(workflowGeneralSettingsVo);
			}
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return listWorkflowGeneralSettingss;
	}

	

	

}

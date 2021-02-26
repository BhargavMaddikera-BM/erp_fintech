package com.blackstrawai.workflow;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Repository;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.ap.dropdowns.BasicLocationVo;
import com.blackstrawai.common.BaseDao;
import com.blackstrawai.common.CommonConstants;
import com.fasterxml.jackson.databind.ObjectMapper;


@Repository
public class WorkflowSettingsDao extends BaseDao {

	private Logger logger = Logger.getLogger(WorkflowSettingsDao.class);

	public WorkflowSettingsVo createWorkflowSettings(WorkflowSettingsVo workflowSettingsVo) throws ApplicationException {
		logger.info("Entry into method: createWorkflowSettings");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = getUserMgmConnection();
			boolean isWorkflowSettingsExist=checkWorkflowSettingsExist(workflowSettingsVo,con);
			if(isWorkflowSettingsExist){
			throw new Exception("Workflow Settings Exist for the Organization");
			}
			String sql = WorkflowSettingsConstants.INSERT_INTO_WORKFLOW_SETTINGS;
			preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			//module_id,rule_name,rule_priority,data,is_base,description,status
			preparedStatement.setInt(1,workflowSettingsVo.getModuleId());
			preparedStatement.setString(2,workflowSettingsVo.getName());
			preparedStatement.setInt(3,workflowSettingsVo.getPriority());
			ObjectMapper mapper = new ObjectMapper();
			String newJsonData = mapper.writeValueAsString(workflowSettingsVo.getData());
			logger.info(newJsonData);							
			preparedStatement.setString(4,newJsonData);			
			preparedStatement.setBoolean(5,workflowSettingsVo.getIsBase());
			preparedStatement.setString(6,workflowSettingsVo.getDescription());
			preparedStatement.setInt(7,workflowSettingsVo.getOrganizationId());
			preparedStatement.setString(8,workflowSettingsVo.getStatus());
			preparedStatement.setString(9, workflowSettingsVo.getUserId());
			preparedStatement.setString(10, workflowSettingsVo.getRoleName());
			int rowAffected = preparedStatement.executeUpdate();
			if (rowAffected == 1) {
				rs = preparedStatement.getGeneratedKeys();
				if (rs.next()) {
					workflowSettingsVo.setId(rs.getInt(1));
				}
			}
		} catch (Exception e) {
			logger.error("Error in createWorkflowSettings",e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return workflowSettingsVo;
	}

	public boolean updateWorkflowSettingPriority(int id,int priority) throws ApplicationException {
		logger.info("Entry into method: updateWorkflowSettingPriority");
		boolean result=false;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = getUserMgmConnection();
			String sql = WorkflowSettingsConstants.UPDATE_WORKFLOW_SETTING_PRIORITY;
			preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setInt(1,priority);
			preparedStatement.setInt(2, id);
			int rowAffected = preparedStatement.executeUpdate();
			if (rowAffected == 1) {
				rs = preparedStatement.getGeneratedKeys();
				result=true;
			}

		} catch (Exception e) {
			logger.error("Error in updateWorkflowSettingPriority",e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return result;
	}
	
	
	public WorkflowSettingsVo updateWorkflowSettings(WorkflowSettingsVo workflowSettingsVo) throws ApplicationException {
		logger.info("Entry into method: updateWorkflowSettings");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = getUserMgmConnection();
//			boolean isWorkflowSettingsExist=checkWorkflowSettingsExist(workflowSettingsVo,con);
//			if(isWorkflowSettingsExist){
//			throw new Exception("Workflow Settings Exist for the Organization");
//			}
			
			String sql = WorkflowSettingsConstants.UPDATE_WORKFLOW_SETTINGS;
			preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setInt(1,workflowSettingsVo.getModuleId());
			preparedStatement.setString(2,workflowSettingsVo.getName());
			preparedStatement.setInt(3,workflowSettingsVo.getPriority());
			ObjectMapper mapper = new ObjectMapper();
			String newJsonData = mapper.writeValueAsString(workflowSettingsVo.getData());
			logger.info(newJsonData);							
			preparedStatement.setString(4,newJsonData);			
			preparedStatement.setBoolean(5,workflowSettingsVo.getIsBase());
			preparedStatement.setString(6,workflowSettingsVo.getDescription());
			preparedStatement.setInt(7,workflowSettingsVo.getOrganizationId());
			preparedStatement.setString(8,workflowSettingsVo.getStatus());
			preparedStatement.setString(9, workflowSettingsVo.getUserId());
			preparedStatement.setTimestamp(10,  new Timestamp(System.currentTimeMillis()));
			preparedStatement.setString(11, workflowSettingsVo.getRoleName());
			preparedStatement.setInt(12, workflowSettingsVo.getId());
			int rowAffected = preparedStatement.executeUpdate();
			if (rowAffected == 1) {
				rs = preparedStatement.getGeneratedKeys();
				if (rs.next()) {
					workflowSettingsVo.setId(rs.getInt(1));
				}
			}

		} catch (Exception e) {
			logger.error("Error in updateWorkflowSettings",e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return workflowSettingsVo;
	}

	public WorkflowSettingsVo deleteWorkflowSettings(int id,String status) throws ApplicationException {
		logger.info("Entry into method: deleteWorkflowSettings");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		
		WorkflowSettingsVo workflowSettingsVo = new WorkflowSettingsVo();
		try {

		
			con = getUserMgmConnection();
			String sql = WorkflowSettingsConstants.CHANGE_WORKFLOW_SETTINGS_STATUS;
			preparedStatement = con.prepareStatement(sql);
			preparedStatement.setString(1, status);
			preparedStatement.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
			preparedStatement.setInt(3, id);
			workflowSettingsVo.setId(id);
			preparedStatement.executeUpdate();
		
		
		} catch (Exception e) {
			logger.error("Error in deleteWorkflowSettings",e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return workflowSettingsVo;
	}

	public List<WorkflowSettingsVo> getAllWorkflowSettingsOfAnOrganization(int organizationId,int moduleId) throws ApplicationException {
		logger.info("Entry into method: getAllWorkflowSettingsOfAnOrganization");
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		List<WorkflowSettingsVo> listWorkflowSettings = new LinkedList<WorkflowSettingsVo>();
		try {
			con = getUserMgmConnection();
			String query = WorkflowSettingsConstants.GET_WORKFLOW_SETTINGS_BY_ORGANIZATION;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, organizationId);
			preparedStatement.setInt(2, moduleId);
			preparedStatement.setString(3, CommonConstants.STATUS_AS_ACTIVE);
			preparedStatement.setString(4, CommonConstants.STATUS_AS_INACTIVE);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				WorkflowSettingsVo workflowSettingsVo = new WorkflowSettingsVo();
				workflowSettingsVo.setId(rs.getInt(1));
				workflowSettingsVo.setModuleId(rs.getInt(2));
				workflowSettingsVo.setName(rs.getString(3));
				workflowSettingsVo.setPriority(rs.getInt(4));
				String data = rs.getString(5);
				
				convertJsonObjectToRuleData(workflowSettingsVo, data);
				workflowSettingsVo.setIsBase(rs.getBoolean(6));
				workflowSettingsVo.setDescription(rs.getString(7));				
				workflowSettingsVo.setStatus(rs.getString(8));
				workflowSettingsVo.setOrganizationId(rs.getInt(9));
				listWorkflowSettings.add(workflowSettingsVo);
			}
		} catch (Exception e) {
			logger.error("getAllWorkflowSettingsOfAnOrganization",e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return listWorkflowSettings;
	}
	
	public List<WorkflowSettingsVo> getAllActiveWorkflowSettingsOfAnOrganization(int organizationId,int moduleId) throws ApplicationException {
		logger.info("Entry into method: getAllActiveWorkflowSettingsOfAnOrganization");
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		List<WorkflowSettingsVo> listWorkflowSettings = new LinkedList<WorkflowSettingsVo>();
		try {
			con = getUserMgmConnection();
			String query = WorkflowSettingsConstants.GET_ACTIVE_WORKFLOW_SETTINGS_BY_ORGANIZATION;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, organizationId);
			preparedStatement.setInt(2, moduleId);
			preparedStatement.setString(3, CommonConstants.STATUS_AS_ACTIVE);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				WorkflowSettingsVo workflowSettingsVo = new WorkflowSettingsVo();
				workflowSettingsVo.setId(rs.getInt(1));
				workflowSettingsVo.setModuleId(rs.getInt(2));
				workflowSettingsVo.setName(rs.getString(3));
				workflowSettingsVo.setPriority(rs.getInt(4));
				String data = rs.getString(5);				
				convertJsonObjectToRuleData(workflowSettingsVo, data);
				workflowSettingsVo.setIsBase(rs.getBoolean(6));
				workflowSettingsVo.setDescription(rs.getString(7));				
				workflowSettingsVo.setStatus(rs.getString(8));
				workflowSettingsVo.setOrganizationId(rs.getInt(9));
				listWorkflowSettings.add(workflowSettingsVo);
			}
		} catch (Exception e) {
			logger.error("getAllActiveWorkflowSettingsOfAnOrganization",e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return listWorkflowSettings;
	}

	private void convertJsonObjectToRuleData(WorkflowSettingsVo workflowSettingsVo, String data) throws ParseException {
		if (data != null ) {
			JSONParser parser = new JSONParser();
			JSONObject dataFields = (JSONObject) parser.parse(data);
			if(dataFields!=null) {
			WorkflowSettingsRuleData ruleData = new WorkflowSettingsRuleData();
			ruleData.setValidationParameterId(dataFields.get("validationParameterId")!=null?Integer.parseInt(dataFields.get("validationParameterId").toString()):0);
			ruleData.setChoiceId(dataFields.get("choiceId")!=null?Integer.parseInt(dataFields.get("choiceId").toString()):0);
			ruleData.setChoiceName(dataFields.get("choiceName")!=null?dataFields.get("choiceName").toString():"");
			ruleData.setLessThan(dataFields.get("lessThan")!=null?dataFields.get("lessThan").toString():"");					
			ruleData.setEqual(dataFields.get("equal")!=null?dataFields.get("equal").toString():"");
			ruleData.setMin(dataFields.get("min")!=null?dataFields.get("min").toString():null);
			ruleData.setMax(dataFields.get("max")!=null?dataFields.get("max").toString():null);
			List<Object> isList=new ArrayList<Object>();
			if(dataFields.get("is")!=null) {
			JSONArray dataArray = (JSONArray) parser.parse(dataFields.get("is").toString());		
			for(Object o: dataArray){
			    if ( o instanceof JSONObject ) {//For Location
			    	JSONObject is=(JSONObject)o;
			    	BasicLocationVo basicLocationVo=new BasicLocationVo();
			    	basicLocationVo.setId(is.get("id")!=null ?Integer.parseInt(is.get("id").toString()):0);
			    	basicLocationVo.setName(is.get("name")!=null ?is.get("name").toString():"");
			    	basicLocationVo.setIsRegistered(is.get("isRegistered")!=null?Boolean.parseBoolean(is.get("isRegistered").toString()):false);
			    	basicLocationVo.setGstNo(is.get("gstn")!=null ?is.get("gstn").toString():"");
			    	isList.add(basicLocationVo);
			    }else if( o instanceof Long ) {//For Vendor
			    	Long is=(Long)o;
			    	isList.add(is);
			    }
			}
			
			}
			ruleData.setIs(isList);
			List<Object> isNotList=new ArrayList<Object>();
			if(dataFields.get("isNot")!=null) {
			JSONArray dataArray = (JSONArray) parser.parse(dataFields.get("isNot").toString());
			for(Object o: dataArray){
			    if ( o instanceof JSONObject ) {//For Location
			    	JSONObject is=(JSONObject)o;
			    	BasicLocationVo basicLocationVo=new BasicLocationVo();
			    	basicLocationVo.setId(is.get("id")!=null ?Integer.parseInt(is.get("id").toString()):0);
			    	basicLocationVo.setName(is.get("name")!=null ?is.get("name").toString():"");
			    	basicLocationVo.setIsRegistered(is.get("isRegistered")!=null?Boolean.parseBoolean(is.get("isRegistered").toString()):false);
			    	basicLocationVo.setGstNo(is.get("gstn")!=null ?is.get("gstn").toString():"");
			    	isNotList.add(basicLocationVo);
			    }else if( o instanceof Long ) {//For Vendor
			    	Long is=(Long)o;
			    	isNotList.add(is);
			    }
			}
			}
			ruleData.setIsNot(isNotList);
			ruleData.setDisabled(dataFields.get("disabled")!=null?Boolean.parseBoolean(dataFields.get("disabled").toString()):false);
			ruleData.setEnabled(dataFields.get("enabled")!=null?Boolean.parseBoolean(dataFields.get("enabled").toString()):false);
			ruleData.setGreaterThan(dataFields.get("greaterThan")!=null?dataFields.get("greaterThan").toString():"");
			ruleData.setApprovalTypeId(dataFields.get("approvalTypeId")!=null?Integer.parseInt(dataFields.get("approvalTypeId").toString()):0);			
			ruleData.setEmail(dataFields.get("email")!=null?Boolean.parseBoolean(dataFields.get("email").toString()):false);
			ruleData.setInApp(dataFields.get("inApp")!=null?Boolean.parseBoolean(dataFields.get("inApp").toString()):false);
			ruleData.setSms(dataFields.get("sms")!=null?Boolean.parseBoolean(dataFields.get("sms").toString()):false);
			ruleData.setWhatsApp(dataFields.get("whatsApp")!=null?Boolean.parseBoolean(dataFields.get("whatsApp").toString()):false);

			List<WorkflowSettingsCommonVo> usersList=new ArrayList<WorkflowSettingsCommonVo>();
			if(dataFields.get("usersList")!=null) {
			JSONArray dataArray = (JSONArray) parser.parse(dataFields.get("usersList").toString());
			Iterator<JSONObject> iterator = dataArray.iterator();					
			while(iterator.hasNext()) {
				JSONObject userJson=(JSONObject)iterator.next();						
				int id=userJson.get("id")!=null?Integer.parseInt(userJson.get("id").toString()):0;
				String name=userJson.get("name")!=null?userJson.get("name").toString():"";
				String type=userJson.get("type")!=null?userJson.get("type").toString():"";
				int roleId=userJson.get("roleId")!=null?Integer.parseInt(userJson.get("roleId").toString()):0;
				WorkflowSettingsCommonVo userObj=new WorkflowSettingsCommonVo();
				userObj.setId(id);
				userObj.setName(name);
				userObj.setType(type);
				userObj.setRoleId(roleId);
				usersList.add(userObj);
			}
			}
			ruleData.setUsersList(usersList);
			
			List<WorkflowSettingsCommonVo> rolesList=new ArrayList<WorkflowSettingsCommonVo>();
//			if(dataFields.get("rolesList")!=null) {
//			JSONArray dataArray = (JSONArray) parser.parse(dataFields.get("rolesList").toString());
//			Iterator<JSONObject> iterator = dataArray.iterator();					
//			while(iterator.hasNext()) {
//				JSONObject roleJson=(JSONObject)iterator.next();						
//				int id=roleJson.get("id")!=null?Integer.parseInt(roleJson.get("id").toString()):0;
//				String name=roleJson.get("name")!=null?roleJson.get("name").toString():"";
//				String type=roleJson.get("type")!=null?roleJson.get("type").toString():"";
//				WorkflowSettingsCommonVo roleObj=new WorkflowSettingsCommonVo();
//				roleObj.setId(id);
//				roleObj.setName(name);
//				roleObj.setType(type);
//				rolesList.add(roleObj);
//			}
//			}
			//ruleData.setRolesList(rolesList);
			List<WorkflowSettingsCommonVo> setSequence=new ArrayList<WorkflowSettingsCommonVo>();
			if(dataFields.get("setSequence")!=null) {
			JSONArray dataArray = (JSONArray) parser.parse(dataFields.get("setSequence").toString());
			Iterator<JSONObject> iterator = dataArray.iterator();					
			while(iterator.hasNext()) {
				JSONObject roleJson=(JSONObject)iterator.next();						
				int id=roleJson.get("id")!=null?Integer.parseInt(roleJson.get("id").toString()):0;
				String name=roleJson.get("name")!=null?roleJson.get("name").toString():"";
				String type=roleJson.get("type")!=null?roleJson.get("type").toString():"";
				WorkflowSettingsCommonVo seqObj=new WorkflowSettingsCommonVo();
				seqObj.setId(id);
				seqObj.setName(name);
				seqObj.setType(type);
				setSequence.add(seqObj);
			}
			}
			ruleData.setSetSequence(setSequence);			
			workflowSettingsVo.setData(ruleData);
			}
		}
	}
	
	public boolean checkWorkflowDefaultRuleExistForModule(int organizationId,int moduleId) throws ApplicationException {
		logger.info("Entry into method: checkWorkflowDefaultRuleExistForModule");
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			con = getUserMgmConnection();
			preparedStatement = con.prepareStatement(WorkflowSettingsConstants.CHECK_WORKFLOW_DEFAULT_RULE_EXIST_FOR_MODULE);
			preparedStatement.setInt(1, organizationId);
			preparedStatement.setInt(2, moduleId);
			preparedStatement.setString(3,CommonConstants.STATUS_AS_ACTIVE);
			preparedStatement.setBoolean(4,true);
			rs = preparedStatement.executeQuery();
			if (rs.next()) {
				return true;	
			}
		} catch (Exception e) {
			logger.error("Error in checkWorkflowDefaultRuleExistForModule",e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return false;
	}

	private boolean checkWorkflowSettingsExist(WorkflowSettingsVo workflowSettingsVo,Connection conn) throws ApplicationException {
		logger.info("Entry into method: checkWorkflowSettingsExist");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {

		
			String query = WorkflowSettingsConstants.CHECK_WORKFLOW_SETTINGS_FOR_ORGANIZATION;
			preparedStatement = conn.prepareStatement(query);
			preparedStatement.setInt(1, workflowSettingsVo.getOrganizationId());
			preparedStatement.setInt(2, workflowSettingsVo.getModuleId());
			preparedStatement.setString(3,workflowSettingsVo.getName());

			rs = preparedStatement.executeQuery();
			if (rs.next()) {
				if(workflowSettingsVo.getId()==0) {//create scenario
					return true;	
				}else {
					if(workflowSettingsVo.getId()!=rs.getInt(1)) {//If it is not same record
						return true;
					}
				}
			}
		} catch (Exception e) {
			logger.error("Error in checkWorkflowSettingsExist",e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, null);
		}
		return false;
	}
	
	public WorkflowSettingsVo getWorkflowSettingsById(int id) throws ApplicationException {
		logger.info("Entry into method: getWorkflowSettingsById");
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		WorkflowSettingsVo workflowSettingsVo = new WorkflowSettingsVo();
		try {
			con = getUserMgmConnection();
			String query = WorkflowSettingsConstants.GET_WORKFLOW_SETTINGS_BY_ID;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, id);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				
				workflowSettingsVo.setId(rs.getInt(1));
				workflowSettingsVo.setModuleId(rs.getInt(2));
				workflowSettingsVo.setName(rs.getString(3));
				workflowSettingsVo.setPriority(rs.getInt(4));
				String data = rs.getString(5);				
				convertJsonObjectToRuleData(workflowSettingsVo, data);
				workflowSettingsVo.setIsBase(rs.getBoolean(6));
				workflowSettingsVo.setDescription(rs.getString(7));				
				workflowSettingsVo.setStatus(rs.getString(8));
				workflowSettingsVo.setOrganizationId(rs.getInt(9));
			}
			
		} catch (Exception e) {
			logger.error("Error in getWorkflowSettingsById",e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return workflowSettingsVo;
	}
	

	public void syncWorflow() throws ApplicationException {
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			con = getUserMgmConnection();
			con.setAutoCommit(false);
			String query = WorkflowSettingsConstants.GET_UNIQUE_SETTINGS_ORG;
			preparedStatement = con.prepareStatement(query);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				String orgId=rs.getString(1);
				String userId=rs.getString(2);
				String roleName=rs.getString(3);
				//closeResources(null, preparedStatement, null);
				preparedStatement = con.prepareStatement(WorkflowSettingsConstants.INSERT_BANKING_MODULE_SETTINGS_ORG);
				preparedStatement.setString(1, orgId);
				preparedStatement.setString(2, userId);
				preparedStatement.setString(3, roleName);
				preparedStatement.executeUpdate();
				//closeResources(null, preparedStatement, null);
				preparedStatement = con.prepareStatement(WorkflowSettingsConstants.INSERT_BANKING_MODULE_RULE_ORG);
				preparedStatement.setString(1, orgId);
				preparedStatement.setString(2, userId);
				preparedStatement.setString(3, roleName);
				preparedStatement.executeUpdate();
			}
			con.commit();
			
		}catch(Exception e) {
			try {
				con.rollback();
			}catch(Exception ex) {
				logger.error("Error in Committing",e);
			}
			logger.error("Error in syncWorflow",e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
	}
}

package com.blackstrawai.request.workflow;

import java.sql.Timestamp;
import java.util.List;

import com.blackstrawai.common.BaseRequest;
import com.blackstrawai.workflow.WorkflowCommonVo;

public class WorkflowGeneralSettingsRequest extends BaseRequest{
	private int id;    
	private int moduleId;    
	private List<WorkflowCommonVo>  data;      
	private int organizationId;    
	private int updateUserId;    
	private String updateRoleName;    
	private String roleName;    
	private boolean isSuperAdmin;    
	private String status;    
	private Timestamp createTs;    
	private Timestamp updateTs;    

	public void setId(int id){
		this.id=id;
	}
	public int getId(){
		return  id; 
	}
	public void setModuleId(int moduleId){
		this.moduleId=moduleId;
	}
	public int getModuleId(){
		return  moduleId; 
	}

	public List<WorkflowCommonVo> getData() {
		return data;
	}
	public void setData(List<WorkflowCommonVo> data) {
		this.data = data;
	}
	public void setOrganizationId(int organizationId){
		this.organizationId=organizationId;
	}
	public int getOrganizationId(){
		return  organizationId; 
	}
	public void setUpdateUserId(int updateUserId){
		this.updateUserId=updateUserId;
	}
	public int getUpdateUserId(){
		return  updateUserId; 
	}
	public void setUpdateRoleName(String updateRoleName){
		this.updateRoleName=updateRoleName;
	}
	public String getUpdateRoleName(){
		return  updateRoleName; 
	}
	public void setRoleName(String roleName){
		this.roleName=roleName;
	}
	public String getRoleName(){
		return  roleName; 
	}
	public void setIsSuperAdmin(boolean isSuperAdmin){
		this.isSuperAdmin=isSuperAdmin;
	}
	public boolean getIsSuperAdmin(){
		return  isSuperAdmin; 
	}
	public void setStatus(String status){
		this.status=status;
	}
	public String getStatus(){
		return  status; 
	}
	public void setCreateTs(Timestamp createTs){
		this.createTs=createTs;
	}
	public Timestamp getCreateTs(){
		return  createTs; 
	}
	public void setUpdateTs(Timestamp updateTs){
		this.updateTs=updateTs;
	}
	public Timestamp getUpdateTs(){
		return  updateTs; 
	}
}
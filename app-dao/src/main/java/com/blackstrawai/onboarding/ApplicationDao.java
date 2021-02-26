package com.blackstrawai.onboarding;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.common.BaseDao;
import com.blackstrawai.common.CommonConstants;
import com.blackstrawai.onboarding.loginandregistration.ApplicationVo;
import com.blackstrawai.onboarding.loginandregistration.ApplicationsVo;
import com.blackstrawai.onboarding.loginandregistration.ModuleVo;


@Repository
public class ApplicationDao extends BaseDao{
	
private  Logger logger = Logger.getLogger(ApplicationDao.class);

private List<ApplicationVo> getAllApplications()throws ApplicationException {
	logger.info("Entry into method:getAllApplications");
	Connection con = null;
	PreparedStatement preparedStatement = null;
	ResultSet rs = null;
	List<ApplicationVo>listAllApplications=new ArrayList<ApplicationVo>();
	try{
		con = getUserMgmConnection();
		String query=CommonConstants.GET_ALL_APPLICATIONS;
		preparedStatement=con.prepareStatement(query);
		rs = preparedStatement.executeQuery();			
		while (rs.next()) {
			ApplicationVo applicationVo=new ApplicationVo();
			applicationVo.setId(rs.getInt(1));
			applicationVo.setName(rs.getString(2));
			applicationVo.setDescription(rs.getString(3));
			applicationVo.setUrl(rs.getString(4));
			String status=rs.getString(5);
			applicationVo.setStatus(status);
			listAllApplications.add(applicationVo);				
		}
	}catch(Exception e){
		throw new ApplicationException(e);
	}finally {
		closeResources(rs, preparedStatement, con);

	}	
	return listAllApplications;
}
	
	public ApplicationsVo getAllApplicationsAndModules()throws ApplicationException {	
		logger.info("Entry into method:getAllApplicationsAndModules");
		List<ApplicationVo> apps=getAllApplications();
		apps=getAllModulesInApplication(apps);
		ApplicationsVo applicationsVo=new ApplicationsVo();
		applicationsVo.setApps(apps);
		return applicationsVo;
		
	}
	
	private List<ApplicationVo> getAllModulesInApplication(List<ApplicationVo> applications)throws ApplicationException {
		logger.info("Entry into method:getAllModulesInApplication");
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		List<ApplicationVo>listAllApplications=new ArrayList<ApplicationVo>();
		try{
			con = getUserMgmConnection();
			for(int i=0;i<applications.size();i++){
				ApplicationVo appVo=applications.get(i);
				String query=CommonConstants.GET_ALL_MODULES_IN_APPLICATION;
				preparedStatement=con.prepareStatement(query);
				preparedStatement.setInt(1,appVo.getId());		
				rs = preparedStatement.executeQuery();			
				while (rs.next()) {
					ModuleVo moduleVo=new ModuleVo();
					moduleVo.setId(rs.getInt(1));
					moduleVo.setName(rs.getString(2));
					moduleVo.setDescription(rs.getString(3));
					moduleVo.setUrl(rs.getString(4));
					String status=rs.getString(5);
					moduleVo.setStatus(status);
					appVo.getModules().add(moduleVo);			
				}
				listAllApplications.add(appVo);
			}
			
		}catch(Exception e){
			throw new ApplicationException(e);
		}finally {
			closeResources(rs, preparedStatement, con);

		}	
		return listAllApplications;
	}
}

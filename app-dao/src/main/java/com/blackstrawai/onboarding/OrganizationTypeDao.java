package com.blackstrawai.onboarding;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.common.BaseDao;
import com.blackstrawai.common.FinanceCommonConstants;
import com.blackstrawai.onboarding.organization.OrganizationIndustryVo;
import com.blackstrawai.onboarding.organization.OrganizationTypeVo;

@Repository
public class OrganizationTypeDao extends BaseDao{


	
	private  Logger logger = Logger.getLogger(OrganizationTypeDao.class);
	
	

	public OrganizationTypeVo getOrganizationTypeByName(String name)throws ApplicationException{
		logger.info("Entry into getOrganizationTypeByName");
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		OrganizationTypeVo organizationTypeVo=new OrganizationTypeVo();
		
		try{
			con = getFinanceCommon();
			String query=FinanceCommonConstants.GET_ORGANIZATION_TYPE_BY_NAME;
			preparedStatement=con.prepareStatement(query);
			preparedStatement.setString(1, name);
			rs = preparedStatement.executeQuery();			
			while (rs.next()) {
				
				organizationTypeVo.setId(rs.getInt(1));
				organizationTypeVo.setName(rs.getString(2));
				organizationTypeVo.setDescription(rs.getString(3));
			}

					
				
				
			
		}catch(Exception e){
			throw new ApplicationException(e);
		}finally {
			closeResources(rs, preparedStatement, con);

		}	
		return organizationTypeVo;
		
	}

	public OrganizationIndustryVo getOrganizationDivisionByName(String name)throws ApplicationException{
		logger.info("Entry into getOrganizationDivisonByName");
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		OrganizationIndustryVo organizationDivisionVo=new OrganizationIndustryVo();
		
		try{
			con = getFinanceCommon();
			String query=FinanceCommonConstants.GET_ORGANIZATION_DIVISION_BY_NAME;
			preparedStatement=con.prepareStatement(query);
			preparedStatement.setString(1, name);
			rs = preparedStatement.executeQuery();			
			while (rs.next()) {
				
				organizationDivisionVo.setId(rs.getInt(1));
				organizationDivisionVo.setName(rs.getString(2));
				organizationDivisionVo.setDescription(rs.getString(3));
			}

		
			
		}catch(Exception e){
			throw new ApplicationException(e);
		}finally {
			closeResources(rs, preparedStatement, con);

		}	
		return organizationDivisionVo;
		
	}
	
}

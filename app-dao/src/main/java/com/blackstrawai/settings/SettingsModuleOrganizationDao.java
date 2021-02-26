package com.blackstrawai.settings;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.common.BaseDao;


@Repository
public class SettingsModuleOrganizationDao extends BaseDao {

	private Logger logger = Logger.getLogger(SettingsModuleOrganizationDao.class);
		
	public SettingsModuleOrganizationVo getSettingsModuleOrganizationBySubmodule(int organizationId,String subModule) throws ApplicationException {
		logger.info("Entry into getSettingsModuleOrganizationBySubmodule");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		SettingsModuleOrganizationVo settingsModuleOrganizationVo=new SettingsModuleOrganizationVo();
		if(subModule!=null) {
			try {
				con = getUserMgmConnection();
				preparedStatement = con
						.prepareStatement(SettingsAndPreferencesConstants.GET_SETTINGS_MODULE_ORGANIZATION_BY_SUB_MODULE) ;
				preparedStatement.setInt(1, organizationId);
				preparedStatement.setString(2, subModule);
				rs=preparedStatement.executeQuery();
				if(rs.next()) {
					settingsModuleOrganizationVo.setId(rs.getInt(1));
					settingsModuleOrganizationVo.setType(rs.getString(2));
					settingsModuleOrganizationVo.setName(rs.getString(3));
					settingsModuleOrganizationVo.setModuleName(rs.getString(4));
					settingsModuleOrganizationVo.setSubModuleName(rs.getString(5));
					settingsModuleOrganizationVo.setRequired(rs.getBoolean(6));
					settingsModuleOrganizationVo.setOrganizationId(rs.getInt(7));
					settingsModuleOrganizationVo.setUserId(rs.getString(8));
					settingsModuleOrganizationVo.setRoleName(rs.getString(9));
				}
				logger.info("Successfully Feteched getSettingsModuleOrganizationBySubmodule Object:"+settingsModuleOrganizationVo);
			} catch (Exception e) {
				logger.info("Error in getSettingsModuleOrganizationBySubmodule ", e);
				throw new ApplicationException(e);
			}finally {
				closeResources(rs, preparedStatement, con);
			}
		}

		return settingsModuleOrganizationVo;
	}

	}
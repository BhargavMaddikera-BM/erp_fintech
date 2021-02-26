package com.blackstrawai.chartofaccounts;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.common.BaseDao;
import com.blackstrawai.settings.CommonVo;
import com.blackstrawai.settings.SettingsAndPreferencesConstants;
import com.blackstrawai.settings.chartofaccounts.ChartOfAccountsLedgerDetailsVo;
import com.blackstrawai.settings.chartofaccounts.ChartOfAccountsLevel1Vo;
import com.blackstrawai.settings.chartofaccounts.ChartOfAccountsLevel2Vo;
import com.blackstrawai.settings.chartofaccounts.ChartOfAccountsLevel3Vo;
import com.blackstrawai.settings.chartofaccounts.ChartOfAccountsLevel4Vo;
import com.blackstrawai.settings.chartofaccounts.ChartOfAccountsLevel5Vo;
import com.blackstrawai.settings.chartofaccounts.ChartOfAccountsLevel6Vo;
import com.blackstrawai.settings.chartofaccounts.ChartOfAccountsReportVo;
import com.blackstrawai.settings.chartofaccounts.MinimalChartOfAccountsLevel2Vo;
import com.blackstrawai.settings.chartofaccounts.MinimalChartOfAccountsLevel3Vo;
import com.blackstrawai.settings.chartofaccounts.MinimalChartOfAccountsLevel4Vo;
import com.blackstrawai.settings.chartofaccounts.MinimalChartOfAccountsLevel6Vo;
import com.blackstrawai.settings.chartofaccounts.MinimalChartOfAccountsVo;

@Repository
public class ChartOfAccountsDao extends BaseDao {

	private Logger logger = Logger.getLogger(ChartOfAccountsDao.class);



	public String  getDefaultLedger(String entity ,String module ,  String fieldType) throws ApplicationException {
		String ledgerName = null;
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		List<String> paramList = new ArrayList<String>();
		try{
			if(entity!=null) {
			StringBuilder query = new StringBuilder(ChartOfAccountsConstants.GET_DEFAULT_LEDGER);
			query = query.append("where act_type in (? )");
			paramList.add(entity);
			if(module!=null) {
				query = query.append(" and additional_condition_type = ? ");
				paramList.add(module);
				
			}
			if(fieldType!=null) {
				query = query.append(" and field_type = ? ");
				paramList.add(fieldType);
			}
			
			logger.info("Final Query ::"+query.toString());
			logger.info("paramList::"+paramList);
			con = getUserMgmConnection();
			preparedStatement = con.prepareStatement(query.toString());
			int counter = 1;
			for (int i = 0; i < paramList.size(); i++) {
				logger.info(counter);
				preparedStatement.setString(counter, paramList.get(i));
				counter++;
			}
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				ledgerName = rs.getString(1);
			}
			logger.info("Default ledger ::"+ledgerName);
			}
		}catch(Exception e) {
			logger.info("Error in GetDefaultLedger"+e);
			throw new ApplicationException(e.getMessage());
		}finally{
			closeResources(rs, preparedStatement, con);
		}
		return ledgerName;
		
	}

	
	public String  getDefaultLedgerByMultipleEntity(List<String> entities ,String module ,  String fieldType) throws ApplicationException {
		String ledgerName = null;
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		List<String> paramList = new ArrayList<String>();
		try{
			if(entities!=null) {
			StringBuilder query = new StringBuilder(ChartOfAccountsConstants.GET_DEFAULT_LEDGER);
			String inSql = String.join(",", Collections.nCopies(entities.size(), "?"));
			query = query.append("where act_type in ("+ inSql+" )");
			paramList.addAll(entities);
			if(module!=null) {
				query = query.append(" and additional_condition_type = ? ");
				paramList.add(module);
				
			}
			if(fieldType!=null) {
				query = query.append(" and field_type = ? ");
				paramList.add(fieldType);
			}
			
			logger.info("Final Query ::"+query.toString());
			logger.info("paramList::"+paramList);
			con = getUserMgmConnection();
			preparedStatement = con.prepareStatement(query.toString());
			int counter = 1;
			for (int i = 0; i < paramList.size(); i++) {
				logger.info(counter);
				preparedStatement.setString(counter, paramList.get(i));
				counter++;
			}
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				ledgerName = rs.getString(1);
			}
			logger.info("Default ledger ::"+ledgerName);
			}
		}catch(Exception e) {
			logger.info("Error in GetDefaultLedger"+e);
			throw new ApplicationException(e.getMessage());
		}finally{
			closeResources(rs, preparedStatement, con);
		}
		return ledgerName;
		
	}

	
	
	

	public List<MinimalChartOfAccountsVo> getItemsPurchaseAccounts(String type , int orgId) throws SQLException, ApplicationException {
		 List<MinimalChartOfAccountsVo> purchaseAccounts = null ;
		 List<String> entity = null;
		 switch(type) {
		 case SettingsAndPreferencesConstants.CATEGORY_GOODS:
			 entity = Arrays.asList("Purchase","Inventory","Prepaid & Deposit");
			 purchaseAccounts = getLedgersByMultipleEntity(orgId, entity);
			 break;
		 case SettingsAndPreferencesConstants.CATEGORY_SERVICE:
			 entity = Arrays.asList("Direct Expense" , "Prepaid & Deposit");
			 purchaseAccounts = getLedgersByMultipleEntity(orgId, entity);
			 break;
		 case SettingsAndPreferencesConstants.CATEGORY_FIXEDASSETS:
			 entity = Arrays.asList("Fixed Asset");
			 purchaseAccounts = getLedgersByMultipleEntity(orgId, entity);
			 break;
		 }
		return purchaseAccounts;
		 
	}
	
	public List<MinimalChartOfAccountsVo> getItemsSalesAccounts(String type , int orgId) throws SQLException, ApplicationException {
		 List<MinimalChartOfAccountsVo> purchaseAccounts = null ;
		 List<String> entity = null;
		 switch(type) {
		 case SettingsAndPreferencesConstants.CATEGORY_GOODS:
			 entity = Arrays.asList("Sale","Inventory","Prepaid & Deposit");
			 purchaseAccounts = getLedgersByMultipleEntity(orgId, entity);
			 break;
		 case SettingsAndPreferencesConstants.CATEGORY_SERVICE:
			 
			 entity = Arrays.asList("Sale","Prepaid & Deposit");
			 purchaseAccounts = getLedgersByMultipleEntity(orgId, entity);
			 break;
		 case SettingsAndPreferencesConstants.CATEGORY_FIXEDASSETS:
			 entity = Arrays.asList("Fixed Asset");
			 purchaseAccounts = getLedgersByMultipleEntity(orgId, entity);
			 break;
		 }
		return purchaseAccounts;
		 
	}
	

	public List<MinimalChartOfAccountsVo> getAllLedgersByOrgId(int orgId) throws ApplicationException {
		logger.info("Entry into method:getLedgersByEntity" );
		
		List<MinimalChartOfAccountsVo> minimalChartOfAccounts = new ArrayList<MinimalChartOfAccountsVo>();
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			con = getUserMgmConnection();
			preparedStatement = con.prepareStatement(ChartOfAccountsConstants.GET_LEDGER_BY_ORG_ID);
			preparedStatement.setInt(1, orgId);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				MinimalChartOfAccountsVo accountsVo = new MinimalChartOfAccountsVo();
				accountsVo.setId(rs.getInt(1));
				accountsVo.setName(rs.getString(2));
				accountsVo.setLevel("L5");
				accountsVo.setType(rs.getString(3));
				accountsVo.setValue(accountsVo.getId());
				minimalChartOfAccounts.add(accountsVo);
			}
		} catch (Exception e) {
			logger.info("eror"+e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);

		}
		return minimalChartOfAccounts;
	}


	
	
	
	public List<MinimalChartOfAccountsVo> getLedgersByMultipleEntity(int orgId, List<String> entitys) throws ApplicationException {
		logger.info("Entry into method:getLedgersByEntity" );
		
		String query = ChartOfAccountsConstants.GET_LEDGER_BY_ORG_ID+
					" and coae.name in ("+  String.join(",", Collections.nCopies(entitys.size(), "?")) +")";
		
		List<MinimalChartOfAccountsVo> minimalChartOfAccounts = new ArrayList<MinimalChartOfAccountsVo>();
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			con = getUserMgmConnection();
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, orgId);
			int count = 1;
			for(String val : entitys) {
				count++;
				preparedStatement.setString(count, val);
			}
			logger.info("prep::>" + preparedStatement.toString());
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				MinimalChartOfAccountsVo accountsVo = new MinimalChartOfAccountsVo();
				accountsVo.setId(rs.getInt(1));
				accountsVo.setName(rs.getString(2));
				accountsVo.setLevel("L5");
				accountsVo.setType(rs.getString(3));
				accountsVo.setValue(accountsVo.getId());
				minimalChartOfAccounts.add(accountsVo);
			}
		} catch (Exception e) {
			logger.info("eror"+e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);

		}
		return minimalChartOfAccounts;
	}

	
	public List<MinimalChartOfAccountsVo> getLedgersByEntity(Integer orgId,String entityName)
			throws SQLException, ApplicationException {
		logger.info("Entry into method:getLedgersByEntity");
		List<MinimalChartOfAccountsVo> minimalChartOfAccounts = new ArrayList<MinimalChartOfAccountsVo>();
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			con = getUserMgmConnection();
			preparedStatement = con.prepareStatement(ChartOfAccountsConstants.GET_LEDGER_BY_ENTITY_NAME);
			preparedStatement.setInt(1, orgId);
			preparedStatement.setString(2, entityName);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				MinimalChartOfAccountsVo accountsVo = new MinimalChartOfAccountsVo();
				accountsVo.setId(rs.getInt(1));
				accountsVo.setName(rs.getString(2));
				accountsVo.setLevel("L5");
				accountsVo.setValue(accountsVo.getId());
				accountsVo.setType(rs.getString(3));
				minimalChartOfAccounts.add(accountsVo);
			}
		} catch (Exception e) {

			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);

		}
		return minimalChartOfAccounts;
	}

	
	public List<MinimalChartOfAccountsVo> getLedgerById(Integer orgId,Integer ledgerId)
			throws SQLException, ApplicationException {
		logger.info("Entry into getLedgersById");
		List<MinimalChartOfAccountsVo> minimalChartOfAccounts = new ArrayList<MinimalChartOfAccountsVo>();
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			con = getUserMgmConnection();
			preparedStatement = con.prepareStatement(ChartOfAccountsConstants.GET_LEDGER_BY_LEDGER_ID);
			preparedStatement.setInt(1, orgId);
			preparedStatement.setInt(2, ledgerId);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				MinimalChartOfAccountsVo accountsVo = new MinimalChartOfAccountsVo();
				accountsVo.setId(rs.getInt(1));
				accountsVo.setName(rs.getString(2));
				accountsVo.setLevel("L5");
				accountsVo.setValue(accountsVo.getId());
				accountsVo.setType(rs.getString(3));
				minimalChartOfAccounts.add(accountsVo);
			}
		} catch (Exception e) {

			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);

		}
		return minimalChartOfAccounts;
	}

	
	public List<MinimalChartOfAccountsVo> getLedgerByName(Integer orgId,String  ledgerName)
			throws SQLException, ApplicationException {
		logger.info("Entry into getLedgersById");
		List<MinimalChartOfAccountsVo> minimalChartOfAccounts = new ArrayList<MinimalChartOfAccountsVo>();
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			con = getUserMgmConnection();
			preparedStatement = con.prepareStatement(ChartOfAccountsConstants.GET_LEDGER_BY_LEDGER_NAME);
			preparedStatement.setInt(1, orgId);
			preparedStatement.setString(2, ledgerName);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				MinimalChartOfAccountsVo accountsVo = new MinimalChartOfAccountsVo();
				accountsVo.setId(rs.getInt(1));
				accountsVo.setName(rs.getString(2));
				accountsVo.setLevel("L5");
				accountsVo.setValue(accountsVo.getId());
				accountsVo.setType(rs.getString(3));
				minimalChartOfAccounts.add(accountsVo);
			}
		} catch (Exception e) {

			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);

		}
		return minimalChartOfAccounts;
	}
	
	public int getChartOfAccountsLevel5Id(int organizationId, String level3Name, String level4Name, String level5Name)
			throws ApplicationException {
		logger.info("Entry into method:getChartOfAccountsLevel5Id");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = getUserMgmConnection();
			int level3Id = 0;
			int level4Id = 0;
			int level5Id = 0;
			String query = ChartOfAccountsConstants.GET_LEVEL3_CHART_OF_ACCOUNTS_BY_NAME_ORGANIZATION;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, organizationId);
			preparedStatement.setString(2, level3Name);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				level3Id = rs.getInt(1);

			}
			closeResources(rs, preparedStatement, null);
			if (level3Id > 0) {
				query = ChartOfAccountsConstants.GET_LEVEL4_CHART_OF_ACCOUNTS_BY_ID_NAME_ORGANIZATION;
				preparedStatement = con.prepareStatement(query);
				preparedStatement.setInt(1, organizationId);
				preparedStatement.setInt(2, level3Id);
				preparedStatement.setString(3, level4Name);
				rs = preparedStatement.executeQuery();
				while (rs.next()) {
					level4Id = rs.getInt(1);

				}
			}
			closeResources(rs, preparedStatement, null);
			if (level4Id > 0) {
				query = ChartOfAccountsConstants.GET_LEVEL5_CHART_OF_ACCOUNTS_BY_ID_NAME_ORGANIZATION;
				preparedStatement = con.prepareStatement(query);
				preparedStatement.setInt(1, organizationId);
				preparedStatement.setInt(2, level4Id);
				preparedStatement.setString(3, level5Name);
				rs = preparedStatement.executeQuery();
				while (rs.next()) {
					level5Id = rs.getInt(1);
					return level5Id;
				}
			}
		} catch (Exception e) {

			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);

		}
		return 0;

	}

	/*public void createLevel6(String name, String description, int organizationId, String userId, boolean isSuperAdmin,
			int level5Id, boolean isBase, String displayName) throws ApplicationException {
		logger.info("Entry into method:createLevel6");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = getUserMgmConnection();

			String sql = ChartOfAccountsConstants.INSERT_INTO_CHART_OF_ACCOUNTS6_ORGANIZATION;

			preparedStatement = con.prepareStatement(sql);
			preparedStatement.setString(1, name);
			preparedStatement.setString(2, description);
			preparedStatement.setBoolean(3, isBase);
			preparedStatement.setInt(4, organizationId);
			preparedStatement.setInt(5, Integer.parseInt(userId));
			preparedStatement.setBoolean(6, isSuperAdmin);
			preparedStatement.setInt(7, level5Id);
			preparedStatement.setString(8, displayName);
			preparedStatement.executeUpdate();

		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);

		}

	}

	public int createLevel6ReturnId(String name, String description, int organizationId, String userId, boolean isSuperAdmin,
			int level5Id, boolean isBase, String displayName) throws ApplicationException {
		logger.info("Entry into method:createLevel6");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = getUserMgmConnection();

			String sql = ChartOfAccountsConstants.INSERT_INTO_CHART_OF_ACCOUNTS6_ORGANIZATION;

			preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setString(1, name);
			preparedStatement.setString(2, description);
			preparedStatement.setBoolean(3, isBase);
			preparedStatement.setInt(4, organizationId);
			preparedStatement.setInt(5, Integer.parseInt(userId));
			preparedStatement.setBoolean(6, isSuperAdmin);
			preparedStatement.setInt(7, level5Id);
			preparedStatement.setString(8, displayName);
			int rowAffected = preparedStatement.executeUpdate();
			if (rowAffected == 1) {
				rs = preparedStatement.getGeneratedKeys();
				if (rs.next()) {
					return rs.getInt(1);
				}
			}

		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);

		}
		return 0;

	}
*/
	public List<MinimalChartOfAccountsVo> getChartOfAccountsValues(Integer orgId, Connection con)
			throws SQLException, ApplicationException {
		logger.info("Entry into method:getChartOfAccountsValues");
		List<MinimalChartOfAccountsVo> minimalChartOfAccounts = new ArrayList<MinimalChartOfAccountsVo>();
		PreparedStatement preparedStatement =null;
		ResultSet rs = null;
		try  {
			preparedStatement = con.prepareStatement(ChartOfAccountsConstants.GET_CHART_OF_ACCOUNTS_FOR_DROPDOWN_ORGANIZATION);
			preparedStatement.setInt(1, orgId);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				MinimalChartOfAccountsVo accountsVo = new MinimalChartOfAccountsVo();
				Integer level6Id = rs.getInt(3);
				String level6Name = rs.getString(5) != null ? rs.getString(5) : rs.getString(4);
				if (level6Id != null && level6Name != null) {
					accountsVo.setId(level6Id);
					accountsVo.setName(rs.getString(2) + "~" + level6Name);
					accountsVo.setLevel("L6");
					accountsVo.setValue(level6Id);
					minimalChartOfAccounts.add(accountsVo);
				} else {
					accountsVo.setId(rs.getInt(1));
					accountsVo.setName(rs.getString(2));
					accountsVo.setLevel("L5");
					accountsVo.setValue(rs.getInt(1));
					minimalChartOfAccounts.add(accountsVo);
				}
			}
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, null);

		}
		logger.info("retrieved method:getChartOfAccountsValues of size::" + minimalChartOfAccounts.size());
		return minimalChartOfAccounts;
	}


	public List<MinimalChartOfAccountsVo> getAllCustomerEntityLevel5LedgersForOrg(int organizationId, Connection con) throws ApplicationException{
		logger.info("Entry into method: getAllCustomerEntityLedgersForOrg");
		List<MinimalChartOfAccountsVo> minimalChartOfAccounts = new ArrayList<MinimalChartOfAccountsVo>();
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			preparedStatement = con.prepareStatement(ChartOfAccountsConstants.GET_CUSTOMER_LEDGERS_FOR_LEVEL5_ORGANIZATION);
			preparedStatement.setInt(1, organizationId);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				MinimalChartOfAccountsVo accountsVo = new MinimalChartOfAccountsVo();
				accountsVo.setId(rs.getInt(1));
				accountsVo.setName(rs.getString(2));
				accountsVo.setLevel("L5");
				accountsVo.setValue(rs.getInt(1));

				minimalChartOfAccounts.add(accountsVo);
			}
		}catch(Exception e) {
			logger.error("Error in getAllCustomerEntityLedgersForOrg",e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, null);

		}

		return minimalChartOfAccounts;
	}

	public List<MinimalChartOfAccountsVo> getChartOfAccountsForGivenLevel2Type(String level2Type, Integer orgId,
			Connection con) throws SQLException, ApplicationException {
		logger.info("Entry into method:getChartOfAccountsForGivenLevel2Type");
		List<MinimalChartOfAccountsVo> minimalChartOfAccounts = new ArrayList<MinimalChartOfAccountsVo>();
		PreparedStatement preparedStatement =null;
		ResultSet rs = null;
		try {
			preparedStatement = con.prepareStatement(ChartOfAccountsConstants.GET_CHART_OF_ACCOUNTS_FOR_LEVEL2_GIVEN_TYPE_ORGANIZATION); 
			preparedStatement.setInt(1, orgId);
			preparedStatement.setString(2, level2Type);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				MinimalChartOfAccountsVo accountsVo = new MinimalChartOfAccountsVo();
				Integer level6Id = rs.getInt(3);
				String level6Name = rs.getString(4);
				if (level6Id != null && level6Name != null) {
					accountsVo.setId(level6Id);
					accountsVo.setName(rs.getString(2) + "~" + level6Name);
					accountsVo.setLevel("L6");
					accountsVo.setValue(level6Id);
					minimalChartOfAccounts.add(accountsVo);
				} else {
					accountsVo.setId(rs.getInt(1));
					accountsVo.setName(rs.getString(2));
					accountsVo.setLevel("L5");
					accountsVo.setValue(rs.getInt(1));
					minimalChartOfAccounts.add(accountsVo);
				}
			}
		}catch(Exception e) {
			logger.error("Error in getAllCustomerEntityLedgersForOrg",e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, null);

		}
		logger.info("retrieved getChartOfAccountsForGivenLevel2Type of size::" + minimalChartOfAccounts.size());
		return minimalChartOfAccounts;
	}

	public List<MinimalChartOfAccountsVo> getChartOfAccountsValuesLevel5(Integer orgId, Connection con)
			throws SQLException, ApplicationException {
		logger.info("Entry into method:getChartOfAccountsValuesLevel5");
		List<MinimalChartOfAccountsVo> minimalChartOfAccounts = new ArrayList<MinimalChartOfAccountsVo>();
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			String query = ChartOfAccountsConstants.GET_LEVEL5_CHART_OF_ACCOUNTS_ORGANIZATION;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, orgId);
			preparedStatement.setString(2, "DEL");
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				MinimalChartOfAccountsVo accountsVo = new MinimalChartOfAccountsVo();
				accountsVo.setId(rs.getInt(1));
				accountsVo.setName(rs.getString(2));
				accountsVo.setLevel("L5");
				accountsVo.setValue(accountsVo.getId());
				minimalChartOfAccounts.add(accountsVo);
			}
		} catch (Exception e) {

			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, null);

		}
		return minimalChartOfAccounts;
	}

	public List<MinimalChartOfAccountsLevel6Vo> getLevel6ChartOfAccounts(Integer orgId, Connection con)
			throws SQLException, ApplicationException {
		logger.info("Entry into method:getLevel6ChartOfAccounts");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;

		List<MinimalChartOfAccountsLevel6Vo> data = new ArrayList<MinimalChartOfAccountsLevel6Vo>();
		try {
			String query = ChartOfAccountsConstants.GET_LEVEL6_CHART_OF_ACCOUNTS_ORGANIZATION;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, orgId);
			preparedStatement.setString(2, "DEL");
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				MinimalChartOfAccountsLevel6Vo minimalChartOfAccountsLevel6Vo = new MinimalChartOfAccountsLevel6Vo();
				minimalChartOfAccountsLevel6Vo.setId(rs.getInt(1));
				minimalChartOfAccountsLevel6Vo.setName(rs.getString(3) != null ? rs.getString(3) : rs.getString(2));
				minimalChartOfAccountsLevel6Vo.setLevel5Id(rs.getInt(5));
				minimalChartOfAccountsLevel6Vo.setLevel("L6");
				minimalChartOfAccountsLevel6Vo.setValue(minimalChartOfAccountsLevel6Vo.getId());
				data.add(minimalChartOfAccountsLevel6Vo);
			}
		} catch (Exception e) {

			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, null);

		}
		return data;
	}
	
	
	public int getLevel4IdGivenName(Integer orgId, String name)
			throws ApplicationException {
		logger.info("Entry into method:getLevel6ChartOfAccounts");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection connection=null;
		try {
			String query = ChartOfAccountsConstants.GET_LEVEL4_CHART_OF_ACCOUNTS_NAME_ORGANIZATION;
			connection = getUserMgmConnection();
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setInt(1, orgId);
			preparedStatement.setString(2, name);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				return rs.getInt(1);
			}
		} catch (Exception e) {

			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, connection);

		}
		return 0;
	}

	
	public void createLevel5WithMinimumDetails(String name, int level4Id, String description,int organizationId, String userId, boolean isSuperAdmin,
			boolean isBase,int entityId,boolean mandatorySubLedger,String roleName) throws ApplicationException {
		logger.info("Entry into method:createLevel4WithMinimumDetails");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection connection = null;
		try {
			connection = getUserMgmConnection();
			String sql = ChartOfAccountsConstants.INSERT_INTO_CHART_OF_ACCOUNTS5_ORGANIZATION_ADDITIONAL;
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, name);
			preparedStatement.setInt(2, level4Id);
			preparedStatement.setString(3, description);
			preparedStatement.setBoolean(4, isBase);
			preparedStatement.setInt(5, organizationId);
			preparedStatement.setInt(6, Integer.parseInt(userId));
			preparedStatement.setBoolean(7, isSuperAdmin);
			preparedStatement.setInt(8, entityId);
			preparedStatement.setInt(9, 0);
			preparedStatement.setBoolean(10, mandatorySubLedger);
			preparedStatement.setString(11, roleName);
			preparedStatement.executeUpdate();

		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, connection);

		}

	}

	//Code for Hierachy Droop down that displays all the Hierachy in a single drop down 
	public List<ChartOfAccountsLevel1Vo> getChartOfAccountsByOrgId(Integer orgId) throws ApplicationException {
		logger.info("To getChartOfAccountsByOrgId::" + orgId);
		List<ChartOfAccountsLevel1Vo> level1List = null;
		Connection con = null;
		try {
			con = getUserMgmConnection();
			level1List = getChartOfAccountsLevel1ByOrgId(orgId, con);
			for (ChartOfAccountsLevel1Vo level1Vo : level1List) {
				List<ChartOfAccountsLevel2Vo> level2List = getChartOfAccountsLevel2ByLevel1Id(level1Vo.getId(), orgId,
						con);
				for (ChartOfAccountsLevel2Vo level2Vo : level2List) {
					List<ChartOfAccountsLevel3Vo> level3List = getChartOfAccountsLevel3ByLevel2Id(level2Vo.getId(),
							orgId, con);
					for (ChartOfAccountsLevel3Vo level3Vo : level3List) {
						List<ChartOfAccountsLevel4Vo> level4List = getChartOfAccountsLevel4ByLevel3Id(level3Vo.getId(),
								orgId, con);
						for (ChartOfAccountsLevel4Vo level4Vo : level4List) {
							List<ChartOfAccountsLevel5Vo> level5List = getChartOfAccountsLevel5ByLevel4Id(
									level4Vo.getId(), orgId, con);
							level4Vo.setChild(level5List);
						}
						level3Vo.setChild(level4List);
					}
					level2Vo.setChild(level3List);
				}
				level1Vo.setChild(level2List);
			}
			logger.info("Successfully getChartOfAccountsByOrgId::" + level1List);
		} catch (Exception e) {
			logger.info("Error in  getChartOfAccountsByOrgId ::", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(null, null, con);
		}

		return level1List;

	}

	public List<ChartOfAccountsLevel1Vo> getChartOfAccountsLevel1ByOrgId(Integer orgId, Connection con)
			throws ApplicationException {
		logger.info("To getChartOfAccountsLevel1ByOrgId::" + orgId);
		List<ChartOfAccountsLevel1Vo> level1List = null;
		PreparedStatement preparedStatement =null;
		ResultSet rs =null;
		try  {
			preparedStatement = con.prepareStatement(ChartOfAccountsConstants.GET_LEVEL1_CHART_OF_ACCOUNTS_BY_ORG_ID);
			preparedStatement.setInt(1, orgId);
			level1List = new ArrayList<ChartOfAccountsLevel1Vo>();
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				ChartOfAccountsLevel1Vo level1Vo = new ChartOfAccountsLevel1Vo();
				level1Vo.setId(rs.getInt(1));
				level1Vo.setName(rs.getString(2));
				level1List.add(level1Vo);
			}
			logger.info("Successfully  fetched  getChartOfAccountsLevel1ByOrgId::" + level1List.size());
		} catch (Exception e) {
			logger.info("Error in  getChartOfAccountsLevel1ByOrgId ::", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, null);

		}
		return level1List;
	}

	public List<ChartOfAccountsLevel2Vo> getChartOfAccountsLevel2ByLevel1Id(Integer level1Id, Integer orgId,
			Connection con) throws ApplicationException {
		logger.info("To getChartOfAccountsLevel2ByLevel1Id::" + orgId);
		List<ChartOfAccountsLevel2Vo> level2List = null;
		PreparedStatement preparedStatement =null;
		ResultSet rs =null;
		try{
			preparedStatement = con.prepareStatement(ChartOfAccountsConstants.GET_LEVEL2_CHART_OF_ACCOUNTS_BY_LEVEL1_ID_AND_ORG_ID); 
			preparedStatement.setInt(1, level1Id);
			preparedStatement.setInt(2, orgId);
			level2List = new ArrayList<ChartOfAccountsLevel2Vo>();
			logger.info("preparedStatement" + preparedStatement.toString());
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				ChartOfAccountsLevel2Vo level2Vo = new ChartOfAccountsLevel2Vo();
				level2Vo.setId(rs.getInt(1));
				level2Vo.setName(rs.getString(2));
				level2List.add(level2Vo);
			}
			logger.info("Successfully  fetched  getChartOfAccountsLevel2ByLevel1Id::" + level2List.size());
		} catch (Exception e) {
			logger.info("Error in  getChartOfAccountsLevel2ByLevel1Id ::", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, null);

		}
		return level2List;
	}

	public List<ChartOfAccountsLevel3Vo> getChartOfAccountsLevel3ByLevel2Id(Integer level2Id, Integer orgId,
			Connection con) throws ApplicationException {
		logger.info("To getChartOfAccountsLevel3ByLevel2Id::" + level2Id);
		List<ChartOfAccountsLevel3Vo> level3List = null;
		PreparedStatement preparedStatement =null;
		ResultSet rs =null;
		try {
			preparedStatement = con.prepareStatement(ChartOfAccountsConstants.GET_LEVEL3_CHART_OF_ACCOUNTS_BY_LEVEL2_ID_AND_ORG_ID); 
			preparedStatement.setInt(1, level2Id);
			preparedStatement.setInt(2, orgId);
			level3List = new ArrayList<ChartOfAccountsLevel3Vo>();
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				ChartOfAccountsLevel3Vo level3Vo = new ChartOfAccountsLevel3Vo();
				level3Vo.setId(rs.getInt(1));
				level3Vo.setName(rs.getString(2));
				level3List.add(level3Vo);
			}
			logger.info("Successfully  fetched  getChartOfAccountsLevel3ByLevel2Id::" + level3List.size());
		} catch (Exception e) {
			logger.info("Error in  getChartOfAccountsLevel3ByLevel2Id ::", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, null);

		}
		return level3List;
	}

	public List<ChartOfAccountsLevel4Vo> getChartOfAccountsLevel4ByLevel3Id(Integer level3Id, Integer orgId,
			Connection con) throws ApplicationException {
		logger.info("To getChartOfAccountsLevel4ByLevel3Id::" + level3Id);
		List<ChartOfAccountsLevel4Vo> level4List = null;
		PreparedStatement preparedStatement =null;
		ResultSet rs =null;
		try {
			preparedStatement = con.prepareStatement(ChartOfAccountsConstants.GET_LEVEL4_CHART_OF_ACCOUNTS_BY_LEVEL3_ID_AND_ORG_ID); 
			preparedStatement.setInt(1, level3Id);
			preparedStatement.setInt(2, orgId);
			level4List = new ArrayList<ChartOfAccountsLevel4Vo>();
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				ChartOfAccountsLevel4Vo level4Vo = new ChartOfAccountsLevel4Vo();
				level4Vo.setId(rs.getInt(1));
				level4Vo.setName(rs.getString(2));
				level4List.add(level4Vo);
			}
			logger.info("Successfully  fetched  getChartOfAccountsLevel4ByLevel3Id::" + level4List.size());
		} catch (Exception e) {
			logger.info("Error in  getChartOfAccountsLevel4ByLevel3Id ::", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, null);

		}
		return level4List;
	}

	public List<ChartOfAccountsLevel5Vo> getChartOfAccountsLevel5ByLevel4Id(Integer level4Id, Integer orgId,
			Connection con) throws ApplicationException {
		logger.info("To getChartOfAccountsLevel5ByLevel4Id::" + level4Id);
		List<ChartOfAccountsLevel5Vo> level5List = null;
		PreparedStatement preparedStatement =null;
		ResultSet rs =null;
		try{
			preparedStatement = con.prepareStatement(ChartOfAccountsConstants.GET_LEVEL5_CHART_OF_ACCOUNTS_BY_LEVEL4_ID_AND_ORG_ID); 
			preparedStatement.setInt(1, level4Id);
			preparedStatement.setInt(2, orgId);
			level5List = new ArrayList<ChartOfAccountsLevel5Vo>();
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				ChartOfAccountsLevel5Vo level5Vo = new ChartOfAccountsLevel5Vo();
				level5Vo.setId(rs.getInt(1));
				level5Vo.setName(rs.getString(2));
				level5List.add(level5Vo);
			}
			logger.info("Successfully  fetched  getChartOfAccountsLevel5ByLevel4Id::" + level5List.size());
		} catch (Exception e) {
			logger.info("Error in  getChartOfAccountsLevel5ByLevel4Id ::", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, null);

		}
		return level5List;
	}

	public List<ChartOfAccountsLevel6Vo> getChartOfAccountsLevel6ByLevel5Id(Integer level5Id, Integer orgId,
			Connection con) throws ApplicationException {
		logger.info("To getChartOfAccountsLevel6ByLevel5Id::" + level5Id);
		List<ChartOfAccountsLevel6Vo> level6List = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try  {
			preparedStatement = con.prepareStatement(ChartOfAccountsConstants.GET_LEVEL6_CHART_OF_ACCOUNTS_BY_LEVEL5_ID_AND_ORG_ID);
			preparedStatement.setInt(1, level5Id);
			preparedStatement.setInt(2, orgId);
			level6List = new ArrayList<ChartOfAccountsLevel6Vo>();
			logger.info("PrepStmrn::" + preparedStatement.toString());
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				ChartOfAccountsLevel6Vo level6Vo = new ChartOfAccountsLevel6Vo();
				level6Vo.setId(rs.getInt(1));
				level6Vo.setName(rs.getString(3) != null ? rs.getString(3) : rs.getString(2));
				level6List.add(level6Vo);
			}
			logger.info("Successfully  fetched  getChartOfAccountsLevel6ByLevel5Id::" + level6List.size());
		} catch (Exception e) {
			logger.info("Error in  getChartOfAccountsLevel6ByLevel5Id ::", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, null);

		}
		return level6List;
	}

	//TO get the minimum level 4 values based on Org Id
	public List<MinimalChartOfAccountsLevel4Vo> getAccountNamesByOrgId(Integer orgId, Connection con)
			throws ApplicationException {
		logger.info("To getAccountNamesByOrgId::" + orgId);
		List<MinimalChartOfAccountsLevel4Vo> level4List = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			preparedStatement = con.prepareStatement(ChartOfAccountsConstants.GET_ACCOUNT_NAME_BY_ORG_ID);
			preparedStatement.setInt(1, orgId);
			level4List = new ArrayList<MinimalChartOfAccountsLevel4Vo>();
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				MinimalChartOfAccountsLevel4Vo level4Vo = new MinimalChartOfAccountsLevel4Vo();
				level4Vo.setId(rs.getInt(1));
				level4Vo.setName(rs.getString(2));
				level4Vo.setValue(rs.getInt(1));
				level4Vo.setLevel3Id(rs.getInt(3));
				level4Vo.setLevel("L4");
				level4List.add(level4Vo);
			}
			logger.info("Successfully  fetched  getAccountNamesByOrgId::" + level4List.size());
		} catch (Exception e) {
			logger.info("Error in  getAccountNamesByOrgId ::", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, null);

		}
		return level4List;
	}

	// TO get the minimum level 3 values based on Org Id
	public List<MinimalChartOfAccountsLevel3Vo> getAccountGroupsByOrgId(Integer orgId, Connection con)
			throws ApplicationException {
		logger.info("To getAccountGroupsByOrgId::" + orgId);
		List<MinimalChartOfAccountsLevel3Vo> level3List = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			preparedStatement = con.prepareStatement(ChartOfAccountsConstants.GET_ACCOUNT_GROUP_BY_ORG_ID);
			preparedStatement.setInt(1, orgId);
			level3List = new ArrayList<MinimalChartOfAccountsLevel3Vo>();
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				MinimalChartOfAccountsLevel3Vo level3Vo = new MinimalChartOfAccountsLevel3Vo();
				level3Vo.setId(rs.getInt(1));
				level3Vo.setName(rs.getString(2));
				level3Vo.setValue(rs.getInt(1));
				level3Vo.setLevel2Id(rs.getInt(3));
				level3Vo.setLevel("L3");
				level3List.add(level3Vo);
			}
			logger.info("Successfully  fetched  getAccountGroupsByOrgId::" + level3List.size());
		} catch (Exception e) {
			logger.info("Error in  getAccountGroupsByOrgId ::", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, null);
		}
		return level3List;
	}

	//TO get the minimum level 2 values based on Org Id
	public List<MinimalChartOfAccountsLevel2Vo> getAccountTypesByOrgId(Integer orgId, Connection con)
			throws ApplicationException {
		logger.info("To getAccountTypesByOrgId::" + orgId);
		List<MinimalChartOfAccountsLevel2Vo> level2List = null;
		PreparedStatement preparedStatement =null;
		ResultSet rs =null;
		try {
			preparedStatement = con.prepareStatement(ChartOfAccountsConstants.GET_ACCOUNT_TYPE_BY_ORG_ID);
			preparedStatement.setInt(1, orgId);
			level2List = new ArrayList<MinimalChartOfAccountsLevel2Vo>();
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				MinimalChartOfAccountsLevel2Vo level2Vo = new MinimalChartOfAccountsLevel2Vo();
				level2Vo.setId(rs.getInt(1));
				level2Vo.setName(rs.getString(2));
				level2Vo.setValue(rs.getInt(1));
				level2Vo.setLevel1Id(rs.getInt(3));
				level2Vo.setLevel("L2");
				level2List.add(level2Vo);
			}
			logger.info("Successfully  fetched  getAccountGroupsByOrgId::" + level2List.size());
		} catch (Exception e) {
			logger.info("Error in  getAccountGroupsByOrgId ::", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, null);
		}
		return level2List;
	}

	public String getAccountTypeByLedgerId(Integer ledgerId) throws ApplicationException {
		String accountType = null;
		logger.info("To getAccountTypeByLedgerId::" + ledgerId);
		Connection con = null;
		PreparedStatement preparedStatement =null;
		ResultSet rs =null;
		try {
			con = getUserMgmConnection();
			preparedStatement = con.prepareStatement(ChartOfAccountsConstants.GET_ACCOUNT_TYPE_BY_LEDGER_ID);
			preparedStatement.setInt(1, ledgerId);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				accountType = rs.getString(1);
			}
		} catch (Exception e) {
			logger.info("Error in  getAccountTypeByLedgerId ::", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}

		return accountType;

	}

	public String getAccountTypeBySubLedgerId(Integer subLLedgerId) throws ApplicationException {
		String accountType = null;
		logger.info("To getAccountTypeBySubLedgerId::" + subLLedgerId);
		Connection con = null;
		PreparedStatement preparedStatement =null;
		ResultSet rs =null;
		try  {
			con = getUserMgmConnection();
			preparedStatement = con.prepareStatement(ChartOfAccountsConstants.GET_ACCOUNT_TYPE_BY_SUB_LEDGER_ID);
			preparedStatement.setInt(1, subLLedgerId);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				accountType = rs.getString(1);
			}
		} catch (Exception e) {
			logger.info("Error in  getAccountTypeBySubLedgerId ::", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return accountType;

	}

	public String getSubLedgerDescription(Integer subLedgerId, Integer orgId) throws ApplicationException {
		String ledger6Description = null;
		logger.info("To getSubLedgerDescription::" + subLedgerId);
		Connection con = null;
		PreparedStatement preparedStatement =null;
		ResultSet rs =null;
		if (subLedgerId != null && subLedgerId != 0) {
			try{
				con = getUserMgmConnection();
				preparedStatement = con.prepareStatement(ChartOfAccountsConstants.GET_SUBLEDGER_DESCRIPTION_DETAILS);
				preparedStatement.setInt(1, orgId);
				preparedStatement.setInt(2, subLedgerId);
				rs = preparedStatement.executeQuery();
				while (rs.next()) {
					ledger6Description = rs.getString(1) + "~" + rs.getString(2);
				}
				logger.info(" getSubLedgerDescription value is :: " + ledger6Description);
			} catch (Exception e) {
				logger.info("Error in  getSubLedgerDescription ::", e);
				throw new ApplicationException(e);
			} finally {
				closeResources(rs, preparedStatement, con);
			}
		}
		return ledger6Description;

	}

	public String getLedgerName(Integer ledgerId, Integer orgId) throws ApplicationException {
		String ledgerName = null;
		logger.info("To getLedgername::");
		Connection con =null;
		PreparedStatement preparedStatement =null;
		ResultSet rs = null;
		try {
			con = getUserMgmConnection();
			preparedStatement = con.prepareStatement(ChartOfAccountsConstants.GET_LEDGER_NAME);
			preparedStatement.setInt(1, orgId);
			preparedStatement.setInt(2, ledgerId);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				ledgerName = rs.getString(1);
			}
		} catch (Exception e) {
			logger.info("Error in  getLedgername ::", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return ledgerName;

	}

	public String getSubLedgeName(Integer subLedgerId, Integer orgId) throws ApplicationException {
		String subLedgerName = null;
		logger.info("To getSubLedgername::");
		Connection con =null;
		PreparedStatement preparedStatement =null;
		ResultSet rs = null;
		try  {
			con = getUserMgmConnection();
			preparedStatement = con.prepareStatement(ChartOfAccountsConstants.GET_SUB_LEDGER_NAME);
			preparedStatement.setInt(1, orgId);
			preparedStatement.setInt(2, subLedgerId);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				subLedgerName = rs.getString(1);
			}
		} catch (Exception e) {
			logger.info("Error in  getSubLedgername ::", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return subLedgerName;

	}

	//To get all the chart of Accounts level by Ledger Id 
	public ChartOfAccountsLedgerDetailsVo getChartOfAccountsByLedgerId(Integer ledgerId,Integer orgId) throws ApplicationException {
		logger.info("To get ChartOfAccountsLedgerDetailsVo for Ledger Id " + ledgerId);
		ChartOfAccountsLedgerDetailsVo ledgerDetailsVo = null;
		Connection connection =null;
		PreparedStatement preparedStatement =null;
		ResultSet rs = null;
		try{
			connection = getUserMgmConnection();
			preparedStatement = connection.prepareStatement(ChartOfAccountsConstants.GET_ALL_LEVELS_BY_ORG_ID_AND_LEDGER_ID);
			preparedStatement.setInt(1, orgId);
			preparedStatement.setInt(2, ledgerId);
			rs = preparedStatement.executeQuery();
			while(rs.next()) {
				ledgerDetailsVo = new ChartOfAccountsLedgerDetailsVo();
				ledgerDetailsVo.setLedgerId(rs.getInt(1));
				ledgerDetailsVo.setLedgerName(rs.getString(2));
				ledgerDetailsVo.setLedgerStatus(rs.getString(3));
				ledgerDetailsVo.setLevel4Name(rs.getString(4));
				ledgerDetailsVo.setLevel3Name(rs.getString(5));
				ledgerDetailsVo.setLevel2Name(rs.getString(6));
				ledgerDetailsVo.setLevel1Name(rs.getString(7));
				ledgerDetailsVo.setIsBase(rs.getBoolean(8));
			}
		}catch (Exception e) {
			logger.info("Error in  getChartOfAccountsByLedgerId ::", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, connection);
		}
		return ledgerDetailsVo;

	}


	public int getLedgerIdGivenName(String ledgerName,int organizationId) throws ApplicationException {
		logger.info("getLedgerIdGivenName");
		Connection con=null;
		PreparedStatement preparedStatement=null;
		ResultSet rs =null;
		int value=0;
		try {
			con=getUserMgmConnection();
			String query=ChartOfAccountsConstants.GET_LEDGER_ID_GIVEN_NAME_ORGANIZATION;
			preparedStatement=con.prepareStatement(query);
			preparedStatement.setInt(1,organizationId);
			preparedStatement.setString(2, ledgerName);
			rs = preparedStatement.executeQuery();	
			while(rs.next()){
				value=rs.getInt(1);
			}
		}catch(Exception e){
			throw new ApplicationException(e);
		}		

		finally{
			closeResources(rs, preparedStatement, con);
		}
		return value;
	}


	public List<CommonVo> getLedgerBySubLedegerDescription(Integer orgId , Integer subledgerId,String description) throws ApplicationException{
		List<CommonVo> ledgers = new ArrayList<CommonVo>();
		logger.info("To  getLedgerBySubLedegerDescription ::");
		Connection connection =null;
		PreparedStatement preparedStatement = null;
		ResultSet rs =null;
		try{
			connection = getUserMgmConnection();
			preparedStatement = connection.prepareStatement(ChartOfAccountsConstants.GET_LEDGER_BY_SUBLEDGER_DESCRIPTION);
			preparedStatement.setInt(1, orgId);
			preparedStatement.setString(2, description);
			preparedStatement.setInt(3, subledgerId);
			rs = preparedStatement.executeQuery();
			while(rs.next()) {
				CommonVo vo = new CommonVo();
				vo.setId(rs.getInt(1));
				vo.setName(rs.getString(2));
				vo.setValue(rs.getInt(1));
				ledgers.add(vo);
			}
			logger.info("fetched  getLedgerBySubLedegerDescription ::" + ledgers.size());
		}catch (Exception e) {
			logger.info("Error in  getLedgerBySubLedegerDescription ::", e);
			throw new ApplicationException(e);
		}
		finally{
			closeResources(rs, preparedStatement, connection);
		}
		return ledgers;

	}

	
	public List<MinimalChartOfAccountsVo> getLedgersBySubLedegerDescription(Integer orgId , String subledgerId,String description) throws ApplicationException{
		List<MinimalChartOfAccountsVo> ledgers = new ArrayList<MinimalChartOfAccountsVo>();
		logger.info("To  getLedgerBySubLedegerDescription ::"+subledgerId);
		Connection connection =null;
		PreparedStatement preparedStatement = null;
		ResultSet rs =null;
		try{
			connection = getUserMgmConnection();
			preparedStatement = connection.prepareStatement(ChartOfAccountsConstants.GET_LEDGER_BY_SUBLEDGER_DESCRIPTION);
			preparedStatement.setInt(1, orgId);
			preparedStatement.setString(2, description);
			preparedStatement.setString(3, subledgerId);
			rs = preparedStatement.executeQuery();
			while(rs.next()) {
				MinimalChartOfAccountsVo vo = new MinimalChartOfAccountsVo();
				vo.setId(rs.getInt(1));
				vo.setName(rs.getString(2));
				vo.setValue(rs.getInt(1));
				vo.setLevel("L5");
				ledgers.add(vo);
			}
			logger.info("fetched  getLedgerBySubLedegerDescription ::" + ledgers.size());
		}catch (Exception e) {
			logger.info("Error in  getLedgerBySubLedegerDescription ::", e);
			throw new ApplicationException(e);
		}
		finally{
			closeResources(rs, preparedStatement, connection);
		}
		return ledgers;

	}

	
	public List<MinimalChartOfAccountsVo> getLedgersAndItsSiblingsByLedgerName(Integer orgId , String ledgerName) throws ApplicationException{
		List<MinimalChartOfAccountsVo> ledgers = new ArrayList<MinimalChartOfAccountsVo>();
		logger.info("To  getLedgersAndItsSiblingsByLedgerName ::"+ledgerName);
		Connection connection =null;
		PreparedStatement preparedStatement = null;
		ResultSet rs =null;
		if(ledgerName!=null) {
		try{
			connection = getUserMgmConnection();
			preparedStatement = connection.prepareStatement(ChartOfAccountsConstants.GET_LEDGER_AND_SIBLINGS_BY_LEDGER_NAME);
			preparedStatement.setInt(1, orgId);
			preparedStatement.setInt(2, orgId);
			preparedStatement.setString(3, ledgerName);
			rs = preparedStatement.executeQuery();
			while(rs.next()) {
				MinimalChartOfAccountsVo vo = new MinimalChartOfAccountsVo();
				vo.setId(rs.getInt(1));
				vo.setName(rs.getString(2));
				vo.setValue(rs.getInt(1));
				vo.setLevel("L5");
				ledgers.add(vo);
			}
			logger.info("fetched  getLedgerBySubLedegerDescription ::" + ledgers.size());
		}catch (Exception e) {
			logger.info("Error in  getLedgerBySubLedegerDescription ::", e);
			throw new ApplicationException(e);
		}
		finally{
			closeResources(rs, preparedStatement, connection);
		}
		}
		return ledgers;

	}

	
	
	public Integer getAccountingEntryLedgerId(String ledger, Integer orgId) throws ApplicationException {
		logger.info("Enter into Method: getAccountingEntryLedgerId");
		Integer ledgerId = null;
		Connection con = null;
		PreparedStatement preparedStatement =null;
		ResultSet rs =null;
		try{
			con = getUserMgmConnection();
			preparedStatement = con.prepareStatement(ChartOfAccountsConstants.GET_ID_GIVEN_LEDGER_NAME_ORGANIZATION); 
			preparedStatement.setInt(1, orgId);
			preparedStatement.setString(2, ledger);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				ledgerId = rs.getInt(1);
			}
		} catch (Exception e) {
			logger.info("Error in  getAccountingEntryLedgerId:", e);
			throw new ApplicationException(e.getMessage());
		}
		finally{
			closeResources(rs, preparedStatement, con);
		}
		return ledgerId;

	}


	public Integer getAccountingEntryBySubLedgerId(String subLedger, Integer orgId) throws ApplicationException {
		logger.info("Enter into Method: getAccountingEntryLedgerId");
		Integer ledgerId = null;
		Connection con =null;
		PreparedStatement preparedStatement =null;
		ResultSet rs =null;
		try {
			con = getUserMgmConnection();
			preparedStatement = con.prepareStatement(ChartOfAccountsConstants.GET_ID_GIVEN_SUB_LEDGER_NAME_ORGANIZATION); 
			preparedStatement.setInt(1, orgId);
			preparedStatement.setString(2, subLedger);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				ledgerId = rs.getInt(1);
			}
		} catch (Exception e) {
			logger.info("Error in  getAccountingEntryBySubLedgerId:", e);
			throw new ApplicationException(e.getMessage());
		}
		finally{
			closeResources(rs, preparedStatement, con);
		}
		return ledgerId;

	}
	
	public List<MinimalChartOfAccountsLevel6Vo> getLedgerByType(int orgId,String type,int id) throws ApplicationException {
		logger.info("To getLedgerByType::");
		Connection con =null;
		PreparedStatement preparedStatement =null;
		ResultSet rs = null;
		List<MinimalChartOfAccountsLevel6Vo> data=new ArrayList<MinimalChartOfAccountsLevel6Vo>();
		try {
			con = getUserMgmConnection();
			preparedStatement = con.prepareStatement(ChartOfAccountsConstants.GET_SUB_LEDGER_BY_TYPE);
			preparedStatement.setInt(1, orgId);
			preparedStatement.setString(2, type);
			preparedStatement.setInt(3, id);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				MinimalChartOfAccountsLevel6Vo minimalChartOfAccountsLevel6Vo=new MinimalChartOfAccountsLevel6Vo();
				minimalChartOfAccountsLevel6Vo.setId(rs.getInt(1));
				minimalChartOfAccountsLevel6Vo.setValue(minimalChartOfAccountsLevel6Vo.getId());
				minimalChartOfAccountsLevel6Vo.setName(rs.getString(2));
				data.add(minimalChartOfAccountsLevel6Vo);
			}
		} catch (Exception e) {
			logger.info("Error in  getLedgername ::", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return data;

	}
	
	public List<ChartOfAccountsReportVo> getCoaReportOfAnOrganization(int orgId) throws ApplicationException {
		logger.info("To getCoaReportOfAnOrganization::");
		Connection con =null;
		PreparedStatement preparedStatement =null;
		ResultSet rs = null;
		List<ChartOfAccountsReportVo> data=new ArrayList<ChartOfAccountsReportVo>();
		try {
			con = getUserMgmConnection();
			preparedStatement = con.prepareStatement(ChartOfAccountsConstants.GET_COA_REPORT_BY_ORG);
			preparedStatement.setInt(1, orgId);
			logger.info(preparedStatement.toString());
			rs = preparedStatement.executeQuery();
			
			while (rs.next()) {
				ChartOfAccountsReportVo coaReport=new ChartOfAccountsReportVo();
				coaReport.setLevel1Name(rs.getString(1));
				coaReport.setLevel2Name(rs.getString(2));
				coaReport.setLevel3Name(rs.getString(3));
				coaReport.setLevel4Name(rs.getString(4));
				coaReport.setLevel5Name(rs.getString(5));
				coaReport.setEntityName(rs.getString(6));
				data.add(coaReport);
			}
		} catch (Exception e) {
			logger.info("Error in  getCoaReportOfAnOrganization ::", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return data;

	}
	
}

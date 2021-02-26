package com.blackstrawai.settings;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.common.BaseDao;
import com.blackstrawai.common.FinanceCommonDao;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Repository
public class ListPageCustomizationDao extends BaseDao{

	@Autowired
	FinanceCommonDao financeCommonDao;

	private Logger logger = Logger.getLogger(ListPageCustomizationDao.class);


	public ListPageCustomizationVo updateCustomization(ListPageCustomizationVo listPageCustomizationVo) throws ApplicationException {
		logger.info("Entry into method: updateCustomization");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = getUserMgmConnection();
			String sql = SettingsAndPreferencesConstants.UPDATE_LIST_PAGE_CUSTOMIZATION;
			preparedStatement = con.prepareStatement(sql);
			preparedStatement.setString(1, listPageCustomizationVo.getData());
			preparedStatement.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
			preparedStatement.setString(3, listPageCustomizationVo.getUpdateUserId());
			preparedStatement.setString(4,listPageCustomizationVo.getUpdateRoleName());
			preparedStatement.setInt(5, listPageCustomizationVo.getId());
			preparedStatement.executeUpdate();
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return listPageCustomizationVo;
	}



	public List<ListPageCustomizationVo> getAllCustomizationForUserAndRole(int organizationId,String userId,String roleName,String moduleName) throws ApplicationException {
		logger.info("Entry into method: getAllCustomizationForUserAndRole");
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		List<ListPageCustomizationVo> data = new ArrayList<ListPageCustomizationVo>();
		try {
			con = getUserMgmConnection();
			String query="";
			if(roleName.equals("Super Admin")){
				query = SettingsAndPreferencesConstants.GET_LIST_PAGE_CUSTOMIZATION_ORGANIZATION;
			}else{
				query = SettingsAndPreferencesConstants.GET_LIST_PAGE_CUSTOMIZATION_BY_ORGANIZATION_USER_ROLE;
			}
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, organizationId);
			preparedStatement.setString(2, moduleName);
			if(!(roleName.equals("Super Admin"))){
				preparedStatement.setString(3, userId);
				preparedStatement.setString(4, roleName);

			}	
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				ListPageCustomizationVo listPageCustomizationVo=new ListPageCustomizationVo();
				listPageCustomizationVo.setId(rs.getInt(1));
				listPageCustomizationVo.setData(rs.getString(2));
				listPageCustomizationVo.setStatus(rs.getString(3));
				listPageCustomizationVo.setOrganizationId(organizationId);
				listPageCustomizationVo.setRoleName(roleName);
				listPageCustomizationVo.setUserId(userId);
				listPageCustomizationVo.setModuleName(moduleName);
				data.add(listPageCustomizationVo);
			}
			if(data.size()==0){
				closeResources(rs, preparedStatement, null);		
				String payload=getCustomizePayload(moduleName);
				String sql = SettingsAndPreferencesConstants.INSERT_INTO_LIST_PAGE_CUSTOMIZATION;
				preparedStatement = con.prepareStatement(sql);
				preparedStatement.setString(1,payload);
				preparedStatement.setString(2,moduleName);
				preparedStatement.setString(3, "ACT");
				preparedStatement.setInt(4,organizationId);
				preparedStatement.setString(5, userId);
				preparedStatement.setString(6, roleName);
				preparedStatement.executeUpdate();			
				ListPageCustomizationVo listPageCustomizationVo=new ListPageCustomizationVo();
				listPageCustomizationVo.setModuleName(moduleName);
				listPageCustomizationVo.setOrganizationId(organizationId);
				listPageCustomizationVo.setRoleName(roleName);
				listPageCustomizationVo.setUserId(userId);
				listPageCustomizationVo.setStatus("ACT");
				listPageCustomizationVo.setData(payload);
				data.add(listPageCustomizationVo);
			}			

		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}

		return data;
	}

	private String getCustomizePayload(String moduleName)throws ApplicationException{
		Map<String,Boolean> data=new HashMap<String,Boolean>();
		String dataPayload="";
		if(moduleName.equals("AP-Payment")){
			data.put("paymentDate", true);
			data.put("paymentRefNo", true);
			data.put("invoiceNumber", true);
			data.put("paymentType", true);
			data.put("paidVia", true);
			data.put("contactList", true);
			data.put("status", true);
			data.put("amount", true);			
		}else if(moduleName.equals("AP-PO")){
			data.put("PO Number", true);
			data.put("Vendor Name", true);
			data.put("Date of Creation", true);
			data.put("Expected Delivery", true);
			data.put("Status", true);
			data.put("Amount", true);			
		}else if(moduleName.equals("AP-Invoice")){
			data.put("Vendor Name", true);
			data.put("Invoice No", true);
			data.put("PO Ref.No", true);
			data.put("Date of Creation", true);
			data.put("Due Date", true);
			data.put("Status", true);
			data.put("Balance Due", true);
			data.put("Action", true);
			data.put("Amount", true);			
		}else if(moduleName.equals("Vendor")){
			data.put("vendorDisplayName", true);
			data.put("primaryContact", true);
			data.put("phoneNumber", true);
			data.put("mobileNumber", true);
			data.put("noOfOpenPo", true);
			data.put("status", true);
			data.put("vendorId", true);
		}


		ObjectMapper objectMapper = new ObjectMapper();
		try {
			dataPayload = objectMapper.writeValueAsString(data);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			throw new ApplicationException(e);
		}


		return dataPayload;
	}

}


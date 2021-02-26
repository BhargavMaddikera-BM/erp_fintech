package com.blackstrawai.settings;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.common.BaseDao;

@Repository
public class VoucherDao extends BaseDao{
	
	private Logger logger = Logger.getLogger(VoucherDao.class);
	
	public VoucherVo createVoucher(VoucherVo voucherVo) throws ApplicationException{
		logger.info("Entry into method:createVoucher");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = getUserMgmConnection();
			boolean isVoucherExist=checkVoucherExist(voucherVo.getVoucherName(),voucherVo.getOrganizationId(),con);
			if(isVoucherExist){
				throw new Exception("Voucher Exist for the Organization");
			}
			String sql = SettingsAndPreferencesConstants.INSERT_INTO_VOUCHER_MANAGEMENT_ORGANIZATION;
			preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);	
			preparedStatement.setString(1,voucherVo.getType() );
			preparedStatement.setString(2, voucherVo.getDescription());
			preparedStatement.setString(3, voucherVo.getPrefix());
			preparedStatement.setString(4, voucherVo.getSuffix());
			preparedStatement.setInt(5,voucherVo.getOrganizationId());
			preparedStatement.setInt(6, Integer.valueOf(voucherVo.getUserId()));
			preparedStatement.setBoolean(7, voucherVo.getIsSuperAdmin());
			preparedStatement.setString(8, voucherVo.getVoucherName());
			preparedStatement.setString(9, voucherVo.getMinimumDigits());
			preparedStatement.setString(10, voucherVo.getMinimumNumberRange());
			preparedStatement.setString(11, voucherVo.getMaximumNumberRange());
			preparedStatement.setString(12, voucherVo.getAlertValue());
			preparedStatement.setBoolean(13, false);
			preparedStatement.setString(14, voucherVo.getRoleName());
			int rowAffected = preparedStatement.executeUpdate();
			if (rowAffected == 1) {
				rs = preparedStatement.getGeneratedKeys();
				if (rs.next()) {
					voucherVo.setId(rs.getInt(1));
				}
			}
			
		}catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}

		return voucherVo;
	}
	
	private boolean checkVoucherExist(String voucherName, Integer organizationId, Connection con)
			throws ApplicationException {
		logger.info("Entry into method:checkVoucherExist");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			String query = SettingsAndPreferencesConstants.CHECK_VOUCHER_EXIST_ORGANIZATION;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, organizationId);
			preparedStatement.setString(2, voucherName);
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

	public List<VoucherVo> getAllvouchersOfAnOrganization(int organizationId) throws ApplicationException{
		logger.info("Entry into method:getAllvouchersOfAnOrganization");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		List<VoucherVo> listOfVouchers=new ArrayList<VoucherVo>();
		try {
			con = getUserMgmConnection();
			String query = SettingsAndPreferencesConstants.GET_VOUCHERS_ORGANIZATION;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, organizationId);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				VoucherVo voucherVo=new VoucherVo();
				voucherVo.setId(rs.getInt(1));
				voucherVo.setType(rs.getString(2));
				voucherVo.setVoucherName(rs.getString(3));
				voucherVo.setDescription(rs.getString(4));
				voucherVo.setPrefix(rs.getString(5));
				voucherVo.setSuffix(rs.getString(6));
				voucherVo.setMinimumDigits(rs.getString(7));
				voucherVo.setMinimumNumberRange(rs.getString(8));
				voucherVo.setMaximumNumberRange(rs.getString(9));
				voucherVo.setAlertValue(rs.getString(10));
				voucherVo.setOrganizationId(rs.getInt(11));
				voucherVo.setUserId(rs.getString(12));
				voucherVo.setIsSuperAdmin(rs.getBoolean(13));
				voucherVo.setStatus(rs.getString(14));
				voucherVo.setCreateTs(rs.getTimestamp(15));
				listOfVouchers.add(voucherVo);
			}
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);

		}
		
		return listOfVouchers;
	}
	
	
	public List<VoucherVo> getAllvouchersOfAnOrganizationForUserAndRole(int organizationId,String userId,String roleName) throws ApplicationException{
		logger.info("Entry into method:getAllvouchersOfAnOrganizationForUserAndRole");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		List<VoucherVo> listOfVouchers=new ArrayList<VoucherVo>();
		try {
			con = getUserMgmConnection();
			String query="";
			if(roleName.equals("Super Admin")){
				 query = SettingsAndPreferencesConstants.GET_VOUCHERS_ORGANIZATION;
			}else{
				 query = SettingsAndPreferencesConstants.GET_VOUCHERS_ORGANIZATION_USER_ROLE;
			}
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, organizationId);
			if(!(roleName.equals("Super Admin"))){
				preparedStatement.setString(2, userId);
				preparedStatement.setString(3, roleName);
			}	
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				VoucherVo voucherVo=new VoucherVo();
				voucherVo.setId(rs.getInt(1));
				voucherVo.setType(rs.getString(2));
				voucherVo.setVoucherName(rs.getString(3));
				voucherVo.setDescription(rs.getString(4));
				voucherVo.setPrefix(rs.getString(5));
				voucherVo.setSuffix(rs.getString(6));
				voucherVo.setMinimumDigits(rs.getString(7));
				voucherVo.setMinimumNumberRange(rs.getString(8));
				voucherVo.setMaximumNumberRange(rs.getString(9));
				voucherVo.setAlertValue(rs.getString(10));
				voucherVo.setOrganizationId(rs.getInt(11));
				voucherVo.setUserId(rs.getString(12));
				voucherVo.setIsSuperAdmin(rs.getBoolean(13));
				voucherVo.setStatus(rs.getString(14));
				voucherVo.setCreateTs(rs.getTimestamp(15));
				listOfVouchers.add(voucherVo);
			}
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);

		}
		
		return listOfVouchers;
	}

	public VoucherVo getVoucherById(int voucherId) throws ApplicationException {
		logger.info("Entry into method:getShippingPreferenceById");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		VoucherVo voucherVo = new VoucherVo();
		try {
			con = getUserMgmConnection();
			String query = SettingsAndPreferencesConstants.GET_VOUCHER_BY_ID_ORGANIZATION;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1,voucherId);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				voucherVo.setId(rs.getInt(1));
				voucherVo.setType(rs.getString(2));
				voucherVo.setVoucherName(rs.getString(3));
				voucherVo.setDescription(rs.getString(4));
				voucherVo.setPrefix(rs.getString(5));
				voucherVo.setSuffix(rs.getString(6));
				voucherVo.setMinimumDigits(rs.getString(7));
				voucherVo.setMinimumNumberRange(rs.getString(8));
				voucherVo.setMaximumNumberRange(rs.getString(9));
				voucherVo.setAlertValue(rs.getString(10));
				voucherVo.setOrganizationId(rs.getInt(11));
				voucherVo.setUserId(rs.getString(12));
				voucherVo.setIsSuperAdmin(rs.getBoolean(13));
				voucherVo.setStatus(rs.getString(14));
				voucherVo.setCreateTs(rs.getTimestamp(15));
			}
		}catch (Exception e) {
				throw new ApplicationException(e);
			} finally {
				closeResources(rs, preparedStatement, con);

			}
		return voucherVo;
	}
	
	public VoucherVo deleteVoucher(int voucherId,String status,String userId,String roleName) throws ApplicationException {
		logger.info("Entry into method:deleteVoucher");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		VoucherVo voucherVo = new VoucherVo();
		try {
			con = getUserMgmConnection();
			String sql = SettingsAndPreferencesConstants.DELETE_VOUCHER_ORGANIZATION;
			preparedStatement = con.prepareStatement(sql);
			preparedStatement.setString(1, status);
			preparedStatement.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
			preparedStatement.setString(3, userId);
			preparedStatement.setString(4, roleName);
			preparedStatement.setInt(5, voucherId);
			voucherVo.setId(voucherId);
			preparedStatement.executeUpdate();
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);

		}
		return voucherVo;
	}
	
	public VoucherVo updateVoucher(VoucherVo voucherVo) throws ApplicationException {
		logger.info("Entry into method:updateVoucher");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = getUserMgmConnection();
			String sql = SettingsAndPreferencesConstants.UPDATE_VOUCHER_ORGANIZATION;
			preparedStatement = con.prepareStatement(sql);
			preparedStatement.setString(1, voucherVo.getType());
			preparedStatement.setString(2, voucherVo.getVoucherName());
			preparedStatement.setString(3, voucherVo.getDescription());
			preparedStatement.setString(4, voucherVo.getPrefix());
			preparedStatement.setString(5, voucherVo.getSuffix());
			preparedStatement.setString(6, voucherVo.getMinimumDigits());
			preparedStatement.setString(7, voucherVo.getMinimumNumberRange());
			preparedStatement.setString(8, voucherVo.getMaximumNumberRange());
			preparedStatement.setString(9, voucherVo.getAlertValue());
			preparedStatement.setInt(10, voucherVo.getOrganizationId());
			preparedStatement.setString(11, voucherVo.getUserId());
			preparedStatement.setBoolean(12, voucherVo.getIsSuperAdmin());
			preparedStatement.setString(13, voucherVo.getStatus());
			preparedStatement.setTimestamp(14, voucherVo.getUpdateTs());
			preparedStatement.setString(15, voucherVo.getRoleName());
			preparedStatement.setInt(16, voucherVo.getId());
			preparedStatement.executeUpdate();
		}catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return voucherVo;
	}
	
	public VoucherVo getVoucherBaseOnType(int organizationId,String type)throws ApplicationException{
		logger.info("Entry into method:getVoucherBaseOnType");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		VoucherVo voucherVo = new VoucherVo();
		try {
			con = getUserMgmConnection();
			String query = SettingsAndPreferencesConstants.GET_VOUCHER_DETAILS_ORGANIZATION;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1,organizationId);
			preparedStatement.setString(2, type);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				voucherVo.setId(rs.getInt(1));
				voucherVo.setPrefix(rs.getString(2));
				voucherVo.setSuffix(rs.getString(3));	
				voucherVo.setMinimumDigits(rs.getString(4));
			}
		}catch (Exception e) {
				throw new ApplicationException(e);
			} finally {
				closeResources(rs, preparedStatement, con);

			}
		return voucherVo;
		
	}
	
}

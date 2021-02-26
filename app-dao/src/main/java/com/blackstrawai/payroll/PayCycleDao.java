package com.blackstrawai.payroll;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.DateConverter;
import com.blackstrawai.common.BaseDao;
import com.blackstrawai.onboarding.OrganizationDao;
import com.blackstrawai.settings.SettingsAndPreferencesConstants;

@Repository
public class PayCycleDao extends BaseDao {
	
	@Autowired
	@Lazy
	private OrganizationDao organizationDao;
	
	private Logger logger = Logger.getLogger(PayCycleDao.class);
	public PayCycleVo createPayCycle(PayCycleVo payCycleVo) throws ApplicationException {

		logger.info("Entry into method: createPayCycle DAO");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;

		try {
			con = getPayrollConnection();
			con.setAutoCommit(false);

			boolean isPayPeriodExist = checkPayCycleExist(payCycleVo.getName(),
					payCycleVo.getOrganizationId(), con);
			if (isPayPeriodExist) {
				throw new Exception("Pay Cycle Exist for the Organization");
			}

			String sql = SettingsAndPreferencesConstants.INSERT_INTO_PAY_CYCLE_ORGANIZATION;
			preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setString(1, payCycleVo.getName());
			preparedStatement.setString(2, payCycleVo.getCycle());
			if (payCycleVo.getStartDate() != null)
				preparedStatement.setDate(3,
						DateConverter.getInstance().convertStringToDate(payCycleVo.getStartDate(), "yyyy-MM-dd"));
			if (payCycleVo.getEndDate() != null)
				preparedStatement.setDate(4,
						DateConverter.getInstance().convertStringToDate(payCycleVo.getEndDate(), "yyyy-MM-dd"));
			if (payCycleVo.getStatus() != null)
				preparedStatement.setString(5, payCycleVo.getStatus());
			else
				preparedStatement.setString(5, "ACT");
			
			preparedStatement.setInt(6, payCycleVo.getOrganizationId());
			preparedStatement.setString(7, payCycleVo.getUserId());
			preparedStatement.setString(8, payCycleVo.getRoleName());
			preparedStatement.setTimestamp(9, new Timestamp(System.currentTimeMillis()));

			int rowAffected = preparedStatement.executeUpdate();

			if (rowAffected == 1) {

				rs = preparedStatement.getGeneratedKeys();
				if (rs.next()) {
					payCycleVo.setId(rs.getInt(1));

				}
			}
			con.commit();
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return payCycleVo;

	}

	private boolean checkPayCycleExist(String name, int organizationId, Connection con) throws ApplicationException {
		logger.info("Entry into method: checkPayPeriodExist DAO");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			String query = SettingsAndPreferencesConstants.CHECK_PAY_CYCLE_EXIST_ORGANIZATION;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, organizationId);
			preparedStatement.setString(2, name);
			
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

	public List<PayCycleVo> getAllPayCyclesOfAnOrganization(int organizationId, String userId, String roleName) throws ApplicationException {

		logger.info("Entry into method: getAllPayCyclesOfAnOrganization");
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		List<PayCycleVo> listPayCycles = new ArrayList<PayCycleVo>();
		try {
			con = getPayrollConnection();
			String query = "";
			if (roleName.equals("Super Admin")) {
				query = SettingsAndPreferencesConstants.GET_PAY_CYCLE_BY_ORGANIZATION;
			} else {
				query = SettingsAndPreferencesConstants.GET_PAY_CYCLE_BY_ORGANIZATION_USER_ROLE;
			}
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, organizationId);
			preparedStatement.setString(2, "ACT");
			if (!(roleName.equals("Super Admin"))) {
				preparedStatement.setString(3, userId);
				preparedStatement.setString(4, roleName);
			}
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				PayCycleVo payCycleVo = new PayCycleVo();
				payCycleVo.setName(rs.getString(1));
				payCycleVo.setCycle(rs.getString(2));
				Connection con1=getUserMgmConnection();
				String dateFormat = organizationDao.getDefaultDateForOrganization(organizationId,
						con1);
				closeResources(null, null, con1);
				if (dateFormat != null && dateFormat.length() > 0) {
					payCycleVo.setStartDate(rs.getDate(3) != null
							? DateConverter.getInstance().correctDatePickerDateToStringPayPeriod(
									DateConverter.getInstance().convertDateToGivenFormat(rs.getDate(3), dateFormat), dateFormat)
							: null);
					payCycleVo.setEndDate(rs.getDate(4) != null
							? DateConverter.getInstance().correctDatePickerDateToStringPayPeriod(
									DateConverter.getInstance().convertDateToGivenFormat(rs.getDate(4), dateFormat), dateFormat)
							: null);
				} else {
					payCycleVo.setStartDate(DateConverter.getInstance().correctDatePickerDateToStringNonCorePayments(
							DateConverter.getInstance().convertDateToGivenFormat(rs.getDate(3), "yyyy-MM-dd")));
					payCycleVo.setEndDate(DateConverter.getInstance().correctDatePickerDateToStringNonCorePayments(
							DateConverter.getInstance().convertDateToGivenFormat(rs.getDate(4), "yyyy-MM-dd")));
				}

				payCycleVo.setStatus(rs.getString(5));
				payCycleVo.setId(rs.getInt(6));
				listPayCycles.add(payCycleVo);
			}
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return listPayCycles;
	
	}

	public PayCycleVo getPayCycleById(int id) throws ApplicationException {
		logger.info("Entry into method: getPayCycleById");
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		PayCycleVo payCycleVo = new PayCycleVo();
		try {
			con = getPayrollConnection();
			String query = SettingsAndPreferencesConstants.GET_PAY_CYCLE_BY_ID_ORGANIZATION;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, id);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				payCycleVo.setOrganizationId(rs.getInt(7));
				payCycleVo.setName(rs.getString(1));
				payCycleVo.setCycle(rs.getString(2));
				Connection con1=getUserMgmConnection();
				String dateFormat = organizationDao.getDefaultDateForOrganization(payCycleVo.getOrganizationId(),
						con1);
				closeResources(null, null, con1);
				if (dateFormat != null && dateFormat.length() > 0) {
					payCycleVo.setStartDate(rs.getDate(3) != null
							? DateConverter.getInstance().correctDatePickerDateToStringNonCorePayments(
									DateConverter.getInstance().convertDateToGivenFormat(rs.getDate(3), dateFormat))
							: null);
					payCycleVo.setEndDate(rs.getDate(4) != null
							? DateConverter.getInstance().correctDatePickerDateToStringNonCorePayments(
									DateConverter.getInstance().convertDateToGivenFormat(rs.getDate(4), dateFormat))
							: null);
				} else {
					payCycleVo.setStartDate(DateConverter.getInstance().correctDatePickerDateToStringNonCorePayments(
							DateConverter.getInstance().convertDateToGivenFormat(rs.getDate(3), "yyyy-MM-dd")));
					payCycleVo.setEndDate(DateConverter.getInstance().correctDatePickerDateToStringNonCorePayments(
							DateConverter.getInstance().convertDateToGivenFormat(rs.getDate(4), "yyyy-MM-dd")));
				}

				payCycleVo.setStatus(rs.getString(5));
				payCycleVo.setId(rs.getInt(6));
				payCycleVo.setUserId(rs.getString(8));
				payCycleVo.setRoleName(rs.getString(9));
				payCycleVo.setUpdateUserId(rs.getString(10));
				payCycleVo.setUpdateRoleName(rs.getString(11));
			}
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return payCycleVo;
	}

	public PayCycleVo deletePayCycle(int id, String status, String userId, String roleName, int organizationId) throws ApplicationException {

		logger.info("Entry into method: deletePayCycle");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		PayCycleVo payCycleVo = new PayCycleVo();
		try {
			con = getPayrollConnection();
			String sql = SettingsAndPreferencesConstants.DELETE_PAY_CYCLE;
			preparedStatement = con.prepareStatement(sql);
			preparedStatement.setString(1, status);
			preparedStatement.setString(2, userId);
			preparedStatement.setString(3, roleName);
			preparedStatement.setInt(4, organizationId);
			preparedStatement.setInt(5, id);
			preparedStatement.executeUpdate();
			payCycleVo.setId(id);
			return payCycleVo;
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}

	
	}

	public PayCycleVo updatePayCycle(PayCycleVo payCycleVo) throws ApplicationException {

		logger.info("Entry into method: updatePayCycle DAO");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = getPayrollConnection();
			con.setAutoCommit(false);
			
					boolean isPayCycleExists = checkPayCycleExist(payCycleVo.getName(),
							payCycleVo.getOrganizationId(), con);
					if (isPayCycleExists) {
						throw new Exception("Pay Cycle Exist for the Organization");
					}
				

				String sql = SettingsAndPreferencesConstants.UPDATE_PAY_CYCLE;
				preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				preparedStatement.setString(1, payCycleVo.getName());
				preparedStatement.setString(2, payCycleVo.getCycle());
				if (payCycleVo.getStartDate() != null)
					preparedStatement.setDate(3,
							DateConverter.getInstance().convertStringToDate(payCycleVo.getStartDate(), "yyyy-MM-dd"));
				else
					preparedStatement.setDate(3, null);
				if (payCycleVo.getEndDate() != null)
					preparedStatement.setDate(4,
							DateConverter.getInstance().convertStringToDate(payCycleVo.getEndDate(), "yyyy-MM-dd"));
				else
					preparedStatement.setDate(4, null);
				preparedStatement.setString(5, payCycleVo.getStatus());
				preparedStatement.setString(6, payCycleVo.getUserId());
				preparedStatement.setString(7, payCycleVo.getRoleName());
				preparedStatement.setInt(8, payCycleVo.getId());
				preparedStatement.executeUpdate();
				con.commit();

		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return payCycleVo;
	}

}

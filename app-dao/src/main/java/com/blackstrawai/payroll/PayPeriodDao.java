package com.blackstrawai.payroll;

import java.sql.Connection;
import java.sql.Date;
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
import com.blackstrawai.common.CommonConstants;
import com.blackstrawai.onboarding.OrganizationDao;
import com.blackstrawai.payroll.dropdowns.PayPeriodCycleVo;
import com.blackstrawai.settings.SettingsAndPreferencesConstants;

@Repository
public class PayPeriodDao extends BaseDao {

	private Logger logger = Logger.getLogger(PayPeriodDao.class);

	@Autowired
	@Lazy
	private OrganizationDao organizationDao;

	public PayPeriodVo createPayPeriod(PayPeriodVo payPeriodVo) throws ApplicationException {

		logger.info("Entry into method: createPayPeriod DAO");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;

		try {
			con = getPayrollConnection();
			con.setAutoCommit(false);
			for (PayFrequencyListVo freq : payPeriodVo.getPayFrequencyList()) {
				boolean isPayPeriodExist = checkPayPeriodExist(payPeriodVo.getPeriod(), freq.getName(),
						payPeriodVo.getOrganizationId(), con);
				if (isPayPeriodExist) {
					throw new Exception("Pay Period Exist for the Organization");
				}
			}

			String sql = SettingsAndPreferencesConstants.INSERT_INTO_PAY_PERIOD_FREQUENCY_ORGANIZATION;
			preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setString(1, payPeriodVo.getPeriod());
			preparedStatement.setInt(2, payPeriodVo.getOrganizationId());
			preparedStatement.setString(3, payPeriodVo.getUserId());
			preparedStatement.setString(4, payPeriodVo.getRoleName());
			preparedStatement.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
			preparedStatement.setString(6, "ACT");
//			preparedStatement.setString(7, payPeriodVo.getFrequency());
			int rowAffected = preparedStatement.executeUpdate();

			if (rowAffected == 1) {

				rs = preparedStatement.getGeneratedKeys();
				if (rs.next()) {
					payPeriodVo.setId(rs.getInt(1));

				}

				// Create Child Table: pay_period_frequency
				List<PayFrequencyListVo> payFrequencyList = payPeriodVo.getPayFrequencyList();
				if (payFrequencyList != null) {
					for (PayFrequencyListVo pay : payFrequencyList) {
						pay.setPayPeriodId(payPeriodVo.getId());
						createPayFrequencyChild(pay, con, payPeriodVo.getFrequency());
					}
				}
			}
			con.commit();
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return payPeriodVo;

	}

	private boolean checkPayPeriodExist(String payPeriodName, String payFrequencyName, Integer organizationId,
			Connection con) throws ApplicationException {
		logger.info("Entry into method: checkPayPeriodExist DAO");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			String query = SettingsAndPreferencesConstants.CHECK_PAY_PERIOD_FREQUENCY_EXIST_ORGANIZATION;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, organizationId);
			preparedStatement.setString(2, payPeriodName);
			preparedStatement.setString(3, payFrequencyName);
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

	public List<PayFrequencyListVo> getAllPayPeriodOfAnOrganization(int organizationId, String userId, String roleName)
			throws ApplicationException {
		logger.info("Entry into method: getAllPayPeriodsOfAnOrganization");
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		List<PayFrequencyListVo> listPayPeriods = new ArrayList<PayFrequencyListVo>();
		try {
			con = getPayrollConnection();
			String query = "";
			if (roleName.equals("Super Admin")) {
				query = SettingsAndPreferencesConstants.GET_PAY_PERIOD_BY_ORGANIZATION;
				;
			} else {
				query = SettingsAndPreferencesConstants.GET_PAY_PERIOD_BY_ORGANIZATION_USER_ROLE;
			}
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, organizationId);
			if (!(roleName.equals("Super Admin"))) {
				preparedStatement.setString(2, userId);
				preparedStatement.setString(3, roleName);
			}
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				PayFrequencyListVo payPeriodVo = new PayFrequencyListVo();
				payPeriodVo.setName(rs.getString(1));
				payPeriodVo.setCycle(rs.getString(2));
				Connection con1=getUserMgmConnection();
				String dateFormat = organizationDao.getDefaultDateForOrganization(organizationId,
						con1);
				closeResources(null, null, con1);
				if (dateFormat != null && dateFormat.length() > 0) {
					payPeriodVo
							.setStartDate(rs.getDate(3) != null
									? DateConverter.getInstance()
											.correctDatePickerDateToStringPayPeriod(DateConverter.getInstance()
													.convertDateToGivenFormat(rs.getDate(3), dateFormat), dateFormat)
									: null);
					payPeriodVo
							.setEndDate(rs.getDate(4) != null
									? DateConverter.getInstance()
											.correctDatePickerDateToStringPayPeriod(DateConverter.getInstance()
													.convertDateToGivenFormat(rs.getDate(4), dateFormat), dateFormat)
									: null);
				} else {
					payPeriodVo.setStartDate(DateConverter.getInstance().correctDatePickerDateToStringNonCorePayments(
							DateConverter.getInstance().convertDateToGivenFormat(rs.getDate(3), "yyyy-MM-dd")));
					payPeriodVo.setEndDate(DateConverter.getInstance().correctDatePickerDateToStringNonCorePayments(
							DateConverter.getInstance().convertDateToGivenFormat(rs.getDate(4), "yyyy-MM-dd")));
				}
				
				String status = rs.getString(5);
				if (status.trim().equalsIgnoreCase(CommonConstants.STATUS_AS_ACTIVE))
					payPeriodVo.setStatus(CommonConstants.DISPLAY_STATUS_AS_ACTIVE);
				else if (status.trim().equalsIgnoreCase(CommonConstants.STATUS_AS_INACTIVE))
					payPeriodVo.setStatus(CommonConstants.DISPLAY_STATUS_AS_INACTIVE);
				else
					payPeriodVo.setStatus(status);
				
				payPeriodVo.setId(rs.getInt(6));
				listPayPeriods.add(payPeriodVo);
			}
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return listPayPeriods;
	}

	public PayPeriodVo getPayPeriodById(int id) throws ApplicationException {
		logger.info("Entry into method: getPayPeriodById");
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		PayPeriodVo payPeriodVo = new PayPeriodVo();
		try {
			con = getPayrollConnection();
			String query = SettingsAndPreferencesConstants.GET_PAY_PERIOD_BY_ID_ORGANIZATION;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, id);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				payPeriodVo.setId(rs.getInt(1));
				payPeriodVo.setPeriod(rs.getString(2));
				payPeriodVo.setOrganizationId(rs.getInt(3));
				payPeriodVo.setRoleName(rs.getString(4));
				payPeriodVo.setUpdateRoleName(rs.getString(5));
				payPeriodVo.setUpdateUserId(rs.getString(6));
				payPeriodVo.setUserId(rs.getString(7));

				List<PayFrequencyListVo> fList = new ArrayList<PayFrequencyListVo>();
				PayFrequencyListVo payFrequency = new PayFrequencyListVo();
				payFrequency.setId(rs.getInt(8));
				payFrequency.setPayPeriodId(rs.getInt(9));
				payFrequency.setName(rs.getString(10));
				payPeriodVo.setName(rs.getString(10));
				payFrequency.setCycle(rs.getString(11));
				Date startDate = rs.getDate(12);
				if (startDate != null) {
					String startDateFormatted = DateConverter.getInstance()
							.correctDatePickerDateToStringNonCorePayments(
									DateConverter.getInstance().convertDateToGivenFormat(startDate, "yyyy-MM-dd"));
					payFrequency.setStartDate(startDateFormatted);
					payPeriodVo.setStartDate(startDateFormatted);
				}

				Date endDate = rs.getDate(13);
				if (endDate != null) {
					String endDateFormatted = DateConverter.getInstance().correctDatePickerDateToStringNonCorePayments(
							DateConverter.getInstance().convertDateToGivenFormat(endDate, "yyyy-MM-dd"));
					payFrequency.setEndDate(endDateFormatted);
					payPeriodVo.setEndDate(endDateFormatted);
				}
				payFrequency.setStatus(rs.getString(14));
				fList.add(payFrequency);
				payPeriodVo.setPayFrequencyList(fList);
				payPeriodVo.setFrequency(rs.getString(15));
				payPeriodVo.setCycle(rs.getString(11));
			}
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return payPeriodVo;
	}

	@SuppressWarnings("resource")
	public PayPeriodVo deletePayPeriod(int id, String status, String userId, String roleName, int organizationId)
			throws ApplicationException {
		logger.info("Entry into method: deletePayPeriod");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
//		PayPeriodVo payPeriodVo = new PayPeriodVo();
		try {
			con = getPayrollConnection();
			String sql = SettingsAndPreferencesConstants.DELETE_PAY_PERIOD_FREQUENCY;
			preparedStatement = con.prepareStatement(sql);
			preparedStatement.setString(1, status);
			preparedStatement.setInt(2, id);
			preparedStatement.executeUpdate();

			PayPeriodVo pay = getPayPeriodById(id);
			sql = SettingsAndPreferencesConstants.DELETE_PAY_PERIOD;
			preparedStatement = con.prepareStatement(sql);
			preparedStatement.setString(1, userId);
			preparedStatement.setString(2, roleName);
			preparedStatement.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
			preparedStatement.setInt(4, pay.getId());
			preparedStatement.executeUpdate();
			return pay;
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}

	}

	@SuppressWarnings("resource")
	public PayPeriodVo updatePayPeriod(PayPeriodVo payPeriodVo) throws ApplicationException {

		logger.info("Entry into method: updatePayPeriod DAO");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;

		try {
			con = getPayrollConnection();
			con.setAutoCommit(false);
			for (PayFrequencyListVo freq : payPeriodVo.getPayFrequencyList()) {
				if ((freq.getStatus() != null && freq.getStatus().equalsIgnoreCase("NEW")) || freq.getId() == null) {
					freq.setPayPeriodId(payPeriodVo.getId());
					boolean exists = checkPayPeriodExist(payPeriodVo.getPeriod(), freq.getName(),
							payPeriodVo.getOrganizationId(), con);
					if (exists)
						throw new Exception("Pay period already exists with same name");
					createPayFrequencyChild(freq, con, payPeriodVo.getFrequency());
				} else {
					if (!getPayPeriodById(freq.getId()).getPayFrequencyList().get(0).getName().equals(freq.getName())) {
						boolean isPayPeriodExist = checkPayPeriodExist(payPeriodVo.getPeriod(), freq.getName(),
								payPeriodVo.getOrganizationId(), con);
						if (isPayPeriodExist) {
							throw new Exception("Pay Period Exist for the Organization");
						}
					}

					String sql = SettingsAndPreferencesConstants.UPDATE_PAY_PERIOD;
					preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
					preparedStatement.setString(1, freq.getName());
					preparedStatement.setString(2, freq.getCycle());
					if (freq.getStartDate() != null)
						preparedStatement.setDate(3,
								DateConverter.getInstance().convertStringToDate(freq.getStartDate(), "yyyy-MM-dd"));
					else
						preparedStatement.setDate(3, null);
					if (freq.getEndDate() != null)
						preparedStatement.setDate(4,
								DateConverter.getInstance().convertStringToDate(freq.getEndDate(), "yyyy-MM-dd"));
					else
						preparedStatement.setDate(4, null);
					preparedStatement.setString(5, freq.getStatus());
					preparedStatement.setInt(6, freq.getId());
					int rowAffected = preparedStatement.executeUpdate();

					if (rowAffected == 1) {
						sql = SettingsAndPreferencesConstants.DELETE_PAY_PERIOD;
						preparedStatement = con.prepareStatement(sql);
						preparedStatement.setString(1, payPeriodVo.getUserId());
						preparedStatement.setString(2, payPeriodVo.getRoleName());
						preparedStatement.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
						preparedStatement.setInt(4, freq.getPayPeriodId());
						preparedStatement.executeUpdate();
					}
				}
			}
			con.commit();
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return payPeriodVo;
	}

	private void createPayFrequencyChild(PayFrequencyListVo pay, Connection con, String frequency)
			throws ApplicationException {
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			String sql = SettingsAndPreferencesConstants.INSERT_INTO_PAY_PERIOD_FREQUENCY_CHILD_ORGANIZATION;
			preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setString(1, pay.getName());
			preparedStatement.setString(2, pay.getCycle());
			if (pay.getStartDate() != null)
				preparedStatement.setDate(3,
						DateConverter.getInstance().convertStringToDate(pay.getStartDate(), "yyyy-MM-dd"));
			if (pay.getEndDate() != null)
				preparedStatement.setDate(4,
						DateConverter.getInstance().convertStringToDate(pay.getEndDate(), "yyyy-MM-dd"));
			preparedStatement.setInt(5, pay.getPayPeriodId());
			preparedStatement.setString(6, frequency);
			String status = pay.getStatus() != null ? pay.getStatus() : "ACT";
			preparedStatement.setString(7, status);
			preparedStatement.executeUpdate();
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, null);
		}
	}

	public List<PayPeriodCycleVo> getPayPeriodCycles(int organizationId) throws ApplicationException {

		List<PayPeriodCycleVo> periods = new ArrayList<PayPeriodCycleVo>();
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			con = getPayrollConnection();
			preparedStatement = con.prepareStatement(SettingsAndPreferencesConstants.GET_PAY_PERIOD_CYCLES);
			preparedStatement.setInt(1, organizationId);
			rs = preparedStatement.executeQuery();

			while (rs.next()) {
				PayPeriodCycleVo period = new PayPeriodCycleVo();
				period.setId(rs.getInt(1));
				period.setName(rs.getString(2));
				period.setCycle(rs.getString(3));
				if (rs.getDate(4) != null)
					period.setStartDate(DateConverter.getInstance().correctDatePickerDateToStringNonCorePayments(
							DateConverter.getInstance().convertDateToGivenFormat(rs.getDate(4), "yyyy-MM-dd")));
				if (rs.getDate(5) != null)
					period.setEndDate(DateConverter.getInstance().correctDatePickerDateToStringNonCorePayments(
							DateConverter.getInstance().convertDateToGivenFormat(rs.getDate(5), "yyyy-MM-dd")));
				period.setValue(rs.getInt(1));
				periods.add(period);
			}
			logger.info("Fetched in getPayPeriodCycles:" + periods);
		} catch (Exception e) {
			logger.info("Error in getPayPeriodCycles", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return periods;

	}

}
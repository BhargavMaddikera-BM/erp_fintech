package com.blackstrawai.vendorsettings;

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

@Repository
public class RecordsSubmittedDao extends BaseDao {

	private Logger logger = Logger.getLogger(RecordsSubmittedDao.class);

	public List<RecordsSubmittedVo> getCountOfRecordsSubmitted(int organizationId) throws ApplicationException {
		logger.info("Entry into Method: getCountOfRecordsSubmitted");
		Connection con = null;
		List<RecordsSubmittedVo> recordsSubmittedVo = new ArrayList<RecordsSubmittedVo>();
		try {
			con = getAccountsPayable();
			recordsSubmittedVo.add(getBalanceConfirmationQuickCount(organizationId, con));
			recordsSubmittedVo.add(getBalanceConfirmationDetailedCount(organizationId, con));
			recordsSubmittedVo.add(getInvoiceQuickCount(organizationId, con));
			recordsSubmittedVo.add(getInvoiceDetailedCount(organizationId, con));
			recordsSubmittedVo.add(getVendorOnboardingQuickCount(organizationId, con));
			recordsSubmittedVo.add(getVendorOnboardingDetailedCount(organizationId, con));
		} catch (Exception e) {
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(null, null, con);

		}

		return recordsSubmittedVo;

	}

	private RecordsSubmittedVo getInvoiceDetailedCount(int organizationId, Connection con) throws ApplicationException {
		logger.info("Entry into Method: getInvoiceDetailedCount");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		RecordsSubmittedVo recordsSubmittedVo = new RecordsSubmittedVo();
		recordsSubmittedVo.setModuleName(CommonConstants.MODULE_NAME_INVOICE);
		recordsSubmittedVo.setTypeName(CommonConstants.TYPE_NAME_DETAILED);
		recordsSubmittedVo.setId(1);
		try {
			String query = RecordsSubmittedConstants.INVOICE_DETAILED_COUNT;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, organizationId);
			rs = preparedStatement.executeQuery();
			if (rs.next()) {
				recordsSubmittedVo.setTotalCount(rs.getString(1));
			}
		} catch (Exception e) {
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, null);

		}
		return recordsSubmittedVo;
	}

	private RecordsSubmittedVo getInvoiceQuickCount(int organizationId, Connection con) throws ApplicationException {
		logger.info("Entry into Method: getInvoiceQuickCount");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		RecordsSubmittedVo recordsSubmittedVo = new RecordsSubmittedVo();
		recordsSubmittedVo.setModuleName(CommonConstants.MODULE_NAME_INVOICE);
		recordsSubmittedVo.setTypeName(CommonConstants.TYPE_NAME_QUICK);
		recordsSubmittedVo.setId(2);
		try {
			String query = RecordsSubmittedConstants.INVOICE_QUICK_COUNT;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, organizationId);
			rs = preparedStatement.executeQuery();
			if (rs.next()) {
				recordsSubmittedVo.setTotalCount(rs.getString(1));
			}
		} catch (Exception e) {
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, null);

		}
		return recordsSubmittedVo;
	}

	private RecordsSubmittedVo getBalanceConfirmationQuickCount(int organizationId, Connection con)
			throws ApplicationException {
		logger.info("Entry into Method: getBalanceConfirmationQuickCount");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		RecordsSubmittedVo recordsSubmittedVo = new RecordsSubmittedVo();
		recordsSubmittedVo.setModuleName(CommonConstants.MODULE_NAME_BALANCE_CONFIRMATION);
		recordsSubmittedVo.setTypeName(CommonConstants.TYPE_NAME_QUICK);
		recordsSubmittedVo.setId(3);
		try {
			String query = RecordsSubmittedConstants.BALANCE_CONFIRMATION_QUICK_COUNT;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, organizationId);
			rs = preparedStatement.executeQuery();
			if (rs.next()) {
				recordsSubmittedVo.setTotalCount(rs.getString(1));
			}
		} catch (Exception e) {
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, null);

		}
		return recordsSubmittedVo;
	}

	private RecordsSubmittedVo getBalanceConfirmationDetailedCount(int organizationId, Connection con)
			throws ApplicationException {
		logger.info("Entry into Method: getBalanceConfirmationDetailedCount");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		RecordsSubmittedVo recordsSubmittedVo = new RecordsSubmittedVo();
		recordsSubmittedVo.setModuleName(CommonConstants.MODULE_NAME_BALANCE_CONFIRMATION);
		recordsSubmittedVo.setTypeName(CommonConstants.TYPE_NAME_DETAILED);
		recordsSubmittedVo.setId(4);
		try {
			String query = RecordsSubmittedConstants.BALANCE_CONFIRMATION_DETAILED_COUNT;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, organizationId);
			rs = preparedStatement.executeQuery();
			if (rs.next()) {
				recordsSubmittedVo.setTotalCount(rs.getString(1));
			}
		} catch (Exception e) {
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, null);

		}
		return recordsSubmittedVo;
	}

	private RecordsSubmittedVo getVendorOnboardingQuickCount(int organizationId, Connection con)
			throws ApplicationException {
		logger.info("Entry into Method: getVendorOnboardingQuickCount");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		RecordsSubmittedVo recordsSubmittedVo = new RecordsSubmittedVo();
		recordsSubmittedVo.setModuleName(CommonConstants.MODULE_NAME_VENDOR_ONBOARDING);
		recordsSubmittedVo.setTypeName(CommonConstants.TYPE_NAME_QUICK);
		recordsSubmittedVo.setId(5);
		try {
			String query = RecordsSubmittedConstants.VENDOR_ONBOARDING_QUICK_COUNT;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, organizationId);
			rs = preparedStatement.executeQuery();
			if (rs.next()) {
				recordsSubmittedVo.setTotalCount(rs.getString(1));
			}

		} catch (Exception e) {
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, null);

		}
		return recordsSubmittedVo;
	}

	private RecordsSubmittedVo getVendorOnboardingDetailedCount(int organizationId, Connection con)
			throws ApplicationException {
		logger.info("Entry into Method: getVendorOnboardingDetailedCount");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		RecordsSubmittedVo recordsSubmittedVo = new RecordsSubmittedVo();
		recordsSubmittedVo.setModuleName(CommonConstants.MODULE_NAME_VENDOR_ONBOARDING);
		recordsSubmittedVo.setTypeName(CommonConstants.TYPE_NAME_DETAILED);
		recordsSubmittedVo.setId(6);
		try {
			String query = RecordsSubmittedConstants.VENDOR_ONBOARDING_DETAILED_COUNT;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, organizationId);
			rs = preparedStatement.executeQuery();
			if (rs.next()) {
				recordsSubmittedVo.setTotalCount(rs.getString(1));
			}

		} catch (Exception e) {
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, null);

		}
		return recordsSubmittedVo;
	}
}

package com.blackstrawai.settings;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.common.BaseDao;

@Repository
public class TaxRateTypeDao extends BaseDao {

	private Logger logger = Logger.getLogger(TaxRateTypeDao.class);

	public TaxRateTypeVo getTaxRateTypeById(int id) throws ApplicationException {
		logger.info("Entry into method: getTaxRateTypeById");
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		TaxRateTypeVo taxRateTypeVo = new TaxRateTypeVo();
		try {
			con = getUserMgmConnection();
			String query = SettingsAndPreferencesConstants.GET_TAX_RATE_TYPE_BY_ID;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, id);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				taxRateTypeVo.setId(rs.getInt(1));
				taxRateTypeVo.setBase(rs.getBoolean(2));
				taxRateTypeVo.setType(rs.getString(3));
				taxRateTypeVo.setStatus(rs.getString(4));
				taxRateTypeVo.setOrganizationId(rs.getInt(5));
				taxRateTypeVo.setUserId(rs.getString(6));
				taxRateTypeVo.setSuperAdmin(rs.getBoolean(7));
				taxRateTypeVo.setCreateTs(rs.getTimestamp(8));
				taxRateTypeVo.setIsInter(rs.getString(9));
				taxRateTypeVo.setUpdateTs(rs.getTimestamp(10));
				taxRateTypeVo.setUsageType(rs.getString(11));
			}
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return taxRateTypeVo;
	}

}

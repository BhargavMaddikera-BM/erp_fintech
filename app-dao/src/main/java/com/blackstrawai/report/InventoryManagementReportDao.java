package com.blackstrawai.report;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.common.BaseDao;
import com.blackstrawai.common.CommonConstants;
import com.blackstrawai.inventorymgmt.InventoryManagementConstants;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.math.MathContext;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class InventoryManagementReportDao extends BaseDao {

	private Logger logger = Logger.getLogger(InventoryManagementReportDao.class);


	public InventoryMgmtReportVo getInventoryManagementReport(Integer orgId, String startDate,
															  String endDate, Integer rateType,int periodId) throws ApplicationException {
		logger.info("To get Inventory management report for the org " + orgId + " between "+ startDate + " and "+endDate);
		InventoryMgmtReportVo inventoryMgmtReportVo = null;
		if (orgId != null  && rateType != null && (startDate != null &&  endDate != null || periodId==1)) {
			//Connection con = null;
			try  {
				//	con = getInventoryMgmtConnection();
				inventoryMgmtReportVo = new InventoryMgmtReportVo();
				inventoryMgmtReportVo.setOrgId(orgId);
				inventoryMgmtReportVo.setStartDate(startDate);
				inventoryMgmtReportVo.setEndDate(endDate);
				//List<InventoryMgmtReportGeneralVo> invMgmtReportData = getInventoryManagementReportData(orgId,startDate,endDate,rateType, con);
				List<InventoryMgmtReportGeneralVo> invMgmtReportData = getInventoryManagementReportDataV2(inventoryMgmtReportVo,rateType,periodId);
				inventoryMgmtReportVo.setInventoryMgmtReportData(invMgmtReportData);
			} catch (ApplicationException e) {
				logger.info("Error in getInventoryManagementReport:: ", e);
				throw e;
			}/*finally{
				closeResources(null, null, con);
			}*/
		}
		return inventoryMgmtReportVo;
	}


	private List<InventoryMgmtReportGeneralVo> getInventoryManagementReportDataV2(
			InventoryMgmtReportVo inventoryMgmtReportVo, Integer rateType, int periodId) throws ApplicationException {

		logger.info(
				"To get  in getInventoryManagementReport  for the org "
						+ inventoryMgmtReportVo.getOrgId()
						+ " between "
						+ inventoryMgmtReportVo.getStartDate()
						+ " and "
						+ inventoryMgmtReportVo.getEndDate());

		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		List<InventoryMgmtReportGeneralVo> inventoryMgmtReportGeneralVoList;

		try {
			con = getInventoryMgmtConnection();

			// products list
			String prod_list_query =
					InventoryManagementConstants.GET_INV_MGMT_REPORT_DATA_PRODUCTS_LIST_V2;

			if (periodId == 1) {
				prod_list_query =
						InventoryManagementConstants.GET_INV_MGMT_REPORT_DATA_PRODUCTS_LIST_GETALL_V2;
			}

			preparedStatement = con.prepareStatement(prod_list_query);
			preparedStatement.setInt(1, inventoryMgmtReportVo.getOrgId());
			preparedStatement.setString(2, CommonConstants.STATUS_AS_ACTIVE);
			if (periodId > 1) {
				preparedStatement.setString(3, inventoryMgmtReportVo.getStartDate());
				preparedStatement.setString(4, inventoryMgmtReportVo.getEndDate());
			}

			rs = preparedStatement.executeQuery();

			inventoryMgmtReportGeneralVoList = getProductItemslist(rs);
			List<String> productIdsParamList = new ArrayList<>();

			inventoryMgmtReportGeneralVoList.forEach(
					productIdsList -> productIdsParamList.add(productIdsList.getId()));

			if (inventoryMgmtReportGeneralVoList.isEmpty()) {
				throw new ApplicationException("No Data found");
			}

			String prodId;
			// Quantity Sold
			String qs_stmt = InventoryManagementConstants.GET_INV_MGMT_REPORT_DATA_QUANTITY_SOLD_V2;
			if (periodId == 1) {
				qs_stmt = InventoryManagementConstants.GET_INV_MGMT_REPORT_DATA_QUANTITY_SOLD_GETALL_V2;
			}

			closeResources(rs, preparedStatement, null);
			rs = getResultSet(productIdsParamList, qs_stmt, inventoryMgmtReportVo, con, periodId);

			while (rs.next()) {
				prodId = Integer.toString(rs.getInt("product_id"));
				for (InventoryMgmtReportGeneralVo data : inventoryMgmtReportGeneralVoList) {
					if (data.getId().equals(prodId)) {
						data.setQuantitySold(rs.getBigDecimal("quantity_sold"));
					}
				}
			}

			// Quantity Bought
			String qb_stmt = InventoryManagementConstants.GET_INV_MGMT_REPORT_DATA_QUANTITY_BOUGHT_V2;
			if (periodId == 1) {
				qb_stmt = InventoryManagementConstants.GET_INV_MGMT_REPORT_DATA_QUANTITY_BOUGHT_GETALL_V2;
			}
			closeResources(rs, preparedStatement, null);
			rs = getResultSet(productIdsParamList, qb_stmt, inventoryMgmtReportVo, con, periodId);

			while (rs.next()) {
				prodId = Integer.toString(rs.getInt("product_id"));
				for (InventoryMgmtReportGeneralVo data : inventoryMgmtReportGeneralVoList) {
					if (data.getId().equals(prodId)) {
						data.setQuantityBought(rs.getBigDecimal("quantity_bought"));
					}
				}
			}

			String rate_type_query;
			if (rateType == 0) {
				rate_type_query = InventoryManagementConstants.GET_INV_MGMT_REPORT_DATA_RATE_FIFO_V2;
				if (periodId == 1) {
					rate_type_query =
							InventoryManagementConstants.GET_INV_MGMT_REPORT_DATA_RATE_FIFO_GETALL_V2;
				}
			} else {
				logger.info("WA Query");
				rate_type_query = InventoryManagementConstants.GET_INV_MGMT_REPORT_DATA_RATE_WA_V2;
				if (periodId == 1) {
					rate_type_query = InventoryManagementConstants.GET_INV_MGMT_REPORT_DATA_RATE_WA_GETALL_V2;
				}
			}

			closeResources(rs, preparedStatement, null);
			rs = getResultSet(productIdsParamList, rate_type_query, inventoryMgmtReportVo, con, periodId);

			while (rs.next()) {
				prodId = Integer.toString(rs.getInt("product_id"));
				for (InventoryMgmtReportGeneralVo data : inventoryMgmtReportGeneralVoList) {
					if (data.getId().equals(prodId)) {
						data.setStockRate(rs.getBigDecimal("stock_rate"));
					}
				}
			}

			for (InventoryMgmtReportGeneralVo data : inventoryMgmtReportGeneralVoList) {
				logger.info("Data " + data);

				BigDecimal closingStock = data.getQuantityBought().subtract(data.getQuantitySold());
				data.setClosingStock(closingStock);

				BigDecimal closingStockValue;

				if (data.getStockRate().compareTo(BigDecimal.ZERO) == 0.00) {
					closingStockValue = closingStock.multiply(data.getOpeningStockValue());
				} else {
					if (rateType == 0) {
						closingStockValue = closingStock.multiply(data.getStockRate());
					} else {
						closingStockValue =
								data.getStockRate()
										.divide(data.getQuantityBought(), MathContext.DECIMAL128)
										.multiply(closingStock);
					}

					data.setClosingStockValue(closingStockValue);
				}

				data.setQuantityBought(data.getQuantityBought().subtract(data.getOpeningStock()));
			}

			logger.info(
					"Successfully fetched  getInventoryManagementReportData "
							+ inventoryMgmtReportGeneralVoList);

		} catch (Exception e) {
			logger.error("Exception in getInventoryManagementReportData ", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}

		return inventoryMgmtReportGeneralVoList;
	}

	private List<InventoryMgmtReportGeneralVo> getProductItemslist(ResultSet rs) throws SQLException {

		InventoryMgmtReportGeneralVo inventoryMgmtReportGeneralVo;
		List<InventoryMgmtReportGeneralVo> inventoryMgmtReportGeneralVoList = new ArrayList<>();

		while (rs.next()) {
			inventoryMgmtReportGeneralVo = new InventoryMgmtReportGeneralVo();
			inventoryMgmtReportGeneralVo.setId(rs.getString("id"));
			inventoryMgmtReportGeneralVo.setProductId(rs.getString("product_id"));
			inventoryMgmtReportGeneralVo.setProductCategory(rs.getString("product_category_name"));
			inventoryMgmtReportGeneralVo.setProductName(rs.getString("product_name"));
			inventoryMgmtReportGeneralVo.setOpeningStock(rs.getBigDecimal("opening_stock"));
			inventoryMgmtReportGeneralVo.setOpeningStockValue(rs.getBigDecimal("opening_stock_value"));
			inventoryMgmtReportGeneralVoList.add(inventoryMgmtReportGeneralVo);

			logger.info("Product Id List>>" + inventoryMgmtReportGeneralVo.getId());
		}

		return inventoryMgmtReportGeneralVoList;
	}

	private ResultSet getResultSet(
			List<String> productIdsParamList,
			String query_stmt,
			InventoryMgmtReportVo inventoryMgmtReportVo,
			Connection con,
			int periodId)
			throws ApplicationException {

		ResultSet rs;
		try {
			String sqlIN = productIdsParamList.stream().collect(Collectors.joining(",", "(", ")"));

			query_stmt = query_stmt.replace("(replace_query_param)", sqlIN);
			logger.info(query_stmt);

			PreparedStatement preparedStatement = con.prepareStatement(query_stmt);
			preparedStatement.setInt(1, inventoryMgmtReportVo.getOrgId());
			preparedStatement.setString(2, CommonConstants.STATUS_AS_ACTIVE);

			if (periodId > 1) {
				preparedStatement.setString(3, inventoryMgmtReportVo.getStartDate());
				preparedStatement.setString(4, inventoryMgmtReportVo.getEndDate());
			}
			rs = preparedStatement.executeQuery();
		} catch (Exception ex) {
			logger.error("Exception in getting the result in inventory management:: ", ex);
			throw new ApplicationException(ex);
		}
    /*	finally{
    	closeResources(rs, preparedStatement, null);
    }*/

		return rs;
	}
}

package com.blackstrawai.ap;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.DateConverter;
import com.blackstrawai.ap.dropdowns.BasicVendorDetailsVo;
import com.blackstrawai.ap.dropdowns.PoDetailsDropDownVo;
import com.blackstrawai.ap.purchaseorder.PoAddressVo;
import com.blackstrawai.ap.purchaseorder.PoBillingAddressVo;
import com.blackstrawai.ap.purchaseorder.PoDeliveryAddressVo;
import com.blackstrawai.ap.purchaseorder.PoFilterVo;
import com.blackstrawai.ap.purchaseorder.PoGeneralInfoVo;
import com.blackstrawai.ap.purchaseorder.PoItemsVo;
import com.blackstrawai.ap.purchaseorder.PoListVo;
import com.blackstrawai.ap.purchaseorder.PoProductVo;
import com.blackstrawai.ap.purchaseorder.PoReferenceNumberVo;
import com.blackstrawai.ap.purchaseorder.PoTaxDetailsVo;
import com.blackstrawai.ap.purchaseorder.PoTaxDistributionVo;
import com.blackstrawai.ap.purchaseorder.PurchaseOrderVo;
import com.blackstrawai.ap.vendorportal.purchaseorder.DeclinePoVo;
import com.blackstrawai.ap.vendorportal.purchaseorder.VendorPortalPoFilterVo;
import com.blackstrawai.ap.vendorportal.purchaseorder.VendorPortalPoListVo;
import com.blackstrawai.attachments.AttachmentsConstants;
import com.blackstrawai.attachments.AttachmentsDao;
import com.blackstrawai.attachments.AttachmentsVo;
import com.blackstrawai.attachments.UploadFileVo;
import com.blackstrawai.common.BaseDao;
import com.blackstrawai.common.CommonConstants;
import com.blackstrawai.common.FinanceCommonDao;
import com.blackstrawai.externalintegration.banking.yesbank.YesBankConstants;
import com.blackstrawai.keycontact.VendorDao;
import com.blackstrawai.onboarding.OrganizationDao;
import com.blackstrawai.settings.CommonVo;

@Repository
public class PurchaseOrderDao extends BaseDao {
	private Logger logger = Logger.getLogger(PurchaseOrderDao.class);

	@Autowired
	private OrganizationDao organizationDao;

	@Autowired
	private AttachmentsDao attachmentsDao;

	@Autowired
	private VendorDao vendorDao;

	@Autowired
	private FinanceCommonDao financeCommonDao;

	public Boolean createPurchaseOrder(PurchaseOrderVo poVo) throws ApplicationException, SQLException {
		boolean isCreatedSuccessfully = false;
		logger.info("To create a new Purchase Order in PurchaseOrderDao");
		Connection connection = null;
		try {
			connection = getAccountsPayable();
			connection.setAutoCommit(false);
			createPoGeneralInfo(poVo, connection);
			if (poVo.getId() != null && poVo.getAddress() != null) {
				createPoBillingAddress(poVo.getAddress().getBillingAddress(), poVo.getId(), connection);
				createPoDeliveryAddress(poVo.getAddress().getDeliveryAddress(), poVo.getId(), connection);
			}
			if (poVo.getId() != null && poVo.getItems() != null) {
				for (PoProductVo product : poVo.getItems().getProducts()) {
					Integer productId = createProduct(product, connection, poVo.getId());
					createTaxDetailsForProduct(productId, product, connection);
				}
				createItemsDetails(poVo.getItems(), connection, poVo.getId());
			}
			attachmentsDao.createAttachments(poVo.getOrganizationId(),poVo.getUserId(),poVo.getAttachments(), AttachmentsConstants.MODULE_TYPE_PO, poVo.getId(),poVo.getRoleName());
			connection.commit();
			isCreatedSuccessfully = true;
			logger.info("Succecssfully created po in PurchaseOrderDao");
		} catch (Exception e) {
			logger.info("Error in createPurchaseOrder:: ", e);
			isCreatedSuccessfully = false;
			connection.rollback();
			throw new ApplicationException(e.getMessage());
		} finally {
			if (connection != null) {
				connection.close();
			}
		}
		return isCreatedSuccessfully;
	}

	private void createPoGeneralInfo(PurchaseOrderVo poVo, Connection connection) throws ApplicationException {
		logger.info("To insert into table purchase_order");
		if (poVo != null && poVo.getGeneralInformation() != null && poVo.getAddress()!=null) {
			StringBuilder poNumberBuilder = new StringBuilder();
			poNumberBuilder
			.append(poVo.getGeneralInformation().getPoOrderNoPrefix() != null && !poVo.getGeneralInformation().getPoOrderNoPrefix().isEmpty()
			? poVo.getGeneralInformation().getPoOrderNoPrefix()
					: " ")
			.append("~").append(poVo.getGeneralInformation().getPurchaseOrderNo()).append("~")
			.append(poVo.getGeneralInformation().getPoOrderNoSuffix() != null && !poVo.getGeneralInformation().getPoOrderNoSuffix().isEmpty()
			? poVo.getGeneralInformation().getPoOrderNoSuffix()
					: " ");
			String poNumber = poNumberBuilder.toString();
			logger.info("poNumber" + poNumber);
			boolean isPoExist = checkPoExistForAnOrganization(poVo.getOrganizationId(), connection, poNumber);
			if (isPoExist) {
				throw new ApplicationException("Purchase Order Number Already Exists");
			}
			ResultSet rs = null;
			PreparedStatement preparedStatement =null;
			try {
				preparedStatement = connection.prepareStatement(PurchaseOrderConstants.INSERT_INTO_PURCHASE_ORDER, Statement.RETURN_GENERATED_KEYS); 
				String dateFormat = getDefaultDateOfOrg(poVo.getOrganizationId());
				preparedStatement.setInt(1, poVo.getGeneralInformation().getVendorId());
				preparedStatement.setString(2, poVo.getGeneralInformation().getVendorGstNumber());
				if (poVo.getGeneralInformation().getPurchaseOrderDate() != null) {
					Date poDate = DateConverter.getInstance()
							.convertStringToDate(poVo.getGeneralInformation().getPurchaseOrderDate(), dateFormat);
					if (poDate != null) {
						String date = DateConverter.getInstance().convertDateToGivenFormat(poDate, "yyyy-MM-dd");
						logger.info("converted poDate::" + date);
						preparedStatement.setString(3, date);
					} else {
						logger.info("converted poDate set as null");
						preparedStatement.setString(3, null);
					}
				} else {
					preparedStatement.setString(3, poVo.getGeneralInformation().getPurchaseOrderDate());
				}
				preparedStatement.setString(4, poNumber);
				preparedStatement.setInt(5,
						poVo.getGeneralInformation().getLocationId() != null
						? poVo.getGeneralInformation().getLocationId()
								: 0);
				preparedStatement.setString(6,
						poVo.getGeneralInformation().getLocationGstNumber() != null
						? poVo.getGeneralInformation().getLocationGstNumber()
								: null);
				if (poVo.getGeneralInformation().getDeliveryDate() != null) {
					Date dueDate = DateConverter.getInstance()
							.convertStringToDate(poVo.getGeneralInformation().getDeliveryDate(), dateFormat);
					if (dueDate != null) {
						String date = DateConverter.getInstance().convertDateToGivenFormat(dueDate, "yyyy-MM-dd");
						logger.info("convertedDate::" + date);
						preparedStatement.setString(7, date);
					} else {
						logger.info("convertedDate set as null");
						preparedStatement.setString(7, null);
					}
				} else {
					preparedStatement.setString(7, poVo.getGeneralInformation().getDeliveryDate());
				}
				preparedStatement.setString(8,
						poVo.getGeneralInformation().getReferenceNo() != null
						? poVo.getGeneralInformation().getReferenceNo()
								: null);
				preparedStatement.setInt(9, poVo.getGeneralInformation().getPurchaseOrderTypeId()!=null ? poVo.getGeneralInformation().getPurchaseOrderTypeId() : 0);
				preparedStatement.setInt(10,
						poVo.getGeneralInformation().getShippingPreferenceId() != null
						? poVo.getGeneralInformation().getShippingPreferenceId()
								: 0);
				preparedStatement.setInt(11,
						poVo.getGeneralInformation().getShippingMethodId() != null
						? poVo.getGeneralInformation().getShippingMethodId()
								: 0);
				preparedStatement.setInt(12,
						poVo.getGeneralInformation().getPaymentTermsId() != null
						? poVo.getGeneralInformation().getPaymentTermsId()
								: 0);
				preparedStatement.setString(13, poVo.getGeneralInformation().getNotes());
				preparedStatement.setString(14, poVo.getGeneralInformation().getTermsConditions());
				preparedStatement.setBoolean(15, poVo.getAddress().getIsSameBillingAddress());
				preparedStatement.setString(16,
						poVo.getStatus() != null ? poVo.getStatus() : CommonConstants.STATUS_AS_ACTIVE);
				preparedStatement.setDate(17, new Date(System.currentTimeMillis()));
				preparedStatement.setInt(18, Integer.valueOf(poVo.getUserId()));
				preparedStatement.setInt(19, poVo.getOrganizationId());
				preparedStatement.setBoolean(20, poVo.getIsSuperAdmin()!=null ?  poVo.getIsSuperAdmin() : false);
				preparedStatement.setString(21, poVo.getRoleName());
				logger.info("preparedStmnt ::" + preparedStatement.toString());
				int rowAffected = preparedStatement.executeUpdate();
				if (rowAffected == 1) {
					rs = preparedStatement.getGeneratedKeys();
					while (rs.next()) {
						poVo.setId(rs.getInt(1));
					}
				}
				logger.info("Successfully inserted into table purchase_order ");
			} catch (Exception e) {
				logger.info("Error in inserted into table purchase_order ", e);
				throw new ApplicationException(e);
			}finally{
				closeResources(rs, preparedStatement, null);
			}

		}

	}

	private void createPoBillingAddress(PoBillingAddressVo billingAddress, Integer purchaseOrderId,
			Connection connection) throws ApplicationException {
		logger.info("To insert into table purchase_order_billing_address");
		PreparedStatement preparedStatement = null;
		try  {
			preparedStatement = connection.prepareStatement(PurchaseOrderConstants.INSERT_INTO_PO_BILLING_ADDRESS);
			preparedStatement.setString(1, billingAddress.getPhoneNo());
			preparedStatement.setInt(2, billingAddress.getCountry());
			preparedStatement.setString(3, billingAddress.getAddress_1());
			preparedStatement.setString(4, billingAddress.getAddress_2());
			preparedStatement.setInt(5, billingAddress.getState());
			preparedStatement.setString(6, billingAddress.getCity());
			preparedStatement.setString(7, billingAddress.getZipCode() != null ? billingAddress.getZipCode() : null);
			preparedStatement.setString(8, billingAddress.getLandMark());
			preparedStatement.setDate(9, new Date(System.currentTimeMillis()));
			preparedStatement.setInt(10, purchaseOrderId);
			preparedStatement.setString(11, billingAddress.getAttention());
			logger.info("PO_Billing::" + preparedStatement.toString());
			preparedStatement.execute();
			logger.info("Successfully inserted into table purchase_order_billing_address ");
		} catch (Exception e) {
			logger.info("Error in inserted into table purchase_order_billing_address ", e);
			throw new ApplicationException(e);
		}finally{
			closeResources(null,preparedStatement,null);
		}

	}

	private void createPoDeliveryAddress(PoDeliveryAddressVo deliveryAddress, Integer purchaseOrderId,
			Connection connection) throws ApplicationException {
		logger.info("To insert into table purchase_order_delivery_address");
		PreparedStatement preparedStatement =null;
		try {
			preparedStatement = connection.prepareStatement(PurchaseOrderConstants.INSERT_INTO_PO_DELIVERY_ADDRESS);
			preparedStatement.setString(1, deliveryAddress.getPhoneNo());
			preparedStatement.setInt(2, deliveryAddress.getCountry());
			preparedStatement.setString(3, deliveryAddress.getAddress_1());
			preparedStatement.setString(4, deliveryAddress.getAddress_2());
			preparedStatement.setInt(5, deliveryAddress.getState());
			preparedStatement.setString(6, deliveryAddress.getCity());
			preparedStatement.setString(7, deliveryAddress.getZipCode() != null ? deliveryAddress.getZipCode() : null);
			preparedStatement.setString(8, deliveryAddress.getLandMark());
			preparedStatement.setDate(9, new Date(System.currentTimeMillis()));
			preparedStatement.setInt(10, purchaseOrderId);
			preparedStatement.setString(11, deliveryAddress.getAttention());
			preparedStatement.execute();
			logger.info("Successfully inserted into table purchase_order_delivery_address ");
		} catch (Exception e) {
			logger.info("Error in inserted into table purchase_order_delivery_address ", e);
			throw new ApplicationException(e);
		}finally{
			closeResources(null,preparedStatement,null);
		}

	}

	private Integer createProduct(PoProductVo product, Connection connection, Integer id) throws ApplicationException {
		logger.info("To insert into table purchase_order_product " + product);
		Integer generatedId = null;
		if (product != null && id != null) {
			PreparedStatement preparedStatement =null;
			ResultSet rs =null;
			try {
				preparedStatement = connection.prepareStatement(PurchaseOrderConstants.INSERT_INTO_PO_PRODUCT, Statement.RETURN_GENERATED_KEYS);
				preparedStatement.setInt(1, product.getProductId() != null ? product.getProductId() : 0);
				preparedStatement.setInt(2, product.getProductAccountId() != null ? product.getProductAccountId() : 0);
				preparedStatement.setString(3, product.getProductAccountName());
				preparedStatement.setString(4, product.getProductAccountLevel());
				preparedStatement.setString(5, product.getQuantity());
				preparedStatement.setInt(6, product.getMeasureId() != null ? product.getMeasureId() : 0);
				preparedStatement.setDouble(7, product.getUnitPrice() != null ? product.getUnitPrice() : 0.00);
				// The Integer value when Tax rate Id is 0 , It means tax is not applicable for
				// that product
				preparedStatement.setInt(8, product.getTaxRateId() != null ? product.getTaxRateId() : 0);
				preparedStatement.setDouble(9, product.getAmount() != null ? product.getAmount() : 0.00);
				preparedStatement.setString(10, CommonConstants.STATUS_AS_ACTIVE);
				preparedStatement.setDate(11, new Date(System.currentTimeMillis()));
				preparedStatement.setInt(12, id);
				preparedStatement.setDouble(13, product.getNetAmount()!=null ?product.getNetAmount() : 0.00);
				preparedStatement.setDouble(14, product.getBaseUnitPrice()!=null ? product.getBaseUnitPrice() : 0.00);
				preparedStatement.setString(15, product.getDescription()!=null ? product.getDescription() : null );
				int rowAffected = preparedStatement.executeUpdate();
				if (rowAffected == 1) {
					rs = preparedStatement.getGeneratedKeys();
					while (rs.next()) {
						generatedId = rs.getInt(1);
					}
				}
				logger.info("Successfully inserted into table purchase_order_product with Id ::" + generatedId);
			} catch (Exception e) {
				logger.info("Error in inserting to table purchase_order_product ", e);
				throw new ApplicationException(e);
			}finally{
				closeResources(rs,preparedStatement,null);
			}
		}
		return generatedId;
	}

	/*
	 * To add the gst and tax details for each product Will allow to add the tax
	 * details only when the tax Rate id is not equal to 0 as tax rate id 0 means
	 * tax is not applicable for product
	 */
	private void createTaxDetailsForProduct(Integer productId, PoProductVo product, Connection connection)
			throws ApplicationException {
		logger.info("To insert into table accounts_payable_tax_details " + product);
		if (productId != null && product != null && product.getTaxRateId()!=null && product.getTaxRateId() != 0 && product.getTaxDetails() != null
				&& product.getTaxDetails().getTaxDistribution() != null) {
			for (PoTaxDistributionVo distributionVo : product.getTaxDetails().getTaxDistribution()) {
				PreparedStatement preparedStatement = null;
				try {
					preparedStatement = connection.prepareStatement(PurchaseOrderConstants.INSERT_INTO_TAX_DETAILS);
					preparedStatement.setString(1, PurchaseOrderConstants.MODULE_TYPE_PO);
					preparedStatement.setInt(2, productId);
					preparedStatement.setString(3, product.getTaxDetails().getGroupName().replace("%", ""));
					preparedStatement.setString(4, distributionVo.getTaxName());
					preparedStatement.setString(5, distributionVo.getTaxRate());
					preparedStatement.setDouble(6, distributionVo.getTaxAmount());
					preparedStatement.setDate(7, new Date(System.currentTimeMillis()));
					preparedStatement.executeUpdate();
					logger.info("Successfully inserted into table accounts_payable_tax_details");

				} catch (Exception e) {
					logger.info("Error in inserting to table accounts_payable_tax_details ", e);
					throw new ApplicationException(e);
				}finally{
					closeResources(null, preparedStatement, null);
				}
			}
		}
	}

	private void createItemsDetails(PoItemsVo items, Connection connection, Integer poId) throws ApplicationException {
		logger.info("To insert into table purchase_order_items_details " + items);
		PreparedStatement preparedStatement =null;
		try {
			preparedStatement = connection.prepareStatement(PurchaseOrderConstants.INSERT_INTO_PO_ITEM_DETAILS);
			preparedStatement.setInt(1, items.getSourceOfSupplyId()!=null ?  items.getSourceOfSupplyId() : 0);
			preparedStatement.setInt(2, items.getTaxApplicationMethodId()!=null ? items.getTaxApplicationMethodId() :0);
			preparedStatement.setInt(3, items.getCurrencyId()!=null ? items.getCurrencyId() : 0);
			preparedStatement.setString(4, items.getExchangeRate()!=null ? items.getExchangeRate() : null);
			preparedStatement.setDouble(5, items.getSubTotal()!=null ? items.getSubTotal() : 0.00);
			preparedStatement.setDouble(6, items.getDiscountValue()!=null ? items.getDiscountValue() : 0.00);
			preparedStatement.setInt(7, items.getDiscountTypeId()!=null ? items.getDiscountTypeId() : 0);
			preparedStatement.setDouble(8, items.getDiscountAmount()!=null ? items.getDiscountAmount() : 0.00);
			preparedStatement.setBoolean(9, items.getIsApplyAfterTax());
			preparedStatement.setInt(10, items.getTdsId()!=null ?  items.getTdsId()  : 0);
			preparedStatement.setDouble(11, items.getTdsValue()!=null ? items.getTdsValue() : 0.00);
			preparedStatement.setDouble(12, items.getAdjustment()!=null ? items.getAdjustment() : 0.00);
			preparedStatement.setDouble(13, items.getTotal()!=null ? items.getTotal() : 0.00);
			preparedStatement.setInt(14, poId);
			preparedStatement.setDate(15, new Date(System.currentTimeMillis()));
			preparedStatement.setBoolean(16, items.getIsRCMApplicable());
			preparedStatement.executeUpdate();
			logger.info("Successfully inserted into table purchase_order_items_details");
		} catch (Exception e) {
			logger.info("Error in inserting to table purchase_order_items_details ", e);
			throw new ApplicationException(e);
		}finally{
			closeResources(null, preparedStatement, null);
		}
	}

	// TO check of PO already Exist For Organization
	private boolean checkPoExistForAnOrganization(Integer orgId, Connection con, String poNo)
			throws ApplicationException {
		Boolean isPoExist = false;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			preparedStatement = con.prepareStatement(PurchaseOrderConstants.CHECK_PO_EXIST_FOR_ORG);
			preparedStatement.setInt(1, orgId);
			preparedStatement.setString(2, poNo);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				isPoExist = true;
			}
		} catch (Exception e) {
			logger.info("Error in checkPoExistForAnOrganization  ", e);
			throw new ApplicationException(e);
		}finally{
			closeResources(rs, preparedStatement, null);
		}
		return isPoExist;
	}

	// to get the default date of an organization
	private String getDefaultDateOfOrg(Integer orgId) throws ApplicationException {
		logger.info("To getDefaultDateOfOrg ");
		String dateFormat = null;
		Connection conn =null;
		try {
			conn = getUserMgmConnection();
			dateFormat = organizationDao.getDefaultDateForOrganization(orgId, conn);
		} catch (Exception e) {
			logger.info("Error in getDefaultDateOfOrg ", e);
			throw new ApplicationException(e);
		}finally{
			closeResources(null, null, conn);
		}
		return dateFormat;

	}

	// To soft delete values in Purchase order tables
	public void deletePurchaseOrderEntries(Integer id,String userId,String roleName) throws ApplicationException {
		logger.info("To deletePurchaseOrderEntries:: ");
		if (id != null) {
			Connection connection = null;
			try  {
				connection = getAccountsPayable();
				// To remove from purchase Order table
				changeStatusForPoTables(id, CommonConstants.STATUS_AS_DELETE, connection,
						PurchaseOrderConstants.DELETE_PURCHASE_ORDER);
				// To remove from purchase order Product table
				changeStatusForPoTables(id, CommonConstants.STATUS_AS_DELETE, connection,
						PurchaseOrderConstants.DELETE_PO_PRODUCT_FOR_PO_ID);
				// To remove from Attachments table
				attachmentsDao.changeStatusForAttachments(id, CommonConstants.STATUS_AS_DELETE,
						AttachmentsConstants.MODULE_TYPE_PO,userId,roleName);
				logger.info("Deleted successfully in all tables ");
			} catch (Exception e) {
				logger.info("Error in deletePurchaseOrderEntries:: ", e);
				throw new ApplicationException(e);
			}finally{
				closeResources(null, null, connection);
			}
		}
	}

	private void changeStatusForPoTables(Integer id, String status, Connection con, String query)
			throws ApplicationException {
		logger.info("To Change the status with query :: " + query);
		if (query != null) {
			PreparedStatement preparedStatement = null;
			try {
				preparedStatement = con.prepareStatement(query);
				preparedStatement.setString(1, status);
				preparedStatement.setDate(2, new Date(System.currentTimeMillis()));
				preparedStatement.setInt(3, id);
				preparedStatement.executeUpdate();
				logger.info("Status chaneged successfully ");
			} catch (Exception e) {
				logger.info("Error in changeStatusForPoTables ", e);
				throw new ApplicationException(e);
			}finally{
				closeResources(null, preparedStatement, null);
			}
		}

	}

	private void activateDeactivatePo(Integer id, String status, Connection con, String query,String userId, String roleName)
			throws ApplicationException {
		logger.info("To Change the status with query :: " + query);
		if (query != null) {
			PreparedStatement preparedStatement =null;
			try {
				preparedStatement = con.prepareStatement(query);
				preparedStatement.setString(1, status);
				preparedStatement.setDate(2, new Date(System.currentTimeMillis()));
				preparedStatement.setString(3, userId);
				preparedStatement.setString(4, roleName);
				preparedStatement.setInt(5, id);
				preparedStatement.executeUpdate();
				logger.info("Status chaneged successfully ");
			} catch (Exception e) {
				logger.info("Error in changeStatusForPoTables ", e);
				throw new ApplicationException(e);
			}finally{
				closeResources(null, preparedStatement, null);
			}
		}

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<PoListVo> getPoFilteredList(PoFilterVo filterVo) throws ApplicationException {
		logger.info("To get the Filteredlist with filter values :::" + filterVo);
		Connection connection = null;
		List<PoListVo> listVos = new ArrayList<PoListVo>();
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			String dateFormat = getDefaultDateOfOrg(filterVo.getOrgId());
			StringBuilder filterQuery = new StringBuilder(PurchaseOrderConstants.GET_ALL_FILTERED_PURCHASE_ORDER);
			List paramsList = new ArrayList<>();
			paramsList.add(filterVo.getOrgId());
			// paramsList.add(CommonConstants.STATUS_AS_DELETE);
			if (filterVo.getStatus() != null) {
				filterQuery.append(" and po.status = ? ");
				paramsList.add(filterVo.getStatus());
			}
			if (filterVo.getFromAmount() != null) {
				filterQuery.append(" and poi.total >= ?");
				paramsList.add(filterVo.getFromAmount());
			}
			if (filterVo.getToAmount() != null) {
				filterQuery.append(" and poi.total <= ?");
				paramsList.add(filterVo.getToAmount());
			}
			if (filterVo.getVendorId() != null) {
				filterQuery.append(" and po.vendor_id = ?");
				paramsList.add(filterVo.getVendorId());
			}
			if (filterVo.getStartDate() != null) {
				if (filterVo.getEndDate() == null) {
					java.sql.Date date = new java.sql.Date(System.currentTimeMillis());
					filterVo.setEndDate(DateConverter.getInstance().getDatePickerDateFormat(date));
					logger.info("End date is null and so setting the sysdate::" + filterVo.getEndDate());
				}

				if (filterVo.getStartDate() != null && filterVo.getEndDate() != null) {
					filterQuery.append(" and Cast(po.purchase_order_date as datetime)  between ? and ? ");
					String fromdate = DateConverter.getInstance().correctDatePickerDateToString(filterVo.getStartDate());
					logger.info("convertedFromDate::" + fromdate);
					paramsList.add(fromdate);
					String todate = DateConverter.getInstance().correctDatePickerDateToString(filterVo.getEndDate());
					logger.info("convertedEndDate::" + todate);
					paramsList.add(todate);
				}
			}
			filterQuery.append("  order by po.id desc");
			logger.info(filterQuery.toString());
			logger.info(paramsList);
			connection = getAccountsPayable();
			preparedStatement = connection.prepareStatement(filterQuery.toString());
			int counter = 1;
			for (int i = 0; i < paramsList.size(); i++) {
				logger.info(counter);
				preparedStatement.setObject(counter, paramsList.get(i));
				counter++;
			}
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				PoListVo listVo = new PoListVo();
				listVo.setPoId(rs.getInt(1));
				Date poDate = rs.getDate(3);
				if (poDate != null && dateFormat != null) {
					String puchaseOrderDate = DateConverter.getInstance().convertDateToGivenFormat(poDate, dateFormat);
					listVo.setDateOfCreation(puchaseOrderDate);
				}
				Date dueDate = rs.getDate(4);
				if (dueDate != null && dateFormat != null) {
					String duedate = DateConverter.getInstance().convertDateToGivenFormat(dueDate, dateFormat);
					listVo.setDeliveryDate(duedate);
				}
				listVo.setStatus(rs.getString(5));
				listVo.setAmount(rs.getDouble(6));
				String poNo = rs.getString(7) != null ? rs.getString(7).replace("~", "") : null;
				logger.info("poNo is::" + poNo);
				listVo.setPoNumber(poNo);
				listVo.setVendorName(rs.getString(8));
				listVo.setCurrencySymbol(rs.getString(9));
				listVo.setIsPaymentInitiated(checkYesBankPaymentInitiated(filterVo.getOrgId(), rs.getInt(2), YesBankConstants.KEYCONTACT_VENDOR, rs.getInt(1), YesBankConstants.MODULE_PO_PAYMENT));
				listVos.add(listVo);
			}
			logger.info("listVos size::" + listVos.size());
			logger.info("Retrieved Pos ");

		} catch (Exception e) {
			logger.info("Error in getAllFilteredPos:: ", e);
			throw new ApplicationException(e);
		}finally {
			closeResources(rs, preparedStatement, connection);
		}
		return listVos;

	}

	

	public Boolean checkYesBankPaymentInitiated(int orgId,int keyContactId,String keyContactType,int moduleId,String moduleType)throws ApplicationException
	{
		Connection con = null;
		PreparedStatement preparedStatement = null;
		Boolean isPaymentInitiated =false;
		ResultSet rs = null;
		try {
			con = getBankingConnection();
			preparedStatement = con.prepareStatement(YesBankConstants.CHECK_PAYMENT_INITIATED);
			preparedStatement.setInt(1, orgId);
			preparedStatement.setInt(2, keyContactId);
			preparedStatement.setString(3, keyContactType);
			preparedStatement.setString(4, moduleType);
			preparedStatement.setInt(5,moduleId);
			rs=preparedStatement.executeQuery();
			while (rs.next()) {
				
				isPaymentInitiated=true;
			}
		} catch (Exception e) {
			logger.info("::: Exception in  checkPaymentInitiated Details ::::" + e);
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return isPaymentInitiated;
	}
	
	public Integer getMaxAmount(Integer id,String query) throws ApplicationException {
		logger.info("To getMaxAmount");
		Integer maxAmt = null;
		if (id != null) {
			Connection connection = null;
			PreparedStatement preparedStatement =null;
			ResultSet rs =null;
			try
			{
				connection = getAccountsPayable();
				preparedStatement = connection.prepareStatement(query);
				preparedStatement.setInt(1, id);
				rs = preparedStatement.executeQuery();
				while (rs.next()) {
					maxAmt = rs.getInt(1);
				}
			} catch (ApplicationException | SQLException e) {
				logger.info(e);
				throw new ApplicationException(e);
			}finally {
				closeResources(rs, preparedStatement, connection);
			}
		}
		return maxAmt;

	}

	public Map<String, Object> getFilterDropdownValues(Integer orgId) throws ApplicationException {
		Map<String, Object> map = new HashMap<>();
		List<BasicVendorDetailsVo> vendorList = null;
		Integer maxAmnt = null;
		Connection con = null;
		try {
			con = getAccountsPayable();
			maxAmnt = getMaxAmount(orgId,PurchaseOrderConstants.GET_MAX_AMOUNT_FOR_ORG);
			vendorList = vendorDao.getBasicVendorWithGSTNo(orgId, con);
			logger.info("vendorList size::" + vendorList.size());
			if (maxAmnt != null) {
				map.put("maxAmount", maxAmnt);
			}
			if (vendorList != null && vendorList.size() > 0) {
				map.put("Vendors", vendorList);
			}

		} catch (Exception e) {
			logger.info(e);
			throw new ApplicationException(e);
		}finally{
			closeResources(null, null, con);
		}

		return map;
	}

	public PurchaseOrderVo getPurchaseOrderById(Integer poId) throws ApplicationException {
		logger.info("To get Po in getPurchaseOrderById for:::" + poId);
		PurchaseOrderVo poVo = null;
		if (poId != null) {
			Connection con = null;
			try {
				con = getAccountsPayable();
				poVo = new PurchaseOrderVo();
				poVo.setId(poId);
				getPoGeneralInfoById(poVo, con);
				if(poVo.getId()!=null ) {
					PoBillingAddressVo billingAddressVo = getPoBillingAddress(poVo.getId(),con);
					PoDeliveryAddressVo deliveryAddressVo = getPoDeliveryAddress(poVo.getId(), con);
					PoItemsVo itemsVo = getPoItemsDetailsByPoId(poVo.getId(), con);
					List<PoProductVo> products = getProductListForPo(poVo.getId(), con);
					if (itemsVo != null)
						itemsVo.setProducts(products);
					if(poVo.getAddress()!=null) {
						poVo.getAddress().setBillingAddress(billingAddressVo);
						poVo.getAddress().setDeliveryAddress(deliveryAddressVo);
					}
					poVo.setItems(itemsVo);
					List<UploadFileVo> uploadFileVos = new ArrayList<UploadFileVo>();
					for (AttachmentsVo attachments : attachmentsDao.getAttachmentDetails(poId, AttachmentsConstants.MODULE_TYPE_PO)) {
						UploadFileVo uploadFileVo = new UploadFileVo();
						uploadFileVo.setId(attachments.getId());
						uploadFileVo.setName(attachments.getFileName());
						uploadFileVo.setSize(attachments.getSize());
						uploadFileVos.add(uploadFileVo);
					}
					poVo.setAttachments(uploadFileVos);
				}
				logger.info("Succecssfully fetched Po in getPurchaseOrderById + " + poVo);
			} catch (Exception e) {
				logger.info("Error in getPurchaseOrderById:: ", e);
				throw new ApplicationException(e);
			}finally{
				closeResources(null,null,con);
			}
		}
		return poVo;

	}




	private void getPoGeneralInfoById(PurchaseOrderVo poVo, Connection con) throws ApplicationException {
		logger.info("To get  in getPoGeneralInfoById " + poVo);
		if (poVo != null && poVo.getId() != null) {
			PreparedStatement preparedStatement = null;
			ResultSet rs = null;
			try  {
				preparedStatement = con.prepareStatement(PurchaseOrderConstants.GET_PURCHASE_ORDER_BY_ID);
				preparedStatement.setInt(1, poVo.getId());
				rs = preparedStatement.executeQuery();
				while (rs.next()) {
					PoGeneralInfoVo generalInfoVo = new PoGeneralInfoVo();
					PoAddressVo addressVo = new PoAddressVo();
					generalInfoVo.setVendorId(rs.getInt(1));
					generalInfoVo.setVendorGstNumber(rs.getString(2));
					Date poCreateDate = rs.getDate(3);
					String[] poNum =rs.getString(4).split("~");
					if(poNum.length>1) {
						generalInfoVo.setPoOrderNoPrefix(poNum[0]); 
						generalInfoVo.setPurchaseOrderNo(poNum[1]);
						generalInfoVo.setPoOrderNoSuffix(poNum[2]); 
					}
					generalInfoVo.setLocationId(rs.getInt(5));
					generalInfoVo.setLocationGstNumber(rs.getString(6)!=null ? rs.getString(6) : "NA");
					Date poDeliveryDate = rs.getDate(7);
					generalInfoVo.setReferenceNo(rs.getString(8));
					generalInfoVo.setPurchaseOrderTypeId(rs.getInt(9));
					generalInfoVo.setShippingPreferenceId(rs.getInt(10));
					generalInfoVo.setShippingMethodId(rs.getInt(11));
					generalInfoVo.setPaymentTermsId(rs.getInt(12));
					generalInfoVo.setNotes(rs.getString(13));
					generalInfoVo.setTermsConditions(rs.getString(14));
					addressVo.setIsSameBillingAddress(rs.getBoolean(15));
					poVo.setStatus(rs.getString(16));
					poVo.setUserId(String.valueOf(rs.getInt(17)));
					poVo.setOrganizationId(rs.getInt(18));
					poVo.setIsSuperAdmin(rs.getBoolean(19));
					String dateFormat = getDefaultDateOfOrg(poVo.getOrganizationId());
					if (poCreateDate != null && poVo.getOrganizationId() != null) {
						String poDate = DateConverter.getInstance().convertDateToGivenFormat(poCreateDate,dateFormat);
						logger.info("Converted PoDate::: "+ poDate);
						generalInfoVo.setPurchaseOrderDate(poDate);
					}
					if (poDeliveryDate != null && poVo.getOrganizationId() != null) {
						String duedate = DateConverter.getInstance().convertDateToGivenFormat(poDeliveryDate, dateFormat);
						logger.info("Converted Poduedate::: "+ duedate);
						generalInfoVo.setDeliveryDate(duedate);
					}
					poVo.setGeneralInformation(generalInfoVo);
					poVo.setAddress(addressVo);
				}

				logger.info("Successfully fetched  getPoGeneralInfoById " + poVo);
			} catch (Exception e) {
				logger.info("Error in getInvoiceGeneralInfoById ", e);
				throw new ApplicationException(e);
			}finally{
				closeResources(rs,preparedStatement,null);
			}
		}
	}

	private PoItemsVo getPoItemsDetailsByPoId(Integer poId, Connection con) throws ApplicationException {
		logger.info("To get  in getPoItemsDetailsByPoId "); 
		PoItemsVo itemsVo = null;
		if(poId!=null) {
			PreparedStatement preparedStatement =null;
			ResultSet rs =null;
			try{
				preparedStatement = con.prepareStatement(PurchaseOrderConstants.GET_PO_ITEMS_DETAILS_BY_PO_ID);
				preparedStatement.setInt(1, poId);
				rs = preparedStatement.executeQuery();
				while(rs.next()) {
					itemsVo = new PoItemsVo();
					itemsVo.setSourceOfSupplyId(rs.getInt(1));
					itemsVo.setTaxApplicationMethodId(rs.getInt(2));
					itemsVo.setCurrencyId(rs.getInt(3));
					itemsVo.setExchangeRate(rs.getString(4));
					itemsVo.setSubTotal(rs.getDouble(5));
					itemsVo.setDiscountValue(rs.getDouble(6));
					itemsVo.setDiscountTypeId(rs.getInt(7));
					itemsVo.setDiscountAmount(rs.getDouble(8));
					itemsVo.setIsApplyAfterTax(rs.getBoolean(9));
					itemsVo.setTdsId(rs.getInt(10));
					itemsVo.setTdsValue(rs.getDouble(11));
					itemsVo.setAdjustment(rs.getDouble(12));
					itemsVo.setTotal(rs.getDouble(13));
					itemsVo.setIsRCMApplicable(rs.getBoolean(14));
				}
				logger.info("Successfully fetched  getPoItemsDetailsByPoId "); 
			}catch(Exception e) {
				logger.info("Error in getPoItemsDetailsByPoId ", e); 
				throw new ApplicationException(e);
			}finally{
				closeResources(rs,preparedStatement,null);
			}
		}
		return itemsVo;
	}

	private PoBillingAddressVo getPoBillingAddress(Integer poId, Connection con) throws ApplicationException {
		PoBillingAddressVo addressVo = null;
		logger.info("Entry into getPoBillingAddress"); 
		PreparedStatement preparedStatement =null;
		ResultSet rs = null;
		try{
			preparedStatement = con.prepareStatement(PurchaseOrderConstants.GET_BILLING_ADDRESS);
			preparedStatement.setInt(1,poId ); 
			preparedStatement.execute();
			rs = preparedStatement.executeQuery();
			while(rs.next()) {
				addressVo = new PoBillingAddressVo();
				addressVo.setPhoneNo(rs.getString(1));
				addressVo.setCountry(rs.getInt(2));
				addressVo.setAddress_1(rs.getString(3));
				addressVo.setAddress_2(rs.getString(4));
				addressVo.setState(rs.getInt(5));
				addressVo.setCity(rs.getString(6));
				addressVo.setZipCode(rs.getString(7));
				addressVo.setLandMark(rs.getString(8));
				addressVo.setAttention(rs.getString(9));
			}
			logger.info("Successfully fetched getPoBillingAddress"); 
		}catch(Exception e) {
			logger.info("Error in getPoBillingAddress ",e); 
			throw new ApplicationException(e); 
		}finally{
			closeResources(rs,preparedStatement,null);
		}
		return addressVo; 
	}

	private PoDeliveryAddressVo getPoDeliveryAddress(Integer poId, Connection con) throws ApplicationException {
		PoDeliveryAddressVo deliveryAddressVo = null;
		logger.info("Entry into getPoDeliveryAddress"); 
		PreparedStatement preparedStatement =null;
		ResultSet rs =null;
		try{
			preparedStatement = con.prepareStatement(PurchaseOrderConstants.GET_DELIVERY_ADDRESS);
			preparedStatement.setInt(1,poId ); 
			preparedStatement.execute();
			rs = preparedStatement.executeQuery();
			while(rs.next()) {
				deliveryAddressVo = new PoDeliveryAddressVo();
				deliveryAddressVo.setPhoneNo(rs.getString(1));
				deliveryAddressVo.setCountry(rs.getInt(2));
				deliveryAddressVo.setAddress_1(rs.getString(3));
				deliveryAddressVo.setAddress_2(rs.getString(4));
				deliveryAddressVo.setState(rs.getInt(5));
				deliveryAddressVo.setCity(rs.getString(6));
				deliveryAddressVo.setZipCode(rs.getString(7));
				deliveryAddressVo.setLandMark(rs.getString(8));
				deliveryAddressVo.setAttention(rs.getString(9));
			}
			logger.info("Successfully fetched getPoBillingAddress"); 
		}catch(Exception e) {
			logger.info("Error in getPoBillingAddress ",e); 
			throw new ApplicationException(e); 
		}finally{
			closeResources(rs,preparedStatement,null);
		}
		return deliveryAddressVo; 
	}



	private PoTaxDetailsVo getTaxDetails( Integer productId,Connection con) throws ApplicationException{
		logger.info("To get  in getTaxDetails for prodId:"+productId); 
		PoTaxDetailsVo  taxDetailsVo = null;
		PreparedStatement preparedStatement =null;
		ResultSet rs = null;
		try{
			preparedStatement = con.prepareStatement(BillsInvoiceConstants.GET_TAX_DETAILS);
			preparedStatement.setInt(1, productId);
			preparedStatement.setString(2, PurchaseOrderConstants.MODULE_TYPE_PO);
			taxDetailsVo = new PoTaxDetailsVo();
			List<PoTaxDistributionVo> distributionVos = new ArrayList<PoTaxDistributionVo>();
			rs = preparedStatement.executeQuery();
			while(rs.next()) {
				PoTaxDistributionVo distributionVo= new PoTaxDistributionVo();
				taxDetailsVo.setId(rs.getInt(1));
				taxDetailsVo.setComponentName(rs.getString(2));
				taxDetailsVo.setComponentItemId(rs.getInt(3));
				taxDetailsVo.setGroupName(rs.getString(4));
				distributionVo.setTaxName(rs.getString(5));
				distributionVo.setTaxRate(rs.getString(6));
				distributionVo.setTaxAmount(rs.getDouble(7));
				distributionVos.add(distributionVo);
			}
			taxDetailsVo.setTaxDistribution(distributionVos);
			logger.info("Successfully fetched  getTaxDetails " +taxDetailsVo); 
		}catch(Exception e) {
			logger.info("Error in getTaxDetails ", e); 
			throw new ApplicationException(e);
		}finally{
			closeResources(rs,preparedStatement,null);
		}
		return taxDetailsVo;
	}


	private List<PoProductVo> getProductListForPo(Integer poId , Connection con) throws ApplicationException {
		logger.info("To get  in getProductListForPo "); 
		List<PoProductVo> productVos = new ArrayList<PoProductVo>();
		PreparedStatement preparedStatement=null;
		ResultSet rs = null;
		try{
			preparedStatement = con.prepareStatement(PurchaseOrderConstants.GET_ALL_PRODUCTS_BY_PO_ID);
			preparedStatement.setInt(1, poId);
			rs = preparedStatement.executeQuery();
			while(rs.next()) {
				PoProductVo productVo = new PoProductVo();
				productVo.setId(rs.getInt(1));
				productVo.setProductId(rs.getInt(2));
				productVo.setProductDisplayname(rs.getString(3));
				productVo.setHsn(rs.getString(4));
				productVo.setProductAccountId(rs.getInt(5));
				productVo.setProductAccountName(rs.getString(6));
				productVo.setProductAccountLevel(rs.getString(7));
				productVo.setQuantity(rs.getString(8));
				productVo.setMeasureId(rs.getInt(9));
				productVo.setQuantityName(rs.getString(8) + " " + rs.getString(10));
				productVo.setUnitPrice(rs.getDouble(11));
				productVo.setTaxRateId(rs.getInt(12));
				productVo.setTaxDisplayValue(rs.getString(13)!=null ? rs.getString(13): "");
				productVo.setAmount(rs.getDouble(14));
				productVo.setStatus(rs.getString(15));
				productVo.setType(rs.getString(16));
				productVo.setNetAmount(rs.getDouble(17));
				productVo.setBaseUnitPrice(rs.getDouble(18));
				productVo.setDescription(rs.getString(19));
				productVo.setTaxDetails(getTaxDetails(rs.getInt(1),con));
				logger.info(productVo);
				productVos.add(productVo);
			}
			logger.info("Successfully fetched  getProductListForInvoices "+productVos);
		}catch(Exception e) {
			logger.info("Error in getProductListForInvoices ", e); 
			throw new ApplicationException(e);
		}finally{
			closeResources(rs,preparedStatement,null);
		}
		return productVos;

	}


	//to activate or deactivate the PO
	public void activateOrDeactivatePo(Integer id, String status, String userId, String roleName) throws ApplicationException {
		Connection connection = null;
		logger.info("Entry to activateOrDeactivatePo ");
		if (id != null && status != null) {
			String statusToUpdate = null;
			if (status.equalsIgnoreCase(CommonConstants.STATUS_AS_ACTIVE)) {
				statusToUpdate = CommonConstants.STATUS_AS_ACTIVE;
			} else if (status.equalsIgnoreCase(CommonConstants.STATUS_AS_INACTIVE)) {
				statusToUpdate = CommonConstants.STATUS_AS_INACTIVE;
			}
			logger.info("Status to update is ::  " + statusToUpdate);
			if (statusToUpdate != null) {
				try {
					connection = getAccountsPayable();
					// To remove from PO info table
					activateDeactivatePo(id, statusToUpdate, connection,PurchaseOrderConstants.ACTIVATE_DEACTIVATE_PURCHASE_ORDER,userId,roleName);
				} catch (Exception e) {
					logger.info("Error in activateOrDeactivatePo:: ", e);
					throw new ApplicationException(e);
				} finally {
					closeResources(null, null, connection);
				}
			}
		}

	}


	//Update  PO
	public boolean updatePo(PurchaseOrderVo poVo) throws ApplicationException, SQLException {
		boolean isUpdatedSuccessfully = false;
		logger.info("To update a new Po in PoDao:::" + poVo);
		Connection connection = null;
		if(poVo!=null && poVo.getId()!=null) {
			try{
				connection = getAccountsPayable();
				connection.setAutoCommit(false);
				updatePoGeneralInfo(poVo,connection);
				if(poVo.getAddress()!=null){
					updateBillingAddress(poVo.getAddress().getBillingAddress(), connection,  poVo.getId());
					updateDeliveryAddress(poVo.getAddress().getDeliveryAddress(), connection,  poVo.getId());
				}
				if(poVo.getItems()!=null) {
					if(poVo.getItems().getProducts()!=null) {
						for(PoProductVo product :  poVo.getItems().getProducts()) {
							if (product.getStatus() != null) {
								switch (product.getStatus().toUpperCase()) {
								case CommonConstants.STATUS_AS_NEW:
									Integer productId = createProduct(product,connection,poVo.getId());
									createTaxDetailsForProduct(productId ,product ,connection);
									break;
								case CommonConstants.STATUS_AS_ACTIVE:
									boolean updateStatus = updateProduct(product,connection,poVo.getId());
									if(updateStatus) 
										updateTaxDetailsForProduct(product.getId(),product,connection);
									break;
								case CommonConstants.STATUS_AS_DELETE:
									changeStatusForPoTables(product.getId(), CommonConstants.STATUS_AS_DELETE, connection,	PurchaseOrderConstants.DELETE_PO_PRODUCT);
									deleteTaxDetails(product.getId(),connection);
									break;
								}
							}
						}

					}
					updateItemsDetails(poVo.getItems(),connection,poVo.getId());

				}

				if(poVo.getAttachments()!=null && poVo.getAttachments().size()>0) {
					attachmentsDao.createAttachments(poVo.getOrganizationId(),poVo.getUserId(),poVo.getAttachments(), AttachmentsConstants.MODULE_TYPE_PO, poVo.getId(),poVo.getRoleName());
				}

				if(poVo.getAttachmentsToRemove()!=null && poVo.getAttachmentsToRemove().size()>0) {
					for(Integer attachmentId : poVo.getAttachmentsToRemove()) {
						attachmentsDao.changeStatusForSingleAttachment(attachmentId, CommonConstants.STATUS_AS_DELETE,poVo.getUserId(),poVo.getRoleName());  
					}
				}
				connection.commit();
				isUpdatedSuccessfully = true;
				logger.info("Succecssfully updated po in PODao");
			}catch(Exception e) {
				logger.info("Error in updatePo:: ",e);
				isUpdatedSuccessfully = false;
				connection.rollback();
				throw new ApplicationException(e.getMessage());
			}finally {
				if (connection != null) {
					connection.close();
				}
			}
		}
		return isUpdatedSuccessfully;
	}

	private void updateItemsDetails(PoItemsVo items, Connection connection, Integer poId) throws ApplicationException {
		logger.info("To update into table purchase_order_items_details " + items);
		PreparedStatement preparedStatement =null;
		try  {
			preparedStatement = connection.prepareStatement(PurchaseOrderConstants.UPDATE_PO_ITEM_DETAILS);
			preparedStatement.setInt(1, items.getSourceOfSupplyId()!=null ?  items.getSourceOfSupplyId() : 0);
			preparedStatement.setInt(2, items.getTaxApplicationMethodId()!=null ? items.getTaxApplicationMethodId() :0);
			preparedStatement.setInt(3, items.getCurrencyId()!=null ? items.getCurrencyId() : 0);
			preparedStatement.setString(4, items.getExchangeRate()!=null ? items.getExchangeRate() : null);
			preparedStatement.setDouble(5, items.getSubTotal()!=null ? items.getSubTotal() : 0.00);
			preparedStatement.setDouble(6, items.getDiscountValue()!=null ? items.getDiscountValue() : 0.00);
			preparedStatement.setInt(7, items.getDiscountTypeId()!=null ? items.getDiscountTypeId() : 0);
			preparedStatement.setDouble(8, items.getDiscountAmount()!=null ? items.getDiscountAmount() : 0.00);
			preparedStatement.setBoolean(9, items.getIsApplyAfterTax());
			preparedStatement.setInt(10, items.getTdsId()!=null ?  items.getTdsId()  : 0);
			preparedStatement.setDouble(11, items.getTdsValue()!=null ? items.getTdsValue() : 0.00);
			preparedStatement.setDouble(12, items.getAdjustment()!=null ? items.getAdjustment() : 0.00);
			preparedStatement.setDouble(13, items.getTotal()!=null ? items.getTotal() : 0.00);
			preparedStatement.setDate(14, new Date(System.currentTimeMillis()));
			preparedStatement.setBoolean(15, items.getIsRCMApplicable());
			preparedStatement.setInt(16, poId);
			preparedStatement.executeUpdate();
			logger.info("Successfully updated into table purchase_order_items_details");
		} catch (Exception e) {
			logger.info("Error in updating to table purchase_order_items_details ", e);
			throw new ApplicationException(e);
		}finally{
			closeResources(null, preparedStatement, null);
		}

	}

	private boolean updateProduct(PoProductVo product, Connection connection, Integer id) throws ApplicationException {
		logger.info("To update into table purchase_order_product " + product);
		boolean isUpdated = false;
		if (product != null && id != null) {
			PreparedStatement preparedStatement = null;
			try  {
				preparedStatement = connection.prepareStatement(PurchaseOrderConstants.UPDATE_PO_PRODUCT);
				preparedStatement.setInt(1, product.getProductId() != null ? product.getProductId() : 0);
				preparedStatement.setInt(2, product.getProductAccountId() != null ? product.getProductAccountId() : 0);
				preparedStatement.setString(3, product.getProductAccountName());
				preparedStatement.setString(4, product.getProductAccountLevel());
				preparedStatement.setString(5, product.getQuantity());
				preparedStatement.setInt(6, product.getMeasureId() != null ? product.getMeasureId() : 0);
				preparedStatement.setDouble(7, product.getUnitPrice() != null ? product.getUnitPrice() : 0.00);
				// The Integer value when Tax rate Id is 0 , It means tax is not applicable for
				// that product
				preparedStatement.setInt(8, product.getTaxRateId() != null ? product.getTaxRateId() : 0);
				preparedStatement.setDouble(9, product.getAmount() != null ? product.getAmount() : 0.00);
				preparedStatement.setString(10, CommonConstants.STATUS_AS_ACTIVE);
				preparedStatement.setDate(11, new Date(System.currentTimeMillis()));
				preparedStatement.setDouble(12, product.getNetAmount()!=null ? product.getNetAmount() : 0.00);
				preparedStatement.setDouble(13, product.getBaseUnitPrice()!=null ? product.getBaseUnitPrice() : 0.00);
				preparedStatement.setString(14, product.getDescription()!=null ? product.getDescription() : null );
				preparedStatement.setInt(15, product.getId());
				preparedStatement.setInt(16, id);
				preparedStatement.executeUpdate();
				isUpdated = true;
				logger.info("Successfully updated into table purchase_order_product with Id ::" );
			}catch (Exception e) {
				isUpdated = false;
				logger.info("Error in updated to table purchase_order_product ", e);
				throw new ApplicationException(e);
			}finally{
				closeResources(null, preparedStatement, null);
			}
		}
		return isUpdated;
	}

	private void updateBillingAddress( PoBillingAddressVo billingAddress, Connection con ,Integer poId) throws ApplicationException {
		logger.info("Entry into updateBillingAddress" + billingAddress); 
		PreparedStatement preparedStatement =null;
		try{
			preparedStatement = con.prepareStatement(PurchaseOrderConstants.UPDATE_PO_BILLING_ADDRESS);
			preparedStatement.setString(1, billingAddress.getPhoneNo());
			preparedStatement.setInt(2, billingAddress.getCountry());
			preparedStatement.setString(3, billingAddress.getAddress_1());
			preparedStatement.setString(4, billingAddress.getAddress_2());
			preparedStatement.setInt(5, billingAddress.getState());
			preparedStatement.setString(6, billingAddress.getCity());
			preparedStatement.setString(7, billingAddress.getZipCode() != null ? billingAddress.getZipCode() : null);
			preparedStatement.setString(8, billingAddress.getLandMark());
			preparedStatement.setDate(9, new Date(System.currentTimeMillis()));
			preparedStatement.setString(10, billingAddress.getAttention());
			preparedStatement.setInt(11, poId);
			logger.info("PO_Billing::" + preparedStatement.toString());
			preparedStatement.execute();
			logger.info("Successfully updated into table purchase_order_billing_address ");
		} catch (Exception e) {
			logger.info("Error in update into table purchase_order_billing_address ", e);
			throw new ApplicationException(e);
		}finally{
			closeResources(null, preparedStatement, null);
		}
	}

	private void updateDeliveryAddress( PoDeliveryAddressVo deliveryAddress, Connection con ,Integer poId) throws ApplicationException {
		logger.info("Entry into updateDeliveryAddress"); 
		PreparedStatement preparedStatement =null;
		try{
			preparedStatement = con.prepareStatement(PurchaseOrderConstants.UPDATE_PO_DELIVERY_ADDRESS);
			preparedStatement.setString(1, deliveryAddress.getPhoneNo());
			preparedStatement.setInt(2, deliveryAddress.getCountry());
			preparedStatement.setString(3, deliveryAddress.getAddress_1());
			preparedStatement.setString(4, deliveryAddress.getAddress_2());
			preparedStatement.setInt(5, deliveryAddress.getState());
			preparedStatement.setString(6, deliveryAddress.getCity());
			preparedStatement.setString(7, deliveryAddress.getZipCode() != null ? deliveryAddress.getZipCode() : null);
			preparedStatement.setString(8, deliveryAddress.getLandMark());
			preparedStatement.setDate(9, new Date(System.currentTimeMillis()));
			preparedStatement.setString(10, deliveryAddress.getAttention());
			preparedStatement.setInt(11, poId);
			preparedStatement.execute();
			logger.info("Successfully updated into table purchase_order_delivery_address ");
		} catch (Exception e) {
			logger.info("Error in update into table purchase_order_delivery_address ", e);
			throw new ApplicationException(e);
		}finally{
			closeResources(null, preparedStatement, null);
		}
	}

	private void updatePoGeneralInfo(PurchaseOrderVo poVo, Connection connection) throws ApplicationException {
		logger.info("To update into table purchase_order_generalInfo");
		if (poVo != null && poVo.getGeneralInformation() != null && poVo.getAddress()!=null) {
			String purchaseOrderNumber  = null;
			String existingPurchaseOrderNo = getPurchaseOrderNumber(poVo.getId(), connection);
			String purchaseOrderNumToUpdate = new StringBuilder().append(poVo.getGeneralInformation().getPoOrderNoPrefix() != null && !poVo.getGeneralInformation().getPoOrderNoPrefix().isEmpty()
					? poVo.getGeneralInformation().getPoOrderNoPrefix()
							: " ")
					.append("~").append(poVo.getGeneralInformation().getPurchaseOrderNo()).append("~")
					.append(poVo.getGeneralInformation().getPoOrderNoSuffix() != null && !poVo.getGeneralInformation().getPoOrderNoSuffix().isEmpty()
					? poVo.getGeneralInformation().getPoOrderNoSuffix()
							: " ").toString();
			logger.info("poNumber::" + purchaseOrderNumToUpdate);
			logger.info("existingPurchaseOrderNo::" + existingPurchaseOrderNo);
			if(existingPurchaseOrderNo.trim().equalsIgnoreCase(purchaseOrderNumToUpdate.trim())) {
				purchaseOrderNumber = purchaseOrderNumToUpdate;
			}else {
				boolean isPoExist = checkPoExistForAnOrganization(poVo.getOrganizationId(), connection, purchaseOrderNumToUpdate);
				if (isPoExist) {
					throw new ApplicationException("Purchase Order Number Already Exists");
				}else {
					logger.info("To add the new PurchseOrder in the Update purchse order api ");
					purchaseOrderNumber = purchaseOrderNumToUpdate;
				} 

			}
			logger.info("The purchase order number to be inserted is :: " +purchaseOrderNumber);
			PreparedStatement preparedStatement =null;
			try {
				preparedStatement = connection.prepareStatement(PurchaseOrderConstants.UPDATE_PURCHASE_ORDER);
				String dateFormat = getDefaultDateOfOrg(poVo.getOrganizationId());
				preparedStatement.setInt(1, poVo.getGeneralInformation().getVendorId());
				preparedStatement.setString(2, poVo.getGeneralInformation().getVendorGstNumber());
				if (poVo.getGeneralInformation().getPurchaseOrderDate() != null) {
					Date poDate = DateConverter.getInstance().convertStringToDate(poVo.getGeneralInformation().getPurchaseOrderDate(), dateFormat);
					if (poDate != null) {
						String date = DateConverter.getInstance().convertDateToGivenFormat(poDate, "yyyy-MM-dd");
						logger.info("converted poDate::" + date);
						preparedStatement.setString(3, date);
					} else {
						logger.info("converted poDate set as null");
						preparedStatement.setString(3, null);
					}
				} else {
					preparedStatement.setString(3, poVo.getGeneralInformation().getPurchaseOrderDate());
				}
				preparedStatement.setString(4, purchaseOrderNumber);
				preparedStatement.setInt(5,poVo.getGeneralInformation().getLocationId() != null ? poVo.getGeneralInformation().getLocationId(): 0);
				preparedStatement.setString(6,poVo.getGeneralInformation().getLocationGstNumber() != null ? poVo.getGeneralInformation().getLocationGstNumber()	: null);
				if (poVo.getGeneralInformation().getDeliveryDate() != null) {
					Date dueDate = DateConverter.getInstance().convertStringToDate(poVo.getGeneralInformation().getDeliveryDate(), dateFormat);
					if (dueDate != null) {
						String date = DateConverter.getInstance().convertDateToGivenFormat(dueDate, "yyyy-MM-dd");
						logger.info("convertedDate::" + date);
						preparedStatement.setString(7, date);
					} else {
						logger.info("convertedDate set as null");
						preparedStatement.setString(7, null);
					}
				} else {
					preparedStatement.setString(7, poVo.getGeneralInformation().getDeliveryDate());
				}
				preparedStatement.setString(8,poVo.getGeneralInformation().getReferenceNo() != null	? poVo.getGeneralInformation().getReferenceNo(): null);
				preparedStatement.setInt(9, poVo.getGeneralInformation().getPurchaseOrderTypeId()!=null ? poVo.getGeneralInformation().getPurchaseOrderTypeId() : 0);
				preparedStatement.setInt(10,poVo.getGeneralInformation().getShippingPreferenceId() != null ? poVo.getGeneralInformation().getShippingPreferenceId() : 0);
				preparedStatement.setInt(11,poVo.getGeneralInformation().getShippingMethodId() != null ? poVo.getGeneralInformation().getShippingMethodId(): 0);
				preparedStatement.setInt(12,poVo.getGeneralInformation().getPaymentTermsId() != null ? poVo.getGeneralInformation().getPaymentTermsId()	: 0);
				preparedStatement.setString(13, poVo.getGeneralInformation().getNotes());
				preparedStatement.setString(14, poVo.getGeneralInformation().getTermsConditions());
				preparedStatement.setBoolean(15, poVo.getAddress().getIsSameBillingAddress());
				preparedStatement.setString(16,	poVo.getStatus() != null ? poVo.getStatus() : CommonConstants.STATUS_AS_ACTIVE);
				preparedStatement.setDate(17, new Date(System.currentTimeMillis()));
				preparedStatement.setInt(18, Integer.valueOf(poVo.getUserId()));
				preparedStatement.setInt(19, poVo.getOrganizationId());
				preparedStatement.setBoolean(20, poVo.getIsSuperAdmin()!=null ?  poVo.getIsSuperAdmin() : false);
				preparedStatement.setString(21, poVo.getRoleName());
				preparedStatement.setInt(22, poVo.getId());
				logger.info("preparedStmnt ::" + preparedStatement.toString());
				preparedStatement.executeUpdate();
				logger.info("Successfully updated  into table purchase_order generalinfo  ");
			} catch (Exception e) {
				logger.info("Error in update  table purchase_order generalinfo", e);
				throw new ApplicationException(e);
			}finally{
				closeResources(null,preparedStatement,null);
			}

		}


	}

	private void updateTaxDetailsForProduct(Integer productId, PoProductVo product, Connection connection) throws ApplicationException {
		logger.info("To update into table accounts_payable_tax_details " + product);
		/*
		 * To delete the existing entries and then adding the updated entries.. This is
		 * because during Update we do tax recalculation  the Primary id would be missed
		 * out in UI and so following this way of updating
		 */
		deleteTaxDetails(productId,connection);
		createTaxDetailsForProduct(productId, product, connection);

	}


	private void deleteTaxDetails(Integer productId,Connection connection) throws ApplicationException {
		logger.info("To delete into table accounts_payable_tax_details " );
		PreparedStatement preparedStatement = null;
		try{
			preparedStatement = connection.prepareStatement(BillsInvoiceConstants.MODIFY_TAX_DETAILS_STATUS);
			preparedStatement.setDate(1,  new Date(System.currentTimeMillis()));
			preparedStatement.setString(2, CommonConstants.STATUS_AS_DELETE);
			preparedStatement.setString(3, PurchaseOrderConstants.MODULE_TYPE_PO);
			preparedStatement.setInt(4, productId);
			preparedStatement.execute();
		}catch (Exception e) {
			logger.info("Error in deleteTaxDetails to table accounts_payable_tax_details ", e);
			throw new ApplicationException(e);
		}finally{
			closeResources(null,preparedStatement,null);
		}
	}



	private String getPurchaseOrderNumber(Integer poId, Connection con) throws ApplicationException {
		logger.info("Entry into method:getPurchaseOrderNo in DAO");
		String purchaseOrderNo = "";
		PreparedStatement preparedStatement =null;
		ResultSet rs =null;
		try{
			preparedStatement = con.prepareStatement(PurchaseOrderConstants.GET_PO_NUMBER_FOR_PO_ID);
			preparedStatement.setInt(1, poId);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				purchaseOrderNo = rs.getString(1);
			}
		} catch (Exception e) {
			logger.info("Error in method:getPurchaseOrderNo ", e);
			throw new ApplicationException(e);
		}finally{
			closeResources(rs,preparedStatement,null);
		}
		return purchaseOrderNo;

	}


	public List<PoReferenceNumberVo> getPOReferenceNumber(int organizationId,int vendorId) throws ApplicationException {
		logger.info("Entry into method:getPOReferenceNumber");
		Connection con =null;
		PreparedStatement preparedStatement =null;
		ResultSet rs = null;
		List<PoReferenceNumberVo> data=new ArrayList<PoReferenceNumberVo>();
		try {
			con = getAccountsPayable();
			preparedStatement = con.prepareStatement(PurchaseOrderConstants.GET_PO_REFERENCE_NO);
			preparedStatement.setInt(1, organizationId);
			preparedStatement.setInt(2, vendorId);
			preparedStatement.setString(3, CommonConstants.STATUS_AS_ACTIVE);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				PoReferenceNumberVo poReferenceNumberVo=new PoReferenceNumberVo();
				poReferenceNumberVo.setId(rs.getInt(1));
				poReferenceNumberVo.setValue(poReferenceNumberVo.getId());
				String poRef = rs.getString(2);
				if (poRef != null)
					poRef = poRef.replace("~", "/");
				poReferenceNumberVo.setName(poRef);
				data.add(poReferenceNumberVo);
			}
		} catch (Exception e) {
			logger.info("Error in getPOReferenceNumber", e);
			throw new ApplicationException(e);
		}finally {
			closeResources(rs, preparedStatement, con);

		}	
		return data;

	}


	//Vendor Portal List PO with Filter 
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<VendorPortalPoListVo> getPurchaseOrderFilteredList(VendorPortalPoFilterVo filterVo) throws ApplicationException {
		logger.info("To get the Filteredlist with filter values :::" + filterVo);
		Connection connection = null;
		List<VendorPortalPoListVo> listVos = new ArrayList<VendorPortalPoListVo>();
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			String dateFormat = getDefaultDateOfOrg(filterVo.getOrgId());
			StringBuilder filterQuery = new StringBuilder(PurchaseOrderConstants.GET_PURCHASE_ORDER_FOR_VENDOR);
			List paramsList = new ArrayList<>();
			paramsList.add(filterVo.getVendorId());
			paramsList.add(CommonConstants.STATUS_AS_DELETE);
			if (filterVo.getStatus() != null) {
				filterQuery.append(" and po.status = ? ");
				paramsList.add(filterVo.getStatus());
			}
			if (filterVo.getFromAmount() != null) {
				filterQuery.append(" and poi.total >= ?");
				paramsList.add(filterVo.getFromAmount());
			}
			if (filterVo.getToAmount() != null) {
				filterQuery.append(" and poi.total <= ?");
				paramsList.add(filterVo.getToAmount());
			}
			if (filterVo.getOrgId() != null) {
				filterQuery.append(" and po.organization_id = ?");
				paramsList.add(filterVo.getOrgId());
			}
			if (filterVo.getStartDate() != null) {
				if (filterVo.getEndDate() == null) {
					java.sql.Date date = new java.sql.Date(System.currentTimeMillis());
					filterVo.setEndDate(DateConverter.getInstance().getDatePickerDateFormat(date));
					logger.info("End date is null and so setting the sysdate::" + filterVo.getEndDate());
				}

				if (filterVo.getStartDate() != null && filterVo.getEndDate() != null) {
					filterQuery.append(" and Cast(po.purchase_order_date as datetime)  between ? and ? ");
					String fromdate = DateConverter.getInstance().correctDatePickerDateToString(filterVo.getStartDate());
					logger.info("convertedFromDate::" + fromdate);
					paramsList.add(fromdate);
					String todate = DateConverter.getInstance().correctDatePickerDateToString(filterVo.getEndDate());
					logger.info("convertedEndDate::" + todate);
					paramsList.add(todate);
				}
			}
			filterQuery.append("  order by po.id desc");
			logger.info(filterQuery.toString());
			logger.info(paramsList);
			connection = getAccountsPayable();
			preparedStatement = connection.prepareStatement(filterQuery.toString());
			int counter = 1;
			for (int i = 0; i < paramsList.size(); i++) {
				logger.info(counter);
				preparedStatement.setObject(counter, paramsList.get(i));
				counter++;
			}
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				VendorPortalPoListVo listVo = new VendorPortalPoListVo();
				listVo.setPoId(rs.getInt(1));
				Date poDate = rs.getDate(3);
				if (poDate != null && dateFormat != null) {
					String puchaseOrderDate = DateConverter.getInstance().convertDateToGivenFormat(poDate, dateFormat);
					listVo.setDateOfCreation(puchaseOrderDate);
				}
				Date dueDate = rs.getDate(4);
				if (dueDate != null && dateFormat != null) {
					String duedate = DateConverter.getInstance().convertDateToGivenFormat(dueDate, dateFormat);
					listVo.setDeliveryDate(duedate);
				}
				String status  = rs.getString(5);
				String displayStatus = null;
				switch(status) {
				case CommonConstants.STATUS_AS_ACTIVE: 
					displayStatus = CommonConstants.DISPLAY_STATUS_AS_ACTIVE;
					break;
				case CommonConstants.STATUS_AS_DRAFT: 
					displayStatus = CommonConstants.DISPLAY_STATUS_AS_DRAFT;
					break;
				case CommonConstants.STATUS_AS_INACTIVE: 
					displayStatus = CommonConstants.DISPLAY_STATUS_AS_INACTIVE;
					break;
				case CommonConstants.STATUS_AS_ACCEPT: 
					displayStatus = CommonConstants.DISPLAY_STATUS_AS_ACCEPT;
					break;
				case CommonConstants.STATUS_AS_DECLINE: 
					displayStatus = CommonConstants.DISPLAY_STATUS_AS_DECLINE;
					break;
				case CommonConstants.STATUS_AS_EXPIRED: 
					displayStatus = CommonConstants.DISPLAY_STATUS_AS_EXPIRED;
					break;
				case CommonConstants.STATUS_AS_OPEN: 
					displayStatus = CommonConstants.DISPLAY_STATUS_AS_OPEN;
					break;
				}
				logger.info("Status is ::" + displayStatus);
				listVo.setStatus(displayStatus);
				listVo.setAmount(rs.getDouble(6));
				String poNo = rs.getString(7) != null ? rs.getString(7).replace("~", "") : null;
				logger.info("poNo is::" + poNo);
				listVo.setPoNumber(poNo);
				listVo.setCustomerName(rs.getString(8));
				listVos.add(listVo);
			}
			logger.info("listVos size::" + listVos.size());
			logger.info("Retrieved Pos ");

		} catch (Exception e) {
			logger.info("Error in getAllFilteredPos:: ", e);
			throw new ApplicationException(e);
		}finally {
			closeResources(rs, preparedStatement, connection);
		}
		return listVos;

	}

	//Vendor Portal Change PO to accepted state 
	public void acceptPurchaseOrder(Integer poId, String userId, String roleName) throws ApplicationException {
		logger.info(" To Updated Po with Id::"+poId);
		Connection con  =null;
		PreparedStatement preparedStatement =null;
		try{
			con  = getAccountsPayable();
			preparedStatement = con.prepareStatement(PurchaseOrderConstants.ACCEPT_PO_BY_VENDOR);
			preparedStatement.setString(1, CommonConstants.STATUS_AS_ACCEPT);
			preparedStatement.setDate(2, new Date(System.currentTimeMillis()));
			preparedStatement.setString(3, userId);
			preparedStatement.setString(4, roleName);
			preparedStatement.setInt(5, poId);
			preparedStatement.executeUpdate();
			logger.info("Updated Po ");
		}catch (Exception e) {
			logger.info("Error in acceptPurchaseOrder:: ", e);
			throw new ApplicationException(e);
		}finally {
			closeResources(null, preparedStatement, con);
		}
	}	

	// vendor Portal decline PO 
	public void declinePurchaseOrder(DeclinePoVo declinePoVo) throws ApplicationException {
		logger.info(" To Updated Po with Id::"+declinePoVo.getPoId());
		Connection con  =null;
		PreparedStatement preparedStatement =null;
		try{
			con  = getAccountsPayable() ;
			preparedStatement = con.prepareStatement(PurchaseOrderConstants.DECLINE_PO_BY_VENDOR);
			preparedStatement.setString(1, CommonConstants.STATUS_AS_DECLINE);
			preparedStatement.setInt(2, declinePoVo.getReasonId());
			preparedStatement.setString(3, declinePoVo.getReason());
			preparedStatement.setDate(4, new Date(System.currentTimeMillis()));
			preparedStatement.setString(5, declinePoVo.getUserId());
			preparedStatement.setString(6, declinePoVo.getRoleName());
			preparedStatement.setInt(7, declinePoVo.getPoId());
			preparedStatement.executeUpdate();
			logger.info("Updated Po ");
		}catch (Exception e) {
			logger.info("Error in acceptPurchaseOrder:: ", e);
			throw new ApplicationException(e);
		}finally {
			closeResources(null, preparedStatement, con);
		}
	}	


	//vendor Portal Filter Drop downs 
	public Map<String, Object> getVpFilterDropdownValues(Integer vendorId) throws ApplicationException {
		Map<String, Object> map = new HashMap<>();
		Integer maxAmnt = null;
		Connection con =null;
		try {
			con = getAccountsPayable();
			maxAmnt = getMaxAmount(vendorId,PurchaseOrderConstants.GET_MAX_AMOUNT_FOR_VENDOR);
			if (maxAmnt != null) {
				map.put("maxAmount", maxAmnt);
			}
			logger.info("getVpFilterDropdownValues::"+map);
		} catch (Exception e) {
			logger.info("Error in getVpFilterDropdownValues",e);
			throw new ApplicationException(e);
		}finally {
			closeResources(null, null, con);
		}

		return map;
	}

	//vendor Portal Reason Drop downs 
	public Map<String, Object> getVpReasonDropdownValues() throws ApplicationException {
		Map<String, Object> map = new HashMap<>();
		List<CommonVo> reasons= financeCommonDao.getReasons();
		try {
			if (reasons != null && reasons.size()>0) {
				map.put("reasons", reasons);
			}
			logger.info("getVpReasonDropdownValues::"+map);
		} catch (Exception e) {
			logger.info("Error in getVpReasonDropdownValues",e);
			throw new ApplicationException(e);
		}

		return map;
	}

	public List<PoDetailsDropDownVo> getPurchaseBillReferenceNumberByOrgVendor(Integer orgId, Integer vendorId, int currencyId, int paymentId) throws ApplicationException {

		List<PoDetailsDropDownVo> listVos = new ArrayList<PoDetailsDropDownVo>();
		if (orgId != null && vendorId != null) {
			Connection con =null;
			PreparedStatement preparedStatement =null;
			ResultSet rs =null;
			try  {
				con = getAccountsPayable();
				preparedStatement = con.prepareStatement(PurchaseOrderConstants.GET_ALL_PURCHASES_ORG_VENDOR);
				preparedStatement.setInt(1, orgId);
				preparedStatement.setInt(2, vendorId);
				rs = preparedStatement.executeQuery();
				while (rs.next()) {
					PoDetailsDropDownVo listVo = new PoDetailsDropDownVo();
					listVo.setId(rs.getInt(1));
					listVo.setVendorId(rs.getInt(2));
					listVo.setPurchaseOrderNumber(rs.getString(3));
					listVo.setStatus(rs.getString(4));
					listVo.setAmount(rs.getDouble(5));
					listVo.setCurrencyId(rs.getInt(6));
					listVos.add(listVo);
					}
					logger.info("Values in listVos"+listVos.size());

			} catch (Exception e) {
				logger.info("Error in getAllInvoicesWithBillsForOrg ", e);
				throw new ApplicationException(e);
			} finally {
				closeResources(rs, preparedStatement, con);			
			}
		}
		return listVos;

	}

}

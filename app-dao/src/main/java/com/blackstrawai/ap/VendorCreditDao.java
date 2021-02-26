package com.blackstrawai.ap;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.ap.creditnote.CnProductVo;
import com.blackstrawai.ap.creditnote.CnTaxDetailsVo;
import com.blackstrawai.ap.creditnote.CnTaxDistributionVo;
import com.blackstrawai.ap.creditnote.VendorCreditAddressVo;
import com.blackstrawai.ap.creditnote.VendorCreditCreateVo;
import com.blackstrawai.ap.creditnote.VendorCreditGeneralInformationVo;
import com.blackstrawai.ap.creditnote.VendorCreditTransactionVo;
import com.blackstrawai.attachments.AttachmentsConstants;
import com.blackstrawai.attachments.AttachmentsDao;
import com.blackstrawai.attachments.AttachmentsVo;
import com.blackstrawai.attachments.UploadFileVo;
import com.blackstrawai.common.BaseAddressVo;
import com.blackstrawai.common.BaseDao;
import com.blackstrawai.common.CommonConstants;
import com.blackstrawai.journals.GeneralLedgerVo;
import com.blackstrawai.keycontact.VendorDao;
import com.blackstrawai.keycontact.vendor.VendorDestinationAddressVo;
import com.blackstrawai.keycontact.vendor.VendorOriginAddressVo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

@Repository
public class VendorCreditDao extends BaseDao {

	@Autowired
	private VendorDao vendorDao;

	@Autowired
	private ObjectMapper mapper;

	@Autowired
	private AttachmentsDao attachmentsDao;

	private static Gson gson = new Gson();

	private static Logger logger = Logger.getLogger(VendorCreditDao.class);

	public VendorCreditCreateVo createVendorCreditNote(VendorCreditCreateVo vendorCreditCreateVo)
			throws ApplicationException {
		Connection con = getAccountsPayable();

		try {
			con.setAutoCommit(false);

			Integer vendorCreditGeneralInfoId = createVendorCreditGeneralInformation(vendorCreditCreateVo, con);

			updateVendorOriginAndBillingByGstAndVendorId(vendorCreditCreateVo, con);
			createOrgOriginAndBillingByGstAndVendorCreditId(vendorCreditGeneralInfoId,
					vendorCreditCreateVo.getGeneralInfo().getAddress(), con);

			createVendorCreditTransactionalInformation(vendorCreditGeneralInfoId, vendorCreditCreateVo, con);

			createVendorCreditNoteProductInformation(vendorCreditGeneralInfoId,
					vendorCreditCreateVo.getTransactionDetails().getProducts(), con);

			createTaxDetailsForProduct(vendorCreditCreateVo.getTransactionDetails().getProducts(), con);
			con.commit();

			vendorCreditCreateVo.setId(vendorCreditGeneralInfoId);

		} catch (Exception e) {
			throw new ApplicationException(e.getMessage());

		} finally {
			closeResources(null, null, con);
		}
		return vendorCreditCreateVo;
	}

	public VendorCreditCreateVo updateVendorCreditNote(VendorCreditCreateVo vendorCreditCreateVo)
			throws ApplicationException {
		Connection con = getAccountsPayable();

		try {
			con.setAutoCommit(false);

			Integer vendorCreditGeneralInfoId = updateVendorCreditGeneralInformation(vendorCreditCreateVo, con);

			updateVendorOriginAndBillingByGstAndVendorId(vendorCreditCreateVo, con);
			updateOrgOriginAndBillingByGstAndVendorCreditId(vendorCreditGeneralInfoId,
					vendorCreditCreateVo.getGeneralInfo().getAddress(), con);

			updateVendorCreditTransactionalInformation(vendorCreditGeneralInfoId, vendorCreditCreateVo, con);

			updateProductAndTaxDetails(vendorCreditCreateVo, con, vendorCreditGeneralInfoId);

			con.commit();
			vendorCreditCreateVo.setId(vendorCreditGeneralInfoId);

		} catch (Exception e) {
			throw new ApplicationException(e.getMessage());

		} finally {
			closeResources(null, null, con);
		}
		return vendorCreditCreateVo;
	}

	/**
	 * @param vendorCreditCreateVo
	 * @param con
	 * @param vendorCreditGeneralInfoId
	 * @param cnProductVosToCreate
	 * @param cnProductVosToUpdate
	 * @throws ApplicationException
	 */
	private void updateProductAndTaxDetails(VendorCreditCreateVo vendorCreditCreateVo, Connection con,
			Integer vendorCreditGeneralInfoId) throws ApplicationException {
		for (CnProductVo cnProductVo : vendorCreditCreateVo.getTransactionDetails().getProducts()) {
			if (cnProductVo.getStatus() != null) {
				switch (cnProductVo.getStatus().toUpperCase()) {
				case CommonConstants.STATUS_AS_NEW:
					createVendorCreditNoteProductInformation(vendorCreditGeneralInfoId,
							new ArrayList<>(Arrays.asList(cnProductVo)), con);
					createTaxDetailsForProduct(new ArrayList<>(Arrays.asList(cnProductVo)), con);
//						inventoryDao.addAPInvoiceProductToInventoryMgmt(productId , product, invoiceVo);

					break;
				case CommonConstants.STATUS_AS_ACTIVE:
					updateVendorCreditNoteProductInformation(vendorCreditGeneralInfoId,
							new ArrayList<>(Arrays.asList(cnProductVo)), con);
					updateTaxDetailsForProduct(cnProductVo.getId(), cnProductVo, con);

//						inventoryDao.updateAPInvoiceProductToInventoryMgmt(product, invoiceVo);

					break;
				case CommonConstants.STATUS_AS_DELETE:
					updateVendorCreditNoteProductInformation(vendorCreditGeneralInfoId,
							new ArrayList<>(Arrays.asList(cnProductVo)), con);
					deleteTaxDetails(cnProductVo.getId(), VendorCreditConstants.MODULE_TYPE_VENDOR_CREDIT, con);
//						inventoryDao.deleteAPInvoiceProductToInventoryMgmt(product, invoiceVo);

					break;

				default:
					logger.error("Invalid status found for product".concat(cnProductVo.getStatus()));
					throw new ApplicationException("Invalid status found for product".concat(cnProductVo.getStatus()));
				}
			}
		}
	}

	/**
	 * Gets the vendor credit note id.
	 *
	 * @param creditNoteId the credit note id
	 * @return the vendor credit note id
	 * @throws ApplicationException the application exception
	 */
	public VendorCreditCreateVo getVendorCreditNoteId(Integer creditNoteId) throws ApplicationException {

		VendorCreditCreateVo vendorCreditCreateVo = new VendorCreditCreateVo();
		Connection con = getAccountsPayable();
		try {
			getVendorCreditGeneralInformation(creditNoteId, vendorCreditCreateVo, con);
			getVendorOriginAndBillingByGstAndVendorId(vendorCreditCreateVo, con);
			getOrgOriginAndBillingByGstAndVendorCreditId(vendorCreditCreateVo, con);
			getVendorCreditTransactionalInformation(vendorCreditCreateVo, con);
			vendorCreditCreateVo.getTransactionDetails()
					.setProducts(getProductListForVendorCredit(vendorCreditCreateVo.getId(), con));
			getAttachmentForVendorCreditNote(vendorCreditCreateVo);
			
			if(vendorCreditCreateVo.getTransactionDetails()!=null && vendorCreditCreateVo.getTransactionDetails().getProducts()!=null){
				vendorCreditCreateVo.getTransactionDetails().setGroupedTax(calculateTotalTax(vendorCreditCreateVo.getTransactionDetails().getProducts()));
			}

		} catch (Exception e) {
			throw new ApplicationException(e.getMessage());

		} finally {
			closeResources(null, null, con);
		}
		return vendorCreditCreateVo;

	}

	/**
	 * @param vendorCreditCreateVo
	 * @throws ApplicationException
	 */
	private void getAttachmentForVendorCreditNote(VendorCreditCreateVo vendorCreditCreateVo)
			throws ApplicationException {
		List<UploadFileVo> uploadFileVos = new ArrayList<>();
		String moduleType = AttachmentsConstants.MODULE_TYPE_VENDOR_CREDIT;
		for (AttachmentsVo attachments : attachmentsDao.getAttachmentDetails(vendorCreditCreateVo.getId(),
				moduleType)) {
			UploadFileVo uploadFileVo = new UploadFileVo();
			uploadFileVo.setId(attachments.getId());
			uploadFileVo.setName(attachments.getFileName());
			uploadFileVo.setSize(attachments.getSize());
			uploadFileVo.setIsAkshar(attachments.getIsAkshar());
			uploadFileVos.add(uploadFileVo);
		}
		vendorCreditCreateVo.setAttachments(uploadFileVos);
	}

	private void getVendorCreditTransactionalInformation(VendorCreditCreateVo vendorCreditCreateVo, Connection con)
			throws ApplicationException {
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			preparedStatement = con
					.prepareStatement(VendorCreditConstants.GET_VENDOR_CREDIT_TRANSACTION_DETAILS_BY_CREDIT_NOTE_ID);
			preparedStatement.setInt(1, vendorCreditCreateVo.getId());
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				VendorCreditTransactionVo vendorCreditTransactionVo = new VendorCreditTransactionVo();
				vendorCreditTransactionVo.setSourceOfSupplyId(rs.getInt(1));
				vendorCreditTransactionVo.setDestinationOfSupplyId(rs.getInt(2));
				vendorCreditTransactionVo.setTaxApplicationMethodId(rs.getInt(3));
				vendorCreditTransactionVo.setCurrencyid(rs.getInt(4));
				vendorCreditTransactionVo.setExchangeRate(rs.getString(5));
				vendorCreditTransactionVo.setSubTotal(rs.getString(6));
				vendorCreditTransactionVo.setDiscountValue(rs.getString(7));
				vendorCreditTransactionVo.setDiscountTypeId(rs.getInt(8));
				vendorCreditTransactionVo.setDiscountAmount(rs.getString(9));
				vendorCreditTransactionVo.setTdsId(rs.getInt(10));
				vendorCreditTransactionVo.setTdsValue(rs.getString(11));
				vendorCreditTransactionVo.setAdjustment(rs.getString(12));
				vendorCreditTransactionVo.setTotal(rs.getString(13));
				vendorCreditTransactionVo.setIsApplyAfterTax(rs.getBoolean(14));
				vendorCreditCreateVo.setTransactionDetails(vendorCreditTransactionVo);
			}
		} catch (Exception e) {
			logger.info("Error in getVendorCreditTransactionalInformation:: ", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, null);
		}

	}

	private List<CnProductVo> getProductListForVendorCredit(Integer creditNoteId, Connection con)
			throws ApplicationException {
		logger.info("To get  in getProductListForInvoices ");
		List<CnProductVo> productVos = new ArrayList<>();
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			preparedStatement = con.prepareStatement(VendorCreditConstants.GET_VENDOR_CREDIT_PRODUCT_INFO);
			preparedStatement.setInt(1, creditNoteId);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				CnProductVo productVo = new CnProductVo();
				productVo.setId(rs.getInt(1));
				productVo.setProductId(rs.getInt(2));
				productVo.setProductDisplayname(rs.getString(3));
				productVo.setHsn(rs.getString(4));
				productVo.setProductAccountId(rs.getInt(5));
				productVo.setQuantity(rs.getString(6));
				productVo.setMeasureId(rs.getInt(7));
				productVo.setQuantityName(rs.getString(6) + " " + rs.getString(8));
				productVo.setUnitPrice(rs.getDouble(9));
				productVo.setTaxRateId(rs.getInt(10));
				productVo.setTaxDisplayValue(rs.getString(11) != null ? rs.getString(11) : "");
				productVo.setAmount(rs.getDouble(12));
				productVo.setStatus(rs.getString(13));
				productVo.setProductAccountName(rs.getString(14));
				productVo.setProductAccountLevel(rs.getString(15));
				productVo.setDescription(rs.getString(16));
				productVo.setType(rs.getString(17));
				productVo.setTaxDetails(getTaxDetails(rs.getInt(1), con));
				logger.info(productVo);
				productVos.add(productVo);
			}
			logger.info("Successfully fetched  getProductListForInvoices " + productVos);
		} catch (Exception e) {
			logger.info("Error in getProductListForInvoices ", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, null);
		}
		return productVos;

	}

	private CnTaxDetailsVo getTaxDetails(Integer productId, Connection con) throws ApplicationException {
		logger.info("To get  in getTaxDetails for prodId:" + productId);
		CnTaxDetailsVo taxDetailsVo = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			preparedStatement = con.prepareStatement(VendorCreditConstants.GET_TAX_DETAILS);
			preparedStatement.setInt(1, productId);
			preparedStatement.setString(2, VendorCreditConstants.MODULE_TYPE_VENDOR_CREDIT);
			taxDetailsVo = new CnTaxDetailsVo();
			List<CnTaxDistributionVo> distributionVos = new ArrayList<>();
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				CnTaxDistributionVo distributionVo = new CnTaxDistributionVo();
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
			logger.info("Successfully fetched  getTaxDetails " + taxDetailsVo);
		} catch (Exception e) {
			logger.info("Error in getTaxDetails ", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, null);
		}
		return taxDetailsVo;
	}

	private void getVendorCreditGeneralInformation(Integer creditNoteId, VendorCreditCreateVo vendorCreditCreateVo,
			Connection con) throws ApplicationException {
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		VendorCreditGeneralInformationVo vendorCreditGeneralInformationVo = null;

		if (creditNoteId != null) {
			try {

				preparedStatement = con.prepareStatement(VendorCreditConstants.VENDOR_CREDIT_GENERAL_INFO_BY_ID);
				preparedStatement.setInt(1, creditNoteId);
				rs = preparedStatement.executeQuery();
				while (rs.next()) {
					vendorCreditGeneralInformationVo = new VendorCreditGeneralInformationVo();
					vendorCreditCreateVo.setId(creditNoteId);
					vendorCreditCreateVo.setOrganizationId(rs.getInt(1));
					vendorCreditCreateVo.setUserId(rs.getInt(2));
					vendorCreditCreateVo.setRoleName(rs.getString(3));
					vendorCreditGeneralInformationVo.setVendorId(rs.getInt(4));
					vendorCreditGeneralInformationVo.setVendorGstNo(rs.getString(5));
					vendorCreditGeneralInformationVo.setLocationId(rs.getInt(6));
					vendorCreditGeneralInformationVo.setOrgGst(rs.getString(7));
					vendorCreditGeneralInformationVo.setInvoiceId(rs.getInt(8));
					vendorCreditGeneralInformationVo.setCreditNoteNo(rs.getString(9));
					vendorCreditGeneralInformationVo.setCreditNoteDate(rs.getString(10));
					vendorCreditGeneralInformationVo.setVoucherNo(rs.getString(11));
					vendorCreditGeneralInformationVo.setVoucherDate(rs.getString(12));
					vendorCreditGeneralInformationVo.setAdditionalTerms(rs.getString(13));
					vendorCreditGeneralInformationVo.setNotes(rs.getString(14));
					vendorCreditGeneralInformationVo.setReason(rs.getString(15));
					vendorCreditCreateVo.setGeneralLedgerData(gson.fromJson(rs.getString(16), GeneralLedgerVo.class));
					vendorCreditGeneralInformationVo.setAksharData(rs.getString(17));
					vendorCreditCreateVo.setGeneralInfo(vendorCreditGeneralInformationVo);
				}

			} catch (Exception e) {
				logger.info("Error in getVendorCreditGeneralInformation:: ", e);
				throw new ApplicationException(e);
			} finally {
				closeResources(rs, preparedStatement, null);
			}
		}

	}

	private void updateTaxDetailsForProduct(Integer productId, CnProductVo product, Connection connection)
			throws ApplicationException {
		logger.info("To update into table accounts_payable_tax_details " + product);
		/*
		 * To delete the existing entries and then adding the updated entries.. This is
		 * because during Update we do tax recalculation the Primary id would be missed
		 * out and so following this way of updating
		 */
		deleteTaxDetails(productId, VendorCreditConstants.MODULE_TYPE_VENDOR_CREDIT, connection);
		createTaxDetailsForProduct(Arrays.asList(product), connection);

	}
	
	private static List<CnTaxDistributionVo> calculateTotalTax(List<CnProductVo> list) {
		logger.info("To method calculateTotalTax::" + list.size());
		Map<String, Double> taxDetailsMap = new HashMap<>();

		list.forEach(product -> {
			logger.info("CommonConstants.STATUS_AS_DELETE.equals(product.getStatus())"
					+ CommonConstants.STATUS_AS_DELETE.equals(product.getStatus()));
			logger.info("To method product" + product.getTaxDetails());
			if (product.getTaxDetails() != null && !(CommonConstants.STATUS_AS_DELETE.equals(product.getStatus()))) {
				product.getTaxDetails().getTaxDistribution().forEach(taxDistribution -> {
					logger.info("To method product tax size" + product.getTaxDetails().getTaxDistribution().size());
					String taxName = taxDistribution.getTaxName() + "~" + taxDistribution.getTaxRate();
					if (taxName != null && taxDetailsMap.containsKey(taxName)) {
						Double amount = taxDetailsMap.get(taxName);
						amount = amount + taxDistribution.getTaxAmount();
						logger.info("amount" + amount + "for key " + taxName);
						taxDetailsMap.put(taxName, Math.round(amount * 100.0) / 100.0);
					} else {
						logger.info("Adding new entry in map for key" + taxName);
						taxDetailsMap.put(taxName, Math.round(taxDistribution.getTaxAmount() * 100.0) / 100.0);
					}
				});
			}
		});

		List<CnTaxDistributionVo> taxDistributionList = new ArrayList<>();
		if (taxDetailsMap.size() > 0) {
			taxDetailsMap.forEach((k, v) -> {
				CnTaxDistributionVo distribution = new CnTaxDistributionVo();
				distribution.setTaxName(k.split("~")[0]);
				distribution.setTaxRate(k.split("~")[1]);
				distribution.setTaxAmount(v);
				taxDistributionList.add(distribution);
			});
		}
		logger.info(
				"Completed method calculateTotalTax with taxDistributionList size :: " + taxDistributionList.size());
		return taxDistributionList;

	}

	private void createTaxDetailsForProduct(List<CnProductVo> cnProductVos, Connection connection)
			throws ApplicationException {
		logger.info("To insert into table accounts_payable_tax_details " + cnProductVos);
		PreparedStatement preparedStatement = null;
		for (CnProductVo product : cnProductVos) {
			try {
				preparedStatement = connection.prepareStatement(VendorCreditConstants.INSERT_INTO_TAX_DETAILS);
				if (product != null && product.getTaxRateId() != null && product.getTaxRateId() != 0
						&& product.getTaxDetails() != null && product.getTaxDetails().getTaxDistribution() != null) {

					for (CnTaxDistributionVo distributionVo : product.getTaxDetails().getTaxDistribution()) {

						String moduleType = VendorCreditConstants.MODULE_TYPE_VENDOR_CREDIT;
						preparedStatement.setString(1, moduleType);
						preparedStatement.setInt(2, product.getId());
						preparedStatement.setString(3, product.getTaxDetails().getGroupName().replace("%", ""));
						preparedStatement.setString(4, distributionVo.getTaxName());
						preparedStatement.setString(5, distributionVo.getTaxRate());
						preparedStatement.setDouble(6, distributionVo.getTaxAmount());
						preparedStatement.setDate(7, new Date(System.currentTimeMillis()));
						preparedStatement.executeUpdate();
						logger.info("Successfully inserted into table accounts_payable_tax_details");

					}
				}
			} catch (Exception e) {
				logger.info("Error in inserting to table accounts_payable_tax_details ", e);
				throw new ApplicationException(e);
			} finally {
				closeResources(null, preparedStatement, null);
			}
		}
	}

	private void deleteTaxDetails(Integer productId, String module, Connection connection) throws ApplicationException {
		logger.info("To delete into table ar_tax_details ");
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = connection.prepareStatement(VendorCreditConstants.MODIFY_TAX_DETAILS_STATUS);
			preparedStatement.setDate(1, new Date(System.currentTimeMillis()));
			preparedStatement.setString(2, CommonConstants.STATUS_AS_DELETE);
			preparedStatement.setString(3, module);
			preparedStatement.setInt(4, productId);
			preparedStatement.executeUpdate();
		} catch (Exception e) {
			logger.info("Error in deleteTaxDetails to table ar_tax_details ", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(null, preparedStatement, null);
		}
	}

	private void updateVendorCreditNoteProductInformation(Integer vendorCreditGeneralInfoId,
			List<CnProductVo> cnProductVosToUpdate, Connection con) throws ApplicationException {
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;

		try {
			preparedStatement = con.prepareStatement(VendorCreditConstants.UPDATE_VENDOR_CREDIT_PRODUCT);
			for (CnProductVo productVo : cnProductVosToUpdate) {
				preparedStatement.setInt(1, productVo.getProductId());
				preparedStatement.setString(2, productVo.getDescription());
				preparedStatement.setString(3, productVo.getQuantity());
				preparedStatement.setInt(4, productVo.getMeasureId());
				preparedStatement.setString(5, String.valueOf(productVo.getUnitPrice()));
				preparedStatement.setInt(6, productVo.getTaxRateId());
				preparedStatement.setString(7, String.valueOf(productVo.getAmount()));
				preparedStatement.setTimestamp(8, new Timestamp(new java.util.Date().getTime()));
				preparedStatement.setInt(9, vendorCreditGeneralInfoId);
				preparedStatement.setInt(10, productVo.getProductAccountId());
				preparedStatement.setString(11, productVo.getProductAccountName());
				preparedStatement.setString(12, productVo.getProductAccountLevel());
				preparedStatement.setInt(13, productVo.getId());
				preparedStatement.addBatch();
			}
			preparedStatement.executeBatch();

		} catch (Exception e) {
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, null);
		}

	}

	private void updateOrgOriginAndBillingByGstAndVendorCreditId(Integer vendorCreditGeneralInfoId,
			VendorCreditAddressVo address, Connection connection) throws ApplicationException {
		updateOrganizationDeliveryAddressForVendorCredit(vendorCreditGeneralInfoId,
				address.getOrganizationDeliveryAddress(), address.getIsSameBillingAddressForOrganization(), connection);
		updateOrganizationBillingAddressForVendorCredit(vendorCreditGeneralInfoId,
				address.getOrganizationBillingAddress(), connection);

	}

	private void updateOrganizationBillingAddressForVendorCredit(Integer vendorCreditGeneralInfoId,
			BaseAddressVo organizationBillingAddress, Connection connection) throws ApplicationException {
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;

		try {
			preparedStatement = connection
					.prepareStatement(VendorCreditConstants.UPDATE_VENDOR_CREDIT_ORG_BILLING_ADDRESS);
			organizationAddressPreparestatement(preparedStatement, organizationBillingAddress,
					vendorCreditGeneralInfoId);
			preparedStatement.setInt(12, organizationBillingAddress.getId());
			preparedStatement.executeUpdate();

		} catch (Exception e) {
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, null);
		}

	}

	private void updateOrganizationDeliveryAddressForVendorCredit(Integer vendorCreditGeneralInfoId,
			BaseAddressVo organizationDeliveryAddress, Boolean isSameBillingAddressForOrganization,
			Connection connection) throws ApplicationException {
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;

		try {

			preparedStatement = connection
					.prepareStatement(VendorCreditConstants.UPDATE_VENDOR_CREDIT_ORG_DELIVERY_ADDRESS);
			organizationAddressPreparestatement(preparedStatement, organizationDeliveryAddress,
					vendorCreditGeneralInfoId);
			preparedStatement.setBoolean(12, isSameBillingAddressForOrganization);
			preparedStatement.setInt(13, organizationDeliveryAddress.getId());
			preparedStatement.executeUpdate();

		} catch (Exception e) {
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, null);
		}
	}

	private void updateVendorCreditTransactionalInformation(Integer vendorCreditGeneralInfoId,
			VendorCreditCreateVo vendorCreditCreateVo, Connection con) throws ApplicationException {
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;

		try {
			preparedStatement = con.prepareStatement(VendorCreditConstants.UPDATE_VENDOR_CREDIT_TRANSACTION_DETAILS);
			preparedStatement.setInt(1, vendorCreditCreateVo.getTransactionDetails().getSourceOfSupplyId());
			preparedStatement.setInt(2, vendorCreditCreateVo.getTransactionDetails().getDestinationOfSupplyId());
			preparedStatement.setInt(3, vendorCreditCreateVo.getTransactionDetails().getTaxApplicationMethodId());
			preparedStatement.setInt(4, vendorCreditCreateVo.getTransactionDetails().getCurrencyid());
			preparedStatement.setString(5, vendorCreditCreateVo.getTransactionDetails().getExchangeRate());
			preparedStatement.setString(6, vendorCreditCreateVo.getTransactionDetails().getSubTotal());
			preparedStatement.setString(7, vendorCreditCreateVo.getTransactionDetails().getDiscountValue());
			preparedStatement.setString(8, vendorCreditCreateVo.getTransactionDetails().getDiscountAmount());
			preparedStatement.setObject(9, vendorCreditCreateVo.getTransactionDetails().getTdsId());
			preparedStatement.setString(10, vendorCreditCreateVo.getTransactionDetails().getTdsValue());
			preparedStatement.setString(11, vendorCreditCreateVo.getTransactionDetails().getAdjustment());
			preparedStatement.setString(12, vendorCreditCreateVo.getTransactionDetails().getTotal());
			preparedStatement.setTimestamp(13, new Timestamp(new java.util.Date().getTime()));
			preparedStatement.setBoolean(14, vendorCreditCreateVo.getTransactionDetails().getIsApplyAfterTax());
			preparedStatement.setInt(15, vendorCreditGeneralInfoId);
			
			preparedStatement.executeUpdate();
		} catch (Exception e) {
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, null);
		}

	}

	private Integer updateVendorCreditGeneralInformation(VendorCreditCreateVo vendorCreditCreateVo, Connection con)
			throws ApplicationException {
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;

		try {

			preparedStatement = con.prepareStatement(VendorCreditConstants.UPDATE_VENDOR_CREDIT_GENERAL_INFO);
			preparedStatement.setInt(1, vendorCreditCreateVo.getOrganizationId());
			preparedStatement.setInt(2, vendorCreditCreateVo.getUserId());
			preparedStatement.setString(3, vendorCreditCreateVo.getRoleName());
			preparedStatement.setTimestamp(4, new Timestamp(new java.util.Date().getTime()));
			preparedStatement.setInt(5, vendorCreditCreateVo.getGeneralInfo().getVendorId());
			preparedStatement.setString(6, vendorCreditCreateVo.getGeneralInfo().getVendorGstNo());
			preparedStatement.setInt(7, vendorCreditCreateVo.getGeneralInfo().getLocationId());
			preparedStatement.setString(8, vendorCreditCreateVo.getGeneralInfo().getOrgGst());
			preparedStatement.setInt(9, vendorCreditCreateVo.getGeneralInfo().getInvoiceId());
			preparedStatement.setString(10, vendorCreditCreateVo.getGeneralInfo().getCreditNoteNo());
			preparedStatement.setString(11, vendorCreditCreateVo.getGeneralInfo().getCreditNoteDate());
			preparedStatement.setString(12, vendorCreditCreateVo.getGeneralInfo().getVoucherNo());
			preparedStatement.setString(13, vendorCreditCreateVo.getGeneralInfo().getVoucherDate());
			preparedStatement.setString(14, vendorCreditCreateVo.getGeneralInfo().getAdditionalTerms());
			preparedStatement.setString(15, vendorCreditCreateVo.getGeneralInfo().getNotes());
			preparedStatement.setString(16, vendorCreditCreateVo.getGeneralInfo().getReason());

			String glData = vendorCreditCreateVo.getGeneralLedgerData() != null
					? mapper.writeValueAsString(vendorCreditCreateVo.getGeneralLedgerData())
					: null;

			preparedStatement.setString(17, glData);
			preparedStatement.setString(18, vendorCreditCreateVo.getGeneralInfo().getAksharData());
			preparedStatement.setInt(19, vendorCreditCreateVo.getId());
			preparedStatement.executeUpdate();

		} catch (Exception e) {
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, null);
		}

		return vendorCreditCreateVo.getId();
	}

	private void createVendorCreditNoteProductInformation(Integer vendorCreditGeneralInfoId,
			List<CnProductVo> cnProductVos, Connection con) throws ApplicationException {
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;

		try {
			preparedStatement = con.prepareStatement(VendorCreditConstants.INSERT_VENDOR_CREDIT_PRODUCT,
					Statement.RETURN_GENERATED_KEYS);
			for (CnProductVo productVo : cnProductVos) {
				preparedStatement.setInt(1, productVo.getProductId());
				preparedStatement.setString(2, productVo.getDescription());
				preparedStatement.setString(3, productVo.getQuantity());
				preparedStatement.setInt(4, productVo.getMeasureId());
				preparedStatement.setString(5, String.valueOf(productVo.getUnitPrice()));
				preparedStatement.setInt(6, productVo.getTaxRateId());
				preparedStatement.setString(7, String.valueOf(productVo.getAmount()));
				preparedStatement.setTimestamp(8, new Timestamp(new java.util.Date().getTime()));
				preparedStatement.setInt(9, vendorCreditGeneralInfoId);
				preparedStatement.setInt(10, productVo.getProductAccountId());
				preparedStatement.setString(11, productVo.getProductAccountName());
				preparedStatement.setString(12, productVo.getProductAccountLevel());
				preparedStatement.addBatch();
			}
			preparedStatement.executeBatch();
			rs = preparedStatement.getGeneratedKeys();

			int i = 0;
			while (rs.next()) {

				cnProductVos.get(i).setId(rs.getInt(1));
				i++;
			}
		} catch (Exception e) {
			throw new ApplicationException(e.getMessage());

		} finally {
			closeResources(rs, preparedStatement, null);

		}

	}

	private void createVendorCreditTransactionalInformation(Integer vendorCreditGeneralInfoId,
			VendorCreditCreateVo vendorCreditCreateVo, Connection con) throws ApplicationException {
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;

		try {
			preparedStatement = con.prepareStatement(VendorCreditConstants.INSERT_VENDOR_CREDIT_TRANSACTION_DETAILS);
			preparedStatement.setInt(1, vendorCreditCreateVo.getTransactionDetails().getSourceOfSupplyId());
			preparedStatement.setInt(2, vendorCreditCreateVo.getTransactionDetails().getDestinationOfSupplyId());
			preparedStatement.setInt(3, vendorCreditCreateVo.getTransactionDetails().getTaxApplicationMethodId());
			preparedStatement.setInt(4, vendorCreditCreateVo.getTransactionDetails().getCurrencyid());
			preparedStatement.setString(5, vendorCreditCreateVo.getTransactionDetails().getExchangeRate());
			preparedStatement.setString(6, vendorCreditCreateVo.getTransactionDetails().getSubTotal());
			preparedStatement.setString(7, vendorCreditCreateVo.getTransactionDetails().getDiscountValue());
			preparedStatement.setInt(8, vendorCreditCreateVo.getTransactionDetails().getDiscountTypeId());
			preparedStatement.setString(9, vendorCreditCreateVo.getTransactionDetails().getDiscountAmount());
			preparedStatement.setObject(10, vendorCreditCreateVo.getTransactionDetails().getTdsId());
			preparedStatement.setString(11, vendorCreditCreateVo.getTransactionDetails().getTdsValue());
			preparedStatement.setString(12, vendorCreditCreateVo.getTransactionDetails().getAdjustment());
			preparedStatement.setString(13, vendorCreditCreateVo.getTransactionDetails().getTotal());
			preparedStatement.setInt(14, vendorCreditGeneralInfoId);
			preparedStatement.setTimestamp(15, new Timestamp(new java.util.Date().getTime()));
			preparedStatement.setBoolean(16, vendorCreditCreateVo.getTransactionDetails().getIsApplyAfterTax());
			preparedStatement.executeUpdate();

		} catch (Exception e) {
			throw new ApplicationException(e.getMessage());

		} finally {
			closeResources(rs, preparedStatement, null);

		}

	}

	private void createOrgOriginAndBillingByGstAndVendorCreditId(Integer vendorCreditGeneralInfoId,
			VendorCreditAddressVo address, Connection connection) throws ApplicationException {
		createOrganizationDeliveryAddressForVendorCredit(vendorCreditGeneralInfoId,
				address.getOrganizationDeliveryAddress(), address.getIsSameBillingAddressForOrganization(), connection);
		createOrganizationBillingAddressForVendorCredit(vendorCreditGeneralInfoId,
				address.getOrganizationBillingAddress(), connection);
	}

	private void createOrganizationBillingAddressForVendorCredit(Integer vendorCreditGeneralInfoId,
			BaseAddressVo organizationBillingAddress, Connection connection) throws ApplicationException {
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			preparedStatement = connection
					.prepareStatement(VendorCreditConstants.INSERT_VENDOR_CREDIT_ORG_BILLING_ADDRESS);
			organizationAddressPreparestatement(preparedStatement, organizationBillingAddress,
					vendorCreditGeneralInfoId);
			preparedStatement.executeUpdate();

		} catch (Exception e) {
			throw new ApplicationException(e.getMessage());

		} finally {
			closeResources(rs, preparedStatement, null);

		}

	}

	private void createOrganizationDeliveryAddressForVendorCredit(Integer vendorCreditGeneralInfoId,
			BaseAddressVo organizationDeliveryAddress, Boolean isSameBillingAddressForOrganization, Connection con)
			throws ApplicationException {
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;

		try {
			preparedStatement = con.prepareStatement(VendorCreditConstants.INSERT_VENDOR_CREDIT_ORG_DELIVERY_ADDRESS);
			organizationAddressPreparestatement(preparedStatement, organizationDeliveryAddress,
					vendorCreditGeneralInfoId);
			preparedStatement.setBoolean(12, isSameBillingAddressForOrganization);
			int rowAffected = preparedStatement.executeUpdate();
			if (rowAffected != 1) {
				throw new ApplicationException("Unable to inset vendor credit org delivery address");
			}

		} catch (Exception e) {
			throw new ApplicationException(e.getMessage());

		} finally {
			closeResources(rs, preparedStatement, null);

		}

	}

	private void getOrgOriginAndBillingByGstAndVendorCreditId(VendorCreditCreateVo vendorCreditCreateVo, Connection con)
			throws ApplicationException {

		getOrgDeliveryAddressByCreditNoteId(vendorCreditCreateVo, con);
		getOrgBillingAddressByCreditNoteId(vendorCreditCreateVo, con);

	}

	private void getOrgBillingAddressByCreditNoteId(VendorCreditCreateVo vendorCreditCreateVo, Connection con)
			throws ApplicationException {
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		BaseAddressVo baseAddressVo = null;
		try {

			preparedStatement = con.prepareStatement(VendorCreditConstants.GET_VENDOR_CREDIT_ORG_BILLING_ADDRESS);
			preparedStatement.setInt(1, vendorCreditCreateVo.getId());
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				baseAddressVo = new BaseAddressVo();
				prepareStatementForGetOrgAddress(rs, baseAddressVo);
				vendorCreditCreateVo.getGeneralInfo().getAddress().setOrganizationBillingAddress(baseAddressVo);
			}

		} catch (Exception e) {
			logger.info("Error in getOrgDeliveryAddressByCreditNoteId:: ", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, null);
		}

	}

	private void getOrgDeliveryAddressByCreditNoteId(VendorCreditCreateVo vendorCreditCreateVo, Connection con)
			throws ApplicationException {
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		BaseAddressVo baseAddressVo = null;
		try {

			preparedStatement = con.prepareStatement(VendorCreditConstants.GET_VENDOR_CREDIT_ORG_DELIVERY_ADDRESS);
			preparedStatement.setInt(1, vendorCreditCreateVo.getId());
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				baseAddressVo = new BaseAddressVo();
				prepareStatementForGetOrgAddress(rs, baseAddressVo);
				vendorCreditCreateVo.getGeneralInfo().getAddress().setOrganizationDeliveryAddress(baseAddressVo);
				vendorCreditCreateVo.getGeneralInfo().getAddress()
						.setIsSameBillingAddressForOrganization(rs.getBoolean(11));
			}

		} catch (Exception e) {
			logger.info("Error in getOrgDeliveryAddressByCreditNoteId:: ", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, null);
		}

	}

	/**
	 * @param rs
	 * @param baseAddressVo
	 * @throws SQLException
	 */
	private void prepareStatementForGetOrgAddress(ResultSet rs, BaseAddressVo baseAddressVo) throws SQLException {
		baseAddressVo.setId(rs.getInt(1));
		baseAddressVo.setPhoneNo(rs.getString(2));
		baseAddressVo.setCountry(rs.getInt(3));
		baseAddressVo.setAddress_1(rs.getString(4));
		baseAddressVo.setAddress_2(rs.getString(5));
		baseAddressVo.setState(rs.getInt(6));
		baseAddressVo.setCity(rs.getString(7));
		baseAddressVo.setZipCode(rs.getString(8));
		baseAddressVo.setLandMark(rs.getString(9));
		baseAddressVo.setGstNo(rs.getString(10));
	}

	private void organizationAddressPreparestatement(PreparedStatement preparedStatement,
			BaseAddressVo organizationDeliveryAddress, Integer vendorCreditGeneralInfoId) throws SQLException {
		preparedStatement.setString(1, organizationDeliveryAddress.getPhoneNo());
		preparedStatement.setInt(2, organizationDeliveryAddress.getCountry());
		preparedStatement.setString(3, organizationDeliveryAddress.getAddress_1());
		preparedStatement.setString(4, organizationDeliveryAddress.getAddress_2());
		preparedStatement.setInt(5, organizationDeliveryAddress.getState());
		preparedStatement.setString(6, organizationDeliveryAddress.getCity());
		preparedStatement.setString(7, organizationDeliveryAddress.getZipCode());
		preparedStatement.setString(8, organizationDeliveryAddress.getLandMark());
		preparedStatement.setString(9, organizationDeliveryAddress.getGstNo());
		preparedStatement.setTimestamp(10, new Timestamp(new java.util.Date().getTime()));
		preparedStatement.setInt(11, vendorCreditGeneralInfoId);
	}

	private void organizationAddressPreparestatement1(PreparedStatement preparedStatement,
			BaseAddressVo organizationDeliveryAddress, Integer vendorCreditGeneralInfoId) throws SQLException {
		preparedStatement.setString(1, organizationDeliveryAddress.getPhoneNo());
		preparedStatement.setInt(2, organizationDeliveryAddress.getCountry());
		preparedStatement.setString(3, organizationDeliveryAddress.getAddress_1());
		preparedStatement.setString(4, organizationDeliveryAddress.getAddress_2());
		preparedStatement.setInt(5, organizationDeliveryAddress.getState());
		preparedStatement.setString(6, organizationDeliveryAddress.getCity());
		preparedStatement.setString(7, organizationDeliveryAddress.getZipCode());
		preparedStatement.setString(8, organizationDeliveryAddress.getLandMark());
		preparedStatement.setString(9, organizationDeliveryAddress.getGstNo());
		preparedStatement.setTimestamp(10, new Timestamp(new java.util.Date().getTime()));
		preparedStatement.setInt(11, vendorCreditGeneralInfoId);
	}

	private void updateVendorOriginAndBillingByGstAndVendorId(VendorCreditCreateVo vendorCreditCreateVo, Connection con)
			throws ApplicationException {

		BaseAddressVo billingBaseAddress = vendorCreditCreateVo.getGeneralInfo().getAddress().getVendorBillingAddress();
		BaseAddressVo deliveryBaseAddress = vendorCreditCreateVo.getGeneralInfo().getAddress()
				.getVendorDeliveryAddress();

		VendorOriginAddressVo vendorOriginAddressVo = gson.fromJson(gson.toJson(billingBaseAddress),
				VendorOriginAddressVo.class);
		VendorDestinationAddressVo destinationAddressVo = gson.fromJson(gson.toJson(deliveryBaseAddress),
				VendorDestinationAddressVo.class);
		vendorOriginAddressVo.setGstNo(vendorCreditCreateVo.getGeneralInfo().getVendorGstNo());
		destinationAddressVo.setGstNo(vendorCreditCreateVo.getGeneralInfo().getVendorGstNo());
		vendorDao.updateVendorOriginAddress(vendorOriginAddressVo, con,
				vendorCreditCreateVo.getGeneralInfo().getVendorId());

		vendorDao.updateVendorDestinationAddress(destinationAddressVo, con,
				vendorCreditCreateVo.getGeneralInfo().getVendorId());

	}

	private void getVendorOriginAndBillingByGstAndVendorId(VendorCreditCreateVo vendorCreditCreateVo, Connection con)
			throws ApplicationException {
		BaseAddressVo baseBillingAddressVo = gson.fromJson(gson.toJson(
				vendorDao.getVendorsOriginAddressByVendorIdAndGstNo(vendorCreditCreateVo.getGeneralInfo().getVendorId(),
						vendorCreditCreateVo.getGeneralInfo().getVendorGstNo(),con)),
				BaseAddressVo.class);

		vendorCreditCreateVo.getGeneralInfo().setAddress(new VendorCreditAddressVo());

		vendorCreditCreateVo.getGeneralInfo().getAddress().setVendorBillingAddress(baseBillingAddressVo);

		BaseAddressVo baseDeliveryAddressVo = gson.fromJson(gson.toJson(vendorDao
				.getVendorDestinationAddressByVendorIdAndGstNo(vendorCreditCreateVo.getGeneralInfo().getVendorId(),
						vendorCreditCreateVo.getGeneralInfo().getVendorGstNo(),con)),
				BaseAddressVo.class);

		vendorCreditCreateVo.getGeneralInfo().getAddress().setVendorDeliveryAddress(baseDeliveryAddressVo);

	}

	private Integer createVendorCreditGeneralInformation(VendorCreditCreateVo vendorCreditCreateVo, Connection con)
			throws SQLException, ApplicationException {
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;

		try {
			preparedStatement = con.prepareStatement(VendorCreditConstants.INSERT_VENDOR_CREDIT_GENERAL_INFO,
					Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setInt(1, vendorCreditCreateVo.getOrganizationId());
			preparedStatement.setInt(2, vendorCreditCreateVo.getUserId());
			preparedStatement.setString(3, vendorCreditCreateVo.getRoleName());
			preparedStatement.setInt(4, vendorCreditCreateVo.getGeneralInfo().getVendorId());
			preparedStatement.setString(5, vendorCreditCreateVo.getGeneralInfo().getVendorGstNo());
			preparedStatement.setInt(6, vendorCreditCreateVo.getGeneralInfo().getLocationId());
			preparedStatement.setString(7, vendorCreditCreateVo.getGeneralInfo().getOrgGst());
			preparedStatement.setInt(8, vendorCreditCreateVo.getGeneralInfo().getInvoiceId());
			preparedStatement.setString(9, vendorCreditCreateVo.getGeneralInfo().getCreditNoteNo());
			preparedStatement.setString(10, vendorCreditCreateVo.getGeneralInfo().getCreditNoteDate());
			preparedStatement.setString(11, vendorCreditCreateVo.getGeneralInfo().getVoucherNo());
			preparedStatement.setString(12, vendorCreditCreateVo.getGeneralInfo().getVoucherDate());
			preparedStatement.setString(13, vendorCreditCreateVo.getGeneralInfo().getAdditionalTerms());
			preparedStatement.setString(14, vendorCreditCreateVo.getGeneralInfo().getNotes());
			preparedStatement.setString(15, vendorCreditCreateVo.getGeneralInfo().getReason());

			String glData = vendorCreditCreateVo.getGeneralLedgerData() != null
					? mapper.writeValueAsString(vendorCreditCreateVo.getGeneralLedgerData())
					: null;

			preparedStatement.setString(16, glData);
			preparedStatement.setString(17, vendorCreditCreateVo.getGeneralInfo().getAksharData());

			int rowAffected = preparedStatement.executeUpdate();
			if (rowAffected == 1) {
				rs = preparedStatement.getGeneratedKeys();
				if (rs.next()) {
					return rs.getInt(1);
				}
			}
		} catch (Exception e) {
			throw new ApplicationException(e.getMessage());

		} finally {
			closeResources(rs, preparedStatement, null);

		}

		return null;
	}

	/**
	 * Update vendor advance credit by id.
	 *
	 * @param vendorCreditId     the vendor credit id
	 * @param connection         the connection
	 * @param totalAppliedAmount the total applied amount
	 * @throws ApplicationException the application exception
	 */
	public void updateVendorAdvanceCreditById(int vendorCreditId, Connection connection, double totalAppliedAmount)
			throws ApplicationException {

		PreparedStatement preparedStatement = null;
		ResultSet rs = null;

		try {
			preparedStatement = connection
					.prepareStatement(VendorCreditConstants.FETCH_VENDOR_CREDIT_STATUS_AND_BALANCE_BY_ID);
			preparedStatement.setInt(1, vendorCreditId);
			rs = preparedStatement.executeQuery();
			if (rs.next()) {
				double availableBalance = rs.getDouble(1);
				String status = rs.getString(2);
				double remainingBalance = availableBalance - totalAppliedAmount;
				String updatedStatus = getStatusBaseOfRemainingBalance(remainingBalance);
				try (PreparedStatement updateBalanceAndStatus = connection
						.prepareStatement(VendorCreditConstants.UPDATE_VENDOR_CREDIT_STATUS_AND_BALANCE_BY_ID)) {
					updateBalanceAndStatus.setDouble(1, remainingBalance);
					updateBalanceAndStatus.setString(2, updatedStatus);
					updateBalanceAndStatus.setInt(3, vendorCreditId);
					updateBalanceAndStatus.executeUpdate();
				}
			}

		} catch (Exception e) {
			throw new ApplicationException(e.getMessage());

		} finally {
			closeResources(rs, preparedStatement, null);
		}

	}

	private String getStatusBaseOfRemainingBalance(double remainingBalance) throws ApplicationException {
		if (remainingBalance > 0.0) {
			return "Partially Adjusted";
		} else if (remainingBalance == 0.0) {
			return "Adjusted";
		} else {
			throw new ApplicationException(
					"Can not perform operation as available balance is less than applied balance");
		}
	}

}

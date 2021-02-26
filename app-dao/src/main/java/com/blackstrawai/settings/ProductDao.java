package com.blackstrawai.settings;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.ap.dropdowns.BasicProductVo;
import com.blackstrawai.attachments.AttachmentsConstants;
import com.blackstrawai.attachments.AttachmentsDao;
import com.blackstrawai.attachments.AttachmentsVo;
import com.blackstrawai.attachments.UploadFileVo;
import com.blackstrawai.common.BaseDao;
import com.blackstrawai.common.CommonConstants;
import com.blackstrawai.externalintegration.general.aadhaar.AksharLineItemVo;
import com.blackstrawai.externalintegration.general.akshar.AksharProductItemVo;
import com.blackstrawai.externalintegration.general.akshar.AksharProductVo;

@Repository
public class ProductDao extends BaseDao {

	private Logger logger = Logger.getLogger(ProductDao.class);

	@Autowired
	private TaxGroupDao taxGroupDao;
	
	@Autowired
	private AttachmentsDao attachmentsDao;
	
	public ProductVo createProduct(ProductVo productVo) throws ApplicationException {
		logger.info("Entry into method:createProduct");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = getUserMgmConnection();
			boolean isProductExist=checkProductExist(productVo.getProductId(),productVo.getOrganizationId(),con);
			if(isProductExist){
				throw new Exception("Product Exist for the Organization");
			}
			String sql = SettingsAndPreferencesConstants.INSERT_INTO_PRODUCT_MANAGEMENT_ORGANIZATION;
			preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);	
			preparedStatement.setString(1,productVo.getType()!=null ?productVo.getType():null);
			preparedStatement.setString(2, productVo.getProductId()!=null ?productVo.getProductId():null);
			preparedStatement.setString(3,productVo.getName()!=null?productVo.getName():null);
			preparedStatement.setString(4,productVo.getHsn()!=null?productVo.getHsn():null);
			preparedStatement.setString(5,productVo.getDescription()!=null?productVo.getDescription():null);
			preparedStatement.setInt(6,productVo.getUnitMeasureId()!=null?productVo.getUnitMeasureId():0);
			preparedStatement.setString(7,productVo.getDefaultTaxPreference()!=null?productVo.getDefaultTaxPreference():null);
			preparedStatement.setObject(8, productVo.getTaxGroupIdInter()!=null?productVo.getTaxGroupIdInter():null);
			preparedStatement.setObject(9, productVo.getTaxGroupIdIntra()!=null?productVo.getTaxGroupIdIntra():null);
			preparedStatement.setString(10, productVo.getUnitPricePurchase()!=null?productVo.getUnitPricePurchase():null);
			preparedStatement.setInt(11,productVo.getPurchaseAccountId()!=null?productVo.getPurchaseAccountId():0);
			preparedStatement.setString(12, productVo.getPurchaseAccountLevel()!=null?productVo.getPurchaseAccountLevel():null);
			preparedStatement.setString(13, productVo.getPurchaseAccountName()!=null?productVo.getPurchaseAccountName():null);
			preparedStatement.setString(14,productVo.getMinimimOrderQuantity()!=null?productVo.getMinimimOrderQuantity():null);
			preparedStatement.setInt(15,productVo.getSalesAccountId()!=null?productVo.getSalesAccountId():0);
			preparedStatement.setString(16, productVo.getSalesAccountLevel()!=null?productVo.getSalesAccountLevel():null);
			preparedStatement.setString(17, productVo.getSalesAccountName()!=null?productVo.getSalesAccountName():null);
			preparedStatement.setString(18,productVo.getQuantity()!=null?productVo.getQuantity():null);
			preparedStatement.setString(19,productVo.getUnitPriceSale()!=null?productVo.getUnitPriceSale():null);
			preparedStatement.setInt(20,productVo.getOrganizationId()!=null?productVo.getOrganizationId():0);
			preparedStatement.setInt(21, Integer.valueOf(productVo.getUserId()));
			preparedStatement.setBoolean(22,productVo.getIsSuperAdmin()!=null?productVo.getIsSuperAdmin():null);
			preparedStatement.setString(23, productVo.getRoleName());
			preparedStatement.setString(24, productVo.getMrpPurchase()!=null ?  productVo.getMrpPurchase() : null);
			preparedStatement.setString(25, productVo.getMrpSales()!=null ? productVo.getMrpSales() : null);
			preparedStatement.setString(26, productVo.getCategory()!=null ? productVo.getCategory() : null);
			preparedStatement.setString(27, productVo.getOpeningStockQuantity()!=null ?  productVo.getOpeningStockQuantity() : null);			preparedStatement.setInt(28, productVo.getStockValueCurrencyId()!=null ?   productVo.getStockValueCurrencyId() : 0 );
            preparedStatement.setInt(28, productVo.getStockValueCurrencyId()!=null ?   productVo.getStockValueCurrencyId() : 0 );
			preparedStatement.setString(29, productVo.getOpeningStockValue()!=null ? productVo.getOpeningStockValue() : null);
			preparedStatement.setBoolean(30, productVo.isShowPurchaseLedger());
			preparedStatement.setBoolean(31, productVo.isShowSalesLedger());
			preparedStatement.setBoolean(32, productVo.isShowInventoryMgmt());
			int rowAffected = preparedStatement.executeUpdate();
			if (rowAffected == 1) {
				rs = preparedStatement.getGeneratedKeys();
				if (rs.next()) {
					productVo.setId(rs.getInt(1));
				}
			}
			
			if (productVo.getAttachments() != null && productVo.getAttachments().size() > 0) {
				attachmentsDao.createAttachments(productVo.getOrganizationId(), productVo.getUserId(),
						productVo.getAttachments(), AttachmentsConstants.PRODUCT, productVo.getId(),
						productVo.getRoleName());
			}

		}catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return productVo;
	}

	private boolean checkProductExist(String productId, Integer organizationId, Connection con) throws ApplicationException{
		logger.info("Entry into method:checkProductExist");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			String query = SettingsAndPreferencesConstants.CHECK_PRODUCT_EXIST_ORGANIZATION;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, organizationId);
			preparedStatement.setString(2, productId);
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

	public List<ListBasicProductVo> getAllProductsOfAnOrganization(int organizationId) throws ApplicationException {
		logger.info("Entry into method:getAllProductsOfAnOrganization");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		List<ListBasicProductVo> listOfProducts=new ArrayList<ListBasicProductVo>();
		try {
			con = getUserMgmConnection();
			String query = SettingsAndPreferencesConstants.GET_PRODUCT_ORGANIZATION;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, organizationId);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				ListBasicProductVo listBasicProductVo=new ListBasicProductVo();
				listBasicProductVo.setId(rs.getInt(1));
				listBasicProductVo.setProductId(rs.getString(2));
				listBasicProductVo.setName(rs.getString(3));
				listBasicProductVo.setUnitPricePurchase(rs.getString(4));
				listBasicProductVo.setUnitPriceSale(rs.getString(5));
				listBasicProductVo.setOrganizationId(rs.getInt(6));
				listBasicProductVo.setUserId(rs.getString(7));
				listBasicProductVo.setIsSuperAdmin(rs.getBoolean(8));
				listBasicProductVo.setStatus(rs.getString(9));
				listOfProducts.add(listBasicProductVo);
			}
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return listOfProducts;
	}

	public List<ListBasicProductVo> getAllProductsOfAnOrganizationForUserAndRole(int organizationId,String userId,String roleName) throws ApplicationException {
		logger.info("Entry into method:getAllProductsOfAnOrganizationForUserAndRole");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		List<ListBasicProductVo> listOfProducts=new ArrayList<ListBasicProductVo>();
		try {
			con = getUserMgmConnection();
			String query="";
			if(roleName.equals("Super Admin")){
				query = SettingsAndPreferencesConstants.GET_PRODUCT_ORGANIZATION;
			}else{
				query = SettingsAndPreferencesConstants.GET_PRODUCT_ORGANIZATION_USER_ROLE;
			}
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, organizationId);
			if(!(roleName.equals("Super Admin"))){
				preparedStatement.setString(2, userId);
				preparedStatement.setString(3, roleName);
			}	
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				ListBasicProductVo listBasicProductVo=new ListBasicProductVo();
				listBasicProductVo.setId(rs.getInt(1));
				listBasicProductVo.setProductId(rs.getString(2));
				listBasicProductVo.setName(rs.getString(3));
				listBasicProductVo.setUnitPricePurchase(rs.getString(4));
				listBasicProductVo.setUnitPriceSale(rs.getString(5));
				listBasicProductVo.setOrganizationId(rs.getInt(6));
				listBasicProductVo.setUserId(rs.getString(7));
				listBasicProductVo.setIsSuperAdmin(rs.getBoolean(8));
				listBasicProductVo.setStatus(rs.getString(9));
				listOfProducts.add(listBasicProductVo);
			}
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return listOfProducts;
	}

	public ProductVo getProductById(int id) throws ApplicationException {
		logger.info("Entry into method:getProductById");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		ProductVo productVo = new ProductVo();
		try{
			con = getUserMgmConnection();
			String query = SettingsAndPreferencesConstants.GET_PRODUCT_BY_ID_ORGANIZATION;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1,id);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				productVo.setId(rs.getInt(1));
				productVo.setType(rs.getString(2));
				productVo.setProductId(rs.getString(3));
				productVo.setName(rs.getString(4));
				productVo.setHsn(rs.getString(5));
				productVo.setDescription(rs.getString(6));
				productVo.setUnitMeasureId(rs.getInt(7));
				productVo.setDefaultTaxPreference(rs.getString(8));
				if(rs.getInt(9)==0)
					productVo.setTaxGroupIdInter(null);
				else
					productVo.setTaxGroupIdInter(rs.getInt(9));
				if(rs.getInt(10)==0)
					productVo.setTaxGroupIdIntra(null);
				else
					productVo.setTaxGroupIdIntra(rs.getInt(10));

				/*productVo.setTaxGroupIdInter(rs.getInt(9));
			productVo.setTaxGroupIdIntra(rs.getInt(10));*/
				productVo.setUnitPricePurchase(rs.getString(11));
				productVo.setPurchaseAccountId(rs.getInt(12));
				productVo.setPurchaseAccountLevel(rs.getString(13));
				productVo.setPurchaseAccountName(rs.getString(14));
				productVo.setMinimimOrderQuantity(rs.getString(15));
				productVo.setSalesAccountId(rs.getInt(16));
				productVo.setSalesAccountLevel(rs.getString(17));
				productVo.setSalesAccountName(rs.getString(18));
				productVo.setQuantity(rs.getString(19));
				productVo.setUnitPriceSale(rs.getString(20));
				productVo.setOrganizationId(rs.getInt(21));
				productVo.setUserId(rs.getString(22));
				productVo.setIsSuperAdmin(rs.getBoolean(23));
				productVo.setStatus(rs.getString(24));
				productVo.setMrpPurchase(rs.getString(25));
				productVo.setMrpSales(rs.getString(26));
				productVo.setCategory(rs.getString(27));
			    productVo.setOpeningStockQuantity(rs.getString(28));
				productVo.setStockValueCurrencyId(rs.getInt(29));
				productVo.setOpeningStockValue(rs.getString(30));
				productVo.setShowPurchaseLedger(rs.getBoolean(31));
				productVo.setShowSalesLedger(rs.getBoolean(32));
				productVo.setShowInventoryMgmt(rs.getBoolean(33));
				List<UploadFileVo> uploadFileVos = new ArrayList<UploadFileVo>();
				for (AttachmentsVo attachments : attachmentsDao.getAttachmentDetails(productVo.getId(),
						AttachmentsConstants.PRODUCT)) {
					UploadFileVo uploadFileVo = new UploadFileVo();
					uploadFileVo.setId(attachments.getId());
					uploadFileVo.setName(attachments.getFileName());
					uploadFileVo.setSize(attachments.getSize());
					uploadFileVos.add(uploadFileVo);
				}
				productVo.setAttachments(uploadFileVos);
				
			}
		}catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return productVo;
	}


	public List<AksharProductItemVo> getProductsGivenNames(AksharProductVo data) throws ApplicationException {
		logger.info("Entry into getProductsGivenNames");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		List<AksharProductItemVo>listOfProduct=new ArrayList<AksharProductItemVo>();
		try{
			con = getUserMgmConnection();
			String query = SettingsAndPreferencesConstants.GET_PRODUCT_BY_NAME;
			preparedStatement = con.prepareStatement(query);
			for(int i=0;i<data.getLineItem().size();i++){
				AksharLineItemVo lineItemVo=data.getLineItem().get(i);
				AksharProductItemVo productVo=null;
				preparedStatement.setInt(1,data.getOrganizationId());
				preparedStatement.setString(2,lineItemVo.getItemName());
				rs = preparedStatement.executeQuery();
				if (rs.next()) {
					Map<String , String> taxRateMap = taxGroupDao.getTaxRatesMappingForOrganization(data.getOrganizationId());
					productVo = new AksharProductItemVo();
					productVo.setId(rs.getInt(1));
					rs.getString(2);
					productVo.setProductId(rs.getString(3));
					productVo.setProductDisplayname(rs.getString(4));
					productVo.setHsn_sac_code(lineItemVo.getHsn_sac_code());
					rs.getString(6);
					productVo.setMeasureId(rs.getInt(7));
					productVo.setUnitPrice(lineItemVo.getRate());
					productVo.setProductAccountId(rs.getInt(12));
					productVo.setProductAccountLevel(rs.getString(13));
					productVo.setProductAccountName(rs.getString(14));
					productVo.setQuantity(lineItemVo.getQuantity());
					rs.getInt(16);
					rs.getString(17);
					rs.getString(18);
					rs.getString(19);
					rs.getString(20);
					rs.getInt(21);
					rs.getString(22);
					rs.getBoolean(23);
					productVo.setStatus("NEW");
					productVo.setAmount(lineItemVo.getAmount());
					productVo.setIsProductExist(true);
					boolean isproceed = true;
					if(lineItemVo.getTax()!=null && lineItemVo.getTax().size()>0) {
						for(String value:lineItemVo.getTax() ) {
							if(isproceed) {
							String[] taxrateArray = value.split("_");
							String taxType = taxrateArray[0];
							logger.info("taxType::"+taxrateArray[0]);
							String taxRate = taxrateArray[1].replace("%", "").trim();
							logger.info("taxRate::"+taxRate);
							String rate = null;

							if(taxRate.contains(".") && ("00".equals(taxRate.split("\\.")[1]) || "0".equals(taxRate.split("\\.")[1])) ){
								rate= taxRate.split("\\.")[0];
							}else {
								rate =taxRate;
							}
							logger.info("taxType::"+taxType+"  taxRate:::"+taxRate+ "  rate::"+rate);
							if(taxRateMap.containsKey(taxType+" "+rate)) {
								logger.info("contains key :: "+taxType+" "+rate);
								productVo.setIsTaxGroupExist(true);
							}else {
								logger.info("does not contains key :: "+taxType+" "+rate);
								isproceed = false;
							}
						}
						}
					}
					if(productVo.getIsTaxGroupExist()) {
						logger.info("to get the tax rate id");
						//Map<String , Integer> taxGroupMap = new HashMap<String, Integer>();
						List<TaxGroupVo> taxGroupList = taxGroupDao.getTaxGroupForOrganization(data.getOrganizationId());
					//	if(taxGroupList!=null && taxGroupList.size()>0) {
						//	 taxGroupMap = taxGroupList.stream().collect(Collectors.toMap(TaxGroupVo::getTaxesIncluded, TaxGroupVo::getId));
						//	}
						for(TaxGroupVo tax : taxGroupList) {
							StringBuilder  match = new StringBuilder();
							String[] taxes = tax.getTaxesIncluded().split(",");
							logger.info(taxes[0]+"taxes");
							if(taxes.length>=0) {
								for(String value:lineItemVo.getTax() ) {
									 
									String[] taxrateArray = value.split("_");
									String taxType = taxrateArray[0];
									String taxRate = taxrateArray[1].replace("%", "").trim();
									String rate = null;
									if(taxRate.contains(".") && ("00".equals(taxRate.split("\\.")[1]) || "0".equals(taxRate.split("\\.")[1])) ){
										rate= taxRate.split("\\.")[0];
									}else {
										rate =taxRate;
									}
								//	logger.info("taxType::"+taxType+"  taxRate:::"+taxRate+ "  rate::"+rate);
									String taxvalue = taxType+" "+rate;
									for(int j = 0;j<taxes.length;j++) {
									//	logger.info("taxes[j]"+j+ taxes[j] + "taxvalue is:"+taxvalue);
										if(taxvalue.equals(taxes[j].trim())) {
											logger.info("To append zero");
											match.append("0");
										}
									}
								}
							}
							logger.info("match::"+match.toString());
							logger.info("match.toString().length()"+match.toString().length());
							if(match.toString().length()==lineItemVo.getTax().size()) {
								productVo.setTaxRateId(tax.getId());
								break;
							}else {
								productVo.setTaxRateId(0);
							}
						}
						
					}
				}else{
					productVo = new AksharProductItemVo();
					productVo.setProductDisplayname(lineItemVo.getItemName());
					productVo.setIsProductExist(false);
					productVo.setUnitPrice(lineItemVo.getRate());
					productVo.setAmount(lineItemVo.getAmount());
					productVo.setHsn_sac_code(lineItemVo.getHsn_sac_code());
					productVo.setQuantity(lineItemVo.getQuantity());
				}
				listOfProduct.add(productVo);
			}
		}catch (Exception e) {
			logger.info("error ::"+e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}

		return listOfProduct;
	}


	public ProductVo deleteProduct(int id,String status,String userId,String roleName) throws ApplicationException {
		logger.info("Entry into method:deleteProduct");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		ProductVo productVo = new ProductVo();
		try {
			con = getUserMgmConnection();
			String sql = SettingsAndPreferencesConstants.DELETE_PRODUCT_ORGANIZATION;
			preparedStatement = con.prepareStatement(sql);
			preparedStatement.setString(1, status);
			preparedStatement.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
			preparedStatement.setString(3, userId);
			preparedStatement.setString(4, roleName);
			preparedStatement.setInt(5, id);
			productVo.setId(id);
			preparedStatement.executeUpdate();
		}catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return productVo;
	}

	public ProductVo updateProduct(ProductVo productVo) throws ApplicationException {
		logger.info("Entry into method:updateProduct");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = getUserMgmConnection();
			String sql = SettingsAndPreferencesConstants.UPDATE_PRODUCT_ORGANIZATION;
			preparedStatement = con.prepareStatement(sql);
			preparedStatement.setString(1,productVo.getType()!=null ?productVo.getType():null);
			preparedStatement.setString(2, productVo.getProductId()!=null ?productVo.getProductId():null);
			preparedStatement.setString(3,productVo.getName()!=null?productVo.getName():null);
			preparedStatement.setString(4,productVo.getHsn()!=null?productVo.getHsn():null);
			preparedStatement.setString(5,productVo.getDescription()!=null?productVo.getDescription():null);
			preparedStatement.setInt(6,productVo.getUnitMeasureId()!=null?productVo.getUnitMeasureId():null);
			preparedStatement.setString(7,productVo.getDefaultTaxPreference()!=null?productVo.getDefaultTaxPreference():null);
			preparedStatement.setObject(8, productVo.getTaxGroupIdInter()!=null?productVo.getTaxGroupIdInter():null);
			preparedStatement.setObject(9, productVo.getTaxGroupIdIntra()!=null?productVo.getTaxGroupIdIntra():null);
			preparedStatement.setString(10, productVo.getUnitPricePurchase()!=null?productVo.getUnitPricePurchase():null);
			preparedStatement.setInt(11,productVo.getPurchaseAccountId()!=null?productVo.getPurchaseAccountId():0);
			preparedStatement.setString(12, productVo.getPurchaseAccountLevel()!=null?productVo.getPurchaseAccountLevel():null);
			preparedStatement.setString(13, productVo.getPurchaseAccountName()!=null?productVo.getPurchaseAccountName():null);
			preparedStatement.setString(14,productVo.getMinimimOrderQuantity()!=null?productVo.getMinimimOrderQuantity():null);
			preparedStatement.setInt(15,productVo.getSalesAccountId()!=null?productVo.getSalesAccountId():0);
			preparedStatement.setString(16, productVo.getSalesAccountLevel()!=null?productVo.getSalesAccountLevel():null);
			preparedStatement.setString(17, productVo.getSalesAccountName()!=null?productVo.getSalesAccountName():null);
			preparedStatement.setString(18,productVo.getQuantity()!=null?productVo.getQuantity():null);
			preparedStatement.setString(19,productVo.getUnitPriceSale()!=null?productVo.getUnitPriceSale():null);
			preparedStatement.setInt(20,productVo.getOrganizationId()!=null?productVo.getOrganizationId():0);
			preparedStatement.setInt(21, Integer.valueOf(productVo.getUserId()));
			preparedStatement.setBoolean(22,productVo.getIsSuperAdmin()!=null?productVo.getIsSuperAdmin():null);
			preparedStatement.setTimestamp(23, new Timestamp(System.currentTimeMillis()));
			preparedStatement.setString(24, productVo.getRoleName());
			preparedStatement.setString(25, productVo.getMrpPurchase()!=null ?  productVo.getMrpPurchase() : null);
			preparedStatement.setString(26, productVo.getMrpSales()!=null ? productVo.getMrpSales() : null);
			preparedStatement.setString(27, productVo.getCategory()!=null ? productVo.getCategory() : null);
			preparedStatement.setString(28, productVo.getOpeningStockQuantity()!=null ?  productVo.getOpeningStockQuantity() : null);
            preparedStatement.setInt(29, productVo.getStockValueCurrencyId()!=null ?   productVo.getStockValueCurrencyId() : 0 );
            preparedStatement.setString(30, productVo.getOpeningStockValue()!=null ? productVo.getOpeningStockValue() : null);
        	preparedStatement.setBoolean(31, productVo.isShowPurchaseLedger());
			preparedStatement.setBoolean(32, productVo.isShowSalesLedger());
			preparedStatement.setBoolean(33, productVo.isShowInventoryMgmt());
            preparedStatement.setInt(34, productVo.getId());
			preparedStatement.executeUpdate();
			
			if (productVo.getAttachments() != null && productVo.getAttachments().size() > 0) {
				attachmentsDao.createAttachments(productVo.getOrganizationId(), productVo.getUserId(),
						productVo.getAttachments(), AttachmentsConstants.PRODUCT, productVo.getId(),
						productVo.getRoleName());
			}

			if (productVo.getAttachmentsToRemove() != null && productVo.getAttachmentsToRemove().size() > 0) {
				for (Integer attachmentId : productVo.getAttachmentsToRemove()) {
					attachmentsDao.changeStatusForSingleAttachment(attachmentId, CommonConstants.STATUS_AS_DELETE,
							productVo.getUserId(), productVo.getRoleName());
				}
			}
			
			
		}
		catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return productVo;
	}

	//To get the product details for Product in Purchase type accounts  needed in Dropdowns
	public List<BasicProductVo> getBasicProduct(Integer organizationId,Connection con)throws ApplicationException{
		logger.info("Entry into method:getBasicProduct");
		List<BasicProductVo>productList=new ArrayList<BasicProductVo>();
		PreparedStatement preparedStatement =null;
		ResultSet rs =null;
		try
		{
			preparedStatement = con.prepareStatement(SettingsAndPreferencesConstants.GET_PRODUCT_PRGANIZATION);
			preparedStatement.setInt(1,organizationId);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				BasicProductVo data=new BasicProductVo();
				data.setId(rs.getInt(1));
				data.setName(rs.getString(2));
				data.setHsn(rs.getString(3));
				data.setPurchaseAccountId(rs.getInt(4));
				data.setPurchaseQuantity(rs.getDouble(5));
				data.setUnitMeasureId(rs.getInt(6));
				data.setUnitPrice(rs.getDouble(7));
				//data.setTaxRateGroupId(rs.getInt(8));
				productList.add(data);				
			}
		}catch(Exception e){
			logger.info("Error in method:getBasicProduct");
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, null);
		}
		return productList;
	}


	//To get the product details for Product in Purchase type accounts along with Tax group details needed in Dropdowns
	public List<BasicProductVo> getBasicProductWithTaxGroups(Integer organizationId,Connection con)throws ApplicationException{
		logger.info("Entry into method:getBasicProductWithTaxGroups");
		List<BasicProductVo>productList=new ArrayList<BasicProductVo>();
		PreparedStatement preparedStatement =null;
		ResultSet rs =null;
		try
		{
			preparedStatement = con.prepareStatement(SettingsAndPreferencesConstants.GET_PRODUCT_ORGANIZATION_WITH_TAX);
			preparedStatement.setInt(1,organizationId);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				BasicProductVo data=new BasicProductVo();
				data.setId(rs.getInt(1));
				data.setType(rs.getString(2));
				data.setName(rs.getString(3));
				data.setHsn(rs.getString(4));
				data.setUnitMeasureId(rs.getInt(5));
				data.setInterTaxRateGroupId(rs.getInt(6));
				data.setIntraTaxRateGroupId(rs.getInt(7));
				data.setUnitPrice(rs.getDouble(8));
				data.setPurchaseAccountId(rs.getInt(9));
				data.setPurchaseAccountLevel(rs.getString(10));
				data.setPurchaseAccountName(rs.getString(11));
				Double quantity = rs.getDouble(12);
				data.setPurchaseQuantity(quantity!=null && quantity!=0 ? quantity : 1);
				data.setSalesAccountId(rs.getInt(13));
				data.setSalesAccountLevel(rs.getString(14));
				data.setSalesAccountName(rs.getString(15));
				data.setSalesQuantity(rs.getDouble(16));
				Double saleQunatity = rs.getDouble(17);
				data.setSalesUnitPrice(saleQunatity!=null && saleQunatity!=0 ? saleQunatity : 1);
				data.setDescription(rs.getString(18));
				productList.add(data);				
			}
		}catch(Exception e){
			logger.info("Error in method:getBasicProduct");
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, null);
		}
		return productList;
	}

	public void createProductCategory(ProductCategoryVo productCategoryVo) throws ApplicationException {
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null ;
		try {
			con =  getInventoryMgmtConnection();
			boolean checkExist = checkProductCategoryExist(productCategoryVo.getType(), productCategoryVo.getOrganizationId(), productCategoryVo.getCategory(), con);
			if(checkExist) {
				throw new ApplicationException("Category already Exist");
			}
			preparedStatement = con.prepareStatement(SettingsAndPreferencesConstants.INSERT_INTO_PRODUCT_CATEGORY);
			preparedStatement.setString(1, productCategoryVo.getCategory());
			preparedStatement.setString(2, productCategoryVo.getType());
			preparedStatement.setString(3, productCategoryVo.getUserId());
			preparedStatement.setInt(4, productCategoryVo.getOrganizationId());
			preparedStatement.setString(5, productCategoryVo.getRoleName());
			preparedStatement.executeUpdate();
		}catch(Exception e){
			logger.info("Error in method:createProductCategory"+e);
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(resultSet, preparedStatement, con);
		}
		
	}

	
	public void updateProductCategory(ProductCategoryVo productCategoryVo) throws ApplicationException {
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null ;
		try {
			con =  getInventoryMgmtConnection();
			String category = getProductCategoryName(productCategoryVo.getId(), con);
			String updateCategory = null;
			if(category!=null && category.equals(productCategoryVo.getCategory())) {
				updateCategory = productCategoryVo.getCategory();
			}else {
				boolean exist = checkProductCategoryExist(productCategoryVo.getType(), productCategoryVo.getOrganizationId(), productCategoryVo.getCategory(), con);
				if(exist) {
					throw new ApplicationException("Category Already Exist");
				}else {
					updateCategory = productCategoryVo.getCategory();
				}
			}
			logger.info("Update Category:: "+ updateCategory);
			preparedStatement = con.prepareStatement(SettingsAndPreferencesConstants.UPDATE_PRODUCT_CATEGORY);
			preparedStatement.setString(1,updateCategory);
			preparedStatement.setString(2, productCategoryVo.getType());
			preparedStatement.setString(3, productCategoryVo.getUserId());
			preparedStatement.setString(4, productCategoryVo.getRoleName());
			preparedStatement.setDate(5, new Date(System.currentTimeMillis()));
			preparedStatement.setInt(6, productCategoryVo.getId());
			preparedStatement.executeUpdate();
		}catch(Exception e){
			logger.info("Error in method:createProductCategory"+e);
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(resultSet, preparedStatement, con);
		}
		
	}
	
	
	private boolean checkProductCategoryExist(String type, Integer organizationId, String category, Connection con) throws ApplicationException{
		logger.info("Entry into method:checkProductCategoryExist");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		boolean exist = false; 
		try {
			String query = SettingsAndPreferencesConstants.CHECK_CATEGORY_EXIST;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, organizationId);
			preparedStatement.setString(2, type);
			preparedStatement.setString(3, category);
			rs = preparedStatement.executeQuery();
			if (rs.next()) {
				exist = true;
			}
			logger.info("Exist ::"+ exist);
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, null);
		}
		return exist;
	}
	
	
	public String getProductCategoryName( Integer id, Connection con) throws ApplicationException{
		logger.info("Entry into method:checkProductCategoryExist");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		String  category = null; 
		try {
			String query = SettingsAndPreferencesConstants.GET_PRODUCT_CATEGORY_NAME;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, id);
			rs = preparedStatement.executeQuery();
			if (rs.next()) {
				category = rs.getString(1);
			}
			logger.info("Exist ::"+ category);
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, null);
		}
		return category;
	}
	
	
	
}

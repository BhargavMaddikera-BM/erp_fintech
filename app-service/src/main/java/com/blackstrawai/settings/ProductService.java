
package com.blackstrawai.settings;

import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.attachments.AttachmentService;
import com.blackstrawai.attachments.AttachmentsConstants;
import com.blackstrawai.common.BaseService;
import com.blackstrawai.common.CommonConstants;
import com.blackstrawai.common.DropDownDao;
import com.blackstrawai.common.FinanceCommonDao;
import com.blackstrawai.common.HsnCodeVo;
import com.blackstrawai.common.SaCodeVo;
import com.blackstrawai.externalintegration.general.akshar.AksharProductItemVo;
import com.blackstrawai.externalintegration.general.akshar.AksharProductVo;
import com.blackstrawai.inventorymgmt.ProductInventoryDao;


@Service
public class ProductService extends BaseService {

	@Autowired
	ProductDao productDao;
	
	@Autowired
	DropDownDao dropDownDao;
	
	@Autowired
	private FinanceCommonDao financeCommonDao;
	
	@Autowired
	private AttachmentService attachmentService;
	
	@Autowired
	private ProductInventoryDao productInventoryDao;
	
	private Logger logger = Logger.getLogger(ProductService.class);

	public ProductVo createProduct(ProductVo productVo) throws ApplicationException {
		logger.info("Entry into method:createProduct");
		productDao.createProduct(productVo);
		if (!productVo.getAttachments().isEmpty() && productVo.getId() != null) {
			logger.info("Entry into upload");
			attachmentService.upload(AttachmentsConstants.PRODUCT, productVo.getId(),
					productVo.getOrganizationId(), productVo.getAttachments());
			logger.info("Upload Successful");
		}
		
		if(productVo.getId()!=null && CommonConstants.STATUS_AS_ACTIVE.equals(productVo.getStatus()) && productVo.getOpeningStockQuantity()!=null && productVo.getOpeningStockValue()!=null) {
			logger.info("To add into inventory Mgmt");
			productInventoryDao.addBaseProductToInventoryMgmt(productVo);
		}
		return productVo;
	}

	public List<ListBasicProductVo> getAllProductsOfAnOrganization(int organizationId) throws ApplicationException {
		logger.info("Entry into method:getAllProductsOfAnOrganization");
		List<ListBasicProductVo> productList= productDao.getAllProductsOfAnOrganization(organizationId);
		Collections.reverse(productList);
		return productList;
	}
	
	public List<ListBasicProductVo> getAllProductsOfAnOrganizationForUserAndRole(int organizationId,String userId,String roleName) throws ApplicationException {
		logger.info("Entry into method:getAllProductsOfAnOrganizationForUserAndRole");
		List<ListBasicProductVo> productList= productDao.getAllProductsOfAnOrganizationForUserAndRole(organizationId,userId,roleName);
		Collections.reverse(productList);
		return productList;
	}

	public ProductVo getProductById(int id) throws ApplicationException {
		logger.info("Entry into method:getProductById");
		ProductVo productVo = productDao.getProductById(id);
		if (productVo != null && productVo.getAttachments()!=null && productVo.getAttachments().size() > 0 && productVo.getId() != null) {
			attachmentService.encodeAllFiles(productVo.getOrganizationId(), productVo.getId(),
					AttachmentsConstants.PRODUCT, productVo.getAttachments());
		}
		
		return productVo;
	}

	public ProductVo deleteProduct(int id,String status,String userId,String roleName) throws ApplicationException {
		logger.info("Entry into method:deleteProduct");
		 ProductVo productVo = productDao.deleteProduct(id,status,userId,roleName);
		 if(CommonConstants.STATUS_AS_DELETE .equals(productVo.getStatus())) {
			 productInventoryDao.deleteBaseProductToInventoryMgmt(productVo.getId(), productVo);
		 }
		 return productVo;
	}

	public ProductVo updateProduct(ProductVo productVo) throws ApplicationException {
		logger.info("Entry into method:updateProduct");
		productDao.updateProduct(productVo);
		if ( !productVo.getAttachments().isEmpty() && productVo.getId() != null) {
			logger.info("Entry into upload");
			attachmentService.upload(AttachmentsConstants.PRODUCT, productVo.getId(),
					productVo.getOrganizationId(), productVo.getAttachments());
			logger.info("Upload Successfull");
		}
		
		if(productVo.getId()!=null && CommonConstants.STATUS_AS_ACTIVE.equals(productVo.getStatus()) && productVo.getOpeningStockQuantity()!=null && productVo.getOpeningStockValue()!=null) {
			logger.info("To UPDATE into inventory Mgmt");
			productInventoryDao.updateBaseProductToInventoryMgmt(productVo);
		}
		
		return productVo;
	}
	
	public ProductDropDownVo getProductDropDownData(int organizationId,String type)throws ApplicationException {	
		logger.info("Entry into method: getProductDropDownData");
		return dropDownDao.getProductDropDownData(organizationId ,type);
	}
	
	
	public List<AksharProductItemVo> getProductsGivenNames(AksharProductVo data ) throws ApplicationException {
		logger.info("Entry into method:getProductsGivenNames");
		return productDao.getProductsGivenNames(data);
	}

	
	public List<SaCodeVo> getSACodes() throws ApplicationException {
		logger.info("Entry into method:getSACodes");
		return financeCommonDao.getSaCodes();
	}
	
	public List<HsnCodeVo> getHSNCodes() throws ApplicationException {
		logger.info("Entry into method:getHSNCodes");
		return financeCommonDao.getHsnCodes();
	}

	public void createProductCategory(ProductCategoryVo productCategoryVo) throws ApplicationException {
		logger.info("To create Product category ");
		productDao.createProductCategory(productCategoryVo);
		
	}

	public List<ProductCategoryVo> getProductCategoryByType(String type, int organizationId) throws ApplicationException {
		// TODO Auto-generated method stub
		return 	dropDownDao.getProductCategoryByType(type,organizationId );
	}

	public void updateProductCategory(ProductCategoryVo productCategoryVo) throws ApplicationException {
		logger.info("To update Product category ");
		productDao.updateProductCategory(productCategoryVo);		
	}
	
}

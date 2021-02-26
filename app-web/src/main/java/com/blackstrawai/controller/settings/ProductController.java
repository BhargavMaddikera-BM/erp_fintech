
package com.blackstrawai.controller.settings;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.common.BaseController;
import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.common.CommonConstants;
import com.blackstrawai.common.Constants;
import com.blackstrawai.common.HsnCodeVo;
import com.blackstrawai.common.SaCodeVo;
import com.blackstrawai.externalintegration.general.akshar.AksharProductItemVo;
import com.blackstrawai.externalintegration.general.akshar.AksharProductVo;
import com.blackstrawai.helper.SettingsConvertToVoHelper;
import com.blackstrawai.request.JSONObject;
import com.blackstrawai.request.externalintegration.general.AksharProductRequest;
import com.blackstrawai.request.settings.ProductCategoryRequest;
import com.blackstrawai.request.settings.ProductDropDownResponse;
import com.blackstrawai.request.settings.ProductRequest;
import com.blackstrawai.response.externalintegration.general.AksharProductResponse;
import com.blackstrawai.response.settings.HsnCodeResponse;
import com.blackstrawai.response.settings.ListProductResponse;
import com.blackstrawai.response.settings.ProductCategoryListResponse;
import com.blackstrawai.response.settings.ProductResponse;
import com.blackstrawai.response.settings.SaCodeResponse;
import com.blackstrawai.settings.ListBasicProductVo;
import com.blackstrawai.settings.ProductCategoryVo;
import com.blackstrawai.settings.ProductDropDownVo;
import com.blackstrawai.settings.ProductService;
import com.blackstrawai.settings.ProductVo;


@RestController
@CrossOrigin
//This will be part of Settings and Preferences. Hence sp.
@RequestMapping("/decifer/sp/product")
public class ProductController extends BaseController {

	@Autowired
	ProductService productService;

	private Logger logger = Logger.getLogger(ProductController.class);

	// For Creating Product

	@RequestMapping(value = "/v1/products", method = RequestMethod.POST)
	public ResponseEntity<BaseResponse> createProduct(HttpServletRequest httpRequest, HttpServletResponse httpResponse,

			@RequestBody JSONObject<ProductRequest> productRequest) {
		logger.info("Entry into method:createProduct");
		BaseResponse response = new ProductResponse();
		try {
			logger.info("Request Payload:" + generateRequestAndResponseLogPaylod(productRequest));
			ProductVo productVo = SettingsConvertToVoHelper.getInstance()
					.convertProductVoFromProductRequest(productRequest.getData());
			productVo = productService.createProduct(productVo);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((ProductResponse) response).setData(productVo);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_PRODUCT_CREATED,
					Constants.SUCCESS_PRODUCT_CREATED, Constants.PRODUCT_CREATED_SUCCESSFULLY);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			if (e.getCause() != null) {
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_PRODUCT_CREATED,
						e.getCause().getMessage(), Constants.PRODUCT_CREATED_UNSUCCESSFULLY);
			} else {
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_PRODUCT_CREATED,
						e.getMessage(), Constants.PRODUCT_CREATED_UNSUCCESSFULLY);
			}
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/*// For Fetching all Product details

	@RequestMapping(value = "/v1/products/{organizationId}/{organizationName}", method = RequestMethod.GET)
	public ResponseEntity<BaseResponse> getAllProductsOfAnOrganization(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @PathVariable int organizationId, @PathVariable String organizationName) {
		logger.info("Entry into method:getAllProductsOfAnOrganization");
		BaseResponse response = new ListProductResponse();
		try {
			List<ListBasicProductVo> listAllProducts = productService.getAllProductsOfAnOrganization(organizationId);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((ListProductResponse) response).setData(listAllProducts);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_PRODUCT_FETCH,
					Constants.SUCCESS_PRODUCT_FETCH, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_PRODUCT_FETCH, e.getMessage(),
					Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}*/

	@RequestMapping(value = "/v1/products/{organizationId}/{organizationName}/{userId}/{roleName}", method = RequestMethod.GET)
	public ResponseEntity<BaseResponse> getAllProductsOfAnOrganizationForUserAndRole(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @PathVariable int organizationId,@PathVariable String organizationName,
			@PathVariable String userId,@PathVariable String roleName) {
		logger.info("Entry into method:getAllProductsOfAnOrganizationForUserAndRole");
		BaseResponse response = new ListProductResponse();
		try {
			List<ListBasicProductVo> listAllProducts = productService.getAllProductsOfAnOrganizationForUserAndRole(organizationId,userId,roleName);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((ListProductResponse) response).setData(listAllProducts);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_PRODUCT_FETCH,
					Constants.SUCCESS_PRODUCT_FETCH, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_PRODUCT_FETCH, e.getMessage(),
					Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// For Fetching Product by Id

	@RequestMapping(value = "/v1/products/{organizationId}/{organizationName}/{userId}/{roleName}/{id}", method = RequestMethod.GET)
	public ResponseEntity<BaseResponse> getProductById(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, 
			@PathVariable int organizationId, 
			@PathVariable String organizationName,
			@PathVariable String userId,
			@PathVariable String roleName,
			@PathVariable int id) {
		logger.info("Entry into method:getProductById");
		BaseResponse response = new ProductResponse();
		try {
			ProductVo productVo = productService.getProductById(id);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((ProductResponse) response).setData(productVo);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_PRODUCT_FETCH,
					Constants.SUCCESS_PRODUCT_FETCH, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_PRODUCT_FETCH, e.getMessage(),
					Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// For Deleting Product

	@RequestMapping(value = "/v1/products/{organizationId}/{organizationName}/{userId}/{id}/{roleName}/{status}", method = RequestMethod.DELETE)
	public ResponseEntity<BaseResponse> deleteVoucher(HttpServletRequest httpRequest, 
			HttpServletResponse httpResponse,
			@PathVariable int organizationId, 
			@PathVariable String organizationName,
			@PathVariable String userId, 
			@PathVariable int id,
			@PathVariable String roleName,
			@PathVariable String status) {
		logger.info("Entry into method:deleteProduct");
		BaseResponse response = new ProductResponse();
		try {
			productService.deleteProduct(id,status,userId,roleName);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			if(status.equals(CommonConstants.STATUS_AS_ACTIVE)) {
				response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_PRODUCT_ACTIVATED,
						Constants.SUCCESS_PRODUCT_ACTIVATED, Constants.PRODUCT_DELETED_SUCCESSFULLY);
				logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			}else if(status.equals(CommonConstants.STATUS_AS_INACTIVE)) {
				response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_PRODUCT_DEACTIVATED,
						Constants.SUCCESS_PRODUCT_DEACTIVATED, Constants.PRODUCT_DELETED_SUCCESSFULLY);
			}
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_PRODUCT_DELETED, e.getMessage(),
					Constants.PRODUCT_DELETED_UNSUCCESSFULLY);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	// For Update Product

	@RequestMapping(value = "/v1/products", method = RequestMethod.PUT)
	public ResponseEntity<BaseResponse> updateProduct(HttpServletRequest httpRequest, HttpServletResponse httpResponse,
			@RequestBody JSONObject<ProductRequest> productRequest) {
		logger.info("Entry into method:updateProduct");
		BaseResponse response = new ProductResponse();
		try {
			logger.info("Request Payload:" + generateRequestAndResponseLogPaylod(productRequest));
			ProductVo productVo = SettingsConvertToVoHelper.getInstance()
					.convertProductVoFromProductRequest(productRequest.getData());
			productVo = productService.updateProduct(productVo);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((ProductResponse) response).setData(productVo);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_PRODUCT_UPDATED,
					Constants.SUCCESS_PRODUCT_UPDATED, Constants.PRODUCT_UPDATED_SUCCESSFULLY);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_PRODUCT_UPDATED, e.getMessage(),
					Constants.PRODUCT_UPDATED_UNSUCCESSFULLY);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/v1/products/info", method = RequestMethod.PUT)
	public ResponseEntity<BaseResponse> getProductInfo(HttpServletRequest httpRequest, HttpServletResponse httpResponse,
			@RequestBody JSONObject<AksharProductRequest> aksharProductRequest) {
		logger.info("Entry into method:updateProduct");
		BaseResponse response = new AksharProductResponse();
		try {
			logger.info("Request Payload:" + generateRequestAndResponseLogPaylod(aksharProductRequest));
			AksharProductVo data= SettingsConvertToVoHelper.getInstance()
					.convertAksharProductVoFromAksharProductRequest(aksharProductRequest.getData());
			List<AksharProductItemVo> listOfProductVo = productService.getProductsGivenNames(data);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((AksharProductResponse) response).setData(listOfProductVo);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_PRODUCT_FETCH,
					Constants.SUCCESS_PRODUCT_FETCH, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_PRODUCT_FETCH, e.getMessage(),
					Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/v1/dropsdowns/products/{organizationId}/{organizationName}/{userId}/{roleName}/{type}")
	public ResponseEntity<BaseResponse> getProductDropDowns(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse,
			@PathVariable int organizationId, 
			@PathVariable String organizationName,
			@PathVariable String userId,
			@PathVariable String roleName,
			@PathVariable String type) {
		logger.info("Entry into getCustomerDropDown");
		BaseResponse response = new ProductDropDownResponse();
		try {
			ProductDropDownVo data = productService.getProductDropDownData(organizationId ,type);
			setTokens(response, httpServletRequest.getHeader("keyToken"), httpServletRequest.getHeader("valueToken"));
			((ProductDropDownResponse) response).setData(data);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_DROP_DOWN_FETCH,
					Constants.SUCCESS_DROP_DOWN_FETCH, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);

		} catch (Exception e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_DROP_DOWN_FETCH, e.getMessage(),
					Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}



	@RequestMapping(value = "/v1/products/type/{name}", method = RequestMethod.GET)
	public ResponseEntity<BaseResponse> getHsnAndSacCodes(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @PathVariable String name) {
		logger.info("Entry into method:getHsnAndSacCodes");
		BaseResponse hsnResponse = null;
		BaseResponse saCodeResponse = null;
		try {
			if(name!=null) {
				if("goods".equals(name)  || "fixedAsset".equals(name)) {
					hsnResponse = new HsnCodeResponse();
					List<HsnCodeVo> hsnCodes = productService.getHSNCodes();
					setTokens(hsnResponse, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
					((HsnCodeResponse) hsnResponse).setData(hsnCodes);
					hsnResponse = constructResponse(hsnResponse, Constants.SUCCESS, Constants.SUCCESS_DROP_DOWN_FETCH,
							Constants.SUCCESS_PRODUCT_FETCH, Constants.SUCCESS_DURING_GET);
					logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(hsnResponse));
				}

				if("services".equals(name)) {
					saCodeResponse = new SaCodeResponse();
					List<SaCodeVo> saCodes = productService.getSACodes();
					setTokens(saCodeResponse, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
					((SaCodeResponse) saCodeResponse).setData(saCodes);
					saCodeResponse = constructResponse(saCodeResponse, Constants.SUCCESS, Constants.SUCCESS_DROP_DOWN_FETCH,
							Constants.SUCCESS_PRODUCT_FETCH, Constants.SUCCESS_DURING_GET);
					logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(saCodeResponse));
				}
			}
			return new ResponseEntity<BaseResponse>(hsnResponse!=null ? hsnResponse : saCodeResponse, HttpStatus.OK);

		} catch (ApplicationException e) {
			saCodeResponse = constructResponse(saCodeResponse, Constants.FAILURE, Constants.FAILURE_DROP_DOWN_FETCH, e.getMessage(),
					Constants.FAILURE_DURING_GET);
			hsnResponse = constructResponse(hsnResponse, Constants.FAILURE, Constants.FAILURE_DROP_DOWN_FETCH, e.getMessage(),
					Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(saCodeResponse!=null ? saCodeResponse : hsnResponse));
			logger.info(saCodeResponse!=null ? saCodeResponse : hsnResponse);
			return new ResponseEntity<BaseResponse>(saCodeResponse!=null ? saCodeResponse : hsnResponse , HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}



	@RequestMapping(value = "/v1/productcategory", method = RequestMethod.POST)
	public ResponseEntity<BaseResponse> createProductCategory(HttpServletRequest httpRequest, HttpServletResponse httpResponse,
			@RequestBody JSONObject<ProductCategoryRequest> categoryRequest) {
		logger.info("Entry into method:createProduct");
		BaseResponse response = new ProductResponse();
		try {
			logger.info("Request Payload:" + generateRequestAndResponseLogPaylod(categoryRequest));
			ProductCategoryVo productCategoryVo = SettingsConvertToVoHelper.getInstance()
					.convertProductCategoryVoFromRequest(categoryRequest.getData());
			productService.createProductCategory(productCategoryVo);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_PRODUCT_CATEGORY_CREATED,
					Constants.SUCCESS_PRODUCT_CATEGORY_CREATED, Constants.PRODUCT_CREATED_SUCCESSFULLY);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			if (e.getCause() != null) {
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_PRODUCT_CATEGORY_CREATED,
						e.getCause().getMessage(), Constants.PRODUCT_CREATED_UNSUCCESSFULLY);
			} else {
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_PRODUCT_CATEGORY_CREATED,
						e.getMessage(), Constants.PRODUCT_CREATED_UNSUCCESSFULLY);
			}
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}


	@RequestMapping(value = "/v1/productcategory", method = RequestMethod.PUT)
	public ResponseEntity<BaseResponse> updateProductCategory(HttpServletRequest httpRequest, HttpServletResponse httpResponse,
			@RequestBody JSONObject<ProductCategoryRequest> categoryRequest) {
		logger.info("Entry into method:createProduct");
		BaseResponse response = new ProductResponse();
		try {
			logger.info("Request Payload:" + generateRequestAndResponseLogPaylod(categoryRequest));
			ProductCategoryVo productCategoryVo = SettingsConvertToVoHelper.getInstance()
					.convertProductCategoryVoFromRequest(categoryRequest.getData());
			productService.updateProductCategory(productCategoryVo);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_PRODUCT_CATEGORY_UPDATED,
					Constants.SUCCESS_PRODUCT_CATEGORY_UPDATED, Constants.PRODUCT_CREATED_SUCCESSFULLY);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			if (e.getCause() != null) {
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_PRODUCT_CATEGORY_UPDATED,
						e.getCause().getMessage(), Constants.PRODUCT_CREATED_UNSUCCESSFULLY);
			} else {
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_PRODUCT_CATEGORY_UPDATED,
						e.getMessage(), Constants.PRODUCT_CREATED_UNSUCCESSFULLY);
			}
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/v1/productcategory/{organizationId}/{organizationName}/{userId}/{roleName}/{type}", method = RequestMethod.GET)
	public ResponseEntity<BaseResponse> getProductById(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, 
			@PathVariable int organizationId, 
			@PathVariable String organizationName,
			@PathVariable String userId,
			@PathVariable String roleName,
			@PathVariable String type) {
		logger.info("Entry into method:getProductById");
		BaseResponse response = new ProductCategoryListResponse();
		try {
			List<ProductCategoryVo> categoryListResponse = productService.getProductCategoryByType( type , organizationId);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((ProductCategoryListResponse) response).setData(categoryListResponse);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_DROP_DOWN_FETCH,
					Constants.SUCCESS_DROP_DOWN_FETCH, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_DROP_DOWN_FETCH, e.getMessage(),
					Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}

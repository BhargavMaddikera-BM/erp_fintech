package com.blackstrawai.controller.keycontact;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

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
import com.blackstrawai.common.BaseAddressVo;
import com.blackstrawai.common.BaseController;
import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.common.CommonConstants;
import com.blackstrawai.common.Constants;
import com.blackstrawai.externalintegration.compliance.TaxillaService;
import com.blackstrawai.externalintegration.compliance.taxilla.TaxillaAddr;
import com.blackstrawai.externalintegration.compliance.taxilla.TaxillaGsonVo;
import com.blackstrawai.externalintegration.compliance.taxilla.TaxillaPrAdrVo;
import com.blackstrawai.helper.KeyContactConvertToVoHelper;
import com.blackstrawai.keycontact.VendorService;
import com.blackstrawai.keycontact.dropdowns.VendorDropDownVo;
import com.blackstrawai.keycontact.vendor.VendorBasedOnGstVo;
import com.blackstrawai.keycontact.vendor.VendorBasicDetailsVo;
import com.blackstrawai.keycontact.vendor.VendorDestinationAddressVo;
import com.blackstrawai.keycontact.vendor.VendorOriginAddressVo;
import com.blackstrawai.keycontact.vendor.VendorVo;
import com.blackstrawai.request.JSONObject;
import com.blackstrawai.request.keycontact.vendor.VendorAddressBasedOnGstRequest;
import com.blackstrawai.request.keycontact.vendor.VendorRequest;
import com.blackstrawai.response.keycontact.vendor.VendorBasicDetailsResponse;
import com.blackstrawai.response.keycontact.vendor.VendorDropDownResponse;
import com.blackstrawai.response.keycontact.vendor.VendorResponse;

@RestController
@CrossOrigin
@RequestMapping("/decifer/ap/vendor")
public class VendorController extends BaseController {

	@Autowired
	VendorService vendorService;
	
	@Autowired
	private TaxillaService taxillaService;
	

	private Logger logger = Logger.getLogger(VendorController.class);

	// For Creating Vendor
	@RequestMapping(value = "/v1/vendors", method = RequestMethod.POST)
	public ResponseEntity<BaseResponse> createVendor(HttpServletRequest httpRequest, HttpServletResponse httpResponse,
			@RequestBody JSONObject<VendorRequest> vendorRequest) {
		logger.info("Entry into method: Create Vendor");
		BaseResponse response = new VendorResponse();
		try {
			logger.info("Request Payload:" + generateRequestAndResponseLogPaylod(vendorRequest));
			VendorVo vendorVo = KeyContactConvertToVoHelper.getInstance()
					.convertVendorVoFromVendorRequest(vendorRequest.getData());
			
			fetchAddressDetailsIfGstExist(vendorVo);
			
			vendorService.createVendor(vendorVo);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_VENDOR_CREATED,
					Constants.SUCCESS_VENDOR_CREATED, Constants.VENDOR_CREATED_SUCCESSFULLY);
			((VendorResponse)response).setData(vendorVo);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			logger.error("Error Payload:" +e.getMessage());
			if(e.getCause()!=null ) {
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_VENDOR_CREATED,
						e.getCause().getMessage(), Constants.VENDOR_CREATED_UNSUCCESSFULLY);
			}else {
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_VENDOR_CREATED,
						e.getMessage(), Constants.VENDOR_CREATED_UNSUCCESSFULLY);
			}
				
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	/**
	 * Fetch address details if gst exist.
	 *
	 * @param vendorVo the vendor vo
	 */
	private void fetchAddressDetailsIfGstExist(VendorVo vendorVo) {
		List<VendorBasedOnGstVo> basedOnGstVos = new ArrayList<>();
		
		if (vendorVo.getOriginalAndDestinationAddressBasedOnGst() != null) {
			basedOnGstVos.addAll(vendorVo.getOriginalAndDestinationAddressBasedOnGst());
		} else {

			List<String> gstNumbers = new ArrayList<>();
			gstNumbers.addAll(Arrays.asList(vendorVo.getVendorGeneralInformation().getOtherGsts()));
			if (vendorVo.getVendorGeneralInformation().getGstNo() != null) {
				gstNumbers.add(vendorVo.getVendorGeneralInformation().getGstNo());
			}

			gstNumbers.parallelStream().map(gstNo -> taxillaService.getByGst(gstNo)).filter(Objects::nonNull)
					.forEach(taxillaVo -> {
						VendorBasedOnGstVo vendorBasedOnGstVo = new VendorBasedOnGstVo();
						vendorBasedOnGstVo.setGstNo(taxillaVo.getGstin());
						getPrimaryAndBillingAddress(taxillaVo, vendorBasedOnGstVo);
						getAdditionalAddress(taxillaVo, vendorBasedOnGstVo);

						addVendorDetailsForDefaultGst(vendorVo, taxillaVo);
						basedOnGstVos.add(vendorBasedOnGstVo);
					});
		}
		
		vendorVo.setOriginalAndDestinationAddressBasedOnGst(basedOnGstVos);
	}

	/**
	 * Gets the additional address.
	 *
	 * @param taxillaVo the taxilla vo
	 * @param vendorBasedOnGstVo the vendor based on gst vo
	 * @return the additional address
	 */
	private void getAdditionalAddress(TaxillaGsonVo taxillaVo, VendorBasedOnGstVo vendorBasedOnGstVo) {
		if (taxillaVo.getAdadr() != null) {
			List<BaseAddressVo> baseAddressVos = new ArrayList<>();
			for(TaxillaPrAdrVo taxillaPrAdrVo :taxillaVo.getAdadr()) {
				baseAddressVos.add(convertToBaseAddressVo(taxillaPrAdrVo.getAddr(), taxillaVo.getGstin(), 2, null));							
			}
			baseAddressVos.add(convertToBaseAddressVo(taxillaVo.getPradr().getAddr(), taxillaVo.getGstin(), 1,3));
			vendorBasedOnGstVo.setVendorAdditionalAddresses(baseAddressVos);
		}
	}

	/**
	 * Gets the primary and billing address.
	 *
	 * @param taxillaVo the taxilla vo
	 * @param vendorBasedOnGstVo the vendor based on gst vo
	 * @return the primary and billing address
	 */
	private void getPrimaryAndBillingAddress(TaxillaGsonVo taxillaVo, VendorBasedOnGstVo vendorBasedOnGstVo) {
		if (taxillaVo.getPradr() != null) {
			vendorBasedOnGstVo.setVendorOriginAddress(convertToOriginalAddressVo(
					taxillaVo.getPradr().getAddr(), taxillaVo.getGstin(), 1));
			vendorBasedOnGstVo.setVendorDestinationAddress(
					convertToBillingAddressVo(taxillaVo.getPradr().getAddr(), taxillaVo.getGstin(), 1));
		}
	}

	/**
	 * Adds the vendor details for default gst.
	 *
	 * @param vendorVo the vendor vo
	 * @param taxillaVo the taxilla vo
	 */
	private void addVendorDetailsForDefaultGst(VendorVo vendorVo, TaxillaGsonVo taxillaVo) {
		if (taxillaVo.getGstin().equals(vendorVo.getVendorGeneralInformation().getGstNo())) {
			if (vendorVo.getVendorGeneralInformation().getVendorDisplayName() == null) {
				vendorVo.getVendorGeneralInformation().setVendorDisplayName(taxillaVo.getTradeNam());
			}
			if (vendorVo.getVendorGeneralInformation().getCompanyName() == null) {
				vendorVo.getVendorGeneralInformation().setCompanyName(taxillaVo.getLgnm());
			}
		}
	}

	/**
	 * Convert to origin and billing address vo.
	 *
	 * @param taxillaVo the taxilla vo
	 * @param vendorOriginAddressVo the vendor origin address vo
	 */
	private static BaseAddressVo convertToBaseAddressVo(TaxillaAddr taxillaAddr, String gstNo, int addressType, Integer selectedAddress) {
		BaseAddressVo baseAddressVo = new BaseAddressVo();
		baseAddressVo.setAddress_1(taxillaAddr.getBno() + taxillaAddr.getBnm());
		baseAddressVo.setAddress_2(taxillaAddr.getSt() + taxillaAddr.getLoc());
		baseAddressVo.setCountry(1);
		baseAddressVo.setState(Integer.parseInt(gstNo.substring(0, 2)));
		baseAddressVo.setLandMark(taxillaAddr.getSt());
		baseAddressVo.setZipCode(taxillaAddr.getPncd());
		baseAddressVo.setAddressType(addressType);
		baseAddressVo.setGstNo(gstNo);
		baseAddressVo.setCity(taxillaAddr.getDst() != null ? taxillaAddr.getDst().replaceAll("\\p{P}", ""):null);
		baseAddressVo.setSelectedAddress(selectedAddress);
		
		return baseAddressVo;
	}
	
	private static VendorDestinationAddressVo convertToBillingAddressVo(TaxillaAddr taxillaAddr, String gstNo, int addressType) {
		VendorDestinationAddressVo destinationAddressVo = new VendorDestinationAddressVo();
		destinationAddressVo.setAddress_1(taxillaAddr.getBno() + taxillaAddr.getBnm());
		destinationAddressVo.setAddress_2(taxillaAddr.getSt() + taxillaAddr.getLoc());
		destinationAddressVo.setCountry(1);
		destinationAddressVo.setState(Integer.parseInt(gstNo.substring(0, 2)));
		destinationAddressVo.setLandMark(taxillaAddr.getSt());
		destinationAddressVo.setZipCode(taxillaAddr.getPncd());
		destinationAddressVo.setAddressType(addressType);
		destinationAddressVo.setCity(taxillaAddr.getDst() != null ? taxillaAddr.getDst().replaceAll("\\p{P}", ""):null);
		destinationAddressVo.setGstNo(gstNo);
		destinationAddressVo.setSelectedAddress(3);

		return destinationAddressVo;
	}
	
	private static VendorOriginAddressVo convertToOriginalAddressVo(TaxillaAddr taxillaAddr, String gstNo, int addressType) {
		VendorOriginAddressVo vendorOriginalAddressVo = new VendorOriginAddressVo();
		vendorOriginalAddressVo.setAddress_1(taxillaAddr.getBno() + taxillaAddr.getBnm());
		vendorOriginalAddressVo.setAddress_2(taxillaAddr.getSt() + taxillaAddr.getLoc());
		vendorOriginalAddressVo.setCountry(1);
		vendorOriginalAddressVo.setState(Integer.parseInt(gstNo.substring(0, 2)));
		vendorOriginalAddressVo.setLandMark(taxillaAddr.getSt());
		vendorOriginalAddressVo.setZipCode(taxillaAddr.getPncd());
		vendorOriginalAddressVo.setAddressType(addressType);
		vendorOriginalAddressVo.setGstNo(gstNo);
		vendorOriginalAddressVo.setCity(taxillaAddr.getDst() != null ? taxillaAddr.getDst().replaceAll("\\p{P}", ""):null);
		vendorOriginalAddressVo.setSelectedAddress(3);
		return vendorOriginalAddressVo;
	}
	
	

	// For Deleting Vendor

	@RequestMapping(value = "/v1/vendors/{organizationId}/{organizationName}/{userId}/{vendorId}/{roleName}/{status}", method = RequestMethod.DELETE)
	public ResponseEntity<BaseResponse> deleteVendor(
			HttpServletRequest httpRequest, 
			HttpServletResponse httpResponse,
			@PathVariable int organizationId, 
			@PathVariable String organizationName,
			@PathVariable String userId, 
			@PathVariable int vendorId,
			@PathVariable String roleName,
			@PathVariable String status) {
		logger.info("Entry into method:deleteVendor");
		BaseResponse response = new VendorResponse();
		try {
			VendorVo vendorVo = vendorService.deleteVendor(vendorId,status,userId,roleName);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			vendorVo.setKeyToken(null);
			vendorVo.setValueToken(null);
			((VendorResponse) response).setData(vendorVo);
			if(status.equals(CommonConstants.STATUS_AS_ACTIVE)) {
				response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_VENDOR_ACTIVATED,
						Constants.SUCCESS_VENDOR_ACTIVATED, Constants.VENDOR_DELETED_SUCCESSFULLY);
				logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			}else if(status.equals(CommonConstants.STATUS_AS_INACTIVE)) {
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_VENDOR_DEACTIVATED,
					Constants.SUCCESS_VENDOR_DEACTIVATED, Constants.VENDOR_DELETED_SUCCESSFULLY);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			}
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_VENDOR_DELETED, e.getMessage(),
					Constants.VENDOR_DELETED_UNSUCCESSFULLY);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}	
	
	// For Fetching all Vendor details
	@RequestMapping(value = "/v1/vendors/{organizationId}/{organizationName}/{userId}/{roleName}", method = RequestMethod.GET)
	public ResponseEntity<BaseResponse> getAllVendorsOfAnOrganizationForUserAndRole(
			HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, 
			@PathVariable int organizationId, 
			@PathVariable String organizationName,
			@PathVariable String userId,
			@PathVariable String roleName) {
		logger.info("Entry into method:getAllVendorsOfAnOrganizationForUserAndRole");
		BaseResponse response = new VendorBasicDetailsResponse();
		try {
			List<VendorVo> listAllVendors = vendorService.getAllVendorsOfAnOrganizationForUserAndRole(organizationId,userId,roleName);
			List<VendorBasicDetailsVo> vendorBasicDetailsVo = KeyContactConvertToVoHelper.getInstance()
					.convertVendorListToVendorBasicDetails(listAllVendors);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((VendorBasicDetailsResponse) response).setData(vendorBasicDetailsVo);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_VENDOR_FETCH,
					Constants.SUCCESS_VENDOR_FETCH, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_VENDOR_FETCH, e.getMessage(),
					Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// For Fetching One Vendor detail by vendor id
	@RequestMapping(value = "/v1/vendors/{organizationId}/{organizationName}/{userId}/{roleName}/{id}", method = RequestMethod.GET)
	public ResponseEntity<BaseResponse> getVendorByVendorId(
			HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, 
			@PathVariable int organizationId, 
			@PathVariable String organizationName,
			@PathVariable String userId,
			@PathVariable String roleName,
			@PathVariable int id) {
		logger.info("Entry into method:getVendorByVendorId");
		BaseResponse response = new VendorResponse();
		try {
			VendorVo vendorVo = vendorService.getVendorByVendorId(id);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((VendorResponse) response).setData(vendorVo);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_VENDOR_FETCH,
					Constants.SUCCESS_VENDOR_FETCH, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_VENDOR_FETCH, e.getMessage(),
					Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// For Updating Vendor
	@RequestMapping(value = "/v1/vendors", method = RequestMethod.PUT)
	public ResponseEntity<BaseResponse> updateVendor(HttpServletRequest httpRequest, HttpServletResponse httpResponse,
			@RequestBody JSONObject<VendorRequest> vendorRequest) {
		logger.info("Entry into method:updateVendor");
		BaseResponse response = new VendorResponse();
		try {
			logger.info("Request Payload:" + generateRequestAndResponseLogPaylod(vendorRequest));
			VendorVo vendorVo = KeyContactConvertToVoHelper.getInstance()
					.convertVendorVoFromVendorRequest(vendorRequest.getData());
			vendorService.updateVendor(vendorVo);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_VENDOR_UPDATED,
					Constants.SUCCESS_VENDOR_UPDATED, Constants.VENDOR_UPDATED_SUCCESSFULLY);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			if(e.getCause()!=null) {
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_VENDOR_UPDATED, 
						e.getCause().getMessage(),
						Constants.VENDOR_UPDATED_UNSUCCESSFULLY);
			}else {
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_VENDOR_UPDATED, e.getMessage(),
						Constants.VENDOR_UPDATED_UNSUCCESSFULLY);
			}
			
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/v1/dropsdowns/vendors/{organizationId}")
	public ResponseEntity<BaseResponse> getVendorDropDown(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, @PathVariable int organizationId) {
		logger.info("Entry into getVendorDropDown");
		BaseResponse response = new VendorDropDownResponse();
		try {
			VendorDropDownVo data = vendorService.getVendorDropDownData(organizationId);
			setTokens(response, httpServletRequest.getHeader("keyToken"), httpServletRequest.getHeader("valueToken"));
			((VendorDropDownResponse) response).setData(data);
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
	
	// For Updating Vendor Address
		@RequestMapping(value = "/v1/vendors/address", method = RequestMethod.PUT)
		public ResponseEntity<BaseResponse> updateVendorAddress(HttpServletRequest httpRequest, HttpServletResponse httpResponse,
				@RequestBody JSONObject<VendorAddressBasedOnGstRequest> vendorAddressRequest) {
			logger.info("Entry into method:updateVendorAddress");
			BaseResponse response = new VendorResponse();
			try {
				logger.info("Request Payload:" + generateRequestAndResponseLogPaylod(vendorAddressRequest));
				VendorBasedOnGstVo vendoraddressVo = KeyContactConvertToVoHelper.getInstance()
						.convertVendorAddressBasedOnGstAndVendorId(vendorAddressRequest.getData());
				vendorService.updateVendorAddress(vendoraddressVo, vendorAddressRequest.getData().getOrgId(), vendorAddressRequest.getData().getVendorId(),
						vendorAddressRequest.getData().getUserId(), vendorAddressRequest.getData().getRoleName());
				setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
				response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_VENDOR_UPDATED,
						Constants.SUCCESS_VENDOR_UPDATED, Constants.VENDOR_UPDATED_SUCCESSFULLY);
				logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
				return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
			} catch (ApplicationException e) {
				if(e.getCause()!=null) {
					response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_VENDOR_UPDATED, 
							e.getCause().getMessage(),
							Constants.VENDOR_UPDATED_UNSUCCESSFULLY);
				}else {
					response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_VENDOR_UPDATED, e.getMessage(),
							Constants.VENDOR_UPDATED_UNSUCCESSFULLY);
				}
				
				logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
				return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
	
/*
 * // For Fetching all Vendor details
 * 
 * @RequestMapping(value = "/v1/vendors/{organizationId}/{userId}", method =
 * RequestMethod.GET) public ResponseEntity<BaseResponse>
 * getAllVendorsOfAnOrganization(HttpServletRequest httpRequest,
 * HttpServletResponse httpResponse, @PathVariable int
 * organizationId, @PathVariable int userId) {
 * logger.info("Entry into method:getAllVendorsOfAnOrganization"); BaseResponse
 * response = new VendorBasicDetailsResponse(); try { List<VendorVo>
 * listAllVendors = vendorService.getAllVendorsOfAnOrganization(organizationId);
 * List<VendorBasicDetailsVo>
 * vendorBasicDetailsVo=ConvertToVoHelper.getInstance()
 * .convertVendorListToVendorBasicDetails(listAllVendors); setTokens(response,
 * httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
 * ((VendorBasicDetailsResponse) response).setData(vendorBasicDetailsVo);
 * response = constructResponse(response, Constants.SUCCESS,
 * Constants.SUCCESS_VENDOR_FETCH, Constants.SUCCESS_VENDOR_FETCH,
 * Constants.SUCCESS_DURING_GET); logger.info("Response Payload:" +
 * generateRequestAndResponseLogPaylod(response)); return new
 * ResponseEntity<BaseResponse>(response, HttpStatus.OK); } catch
 * (ApplicationException e) { response = constructResponse(response,
 * Constants.FAILURE, Constants.FAILURE_VENDOR_FETCH, e.getMessage(),
 * Constants.FAILURE_DURING_GET); logger.error("Error Payload:" +
 * generateRequestAndResponseLogPaylod(response)); logger.info(response); return
 * new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
 * } }
 */
}

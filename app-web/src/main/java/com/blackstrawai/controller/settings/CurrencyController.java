package com.blackstrawai.controller.settings;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.blackstrawai.common.BaseController;
import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.common.CommonConstants;
import com.blackstrawai.common.Constants;
import com.blackstrawai.forex.ExchangeRate;
import com.blackstrawai.helper.SettingsConvertToVoHelper;
import com.blackstrawai.onboarding.OrganizationService;
import com.blackstrawai.request.JSONObject;
import com.blackstrawai.request.settings.CurrencyRequest;
import com.blackstrawai.response.settings.CurrencyResponse;
import com.blackstrawai.response.settings.ListCurrencyResponse;
import com.blackstrawai.settings.BaseCurrencyVo;
import com.blackstrawai.settings.CurrencyService;
import com.blackstrawai.settings.CurrencyVo;

@RestController
@CrossOrigin
@RequestMapping("/decifer/sp/currency")
public class CurrencyController extends BaseController {	

	@Autowired
	CurrencyService currencyService;

	@Autowired
	private OrganizationService organizationService;

	private Logger logger = Logger.getLogger(CurrencyController.class);
	//For Creating Currency
	@RequestMapping(value = "/v1/currencies", method = RequestMethod.POST)
	public ResponseEntity<BaseResponse> createCurrency(HttpServletRequest httpRequest, HttpServletResponse httpResponse,@RequestBody JSONObject<CurrencyRequest> currencyRequest) {		
		logger.info("Entry into method:createCurrency");	
		BaseResponse response = new CurrencyResponse();
		try {	
			logger.info("Request Payload:"+generateRequestAndResponseLogPaylod(currencyRequest));				
			CurrencyVo currencyVo=SettingsConvertToVoHelper.getInstance().convertCurrencyVoFromCurrencyRequest(currencyRequest.getData());		
			currencyVo=currencyService.createCurrency(currencyVo);
			setTokens(response,httpRequest.getHeader("keyToken"),httpRequest.getHeader("valueToken"));
			currencyVo.setKeyToken(null);
			currencyVo.setValueToken(null);
			((CurrencyResponse) response).setData(currencyVo);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_CURRENCY_CREATED,Constants.SUCCESS_CURRENCY_CREATED,Constants.CURRENCY_CREATED_SUCCESSFULLY);
			logger.info("Response Payload:"+generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (Exception e) {
			if(e.getCause()!=null){
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_CURRENCY_CREATED,e.getCause().getMessage(), Constants.CURRENCY_CREATED_UNSUCCESSFULLY);
			}else{
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_CURRENCY_CREATED,e.getMessage(), Constants.CURRENCY_CREATED_UNSUCCESSFULLY);
			}
			logger.error("Error Payload:"+generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		} 
	}

	//For Update Currency
	@RequestMapping(value = "/v1/currencies", method = RequestMethod.PUT)
	public ResponseEntity<BaseResponse> updateCurrency(HttpServletRequest httpRequest, HttpServletResponse httpResponse,@RequestBody JSONObject<CurrencyRequest> currencyRequest) {		
		logger.info("Entry into method:updateCurrency");	
		BaseResponse response = new CurrencyResponse();
		try {	
			logger.info("Request Payload:"+generateRequestAndResponseLogPaylod(currencyRequest));				
			CurrencyVo currencyVo=SettingsConvertToVoHelper.getInstance().convertCurrencyVoFromCurrencyRequest(currencyRequest.getData());		
			currencyVo=currencyService.updateCurrency(currencyVo);
			setTokens(response,httpRequest.getHeader("keyToken"),httpRequest.getHeader("valueToken"));
			currencyVo.setKeyToken(null);
			currencyVo.setValueToken(null);
			((CurrencyResponse) response).setData(currencyVo);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_CURRENCY_UPDATED,Constants.SUCCESS_CURRENCY_UPDATED,Constants.CURRENCY_UPDATED_SUCCESSFULLY);
			logger.info("Response Payload:"+generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (Exception e) {
			if(e.getCause()!=null){
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_CURRENCY_UPDATED,e.getCause().getMessage(), Constants.CURRENCY_UPDATED_UNSUCCESSFULLY);
			}else{
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_CURRENCY_UPDATED,e.getMessage(), Constants.CURRENCY_UPDATED_UNSUCCESSFULLY);

			}
			logger.error("Error Payload:"+generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		} 
	}

	//For Deleting Currency
	@RequestMapping(value = "/v1/currencies/{organizationId}/{userId}/{id}/{status}", method = RequestMethod.DELETE)
	public ResponseEntity<BaseResponse> deleteCurrency(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, 
			@PathVariable int organizationId, @PathVariable int userId, @PathVariable int id,@PathVariable String status) {
		logger.info("Entry into method:deleteCurrency");	
		BaseResponse response = new CurrencyResponse();
		try {	
			CurrencyVo currencyVo=currencyService.deleteCurrency(id,status);
			setTokens(response,httpRequest.getHeader("keyToken"),httpRequest.getHeader("valueToken"));
			currencyVo.setKeyToken(null);
			currencyVo.setValueToken(null);
			((CurrencyResponse) response).setData(currencyVo);
			if(status.equals(CommonConstants.STATUS_AS_ACTIVE)) {
				response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_CURRENCY_ACTIVATED,
						Constants.SUCCESS_CURRENCY_ACTIVATED, Constants.CURRENCY_DELETED_SUCCESSFULLY);				
			}else if(status.equals(CommonConstants.STATUS_AS_INACTIVE)) {
				response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_CURRENCY_DEACTIVATED,
						Constants.SUCCESS_CURRENCY_DEACTIVATED, Constants.CURRENCY_DELETED_SUCCESSFULLY);
			}
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (Exception e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_CURRENCY_DELETED,e.getMessage(), Constants.CURRENCY_DELETED_UNSUCCESSFULLY);
			logger.error("Error Payload:"+generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		} 
	}


	//For Fetching all Currencies belonging to an Organization
	@RequestMapping(value = "/v1/currencies/organizations/{organizationId}/{organizationName}")
	public ResponseEntity<BaseResponse> getAllCurrenciesOfAnOrganization(HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse,@PathVariable int organizationId,@PathVariable String organizationName) {
		logger.info("Entry into method:getAllCurrenciesOfAnOrganization");
		BaseResponse response = new ListCurrencyResponse();
		try {
			List<CurrencyVo>  listAllCurrencies = currencyService.getAllCurrenciesOfAnOrganization(organizationId,organizationName );
			//For setting up Direct and Indirect Exchange Rate  
			BaseCurrencyVo baseCurrencyVo = currencyService
					.getBaseCurrencyById(organizationService.getOrganizationBaseCurrency(organizationId));

			String data=ExchangeRate.getInstance().getForex(baseCurrencyVo.getAlternateSymbol());
			JSONParser parser=new JSONParser();
			org.json.simple.JSONObject json = (org.json.simple.JSONObject) parser.parse(data);
			if(null!=json.get("result") && "error".equals(json.get("result"))){
				setTokens(response,httpServletRequest.getHeader("keyToken"),httpServletRequest.getHeader("valueToken"));
				((ListCurrencyResponse) response).setData(null);
				response = constructResponse(response, Constants.FAILURE, Constants.INVALID_BASE_CURRENCY_FOR_ORGANIZATION_ID,
						Constants.INVALID_BASE_CURRENCY_FOR_ORGANIZATION_ID, Constants.FAILURE_DURING_GET);
			}else{

				org.json.simple.JSONObject rateJson = (org.json.simple.JSONObject) parser.parse(json.get("rates").toString());
				for(CurrencyVo vo :listAllCurrencies){

					String indirectRateStr=rateJson.get(vo.getAlternateSymbol()).toString();

					Double inDirectRate=0.0;
					if(null!=indirectRateStr) {
						inDirectRate=Double.parseDouble(indirectRateStr);
					}
					//Calculation for direct rate
					Double directRate=1/inDirectRate;
					//limiting to decimals
					if( baseCurrencyVo.getNumberOfDecimalPlaces()>0) {

						directRate = BigDecimal.valueOf(directRate).setScale(baseCurrencyVo.getNumberOfDecimalPlaces(), RoundingMode.HALF_UP).doubleValue();
					}
					vo.setDirectRate(" 1 "+vo.getAlternateSymbol()+" = "+directRate+" "+String.valueOf(baseCurrencyVo.getAlternateSymbol()));
					vo.setIndirectRate(indirectRateStr);

				}

			}
			setTokens(response,httpServletRequest.getHeader("keyToken"),httpServletRequest.getHeader("valueToken"));
			((ListCurrencyResponse) response).setData(listAllCurrencies);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_CURRENCY_FETCH,
					Constants.SUCCESS_CURRENCY_FETCH, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (Exception e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_CURRENCY_FETCH, e.getMessage(),
					Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}


	//For Fetching a particular Currency
	@RequestMapping(value = "/v1/currencies/{organizationId}/{userId}/{id}")
	public ResponseEntity<BaseResponse> getCurrencyDetails(
			HttpServletRequest httpRequest,
			HttpServletResponse httpResponse,@PathVariable int organizationId, @PathVariable int userId,
			@PathVariable int id) {
		logger.info("Entry into method:getCurrencyDetails");
		BaseResponse response = new CurrencyResponse();
		try {
			CurrencyVo data = currencyService.getCurrency(id);
			setTokens(response,httpRequest.getHeader("keyToken"),httpRequest.getHeader("valueToken"));
			((CurrencyResponse) response).setData(data);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_CURRENCY_FETCH,
					Constants.SUCCESS_CURRENCY_FETCH, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (Exception e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_CURRENCY_FETCH, e.getMessage(),
					Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}


	}



}

package com.blackstrawai.controller.externalintegration.general;

import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blackstrawai.common.BaseController;
import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.common.Constants;
import com.blackstrawai.externalintegration.general.exchangerate.ExchangeRateVo;
import com.blackstrawai.forex.ExchangeRate;
import com.blackstrawai.onboarding.OrganizationService;
import com.blackstrawai.response.externalintegration.general.ExchangeRateResponse;
import com.blackstrawai.settings.BaseCurrencyVo;
import com.blackstrawai.settings.CurrencyService;
import com.blackstrawai.settings.CurrencyVo;

@RestController
@CrossOrigin
@RequestMapping("/decifer/exchange_rate")
public class ExchangeRateController extends BaseController{

	private Logger logger = Logger.getLogger(ExchangeRateController.class);
	@Autowired
	private OrganizationService organizationService;

	@Autowired
	CurrencyService currencyService;

	
	@RequestMapping(value = "/v1/exchange_rates/{organizationId}/{currencyId}")
	public ResponseEntity<BaseResponse> getExchangeRate(
			HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse,@PathVariable int organizationId,@PathVariable int currencyId) {
		logger.info("Entry into method: getExchangeRate");
		BaseResponse response = new ExchangeRateResponse();
		try {
		
			BaseCurrencyVo baseCurrencyVo = currencyService
					.getBaseCurrencyById(organizationService.getOrganizationBaseCurrency(organizationId));
			
			String data=ExchangeRate.getInstance().getForex(baseCurrencyVo.getAlternateSymbol());
			JSONParser parser=new JSONParser();
			JSONObject json = (JSONObject) parser.parse(data);
			if(null!=json.get("result") && "error".equals(json.get("result"))){
				setTokens(response,httpServletRequest.getHeader("keyToken"),httpServletRequest.getHeader("valueToken"));
				((ExchangeRateResponse) response).setData(null);
				response = constructResponse(response, Constants.FAILURE, Constants.INVALID_BASE_CURRENCY_FOR_ORGANIZATION_ID,
						Constants.INVALID_BASE_CURRENCY_FOR_ORGANIZATION_ID, Constants.FAILURE_DURING_GET);
			}else{
				
				JSONObject rateJson = (JSONObject) parser.parse(json.get("rates").toString());
				
				CurrencyVo currencyVo=currencyService.getCurrency(currencyId);
				String indirectRateStr=rateJson.get(currencyVo.getAlternateSymbol()).toString();
				
				Double inDirectRate=0.0;
				if(null!=indirectRateStr) {
					inDirectRate=Double.parseDouble(indirectRateStr);
				}
				//Calculation for direct rate
				Double directRate=1/inDirectRate;
				if( baseCurrencyVo.getNumberOfDecimalPlaces()>0) {
					
					directRate = BigDecimal.valueOf(directRate).setScale(baseCurrencyVo.getNumberOfDecimalPlaces(), RoundingMode.HALF_UP).doubleValue();
				}
				
				ExchangeRateVo exchangeRateVo=new ExchangeRateVo();
				exchangeRateVo.setDirectRate(String.valueOf(directRate));
				exchangeRateVo.setIndirectRate(indirectRateStr);
				setTokens(response,httpServletRequest.getHeader("keyToken"),httpServletRequest.getHeader("valueToken"));
				((ExchangeRateResponse) response).setData(exchangeRateVo);
				response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_EXCHANGE_RATE_DETAILS_FETCH,
						Constants.SUCCESS_EXCHANGE_RATE_DETAILS_FETCH, Constants.SUCCESS_DURING_GET);
			}
			
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (Exception e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_EXCHANGE_RATE_FETCH, e.getMessage(),
					Constants.FAILURE_DURING_GET);
		logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
}

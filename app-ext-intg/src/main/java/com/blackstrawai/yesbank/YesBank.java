package com.blackstrawai.yesbank;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.ApplicationRuntimeException;
import com.blackstrawai.externalintegration.yesbank.Response.YesBankAuthorizationVo;
import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.FileReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class YesBank {

	private static YesBank yesBank;
	private static String yesBankClientId = null;
	private static String yesBankClientSecret = null;
	private static String yesBankBulkClientId = null;
	private static String yesBankBulkClientSecret = null;
	private static Map<String, String> urlMapping;
	private static Map<String, String> urlBulkMapping;

	private final Logger logger = Logger.getLogger(YesBank.class);

	private YesBank() {

	}

	static{
		configureSinglePaymentSettings();
		configureBulkPaymentSettings();
	}

	private static void configureSinglePaymentSettings(){

		FileReader reader;
		try {

			reader = new FileReader(YesBankConstants.DECIFER_CONFIG_PROPERTTY_URL);
			Properties p = new Properties();
			p.load(reader);

			yesBankClientId = p.getProperty(YesBankConstants.CLIENT_ID_PROPERTY_KEY);
			yesBankClientSecret = p.getProperty(YesBankConstants.CLIENT_SECRET_PROPERTY_KEY);

			urlMapping = new HashMap<>();
			urlMapping.put(YesBankConstants.DOMESTIC_PAYMENTS,p.getProperty(YesBankConstants.DOMESTIC_PAYMENTS_URL_PROPERTY_KEY));
			urlMapping.put(YesBankConstants.FUNDS_CONFIRMATION,p.getProperty(YesBankConstants.FUNDS_CONFIRMATION_URL_PROPERTY_KEY));
			urlMapping.put(YesBankConstants.BANK_STATEMENT,p.getProperty(YesBankConstants.YES_BANK_STATEMENT_URL_PROPERTY_KEY));
			urlMapping.put(YesBankConstants.BULK_PAYMENTS,p.getProperty(YesBankConstants.YES_BULK_PAYMENTS_URL_PROPERTY_KEY));

		} catch (Exception e) {
			throw new ApplicationRuntimeException(e);
		}
	}



	private static void configureBulkPaymentSettings(){

		FileReader reader;
		try {

			reader = new FileReader(YesBankConstants.DECIFER_CONFIG_PROPERTTY_URL);
			Properties p = new Properties();
			p.load(reader);

			yesBankBulkClientId = p.getProperty(YesBankConstants.BULK_PAYMENT_CLIENT_ID_PROPERTY_KEY);
			yesBankBulkClientSecret = p.getProperty(YesBankConstants.BULK_PAYMENT_CLIENT_SECRET_PROPERTY_KEY);

			urlBulkMapping = new HashMap<>();
			urlBulkMapping.put(YesBankConstants.BULK_PAYMENTS,p.getProperty(YesBankConstants.YES_BULK_PAYMENTS_URL_PROPERTY_KEY));
			urlBulkMapping.put(YesBankConstants.BULK_PAYMENTS_DETAIL,p.getProperty(YesBankConstants.YES_BULK_PAYMENTS_DETAIL_URL_PROPERTY_KEY));
		} catch (Exception e) {
			throw new ApplicationRuntimeException(e);
		}
	}

	public static YesBank getInstance() {

		if (yesBank == null) {
			yesBank = new YesBank();
		}
		return yesBank;
	}

	public String getSingleResponseEntity(String requestUrl, String jsonRequestBody, YesBankAuthorizationVo authorization)
			throws ApplicationException {

		try {

			logger.info("Requested URL>>>>" + urlMapping.get(requestUrl));

			WebTarget webTarget =SSLHandler.getInstance().sslConnect().target(urlMapping.get(requestUrl));

			Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON_TYPE);
			invocationBuilder.header(YesBankConstants.CLIENT_ID, yesBankClientId);
			invocationBuilder.header(YesBankConstants.CLIENT_SECRET, yesBankClientSecret);

			String auth = authorization.getUserName() + ":" +authorization.getAuthKey();
			byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(StandardCharsets.ISO_8859_1));
			String authHeader = "Basic " + new String(encodedAuth);
			logger.info("authHeader.." + authHeader);

			invocationBuilder.header(javax.ws.rs.core.HttpHeaders.AUTHORIZATION, authHeader);

			Response response =invocationBuilder.post(Entity.entity(jsonRequestBody, MediaType.APPLICATION_JSON));

			String responseEntity = response.readEntity(String.class);
			logger.info(response.getStatus() + "<<<payments response entity>>>>>" + responseEntity);

			if(responseEntity.contains("<h1>401 Authorization Required")){
				throw new ApplicationException(YesBankConstants.ERROR_YBL_NOT_VALID_REQUEST);
			}
			return responseEntity;
		} catch (Exception ex) {
			throw ex;
		}
	}


	public String getBulkResponseEntity(String requestUrl, String jsonRequestBody, YesBankAuthorizationVo authorization)
			throws ApplicationException {

		try {

			logger.info("Requested URL>>>>" + urlBulkMapping.get(requestUrl));

			WebTarget webTarget =SSLHandler.getInstance().sslConnect().target(urlBulkMapping.get(requestUrl));

			Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON_TYPE);
			invocationBuilder.header(YesBankConstants.CLIENT_ID, yesBankBulkClientId);
			invocationBuilder.header(YesBankConstants.CLIENT_SECRET, yesBankBulkClientSecret);

			logger.info("Client Id is:"+yesBankBulkClientId +"-Client Secret is:"+yesBankBulkClientSecret);

			String auth = authorization.getUserName() + ":" +authorization.getAuthKey();
			byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(StandardCharsets.ISO_8859_1));
			String authHeader = "Basic " + new String(encodedAuth);
			logger.info("authHeader.." + authHeader);

			invocationBuilder.header(javax.ws.rs.core.HttpHeaders.AUTHORIZATION, authHeader);

			Response response =invocationBuilder.post(Entity.entity(jsonRequestBody, MediaType.APPLICATION_JSON));

			String responseEntity = response.readEntity(String.class);
			logger.info(response.getStatus() + "<<<payments response entity>>>>>" + responseEntity);

			if(responseEntity.contains("<h1>401 Authorization Required")){
				throw new ApplicationException(YesBankConstants.ERROR_YBL_NOT_VALID_REQUEST);
			}
			return responseEntity;
		} catch (Exception ex) {
			throw ex;
		}
	}
}

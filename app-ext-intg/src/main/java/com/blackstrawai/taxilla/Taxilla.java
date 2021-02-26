package com.blackstrawai.taxilla;

import java.io.FileReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.ApplicationRuntimeException;
import com.blackstrawai.externalintegration.compliance.taxilla.TaxillaGsonVo;
import com.blackstrawai.externalintegration.compliance.taxilla.TaxillaGstResponseVo;
import com.google.gson.Gson;

public class Taxilla {

	private static Taxilla taxilla;

	private Logger logger = Logger.getLogger(Taxilla.class);

	private static Long startTime;
	private static Long expiresIn;
	private static String token;
	private static Map<String, String> taxillaMap = new HashMap<String, String>();
	
	private static final Gson gsonConverter = new Gson();
	
	public static Map<String, String> getTaxillaMap() {
		return taxillaMap;
	}

	private Taxilla() {

	}

	public static Taxilla getInstance() {

		if (taxilla == null) {
			taxilla = new Taxilla();
			FileReader reader;
			try {
				reader = new FileReader("/decifer/config/app_config.properties");
				Properties p = new Properties();
				p.load(reader);

				String baseUrl = p.getProperty(TaxillaConstants.GSTR_BASE_URL_KEY);
				taxillaMap.put(TaxillaConstants.GSTIN_URL_KEY, p.getProperty(TaxillaConstants.GSTIN_URL_KEY));
				taxillaMap.put(TaxillaConstants.AUTH_URL_KEY, p.getProperty(TaxillaConstants.AUTH_URL_KEY));
				taxillaMap.put(TaxillaConstants.GSP_APP_ID_KEY, p.getProperty(TaxillaConstants.GSP_APP_ID_KEY));
				taxillaMap.put(TaxillaConstants.GSP_APP_SECRET_KEY, p.getProperty(TaxillaConstants.GSP_APP_SECRET_KEY));
				taxillaMap.put(TaxillaConstants.GSTR1_KEY, baseUrl + "/" + TaxillaConstants.GSTR1);
				taxillaMap.put(TaxillaConstants.GSTR2A_KEY, baseUrl + "/" + TaxillaConstants.GSTR2A);
				taxillaMap.put(TaxillaConstants.GSTR2B_KEY, baseUrl + "/" + TaxillaConstants.GSTR2B);
				taxillaMap.put(TaxillaConstants.GSTR3B_KEY, baseUrl + "/" + TaxillaConstants.GSTR3B);
				taxillaMap.put(TaxillaConstants.GSTR4_KEY, baseUrl + "/" + TaxillaConstants.GSTR4);
				taxillaMap.put(TaxillaConstants.GSTR9_KEY, baseUrl + "/" + TaxillaConstants.GSTR9);
				taxillaMap.put(TaxillaConstants.GSTR9A_KEY, baseUrl + "/" + TaxillaConstants.GSTR9A);
				taxillaMap.put(TaxillaConstants.GSTR9C_KEY, baseUrl + "/" + TaxillaConstants.GSTR9C);
				taxillaMap.put(TaxillaConstants.ITC03_KEY, baseUrl + "/" + TaxillaConstants.ITC03);
				taxillaMap.put(TaxillaConstants.ITC04_KEY, baseUrl + "/" + TaxillaConstants.ITC04);
			} catch (Exception e) {
				throw new ApplicationRuntimeException(e);
			}
		}

		return taxilla;
	}

	public JSONObject getByGstNo(String gstNo) throws ApplicationException {
//		TaxillaVo taxillaVo = new TaxillaVo();
		try {
			String token = getAccessToken(System.currentTimeMillis());

			if (gstNo != null) {
				gstNo = gstNo.trim();
			}

			WebTarget gstTarget = ClientBuilder.newClient().target(taxillaMap.get(TaxillaConstants.GSTIN_URL_KEY)).queryParam("action", "{action}")
					.queryParam("gstin", "{gstin}").resolveTemplate("action", "TP").resolveTemplate("gstin", gstNo);

			Invocation.Builder gstInvocationBuilder = gstTarget.request(MediaType.APPLICATION_JSON_TYPE);
			gstInvocationBuilder.header("Content-Type", "application/json");
			gstInvocationBuilder.header(HttpHeaders.AUTHORIZATION, "Bearer " + token);
			Response response = gstInvocationBuilder.get();
			String gstString = response.readEntity(String.class);
			JSONParser parser = new JSONParser();
			JSONObject gstJson = (JSONObject) parser.parse(gstString);
			return gstJson;
		} catch (Exception e) {
			throw new ApplicationException(e);
		}

	}
	
	
	public TaxillaGsonVo getByGst(String gstNo) throws ApplicationException {
		try {
			String token = getAccessToken(System.currentTimeMillis());

			if (gstNo != null) {
				gstNo = gstNo.trim();
			}

			WebTarget gstTarget = ClientBuilder.newClient().target(taxillaMap.get(TaxillaConstants.GSTIN_URL_KEY))
					.queryParam("action", "{action}").queryParam("gstin", "{gstin}").resolveTemplate("action", "TP")
					.resolveTemplate("gstin", gstNo);

			Invocation.Builder gstInvocationBuilder = gstTarget.request(MediaType.APPLICATION_JSON_TYPE);
			gstInvocationBuilder.header("Content-Type", "application/json");
			gstInvocationBuilder.header(HttpHeaders.AUTHORIZATION, "Bearer " + token);
			Response response = gstInvocationBuilder.get();
			String gstString = response.readEntity(String.class);
			TaxillaGstResponseVo taxillaGstResponseVo = gsonConverter.fromJson(gstString, TaxillaGstResponseVo.class);
			return taxillaGstResponseVo.getResult();
		} catch (Exception e) {
			throw new ApplicationException(e);
		}

	}

	public Map<String, String> getGSTStatus(List<String> gstList, String gstinUrl) throws ApplicationException {
		try {
			String token = getAccessToken(System.currentTimeMillis());

			Map<String, String> statusMap = new HashMap<String, String>();
			for (String gstNo : gstList) {
				WebTarget gstTarget = ClientBuilder.newClient().target(gstinUrl).queryParam("action", "{action}")
						.queryParam("gstin", "{gstin}").resolveTemplate("action", "TP").resolveTemplate("gstin", gstNo);

				if (token != null) {
					Invocation.Builder gstInvocationBuilder = gstTarget.request(MediaType.APPLICATION_JSON_TYPE);
					gstInvocationBuilder.header("Content-Type", "application/json");
					gstInvocationBuilder.header(HttpHeaders.AUTHORIZATION, "Bearer " + token);
					Response response = gstInvocationBuilder.get();
					String gstString = response.readEntity(String.class);
					JSONParser parser = new JSONParser();
					JSONObject gstJson = (JSONObject) parser.parse(gstString);
					if (gstJson.get("result") != null) {
						gstJson = (JSONObject) gstJson.get("result");
						String status = gstJson.get("sts").toString();
						if (status.equalsIgnoreCase("cancelled")) {
							logger.info("Cancelled status:" + gstNo);
							statusMap.put(gstNo, status);
						} else {
							statusMap.put(gstNo, status);
						}

					}
				}

			}

			return statusMap;
		} catch (Exception e) {
			throw new ApplicationException(e);
		}

	}

	public String getAccessToken(Long currentTime) throws ApplicationException {
		try {
			if (token != null && startTime != null && expiresIn != null && currentTime - startTime < expiresIn) {
				return token;
			}
			WebTarget webTarget = ClientBuilder.newClient().target(taxillaMap.get(TaxillaConstants.AUTH_URL_KEY)).queryParam("grant_type", "{grantType}")
					.resolveTemplate("grantType", "token");
			Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON_TYPE);
			invocationBuilder.header("gspappid", taxillaMap.get(TaxillaConstants.GSP_APP_ID_KEY));
			invocationBuilder.header("gspappsecret", taxillaMap.get(TaxillaConstants.GSP_APP_SECRET_KEY));
			Response response = invocationBuilder.post(null);
			String authString = response.readEntity(String.class);
			JSONParser parser = new JSONParser();
			JSONObject authJson;
			authJson = (JSONObject) parser.parse(authString);
			token = authJson.get("access_token") != null ? authJson.get("access_token").toString() : null;
			startTime = System.currentTimeMillis();
			expiresIn = (Long) authJson.get("expires_in");
		} catch (Exception e) {
			throw new ApplicationException(e);
		}
		return token;
	}
	
	public JSONObject getGspResponse(String gstin, String username, String requestId, String retPeriod,
			String targetUrl, String action, String otp) throws ApplicationException {

		try {
			String token = getAccessToken(System.currentTimeMillis());

			if (gstin != null && gstin.length() >= 2) {
				gstin = gstin.trim();
			} else {
				throw new ApplicationException("GSTIN is null/empty");
			}

			WebTarget gstTarget = ClientBuilder.newClient().target(targetUrl)
					.queryParam(TaxillaConstants.ACTION, TaxillaConstants.ACTION_PARAMETER)
					.queryParam(TaxillaConstants.RET_PERIOD, TaxillaConstants.RET_PERIOD_PARAMETER)
					.queryParam(TaxillaConstants.GSTIN, TaxillaConstants.GSTIN_PARAMETER)
					.resolveTemplate(TaxillaConstants.ACTION, action).resolveTemplate(TaxillaConstants.GSTIN, gstin)
					.resolveTemplate(TaxillaConstants.RET_PERIOD, retPeriod);

			Invocation.Builder gstInvocationBuilder = gstTarget.request(MediaType.APPLICATION_JSON_TYPE);
			gstInvocationBuilder.header(TaxillaConstants.CONTENT_TYPE, TaxillaConstants.CONTENT_TYPE_APPLICATION_JSON);
			gstInvocationBuilder.header(TaxillaConstants.USERNAME, username);
			gstInvocationBuilder.header(TaxillaConstants.STATE_CD, gstin.substring(0, 2));
			gstInvocationBuilder.header(TaxillaConstants.GSTIN, gstin);
			gstInvocationBuilder.header(TaxillaConstants.RET_PERIOD, retPeriod);
			gstInvocationBuilder.header(TaxillaConstants.REQUEST_ID, requestId);
			gstInvocationBuilder.header(TaxillaConstants.OTP, otp);

			gstInvocationBuilder.header(HttpHeaders.AUTHORIZATION, TaxillaConstants.BEARER + " " + token);
			Response response = gstInvocationBuilder.get();
			String gstString = response.readEntity(String.class);
			JSONParser parser = new JSONParser();
			JSONObject gstJson = (JSONObject) parser.parse(gstString);
			return gstJson;
		} catch (Exception e) {
			throw new ApplicationException(e);
		}

	}

}

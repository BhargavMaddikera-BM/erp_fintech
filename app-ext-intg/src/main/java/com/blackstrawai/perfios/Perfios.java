package com.blackstrawai.perfios;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.ApplicationRuntimeException;
import com.blackstrawai.externalintegration.banking.perfios.PerfiosAutoWidgetUrlVo;
import com.blackstrawai.externalintegration.banking.perfios.PerfiosMetadataVo;
import com.blackstrawai.externalintegration.banking.perfios.PerfiosStatementInstitutionVo;
import com.blackstrawai.externalintegration.banking.perfios.PerfiosUserAccountTransactionVo;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Perfios {
	private static Perfios perfios;

	private Logger logger = Logger.getLogger(Perfios.class);

	private static String pertnerLoginUrl = null;
	private static String partnerLoginPartner = null;
	private static String partnerLoginKey = null;
	private static String userAuthUrl = null;
	private static String userRegistrationUrl = null;
	private static String autoUpdateWidgetUrl = null;
	private static String userTransactionUrl = null;
	private static String userUnRegisterUrl = null;
	private static String siteMetadataUrl = null;
	private static String statementInstitutionUrl = null;

	private Perfios() {

	}

	public static Perfios getInstance() {

		if (perfios == null) {
			perfios = new Perfios();
			FileReader reader;
			try {
				reader = new FileReader("/decifer/config/app_config.properties");
				Properties p = new Properties();
				p.load(reader);
				pertnerLoginUrl = p.getProperty("perfios_partnerLogin_url");
				userAuthUrl = p.getProperty("perfios_userLogin_url");
				partnerLoginPartner = p.getProperty("perfios_partnerLogin_partner");
				partnerLoginKey = p.getProperty("perfios_partnerLogin_key");
				userRegistrationUrl = p.getProperty("perfios_user_registration");
				autoUpdateWidgetUrl = p.getProperty("perfios_auto_update_widget_url");
				userTransactionUrl = p.getProperty("perfios_user_transaction_url");
				userUnRegisterUrl = p.getProperty("perfios_user_unregister_url");
				siteMetadataUrl = p.getProperty("perfios_site_metadata_url");
				statementInstitutionUrl = p.getProperty("perfios_statement_institution_url");
			} catch (Exception e) {
				throw new ApplicationRuntimeException(e);
			}
		}

		return perfios;
	}

	public String partnerLogin() throws ApplicationException {
		try {
			String authToken = null;
			WebTarget partnerLoginTarget = ClientBuilder.newClient().target(pertnerLoginUrl)
					.queryParam("partner", "{partner}").queryParam("key", "{key}")
					.resolveTemplate("partner", partnerLoginPartner).resolveTemplate("key", partnerLoginKey);

			Invocation.Builder gstInvocationBuilder = partnerLoginTarget.request(MediaType.APPLICATION_JSON_TYPE);

			Response response = gstInvocationBuilder.post(null);
			String partnerLoginString = response.readEntity(String.class);
			JSONParser parser = new JSONParser();
			JSONObject partnerLoginJson = (JSONObject) parser.parse(partnerLoginString);

			if (partnerLoginJson.get("tokens") != null && partnerLoginJson.get("status") != null) {
				JSONObject status = (JSONObject) partnerLoginJson.get("status");
				if (status.get("code").toString().equals("200")) {
					JSONObject tokenJson = (JSONObject) partnerLoginJson.get("tokens");
					authToken = tokenJson.get("auth") != null ? (String) tokenJson.get("auth") : null;
				}

			}
			logger.info("Partner Login Url:"+pertnerLoginUrl);
			logger.info("partner:"+partnerLoginPartner);
			logger.info("key:"+partnerLoginKey);
			logger.info("authToken:"+authToken);
			return authToken;
		} catch (Exception e) {
			throw new ApplicationException(e);
		}
	}

	public String userLogin(String partnerLoginAuth, String id) throws ApplicationException {
		try {
			String authToken = null;
			WebTarget userAuthTarget = ClientBuilder.newClient().target(userAuthUrl).queryParam("auth", "{auth}")
					.queryParam("user", "{user}").resolveTemplate("auth", partnerLoginAuth).resolveTemplate("user", id);

			Invocation.Builder gstInvocationBuilder = userAuthTarget.request(MediaType.APPLICATION_JSON_TYPE);

			Response response = gstInvocationBuilder.post(null);
			String userAuthString = response.readEntity(String.class);
			JSONParser parser = new JSONParser();
			JSONObject userAuthJson = (JSONObject) parser.parse(userAuthString);

			if (userAuthJson.get("tokens") != null && userAuthJson.get("status") != null) {
				JSONObject status = (JSONObject) userAuthJson.get("status");
				if (status.get("code").toString().equals("200")) {
					JSONObject tokenJson = (JSONObject) userAuthJson.get("tokens");
					authToken = tokenJson.get("user_auth") != null ? (String) tokenJson.get("user_auth") : null;
				}

			}
			logger.info("userAuthUrl:"+userAuthUrl);
			logger.info("user:"+partnerLoginAuth);
			logger.info("id:"+id);
			logger.info("user-authToken:"+authToken);
			
			return authToken;
		} catch (Exception e) {
			throw new ApplicationException(e);
		}

	}

	public void userRegistration(String partnerLoginAuth, String id) throws ApplicationException {
		try {

			WebTarget userAuthTarget = ClientBuilder.newClient().target(userRegistrationUrl)
					.queryParam("auth", "{auth}").queryParam("uniqueUserId", "{uniqueUserId}")
					.resolveTemplate("auth", partnerLoginAuth).resolveTemplate("uniqueUserId", id);

			Invocation.Builder gstInvocationBuilder = userAuthTarget.request(MediaType.APPLICATION_JSON_TYPE);

			Response response = gstInvocationBuilder.post(null);
			String userAuthString = response.readEntity(String.class);
			JSONParser parser = new JSONParser();
			JSONObject userAuthJson = (JSONObject) parser.parse(userAuthString);

			if (userAuthJson.get("status") != null) {
				JSONObject status = (JSONObject) userAuthJson.get("status");
				if (status.get("code").toString().equals("200")) {
					logger.info("User registered successfully");
				}

			}

		} catch (Exception e) {
			throw new ApplicationException(e);
		}

	}

	public String getAutoUpdateWidgetUrl(String partnerLoginAuth, String userAuth,int instId) throws ApplicationException {
		String widgetUrl = null;
		try {

			WebTarget widgetUrlTarget = ClientBuilder.newClient().target(autoUpdateWidgetUrl)
					.queryParam("auth", "{auth}").queryParam("user_auth", "{user_auth}").queryParam("preload", "{preload}").queryParam("instLinkCode", "{instLinkCode}")
					.resolveTemplate("auth", partnerLoginAuth).resolveTemplate("user_auth", userAuth).resolveTemplate("preload", true).resolveTemplate("instLinkCode", instId);

			Invocation.Builder gstInvocationBuilder = widgetUrlTarget.request(MediaType.APPLICATION_JSON_TYPE);
			gstInvocationBuilder.accept(MediaType.APPLICATION_JSON_TYPE);
			gstInvocationBuilder.header("Content-Type", " application/x-www-form-urlencoded");
			PerfiosAutoWidgetUrlVo data=new PerfiosAutoWidgetUrlVo();
			data.setPreload(true);
			data.setInstLinkCode(instId);	

			ObjectMapper objectMapper = new ObjectMapper();			
			String finalJsonRequest = objectMapper.writeValueAsString(data);
			Response response = gstInvocationBuilder.post(Entity.entity(finalJsonRequest, MediaType.APPLICATION_JSON_TYPE));
			String userAuthString = response.readEntity(String.class);
			JSONParser parser = new JSONParser();
			JSONObject widgetUrlJson = (JSONObject) parser.parse(userAuthString);

			if (widgetUrlJson.get("status") != null && widgetUrlJson.get("extData") != null) {
				JSONObject status = (JSONObject) widgetUrlJson.get("status");
				if (status.get("code").toString().equals("200")) {
					logger.info("Auto update widget url fetched successfully");
					JSONObject urlJson = (JSONObject) widgetUrlJson.get("extData");
					widgetUrl = urlJson.get("url") != null ? (String) urlJson.get("url") : null;
				}

			}

		} catch (Exception e) {
			throw new ApplicationException(e);
		}
		return widgetUrl;
	}

	public List<PerfiosUserAccountTransactionVo> getUserAccountTransaction(String accountId, String auth,
			String userAuth) throws ApplicationException {
		List<PerfiosUserAccountTransactionVo> userTransactionVoList = new ArrayList<PerfiosUserAccountTransactionVo>();
		try {
			WebTarget userTransactionTarget = ClientBuilder.newClient().target(userTransactionUrl + "/" + accountId)
					.queryParam("auth", "{auth}").queryParam("user_auth", "{user_auth}").resolveTemplate("auth", auth)
					.resolveTemplate("user_auth", userAuth);

			Invocation.Builder gstInvocationBuilder = userTransactionTarget.request(MediaType.APPLICATION_JSON_TYPE);


			Response response = gstInvocationBuilder.post(null);
			String responseString = response.readEntity(String.class);

			JSONParser parser = new JSONParser();
			JSONObject responseJson = (JSONObject) parser.parse(responseString);
			if (responseJson.get("status") != null && responseJson.get("data") != null) {
				JSONObject status = (JSONObject) responseJson.get("status");
				if (status.get("code").toString().equals("200")) {
					logger.info("User Account Transactions fetched successfully");
					JSONObject accountJson = (JSONObject) responseJson.get("account");
					JSONArray dataArray = (JSONArray) responseJson.get("data");
					List<PerfiosStatementInstitutionVo> financialInstitutionList=getStatementInstitution(auth,userAuth);
					for (Object dataObj : dataArray) {
						JSONObject data = (JSONObject) dataObj;
						PerfiosUserAccountTransactionVo uat = new PerfiosUserAccountTransactionVo();
						uat.setTxnSeqId(data.get("txn_seq_id") != null ? data.get("txn_seq_id").toString() : null);
						uat.setXnDate(data.get("xn_date") != null ? data.get("xn_date").toString() : null);
						uat.setXnDetails(data.get("xn_details") != null ? data.get("xn_details").toString() : null);
						uat.setChequeNum(data.get("cheque_num") != null ? data.get("cheque_num").toString() : null);
						uat.setXnAmount(data.get("xn_amount") != null ? data.get("xn_amount").toString() : null);
						uat.setBalance(data.get("balance") != null ? data.get("balance").toString() : null);
						uat.setCategoryId(data.get("category_id") != null ? data.get("category_id").toString() : null);
						uat.setUserComment(
								data.get("user_comment") != null ? data.get("user_comment").toString() : null);
						uat.setSplitRefId(
								data.get("split_ref_id") != null ? data.get("split_ref_id").toString() : null);
						uat.setXnId(data.get("xn_id") != null ? data.get("xn_id").toString() : null);
						uat.setCategory(data.get("category") != null ? data.get("category").toString() : null);
						uat.setiType(accountJson.get("iType") != null ? accountJson.get("iType").toString() : null);
						uat.setAccountName(accountJson.get("name") != null ? accountJson.get("name").toString() : null);
						uat.setCurrency(accountJson.get("currencyCode") != null ? accountJson.get("currencyCode").toString() : null);
						if (accountJson.get("instId") != null) {
							uat.setInstId((Long) accountJson.get("instId"));
						}
						for(int i=0;i<financialInstitutionList.size();i++){
							PerfiosStatementInstitutionVo perfiosStatementInstitutionVo=financialInstitutionList.get(i);
							if(perfiosStatementInstitutionVo.getId()==uat.getInstId()){
								uat.setInstName(perfiosStatementInstitutionVo.getName());
							}
						}					
						uat.setAccountId(accountId);
						userTransactionVoList.add(uat);
					}
				}

			}
		} catch (Exception e) {
			throw new ApplicationException(e);
		}
		return userTransactionVoList;
	}

	public void unRegisterUser(String uniqueUserId, String partnerAuth, String userAuth) throws ApplicationException {
		try {

			WebTarget widgetUrlTarget = ClientBuilder.newClient().target(userUnRegisterUrl).queryParam("auth", "{auth}")
					.queryParam("user_auth", "{user_auth}").resolveTemplate("auth", partnerAuth)
					.resolveTemplate("user_auth", userAuth);

			Invocation.Builder gstInvocationBuilder = widgetUrlTarget.request(MediaType.APPLICATION_JSON_TYPE);

			Response response = gstInvocationBuilder.post(null);
			String userUnregisterString = response.readEntity(String.class);
			JSONParser parser = new JSONParser();
			JSONObject userUnregisterJson = (JSONObject) parser.parse(userUnregisterString);

			if (userUnregisterJson.get("status") != null) {
				JSONObject status = (JSONObject) userUnregisterJson.get("status");
				if (status.get("code").toString().equals("200")) {
					logger.info("User unregistered successfully");

				}

			}

		} catch (Exception e) {
			throw new ApplicationException(e);
		}

	}

	public List<PerfiosMetadataVo> getMetadata(String partnerAuth) throws ApplicationException {
		List<PerfiosMetadataVo> metadataList = new ArrayList<PerfiosMetadataVo>();
		try {

			WebTarget metadataTarget = ClientBuilder.newClient().target(siteMetadataUrl).queryParam("auth", "{auth}")
					.resolveTemplate("auth", partnerAuth);

			Invocation.Builder gstInvocationBuilder = metadataTarget.request(MediaType.APPLICATION_JSON_TYPE);

			Response response = gstInvocationBuilder.post(null);
			String metadataString = response.readEntity(String.class);
			JSONParser parser = new JSONParser();
			JSONObject metadataJson = (JSONObject) parser.parse(metadataString);

			if (metadataJson.get("status") != null && metadataJson.get("data") != null) {
				JSONObject status = (JSONObject) metadataJson.get("status");
				if (status.get("code").toString().equals("200")) {
					logger.info("Site Metadata fetched successfully");
					JSONArray dataArray = (JSONArray) metadataJson.get("data");

					for (Object dataObj : dataArray) {
						JSONObject data = (JSONObject) dataObj;
						PerfiosMetadataVo metadata = new PerfiosMetadataVo();
						metadata.setDisplayIdTypeIdentifiers(data.get("displayIdTypeIdentifiers") != null
								? (Long) data.get("displayIdTypeIdentifiers")
										: null);
						metadata.setInstDesc(data.get("instDesc") != null ? data.get("instDesc").toString() : null);
						metadata.setInstId(data.get("instId") != null ? (Long) data.get("instId") : null);
						metadata.setInstCode(data.get("instCode") != null ? (Long) data.get("instCode") : null);
						metadata.setInstName(data.get("instName") != null ? data.get("instName").toString() : null);
						metadata.setDiscoverySupported(
								data.get("isDiscoverySupported") != null ? (boolean) data.get("isDiscoverySupported")
										: null);
						metadata.setLabelsConfirmPassword(
								data.get("labelsConfirmPassword") != null ? data.get("labelsConfirmPassword").toString()
										: null);
						metadata.setLabelsPassword(
								data.get("labelsPassword") != null ? data.get("labelsPassword").toString() : null);
						metadata.setLabelsUserId(
								data.get("labelsUserId") != null ? data.get("labelsUserId").toString() : null);
						metadata.setMfaRequirement(
								data.get("mfaRequirement") != null ? data.get("mfaRequirement").toString() : null);

						metadata.setDisplayIdTypeChoices(
								data.get("displayIdTypeChoices") != null ? data.get("displayIdTypeChoices").toString() : null);
						metadata.setDisplayIdTypeValues(
								data.get("displayIdTypeValues") != null ? data.get("displayIdTypeValues").toString() : null);

						metadataList.add(metadata);
					}
				}
			}

		} catch (Exception e) {
			throw new ApplicationException(e);
		}
		return metadataList;
	}

	public List<PerfiosStatementInstitutionVo> getStatementInstitution(String auth, String userAuth) throws ApplicationException {
		List<PerfiosStatementInstitutionVo> statements = new ArrayList<PerfiosStatementInstitutionVo>();
		try {

			WebTarget target = ClientBuilder.newClient().target(statementInstitutionUrl)
					.queryParam("auth", "{auth}").queryParam("user_auth", "{user_auth}")
					.resolveTemplate("auth", auth).resolveTemplate("user_auth", userAuth);

			Invocation.Builder gstInvocationBuilder = target.request(MediaType.APPLICATION_JSON_TYPE);

			Response response = gstInvocationBuilder.post(null);
			String userAuthString = response.readEntity(String.class);
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(userAuthString);

			if (json.get("status") != null) {
				JSONObject status = (JSONObject) json.get("status");
				if (status.get("code").toString().equals("200")) {
					JSONArray dataArray = (JSONArray) json.get("data");

					for (Object dataObj : dataArray) {
						JSONObject data = (JSONObject) dataObj;
						PerfiosStatementInstitutionVo psi = new PerfiosStatementInstitutionVo();
						psi.setId(data.get("id") != null
								? (Long) data.get("id")
										: null);
						psi.setName(data.get("name") != null ? data.get("name").toString() : null);
						psi.setiType(data.get("itype") != null ? (String) data.get("itype") : null);
						psi.setDesc(data.get("desc") != null ? (String) data.get("desc") : null);
						psi.setGeography(data.get("geography") != null ? (String) data.get("geography") : null);
						if(psi.getiType()!=null){
							if(psi.getiType().equals("bank")|| psi.getiType().equals("cc")){
								statements.add(psi);
							}
						}
					}

				}

			}

		} catch (Exception e) {
			throw new ApplicationException(e);
		}

		return statements;
	}
}

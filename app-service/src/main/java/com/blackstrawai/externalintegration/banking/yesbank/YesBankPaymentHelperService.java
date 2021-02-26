package com.blackstrawai.externalintegration.banking.yesbank;

import java.io.IOException;
import java.util.Objects;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.common.CommonConstants;
import com.blackstrawai.externalintegration.yesbank.WorkflowBankingVo;
import com.blackstrawai.externalintegration.yesbank.Request.paymenttransfer.PaymentTransferVo;
import com.blackstrawai.externalintegration.yesbank.Response.YesBankAuthorizationVo;
import com.blackstrawai.externalintegration.yesbank.Response.bulkpayments.BulkPaymentsResponse;
import com.blackstrawai.externalintegration.yesbank.Response.paymenttransfer.PaymentTransferResponseVo;
import com.blackstrawai.yesbank.YesBank;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class YesBankPaymentHelperService {

	private final ObjectMapper objectMapper = new ObjectMapper();
	private final Logger logger = Logger.getLogger(YesBankPaymentHelperService.class);

	private final YesBankIntegrationDao yesBankIntegrationDao;

	public YesBankPaymentHelperService(YesBankIntegrationDao yesBankIntegrationDao) {
		this.yesBankIntegrationDao = yesBankIntegrationDao;
	}

	public PaymentTransferResponseVo makeSinglePayment(PaymentTransferVo paymentTransferPaymentsVo, String organizationId,
			String userId, String roleName, String finalJsonRequest, String uiJsonRequest, int paymentId)
					throws ApplicationException, IOException, JsonParseException, JsonMappingException {
		YesBankAuthorizationVo authorization = yesBankIntegrationDao.getCustomerAuthorization(organizationId,
				paymentTransferPaymentsVo.getData().getConsentId(),
				paymentTransferPaymentsVo.getData().getInitiation().getDebtorAccount().getIdentification(), userId,
				roleName);

		if (Objects.isNull(authorization)) {
			throw new ApplicationException(YesBankConstants.ERROR_YBL_CUST_INFO_NOT_AVAIALBLE);
		}

		PaymentTransferResponseVo paymentTransferResponseVo = null;

		if (Objects.nonNull(finalJsonRequest)) {

			logger.info("Final Payment Transfer Request" + finalJsonRequest);
			String paymentTransferJsonRes = YesBank.getInstance().getSingleResponseEntity("domesticPayments",
					finalJsonRequest, authorization);
			logger.info("Final Payment Transfer Response" + paymentTransferJsonRes);

			JsonNode node = objectMapper.readTree(paymentTransferJsonRes);
			String code = null;
			if (Objects.nonNull(node.get("Code"))) {
				code = node.get("Code").asText();
				logger.info("Code:::" + code);
			}

			if (Objects.isNull(code)) {
				paymentTransferResponseVo = objectMapper.readValue(paymentTransferJsonRes,
						PaymentTransferResponseVo.class);
				logger.info("<<<<payment Transfer Response >>>>" + paymentTransferResponseVo);

				if (Objects.nonNull(paymentTransferResponseVo)) {

					yesBankIntegrationDao.updatePaymentTransferInfo(paymentTransferPaymentsVo, finalJsonRequest,
							paymentTransferResponseVo, paymentTransferJsonRes, organizationId, userId, roleName,
							uiJsonRequest, paymentId);
				}
			} else {
				throw new ApplicationException(YesBankConstants.ERROR_YBL_NOT_VALID_REQUEST);
			}
		}
		return paymentTransferResponseVo;
	}

	public boolean rejectOrDeclinePayment(int paymentId,boolean isReject,String paymentType,String fileIdentifier)throws ApplicationException {
		return yesBankIntegrationDao.rejectOrDeclinePayment(paymentId, isReject,paymentType,fileIdentifier);
	}	



	public BulkPaymentsResponse makeBulkPayment(int paymentId)
			throws ApplicationException, IOException, JsonParseException, JsonMappingException {
		
		WorkflowBankingVo workflowBankingVo=yesBankIntegrationDao.getWorfklowDataForPayment(paymentId);
		
		BulkPaymentsResponse paymentTransferResponseVo=new BulkPaymentsResponse();
		YesBankAuthorizationVo authorization = yesBankIntegrationDao.getCustomerAuthorization(String.valueOf(workflowBankingVo.getOrganizationId()),
				workflowBankingVo.getPaymentTransferPaymentsVo().getData().getConsentId(),
				workflowBankingVo.getPaymentTransferPaymentsVo().getData().getInitiation().getDebtorAccount().getIdentification(), workflowBankingVo.getUserId(),
				workflowBankingVo.getRoleName());
		
		if (Objects.nonNull(workflowBankingVo.getFinalJsonRequest())) {

			logger.info("Final Payment Transfer Request" + workflowBankingVo.getFinalJsonRequest());
			String paymentTransferJsonRes = YesBank.getInstance().getBulkResponseEntity("bulkPayments",
					workflowBankingVo.getFinalJsonRequest(), authorization);
			logger.info("Final Payment Transfer Response" + paymentTransferJsonRes);

			JsonNode node = objectMapper.readTree(paymentTransferJsonRes);
			logger.info("node:::" + node);

			String data = null;
			if (Objects.nonNull(node.get("Data"))) {
				data = node.get("Data").toString();
				logger.info("Data:::" + data);
			}
			String fileName=null;
			if (Objects.nonNull(data)) {
				paymentTransferResponseVo = objectMapper.readValue(paymentTransferJsonRes,
						BulkPaymentsResponse.class);
				fileName= paymentTransferResponseVo.getData().getFileName();
				logger.info("<<<<payment Transfer Response >>>>" + paymentTransferResponseVo);
			}
			if (Objects.nonNull(paymentTransferJsonRes)) {
				yesBankIntegrationDao.makeBulkPayment(workflowBankingVo.getFileIdentifier(),paymentTransferJsonRes,fileName);
			}
		} else {
			throw new ApplicationException(YesBankConstants.ERROR_YBL_NOT_VALID_REQUEST);
		}


		return paymentTransferResponseVo;
	}

	public String getSingleTxnStatusOfBulkPayment(String fileIdentifier ,String  orgId , String uniqueIdentifier, String customerId, String accountNo , String userId , String roleName) throws ApplicationException {
		String status = "PENDING";

		String request = "{\r\n" + 
				"			  \"Data\": {\r\n" + 
				"			      \"InstrId\": \""+uniqueIdentifier+"\",\r\n" + 
				"			      \"ConsentId\": \""+customerId+"\",\r\n" + 
				"			      \"SecondaryIdentification\": \""+customerId+"\"\r\n" + 
				"			    }\r\n" + 
				"			  }";

		YesBankAuthorizationVo authorization = yesBankIntegrationDao.getCustomerAuthorization(orgId,
				customerId,
				accountNo, userId,
				roleName);
		try {
			if (Objects.isNull(authorization)) {
				throw new ApplicationException(YesBankConstants.ERROR_YBL_CUST_INFO_NOT_AVAIALBLE);
			}

			logger.info("Final Payment Transfer Request" + request);
			String paymentStatusJsonRes = YesBank.getInstance().getBulkResponseEntity("bulkPayments_detail",
					request, authorization);
			logger.info("Final Payment Transfer Response" + paymentStatusJsonRes);

			JsonNode node;

			node = objectMapper.readTree(paymentStatusJsonRes);

			String data = null;
			String code = null;

			if (Objects.nonNull(node.get("Data"))) {
				JsonNode nodeData =  node.get("Data");
				status = nodeData.get("Status").toString().replaceAll("\"", "");
				data = nodeData.toString();
			}else {
				if (Objects.nonNull(node.get("Code"))) {
					code = node.get("Code").asText();
					status = CommonConstants.DISPLAY_STATUS_AS_FAILED;
				}
			}
			logger.info("data:::" + data);
			logger.info("Code:::" + code);
			logger.info("status:::" + status);
			if(status!=null) {
				yesBankIntegrationDao.updateBulkPaymentTransferInfo(uniqueIdentifier, paymentStatusJsonRes, orgId, userId, roleName,  status, fileIdentifier, customerId);
			}
			/*
		JsonNode node = objectMapper.readTree(paymentTransferJsonRes);
		String code = null;
		if (Objects.nonNull(node.get("Code"))) {
			code = node.get("Code").asText();
			logger.info("Code:::" + code);
		}

		if (Objects.isNull(code)) {
			paymentTransferResponseVo = objectMapper.readValue(paymentTransferJsonRes,
					BulkPaymentsResponse.class);
		}*/
			if(status.equalsIgnoreCase("pending")) {
				status = "Payment Initiated";
			}
			logger.info("<<<<payment Transfer Response >>>>" + paymentStatusJsonRes);
		} catch (IOException e) {
			throw new ApplicationException(e.getMessage());
		}
		return status;

	}
}

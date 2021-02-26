package com.blackstrawai.controller.externalintegration.banking.perfios;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.common.BaseController;
import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.common.Constants;
import com.blackstrawai.externalintegration.banking.perfios.PerfiosCallBackProcessingVo;
import com.blackstrawai.externalintegration.banking.perfios.PerfiosService;
import com.blackstrawai.externalintegration.banking.perfios.PerfiosStatementInstitutionVo;
import com.blackstrawai.externalintegration.banking.perfios.PerfiosUserAccountTransactionVo;
import com.blackstrawai.externalintegration.banking.perfios.PerfiosWidgetUrlVo;
import com.blackstrawai.request.PerfiosJSONObject;
import com.blackstrawai.response.externalintegration.banking.perfios.PerfiosResponse;
import com.blackstrawai.response.externalintegration.banking.perfios.PerfiosStatementInstitutionResponse;
import com.blackstrawai.response.externalintegration.banking.perfios.PerfiosUserTransactionResponse;

@Controller
@CrossOrigin
@RequestMapping("/decifer/perfios")
public class PerfiosController extends BaseController {
	@Autowired
	PerfiosService perfiosService;
	private Logger logger = Logger.getLogger(PerfiosController.class);
	private static Map<String,PerfiosCallBackProcessingVo>transactionMap=new ConcurrentHashMap<String,PerfiosCallBackProcessingVo>();


	@RequestMapping(value = "/v1/widget_url/{organizationId}/{userId}/{roleName}/{instId}", method = RequestMethod.POST)
	public ResponseEntity<BaseResponse> getWidgetUrl(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @PathVariable int organizationId, @PathVariable int userId, @PathVariable String roleName,
			@PathVariable int  instId ) {
		logger.info("Entry into method: getWidgetUrl");
		BaseResponse response = new PerfiosResponse();
		try {
			PerfiosWidgetUrlVo perfiosVo = perfiosService.getWidgetUrl(organizationId, userId, roleName,instId);

			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((PerfiosResponse) response).setData(perfiosVo);
			response = constructResponse(response, Constants.SUCCESS, Constants.PERFIOS_FETCH_SUCCESS,
					Constants.PERFIOS_FETCH_SUCCESS, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_PERFIOS_FETCH,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/v1/callback", method = RequestMethod.POST)
	public ResponseEntity<BaseResponse> callback(HttpServletRequest httpRequest, HttpServletResponse httpResponse,
			@RequestBody PerfiosJSONObject callbackRequest) {
		logger.info("Entry into method: callback");

		BaseResponse response = new PerfiosResponse();
		try {
			transactionMap.remove(callbackRequest.getUniqueUserId());
			perfiosService.sendEmail(callbackRequest.getAccountId(), callbackRequest.getUserId(), callbackRequest.getUniqueUserId()+"--"+callbackRequest.getDescription()+"--"+ callbackRequest.getMessage()+"--"+callbackRequest.getSuccess());
			int val=0;
			if(callbackRequest.getAccountId()!=null){
				val=Integer.parseInt(callbackRequest.getAccountId());				
			}
			if(val>0){
				PerfiosCallBackProcessingVo perfiosCallBackProcessingVo=new PerfiosCallBackProcessingVo();
				perfiosCallBackProcessingVo.setCallBackProcessingStarted(true);
				transactionMap.put(callbackRequest.getUniqueUserId(), perfiosCallBackProcessingVo);
				List<PerfiosUserAccountTransactionVo> userTransactionVo = perfiosService.getUserAccountTransaction(callbackRequest.getAccountId(), callbackRequest.getUniqueUserId());
				if (userTransactionVo != null) {			
					perfiosService.createUserAccountTransaction(userTransactionVo, callbackRequest.getUniqueUserId());
					if(transactionMap.containsKey(callbackRequest.getUniqueUserId())){
						PerfiosCallBackProcessingVo perfiosCallBackProcessingVo1=transactionMap.get(callbackRequest.getUniqueUserId());
						perfiosCallBackProcessingVo1.setCallBackProcessingCompleted(true);
					}
				}

			}else{
				PerfiosCallBackProcessingVo perfiosCallBackProcessingVo=new PerfiosCallBackProcessingVo();
				perfiosCallBackProcessingVo.setCallBackProcessingStarted(true);
				perfiosCallBackProcessingVo.setCallBackProcessingCompleted(true);
				perfiosCallBackProcessingVo.setMessage(callbackRequest.getDescription());
				transactionMap.put(callbackRequest.getUniqueUserId(), perfiosCallBackProcessingVo);
			}

			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((PerfiosResponse) response).setData(null);
			response = constructResponse(response, Constants.SUCCESS, Constants.PERFIOS_FETCH_SUCCESS,
					Constants.PERFIOS_FETCH_SUCCESS, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (Exception e) {			
			/*try {
				EmailUtil.getInstance().sendEmail("bhargav.maddikera@blackstraw.ai", e.getMessage(), e.getMessage());
			} catch (ApplicationException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}*/
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_PERFIOS_FETCH,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}



	@RequestMapping(value = "/v1/poll/callback/{uniqueUserId}", method = RequestMethod.GET)
	public ResponseEntity<BaseResponse> pollCallback(HttpServletRequest httpRequest, HttpServletResponse httpResponse,
			@PathVariable String uniqueUserId) {
		logger.info("Entry into method: pollCallback");

		BaseResponse response = new PerfiosUserTransactionResponse();
		PerfiosCallBackProcessingVo perfiosCallBackProcessingVo=null;
		try {
			synchronized (uniqueUserId) {
				if (transactionMap.containsKey(uniqueUserId)) {
					perfiosCallBackProcessingVo = transactionMap.get(uniqueUserId);
				}
			}
			((PerfiosUserTransactionResponse) response).setData(perfiosCallBackProcessingVo);
			/*if (transactionMap.containsKey(uniqueUserId)) {
				transactionMap.remove(uniqueUserId);				
			}
*/
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));			
			response = constructResponse(response, Constants.SUCCESS, Constants.PERFIOS_FETCH_SUCCESS,
					Constants.PERFIOS_FETCH_SUCCESS, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (Exception e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_PERFIOS_FETCH,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}


	@RequestMapping(value = "/v1/clear/transaction_map/{uniqueUserId}", method = RequestMethod.GET)
	public ResponseEntity<BaseResponse> clearUserId(HttpServletRequest httpRequest, HttpServletResponse httpResponse,
			@PathVariable String uniqueUserId) {
		logger.info("Entry into method: clearUserId");

		BaseResponse response = new PerfiosUserTransactionResponse();
		try {
			synchronized (uniqueUserId) {
				if (transactionMap.containsKey(uniqueUserId)) {
					PerfiosCallBackProcessingVo data=transactionMap.get(uniqueUserId);
					data.setCallBackProcessingCompleted(false);
					data.setCallBackProcessingStarted(false);
					data.setMessage(null);
					transactionMap.remove(uniqueUserId);				
				}
			}
			((PerfiosUserTransactionResponse) response).setData(null);

			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));			
			response = constructResponse(response, Constants.SUCCESS, Constants.PERFIOS_FETCH_SUCCESS,
					Constants.PERFIOS_FETCH_SUCCESS, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (Exception e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_PERFIOS_FETCH,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/v1/statement_institution", method = RequestMethod.POST)
	public ResponseEntity<BaseResponse> getStatementInstitution(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse) {
		logger.info("Entry into method: getWidgetUrl");
		BaseResponse response = new PerfiosStatementInstitutionResponse();
		try {
			List<PerfiosStatementInstitutionVo> perfiosVo = perfiosService.getStatementInstitution();			
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((PerfiosStatementInstitutionResponse) response).setData(perfiosVo);
			response = constructResponse(response, Constants.SUCCESS, Constants.PERFIOS_FETCH_SUCCESS,
					Constants.PERFIOS_FETCH_SUCCESS, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_PERFIOS_FETCH,
					Constants.FAILURE_PERFIOS_FETCH_MESSAGE, Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/*@RequestMapping(value = "/v1/unique_user_ids", method = RequestMethod.GET)
	public ResponseEntity<BaseResponse> getAllUniqueUserId(HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
		logger.info("Entry into method: getAllUniqueUserId");

		BaseResponse response = new PerfiosUniqueUserIdResponse();
		try {			
			((PerfiosUniqueUserIdResponse) response).setData(transactionMap);
			response = constructResponse(response, Constants.SUCCESS, Constants.PERFIOS_FETCH_SUCCESS,
					Constants.PERFIOS_FETCH_SUCCESS, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (Exception e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_PERFIOS_FETCH,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}	
	 */

	/*@RequestMapping(value = "/v1/unregistration/{uniqueUserId}", method = RequestMethod.POST)
	public ResponseEntity<BaseResponse> deleteUser(HttpServletRequest httpRequest, HttpServletResponse httpResponse,
			@PathVariable String uniqueUserId) {
		logger.info("Entry into method: deleteUser");

		BaseResponse response = new PerfiosResponse();
		try {
			perfiosService.deleteUser(uniqueUserId);			
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));			
			response = constructResponse(response, Constants.SUCCESS, Constants.PERFIOS_USER_UNREGISTRATION_SUCCESS,
					Constants.PERFIOS_USER_UNREGISTRATION_SUCCESS, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (Exception e) {
			response = constructResponse(response, Constants.FAILURE, Constants.PERFIOS_USER_UNREGISTRATION_FAILURE,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}*/

	/*@RequestMapping(value = "/v1/metadata", method = RequestMethod.POST)
	public ResponseEntity<BaseResponse> getMetadata(HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
		logger.info("Entry into method: getMetadata");

		BaseResponse response = new PerfiosMetadataResponse();
		try {
			List<PerfiosMetadataVo> metadata = perfiosService.getMetadata();	
			((PerfiosMetadataResponse) response).setData(metadata);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));			
			response = constructResponse(response, Constants.SUCCESS, Constants.PERFIOS_FETCH_METADATA_SUCCESS,
					Constants.PERFIOS_FETCH_METADATA_SUCCESS, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (Exception e) {
			response = constructResponse(response, Constants.FAILURE, Constants.PERFIOS_FETCH_METADATA_FAILURE,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/v1/metadata/user_transaction/{uniqueUserId}/{instId}", method = RequestMethod.POST)
	public ResponseEntity<BaseResponse> getMetadataUserTransaction(HttpServletRequest httpRequest, HttpServletResponse httpResponse, @PathVariable String uniqueUserId, 
			@PathVariable Long instId) {
		logger.info("Entry into method: getMetadataUserTransaction");

		BaseResponse response = new PerfiosMetadataUserTransactionResponse();
		try {
			boolean isUserExist = perfiosService.isUserIdInstIdExist(uniqueUserId, instId);
			if (isUserExist) {
				String accountId = perfiosService.getAccountIdFromUserTransactions(uniqueUserId, instId);
				List<PerfiosUserAccountTransactionVo> uatList = perfiosService.getUserAccountTransaction(accountId, uniqueUserId);
				perfiosService.deleteUserAccountTransaction(uniqueUserId, instId);
				perfiosService.createUserAccountTransaction(uatList, uniqueUserId);
				((PerfiosMetadataUserTransactionResponse) response).setUserExist(true);
				((PerfiosMetadataUserTransactionResponse) response).setData(uatList);
			} else {
				((PerfiosMetadataUserTransactionResponse) response).setUserExist(false);
				((PerfiosMetadataUserTransactionResponse) response).setData(null);
			}

			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));			
			response = constructResponse(response, Constants.SUCCESS, Constants.PERFIOS_FETCH_METADATA_SUCCESS,
					Constants.PERFIOS_FETCH_METADATA_SUCCESS, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (Exception e) {
			response = constructResponse(response, Constants.FAILURE, Constants.PERFIOS_FETCH_METADATA_FAILURE,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}*/


	/*@RequestMapping(value = "/v1/check_callback_started/{uniqueUserId}", method = RequestMethod.GET)
	public ResponseEntity<BaseResponse> isCallBackStarted(HttpServletRequest httpRequest, HttpServletResponse httpResponse,
			@PathVariable String uniqueUserId) {
		logger.info("Entry into method: isCallBackStarted");

		BaseResponse response = new PerfiosUserTransactionResponse();
		try {
			synchronized (uniqueUserId) {
				if (transactionMap.containsKey(uniqueUserId)) {
					((PerfiosUserTransactionResponse) response).setCallBackStarted(true);
				}
			}
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));			
			response = constructResponse(response, Constants.SUCCESS, Constants.PERFIOS_FETCH_SUCCESS,
					Constants.PERFIOS_FETCH_SUCCESS, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (Exception e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_PERFIOS_FETCH,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	 */

}

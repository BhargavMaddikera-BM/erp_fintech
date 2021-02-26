package com.blackstrawai.controller.banking;

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
import com.blackstrawai.banking.BankMasterService;
import com.blackstrawai.banking.dashboard.BankMasterAccountBaseVo;
import com.blackstrawai.banking.dashboard.BankMasterAccountVo;
import com.blackstrawai.banking.dashboard.BankMasterCardVo;
import com.blackstrawai.banking.dashboard.BankMasterCashAccountVo;
import com.blackstrawai.banking.dashboard.BankMasterWalletVo;
import com.blackstrawai.banking.dropdowns.BankMasterDropDownVo;
import com.blackstrawai.common.BaseController;
import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.common.Constants;
import com.blackstrawai.helper.BankingConvertToVoHelper;
import com.blackstrawai.request.JSONObject;
import com.blackstrawai.request.banking.dashboard.BankMasterAccountRequest;
import com.blackstrawai.request.banking.dashboard.BankMasterCardRequest;
import com.blackstrawai.request.banking.dashboard.BankMasterCashAccountRequest;
import com.blackstrawai.request.banking.dashboard.BankMasterWalletRequest;
import com.blackstrawai.response.banking.dashboard.BankMasterAccountBaseResponse;
import com.blackstrawai.response.banking.dashboard.BankMasterAccountResponse;
import com.blackstrawai.response.banking.dashboard.BankMasterCardResponse;
import com.blackstrawai.response.banking.dashboard.BankMasterCashAccountResponse;
import com.blackstrawai.response.banking.dashboard.BankMasterDropDownResponse;
import com.blackstrawai.response.banking.dashboard.BankMasterWalletResponse;

@RestController
@CrossOrigin
@RequestMapping("/decifer/bank_master")
public class BankMasterController extends BaseController {

	@Autowired
	BankMasterService bankMasterService;
	private Logger logger = Logger.getLogger(BankMasterController.class);

	// For Creating Bank Master Accounts
	@RequestMapping(value = "/v1/bankMasterAccounts", method = RequestMethod.POST)
	public ResponseEntity<BaseResponse> createBankMasterAccounts(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse,
			@RequestBody JSONObject<BankMasterAccountRequest> bankMasterAccountRequest) {
		logger.info("Entry into method: createBankMasterAccounts");
		BaseResponse response = new BankMasterAccountResponse();
		try {
			logger.info("Request Payload:" + generateRequestAndResponseLogPaylod(bankMasterAccountRequest));
			BankMasterAccountVo bankMasterAccountVo = BankingConvertToVoHelper.getInstance()
					.convertBankMasterAccountsVoFromBankMasterAccountsRequest(bankMasterAccountRequest.getData());
			bankMasterService.createBankMasterAccounts(bankMasterAccountVo);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_BANK_MASTER_ACCOUNTS_CREATED,
					Constants.SUCCESS_BANK_MASTER_ACCOUNTS_CREATED,
					Constants.BANK_MASTER_ACCOUNTS_CREATED_SUCCESSFULLY);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			logger.error("Error Payload:" + e.getMessage());
			if (e.getCause() != null) {
				response = constructResponse(response, Constants.FAILURE,
						Constants.FAILURE_BANK_MASTER_ACCOUNTS_CREATED, e.getCause().getMessage(),
						Constants.BANK_MASTER_ACCOUNTS_CREATED_UNSUCCESSFULLY);
			} else {
				response = constructResponse(response, Constants.FAILURE,
						Constants.FAILURE_BANK_MASTER_ACCOUNTS_CREATED, e.getMessage(),
						Constants.BANK_MASTER_ACCOUNTS_CREATED_UNSUCCESSFULLY);
			}

			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	// For Creating Bank Master Credit Card
	@RequestMapping(value = "/v1/bankMasterCards", method = RequestMethod.POST)
	public ResponseEntity<BaseResponse> createBankMasterCard(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @RequestBody JSONObject<BankMasterCardRequest> bankMasterCardRequest) {
		logger.info("Entry into method: createBankMasterCard");
		BaseResponse response = new BankMasterCardResponse();
		try {
			logger.info("Request Payload:" + generateRequestAndResponseLogPaylod(bankMasterCardRequest));
			BankMasterCardVo bankMasterCardVo = BankingConvertToVoHelper.getInstance()
					.convertBankMasterCardsVoFromBankMasterCardsRequest(bankMasterCardRequest.getData());
			bankMasterService.createBankMasterCard(bankMasterCardVo);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_BANK_MASTER_CARD_CREATED,
					Constants.SUCCESS_BANK_MASTER_CARD_CREATED, Constants.BANK_MASTER_CARD_CREATED_SUCCESSFULLY);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			logger.error("Error Payload:" + e.getMessage());
			if (e.getCause() != null) {
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_BANK_MASTER_CARD_CREATED,
						e.getCause().getMessage(), Constants.BANK_MASTER_CARD_CREATED_UNSUCCESSFULLY);
			} else {
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_BANK_MASTER_CARD_CREATED,
						e.getMessage(), Constants.BANK_MASTER_CARD_CREATED_UNSUCCESSFULLY);
			}

			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	// For Creating Bank Master Wallet
	@RequestMapping(value = "/v1/bankMasterWallets", method = RequestMethod.POST)
	public ResponseEntity<BaseResponse> createBankMasterWallet(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse,
			@RequestBody JSONObject<BankMasterWalletRequest> bankMasterWalletRequest) {
		logger.info("Entry into method: createBankMasterWallet");
		BaseResponse response = new BankMasterWalletResponse();
		try {
			logger.info("Request Payload:" + generateRequestAndResponseLogPaylod(bankMasterWalletRequest));
			BankMasterWalletVo bankMasterWalletVo = BankingConvertToVoHelper.getInstance()
					.convertBankMasterWalletsVoFromBankMasterWalletsRequest(bankMasterWalletRequest.getData());
			bankMasterService.createBankMasterWallet(bankMasterWalletVo);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_BANK_MASTER_WALLET_CREATED,
					Constants.SUCCESS_BANK_MASTER_WALLET_CREATED, Constants.BANK_MASTER_WALLET_CREATED_SUCCESSFULLY);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			logger.error("Error Payload:" + e.getMessage());
			if (e.getCause() != null) {
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_BANK_MASTER_WALLET_CREATED,
						e.getCause().getMessage(), Constants.BANK_MASTER_WALLET_CREATED_UNSUCCESSFULLY);
			} else {
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_BANK_MASTER_WALLET_CREATED,
						e.getMessage(), Constants.BANK_MASTER_WALLET_CREATED_UNSUCCESSFULLY);
			}

			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	// For Creating Bank Master Cash Accounts
	@RequestMapping(value = "/v1/bankMasterCashAccounts", method = RequestMethod.POST)
	public ResponseEntity<BaseResponse> createBankMasterCashAccounts(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse,
			@RequestBody JSONObject<BankMasterCashAccountRequest> bankMasterCashAccountRequest) {
		logger.info("Entry into method: createBankMasterCashAccounts");
		BaseResponse response = new BankMasterCashAccountResponse();
		try {
			logger.info("Request Payload:" + generateRequestAndResponseLogPaylod(bankMasterCashAccountRequest));
			BankMasterCashAccountVo bankMasterCashAccountVo = BankingConvertToVoHelper.getInstance()
					.convertbankMasterCashAccountVoFromBankMasterCashAccountRequest(
							bankMasterCashAccountRequest.getData());
			bankMasterService.createBankMasterCashAccounts(bankMasterCashAccountVo);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			response = constructResponse(response, Constants.SUCCESS,
					Constants.SUCCESS_BANK_MASTER_CASH_ACCOUNT_CREATED,
					Constants.SUCCESS_BANK_MASTER_CASH_ACCOUNT_CREATED,
					Constants.BANK_MASTER_CASH_ACCOUNT_CREATED_SUCCESSFULLY);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			logger.error("Error Payload:" + e.getMessage());
			if (e.getCause() != null) {
				response = constructResponse(response, Constants.FAILURE,
						Constants.FAILURE_BANK_MASTER_CASH_ACCOUNT_CREATED, e.getCause().getMessage(),
						Constants.BANK_MASTER_CASH_ACCOUNT_CREATED_UNSUCCESSFULLY);
			} else {
				response = constructResponse(response, Constants.FAILURE,
						Constants.FAILURE_BANK_MASTER_CASH_ACCOUNT_CREATED, e.getMessage(),
						Constants.BANK_MASTER_CASH_ACCOUNT_CREATED_UNSUCCESSFULLY);
			}

			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}


	//For creating Bank Master Drop Downs
	@RequestMapping(value = "/v1/dropsdowns/bankMasters/{organizationId}")
	public ResponseEntity<BaseResponse> getBankMastersDropDownData(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, @PathVariable int organizationId) {
		logger.info("Entry into getBankMastersDropDownData");
		BaseResponse response = new BankMasterDropDownResponse();
		try {
			BankMasterDropDownVo data = bankMasterService.getBankMastersDropDownData(organizationId);
			setTokens(response, httpServletRequest.getHeader("keyToken"), httpServletRequest.getHeader("valueToken"));
			((BankMasterDropDownResponse) response).setData(data);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_DROP_DOWN_FETCH,
					Constants.SUCCESS_DROP_DOWN_FETCH, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);

		} catch (Exception e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_PO_FETCH, e.getMessage(),
					Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	// For Fetching Bank Master Accounts detail by id
	@RequestMapping(value = "/v1/bankMasterAccounts/{organizationId}/{organizationName}/{userId}/{roleName}/{id}", method = RequestMethod.GET)
	public ResponseEntity<BaseResponse> getBankMasterAccountsById(
			HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, 
			@PathVariable int organizationId, 
			@PathVariable String organizationName,
			@PathVariable int userId,
			@PathVariable String roleName,
			@PathVariable int id) {
		logger.info("Entry into method: getBankMasterAccountsById");
		BaseResponse response = new BankMasterAccountResponse();
		try {
			BankMasterAccountVo bankMasterAccountVo = bankMasterService.getBankMasterAccountsById(id);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((BankMasterAccountResponse) response).setData(bankMasterAccountVo);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_BANK_MASTER_ACCOUNT_FETCH,
					Constants.SUCCESS_BANK_MASTER_ACCOUNT_FETCH, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_BANK_MASTER_ACCOUNT_FETCH,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// For Fetching Bank Master Cards detail by id
	@RequestMapping(value = "/v1/bankMasterCards/{organizationId}/{organizationName}/{userId}/{roleName}/{id}", method = RequestMethod.GET)
	public ResponseEntity<BaseResponse> getBankMasterCardsById(
			HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, 
			@PathVariable int organizationId, 
			@PathVariable String organizationName,
			@PathVariable int userId,
			@PathVariable String roleName,
			@PathVariable int id) {
		logger.info("Entry into method: getBankMasterCardsById");
		BaseResponse response = new BankMasterCardResponse();
		try {
			BankMasterCardVo bankMasterCardVo = bankMasterService.getBankMasterCardsById(id);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((BankMasterCardResponse) response).setData(bankMasterCardVo);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_BANK_MASTER_CARD_FETCH,
					Constants.SUCCESS_BANK_MASTER_CARD_FETCH, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_BANK_MASTER_CARD_FETCH,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// For Fetching Bank Master Wallets detail by id
	@RequestMapping(value = "/v1/bankMasterWallets/{organizationId}/{organizationName}/{userId}/{roleName}/{id}", method = RequestMethod.GET)
	public ResponseEntity<BaseResponse> getBankMasterWalletsById(
			HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, 
			@PathVariable int organizationId, 
			@PathVariable String organizationName,
			@PathVariable int userId,
			@PathVariable String roleName,
			@PathVariable int id) {
		logger.info("Entry into method: getAccountingAspectsById");
		BaseResponse response = new BankMasterWalletResponse();
		try {
			BankMasterWalletVo bankMasterWalletVo = bankMasterService.getBankMasterWalletsById(id);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((BankMasterWalletResponse) response).setData(bankMasterWalletVo);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_BANK_MASTER_WALLET_FETCH,
					Constants.SUCCESS_BANK_MASTER_WALLET_FETCH, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_BANK_MASTER_WALLET_FETCH,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// For Fetching Bank Master Cash Account detail by id
	@RequestMapping(value = "/v1/bankMasterCashAccounts/{organizationId}/{organizationName}/{userId}/{roleName}/{id}", method = RequestMethod.GET)
	public ResponseEntity<BaseResponse> getBankMasterCashAccountById(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, 
			@PathVariable int organizationId, 
			@PathVariable String organizationName,
			@PathVariable int userId,
			@PathVariable String roleName,
			@PathVariable int id) {
		logger.info("Entry into method: getBankMasterCashAccountById");
		BaseResponse response = new BankMasterCashAccountResponse();
		try {
			BankMasterCashAccountVo bankMasterCashAccountVo = bankMasterService.getBankMasterCashAccountById(id);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((BankMasterCashAccountResponse) response).setData(bankMasterCashAccountVo);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_BANK_MASTER_CASH_ACCOUNT_FETCH,
					Constants.SUCCESS_BANK_MASTER_CASH_ACCOUNT_FETCH, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_BANK_MASTER_CASH_ACCOUNT_FETCH,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// For Updating Bank Master Accounts
	@RequestMapping(value = "/v1/bankMasterAccounts", method = RequestMethod.PUT)
	public ResponseEntity<BaseResponse> updateBankMasterAccounts(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse,
			@RequestBody JSONObject<BankMasterAccountRequest> bankMasterAccountRequest) {
		logger.info("Entry into method: updateBankMasterAccounts");
		BaseResponse response = new BankMasterAccountResponse();
		try {
			logger.info("Request Payload:" + generateRequestAndResponseLogPaylod(bankMasterAccountRequest));
			BankMasterAccountVo bankMasterAccountVo = BankingConvertToVoHelper.getInstance()
					.convertBankMasterAccountsVoFromBankMasterAccountsRequest(bankMasterAccountRequest.getData());
			bankMasterService.updateBankMasterAccounts(bankMasterAccountVo);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_BANK_MASTER_ACCOUNT_UPDATED,
					Constants.SUCCESS_BANK_MASTER_ACCOUNT_UPDATED, Constants.BANK_MASTER_ACCOUNTS_UPDATED_SUCCESSFULLY);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			if (e.getCause() != null) {
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_BANK_MASTER_ACCOUNT_UPDATED,
						e.getCause().getMessage(), Constants.BANK_MASTER_ACCOUNTS_UPDATED_UNSUCCESSFULLY);
			} else {
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_BANK_MASTER_ACCOUNT_UPDATED,
						e.getMessage(), Constants.BANK_MASTER_ACCOUNTS_UPDATED_UNSUCCESSFULLY);
			}

			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// For Updating Bank Master Cards
	@RequestMapping(value = "/v1/bankMasterCards", method = RequestMethod.PUT)
	public ResponseEntity<BaseResponse> updateBankMasterCards(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @RequestBody JSONObject<BankMasterCardRequest> bankMasterCardRequest) {
		logger.info("Entry into method: updateBankMasterCards");
		BaseResponse response = new BankMasterCardResponse();
		try {
			logger.info("Request Payload:" + generateRequestAndResponseLogPaylod(bankMasterCardRequest));
			BankMasterCardVo bankMasterCardVo = BankingConvertToVoHelper.getInstance()
					.convertBankMasterCardsVoFromBankMasterCardsRequest(bankMasterCardRequest.getData());
			bankMasterService.updateBankMasterCards(bankMasterCardVo);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_BANK_MASTER_CARD_UPDATED,
					Constants.SUCCESS_BANK_MASTER_CARD_UPDATED, Constants.BANK_MASTER_CARD_UPDATED_SUCCESSFULLY);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			if (e.getCause() != null) {
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_BANK_MASTER_CARD_UPDATED,
						e.getCause().getMessage(), Constants.BANK_MASTER_CARD_UPDATED_UNSUCCESSFULLY);
			} else {
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_BANK_MASTER_CARD_UPDATED,
						e.getMessage(), Constants.BANK_MASTER_CARD_UPDATED_UNSUCCESSFULLY);
			}

			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// For Updating Bank Master Wallets
	@RequestMapping(value = "/v1/bankMasterWallets", method = RequestMethod.PUT)
	public ResponseEntity<BaseResponse> updateBankMasterWallets(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse,
			@RequestBody JSONObject<BankMasterWalletRequest> bankMasterWalletRequest) {
		logger.info("Entry into method: updateBankMasterWallets");
		BaseResponse response = new BankMasterWalletResponse();
		try {
			logger.info("Request Payload:" + generateRequestAndResponseLogPaylod(bankMasterWalletRequest));
			BankMasterWalletVo bankMasterWalletVo = BankingConvertToVoHelper.getInstance()
					.convertBankMasterWalletsVoFromBankMasterWalletsRequest(bankMasterWalletRequest.getData());
			bankMasterService.updateBankMasterWallets(bankMasterWalletVo);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_BANK_MASTER_WALLET_UPDATED,
					Constants.SUCCESS_BANK_MASTER_WALLET_UPDATED, Constants.BANK_MASTER_WALLET_UPDATED_SUCCESSFULLY);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			if (e.getCause() != null) {
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_BANK_MASTER_WALLET_UPDATED,
						e.getCause().getMessage(), Constants.BANK_MASTER_WALLET_UPDATED_UNSUCCESSFULLY);
			} else {
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_BANK_MASTER_WALLET_UPDATED,
						e.getMessage(), Constants.BANK_MASTER_WALLET_UPDATED_UNSUCCESSFULLY);
			}

			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// For Updating Bank Master Cash Accounts
	@RequestMapping(value = "/v1/bankMasterCashAccounts", method = RequestMethod.PUT)
	public ResponseEntity<BaseResponse> updateBankMasterCashAccounts(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse,
			@RequestBody JSONObject<BankMasterCashAccountRequest> bankMasterCashAccountRequest) {
		logger.info("Entry into method: updateBankMasterCashAccounts");
		BaseResponse response = new BankMasterCashAccountResponse();
		try {
			logger.info("Request Payload:" + generateRequestAndResponseLogPaylod(bankMasterCashAccountRequest));
			BankMasterCashAccountVo bankMasterCashAccountVo = BankingConvertToVoHelper.getInstance()
					.convertbankMasterCashAccountVoFromBankMasterCashAccountRequest(
							bankMasterCashAccountRequest.getData());
			bankMasterService.updateBankMasterCashAccounts(bankMasterCashAccountVo);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			response = constructResponse(response, Constants.SUCCESS,
					Constants.SUCCESS_BANK_MASTER_CASH_ACCOUNT_UPDATED,
					Constants.SUCCESS_BANK_MASTER_CASH_ACCOUNT_UPDATED,
					Constants.BANK_MASTER_CASH_ACCOUNT_UPDATED_SUCCESSFULLY);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			if (e.getCause() != null) {
				response = constructResponse(response, Constants.FAILURE,
						Constants.FAILURE_BANK_MASTER_CASH_ACCOUNT_UPDATED, e.getCause().getMessage(),
						Constants.BANK_MASTER_CASH_ACCOUNT_UPDATED_UNSUCCESSFULLY);
			} else {
				response = constructResponse(response, Constants.FAILURE,
						Constants.FAILURE_BANK_MASTER_CASH_ACCOUNT_UPDATED, e.getMessage(),
						Constants.BANK_MASTER_CASH_ACCOUNT_UPDATED_UNSUCCESSFULLY);
			}

			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// For Fetching all Bank Master Accounts details
	@RequestMapping(value = "/v1/bankMasterAccounts/{organizationId}/{organizationName}/{userId}/{roleName}", method = RequestMethod.GET)
	public ResponseEntity<BaseResponse> getAllBankMasterAccountsOfAnOrganizationForUserAndRole(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @PathVariable int organizationId,@PathVariable String organizationName,
			@PathVariable String userId,@PathVariable String roleName) {
		logger.info("Entry into method: getAllBankMasterAccountsOfAnOrganizationForUserAndRole");
		BaseResponse response = new BankMasterAccountBaseResponse();
		try {
			List<BankMasterAccountBaseVo> listAllBankMasterAccounts = bankMasterService
					.getAllBankMasterAccountsOfAnOrganizationForUserAndRole(organizationId,userId,roleName);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((BankMasterAccountBaseResponse) response).setData(listAllBankMasterAccounts);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_BANK_MASTER_ACCOUNT_FETCH,
					Constants.SUCCESS_BANK_MASTER_ACCOUNT_FETCH, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_BANK_MASTER_ACCOUNT_FETCH,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}


	// For Fetching all Bank Master Card details
	@RequestMapping(value = "/v1/bankMasterCards/{organizationId}/{organizationName}/{userId}/{roleName}", method = RequestMethod.GET)
	public ResponseEntity<BaseResponse> getAllBankMasterCardsOfAnOrganizationForUserAndRole(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @PathVariable int organizationId,@PathVariable String organizationName,
			@PathVariable String userId,@PathVariable String roleName) {
		logger.info("Entry into method: getAllBankMasterCardsOfAnOrganizationForUserAndRole");
		BaseResponse response = new BankMasterAccountBaseResponse();
		try {
			List<BankMasterAccountBaseVo> listAllBankMasterCards = bankMasterService
					.getAllBankMasterCardsOfAnOrganizationForUserAndRole(organizationId,userId,roleName);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((BankMasterAccountBaseResponse) response).setData(listAllBankMasterCards);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_BANK_MASTER_CARD_FETCH,
					Constants.SUCCESS_BANK_MASTER_CARD_FETCH, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_BANK_MASTER_CARD_FETCH,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// For Fetching all Bank Master Wallet details
	@RequestMapping(value = "/v1/bankMasterWallets/{organizationId}/{organizationName}/{userId}/{roleName}", method = RequestMethod.GET)
	public ResponseEntity<BaseResponse> getAllBankMasterWalletsOfAnOrganizationForUserAndRole(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @PathVariable int organizationId,@PathVariable String organizationName,
			@PathVariable String userId,@PathVariable String roleName) {
		logger.info("Entry into method: getAllBankMasterWalletsOfAnOrganizationForUserAndRole");
		BaseResponse response = new BankMasterAccountBaseResponse();
		try {
			List<BankMasterAccountBaseVo> listAllBankMasterWallets = bankMasterService
					.getAllBankMasterWalletsOfAnOrganizationForUserAndRole(organizationId,userId,roleName);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((BankMasterAccountBaseResponse) response).setData(listAllBankMasterWallets);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_BANK_MASTER_WALLET_FETCH,
					Constants.SUCCESS_BANK_MASTER_WALLET_FETCH, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_BANK_MASTER_WALLET_FETCH,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// For Fetching all Bank Master Cash Accounts details
	@RequestMapping(value = "/v1/bankMasterCashAccounts/{organizationId}/{organizationName}/{userId}/{roleName}", method = RequestMethod.GET)
	public ResponseEntity<BaseResponse> getAllBankMasterCashAccountsOfAnOrganizationForUserAndRole(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @PathVariable int organizationId,@PathVariable String organizationName,
			@PathVariable String userId,@PathVariable String roleName) {
		logger.info("Entry into method: getAllBankMasterCashAccountsOfAnOrganizationForUserAndRole");
		BaseResponse response = new BankMasterAccountBaseResponse();
		try {
			List<BankMasterAccountBaseVo> listAllBankMasterCashAccounts = bankMasterService
					.getAllBankMasterCashAccountsOfAnOrganizationForUserAndrole(organizationId,userId,roleName);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((BankMasterAccountBaseResponse) response).setData(listAllBankMasterCashAccounts);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_BANK_MASTER_CASH_ACCOUNT_FETCH,
					Constants.SUCCESS_BANK_MASTER_CASH_ACCOUNT_FETCH, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_BANK_MASTER_CASH_ACCOUNT_FETCH,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}


	// For Fetching all Bank Master details
	@RequestMapping(value = "/v1/bankMasterAllAccounts/{organizationId}/{organizationName}/{userId}/{roleName}", method = RequestMethod.GET)
	public ResponseEntity<BaseResponse> getAllBankMasterDetailsOfAnOrganizationForUserAndRole(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @PathVariable int organizationId,@PathVariable String organizationName,
			@PathVariable String userId,@PathVariable String roleName) {
		logger.info("Entry into method: getAllBankMasterDetailsOfAnOrganizationForUserAndRole");
		BaseResponse response = new BankMasterAccountBaseResponse();
		try {
			List<BankMasterAccountBaseVo> bankMasterAllAccounts = bankMasterService
					.getAllBankMasterDetailsOfAnOrganizationForUserAndRole(organizationId,userId,roleName);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((BankMasterAccountBaseResponse) response).setData(bankMasterAllAccounts);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_BANK_MASTER_DETAILS_FETCH,
					Constants.SUCCESS_BANK_MASTER_DETAILS_FETCH, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_BANK_MASTER_DETAILS_FETCH,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	/*	// For Fetching all Bank Master Accounts details
	@RequestMapping(value = "/v1/bankMasterAccounts/{organizationId}/{userId}", method = RequestMethod.GET)
	public ResponseEntity<BaseResponse> getAllBankMasterAccountsOfAnOrganization(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @PathVariable int organizationId, @PathVariable int userId) {
		logger.info("Entry into method: getAllBankMasterAccountsOfAnOrganization");
		BaseResponse response = new BankMasterAccountBaseResponse();
		try {
			List<BankMasterAccountBaseVo> listAllBankMasterAccounts = bankMasterService
					.getAllBankMasterAccountsOfAnOrganization(organizationId);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((BankMasterAccountBaseResponse) response).setData(listAllBankMasterAccounts);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_BANK_MASTER_ACCOUNT_FETCH,
					Constants.SUCCESS_BANK_MASTER_ACCOUNT_FETCH, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_BANK_MASTER_ACCOUNT_FETCH,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}*/

	/*// For Fetching all Bank Master Card details
	@RequestMapping(value = "/v1/bankMasterCards/{organizationId}/{userId}", method = RequestMethod.GET)
	public ResponseEntity<BaseResponse> getAllBankMasterCardsOfAnOrganization(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @PathVariable int organizationId, @PathVariable int userId) {
		logger.info("Entry into method: getAllBankMasterCardsOfAnOrganization");
		BaseResponse response = new BankMasterAccountBaseResponse();
		try {
			List<BankMasterAccountBaseVo> listAllBankMasterCards = bankMasterService
					.getAllBankMasterCardsOfAnOrganization(organizationId);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((BankMasterAccountBaseResponse) response).setData(listAllBankMasterCards);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_BANK_MASTER_CARD_FETCH,
					Constants.SUCCESS_BANK_MASTER_CARD_FETCH, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_BANK_MASTER_CARD_FETCH,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}*/

	/*// For Fetching all Bank Master Wallet details
	@RequestMapping(value = "/v1/bankMasterWallets/{organizationId}/{userId}", method = RequestMethod.GET)
	public ResponseEntity<BaseResponse> getAllBankMasterWalletsOfAnOrganization(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @PathVariable int organizationId, @PathVariable int userId) {
		logger.info("Entry into method: getAllBankMasterWalletsOfAnOrganization");
		BaseResponse response = new BankMasterAccountBaseResponse();
		try {
			List<BankMasterAccountBaseVo> listAllBankMasterWallets = bankMasterService
					.getAllBankMasterWalletsOfAnOrganization(organizationId);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((BankMasterAccountBaseResponse) response).setData(listAllBankMasterWallets);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_BANK_MASTER_WALLET_FETCH,
					Constants.SUCCESS_BANK_MASTER_WALLET_FETCH, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_BANK_MASTER_WALLET_FETCH,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}*/

/*	// For Fetching all Bank Master Cash Accounts details
	@RequestMapping(value = "/v1/bankMasterCashAccounts/{organizationId}/{userId}", method = RequestMethod.GET)
	public ResponseEntity<BaseResponse> getAllBankMasterCashAccountsOfAnOrganization(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @PathVariable int organizationId, @PathVariable int userId) {
		logger.info("Entry into method: getAllBankMasterCashAccountsOfAnOrganization");
		BaseResponse response = new BankMasterAccountBaseResponse();
		try {
			List<BankMasterAccountBaseVo> listAllBankMasterCashAccounts = bankMasterService
					.getAllBankMasterCashAccountsOfAnOrganization(organizationId);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((BankMasterAccountBaseResponse) response).setData(listAllBankMasterCashAccounts);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_BANK_MASTER_CASH_ACCOUNT_FETCH,
					Constants.SUCCESS_BANK_MASTER_CASH_ACCOUNT_FETCH, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_BANK_MASTER_CASH_ACCOUNT_FETCH,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}*/

	/*
	// For Fetching all Bank Master details
	@RequestMapping(value = "/v1/bankMasterAllAccounts/{organizationId}/{userId}", method = RequestMethod.GET)
	public ResponseEntity<BaseResponse> getAllBankMasterDetailsOfAnOrganization(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @PathVariable int organizationId, @PathVariable int userId) {
		logger.info("Entry into method: getAllBankMasterDetailsOfAnOrganization");
		BaseResponse response = new BankMasterAccountBaseResponse();
		try {
			List<BankMasterAccountBaseVo> bankMasterAllAccounts = bankMasterService
					.getAllBankMasterDetailsOfAnOrganization(organizationId);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((BankMasterAccountBaseResponse) response).setData(bankMasterAllAccounts);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_BANK_MASTER_DETAILS_FETCH,
					Constants.SUCCESS_BANK_MASTER_DETAILS_FETCH, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_BANK_MASTER_DETAILS_FETCH,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			logger.info(response);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
*/

}

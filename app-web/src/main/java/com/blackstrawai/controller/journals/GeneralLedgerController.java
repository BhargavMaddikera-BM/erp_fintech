package com.blackstrawai.controller.journals;

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
import org.springframework.web.bind.annotation.RestController;

import com.blackstrawai.common.BaseController;
import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.common.Constants;
import com.blackstrawai.journals.GeneralLedgerService;
import com.blackstrawai.journals.GeneralLedgerVo;
import com.blackstrawai.journals.JournalEntriesConstants;
import com.blackstrawai.request.JSONObject;
import com.blackstrawai.request.journals.GeneralLedgerRequest;
import com.blackstrawai.response.journals.GeneralLedgerResponse;

@RestController
@CrossOrigin
@RequestMapping("/decifer/general_ledger")

public class GeneralLedgerController  extends BaseController{
	private Logger logger = Logger.getLogger(GeneralLedgerController.class);

	@Autowired
	private GeneralLedgerService generalLedgerService;
	
	@RequestMapping(value = "/v1/general_ledgers")
	public ResponseEntity<BaseResponse> getGeneralLedgers(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, @RequestBody JSONObject<GeneralLedgerRequest> generalLedgerRequest) {
		logger.info("Entry into getGeneralLedgers");
		BaseResponse response = new GeneralLedgerResponse();
		try {
			GeneralLedgerVo data = null;
			logger.info("Request Payload:" + generateRequestAndResponseLogPaylod(generalLedgerRequest));

			if(generalLedgerRequest.getData().getInvoice()!=null) {
				String module = JournalEntriesConstants.SUB_MODULE_INVOICES;
				data = generalLedgerService.getGenealLedgers(module, generalLedgerRequest.getData().getInvoice());
			}
			if(generalLedgerRequest.getData().getCreditNote()!=null) {
				String module = JournalEntriesConstants.SUB_MODULE_CREDIT_NOTES;
				data = generalLedgerService.getGenealLedgers(module, generalLedgerRequest.getData().getCreditNote());
			}
			if(generalLedgerRequest.getData().getPayments()!=null) {
				String module = JournalEntriesConstants.SUB_MODULE_PAYMENTS;
				data = generalLedgerService.getGenealLedgers(module, generalLedgerRequest.getData().getPayments());
			}
			if(generalLedgerRequest.getData().getPayrun()!=null) {
				String module = JournalEntriesConstants.SUB_MODULE_PAY_RUN;
				data = generalLedgerService.getGenealLedgers(module, generalLedgerRequest.getData().getPayrun());
			}
			if(generalLedgerRequest.getData().getReceiptVo()!=null) {
				String module = JournalEntriesConstants.SUB_MODULE_RECEIPTS;
				data = generalLedgerService.getGenealLedgers(module, generalLedgerRequest.getData().getReceiptVo());
			}
			if(generalLedgerRequest.getData().getApInvoice()!=null) {
				String module = JournalEntriesConstants.SUB_MODULE_INVOICE_WITH_BILLS;
				data = generalLedgerService.getGenealLedgers(module, generalLedgerRequest.getData().getApInvoice());
			}
			setTokens(response, httpServletRequest.getHeader("keyToken"), httpServletRequest.getHeader("valueToken"));
			((GeneralLedgerResponse) response).setData(data);
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
	
	
	
	@RequestMapping(value = "/v1/view_journals/{organizationId}/{organizationName}/{id}/{module}")
	public ResponseEntity<BaseResponse> getJournals(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, 
			@PathVariable int organizationId,@PathVariable String organizationName , 
			@PathVariable int id ,  @PathVariable String module) {
		logger.info("Entry into getGeneralLedgers");
		BaseResponse response = new GeneralLedgerResponse();
		try {
			GeneralLedgerVo data = null;
			if(module!=null ) {
				data = 	 generalLedgerService.getJournalView(organizationId , id ,module);
			}
			setTokens(response, httpServletRequest.getHeader("keyToken"), httpServletRequest.getHeader("valueToken"));
			((GeneralLedgerResponse) response).setData(data);
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

}

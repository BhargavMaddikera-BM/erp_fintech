package com.blackstrawai.controller.vendorsettings;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
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
import com.blackstrawai.response.vendorsettings.RecordsCountResponse;
import com.blackstrawai.vendorsettings.RecordsSubmittedService;
import com.blackstrawai.vendorsettings.RecordsSubmittedVo;

@RestController
@CrossOrigin
@RequestMapping("/decifer/vendorportal")
public class RecordsSubmittedController extends BaseController {

	@Autowired
	RecordsSubmittedService recordsSubmittedService;

	private Logger logger = Logger.getLogger(RecordsSubmittedController.class);

	@RequestMapping(value = "/v1/records_count/{organizationId}")
	public ResponseEntity<BaseResponse> getCountOfRecordsSubmitted(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, @PathVariable int organizationId) {
		logger.info("Entry into getVendorAccessMgmtDropDown");
		BaseResponse response = new RecordsCountResponse();
		try {
			List<RecordsSubmittedVo> recordsSubmittedVo = recordsSubmittedService.getCountOfRecordsSubmitted(organizationId);
			setTokens(response, httpServletRequest.getHeader("keyToken"), httpServletRequest.getHeader("valueToken"));
			((RecordsCountResponse) response).setData(recordsSubmittedVo);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_RECORDS_COUNT_FETCH,
					Constants.SUCCESS_RECORDS_COUNT_FETCH, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);

		} catch (Exception e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_RECORDS_COUNT_FETCH,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

}

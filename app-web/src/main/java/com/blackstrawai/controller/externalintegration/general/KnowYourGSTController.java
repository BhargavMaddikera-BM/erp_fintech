package com.blackstrawai.controller.externalintegration.general;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blackstrawai.common.BaseController;
import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.common.Constants;
import com.blackstrawai.knowyourgst.KnowYourGST;
import com.blackstrawai.response.externalintegration.general.KnowYourGSTResponse;

@RestController
@CrossOrigin
@RequestMapping("/decifer/know_your_gst")
public class KnowYourGSTController extends BaseController {


	private Logger logger = Logger.getLogger(KnowYourGSTController.class);

	@RequestMapping(value = "/v1/{pan}")
	public ResponseEntity<BaseResponse> getGST(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse,@PathVariable String pan) {
		logger.info("Entry into method: getGST");
		BaseResponse response = new KnowYourGSTResponse();
		List<String> data = null;
		try {
			int i = 0;
			do {
				++i;
				data = KnowYourGST.getInstance().getGST(pan);
			} while (i < 3 && (data != null && data.size() == 0));


			setTokens(response, httpServletRequest.getHeader("keyToken"), httpServletRequest.getHeader("valueToken"));
			((KnowYourGSTResponse) response).setData(data);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_KNOW_YOUR_GST_FETCH,
					Constants.SUCCESS_KNOW_YOUR_GST_FETCH, Constants.SUCCESS_DURING_GET);
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (Exception e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_KNOW_YOUR_GST_FETCH,
					e.getMessage(), Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

}

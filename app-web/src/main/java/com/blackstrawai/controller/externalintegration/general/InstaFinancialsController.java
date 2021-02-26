package com.blackstrawai.controller.externalintegration.general;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.common.BaseController;
import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.common.CacheService;
import com.blackstrawai.common.Constants;
import com.blackstrawai.externalintegration.general.instafinancials.InstaFinancialsDirectorVo;
import com.blackstrawai.externalintegration.general.instafinancials.InstaFinancialsVo;
import com.blackstrawai.onboarding.OrganizationTypeService;
import com.blackstrawai.response.externalintegration.general.InstaFinancialsResponse;
import com.blackstrawai.roc.InstaFinancials;

@RestController
@CrossOrigin
@RequestMapping("/decifer/insta_finacials")
public class InstaFinancialsController extends BaseController{

	private Logger logger = Logger.getLogger(InstaFinancialsController.class);
	@Autowired
	CacheService cacheService;
	@Autowired
	OrganizationTypeService organizationTypeService;
	
	
	@RequestMapping(value = "/v1/insta_finacials/{cinNumber}", method = RequestMethod.GET)
	public ResponseEntity<BaseResponse> getCinSummary(
			HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse,@PathVariable String cinNumber) {
		logger.info("Entry into method: getCinSummary");
		BaseResponse response = new InstaFinancialsResponse();
		try {
		
			
			String data=InstaFinancials.getInstance().getByCin(cinNumber);
			JSONParser parser=new JSONParser();
			JSONObject json = (JSONObject) parser.parse(data);
			if(null!=json.get("Status") && "error".equals(json.get("Status"))){
				setTokens(response,httpServletRequest.getHeader("keyToken"),httpServletRequest.getHeader("valueToken"));
				((InstaFinancialsResponse) response).setData(null);
				response = constructResponse(response, Constants.FAILURE, Constants.INVALID_CIN,
						Constants.INVALID_CIN, Constants.FAILURE_DURING_GET);
			}else{


				cacheService.addCinNumber(cinNumber, data);
				InstaFinancialsVo instaFinancialsVo = convertInstaFiancialsVoFromJsonData(json);

				((InstaFinancialsResponse) response).setData(instaFinancialsVo);
				response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_INSTA_FINANCIALS_DETAILS_FETCH,
						Constants.SUCCESS_INSTA_FINANCIALS_DETAILS_FETCH, Constants.SUCCESS_DURING_GET);
			}
			
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (Exception e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_INSTA_FINANCIALS_FETCH, e.getMessage(),
					Constants.FAILURE_DURING_GET);
		logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}


	public InstaFinancialsVo convertInstaFiancialsVoFromJsonData(JSONObject json) throws ApplicationException {
		InstaFinancialsVo instaFinancialsVo=new InstaFinancialsVo();
		if(null!=json.get("InstaSummary")) {
			JSONObject instaSummary=(JSONObject)json.get("InstaSummary");
			JSONObject companyMasterSummary=instaSummary.get("CompanyMasterSummary")!=null?(JSONObject)instaSummary.get("CompanyMasterSummary"):null;						
			if(null!=companyMasterSummary) {
				instaFinancialsVo.setOrganizationName(companyMasterSummary.get("CompanyName")!=null?(String)companyMasterSummary.get("CompanyName"):null);
				instaFinancialsVo.setOrganizationType(companyMasterSummary.get("CompanyMcaIndustry")!=null?(String)companyMasterSummary.get("CompanyMcaIndustry"):null);
				instaFinancialsVo.setOrganizationTypeId(organizationTypeService.getOrganizationTypeByName(instaFinancialsVo.getOrganizationType()).getId());	
				instaFinancialsVo.setIndustryOfOrganization(companyMasterSummary.get("CompanyMcaIndustryDivision") != null?(String)companyMasterSummary.get("CompanyMcaIndustryDivision"):null);
				instaFinancialsVo.setIndustryOfOrganizationId(organizationTypeService.getOrganizationDivisionByName(instaFinancialsVo.getIndustryOfOrganization()).getId());
				instaFinancialsVo.setIndustryGroupOfOrganization(companyMasterSummary.get("CompanyMcaIndustryGroup") != null?(String) companyMasterSummary.get("CompanyMcaIndustryGroup"):null);
				instaFinancialsVo.setEmail(companyMasterSummary.get("CompanyEmail") != null?(String) companyMasterSummary.get("CompanyEmail"):null);
				instaFinancialsVo.setTaxId(companyMasterSummary.get("CompanyPan") != null?(String) companyMasterSummary.get("CompanyPan"):null);
				instaFinancialsVo.setRegisteredAddress(companyMasterSummary.get("CompanyBookAddress") != null?(String) companyMasterSummary.get("CompanyBookAddress"):null);
				instaFinancialsVo.setDateOfIncorporation(companyMasterSummary.get("CompanyDateOfInc") != null?(String) companyMasterSummary.get("CompanyDateOfInc"):null);
			}

			JSONObject directorSignatoryMasterSummary = instaSummary.get("DirectorSignatoryMasterSummary")!=null?(JSONObject) instaSummary.get("DirectorSignatoryMasterSummary"):null;
			if(null!=directorSignatoryMasterSummary) {
				JSONObject directorCurrentDirectorshipMasterSummary = directorSignatoryMasterSummary.get("DirectorCurrentMasterSummary") != null?(JSONObject) directorSignatoryMasterSummary.get("DirectorCurrentMasterSummary"):null;
				if (null!=directorCurrentDirectorshipMasterSummary && directorCurrentDirectorshipMasterSummary.get("Director") != null) {
					JSONArray directors = (JSONArray) directorCurrentDirectorshipMasterSummary.get("Director");
					List<InstaFinancialsDirectorVo> directorDetails=new ArrayList<InstaFinancialsDirectorVo>();
					for (Object obj : directors) {
						JSONObject directorJson=(JSONObject)obj;
						InstaFinancialsDirectorVo director=new InstaFinancialsDirectorVo();
						director.setDirectorDin(directorJson.get("DirectorDin")!=null?(String)directorJson.get("DirectorDin"):null);
						director.setDirectorName(directorJson.get("DirectorName")!=null?(String)directorJson.get("DirectorName"):null);
						director.setDirectorEmail(directorJson.get("DirectorEmail")!=null?(String)directorJson.get("DirectorEmail"):null);
						directorDetails.add(director);
					}
					System.out.println("Direcor Details:"+directorDetails);
					instaFinancialsVo.setDirectorDetails(directorDetails);
				}

			}

		}
		return instaFinancialsVo;
	}
}

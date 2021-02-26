package com.blackstrawai.helper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.blackstrawai.externalintegration.compliance.employeeprovidentfund.EmployeeProvidentFundLoginVo;
import com.blackstrawai.externalintegration.compliance.taxilla.B2BInvoiceInvItmDetVo;
import com.blackstrawai.externalintegration.compliance.taxilla.B2BInvoiceInvItmVo;
import com.blackstrawai.externalintegration.compliance.taxilla.B2BInvoiceInvVo;
import com.blackstrawai.externalintegration.compliance.taxilla.B2BInvoiceVo;
import com.blackstrawai.externalintegration.compliance.taxilla.B2clInvoiceVo;
import com.blackstrawai.externalintegration.compliance.taxilla.B2csInvoiceVo;
import com.blackstrawai.externalintegration.compliance.taxilla.CdnInvoiceNtVo;
import com.blackstrawai.externalintegration.compliance.taxilla.CdnInvoiceVo;
import com.blackstrawai.externalintegration.compliance.taxilla.NilRatedSuppliesInvVo;
import com.blackstrawai.externalintegration.compliance.taxilla.NilRatedSuppliesVo;
import com.blackstrawai.externalintegration.compliance.taxilla.TaxillaAddr;
import com.blackstrawai.externalintegration.compliance.taxilla.TaxillaPrAdrVo;
import com.blackstrawai.externalintegration.compliance.taxilla.TaxillaVo;
import com.blackstrawai.externalintegration.general.aadhaar.AadhaarBankAdvanceVo;
import com.blackstrawai.externalintegration.general.aadhaar.AadhaarBankBasicVo;
import com.blackstrawai.externalintegration.general.aadhaar.AadhaarGstnAdditionalPlacesVo;
import com.blackstrawai.externalintegration.general.aadhaar.AadhaarGstnAdvContactDetailsVo;
import com.blackstrawai.externalintegration.general.aadhaar.AadhaarGstnAdvanceVo;
import com.blackstrawai.externalintegration.general.aadhaar.AadhaarGstnBasicVo;
import com.blackstrawai.externalintegration.general.aadhaar.AadhaarGstnContactInfoAddressBasicVo;
import com.blackstrawai.externalintegration.general.aadhaar.AadhaarGstnContactInfoAdvanceVo;
import com.blackstrawai.externalintegration.general.aadhaar.AadhaarGstnContactInfoBasicVo;
import com.blackstrawai.externalintegration.general.aadhaar.AadhaarPanAdvanceVo;
import com.blackstrawai.externalintegration.general.aadhaar.AadhaarPanBasicVo;
import com.blackstrawai.request.externalintegration.compliance.EmployeeProvidentFundRequest;

public class ExternalIntegrationConvertToVoHelper {

	private static ExternalIntegrationConvertToVoHelper externalIntegrationConvertToVoHelper;

	public static ExternalIntegrationConvertToVoHelper getInstance() {
		if (externalIntegrationConvertToVoHelper == null) {
			externalIntegrationConvertToVoHelper = new ExternalIntegrationConvertToVoHelper();
		}
		return externalIntegrationConvertToVoHelper;
	}

	public AadhaarPanBasicVo convertAadhaarPanBasicVoFromJsonData(JSONObject data) {
		AadhaarPanBasicVo aadharPanVo = new AadhaarPanBasicVo();
		aadharPanVo.setPanNumber(data.get("pan_number") != null ? data.get("pan_number").toString() : null);
		aadharPanVo.setPanStatus(data.get("pan_status") != null ? data.get("pan_status").toString() : null);
		aadharPanVo.setFullName(data.get("name") != null ? data.get("name").toString() : null);
		return aadharPanVo;
	}

	public AadhaarBankBasicVo convertAadhaarBankBasicVoFromJsonData(JSONObject data) {
		AadhaarBankBasicVo aadharBankVo = new AadhaarBankBasicVo();
		aadharBankVo.setStatus(data.get("Status") != null ? data.get("Status").toString() : null);
		aadharBankVo.setBeneficiaryName(data.get("BeneName") != null ? data.get("BeneName").toString() : null);
		aadharBankVo.setRemark(data.get("Remark") != null ? data.get("Remark").toString() : null);
		aadharBankVo.setBankRef(data.get("BankRef") != null ? data.get("BankRef").toString() : null);
		return aadharBankVo;
	}

	@SuppressWarnings("unchecked")
	public AadhaarGstnBasicVo convertAadhaarGstnBasicVoFromJsonData(JSONObject data) {
		AadhaarGstnBasicVo aadharGstnBasicVo = new AadhaarGstnBasicVo();

		aadharGstnBasicVo.setStateJursdiction(data.get("stj") != null ? data.get("stj").toString() : null);
		aadharGstnBasicVo.setLegalName(data.get("lgnm") != null ? data.get("lgnm").toString() : null);
		aadharGstnBasicVo.setStateJurisdictionCode(data.get("stjCd") != null ? data.get("stjCd").toString() : null);
		aadharGstnBasicVo.setTaxpayerType(data.get("dty") != null ? data.get("dty").toString() : null);
		// Processing Additional places of Business In state
		if (data.get("adadr") != null) {

			JSONArray additionalPalcesBusinsInStateArray = (JSONArray) data.get("adadr");
			if (additionalPalcesBusinsInStateArray != null) {
				Iterator<JSONObject> additionalPalcesBusinsInStateIterator = additionalPalcesBusinsInStateArray
						.iterator();
				List<AadhaarGstnAdditionalPlacesVo> additionalPalcesBusinsInState = new ArrayList<AadhaarGstnAdditionalPlacesVo>();
				while (additionalPalcesBusinsInStateIterator.hasNext()) {

					JSONObject additionPlace = (JSONObject) additionalPalcesBusinsInStateIterator.next();
					AadhaarGstnAdditionalPlacesVo place = new AadhaarGstnAdditionalPlacesVo();
					place.setAddress(additionPlace.get("adr") != null ? additionPlace.get("adr").toString() : null);
					place.setMobileNumber(additionPlace.get("mb") != null ? additionPlace.get("mb").toString() : null);
					place.setNatureOfBusiness(
							additionPlace.get("ntr") != null ? additionPlace.get("ntr").toString() : null);
					additionalPalcesBusinsInState.add(place);
				}
				aadharGstnBasicVo.setAddiPlacesBusnsInStateReg(additionalPalcesBusinsInState);
			}
		}
		aadharGstnBasicVo.setDateOfCancelofReg(data.get("cxdt") != null ? data.get("cxdt").toString() : null);
		aadharGstnBasicVo.setGstin(data.get("gstin") != null ? data.get("gstin").toString() : null);
		// Processing Nature of business
		if (data.get("nba") != null) {
			JSONArray natureOfBusinessArray = (JSONArray) data.get("nba");
			if (natureOfBusinessArray != null) {
				Iterator<String> natureOfBusinessIterator = natureOfBusinessArray.iterator();
				List<String> natureOfBusiness = new ArrayList<String>();
				while (natureOfBusinessIterator.hasNext()) {
					natureOfBusiness.add(natureOfBusinessIterator.next());
				}
				aadharGstnBasicVo.setNatureOfBusiness(natureOfBusiness);
			}
		}
		aadharGstnBasicVo.setLastupdated(data.get("lstupdt") != null ? data.get("lstupdt").toString() : null);
		aadharGstnBasicVo.setRegistrationDate(data.get("rgdt") != null ? data.get("rgdt").toString() : null);
		aadharGstnBasicVo.setConstOfBusiness(data.get("ctb") != null ? data.get("ctb").toString() : null);
		// Processing Primary Contact info for business address
		if (data.get("pradr") != null) {

			JSONObject primaryObject = (JSONObject) data.get("pradr");
			AadhaarGstnContactInfoBasicVo contactInfo = new AadhaarGstnContactInfoBasicVo();
			contactInfo.setNatureOfBusnsCarriedAtAddrress(
					primaryObject.get("ntr") != null ? primaryObject.get("ntr").toString() : null);
			if (primaryObject.get("addr") != null) {
				JSONObject addressObject = (JSONObject) primaryObject.get("addr");
				AadhaarGstnContactInfoAddressBasicVo address = new AadhaarGstnContactInfoAddressBasicVo();
				address.setBuidingName(addressObject.get("bnm") != null ? addressObject.get("bnm").toString() : null);
				address.setBuildingNumber(
						addressObject.get("bno") != null ? addressObject.get("bno").toString() : null);
				address.setCity(addressObject.get("city") != null ? addressObject.get("city").toString() : null);
				address.setDistrict(addressObject.get("dst") != null ? addressObject.get("dst").toString() : null);
				address.setFloorNo(addressObject.get("flno") != null ? addressObject.get("flno").toString() : null);
				address.setLg(addressObject.get("lg") != null ? addressObject.get("lg").toString() : null);
				address.setLocation(addressObject.get("loc") != null ? addressObject.get("loc").toString() : null);
				address.setLt(addressObject.get("lt") != null ? addressObject.get("lt").toString() : null);
				address.setPinCode(addressObject.get("pncd") != null ? addressObject.get("pncd").toString() : null);
				address.setStateCode(addressObject.get("stcd") != null ? addressObject.get("stcd").toString() : null);
				address.setStreet(addressObject.get("st") != null ? addressObject.get("st").toString() : null);

				contactInfo.setContactInfoAddress(address);
			}
			aadharGstnBasicVo.setPrimaryBusinessContactInfo(contactInfo);
		}

		aadharGstnBasicVo.setTradeName(data.get("tradeNam") != null ? data.get("tradeNam").toString() : null);
		aadharGstnBasicVo.setCurrentStatus(data.get("sts") != null ? data.get("sts").toString() : null);
		aadharGstnBasicVo.setCentralJurisdictionCode(data.get("ctjCd") != null ? data.get("ctjCd").toString() : null);
		aadharGstnBasicVo.setCentralJurisdiction(data.get("ctj") != null ? data.get("ctj").toString() : null);
		return aadharGstnBasicVo;
	}

	@SuppressWarnings("unchecked")
	public AadhaarGstnAdvanceVo convertAadhaarGstnAdvanceVoFromJsonData(JSONObject data) {
		AadhaarGstnAdvanceVo gstnAdvanceVo = new AadhaarGstnAdvanceVo();
		if (data.get("adadr") != null) {
			JSONArray additionalPalcesBusinsInStateArray = (JSONArray) data.get("adadr");
			if (additionalPalcesBusinsInStateArray != null) {
				Iterator<JSONObject> additionalPalcesBusinsInStateIterator = additionalPalcesBusinsInStateArray
						.iterator();
				List<AadhaarGstnAdditionalPlacesVo> additionalPalcesBusinsInState = new ArrayList<AadhaarGstnAdditionalPlacesVo>();
				while (additionalPalcesBusinsInStateIterator.hasNext()) {

					JSONObject additionPlace = (JSONObject) additionalPalcesBusinsInStateIterator.next();
					AadhaarGstnAdditionalPlacesVo place = new AadhaarGstnAdditionalPlacesVo();
					place.setAddress(additionPlace.get("adr") != null ? additionPlace.get("adr").toString() : null);
					place.setMobileNumber(additionPlace.get("mb") != null ? additionPlace.get("mb").toString() : null);
					place.setNatureOfBusiness(
							additionPlace.get("ntr") != null ? additionPlace.get("ntr").toString() : null);
					additionalPalcesBusinsInState.add(place);
				}
				gstnAdvanceVo.setAdditionalPlacesOfBusnessInState(additionalPalcesBusinsInState);
			}
		}

		gstnAdvanceVo.setCanFlag(data.get("canFlag") != null ? data.get("canFlag").toString() : null);
		gstnAdvanceVo.setCentralJurisdiction(data.get("ctj") != null ? data.get("ctj").toString() : null);
		gstnAdvanceVo.setCentralJurisdictionCode(data.get("ctjCd") != null ? data.get("ctjCd").toString() : null);
		gstnAdvanceVo.setComplianceRatingOFBusiness(data.get("cmpRt") != null ? data.get("cmpRt").toString() : null);
		gstnAdvanceVo.setConstOfBusiness(data.get("ctb") != null ? data.get("ctb").toString() : null);

		if (data.get("contacted") != null) {

			JSONObject contactObj = (JSONObject) data.get("contacted");
			AadhaarGstnAdvContactDetailsVo contact = new AadhaarGstnAdvContactDetailsVo();
			contact.setEmail(contactObj.get("email") != null ? contactObj.get("email").toString() : null);
			contact.setMobileNumber(contactObj.get("mobNum") != null ? contactObj.get("mobNum").toString() : null);
			contact.setName(contactObj.get("name") != null ? contactObj.get("name").toString() : null);

			gstnAdvanceVo.setContactDetails(contact);
		}

		gstnAdvanceVo.setCurrentStatus(data.get("sts") != null ? data.get("sts").toString() : null);
		gstnAdvanceVo.setDateOfCancelofReg(data.get("cxdt") != null ? data.get("cxdt").toString() : null);
		gstnAdvanceVo.setGstin(data.get("gstin") != null ? data.get("gstin").toString() : null);
		gstnAdvanceVo.setLastupdated(data.get("lstupdt") != null ? data.get("lstupdt").toString() : null);
		gstnAdvanceVo.setLegalName(data.get("lgnm") != null ? data.get("lgnm").toString() : null);
		// Processing mbr:
		if (data.get("mbr") != null) {
			JSONArray listOFAuthSignOFBusArray = (JSONArray) data.get("mbr");
			if (listOFAuthSignOFBusArray != null) {
				Iterator<String> listOFAuthSignOFBusIterator = listOFAuthSignOFBusArray.iterator();
				List<String> listOFAuthSignOFBus = new ArrayList<String>();
				while (listOFAuthSignOFBusIterator.hasNext()) {
					listOFAuthSignOFBus.add(listOFAuthSignOFBusIterator.next());
				}
				gstnAdvanceVo.setListOFAuthorizedSignOfBusiness(listOFAuthSignOFBus);
			}
		}

		// Processing Nature of business
		if (data.get("nba") != null) {
			JSONArray natureOfBusinessArray = (JSONArray) data.get("nba");
			if (natureOfBusinessArray != null) {
				Iterator<String> natureOfBusinessIterator = natureOfBusinessArray.iterator();
				List<String> natureOfBusiness = new ArrayList<String>();
				while (natureOfBusinessIterator.hasNext()) {
					natureOfBusiness.add(natureOfBusinessIterator.next());
				}
				gstnAdvanceVo.setNatureOfBusiness(natureOfBusiness);
			}
		}

		gstnAdvanceVo.setPpr(data.get("ppr") != null ? data.get("ppr").toString() : null);

		// Processing Primary Contact info for business address
		if (data.get("pradr") != null) {

			JSONObject primaryObject = (JSONObject) data.get("pradr");
			AadhaarGstnContactInfoAdvanceVo contactInfo = new AadhaarGstnContactInfoAdvanceVo();
			contactInfo.setLastUpdatedDate(
					primaryObject.get("lastUpdatedDate") != null ? primaryObject.get("lastUpdatedDate").toString()
							: null);
			contactInfo.setNatureOfBusnsCarriedAtAddrress(
					primaryObject.get("ntr") != null ? primaryObject.get("ntr").toString() : null);
			contactInfo.setPrimaryRegisteredAddress(
					primaryObject.get("adr") != null ? primaryObject.get("adr").toString() : null);
			contactInfo.setPrimaryRegisteredAddressLine2(
					primaryObject.get("addr") != null ? primaryObject.get("addr").toString() : null);
			contactInfo.setRegisteredEmail(primaryObject.get("em") != null ? primaryObject.get("em").toString() : null);
			contactInfo.setRegisteredMobileNumber(
					primaryObject.get("mb") != null ? primaryObject.get("mb").toString() : null);
			gstnAdvanceVo.setPrimaryBusinessContactInfo(contactInfo);
		}

		gstnAdvanceVo.setRegistrationDate(data.get("rgdt") != null ? data.get("rgdt").toString() : null);
		gstnAdvanceVo.setStateJurisdiction(data.get("stj") != null ? data.get("stj").toString() : null);
		gstnAdvanceVo.setStateJurisdictionCode(data.get("stjCd") != null ? data.get("stjCd").toString() : null);
		gstnAdvanceVo.setTaxpayerType(data.get("dty") != null ? data.get("dty").toString() : null);
		gstnAdvanceVo.setTradeName(data.get("tradeNam") != null ? data.get("tradeNam").toString() : null);
		return gstnAdvanceVo;
	}

	public AadhaarPanAdvanceVo convertAadhaarPanAdvanceVoFromJsonData(JSONObject dataAdvanced) {
		AadhaarPanAdvanceVo aadharPanAdvancedVo = new AadhaarPanAdvanceVo();
		aadharPanAdvancedVo.setPanNumber(
				dataAdvanced.get("pan_number") != null ? dataAdvanced.get("pan_number").toString() : null);
		aadharPanAdvancedVo.setPanStatus(
				dataAdvanced.get("pan_status") != null ? dataAdvanced.get("pan_status").toString() : null);
		aadharPanAdvancedVo.setFirstName(
				dataAdvanced.get("first_name") != null ? dataAdvanced.get("first_name").toString() : null);
		aadharPanAdvancedVo
				.setLastName(dataAdvanced.get("last_name") != null ? dataAdvanced.get("last_name").toString() : null);
		aadharPanAdvancedVo.setPanHolderTitle(
				dataAdvanced.get("pan_holder_title") != null ? dataAdvanced.get("pan_holder_title").toString() : null);
		aadharPanAdvancedVo.setPanLastUpdatedDate(
				dataAdvanced.get("pan_last_updated") != null ? dataAdvanced.get("pan_last_updated").toString() : null);
		return aadharPanAdvancedVo;
	}

	public AadhaarBankAdvanceVo convertAadhaarBankAdvanceVoFromJsonData(JSONObject data) {
		AadhaarBankAdvanceVo aadharBankVo = new AadhaarBankAdvanceVo();
		aadharBankVo.setStatus(data.get("Status") != null ? data.get("Status").toString() : null);
		aadharBankVo.setBeneficiaryName(data.get("BeneName") != null ? data.get("BeneName").toString() : null);
		aadharBankVo.setRemark(data.get("Remark") != null ? data.get("Remark").toString() : null);
		aadharBankVo.setBankRef(data.get("BankRef") != null ? data.get("BankRef").toString() : null);
		aadharBankVo.setAmount(data.get("Amount") != null ? data.get("Amount").toString() : null);
		return aadharBankVo;
	}

	@SuppressWarnings("unchecked")
	public TaxillaVo convertTaxillaFromJson(JSONObject gstJson) {
		TaxillaVo taxillaVo = new TaxillaVo();
		if (gstJson.get("result") != null) {
			gstJson = (JSONObject) gstJson.get("result");
			taxillaVo.setAdadr(gstJson.get("adadr") != null ? (List<String>) gstJson.get("adadr") : null);
			taxillaVo.setCtb(gstJson.get("ctb") != null ? (String) gstJson.get("ctb") : null);
			taxillaVo.setCtj(gstJson.get("ctj") != null ? (String) gstJson.get("ctj") : null);
			taxillaVo.setCtjCd(gstJson.get("ctjCd") != null ? (String) gstJson.get("ctjCd") : null);
			taxillaVo.setCxdt(gstJson.get("cxdt") != null ? (String) gstJson.get("cxdt") : null);
			taxillaVo.setDty(gstJson.get("dty") != null ? (String) gstJson.get("dty") : null);
			taxillaVo.setGstin(gstJson.get("gstin") != null ? (String) gstJson.get("gstin") : null);
			taxillaVo.setLgnm(gstJson.get("lgnm") != null ? (String) gstJson.get("lgnm") : null);
			taxillaVo.setLstupdt(gstJson.get("lstupdt") != null ? (String) gstJson.get("lstupdt") : null);
			taxillaVo.setNba(gstJson.get("nba") != null ? (List<String>) gstJson.get("nba") : null);

			taxillaVo.setPradr(
					gstJson.get("pradr") != null ? convertTaxillaPrAdrFromJson((JSONObject) gstJson.get("pradr"))
							: null);
			taxillaVo.setRgdt(gstJson.get("rgdt") != null ? (String) gstJson.get("rgdt") : null);
			taxillaVo.setStj(gstJson.get("stj") != null ? (String) gstJson.get("stj") : null);
			taxillaVo.setStjCd(gstJson.get("stjCd") != null ? (String) gstJson.get("stjCd") : null);
			taxillaVo.setSts(gstJson.get("sts") != null ? (String) gstJson.get("sts") : null);
			taxillaVo.setTradeNam(gstJson.get("tradeNam") != null ? (String) gstJson.get("tradeNam") : null);

		}
		return taxillaVo;
	}

	public TaxillaPrAdrVo convertTaxillaPrAdrFromJson(JSONObject pradrJson) {
		TaxillaPrAdrVo pradr = new TaxillaPrAdrVo();
		if (pradrJson.get("addr") != null) {
			TaxillaAddr addr = new TaxillaAddr();
			JSONObject addrJson = (JSONObject) pradrJson.get("addr");
			addr.setBnm(addrJson.get("bnm") != null ? (String) addrJson.get("bnm") : null);
			addr.setBno(addrJson.get("bno") != null ? (String) addrJson.get("bno") : null);
			addr.setCity(addrJson.get("city") != null ? (String) addrJson.get("city") : null);
			addr.setDst(addrJson.get("dst") != null ? (String) addrJson.get("dst") : null);
			addr.setFlno(addrJson.get("flno") != null ? (String) addrJson.get("flno") : null);
			addr.setLg(addrJson.get("lg") != null ? (String) addrJson.get("lg") : null);
			addr.setLoc(addrJson.get("loc") != null ? (String) addrJson.get("loc") : null);
			addr.setLt(addrJson.get("lt") != null ? (String) addrJson.get("lt") : null);
			addr.setPncd(addrJson.get("pncd") != null ? (String) addrJson.get("pncd") : null);
			addr.setSt(addrJson.get("st") != null ? (String) addrJson.get("st") : null);
			addr.setStcd(addrJson.get("stcd") != null ? (String) addrJson.get("stcd") : null);
			pradr.setAddr(addr);
		}
		pradr.setNtr(pradrJson.get("ntr") != null ? (String) pradrJson.get("ntr") : null);
		return pradr;
	}

	public List<B2BInvoiceVo> convertB2bInvoicesFromJson(JSONObject taxillaJson, String action) throws ParseException {
		List<B2BInvoiceVo> b2bList = new ArrayList<B2BInvoiceVo>();
		if (taxillaJson != null && taxillaJson.get(action) != null) {
			JSONParser parser = new JSONParser();
			JSONArray b2bArray = (JSONArray) parser.parse(taxillaJson.get(action).toString());

			for (Object obj : b2bArray) {
				JSONObject json = (JSONObject) obj;
				B2BInvoiceVo b2b = new B2BInvoiceVo();
				b2b.setCtin(json.get("ctin") != null ? (String) json.get("ctin") : null);
				b2b.setCfs(json.get("cfs") != null ? (String) json.get("cfs") : null);
				b2b.setInv(json.get("inv") != null
						? convertB2bInvoicesInvFromJson((JSONArray) parser.parse(json.get("inv").toString()))
						: null);
				b2bList.add(b2b);
			}
		}
		return b2bList;
	}

	private List<B2BInvoiceInvVo> convertB2bInvoicesInvFromJson(JSONArray taxillaJson) throws ParseException {
		List<B2BInvoiceInvVo> b2bList = new ArrayList<B2BInvoiceInvVo>();
		if (taxillaJson != null) {
			JSONParser parser = new JSONParser();

			for (Object obj : taxillaJson) {
				JSONObject json = (JSONObject) obj;
				B2BInvoiceInvVo inv = new B2BInvoiceInvVo();
				inv.setChksum(json.get("chksum") != null ? (String) json.get("chksum") : null);
				inv.setUpdby(json.get("updby") != null ? (String) json.get("updby") : null);
				inv.setInum(json.get("inum") != null ? (String) json.get("inum") : null);
				inv.setIdt(json.get("idt") != null ? (String) json.get("idt") : null);
				inv.setVal(json.get("val") != null ? (double) json.get("val") : null);
				inv.setPos(json.get("pos") != null ? (String) json.get("pos") : null);
				inv.setRchrg(json.get("rchrg") != null ? (String) json.get("rchrg") : null);
				inv.setEtin(json.get("etin") != null ? (String) json.get("etin") : null);
				inv.setInvTyp(json.get("inv_typ") != null ? (String) json.get("inv_typ") : null);
				inv.setCflag(json.get("cflag") != null ? (String) json.get("cflag") : null);
				inv.setDiffPercent(json.get("diff_percent") != null ? (double) json.get("diff_percent") : null);
				inv.setOpd(json.get("opd") != null ? (String) json.get("opd") : null);
				inv.setItms(json.get("itms") != null
						? convertB2bInvoicesInvItmsFromJson((JSONArray) parser.parse(json.get("itms").toString()))
						: null);
				inv.setOinum(json.get("oinum") != null ? (String) json.get("oinum") : null);
				inv.setOidt(json.get("oidt") != null ? (String) json.get("oidt") : null);
				inv.setFlag(json.get("flag") != null ? (String) json.get("flag") : null);

				b2bList.add(inv);
			}
		}
		return b2bList;

	}

	private List<B2BInvoiceInvItmVo> convertB2bInvoicesInvItmsFromJson(JSONArray taxillaJson) {
		List<B2BInvoiceInvItmVo> b2bList = new ArrayList<B2BInvoiceInvItmVo>();
		if (taxillaJson != null) {
			for (Object obj : taxillaJson) {
				JSONObject json = (JSONObject) obj;
				B2BInvoiceInvItmVo itm = new B2BInvoiceInvItmVo();
				itm.setNum(json.get("num") != null ? (Integer) json.get("num") : null);
				itm.setItmDet(json.get("itm_det") != null
						? convertB2bInvoicesInvItmDetFromJson((JSONObject) json.get("itm_det"))
						: null);
				b2bList.add(itm);
			}
		}
		return b2bList;
	}

	private B2BInvoiceInvItmDetVo convertB2bInvoicesInvItmDetFromJson(JSONObject json) {
		B2BInvoiceInvItmDetVo det = new B2BInvoiceInvItmDetVo();
		det.setRt(json.get("rt") != null ? (Integer) json.get("rt") : null);
		det.setTxval(json.get("txval") != null ? (Integer) json.get("txval") : null);
		det.setIamt(json.get("iamt") != null ? (Integer) json.get("iamt") : null);
		det.setCamt(json.get("camt") != null ? (Integer) json.get("camt") : null);
		det.setSamt(json.get("samt") != null ? (Integer) json.get("samt") : null);
		det.setCsamt(json.get("csamt") != null ? (Integer) json.get("csamt") : null);
		return det;
	}

	public List<B2clInvoiceVo> convertB2clInvoicesFromJson(JSONObject taxillaJson, String action)
			throws ParseException {

		List<B2clInvoiceVo> b2clList = new ArrayList<B2clInvoiceVo>();
		if (taxillaJson != null && taxillaJson.get(action) != null) {
			JSONParser parser = new JSONParser();
			JSONArray b2clArray = (JSONArray) parser.parse(taxillaJson.get(action).toString());

			for (Object obj : b2clArray) {
				JSONObject json = (JSONObject) obj;
				B2clInvoiceVo b2cl = new B2clInvoiceVo();
				b2cl.setPos(json.get("pos") != null ? (String) json.get("pos") : null);
				b2cl.setInv(json.get("inv") != null
						? convertB2bInvoicesInvFromJson((JSONArray) parser.parse(json.get("inv").toString()))
						: null);
				b2clList.add(b2cl);
			}
		}
		return b2clList;

	}

	public List<B2csInvoiceVo> convertB2csInvoicesFromJson(JSONObject taxillaJson, String action)
			throws ParseException {

		List<B2csInvoiceVo> b2csList = new ArrayList<B2csInvoiceVo>();
		if (taxillaJson != null && taxillaJson.get(action) != null) {
			JSONParser parser = new JSONParser();
			JSONArray b2csArray = (JSONArray) parser.parse(taxillaJson.get(action).toString());

			for (Object obj : b2csArray) {
				JSONObject json = (JSONObject) obj;
				B2csInvoiceVo inv = new B2csInvoiceVo();
				inv.setChksum(json.get("chksum") != null ? (String) json.get("chksum") : null);
				inv.setPos(json.get("pos") != null ? (String) json.get("pos") : null);
				inv.setEtin(json.get("etin") != null ? (String) json.get("etin") : null);
				inv.setDiffPercent(json.get("diff_percent") != null ? (double) json.get("diff_percent") : null);
				inv.setFlag(json.get("flag") != null ? (String) json.get("flag") : null);
				inv.setSplyTy(json.get("sply_ty") != null ? (String) json.get("sply_ty") : null);
				inv.setRt(json.get("sply_ty") != null ? (Integer) json.get("sply_ty") : null);
				inv.setTyp(json.get("typ") != null ? (String) json.get("typ") : null);
				inv.setTxval(json.get("txval") != null ? (Integer) json.get("txval") : null);
				inv.setIamt(json.get("iamt") != null ? (Integer) json.get("iamt") : null);
				inv.setCsamt(json.get("csamt") != null ? (Integer) json.get("csamt") : null);

				b2csList.add(inv);
			}
		}
		return b2csList;

	}

	public List<CdnInvoiceVo> convertCdnInvoicesFromJson(JSONObject taxillaJson, String action) throws ParseException {
		List<CdnInvoiceVo> cdnList = new ArrayList<CdnInvoiceVo>();
		if (taxillaJson != null && taxillaJson.get(action) != null) {
			JSONParser parser = new JSONParser();
			JSONArray cdnArray = (JSONArray) parser.parse(taxillaJson.get(action).toString());

			for (Object obj : cdnArray) {
				JSONObject json = (JSONObject) obj;
				CdnInvoiceVo cdn = new CdnInvoiceVo();
				cdn.setCtin(json.get("ctin") != null ? (String) json.get("ctin") : null);
				cdn.setCfs(json.get("cfs") != null ? (String) json.get("cfs") : null);
				cdn.setNt(json.get("nt") != null
						? convertCdnInvoicesNtFromJson((JSONArray) parser.parse(json.get("nt").toString()))
						: null);
				cdnList.add(cdn);
			}
		}
		return cdnList;
	}

	private List<CdnInvoiceNtVo> convertCdnInvoicesNtFromJson(JSONArray taxillaJson) throws ParseException {
		List<CdnInvoiceNtVo> ntList = new ArrayList<CdnInvoiceNtVo>();
		if (taxillaJson != null) {
			JSONParser parser = new JSONParser();

			for (Object obj : taxillaJson) {
				JSONObject json = (JSONObject) obj;
				CdnInvoiceNtVo nt = new CdnInvoiceNtVo();
				nt.setChksum(json.get("chksum") != null ? (String) json.get("chksum") : null);
				nt.setUpdby(json.get("updby") != null ? (String) json.get("updby") : null);
				nt.setInum(json.get("inum") != null ? (String) json.get("inum") : null);
				nt.setIdt(json.get("idt") != null ? (String) json.get("idt") : null);
				nt.setVal(json.get("val") != null ? (double) json.get("val") : null);
				nt.setPos(json.get("pos") != null ? (String) json.get("pos") : null);
				nt.setRchrg(json.get("rchrg") != null ? (String) json.get("rchrg") : null);
				nt.setInvTyp(json.get("inv_typ") != null ? (String) json.get("inv_typ") : null);
				nt.setCflag(json.get("cflag") != null ? (String) json.get("cflag") : null);
				nt.setOpd(json.get("opd") != null ? (String) json.get("opd") : null);
				nt.setItms(json.get("itms") != null
						? convertB2bInvoicesInvItmsFromJson((JSONArray) parser.parse(json.get("itms").toString()))
						: null);
				nt.setFlag(json.get("flag") != null ? (String) json.get("flag") : null);
				nt.setNtty(json.get("ntty") != null ? (String) json.get("ntty") : null);
				nt.setNtNum(json.get("nt_num") != null ? (String) json.get("nt_num") : null);
				nt.setNtDt(json.get("nt_dt") != null ? (String) json.get("nt_dt") : null);
				nt.setpGst(json.get("p_gst") != null ? (String) json.get("p_gst") : null);
				nt.setdFlag(json.get("d_flag") != null ? (String) json.get("d_flag") : null);
				nt.setOntNum(json.get("ont_num") != null ? (String) json.get("ont_num") : null);
				nt.setOntDt(json.get("ont_dt") != null ? (String) json.get("ont_dt") : null);

				ntList.add(nt);
			}
		}
		return ntList;

	}

	public NilRatedSuppliesVo convertNilRatedSuppliesFromJson(JSONObject taxillaJson, String action) throws ParseException {
		NilRatedSuppliesVo nil = new NilRatedSuppliesVo();
		if (taxillaJson != null && taxillaJson.get(action) != null) {
			JSONParser parser = new JSONParser();
			taxillaJson = (JSONObject) parser.parse(taxillaJson.get("nil").toString());
			nil.setFlag(taxillaJson.get("flag") != null ? (String) taxillaJson.get("flag") : null);
			nil.setChksum(taxillaJson.get("chksum") != null ? (String) taxillaJson.get("chksum") : null);
			nil.setInv(taxillaJson.get("inv") != null
					? convertNilRatedSuppliesInvFromJson((JSONArray) parser.parse(taxillaJson.get("inv").toString()))
					: null);
		}
		return nil;
	}

	private List<NilRatedSuppliesInvVo> convertNilRatedSuppliesInvFromJson(JSONArray invArr) {

		List<NilRatedSuppliesInvVo> invList = new ArrayList<NilRatedSuppliesInvVo>();
		if (invArr != null) {
			for (Object obj : invArr) {
				JSONObject json = (JSONObject) obj;
				NilRatedSuppliesInvVo inv = new NilRatedSuppliesInvVo();
				inv.setSplyTy(json.get("sply_ty") != null ? (String) json.get("sply_ty") : null);
				inv.setExptAmt(json.get("expt_amt") != null ? (Double) json.get("expt_amt") : null);
				inv.setNilAmt(json.get("nil_amt") != null ? (Double) json.get("nil_amt") : null);
				inv.setNgsupAmt(json.get("ngsup_amt") != null ? (Double) json.get("ngsup_amt") : null);
				invList.add(inv);
			}
		}
		return invList;

	
	}

	public EmployeeProvidentFundLoginVo convertEpfLoginRequestToVo(EmployeeProvidentFundRequest data) {
		EmployeeProvidentFundLoginVo loginVo = new EmployeeProvidentFundLoginVo();
		loginVo.setId(data.getId());
		loginVo.setOrganizationId(data.getOrganizationId());
		loginVo.setUserId(data.getUserId());
		loginVo.setLoginName(data.getLoginName());
		loginVo.setLoginPassword(data.getLoginPassword());
		loginVo.setRoleName(data.getRoleName());
		loginVo.setRememberMe(data.isRememberMe());
		return loginVo;
	}

}

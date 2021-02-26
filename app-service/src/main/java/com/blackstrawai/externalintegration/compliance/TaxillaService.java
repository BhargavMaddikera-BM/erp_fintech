package com.blackstrawai.externalintegration.compliance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.common.BaseService;
import com.blackstrawai.externalintegration.compliance.taxilla.TaxillaGsonVo;
import com.blackstrawai.taxilla.Taxilla;

/**
 * Service layer for Taxilla
 * 
 * @author adityabharadwaj
 *
 */
@Service
public class TaxillaService extends BaseService {
	@Autowired
	private TaxillaDao taxillaDao;

	/**
	 * Get OTP by orgId, userId and roleName
	 * 
	 * @param organizationId
	 * @param userId
	 * @param roleName
	 * @return OTP
	 * @throws ApplicationException
	 */
	public String getOtp(int organizationId, int userId, String roleName) throws ApplicationException {
		return taxillaDao.getOtp(organizationId, userId, roleName);
	}

	public void setOtp(int organizationId, String userId, String roleName, String otp) throws ApplicationException {
		taxillaDao.setOtp(organizationId, userId, roleName, otp);
	}
	
	
	/**
	 * Gets the address details by gst number from taxilla.
	 *
	 * @param gst the gst
	 * @return the address details by gst number from taxilla
	 */
	public org.json.simple.JSONObject getAddressDetailsByGstNumberFromTaxilla(String gst) {
		try {
			return Taxilla.getInstance().getByGstNo(gst);
		} catch (ApplicationException e) {
			return null;
		}
	}
	
	
	public TaxillaGsonVo getByGst(String gst) {
		try {
			return Taxilla.getInstance().getByGst(gst);
		} catch (ApplicationException e) {
			return null;
		}
	}

}

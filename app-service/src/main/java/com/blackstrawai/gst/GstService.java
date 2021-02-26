package com.blackstrawai.gst;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blackstrawai.ApplicationException;

@Service
public class GstService {

	@Autowired
	private GstDao gstDao;

	private Logger logger = Logger.getLogger(GstService.class);

	public void createGstForPan(PanGstVo panGstVo) throws ApplicationException {
		logger.trace("Entry into method : createGstForPan");
		gstDao.addGstNumberForPan(panGstVo);
	}

	public List<String> getGstFromPanNumber(String panNO, Integer orgId) throws ApplicationException {
		logger.trace("Entry into method: getGstFromPanNumber");
		return gstDao.getGstFromPanNumber(panNO, orgId);
	}

}

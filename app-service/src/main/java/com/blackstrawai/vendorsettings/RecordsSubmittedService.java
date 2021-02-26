package com.blackstrawai.vendorsettings;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.common.BaseService;

@Service
public class RecordsSubmittedService extends BaseService {

	@Autowired
	RecordsSubmittedDao recordsSubmittedDao;

	private Logger logger = Logger.getLogger(RecordsSubmittedService.class);

	public List<RecordsSubmittedVo> getCountOfRecordsSubmitted(int organizationId) throws ApplicationException {
		logger.info("Entry into Method: getCountOfRecordsSubmitted");
		return recordsSubmittedDao.getCountOfRecordsSubmitted(organizationId);
	}

}

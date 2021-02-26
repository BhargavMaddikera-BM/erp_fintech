package com.blackstrawai.payroll;

import java.text.DateFormatSymbols;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.common.BaseService;
import com.blackstrawai.common.DropDownDao;
import com.blackstrawai.payroll.dropdowns.PayPeriodDropDownVo;

@Service
public class PayPeriodService extends BaseService {

	@Autowired
	PayPeriodDao payPeriodDao;
	@Autowired
	DropDownDao dropDownDao;

	private Logger logger = Logger.getLogger(PayPeriodService.class);

	public PayPeriodVo createPayPeriod(PayPeriodVo payPeriodVo) throws ApplicationException {
		logger.info("Entry into method:createPayPeriod service");
		return payPeriodDao.createPayPeriod(payPeriodVo);
	}

	public List<PayFrequencyListVo> getAllPayPeriodsOfAnOrganization(int organizationId, String userId, String roleName)
			throws ApplicationException {
		logger.info("Entry into method:getAllPayPeriodOfAnOrganization");
		List<PayFrequencyListVo> payPeriodList = payPeriodDao.getAllPayPeriodOfAnOrganization(organizationId, userId,
				roleName);
		Collections.reverse(payPeriodList);
		return payPeriodList;
	}

	public PayPeriodVo getPayPeriodById(int id) throws ApplicationException {
		logger.info("Entry into method:getPayPeriodById");
		return payPeriodDao.getPayPeriodById(id);
	}

	public PayPeriodVo deletePayPeriod(int id, String status, String userId, String roleName, int organizationId)
			throws ApplicationException {
		logger.info("Entry into method:deletePayPeriod");
		return payPeriodDao.deletePayPeriod(id, status, userId, roleName, organizationId);
	}

	public PayPeriodVo updatePayPeriod(PayPeriodVo payPeriodVo) throws ApplicationException {
		logger.info("Entry into method:updatePayPeriod");
		return payPeriodDao.updatePayPeriod(payPeriodVo);
	}

	public PayPeriodDropDownVo getPayPeriodDropDownData(int organizationId) throws ApplicationException {
		logger.info("Entry into method: getPayPeriodDropDownData");
		return dropDownDao.getPayPeriodDropDown(organizationId);
	}

	public List<PayPeriodMonthlyTableVo> getPayPeriodMonthlyTable(int year, String cycle) throws ApplicationException {
		List<PayPeriodMonthlyTableVo> table = new ArrayList<PayPeriodMonthlyTableVo>();
		for (int i = 4; i <= 12; i++) {
			YearMonth yearMonth = YearMonth.of(year, i);
			LocalDate startDate = yearMonth.atDay(1);
			LocalDate endDate = yearMonth.atEndOfMonth();
			String[] shortMonths = new DateFormatSymbols().getShortMonths();
			
			PayPeriodMonthlyTableVo monthlyTable = new PayPeriodMonthlyTableVo();
			monthlyTable.setId(i-3);
			monthlyTable.setName(shortMonths[i-1] + " " + Integer.toString(year));
			monthlyTable.setCycle(cycle);
			monthlyTable.setStartDate(startDate.toString());
			monthlyTable.setEndDate(endDate.toString());
			
			table.add(monthlyTable);
		}
		year++;
		for (int i = 1; i <= 3; i++) {
			YearMonth yearMonth = YearMonth.of(year, i);
			LocalDate startDate = yearMonth.atDay(1);
			LocalDate endDate = yearMonth.atEndOfMonth();
			String[] shortMonths = new DateFormatSymbols().getShortMonths();
			
			PayPeriodMonthlyTableVo monthlyTable = new PayPeriodMonthlyTableVo();
			monthlyTable.setId(i+9);
			monthlyTable.setName(shortMonths[i-1] + " " + Integer.toString(year));
			monthlyTable.setCycle(cycle);
			monthlyTable.setStartDate(startDate.toString());
			monthlyTable.setEndDate(endDate.toString());
			
			table.add(monthlyTable);
		}
		return table;
	}

}

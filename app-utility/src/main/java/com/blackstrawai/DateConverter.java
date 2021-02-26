package com.blackstrawai;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.log4j.Logger;

public class DateConverter {

	private static DateConverter dateConverter;
	private Logger logger = Logger.getLogger(DateConverter.class);

	private DateConverter() {

	}

	public static DateConverter getInstance() {
		if (dateConverter == null) {
			dateConverter = new DateConverter();
		}
		return dateConverter;
	}

	public Date convertStringToDate(String date, String dateFormat) throws ApplicationException {
		try {
			SimpleDateFormat dateFormatt = new SimpleDateFormat(dateFormat);
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MMM-yy");
			SimpleDateFormat sdf3 = new SimpleDateFormat("MM-dd-yy");
			SimpleDateFormat sdf4 = new SimpleDateFormat("dd-MMM-yyyy");
			SimpleDateFormat sdf5 = new SimpleDateFormat("MM/dd/yyyy");
			SimpleDateFormat sdf6 = new SimpleDateFormat("dd/MM/yyyy");
			if (date.isEmpty() || date.length() == 1) {
				return null;
			} else if (date.substring(4, 5).contains("-")) {
				java.util.Date sqlDate = sdf1.parse(date);
				java.sql.Date sqlStartDate = new java.sql.Date(sqlDate.getTime());
				logger.info("Converted date ::" + sqlStartDate);
				return sqlStartDate;
			} else if (date.substring(2, 3).contains("-") && date.substring(6, 7).contains("-") && date.length() == 9) {
				java.util.Date sqlDate = sdf2.parse(date);
				java.sql.Date sqlStartDate = new java.sql.Date(sqlDate.getTime());
				logger.info("Converted date ::" + sqlStartDate);
				return sqlStartDate;
			} else if (date.substring(2, 3).contains("-") && date.substring(5, 6).contains("-")) {
				java.util.Date sqlDate = sdf3.parse(date);
				java.sql.Date sqlStartDate = new java.sql.Date(sqlDate.getTime());
				logger.info("Converted date ::" + sqlStartDate);
				return sqlStartDate;
			} else if (date.substring(2, 3).contains("-") && date.substring(6, 7).contains("-")
					&& date.length() == 11) {
				java.util.Date sqlDate = sdf4.parse(date);
				java.sql.Date sqlStartDate = new java.sql.Date(sqlDate.getTime());
				logger.info("Converted date ::" + sqlStartDate);
				return sqlStartDate;
			} else if (date.substring(2, 3).contains("/") && date.substring(5, 6).contains("/")) {
				if (dateFormatt.equals(sdf5)) {
					logger.info("In format 5th");
					java.util.Date sqlDate = sdf5.parse(date);
					java.sql.Date sqlStartDate = new java.sql.Date(sqlDate.getTime());
					logger.info("Converted date ::" + sqlStartDate);
					return sqlStartDate;
				}
				if (dateFormatt.equals(sdf6)) {
					logger.info("In format 6th");
					java.util.Date sqlDate = sdf6.parse(date);
					java.sql.Date sqlStartDate = new java.sql.Date(sqlDate.getTime());
					logger.info("Converted date ::" + sqlStartDate);
					return sqlStartDate;
				}

			}

		} catch (ParseException e) {
			throw new ApplicationException(e);
		}
		return null;

	}

	public java.util.Date convertStringToTimestamp(String strDate) throws ApplicationException {
		try {
			SimpleDateFormat dateFormat;
			if (strDate.contains("T"))
				dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSS'Z'");
			else
				dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		    java.util.Date parsedDate = dateFormat.parse(strDate);
		    return parsedDate;
		} catch (ParseException e) {
			throw new ApplicationException(e);
		}
	}

	/*
	 * public Date addDaysToCurrentDate() throws DeciferException{ try {
	 * LocalDateTime today = LocalDateTime.now(); LocalDateTime sameDayThirdMonth =
	 * today.plusMonths(3); Date sqlDate =
	 * Date.valueOf(sameDayThirdMonth.toLocalDate()); return sqlDate;
	 * 
	 * } catch (Exception e) { throw new DeciferException(e); }
	 * 
	 * }
	 */

	public String convertDateToGivenFormat(Date inputDate, String dateFormat) {
		String convertedDate = null;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
		convertedDate = simpleDateFormat.format(inputDate);
		return convertedDate;

	}
	
	public String convertDateToGivenFormat(java.util.Date inputDate, String dateFormat) {
		String convertedDate = null;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
		convertedDate = simpleDateFormat.format(inputDate);
		return convertedDate;

	}

	public String correctDatePickerDateToString(String datePickerDate) throws ApplicationException {
		logger.info("Date picker given:" + datePickerDate);
		String convertedDate = "";
		try {
			DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH);
			LocalDate date = LocalDate.parse(datePickerDate, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
			if (datePickerDate != null && datePickerDate.contains("T00:00:00.000Z")) {
				convertedDate = outputFormatter.format(date);
			} else {
				date = date.plusDays(1);
				convertedDate = outputFormatter.format(date);
			}
			logger.info("Date picker date converted:" + convertedDate);
		} catch (Exception e) {
			logger.error("Error in correctDatePickerDateToString:", e);
			throw new ApplicationException(e);
		}
		return convertedDate;
	}

	public String correctDatePickerDateToStringNonCorePaymentsCreate(String datePickerDate)
			throws ApplicationException {
		logger.info("Date picker given:" + datePickerDate);
		String convertedDate = "";
		try {
			DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH);
			LocalDate date = LocalDate.parse(datePickerDate, DateTimeFormatter.ISO_OFFSET_DATE_TIME);

			convertedDate = outputFormatter.format(date);

			logger.info("Date picker date converted:" + convertedDate);
		} catch (Exception e) {
			logger.error("Error in correctDatePickerDateToString:", e);
			throw new ApplicationException(e);
		}
		return convertedDate;
	}

	public String correctDatePickerDateToStringNonCorePayments(String datePickerDate) throws ApplicationException {
		logger.info("Date picker given:" + datePickerDate);
		String convertedDate = "";
		try {
			DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH);
			LocalDate date = LocalDate.parse(datePickerDate, outputFormatter);
//			date=date.plusDays(1);
			convertedDate = outputFormatter.format(date);
			logger.info("Date picker date converted:" + convertedDate);
		} catch (Exception e) {
			logger.error("Error in correctDatePickerDateToString:", e);
			throw new ApplicationException(e);
		}
		return convertedDate;
	}

	public String correctDatePickerDateToStringPayPeriod(String datePickerDate, String dateFormat)
			throws ApplicationException {
		logger.info("Date picker given:" + datePickerDate);
		String convertedDate = "";
		try {
			DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern(dateFormat, Locale.ENGLISH);
			LocalDate date = LocalDate.parse(datePickerDate, outputFormatter);
//			date=date.plusDays(1);
			convertedDate = outputFormatter.format(date);
			logger.info("Date picker date converted:" + convertedDate);
		} catch (Exception e) {
			logger.error("Error in correctDatePickerDateToString:", e);
			throw new ApplicationException(e);
		}
		return convertedDate;
	}

	public String getDatePickerDateFormat(Date date) throws ParseException {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'00:00:00.000'Z'");
		String convertedDate = dateFormat.format(date);
		logger.info("Date picker date converted:" + convertedDate);
		return convertedDate;
	}
	
	public String getDatePickerDateFormatNonCore(Date date) throws ParseException {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'00:00:00.000'Z'");
		String convertedDate = dateFormat.format(date);
		logger.info("Date picker date converted:" + convertedDate);
		return convertedDate;
	}

	public Map<String, Integer> getDayMonthYearFromDate(java.sql.Date date) {
		// create calander instance and get required params
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int month = cal.get(Calendar.MONTH) + 1;
		int day = cal.get(Calendar.DAY_OF_MONTH);
		int year = cal.get(Calendar.YEAR);
		Map<String, Integer> dmyMap = new HashMap<String, Integer>();
		dmyMap.put("Day", day);
		dmyMap.put("Month", month);
		dmyMap.put("Year", year);
		return dmyMap;
	}

	public String convertTimestampToDate(String inputFormat,String outputFormat,String inputDate) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(inputFormat);
		SimpleDateFormat output = new SimpleDateFormat(outputFormat);
		java.util.Date d = sdf.parse(inputDate);
		return output.format(d);
	}
}

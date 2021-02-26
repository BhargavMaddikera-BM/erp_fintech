package com.blackstrawai.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Date;

import com.blackstrawai.common.CommonConstants;

public final class DaoUtil {
    public static String getStatus(String status) {

        return (status == null || status.isEmpty()) ? CommonConstants.STATUS_AS_ACTIVE : status.equalsIgnoreCase(CommonConstants.STATUS_AS_NEW) ? CommonConstants.STATUS_AS_ACTIVE : status;

    }

    public static Timestamp getTimestampFromString(String dateString) throws ParseException {


        TemporalAccessor ta = DateTimeFormatter.ISO_INSTANT.parse(dateString);
        Instant i = Instant.from(ta);
        Date date = Date.from(i);
        return new Timestamp(date.getTime());
    }

       

    public static String getStringFromTimeStamp(Timestamp dateTimeStamp) throws ParseException {
        Date date = new Date(dateTimeStamp.getTime());

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        return dateFormat.format(date);
    }

}

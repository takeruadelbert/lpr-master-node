package com.stn.ester.rest.helper;

import com.stn.ester.rest.var.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class DateTimeHelper {

    @Autowired
    public DateTimeHelper() {

    }

    public static String getCurrentDateInString() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DateTimeFormat.FORMAT_DATE);
        Date date = new Date();
        return simpleDateFormat.format(date);
    }

    public static Date getCurrentDate() throws Exception {
        return new SimpleDateFormat(DateTimeFormat.FORMAT_DATE).parse(getCurrentDateInString());
    }

    public static Date getDate(String dateValue) throws ParseException {
        return new SimpleDateFormat(DateTimeFormat.FORMAT_DATE).parse(dateValue);
    }

    public static Date getDateTime(String dateTimeValue) throws ParseException {
        try {
            return new SimpleDateFormat(DateTimeFormat.FORMAT_DATETIME).parse(dateTimeValue);
        } catch (ParseException e) {
            return getDate(dateTimeValue + " 00:00:00");
        }
    }

    public static String getCurrentTimeStamp() {
        return Long.toString(System.currentTimeMillis());
    }

    public static Date getDateTimeNowPlusSeveralDays(int day) {
        LocalDateTime today = LocalDateTime.now(); //Today
        LocalDateTime maxDay = today.plusDays(day); //Plus several days
        Date currentDatePlusSeveralDays = Date.from(maxDay.atZone(ZoneId.systemDefault()).toInstant());
        return currentDatePlusSeveralDays;
    }

    public static Date getDateTimeNow() {
        LocalDateTime today = LocalDateTime.now();
        Date dateTimeNow = Date.from(today.atZone(ZoneId.systemDefault()).toInstant());
        return dateTimeNow;
    }
}

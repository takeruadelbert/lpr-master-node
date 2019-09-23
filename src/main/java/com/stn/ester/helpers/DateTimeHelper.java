package com.stn.ester.helpers;

import com.stn.ester.constants.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
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

    public static Date getCurrentDate() {
        return new Date();
    }

    public static LocalDate getCurrentLocalDate() {
        return LocalDate.now();
    }

    public static LocalDateTime getCurrentLocalDateTime() {
        return LocalDateTime.now();
    }

    public static LocalDate convertToDate(String dateValue) throws DateTimeParseException {
        return LocalDate.parse(dateValue, DateTimeFormatter.ofPattern(DateTimeFormat.FORMAT_DATE));
    }

    public static LocalDateTime convertToDateTime(String dateTimeValue) throws DateTimeParseException {
        return LocalDateTime.parse(dateTimeValue, DateTimeFormatter.ofPattern(DateTimeFormat.FORMAT_DATETIME));
    }

    public static String getCurrentTimeStamp() {
        return Long.toString(System.currentTimeMillis());
    }

    @Deprecated
    public static Date getDateTimeNowPlusSeveralDays(int day) {
        LocalDateTime today = LocalDateTime.now(); //Today
        LocalDateTime maxDay = today.plusDays(day); //Plus several days
        Date currentDatePlusSeveralDays = Date.from(maxDay.atZone(ZoneId.systemDefault()).toInstant());
        return currentDatePlusSeveralDays;
    }

    @Deprecated
    public static Date getDateTimeNow() {
        LocalDateTime today = LocalDateTime.now();
        Date dateTimeNow = Date.from(today.atZone(ZoneId.systemDefault()).toInstant());
        return dateTimeNow;
    }
}

package com.stn.ester.helpers;

import com.stn.ester.constants.DateTimeFormat;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;

public class DateTimeHelper {

    public DateTimeHelper() {

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

    public static String convertToDateString(LocalDate localDate) {
        return localDate.format(DateTimeFormatter.ofPattern(DateTimeFormat.FORMAT_DATE));
    }

    public static String convertToDateTimeString(LocalDateTime localDateTime) {
        return localDateTime.format(DateTimeFormatter.ofPattern(DateTimeFormat.FORMAT_DATETIME));
    }

    public static String getCurrentTimeStamp() {
        return Long.toString(System.currentTimeMillis());
    }

    public static Date asDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    public static Date asDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static LocalDate asLocalDate(Date date) {
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public static LocalDateTime asLocalDateTime(Date date) {
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();
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

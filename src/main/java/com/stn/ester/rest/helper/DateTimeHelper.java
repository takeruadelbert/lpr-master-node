package com.stn.ester.rest.helper;

import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeHelper {
    private final static String datePattern = "yyyy-MM-dd";

    @Autowired
    public DateTimeHelper() {

    }

    public static String getCurrentDateInString() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(datePattern);
        Date date = new Date();
        return simpleDateFormat.format(date);
    }

    public static Date getCurrentDate() throws Exception {
        return new SimpleDateFormat(datePattern).parse(getCurrentDateInString());
    }

    public static Date getCurrentDate(String dateValue) throws Exception {
        return new SimpleDateFormat(datePattern).parse(dateValue);
    }
}

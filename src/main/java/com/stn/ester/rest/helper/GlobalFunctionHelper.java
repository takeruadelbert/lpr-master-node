package com.stn.ester.rest.helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.text.SimpleDateFormat;
import java.util.Date;

@ControllerAdvice
public class GlobalFunctionHelper {

    @Autowired
    public GlobalFunctionHelper() {
    }

    public static String getDate() {
        String timeStamp = new SimpleDateFormat("HHmmss").format(new Date());
        return timeStamp;
    }

    public static String getNameFile(String vData) {
//        int getLastCharOfDot = vData.lastIndexOf('.');
        int index = vData.indexOf( '.' );
        String name = vData.substring(0, index);
        return name;
    }

    public static String getExtension(String vData) {
        int index = vData.indexOf( '.' );
        String extension = vData.substring(vData.indexOf( '.' ) + 1, vData.length());
        return extension;
    }
}

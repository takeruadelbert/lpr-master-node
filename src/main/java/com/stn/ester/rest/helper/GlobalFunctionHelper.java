package com.stn.ester.rest.helper;

import com.stn.ester.rest.dao.jpa.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

@ControllerAdvice
public class GlobalFunctionHelper {

    private static final String timeNow = "yyyyMMddHHmmss";
    @Autowired
    private static FileRepository fileRepository;
    @Autowired
    public GlobalFunctionHelper() {
    }

    // get Time now
    public static String getDateTimeNow() {
        String timeStamp = new SimpleDateFormat(timeNow).format(new Date());
        return timeStamp;
    }

    // get name file
    public static String getNameFile(String vData) {
        //int getLastCharOfDot = vData.lastIndexOf('.');
        int index = vData.indexOf( '.' );
        String name = vData.substring(0, index);
        return name;
    }

    // get extension file
    public static String getExtensionFile(String vData) {
        int index = vData.indexOf( '.' );
        String extension = "." + vData.substring(vData.indexOf( '.' ) + 1, vData.length());
        return extension;
    }

    // get extension image
    public static String getExtensionImage(String name) {
        String getExtension = name;
        int index = getExtension.indexOf( '.' );
        String extension = getExtension.substring(getExtension.indexOf( '.' ) + 1, getExtension.length());
        return extension;
    }

    // get extension from base64 image
    public static String getExtensionFromBase64(String base64) {
        int getIndexOfColonTwoPoint = base64.indexOf( ';' );
        String getExtension = base64.substring(base64.indexOf( '/' ) + 1, getIndexOfColonTwoPoint);
        return "." + getExtension;
    }

    // remove Data: base64 image
    public static String removeDataFromBase64(String base64) {
        int index = base64.indexOf( ',' );
        String getBase64WithoutData = base64.substring(index + 1, base64.length());
        return getBase64WithoutData;
    }

    // remove all spaces in base64 string
    public static String perfectionBase64(String newBase64) {
        String getNewBase64 = newBase64.replaceAll(" ","+");
        return getNewBase64;
    }

    // get operation system
    public static String getOS() {
        String getOS = System.getProperty("os.name");
        return getOS;
    }
}

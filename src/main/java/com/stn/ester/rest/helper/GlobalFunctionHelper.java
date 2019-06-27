package com.stn.ester.rest.helper;

import com.stn.ester.rest.dao.jpa.AssetFileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

@ControllerAdvice
public class GlobalFunctionHelper {

    private static final String timeNow = "yyyyMMddHHmmss";
    @Autowired
    private static AssetFileRepository assetFileRepository;

    @Autowired
    public GlobalFunctionHelper() {
    }

    public static String getCurrentTimestamp() {
        String timeStamp = new SimpleDateFormat(timeNow).format(new Date());
        return timeStamp;
    }

    // get name file
    public static String getNameFile(String vData) {
        if (vData == null) return null;
        int index = vData.lastIndexOf('.');
        if (index == -1) return vData;
        String name = vData.substring(0, index);
        return name;
    }

    // get extension file
    public static String getExtensionFile(String vData) {
        String extension = "." + vData.substring(vData.lastIndexOf(".") + 1);
        return extension;
    }

    // remove data: from base64 image
    public static String removeDataFromBase64(String base64Image) {
        String getDataImage = base64Image.substring(0, base64Image.indexOf(',') + 1);
        return getDataImage;
    }

    // get extension from base64 image
    public static String getExtensionFromBase64(String base64image) {
        int getIndexOfColonTwoPoint = base64image.indexOf(';');
        String getExtension = base64image.substring(base64image.indexOf('/') + 1, getIndexOfColonTwoPoint);
        return "." + getExtension;
    }

    // remove all spaces in base64 string
    public static String perfectionBase64(String newBase64) {
        String getNewBase64 = newBase64.replaceAll(" ", "+");
        return getNewBase64;
    }

    public static String removeDataFromBase64Two(String base64image) {
        String unwantedText = "data:image/png;base64,";
        int index = base64image.indexOf(',');
        String vData = base64image.substring(index + 1, base64image.length());
        return vData;
    }

    // get operation system
    public static String getOS() {
        String getOS = System.getProperty("os.name");
        return getOS;
    }
}

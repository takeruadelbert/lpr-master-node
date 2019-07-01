package com.stn.ester.rest.helper;

import com.stn.ester.rest.dao.jpa.AssetFileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

@ControllerAdvice
public class GlobalFunctionHelper {

    private static final String timeNow = "yyyy-MM-dd HH:mm:ss";
    @Autowired
    private static AssetFileRepository assetFileRepository;

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
        if (vData == null) return null;
        int index = vData.lastIndexOf('.');
        if (index == -1) return vData;
        String name = vData.substring(0, index);
        return name;
    }

    // get extension file
    public static String getExtensionFile(String vData) {
        String extension = vData.substring(vData.lastIndexOf(".") + 1);
        return extension;
    }

    private static String[] splitDataEncodedBase64(String encodedBase64) {
        return encodedBase64.split(",");
    }

    // remove data: from base64 image
    public static String getRawDataFromEncodedBase64(String encodedBase64) {
        String[] temp = splitDataEncodedBase64(encodedBase64);
        return temp[1];
    }

    public static String getExtFromEncodedBase64(String encodedBase64) {
        String[] temp = splitDataEncodedBase64(encodedBase64);
        String data = temp[0];
        String[] temp2 = data.split("data:image/");
        String data2 = temp2[0];
        String[] temp3 = data2.split(";");
        return temp3[0];
    }
}

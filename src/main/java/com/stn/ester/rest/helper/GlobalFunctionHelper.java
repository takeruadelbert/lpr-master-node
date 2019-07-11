package com.stn.ester.rest.helper;

import com.stn.ester.rest.dao.jpa.AssetFileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.io.File;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.*;

@ControllerAdvice
public class GlobalFunctionHelper {

    private static final String timeNow = "yyyy-MM-dd HH:mm:ss";
    private static final SecureRandom secureRandom = new SecureRandom();
    private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder();

    @Autowired
    public GlobalFunctionHelper() {

	}
	
    /* get time now */
    public static String getDateTimeNow() {
        String timeStamp = new SimpleDateFormat(timeNow).format(new Date());
        return timeStamp;
    }

    /* get name file */
    public static String getNameFile(String vData) {
        if (vData == null) return null;
        int index = vData.lastIndexOf('.');
        if (index == -1) return vData;
        String name = vData.substring(0, index);
        return name;
    }

    /* get extension file */
    public static String getExtensionFile(String vData) {
        String extension = vData.substring(vData.lastIndexOf(".") + 1);
        return extension;
    }

    private static String[] splitDataEncodedBase64(String encodedBase64) {
        return encodedBase64.split(",");
    }

    /* remove data: from base64 image */
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

    public static String generateToken() {
        byte[] randomBytes = new byte[24];
        secureRandom.nextBytes(randomBytes);
        return base64Encoder.encodeToString(randomBytes);
    }

    /*
      check if required directory is exist.
      If it doesn't exists, then program creates the directory automatically
    */
    public static void autoCreateDir(String path) {
        try {
            File assetDir = new File(path);
            if (!assetDir.exists()) {
                assetDir.mkdirs();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

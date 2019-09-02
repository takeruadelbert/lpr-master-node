package com.stn.ester.rest.helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalFunctionHelper {

    private static final String timeNow = "yyyy-MM-dd HH:mm:ss";
    private static final SecureRandom secureRandom = new SecureRandom();
    private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder();
    private static final String SYMBOL = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    @Autowired
    public GlobalFunctionHelper() {
    }

    // Get date and time now.
    public static String getDateTimeNow() {
        String timeStamp = new SimpleDateFormat(timeNow).format(new Date());
        return timeStamp;
    }

    // Get name file.
    public static String getNameFile(String vData) {
        if (vData == null) return null;
        int index = vData.lastIndexOf('.');
        if (index == -1) return vData;
        String name = vData.substring(0, index);
        return name;
    }

    // Get extension file.
    public static String getExtensionFile(String vData) {
        String extension = vData.substring(vData.lastIndexOf(".") + 1);
        return extension;
    }

    private static String[] splitDataEncodedBase64(String encodedBase64) {
        return encodedBase64.split(",");
    }

    // Remove data: from base64 image.
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
        return secureRandom.ints(32, 0, SYMBOL.length()).mapToObj(i -> SYMBOL.charAt(i))
                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append).toString();
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

    public static Map<String, Object> jsonStringToMap(String jsonString) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> jsonMap = new HashMap();
        if (jsonString == null || jsonString.isEmpty())
            return jsonMap;
        jsonMap = mapper.readValue(jsonString, Map.class);
        return jsonMap;
    }

    public static void writeByteArraysToFile(String fileName, byte[] content) throws IOException {
        File file = new File(fileName);
        BufferedOutputStream writer = new BufferedOutputStream(new FileOutputStream(file));
        writer.write(content);
        writer.flush();
        writer.close();
    }
}

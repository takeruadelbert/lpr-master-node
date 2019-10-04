package com.stn.ester.helpers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class GlobalFunctionHelper {

    private static final String timeNow = "yyyy-MM-dd HH:mm:ss";
    private static final SecureRandom secureRandom = new SecureRandom();
    private static final String ALPHANUMERIC_CHAR = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    @Autowired
    public GlobalFunctionHelper() {
    }

    // Get date and time now.
    public static String getDateTimeNow() {
        String timeStamp = new SimpleDateFormat(timeNow).format(new Date());
        return timeStamp;
    }

    public static String generateToken() {
        return secureRandom.ints(32, 0, ALPHANUMERIC_CHAR.length()).mapToObj(i -> ALPHANUMERIC_CHAR.charAt(i))
                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append).toString();
    }

    public static Map<String, Object> jsonStringToMap(String jsonString) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> jsonMap = new HashMap();
        if (jsonString == null || jsonString.isEmpty())
            return jsonMap;
        jsonMap = mapper.readValue(jsonString, Map.class);
        return jsonMap;
    }
}

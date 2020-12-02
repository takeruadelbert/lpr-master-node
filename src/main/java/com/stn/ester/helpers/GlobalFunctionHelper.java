package com.stn.ester.helpers;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GlobalFunctionHelper {

    private static final String timeNow = "yyyy-MM-dd HH:mm:ss";
    private static final SecureRandom secureRandom = new SecureRandom();
    private static final String ALPHANUMERIC_CHAR = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public GlobalFunctionHelper() {
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

    public static ArrayList stringCommaToList(String s) {
        ArrayList result = new ArrayList();
        Object[] values = s.split(",", -1);
        for (Object value : values) {
            result.add(value);
        }
        return result;
    }

    public static String replacePathVariableTo(String target, String replaceWith) {
        return target.replaceAll("\\{.*?}", replaceWith);
    }
}

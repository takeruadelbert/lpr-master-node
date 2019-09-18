package com.stn.ester.services;

import com.stn.ester.constants.DateTimeFormat;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Service
public class GeneralService {

    public static final String STRING_SERVER_TIME = "serverTime";

    public Object getStatus() {
        Map<String, Object> result = new HashMap();
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DateTimeFormat.FORMAT_DATETIME);
        result.put(STRING_SERVER_TIME, dateTimeFormatter.format(now));
        return result;
    }

}

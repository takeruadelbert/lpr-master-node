package com.stn.ester.services;

import com.stn.ester.constants.DateTimeFormat;
import com.stn.ester.entities.EsterLicense;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Service
public class GeneralService {

    public static final String STRING_SERVER_TIME = "serverTime";
    public static final String STRING_IS_EXPIRED = "isExpired";
    public static final String STRING_LICENSE_TO = "licenseTo";
    public static final String STRING_EXPIRE_AT = "expireAt";

    private EsterLicenseService esterLicenseService;

    @Autowired
    public GeneralService(EsterLicenseService esterLicenseService) {
        this.esterLicenseService = esterLicenseService;
    }

    public Object getStatus() {
        Map<String, Object> result = new HashMap();
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DateTimeFormat.FORMAT_DATETIME);
        result.put(STRING_SERVER_TIME, dateTimeFormatter.format(now));
        return result;
    }

    public Object getLicense() {
        Map<String, Object> result = new HashMap();
        EsterLicense esterLicense = esterLicenseService.getOrCreate();
        result.put(STRING_IS_EXPIRED, esterLicenseService.isExpired());
        result.put(STRING_LICENSE_TO, esterLicense.getLicenseTo());
        result.put(STRING_EXPIRE_AT, esterLicense.getExpire());
        return result;
    }

}

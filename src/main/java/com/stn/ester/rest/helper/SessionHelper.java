package com.stn.ester.rest.helper;

import org.springframework.beans.factory.annotation.Autowired;

public class SessionHelper {
    @Autowired
    public SessionHelper() {

    }

    public static long getUserID() {
        return 1;
    }

    public static long getDepartmentID() {
        return 1;
    }
}

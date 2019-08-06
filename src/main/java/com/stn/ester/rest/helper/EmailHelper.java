package com.stn.ester.rest.helper;

import javax.servlet.http.HttpServletRequest;

public class EmailHelper {
    public static String emailFrom() {
        String emailFrom = "mailtest.stn@gmail.com";
        return emailFrom;
    }

    public static String emailTo() {
        String emailTo = "sample.sendmail1@gmail.com";
        return emailTo;
    }

    public static String emailSubject() {
        String emailSubject = "Konfirmasi Reset Password";
        return emailSubject;
    }

    public static String createLinkResetPassword(String token, HttpServletRequest request) {
        String Scheme = String.valueOf(request.getScheme());
        String ServerName = request.getServerName();
        String RequestURI = request.getRequestURI();
        String ServerPort = String.valueOf(request.getServerPort());
        String linkResetPassword = Scheme + "://" + ServerName + ":" + ServerPort + "/" + RequestURI + "/" + token;
        return linkResetPassword;
    }
}

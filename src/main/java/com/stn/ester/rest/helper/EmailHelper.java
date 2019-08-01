package com.stn.ester.rest.helper;

import com.stn.ester.rest.domain.User;

import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import java.util.Optional;
import java.util.Properties;

public class EmailHelper {

    private static final String SMTP_USERNAME = "info@suryateknologi.co.id";
    private static final String SMTP_PASSWORD = "emkF1qRD";
    public static String resetPasswordToken = "";

    // Configuration username and password SMTP.
    public static Session passwordAuthentication(Properties prop) {
        Session session = Session.getInstance(prop, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SMTP_USERNAME, SMTP_PASSWORD);
            }
        });
        return session;
    }

    public static String emailFrom() {
        String emailFrom = "mailtest.stn@gmail.com";
        return emailFrom;
    }

    public static String emailTo() {
        String emailTo = "sample.sendmail1@gmail.com";
        return emailTo;
    }

    public static String emailSubject() {
        String emailSubject = "Password Reset";
        return emailSubject;
    }

    public static String createLinkResetPassword(Optional<User> user, String scheme, String serverName, String serverPort) {
        String username = user.get().getUsername();
        String requestURI = "users/reset-password";
        String linkResetPassword = scheme + "://" + serverName + ":" + serverPort + "/" + requestURI + "/" + resetPasswordToken;
        return linkResetPassword;
    }
}

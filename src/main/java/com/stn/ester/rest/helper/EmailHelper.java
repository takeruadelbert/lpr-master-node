package com.stn.ester.rest.helper;

import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import java.util.Properties;

public class EmailHelper {
    private static final String SMTP_USERNAME = "info@suryateknologi.co.id";
    private static final String SMTP_PASSWORD = "emkF1qRD";

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
        String emailSubject = "Your reset password is ready!";
        return emailSubject;
    }

    public static String emailTemplate() {
        String emailTemplate = "" +
                "Dear user, \n\n" +
                "Please click link below to reset your password! \n\n" +
                "http://localhost:8080/users/reset-password/CyJKAHSD7";
        return emailTemplate;
    }
}

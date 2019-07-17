package com.stn.ester.rest.helper;

import com.stn.ester.rest.domain.User;

import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import java.util.Optional;
import java.util.Properties;

public class EmailHelper {

    private static final String SMTP_USERNAME = "info@suryateknologi.co.id";
    private static final String SMTP_PASSWORD = "emkF1qRD";

    private static final String slash = "/";
    private static final String pointTwo = ":";
    private static final String breakLine = "\n";
    private static final String breakLines = "\n\n";

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
        String emailSubject = "Ester Password Reset";
        return emailSubject;
    }

    public static String emailTemplate(Optional<User> user, String scheme, String serverName, String serverPort, String token) {
        String username = user.get().getUsername();
        String requestURI = "users/reset-password";
        String linkResetPassword = scheme + "://" + serverName + pointTwo + serverPort + slash + requestURI + slash + token;

        // Set template email
        String emailTemplate = "" +
                "Hi " + username + ", " + breakLines +
                "We've received a request to reset your password. If you didn't make the request," + breakLine +
                "just ignore this email. Otherwise, you can reset your password using this link, " + breakLine +
                "click link below to reset your password." + breakLines +
                linkResetPassword + breakLines +
                "Thanks," + breakLine +
                "The Ester Team";
        return emailTemplate;
    }
}

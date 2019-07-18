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
        String emailSubject = "Ester Password Reset";
        return emailSubject;
    }

    public static String emailTemplate(Optional<User> user, String scheme, String serverName, String serverPort, String token) {
        String username = user.get().getUsername();
        String requestURI = "users/reset-password";
        String linkResetPassword = scheme + "://" + serverName + GlobalFunctionHelper.pointTwo + serverPort + GlobalFunctionHelper.slash + requestURI + GlobalFunctionHelper.slash + token;

        // Set email template.
        String emailTemplate = "" +
                "Hi " + username + ", " + GlobalFunctionHelper.breakLines +
                "Kami telah menerima request untuk mereset password anda." + GlobalFunctionHelper.breakLine +
                "Jika anda tidak membuat request, cukup abaikan email ini." + GlobalFunctionHelper.breakLine +
                "Password reset ini valid 24 jam." + GlobalFunctionHelper.breakLine +
                "Klik tautan di bawah ini untuk mereset password anda :" + GlobalFunctionHelper.breakLines +
                linkResetPassword + GlobalFunctionHelper.breakLines +
                "Terima kasih," + GlobalFunctionHelper.breakLine +
                "Tim Ester";
        return emailTemplate;
    }
}

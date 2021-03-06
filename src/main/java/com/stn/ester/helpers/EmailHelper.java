package com.stn.ester.helpers;

import com.stn.ester.constants.LogoResource;

import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EmailHelper {
    private static final String EMAIL_FROM = "mailtest.stn@gmail.com";
    private static final String EMAIL_TO = "sample.sendmail1@gmail.com";
    private static final String EMAIL_SUBJECT = "Konfirmasi Reset Password";
    private static final String REQUEST_URL_PASSWORD_RESET = "users/password_reset";

    public static String emailFrom() {
        String emailFrom = EMAIL_FROM;
        return emailFrom;
    }

    public static String emailTo(String userEmail) {
        String emailTo = userEmail;
        return emailTo;
    }

    public static String emailSubject() {
        String emailSubject = EMAIL_SUBJECT;
        return emailSubject;
    }

    public static String createLinkResetPassword(String token, HttpServletRequest request) {
        String Scheme = String.valueOf(request.getScheme());
        String ServerName = request.getServerName();
        String ServerPort = String.valueOf(request.getServerPort());
        String RequestURI = REQUEST_URL_PASSWORD_RESET;
        String linkResetPassword = Scheme + "://" + ServerName + ":" + ServerPort + "/" + RequestURI + "/" + token;
        return linkResetPassword;
    }

    public static Object embeddedImageOnTemplateMail(String htmlFile, MimeMessage message) throws IOException, MessagingException {
        MimeMultipart multipart = new MimeMultipart();
        BodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setContent(htmlFile, LogoResource.CONTENT_HTML);
        multipart.addBodyPart(messageBodyPart);

        // inline the image to MimeBodyPart, and each MimeBodyPart only have maximum one img, so create one MimeBodyPart again if you want to add new image/logo
        MimeBodyPart imgBody = new MimeBodyPart();
        MimeBodyPart imgBody2 = new MimeBodyPart();
        MimeBodyPart imgBody3 = new MimeBodyPart();
        MimeBodyPart imgBody4 = new MimeBodyPart();

        // put here all source image, logo, and icon to get path file
        List<Path> imgPath = new ArrayList<Path>();
        imgPath.addAll(Arrays.asList(LogoResource.FILE_PATH_ICON_FACEBOOK, LogoResource.FILE_PATH_ICON_TWITTER, LogoResource.FILE_PATH_ICON_LINKEDIN, LogoResource.FILE_PATH_ICON_MY_WEBSITE));

        // put here all image, logo, and icon name to create content_id
        List<String> contentID = new ArrayList<String>();
        contentID.addAll(Arrays.asList(LogoResource.CONTENT_ID_FACEBOOK, LogoResource.CONTENT_ID_TWITTER, LogoResource.CONTENT_ID_LINKEDIN, LogoResource.CONTENT_ID_MY_WEBSITE));

        // put here all imgBody to embed the image
        List<MimeBodyPart> imgBodyPart = new ArrayList<MimeBodyPart>();
        imgBodyPart.addAll(Arrays.asList(imgBody, imgBody2, imgBody3, imgBody4));

        // loop each image and actually will display all inline image/logo on template mail
        for (int i = 0, k = 0, l = 0; i < imgPath.size() && k < contentID.size() && l < imgBodyPart.size(); i++, k++, l++) {
            imgBodyPart.get(l).attachFile(imgPath.get(i).toString());
            imgBodyPart.get(l).setContentID("<" + contentID.get(k) + ">");
            imgBodyPart.get(l).setDisposition(MimeBodyPart.INLINE);
            imgBodyPart.get(l).setHeader(LogoResource.CONTENT_TYPE, LogoResource.CONTENT_TYPE_IMAGE_PNG);
            multipart.addBodyPart(imgBodyPart.get(l));
        }
        message.setContent(multipart);
        return message;
    }
}

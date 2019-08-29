package com.stn.ester.rest.helper;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ConstantHelper {
    private static final Path ROOT = FileSystems.getDefault().getPath("").toAbsolutePath();
    public static final Path FILE_PATH_ICON_FACEBOOK = Paths.get(ROOT.toString(),"src", "main", "resources", "static", "images", "facebook.png");
    public static final Path FILE_PATH_ICON_TWITTER = Paths.get(ROOT.toString(),"src", "main", "resources", "static", "images", "twitter.png");
    public static final Path FILE_PATH_ICON_LINKEDIN = Paths.get(ROOT.toString(),"src", "main", "resources", "static", "images", "linkedin.png");
    public static final Path FILE_PATH_ICON_MY_WEBSITE = Paths.get(ROOT.toString(),"src", "main", "resources", "static", "images", "my_website.png");
    public static final String CONTENT_ID_FACEBOOK = "facebook";
    public static final String CONTENT_ID_TWITTER = "twitter";
    public static final String CONTENT_ID_LINKEDIN = "linkedin";
    public static final String CONTENT_ID_MY_WEBSITE = "my_website";
    public static final String CONTENT_HTML = "text/html";
    public static final String CONTENT_TYPE_IMAGE_PNG = "image/png";
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String VARIABLE_SETTER_THYMELEAF_TEMPLATE_MAIL_USERNAME = "username";
    public static final String VARIABLE_SETTER_THYMELEAF_TEMPLATE_MAIL_ACTION = "url_action";
    public static final String VARIABLE_SETTER_THYMELEAF_TEMPLATE_MAIL_ADDRESS = "address";
    public static final String VARIABLE_SETTER_THYMELEAF_TEMPLATE_MAIL_NAME = "name";
    public static final String VARIABLE_SETTER_THYMELEAF_TEMPLATE_MAIL_WEBSITE = "website";
    public static final String TEMPLATE_MAIL_FILE = "email_template";
    public static final String ENCODING_UTF_8 = "utf-8";
}
package com.stn.ester.rest.helper;

import com.stn.ester.rest.dao.jpa.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.text.SimpleDateFormat;
import java.util.Date;

@ControllerAdvice
public class GlobalFunctionHelper {

    private static final String tmeNow = "HHmmss";
    @Autowired
    private static FileRepository fileRepository;
    @Autowired
    public GlobalFunctionHelper() {
    }

    // get Time now
    public static String getTimeNow() {
        String timeStamp = new SimpleDateFormat(tmeNow).format(new Date());
        return timeStamp;
    }

    // get name file
    public static String getNameFile(String vData) {
        //int getLastCharOfDot = vData.lastIndexOf('.');
        int index = vData.indexOf( '.' );
        String name = vData.substring(0, index);
        return name;
    }

    // get extension file
    public static String getExtensionFile(String vData) {
        int index = vData.indexOf( '.' );
        String extension = "." + vData.substring(vData.indexOf( '.' ) + 1, vData.length());
        return extension;
    }

    // get extension image
    public static String getExtensionImage(String name) {
        String getExtension = name;
        int index = getExtension.indexOf( '.' );
        String extension = getExtension.substring(getExtension.indexOf( '.' ) + 1, getExtension.length());
        return extension;
    }

    // remove data: from base64 image
    public static String removeDataFromBase64(String base64Image) {
        int getlenght = base64Image.length();
        String getDataImage = base64Image.substring(0,base64Image.indexOf( ',' ) + 1);
        return getDataImage;
    }

    // get extension from base64 image
    public static String getExtensionFromBase64(String base64image) {
        int getIndexOfColonTwoPoint = base64image.indexOf( ';' );
        String getExtension = base64image.substring(base64image.indexOf( '/' ) + 1, getIndexOfColonTwoPoint);
        return "." + getExtension;
    }

    // remove Data: base64 image
    public static String removeDataFromBase64Two(String base64image) {
        // String unwantedText = "data:image/png;base64,";
        int index = base64image.indexOf( ',' );
        String vData = base64image.substring(index + 1, base64image.length());
        return vData;
    }

    // get operation system
    public static String getOS() {
        String getOS = System.getProperty("os.name");
        return getOS;
    }
}

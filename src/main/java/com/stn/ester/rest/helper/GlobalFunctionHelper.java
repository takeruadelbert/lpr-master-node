package com.stn.ester.rest.helper;

import com.stn.ester.rest.dao.jpa.FileRepository;
import com.stn.ester.rest.domain.File;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

@ControllerAdvice
public class GlobalFunctionHelper {

    @Autowired
    private static FileRepository fileRepository;

    @Autowired
    public GlobalFunctionHelper() {
    }

    // get DateTime now
    public static String getDate() {
        String timeStamp = new SimpleDateFormat("HHmmss").format(new Date());
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
    public static String getExtension(String vData) {
        int index = vData.indexOf( '.' );
        String extension = vData.substring(vData.indexOf( '.' ) + 1, vData.length());
        return extension;
    }

    //get extension image
    public static String getExtensionImage(String name) {
        String getExtension = name;
        int index = getExtension.indexOf( '.' );
        String extension = getExtension.substring(getExtension.indexOf( '.' ) + 1, getExtension.length());
        return extension;
    }
}

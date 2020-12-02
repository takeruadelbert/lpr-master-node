package com.stn.ester.helpers;

import java.io.File;

public class FileHelper {

    /*
  check if required directory is exist.
  If it doesn't exists, then program creates the directory automatically
*/
    public static void autoCreateDir(String path) {
        try {
            File assetDir = new File(path);
            if (!assetDir.exists()) {
                assetDir.mkdirs();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // Get name file.
    public static String getNameFile(String vData) {
        if (vData == null) return null;
        int index = vData.lastIndexOf('.');
        if (index == -1) return vData;
        String name = vData.substring(0, index);
        return name;
    }

    // Get extension file.
    public static String getExtensionFile(String vData) {
        String extension = vData.substring(vData.lastIndexOf(".") + 1);
        return extension;
    }

    private static String[] splitDataEncodedBase64(String encodedBase64) {
        return encodedBase64.split(",");
    }

    // Remove data: from base64 image.
    public static String getRawDataFromEncodedBase64(String encodedBase64) {
        String[] temp = splitDataEncodedBase64(encodedBase64);
        return temp[1];
    }

    public static String getExtFromEncodedBase64(String encodedBase64) {
        String[] temp = splitDataEncodedBase64(encodedBase64);
        String data = temp[0];
        String[] temp2 = data.split("data:image/");
        String data2 = temp2[0];
        String[] temp3 = data2.split(";");
        return temp3[0];
    }

    public static String removeRequestParamFromExtension(String filename) {
        if (filename == null) return null;
        int index = filename.indexOf('?');
        if (index == -1) return filename;
        String name = filename.substring(0, index);
        return name;
    }
}

package com.stn.lprmaster.misc;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;

public class MasterNodeHelper {
    public static String convertBytesToBase64(String extensionFile, byte[] imageByteArray) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("data:image/%s;base64,", extensionFile));
        sb.append(StringUtils.newStringUtf8(Base64.encodeBase64(imageByteArray, false)));
        return sb.toString();
    }
}

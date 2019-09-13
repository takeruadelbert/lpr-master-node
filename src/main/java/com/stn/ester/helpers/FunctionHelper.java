package com.stn.ester.helpers;

public class FunctionHelper {

    public static String replacePathVariableTo(String target,String replaceWith){
        return target.replaceAll("\\{.*?}", replaceWith);
    }
}

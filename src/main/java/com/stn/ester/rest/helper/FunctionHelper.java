package com.stn.ester.rest.helper;

public class FunctionHelper {

    public static String replacePathVariableTo(String target,String replaceWith){
        return target.replaceAll("\\{.*?}", replaceWith);
    }
}

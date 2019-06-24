package com.stn.ester.rest.base;

public enum PageAccess {
    INDEX("index"), GET("get"), CREATE("create"), UPDATE("update"), DELETE("delete"), LIST("list");

    String name;

    PageAccess(String name) {
        this.name = name;
    }

    String getName(){
        return name;
    }

    public boolean isEqual(String s){
        return getName().equals(s);
    }
}

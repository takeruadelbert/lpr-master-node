package com.stn.ester.rest.domain.enumerate;

import java.util.Arrays;
import java.util.List;

public enum Gender {
    MALE("Laki-laki"),
    FEMALE("Wanita");

    private String label;

    Gender(String label) {
        this.label = label;
    }

    public String getLabel() {
        return this.label;
    }

    public static List<Gender> toList() {
        return Arrays.asList(Gender.values());
    }
}

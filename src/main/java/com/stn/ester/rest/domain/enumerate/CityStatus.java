package com.stn.ester.rest.domain.enumerate;

import java.util.Arrays;
import java.util.List;

public enum CityStatus {
    DISTRICT("Kabupaten"),
    CITY("Kota");

    private String label;

    CityStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return this.label;
    }

    public static List<CityStatus> toList() {
        return Arrays.asList(CityStatus.values());
    }
}

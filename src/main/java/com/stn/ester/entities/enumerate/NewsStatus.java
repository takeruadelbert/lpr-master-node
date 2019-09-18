package com.stn.ester.entities.enumerate;

import java.util.Arrays;
import java.util.List;

public enum NewsStatus {
    SHOWED("Ditampilkan"),
    UNSHOWED("Tidak Ditampilkan");

    private String label;

    NewsStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return this.label;
    }

    public static List<NewsStatus> toList() {
        return Arrays.asList(NewsStatus.values());
    }
}

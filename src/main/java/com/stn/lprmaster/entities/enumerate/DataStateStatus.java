package com.stn.lprmaster.entities.enumerate;

import java.util.Arrays;
import java.util.List;

public enum DataStateStatus {
    RUNNING("Running"),
    STOPPED("Stopped");

    private String label;

    DataStateStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public static List<DataStateStatus> toList() {
        return Arrays.asList(DataStateStatus.values());
    }
}

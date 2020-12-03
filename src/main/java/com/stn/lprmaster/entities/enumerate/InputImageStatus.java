package com.stn.lprmaster.entities.enumerate;

import java.util.Arrays;
import java.util.List;

public enum InputImageStatus {
    WAITING("Waiting"),
    DONE("Done");

    private String label;

    InputImageStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public static List<InputImageStatus> toList() {
        return Arrays.asList(InputImageStatus.values());
    }
}

package com.stn.ester.rest.domain.enumerate;

import java.util.Arrays;
import java.util.List;

public enum EmployeeWorkStatus {
    ACTIVE("Aktif"),
    MUTATION("Mutasi"),
    RETIRED("Pensiun"),
    RESIGN("Keluar");

    private String label;

    EmployeeWorkStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return this.label;
    }

    public static List<EmployeeWorkStatus> toList() {
        return Arrays.asList(EmployeeWorkStatus.values());
    }
}

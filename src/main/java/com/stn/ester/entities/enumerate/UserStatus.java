package com.stn.ester.entities.enumerate;

import com.stn.ester.core.exceptions.InvalidValueException;

import java.util.Arrays;
import java.util.List;

public enum UserStatus {
    ACTIVE("Aktif"),
    DISABLE("Tidak Aktif"),
    EXPIRE("Expired");

    private String label;

    UserStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return this.label;
    }

    public static List<UserStatus> toList() {
        return Arrays.asList(UserStatus.values());
    }

    public static UserStatus getIfPresentOrThrowError(String label) {
        try {
            return UserStatus.valueOf(label);
        } catch (Exception ex) {
            throw new InvalidValueException("Incorrect User Status");
        }
    }
}

package com.stn.ester.repositories.jpa.projections;

public interface OptionItem {
    default String getKey() {
        return null;
    }

    default String getValue() {
        return null;
    }
}

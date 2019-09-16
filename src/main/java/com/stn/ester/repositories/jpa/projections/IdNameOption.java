package com.stn.ester.repositories.jpa.projections;

public interface IdNameOption extends OptionItem {
    String getId();

    String getName();

    default String getKey() {
        return getId();
    }

    default String getValue() {
        return getName();
    }
}

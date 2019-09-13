package com.stn.ester.repositories.jpa.projections;

public interface IdNameList extends OptionList {
    String getId();

    String getName();

    default String getKey() {
        return getId();
    }

    default String getValue() {
        return getName();
    }
}

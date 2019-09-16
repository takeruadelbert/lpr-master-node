package com.stn.ester.repositories.jpa.projections;

public interface IdLabelOption extends OptionItem {
    String getId();

    String getLabel();

    default String getKey() {
        return getId();
    }

    default String getValue() {
        return getLabel();
    }
}

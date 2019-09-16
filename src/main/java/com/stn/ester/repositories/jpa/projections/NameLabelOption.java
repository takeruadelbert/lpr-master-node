package com.stn.ester.repositories.jpa.projections;

public interface NameLabelOption extends OptionItem {
    String getName();
    String getLabel();

    default String getKey() {
        return getName();
    }

    default String getValue() {
        return getLabel();
    }
}

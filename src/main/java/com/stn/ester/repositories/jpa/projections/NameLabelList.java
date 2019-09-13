package com.stn.ester.repositories.jpa.projections;

public interface NameLabelList extends OptionList {
    String getName();
    String getLabel();

    default String getKey() {
        return getName();
    }

    default String getValue() {
        return getLabel();
    }
}

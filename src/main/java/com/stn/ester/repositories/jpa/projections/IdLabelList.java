package com.stn.ester.repositories.jpa.projections;

public interface IdLabelList extends OptionList {
    String getId();

    String getLabel();

    default String getKey() {
        return getId();
    }

    default String getValue() {
        return getLabel();
    }
}

package com.stn.ester.repositories.jpa.projections;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Transient;

public interface OptionList {
    default String getKey() {
        return null;
    }

    default String getValue() {
        return null;
    }
}

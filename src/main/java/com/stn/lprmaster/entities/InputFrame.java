package com.stn.lprmaster.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.stn.ester.entities.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class InputFrame extends BaseEntity {
    private static final String COLUMN_DATA_STATE = "data_state_id";
    private static final String JSON_PROPERTY_DATA_STATE = "dataStateId";
    private static final String COLUMN_MAPPED_INPUT_FRAME = "inputFrame";

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = COLUMN_DATA_STATE, insertable = false, updatable = false)
    private DataState dataState;

    @JsonProperty(JSON_PROPERTY_DATA_STATE)
    @Column(name = COLUMN_DATA_STATE)
    private Long dataStateId;

    private String token;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = COLUMN_MAPPED_INPUT_FRAME, cascade = CascadeType.ALL)
    @JsonManagedReference
    private OutputFrame outputFrame;
}

package com.stn.lprmaster.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.stn.ester.entities.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class OutputFrame extends BaseEntity {
    private static final String COLUMN_INPUT_FRAME = "input_frame_id";

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = COLUMN_INPUT_FRAME)
    @JsonBackReference
    private InputFrame inputFrame;
    private String vehicleType;
    private String licensePlateNumber;
    private String token;
}

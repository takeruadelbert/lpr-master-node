package com.stn.lprmaster.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.stn.ester.entities.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

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

    public OutputFrame(String vehicleType, String licensePlateNumber, String token, InputFrame inputFrame) {
        this.vehicleType = vehicleType;
        this.licensePlateNumber = licensePlateNumber;
        this.token = token;
        this.inputFrame = inputFrame;
    }
}

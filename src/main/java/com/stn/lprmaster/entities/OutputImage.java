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
public class OutputImage extends BaseEntity {
    private static final String COLUMN_INPUT_IMAGE = "input_image_id";

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = COLUMN_INPUT_IMAGE)
    @JsonBackReference
    private InputImage inputImage;
    private String vehicleType;
    private String licensePlateNumber;
    private String token;
}

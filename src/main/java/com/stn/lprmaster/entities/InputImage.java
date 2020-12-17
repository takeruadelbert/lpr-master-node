package com.stn.lprmaster.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.stn.ester.entities.base.BaseEntity;
import com.stn.lprmaster.entities.enumerate.InputImageStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class InputImage extends BaseEntity {
    private static final String COLUMN_MAPPED_INPUT_IMAGE = "inputImage";

    @Column(unique = true)
    private String ticketNumber;

    @Enumerated(EnumType.STRING)
    private InputImageStatus status;
    private String token;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = COLUMN_MAPPED_INPUT_IMAGE, cascade = CascadeType.ALL)
    @JsonManagedReference
    private OutputImage outputImage;
}

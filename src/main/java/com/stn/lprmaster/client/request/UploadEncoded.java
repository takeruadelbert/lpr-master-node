package com.stn.lprmaster.client.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UploadEncoded {
    private String filename;
    private String encoded_file;
}

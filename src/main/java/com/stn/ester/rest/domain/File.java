package com.stn.ester.rest.domain;

import javax.persistence.*;
import lombok.Data;

@Data
@Entity
public class File extends AppDomain {
    public static final String unique_name = "file";

    public String url;
    public String name;
    public String extension;

    @Override
    public String underscoreName() {
        return null;
    }
}

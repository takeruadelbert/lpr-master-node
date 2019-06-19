package com.stn.ester.rest.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Data
@Entity
public class News extends AppDomain {
    public static String unique_name = "news";
    @NotBlank(message = "Title is mandatory.")
    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String synopsis;

    @Column(columnDefinition = "TEXT")
    private String content;
    private String thumbnail_path;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @JsonSetter("userId")
    public void setUserId(long userId) {
        if (userId != 0)
            this.userId = userId;
    }

    public String underscoreName() {
        return unique_name;
    }
}

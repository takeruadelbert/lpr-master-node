package com.stn.ester.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.stn.ester.entities.base.BaseEntity;
import com.stn.ester.entities.constant.EntityConstant;
import com.stn.ester.entities.enumerate.NewsStatus;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Data
@Entity
public class News extends BaseEntity {
    private static final String COLUMN_AUTHOR = "author_id";
    private static final String COLUMN_DEPARTMENT = "department_id";
    private static final String COLUMN_THUMBNAIL = "thumbnail_id";
    private static final String JSON_PROPERTY_AUTHOR = "authorId";
    private static final String JSON_PROPERTY_DEPARTMENT = "departmentId";
    private static final String JSON_PROPERTY_THUMBNAIL = "thumbnailId";

    @NotBlank(message = EntityConstant.MESSAGE_NOT_BLANK)
    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = EntityConstant.COLUMN_DEFINITION_TEXT)
    private String synopsis;

    @Column(columnDefinition = EntityConstant.COLUMN_DEFINITION_TEXT)
    private String content;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = COLUMN_AUTHOR, insertable = false, updatable = false)
    private User author;

    @Column(name = COLUMN_AUTHOR, nullable = false)
    private Long authorId;

    @JsonSetter(JSON_PROPERTY_AUTHOR)
    public void setAuthorId(long authorId) {
        if (authorId != 0)
            this.authorId = authorId;
    }

    @Column(columnDefinition = EntityConstant.COLUMN_DEFINITION_DATE)
    @DateTimeFormat(pattern = EntityConstant.FORMAT_DEFAULT_DATE)
    private LocalDate startDate;

    @Column(columnDefinition = EntityConstant.COLUMN_DEFINITION_DATE)
    @DateTimeFormat(pattern = EntityConstant.FORMAT_DEFAULT_DATE)
    private LocalDate expiredDate;

    @Enumerated(EnumType.STRING)
    private NewsStatus newsStatus;

    @Column(name = COLUMN_DEPARTMENT)
    @JsonProperty(JSON_PROPERTY_DEPARTMENT)
    private Long departmentId;

    @JsonSetter(JSON_PROPERTY_DEPARTMENT)
    public void setDepartmentId(Long departmentId) {
        if (departmentId != 0)
            this.departmentId = departmentId;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = COLUMN_THUMBNAIL, insertable = false, updatable = false)
    private AssetFile thumbnail;

    @Column(name = COLUMN_THUMBNAIL)
    @JsonProperty(JSON_PROPERTY_THUMBNAIL)
    private Long thumbnailId;

    @JsonSetter(JSON_PROPERTY_THUMBNAIL)
    public void setThumbnailId(Long thumbnailId) {
        if (thumbnailId != null)
            this.thumbnailId = thumbnailId;
    }

    public void setThumbnail(AssetFile thumbnail) {
        this.thumbnail = thumbnail;
    }

    @Transient
    private String token;

    public String getToken() {
        return this.token;
    }

    public Long getDepartmentId() {
        return this.departmentId;
    }

    public void setNewsStatus(NewsStatus newsStatus) {
        this.newsStatus = newsStatus;
    }

    public NewsStatus getNewsStatus() {
        return this.newsStatus;
    }
}

package com.stn.ester.rest.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.stn.ester.rest.domain.constant.EntityConstant;
import com.stn.ester.rest.domain.enumerate.NewsStatus;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Date;

@Data
@Entity
public class News extends AppDomain {
    public static String unique_name = "news";
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

    @Column(columnDefinition = EntityConstant.COLUMN_DEFINITION_DATETIME)
    @DateTimeFormat(pattern = EntityConstant.FORMAT_DEFAULT_DATE)
    private Date startDate;

    @Column(columnDefinition = EntityConstant.COLUMN_DEFINITION_DATETIME)
    @DateTimeFormat(pattern = EntityConstant.FORMAT_DEFAULT_DATE)
    private Date expiredDate;

    @Enumerated(EnumType.STRING)
    private NewsStatus newsStatus;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = COLUMN_DEPARTMENT, insertable = false, updatable = false)
    private Department department;

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

    public Date getStartDate() {
        return this.startDate;
    }

    public Date getExpiredDate() {
        return this.expiredDate;
    }

    public Long getDepartmentId() {
        return this.departmentId;
    }

    public String underscoreName() {
        return unique_name;
    }

    public void setNewsStatus(NewsStatus newsStatus) {
        this.newsStatus = newsStatus;
    }

    public NewsStatus getNewsStatus() {
        return this.newsStatus;
    }
}

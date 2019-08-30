package com.stn.ester.rest.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
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
    @NotBlank(message = "Title is mandatory.")
    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String synopsis;

    @Column(columnDefinition = "TEXT")
    private String content;
    private String thumbnail_path;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "author_id", insertable = false, updatable = false)
    private User author;

    @Column(name = "author_id", nullable = false)
    private Long authorId;

    @JsonSetter("authorId")
    public void setAuthorId(long authorId) {
        if (authorId != 0)
            this.authorId = authorId;
    }

    @Column(columnDefinition = "DateTime")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startDate;

    @Column(columnDefinition = "DateTime")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date expiredDate;

    @Enumerated(EnumType.STRING)
    private NewsStatus newsStatus;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "department_id", insertable = false, updatable = false)
    private Department department;

    @Column(name = "department_id")
    @JsonProperty("departmentId")
    private Long departmentId;

    @JsonSetter("departmentId")
    public void setDepartmentId(Long departmentId) {
        if (departmentId != 0)
            this.departmentId = departmentId;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "thumbnail_id", insertable = false, updatable = false)
    private AssetFile thumbnail;

    @Column(name = "thumbnail_id")
    @JsonProperty("thumbnailId")
    private Long assetFileId;

    @JsonSetter("thumbnailId")
    public void setAssetFileId(Long assetFileId) {
        if (assetFileId != null)
            this.assetFileId = assetFileId;
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

package com.stn.ester.rest.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
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

    @Column(columnDefinition = "Date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startDate;

    @Column(columnDefinition = "Date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date expiredDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "news_status_id", insertable = false, updatable = false)
    private NewsStatus newsStatus;

    @Column(name = "news_status_id", nullable = false)
    private Long newsStatusId;

    @JsonSetter("newsStatusId")
    public void setNewsStatusId(long newsStatusId) {
        if (newsStatusId != 0)
            this.newsStatusId = newsStatusId;
    }

    public Date getStartDate() {
        return this.startDate;
    }

    public Date getExpiredDate() {
        return this.expiredDate;
    }

    public long getNewsStatusId() {
        return this.newsStatusId;
    }

    public String underscoreName() {
        return unique_name;
    }
}

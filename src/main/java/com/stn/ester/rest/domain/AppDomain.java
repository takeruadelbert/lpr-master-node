package com.stn.ester.rest.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.util.Date;

@MappedSuperclass
public abstract class AppDomain {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @CreatedDate
    @CreationTimestamp
    Date createdDate;

    @LastModifiedDate
    @CreationTimestamp
    Date lastModifiedDate;

    @JsonIgnore
    @Transient
    public boolean isPreparedForUpdate=false;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @JsonIgnore
    @Transient
    public boolean isPreparedForUpdate() {
        return isPreparedForUpdate;
    }

    public void setPreparedForUpdate(boolean preparedForUpdate) {
        isPreparedForUpdate = preparedForUpdate;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public abstract String underscoreName();

}

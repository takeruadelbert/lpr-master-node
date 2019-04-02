package com.stn.ester.rest.domain;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import java.util.Date;

@MappedSuperclass
public abstract class AppDomain {

    @Id
    @GeneratedValue
    long id;

    @CreatedDate
    Date createdDate;

    @LastModifiedDate
    Date lastModifiedDate;

    @Transient
    boolean isPreparedForUpdate=false;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

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

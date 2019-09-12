package com.stn.ester.rest.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Date;

@MappedSuperclass
public abstract class AppDomain {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @CreatedDate
    @CreationTimestamp
    @JsonIgnore
    LocalDateTime createdDate;

    @LastModifiedDate
    @UpdateTimestamp
    @JsonIgnore
    LocalDateTime lastModifiedDate;

    @JsonIgnore
    @Transient
    public boolean isPreparedForUpdate = false;

    @JsonIgnore
    public boolean deleted = false;

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

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDateTime getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(LocalDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public abstract String underscoreName();

    public void setAttribute(String attributeName,Object value){
        try {
            Field attribute = getClass().getDeclaredField(attributeName);
            attribute.setAccessible(true);
            attribute.set(this,value);
        }catch(Exception ex){
            System.out.println(ex);
        }
    }

    // for validation grouping purpose
    public interface Existing {

    }

    public interface New {

    }
}

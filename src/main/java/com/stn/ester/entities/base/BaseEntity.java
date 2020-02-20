package com.stn.ester.entities.base;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.lang.reflect.Field;
import java.time.LocalDateTime;

@MappedSuperclass
@Data
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @CreatedDate
    @CreationTimestamp
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column(updatable = false)
    protected LocalDateTime createdDate;

    @LastModifiedDate
    @UpdateTimestamp
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    protected LocalDateTime lastModifiedDate;

    @JsonIgnore
    @Transient
    public boolean isPreparedForUpdate = false;

    @JsonIgnore
    public boolean deleted = false;

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

    public void setAttribute(String attributeName, Object value) {
        try {
            Field attribute = getClass().getDeclaredField(attributeName);
            attribute.setAccessible(true);
            attribute.set(this, value);
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    public Object getAttribute(String attributeName, Object value) {
        try {
            Field attribute = getClass().getDeclaredField(attributeName);
            attribute.setAccessible(true);
            return attribute.get(value);
        } catch (Exception ex) {
            return null;
        }
    }

}

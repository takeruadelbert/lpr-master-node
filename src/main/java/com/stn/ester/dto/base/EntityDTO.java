package com.stn.ester.dto.base;

import com.stn.ester.entities.base.BaseEntity;

public abstract class EntityDTO<T extends BaseEntity> {

    public EntityDTO() {}

    protected EntityDTO(T entity) {
    }

}

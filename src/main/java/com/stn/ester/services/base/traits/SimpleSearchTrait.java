package com.stn.ester.services.base.traits;

import com.stn.ester.core.exceptions.SimpleSearchException;
import com.stn.ester.dto.base.EntityDTO;
import com.stn.ester.entities.base.BaseEntity;
import com.stn.ester.helpers.ReflectionHelper;
import com.stn.ester.helpers.SearchAndFilterHelper;
import com.stn.ester.repositories.jpa.base.BaseRepository;
import com.stn.ester.services.base.RepositoryAware;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;

public interface SimpleSearchTrait<U extends BaseEntity, T extends EntityDTO<U>, X extends BaseRepository<U>> extends RepositoryAware<X> {

    String MESSAGE_NO_SUCH_METHOD = "No constructor of %s that accepts %s";

    Collection<String> getSimpleSearchKeys();

    default int getSimpleSearchSize() {
        return 10;
    }

    default Collection<T> simpleSearch(String keyword) {
        Collection<T> result = new ArrayList<>();
        Collection<String> keys = getSimpleSearchKeys();
        Specification<U> spec = SearchAndFilterHelper.resolveSpecificationSingleKeyword(keys, keyword);
        Class<?> clazz = ReflectionHelper.getActualTypeArgumentFromGenericInterface(getClass(), BaseEntity.class, SimpleSearchTrait.class);
        Class<?> clazzDTO = ReflectionHelper.getActualTypeArgumentFromGenericInterface(getClass(), EntityDTO.class, SimpleSearchTrait.class);
        Constructor<T> constructor;
        try {
            constructor = (Constructor<T>) clazzDTO.getConstructor(clazz);
        } catch (NoSuchMethodException e) {
            throw new SimpleSearchException(String.format(MESSAGE_NO_SUCH_METHOD, clazzDTO, clazz));
        }
        try {
            for (U entity : getRepository().findAll(spec, PageRequest.of(0, getSimpleSearchSize()))) {
                result.add(constructor.newInstance(entity));
            }
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new SimpleSearchException(String.format("Cannot instantiate instance of %s", clazz));
        }
        return result;
    }
}

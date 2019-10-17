package com.stn.ester.services.base.traits;

import com.stn.ester.core.exceptions.SimpleSearchException;
import com.stn.ester.core.search.AppSpecification;
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

public interface AdvanceSearchTrait<U extends BaseEntity, X extends BaseRepository<U>> extends RepositoryAware<X> {

    String MESSAGE_NO_SUCH_METHOD = "No constructor of %s that accepts %s";

    default Collection advanceSearch(String keyword, Collection<String> keys, Collection<AppSpecification> appSpecifications, Class targetClazz) {
        return advanceSearch(keyword, keys, appSpecifications, null, targetClazz);
    }

    default Collection advanceSearch(String keyword, Collection<String> keys, Specification externalSpecification, Class targetClazz) {
        return advanceSearch(keyword, keys, null, externalSpecification, targetClazz);
    }

    default Collection advanceSearch(String keyword, Collection<String> keys, Collection<AppSpecification> appSpecifications, Specification externalSpec, Class targetClazz) {
        Specification<U> spec = SearchAndFilterHelper.resolveSpecificationSingleKeyword(keys, keyword);
        if (externalSpec != null) {
            spec = Specification.where(spec).or(externalSpec);
        }
        if (appSpecifications != null) {
            spec = SearchAndFilterHelper.joinSpecification(spec, appSpecifications);
        }
        return advanceSearch(spec, targetClazz);
    }

    default Collection advanceSearch(Specification<U> spec, Class targetClazz) {
        Collection result = new ArrayList<>();
        Class<?> clazz = ReflectionHelper.getActualTypeArgumentFromGenericInterface(getClass(), BaseEntity.class, AdvanceSearchTrait.class);
        Constructor constructor;
        try {
            constructor = targetClazz.getConstructor(clazz);
        } catch (NoSuchMethodException e) {
            throw new SimpleSearchException(String.format(MESSAGE_NO_SUCH_METHOD, targetClazz, clazz));
        }
        try {
            for (U entity : getRepository().findAll(spec, PageRequest.of(0, 10))) {
                result.add(constructor.newInstance(entity));
            }
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new SimpleSearchException(String.format("Cannot instantiate instance of %s", clazz));
        }
        return result;
    }
}

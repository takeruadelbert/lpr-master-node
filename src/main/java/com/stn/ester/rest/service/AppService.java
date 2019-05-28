package com.stn.ester.rest.service;

import com.stn.ester.rest.domain.AppDomain;
import com.stn.ester.rest.helper.UpdaterHelper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.HashMap;

public abstract class AppService {

    protected HashMap<String, PagingAndSortingRepository> repositories;
    protected String baseRepoName;

    @PersistenceContext
    protected EntityManager entityManager;

    public AppService(String baseRepoName) {
        repositories = new HashMap();
        this.baseRepoName = baseRepoName;
    }

    public Page<Object> index(Integer page, Integer size) {
        return repositories.get(baseRepoName).findAll(PageRequest.of(page, size));
    }

    @Transactional
    public Object create(AppDomain o) {
        Object saved = repositories.get(baseRepoName).save(o);
        entityManager.refresh(saved);
        return saved;
    }

    public Object get(Long id) {
        if (repositories.get(baseRepoName).existsById(id)) {
            return repositories.get(
                    baseRepoName).findById(id).get();
        } else {
            throw new ResourceNotFoundException();
        }
    }

    public Object update(Long id, AppDomain object) {
        AppDomain old = (AppDomain) repositories.get(baseRepoName).findById(id).get();
        if (old == null) {
            throw new ResourceNotFoundException();
        }
        preUpdate(object, old);
        return repositories.get(baseRepoName).save(old);
    }

    public void delete(Long id) {
        repositories.get(baseRepoName).deleteById(id);
    }

    // TODO
    // check prepare for level 2 or more
    private void preUpdate(AppDomain src, AppDomain target) {
        if (target.isPreparedForUpdate)
            return;
        BeanUtils.copyProperties(src, target, UpdaterHelper.getNullPropertyNames(src));
        target.setPreparedForUpdate(true);
        final BeanWrapper bw = new BeanWrapperImpl(target);
        java.beans.PropertyDescriptor[] pds = bw.getPropertyDescriptors();
        for (java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = bw.getPropertyValue(pd.getName());
            if (srcValue != null && AppDomain.class.isAssignableFrom(srcValue.getClass())) {
                AppDomain toCompare = (AppDomain) srcValue;
                if (repositories.containsKey(toCompare.underscoreName())) {
                    AppDomain toSave = (AppDomain) repositories.get(toCompare.underscoreName()).findById(toCompare.getId()).get();
                    preUpdate(toCompare, toSave);
                    bw.setPropertyValue(pd.getName(), toSave);
                }
            }
        }
    }

}

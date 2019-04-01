package com.stn.ester.rest.service;

import com.stn.ester.rest.domain.AppDomain;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

public abstract class AppService<T extends PagingAndSortingRepository> {

    protected T repository;

    public AppService(T repository){
        this.repository=repository;
    }

    public Page<Object> index(Integer page,Integer size){
        return repository.findAll(PageRequest.of(page,size));
    }

    public Object create(AppDomain o){
        return repository.save(o);
    }

    public Object get(Long id){
        return repository.findById(id).get();
    }

    public Object update (Long id,AppDomain object){
        Object old=repository.findById(id).get();
        if (old==null){
            return null;
        }
        copyNonNullProperties(object,old);
        return repository.save(old);
    }


    //https://stackoverflow.com/questions/27818334/jpa-update-only-specific-fields
    public static void copyNonNullProperties(Object src, Object target) {
        BeanUtils.copyProperties(src, target, getNullPropertyNames(src));
    }

    public static String[] getNullPropertyNames (Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<String>();
        for(java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) emptyNames.add(pd.getName());
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }
}

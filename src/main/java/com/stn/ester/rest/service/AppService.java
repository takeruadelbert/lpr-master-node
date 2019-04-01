package com.stn.ester.rest.service;

import com.stn.ester.rest.domain.AppDomain;
import com.stn.ester.rest.helper.UpdaterHelper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public abstract class AppService {

    protected HashMap<String,PagingAndSortingRepository> repositories;
    protected String baseRepoName;

    public AppService(String baseRepoName){
        repositories=new HashMap();
        this.baseRepoName=baseRepoName;
    }

    public Page<Object> index(Integer page,Integer size){
        return repositories.get(baseRepoName).findAll(PageRequest.of(page,size));
    }

    public Object create(AppDomain o){
        return repositories.get(baseRepoName).save(o);
    }

    public Object get(Long id){
        return repositories.get(baseRepoName).findById(id).get();
    }

    public Object update (Long id,AppDomain object){
        Object old=repositories.get(baseRepoName).findById(id).get();
        if (old==null){
            return null;
        }
        preUpdate(object,old);
        return repositories.get(baseRepoName).save(old);
    }

    private void preUpdate(Object src,Object target){
        BeanUtils.copyProperties(src, target, UpdaterHelper.getNullPropertyNames(src));
        final BeanWrapper bw = new BeanWrapperImpl(target);
        java.beans.PropertyDescriptor[] pds = bw.getPropertyDescriptors();
        for(java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = bw.getPropertyValue(pd.getName());
            if (srcValue != null && AppDomain.class.isAssignableFrom(srcValue.getClass()) ){
                AppDomain toCompare=(AppDomain) srcValue;
                Object toSave=repositories.get(toCompare.underscoreName()).findById(toCompare.getId()).get();
                BeanUtils.copyProperties(srcValue,toSave, UpdaterHelper.getNullPropertyNames(srcValue));
                bw.setPropertyValue(pd.getName(),toSave);
            }
        }
    }

}

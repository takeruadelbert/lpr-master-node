package com.stn.ester.rest.service;

import com.stn.ester.rest.domain.AppDomain;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.PagingAndSortingRepository;

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
        object.setId(id);
        return repository.save(object);
    }
}

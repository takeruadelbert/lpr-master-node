package com.stn.ester.rest.controller.api;

import com.stn.ester.rest.domain.AppDomain;
import com.stn.ester.rest.domain.User;
import com.stn.ester.rest.service.AppService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

public abstract class AppController<T extends AppService,U extends AppDomain> {

    protected static final String DEFAULT_PAGE_SIZE = "10";
    protected static final String DEFAULT_PAGE_NUM = "0";

    protected T service;

    public AppController(T service){
        this.service=service;
    }

    @RequestMapping(value="", method = RequestMethod.GET)
    public Page<Object> index(@RequestParam(name="page", defaultValue = DEFAULT_PAGE_NUM) Integer page, @RequestParam(name="size", defaultValue = DEFAULT_PAGE_SIZE) Integer size){
        return service.index(page,size);
    }

    @RequestMapping(value ="/{id}", method = RequestMethod.GET)
    public Object get(@PathVariable long id){
        return service.get(id);
    }

    @RequestMapping(value ="", method = RequestMethod.POST)
    public Object create(@RequestBody U domain){
        return service.create(domain);
    }

    @RequestMapping(value ="/{id}", method = RequestMethod.PUT)
    public Object update(@PathVariable long id,@RequestBody U domain){
        return service.update(id,domain);
    }
}

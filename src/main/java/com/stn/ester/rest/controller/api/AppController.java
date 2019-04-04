package com.stn.ester.rest.controller.api;

import com.stn.ester.rest.service.AppService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

public abstract class AppController<T extends AppService> {

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
}

package com.stn.ester.rest.controller;

import com.stn.ester.rest.domain.AppDomain;
import com.stn.ester.rest.service.AppService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

public abstract class AppController<T extends AppService, U extends AppDomain> {

    protected static final String DEFAULT_PAGE_SIZE = "10";
    protected static final String DEFAULT_PAGE_NUM = "0";

    protected T service;

    @Autowired
    protected ModelMapper modelMapper;

    public AppController(T service) {
        this.service = service;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public Page<Object> index(@RequestParam(name = "page", defaultValue = DEFAULT_PAGE_NUM) Integer page, @RequestParam(name = "size", defaultValue = DEFAULT_PAGE_SIZE) Integer size) {
        return service.index(page, size);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Object get(@PathVariable long id) {
        return service.get(id);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public Object create(@Validated(AppDomain.New.class) @RequestBody U domain) {
        return service.create(domain);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Object update(@PathVariable long id, @Validated(AppDomain.Existing.class) @RequestBody U domain) {
        return service.update(id, domain);
    }

    @RequestMapping(value = "", method = RequestMethod.PUT)
    public Object update(@Valid @RequestBody U domain) {
        return service.update((long) 1, domain);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable long id) {
        service.delete(id);
    }
}

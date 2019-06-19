package com.stn.ester.rest.controller;

import com.google.common.base.Joiner;
import com.stn.ester.rest.domain.AppDomain;
import com.stn.ester.rest.search.AppSpecification;
import com.stn.ester.rest.search.SpecificationsBuilder;
import com.stn.ester.rest.search.util.SearchOperation;
import com.stn.ester.rest.service.AppService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    public Page<Object> index(@RequestParam(name = "page", defaultValue = DEFAULT_PAGE_NUM) Integer page, @RequestParam(name = "size", defaultValue = DEFAULT_PAGE_SIZE) Integer size, @RequestParam(value = "search", required = false) String search) {
        Specification<U> spec = resolveSpecification(search);
        return service.index(page, size, spec);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Object get(@PathVariable long id) {
        return service.get(id);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public Object create(@Valid @RequestBody U domain) {
        return service.create(domain);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Object update(@PathVariable long id, @Valid @RequestBody U domain) {
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

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Object list() {
        return service.getList();
    }

    protected Specification<U> resolveSpecification(String searchParameters) {
        SpecificationsBuilder builder = new SpecificationsBuilder<>();
        String operationSetExper = Joiner.on("|")
                .join(SearchOperation.SIMPLE_OPERATION_SET);
        Pattern pattern = Pattern.compile("(\\p{Punct}?)(\\w+?)(" + operationSetExper + ")(\\p{Punct}?)(\\w+?)(\\p{Punct}?),");
        Matcher matcher = pattern.matcher(searchParameters + ",");
        System.out.println(searchParameters);
        while (matcher.find()) {
            builder.with(matcher.group(1), matcher.group(2), matcher.group(3), matcher.group(5), matcher.group(4), matcher.group(6));
        }
        return builder.build();
    }
}

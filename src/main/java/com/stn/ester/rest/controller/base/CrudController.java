package com.stn.ester.rest.controller.base;

import com.stn.ester.rest.domain.AppDomain;
import com.stn.ester.rest.helper.SearchAndFilterHelper;
import com.stn.ester.rest.service.AppService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public abstract class CrudController<T extends AppService, U extends AppDomain> extends SecuredController {

    protected static final String DEFAULT_PAGE_SIZE = "10";
    protected static final String DEFAULT_PAGE_NUM = "0";

    protected T service;

    @Autowired
    protected ModelMapper modelMapper;

    public CrudController(T service) {
        this.service = service;
    }

    @PreAuthorize("hasRole(#this.this.readCurrentUserRole())")
    @RequestMapping(value = "", method = RequestMethod.GET)
    public Page<Object> index(@RequestParam(name = "page", defaultValue = DEFAULT_PAGE_NUM) Integer page, @RequestParam(name = "size", defaultValue = DEFAULT_PAGE_SIZE) Integer size, @RequestParam(value = "search", required = false) String search) throws UnsupportedEncodingException {
        if (search != null) {
            search = URLDecoder.decode(search, StandardCharsets.UTF_8.toString());
        }
        Specification<U> spec = SearchAndFilterHelper.resolveSpecification(search);
        return service.index(page, size, spec);
    }

    @PreAuthorize("hasRole(#this.this.readCurrentUserRole())")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Object get(@PathVariable long id) {
        return service.get(id);
    }

    @PreAuthorize("hasRole(#this.this.readCurrentUserRole())")
    @RequestMapping(value = "", method = RequestMethod.POST)
    public Object create(@Valid @RequestBody U domain) {
        return service.create(domain);
    }

    @PreAuthorize("hasRole(#this.this.readCurrentUserRole())")
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Object update(@PathVariable long id, @Valid @RequestBody U domain) {
        return service.update(id, domain);
    }

    @PreAuthorize("hasRole(#this.this.readCurrentUserRole())")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable long id) {
        service.delete(id);
    }

    @PreAuthorize("hasRole(#this.this.readCurrentUserRole())")
    @RequestMapping(value = "", method = RequestMethod.OPTIONS)
    public Object list() {
        return service.getList();
    }


}

package com.stn.ester.rest.controller;

import com.stn.ester.rest.domain.AppDomain;
import com.stn.ester.rest.helper.GlobalFunctionHelper;
import com.stn.ester.rest.helper.SessionHelper;
import com.stn.ester.rest.search.SpecificationsBuilder;
import com.stn.ester.rest.service.AppService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import static com.stn.ester.rest.security.SecurityConstants.*;

public abstract class AppController<T extends AppService, U extends AppDomain> implements BeanNameAware {

    protected static final String DEFAULT_PAGE_SIZE = "10";
    protected static final String DEFAULT_PAGE_NUM = "0";

    protected T service;
    String beanName;

    @Autowired
    protected ModelMapper modelMapper;

    public AppController(T service) {
        this.service = service;
    }

    @PreAuthorize("hasAuthority(#this.this.getAuthority())")
    @RequestMapping(value = "", method = RequestMethod.GET)
    public Page<Object> index(@RequestParam(name = "page", defaultValue = DEFAULT_PAGE_NUM) Integer page, @RequestParam(name = "size", defaultValue = DEFAULT_PAGE_SIZE) Integer size, @RequestParam(value = "search", required = false) String search) throws UnsupportedEncodingException {
        if (search != null) {
            search = URLDecoder.decode(search, StandardCharsets.UTF_8.toString());
        }
        Specification<U> spec = resolveSpecification(search);
        return service.index(page, size, spec);
    }

    @PreAuthorize("hasAuthority(#this.this.getAuthority())")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Object get(@PathVariable long id) {
        return service.get(id);
    }

    @PreAuthorize("hasAuthority(#this.this.getAuthority())")
    @RequestMapping(value = "", method = RequestMethod.POST)
    public Object create(@Valid @RequestBody U domain) {
        return service.create(domain);
    }

    @PreAuthorize("hasAuthority(#this.this.getAuthority())")
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Object update(@PathVariable long id, @Valid @RequestBody U domain) {
        return service.update(id, domain);
    }

    @RequestMapping(value = "", method = RequestMethod.PUT)
    public Object update(@Valid @RequestBody U domain) {
        return service.update((long) 1, domain);
    }

    @PreAuthorize("hasAuthority(#this.this.getAuthority())")
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
        try {
            this.loopSpecToSpecBuilder(builder, GlobalFunctionHelper.jsonStringToMap(searchParameters));
        } catch (IOException ex) {
            System.out.println(ex);
        }
        return builder.build();
    }

    private void loopSpecToSpecBuilder(SpecificationsBuilder specificationsBuilder, Map<String, Object> jsonMap) {
        for (Map.Entry<String, Object> entry : jsonMap.entrySet()) {
            if (Map.class.isAssignableFrom(entry.getValue().getClass())) {
                this.loopSpecToSpecBuilder(specificationsBuilder, (Map<String, Object>) entry.getValue());
            } else {
                String operation = entry.getKey().substring(0, 1);
                String key = entry.getKey().substring(1);
                specificationsBuilder.with(key, operation, entry.getValue().toString(), null, null);
            }
        }
    }

    @Override
    public void setBeanName(final String beanName) {
        this.beanName = beanName;
    }

    public String getBeanName() {
        return beanName;
    }

    public String getAuthority() {
        String authority = "NOACCESS";
        if (!SessionHelper.isSuperAdmin()) {
            String currentName = getBeanName().replace("Controller", "");
            final ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder
                    .currentRequestAttributes();
            final HttpServletRequest request = attr.getRequest();
            authority = AUTHORITY_PREFIX + "_" + request.getMethod() + "_" + currentName;
        } else {
            authority = ROLE_PREFIX + "_" + ROLE_SUPERADMIN;
        }
        return authority;
    }
}

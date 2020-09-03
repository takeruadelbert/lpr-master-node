package com.stn.ester.controllers.base;

import com.stn.ester.core.exceptions.NotFoundException;
import com.stn.ester.dto.base.EntityDTO;
import com.stn.ester.entities.base.BaseEntity;
import com.stn.ester.helpers.SearchAndFilterHelper;
import com.stn.ester.services.base.CrudService;
import com.stn.ester.services.base.traits.SimpleSearchTrait;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Map;

public abstract class CrudController<T extends CrudService, U extends BaseEntity> extends SecuredController {

    protected static final String DEFAULT_PAGE_SIZE = "10";
    protected static final String DEFAULT_PAGE_NUM = "0";

    protected T service;

    @Autowired
    protected ModelMapper modelMapper;

    private Boolean isReturnAsDTO = false;

    private Class<? extends EntityDTO> dtoClass;

    private Class<U> entityClass;

    public CrudController(T service) {
        this.service = service;
        this.entityClass = (Class<U>) ((ParameterizedType) this.getClass().getGenericSuperclass())
                .getActualTypeArguments()[1];
    }

    @PreAuthorize("hasRole(#this.this.readCurrentUserRole())")
    @RequestMapping(value = "", method = RequestMethod.GET)
    public Page<? extends Object> index(@RequestParam(name = "page", defaultValue = DEFAULT_PAGE_NUM) Integer page, @RequestParam(name = "size", defaultValue = DEFAULT_PAGE_SIZE) Integer size, @RequestParam(value = "search", required = false) String search) throws UnsupportedEncodingException {
        if (search != null) {
            search = URLDecoder.decode(search, StandardCharsets.UTF_8.toString());
        }
        Specification<U> spec = SearchAndFilterHelper.resolveSpecification(search);
        return isCastToDTO(service.index(page, size, spec));
    }

    @PreAuthorize("hasRole(#this.this.readCurrentUserRole())")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Object get(@PathVariable long id) {
        return this.isCastToDTO(service.get(id));
    }

    @PreAuthorize("hasRole(#this.this.readCurrentUserRole())")
    @RequestMapping(value = "", method = RequestMethod.POST)
    public Object create(@Valid @RequestBody U domain) {
        return this.isCastToDTO(service.create(domain));
    }

    @PreAuthorize("hasRole(#this.this.readCurrentUserRole())")
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Object update(@PathVariable long id, @Valid @RequestBody U domain, @RequestBody Map<String, Object> requestBody) {
        return this.isCastToDTO(service.update(id, domain, requestBody));
    }

    @PreAuthorize("hasRole(#this.this.readCurrentUserRole())")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable long id) {
        service.delete(id);
    }

    @PreAuthorize("hasRole(#this.this.readCurrentUserRole())")
    @RequestMapping(value = "", method = RequestMethod.OPTIONS)
    public Object list() {
        return service.list();
    }

    @PreAuthorize("hasRole(#this.this.readCurrentUserRole())")
    @RequestMapping(value = "simplesearch", method = RequestMethod.OPTIONS)
    public Collection simpleSearch(@RequestParam(name = "keyword") String keyword) {
        if (service instanceof SimpleSearchTrait) {
            return ((SimpleSearchTrait) service).simpleSearch(keyword);
        } else {
            throw new NotFoundException("Simple search not found");
        }
    }

    private Object isCastToDTO(BaseEntity o) {
        if (isReturnAsDTO) {
            try {
                return dtoClass.getConstructor(entityClass).newInstance(o);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return o;
    }

    private Page<? extends Object> isCastToDTO(Page<BaseEntity> page){
        return page.map(p-> isCastToDTO(p));
    }

    protected void enableReturnAsDTO(Class<? extends EntityDTO<U>> dtoClass) {
        this.dtoClass = dtoClass;
        this.isReturnAsDTO = true;
    }

    protected void disabledReturnAsDto() {
        this.isReturnAsDTO = false;
    }
}

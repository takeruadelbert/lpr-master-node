package com.stn.ester.rest.service;

import com.stn.ester.rest.dao.jpa.base.AppRepository;
import com.stn.ester.rest.dao.jpa.base.AppRepository;
import com.stn.ester.rest.dao.jpa.projections.IdLabelList;
import com.stn.ester.rest.dao.jpa.projections.IdNameList;
import com.stn.ester.rest.dao.jpa.projections.NameLabelList;
import com.stn.ester.rest.dao.jpa.projections.OptionList;
import com.stn.ester.rest.domain.AppDomain;
import com.stn.ester.rest.exception.ListNotFoundException;
import com.stn.ester.rest.helper.UpdaterHelper;
import com.stn.ester.rest.service.base.OptionBehaviour;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

public abstract class AppService implements OptionBehaviour {

    protected HashMap<String, AppRepository> repositories;
    protected String baseRepoName;

    @PersistenceContext
    protected EntityManager entityManager;

    public AppService(String baseRepoName) {
        repositories = new HashMap();
        this.baseRepoName = baseRepoName;
    }

    public Page<Object> index(Integer page, Integer size) {
        return repositories.get(baseRepoName).findAll(PageRequest.of(page, size));
    }

    public Page<Object> index(Integer page, Integer size, Specification spec) {
        return repositories.get(baseRepoName).findAll(spec, PageRequest.of(page, size));
    }

    @Transactional
    public Object create(AppDomain o) {
        Object saved = repositories.get(baseRepoName).save(o);
        entityManager.refresh(saved);
        return saved;
    }

    public Object get(Long id) {
        if (repositories.get(baseRepoName).existsById(id)) {
            return repositories.get(
                    baseRepoName).findById(id).get();
        } else {
            throw new ResourceNotFoundException();
        }
    }

    public Object update(Long id, AppDomain object) {
        AppDomain old = (AppDomain) repositories.get(baseRepoName).findById(id).get();
        if (old == null) {
            throw new ResourceNotFoundException();
        }
        preUpdate(object, old);
        return repositories.get(baseRepoName).save(old);
    }

    public void delete(Long id) {
        repositories.get(baseRepoName).deleteById(id);
    }

    // TODO
    // check prepare for level 2 or more
    private void preUpdate(AppDomain src, AppDomain target) {
        if (target.isPreparedForUpdate)
            return;
        BeanUtils.copyProperties(src, target, UpdaterHelper.getNullPropertyNames(src));
        target.setPreparedForUpdate(true);
        final BeanWrapper bw = new BeanWrapperImpl(target);
        java.beans.PropertyDescriptor[] pds = bw.getPropertyDescriptors();
        for (java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = bw.getPropertyValue(pd.getName());
            if (srcValue != null && AppDomain.class.isAssignableFrom(srcValue.getClass())) {
                AppDomain toCompare = (AppDomain) srcValue;
                if (repositories.containsKey(toCompare.underscoreName())) {
                    AppDomain toSave = (AppDomain) repositories.get(toCompare.underscoreName()).findById(toCompare.getId()).get();
                    preUpdate(toCompare, toSave);
                    bw.setPropertyValue(pd.getName(), toSave);
                }
            }
        }
    }

    @Override
    public Object getList() {
        String projectionType = this.getReflectionType();
        if (projectionType.isEmpty())
            throw new ListNotFoundException();
        // check projection type
        HashMap<String, String> result = new HashMap<>();
        String[] temp = projectionType.split("\\.");
        switch (temp[temp.length - 1]) {
            case "IdNameList":
                List<IdNameList> list = repositories.get(baseRepoName).findAllProjectedBy();
                if (list.size() > 0) {
                    for (IdNameList option : list) {
                        result.put(option.getId(), option.getName());
                    }
                }
                return result;
            case "IdLabelList":
                List<IdLabelList> labelLists = repositories.get(baseRepoName).findAllProjectedBy();
                if (labelLists.size() > 0) {
                    for (IdLabelList option : labelLists) {
                        result.put(option.getId(), option.getLabel());
                    }
                }
                return result;
            case "NameLabelList":
                List<NameLabelList> nameLabelLists = repositories.get(baseRepoName).findAllProjectedBy();
                if (nameLabelLists.size() > 0) {
                    for (NameLabelList option : nameLabelLists) {
                        result.put(option.getName(), option.getLabel());
                    }
                }
                return result;
            default:
                throw new ListNotFoundException();
        }
    }

    private String getReflectionType() {
        Class[] interfazes = (repositories.get(baseRepoName)).getClass().getInterfaces();
        for (Class interfaze : interfazes) {
            if (AppRepository.class.isAssignableFrom(interfaze)) {
                Type[] genericInterfaces = interfaze.getGenericInterfaces();
                for (Type genericInterface : genericInterfaces) {
                    if (genericInterface instanceof ParameterizedType) {
                        Type[] typeParameter = ((Class<?>) ((ParameterizedType) genericInterface).getRawType()).getTypeParameters();
                        Type[] actualTypeArgument = ((ParameterizedType) genericInterface).getActualTypeArguments();
                        for (int i = 0; i < typeParameter.length; i++) {
                            if (typeParameter[i].getTypeName().equals("Y")) {
                                return actualTypeArgument[i].getTypeName();
                            }
                        }
                    }
                }
            }
        }
        return null;
    }
}

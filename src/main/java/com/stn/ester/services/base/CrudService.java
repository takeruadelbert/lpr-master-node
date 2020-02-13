package com.stn.ester.services.base;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.CaseFormat;
import com.google.common.collect.Sets;
import com.stn.ester.core.base.AutoRemoveChild;
import com.stn.ester.core.base.OnDeleteRemoveChild;
import com.stn.ester.core.base.OnDeleteSetParentNull;
import com.stn.ester.core.base.TableFieldPair;
import com.stn.ester.core.exceptions.ListNotFoundException;
import com.stn.ester.core.search.AppSpecification;
import com.stn.ester.core.search.util.SearchOperation;
import com.stn.ester.core.search.util.SpecSearchCriteria;
import com.stn.ester.entities.base.BaseEntity;
import com.stn.ester.helpers.ReflectionHelper;
import com.stn.ester.helpers.UpdaterHelper;
import com.stn.ester.repositories.jpa.base.BaseRepository;
import com.stn.ester.repositories.jpa.base.traits.RepositoryListTrait;
import com.stn.ester.repositories.jpa.projections.OptionItem;
import org.hibernate.Hibernate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.lang.annotation.Annotation;
import java.util.*;
import java.util.stream.Collectors;

import static com.stn.ester.entities.constant.EntityConstant.FIELD_CREATED_DATE;
import static com.stn.ester.entities.constant.EntityConstant.FIELD_ID;

public abstract class CrudService<T extends BaseEntity, U extends BaseRepository<T>> extends BaseService<T> {

    protected U currentEntityRepository;

    protected ApplicationContext applicationContext;

    @PersistenceContext
    protected EntityManager entityManager;

    public CrudService(U entityRepository) {
        this.currentEntityRepository = entityRepository;
    }

    @Autowired
    public final void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        repositories = applicationContext.getBeansOfType(BaseRepository.class);
    }

    public Page<T> index(Integer page, Integer size) {
        return index(page, size, null);
    }

    public Page<T> index(Integer page, Integer size, Specification spec) {
        return index(page, size, spec, Sort.by(Sort.Order.desc(FIELD_CREATED_DATE), Sort.Order.desc(FIELD_ID)));
    }

    public Page<T> index(Integer page, Integer size, Specification spec, Sort sort) {
        return currentEntityRepository.findAll(spec, PageRequest.of(page, size, sort));
    }

    public T get(Long id) {
        if (currentEntityRepository.existsById(id)) {
            return currentEntityRepository.findById(id).get();
        } else {
            throw new ResourceNotFoundException();
        }
    }

    @Transactional
    public T create(T o) {
        T saved = currentEntityRepository.save(o);
        entityManager.refresh(saved);
        return saved;
    }

    @Transactional
    public T update(Long id, T object) {
        return update(id, object, null);
    }

    @Transactional
    public T update(Long id, T object, Map<String, Object> comparator) {
        T old = currentEntityRepository.findById(id).get();
        if (old == null) {
            throw new ResourceNotFoundException();
        }
        autoRemoveChild(id, old, object, comparator);
        preUpdate(object, old, comparator);
        T saved = currentEntityRepository.save(old);
        entityManager.flush();
        entityManager.refresh(saved);
        return saved;
    }

    public Object list() {
        if (currentEntityRepository instanceof RepositoryListTrait) {
            Map<String, String> result = new HashMap<>();
            List<OptionItem> list = ((RepositoryListTrait) currentEntityRepository).findAllProjectedBy();
            for (OptionItem optionItem : list) {
                result.put(optionItem.getKey(), optionItem.getValue());
            }
            return result;
        } else {
            throw new ListNotFoundException();
        }
    }

    public void delete(Long id) {
        this.onDeleteSetParentNull(id);
        this.onDeleteRemoveChild(id);
        currentEntityRepository.deleteById(id);
    }


    // TODO
    // check prepare for level 2 or more
    private void preUpdate(BaseEntity src, BaseEntity target, Map<String, Object> comparator) {
        if (target.isPreparedForUpdate)
            return;
        BeanUtils.copyProperties(src, target, UpdaterHelper.getIgnoredProperty(src, comparator));
        target.setPreparedForUpdate(true);
        final BeanWrapper bw = new BeanWrapperImpl(target);
        java.beans.PropertyDescriptor[] pds = bw.getPropertyDescriptors();
        for (java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = bw.getPropertyValue(pd.getName());
            if (srcValue != null && BaseEntity.class.isAssignableFrom(srcValue.getClass())) {
                BaseEntity toCompare = (BaseEntity) srcValue;
                String repositoryBeanName = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, Hibernate.unproxy(toCompare).getClass().getSimpleName()) + "Repository";
                BaseEntity toSave = (BaseEntity) repositories.get(repositoryBeanName).findById(toCompare.getId()).get();
                Map<String, Object> childComparator = null;
                if (comparator != null) {
                    Object objectValue = comparator.getOrDefault(pd.getName(), null);
                    if (objectValue != null) {
                        ObjectMapper objectMapper = new ObjectMapper();
                        childComparator = objectMapper.convertValue(objectValue, Map.class);
                    }
                }
                preUpdate(toCompare, toSave, childComparator);
                bw.setPropertyValue(pd.getName(), toSave);
            }
        }
    }

    //untuk automatis menghapus data yang tidak child yang tidak diterima
    private void autoRemoveChild(Long parentId, T oldObject, T newObject, Map<String, Object> comparator) {
        Class<?> clazz = ReflectionHelper.getActualTypeArgumentFromGenericInterfaceWithProxiedClass(currentEntityRepository.getClass(), BaseEntity.class, BaseRepository.class);
        Annotation autoRemoveChild = clazz.getDeclaredAnnotation(AutoRemoveChild.class);
        if (autoRemoveChild != null && comparator != null) {
            TableFieldPair[] tableFieldPairs = clazz.getDeclaredAnnotation(AutoRemoveChild.class).value();
            for (TableFieldPair tableFieldPair : tableFieldPairs) {
                if (CrudService.class.isAssignableFrom(tableFieldPair.service())) {
                    String serviceName = tableFieldPair.service().getSimpleName();
                    String attributeName = tableFieldPair.attributeName();
                    serviceName = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, serviceName);
                    Object attributeOfNewObject = newObject.getAttribute(attributeName, newObject);
                    Object attributeOfOldObject = newObject.getAttribute(attributeName, oldObject);
                    Set<Long> newIds;
                    Set<Long> oldIds;
                    if (attributeOfNewObject != null
                            && Collection.class.isAssignableFrom(attributeOfNewObject.getClass())
                            && attributeOfOldObject != null
                            && Collection.class.isAssignableFrom(attributeOfOldObject.getClass())
                            && comparator.containsKey(attributeName)) {
                        Set<BaseEntity> newEntities = (Set) attributeOfNewObject;
                        Set<BaseEntity> oldEntities = (Set) attributeOfOldObject;
                        newIds = newEntities.stream().map(BaseEntity::getId).collect(Collectors.toSet());
                        oldIds = oldEntities.stream().map(BaseEntity::getId).collect(Collectors.toSet());
                        applicationContext.getBean(serviceName, tableFieldPair.service()).deleteIfIdsNotFound(oldIds, newIds);
                    }
                }
            }
        }
    }

    protected void deleteIfIdsNotFound(Set<Long> oldIds, Set<Long> newIds) {
        Sets.SetView<Long> differentIds = Sets.difference(oldIds, newIds);
        for (Long toRemove : differentIds) {
            delete(toRemove);
        }
    }

    private void onDeleteRemoveChild(Long parentId) {
        Class<?> clazz = ReflectionHelper.getActualTypeArgumentFromGenericInterfaceWithProxiedClass(currentEntityRepository.getClass(), BaseEntity.class, BaseRepository.class);
        Annotation onDeleteRemoveChild = clazz.getDeclaredAnnotation(OnDeleteRemoveChild.class);
        if (onDeleteRemoveChild != null) {
            TableFieldPair[] tableFieldPairs = clazz.getDeclaredAnnotation(OnDeleteRemoveChild.class).value();
            for (TableFieldPair tableFieldPair : tableFieldPairs) {
                if (CrudService.class.isAssignableFrom(tableFieldPair.service())) {
                    String serviceName = tableFieldPair.service().getSimpleName();
                    serviceName = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, serviceName);
                    applicationContext.getBean(serviceName, tableFieldPair.service()).deleteByParentId(parentId, tableFieldPair.fieldName());
                }
            }
        }
    }

    protected void deleteByParentId(Long parentId, String fieldName) {
        String lowerCamelCase = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, fieldName);
        SpecSearchCriteria searchByParentId = new SpecSearchCriteria(null, lowerCamelCase, SearchOperation.EQUALITY, parentId);
        AppSpecification spec = new AppSpecification(searchByParentId);
        Iterable<T> entities = currentEntityRepository.findAll(spec);
        for (T baseEntity : entities) {
            delete(baseEntity.getId());
        }
    }

    private void onDeleteSetParentNull(Long parentId) {
        Class<?> clazz = ReflectionHelper.getActualTypeArgumentFromGenericInterfaceWithProxiedClass(currentEntityRepository.getClass(), BaseEntity.class, BaseRepository.class);
        Annotation onDeleteSetParentNullAnnotation = clazz.getDeclaredAnnotation(OnDeleteSetParentNull.class);
        if (onDeleteSetParentNullAnnotation != null) {
            TableFieldPair[] tableFieldPairs = clazz.getDeclaredAnnotation(OnDeleteSetParentNull.class).value();
            for (TableFieldPair tableFieldPair : tableFieldPairs) {
                if (CrudService.class.isAssignableFrom(tableFieldPair.service())) {
                    String serviceName = tableFieldPair.service().getSimpleName();
                    serviceName = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, serviceName);
                    applicationContext.getBean(serviceName, tableFieldPair.service()).setParentNull(parentId, tableFieldPair.fieldName());
                }
            }
        }
    }

    protected void setParentNull(Long parentId, String fieldName) {
        String lowerCamelCase = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, fieldName);
        SpecSearchCriteria searchByParentId = new SpecSearchCriteria(null, lowerCamelCase, SearchOperation.EQUALITY, parentId);
        AppSpecification spec = new AppSpecification(searchByParentId);
        Iterable<T> entities = currentEntityRepository.findAll(spec);
        for (T baseEntity : entities) {
            baseEntity.setAttribute(lowerCamelCase, null);
            currentEntityRepository.save(baseEntity);
        }
    }
}

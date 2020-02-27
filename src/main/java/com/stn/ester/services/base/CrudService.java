package com.stn.ester.services.base;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.CaseFormat;
import com.google.common.collect.Sets;
import com.stn.ester.core.base.*;
import com.stn.ester.core.exceptions.BadRequestException;
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
import org.springframework.data.repository.support.Repositories;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
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
        addManyToManyByArray(null, o);
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
        addManyToManyByArray(id, object);
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

    //untuk automatis menghapus data child yang tidak ada/diterima
    private void autoRemoveChild(Long parentId, T oldObject, T newObject, Map<String, Object> comparator) {
        Class<?> entityClass = ReflectionHelper.getActualTypeArgumentFromGenericInterfaceWithProxiedClass(currentEntityRepository.getClass(), BaseEntity.class, BaseRepository.class);
        AutoRemoveChild autoRemoveChildAnnotation = entityClass.getDeclaredAnnotation(AutoRemoveChild.class);
        AutoRemoveChild.List autoRemoveChildListAnnotation = entityClass.getDeclaredAnnotation(AutoRemoveChild.List.class);
        ArrayList<AutoRemoveChild> autoRemoveChildren = new ArrayList<>();
        if (autoRemoveChildAnnotation != null) {
            autoRemoveChildren.add(autoRemoveChildAnnotation);
        }
        if (autoRemoveChildListAnnotation != null) {
            autoRemoveChildren.addAll(Arrays.asList(autoRemoveChildListAnnotation.value()));
        }
        for (AutoRemoveChild autoRemoveChild : autoRemoveChildren) {
            if (comparator != null) {
                String attributeName = autoRemoveChild.attributeName();
                String fieldName = autoRemoveChild.fieldName();
                String attributeArrayName = autoRemoveChild.attributeArrayName();
                Object attributeOfNewObject = null;
                Object attributeOfOldObject = null;
                String serviceName = autoRemoveChild.service().getSimpleName();
                serviceName = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, serviceName);
                if (autoRemoveChild.type() == AutoRemoveChildType.ONETOMANY) {
                    attributeOfNewObject = newObject.getAttribute(attributeName, newObject);
                    attributeOfOldObject = newObject.getAttribute(attributeName, oldObject);
                } else if (autoRemoveChild.type() == AutoRemoveChildType.MANYTOMANY) {
                    attributeOfNewObject = newObject.getAttribute(attributeArrayName, newObject);
                    attributeOfOldObject = newObject.getAttribute(attributeName, oldObject);
                }
                Collection<Long> newIds;
                Collection<Long> oldIds;
                if (attributeOfNewObject != null
                        && Collection.class.isAssignableFrom(attributeOfNewObject.getClass())
                        && attributeOfOldObject != null
                        && Collection.class.isAssignableFrom(attributeOfOldObject.getClass())) {
                    if (autoRemoveChild.type() == AutoRemoveChildType.ONETOMANY
                            && comparator.containsKey(attributeName)) {
                        Collection<BaseEntity> newEntities = (Collection) attributeOfNewObject;
                        Collection<BaseEntity> oldEntities = (Collection) attributeOfOldObject;
                        newIds = newEntities.stream().map(BaseEntity::getId).collect(Collectors.toSet());
                        oldIds = oldEntities.stream().map(BaseEntity::getId).collect(Collectors.toSet());
                        applicationContext.getBean(serviceName, autoRemoveChild.service()).deleteIfIdsNotFound(oldIds, newIds);
                    } else if (autoRemoveChild.type() == AutoRemoveChildType.MANYTOMANY
                            && comparator.containsKey(attributeArrayName)) {
                        newIds = (Collection) attributeOfNewObject;
                        Collection<BaseEntity> oldEntities = (Collection) attributeOfOldObject;
                        oldIds = oldEntities.stream().map(entity -> (Long) entity.getAttribute(fieldName, entity)).filter(out -> out != null).collect(Collectors.toSet());
                        Sets.SetView<Long> differentIds = Sets.difference(new HashSet<Long>(oldIds), new HashSet<Long>(newIds));
                        String repositoryName = autoRemoveChild.repository().getSimpleName();
                        repositoryName = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, repositoryName);
                        if (differentIds.size() > 0) {
                            HashMap<Long, Long> foreignKeytoId = new HashMap<>();
                            for (BaseEntity entity : oldEntities) {
                                foreignKeytoId.put((Long) entity.getAttribute(fieldName, entity), entity.getId());
                            }
                            for (Long foreignKey : differentIds) {
                                applicationContext.getBean(repositoryName, autoRemoveChild.repository()).deleteById(foreignKeytoId.get(foreignKey));
                            }
                            oldObject.setAttribute(attributeName, null);
                        }
                    }
                }
            }
        }
    }


    protected void deleteIfIdsNotFound(Collection<Long> oldIds, Collection<Long> newIds) {
        Sets.SetView<Long> differentIds = Sets.difference(new HashSet<Long>(oldIds), new HashSet<Long>(newIds));
        for (Long toRemove : differentIds) {
            delete(toRemove);
        }
    }

    //jika suatu entity didelete, makanya semua child nya akan didelete juga
    private void onDeleteRemoveChild(Long parentId) {
        Class<?> entityClass = ReflectionHelper.getActualTypeArgumentFromGenericInterfaceWithProxiedClass(currentEntityRepository.getClass(), BaseEntity.class, BaseRepository.class);
        OnDeleteRemoveChild onDeleteRemoveChildAnnotation = entityClass.getDeclaredAnnotation(OnDeleteRemoveChild.class);
        OnDeleteRemoveChild.List onDeleteRemoveChildListAnnotation = entityClass.getDeclaredAnnotation(OnDeleteRemoveChild.List.class);
        ArrayList<OnDeleteRemoveChild> onDeleteRemoveChildren = new ArrayList<>();
        if (onDeleteRemoveChildAnnotation != null) {
            onDeleteRemoveChildren.add(onDeleteRemoveChildAnnotation);
        }
        if (onDeleteRemoveChildListAnnotation != null) {
            onDeleteRemoveChildren.addAll(Arrays.asList(onDeleteRemoveChildListAnnotation.value()));
        }
        for (OnDeleteRemoveChild onDeleteRemoveChild : onDeleteRemoveChildren) {
            if (CrudService.class.isAssignableFrom(onDeleteRemoveChild.service())) {
                String serviceName = onDeleteRemoveChild.service().getSimpleName();
                serviceName = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, serviceName);
                applicationContext.getBean(serviceName, onDeleteRemoveChild.service()).deleteByParentId(parentId, onDeleteRemoveChild.fieldName());
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

    //jika suatu entity didelete, maka entity lain yang memakai entity tersebut _id akan di set null
    //contoh
    //biodata terdapat countryId
    //jika ada data country yg dipakai di biodata dan data country tersebut didelete, makanya countryId di biodata akan di set null
    private void onDeleteSetParentNull(Long parentId) {
        Class<?> entityClass = ReflectionHelper.getActualTypeArgumentFromGenericInterfaceWithProxiedClass(currentEntityRepository.getClass(), BaseEntity.class, BaseRepository.class);
        OnDeleteSetParentNull onDeleteSetParentNullAnnotation = entityClass.getDeclaredAnnotation(OnDeleteSetParentNull.class);
        OnDeleteSetParentNull.List onDeleteSetParentNullListAnnotation = entityClass.getDeclaredAnnotation(OnDeleteSetParentNull.List.class);
        ArrayList<OnDeleteSetParentNull> onDeleteSetParentNulls = new ArrayList<>();
        if (onDeleteSetParentNullAnnotation != null) {
            onDeleteSetParentNulls.add(onDeleteSetParentNullAnnotation);
        }
        if (onDeleteSetParentNullListAnnotation != null) {
            onDeleteSetParentNulls.addAll(Arrays.asList(onDeleteSetParentNullListAnnotation.value()));
        }
        for (OnDeleteSetParentNull onDeleteSetParentNull : onDeleteSetParentNulls) {
            if (CrudService.class.isAssignableFrom(onDeleteSetParentNull.service())) {
                String serviceName = onDeleteSetParentNull.service().getSimpleName();
                serviceName = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, serviceName);
                applicationContext.getBean(serviceName, onDeleteSetParentNull.service()).setParentNull(parentId, onDeleteSetParentNull.fieldName());
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

    protected void addManyToManyByArray(Long id, BaseEntity newObject) {
        Class<?> entityClass = ReflectionHelper.getActualTypeArgumentFromGenericInterfaceWithProxiedClass(currentEntityRepository.getClass(), BaseEntity.class, BaseRepository.class);
        ManyToManyByArray manyToManyByArrayAnnotation = entityClass.getDeclaredAnnotation(ManyToManyByArray.class);
        ManyToManyByArray.List manyToManyByArrayListAnnotation = entityClass.getDeclaredAnnotation(ManyToManyByArray.List.class);
        ArrayList<ManyToManyByArray> manyToManyByArrays = new ArrayList<>();
        if (manyToManyByArrayAnnotation != null) {
            manyToManyByArrays.add(manyToManyByArrayAnnotation);
        }
        if (manyToManyByArrayListAnnotation != null) {
            manyToManyByArrays.addAll(Arrays.asList(manyToManyByArrayListAnnotation.value()));
        }
        Repositories repositories = new Repositories(applicationContext);
        for (ManyToManyByArray manyToManyByArray : manyToManyByArrays) {
            if (newObject.getAttribute(manyToManyByArray.attributeArrayName(), newObject) != null) {
                BaseRepository<BaseEntity> joinEntityRepository = (BaseRepository) repositories.getRepositoryFor(manyToManyByArray.joinEntity()).get();
                BaseRepository<BaseEntity> targetEntityRepository = (BaseRepository) repositories.getRepositoryFor(manyToManyByArray.targetEntity()).get();
                String attributeForeignKey = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, entityClass.getSimpleName()) + "Id";
                String targetForeignKey = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, manyToManyByArray.targetEntity().getSimpleName()) + "Id";
                Set<Long> currentJoinIds = new HashSet<>();
                if (id != null) {
                    AppSpecification spec = new AppSpecification(new SpecSearchCriteria(attributeForeignKey, SearchOperation.EQUALITY, id));
                    Collection<BaseEntity> oldJoinEntities = joinEntityRepository.findAll(spec);
                    currentJoinIds.addAll(oldJoinEntities.stream().map(entity -> (Long) entity.getAttribute(targetForeignKey, entity)).collect(Collectors.toSet()));
                }
                Set<Long> newIds = new HashSet<>((Collection<? extends Long>) newObject.getAttribute(manyToManyByArray.attributeArrayName(), newObject));
                Set<Long> newTargetIds = Sets.difference(newIds, currentJoinIds);
                for (Long targetId : newTargetIds) {
                    BaseEntity targetEntity = targetEntityRepository.findById(targetId).orElseThrow(() -> new BadRequestException(String.format("%s with id %d not found", manyToManyByArray.targetEntity().getSimpleName(), targetId)));
                    BaseEntity joinEntity = null;
                    try {
                        joinEntity = manyToManyByArray.joinEntity().newInstance();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    }
                    if (joinEntity != null) {
                        joinEntity.setAttribute(CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, manyToManyByArray.targetEntity().getSimpleName()), targetEntity);
                        joinEntity.setAttribute(CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, entityClass.getSimpleName()), newObject);
                        Collection var = ((Collection) newObject.getAttribute(manyToManyByArray.attributeName(), newObject));
                        if (var == null) {
                            var = new ArrayList();
                        }
                        var.add(joinEntity);
                        newObject.setAttribute(manyToManyByArray.attributeName(), var);
                    }
                }
            }
        }
    }
}

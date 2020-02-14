package com.stn.ester.core.validation.constraintvalidator;

import com.stn.ester.core.search.AppSpecification;
import com.stn.ester.core.search.util.SearchOperation;
import com.stn.ester.core.search.util.SpecSearchCriteria;
import com.stn.ester.core.validation.IsUnique;
import com.stn.ester.entities.base.BaseEntity;
import com.stn.ester.repositories.jpa.base.BaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.support.Repositories;

import javax.persistence.EntityManager;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;

public class IsUniqueValidator implements ConstraintValidator<IsUnique, BaseEntity> {

    String columnNames;
    ApplicationContext applicationContext;
    Repositories repositories;
    EntityManager entityManager;

    @Autowired
    public IsUniqueValidator(ApplicationContext applicationContext, EntityManager entityManager) {
        this.entityManager = entityManager;
        this.applicationContext = applicationContext;
        this.repositories = new Repositories(applicationContext);
    }

    @Override
    public void initialize(IsUnique constraintAnnotation) {
        columnNames = constraintAnnotation.columnNames();
    }

    @Override
    public boolean isValid(BaseEntity s, ConstraintValidatorContext constraintValidatorContext) {
        Class<?> clazz = s.getClass();
        Class<?> baseClazz = clazz.getSuperclass();
        try {
            Field field = clazz.getDeclaredField(columnNames);
            Field fieldId = baseClazz.getDeclaredField("id");
            field.setAccessible(true);
            fieldId.setAccessible(true);
            Specification specification = new AppSpecification(new SpecSearchCriteria(columnNames, SearchOperation.EQUALITY, field.get(s)));
            Object o = fieldId.get(s);
            if (fieldId.get(s) != null) {
                specification = specification.and(new AppSpecification(new SpecSearchCriteria("id", SearchOperation.NEGATION, fieldId.get(s))));
            }
            long count = ((BaseRepository) repositories.getRepositoryFor(clazz).get()).count(specification);
            return count == 0;
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return false;
    }
}

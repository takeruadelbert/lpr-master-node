package com.stn.ester.core.validation.constraintvalidator;

import com.stn.ester.core.search.AppSpecification;
import com.stn.ester.core.search.util.SearchOperation;
import com.stn.ester.core.search.util.SpecSearchCriteria;
import com.stn.ester.core.validation.IsUnique;
import com.stn.ester.entities.base.BaseEntity;
import com.stn.ester.repositories.jpa.base.BaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.repository.support.Repositories;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IsUniqueValidator implements ConstraintValidator<IsUnique, String> {

    Class<? extends BaseEntity> entityClass;
    String key;
    ApplicationContext applicationContext;
    Repositories repositories;

    @Autowired
    public IsUniqueValidator(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        this.repositories = new Repositories(applicationContext);
    }

    @Override
    public void initialize(IsUnique constraintAnnotation) {
        key = constraintAnnotation.key();
        entityClass = constraintAnnotation.entityClass();
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        AppSpecification specification = new AppSpecification(new SpecSearchCriteria(key, SearchOperation.EQUALITY, s));
        long count = ((BaseRepository) repositories.getRepositoryFor(entityClass).get()).count(specification);
        return count == 0;
    }
}

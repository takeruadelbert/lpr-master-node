package com.stn.ester.rest.search;

import com.stn.ester.rest.domain.AppDomain;
import com.stn.ester.rest.exception.FilterException;
import com.stn.ester.rest.search.util.SpecSearchCriteria;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.Calendar;
import java.util.Date;

public class AppSpecification<T extends AppDomain> implements Specification<T> {

    private SpecSearchCriteria criteria;

    public AppSpecification() {
    }

    public AppSpecification(final SpecSearchCriteria criteria) {
        super();
        this.criteria = criteria;
    }

    public SpecSearchCriteria getCriteria() {
        return criteria;
    }

    @Override
    public Predicate toPredicate(final Root<T> root, final CriteriaQuery<?> query, final CriteriaBuilder builder) {
        try {
            Path path;
            if (criteria.getClassJoin() == null) {
                path = root.get(criteria.getKey());
            } else {
                Join joinPredicate = root.join(getCriteria().getClassJoin());
                path = joinPredicate.get(criteria.getKey());
            }
            Object value;
            if (path.getModel().getBindableJavaType().isAssignableFrom(Date.class)){
                value=new Date(criteria.getValue().toString());
            }else{
                value=criteria.getValue();
            }

            switch (criteria.getOperation()) {
                case EQUALITY:
                    return builder.equal(path,value);
                case NEGATION:
                    return builder.notEqual(path, value);
                case GREATER_THAN:
                    return builder.greaterThan(path, value.toString());
                case LESS_THAN:
                    return builder.lessThan(path, value.toString());
                case LIKE:
                    return builder.like(path, value.toString());
                case STARTS_WITH:
                    return builder.like(path, value + "%");
                case ENDS_WITH:
                    return builder.like(path, "%" + value);
                case CONTAINS:
                    return builder.like(path, "%" + value + "%");
                default:
                    return null;
            }
        } catch (IllegalArgumentException illegal) {
            throw new FilterException(criteria.getKey());
        }


    }
}

package com.stn.ester.rest.search;

import com.stn.ester.rest.domain.AppDomain;
import com.stn.ester.rest.exception.FilterException;
import com.stn.ester.rest.search.util.SpecSearchCriteria;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;

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
            switch (criteria.getOperation()) {
                case EQUALITY:
                    return builder.equal(path, criteria.getValue());
                case NEGATION:
                    return builder.notEqual(path, criteria.getValue());
                case GREATER_THAN:
                    return builder.greaterThan(path, criteria.getValue().toString());
                case LESS_THAN:
                    return builder.lessThan(path, criteria.getValue().toString());
                case LIKE:
                    return builder.like(path, criteria.getValue().toString());
                case STARTS_WITH:
                    return builder.like(path, criteria.getValue() + "%");
                case ENDS_WITH:
                    return builder.like(path, "%" + criteria.getValue());
                case CONTAINS:
                    return builder.like(path, "%" + criteria.getValue() + "%");
                default:
                    return null;
            }
        } catch (IllegalArgumentException illegal) {
            throw new FilterException(criteria.getKey());
        }


    }
}

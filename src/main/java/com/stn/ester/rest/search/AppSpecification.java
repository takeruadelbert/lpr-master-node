package com.stn.ester.rest.search;

import com.stn.ester.rest.domain.AppDomain;
import com.stn.ester.rest.exception.FilterException;
import com.stn.ester.rest.search.util.SpecSearchCriteria;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

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
            switch (criteria.getOperation()) {
                case EQUALITY:
                    return builder.equal(root.get(criteria.getKey()), criteria.getValue());
                case NEGATION:
                    return builder.notEqual(root.get(criteria.getKey()), criteria.getValue());
                case GREATER_THAN:
                    return builder.greaterThan(root.get(criteria.getKey()), criteria.getValue().toString());
                case LESS_THAN:
                    return builder.lessThan(root.get(criteria.getKey()), criteria.getValue().toString());
                case LIKE:
                    return builder.like(root.get(criteria.getKey()), criteria.getValue().toString());
                case STARTS_WITH:
                    return builder.like(root.get(criteria.getKey()), criteria.getValue() + "%");
                case ENDS_WITH:
                    return builder.like(root.get(criteria.getKey()), "%" + criteria.getValue());
                case CONTAINS:
                    return builder.like(root.get(criteria.getKey()), "%" + criteria.getValue() + "%");
                default:
                    return null;
            }
        } catch (IllegalArgumentException illegal) {
            throw new FilterException(criteria.getKey());
        }


    }
}

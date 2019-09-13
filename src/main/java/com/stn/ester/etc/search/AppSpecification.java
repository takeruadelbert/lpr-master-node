package com.stn.ester.etc.search;

import com.stn.ester.entities.base.BaseEntity;
import com.stn.ester.etc.exceptions.FilterException;
import com.stn.ester.helpers.DateTimeHelper;
import com.stn.ester.etc.search.util.SpecSearchCriteria;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

public class AppSpecification<T extends BaseEntity> implements Specification<T> {

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
            if (LocalDate.class.isAssignableFrom(path.getModel().getBindableJavaType())) {
                value = DateTimeHelper.convertToDate(criteria.getValue().toString());
            } else if (LocalDateTime.class.isAssignableFrom(path.getModel().getBindableJavaType())) {
                value = DateTimeHelper.convertToDateTime(criteria.getValue().toString());
            } else if (Enum.class.isAssignableFrom(path.getModel().getBindableJavaType())) {
                value=Enum.valueOf(path.getModel().getBindableJavaType(),criteria.getValue().toString());
            } else {
                value = criteria.getValue();
            }
            switch (criteria.getOperation()) {
                case EQUALITY:
                    return builder.equal(path, value);
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
        } catch (DateTimeParseException e) {
            throw new FilterException();
        }
    }
}

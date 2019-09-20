package com.stn.ester.core.search;

import com.stn.ester.entities.base.BaseEntity;
import com.stn.ester.core.exceptions.FilterException;
import com.stn.ester.helpers.DateTimeHelper;
import com.stn.ester.core.search.util.SpecSearchCriteria;
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
            Path path = getPathOrJoinedPath(root);
            Comparable value = parseValue(path);
            return createPredicate(builder, path, value);
        } catch (IllegalArgumentException illegal) {
            throw new FilterException(criteria.getKey());
        } catch (DateTimeParseException e) {
            throw new FilterException();
        }
    }

    private Path getPathOrJoinedPath(Root<T> root) {
        if (criteria.getClassJoin() == null) {
            return root.get(criteria.getKey());
        } else {
            Join joinPredicate = root.join(getCriteria().getClassJoin());
            return joinPredicate.get(criteria.getKey());
        }
    }

    private Comparable parseValue(Path path) {
        if (LocalDate.class.isAssignableFrom(path.getModel().getBindableJavaType())) {
            return DateTimeHelper.convertToDate(criteria.getValue().toString());
        } else if (LocalDateTime.class.isAssignableFrom(path.getModel().getBindableJavaType())) {
            return DateTimeHelper.convertToDateTime(criteria.getValue().toString());
        } else if (Enum.class.isAssignableFrom(path.getModel().getBindableJavaType())) {
            return Enum.valueOf(path.getModel().getBindableJavaType(), criteria.getValue().toString());
        } else {
            return criteria.getValue().toString();
        }
    }

    private Predicate createPredicate(CriteriaBuilder builder, Path path, Comparable value) {
        switch (criteria.getOperation()) {
            case EQUALITY:
                return builder.equal(path, value);
            case NEGATION:
                return builder.notEqual(path, value);
            case GREATER_THAN:
                return builder.greaterThan(path, value);
            case LESS_THAN:
                return builder.lessThan(path, value);
            case GREATER_THAN_OR_EQUAL:
                return builder.greaterThanOrEqualTo(path, value);
            case LESS_THAN_OR_EQUAL:
                return builder.lessThanOrEqualTo(path, value);
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
    }
}

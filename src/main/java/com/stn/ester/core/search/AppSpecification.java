package com.stn.ester.core.search;

import com.stn.ester.core.exceptions.BadRequestException;
import com.stn.ester.core.exceptions.FilterException;
import com.stn.ester.core.search.util.SpecSearchCriteria;
import com.stn.ester.entities.base.BaseEntity;
import com.stn.ester.helpers.DateTimeHelper;
import com.stn.ester.helpers.GlobalFunctionHelper;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

import static com.stn.ester.core.search.util.SearchOperation.EQUAL_DATE_WITH_DATETIME;
import static com.stn.ester.core.search.util.SearchOperation.IN;

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
            ValueWrap valueWrap = parseValue(path);
            query.distinct(true);
            return createPredicate(builder, path, valueWrap);
        } catch (IllegalArgumentException illegal) {
            throw new FilterException(criteria.getKey());
        } catch (DateTimeParseException e) {
            throw new BadRequestException(String.format("Wrong format for %s", criteria.getKey()));
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

    private ValueWrap parseValue(Path path) {
        if (LocalDate.class.isAssignableFrom(path.getModel().getBindableJavaType())) {
            return new ValueWrap(DateTimeHelper.convertToDate(criteria.getValue().toString()));
        } else if (LocalDateTime.class.isAssignableFrom(path.getModel().getBindableJavaType())) {
            if (criteria.getValue().toString().length() == 10) {
                return new ValueWrap(DateTimeHelper.convertToDate(criteria.getValue().toString()), true);
            } else {
                return new ValueWrap(DateTimeHelper.convertToDateTime(criteria.getValue().toString()));
            }
        } else if (Enum.class.isAssignableFrom(path.getModel().getBindableJavaType())) {
            return new ValueWrap(Enum.valueOf(path.getModel().getBindableJavaType(), criteria.getValue().toString()));
        } else if (criteria.getOperation() == IN) {
            return new ValueWrap(GlobalFunctionHelper.stringCommaToList(criteria.getValue().toString()));
        } else {
            return new ValueWrap(criteria.getValue().toString());
        }
    }

    private Predicate createPredicate(CriteriaBuilder builder, Path path, ValueWrap valueWrap) {
        switch (criteria.getOperation()) {
            case EQUALITY:
            case EQUAL_DATE_WITH_DATETIME:
                if (valueWrap.isDateCompareToDatetime || criteria.getOperation() == EQUAL_DATE_WITH_DATETIME) {
                    return builder.and(builder.greaterThanOrEqualTo(path, ((LocalDate) valueWrap.getValue()).atStartOfDay()), builder.lessThan(path, ((LocalDate) valueWrap.getValue()).plusDays(1).atStartOfDay()));
                } else {
                    return builder.equal(path, valueWrap.getValue());
                }
            case NEGATION:
                return builder.notEqual(path, valueWrap.getValue());
            case GREATER_THAN:
                if (valueWrap.isDateCompareToDatetime) {
                    return builder.greaterThan(path, ((LocalDate) valueWrap.getValue()).atTime(LocalTime.MAX));
                } else {
                    return builder.greaterThan(path, valueWrap.getValue());
                }
            case LESS_THAN:
                if (valueWrap.isDateCompareToDatetime) {
                    return builder.lessThan(path, ((LocalDate) valueWrap.getValue()).atStartOfDay());
                } else {
                    return builder.lessThan(path, valueWrap.getValue());
                }
            case GREATER_THAN_OR_EQUAL:
                if (valueWrap.isDateCompareToDatetime) {
                    return builder.greaterThanOrEqualTo(path, ((LocalDate) valueWrap.getValue()).atStartOfDay());
                } else {
                    return builder.greaterThanOrEqualTo(path, valueWrap.getValue());
                }
            case LESS_THAN_OR_EQUAL:
                if (valueWrap.isDateCompareToDatetime) {
                    return builder.lessThanOrEqualTo(path, ((LocalDate) valueWrap.getValue()).atTime(LocalTime.MAX));
                } else {
                    return builder.lessThanOrEqualTo(path, valueWrap.getValue());
                }
            case LIKE:
                return builder.like(path, valueWrap.getValue().toString());
            case STARTS_WITH:
                return builder.like(path, valueWrap.getValue() + "%");
            case ENDS_WITH:
                return builder.like(path, "%" + valueWrap.getValue());
            case CONTAINS:
                return builder.like(path, "%" + valueWrap.getValue() + "%");
            case IN:
                return path.in(valueWrap.getValues());
            default:
                return null;
        }
    }

    class ValueWrap {
        Comparable value;
        Boolean isDateCompareToDatetime;
        ArrayList values;

        ValueWrap(Comparable value) {
            this(value, false);
        }

        ValueWrap(Comparable value, Boolean isDateCompareToDatetime) {
            this.value = value;
            this.isDateCompareToDatetime = isDateCompareToDatetime;
        }

        ValueWrap(ArrayList values) {
            this.values = values;
        }

        public ArrayList getValues() {
            return this.values;
        }

        public Comparable getValue() {
            return value;
        }

        public Boolean getDateCompareToDatetime() {
            return isDateCompareToDatetime;
        }
    }
}

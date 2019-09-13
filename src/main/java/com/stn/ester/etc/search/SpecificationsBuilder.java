package com.stn.ester.etc.search;

import com.stn.ester.entities.base.BaseEntity;
import com.stn.ester.etc.search.util.SearchOperation;
import com.stn.ester.etc.search.util.SpecSearchCriteria;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class SpecificationsBuilder<T extends BaseEntity> {
    private final List<SpecSearchCriteria> params;

    public SpecificationsBuilder() {
        params = new ArrayList<>();
    }

    public final SpecificationsBuilder with(final String key, final String operation, final Object value, final String prefix, final String suffix) {
        return with(null, key, operation, value, prefix, suffix, null);
    }

    public final SpecificationsBuilder with(final String orPredicate, final String key, final String operation, final Object value, final String prefix, final String suffix, final String joinClass) {
        SearchOperation op = SearchOperation.getSimpleOperation(operation.charAt(0));
        if (op != null) {
            if (op == SearchOperation.EQUALITY) { // the operation may be complex operation
                final boolean startWithAsterisk = prefix != null && prefix.contains(SearchOperation.ZERO_OR_MORE_REGEX);
                final boolean endWithAsterisk = suffix != null && suffix.contains(SearchOperation.ZERO_OR_MORE_REGEX);

                if (startWithAsterisk && endWithAsterisk) {
                    op = SearchOperation.CONTAINS;
                } else if (startWithAsterisk) {
                    op = SearchOperation.ENDS_WITH;
                } else if (endWithAsterisk) {
                    op = SearchOperation.STARTS_WITH;
                }
            }
            params.add(new SpecSearchCriteria(orPredicate, key, op, value, joinClass));
        }
        return this;
    }

    public final SpecificationsBuilder with(final String key, final String operation, final Object value, final String prefix, final String suffix, final String joinClass) {
        return with(null, key, operation, value, prefix, suffix, joinClass);
    }

    public Specification<T> build() {
        if (params.size() == 0)
            return null;

        Specification<T> result = new AppSpecification(params.get(0));

        for (int i = 1; i < params.size(); i++) {
            result = params.get(i).isOrPredicate()
                    ? Specification.where(result).or(new AppSpecification(params.get(i)))
                    : Specification.where(result).and(new AppSpecification(params.get(i)));
        }

        return result;
    }

    public final SpecificationsBuilder with(AppSpecification spec) {
        params.add(spec.getCriteria());
        return this;
    }

    public final SpecificationsBuilder with(SpecSearchCriteria criteria) {
        params.add(criteria);
        return this;
    }

    @Override
    public String toString() {
        String result = "";
        for (SpecSearchCriteria param : params) {
            result += param.toString();
        }
        return result;
    }

}

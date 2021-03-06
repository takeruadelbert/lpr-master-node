package com.stn.ester.helpers;

import com.stn.ester.core.search.AppSpecification;
import com.stn.ester.core.search.SpecificationsBuilder;
import com.stn.ester.core.search.util.SearchOperation;
import com.stn.ester.core.search.util.SpecSearchCriteria;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.jpa.domain.Specification;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

@Log4j2
public class SearchAndFilterHelper {

    //input as string jsonobject
    public static Specification resolveSpecification(String searchParameters) {
        SpecificationsBuilder builder = new SpecificationsBuilder<>();
        try {
            SearchAndFilterHelper.loopSpecToSpecBuilder(builder, GlobalFunctionHelper.jsonStringToMap(searchParameters), null);
        } catch (IOException ex) {
            log.trace(ex);
        }
        return builder.build();
    }

    public static Specification resolveSpecification(Map<String, Object> searchMap) {
        SpecificationsBuilder builder = new SpecificationsBuilder<>();
        SearchAndFilterHelper.loopSpecToSpecBuilder(builder, searchMap, null);
        return builder.build();
    }

    public static Specification joinSpecification(Specification currentSpecification, Collection<AppSpecification> appSpecifications) {
        for (AppSpecification appSpecification : appSpecifications) {
            currentSpecification = appSpecification.getCriteria().isOrPredicate()
                    ? Specification.where(currentSpecification).or(appSpecification)
                    : Specification.where(currentSpecification).and(appSpecification);
        }
        return currentSpecification;
    }

    public static Specification resolveSpecificationSingleKeyword(Collection<String> keys, String keyword) {
        SpecificationsBuilder builder = new SpecificationsBuilder<>();
        for (String key : keys) {
            String[] explodedKey = key.split("\\.");
            if (explodedKey.length == 1) {
                builder.with(new AppSpecification(new SpecSearchCriteria(SearchOperation.OR_PREDICATE_FLAG, key, SearchOperation.CONTAINS, keyword)));
            } else if (explodedKey.length == 2) {
                builder.with(new AppSpecification(new SpecSearchCriteria(SearchOperation.OR_PREDICATE_FLAG, explodedKey[1], SearchOperation.CONTAINS, keyword, explodedKey[0])));
            }
        }
        return builder.build();
    }

    private static void loopSpecToSpecBuilder(SpecificationsBuilder specificationsBuilder, Map<String, Object> jsonMap, String className) {
        for (Map.Entry<String, Object> entry : jsonMap.entrySet()) {
            if (Map.class.isAssignableFrom(entry.getValue().getClass())) {
                String joinClass = entry.getKey();
                if (className != null) {
                    joinClass = className + "," + joinClass;
                }
                SearchAndFilterHelper.loopSpecToSpecBuilder(specificationsBuilder, (Map<String, Object>) entry.getValue(), joinClass);
            } else {
                SearchOperation operation = SearchOperation.getOperation(entry.getKey());
                String key = getKey(entry.getKey(), operation);
                specificationsBuilder.with(key, operation, entry.getValue().toString(), null, null, className);
            }
        }
    }

    private static String getKey(String searchField, SearchOperation searchOperation) {
        String key = "";
        if (searchOperation != null) {
            String searchOperationSymbol = SearchOperation.OPERATION_SYMBOL_MAP.get(searchOperation);
            key = searchField.substring(searchOperationSymbol.length());
        }
        return key;
    }
}

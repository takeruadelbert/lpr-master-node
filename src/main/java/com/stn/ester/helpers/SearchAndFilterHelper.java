package com.stn.ester.helpers;

import com.stn.ester.core.search.SpecificationsBuilder;
import com.stn.ester.core.search.util.SearchOperation;
import org.springframework.data.jpa.domain.Specification;

import java.io.IOException;
import java.util.Map;

public class SearchAndFilterHelper {

    public static Specification resolveSpecification(String searchParameters) {
        SpecificationsBuilder builder = new SpecificationsBuilder<>();
        try {
            SearchAndFilterHelper.loopSpecToSpecBuilder(builder, GlobalFunctionHelper.jsonStringToMap(searchParameters), null);
        } catch (IOException ex) {
            System.out.println(ex);
        }
        return builder.build();
    }

    private static void loopSpecToSpecBuilder(SpecificationsBuilder specificationsBuilder, Map<String, Object> jsonMap, String className) {
        for (Map.Entry<String, Object> entry : jsonMap.entrySet()) {
            if (Map.class.isAssignableFrom(entry.getValue().getClass())) {
                SearchAndFilterHelper.loopSpecToSpecBuilder(specificationsBuilder, (Map<String, Object>) entry.getValue(), entry.getKey());
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

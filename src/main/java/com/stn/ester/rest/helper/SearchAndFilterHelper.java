package com.stn.ester.rest.helper;

import com.stn.ester.rest.search.SpecificationsBuilder;
import org.springframework.data.jpa.domain.Specification;

import java.io.IOException;
import java.util.Map;

public class SearchAndFilterHelper {

    public static Specification resolveSpecification(String searchParameters) {
        SpecificationsBuilder builder = new SpecificationsBuilder<>();
        try {
            SearchAndFilterHelper.loopSpecToSpecBuilder(builder, GlobalFunctionHelper.jsonStringToMap(searchParameters));
        } catch (IOException ex) {
            System.out.println(ex);
        }
        return builder.build();
    }

    private static void loopSpecToSpecBuilder(SpecificationsBuilder specificationsBuilder, Map<String, Object> jsonMap) {
        for (Map.Entry<String, Object> entry : jsonMap.entrySet()) {
            if (Map.class.isAssignableFrom(entry.getValue().getClass())) {
                SearchAndFilterHelper.loopSpecToSpecBuilder(specificationsBuilder, (Map<String, Object>) entry.getValue());
            } else {
                String operation = entry.getKey().substring(0, 1);
                String key = entry.getKey().substring(1);
                specificationsBuilder.with(key, operation, entry.getValue().toString(), null, null);
            }
        }
    }
}

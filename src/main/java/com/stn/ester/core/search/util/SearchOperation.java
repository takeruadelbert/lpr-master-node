package com.stn.ester.core.search.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public enum SearchOperation {
    EQUALITY, NEGATION, GREATER_THAN, LESS_THAN, CONTAINS, GREATER_THAN_OR_EQUAL, LESS_THAN_OR_EQUAL, LIKE, STARTS_WITH, ENDS_WITH, EQUAL_DATE_WITH_DATETIME,IN;

    public static final Map<SearchOperation, String> OPERATION_SYMBOL_MAP;

    static {
        Map<SearchOperation, String> operationMap = new HashMap<>();
        operationMap.put(EQUALITY, ":");
        operationMap.put(NEGATION, "!");
        operationMap.put(GREATER_THAN, ">");
        operationMap.put(LESS_THAN, "<");
        operationMap.put(CONTAINS, "~");
        operationMap.put(GREATER_THAN_OR_EQUAL, ">:");
        operationMap.put(LESS_THAN_OR_EQUAL, "<:");
        operationMap.put(EQUAL_DATE_WITH_DATETIME, "d:");
        operationMap.put(IN,"i:");
        OPERATION_SYMBOL_MAP = Collections.unmodifiableMap(operationMap);
    }

    public static final String OR_PREDICATE_FLAG = "'";

    public static final String ZERO_OR_MORE_REGEX = "*";

    public static final String OR_OPERATOR = "OR";

    public static final String AND_OPERATOR = "AND";

    public static final String LEFT_PARANTHESIS = "(";

    public static final String RIGHT_PARANTHESIS = ")";

    @Deprecated
    private static SearchOperation getSimpleOperation(final String input) {
        for (Map.Entry<SearchOperation, String> entry : OPERATION_SYMBOL_MAP.entrySet()) {
            if (entry.getValue().equals(input)) {
                return entry.getKey();
            }
        }
        return null;
    }

    public static SearchOperation getOperation(final String key) {
        int endString = 2;
        String operationString = key.substring(0, endString);
        SearchOperation operation = null;
        if (operationString.length() == endString) {
            operation = getSimpleOperation(operationString);
        }
        if (operation == null) {
            operationString = key.substring(0, 1);
            operation = getSimpleOperation(operationString);
        }
        return operation;
    }
}

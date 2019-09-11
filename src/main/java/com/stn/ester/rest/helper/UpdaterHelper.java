package com.stn.ester.rest.helper;

import com.google.common.collect.Sets;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class UpdaterHelper {

    @Autowired
    public UpdaterHelper() {

    }

    //https://stackoverflow.com/questions/27818334/jpa-update-only-specific-fields
    public static void copyNonNullProperties(Object src, Object target) {
        BeanUtils.copyProperties(src, target, getNullPropertyNames(src));
    }

    public static String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<String>();
        for (java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) emptyNames.add(pd.getName());
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    public static String[] getIgnoredProperty(Object source, Map<String, Object> comporator) {
        String[] emptyNames = UpdaterHelper.getNullPropertyNames(source);
        Set<String> ignoredProperty = Sets.newHashSet(emptyNames);
        if (comporator != null) {
            for (String emptyName : emptyNames) {
                if (comporator.containsKey(emptyName) && (comporator.get(emptyName) == null)) {
                    ignoredProperty.remove(emptyName);
                }
            }
        }
        return ignoredProperty.toArray(new String[ignoredProperty.size()]);
    }

}

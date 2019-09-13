package com.stn.ester.helpers;

import com.stn.ester.repositories.jpa.base.AppRepository;
import com.stn.ester.repositories.jpa.projections.OptionList;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class ReflectionHelper {

    public static Class getActualTypeArgumentFromGenericInterface(Class thisClass, Class toCheck) {
        Class[] interfazes = thisClass.getInterfaces();
        for (Class interfaze : interfazes) {
            if (AppRepository.class.isAssignableFrom(interfaze)) {
                Type[] genericInterfaces = interfaze.getGenericInterfaces();
                for (Type genericInterface : genericInterfaces) {
                    if (genericInterface instanceof ParameterizedType) {
                        Type[] actualTypeArguments = ((ParameterizedType) genericInterface).getActualTypeArguments();
                        for (Type actualTypeArgument : actualTypeArguments) {
                            if (actualTypeArgument instanceof Class) {
                                Class<?> clazz = (Class<?>) actualTypeArgument;
                                if (toCheck.isAssignableFrom(clazz)) {
                                    return clazz;
                                }
                            }
                        }
                    }
                }
            }
        }
        return null;
    }
}

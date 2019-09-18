package com.stn.ester.helpers;

import com.stn.ester.repositories.jpa.base.BaseRepository;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class ReflectionHelper {

    public static Class getActualTypeArgumentFromGenericInterfaceWithProxiedClass(Class thisClass, Class toCheck, Class fromInterface) {
        Class[] interfazes = thisClass.getInterfaces();
        for (Class interfaze : interfazes) {
            if (fromInterface.isAssignableFrom(interfaze)) {
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

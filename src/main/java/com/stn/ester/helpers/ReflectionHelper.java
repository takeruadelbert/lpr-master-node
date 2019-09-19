package com.stn.ester.helpers;

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

    public static Class getActualTypeArgumentFromGenericInterface(Class thisClass, Class toCheck, Class fromInterface) {
        Type[] interfazes = thisClass.getGenericInterfaces();
        Class[] clazzs = thisClass.getInterfaces();
        for (int i = 0; i < interfazes.length; i++) {
            if (fromInterface.isAssignableFrom(clazzs[i]) && interfazes[i] instanceof ParameterizedType) {
                Type[] actualTypeArguments = ((ParameterizedType) interfazes[i]).getActualTypeArguments();
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
        return null;
    }
}

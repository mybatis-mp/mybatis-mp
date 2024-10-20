/*
 *  Copyright (c) 2022-2023, Mybatis-Flex (fuhai999@gmail.com).
 *  <p>
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *  <p>
 *  http://www.apache.org/licenses/LICENSE-2.0
 *  <p>
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package db.sql.api.impl.tookit;


import db.sql.api.GetterFun;

import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class LambdaUtil {

    private static final Map<GetterFun, LambdaFieldInfo> LAMBDA_GETTER_FIELD_MAP = new ConcurrentHashMap<>(65535);

    private static final Map<String, Class<?>> CLASS_MAP = new ConcurrentHashMap();


    private LambdaUtil() {

    }

    public static <T, R> String getName(GetterFun<T, R> getter) {
        return getFieldInfo(getter).getName();
    }

    private static LambdaFieldInfo getLambdaFieldInfo(SerializedLambda serializedLambda, ClassLoader classLoader) {
        Class type = getClass(serializedLambda, classLoader);
        String methodName = serializedLambda.getImplMethodName();
        String fieldName = PropertyNamer.methodToProperty(methodName);
        return new LambdaFieldInfo(type, fieldName);
    }

    public static <T, R> LambdaFieldInfo getFieldInfo(GetterFun<T, R> getter) {
        return LAMBDA_GETTER_FIELD_MAP.computeIfAbsent(getter, (key) -> getLambdaFieldInfo(getSerializedLambda(getter), getter.getClass().getClassLoader()));
    }

    private static <T, R> SerializedLambda getSerializedLambda(GetterFun<T, R> getter) {
        try {
            Method method = getter.getClass().getDeclaredMethod("writeReplace");
            method.setAccessible(Boolean.TRUE);
            return (SerializedLambda) method.invoke(getter);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static <T> Class<T> getClass(SerializedLambda lambda, ClassLoader classLoader) {
        String classNamePath = getClassNamePath(lambda);
        return (Class<T>) CLASS_MAP.computeIfAbsent(classNamePath, key -> {
            try {
                return Class.forName(getClassName(key), false, classLoader);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private static String getClassName(String classNamePath) {
        return classNamePath.replace("/", ".");
    }

    private static String getClassNamePath(SerializedLambda lambda) {
        String type = lambda.getInstantiatedMethodType();
        return type.substring(2, type.indexOf(";"));
    }

    public static void main(String[] args) {
        System.out.println(getName(LambdaFieldInfo::getName));
        System.out.println(getName(LambdaFieldInfo::getName));
        System.out.println(LAMBDA_GETTER_FIELD_MAP.size());
    }

    public static class LambdaFieldInfo {

        private final Class type;
        private final String name;

        public LambdaFieldInfo(Class type, String name) {
            this.type = type;
            this.name = name;
        }

        public Class getType() {
            return type;
        }

        public String getName() {
            return name;
        }
    }

}

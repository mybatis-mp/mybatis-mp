package cn.mybatis.mp.core;

public class NotTableFieldException extends RuntimeException {

    public NotTableFieldException(Class clazz, String fieldName) {
        super("the field: " + fieldName + " not found in " + clazz.getName());
    }
}

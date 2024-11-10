package cn.mybatis.mp.core.util;

import cn.mybatis.mp.core.db.reflect.CreatedEventInfo;
import db.sql.api.impl.tookit.Objects;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public class CreatedEventUtil {

    public static void onCreated(Object rowValue, Map context, CreatedEventInfo createdEventInfo) {
        if (Objects.isNull(rowValue)) {
            return;
        }
        try {
            if (createdEventInfo.isHasContextParam()) {
                createdEventInfo.getMethod().invoke(null, context, rowValue);
            } else {
                createdEventInfo.getMethod().invoke(null, rowValue);
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}

package cn.mybatis.mp.core.util;

import cn.mybatis.mp.core.db.reflect.*;
import cn.mybatis.mp.db.Model;

import java.io.Serializable;

public final class ModelInfoUtil {
    public static void setValue(ModelFieldInfo modelFieldInfo, Object target, Object value) {
        try {
            modelFieldInfo.getWriteFieldInvoker().invoke(target, new Object[]{value});
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 从实体类的Model类中获取ID
     *
     * @param model 实体类的model类实例
     * @return 返回ID
     */
    public static Serializable getEntityIdValue(Model model) {
        return getEntityIdValue(Models.get(model.getClass()), model, false);
    }

    /**
     * 从实体类中获取ID
     *
     * @param modelInfo Model类信息
     * @param model     实体类的model类实例
     * @return 返回ID
     */
    public static Serializable getEntityIdValue(ModelInfo modelInfo, Model model) {
        return getEntityIdValue(modelInfo, model, true);
    }

    /**
     * 从实体类中获取ID
     *
     * @param modelInfo Model类信息
     * @param model     实体类的model类实例
     * @param check     是否检查
     * @return 返回ID
     */
    public static Serializable getEntityIdValue(ModelInfo modelInfo, Model model, boolean check) {
        return getEntityIdValue(modelInfo, Tables.get(modelInfo.getEntityType()), model, check);
    }

    /**
     * 从Model类中获取ID
     *
     * @param modelInfo Model类信息
     * @param model     实体类的model类实例
     * @return 返回ID
     */
    public static Serializable getEntityIdValue(ModelInfo modelInfo, TableInfo tableInfo, Model model, boolean check) {
        if (check) {
            if (model.getClass() != modelInfo.getType()) {
                throw new RuntimeException("Not Supported");
            }
        }
        TableInfoUtil.checkId(tableInfo);
        Serializable id;
        try {
            id = (Serializable) modelInfo.getIdFieldInfo().getReadFieldInvoker().invoke(model, null);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return id;
    }
}

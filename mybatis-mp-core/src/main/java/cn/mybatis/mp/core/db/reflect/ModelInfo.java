package cn.mybatis.mp.core.db.reflect;

import cn.mybatis.mp.core.NotTableClassException;
import cn.mybatis.mp.core.util.FieldUtil;
import cn.mybatis.mp.core.util.GenericUtil;
import cn.mybatis.mp.db.annotations.Table;
import db.sql.api.impl.tookit.Objects;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ModelInfo {

    /**
     * 对应的类
     */
    private final Class<?> type;

    /**
     * 对应实体类的type
     */
    private final Class<?> entityType;

    /**
     * 表信息
     */
    private final TableInfo tableInfo;

    /**
     * 所有 字段
     */
    private final List<ModelFieldInfo> modelFieldInfos;

    /**
     * 字段个数
     */
    private final int fieldSize;

    /**
     * ID 字段信息
     */
    private final List<ModelFieldInfo> idFieldInfos;

    /**
     * 乐观锁字段
     */
    private final ModelFieldInfo versionFieldInfo;

    /**
     * 多租户ID
     */
    private final ModelFieldInfo tenantIdFieldInfo;

    private final ModelFieldInfo idFieldInfo;

    public ModelInfo(Class<?> model) {
        this.type = model;
        Class<?> entity = GenericUtil.getGenericInterfaceClass(model).stream().filter(item -> item.isAnnotationPresent(Table.class)).findFirst().orElseThrow(() -> new RuntimeException(MessageFormat.format("class {0} have no generic type", model.getName())));
        this.entityType = entity;

        try {
            this.tableInfo = Tables.get(entity);
        } catch (NotTableClassException e) {
            throw new RuntimeException(MessageFormat.format("unable match model class {0} , the generic class {1} is not a entity", model.getName(), entity.getName()));
        }

        List<ModelFieldInfo> modelFieldInfos = FieldUtil.getResultMappingFields(model).stream().map(field -> new ModelFieldInfo(entity, model, field)).collect(Collectors.toList());

        this.idFieldInfos = modelFieldInfos.stream().filter(item -> item.getTableFieldInfo().isTableId()).collect(Collectors.toList());
        this.idFieldInfo = this.idFieldInfos.size() == 1 ? this.idFieldInfos.get(0) : null;
        this.versionFieldInfo = modelFieldInfos.stream().filter(item -> item.getTableFieldInfo().isVersion()).findFirst().orElse(null);
        this.tenantIdFieldInfo = modelFieldInfos.stream().filter(item -> item.getTableFieldInfo().isTenantId()).findFirst().orElse(null);

        this.modelFieldInfos = Collections.unmodifiableList(modelFieldInfos);
        this.fieldSize = this.modelFieldInfos.size();
    }

    public Class<?> getType() {
        return type;
    }

    public Class<?> getEntityType() {
        return entityType;
    }

    public TableInfo getTableInfo() {
        return tableInfo;
    }

    public ModelFieldInfo getVersionFieldInfo() {
        return versionFieldInfo;
    }

    public ModelFieldInfo getTenantIdFieldInfo() {
        return tenantIdFieldInfo;
    }

    public List<ModelFieldInfo> getModelFieldInfos() {
        return modelFieldInfos;
    }

    public List<ModelFieldInfo> getIdFieldInfos() {
        return idFieldInfos;
    }

    public int getFieldSize() {
        return fieldSize;
    }

    public ModelFieldInfo getSingleIdFieldInfo(boolean throwException) {
        if (throwException && Objects.isNull(this.idFieldInfo)) {
            throw new RuntimeException("Model:" + this.type + " has multi ID or non-single ID.");
        }
        return this.idFieldInfo;
    }
}

package cn.mybatis.mp.generator.util;

import cn.mybatis.mp.core.mybatis.mapper.context.Pager;
import cn.mybatis.mp.core.sql.executor.chain.DeleteChain;
import cn.mybatis.mp.core.sql.executor.chain.InsertChain;
import cn.mybatis.mp.core.sql.executor.chain.QueryChain;
import cn.mybatis.mp.core.sql.executor.chain.UpdateChain;
import cn.mybatis.mp.core.util.NamingUtil;
import cn.mybatis.mp.db.IdAutoType;
import cn.mybatis.mp.db.annotations.*;
import cn.mybatis.mp.generator.config.ContainerType;
import cn.mybatis.mp.generator.config.EntityConfig;
import cn.mybatis.mp.generator.config.GeneratorConfig;
import cn.mybatis.mp.generator.database.meta.ColumnInfo;
import cn.mybatis.mp.generator.database.meta.EntityInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GeneratorUtil {

    /**
     * 获取实体类名字
     *
     * @param generatorConfig
     * @param tableName
     * @return
     */
    public static String getEntityName(GeneratorConfig generatorConfig, String tableName) {
        EntityConfig entityConfig = generatorConfig.getEntityConfig();
        if (entityConfig.getNameConvert() == null) {
            entityConfig.nameConvert((table -> NamingUtil.firstToUpperCase(NamingUtil.underlineToCamel(table))));
        }
        return entityConfig.getNameConvert().apply(tableName);
    }


    /**
     * 获取实体类字段名字
     *
     * @param generatorConfig
     * @param columnInfo
     * @return
     */
    public static String getEntityFieldName(GeneratorConfig generatorConfig, ColumnInfo columnInfo) {
        EntityConfig entityConfig = generatorConfig.getEntityConfig();
        if (entityConfig.getFieldNameConverter() == null) {
            return entityConfig.getFieldNamingStrategy().getName(columnInfo.getName(), false);
        }
        return entityConfig.getFieldNameConverter().apply(columnInfo);
    }

    /**
     * 获取实体类字段备注
     *
     * @param generatorConfig
     * @param columnInfo
     * @return
     */
    public static String getEntityFieldRemarks(GeneratorConfig generatorConfig, ColumnInfo columnInfo) {
        EntityConfig entityConfig = generatorConfig.getEntityConfig();
        if (entityConfig.getRemarksConverter() == null) {
            return columnInfo.getRemarks();
        }
        return entityConfig.getRemarksConverter().apply(columnInfo);
    }

    /**
     * 获取列的java 类型
     *
     * @param generatorConfig
     * @param columnInfo
     * @return
     */
    public static Class<?> getColumnType(GeneratorConfig generatorConfig, ColumnInfo columnInfo) {
        Class<?> type = generatorConfig.getEntityConfig().getTypeMapping().get(columnInfo.getJdbcType());
        if (type == null) {
            return Object.class;
        }
        return type;
    }

    /**
     * 构建实体类的imports
     *
     * @param generatorConfig
     * @param entityInfo
     * @return
     */
    public static List<String> buildEntityImports(GeneratorConfig generatorConfig, EntityInfo entityInfo) {
        List<String> classList = new ArrayList<>();
        classList.add(Table.class.getName());
        if (entityInfo.getIdFieldInfo() != null) {
            classList.add(TableId.class.getName());
            classList.add(IdAutoType.class.getName());
        }

        if (generatorConfig.getEntityConfig().getSuperClass() != null) {
            classList.add(generatorConfig.getEntityConfig().getSuperClass());
        }
        if (generatorConfig.getEntityConfig().isLombok()) {
            classList.add(lombok.Data.class.getName());
        }
        entityInfo.getFieldInfoList().forEach(item -> {
            classList.add(item.getType().getName());
            if (item.isNeedTableField()) {
                classList.add(TableField.class.getName());
            }
            if (item.getColumnInfo().isVersion()) {
                classList.add(Version.class.getName());
            } else if (item.getColumnInfo().isTenantId()) {
                classList.add(TenantId.class.getName());
            } else if (item.getColumnInfo().isLogicDelete()) {
                classList.add(LogicDelete.class.getName());
            }
        });
        return buildImports(classList);
    }

    /**
     * 构建实体类的imports
     *
     * @param generatorConfig
     * @param entityInfo
     * @return
     */
    public static List<String> buildMapperImports(GeneratorConfig generatorConfig, EntityInfo entityInfo) {
        List<String> classList = new ArrayList<>();
        if (generatorConfig.getMapperConfig().getSuperClass() != null) {
            classList.add(generatorConfig.getMapperConfig().getSuperClass());
        }
        if (generatorConfig.getMapperConfig().isMapperAnnotation()) {
            classList.add(Mapper.class.getName());
        }
        classList.add(entityInfo.getEntityPackage() + "." + entityInfo.getName());
        return buildImports(classList);
    }

    /**
     * 构建Dao接口的imports
     *
     * @param generatorConfig
     * @param entityInfo
     * @return
     */
    public static List<String> buildDaoImports(GeneratorConfig generatorConfig, EntityInfo entityInfo) {
        List<String> classList = new ArrayList<>();
        if (generatorConfig.getDaoConfig().getSuperClass() != null) {
            classList.add(generatorConfig.getDaoConfig().getSuperClass());
        }

        classList.add(entityInfo.getEntityPackage() + "." + entityInfo.getName());
        if (entityInfo.getIdFieldInfo() != null) {
            classList.add(entityInfo.getIdFieldInfo().getType().getName());
        }
        return buildImports(classList);
    }

    /**
     * 构建Dao实现类的imports
     *
     * @param generatorConfig
     * @param entityInfo
     * @return
     */
    public static void buildDaoImplImports(GeneratorConfig generatorConfig, EntityInfo entityInfo, Map<String, Object> data) {
        List<String> classList = new ArrayList<>();
        if (generatorConfig.getDaoImplConfig().getSuperClass() != null) {
            classList.add(generatorConfig.getDaoImplConfig().getSuperClass());
        }

        classList.add(entityInfo.getEntityPackage() + "." + entityInfo.getName());
        if (entityInfo.getIdFieldInfo() != null) {
            classList.add(entityInfo.getIdFieldInfo().getType().getName());
        }
        classList.add(entityInfo.getMapperPackage() + "." + entityInfo.getMapperName());
        classList.add(entityInfo.getDaoPackage() + "." + entityInfo.getDaoName());

        if (generatorConfig.getContainerType() == ContainerType.SPRING) {
            classList.add(org.springframework.stereotype.Repository.class.getName());
            classList.add(org.springframework.beans.factory.annotation.Autowired.class.getName());
            data.put("repositoryAnnotationName", org.springframework.stereotype.Repository.class.getSimpleName());
            data.put("autowiredAnnotationName", org.springframework.beans.factory.annotation.Autowired.class.getSimpleName());
        } else if (generatorConfig.getContainerType() == ContainerType.SOLON) {
            classList.add(org.noear.solon.annotation.Component.class.getName());
            classList.add(org.noear.solon.annotation.Inject.class.getName());
            classList.add(org.noear.solon.annotation.Init.class.getName());
            data.put("repositoryAnnotationName", org.noear.solon.annotation.Component.class.getSimpleName());
            data.put("autowiredAnnotationName", org.noear.solon.annotation.Inject.class.getSimpleName());
        }
        data.put("imports", buildImports(classList));
    }


    /**
     * 构建Dao接口的imports
     *
     * @param generatorConfig
     * @param entityInfo
     * @return
     */
    public static List<String> buildServiceImports(GeneratorConfig generatorConfig, EntityInfo entityInfo) {
        List<String> classList = new ArrayList<>();
        if (generatorConfig.getServiceConfig().getSuperClass() != null) {
            classList.add(generatorConfig.getServiceConfig().getSuperClass());
        }

        classList.add(entityInfo.getEntityPackage() + "." + entityInfo.getName());
        if (entityInfo.getIdFieldInfo() != null) {
            classList.add(entityInfo.getIdFieldInfo().getType().getName());
        }
        return buildImports(classList);
    }

    /**
     * 构建Dao实现类的imports
     *
     * @param generatorConfig
     * @param entityInfo
     * @return
     */
    public static void buildServiceImplImports(GeneratorConfig generatorConfig, EntityInfo entityInfo, Map<String, Object> data) {
        List<String> classList = new ArrayList<>();
        if (generatorConfig.getServiceImplConfig().getSuperClass() != null) {
            classList.add(generatorConfig.getServiceImplConfig().getSuperClass());
        }

        classList.add(entityInfo.getEntityPackage() + "." + entityInfo.getName());
        if (entityInfo.getIdFieldInfo() != null) {
            classList.add(entityInfo.getIdFieldInfo().getType().getName());
        }
        classList.add(entityInfo.getDaoPackage() + "." + entityInfo.getDaoName());
        classList.add(entityInfo.getServicePackage() + "." + entityInfo.getServiceName());

        if (!generatorConfig.getServiceImplConfig().isInjectMapper()) {
            classList.add(entityInfo.getMapperPackage() + "." + entityInfo.getMapperName());
            classList.add(QueryChain.class.getName());
            classList.add(UpdateChain.class.getName());
            classList.add(InsertChain.class.getName());
            classList.add(DeleteChain.class.getName());
        }

        if (generatorConfig.getContainerType() == ContainerType.SPRING) {
            classList.add(org.springframework.stereotype.Service.class.getName());
            classList.add(org.springframework.beans.factory.annotation.Autowired.class.getName());
            data.put("serviceAnnotationName", org.springframework.stereotype.Service.class.getSimpleName());
            data.put("autowiredAnnotationName", org.springframework.beans.factory.annotation.Autowired.class.getSimpleName());
        } else if (generatorConfig.getContainerType() == ContainerType.SOLON) {
            classList.add(org.noear.solon.annotation.Component.class.getName());
            classList.add(org.noear.solon.annotation.Inject.class.getName());
            data.put("serviceAnnotationName", org.noear.solon.annotation.Component.class.getSimpleName());
            data.put("autowiredAnnotationName", org.noear.solon.annotation.Inject.class.getSimpleName());
        }
        data.put("imports", buildImports(classList));
    }

    /**
     * 构建Action的imports
     *
     * @param generatorConfig
     * @param entityInfo
     * @return
     */
    public static void buildActionImports(GeneratorConfig generatorConfig, EntityInfo entityInfo, Map<String, Object> data) {
        List<String> classList = new ArrayList<>();
        if (generatorConfig.getActionConfig().getSuperClass() != null) {
            classList.add(generatorConfig.getActionConfig().getSuperClass());
        }

        classList.add(entityInfo.getEntityPackage() + "." + entityInfo.getName());

        if (entityInfo.getIdFieldInfo() != null) {
            classList.add(entityInfo.getIdFieldInfo().getType().getName());
        }

        classList.add(entityInfo.getServicePackage() + "." + entityInfo.getServiceName());

        if (generatorConfig.getContainerType() == ContainerType.SPRING) {
            classList.add(org.springframework.web.bind.annotation.RestController.class.getName());
            classList.add(org.springframework.web.bind.annotation.RequestMapping.class.getName());
            classList.add(org.springframework.beans.factory.annotation.Autowired.class.getName());

            data.put("controllerAnnotationName", org.springframework.web.bind.annotation.RestController.class.getSimpleName());
            data.put("requestMappingAnnotationName", org.springframework.web.bind.annotation.RequestMapping.class.getSimpleName());
            data.put("autowiredAnnotationName", org.springframework.beans.factory.annotation.Autowired.class.getSimpleName());
        } else if (generatorConfig.getContainerType() == ContainerType.SOLON) {
            classList.add(org.noear.solon.annotation.Controller.class.getName());
            classList.add(org.noear.solon.annotation.Mapping.class.getName());
            classList.add(org.noear.solon.core.handle.MethodType.class.getName());
            classList.add(org.noear.solon.annotation.Inject.class.getName());

            data.put("controllerAnnotationName", org.noear.solon.annotation.Controller.class.getSimpleName());
            data.put("requestMappingAnnotationName", org.noear.solon.annotation.Mapping.class.getSimpleName());
            data.put("autowiredAnnotationName", org.noear.solon.annotation.Inject.class.getSimpleName());
        }

        if (generatorConfig.getActionConfig().isSave() || generatorConfig.getActionConfig().isUpdate()) {
            if (generatorConfig.getContainerType() == ContainerType.SPRING) {
                classList.add(org.springframework.web.bind.annotation.PostMapping.class.getName());
                data.put("postMappingAnnotationName", org.springframework.web.bind.annotation.PostMapping.class.getSimpleName());
            } else {
                classList.add(org.noear.solon.annotation.Post.class.getName());
                classList.add(org.noear.solon.annotation.Mapping.class.getName());
                classList.add(org.noear.solon.core.handle.MethodType.class.getName());
                data.put("postMappingAnnotationName", org.noear.solon.annotation.Mapping.class.getSimpleName());
            }
        }

        if (generatorConfig.getActionConfig().isGetById() || generatorConfig.getActionConfig().isFind()) {
            if (generatorConfig.getActionConfig().isFind()) {
                classList.add(Pager.class.getName());
            }

            if (generatorConfig.getContainerType() == ContainerType.SPRING) {
                classList.add(org.springframework.web.bind.annotation.GetMapping.class.getName());
                data.put("getMappingAnnotationName", org.springframework.web.bind.annotation.GetMapping.class.getSimpleName());
            } else {
                classList.add(org.noear.solon.annotation.Get.class.getName());
                classList.add(org.noear.solon.annotation.Mapping.class.getName());
                data.put("getMappingAnnotationName", org.noear.solon.annotation.Mapping.class.getSimpleName());
            }
        }


        if (generatorConfig.getActionConfig().isDeleteById()) {
            if (generatorConfig.getContainerType() == ContainerType.SPRING) {
                classList.add(org.springframework.web.bind.annotation.DeleteMapping.class.getName());
                data.put("deleteMappingAnnotationName", org.springframework.web.bind.annotation.DeleteMapping.class.getSimpleName());
            } else {
                classList.add(org.noear.solon.annotation.Delete.class.getName());
                classList.add(org.noear.solon.annotation.Mapping.class.getName());
                data.put("deleteMappingAnnotationName", org.noear.solon.annotation.Mapping.class.getSimpleName());
            }
        }

        data.put("imports", buildImports(classList));
    }

    public static List<String> buildImports(List<String> classList) {
        return classList.stream().filter(item -> !item.startsWith("java.lang")).distinct().sorted(Comparator.reverseOrder()).collect(Collectors.toList());
    }
}

package cn.mybatis.mp.generator.database.meta;

import cn.mybatis.mp.generator.config.GeneratorConfig;
import cn.mybatis.mp.generator.util.GeneratorUtil;
import cn.mybatis.mp.generator.util.PathUtils;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@ToString
public class EntityInfo {

    public final String serviceName;
    public final String servicePackage;
    public final String serviceImplName;
    public final String serviceImplPackage;
    public final String actionName;
    public final String actionPackage;
    private final TableInfo tableInfo;
    private final String name;
    private final String remarks;
    private final EntityFieldInfo idFieldInfo;
    private final List<EntityFieldInfo> idFieldInfoList;
    private final List<EntityFieldInfo> allFieldInfoList;
    private final List<EntityFieldInfo> fieldInfoList;
    private final List<EntityFieldInfo> excludeFieldInfoList;
    private final String entityPackage;
    private final String mapperName;
    private final String mapperPackage;
    private final String daoName;
    private final String daoPackage;
    private final String daoImplName;
    private final String daoImplPackage;
    private boolean hasIgnoreTablePrefix = false;

    public EntityInfo(GeneratorConfig generatorConfig, TableInfo tableInfo) {
        String tmpTableName = tableInfo.getName();
        for (String prefix : generatorConfig.getTableConfig().getTablePrefixs()) {
            if (tmpTableName.startsWith(prefix)) {
                tmpTableName = tmpTableName.replaceFirst(prefix, "");
                hasIgnoreTablePrefix = true;
                break;
            }
        }


        this.name = GeneratorUtil.getEntityName(generatorConfig, tmpTableName);
        this.remarks = tableInfo.getRemarks();
        this.tableInfo = tableInfo;
        if (tableInfo.getIdColumnInfo() != null) {
            this.idFieldInfo = new EntityFieldInfo(generatorConfig, this, tableInfo.getIdColumnInfo());
        } else {
            this.idFieldInfo = null;
        }
        this.idFieldInfoList = tableInfo.getIdColumnInfoList().stream().map(item -> new EntityFieldInfo(generatorConfig, this, item)).collect(Collectors.toList());
        this.allFieldInfoList = tableInfo.getColumnInfoList().stream().map(item -> new EntityFieldInfo(generatorConfig, this, item)).collect(Collectors.toList());

        this.excludeFieldInfoList = allFieldInfoList.stream().filter(item -> generatorConfig.getEntityConfig().getExcludeColumns().contains(item.getColumnInfo().getName()) || generatorConfig.getEntityConfig().getExcludeColumns().contains(item.getColumnInfo().getName().toUpperCase())).collect(Collectors.toList());

        List<EntityFieldInfo> fieldInfoList = new ArrayList<>(this.allFieldInfoList);
        fieldInfoList.removeAll(this.excludeFieldInfoList);
        this.fieldInfoList = fieldInfoList;

        this.entityPackage = PathUtils.buildPackage(generatorConfig.getBasePackage(), generatorConfig.getEntityConfig().getPackageName()).toString();

        this.mapperName = this.name + generatorConfig.getMapperConfig().getSuffix();
        this.mapperPackage = PathUtils.buildPackage(generatorConfig.getBasePackage(), generatorConfig.getMapperConfig().getPackageName()).toString();

        this.daoName = this.name + generatorConfig.getDaoConfig().getSuffix();
        this.daoPackage = PathUtils.buildPackage(generatorConfig.getBasePackage(), generatorConfig.getDaoConfig().getPackageName()).toString();

        this.daoImplName = this.name + generatorConfig.getDaoImplConfig().getSuffix();
        this.daoImplPackage = PathUtils.buildPackage(generatorConfig.getBasePackage(), generatorConfig.getDaoImplConfig().getPackageName()).toString();

        this.serviceName = this.name + generatorConfig.getServiceConfig().getSuffix();
        this.servicePackage = PathUtils.buildPackage(generatorConfig.getBasePackage(), generatorConfig.getServiceConfig().getPackageName()).toString();

        this.serviceImplName = this.name + generatorConfig.getServiceImplConfig().getSuffix();
        this.serviceImplPackage = PathUtils.buildPackage(generatorConfig.getBasePackage(), generatorConfig.getServiceImplConfig().getPackageName()).toString();

        this.actionName = this.name + generatorConfig.getActionConfig().getSuffix();
        this.actionPackage = PathUtils.buildPackage(generatorConfig.getBasePackage(), generatorConfig.getActionConfig().getPackageName()).toString();
    }

    public boolean hasId() {
        return !idFieldInfoList.isEmpty();
    }

    public boolean hasMultiId() {
        return idFieldInfoList.size() > 1;
    }
}

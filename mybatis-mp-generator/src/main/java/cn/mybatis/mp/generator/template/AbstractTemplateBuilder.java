package cn.mybatis.mp.generator.template;

import cn.mybatis.mp.generator.config.GeneratorConfig;
import cn.mybatis.mp.generator.database.meta.EntityInfo;
import db.sql.api.impl.tookit.Objects;

import java.io.File;

public abstract class AbstractTemplateBuilder implements ITemplateBuilder {

    protected final GeneratorConfig generatorConfig;

    protected final EntityInfo entityInfo;

    public AbstractTemplateBuilder(GeneratorConfig generatorConfig, EntityInfo entityInfo) {
        this.generatorConfig = generatorConfig;
        this.entityInfo = entityInfo;
    }

    @Override
    public boolean fileCover() {
        return this.generatorConfig.isFileCover();
    }

    @Override
    public EntityInfo entityInfo() {
        return entityInfo;
    }

    @Override
    public String charset() {
        return this.generatorConfig.getCharset();
    }

    protected StringBuilder getJavaDirPath() {
        StringBuilder javaDirPath = new StringBuilder(generatorConfig.getBaseFilePath());
        if (Objects.nonNull(generatorConfig.getJavaPath())) {
            javaDirPath.append(File.separator).append(generatorConfig.getJavaPath());
        }
        return javaDirPath;
    }

    protected StringBuilder getResourceDirPath() {
        StringBuilder resourceDirPath = new StringBuilder(generatorConfig.getBaseFilePath());
        if (Objects.nonNull(generatorConfig.getResourcePath())) {
            resourceDirPath.append(File.separator).append(generatorConfig.getResourcePath());
        }
        return resourceDirPath;
    }
}

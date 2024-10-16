package cn.mybatis.mp.generator.template;

import cn.mybatis.mp.generator.config.GeneratorConfig;
import cn.mybatis.mp.generator.database.meta.EntityInfo;
import cn.mybatis.mp.generator.util.GeneratorUtil;

import java.io.File;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class ServiceImplTemplateBuilder extends AbstractTemplateBuilder {

    public ServiceImplTemplateBuilder(GeneratorConfig generatorConfig, EntityInfo entityInfo) {
        super(generatorConfig, entityInfo);
    }

    @Override
    public boolean enable() {
        return generatorConfig.getServiceImplConfig().isEnable();
    }

    @Override
    public String targetFilePath() {
        return getJavaDirPath()
                .append(File.separator)
                .append((entityInfo.getServiceImplPackage() + "." + entityInfo.getServiceImplName()).replaceAll("\\.", File.separator) + ".java")
                .toString();
    }

    @Override
    public String templateFilePath() {
        return generatorConfig.getTemplateRootPath() + "/service.impl";
    }

    @Override
    public Map<String, Object> contextData() {
        Map<String, Object> data = new HashMap<>();
        GeneratorUtil.buildServiceImplImports(generatorConfig, entityInfo, data);
        if (generatorConfig.getServiceImplConfig().getSuperClass() != null) {
            int dotIndex = generatorConfig.getServiceImplConfig().getSuperClass().lastIndexOf(".");
            String superName;
            if (dotIndex > 0) {
                superName = generatorConfig.getServiceImplConfig().getSuperClass().substring(dotIndex + 1);
            } else {
                superName = generatorConfig.getServiceImplConfig().getSuperClass();
            }
            data.put("superExtend", " extends " + superName);
        } else {
            data.put("superExtend", "");
        }
        data.put("date", LocalDate.now().toString());
        data.put("author", generatorConfig.getAuthor());
        data.put("entityInfo", entityInfo);
        data.put("entityConfig", generatorConfig.getEntityConfig());
        data.put("mapperConfig", generatorConfig.getMapperConfig());
        data.put("daoConfig", generatorConfig.getDaoConfig());
        data.put("daoImplConfig", generatorConfig.getDaoImplConfig());
        data.put("serviceConfig", generatorConfig.getServiceConfig());
        data.put("serviceImplConfig", generatorConfig.getServiceImplConfig());
        data.put("generatorConfig", generatorConfig);
        data.put("containerType", generatorConfig.getContainerType());
        return data;
    }
}

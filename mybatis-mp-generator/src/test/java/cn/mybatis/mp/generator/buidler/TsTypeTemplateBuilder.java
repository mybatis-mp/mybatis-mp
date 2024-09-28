package cn.mybatis.mp.generator.buidler;

import cn.mybatis.mp.generator.JavaToTsType;
import cn.mybatis.mp.generator.config.GeneratorConfig;
import cn.mybatis.mp.generator.database.meta.EntityInfo;
import cn.mybatis.mp.generator.template.AbstractTemplateBuilder;

import java.util.HashMap;
import java.util.Map;

public class TsTypeTemplateBuilder extends AbstractTemplateBuilder {

    public TsTypeTemplateBuilder(GeneratorConfig generatorConfig, EntityInfo entityInfo) {
        super(generatorConfig, entityInfo);
    }

    @Override
    public boolean enable() {
        return generatorConfig.getMapperXmlConfig().isEnable();
    }

    @Override
    public String targetFilePath() {
        return generatorConfig.getBaseFilePath() + "/tsTypes/" + entityInfo.getName().replaceAll("\\.", "/") + ".ts";
    }

    @Override
    public String templateFilePath() {
        return generatorConfig.getTemplateRootPath() + "/tsType";
    }

    @Override
    public Map<String, Object> contextData() {
        Map<String, Object> data = new HashMap<>();
        data.put("entityInfo", entityInfo);
        data.put("javaToTsType", new JavaToTsType());
        return data;
    }
}

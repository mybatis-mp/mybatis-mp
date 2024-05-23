package cn.mybatis.mp.generator;

import cn.mybatis.mp.generator.config.ContainerType;
import cn.mybatis.mp.generator.config.GeneratorConfig;

public class Test {

    public static void main(String[] args) {
        new FastGenerator(new GeneratorConfig(
                "jdbc:mysql://test.mysql.com:3306/casual_put",
                "root",
                "123456")
                .basePackage("com.test")
                .swaggerVersion(3)
                .containerType(ContainerType.SPRING)
                .columnConfig(columnConfig -> {
                    columnConfig.disableUpdateColumns("create_time");
                    columnConfig.versionColumn("phone");
                    columnConfig.logicDeleteColumn("free");
                    columnConfig.tenantIdColumn("state");
                })
                .entityConfig(entityConfig -> {
                    entityConfig.lombok(false);
                    entityConfig.swagger(true);
                    entityConfig.logicDeleteCode("@LogicDelete(beforeValue=\"0\",afterValue=\"1\",deleteTimeField=\"create_time\")");
                })
                .mapperXmlConfig(mapperXmlConfig -> {
                    mapperXmlConfig.enable(true).resultMap(true).columnList(true);
                })
                .serviceImplConfig(serviceImplConfig -> {
                    serviceImplConfig.injectMapper(true);
                })
                .actionConfig(actionConfig -> {
                    actionConfig
                            .save(true)
                            .update(true)
                            .find(true)
                            .getById(true)
                            .deleteById(true)
                            .swagger(true)
                            .returnClass(Object.class.getName());
                })
        ).create();
    }
}

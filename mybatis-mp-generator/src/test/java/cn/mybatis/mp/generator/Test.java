package cn.mybatis.mp.generator;

import cn.mybatis.mp.generator.buidler.TsTypeTemplateBuilder;
import cn.mybatis.mp.generator.config.ContainerType;
import cn.mybatis.mp.generator.config.GeneratorConfig;
import db.sql.api.DbType;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;

public class Test {

    private static void oracleTest() {
        new FastGenerator(new GeneratorConfig(
                "jdbc:oracle:thin:@//localhost:1521/xe?currentSchema=SYSTEM",
                "system",
                "oracle")
                .basePackage("com.test")
                .swaggerVersion(3)
                .containerType(ContainerType.SPRING)
                .tableConfig(tableConfig -> {
                    tableConfig.includeTable("t_sys_user", "t_sys_user", "t_sys_user", "t_sys_user");
                })
                .columnConfig(columnConfig -> {
                    columnConfig.disableUpdateColumns("create_time");
                    columnConfig.versionColumn("phone");
                    columnConfig.logicDeleteColumn("free");
                    columnConfig.tenantIdColumn("state");
                })
                .entityConfig(entityConfig -> {
                    entityConfig.lombok(false);
                    entityConfig.swagger(true);
                    entityConfig.alwaysAnnotation(false);
                    entityConfig.logicDeleteCode("@LogicDelete(beforeValue=\"0\",afterValue=\"1\",deleteTimeField=\"create_time\")");
                })
                .mapperXmlConfig(mapperXmlConfig -> {
                    mapperXmlConfig.enable(true).resultMap(true).columnList(true);
                })
                .serviceImplConfig(serviceImplConfig -> {
                    serviceImplConfig.superClass(Integer.class).injectMapper(true);
                })
                .actionConfig(actionConfig -> {
                    actionConfig
                            .enableSave(true)
                            .enableUpdate(true)
                            .enableFind(true)
                            .enableGet(true)
                            .enableDelete(true)
                            .swagger(true)
                            .returnClass(Object.class.getName());
                })
        ).create();
    }

    private static void mysqlTest() {
        new FastGenerator(new GeneratorConfig(
                "jdbc:mysql://localhost:3306/test3",
                "root",
                "123456")
                .basePackage("com.test")
                .swaggerVersion(3)
                .containerType(ContainerType.SPRING)
                .tableConfig(tableConfig -> {
                    //tableConfig.includeTable("ip_info");
                })
                .templateBuilders(list -> {
                    list.add(TsTypeTemplateBuilder.class);
                })
                .columnConfig(columnConfig -> {
                    columnConfig.disableUpdateColumns("create_time");
                    columnConfig.versionColumn("phone");
                    columnConfig.logicDeleteColumn("free");
                    columnConfig.tenantIdColumn("state");
                })
                .entityConfig(entityConfig -> {
                    entityConfig.lombok(false).serial(false);
                    entityConfig.swagger(true).packageName("model");
                    entityConfig.logicDeleteCode("@LogicDelete(beforeValue=\"0\",afterValue=\"1\",deleteTimeField=\"create_time\")");
                })
                .mapperXmlConfig(mapperXmlConfig -> {
                    mapperXmlConfig.enable(true).resultMap(true).columnList(true);
                })
                .daoConfig(daoConfig -> daoConfig.enable(false))
                .daoImplConfig(daoImplConfig -> daoImplConfig.enable(false))
                .serviceConfig(serviceConfig -> serviceConfig.superClass(IService.class).generic(true))
                .serviceImplConfig(serviceImplConfig -> {
                    serviceImplConfig.superClass(ServiceImpl.class).injectMapper(true).injectDao(false);
                })
                .actionConfig(actionConfig -> {
                    actionConfig
                            .enableSave(true)
                            .enableUpdate(true)
                            .enableFind(true)
                            .enableGet(true)
                            .enableDelete(true)
                            .swagger(true)
                            .returnClass(Object.class.getName());
                })
        ).create();

    }

    @org.junit.jupiter.api.Test
    public void h2TestCase(){
        h2Test();
    }

    private static void h2Test() {

        DataSource dataSource = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .setName("test3")
                .addScript("schema.sql")
                .build();


        //根据数据源生成
        new FastGenerator(new GeneratorConfig(
                DbType.H2,//数据库类型
                dataSource)
                .basePackage("com.test")//根包路径
                .baseFilePath("../generate")
                .templateBuilders(list -> {
                    list.add(TsTypeTemplateBuilder.class);
                })
                .mapperXmlConfig(mapperXmlConfig -> mapperXmlConfig.enable(true).columnList(true).resultMap(true))
        ).create();
    }

    public static void main(String[] args) {
        Long start = System.currentTimeMillis();
        mysqlTest();
        System.out.println(System.currentTimeMillis() - start);
    }
}

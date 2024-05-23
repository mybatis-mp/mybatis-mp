#### 喜欢的朋友加入QQ群：<font color="red">121908790</font> ，群里不仅可以提mybatis-mp框架问题，还可以帮你解决后端的各种问题！
##### 另外，喜欢的朋友，帮忙关注 和 star（点点小爱心）！


> <strong style="color:red">特别申明：禁止在非法项目中使用，否则后果自负！</strong>

<p align="center">
    <a target="_blank" href="https://search.maven.org/search?q=mybatis-mp">
        <img src="https://img.shields.io/maven-central/v/cn.mybatis-mp/mybatis-mp?label=Maven%20Central" alt="Maven" />
    </a>
    <a target="_blank" href="https://www.apache.org/licenses/LICENSE-2.0.txt">
		<img src="https://img.shields.io/:license-Apache2-blue.svg" alt="Apache 2" />
	</a>
    <a target="_blank" href='https://gitee.com/mybatis-mp/mybatis-mp'>
		<img src='https://gitee.com/mybatis-mp/mybatis-mp/badge/star.svg' alt='Gitee star'/>
	</a>
</p>

## 与众不同的 几大亮点：
#### 1：mybatis-mp - 亮点一：可自定义动态默认值
#### 2：mybatis-mp - 亮点二：支持不同数据库ID自增配置
#### 3：mybatis-mp - 亮点三：逻辑删除，可自动填充删除时间
#### 4：mybatis-mp - 亮点四：可自定义sql（sql模板）
#### 5：mybatis-mp - 亮点五：mapWithKey（把查询转成一个 map）
#### 6：mybatis-mp - 亮点 六：部分字段 新增 和 修改
#### 7：mybatis-mp - 亮点七：枚举的良好支持 
#### 8：mybatis-mp - 亮点八：mybatis-xml returnType 的 ORM 映射
#### 9：mybatis-mp - 亮点九：优雅的 XML和 @Select查询 自动分页
#### 10：mybatis-mp - 亮点十：支持多层嵌套VO，自动映射以及自动select 所需列

## 特征

#### 1、很轻量,非常轻量

> 轻量级封装mybatis。
> 其他框架都比较深度修改了mybatis源码。

#### 2、高性能

> 对比其他mybatis框架，性能不差，接近最优。

#### 3、灵活方便

> 中高度实现ORM，查询API零学习成本。

#### 4、高可用

> 可应付90%的SQL需求。

#### 5、可靠，安全

> 没有过于复杂的设计，但是api却很丰富，足够使用！
> 其他框架或多或少设计的过于复杂，反而容易出现各种问题。

#### 5、优秀的分页和SQL优化能力

> 自动过滤多余的left join
> count查询 自动去除order by ，无效的left join，以及select部分替换成 select count(*) 或 select 1 后 在select count(*)
> 内置分页功能，超级牛逼！

## QQ 群

群号： 121908790 ,邀请各位大神参与补充，绝对开源，大家都可以进行代码提交，审核通过会进行master分支。
![](./doc/image/qq-group.png)

# springboot接入示例：

https://gitee.com/mybatis-mp/mybatis-mp-spring-boot-demo

# 快速开始

## 1. 基于spring-boot开发 (已引入spring、springboot 基本依赖，创建SpringApplication.run即可启动)

### 1.1 springboot2 maven 集成

```xml
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>cn.mybatis-mp</groupId>
            <artifactId>mybatis-mp-spring-boot-parent</artifactId>
            <version>1.4.7</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>

<dependencies>
    <dependency>
        <groupId>cn.mybatis-mp</groupId>
        <artifactId>mybatis-mp-spring-boot-starter</artifactId>
    </dependency>
</dependencies>
```

### 1.2 springboot3 maven 集成

```xml
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>cn.mybatis-mp</groupId>
            <artifactId>mybatis-mp-spring-boot-parent</artifactId>
            <version>1.4.7-spring-boot3</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>

<dependencies>
    <dependency>
        <groupId>cn.mybatis-mp</groupId>
        <artifactId>mybatis-mp-spring-boot-starter</artifactId>
    </dependency>
</dependencies>
```

#### 1.3 数据源 配置

配置spring boot配置文件

```yaml
spring.datasource.url=jdbc:mysql://localhost/test
spring.datasource.username=dbuser
spring.datasource.password=dbpass
```

或者 自己实例一个 DataSource 也可以

```java

@Configuration(proxyBeanMethods = false)
public class DatasourceConfig {

    @Bean
    public DataSource getDataSource() {
        return new EmbeddedDatabaseBuilder()
                .setName("test_db")
                .setType(EmbeddedDatabaseType.H2)
                .addScript("schema.sql")
                .build();
    }
}

```

### 其他配置(可不配置)

#### 数据库命名规则配置(可不配置，在项目启动时配置)

```java

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        MybatisMpConfig.setTableUnderline(true); //数据库表是否下划线规则 默认 true
        MybatisMpConfig.setColumnUnderline(true); ///数据库列是否下划线规则 默认 true
        SpringApplication.run(DemoApplication.class, args);
    }
}
```

#### 数据库类型配置(可不配置，自动识别)

```yaml
mybatis:
  configuration:
    databaseId: MYSQL
```

> 更多数据库支持，请查看类：db.sql.api.DbType

> 启动springboot即可，非常简单

### 一切都就绪，启动即可

> 1.添加此依赖，无需再添加mybatis依赖

> 2.包含 mybatis、mybatis-spring、 mybatis-spring-boot-starter 所有功能（支持原有mybatis的所有功能）

> 3.更多mybatis 配置参数，参考 https://mybatis.org/spring-boot-starter/mybatis-spring-boot-autoconfigure/zh/index.html

> 4.参考示例：https://gitee.com/mybatis-mp/mybatis-mp/tree/master/mybatis-mp-spring-boot-demo

> 5.更多 mybatis 用法，参考：
> mybatis：https://mybatis.org/mybatis-3/zh/index.html
>
> mybatis spring: https://mybatis.org/spring/zh/index.html

> 6.更多mybatis-mp 用法，参考作者编写的test case:(包含各种简单，复杂的CRUD操作案例)
>
> https://gitee.com/mybatis-mp/mybatis-mp/tree/master/mybatis-mp-core/src/test/java/com/mybatis/mp/core/test/testCase

# 枚举类支持
## 1.要求
> mybatis-mp 支持枚举作为参数进行进行CRUD；要求一下：
### 1.1.普通枚举，以名字存储（继承 Serializable）
### 1.2.非普通枚举，自定义属性存储（继承 EnumSupport）
```java
public enum TestEnum implements EnumSupport<String> {

    X1("a1"),X2("a2");

    TestEnum(String code){
        this.code=code;
    }

    private final String code;

    @Override
    public String getCode() {
        return this.code;
    }

```
## 2.如何使用
### 2.1：新增
```java
@Table
@Data
public class DefaultValueTest {

    @TableId
    private Integer id;

    @TableField(defaultValue = "{BLANK}")
    private String value1;

    @TableField(defaultValue = "1", updateDefaultValue = "2")
    private Integer value2;

    @TableField(defaultValue = "{NOW}")
    private LocalDateTime createTime;

    private TestEnum value3;
}
```
```java
DefaultValueTestMapper mapper = session.getMapper(DefaultValueTestMapper.class);
DefaultValueTest defaultValueTest = new DefaultValueTest();
defaultValueTest.setValue3(TestEnum.X1);
mapper.save(defaultValueTest);
```
### 2.2：查询
```java
QueryChain.of(mapper)
.in(DefaultValueTest::getValue1,Arrays.asList(TestEnum.X1,TestEnum.X2))
.list();
```
> 把它看成普通的参数即可，用户和 int string 都是一样的

# 注解

### 1. @Table

```java

@Table
public class Student {

}
```

### 2. @TableId

```java

@Table
public class Student {

    @TableId
    private Integer id;
}
```

> @TableId 支持不同数据库自增或序列 以及 自定义SQL，默认是数据库自增

<table>
    <tr align="center">
        <th>属性</th>
        <th>默认</th>
        <th align="left">说明</th>
    </tr>
    <tr align="center">
        <td>value</td>
        <td>IdAutoType.AUTO</td>
        <td align="left">
            <p>IdAutoType.AUTO: 数据库自增</p>
            <p>IdAutoType.NONE: 开发者自己set值</p>
            <p>IdAutoType.SQL: 结合@TableId.sql属性使用</p>
            <p>IdAutoType.NONE: 开发者自己set值</p>
            <p>IdAutoType.GENERATOR: 结合@TableId.generatorName属性,实现自定义自增；</p>
        </td>
    </tr>
    <tr align="center">
        <td>dbType</td>
        <td>DbType.MYSQL</td>
        <td align="left">设置数据库的类型，@TableId 可配置多个，以支持多数据库切换</td>
    </tr>
    <tr align="center">
        <td>sql</td>
        <td>""</td>
        <td align="left">IdAutoType.SQL生效,实现数据库层自增；例如：SELECT LAST_INSERT_ID() 或 序列</td>
    </tr>
    <tr align="center">
        <td>generatorName</td>
        <td>""</td>
        <td align="left">
            <p>IdAutoType.GENERATOR生效;可取值：</p>
            <p>IdentifierGeneratorType.DEFAULT（推荐，可替换成自己的实现）：基于雪花算法</p>
            <p>IdentifierGeneratorType.UUID: 基于UUID</p>
            <p>IdentifierGeneratorType.mpNextId：基于雪花算法</p>
            <p>自定义：只需要实现 IdentifierGenerator，并注册（项目启动时）ID生成器：IdentifierGeneratorFactory.register("名字"，生成器的实例)</p>        
        </td>
    </tr>
</table>

### 3. @TableField(可不配置) 数据库字段注解

```java

@Table
public class Student {

    @TableField
    private LocalDateTime createTime;
}
```

> @TableField 可以设置列名、typeHandler、jdbcType、是否查询、是否更新,保存时默认值，修改时默认值 等

> 默认值 可以是常量，动态值（结合 动态参数配置）,例如{BLANK} 空值，支持string,list,map,set,数组，更多查看 动态参数配置 一项

### 4. @Ignore 可忽略字段（可用的实体类 VO 等字段上）

### 5. @ForeignKey 外键，配置后 JOIN时无需要再加 ON条件（自动添加）

```java

@Data
@Table
public class Achievement {

    @TableId
    private Integer id;

    @ForeignKey(Student.class)
    private Integer studentId;

    private BigDecimal score;

    private Date createTime;
}
```

### 6. 非实体类结果映射

> 非实体类映射，可以select(非实体类.class),除@ResultField注解，其他均可自动帮你select;
```java
QueryChain queryChain = QueryChain.of(sysUserMapper)
    .select(SysUserRoleAutoSelectVo.class)
    .from(SysUser.class)
    .join(SysUser.class, SysRole.class)
    .eq(SysUser::getId, 2)
    .returnType(SysUserRoleAutoSelectVo.class);
```

```java

@Data
@ToString(callSuper = true)
@ResultEntity(Student.class)
public class StudentVo {

    private Integer id;

    private String name;

    //private LocalDateTime stCreateTime;

    @ResultEntityField(target = Student.class, property = "createTime")
    private LocalDateTime st2CreateTime;
}

@Data
@ToString(callSuper = true)
@ResultEntity(Student.class)
public class StudentAchievementVo extends StudentVo {

    @ResultEntityField(target = Achievement.class, property = "id")
    private Integer achievementId;

    @NestedResultEntity(target = Achievement.class)
    private Achievement achievement;

    @ResultField(value = "xx_score")
    private Integer score;

}
```

#### @ResultEntity

> 用于返回类 和 实体类关联 (只能配置一个，哪个返回列多，配哪个实体类)

#### @ResultEntityField

> 用于解决字段名字和实体类名字不一致的问题

#### @NestedResultEntity

> 用于内嵌类的映射 和 @ResultEntity 类似,可以映射一对一，一对多（List<POJO> 属性）

```java
@Data
@ResultEntity(SysRole.class)
public class OneToManyVo {

    private Integer id;

    private String name;

    private LocalDateTime createTime;

    @NestedResultEntity(target = SysUser.class)
    private List<SysUser> sysUserList;
}
```

#### @NestedResultEntityField

> 用于解决内嵌字段名字和实体类名字不一致的问题

#### @ResultField

> 返回列的映射（用于非实体类字段，可以设置列名、typeHandler、jdbcType）
> 返回可以平级 或者 1级 2级 多层映射
```java
@Data
@ResultEntity(SysUser.class)
public class SysUserVo {

    private Integer id;
    
    private String userName;
    
    @ResultField("asNameOther")
    private String asName;

    @ResultField
    private String asName2;

}
```
2种用法：
```java
    SysUserVo sysUser = QueryChain.of(sysUserMapper)
        .select(SysUser::getId, SysUser::getUserName)
        .selectWithFun(SysUser::getUserName, c -> c.as("asNameOther"))
        .selectWithFun(SysUser::getUserName, c -> c.as(SysUserVo::getAsName2))
        .from(SysUser.class)
        .eq(SysUser::getId, 1)
        .returnType(SysUserVo.class)
        .get();
```
> asName 字段 也可以 用 selectWithFun(SysUser::getUserName, c -> c.as(SysUserVo::getAsName))映射

### 7. @Version 乐观锁

> 乐观锁，只能注解在Integer类型字段上
>
> 在save(实体类)、save(Model)、update(实体类)、update(Model);

<font color="red">其他update地方操作 需要自己维护，进行version+1.</font>

### 8. @TenantId 多租户ID

> 多租户是围绕实体进行自动设置租户ID，通常在 from(实体类),join(实体类),delete(实体类),update(实体类)，

> 另外设置租户ID的获取方法,示例是返回租户ID为2：

```java
TenantContext.registerTenantGetter(() -> {
    return 2;
});
```

如果想临时关闭
```java
TenantContext.registerTenantGetter(() -> {
     if(临时关闭){
         //返回null 即可
        return null;    
     }
     return 2;
});
```


### 9. @LogicDelete 逻辑删除

> 支持字段类型：string，数字，布尔类型，时间类型（Date,LocalDateTime,Long,Integer）

> 逻辑删除 在deleteById,delete(实体类),delete(Where) 生效

> 查询时，将自动添加删除过滤条件（通常在 from(实体类),join(实体类),update(实体类)时，自动添加，delete 除上面3个方法， 其他不附加）

```java
@Data
@Table
public class LogicDeleteTest {

    @TableId
    private Long id;

    private String name;

    private LocalDateTime deleteTime;

    @LogicDelete(beforeValue = "0", afterValue = "1", deleteTimeField = "deleteTime")
    private Byte deleted;
}
```

#### 9.1 @LogicDelete 属性 beforeValue

> 未删除前的值，只能是固定值；时间类型的逻辑，可不填

#### 9.2 @LogicDelete 属性 afterValue

> 删除后的值，可固定值或者动态值 例如 afterValue = "{NOW}"，目前支持LocalDateTime,Date,Long,Integer，框架自动给值

#### 9.3 @LogicDelete 属性 deleteTimeField

> 逻辑删除的时间字段，可不填，填了系统自动update 时 设置删除时间，deleteTimeField对应的字段类型
> 支持：LocalDateTime,Date,Long,Integer类型

#### 9.4 逻辑删除全局开关(默认开)

```java
MybatisMpConfig.setLogicDeleteSwitch(true);
```

#### 9.5 逻辑删除-局部开关

```java
try (LogicDeleteSwitch ignored = LogicDeleteSwitch.with(false)) {
    logicDeleteTestMapper.getById(1);
}
```

> 上面代码必须try (LogicDeleteSwitch ignored = LogicDeleteSwitch.with(false))

或
> 下面的更简单：

```java
LogicDeleteTest logicDeleteTest = LogicDeleteUtil.execute(false, () -> {
    return logicDeleteTestMapper.getById(1);
});
```
## 10. @Fetch 加载额外数据，实现1对1,1对多
> 用于非实体类中，内嵌(非实体)类中
配置代码
```java
@Data
@ResultEntity(SysUser.class)
public class FetchSysUserVo {

    private Integer id;

    private String userName;

    private String password;

    @Fetch(source = SysUser.class, property = "role_id", target = SysRole.class, targetProperty = "id")
    private SysRoleVo sysRole;

    private LocalDateTime create_time;

}
```
```java
List<FetchSysUserVo> list = QueryChain.of(sysUserMapper)
                    .select(FetchSysUserVo.class)
                    .from(SysUser.class)
                    .eq(SysUser::getId,2)
                    .returnType(FetchSysUserVo.class)
                    .list();
```
> [FetchSysUserVo(id=2, userName=test1, password=123456, sysRole=SysRoleVo(id=1, name=测试), create_time=2023-10-11T15:16:17)]

> 以上为1对1，如果需要实现 1对多，则 private SysRoleVo sysRole 改为 private List<SysRoleVo> sysRoles 即可

> SysRoleVo 也可以是 实体类 SysRole

> @Fetch 属性说明 
<table>
    <tr align="center">
        <th>属性</th>
        <th>默认值</th>
        <th align="left">说明</th>
    </tr>
    <tr align="center">
        <td>column</td>
        <td>""</td>
        <td align="left">
            <p>指定结果列，用于获取查询子表的值</p>
            <p>column 和  property 二选一，column优先</p>    
        </td>
    </tr>
    <tr align="center">
        <td>source</td>
        <td>Void.class</td>
        <td align="left">
            <p>指定列的对应的实体</p>
            <p>选择property属性时填写</p>    
        </td>
    </tr>
    <tr align="center">
        <td>property</td>
        <td>""</td>
        <td align="left">
            <p>指定列的对应的实体的属性</p>
            <p>column 和  property 二选一，column优先</p>    
        </td>
    </tr>
    <tr align="center">
        <td>storey</td>
        <td>1</td>
        <td align="left">
            <p>指定列的存储层级，默认为1</p>
            <p>选择property属性时填写</p>    
        </td>
    </tr>
    <tr align="center">
        <td>target</td>
        <td></td>
        <td align="left">
            <p>指定加载实体类（也就是表的意思）</p>
        </td>
    </tr>
    <tr align="center">
        <td>targetProperty</td>
        <td></td>
        <td align="left">
            <p>指定加载实体类的属性</p>
            <p>指定属性对象列进行匹配查询</p>    
        </td>
    </tr>
    <tr align="center">
        <td>targetSelectProperty</td>
        <td></td>
        <td align="left">
            <p>指定返回实体类的属性</p>
            <p>指定返回属性对象列</p>    
        </td>
    </tr>
    <tr align="center">
        <td>multiValueErrorIgnore</td>
        <td>false</td>
        <td align="left">
            <p>1对1 多条时，发现多条是否忽略错误</p>
        </td>
    </tr>
    <tr align="center">
        <td>orderBy</td>
        <td></td>
        <td align="left">
            <p>排序，格式为 属性 desc/asc,多个逗号分割</p>
            <p>例如：id asc,userName desc</p>
        </td>
    </tr>
</table>

## 11. ModelEntityField 注解
> Model类字段实体类注解 用于解决字段命名不一样问题
> 
> Model是类似实体类子集类，用于新增、修改中，可以减少类型转换的损失；save(model)/update(model)
> 再也不用转换了
```java
@Data
@Table
public class IdTest {


    @TableId
    @TableId(dbType = DbType.SQL_SERVER, value = IdAutoType.AUTO)
    @TableId(dbType = DbType.PGSQL, value = IdAutoType.SQL, sql = "select nextval('seq1')")
    private Long id;

    private LocalDateTime createTime;

}
```

```java
@Data
public class IdTestModel implements Model<IdTest> {

    @ModelEntityField("id")
    private Long xxid;

    private LocalDateTime createTime;
}
```
> Model类会继承实体类的相关信息，例如上面 IdTestModel 自动匹配 IdTest的相关信息；
> 
## 12.TypeHandler 注解
> 丰富Vo类 ，实现类似脱敏的功能；注意：实体类里不会生效
> 
```java
package com.mybatis.mp.core.test.typeHandler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.*;

public class PhoneTypeHandler extends BaseTypeHandler<String> {
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, String parameter, JdbcType jdbcType) throws SQLException {
        throw new RuntimeException("not support");
    }

    private String format(String value) throws SQLException {
        if (value != null) {
            //此处实现 手机号码脱敏
            return value.substring(0,3)+"****"+value.substring(7);
        }
        return "";
    }

    @Override
    public String getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return format(rs.getString(columnName));
    }

    @Override
    public String getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return format(rs.getString(columnIndex));
    }

    @Override
    public String getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return format(cs.getString(columnIndex));
    }
}
```
```java
@Data
@ResultEntity(SysRole.class)
public class OneToManyTypeHandlerVo {

    private Integer id;

    @TypeHandler(PhoneTypeHandler.class)
    private String name;

    private LocalDateTime createTime;

    @TypeHandler(PhoneTypeHandler.class)
    @ResultField("kk2")
    private String kkName;

    @NestedResultEntity(target = SysUser.class)
    private List<SysUserTypeHandlerVo> sysUserList;
}
```
内嵌的类 也是可以的
```java
@Data
public class SysUserTypeHandlerVo {

    private Integer id;

    @TypeHandler(PhoneTypeHandler.class)
    private String userName;

    @TypeHandler(PhoneTypeHandler.class)
    @NestedResultEntityField("password")
    private String pwd;

    @TypeHandler(PhoneTypeHandler.class)
    @ResultField("kk")
    private String kkName;

}
```
查询代码：关键在 returnType(OneToManyTypeHandlerVo.class) ；其他都是普通的查询代码 
```java
List<OneToManyTypeHandlerVo> list = QueryChain.of(sysRoleMapper)
                    .select(SysUser.class)
                    .selectWithFun(SysUser::getUserName, c -> c.as("kk"))
                    .selectWithFun(SysUser::getUserName, c -> c.as("kk2"))
                    .select(SysRole.class)
                    .from(SysRole.class)
                    .join(SysRole.class, SysUser.class, on -> on.eq(SysUser::getRole_id, SysRole::getId))
                    .returnType(OneToManyTypeHandlerVo.class)
                    .list();
```


# XML 或 @Select注解的自动分页
## 1.1 要求：

> 1. 方法返回是 Pager<T> T不能省略

> 2. 方法第一个参数 是 Pager<T> 类型

> 3. 方法上加上注解 @Paging
## 1.2 方法示例：
```
    @Paging
    Pager<SysRole> xmlPaging(Pager<SysRole> pager, @Param("id") Integer id, @Param("id2") Integer id2);

    @Paging
    Pager<SysRole> xmlPaging2(Pager<SysRole> pager);

    @Paging
    Pager<SysRole> xmlDynamicPaging(Pager<SysRole> pager, @Param("id") Integer id, @Param("id2") Integer id2, @Param("id3") Integer id3);

    @Paging
    @Select("select * from sys_role where id >=#{id} and id <=#{id2}")
    Pager<SysRole> annotationPaging(Pager<SysRole> pager, @Param("id") Integer id, @Param("id2") Integer id2);
```
## 1.3 xml 代码示例：
```xml
    <resultMap id="sysRole" type="com.mybatis.mp.core.test.DO.SysRole">
        <id column="id" property="id"/>
    </resultMap>

    <select id="xmlPaging" resultType="com.mybatis.mp.core.test.DO.SysRole">
        select *
        from sys_role
        where id >= #{id}
          and id &lt;= #{id2}
    </select>

    <select id="xmlPaging2" resultMap="sysRole">
        select *
        from sys_role
    </select>

    <select id="xmlDynamicPaging" resultType="com.mybatis.mp.core.test.DO.SysRole">
        select * from sys_role where id >=#{id} and id &lt;=#{id2}
        <if test="id3 != null">
            and id &lt;=#{id3}
        </if>
    </select>
```

## 1.4 (各种场景) 代码使用：
```java
 Pager<SysRole> pager = sysRoleMapper.xmlPaging(Pager.of(1), 1, 1);

 Pager<SysRole> pager = sysRoleMapper.xmlPaging2(Pager.of(2));

 Pager<SysRole> pager = sysRoleMapper.xmlDynamicPaging(Pager.of(2),1,2,1);

 Pager<SysRole> pager  = sysRoleMapper.annotationPaging(Pager.of(2), 1, 2);
 
```

# mybatis-mp mvc 架构理念

> mybatis-mp 只设计到1层持久层，不过 mybatis-mp的理念，把持久层分2层，mapper层，dao层

## mapper层、dao层的区别

> 区别在于 dao层是对mapper的扩展和限制
>

> 扩展：增加CRUD链式操作 增加 queryChain()、updateChain()、deleteChain()、insertChain()等方法，无需操作mapper接口；
>

> 限制：dao 只暴露 save(实体类|Model类)、update(实体类|Model类)、getById(ID)、deleteById(ID)、delete(实体类)等
>
> mapper层一般不增加代码，如果有无法的实现的数据库操作，则需要在 mapper类上添加方法
>
<font color="red">如果不需要dao 可以直接使用mapper接口，进行CRUD；代码生成器有对应的关闭dao生成 和 service
注入mapper的开关</font>

## mybatis-mp service层的理解

> service 不应该支持操作mapper,因为mapper包含丰富的api，这样不利于代码的维护性
>
> service 不应该使用Query等作为参数，dao层也不应该；让service更专注于业务
>
>

# 链路式 CRUD 说明

> 为了增加CRUD一体式操作，增加了链路式类，分别为是:
>
> QueryChain 包含 get(),list(),count(),exists(),map(),paging()等查询方法；
>
> UpdateChain 包含 execute();
>
> InsertChain 包含 execute();
>
> DeleteChain 包含 execute();
>

## 如何简单创建 CRUD Chain类

> Chain类都包含一个of(MybatisMapper mapper)静态方法，直接调用即可
>
例如：

```java
List<SysUser> list=QueryChain.of(sysUserMapper).select(...).list();
```

```java
int updateCnt=UpdateChain.of(sysUserMapper)
        .update(SysUser.class)
        .set(SysUser::getName,"hi")
        .eq(SysUser::getName,"haha")
        .execute();
```

>
> dao 里自带 queryChain()、updateChain()等方法，可以直接获取链路式CRUD类
>

## Dao 层 CRUD

> 继承 cn.mybatis.mp.core.mvc.Dao 接口 和 cn.mybatis.mp.core.mvc.impl.DaoImpl

### save

### update

### getById

### deleteById

### deleteByIds

### map 查询结果转map方法

### 链式类的获取方法

> queryChain()
>
> updateChain()
>
> insertChain()
>
> deleteChain()
> 使用方式

```java
queryChain()
    .select(SysUser.class)
    .select(SysRole.class)
    .selectWithFun(SysUser::getName,c->c.concat("").as("copy_name"))
    .from(SysUser.class)
    .join(SysUser.class,SysRole.class)
    .eq(SysUser::getId,id)
    .returnType(SysUserVo.class)
    .get();

updateChain()
    .update(SysRole.class)
    .set(SysRole::getName,"test")
    .eq(SysRole::getId,1)
    .execute();

```

## Service层 链路式 CRUD

> 使用代码生成器时，可在 ServiceImplConfig injectMapper 一项设置为true，即可生成 queryChain()等方法 和 Dao 层 链路式 CRUD
> 一样
>

<font color="red">建议在dao里操作</font>,如果还是想操作，参考 其他层 链路式 CRUD

## 其他层 链路式 CRUD

```java
QueryChain.of(sysUserMapper)
    .select(SysUser.class)
    .select(SysRole.class)
    .selectWithFun(SysUser::getName,c->c.concat("").as("copy_name"))
    .from(SysUser.class)
    .join(SysUser.class,SysRole.class)
    .eq(SysUser::getId,id)
    .returnType(SysUserVo.class)
    .get();

UpdateChain.of(sysUserMapper)
    .update(SysRole.class)
    .set(SysRole::getName,"test")
    .eq(SysRole::getId,1)
    .execute();     
```

# 使用前，请先查看此项（重要）

> 其他框架一般都是默认忽略null值参数的

> 但是这样可能比较容易导致bug出现，例如 删除 修改时，因为null 导致数据紊乱了

> 因此mybatis-mp,默认会检测 null值，并设有开关，让有需要忽略null，或者 空字符串等需要操作；如下使用：

> Query重 select 可以select 实体类，vo类，属性等，推荐select vo类(也就是配置@ResultEntity的vo类)

```java
SysUser sysUser = QueryChain.of(sysUserMapper)
    // forSearch包含忽略null 、空字符串、对字符串进行trim去空格    
    .forSearch()
    .select(SysUser::getId)
    .from(SysUser.class)
    .eq(SysUser::getUserName, null )
    .eq(SysUser::getUserName, "" )
    .eq(SysUser::getUserName," admin ")
    .returnType(SysUser.class)
    .get();
```

或者

```java
SysUser sysUser = QueryChain.of(sysUserMapper)
    // 忽略 null 条件参数    
    .ignoreNullValueInCondition(true)
    // 忽略 空字符串 条件参数    
    .ignoreEmptyInCondition(true)
    //  对字符串进行trim 去空格操作    
    .trimStringInCondition(true)
    .select(SysUser::getId)
    .from(SysUser.class)
    .eq(SysUser::getUserName, null )
    .eq(SysUser::getUserName, "" )
    .eq(SysUser::getUserName," admin ")
    .returnType(SysUser.class)
    .get();
```

目前 CRUD类、Where类都支持以上方法

# 开始CRUD（CRUD教程）

> Mapper 需要继承 MybatisMapper

```java
public interface StudentMapper extends MybatisMapper<Student> {

}
```

## 1.Mapper方法

### 单个查询

> getById(Serializable id) 根据ID查询实体

> R get(Query query) 单个动态查询（可自定返回类型）

> T get(Where where) 单个动态查询（只返回实体类）

### 列表查询

> List<R> list(Query query) 列表动态查询（可自定返回类型）

> List<T> list(Where where) 列表动态查询（只返回实体类）
>

### count查询

> count(Query query) 动态count查询

> count(Where where) 动态count查询

### exists查询

> exists(Query query) 动态检测是否存在

> exists(Where where) 动态检测是否存在

### 删除

> deleteById(Serializable id) 根据ID删除

> delete(T entity) 根据实体类删除

> delete(Where where) 动态删除

> delete(Delete delete) 动态删除

> deleteByIds(Serializable... ids) 根据多个ID删除

> deleteByIds(List<Serializable> ids) 根据多个ID删除

### 保存

> save(T entity) 实体类保存

> save(Model entity) 实体类部分保存

> save(Insert insert) 动态插入（无法返回ID）

> save(List<T> list) 插入多个

> saveBatch(List<T> list, Getter<T>... saveFields) 原生sql批量插入,需要指定列
<pre>
 * 使用数据库原生方式批量插入
 * 一次最好在100条内
 * 
 * 会自动加入 主键 租户ID 逻辑删除列 乐观锁
 * 自动设置 默认值,不会忽略NULL值字段
</pre>

```java
List<DefaultValueTest> list= Arrays.asList(new DefaultValueTest(),new DefaultValueTest());
mapper.saveBatch(list, DefaultValueTest::getValue1, DefaultValueTest::getValue2, DefaultValueTest::getCreateTime);
```

### 修改

> update(T entity) 实体类更新

> update(T entity,F... fields) 实体类更新 + 强制字段更新

> update(Model entity) 实体类部分更新

> update(Model entity,F... fields) 实体类部分更新 + 强制字段更新

> update(T entity,Where where) 实体类批量更新

> update(Update update) 动态更新

### mapWithKey 查询结果转成一个map的方法

> Map<K, V> mapWithKey(Getter<T> mapKey, Serializable... ids) key为mapKey，value当前实体类

> Map<K, V> mapWithKey(Getter<G> mapKey, BaseQuery query) key为mapKey，value为query指定的类型

> Map<K, V> mapWithKey(Getter<T> mapKey, List<Serializable> ids) key为mapKey，value当前实体类

> Map<K, T> mapWithKey(Getter<T> mapKey, Consumer<Where> consumer) key为mapKey，value当前实体类

> Map<K, T> mapWithKey ... 还有其他的方法

### 分页查询

> Pager<R> paging(Query query, Pager<R> pager) 分页查询（可自定返回类型）

> Pager<T> paging(Where where, Pager<R> pager) 分页查询（只返回实体类类型）

### 牛逼的SQL优化

#### 查询优化

> 去除无用left join

#### count优化

> 去除无用left join (自动判断是否有效)
>
> 去除无用的order by (自动判断是否有效)
>
> 优化union 查询 （优化 left join 和 order by,自动判断是否有效）

## CRUD 操作

> 文档为演示 所以是调用 QueryChain.of(mapper),实际使用 queryChain()方法，在dao层
>

### 1.1 单个查询

```java
Student stu=studentMapper.getById(1);

或

Student stu2=QueryChain.of(sysUserMapper)
    .eq(Student::getId,1)
    .get();

或

Student stu3=studentMapper.get(where->where.eq(Student::getId,1));

```

> 能用前者优先前者，后者为单个动态查询

### 1.2 选择部分列

```java
Student stu3=QueryChain.of(sysUserMapper)
    .select(Student::getName,Student::getCreateTime)
    .from(Student.class)
    .eq(Student::getId,1)
    .get();
```

### 1.2 忽略部分列

```java
Student stu3=QueryChain.of(sysUserMapper)
    .select(Student.class)
    .selectIgnore(Student::getCreateTime)
    .from(Student.class)
    .eq(Student::getId,1)
    .get();
```

> 注意：需要先select 再 selectIgnore

### 1.3 join 连表查询

#### 1.3.1 基础用法

```java
List<Achievement> achievementList=QueryChain.of(achievementMapper)
    .select(Achievement.class)
    .select(Student::getName)
    .from(Achievement.class)
    .join(Achievement.class,Student.class,on->on.eq(Achievement::getStudentId,Student::getId))
    .eq(Achievement::getId,1)
    .list();

```

> 支持各种连接：INNER JOIN ,LEFT JOIN 等等

#### 1.3.2 省力用法

> join 可结合 @ForeignKey使用 这样无需加 ON 条件

```java
List<Achievement> achievementList2=QueryChain.of(achievementMapper)
    .select(Achievement.class)
    .select(Student::getName)
    .from(Achievement.class)
    .join(Achievement.class,Student.class)
    .eq(Achievement::getId,1)
    .list();
```

> 注意，假如自己拼接上了 on 条件 ，则 @ForeignKey 不生效，需要自己把条件书写完成！

#### 1.3.3 相同表 join

```java
List<SysUser> list=QueryChain.of(sysUserMapper)
    .select(SysUser.class)
    .from(SysUser.class)
    .join(JoinMode.INNER,SysUser.class,1,SysUser.class,2,on->on.eq(SysUser::getId,1,SysUser::getRole_id,2))
    .list();
```

> 注意 select(SysUser.class) 只是返回前面那个表的数据，如需返回后面那个表，则需要 结合注解@ResultField(别名)
>
> 然后 new Query().select(SysUser::getUserName,2,c->c.as("sub_name"))，其中2 表示实体类SysUser对应的表实例的缓存层级

#### 1.3.4 不同表join

```java
List<SysUserAndRole> list=QueryChain.of(sysUserMapper)
    .select(SysUser.class)
    .select(SysRole.class)
    .from(SysUser.class)
    .join(SysUser.class,SysRole.class,on->on.eq(SysUser::getRole_id,SysRole::getId))
    .returnType(SysUserAndRole.class)
    .list();
```

> SysUserAndRole 如何映射，请查看注解说明。

#### 1.3.5 join 子表

```java
SubQuery subQuery=SubQuery.create("sub")
    .select(SysRole.class)
    .from(SysRole.class)
    .eq(SysRole::getId,1);

List<SysUser> list=QueryChain.of(sysUserMapper)
    .select(SysUser.class)
    .from(SysUser.class)
    .join(JoinMode.INNER,SysUser.class,subQuery,on->on.eq(SysUser::getRole_id,subQuery.$(subQuery,SysRole::getId)))
    .list();
```

#### 1.3.6 connect 更好的内联

> 这个方法的意义在于 拿到本身实例，从而更好的链式操作，例如下面的query.$(SysUser::getId)：
>
> 这样可以使用query里方法，query.$(SysUser::getId) 就是从query里取得SysUser的id列，从而被 exists子查询引用。

```java
List<SysUser> list=QueryChain.of(sysUserMapper)
    .select(SysUser::getId,SysUser::getUserName,SysUser::getRole_id)
    .from(SysUser.class)
    .connect(query->{
        query.exists(SubQuery.create()
            .select1()
            .from(SysUser.class)
            .eq(SysUser::getId,query.$(SysUser::getId))
            .isNotNull(SysUser::getPassword)
            .limit(1)
            );
    })
    .list();

```

### 1.4 删除

```java
studentMapper.deleteById(1);

    或
        
DeleteChain.of(studentMapper).eq(Student::getId,1).execute();

    或

studentMapper.delete(where->where.eq(Student::getId,1))
```

> 能用前者优先前者，后者为单个动态删除
>

### 1.5 新增

```java
Student student=new Student();
//student.setIdMethod(11);
student.setName("哈哈");
student.setExcellent(true);
student.setCreateTime(LocalDateTime.now());
studentMapper.save(student);

    或者

@Data
public class StudentDTO implements cn.mybatis.mp.db.Model<Student> {

    private Integer id;

    private String name;

    private LocalDateTime createTime;
}

StudentDTO studentDTO = new StudentDTO();
studentDTO.setName("DTO Insert");
studentDTO.setCreateTime(LocalDateTime.now());
studentMapper.save(studentDTO);

        或者
        
InsertChain.of(studentMapper)
    .insert(Student.class)
    .field(
        Student::getName,
        Student::getExcellent,
        Student::getCreateTime
    )
    .values(Arrays.asList("哈哈",true,LocalDateTime.now()))
    .execute();

```

> 能用前者优先前者，后者为动态插入（可多个）
>

### 1.6 更新

```java
Student student=new Student();
student.setName("哈哈");
student.setExcellent(true);
student.setCreateTime(LocalDateTime.now());
studentMapper.update(student);

    或者

@Data
public class StudentDTO implements cn.mybatis.mp.db.Model<Student> {

    private Integer id;
    
    private String name;
    
    private LocalDateTime createTime;
}

StudentDTO studentDTO = new StudentDTO();
studentDTO.setId(1);
studentDTO.setName("DTO Insert");
studentMapper.update(studentDTO);

或者

UpdateChain.of(studentMapper)
    .update(Student.class)
    .set(Student::getName,"嘿嘿")
    .eq(Student::getId,1)
    .execute();

或者

SysUser updateSysUser = new SysUser();
updateSysUser.setUserName("adminxx");
sysUserMapper.update(updateSysUser, where->where.eq(SysUser::getId,1));
```

> 能用前者优先前者，后者为动态更新
>

### 1.7 group by

```java
List<Integer> counts=QueryChain.of(sysUserMapper)
        .select(SysUser::getId,c->c.count())
        .from(SysUser.class)
        .groupBy(SysUser::getRole_id)
        .returnType(Integer.TYPE)
        .list();
```

### 1.8 order by

```java
SysUser sysUser=QueryChain.of(sysUserMapper)
        .select(SysUser::getId,SysUser::getUserName,SysUser::getRole_id)
        .from(SysUser.class)
        .orderBy(false,SysUser::getRole_id,SysUser::getId)
        .limit(1)
        .get();
```

### 1.9 group by having

```java
Integer count=QueryChain.of(sysUserMapper)
        .selectWithFun(SysUser::getRole_id,FunctionInterface::count)
        .from(SysUser.class)
        .groupBy(SysUser::getRole_id)
        .having(SysUser::getRole_id,c->c.gt(0))
        .returnType(Integer.TYPE)
        .get();
```

### 2.0 条件

#### 2.1 and or 相互切换

```java
QueryChain.of(sysUserMapper)
    .select(SysUser::getId)
    .from(SysUser.class)
    .eq(SysUser::getId,2).or().eq(SysUser::getId,1)
    .returnType(Integer.TYPE)
    .get();
```

> 调用 and(),则后续操作默认都and操作
>
> 调用 or(),则后续操作默认都or操作

#### 2.2 大于( gt() )

```java
Integer id=QueryChain.of(sysUserMapper)
        .select(SysUser::getId)
        .from(SysUser.class)
        .gt(SysUser::getId,2)
        .returnType(Integer.TYPE)
        .get();
```    

#### 2.3 大于等于( gte() )

```java
Integer id=QueryChain.of(sysUserMapper)
        .select(SysUser::getId)
        .from(SysUser.class)
        .gte(SysUser::getId,2)
        .limit(1)
        .returnType(Integer.TYPE)
        .get();
```   

#### 2.4 小于( lt() )

```java
Integer id=QueryChain.of(sysUserMapper)
        .select(SysUser::getId)
        .from(SysUser.class)
        .lt(SysUser::getId,2)
        .limit(1)
        .returnType(Integer.TYPE)
        .get();
```    

#### 2.5 小于等于( lte() )

```java
Integer id=QueryChain.of(sysUserMapper)
        .select(SysUser::getId)
        .from(SysUser.class)
        .lte(SysUser::getId,1)
        .limit(1)
        .returnType(Integer.TYPE)
        .get();
```   

#### 2.6 等于( eq() )

```java
Integer id=QueryChain.of(sysUserMapper)
        .select(SysUser::getId)
        .from(SysUser.class)
        .eq(SysUser::getId,2)
        .returnType(Integer.TYPE)
        .get();
```    

#### 2.7 不等于( ne() )

```java
Integer id=QueryChain.of(sysUserMapper)
        .select(SysUser::getId)
        .from(SysUser.class)
        .ne(SysUser::getId,2)
        .returnType(Integer.TYPE)
        .count();
```  

#### 2.8 is NULL

```java
SysUserMapper sysUserMapper=session.getMapper(SysUserMapper.class);
        SysUser sysUser=QueryChain.of(sysUserMapper)
        .select(SysUser::getId,SysUser::getPassword,SysUser::getRole_id)
        .from(SysUser.class)
        .isNull(SysUser::getPassword)
        .get();
```    

#### 2.9 is NOT NULL

```java
SysUser sysUser=QueryChain.of(sysUserMapper)
        .select(SysUser::getId,SysUser::getPassword)
        .from(SysUser.class)
        .isNotNull(SysUser::getPassword)
        .eq(SysUser::getId,3)
        .returnType(Integer.TYPE)
        .get();
```  

#### 3.0 in

```java
List<Integer> list=QueryChain.of(sysUserMapper)
        .select(SysUser::getId)
        .from(SysUser.class)
        .in(SysUser::getId,1,2)
        .returnType(Integer.TYPE)
        .list();
```

#### 3.1 like

```java
SysUser sysUser=QueryChain.of(sysUserMapper)
        .select(SysUser::getId,SysUser::getPassword,SysUser::getRole_id)
        .from(SysUser.class)
        .like(SysUser::getUserName,"test1")
        .get();
```

#### 3.2 left like

```java
SysUser sysUser = QueryChain.of(sysUserMapper)
        .select(SysUser::getId, SysUser::getPassword, SysUser::getRole_id)
        .from(SysUser.class)
        .like(SysUser::getUserName, "test1", LikeMode.LEFT)
        .get();
```

#### 3.3 right like

```java
Integer count = QueryChain.of(sysUserMapper)
        .select(SysUser::getId, c -> c.count())
        .from(SysUser.class)
        .like(SysUser::getUserName, "test", LikeMode.RIGHT)
        .returnType(Integer.TYPE)
        .count();
```

#### 3.4 not like

```java
SysUser sysUser = QueryChain.of(sysUserMapper)
        .select(SysUser::getId, SysUser::getPassword, SysUser::getRole_id)
        .from(SysUser.class)
        .notLike(SysUser::getUserName, "test1")
        .like(SysUser::getUserName, "test")
        .get();
```

#### 3.5 not left like

```java
SysUser sysUser = QueryChain.of(sysUserMapper)
        .select(SysUser::getId, SysUser::getPassword, SysUser::getRole_id)
        .from(SysUser.class)
        .notLike(SysUser::getUserName, "est1", LikeMode.LEFT)
        .like(SysUser::getUserName, "test")
        .get();
```

#### 3.6 not right like

```java
SysUser sysUser = QueryChain.of(sysUserMapper)
        .select(SysUser::getId, SysUser::getPassword, SysUser::getRole_id)
        .from(SysUser.class)
        .notLike(SysUser::getUserName, "test2", LikeMode.RIGHT)
        .like(SysUser::getUserName, "test")
        .get();
```

#### 3.7 between

```java
List<Integer> list = QueryChain.of(sysUserMapper)
        .select(SysUser::getId)
        .from(SysUser.class)
        .between(SysUser::getId, 1, 2)
        .returnType(Integer.TYPE)
        .list();
```

#### 3.8 not between

```java
List<Integer> list = QueryChain.of(sysUserMapper)
        .select(SysUser::getId)
        .from(SysUser.class)
        .between(SysUser::getId, 1, 3)
        .notBetween(SysUser::getId, 1, 2)
        .returnType(Integer.TYPE)
        .list();
```

#### 3.9 exists

```java
boolean exists = QueryChain.of(sysUserMapper)
        .select(SysUser::getId, SysUser::getUserName, SysUser::getRole_id)
        .from(SysUser.class)
        .join(SysUser.class, SysRole.class)
        .like(SysUser::getUserName, "test")
        .exists();
或者

sysUserMapper.exists(where->where.like(SysUser::getUserName, "test"));
```

### 3.0 select 去重

```java
List<Integer> roleIds = QueryChain.of(sysUserMapper)
        .selectDistinct()
        .select(SysUser::getRole_id)
        .from(SysUser.class)
        .returnType(Integer.TYPE)
        .list();
```

### 4.0 union 和 union all 查询

```java
List<SysUser> list = QueryChain.of(sysUserMapper)
        .select(SysUser::getRole_id, SysUser::getId)
        .from(SysUser.class)
        .eq(SysUser::getId, 1)
        .union(Query.create()
        .select(SysUser::getRole_id, SysUser::getId)
        .from(SysUser.class)
        .lt(SysUser::getId, 3)
        .list();
```

```java
List<SysUser> list = QueryChain.of(sysUserMapper)
        .select(SysUser::getRole_id, SysUser::getId)
        .from(SysUser.class)
        .eq(SysUser::getId, 1)
        .unionAll(Query.create()
        .select(SysUser::getRole_id, SysUser::getId)
        .from(SysUser.class)
        .lt(SysUser::getId, 3)
        .list();
        
```

### 4.1 为空

```java
Integer id = QueryChain.of(sysUserMapper)
        .select(SysUser::getId)
        .from(SysUser.class)
        .eq(SysUser::getId, 2)
        .empty(SysUser::getUserName)
        .returnType(Integer.TYPE)
        .get();
```

### 4.2 不为空

```java
Integer id = QueryChain.of(sysUserMapper)
        .select(SysUser::getId)
        .from(SysUser.class)
        .eq(SysUser::getId, 2)
        .notEmpty(SysUser::getUserName)
        .returnType(Integer.TYPE)
        .get();
```

### 4.3 with 操作

```java
SubQuery subQuery = SubQuery.create("sub")
    .select(SysRole.class)
    .from(SysRole.class)
    .eq(SysRole::getId, 1);

List<SysUser> list = QueryChain.of(sysUserMapper)
    .with(subQuery)
    .selectWithFun(subQuery, SysRole::getId, c -> c.as("xx"))
    .select(subQuery, "id")
    .select(SysUser.class)
    .from(SysUser.class)
    .from(subQuery)
    .eq(SysUser::getRole_id, subQuery.$(subQuery, SysRole::getId))
    .orderBy(subQuery, SysRole::getId)
    .list();
```

> 上面是一个利用with 构建的复杂SQL，核心在
> .with(subQuery)
>
> .from(subQuery)
>
> 把它看成一个子查询

## 函数操作

### 1.1 聚合函数（min,count,max,avg）

```java
Integer count = QueryChain.of(sysUserMapper)
        .select(SysUser::getId, c -> c.count())
        .from(SysUser.class)
        .like(SysUser::getUserName, "test", LikeMode.RIGHT)
        .returnType(Integer.TYPE)
        .count();
```

```java
    selectWithFun(SysUser::getId,c->c.min())
    selectWithFun(SysUser::getId,c->c.max())
    selectWithFun(SysUser::getId,c->c.avg())
```

### 1.2 运算（加,减,乘,除）

```java
selectWithFun(SysUser::getId,c->c.plus(1))
selectWithFun(SysUser::getId,c->c.subtract(1))
selectWithFun(SysUser::getId,c->c.multiply(1))
selectWithFun(SysUser::getId,c->c.divide(1))
```

### 1.3 其他函数

```java
abs，
pow，
concat，
concatAs，
round，
if,
case when
比较函数
gte,gt,lt,lte 等等还很多
```

## 如何结合函数进行条件查询

```java
Integer id = QueryChain.of(sysUserMapper)
    .select(SysUser::getId)
    .from(SysUser.class)
    .and(SysUser::getId, c -> c.concat("x1").eq("2x1"))
    .returnType(Integer.TYPE)
    .get();
```

或

```java
SysUser sysUser = sysUserMapper.get(where -> where.and(SysUser::getId, c -> c.concat("x1").eq("2x1")));
```

## 复杂SQL示例

### exists

```java
boolean exists = QueryChain.of(sysUserMapper)
        .select(SysUser::getId, SysUser::getUserName, SysUser::getRole_id)
        .from(SysUser.class)
        .join(SysUser.class, SysRole.class)
        .like(SysUser::getUserName, "test")
        .exists();
```

### in 一张表的数据

``` java
List<SysUser> list = QueryChain.of(sysUserMapper)
    .select(SysUser::getId, SysUser::getUserName, SysUser::getRole_id)
    .from(SysUser.class)
    .connect(queryChain -> {
        queryChain.in(SysUser::getId, new SubQuery()
                .select(SysUser::getId)
                .from(SysUser.class)
                .connect(subQuery -> {
                    subQuery.eq(SysUser::getId, queryChain.$().field(SysUser::getId));
                })
                .isNotNull(SysUser::getPassword)
                .limit(1)
        );
    })
    .list();
```
sql 如下:
```
SELECT 
    t.id, t.user_name, t.role_id
FROM
    t_sys_user t
WHERE
    t.id IN (SELECT 
            st.id
        FROM
            t_sys_user st
        WHERE
            st.id = t.id AND st.password IS NOT NULL
        LIMIT 1 OFFSET 0)
```

### join 自己

```java
Integer count = QueryChain.of(sysUserMapper)
        .selectWithFun(SysUser::getId, FunctionInterface::count)
        .from(SysUser.class)
        .join(JoinMode.INNER, SysUser.class, 1, SysUser.class, 2, on -> on.eq(SysUser::getId, 1, SysUser::getRole_id, 2))
        .returnType(Integer.TYPE)
        .get();
```
sql 如下：
```java
SELECT 
    COUNT(t.id)
FROM
    t_sys_user t
        INNER JOIN
    t_sys_user t2 ON t.id = t2.role_id
LIMIT 1 OFFSET 0
```


### join 子表

```java
SubQuery subQuery = SubQuery.create("sub")
    .select(SysRole.class)
    .from(SysRole.class)
    .eq(SysRole::getId, 1);

List<SysUser> list = QueryChain.of(sysUserMapper)
    .selectWithFun(subQuery, SysRole::getId, c -> c.as("xx"))
    .select(SysUser.class)
    .from(SysUser.class)
    .join(JoinMode.INNER, SysUser.class, subQuery, on -> on.eq(SysUser::getRole_id, subQuery.$(subQuery, SysRole::getId)))
    .orderBy(subQuery, SysRole::getId)
    .list();
```

> selectWithFun(subQuery, SysRole::getId, c -> c.as("xx")) 返回子表的角色ID并设置别名
sql 如下：
```
SELECT sub.id AS xx , sub.id , t.id , t.user_name , t.password , t.role_id , t.create_time 

FROM t_sys_user t 

INNER JOIN ( 
    SELECT st.id , st.name , st.create_time FROM sys_role st WHERE st.id = 1
) AS sub ON t.role_id = sub.id 

ORDER BY sub.id ASC
```

### select 1 or  select *

```java
new Query().select1();
new Query().selectAll();
```

### select count(1) or select count(*)

```java
new Query().selectCount1();
new Query().selectCountAll();
```


# 批量操作

MybatisBatchUtil 内置了批量保存，批量修改，批量其他操作

## batchSave 批量保存

```java
/**
 * 批量插入（batchSize！=1时，无法获取主键）
 *
 * @param sqlSessionFactory mybatis SqlSessionFactory 通过spring 注解注入获取
 * @param mapperType        MybatisMapper 的 class
 * @param list              数据列表
 * @param batchSize         一次批量处理的条数(如需获取主键，请设置为1)
 * @param <M>               MybatisMapper
 * @param <T>               数据的类型
 * @return 影响的条数
 */
public static <M extends MybatisMapper, T> int batchSave(SqlSessionFactory sqlSessionFactory, Class<M> mapperType, List<T> list, int batchSize) {
    
}
```

## batchUpdate 批量更新

```java
/**
 * 批量更新
 *
 * @param sqlSessionFactory mybatis SqlSessionFactory 通过spring 注解注入获取
 * @param mapperType        MybatisMapper 的 class
 * @param list              数据列表
 * @param batchSize         一次批量处理的条数
 * @param <M>               MybatisMapper
 * @param <T>               数据的类型
 * @return 影响的条数
 */
public static <M extends MybatisMapper, T> int batchUpdate(SqlSessionFactory sqlSessionFactory, Class<M> mapperType, List<T> list, int batchSize) {
    
}
```

## batch（核心）批量

```java
/**
 * 批量操作
 *
 * @param sqlSessionFactory mybatis SqlSessionFactory 通过spring 注解注入获取
 * @param mapperType        MybatisMapper 的 class
 * @param list              数据列表
 * @param batchSize         一次批量处理的条数
 * @param batchFunction     操作方法
 * @param <M>               MybatisMapper
 * @param <T>               数据的类型
 * @return 影响的条数
 */
public static <M extends MybatisMapper, T> int batch(SqlSessionFactory sqlSessionFactory, Class<M> mapperType, List<T> list, int batchSize, MybatisBatchBiConsumer<SqlSession, M, T> batchFunction) {
    
}
```

## 如何使用批量操作

```java
List<IdTest> list = new ArrayList<>(10000);
for (int i = 0; i < 10000; i++) {
    IdTest idTest = new IdTest();
    idTest.setCreateTime(LocalDateTime.now());
    list.add(idTest);
}

MybatisBatchUtil.batchSave(sqlSessionFactory, IdTestMapper.class, list);
```

> sqlSessionFactory 是mybatis SqlSessionFactory 可通过spring 依赖注入获得

> IdTestMapper 是mybatis Mapper 接口

> 如需获取主键，可设置batchSize=1，虽然性能可能下降，也比一般的循环save，update快！
>

# 动态参数配置（用于默认值，逻辑删除）

> 项目启动时设置

```java
MybatisMpConfig.setDefaultValue("{NOW}", (type) -> {
    if (type == LocalDateTime.class) {
        return LocalDateTime.now();
    } else if (type == LocalDate.class) {
        return LocalDate.now();
    } else if (type == Date.class) {
        return new Date();
    } else if (type == Long.class) {
        return System.currentTimeMillis();
    } else if (type == Integer.class) {
        return (int) (System.currentTimeMillis() / 1000);
    }
    throw new RuntimeException("Inconsistent types");
});
```

## 内置动态参数配置

### 1.{BLANK} 空

> 可应用在 字符串，集合，数组上

### 2.{NOW} 当前时间

> 可应用在 LocalDateTime，LocalDate，Date，Long，Integer 字段上

# 如何创建条件，列，表等

## 1.使用命令工厂创建

> Query，QueryChain等中有个方法，专门提供创建sql的工厂类：$()
>
>    可创建 条件，列，表，函数等等，例如

```java
new Query().$().table(...) //创建表
new Query().$().field(...) //创建表的列
new Query().$().columnName(...) //获取数据库列名
new Query().$().gt(...) //创建大于的条件
```

    结合实际使用,例如：

```java
new Query(){{
    select(SysUser::getRole_id)
    .from(SysUser.class)
    .eq($().field(SysUser::getId),1)
    .gt($().table(SysUser.class).$("role_id"),2);
}};
```

甚至 Query 也包含了部分创建 列 表的功能

```java
new Query(){{
    select($(SysUser::getId))
    .from($(SysUser.class))
    .eq($("id"),1);
}};
```
## 2.通过命令模板创建 (完美结合框架)
> 3个模板类：普通模板 CmdTemplate ，函数模板 FunTemplate（后续可继续调用框架的函数），条件模板 ConditionTemplate（用于 where 中）
```java
QueryChain queryChain = QueryChain.of(sysUserMapper);
queryChain.selectWithFun(SysUser::getRole_id, c -> CmdTemplate.create("count({0})+{1}", c, "1"));
queryChain.from(SysUser.class);
queryChain.and(cs -> ConditionTemplate.create("{0}+{1}={2}", cs[0], cs[1], 2), SysUser::getId, SysUser::getId);
queryChain.returnType(String.class);
String str = queryChain.get();
```

```java
QueryChain queryChain = QueryChain.of(sysUserMapper);
queryChain.selectWithFun(SysUser::getRole_id, c -> FunTemplate.create("count({0})",c).plus(1).concat(1,"2",3).length());
queryChain.from(SysUser.class);
queryChain.and(cs -> ConditionTemplate.create("{0}+{1}={2}", cs[0], cs[1], 2), SysUser::getId, SysUser::getId);
queryChain.returnType(String.class);
String str = queryChain.get();
```

# 支持那些数据库函数方法

## 数据库函数使用方法

```java
Integer id = QueryChain.of(sysUserMapper)
    //方法 1    
    .selectWithFun(SysUser::getId, c -> c.sin().as("x_sin"))
    .from(SysUser.class)
    .eq(SysUser::getId, 2)
    //方法 2
    .and(queryChain -> {
        return queryChain.$(SysUser::getCreate_time, c -> c.date().eq("2023-12-10"));
    })
    //方法 3
    .and(queryChain -> {
        return Methods.date(queryChain.$(SysUser::getCreate_time)).eq("2023-12-10");
    })
    .empty(SysUser::getUserName)
    //方法 1     
    .orderBy(SysUser::getId, c -> c.plus(1))
    .returnType(Integer.TYPE)
    .get();
```

## 数据库函数说明

<table>
    <thead>
    <tr align="left">
        <th>支持的函数</th>
        <th>函数说明</th>
    </tr>
    </thead>
    <tbody>
    <tr>
        <td>count</td>
        <td>查询数据总量</td>
    </tr>
    <tr>
        <td>sum</td>
        <td>返回指定字段值的和</td>
    </tr>
    <tr>
        <td>avg</td>
        <td>返回指定列的平均值</td>
    </tr>
    <tr>
        <td>min</td>
        <td>返回指定列的最小值</td>
    </tr>
    <tr>
        <td>max</td>
        <td>返回指定列的最大值</td>
    </tr>
    <tr>
        <td>abs</td>
        <td>返回绝对值</td>
    </tr>
    <tr>
        <td>ceil</td>
        <td>返回大于或等于 x 的最小整数（向上取整）</td>
    </tr>
    <tr>
        <td>floor</td>
        <td>返回小于或等于 x 的最大整数（向下取整）</td>
    </tr>
    <tr>
        <td>rand</td>
        <td>返回 0~1 的随机数</td>
    </tr>
    <tr>
        <td>sign</td>
        <td>返回 x 的符号，x 是负数、0、正数分别返回 -1、0、1</td>
    </tr>
    <tr>
        <td>pi</td>
        <td>返回圆周率</td>
    </tr>
    <tr>
        <td>truncate</td>
        <td>返回数值 x 保留到小数点后 y 位的值</td>
    </tr>
    <tr>
        <td>round</td>
        <td>返回离 x 最近的整数（四舍五入）</td>
    </tr>
    <tr>
        <td>pow</td>
        <td>返回 x 的 y 次方</td>
    </tr>
    <tr>
        <td>power</td>
        <td>返回 x 的 y 次方</td>
    </tr>
    <tr>
        <td>sqrt</td>
        <td>返回 x 的平方根</td>
    </tr>
    <tr>
        <td>exp</td>
        <td>返回 e 的 x 次方</td>
    </tr>
    <tr>
        <td>mod</td>
        <td>取模</td>
    </tr>
    <tr>
        <td>log</td>
        <td>返回自然对数（以 e 为底的对数）</td>
    </tr>
    <tr>
        <td>log2</td>
        <td>返回以 2 为底的对数</td>
    </tr>
    <tr>
        <td>log10</td>
        <td>返回以 10 为底的对数</td>
    </tr>
    <tr>
        <td>radians</td>
        <td>将角度转换为弧度</td>
    </tr>
    <tr>
        <td>degrees</td>
        <td>将弧度转换为角度</td>
    </tr>
    <tr>
        <td>sin</td>
        <td>求正弦值</td>
    </tr>
    <tr>
        <td>asin</td>
        <td>求反正弦值</td>
    </tr>
    <tr>
        <td>cos</td>
        <td>求余弦值</td>
    </tr>
    <tr>
        <td>acos</td>
        <td>求反余弦值</td>
    </tr>
    <tr>
        <td>tan</td>
        <td>求正切值</td>
    </tr>
    <tr>
        <td>atan</td>
        <td>求反正切值</td>
    </tr>
    <tr>
        <td>cot</td>
        <td>求余切值</td>
    </tr>
    <tr>
        <td>charLength</td>
        <td>返回字符串 s 的字符数</td>
    </tr>
    <tr>
        <td>length</td>
        <td>返回字符串 s 的长度</td>
    </tr>
    <tr>
        <td>concat</td>
        <td>字符串拼接</td>
    </tr>
    <tr>
        <td>concatWs</td>
        <td>字符串拼接，需要指定分割符</td>
    </tr>
    <tr>
        <td>upper</td>
        <td>转大写</td>
    </tr>
    <tr>
        <td>lower</td>
        <td>转小写</td>
    </tr>
    <tr>
        <td>left</td>
        <td>左截取</td>
    </tr>
    <tr>
        <td>right</td>
        <td>截取</td>
    </tr>
    <tr>
        <td>lpad</td>
        <td>从左字符串 补全，需要补全的字符和长度</td>
    </tr>
    <tr>
        <td>rpad</td>
        <td>从右字符串 补全，需要补全的字符和长度</td>
    </tr>
    <tr>
        <td>trim</td>
        <td>去除两边的空格</td>
    </tr>
    <tr>
        <td>ltrim</td>
        <td>去除左边的空格</td>
    </tr>
    <tr>
        <td>rtrim</td>
        <td>去除右边的空格</td>
    </tr>
    <tr>
        <td>repeat</td>
        <td>将字符串 s 重复 n 次</td>
    </tr>
    <tr>
        <td>replace</td>
        <td>用字符串 s2 代替字符串 s 中的字符串 s1</td>
    </tr>
    <tr>
        <td>strcmp</td>
        <td>比较字符串 s1 和 s2</td>
    </tr>
    <tr>
        <td>substring</td>
        <td>获取从字符串 s 中的第 n 个位置开始长度为 len 的字符串</td>
    </tr>
    <tr>
        <td>instr</td>
        <td>从字符串 s 中获取 s1 的开始位置</td>
    </tr>
    <tr>
        <td>reverse</td>
        <td>将字符串 s 的顺序反过来</td>
    </tr>
    <tr>
        <td>field</td>
        <td>返回第一个与字符串 s 匹配的字符串的位置</td>
    </tr>
    <tr>
        <td>findInSet</td>
        <td>返回在字符串 s2 中与 s1 匹配的字符串的位置</td>
    </tr>
    <tr>
        <td>currentDate</td>
        <td>返回当前日期</td>
    </tr>
    <tr>
        <td>currentTime</td>
        <td>返回当前时间</td>
    </tr>
    <tr>
        <td>currentDateTime</td>
        <td>返回当前日期和时间</td>
    </tr>
    <tr>
        <td>unixTimestamp</td>
        <td>以 UNIX 时间戳的形式返回当前时间</td>
    </tr>
    <tr>
        <td>fromUnixTime</td>
        <td>把 UNIX 时间戳的时间转换为普通格式的时间</td>
    </tr>
    <tr>
        <td>month</td>
        <td>日期 d 中的月份值，范围是 1~12</td>
    </tr>
    <tr>
        <td>weekday</td>
        <td>日期星期几，0 表示星期一，1 表示星期二</td>
    </tr>
    <tr>
        <td>year</td>
        <td>返回年</td>
    </tr>
    <tr>
        <td>day</td>
        <td>返回日期的天数值</td>
    </tr>
    <tr>
        <td>hour</td>
        <td>返回时间 t 中的小时值</td>
    </tr>
    <tr>
        <td>dateDiff</td>
        <td>计算日期 d1 到 d2 之间相隔的天数</td>
    </tr>
    <tr>
        <td>dateAdd</td>
        <td>日期加操作</td>
    </tr>
    <tr>
        <td>dateSub</td>
        <td>日期减操作</td>
    </tr>
    <tr>
        <td>md5</td>
        <td>字符串md5 加密</td>
    </tr>
    <tr>
        <td>inetAton</td>
        <td>将 IP 地址转换为数字表示，IP 值需要加上引号</td>
    </tr>
    <tr>
        <td>inetNtoa</td>
        <td>将数字 n 转换成 IP 的形式</td>
    </tr>
    </tbody>
</table>

# 多数据源支持
目前仅支持spring项目

[点击前往多数据源文档](./mybatis-mp-routing-datasource/readme.md)

# 如何支持不同数据库

## 如何配置不同类型的数据库

```yaml
mybatis:
  configuration:
    databaseId: MYSQL
```

## 目前数据库支持的类型

 已测试通过的有：
> H2，MYSQL，MARIA_DB ，PGSQL，ORACLE（12c及以上）,SQL SERVER（2012及以上）,DM达梦,DB2

> 其实也支持其他数据库（只要是和以上数据库规范接近的），只是作者没有放开，如有需要，可联系作者放开！

# 代码生成器

## maven引入

```xml

<dependency>
    <groupId>cn.mybatis-mp</groupId>
    <artifactId>mybatis-mp-generator</artifactId>
    <version>1.5.0</version>
    <scope>provided</scope>
</dependency>
```

## 添加数据库驱动 例如：

```xml

<dependency>
    <groupId>com.mysql</groupId>
    <artifactId>mysql-connector-j</artifactId>
    <version>8.0.32</version>
</dependency>
```

## 然后，编写一个任意带有 main 方法的类，如下所示

```java
// 根据数据库链接生成
new FastGenerator(new GeneratorConfig(
        "jdbc:mysql://xxx.xx.x:3306/数据库名字",
        "用户名",
        "密码")
        .basePackage("com.test")//根包路径
        ).create();

        or
        
//根据数据源生成
new FastGenerator(new GeneratorConfig(
        DbType.H2,//数据库类型
        dataSource)
        .basePackage("com.test")//根包路径
        ).create();

```

## 配置 GeneratorConfig

<table>
    <tr align="center">
        <th>属性</th>
        <th>默认值</th>
        <th align="left">说明</th>
    </tr>
    <tr align="center">
        <td>containerType</td>
        <td>SPRING</td>
        <td align="left">
            <p>容器类型，默认SPRING</p>
            <p>目前支持，SPRING、SOLON</p>
        </td>
    </tr>
    <tr align="center">
        <td>swaggerVersion</td>
        <td>3</td>
        <td align="left">swagger版本：2 代表2.x，3代表3.x</td>
    </tr>
    <tr align="center">
        <td>author</td>
        <td>""</td>
        <td align="left">作者</td>
    </tr>
    <tr align="center">
        <td>ignoreView</td>
        <td>false</td>
        <td align="left">是否忽略视图</td>
    </tr>
    <tr align="center">
        <td>ignoreTable</td>
        <td>false</td>
        <td align="left">是否忽略表</td>
    </tr>
    <tr align="center">
        <td>baseFilePath</td>
        <td>System.getProperty("user.dir") + "/demo-generate"</td>
        <td align="left">根文件路径</td>
    </tr>
    <tr align="center">
        <td>basePackage</td>
        <td>""</td>
        <td align="left">根包路径</td>
    </tr>
    <tr align="center">
        <td>templateRootPath</td>
        <td>templates</td>
        <td align="left">模板根目录，默认即可</td>
    </tr>
    <tr align="center">
        <td>templateEngine</td>
        <td>new FreemarkerTemplateEngine()</td>
        <td align="left">模板引擎，默认Freemarker引擎，其他引擎需要自己实现</td>
    </tr>
    <tr align="center">
        <td>templateBuilders</td>
        <td>包含 实体类，mapper,mapper xml,dao,service,serviceImpl,action等模板生成构建器</td>
        <td align="left">模板生成构建器，继承AbstractTemplateBuilder，即可实现自己的生成器（生成自己的页面或其他类等）</td>
    </tr>
</table>

## 配置 TableConfig(表配置)

```java
new GeneratorConfig(...).tableConfig(tableConfig->{
    tableConfig.includeTables("table1","table2");
});
```

<table>
    <tr align="center">
        <th>属性</th>
        <th>默认值</th>
        <th align="left">说明</th>
    </tr>
    <tr align="center">
        <td>tablePrefixs</td>
        <td>空</td>
        <td align="left">表、视图的前缀，用于生成类名时忽略前缀</td>
    </tr>
    <tr align="center">
        <td>includeTables</td>
        <td>空</td>
        <td align="left">默认包含所有表、视图</td>
    </tr>
    <tr align="center">
        <td>excludeTables</td>
        <td>空</td>
        <td align="left">排除表，默认不排除</td>
    </tr>
</table>

## 配置 ColumnConfig(列配置)

```java
new GeneratorConfig(...).columnConfig(columnConfig->{
    columnConfig.excludeColumns("create_time","creater_id");
});
```

<table>
    <tr align="center">
        <th>属性</th>
        <th>默认值</th>
        <th align="left">说明</th>
    </tr>
    <tr align="center">
        <td>versionColumn</td>
        <td>空</td>
        <td align="left">指定乐观锁列名</td>
    </tr>
    <tr align="center">
        <td>tenantIdColumn</td>
        <td>空</td>
        <td align="left">指定租户ID列名</td>
    </tr>
    <tr align="center">
        <td>logicDeleteColumn</td>
        <td>空</td>
        <td align="left">逻辑删除列名，配置实体类配置：logicDeleteCode 一起使用</td>
    </tr>
    <tr align="center">
        <td>excludeColumns</td>
        <td>空</td>
        <td align="left">排除列，默认不排除<strong>（在有公共实体类的时候很实用）</strong></td>
    </tr>
    <tr align="center">
        <td>disableUpdateColumns</td>
        <td>空</td>
        <td align="left">禁止更新的列,这样字段上会生成<strong>@TableField(update=false)</strong></td>
    </tr>
    <tr align="center">
        <td>disableSelectColumns</td>
        <td>空</td>
        <td align="left">禁止Select的列,这样字段上会生成<strong>@TableField(select=false)</strong></td>
    </tr>
    <tr align="center">
        <td>defaultValueConvert</td>
        <td>默认实现</td>
        <td align="left">可动态转换数据库的默认值（由静态值转成动态值）</td>
    </tr>
</table>

## 配置 EntityConfig(实体类配置)

```java
new GeneratorConfig(...).entityConfig(entityConfig->{
    entityConfig.lombok(true);
});
```

<table>
    <tr align="center">
        <th>属性</th>
        <th>默认值</th>
        <th align="left">说明</th>
    </tr>
    <tr align="center">
        <td>swagger</td>
        <td>false</td>
        <td align="left">是否开启swagger</td>
    </tr>
    <tr align="center">
        <td>superClass</td>
        <td>NULL</td>
        <td align="left">实体类的父类，例如：com.xx.test.BaseEntity</td>
    </tr>
    <tr align="center">
        <td>lombok</td>
        <td>true</td>
        <td align="left">是否开启lombok，这样类上会生成<strong>@Data</strong></td>
    </tr>
    <tr align="center">
        <td>schema</td>
        <td>false</td>
        <td align="left">注解上是否加上schema信息</td>
    </tr>
    <tr align="center">
        <td>packageName</td>
        <td>DO</td>
        <td align="left">实体类包名</td>
    </tr>
    <tr align="center">
        <td>nameConvert</td>
        <td>NULL</td>
        <td align="left">实体类名转换器，可以自定义规则,默认大驼峰规则</td>
    </tr>
    <tr align="center">
        <td>fieldNamingStrategy</td>
        <td>NamingStrategy.UNDERLINE_TO_CAMEL</td>
        <td align="left">字段名策略，支持 NO_CHANGE ，UNDERLINE_TO_CAMEL </td>
    </tr>
    <tr align="center">
        <td>fieldNameConverter</td>
        <td>NULL</td>
        <td align="left">字段名转换器，优先级大于 fieldNamingStrategy</td>
    </tr>
    <tr align="center">
        <td>remarksConverter</td>
        <td>NULL</td>
        <td align="left">字段备注转换器，用于实现不一样的备注</td>
    </tr>
    <tr align="center">
        <td>defaultTableIdCode</td>
        <td>NULL</td>
        <td align="left">默认TableId代码，数据库非自增时生效,例如@TableId(...)</td>
    </tr>
    <tr align="center">
        <td>logicDeleteCode</td>
        <td>NULL</td>
        <td align="left">默认@LogicDelete代码，数据库非自增时生效,例如@LogicDelete(beforeValue="0",afterValue="1",deleteTimeField="create_time</td>
    </tr>
    <tr align="center">
        <td>typeMapping</td>
        <td>内置包含各种列类型的java映射</td>
        <td align="left">数据库列类型映射，用于定制</td>
    </tr>
</table>

## 配置 MapperConfig(mapper类配置)

```java
new GeneratorConfig(...).mapperConfig(entityConfig->{
    mapperConfig.mapperAnnotation(true);
});
```

<table>
    <tr align="center">
        <th>属性</th>
        <th>默认值</th>
        <th align="left">说明</th>
    </tr>
    <tr align="center">
        <td>superClass</td>
        <td>默认继承 MybatisMapper 接口</td>
        <td align="left">Mapper接口的父接口，例如：cn.mybatis.mp.core.mybatis.mapper.MybatisMapper</td>
    </tr>
    <tr align="center">
        <td>mapperAnnotation</td>
        <td>true</td>
        <td align="left">是否开启mybatis @Mapper注解，这样类上会生成<strong>@Mapper</strong></td>
    </tr>
    <tr align="center">
        <td>packageName</td>
        <td>mapper</td>
        <td align="left">mapper类的包名</td>
    </tr>
    <tr align="center">
        <td>suffix</td>
        <td>Mapper</td>
        <td align="left">mapper类的后缀</td>
    </tr>
</table>

## 配置 MapperXmlConfig(mapper xml配置)

```java
new GeneratorConfig(...).mapperXmlConfig(mapperXmlConfig->{
    mapperXmlConfig.enable(true);
});
```

<table>
    <tr align="center">
        <th>属性</th>
        <th>默认值</th>
        <th align="left">说明</th>
    </tr>
    <tr align="center">
        <td>enable</td>
        <td>false</td>
        <td align="left">是否生成mapper xml</td>
    </tr>
    <tr align="center">
        <td>resultMap</td>
        <td>true</td>
        <td align="left">是否生成resultMap</td>
    </tr>
    <tr align="center">
        <td>columnList</td>
        <td>true</td>
        <td align="left">是否生成列信息，用于select 列</td>
    </tr>
    <tr align="center">
        <td>packageName</td>
        <td>mappers</td>
        <td align="left">mapper xml的目录名字</td>
    </tr>
    <tr align="center">
        <td>suffix</td>
        <td>""</td>
        <td align="left">mapper xml文件的后缀</td>
    </tr>
</table>

## 配置 DaoConfig(dao接口配置)

```java
new GeneratorConfig(...).daoConfig(daoConfig->{
    daoConfig.enable(true);
});
```

<table>
    <tr align="center">
        <th>属性</th>
        <th>默认值</th>
        <th align="left">说明</th>
    </tr>
    <tr align="center">
        <td>enable</td>
        <td>false</td>
        <td align="left">是否生成 dao 接口</td>
    </tr>
    <tr align="center">
        <td>superClass</td>
        <td>默认继承 Dao 接口</td>
        <td align="left">dao接口的父接口，例如：cn.mybatis.mp.core.mvc.Dao</td>
    </tr>
    <tr align="center">
        <td>generic</td>
        <td>true</td>
        <td align="left">是否启用泛型，启用后会在superclass后面加泛型&gt;Entity,ID></td>
    </tr>
    <tr align="center">
        <td>packageName</td>
        <td>dao</td>
        <td align="left">dao接口的包名</td>
    </tr>
    <tr align="center">
        <td>suffix</td>
        <td>Dao</td>
        <td align="left">dao接口的后缀</td>
    </tr>
</table>

## 配置 DaoImplConfig(dao接口实现类的配置)

```java
new GeneratorConfig(...).daoImplConfig(daoImplConfig->{
    daoImplConfig.enable(true);
});
```

<table>
    <tr align="center">
        <th>属性</th>
        <th>默认值</th>
        <th align="left">说明</th>
    </tr>
    <tr align="center">
        <td>superClass</td>
        <td>默认继承 DaoImpl 实现类</td>
        <td align="left">dao接口的父接口，例如：cn.mybatis.mp.core.mvc.impl.DaoImpl</td>
    </tr>
    <tr align="center">
        <td>generic</td>
        <td>true</td>
        <td align="left">是否启用泛型，启用后会在superclass后面加泛型&gt;Entity,ID></td>
    </tr>
    <tr align="center">
        <td>packageName</td>
        <td>dao.impl</td>
        <td align="left">dao实现类的包名</td>
    </tr>
    <tr align="center">
        <td>suffix</td>
        <td>DaoImpl</td>
        <td align="left">dao实现类的后缀</td>
    </tr>
</table>

## 配置 ServiceConfig(service接口配置)

```java
new GeneratorConfig(...).serviceConfig(serviceConfig->{
    serviceConfig.enable(true);
});
```

<table>
    <tr align="center">
        <th>属性</th>
        <th>默认值</th>
        <th align="left">说明</th>
    </tr>
    <tr align="center">
        <td>enable</td>
        <td>false</td>
        <td align="left">是否生成 Service 接口</td>
    </tr>
    <tr align="center">
        <td>superClass</td>
        <td>默认继承 Service 接口</td>
        <td align="left">Service接口的父接口，例如：cn.mybatis.mp.core.mvc.Service</td>
    </tr>
    <tr align="center">
        <td>generic</td>
        <td>true</td>
        <td align="left">是否启用泛型，启用后会在superclass后面加泛型&gt;Entity,ID></td>
    </tr>
    <tr align="center">
        <td>packageName</td>
        <td>service</td>
        <td align="left">Service接口的包名</td>
    </tr>
    <tr align="center">
        <td>suffix</td>
        <td>Service</td>
        <td align="left">Service接口的后缀</td>
    </tr>
</table>

## 配置 ServiceImplConfig(service接口实现类的配置)

```java
new GeneratorConfig(...).serviceImplConfig(serviceImplConfig->{
    serviceImplConfig.injectDao(true);
});
```

<table>
    <tr align="center">
        <th>属性</th>
        <th>默认值</th>
        <th align="left">说明</th>
    </tr>
    <tr align="center">
        <td>injectDao</td>
        <td>true</td>
        <td align="left">是否注入dao</td>
    </tr>
    <tr align="center">
        <td>injectMapper</td>
        <td>true</td>
        <td align="left">是否注入mapper</td>
    </tr>
    <tr align="center">
        <td>superClass</td>
        <td>默认继承 ServiceImpl 实现类</td>
        <td align="left">dao接口的父接口，例如：cn.mybatis.mp.core.mvc.impl.ServiceImpl</td>
    </tr>
    <tr align="center">
        <td>generic</td>
        <td>true</td>
        <td align="left">是否启用泛型，启用后会在superclass后面加泛型&gt;Entity,ID></td>
    </tr>
    <tr align="center">
        <td>packageName</td>
        <td>service.impl</td>
        <td align="left">service实现类的包名</td>
    </tr>
    <tr align="center">
        <td>suffix</td>
        <td>ServiceImpl</td>
        <td align="left">service实现类的后缀</td>
    </tr>
</table>

## 配置 ActionConfig(action实现类的配置)

```java
new GeneratorConfig(...).actionConfig(actionConfig->{
    actionConfig.enable(true);
});
```

<table>
    <tr align="center">
        <th>属性</th>
        <th>默认值</th>
        <th align="left">说明</th>
    </tr>
    <tr align="center">
        <td>enable</td>
        <td>true</td>
        <td align="left">是否生成控制器</td>
    </tr>
    <tr align="center">
        <td>swagger</td>
        <td>true</td>
        <td align="left">是否开启swagger</td>
    </tr>
    <tr align="center">
        <td>injectService</td>
        <td>true</td>
        <td align="left">是否注入service</td>
    </tr>
    <tr align="center">
        <td>superClass</td>
        <td>NULL</td>
        <td align="left">action父类，例如：cn.xxx.BaseAction</td>
    </tr>
    <tr align="center">
        <td>generic</td>
        <td>true</td>
        <td align="left">是否启用泛型，启用后会在superclass后面加泛型&gt;Entity,ID></td>
    </tr>
    <tr align="center">
        <td>packageName</td>
        <td>action</td>
        <td align="left">action实现类的包名</td>
    </tr>
    <tr align="center">
        <td>suffix</td>
        <td>ServiceImpl</td>
        <td align="left">action实现类的后缀</td>
    </tr>
    <tr align="center">
        <td>returnClass</td>
        <td>Object</td>
        <td align="left">save update 等返回的类型</td>
    </tr>
    <tr align="center">
        <td>save</td>
        <td>true</td>
        <td align="left">是否生成save方法</td>
    </tr>
    <tr align="center">
        <td>update</td>
        <td>true</td>
        <td align="left">是否生成update方法</td>
    </tr>
    <tr align="center">
        <td>deleteById</td>
        <td>true</td>
        <td align="left">是否生成deleteById方法</td>
    </tr>
    <tr align="center">
        <td>getById</td>
        <td>true</td>
        <td align="left">是否生成getById方法</td>
    </tr>
    <tr align="center">
        <td>find</td>
        <td>true</td>
        <td align="left">是否生成find方法</td>
    </tr>
</table>

# 如何扩展 SQL指令

## 1.继承 Cmd 实现 sql 方法即可

## 2.联系作者 帮忙 实现

> 扩展起来，非常方便，不过最好是联系作者，这样可以让更多开发者使用！

# 支持作者，赏作者一盒盒饭（^o^）

<img src="./doc/image/alipay.png" style="width:500px">
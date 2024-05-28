#### 喜欢的朋友加入QQ群：<font color="red">121908790</font> ，群里不仅可以提mybatis-mp框架问题，还可以帮你解决后端的各种问题！
##### 另外，喜欢的朋友，帮忙关注 和 star（点点小爱心）！官网文档：http://mybatis-mp.cn !!!


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
            <version>1.5.1</version>
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
            <version>1.5.0-spring-boot3</version>
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
## 开始使用
```
List<SysUser> list = QueryChain.of(sysUserMapper)
    // forSearch包含忽略null 、空字符串、对字符串进行trim去空格    
    .forSearch()
    .eq(SysUser::getId,1)
    .like(SysUser::getUserName," admin ")
    .list();
```
> 优雅 简单 方便 快捷

# 支持作者，赏作者一盒盒饭（^o^）

<img src="./doc/image/alipay.png" style="width:500px">
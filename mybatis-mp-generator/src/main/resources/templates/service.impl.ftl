package ${entityInfo.serviceImplPackage};

<#list imports as pkg>
import ${pkg};
</#list>

/**
 * <p>
 * ${entityInfo.tableInfo.remarks!} Service 实现类
 * </p>
 *
 * @author ${author!}
 * @since ${date}
 */
@${serviceAnnotationName}
public class ${entityInfo.serviceImplName}${superExtend}<#if serviceConfig.isGeneric()><${entityInfo.name},<#if entityInfo.idFieldInfo??>${entityInfo.idFieldInfo.typeName}<#else>Void</#if>></#if> <#if serviceConfig.isEnable()>implements ${entityInfo.serviceName}</#if> {

<#if serviceImplConfig.isInjectDao()>
    @${autowiredAnnotationName}
    private ${serviceImplConfig.injectDaoClassName(entityInfo)} ${util.firstToLower(serviceImplConfig.injectDaoClassName(entityInfo))};
    
</#if>
<#if !serviceImplConfig.isInjectDao() && serviceImplConfig.isInjectMapper()>
    @${autowiredAnnotationName}
    private ${serviceImplConfig.mapperClassName(entityInfo)} ${util.firstToLower(serviceImplConfig.mapperClassName(entityInfo))};

    protected ${entityInfo.mapperName} getMapper(){
        return this.${util.firstToLower(entityInfo.mapperName)};
    }

    private QueryChain<${entityInfo.name}> queryChain() {
        return QueryChain.of(${util.firstToLower(serviceImplConfig.mapperClassName(entityInfo))});
    }

    private UpdateChain updateChain() {
        return UpdateChain.of(${util.firstToLower(serviceImplConfig.mapperClassName(entityInfo))});
    }

    private InsertChain insertChain(){
        return InsertChain.of(${util.firstToLower(serviceImplConfig.mapperClassName(entityInfo))});
    }

    private DeleteChain deleteChain(){
        return DeleteChain.of(${util.firstToLower(serviceImplConfig.mapperClassName(entityInfo))});
    }

</#if>
}

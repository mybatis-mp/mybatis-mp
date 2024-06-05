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
public class ${entityInfo.serviceImplName} ${superExtend}<#if serviceConfig.isGeneric()><${entityInfo.name},<#if entityInfo.idFieldInfo??>${entityInfo.idFieldInfo.typeName}<#else>Void</#if>></#if> implements ${entityInfo.serviceName}{

<#if serviceImplConfig.isInjectDao()>
    @${autowiredAnnotationName}
    private ${entityInfo.daoName} ${util.firstToLower(entityInfo.daoName)};

</#if>
<#if !serviceImplConfig.isInjectDao()>
    @${autowiredAnnotationName}
    private ${entityInfo.mapperName} ${util.firstToLower(entityInfo.mapperName)};

    private QueryChain<${entityInfo.name}> queryChain() {
        return QueryChain.of(${util.firstToLower(entityInfo.mapperName)});
    }

    private UpdateChain updateChain() {
        return UpdateChain.of(${util.firstToLower(entityInfo.mapperName)});
    }

    private InsertChain insertChain(){
        return InsertChain.of(${util.firstToLower(entityInfo.mapperName)});
    }

    private DeleteChain deleteChain(){
        return DeleteChain.of(${util.firstToLower(entityInfo.mapperName)});
    }

</#if>
}

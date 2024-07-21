package ${entityInfo.daoImplPackage};

<#list imports as pkg>
import ${pkg};
</#list>

/**
 * <p>
 * ${entityInfo.tableInfo.remarks!} Dao 实现类
 * </p>
 *
 * @author ${author!}
 * @since ${date}
 */
@${repositoryAnnotationName}
public class ${entityInfo.daoImplName} ${superExtend}<#if daoConfig.isGeneric()><${entityInfo.name},<#if entityInfo.idFieldInfo??>${entityInfo.idFieldInfo.typeName}<#else>Void</#if>></#if> implements ${entityInfo.daoName}{

<#if containerType.is("solon")>
    @${autowiredAnnotationName}
    private ${entityInfo.mapperName} ${util.firstToLower(entityInfo.mapperName)};

    @Init
    public void init(){
        this.setMapper(${util.firstToLower(entityInfo.mapperName)});
    }

    private ${entityInfo.mapperName} getMapper(){
        return this.${util.firstToLower(entityInfo.mapperName)};
    }
<#else>
    @${autowiredAnnotationName}
    public ${entityInfo.daoImplName} (${entityInfo.mapperName} ${util.firstToLower(entityInfo.mapperName)}){
        super(${util.firstToLower(entityInfo.mapperName)});
    }

    private ${entityInfo.mapperName} getMapper(){
        return (${entityInfo.mapperName}) this.mapper;
    }
</#if>
}

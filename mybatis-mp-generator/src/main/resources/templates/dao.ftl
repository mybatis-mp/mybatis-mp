package ${entityInfo.daoPackage};

<#list imports as pkg>
import ${pkg};
</#list>

/**
 * <p>
 * ${entityInfo.tableInfo.remarks!} Dao 接口
 * </p>
 *
 * @author ${author!}
 * @since ${date}
 */
public interface ${entityInfo.daoName}${superExtend}<#if daoConfig.isGeneric()><${entityInfo.name}, <#if entityInfo.idFieldInfo??>${entityInfo.idFieldInfo.typeName}<#else>Void</#if>></#if> {
<#if entityInfo.hasMultiId()>
     /**
      * 根据多个主键查询
      */
     ${entityInfo.name} getById(<#list entityInfo.idFieldInfoList as field>${field.typeName} ${field.name}<#if field_has_next>, </#if></#list>);
</#if>
}

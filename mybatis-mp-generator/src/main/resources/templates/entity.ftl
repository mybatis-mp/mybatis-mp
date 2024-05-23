package ${entityInfo.entityPackage};

<#list imports as pkg>
import ${pkg};
</#list>
<#if entityConfig.isSwagger()>
    <#if generatorConfig.getSwaggerVersion() == 3>
import io.swagger.v3.oas.annotations.media.Schema;
    <#else>
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
    </#if>
</#if>

/**
 * <p>
 * ${entityInfo.tableInfo.remarks!}
 * </p>
 *
 * @author ${author!}
 * @since ${date}
 */
<#if entityConfig.isLombok()>
    @Data
</#if>
<#if entityConfig.isSwagger()>
<#if generatorConfig.getSwaggerVersion() == 3>
@Schema(name = "${entityInfo.tableInfo.remarks!}")
<#else>
@ApiModel("${entityInfo.tableInfo.remarks!}")
</#if>
</#if>
@Table(value="${entityInfo.tableInfo.name}"<#if entityConfig.isSchema()>,schema="${entityInfo.tableInfo.schema!}"</#if>)
public class ${entityInfo.name} ${superExtend}{

<#list entityInfo.fieldInfoList as field>
<#if field.remarks?? && field.remarks != "">
    /**
     * ${field.remarks!}
     */
</#if>
<#if field.columnInfo.primaryKey>
    ${field.buildTableIdCode()!}
    private ${field.typeName} ${field.name};
<#else>
<#if field.columnInfo.isVersion()>
    @Version
<#elseif  field.columnInfo.isTenantId()>
    @TenantId
<#elseif field.columnInfo.isLogicDelete()>
    ${entityConfig.getLogicDeleteCode()!}
</#if>
<#if entityConfig.isSwagger()>
<#if generatorConfig.getSwaggerVersion() == 3>
    @Schema(description = "${field.remarks!}")
<#else>
    @ApiModelProperty("${field.remarks!}")
</#if>
</#if>
    <#if field.isNeedTableField()>
    ${field.buildTableField()}
    private ${field.typeName} ${field.name};
<#else>
    private ${field.typeName} ${field.name};
</#if>
</#if>

</#list>
<#if entityConfig.isLombok() == false>
<#list entityInfo.fieldInfoList as field>
    public void ${field.setterMethodName()} (${field.typeName} ${field.name}) {
        this.${field.name} = ${field.name};
    }

    public ${field.typeName} ${field.getterMethodName()} () {
        return this.${field.name};
    }

</#list>
</#if>
<#if !entityConfig.isLombok()>
    @Override
    public String toString() {
        return "${entityInfo.name}{" +
    <#list entityInfo.fieldInfoList as field>
        <#if field_index==0>
            "${field.name} = " + ${field.name} +
        <#else>
            ", ${field.name} = " + ${field.name} +
        </#if>
    </#list>
        "}";
    }
</#if>
}

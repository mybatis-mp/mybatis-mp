package ${entityInfo.actionPackage};

<#list imports as pkg>
import ${pkg};
</#list>
<#if actionConfig.isSwagger()>
    <#if generatorConfig.getSwaggerVersion() == 3>
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
    <#else>
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
    </#if>
</#if>

/**
 * <p>
 * ${entityInfo.tableInfo.remarks!} 控制器
 * </p>
 *
 * @author ${author!}
 * @since ${date}
 */
@${controllerAnnotationName}
@${requestMappingAnnotationName}("/${util.firstToLower(entityInfo.daoName)}")
<#if actionConfig.isSwagger()>
    <#if generatorConfig.getSwaggerVersion() == 3>
@Tag(name = "${entityInfo.tableInfo.remarks!}")
    <#else>
@Api("${entityInfo.tableInfo.remarks!}-接口")
    </#if>
</#if>
public class ${entityInfo.actionName} ${superExtend}<#if actionConfig.isGeneric()><${entityInfo.name},<#if entityInfo.idFieldInfo??>${entityInfo.idFieldInfo.typeName}<#else>Void</#if>></#if>{

<#if actionConfig.isInjectService() && serviceConfig.isEnable()>
    @${autowiredAnnotationName}
    private ${entityInfo.serviceName} ${util.firstToLower(entityInfo.serviceName)};

</#if>
<#if actionConfig.isGetById()  && entityInfo.hasId()>
<#if generatorConfig.getContainerType().is("solon")>
    @Get
</#if>
    @${getMappingAnnotationName}("${actionConfig.getGetByIdUriPath()}")
<#if actionConfig.isSwagger()>
    <#if generatorConfig.getSwaggerVersion() == 3>
    @Operation(summary = "根据ID查询")
    <#list entityInfo.idFieldInfoList as field>
    @Parameter(name = "${field.name}", description = "${field.remarks}", required = true)
    </#list>
    <#else >
    @ApiOperation("根据ID查询")
    <#list entityInfo.idFieldInfoList as field>
    @ApiParam(name = "${field.name}", value = "${field.remarks}", required = true)
    </#list>
    </#if>
</#if>
    public ${actionConfig.returnClassName} ${actionConfig.getGetByIdMethodName()}(<#list entityInfo.idFieldInfoList as field>${field.typeName} ${field.name}<#if field_has_next>, </#if></#list>){
        // TODO 代码自动生成 未实现（注意）
        return null;
    }

</#if>
<#if actionConfig.isSave()>
<#if generatorConfig.getContainerType().is("solon")>
    @Post
</#if>
    @${postMappingAnnotationName}("${actionConfig.getSaveUriPath()}")
<#if actionConfig.isSwagger()>
    <#if generatorConfig.getSwaggerVersion() == 3>
    @Operation(summary = "新增")
    <#else >
    @ApiOperation("新增")
    </#if>
</#if>
    public ${actionConfig.returnClassName} ${actionConfig.getSaveMethodName()}(${entityInfo.name} ${util.firstToLower(entityInfo.name)}){
        // TODO 代码自动生成 未实现（注意）
        return null;
    }

</#if>
<#if actionConfig.isUpdate()>
<#if generatorConfig.getContainerType().is("solon")>
    @Post
</#if>
    @${postMappingAnnotationName}("${actionConfig.getUpdateUriPath()}")
<#if actionConfig.isSwagger()>
    <#if generatorConfig.getSwaggerVersion() == 3>
    @Operation(summary = "修改")
    <#else >
    @ApiOperation("修改")
    </#if>
</#if>
    public ${actionConfig.returnClassName} ${actionConfig.getUpdateMethodName()}(${entityInfo.name} ${util.firstToLower(entityInfo.name)}){
        // TODO 代码自动生成 未实现（注意）
        return null;
    }

</#if>
<#if actionConfig.isDeleteById() && entityInfo.hasId()>
<#if generatorConfig.getContainerType().is("solon")>
    @Delete
</#if>
    @${deleteMappingAnnotationName}("${actionConfig.getDeleteByIdUriPath()}")
<#if actionConfig.isSwagger()>
    <#if generatorConfig.getSwaggerVersion() == 3>
    @Operation(summary = "根据ID删除")
    <#list entityInfo.idFieldInfoList as field>
    @Parameter(name = "${field.name}", description = "${field.remarks}", required = true)
    </#list>
    <#else >
    @ApiOperation("根据ID删除")
    <#list entityInfo.idFieldInfoList as field>
    @ApiParam(name = "${field.name}", value = "${field.remarks}", required = true)
    </#list>
    </#if>
</#if>
    public ${actionConfig.returnClassName} ${actionConfig.getDeleteByIdMethodName()}(<#list entityInfo.idFieldInfoList as field>${field.typeName} ${field.name}<#if field_has_next>, </#if></#list>){
        // TODO 代码自动生成 未实现（注意）
        return null;
    }

</#if>
<#if actionConfig.isFind()>
<#if generatorConfig.getContainerType().is("solon")>
    @Get
</#if>
    @${getMappingAnnotationName}("${actionConfig.getFindUriPath()}")
<#if actionConfig.isSwagger()>
    <#if generatorConfig.getSwaggerVersion() == 3>
    @Operation(summary = "分页查询")
    @Parameter(name = "pager", description = "分页信息")
    <#else >
    @ApiOperation("分页查询")
    @ApiParam(name = "pager", value = "分页信息")
    </#if>
</#if>
    public ${actionConfig.returnClassName} ${actionConfig.getFindMethodName()}(Pager<${entityInfo.name}> pager){
        // TODO 代码自动生成 未实现（注意）
        return null;
    }

</#if>
}

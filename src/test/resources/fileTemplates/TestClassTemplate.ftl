package ${classSpecification.pack};

import lombok.Data;
import

@Entity
<#if classSpecification.table??>
@Table(
    <#if classSpecification.table.name??>
    name = classSpecification.table.name,
    </#if>
    <#if classSpecification.table.catalog??>
    catalog = classSpecification.table.catalog,
    </#if>
    <#if classSpecification.table.schema??>
    schema = classSpecification.table.schema,
    </#if>
    )
</#if>
<#if classSpecification.immutable?? && classSpecification.immutable== true>
@Immutable
</#if>
@Data
public class ${classSpecification.className} {
    <#list classSpecification.fields as field>
        private ${field.type} ${field.name};
    </#list>
}

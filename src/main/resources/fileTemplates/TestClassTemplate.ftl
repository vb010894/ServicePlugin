package ${classSpecification.pack};

import lombok.Data;

@Entity
<#if classSpecification.table != "null">
@Table(
    <#if classSpecification.table.name != "null">
    name = classSpecification.table.name,
    </#if>
    <#if classSpecification.table.catalog != "null">
    catalog = classSpecification.table.catalog,
    </#if>
    <#if classSpecification.table.schema != "null">
    schema = classSpecification.table.schema,
    </#if>
    )
</#if>
@Data
${tableMarker}

public class ${classSpecification.name} {
    <#list classSpecification.fieldSpecifications as field>
        private ${field.type} ${field.name};
    </#list>
}

package ${classSpecification.pack};

import lombok.Data;

<#assign tableMarker = classSpecification.table>

<#if classSpecification.table == "null">
    @Table
    <#else>
    @Repository
</#if>
@Data
${tableMarker}

public class ${classSpecification.name} {
    <#list classSpecification.fieldSpecifications as field>
        private ${field.type} ${field.name};
    </#list>
}

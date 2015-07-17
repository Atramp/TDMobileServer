var imgs = [
<#if fileNames?exists>
    <#list fileNames as fileName>
        <#if fileName_index = fileNames?size - 1>
            '${fileName}'
        <#else >
            '${fileName}',
        </#if>
    </#list>
</#if>
];
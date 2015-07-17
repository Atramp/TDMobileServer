CACHE MANIFEST

CACHE:
<#if fileNames?exists>
    <#list fileNames as fileName>
        img/${fileName}
    </#list>
</#if>

/resources/js/jquery/jquery-1.10.2.min.js
pictures.js?v=${version}
main.html

VERSION: ${version}
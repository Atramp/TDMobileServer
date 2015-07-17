<chart <#include "chart_root.ftl"> >
	<#list serials as serial >
		<dataset>
			<#list serial.dataset as item> 
	        	<set label="${item.label}" value="${item.value}" tooltext="${item.tip}" color="${item.color}"/>
            </#list>
		</dataset>
	</#list>
</chart>
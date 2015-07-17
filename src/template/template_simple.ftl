<chart <#include "chart_root.ftl"> >
	<#list serials as serial >
		<dataset>
			<#list serial.dataset as item> 
	        	<set label="${item.label}" value="${item.value}" tooltext="${item.tip}"/>
			</#list>
		</dataset>
	</#list>
</chart>
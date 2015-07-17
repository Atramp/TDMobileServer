<chart  <#include "chart_root.ftl"> >
	<categories>
    <#list categories as category>
    	<category label="${category}" />
    </#list>
    </categories>

    <#list serials as serial >
        <dataset parentYAxis="${serial.parentYAxis}" seriesname="${serial.serialName}" renderAs="${serial.renderAs}">
	        <#list serial.dataset as item>
	        	<set label="${item.label}" value="${item.value}" tooltext="${item.tip}"/>
	        </#list>
        </dataset>
     </#list>
</chart>
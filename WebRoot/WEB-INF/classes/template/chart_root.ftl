caption="${caption}"
subCaption="${subCaption}"
xAxisName="${xAxisName}"
yAxisName="${yAxisName}"

bgColor="${bgColor}"
showBorder="${showBorder}"

showvalues="${showvalues}" 
showLabels="${showLabels}"

numberSuffix="${numberSuffix}"
sNumberSuffix="${sNumberSuffix}"

use3DLighting="0"
plotGradientColor=""
showPlotBorder="0"

<#if extensions?exists>
	<#list extensions?keys as key>
		${key} = "${extensions[key]}"
	</#list>
</#if>
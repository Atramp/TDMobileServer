
<map caption="${caption}" subCaption="${subCaption}" showCanvasBorder="0" borderAlpha="0"  hoverColor="E6EAEE" >
	<colorRange>
		<#list colors as color>
			<color minValue="${color.minValue}" maxValue="${color.maxValue}" displayValue="${color.displayValue}" color="${color.color}"/>
		</#list>
	</colorRange>
	<data>
		<#list entities as entity>
			<entity id="${entity.id}" displayValue="${entity.displayValue}" value="${entity.value}" toolText="${entity.toolText}" color="${entity.color}"/>
		</#list>
	</data>
</map>
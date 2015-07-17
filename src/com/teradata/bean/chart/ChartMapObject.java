package com.teradata.bean.chart;

import java.util.ArrayList;
import java.util.List;

public class ChartMapObject extends ChartObject {
	private List<Color> colors = null;
	private List<Entity> entities = null;
	
	public void addColor(Color color){
		if(null == colors){
			colors = new ArrayList<Color>();
		}
		colors.add(color);
	}
	
	public void addEntity(Entity entity){
		if(null == entities){
			entities = new ArrayList<Entity>();
		}
		entities.add(entity);
	}
	
	public class Color{
		private String minValue;
		private String maxValue;
		private String displayValue;
		private String color;
		
		public Color(){
			
		}
		
		public Color(String minValue, String maxValue, String displayValue, String color) {
			super();
			this.minValue = minValue;
			this.maxValue = maxValue;
			this.displayValue = displayValue;
			this.color = color;
		}
		/**
		 * @return the minValue
		 */
		public String getMinValue() {
			return minValue;
		}
		/**
		 * @param minValue the minValue to set
		 */
		public void setMinValue(String minValue) {
			this.minValue = minValue;
		}
		/**
		 * @return the maxValue
		 */
		public String getMaxValue() {
			return maxValue;
		}
		/**
		 * @param maxValue the maxValue to set
		 */
		public void setMaxValue(String maxValue) {
			this.maxValue = maxValue;
		}
		/**
		 * @return the displayValue
		 */
		public String getDisplayValue() {
			return displayValue;
		}
		/**
		 * @param displayValue the displayValue to set
		 */
		public void setDisplayValue(String displayValue) {
			this.displayValue = displayValue;
		}
		/**
		 * @return the color
		 */
		public String getColor() {
			return color;
		}
		/**
		 * @param color the color to set
		 */
		public void setColor(String color) {
			this.color = color;
		}
		
	}
	
	public class Entity{
		private String id;
		private String value;
		private String displayValue;
		private String toolText;
		private String color;
		
		public Entity(){
			
		}
		
		public Entity(String id, String value, String displayValue, String toolText, String color) {
			super();
			this.id = id;
			this.value = value;
			this.displayValue = displayValue;
			this.toolText = toolText;
			this.color = color;
		}
		/**
		 * @return the id
		 */
		public String getId() {
			return id;
		}
		/**
		 * @param id the id to set
		 */
		public void setId(String id) {
			this.id = id;
		}
		/**
		 * @return the value
		 */
		public String getValue() {
			return value;
		}
		/**
		 * @param value the value to set
		 */
		public void setValue(String value) {
			this.value = value;
		}
		/**
		 * @return the displayValue
		 */
		public String getDisplayValue() {
			return displayValue;
		}
		/**
		 * @param displayValue the displayValue to set
		 */
		public void setDisplayValue(String displayValue) {
			this.displayValue = displayValue;
		}
		/**
		 * @return the toolText
		 */
		public String getToolText() {
			return toolText;
		}
		/**
		 * @param toolText the toolText to set
		 */
		public void setToolText(String toolText) {
			this.toolText = toolText;
		}
		/**
		 * @return the color
		 */
		public String getColor() {
			return color;
		}
		/**
		 * @param color the color to set
		 */
		public void setColor(String color) {
			this.color = color;
		}
		
		
	}

	/**
	 * @return the colors
	 */
	public List<Color> getColors() {
		return colors;
	}

	/**
	 * @param colors the colors to set
	 */
	public void setColors(List<Color> colors) {
		this.colors = colors;
	}

	/**
	 * @return the entities
	 */
	public List<Entity> getEntities() {
		return entities;
	}

	/**
	 * @param entities the entities to set
	 */
	public void setEntities(List<Entity> entities) {
		this.entities = entities;
	}
}

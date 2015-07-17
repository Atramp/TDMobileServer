package com.teradata.bean.chart;

import com.teradata.dao.KpiDao;

import java.util.*;

public class ChartObject {
    public static enum RENDERAS {
        LINE("line"), BAR("bar"), AREA("area");
        private String value;

        private RENDERAS(String value) {
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }
    }

    public static enum YAXIS {
        LEFT(""), RIGHT("S");
        private String value;

        private YAXIS(String value) {
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }
    }

    /**
     * 标题
     */
    private String caption = "";
    /**
     * 副标题
     */
    private String subCaption = "";
    /**
     * X轴名称
     */
    private String xAxisName = "";
    /**
     * Y轴名称
     */
    private String yAxisName = "";

    /**
     * 背景颜色
     */
    private String bgColor = "#FFFFFF";
    /**
     * 是否显示边框，1-显示（2D Charts默认值） 0-隐藏（3D Charts默认值）
     */
    private String showBorder = "0";//

    /**
     * 是否显示值 1-显示（默认） 2-隐藏
     */
    private String showvalues = "1";
    /**
     * 是否显示标签 1-显示（默认） 2-隐藏
     */
    private String showLabels = "1";

    /**
     * X轴值后缀
     */
    private String numberSuffix = "";
    /**
     * Y轴值后缀
     */
    private String sNumberSuffix = "";

    /**
     * 更多配置
     */
    private Map extensions = null;

    /**
     * 类别，X轴上的值
     */
    private List categories = null;
    /**
     * Y轴值
     */
    protected List<Serial> serials = null;

    public void addCategory(String category) {
        if (null == categories)
            categories = new ArrayList();
        categories.add(category);
    }

    public Serial addSerial() {
        if (null == serials)
            serials = new ArrayList();
        Serial serial = new Serial();
        serials.add(serial);
        return serial;
    }

    public Serial addSerial(Serial s) {
        if (null == serials)
            serials = new ArrayList();
        serials.add(s);
        return s;
    }

    /**
     * @param seriesname  名称
     * @param parentYAxis 双Y轴图，对应的Y轴
     * @param renderAs    绘制类型
     * @return
     */
    public Serial addSerial(String seriesname, YAXIS parentYAxis, RENDERAS renderAs) {
        if (null == serials)
            serials = new ArrayList();
        Serial serial = new Serial(seriesname, parentYAxis.getValue(), renderAs.getValue());
        serials.add(serial);
        return serial;
    }

    public void setAttribute(String attrName, String attrValue) {
        if (null == extensions)
            extensions = new HashMap();
        extensions.put(attrName, attrValue);
    }

    public ChartObject() {

    }

    /**
     * @return the caption
     */
    public String getCaption() {
        return caption;
    }

    /**
     * @param caption the caption to set
     */
    public void setCaption(String caption) {
        this.caption = caption;
    }

    /**
     * @return the subCaption
     */
    public String getSubCaption() {
        return subCaption;
    }

    /**
     * @param subCaption the subCaption to set
     */
    public void setSubCaption(String subCaption) {
        this.subCaption = subCaption;
    }

    /**
     * @return the xAxisName
     */
    public String getxAxisName() {
        return xAxisName;
    }

    /**
     * @param xAxisName the xAxisName to set
     */
    public void setxAxisName(String xAxisName) {
        this.xAxisName = xAxisName;
    }

    /**
     * @return the yAxisName
     */
    public String getyAxisName() {
        return yAxisName;
    }

    /**
     * @param yAxisName the yAxisName to set
     */
    public void setyAxisName(String yAxisName) {
        this.yAxisName = yAxisName;
    }

    /**
     * @return the bgColor
     */
    public String getBgColor() {
        return bgColor;
    }

    /**
     * @param bgColor the bgColor to set
     */
    public void setBgColor(String bgColor) {
        this.bgColor = bgColor;
    }

    /**
     * @return the showBorder
     */
    public String getShowBorder() {
        return showBorder;
    }

    /**
     * @param showBorder the showBorder to set
     */
    public void setShowBorder(boolean showBorder) {
        this.showBorder = showBorder ? "1" : "0";
    }

    /**
     * @return the showvalues
     */
    public String getShowvalues() {
        return showvalues;
    }

    /**
     * @param showvalues the showvalues to set
     */
    public void setShowvalues(boolean showvalues) {
        this.showvalues = showvalues ? "1" : "0";
    }

    /**
     * @return the showLabels
     */
    public String getShowLabels() {
        return showLabels;
    }

    /**
     * @param showLabels the showLabels to set
     */
    public void setShowLabels(boolean showLabels) {
        this.showLabels = showLabels ? "1" : "0";
    }

    /**
     * @return the numberSuffix
     */
    public String getNumberSuffix() {
        return numberSuffix;
    }

    /**
     * @param numberSuffix the numberSuffix to set
     */
    public void setNumberSuffix(String numberSuffix) {
        this.numberSuffix = numberSuffix;
    }

    /**
     * @return the sNumberSuffix
     */
    public String getsNumberSuffix() {
        return sNumberSuffix;
    }

    /**
     * @param sNumberSuffix the sNumberSuffix to set
     */
    public void setsNumberSuffix(String sNumberSuffix) {
        this.sNumberSuffix = sNumberSuffix;
    }

    /**
     * @return the categories
     */
    public List getCategories() {
        return categories;
    }

    /**
     * @param categories the categories to set
     */
    public void setCategories(List categories) {
        this.categories = categories;
    }

    /**
     * @return the serials
     */
    public List<Serial> getSerials() {
        return serials;
    }

    /**
     * @param serials the serials to set
     */
    public void setSerials(List<Serial> serials) {
        this.serials = serials;
    }

    /**
     * @return the extensions
     */
    public Map getExtensions() {
        return extensions;
    }

    public class Serial {
        private String serialName = "";
        private String parentYAxis = "";
        private String renderAs = "";
        private List<Data> dataset = null;

        public Serial() {

        }

        public Serial(String serialName, String parentYAxis, String renderAs) {
            super();
            this.serialName = serialName;
            this.parentYAxis = parentYAxis;
            this.renderAs = renderAs;
        }

        /**
         * 添加一个数据项
         *
         * @param data
         */
        public void addData(Data data) {
            if (null == dataset)
                dataset = new ArrayList<Data>();
            dataset.add(data);
        }

        /**
         * 添加一个数据项
         *
         * @param value
         */
        public void addData(String value) {
            if (null == dataset)
                dataset = new ArrayList<Data>();
            dataset.add(new Data("", value, ""));
        }

        /**
         * 添加一个数据项
         *
         * @param label
         * @param value
         */
        public void addData(String label, String value) {
            if (null == dataset)
                dataset = new ArrayList<Data>();
            dataset.add(new Data(label, value, ""));
        }


        /**
         * 添加一个数据项
         *
         * @param label
         * @param value
         * @param tip
         */
        public void addData(String label, String value, String tip) {
            if (null == dataset)
                dataset = new ArrayList<Data>();
            dataset.add(new Data(label, value, tip));
        }

        /**
         * @return the serialName
         */
        public String getSerialName() {
            return serialName;
        }

        /**
         * @param serialName the serialName to set
         */
        public void setSerialName(String serialName) {
            this.serialName = serialName;
        }

        /**
         * @return the parentYAxis
         */
        public String getParentYAxis() {
            return parentYAxis;
        }

        /**
         * @param parentYAxis the parentYAxis to set
         */
        public void setParentYAxis(String parentYAxis) {
            this.parentYAxis = parentYAxis;
        }

        /**
         * @return the renderAs
         */
        public String getRenderAs() {
            return renderAs;
        }

        /**
         * @param renderAs the renderAs to set
         */
        public void setRenderAs(String renderAs) {
            this.renderAs = renderAs;
        }

        public List<Data> getDataset() {
            return this.dataset;
        }

        public class Data {
            private String label = "";
            private String value = "";
            private String tip = "";
            private String color = "";

            public Data(String label, String value, String tip) {
                super();
                this.label = label;
                this.value = value;
                this.tip = tip;
            }

            public Data(String label, String value, String tip, String color) {
                super();
                this.label = label;
                this.value = value;
                this.tip = tip;
                this.setColor(color);
            }

            /**
             * @return the label
             */
            public String getLabel() {
                return label;
            }

            /**
             * @param label the label to set
             */
            public void setLabel(String label) {
                this.label = label;
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
             * @return the tip
             */
            public String getTip() {
                return tip;
            }

            /**
             * @param tip the tip to set
             */
            public void setTip(String tip) {
                this.tip = tip;
            }


            public String getColor() {
                return color;
            }

            public void setColor(String color) {
                this.color = color;
            }
        }
    }

    // 数据库中chart_type配置的缓存
    private static Map allChartType;

    /**
     * 根据kpiNo、kpiBelonging获取chartType配置
     *
     * @param kpiNo
     * @param kpiBelonging
     * @return String[0]为CHART_TYPE_CODE;String[1]为XML_TEMPLATE
     */
    public static String[] getChartType(String kpiNo, String kpiBelonging) {
        if (null == allChartType) {
            synchronized (ChartObject.class) {
                if (null == allChartType) {
                    allChartType = KpiDao.getService().getAllChartType();
                }
            }
        }
        Map item = (Map) allChartType.get(kpiNo.concat("_").concat(kpiBelonging));
        return new String[]{item.get("CHART_TYPE_CODE").toString(), item.get("XML_TEMPLATE").toString()};
    }

    public static enum CHART_TYPE_CODE {
        PIE2D("PIE2D"), BAR2D("BAR2D"), COLUMN2D("COLUMN2D"), AREA("AREA"), LINE("LINE"), SPARKLINE("SparkLine"),
        SCROLLCOMBI2D("ScrollCombi2D");
        private String value;

        private CHART_TYPE_CODE(String value) {
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }

        public boolean equals(String target) {
            return this.getValue().equals(target);
        }
    }

}

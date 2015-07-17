package com.teradata.common;

import java.util.HashMap;
import java.util.Map;

public class Constant {
    /**
     * 地图chart各省名称及编码
     */
    private static class Province4Map {
        private static final Map map = new HashMap();

        static {
            map.put("10100", new String[]{"北京", "CN.BJ"});
            map.put("10200", new String[]{"上海", "CN.SH"});
            map.put("10300", new String[]{"天津", "CN.TJ"});
            map.put("10400", new String[]{"重庆", "CN.CQ"});
            map.put("10500", new String[]{"贵州", "CN.GZ"});
            map.put("10600", new String[]{"湖北", "CN.HU"});
            map.put("10700", new String[]{"陕西", "CN.SA"});
            map.put("10800", new String[]{"河北", "CN.HB"});
            map.put("10900", new String[]{"河南", "CN.HE"});
            map.put("11000", new String[]{"安徽", "CN.AH"});
            map.put("11100", new String[]{"福建", "CN.FJ"});
            map.put("11200", new String[]{"青海", "CN.QH"});
            map.put("11300", new String[]{"甘肃", "CN.GS"});
            map.put("11400", new String[]{"浙江", "CN.ZJ"});
            map.put("11500", new String[]{"海南", "CN.HA"});
            map.put("11600", new String[]{"黑龙江", "CN.HL"});
            map.put("11700", new String[]{"江苏", "CN.JS"});
            map.put("11800", new String[]{"吉林", "CN.JL"});
            map.put("11900", new String[]{"宁夏", "CN.NX"});
            map.put("12000", new String[]{"山东", "CN.SD"});
            map.put("12100", new String[]{"山西", "CN.SX"});
            map.put("12200", new String[]{"新疆", "CN.XJ"});
            map.put("12300", new String[]{"广东", "CN.GD"});
            map.put("12400", new String[]{"辽宁", "CN.LN"});
            map.put("12500", new String[]{"广西", "CN.GX"});
            map.put("12600", new String[]{"湖南", "CN.HN"});
            map.put("12700", new String[]{"江西", "CN.JX"});
            map.put("12800", new String[]{"内蒙古", "CN.NM"});
            map.put("12900", new String[]{"云南", "CN.YN"});
            map.put("13000", new String[]{"四川", "CN.SC"});
            map.put("13100", new String[]{"西藏", "CN.XZ"});
        }
    }

    /**
     * 获取地图chart指定省份的名称和编码
     *
     * @param branchNo
     * @return
     */
    public static String[] province4map(String branchNo) {
        return (String[]) Province4Map.map.get(branchNo);
    }

    public static String chartType(int type) {
        switch (type) {
            default:
                return "";
            case 1:
                return "MSCombiDY2D";
            case 2:
                return "PIE2D";
            case 3:
                return "BAR2D";
            case 4:
                return "COLUMN2D";
        }
    }

    public static String chartTemplate(int type) {
        switch (type) {
            default:
                return "template_simple.ftl";
            case 1:
                return "template_combo.ftl";
            case 2:
                return "template_pie.ftl";
            case 3:
                return "template_simple.ftl";
            case 4:
                return "template_simple.ftl";
            case 5:
                return "template_china_map.ftl";
        }
    }

    /**
     * 百度消息推送参数
     * 终端类型
     */
    public enum DEVICE_TYPE {
        WEB(1), PC(2), ANDROID(3), IOS(4), WP(5);

        private DEVICE_TYPE(int type) {
            this.type = type;
        }

        private int type;

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }

    /**
     * 百度消息推送参数
     * 消息类型
     */
    public enum MESSAGE_TYPE {
        MESSAGE(0), NOTIFACTION(1);

        private MESSAGE_TYPE(int type) {
            this.type = type;
        }

        private int type;

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }
}

package com.teradata.servlet.chart;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.teradata.bean.chart.ChartMapObject;
import com.teradata.bean.chart.ChartMapObject.Entity;
import com.teradata.bean.chart.ChartObject;
import com.teradata.common.Constant;
import com.teradata.common.collection.UpperCaseMap;
import com.teradata.common.utils.CommonUtil;
import com.teradata.dao.KpiValueDao;

/**
 * 指标全国地图
 */
public class KpiMapChartServlet extends AbstractXMLServlet {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String version = request.getParameter("version");
        if (version != null && version.compareTo("1.2") > 0) {
            response.setContentType("text/json");
            response.setCharacterEncoding("utf-8");
            List<Map> requestList = KpiValueDao.getService().getAllProvinceValueByKpi(request.getParameter("kpiid"),
                    request.getParameter("date"), "5", "KPI_VALUE", true);
            if (null != requestList && requestList.size() > 0) {
                List<Map<String, Object>> results = new ArrayList<Map<String, Object>>(requestList.size());
                Map option = new HashMap();
                option.put("UNIT", requestList.get(0).get("UNIT_NAME").toString());
                option.put("KPI_NAME",requestList.get(0).get("KPI_NAME").toString());
                results.add(option);
                for (int i = 0, size = requestList.size(); i < size; i++) {
                    Map map = requestList.get(i);
                    if ("13200".equals(map.get("BRANCH_NO").toString())) {
                        continue;
                    }
                    String[] provinceProperty = Constant.province4map(CommonUtil.getString(map, "BRANCH_NO"));
                    if (provinceProperty == null)
                        continue;
                    Map<String, Object> temp = new HashMap<String, Object>();
                    temp.put("name", provinceProperty[0]);
                    temp.put("value", Double.parseDouble(CommonUtil.numberFormat(CommonUtil.getString(map, "KPI_VALUE"),
                            CommonUtil.getString(map, "UNIT_DIVISOR"),
                            CommonUtil.getString(map, "PRECISIONS_FORMAT"))));
                    results.add(temp);
                }
                response.getWriter().write(new Gson().toJson(results));
            }
        } else {
            request.setAttribute("kpiBelonging", "5");
            super.doGet(request, response);
        }
    }

    private String getMinMaxVaule(List list, int position, String orderby) {
        HashMap map = (HashMap) list.get(position);
        return map.get(orderby).toString();
    }

    @Override
    protected ChartObject processDataWithTemplate(HttpServletRequest request) {
        String kpiBelonging = request.getAttribute("kpiBelonging").toString();

        String topColor = "008200";
        String normColor = "38B0DE";
        String lowColor = "B80000";

        String orderName = "";
        String tableOrder = "";
        int orderBy = Integer.parseInt(request.getParameter("orderby"));
        switch (orderBy) {
            case 1:
                orderName = "同比";
                tableOrder = "TB_KPI_RATE";
                break;
            case 2:
                orderName = "环比";
                tableOrder = "HB_KPI_RATE";
                break;
            case 3:
                orderName = "指标值";
                tableOrder = "KPI_VALUE";
                break;
        }

        List<Map> requestList = KpiValueDao.getService().getAllProvinceValueByKpi(request.getParameter("kpiid"),
                request.getParameter("date"), kpiBelonging, tableOrder, true);
        if (null == requestList || requestList.isEmpty())
            return null;
        for (Map map : requestList) {
            if ("13200".equals(map.get("BRANCH_NO").toString())) {
                requestList.remove(map);
                break;
            }
        }
        ChartMapObject co = new ChartMapObject();
        co.setCaption(requestList.get(0).get("KPI_NAME").toString());
        if (orderBy == 3) {
            co.setSubCaption(orderName.concat("全国情况 单位：").concat(requestList.get(0).get("UNIT_NAME").toString()));
        } else {
            co.setSubCaption(orderName.concat("全国情况"));
        }
        co.addColor(co.new Color(getMinMaxVaule(requestList, 4, tableOrder),
                getMinMaxVaule(requestList, 0, tableOrder), orderName.concat("前五省份"), topColor));
        co.addColor(co.new Color(getMinMaxVaule(requestList, requestList.size() - 1, tableOrder), getMinMaxVaule(
                requestList, requestList.size() - 6, tableOrder), orderName.concat("后五省份"), lowColor));

        for (int i = 0; i < requestList.size(); i++) {
            UpperCaseMap map = (UpperCaseMap) requestList.get(i);
            String[] provinceProperty = Constant.province4map(CommonUtil.getString(map, "BRANCH_NO"));
            if (provinceProperty == null)
                continue;
            Entity entity = co.new Entity();
            entity.setId(provinceProperty[1]);
            entity.setDisplayValue(provinceProperty[0]);
            switch (orderBy) {
                default:
                    break;
                case 1:
                    entity.setValue(map.get("TB_KPI_RATE").toString());
                    entity.setToolText(new StringBuilder().append(provinceProperty[0]).append("{br}").append(orderName)
                            .append("：").append(CommonUtil.getString(map, "TB_KPI_RATE")).toString());
                    break;
                case 2:
                    entity.setValue(map.get("HB_KPI_RATE").toString());
                    entity.setToolText(new StringBuilder().append(provinceProperty[0]).append("{br}").append(orderName)
                            .append("：").append(CommonUtil.getString(map, "HB_KPI_RATE")).toString());
                    break;
                case 3:
                    entity.setValue(map.get("KPI_VALUE").toString());
                    entity.setToolText(new StringBuilder()
                            .append(provinceProperty[0])
                            .append("{br}")
                            .append(orderName)
                            .append("：")
                            .append(CommonUtil.numberFormat(CommonUtil.getString(map, "KPI_VALUE"),
                                    CommonUtil.getString(map, "UNIT_DIVISOR"),
                                    CommonUtil.getString(map, "PRECISIONS_FORMAT"))).toString());
                    break;
            }

            if (i <= 4) {
                entity.setColor(topColor); // 绿色
            } else if (i >= requestList.size() - 5) {
                entity.setColor(lowColor); // 红色
            } else {
                entity.setColor(normColor); // 黄色
            }
            co.addEntity(entity);
        }
        return co;

    }
}

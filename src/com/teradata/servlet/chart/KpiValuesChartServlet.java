package com.teradata.servlet.chart;

import com.teradata.bean.chart.ChartObject;
import com.teradata.bean.chart.ChartObject.Serial;
import com.teradata.common.collection.UpperCaseMap;
import com.teradata.common.utils.CommonUtil;
import com.teradata.common.utils.DateUtil;
import com.teradata.dao.KpiValueDao;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 指标图表
 */
public class KpiValuesChartServlet extends AbstractXMLServlet {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("kpiBelonging", "3");
        super.doGet(request, response);
    }

    /*市场竞争类指标的特殊处理*/
    private ChartObject processData4market(HttpServletRequest request) {
        String kpiIds = "0144".equals(request.getParameter("kpiid")) ? "0144,004B,004C" : "0145,004D,004E";
        String date = request.getParameter("date");
        String kpiBelonging = request.getAttribute("kpiBelonging").toString();// 特殊处理，使用二级页面的单位
        List<Map> list = KpiValueDao.getService().getKpiValueByKpis(kpiIds, date,
                CommonUtil.getValue(request.getParameter("branch"), "10000"),
                kpiBelonging);
        ChartObject co = new ChartObject();
        if (kpiIds.indexOf("0144") > -1) {
            co.setCaption("中国移动新增市场份额（联通、电信）");
        } else if (kpiIds.indexOf("0145") > -1) {
            co.setCaption("中国移动市场份额（联通、电信）");
        }

        co.setShowvalues(true);
        co.setShowLabels(false);
        co.setBgColor("FFFFFF,FFFFFF");
        co.setAttribute("showLegend", "1");
        co.setAttribute("showPercentValues", "1");
        co.setAttribute("showPercentageInLabel", "1");

        String[] colors = new String[]{"3398cc","eeb311","55b949"};

        ChartObject.Serial serial = co.addSerial();
        for (int i = 0; i < list.size(); i++) {
            UpperCaseMap dataMap = (UpperCaseMap) list.get(i);
            serial.addData(serial.new Data(dataMap.getString("KPI_NAME"),dataMap.getString("KPI_VALUE"),"",colors[i]));
        }
        return co;
    }

    protected ChartObject processDataWithTemplate(HttpServletRequest request) {
        String kpiId = request.getParameter("kpiid");
        String kpiBelonging = "2";// 特殊处理，使用二级页面的单位

        // 市场竞争类指标特殊处理
        if ("0144".equals(kpiId) || "0145".equals(kpiId)) {
            return processData4market(request);
        }
        String startDate = request.getParameter("date");
        int date_offset = Integer.parseInt(CommonUtil.getValue(request.getParameter("date_offset"), "-6"));
        String endDate = DateUtil.addMonths(startDate, date_offset);
        List<Map> list = KpiValueDao.getService().getKpiValueTrendByKpi(kpiId, startDate,
                endDate, CommonUtil.getValue(request.getParameter("branch"), "10000"),
                kpiBelonging);
        if (null == list) {
            return null;
        }

        ChartObject co = new ChartObject();
        co.setCaption((String) list.get(0).get("KPI_NAME"));
        co.setSubCaption("单位：" + list.get(0).get("UNIT_NAME"));
        co.setsNumberSuffix("%");
        Serial values = co.addSerial("指标值", ChartObject.YAXIS.LEFT, ChartObject.RENDERAS.BAR);
        Serial hbrates = co.addSerial("环比值", ChartObject.YAXIS.RIGHT, ChartObject.RENDERAS.LINE);
        Serial tbrates = co.addSerial("同比值", ChartObject.YAXIS.RIGHT, ChartObject.RENDERAS.LINE);
        for (int i = 0; i < list.size(); i++) {
            UpperCaseMap item = (UpperCaseMap) list.get(i);
            co.addCategory(item.getString("STATIS_MON").substring(2, 6));
            values.addData(CommonUtil.numberFormat(item.getString("KPI_VALUE"), item.getString("UNIT_DIVISOR"),
                    item.getString("PRECISIONS_FORMAT")));
            hbrates.addData(item.getString("HB_KPI_RATE",""));
            tbrates.addData(item.getString("TB_KPI_RATE",""));
        }
        co.setShowBorder(false);
        co.setShowvalues(false);
        co.setAttribute("lineThickness", "5");
        return co;
    }
}
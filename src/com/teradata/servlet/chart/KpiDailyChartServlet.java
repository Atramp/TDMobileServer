package com.teradata.servlet.chart;

import com.teradata.bean.chart.ChartObject;
import com.teradata.common.collection.UpperCaseMap;
import com.teradata.common.utils.CommonUtil;
import com.teradata.common.utils.DateUtil;
import com.teradata.dao.KpiValueDao;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 日制表图表
 */
public class KpiDailyChartServlet extends AbstractXMLServlet {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("kpiBelonging", "1");
        super.doGet(request, response);
    }

    @Override
    protected ChartObject processDataWithTemplate(HttpServletRequest request) {
        String kpiId = request.getParameter("kpiid");
        String date = request.getParameter("date");
        String kpiBelonging = request.getAttribute("kpiBelonging").toString();
        String chartTypeCode = request.getAttribute("CHART_TYPE_CODE").toString();

        List<Double> helplist = new ArrayList();
        List<Map> requestList = KpiValueDao.getService().getKpiValueById(kpiId, date, DateUtil.addDays(date, -30),
                kpiBelonging);
        if (requestList == null || requestList.size() == 0)
            return null;
        UpperCaseMap item = (UpperCaseMap) requestList.get(0);
        ChartObject co = new ChartObject();
        co.setCaption(item.getString("KPI_NAME").concat(" 单位：").concat(item.getString("UNIT_NAME")));
        co.setShowvalues(false);
//        co.setAttribute("paletteColors", "3398cc");
        co.setBgColor("FFFFFF,FFFFFF");
        co.setAttribute("scrollToEnd", "1");
        co.setAttribute("maxColWidth", "100");
        co.setAttribute("formatNumberScale", "0");
        co.setAttribute("showAlternateHGridColor", "0");
        co.setAttribute("borderAlpha", "0");
        ChartObject.Serial serial = null;
        if (ChartObject.CHART_TYPE_CODE.AREA.equals(chartTypeCode)) {
            serial = co.addSerial("", ChartObject.YAXIS.LEFT, ChartObject.RENDERAS.AREA);
            co.setAttribute("alpha", "60");
        } else if (ChartObject.CHART_TYPE_CODE.LINE.equals(chartTypeCode)) {
            serial = co.addSerial("", ChartObject.YAXIS.LEFT, ChartObject.RENDERAS.LINE);
            co.setAttribute("lineThickness", "2");
        } else {
            serial = co.addSerial();
        }
        request.setAttribute("CHART_TYPE_CODE", ChartObject.CHART_TYPE_CODE.SCROLLCOMBI2D.getValue());
        for (int i = 0; i < requestList.size(); i++) {
            UpperCaseMap map = (UpperCaseMap) requestList.get(i);
            co.addCategory(map.getString("STATIS_DATE").substring(4, 8));
            String kpiValue = CommonUtil.numberFormat(map.getString("KPI_VALUE"),
                    map.getString("UNIT_DIVISOR"), map.getString("PRECISIONS_FORMAT"));
            serial.addData(kpiValue);
            helplist.add(Double.parseDouble(kpiValue));
        }
        co.setAttribute("yAxisMinValue", String.valueOf(Collections.min(helplist) * 0.9));
        return co;
    }
}

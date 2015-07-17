package com.teradata.servlet.chart;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.teradata.bean.chart.ChartObject;
import com.teradata.bean.chart.ChartObject.Serial;
import com.teradata.common.collection.UpperCaseMap;
import com.teradata.common.utils.CommonUtil;
import com.teradata.dao.KpiValueDao;

/**
 * 指标详情图表
 */
public class KpiDetailChartServlet extends AbstractXMLServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("kpiBelonging", "4");
        super.doGet(request, response);
    }

    protected ChartObject processDataWithTemplate(HttpServletRequest request) {
		String chartTypeCode = request.getAttribute("CHART_TYPE_CODE").toString();
        String kpiBelonging = request.getAttribute("kpiBelonging").toString();

		List<Map> dbList = KpiValueDao.getService().getSubsByKpi(request.getParameter("kpiid"), request.getParameter("date"),
				CommonUtil.getValue(request.getParameter("branch"), "10000"),
                kpiBelonging);
		if (null != dbList) {
			ChartObject co = new ChartObject();
			co.setCaption(dbList.get(0).get("CAPTION").toString().concat("（").concat(dbList.get(0).get("UNIT_NAME").toString()).concat("）"));
			Serial serial = co.addSerial();
			for (int i = 0; i < dbList.size(); i++) {
				UpperCaseMap item = (UpperCaseMap) dbList.get(i);
				String value = CommonUtil.numberFormat(item.getString("KPI_VALUE"), item.getString("UNIT_DIVISOR"),
						item.getString("PRECISIONS_FORMAT"));
				serial.addData(item.getString("KPI_NAME"), value, item.getString("KPI_NAME").concat("，").concat(value));
			}
			if(ChartObject.CHART_TYPE_CODE.PIE2D.equals(chartTypeCode)){// 饼图不显示label
				co.setShowLabels(false);
			}
			co.setAttribute("formatNumberScale", "0");
			co.setAttribute("numDivLines", "0");
			co.setAttribute("showPercentValues", "1");
			co.setAttribute("placeValuesInside", "1");
			co.setAttribute("showLegend", "1");
			return co;
		}
		return null;
	}
}

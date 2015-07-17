package com.teradata.servlet.chart;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.teradata.bean.chart.ChartObject;
import com.teradata.bean.chart.ChartObject.Serial;
import com.teradata.common.collection.UpperCaseMap;
import com.teradata.common.utils.CommonUtil;
import com.teradata.common.utils.DateUtil;
import com.teradata.dao.KpiValueDao;

/**
 * 指标趋势
 */
public class KpiTrendChartServlet extends AbstractXMLServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("kpiBelonging", "2");
        super.doGet(request, response);
    }

    protected ChartObject processDataWithTemplate(HttpServletRequest request) {
		String kpiId = request.getParameter("kpiid");
		String startDate = request.getParameter("startDate");
		String branchNo = CommonUtil.getValue(request.getParameter("branch_no"), "10000");
        String kpiBelonging = request.getAttribute("kpiBelonging").toString();

		List result = KpiValueDao.getService().getKpiValueTrendByKpi(kpiId, startDate,
				DateUtil.addMonths(startDate, -6), branchNo, kpiBelonging);
		if (null != result) {
			ChartObject co = new ChartObject();
			Serial serial = co.addSerial();
			for (int i = 0; i < result.size(); i++) {
				UpperCaseMap item = (UpperCaseMap) result.get(i);
				serial.addData(item.getString("KPI_NAME"), item.getString("HB_KPI_RATE"));
			}
			String bgColor = "1".equals(request.getParameter("color")) ? "#4ddf5b" : "#4db2df";
			co.setBgColor(bgColor);
			co.setNumberSuffix("%");
			co.setShowvalues(false);
			co.setShowBorder(false);
			co.setShowLabels(false);

			// 设置一些扩展属性
			co.setAttribute("drawAnchors", "1");
			co.setAttribute("showHighLowValue", "0");
			co.setAttribute("platte", "1");
			co.setAttribute("lineColor", "#FFFFFF");
			co.setAttribute("openColor", "#FFFFFF");
			co.setAttribute("closeColor", "#FFFFFF");
            co.setAttribute("lineThickness", "2");
            co.setAttribute("anchorradius", "2");

			return co;
		}
		return null;
	}
}

package com.teradata.servlet.data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.teradata.common.collection.UpperCaseMap;
import com.teradata.common.utils.CommonUtil;
import com.teradata.dao.KpiValueDao;

public class KpiValuesDailyServlet extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public KpiValuesDailyServlet() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html; charset=utf-8");
		response.setCharacterEncoding("UTF-8");

		String date = request.getParameter("date");
		String kpiSetId = request.getParameter("kpi_set_id");
		String kpiBelonging = CommonUtil.getValue(request.getParameter("kpi_belonging"), "2");
		String branchNo = CommonUtil.getValue(request.getParameter("branch_no"), "10000");

		List list = KpiValueDao.getService().getDailyKpiValueBySet(kpiSetId, date, branchNo, kpiBelonging);
		List result = new ArrayList(list.size());
		for (int i = 0; i < list.size(); i++) {
			UpperCaseMap map = (UpperCaseMap) list.get(i);
			Map temp = new HashMap();
			temp.put("kpi_no", map.getString("KPI_NO"));
			temp.put("kpi_name", map.getString("KPI_NAME"));
			temp.put("unit_name", map.getString("UNIT_NAME"));
			temp.put("kpi_vaule", CommonUtil.numberFormat(map.getString("KPI_VALUE"), map.getString("UNIT_DIVISOR"),
							map.getString("PRECISIONS_FORMAT")));// 当日值
			temp.put("kpi_mon_value", CommonUtil.numberFormat(CommonUtil.getString(map, "KPI_MON_VALUE"),
					CommonUtil.getString(map, "UNIT_DIVISOR"),
					CommonUtil.getString(map, "PRECISIONS_FORMAT")));// 本月累计
			temp.put("kpi_year_value", CommonUtil.numberFormat(CommonUtil.getString(map, "KPI_YEAR_VALUE"),
					CommonUtil.getString(map, "UNIT_DIVISOR"),
					CommonUtil.getString(map, "PRECISIONS_FORMAT")));// 本年累计
			temp.put("kpi_mon_and_lastmon_net",CommonUtil.numberFormat(map.getString("KPI_MON_AND_LASTMON_NET", "0"), "10000", CommonUtil.PRECISIONS.DOT_TOW));// 月累计净增
			temp.put("kpi_year_and_lastyear_net",CommonUtil.numberFormat(map.getString("KPI_YEAR_AND_LASTYEAR_NET", "0"), "10000", CommonUtil.PRECISIONS.DOT_TOW));// 年累计净增
			temp.put("kpi_lastmon_value", map.getString("KPI_LASTMON_VALUE", "0"));
			temp.put("kpi_mon_rate_hb", map.getString("KPI_MON_RATE_HB", "0"));
			temp.put("kpi_lastyear_mon_value", map.getString("KPI_LASTYEAR_MON_VALUE", "0"));
			temp.put("kpi_mon_rate_tb", map.getString("KPI_MON_RATE_TB", "0"));
			temp.put("kpi_lastyear_value", map.getString("KPI_LASTYEAR_VALUE", "0"));
			temp.put("kpi_year_tb", map.getString("KPI_YEAR_TB", "0"));
			temp.put("kpi_mon_then_lastyear_pp", map.getString("KPI_MON_THEN_LASTYEAR_PP", "0"));

			result.add(temp);
		}
		Gson gson = new Gson();
		String strExpen = gson.toJson(result);

		response.getWriter().write(strExpen);
	}

	/**
	 * The doPost method of the servlet. <br>
	 * 
	 * This method is called when a form has its tag value method equals to
	 * post.
	 * 
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	/**
	 * Initialization of the servlet. <br>
	 * 
	 * @throws ServletException
	 *             if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}

}
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
import com.teradata.common.utils.CommonUtil;
import com.teradata.dao.KpiValueDao;

public class KpiValuesByUserServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor of the object.
	 */
	public KpiValuesByUserServlet() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	private String service(String username, String date, String kpiBelonging) {
		List<Map> list = KpiValueDao.getService().getKpiValueByUserCollection(username, date, "10000", kpiBelonging);
		List<Map> result = new ArrayList<Map>(list.size());
		for (Map map : list) {
			Map item = new HashMap();
			item.put("kpiid", map.get("KPI_NO"));
			item.put("kpiname", map.get("KPI_NAME"));
			item.put("kpivaule",
					CommonUtil.numberFormat(CommonUtil.getString(map, "KPI_VALUE"),
							CommonUtil.getString(map, "UNIT_DIVISOR"), CommonUtil.getString(map, "PRECISIONS_FORMAT")));
			item.put("hbrate", map.get("HB_KPI_RATE"));
			item.put("tbrate", map.get("TB_KPI_RATE"));
			item.put("unit_name", map.get("UNIT_NAME"));
			item.put("subs_chart_type", CommonUtil.getString(map, "SUBS_CHART_TYPE", ""));
			result.add(item);
		}

		return new Gson().toJson(result);
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html; charset=utf-8");
		response.setCharacterEncoding("UTF-8");

		String date = request.getParameter("date");
		String username = request.getParameter("username");
		String kpiBelonging = CommonUtil.getValue(request.getParameter("kpi_belonging"), "2");

		response.getWriter().write(service(username, date, kpiBelonging));
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

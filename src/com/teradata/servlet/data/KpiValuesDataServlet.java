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

public class KpiValuesDataServlet extends HttpServlet {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor of the object.
	 */
	public KpiValuesDataServlet() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy();
	}

	/**
	 * 业务方法
	 *
	 * @param kpiids
	 * @param date
	 * @param username
	 * @param kpiBelonging
	 * @return
	 */
	private String service(String kpi_set_id, String date, String username, String branch_no, String kpiBelonging) {
		List list = KpiValueDao.getService().getKpiValueByKpiSet(kpi_set_id, username, date, branch_no, kpiBelonging);
		if (null != list) {
			List resultList = new ArrayList(list.size());
			for (int i = 0; i < list.size(); i++) {
				UpperCaseMap map = (UpperCaseMap) list.get(i);
				Map temp = new HashMap();
				temp.put("kpiid", map.get("KPI_NO"));
				temp.put("kpiname", map.get("KPI_NAME"));
				temp.put("hbrate", map.get("HB_KPI_RATE"));
				temp.put("tbrate", map.get("TB_KPI_RATE"));
				temp.put("isconnection", map.get("ISCOLLECTION"));
				temp.put("unit_name", map.get("UNIT_NAME"));
				temp.put("kpivaule",
						CommonUtil.numberFormat(CommonUtil.getString(map, "KPI_VALUE"),
								CommonUtil.getString(map, "UNIT_DIVISOR"),
								CommonUtil.getString(map, "PRECISIONS_FORMAT")));
				temp.put("subs_chart_type", map.getString("SUBS_CHART_TYPE", ""));
				resultList.add(temp);
			}
			Gson gson = new Gson();
			return gson.toJson(resultList);
		}
		return null;
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html; charset=utf-8");
		response.setCharacterEncoding("UTF-8");

		String date = request.getParameter("date");
		String username = request.getParameter("username");
		String branch_no = CommonUtil.getValue(request.getParameter("branch_no"), "10000");
		String kpiBelonging = CommonUtil.getValue(request.getParameter("kpi_belonging"), "2");
		String kpiids = request.getParameter("kpi_set_id");
		String result = service(kpiids, date, username, branch_no, kpiBelonging);
		response.getWriter().write(result);
	}

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
	}

}

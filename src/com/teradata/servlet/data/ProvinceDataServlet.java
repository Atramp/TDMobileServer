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

public class ProvinceDataServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor of the object.
	 */
	public ProvinceDataServlet() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	private String service(String kpiid, String date, String kpiBelonging) {
		List<Map> list = KpiValueDao.getService().getAllProvinceValueByKpi(kpiid, date, kpiBelonging, "BRANCH_INDEX", false);
		if (null != list && list.size() > 0) {
			List<Map> provinceData = new ArrayList<Map>(list.size());
			for (Map map : list) {
				UpperCaseMap temp = (UpperCaseMap) map;
				Map item = new HashMap();
				
				item.put("kpiid", temp.getString("KPI_NO"));
				item.put("kpiname", temp.getString("KPI_NAME"));
				item.put("kpivalue",
						CommonUtil.numberFormat(CommonUtil.getString(map, "KPI_VALUE"),
								CommonUtil.getString(map, "UNIT_DIVISOR"),
								CommonUtil.getString(map, "PRECISIONS_FORMAT")));
				item.put("hbrate", temp.getString("HB_KPI_RATE"));
				item.put("tbrate", temp.getString("TB_KPI_RATE"));
				item.put("branchid", temp.getString("BRANCH_NO"));
				item.put("brancname", temp.getString("BRANCH_NAME"));
				item.put("unit_name", temp.getString("UNIT_NAME"));
				provinceData.add(item);
			}

			return new Gson().toJson(provinceData);
		}
		return null;
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html; charset=utf-8");
		response.setCharacterEncoding("UTF-8");

		String date = request.getParameter("date");
		String kpiid = request.getParameter("kpiid");
		String kpiBelonging = CommonUtil.getValue(request.getParameter("kpi_belonging"), "2");
		String result = service(kpiid, date, kpiBelonging);
		response.getWriter().write(result);
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

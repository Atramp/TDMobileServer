package com.teradata.servlet.data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.teradata.dao.KpiDao;

public class KpiServlet extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public KpiServlet() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	/**
	 * The doGet method of the servlet. <br>
	 * 
	 * This method is called when a form has its tag value method equals to get.
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
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
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
		response.setContentType("text/html; charset=utf-8");
		response.setCharacterEncoding("UTF-8");
		String kpiId = request.getParameter("kpi_id");
		String kpiSetId = request.getParameter("kpi_set_id");
		Gson gson = new Gson();
		if(null != kpiSetId && !kpiSetId.isEmpty()){
			response.getWriter().write(gson.toJson(KpiDao.getService().getKpisBySet(kpiSetId)));
		}else if(null != kpiId && !kpiId.isEmpty()){
			response.getWriter().write(gson.toJson(KpiDao.getService().getKpiById(kpiId)));
		} else {
			List list = KpiDao.getService().getKpisAllSet("0");
			LinkedHashMap temp = new LinkedHashMap();
			for (int i = 0; i < list.size(); i++) {
				Map item = (Map) list.get(i);
				Map kpiSet = (Map)temp.get((String) item.get("KPI_SET_ID"));
				if(null == kpiSet){
					kpiSet = new HashMap();
					temp.put((String) item.get("KPI_SET_ID"), kpiSet);
					kpiSet.put("kpi_set_id", item.get("KPI_SET_ID"));
					kpiSet.put("kpi_set_name", item.get("KPI_SET_NAME"));
					kpiSet.put("kpi_set_icon", item.get("KPI_SET_ICON"));
					kpiSet.put("kpi_set_color", item.get("KPI_SET_COLOR"));
					kpiSet.put("kpi_set_index", item.get("KPI_SET_INDEX"));
					kpiSet.put("kpis", new ArrayList());
				}
				List kpiList = (List) kpiSet.get("kpis");
				
				Map newItem = new HashMap();
				newItem.put("kpi_no", item.get("KPI_NO"));
				newItem.put("kpi_name", item.get("KPI_NAME"));
				newItem.put("kpi_index", item.get("KPI_INDEX"));
				kpiList.add(newItem);
			}
			List result = new ArrayList(temp.values());
			response.getWriter().write(gson.toJson(result));
		}
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

package com.teradata.servlet.data;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.teradata.common.utils.CommonUtil;
import com.teradata.dao.KpiDao;

public class KpiSetServlet extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public KpiSetServlet() {
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
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html; charset=utf-8");
		response.setCharacterEncoding("UTF-8");
		String kpiSetId = request.getParameter("kpi_set_id");
		String tag = request.getParameter("tag");
		tag = CommonUtil.getValue(tag, "0");
		if(null == kpiSetId || kpiSetId.isEmpty()){
			List result = KpiDao.getService().getAllKpiset(tag);
			if(result != null){
				Gson gson = new Gson();
				response.getWriter().write(gson.toJson(result));
			}
		}else{
			Map result = KpiDao.getService().getKpiSetById(kpiSetId);
			if(result != null){
				Gson gson = new Gson();
				response.getWriter().write(gson.toJson(result));
			}
		}
	}

	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}

}

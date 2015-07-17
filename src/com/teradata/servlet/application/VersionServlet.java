package com.teradata.servlet.application;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.teradata.dao.ApplicationDao;

public class VersionServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor of the object.
	 */
	public VersionServlet() {
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
		String clientVersion = request.getParameter("version"); // 客户端版本
		String clientType = request.getParameter("client"); // 客户端版本
		if (null != clientType) {
			Map<String, Map> versions = ApplicationDao.getService().getAllVersion();
			Map<String, String> version = versions.get(clientType);
			// 系统最新版本号大于客户端版本号
			if (version.get("VERSION").toString().compareTo(clientVersion) > 0) {
				String updateType = version.get("UPDATE_TYPE");
				String maxMandVersion = version.get("MAX_MAND_VERSION");
				if ("0".equals(updateType) && null != maxMandVersion && maxMandVersion.compareTo(clientVersion) > 0)
					updateType = "1";
				Map map = new HashMap();
				map.put("UPDATE_TYPE", updateType);
				map.put("UPDATE_URL", version.get("UPDATE_URL"));
				map.put("CHANGE_LOG", version.get("CHANGE_LOG"));
				response.getWriter().write(new Gson().toJson(map));
			} else {
				Map map = new HashMap();
				map.put("UPDATE_TYPE", "-1");
				map.put("UPDATE_URL", "");
				map.put("CHANGE_LOG", "");
				response.getWriter().write(new Gson().toJson(map));
			}
		}
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
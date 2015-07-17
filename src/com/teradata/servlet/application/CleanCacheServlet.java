package com.teradata.servlet.application;

import com.teradata.common.cache.CacheUtil;
import com.teradata.common.config.Configuration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CleanCacheServlet extends HttpServlet {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructor of the object.
     */
    public CleanCacheServlet() {
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
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        String path = request.getContextPath();
        String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
        String cacheStr = request.getParameter("cacheStr");
        if (cacheStr == null || cacheStr.isEmpty()) {
            CacheUtil.getCacheManager().clearAll();
        } else if ("application.properties".equals(cacheStr)) {// 用于重新加载application.properties
            Configuration.load();
            response.getWriter().write("application.properties加载成功！");
            return;
        } else {
            String[] caches = cacheStr.split("~");
            for (String cache : caches) {
                String[] temp = cache.split("@");
                CacheUtil.getCache(temp[1]).remove(java.net.URLDecoder.decode(temp[0], "utf-8").toString());

            }
        }
        response.sendRedirect("/TDMobileServer/jsp/ehcache.jsp");
    }

    /**
     * The doPost method of the servlet. <br>
     * <p/>
     * This method is called when a form has its tag value method equals to
     * post.
     *
     * @param request  the request send by the client to the server
     * @param response the response send by the server to the client
     * @throws javax.servlet.ServletException if an error occurred
     * @throws java.io.IOException            if an error occurred
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    /**
     * Initialization of the servlet. <br>
     *
     * @throws javax.servlet.ServletException if an error occurs
     */
    public void init() throws ServletException {
        // Put your code here
    }

}
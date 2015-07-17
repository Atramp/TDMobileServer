package com.teradata.servlet.data;

import com.teradata.common.cache.CacheUtil;
import com.teradata.dao.KpiValueDao;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class KpiMaxMonthServlet extends HttpServlet {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructor of the object.
     */
    public KpiMaxMonthServlet() {
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
        String month = request.getParameter("month");
        String curMonth = KpiValueDao.getService().getKpiValueMonthlyMax();
        if (month != null && !month.isEmpty()) {
            if (curMonth == null || month.compareTo(curMonth) > 0) {
                int i = KpiValueDao.getService().insertMaxMonth(month, "1", null);
                if (i == 1) {
                    CacheUtil.clearSingleElement(CacheUtil.CACHES.KPI, "MAX_MONTH");
                    curMonth = month;
                }
            }
        }
        response.getWriter().write(curMonth);
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
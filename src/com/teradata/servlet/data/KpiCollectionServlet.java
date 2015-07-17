package com.teradata.servlet.data;

import com.teradata.common.cache.CacheUtil;
import com.teradata.common.utils.CommonUtil;
import com.teradata.dao.KpiDao;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class KpiCollectionServlet extends HttpServlet {

    /**
     * Constructor of the object.
     */
    public KpiCollectionServlet() {
        super();
    }

    /**
     * Destruction of the servlet. <br>
     */
    public void destroy() {
        super.destroy(); // Just puts "destroy" string in log
        // Put your code here
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html; charset=utf-8");
        response.setCharacterEncoding("UTF-8");

        String kpiSetId = request.getParameter("kpi_set_id");
        String username = request.getParameter("username");
        String kpiids = request.getParameter("kpiids");
        String operation = request.getParameter("operation"); //1插入，2删除
        boolean result = false;
        response.getWriter().write(kpiids + "-" + operation);
        try {
            if (operation.equals("1")) {// 增加一条收藏
                result = KpiDao.getService().addUserCollection(username, kpiids);
            } else if (operation.equals("2")) {// 删除一条收藏
                result = KpiDao.getService().delUserCollection(username, kpiids);
            } else if (operation.equals("3")) {// 删除用户所有收藏，之后添加多条
                result = KpiDao.getService().updateUserCollections(username, kpiids.split(","));
                kpiSetId = "*";// 清理所有cache
            }
            if (result) { //成功
                resetCache(kpiSetId, username);
                response.getWriter().write("1");
            } else { //失败
                response.getWriter().write("0");

            }
        }catch(Exception e){
            response.getWriter().write(e.getMessage());
        }
    }

    /**
     * The doPost method of the servlet. <br>
     * <p/>
     * This method is called when a form has its tag value method equals to
     * post.
     *
     * @param request  the request send by the client to the server
     * @param response the response send by the server to the client
     * @throws ServletException if an error occurred
     * @throws IOException      if an error occurred
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    /**
     * Initialization of the servlet. <br>
     *
     * @throws ServletException if an error occurs
     */
    public void init() throws ServletException {
        // Put your code here
    }

    private void resetCache(String kpiSetId, String userName) {
        List<String> keys = CacheUtil.getCache(CacheUtil.CACHES.SERVLET.getValue()).getKeys();
        for (String key : keys) {
            if (CommonUtil.stringMatches(key, "KpiValuesDataServlet.*" + kpiSetId + ".*" + userName)) {
                CacheUtil.getCache(CacheUtil.CACHES.SERVLET.getValue()).remove(key);
            }
        }
    }
}
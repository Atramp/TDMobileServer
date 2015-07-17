package com.teradata.servlet.data;

import com.google.gson.Gson;
import com.teradata.bean.comment.Comment;
import com.teradata.common.utils.CommonUtil;
import com.teradata.dao.CommentDao;
import com.teradata.dao.KpiDao;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.*;

public class CommentServlet extends HttpServlet {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructor of the object.
     */
    public CommentServlet() {
        super();
    }

    /**
     * Destruction of the servlet. <br>
     */
    public void destroy() {
        super.destroy();
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/json;charset=utf-8");
        String method = request.getParameter("method");
        Gson gson = new Gson();
        if (null == method || "count".equals(method)) {
            List result = CommentDao.getService().queryUnReadCommentCountBySet(request.getParameter("username"), request.getParameter("statis_date"));
            Map<String, Map> map = new HashMap();
            for (Map item : (List<Map>) result) {
                String kpiSetId = item.get("KPI_SET_ID").toString();
                Map<String, Integer> sub = map.get(kpiSetId);
                if (sub == null) {
                    sub = new HashMap();
                    sub.put("total", 0);
                    map.put(kpiSetId, sub);
                }
                int numByKpi = Integer.parseInt(item.get("NUM").toString());
                sub.put("total", sub.get("total") + numByKpi);
                sub.put(item.get("KPI_NO").toString(), numByKpi);
            }
            response.getWriter().write(gson.toJson(map));
        } else if ("list".equals(method)) {
            List result = CommentDao.getService().queryCommentsByKpi(request.getParameter("kpi_no"), request.getParameter("statis_date"));
            // 记录当前用户已读最新评论
            if (result.size() > 0)
                CommentDao.getService().insertUserLastRead(request.getParameter("username"), ((Map) result.get(0)).get("comment_id").toString());
            response.getWriter().write(gson.toJson(result));
        } else if ("comment".equals(method)) {
            Comment comment = Comment.newInstance();
            comment.setAuthor(request.getParameter("username"));
            comment.setContent(request.getParameter("content"));

            int result = CommentDao.getService().insertCommentToKpi(request.getParameter("kpi_no"), request.getParameter("statis_date"), comment);
            response.getWriter().write(gson.toJson(result > 0));
        } else if ("reply".equals(method)) {
            Map _comment = CommentDao.getService().queryCommentById(request.getParameter("post_to"));
            if (!_comment.isEmpty()) {
                Comment comment = Comment.newInstance();
                comment.setAuthor(request.getParameter("username"));
                comment.setContent(request.getParameter("content"));
                comment.setPost_to(request.getParameter("post_to"));

                String kpi_no = CommonUtil.getString(_comment, "KPI_NO");
                String statis_date = CommonUtil.getString(_comment, "STATIS_DATE");

                Map kpi = KpiDao.getService().getKpiById(kpi_no);

                int result = CommentDao.getService().insertCommentToKpi(kpi_no, statis_date, comment);
                sendNotification(request.getParameter("username"), _comment.get("COMMENT_AUTHOR").toString(), kpi_no, kpi.get("kpi_name").toString(), statis_date);

                response.getWriter().write(gson.toJson(result > 0));
            }
        } else if ("delete".equals(method)) {
            int result = CommentDao.getService().updateCommentStatus(request.getParameter("comment_id"), Comment.STATUS.DELETED.getStatus());
            response.getWriter().write(gson.toJson(result > 0));
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    /**
     * Initialization of the servlet. <br>
     *
     * @throws javax.servlet.ServletException if an error occurs
     */
    public void init() throws ServletException {
    }

    /**
     * 推送消息
     * 回复时推送消息给被回复人
     *
     * @param from
     * @param to
     */
    private void sendNotification(String from, String to, String kpi_no, String kpi_name, String statis_date) {
        this.getServletContext().getServerInfo();
        // 回复时推送消息给被回复人
        HttpPost httpPost = new HttpPost("http://218.206.191.164:8089/pushserver/service/push/user/reply-notification");
        //HttpPost httpPost = new HttpPost("http://localhost:8080/pushserver/service/push/user/reply-notification");
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("username", to));
        nvps.add(new BasicNameValuePair("description", from.concat("回复了您，点击查看详情。")));
        nvps.add(new BasicNameValuePair("kpi_no", kpi_no));
        nvps.add(new BasicNameValuePair("kpi_name", kpi_name));
        nvps.add(new BasicNameValuePair("statis_date", statis_date));


        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nvps, Charset.forName("UTF-8")));
            HttpClients.createDefault().execute(httpPost);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

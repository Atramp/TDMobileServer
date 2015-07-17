package com.teradata.servlet.application;

import com.teradata.bean.User.SMSSession;
import com.teradata.common.SMSSessionManager;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;


public class SMSServlet extends HttpServlet {
    /**
     * Constructor of the object.
     */
    public SMSServlet() {
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
        String statusCode = "";
        String mobile = request.getParameter("username");
        String code = request.getParameter("code");

        SMSSession SMSSession = SMSSessionManager.getSession(mobile);
        if (SMSSession == null) {
            statusCode = "-1";  //session不存在返回-1
        } else {
            if (SMSSession.getExpireTime().getTime() < new Date().getTime()) {
                SMSSessionManager.removeSession(mobile, SMSSession); //删除掉这个验证信息！
                statusCode = "-2";  //session过期返回-2
            } else {
                if (SMSSession.getCode().equals(code)) {
                    SMSSessionManager.removeSession(mobile, SMSSession); //删除掉这个验证信息！
                    statusCode = "1";  //验证码正确返回1
                } else {
                    statusCode = "0";  //验证码错误返回0
                }
            }
        }
        response.getWriter().write(statusCode);
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
    }

}

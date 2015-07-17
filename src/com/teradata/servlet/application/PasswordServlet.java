package com.teradata.servlet.application;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.teradata.bean.User.SMSSession;
import com.teradata.bean.User.User;
import com.teradata.common.SMSSessionManager;
import com.teradata.dao.UserDao;
import com.teradata.service.SMSSendService;


public class PasswordServlet extends HttpServlet {

    /**
     * Constructor of the object.
     */
    public PasswordServlet() {
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
        String username = request.getParameter("username");
        String code = request.getParameter("code");
        String password = request.getParameter("password");
        String oldPassword = request.getParameter("oldpassword");
        String operation = request.getParameter("operation");//操作类型

        int resultCode = -1;
        //1 获取注册码
        if (operation.equals("1")) {
            //验证这个用户是否存在 1=有这个用户并发短信，0=没有这个用户不发送短信，-1=异常
            User user = UserDao.getService().selectUserById(username);
            String verificationCode = SMSSendService.sendVerificationCode(username);
            SMSSession smsSession = new SMSSession(username, verificationCode);
            SMSSessionManager.addSession(username, smsSession);
            resultCode = user == null ? 0 : 1;
        }
        //2 判断验证信息
        else if (operation.equals("2")) {
            SMSSession SMSSession = SMSSessionManager.getSession(username);
            if (SMSSession != null) {
                if (SMSSession.getExpireTime().getTime() < new Date().getTime()) {
                    SMSSessionManager.removeSession(username); //删除掉这个验证信息！
                    resultCode = -2;  //session过期返回-2
                } else {
                    if (SMSSession.getCode().equals(code)) {
                        SMSSessionManager.removeSession(username); //删除掉这个验证信息！
                        resultCode = 1;  //验证码正确返回1
                    } else {
                        resultCode = 0;  //验证码错误返回0
                    }
                }
            }
        }//忘记操作-更新密码操作
        else if (operation.equals("3")) {
            resultCode = UserDao.getService().updatePassword(username, password) ? 1 : 0;
        }
        //忘记操作-更新密码操作
        else if (operation.equals("4")) {
            User user = UserDao.getService().selectUserByIdPWD(username, oldPassword);
            if (user != null) {
                resultCode = UserDao.getService().updatePassword(username, password) ? 1 : 0;
            }
        }
        response.getWriter().write(String.valueOf(resultCode));
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

}
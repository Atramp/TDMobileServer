package com.teradata.servlet.application;

import com.teradata.bean.User.SMSSession;
import com.teradata.bean.User.User;
import com.teradata.common.SMSSessionManager;
import com.teradata.common.utils.CommonUtil;
import com.teradata.dao.ApplicationDao;
import com.teradata.dao.UserDao;
import com.teradata.service.SMSSendService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class UserServlet extends HttpServlet {

    /**
     * Constructor of the object.
     */
    public UserServlet() {
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
        String password = request.getParameter("password");
        String terminalID = request.getParameter("terminalid");
        String resultCode = "1";// 默认值为成功
        try {
            User user = UserDao.getService().selectUserByIdPWD(username, password);
            // 验证用户名密码正确后发送验证码
            if (user != null) {
                insertLoginLog(username, terminalID == null ? "" : terminalID);
                if (user.getPasswd().equals(user.getDefault_passwd())) {
                    resultCode = "2";
                } else {
                    // 记录登录日志
                    SMSSendService smsService = new SMSSendService();
                    String verificationCode = smsService.sendVerificationCode(username);

                    Calendar calendar = Calendar.getInstance();
                    calendar.add(Calendar.MILLISECOND, 120000);
                    SMSSession smsSession = new SMSSession(username, verificationCode);
                    smsSession.setCode(verificationCode);
                    smsSession.setUsername(username);
                    smsSession.setExpireTime(calendar.getTime());
                    SMSSessionManager.addSession(username, smsSession);
                }
            } else {
                resultCode = "0";
            }
        } catch (Exception e) {
            e.printStackTrace();
            resultCode = "-1";
        }
        response.getWriter().write(resultCode);
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

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    /**
     * Initialization of the servlet. <br>
     *
     * @throws javax.servlet.ServletException if an error occurs
     */
    public void init() throws ServletException {
    }

    private void insertLoginLog(String username, String terminalid) {
        Map log = new HashMap();
        log.put("PAGE_ID", "LOGIN");
        log.put("USER_ID", username);
        log.put("DATA_ID", terminalid);
        log.put("ACCESS_START_TIME", CommonUtil.Date.getStandardDate());
        log.put("ACCESS_END_TIME", CommonUtil.Date.getStandardDate());
        log.put("LGTD", "");
        log.put("LAT", "");
        log.put("USER_OPR_TYP", "");
        log.put("PAGE_TYP", "");
        log.put("CLIENT_TERMINAL", "");
        log.put("BAK1", "test");
        log.put("BAK2", "");
        ApplicationDao.getService().insertAppLog(log);
    }
}

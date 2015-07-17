package com.teradata.dao;

import com.teradata.bean.User.User;
import org.apache.ibatis.session.SqlSession;

import java.util.List;
import java.util.Map;

/**
 * 系统相关数据库操作
 *
 * @author liuyeteng
 * @date 2014/7/11
 */
public class UserDao extends AbstractCommonDao {
    private static UserDao service;

    private UserDao() {
        super();
    }

    public static UserDao getService() {
        if (service == null) {
            synchronized (UserDao.class) {
                if (service == null)
                    service = new UserDao();
            }
        }
        return service;
    }

    public User selectUserById(String username) {
        DBParam param = DBParam.createParam();
        param.put("USER_NAME", username);
        try (SqlSession session = getSession()) {
            User user = session.selectOne("APP.selectUserById", param);
            return user;
        }
    }

    public User selectUserByIdPWD(String username, String password) {
        DBParam param = DBParam.createParam();
        param.put("USER_NAME", username);
        param.put("PASSWORD", password);
        try (SqlSession session = getSession()) {
            User user = session.selectOne("APP.selectUserByIdPassword", param);
            return user;
        }
    }

    public boolean updatePassword(String username, String newPassword) {
        DBParam param = DBParam.createParam();
        param.put("USER_NAME", username);
        param.put("PASSWORD", newPassword);
        try (SqlSession session = getSession()) {
            return session.update("APP.updateUserPassword", param) == 1;
        }
    }

}
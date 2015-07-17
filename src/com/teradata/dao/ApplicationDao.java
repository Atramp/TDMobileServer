package com.teradata.dao;

import org.apache.ibatis.session.SqlSession;

import java.util.List;
import java.util.Map;

/**
 * 系统相关数据库操作
 *
 * @author liuyeteng
 * @date 2014/7/11
 */
public class ApplicationDao extends AbstractCommonDao {
    private static ApplicationDao service;

    private ApplicationDao() {
        super();
    }

    public static ApplicationDao getService() {
        if (service == null) {
            synchronized (ApplicationDao.class) {
                if (service == null)
                    service = new ApplicationDao();
            }
        }
        return service;
    }

    public Map<String, Map> getAllVersion() {
        Map<String, Map> result = (Map<String, Map>) getFromCache("VERSION");
        if (null == result || result.size() == 0) {
            synchronized (service) {
                if ((result = (Map<String, Map>) getFromCache("VERSION")) == null) {
                    try (SqlSession session = getSession()) {
                        result = session.selectMap("APP.selectAllVersion", DBParam.createParam(), "CLIENT_TYPE");
                        if (null != result && result.size() > 0)
                            putIntoCache("VERSION", result);
                    }
                }
            }
        }
        return result;
    }

    public int insertAppLog(Map log) {
        if (null != log && !log.isEmpty()) {
            try (SqlSession session = getSession()) {
                return session.insert("LOG.insertLog", DBParam.createParam(log));
            }
        }
        return 0;
    }

    public int insertAppLog(List<Map> logs) {
        int result = 0;
        if (null != logs && !logs.isEmpty()) {
            try (SqlSession session = getSession()) {
                for (Map log : logs) {
                    if (null != logs && !logs.isEmpty())
                        result += session.insert("LOG.insertLog", DBParam.createParam(log));
                }
            }
        }
        return result;
    }
}
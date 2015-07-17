package com.teradata.dao;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.teradata.common.cache.CacheUtil;
import net.sf.ehcache.Element;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;


/**
 * @author Liuyueteng
 * @date 2014/07/02
 */
public abstract class AbstractCommonDao {
    protected static SqlSessionFactory factory = null;
    private Logger logger = Logger.getLogger(AbstractCommonDao.class);

    static {
        Properties properties = new Properties();
        try {
            properties.load(AbstractCommonDao.class.getResourceAsStream("/config/log4j.properties"));
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        PropertyConfigurator.configure(properties);
        try {
            InputStream inputStream = AbstractCommonDao.class.getResourceAsStream("/config/mybatis/mybatis.xml");
            factory = new SqlSessionFactoryBuilder().build(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected SqlSession getSession() {
        return factory.openSession(true);
    }

    protected SqlSession getSession(boolean autoCommit) {
        return factory.openSession(autoCommit);
    }

    /**
     * 必填参数检查
     *
     * @param param
     * @return
     */
    protected boolean paramCheck(String... param) {
        for (int i = 0; i < param.length; i++) {
            if (param[i] == null || param[i].length() == 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * 通用参数类，初始化时指定dbName
     *
     * @author Legolas
     */
    protected static class DBParam extends HashMap {
        private DBParam() {
        }

        public static DBParam createParam() {
            DBParam param = new DBParam();
            param.put("dbName", "dwttemp");
            return param;
        }

        public static DBParam createParam(Map params) {
            DBParam param = createParam();
            param.putAll(params);
            return param;
        }

        public DBParam put(Object key, Object value) {
            super.put(key, value);
            return this;
        }
    }

    protected Object getFromCache(String name) {
        Element e = CacheUtil.getCache().get(name);
        if (e == null)
            return null;
        return e.getValue();
    }

    protected void putIntoCache(String name, Object value) {
        CacheUtil.getCache().put(new Element(name, value));
    }

    protected String arrayJoin(String[] array) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String temp : array) {
            stringBuilder.append("'").append(temp).append("'").append(",");
        }
        return stringBuilder.substring(0, stringBuilder.length() - 1);
    }
}

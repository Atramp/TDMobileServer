package com.teradata.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.teradata.common.collection.UpperCaseMap;
import com.teradata.common.utils.DateUtil;
import org.apache.ibatis.session.SqlSession;

/**
 * KPI_SET相关数据库类
 *
 * @author liuyeteng
 * @date 2014/7/11
 */
public class KpiDao extends AbstractCommonDao {
    private static KpiDao service;

    private KpiDao() {
    }

    public static KpiDao getService() {
        if (service == null) {
            synchronized (KpiDao.class) {
                if (service == null)
                    service = new KpiDao();
            }
        }
        return service;
    }

    /**
     * 获取全部kpiset
     */
    public List<Map> getAllKpiset(String tag) {
        if (!paramCheck(tag))
            return null;
        String cacheKey = "KPI.selectAllKpiset_".concat(tag);
        List<Map> list = (List<Map>) getFromCache(cacheKey);
        if (list == null) {
            SqlSession session = getSession();
            list = session.selectList("KPI.selectAllKpiset", DBParam.createParam().put("TAG", tag));
            session.close();
            putIntoCache(cacheKey, list);
        }
        return list;
    }

    /**
     * 获取指定kpiset 构建了一个Map作为二级cache存放每一个kpiset
     */
    public Map getKpiSetById(String kpiSetId) {
        if (!paramCheck(kpiSetId)) {
            return null;
        }
        Map kpiSet = null;
        ConcurrentHashMap<String, Map> map = (ConcurrentHashMap<String, Map>) getFromCache("KPI.selectKpisetById");
        if (map == null)
            putIntoCache("KPI.selectKpisetById", map = new ConcurrentHashMap<String, Map>());
        else
            kpiSet = map.get(kpiSetId);
        if (kpiSet == null) {
            SqlSession session = getSession();
            kpiSet = session.selectOne("KPI.selectKpisetById", DBParam.createParam().put("KPI_SET_ID", kpiSetId));
            session.close();
            map.put(kpiSetId, kpiSet);
        }
        return kpiSet;
    }

    /**
     * 获取全部kpiSet
     */
    public List<Map> getKpisAllSet(String tag) {
        List<Map> list = (List<Map>) getFromCache("KPI.selectKpisAllSet");
        if (null == list) {
            SqlSession session = getSession();
            list = session.selectList("KPI.selectKpisAllSet", DBParam.createParam().put("TAG", tag));
            session.close();
            putIntoCache("KPI.selectKpisAllSet", list);
        }
        return list;
    }

    /**
     * 获取set的所有kpi
     */
    public List<Map> getKpisBySet(String kpiSetId) {
        if (!paramCheck(kpiSetId)) {
            return null;
        }
        List<Map> list = null;
        Map<String, List<Map>> map = (Map<String, List<Map>>) getFromCache("KPI.selectKpisBySet");
        if (map == null)
            putIntoCache("KPI.getKpisBySet", map = new ConcurrentHashMap<String, List<Map>>());
        else
            list = map.get("kpiSetId");
        if (list == null) {
            SqlSession session = getSession();
            list = session.selectList("KPI.selectKpisBySet", DBParam.createParam().put("KPI_SET_ID", kpiSetId));
            session.close();
            map.put(kpiSetId, list);
        }
        return list;
    }

    /**
     * 根据KPI_ID获取KPI信息
     */
    public Map getKpiById(String kpiId) {
        if (!paramCheck(kpiId)) {
            return null;
        }
        Map kpi = null;
        Map<String, Map> map = (Map<String, Map>) getFromCache("KPI.selectKpiById");
        if (map == null)
            putIntoCache("KPI.selectKpiById", map = new ConcurrentHashMap<String, Map>());
        kpi = map.get(kpiId);
        if (kpi == null) {
            SqlSession session = getSession();
            kpi = session.selectOne("KPI.selectKpiById", DBParam.createParam().put("KPI_NO", kpiId));
            session.close();
            map.put(kpiId, kpi);
        }
        return kpi;
    }

    /**
     * 获取所有chartType配置
     *
     * @return
     */
    public Map getAllChartType() {
        Map<String, Map> allChartType = (Map<String, Map>) getFromCache("KPI.allChartType");

        putIntoCache("KPI.allChartType", allChartType = new HashMap<String, Map>());
        SqlSession session = getSession();
        List<Map> list = session.selectList("KPI.selectAllChartType", DBParam.createParam());
        session.close();
        for (Map temp : list) {
            UpperCaseMap map = (UpperCaseMap) temp;
            String key = map.getString("KPI_NO").concat("_").concat(map.getString("KPI_BELONGING"));
            allChartType.put(key, map);
        }

        return allChartType;
    }

    public boolean addUserCollection(String userName, String kpiID) {
        try (SqlSession session = getSession()) {
            DBParam param = DBParam.createParam();
            param.put("USER_ID", userName);
            param.put("KPI_NO", kpiID);
            param.put("COLLECTION_TIME", DateUtil.getTime());
            return session.insert("KPI.insertUserCollection", param) == 1;
        }
    }

    public boolean updateUserCollections(String userName, String[] kpiIDArray) {
        SqlSession session = getSession(false);
        try {
            if (session.delete("KPI.delUserAllCollection", DBParam.createParam().put("USER_ID", userName)) > 0) {
                DBParam param = DBParam.createParam();
                param.put("USER_ID", userName);
                param.put("COLLECTION_TIME", DateUtil.getTime());
                for (String kpiID : kpiIDArray) {
                    param.put("KPI_NO", kpiID);
                    session.insert("KPI.insertUserCollection", param);
                }
            }
            session.commit();
            return true;
        } catch (Exception e) {
            session.rollback();
            return false;
        } finally {
            session.close();
        }
    }

    public boolean delUserCollection(String userName, String kpiID) {
        try (SqlSession session = getSession()) {
            DBParam param = DBParam.createParam();
            param.put("USER_ID", userName);
            param.put("KPI_NO", kpiID);
            return session.insert("KPI.delUserCollection", param) == 1;
        }
    }
}

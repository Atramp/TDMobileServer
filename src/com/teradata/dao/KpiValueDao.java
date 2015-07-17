package com.teradata.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;


/**
 * KPI_SET相关数据库类
 *
 * @author liuyeteng
 * @date 2014/7/11
 */
public class KpiValueDao extends AbstractCommonDao {
    private static KpiValueDao service;

    private KpiValueDao() {
    }

    public static KpiValueDao getService() {
        if (service == null) {
            synchronized (KpiValueDao.class) {
                if (service == null)
                    service = new KpiValueDao();
            }
        }
        return service;
    }

    public void init4test() {
        factory.openSession();
    }

    public List<Map> getAllProvinceValueByKpi(String kpiNo, String statis_mon, String kpiBelonging) {
        return getAllProvinceValueByKpi(kpiNo, statis_mon, kpiBelonging, null, false);
    }

    public List<Map> getAllProvinceValueByKpi(String kpiNo, String statis_mon, String kpiBelonging, String orderBy,
                                              boolean isDesc) {
        if (!paramCheck(kpiNo, statis_mon, kpiBelonging)) {
            return null;
        }
        SqlSession session = getSession();
        DBParam params = DBParam.createParam();
        params.put("KPI_NO", kpiNo);
        params.put("STATIS_MON", statis_mon);
        params.put("ORDERBY", orderBy == null || orderBy.length() == 0 ? null : orderBy);
        params.put("KPI_BELONGING", kpiBelonging);
        params.put("ISDESC", isDesc);
        List<Map> result = session.selectList("KPI_VALUE.getAllProvinceValueByKpi", params);
        session.close();
        return result;
    }

    public List<Map> getDailyKpiValueBySet(String kpiSetId, String date, String branchNo, String kpiBelonging) {
        if (!paramCheck(kpiSetId, date, branchNo, kpiBelonging)) {
            return null;
        }
        SqlSession session = getSession();
        DBParam params = DBParam.createParam();
        params.put("KPI_SET_ID", kpiSetId);
        params.put("STATIS_DATE", date);
        params.put("KPI_BELONGING", kpiBelonging);
        params.put("BRANCH_NO", branchNo);
        List<Map> result = session.selectList("KPI_VALUE.getDailyKpiValueBySet", params);
        session.close();
        return result;
    }

    public List<Map> getKpiValueTrendByKpi(String kpiNo, String startDate, String endDate, String branchNo,
                                           String kpiBelonging) {
        if (!paramCheck(kpiNo, startDate, endDate, kpiBelonging)) {
            return null;
        }
        SqlSession session = getSession();
        DBParam params = DBParam.createParam();
        params.put("KPI_NO", kpiNo);
        params.put("BRANCH_NO", branchNo);
        params.put("KPI_BELONGING", kpiBelonging);
        params.put("START_MON", startDate);
        params.put("END_MON", endDate);

        List<Map> result = session.selectList("KPI_VALUE.getKpiValueTrendByKpi", params);
        session.close();
        return result;
    }

    public List<Map> getKpiValueByKpis(String kpiIds, String date, String branchNo,
                                       String kpiBelonging) {
        if (!paramCheck(kpiIds, date, kpiBelonging)) {
            return null;
        }
        String ids[] = kpiIds.split(",");

        SqlSession session = getSession();
        DBParam params = DBParam.createParam();
        params.put("IDS", ids);
        params.put("BRANCH_NO", branchNo);
        params.put("KPI_BELONGING", kpiBelonging);
        params.put("STATIS_MON", date);

        List<Map> result = session.selectList("KPI_VALUE.getKpiValueByKpis", params);
        session.close();
        return result;
    }

    public List<Map> getKpiValueByKpiSet(String kpiNo, String userName, String date, String branchNo,
                                         String kpiBelonging) {
        if (!paramCheck(kpiNo, userName, date, kpiBelonging)) {
            return null;
        }
        SqlSession session = getSession();
        DBParam params = DBParam.createParam();
        params.put("KPI_SET_ID", kpiNo);
        params.put("BRANCH_NO", branchNo);
        params.put("USER_ID", userName);
        params.put("STATIS_MON", date);
        params.put("KPI_BELONGING", kpiBelonging);

        List<Map> result = session.selectList("KPI_VALUE.getKpiValueByKpiSet", params);
        session.close();
        return result;
    }

    public List<Map> getKpiValueByUserCollection(String userName, String date, String branchNo, String kpiBelonging) {
        if (!paramCheck(userName, date, kpiBelonging)) {
            return null;
        }
        SqlSession session = getSession();
        DBParam params = DBParam.createParam();
        params.put("BRANCH_NO", branchNo);
        params.put("USER_ID", userName);
        params.put("STATIS_MON", date);
        params.put("KPI_BELONGING", kpiBelonging);

        List<Map> result = session.selectList("KPI_VALUE.getKpiValueByUserCollection", params);
        session.close();
        return result;
    }

    public String getKpiValueDailyMax() {
        SqlSession session = getSession();
        Map result = session.selectOne("KPI_VALUE.getMaxDay", DBParam.createParam());
        session.close();
        if (null == result)
            return null;
        return (String) result.get("_DAY");
    }

    public String getKpiValueMonthlyMax() {
        try (SqlSession session = getSession()) {
            String month = (String) getFromCache("MAX_MONTH");
            if (month != null && !month.isEmpty()) {
                return month;
            }
            Map result = session.selectOne("KPI_VALUE.getMaxMonth", DBParam.createParam());
            if (null == result)
                return null;
            month = (String) result.get("_MONTH");
            putIntoCache("MAX_MONTH", month);
            return month;
        }
    }

    public List<Map> getKpiValueById(String kpiNo, String startDate, String endDate, String kpiBelonging) {
        if (!paramCheck(kpiNo, startDate, endDate, kpiBelonging)) {
            return null;
        }
        SqlSession session = getSession();
        DBParam params = DBParam.createParam();
        params.put("KPI_NO", kpiNo);
        params.put("BRANCH_NO", "10000");
        params.put("START_DATE", startDate);
        params.put("END_DATE", endDate);
        params.put("KPI_BELONGING", kpiBelonging);

        List<Map> result = session.selectList("KPI_VALUE.getKpiValueById", params);
        session.close();
        return result;
    }

    public List<Map> getSubsByKpi(String kpiNo, String date, String branchNo, String kpiBelonging) {
        if (!paramCheck(kpiNo, date, branchNo, kpiBelonging)) {
            return null;
        }
        SqlSession session = getSession();
        DBParam params = DBParam.createParam();
        params.put("KPI_NO", kpiNo);
        params.put("BRANCH_NO", branchNo);
        params.put("STATIS_MON", date);
        params.put("KPI_BELONGING", kpiBelonging);

        List<Map> result = session.selectList("KPI_VALUE.getSubsByKpi", params);
        session.close();
        return result;
    }

    public List<Map> getKpiValueByKpiBranch(String kpiNo, String startDate, String endDate) {
        return null;
    }

    public int insertMaxMonth(String month, String state, String scriptFinishTime) {
        try (SqlSession session = getSession(true)) {
            DBParam param = DBParam.createParam();
            param.put("STATIS_DATE", month);
            param.put("STATE", state);
            param.put("SCRIPT_FINISH_TIME", scriptFinishTime);
            return session.insert("KPI_VALUE.insertMaxMonth", param);
        }
    }

}

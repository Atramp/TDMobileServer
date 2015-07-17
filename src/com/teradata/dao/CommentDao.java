package com.teradata.dao;

import com.teradata.bean.comment.Comment;
import org.apache.ibatis.session.SqlSession;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by alex on 15/4/22.
 */
public class CommentDao extends AbstractCommonDao {
    private static CommentDao service;

    private CommentDao() {
    }

    public static CommentDao getService() {
        if (service == null) {
            synchronized (CommentDao.class) {
                if (service == null)
                    service = new CommentDao();
            }
        }
        return service;
    }

    public Map queryCommentById(String comment_id) {
        try (SqlSession sqlSession = getSession()) {
            return sqlSession.selectOne("KPI_COMMENT.queryCommentById",
                    DBParam.createParam().put("COMMENT_ID", comment_id));
        }
    }

    public Map queryCommentCountBySet(String statis_date) {
        try (SqlSession sqlSession = getSession()) {
            return sqlSession.selectMap("KPI_COMMENT.queryCommentCountBySet",
                    DBParam.createParam().put("STATIS_DATE", statis_date), "KPI_SET_ID");
        }
    }

    public List<Map> queryUnReadCommentCountBySet(String username, String statis_date) {
        try (SqlSession sqlSession = getSession()) {
            return sqlSession.selectList("KPI_COMMENT.queryUnreadCommentCountBySet",
                    DBParam.createParam().put("STATIS_DATE", statis_date).put("USERNAME", username));
        }
    }

    public List<Map> queryCommentsByKpi(String kpi_no, String statis_date) {
        try (SqlSession sqlSession = getSession()) {
            return sqlSession.selectList("KPI_COMMENT.queryCommentsByKpi",
                    DBParam.createParam().put("KPI_NO", kpi_no).put("STATIS_DATE", statis_date));
        }
    }

    public int insertComment(Comment comment) {
        try (SqlSession sqlSession = getSession()) {
            return sqlSession.insert("KPI_COMMENT.insertComment", DBParam.createParam().put("COMMENT", comment));
        }
    }

    public int insertCommentToKpi(String kpi_no, String statis_date, Comment comment) {
        int success = 0;
        try (SqlSession sqlSession = getSession(false)) {
            success += sqlSession.insert("KPI_COMMENT.insertComment", DBParam.createParam().put("COMMENT", comment));

            DBParam param = DBParam.createParam();
            param.put("KPI_NO", kpi_no);
            param.put("STATIS_DATE", statis_date);
            param.put("COMMENT_ID", comment.getId());
            param.put("STATUS", comment.getStatus());
            success += sqlSession.insert("KPI_COMMENT.insertKpiComment", param);
            sqlSession.commit();
        }
        return success;
    }

    public int updateCommentStatus(String commentID, String status) {
        try (SqlSession sqlSession = getSession()) {
            return sqlSession.update("KPI_COMMENT.updateCommentStatus",
                    DBParam.createParam().put("COMMENT_ID", commentID).put("STATUS", status));
        }
    }

    public int insertUserLastRead(String username, String commentID) {
        try (SqlSession sqlSession = getSession()) {
            String date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date());
            return sqlSession.insert("KPI_COMMENT.insertUserLastRead",
                    DBParam.createParam()
                            .put("LAST_READ_ID", commentID)
                            .put("USERNAME", username)
                            .put("READ_TIME", date));
        }
    }
}

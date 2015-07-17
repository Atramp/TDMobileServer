package com.teradata.bean.comment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alex on 15/4/22.
 */
public class KpiComment {
    private String kpi_no;
    private String statis_date;
    private List<Comment> comments;

    public void addComment(Comment comment){
        if(comments == null)
            comments = new ArrayList<Comment>();
        comments.add(comment);
    }

    public String getKpi_no() {
        return kpi_no;
    }

    public void setKpi_no(String kpi_no) {
        this.kpi_no = kpi_no;
    }

    public String getStatis_date() {
        return statis_date;
    }

    public void setStatis_date(String statis_date) {
        this.statis_date = statis_date;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

}

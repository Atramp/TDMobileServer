package com.teradata.bean.comment;

import com.teradata.common.utils.DateUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by alex on 15/4/22.
 */
public class Comment {

    public enum STATUS {
        VALID("0"), DELETED("2");
        private String status;

        STATUS(String status) {
            this.status = status;
        }

        public String getStatus() {
            return this.status;
        }
    }

    private String id;
    private String post_to;
    private String author;
    private String content;
    private String status;
    private String create_time;
    private List<Comment> replies;

    public static Comment newInstance() {
        Comment comment = new Comment();

        comment.setId(DateUtil.getTimestamp(15));
        comment.setStatus(STATUS.VALID.getStatus());
        comment.setCreate_time(DateUtil.getTime());
        return comment;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPost_to() {
        return post_to;
    }

    public void setPost_to(String post_to) {
        this.post_to = post_to;
    }

    public List<Comment> getReplies() {
        return replies;
    }

    public void setReplies(List<Comment> replies) {
        this.replies = replies;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public List<Comment> getReplys() {
        return replies;
    }

    public void setReplys(List<Comment> replies) {
        this.replies = replies;
    }

}

package com.teradata.bean.User;

import com.teradata.common.config.Configuration;

import java.util.Calendar;
import java.util.Date;

public class SMSSession {

    private String username; //用户名
    private String code;     //用户验证码
    private Date createTime;  //创建时间
    private Date expireTime;  //结束时间

    public SMSSession(String username, String code) {
        this.username = username;
        this.code = code;
        Calendar calendar = Calendar.getInstance();
        this.createTime = calendar.getTime();
        calendar.add(Calendar.MILLISECOND, Configuration.getInt("SMS_EXPIRED_TIME", 1200000));
        this.expireTime = calendar.getTime();
    }

    public SMSSession(String username, String code, int milliSecond) {
        this.username = username;
        this.code = code;
        Calendar calendar = Calendar.getInstance();
        this.createTime = calendar.getTime();
        calendar.add(Calendar.MILLISECOND, milliSecond);
        this.expireTime = calendar.getTime();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }

}

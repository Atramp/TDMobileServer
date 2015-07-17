package com.teradata.bean.User;

/**
 * Created by alex on 14-11-3.
 */
public class User {
    private String msisdn;
    private String imei;
    private String passwd;
    private String default_passwd;
    private String os_typ;

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String getOs_typ() {
        return os_typ;
    }

    public void setOs_typ(String os_typ) {
        this.os_typ = os_typ;
    }

    public String getDefault_passwd() {
        return default_passwd;
    }

    public void setDefault_passwd(String default_passwd) {
        this.default_passwd = default_passwd;
    }
}

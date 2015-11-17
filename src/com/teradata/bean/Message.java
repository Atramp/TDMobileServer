//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.teradata.bean;

import com.google.gson.Gson;
import com.teradata.common.utils.CommonUtil.Date;
import java.util.HashMap;
import java.util.Map;

public class Message {
    private Map<String, String> custom_content;
    private static final String _MSGID = "msgId";
    private static final String _TYPE = "type";
    private static final String _TITLE = "title";
    private static final String _DESCRIPTION = "description";
    private static final String _URL = "url";

    public Message() {
        this.custom_content = new HashMap();
        this.custom_content.put("type", "3");
        this.custom_content.put("msgId", Date.getStandardTimestamp0());
    }

    public Message(String type) {
        this();
        this.custom_content.put("type", type);
    }

    public void setTitle(String title) {
        this.custom_content.put("title", title);
    }

    public void setDescription(String description) {
        this.custom_content.put("description", description);
    }

    public void setUrl(String url) {
        this.custom_content.put("url", url);
    }

    public String getTitle() {
        return this.custom_content.get("title") == null?null:((String)this.custom_content.get("title")).toString();
    }

    public String getDescription() {
        return this.custom_content.get("description") == null?null:((String)this.custom_content.get("description")).toString();
    }

    public String getUrl() {
        return this.custom_content.get("url") == null?null:((String)this.custom_content.get("url")).toString();
    }

    public Map<String, String> getCustom_content() {
        return this.custom_content;
    }

    public void setCustom_content(Map<String, String> custom_content) {
        this.custom_content = custom_content;
    }

    public String toString() {
        return (new Gson()).toJson(this.custom_content);
    }
}

package com.teradata.common;

import com.teradata.bean.User.SMSSession;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by alex on 15/4/28.
 */
public class SMSSessionManager {
    private static final ConcurrentHashMap<String, SMSSession> sessionMap = new ConcurrentHashMap<String, SMSSession>();

    public static SMSSession getSession(String key){return sessionMap.get(key);}

    public static void addSession(String key, SMSSession smsSession){
        sessionMap.put(key, smsSession);
    }

    public static void removeSession(String key){
        sessionMap.remove(key);
    }

    public static void removeSession(String key, SMSSession smsSession){
        sessionMap.remove(key,smsSession);
    }
}

package com.teradata.common.cache;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;

public class CacheUtil {
    public enum CACHES {
        APP("APP"), SERVLET("SERVLET"), KPI("KPI"), SERVLETDAILY("SERVLET_DAILY");
        private String value;

        CACHES(String str) {
            this.value = str;
        }

        public String getValue() {
            return this.value;
        }
    }

    private static final CacheManager cacheManager = new CacheManager();

    public static CacheManager getCacheManager() {
        return cacheManager;
    }

    public static Ehcache getCache() {
        return cacheManager.getEhcache(CACHES.KPI.getValue());
    }

    public static Ehcache getCache(String cacheName) {
        return cacheManager.getEhcache(cacheName);
    }

    public static void clearAll() {
        cacheManager.clearAll();
    }

    public static void clearSingleCache(String cacheName) {
        try {
            getCache(cacheName).removeAll();
        } catch (Exception e) {
            new Exception("清理缓存出错", e).printStackTrace();
        }
    }

    public static void clearSingleElement(Enum<CacheUtil.CACHES> _enum, String elementName) {
        clearSingleElement(((CacheUtil.CACHES) _enum).getValue(), elementName);
    }

    public static void clearSingleElement(String cacheName, String elementName) {
        try {
            getCache(cacheName).remove(elementName);
        } catch (Exception e) {
            new Exception("清理缓存出错", e).printStackTrace();
        }
    }
}

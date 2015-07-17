package com.teradata.servlet.filter;

import com.teradata.common.cache.CacheUtil;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.constructs.blocking.LockTimeoutException;
import net.sf.ehcache.constructs.web.AlreadyCommittedException;
import net.sf.ehcache.constructs.web.AlreadyGzippedException;
import net.sf.ehcache.constructs.web.filter.FilterNonReentrantException;
import net.sf.ehcache.constructs.web.filter.SimplePageCachingFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;



public class ServletCacheFilter extends SimplePageCachingFilter {

    @Override
    protected CacheManager getCacheManager() {
        return CacheUtil.getCacheManager();
    }

    @Override
    protected String calculateKey(HttpServletRequest httpRequest) {
        StringBuilder key = new StringBuilder().append(httpRequest.getMethod())
                .append(":").append(httpRequest.getServletPath()).append("?");
        if ("POST".equals(httpRequest.getMethod().toUpperCase())) {
            Enumeration<String> pNames = httpRequest.getParameterNames();
            while (pNames.hasMoreElements()) {
                String pName = pNames.nextElement();
                key.append(pName).append("=").append(httpRequest.getParameter(pName)).append("&");
            }
            if ("&".equals(key.charAt(key.length() - 1)))
                key.deleteCharAt(key.length() - 1);
        } else if ("GET".equals(httpRequest.getMethod().toUpperCase())) {
            key.append(httpRequest.getQueryString());
        }
        return key.toString();
    }

    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws AlreadyGzippedException, AlreadyCommittedException, FilterNonReentrantException, LockTimeoutException, Exception {
        if ("GET".equals(request.getMethod().toUpperCase())) {
            String queryString = request.getQueryString();
            if (queryString != null && queryString.indexOf("debug") > -1) {
                chain.doFilter(request, response);
                return;
            }
        }
        super.doFilter(request, response, chain);
    }
}

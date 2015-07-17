package com.teradata.servlet.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthorFilter implements Filter {

	public void destroy() {

	}

	public void init(FilterConfig config) throws ServletException {
	}

	public void doFilter(ServletRequest servletrequest, ServletResponse servletresponse, FilterChain filterchain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletrequest;
		((HttpServletResponse) servletresponse).addHeader("Access-Control-Allow-Origin", "*");
		if (true) {
			filterchain.doFilter(servletrequest, servletresponse);
			return;
		}
		if (request.getSession().getAttribute("USER_ID") == null) {
			servletresponse.setContentType("text/html;charset=UTF-8");
			servletresponse.getWriter().write("请先登录");
		} else {
			filterchain.doFilter(servletrequest, servletresponse);
		}
	}

}

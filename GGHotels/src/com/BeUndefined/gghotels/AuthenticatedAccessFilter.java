package com.BeUndefined.gghotels;

//Gabriel Chan

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ca.on.senecac.prg556.common.StringHelper;

/**
 * Servlet Filter implementation class AuthenticatedAccessFilter
 */
//Gabriel Chan
public class AuthenticatedAccessFilter implements Filter {
	
	private String loginPage = "/clientlogin.jspx";

    /**
     * Default constructor. 
     */
    public AuthenticatedAccessFilter() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
		// TODO Auto-generated method stub
		 //validate user session
		HttpServletRequest request = (HttpServletRequest)req;
		String uriLogin = request.getContextPath() + getLoginPage();
		if (null == request.getSession().getAttribute("client") && !uriLogin.equals(request.getRequestURI())) // null means not logged in, don't redirect /login.jspx
			((HttpServletResponse)resp).sendRedirect(uriLogin);
		else
			chain.doFilter(req, resp);
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException
	{
		if (StringHelper.isNotNullOrEmpty(fConfig.getInitParameter("login")))
			setLoginPage(StringHelper.stringPrefix(fConfig.getInitParameter("login"), "/"));
	}

	public synchronized String getLoginPage() {
		return loginPage;
	}

	private synchronized void setLoginPage(String loginPage) {
		this.loginPage = loginPage;
	}
}

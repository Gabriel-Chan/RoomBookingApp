package com.ckentcorp.gghotels;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ckentcorp.gghotels.data.ClientDAOFactory;

import ca.on.senecac.prg556.common.StringHelper;
import ca.senecacollege.prg556.hocorba.bean.Client;

/**
 * Servlet Filter implementation class ClientLoginFilter
 */
public class ClientLoginFilter implements Filter {

    /**
     * Default constructor. 
     */
    public ClientLoginFilter() {
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
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException 
	{
		// TODO Auto-generated method stub

		HttpServletRequest request = (HttpServletRequest)req;
		HttpServletResponse response = (HttpServletResponse)resp;
		HttpSession session = request.getSession();
		try
		{
			Client client = (Client)session.getAttribute("client");
			if(client != null)
				response.sendRedirect(request.getContextPath() + "/"); // redirect to context root folder
			String username = request.getParameter("username");
			String password = request.getParameter("password");
			if ("POST".equals(request.getMethod()) && StringHelper.isNotNullOrEmpty(username) && StringHelper.isNotNullOrEmpty(password))
			{
				client = ClientDAOFactory.getClientDAO().validateClient(username, password);
				if (client != null)
				{
					session.setAttribute("client", client);
					response.sendRedirect(request.getContextPath() + "/"); // redirect to context root folder
					return;
				}
				else
					request.setAttribute("username", username);
					request.setAttribute("unsuccessfulLogin", Boolean.TRUE);
			}
			chain.doFilter(req, resp); // continue on to login.jspx
		}
		catch (SQLException sqle)
		{
			throw new ServletException(sqle);
		}
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}

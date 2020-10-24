package com.BeUndefined.gghotels;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ca.senecacollege.prg556.hocorba.bean.Client;
import ca.senecacollege.prg556.hocorba.bean.ConferenceRoomBooking;

import com.BeUndefined.gghotels.data.ConferenceRoomBookingDAOFactory;
import com.ckentcorp.gghotels.data.ClientDAOFactory;

/**
 * Servlet Filter implementation class HOCORBAMenuFilter
 */
public class HOCORBAMenuFilter implements Filter {

    /**
     * Default constructor. 
     */
    public HOCORBAMenuFilter() {
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
		// place your code here
		try
		{
			HttpServletRequest request = (HttpServletRequest)req;
			HttpSession session = request.getSession();
			Client client = (Client)session.getAttribute("client");
			List<ConferenceRoomBooking> userBookings = ConferenceRoomBookingDAOFactory.getConferenceRoomBookingDAO().getConferenceRoomBookings(client.getId());
			request.setAttribute("client", client);
			request.setAttribute("numBookings", userBookings.size());
			
			chain.doFilter(req, resp);
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

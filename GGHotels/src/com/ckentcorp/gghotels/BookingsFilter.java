package com.ckentcorp.gghotels;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

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

import ca.senecacollege.prg556.hocorba.bean.Client;
import ca.senecacollege.prg556.hocorba.bean.ConferenceRoomBooking;

import com.BeUndefined.gghotels.data.ConferenceRoomBookingDAOFactory;
import com.ckentcorp.gghotels.BadRequestException;

/**
 * Servlet Filter implementation class BookingsFilter
 */
public class BookingsFilter implements Filter {

    /**
     * Default constructor. 
     */
    public BookingsFilter() {
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
		HttpServletRequest request = (HttpServletRequest)req;
		HttpServletResponse response = (HttpServletResponse)resp;
		HttpSession session = request.getSession();
		Client client = (Client)session.getAttribute("client");
		try
		{
			List<ConferenceRoomBooking> bookedrooms = ConferenceRoomBookingDAOFactory.getConferenceRoomBookingDAO().getConferenceRoomBookings(client.getId());
			request.setAttribute("bookedrooms", bookedrooms);
			String cancel = request.getParameter("Cancel");
			if ("POST".equals(request.getMethod()) && "Cancel".equals(cancel))
			{
				int bookingcode = Integer.parseInt(request.getParameter("bookingcode"));
				if(ConferenceRoomBookingDAOFactory.getConferenceRoomBookingDAO().getConferenceRoomBooking(bookingcode) != null)
				{
					ConferenceRoomBookingDAOFactory.getConferenceRoomBookingDAO().cancelConferenceRoomBooking(bookingcode);
					request.setAttribute("bookedrooms", ConferenceRoomBookingDAOFactory.getConferenceRoomBookingDAO().getConferenceRoomBookings(client.getId()));
				}
				else
				{
					throw new BadRequestException("Something Went Wrong!");
				}
			}
			// pass the request along the filter chain
			
		}
		catch (SQLException sqle) 
		{
			throw new ServletException(sqle);
		} 
		chain.doFilter(request, response);
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}

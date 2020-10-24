package com.BeUndefined.gghotels;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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

import ca.on.senecac.prg556.common.StringHelper;
import ca.senecacollege.prg556.hocorba.bean.Client;
import ca.senecacollege.prg556.hocorba.bean.ConferenceRoom;
import ca.senecacollege.prg556.hocorba.bean.ConferenceRoomBooking;

import com.BeUndefined.gghotels.data.ConferenceRoomBookingDAOFactory;
import com.ckentcorp.gghotels.BadRequestException;
import com.ckentcorp.gghotels.data.ConferenceRoomDAOFactory;

//Gabriel Chan
/**
 * Servlet Filter implementation class BookFilter
 */
public class BookFilter implements Filter {

    /**
     * Default constructor. 
     */
    public BookFilter() {
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
		// place your code here
		HttpServletRequest request = (HttpServletRequest)req;
		HttpServletResponse response = (HttpServletResponse)resp;
		HttpSession session = request.getSession();
		Client client = (Client)session.getAttribute("client");
		SimpleDateFormat dateformat = new SimpleDateFormat("MMMM dd, yyyy");
		Date today = new Date();
		String reqSub = request.getParameter("submit");
		String reqBook = request.getParameter("book");
		try
		{
			if ("POST".equals(request.getMethod()))
			{
				Date reqStartDate = dateformat.parse(request.getParameter("startdate"));
				if (reqStartDate.compareTo(today) > 0)//start date is after today
				{
					String startDate = dateformat.format(reqStartDate);
					int reqstarttime  = Integer.parseInt(request.getParameter("starttime"));
					int reqduration  = Integer.parseInt(request.getParameter("bookduration"));
					//add date and start time together
					long startdate = reqStartDate.getTime();
					long longstartdate = startdate + reqstarttime*60000L;
					reqStartDate.setTime(longstartdate);
					
					Integer reqmincapacity = null;
					BigDecimal reqmaxrate = null;
					boolean validcap = true;
					boolean validrate = true;
					if (StringHelper.isNotNullOrEmpty(request.getParameter("mincapacity")))
					{
						reqmincapacity  = Integer.parseInt(request.getParameter("mincapacity"));
						if (Integer.compare(reqmincapacity, 0) < 0)//compare integer object
						{
							request.setAttribute("invalidcapacity", "invalidcapacity");
							validcap = false;
						}
					}
					
					if (StringHelper.isNotNullOrEmpty(request.getParameter("maxrate")))
					{
						reqmaxrate  = new BigDecimal(request.getParameter("maxrate"));
						if (reqmaxrate.compareTo( new BigDecimal("0")) < 0)
						{
							request.setAttribute("invalidrate", "invalidrate");
							validrate = false;
						}
					}
					if ("Submit".equals(reqSub) && validcap && validrate)
					{
						List<ConferenceRoom> availableConferenceRooms = ConferenceRoomDAOFactory.getConferenceRoomDAO().findAvailableConferenceRooms(reqStartDate, reqduration, reqmincapacity, reqmaxrate);
						request.setAttribute("availconferencerooms", availableConferenceRooms);
						request.setAttribute("startdate",startDate);
						request.setAttribute("starttime",reqstarttime);
						request.setAttribute("bookduration",reqduration);
						request.setAttribute("reqmincapacity",reqmincapacity);
						request.setAttribute("reqmaxrate",reqmaxrate);
						request.setAttribute("clickSub",true);
					}
					else if ("Book".equals(reqBook))
					{
						if (StringHelper.isNullOrEmpty(request.getParameter("roomcode")) || 
								ConferenceRoomDAOFactory.getConferenceRoomDAO().getConferenceRoom(request.getParameter("roomcode")) == null)
						{
							throw new BadRequestException("Invalid Room Code!");
						}
						else
						{
							ConferenceRoomBookingDAOFactory.getConferenceRoomBookingDAO().bookConferenceRoom(client.getId(), request.getParameter("roomcode"), reqStartDate, reqduration);
							response.sendRedirect(request.getContextPath() + "/"); // redirect to context root folder
						}
					}
				}
				else // start date before today
				{
					if ("Submit".equals(reqSub))
					{
					request.setAttribute("invalidstartdate", "invalidstartdate");
					}
					else if ("Book".equals(reqBook))
					{
						throw new BadRequestException("Invalid Start Date!");
					}
				}
			}
		}
		catch (ParseException e)
		{	
			if ("Submit".equals(reqSub))
			{
			request.setAttribute("invalidstartdate", "invalidstartdate");
			}
			else if ("Book".equals(reqBook))
			{
				throw new BadRequestException("Invalid Start Date!");
			}
			
		} 
		catch (SQLException sqle) 
		{
			throw new ServletException(sqle);
		}
		chain.doFilter(req, resp);
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}

package com.ckentcorp.gghotels;

import javax.annotation.Resource;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.sql.DataSource;

import com.BeUndefined.gghotels.data.DataSourceFactory;

@WebListener
public class HOCORBAContextListener implements ServletContextListener
{

	@Resource(name = "BookingDS")
	private DataSource _ds;

	/**
	 * Default constructor.
	 */
	public HOCORBAContextListener()
	{
	}

	/**
	 * @see ServletContextListener#contextInitialized(ServletContextEvent)
	 */
	public void contextInitialized(ServletContextEvent context)
	{
		DataSourceFactory.setDataSource(_ds);
	}

	/**
	 * @see ServletContextListener#contextDestroyed(ServletContextEvent)
	 */
	public void contextDestroyed(ServletContextEvent context)
	{
		DataSourceFactory.setDataSource(null);
	}

}

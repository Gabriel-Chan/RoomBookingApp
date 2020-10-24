package com.BeUndefined.gghotels.data;

import javax.sql.DataSource;

public class DataSourceFactory 
{
	private static DataSource dataSource;
	
	public static DataSource getDataSource()
	{
		return dataSource;
	}
	
	public static void setDataSource(DataSource dataSource)
	{
		DataSourceFactory.dataSource = dataSource;
	}
}

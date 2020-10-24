package com.ckentcorp.gghotels.data;

import com.BeUndefined.gghotels.data.DataSourceFactory;

import ca.senecacollege.prg556.hocorba.dao.ClientDAO;

public class ClientDAOFactory 
{
	public static ClientDAO getClientDAO()
	{
		return new ClientData(DataSourceFactory.getDataSource());
	}
}

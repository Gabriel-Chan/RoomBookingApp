package com.ckentcorp.gghotels.data;

import com.BeUndefined.gghotels.data.DataSourceFactory;

import ca.senecacollege.prg556.hocorba.dao.ConferenceRoomDAO;

public class ConferenceRoomDAOFactory 
{
	public static ConferenceRoomDAO getConferenceRoomDAO()
	{
		return new ConferenceRoomData(DataSourceFactory.getDataSource());
	}
}

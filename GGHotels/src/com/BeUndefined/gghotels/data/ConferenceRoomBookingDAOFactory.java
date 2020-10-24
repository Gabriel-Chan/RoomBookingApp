package com.BeUndefined.gghotels.data;

import ca.senecacollege.prg556.hocorba.dao.ConferenceRoomBookingDAO;
//Gabriel Chan
public class ConferenceRoomBookingDAOFactory {

	public static ConferenceRoomBookingDAO getConferenceRoomBookingDAO()
	{
		return new ConferenceRoomBookingData(DataSourceFactory.getDataSource());
	}
}

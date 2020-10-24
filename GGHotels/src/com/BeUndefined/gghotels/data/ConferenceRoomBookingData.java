package com.BeUndefined.gghotels.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.math.BigDecimal;

import javax.sql.DataSource;

import ca.senecacollege.prg556.hocorba.bean.ConferenceRoomBooking;
import ca.senecacollege.prg556.hocorba.dao.ConferenceRoomBookingDAO;

//Gabriel Chan

class ConferenceRoomBookingData implements ConferenceRoomBookingDAO
{
	private DataSource ds;

	ConferenceRoomBookingData(DataSource ds)
	{
		setDs(ds);
	}

	@Override
	public ConferenceRoomBooking bookConferenceRoom(int clientId, String roomCode, Date start, int duration) throws SQLException
	{
		java.sql.Timestamp startDate = new java.sql.Timestamp(start.getTime());

		try (Connection conn = getDs().getConnection())
		{
			try (Statement stmt = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE))
			{
				try (ResultSet rslt = stmt.executeQuery("SELECT client_id,room_code,start_date,duration FROM booking"))
				{
					rslt.moveToInsertRow();
					rslt.updateInt("client_id", clientId);
					rslt.updateString("room_code", roomCode);
					rslt.updateTimestamp("start_date", startDate);
					rslt.updateInt("duration", duration);
					rslt.insertRow();
				}
			}
			try (Statement stmt = conn.createStatement())
			{
				try (ResultSet rslt = stmt.executeQuery("SELECT TOP 1 booking_code, room_code,name,capacity,start_date,duration,rate "
						+ "FROM booking INNER JOIN conference_room ON booking.room_code = conference_room.code ORDER BY booking_code DESC"))
				{
					if (rslt.next())
					{
						return new ConferenceRoomBooking(rslt.getInt("booking_code"), rslt.getString("room_code"),
								rslt.getString("name"),rslt.getInt("capacity"),rslt.getTimestamp("start_date"),
								rslt.getInt("duration"),(rslt.getBigDecimal("rate").multiply(new BigDecimal(duration)).divide(new BigDecimal(60))));
					}
					else
						return null;
				}
			}
		}
	}

	@Override
	public void cancelConferenceRoomBooking(int bookingCode) throws SQLException 
	{
		try (Connection conn = getDs().getConnection())
		{
			try (PreparedStatement pstmt = conn.prepareStatement("SELECT booking_code FROM booking WHERE booking_code = ?", ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE))
			{
				pstmt.setInt(1, bookingCode);
				try (ResultSet rslt = pstmt.executeQuery())
				{
					if (rslt.next())
						rslt.deleteRow();
				}
			}
		}
	}

	@Override
	public ConferenceRoomBooking getConferenceRoomBooking(int bookingCode) throws SQLException
	{
		try (Connection conn = getDs().getConnection())
		{
			try (PreparedStatement pstmt = conn.prepareStatement("SELECT booking_code, room_code,name,capacity,start_date,duration,rate "
					+ "FROM booking INNER JOIN conference_room"
					+ " ON booking.room_code = conference_room.code WHERE booking_code = ?"))
			{
				pstmt.setInt(1, bookingCode);
				try (ResultSet rslt = pstmt.executeQuery())
				{
					if (rslt.next())
					{
						return new ConferenceRoomBooking(rslt.getInt("booking_code"), rslt.getString("room_code"),
								rslt.getString("name"),rslt.getInt("capacity"),rslt.getTimestamp("start_date"),
								rslt.getInt("duration"),(rslt.getBigDecimal("rate").multiply(new BigDecimal(rslt.getInt("duration")/60))));
					}
					else
						return null;
				}
			}
		}
	}

	@Override
	public List<ConferenceRoomBooking> getConferenceRoomBookings(int clientId) throws SQLException 
	{	
		try (Connection conn = getDs().getConnection())
		{
			try (PreparedStatement pstmt = conn.prepareStatement("SELECT booking_code, room_code,name,capacity,start_date,duration,rate "
					+ "FROM booking INNER JOIN conference_room"
					+ " ON booking.room_code = conference_room.code WHERE client_id = ?", 
					ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE))
				{
					List<ConferenceRoomBooking> userBookings = new ArrayList<ConferenceRoomBooking>();
					pstmt.setInt(1, clientId);
					try (ResultSet rslt = pstmt.executeQuery())
					{
						while (rslt.next())
						{
							userBookings.add(new ConferenceRoomBooking(rslt.getInt("booking_code"), rslt.getString("room_code"),
									rslt.getString("name"),rslt.getInt("capacity"),rslt.getTimestamp("start_date"),
									rslt.getInt("duration"),(rslt.getBigDecimal("rate").multiply(new BigDecimal(rslt.getInt("duration"))).divide(new BigDecimal(60)))));
						}
					}
					return userBookings;
				}
		}
	}
	private DataSource getDs()
	{
		return ds;
	}

	private void setDs(DataSource ds)
	{
		this.ds = ds;
	}
}

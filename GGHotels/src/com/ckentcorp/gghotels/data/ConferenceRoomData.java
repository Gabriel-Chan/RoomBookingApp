package com.ckentcorp.gghotels.data;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.sql.DataSource;

import ca.senecacollege.prg556.hocorba.bean.ConferenceRoom;
import ca.senecacollege.prg556.hocorba.dao.ConferenceRoomDAO;

class ConferenceRoomData implements ConferenceRoomDAO
{
	
	private DataSource ds;

	ConferenceRoomData(DataSource ds)
	{
		setDs(ds);
	}

	@Override
	public List<ConferenceRoom> findAvailableConferenceRooms(Date start, int duration, 
			Integer minimumCapacity, BigDecimal maximumRate) throws SQLException
	{
		List<ConferenceRoom> availableConferenceRooms = null;
		java.sql.Timestamp startDate = new java.sql.Timestamp(start.getTime());
		Date end = new Date();
		long enddate = start.getTime();
		long longenddate = enddate + duration*60000L;
		end.setTime(longenddate);
		java.sql.Timestamp endDate = new java.sql.Timestamp(start.getTime());
		if(minimumCapacity == null && maximumRate == null)
		{
			try (Connection conn = getDs().getConnection())
			{
				try (Statement stmt = conn.createStatement())
				{
					try (PreparedStatement pstmt = conn.prepareStatement("SELECT code, name, capacity, rate FROM conference_room WHERE code NOT IN "
							+ "(SELECT room_code FROM booking WHERE ? < DATEADD(minute, duration, start_date) AND "
							+ "? > start_date) ORDER BY rate,capacity DESC"))
						{
							pstmt.setTimestamp(1, startDate);
							pstmt.setTimestamp(2, endDate);
					
							try (ResultSet rslt = pstmt.executeQuery())
							{
								availableConferenceRooms = new ArrayList<ConferenceRoom>();
								while (rslt.next())
								{
									availableConferenceRooms.add(new ConferenceRoom(rslt.getString("code"), rslt.getString("name"),rslt.getInt("capacity"),rslt.getBigDecimal("rate")));
								}
							}
				}
			}
		}
		}
		else if (minimumCapacity != null && maximumRate == null)
		{
			try (Connection conn = getDs().getConnection())
			{
				try (Statement stmt = conn.createStatement())
				{
					try (PreparedStatement pstmt = conn.prepareStatement("SELECT code, name, capacity, rate FROM conference_room WHERE code NOT IN "
							+ "(SELECT room_code FROM booking WHERE ? < DATEADD(minute, duration, start_date) AND "
							+ "? > start_date) AND capacity >= ? ORDER BY rate,capacity DESC"))
						{
							pstmt.setTimestamp(1, startDate);
							pstmt.setTimestamp(2, endDate);
							pstmt.setInt(3, minimumCapacity);
					
							try (ResultSet rslt = pstmt.executeQuery())
							{
								availableConferenceRooms = new ArrayList<ConferenceRoom>();
								while (rslt.next())
								{
									availableConferenceRooms.add(new ConferenceRoom(rslt.getString("code"), rslt.getString("name"),rslt.getInt("capacity"),rslt.getBigDecimal("rate")));
								}
							}
						}
				}
			}
		}
		else if (minimumCapacity == null && maximumRate != null)
		{
			try (Connection conn = getDs().getConnection())
			{
				try (Statement stmt = conn.createStatement())
				{
					try (PreparedStatement pstmt = conn.prepareStatement("SELECT code, name, capacity, rate FROM conference_room WHERE code NOT IN "
							+ "(SELECT room_code FROM booking WHERE ? < DATEADD(minute, duration, start_date) AND "
							+ "? > start_date) AND rate <= ? ORDER BY rate,capacity DESC"))
						{
							pstmt.setTimestamp(1, startDate);
							pstmt.setTimestamp(2, endDate);
							pstmt.setBigDecimal(3, maximumRate);
					
							try (ResultSet rslt = pstmt.executeQuery())
							{
								availableConferenceRooms = new ArrayList<ConferenceRoom>();
								while (rslt.next())
								{
									availableConferenceRooms.add(new ConferenceRoom(rslt.getString("code"), rslt.getString("name"),rslt.getInt("capacity"),rslt.getBigDecimal("rate")));
								}
							}
						}
				}
			}
		}
		else
		{
			try (Connection conn = getDs().getConnection())
			{
				try (Statement stmt = conn.createStatement())
				{
					try (PreparedStatement pstmt = conn.prepareStatement("SELECT code, name, capacity, rate FROM conference_room WHERE code NOT IN "
							+ "(SELECT room_code FROM booking WHERE ? < DATEADD(minute, duration, start_date) AND "
							+ "? > start_date) AND capacity >= ? AND rate <= ? ORDER BY rate,capacity DESC"))
						{
							pstmt.setTimestamp(1, startDate);
							pstmt.setTimestamp(2, endDate);
							pstmt.setInt(3, minimumCapacity);
							pstmt.setBigDecimal(4, maximumRate);
					
							try (ResultSet rslt = pstmt.executeQuery())
							{
								availableConferenceRooms = new ArrayList<ConferenceRoom>();
								while (rslt.next())
								{
									availableConferenceRooms.add(new ConferenceRoom(rslt.getString("code"), rslt.getString("name"),rslt.getInt("capacity"),rslt.getBigDecimal("rate")));
								}
							}
						}
				}
			}
		}
		return availableConferenceRooms;
	}

	@Override
	public ConferenceRoom getConferenceRoom(String roomCode) throws SQLException 
	{
		try (Connection conn = getDs().getConnection())
		{
			try (PreparedStatement pstmt = conn.prepareStatement("SELECT code, name, capacity, rate FROM conference_room WHERE code = ?"))
			{
				pstmt.setString(1, roomCode);
				try (ResultSet rslt = pstmt.executeQuery())
				{
					if (rslt.next())
						return new ConferenceRoom(rslt.getString("code"), rslt.getString("name"),rslt.getInt("capacity"),rslt.getBigDecimal("rate"));
					else
						return null;
				}
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

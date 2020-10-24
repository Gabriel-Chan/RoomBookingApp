package com.ckentcorp.gghotels.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import ca.senecacollege.prg556.hocorba.bean.Client;
import ca.senecacollege.prg556.hocorba.dao.ClientDAO;

public class ClientData implements ClientDAO
{
	private DataSource ds;

	ClientData(DataSource ds)
	{
		setDs(ds);
	}

	@Override
	public Client getClient(int id) throws SQLException
	{
		try (Connection conn = getDs().getConnection())
		{
			try (PreparedStatement pstmt = conn.prepareStatement("SELECT id, first_name, last_name FROM client WHERE id = ?"))
			{
				pstmt.setInt(1, id);
				try (ResultSet rslt = pstmt.executeQuery())
				{
					if (rslt.next())
						return new Client(rslt.getInt("id"), rslt.getString("first_name"),rslt.getString("last_name"));
					else
						return null;
				}
			}
		}
	}

	@Override
	public Client validateClient(String username, String password) throws SQLException
	{
		try (Connection conn = getDs().getConnection())
		{
			try (PreparedStatement pstmt = conn.prepareStatement("SELECT id FROM client WHERE username = ? AND password = ?"))
			{
				pstmt.setString(1, username);
				pstmt.setString(2, password);
				try (ResultSet rslt = pstmt.executeQuery())
				{
					if (rslt.next())
						return getClient(rslt.getInt("id"));
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

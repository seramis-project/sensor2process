package at.ac.wu.seramis.sensor2process.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Database
{
	private static Database _instance = null; // singleton

	private static final String DRIVER = "org.postgresql.Driver";

	public static final String SERVER = "localhost";
	public static final Integer PORT = 5432;
	public static final String DB_NAME = "spot";
	public static final String USER = "spot";
	public static final String PASSW = "<DB_PASSWORD>";

	private Connection dbConnection = null;

	private Database()
	{
	}

	public static boolean establishConnection(String address, int port, String database, String username, String password)
	{
		// initialize PSQL JDBC driver
		try
		{
			Class.forName(DRIVER);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}

		// connect to the PSQL server
		try
		{
			getInstance().dbConnection = DriverManager.getConnection("jdbc:postgresql://" + address + ":" + port + "/" + database, username, password);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return false;
		}

		return true;
	}

	public static boolean closeConnection()
	{
		try
		{
			getInstance().dbConnection.close();
		}
		catch (Exception e)
		{
			return false;
		}

		return true;
	}

	public static PreparedStatement getPreparedStatement(String sql)
	{
		try
		{
			return getInstance().dbConnection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	public static PreparedStatement getPreparedStatement2(String sql)
	{
		try
		{
			return getInstance().dbConnection.prepareStatement(sql, Statement.NO_GENERATED_KEYS);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	public static PreparedStatement getLazyStatement(String sql)
	{
		try
		{
			getInstance().dbConnection.setAutoCommit(false);
			return getInstance().dbConnection.prepareStatement(sql);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	public static ResultSet sql(String sql)
	{
		// System.out.println("SQL: " + sql);

		try
		{
			if (sql.toUpperCase().startsWith("SELECT"))
			{
				return getInstance().dbConnection.createStatement().executeQuery(sql);
			}
			else
			{
				Statement stmt = getInstance().dbConnection.createStatement();
				stmt.execute(sql, Statement.RETURN_GENERATED_KEYS);
				return stmt.getGeneratedKeys();
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	private static Database getInstance()
	{
		if (_instance == null)
		{
			synchronized (Database.class)
			{
				if (_instance == null)
				{
					_instance = new Database();
				}
			}
		}

		return _instance;
	}
}
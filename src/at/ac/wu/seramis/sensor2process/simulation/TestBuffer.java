package at.ac.wu.seramis.sensor2process.simulation;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;

import at.ac.wu.seramis.sensor2process.database.Database;

public class TestBuffer extends Buffer
{

	public TestBuffer()
	{
		Database.establishConnection(Database.SERVER, Database.PORT, Database.DB_NAME, Database.USER, Database.PASSW);
	}

	@Override
	public void run()
	{

		while (true)
		{
			try
			{
				this.buffer.clear();
				this.buffer.addAll(this.TestFittingRoom());
				this.buffer.addAll(this.TestMovingTrivial());
				this.buffer.addAll(this.TestMovingConservative());
				this.buffer.addAll(this.TestStartsMoving());
				this.buffer.addAll(this.TestStopsMoving());

				System.err.println("Buffersize: " + this.buffer.size());

				while (this.buffer.size() > 35)
				{
					Thread.sleep(10000);
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	private ArrayList<RawRead> TestFittingRoom() throws SQLException
	{
		ResultSet result = Database.sql("SELECT * from readings_new_sorted  WHERE epc = 'urn:epc:tag:gid-96:66220150.40744.100004'");
		ArrayList<RawRead> listReads = new ArrayList<RawRead>();

		while (result.next())
		{
			listReads.add(new RawRead(result.getString("epc"), result.getString("id_device"), result.getInt("ant"), result.getInt("rssi"), LocalDateTime.ofInstant(Instant.ofEpochMilli(result.getLong("time_millis")), ZoneOffset.ofHours(0))));
		}

		result.close();
		return listReads;
	}

	private ArrayList<RawRead> TestStopsMoving()
	{
		ArrayList<RawRead> listReads = new ArrayList<RawRead>();

		return listReads;
	}

	private ArrayList<RawRead> TestStartsMoving()
	{
		ArrayList<RawRead> listReads = new ArrayList<RawRead>();

		return listReads;
	}

	private ArrayList<RawRead> TestMovingConservative()
	{
		ArrayList<RawRead> listReads = new ArrayList<RawRead>();

		return listReads;
	}

	private ArrayList<RawRead> TestMovingTrivial()
	{
		ArrayList<RawRead> listReads = new ArrayList<RawRead>();
		// RawRead read1 = new RawRead(result.getString("epc"),
		// result.getString("id_device"), result.getInt("ant"),
		// result.getInt("rssi"),
		// LocalDateTime.ofInstant(Instant.ofEpochMilli(result.getLong("time_millis")),
		// ZoneOffset.ofHours(0))
		return listReads;
	}
}

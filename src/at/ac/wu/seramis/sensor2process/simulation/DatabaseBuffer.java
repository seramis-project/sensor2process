package at.ac.wu.seramis.sensor2process.simulation;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import at.ac.wu.seramis.sensor2process.database.Database;

public class DatabaseBuffer extends Buffer
{
	public static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");
	private long t2;
	private long t1;

	public DatabaseBuffer(long t1, long t2)
	{
		Database.establishConnection(Database.SERVER, Database.PORT, Database.DB_NAME, Database.USER, Database.PASSW);
		this.t1 = t1;
		this.t2 = t2;
	}

	@Override
	public void run()
	{
		// IDEA was to speed up processing by using a cursor on the results.
		// However, this code does not really work.
		int pos = 1;
		long max = 10;
		try
		{
			ResultSet result = Database.sql("SELECT count(*) as c FROM rtls_reads WHERE time_millis >= '" + this.t1 + "' AND time_millis < '" + this.t2 + "'");
			// ResultSet result = Database.sql("SELECT count(*) as c from
			// rtls_misplaced");

			if (result.next())
			{
				max = result.getLong("c");
			}

			PreparedStatement statement = Database.getLazyStatement("SELECT * " + "FROM rtls_reads  WHERE time_millis >= '" + this.t1 + "' AND time_millis < '" + this.t2 + "' ORDER BY time_millis");
			// PreparedStatement statement = Database.getLazyStatement("select
			// id, epc, id_device, ant, rssi, process_name, time_stamp,
			// time_millis, time_zone from rtls_misplaced");
			statement.setFetchSize(1000); // configure the fetch size
			statement.setQueryTimeout(0);
			result = statement.executeQuery();

			NumberFormat format = NumberFormat.getPercentInstance();
			format.setMinimumFractionDigits(2);

			while (result.next())
			{
				this.buffer.add(new RawRead(result.getString("epc"), result.getString("id_device"), result.getInt("ant"), result.getInt("rssi"), LocalDateTime.ofInstant(Instant.ofEpochMilli(result.getLong("time_millis")), ZoneId.systemDefault().getRules().getOffset(Instant.ofEpochMilli(result.getLong("time_millis"))))));
				pos += 1;

				if (pos % 10000 == 0)
				{
					try
					{
						System.err.println("Buffersize: " + this.buffer.size() + " \t" + ((RawRead) this.buffer.get(0)).getTimestamp().format(DATE_TIME_FORMAT) + " \t" + format.format((double) pos / max));
					}
					catch (Exception e)
					{
						continue;
					}
				}

				while (this.buffer.size() > 20000)
				{
					try
					{
						System.err.print("z");
						Thread.sleep(100);
					}
					catch (InterruptedException e)
					{
					}
				}
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}

		this.buffer.add(null);

		// boolean allItemsProcessed = false;
		// while (!allItemsProcessed)
		// {
		// try
		// {
		// ResultSet result = Database.sql("SELECT * FROM rtls_reads WHERE
		// time_millis > '1477346400000' AND time_millis < '1477510800000' ORDER
		// BY time_millis LIMIT 10000 OFFSET " + pos);
		//
		// int lastPos = pos;
		// while (result.next())
		// {
		// this.buffer.add(new RawRead(result.getString("epc"),
		// result.getString("id_device"), result.getInt("ant"),
		// result.getInt("rssi"),
		// LocalDateTime.ofInstant(Instant.ofEpochMilli(result.getLong("time_millis")),
		// ZoneOffset.ofHours(0))));
		// pos += 1;
		// }
		// if (lastPos == pos){ // no new results!
		// allItemsProcessed = true;
		// this.buffer.add(null);
		// }
		//
		//
		// result.close();
		// try
		// {
		// System.err.println("Buffersize: " + this.buffer.size() + " " +
		// this.buffer.get(0).getTimestamp().format(SPOT.DATE_TIME_FORMAT));
		// }
		// catch (Exception e)
		// {
		// continue;
		// }
		//
		// while (this.buffer.size() > 20000)
		// {
		// Thread.sleep(100);
		// }
		// }
		// catch (Exception e)
		// {
		// e.printStackTrace();
		// break;
		// }
		// }
	}
}

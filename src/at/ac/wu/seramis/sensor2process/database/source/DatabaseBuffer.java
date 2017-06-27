package at.ac.wu.seramis.sensor2process.database.source;

import at.ac.wu.seramis.sensor2process.database.Database;
import at.ac.wu.seramis.sensor2process.database.model.RawRead;
import at.ac.wu.seramis.sensor2process.utils.Helpers;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class DatabaseBuffer extends Buffer
{

	public DatabaseBuffer()
	{
		Database.establishConnection(Database.SERVER, Database.PORT, Database.DB_NAME, Database.USER, Database.PASSW);
	}

	@Override
	public void run()
	{
		int pos = 1;
        long max = 10;
		try {
            ResultSet result = Database.sql("SELECT count(*) as c FROM rtls_reads  WHERE time_millis > '1477346400000' AND time_millis < '1477510800000'");
            if (result.next()){
                max = result.getLong("c");
            }

            PreparedStatement statement = Database.getLazyStatement("SELECT id, epc, id_device, ant, rssi, process_name, time_stamp, time_millis, time_zone " +
					"FROM rtls_reads  " +
					"WHERE time_millis > '1477346400000' AND time_millis < '1477510800000' ORDER BY time_millis");
			statement.setFetchSize(1000); // configure the fetch size
            statement.setQueryTimeout(0);
			result = statement.executeQuery();

			NumberFormat format = NumberFormat.getPercentInstance();
			format.setMinimumFractionDigits(2);


			while (result.next()){
				this.buffer.add(new RawRead(result.getString("epc"), result.getString("id_device"), result.getInt("ant"), result.getInt("rssi"), LocalDateTime.ofInstant(Instant.ofEpochMilli(result.getLong("time_millis")), ZoneOffset.ofHours(0))));
				pos += 1;

				if (pos % 10000 == 0) {
					try {
						System.err.println("Buffersize: " + this.buffer.size() + " \t" + this.buffer.get(0).getTimestamp().format(Helpers.DATE_TIME_FORMAT) + " \t" + format.format((double) pos / max));
					} catch (Exception e) {
						continue;
					}
				}

				while (this.buffer.size() > 20000)
				{
					try {
						System.err.print("z");
                        Thread.sleep(100);
                    } catch (InterruptedException e) {}
                }
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		this.buffer.add(null);
	}
}

package at.ac.wu.seramis.sensor2process.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import at.ac.wu.seramis.sensor2process.cep.model.events.SenseEvent;
import at.ac.wu.seramis.sensor2process.cep.model.events.TemporalSenseEvent;
import at.ac.wu.seramis.sensor2process.database.model.Actor;
import at.ac.wu.seramis.sensor2process.database.model.Sensor;

public class DataAccess
{

	private HashMap<Integer, ArrayList<Integer>> clusterTrajectories = new HashMap<>();

	public static ArrayList<Actor> getActors(String database)
	{
		ArrayList<Actor> actors = new ArrayList<>();

		try
		{
			ResultSet result = Database.sql("SELECT DISTINCT epc FROM " + database + " ORDER BY epc ASC");

			while (result.next())
			{
				actors.add(new Actor(result.getString(1)));
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return actors;
	}

	public Actor getActor(String epc, String database) throws SQLException, Exception
	{

		try
		{

			ResultSet result = Database.sql("SELECT * FROM " + database + " WHERE epc = '" + epc + "'");

			Actor actor = new Actor(epc);

			ArrayList<TemporalSenseEvent> listReads = new ArrayList<>();

			while (result.next())
			{

				int readID = result.getInt("id");
				Sensor sensor = this.getSensorById(result.getString("id_device"), result.getInt("ant"));
				int rssi = result.getInt("rssi");

				Calendar cal = Calendar.getInstance();
				long time_millis = result.getLong("time_millis");
				cal.setTimeInMillis(time_millis);

				Date date = cal.getTime();
				SenseEvent event = new SenseEvent();
				event.setId(epc);
				event.setTimestamp(time_millis);
				event.setSignalStrength(rssi);
				event.setX(sensor.getPosX());
				event.setY(sensor.getPosY());
				listReads.add(event);
			}
			Actor actor2 = new Actor(epc);
			actor2.setReads(listReads);
			return actor2;

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public ArrayList<Sensor> getSensors() throws Exception
	{

		try
		{

			// Create statement to query DB
			ResultSet result = Database.sql("SELECT * FROM sensors");
			ArrayList<Sensor> sensorsList = new ArrayList<>();
			while (result.next())
			{
				// Query to object

				String id_device = result.getString("id_device");
				int antenna = result.getInt("antenna");
				String zone = result.getString("zone");
				double pos_x = result.getFloat("pos_x");
				double pos_y = result.getFloat("pos_y");
				double range_x = result.getFloat("range_x");
				double range_y = result.getFloat("range_y");

				Sensor sensor = new Sensor(id_device, antenna, zone, pos_x, pos_y, range_x, range_y);
				sensorsList.add(sensor);
			}

			return sensorsList;

		}
		catch (Exception e)
		{
			throw e;
		}

	}

	public static HashMap<String, Sensor> getSensorMap()
	{
		HashMap<String, Sensor> sensorMap = new HashMap<>();
		try
		{

			// Create statement to query DB
			ResultSet result = Database.sql("SELECT * FROM sensors");
			ArrayList<Sensor> sensorsList = new ArrayList<>();
			while (result.next())
			{
				// Query to object

				String id_device = result.getString("id_device");
				int antenna = result.getInt("antenna");
				String zone = result.getString("zone");
				double pos_x = result.getFloat("pos_x");
				double pos_y = result.getFloat("pos_y");
				double range_x = result.getFloat("range_x");
				double range_y = result.getFloat("range_y");

				Sensor sensor = new Sensor(id_device, antenna, zone, pos_x, pos_y, range_x, range_y);
				sensorMap.put(id_device + "+" + antenna, sensor);
			}

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return sensorMap;
	}

	public Sensor getSensorById(String sensorID, int ant) throws Exception
	{

		try
		{

			// Create statement to query DB
			ResultSet result = Database.sql("SELECT * FROM sensors WHERE id_device = '" + sensorID + "' AND antenna = '" + ant + "'");
			while (result.next())
			{
				// Query to object

				String id_device = result.getString("id_device");
				int antenna = result.getInt("antenna");
				String zone = result.getString("zone");
				double pos_x = result.getFloat("pos_x");
				double pos_y = result.getFloat("pos_y");
				double range_x = result.getFloat("range_x");
				double range_y = result.getFloat("range_y");

				Sensor sensor = new Sensor(id_device, antenna, zone, pos_x, pos_y, range_x, range_y);

				return sensor;
			}

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	// Modify method so mongo accesses to this.
	public static ArrayList<TemporalSenseEvent> getReadsNightByClass(String selectedClass)
	{
		ArrayList<TemporalSenseEvent> listReads = new ArrayList<>();

		try
		{
			ResultSet result = Database.sql("SELECT * FROM gdp_night ORDER BY epc");// WHERE
																					// substring(epc
																					// from
																					// 22
																					// for
																					// 2)
																					// =
																					// '"
																					// +
																					// selectedClass
																					// +
																					// "'");
			int count = 0;

			// disable to speed up app start
			// while (result.next())
			// {
			// int readID = result.getInt("id");
			// String epc = result.getString("epc");
			// int rssi = result.getInt("rssi");
			// long time_millis = result.getLong("time_millis");
			// double pos_x = result.getFloat("pos_x");
			// double pos_y = result.getFloat("pos_y");
			// String epc_code_piece = result.getString("epc_code_piece");
			//
			// listReads.add(new ReadNight(readID, epc, rssi, time_millis,
			// pos_x, pos_y, epc_code_piece));
			// System.out.println(new ReadNight(readID, epc, rssi, time_millis,
			// pos_x, pos_y, epc_code_piece));
			// count++;
			// }
			// System.out.println(count);

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return listReads;
	}

	public HashMap<Integer, ArrayList<Integer>> getClusterTrajectories()
	{
		return this.clusterTrajectories;
	}

	public void setClusterTrajectories(HashMap<Integer, ArrayList<Integer>> clusterTrajectories)
	{
		this.clusterTrajectories = clusterTrajectories;
	}

//	public static HashMap<String, Integer> getEPCClassesMap()
//	{
//		HashMap<String, Integer> classesMap = new HashMap<>();
//		try
//		{
//			// Create statement to query DB
//			ResultSet result = Database.sql("SELECT * from erfurt.classes_from_october order by \"EPC\""); // classes for Erfurt experiment
//
//			while (result.next())
//			{
//				// Query to object
//
//				String EPC = result.getString("EPC").substring(0, 12);
//				int Planungsgruppe = result.getInt("Planungsgruppe");
//
//				classesMap.put(EPC, Planungsgruppe);
//			}
//
//		}
//		catch (Exception e)
//		{
//			e.printStackTrace();
//		}
//		return classesMap;
//	}

}

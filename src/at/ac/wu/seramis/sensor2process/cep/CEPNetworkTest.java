package at.ac.wu.seramis.sensor2process.cep;

import at.ac.wu.seramis.sensor2process.cep.model.events.IBusinessEvent;
import at.ac.wu.seramis.sensor2process.cep.model.events.SenseEvent;
import at.ac.wu.seramis.sensor2process.cep.model.events.TemporalPosition;
import at.ac.wu.seramis.sensor2process.cep.model.events.TemporalSenseEvent;
import at.ac.wu.seramis.sensor2process.cep.model.patterns.*;
import at.ac.wu.seramis.sensor2process.database.DataAccess;
import at.ac.wu.seramis.sensor2process.database.Database;
import at.ac.wu.seramis.sensor2process.database.model.Sensor;
import at.ac.wu.seramis.sensor2process.utils.CEPConnector;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;
import org.apache.flink.streaming.api.functions.source.SourceFunction;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.HashMap;



public class CEPNetworkTest extends Thread
{
	public static void main(String[] args) throws Exception
	{
		new CEPNetworkTest().start();
	}

	@Override
	public void run()
	{
		StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

		DataStream<TemporalSenseEvent> dataStream = env.addSource(new UDPTextStream<String>()).map(new SensorReading());

		DataStream<TemporalPosition> positionDataStream = CEPCenterOfGravity.extractPositionsFromSensors(dataStream);

		DataStream<IBusinessEvent> fittingEvents = CEPBusinessPosition.toBusinessEventsStream(positionDataStream, new FittingRoomPattern());
		DataStream<IBusinessEvent> trivialMovingEvents = CEPBusinessPosition.toBusinessEventsStream(positionDataStream, new MovingTrivialPattern());

		DataStream<IBusinessEvent> conservativeMovingEvents = CEPBusinessPosition.toFlatBusinessEventsStream(trivialMovingEvents, new MovingConservativePattern());

		DataStream<IBusinessEvent> conservativeAndTrivialEvts = conservativeMovingEvents.union(trivialMovingEvents);

		DataStream<IBusinessEvent> startsMovingEvents = CEPBusinessPosition.toFlatBusinessEventsStream(conservativeAndTrivialEvts, new StartsMovingPattern());

		DataStream<IBusinessEvent> stopsMovingEvents = CEPBusinessPosition.toFlatBusinessEventsStream(conservativeAndTrivialEvts, new StopsMovingPattern());

		DataStream<IBusinessEvent> businessEventDataStream = conservativeMovingEvents.union(startsMovingEvents, stopsMovingEvents);

		businessEventDataStream.print();
		fittingEvents.print();

		positionDataStream.addSink(new EventSink<>());
		// positionDataStream.print();

		try
		{
			env.execute("Simulated DataStream");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	class EventSink<T extends TemporalPosition> extends RichSinkFunction<T>
	{
		private static final long serialVersionUID = 1360796110321772643L;

		@Override
		public void invoke(T value) throws Exception
		{
			CEPConnector.getTemporalPositions().put(value.getId(), value);
		}
	}

	public static class SensorReading implements MapFunction<String, TemporalSenseEvent>
	{
		private static final long serialVersionUID = -9057427751350352940L;

		private static HashMap<String, Sensor> _sensors;

		static
		{
			Database.establishConnection(Database.SERVER, Database.PORT, Database.DB_NAME, Database.USER, Database.PASSW);
			_sensors = DataAccess.getSensorMap();
		}

		@Override
		public SenseEvent map(String value) throws Exception
		{
			String[] data = value.split(";");

			SenseEvent senseEvent = new SenseEvent();
			senseEvent.setId(data[0]);
			senseEvent.setX(_sensors.get(data[1] + "+" + data[2]).getPosX());
			senseEvent.setY(_sensors.get(data[1] + "+" + data[2]).getPosY());
			senseEvent.setSignalStrength(Double.valueOf(data[3]));
			senseEvent.setTimestamp(Long.valueOf(data[4]));

			return senseEvent;
		}

	}

	static class UDPTextStream<T> implements SourceFunction<String>
	{
		private static final long serialVersionUID = -8171568396068656378L;

		@Override
		public void run(org.apache.flink.streaming.api.functions.source.SourceFunction.SourceContext<String> ctx) throws Exception
		{
			DatagramSocket udpSocket = null;

			try
			{
				udpSocket = new DatagramSocket(1337);

				byte[] buffer = new byte[65536];
				DatagramPacket incoming = new DatagramPacket(buffer, buffer.length);

				while (true)
				{
					udpSocket.receive(incoming);
					byte[] data = incoming.getData();
					String s = new String(data, 0, incoming.getLength());
					long timestamp = Long.parseLong(s.substring(s.length() - 13, s.length()));
					ctx.collectWithTimestamp(s, timestamp);
				}
			}
			catch (IOException e)
			{
				System.err.println("IOException " + e);
			}
		}

		@Override
		public void cancel()
		{
			// TODO Auto-generated method stub
		}

	}
}

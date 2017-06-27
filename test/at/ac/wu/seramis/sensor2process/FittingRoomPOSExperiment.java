package at.ac.wu.seramis.sensor2process;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.timestamps.AscendingTimestampExtractor;
import org.junit.Test;

import at.ac.wu.seramis.sensor2process.cep.CEPBusinessPosition;
import at.ac.wu.seramis.sensor2process.cep.CEPCenterOfGravity;
import at.ac.wu.seramis.sensor2process.cep.CEPNetworkTest;
import at.ac.wu.seramis.sensor2process.cep.model.events.IBusinessEvent;
import at.ac.wu.seramis.sensor2process.cep.model.events.MovingTrivial;
import at.ac.wu.seramis.sensor2process.cep.model.events.PositionBusinessEvent;
import at.ac.wu.seramis.sensor2process.cep.model.events.TemporalPosition;
import at.ac.wu.seramis.sensor2process.cep.model.events.TemporalSenseEvent;
import at.ac.wu.seramis.sensor2process.cep.model.patterns.FittingRoomPattern;
import at.ac.wu.seramis.sensor2process.cep.model.patterns.MovingTrivialPattern;
import at.ac.wu.seramis.sensor2process.cep.model.patterns.PointOfSalesPattern;
import at.ac.wu.seramis.sensor2process.sinks.TestSink;

// import at.ac.wu.seramis.sensor2process.database.DataAccessNEW;

/**
 * Created by andreas on 11/22/16.
 */
public class FittingRoomPOSExperiment
{
	@Test
	public void testFittingRoomAndPointOfSale() throws Exception
	{
		StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
		env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);

		DataStream<TemporalSenseEvent> dataStream = env.addSource(new TextStream<String>(0, 0)).map(new CEPNetworkTest.SensorReading()).assignTimestampsAndWatermarks(new AscendingTimestampExtractor<TemporalSenseEvent>()
		{
			@Override
			public long extractAscendingTimestamp(TemporalSenseEvent temporalSenseEvent)
			{
				return temporalSenseEvent.getTimestamp();

			}
		});
		DataStream<TemporalPosition> positionDataStream = CEPCenterOfGravity.extractPositionsFromSensorsEventTime(dataStream, 1);
		DataStream<IBusinessEvent> fittingEvents = CEPBusinessPosition.toBusinessEventsStream(positionDataStream, new FittingRoomPattern());

		// positionDataStream.print();
		DataStream<IBusinessEvent> trivialMovingEvents = CEPBusinessPosition.toBusinessEventsStream(positionDataStream, new MovingTrivialPattern());

		DataStream<IBusinessEvent> posEvents = CEPBusinessPosition.toBusinessEventsStream(positionDataStream, new PointOfSalesPattern());
		// Converts the general stream into the Postgres-specific
		// representation:
		// fittingEvents.print();
		// posEvents.print();
		// trivialMovingEvents.print();
		DataStream<IBusinessEvent> complete = fittingEvents.union(posEvents, trivialMovingEvents.filter(value -> ((MovingTrivial) value).isMoving()));

		// fittingAndPOS.print();

		// DataStream<BusinessEvent> pgsqlBusinessEvents =
		// complete.map(businessEvent ->
		// BusinessEventConverter.convert(businessEvent));
		// // Add a new Postgres Sink:
		TestSink sinkFunction = new TestSink();
		complete.addSink(sinkFunction);

		try
		{
			env.execute("Simulated DataStream");
			Experiment experiment = new Experiment();
			for (IBusinessEvent be : sinkFunction.testObjects)
			{
				experiment.add(be);
			}
			experiment.finalize();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

	}

	public class Experiment
	{
		private List<String> fittingRoomGroups = Arrays.asList(new String[] { "1", "10", "12", "15", "4", "16", "8" });
		private HashMap<String, String> mapping;

		private Set<String> truePositives, falsePositives, falseNegatives;

		public Experiment()
		{
			// mapping = DataAccessNEW.getEPCGroupsTMP();
			this.truePositives = new HashSet<>();
			this.falsePositives = new HashSet<>();
			this.falseNegatives = new HashSet<>();
		}

		public void add(IBusinessEvent value) throws Exception
		{
			if (value instanceof PositionBusinessEvent && value.getType().equals(PositionBusinessEvent.FITTING_ROOM_TYPE))
			{
				String group = this.mapping.get(value.getId());
				if (this.fittingRoomGroups.contains(group))
				{
					this.truePositives.add(value.getId());
				}
				else
				{
					this.falsePositives.add(value.getId());
				}
			}
		}

		@Override
		public void finalize()
		{
			for (String epc : this.mapping.keySet())
			{
				String group = this.mapping.get(epc);
				if (this.fittingRoomGroups.contains(group) && !this.truePositives.contains(epc))
				{
					this.falseNegatives.add(epc);
				}
			}
			double precision = this.truePositives.size() / (double) (this.truePositives.size() + this.falsePositives.size());
			double recall = this.truePositives.size() / (double) (this.truePositives.size() + this.falseNegatives.size());
			System.out.println("Precision: " + precision + " \nRecall: " + recall);
		}
	}
}

package at.ac.wu.seramis.sensor2process;

import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.timestamps.AscendingTimestampExtractor;
import org.junit.Assert;
import org.junit.Test;

import at.ac.wu.seramis.sensor2process.cep.CEPBusinessPosition;
import at.ac.wu.seramis.sensor2process.cep.model.events.IBusinessEvent;
import at.ac.wu.seramis.sensor2process.cep.model.events.PositionEvent;
import at.ac.wu.seramis.sensor2process.cep.model.events.TemporalPosition;
import at.ac.wu.seramis.sensor2process.cep.model.patterns.MovingConservativePattern;
import at.ac.wu.seramis.sensor2process.cep.model.patterns.MovingTrivialPattern;
import at.ac.wu.seramis.sensor2process.cep.model.patterns.StartsMovingPattern;
import at.ac.wu.seramis.sensor2process.cep.model.patterns.StopsMovingPattern;
import at.ac.wu.seramis.sensor2process.sinks.TestSink;

public class StopsMovingTest
{
	private static final String EPC = "epcTest2";

	@Test
	public void testTrivialMove() throws Exception
	{
		PositionEvent e1 = new PositionEvent();
		e1.setId(EPC);
		e1.setTimestamp(1477346400000l); // 10/25/2016 00:00:000
		e1.setCertainty(0.8);
		e1.setX(10);
		e1.setY(10);

		PositionEvent e5 = new PositionEvent();
		e5.setId(EPC);
		e5.setTimestamp(1477348200000l); // 10/25/2016 00:30:000
		e5.setCertainty(0.8);
		e5.setX(11);
		e5.setY(9);

		PositionEvent e6 = new PositionEvent();
		e6.setId(EPC);
		e6.setTimestamp(1477349100000l); // 10/25/2016 00:45:000
		e6.setCertainty(0.8);
		e6.setX(10);
		e6.setY(10);

		// moved 5 to the right
		PositionEvent e2 = new PositionEvent();
		e2.setId(EPC);
		e2.setTimestamp(1477350000000l); // 10/25/2016 01:00:000
		e2.setCertainty(0.8);
		e2.setX(15);
		e2.setY(10);

		// moved 10 up, 5 minutes later
		PositionEvent e3 = new PositionEvent();
		e3.setId(EPC);
		e3.setTimestamp(1477350300000l); // 10/25/2016 01:05:000
		e3.setCertainty(0.8);
		e3.setX(15);
		e3.setY(0);

		PositionEvent e4 = new PositionEvent();
		e4.setId(EPC);
		e4.setTimestamp(1477350600000l); // 10/25/2016 01:10:000
		e4.setCertainty(0.8);
		e4.setX(15);
		e4.setY(0);

		// moved 1 down and 1 left, 5 minutes later
		PositionEvent e7 = new PositionEvent();
		e7.setId(EPC);
		e7.setTimestamp(1477350900000l); // 10/25/2016 01:15:000
		e7.setCertainty(0.8);
		e7.setX(15);
		e7.setY(0);

		// moved 1 down and 1 left, 5 minutes later
		PositionEvent e8 = new PositionEvent();
		e8.setId(EPC);
		e8.setTimestamp(1477351200000l); // 10/25/2016 01:20:000
		e8.setCertainty(0.8);
		e8.setX(15);
		e8.setY(0);

		StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
		env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);

		DataStream<TemporalPosition> dataStream = env.fromElements(e1, e5, e6, e2, e3, e4, e7, e8).map(e -> (TemporalPosition) e).assignTimestampsAndWatermarks(new AscendingTimestampExtractor<TemporalPosition>()
		{
			@Override
			public long extractAscendingTimestamp(TemporalPosition temporalSenseEvent)
			{
				return temporalSenseEvent.getTimestamp();
			}
		});
		DataStream<IBusinessEvent> trivialMovingEvents = CEPBusinessPosition.toBusinessEventsStream(dataStream, new MovingTrivialPattern()).map(e -> e);
		trivialMovingEvents.print();
		DataStream<IBusinessEvent> conservativeMovingEvents = CEPBusinessPosition.toFlatBusinessEventsStream(trivialMovingEvents, new MovingConservativePattern());
		// conservativeMovingEvents.print();
		DataStream<IBusinessEvent> conservativeAndTrivialEvts = conservativeMovingEvents.union(trivialMovingEvents);

		DataStream<IBusinessEvent> startsMovingEvents = CEPBusinessPosition.toFlatBusinessEventsStream(conservativeAndTrivialEvts, new StartsMovingPattern());
		DataStream<IBusinessEvent> stopsMovingEvents = CEPBusinessPosition.toFlatBusinessEventsStream(conservativeAndTrivialEvts, new StopsMovingPattern());

		DataStream<IBusinessEvent> businessEventDataStream = conservativeMovingEvents.union(startsMovingEvents, stopsMovingEvents);

		businessEventDataStream.print();
		businessEventDataStream.addSink(new TestSink());

		try
		{
			env.execute("Simulated DataStream");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		Assert.assertEquals(1, TestSink.testObjects.size());

	}
}

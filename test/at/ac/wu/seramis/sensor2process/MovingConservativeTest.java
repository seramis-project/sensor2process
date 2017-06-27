package at.ac.wu.seramis.sensor2process;

import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.timestamps.AscendingTimestampExtractor;
import org.junit.Assert;
import org.junit.Test;

import at.ac.wu.seramis.sensor2process.cep.CEPBusinessPosition;
import at.ac.wu.seramis.sensor2process.cep.model.events.IBusinessEvent;
import at.ac.wu.seramis.sensor2process.cep.model.events.MovingConservative;
import at.ac.wu.seramis.sensor2process.cep.model.events.PositionEvent;
import at.ac.wu.seramis.sensor2process.cep.model.events.TemporalPosition;
import at.ac.wu.seramis.sensor2process.cep.model.patterns.MovingConservativePattern;
import at.ac.wu.seramis.sensor2process.cep.model.patterns.MovingTrivialPattern;
import at.ac.wu.seramis.sensor2process.sinks.TestSink;

public class MovingConservativeTest
{
	private static final String EPC = "epcTest2";

	@Test
	public void testConservativeMove() throws Exception
	{
		PositionEvent e1 = new PositionEvent();
		e1.setId(EPC);
		e1.setTimestamp(1477346400000l); // 10/25/2016 00:00:000
		e1.setCertainty(0.8);
		e1.setX(10);
		e1.setY(10);

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

		// moved 5 down 5 minutes later
		PositionEvent e4 = new PositionEvent();
		e4.setId(EPC);
		e4.setTimestamp(1477350600000l); // 10/25/2016 01:10:000
		e4.setCertainty(0.8);
		e4.setX(10);
		e4.setY(1);

		// moved 1 up, 5 minutes later
		PositionEvent e5 = new PositionEvent();
		e5.setId(EPC);
		e5.setTimestamp(1477350900000l); // 10/25/2016 01:15:000
		e5.setCertainty(0.8);
		e5.setX(11);
		e5.setY(1);

		// moved 1 up, bit later
		PositionEvent e6 = new PositionEvent();
		e6.setId(EPC);
		e6.setTimestamp(1477351000000l);
		e6.setCertainty(0.8);
		e6.setX(11);
		e6.setY(1);

		StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
		env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);

		DataStream<TemporalPosition> dataStream = env.fromElements(e1, e2, e3, e4, e5, e6).map(e -> (TemporalPosition) e).assignTimestampsAndWatermarks(new AscendingTimestampExtractor<TemporalPosition>()
		{
			@Override
			public long extractAscendingTimestamp(TemporalPosition temporalSenseEvent)
			{
				return temporalSenseEvent.getTimestamp();
			}
		});
		DataStream<IBusinessEvent> movingTrivial = CEPBusinessPosition.toBusinessEventsStream(dataStream, new MovingTrivialPattern());
		movingTrivial.print();
		DataStream<IBusinessEvent> conservativeMovingEvents = CEPBusinessPosition.toFlatBusinessEventsStream(movingTrivial, new MovingConservativePattern());
		conservativeMovingEvents.print();
		conservativeMovingEvents.addSink(new TestSink());
		try
		{
			env.execute("Simulated DataStream");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		Assert.assertEquals(4, TestSink.testObjects.size());
		Assert.assertTrue(((MovingConservative) TestSink.testObjects.get(0)).isMoving());
		Assert.assertTrue(((MovingConservative) TestSink.testObjects.get(1)).isMoving());
		Assert.assertFalse(((MovingConservative) TestSink.testObjects.get(2)).isMoving());
		Assert.assertFalse(((MovingConservative) TestSink.testObjects.get(3)).isMoving());

	}
}
